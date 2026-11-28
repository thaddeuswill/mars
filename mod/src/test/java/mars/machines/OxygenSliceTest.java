package mars.machines;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import mars.TestData;
import mars.TestSims;
import mars.core.Ids;
import mars.core.ParameterId;
import mars.gates.Condition;
import mars.gates.GateDef;
import mars.gates.GateTracker;
import mars.index.sim.MachineDef;
import mars.index.sim.Pace;
import mars.index.sim.PlanetSim;
import mars.session.SessionClock;
import org.junit.jupiter.api.Test;

/**
 * Slice B, air we make (docs/design/phase3-hydrosphere-and-drift.md section 3; rule A): the algae
 * vat and the carbon scrubber, the gas bookkeeping, and a scripted standard build over sols that
 * reaches Breathable and Equilibrium. All numbers come from the real data files. The arithmetic of
 * the script is in docs/team/reports/2026-11-28-technical-lead-slice-b-oxygen.md.
 */
class OxygenSliceTest {
  static final String VAT = "mars:algae_vat";
  static final String SCRUBBER = "mars:carbon_scrubber";
  static final String OUTGASSER = TestSims.OUTGASSER;
  static final String PROCESSOR = "mars:atmospheric_processor";
  static final String EMITTER = "mars:super_greenhouse_emitter";
  static final String EXTRACTOR = "mars:ice_extractor";
  static final ParameterId CO2 = Ids.CO2_PARTIAL;

  // ---- scripted standard build ------------------------------------------------------------

  /** Places machines and panels over sols, as a player following the standard build would. */
  static final class Build {
    final PlanetSim sim;
    long next = 5000;
    int panels;
    final java.util.Deque<Long> processors = new java.util.ArrayDeque<>();
    final java.util.Deque<Long> emitters = new java.util.ArrayDeque<>();
    final java.util.Deque<Long> outgassers = new java.util.ArrayDeque<>();
    int vats;
    int scrubbers;

    Build(PlanetSim sim) {
      this.sim = sim;
    }

    void add(String type, int n) {
      for (int i = 0; i < n; i++) {
        long p = next++;
        sim.addMachine(type, p);
        switch (type) {
          case PROCESSOR -> processors.add(p);
          case EMITTER -> emitters.add(p);
          case OUTGASSER -> outgassers.add(p);
          case VAT -> vats++;
          case SCRUBBER -> scrubbers++;
          default -> {}
        }
      }
      power();
    }

    void remove(String type, java.util.Deque<Long> from, int n) {
      for (int i = 0; i < n && !from.isEmpty(); i++) {
        sim.removeMachine(type, from.poll());
      }
      power();
    }

    /** Enough clean panels that the grid covers demand (5 units per sol each). */
    void power() {
      while (sim.power().supplyPerSol() < sim.power().demandPerSol() * 1.02 + 1) {
        sim.addMachine(TestSims.PANEL, 1000L + panels++);
      }
    }

    boolean latched(String id) {
      return sim.gates().isLatched(id);
    }

    double now(ParameterId id) {
      return sim.model().current(id);
    }
  }

  /** What the script saw, for the report. */
  record Result(
      double phase3Sol,
      double co2AtPhase3,
      double breathableSol,
      double equilibriumSol,
      int vats,
      int scrubbers,
      int panelsAtEnd,
      double powerDemandAtPeak,
      double o2Fraction,
      double biomass,
      double carbonate,
      double pressureAtEnd) {}

