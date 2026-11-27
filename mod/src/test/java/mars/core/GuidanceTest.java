package mars.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GuidanceTest {
  @Test
  void classroomNeverEndsUpOnExpert() {
    assertEquals(Guidance.GENTLE, Guidance.effective(true, Guidance.EXPERT));
    assertEquals(Guidance.GENTLE, Guidance.effective(true, null));
    assertEquals(Guidance.STANDARD, Guidance.effective(true, Guidance.STANDARD));
  }

  @Test
  void standardPlayDefaultsToStandardAndKeepsExpert() {
    assertEquals(Guidance.STANDARD, Guidance.effective(false, null));
    assertEquals(Guidance.EXPERT, Guidance.effective(false, Guidance.EXPERT));
    assertTrue(Guidance.allowed(false, Guidance.EXPERT));
    assertFalse(Guidance.allowed(true, Guidance.EXPERT));
  }

  @Test
  void parseIsCaseInsensitiveAndRejectsUnknown() {
    assertEquals(Guidance.EXPERT, Guidance.parse(" Expert ").orElseThrow());
    assertTrue(Guidance.parse("hard").isEmpty());
    assertTrue(Guidance.parse(null).isEmpty());
  }
}
