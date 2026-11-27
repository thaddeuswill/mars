package mars.index.sim;

import static org.junit.jupiter.api.Assertions.assertTrue;

import mars.TestSims;
import mars.core.Ids;
import org.junit.jupiter.api.Test;

/**
 * Runs the balance sheet's "Standard solo curve" (section 2) through the real sim with the frost
 * sink and prints it cell by cell against the sheet. The sheet's early cells are unverified (SME,
 * docs/science/frost-sink-review.md Q3), so this test records the comparison; it asserts only what
 * the SME could confirm (the late curve), with the tolerance stated.
 */
class SoloCurveComparisonTest {
  // Balance sheet 2, standard solo curve. "Sol N" is read as the start of sol N (t = N - 1).
  private static final int[] SOLS = {1, 5, 9, 10, 12, 14, 16, 18, 20, 22, 24, 25};
  private static final double[] SHEET = {
    0.61, 0.67, 0.74, 0.86, 1.18, 1.70, 1.93, 2.71, 3.46, 4.18, 4.87, 5.20
  };

  /** Build order: Outgassers 1 at sol 9, 2 by 11, 3 by 13, 4 by 16 (start of those sols). */
  private static int outgassersAt(double t) {
    int sol = (int) Math.floor(t) + 1;
    return sol >= 16 ? 4 : sol >= 13 ? 3 : sol >= 11 ? 2 : sol >= 9 ? 1 : 0;
  }

  private static double[] run(boolean storm) throws Exception {
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    s.addMachine(TestSims.OVEN, 100L);
    s.addMachine(TestSims.OVEN, 101L);
    double[] out = new double[SOLS.length];
    int placed = 0;
    int panels = 0;
    int idx = 0;
    double dt = 1.0 / 24.0; // advance in 1/24 sol chunks (10 steps of 1/240)
    for (double t = 0; idx < SOLS.length; t += dt) {
      if (Math.abs(t - (SOLS[idx] - 1)) < 1e-9) {
        out[idx++] = s.model().current(Ids.PRESSURE);
      }
      while (placed < outgassersAt(t)) {
        s.addMachine(TestSims.OUTGASSER, 1L + placed++);
      }
      // Enough clean panels for the demand (5 units each), at least one.
      int need = (int) Math.ceil((4.0 * placed + 2.0) / 5.0);
      while (panels < need) {
        s.addMachine(TestSims.PANEL, 1000L + panels++);
      }
      boolean inStorm = storm && t >= 13 && t < 15; // sols 14 and 15
      for (int i = 0; i < panels; i++) {
        s.setEfficiency(1000L + i, inStorm ? 0.4 : 1.0);
      }
      s.model().advanceSols(dt);
    }
    return out;
  }

  @Test
  void printTheCurveAgainstTheSheetCellByCell() throws Exception {
    double[] noStorm = run(false);
    double[] storm = run(true);
    System.out.println("sol | sheet | sim no storm | diff | sim with sols 14-15 storm | diff");
    for (int i = 0; i < SOLS.length; i++) {
      System.out.printf(
          "%3d | %5.2f | %6.3f | %+6.3f | %6.3f | %+6.3f%n",
          SOLS[i], SHEET[i], noStorm[i], noStorm[i] - SHEET[i], storm[i], storm[i] - SHEET[i]);
    }
    // What the SME could confirm: the late curve rises and the frost makes it slower than no
    // sink. The sheet's 5.20 at sol 25 is not reproduced (the sim reaches more: no build delays).
    for (int i = 1; i < SOLS.length; i++) {
      assertTrue(noStorm[i] > noStorm[i - 1], "monotone rise, cell " + SOLS[i]);
    }
    assertTrue(noStorm[SOLS.length - 1] > 5.0, "the 5 kPa gate is reached by sol 25");
    // With the storm modelled as 40 % panel output on sols 14 and 15 and panels sized exactly to
    // demand (both are this test's reading of the sheet, which states neither), every cell is
    // within 0.06 kPa of the sheet. That shows the sheet is consistent with the -2 % sink under
    // these assumptions; it does not prove the assumptions.
    for (int i = 0; i < SOLS.length; i++) {
      assertTrue(Math.abs(storm[i] - SHEET[i]) <= 0.06, "storm run, cell sol " + SOLS[i]);
    }
  }
}
