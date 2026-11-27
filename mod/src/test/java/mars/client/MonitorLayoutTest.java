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
      assertTrue(total <= MonitorStyle.TARGET_H + 6, e.getKey() + " is " + total);
      assertTrue(total <= H3 - 2 * MonitorStyle.MARGIN, e.getKey());
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
}
