package mars;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;

/** The hull blocks have what they need: blockstates, models, textures, items, loot, tags. */
class HullAssetsTest {
  private static Path assets() {
    return TestData.pack().resolve("assets/mars");
  }

  @Test
  void everyHullBlockHasItsFiles() throws Exception {
    for (String id : List.of("hull_plate", "hull_glass", "hull_door")) {
      assertTrue(Files.exists(assets().resolve("blockstates/" + id + ".json")), id + " blockstate");
      assertTrue(Files.exists(assets().resolve("models/item/" + id + ".json")), id + " item model");
      assertTrue(
          Files.exists(TestData.pack().resolve("data/mars/loot_table/blocks/" + id + ".json")),
          id + " loot");
    }
    for (String t :
        List.of(
            "block/hull_plate",
            "block/hull_glass",
            "block/hull_door_top",
            "block/hull_door_bottom",
            "item/hull_door")) {
      Path p = assets().resolve("textures/" + t + ".png");
      assertTrue(Files.exists(p), t);
      var img = ImageIO.read(p.toFile());
      assertEquals(16, img.getWidth(), t);
      assertEquals(16, img.getHeight(), t);
    }
  }

  @Test
  void theDoorHasAStateForEveryCombination() throws Exception {
    String s = Files.readString(assets().resolve("blockstates/hull_door.json"));
    int variants = s.split("\"facing=").length - 1;
    assertEquals(32, variants, "4 facings x 2 halves x 2 hinges x open or closed");
    for (String half : List.of("bottom", "top")) {
      for (String hinge : List.of("left", "right")) {
        for (String open : List.of("", "_open")) {
          assertTrue(
              Files.exists(
                  assets()
                      .resolve("models/block/hull_door_" + half + "_" + hinge + open + ".json")));
        }
      }
    }
  }

  @Test
  void theAirtightTagsExist() throws Exception {
    Path tags = TestData.pack().resolve("data/mars/tags/block");
    String airtight = Files.readString(tags.resolve("airtight.json"));
    assertTrue(airtight.contains("mars:hull_plate") && airtight.contains("mars:hull_glass"));
    assertTrue(Files.exists(tags.resolve("not_airtight.json")));
  }
}
