package mars.monitor.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import mars.TestData;
import mars.TestSims;
import mars.index.sim.Pace;
import mars.index.sim.PlanetSim;
import org.junit.jupiter.api.Test;

/**
 * The starved-machine alert (BACKLOG #49): rare, one line, only for machines that feed the Index.
 */
class StarvedAlertTest {
  private static long alerts(MonitorView v) {
    return v.rows().stream().filter(MonitorView::isAlert).count();
  }

  private static MonitorView.Row alert(MonitorView v) {
    return v.rows().stream().filter(MonitorView::isAlert).findFirst().orElseThrow();
  }

  @Test
  void noAlertWhenNothingIsPlacedOrEverythingRuns() throws Exception {
    assertEquals(0, alerts(MonitorView.of(TestSims.sim(Pace.STANDARD))));
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 3);
    s.addMachine(TestSims.OUTGASSER, 1L);
    s.addMachine(TestSims.OVEN, 2L);
    assertEquals(PlanetSimState.POWERED, PlanetSimState.of(s));
    assertEquals(0, alerts(MonitorView.of(s)), "everything runs: no alert");
  }

  @Test
  void noPowerGivesOneAlertLineThatIsTheFirstRow() throws Exception {
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    s.addMachine(TestSims.OUTGASSER, 1L);
    s.addMachine(TestSims.OUTGASSER, 2L);
    MonitorView v = MonitorView.of(s);
    assertEquals(1, alerts(v));
    assertTrue(MonitorView.isAlert(v.rows().get(0)), "the alert is the first row");
    MonitorView.Row a = alert(v);
    assertEquals(MonitorView.Tone.ALERT, a.tone());
    assertEquals(1, a.causes().size());
    assertEquals(2, a.causes().get(0).count());
    assertEquals("monitor.cause.needs_power", a.causes().get(0).reasonKey());
    assertEquals("monitor.alert.starved.many", MonitorView.alertKey(a.causes()));
  }

  @Test
  void aShareOfPowerIsAlsoStarvedAndSaysLowPower() throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 1);
    s.setEfficiency(1000L, 0.6);
    s.addMachine(TestSims.OUTGASSER, 1L);
    s.addMachine(TestSims.OUTGASSER, 2L);
    MonitorView.Row a = alert(MonitorView.of(s));
    assertEquals("monitor.cause.low_power", a.causes().get(0).reasonKey());
  }

  @Test
  void manyMachinesStillGiveOneLine() throws Exception {
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    for (long i = 0; i < 12; i++) {
      s.addMachine(i % 2 == 0 ? TestSims.OUTGASSER : TestSims.OVEN, i + 1);
    }
    MonitorView v = MonitorView.of(s);
    assertEquals(1, alerts(v));
    MonitorView.Row a = alert(v);
    assertEquals("monitor.alert.starved.mixed", MonitorView.alertKey(a.causes()));
    assertEquals(12, MonitorView.alertTotal(a.causes()));
    s.addMachine(TestSims.OUTGASSER, 99L);
    assertEquals(1, alerts(MonitorView.of(s)));
  }

  @Test
  void aLoadThatChangesNoIndexRowIsNotAlerted() throws Exception {
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    s.addMachine("mars:water_reclaimer", 1L);
    MonitorView v = MonitorView.of(s);
    assertEquals(0, alerts(v), "the reclaimer feeds no Index row; the Power row says needs power");
    assertEquals(
        "monitor.power.unpowered",
        v.rows().stream()
            .filter(r -> r.labelKey().equals("monitor.row.power"))
            .findFirst()
            .orElseThrow()
            .valueKey());
  }

  @Test
  void theAlertTextIsInLangShortAndPlain() throws Exception {
    JsonObject l =
        JsonParser.parseString(
                Files.readString(
                    TestData.pack().resolve("assets/mars/lang/en_us.json"), StandardCharsets.UTF_8))
            .getAsJsonObject();
    for (String k :
        new String[] {
          "monitor.alert.starved.one", "monitor.alert.starved.many", "monitor.alert.starved.mixed"
        }) {
      String t = l.get(k).getAsString();
      assertFalse(t.contains("~") || t.contains("<"), k + " CR-T7b");
      assertTrue(t.split("\\s+").length <= 10, k + " is short");
      assertTrue(t.contains("starved"), k);
    }
    assertTrue(l.has("monitor.row.alert"));
  }

  /** Tiny helper so the test reads the grid state without a new public API. */
  private enum PlanetSimState {
    POWERED;

    static PlanetSimState of(PlanetSim s) {
      return s.power().state(s.stepSols()) == mars.power.PowerGrid.State.POWERED ? POWERED : null;
    }
  }
}
