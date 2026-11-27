package mars.index.sim;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import mars.core.ParameterId;

/**
 * A machine type's effect on the planet, read from {@code data/mars/machines/<name>.json}: what it
 * contributes to which Index parameter, per sol, at standard pace (ARCHITECTURE.md 2.1, 2.16).
 */
public record MachineDef(String id, List<Contribution> contributions) {

  /** One contribution: {@code ratePerSol} added to {@code parameter} (negative = a sink). */
  public record Contribution(ParameterId parameter, double ratePerSol) {}

  public static MachineDef parse(JsonObject o, String file) {
    String id = string(o, "id", file);
    JsonElement arr = o.get("contributions");
    if (arr == null || !arr.isJsonArray()) {
      throw new IllegalArgumentException(file + ": missing array field 'contributions'");
    }
    List<Contribution> list = new ArrayList<>();
    JsonArray a = arr.getAsJsonArray();
    for (JsonElement e : a) {
      JsonObject c = e.getAsJsonObject();
      JsonElement rate = c.get("ratePerSol");
      if (rate == null || !rate.isJsonPrimitive() || !rate.getAsJsonPrimitive().isNumber()) {
        throw new IllegalArgumentException(file + ": contribution missing number 'ratePerSol'");
      }
      list.add(new Contribution(ParameterId.of(string(c, "parameter", file)), rate.getAsDouble()));
    }
    return new MachineDef(id, List.copyOf(list));
  }

  private static String string(JsonObject o, String field, String file) {
    JsonElement e = o.get(field);
    if (e == null || !e.isJsonPrimitive()) {
      throw new IllegalArgumentException(file + ": missing string field '" + field + "'");
    }
    return e.getAsString();
  }
}
