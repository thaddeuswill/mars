package mars.machines;

import mars.planets.PlanetManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * The Regolith Oven (balance sheet 2 and 2c): a small pressure source on the power grid. Like the
 * Outgasser it has no block entity and never ticks; its position is registered with the planet and
 * the planet's own step does the rest. Rate and power draw live in {@code
 * data/mars/machines/regolith_oven.json}. Not gated: it is the player's starting machine.
 */
public class RegolithOvenBlock extends MachineBlock {
  public static final String TYPE = "mars:regolith_oven";

  public RegolithOvenBlock(Properties properties, VoxelShape shape) {
    super(properties, shape);
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
