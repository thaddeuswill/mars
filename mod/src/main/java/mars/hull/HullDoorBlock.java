package mars.hull;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Hull door (the airlock door of dome-layout.md, one block type for inner and outer doors). Closed
 * it is airtight, open it joins the two sides ({@link Airtight}). Opens by hand and by redstone.
 * Every open, close, placement and removal tells the planet's domes. The airlock interlock (inner
 * and outer door never open together) is Stage 3 slice 6, not here.
 */
public class HullDoorBlock extends DoorBlock {
  public HullDoorBlock(Properties properties) {
    super(BlockSetType.COPPER, properties);
  }

  @Override
  public MapCodec<DoorBlock> codec() {
    return CODEC;
  }

  @Override
  protected InteractionResult useWithoutItem(
      BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
    InteractionResult r = super.useWithoutItem(state, level, pos, player, hit);
    DomeEvents.changed(level, pos);
    return r;
  }

  @Override
  public void setOpen(Entity entity, Level level, BlockState state, BlockPos pos, boolean open) {
    super.setOpen(entity, level, state, pos, open);
    DomeEvents.changed(level, pos);
  }

  @Override
  protected void neighborChanged(
      BlockState state,
      Level level,
      BlockPos pos,
      Block block,
      BlockPos fromPos,
      boolean isMoving) {
    super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    DomeEvents.changed(level, pos);
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
