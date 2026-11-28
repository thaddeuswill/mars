package mars.hull;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;

/**
 * Seal Kit (docs/design/landing-spec.md section 2): right-click a block in the {@code
 * mars:patchable} tag (today the cracked hull plate) and it becomes a whole hull plate. One kit is
 * used (not in creative). The block change goes through the same place and remove hooks as a hand
 * repair, so the dome's seal is re-checked by the existing code. After a patch a {@link
 * SealKitPatchedEvent} is posted so the Landing can tick step 5. [FICTION: stand-in] no science
 * claim is made about how the kit works. Cost: one tag test per use.
 */
public class SealKitItem extends Item {
  public SealKitItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    var level = context.getLevel();
    var pos = context.getClickedPos();
    BlockState state = level.getBlockState(pos);
    if (!state.is(HullTags.PATCHABLE)) {
      if (context.getPlayer() instanceof ServerPlayer sp) {
        sp.displayClientMessage(Component.translatable("seal_kit.no_target"), true);
      }
      return InteractionResult.PASS;
    }
    if (level.isClientSide) {
      return InteractionResult.SUCCESS;
    }
    level.setBlock(pos, HullBlocks.HULL_PLATE.get().defaultBlockState(), Block.UPDATE_ALL);
    level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 0.6f, 1.4f);
    var player = context.getPlayer();
    if (player instanceof ServerPlayer sp) {
      sp.displayClientMessage(Component.translatable("seal_kit.patched"), true);
      if (!sp.getAbilities().instabuild) {
        context.getItemInHand().shrink(1);
      }
      NeoForge.EVENT_BUS.post(
          new SealKitPatchedEvent(sp, (net.minecraft.server.level.ServerLevel) level, pos));
    }
    return InteractionResult.CONSUME;
  }
}
