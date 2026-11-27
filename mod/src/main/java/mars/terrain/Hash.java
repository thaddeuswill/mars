package mars.terrain;

/** Stateless hashing and 2-D value noise: the same inputs always give the same numbers. */
final class Hash {
  private Hash() {}

  static long mix(long a, long b) {
    long z = a + 0x9E3779B97F4A7C15L * (b + 1);
    z = (z ^ (z >>> 30)) * 0xBF58476D1CE4E5B9L;
    z = (z ^ (z >>> 27)) * 0x94D049BB133111EBL;
    return z ^ (z >>> 31);
  }

  static long mix(long seed, long salt, long x, long z) {
    return mix(mix(mix(seed, salt), x), z);
  }

  /** A number in [0, 1) derived from {@code h} and a stream index {@code i}. */
  static double unit(long h, int i) {
    return (mix(h, i) >>> 11) * 0x1.0p-53;
  }

  static double lerp(double a, double b, double t) {
    return a + (b - a) * t;
  }

  static double smoothstep(double t) {
    double c = Math.max(0.0, Math.min(1.0, t));
    return c * c * (3.0 - 2.0 * c);
  }

  /** Smooth value noise in [0, 1], lattice spacing 1. */
  static double valueNoise(long seed, long salt, double x, double z) {
    long x0 = (long) Math.floor(x);
    long z0 = (long) Math.floor(z);
    double fx = x - x0;
    double fz = z - z0;
    double sx = fx * fx * fx * (fx * (fx * 6 - 15) + 10);
    double sz = fz * fz * fz * (fz * (fz * 6 - 15) + 10);
    double a = unit(mix(seed, salt, x0, z0), 0);
    double b = unit(mix(seed, salt, x0 + 1, z0), 0);
    double c = unit(mix(seed, salt, x0, z0 + 1), 0);
    double d = unit(mix(seed, salt, x0 + 1, z0 + 1), 0);
    return lerp(lerp(a, b, sx), lerp(c, d, sx), sz);
  }
}
