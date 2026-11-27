package mars.learning;

/** A world-local learner pseudonym. Raw account identifiers are outside the log API. */
public record PlayerId(String value) {
  public static final String ALPHABET = "23456789ABCDEFGHJKMNPQRSTUVWXYZ";

  public PlayerId {
    if (value == null || !value.matches("P-[" + ALPHABET + "]{5}")) {
      throw new IllegalArgumentException("Invalid learner pseudonym");
    }
  }
}
