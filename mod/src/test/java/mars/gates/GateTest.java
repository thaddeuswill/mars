package mars.gates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonParser;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mars.TestData;
import mars.TestSims;
import mars.core.Ids;
import mars.index.sim.Pace;
import mars.index.sim.PlanetSim;
import mars.monitor.view.MonitorView;
import org.junit.jupiter.api.Test;

/** Gates: Dome stable (machine state + hold), Phase 2 begins, latching, I-1 and I-2. */
class GateTest {
  static final String DOME = "mars:dome_stable";
  static final String PHASE2 = "mars:phase_2_begins";

  private static PlanetSim sim(Pace pace) throws Exception {
    return TestSims.sim(pace);
  }

  private static void panel(PlanetSim s) {
    s.addMachine(TestSims.PANEL, 1000L);
  }

  private static boolean latched(PlanetSim s, String id) {
    return s.gates().isLatched(id);
  }

  @Test
  void gateFilesMatchTheBalanceSheetAndDesign() throws Exception {
    var gates = TestSims.gates();
    GateDef dome = gates.stream().filter(g -> g.id().equals(DOME)).findFirst().orElseThrow();
    GateDef p2 = gates.stream().filter(g -> g.id().equals(PHASE2)).findFirst().orElseThrow();
    String sheet = Files.readString(TestData.pack().resolve("../docs/design/balance-sheet.md"));
    Matcher m =
        Pattern.compile("\\| Dome stable \\| (\\d+) sols? [^|]*\\| (\\d+) sols?").matcher(sheet);
    assertTrue(m.find(), "balance sheet section 4 row not found");
    System.out.println(
        "hold sheet "
            + m.group(1)
            + "/"
            + m.group(2)
            + " file "
            + dome.hold().standardSols()
            + "/"
            + dome.hold().classroomSols());
    assertEquals(Double.parseDouble(m.group(1)), dome.hold().standardSols());
    assertEquals(Double.parseDouble(m.group(2)), dome.hold().classroomSols());
    assertTrue(dome.unlocks().contains("mars:regolith_outgasser"));
    Condition.Compare c = (Condition.Compare) p2.condition();
    assertEquals(5.0, c.value());
    assertEquals(Condition.Op.GE, c.op());
    assertEquals(Ids.PRESSURE, c.parameter());
  }

  @Test
  void domeStableLatchesAfterThreeSolsOfLifeSupportNotBefore() throws Exception {
    PlanetSim s = sim(Pace.STANDARD);
    assertFalse(s.gates().isUnlocked(TestSims.OUTGASSER));
    panel(s);
    s.model().advanceSols(2.9);
    System.out.println(
        "after 2.9 sols: latched="
            + latched(s, DOME)
            + " held="
            + s.gates().state(DOME).heldSols());
    assertFalse(latched(s, DOME));
    assertFalse(s.gates().isUnlocked(TestSims.OUTGASSER));
    s.model().advanceSols(0.2);
    System.out.println(
        "after 3.1 sols: latched="
            + latched(s, DOME)
            + " unlocked="
            + s.gates().isUnlocked(TestSims.OUTGASSER));
    assertTrue(latched(s, DOME));
    assertTrue(s.gates().isUnlocked(TestSims.OUTGASSER));
    assertEquals(List.of(DOME), s.drainNewlyLatched());
    assertEquals(List.of(), s.drainNewlyLatched());
  }

  @Test
  void noPowerMeansNoDomeStable() throws Exception {
    PlanetSim s = sim(Pace.STANDARD);
    s.model().advanceSols(20);
    assertFalse(latched(s, DOME));
    assertEquals(0.0, s.gates().state(DOME).heldSols(), 1e-9);
  }

  @Test
  void aShortFailurePausesTheCountAndALongOneResetsIt() throws Exception {
    PlanetSim s = sim(Pace.STANDARD);
    panel(s);
    s.model().advanceSols(2.0);
    double held = s.gates().state(DOME).heldSols();
    s.removeMachine(TestSims.PANEL, 1000L);
    s.model().advanceSols(0.5); // within the 1 sol grace
    double paused = s.gates().state(DOME).heldSols();
    System.out.println("held " + held + ", after 0.5 sol failure " + paused);
    assertEquals(held, paused, 1e-9);
    panel(s);
    s.model().advanceSols(0.5);
    assertEquals(held + 0.5, s.gates().state(DOME).heldSols(), 0.02);
    s.removeMachine(TestSims.PANEL, 1000L);
    s.model().advanceSols(1.2); // longer than the grace
    System.out.println("after 1.2 sol failure: held=" + s.gates().state(DOME).heldSols());
    assertEquals(0.0, s.gates().state(DOME).heldSols(), 1e-9);
    assertFalse(latched(s, DOME));
  }

