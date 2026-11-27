package mars.terrain;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Does this world folder already hold Mars data? Used once, when a world's Mars level first loads
 * and the world has no saved terrain mode. True means "a save from before W1": keep the flat slab.
 * Evidence: any region file under {@code dimensions/mars/}, or any {@code mars_*.dat} file in the
 * world's {@code data/} folder or in a Mars dimension's {@code data/} folder (the Mars mod writes
 * these as soon as a player has used it).
 */
public final class LegacyDetect {
  /** The marker file's own name must not count as evidence. */
  public static final String MARKER_FILE = "mars_terrain.dat";

  private LegacyDetect() {}

  public static boolean hasMarsData(Path worldRoot) {
    if (hasMarsDat(worldRoot.resolve("data"))) {
      return true;
    }
    Path marsDims = worldRoot.resolve("dimensions").resolve("mars");
    if (!Files.isDirectory(marsDims)) {
      return false;
    }
    try (DirectoryStream<Path> planets = Files.newDirectoryStream(marsDims)) {
      for (Path planet : planets) {
        if (hasMarsDat(planet.resolve("data")) || hasRegionFile(planet.resolve("region"))) {
          return true;
        }
      }
    } catch (IOException e) {
      // Cannot tell: refuse to change a world we cannot read (the safe answer is "keep flat").
      return true;
    }
    return false;
  }

  private static boolean hasMarsDat(Path dir) {
    if (!Files.isDirectory(dir)) {
      return false;
    }
    try (DirectoryStream<Path> files = Files.newDirectoryStream(dir, "mars_*.dat")) {
      for (Path f : files) {
        if (!f.getFileName().toString().equals(MARKER_FILE)) {
          return true;
        }
      }
    } catch (IOException e) {
      return true;
    }
    return false;
  }

  private static boolean hasRegionFile(Path dir) {
    if (!Files.isDirectory(dir)) {
      return false;
    }
    try (DirectoryStream<Path> files = Files.newDirectoryStream(dir, "*.mca")) {
      return files.iterator().hasNext();
    } catch (IOException e) {
      return true;
    }
  }
}
