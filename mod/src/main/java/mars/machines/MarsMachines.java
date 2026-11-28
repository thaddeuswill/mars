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
  // Round array centre (2.18): base plate, three legs, deck, and the disc's own-cell part (y
  // 15..16).
  private static final VoxelShape PANEL =
      net.minecraft.world.phys.shapes.Shapes.or(
          Block.box(2, 0, 2, 14, 1, 14),
          Block.box(7, 1, 3, 9, 13, 5),
          Block.box(3, 1, 10, 5, 13, 12),
          Block.box(11, 1, 10, 13, 13, 12),
          Block.box(1, 13, 1, 15, 15, 15),
          Block.box(0, 15, 0, 16, 16, 16));
  // Wall-pack battery (batch 3): thin, so its box turns with facing. Centred, 12 x 16 x 6 px.
  private static final double[] BATTERY = {2, 0, 5, 14, 16, 11};
  // Water reclaimer: the model is 30 px (1.9 blocks) tall. One block cell, with a fence-style
  // collision 24 px high (vanilla fences do the same) and a 16 px outline; the top 6 px of the
  // model overhang the cell above and are visual only.
  private static final VoxelShape RECLAIMER = Block.box(1, 0, 1, 15, 16, 15);
  private static final VoxelShape RECLAIMER_COLLISION = Block.box(1, 0, 1, 15, 24, 15);
  // Atmospheric processor (model 24 px) and emitter (model 32 px): one cell each, outline 16 px,
  // fence-high collision 24 px; the rest of the model overhangs the cell above (visual only).
  private static final VoxelShape CELL = Block.box(1, 0, 1, 15, 16, 15);
  private static final VoxelShape TALL_COLLISION = Block.box(1, 0, 1, 15, 24, 15);
  private static final VoxelShape OXYGEN_MAKER = Block.box(1, 0, 1, 15, 15, 15);
  private static final VoxelShape MONITOR = Block.box(1, 0, 1, 15, 16, 15);

  public static final DeferredBlock<RegolithOutgasserBlock> REGOLITH_OUTGASSER =
      machine("regolith_outgasser", OUTGASSER, RegolithOutgasserBlock::new);
  public static final DeferredBlock<RegolithOvenBlock> REGOLITH_OVEN =
      BLOCKS.register("regolith_oven", () -> new RegolithOvenBlock(machineProperties(), OVEN));
  public static final DeferredBlock<WaterReclaimerBlock> WATER_RECLAIMER =
      BLOCKS.register(
          "water_reclaimer",
          () -> new WaterReclaimerBlock(machineProperties(), RECLAIMER, RECLAIMER_COLLISION));
  public static final DeferredBlock<GatedLoadBlock> ATMOSPHERIC_PROCESSOR =
      BLOCKS.register(
          "atmospheric_processor",
          () ->
              new GatedLoadBlock(
                  machineProperties(), CELL, TALL_COLLISION, "mars:atmospheric_processor"));
  public static final DeferredBlock<GatedLoadBlock> SUPER_GREENHOUSE_EMITTER =
      BLOCKS.register(
          "super_greenhouse_emitter",
          () ->
              new GatedLoadBlock(
                  machineProperties(), CELL, TALL_COLLISION, "mars:super_greenhouse_emitter"));
  // Ice extractor (model 32 px): one cell, outline 16 px, fence-high collision 24 px; the sleeve
  // and pump box overhang the cell above (visual only), like the emitter.
  public static final DeferredBlock<GatedLoadBlock> ICE_EXTRACTOR =
      BLOCKS.register(
          "ice_extractor",
          () ->
              new GatedLoadBlock(machineProperties(), CELL, TALL_COLLISION, "mars:ice_extractor"));
  // Algae vat (model 28 px, hood top 24 px) and carbon scrubber (model 24 px): one cell each,
  // outline and collision 24 px (the vat's air line overhangs to 28 px, visual only).
  private static final VoxelShape TALL_CELL = Block.box(1, 0, 1, 15, 24, 15);
  public static final DeferredBlock<GatedLoadBlock> ALGAE_VAT =
      BLOCKS.register(
          "algae_vat",
          () -> new GatedLoadBlock(machineProperties(), TALL_CELL, TALL_CELL, "mars:algae_vat"));
  public static final DeferredBlock<GatedLoadBlock> CARBON_SCRUBBER =
      BLOCKS.register(
          "carbon_scrubber",
          () ->
              new GatedLoadBlock(
                  machineProperties(), TALL_CELL, TALL_CELL, "mars:carbon_scrubber"));
  public static final DeferredBlock<SolarPanelBlock> SOLAR_PANEL =
      machine("solar_panel", PANEL, SolarPanelBlock::new);
  public static final DeferredBlock<BatteryBlock> BATTERY_BLOCK =
      BLOCKS.register("battery", () -> new BatteryBlock(machineProperties(), BATTERY));
  public static final DeferredBlock<PoweredMachineBlock> OXYGEN_MAKER_BLOCK =
      BLOCKS.register(
          "oxygen_maker", () -> new PoweredMachineBlock(machineProperties(), OXYGEN_MAKER));
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
          WATER_RECLAIMER,
          ATMOSPHERIC_PROCESSOR,
          SUPER_GREENHOUSE_EMITTER,
          ICE_EXTRACTOR,
          ALGAE_VAT,
          CARBON_SCRUBBER,
          SOLAR_PANEL,
          BATTERY_BLOCK,
          OXYGEN_MAKER_BLOCK,
          PLANETARY_MONITOR);

  /**
   * The gate input items (docs/design/gate-input-items.md): parts the next machine's recipe needs.
   * Plain items with a one-line tooltip; the gate that hands them out lists them in its {@code
   * cache}. Ids are the contract. All amounts and recipes are INVENTED.
   */
  public static final List<String> PART_IDS =
      List.of("heater_coil", "filter_cartridge", "pump_bearing", "sorbent_bed");

  public static final List<net.neoforged.neoforge.registries.DeferredItem<Item>> PARTS =
      new java.util.ArrayList<>();

  static {
    for (String id : PART_IDS) {
      PARTS.add(
          ITEMS.<Item>register(
              id,
              () ->
                  new Item(new Item.Properties()) {
                    @Override
                    public void appendHoverText(
                        ItemStack stack,
                        Item.TooltipContext context,
                        List<Component> tooltip,
                        net.minecraft.world.item.TooltipFlag flag) {
                      tooltip.add(
                          Component.translatable("item.mars." + id + ".tooltip")
                              .withStyle(net.minecraft.ChatFormatting.GRAY));
                    }
                  }));
    }
  }

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
                        PARTS.forEach(p -> output.accept(p.get()));
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
    // The Oven's tooltip names the missing water (balance sheet 2c decision 6).
    ITEMS.register(
        "regolith_oven",
        () ->
            new net.minecraft.world.item.BlockItem(REGOLITH_OVEN.get(), new Item.Properties()) {
              @Override
              public void appendHoverText(
                  ItemStack stack,
                  Item.TooltipContext context,
                  List<Component> tooltip,
                  net.minecraft.world.item.TooltipFlag flag) {
                tooltip.add(Component.translatable("monitor.oven.tooltip"));
              }
            });
    ITEMS.register("oxygen_maker", () -> tipped(OXYGEN_MAKER_BLOCK));
    ITEMS.registerSimpleBlockItem("battery", BATTERY_BLOCK, new Item.Properties());
    ITEMS.register("atmospheric_processor", () -> tipped(ATMOSPHERIC_PROCESSOR));
    ITEMS.register("super_greenhouse_emitter", () -> tipped(SUPER_GREENHOUSE_EMITTER));
    ITEMS.register("ice_extractor", () -> tipped(ICE_EXTRACTOR));
    ITEMS.register("algae_vat", () -> tipped(ALGAE_VAT));
    ITEMS.register("carbon_scrubber", () -> tipped(CARBON_SCRUBBER));
    ITEMS.register(
        "water_reclaimer",
        () ->
            new net.minecraft.world.item.BlockItem(WATER_RECLAIMER.get(), new Item.Properties()) {
              @Override
              public void appendHoverText(
                  ItemStack stack,
                  Item.TooltipContext context,
                  List<Component> tooltip,
                  net.minecraft.world.item.TooltipFlag flag) {
                tooltip.add(Component.translatable("block.mars.water_reclaimer.tooltip"));
              }
            });
  }

  /** A block item whose tooltip is the block's {@code .tooltip} lang key. */
  private static Item tipped(DeferredBlock<? extends Block> block) {
    return new net.minecraft.world.item.BlockItem(block.get(), new Item.Properties()) {
      @Override
      public void appendHoverText(
          ItemStack stack,
          Item.TooltipContext context,
          List<Component> tooltip,
          net.minecraft.world.item.TooltipFlag flag) {
        tooltip.add(Component.translatable(block.get().getDescriptionId() + ".tooltip"));
      }
    };
  }

  public static void register(IEventBus modBus) {
    BLOCKS.register(modBus);
    ITEMS.register(modBus);
    TABS.register(modBus);
  }
}
