package mars.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import mars.TestSims;
import mars.dome.Probe;
import mars.index.sim.Pace;
import mars.index.sim.PlanetSim;
import mars.monitor.view.MonitorView;
import org.junit.jupiter.api.Test;

/** The Monitor's Air and Water rows, the failure lines, the Waiting line, and the height budget. */
class MonitorLifeViewTest {
  private static Probe room(boolean hole) {
    return (x, y, z) -> {
      if (hole && x == 0 && y == 2 && z == 2) {
        return Probe.Cell.OUTSIDE;
      }
      boolean wall = x <= 0 || x >= 4 || y <= 0 || y >= 4 || z <= 0 || z >= 4;
      boolean outer = x < 0 || x > 4 || y < 0 || y > 4 || z < 0 || z > 4;
      return outer ? Probe.Cell.OUTSIDE : wall ? Probe.Cell.AIRTIGHT : Probe.Cell.OPEN;
    };
  }

  private static PlanetSim dome(int makers, int panels, boolean hole) throws Exception {
    PlanetSim s = TestSims.lifeSim(Pace.STANDARD);
    for (int i = 0; i < makers; i++) {
      s.addMachine("mars:oxygen_maker", 100L + i);
    }
    s.addMachine("mars:water_reclaimer", 200L);
    for (int i = 0; i < 5; i++) {
      s.addMachine("mars:o2_tank", 300L + i);
    }
    s.addMachine(TestSims.BATTERY, 400L);
    s.addMachine(TestSims.OVEN, 500L);
    s.addMachine(TestSims.OVEN, 501L);
    for (int i = 0; i < panels; i++) {
      s.addMachine(TestSims.PANEL, 1000L + i);
      s.setEfficiency(1000L + i, 0.92);
    }
    s.domes().register(mars.dome.Pos.pack(2, 2, 2));
    s.domes().refreshDirty(room(hole));
    s.startLifeSupport();
    return s;
  }

  private static MonitorView.Row row(MonitorView v, String label) {
    return v.rows().stream().filter(r -> r.labelKey().equals(label)).findFirst().orElse(null);
  }

  private static List<String> lines(MonitorView.Row r) {
    return r.causes().stream().map(MonitorView.CauseLine::sourceKey).toList();
  }

  private static void report(String name, MonitorView v) {
    var fit = MonitorLayout.fit(v);
    System.out.printf(
        "Monitor %-28s rows=%d fixed=%d px panel(scale 3 screen 270)=%d px%n",
        name,
        fit.rows().size(),
        MonitorLayout.fixedHeight(fit),
        MonitorLayout.panelHeight(fit, 270));
  }

  @Test
  void normalAirShowsStoredOverCapacityAndANetOfPlusTwo() throws Exception {
    PlanetSim s = dome(1, 1, false);
    s.model().advanceSols(2.0);
    MonitorView v = MonitorView.of(s);
    var air = row(v, "monitor.row.air");
    assertNotNull(air);
    assertEquals(MonitorView.Trend.RISING, air.trend());
    assertEquals("+2/sol", air.trendRate().replace("−", "-"));
    assertTrue(air.value().endsWith(" / 200"), air.value());
    assertTrue(lines(air).isEmpty(), "no failure line while the net is positive");
    var water = row(v, "monitor.row.water");
    assertNotNull(water);
    assertTrue(water.value().endsWith(" / 120"), water.value());
    report("normal", v);
  }

  @Test
  void fallingAirNamesTheSolsLeftAndEmptyTanksAddTheGameRuleLine() throws Exception {
    PlanetSim s = dome(0, 1, false); // no makers: 14 units at 8 a sol = 1.75 sols
    s.model().advanceSols(1.2 + 0.5);
    var air = row(MonitorView.of(s), "monitor.row.air");
    assertEquals(List.of("monitor.air.falling"), lines(air));
    assertTrue(air.emphasis(), "under 2 sols left: the Air row is a large problem row");
    s.model().advanceSols(3.0);
    var empty = row(MonitorView.of(s), "monitor.row.air");
    assertEquals(List.of("monitor.air.empty", "monitor.air.blackout"), lines(empty));
    report("air empty", MonitorView.of(s));
  }

  @Test
  void aBreachShowsLeakingAndTheDomeStableGoalNamesTheFailingState() throws Exception {
    PlanetSim s = dome(1, 1, true);
    s.model().advanceSols(2.0);
    MonitorView v = MonitorView.of(s);
    assertEquals("monitor.dome.leaking", row(v, "monitor.row.dome").valueKey());
    var goal = row(v, "monitor.row.next_goal");
    assertEquals("monitor.gate.dome_stable.waiting.sealed", goal.detailKey());
    // Before the Landing ends the goal says so.
    PlanetSim early = dome(1, 1, false);
    assertEquals(
        "monitor.gate.dome_stable.waiting.landing",
        row(MonitorView.of(early), "monitor.row.next_goal").detailKey());
    report("breach", v);
  }

  @Test
  void aPlanetWithoutLifeSupportShowsNoAirOrWaterRow() throws Exception {
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    MonitorView v = MonitorView.of(s);
    assertEquals(null, row(v, "monitor.row.air"));
    assertEquals(null, row(v, "monitor.row.water"));
  }

  @Test
  void theBusiestLifeSupportStateStaysWithinWhatTheLayoutCanDraw() throws Exception {
    // Breach, air falling, a dusty panel and the Oven line: the worst case the layout must fit.
    PlanetSim s = dome(0, 1, true);
    s.model().advanceSols(1.2 + 0.8);
    MonitorView v = MonitorView.of(s);
    report("busiest (breach+low air)", v);
    var fit = MonitorLayout.fit(v);
    int h = MonitorLayout.panelHeight(fit, 270);
    assertTrue(
        h <= 270 - 2 * MonitorStyle.MARGIN, "fits a 270 px high GUI (scale 4 on 1080p): " + h);
  }
}
