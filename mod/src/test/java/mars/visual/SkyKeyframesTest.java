package mars.visual;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.file.Files;
import java.nio.file.Path;
import mars.TestData;
import org.junit.jupiter.api.Test;

class SkyKeyframesTest {
  private static SkyKeyframes real() throws Exception {
    Path f = TestData.pack().resolve("assets/mars/keyframes/sky.json");
    return SkyKeyframes.parse(JsonParser.parseString(Files.readString(f)).getAsJsonObject());
  }

  private static JsonObject realJson() throws Exception {
    return JsonParser.parseString(
            Files.readString(TestData.pack().resolve("assets/mars/keyframes/sky.json")))
        .getAsJsonObject();
  }

  @Test
  void theDataFileLoadsWithFiveOrderedKeysFromTheArtDocument() throws Exception {
    SkyKeyframes kf = real();
    assertEquals(5, kf.keys().size());
    double[] at = {0.6, 5, 15, 30, 75};
    for (int i = 0; i < at.length; i++) {
      assertEquals(at[i], kf.keys().get(i).at(), 0);
    }
    assertEquals(0xA87B5A, kf.keys().get(0).zenith());
    assertEquals(0x6A93CF, kf.keys().get(4).zenith());
    assertEquals(30, kf.smoothingSeconds(), 0);
    assertEquals(48, kf.fogEndFloorBlocks(), 0);
  }

  @Test
  void atEveryKeyTheLookIsExactlyTheKey() throws Exception {
    SkyKeyframes kf = real();
    for (SkyKeyframes.Key k : kf.keys()) {
      SkyKeyframes.Look l = kf.sample(k.at());
      assertEquals(k.zenith(), l.zenith(), "zenith at " + k.at());
      assertEquals(k.horizon(), l.horizon(), "horizon at " + k.at());
      assertEquals(k.fog(), l.fog(), "fog at " + k.at());
      assertEquals(k.sunsetHalo(), l.sunsetHalo());
      assertEquals(k.night(), l.night());
      assertEquals(k.fogStart(), l.fogStart(), 1e-12);
      assertEquals(k.fogEnd(), l.fogEnd(), 1e-12);
    }
  }

  @Test
  void belowTheFirstAndAboveTheLastTheEndKeysHold() throws Exception {
    SkyKeyframes kf = real();
    assertEquals(kf.sample(0.6), kf.sample(0.0001));
    assertEquals(kf.sample(0.6), kf.sample(-3));
    assertEquals(kf.sample(0.6), kf.sample(Double.NaN));
    assertEquals(kf.sample(75), kf.sample(500));
    assertEquals(kf.sample(75), kf.sample(Double.POSITIVE_INFINITY));
  }

  @Test
  void betweenKeysTheMixIsOnTheLogAxis() throws Exception {
    SkyKeyframes kf = real();
    // The geometric middle of 5 and 15 is sqrt(75) kPa: fog start must be exactly halfway.
    double mid = Math.sqrt(5 * 15);
    assertEquals((0.47 + 0.42) / 2, kf.sample(mid).fogStart(), 1e-12);
    // The arithmetic middle (10 kPa) is past the halfway point on a log axis.
    assertTrue(kf.sample(10).fogStart() < 0.56);
    // A colour between two keys differs from both and lies between them in lightness.
    int z = kf.sample(10).zenith();
    assertTrue(z != kf.keys().get(1).zenith() && z != kf.keys().get(2).zenith());
    double l1 = Oklab.fromRgb(kf.keys().get(1).zenith())[0];
    double l2 = Oklab.fromRgb(kf.keys().get(2).zenith())[0];
    double l = Oklab.fromRgb(z)[0];
    assertTrue(l >= Math.min(l1, l2) - 0.01 && l <= Math.max(l1, l2) + 0.01, "L " + l);
  }

