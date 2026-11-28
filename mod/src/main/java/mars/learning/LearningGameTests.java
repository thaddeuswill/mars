package mars.learning;

import com.google.gson.JsonParser;
import java.nio.file.Files;
import java.nio.file.Path;
import mars.dome.DomeSeals;
import mars.hull.SealChangedEvent;
import mars.planets.SessionData;
import mars.selftest.SelfTest;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.common.NeoForge;
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
    helper.assertTrue(ok, "seal breach or learning export permission check failed");
    helper.succeed();
  }
}
