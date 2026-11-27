package mars.visual;

/**
 * sRGB to OKLab conversion (Bjorn Ottosson's published matrices). Colours are mixed in OKLab so a
 * tan-to-blue blend does not pass through a muddy grey (planetary-keyframes 1.3). Pure Java.
 */
public final class Oklab {
  private Oklab() {}

  /** Converts 0xRRGGBB to {L, a, b}. */
  public static double[] fromRgb(int rgb) {
    double r = toLinear(((rgb >> 16) & 0xFF) / 255.0);
    double g = toLinear(((rgb >> 8) & 0xFF) / 255.0);
    double b = toLinear((rgb & 0xFF) / 255.0);
    double l = Math.cbrt(0.4122214708 * r + 0.5363325363 * g + 0.0514459929 * b);
    double m = Math.cbrt(0.2119034982 * r + 0.6806995451 * g + 0.1073969566 * b);
    double s = Math.cbrt(0.0883024619 * r + 0.2817188376 * g + 0.6299787005 * b);
    return new double[] {
      0.2104542553 * l + 0.7936177850 * m - 0.0040720468 * s,
      1.9779984951 * l - 2.4285922050 * m + 0.4505937099 * s,
      0.0259040371 * l + 0.7827717662 * m - 0.8086757660 * s
    };
  }

  /** Converts {L, a, b} to 0xRRGGBB, clamped to the sRGB range. */
  public static int toRgb(double[] lab) {
    double l = Math.pow(lab[0] + 0.3963377774 * lab[1] + 0.2158037573 * lab[2], 3);
    double m = Math.pow(lab[0] - 0.1055613458 * lab[1] - 0.0638541728 * lab[2], 3);
    double s = Math.pow(lab[0] - 0.0894841775 * lab[1] - 1.2914855480 * lab[2], 3);
    double r = 4.0767416621 * l - 3.3077115913 * m + 0.2309699292 * s;
    double g = -1.2684380046 * l + 2.6097574011 * m - 0.3413193965 * s;
    double b = -0.0041960863 * l - 0.7034186147 * m + 1.7076147010 * s;
    return (to8(r) << 16) | (to8(g) << 8) | to8(b);
  }

  /** Mixes two colours in OKLab; t = 0 gives exactly {@code a}, t = 1 exactly {@code b}. */
  public static int mix(int a, int b, double t) {
    if (t <= 0) {
      return a;
    }
    if (t >= 1) {
      return b;
    }
    double[] x = fromRgb(a);
    double[] y = fromRgb(b);
    return toRgb(
        new double[] {
          x[0] + (y[0] - x[0]) * t, x[1] + (y[1] - x[1]) * t, x[2] + (y[2] - x[2]) * t
        });
  }

  private static double toLinear(double c) {
    return c <= 0.04045 ? c / 12.92 : Math.pow((c + 0.055) / 1.055, 2.4);
  }

  private static int to8(double linear) {
    double c = Math.max(0, Math.min(1, linear));
    double e = c <= 0.0031308 ? c * 12.92 : 1.055 * Math.pow(c, 1 / 2.4) - 0.055;
    return (int) Math.round(Math.max(0, Math.min(1, e)) * 255.0);
  }
}
