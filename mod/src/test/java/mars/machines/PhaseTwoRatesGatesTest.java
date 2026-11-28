package mars.machines;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mars.TestData;
import mars.TestSims;
import mars.core.Ids;
import mars.core.ParameterId;
import mars.gates.Condition;
import mars.gates.GateDef;
import mars.gates.GateTracker;
import mars.index.sim.Pace;
import mars.index.sim.PlanetSim;
import mars.monitor.view.MonitorView;
import org.junit.jupiter.api.Test;

/** Phase 2 rates (balance sheet 2d) and the Thaw, Phase 3, Breathable and Equilibrium gates. */
class PhaseTwoRatesGatesTest {
  private static final String PROC = "mars:atmospheric_processor";
  private static final String EMIT = "mars:super_greenhouse_emitter";
  private static final ParameterId HYDRO = ParameterId.of("mars:hydrosphere");
  private static final ParameterId CO2 = ParameterId.of("mars:co2_partial");
  private static final ParameterId O2F = ParameterId.of("mars:o2_fraction");
  private static final ParameterId BIOMASS = ParameterId.of("mars:biomass");

  private static double sheetRate(String machine, String unit) throws Exception {
    String sheet =
        Files.readString(
            TestData.pack().resolve("../docs/design/balance-sheet.md").normalize(),
            StandardCharsets.UTF_8);
    Matcher m =
        Pattern.compile("\\| " + machine + " \\| [a-z]+ \\+([0-9.]+) " + unit + "/sol each")
            .matcher(sheet);
    assertTrue(m.find(), "balance sheet 2d has no row for " + machine);
    return Double.parseDouble(m.group(1));
  }

  private static GateDef gate(String id) throws Exception {
    return TestSims.gates().stream().filter(g -> g.id().equals(id)).findFirst().orElseThrow();
  }

  private static PlanetSim sim(Pace pace, int panels) throws Exception {
    return TestSims.withPanels(TestSims.sim(pace), panels);
  }

  @Test
  void ratesInDataMatchTheBalanceSheet() throws Exception {
    var p = TestSims.defs().get(PROC).contributions().get(0);
    var e = TestSims.defs().get(EMIT).contributions().get(0);
    assertEquals(Ids.PRESSURE, p.parameter());
    assertEquals(Ids.TEMPERATURE, e.parameter());
    assertEquals(sheetRate("Atmospheric processor", "kPa"), p.ratePerSol(), 1e-12);
    assertEquals(sheetRate("Super-greenhouse emitter", "C"), e.ratePerSol(), 1e-12);
  }

  @Test
  void unpoweredMachinesContributeNothing() throws Exception {
    PlanetSim s = sim(Pace.STANDARD, 0);
    s.addMachine(PROC, 1L);
    s.addMachine(EMIT, 2L);
    s.model().advanceSols(2);
    assertEquals(-60.0, s.model().current(Ids.TEMPERATURE), 1e-9);
    assertEquals(0.6, s.model().current(Ids.PRESSURE), 1e-9);
  }

  @Test
  void emittersAddPerMachineAndOnlyWithPower() throws Exception {
    PlanetSim s = sim(Pace.STANDARD, 3);
    for (int i = 0; i < 3; i++) {
      s.addMachine(EMIT, 10L + i);
    }
    s.model().advanceSols(4);
    assertEquals(-60.0 + 3 * 0.25 * 4, s.model().current(Ids.TEMPERATURE), 0.01);
    PlanetSim one = sim(Pace.STANDARD, 3);
    one.addMachine(EMIT, 10L);
    one.model().advanceSols(4);
    assertEquals(-60.0 + 0.25 * 4, one.model().current(Ids.TEMPERATURE), 0.01);
  }

  @Test
  void processorsAddPerMachine() throws Exception {
    PlanetSim s = sim(Pace.STANDARD, 3);
    s.addMachine(PROC, 1L);
    s.addMachine(PROC, 2L);
    s.model().advanceTicks(100);
    assertEquals(0.30, s.model().netRatePerSol(Ids.PRESSURE), 1e-9);
  }

