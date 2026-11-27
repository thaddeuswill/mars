package mars.core;

import java.util.Locale;
import java.util.Optional;

/**
 * How much extra help the learner gets (DESIGN.md "Difficulty as information"). It only removes
 * extra help (estimates, hints); thresholds, rates and failure reports never change with it. Pure
 * Java.
 */
public enum Guidance {
  GENTLE,
  STANDARD,
  EXPERT;

  /** The lower-case name used in commands, data and saves. */
  public String id() {
    return name().toLowerCase(Locale.ROOT);
  }

  public static Optional<Guidance> parse(String text) {
    if (text == null) {
      return Optional.empty();
    }
    for (Guidance g : values()) {
      if (g.id().equals(text.trim().toLowerCase(Locale.ROOT))) {
        return Optional.of(g);
      }
    }
    return Optional.empty();
  }

  /** Gentle in classroom mode, standard in standard play. */
  public static Guidance defaultFor(boolean classroom) {
    return classroom ? GENTLE : STANDARD;
  }

  /** Expert is not offered in classroom mode. */
  public static boolean allowed(boolean classroom, Guidance level) {
    return !(classroom && level == EXPERT);
  }

  /**
   * The level in force. {@code stored} is null when the player never set one (use the default). A
   * stored expert level never applies in classroom mode, so switching a world to classroom mode
   * later cannot leave a student on expert.
   */
  public static Guidance effective(boolean classroom, Guidance stored) {
    if (stored == null) {
      return defaultFor(classroom);
    }
    return allowed(classroom, stored) ? stored : GENTLE;
  }
}
