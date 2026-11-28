package mars.machines;

import mars.planets.PlanetManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A powered machine that sits on the planet's grid and may be locked by a gate (the atmospheric
 * processor and the super-greenhouse emitter). It has no block entity and never ticks; the planet's
 * own step does the rest. Its draw (and any future Index rate) lives in {@code
 * data/mars/machines/<name>.json}; which gate unlocks it lives in {@code data/mars/gates}. The
 * model may be taller than one cell: the outline is one cell and the collision is fence-high (the
 * overhang is visual only), like the water reclaimer.
 */
public class GatedLoadBlock extends MachineBlock {
  private final String type;
  private final VoxelShape collision;

  public GatedLoadBlock(
      Properties properties, VoxelShape shape, VoxelShape collision, String type) {
    super(properties, shape);
    this.collision = collision;
    this.type = type;
  }

  @Override
  protected VoxelShape getCollisionShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return collision;
  }

  /** Survival players need the gate that lists this machine; creative bypass as the Outgasser. */
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    if (!PlanetManager.placementAllowed(context.getLevel(), context.getPlayer(), type)) {
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
      PlanetManager.machinePlaced(server, type, pos);
    }
  }

  @Override
  protected void onRemove(
      BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
    if (level instanceof ServerLevel server
        && !state.is(newState.getBlock())
        && PlanetManager.isPlanet(server)) {
      PlanetManager.machineRemoved(server, type, pos);
    }
    super.onRemove(state, level, pos, newState, movedByPiston);
  }
}
