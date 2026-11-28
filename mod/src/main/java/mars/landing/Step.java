package mars.landing;

/** The eight Landing steps, in checklist order (landing-spec 3.3). */
public enum Step {
  DOOR,
  TANK,
  MONITOR,
  KIT,
  PATCH,
  OVENS,
  AIRLOCK,
  JOURNAL;

  /** 1 to 8, as in the keys {@code landing.step.N}. */
  public int number() {
    return ordinal() + 1;
  }

  /** Steps whose condition is a fact about the planet, so a teammate can have done them. */
  public boolean planetState() {
    return this == PATCH || this == OVENS;
  }

  public static Step of(int number) {
    return values()[number - 1];
  }
}
