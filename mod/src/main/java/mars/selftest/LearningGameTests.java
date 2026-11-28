package mars.selftest;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mars.core.Guidance;
import mars.monitor.MonitorSender;
import mars.monitor.view.MonitorView;
import mars.planets.LearningService;
import mars.planets.PlanetData;
import mars.planets.PlanetManager;
import mars.planets.PlayerSettingsData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * The journal and guidance (BACKLOG #49 / journal-and-guidance-minimum.md), through the real
 * command dispatcher: the gate latch unlocks three entries and sends one pointer line per player; a
 * player who joins later is told once; a non-operator may set their own guidance in standard play
 * and the classroom rules refuse expert; expert and an open pressure forecast remove the time
 * estimate for that player only.
 */
@GameTestHolder("mars")
@PrefixGameTestTemplate(false)
public final class LearningGameTests {
  private LearningGameTests() {}

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

  /** An online fake player (in the level's player list, so the pointer logic sees it). */
  private static ServerPlayer fake(ServerLevel level, String tail) {
    var p =
        net.neoforged.neoforge.common.util.FakePlayerFactory.get(
            level,
            new com.mojang.authlib.GameProfile(
                java.util.UUID
                    .randomUUID() /* fresh each run: settings persist in the test world */,
                "[Learn" + tail + "]"));
    if (!level.players().contains(p)) {
      level.addNewPlayer(p);
    }
    return p;
  }

  private static MonitorView.Row goalRow(MonitorView v) {
    return v.rows().stream()
        .filter(r -> r.labelKey().equals("monitor.row.next_goal"))
        .findFirst()
        .orElseThrow();
  }

  @GameTest(template = "empty", batch = "mars_learning")
  public static void journalAndGuidance(GameTestHelper h) throws Exception {
    ServerLevel level = h.getLevel();
    var server = level.getServer();
    PlanetData data = PlanetManager.resetForSelfTest(level);
    LearningService.openForecasts().clear();
    ServerPlayer p1 = fake(level, "000a");
    CommandSourceStack op = server.createCommandSourceStack().withLevel(level).withEntity(p1);
    CommandSourceStack student = op.withPermission(0);
    boolean ok =
        SelfTest.check(
            "journal.empty_before_latch",
            "no journal entries before Dome stable",
            "entries=" + LearningService.unlocked(level).size(),
            LearningService.unlocked(level).isEmpty());

    long before = LearningService.pointerLinesSent();
    int players = level.players().size();
    run(op, "mars gate latch dome_stable");
    long sent = LearningService.pointerLinesSent() - before;
    int listed = run(student, "mars journal");
    ok &=
        SelfTest.check(
            "journal.latch_pointer_and_list",
            "one pointer line per online player for the gate (not per entry), and /mars journal"
                + " lists 4 entries",
            "players=" + players + " pointerLines=" + sent + " listed=" + listed,
            sent == players && players >= 1 && listed == 4);

    // A second player who arrives after the latch is told once, and not again.
    long before2 = LearningService.pointerLinesSent();
    ServerPlayer p2 = fake(level, "000b");
    LearningService.pointerIfNew(p2);
    LearningService.pointerIfNew(p2);
    long joinLines = LearningService.pointerLinesSent() - before2;
    CommandSourceStack s2 = op.withEntity(p2).withPermission(0);
    run(s2, "mars journal 1");
    LearningService.pointerIfNew(p2);
    var settings = PlayerSettingsData.get(server).settings();
    int unread = settings.unread(p2.getUUID(), LearningService.unlocked(level)).size();
    ok &=
        SelfTest.check(
            "journal.late_joiner_once",
            "a late joiner gets exactly one pointer line, none after opening an entry; 3 of 4 stay"
                + " unread",
            "joinLines=" + joinLines + " unread=" + unread,
            joinLines == 1 && LearningService.pointerLinesSent() - before2 == 1 && unread == 3);

    // A rising pressure so the goal row has a time estimate.
    data.sim().addMachine("mars:solar_panel", 9_200_000_000_000L);
    data.sim().addMachine("mars:solar_panel", 9_200_000_000_001L);
    data.sim().addMachine("mars:solar_panel", 9_200_000_000_002L);
    data.sim().addMachine("mars:regolith_outgasser", 9_200_000_000_010L);
    data.sim().model().advanceSols(10);
    boolean etaForP1Before = goalRow(MonitorSender.viewFor(p1)).goal().etaKey() != null;

    // 9: a non-operator sets their own level in standard play; the Monitor has no ETA for them.
    run(student, "mars guidance expert");
    boolean expertNoEta =
        LearningService.effective(p1) == Guidance.EXPERT
            && goalRow(MonitorSender.viewFor(p1)).goal().etaKey() == null;
    boolean otherKeepsEta = goalRow(MonitorSender.viewFor(p2)).goal().etaKey() != null;
    boolean cannotSetOthers = refused(student, "mars guidance gentle " + p2.getScoreboardName());
    ok &=
        SelfTest.check(
            "guidance.self_set_expert_hides_eta",
            "a non-operator sets expert for themselves: their Monitor has no estimate, another"
                + " player's still has it, and they cannot set someone else's",
            "etaBefore="
                + etaForP1Before
                + " expertNoEta="
                + expertNoEta
                + " otherKeepsEta="
                + otherKeepsEta
                + " cannotSetOthers="
                + cannotSetOthers,
            etaForP1Before && expertNoEta && otherKeepsEta && cannotSetOthers);

    // Classroom rules (the planet is standard play in this build, so the rule is called directly).
    var teacherOnly =
        LearningService.setGuidance(server, p2.getUUID(), Guidance.GENTLE, true, false, "test");
    var refusedExpert =
        LearningService.setGuidance(server, p2.getUUID(), Guidance.EXPERT, true, true, "test");
    var teacherOk =
        LearningService.setGuidance(server, p2.getUUID(), Guidance.STANDARD, true, true, "test");
    ok &=
        SelfTest.check(
            "guidance.classroom_rules",
            "classroom: a player is told the teacher sets it; expert is refused even for an"
                + " operator; an operator may set gentle or standard",
            teacherOnly + " " + refusedExpert + " " + teacherOk,
            teacherOnly == LearningService.SetResult.TEACHER_ONLY
                && refusedExpert == LearningService.SetResult.EXPERT_REFUSED
                && teacherOk == LearningService.SetResult.OK
                && Guidance.effective(true, Guidance.EXPERT) == Guidance.GENTLE);

    // 10: debug forecast open hides ETA for that player only, and close restores it.
    run(student, "mars guidance standard");
    boolean nonOpRefused = refused(student, "mars debug forecast open");
    run(op, "mars debug forecast open");
    boolean hidden = goalRow(MonitorSender.viewFor(p1)).goal().etaKey() == null;
    boolean p2Unaffected = goalRow(MonitorSender.viewFor(p2)).goal().etaKey() != null;
    run(op, "mars debug forecast close");
    boolean restored = goalRow(MonitorSender.viewFor(p1)).goal().etaKey() != null;
    ok &=
        SelfTest.check(
            "forecast.debug_open_hides_eta_per_player",
            "debug forecast open removes the estimate for that player only (operator only) and"
                + " close restores it",
            "nonOpRefused="
                + nonOpRefused
                + " hidden="
                + hidden
                + " p2Unaffected="
                + p2Unaffected
                + " restored="
                + restored,
            nonOpRefused && hidden && p2Unaffected && restored);
    PlanetManager.resetForSelfTest(level); // leave a fresh planet for the other tests
    LearningService.openForecasts().clear();
    level.removePlayerImmediately(p1, net.minecraft.world.entity.Entity.RemovalReason.DISCARDED);
    level.removePlayerImmediately(p2, net.minecraft.world.entity.Entity.RemovalReason.DISCARDED);
    if (ok) {
      h.succeed();
    } else {
      h.fail("journalAndGuidance failed; see MARS-SELFTEST lines");
    }
  }
}
