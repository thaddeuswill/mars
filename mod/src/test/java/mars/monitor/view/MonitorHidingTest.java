package mars.monitor.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.google.gson.JsonParser;
import java.nio.file.Files;
import java.util.Set;
import mars.TestData;
import mars.TestSims;
import mars.core.Guidance;
import mars.index.sim.Pace;
import mars.index.sim.PlanetSim;
import mars.learning.LineRules;
import org.junit.jupiter.api.Test;

/** CR-T8 on a real Monitor view: the estimate lines are removed per guidance and open forecast. */
class MonitorHidingTest {
  private static LineRules rules() throws Exception {
    return LineRules.parse(
        JsonParser.parseString(Files.readString(TestData.data("monitor/lines.json")))
            .getAsJsonObject(),
        "lines.json");
  }

  private static MonitorView.Row row(MonitorView v, String label) {
    return v.rows().stream().filter(r -> r.labelKey().equals(label)).findFirst().orElseThrow();
  }

  private static MonitorView withEta() throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 3);
    s.model().advanceSols(4);
    s.addMachine(TestSims.OUTGASSER, 1L);
    s.model().advanceSols(10);
    return MonitorView.of(s);
  }

  @Test
  void expertAndOpenPressureForecastRemoveTheEtaButKeepTheBar() throws Exception {
    LineRules r = rules();
    MonitorView v = withEta();
    assertNotNull(row(v, "monitor.row.next_goal").goal().etaKey());
    MonitorView expert = v.hidingLines(k -> r.visible(k, Guidance.EXPERT, Set.<String>of()));
    var g = row(expert, "monitor.row.next_goal").goal();
    assertNull(g.etaKey());
    assertNull(g.etaArg());
    assertEquals(row(v, "monitor.row.next_goal").goal().percent(), g.percent(), "bar stays");
    MonitorView open = v.hidingLines(k -> r.visible(k, Guidance.STANDARD, Set.of("mars:pressure")));
    assertNull(row(open, "monitor.row.next_goal").goal().etaKey());
    // Standard with nothing open: the same object comes back, nothing changed.
    assertSame(v, v.hidingLines(k -> r.visible(k, Guidance.STANDARD, Set.<String>of())));
  }

  @Test
  void anOpenPressureForecastRemovesLevelingOffToo() throws Exception {
    LineRules r = rules();
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 1);
    s.addMachine(TestSims.OVEN, 10L);
    s.addMachine(TestSims.OVEN, 11L);
    s.model().advanceSols(310);
    MonitorView v = MonitorView.of(s);
    assertEquals("monitor.estimate.levelling_off", row(v, "monitor.row.pressure").noteKey());
    assertNotNull(
        row(
                v.hidingLines(k -> r.visible(k, Guidance.EXPERT, Set.<String>of())),
                "monitor.row.pressure")
            .noteKey(),
        "expert alone keeps Leveling off");
    assertNull(
        row(
                v.hidingLines(k -> r.visible(k, Guidance.GENTLE, Set.of("mars:pressure"))),
                "monitor.row.pressure")
            .noteKey());
  }
}
