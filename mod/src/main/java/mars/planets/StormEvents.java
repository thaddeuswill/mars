package mars.planets;

import mars.hazards.storm.StormSchedule;
import mars.index.sim.PlanetSim;
import mars.machines.SolarPanelBlock;
import mars.monitor.view.Fmt;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Turns the pure storm notices into what players see (ARCHITECTURE.md 2.22): one chat line each for
 * warning, onset and end, and the dust every panel gains when a storm ends. Cost: nothing on a tick
 * with no notice (one empty-list check); at a storm's end, one block-state change per panel on the
 * planet (each re-meshes one chunk section, once per storm).
 */
public final class StormEvents {
  private static final Logger LOG = LoggerFactory.getLogger("mars");

  private StormEvents() {}

  /** Announce and apply everything that changed since the last call. */
  public static void announce(ServerLevel level, PlanetData data) {
    PlanetSim sim = data.sim();
    var notices = sim.drainStormNotices();
    if (notices.isEmpty()) {
      return;
    }
    StormSchedule storm = sim.storm();
    double minPerSol = sim.pace().solTicks() / 1200.0;
    for (PlanetSim.StormNotice n : notices) {
      LOG.info(
          "storm.{} on {} at sol {}",
          n.event().name().toLowerCase(java.util.Locale.ROOT),
          level.dimension().location(),
          sim.model().sols());
      switch (n.event()) {
        case WARNED ->
            tell(
                level,
                Component.translatable(
                    "log.storm.warn",
                    Fmt.duration(storm.warningSols(), minPerSol),
                    Fmt.duration(storm.lengthSols(storm.kind()), minPerSol)));
        case STARTED -> tell(level, Component.translatable("log.storm.onset"));
        case ENDED -> {
          tell(level, Component.translatable("log.storm.end"));
          dustPanels(level, sim, n.stagesToAdd());
        }
      }
    }
    data.setDirty();
  }

  private static void tell(ServerLevel level, Component text) {
    for (var player : level.players()) {
      player.sendSystemMessage(text);
    }
  }

  /**
   * Add {@code stages} of dust (to the top stage at most) to every registered panel. The panel's
   * own block-state change updates the grid. A panel in an unloaded chunk loads that chunk once.
   */
  static void dustPanels(ServerLevel level, PlanetSim sim, int stages) {
    int changed = 0;
    for (long packed : sim.machinePositions(SolarPanelBlock.TYPE)) {
      BlockPos pos = BlockPos.of(packed);
      BlockState state = level.getBlockState(pos);
      if (!(state.getBlock() instanceof SolarPanelBlock)) {
        continue; // the registry and the world disagree: leave it to the machine code
      }
      int now = state.getValue(SolarPanelBlock.DUST_STAGE);
      int next = Math.min(4, now + stages);
      if (next != now || state.getValue(SolarPanelBlock.PARTLY_CLEANED)) {
        level.setBlock(
            pos,
            state
                .setValue(SolarPanelBlock.DUST_STAGE, next)
                .setValue(SolarPanelBlock.PARTLY_CLEANED, false),
            Block.UPDATE_ALL);
        changed++;
      }
    }
    LOG.info("storm.dust: +{} stage(s) on {} panel(s)", stages, changed);
  }
}
