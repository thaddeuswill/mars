package mars.machines;

import mars.planets.PlanetManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * The Phase 1 pressure source (balance sheet §2). It has no block entity and never ticks: placing
 * it registers its position with the planet, removing it unregisters, and the planet's own step
 * does the rest. Its rate lives in {@code data/mars/machines/regolith_outgasser.json}.
 */
public class RegolithOutgasserBlock extends MachineBlock {
  public static final String TYPE = "mars:regolith_outgasser";

  public RegolithOutgasserBlock(Properties properties, VoxelShape shape) {
    super(properties, shape);
  }

  /**
   * Survival players may place it only after "Dome stable" (data/mars/gates). Creative players
   * pass. Returning null cancels the placement before anything is registered. {@code /setblock},
   * loot and other non-player routes do not go through here.
   */
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    if (!PlanetManager.placementAllowed(context.getLevel(), context.getPlayer(), TYPE)) {
      return null;
    }
    return super.getStateForPlacement(context);
  }

  @Override
  protected void onPlace(
      BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
    super.onPlace(state, level, pos, oldState, movedByPiston);
    if (level instanceof ServerLevel server
        && !oldState.is(state.getBlock())
        && PlanetManager.isPlanet(server)) {
      PlanetManager.machinePlaced(server, TYPE, pos);
    }
  }

  @Override
  protected void onRemove(
      BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
    if (level instanceof ServerLevel server
        && !state.is(newState.getBlock())
        && PlanetManager.isPlanet(server)) {
      PlanetManager.machineRemoved(server, TYPE, pos);
    }
    super.onRemove(state, level, pos, newState, movedByPiston);
  }
}