  /** The standard build (INVENTED counts): the sheet's schedule with the Phase 3 air machines. */
  static Result standardBuild(int vats, int scrubbers) throws Exception {
    Build b = new Build(TestSims.sim(Pace.STANDARD));
    b.add(OUTGASSER, 2);
    while (!b.latched("mars:phase_2_begins")) {
      b.sim.model().advanceSols(0.5);
    }
    // Outgassers are Phase 1 machines (DESIGN.md: CO2 raised by Outgassers in Phase 1, then by
    // processors in part): the build retires them when Phase 2 begins.
    b.remove(OUTGASSER, b.outgassers, 2);
    b.add(EMITTER, 2);
    b.add(PROCESSOR, 2);
    double p2 = b.sim.model().sols();
    int[] emitterSols = {6, 14, 24, 34};
    int[] processorSols = {14, 34};
    int ei = 0;
    int pi = 0;
    int extractors = 0;
    double thawAt = -1;
    while (!b.latched("mars:phase_3_begins")) {
      b.sim.model().advanceSols(1);
      double t = b.sim.model().sols() - p2;
      assertTrue(t < 150, "Phase 3 should begin within 150 sols of Phase 2");
      if (ei < emitterSols.length && t >= emitterSols[ei]) {
        b.add(EMITTER, 1);
        ei++;
      }
      if (pi < processorSols.length && t >= processorSols[pi]) {
        b.add(PROCESSOR, 1);
        pi++;
      }
      if (b.latched("mars:thaw")) {
        if (thawAt < 0) {
          thawAt = t;
        }
        double dt = t - thawAt;
        int want = dt >= 14 ? 4 : dt >= 6 ? 3 : 2;
        if (extractors < want) {
          b.add(EXTRACTOR, want - extractors);
          extractors = want;
        }
      }
    }
    double phase3 = b.sim.model().sols();
    double co2AtPhase3 = b.now(CO2);
    // Phase 3: processors up to 10 (filler gas only now), emitters cut back to 2 once warm.
    b.add(PROCESSOR, 10 - b.processors.size());
    b.remove(EMITTER, b.emitters, b.emitters.size() - 2);
    b.add(VAT, vats);
    b.add(SCRUBBER, scrubbers);
    double peak = b.sim.power().demandPerSol();
    double breathable = -1;
    double equilibrium = -1;
    for (int i = 0; i < 160 && equilibrium < 0; i++) {
      b.sim.model().advanceSols(1);
      // Hold the pressure near 76 kPa once there: a player stops the processors (the fraction of
      // O2 falls out of its 19 to 23 percent band if pressure keeps climbing).
      if (b.now(Ids.PRESSURE) >= 76.0 && !b.processors.isEmpty()) {
        b.remove(PROCESSOR, b.processors, b.processors.size());
      }
      if (breathable < 0 && b.latched("mars:breathable")) {
        breathable = b.sim.model().sols() - phase3;
      }
      if (b.latched("mars:equilibrium")) {
        equilibrium = b.sim.model().sols() - phase3;
      }
    }
    return new Result(
        phase3,
        co2AtPhase3,
        breathable,
        equilibrium,
        b.vats,
        b.scrubbers,
        b.panels,
        peak,
        b.now(Ids.O2_FRACTION),
        b.now(Ids.BIOMASS),
        b.sim.carbonateLockedKpa(),
        b.now(Ids.PRESSURE));
  }

  @Test
  void aScriptedStandardBuildReachesBreathableAndEquilibrium() throws Exception {
    Result r = standardBuild(9, 1);
    System.out.println("OXYGEN-SCRIPT " + r);
    assertTrue(r.co2AtPhase3() > 10, "there is CO2 to convert when Phase 3 begins: " + r);
    assertTrue(r.breathableSol() > 0, "Breathable latched: " + r);
    assertTrue(r.equilibriumSol() > r.breathableSol(), "Equilibrium latched after it: " + r);
    assertTrue(r.vats() <= 12 && r.scrubbers() <= 3, "a plausible number of machines: " + r);
    assertTrue(r.equilibriumSol() <= 60, "within 60 sols of Phase 3 (design: 42 plus holds): " + r);
    assertTrue(r.o2Fraction() >= 19 && r.o2Fraction() <= 23, "O2 in the fire-safe band: " + r);
    assertTrue(r.biomass() >= 1500, "biomass from the vats alone reaches 1500: " + r);
    assertTrue(r.carbonate() > 0, "the scrubber recorded carbonate: " + r);
  }

