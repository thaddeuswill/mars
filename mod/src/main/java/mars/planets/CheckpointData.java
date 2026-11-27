package mars.planets;

import java.util.LinkedHashMap;
import java.util.Map;
import mars.index.sim.CheckpointList;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

/**
 * Saved planet checkpoints (ARCHITECTURE.md 7.4a), kept per planet in the overworld's data folder,
 * so they live inside the world and are restored or lost together with it. Each entry is the
 * planet's own saved NBT (a few KB: values, gates, a 120-sample history). At most {@link #MAX} per
 * planet; the oldest is dropped.
 */
public final class CheckpointData extends SavedData {
  static final String NAME = "mars_checkpoints";
  public static final int MAX = 8;

  private final Map<String, CheckpointList<CompoundTag>> byPlanet = new LinkedHashMap<>();

  public static CheckpointData get(MinecraftServer server) {
    return server
        .overworld()
        .getDataStorage()
        .computeIfAbsent(new SavedData.Factory<>(CheckpointData::new, CheckpointData::load), NAME);
  }

  public CheckpointList<CompoundTag> of(String planetId) {
    return byPlanet.computeIfAbsent(planetId, k -> new CheckpointList<>(MAX));
  }

  public static CheckpointData load(CompoundTag tag, HolderLookup.Provider registries) {
    CheckpointData d = new CheckpointData();
    CompoundTag planets = tag.getCompound("planets");
    for (String id : planets.getAllKeys()) {
      ListTag list = planets.getList(id, Tag.TAG_COMPOUND);
      for (int i = 0; i < list.size(); i++) {
        CompoundTag e = list.getCompound(i);
        d.of(id).save(e.getString("name"), e.getDouble("sol"), e.getCompound("state"));
      }
    }
    return d;
  }

  @Override
  public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
    CompoundTag planets = new CompoundTag();
    byPlanet.forEach(
        (id, list) -> {
          ListTag l = new ListTag();
          for (var e : list.list()) {
            CompoundTag t = new CompoundTag();
            t.putString("name", e.name());
            t.putDouble("sol", e.sol());
            t.put("state", e.payload());
            l.add(t);
          }
          planets.put(id, l);
        });
    tag.put("planets", planets);
    return tag;
  }
}
