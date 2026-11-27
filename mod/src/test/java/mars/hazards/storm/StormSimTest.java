package mars.hazards.storm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import mars.TestSims;
import mars.index.sim.IndexData;
import mars.index.sim.Pace;
import mars.index.sim.PlanetSim;
import mars.index.sim.PlanetState;
import mars.session.SessionClock;
import org.junit.jupiter.api.Test;

/** The storm on the planet: grid cut, panel dust notice, pause, save and load, pace. */
class StormSimTest {
  private static PlanetSim withPower(Pace pace, long seed) throws Exception {
    PlanetSim s = TestSims.stormSim(pace, seed);
    s.addMachine(TestSims.PANEL, 1000L);
    s.addMachine(TestSims.BATTERY, 5000L);
    s.addMachine(TestSims.OUTGASSER, 1L);
    return s;
  }

  @Test
  void aStormCutsSolarBySixtyPercentTimesTheCapAndGivesItBack() throws Exception {
    PlanetSim s = withPower(Pace.STANDARD, 1);
    double clear = s.power().supplyPerSol();
    assertTrue(clear > 0);
    assertTrue(s.stormStart(StormSchedule.Kind.PHASE_1));
    assertEquals(clear * 0.4, s.power().supplyPerSol(), 1e-9);
    assertEquals(0.4, s.stormSolarFactor(), 1e-9);
    assertTrue(s.stormEnd());
    assertEquals(clear, s.power().supplyPerSol(), 1e-9);
    assertEquals(1.0, s.stormSolarFactor());
  }

  @Test
  void aDustyPanelStillStacksWithTheStorm() throws Exception {
    PlanetSim s = withPower(Pace.STANDARD, 1);
    s.setEfficiency(1000L, 0.6); // stage 4
    double clear = s.power().supplyPerSol();
    s.stormStart(StormSchedule.Kind.PHASE_1);
    assertEquals(clear * 0.4, s.power().supplyPerSol(), 1e-9); // stage factor x (1 - 0.6 x 1.0)
  }

  @Test
  void theStormReportsStartAndEndAndTheDustOnlyAtTheEnd() throws Exception {
    PlanetSim s = withPower(Pace.STANDARD, 1);
    s.model().advanceSols(13.5);
    assertEquals(
        List.of(new PlanetSim.StormNotice(StormSchedule.Event.WARNED, 0)), s.drainStormNotices());
    s.model().advanceSols(1.0); // sol 14.5: onset passed
    assertEquals(
        List.of(new PlanetSim.StormNotice(StormSchedule.Event.STARTED, 0)), s.drainStormNotices());
    s.model().advanceSols(1.0); // sol 15.5, still on: no dust yet
    assertTrue(s.drainStormNotices().isEmpty());
    s.model().advanceSols(1.0); // sol 16.5: over
    assertEquals(
        List.of(new PlanetSim.StormNotice(StormSchedule.Event.ENDED, 2)), s.drainStormNotices());
  }

  @Test
  void aTwoSolStormDrainsOneBatteryButItIsSurvivable() throws Exception {
    // Balance sheet 2b: one battery (10 units) is 2.5 sols of one Outgasser; 2 sols is survivable.
    PlanetSim s = withPower(Pace.STANDARD, 1);
    s.model().advanceSols(10); // battery full
    double full = s.power().charge();
    assertEquals(s.power().capacity(), full, 1e-6);
    s.stormStart(StormSchedule.Kind.PHASE_1);
    s.model().advanceSols(2.0 - 1e-6);
    System.out.println("battery after a 2-sol storm: " + s.power().charge() + " of " + full);
    assertTrue(s.power().charge() < full, "the storm drew the battery down");
    assertTrue(s.power().charge() > 0, "one battery rides through a 2-sol storm");
  }

  @Test
  void pausedTicksChangeNothing() throws Exception {
    PlanetSim s = withPower(Pace.STANDARD, 1);
    s.model().advanceSols(13.9); // inside the warning
    PlanetState before = s.toState();
    SessionClock clock = new SessionClock(true);
    for (int i = 0; i < 100_000; i++) {
      s.onServerTick(clock);
    }
    assertEquals(before, s.toState());
    assertTrue(s.drainStormNotices().size() == 1, "only the warning from before the pause");
  }

  @Test
  void theScheduleSurvivesSaveAndLoad() throws Exception {
    PlanetSim a = withPower(Pace.STANDARD, 42);
    a.model().advanceSols(15); // mid-storm
    PlanetState st = a.toState();
    assertEquals(PlanetState.CURRENT_SCHEMA, st.schema());
    assertEquals(6, st.schema());
    assertTrue(st.storm() != null && st.storm().phase() == StormSchedule.Phase.ACTIVE);
    PlanetSim b = restore(st, Pace.STANDARD, 42);
    assertEquals(st, b.toState());
    assertEquals(a.power().supplyPerSol(), b.power().supplyPerSol(), 1e-9);
    a.model().advanceSols(30);
    b.model().advanceSols(30);
    assertEquals(a.toState().storm(), b.toState().storm());
  }