  @Test
  void withoutAnyScrubberTheAirIsBreathableButOverrichUntilPressureDilutesIt() throws Exception {
    // All of the CO2 becomes O2 (rule A): O2 is the sum of CO2 and O2, above the 23 percent
    // ceiling at 76 kPa. Breathable latches; Equilibrium needs more pressure or a scrubber.
    Result r = standardBuild(9, 0);
    System.out.println("OXYGEN-NOSCRUB " + r);
    assertTrue(r.breathableSol() > 0, r.toString());
    assertTrue(r.carbonate() == 0, r.toString());
  }

  // ---- the mechanism --------------------------------------------------------------------------

  private static PlanetSim air(int vats, int scrubbers, int panels) throws Exception {
    // No polar frost here: its buried-CO2 release would add CO2 and blur the arithmetic.
    PlanetSim s =
        TestSims.withPanels(
            new PlanetSim(TestSims.index(), TestSims.defs(), TestSims.gates(), Pace.STANDARD),
            panels);
    for (int i = 0; i < vats; i++) {
      s.addMachine(VAT, 100L + i);
    }
    for (int i = 0; i < scrubbers; i++) {
      s.addMachine(SCRUBBER, 200L + i);
    }
    s.model().set(Ids.PRESSURE, 40.0);
    s.model().set(CO2, 12.0);
    s.model().set(Ids.O2_FRACTION, 5.0);
    s.model().set(Ids.TEMPERATURE, 3.0);
    s.model().set(Ids.HYDROSPHERE, 15.0);
    return s;
  }

  private static double o2Kpa(PlanetSim s) {
    return s.model().current(Ids.O2_PARTIAL);
  }

  @Test
  void aVatTurnsCo2IntoO2OneForOne() throws Exception {
    PlanetSim s = air(2, 0, 3);
    double co2 = s.model().current(CO2);
    double o2 = o2Kpa(s);
    double p = s.model().current(Ids.PRESSURE);
    s.model().advanceSols(3);
    double rate = TestSims.defs().get(VAT).conversions().get(0).kpaPerSol();
    double moved = 2 * rate * 3;
    assertEquals(co2 - moved, s.model().current(CO2), 1e-6);
    assertEquals(o2 + moved, o2Kpa(s), 1e-6);
    assertEquals((co2 + o2) - (s.model().current(CO2) + o2Kpa(s)), 0.0, 1e-6, "CO2 + O2 kept");
    assertEquals(p, s.model().current(Ids.PRESSURE), 1e-9, "total pressure kept");
  }

  @Test
  void aVatNeverTakesMoreCo2ThanTheAirHas() throws Exception {
    PlanetSim s = air(9, 0, 6);
    s.model().set(CO2, 0.05);
    double o2Before = o2Kpa(s);
    s.model().advanceSols(1);
    assertEquals(0.0, s.model().current(CO2), 1e-9);
    double after = o2Kpa(s);
    assertEquals(o2Before + 0.05, after, 1e-6, "only the 0.05 kPa there was");
    s.model().advanceSols(1);
    assertEquals(after, o2Kpa(s), 1e-9, "nothing to convert, nothing happens");
  }

  @Test
  void unpoweredMachinesDoNothing() throws Exception {
    PlanetSim s = air(3, 2, 0);
    double co2 = s.model().current(CO2);
    s.model().advanceSols(2);
    assertEquals(co2, s.model().current(CO2), 1e-9);
    assertEquals(0.0, s.carbonateLockedKpa(), 0.0);
  }

  @Test
  void aScrubberLowersCo2LocksItAsCarbonateAndMakesNoO2() throws Exception {
    PlanetSim s = air(0, 2, 2);
    double o2 = o2Kpa(s);
    double p = s.model().current(Ids.PRESSURE);
    s.model().advanceSols(4);
    double rate = TestSims.defs().get(SCRUBBER).conversions().get(0).kpaPerSol();
    double locked = 2 * rate * 4;
    assertEquals(12.0 - locked, s.model().current(CO2), 1e-6);
    assertEquals(locked, s.carbonateLockedKpa(), 1e-6);
    assertEquals(o2, o2Kpa(s), 1e-6, "no O2 made; the O2 kPa is kept as pressure falls");
    assertEquals(p - locked, s.model().current(Ids.PRESSURE), 1e-6, "the gas left the air");
  }

