package mars.index.sim;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonParser;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mars.TestData;
import mars.TestSims;
import mars.core.Ids;
import mars.session.SessionClock;
import org.junit.jupiter.api.Test;

/** The polar-frost sink: three temperature branches, scaling, equilibrium, saves (slice 5). */
class FrostSinkTest {
  private static final double DT = 100.0 / 24_000.0; // one step in sols

  private static FrostSink sink() throws Exception {
    return new FrostSink(TestSims.frost());
  }

  private static double pressure(PlanetSim s) {
    return s.model().current(Ids.PRESSURE);
  }

  // ---- branch boundaries ----

  @Test
  void branchesSwitchExactlyAtMinus40AndMinus20() throws Exception {
    FrostSink f = sink();
    assertEquals(FrostSink.Branch.COLD, f.branch(-40.0001));
    assertEquals(FrostSink.Branch.MILD, f.branch(-40.0), "-40 is mild (both ends included)");
    assertEquals(FrostSink.Branch.MILD, f.branch(-30));
    assertEquals(FrostSink.Branch.MILD, f.branch(-20.0), "-20 is mild (both ends included)");
    assertEquals(FrostSink.Branch.RELEASE, f.branch(-19.9999));
    System.out.println("branches: -40.0001 COLD, -40 MILD, -20 MILD, -19.9999 RELEASE");
  }

  @Test
  void coldTakesTwoPercentAndMildOnePercentOfPressureAboveBaseline() throws Exception {
    // 5.00 kPa, 4.40 above the 0.60 baseline: 2 % = 0.088 per sol (CX-3 check), 1 % = 0.044.
    FrostSink cold = sink();
    assertEquals(-0.088, cold.ratePerSol(5.0, -50, 1.0), 1e-12);
    assertEquals(-0.088 * DT, cold.step(5.0, -50, DT), 1e-12);
    FrostSink justCold = sink();
    assertEquals(-0.088, justCold.ratePerSol(5.0, -40.0001, 1.0), 1e-12);
    FrostSink mild = sink();
    assertEquals(-0.044, mild.ratePerSol(5.0, -40, 1.0), 1e-12);
    assertEquals(-0.044, mild.ratePerSol(5.0, -20, 1.0), 1e-12);
    assertEquals(-0.044 * DT, mild.step(5.0, -30, DT), 1e-12);
    System.out.println("5 kPa: cold -0.088/sol, mild -0.044/sol");
  }

  @Test
  void zeroAtOrBelowBaselineAndItNeverAddsAirWhileTaking() throws Exception {
    for (double t : new double[] {-60, -40.0001, -40, -30, -20}) {
      FrostSink f = sink();
      assertEquals(0.0, f.step(0.6, t, 1.0), 0.0, "at baseline, T=" + t);
      assertEquals(0.0, f.step(0.3, t, 1.0), 0.0, "below baseline, T=" + t);
      assertEquals(0.0, f.step(0.0, t, 1.0), 0.0, "zero pressure, T=" + t);
      assertEquals(0.0, f.ratePerSol(0.4, t, 1.0), 0.0);
      assertEquals(0.0, f.storedKpa(), 0.0, "nothing was taken");
      assertTrue(f.step(0.6001, t, DT) <= 0, "never positive while taking, T=" + t);
    }
  }

  // ---- release ----

