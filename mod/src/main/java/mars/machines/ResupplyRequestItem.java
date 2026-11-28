package mars.machines;

import java.util.List;
import mars.planets.GateCaches;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * The resupply request (docs/design/recipe-inputs-ruling.md section 6): right-click calls the Relay
 * Orbiter for a refill of the latest gate's drop. The item is used up only when a pack is sent; in
 * the pass cool-down, or before any gate has dropped anything, it is kept. Server side only; the
 * work is in {@link GateCaches#request}. Game design, not real.
 */
public class ResupplyRequestItem extends Item {
  public ResupplyRequestItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);
    if (level.isClientSide() || !(player instanceof ServerPlayer sp)) {
      return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
    if (GateCaches.request(sp) == GateCaches.RequestResult.SENT && !sp.getAbilities().instabuild) {
      stack.shrink(1);
    }
    return InteractionResultHolder.sidedSuccess(stack, false);
  }

  @Override
  public void appendHoverText(
      ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
    tooltip.add(
        Component.translatable("item.mars.resupply_request.tooltip")
            .withStyle(ChatFormatting.GRAY));
  }
}
