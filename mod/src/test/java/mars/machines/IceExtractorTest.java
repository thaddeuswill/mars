package mars.machines;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mars.TestData;
import mars.TestSims;
import mars.core.Ids;
import mars.gates.Condition;
import mars.gates.GateTracker;
import mars.index.sim.Pace;
import mars.index.sim.PlanetSim;
import mars.monitor.view.MonitorView;
import mars.power.MachineLook;
import mars.session.SessionClock;
import org.junit.jupiter.api.Test;

/**
 * Slice A, water (docs/design/phase3-hydrosphere-and-drift.md 2 and 3): the Hydrosphere loss term,
 * the ice extractor's rate, draw and temperature condition, the Hydrosphere gate, and the Monitor's
 * Surface water row. All numbers come from the real data files and are compared with the balance
 * sheet.
 */
class IceExtractorTest {
  private static final String EXTRACTOR = "mars:ice_extractor";

  private static String sheet() throws Exception {
    return Files.readString(
        TestData.pack().resolve("../docs/design/balance-sheet.md").normalize(),
        StandardCharsets.UTF_8);
  }

  private static String sheetLine(String start) throws Exception {
    return sheet()
        .lines()
        .filter(l -> l.startsWith(start))
        .findFirst()
        .orElseThrow(() -> new AssertionError("balance sheet has no row '" + start + "'"));
  }

