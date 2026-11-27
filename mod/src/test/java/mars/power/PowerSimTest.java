package mars.power;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mars.TestData;
import mars.TestSims;
import mars.core.Ids;
import mars.hazards.dust.PanelDustTuning;
import mars.index.sim.Pace;
import mars.index.sim.PlanetSim;
import mars.monitor.view.MonitorView;
import org.junit.jupiter.api.Test;

/** Power through the real planet sim and the real data files (slice 3, balance sheet 2b). */
class PowerSimTest {
  private static final String OUT = TestSims.OUTGASSER;

  private static double pressure(PlanetSim s) {
    return s.model().snapshot().get(Ids.PRESSURE);
  }

  private static MonitorView.Row power(PlanetSim s) {
    return MonitorView.of(s).rows().stream()
        .filter(r -> r.labelKey().equals("monitor.row.power"))
        .findFirst()
        .orElseThrow();
  }

  @Test
  void anUnpoweredOutgasserRaisesNothingAndTheMonitorSaysWhy() throws Exception {
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    s.addMachine(OUT, 1L);
    double p0 = pressure(s);
    s.model().advanceSols(10);
    System.out.println("unpowered Outgasser, 10 sols: " + p0 + " -> " + pressure(s));
    assertEquals(p0, pressure(s), 1e-12);
    MonitorView.Row r = power(s);
    assertEquals("monitor.power.unpowered", r.valueKey());
    MonitorView.Row p =
        MonitorView.of(s).rows().stream()
            .filter(x -> x.labelKey().equals("monitor.row.pressure"))
            .findFirst()
            .orElseThrow();
    assertEquals("0.00\u00a0kPa/sol", p.causes().get(0).rate());
  }

