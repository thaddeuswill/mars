package mars.monitor.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import mars.TestSims;
import mars.index.sim.Pace;
import mars.index.sim.PlanetSim;
import org.junit.jupiter.api.Test;

/** The plot data in the three states the screen must draw: empty, rising, long game. */
class MonitorHistoryViewTest {
  @Test
  void newPlanetHasNoDrawablePlot() throws Exception {
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    MonitorView.History h = MonitorView.of(s).history();
    System.out.println("new planet history: " + h);
    assertFalse(h.drawable());
    assertEquals("kPa", h.unit());
    assertEquals(1, h.points().size(), "just the current value");
  }

  @Test
  void risingPlanetHasAClimbingLineAndAnOutgasserMarker() throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 3);
    s.model().advanceSols(6);
    s.addMachine(TestSims.OUTGASSER, 1L);
    s.model().advanceSols(10);
    MonitorView.History h = MonitorView.of(s).history();
    System.out.println("rising history: " + h);
    assertTrue(h.drawable());
    var pts = h.points();
    assertTrue(pts.get(pts.size() - 1).value() > pts.get(0).value() + 0.9);
    assertEquals(16.0, pts.get(pts.size() - 1).sol(), 0.01);
    for (int i = 1; i < pts.size(); i++) {
      assertTrue(pts.get(i).value() >= pts.get(i - 1).value() - 1e-9, "never falls here");
    }
    var placed =
        h.markers().stream()
            .filter(m -> m.labelKey().equals("monitor.event.placed.regolith_outgasser"))
            .toList();
    assertEquals(1, placed.size());
    assertEquals(6.0, placed.get(0).sol(), 0.1);
    var goal = h.markers().stream().filter(m -> m.labelKey().equals("gate.reached")).findFirst();
    assertTrue(goal.isPresent());
    assertEquals("gate.mars.dome_stable", goal.get().argKey());
  }

  @Test
  void aLongGameIsThinnedToThePlotBudgetKeepingFirstAndLast() throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 3);
    s.addMachine(TestSims.OUTGASSER, 1L);
    s.model().advanceSols(119);
    assertTrue(s.history().samples().size() > MonitorView.MAX_PLOT_POINTS);
    MonitorView.History h = MonitorView.of(s).history();
    System.out.println(
        "119 sols: " + s.history().samples().size() + " stored -> " + h.points().size() + " sent");
    assertEquals(MonitorView.MAX_PLOT_POINTS, h.points().size());
    assertEquals(s.history().samples().get(0).sol(), h.points().get(0).sol(), 1e-9);
    assertEquals(119.0, h.points().get(h.points().size() - 1).sol(), 0.01);
  }
}
