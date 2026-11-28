package mars.planets;

import mars.core.Planets;
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
            PlanetData.factory(level.dimension().location().toString()), PlanetData.NAME);
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
        PlanetData.factory(level.dimension().location().toString()).constructor().get();
    level.getDataStorage().set(PlanetData.NAME, fresh);
    fresh.setDirty();
    return fresh;
  }

  public static void machinePlaced(ServerLevel level, String type, BlockPos pos) {
    PlanetData d = data(level);
    if (d.sim().addMachine(type, pos.asLong())) {
      d.setDirty();
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
   * have latched. Creative players may bypass the lock when the server option is enabled (logged).
   * On refusal the player is told why.
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
    if (player != null && player.isCreative() && mars.MarsServerConfig.CREATIVE_GATE_BYPASS.get()) {
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
      d.setDirty();
      net.neoforged.neoforge.common.NeoForge.EVENT_BUS.post(new GateLatchedEvent(level, id));
    }
  }

  /**
   * A solar panel block now shows this dust state (placed, {@code /setblock}, or the game's own
   * sync write). The planet takes it when it disagrees with its own record.
   */
  public static void panelState(
      ServerLevel level, BlockPos pos, int stage, boolean partlyCleaned, double fraction) {
    PlanetData d = data(level);
    d.sim().setPanelDust(pos.asLong(), stage, partlyCleaned, fraction);
    d.setDirty();
  }

  /**
   * Write the planet's dust stage into a panel's block when it differs (a stage change in a loaded
   * chunk, or a chunk that has just loaded). Flag 2: tell clients, no neighbour updates.
   */
  static void syncPanelBlock(ServerLevel level, mars.hazards.dust.PanelDustModel.View v) {
    BlockPos pos = BlockPos.of(v.pos());
    if (!level.isLoaded(pos)) {
      return; // the chunk gets it when it loads
    }
    var state = level.getBlockState(pos);
    if (!(state.getBlock() instanceof mars.machines.SolarPanelBlock)) {
      return;
    }
    if (state.getValue(mars.machines.SolarPanelBlock.DUST_STAGE) != v.stage()
        || state.getValue(mars.machines.SolarPanelBlock.PARTLY_CLEANED) != v.partlyCleaned()) {
      level.setBlock(
          pos,
          state
              .setValue(mars.machines.SolarPanelBlock.DUST_STAGE, v.stage())
              .setValue(mars.machines.SolarPanelBlock.PARTLY_CLEANED, v.partlyCleaned()),
          2);
    }
  }

  /** Write every pending panel stage change into the loaded blocks. One list copy per call. */
  public static void syncPanels(ServerLevel level, PlanetData d) {
    if (!d.sim().hasBlockSync()) {
      return;
    }
    for (var v : d.sim().drainBlockSync()) {
      syncPanelBlock(level, v);
    }
  }

  /** A chunk just loaded: bring its panels to the stage the planet says (they may be older). */
  public static void onChunkLoad(net.neoforged.neoforge.event.level.ChunkEvent.Load e) {
    if (!(e.getLevel() instanceof ServerLevel level) || !isPlanet(level)) {
      return;
    }
    PlanetData d = data(level);
    if (!d.sim().hasHazards()) {
      return;
    }
    // Queue only: the blocks are written on the next planet tick, not inside the load event.
    d.sim().queueChunkSync(e.getChunk().getPos().toLong());
    LOG.info("DEBUGSYNC chunk {} queued={}", e.getChunk().getPos(), d.sim().hasBlockSync());
  }

  /** Tell the planet a machine's output changed (a dusty solar panel). Fraction is 0 to 1. */
  public static void machineOutput(ServerLevel level, BlockPos pos, double fraction) {
    PlanetData d = data(level);
    d.sim().setEfficiency(pos.asLong(), fraction);
    d.setDirty();
  }
}
