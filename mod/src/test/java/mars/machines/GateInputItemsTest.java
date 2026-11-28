package mars.machines;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
  /**
   * The per-gate drop table of docs/design/recipe-inputs-ruling.md section 5 (all counts INVENTED),
   * in file order.
   */
  private static final Map<String, List<String>> DROPS =
      Map.of(
          "mars:dome_stable",
              List.of("mars:heater_coil=4", "minecraft:furnace=2", "minecraft:coal=16"),
          "mars:phase_2_begins",
              List.of(
                  "mars:filter_cartridge=3",
                  "mars:machine_core=6",
                  "minecraft:bucket=2",
                  "minecraft:coal=16"),
          "mars:thaw", List.of("mars:pump_bearing=2", "mars:machine_core=4"),
          "mars:hydrosphere", List.of("mars:resin_pack=4", "mars:machine_core=2"),
          "mars:phase_3_begins",
              List.of("mars:sorbent_bed=4", "mars:machine_core=4", "minecraft:glass=8"),
          "mars:breathable", List.of("mars:signal_board=5", "mars:machine_core=2"),
          "mars:equilibrium", List.of("mars:activation_cell=1"));

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
    int seen = 0;
    for (GateDef g : TestSims.gates()) {
      List<String> want = DROPS.get(g.id());
      assertTrue(want != null, g.id() + " is not in the drop table");
      List<String> have = g.cache().stream().map(c -> c.item() + "=" + c.count()).toList();
      assertEquals(want, have, g.id());
      assertEquals("gate.supply." + g.id().substring(5), g.supplyNoteKey());
      seen++;
    }
    assertEquals(DROPS.size(), seen, "every gate has a drop");
  }

  @Test
  void everyDropItemIsARegisteredItemOrVanilla() throws Exception {
    String src =
        Files.readString(
            Path.of("src/main/java/mars/machines/MarsMachines.java"), StandardCharsets.UTF_8);
    for (GateDef g : TestSims.gates()) {
      for (GateDef.CacheItem c : g.cache()) {
        if (c.item().startsWith("mars:")) {
          assertTrue(src.contains("\"" + c.item().substring(5) + "\""), c.item());
        } else {
          assertTrue(c.item().startsWith("minecraft:"), c.item());
        }
      }
    }
  }

  @Test
  void craftablePartsAreVanillaOnlyAndDropOnlyPartsHaveNoRecipe() throws Exception {
    JsonObject l = lang();
    for (String part : MarsMachinesIds.PARTS) {
      boolean dropOnly = MarsMachinesIds.DROP_ONLY.contains(part);
      assertEquals(
          !dropOnly, Files.exists(TestData.data("recipe/" + part + ".json")), part + " recipe");
      if (!dropOnly) {
        JsonObject r = recipe(part);
        assertTrue(r.getAsJsonObject("result").get("id").getAsString().equals("mars:" + part));
        for (String i : ingredients(r)) {
          assertTrue(i.startsWith("minecraft:"), part + " is made from vanilla items only: " + i);
        }
      } else {
        assertTrue(
            l.get("item.mars." + part + ".tooltip").getAsString().contains("sent from orbit"),
            part + " tooltip carries the honest label");
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
  void theResupplyRequestIsFourIronAndACoil() throws Exception {
    JsonObject r = recipe("resupply_request");
    assertEquals("minecraft:crafting_shapeless", r.get("type").getAsString());
    List<String> in = ingredients(r);
    assertEquals(5, in.size());
    assertEquals(4, in.stream().filter("minecraft:iron_ingot"::equals).count());
    assertEquals(1, in.stream().filter("mars:heater_coil"::equals).count());
    JsonObject l = lang();
    assertTrue(l.get("item.mars.resupply_request.tooltip").getAsString().contains("3 sols"));
    assertTrue(
        Files.exists(TestData.pack().resolve("assets/mars/textures/item/resupply_request.png")));
  }

  /**
   * Ruling section 7 rows 2 to 6: exact machine ingredients, and no vanilla hopper/furnace/bucket.
   */
  @Test
  void machineRecipesAreIronACorePlusTheGatePart() throws Exception {
    Map<String, Map<String, Long>> want =
        Map.of(
            "atmospheric_processor",
            Map.of(
                "minecraft:iron_ingot", 4L, "mars:machine_core", 1L, "mars:filter_cartridge", 1L),
            "super_greenhouse_emitter",
            Map.of("minecraft:iron_ingot", 4L, "mars:machine_core", 1L, "mars:heater_coil", 1L),
            "ice_extractor",
            Map.of("minecraft:iron_ingot", 5L, "mars:machine_core", 1L, "mars:pump_bearing", 1L),
            "carbon_scrubber",
            Map.of("minecraft:iron_ingot", 6L, "mars:machine_core", 1L, "mars:sorbent_bed", 2L),
            "regolith_outgasser",
            Map.of("minecraft:iron_ingot", 8L, "mars:heater_coil", 1L));
    JsonObject only = new JsonObject();
    assertEquals(0, only.size());
    for (var e : want.entrySet()) {
      JsonObject r = recipe(e.getKey());
      Map<String, Long> have = new java.util.TreeMap<>();
      if (r.has("pattern")) {
        Map<Character, String> key = new java.util.HashMap<>();
        for (var k : r.getAsJsonObject("key").entrySet()) {
          key.put(k.getKey().charAt(0), k.getValue().getAsJsonObject().get("item").getAsString());
        }
        for (var row : r.getAsJsonArray("pattern")) {
          for (char ch : row.getAsString().toCharArray()) {
            if (ch != ' ') {
              have.merge(key.get(ch), 1L, Long::sum);
            }
          }
        }
      }
      assertEquals(new java.util.TreeMap<>(e.getValue()), have, e.getKey());
      for (String banned :
          List.of(
              "minecraft:hopper",
              "minecraft:furnace",
              "minecraft:bucket",
              "minecraft:redstone",
              "minecraft:copper_ingot")) {
        assertFalse(have.containsKey(banned), e.getKey() + " still needs " + banned);
      }
    }
  }

  @Test
  void machineRecipesUseTheNewParts() throws Exception {
    assertTrue(ingredients(recipe("regolith_outgasser")).contains("mars:heater_coil"));
    assertTrue(ingredients(recipe("super_greenhouse_emitter")).contains("mars:heater_coil"));
    assertTrue(ingredients(recipe("atmospheric_processor")).contains("mars:filter_cartridge"));
    assertTrue(ingredients(recipe("ice_extractor")).contains("mars:pump_bearing"));
    assertTrue(ingredients(recipe("ice_extractor")).contains("mars:machine_core"));
  }

  @Test
  void noRecipeNeedsAnItemThatNothingCanMake() throws Exception {
    // Never hard-block: every mars: ingredient of every machine recipe is a part with its own
    // open vanilla-only recipe, or a drop-only part that a gate drop and the resupply request
    // always supply (orbital resupply, recipe-inputs-ruling.md).
    for (String m :
        List.of(
            "regolith_outgasser",
            "super_greenhouse_emitter",
            "atmospheric_processor",
            "ice_extractor",
            "algae_vat",
            "carbon_scrubber")) {
      for (String i : ingredients(recipe(m))) {
        if (i.startsWith("mars:") && !MarsMachinesIds.DROP_ONLY.contains(i.substring(5))) {
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
    keys.addAll(
        List.of("grant.refill", "grant.refill.note", "grant.refill.wait", "grant.refill.none"));
    assertEquals(DROPS.size() + 5, keys.size());
    // Forecast answers (pressure, temperature, gases, rates) never appear in these lines.
    java.util.regex.Pattern giveAway =
        java.util.regex.Pattern.compile(
            "pressure|kpa|temperature|warm|cool|oxygen|co2|carbon|rise|fall|sol\\b|sols\\b",
            java.util.regex.Pattern.CASE_INSENSITIVE);
    java.util.regex.Pattern timerOk =
        java.util.regex.Pattern.compile(
            "pressure|kpa|temperature|warm|cool|oxygen|co2|carbon|rise|fall",
            java.util.regex.Pattern.CASE_INSENSITIVE);
    for (String k : keys) {
      String v = l.get(k).getAsString();
      assertTrue(v.trim().split("\\s+").length <= 20, k + " is over 20 words (CR-T7c)");
      assertFalse(v.contains("~") || v.matches(".*<\\s*\\d.*"), k + " (CR-T7b)");
      // The request lines state the pass timer, so "sols" is allowed there (not a forecast).
      boolean timer = k.startsWith("grant.refill");
      assertFalse(
          (timer ? timerOk : giveAway).matcher(v).find(), k + " gives an answer away: " + v);
      if (k.startsWith("gate.supply.") || k.equals("grant.refill.note")) {
        assertTrue(v.contains("Sent from orbit"), k + " names the sender label");
        assertTrue(v.contains("Relay Orbiter"), k + " names the Relay Orbiter");
      }
      assertFalse(v.toLowerCase().contains("colour") || v.toLowerCase().contains("armour"), k);
    }
    for (String part : MarsMachinesIds.PARTS) {
      if (part.equals("sorbent_bed")) {
        continue; // design 3.5: the scrubber's whole job is CO2, so its bed may name the gas
      }
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
    static final List<String> PARTS =
        List.of(
            "heater_coil",
            "filter_cartridge",
            "pump_bearing",
            "sorbent_bed",
            "machine_core",
            "resin_pack",
            "signal_board",
            "activation_cell");
    static final List<String> DROP_ONLY =
        List.of("sorbent_bed", "machine_core", "resin_pack", "signal_board", "activation_cell");
  }

  @Test
  void partIdsMatchTheRegisteredList() throws Exception {
    String src =
        Files.readString(
            Path.of("src/main/java/mars/machines/MarsMachines.java"), StandardCharsets.UTF_8);
    for (String p : MarsMachinesIds.PARTS) {
      assertTrue(src.contains("\"" + p + "\""), p);
    }
    for (String p : MarsMachinesIds.DROP_ONLY) {
      assertTrue(src.contains("\"" + p + "\""), p);
    }
    assertTrue(src.contains("\"resupply_request\""));
  }
}
