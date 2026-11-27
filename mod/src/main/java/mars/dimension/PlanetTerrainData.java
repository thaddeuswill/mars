package mars.dimension;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;
import mars.terrain.TerrainParams;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

/**
 * Planet terrain data: {@code data/mars/planets/<planet>.json}, keyed by the dimension id ({@code
 * mars:planet_1}). Replaced whole on each data reload; worldgen threads read an immutable map.
 */
public final class PlanetTerrainData {
  private static volatile Map<ResourceLocation, TerrainParams> current = Map.of();

  private PlanetTerrainData() {}

  /** The terrain numbers for a planet dimension, or null if it has no planet file. */
  public static TerrainParams get(ResourceLocation dimension) {
    return current.get(dimension);
  }

  /** Test seam: the loaded map. */
  public static Map<ResourceLocation, TerrainParams> all() {
    return current;
  }

  /** Reads the files off the game thread, then swaps the holder on apply. */
  public static final class Listener
      extends SimplePreparableReloadListener<Map<ResourceLocation, TerrainParams>> {
    private static final Gson GSON = new Gson();

    @Override
    protected Map<ResourceLocation, TerrainParams> prepare(
        ResourceManager manager, ProfilerFiller profiler) {
      Map<ResourceLocation, JsonElement> raw = new HashMap<>();
      SimpleJsonResourceReloadListener.scanDirectory(manager, "planets", GSON, raw);
      Map<ResourceLocation, TerrainParams> out = new HashMap<>();
      for (Map.Entry<ResourceLocation, JsonElement> e : raw.entrySet()) {
        if (e.getKey().getNamespace().equals("mars")) {
          JsonObject o = e.getValue().getAsJsonObject();
          out.put(e.getKey(), TerrainParams.parse(o, "data/mars/planets/" + e.getKey().getPath()));
        }
      }
      return Map.copyOf(out);
    }

    @Override
    protected void apply(
        Map<ResourceLocation, TerrainParams> data,
        ResourceManager manager,
        ProfilerFiller profiler) {
      current = data;
    }
  }
}