  @Test
  void risingPressureNeverMovesFogTheWrongWayAndSkyKeepsMovingTowardBlue() throws Exception {
    SkyKeyframes kf = real();
    double lastStart = 2;
    double lastEnd = 2;
    double lastBlueShare = -1000;
    for (double p = 0.6; p <= 75; p *= 1.02) {
      SkyKeyframes.Look l = kf.sample(p);
      assertTrue(l.fogStart() <= lastStart + 1e-12, "fog start rose at " + p);
      assertTrue(l.fogEnd() <= lastEnd + 1e-12, "fog end rose at " + p);
      lastStart = l.fogStart();
      lastEnd = l.fogEnd();
      int z = l.zenith();
      double blueShare = (z & 0xFF) - ((z >> 16) & 0xFF); // blue minus red: tan is negative
      assertTrue(blueShare >= lastBlueShare - 1, "zenith lost blue at " + p);
      lastBlueShare = Math.max(lastBlueShare, blueShare);
    }
  }

  @Test
  void fogDistancesFollowTheRenderDistanceWithAFloor() throws Exception {
    SkyKeyframes kf = real();
    SkyKeyframes.Look start = kf.sample(0.6);
    double[] d = kf.fogDistances(start, 192);
    assertEquals(0.50 * 192, d[0], 1e-9);
    assertEquals(192, d[1], 1e-9);
    double[] low = kf.fogDistances(kf.sample(75), 32); // 0.85 * 32 = 27 blocks, below the floor
    assertEquals(48, low[1], 1e-9);
    assertTrue(low[0] < low[1]);
  }

  @Test
  void aBrokenFileIsRefusedWithTheFieldNamed() throws Exception {
    JsonObject unordered = realJson();
    unordered.getAsJsonArray("keys").get(2).getAsJsonObject().addProperty("at", 3.0);
    String m1 =
        assertThrows(IllegalArgumentException.class, () -> SkyKeyframes.parse(unordered))
            .getMessage();
    assertTrue(m1.contains("increasing") && m1.contains("keys[2]"), m1);

    JsonObject badColour = realJson();
    badColour.getAsJsonArray("keys").get(0).getAsJsonObject().addProperty("fog", "tan");
    String m2 =
        assertThrows(IllegalArgumentException.class, () -> SkyKeyframes.parse(badColour))
            .getMessage();
    assertTrue(m2.contains("keys[0].fog"), m2);

    JsonObject badAxis = realJson();
    badAxis.addProperty("axis", "linear");
    assertThrows(IllegalArgumentException.class, () -> SkyKeyframes.parse(badAxis));

    JsonObject zero = realJson();
    zero.getAsJsonArray("keys").get(0).getAsJsonObject().addProperty("at", 0);
    assertThrows(IllegalArgumentException.class, () -> SkyKeyframes.parse(zero));

    JsonObject fogOrder = realJson();
    fogOrder.getAsJsonArray("keys").get(1).getAsJsonObject().addProperty("fog_start", 1.0);
    assertThrows(IllegalArgumentException.class, () -> SkyKeyframes.parse(fogOrder));
  }

  @Test
  void daylightIsOneAtNoonZeroAtMidnight() {
    assertEquals(1, SkyKeyframes.daylight(0.0), 0);
    assertEquals(0, SkyKeyframes.daylight(0.5), 0);
    assertEquals(0.5, SkyKeyframes.daylight(0.25), 1e-12);
  }

  @Test
  void oklabRoundTripsAndMixesToTheEnds() {
    for (int c : new int[] {0xA87B5A, 0x6A93CF, 0x000000, 0xFFFFFF, 0x0D0B0E}) {
      assertEquals(c, Oklab.toRgb(Oklab.fromRgb(c)), "round trip " + Integer.toHexString(c));
    }
    assertEquals(0x112233, Oklab.mix(0x112233, 0xAABBCC, 0));
    assertEquals(0xAABBCC, Oklab.mix(0x112233, 0xAABBCC, 1));
  }

  /** OKLab distance between two colours. */
  private static double dist(int a, int b) {
    double[] x = Oklab.fromRgb(a);
    double[] y = Oklab.fromRgb(b);
    return Math.sqrt(
        (x[0] - y[0]) * (x[0] - y[0])
            + (x[1] - y[1]) * (x[1] - y[1])
            + (x[2] - y[2]) * (x[2] - y[2]));
  }