  @Test
  void warmReturnsTheStoredAirThenTheBuriedHalfKpaOnce() throws Exception {
    FrostSink f = sink();
    double taken = 0;
    for (int i = 0; i < 2400; i++) {
      taken -= f.step(3.0, -50, DT); // 10 sols of cold at a held 3.0 kPa
    }
    assertEquals(taken, f.storedKpa(), 1e-12);
    System.out.println("stored after 10 cold sols at 3 kPa: " + f.storedKpa());
    assertTrue(f.storedKpa() > 0.3);
    double stored = f.storedKpa();
    // Warm: the reservoir comes back at 0.5 kPa per sol, then 0.5 kPa more, then nothing.
    double returned = 0;
    double firstSol = 0;
    for (int i = 0; i < 240 * 20; i++) {
      double d = f.step(3.0, -10, DT);
      assertTrue(d >= 0, "release never takes air");
      returned += d;
      if (i == 239) {
        firstSol = returned;
      }
    }
    System.out.println(
        "returned " + returned + " (stored " + stored + " + buried 0.5), first sol " + firstSol);
    assertEquals(0.5, firstSol, 1e-9, "0.5 kPa per sol");
    assertEquals(stored + 0.5, returned, 1e-9);
    assertEquals(0.0, f.storedKpa(), 1e-12);
    assertEquals(0.0, f.buriedRemainingKpa(), 1e-12);
    // The buried CO2 gives back once: freeze some more, warm again, only the stored part returns.
    double again = 0;
    for (int i = 0; i < 2400; i++) {
      again -= f.step(3.0, -50, DT);
    }
    double back = 0;
    for (int i = 0; i < 240 * 20; i++) {
      back += f.step(3.0, -10, DT);
    }
    assertEquals(again, back, 1e-9, "second warm spell returns only what was taken");
  }

  @Test
  void releaseRateAtExactlyMinus20IsStillMild() throws Exception {
    FrostSink f = sink();
    assertTrue(f.step(2.0, -20, DT) < 0, "at -20 the frost still takes");
    assertTrue(f.step(2.0, -19.99, DT) > 0, "above -20 it gives back (buried air is there)");
  }

  // ---- pace scaling ----

  @Test
  void rateScaleSpeedsUpTheSinkAndTheReleaseButNotTheBuriedAmount() throws Exception {
    double scale = 4.0 / (1 + 0.35 * 3); // classroom x4, group 4
    FrostSink f = sink();
    assertEquals(-0.088 * scale, f.ratePerSol(5.0, -50, scale), 1e-12);
    assertEquals(0.5 * scale, f.ratePerSol(5.0, -10, scale), 1e-12, "release rate scales");
    double total = 0;
    for (int i = 0; i < 240 * 5; i++) {
      total += f.step(5.0, -10, DT * scale);
    }
    assertEquals(0.5, total, 1e-9, "the buried amount is not scaled");
  }

  // ---- equilibrium against the data ----

  /** A build: panels (with an output fraction), Outgassers and Ovens. Returns the settled kPa. */
  private static double settle(Pace pace, double panelFraction, int panels, int outg, int ovens)
      throws Exception {
    PlanetSim s = TestSims.sim(pace);
    for (int i = 0; i < panels; i++) {
      s.addMachine(TestSims.PANEL, 1000L + i);
      s.setEfficiency(1000L + i, panelFraction);
    }
    for (int i = 0; i < outg; i++) {
      s.addMachine(TestSims.OUTGASSER, 1L + i);
    }
    for (int i = 0; i < ovens; i++) {
      s.addMachine(TestSims.OVEN, 100L + i);
    }
    // The slowest mode here (group of 5, time constant about 120 sols) settles within 1500 sols.
    s.model().advanceSols(1500);
    return pressure(s);
  }

  @Test
  void equilibriumIsBaselinePlusFiftyTimesTheNetSourceAndMatchesTheSheetTable() throws Exception {
    double k = TestSims.frost().coldFractionPerSol();
    assertEquals(0.02, k, 1e-12);
    // Balance sheet 2c decision 4 table: build, S (kPa/sol), P_eq, reaches 5 kPa.
    record Row(String name, double panel, int panels, int og, int ov, double s, double eq) {}
    List<Row> rows =
        List.of(
            new Row("2 Ovens only", 1.0, 1, 0, 2, 0.02, 1.6),
            new Row("1 Outgasser, 1 clean panel", 1.0, 1, 1, 0, 0.10, 5.6),
            new Row("1 Outgasser + 2 Ovens, 1 clean panel (83 %)", 1.0, 1, 1, 2, 0.10, 5.6),
            new Row("1 Outgasser, stage 4 panel (60 %)", 0.6, 1, 1, 0, 0.075, 4.35),
            new Row("1 Outgasser + 2 Ovens, stage 2 panel (82 %)", 0.82, 1, 1, 2, 0.082, 4.7),
            new Row("2 Outgassers + 2 Ovens, 2 clean panels", 1.0, 2, 2, 2, 0.22, 11.6));
    for (Row r : rows) {
      double got = settle(Pace.STANDARD, r.panel, r.panels, r.og, r.ov);
      double formula = 0.6 + r.s / k;
      System.out.printf(
          "%-48s S=%.3f  P_eq sheet %.2f  formula %.3f  simulated %.3f  reaches 5 kPa: %s%n",
          r.name, r.s, r.eq, formula, got, got > 5.0);
      assertEquals(r.eq, formula, 0.011, "the formula matches the sheet row: " + r.name);
      assertEquals(formula, got, 0.01, r.name);
    }
  }

