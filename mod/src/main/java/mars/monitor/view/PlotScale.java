package mars.monitor.view;

/**
 * Axis maths for the Monitor's pressure plot, pure so it can be tested. The Y axis always starts at
 * zero (an honest baseline) and tops out at the next "nice" value (1, 2, 5, 10, 20, 50, 100, ...)
 * above the largest value, so the line climbs as the planet is terraformed and the axis grows in
 * steps instead of every frame.
 */
public final class PlotScale {
  private PlotScale() {}

  /** Smallest of 1, 2, 5 times a power of ten that is at least 1.1 times {@code max}. */
  public static double niceMax(double max) {
    double target = Math.max(1.0, max * 1.1);
    double base = Math.pow(10, Math.floor(Math.log10(target)));
    for (double m : new double[] {1, 2, 5, 10}) {
      if (m * base >= target - 1e-12) {
        return m * base;
      }
    }
    return 10 * base;
  }

  /** Hold the phase range until an actual sample would leave it. */
  public static double topFor(double goal, double maxSample) {
    double phaseTop = 1.2 * goal;
    return goal > 0 && maxSample <= phaseTop ? phaseTop : niceMax(maxSample);
  }

  /** Pixel offset from the left of a plot {@code width} wide for a sol in [first, last]. */
  public static int x(double sol, double first, double last, int width) {
    if (last <= first) {
      return 0;
    }
    double f = Math.max(0, Math.min(1, (sol - first) / (last - first)));
    return (int) Math.round(f * (width - 1));
  }

  /** Pixel offset from the top of a plot {@code height} tall for a value in [0, top]. */
  public static int y(double value, double top, int height) {
    double f = Math.max(0, Math.min(1, value / top));
    return (int) Math.round((1 - f) * (height - 1));
  }
}
