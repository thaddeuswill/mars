package mars;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;

/** The habitat furniture has what it needs: states, models, textures, items, loot, names. */
class HabitatAssetsTest {
  private static Path assets() {
    return TestData.pack().resolve("assets/mars");
  }

  @Test
  void bunkAndCrateHaveTheirFiles() throws Exception {
    for (String id : List.of("habitat_bunk", "stowage_crate")) {
      assertTrue(Files.exists(assets().resolve("blockstates/" + id + ".json")), id + " blockstate");
      assertTrue(Files.exists(assets().resolve("models/item/" + id + ".json")), id + " item model");
      assertTrue(
          Files.exists(TestData.pack().resolve("data/mars/loot_table/blocks/" + id + ".json")),
          id + " loot");
    }
    for (String m :
        List.of("habitat_bunk_foot", "habitat_bunk_head", "stowage_crate", "stowage_crate_open")) {
      assertTrue(Files.exists(assets().resolve("models/block/" + m + ".json")), m);
    }
    for (String t :
        List.of(
            "habitat_bunk_frame",
            "habitat_bunk_end",
            "habitat_bunk_top",
            "habitat_bunk_bag",
            "habitat_bunk_strap",
            "habitat_bunk_pillow",
            "stowage_crate_front",
            "stowage_crate_side",
            "stowage_crate_top",
            "stowage_crate_skid",
            "stowage_crate_inside")) {
      Path p = assets().resolve("textures/block/" + t + ".png");
      assertTrue(Files.exists(p), t);
      var img = ImageIO.read(p.toFile());
      assertEquals(16, img.getWidth(), t);
      assertEquals(16, img.getHeight(), t);
    }
  }

  @Test
  void bunkStateCoversEveryCombinationAndCrateEveryFacing() throws Exception {
    String bunk = Files.readString(assets().resolve("blockstates/habitat_bunk.json"));
    assertEquals(16, bunk.split("\"facing=").length - 1, "4 facings x occupied x 2 parts");
    String crate = Files.readString(assets().resolve("blockstates/stowage_crate.json"));
    assertEquals(12, crate.split("\"facing=").length - 1, "6 facings x open or closed");
  }

  @Test
  void bunkDropsOnlyFromOnePartAndHasNames() throws Exception {
    String loot =
        Files.readString(TestData.pack().resolve("data/mars/loot_table/blocks/habitat_bunk.json"));
    assertTrue(loot.contains("\"part\": \"head\""), "one part drops, like the vanilla bed");
    String lang = Files.readString(assets().resolve("lang/en_us.json"));
    assertTrue(lang.contains("\"block.mars.habitat_bunk\": \"Habitat Bunk\""));
    assertTrue(lang.contains("\"block.mars.stowage_crate\": \"Stowage Crate\""));
    assertTrue(lang.contains("\"block.mars.habitat_bench\": \"Work Bench\""));
  }
}
