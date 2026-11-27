package mars.visual;

/**
 * The numbers behind the ambient dust (docs/art/atmosphere-plan.md 5.1, AT-3): how many motes and
 * streaks per tick, and which way they drift. Pure Java so the rules are unit tested. All values
 * are the Lead Artist's INVENTED estimates (storm cap 40 alive is the plan's). Cost: a few
 * multiplications per client tick.
 */
public final class DustMath {
  /** Motes on a clear day: about 4 per second (plan 5.1). */
  public static final double MOTES_PER_TICK = 0.2;

  /** Storm motes at full strength: with a life of about 1.5 s this keeps about 40 alive. */
  public static final double STORM_MOTES_PER_TICK = 1.2;

  /** One wind streak per 5 s on plain ground at 0.6 kPa. */
  public static final double STREAKS_PER_TICK = 0.01;

  private DustMath() {}

  /** Thin air hangs more dust: 1 below 15 kPa, 0.5 from 30 kPa up, log-linear between. */
  public static double pressureFactor(double kpa) {
    if (!(kpa > 15)) {
      return 1;
    }
    if (kpa >= 30) {
      return 0.5;
    }
    return 1 - 0.5 * (Math.log10(kpa) - Math.log10(15)) / (Math.log10(30) - Math.log10(15));
  }

  /**
   * Motes to spawn per tick. {@code particleFactor} is the player's particle setting (1 all, 0.5
   * decreased, 0 minimal).
   */
  public static double motesPerTick(double kpa, double storm, double particleFactor) {
    double s = Math.max(0, Math.min(1, storm));
    return (MOTES_PER_TICK * pressureFactor(kpa) + STORM_MOTES_PER_TICK * s) * particleFactor;
  }

  /** Streak attempts per tick; a storm hides streaks in its own dust, so they stop. */
  public static double streaksPerTick(double kpa, double storm, double particleFactor) {
    double s = Math.max(0, Math.min(1, storm));
    return STREAKS_PER_TICK * pressureFactor(kpa) * (1 - s) * particleFactor;
  }

  /**
   * Unit vector {x, z} of the wind from a compass bearing it blows TOWARD (0 = north = -z, 90 =
   * east = +x), the same convention as the dune generator (mars.terrain.DuneField).
   */
  public static double[] windVector(double blowsTowardDeg) {
    double a = Math.toRadians(blowsTowardDeg);
    return new double[] {Math.sin(a), -Math.cos(a)};
  }
}
