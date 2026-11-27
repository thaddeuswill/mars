package mars.index.sim;

import com.google.gson.JsonObject;

/**
 * The pace presets, parsed from {@code data/mars/pace.json} (ARCHITECTURE.md 2.1, 2.20). Pure Java.
 * The file holds rates, group size and the creative-bypass switch only: it has no threshold, so no
 * mode can scale one (invariant I-1). Hold lengths stay in the gate files.
 */
public record PaceConfig(int solTicks, int stepTicks, Preset standard, Preset classroom) {

  /** One mode's numbers, including the group sizes that mode allows. */
  public record Preset(
      double rateMultiplier,
      int groupSize,
      int groupMin,
      int groupMax,
      boolean creativeGateBypass) {
    public boolean groupAllowed(int n) {
      return n >= groupMin && n <= groupMax;
    }
  }

  /** The two modes an operator can choose. */
  public enum Mode {
    STANDARD,
    CLASSROOM;

    public static Mode parse(String s) {
      return valueOf(s.toUpperCase(java.util.Locale.ROOT));
    }
  }

  public static PaceConfig parse(JsonObject o, String file) {
    int schema = o.has("schema") ? o.get("schema").getAsInt() : -1;
    if (schema != 1) {
      throw new IllegalArgumentException(file + ": unsupported schema " + schema);
    }
    PaceConfig c =
        new PaceConfig(
            o.get("sol_ticks").getAsInt(),
            o.get("step_ticks").getAsInt(),
            preset(obj(o, "standard", file), file),
            preset(obj(o, "classroom", file), file));
    if (c.solTicks <= 0 || c.stepTicks <= 0) {
      throw new IllegalArgumentException(file + ": invalid ticks");
    }
    for (Preset p : new Preset[] {c.standard, c.classroom}) {
      if (p.groupMin() < 1 || p.groupMax() < p.groupMin()) {
        throw new IllegalArgumentException(file + ": invalid group size range " + p);
      }
      if (p.rateMultiplier() <= 0 || !p.groupAllowed(p.groupSize())) {
        throw new IllegalArgumentException(file + ": invalid preset " + p);
      }
    }
    return c;
  }

  private static JsonObject obj(JsonObject o, String key, String file) {
    if (!o.has(key) || !o.get(key).isJsonObject()) {
      throw new IllegalArgumentException(file + ": missing object '" + key + "'");
    }
    return o.getAsJsonObject(key);
  }

  private static Preset preset(JsonObject p, String file) {
    for (String k :
        new String[] {
          "rate_multiplier", "group_size", "group_size_range", "creative_gate_bypass"
        }) {
      if (!p.has(k)) {
        throw new IllegalArgumentException(file + ": preset lacks '" + k + "'");
      }
    }
    JsonObject range = obj(p, "group_size_range", file);
    if (!range.has("min") || !range.has("max")) {
      throw new IllegalArgumentException(file + ": group_size_range needs min and max");
    }
    return new Preset(
        p.get("rate_multiplier").getAsDouble(),
        p.get("group_size").getAsInt(),
        range.get("min").getAsInt(),
        range.get("max").getAsInt(),
        p.get("creative_gate_bypass").getAsBoolean());
  }

  /** The pace for a mode; {@code groupSize} null means the preset's own. */
  public Pace pace(Mode mode, Integer groupSize) {
    Preset p = mode == Mode.CLASSROOM ? classroom : standard;
    int n = groupSize == null ? p.groupSize() : groupSize;
    if (!p.groupAllowed(n)) {
      throw new IllegalArgumentException(
          "Group size for "
              + mode.name().toLowerCase(java.util.Locale.ROOT)
              + " must be "
              + p.groupMin()
              + " to "
              + p.groupMax()
              + " (you gave "
              + n
              + "). Nothing was changed.");
    }
    return new Pace(
        p.rateMultiplier(), n, solTicks, stepTicks, mode == Mode.CLASSROOM, p.creativeGateBypass());
  }
}
