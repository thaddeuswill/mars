package mars.selftest;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mars.hazards.storm.StormSchedule;
import mars.machines.MarsMachines;
import mars.machines.SolarPanelBlock;
import mars.monitor.MonitorPayload;
import mars.monitor.MonitorSender;
import mars.planets.PlanetData;
import mars.planets.PlanetManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * S3-1: the dust storm with real blocks and the real command dispatcher. A started storm cuts a
 * real panel's supply, the Monitor shows the storm row and its cause line (and both survive the
 * wire), the end of the storm adds dust to the real panel block, a save and load keeps the
 * schedule, an older save without a schedule loads with a fresh one, and a source without operator
 * permission is refused.
 */
@GameTestHolder("mars")
@PrefixGameTestTemplate(false)
public final class StormGameTests {
  private static final double TOL = 1e-6;

  private StormGameTests() {}

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

  private static String rowLabel(ServerLevel level, String label) {
    return MonitorSender.viewFor(level).rows().stream()
        .filter(r -> r.labelKey().equals(label))
        .map(r -> r.value() + "|" + r.valueKey() + "|causes=" + r.causes().size())
        .findFirst()
        .orElse("none");
  }

  @GameTest(template = "empty", batch = "mars_storm")
  public static void stormOnRealPanelsAndCommands(GameTestHelper h) throws Exception {
    ServerLevel level = h.getLevel();
    PlanetData data = PlanetManager.resetForSelfTest(level);
    var sim = data.sim();
    CommandSourceStack op = level.getServer().createCommandSourceStack().withLevel(level);
    BlockPos panelPos = new BlockPos(6, 1, 6);
    h.setBlock(panelPos, MarsMachines.SOLAR_PANEL.get());
    h.setBlock(new BlockPos(0, 1, 0), MarsMachines.REGOLITH_OUTGASSER.get());
    BlockPos abs = h.absolutePos(panelPos);

    boolean ok =
        SelfTest.check(
            "storm.schedule_attached",
            "a new planet has a storm schedule, calm, first storm at sol 14, no storm row yet",
            "phase="
                + sim.storm().phase()
                + " start="
                + sim.storm().startSol()
                + " row="
                + rowLabel(level, "monitor.storm.label.warn"),
            sim.storm().phase() == StormSchedule.Phase.CALM
                && sim.storm().startSol() == 14.0
                && MonitorSender.viewFor(level).rows().stream()
                    .noneMatch(r -> r.labelKey().startsWith("monitor.storm")));
    double clear = sim.power().supplyPerSol();

    run(op, "mars storm status");
    run(op, "mars storm start");
    ok &=
        SelfTest.check(
            "storm.command_cuts_real_panel",
            "/mars storm start: supply is 40 % of 5 units, active, Monitor row says in progress",
            "supply="
                + sim.power().supplyPerSol()
                + " active="
                + sim.storm().active()
                + " row="
                + rowLabel(level, "monitor.storm.label.active"),
            Math.abs(sim.power().supplyPerSol() - clear * 0.4) < TOL
                && sim.storm().active()
                && rowLabel(level, "monitor.storm.label.active").endsWith("causes=1"));

    var view = MonitorSender.viewFor(level);
    RegistryFriendlyByteBuf buf =
        new RegistryFriendlyByteBuf(io.netty.buffer.Unpooled.buffer(), level.registryAccess());
    MonitorPayload.CODEC.encode(buf, new MonitorPayload(view));
    boolean same = MonitorPayload.CODEC.decode(buf).view().equals(view);
    ok &=
        SelfTest.check(
            "storm.row_survives_the_wire",
            "the storm row and its sunlight cause line encode and decode unchanged",
            "same=" + same,
            same);

    // Save and load while the storm is on.
    CompoundTag tag = data.save(new CompoundTag(), level.registryAccess());
    PlanetData back =
        PlanetData.load(
            level.dimension().location().toString(),
            PlanetManager.pace(level),
            PlanetManager.seed(level),
            tag,
            level.registryAccess());
    ok &=
        SelfTest.check(
            "storm.nbt_roundtrip",
            "an active storm survives an NBT save and load with the same schedule state",
            "state=" + back.sim().toState().storm(),
            back.sim().toState().equals(sim.toState())
                && back.sim().storm().active()
                && tag.getInt("schema") == 6);
    tag.remove("storm");
    tag.putInt("schema", 5);
    PlanetData old =
        PlanetData.load(
            level.dimension().location().toString(),
            PlanetManager.pace(level),
            PlanetManager.seed(level),
            tag,
            level.registryAccess());
    ok &=
        SelfTest.check(
            "storm.old_save_migrates",
            "a schema 5 save (no storm tag) loads calm, with a first storm ahead of its sol",
            "phase="
                + old.sim().storm().phase()
                + " start="
                + old.sim().storm().startSol()
                + " sol="
                + old.sim().model().sols(),
            old.sim().storm().phase() == StormSchedule.Phase.CALM
                && old.sim().storm().startSol() >= 14.0);

    run(op, "mars storm end");
    int stage = level.getBlockState(abs).getValue(SolarPanelBlock.DUST_STAGE);
    ok &=
        SelfTest.check(
            "storm.end_dusts_real_panel",
            "at the storm's end the panel block gains 2 dust stages and supply is 82 % of clear",
            "stage="
                + stage
                + " supply="
                + sim.power().supplyPerSol()
                + " row="
                + rowLabel(level, "monitor.storm.label.over"),
            stage == 2
                && Math.abs(sim.power().supplyPerSol() - clear * 0.82) < TOL
                && rowLabel(level, "monitor.storm.label.over")
                    .startsWith("|monitor.storm.value.over"));
    ok &=
        SelfTest.check(
            "storm.end_twice_is_harmless",
            "a second /mars storm end changes nothing",
            "stage=" + level.getBlockState(abs).getValue(SolarPanelBlock.DUST_STAGE),
            run(op, "mars storm end") == 1
                && level.getBlockState(abs).getValue(SolarPanelBlock.DUST_STAGE) == 2);

    CommandSourceStack student = op.withPermission(0);
    ok &=
        SelfTest.check(
            "storm.permission_guard",
            "a source without operator permission is refused by start, end and status",
            "refused",
            refused(student, "mars storm start")
                && refused(student, "mars storm end")
                && refused(student, "mars storm status"));

    h.destroyBlock(panelPos);
    h.destroyBlock(new BlockPos(0, 1, 0));
    PlanetManager.resetForSelfTest(level); // leave a fresh planet for the other tests
    if (ok) {
      h.succeed();
    } else {
      h.fail("stormOnRealPanelsAndCommands failed; see MARS-SELFTEST lines");
    }
  }
}
