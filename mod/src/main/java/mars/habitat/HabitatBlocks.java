package mars.habitat;

import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Habitat furniture of the starting dome: bunk and crate (the Work Bench stays a crafting table
 * until machine recipes exist). Both listed in the machines creative tab.
 */
public final class HabitatBlocks {
  public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks("mars");
  public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems("mars");

  public static final DeferredBlock<HabitatBunkBlock> HABITAT_BUNK =
      BLOCKS.register(
          "habitat_bunk",
          () ->
              new HabitatBunkBlock(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.COLOR_LIGHT_BLUE)
                      .sound(SoundType.WOOL)
                      .strength(0.5f)
                      .noOcclusion()
                      .pushReaction(PushReaction.DESTROY)));

  public static final DeferredBlock<StowageCrateBlock> STOWAGE_CRATE =
      BLOCKS.register(
          "stowage_crate",
          () ->
              new StowageCrateBlock(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.METAL)
                      .sound(SoundType.METAL)
                      .strength(2.0f, 6.0f)
                      .noOcclusion()));

  public static final DeferredItem<Item> HABITAT_BUNK_ITEM =
      ITEMS.register("habitat_bunk", () -> new BedItem(HABITAT_BUNK.get(), new Item.Properties()));
  public static final DeferredItem<net.minecraft.world.item.BlockItem> STOWAGE_CRATE_ITEM =
      ITEMS.registerSimpleBlockItem("stowage_crate", STOWAGE_CRATE, new Item.Properties());

  private HabitatBlocks() {}

  public static void register(IEventBus modBus) {
    BLOCKS.register(modBus);
    ITEMS.register(modBus);
    modBus.addListener(HabitatBlocks::addBlocksToEntityTypes);
  }

  /** The crate stores items in the barrel's block entity. The bunk has none. */
  static void addBlocksToEntityTypes(BlockEntityTypeAddBlocksEvent event) {
    event.modify(BlockEntityType.BARREL, STOWAGE_CRATE.get());
  }
}
