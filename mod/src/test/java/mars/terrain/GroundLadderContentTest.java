package mars.terrain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import mars.TestData;
import org.junit.jupiter.api.Test;

/**
 * The staged ground ladder (raw regolith, washed regolith, rich soil, biocrust): every file is
 * valid, ids resolve, the recipes chain without a loop, the biocrust kit's tag now holds an item,
 * and every invented step carries an honest label.
 */
class GroundLadderContentTest {
  private static final List<String> NEW = List.of("washed_regolith", "rich_soil", "biocrust");

  private static JsonObject json(Path p) throws Exception {
    return JsonParser.parseString(Files.readString(p, StandardCharsets.UTF_8)).getAsJsonObject();
  }

  private static JsonObject lang() throws Exception {
    return json(TestData.pack().resolve("assets/mars/lang/en_us.json"));
  }

  private static List<String> ingredientIds(JsonObject recipe) {
    List<String> out = new ArrayList<>();
    for (JsonElement e : recipe.getAsJsonArray("ingredients")) {
      JsonObject o = e.getAsJsonObject();
      out.add(o.has("tag") ? "#" + o.get("tag").getAsString() : o.get("item").getAsString());
    }
    return out;
  }

  @Test
  void theLadderFileNamesFourStepsInOrderAndEveryNewIdHasItsFiles() throws Exception {
    JsonArray steps = json(TestData.data("ladder/ground_ladder.json")).getAsJsonArray("steps");
    assertEquals(4, steps.size());
    assertEquals("minecraft:red_sand", steps.get(0).getAsJsonObject().get("id").getAsString());
    for (int i = 0; i < NEW.size(); i++) {
      assertEquals(
          "mars:" + NEW.get(i), steps.get(i + 1).getAsJsonObject().get("id").getAsString());
    }
    Path assets = TestData.pack().resolve("assets/mars");
    for (String b : NEW) {
      for (String f :
          List.of(
              "blockstates/" + b + ".json",
              "models/block/" + b + ".json",
              "models/item/" + b + ".json")) {
        json(assets.resolve(f)); // valid JSON
      }
      json(TestData.data("loot_table/blocks/" + b + ".json"));
      BufferedImage img = ImageIO.read(assets.resolve("textures/block/" + b + ".png").toFile());
      assertEquals(16, img.getWidth(), b);
      assertEquals(16, img.getHeight(), b);
      assertTrue(
          json(TestData.data("../minecraft/tags/block/mineable/shovel.json"))
              .getAsJsonArray("values")
              .toString()
              .contains("mars:" + b),
          b + " is dug with a shovel");
    }
  }

  @Test
  void theRecipesChainFromRawToRichWithoutALoop() throws Exception {
    JsonObject washed = json(TestData.data("recipe/washed_regolith.json"));
    JsonObject rich = json(TestData.data("recipe/rich_soil.json"));
    assertEquals("minecraft:crafting_shapeless", washed.get("type").getAsString());
    assertEquals("minecraft:crafting_shapeless", rich.get("type").getAsString());
    assertEquals("mars:washed_regolith", washed.getAsJsonObject("result").get("id").getAsString());
    assertEquals("mars:rich_soil", rich.getAsJsonObject("result").get("id").getAsString());
    List<String> w = ingredientIds(washed);
    assertEquals(4, w.stream().filter("minecraft:red_sand"::equals).count());
    assertTrue(w.contains("minecraft:water_bucket"), "washing uses a bucket of water");
    List<String> r = ingredientIds(rich);
    assertTrue(r.contains("#mars:washed_regolith"));
    assertEquals(1, r.stream().filter("minecraft:bone_meal"::equals).count());
    // No loop: no mars recipe makes bone meal or red sand, so rich soil cannot feed the bone meal
    // that rich soil needs, and the first planter (washed regolith and water) never needs it.
    try (var files = Files.list(TestData.data("recipe"))) {
      for (Path f : files.filter(x -> x.toString().endsWith(".json")).toList()) {
        JsonObject o = json(f);
        if (!o.has("result") || !o.get("result").isJsonObject()) {
          continue;
        }
        String out = o.getAsJsonObject("result").get("id").getAsString();
        assertFalse(out.equals("minecraft:bone_meal") || out.equals("minecraft:red_sand"), f + "");
      }
    }
    assertFalse(w.contains("minecraft:bone_meal"), "washing needs no bone meal");
  }

  @Test
  void theWashedRegolithTagHoldsTheItemAndTheBiocrustKitUsesIt() throws Exception {
    JsonArray v = json(TestData.data("tags/item/washed_regolith.json")).getAsJsonArray("values");
    assertEquals(1, v.size());
    assertEquals("mars:washed_regolith", v.get(0).getAsString());
    JsonObject kit = json(TestData.data("recipe/biocrust_kit.json"));
    assertEquals(2, ingredientIds(kit).stream().filter("#mars:washed_regolith"::equals).count());
  }

  @Test
  void everyInventedStepIsLabelledHonestly() throws Exception {
    JsonObject l = lang();
    for (String b : NEW) {
      assertTrue(l.has("block.mars." + b), b);
      assertTrue(l.has("block.mars." + b + ".tooltip"), b + " tooltip");
    }
    String washed = l.get("block.mars.washed_regolith.tooltip").getAsString();
    assertTrue(
        washed.contains("game design") && washed.contains("not verified"),
        "perchlorate washing is labelled: " + washed);
    for (String b : List.of("rich_soil", "biocrust")) {
      assertTrue(
          l.get("block.mars." + b + ".tooltip").getAsString().contains("Made up for the game"), b);
    }
    // The biocrust text is visible before forecast F8 fires: it must name no gas (CR-F9).
    String all =
        l.get("block.mars.biocrust.tooltip").getAsString()
            + l.get("block.mars.rich_soil.tooltip").getAsString();
    assertFalse(
        all.toLowerCase().matches(".*\\b(o2|co2|oxygen|carbon dioxide|gas|gases)\\b.*"), all);
    for (String b : NEW) {
      String t = l.get("block.mars." + b + ".tooltip").getAsString();
      assertTrue(t.split("\\s+").length <= 35, b + " tooltip is short");
      assertFalse(t.contains("~") || t.contains("<"), b + " CR-T7b");
    }
  }
}
