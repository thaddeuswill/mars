package mars.world;

import java.util.List;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
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

  // ---- W2 Tier A surface blocks (docs/science/mars-surface-materials.md). Plain cubes; hardness
  // values are INVENTED game numbers. co2_frost is deferred (needs a polar biome and a rule).

  /** Thin salt-cemented crust under the dust (NTRS 20050203800). Breaks into red sand. */
  public static final DeferredBlock<Block> DURICRUST =
      block(
          "duricrust",
          BlockBehaviour.Properties.of()
              .mapColor(MapColor.TERRACOTTA_ORANGE)
              .strength(1.0f)
              .sound(SoundType.GRAVEL));

  /** Lava rock with gas holes: dark grey-brown, never glowing. */
  public static final DeferredBlock<Block> VESICULAR_BASALT =
      block(
          "vesicular_basalt",
          BlockBehaviour.Properties.of()
              .mapColor(MapColor.COLOR_GRAY)
              .strength(1.5f, 6.0f)
              .requiresCorrectToolForDrops()
              .sound(SoundType.STONE));

  /** Dark sand with grey hematite spherules ("blueberries" are grey). Drops iron nuggets. */
  public static final DeferredBlock<Block> SPHERULE_SAND =
      block(
          "spherule_sand",
          BlockBehaviour.Properties.of()
              .mapColor(MapColor.COLOR_BROWN)
              .strength(0.6f)
              .sound(SoundType.SAND));

  /** Angular rock fragments in a dark matrix: crater rims and ejecta. */
  public static final DeferredBlock<Block> IMPACT_BRECCIA =
      block(
          "impact_breccia",
          BlockBehaviour.Properties.of()
              .mapColor(MapColor.COLOR_GRAY)
              .strength(2.0f, 6.0f)
              .requiresCorrectToolForDrops()
              .sound(SoundType.STONE));

  /** Bright white gypsum veins in layered rock (water once moved through cracks). */
  public static final DeferredBlock<Block> GYPSUM_VEIN =
      block(
          "gypsum_vein",
          BlockBehaviour.Properties.of()
              .mapColor(MapColor.TERRACOTTA_WHITE)
              .strength(1.5f, 3.0f)
              .requiresCorrectToolForDrops()
              .sound(SoundType.STONE));

  // ---- Conductor ores and carbonate (docs/science/mars-conductor-and-carbonate-ores.md). The two
  // ores are INVENTED (no source says Mars has minable copper or redstone material); the carbonate
  // vein is REAL but game-sized. Each shows its one-line honest label as the item tooltip (lang key
  // block.mars.<id>.tooltip). Hardness values copy vanilla copper and redstone ore.

  /** Rare grey-brown rock with rust and dull gold flecks. Drops raw copper. INVENTED. */
  public static final DeferredBlock<Block> SULFIDE_VEIN =
      labelled(
          "sulfide_vein",
          BlockBehaviour.Properties.of()
              .mapColor(MapColor.COLOR_GRAY)
              .strength(3.0f, 3.0f)
              .requiresCorrectToolForDrops()
              .sound(SoundType.STONE));

  /** Rare dark rock with metallic speckle. Drops redstone dust. INVENTED. */
  public static final DeferredBlock<Block> MAGNETIC_ORE =
      labelled(
          "magnetic_ore",
          BlockBehaviour.Properties.of()
              .mapColor(MapColor.DEEPSLATE)
              .strength(3.0f, 3.0f)
              .requiresCorrectToolForDrops()
              .sound(SoundType.STONE));

  /** Small veins of pale carbonate rock. REAL, game-sized. Drops the carbonate item. */
  public static final DeferredBlock<Block> CARBONATE_ROCK =
      labelled(
          "carbonate_rock",
          BlockBehaviour.Properties.of()
              .mapColor(MapColor.TERRACOTTA_WHITE)
              .strength(1.5f, 3.0f)
              .requiresCorrectToolForDrops()
              .sound(SoundType.STONE));

  /** Muted tan sand: the light stripe of a sand ripple on dune faces (planet data ripple_block). */
  public static final DeferredBlock<Block> RIPPLE_SAND =
      block(
          "ripple_sand",
          BlockBehaviour.Properties.of()
              .mapColor(MapColor.TERRACOTTA_ORANGE)
              .strength(0.5f)
              .sound(SoundType.SAND));

  // ---- The staged ground ladder (docs/design/inspiration-survey-mechanics.md, Planet Crafter and
  // Farmer's Delight): raw regolith (vanilla red sand) -> washed regolith -> rich soil ->
  // biocrust. Plain cubes for now; spreading and planting are W3. Recipes and amounts are
  // INVENTED and live in data (data/mars/recipe, data/mars/ladder/ground_ladder.json).

  /** Regolith rinsed with water. That washing removes toxic salts is game design, NOT VERIFIED. */
  public static final DeferredBlock<Block> WASHED_REGOLITH =
      labelled(
          "washed_regolith",
          BlockBehaviour.Properties.of()
              .mapColor(MapColor.TERRACOTTA_ORANGE)
              .strength(0.5f)
              .sound(SoundType.SAND));

  /** Washed regolith mixed with bone meal. Made up for the game. */
  public static final DeferredBlock<Block> RICH_SOIL =
      labelled(
          "rich_soil",
          BlockBehaviour.Properties.of()
              .mapColor(MapColor.TERRACOTTA_BROWN)
              .strength(0.5f)
              .sound(SoundType.GRAVEL));

  /** The top step: a crust a seed kit starts. A stand-in for living crust; made up for the game. */
  public static final DeferredBlock<Block> BIOCRUST =
      labelled(
          "biocrust",
          BlockBehaviour.Properties.of()
              .mapColor(MapColor.COLOR_GRAY)
              .strength(0.6f)
              .sound(SoundType.GRAVEL));

  /** What a carbonate rock drops; the seed kit recipe uses it (tag mars:seed_kit_carbonate). */
  public static final DeferredItem<Item> CARBONATE = ITEMS.registerSimpleItem("carbonate");

  /**
   * The four seed kits (docs/design/seed-kit-recipes.md): plain items with a tooltip for now. Their
   * recipes are data/mars/recipe/*_kit.json; what placing one does is a later slice (W3). Texture
   * is a placeholder (the carbonate icon) until the Artist draws them. The tooltips must not name
   * any gas (forecast F8 guard).
   */
  public static final List<String> SEED_KIT_IDS =
      List.of("biocrust_kit", "meadow_kit", "wetland_kit", "forest_kit");

  public static final List<DeferredItem<Item>> SEED_KITS = new java.util.ArrayList<>();

  static {
    for (String id : SEED_KIT_IDS) {
      SEED_KITS.add(
          ITEMS.<Item>register(
              id,
              () ->
                  new Item(new Item.Properties()) {
                    @Override
                    public void appendHoverText(
                        ItemStack stack,
                        Item.TooltipContext context,
                        List<Component> lines,
                        TooltipFlag flag) {
                      lines.add(
                          Component.translatable("item.mars." + id + ".tooltip")
                              .withStyle(ChatFormatting.GRAY));
                    }
                  }));
    }
  }

  /** Every terrain block, in creative-tab order. */
  public static final List<DeferredBlock<Block>> ALL =
      List.of(
          BASALTIC_SAND,
          HEMATITE_ORE,
          DARK_SAND_ORE,
          GROUND_ICE,
          DURICRUST,
          VESICULAR_BASALT,
          SPHERULE_SAND,
          IMPACT_BRECCIA,
          GYPSUM_VEIN,
          SULFIDE_VEIN,
          MAGNETIC_ORE,
          CARBONATE_ROCK,
          RIPPLE_SAND,
          WASHED_REGOLITH,
          RICH_SOIL,
          BIOCRUST);

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
                        output.accept(CARBONATE.get());
                        SEED_KITS.forEach(k -> output.accept(k.get()));
                      })
                  .build());

  private MarsTerrainBlocks() {}

  private static DeferredBlock<Block> block(String id, BlockBehaviour.Properties props) {
    DeferredBlock<Block> b = BLOCKS.registerSimpleBlock(id, props);
    ITEMS.registerSimpleBlockItem(id, b, new Item.Properties());
    return b;
  }

  /** A block whose item shows its honest one-line label (lang key block.mars.id.tooltip). */
  private static DeferredBlock<Block> labelled(String id, BlockBehaviour.Properties props) {
    DeferredBlock<Block> b = BLOCKS.registerSimpleBlock(id, props);
    Supplier<Item> item =
        () ->
            new BlockItem(b.get(), new Item.Properties()) {
              @Override
              public void appendHoverText(
                  ItemStack stack,
                  Item.TooltipContext context,
                  List<Component> lines,
                  TooltipFlag flag) {
                lines.add(
                    Component.translatable("block.mars." + id + ".tooltip")
                        .withStyle(ChatFormatting.GRAY));
              }
            };
    ITEMS.register(id, item);
    return b;
  }

  public static void register(IEventBus modBus) {
    BLOCKS.register(modBus);
    ITEMS.register(modBus);
    TABS.register(modBus);
  }
}
