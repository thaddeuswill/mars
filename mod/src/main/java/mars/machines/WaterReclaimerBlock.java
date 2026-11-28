package mars.machines;

import mars.planets.PlanetManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * The water reclaimer: a game-sized version of the ISS Water Recovery System
 * (docs/science/machine-references.md). So far it is a powered load on the planet's grid and
 * changes no Index row: the Index has no water or life-support parameter yet, and the Dome stable
 * stand-in only asks whether the grid covers demand (the reclaimer's draw is part of that demand).
 * No block entity, never ticks; the planet's own step does the rest. Draw lives in {@code
 * data/mars/machines/water_reclaimer.json}. The model is 1.9 blocks tall; the shape is one cell
 * with a taller collision (see {@link MarsMachines}).
 */
public class WaterReclaimerBlock extends PoweredMachineBlock {
  public static final String TYPE = "mars:water_reclaimer";
  private final VoxelShape collision;

  public WaterReclaimerBlock(Properties properties, VoxelShape shape, VoxelShape collision) {
    super(properties, shape);
    this.collision = collision;
  }

  @Override
  protected VoxelShape getCollisionShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return collision;
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
