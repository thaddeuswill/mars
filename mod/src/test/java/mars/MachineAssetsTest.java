package mars;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import org.junit.jupiter.api.Test;

/**
 * Every registered machine has a blockstate, every model it uses exists, every texture those models
 * use exists (a missing one renders magenta in game), and each has an item model.
 */
class MachineAssetsTest {
  /** Must match mars.machines.MarsMachines (registry IDs are the contract). */
  static final List<String> IDS =
      List.of(
          "regolith_outgasser",
          "regolith_oven",
          "solar_panel",
          "battery",
          "oxygen_maker",
          "planetary_monitor");

  private static Path assets() {
    return TestData.pack().resolve("assets/mars");
  }

  private static JsonObject read(Path p) throws Exception {
    try (Reader r = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
      return JsonParser.parseReader(r).getAsJsonObject();
    }
  }

  private static Path model(String ref) {
    String path = ref.startsWith("mars:") ? ref.substring(5) : ref;
    return assets().resolve("models/" + path + ".json");
  }

  /** Checks a model, its mars-namespace parents, and its textures; returns problems found. */
  private static List<String> checkModel(String ref) throws Exception {
    List<String> problems = new ArrayList<>();
    if (!ref.startsWith("mars:")) {
      return problems; // vanilla parent such as block/block
    }
    Path p = model(ref);
    if (!Files.exists(p)) {
      problems.add("missing model " + ref);
      return problems;
    }
    JsonObject m = read(p);
    if (m.has("textures")) {
      for (Map.Entry<String, JsonElement> t : m.getAsJsonObject("textures").entrySet()) {
        String tex = t.getValue().getAsString();
        if (tex.startsWith("mars:")) {
          Path png = assets().resolve("textures/" + tex.substring(5) + ".png");
          if (!Files.exists(png)) {
            problems.add(ref + " texture " + t.getKey() + " -> missing " + tex);
          }
        }
      }
    }
    if (m.has("parent")) {
      problems.addAll(checkModel(m.get("parent").getAsString()));
    }
    return problems;
  }

  @Test
  void everyMachineHasBlockstateModelsTexturesAndItemModel() throws Exception {
    List<String> problems = new ArrayList<>();
    for (String id : IDS) {
      Path bs = assets().resolve("blockstates/" + id + ".json");
      if (!Files.exists(bs)) {
        problems.add(id + ": no blockstate");
        continue;
      }
      TreeSet<String> models = new TreeSet<>();
      for (Map.Entry<String, JsonElement> v : read(bs).getAsJsonObject("variants").entrySet()) {
        models.add(v.getValue().getAsJsonObject().get("model").getAsString());
      }
      for (String m : models) {
        problems.addAll(checkModel(m));
      }
      Path item = assets().resolve("models/item/" + id + ".json");
      if (!Files.exists(item)) {
        problems.add(id + ": no item model");
      } else {
        problems.addAll(checkModel(read(item).get("parent").getAsString()));
      }
      System.out.println(id + ": blockstate uses " + models);
    }
    assertTrue(problems.isEmpty(), "asset problems: " + problems);
  }

  @Test
  void canopyAndLootTablesExist() throws Exception {
    JsonObject v =
        read(assets().resolve("blockstates/solar_panel_canopy.json")).getAsJsonObject("variants");
    for (int ox = 0; ox < 3; ox++) {
      for (int oz = 0; oz < 3; oz++) {
        assertTrue(
            v.has("offset_x=" + ox + ",offset_z=" + oz), "canopy variant missing " + ox + "," + oz);
      }
    }
    List<String> problems = checkModel("mars:block/solar_panel_canopy");
    for (String id : IDS) {
      Path loot = TestData.data("loot_table/blocks/" + id + ".json");
      if (!Files.exists(loot)) {
        problems.add("no loot table for " + id);
      } else if (!read(loot).toString().contains("mars:" + id)) {
        problems.add("loot table for " + id + " doesn't drop mars:" + id);
      }
    }
    System.out.println(
        "canopy: 9 variants; loot tables checked for "
            + IDS.size()
            + " machines; problems "
            + problems);
    assertTrue(problems.isEmpty(), problems.toString());
  }

  @Test
  void solarPanelCoversEveryStateAndRetiredModelsAreGone() throws Exception {
    JsonObject v =
        read(assets().resolve("blockstates/solar_panel.json")).getAsJsonObject("variants");
    int n = 0;
    for (String facing : List.of("north", "east", "south", "west")) {
      for (int stage = 0; stage <= 4; stage++) {
        for (String pc : List.of("false", "true")) {
          String key = "dust_stage=" + stage + ",facing=" + facing + ",partly_cleaned=" + pc;
          assertTrue(v.has(key), "missing variant " + key);
          String model = v.getAsJsonObject(key).get("model").getAsString();
          String expected =
              pc.equals("true")
                  ? "mars:block/solar_panel_round_partly_cleaned"
                  : "mars:block/solar_panel_round_dust" + stage;
          assertEquals(expected, model, key);
          n++;
        }
      }
    }
    System.out.println("solar_panel: " + n + " variants, every one mapped as expected");
    assertEquals(n, v.size());
    for (String retired :
        List.of("models/block/solar_panel.json", "models/block/solar_panel_dusty.json")) {
      assertTrue(
          !Files.exists(assets().resolve(retired)),
          "retired batch 1 asset still present: " + retired);
    }
  }
}
