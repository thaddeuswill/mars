package mars.learning;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import mars.core.Guidance;

/**
 * Which Monitor lines state an estimate, read from {@code data/mars/monitor/lines.json} (CR-T8).
 * {@code reveals} lists the value ids a line states or implies; {@code hidden_under} lists the
 * guidance levels that never see it. A line is not drawn while a forecast about one of its revealed
 * values is open, and never under a level in {@code hidden_under}. Pure Java; one set lookup per
 * line per Monitor open.
 */
public record LineRules(Map<String, Rule> lines) {

  /** One line's declaration. */
  public record Rule(Set<String> reveals, Set<Guidance> hiddenUnder) {}

  public static final LineRules NONE = new LineRules(Map.of());

  public static LineRules parse(JsonObject root, String file) {
    if (!root.has("schema") || root.get("schema").getAsInt() != 1) {
      throw new IllegalArgumentException(file + ": schema must be 1");
    }
    Map<String, Rule> out = new LinkedHashMap<>();
    for (var e : root.getAsJsonObject("lines").entrySet()) {
      JsonObject o = e.getValue().getAsJsonObject();
      Set<String> reveals = new LinkedHashSet<>();
      if (o.has("reveals")) {
        for (JsonElement r : (JsonArray) o.get("reveals")) {
          reveals.add(r.getAsString());
        }
      }
      Set<Guidance> hidden = EnumSet.noneOf(Guidance.class);
      if (o.has("hidden_under")) {
        for (JsonElement g : (JsonArray) o.get("hidden_under")) {
          hidden.add(
              Guidance.parse(g.getAsString())
                  .orElseThrow(
                      () ->
                          new IllegalArgumentException(
                              file + ": unknown guidance level " + g.getAsString())));
        }
      }
      out.put(e.getKey(), new Rule(Set.copyOf(reveals), Set.copyOf(hidden)));
    }
    return new LineRules(Map.copyOf(out));
  }

  public boolean declared(String lineKey) {
    return lines.containsKey(lineKey);
  }

  /**
   * May this player see this line? A line with no declaration is always shown (the content test
   * makes sure no estimate line is undeclared).
   *
   * @param openAsked value ids that an open forecast of this player asks about
   */
  public boolean visible(String lineKey, Guidance guidance, Set<String> openAsked) {
    Rule r = lines.get(lineKey);
    if (r == null) {
      return true;
    }
    if (r.hiddenUnder().contains(guidance)) {
      return false;
    }
    for (String v : r.reveals()) {
      if (openAsked.contains(v)) {
        return false;
      }
    }
    return true;
  }
}
