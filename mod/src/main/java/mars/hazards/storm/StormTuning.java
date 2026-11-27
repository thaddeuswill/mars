package mars.hazards.storm;

import com.google.gson.JsonObject;

/**
 * Storm schedule numbers from {@code data/mars/storms/dust_storm.json} (balance sheet section 2).
 * All values are invented game-balance numbers. Pure Java; a bad file fails with the file and field
 * named. There is no solar cut or dust field here on purpose: those are physical and live in the
 * panel dust file, so pace can never scale them.
 */
public record StormTuning(double capPhase1, Mode standard, Mode classroom) {

  /** One pace mode's timing, all in sols of simulation time. */
  public record Mode(
      double phase1LengthSols,
      double holdLengthSols,
      double firstNotBeforeSol,
      double gapMinSols,
      double gapMaxSols,
      double warningSols) {
    public Mode {
      if (phase1LengthSols <= 0
          || holdLengthSols <= 0
          || firstNotBeforeSol < warningSols
          || warningSols < 0
          || gapMinSols <= warningSols
          || gapMaxSols < gapMinSols) {
        throw new IllegalArgumentException("Invalid storm timing: " + this);
      }
    }

    public double lengthSols(StormSchedule.Kind kind) {
      return kind == StormSchedule.Kind.PHASE_1 ? phase1LengthSols : holdLengthSols;
    }
  }

  public Mode mode(boolean classroom) {
    return classroom ? this.classroom : standard;
  }

  public static StormTuning parse(JsonObject o, String file) {
    if (!o.has("schema") || o.get("schema").getAsInt() != 1) {
      throw new IllegalArgumentException(file + ": schema must be 1");
    }
    double cap = num(obj(o, "storm_cap", file), "phase_1", file);
    return new StormTuning(
        cap, mode(obj(o, "standard", file), file), mode(obj(o, "classroom", file), file));
  }

  private static Mode mode(JsonObject m, String file) {
    JsonObject gap = obj(m, "gap_sols", file);
    return new Mode(
        num(m, "phase_1_length_sols", file),
        num(m, "hold_length_sols", file),
        num(m, "first_not_before_sol", file),
        num(gap, "min", file),
        num(gap, "max", file),
        num(m, "warning_sols", file));
  }

  private static JsonObject obj(JsonObject o, String field, String file) {
    if (!o.has(field) || !o.get(field).isJsonObject()) {
      throw new IllegalArgumentException(file + ": missing object field '" + field + "'");
    }
    return o.getAsJsonObject(field);
  }

  private static double num(JsonObject o, String field, String file) {
    if (!o.has(field)
        || !o.get(field).isJsonPrimitive()
        || !o.get(field).getAsJsonPrimitive().isNumber()) {
      throw new IllegalArgumentException(file + ": missing number field '" + field + "'");
    }
    return o.get(field).getAsDouble();
  }
}
