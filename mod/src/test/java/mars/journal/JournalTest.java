package mars.journal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import mars.TestData;
import org.junit.jupiter.api.Test;

/** The shipped Phase 1 entries and the pure journal logic (landing-spec 5). */
class JournalTest {
  private static final Map<String, String> PARAMS = Map.of("hold_sols", "3", "grace_minutes", "24");

  static Journal shipped() throws Exception {
    List<JournalEntry> out = new ArrayList<>();
    try (Stream<Path> s = Files.list(TestData.data("journal"))) {
      for (Path p : s.filter(f -> f.toString().endsWith(".json")).sorted().toList()) {
        try (var r = Files.newBufferedReader(p)) {
          out.add(JournalEntry.parse(new Gson().fromJson(r, JsonObject.class), p.toString()));
        }
      }
    }
    return new Journal(out);
  }

  private static String all(JournalEntry e) {
    return String.join(
        " ",
        e.title(),
        e.text(),
        e.earthLine() == null ? "" : e.earthLine(),
        e.fictionNote() == null ? "" : e.fictionNote(),
        e.hsMore() == null ? "" : e.hsMore());
  }

  @Test
  void theEightPhaseOneEntriesShipWithUniqueIds() throws Exception {
    Journal j = shipped();
    assertEquals(8, j.entries().size());
    assertEquals(8, j.entries().stream().map(JournalEntry::id).distinct().count());
    assertNotNull(j.get("briefing.shift1"));
    assertNotNull(j.get("journal.dome.stable"));
  }

  @Test
  void triggersMatchTheSpec() throws Exception {
    Journal j = shipped();
    assertEquals(List.of("journal.about"), j.fired("join").stream().map(JournalEntry::id).toList());
    assertEquals(
        List.of("journal.a1.pressure"),
        j.fired("step:MONITOR").stream().map(JournalEntry::id).toList());
    assertEquals(
        List.of("journal.dome.stable", "journal.sky.stylized"),
        j.fired("gate:mars:dome_stable").stream().map(JournalEntry::id).sorted().toList());
    assertEquals(
        List.of("journal.sky.stylized"),
        j.fired("placed:mars:regolith_outgasser").stream().map(JournalEntry::id).toList());
    assertTrue(j.get("journal.dome.stable").planetScope());
    assertFalse(j.get("journal.power").planetScope());
  }

  @Test
  void textsKeepToTheCapsAndBanTheSymbolsAndLeaveNoPlaceholder() throws Exception {
    for (JournalEntry e : shipped().entries()) {
      String main = Journal.fill(e.text(), PARAMS);
      int words =
          (main + " " + (e.earthLine() == null ? "" : e.earthLine())).trim().split("\\s+").length;
      assertTrue(words <= 80, e.id() + " has " + words + " words (cap 80)");
      String text = Journal.fill(all(e), PARAMS);
      assertFalse(text.contains("~") || text.contains("<"), e.id());
      assertFalse(text.contains("%"), e.id() + " uses a % sign (write percent)");
    }
  }

  @Test
  void theBriefingIs38WordsAndTheSkyTextIsTheSmesSentenceByteForByte() throws Exception {
    Journal j = shipped();
    String brief = Journal.fill(j.get("briefing.shift1").text(), PARAMS);
    assertEquals(38, brief.split("\\s+").length);
    assertTrue(brief.contains("keep some power in reserve"));
    assertEquals(
        "More air scatters more blue light, so the sky slowly loses its dusty tan. Nobody has"
            + " seen a thickened Mars sky. These in-between colors are our best guess, made"
            + " stronger so you can see them.",
        j.get("journal.sky.stylized").text());
  }

  /** CR-F9 with journal copies: nothing readable before F1 locks may state F1's answer. */
  @Test
  void noEntryGivesAwayTheKeyedAnswerOfForecastF1() throws Exception {
    Pattern f1 =
        Pattern.compile(
            "(storm|dust)[^.]*(cut|cuts|drop|drops|lower|lowers|reduce|reduces|block|blocks)[^.]*(solar|sunlight|power)",
            Pattern.CASE_INSENSITIVE);
    for (JournalEntry e : shipped().entries()) {
      boolean declared = e.reveals().contains("forecast.f1.storm_power");
      assertEquals(
          declared,
          f1.matcher(Journal.fill(all(e), PARAMS)).find(),
          e.id() + ": F1 answer wording must match its declared reveals");
    }
    // The pattern can fail: the removed sentence is caught.
    assertTrue(f1.matcher("Dust storms cut solar power, so store some.").find());
  }

  @Test
  void fillRefusesAnUnfilledPlaceholder() {
    assertEquals("a 3 b", Journal.fill("a {x} b", Map.of("x", "3")));
    assertThrows(IllegalStateException.class, () -> Journal.fill("a {y} b", Map.of("x", "3")));
  }

  @Test
  void theBookHasAContentsPageWithJumpsAndNewMarks() throws Exception {
    Journal j = shipped();
    JournalState st = new JournalState();
    st.unlock("journal.about");
    st.unlock("journal.a1.pressure");
    st.markAllSeen();
    st.unlock("briefing.shift1");
    Journal.Book b = j.build(st, false, PARAMS, "Keeper's Journal", "New");
    assertTrue(b.pages().size() >= 4, "contents plus one page per entry");
    var contents = b.pages().get(0);
    var jumps = contents.stream().filter(s -> s.jumpToPage() > 0).toList();
    assertEquals(3, jumps.size());
    assertTrue(jumps.stream().anyMatch(s -> s.text().contains("Shift Briefing (New)")));
    assertFalse(jumps.stream().anyMatch(s -> s.text().contains("Air pressure (New)")));
    for (var s : jumps) {
      assertTrue(s.jumpToPage() >= 2 && s.jumpToPage() <= b.pages().size(), s.text());
    }
    // A high-school book adds the Read more page of the pressure entry.
    int hsPages = j.build(st, true, PARAMS, "Keeper's Journal", "New").pages().size();
    assertEquals(b.pages().size() + 1, hsPages);
  }

  @Test
  void unlockingIsOncePerEntryAndReadingClearsTheNewMarks() {
    JournalState st = new JournalState();
    assertTrue(st.unlock("a"));
    assertFalse(st.unlock("a"));
    assertEquals(List.of("a"), st.unread());
    st.markAllSeen();
    assertTrue(st.unread().isEmpty());
    JournalState back = new JournalState(st.unlocked(), st.seen());
    assertEquals(st.unlocked(), back.unlocked());
    assertTrue(back.unread().isEmpty());
  }

  @Test
  void splitKeepsWordsWholeAndPagesUnderTheBudget() {
    for (String c : Journal.split("word ".repeat(200).trim(), Journal.PAGE_CHARS)) {
      assertTrue(c.length() <= Journal.PAGE_CHARS);
    }
  }
}