  @Test
  void paceAndGroupScalingKeepTheEquilibriumEqual() throws Exception {
    double standard = settle(Pace.STANDARD, 1.0, 2, 2, 2);
    for (Pace p :
        new Pace[] {
          Pace.classroom(3), Pace.classroom(4), Pace.classroom(5), new Pace(1.0, 5, 24_000, 100)
        }) {
      double got = settle(p, 1.0, 2, 2, 2);
      System.out.printf("equilibrium %s: %.4f (standard %.4f)%n", p, got, standard);
      assertEquals(standard, got, 0.01, p.toString());
    }
  }

  @Test
  void theMonitorsEquilibriumFigureIsTheSameInEveryMode() throws Exception {
    for (Pace p : new Pace[] {Pace.STANDARD, Pace.classroom(4), Pace.classroom(5)}) {
      PlanetSim s = TestSims.withPanels(TestSims.sim(p), 1);
      s.addMachine(TestSims.OUTGASSER, 1L);
      assertEquals(5.6, s.frostEquilibriumKpa(), 1e-9, p.toString());
    }
  }

  // ---- pause and saves ----

  @Test
  void aPausedSessionLeavesPressureAndReservoirUnchanged() throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 1);
    s.addMachine(TestSims.OUTGASSER, 1L);
    s.model().advanceSols(20);
    double p = pressure(s);
    double stored = s.frostStoredKpa();
    assertTrue(stored > 0.1);
    SessionClock clock = new SessionClock(true);
    for (int i = 0; i < 100_000; i++) {
      s.onServerTick(clock);
    }
    assertEquals(p, pressure(s), 0.0);
    assertEquals(stored, s.frostStoredKpa(), 0.0);
    System.out.println(
        "paused 100,000 ticks: pressure " + p + ", reservoir " + stored + " unchanged");
  }

  @Test
  void theReservoirSurvivesASaveAndWarmingAfterTheLoadReturnsIt() throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 1);
    s.addMachine(TestSims.OUTGASSER, 1L);
    s.model().advanceSols(30);
    PlanetState st = s.toState();
    assertEquals(PlanetState.CURRENT_SCHEMA, st.schema());
    assertEquals(5, st.schema());
    assertEquals(s.frostStoredKpa(), st.frost().storedKpa(), 0.0);
    PlanetSim back =
        PlanetSim.fromState(
                st,
                TestSims.index(),
                TestSims.defs(),
                TestSims.gates(),
                TestSims.frost(),
                Pace.STANDARD)
            .sim();
    assertEquals(st, back.toState());
    assertEquals(s.frostStoredKpa(), back.frostStoredKpa(), 0.0);
    // Warm both and compare.
    s.model().set(Ids.TEMPERATURE, 0);
    back.model().set(Ids.TEMPERATURE, 0);
    s.model().advanceSols(2);
    back.model().advanceSols(2);
    assertEquals(pressure(s), pressure(back), 1e-12);
  }

  @Test
  void anOlderSaveLoadsWithAnEmptyReservoir() throws Exception {
    // A schema 4 save has no frost part: it is built with the old constructor.
    PlanetState old =
        new PlanetState(
            4,
            Map.of("mars:pressure", 2.0),
            24_000,
            0,
            Map.of(),
            0,
            Map.of(),
            Map.of(),
            PressureHistory.State.EMPTY);
    PlanetSim s =
        PlanetSim.fromState(
                old,
                TestSims.index(),
                TestSims.defs(),
                TestSims.gates(),
                TestSims.frost(),
                Pace.STANDARD)
            .sim();
    assertEquals(2.0, pressure(s), 1e-12);
    assertEquals(0.0, s.frostStoredKpa(), 0.0);
    assertEquals(0.5, s.frostBuriedRemainingKpa(), 0.0);
    assertEquals(PlanetState.CURRENT_SCHEMA, s.toState().schema());
    // A schema 5 save from a newer build is still refused.
    assertThrows(
        IllegalStateException.class,
        () ->
            PlanetSim.fromState(
                new PlanetState(6, Map.of(), 0, 0, Map.of()),
                TestSims.index(),
                TestSims.defs(),
                TestSims.gates(),
                TestSims.frost(),
                Pace.STANDARD));
  }

  // ---- data ----

  @Test
  void theBaselineIsPerPlanetData() throws Exception {
    try (var r =
        Files.newBufferedReader(TestData.data("sinks/polar_frost.json"), StandardCharsets.UTF_8)) {
      FrostSink.Def def =
          FrostSink.Def.parse(JsonParser.parseReader(r).getAsJsonObject(), "polar_frost.json");
      assertEquals(0.6, def.forPlanet("mars:planet_1").baselineKpa(), 0.0);
      assertEquals(0.6, def.forPlanet("mars:planet_unknown").baselineKpa(), 0.0, "default");
      FrostSink.Def other = new FrostSink.Def(def.defaults(), Map.of("mars:planet_2", 1.1));
      assertEquals(1.1, other.forPlanet("mars:planet_2").baselineKpa(), 0.0);
      assertEquals(0.6, other.forPlanet("mars:planet_1").baselineKpa(), 0.0);
      // A planet with a higher baseline: nothing is taken below it.
      FrostSink f = new FrostSink(other.forPlanet("mars:planet_2"));
      assertEquals(0.0, f.step(1.0, -60, 1.0), 0.0);
      assertTrue(f.step(2.0, -60, DT) < 0);
    }
  }

  @Test
  void theDataFileMatchesBalanceSheet2c() throws Exception {
    String sheet =
        Files.readString(
            TestData.pack().resolve("../docs/design/balance-sheet.md").normalize(),
            StandardCharsets.UTF_8);
    int a = sheet.indexOf("### Frost sink model");
    int b = sheet.indexOf("### Decision 7", a);
    assertTrue(a >= 0 && b > a, "frost sink model section not found");
    String m = sheet.substring(a, b);
    FrostSink.Params p = TestSims.frost();
    assertEquals(num(m, "`baselineKpa` = (\\d+(?:\\.\\d+)?)"), p.baselineKpa(), 1e-12);
    assertEquals(
        num(m, "T < -40 C: takes (\\d+(?:\\.\\d+)?) %") / 100, p.coldFractionPerSol(), 1e-12);
    assertEquals(
        num(m, "-40 C <= T <= -20 C: takes (\\d+(?:\\.\\d+)?) %") / 100,
        p.mildFractionPerSol(),
        1e-12);
    assertEquals(
        num(m, "at \\+(\\d+(?:\\.\\d+)?) kPa/sol until empty"), p.releaseRateKpaPerSol(), 1e-12);
    assertEquals(
        num(m, "until empty, then up to (\\d+(?:\\.\\d+)?) kPa more"), p.buriedKpa(), 1e-12);
    assertEquals(-40, p.coldBelowC(), 0.0);
    assertEquals(-20, p.releaseAboveC(), 0.0);
  }

  private static double num(String text, String regex) {
    Matcher mm = Pattern.compile(regex).matcher(text);
    assertTrue(mm.find(), "balance sheet has no match for: " + regex);
    return Double.parseDouble(mm.group(1));
  }
}
