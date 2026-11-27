package mars.hull;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A roof fixture (vent or dish): a decoration block standing on the hull roof. It is not a full
 * cube and is in {@code mars:not_airtight}, so it never seals anything and never changes a seal.
 * The shape is the union of the model's element boxes (see the factory methods).
 */
public class RoofFixtureBlock extends Block {
  /** Vent model: base 14 x 1 x 14 px at 1 and body 12 x 5 x 12 px inset 2. */
  public static final VoxelShape VENT_SHAPE =
      Shapes.or(Block.box(1, 0, 1, 15, 1, 15), Block.box(2, 0, 2, 14, 5, 14));

  /**
   * Dish model: foot 14 wide at y 10..11, mast 4 wide up to 15, rim to 16. A base box for the wide
   * parts and a mast box for the tall one.
   */
  public static final VoxelShape DISH_SHAPE =
      Shapes.or(Block.box(1, 0, 1, 15, 11, 15), Block.box(6, 0, 6, 10, 16, 10));

  private final VoxelShape shape;

  public RoofFixtureBlock(Properties properties, VoxelShape shape) {
    super(properties);
    this.shape = shape;
  }

  @Override
  protected MapCodec<? extends Block> codec() {
    // Shapes aren't data, so this codec is only used for registry bookkeeping.
    return simpleCodec(p -> new RoofFixtureBlock(p, shape));
  }

  @Override
  protected VoxelShape getShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return shape;
  }
}
