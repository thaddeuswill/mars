package mars.visual;

/**
 * Exponential smoothing of the pressure the sky follows, on the log10 axis (planetary-keyframes
 * 1.3: time constant 30 s, so a gate or a debug command gives a one-to-two-minute shift, never a
 * snap). Pure Java; one multiply-add per client tick.
 */
public final class Smoothing {
  private Smoothing() {}

  /**
   * Moves {@code shownKpa} toward {@code targetKpa} for {@code dtSeconds}. A time constant of 0 (or
   * less) snaps. The result never overshoots the target and, for dt greater than 0, stays on the
   * same side of it.
   */
  public static double step(
      double shownKpa, double targetKpa, double dtSeconds, double tauSeconds) {
    if (tauSeconds <= 0 || dtSeconds >= 1e9) {
      return targetKpa;
    }
    if (dtSeconds <= 0) {
      return shownKpa;
    }
    double from = Math.log10(shownKpa);
    double to = Math.log10(targetKpa);
    double k = 1 - Math.exp(-dtSeconds / tauSeconds);
    return Math.pow(10, from + (to - from) * k);
  }
}
