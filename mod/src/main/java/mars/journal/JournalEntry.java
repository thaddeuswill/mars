package mars.journal;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

/**
 * One journal entry, read from {@code data/mars/journal/<name>.json} (landing-spec 5.1). The same
 * files feed the Stage 4 journal screen. Pure.
 *
 * <p>{@code triggers} are tokens (any one unlocks the entry): {@code join}, {@code
 * step:<STEP>}, {@code landing_over}, {@code briefing_shown}, {@code gate:<id>}, {@code
 * placed:<machine id>}. {@code scope} is "player" (unlocked by that player's own action) or
 * "planet" (every player on the planet gets it, and a player who was offline gets it on join).
 * {@code reveals} names the forecasts whose answer the text could give away (CR-F9); empty when
 * none.
 */
public record JournalEntry(
    String id,
    String section,
    String title,
    List<String> triggers,
    boolean planetScope,
    List<String> goalIds,
    String text,
    String earthLine,
    String fictionNote,
    String hsMore,
    List<String> reveals,
    List<String> names) {

  /** Sections in the order the contents page lists them. */
  public static final List<String> SECTIONS = List.of("Briefings", "Shift notes", "Game rules");

  public static JournalEntry parse(JsonObject o, String file) {
    JsonObject text = o.has("text") ? o.getAsJsonObject("text") : new JsonObject();
    String id = str(o, "id", file);
    String ms = str(text, "ms", file + " " + id);
    String section = str(o, "section", file);
    if (!SECTIONS.contains(section)) {
      throw new IllegalStateException(file + ": unknown section \"" + section + "\"");
    }
    List<String> triggers = strings(o, "trigger");
    if (triggers.isEmpty()) {
      throw new IllegalStateException(file + ": an entry needs at least one trigger");
    }
    String scope = o.has("scope") ? o.get("scope").getAsString() : "player";
    if (!scope.equals("player") && !scope.equals("planet")) {
      throw new IllegalStateException(file + ": scope must be player or planet");
    }
    return new JournalEntry(
        id,
        section,
        str(o, "title", file),
        triggers,
        scope.equals("planet"),
        strings(o, "goal_ids"),
        ms,
        opt(o, "earth_line"),
        opt(o, "fiction_note"),
        opt(text, "hs_more"),
        strings(o, "reveals"),
        strings(o, "names"));
  }

  private static String str(JsonObject o, String key, String file) {
    if (!o.has(key) || !o.get(key).isJsonPrimitive()) {
      throw new IllegalStateException(file + ": missing \"" + key + "\"");
    }
    return o.get(key).getAsString();
  }

  private static String opt(JsonObject o, String key) {
    return o.has(key) && o.get(key).isJsonPrimitive() ? o.get(key).getAsString() : null;
  }

  private static List<String> strings(JsonObject o, String key) {
    List<String> out = new ArrayList<>();
    if (o.has(key)) {
      JsonElement e = o.get(key);
      if (e.isJsonArray()) {
        JsonArray a = e.getAsJsonArray();
        a.forEach(x -> out.add(x.getAsString()));
      } else {
        out.add(e.getAsString());
      }
    }
    return List.copyOf(out);
  }
}
