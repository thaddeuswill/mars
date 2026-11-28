package mars.index.sim;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import mars.TestData;
import mars.core.Ids;
import mars.core.ParameterId;
import org.junit.jupiter.api.Test;

class PlanetaryIndexModelTest {
  private static PlanetaryIndexModel model(Pace pace) throws Exception {
    IndexData.Loaded d = IndexData.loadDirectory(TestData.data("parameters"));
    return new PlanetaryIndexModel(d.parameters(), d.boilingPoint(), pace);
  }

  @Test
  void startValuesComeFromData() throws Exception {
    IndexSnapshot s = model(Pace.STANDARD).snapshot();
    System.out.println("start snapshot: " + s);
    assertEquals(0.6, s.get(Ids.PRESSURE), 1e-9);
    assertEquals(-60, s.get(Ids.TEMPERATURE), 1e-9);
    assertEquals(0.57, s.get(Ids.CO2_PARTIAL), 1e-9);
  }

  @Test
  void contributionsAreRatesPerSol() throws Exception {
    PlanetaryIndexModel m = model(Pace.STANDARD);
    m.contribute(Ids.PRESSURE, 0.10, "outgasser-1");
    m.advanceSols(10);
    double p = m.snapshot().get(Ids.PRESSURE);
    System.out.printf("one Outgasser, 10 sols: expected 1.600 kPa, read %.3f kPa%n", p);
    assertEquals(1.6, p, 1e-6);
  }

  @Test
  void classroomMultiplierAndGroupScaleTouchRatesOnly() throws Exception {
    Pace classroom4 = new Pace(4.0, 4, 24_000, 100);
    PlanetaryIndexModel m = model(classroom4);
    m.contribute(Ids.PRESSURE, 0.10, "outgasser-1");
    m.advanceSols(1);
    double expected = 0.6 + 0.10 * 4.0 / (1 + 0.35 * 3);
    double p = m.snapshot().get(Ids.PRESSURE);
    System.out.printf("classroom x4, group 4, 1 sol: expected %.4f, read %.4f%n", expected, p);
    assertEquals(expected, p, 1e-9);
  }

  @Test
  void removedHandleStopsAndValuesClamp() throws Exception {
    PlanetaryIndexModel m = model(Pace.STANDARD);
    PlanetaryIndexModel.ContributionHandle h = m.contribute(Ids.HYDROSPHERE, -5, "drain");
    m.advanceSols(1);
    System.out.println(
        "hydrosphere after a -5/sol sink from 0: read " + m.snapshot().get(Ids.HYDROSPHERE));
    assertEquals(0.0, m.snapshot().get(Ids.HYDROSPHERE), 1e-9);
    h.remove();
    m.contribute(Ids.HYDROSPHERE, 2, "melt");
    m.advanceSols(1);
    // +2 in one sol, minus the hydrosphere loss term (1 percent of the value a sol, about 0.01).
    assertEquals(2.0, m.snapshot().get(Ids.HYDROSPHERE), 0.02);
  }

  @Test
  void derivedValuesFollowStoredOnes() throws Exception {
    PlanetaryIndexModel m = model(Pace.STANDARD);
    m.contribute(Ids.PRESSURE, 74.4, "test");
    m.contribute(Ids.O2_FRACTION, 21.9, "test");
    m.advanceSols(1);
    IndexSnapshot s = m.snapshot();
    System.out.printf(
        "at %.1f kPa and %.1f %% O2: O2 partial read %.2f kPa, boiling point read %.1f °C%n",
        s.get(Ids.PRESSURE),
        s.get(Ids.O2_FRACTION),
        s.get(Ids.O2_PARTIAL),
        s.get(Ids.BOILING_POINT));
    assertEquals(75.0 * 22.0 / 100, s.get(Ids.O2_PARTIAL), 1e-6);
    assertEquals(91.8, s.get(Ids.BOILING_POINT), 1.0);
  }

  @Test
  void unknownParameterIsRejected() throws Exception {
    PlanetaryIndexModel m = model(Pace.STANDARD);
    assertThrows(
        IllegalArgumentException.class, () -> m.contribute(ParameterId.of("mars:nope"), 1, "x"));
  }
}
