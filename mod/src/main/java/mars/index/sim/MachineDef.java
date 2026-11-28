package mars.index.sim;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import mars.core.ParameterId;
import mars.gates.Condition;

/**
 * A machine type's effect on the planet, read from {@code data/mars/machines/<name>.json}: what it
 * contributes to which Index parameter, per sol, at standard pace (ARCHITECTURE.md 2.1, 2.16). The
 * optional {@code requires} is a gate-style condition (a parameter comparison, for example {@code
 * mars:temperature >= -20}): while it is false the machine's contributions are 0. It reads the same
 * condition language as the gates, so a data file cannot scale a threshold (invariant I-1).
 */
public record MachineDef(
    String id,
    List<Contribution> contributions,
    Power power,
    int startingGrant,
    Condition requires,
    List<Conversion> conversions) {

  public MachineDef(
      String id,
      List<Contribution> contributions,
      Power power,
      int startingGrant,
      Condition requires) {
    this(id, contributions, power, startingGrant, requires, List.of());
  }

  public MachineDef(String id, List<Contribution> contributions, Power power, int startingGrant) {
    this(id, contributions, power, startingGrant, null, List.of());
  }

  public MachineDef(String id, List<Contribution> contributions, Power power) {
    this(id, contributions, power, 0, null, List.of());
  }

  /**
   * Power use (balance sheet §2b), all optional in the file. {@code drawPerSol}: units per sol a
   * machine needs (its contributions scale with the share of power it gets). {@code supplyPerSol}:
   * units per sol it makes at full output. {@code storage}: units it can hold. {@code
   * startingGrant} (file field, outside power): how many the first player to arrive gets, once.
   */
  public record Power(double drawPerSol, double supplyPerSol, double storage) {
    public static final Power NONE = new Power(0, 0, 0);
  }

  public MachineDef(String id, List<Contribution> contributions) {
    this(id, contributions, Power.NONE);
  }

  /**
   * One contribution: {@code ratePerSol} added to {@code parameter} (negative = a sink). {@code
   * offAfterGate}, when not null, is a gate id: once that gate has latched the contribution is 0
   * (the atmospheric processor adds CO2 only until Phase 3 begins; after that it adds filler gas,
   * rule A).
   */
  public record Contribution(ParameterId parameter, double ratePerSol, String offAfterGate) {
    public Contribution(ParameterId parameter, double ratePerSol) {
      this(parameter, ratePerSol, null);
    }
  }

  /**
   * A conversion moves gas between two Index values (rule A, ARCHITECTURE.md 2.23i). It takes up to
   * {@code kpaPerSol} of {@code from} (a partial pressure in kPa) each sol at full power. Where the
   * gas goes: {@code toO2} true: the same kPa of oxygen (one for one, at constant total pressure);
   * false: it is locked into carbonate rock and counted in the planet's carbonate tally. {@code
   * biomassPerKpa}: biomass units made per kPa moved (0 for none). A conversion never takes more
   * than the air has, so it cannot make gas from nothing.
   */
  public record Conversion(
      ParameterId from, boolean toO2, double kpaPerSol, double biomassPerKpa) {}

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
      String off = null;
      JsonElement og = c.get("offAfterGate");
      if (og != null) {
        if (!og.isJsonPrimitive() || !og.getAsJsonPrimitive().isString()) {
          throw new IllegalArgumentException(file + ": 'offAfterGate' must be a gate id string");
        }
        off = og.getAsString();
      }
      list.add(
          new Contribution(ParameterId.of(string(c, "parameter", file)), rate.getAsDouble(), off));
    }
    List<Conversion> conversions = new ArrayList<>();
    JsonElement cv = o.get("conversions");
    if (cv != null) {
      if (!cv.isJsonArray()) {
        throw new IllegalArgumentException(file + ": 'conversions' must be an array");
      }
      for (JsonElement e : cv.getAsJsonArray()) {
        JsonObject c = e.getAsJsonObject();
        String to = c.has("to") ? string(c, "to", file) : "carbonate";
        if (!to.equals("mars:o2_partial") && !to.equals("carbonate")) {
          throw new IllegalArgumentException(
              file + ": conversion 'to' must be mars:o2_partial or carbonate, not " + to);
        }
        double kpa = optNumber(c, "kpaPerSol", file);
        if (kpa <= 0) {
          throw new IllegalArgumentException(file + ": conversion needs 'kpaPerSol' above 0");
        }
        conversions.add(
            new Conversion(
                ParameterId.of(string(c, "from", file)),
                to.equals("mars:o2_partial"),
                kpa,
                optNumber(c, "biomassPerKpa", file)));
      }
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
    int grant = 0;
    JsonElement g = o.get("startingGrant");
    if (g != null) {
      if (!g.isJsonPrimitive() || !g.getAsJsonPrimitive().isNumber() || g.getAsInt() < 0) {
        throw new IllegalArgumentException(file + ": 'startingGrant' must be a whole number >= 0");
      }
      grant = g.getAsInt();
    }
    Condition requires = null;
    JsonElement rq = o.get("requires");
    if (rq != null) {
      requires = Condition.parse(rq, file);
      java.util.Set<String> states = new java.util.HashSet<>();
      requires.machineStates(states);
      if (!states.isEmpty()) {
        throw new IllegalArgumentException(
            file + ": 'requires' may only compare parameters, not machine states " + states);
      }
    }
    return new MachineDef(id, List.copyOf(list), power, grant, requires, List.copyOf(conversions));
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
