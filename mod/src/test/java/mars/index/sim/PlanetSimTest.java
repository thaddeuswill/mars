package mars.index.sim;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mars.TestData;
import mars.TestSims;
import mars.core.Ids;
import org.junit.jupiter.api.Test;

class PlanetSimTest {
  static final String OUTGASSER = TestSims.OUTGASSER;

  static IndexData.Loaded index() throws Exception {
    return TestSims.index();
  }

  static Map<String, MachineDef> defs() throws Exception {
    return TestSims.defs();
  }

  static PlanetSim sim(Pace pace) throws Exception {
    return TestSims.withPanels(TestSims.sim(pace), 3);
  }

  static double pressure(PlanetSim s) {
    return s.model().snapshot().get(Ids.PRESSURE);
  }

  @Test
  void outgasserRateInDataMatchesBalanceSheet() throws Exception {
    double fromData = defs().get(OUTGASSER).contributions().get(0).ratePerSol();
    String sheet =
        Files.readString(
            TestData.pack().resolve("../docs/design/balance-sheet.md").normalize(),
            StandardCharsets.UTF_8);
    Matcher m =
        Pattern.compile("\\*\\*Regolith Outgasser\\*\\*[^\\n]*\\| \\+([0-9.]+) kPa each")
            .matcher(sheet);
    assertTrue(m.find(), "balance sheet section 2 has no Outgasser row");
    double fromSheet = Double.parseDouble(m.group(1));
    System.out.println("Outgasser rate: data " + fromData + ", balance sheet " + fromSheet);
    assertEquals(fromSheet, fromData, 1e-12);
    assertEquals(Ids.PRESSURE, defs().get(OUTGASSER).contributions().get(0).parameter());
  }

  @Test
  void tickingRunsOneStepPerHundredTicksAndNoMoreCost() throws Exception {
    PlanetSim s = sim(Pace.STANDARD);
    s.addMachine(OUTGASSER, 1L);
    int steps = 0;
    for (int i = 0; i < 250; i++) {
      if (s.onServerTick()) {
        steps++;
      }
    }
    double expected = 0.6 + 0.10 * steps * 100 / 24_000.0;
    System.out.printf(
        "250 ticks: %d steps, expected %.6f kPa, read %.6f kPa%n", steps, expected, pressure(s));
    assertEquals(2, steps);
    assertEquals(expected, pressure(s), 1e-12);
    assertEquals(200, s.model().simTicks());
  }

  @Test
  void oneOutgasserForTenSolsAddsOneKpa() throws Exception {
    PlanetSim s = sim(Pace.STANDARD);
    s.addMachine(OUTGASSER, 1L);
    s.model().advanceSols(10);
    System.out.printf("1 Outgasser, 10 sols: expected 1.600, read %.4f%n", pressure(s));
    assertEquals(1.6, pressure(s), 1e-6);
  }

  @Test
  void twoOutgassersDoubleAndRemovingOneHalvesTheRate() throws Exception {
    PlanetSim s = sim(Pace.STANDARD);
    assertTrue(s.addMachine(OUTGASSER, 1L));
    assertTrue(s.addMachine(OUTGASSER, 2L));
    assertFalse(s.addMachine(OUTGASSER, 2L), "the same position must not count twice");
    assertEquals(2, s.machineCount(OUTGASSER));
    System.out.println("two Outgassers net rate/sol: " + s.model().netRatePerSol(Ids.PRESSURE));
    assertEquals(0.20, s.model().netRatePerSol(Ids.PRESSURE), 1e-12);
    assertTrue(s.removeMachine(OUTGASSER, 1L));
    assertFalse(s.removeMachine(OUTGASSER, 1L));
    assertEquals(0.10, s.model().netRatePerSol(Ids.PRESSURE), 1e-12);
    s.model().advanceSols(10);
    assertEquals(1.6, pressure(s), 1e-6);
  }

