package mars.session;

/**
 * The session clock's on/off switch (ARCHITECTURE.md 7.2): one paused flag for the whole server and
 * a session number that goes up on every resume. Pure Java. Simulation time is not stored here: it
 * is the planet's own tick counter, which only advances when {@link #running()} is true, so nothing
 * here reads wall-clock time, the daylight cycle, or sleeping (invariant I-3). Cost: one boolean
 * read per planet tick.
 */
public final class SessionClock {
  /** Saved form version; a newer one is refused. */
  public static final int CURRENT_SCHEMA = 1;

  private boolean paused;
  private int sessionNumber;

  public SessionClock(boolean startPaused) {
    this.paused = startPaused;
    this.sessionNumber = 1;
  }

  /** Rebuild from a saved state. A newer schema is refused rather than read and overwritten. */
  public static SessionClock restore(int schema, boolean paused, int sessionNumber) {
    if (schema > CURRENT_SCHEMA) {
      throw new IllegalStateException(
          "Session save has schema "
              + schema
              + " but this build reads up to "
              + CURRENT_SCHEMA
              + "; refusing to load it (it would be overwritten).");
    }
    SessionClock c = new SessionClock(paused);
    c.sessionNumber = Math.max(1, sessionNumber);
    return c;
  }

  public boolean running() {
    return !paused;
  }

  public boolean paused() {
    return paused;
  }

  public int sessionNumber() {
    return sessionNumber;
  }

  /** Returns true if the state changed. */
  public boolean pause() {
    if (paused) {
      return false;
    }
    paused = true;
    return true;
  }

  /** Starts a new session (7.2). Returns true if the state changed. */
  public boolean resume() {
    if (!paused) {
      return false;
    }
    paused = false;
    sessionNumber++;
    return true;
  }
}
