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
public record MachineDef(String id, List<Contribution> contributions, Power power) {

  /**
   * Power use (balance sheet §2b), all optional in the file. {@code drawPerSol}: units per sol a
   * machine needs (its contributions scale with the share of power it gets). {@code supplyPerSol}:
   * units per sol it makes at full output. {@code storage}: units it can hold.
   */
  public record Power(double drawPerSol, double supplyPerSol, double storage) {
    public static final Power NONE = new Power(0, 0, 0);
  }

  public MachineDef(String id, List<Contribution> contributions) {
    this(id, contributions, Power.NONE);
  }

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
    Power power = Power.NONE;
    JsonElement p = o.get("power");
    if (p != null) {
      if (!p.isJsonObject()) {
        throw new IllegalArgumentException(file + ": 'power' must be an object");
      }
      JsonObject po = p.getAsJsonObject();
      power =
          new Power(
              optNumber(po, "drawPerSol", file),
              optNumber(po, "supplyPerSol", file),
              optNumber(po, "storage", file));
    }
    return new MachineDef(id, List.copyOf(list), power);
  }

  private static double optNumber(JsonObject o, String field, String file) {
    JsonElement e = o.get(field);
    if (e == null) {
      return 0;
    }
    if (!e.isJsonPrimitive() || !e.getAsJsonPrimitive().isNumber() || e.getAsDouble() < 0) {
      throw new IllegalArgumentException(
          file + ": power field '" + field + "' must be a number of 0 or more");
    }
    return e.getAsDouble();
  }

  private static String string(JsonObject o, String field, String file) {
    JsonElement e = o.get(field);
    if (e == null || !e.isJsonPrimitive()) {
      throw new IllegalArgumentException(file + ": missing string field '" + field + "'");
    }
    return e.getAsString();
  }
}
