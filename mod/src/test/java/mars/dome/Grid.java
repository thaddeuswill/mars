package mars.dome;

/**
 * A tiny world for sealing tests, written as ASCII layers. {@code #} airtight, {@code .} open air,
 * {@code M} a Monitor (airtight casing), anything outside the grid or marked {@code ^} is OUTSIDE
 * (open sky, world edge). Layers are y from the bottom; rows are z; columns are x.
 */
final class Grid implements Probe {
  private final char[][][] c; // [y][z][x]
  int probes;

  /** Cells that act as a door: closed = airtight. */
  boolean doorClosed = true;

  Grid(String[]... layers) {
    c = new char[layers.length][][];
    for (int y = 0; y < layers.length; y++) {
      c[y] = new char[layers[y].length][];
      for (int z = 0; z < layers[y].length; z++) {
        c[y][z] = layers[y][z].toCharArray();
      }
    }
  }

  void set(int x, int y, int z, char ch) {
    c[y][z][x] = ch;
  }

  @Override
  public Cell at(int x, int y, int z) {
    probes++;
    if (y < 0 || y >= c.length || z < 0 || z >= c[y].length || x < 0 || x >= c[y][z].length) {
      return Cell.OUTSIDE;
    }
    return switch (c[y][z][x]) {
      case '#', 'M' -> Cell.AIRTIGHT;
      case 'D' -> doorClosed ? Cell.AIRTIGHT : Cell.OPEN;
      case '^' -> Cell.OUTSIDE;
      default -> Cell.OPEN;
    };
  }

  /** A solid {@code n} x {@code n} x {@code n} box of hull with an empty inside. */
  static Grid room(int n) {
    String[][] layers = new String[n][n];
    for (int y = 0; y < n; y++) {
      for (int z = 0; z < n; z++) {
        StringBuilder row = new StringBuilder();
        for (int x = 0; x < n; x++) {
          boolean wall = x == 0 || x == n - 1 || y == 0 || y == n - 1 || z == 0 || z == n - 1;
          row.append(wall ? '#' : '.');
        }
        layers[y][z] = row.toString();
      }
    }
    return new Grid(layers);
  }
}
