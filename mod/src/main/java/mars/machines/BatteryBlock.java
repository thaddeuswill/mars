package mars.machines;

import mars.planets.PlanetManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

/**
 * The wall-pack battery: stores power for the planet's shared grid (balance sheet 2b). Like the
 * Outgasser it has no block entity; its position is registered with the planet. The {@code charge}
 * property (0 to 3) shows quarters of the grid's stored energy, rounded down (3 = 75 percent or
 * more); the planet sets it (see {@link MachineStateSync}). All batteries on a planet share one
 * store, so they show the same level. Default 0 (also what an old save loads as).
 */
public class BatteryBlock extends MachineBlock {
  public static final String TYPE = "mars:battery";

  public static final IntegerProperty CHARGE = IntegerProperty.create("charge", 0, 3);

  public BatteryBlock(Properties properties, double[] northBox) {
    super(properties, northBox);
    registerDefaultState(defaultBlockState().setValue(CHARGE, 0));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(CHARGE);
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
