package mars.monitor.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import mars.TestSims;
import mars.gates.Condition;
import mars.index.sim.Pace;
import org.junit.jupiter.api.Test;

class PlotScaleTest {
  @Test
  void axisTopIsANiceValueAboveTheData() {
    assertEquals(1, PlotScale.niceMax(0.6), 0);
    assertEquals(2, PlotScale.niceMax(1.6), 0);
    assertEquals(10, PlotScale.niceMax(5.0), 0, "5.0 must not sit on the top edge");
    assertEquals(5, PlotScale.niceMax(4.0), 0);
    assertEquals(100, PlotScale.niceMax(75), 0);
    assertEquals(1, PlotScale.niceMax(0), 0);
  }

  @Test
  void phaseTopStaysFixedUntilSamplesNeedMoreRoom() {
    assertEquals(6, PlotScale.topFor(5, 4.7), 1e-9);
    assertEquals(6, PlotScale.topFor(5, 5.0), 1e-9);
    assertEquals(36, PlotScale.topFor(30, 31), 1e-9);
    assertEquals(90, PlotScale.topFor(75, 75), 1e-9);
    assertEquals(50, PlotScale.topFor(30, 37), 1e-9);
  }

  @Test
  void plotGoalUsesTheSamePressureThresholdForBothPaces() throws Exception {
    var expected =
        TestSims.gates().stream()
            .filter(gate -> gate.id().equals("mars:phase_2_begins"))
            .findFirst()
            .orElseThrow();
    double threshold = ((Condition.Compare) expected.condition()).value();
    assertEquals(threshold, MonitorView.historyOf(TestSims.sim(Pace.STANDARD)).goalLine().value());
    assertEquals(
        threshold, MonitorView.historyOf(TestSims.sim(Pace.classroom(4))).goalLine().value());
  }

  @Test
  void pixelsStayInsideThePlot() {
    assertEquals(0, PlotScale.x(5, 5, 15, 100));
    assertEquals(99, PlotScale.x(15, 5, 15, 100));
    assertEquals(99, PlotScale.x(99, 5, 15, 100));
    assertEquals(0, PlotScale.y(10, 10, 30));
    assertEquals(29, PlotScale.y(0, 10, 30));
    assertEquals(29, PlotScale.y(-3, 10, 30));
  }
}
