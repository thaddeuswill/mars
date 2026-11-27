package mars.monitor.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import mars.TestSims;
import mars.gates.GateTracker;
import mars.index.sim.Pace;
import mars.index.sim.PlanetSim;
import org.junit.jupiter.api.Test;

/** The next-gate bar in the three states the player meets: far, holding, reached. */
class MonitorGoalViewTest {
  private static MonitorView.Row goal(MonitorView v) {
    return v.rows().stream()
        .filter(r -> r.labelKey().equals("monitor.row.next_goal"))
        .findFirst()
        .orElseThrow();
  }

  @Test
  void farFromTheGateTheBarIsEmptyAndTheHoldCountIsZero() throws Exception {
    MonitorView.Row g = goal(MonitorView.of(TestSims.sim(Pace.STANDARD)));
    System.out.println("far: " + g.goal());
    assertEquals("gate.mars.dome_stable", g.valueKey());
    assertEquals(0.0, g.goal().fraction(), 1e-9);
    assertEquals("0%", g.goal().percent());
    assertNull(g.goal().etaKey(), "a hold gate shows its count, not an estimate");
  }

  @Test
  void holdingShowsTheHeldFractionOfTheHold() throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 3);
    s.model().advanceSols(1.5);
    MonitorView.Row g = goal(MonitorView.of(s));
    System.out.println("holding: " + g.side() + " " + g.goal());
    assertEquals(0.5, g.goal().fraction(), 0.02);
    assertTrue(g.side().contains("/ 3 sols"), g.side());
  }

  @Test
  void pressureGoalShowsLogProgressAnEstimateAndGoalsCount() throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 3);
    s.model().advanceSols(4); // Dome stable (3 sols) latched
    s.addMachine(TestSims.OUTGASSER, 1L);
    s.model().advanceSols(10);
    MonitorView v = MonitorView.of(s);
    MonitorView.Row g = goal(v);
    double p = s.model().snapshot().get(mars.core.Ids.PRESSURE);
    System.out.println("pressure goal at " + p + ": " + g.goal() + " count " + g.detailRight());
    assertEquals("gate.mars.phase_2_begins", g.valueKey());
    assertEquals(Math.log10(p / 0.6) / Math.log10(5 / 0.6), g.goal().fraction(), 1e-9);
    assertEquals("monitor.goal.eta", g.goal().etaKey());
    assertTrue(Long.parseLong(g.goal().etaArg()) > 1);
    assertEquals("1 / 2", g.detailRight());
    assertEquals("monitor.gate.phase_2_begins.progress", g.detailKey());
  }

  @Test
  void aStalledPressureSaysNotRisingInsteadOfAnEstimate() throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 1);
    s.gates().latch("mars:dome_stable", GateTracker.Cause.ADMIN, 0);
    MonitorView.Row g = goal(MonitorView.of(s));
    assertEquals("monitor.goal.not_rising", g.goal().etaKey());
    assertNull(g.goal().etaArg());
  }

  @Test
  void whenEveryGateIsReachedThereIsNoBarOnTheGoalsReachedRow() throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 1);
    s.gates().latch("mars:dome_stable", GateTracker.Cause.ADMIN, 0);
    s.gates().latch("mars:phase_2_begins", GateTracker.Cause.ADMIN, 0);
    MonitorView v = MonitorView.of(s);
    assertTrue(v.rows().stream().anyMatch(r -> r.labelKey().equals("monitor.row.goals_reached")));
    assertTrue(v.rows().stream().noneMatch(r -> r.goal() != null));
  }
}
