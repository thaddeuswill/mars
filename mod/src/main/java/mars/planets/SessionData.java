package mars.planets;

import mars.session.SessionClock;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

/**
 * The server's session clock state (paused flag, session number), saved in the overworld's data
 * folder so a pause survives a restart (ARCHITECTURE.md 7.2). Deviation from the 7.2 text, which
 * names {@code session.json}: this build keeps it in world save data (one less file to keep in
 * step); the Mars data folder arrives with the learning layer.
 */
public final class SessionData extends SavedData {
  static final String NAME = "mars_session";
  private final SessionClock clock;

  private SessionData(SessionClock clock) {
    this.clock = clock;
  }

  public SessionClock clock() {
    return clock;
  }

  public static SessionData get(MinecraftServer server) {
    return server
        .overworld()
        .getDataStorage()
        .computeIfAbsent(
            new SavedData.Factory<>(
                () -> new SessionData(new SessionClock(false)), SessionData::load),
            NAME);
  }

  static SessionData load(CompoundTag tag, HolderLookup.Provider registries) {
    return new SessionData(
        SessionClock.restore(
            tag.getInt("schema"), tag.getBoolean("paused"), tag.getInt("sessionNumber")));
  }

  @Override
  public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
    tag.putInt("schema", SessionClock.CURRENT_SCHEMA);
    tag.putBoolean("paused", clock.paused());
    tag.putInt("sessionNumber", clock.sessionNumber());
    return tag;
  }
}