  @Test
  void everyPressureStepStillShowsInSkyAndFogAfterTheAt2Retune() throws Exception {
    SkyKeyframes kf = real();
    double minFog = 9;
    double minZenith = 9;
    for (int i = 1; i < kf.keys().size(); i++) {
      minFog = Math.min(minFog, dist(kf.keys().get(i - 1).fog(), kf.keys().get(i).fog()));
      minZenith =
          Math.min(minZenith, dist(kf.keys().get(i - 1).zenith(), kf.keys().get(i).zenith()));
    }
    System.out.println("sky steps (OKLab): min fog " + minFog + ", min zenith " + minZenith);
    assertTrue(minFog >= 0.02, "fog step " + minFog);
    assertTrue(minZenith >= 0.03, "zenith step " + minZenith);
    // The Phase 2 step (0.6 to 5 kPa) must still be visible in the fog and the start.
    assertTrue(dist(kf.keys().get(0).fog(), kf.keys().get(1).fog()) >= 0.02);
    assertTrue(kf.keys().get(0).fogStart() - kf.keys().get(1).fogStart() >= 0.02);
  }

  @Test
  void aCalmSkyIsReturnedUnchanged() throws Exception {
    SkyKeyframes kf = real();
    for (double p : new double[] {0.6, 2, 5, 30, 75}) {
      SkyKeyframes.Look calm = kf.sample(p);
      assertSame(calm, kf.withStorm(calm, 0, 0), "pressure " + p);
      assertEquals(0, calm.storm(), 0);
    }
    assertEquals(1, kf.sunAlpha(0), 0);
    assertEquals(1, kf.starFactor(0), 0);
  }

  @Test
  void fullStormMatchesThePlanAtTheEndsAndMovesMonotonically() throws Exception {
    SkyKeyframes kf = real();
    SkyKeyframes.Look calm = kf.sample(0.6);
    SkyKeyframes.Look full = kf.withStorm(calm, 1, 0);
    // fog end 0.18, start 0, sun alpha 0.10 (plan 0.30, lowered per SME), stars 0.1
    assertEquals(0.18, full.fogEnd(), 1e-12);
    assertEquals(0, full.fogStart(), 1e-12);
    assertEquals(0.10, kf.sunAlpha(1), 1e-12);
    assertEquals(0.1, kf.starFactor(1), 1e-12);
    // sky: 85 % toward #9A6A48 and 18 % darker in linear light, so darker than the calm sky
    assertTrue(Oklab.fromRgb(full.horizon())[0] < Oklab.fromRgb(calm.horizon())[0]);
    // At 192 blocks the storm fog ends at 35 blocks (plan table).
    assertEquals(0.18 * 192, kf.fogDistances(full, 192)[1], 1e-9);
    // Low render distance: floor of 16 blocks in a storm, 48 when calm.
    assertEquals(16, kf.fogDistances(full, 32)[1], 1e-9);
    assertEquals(48, kf.fogDistances(calm, 32)[1], 1e-9);
    double lastEnd = 2;
    double lastL = 2;
    for (double s = 0; s <= 1.0001; s += 0.05) {
      SkyKeyframes.Look l = kf.withStorm(calm, s, 0);
      assertTrue(l.fogEnd() <= lastEnd + 1e-12, "fog end rose at " + s);
      assertTrue(l.fogStart() < l.fogEnd());
      double lum = Oklab.fromRgb(l.zenith())[0];
      assertTrue(lum <= lastL + 1e-9 || s == 0, "sky brightened at " + s);
      lastEnd = l.fogEnd();
      lastL = lum;
    }
    // Phase caps: a 0.25 storm leaves the fog end at 0.68 of the render distance at 75 kPa.
    SkyKeyframes.Look late = kf.withStorm(kf.sample(75), 0.25, 0);
    assertEquals(0.85 + (0.18 - 0.85) * 0.25, late.fogEnd(), 1e-12);
  }

  @Test
  void theHazeIsQuietAndLeavesSkySunAndStarsAlone() throws Exception {
    SkyKeyframes kf = real();
    SkyKeyframes.Look calm = kf.sample(0.6);
    SkyKeyframes.Look haze = kf.withStorm(calm, 0, 1);
    assertEquals(calm.zenith(), haze.zenith());
    assertEquals(calm.horizon(), haze.horizon());
    assertEquals(calm.fogEnd() * 0.9, haze.fogEnd(), 1e-12);
    assertEquals(calm.fogStart(), haze.fogStart(), 1e-12);
    assertEquals(0, haze.storm(), 0);
    assertTrue(haze.fog() != calm.fog());
    assertTrue(dist(haze.fog(), calm.fog()) < 0.02, "haze must stay subtle");
  }
}
