package mars.dimension;

import java.nio.file.Path;
import mars.terrain.LegacyDetect;
import mars.terrain.TerrainMode;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.LevelResource;

/**
 * The world's terrain mode, saved in the overworld's data folder ({@code mars_terrain.dat}). The
 * first time a world loads its Mars level the mode is decided: a world that already holds Mars data
 * keeps the old flat slab, any other world gets the W1 noise terrain. After that the saved value
 * never changes. This is how "new terrain applies to new worlds only" is guaranteed: the datapack's
 * dimension file is the same for every world, so the world itself must remember.
 */
public final class TerrainModeData extends SavedData {
  static final String NAME = "mars_terrain";
  private final TerrainMode mode;

  private TerrainModeData(TerrainMode mode) {
    this.mode = mode;
  }

  public TerrainMode mode() {
    return mode;
  }

  /** Reads the saved mode, or decides and saves it. Call on the server thread. */
  public static TerrainMode resolve(MinecraftServer server) {
    Path root = server.getWorldPath(LevelResource.ROOT);
    return server
        .overworld()
        .getDataStorage()
        .computeIfAbsent(new SavedData.Factory<>(() -> decide(root), TerrainModeData::load), NAME)
        .mode;
  }

  private static TerrainModeData decide(Path root) {
    TerrainMode m = LegacyDetect.hasMarsData(root) ? TerrainMode.LEGACY_FLAT : TerrainMode.NOISE_V1;
    TerrainModeData d = new TerrainModeData(m);
    d.setDirty();
    return d;
  }

  static TerrainModeData load(CompoundTag tag, HolderLookup.Provider registries) {
    TerrainMode m = TerrainMode.fromId(tag.getString("mode"));
    if (m == null) {
      // An unreadable value must not turn an old world into a new one.
      m = TerrainMode.LEGACY_FLAT;
    }
    return new TerrainModeData(m);
  }

  @Override
  public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
    tag.putInt("schema", 1);
    tag.putString("mode", mode.id());
    return tag;
  }
}
