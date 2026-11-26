package mars.machines;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A Stage 1 machine block: horizontal facing (front toward the player who placed it) and a fixed,
 * simple shape. The shape is centred and symmetric in x and z on purpose, so it never depends on
 * the model's exact geometry or on the facing; models can be swapped or redone at real scale
 * without touching this class. No behaviour yet (Stage 2).
 */
public class MachineBlock extends HorizontalDirectionalBlock {
  private final VoxelShape shape;
  private final java.util.Map<Direction, VoxelShape> byFacing;

  public MachineBlock(Properties properties, VoxelShape shape) {
    super(properties);
    this.shape = shape;
    this.byFacing = null;
    registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
  }

  /**
   * For machines that are much thinner one way (the wall-pack battery): {@code northBox} is one box
   * for facing north, given as {x0, y0, z0, x1, y1, z1} in pixels; the other facings rotate it
   * about the block centre. Still a plain box, not the model's shape.
   */
  public MachineBlock(Properties properties, double[] northBox) {
    super(properties);
    java.util.Map<Direction, VoxelShape> m = new java.util.EnumMap<>(Direction.class);
    double x0 = northBox[0], y0 = northBox[1], z0 = northBox[2];
    double x1 = northBox[3], y1 = northBox[4], z1 = northBox[5];
    m.put(Direction.NORTH, Block.box(x0, y0, z0, x1, y1, z1));
    m.put(Direction.SOUTH, Block.box(16 - x1, y0, 16 - z1, 16 - x0, y1, 16 - z0));
    m.put(Direction.EAST, Block.box(16 - z1, y0, x0, 16 - z0, y1, x1));
    m.put(Direction.WEST, Block.box(z0, y0, 16 - x1, z1, y1, 16 - x0));
    this.byFacing = m;
    this.shape = m.get(Direction.NORTH);
    registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
  }

  @Override
  protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
    // Shapes aren't data, so this codec is only used for registry bookkeeping.
    return simpleCodec(p -> new MachineBlock(p, shape));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
  }

  @Override
  protected VoxelShape getShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return byFacing == null ? shape : byFacing.get(state.getValue(FACING));
  }
}
