package mars.hazards.storm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import mars.TestSims;
import mars.core.AttributedSources;
import mars.hazards.storm.StormScheduler.Kind;
import mars.index.sim.Pace;
import mars.index.sim.PlanetSim;
import org.junit.jupiter.api.Test;

/** The storm through the real planet sim: the power cut, the deposit, the lock point (step D). */
class StormSimTest {
  private static final double STEP = 1.0 / 240.0;
  private static final long PANEL = 1000L;

  /** A planet with one stage-1 panel (the starting panel). */
  private static PlanetSim planet(Pace pace) throws Exception {
    PlanetSim s = TestSims.hazardSim(pace, 3);
    s.addMachine(TestSims.PANEL, PANEL);
    s.setPanelDust(PANEL, 1, false, 0.92);
    return s;
  }

  private static void steps(PlanetSim s, int n) {
    for (int i = 0; i < n; i++) {
      s.model().step();
    }
  }

  /** Step until the sim time reaches {@code sol}. */
  private static void until(PlanetSim s, double sol) {
    while (s.model().sols() < sol - 1e-9) {
      s.model().step();
    }
  }

  @Test
  void aStageOnePanelInAFullStormMakesTheSpecValueForEveryCap() throws Exception {
    var tuning = TestSims.hazards().dust();
    double[] caps = {1.0, 0.8, 0.5, 0.25};
    double[] want = {1.84, 2.39, 3.22, 3.91}; // spec 2.5, 5 x 0.92 x (1 - 0.6 x cap)
    for (int i = 0; i < caps.length; i++) {
      assertEquals(want[i], 5.0 * tuning.outputInStorm(1, false, caps[i]), 0.005, "cap " + caps[i]);
    }
    // Through the sim, cap 1.0 (Phase 1) and 0.8 (Phase 2 latched), at full strength.
    // The panel is brushed back to stage 1 just before (uncleaned it would be stage 4 by sol 14).
    PlanetSim p1 = planet(Pace.STANDARD);
    until(p1, 13.9);
    p1.setPanelDust(PANEL, 1, false, 0.92);
    until(p1, 14.2);
    assertEquals(1.0, p1.stormStrength(), 1e-12);
    assertEquals(5.0 * 0.92 * 0.4, p1.power().supplyPerSol(), 1e-9);
    PlanetSim p2 = planet(Pace.STANDARD);
    p2.gates().latch("mars:phase_2_begins", mars.gates.GateTracker.Cause.ADMIN, 0);
    until(p2, 13.9);
    p2.setPanelDust(PANEL, 1, false, 0.92);
    until(p2, 14.2);
    assertEquals(0.8, p2.stormStrength(), 1e-12);
    assertEquals(5.0 * 0.92 * (1 - 0.6 * 0.8), p2.power().supplyPerSol(), 1e-9);
  }

  @Test
  void supplyEqualsItsPreWarningValueUntilTheOnsetStepAndDropsInThatStep() throws Exception {
    PlanetSim s = planet(Pace.STANDARD);
    var dust = TestSims.hazards().dust();
    boolean warned = false;
    boolean onset = false;
    int onsetStepDrop = 0;
    while (s.model().sols() < 15.0) {
      double sBefore = s.power().supplyPerSol();
      s.model().step();
      for (var n : s.drainStormEvents()) {
        warned |= n.event().kind() == Kind.WARNED;
        if (n.event().kind() == Kind.ONSET) {
          onset = true;
          assertTrue(warned, "the warning comes first");
          assertTrue(
              s.current(AttributedSources.STORM_SOLAR_CUT) < 0, "the cut starts in the onset step");
          onsetStepDrop++;
        }
      }
      if (!onset) {
        // Before onset the supply is only the panel's dust stage (slow dust, not the storm).
        assertEquals(
            5.0 * dust.outputFraction(s.panelStage(PANEL), s.panelPartlyCleaned(PANEL)),
            s.power().supplyPerSol(),
            1e-9,
            "no storm effect before onset, sol " + s.model().sols());
        assertEquals(0.0, s.stormStrength(), 0.0);
        assertEquals(0.0, s.current(AttributedSources.STORM_SOLAR_CUT), 0.0);
      }
    }
    assertTrue(warned && onset);
    assertEquals(1, onsetStepDrop);
  }

