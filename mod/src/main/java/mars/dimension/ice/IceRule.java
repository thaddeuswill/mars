package mars.dimension.ice;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The ice-to-water rule as an ordered list of data rules; the first match wins (ARCHITECTURE.md
 * 2.5). Pure Java: callers pass the planet's pressure, the block's local temperature, and the
 * boiling point at that pressure ({@code mars:boiling_point}, NaN below the triple point).
 */
public final class IceRule {
  private final List<Rule> rules;
  private final double shortPuddleDrySols;

  private IceRule(List<Rule> rules, double shortPuddleDrySols) {
    this.rules = List.copyOf(rules);
    this.shortPuddleDrySols = shortPuddleDrySols;
  }

  /** How long a short-lived puddle lasts before it dries (an invented balance number). */
  public double shortPuddleDrySols() {
    return shortPuddleDrySols;
  }

  public IceResult evaluate(double pressureKpa, double localTempC, double boilingPointC) {
    for (Rule r : rules) {
      if (r.matches(pressureKpa, localTempC, boilingPointC)) {
        return r.result;
      }
    }
    throw new IllegalStateException(
        "No ice rule matched p=" + pressureKpa + " kPa, t=" + localTempC + " °C (add a catch-all)");
  }

  public static IceRule parse(Reader json, String file) {
    JsonObject root = JsonParser.parseReader(json).getAsJsonObject();
    JsonElement arr = root.get("rules");
    if (arr == null || !arr.isJsonArray()) {
      throw new IllegalArgumentException(file + ": missing array field 'rules'");
    }
    List<Rule> rules = new ArrayList<>();
    int i = 0;
    for (JsonElement e : arr.getAsJsonArray()) {
      String where = file + ": rules[" + i++ + "]";
      JsonObject o = e.getAsJsonObject();
      IceResult result = IceResult.byId(o.get("result").getAsString(), where);
      JsonObject when = o.has("when") ? o.getAsJsonObject("when") : new JsonObject();
      Double tempAtMost = null;
      Double pressureBelow = null;
      boolean atOrAboveBoiling = false;
      for (Map.Entry<String, JsonElement> c : when.entrySet()) {
        switch (c.getKey()) {
          case "local_temp_c_at_most" -> tempAtMost = c.getValue().getAsDouble();
          case "pressure_kpa_below" -> pressureBelow = c.getValue().getAsDouble();
          case "local_temp_at_or_above_boiling_point" ->
              atOrAboveBoiling = c.getValue().getAsBoolean();
          default ->
              throw new IllegalArgumentException(
                  where + ".when: unknown condition '" + c.getKey() + "'");
        }
      }
      rules.add(new Rule(result, tempAtMost, pressureBelow, atOrAboveBoiling));
    }
    JsonElement dry = root.get("short_puddle_dry_sols");
    if (dry == null) {
      throw new IllegalArgumentException(file + ": missing number field 'short_puddle_dry_sols'");
    }
    return new IceRule(rules, dry.getAsDouble());
  }

  private record Rule(
      IceResult result, Double tempAtMost, Double pressureBelow, boolean atOrAboveBoiling) {
    boolean matches(double p, double t, double bp) {
      if (tempAtMost != null && !(t <= tempAtMost)) {
        return false;
      }
      if (pressureBelow != null && !(p < pressureBelow)) {
        return false;
      }
      // NaN (no liquid phase) never satisfies the comparison.
      return !atOrAboveBoiling || t >= bp;
    }
  }
}
