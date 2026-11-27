package mars.monitor.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
  void pixelsStayInsideThePlot() {
    assertEquals(0, PlotScale.x(5, 5, 15, 100));
    assertEquals(99, PlotScale.x(15, 5, 15, 100));
    assertEquals(99, PlotScale.x(99, 5, 15, 100));
    assertEquals(0, PlotScale.y(10, 10, 30));
    assertEquals(29, PlotScale.y(0, 10, 30));
    assertEquals(29, PlotScale.y(-3, 10, 30));
  }
}
