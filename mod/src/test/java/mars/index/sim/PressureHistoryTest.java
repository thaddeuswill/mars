package mars.index.sim;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import mars.TestSims;
import mars.core.Ids;
import org.junit.jupiter.api.Test;

class PressureHistoryTest {
  @Test
  void oneSamplePerSolNotPerStep() {
    PressureHistory h = new PressureHistory();
    double dt = 1.0 / 12; // a real step is 100 ticks = 1/12 sol
    for (int i = 1; i <= 12 * 5; i++) {
      h.record(i * dt, 0.6 + i * 0.001);
    }
    System.out.println("5 sols of steps -> " + h.samples().size() + " samples");
    // The first step is sampled at once (sol 0.08), then one per whole sol.
    assertEquals(6, h.samples().size());
  }

  @Test
  void isBoundedAndKeepsTheWholeSpanByThinning() {
    PressureHistory h = new PressureHistory();
    for (int sol = 1; sol <= 1000; sol++) {
      h.record(sol, sol * 0.1);
      assertTrue(h.samples().size() <= PressureHistory.MAX_POINTS);
    }
    var s = h.samples();
    System.out.println(
        "1000 sols -> "
            + s.size()
            + " samples, stride "
            + h.stride()
            + ", first "
            + s.get(0)
            + ", last "
            + s.get(s.size() - 1));
    assertTrue(h.stride() > 1);
    assertEquals(1, s.get(0).sol(), 1e-9);
    assertTrue(s.get(s.size() - 1).sol() > 900, "the newest part is kept");
    for (int i = 1; i < s.size(); i++) {
      assertTrue(s.get(i).sol() > s.get(i - 1).sol(), "time order");
    }
  }

  @Test
  void eventsAreOncePerKindAndIdAndBounded() {
    PressureHistory h = new PressureHistory();
    h.addEventOnce(1, PressureHistory.Kind.MACHINE_PLACED, "mars:regolith_outgasser");
    h.addEventOnce(2, PressureHistory.Kind.MACHINE_PLACED, "mars:regolith_outgasser");
    assertEquals(1, h.events().size());
    assertEquals(1, h.events().get(0).sol(), 0);
    for (int i = 0; i < 40; i++) {
      h.addEventOnce(i, PressureHistory.Kind.GATE_LATCHED, "mars:g" + i);
    }
    assertEquals(PressureHistory.MAX_EVENTS, h.events().size());
  }

  @Test
  void restoreRepairsBadSavedData() {
    PressureHistory h = new PressureHistory();
    h.restore(
        new PressureHistory.State(
            0,
            Double.NaN,
            List.of(
                new PressureHistory.Sample(1, 0.6),
                new PressureHistory.Sample(Double.NaN, 1),
                new PressureHistory.Sample(0.5, 9), // out of order
                new PressureHistory.Sample(2, 0.7)),
            List.of(
                new PressureHistory.Event(Double.NaN, PressureHistory.Kind.GATE_LATCHED, "x"))));
    assertEquals(2, h.samples().size());
    assertEquals(1, h.stride());
    assertEquals(0, h.events().size());
  }

  @Test
  void simRecordsOnePointPerSolAndAnOutgasserEvent() throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 3);
    s.model().advanceSols(3);
    s.addMachine(TestSims.OUTGASSER, 1L);
    s.addMachine(TestSims.OUTGASSER, 2L);
    s.model().advanceSols(4);
    System.out.println("history: " + s.history().samples() + " events " + s.history().events());
    assertEquals(8, s.history().samples().size(), "sol 0, then one per sol up to 7");
    var placed =
        s.history().events().stream()
            .filter(e -> e.kind() == PressureHistory.Kind.MACHINE_PLACED)
            .toList();
    assertEquals(1, placed.size(), "two Outgassers placed, one event");
    assertEquals(3.0, placed.get(0).sol(), 0.1);
    // Solar panels make no pressure, so they leave no event.
    assertTrue(s.history().events().stream().noneMatch(e -> e.id().contains("solar")));
  }

  @Test
  void historySurvivesSaveAndLoadAndLoadingAddsNoEvents() throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 3);
    s.addMachine(TestSims.OUTGASSER, 1L);
    s.model().advanceSols(10);
    PlanetState st = s.toState();
    assertEquals(PlanetState.CURRENT_SCHEMA, st.schema());
    PlanetSim back =
        PlanetSim.fromState(st, TestSims.index(), TestSims.defs(), TestSims.gates(), Pace.STANDARD)
            .sim();
    assertEquals(s.history().samples(), back.history().samples());
    assertEquals(s.history().events(), back.history().events());
    // It keeps recording after the load, with no gap or restart.
    back.model().advanceSols(2);
    assertEquals(s.history().samples().size() + 2, back.history().samples().size());
  }

  @Test
  void olderSaveWithoutHistoryStillLoadsAndStartsEmpty() throws Exception {
    PlanetState old =
        new PlanetState(
            3,
            java.util.Map.of("mars:pressure", 2.0),
            240_000,
            0,
            java.util.Map.of(TestSims.OUTGASSER, List.of(1L)));
    PlanetSim s =
        PlanetSim.fromState(old, TestSims.index(), TestSims.defs(), TestSims.gates(), Pace.STANDARD)
            .sim();
    assertEquals(0, s.history().samples().size());
    assertEquals(0, s.history().events().size(), "loading a machine must not invent an event");
    assertEquals(2.0, s.model().snapshot().get(Ids.PRESSURE), 1e-12);
  }

  @Test
  void goalLatchLeavesAnEvent() throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 3);
    s.addMachine(TestSims.OUTGASSER, 1L);
    s.model().advanceSols(5);
    System.out.println("events: " + s.history().events());
    assertTrue(
        s.history().events().stream().anyMatch(e -> e.kind() == PressureHistory.Kind.GATE_LATCHED),
        "Dome stable latches after 3 sols of life support");
  }
}
