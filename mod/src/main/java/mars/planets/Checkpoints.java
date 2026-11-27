package mars.planets;

import java.util.Optional;
import mars.index.sim.CheckpointList;
import mars.index.sim.PlanetCheckpoint;
import mars.index.sim.PlanetState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Take and restore planet checkpoints (ARCHITECTURE.md 7.4a). Taking is one NBT write of one
 * planet; restoring replaces the planet's simulation state and keeps its machine registry (the
 * blocks are not rolled back). Not covered: blocks, items, player inventory, third-party machines.
 */
public final class Checkpoints {
  private static final Logger LOG = LoggerFactory.getLogger("mars");

  /** The reserved name that restores a planet to its start values. */
  public static final String START = "start";

  private Checkpoints() {}

  /** Save a checkpoint of this planet level. Returns the name of any checkpoint dropped. */
  public static String take(ServerLevel level, String name, String actor) {
    String id = level.dimension().location().toString();
    PlanetData d = PlanetManager.data(level);
    CompoundTag tag = d.save(new CompoundTag(), level.registryAccess());
    CheckpointData store = CheckpointData.get(level.getServer());
    String dropped = store.of(id).save(name, d.sim().model().sols(), tag);
    store.setDirty();
    LOG.info(
        "checkpoint.saved {} on {} at sol {} by {}{}",
        name,
        id,
        d.sim().model().sols(),
        actor,
        dropped == null ? "" : " (dropped oldest: " + dropped + ")");
    return dropped;
  }

  /** Take an automatic checkpoint on every planet level of the server (session start). */
  public static void takeAll(
      net.minecraft.server.MinecraftServer server, String name, String actor) {
    for (ServerLevel level : server.getAllLevels()) {
      if (PlanetManager.isPlanet(level)) {
        take(level, name, actor);
      }
    }
  }

  /**
   * Restore a named checkpoint, or the start state for {@link #START}. Returns false if there is no
   * such checkpoint.
   */
  public static boolean restore(ServerLevel level, String name, String actor) {
    String id = level.dimension().location().toString();
    PlanetData current = PlanetManager.data(level);
    PlanetState snapshot;
    double sol;
    if (START.equals(name)) {
      snapshot =
          PlanetData.fresh(id, PlanetManager.pace(level), PlanetManager.seed(level))
              .sim()
              .toState();
      sol = 0;
    } else {
      Optional<CheckpointList.Entry<CompoundTag>> e =
          CheckpointData.get(level.getServer()).of(id).get(name);
      if (e.isEmpty()) {
        return false;
      }
      snapshot = PlanetData.fromNbt(e.get().payload());
      sol = e.get().sol();
    }
    double before = current.sim().model().sols();
    PlanetState merged = PlanetCheckpoint.restoreOver(snapshot, current.sim().toState());
    PlanetData restored =
        PlanetData.fromState(id, merged, PlanetManager.pace(level), PlanetManager.seed(level));
    level.getDataStorage().set(PlanetData.NAME, restored);
    restored.setDirty();
    for (var player : level.players()) {
      mars.sky.SkySync.sendTo(player);
    }
    LOG.info(
        "checkpoint.restored {} on {} by {}: sol {} back to sol {}", name, id, actor, before, sol);
    return true;
  }
}
