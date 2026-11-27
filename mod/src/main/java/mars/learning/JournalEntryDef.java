package mars.learning;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

/**
 * One journal entry, read from {@code data/mars/story/journal/<name>.json}. It unlocks when the
 * gate it names has latched. {@code reveals} (CR-F9) and {@code names} (CR-H5) are checked by
 * content tests. {@code status} is free text for the review state (null when absent).
 */
public record JournalEntryDef(
    String id,
    int order,
    String gate,
    String titleKey,
    String textKey,
    List<String> goalIds,
    List<String> reveals,
    List<String> names,
    String status) {

  public static JournalEntryDef parse(JsonObject o, String file) {
    if (!o.has("schema") || o.get("schema").getAsInt() != 1) {
      throw new IllegalArgumentException(file + ": schema must be 1");
    }
    if (!o.has("reveals")) {
      throw new IllegalArgumentException(file + ": reveals is required (CR-F9), empty is fine");
    }
    JsonObject unlock = o.getAsJsonObject("unlock");
    if (unlock == null || !unlock.has("gate")) {
      throw new IllegalArgumentException(file + ": unlock.gate is required");
    }
    return new JournalEntryDef(
        text(o, "id", file),
        o.has("order") ? o.get("order").getAsInt() : 0,
        unlock.get("gate").getAsString(),
        text(o, "title_key", file),
        text(o, "text_key", file),
        list(o, "goal_ids"),
        list(o, "reveals"),
        list(o, "names"),
        o.has("status") ? o.get("status").getAsString() : null);
  }

  private static String text(JsonObject o, String field, String file) {
    if (!o.has(field)) {
      throw new IllegalArgumentException(file + ": missing \"" + field + "\"");
    }
    return o.get(field).getAsString();
  }

  private static List<String> list(JsonObject o, String field) {
    List<String> out = new ArrayList<>();
    if (o.has(field)) {
      for (JsonElement e : (JsonArray) o.get(field)) {
        out.add(e.getAsString());
      }
    }
    return List.copyOf(out);
  }
}
