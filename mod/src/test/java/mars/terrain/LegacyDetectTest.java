package mars.terrain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** "New terrain for new worlds only": what counts as an existing Mars save. */
class LegacyDetectTest {
  private static void touch(Path p) throws IOException {
    Files.createDirectories(p.getParent());
    Files.writeString(p, "x");
  }

  @Test
  void aBrandNewWorldIsNotLegacy(@TempDir Path root) throws IOException {
    touch(root.resolve("level.dat"));
    touch(root.resolve("data/raids.dat"));
    touch(root.resolve("region/r.0.0.mca"));
    assertFalse(LegacyDetect.hasMarsData(root));
  }

  @Test
  void theMarkerFileAloneIsNotEvidence(@TempDir Path root) throws IOException {
    touch(root.resolve("data/" + LegacyDetect.MARKER_FILE));
    assertFalse(LegacyDetect.hasMarsData(root));
  }

  @Test
  void aWorldWithMarsRegionFilesIsLegacy(@TempDir Path root) throws IOException {
    touch(root.resolve("dimensions/mars/planet_1/region/r.0.0.mca"));
    assertTrue(LegacyDetect.hasMarsData(root));
  }

  @Test
  void aWorldWithMarsPlanetDataIsLegacy(@TempDir Path root) throws IOException {
    touch(root.resolve("dimensions/mars/planet_1/data/mars_planet.dat"));
    assertTrue(LegacyDetect.hasMarsData(root));
  }

  @Test
  void aWorldWithAnyMarsOverworldDataIsLegacy(@TempDir Path root) throws IOException {
    touch(root.resolve("data/mars_player_settings.dat"));
    assertTrue(LegacyDetect.hasMarsData(root));
  }

  @Test
  void anEmptyMarsFolderIsNotLegacy(@TempDir Path root) throws IOException {
    Files.createDirectories(root.resolve("dimensions/mars/planet_1/region"));
    assertFalse(LegacyDetect.hasMarsData(root));
  }

  @Test
  void modeIdsRoundTripAndUnknownIsNull() {
    for (TerrainMode m : TerrainMode.values()) {
      assertEquals(m, TerrainMode.fromId(m.id()));
    }
    assertNull(TerrainMode.fromId("something_else"));
  }
}
