package mars.monitor.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import mars.TestSims;
import mars.core.Ids;
import mars.index.sim.Pace;
import mars.index.sim.PlanetSim;
import org.junit.jupiter.api.Test;

/** The Monitor's polar-frost cause line and the "Levelling off" note (slice 5, ui-specs 9). */
class MonitorFrostViewTest {
  private static MonitorView.Row pressure(PlanetSim s) {
    return MonitorView.of(s).rows().stream()
        .filter(r -> r.labelKey().equals("monitor.row.pressure"))
        .findFirst()
        .orElseThrow();
  }

  private static MonitorView.CauseLine frostLine(MonitorView.Row r) {
    return r.causes().stream().filter(MonitorView.CauseLine::isPlanetSide).findFirst().orElse(null);
  }

  @Test
  void theFrostLineIsHiddenAtBaselineAndAppearsNearPointEightFiveKpa() throws Exception {
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    assertNull(frostLine(pressure(s)), "hidden at the 0.60 start");
    s.model().set(Ids.PRESSURE, 0.84);
    assertNull(frostLine(pressure(s)), "0.84 kPa: 2 % x 0.24 = 0.0048, rounds to 0.00");
    s.model().set(Ids.PRESSURE, 0.86);
    MonitorView.CauseLine c = frostLine(pressure(s));
    System.out.println("frost line at 0.86 kPa: " + c);
    assertEquals("monitor.cause.polar_frost", c.sourceKey());
    assertEquals(-1, c.direction());
    assertEquals("−0.01 kPa", c.rate(), "a minus sign in the text, a down shape on screen");
  }

  @Test
  void theFrostLineShowsTheRealRateAtFiveKpaAndTheTrendIncludesIt() throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 1);
    s.addMachine(TestSims.OUTGASSER, 1L);
    s.model().set(Ids.PRESSURE, 5.0);
    MonitorView.Row p = pressure(s);
    MonitorView.CauseLine c = frostLine(p);
    assertEquals("−0.09 kPa", c.rate()); // -0.088
    assertEquals("+0.01 kPa/sol", p.trendRate()); // 0.100 - 0.088
    assertEquals(2, p.causes().size(), "the machine line and the frost line");
  }

  @Test
  void warmSkyReadsGivesBack() throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 1);
    s.addMachine(TestSims.OUTGASSER, 1L);
    s.model().set(Ids.PRESSURE, 3.0);
    s.model().advanceSols(5); // the poles hold some air now
    s.model().set(Ids.TEMPERATURE, -10);
    MonitorView.CauseLine c = frostLine(pressure(s));
    System.out.println("warm frost line: " + c);
    assertEquals("monitor.cause.polar_frost_release", c.sourceKey());
    assertEquals(1, c.direction());
    assertEquals("+0.50 kPa", c.rate());
  }

  @Test
  void levellingOffAppearsWhenTheFrostBalancesTheSourcesAndNotBefore() throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 1);
    s.addMachine(TestSims.OVEN, 10L);
    s.addMachine(TestSims.OVEN, 11L);
    assertNull(pressure(s).noteKey(), "just started: rising, no note");
    s.model().advanceSols(10);
    assertNull(pressure(s).noteKey(), "10 sols in (1.5 kPa is not near 1.6 yet): still rising");
    s.model().advanceSols(300);
    MonitorView.Row p = pressure(s);
    System.out.println("after 310 sols with two Ovens: " + p.value() + " note=" + p.noteKey());
    assertEquals("monitor.estimate.levelling_off", p.noteKey());
    assertEquals(MonitorView.Trend.STEADY, p.trend());
    // No sources: nothing to level off.
    PlanetSim none = TestSims.sim(Pace.STANDARD);
    none.model().set(Ids.PRESSURE, 3.0);
    assertNull(pressure(none).noteKey());
    // Once the pressure goal has latched, the note goes (the goal is reached).
    PlanetSim done = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 1);
    done.addMachine(TestSims.OVEN, 10L);
    done.model().set(Ids.PRESSURE, 5.2); // the frost pulls it down a hair before the gate reads it
    done.model().advanceSols(0.05);
    assertTrue(done.gates().isLatched("mars:phase_2_begins"));
    done.model().set(Ids.PRESSURE, 1.6);
    done.model().advanceSols(1);
    assertFalse("monitor.estimate.levelling_off".equals(pressure(done).noteKey()));
  }

  @Test
  void theOvenNamesAPowerReasonLikeAnyMachine() throws Exception {
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    s.addMachine(TestSims.OVEN, 10L);
    MonitorView.CauseLine c = pressure(s).causes().get(0);
    assertEquals("block.mars.regolith_oven", c.sourceKey());
    assertEquals("monitor.cause.needs_power", c.reasonKey());
  }
}
