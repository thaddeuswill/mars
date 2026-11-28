package mars.learning;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import mars.TestData;
import mars.TestSims;
import mars.core.Guidance;
import mars.gates.GateTracker;
import org.junit.jupiter.api.Test;

/** Journal, guidance line rules, open forecasts and the CR-T8 / CR-F9 content scans. */
class LearningTest {
  static final String DOME = "mars:dome_stable";
  static final String PRESSURE = "mars:pressure";

  static LineRules rules() throws Exception {
    return LineRules.parse(
        read(TestData.data("monitor/lines.json")), "data/mars/monitor/lines.json");
  }

  static JsonObject read(Path p) throws Exception {
    return JsonParser.parseString(Files.readString(p, StandardCharsets.UTF_8)).getAsJsonObject();
  }

  static List<JournalEntryDef> entries() throws Exception {
    try (Stream<Path> s = Files.list(TestData.data("story/journal"))) {
      return s.filter(p -> p.toString().endsWith(".json"))
          .sorted()
          .map(
              p -> {
                try {
                  return JournalEntryDef.parse(read(p), p.getFileName().toString());
                } catch (Exception e) {
                  throw new IllegalStateException(e);
                }
              })
          .toList();
    }
  }

  // ---- 2. LineRules

  @Test
  void etaLineIsHiddenUnderExpertOnly() throws Exception {
    LineRules r = rules();
    assertFalse(r.visible("monitor.goal.eta", Guidance.EXPERT, Set.of()));
    assertTrue(r.visible("monitor.goal.eta", Guidance.GENTLE, Set.of()));
    assertTrue(r.visible("monitor.goal.eta", Guidance.STANDARD, Set.of()));
    assertFalse(r.visible("monitor.goal.eta_soon", Guidance.EXPERT, Set.of()));
  }

  @Test
  void anOpenPressureForecastHidesEveryLineThatRevealsPressure() throws Exception {
    LineRules r = rules();
    Set<String> open = Set.of(PRESSURE);
    for (Guidance g : Guidance.values()) {
      assertFalse(r.visible("monitor.goal.eta", g, open), "eta under " + g);
      assertFalse(r.visible("monitor.estimate.levelling_off", g, open), "levelling under " + g);
    }
    // Leveling off is not an ETA: expert alone does not hide it.
    assertTrue(r.visible("monitor.estimate.levelling_off", Guidance.EXPERT, Set.of()));
    // An unrelated forecast hides nothing.
    assertTrue(
        r.visible("monitor.estimate.levelling_off", Guidance.STANDARD, Set.of("mars:temperature")));
    // Undeclared lines are always shown.
    assertTrue(r.visible("monitor.goal.not_rising", Guidance.EXPERT, open));
  }

  @Test
  void openForecastsArePerPlayerAndCloseRestores() {
    OpenForecasts f = new OpenForecasts();
    UUID a = UUID.randomUUID();
    UUID b = UUID.randomUUID();
    f.open(a, PRESSURE);
    assertEquals(Set.of(PRESSURE), f.askedBy(a));
    assertTrue(f.askedBy(b).isEmpty());
    f.close(a, PRESSURE);
    assertTrue(f.askedBy(a).isEmpty());
  }

  // ---- 3. Journal

  @Test
  void journalIsAFunctionOfLatchedGates() throws Exception {
    List<JournalEntryDef> defs = entries();
    var gates = new GateTracker(TestSims.gates(), false);
    assertTrue(Journal.unlocked(defs, gates::isLatched).isEmpty(), "nothing before the latch");
    gates.latch(DOME, GateTracker.Cause.ADMIN, 10);
    List<String> ids =
        Journal.unlocked(defs, gates::isLatched).stream().map(JournalEntryDef::id).toList();
    assertEquals(
        List.of(
            "mars:journal/airmix",
            "mars:journal/on_earth",
            "mars:journal/sky_stylized",
            "mars:journal/band_made_up"),
        ids);
    // Lock-in: nothing a later pressure drop does can un-latch it, so the list is unchanged.
    assertEquals(ids.size(), Journal.unlocked(defs, gates::isLatched).size());
    // Two players, and a player who joins later, get the same answer from the same latched set.
    assertEquals(
        Journal.unlocked(defs, gates::isLatched), Journal.unlocked(defs, gates::isLatched));
    assertEquals(4, Journal.unlockedBy(defs, DOME).size());
    assertTrue(Journal.unlockedBy(defs, "mars:phase_2_begins").isEmpty());
  }

