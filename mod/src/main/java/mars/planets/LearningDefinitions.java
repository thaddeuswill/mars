package mars.planets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mars.learning.JournalEntryDef;
import mars.learning.LineRules;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

/**
 * The learning-layer data: the Monitor line declarations ({@code data/mars/monitor/lines.json},
 * CR-T8) and the journal entries ({@code data/mars/story/journal/}). Replaced whole on each data
 * reload, like {@link mars.index.IndexDefinitions}.
 */
public final class LearningDefinitions {
  /** Everything read from data. */
  public record Defs(LineRules lines, List<JournalEntryDef> journal) {}

  private static volatile Defs current = new Defs(LineRules.NONE, List.of());

  private LearningDefinitions() {}

  public static Defs get() {
    return current;
  }

  /** Reads the files off the game thread, then swaps the holder on apply. */
  public static final class Listener extends SimplePreparableReloadListener<Defs> {
    private static final Gson GSON = new Gson();

    @Override
    protected Defs prepare(ResourceManager manager, ProfilerFiller profiler) {
      LineRules lines;
      ResourceLocation file = ResourceLocation.fromNamespaceAndPath("mars", "monitor/lines.json");
      try (Reader r = manager.openAsReader(file)) {
        lines = LineRules.parse(GSON.fromJson(r, JsonObject.class), "data/mars/monitor/lines.json");
      } catch (IOException e) {
        throw new IllegalStateException("Cannot read data/mars/monitor/lines.json", e);
      }
      Map<ResourceLocation, JsonElement> raw = new HashMap<>();
      SimpleJsonResourceReloadListener.scanDirectory(manager, "story/journal", GSON, raw);
      List<JournalEntryDef> journal =
          raw.entrySet().stream()
              .filter(e -> e.getKey().getNamespace().equals("mars"))
              .map(
                  e ->
                      JournalEntryDef.parse(
                          e.getValue().getAsJsonObject(), e.getKey().getPath() + ".json"))
              .toList();
      return new Defs(lines, List.copyOf(journal));
    }

    @Override
    protected void apply(Defs defs, ResourceManager manager, ProfilerFiller profiler) {
      current = defs;
    }
  }
}
