package mars.life;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;
import mars.TestData;
import org.junit.jupiter.api.Test;

/** Recipe costs (balance sheet 2d.3, life-support-spec 2.3): the data matches the sheet. */
class RecipeCostsTest {
  private static JsonObject costs() throws Exception {
    try (var r =
        Files.newBufferedReader(TestData.data("recipe_costs.json"), StandardCharsets.UTF_8)) {
      return JsonParser.parseReader(r).getAsJsonObject();
    }
  }

  private static Map<String, Integer> mat(JsonObject o) {
    Map<String, Integer> m = new LinkedHashMap<>();
    for (String k : new String[] {"Fe", "W", "G", "B"}) {
      m.put(k, o.get(k).getAsInt());
    }
    return m;
  }

  private static Map<String, Integer> cost(String id, String col) throws Exception {
    return mat(costs().getAsJsonObject("items").getAsJsonObject(id).getAsJsonObject(col));
  }

  @Test
  void standardCostsMatchTheBalanceSheetTable() throws Exception {
    assertEquals(Map.of("Fe", 6, "W", 3, "G", 2, "B", 0), cost("mars:oxygen_maker", "standard"));
    assertEquals(Map.of("Fe", 6, "W", 2, "G", 4, "B", 0), cost("mars:water_reclaimer", "standard"));
    assertEquals(Map.of("Fe", 6, "W", 4, "G", 8, "B", 0), cost("mars:solar_panel", "standard"));
    assertEquals(Map.of("Fe", 4, "W", 6, "G", 0, "B", 0), cost("mars:battery", "standard"));
    assertEquals(Map.of("Fe", 3, "W", 2, "G", 0, "B", 5), cost("mars:regolith_oven", "standard"));
    assertEquals(
        Map.of("Fe", 10, "W", 6, "G", 2, "B", 6), cost("mars:regolith_outgasser", "standard"));
    assertEquals(Map.of("Fe", 1, "W", 0, "G", 0, "B", 1), cost("mars:seal_kit", "standard"));
  }

  @Test
  void classroomIsTheStandardCostOverOnePointThreeRoundedUp() throws Exception {
    for (String id : costs().getAsJsonObject("items").keySet()) {
      if (id.equals("mars:hull_door")) {
        continue; // the spec table says 3 Fe; the rule gives 4 (flagged in the data and report)
      }
      var std = cost(id, "standard");
      var cls = cost(id, "classroom");
      for (String k : std.keySet()) {
        assertEquals((int) Math.ceil(std.get(k) / 1.3 - 1e-9), cls.get(k), id + " " + k);
      }
    }
  }

  @Test
  void everyPlayableRecipeCostsTheStandardCostInTheStandInMaterials() throws Exception {
    JsonObject c = costs();
    Map<String, String> mats = new LinkedHashMap<>();
    c.getAsJsonObject("materials")
        .entrySet()
        .forEach(e -> mats.put(e.getKey(), e.getValue().getAsString()));
    for (var idEl : c.getAsJsonArray("playableRecipes")) {
      String id = idEl.getAsString();
      String file = id.substring(id.indexOf(':') + 1);
      JsonObject recipe;
      try (var r =
          Files.newBufferedReader(
              TestData.data("recipe/" + file + ".json"), StandardCharsets.UTF_8)) {
        recipe = JsonParser.parseReader(r).getAsJsonObject();
      }
      assertEquals(id, recipe.getAsJsonObject("result").get("id").getAsString());
      Map<String, Integer> have = new LinkedHashMap<>();
      for (var ing : recipe.getAsJsonArray("ingredients")) {
        have.merge(ing.getAsJsonObject().get("item").getAsString(), 1, Integer::sum);
      }
      Map<String, Integer> want = new LinkedHashMap<>();
      cost(id, "standard")
          .forEach(
              (k, n) -> {
                if (n > 0) {
                  want.put(mats.get(k), n);
                }
              });
      assertEquals(want, have, id);
      assertTrue(have.values().stream().mapToInt(i -> i).sum() <= 9, "fits the 3 x 3 grid");
    }
  }

  @Test
  void theMachinesThatDoNotFitTheGridAreOnTheRecord() throws Exception {
    JsonObject c = costs();
    int tooBig = 0;
    for (String id : c.getAsJsonObject("items").keySet()) {
      int n = cost(id, "standard").values().stream().mapToInt(i -> i).sum();
      boolean playable = c.getAsJsonArray("playableRecipes").toString().contains(id);
      if (n > 9) {
        assertTrue(!playable, id + " has " + n + " items and cannot be a grid recipe");
        tooBig++;
      }
    }
    assertEquals(6, tooBig, "oxygen maker, reclaimer, panel, battery, Oven, Outgasser");
  }
}
