package mars.machines;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mars.TestData;
import mars.TestSims;
import mars.gates.GateDef;
import org.junit.jupiter.api.Test;

/**
 * The gate input items and the supply caches (docs/design/gate-input-items.md): data, recipes, text
 * and the cache fields of the gate files. All amounts are INVENTED. The block-side delivery is
 * checked in the boot checks (GameTests).
 */
class GateInputItemsTest {
  private static final Map<String, String> CACHE_GATE =
      Map.of(
          "mars:dome_stable", "mars:heater_coil",
          "mars:phase_2_begins", "mars:filter_cartridge",
          "mars:thaw", "mars:pump_bearing");
  private static final Map<String, Integer> CACHE_COUNT =
      Map.of("mars:heater_coil", 4, "mars:filter_cartridge", 3, "mars:pump_bearing", 2);

  private static JsonObject json(Path p) throws Exception {
    return JsonParser.parseString(Files.readString(p, StandardCharsets.UTF_8)).getAsJsonObject();
  }

  private static JsonObject lang() throws Exception {
    return json(TestData.pack().resolve("assets/mars/lang/en_us.json"));
  }

  private static JsonObject recipe(String name) throws Exception {
    return json(TestData.data("recipe/" + name + ".json"));
  }

  private static List<String> ingredients(JsonObject r) {
    List<String> out = new ArrayList<>();
    if (r.has("ingredients")) {
      for (var e : r.getAsJsonArray("ingredients")) {
        out.add(e.getAsJsonObject().get("item").getAsString());
      }
    } else {
      for (var e : r.getAsJsonObject("key").entrySet()) {
        out.add(e.getValue().getAsJsonObject().get("item").getAsString());
      }
    }
    return out;
  }

  @Test
  void cachesAreInTheGateFilesWithTheDesignAmounts() throws Exception {
    for (GateDef g : TestSims.gates()) {
      String item = CACHE_GATE.get(g.id());
      if (item == null) {
        assertTrue(g.cache().isEmpty(), g.id() + " has no cache in this slice");
        continue;
      }
      assertEquals(1, g.cache().size(), g.id());
      assertEquals(item, g.cache().get(0).item());
      assertEquals(CACHE_COUNT.get(item), g.cache().get(0).count(), g.id());
      assertEquals("gate.supply." + g.id().substring(5), g.supplyNoteKey());
    }
  }

  @Test
  void everyPartHasARecipeWithOnlyVanillaIngredientsAndAnIconAndALang() throws Exception {
    JsonObject l = lang();
    for (String part : MarsMachinesIds.PARTS) {
      JsonObject r = recipe(part);
      assertTrue(r.getAsJsonObject("result").get("id").getAsString().equals("mars:" + part));
      for (String i : ingredients(r)) {
        assertTrue(i.startsWith("minecraft:"), part + " is made from vanilla items only: " + i);
      }
      assertTrue(l.has("item.mars." + part) && l.has("item.mars." + part + ".tooltip"), part);
      assertTrue(
          Files.exists(TestData.pack().resolve("assets/mars/textures/item/" + part + ".png")));
      assertTrue(
          Files.exists(TestData.pack().resolve("assets/mars/models/item/" + part + ".json")));
    }
    // Amounts from the design (INVENTED): 2 iron -> 1 coil; ring of nuggets -> 2 bearings.
    assertEquals(
        List.of("minecraft:iron_ingot", "minecraft:iron_ingot"),
        ingredients(recipe("heater_coil")));
    assertEquals(2, recipe("pump_bearing").getAsJsonObject("result").get("count").getAsInt());
    List<String> fc = ingredients(recipe("filter_cartridge"));
    assertEquals(2, fc.stream().filter("minecraft:glass_pane"::equals).count());
    assertTrue(fc.contains("minecraft:iron_nugget"));
  }

