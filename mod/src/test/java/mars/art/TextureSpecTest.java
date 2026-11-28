package mars.art;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javax.imageio.ImageIO;
import mars.TestData;
import org.junit.jupiter.api.Test;

/** Texture files from the Lead Artist: present, 16 x 16, valid alpha, terrain fully opaque. */
class TextureSpecTest {
  private static final List<String> TERRAIN =
      List.of(
          "basaltic_sand",
          "ripple_sand",
          "hematite_ore",
          "dark_sand_ore",
          "ground_ice",
          "duricrust",
          "vesicular_basalt",
          "spherule_sand",
          "impact_breccia",
          "gypsum_vein");
  private static final List<String> OVERLAYS =
      List.of(
          "made_up_band",
          "made_up_plate",
          "water_reclaimer_base",
          "water_reclaimer_side",
          "water_reclaimer_front",
          "water_reclaimer_frame",
          "water_reclaimer_canister",
          "water_reclaimer_top",
          "water_reclaimer_tube",
          "water_reclaimer_spout");

  private static BufferedImage load(String name) throws IOException {
    Path p = TestData.pack().resolve("assets/mars/textures/block/" + name + ".png");
    assertTrue(Files.exists(p), name + ": missing texture");
    return ImageIO.read(p.toFile());
  }

  @Test
  void terrainTexturesAre16x16AndOpaque() throws IOException {
    for (String n : TERRAIN) {
      BufferedImage img = load(n);
      assertEquals(16, img.getWidth(), n);
      assertEquals(16, img.getHeight(), n);
      for (int x = 0; x < 16; x++) {
        for (int y = 0; y < 16; y++) {
          assertEquals(255, img.getRGB(x, y) >>> 24, n + " alpha at " + x + "," + y);
        }
      }
    }
  }

  @Test
  void inventedMachineTexturesAre16x16() throws IOException {
    for (String n : OVERLAYS) {
      BufferedImage img = load(n);
      assertEquals(16, img.getWidth(), n);
      assertEquals(16, img.getHeight(), n);
    }
  }

  @Test
  void madeUpBandIsDashedTwoOnTwoOff() throws IOException {
    BufferedImage b = load("made_up_band");
    int purple = 0xCC79A7;
    for (int x = 0; x < 16; x++) {
      boolean on = (x / 2) % 2 == 0;
      int argb = b.getRGB(x, 14);
      assertEquals(on, (argb >>> 24) == 255 && (argb & 0xFFFFFF) == purple, "band dash at x=" + x);
      assertEquals(0, b.getRGB(x, 5) >>> 24, "band overlay must be clear above the band");
    }
  }
}
