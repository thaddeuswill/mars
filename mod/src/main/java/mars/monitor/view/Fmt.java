package mars.monitor.view;

import java.util.Locale;

/**
 * The one number-formatting helper for player-facing text (PROCESS.md section 4): thousands
 * separators, a true minus sign (U+2212), and units.
 */
public final class Fmt {
  public static final String MINUS = "−";

  private Fmt() {}

  /** {@code 1,234.50}-style number with {@code decimals} places and a true minus. */
  public static String number(double v, int decimals) {
    String s = String.format(Locale.ROOT, "%,." + decimals + "f", v);
    if (s.startsWith("-")) {
      // "-0.00" reads as a sign on nothing; show plain zero.
      boolean allZero = s.chars().noneMatch(c -> c >= '1' && c <= '9');
      s = allZero ? s.substring(1) : MINUS + s.substring(1);
    }
    return s;
  }

  /** Like {@link #number} but always with a sign: {@code +0.20} or {@code −0.05}. */
  public static String signed(double v, int decimals) {
    String s = number(v, decimals);
    return s.startsWith(MINUS) || v == 0 ? s : "+" + s;
  }

  public static String withUnit(double v, int decimals, String unit) {
    return number(v, decimals) + " " + unit;
  }
}
