package mars.hull;

import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers the dome's building blocks (Stage 3 slice 1): hull plate, hull glass and hull door.
 * Registry IDs are the contract; textures are straw-man until the Artist's pass. They share the
 * machines creative tab (see {@link mars.machines.MarsMachines#TAB}). No recipes yet: crafting
 * arrives with slice 2.
 */
public final class HullBlocks {
  public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks("mars");
  public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems("mars");

  public static final DeferredBlock<HullPlateBlock> HULL_PLATE =
      BLOCKS.register(
          "hull_plate",
          () ->
              new HullPlateBlock(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.METAL)
                      .strength(3.0f, 12.0f)
                      .sound(SoundType.METAL)));
  public static final DeferredBlock<HullGlassBlock> HULL_GLASS =
      BLOCKS.register(
          "hull_glass",
          () ->
              new HullGlassBlock(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.NONE)
                      .strength(1.5f, 12.0f)
                      .sound(SoundType.GLASS)
                      .noOcclusion()
                      .isValidSpawn((s, l, p, e) -> false)
                      .isRedstoneConductor((s, l, p) -> false)
                      .isSuffocating((s, l, p) -> false)
                      .isViewBlocking((s, l, p) -> false)));

  /**
   * The cracked plate of the starting dome (Landing step 5). Not in {@code mars:airtight}, so air
   * leaks through it; the player breaks it and puts a hull plate there. No loot, not in the tab.
   */
  public static final DeferredBlock<Block> HULL_PLATE_CRACKED =
      BLOCKS.register(
          "hull_plate_cracked",
          () ->
              new HullPlateBlock(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.METAL)
                      .strength(1.0f, 6.0f)
                      .sound(SoundType.METAL)
                      .noLootTable()));

  public static final DeferredBlock<HullDoorBlock> HULL_DOOR =
      BLOCKS.register(
          "hull_door",
          () ->
              new HullDoorBlock(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.METAL)
                      .strength(3.0f, 12.0f)
                      .sound(SoundType.METAL)
                      .noOcclusion()));

  public static final DeferredBlock<HullPlateBlock> HULL_PLATE_BASE = plate("hull_plate_base");
  public static final DeferredBlock<HullPlateBlock> HULL_PLATE_RIM = plate("hull_plate_rim");

  /** Hab B roof glass: ribbed greenhouse panes (Artist, habitat restyle). Airtight like glass. */
  public static final DeferredBlock<HullGlassBlock> HULL_GLASS_RIBBED =
      BLOCKS.register("hull_glass_ribbed", () -> new HullGlassBlock(glassProperties()));

  /** Roof fixtures: decoration only, not airtight, no sealing role. */
  public static final DeferredBlock<RoofFixtureBlock> ROOF_VENT =
      BLOCKS.register(
          "roof_vent",
          () ->
              new RoofFixtureBlock(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.METAL)
                      .strength(2.0f, 6.0f)
                      .sound(SoundType.METAL)
                      .noOcclusion(),
                  RoofFixtureBlock.VENT_SHAPE));

  public static final DeferredBlock<RoofFixtureBlock> ROOF_DISH =
      BLOCKS.register(
          "roof_dish",
          () ->
              new RoofFixtureBlock(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.METAL)
                      .strength(2.0f, 6.0f)
                      .sound(SoundType.METAL)
                      .noOcclusion(),
                  RoofFixtureBlock.DISH_SHAPE));

  /**
   * Hull Light Panel: an airtight roof plate that emits {@link #LIGHT_LEVEL}. Measured against the
   * lit rooms in the dome (see ARCHITECTURE.md 2.20 addendum).
   */
  public static final int LIGHT_LEVEL = 12;

  public static final DeferredBlock<HullPlateBlock> HULL_LIGHT =
      plate("hull_light", BlockBehaviour.Properties.of().lightLevel(s -> LIGHT_LEVEL));

  private static DeferredBlock<HullPlateBlock> plate(String id) {
    return plate(id, BlockBehaviour.Properties.of());
  }

  private static DeferredBlock<HullPlateBlock> plate(String id, BlockBehaviour.Properties base) {
    return BLOCKS.register(
        id,
        () ->
            new HullPlateBlock(
                base.mapColor(MapColor.METAL).strength(3.0f, 12.0f).sound(SoundType.METAL)));
  }

  private static BlockBehaviour.Properties glassProperties() {
    return BlockBehaviour.Properties.of()
        .mapColor(MapColor.NONE)
        .strength(1.5f, 12.0f)
        .sound(SoundType.GLASS)
        .noOcclusion()
        .isValidSpawn((s, l, p, e) -> false)
        .isRedstoneConductor((s, l, p) -> false)
        .isSuffocating((s, l, p) -> false)
        .isViewBlocking((s, l, p) -> false);
  }

  static {
    ITEMS.registerSimpleBlockItem("hull_plate", HULL_PLATE, new Item.Properties());
    ITEMS.registerSimpleBlockItem("hull_glass", HULL_GLASS, new Item.Properties());
    ITEMS.registerSimpleBlockItem("hull_door", HULL_DOOR, new Item.Properties());
    ITEMS.registerSimpleBlockItem("hull_plate_base", HULL_PLATE_BASE, new Item.Properties());
    ITEMS.registerSimpleBlockItem("hull_plate_rim", HULL_PLATE_RIM, new Item.Properties());
    ITEMS.registerSimpleBlockItem("hull_glass_ribbed", HULL_GLASS_RIBBED, new Item.Properties());
    ITEMS.registerSimpleBlockItem("hull_light", HULL_LIGHT, new Item.Properties());
    ITEMS.registerSimpleBlockItem("roof_vent", ROOF_VENT, new Item.Properties());
    ITEMS.registerSimpleBlockItem("roof_dish", ROOF_DISH, new Item.Properties());
  }

  /** In creative-tab order. */
  public static final List<DeferredBlock<? extends Block>> ALL =
      List.of(
          HULL_PLATE,
          HULL_PLATE_BASE,
          HULL_PLATE_RIM,
          HULL_LIGHT,
          HULL_GLASS,
          HULL_GLASS_RIBBED,
          HULL_DOOR,
          ROOF_VENT,
          ROOF_DISH);

  private HullBlocks() {}

  public static void register(IEventBus modBus) {
    BLOCKS.register(modBus);
    ITEMS.register(modBus);
  }
}
