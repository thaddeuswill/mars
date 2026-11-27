package mars.terrain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import mars.TestData;
import org.junit.jupiter.api.Test;

/** W1 terrain content: JSON is valid, every reference resolves, amounts live in data. */
class TerrainContentTest {
  private static final Set<String> TERRAIN_BLOCKS =
      Set.of(
          "basaltic_sand",
          "hematite_ore",
          "dark_sand_ore",
          "ground_ice",
          "duricrust",
          "vesicular_basalt",
          "spherule_sand",
          "impact_breccia",
          "gypsum_vein");

  /** Registry ids of the machines, the other mars blocks a terrain file could name. */
  private static final Set<String> OTHER_IDS = Set.of("mars", "planet");

  static JsonElement read(Path p) {
    try (Reader r = Files.newBufferedReader(p)) {
      return new Gson().fromJson(r, JsonElement.class);
    } catch (IOException e) {
      throw new IllegalStateException(p + ": " + e, e);
    }
  }

  private static List<Path> jsonFiles(Path dir) throws IOException {
    try (Stream<Path> s = Files.walk(dir)) {
      return s.filter(p -> p.toString().endsWith(".json")).toList();
    }
  }

  private static void strings(JsonElement e, List<String> out) {
    if (e.isJsonPrimitive() && e.getAsJsonPrimitive().isString()) {
      out.add(e.getAsString());
    } else if (e.isJsonArray()) {
      for (JsonElement c : e.getAsJsonArray()) {
        strings(c, out);
      }
    } else if (e.isJsonObject()) {
      for (var en : e.getAsJsonObject().entrySet()) {
        strings(en.getValue(), out);
      }
    }
  }

  private static boolean resolves(String id) {
    String name = id.substring("mars:".length());
    if (TERRAIN_BLOCKS.contains(name) || OTHER_IDS.contains(name)) {
      return true;
    }
    for (String kind :
        List.of(
            "noise",
            "density_function",
            "configured_feature",
            "placed_feature",
            "biome",
            "noise_settings")) {
      if (Files.exists(TestData.data("worldgen/" + kind + "/" + name + ".json"))) {
        return true;
      }
    }
    return false;
  }

  @Test
  void everyWorldgenFileIsValidJsonAndEveryMarsReferenceResolves() throws IOException {
    List<String> problems = new ArrayList<>();
    int files = 0;
    List<Path> all = new ArrayList<>(jsonFiles(TestData.data("worldgen")));
    all.add(TestData.data("dimension/planet_1.json"));
    for (Path p : all) {
      files++;
      List<String> strs = new ArrayList<>();
      strings(read(p), strs);
      for (String s : strs) {
        if (s.startsWith("mars:") && !resolves(s)) {
          problems.add(p.getFileName() + " references " + s);
        }
      }
    }
    System.out.println("terrain content: " + files + " worldgen files checked");
    assertTrue(problems.isEmpty(), problems.toString());
  }

  @Test
  void everyTerrainBlockHasItsFiles() {
    Path assets = TestData.pack().resolve("assets/mars");
    JsonObject lang = read(assets.resolve("lang/en_us.json")).getAsJsonObject();
    for (String b : TERRAIN_BLOCKS) {
      for (String f :
          List.of(
              "blockstates/" + b + ".json",
              "models/block/" + b + ".json",
              "models/item/" + b + ".json",
              "textures/block/" + b + ".png")) {
        assertTrue(Files.exists(assets.resolve(f)), b + ": missing " + f);
      }
      assertTrue(
          Files.exists(TestData.data("loot_table/blocks/" + b + ".json")), b + ": loot table");
      assertTrue(lang.has("block.mars." + b), b + ": lang key");
    }
    assertTrue(lang.has("itemGroup.mars.terrain"));
  }

  @Test
  void hematiteDropsRawIronNotItself() {
    String loot = read(TestData.data("loot_table/blocks/hematite_ore.json")).toString();
    assertTrue(loot.contains("minecraft:raw_iron"), "vanilla progression: hematite gives iron");
    assertTrue(loot.contains("silk_touch"));
  }

  @Test
  void darkSandOreSmeltsToVanillaGlass() {
    JsonObject r =
        read(TestData.data("recipe/smelting_dark_sand_ore_to_glass.json")).getAsJsonObject();
    assertEquals("minecraft:smelting", r.get("type").getAsString());
    assertEquals("mars:dark_sand_ore", r.getAsJsonObject("ingredient").get("item").getAsString());
    assertEquals("minecraft:glass", r.getAsJsonObject("result").get("id").getAsString());
  }

