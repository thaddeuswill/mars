package mars;

import java.nio.file.Files;
import java.nio.file.Path;

/** Locates the real content in ../pack (passed by Gradle as -Dmars.packDir). */
public final class TestData {
  private TestData() {}

  public static Path pack() {
    String dir = System.getProperty("mars.packDir", "../pack");
    Path p = Path.of(dir);
    if (!Files.isDirectory(p)) {
      throw new IllegalStateException("pack directory not found: " + p.toAbsolutePath());
    }
    return p;
  }

  public static Path data(String relative) {
    return pack().resolve("data/mars").resolve(relative);
  }
}
