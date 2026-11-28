package mars.landing;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.nio.file.Files;
import mars.TestData;
import org.junit.jupiter.api.Test;

class LandingConfigTest {
  private static LandingConfig shipped() throws Exception {
    try (var r = Files.newBufferedReader(TestData.data("landing.json"))) {
      return LandingConfig.parse(new Gson().fromJson(r, JsonObject.class), "landing.json");
    }
  }

  @Test
  void theShippedFileHasTheStandInFlagsAndAThreeMinuteHint() throws Exception {
    LandingConfig c = shipped();
    assertEquals(180, c.hintAfterSeconds());
    assertEquals(3600, c.hintDelayTicks());
    assertEquals(false, c.suitsBuilt());
    assertEquals(false, c.dustOffBuilt());
  }

  @Test
  void theFlagsSwitchTheTextKeysFromTheStandInToTheRealBlock() {
    LandingConfig now = new LandingConfig(180, false, false);
    LandingConfig later = new LandingConfig(180, true, true);
    assertEquals("landing.step.2", now.stepKey(Step.TANK));
    assertEquals("landing.step.2.suit", later.stepKey(Step.TANK));
    assertEquals("landing.step.7", now.stepKey(Step.AIRLOCK));
    assertEquals("landing.step.7.dust", later.stepKey(Step.AIRLOCK));
    assertEquals("landing.step.3", later.stepKey(Step.MONITOR));
    assertArrayEquals(
        new String[] {"landing.skip.1", "landing.skip.2", "landing.skip.3"}, now.skipKeys());
    assertArrayEquals(
        new String[] {"landing.skip.1.suit", "landing.skip.2", "landing.skip.3.dust"},
        later.skipKeys());
  }

  @Test
  void aFileWithAMissingFieldIsRefusedByName() {
    JsonObject o = new JsonObject();
    o.addProperty("hint_after_seconds", 10);
    assertThrows(IllegalStateException.class, () -> LandingConfig.parse(o, "x.json"));
  }
}
