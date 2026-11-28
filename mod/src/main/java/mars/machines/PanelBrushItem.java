package mars.machines;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.NeoForge;

/**
 * Panel Brush (docs/design/f1-storm-spec.md 2.7): hold right-click on a solar panel (the centre or
 * any of its eight cells) for 2 seconds and the panel goes back to dust stage 0. 64 uses; creative
 * players use none. The stage is cleaned in the planet's record (the block follows on the next
 * tick), a {@link PanelCleanedEvent} is posted, and the brush wears by one. The 2 s hold is real
 * time and is never scaled by the pace (spec 2.8). [FICTION: stand-in] no Mars mission flew a
 * brush. Cost: one ray trace at the end of the hold.
 */
public class PanelBrushItem extends Item {
  /** Hold time in ticks: 2 seconds (spec 2.7, never scaled). */
  public static final int HOLD_TICKS = 40;

  public static final int USES = 64;

  public PanelBrushItem(Properties properties) {
    super(properties.durability(USES));
  }

  /** The centre block of the array that {@code pos} belongs to, or null if it is not a panel. */
  public static BlockPos panelCentre(Level level, BlockPos pos) {
    BlockState s = level.getBlockState(pos);
    if (s.getBlock() instanceof SolarPanelBlock) {
      return pos;
    }
    if (s.getBlock() instanceof SolarPanelCanopyBlock) {
      BlockPos c = SolarPanelCanopyBlock.centreOf(pos, s);
      return level.getBlockState(c).getBlock() instanceof SolarPanelBlock ? c : null;
    }
    return null;
  }

  /**
   * Brush the panel at {@code pos} now. Returns the stage it had, or -1 when nothing was brushed
   * (no panel, or already clean: the brush is not used up).
   */
  public static int brush(ServerPlayer player, ServerLevel level, BlockPos pos, ItemStack stack) {
    BlockPos centre = panelCentre(level, pos);
    if (centre == null) {
      player.displayClientMessage(Component.translatable("panel_brush.no_target"), true);
      return -1;
    }
    var data = mars.planets.PlanetManager.data(level);
    long key = centre.asLong();
    if (data.sim().panelStage(key) == 0 && !data.sim().panelPartlyCleaned(key)) {
      player.displayClientMessage(Component.translatable("panel_brush.clean_already"), true);
      return -1;
    }
    boolean warned = data.sim().stormWarned();
    int before = data.sim().cleanPanel(key);
    data.setDirty();
    mars.planets.PlanetManager.syncPanels(level, data);
    level.playSound(null, centre, SoundEvents.BRUSH_GENERIC, SoundSource.BLOCKS, 0.8f, 1.2f);
    player.displayClientMessage(Component.translatable("panel_brush.cleaned", before), true);
    stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
    NeoForge.EVENT_BUS.post(new PanelCleanedEvent(player, level, before, warned));
    return before;
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    BlockPos centre = panelCentre(context.getLevel(), context.getClickedPos());
    if (centre == null || context.getPlayer() == null) {
      return InteractionResult.PASS;
    }
    context.getPlayer().startUsingItem(context.getHand());
    return InteractionResult.CONSUME;
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
    if (level instanceof ServerLevel server && entity instanceof ServerPlayer player) {
      BlockHitResult hit = Item.getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
      if (hit.getType() == HitResult.Type.BLOCK) {
        brush(player, server, hit.getBlockPos(), stack);
      }
    }
    return stack;
  }

  @Override
  public UseAnim getUseAnimation(ItemStack stack) {
    return UseAnim.BRUSH;
  }

  @Override
  public int getUseDuration(ItemStack stack, LivingEntity entity) {
    return HOLD_TICKS;
  }
}
