package mars.machines;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Invisible filler under the round array's overhang (ARCHITECTURE.md 2.18). Its shape is the part
 * of the array's square outline that lies over its own cell, at the array's height, so the
 * selection outline and collision match the overhang without any shape leaving its block. Breaking
 * it breaks the whole panel. It has no item and drops nothing itself.
 */
public class SolarPanelCanopyBlock extends Block {
  /** Position of this cell in the 3x3 footprint: 0, 1, 2 (the centre is 1, 1). */
  public static final IntegerProperty OFFSET_X = IntegerProperty.create("offset_x", 0, 2);

  public static final IntegerProperty OFFSET_Z = IntegerProperty.create("offset_z", 0, 2);

  /** Array outline in pixels relative to the centre block (model: -9..25, top at y 10..11). */
  static final double DISC_MIN = -9;

  static final double DISC_MAX = 25;
  static final double DISC_Y0 = 10;
  static final double DISC_Y1 = 11;
  private static final VoxelShape[][] SHAPES = new VoxelShape[3][3];

  static {
    for (int ox = 0; ox < 3; ox++) {
      for (int oz = 0; oz < 3; oz++) {
        SHAPES[ox][oz] = Block.box(lo(ox), DISC_Y0, lo(oz), hi(ox), DISC_Y1, hi(oz));
      }
    }
  }

  private static double lo(int o) {
    return Math.max(0, DISC_MIN - 16 * (o - 1));
  }

  private static double hi(int o) {
    return Math.min(16, DISC_MAX - 16 * (o - 1));
  }

  public SolarPanelCanopyBlock(Properties properties) {
    super(properties);
    registerDefaultState(stateDefinition.any().setValue(OFFSET_X, 0).setValue(OFFSET_Z, 0));
  }

  @Override
  protected MapCodec<? extends Block> codec() {
    return simpleCodec(SolarPanelCanopyBlock::new);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(OFFSET_X, OFFSET_Z);
  }

  /** The centre panel this canopy cell belongs to. */
  public static BlockPos centreOf(BlockPos pos, BlockState state) {
    return pos.offset(1 - state.getValue(OFFSET_X), 0, 1 - state.getValue(OFFSET_Z));
  }

  @Override
  protected RenderShape getRenderShape(BlockState state) {
    return RenderShape.INVISIBLE; // the centre block draws the whole array
  }

  @Override
  protected VoxelShape getShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return SHAPES[state.getValue(OFFSET_X)][state.getValue(OFFSET_Z)];
  }

  @Override
  public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
    BlockPos centre = centreOf(pos, state);
    if (!level.isClientSide()
        && level.getBlockState(centre).getBlock() instanceof SolarPanelBlock) {
      level.destroyBlock(centre, !player.isCreative(), player);
    }
    return super.playerWillDestroy(level, pos, state, player);
  }

  @Override
  public ItemStack getCloneItemStack(
      BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
    return new ItemStack(MarsMachines.SOLAR_PANEL.get());
  }
}
