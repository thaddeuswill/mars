package mars;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import org.junit.jupiter.api.Test;

/** CR-T7 scan over the English text (docs/learning/content-rules.md). */
class LangWordingTest {
  private static JsonObject lang() throws Exception {
    return JsonParser.parseString(
            Files.readString(
                TestData.pack().resolve("assets/mars/lang/en_us.json"), StandardCharsets.UTF_8))
        .getAsJsonObject();
  }

  @Test
  void noTildeOrLessThanAsWordsAndNoBritishLevelling() throws Exception {
    for (Map.Entry<String, com.google.gson.JsonElement> e : lang().entrySet()) {
      String v = e.getValue().getAsString();
      String key = e.getKey();
      assertTrue(!v.contains("~"), key + " uses ~; write 'about' (CR-T7b)");
      assertTrue(!v.matches(".*<\\s*\\d.*"), key + " uses < as a word; write 'under' (CR-T7b)");
      assertTrue(!v.toLowerCase().contains("levelling"), key + " uses British spelling (CR-T7a)");
    }
  }

  @Test
  void estimateAndGoalWordingIsFinal() throws Exception {
    JsonObject l = lang();
    assertEquals("about %s sols", l.get("monitor.goal.eta").getAsString());
    assertEquals("about 1 sol", l.get("monitor.goal.eta_one").getAsString());
    assertEquals("under 1 sol", l.get("monitor.goal.eta_soon").getAsString());
    assertEquals("Pressure goal: %s", l.get("monitor.plot.goal.partial").getAsString());
    assertTrue(l.get("monitor.estimate.levelling_off").getAsString().startsWith("Leveling off"));
  }

  @Test
  void oneShotLinesAreAtMostTwentyWords() throws Exception {
    for (String k : new String[] {"grant.starting", "monitor.plot.rescaled"}) {
      String v = lang().get(k).getAsString();
      int words = v.trim().split("\\s+").length;
      assertTrue(words <= 20, k + " has " + words + " words (CR-T7c, invented budget)");
    }
  }

  @Test
  void noBannedTermsOrBlameWording() throws Exception {
    java.util.regex.Pattern banned =
        java.util.regex.Pattern.compile(
            "\\b(learners?|lessons?|objectives?|quiz(zes)?|tests?)\\b",
            java.util.regex.Pattern.CASE_INSENSITIVE);
    java.util.regex.Pattern blame =
        java.util.regex.Pattern.compile(
            "you failed|wrong!|you forgot", java.util.regex.Pattern.CASE_INSENSITIVE);
    for (Map.Entry<String, com.google.gson.JsonElement> e : lang().entrySet()) {
      String v = e.getValue().getAsString();
      assertTrue(!banned.matcher(v).find(), e.getKey() + " uses a banned word (CR-T1)");
      assertTrue(!blame.matcher(v).find(), e.getKey() + " blames the player (CR-T5)");
    }
  }

  @Test
  void gateNamesAndRowLabelsAreShort() throws Exception {
    // The label column is 86 px; about 24 characters (CR-T7c, invented budget).
    for (Map.Entry<String, com.google.gson.JsonElement> e : lang().entrySet()) {
      String k = e.getKey();
      if (k.startsWith("gate.mars.") || k.startsWith("monitor.row.")) {
        int n = e.getValue().getAsString().length();
        assertTrue(n <= 24, k + " is " + n + " characters (CR-T7c)");
      }
    }
  }

  @Test
  void gateDetailLinesAreShort() throws Exception {
    // They share a line with the goals count and the percent (MonitorLayout.fitDetail).
    for (Map.Entry<String, com.google.gson.JsonElement> e : lang().entrySet()) {
      if (e.getKey().startsWith("monitor.gate.")) {
        int n = e.getValue().getAsString().length();
        assertTrue(n <= 28, e.getKey() + " is " + n + " characters; it must fit beside the count");
      }
    }
  }
}