  @Test
  void oneCleanPanelRunsOneOutgasserAtFullRate() throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 1);
    s.addMachine(OUT, 1L);
    double p0 = pressure(s);
    s.model().advanceSols(10);
    System.out.println("1 panel, 1 Outgasser, 10 sols: " + p0 + " -> " + pressure(s));
    assertEquals(TestSims.coldPressure(p0, 0.1, 10), pressure(s), 1e-3);
    assertEquals("monitor.power.powered", power(s).valueKey());
  }

  @Test
  void twoOutgassersOnOnePanelShareTheShortfall() throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 1);
    s.addMachine(OUT, 1L);
    s.addMachine(OUT, 2L);
    double p0 = pressure(s);
    s.model().advanceSols(10);
    System.out.println("1 panel, 2 Outgassers, 10 sols: " + p0 + " -> " + pressure(s));
    assertEquals(TestSims.coldPressure(p0, 2.0 * 0.625 * 0.1, 10), pressure(s), 1e-3);
    assertEquals("monitor.power.short", power(s).valueKey());
  }

  @Test
  void anOutgasserAndTwoOvensShareOneCleanPanelByTier() throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 1);
    s.addMachine(OUT, 1L);
    s.addMachine(TestSims.OVEN, 10L);
    s.addMachine(TestSims.OVEN, 11L);
    assertEquals(6.0, s.power().demandPerSol(), 1e-12);
    assertEquals(5.0, s.power().supplyPerSol(), 1e-12);
    double sources = s.model().netRatePerSol(Ids.PRESSURE);
    System.out.println("Outgasser + 2 Ovens + 1 panel: source " + sources + " kPa/sol");
    // Tiers (balance sheet 2d.4): the Ovens (tier 2) are served first and run full (0.02), the
    // Outgasser (tier 3) gets what is left, 3 of its 4 units (75 %): 0.02 + 0.075.
    assertEquals(0.095, sources, 1e-12);
    var causes = s.model().causes(Ids.PRESSURE);
    for (var c : causes) {
      double expected = c.source().equals(OUT) ? 0.10 * 0.75 : 0.02;
      assertEquals(expected, c.ratePerSol(), 1e-12, c.source());
    }
    assertEquals("monitor.power.short", power(s).valueKey());
    // Two Ovens alone on the clean panel: full rate, 3 units spare.
    PlanetSim o = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 1);
    o.addMachine(TestSims.OVEN, 10L);
    o.addMachine(TestSims.OVEN, 11L);
    assertEquals(0.02, o.model().netRatePerSol(Ids.PRESSURE), 1e-12);
    // An Oven with an empty grid contributes nothing; a panel restores it.
    PlanetSim e = TestSims.sim(Pace.STANDARD);
    e.addMachine(TestSims.OVEN, 10L);
    assertEquals(0.0, e.model().netRatePerSol(Ids.PRESSURE), 1e-12);
    e.addMachine(TestSims.PANEL, 1000L);
    assertEquals(0.01, e.model().netRatePerSol(Ids.PRESSURE), 1e-12);
  }

  @Test
  void ovenDataMatchesBalanceSheet2c() throws Exception {
    String all =
        Files.readString(
            TestData.pack().resolve("../docs/design/balance-sheet.md").normalize(),
            StandardCharsets.UTF_8);
    int a = all.indexOf("### Decision 1.");
    int b = all.indexOf("### Decision 2.", a);
    assertTrue(a >= 0 && b > a, "2c decision 1 not found");
    String sheet = all.substring(a, b);
    var oven = TestSims.defs().get(TestSims.OVEN);
    assertEquals(
        sheetNumber(sheet, "Oven draw \\| \\*\\*(\\d+(?:\\.\\d+)?) power unit per sol"),
        oven.power().drawPerSol());
    assertEquals(
        sheetNumber(sheet, "Oven pressure rate \\| \\+(\\d+(?:\\.\\d+)?) kPa per sol"),
        oven.contributions().get(0).ratePerSol());
    assertEquals(2, oven.startingGrant(), "two starting Ovens (decision 2)");
  }

  @Test
  void aDustyPanelPowersLessUsingTheDustTable() throws Exception {
    PanelDustTuning dust;
    try (Reader r =
        Files.newBufferedReader(TestData.data("hazards/panel_dust.json"), StandardCharsets.UTF_8)) {
      dust = PanelDustTuning.parse(r, "panel_dust.json");
    }
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    s.addMachine(TestSims.PANEL, 1000L);
    s.setEfficiency(1000L, dust.outputFraction(4, false)); // 60 %: 3.0 of 4 units
    s.addMachine(OUT, 1L);
    double p0 = pressure(s);
    s.model().advanceSols(10);
    System.out.println("stage 4 panel, 1 Outgasser, 10 sols: " + p0 + " -> " + pressure(s));
    assertEquals(TestSims.coldPressure(p0, 0.075, 10), pressure(s), 1e-3);
    // Cleaning the panel (back to stage 0) restores the full rate.
    s.setEfficiency(1000L, dust.outputFraction(0, false));
    double p1 = pressure(s);
    s.model().advanceSols(10);
    assertEquals(TestSims.coldPressure(p1, 0.1, 10), pressure(s), 1e-3);
  }

  @Test
  void aBatteryKeepsTheOutgasserRunningAfterThePanelIsGone() throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 1);
    s.addMachine(TestSims.BATTERY, 2000L);
    s.addMachine(OUT, 1L);
    s.model().advanceSols(10); // 1 spare unit per sol: charge reaches 10 (full)
    System.out.println("battery after 10 sols with a spare unit per sol: " + s.power().charge());
    assertEquals(10.0, s.power().charge(), 1e-6);
    s.removeMachine(TestSims.PANEL, 1000L);
    double p0 = pressure(s);
    s.model().advanceSols(5); // 10 units last 2.5 sols at 4 per sol
    System.out.println("panel removed, 5 sols on the battery: " + p0 + " -> " + pressure(s));
    // 2.5 sols powered, then 2.5 sols with the frost only.
    double powered = TestSims.coldPressure(p0, 0.1, 2.5);
    assertEquals(TestSims.coldPressure(powered, 0.0, 2.5), pressure(s), 5e-3);
    assertEquals("monitor.power.unpowered", power(s).valueKey());
  }

  @Test
  void classroomPaceScalesThePlanetEffectButNotPower() throws Exception {
    PlanetSim std = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 1);
    PlanetSim cls = TestSims.withPanels(TestSims.sim(new Pace(4.0, 4, 24_000, 100)), 1);
    for (PlanetSim s : new PlanetSim[] {std, cls}) {
      s.addMachine(TestSims.BATTERY, 2000L);
      s.addMachine(OUT, 1L);
      s.model().advanceSols(3);
    }
    System.out.println(
        "charge standard " + std.power().charge() + " classroom " + cls.power().charge());
    assertEquals(std.power().charge(), cls.power().charge(), 1e-6);
  }

  @Test
  void saveAndRestoreKeepsChargeAndPanelOutput() throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 1);
    s.addMachine(TestSims.BATTERY, 2000L);
    s.setEfficiency(1000L, 0.6);
    s.addMachine(OUT, 1L);
    s.model().advanceSols(10); // supply 3 < 4: battery empty, 0.6 kept
    s.removeMachine(OUT, 1L);
    s.model().advanceSols(4); // 3 units/sol charge: 12 -> capped at 10
    var state = s.toState();
    PlanetSim back =
        PlanetSim.fromState(
                state,
                TestSims.index(),
                TestSims.defs(),
                TestSims.gates(),
                TestSims.frost(),
                Pace.STANDARD)
            .sim();
    System.out.println(
        "saved charge " + state.batteryCharge() + " restored " + back.power().charge());
    assertEquals(s.power().charge(), back.power().charge(), 1e-12);
    assertEquals(s.power().supplyPerSol(), back.power().supplyPerSol(), 1e-12);
    assertEquals(3.0, back.power().supplyPerSol(), 1e-12);
  }

  @Test
  void dataFilesMatchBalanceSheet2b() throws Exception {
    String all =
        Files.readString(
            TestData.pack().resolve("../docs/design/balance-sheet.md").normalize(),
            StandardCharsets.UTF_8);
    int a = all.indexOf("## 2b.");
    int b = all.indexOf("\n## 3.", a);
    assertTrue(a >= 0 && b > a, "balance sheet 2b not found");
    String sheet = all.substring(a, b);
    var defs = TestSims.defs();
    assertEquals(
        sheetNumber(sheet, "Clean solar panel supply \\| (\\d+(?:\\.\\d+)?) power units per sol"),
        defs.get(TestSims.PANEL).power().supplyPerSol());
    assertEquals(
        sheetNumber(sheet, "Regolith Outgasser draw \\| (\\d+(?:\\.\\d+)?) power units per sol"),
        defs.get(OUT).power().drawPerSol());
    assertEquals(
        sheetNumber(sheet, "Battery capacity \\| (\\d+(?:\\.\\d+)?) power units"),
        defs.get(TestSims.BATTERY).power().storage());
  }

  private static double sheetNumber(String sheet, String regex) {
    Matcher m = Pattern.compile(regex).matcher(sheet);
    assertTrue(m.find(), "balance sheet 2b has no match for: " + regex);
    return Double.parseDouble(m.group(1));
  }
}
