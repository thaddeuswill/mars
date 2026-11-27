package mars.index;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;
import mars.index.sim.IndexData;
import mars.index.sim.MachineDef;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

/**
 * The Index parameter and machine definitions, loaded from {@code data/mars/parameters/} and {@code
 * data/mars/machines/} by the datapack reload (ARCHITECTURE.md 3: content lives in data). The
 * holder is replaced whole on every reload; planets already running keep the definitions they were
 * built with until they are next loaded.
 */
public final class IndexDefinitions {
  /** Everything the pure simulation needs from data. */
  public record Defs(IndexData.Loaded index, Map<String, MachineDef> machines) {}

  private static volatile Defs current;

  private IndexDefinitions() {}

  public static boolean isLoaded() {
    return current != null;
  }

  public static Defs require() {
    Defs d = current;
    if (d == null) {
      throw new IllegalStateException(
          "Mars parameter and machine data are not loaded yet (the datapack reload hasn't run).");
    }
    return d;
  }

  /** Reads both directories off the game thread, then swaps the holder on apply. */
  public static final class Listener extends SimplePreparableReloadListener<Defs> {
    private static final Gson GSON = new Gson();

    @Override
    protected Defs prepare(ResourceManager manager, ProfilerFiller profiler) {
      Map<String, JsonObject> parameters = files(manager, "parameters");
      Map<String, JsonObject> machineFiles = files(manager, "machines");
      IndexData.Loaded index = IndexData.load(parameters, "data/mars/parameters");
      Map<String, MachineDef> machines = new HashMap<>();
      machineFiles.forEach(
          (file, json) -> {
            MachineDef d = MachineDef.parse(json, file);
            machines.put(d.id(), d);
          });
      return new Defs(index, Map.copyOf(machines));
    }

    @Override
    protected void apply(Defs defs, ResourceManager manager, ProfilerFiller profiler) {
      current = defs;
    }

    private static Map<String, JsonObject> files(ResourceManager manager, String directory) {
      Map<ResourceLocation, JsonElement> raw = new HashMap<>();
      SimpleJsonResourceReloadListener.scanDirectory(manager, directory, GSON, raw);
      Map<String, JsonObject> out = new HashMap<>();
      raw.forEach(
          (id, json) -> {
            if (id.getNamespace().equals("mars")) {
              out.put(id.getPath() + ".json", json.getAsJsonObject());
            }
          });
      return out;
    }
  }
}