  @Test
  void classroomHoldIsOneSolAndGraceIsHalf() throws Exception {
    PlanetSim s = sim(Pace.classroom(4));
    panel(s);
    s.model().advanceSols(0.9);
    assertFalse(latched(s, DOME));
    s.model().advanceSols(0.2);
    assertTrue(latched(s, DOME));
    assertEquals(0.5, Pace.classroom(4).holdResetSols());
  }

  @Test
  void i1_phase2ThresholdIsTheSameInEveryModeAndGroupSize() throws Exception {
    Pace[] paces = {
      Pace.STANDARD,
      new Pace(1.0, 5, 24_000, 100),
      Pace.classroom(3),
      Pace.classroom(4),
      Pace.classroom(5)
    };
    for (Pace pace : paces) {
      PlanetSim below = sim(pace);
      below.model().set(Ids.PRESSURE, 4.999);
      below.model().advanceSols(0.01);
      PlanetSim at = sim(pace);
      at.model().set(Ids.PRESSURE, 5.0);
      at.model().advanceSols(0.01);
      System.out.println(
          "I-1 "
              + pace
              + ": 4.999 kPa latched="
              + latched(below, PHASE2)
              + ", 5.0 kPa latched="
              + latched(at, PHASE2));
      assertFalse(latched(below, PHASE2), pace.toString());
      assertTrue(latched(at, PHASE2), pace.toString());
    }
  }

  @Test
  void i1_aGateFileCannotCarryAScalingField() {
    var bad =
        JsonParser.parseString(
                "{\"id\":\"mars:x\",\"condition\":{\"parameter\":\"mars:pressure\",\"op\":\">=\","
                    + "\"value\":5,\"scale\":0.5}}")
            .getAsJsonObject();
    var e = assertThrows(IllegalArgumentException.class, () -> GateDef.parse(bad, "x.json"));
    System.out.println("rejected: " + e.getMessage());
    assertTrue(e.getMessage().contains("scale"));
  }

  @Test
  void i2_latchedGatesNeverUnlatchWhenValuesFall() throws Exception {
    PlanetSim s = sim(Pace.STANDARD);
    panel(s);
    s.model().set(Ids.PRESSURE, 6.0);
    s.model().advanceSols(3.2);
    assertTrue(latched(s, DOME));
    assertTrue(latched(s, PHASE2));
    s.removeMachine(TestSims.PANEL, 1000L);
    s.model().set(Ids.PRESSURE, 0.6);
    s.model().advanceSols(60);
    System.out.println(
        "I-2 after pressure 0.6 and no power for 60 sols: dome="
            + latched(s, DOME)
            + " phase2="
            + latched(s, PHASE2));
    assertTrue(latched(s, DOME));
    assertTrue(latched(s, PHASE2));
    assertTrue(s.gates().isUnlocked(TestSims.OUTGASSER));
  }

  @Test
  void latchedGatesAndHoldCountersSurviveSaveAndRestore() throws Exception {
    PlanetSim s = sim(Pace.STANDARD);
    panel(s);
    s.model().advanceSols(1.5);
    var mid = s.toState();
    PlanetSim half =
        PlanetSim.fromState(mid, TestSims.index(), TestSims.defs(), TestSims.gates(), Pace.STANDARD)
            .sim();
    assertEquals(s.gates().state(DOME).heldSols(), half.gates().state(DOME).heldSols(), 1e-9);
    half.model().advanceSols(1.6);
    assertTrue(latched(half, DOME));
    var done = half.toState();
    PlanetSim again =
        PlanetSim.fromState(
                done, TestSims.index(), TestSims.defs(), TestSims.gates(), Pace.STANDARD)
            .sim();
    assertTrue(latched(again, DOME));
    assertTrue(again.gates().isUnlocked(TestSims.OUTGASSER));
  }

