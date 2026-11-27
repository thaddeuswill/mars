package mars.hull;

import mars.machines.MachineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The game's airtightness rule, one place (docs/design/dome-layout.md section 5): a full cube or
 * nothing. Order: a hull door is airtight when closed; the {@code mars:not_airtight} tag lets air
 * through; the {@code mars:airtight} tag and every machine casing stop it; otherwise a block is
 * airtight only when its collision shape fills the whole cube. So slabs, stairs, fences, panes,
 * trapdoors, plants and fluids leak, and an open door leaks.
 */
public final class Airtight {
  private Airtight() {}

  public static boolean test(BlockState state, BlockGetter level, BlockPos pos) {
    if (state.isAir()) {
      return false;
    }
    if (state.getBlock() instanceof HullDoorBlock) {
      return !state.getValue(DoorBlock.OPEN);
    }
    if (state.is(HullTags.NOT_AIRTIGHT)) {
      return false;
    }
    if (state.getBlock() instanceof MachineBlock || state.is(HullTags.AIRTIGHT)) {
      return true;
    }
    return state.isCollisionShapeFullBlock(level, pos);
  }
}
