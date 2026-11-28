package mars.landing;

/**
 * Where each step's target stands, in dome-template coordinates (x east, y up, z south from the
 * north-west floor corner; dome-layout.md section 2), and the compass word for a distance chip.
 * Pure. Step 8 has no place (the book is in the hotbar).
 */
public final class Targets {
  private Targets() {}

  /** {x, y, z} of the step's target, or null. */
  public static int[] of(Step step) {
    return switch (step) {
      case DOOR -> new int[] {7, 1, 4};
      case TANK -> new int[] {1, 1, 7};
      case MONITOR -> new int[] {11, 1, 1};
      case KIT -> new int[] {8, 1, 7};
      case PATCH -> new int[] {0, 2, 12};
      case OVENS -> new int[] {18, 1, 4};
      case AIRLOCK -> new int[] {11, 1, 15};
      case JOURNAL -> null;
    };
  }

  /** "north", "south", "east" or "west": the larger of the two offsets (z grows to the south). */
  public static String direction(double dx, double dz) {
    if (Math.abs(dx) > Math.abs(dz)) {
      return dx > 0 ? "east" : "west";
    }
    return dz > 0 ? "south" : "north";
  }
}
