package mars.session;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * The orbital resupply request rule (docs/design/recipe-inputs-ruling.md section 6): the pass timer
 * read from data/mars/terrain/resupply_request.json, and the refill list worked out from a gate's
 * drop. Pure Java, no Minecraft. A refill is half of each line of the latest gate's drop, rounded
 * up, so every line keeps at least 1 and the gate's key part is never left out. It is tech only:
 * nothing here knows about the Planetary Index, and nothing a release gives can change one.
 */
public record Resupply(double passSols) {
  public Resupply {
    if (!(passSols > 0.0)) {
      throw new IllegalArgumentException("pass_sols must be above 0");
    }
  }

  public static Resupply parse(Reader reader, String source) {
    JsonObject o = JsonParser.parseReader(reader).getAsJsonObject();
    if (!o.has("pass_sols")) {
      throw new IllegalArgumentException(source + ": missing pass_sols");
    }
    return new Resupply(o.get("pass_sols").getAsDouble());
  }

  /** Half of every line, rounded up (never below 1 for a line that is in the drop). */
  public static List<StarterChest.Entry> refill(List<StarterChest.Entry> drop) {
    List<StarterChest.Entry> out = new ArrayList<>();
    for (StarterChest.Entry e : drop) {
      out.add(new StarterChest.Entry(e.id(), Math.max(1, (e.count() + 1) / 2)));
    }
    return List.copyOf(out);
  }
}
