package mars.dome;

import java.util.ArrayList;
import java.util.List;

/**
 * The starting dome as a pure list of cells (docs/design/dome-layout.md, group of 5 shell, 23 x 16
 * blocks). No Minecraft types: the placer in {@code mars.hull} turns each {@link Kind} into a
 * block. Keeping the shape here lets a unit test run the flood fill on the real layout.
 *
 * <p>Coordinates are relative to the dome's north-west corner at floor level: x east 0..22, z south
 * 0..15, y up 0..4 (floor 0, air 1..3, roof 4). The plan below is the one in dome-layout.md section
 * 2, with the legend of its section 2 key. Cost: built once, about 23 x 16 x 5 = 1,840 cells, only
 * when the dome is placed.
 */
public final class DomeTemplate {
  public static final int SIZE_X = 23;
  public static final int SIZE_Z = 16;
  public static final int SIZE_Y = 5;

  /** Where the player stands on arrival: the shelter, near its door (dome-layout section 4). */
  public static final int SPAWN_X = 3;

  public static final int SPAWN_Z = 4;

  /** The cracked plate of Hab B's west wall (Landing step 5). */
  public static final int BREACH_X = 0;

  public static final int BREACH_Z = 12;

  /**
   * Roof fixtures stand on the roof at y 5, above the shelter (x 1..6, z 1..7). They are placed by
   * {@code StarterDome}, are not part of {@link #cells} and never take part in sealing: SIZE_Y
   * stays 5.
   */
  public static final int ROOF_FIXTURE_Y = SIZE_Y;

  public static final int DISH_X = 4;
  public static final int DISH_Z = 3;
  public static final int VENT_X = 3;
  public static final int VENT_Z = 6;

  /** What a cell holds. */
  public enum Kind {
    AIR,
    HULL,
    /** Hull plate, base course: the perimeter wall at y 1 (cream band). */
    HULL_BASE,
    /** Hull plate, roof rim: the perimeter roof at y 4 (cream band). */
    HULL_RIM,
    GLASS,
    /** Ribbed greenhouse glass: Hab B's roof. */
    GLASS_RIBBED,
    /** The lower half of a door (y 1). */
    DOOR,
    /** The upper half of a door (y 2). */
    DOOR_UPPER,
    /** The cracked hull plate: a block that is not airtight until it is replaced. */
    CRACKED,
    MONITOR,
    TANK,
    MAKER,
    RECLAIMER,
    BATTERY,
    BED_FOOT,
    BED_HEAD,
    LECTERN,
    BENCH,
    CRATE
  }

  /** Which way a block faces (the direction its front looks). */
  public enum Face {
    NORTH,
    SOUTH,
    EAST,
    WEST
  }

  public record Cell(int x, int y, int z, Kind kind, Face face) {}

  /** The plan, z0 at the top. One character per column. */
  private static final String[] PLAN = {
    "#########=====#########",
    "#b....b#SJ.M.CC#XXXXXW#",
    "#b....b#.......#......#",
    "#b.....#.......#.....B#",
    "#b.....D.......D.....B#",
    "#b....b#.......#.....B#",
    "#b....b#.......#......#",
    "#TT.TTT#L......#OO....#",
    "###D#######D######D####",
    "#......#RR...RR#......#",
    "#......#.....FF#cc....#",
    "#......####D####......E",
    "*......###...###cccc..#",
    "#gggggg###d..###......#",
    "#gggggg###...###......#",
    "###########A###########"
  };

  private DomeTemplate() {}