  @Test
  void machineRecipesUseTheNewParts() throws Exception {
    assertTrue(ingredients(recipe("regolith_outgasser")).contains("mars:heater_coil"));
    assertTrue(ingredients(recipe("super_greenhouse_emitter")).contains("mars:heater_coil"));
    assertTrue(ingredients(recipe("atmospheric_processor")).contains("mars:filter_cartridge"));
    assertTrue(ingredients(recipe("ice_extractor")).contains("mars:pump_bearing"));
  }

  @Test
  void noRecipeNeedsAnItemThatNothingCanMake() throws Exception {
    // Never hard-block: every mars: ingredient of every machine recipe is a part with its own
    // open vanilla-only recipe, so the machines stay craftable from what can be mined.
    for (String m :
        List.of(
            "regolith_outgasser",
            "super_greenhouse_emitter",
            "atmospheric_processor",
            "ice_extractor")) {
      for (String i : ingredients(recipe(m))) {
        if (i.startsWith("mars:")) {
          assertTrue(
              Files.exists(TestData.data("recipe/" + i.substring(5) + ".json")),
              m + " needs " + i + " which has no recipe");
        }
      }
    }
  }

  @Test
  void iceExtractorIsRegisteredEverywhere() throws Exception {
    assertTrue(
        Files.readString(TestData.data("loot_table/blocks/ice_extractor.json"))
            .contains("mars:ice_extractor"));
    JsonObject bs = json(TestData.pack().resolve("assets/mars/blockstates/ice_extractor.json"));
    assertEquals(12, bs.getAsJsonObject("variants").size(), "4 facings x 3 machine states");
    JsonObject l = lang();
    assertEquals("Ice Extractor", l.get("block.mars.ice_extractor").getAsString());
    assertTrue(
        l.get("block.mars.ice_extractor.tooltip")
            .getAsString()
            .startsWith("Game-sized version of a real idea"));
  }

  @Test
  void supplyTextIsShortUsSpelledAndGivesNothingAway() throws Exception {
    JsonObject l = lang();
    List<String> keys = new ArrayList<>(List.of("grant.cache"));
    for (GateDef g : TestSims.gates()) {
      if (!g.cache().isEmpty()) {
        keys.add(g.supplyNoteKey());
      }
    }
    assertEquals(4, keys.size());
    // Forecast answers (pressure, temperature, gases, rates) never appear in these lines.
    java.util.regex.Pattern giveAway =
        java.util.regex.Pattern.compile(
            "pressure|kpa|temperature|warm|cool|oxygen|co2|carbon|rise|fall|sol\\b|sols\\b",
            java.util.regex.Pattern.CASE_INSENSITIVE);
    for (String k : keys) {
      String v = l.get(k).getAsString();
      assertTrue(v.trim().split("\\s+").length <= 20, k + " is over 20 words (CR-T7c)");
      assertFalse(v.contains("~") || v.matches(".*<\\s*\\d.*"), k + " (CR-T7b)");
      assertFalse(giveAway.matcher(v).find(), k + " gives an answer away: " + v);
      assertFalse(v.toLowerCase().contains("colour") || v.toLowerCase().contains("armour"), k);
    }
    for (String part : MarsMachinesIds.PARTS) {
      String tip = l.get("item.mars." + part + ".tooltip").getAsString();
      assertFalse(
          java.util.regex.Pattern.compile(
                  "co2|oxygen|carbon", java.util.regex.Pattern.CASE_INSENSITIVE)
              .matcher(tip)
              .find(),
          tip);
    }
  }

  /** The part ids, kept in sync with MarsMachines.PART_IDS by the next test. */
  static final class MarsMachinesIds {
    static final List<String> PARTS = List.of("heater_coil", "filter_cartridge", "pump_bearing");
  }

  @Test
  void partIdsMatchTheRegisteredList() throws Exception {
    String src =
        Files.readString(
            Path.of("src/main/java/mars/machines/MarsMachines.java"), StandardCharsets.UTF_8);
    for (String p : MarsMachinesIds.PARTS) {
      assertTrue(src.contains("\"" + p + "\""), p);
    }
    JsonArray none = new JsonArray();
    assertEquals(0, none.size());
  }
}