  @Test
  void oreAndIceAmountsAreDataAndNotZero() {
    for (String f : List.of("hematite_vein", "hematite_vein_rich", "dark_sand_vein")) {
      JsonObject placed =
          read(TestData.data("worldgen/placed_feature/" + f + ".json")).getAsJsonObject();
      JsonObject conf =
          read(TestData.data("worldgen/configured_feature/" + f + ".json")).getAsJsonObject();
      boolean count = false;
      for (JsonElement m : placed.getAsJsonArray("placement")) {
        if (m.getAsJsonObject().get("type").getAsString().equals("minecraft:count")) {
          count = m.getAsJsonObject().get("count").getAsInt() > 0;
        }
      }
      assertTrue(count, f + ": placement count");
      assertTrue(conf.getAsJsonObject("config").get("size").getAsInt() > 0, f + ": vein size");
    }
    JsonObject ice =
        read(TestData.data("worldgen/placed_feature/ground_ice_lens.json")).getAsJsonObject();
    String s = ice.toString();
    assertTrue(
        s.contains("rarity_filter") && s.contains("random_offset"), "ice lens depth is data");
    // Lens centre 6 to 15 below the heightmap position (the first air block); the disk reaches 1
    // below and 2 above its centre, so the ice top face is 3 to 12
    // blocks under the surface block (world-and-blocks.md 2.2, "3 to 12 blocks under the surface").
    assertTrue(s.contains("\"min_inclusive\":-15") && s.contains("\"max_inclusive\":-6"), s);
  }

  @Test
  void everyBiomeFeatureExistsAndTheThreeBiomesAreInTheDimension() throws IOException {
    JsonObject dim = read(TestData.data("dimension/planet_1.json")).getAsJsonObject();
    JsonArray biomes =
        dim.getAsJsonObject("generator").getAsJsonObject("biome_source").getAsJsonArray("biomes");
    assertEquals(3, biomes.size());
    for (JsonElement e : biomes) {
      String id = e.getAsJsonObject().get("biome").getAsString();
      JsonObject biome =
          read(TestData.data("worldgen/biome/" + id.substring(5) + ".json")).getAsJsonObject();
      assertEquals(11, biome.getAsJsonArray("features").size(), id + ": 11 decoration steps");
      List<String> feats = new ArrayList<>();
      strings(biome.get("features"), feats);
      for (String f : feats) {
        assertTrue(
            Files.exists(TestData.data("worldgen/placed_feature/" + f.substring(5) + ".json")),
            id + " names missing feature " + f);
      }
      // Real Mars is brown-orange everywhere: no grass or foliage tint keys on the biomes.
      assertTrue(!biome.getAsJsonObject("effects").has("grass_color"), id);
    }
  }

  @Test
  void windAndPadAndBaseHeightAgree() {
    TerrainParams p = LandformsTest.load();
    assertTrue(p.windTowardDeg() >= 0 && p.windTowardDeg() < 360, "wind in planet data");
    JsonObject h = read(TestData.data("worldgen/density_function/height.json")).getAsJsonObject();
    double base = h.get("argument1").getAsDouble();
    // The pad's top block sits at the average natural top block: base minus about one.
    assertTrue(
        Math.abs(p.pad().heightY() - (base - 1.0)) <= 1.0,
        "pad " + p.pad().heightY() + " base " + base);
    // The pad's world is inside the dimension type's height.
    JsonObject t = read(TestData.data("dimension_type/mars.json")).getAsJsonObject();
    JsonObject ns = read(TestData.data("worldgen/noise_settings/planet.json")).getAsJsonObject();
    assertEquals(t.get("min_y").getAsInt(), ns.getAsJsonObject("noise").get("min_y").getAsInt());
    assertEquals(t.get("height").getAsInt(), ns.getAsJsonObject("noise").get("height").getAsInt());
  }

  @Test
  void legacyFlatLayersMatchTheOldDimensionFile() {
    // Copied from the pre-W1 data/mars/dimension/planet_1.json: bedrock 1, stone 56, red_sandstone
    // 4, red_sand 3 from y 0.
    assertEquals(63, LegacyFlat.topY(0));
    assertEquals("minecraft:bedrock", LegacyFlat.blockAt(0, 0));
    assertEquals("minecraft:stone", LegacyFlat.blockAt(1, 0));
    assertEquals("minecraft:stone", LegacyFlat.blockAt(56, 0));
    assertEquals("minecraft:red_sandstone", LegacyFlat.blockAt(57, 0));
    assertEquals("minecraft:red_sandstone", LegacyFlat.blockAt(60, 0));
    assertEquals("minecraft:red_sand", LegacyFlat.blockAt(61, 0));
    assertEquals("minecraft:red_sand", LegacyFlat.blockAt(63, 0));
    assertEquals(null, LegacyFlat.blockAt(64, 0));
  }
}