  @Test
  void panelsKeepTheirStageDuringTheStormAndGetThemAtTheEndInTwoSteps() throws Exception {
    PlanetSim s = planet(Pace.STANDARD);
    until(s, 13.9);
    // Stage so far: 1 (sol 3 would add one; clean the panel's progress at the start for a clear
    // read).
    s.setPanelDust(PANEL, 1, false, 0.92);
    s.drainStormEvents();
    List<String> log = new ArrayList<>();
    int lastStage = s.panelStage(PANEL);
    while (s.model().sols() < 15.0) {
      s.model().step();
      for (var n : s.drainStormEvents()) {
        log.add(n.event().kind() + "@" + String.format("%.3f", s.model().sols()));
      }
      int st = s.panelStage(PANEL);
      if (st != lastStage) {
        log.add("stage " + lastStage + "->" + st + "@" + String.format("%.3f", s.model().sols()));
        lastStage = st;
      }
    }
    System.out.println(log);
    int onsetIdx = indexOf(log, "ONSET");
    int clearIdx = indexOf(log, "CLEARING");
    int endIdx = indexOf(log, "ENDED");
    List<Integer> stageIdx = new ArrayList<>();
    for (int i = 0; i < log.size(); i++) {
      if (log.get(i).startsWith("stage")) {
        stageIdx.add(i);
      }
    }
    assertEquals(2, stageIdx.size(), "two deposit steps in Phase 1: " + log);
    assertTrue(stageIdx.get(0) > clearIdx - 1 && stageIdx.get(0) < endIdx, log.toString());
    assertTrue(stageIdx.get(1) > endIdx - 1, log.toString());
    assertTrue(stageIdx.get(0) > onsetIdx);
    assertEquals(3, s.panelStage(PANEL));
  }

  private static int indexOf(List<String> log, String prefix) {
    for (int i = 0; i < log.size(); i++) {
      if (log.get(i).startsWith(prefix)) {
        return i;
      }
    }
    throw new AssertionError(prefix + " missing in " + log);
  }

  @Test
  void theDepositIsOneStageFromPhase3OnCapsAtFourAndClearsPartlyCleaned() throws Exception {
    var dust = TestSims.hazards().dust();
    assertEquals(2, dust.stormEndStagesFor(1.0));
    assertEquals(2, dust.stormEndStagesFor(0.8));
    assertEquals(1, dust.stormEndStagesFor(0.5));
    assertEquals(1, dust.stormEndStagesFor(0.25));
    var m = new mars.hazards.dust.PanelDustModel(4);
    m.put(1, 3, true);
    m.put(2, 4, true);
    m.deposit(2);
    assertEquals(4, m.stage(1));
    assertFalse(m.partlyCleaned(1));
    assertEquals(4, m.stage(2));
    assertFalse(m.partlyCleaned(2));
  }

  @Test
  void theAttributedStormCutIgnoresWhatThePlayerDoesMidStorm() throws Exception {
    PlanetSim s = TestSims.hazardSim(Pace.STANDARD, 3);
    s.addMachine(TestSims.PANEL, PANEL);
    s.addMachine(TestSims.PANEL, PANEL + 1);
    s.setPanelDust(PANEL, 4, false, 0.6);
    s.setPanelDust(PANEL + 1, 4, false, 0.6);
    double pre = s.power().supplyPerSol(); // 6.0
    until(s, 14.2); // full storm
    double inStorm = s.power().supplyPerSol();
    double cutBefore = s.current(AttributedSources.STORM_SOLAR_CUT);
    assertTrue(inStorm < pre);
    assertTrue(cutBefore <= -0.01, "the storm removes supply: " + cutBefore);
    // The player adds four clean panels in the storm: NET supply now rises above its pre-storm
    // value.
    for (int i = 0; i < 4; i++) {
      s.addMachine(TestSims.PANEL, PANEL + 10 + i);
    }
    double netNow = s.power().supplyPerSol();
    System.out.printf(
        "pre-storm %.2f, in storm %.2f, after adding panels %.2f, storm cut %.2f%n",
        pre, inStorm, netNow, s.current(AttributedSources.STORM_SOLAR_CUT));
    assertTrue(netNow > pre, "net power rose above the pre-storm value (negative control)");
    assertTrue(
        s.current(AttributedSources.STORM_SOLAR_CUT) <= -0.01,
        "the attributed cut still says the storm took supply");
    // Brushing does not remove the cut either.
    s.cleanPanel(PANEL);
    assertTrue(s.current(AttributedSources.STORM_SOLAR_CUT) <= -0.01);
    // The peak since the onset keeps the largest cut.
    assertTrue(
        s.peakSince(AttributedSources.STORM_SOLAR_CUT, 0)
            <= s.current(AttributedSources.STORM_SOLAR_CUT));
    assertEquals(0.0, s.current("mars:no_such_source"));
  }

  @Test
  void noPanelMeansTheWarningGoesOutVoid() throws Exception {
    PlanetSim s = TestSims.hazardSim(Pace.STANDARD, 3); // no panel at all
    List<StormScheduler.Event> ev = new ArrayList<>();
    while (s.model().sols() < 19) {
      s.model().step();
      s.drainStormEvents().forEach(n -> ev.add(n.event()));
    }
    var warned = ev.stream().filter(e -> e.kind() == Kind.WARNED).findFirst().orElseThrow();
    assertTrue(warned.voided());
  }

