package mars.story;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mars.journal.Journal;
import mars.journal.JournalEntry;
import mars.landing.LandingConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

/**
 * The Landing's data and the journal's entries ({@code data/mars/landing.json}, {@code
 * data/mars/journal/*.json}), loaded by the datapack reload like the Index definitions. The holder
 * is replaced whole on a reload.
 */
public final class StoryDefinitions {
  public record Defs(LandingConfig landing, Journal journal) {}

  private static volatile Defs current;

  private StoryDefinitions() {}

  public static boolean isLoaded() {
    return current != null;
  }

  public static Defs require() {
    Defs d = current;
    if (d == null) {
      throw new IllegalStateException("Mars story data is not loaded yet.");
    }
    return d;
  }

  public static final class Listener extends SimplePreparableReloadListener<Defs> {
    private static final Gson GSON = new Gson();

    @Override
    protected Defs prepare(ResourceManager manager, ProfilerFiller profiler) {
      LandingConfig landing;
      try (java.io.Reader r =
          manager.openAsReader(ResourceLocation.fromNamespaceAndPath("mars", "landing.json"))) {
        landing = LandingConfig.parse(GSON.fromJson(r, JsonObject.class), "data/mars/landing.json");
      } catch (java.io.IOException e) {
        throw new IllegalStateException("Cannot read data/mars/landing.json", e);
      }
      Map<ResourceLocation, JsonElement> raw = new HashMap<>();
      SimpleJsonResourceReloadListener.scanDirectory(manager, "journal", GSON, raw);
      List<JournalEntry> entries = new ArrayList<>();
      raw.entrySet().stream()
          .filter(e -> e.getKey().getNamespace().equals("mars"))
          .sorted(Map.Entry.comparingByKey())
          .forEach(
              e ->
                  entries.add(
                      JournalEntry.parse(e.getValue().getAsJsonObject(), e.getKey().toString())));
      return new Defs(landing, new Journal(entries));
    }

    @Override
    protected void apply(Defs defs, ResourceManager manager, ProfilerFiller profiler) {
      current = defs;
    }
  }
}