  @Test
  void pointerAndUnreadAreTrackedPerPlayerSoNobodyIsToldTwice() throws Exception {
    List<JournalEntryDef> defs = entries();
    PlayerSettings s = new PlayerSettings();
    UUID p = UUID.randomUUID();
    assertEquals(4, s.unannounced(p, defs).size());
    s.markNotified(p, s.unannounced(p, defs));
    assertTrue(s.unannounced(p, defs).isEmpty(), "second login: no second pointer");
    assertEquals(4, s.unread(p, defs).size(), "still unread until opened");
    s.markSeen(p, defs.get(0).id());
    assertEquals(3, s.unread(p, defs).size());
    assertEquals(4, s.unannounced(UUID.randomUUID(), defs).size(), "another player is still told");
  }

  // ---- 4. Journal content

  @Test
  void everyJournalEntryIsCompleteAndShort() throws Exception {
    JsonObject lang = read(TestData.pack().resolve("assets/mars/lang/en_us.json"));
    Set<String> gateIds = new HashSet<>();
    TestSims.gates().forEach(g -> gateIds.add(g.id()));
    Set<String> ids = new HashSet<>();
    Set<String> orders = new HashSet<>();
    for (JournalEntryDef d : entries()) {
      assertTrue(ids.add(d.id()), "duplicate id " + d.id());
      assertTrue(orders.add(d.gate() + "/" + d.order()), "duplicate order for " + d.id());
      assertTrue(gateIds.contains(d.gate()), d.id() + " names a gate that does not exist");
      assertTrue(lang.has(d.titleKey()), "missing " + d.titleKey());
      assertTrue(lang.has(d.textKey() + ".ms"), "missing .ms text for " + d.id());
      for (String band : List.of(".ms")) { // the .hs read-more is optional and not budgeted
        String key = lang.has(d.textKey() + band) ? d.textKey() + band : d.textKey() + ".ms";
        int words = lang.get(key).getAsString().trim().split("\\s+").length;
        // 45 words is invented (CR-T7 budget for journal entries, game designer proposal).
        assertTrue(words <= 45, key + " has " + words + " words, budget is 45");
      }
    }
    assertEquals(4, ids.size());
  }

  @Test
  void theOnEarthSentenceIsMarkedAwaitingSmeConfirmation() throws Exception {
    var onEarth =
        entries().stream().filter(d -> d.id().equals("mars:journal/on_earth")).findFirst().get();
    assertTrue(onEarth.status().contains("awaiting SME"));
  }

  // ---- 5. CR-T8 scan

  @Test
  void everyEstimateKeyInTheLanguageFileIsDeclaredWithReveals() throws Exception {
    JsonObject lang = read(TestData.pack().resolve("assets/mars/lang/en_us.json"));
    LineRules r = rules();
    int checked = 0;
    for (String key : lang.keySet()) {
      if (key.startsWith("monitor.goal.eta") || key.startsWith("monitor.estimate.")) {
        checked++;
        assertTrue(r.declared(key), key + " states an estimate but is not in monitor/lines.json");
        assertFalse(
            r.lines().get(key).reveals().isEmpty(), key + " is declared with no reveals (CR-T8)");
      }
    }
    assertTrue(checked >= 5, "expected the five known estimate keys, found " + checked);
    for (String declared : r.lines().keySet()) {
      assertTrue(lang.has(declared), "lines.json declares " + declared + " which has no text");
    }
  }

  @Test
  void anUndeclaredEstimateKeyWouldFailTheScan() throws Exception {
    // Proves the scan can fail: a made-up estimate key is not declared.
    assertFalse(rules().declared("monitor.estimate.some_new_forecast"));
  }

  // ---- 6. CR-F9 scan (vacuous for forecasts until they exist; the field must be present)

  @Test
  void everyJournalEntryDeclaresRevealsAndNoEntryRevealsAnOpenForecastValue() throws Exception {
    for (JournalEntryDef d : entries()) {
      assertTrue(d.reveals() != null, d.id() + " must declare reveals (CR-F9)");
      assertTrue(d.reveals().isEmpty(), d.id() + " reveals " + d.reveals() + " (none expected)");
    }
  }
}
