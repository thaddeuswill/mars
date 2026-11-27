package mars.terrain;

/**
 * Transverse dunes with one planet-wide wind direction. Height along the wind direction rises
 * gently over {@code windwardFraction} of a wavelength and falls over the rest (the slip face).
 * Cost per column: three value-noise samples and one fractional part.
 */
final class DuneField {
  private final TerrainParams.Dunes p;
  private final long seed;
  private final double windX;
  private final double windZ;

  DuneField(TerrainParams.Dunes p, double blowsTowardDeg, long seed) {
    this.p = p;
    this.seed = seed;
    double a = Math.toRadians(blowsTowardDeg);
    // Compass: 0 deg = north = -z, 90 deg = east = +x.
    this.windX = Math.sin(a);
    this.windZ = -Math.cos(a);
  }

  double windX() {
    return windX;
  }

  double windZ() {
    return windZ;
  }

  /** 0 outside a dune field, 1 well inside, smooth between. */
  double mask(double x, double z) {
    double n =
        0.65 * Hash.valueNoise(seed, 0xD00E, x / p.fieldScale(), z / p.fieldScale())
            + 0.35
                * Hash.valueNoise(
                    seed, 0xD00F, x / (p.fieldScale() / 2.3), z / (p.fieldScale() / 2.3));
    return Hash.smoothstep((n - p.thresholdLow()) / (p.thresholdHigh() - p.thresholdLow()));
  }

  /** Dune height in blocks at (x, z), at least 0. */
  double height(double x, double z) {
    double m = mask(x, z);
    if (m <= 0.0) {
      return 0.0;
    }
    double u = x * windX + z * windZ;
    double v = -x * windZ + z * windX;
    double warp =
        (Hash.valueNoise(seed, 0xD010, v / p.crestLength(), 0.5) * 2.0 - 1.0) * p.crestWarp();
    // One wavelength for the whole planet: a position-dependent one shears the crests, because
    // the phase u / wavelength is large far from the origin.
    double wavelength = p.wavelength();
    double phase = (u + warp) / wavelength;
    double band = Math.floor(phase);
    double frac = phase - band;
    double saw =
        frac < p.windwardFraction()
            ? frac / p.windwardFraction()
            : 1.0 - (frac - p.windwardFraction()) / (1.0 - p.windwardFraction());
    // The amplitude changes only at the foot of a slip face, where the height is zero.
    double amp =
        Hash.lerp(
            p.heightMin(),
            p.heightMax(),
            Hash.valueNoise(seed, 0xD012, band * 1.7, v / p.crestLength()));
    return amp * saw * m;
  }
}
