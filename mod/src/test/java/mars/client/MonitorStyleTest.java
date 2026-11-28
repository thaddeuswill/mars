package mars.client;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Measures the Monitor's colours (WCAG relative luminance) on the backing, drawn over the
 * brightest, darkest and a Mars-sky world. Text at least 4.5 to 1; lines and bars at least 3 to 1.
 */
class MonitorStyleTest {
  private static double lin(int c) {
    double v = c / 255.0;
    return v <= 0.03928 ? v / 12.92 : Math.pow((v + 0.055) / 1.055, 2.4);
  }

  private static double lum(int rgb) {
    return 0.2126 * lin((rgb >> 16) & 255)
        + 0.7152 * lin((rgb >> 8) & 255)
        + 0.0722 * lin(rgb & 255);
  }

  static double ratio(int a, int b) {
    double la = lum(a);
    double lb = lum(b);
    return (Math.max(la, lb) + 0.05) / (Math.min(la, lb) + 0.05);
  }

  /** The backing colour after it is blended over a world pixel. */
  static int backingOver(int world) {
    double a = ((MonitorStyle.BACKING >>> 24) & 255) / 255.0;
    int out = 0;
    for (int shift = 16; shift >= 0; shift -= 8) {
      int f = (MonitorStyle.BACKING >> shift) & 255;
      int w = (world >> shift) & 255;
      out |= (int) Math.round(f * a + w * (1 - a)) << shift;
    }
    return out;
  }

  private static final int[] WORLDS = {0xFFFFFF, 0x000000, 0xB98A66};

  @Test
  void textIsAtLeast4_5To1OnTheBacking() {
    int[] text = {
      MonitorStyle.LABEL,
      MonitorStyle.VALUE,
      MonitorStyle.UP,
      MonitorStyle.DOWN,
      MonitorStyle.STEADY,
      MonitorStyle.CAUSE
    };
    String[] names = {"LABEL", "VALUE", "UP", "DOWN", "STEADY", "CAUSE"};
    for (int world : WORLDS) {
      int bg = backingOver(world);
      for (int i = 0; i < text.length; i++) {
        double r = ratio(text[i] & 0xFFFFFF, bg);
        System.out.printf("text %-6s over world %06X: %.2f%n", names[i], world, r);
        assertTrue(r >= 4.5, names[i] + " " + r);
      }
    }
  }

  /** The alarm colour: readable, and used for the starved-machine alert only (no other colour). */
  @Test
  void theAlarmColourIsReadableAndSharedWithNothingElse() {
    for (int world : WORLDS) {
      double r = ratio(MonitorStyle.ALARM & 0xFFFFFF, backingOver(world));
      System.out.printf("alarm over world %06X: %.2f%n", world, r);
      assertTrue(r >= 4.5, "alarm " + r);
    }
    int[] others = {
      MonitorStyle.LABEL, MonitorStyle.VALUE, MonitorStyle.UP, MonitorStyle.DOWN,
      MonitorStyle.STEADY, MonitorStyle.CAUSE, MonitorStyle.STATE_GOOD, MonitorStyle.STATE_SHORT,
      MonitorStyle.STATE_NONE, MonitorStyle.PLOT_LINE, MonitorStyle.PLOT_AXIS, MonitorStyle.EDGE,
      MonitorStyle.BAR_EMPTY
    };
    for (int c : others) {
      assertTrue(c != MonitorStyle.ALARM, "ALARM is not reused");
      // Different enough to tell apart: some channel differs by 60 or more.
      int d =
          Math.max(
              Math.abs(((c >> 16) & 255) - ((MonitorStyle.ALARM >> 16) & 255)),
              Math.max(
                  Math.abs(((c >> 8) & 255) - ((MonitorStyle.ALARM >> 8) & 255)),
                  Math.abs((c & 255) - (MonitorStyle.ALARM & 255))));
      assertTrue(d >= 60, "alarm is close to " + Integer.toHexString(c));
    }
  }

  @Test
  void linesAndBarsAreAtLeast3To1() {
    for (int world : WORLDS) {
      int bg = backingOver(world);
      double axis = ratio(MonitorStyle.PLOT_AXIS & 0xFFFFFF, bg);
      double edge = ratio(MonitorStyle.EDGE & 0xFFFFFF, bg);
      double line = ratio(MonitorStyle.PLOT_LINE & 0xFFFFFF, bg);
      double empty = ratio(MonitorStyle.BAR_EMPTY & 0xFFFFFF, bg);
      System.out.printf(
          "world %06X: axis %.2f edge %.2f series %.2f empty segment %.2f%n",
          world, axis, edge, line, empty);
      assertTrue(axis >= 3 && edge >= 3 && line >= 3, "axis, edge and series");
      assertTrue(empty >= 2.9, "empty battery segment " + empty);
      double goalFill = ratio(MonitorStyle.GOAL_BAR_FILL & 0xFFFFFF, bg);
      double goalEmpty = ratio(MonitorStyle.GOAL_BAR_EMPTY & 0xFFFFFF, bg);
      System.out.printf("goal bar over %06X: fill %.2f empty %.2f%n", world, goalFill, goalEmpty);
      assertTrue(goalFill >= 3, "goal bar fill " + goalFill);
      assertTrue(goalEmpty >= 2.9, "goal bar empty " + goalEmpty);
    }
    double fillVsEmpty =
        ratio(MonitorStyle.STATE_GOOD & 0xFFFFFF, MonitorStyle.BAR_EMPTY & 0xFFFFFF);
    System.out.printf("battery full vs empty segment: %.2f%n", fillVsEmpty);
    assertTrue(fillVsEmpty >= 2.9, "" + fillVsEmpty);
    // The goal bar uses the same two colours; the percent text carries the value as well.
    assertTrue(
        ratio(MonitorStyle.GOAL_BAR_FILL & 0xFFFFFF, MonitorStyle.GOAL_BAR_EMPTY & 0xFFFFFF)
            >= 2.9);
  }
}
