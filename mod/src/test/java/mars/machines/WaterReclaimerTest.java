package mars.machines;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import mars.TestData;
import mars.TestSims;
import mars.index.sim.Pace;
import mars.index.sim.PlanetSim;
import mars.monitor.view.MonitorView;
import org.junit.jupiter.api.Test;

/** Water reclaimer: data, assets, lang, recipe, and the Monitor line. */
class WaterReclaimerTest {
  private static final String TYPE = "mars:water_reclaimer";

  private static JsonObject json(Path p) throws Exception {
    return JsonParser.parseString(Files.readString(p, StandardCharsets.UTF_8)).getAsJsonObject();
  }

  private static JsonObject lang() throws Exception {
    return json(TestData.pack().resolve("assets/mars/lang/en_us.json"));
  }

  @Test
  void drawComesFromDataAndChangesNoIndexRow() throws Exception {
    var def = TestSims.defs().get(TYPE);
    assertEquals(1.5, def.power().drawPerSol(), 1e-12);
    assertTrue(def.contributions().isEmpty());
    assertTrue(
        json(TestData.data("machines/water_reclaimer.json"))
            .get("source")
            .getAsString()
            .contains("INVENTED"));
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    s.addMachine(TYPE, 1L);
    assertEquals(1.5, s.power().demandPerSol(), 1e-12);
  }

  @Test
  void monitorListsItWithTheGridState() throws Exception {
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    s.addMachine(TYPE, 1L);
    MonitorView.Row p = power(MonitorView.of(s));
    assertEquals("block.mars.water_reclaimer", p.causes().get(0).sourceKey());
    assertEquals("monitor.cause.needs_power", p.causes().get(0).reasonKey());
    TestSims.withPanels(s, 1);
    p = power(MonitorView.of(s));
    assertEquals("monitor.cause.running", p.causes().get(0).reasonKey());
    assertEquals(1, p.causes().get(0).count());
  }

  private static MonitorView.Row power(MonitorView v) {
    return v.rows().stream()
        .filter(r -> r.labelKey().equals("monitor.row.power"))
        .findFirst()
        .orElseThrow();
  }

  @Test
  void langLootAndRecipeExist() throws Exception {
    JsonObject l = lang();
    assertEquals("Water Reclaimer", l.get("block.mars.water_reclaimer").getAsString());
    assertTrue(
        l.get("block.mars.water_reclaimer.tooltip")
            .getAsString()
            .startsWith("Game-sized version of a real idea"));
    assertTrue(l.has("monitor.cause.running"));
    assertTrue(l.has("monitor.event.placed.water_reclaimer"));
    assertTrue(
        Files.readString(TestData.data("loot_table/blocks/water_reclaimer.json"))
            .contains("mars:water_reclaimer"));
    JsonObject r = json(TestData.data("recipe/water_reclaimer.json"));
    assertEquals("minecraft:crafting_shaped", r.get("type").getAsString());
    assertEquals("mars:water_reclaimer", r.getAsJsonObject("result").get("id").getAsString());
    String keys = r.getAsJsonObject("key").toString();
    for (String item : new String[] {"iron_ingot", "glass", "copper_ingot", "bucket"}) {
      assertTrue(keys.contains("minecraft:" + item), item);
    }
  }
}
