package mars.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import mars.TestSims;
import mars.dome.Probe;
import mars.index.sim.Pace;
import mars.index.sim.PlanetSim;
import mars.monitor.view.MonitorView;
import org.junit.jupiter.api.Test;

/** The storm and dust lines on the Monitor and their height budget (f1-storm-spec.md AC-17). */
class MonitorStormLayoutTest {
  private static final int H3 = 240;

  private static Probe room(boolean hole) {
    return (x, y, z) -> {
      if (hole && x == 0 && y == 2 && z == 2) {
        return Probe.Cell.OUTSIDE;
      }
      boolean wall = x <= 0 || x >= 4 || y <= 0 || y >= 4 || z <= 0 || z >= 4;
      boolean outer = x < 0 || x > 4 || y < 0 || y > 4 || z < 0 || z > 4;
      return outer ? Probe.Cell.OUTSIDE : wall ? Probe.Cell.AIRTIGHT : Probe.Cell.OPEN;
    };
  }

  /** A dome with life support, one dusty panel, two Ovens, and a storm in the asked state. */
  private static PlanetSim world(boolean leak, String storm) throws Exception {
    PlanetSim s = TestSims.lifeSim(Pace.STANDARD);
    s.addMachine("mars:oxygen_maker", 100L);
    s.addMachine("mars:water_reclaimer", 200L);
    for (int i = 0; i < 5; i++) {
      s.addMachine("mars:o2_tank", 300L + i);
    }
    s.addMachine(TestSims.BATTERY, 400L);
    s.addMachine(TestSims.OVEN, 500L);
    s.addMachine(TestSims.OVEN, 501L);
    s.addMachine(TestSims.OUTGASSER, 600L);
    s.addMachine(TestSims.PANEL, 1000L);
    s.configureHazards(TestSims.hazards(), 3);
    s.setPanelDust(1000L, 3, false, 0.7);
    s.domes().register(mars.dome.Pos.pack(2, 2, 2));
    s.domes().refreshDirty(room(leak));
    s.startLifeSupport();
    switch (storm) {
      case "warned" -> {
        s.scheduleStorm(0.5, 0.75);
        s.model().step();
        s.model().step();
      }
      case "active" -> {
        s.scheduleStorm(0.05, 0.75);
        while (s.stormStrength() < 0.99) {
          s.model().step();
        }
      }
      default -> {}
    }
    return s;
  }

  private static void check(String name, PlanetSim s, boolean expectStorm) {
    MonitorView raw = MonitorView.of(s);
    MonitorView fit = MonitorLayout.fit(raw);
    int rows = MonitorLayout.fixedHeight(fit);
    int plot = MonitorLayout.plotHeight(fit, H3);
    int total = MonitorLayout.panelHeight(fit, H3);
    System.out.printf(
        "%-26s rows %d plot %d panel %d GUI px (scale 3, 240 px)%n", name, rows, plot, total);
    assertTrue(total <= H3 - 2 * MonitorStyle.MARGIN, name + " fits: " + total);
    assertTrue(fit.hasCause(MonitorView.DUST_LINE), name + ": the dust line never drops");
    if (expectStorm) {
      assertTrue(
          fit.hasCause(MonitorView.STORM_ACTIVE), name + ": the active storm line never drops");
      assertTrue(fit.hasCause(MonitorView.STORM_CUT));
    }
  }

  @Test
  void stormWarnedShowsOneCloudLineWithNoPowerOrLightWord() throws Exception {
    PlanetSim s = world(false, "warned");
    MonitorView v = MonitorView.of(s);
    assertTrue(v.hasCause(MonitorView.STORM_EXPECTED));
    assertFalse(v.hasCause(MonitorView.STORM_ACTIVE));
    assertFalse(v.hasCause(MonitorView.STORM_CUT), "the cut is held until the lock");
    var line =
        v.rows().stream()
            .flatMap(r -> r.causes().stream())
            .filter(c -> c.sourceKey().equals(MonitorView.STORM_EXPECTED))
            .findFirst()
            .orElseThrow();
    assertEquals(MonitorView.CauseLine.CLOUD, line.direction());
    assertEquals("half a sol", line.rate());
    check("storm warned", s, false);
  }

