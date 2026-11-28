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
          "gypsum_vein",
          "sulfide_vein",
          "magnetic_ore",
          "carbonate_rock");
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
          "water_reclaimer_spout",
          "atmospheric_processor_base",
          "atmospheric_processor_side",
          "atmospheric_processor_front",
          "atmospheric_processor_top",
          "atmospheric_processor_canister",
          "atmospheric_processor_port",
          "atmospheric_processor_tube",
          "atmospheric_processor_foot",
          "super_greenhouse_emitter_base",
          "super_greenhouse_emitter_tank",
          "super_greenhouse_emitter_front",
          "super_greenhouse_emitter_stack",
          "super_greenhouse_emitter_flare",
          "super_greenhouse_emitter_top");

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
  void carbonateItemIs16x16WithAChunkAndClearCorners() throws IOException {
    Path p = TestData.pack().resolve("assets/mars/textures/item/carbonate.png");
    assertTrue(Files.exists(p), "carbonate item texture missing");
    BufferedImage img = ImageIO.read(p.toFile());
    assertEquals(16, img.getWidth());
    assertEquals(16, img.getHeight());
    int solid = 0;
    for (int x = 0; x < 16; x++) {
      for (int y = 0; y < 16; y++) {
        int a = img.getRGB(x, y) >>> 24;
        assertTrue(a == 0 || a == 255, "carbonate item alpha must be 0 or 255");
        if (a == 255) solid++;
      }
    }
    assertEquals(0, img.getRGB(0, 0) >>> 24, "corner is clear");
    assertTrue(solid >= 40 && solid <= 80, "small chunk, not a full tile: " + solid);
  }

  @Test
  void bakedBandAndPlateOnNewMachines() throws IOException {
    int purple = 0xCC79A7;
    for (String n : List.of("atmospheric_processor_base", "super_greenhouse_emitter_base")) {
      BufferedImage b = load(n);
      assertEquals(purple, b.getRGB(0, 14) & 0xFFFFFF, n + " band dash on");
      assertTrue((b.getRGB(2, 14) & 0xFFFFFF) != purple, n + " band dash off");
    }
    assertEquals(purple, load("atmospheric_processor_front").getRGB(11, 9) & 0xFFFFFF);
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
