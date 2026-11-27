package mars.index.sim;

/**
 * Pacing config (ARCHITECTURE.md 2.1). Multipliers scale rates only, never thresholds: there is no
 * threshold field here on purpose.
 *
 * @param rateMultiplier {@code pace.rateMultiplier}: 1.0 standard, 4.0 classroom
 * @param groupSize {@code group.size}: static, set per planet, never read from who is online
 * @param solTicks {@code pace.solTicks}: simulation ticks per sol (24,000 = 20 minutes)
 * @param stepTicks simulation ticks per Index step (100 = 5 seconds)
 * @param classroom classroom mode: shorter holds (balance sheet 4); never changes a threshold
 */
public record Pace(
    double rateMultiplier, int groupSize, int solTicks, int stepTicks, boolean classroom) {
  public static final Pace STANDARD = new Pace(1.0, 1, 24_000, 100);

  public Pace(double rateMultiplier, int groupSize, int solTicks, int stepTicks) {
    this(rateMultiplier, groupSize, solTicks, stepTicks, false);
  }

  /** Classroom preset (balance sheet 5.1): rates x4, short holds, the given group size. */
  public static Pace classroom(int groupSize) {
    return new Pace(4.0, groupSize, 24_000, 100, true);
  }

  public Pace {
    if (rateMultiplier <= 0 || groupSize < 1 || solTicks <= 0 || stepTicks <= 0) {
      throw new IllegalArgumentException("Invalid pace: " + this);
    }
  }

  /** {@code 1 / (1 + 0.35 * (n - 1))} (balance sheet §5). */
  public double groupRateScale() {
    return 1.0 / (1.0 + 0.35 * (groupSize - 1));
  }
}
