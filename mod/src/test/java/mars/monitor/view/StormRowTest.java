package mars.monitor.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;
import mars.TestData;
import mars.TestSims;
import mars.core.Guidance;
import mars.index.sim.Pace;
import mars.index.sim.PlanetSim;
import mars.learning.LineRules;
import org.junit.jupiter.api.Test;

/** The storm row and its wording: stages, CR-F9 and CR-T7 checks, CR-T8 line hiding. */
class StormRowTest {
  private static JsonObject lang() throws Exception {
    return JsonParser.parseString(
            Files.readString(
                TestData.pack().resolve("assets/mars/lang/en_us.json"), StandardCharsets.UTF_8))
        .getAsJsonObject();
  }

  private static String text(String key) throws Exception {
    return lang().get(key).getAsString();
  }

  private static MonitorView.Row storm(MonitorView v) {
    return v.rows().stream()
        .filter(r -> r.labelKey().startsWith("monitor.storm"))
        .findFirst()
        .orElse(null);
  }

  @Test
  void theRowFollowsTheStormThroughItsStages() throws Exception {
    PlanetSim s = TestSims.stormSim(Pace.STANDARD, 1);
    TestSims.withPanels(s, 1);
    assertEquals(null, storm(MonitorView.of(s)), "nothing before the first warning");
    s.model().advanceSols(13.5);
    var warn = storm(MonitorView.of(s));
    assertEquals("monitor.storm.label.warn", warn.labelKey());
    assertEquals("monitor.storm.value.warn", warn.valueKey());
    assertEquals("10 min", warn.value()); // half a sol to go
    assertTrue(warn.causes().isEmpty(), "the warning says nothing about sunlight");
    s.model().advanceSols(1.0); // sol 14.5
    var on = storm(MonitorView.of(s));
    assertEquals("monitor.storm.label.active", on.labelKey());
    assertEquals("1.5 sols", on.value());
    assertEquals(1, on.causes().size());
    assertEquals("monitor.cause.storm_sunlight", on.causes().get(0).sourceKey());
    assertEquals("\u221260\u00a0%", on.causes().get(0).rate());
    s.model().advanceSols(2.0); // sol 16.5, half a sol after the end
    var over = storm(MonitorView.of(s));
    assertEquals("monitor.storm.label.over", over.labelKey());
    assertEquals("monitor.storm.over.detail", over.detailKey());
    s.model().advanceSols(1.0); // sol 17.5: past the window
    var clear = storm(MonitorView.of(s));
    assertEquals("monitor.storm.label.clear", clear.labelKey());
    assertEquals(null, clear.detailKey());
  }

  @Test
  void aPlanetWithNoStormScheduleHasNoStormRow() throws Exception {
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    s.model().advanceSols(15);
    assertEquals(null, storm(MonitorView.of(s)));
  }

  @Test
  void theSunlightLineAndThePanelPointerAreHeldBackWhileSolarPowerIsAsked() throws Exception {
    LineRules rules =
        LineRules.parse(
            JsonParser.parseString(
                    Files.readString(TestData.data("monitor/lines.json"), StandardCharsets.UTF_8))
                .getAsJsonObject(),
            "lines.json");
    PlanetSim s = TestSims.stormSim(Pace.STANDARD, 1);
    TestSims.withPanels(s, 1);
    s.model().advanceSols(14.5);
    MonitorView on = MonitorView.of(s);
    Set<String> asked = Set.of("mars:solar_power");
    MonitorView hidden = on.hidingLines(k -> rules.visible(k, Guidance.STANDARD, asked));
    assertTrue(storm(hidden).causes().isEmpty(), "cause line held while the forecast is open");
    assertEquals(storm(on).valueKey(), storm(hidden).valueKey(), "the row itself stays");
    MonitorView shown = on.hidingLines(k -> rules.visible(k, Guidance.STANDARD, Set.of()));
    assertEquals(1, storm(shown).causes().size());
    s.model().advanceSols(2.0);
    MonitorView over = MonitorView.of(s);
    assertEquals(
        null, storm(over.hidingLines(k -> rules.visible(k, Guidance.STANDARD, asked))).detailKey());
    assertEquals(
        "monitor.storm.over.detail",
        storm(over.hidingLines(k -> rules.visible(k, Guidance.STANDARD, Set.of()))).detailKey());
  }

  /** CR-F9: what is delivered before onset never names power, sunlight, panels or dimming. */
  @Test
  void theWarningTextsDoNotGiveAwayTheForecast() throws Exception {
    List<String> before =
        List.of(
            "monitor.storm.label.warn",
            "monitor.storm.value.warn",
            "log.storm.warn",
            "log.storm.onset");
    for (String k : before) {
      String t = text(k).toLowerCase(java.util.Locale.ROOT);
      for (String banned : List.of("power", "solar", "sunlight", "panel", "dim", "less")) {
        assertFalse(t.contains(banned), k + " names '" + banned + "' (CR-F9)");
      }
    }
  }

  /** CR-T7: label limits and one-shot line length. */
  @Test
  void theStormWordsKeepToTheContentRules() throws Exception {
    for (String k :
        List.of(
            "monitor.storm.label.warn",
            "monitor.storm.label.active",
            "monitor.storm.label.over",
            "monitor.storm.label.clear")) {
      assertTrue(text(k).length() <= 24, k + " is over 24 characters (CR-T7c)");
    }
    String warn = text("log.storm.warn").replace("%s", "2 sols");
    for (String k : List.of("log.storm.onset", "log.storm.end")) {
      assertTrue(text(k).split("\\s+").length <= 20, k);
    }
    assertTrue(warn.split("\\s+").length <= 20, warn);
    assertEquals(
        "Dust storm forming. It reaches the dome in about 1 sol. It should last about 2 sols.",
        text("log.storm.warn").replaceFirst("%s", "1 sol").replaceFirst("%s", "2 sols"));
  }

  @Test
  void durationWordsAreShortAndPlural() {
    assertEquals("1 sol", Fmt.duration(1.0, 20));
    assertEquals("2 sols", Fmt.duration(2.0, 20));
    assertEquals("1.5 sols", Fmt.duration(1.5, 20));
    assertEquals("5 min", Fmt.duration(0.25, 20));
    assertEquals("10 min", Fmt.duration(0.5, 20));
    assertEquals("1 min", Fmt.duration(0.001, 20));
  }
}
