package mars.terrain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import mars.TestData;
import org.junit.jupiter.api.Test;

/** Terrain shaping maths against the real planet file (data/mars/planets/planet_1.json). */
class LandformsTest {
  private static final long SEED = 20261127L;

  static TerrainParams load() {
    try (Reader r = Files.newBufferedReader(TestData.data("planets/planet_1.json"))) {
      return TerrainParams.parse(new Gson().fromJson(r, JsonObject.class), "planet_1.json");
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  private static TerrainParams withWind(TerrainParams p, double deg) {
    return new TerrainParams(
        deg,
        p.pad(),
        p.craterShape(),
        p.smallLevels(),
        p.large(),
        p.fixed(),
        p.dunes(),
        p.surface(),
        p.rocks());
  }

  @Test
  void padIsExactlyFlatInsideFlatRadius() {
    Landforms f = new Landforms(load(), SEED);
    int r = f.params().pad().flatRadius();
    for (int x = -r; x <= r; x += 3) {
      for (int z = -r; z <= r; z += 3) {
        if (x * x + z * z <= r * r) {
          // Whatever the natural ground is, the pad wins.
          assertEquals(f.params().pad().heightY(), f.at(x, z, 60 + (x & 7)).targetY(), x + "," + z);
          assertEquals(Landforms.Kind.PAD, f.at(x, z, 90).kind());
        }
      }
    }
  }

  @Test
  void rampBlendsBetweenPadAndNatural() {
    Landforms f = new Landforms(load(), SEED);
    TerrainParams.Pad pad = f.params().pad();
    int outside = pad.rampRadius() + 40;
    // Past the ramp, with no crater or dune, the natural height is kept.
    int natural = pad.heightY() + 5;
    assertEquals(1.0, f.padWeight(0, 0), 0.0);
    assertEquals(0.0, f.padWeight(outside, 0), 0.0);
    int mid = (pad.flatRadius() + pad.rampRadius()) / 2;
    double w = f.padWeight(mid, 0);
    assertTrue(w > 0.3 && w < 0.7, "ramp middle weight " + w);
    // Ramp slope: no single step bigger than 1 block per block over the ramp (natural = flat).
    int prev = f.at(pad.flatRadius(), 0, natural).targetY();
    for (int x = pad.flatRadius() + 1; x <= pad.rampRadius(); x++) {
      int y = f.at(x, 0, natural).targetY();
      boolean plain = f.craterDelta(x, 0) == 0.0 && f.duneHeight(x, 0) <= 0.5;
      if (plain) {
        assertTrue(Math.abs(y - prev) <= 1, "step at x=" + x);
      }
      prev = y;
    }
  }

  @Test
  void sameSeedSameTerrainAndOrderDoesNotMatter() {
    Landforms a = new Landforms(load(), SEED);
    Landforms b = new Landforms(load(), SEED);
    List<Integer> forward = new ArrayList<>();
    for (int x = 100; x < 400; x += 7) {
      forward.add(a.at(x, -x / 2, 80).targetY());
    }
    List<Integer> backward = new ArrayList<>();
    for (int x = 399; x >= 100; x--) {
      if ((x - 100) % 7 == 0) {
        backward.add(0, b.at(x, -x / 2, 80).targetY());
      }
    }
    assertEquals(forward, backward);
  }

  @Test
  void aSmallCraterHasABowlARaisedRimAndAnApron() {
    Landforms f = new Landforms(load(), SEED);
    // The first fixed crater: (70, -52), diameter 14.
    TerrainParams.FixedCrater c = f.params().fixed().get(0);
    double R = c.diameter() / 2;
    // Use this crater's own profile (a random crater may overlap the fixed one).
    Crater own = null;
    for (Crater n : new CraterField(f.params(), SEED).near(c.x(), c.z())) {
      if (n.cx() == c.x() && n.cz() == c.z()) {
        own = n;
      }
    }
    assertTrue(own != null, "fixed crater found");
    double centre = own.profile(0);
    double rim = own.profile(R);
    double apron = own.profile(1.6 * R);
    double far = own.profile(3.0 * R);
    assertTrue(centre <= -0.10 * c.diameter() + 0.2, "bowl depth at centre " + centre);
    assertTrue(centre >= -0.20 * c.diameter() - 0.5, "bowl not deeper than 0.2 D: " + centre);
    assertTrue(rim >= 1.0, "rim is raised at least one block: " + rim);
    assertTrue(apron > 0.0 && apron < rim, "ejecta apron falls off outward: " + apron);
    assertEquals(0.0, far, 0.3);
  }

  @Test
  void largeCraterHasFlatFloorAndCentralPeak() {
    TerrainParams p = load();
    CraterField field = new CraterField(p, SEED);
    Crater big = null;
    TerrainParams.LargeLevel g = p.large();
    for (long i = -40; i < 40 && big == null; i++) {
      for (long j = -40; j < 40 && big == null; j++) {
        big =
            field.craterInCell(
                100, g.cell(), g.chance(), g.diameterMin(), g.diameterMax(), i, j, true);
      }
    }
    assertTrue(big != null, "a large crater exists somewhere in 80 x 80 cells");
    assertTrue(big.radius() * 2 >= g.diameterMin() && big.radius() * 2 <= g.diameterMax());
    double floorMid = big.profile(0.8 * big.floorFraction() * big.radius());
    double floorNearPeak = big.profile(0.0);
    assertTrue(floorMid < -0.5 * big.depth(), "flat floor is deep: " + floorMid);
    assertTrue(floorNearPeak > floorMid, "central peak rises above the floor");
    // Shallower than a small crater of the same width (0.08 to 0.10 vs 0.10 to 0.20 of D).
    assertTrue(big.depth() <= 0.10 * 2 * big.radius() + 1e-9);
  }

  @Test
  void manySmallFewBig() {
    Landforms f = new Landforms(load(), SEED);
    TerrainParams p = f.params();
    int[] counts = new int[p.smallLevels().size()];
    long area = 4000L * 4000L;
    for (int i = 0; i < counts.length; i++) {
      TerrainParams.CraterLevel l = p.smallLevels().get(i);
      CraterField field = new CraterField(p, SEED);
      int cells = (int) (4000 / l.cell());
      for (long a = 0; a < cells; a++) {
        for (long b = 0; b < cells; b++) {
          if (field.craterInCell(
                  i, l.cell(), l.chance(), l.diameterMin(), l.diameterMax(), a, b, false)
              != null) {
            counts[i]++;
          }
        }
      }
    }
    for (int i = 0; i + 1 < counts.length; i++) {
      assertTrue(
          counts[i] > counts[i + 1],
          "smaller craters are more numerous: " + counts[i] + " vs " + counts[i + 1]);
    }
    // About 0.6 craters of 16 to 32 blocks per 100 x 100 (data note), within a factor of 2.
    double per10k = counts[2] * 10000.0 / area;
    assertTrue(per10k > 0.3 && per10k < 1.2, "16..32 block craters per 10k columns: " + per10k);
  }

  /** Mean downhill-in-wind-direction slopes: gentle rise upwind of a crest, steep fall past it. */
  private static double[] slopes(Landforms f, double windDeg, int originX, int originZ) {
    double a = Math.toRadians(windDeg);
    double wx = Math.sin(a);
    double wz = -Math.cos(a);
    double rise = 0;
    double fall = 0;
    int nRise = 0;
    int nFall = 0;
    // Walk along the wind direction through the middle of a dune field.
    for (int line = 0; line < 40; line++) {
      double ox = originX - wz * (line - 20) * 5 - wx * 100;
      double oz = originZ + wx * (line - 20) * 5 - wz * 100;
      double prev = f.duneHeight(ox, oz);
      for (int s = 1; s < 400; s++) {
        double h = f.duneHeight(ox + wx * s * 0.5, oz + wz * s * 0.5);
        double dh = (h - prev) / 0.5;
        if (f.duneHeight(ox + wx * s * 0.5, oz + wz * s * 0.5) > 1.0 && prev > 1.0) {
          if (dh > 0.001) {
            rise += dh;
            nRise++;
          } else if (dh < -0.001) {
            fall += -dh;
            nFall++;
          }
        }
        prev = h;
      }
    }
    return new double[] {rise / Math.max(1, nRise), fall / Math.max(1, nFall), nRise, nFall};
  }

  private static int[] findDuneField(Landforms f) {
    for (int x = -3000; x < 3000; x += 40) {
      for (int z = -3000; z < 3000; z += 40) {
        if (f.duneHeight(x, z) > 2.0) {
          return new int[] {x, z};
        }
      }
    }
    throw new AssertionError("no dune field within 3000 blocks");
  }

  @Test
  void duneWindwardSlopeIsGentleAndSlipFaceIsSteepAndFacesTheWind() {
    TerrainParams p = load();
    for (double deg : new double[] {p.windTowardDeg(), 0, 200, 300}) {
      Landforms f = new Landforms(withWind(p, deg), SEED);
      int[] at = findDuneField(f);
      double[] s = slopes(f, deg, at[0], at[1]);
      assertTrue(s[2] > 50 && s[3] > 50, "enough samples at " + deg);
      // Gentle upwind side vs steep downwind side. windward_fraction 0.78 -> about 3.5 to 1.
      assertTrue(s[1] > 2.0 * s[0], "wind " + deg + ": fall " + s[1] + " vs rise " + s[0]);
      assertTrue(s[0] < 0.35, "windward slope under about 19 degrees: " + s[0]);
      assertTrue(s[1] > 0.35, "mean slip face steeper than 19 degrees: " + s[1]);
    }
  }

  @Test
  void reversingTheWindFlipsTheAsymmetry() {
    TerrainParams p = load();
    Landforms f = new Landforms(withWind(p, p.windTowardDeg()), SEED);
    int[] at = findDuneField(f);
    // Walk against the wind: what was a rise is now a fall.
    double[] with = slopes(f, p.windTowardDeg(), at[0], at[1]);
    double[] against = slopes(f, p.windTowardDeg() + 180, at[0], at[1]);
    assertTrue(with[1] > with[0]);
    assertTrue(against[0] > against[1], "against the wind the steep side is the rise");
  }

  @Test
  void duneHeightsStayInTheDataRange() {
    TerrainParams p = load();
    Landforms f = new Landforms(p, SEED);
    double max = 0;
    for (int x = -1500; x < 1500; x += 5) {
      for (int z = -1500; z < 1500; z += 5) {
        max = Math.max(max, f.duneHeight(x, z));
      }
    }
    assertTrue(max <= p.dunes().heightMax() + 1e-9, "max dune " + max);
    assertTrue(max >= p.dunes().heightMin(), "dunes exist: " + max);
  }

  @Test
  void windDirectionIsStoredInPlanetData() {
    TerrainParams p = load();
    assertTrue(p.windTowardDeg() >= 0 && p.windTowardDeg() < 360);
    Landforms f = new Landforms(p, SEED);
    double[] w = f.windVector();
    assertEquals(1.0, Math.hypot(w[0], w[1]), 1e-9);
    // 90 degrees is east.
    double[] east = new Landforms(withWind(p, 90), SEED).windVector();
    assertEquals(1.0, east[0], 1e-9);
    assertEquals(0.0, east[1], 1e-9);
  }

  @Test
  void aMissingKeyIsAnErrorNamingTheKey() {
    JsonObject root = new JsonObject();
    root.add("terrain", new JsonObject());
    var e =
        org.junit.jupiter.api.Assertions.assertThrows(
            IllegalArgumentException.class, () -> TerrainParams.parse(root, "x.json"));
    assertTrue(e.getMessage().contains("wind"), e.getMessage());
  }

  @Test
  void costPerColumnIsSmall() {
    Landforms f = new Landforms(load(), SEED);
    long t0 = System.nanoTime();
    long sink = 0;
    for (int x = 0; x < 256; x++) {
      for (int z = 0; z < 256; z++) {
        sink += f.at(x + 500, z + 500, 80).targetY();
      }
    }
    double msPerChunk = (System.nanoTime() - t0) / 1e6 / 256.0;
    System.out.println("landforms cost: " + msPerChunk + " ms per chunk (sink " + sink + ")");
    assertTrue(msPerChunk < 20.0, "ms per chunk " + msPerChunk);
  }
}
