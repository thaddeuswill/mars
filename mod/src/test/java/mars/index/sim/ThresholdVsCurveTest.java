package mars.index.sim;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import mars.TestData;
import mars.core.Ids;
import mars.dimension.ice.IceResult;
import mars.dimension.ice.IceRule;
import org.junit.jupiter.api.Test;

/**
 * Threshold vs. curve (content-rules.md §1.1 step 2; DESIGN.md table). Runs the balance sheet's
 * standard Phase 1 build order through the real simulation and checks that the regimes forecast F4a
 * rests on are visible long enough. Fixture numbers are from docs/design/balance-sheet.md §2
 * (invented balance numbers there); this test checks the code and data against them, not the
 * science.
 */
class ThresholdVsCurveTest {
  private static final double HEATER_LOCAL_C = 40.0; // test fixture: ice beside a running heater

  @Test
  void phaseOneRegimesAreVisible() throws Exception {
    IndexData.Loaded d = IndexData.loadDirectory(TestData.data("parameters"));
    PlanetaryIndexModel m =
        new PlanetaryIndexModel(d.parameters(), d.boilingPoint(), Pace.STANDARD);
    IceRule ice;
    try (Reader r =
        Files.newBufferedReader(TestData.data("terrain/ice_rule.json"), StandardCharsets.UTF_8)) {
      ice = IceRule.parse(r, "ice_rule.json");
    }
    // Balance sheet §2: 2 Regolith Ovens at +0.01 kPa/sol from the start; polar frost takes
    // 2 % of (pressure - 0.6) per sol while temperature is below -40 °C.
    m.contribute(Ids.PRESSURE, 0.01, "oven-1");
    m.contribute(Ids.PRESSURE, 0.01, "oven-2");
    m.addCoupling(
        (v, dt) -> {
          if (v.get(Ids.TEMPERATURE) < -40) {
            v.add(Ids.PRESSURE, -0.02 * Math.max(0, v.get(Ids.PRESSURE) - 0.6) * dt);
          }
        });
    int built = 0;
    double triplePassedSol = -1;
    double fiveKpaSol = -1;
    double boilFirst = -1;
    double boilLast = -1;
    List<String> curve = new ArrayList<>();
    for (int step = 0; m.sols() < 60 && fiveKpaSol < 0; step++) {
      double sol = m.sols();
      // cumulative build schedule: 1 by sol 9, 2 by 11, 3 by 13, 4 by 16 (balance sheet §2)
      int target = sol >= 16 ? 4 : sol >= 13 ? 3 : sol >= 11 ? 2 : sol >= 9 ? 1 : 0;
      while (built < target) {
        m.contribute(Ids.PRESSURE, 0.10, "outgasser-" + (++built));
      }
      m.step();
      double p = m.snapshot().get(Ids.PRESSURE);
      double bpC = m.snapshot().get(Ids.BOILING_POINT);
      if (triplePassedSol < 0 && p >= 0.611657) {
        triplePassedSol = m.sols();
      }
      if (fiveKpaSol < 0 && p >= 5.0) {
        fiveKpaSol = m.sols();
      }
      if (ice.evaluate(p, HEATER_LOCAL_C, bpC) == IceResult.BOIL_AWAY) {
        if (boilFirst < 0) {
          boilFirst = m.sols();
        }
        boilLast = m.sols();
      }
      if (step % 960 == 0) {
        curve.add(String.format("sol %.0f: %.2f kPa, bp %.1f °C", m.sols(), p, bpC));
      }
    }
    curve.forEach(System.out::println);
    System.out.printf(
        "triple point passed at sol %.2f; 5 kPa at sol %.2f; heater ice boils away from sol %.2f to %.2f (%.1f sols)%n",
        triplePassedSol, fiveKpaSol, boilFirst, boilLast, boilLast - boilFirst);

    // The sublimation regime is hidden (passed within ~1 sol), which is why F4a no longer rests on
    // it.
    assertTrue(
        triplePassedSol > 0 && triplePassedSol < 2,
        "triple point passed at sol " + triplePassedSol);
    // The regime F4a now rests on (heater ice boils away below 5 kPa) must last at least ~2 sols.
    assertTrue(
        boilLast - boilFirst >= 2, "boil-away regime lasted " + (boilLast - boilFirst) + " sols");
    // Phase 2 should begin in the balance sheet's window (~sol 25, accept 20-35).
    assertTrue(fiveKpaSol >= 20 && fiveKpaSol <= 35, "5 kPa reached at sol " + fiveKpaSol);
  }
}
