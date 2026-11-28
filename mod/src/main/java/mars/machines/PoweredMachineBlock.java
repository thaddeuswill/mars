package mars.machines;

import com.mojang.serialization.MapCodec;
import mars.planets.PlanetManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A machine that shows whether it works: the {@code machine_state} property (off, idle, running).
 * The planet sets it (see {@link MachineStateSync}); the block never ticks. The default is idle,
 * which is also what a block saved before the property existed loads as. Off a planet (the machine
 * is not registered with any grid) a freshly placed machine shows off.
 */
public class PoweredMachineBlock extends MachineBlock {
  public static final EnumProperty<MachineState> MACHINE_STATE =
      EnumProperty.create("machine_state", MachineState.class);

  private final VoxelShape shape;

  public PoweredMachineBlock(Properties properties, VoxelShape shape) {
    super(properties, shape);
    this.shape = shape;
    registerDefaultState(defaultBlockState().setValue(MACHINE_STATE, MachineState.IDLE));
  }

  @Override
  protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
    return simpleCodec(p -> new PoweredMachineBlock(p, shape));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(MACHINE_STATE);
  }

  @Override
  protected void onPlace(
      BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
    super.onPlace(state, level, pos, oldState, movedByPiston);
    if (level instanceof ServerLevel server
        && !oldState.is(state.getBlock())
        && !PlanetManager.isPlanet(server)
        && state.getValue(MACHINE_STATE) != MachineState.OFF) {
      server.setBlock(pos, state.setValue(MACHINE_STATE, MachineState.OFF), Block.UPDATE_CLIENTS);
    }
  }
}
