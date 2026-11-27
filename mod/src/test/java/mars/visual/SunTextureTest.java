package mars.visual;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import mars.TestData;
import org.junit.jupiter.api.Test;

class SunTextureTest {
  @Test
  void marsSunTextureDecodesAt32By32Pixels() throws IOException {
    Path texture = TestData.pack().resolve("assets/mars/textures/environment/sun.png");
    try (InputStream stream = Files.newInputStream(texture)) {
      BufferedImage image = ImageIO.read(stream);
      assertNotNull(image, "Could not decode " + texture);
      assertEquals(32, image.getWidth(), "Mars sun texture width");
      assertEquals(32, image.getHeight(), "Mars sun texture height");
    }
  }
}
