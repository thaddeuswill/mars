package mars.visual;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    assertEquals((0.62 + 0.50) / 2, kf.sample(mid).fogStart(), 1e-12);
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
    assertEquals(0.70 * 192, d[0], 1e-9);
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
}
