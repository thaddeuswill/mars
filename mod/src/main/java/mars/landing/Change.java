package mars.landing;

/**
 * One thing that happened to a player's Landing, returned by {@link LandingState} so the caller can
 * log it, say a BELL line or send a panel update. {@code step} and {@code status} are set for
 * {@link Kind#STEP}; {@code phase} is the new phase for the others.
 */
public record Change(Kind kind, Step step, Status status, Phase phase) {
  public enum Kind {
    /** The checklist was offered. */
    OFFERED,
    /** The player started the checklist. */
    STARTED,
    /** A step left PENDING. */
    STEP,
    /** Every step is done, auto or skipped: Landing DONE. */
    FINISHED,
    /** "I know the drill" or Skip all. */
    SKIPPED_ALL
  }

  static Change offered() {
    return new Change(Kind.OFFERED, null, null, Phase.OFFERED);
  }

  static Change started() {
    return new Change(Kind.STARTED, null, null, Phase.RUNNING);
  }

  static Change step(Step step, Status status) {
    return new Change(Kind.STEP, step, status, null);
  }

  static Change finished() {
    return new Change(Kind.FINISHED, null, null, Phase.DONE);
  }

  static Change skippedAll() {
    return new Change(Kind.SKIPPED_ALL, null, null, Phase.SKIPPED_ALL);
  }
}
