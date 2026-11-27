package mars.dome;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

/**
 * The flood fill behind "is this room sealed?" (ARCHITECTURE.md 2.3). Breadth first from a start
 * cell through OPEN cells, six neighbours, nothing diagonal.
 *
 * <p>Two guards keep it cheap and finite: it stops at the first OUTSIDE cell (a hole to the sky),
 * and it stops when more than {@code cap} open cells have been counted (a room too big to be a
 * dome, or open ground). Cost: at most {@code cap + 1} open cells, each probed once with its six
 * neighbours, so at most about {@code 6 * (cap + 1)} probe calls; the cap is the only thing that
 * ends a fill in open terrain.
 *
 * <p>The result depends only on the probed positions, which the result lists. A changed block can
 * alter the result only if it is one of them, which is what makes the cache in {@link DomeSeals}
 * exact: any other edit cannot matter.
 */
public final class SealCheck {
  /** How a fill ended. */
  public enum Status {
    /** Every open cell reachable from the start is enclosed by airtight cells. */
    SEALED,
    /** The fill reached open sky, an unloaded chunk or the world edge. */
    OPEN_TO_OUTSIDE,
    /** More than the cap of open cells: too big to prove sealed. */
    TOO_BIG,
    /** The start cell itself is airtight: there is no room to check. */
    NO_SPACE
  }

  /**
   * @param status how it ended
   * @param volume open cells counted (at most {@code cap + 1}; exact when SEALED)
   * @param probed every position asked about, packed with {@link Pos}
   * @param leakAt the first OUTSIDE cell found, or {@link Long#MIN_VALUE} when none
   */
  public record Result(Status status, int volume, Set<Long> probed, long leakAt) {
    public boolean sealed() {
      return status == Status.SEALED;
    }
  }

  public static final long NO_LEAK = Long.MIN_VALUE;

  private static final int[][] STEPS = {
    {1, 0, 0}, {-1, 0, 0}, {0, 1, 0}, {0, -1, 0}, {0, 0, 1}, {0, 0, -1}
  };

  private SealCheck() {}

  /** Fill from (x, y, z). {@code cap} is the largest room the game will call a dome. */
  public static Result fill(Probe probe, int x, int y, int z, int cap) {
    return fill(probe, x, y, z, cap, new HashSet<>());
  }

  /**
   * As {@link #fill(Probe, int, int, int, int)}, sharing a probed set across several starts of one
   * dome. A start already in {@code probed} as an open cell of an earlier fill is the caller's
   * business (see {@link DomeSeals}).
   */
  static Result fill(Probe probe, int x, int y, int z, int cap, Set<Long> probed) {
    probed.add(Pos.pack(x, y, z));
    Probe.Cell first = probe.at(x, y, z);
    if (first == Probe.Cell.AIRTIGHT) {
      return new Result(Status.NO_SPACE, 0, probed, NO_LEAK);
    }
    if (first == Probe.Cell.OUTSIDE) {
      return new Result(Status.OPEN_TO_OUTSIDE, 0, probed, Pos.pack(x, y, z));
    }
    ArrayDeque<Long> queue = new ArrayDeque<>();
    queue.add(Pos.pack(x, y, z));
    int volume = 1;
    while (!queue.isEmpty()) {
      long cur = queue.poll();
      int cx = Pos.x(cur);
      int cy = Pos.y(cur);
      int cz = Pos.z(cur);
      for (int[] s : STEPS) {
        int nx = cx + s[0];
        int ny = cy + s[1];
        int nz = cz + s[2];
        long np = Pos.pack(nx, ny, nz);
        if (!probed.add(np)) {
          continue;
        }
        switch (probe.at(nx, ny, nz)) {
          case AIRTIGHT -> {}
          case OUTSIDE -> {
            return new Result(Status.OPEN_TO_OUTSIDE, volume, probed, np);
          }
          case OPEN -> {
            volume++;
            if (volume > cap) { // the guard: without it, open ground never ends
              return new Result(Status.TOO_BIG, volume, probed, NO_LEAK);
            }
            queue.add(np);
          }
        }
      }
    }
    return new Result(Status.SEALED, volume, probed, NO_LEAK);
  }
}
