package mars.terrain;

/**
 * One stylised canyon: a long trench with a flat floor and stepped walls. Real Valles Marineris is
 * about 4000 km long and up to 7 km deep; here it is about 1000 blocks long and 60 deep, which is
 * labelled "stylised" wherever players can read it (docs/science/mars-terrain-reference.md). The
 * depth is limited by the world floor: the dimension starts at y 0 and the ground is at y 80. Cost
 * per column: two value-noise samples and a few multiplications.
 */
final class Canyon {
  private final TerrainParams.CanyonShape p;
  private final long seed;
  private final double dirX;
  private final double dirZ;

  Canyon(TerrainParams.CanyonShape p, long seed) {
    this.p = p;
    this.seed = seed;
    double a = Math.toRadians(p.bearingDeg());
    // Compass: 0 deg = north = -z, 90 deg = east = +x.
    this.dirX = Math.sin(a);
    this.dirZ = -Math.cos(a);
  }

  /** Depth profile in blocks (0 outside the canyon, positive inside), before wall terraces. */
  double depthAt(double x, double z) {
    if (p.depth() <= 0) {
      return 0.0;
    }
    double dx = x - p.centerX();
    double dz = z - p.centerZ();
    double a = dx * dirX + dz * dirZ;
    double half = p.length() / 2.0;
    if (Math.abs(a) >= half) {
      return 0.0;
    }
    double b = -dx * dirZ + dz * dirX;
    double wander = (Hash.valueNoise(seed, 0xCA01, a / 240.0, 0.5) - 0.5) * 2.0 * p.wander();
    double width =
        Hash.lerp(p.widthMin(), p.widthMax(), Hash.valueNoise(seed, 0xCA02, a / 300.0, 0.5));
    double s = Math.abs(b - wander) / (width / 2.0);
    if (s >= 1.0) {
      return 0.0;
    }
    double f =
        s <= p.floorFraction()
            ? 1.0
            : 1.0 - Hash.smoothstep((s - p.floorFraction()) / (1.0 - p.floorFraction()));
    // Stepped walls: snap most of the profile to benches, so layers show in the wall.
    double bench = p.benchHeight() / p.depth();
    double q = Math.rint(f / bench) * bench;
    double shaped = 0.3 * f + 0.7 * Math.min(1.0, q);
    double end = Hash.smoothstep((half - Math.abs(a)) / (p.endTaper() * p.length()));
    return p.depth() * shaped * end;
  }
}