  @Test
  void ashortGridScalesTheEmitterByItsShare() throws Exception {
    PlanetSim s = sim(Pace.STANDARD, 1);
    for (int i = 0; i < 3; i++) {
      s.addMachine(EMIT, 10L + i);
    }
    s.model().advanceTicks(100);
    double share = Math.min(1.0, s.power().supplyPerSol() / s.power().demandPerSol());
    assertTrue(share < 1.0);
    assertEquals(0.75 * share, s.model().netRatePerSol(Ids.TEMPERATURE), 1e-9);
  }

  @Test
  void gateFilesMatchTheSpecAndTheSheet() throws Exception {
    assertEquals(-20.0, ((Condition.Compare) gate("mars:thaw").condition()).value());
    assertNull(gate("mars:thaw").hold());
    assertNull(gate("mars:phase_3_begins").hold());
    assertEquals(7.0, gate("mars:breathable").hold().standardSols());
    assertEquals(14.0, gate("mars:equilibrium").hold().standardSols());
    assertEquals(1.0, gate("mars:breathable").hold().resetAfterStandardSols());
    assertEquals(1.0, gate("mars:equilibrium").hold().resetAfterStandardSols());
    String sheet = Files.readString(TestData.pack().resolve("../docs/design/balance-sheet.md"));
    Matcher m =
        Pattern.compile("\\| Breathable \\| (\\d+) sols? [^|]*\\| (\\d+) sols?").matcher(sheet);
    assertTrue(m.find());
    assertEquals(Double.parseDouble(m.group(1)), gate("mars:breathable").hold().standardSols());
    assertEquals(Double.parseDouble(m.group(2)), gate("mars:breathable").hold().classroomSols());
    m = Pattern.compile("\\| Equilibrium \\| (\\d+) sols? [^|]*\\| (\\d+) sols?").matcher(sheet);
    assertTrue(m.find());
    assertEquals(Double.parseDouble(m.group(1)), gate("mars:equilibrium").hold().standardSols());
    assertEquals(Double.parseDouble(m.group(2)), gate("mars:equilibrium").hold().classroomSols());
  }

  @Test
  void newGatesUnlockOnlyBlocksThatExist() throws Exception {
    for (GateDef g : TestSims.gates()) {
      for (String u : g.unlocks()) {
        String name = u.substring(u.indexOf(':') + 1);
        assertTrue(
            Files.exists(TestData.data("loot_table/blocks/" + name + ".json")),
            g.id() + " unlocks " + u + " which is not a block yet");
      }
    }
  }

  @Test
  void thawLatchesAtMinusTwentyInEveryPaceAndStays() throws Exception {
    for (Pace pace : new Pace[] {Pace.STANDARD, Pace.classroom(3), Pace.classroom(5)}) {
      PlanetSim s = sim(pace, 0);
      s.model().set(Ids.TEMPERATURE, -20.01);
      s.model().advanceTicks(200);
      assertFalse(s.gates().isLatched("mars:thaw"), pace + " below the threshold");
      s.model().set(Ids.TEMPERATURE, -20.0);
      s.model().advanceTicks(200);
      assertTrue(s.gates().isLatched("mars:thaw"), pace + " at the threshold, no hold");
      s.model().set(Ids.TEMPERATURE, -60.0);
      s.model().advanceTicks(200);
      assertTrue(s.gates().isLatched("mars:thaw"), "gates lock in");
    }
  }

