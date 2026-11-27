package mars.dome;

/**
 * Block positions packed in one long, bit for bit the same layout as Minecraft's {@code
 * BlockPos.asLong} (x 26 bits, z 26 bits, y 12 bits), so saved machine positions and dome positions
 * are interchangeable. Pure arithmetic, no Minecraft classes; a boot check compares it with {@code
 * BlockPos}.
 */
public final class Pos {
  private static final int PACKED_X = 26;
  private static final int PACKED_Z = 26;
  private static final int PACKED_Y = 12;
  private static final long MASK_X = (1L << PACKED_X) - 1L;
  private static final long MASK_Y = (1L << PACKED_Y) - 1L;
  private static final long MASK_Z = (1L << PACKED_Z) - 1L;
  private static final int Y_SHIFT = 0;
  private static final int Z_SHIFT = PACKED_Y;
  private static final int X_SHIFT = PACKED_Y + PACKED_Z;

  private Pos() {}

  public static long pack(int x, int y, int z) {
    return ((x & MASK_X) << X_SHIFT) | ((y & MASK_Y) << Y_SHIFT) | ((z & MASK_Z) << Z_SHIFT);
  }

  public static int x(long p) {
    return (int) (p << (64 - X_SHIFT - PACKED_X) >> (64 - PACKED_X));
  }

  public static int y(long p) {
    return (int) (p << (64 - PACKED_Y) >> (64 - PACKED_Y));
  }

  public static int z(long p) {
    return (int) (p << (64 - Z_SHIFT - PACKED_Z) >> (64 - PACKED_Z));
  }
}
