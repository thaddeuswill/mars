package mars.terrain;

/**
 * The flat slab that every Mars world had before W1: copied from the old {@code
 * data/mars/dimension/planet_1.json} (flat generator, layers bedrock 1, stone 56, red_sandstone 4,
 * red_sand 3, starting at the world's bottom y 0). Existing saves keep generating exactly this.
 */
public final class LegacyFlat {
  /** Layer block ids from the bottom up, with heights. */
  public static final String[] BLOCKS = {
    "minecraft:bedrock", "minecraft:stone", "minecraft:red_sandstone", "minecraft:red_sand"
  };

  public static final int[] HEIGHTS = {1, 56, 4, 3};

  private LegacyFlat() {}

  /** y of the top block (63 for the layers above, with the world's bottom at y 0). */
  public static int topY(int minY) {
    int sum = 0;
    for (int h : HEIGHTS) {
      sum += h;
    }
    return minY + sum - 1;
  }

  /** The block id at world y, or null above the slab. */
  public static String blockAt(int y, int minY) {
    int bottom = minY;
    for (int i = 0; i < HEIGHTS.length; i++) {
      if (y < bottom + HEIGHTS[i]) {
        return y >= bottom ? BLOCKS[i] : null;
      }
      bottom += HEIGHTS[i];
    }
    return null;
  }
}
