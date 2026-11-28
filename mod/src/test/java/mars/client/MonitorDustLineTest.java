package mars.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import mars.TestSims;
import mars.index.sim.Pace;
import mars.index.sim.PlanetSim;
import mars.monitor.view.MonitorView;
import org.junit.jupiter.api.Test;

/** The "Panel dust -n %" cause line (slice 5 step C, f1-storm-spec.md 3.1). */
class MonitorDustLineTest {
  private static final int H3 = 240;

  private static String line(PlanetSim s) {
    for (var r : MonitorView.of(s).rows()) {
      for (var c : r.causes()) {
        if (c.sourceKey().equals(MonitorView.DUST_LINE)) {
          return c.rate();
        }
      }
    }
    return null;
  }

  private static PlanetSim oneAt(int stage, boolean partly) throws Exception {
    PlanetSim s = TestSims.hazardSim(Pace.STANDARD, 5);
    s.addMachine(TestSims.PANEL, 1000L);
    s.setPanelDust(1000L, stage, partly, 1.0);
    return s;
  }

  @Test
  void thePercentIsOneMinusTheStageFactor() throws Exception {
    assertEquals(null, line(oneAt(0, false)), "a clean panel has no line");
    int[] want = {0, 8, 18, 30, 40};
    for (int stage = 1; stage <= 4; stage++) {
      assertEquals("−" + want[stage] + " %", line(oneAt(stage, false)), "stage " + stage);
    }
    assertEquals("−9 %", line(oneAt(2, true)), "stage 2 partly cleaned: 82 -> 91 %");
    assertEquals("−15 %", line(oneAt(3, true)), "stage 3 partly cleaned: 70 -> 85 %");
    assertEquals("−20 %", line(oneAt(4, true)), "stage 4 partly cleaned: 60 -> 80 %");
  }

  @Test
  void theLineIsTheAverageOverPanels() throws Exception {
    PlanetSim s = oneAt(4, false);
    s.addMachine(TestSims.PANEL, 1001L); // stage 0
    assertEquals("−20 %", line(s));
  }

  @Test
  void theLineIsALossLineWithAShapeAndNoReason() throws Exception {
    PlanetSim s = oneAt(3, false);
    var power =
        MonitorView.of(s).rows().stream()
            .filter(r -> r.labelKey().equals("monitor.row.power"))
            .findFirst()
            .orElseThrow();
    var c = power.causes().get(0);
    assertEquals(-1, c.direction(), "down shape, not colour alone");
    assertEquals(null, c.reasonKey());
    assertTrue(c.isPlanetSide());
    assertEquals(1, MonitorLayout.shownCauses(power).size());
  }

  @Test
  void theServerCanHoldTheLineAndItComesBack() throws Exception {
    MonitorView v = MonitorView.of(oneAt(2, false));
    assertTrue(v.hasCause(MonitorView.DUST_LINE));
    MonitorView held = v.withoutCause(MonitorView.DUST_LINE);
    assertFalse(held.hasCause(MonitorView.DUST_LINE));
    assertEquals(v.rows().size(), held.rows().size());
  }

  /** The worst case: levelling off, short on power, dust, frost line, battery. */
  private static PlanetSim worst() throws Exception {
    PlanetSim s = TestSims.hazardSim(Pace.STANDARD, 5);
    s.addMachine(TestSims.PANEL, 1000L);
    s.setPanelDust(1000L, 4, false, 0.6);
    s.addMachine(TestSims.BATTERY, 5000L);
    s.addMachine(TestSims.OUTGASSER, 1L);
    s.addMachine(TestSims.OVEN, 10L);
    s.addMachine(TestSims.OVEN, 11L);
    s.model().advanceSols(0.5); // before the dust has changed anything else
    return s;
  }

  @Test
  void thePlotDropsBeforeTheDustLineAndTheHeightFits() throws Exception {
    MonitorView raw = MonitorView.of(worst());
    assertTrue(raw.hasCause(MonitorView.DUST_LINE));
    MonitorView fit = MonitorLayout.fit(raw);
    assertTrue(fit.hasCause(MonitorView.DUST_LINE), "a problem line never drops");
    int plot = MonitorLayout.plotHeight(fit, H3);
    int total = MonitorLayout.panelHeight(fit, H3);
    System.out.println(
        "dust worst case: rows "
            + MonitorLayout.fixedHeight(fit)
            + " plot "
            + plot
            + " panel "
            + total);
    assertTrue(total <= H3 - 2 * MonitorStyle.MARGIN, "fits GUI scale 3: " + total);
    // With the line held (Landing not over) the panel is shorter by exactly one line.
    int without =
        MonitorLayout.fixedHeight(MonitorLayout.fit(raw.withoutCause(MonitorView.DUST_LINE)));
    assertTrue(MonitorLayout.fixedHeight(fit) >= without + MonitorStyle.LINE_H || plot == 0);
  }
}
