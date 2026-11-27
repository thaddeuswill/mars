package mars.machines;

import mars.planets.PlanetManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A machine with no behaviour of its own: its position is registered with the planet when placed
 * and forgotten when removed, and the planet's step does the rest (like the Oven and the battery).
 * Used by the life-support machines (oxygen maker, water reclaimer, oxygen tank), whose numbers
 * live in {@code data/mars/machines/<name>.json}. Cost: one map update on place or remove.
 */
public class RegisteredMachineBlock extends MachineBlock {
  private final String type;

  public RegisteredMachineBlock(Properties properties, VoxelShape shape, String type) {
    super(properties, shape);
    this.type = type;
  }

  public String type() {
    return type;
  }

  @Override
  protected void onPlace(
      BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
    super.onPlace(state, level, pos, oldState, movedByPiston);
    if (level instanceof ServerLevel server
        && !oldState.is(state.getBlock())
        && PlanetManager.isPlanet(server)) {
      PlanetManager.machinePlaced(server, type, pos);
      // A life-support machine's room must be sealed (dome-layout section 5.4).
      mars.hull.DomeEvents.register(server, pos);
    }
  }

  @Override
  protected void onRemove(
      BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
    if (level instanceof ServerLevel server
        && !state.is(newState.getBlock())
        && PlanetManager.isPlanet(server)) {
      PlanetManager.machineRemoved(server, type, pos);
      mars.hull.DomeEvents.unregister(server, pos);
    }
    super.onRemove(state, level, pos, newState, movedByPiston);
  }
}
