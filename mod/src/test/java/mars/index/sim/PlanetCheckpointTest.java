package mars.index.sim;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import mars.TestSims;
import mars.core.Ids;
import org.junit.jupiter.api.Test;

/** Slice 8: rolling a planet back to a saved state (ARCHITECTURE.md 7.4a). */
class PlanetCheckpointTest {
  private static PlanetSim running() throws Exception {
    PlanetSim sim = TestSims.sim(Pace.STANDARD);
    TestSims.withPanels(sim, 2);
    sim.addMachine(TestSims.OUTGASSER, 1L);
    return sim;
  }

  private static PlanetSim rebuild(PlanetState s) throws Exception {
    return PlanetSim.fromState(
            s, TestSims.index(), TestSims.defs(), TestSims.gates(), TestSims.frost(), Pace.STANDARD)
        .sim();
  }

  @Test
  void restoreBringsBackValuesAndGatesButKeepsTheMachinesThatStandNow() throws Exception {
    PlanetSim sim = running();
    sim.model().advanceSols(2);
    PlanetState snapshot = sim.toState();
    double pressureThen = sim.model().snapshot().get(Ids.PRESSURE);
    assertFalse(sim.gates().isLatched("mars:dome_stable"));

    sim.model().advanceSols(6); // dome_stable holds 3 sols: latches in this stretch
    sim.addMachine(TestSims.OUTGASSER, 2L);
    assertTrue(sim.gates().isLatched("mars:dome_stable"));
    PlanetState current = sim.toState();

    PlanetState merged = PlanetCheckpoint.restoreOver(snapshot, current);
    PlanetSim back = rebuild(merged);

    System.out.println(
        "pressure then=" + pressureThen + " restored=" + back.model().snapshot().get(Ids.PRESSURE));
    assertEquals(pressureThen, back.model().snapshot().get(Ids.PRESSURE), 1e-9);
    assertEquals(snapshot.simTicks(), back.model().simTicks());
    assertFalse(back.gates().isLatched("mars:dome_stable"), "gates return to the checkpoint");
    assertEquals(2, back.machineCount(TestSims.OUTGASSER), "machines are not rolled back");
    assertEquals(2, back.machineCount(TestSims.PANEL));
  }

  @Test
  void restoreToStartKeepsMachinesAndResetsPressure() throws Exception {
    PlanetSim sim = running();
    sim.model().advanceSols(5);
    PlanetState start = TestSims.sim(Pace.STANDARD).toState();
    PlanetSim back = rebuild(PlanetCheckpoint.restoreOver(start, sim.toState()));
    assertEquals(0.6, back.model().snapshot().get(Ids.PRESSURE), 1e-9);
    assertEquals(0, back.model().simTicks());
    assertEquals(1, back.machineCount(TestSims.OUTGASSER));
    // And the planet runs on from the restored state.
    back.model().advanceSols(1);
    assertTrue(back.model().snapshot().get(Ids.PRESSURE) > 0.6);
  }

  @Test
  void listKeepsTheNewestAndReplacesSameName() {
    CheckpointList<String> l = new CheckpointList<>(2);
    assertNull(l.save("a", 1, "A"));
    assertNull(l.save("b", 2, "B"));
    assertNull(l.save("a", 3, "A2")); // replaces, order: b, a
    assertEquals("A2", l.get("a").orElseThrow().payload());
    assertEquals("b", l.save("c", 4, "C")); // b is the oldest
    assertTrue(l.get("b").isEmpty());
    assertEquals(2, l.list().size());
    assertThrows(IllegalArgumentException.class, () -> new CheckpointList<String>(0));
  }

  @Test
  void unknownNameIsEmptyAndTheListIsACopy() {
    CheckpointList<String> l = new CheckpointList<>(3);
    l.save("a", 1, "A");
    assertTrue(l.get("missing").isEmpty());
    var copy = l.list();
    l.save("b", 2, "B");
    assertEquals(1, copy.size(), "a list taken earlier does not change");
    assertThrows(UnsupportedOperationException.class, () -> copy.add(null));
  }

  @Test
  void restoreKeepsTheCurrentMachineOutputNotTheSnapshots() throws Exception {
    PlanetSim sim = running();
    PlanetState snapshot = sim.toState();
    sim.model().advanceSols(1);
    PlanetState current = sim.toState();
    PlanetState merged = PlanetCheckpoint.restoreOver(snapshot, current);
    assertEquals(current.efficiency(), merged.efficiency());
    assertEquals(current.machines(), merged.machines());
  }

  @Test
  void reportHoldsPlanetNumbersOnly() throws Exception {
    PlanetSim sim = running();
    sim.model().advanceSols(4);
    String text = PlanetReport.text("mars:planet_1", sim.toState(), Pace.STANDARD, 3, true);
    System.out.println(text);
    assertTrue(text.contains("Planet: mars:planet_1"));
    assertTrue(text.contains("Simulation time: sol 4.00"));
    assertTrue(text.contains("Session: 3 (paused)"));
    assertTrue(text.contains("mars:pressure = "));
    assertTrue(text.contains("mars:dome_stable: reached at sol"));
    assertTrue(text.contains("mars:regolith_outgasser: 1"));
    assertTrue(text.contains("No player data is in this file."));
  }
}
