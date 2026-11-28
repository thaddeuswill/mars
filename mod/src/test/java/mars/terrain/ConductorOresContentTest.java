package mars.terrain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import mars.TestData;
import mars.session.StarterChest;
import org.junit.jupiter.api.Test;

/** Conductor ores, carbonate and the starter chest (BACKLOG #49): data is valid and complete. */
class ConductorOresContentTest {
  private static final List<String> ORES =
      List.of("sulfide_vein", "magnetic_ore", "carbonate_rock");

  private static JsonObject lang() {
    return TerrainContentTest.read(TestData.pack().resolve("assets/mars/lang/en_us.json"))
        .getAsJsonObject();
  }

  private static JsonObject json(String rel) {
    return TerrainContentTest.read(TestData.data(rel)).getAsJsonObject();
  }

  @Test
  void everyOreHasLabelLootTagsAndPlacementData() {
    JsonObject lang = lang();
    String pickaxe = json("../minecraft/tags/block/mineable/pickaxe.json").toString();
    for (String b : ORES) {
      String tip = lang.get("block.mars." + b + ".tooltip").getAsString();
      assertTrue(tip.length() > 40, b + ": honest one-line label");
      assertTrue(
          tip.startsWith("Game invention") || tip.startsWith("Real, but game-sized"),
          b + ": label says invented or real-but-game-sized");
      assertTrue(pickaxe.contains("mars:" + b), b + ": mineable by pickaxe");
      assertTrue(Files.exists(TestData.data("loot_table/blocks/" + b + ".json")), b + ": loot");
      JsonObject placed = json("worldgen/placed_feature/" + b + ".json");
      JsonObject conf = json("worldgen/configured_feature/" + b + ".json");
      int chance = 0;
      for (JsonElement m : placed.getAsJsonArray("placement")) {
        JsonObject o = m.getAsJsonObject();
        if (o.get("type").getAsString().equals("minecraft:rarity_filter")) {
          chance = o.get("chance").getAsInt();
        }
      }
      assertTrue(chance >= 50, b + ": rare (rarity_filter chance in data), was " + chance);
      int size = conf.getAsJsonObject("config").get("size").getAsInt();
      assertTrue(size >= 3 && size <= 8, b + ": cluster size in data, was " + size);
    }
  }

  @Test
  void lootDropsTheVanillaItemsTheRecipesNeed() {
    assertTrue(
        json("loot_table/blocks/sulfide_vein.json").toString().contains("minecraft:raw_copper"));
    assertTrue(
        json("loot_table/blocks/magnetic_ore.json").toString().contains("minecraft:redstone"));
    assertTrue(json("loot_table/blocks/carbonate_rock.json").toString().contains("mars:carbonate"));
    assertTrue(
        json("loot_table/blocks/sulfide_vein.json").toString().contains("silk_touch"),
        "silk touch keeps the block");
    String stone = json("../minecraft/tags/block/needs_stone_tool.json").toString();
    String iron = json("../minecraft/tags/block/needs_iron_tool.json").toString();
    assertTrue(stone.contains("mars:sulfide_vein"), "copper-like: stone pickaxe");
    assertTrue(iron.contains("mars:magnetic_ore"), "redstone-like: iron pickaxe");
  }

  @Test
  void whereTheyGoFollowsTheScienceDoc() {
    String mesa = json("worldgen/biome/highland_mesa.json").toString();
    String flats = json("worldgen/biome/rocky_flats.json").toString();
    assertTrue(mesa.contains("mars:sulfide_vein") && mesa.contains("mars:magnetic_ore"));
    assertTrue(flats.contains("mars:sulfide_vein"));
    assertTrue(!flats.contains("mars:magnetic_ore"), "magnetic ore is old high ground only (SME)");
    // The rarity order: magnetic is rarer than copper-like, which is rarer than the 3-per-area
    // carbonate patches.
    assertTrue(chance("magnetic_ore") > chance("sulfide_vein"));
    assertTrue(chance("sulfide_vein") > chance("carbonate_rock"));
  }

  private static int chance(String name) {
    for (JsonElement m :
        json("worldgen/placed_feature/" + name + ".json").getAsJsonArray("placement")) {
      JsonObject o = m.getAsJsonObject();
      if (o.get("type").getAsString().equals("minecraft:rarity_filter")) {
        return o.get("chance").getAsInt();
      }
    }
    return 0;
  }

  @Test
  void carbonateItemTagAndGypsumFallbackAreInData() {
    assertTrue(json("tags/item/seed_kit_carbonate.json").toString().contains("mars:carbonate"));
    JsonObject notes = json("terrain/conductor_ores.json");
    assertEquals(
        "mars:gypsum_vein",
        notes.getAsJsonObject("seed_kit_carbonate").get("fallback_item").getAsString());
    for (String k : notes.getAsJsonObject("blocks").keySet()) {
      assertTrue(
          notes.getAsJsonObject("blocks").getAsJsonObject(k).get("status").getAsString().length()
              > 0);
    }
    assertTrue(lang().has("item.mars.carbonate"));
    assertTrue(Files.exists(TestData.pack().resolve("assets/mars/textures/item/carbonate.png")));
    assertTrue(Files.exists(TestData.pack().resolve("assets/mars/models/item/carbonate.json")));
  }

  @Test
  void rippleStripeBlockIsTheNewRippleSand() {
    JsonObject surface =
        json("planets/planet_1.json").getAsJsonObject("terrain").getAsJsonObject("surface");
    assertEquals("mars:ripple_sand", surface.get("ripple_block").getAsString());
  }

  @Test
  void starterChestIsFourCopperAndEightRedstoneWithLandingFriendlyText() throws IOException {
    Path p = TestData.data("terrain/starter_chest.json");
    StarterChest chest;
    try (Reader r = Files.newBufferedReader(p)) {
      chest = StarterChest.parse(r, p.toString());
    }
    assertEquals(
        List.of(
            new StarterChest.Entry("minecraft:copper_ingot", 4),
            new StarterChest.Entry("minecraft:redstone", 8)),
        chest.items());
    String msg = lang().get(chest.messageKey()).getAsString();
    int words = msg.trim().split("\\s+").length;
    assertTrue(words <= 20, "CR-T7c: " + words + " words");
    assertTrue(!msg.contains("~") && !msg.contains("<"), "CR-T7b");
    assertTrue(lang().has(chest.nameKey()));
  }

  @Test
  void parserRejectsAnEmptyOrOversizedChest() {
    for (String bad :
        List.of(
            "{\"name_key\":\"a\",\"message_key\":\"b\",\"items\":[]}",
            "{\"name_key\":\"a\",\"message_key\":\"b\",\"items\":[{\"id\":\"x\",\"count\":65}]}")) {
      boolean threw = false;
      try {
        StarterChest.parse(new java.io.StringReader(bad), "t");
      } catch (IllegalArgumentException e) {
        threw = true;
      }
      assertTrue(threw, bad);
    }
    JsonArray unused = new JsonArray();
    assertEquals(0, unused.size());
  }
}
