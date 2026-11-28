package mars.landing;

/** The planet facts that two steps read (landing-spec 3.3), gathered by the caller once a second. */
public record Facts(boolean habBSealed, boolean ovensPowered) {
  public static final Facts NONE = new Facts(false, false);
}
