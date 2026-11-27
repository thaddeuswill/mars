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
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 3);
    s.addMachine(TestSims.OUTGASSER, 1L);
    s.addMachine(TestSims.OUTGASSER, 2L);
    s.model().advanceSols(10);
    MonitorView v = MonitorView.of(s);
    MonitorView.Row p = row(v, "monitor.row.pressure");
    System.out.println("after 10 sols with two Outgassers: " + p);
    // With the frost sink (slice 5) ten sols of two Outgassers read 2.41 kPa, not 2.60.
    assertEquals("2.41\u00a0kPa", p.value());
    assertEquals(MonitorView.Trend.RISING, p.trend());
    assertEquals("+0.16\u00a0kPa/sol", p.trendRate()); // +0.20 machines, -0.04 frost
    assertEquals(2, p.causes().size());
    MonitorView.CauseLine frost = p.causes().get(1);
    assertEquals("monitor.cause.polar_frost", frost.sourceKey());
    assertEquals(-1, frost.direction());
    assertEquals("−0.04\u00a0kPa", frost.rate());
    assertEquals("block.mars.regolith_outgasser", p.causes().get(0).sourceKey());
    assertEquals(2, p.causes().get(0).count());
    assertEquals("+0.20 kPa/sol", p.causes().get(0).rate());
    assertEquals(10.0, v.sols(), 1e-6);
    // Above the triple point the water line gets a number.
    MonitorView.Row w = row(v, "monitor.row.water_boils");
    assertNull(w.valueKey());
    assertNotNull(w.value());
    System.out.println("water row at 2.41 kPa: " + w.value());
    assertEquals("21\u00a0°C", w.value()); // boiling point at 2.41 kPa (was 22 at 2.60)
  }

  @Test
  void aSinkReadsFalling() throws Exception {
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    s.model().contribute(Ids.PRESSURE, -0.05, "mars:polar_frost");
    MonitorView.Row p = row(MonitorView.of(s), "monitor.row.pressure");
    assertEquals(MonitorView.Trend.FALLING, p.trend());
    assertEquals("−0.05 kPa/sol", p.trendRate());
  }

  @Test
  void aStalledOutgasserSaysWhyOnItsOwnLine() throws Exception {
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    s.addMachine(TestSims.OUTGASSER, 1L);
    MonitorView v = MonitorView.of(s);
    MonitorView.CauseLine c = row(v, "monitor.row.pressure").causes().get(0);
    assertEquals("monitor.cause.needs_power", c.reasonKey());
    MonitorView.Row power = row(v, "monitor.row.power");
    assertEquals(MonitorView.Tone.NONE, power.tone());
    assertEquals(true, power.emphasis());
    assertEquals(-1.0, power.bar(), 1e-12);
  }

  @Test
  void shortPowerAndDustAreNamedAndThePowerRowIsOneSummaryLine() throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 1);
    s.addMachine(TestSims.OUTGASSER, 1L);
    s.addMachine(TestSims.OUTGASSER, 2L);
    s.addMachine(TestSims.BATTERY, 3000L);
    s.setEfficiency(1000L, 0.6);
    MonitorView v = MonitorView.of(s);
    assertEquals(
        "monitor.cause.low_power", row(v, "monitor.row.pressure").causes().get(0).reasonKey());
    MonitorView.Row power = row(v, "monitor.row.power");
    assertEquals(MonitorView.Tone.SHORT, power.tone());
    assertEquals("monitor.power.summary", power.detailKey());
    assertEquals(2, power.detailArgs().size());
    assertEquals(1, power.causes().size());
    assertEquals("monitor.cause.dusty", power.causes().get(0).reasonKey());
    assertEquals("0.0 / 10.0", power.detailRight());
    assertEquals(0.0, power.bar(), 1e-12);
  }

  @Test
  void onlyTheGoalAndProblemRowsAreEmphasised() throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 2);
    s.addMachine(TestSims.OUTGASSER, 1L);
    MonitorView v = MonitorView.of(s);
    for (MonitorView.Row r : v.rows()) {
      boolean expected =
          r.labelKey().equals("monitor.row.pressure")
              || r.labelKey().equals("monitor.row.next_goal");
      assertEquals(expected, r.emphasis(), r.labelKey());
    }
    assertEquals(MonitorView.Tone.GOOD, row(v, "monitor.row.power").tone());
  }
}
