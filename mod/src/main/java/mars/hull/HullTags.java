package mars.hull;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

/** Block tags for sealing (data in {@code pack/data/mars/tags/block}). */
public final class HullTags {
  /** Blocks that stop air even if they are not full cubes. */
  public static final TagKey<Block> AIRTIGHT = tag("airtight");

  /**
   * Blocks that let air through even if they are full cubes (a vent grating). Wins over AIRTIGHT.
   */
  public static final TagKey<Block> NOT_AIRTIGHT = tag("not_airtight");

  /** Blocks a Seal Kit can patch (today only the cracked hull plate). */
  public static final TagKey<Block> PATCHABLE = tag("patchable");

  private HullTags() {}

  private static TagKey<Block> tag(String path) {
    return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("mars", path));
  }
}