  @Test
  void stormActiveShowsTheLineAndTheCutAndTheDustLineStaysSeparate() throws Exception {
    PlanetSim s = world(false, "active");
    MonitorView v = MonitorView.of(s);
    assertTrue(v.hasCause(MonitorView.STORM_ACTIVE));
    assertTrue(v.hasCause(MonitorView.STORM_CUT));
    assertTrue(v.hasCause(MonitorView.DUST_LINE));
    assertFalse(v.hasCause(MonitorView.STORM_EXPECTED));
    var cut =
        v.rows().stream()
            .flatMap(r -> r.causes().stream())
            .filter(c -> c.sourceKey().equals(MonitorView.STORM_CUT))
            .findFirst()
            .orElseThrow();
    assertEquals("−60 %", cut.rate(), "Phase 1 storm at full strength cuts 60 %");
    assertEquals(-1, cut.direction());
    check("storm active", s, true);
  }

  @Test
  void theWorstCombinationKeepsEveryProblemLineAndTheLeakingDome() throws Exception {
    PlanetSim s = world(true, "active");
    MonitorView fit = MonitorLayout.fit(MonitorView.of(s));
    assertTrue(
        fit.rows().stream().anyMatch(r -> "monitor.dome.leaking".equals(r.valueKey())),
        "a leaking dome is a problem line and never drops");
    check("storm + dust + leak", s, true);
    // The plot goes first: with this many lines there is none.
    assertEquals(0, MonitorLayout.plotHeight(fit, H3));
  }

  @Test
  void theWarningLineIsTheLastInformationLineToGoAndOnlyWhenNothingElseFits() throws Exception {
    PlanetSim s = world(true, "warned");
    MonitorView fit = MonitorLayout.fit(MonitorView.of(s));
    int total = MonitorLayout.panelHeight(fit, H3);
    System.out.println(
        "warned + leak: panel "
            + total
            + " forecast line kept "
            + fit.hasCause(MonitorView.STORM_EXPECTED));
    assertTrue(total <= H3 - 2 * MonitorStyle.MARGIN);
    assertTrue(fit.hasCause(MonitorView.DUST_LINE));
  }

  @Test
  void theHeldViewHasNoStormOrDustLine() throws Exception {
    MonitorView v = MonitorView.of(world(false, "active"));
    MonitorView held =
        v.withoutCause(MonitorView.DUST_LINE)
            .withoutCause(MonitorView.STORM_EXPECTED)
            .withoutCause(MonitorView.STORM_ACTIVE)
            .withoutCause(MonitorView.STORM_CUT);
    assertFalse(held.hasCause(MonitorView.STORM_CUT));
    assertFalse(held.hasCause(MonitorView.STORM_ACTIVE));
  }

  @Test
  void etaWordsRoundToHalfSols() {
    assertEquals("half a sol", MonitorView.etaWords(0.4));
    assertEquals("1 sol", MonitorView.etaWords(1.0));
    assertEquals("1.5 sols", MonitorView.etaWords(1.4));
    assertEquals("2 sols", MonitorView.etaWords(2.1));
    assertEquals("less than half a sol", MonitorView.etaWords(0.1));
  }

  @Test
  void theLineTextsNameNoPowerOrLightWordBeforeTheLock() throws Exception {
    // CR-F9: the warning, the reminder and the expected-storm line must not say what the storm
    // does.
    String lang =
        java.nio.file.Files.readString(
            mars.TestData.pack().resolve("assets/mars/lang/en_us.json"),
            java.nio.charset.StandardCharsets.UTF_8);
    var json = com.google.gson.JsonParser.parseString(lang).getAsJsonObject();
    for (String key :
        new String[] {"log.storm.warn", "log.storm.remind", "monitor.storm.expected"}) {
      String text = json.get(key).getAsString().toLowerCase(java.util.Locale.ROOT);
      for (String banned : new String[] {"solar", "power", "sunlight", "light", "dim"}) {
        assertFalse(text.contains(banned), key + " contains '" + banned + "': " + text);
      }
    }
    assertEquals(
        "Dust storm forming. Arrival in about 1 sol. Expected length: half a sol to a full sol.",
        json.get("log.storm.warn").getAsString());
  }
}
