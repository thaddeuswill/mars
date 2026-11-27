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
}
