package mars;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;

/** The Mars sky draws mars:textures/environment/sun.png; it must exist and be 32 x 32. */
class SunTextureTest {
  @Test
  void sunTextureExistsAndIs32By32() throws Exception {
    Path f = TestData.pack().resolve("assets/mars/textures/environment/sun.png");
    assertTrue(Files.isRegularFile(f), "missing " + f);
    BufferedImage img = ImageIO.read(f.toFile());
    assertEquals(32, img.getWidth());
    assertEquals(32, img.getHeight());
  }

  @Test
  void skyEffectsPointAtTheMarsSunTexture() throws Exception {
    String src =
        Files.readString(
            TestData.pack()
                .getParent()
                .resolve("mod/src/main/java/mars/client/sky/MarsSkyEffects.java"));
    assertTrue(src.contains("\"mars\", \"textures/environment/sun.png\""));
  }
}
