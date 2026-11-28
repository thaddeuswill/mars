package mars.planets;

import mars.core.Planets;
import mars.index.IndexDefinitions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Finds a level's planet and routes machine place/remove events to it (ARCHITECTURE.md 1.3, 6). A
 * level is a planet when its dimension is in the {@code mars} namespace. No state is kept here: the
 * data lives in the level's saved data.
 */
public final class PlanetManager {
  private static final Logger LOG = LoggerFactory.getLogger("mars");

  /** Test seam, set only by the gameTestServer run configuration (docs/tech/stage2-plan.md §4). */
  private static final boolean ANY_LEVEL_IS_PLANET =
      Boolean.getBoolean("mars.selftest.anyLevelIsPlanet");

  private PlanetManager() {}

  public static boolean isPlanet(Level level) {
    return Planets.isPlanetNamespace(
        level.dimension().location().getNamespace(), ANY_LEVEL_IS_PLANET);
  }

  /** The planet's data, created from the start values on first use. Only call for planet levels. */
  public static PlanetData data(ServerLevel level) {
    return level
        .getDataStorage()
        .computeIfAbsent(
            PlanetData.factory(level.dimension().location().toString(), pace(level), seed(level)),
            PlanetData.NAME);
  }

  /** The world seed, which seeds each planet's storm schedule (with the planet's name). */
  public static long seed(ServerLevel level) {
    return level.getSeed();
  }

  /** This world's pace (standard or classroom, chosen by an operator; default standard). */
  public static mars.index.sim.Pace pace(ServerLevel level) {
    return PaceData.get(level.getServer()).pace(IndexDefinitions.require().pace());
  }

  /**
   * Switch the world's pace and rebuild every loaded planet on it. The planet's numbers, gates and
   * machines are kept; only the rates, group size, hold lengths and bypass switch change. Never
   * touches a threshold.
   */
  private static final java.time.format.DateTimeFormatter PACE_STAMP =
      java.time.format.DateTimeFormatter.ofPattern("HHmmss");

  public static void setPace(
      net.minecraft.server.MinecraftServer server,
      mars.index.sim.PaceConfig.Mode mode,
      Integer groupSize) {
    PaceData pd = PaceData.get(server);
    mars.index.sim.PaceConfig cfg = IndexDefinitions.require().pace();
    mars.index.sim.Pace pace = cfg.pace(mode, groupSize); // validates the group size first
    // Safety net (slice 9): a checkpoint of every planet before the pace changes, so a teacher who
    // picks the wrong mode can go back. Bounded by the normal checkpoint list (oldest dropped).
    Checkpoints.takeAll(
        server, "before_pace_" + java.time.LocalTime.now().format(PACE_STAMP), "pace-change");
    pd.set(mode, pace.groupSize());
    for (ServerLevel level : server.getAllLevels()) {
      if (isPlanet(level)) {
        String id = level.dimension().location().toString();
        PlanetData rebuilt =
            PlanetData.fromState(id, data(level).sim().toState(), pace, seed(level));
        level.getDataStorage().set(PlanetData.NAME, rebuilt);
        rebuilt.setDirty();
      }
    }
  }

  /**
   * Self-test only: replace the level's planet with a fresh one, because the GameTest world keeps
   * its saved planet between runs and a latched gate cannot be undone. Refuses to run unless the
   * test seam property is on, so it can never reset a real planet.
   */
  public static PlanetData resetForSelfTest(ServerLevel level) {
    if (!ANY_LEVEL_IS_PLANET) {
      throw new IllegalStateException("resetForSelfTest is only for the GameTest server");
    }
    PlanetData fresh =
        PlanetData.factory(level.dimension().location().toString(), pace(level), seed(level))
            .constructor()
            .get();
    level.getDataStorage().set(PlanetData.NAME, fresh);
    fresh.setDirty();
    return fresh;
  }

  public static void machinePlaced(ServerLevel level, String type, BlockPos pos) {
    PlanetData d = data(level);
    if (d.sim().addMachine(type, pos.asLong())) {
      d.setDirty();
      MachineStateSync.sync(level, d.sim());
      LOG.info(
          "{} placed at {} in {}: {} on this planet",
          type,
          pos.toShortString(),
          level.dimension().location(),
          d.sim().machineCount(type));
    }
  }

  public static void machineRemoved(ServerLevel level, String type, BlockPos pos) {
    PlanetData d = data(level);
    if (d.sim().removeMachine(type, pos.asLong())) {
      d.setDirty();
      MachineStateSync.sync(level, d.sim());
      LOG.info(
          "{} removed at {} in {}: {} left",
          type,
          pos.toShortString(),
          level.dimension().location(),
          d.sim().machineCount(type));
    }
  }

  /**
   * May this player place the machine here? Survival players need every gate that unlocks it to
   * have latched. Creative players pass while the pace's creative bypass is on (standard; off in
   * classroom; logged). Not a planet: yes. On refusal the player is told why.
   */
  public static boolean placementAllowed(
      net.minecraft.world.level.Level level,
      net.minecraft.world.entity.player.Player player,
      String type) {
    if (!(level instanceof ServerLevel server) || !isPlanet(server)) {
      return true;
    }
    mars.gates.GateDef lock = data(server).sim().gates().lockedBy(type);
    if (lock == null) {
      return true;
    }
    if (player != null && player.isCreative() && data(server).sim().pace().creativeGateBypass()) {
      LOG.info(
          "{} placed {} in creative before gate {} (admin route)",
          player.getName().getString(),
          type,
          lock.id());
      return true;
    }
    if (player != null) {
      player.displayClientMessage(
          net.minecraft.network.chat.Component.translatable(
              "gate.locked.placement",
              net.minecraft.network.chat.Component.translatable(lock.nameKey())),
          true);
    }
    return false;
  }

  /** Tell every player on the planet about gates that latched since the last call. */
  public static void announceGates(ServerLevel level, PlanetData d) {
    for (String id : d.sim().drainNewlyLatched()) {
      mars.gates.GateDef def =
          d.sim().gates().defs().stream().filter(g -> g.id().equals(id)).findFirst().orElse(null);
      if (def == null) {
        continue;
      }
      LOG.info(
          "Gate {} latched on {} at sol {} (cause {})",
          id,
          level.dimension().location(),
          d.sim().model().sols(),
          d.sim().gates().state(id).cause());
      var name = net.minecraft.network.chat.Component.translatable(def.nameKey());
      for (var player : level.players()) {
        player.sendSystemMessage(
            net.minecraft.network.chat.Component.translatable("gate.reached", name));
        for (String m : def.unlocks()) {
          player.sendSystemMessage(
              net.minecraft.network.chat.Component.translatable(
                  "gate.unlocked",
                  net.minecraft.network.chat.Component.translatable(
                      "block." + m.replace(':', '.'))));
        }
      }
      LearningService.onGateLatched(level, id);
      d.setDirty();
    }
  }

  /** Tell the planet a machine's output changed (a dusty solar panel). Fraction is 0 to 1. */
  public static void machineOutput(ServerLevel level, BlockPos pos, double fraction) {
    PlanetData d = data(level);
    d.sim().setEfficiency(pos.asLong(), fraction);
    d.setDirty();
  }
}
