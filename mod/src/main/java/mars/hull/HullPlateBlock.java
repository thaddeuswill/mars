package mars.hull;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/** Hull plate: a full, airtight block. It tells the planet's domes when it is placed or removed. */
public class HullPlateBlock extends Block {
  public HullPlateBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected MapCodec<? extends Block> codec() {
    return simpleCodec(HullPlateBlock::new);
  }

  @Override
  protected void onPlace(
      BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
    super.onPlace(state, level, pos, oldState, movedByPiston);
    DomeEvents.changed(level, pos);
  }

  @Override
  protected void onRemove(
      BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
    super.onRemove(state, level, pos, newState, movedByPiston);
    DomeEvents.changed(level, pos);
  }
}
