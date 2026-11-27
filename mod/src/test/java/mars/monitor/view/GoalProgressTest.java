package mars.monitor.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GoalProgressTest {
  @Test
  void pressureUsesTheLogScaleFromTheStartValue() {
    // ui-specs section 3: 62 % must not sit at 3.3 kPa (linear) but near 2.2 kPa (log).
    double start = 0.6;
    double target = 5.0;
    double at2_2 = GoalProgress.fraction(2.2, start, target, true);
    double at2_2Linear = GoalProgress.fraction(2.2, start, target, false);
    System.out.println("2.2 kPa: log " + at2_2 + ", linear " + at2_2Linear);
    assertEquals(0.62, at2_2, 0.01);
    assertTrue(at2_2Linear < 0.4);
    assertEquals(0.0, GoalProgress.fraction(0.6, start, target, true), 1e-12);
    assertEquals(1.0, GoalProgress.fraction(5.0, start, target, true), 1e-12);
  }

  @Test
  void progressIsClampedAndCanFall() {
    assertEquals(1.0, GoalProgress.fraction(9, 0.6, 5, true));
    assertEquals(0.0, GoalProgress.fraction(0.3, 0.6, 5, true));
    double a = GoalProgress.fraction(2.0, 0.6, 5, true);
    double b = GoalProgress.fraction(1.5, 0.6, 5, true);
    assertTrue(b < a, "a falling value lowers the bar");
    assertEquals(0.5, GoalProgress.fraction(5, 0, 10, false), 1e-12);
  }

  @Test
  void percentNeverReadsFullBeforeTheGoalIsReached() {
    assertEquals("99%", GoalProgress.percent(0.999, false));
    assertEquals("100%", GoalProgress.percent(1.0, true));
    assertEquals("0%", GoalProgress.percent(0.0, false));
  }

  @Test
  void estimateRoundsUpAndHasNoEstimateWhenNotRising() {
    assertEquals(10, GoalProgress.etaSols(0.0, 5.0, 0.5, 0.005));
    assertEquals(11, GoalProgress.etaSols(0.0, 5.0, 0.46, 0.005));
    assertEquals(0, GoalProgress.etaSols(4.9, 5.0, 0.5, 0.005));
    assertEquals(-1, GoalProgress.etaSols(1.0, 5.0, 0.0, 0.005));
    assertEquals(-1, GoalProgress.etaSols(1.0, 5.0, -0.2, 0.005));
    assertEquals(-1, GoalProgress.etaSols(1.0, 5.0, 0.004, 0.005));
  }
}
