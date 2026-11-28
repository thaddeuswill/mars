package mars.power;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MachineLookTest {
  @Test
  void everyGridStateMapsToOneLook() {
    assertEquals(MachineLook.OFF, MachineLook.of(PowerGrid.State.UNPOWERED));
    assertEquals(MachineLook.IDLE, MachineLook.of(PowerGrid.State.SHORT));
    assertEquals(MachineLook.RUNNING, MachineLook.of(PowerGrid.State.POWERED));
    assertEquals(MachineLook.RUNNING, MachineLook.of(PowerGrid.State.IDLE));
  }

  @Test
  void looksFollowARealGridThroughItsStates() {
    PowerGrid g = new PowerGrid();
    g.setTotals(0, 4, 10);
    assertEquals(MachineLook.OFF, MachineLook.of(g.state(0.05)));
    g.setTotals(2, 4, 10);
    assertEquals(MachineLook.IDLE, MachineLook.of(g.state(0.05)));
    g.setTotals(5, 4, 10);
    assertEquals(MachineLook.RUNNING, MachineLook.of(g.state(0.05)));
  }

  @Test
  void batteryBarsAreQuartersRoundedDown() {
    assertEquals(0, MachineLook.batteryBars(0, 10));
    assertEquals(0, MachineLook.batteryBars(2.4, 10));
    assertEquals(1, MachineLook.batteryBars(2.5, 10));
    assertEquals(1, MachineLook.batteryBars(4.99, 10));
    assertEquals(2, MachineLook.batteryBars(5, 10));
    assertEquals(2, MachineLook.batteryBars(7.49, 10));
    assertEquals(3, MachineLook.batteryBars(7.5, 10));
    assertEquals(3, MachineLook.batteryBars(10, 10));
    assertEquals(3, MachineLook.batteryBars(12, 10), "over capacity is clamped");
  }

  @Test
  void batteryWithNoCapacityOrBadNumbersIsZero() {
    assertEquals(0, MachineLook.batteryBars(5, 0));
    assertEquals(0, MachineLook.batteryBars(-1, 10));
    assertEquals(0, MachineLook.batteryBars(Double.NaN, 10));
    assertEquals(0, MachineLook.batteryBars(1, Double.NaN));
  }
}
