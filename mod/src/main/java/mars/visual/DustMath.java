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

  /**
   * Streak attempts per tick during a gust (about one per second; half are dropped off dune sand).
   * Between gusts there are none: streaks are rare and bursty (SME atmosphere pass, 2026-11-27).
   * INVENTED.
   */
  public static final double STREAKS_PER_TICK = 0.05;

  /** A gust lasts 5 to 8 s and the calm before the next one 45 to 120 s. INVENTED. */
  public static final int GUST_MIN_TICKS = 100;

  public static final int GUST_MAX_TICKS = 160;
  public static final int CALM_MIN_TICKS = 900;
  public static final int CALM_MAX_TICKS = 2400;

  /**
   * The gust clock: calm, then a gust, then calm again. Pure and driven by a unit random number so
   * a test can run it.
   */
  public static final class Gusts {
    private int ticksLeft;
    private boolean gusting;

    public Gusts(double firstUnit) {
      ticksLeft = span(CALM_MIN_TICKS, CALM_MAX_TICKS, firstUnit);
    }

    /** One client tick; {@code unit} is a fresh random number in 0..1. Returns true in a gust. */
    public boolean tick(double unit) {
      if (--ticksLeft <= 0) {
        gusting = !gusting;
        ticksLeft =
            gusting
                ? span(GUST_MIN_TICKS, GUST_MAX_TICKS, unit)
                : span(CALM_MIN_TICKS, CALM_MAX_TICKS, unit);
      }
      return gusting;
    }

    private static int span(int min, int max, double unit) {
      return min + (int) Math.round(unit * (max - min));
    }
  }

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

  /**
   * Streak attempts per tick; a storm hides streaks in its own dust, so they stop. Only counted
   * while a gust runs.
   */
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
