package mars.monitor.view;

/**
 * Next-gate bar maths (ARCHITECTURE.md 2.9, ui-specs.md section 3), pure so it can be tested.
 * Pressure progress runs on the log scale from the planet's start value to the target, so 0.6 to 5
 * kPa does not sit near 0 % for hours; other values are linear. Progress can go down when the value
 * falls (regression stays visible).
 */
public final class GoalProgress {
  private GoalProgress() {}

  /** Progress 0..1 of {@code now} between {@code start} and {@code target}. */
  public static double fraction(double now, double start, double target, boolean log) {
    if (!(target > start)) {
      return now >= target ? 1 : 0;
    }
    double f;
    if (log && start > 0 && now > 0) {
      f = (Math.log10(now) - Math.log10(start)) / (Math.log10(target) - Math.log10(start));
    } else {
      f = (now - start) / (target - start);
    }
    return Math.max(0, Math.min(1, f));
  }

  /**
   * A full percent for display; never "100 %" before the goal is reached and never "0 %" once
   * anything has moved.
   */
  public static String percent(double fraction, boolean reached) {
    long p = Math.round(fraction * 100);
    if (!reached) {
      p = Math.min(99, p);
    }
    return p + "%";
  }

  /**
   * Whole sols to go at the current net rate, rounded up, or -1 when the value is not rising (below
   * {@code minRate}). 0 means "less than one sol".
   */
  public static long etaSols(double now, double target, double netRatePerSol, double minRate) {
    if (netRatePerSol < minRate || now >= target) {
      return -1;
    }
    double sols = (target - now) / netRatePerSol;
    return sols < 1 ? 0 : (long) Math.ceil(sols);
  }
}
