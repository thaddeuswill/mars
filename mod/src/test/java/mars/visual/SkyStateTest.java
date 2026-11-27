package mars.visual;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonParser;
import java.nio.file.Files;
import mars.TestData;
import org.junit.jupiter.api.Test;

class SkyStateTest {
  private static SkyKeyframes kf() throws Exception {
    return SkyKeyframes.parse(
        JsonParser.parseString(
                Files.readString(TestData.pack().resolve("assets/mars/keyframes/sky.json")))
            .getAsJsonObject());
  }

  @Test
  void smoothingMovesAboutSixThirtiethsOfTheGapPerSecondAndNeverOvershoots() {
    double p = 0.6;
    double lastLog = Math.log10(p);
    double target = 5;
    for (int i = 0; i < 20 * 600; i++) {
      p = Smoothing.step(p, target, 0.05, 30);
      assertTrue(p <= target && Math.log10(p) >= lastLog - 1e-12, "monotone, no overshoot");
      lastLog = Math.log10(p);
    }
    assertEquals(5, p, 1e-6);
    // After one time constant, 63.2 % of the log gap is covered.
    double one = Smoothing.step(0.6, 5, 30, 30);
    double covered = (Math.log10(one) - Math.log10(0.6)) / (Math.log10(5) - Math.log10(0.6));
    assertEquals(1 - Math.exp(-1), covered, 1e-9);
  }

  @Test
  void fallingPressureSmoothsDownwardsToo() {
    double p = Smoothing.step(75, 5, 30, 30);
    assertTrue(p < 75 && p > 5);
    assertEquals(75, Smoothing.step(75, 5, 0, 30), 0);
    assertEquals(5, Smoothing.step(75, 5, 1, 0), 0); // tau 0 snaps
  }

  @Test
  void theFirstValueSnapsAndLaterOnesShiftOverTime() throws Exception {
    SkyState s = new SkyState(kf(), 30);
    assertFalse(s.hasValue());
    s.setTarget(5);
    assertEquals(5, s.shownKpa(), 0);
    assertEquals(kf().sample(5), s.look());
    s.setTarget(75);
    assertEquals(5, s.shownKpa(), 0); // no snap
    s.advance(1);
    assertTrue(s.shownKpa() > 5 && s.shownKpa() < 75);
    for (int i = 0; i < 20 * 300; i++) {
      s.advance(0.05);
    }
    assertEquals(75, s.shownKpa(), 0.05);
    s.reset();
    assertFalse(s.hasValue());
    assertEquals(kf().sample(0.6), s.look()); // before any value: the start look
  }

  @Test
  void badServerValuesAreIgnored() throws Exception {
    SkyState s = new SkyState(kf(), 30);
    s.setTarget(Double.NaN);
    s.setTarget(-1);
    s.setTarget(0);
    s.setTarget(Double.POSITIVE_INFINITY);
    assertFalse(s.hasValue());
  }

  @Test
  void aPacketIsWorthSendingOnlyForTheFirstValueOrAChangeOverATenthOfAPercent() {
    assertTrue(SkyState.shouldSend(null, 0.6));
    assertFalse(SkyState.shouldSend(10.0, 10.0));
    assertFalse(SkyState.shouldSend(10.0, 10.005));
    assertTrue(SkyState.shouldSend(10.0, 10.02));
    assertTrue(SkyState.shouldSend(10.0, 9.98));
  }

  @Test
  void aStormRampsInOverAMinuteAndOutOverNinetySecondsAndTheFirstValueSnaps() throws Exception {
    SkyState s = new SkyState(kf(), 30);
    s.setTarget(0.6);
    s.setStorm(0, 0);
    SkyKeyframes.Look calm = s.look();
    assertEquals(0, s.stormShown(), 0);
    s.setStorm(1, 0);
    for (int i = 0; i < 30; i++) {
      s.advance(1);
    }
    assertEquals(0.5, s.stormShown(), 1e-9);
    for (int i = 0; i < 30; i++) {
      s.advance(1);
    }
    assertEquals(1, s.stormShown(), 1e-9);
    assertTrue(s.look().fogEnd() < calm.fogEnd());
    s.setStorm(0, 0);
    for (int i = 0; i < 45; i++) {
      s.advance(1);
    }
    assertEquals(0.5, s.stormShown(), 1e-9);
    for (int i = 0; i < 45; i++) {
      s.advance(1);
    }
    assertEquals(0, s.stormShown(), 1e-9);
    assertEquals(calm, s.look(), "after the storm the calm sky is back exactly");

    SkyState joined = new SkyState(kf(), 30);
    joined.setTarget(0.6);
    joined.setStorm(0.8, 0);
    assertEquals(0.8, joined.stormShown(), 0, "joining mid-storm shows the storm");
  }

  @Test
  void stormPacketsGoOutOnlyWhenTheStormOrHazeMoves() {
    assertFalse(SkyState.shouldSendStorm(0, 0, 0, 0));
    assertTrue(SkyState.shouldSendStorm(0, 0, 1, 0));
    assertTrue(SkyState.shouldSendStorm(1, 0, 0, 0));
    assertFalse(SkyState.shouldSendStorm(0, 0.50, 0, 0.51));
    assertTrue(SkyState.shouldSendStorm(0, 0.50, 0, 0.53));
    assertTrue(SkyState.shouldSendStorm(0, 0.99, 0, 1));
    assertTrue(SkyState.shouldSendStorm(0, 0.01, 0, 0));
  }

  @Test
  void dustRatesFollowPressureStormAndTheParticleSetting() {
    assertEquals(0.2, DustMath.motesPerTick(0.6, 0, 1), 1e-12);
    assertEquals(0.1, DustMath.motesPerTick(75, 0, 1), 1e-12, "above 30 kPa half");
    assertEquals(0.1, DustMath.motesPerTick(0.6, 0, 0.5), 1e-12, "decreased setting halves");
    assertEquals(0, DustMath.motesPerTick(0.6, 1, 0), 0, "minimal setting is off");
    assertTrue(DustMath.motesPerTick(0.6, 1, 1) > 1.0, "a full storm is dense");
    assertTrue(DustMath.pressureFactor(20) < 1 && DustMath.pressureFactor(20) > 0.5);
    assertEquals(0, DustMath.streaksPerTick(0.6, 1, 1), 0, "no streaks in a storm");
    assertEquals(0.01, DustMath.streaksPerTick(0.6, 0, 1), 1e-12, "one per 5 s");
  }

  @Test
  void windBearingIsACompassBearingBlowingToward() {
    double[] n = DustMath.windVector(0);
    assertEquals(0, n[0], 1e-12);
    assertEquals(-1, n[1], 1e-12); // north is -z
    double[] e = DustMath.windVector(90);
    assertEquals(1, e[0], 1e-12); // east is +x
    assertEquals(0, e[1], 1e-12);
    double[] w = DustMath.windVector(110); // east-south-east: +x and a little +z
    assertTrue(w[0] > 0.9 && w[1] > 0.3 && w[1] < 0.4);
  }
}