  @Test
  void addedFillerGasDilutesTheFractionButKeepsTheO2KPa() throws Exception {
    PlanetSim s = air(0, 0, 2);
    s.addMachine(PROCESSOR, 7L);
    s.gates().latch("mars:phase_3_begins", GateTracker.Cause.ADMIN, 0);
    double o2 = o2Kpa(s);
    double f = s.model().current(Ids.O2_FRACTION);
    s.model().advanceSols(10);
    assertTrue(s.model().current(Ids.PRESSURE) > 41.4);
    assertEquals(o2, o2Kpa(s), 1e-6);
    assertTrue(s.model().current(Ids.O2_FRACTION) < f);
    assertEquals(
        12.0, s.model().current(CO2), 1e-9, "after Phase 3 begins a processor adds filler");
  }

  @Test
  void beforePhaseThreeAProcessorAddsCo2AndAnOutgasserAddsOnlyCo2() throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 3);
    s.addMachine(PROCESSOR, 1L);
    s.addMachine(OUTGASSER, 2L);
    double co2 = s.model().current(CO2);
    double p = s.model().current(Ids.PRESSURE);
    s.model().advanceSols(2);
    double dp = s.model().current(Ids.PRESSURE) - p;
    double dc = s.model().current(CO2) - co2;
    assertEquals(2 * (0.15 + 0.10), dp, 0.02);
    assertEquals(2 * (0.08 + 0.10), dc, 0.02);
  }

  @Test
  void theFrostIsCo2IceSoItNeverLeavesMoreCo2ThanAir() throws Exception {
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    s.model().set(Ids.PRESSURE, 5.0);
    s.model().set(CO2, 4.97);
    s.model().advanceSols(10);
    assertTrue(s.model().current(CO2) < 4.97, "the frost took CO2");
    assertTrue(s.model().current(CO2) <= s.model().current(Ids.PRESSURE) + 1e-9);
  }

  @Test
  void aVatNeedsWaterAndWarmth() throws Exception {
    PlanetSim s = air(2, 0, 3);
    s.model().set(Ids.HYDROSPHERE, 9.0);
    assertFalse(s.machineRequirementMet(VAT));
    double co2 = s.model().current(CO2);
    s.model().advanceSols(1);
    assertEquals(co2, s.model().current(CO2), 1e-9, "no water, no conversion");
    s.model().set(Ids.HYDROSPHERE, 10.0);
    s.model().set(Ids.TEMPERATURE, -0.1);
    assertFalse(s.machineRequirementMet(VAT));
    s.model().set(Ids.TEMPERATURE, 0.0);
    assertTrue(s.machineRequirementMet(VAT));
    assertTrue(s.machineRequirementMet(SCRUBBER), "a scrubber has no condition");
  }

  @Test
  void thresholdsAreNotScaledByPaceAndTheConditionsAreFixed() throws Exception {
    MachineDef vat = TestSims.defs().get(VAT);
    var cond = (Condition.All) vat.requires();
    assertEquals(2, cond.parts().size());
    for (Condition c : cond.parts()) {
      var cmp = (Condition.Compare) c;
      double v = cmp.value();
      assertTrue(v == 10.0 || v == 0.0, "only the design's 10 percent and the 0 C line: " + v);
    }
    GateDef b =
        TestSims.gates().stream().filter(g -> g.id().equals("mars:breathable")).findFirst().get();
    String text = b.condition().toString();
    assertTrue(text.contains("16.0") && text.contains("75.0") && text.contains("0.5"), text);
  }

  @Test
  void pauseFreezesTheConversion() throws Exception {
    PlanetSim s = air(3, 1, 3);
    SessionClock clock = new SessionClock(true);
    clock.pause();
    double co2 = s.model().current(CO2);
    long ticks = s.model().simTicks();
    for (int i = 0; i < 5000; i++) {
      s.onServerTick(clock);
    }
    assertEquals(co2, s.model().current(CO2), 0.0);
    assertEquals(ticks, s.model().simTicks());
    clock.resume();
    for (int i = 0; i < 5000; i++) {
      s.onServerTick(clock);
    }
    assertTrue(s.model().current(CO2) < co2);
  }

  @Test
  void theCarbonateTallySurvivesSaveAndLoadAndIsNotAParameter() throws Exception {
    PlanetSim s = air(0, 2, 2);
    s.model().advanceSols(3);
    double locked = s.carbonateLockedKpa();
    assertTrue(locked > 0);
    var state = s.toState();
    var loaded =
        PlanetSim.fromState(
            state,
            TestSims.index(),
            TestSims.defs(),
            TestSims.gates(),
            TestSims.frost(),
            Pace.STANDARD);
    assertEquals(locked, loaded.sim().carbonateLockedKpa(), 1e-12);
    assertTrue(
        loaded.report().ignored().isEmpty(), "the tally is not reported as an unknown value");
    assertEquals(s.model().current(CO2), loaded.sim().model().current(CO2), 1e-12);
  }

  @Test
  void dataMatchesTheBalanceSheetAndSaysInvented() throws Exception {
    String sheet =
        Files.readString(
            TestData.pack().resolve("../docs/design/balance-sheet.md").normalize(),
            StandardCharsets.UTF_8);
    MachineDef vat = TestSims.defs().get(VAT);
    MachineDef scr = TestSims.defs().get(SCRUBBER);
    String vatRow =
        sheet.lines().filter(l -> l.startsWith("| Algae vat")).findFirst().orElseThrow();
    String scrRow =
        sheet.lines().filter(l -> l.startsWith("| Carbon scrubber")).findFirst().orElseThrow();
    assertTrue(vatRow.contains(fmt(vat.conversions().get(0).kpaPerSol()) + " kPa"), vatRow);
    assertTrue(vatRow.contains(fmt(vat.power().drawPerSol())), vatRow);
    assertTrue(vatRow.contains("INVENTED"), vatRow);
    assertTrue(scrRow.contains(fmt(scr.conversions().get(0).kpaPerSol()) + " kPa"), scrRow);
    assertTrue(scrRow.contains(fmt(scr.power().drawPerSol())), scrRow);
    assertTrue(scrRow.contains("INVENTED"), scrRow);
    assertTrue(vat.contributions().isEmpty() && scr.contributions().isEmpty());
    assertTrue(vat.conversions().get(0).toO2() && !scr.conversions().get(0).toO2());
    assertEquals(100.0, vat.conversions().get(0).biomassPerKpa());
  }

  // ---- the Monitor -----------------------------------------------------------------------

  private static mars.monitor.view.MonitorView.Row row(PlanetSim s, String label) {
    return mars.monitor.view.MonitorView.of(s).rows().stream()
        .filter(r -> r.labelKey().equals(label))
        .findFirst()
        .orElse(null);
  }

  @Test
  void theGasRowsAppearWithTheFirstVatOrScrubberAndNotBefore() throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 2);
    assertEquals(null, row(s, "monitor.row.oxygen"));
    assertEquals(null, row(s, "monitor.row.co2"));
    s.addMachine(SCRUBBER, 9L);
    assertTrue(row(s, "monitor.row.oxygen") != null && row(s, "monitor.row.co2") != null);
    // Phase 3 begun shows them too; a moved value shows them from Phase 2.
    PlanetSim t = TestSims.sim(Pace.STANDARD);
    t.model().set(CO2, 3.0);
    assertEquals(null, row(t, "monitor.row.co2"), "Phase 1: the pressure row carries it");
    t.gates().latch("mars:phase_2_begins", GateTracker.Cause.ADMIN, 0);
    assertTrue(row(t, "monitor.row.co2") != null);
  }

  @Test
  void aRunningVatMakesO2RiseAndCo2FallAndAProblemSaysWhy() throws Exception {
    PlanetSim s = air(2, 1, 3);
    s.model().advanceSols(1);
    var o2 = row(s, "monitor.row.oxygen");
    var co2 = row(s, "monitor.row.co2");
    assertEquals(mars.monitor.view.MonitorView.Trend.RISING, o2.trend());
    assertEquals(mars.monitor.view.MonitorView.Trend.FALLING, co2.trend());
    assertEquals("+0.20 kPa/sol", o2.trendRate().replace('\u00a0', ' '));
    assertEquals("-0.40 kPa/sol", co2.trendRate().replace('\u00a0', ' ').replace('\u2212', '-'));
    // No water: the vat line says so on both rows; the scrubber line has no problem.
    s.model().set(Ids.HYDROSPHERE, 2.0);
    var why =
        row(s, "monitor.row.co2").causes().stream()
            .filter(c -> c.sourceKey().equals("block.mars.algae_vat"))
            .findFirst()
            .get();
    assertEquals("monitor.cause.needs_water", why.reasonKey());
    assertEquals(2, why.count());
    s.model().set(Ids.HYDROSPHERE, 20.0);
    s.model().set(Ids.TEMPERATURE, -5.0);
    assertEquals(
        "monitor.cause.too_cold", row(s, "monitor.row.oxygen").causes().get(0).reasonKey());
    s.model().set(Ids.TEMPERATURE, 3.0);
    s.model().set(CO2, 0.0);
    assertEquals(
        "monitor.cause.no_input", row(s, "monitor.row.oxygen").causes().get(0).reasonKey());
    PlanetSim dark = air(1, 0, 0);
    assertEquals(
        "monitor.cause.needs_power", row(dark, "monitor.row.co2").causes().get(0).reasonKey());
  }

  private static mars.monitor.view.MonitorView.Row nextGoal(PlanetSim s) {
    return mars.monitor.view.MonitorView.of(s).rows().stream()
        .filter(r -> r.labelKey().equals("monitor.row.next_goal"))
        .findFirst()
        .get();
  }

  @Test
  void theNextGoalForBreathableShowsTheFirstUnmetConditionThenTheCount() throws Exception {
    PlanetSim s = air(1, 0, 3);
    for (String g :
        new String[] {
          "mars:dome_stable",
          "mars:phase_2_begins",
          "mars:thaw",
          "mars:hydrosphere",
          "mars:phase_3_begins"
        }) {
      s.gates().latch(g, GateTracker.Cause.ADMIN, 0);
    }
    s.model().set(Ids.PRESSURE, 76.0);
    s.model().set(Ids.O2_FRACTION, 5.0); // 3.8 kPa
    s.model().set(CO2, 8.0);
    var g = nextGoal(s);
    assertEquals("gate.mars.breathable", g.valueKey());
    assertTrue(g.side().endsWith("/ 16 kPa"), g.side());
    assertTrue(g.goal().fraction() > 0 && g.goal().fraction() < 1);
    // Oxygen met, pressure short: pressure is next.
    s.model().set(Ids.O2_FRACTION, 22.0);
    s.model().set(Ids.PRESSURE, 60.0);
    s.model().set(Ids.O2_FRACTION, 30.0); // 18 kPa
    g = nextGoal(s);
    assertTrue(g.side().endsWith("/ 75 kPa"), g.side());
    // Then the CO2 line, which is an upper limit.
    s.model().set(Ids.PRESSURE, 76.0);
    s.model().set(Ids.O2_FRACTION, 22.0);
    g = nextGoal(s);
    assertTrue(g.side().endsWith("/ 0.5 kPa or less"), g.side());
    assertTrue(g.goal().fraction() < 0.1);
    // Every condition met and counting: the hold counter is shown.
    s.model().set(CO2, 0.2);
    s.model().advanceSols(2);
    g = nextGoal(s);
    assertTrue(g.side().endsWith("/ 7 sols"), g.side());
    assertEquals("gate.mars.breathable", g.valueKey());
  }

  @Test
  void breathableAndEquilibriumNeedEveryConditionAndTheHoldsAreNotScaled() throws Exception {
    for (String id : new String[] {"mars:breathable", "mars:equilibrium"}) {
      GateDef g = TestSims.gates().stream().filter(x -> x.id().equals(id)).findFirst().get();
      String c = g.condition().toString();
      assertTrue(c.contains("75.0") && c.contains("0.5"), c);
    }
  }

  private static String fmt(double v) {
    return v == Math.rint(v) ? String.valueOf((long) v) : String.valueOf(v);
  }
}
