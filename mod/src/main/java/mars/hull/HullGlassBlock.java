package mars.hull;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockState;

/** Hull glass: a full, airtight, see-through block (it still blocks the sky for the heightmap). */
public class HullGlassBlock extends TransparentBlock {
  public HullGlassBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected MapCodec<? extends TransparentBlock> codec() {
    return simpleCodec(HullGlassBlock::new);
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
