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
          "super_greenhouse_emitter_top",
          "ice_extractor_base",
          "ice_extractor_side",
          "ice_extractor_front",
          "ice_extractor_front_off",
          "ice_extractor_front_running",
          "ice_extractor_leg",
          "ice_extractor_heater",
          "ice_extractor_sleeve",
          "ice_extractor_top",
          "ice_extractor_pump",
          "ice_extractor_tank",
          "ice_extractor_tank_top",
          "ice_extractor_hose");

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

  @Test
  void oxygenMakerBakesBandOnFourSidesAndPlateOnFront() throws IOException {
    int purple = 0xCC79A7;
    int dark = 0x8E3F6C;
    // used x ranges: front/back 2..13, end/side 3..12; dashes follow the global 2 on / 2 off
    // pattern
    for (String n :
        List.of(
            "o2_generator_face", "o2_generator_back", "o2_generator_end", "o2_generator_side")) {
      BufferedImage b = load(n);
      assertEquals(16, b.getWidth(), n);
      assertEquals(purple, b.getRGB(4, 14) & 0xFFFFFF, n + " dash on, row 14");
      assertEquals(dark, b.getRGB(4, 15) & 0xFFFFFF, n + " dash edge, row 15");
      assertTrue((b.getRGB(6, 14) & 0xFFFFFF) != purple, n + " dash off");
    }
    BufferedImage f = load("o2_generator_face");
    assertEquals(purple, f.getRGB(11, 9) & 0xFFFFFF, "plate");
    assertEquals(0x2A2420, f.getRGB(10, 12) & 0xFFFFFF, "plate slash bottom-left");
    assertEquals(0x2A2420, f.getRGB(13, 9) & 0xFFFFFF, "plate slash top-right");
  }

  @Test
  void seedKitIconsAreDistinctNoGreenAndClearCornered() throws IOException {
    List<String> kits = List.of("biocrust_kit", "meadow_kit", "wetland_kit", "forest_kit");
    List<byte[]> seen = new java.util.ArrayList<>();
    for (String n : kits) {
      Path p = TestData.pack().resolve("assets/mars/textures/item/" + n + ".png");
      assertTrue(Files.exists(p), n + ": missing icon");
      BufferedImage img = ImageIO.read(p.toFile());
      assertEquals(16, img.getWidth(), n);
      assertEquals(16, img.getHeight(), n);
      assertEquals(0, img.getRGB(0, 0) >>> 24, n + " corner is clear");
      for (int x = 0; x < 16; x++) {
        for (int y = 0; y < 16; y++) {
          int argb = img.getRGB(x, y);
          int a = argb >>> 24;
          assertTrue(a == 0 || a == 255, n + " alpha");
          int r = (argb >> 16) & 255;
          int g = (argb >> 8) & 255;
          int b = argb & 255;
          assertTrue(a == 0 || !(g > r + 20 && g > b + 20), n + " has green at " + x + "," + y);
        }
      }
      byte[] bytes = Files.readAllBytes(p);
      for (byte[] other : seen) assertTrue(!java.util.Arrays.equals(bytes, other), n + " distinct");
      seen.add(bytes);
      String model =
          Files.readString(TestData.pack().resolve("assets/mars/models/item/" + n + ".json"));
      assertTrue(model.contains("mars:item/" + n), n + " model points at its own icon");
    }
  }

  /** Animated machine textures (running states): strip of 16 x 16 frames plus a valid .mcmeta. */
  private static final List<String> ANIMATED =
      List.of(
          "regolith_oven_front_running",
          "regolith_outgasser_puff",
          "o2_generator_face_running",
          "water_reclaimer_front_running",
          "atmospheric_processor_canister_running",
          "super_greenhouse_emitter_flare_running",
          "ice_extractor_sleeve_running",
          "ice_extractor_steam");

  @Test
  void animatedTexturesHaveValidMcmetaAndFrameHeights() throws IOException {
    for (String n : ANIMATED) {
      BufferedImage img = load(n);
      assertEquals(16, img.getWidth(), n + " frame width");
      assertEquals(0, img.getHeight() % 16, n + " height is a whole number of frames");
      int frames = img.getHeight() / 16;
      assertTrue(frames >= 2 && frames <= 8, n + " cheap: 2 to 8 frames, got " + frames);
      Path meta = TestData.pack().resolve("assets/mars/textures/block/" + n + ".png.mcmeta");
      assertTrue(Files.exists(meta), n + ": missing .mcmeta");
      com.google.gson.JsonObject anim =
          com.google.gson.JsonParser.parseString(Files.readString(meta))
              .getAsJsonObject()
              .getAsJsonObject("animation");
      int ft = anim.get("frametime").getAsInt();
      assertTrue(ft >= 8 && ft <= 40, n + " frametime 8..40 ticks (no flicker): " + ft);
      assertTrue(
          !anim.has("interpolate") || !anim.get("interpolate").getAsBoolean(),
          n + " no interpolation (pixel steps)");
      if (anim.has("frames")) {
        for (var f : anim.getAsJsonArray("frames")) {
          int idx = f.isJsonObject() ? f.getAsJsonObject().get("index").getAsInt() : f.getAsInt();
          assertTrue(idx >= 0 && idx < frames, n + " frame index " + idx);
        }
      }
      // it must actually move: frame 0 differs from at least one other frame
      boolean moves = false;
      for (int k = 1; k < frames && !moves; k++) {
        for (int x = 0; x < 16 && !moves; x++) {
          for (int y = 0; y < 16 && !moves; y++) {
            moves = img.getRGB(x, 16 * k + y) != img.getRGB(x, y);
          }
        }
      }
      assertTrue(moves, n + " frames are all identical");
    }
  }

  @Test
  void animatedFramesKeepTheMadeUpBandAndPlate() throws IOException {
    for (String n : List.of("o2_generator_face_running", "water_reclaimer_front_running")) {
      BufferedImage strip = load(n);
      BufferedImage still =
          load(
              n.equals("o2_generator_face_running")
                  ? "o2_generator_face"
                  : "water_reclaimer_front");
      for (int k = 0; k < strip.getHeight() / 16; k++) {
        for (int x = 0; x < 16; x++) {
          for (int y = 9; y < 16; y++) { // plate rows and band rows
            if (x < 9 && n.startsWith("water")) continue; // water window region
            assertEquals(
                still.getRGB(x, y),
                strip.getRGB(x, 16 * k + y),
                n + " frame " + k + " " + x + "," + y);
          }
        }
      }
    }
  }

  @Test
  void iceExtractorBakesBandPlateAndHasNoGreen() throws IOException {
    int purple = 0xCC79A7;
    BufferedImage b = load("ice_extractor_base");
    assertEquals(purple, b.getRGB(0, 14) & 0xFFFFFF, "band dash on");
    assertEquals(0x8E3F6C, b.getRGB(0, 15) & 0xFFFFFF, "band dash edge");
    assertTrue((b.getRGB(2, 14) & 0xFFFFFF) != purple, "band dash off");
    for (String n :
        List.of("ice_extractor_front", "ice_extractor_front_off", "ice_extractor_front_running")) {
      BufferedImage f = load(n);
      assertEquals(purple, f.getRGB(10, 4) & 0xFFFFFF, n + " plate");
      assertEquals(0x2A2420, f.getRGB(9, 6) & 0xFFFFFF, n + " plate slash bottom-left");
      assertEquals(0x2A2420, f.getRGB(12, 3) & 0xFFFFFF, n + " plate slash top-right");
    }
    // lamp shape carries the state: off = nothing lit, idle = hollow ring, running = solid
    int lamp = 0x56B4E9;
    assertEquals(0x1E2328, load("ice_extractor_front_off").getRGB(5, 4) & 0xFFFFFF);
    assertEquals(lamp, load("ice_extractor_front").getRGB(4, 3) & 0xFFFFFF);
    assertTrue(
        (load("ice_extractor_front").getRGB(5, 4) & 0xFFFFFF) != lamp, "idle ring is hollow");
    assertEquals(lamp, load("ice_extractor_front_running").getRGB(5, 4) & 0xFFFFFF);
    for (String n : OVERLAYS) {
      if (!n.startsWith("ice_extractor_")) continue;
      BufferedImage img = load(n);
      for (int x = 0; x < 16; x++) {
        for (int y = 0; y < 16; y++) {
          int argb = img.getRGB(x, y);
          int r = (argb >> 16) & 255;
          int g = (argb >> 8) & 255;
          int bl = argb & 255;
          assertTrue(!(g > r + 20 && g > bl + 20), n + " has green at " + x + "," + y);
        }
      }
    }
  }

  @Test
  void iceExtractorModelsKeepOneCellAnd32PxHeight() throws IOException {
    Path models = TestData.pack().resolve("assets/mars/models/block");
    for (String n :
        List.of(
            "ice_extractor", "ice_extractor_off", "ice_extractor_idle", "ice_extractor_running")) {
      com.google.gson.JsonObject m =
          com.google.gson.JsonParser.parseString(Files.readString(models.resolve(n + ".json")))
              .getAsJsonObject();
      double maxY = 0;
      for (var e : m.getAsJsonArray("elements")) {
        var el = e.getAsJsonObject();
        for (int c : new int[] {0, 2}) {
          double lo = el.getAsJsonArray("from").get(c).getAsDouble();
          double hi = el.getAsJsonArray("to").get(c).getAsDouble();
          assertTrue(lo >= 0 && hi <= 16, n + " stays inside one cell on x and z");
        }
        maxY = Math.max(maxY, el.getAsJsonArray("to").get(1).getAsDouble());
      }
      assertEquals(32.0, maxY, n + " model is 32 px high");
    }
    assertTrue(
        Files.readString(models.resolve("ice_extractor_running.json"))
            .contains("ice_extractor_sleeve_running"),
        "running model uses the animated sleeve");
    assertTrue(
        Files.exists(TestData.pack().resolve("assets/mars/models/item/ice_extractor.json")),
        "item model");
  }

  @Test
  void preparedStateModelsPointAtExistingTextures() throws IOException {
    Path models = TestData.pack().resolve("assets/mars/models/block");
    try (var files = Files.list(models)) {
      for (Path f : (Iterable<Path>) files::iterator) {
        String fn = f.getFileName().toString();
        if (!fn.matches(".*_(off|idle|running|charge\\d)\\.json")) continue;
        com.google.gson.JsonObject m =
            com.google.gson.JsonParser.parseString(Files.readString(f)).getAsJsonObject();
        for (var e : m.getAsJsonObject("textures").entrySet()) {
          String ref = e.getValue().getAsString().replace("mars:block/", "");
          assertTrue(
              Files.exists(TestData.pack().resolve("assets/mars/textures/block/" + ref + ".png")),
              fn + " -> missing " + ref);
        }
      }
    }
  }
}