  /**
   * All cells, with {@code makers} oxygen makers filled in the five maker bays (balance sheet 2d.2:
   * ceil(group / 2) are pre-built). {@code breached} leaves the cracked plate in Hab B's wall;
   * false puts a whole hull plate there (the repaired dome).
   */
  public static List<Cell> cells(int makers, boolean breached) {
    List<Cell> out = new ArrayList<>(SIZE_X * SIZE_Z * SIZE_Y);
    int makersLeft = Math.max(0, Math.min(5, makers));
    for (int z = 0; z < SIZE_Z; z++) {
      for (int x = 0; x < SIZE_X; x++) {
        char c = PLAN[z].charAt(x);
        out.add(new Cell(x, 0, z, Kind.HULL, Face.NORTH)); // the floor is hull plate everywhere
        out.add(
            new Cell(
                x,
                SIZE_Y - 1,
                z,
                annexRoof(x, z) ? Kind.GLASS_RIBBED : perimeter(x, z) ? Kind.HULL_RIM : Kind.HULL,
                Face.NORTH)); // the roof
        switch (c) {
          case '#' -> {
            for (int y = 1; y <= 3; y++) {
              out.add(
                  new Cell(
                      x, y, z, y == 1 && perimeter(x, z) ? Kind.HULL_BASE : Kind.HULL, Face.NORTH));
            }
          }
          case '=' -> {
            out.add(new Cell(x, 1, z, Kind.HULL, Face.NORTH));
            out.add(new Cell(x, 2, z, Kind.GLASS, Face.NORTH));
            out.add(new Cell(x, 3, z, Kind.GLASS, Face.NORTH));
          }
          case '*' -> {
            for (int y = 1; y <= 3; y++) {
              Kind k = breached && y == 2 ? Kind.CRACKED : y == 1 ? Kind.HULL_BASE : Kind.HULL;
              out.add(new Cell(x, y, z, k, Face.NORTH));
            }
          }
          case 'E' -> {
            for (int y = 1; y <= 3; y++) {
              out.add(
                  new Cell(
                      x,
                      y,
                      z,
                      y == 1 ? Kind.HULL_BASE : Kind.HULL,
                      Face.NORTH)); // sealed hatch stub
            }
          }
          case 'D', 'A' -> {
            out.add(new Cell(x, 1, z, Kind.DOOR, doorFace(x, z)));
            out.add(new Cell(x, 2, z, Kind.DOOR_UPPER, doorFace(x, z)));
            out.add(new Cell(x, 3, z, Kind.HULL, Face.NORTH)); // lintel
          }
          case 'M' -> out.add(new Cell(x, 1, z, Kind.MONITOR, Face.SOUTH));
          case 'T' -> out.add(new Cell(x, 1, z, Kind.TANK, Face.NORTH));
          case 'X' -> {
            if (makersLeft > 0) {
              makersLeft--;
              out.add(new Cell(x, 1, z, Kind.MAKER, Face.SOUTH));
            }
          }
          case 'W' -> out.add(new Cell(x, 1, z, Kind.RECLAIMER, Face.SOUTH));
          case 'B' -> {
            // Three battery slots; the first one (z3) is filled at the start (landing step 6).
            if (z == 3) {
              out.add(new Cell(x, 1, z, Kind.BATTERY, Face.WEST));
            }
          }
          case 'b' -> {
            // Bunks are two blocks long along z: the foot on the north cell, the head south.
            int above = 0;
            while (z - above - 1 >= 0 && PLAN[z - above - 1].charAt(x) == 'b') {
              above++;
            }
            boolean foot = above % 2 == 0;
            out.add(new Cell(x, 1, z, foot ? Kind.BED_FOOT : Kind.BED_HEAD, Face.SOUTH));
          }
          case 'J' -> out.add(new Cell(x, 1, z, Kind.LECTERN, Face.SOUTH));
          case 'C' -> out.add(new Cell(x, 1, z, Kind.BENCH, Face.SOUTH));
          case 'L', 'c' -> out.add(new Cell(x, 1, z, Kind.CRATE, Face.NORTH));
          default -> {
            // '.', bays, racks, refill points, greenhouse beds and the dust-off station are
            // floor markers in this slice: nothing is built there yet.
          }
        }
      }
    }
    return out;
  }

  /** Cells on the outer wall of the 23 x 16 shell. */
  private static boolean perimeter(int x, int z) {
    return x == 0 || x == SIZE_X - 1 || z == 0 || z == SIZE_Z - 1;
  }

  private static boolean annexRoof(int x, int z) {
    return x >= 1 && x <= 6 && z >= 9 && z <= 14;
  }

  /** A door in a wall that runs along z (an east-west passage) faces east; otherwise south. */
  private static Face doorFace(int x, int z) {
    boolean eastWest = x == 7 || x == 15;
    return eastWest ? Face.EAST : Face.SOUTH;
  }

  /** The cell kinds in a plan column, for tests that want to inspect the plan itself. */
  public static char plan(int x, int z) {
    return PLAN[z].charAt(x);
  }
}
