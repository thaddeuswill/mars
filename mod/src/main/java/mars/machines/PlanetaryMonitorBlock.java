package mars.machines;

import mars.monitor.MonitorSender;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * The Planetary Monitor (ARCHITECTURE.md 2.9). Using it asks the server for the planet's values;
 * the client only draws what it is sent.
 */
public class PlanetaryMonitorBlock extends MachineBlock {
  public PlanetaryMonitorBlock(Properties properties, VoxelShape shape) {
    super(properties, shape);
  }

  /** The Monitor is the dome's seed block: placing it registers a dome (ARCHITECTURE.md 2.3). */
  @Override
  protected void onPlace(
      BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
    super.onPlace(state, level, pos, oldState, movedByPiston);
    if (level instanceof net.minecraft.server.level.ServerLevel server
        && !oldState.is(state.getBlock())
        && mars.planets.PlanetManager.isPlanet(server)) {
      mars.hull.DomeEvents.register(server, pos);
    }
  }

  @Override
  protected void onRemove(
      BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
    if (level instanceof net.minecraft.server.level.ServerLevel server
        && !state.is(newState.getBlock())
        && mars.planets.PlanetManager.isPlanet(server)) {
      mars.hull.DomeEvents.unregister(server, pos);
    }
    super.onRemove(state, level, pos, newState, movedByPiston);
  }

  @Override
  protected InteractionResult useWithoutItem(
      BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
    if (player instanceof ServerPlayer sp) {
      MonitorSender.open(sp);
    }
    return InteractionResult.sidedSuccess(level.isClientSide);
  }
}
