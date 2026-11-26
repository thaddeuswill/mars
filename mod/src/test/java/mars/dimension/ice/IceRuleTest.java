package mars.dimension.ice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import mars.TestData;
import mars.index.sim.BoilingPoint;
import mars.index.sim.IndexData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/** The SME's ice-to-water table (docs/science/round2-review.md §1.1), read from the real data. */
class IceRuleTest {
  private static IceRule rule;
  private static BoilingPoint bp;

  @BeforeAll
  static void load() throws Exception {
    try (Reader r =
        Files.newBufferedReader(TestData.data("terrain/ice_rule.json"), StandardCharsets.UTF_8)) {
      rule = IceRule.parse(r, "ice_rule.json");
    }
    bp = IndexData.loadDirectory(TestData.data("parameters")).boilingPoint();
  }

  @ParameterizedTest(name = "{0} kPa, {1} °C -> {2}")
  @CsvSource({
    // below the triple point, warmed ice sublimates
    "0.60, 5, sublimate",
    "0.60, 40, sublimate",
    // at or above the boiling point it boils away (Phase 1 heater case)
    "0.70, 40, boil_away",
    "2.0, 18.5, boil_away",
    "4.9, 40, boil_away",
    "30, 80, boil_away",
    // between 0 °C and the boiling point: short-lived puddle up to 5 kPa
    "0.70, 1, short_puddle",
    "2.0, 10, short_puddle",
    "4.99, 20, short_puddle",
    // 5 kPa and up, between 0 °C and the boiling point: water stays
    "5.0, 10, stable_water",
    "22, 5, stable_water",
    "75, 15, stable_water",
    // at or below 0 °C: stays ice, at any pressure
    "0.60, 0, stay_ice",
    "2.0, -10, stay_ice",
    "75, 0, stay_ice"
  })
  void smeTable(double kpa, double localC, String expected) {
    double boiling = bp.atKpa(kpa);
    IceResult got = rule.evaluate(kpa, localC, boiling);
    System.out.printf(
        "ice at %.2f kPa, %.1f °C (boiling point %.2f °C): expected %s, read %s%n",
        kpa, localC, boiling, expected, got.id());
    assertEquals(expected, got.id());
  }

  @Test
  void f4aPuddleDriesBeforeTheOutcomeCheck() {
    double dry = rule.shortPuddleDrySols();
    System.out.println("short puddle dries in " + dry + " sol; F4a outcome check runs at 0.25 sol");
    assertTrue(dry < 0.25, "puddle drying time must stay under 0.25 sol (F4a), read " + dry);
  }

  @Test
  void unknownConditionFailsWithFileAndField() {
    String bad =
        "{\"rules\":[{\"result\":\"stay_ice\",\"when\":{\"temprature\":0}}],\"short_puddle_dry_sols\":0.1}";
    IllegalArgumentException e =
        assertThrows(
            IllegalArgumentException.class, () -> IceRule.parse(new StringReader(bad), "bad.json"));
    System.out.println("bad data rejected with: " + e.getMessage());
    assertTrue(e.getMessage().contains("bad.json") && e.getMessage().contains("temprature"));
  }
}
