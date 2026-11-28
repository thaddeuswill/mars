package mars.hazards.storm;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Storm schedule and cap table from {@code data/mars/hazards/dust_storm.json}. Pure Java; a missing
 * field fails with the file and field named.
 */
public record StormTuning(
    double warningLeadSols,
    double reminderLeadSols,
    double rampInSols,
    double rampOutSols,
    Preset standard,
    Preset classroom,
    double retryEverySols,
    int maxRetries,
    List<Cap> caps,
    Set<String> affectedMachineTypes) {

  /** One pace preset: the first storm and the range for the later ones (all in sols). */
  public record Preset(
      double firstOnsetSol,
      double firstLengthSols,
      double gapMinSols,
      double gapMaxSols,
      double lengthMinSols,
      double lengthMaxSols) {}

  /**
   * One row of the cap table. {@code gate} null: always applies. {@code maxStorms} -1: no limit.
   */
  public record Cap(int phase, String gate, double cap, int maxStorms) {}

  public Preset preset(boolean classroomMode) {
    return classroomMode ? classroom : standard;
  }

  /** Index of the highest cap row whose gate has latched (row 0 has no gate). */
  public int capIndex(Predicate<String> latched) {
    int best = 0;
    for (int i = 0; i < caps.size(); i++) {
      String g = caps.get(i).gate();
      if (g == null || latched.test(g)) {
        best = i;
      }
    }
    return best;
  }

  public double capFor(Predicate<String> latched) {
    return caps.get(capIndex(latched)).cap();
  }

  public static StormTuning parse(Reader json, String file) {
    JsonObject o = JsonParser.parseReader(json).getAsJsonObject();
    JsonObject first = obj(o, "first_storm", file);
    JsonObject later = obj(o, "later_storms", file);
    JsonObject guards = obj(o, "guards", file);
    Preset std = preset(first, later, "standard", file);
    Preset cls = preset(first, later, "classroom", file);
    List<Cap> caps = new ArrayList<>();
    JsonArray arr = o.getAsJsonArray("caps");
    if (arr == null || arr.isEmpty()) {
      throw new IllegalArgumentException(file + ": missing array field 'caps'");
    }
    for (JsonElement el : arr) {
      JsonObject c = el.getAsJsonObject();
      JsonElement g = c.get("gate");
      JsonElement m = c.get("max_storms");
      caps.add(
          new Cap(
              num(c, "phase", file).getAsInt(),
              g == null || g.isJsonNull() ? null : g.getAsString(),
              num(c, "cap", file).getAsDouble(),
              m == null || m.isJsonNull() ? -1 : m.getAsInt()));
    }
    if (caps.get(0).gate() != null) {
      throw new IllegalArgumentException(file + ": the first cap row must have no gate");
    }
    Set<String> types = new java.util.LinkedHashSet<>();
    for (JsonElement el : o.getAsJsonArray("affects_machine_types")) {
      types.add(el.getAsString());
    }
    return new StormTuning(
        value(o, "warning_lead_sols", file),
        value(o, "reminder_lead_sols", file),
        value(o, "ramp_in_sols", file),
        value(o, "ramp_out_sols", file),
        std,
        cls,
        num(guards, "retry_every_sols", file).getAsDouble(),
        num(guards, "max_retries", file).getAsInt(),
        List.copyOf(caps),
        Set.copyOf(types));
  }

  private static Preset preset(JsonObject first, JsonObject later, String key, String file) {
    JsonArray gap = later.getAsJsonObject("gap_after_end_sols").getAsJsonArray(key);
    JsonArray len = later.getAsJsonObject("length_sols").getAsJsonArray(key);
    return new Preset(
        num(obj(first, "onset_sol", file), key, file).getAsDouble(),
        num(obj(first, "length_sols", file), key, file).getAsDouble(),
        gap.get(0).getAsDouble(),
        gap.get(1).getAsDouble(),
        len.get(0).getAsDouble(),
        len.get(1).getAsDouble());
  }

  private static double value(JsonObject o, String field, String file) {
    return num(obj(o, field, file), "value", file).getAsDouble();
  }

  private static JsonObject obj(JsonObject o, String field, String file) {
    JsonElement e = o.get(field);
    if (e == null || !e.isJsonObject()) {
      throw new IllegalArgumentException(file + ": missing object field '" + field + "'");
    }
    return e.getAsJsonObject();
  }

  private static JsonElement num(JsonObject o, String field, String file) {
    JsonElement e = o.get(field);
    if (e == null || !e.isJsonPrimitive() || !e.getAsJsonPrimitive().isNumber()) {
      throw new IllegalArgumentException(file + ": missing number field '" + field + "'");
    }
    return e;
  }
}
