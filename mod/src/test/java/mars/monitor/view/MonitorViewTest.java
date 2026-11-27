package mars.monitor.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import mars.TestSims;
import mars.core.Ids;
import mars.index.sim.Pace;
import mars.index.sim.PlanetSim;
import org.junit.jupiter.api.Test;

class MonitorViewTest {
  private static MonitorView.Row row(MonitorView v, String key) {
    return v.rows().stream().filter(r -> r.labelKey().equals(key)).findFirst().orElseThrow();
  }

  @Test
  void startingPlanetReadsSteadyWithNoCauses() throws Exception {
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    MonitorView v = MonitorView.of(s);
    System.out.println("start view: " + v);
    MonitorView.Row p = row(v, "monitor.row.pressure");
    assertEquals("0.60 kPa", p.value());
    assertEquals(MonitorView.Trend.STEADY, p.trend());
    assertEquals(0, p.causes().size());
    assertEquals("−60 °C", row(v, "monitor.row.temperature").value());
  }

  @Test
  void belowTheTriplePointTheWaterLineNeverShowsNaN() throws Exception {
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    MonitorView.Row w = row(MonitorView.of(s), "monitor.row.water_boils");
    System.out.println("water row at 0.60 kPa: value='" + w.value() + "' key=" + w.valueKey());
    assertEquals("monitor.water.no_liquid", w.valueKey());
    assertFalse(w.value().contains("NaN"));
    assertNull(w.trend());
  }

  @Test
  void twoOutgassersShowRisingAndANamedCause() throws Exception {
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    s.addMachine(TestSims.OUTGASSER, 1L);
    s.addMachine(TestSims.OUTGASSER, 2L);
    s.model().advanceSols(10);
    MonitorView v = MonitorView.of(s);
    MonitorView.Row p = row(v, "monitor.row.pressure");
    System.out.println("after 10 sols with two Outgassers: " + p);
    assertEquals("2.60 kPa", p.value());
    assertEquals(MonitorView.Trend.RISING, p.trend());
    assertEquals("+0.20 kPa/sol", p.trendRate());
    assertEquals(1, p.causes().size());
    assertEquals("block.mars.regolith_outgasser", p.causes().get(0).sourceKey());
    assertEquals(2, p.causes().get(0).count());
    assertEquals("+0.20 kPa/sol", p.causes().get(0).rate());
    assertEquals(10.0, v.sols(), 1e-6);
    // Above the triple point the water line gets a number.
    MonitorView.Row w = row(v, "monitor.row.water_boils");
    assertNull(w.valueKey());
    assertNotNull(w.value());
    System.out.println("water row at 2.60 kPa: " + w.value());
    assertEquals("22 °C", w.value());
  }

  @Test
  void aSinkReadsFalling() throws Exception {
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    s.model().contribute(Ids.PRESSURE, -0.05, "mars:polar_frost");
    MonitorView.Row p = row(MonitorView.of(s), "monitor.row.pressure");
    assertEquals(MonitorView.Trend.FALLING, p.trend());
    assertEquals("−0.05 kPa/sol", p.trendRate());
  }
}
