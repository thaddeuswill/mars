package mars.life;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class BlackoutTimerTest {
  @Test
  void blacksOutOnTheHundredAndTwentiethSecondAndThenStartsOver() {
    var t = new BlackoutTimer<String>(120);
    for (int i = 1; i < 120; i++) {
      assertFalse(t.tick("a", true), "second " + i);
    }
    assertTrue(t.tick("a", true), "second 120");
    assertEquals(0, t.seconds("a"));
    assertFalse(t.tick("a", true), "the count started over");
  }

  @Test
  void breathingAgainResetsAndPlayersAreIndependent() {
    var t = new BlackoutTimer<String>(120);
    for (int i = 0; i < 100; i++) {
      t.tick("a", true);
    }
    t.tick("a", false);
    assertEquals(0, t.seconds("a"));
    for (int i = 0; i < 60; i++) {
      t.tick("b", true);
    }
    assertEquals(60, t.seconds("b"));
    assertEquals(0, t.seconds("a"));
  }
}
