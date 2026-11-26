package mars.index.sim;

/**
 * Boiling point of water at a given pressure, from the Antoine equation {@code log10(P) = A - B /
 * (C + T)} with P in the coefficients' pressure unit. Coefficients come from data ({@code
 * data/mars/parameters/boiling_point.json}). Below the triple point there is no liquid phase, so
 * there is no boiling point: the result is {@link Double#NaN}.
 */
public record BoilingPoint(double a, double b, double c, double kpaToUnit, double validFromKpa) {

  /** Boiling point in °C at {@code pressureKpa}, or NaN below the triple point. */
  public double atKpa(double pressureKpa) {
    if (!(pressureKpa >= validFromKpa)) {
      return Double.NaN;
    }
    double logP = Math.log10(pressureKpa * kpaToUnit);
    return b / (a - logP) - c;
  }
}
