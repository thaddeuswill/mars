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

  @Override
  protected InteractionResult useWithoutItem(
      BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
    if (player instanceof ServerPlayer sp) {
      MonitorSender.open(sp);
    }
    return InteractionResult.sidedSuccess(level.isClientSide);
  }
}
