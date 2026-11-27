package mars.machines;

import mars.planets.PlanetManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The wall-pack battery: stores power for the planet's shared grid (balance sheet 2b). Like the
 * Outgasser it has no block entity; its position is registered with the planet.
 */
public class BatteryBlock extends MachineBlock {
  public static final String TYPE = "mars:battery";

  public BatteryBlock(Properties properties, double[] northBox) {
    super(properties, northBox);
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
