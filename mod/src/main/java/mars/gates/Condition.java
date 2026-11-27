package mars.gates;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import mars.core.ParameterId;

/**
 * The gate condition language (ARCHITECTURE.md 2.2): {@code all}, {@code any}, {@code not}, a
 * comparison of one parameter with a plain number, and a named machine-state check. Pure Java.
 * There is no field that scales a threshold, and the parser rejects any key it does not know, so a
 * data file cannot add one (invariant I-1).
 */
public sealed interface Condition {

  /** What a condition can read. */
  interface Context {
    /** Current value of a stored or derived parameter. */
    double value(ParameterId id);

    /** A named world check (for example {@code life_support_ok}). */
    boolean machineState(String name);
  }

  boolean eval(Context ctx);

  /** Collect every named machine state this condition uses (so a bad name fails at load). */
  void machineStates(Set<String> out);

  record All(List<Condition> parts) implements Condition {
    @Override
    public boolean eval(Context ctx) {
      for (Condition c : parts) {
        if (!c.eval(ctx)) {
          return false;
        }
      }
      return true;
    }

    @Override
    public void machineStates(Set<String> out) {
      parts.forEach(c -> c.machineStates(out));
    }
  }

  record Any(List<Condition> parts) implements Condition {
    @Override
    public boolean eval(Context ctx) {
      for (Condition c : parts) {
        if (c.eval(ctx)) {
          return true;
        }
      }
      return false;
    }

    @Override
    public void machineStates(Set<String> out) {
      parts.forEach(c -> c.machineStates(out));
    }
  }

  record Not(Condition inner) implements Condition {
    @Override
    public boolean eval(Context ctx) {
      return !inner.eval(ctx);
    }

    @Override
    public void machineStates(Set<String> out) {
      inner.machineStates(out);
    }
  }

  enum Op {
    GE(">="),
    GT(">"),
    LE("<="),
    LT("<");

    private final String symbol;

    Op(String symbol) {
      this.symbol = symbol;
    }

    static Op of(String s, String file) {
      for (Op o : values()) {
        if (o.symbol.equals(s)) {
          return o;
        }
      }
      throw new IllegalArgumentException(file + ": unknown comparison '" + s + "'");
    }

    boolean test(double a, double b) {
      return switch (this) {
        case GE -> a >= b;
        case GT -> a > b;
        case LE -> a <= b;
        case LT -> a < b;
      };
    }
  }

  /** {@code parameter op value}; a NaN reading is never true. */
  record Compare(ParameterId parameter, Op op, double value) implements Condition {
    @Override
    public boolean eval(Context ctx) {
      double v = ctx.value(parameter);
      return !Double.isNaN(v) && op.test(v, value);
    }

    @Override
    public void machineStates(Set<String> out) {}
  }

  record MachineState(String name) implements Condition {
    @Override
    public boolean eval(Context ctx) {
      return ctx.machineState(name);
    }

    @Override
    public void machineStates(Set<String> out) {
      out.add(name);
    }
  }

  /** Parse one condition object. Unknown keys fail, with the file named. */
  static Condition parse(JsonElement e, String file) {
    if (e == null || !e.isJsonObject()) {
      throw new IllegalArgumentException(file + ": a condition must be an object");
    }
    JsonObject o = e.getAsJsonObject();
    if (o.has("all") || o.has("any")) {
      String key = o.has("all") ? "all" : "any";
      requireOnly(o, Set.of(key), file);
      JsonElement arr = o.get(key);
      if (!arr.isJsonArray() || arr.getAsJsonArray().isEmpty()) {
        throw new IllegalArgumentException(file + ": '" + key + "' needs a non-empty array");
      }
      List<Condition> parts = new ArrayList<>();
      for (JsonElement p : (JsonArray) arr) {
        parts.add(parse(p, file));
      }
      return key.equals("all") ? new All(List.copyOf(parts)) : new Any(List.copyOf(parts));
    }
    if (o.has("not")) {
      requireOnly(o, Set.of("not"), file);
      return new Not(parse(o.get("not"), file));
    }
    if (o.has("machine_state")) {
      requireOnly(o, Set.of("machine_state"), file);
      return new MachineState(o.get("machine_state").getAsString());
    }
    if (o.has("parameter")) {
      requireOnly(o, Set.of("parameter", "op", "value"), file);
      JsonElement v = o.get("value");
      if (!o.has("op") || v == null || !v.isJsonPrimitive() || !v.getAsJsonPrimitive().isNumber()) {
        throw new IllegalArgumentException(file + ": a comparison needs 'op' and a number 'value'");
      }
      return new Compare(
          ParameterId.of(o.get("parameter").getAsString()),
          Op.of(o.get("op").getAsString(), file),
          v.getAsDouble());
    }
    throw new IllegalArgumentException(
        file + ": unknown condition " + o + " (use all, any, not, machine_state or parameter)");
  }

  private static void requireOnly(JsonObject o, Set<String> allowed, String file) {
    for (String k : o.keySet()) {
      if (!allowed.contains(k)) {
        throw new IllegalArgumentException(
            file + ": unknown key '" + k + "' in a condition (thresholds are never scaled)");
      }
    }
  }
}
