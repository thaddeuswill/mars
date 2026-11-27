package mars.machines;

import java.util.List;
import java.util.function.BiFunction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers the Phase 1 machine blocks, their items, and the creative tab (BACKLOG #26). Registry
 * IDs are the contract; model and texture file names are not (the oxygen maker's blockstate points
 * at the art file {@code o2_generator}, so renaming the art is a one-line change).
 */
public final class MarsMachines {
  public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks("mars");
  public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems("mars");
  public static final DeferredRegister<CreativeModeTab> TABS =
      DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "mars");

  // Simple shapes in 1/16 block, centred and symmetric so facing and model don't matter.
  private static final VoxelShape OUTGASSER = Block.box(1, 0, 1, 15, 16, 15);
  private static final VoxelShape OVEN = Block.box(2, 0, 2, 14, 14, 14);
  // Round array centre: base, post, and the part of the array over its own cell (2.18).
  private static final VoxelShape PANEL =
      net.minecraft.world.phys.shapes.Shapes.or(
          Block.box(4, 0, 4, 12, 1, 12),
          Block.box(7, 1, 7, 9, 10, 9),
          Block.box(0, 10, 0, 16, 11, 16));
  // Wall-pack battery (batch 3): thin, so its box turns with facing. Centred, 12 x 16 x 6 px.
  private static final double[] BATTERY = {2, 0, 5, 14, 16, 11};
  private static final VoxelShape OXYGEN_MAKER = Block.box(1, 0, 1, 15, 15, 15);
  private static final VoxelShape MONITOR = Block.box(1, 0, 1, 15, 16, 15);

  public static final DeferredBlock<RegolithOutgasserBlock> REGOLITH_OUTGASSER =
      machine("regolith_outgasser", OUTGASSER, RegolithOutgasserBlock::new);
  public static final DeferredBlock<MachineBlock> REGOLITH_OVEN =
      machine("regolith_oven", OVEN, MachineBlock::new);
  public static final DeferredBlock<SolarPanelBlock> SOLAR_PANEL =
      machine("solar_panel", PANEL, SolarPanelBlock::new);
  public static final DeferredBlock<MachineBlock> BATTERY_BLOCK =
      BLOCKS.register("battery", () -> new MachineBlock(machineProperties(), BATTERY));
  public static final DeferredBlock<MachineBlock> OXYGEN_MAKER_BLOCK =
      machine("oxygen_maker", OXYGEN_MAKER, MachineBlock::new);
  public static final DeferredBlock<PlanetaryMonitorBlock> PLANETARY_MONITOR =
      machine("planetary_monitor", MONITOR, PlanetaryMonitorBlock::new);

  /** Invisible filler under the round array's overhang; no item, not in the tab. */
  public static final DeferredBlock<SolarPanelCanopyBlock> SOLAR_PANEL_CANOPY =
      BLOCKS.register(
          "solar_panel_canopy",
          () ->
              new SolarPanelCanopyBlock(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.METAL)
                      .strength(2.0f, 6.0f)
                      .sound(SoundType.METAL)
                      .noOcclusion()
                      .noLootTable()));

  /** Every machine, in creative-tab order. */
  public static final List<DeferredBlock<? extends Block>> ALL =
      List.of(
          REGOLITH_OUTGASSER,
          REGOLITH_OVEN,
          SOLAR_PANEL,
          BATTERY_BLOCK,
          OXYGEN_MAKER_BLOCK,
          PLANETARY_MONITOR);

  public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB =
      TABS.register(
          "machines",
          () ->
              CreativeModeTab.builder()
                  .title(Component.translatable("itemGroup.mars.machines"))
                  .icon(() -> new ItemStack(REGOLITH_OUTGASSER.get()))
                  .displayItems(
                      (params, output) -> {
                        for (DeferredBlock<? extends Block> b : ALL) {
                          output.accept(b.get());
                        }
                      })
                  .build());

  private MarsMachines() {}

  private static <B extends Block> DeferredBlock<B> machine(
      String id, VoxelShape shape, BiFunction<BlockBehaviour.Properties, VoxelShape, B> ctor) {
    DeferredBlock<B> block = BLOCKS.register(id, () -> ctor.apply(machineProperties(), shape));
    ITEMS.registerSimpleBlockItem(id, block, new Item.Properties());
    return block;
  }

  private static BlockBehaviour.Properties machineProperties() {
    return BlockBehaviour.Properties.of()
        .mapColor(MapColor.METAL)
        .strength(2.0f, 6.0f)
        .sound(SoundType.METAL)
        .noOcclusion();
  }

  static {
    ITEMS.registerSimpleBlockItem("battery", BATTERY_BLOCK, new Item.Properties());
  }

  public static void register(IEventBus modBus) {
    BLOCKS.register(modBus);
    ITEMS.register(modBus);
    TABS.register(modBus);
  }
}
