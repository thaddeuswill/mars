package mars.hull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

/** Where a planet's starting dome stands, saved with the planet's dimension; null until placed. */
public final class StarterDomeData extends SavedData {
  static final String NAME = "mars_start_dome";
  private BlockPos origin;

  public static SavedData.Factory<StarterDomeData> factory() {
    return new SavedData.Factory<>(StarterDomeData::new, StarterDomeData::load);
  }

  /** The dome's north-west floor corner, or null when it has not been placed. */
  public BlockPos origin() {
    return origin;
  }

  void setOrigin(BlockPos origin) {
    this.origin = origin;
    setDirty();
  }

  static StarterDomeData load(CompoundTag tag, HolderLookup.Provider registries) {
    StarterDomeData d = new StarterDomeData();
    if (tag.contains("origin")) {
      d.origin = BlockPos.of(tag.getLong("origin"));
    }
    return d;
  }

  @Override
  public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
    if (origin != null) {
      tag.putLong("origin", origin.asLong());
    }
    return tag;
  }
}
