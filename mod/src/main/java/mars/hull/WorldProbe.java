package mars.hull;

import mars.dome.Probe;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * The flood fill's view of a level. A cell is OUTSIDE when it is outside the build height, in an
 * unloaded chunk (never loads one), or open to the sky (nothing motion-blocking above it, read from
 * the heightmap, which the game updates the moment a block changes).
 */
public final class WorldProbe implements Probe {
  private final ServerLevel level;
  private final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

  public WorldProbe(ServerLevel level) {
    this.level = level;
  }

  @Override
  public Cell at(int x, int y, int z) {
    if (y < level.getMinBuildHeight() || y >= level.getMaxBuildHeight()) {
      return Cell.OUTSIDE;
    }
    pos.set(x, y, z);
    if (!level.hasChunkAt(pos)) {
      return Cell.OUTSIDE;
    }
    if (Airtight.test(level.getBlockState(pos), level, pos)) {
      return Cell.AIRTIGHT;
    }
    return level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z) <= y ? Cell.OUTSIDE : Cell.OPEN;
  }
}
