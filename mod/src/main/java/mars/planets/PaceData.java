package mars.planets;

import mars.index.sim.Pace;
import mars.index.sim.PaceConfig;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

/**
 * The world's pace choice (standard or classroom, and the group size), saved in the overworld's
 * data folder like {@link SessionData}. Only the choice is saved; the numbers come from {@code
 * data/mars/pace.json} (ARCHITECTURE.md 2.20). Default: standard, the preset's own group size.
 */
public final class PaceData extends SavedData {
  static final String NAME = "mars_pace";
  static final int SCHEMA = 1;

  private PaceConfig.Mode mode = PaceConfig.Mode.STANDARD;
  private int groupSize; // 0 = the preset's own

  public static PaceData get(MinecraftServer server) {
    return server
        .overworld()
        .getDataStorage()
        .computeIfAbsent(new SavedData.Factory<>(PaceData::new, PaceData::load), NAME);
  }

  public PaceConfig.Mode mode() {
    return mode;
  }

  public Pace pace(PaceConfig config) {
    return config.pace(mode, groupSize == 0 ? null : groupSize);
  }

  void set(PaceConfig.Mode mode, int groupSize) {
    this.mode = mode;
    this.groupSize = groupSize;
    setDirty();
  }

  public static PaceData load(CompoundTag tag, HolderLookup.Provider registries) {
    PaceData d = new PaceData();
    try {
      d.mode = PaceConfig.Mode.parse(tag.getString("mode"));
    } catch (IllegalArgumentException unknown) {
      d.mode = PaceConfig.Mode.STANDARD; // an unknown or missing mode falls back to standard
    }
    d.groupSize = Math.max(0, tag.getInt("groupSize"));
    return d;
  }

  @Override
  public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
    tag.putInt("schema", SCHEMA);
    tag.putString("mode", mode.name());
    tag.putInt("groupSize", groupSize);
    return tag;
  }
}
