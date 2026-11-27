package mars.selftest;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import mars.core.Ids;
import mars.planets.CheckpointData;
import mars.planets.PlanetData;
import mars.planets.PlanetManager;
import mars.planets.SessionControl;
import mars.planets.SessionData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * Slice 8: the operator commands, run through the real command dispatcher (so the permission guard
 * is part of what is tested): checkpoint save, list and restore, restore to the start, the machine
 * list, the planet report file, the automatic checkpoint at session start, and refusal for a source
 * without operator permission.
 */
@GameTestHolder("mars")
@PrefixGameTestTemplate(false)
public final class AdminGameTests {
  private AdminGameTests() {}

  private static int run(CommandSourceStack src, String cmd) throws CommandSyntaxException {
    return src.getServer().getCommands().getDispatcher().execute(cmd, src);
  }

  private static boolean refused(CommandSourceStack src, String cmd) {
    try {
      run(src, cmd);
      return false;
    } catch (CommandSyntaxException e) {
      return true;
    }
  }

  @GameTest(template = "empty", batch = "mars_admin")
  public static void operatorCommandsRollBackAndReport(GameTestHelper h) throws Exception {
    ServerLevel level = h.getLevel();
    var server = level.getServer();
    PlanetData data = PlanetManager.resetForSelfTest(level);
    data.sim().addMachine("mars:solar_panel", 9_100_000_000_000L);
    data.sim().addMachine("mars:regolith_outgasser", 9_100_000_000_001L);
    CommandSourceStack op = server.createCommandSourceStack().withLevel(level);
    CommandSourceStack student = op.withPermission(0);

    data.sim().model().advanceSols(1);
    double pressureThen = data.sim().model().snapshot().get(Ids.PRESSURE);
    run(op, "mars checkpoint save before");
    boolean ok =
        SelfTest.check(
            "admin.checkpoint_saved",
            "checkpoint 'before' is stored for the planet",
            "list=" + mars.admin.AdminCommands.listText(level).replace('\n', '|'),
            CheckpointData.get(server)
                .of(level.dimension().location().toString())
                .get("before")
                .isPresent());

    data.sim().model().advanceSols(5);
    data.sim().addMachine("mars:regolith_outgasser", 9_100_000_000_002L);
    run(op, "mars gate latch phase_2_begins");
    double pressureLater = data.sim().model().snapshot().get(Ids.PRESSURE);
    run(op, "mars checkpoint restore before");
    PlanetData back = PlanetManager.data(level);
    double pressureBack = back.sim().model().snapshot().get(Ids.PRESSURE);
    ok &=
        SelfTest.check(
            "admin.restore_values",
            "restore puts pressure back to the checkpoint value and un-does the later gate latch",
            "then="
                + pressureThen
                + " later="
                + pressureLater
                + " restored="
                + pressureBack
                + " latched="
                + back.sim().gates().isLatched("mars:phase_2_begins"),
            Math.abs(pressureBack - pressureThen) < 1e-9
                && pressureLater > pressureThen
                && !back.sim().gates().isLatched("mars:phase_2_begins"));
    ok &=
        SelfTest.check(
            "admin.restore_keeps_machines",
            "blocks are not rolled back: the second Outgasser placed after the checkpoint stays",
            "outgassers=" + back.sim().machineCount("mars:regolith_outgasser"),
            back.sim().machineCount("mars:regolith_outgasser") == 2);
    String machines = mars.admin.AdminCommands.machinesText(level);
    ok &=
        SelfTest.check(
            "admin.machine_list",
            "machine list names both types with counts",
            machines.replace('\n', '|'),
            machines.contains("mars:regolith_outgasser x2")
                && machines.contains("mars:solar_panel x1"));
    run(op, "mars checkpoint restore start");
    PlanetData start = PlanetManager.data(level);
    ok &=
        SelfTest.check(
            "admin.restore_start",
            "restore start returns pressure to 0.6 kPa and sol 0, machines kept",
            "pressure="
                + start.sim().model().snapshot().get(Ids.PRESSURE)
                + " ticks="
                + start.sim().model().simTicks()
                + " outgassers="
                + start.sim().machineCount("mars:regolith_outgasser"),
            Math.abs(start.sim().model().snapshot().get(Ids.PRESSURE) - 0.6) < 1e-9
                && start.sim().model().simTicks() == 0
                && start.sim().machineCount("mars:regolith_outgasser") == 2);
    ok &=
        SelfTest.check(
            "admin.unknown_checkpoint_refused",
            "restoring a name that does not exist fails with a message and changes nothing",
            "refused=" + refused(op, "mars checkpoint restore nope"),
            refused(op, "mars checkpoint restore nope"));
    ok &=
        SelfTest.check(
            "admin.reserved_name",
            "'start' cannot be used as a checkpoint name",
            "refused=" + refused(op, "mars checkpoint save start"),
            refused(op, "mars checkpoint save start"));
    boolean allRefused =
        refused(student, "mars checkpoint save x")
            && refused(student, "mars checkpoint list")
            && refused(student, "mars checkpoint restore before")
            && refused(student, "mars machines")
            && refused(student, "mars index export");
    ok &=
        SelfTest.check(
            "admin.permission_guard",
            "a source without operator permission is refused by every new command",
            "allRefused="
                + allRefused
                + " pressure="
                + PlanetManager.data(level).sim().model().snapshot().get(Ids.PRESSURE),
            allRefused);

    Path dir = Files.createTempDirectory("mars-report");
    Path file = mars.admin.AdminCommands.export(level, "gametest", dir);
    String text = Files.readString(file);
    ok &=
        SelfTest.check(
            "admin.export_report",
            "the planet report file exists and names the planet, gates and machines",
            file.getFileName() + " " + text.length() + " chars",
            text.contains("Planet: ")
                && text.contains("Gates:")
                && text.contains("mars:regolith_outgasser: 2"));
    Files.deleteIfExists(file);
    Files.deleteIfExists(dir);

    CheckpointData loaded =
        CheckpointData.load(
            CheckpointData.get(server).save(new CompoundTag(), level.registryAccess()),
            level.registryAccess());
    ok &=
        SelfTest.check(
            "admin.checkpoint_nbt_round_trip",
            "checkpoints survive an NBT save and load",
            "before present="
                + loaded.of(level.dimension().location().toString()).get("before").isPresent(),
            loaded.of(level.dimension().location().toString()).get("before").isPresent());

    var clock = SessionData.get(server).clock();
    clock.pause();
    SessionControl.resume(server, "gametest");
    String auto = "session-" + clock.sessionNumber();
    ok &=
        SelfTest.check(
            "admin.auto_checkpoint_on_resume",
            "resuming a paused session takes a checkpoint named " + auto,
            "present="
                + CheckpointData.get(server)
                    .of(level.dimension().location().toString())
                    .get(auto)
                    .isPresent(),
            CheckpointData.get(server)
                .of(level.dimension().location().toString())
                .get(auto)
                .isPresent());
    if (ok) {
      h.succeed();
    } else {
      h.fail("operatorCommandsRollBackAndReport failed; see MARS-SELFTEST lines");
    }
  }
}