  private static PlanetSim restore(PlanetState st, Pace pace, long seed) throws Exception {
    PlanetSim.Loaded l =
        PlanetSim.fromState(
            st, TestSims.index(), TestSims.defs(), TestSims.gates(), TestSims.frost(), pace);
    l.sim().attachStorms(TestSims.stormTuning(), TestSims.dustTuning(), seed);
    return l.sim();
  }

  @Test
  void aSchemaFiveSaveLoadsWithAFreshScheduleThatWarnsFirst() throws Exception {
    PlanetSim old = TestSims.sim(Pace.STANDARD);
    old.model().advanceSols(30);
    PlanetState s6 = old.toState();
    assertEquals(null, s6.storm(), "no schedule attached: nothing saved");
    PlanetState s5 =
        new PlanetState(
            5,
            s6.values(),
            s6.simTicks(),
            s6.pendingTicks(),
            Map.of(),
            0,
            Map.of(),
            s6.gates(),
            s6.history(),
            s6.frost());
    PlanetSim loaded = restore(s5, Pace.STANDARD, 9);
    var storm = loaded.storm();
    assertEquals(StormSchedule.Phase.CALM, storm.phase());
    assertTrue(storm.startSol() >= 31 - 1e-9, "first storm at least the warning lead from sol 30");
    assertEquals(6, loaded.toState().schema());
    loaded.model().advanceSols(0.5); // the warning comes at once; onset is a sol later
    assertEquals(
        List.of(new PlanetSim.StormNotice(StormSchedule.Event.WARNED, 0)),
        loaded.drainStormNotices());
  }

  @Test
  void paceScalesLengthGapAndWarningButNeverTheCutDustOrAThreshold() throws Exception {
    PlanetSim std = withPower(Pace.STANDARD, 1);
    PlanetSim cls = withPower(Pace.classroom(4), 1);
    assertEquals(0.5, cls.storm().lengthSols(StormSchedule.Kind.PHASE_1));
    assertEquals(2.0, std.storm().lengthSols(StormSchedule.Kind.PHASE_1));
    assertEquals(0.25, cls.storm().warningSols());
    assertEquals(1.0, std.storm().warningSols());
    // Same cut and same dust in both modes.
    double stdClear = std.power().supplyPerSol();
    double clsClear = cls.power().supplyPerSol();
    std.stormStart(StormSchedule.Kind.PHASE_1);
    cls.stormStart(StormSchedule.Kind.PHASE_1);
    assertEquals(0.4, std.power().supplyPerSol() / stdClear, 1e-9);
    assertEquals(0.4, cls.power().supplyPerSol() / clsClear, 1e-9);
    std.stormEnd();
    cls.stormEnd();
    assertEquals(
        std.drainStormNotices().get(1).stagesToAdd(), cls.drainStormNotices().get(1).stagesToAdd());
    // Gate thresholds are the same objects of data in both modes (invariant I-1).
    assertEquals(
        std.gates().defs().stream().map(Object::toString).toList(),
        cls.gates().defs().stream().map(Object::toString).toList().stream().map(x -> x).toList());
    assertFalse(std.gates().defs().isEmpty());
  }

  @Test
  void aClassroomStormRunsAtFourTimesTheTimingOnTheSameClock() throws Exception {
    PlanetSim cls = withPower(Pace.classroom(4), 1);
    cls.model().advanceSols(3.9);
    assertEquals(1, cls.drainStormNotices().size()); // warned at 3.75
    cls.model().advanceSols(0.2); // 4.1: onset at 4
    assertEquals(StormSchedule.Event.STARTED, cls.drainStormNotices().get(0).event());
    cls.model().advanceSols(0.5); // 4.6: over (0.5 sol)
    assertEquals(StormSchedule.Event.ENDED, cls.drainStormNotices().get(0).event());
  }

  @Test
  void anIndexDataTypeStillLoads() throws Exception {
    IndexData.Loaded i = TestSims.index();
    assertFalse(i.parameters().isEmpty());
  }

  @Test
  void twoOutgassersOnOneBatteryRunItFlatInATwoSolStorm() throws Exception {
    // Not prepared: 8 units/sol drawn, 2 made, 6 short per sol, 10 stored: flat in about 1.7 sols.
    PlanetSim s = withPower(Pace.STANDARD, 1);
    s.addMachine(TestSims.OUTGASSER, 2L);
    s.model().advanceSols(10);
    s.stormStart(StormSchedule.Kind.PHASE_1);
    s.model().advanceSols(2.0 - 1e-6);
    assertEquals(0.0, s.power().charge(), 1e-6);
    assertTrue(
        s.power().state(s.stepSols()) != mars.power.PowerGrid.State.POWERED,
        "the Monitor says short on power");
  }
}
