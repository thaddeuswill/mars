package mars.power;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** The pure grid: supply first, then the battery, surplus charges it; nothing is created. */
class PowerGridTest {
  @Test
  void surplusChargesTheBatteryUpToItsCapacity() {
    PowerGrid g = new PowerGrid();
    g.setTotals(5, 4, 10);
    for (int i = 0; i < 4; i++) {
      assertEquals(1.0, g.step(1.0), 1e-12);
    }
    System.out.println("charge after 4 sols of +1 spare: " + g.charge());
    assertEquals(4.0, g.charge(), 1e-12);
    for (int i = 0; i < 20; i++) {
      g.step(1.0);
    }
    assertEquals(10.0, g.charge(), 1e-12, "charge never passes capacity");
  }

  @Test
  void shortfallIsSharedAndTheBatteryCoversWhatItCan() {
    PowerGrid g = new PowerGrid();
    g.setTotals(5, 8, 0);
    assertEquals(0.625, g.step(1.0), 1e-12);
    g.setTotals(5, 8, 10);
    g.setCharge(2);
    // need 8, make 5, battery has 2 of the missing 3: 7 of 8 met, battery empty.
    double sat = g.step(1.0);
    System.out.println(
        "short with a 2-unit battery: satisfaction " + sat + " charge " + g.charge());
    assertEquals(0.875, sat, 1e-12);
    assertEquals(0.0, g.charge(), 1e-12);
  }

  @Test
  void noSupplyNoBatteryMeansUnpowered() {
    PowerGrid g = new PowerGrid();
    g.setTotals(0, 4, 0);
    assertEquals(0.0, g.step(0.5), 1e-12);
    assertEquals(PowerGrid.State.UNPOWERED, g.state(0.5));
    g.setTotals(0, 0, 0);
    assertEquals(PowerGrid.State.IDLE, g.state(0.5));
  }

  @Test
  void previewChangesNothing() {
    PowerGrid g = new PowerGrid();
    g.setTotals(1, 4, 10);
    g.setCharge(6);
    g.preview(1.0);
    assertEquals(6.0, g.charge(), 1e-12);
  }

  @Test
  void anOutgasserAndTwoOvensOnOneCleanPanelGetEightyThreePercent() {
    // Balance sheet 2c decision 1: demand 4 + 1 + 1 = 6, supply 5, equal share: 5/6.
    PowerGrid g = new PowerGrid();
    g.setTotals(5, 4 + 1 + 1, 0);
    double sat = g.step(1.0);
    System.out.println(
        "Outgasser + 2 Ovens, 1 clean panel: share " + sat + " (6 demand, 5 supply)");
    assertEquals(5.0 / 6.0, sat, 1e-12);
    assertEquals(PowerGrid.State.SHORT, g.state(1.0));
    // Total pressure effect: (0.10 + 2 x 0.01) x 5/6 = 0.100 kPa per sol, not 0.12.
    assertEquals(0.100, (0.10 + 2 * 0.01) * sat, 1e-12);
  }
}
