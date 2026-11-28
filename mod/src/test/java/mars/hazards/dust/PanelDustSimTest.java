package mars.hazards.dust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import mars.TestSims;
import mars.index.sim.Pace;
import mars.index.sim.PlanetSim;
import mars.index.sim.PlanetState;
import org.junit.jupiter.api.Test;

/** Panel dust over time through the real planet sim and data (slice 5 step A, AC-1, AC-7). */
class PanelDustSimTest {
  private static final long PANEL = 1000L;
  private static final double STEP = 1.0 / 240.0;

  private static PlanetSim sim(Pace pace) throws Exception {
    PlanetSim s = TestSims.hazardSim(pace, 42);
    s.addMachine(TestSims.PANEL, PANEL);
    return s;
  }

  /** Sols at which each stage 1..4 was first seen, stepping the sim. */
  private static double[] stageTimes(PlanetSim s, double maxSols) {
    double[] t = new double[5];
    int steps = (int) Math.round(maxSols / STEP);
    for (int i = 1; i <= steps; i++) {
      s.model().step();
      int st = s.panelStage(PANEL);
      for (int k = 1; k <= st; k++) {
        if (t[k] == 0) {
          t[k] = i * STEP;
        }
      }
    }
    return t;
  }

  @Test
  void standardPhase1PanelReachesStages1To4AtSols3_6_9_12() throws Exception {
    double[] t = stageTimes(sim(Pace.STANDARD), 13);
    System.out.println("standard stage sols: " + java.util.Arrays.toString(t));
    for (int k = 1; k <= 4; k++) {
      assertEquals(3.0 * k, t[k], STEP * 1.01, "stage " + k);
    }
  }

  @Test
  void classroomPanelReachesStagesAtQuarterTimes() throws Exception {
    double[] t = stageTimes(sim(Pace.classroom(4)), 3.2);
    System.out.println("classroom stage sols: " + java.util.Arrays.toString(t));
    for (int k = 1; k <= 4; k++) {
      assertEquals(0.75 * k, t[k], STEP * 1.01, "stage " + k);
    }
  }

  @Test
  void laterPhasesGoSlowerBecauseTheCapFalls() throws Exception {
    var tuning = TestSims.hazards().dust();
    assertEquals(3.75, tuning.intervalSols(0.8, 1.0), 1e-9);
    assertEquals(6.0, tuning.intervalSols(0.5, 1.0), 1e-9);
    assertEquals(12.0, tuning.intervalSols(0.25, 1.0), 1e-9);
    assertEquals(0.9375, tuning.intervalSols(0.8, 4.0), 1e-9);
    PlanetSim s = sim(Pace.STANDARD);
    s.gates().latch("mars:phase_2_begins", mars.gates.GateTracker.Cause.ADMIN, 0);
    assertEquals(0.8, s.stormCap(), 1e-12);
    assertEquals(3.75, s.dustIntervalSols(), 1e-9);
  }

  @Test
  void aPanelStopsAtStage4AndTheGridSeesTheFloor() throws Exception {
    PlanetSim s = sim(Pace.STANDARD);
    s.model().advanceSols(13);
    assertEquals(4, s.panelStage(PANEL));
    assertEquals(5.0 * 0.60, s.power().supplyPerSol(), 1e-9);
    assertEquals(40, s.panelDustLossPercent());
    s.model().advanceSols(5);
    assertEquals(4, s.panelStage(PANEL));
    assertEquals(5.0 * 0.60, s.power().supplyPerSol(), 1e-9);
  }

  @Test
  void pausedTicksChangeNothing() throws Exception {
    PlanetSim s = sim(Pace.STANDARD);
    s.model().advanceSols(4);
    PlanetState before = s.toState();
    var clock = new mars.session.SessionClock(true);
    for (int i = 0; i < 100_000; i++) {
      s.onServerTick(clock);
    }
    assertEquals(before, s.toState());
  }

  @Test
  void saveThenLoadGivesTheSameStageAndTheSameFuture() throws Exception {
    PlanetSim a = sim(Pace.STANDARD);
    a.model().advanceSols(4.5);
    PlanetState saved = a.toState();
    assertEquals(PlanetState.CURRENT_SCHEMA, saved.schema());
    assertEquals(1, a.panelStage(PANEL));
    PlanetSim b =
        PlanetSim.fromState(
                saved,
                TestSims.index(),
                TestSims.defs(),
                TestSims.gates(),
                TestSims.frost(),
                Pace.STANDARD)
            .sim();
    b.configureHazards(TestSims.hazards(), 999); // the saved seed wins
    assertEquals(42, b.planetSeed());
    assertEquals(1, b.panelStage(PANEL));
    assertEquals(saved, b.toState());
    a.model().advanceSols(2);
    b.model().advanceSols(2);
    assertEquals(2, a.panelStage(PANEL));
    assertEquals(a.toState(), b.toState());
  }

