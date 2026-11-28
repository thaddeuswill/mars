package mars.landing;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import mars.TestData;
import org.junit.jupiter.api.Test;

/** CR-T1, T4, T7 style checks on the Landing text (landing-spec 6 and 10). */
class LandingTextTest {
  private static JsonObject lang() throws Exception {
    try (var r = Files.newBufferedReader(TestData.pack().resolve("assets/mars/lang/en_us.json"))) {
      return new Gson().fromJson(r, JsonObject.class);
    }
  }

  @Test
  void everyKeyTheLandingUsesExistsAndStepTextsAreAtMost12Words() throws Exception {
    JsonObject lang = lang();
    List<String> keys = new ArrayList<>();
    for (boolean flag : new boolean[] {false, true}) {
      LandingConfig c = new LandingConfig(180, flag, flag);
      for (Step s : Step.values()) {
        keys.add(c.stepKey(s));
        keys.add(c.hintKey(s));
      }
      keys.addAll(List.of(c.skipKeys()));
    }
    for (String k : keys) {
      assertNotNull(lang.get(k), "missing language key " + k);
      String text = lang.get(k).getAsString();
      assertTrue(text.split("\\s+").length <= 12, k + ": " + text);
      assertFalse(text.contains("~") || text.contains("<"), k);
    }
    for (String k :
        List.of(
            "landing.intro",
            "landing.done",
            "landing.log.5",
            "landing.log.6",
            "landing.relay",
            "journal.pointer",
            "tank.level",
            "seal_kit.patched",
            "seal_kit.no_target")) {
      assertNotNull(lang.get(k), k);
      assertTrue(lang.get(k).getAsString().split("\\s+").length <= 20, k);
    }
  }

  @Test
  void theBlackoutLineMatchesWhereThePlayerWakes() throws Exception {
    assertTrue(lang().get("life.blackout").getAsString().contains("shelter"));
    assertFalse(lang().get("life.blackout").getAsString().contains("bunk"));
  }

  @Test
  void stepFiveSaysCrackNotLeaking() throws Exception {
    assertTrue(lang().get("landing.step.5").getAsString().contains("crack"));
    assertFalse(lang().get("landing.step.5").getAsString().toLowerCase().contains("leak"));
  }
}
