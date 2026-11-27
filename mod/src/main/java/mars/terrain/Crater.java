package mars.terrain;

/** One impact crater and its height profile (blocks above or below the natural ground). */
record Crater(
    double cx,
    double cz,
    double radius,
    double depth,
    double rimHeight,
    double rimWidthFraction,
    double reach,
    boolean complex,
    double floorFraction,
    double peakHeight) {

  /** Height change at distance {@code r} from the centre. */
  double profile(double r) {
    if (r >= reach) {
      return 0.0;
    }
    double rimGauss =
        rimHeight * Math.exp(-Math.pow((r - radius) / (rimWidthFraction * radius), 2));
    if (r >= radius) {
      return Math.max(rimGauss, rimHeight * Math.pow(radius / r, 3));
    }
    double bowl;
    if (complex) {
      double floorR = floorFraction * radius;
      double peak = peakHeight * Math.exp(-Math.pow(r / (0.12 * radius), 2));
      if (r <= floorR) {
        bowl = -depth + peak;
      } else {
        bowl = -depth * (1.0 - Hash.smoothstep((r - floorR) / (radius - floorR)));
      }
    } else {
      bowl = -depth * (1.0 - (r / radius) * (r / radius));
    }
    return bowl + rimGauss;
  }

  /** True when {@code r} is on the raised rim: within about two blocks of the radius. */
  boolean onRim(double r) {
    return Math.abs(r - radius) <= Math.min(1.2 * rimWidthFraction * radius, 2.0) + 0.5;
  }

  /**
   * How much of the apron's lighter tint a column at {@code r} gets: 1 at the rim, 0 at 1.6 radii
   * (the tint stays near the crater; the raised ejecta reaches further than the tint).
   */
  double ejectaStrength(double r) {
    if (r <= radius) {
      return 0.0;
    }
    return Math.max(0.0, 1.0 - (r - radius) / (0.6 * radius));
  }
}