  private static PlanetSim sim(Pace pace, int panels, int extractors) throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(pace), panels);
    for (int i = 0; i < extractors; i++) {
      s.addMachine(EXTRACTOR, 50L + i);
    }
    return s;
  }

  /** Advance with the temperature held (an emitter would hold it) in slices of 0.25 sol. */
  private static void run(PlanetSim s, double sols, double celsius) {
    for (double done = 0; done < sols - 1e-9; done += 0.25) {
      s.model().set(Ids.TEMPERATURE, celsius);
      s.model().advanceSols(Math.min(0.25, sols - done));
    }
  }

  private static double hydro(PlanetSim s) {
    return s.model().current(Ids.HYDROSPHERE);
  }

  @Test
  void dataMatchesTheBalanceSheet() throws Exception {
    var def = TestSims.defs().get(EXTRACTOR);
    String row = sheetLine("| Ice extractor");
    Matcher rate = Pattern.compile("hydrosphere \\+([0-9.]+) %/sol each").matcher(row);
    assertTrue(rate.find(), row);
    assertEquals(Double.parseDouble(rate.group(1)), def.contributions().get(0).ratePerSol(), 1e-12);
    assertEquals(Ids.HYDROSPHERE, def.contributions().get(0).parameter());
    String[] cells = row.split("\\|");
    assertEquals(Double.parseDouble(cells[3].trim()), def.power().drawPerSol(), 1e-12, "draw");
    Matcher cold = Pattern.compile("temperature ≥ [−-](\\d+) °C").matcher(row);
    assertTrue(cold.find(), row);
    var req = (Condition.Compare) def.requires();
    assertEquals(-Double.parseDouble(cold.group(1)), req.value(), 1e-12);
    assertEquals(Ids.TEMPERATURE, req.parameter());
    assertEquals(Condition.Op.GE, req.op());

    Matcher k = Pattern.compile("k = ([0-9.]+)/sol").matcher(sheetLine("| Hydrosphere loss term"));
    assertTrue(k.find());
    var loss =
        TestSims.index().parameters().stream()
            .filter(p -> p.id().equals(Ids.HYDROSPHERE))
            .findFirst()
            .orElseThrow()
            .loss();
    assertNotNull(loss);
    assertEquals(Double.parseDouble(k.group(1)), loss.perSol(), 1e-12);
    assertEquals(0.0, loss.baseline(), 1e-12);
  }

  @Test
  void inventedNumbersSayTheyAreInvented() throws Exception {
    String machine =
        Files.readString(TestData.data("machines/ice_extractor.json"), StandardCharsets.UTF_8);
    String param =
        Files.readString(TestData.data("parameters/hydrosphere.json"), StandardCharsets.UTF_8);
    assertTrue(machine.contains("INVENTED") && machine.contains("game-sized version"), machine);
    assertTrue(machine.contains("Rodriguez Well") && machine.contains("NTRS 20205011353"));
    assertTrue(param.contains("INVENTED"), "the loss rate is labelled invented");
  }

  @Test
  void worksOnlyAtOrAboveMinusTwentyAndOnlyWithPower() throws Exception {
    PlanetSim cold = sim(Pace.STANDARD, 1, 1);
    run(cold, 2, -20.5);
    assertEquals(0.0, hydro(cold), 1e-9, "below -20 C it adds nothing");
    PlanetSim warm = sim(Pace.STANDARD, 1, 1);
    run(warm, 2, -10);
    assertEquals(0.6, hydro(warm), 0.02, "0.30 per sol, minus a little loss");
    PlanetSim dark = sim(Pace.STANDARD, 0, 1);
    run(dark, 2, -10);
    assertEquals(0.0, hydro(dark), 1e-9, "with no power it adds nothing");
  }

  @Test
  void theConditionIsExactlyMinusTwentyAndIgnoresPace() throws Exception {
    for (Pace pace : new Pace[] {Pace.STANDARD, Pace.classroom(4)}) {
      PlanetSim s = sim(pace, 1, 1);
      s.model().set(Ids.TEMPERATURE, -20.0);
      assertTrue(s.machineRequirementMet(EXTRACTOR), "-20.0 is warm enough");
      s.model().set(Ids.TEMPERATURE, -20.0001);
      assertFalse(s.machineRequirementMet(EXTRACTOR), "just below is too cold");
      assertTrue(s.machineRequirementMet("mars:regolith_oven"), "no condition: always true");
    }
  }

  @Test
  void lossTermRestoresTowardZero() throws Exception {
    PlanetSim s = sim(Pace.STANDARD, 0, 0);
    s.model().set(Ids.HYDROSPHERE, 50.0);
    s.model().advanceSols(1);
    assertEquals(50.0 * Math.exp(-0.01), hydro(s), 0.01);
    s.model().advanceSols(99);
    assertEquals(50.0 * Math.exp(-1.0), hydro(s), 0.1);
    s.model().set(Ids.HYDROSPHERE, 0.0);
    s.model().advanceSols(5);
    assertEquals(0.0, hydro(s), 1e-12, "nothing below the baseline, nothing to lose");
  }

  @Test
  void oneExtractorSettlesAboutThirtyAndTwoAboutSixty() throws Exception {
    PlanetSim one = sim(Pace.STANDARD, 1, 1);
    run(one, 600, -10);
    assertEquals(30.0, hydro(one), 0.4);
    PlanetSim two = sim(Pace.STANDARD, 1, 2);
    run(two, 600, -10);
    assertEquals(60.0, hydro(two), 0.8);
  }

  @Test
  void hydrosphereGateLatchesAtTenWithNoHoldAndStaysLatched() throws Exception {
    PlanetSim s = sim(Pace.STANDARD, 1, 1);
    var gate =
        TestSims.gates().stream().filter(g -> g.id().equals("mars:hydrosphere")).findFirst().get();
    assertEquals(4, gate.order());
    assertEquals(null, gate.hold());
    s.model().set(Ids.HYDROSPHERE, 9.99);
    s.model().advanceSols(0.05);
    assertFalse(s.gates().isLatched("mars:hydrosphere"));
    s.model().set(Ids.HYDROSPHERE, 10.01);
    s.model().advanceSols(0.05);
    assertTrue(s.gates().isLatched("mars:hydrosphere"));
    s.model().set(Ids.HYDROSPHERE, 0.0);
    s.model().advanceSols(1);
    assertTrue(s.gates().isLatched("mars:hydrosphere"), "gates lock in");
    // Not scaled in a classroom either: physical threshold.
    PlanetSim room = sim(Pace.classroom(4), 1, 1);
    room.model().set(Ids.HYDROSPHERE, 9.99);
    room.model().advanceSols(0.05);
    assertFalse(room.gates().isLatched("mars:hydrosphere"));
  }

  @Test
  void thawUnlocksTheExtractorAndEveryUnlockedIdIsABuiltMachine() throws Exception {
    PlanetSim s = sim(Pace.STANDARD, 0, 0);
    assertFalse(s.gates().isUnlocked(EXTRACTOR));
    s.gates().latch("mars:thaw", GateTracker.Cause.ADMIN, 0);
    assertTrue(s.gates().isUnlocked(EXTRACTOR));
    var machines = TestSims.defs();
    for (var g : TestSims.gates()) {
      for (String id : g.unlocks()) {
        assertTrue(machines.containsKey(id), g.id() + " unlocks " + id + " which has no machine");
      }
    }
    assertEquals(7, TestSims.gates().size());
  }

  @Test
  void looksIdleNotRunningWhileTooCold() {
    assertEquals(MachineLook.IDLE, MachineLook.withRequirement(MachineLook.RUNNING, false));
    assertEquals(MachineLook.RUNNING, MachineLook.withRequirement(MachineLook.RUNNING, true));
    assertEquals(MachineLook.OFF, MachineLook.withRequirement(MachineLook.OFF, false));
    assertEquals(MachineLook.IDLE, MachineLook.withRequirement(MachineLook.IDLE, false));
  }

  @Test
  void pauseFreezesTheExtractorAndTheLoss() throws Exception {
    PlanetSim s = sim(Pace.STANDARD, 1, 1);
    s.model().set(Ids.TEMPERATURE, -10.0);
    s.model().set(Ids.HYDROSPHERE, 5.0);
    SessionClock clock = new SessionClock(true);
    for (int i = 0; i < 24_000; i++) {
      s.onServerTick(clock);
    }
    assertEquals(5.0, hydro(s), 1e-12, "paused: neither the extractor nor the loss moves it");
    clock.resume();
    for (int i = 0; i < 24_000; i++) {
      s.onServerTick(clock);
    }
    assertTrue(hydro(s) > 5.0);
  }

  @Test
  void surfaceWaterRowShowsFromTheThawOrWhenAboveZero() throws Exception {
    PlanetSim s = sim(Pace.STANDARD, 1, 0);
    assertTrue(row(s) == null, "hidden at the start: nothing to say about liquid water");
    s.model().set(Ids.HYDROSPHERE, 0.5);
    assertNotNull(row(s), "shown whenever the value is above 0");
    s.model().set(Ids.HYDROSPHERE, 0.0);
    assertTrue(row(s) == null);
    s.gates().latch("mars:thaw", GateTracker.Cause.ADMIN, 0);
    MonitorView.Row r = row(s);
    assertNotNull(r, "shown once the Thaw has latched");
    assertEquals("0.0 %", r.value().replace(' ', ' '));
    assertEquals(MonitorView.Trend.STEADY, r.trend());
  }

  @Test
  void surfaceWaterRowGivesTrendAndCauses() throws Exception {
    PlanetSim s = sim(Pace.STANDARD, 1, 1);
    s.gates().latch("mars:thaw", GateTracker.Cause.ADMIN, 0);
    run(s, 4, -10);
    MonitorView.Row r = row(s);
    assertEquals(MonitorView.Trend.RISING, r.trend());
    var src = r.causes().stream().map(MonitorView.CauseLine::sourceKey).toList();
    assertTrue(src.contains("block.mars.ice_extractor"), src.toString());
    assertTrue(src.contains("monitor.cause.hydrosphere_loss"), src.toString());
    var line =
        r.causes().stream().filter(c -> c.sourceKey().equals("block.mars.ice_extractor")).toList();
    assertEquals(1, line.get(0).count());
    assertEquals(null, line.get(0).reasonKey());
    // Too cold: the extractor line says why instead of a rate.
    s.model().set(Ids.TEMPERATURE, -30.0);
    var cold =
        row(s).causes().stream()
            .filter(c -> c.sourceKey().equals("block.mars.ice_extractor"))
            .findFirst()
            .get();
    assertEquals("monitor.cause.too_cold", cold.reasonKey());
    // No power at all: the power reason wins.
    PlanetSim dark = sim(Pace.STANDARD, 0, 1);
    dark.model().set(Ids.TEMPERATURE, -10.0);
    dark.gates().latch("mars:thaw", GateTracker.Cause.ADMIN, 0);
    var unpowered = row(dark).causes().get(0);
    assertEquals("monitor.cause.needs_power", unpowered.reasonKey());
  }

  private static MonitorView.Row row(PlanetSim s) {
    return MonitorView.of(s).rows().stream()
        .filter(r -> r.labelKey().equals("monitor.row.hydrosphere"))
        .findFirst()
        .orElse(null);
  }

  @Test
  void nextGoalShowsHydrosphereProgressAfterTheThaw() throws Exception {
    PlanetSim s = sim(Pace.STANDARD, 1, 1);
    for (String g : new String[] {"mars:dome_stable", "mars:phase_2_begins", "mars:thaw"}) {
      s.gates().latch(g, GateTracker.Cause.ADMIN, 0);
    }
    s.model().set(Ids.HYDROSPHERE, 5.0);
    MonitorView.Row goal =
        MonitorView.of(s).rows().stream()
            .filter(r -> r.labelKey().equals("monitor.row.next_goal"))
            .findFirst()
            .get();
    assertEquals("gate.mars.hydrosphere", goal.valueKey());
    assertEquals("monitor.gate.hydrosphere.progress", goal.detailKey());
    assertEquals("3 / 7", goal.detailRight());
    assertTrue(goal.side().endsWith("/ 10 %"), goal.side());
    assertEquals(0.5, goal.goal().fraction(), 1e-9);
    assertEquals(null, goal.goal().etaKey(), "no time estimate for water in this build");
  }
}
