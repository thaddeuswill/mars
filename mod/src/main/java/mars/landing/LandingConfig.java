package mars.landing;

import com.google.gson.JsonObject;

/**
 * The Landing's data ({@code data/mars/landing.json}, landing-spec T6): how long before a hint, and
 * the two flags that switch step text from the stand-in to the real block when it exists. Pure.
 */
public record LandingConfig(int hintAfterSeconds, boolean suitsBuilt, boolean dustOffBuilt) {

  /** Server ticks per second; the tests and the hint timer use this. */
  public static final int TICKS_PER_SECOND = 20;

  public long hintDelayTicks() {
    return (long) hintAfterSeconds * TICKS_PER_SECOND;
  }

  /** The language key of a step's text (landing-spec 3.8). */
  public String stepKey(Step step) {
    String base = "landing.step." + step.number();
    if (step == Step.TANK && suitsBuilt) {
      return base + ".suit";
    }
    if (step == Step.AIRLOCK && dustOffBuilt) {
      return base + ".dust";
    }
    return base;
  }

  public String hintKey(Step step) {
    return "landing.hint." + step.number();
  }

  /** The three skip-summary keys (landing-spec 3.5); 1 and 3 switch with the flags. */
  public String[] skipKeys() {
    return new String[] {
      "landing.skip.1" + (suitsBuilt ? ".suit" : ""),
      "landing.skip.2",
      "landing.skip.3" + (dustOffBuilt ? ".dust" : "")
    };
  }

  public static LandingConfig parse(JsonObject o, String file) {
    return new LandingConfig(
        integer(o, "hint_after_seconds", file),
        bool(o, "suits_built", file),
        bool(o, "dust_off_built", file));
  }

  private static int integer(JsonObject o, String key, String file) {
    if (!o.has(key)
        || !o.get(key).isJsonPrimitive()
        || !o.get(key).getAsJsonPrimitive().isNumber()) {
      throw new IllegalStateException(file + ": missing number \"" + key + "\"");
    }
    int v = o.get(key).getAsInt();
    if (v < 0) {
      throw new IllegalStateException(file + ": \"" + key + "\" must not be negative");
    }
    return v;
  }

  private static boolean bool(JsonObject o, String key, String file) {
    if (!o.has(key)
        || !o.get(key).isJsonPrimitive()
        || !o.get(key).getAsJsonPrimitive().isBoolean()) {
      throw new IllegalStateException(file + ": missing true/false \"" + key + "\"");
    }
    return o.get(key).getAsBoolean();
  }
}
