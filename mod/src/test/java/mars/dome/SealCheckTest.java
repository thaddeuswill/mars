package mars.dome;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import org.junit.jupiter.api.Test;

/** The flood fill: sealed room, one hole, cap, open sky, doors, partial results. */
class SealCheckTest {
  @Test
  void aClosedRoomIsSealedAndCountsItsAir() {
    Grid g = Grid.room(5); // 3 x 3 x 3 inside
    var r = SealCheck.fill(g, 2, 2, 2, 2000);
    assertEquals(SealCheck.Status.SEALED, r.status());
    assertEquals(27, r.volume());
    assertTrue(r.sealed());
  }

  @Test
  void oneHoleToTheSkyLeaks() {
    Grid g = Grid.room(5);
    g.set(2, 4, 2, '^'); // roof cell becomes open sky
    var r = SealCheck.fill(g, 2, 2, 2, 2000);
    assertEquals(SealCheck.Status.OPEN_TO_OUTSIDE, r.status());
    assertEquals(Pos.pack(2, 4, 2), r.leakAt());
    assertFalse(r.sealed());
  }

  @Test
  void aHoleInTheWallLeaksToTheWorldEdge() {
    Grid g = Grid.room(5);
    g.set(0, 2, 2, '.'); // the grid edge is open sky in this world
    assertEquals(SealCheck.Status.OPEN_TO_OUTSIDE, SealCheck.fill(g, 2, 2, 2, 2000).status());
  }

  @Test
  void aDiagonalGapIsNotAHole() {
    // Air moves through faces only: a gap that touches the room at an edge leaves it sealed.
    Grid g = Grid.room(5);
    g.set(0, 0, 0, '.'); // a corner of the hull, touching the inside only diagonally
    assertEquals(SealCheck.Status.SEALED, SealCheck.fill(g, 2, 2, 2, 2000).status());
  }

  @Test
  void aRoomBiggerThanTheCapIsTooBigNotSealed() {
    Grid g = Grid.room(15); // 13^3 = 2197 inside
    var r = SealCheck.fill(g, 7, 7, 7, 2000);
    assertEquals(SealCheck.Status.TOO_BIG, r.status());
    assertEquals(2001, r.volume());
    assertEquals(SealCheck.Status.SEALED, SealCheck.fill(Grid.room(15), 7, 7, 7, 2197).status());
  }

  @Test
  void theCapGuardEndsAFillInEndlessOpenGround() {
    // No walls anywhere and no OUTSIDE: only the cap can stop this. If the guard is removed this
    // test fails (it times out instead of returning).
    int[] probes = {0};
    Probe endless =
        (x, y, z) -> {
          probes[0]++;
          return Probe.Cell.OPEN;
        };
    var r =
        assertTimeoutPreemptively(
            Duration.ofSeconds(10), () -> SealCheck.fill(endless, 0, 0, 0, 2000));
    assertEquals(SealCheck.Status.TOO_BIG, r.status());
    assertTrue(probes[0] <= 6 * 2001 + 1, "probe calls " + probes[0]);
    assertTrue(r.probed().size() <= 6 * 2001 + 1);
  }

  @Test
  void aStartInsideAWallHasNoRoom() {
    Grid g = Grid.room(5);
    var r = SealCheck.fill(g, 0, 2, 2, 2000);
    assertEquals(SealCheck.Status.NO_SPACE, r.status());
    assertEquals(0, r.volume());
  }

  @Test
  void aDoorJoinsTwoRoomsOnlyWhileOpen() {
    // Two 3-cell rooms side by side with a door cell D between them, sealed outside.
    Grid g =
        new Grid(
            new String[] {"#######", "#######", "#######"},
            new String[] {"#######", "#..D..#", "#######"},
            new String[] {"#######", "#######", "#######"});
    assertEquals(2, SealCheck.fill(g, 1, 1, 1, 2000).volume());
    assertTrue(SealCheck.fill(g, 1, 1, 1, 2000).sealed());
    g.doorClosed = false;
    var open = SealCheck.fill(g, 1, 1, 1, 2000);
    assertTrue(open.sealed());
    assertEquals(5, open.volume()); // 2 + the door cell + 2
  }

  @Test
  void anOpenDoorLeaksWhenTheOtherSideIsOpen() {
    Grid g =
        new Grid(
            new String[] {"#######", "#######", "#######"},
            new String[] {"#######", "#..D.^#", "#######"},
            new String[] {"#######", "#######", "#######"});
    assertTrue(SealCheck.fill(g, 1, 1, 1, 2000).sealed(), "closed door");
    g.doorClosed = false;
    assertEquals(SealCheck.Status.OPEN_TO_OUTSIDE, SealCheck.fill(g, 1, 1, 1, 2000).status());
  }

  @Test
  void positionsPackLikeBlockPos() {
    for (int[] p :
        new int[][] {{0, 0, 0}, {5, 64, -3}, {-30_000_000, 255, 29_999_999}, {-1, 0, -1}}) {
      long l = Pos.pack(p[0], p[1], p[2]);
      assertEquals(p[0], Pos.x(l));
      assertEquals(p[1], Pos.y(l));
      assertEquals(p[2], Pos.z(l));
    }
  }
}
