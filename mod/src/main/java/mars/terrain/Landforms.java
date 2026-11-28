package mars.terrain;

/**
 * Crater, dune and pad shaping for one planet and one world seed. The Minecraft side hands in the
 * natural ground height of each column and gets back the height to build. Pure and stateless apart
 * from the immutable fields, so it is safe to call from several worldgen threads.
 */
public final class Landforms {
  /** What the top block of a column should look like. */
  public enum Kind {
    NONE,
    BOWL,
    RIM,
    EJECTA,
    DUNE,
    DUNE_RIPPLE,
    CUT,
    CANYON,
    PAD
  }

  /**
   * Result for one column. {@code targetY} is the new top-block y; {@code rockChance} is the chance
   * of a one-block rock on top.
   */
  public record Column(int targetY, Kind kind, double rockChance, double ejectaStrength) {}

  private final TerrainParams params;
  private final CraterField craters;
  private final DuneField dunes;
  private final Canyon canyon;
  private final long seed;

  public Landforms(TerrainParams params, long seed) {
    this.params = params;
    this.seed = seed;
    this.craters = new CraterField(params, seed);
    this.dunes = new DuneField(params.dunes(), params.windTowardDeg(), seed);
    this.canyon = new Canyon(params.canyon(), seed);
  }

  /** A repeatable random number in [0, 1) for a column and a purpose number (rocks, tint). */
  public double roll(int x, int z, int stream) {
    return Hash.unit(Hash.mix(seed, 0x50C5, x, z), stream);
  }

  public TerrainParams params() {
    return params;
  }

  /** Unit vector the wind blows toward, as {x, z}. */
  public double[] windVector() {
    return new double[] {dunes.windX(), dunes.windZ()};
  }

  /** Pad weight: 1 inside the flat radius, 0 outside the ramp radius, smooth between. */
  public double padWeight(double x, double z) {
    TerrainParams.Pad pad = params.pad();
    double r = Math.sqrt(x * x + z * z);
    if (r <= pad.flatRadius()) {
      return 1.0;
    }
    if (r >= pad.rampRadius()) {
      return 0.0;
    }
    return 1.0
        - Hash.smoothstep((r - pad.flatRadius()) / (double) (pad.rampRadius() - pad.flatRadius()));
  }

  /** Crater-only height change at (x, z), in blocks (for tests and tools). */
  public double craterDelta(double x, double z) {
    double sum = 0;
    for (Crater c : craters.near(x, z)) {
      sum += c.profile(Math.hypot(x - c.cx(), z - c.cz()));
    }
    return sum;
  }

  /** Butte keep-out weight: 1 inside the inner radius, 0 beyond the outer radius. */
  public double buttesCutWeight(double x, double z) {
    TerrainParams.Buttes b = params.buttes();
    double r = Math.sqrt(x * x + z * z);
    return 1.0 - Hash.smoothstep((r - b.innerRadius()) / (b.outerRadius() - b.innerRadius()));
  }

  /** Natural height after the butte keep-out (for tests): tall ground is cut toward the cap. */
  public double cutNatural(double x, double z, int naturalY) {
    double cap = params.pad().heightY() + params.buttes().capAbove();
    double w = buttesCutWeight(x, z);
    if (w <= 0 || naturalY <= cap) {
      return naturalY;
    }
    return cap + (naturalY - cap) * (1.0 - w);
  }

  /** True on the light stripe of a sand ripple at (x, z). */
  public boolean ripple(double x, double z) {
    return dunes.ripple(x, z);
  }

  /** Canyon depth at (x, z) in blocks, 0 outside (for tests and tools). */
  public double canyonDepth(double x, double z) {
    return canyon.depthAt(x, z);
  }

  /** Dune height at (x, z), in blocks (for tests and tools). */
  public double duneHeight(double x, double z) {
    return dunes.height(x, z);
  }

  /** The height to build at (x, z) given the natural top-block height there. */
  public Column at(int x, int z, int naturalY) {
    double w = padWeight(x, z);
    if (w >= 1.0) {
      return new Column(params.pad().heightY(), Kind.PAD, 0.0, 0.0);
    }
    int original = naturalY;
    naturalY = (int) Math.round(cutNatural(x, z, naturalY));
    double crater = 0;
    Kind kind = original > naturalY ? Kind.CUT : Kind.NONE;
    double rock = 0;
    double ejecta = 0;
    for (Crater c : craters.near(x, z)) {
      double r = Math.hypot(x - c.cx(), z - c.cz());
      crater += c.profile(r);
      if (c.onRim(r)) {
        if (c.radius() * 2 >= params.surface().rimBlockMinDiameter()) {
          kind = Kind.RIM;
        }
        rock = Math.max(rock, params.rocks().rimChance());
      } else if (r < c.radius()) {
        if (kind != Kind.RIM) {
          kind = Kind.BOWL;
        }
      } else {
        double e = c.ejectaStrength(r);
        if (e > 0) {
          ejecta = Math.max(ejecta, e);
          rock = Math.max(rock, params.rocks().ejectaChance() * e);
          if (kind == Kind.NONE || kind == Kind.CUT) {
            kind = Kind.EJECTA;
          }
        }
      }
    }
    double delta = crater;
    double trench = canyon.depthAt(x, z);
    if (trench > 0.5) {
      delta -= trench;
      if (kind == Kind.NONE || kind == Kind.EJECTA || kind == Kind.CUT || kind == Kind.BOWL) {
        kind = Kind.CANYON;
        rock = 0;
        ejecta = 0;
      }
    }
    if (Math.abs(crater) < 0.5 && trench <= 0.5) {
      double d = dunes.height(x, z);
      if (d > 0.5) {
        delta += d;
        if (kind == Kind.NONE || kind == Kind.EJECTA || kind == Kind.CUT) {
          kind = dunes.ripple(x, z) ? Kind.DUNE_RIPPLE : Kind.DUNE;
          rock = 0;
          ejecta = 0;
        }
      }
    }
    double blended = (1.0 - w) * (naturalY + delta) + w * params.pad().heightY();
    if (w > 0.5 && kind != Kind.RIM) {
      kind = Kind.PAD;
      rock = 0;
      ejecta = 0;
    }
    return new Column((int) Math.round(blended), kind, rock, ejecta);
  }
}