  @Test
  void aWorldSavedBeforeSchema8GetsItsStageFromTheSavedOutput() throws Exception {
    // Schema 7: no hazards, efficiency 0.70 = stage 3, 0.91 = stage 2 partly cleaned.
    PlanetState old =
        new PlanetState(
            7,
            Map.of("mars:pressure", 3.0),
            24_000,
            0,
            Map.of(TestSims.PANEL, List.of(PANEL, PANEL + 1, PANEL + 2)),
            0,
            Map.of(PANEL, 0.70, PANEL + 1, 0.91, PANEL + 2, 0.123));
    PlanetSim s = PlanetSim.fromState(old, TestSims.index(), TestSims.defs(), Pace.STANDARD).sim();
    s.configureHazards(TestSims.hazards(), 7);
    assertEquals(3, s.panelStage(PANEL));
    assertEquals(2, s.panelStage(PANEL + 1));
    assertTrue(s.panelPartlyCleaned(PANEL + 1));
    assertEquals(0, s.panelStage(PANEL + 2), "no match: stage 0");
    assertEquals(0.70, 5.0 * 0.70 / 5.0, 1e-12);
    assertEquals(8, s.toState().schema());
    assertEquals(7, s.planetSeed());
  }

  @Test
  void aStageSetByABlockWinsAndANewPanelStartsClean() throws Exception {
    PlanetSim s = sim(Pace.STANDARD);
    s.setPanelDust(PANEL, 3, false, 0.70);
    assertEquals(3, s.panelStage(PANEL));
    assertEquals(3.5, s.power().supplyPerSol(), 1e-9);
    s.setPanelDust(PANEL, 3, false, 0.70); // the game's own sync write: nothing happens
    assertTrue(s.drainBlockSync().isEmpty());
    s.addMachine(TestSims.PANEL, PANEL + 1);
    assertEquals(0, s.panelStage(PANEL + 1));
  }

  @Test
  void aStageChangeIsQueuedForTheBlockOnceAndTheOutputFollows() throws Exception {
    PlanetSim s = sim(Pace.STANDARD);
    s.model().advanceSols(3.1);
    var sync = s.drainBlockSync();
    assertEquals(1, sync.size());
    assertEquals(1, sync.get(0).stage());
    assertEquals(PANEL, sync.get(0).pos());
    assertTrue(s.drainBlockSync().isEmpty());
    assertEquals(5.0 * 0.92, s.power().supplyPerSol(), 1e-9);
  }

  @Test
  void aPanelInAnUnloadedChunkStillDustsAndIsFoundByChunk() throws Exception {
    PlanetSim s = sim(Pace.STANDARD);
    s.model().advanceSols(6.1); // nobody drains the sync queue: the chunk is "unloaded"
    assertEquals(2, s.panelStage(PANEL));
    var found = s.panelsInChunk(PanelDustModel.chunkKey(PANEL));
    assertEquals(1, found.size());
    assertEquals(2, found.get(0).stage());
    assertTrue(s.panelsInChunk(PanelDustModel.chunkKey(PANEL) + 1).isEmpty());
  }

  @Test
  void cleaningBringsStageZeroBack() throws Exception {
    PlanetSim s = sim(Pace.STANDARD);
    s.model().advanceSols(9.5);
    assertEquals(3, s.panelStage(PANEL));
    assertEquals(3, s.cleanPanel(PANEL));
    assertEquals(0, s.panelStage(PANEL));
    assertEquals(5.0, s.power().supplyPerSol(), 1e-9);
    assertEquals(-1, s.cleanPanel(PANEL + 5));
  }

  @Test
  void removingAPanelForgetsItsDust() throws Exception {
    PlanetSim s = sim(Pace.STANDARD);
    s.model().advanceSols(4);
    s.removeMachine(TestSims.PANEL, PANEL);
    assertTrue(s.toState().hazards().dust().isEmpty());
    assertEquals(0, s.panelStage(PANEL));
  }

  @Test
  void perStepCostWith300PanelsIsSmall() throws Exception {
    PlanetSim s = TestSims.hazardSim(Pace.STANDARD, 1);
    for (int i = 0; i < 300; i++) {
      s.addMachine(TestSims.PANEL, 5000L + i * 7);
    }
    long t0 = System.nanoTime();
    s.model().advanceSols(1);
    double microsPerStep = (System.nanoTime() - t0) / 1e3 / 240.0;
    System.out.printf("300 panels: %.1f us per step (240 steps per sol)%n", microsPerStep);
    assertTrue(microsPerStep < 5_000, "a step must stay far below one tick (50 ms)");
  }

  @Test
  void dustDevilIsSeededAndOneInAHundredPerSol() {
    int hits = 0;
    int n = 100_000;
    for (long sol = 0; sol < n; sol++) {
      boolean a = PanelDustModel.devilStrikes(42, 7, sol, 0.01);
      assertEquals(a, PanelDustModel.devilStrikes(42, 7, sol, 0.01), "same inputs, same answer");
      if (a) {
        hits++;
      }
    }
    System.out.println("devil hits per " + n + " sols: " + hits);
    assertEquals(1000, hits, 200);
    assertFalse(PanelDustModel.devilStrikes(42, 7, 5, 0.0));
  }

  @Test
  void aDevilOnlyTouchesPanelsAtStage2OrMoreAndOnlyMakesThemPartlyCleaned() {
    PanelDustModel m = new PanelDustModel(4);
    m.put(10, 1, false);
    m.put(11, 2, false);
    m.put(12, 4, false);
    // chance 1.0: every chunk is hit
    var changed = m.devil(1, 5, 1.0, 2);
    assertEquals(List.of(11L, 12L), changed);
    assertFalse(m.partlyCleaned(10));
    assertTrue(m.partlyCleaned(11));
    assertEquals(2, m.stage(11));
    assertEquals(4, m.stage(12));
  }
}
