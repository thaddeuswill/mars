package mars.habitat;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The Habitat Bunk: vanilla bed behaviour (sleep, respawn point, two parts) with a plain JSON
 * model. Vanilla draws a bed with an entity renderer and a block entity that only stores a colour;
 * this block draws as a normal model and has no block entity (the Artist's route A, see
 * ARCHITECTURE.md 2.21). Sleeping never moves Mars time: the planet clock runs on game ticks, not
 * on the overworld day time that sleeping skips. An unused bunk is furniture.
 */
public class HabitatBunkBlock extends BedBlock {
  public static final MapCodec<HabitatBunkBlock> CODEC = simpleCodec(HabitatBunkBlock::new);

  public HabitatBunkBlock(BlockBehaviour.Properties properties) {
    super(DyeColor.WHITE, properties);
  }

  @Override
  public MapCodec<BedBlock> codec() {
    return (MapCodec<BedBlock>) (MapCodec<?>) CODEC;
  }

  @Override
  public RenderShape getRenderShape(BlockState state) {
    return RenderShape.MODEL;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return null;
  }
}