  @Test
  void classroomAndGroupScaleRatesButNotAnythingElse() throws Exception {
    PlanetSim s = sim(new Pace(4.0, 4, 24_000, 100));
    s.addMachine(OUTGASSER, 1L);
    double perSol = 0.10 * 4.0 / (1 + 0.35 * 3);
    System.out.printf(
        "classroom x4, group 4: expected %.5f/sol, read cause %.5f/sol%n",
        perSol, s.model().causes(Ids.PRESSURE).get(0).ratePerSol());
    assertEquals(perSol, s.model().causes(Ids.PRESSURE).get(0).ratePerSol(), 1e-12);
  }

  @Test
  void unknownMachineTypeIsRefusedLoudly() throws Exception {
    PlanetSim s = sim(Pace.STANDARD);
    IllegalArgumentException e =
        assertThrows(IllegalArgumentException.class, () -> s.addMachine("mars:nope", 1L));
    assertTrue(e.getMessage().contains("mars:nope"));
  }

  @Test
  void saveAndRestoreKeepsValuesTicksAndMachines() throws Exception {
    PlanetSim s = sim(Pace.STANDARD);
    s.addMachine(OUTGASSER, 11L);
    s.addMachine(OUTGASSER, 22L);
    for (int i = 0; i < 1234; i++) {
      s.onServerTick();
    }
    s.model().set(Ids.TEMPERATURE, -55.5);
    PlanetState state = s.toState();
    PlanetSim.Loaded back = PlanetSim.fromState(state, index(), defs(), Pace.STANDARD);
    System.out.println("saved " + state + "\nrestored " + back.sim().toState());
    assertEquals(state, back.sim().toState());
    assertEquals(2, back.sim().machineCount(OUTGASSER));
    assertEquals(0.20, back.sim().model().netRatePerSol(Ids.PRESSURE), 1e-12);
    assertTrue(back.report().ignored().isEmpty());
    // Both continue identically from here.
    for (int i = 0; i < 500; i++) {
      s.onServerTick();
      back.sim().onServerTick();
    }
    assertEquals(pressure(s), pressure(back.sim()), 1e-15);
  }

  @Test
  void newerSchemaIsRefusedAndUnknownIdsAreReported() throws Exception {
    PlanetState newer = new PlanetState(PlanetState.CURRENT_SCHEMA + 1, Map.of(), 0, 0, Map.of());
    IllegalStateException e =
        assertThrows(
            IllegalStateException.class,
            () -> PlanetSim.fromState(newer, index(), defs(), Pace.STANDARD));
    System.out.println("newer schema refused: " + e.getMessage());
    assertTrue(e.getMessage().contains("refusing"));

    PlanetState odd =
        new PlanetState(
            1,
            Map.of("mars:pressure", 3.0, "mars:from_the_future", 9.0),
            24_000,
            7,
            Map.of("mars:future_machine", List.of(5L)));
    PlanetSim.Loaded l = PlanetSim.fromState(odd, index(), defs(), Pace.STANDARD);
    System.out.println(
        "restore report: ignored="
            + l.report().ignored()
            + " defaulted="
            + l.report().defaulted()
            + " unknownMachines="
            + l.unknownMachineTypes());
    assertEquals(3.0, pressure(l.sim()), 1e-12);
    assertEquals(1, l.report().ignored().size());
    assertTrue(l.report().defaulted().contains(Ids.TEMPERATURE));
    assertEquals(-60, l.sim().model().snapshot().get(Ids.TEMPERATURE), 1e-12);
    assertEquals(List.of("mars:future_machine"), l.unknownMachineTypes());
    // The unknown machine's position survives a re-save.
    assertEquals(List.of(5L), l.sim().toState().machines().get("mars:future_machine"));
    assertEquals(7, l.sim().toState().pendingTicks());
  }

  @Test
  void restoreClampsToLimitsAndIgnoresNaN() throws Exception {
    PlanetState wild =
        new PlanetState(
            1, Map.of("mars:pressure", 1e9, "mars:temperature", Double.NaN), 0, 0, Map.of());
    PlanetSim s = PlanetSim.fromState(wild, index(), defs(), Pace.STANDARD).sim();
    assertEquals(200.0, pressure(s), 1e-12);
    assertEquals(-60.0, s.model().snapshot().get(Ids.TEMPERATURE), 1e-12);
  }
}
