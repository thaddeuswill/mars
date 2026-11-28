package mars.landing;

/** Where a player is in the Landing (landing-spec 3.2). */
public enum Phase {
  NOT_OFFERED,
  OFFERED,
  RUNNING,
  DONE,
  SKIPPED_ALL;

  /** True once the checklist is over, finished or skipped. */
  public boolean over() {
    return this == DONE || this == SKIPPED_ALL;
  }
}
