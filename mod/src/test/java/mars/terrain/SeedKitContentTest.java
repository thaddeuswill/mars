package mars.terrain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import mars.TestData;
import org.junit.jupiter.api.Test;

/**
 * The four seed kits (docs/design/seed-kit-recipes.md): recipes use the carbonate tag, amounts are
 * the Game Designer's INVENTED ones, and no kit text names a gas (forecast F8 guard, CR-F9).
 */
class SeedKitContentTest {
  private static final List<String> KITS =
      List.of("biocrust_kit", "meadow_kit", "wetland_kit", "forest_kit");

  /** Gas names and gas changes that the F8 forecast asks the player to predict first. */
  private static final Pattern GAS =
      Pattern.compile(
          "\\b(o2|co2|oxygen|carbon dioxide|carbon|nitrogen|methane|hydrogen|argon|gas|gases)\\b",
          Pattern.CASE_INSENSITIVE);

  private static JsonObject json(Path p) throws Exception {
    return JsonParser.parseString(Files.readString(p, StandardCharsets.UTF_8)).getAsJsonObject();
  }

  private static JsonObject lang() throws Exception {
    return json(TestData.pack().resolve("assets/mars/lang/en_us.json"));
  }

  @Test
  void kitTextNamesNoGas() throws Exception {
    JsonObject l = lang();
    for (String k : KITS) {
      for (String key : new String[] {"item.mars." + k, "item.mars." + k + ".tooltip"}) {
        assertTrue(l.has(key), key);
        String text = l.get(key).getAsString();
        assertFalse(GAS.matcher(text).find(), key + " names a gas: " + text);
      }
      assertTrue(
          l.get("item.mars." + k + ".tooltip").getAsString().endsWith("Game invention, not real."),
          k);
    }
  }

  @Test
  void theGuardActuallyCatchesGasWords() {
    for (String bad : new String[] {"Makes oxygen", "CO2 down", "adds O2", "more gases"}) {
      assertTrue(GAS.matcher(bad).find(), bad);
    }
  }

  @Test
  void recipesUseTheCarbonateTagBoneMealAndTheDesignersAmounts() throws Exception {
    for (String k : KITS) {
      JsonObject r = json(TestData.data("recipe/" + k + ".json"));
      assertEquals("minecraft:crafting_shapeless", r.get("type").getAsString(), k);
      assertEquals("mars:" + k, r.getAsJsonObject("result").get("id").getAsString(), k);
      String ing = r.getAsJsonArray("ingredients").toString();
      assertTrue(ing.contains("\"tag\":\"mars:seed_kit_carbonate\""), k + " uses the tag");
      assertEquals(1, count(ing, "minecraft:bone_meal"), k + " one bone meal");
      assertEquals(1, count(ing, "seed_kit_carbonate"), k + " one carbonate");
      assertFalse(ing.contains("\"item\":\"mars:carbonate\""), k + " must go through the tag");
    }
    assertEquals(3, count(ingredients("meadow_kit"), "minecraft:wheat_seeds"));
    assertEquals(2, count(ingredients("wetland_kit"), "minecraft:sugar_cane"));
    assertEquals(1, count(ingredients("wetland_kit"), "minecraft:water_bucket"));
    assertEquals(2, count(ingredients("forest_kit"), "minecraft:sweet_berries"));
    assertEquals(2, count(ingredients("forest_kit"), "minecraft:wheat_seeds"));
    assertEquals(2, count(ingredients("biocrust_kit"), "mars:washed_regolith"));
    assertEquals(1, count(ingredients("biocrust_kit"), "minecraft:water_bucket"));
  }

  @Test
  void theCarbonateTagHoldsTheCarbonateItem() throws Exception {
    JsonArray v = json(TestData.data("tags/item/seed_kit_carbonate.json")).getAsJsonArray("values");
    assertTrue(v.toString().contains("mars:carbonate"));
  }

  private static String ingredients(String kit) throws Exception {
    return json(TestData.data("recipe/" + kit + ".json")).getAsJsonArray("ingredients").toString();
  }

  private static int count(String haystack, String needle) {
    int n = 0;
    for (int i = haystack.indexOf(needle); i >= 0; i = haystack.indexOf(needle, i + 1)) {
      n++;
    }
    return n;
  }
}