  @Test
  void phaseThreeNeedsBothPressureAndWarmth() throws Exception {
    for (Pace pace : new Pace[] {Pace.STANDARD, Pace.classroom(4)}) {
      PlanetSim s = sim(pace, 0);
      s.model().set(Ids.PRESSURE, 30.0);
      s.model().set(Ids.TEMPERATURE, -0.01);
      s.model().advanceTicks(200);
      assertFalse(s.gates().isLatched("mars:phase_3_begins"));
      s.model().set(Ids.PRESSURE, 29.9);
      s.model().set(Ids.TEMPERATURE, 1.0);
      s.model().advanceTicks(200);
      assertFalse(s.gates().isLatched("mars:phase_3_begins"));
      s.model().set(Ids.PRESSURE, 30.0);
      s.model().set(Ids.TEMPERATURE, 0.0);
      s.model().advanceTicks(200);
      assertTrue(s.gates().isLatched("mars:phase_3_begins"));
    }
  }

  private static void breathableAir(PlanetSim s) {
    s.model().set(Ids.PRESSURE, 75.0);
    s.model().set(Ids.TEMPERATURE, 5.0);
    s.model().set(O2F, 21.5);
    s.model().set(CO2, 0.4);
    s.model().set(HYDRO, 30.0);
    s.model().set(BIOMASS, 1500.0);
  }

  @Test
  void breathableHoldsSevenSolsAndEquilibriumFourteen() throws Exception {
    PlanetSim s = sim(Pace.STANDARD, 0);
    breathableAir(s);
    s.model().advanceSols(6.8);
    assertFalse(s.gates().isLatched("mars:breathable"));
    s.model().advanceSols(0.4);
    assertTrue(s.gates().isLatched("mars:breathable"));
    assertFalse(s.gates().isLatched("mars:equilibrium"));
    s.model().advanceSols(6.4);
    assertFalse(s.gates().isLatched("mars:equilibrium"));
    s.model().advanceSols(0.4);
    assertTrue(s.gates().isLatched("mars:equilibrium"));
  }

  @Test
  void aBreakShorterThanOneSolPausesAndALongerOneResets() throws Exception {
    PlanetSim s = sim(Pace.STANDARD, 0);
    breathableAir(s);
    s.model().advanceSols(3);
    double held = s.gates().state("mars:breathable").heldSols();
    s.model().set(CO2, 0.6);
    s.model().advanceSols(0.5);
    s.model().set(CO2, 0.4);
    assertEquals(held, s.gates().state("mars:breathable").heldSols(), 1e-9);
    s.model().set(CO2, 0.6);
    s.model().advanceSols(1.2);
    assertEquals(0.0, s.gates().state("mars:breathable").heldSols(), 1e-9);
  }

  @Test
  void thawAndPhaseThreeRowsShowOnTheMonitor() throws Exception {
    PlanetSim s = sim(Pace.STANDARD, 1);
    s.gates().latch("mars:dome_stable", GateTracker.Cause.ADMIN, 0);
    s.gates().latch("mars:phase_2_begins", GateTracker.Cause.ADMIN, 0);
    s.model().set(Ids.TEMPERATURE, -40.0);
    MonitorView.Row g = goal(s);
    assertEquals("gate.mars.thaw", g.valueKey());
    assertEquals("monitor.gate.thaw.progress", g.detailKey());
    assertEquals(0.5, g.goal().fraction(), 1e-9);
    s.gates().latch("mars:thaw", GateTracker.Cause.ADMIN, 0);
    s.model().set(Ids.PRESSURE, 15.0);
    g = goal(s);
    assertEquals("gate.mars.phase_3_begins", g.valueKey());
    assertTrue(g.side().endsWith("/ 30 kPa"), g.side());
    s.model().set(Ids.PRESSURE, 31.0);
    s.model().set(Ids.TEMPERATURE, -10.0);
    g = goal(s);
    assertTrue(g.side().endsWith("/ 0 °C"), "pressure met, so warmth is shown: " + g.side());
    s.gates().latch("mars:phase_3_begins", GateTracker.Cause.ADMIN, 0);
    assertEquals("gate.mars.breathable", goal(s).valueKey());
  }

  private static MonitorView.Row goal(PlanetSim s) {
    return MonitorView.of(s).rows().stream()
        .filter(r -> r.labelKey().equals("monitor.row.next_goal"))
        .findFirst()
        .orElseThrow();
  }
}
