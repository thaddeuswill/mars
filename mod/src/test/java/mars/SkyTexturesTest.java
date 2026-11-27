package mars;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;

/** The sky draw code binds mars:textures/environment/sun.png; it must exist and be 32x32. */
class SkyTexturesTest {
  @Test
  void sunTextureExistsAndIs32By32() throws Exception {
    Path sun = TestData.pack().resolve("assets/mars/textures/environment/sun.png");
    assertTrue(Files.isRegularFile(sun), "missing " + sun);
    var img = ImageIO.read(sun.toFile());
    System.out.println("sun.png " + img.getWidth() + "x" + img.getHeight());
    assertEquals(32, img.getWidth());
    assertEquals(32, img.getHeight());
  }

  @Test
  void skyCodeUsesTheModSunNotTheVanillaOne() throws Exception {
    String src = Files.readString(Path.of("src/main/java/mars/client/sky/MarsSkyEffects.java"));
    assertTrue(src.contains("fromNamespaceAndPath(\"mars\", \"textures/environment/sun.png\")"));
  }
}
