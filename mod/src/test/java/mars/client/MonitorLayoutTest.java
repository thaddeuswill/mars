package mars.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import mars.TestSims;
import mars.index.sim.Pace;
import mars.index.sim.PlanetSim;
import mars.monitor.view.MonitorView;
import org.junit.jupiter.api.Test;

/**
 * Panel height in the three states the player meets, at GUI scale 3 (240 px) and smaller screens.
 */
class MonitorLayoutTest {
  private static final int H3 = 240;

  private static MonitorView started() throws Exception {
    return MonitorView.of(TestSims.sim(Pace.STANDARD));
  }

  private static MonitorView powered() throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 3);
    s.addMachine(TestSims.BATTERY, 5000L);
    s.addMachine(TestSims.OUTGASSER, 1L);
    s.model().advanceSols(10);
    return MonitorView.of(s);
  }

  /** One Outgasser and a dusty panel with no battery: Short on power, two problem lines. */
  private static MonitorView shortAndDusty() throws Exception {
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    s.addMachine(TestSims.PANEL, 1000L);
    s.setEfficiency(1000L, 0.3);
    s.addMachine(TestSims.OUTGASSER, 1L);
    s.addMachine(TestSims.OUTGASSER, 2L);
    s.addMachine(TestSims.OUTGASSER, 3L);
    s.model().advanceSols(2);
    return MonitorView.of(s);
  }

  /** Two Ovens settled at their balance point (1.6 kPa) with a panel and a battery. */
  private static MonitorView levellingOff() throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 1);
    s.addMachine(TestSims.BATTERY, 5000L);
    s.addMachine(TestSims.OVEN, 10L);
    s.addMachine(TestSims.OVEN, 11L);
    s.model().advanceSols(300);
    return MonitorView.of(s);
  }

  /** The worst case: levelling off, short on power, a dusty panel, the frost line, a battery. */
  private static MonitorView levellingShortAndDusty() throws Exception {
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    s.addMachine(TestSims.PANEL, 1000L);
    s.setEfficiency(1000L, 0.6);
    s.addMachine(TestSims.BATTERY, 5000L);
    s.addMachine(TestSims.OUTGASSER, 1L);
    s.addMachine(TestSims.OVEN, 10L);
    s.addMachine(TestSims.OVEN, 11L);
    s.model().advanceSols(600);
    return MonitorView.of(s);
  }

  @Test
  void theFrostLineAndTheLevellingNoteKeepThePanelNearTheTarget() throws Exception {
    for (var e :
        java.util.Map.of(
                "levelling", levellingOff(), "levelling+short+dusty", levellingShortAndDusty())
            .entrySet()) {
      MonitorView raw = e.getValue();
      boolean note = raw.rows().stream().anyMatch(r -> r.noteKey() != null);
      MonitorView v = MonitorLayout.fit(raw);
      int total = MonitorLayout.panelHeight(v, H3);
      System.out.println(
          e.getKey()
              + ": note="
              + note
              + " plot "
              + MonitorLayout.plotHeight(v, H3)
              + " -> panel "
              + total
              + " GUI px");
      assertTrue(note, e.getKey());
      boolean worst = e.getKey().contains("short");
      if (worst) {
        assertEquals(0, MonitorLayout.plotHeight(v, H3), "rows only when problem lines crowd it");
      } else {
        assertTrue(MonitorLayout.plotHeight(v, H3) >= MonitorStyle.PLOT_MIN_H, e.getKey());
      }
      assertTrue(
          total
              <= MonitorStyle.TARGET_H + MonitorStyle.PLOT_OVERFLOW + MonitorStyle.GOAL_BAR_LINE_H,
          e.getKey() + " is " + total);
    }
  }

  @Test
  void panelFitsTheTargetHeightInEveryState() throws Exception {
    for (var e :
        java.util.Map.of("started", started(), "powered", powered(), "short+dusty", shortAndDusty())
            .entrySet()) {
      MonitorView v = MonitorLayout.fit(e.getValue());
      int fixed = MonitorLayout.fixedHeight(v);
      int plot = MonitorLayout.plotHeight(v, H3);
      int total = MonitorLayout.panelHeight(v, H3);
      System.out.println(
          e.getKey() + ": rows " + fixed + " + plot " + plot + " -> panel " + total + " GUI px");
      assertTrue(plot >= MonitorStyle.PLOT_MIN_H, e.getKey());
      // The goal bar costs GOAL_BAR_LINE_H (4) in every state; nothing else grew.
      assertTrue(
          total <= MonitorStyle.TARGET_H + 6 + MonitorStyle.GOAL_BAR_LINE_H,
          e.getKey() + " is " + total);
      assertTrue(total <= H3 - 2 * MonitorStyle.MARGIN, e.getKey());
    }
  }

  /** The plot is about 8 px taller than the old 17 px, the panel stays at most 158 GUI px. */
  @Test
  void thePlotIsTallEnoughForTheGoalLabelStripAndThePanelStaysUnderTheBound() throws Exception {
    final int h720Scale2 = 360;
    for (MonitorView raw : new MonitorView[] {started(), powered()}) {
      MonitorView v = MonitorLayout.fit(raw);
      for (int screen : new int[] {H3, h720Scale2}) {
        int plot = MonitorLayout.plotHeight(v, screen);
        int total = MonitorLayout.panelHeight(v, screen);
        System.out.println("screen " + screen + ": plot " + plot + " panel " + total);
        assertTrue(plot >= 25, "plot " + plot);
        assertTrue(total <= 158, "panel " + total);
        assertTrue(total <= screen - 2 * MonitorStyle.MARGIN);
      }
    }
  }

  @Test
  void aShortScreenDropsThePlotInsteadOfOverflowing() throws Exception {
    for (MonitorView raw : new MonitorView[] {started(), powered(), shortAndDusty()}) {
      MonitorView v = MonitorLayout.fit(raw);
      for (int h : new int[] {120, 100, 80, 60}) {
        int total = MonitorLayout.panelHeight(v, h);
        int plot = MonitorLayout.plotHeight(v, h);
        System.out.println("screen " + h + " px: plot " + plot + " panel " + total);
        if (plot > 0) {
          assertTrue(total <= h - 2 * MonitorStyle.MARGIN, "panel " + total + " in " + h);
        } else {
          assertEquals(MonitorLayout.fixedHeight(v), total);
        }
      }
    }
  }

  @Test
  void theGoalBarFitsTheSameHeightsInEveryState() throws Exception {
    java.util.Map<String, MonitorView> states = new java.util.LinkedHashMap<>();
    states.put("far", started());
    states.put("powered", powered());
    states.put("short+dusty", shortAndDusty());
    states.put("levelling", levellingOff());
    states.put("levelling+short+dusty", levellingShortAndDusty());
    for (var e : states.entrySet()) {
      MonitorView raw = e.getValue();
      boolean hasBar = raw.rows().stream().anyMatch(r -> r.goal() != null);
      MonitorView v = MonitorLayout.fit(raw);
      int total = MonitorLayout.panelHeight(v, H3);
      System.out.println(
          e.getKey()
              + ": bar="
              + hasBar
              + " plot "
              + MonitorLayout.plotHeight(v, H3)
              + " -> panel "
              + total
              + " GUI px");
      assertTrue(hasBar, e.getKey());
      assertTrue(
          total
              <= MonitorStyle.TARGET_H + MonitorStyle.PLOT_OVERFLOW + MonitorStyle.GOAL_BAR_LINE_H,
          e.getKey() + " is " + total);
      // The bar line itself never drops: only the detail text and the plot give way.
      assertTrue(v.rows().stream().anyMatch(r -> r.goal() != null), e.getKey());
    }
  }

  @Test
  void aGoalRowCountsOneBarLineAndKeepsItsCountOffTheDetailLine() throws Exception {
    MonitorView v = started();
    MonitorView.Row g = v.rows().stream().filter(r -> r.goal() != null).findFirst().orElseThrow();
    int expected =
        MonitorStyle.ROW_H_EMPHASIS + MonitorStyle.GOAL_BAR_LINE_H + MonitorStyle.LINE_H - 1;
    assertEquals(expected, MonitorLayout.rowHeight(g));
    MonitorView fitted = MonitorLayout.fit(v);
    MonitorView.Row f =
        fitted.rows().stream().filter(r -> r.goal() != null).findFirst().orElseThrow();
    assertEquals(g.goal(), f.goal());
    assertEquals(g.detailKey(), f.detailKey(), "room enough: the detail line stays");
    MonitorView tight = MonitorLayout.fit(levellingShortAndDusty());
    MonitorView.Row t =
        tight.rows().stream().filter(r -> r.goal() != null).findFirst().orElseThrow();
    assertEquals(null, t.detailKey(), "the detail line is what gives way when tight");
  }

  /**
   * The pace tag shows only in a classroom world; standard adds no row; the panel stays in bound.
   */
  @Test
  void theClassroomPaceTagAddsOneShortRowOnlyInClassroomAndStaysInBound() throws Exception {
    MonitorView standard = started();
    assertTrue(standard.rows().stream().noneMatch(r -> r.labelKey().equals("monitor.row.pace")));
    PlanetSim cs = TestSims.sim(Pace.classroom(4));
    MonitorView room = MonitorView.of(cs);
    assertEquals(
        1, room.rows().stream().filter(r -> r.labelKey().equals("monitor.row.pace")).count());
    assertEquals(standard.rows().size() + 1, room.rows().size());
    MonitorView v = MonitorLayout.fit(room);
    int total = MonitorLayout.panelHeight(v, H3);
    int plot = MonitorLayout.plotHeight(v, H3);
    System.out.println("classroom: plot " + plot + " -> panel " + total + " GUI px");
    assertTrue(plot >= MonitorStyle.PLOT_MIN_H, "the plot survives the extra row");
    assertTrue(
        total <= MonitorStyle.TARGET_H + 6 + MonitorStyle.GOAL_BAR_LINE_H, "panel is " + total);
  }

  /** Storm row states stay inside the same bound as the other problem lines. */
  @Test
  void theStormRowKeepsThePanelInBoundInEveryStormState() throws Exception {
    java.util.Map<String, MonitorView> states = new java.util.LinkedHashMap<>();
    PlanetSim warn = TestSims.stormSim(Pace.STANDARD, 1);
    TestSims.withPanels(warn, 3);
    warn.addMachine(TestSims.BATTERY, 5000L);
    warn.addMachine(TestSims.OUTGASSER, 1L);
    warn.model().advanceSols(13.5);
    states.put("warning", MonitorView.of(warn));
    warn.model().advanceSols(1.0);
    states.put("active", MonitorView.of(warn));
    warn.model().advanceSols(2.0);
    states.put("over", MonitorView.of(warn));
    PlanetSim worst = TestSims.stormSim(Pace.STANDARD, 1);
    worst.addMachine(TestSims.PANEL, 1000L);
    worst.setEfficiency(1000L, 0.6);
    worst.addMachine(TestSims.BATTERY, 5000L);
    worst.addMachine(TestSims.OUTGASSER, 1L);
    worst.addMachine(TestSims.OVEN, 10L);
    worst.addMachine(TestSims.OVEN, 11L);
    worst.model().advanceSols(14.5); // storm on, levelling off, short, dusty
    states.put("worst: storm+short+dusty+frost", MonitorView.of(worst));
    for (var e : states.entrySet()) {
      MonitorView v = MonitorLayout.fit(e.getValue());
      int total = MonitorLayout.panelHeight(v, H3);
      System.out.println(
          e.getKey()
              + ": plot "
              + MonitorLayout.plotHeight(v, H3)
              + " -> panel "
              + total
              + " GUI px");
      assertTrue(
          e.getValue().rows().stream().anyMatch(r -> r.labelKey().startsWith("monitor.storm")),
          e.getKey() + " has a storm row");
      assertTrue(
          total
              <= MonitorStyle.TARGET_H + MonitorStyle.PLOT_OVERFLOW + MonitorStyle.GOAL_BAR_LINE_H,
          e.getKey() + " is " + total);
      assertTrue(total <= H3 - 2 * MonitorStyle.MARGIN, e.getKey());
    }
  }
}
