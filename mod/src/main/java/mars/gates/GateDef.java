package mars.gates;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

/**
 * One gate, read from {@code data/mars/gates/<name>.json} (ARCHITECTURE.md 2.2). {@code order} is
 * the display order; {@code hold} is null for a plain gate and gives the hold length in sols for
 * standard and classroom play (rates and holds scale with pace, thresholds never do). {@code
 * unlocks} lists machine ids that may not be placed by survival players until the gate has latched.
 */
public record GateDef(
    String id, int order, Condition condition, Hold hold, List<String> unlocks, String nameKey) {

  /**
   * Hold lengths in simulation sols (balance sheet 4) and the hold rule's grace: the counter pauses
   * when the condition fails and resets only after more than {@code resetAfter*Sols} of continuous
   * failure (balance sheet 2c decision 7: 1 sol standard, 0.5 classroom). A rate-like scale, never
   * a threshold.
   */
  public record Hold(
      double standardSols,
      double classroomSols,
      double resetAfterStandardSols,
      double resetAfterClassroomSols) {
    public static final double DEFAULT_RESET_STANDARD = 1.0;
    public static final double DEFAULT_RESET_CLASSROOM = 0.5;

    public Hold(double standardSols, double classroomSols) {
      this(standardSols, classroomSols, DEFAULT_RESET_STANDARD, DEFAULT_RESET_CLASSROOM);
    }
  }

  public static GateDef parse(JsonObject o, String file) {
    String id = text(o, "id", file);
    int order = o.has("order") ? o.get("order").getAsInt() : 0;
    Condition condition = Condition.parse(o.get("condition"), file);
    Hold hold = null;
    if (o.has("hold")) {
      JsonObject h = o.getAsJsonObject("hold");
      double resetStd = Hold.DEFAULT_RESET_STANDARD;
      double resetRoom = Hold.DEFAULT_RESET_CLASSROOM;
      if (h.has("reset_after_sols")) {
        JsonObject r = h.getAsJsonObject("reset_after_sols");
        resetStd = num(r, "standard", file);
        resetRoom = num(r, "classroom", file);
      }
      hold =
          new Hold(
              num(h, "standard_sols", file), num(h, "classroom_sols", file), resetStd, resetRoom);
      if (hold.standardSols() <= 0 || hold.classroomSols() <= 0) {
        throw new IllegalArgumentException(file + ": hold lengths must be above 0");
      }
      if (resetStd < 0 || resetRoom < 0) {
        throw new IllegalArgumentException(file + ": reset_after_sols must be 0 or more");
      }
    }
    List<String> unlocks = new ArrayList<>();
    if (o.has("unlocks")) {
      for (JsonElement e : (JsonArray) o.get("unlocks")) {
        unlocks.add(e.getAsString());
      }
    }
    return new GateDef(
        id,
        order,
        condition,
        hold,
        List.copyOf(unlocks),
        o.has("name_key") ? o.get("name_key").getAsString() : "gate." + id.replace(':', '.'));
  }

  private static String text(JsonObject o, String f, String file) {
    JsonElement e = o.get(f);
    if (e == null || !e.isJsonPrimitive()) {
      throw new IllegalArgumentException(file + ": missing string field '" + f + "'");
    }
    return e.getAsString();
  }

  private static double num(JsonObject o, String f, String file) {
    JsonElement e = o.get(f);
    if (e == null || !e.isJsonPrimitive() || !e.getAsJsonPrimitive().isNumber()) {
      throw new IllegalArgumentException(file + ": missing number field '" + f + "'");
    }
    return e.getAsDouble();
  }
}