  @Test
  void aPausedSessionMovesNoGateCounter() throws Exception {
    PlanetSim s = sim(Pace.STANDARD);
    panel(s);
    mars.session.SessionClock clock = new mars.session.SessionClock(false);
    for (int i = 0; i < 2400; i++) {
      s.onServerTick(clock);
    }
    double held = s.gates().state(DOME).heldSols();
    clock.pause();
    for (int i = 0; i < 200_000; i++) {
      s.onServerTick(clock);
    }
    System.out.println("I-3 gates: held " + held + " -> " + s.gates().state(DOME).heldSols());
    assertEquals(held, s.gates().state(DOME).heldSols(), 0.0);
    assertFalse(latched(s, DOME));
  }

  @Test
  void anAdminLatchIsMarkedAdminAndUnlocks() throws Exception {
    PlanetSim s = sim(Pace.STANDARD);
    assertTrue(s.gates().latch(DOME, GateTracker.Cause.ADMIN, 0));
    assertFalse(s.gates().latch(DOME, GateTracker.Cause.ADMIN, 0));
    assertFalse(s.gates().latch("mars:nope", GateTracker.Cause.ADMIN, 0));
    assertEquals(GateTracker.Cause.ADMIN, s.gates().state(DOME).cause());
    assertTrue(s.gates().isUnlocked(TestSims.OUTGASSER));
  }

  @Test
  void conditionLanguageAllAnyNot() throws Exception {
    Condition c =
        Condition.parse(
            JsonParser.parseString(
                "{\"all\":[{\"parameter\":\"mars:pressure\",\"op\":\">=\",\"value\":1},"
                    + "{\"not\":{\"machine_state\":\"life_support_ok\"}}]}"),
            "t");
    Condition.Context ctx =
        new Condition.Context() {
          public double value(mars.core.ParameterId id) {
            return 2;
          }

          public boolean machineState(String name) {
            return false;
          }
        };
    assertTrue(c.eval(ctx));
    assertFalse(
        Condition.parse(JsonParser.parseString("{\"any\":[{\"machine_state\":\"a\"}]}"), "t")
            .eval(ctx));
  }

  @Test
  void anUnknownMachineStateFailsAtLoad() throws Exception {
    var bad =
        GateDef.parse(
            JsonParser.parseString("{\"id\":\"mars:y\",\"condition\":{\"machine_state\":\"typo\"}}")
                .getAsJsonObject(),
            "y.json");
    var e =
        assertThrows(
            IllegalArgumentException.class,
            () -> new PlanetSim(TestSims.index(), TestSims.defs(), List.of(bad), Pace.STANDARD));
    assertTrue(e.getMessage().contains("typo"));
  }

  @Test
  void monitorShowsNextGoalThenGoalsReached() throws Exception {
    PlanetSim s = sim(Pace.STANDARD);
    panel(s);
    s.model().advanceSols(1.0);
    MonitorView v = MonitorView.of(s);
    var next =
        v.rows().stream()
            .filter(r -> r.labelKey().equals("monitor.row.next_goal"))
            .findFirst()
            .orElseThrow();
    System.out.println("monitor before: " + next);
    assertEquals("gate.mars.dome_stable", next.valueKey());
    assertEquals("1.0 / 3 sols", next.side());
    assertEquals("monitor.gate.dome_stable.progress", next.detailKey());
    assertTrue(v.rows().stream().noneMatch(r -> r.labelKey().equals("monitor.row.goals_reached")));
    s.model().advanceSols(2.2);
    MonitorView after = MonitorView.of(s);
    var next2 =
        after.rows().stream()
            .filter(r -> r.labelKey().equals("monitor.row.next_goal"))
            .findFirst()
            .orElseThrow();
    System.out.println("monitor after: " + next2);
    assertEquals("1 / 2", next2.detailRight());
    assertEquals("monitor.goals.count", next2.detailRightKey());
    assertTrue(
        after.rows().stream().noneMatch(r -> r.labelKey().equals("monitor.row.goals_reached")));
    assertEquals("gate.mars.phase_2_begins", next2.valueKey());
    assertTrue(next2.side().endsWith("/ 5 kPa"));
  }
}
