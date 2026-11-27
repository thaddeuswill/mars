package mars.dome;

/**
 * What the flood fill may ask about the world: one answer per block position. The Minecraft layer
 * implements it over a level; tests implement it over a few characters of ASCII art.
 */
@FunctionalInterface
public interface Probe {
  /** What a position is, for sealing. */
  enum Cell {
    /** A full block, a closed door, a machine casing: air cannot pass. */
    AIRTIGHT,
    /** Air, or any block that is not airtight (a slab, a fence, an open door): air passes. */
    OPEN,
    /** Open sky, an unloaded chunk or beyond the world: the fill has left the building. */
    OUTSIDE
  }

  Cell at(int x, int y, int z);
}
