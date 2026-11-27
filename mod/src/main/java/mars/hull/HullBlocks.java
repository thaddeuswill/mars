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

  static {
    ITEMS.registerSimpleBlockItem("hull_plate", HULL_PLATE, new Item.Properties());
    ITEMS.registerSimpleBlockItem("hull_glass", HULL_GLASS, new Item.Properties());
    ITEMS.registerSimpleBlockItem("hull_door", HULL_DOOR, new Item.Properties());
  }

  /** In creative-tab order. */
  public static final List<DeferredBlock<? extends Block>> ALL =
      List.of(HULL_PLATE, HULL_GLASS, HULL_DOOR);

  private HullBlocks() {}

  public static void register(IEventBus modBus) {
    BLOCKS.register(modBus);
    ITEMS.register(modBus);
  }
}
