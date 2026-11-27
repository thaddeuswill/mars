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
}