  @Test
  void theLandingGuardHoldsTheStormUntilSomeoneHasFinishedIt() throws Exception {
    PlanetSim s = planet(Pace.STANDARD);
    s.setLandingDone(false);
    List<StormScheduler.Event> ev = new ArrayList<>();
    until(s, 13.9);
    s.drainStormEvents();
    s.setLandingDone(true);
    while (s.model().sols() < 16) {
      s.model().step();
      s.drainStormEvents().forEach(n -> ev.add(n.event()));
    }
    var warned = ev.stream().filter(e -> e.kind() == Kind.WARNED).findFirst().orElseThrow();
    assertFalse(warned.voided());
    var onset = ev.stream().filter(e -> e.kind() == Kind.ONSET).findFirst().orElseThrow();
    assertEquals(15.0, (double) onset.onsetTick() / 24_000, 0.05, "one sol later");
  }

  @Test
  void pausedTicksMidWarningMidStormAndMidRampChangeNothing() throws Exception {
    for (double at : new double[] {13.5, 14.02, 14.3, 14.7}) {
      PlanetSim s = planet(Pace.STANDARD);
      until(s, at);
      var before = s.toState();
      double strength = s.stormStrength();
      var clock = new mars.session.SessionClock(true);
      for (int i = 0; i < 100_000; i++) {
        s.onServerTick(clock);
      }
      assertEquals(before, s.toState(), "at sol " + at);
      assertEquals(strength, s.stormStrength());
      assertTrue(s.drainStormEvents().size() >= 0);
    }
  }

  @Test
  void aSaveAndLoadMidStormGivesTheSameStrengthStagesAndFuture() throws Exception {
    PlanetSim a = planet(Pace.STANDARD);
    until(a, 13.9);
    a.setPanelDust(PANEL, 1, false, 0.92);
    until(a, 14.3);
    var saved = a.toState();
    assertEquals(mars.hazards.storm.StormScheduler.Mode.ACTIVE, a.stormMode());
    PlanetSim b =
        PlanetSim.fromState(
                saved,
                TestSims.index(),
                TestSims.defs(),
                TestSims.gates(),
                TestSims.frost(),
                Pace.STANDARD)
            .sim();
    b.configureHazards(TestSims.hazards(), 999);
    assertEquals(saved, b.toState());
    // The strength is re-derived on the next step (it is not saved); both runs agree from there.
    steps(a, 1);
    steps(b, 1);
    assertEquals(a.stormStrength(), b.stormStrength(), 1e-12);
    assertEquals(a.power().supplyPerSol(), b.power().supplyPerSol(), 1e-9);
    until(a, 16);
    until(b, 16);
    assertEquals(a.toState(), b.toState());
    assertEquals(3, b.panelStage(PANEL));
  }

  @Test
  void adminAdvanceMarksTheEventsAsFastForwardAndRealTicksDoNot() throws Exception {
    PlanetSim s = planet(Pace.STANDARD);
    s.model().advanceSols(14.2);
    var notes = s.drainStormEvents();
    assertTrue(notes.stream().anyMatch(n -> n.event().kind() == Kind.ONSET));
    assertTrue(notes.stream().allMatch(PlanetSim.StormNote::fastForward));
    PlanetSim t = planet(Pace.STANDARD);
    for (int i = 0; i < 1000; i++) {
      t.onServerTick();
    }
    t.model().advanceSols(0); // nothing
    // Real ticks: one SCHEDULED event at the first step, not fast-forward.
    var real = t.drainStormEvents();
    assertTrue(real.size() >= 1);
    assertTrue(real.stream().noneMatch(PlanetSim.StormNote::fastForward));
  }

  @Test
  void classroomStormCutsAtSol4_5AndEndsBy5() throws Exception {
    PlanetSim s = planet(Pace.classroom(4));
    until(s, 4.6);
    assertEquals(1.0, s.stormStrength(), 1e-12);
    until(s, 5.1);
    assertEquals(0.0, s.stormStrength(), 1e-12);
    assertEquals(mars.hazards.storm.StormScheduler.Mode.NONE, s.stormMode());
  }

  @Test
  void operatorCanScheduleAndEndAStorm() throws Exception {
    PlanetSim s = TestSims.hazardSim(Pace.STANDARD, 3);
    s.addMachine(TestSims.PANEL, PANEL);
    steps(s, 2); // first storm scheduled for sol 14
    assertEquals(mars.hazards.storm.StormScheduler.Mode.SCHEDULED, s.stormMode());
    assertTrue(s.scheduleStorm(1, 0.75), "an operator may replace a storm that has not started");
    PlanetSim t = TestSims.hazardSim(Pace.STANDARD, 3);
    t.addMachine(TestSims.PANEL, PANEL);
    // Replace the schedule: restore an empty scheduler state, then ask for a storm in 0.2 sol.
    var st = t.toState();
    assertTrue(
        st.hazards().storm() == null
            || st.hazards().storm().mode() == mars.hazards.storm.StormScheduler.Mode.NONE);
    assertTrue(t.scheduleStorm(0.2, 0.75));
    until(t, 0.6);
    assertEquals(1.0, t.stormStrength(), 1e-12);
    assertTrue(t.endStorm());
    until(t, 0.8);
    assertEquals(0.0, t.stormStrength(), 1e-12);
  }
}
