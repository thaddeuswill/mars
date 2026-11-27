package mars.world;

import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * The W1 terrain blocks (docs/design/world-and-blocks.md 3.1). All four are NEW blocks, not
 * retextured vanilla ones (owner decision: block textures are global in Minecraft). They are plain
 * blocks: no behaviour yet (ground-ice collapse is W3). Textures are placeholders, see
 * docs/art/w1-placeholder-textures.md. Ore amounts and places are INVENTED (worldgen data).
 */
public final class MarsTerrainBlocks {
  public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks("mars");
  public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems("mars");
  public static final DeferredRegister<CreativeModeTab> TABS =
      DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "mars");

  /** Dark basaltic sand: the darker patches of the surface and the dune sand. Not an ore. */
  public static final DeferredBlock<Block> BASALTIC_SAND =
      block(
          "basaltic_sand",
          BlockBehaviour.Properties.of()
              .mapColor(MapColor.COLOR_BROWN)
              .strength(0.5f)
              .sound(SoundType.SAND));

  /** Iron ore: drops raw iron (vanilla progression). */
  public static final DeferredBlock<Block> HEMATITE_ORE =
      block(
          "hematite_ore",
          BlockBehaviour.Properties.of()
              .mapColor(MapColor.COLOR_RED)
              .strength(3.0f, 3.0f)
              .requiresCorrectToolForDrops()
              .sound(SoundType.STONE));

  /**
   * Dark red-brown sand found as ore in the ground; smelts to glass. Called "sand", never silica.
   */
  public static final DeferredBlock<Block> DARK_SAND_ORE =
      block(
          "dark_sand_ore",
          BlockBehaviour.Properties.of()
              .mapColor(MapColor.TERRACOTTA_BROWN)
              .strength(0.8f)
              .sound(SoundType.SAND));

  /** Water ice under the ground. Plain block for now; behaviour (ice rule, collapse) is W3. */
  public static final DeferredBlock<Block> GROUND_ICE =
      block(
          "ground_ice",
          BlockBehaviour.Properties.of()
              .mapColor(MapColor.ICE)
              .strength(0.8f)
              .sound(SoundType.GLASS));

  /** Every terrain block, in creative-tab order. */
  public static final List<DeferredBlock<Block>> ALL =
      List.of(BASALTIC_SAND, HEMATITE_ORE, DARK_SAND_ORE, GROUND_ICE);

  public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB =
      TABS.register(
          "terrain",
          () ->
              CreativeModeTab.builder()
                  .title(Component.translatable("itemGroup.mars.terrain"))
                  .icon(() -> new ItemStack(HEMATITE_ORE.get()))
                  .displayItems(
                      (params, output) -> {
                        for (DeferredBlock<Block> b : ALL) {
                          output.accept(b.get());
                        }
                      })
                  .build());

  private MarsTerrainBlocks() {}

  private static DeferredBlock<Block> block(String id, BlockBehaviour.Properties props) {
    DeferredBlock<Block> b = BLOCKS.registerSimpleBlock(id, props);
    ITEMS.registerSimpleBlockItem(id, b, new Item.Properties());
    return b;
  }

  public static void register(IEventBus modBus) {
    BLOCKS.register(modBus);
    ITEMS.register(modBus);
    TABS.register(modBus);
  }
}
