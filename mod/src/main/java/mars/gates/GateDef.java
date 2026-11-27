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

  /** Hold lengths in simulation sols (balance sheet 4). */
  public record Hold(double standardSols, double classroomSols) {}

  public static GateDef parse(JsonObject o, String file) {
    String id = text(o, "id", file);
    int order = o.has("order") ? o.get("order").getAsInt() : 0;
    Condition condition = Condition.parse(o.get("condition"), file);
    Hold hold = null;
    if (o.has("hold")) {
      JsonObject h = o.getAsJsonObject("hold");
      hold = new Hold(num(h, "standard_sols", file), num(h, "classroom_sols", file));
      if (hold.standardSols() <= 0 || hold.classroomSols() <= 0) {
        throw new IllegalArgumentException(file + ": hold lengths must be above 0");
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
