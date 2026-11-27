package mars.index.sim;

import com.google.gson.JsonObject;

/**
 * The pace presets, parsed from {@code data/mars/pace.json} (ARCHITECTURE.md 2.1, 2.20). Pure Java.
 * The file holds rates, group size and the creative-bypass switch only: it has no threshold, so no
 * mode can scale one (invariant I-1). Hold lengths stay in the gate files.
 */
public record PaceConfig(
    int solTicks, int stepTicks, int groupMin, int groupMax, Preset standard, Preset classroom) {

  /** One mode's numbers. */
  public record Preset(double rateMultiplier, int groupSize, boolean creativeGateBypass) {}

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
    JsonObject range = obj(o, "group_size_range", file);
    PaceConfig c =
        new PaceConfig(
            o.get("sol_ticks").getAsInt(),
            o.get("step_ticks").getAsInt(),
            range.get("min").getAsInt(),
            range.get("max").getAsInt(),
            preset(obj(o, "standard", file), file),
            preset(obj(o, "classroom", file), file));
    if (c.solTicks <= 0 || c.stepTicks <= 0 || c.groupMin < 1 || c.groupMax < c.groupMin) {
      throw new IllegalArgumentException(file + ": invalid ticks or group size range");
    }
    for (Preset p : new Preset[] {c.standard, c.classroom}) {
      if (p.rateMultiplier() <= 0 || !c.groupAllowed(p.groupSize())) {
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
    for (String k : new String[] {"rate_multiplier", "group_size", "creative_gate_bypass"}) {
      if (!p.has(k)) {
        throw new IllegalArgumentException(file + ": preset lacks '" + k + "'");
      }
    }
    return new Preset(
        p.get("rate_multiplier").getAsDouble(),
        p.get("group_size").getAsInt(),
        p.get("creative_gate_bypass").getAsBoolean());
  }

  public boolean groupAllowed(int n) {
    return n >= groupMin && n <= groupMax;
  }

  /** The pace for a mode; {@code groupSize} null means the preset's own. */
  public Pace pace(Mode mode, Integer groupSize) {
    Preset p = mode == Mode.CLASSROOM ? classroom : standard;
    int n = groupSize == null ? p.groupSize() : groupSize;
    if (!groupAllowed(n)) {
      throw new IllegalArgumentException("Group size must be " + groupMin + " to " + groupMax);
    }
    return new Pace(
        p.rateMultiplier(), n, solTicks, stepTicks, mode == Mode.CLASSROOM, p.creativeGateBypass());
  }
}
