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
  public record Defs(
      IndexData.Loaded index,
      Map<String, MachineDef> machines,
      mars.hazards.dust.PanelDustTuning dust,
      java.util.List<mars.gates.GateDef> gates,
      mars.index.sim.FrostSink.Def frost,
      mars.life.LifeParams life,
      mars.hazards.storm.StormTuning storm) {
    public mars.hazards.HazardParams hazards() {
      return new mars.hazards.HazardParams(dust, storm);
    }
  }

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
      mars.hazards.dust.PanelDustTuning dust;
      ResourceLocation dustFile =
          ResourceLocation.fromNamespaceAndPath("mars", "hazards/panel_dust.json");
      try (java.io.Reader r = manager.openAsReader(dustFile)) {
        dust = mars.hazards.dust.PanelDustTuning.parse(r, "data/mars/hazards/panel_dust.json");
      } catch (java.io.IOException e) {
        throw new IllegalStateException("Cannot read data/mars/hazards/panel_dust.json", e);
      }
      java.util.List<mars.gates.GateDef> gates = new java.util.ArrayList<>();
      files(manager, "gates")
          .forEach((file, json) -> gates.add(mars.gates.GateDef.parse(json, file)));
      mars.index.sim.FrostSink.Def frost;
      ResourceLocation frostFile =
          ResourceLocation.fromNamespaceAndPath("mars", "sinks/polar_frost.json");
      try (java.io.Reader r = manager.openAsReader(frostFile)) {
        frost =
            mars.index.sim.FrostSink.Def.parse(
                GSON.fromJson(r, JsonObject.class), "data/mars/sinks/polar_frost.json");
      } catch (java.io.IOException e) {
        throw new IllegalStateException("Cannot read data/mars/sinks/polar_frost.json", e);
      }
      mars.life.LifeParams life;
      ResourceLocation lifeFile =
          ResourceLocation.fromNamespaceAndPath("mars", "life_support.json");
      try (java.io.Reader r = manager.openAsReader(lifeFile)) {
        life =
            mars.life.LifeParams.parse(
                GSON.fromJson(r, JsonObject.class), "data/mars/life_support.json");
      } catch (java.io.IOException e) {
        throw new IllegalStateException("Cannot read data/mars/life_support.json", e);
      }
      mars.hazards.storm.StormTuning storm;
      ResourceLocation stormFile =
          ResourceLocation.fromNamespaceAndPath("mars", "hazards/dust_storm.json");
      try (java.io.Reader r = manager.openAsReader(stormFile)) {
        storm = mars.hazards.storm.StormTuning.parse(r, "data/mars/hazards/dust_storm.json");
      } catch (java.io.IOException e) {
        throw new IllegalStateException("Cannot read data/mars/hazards/dust_storm.json", e);
      }
      return new Defs(
          index, Map.copyOf(machines), dust, java.util.List.copyOf(gates), frost, life, storm);
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
