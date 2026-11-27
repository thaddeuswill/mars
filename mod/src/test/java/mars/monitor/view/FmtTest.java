package mars.monitor.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class FmtTest {
  @Test
  void thousandsSeparatorsTrueMinusAndUnits() {
    assertEquals("1,234.50", Fmt.number(1234.5, 2));
    assertEquals("−20", Fmt.number(-20, 0));
    assertEquals("0.00", Fmt.number(-0.0001, 2), "a minus sign on nothing is dropped");
    assertEquals("+0.10", Fmt.signed(0.1, 2));
    assertEquals("−0.10", Fmt.signed(-0.1, 2));
    assertEquals("0.00", Fmt.signed(0, 2));
    assertEquals("75.0 kPa", Fmt.withUnit(75, 1, "kPa"));
  }
}
