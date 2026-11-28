package mars.monitor.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import mars.power.MachineLook;
import mars.power.PowerGrid;
import org.junit.jupiter.api.Test;

/** The Monitor words and the block look come from one function (MachineLook). */
class PowerReasonTest {
  @Test
  void monitorWordsFollowTheSameLookAsTheBlockState() {
    for (PowerGrid.State s : PowerGrid.State.values()) {
      String word = MonitorView.powerReason(s, "monitor.cause.running");
      switch (MachineLook.of(s)) {
        case OFF -> assertEquals("monitor.cause.needs_power", word);
        case IDLE -> assertEquals("monitor.cause.low_power", word);
        case RUNNING -> assertEquals("monitor.cause.running", word);
      }
    }
    assertNull(MonitorView.powerReason(PowerGrid.State.POWERED, null));
  }
}
