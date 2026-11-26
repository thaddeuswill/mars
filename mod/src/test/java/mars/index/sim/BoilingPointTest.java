package mars.index.sim;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import mars.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/** The data-driven Antoine coefficients reproduce the SME's table (round2-review §1.1). */
class BoilingPointTest {
  private static BoilingPoint load() throws Exception {
    return IndexData.loadDirectory(TestData.data("parameters")).boilingPoint();
  }

  @ParameterizedTest(name = "{0} kPa -> about {1} °C")
  @CsvSource({"0.7, 2", "1, 7", "2, 18", "3, 24", "4, 29", "5, 33", "101.325, 100"})
  void matchesSmeTable(double kpa, double expectedC) throws Exception {
    double got = load().atKpa(kpa);
    System.out.printf(
        "boiling point at %.3f kPa: expected ~%.0f °C, read %.2f °C%n", kpa, expectedC, got);
    assertEquals(expectedC, got, 1.0);
  }

  @Test
  void noBoilingPointBelowTriplePoint() throws Exception {
    double got = load().atKpa(0.6);
    System.out.println("boiling point at 0.6 kPa (below triple point): read " + got);
    assertTrue(Double.isNaN(got), "expected NaN below the triple point, read " + got);
  }
}
