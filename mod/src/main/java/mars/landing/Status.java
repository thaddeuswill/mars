package mars.landing;

/** What happened to one step for one player. */
public enum Status {
  /** Not yet done. */
  PENDING,
  /** The player did it. */
  DONE,
  /** The world was already in that state (a teammate did it); credits nobody. */
  AUTO,
  /** The player skipped it. */
  SKIPPED
}
