package mars.session;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * The starter chest (data/mars/terrain/starter_chest.json): what the lander leaves the first player
 * on a planet, given with the starting Ovens, once per world. Pure Java: parsing only.
 */
public record StarterChest(String nameKey, String messageKey, List<Entry> items) {
  /** One stack: an item id and a count. */
  public record Entry(String id, int count) {}

  public static StarterChest parse(Reader reader, String source) {
    JsonObject o = JsonParser.parseReader(reader).getAsJsonObject();
    List<Entry> items = new ArrayList<>();
    JsonArray arr = o.getAsJsonArray("items");
    if (arr == null || arr.isEmpty()) {
      throw new IllegalArgumentException(source + ": items must not be empty");
    }
    for (JsonElement e : arr) {
      JsonObject i = e.getAsJsonObject();
      int count = i.get("count").getAsInt();
      if (count < 1 || count > 64) {
        throw new IllegalArgumentException(source + ": count must be 1 to 64");
      }
      items.add(new Entry(i.get("id").getAsString(), count));
    }
    return new StarterChest(
        o.get("name_key").getAsString(), o.get("message_key").getAsString(), List.copyOf(items));
  }
}
