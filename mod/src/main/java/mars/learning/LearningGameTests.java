package mars.learning;

import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import java.nio.file.Files;
import java.nio.file.Path;
import mars.dome.DomeSeals;
import mars.gates.GateTracker;
import mars.hull.SealChangedEvent;
import mars.planets.GateLatchedEvent;
import mars.planets.PlanetManager;
import mars.planets.SessionData;
import mars.selftest.SelfTest;
import mars.story.landing.LandingEvent;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder("mars")
@PrefixGameTestTemplate(false)
public final class LearningGameTests {
  private LearningGameTests() {}

  @GameTest(template = "empty", batch = "mars_learning")
  public static void onlyABreachWritesAPlanetEvent(GameTestHelper helper) throws Exception {
    var level = helper.getLevel();
    var server = level.getServer();
    LearningEvents.closeFor(server);
    var world = LearningWorldData.get(server);
    int session = SessionData.get(server).clock().sessionNumber();
    Path file =
        LearningEvents.dataDirectory(server)
            .resolve(world.id())
            .resolve("actionlog")
            .resolve(ActionLog.sessionName(session) + ".jsonl");
    int before = Files.exists(file) ? Files.readAllLines(file).size() : 0;
    NeoForge.EVENT_BUS.post(
        new SealChangedEvent(
            level, new DomeSeals.Change(0, DomeSeals.Status.UNKNOWN, DomeSeals.Status.SEALED, 12)));
    NeoForge.EVENT_BUS.post(
        new SealChangedEvent(
            level, new DomeSeals.Change(0, DomeSeals.Status.SEALED, DomeSeals.Status.LEAKING, 0)));
    LearningEvents.closeFor(server);

    var lines = Files.exists(file) ? Files.readAllLines(file) : java.util.List.<String>of();
    boolean exactlyOne = lines.size() == before + 1;
    boolean correct = false;
    if (exactlyOne) {
      var row = JsonParser.parseString(lines.get(before)).getAsJsonObject();
      correct =
          row.get("scope").getAsString().equals("planet")
              && row.get("player").isJsonNull()
              && row.get("type").getAsString().equals("failure.reported")
              && row.getAsJsonObject("data")
                  .get("failure")
                  .getAsString()
                  .equals("mars:seal_breach");
    }
    boolean ok =
        SelfTest.check(
            "learning.breach_planet_event",
            "only the breach writes a single planet-scoped event with no actor",
            "addedLines=" + (lines.size() - before) + " fieldsMatch=" + correct,
            exactlyOne && correct);
    var source = server.createCommandSourceStack();
    var mars = server.getCommands().getDispatcher().getRoot().getChild("mars");
    var learning = mars == null ? null : mars.getChild("learning");
    boolean guarded =
        learning != null
            && learning.getChild("export") != null
            && learning.canUse(source.withPermission(2))
            && !learning.canUse(source.withPermission(0));
    ok &=
        SelfTest.check(
            "learning.export_permission",
            "the registered learning export requires operator permission",
            "registeredAndGuarded=" + guarded,
            guarded);
    ok &= checkGameplayEvents(helper, file, lines.size());
    helper.assertTrue(ok, "learning event or export check failed");
    helper.succeed();
  }

  private static boolean checkGameplayEvents(GameTestHelper helper, Path file, int before)
      throws Exception {
    var level = helper.getLevel();
    var firstProfile = new GameProfile(new java.util.UUID(0, 711), "[LearningFirst]");
    var secondProfile = new GameProfile(new java.util.UUID(0, 712), "[LearningSecond]");
    var first = FakePlayerFactory.get(level, firstProfile);
    var second = FakePlayerFactory.get(level, secondProfile);
    NeoForge.EVENT_BUS.post(new LandingEvent(first, "landing.step", "landing_step_5_auto"));
    NeoForge.EVENT_BUS.post(new LandingEvent(second, "landing.skipped", "landing_all"));
    NeoForge.EVENT_BUS.post(new LandingEvent(first, "landing.done", "landing_done"));
    NeoForge.EVENT_BUS.post(new LandingEvent(first, "briefing.opened", "briefing_shift1"));
    NeoForge.EVENT_BUS.post(new LandingEvent(second, "journal.opened", "journal_contents"));

    var sim = PlanetManager.data(level).sim();
    var gates = sim.gates();
    var saved = gates.toStates();
    try {
      gates.restore(
          java.util.Map.of(
              "mars:dome_stable",
              new GateTracker.State(true, GateTracker.Cause.ADMIN, sim.model().simTicks(), 0, 0)));
      NeoForge.EVENT_BUS.post(new GateLatchedEvent(level, "mars:dome_stable"));
    } finally {
      gates.restore(saved);
    }
    LearningEvents.closeFor(level.getServer());

    var lines = Files.readAllLines(file);
    var added = lines.subList(before, lines.size());
    boolean verified = false;
    if (added.size() == 6) {
      var rows =
          added.stream().map(line -> JsonParser.parseString(line).getAsJsonObject()).toList();
      String player1 = rows.get(0).get("player").getAsString();
      String player2 = rows.get(1).get("player").getAsString();
      var gate = rows.get(5);
      verified =
          !player1.equals(player2)
              && rows.get(2).get("player").getAsString().equals(player1)
              && rows.get(3).get("player").getAsString().equals(player1)
              && rows.get(4).get("player").getAsString().equals(player2)
              && rows.stream()
                  .map(row -> row.get("type").getAsString())
                  .toList()
                  .equals(
                      java.util.List.of(
                          "landing.step",
                          "landing.skipped",
                          "landing.done",
                          "briefing.opened",
                          "journal.opened",
                          "gate.reached"))
              && rows.getFirst()
                  .getAsJsonObject("data")
                  .get("landing")
                  .getAsString()
                  .equals("mars:landing_step_5_auto")
              && gate.get("scope").getAsString().equals("planet")
              && gate.get("player").isJsonNull()
              && gate.get("cause").getAsString().equals("admin")
              && added.stream()
                  .noneMatch(
                      line ->
                          line.contains(firstProfile.getName())
                              || line.contains(secondProfile.getName())
                              || line.contains(firstProfile.getId().toString())
                              || line.contains(secondProfile.getId().toString()));
    }
    return SelfTest.check(
        "learning.landing_and_gate",
        "five Landing events use two pseudonyms; the admin gate is planet-scoped",
        "addedLines=" + added.size() + " fieldsMatch=" + verified,
        verified);
  }
}
