package mars.planets;

import mars.session.StartingGrant;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

/** The once-per-world starting-machines flag, saved in the overworld's data folder. */
public final class GrantData extends SavedData {
  static final String NAME = "mars_grant";
  private final StartingGrant grant;

  private GrantData(StartingGrant grant) {
    this.grant = grant;
  }

  public StartingGrant grant() {
    return grant;
  }

  public static GrantData get(MinecraftServer server) {
    return server
        .overworld()
        .getDataStorage()
        .computeIfAbsent(
            new SavedData.Factory<>(() -> new GrantData(new StartingGrant(false)), GrantData::load),
            NAME);
  }

  /**
   * Self-test only: forget the grant (the GameTest world keeps its saved data between runs).
   * Refuses to run without the test seam property, so a real world can never be reset.
   */
  public static GrantData resetForSelfTest(MinecraftServer server) {
    if (!Boolean.getBoolean("mars.selftest.anyLevelIsPlanet")) {
      throw new IllegalStateException("resetForSelfTest is only for the GameTest server");
    }
    GrantData fresh = new GrantData(new StartingGrant(false));
    server.overworld().getDataStorage().set(NAME, fresh);
    fresh.setDirty();
    return fresh;
  }

  static GrantData load(CompoundTag tag, HolderLookup.Provider registries) {
    return new GrantData(new StartingGrant(tag.getBoolean("granted")));
  }

  @Override
  public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
    tag.putBoolean("granted", grant.granted());
    return tag;
  }
}
