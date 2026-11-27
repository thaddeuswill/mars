package mars.dome;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/** The starting dome (dome-layout.md): shape, counts, and the seal rule on the real layout. */
class DomeTemplateTest {
  private static final int OX = 10;
  private static final int OY = 10;
  private static final int OZ = 10;

  private static Probe probe(List<DomeTemplate.Cell> cells) {
    Map<Long, DomeTemplate.Kind> by = new HashMap<>();
    for (var c : cells) {
      by.put(Pos.pack(OX + c.x(), OY + c.y(), OZ + c.z()), c.kind());
    }
    return (x, y, z) -> {
      int rx = x - OX;
      int ry = y - OY;
      int rz = z - OZ;
      if (ry < 0) {
        return Probe.Cell.AIRTIGHT; // the ground
      }
      boolean in =
          rx >= 0
              && rx < DomeTemplate.SIZE_X
              && rz >= 0
              && rz < DomeTemplate.SIZE_Z
              && ry < DomeTemplate.SIZE_Y;
      if (!in) {
        return Probe.Cell.OUTSIDE; // open ground and sky around the dome
      }
      DomeTemplate.Kind k = by.getOrDefault(Pos.pack(x, y, z), DomeTemplate.Kind.AIR);
      return switch (k) {
        case AIR, CRACKED -> Probe.Cell.OPEN;
        default -> Probe.Cell.AIRTIGHT;
      };
    };
  }

  private static long seed(int x, int z) {
    return Pos.pack(OX + x, OY + 1, OZ + z);
  }

  @Test
  void everyPlanRowIs23ColumnsAnd16Rows() {
    for (int z = 0; z < DomeTemplate.SIZE_Z; z++) {
      for (int x = 0; x < DomeTemplate.SIZE_X; x++) {
        DomeTemplate.plan(x, z); // throws if a row is short
      }
    }
  }

  @Test
  void countsMatchTheLayoutSheet() {
    var cells = DomeTemplate.cells(3, true);
    Map<DomeTemplate.Kind, Integer> n = new EnumMap<>(DomeTemplate.Kind.class);
    cells.forEach(c -> n.merge(c.kind(), 1, Integer::sum));
    System.out.println("DomeTemplate counts (3 makers, breached): " + n);
    assertEquals(7, n.get(DomeTemplate.Kind.DOOR), "7 doors");
    assertEquals(1, n.get(DomeTemplate.Kind.CRACKED), "one cracked plate");
    assertEquals(5, n.get(DomeTemplate.Kind.TANK), "5 tanks");
    assertEquals(1, n.get(DomeTemplate.Kind.MONITOR));
    assertEquals(1, n.get(DomeTemplate.Kind.RECLAIMER));
    assertEquals(3, n.get(DomeTemplate.Kind.MAKER));
    assertEquals(1, n.get(DomeTemplate.Kind.BATTERY));
    assertEquals(5, n.get(DomeTemplate.Kind.BED_FOOT), "5 bunks");
    assertEquals(5, n.get(DomeTemplate.Kind.BED_HEAD));
    // Layout sheet section 8: glass is 10 window blocks plus 36 annex roof cells; 4 of those are
    // now ceiling lights (Hab B), so 32 stay ribbed glass.
    assertEquals(10, n.get(DomeTemplate.Kind.GLASS));
    assertEquals(32, n.get(DomeTemplate.Kind.GLASS_RIBBED));
    assertEquals(18, n.get(DomeTemplate.Kind.HULL_LIGHT), "18 ceiling lights");
    // The sheet says about 728 hull plates in walls, roof and lintels, plus about 235 floor cells.
    int walls =
        n.get(DomeTemplate.Kind.HULL)
            + n.get(DomeTemplate.Kind.HULL_BASE)
            + n.get(DomeTemplate.Kind.HULL_RIM);
    assertTrue(walls > 700 && walls < 1200, "hull plates incl. floor: " + walls);
  }

  @Test
  void trimCoursesAreOnThePerimeterOnly() {
    var cells = DomeTemplate.cells(3, true);
    int base = 0;
    int rim = 0;
    for (var c : cells) {
      boolean edge =
          c.x() == 0
              || c.x() == DomeTemplate.SIZE_X - 1
              || c.z() == 0
              || c.z() == DomeTemplate.SIZE_Z - 1;
      switch (c.kind()) {
        case HULL_BASE -> {
          base++;
          assertEquals(1, c.y(), "base course is y 1");
          assertTrue(edge, "base course on the perimeter: " + c);
          char ch = DomeTemplate.plan(c.x(), c.z());
          assertTrue("#*E".indexOf(ch) >= 0, "never under a door or glass cell: " + ch);
        }
        case HULL_RIM -> {
          rim++;
          assertEquals(DomeTemplate.SIZE_Y - 1, c.y(), "roof rim is y 4");
          assertTrue(edge, "rim on the perimeter: " + c);
        }
        case GLASS_RIBBED -> assertEquals(DomeTemplate.SIZE_Y - 1, c.y(), "ribbed glass is roof");
        default -> {}
      }
      if (edge && c.y() == 1 && DomeTemplate.plan(c.x(), c.z()) == '#') {
        assertEquals(DomeTemplate.Kind.HULL_BASE, c.kind(), "perimeter wall at y1 " + c);
      }
    }
    // Roof perimeter: 2 * (23 + 16) - 4 cells.
    assertEquals(2 * (DomeTemplate.SIZE_X + DomeTemplate.SIZE_Z) - 4, rim);
    assertTrue(base > 30, "base course count " + base);
    // The cracked plate keeps its own kind and the roof fixtures are outside the template.
    assertEquals(1, cells.stream().filter(c -> c.kind() == DomeTemplate.Kind.CRACKED).count());
    assertTrue(cells.stream().allMatch(c -> c.y() < DomeTemplate.SIZE_Y));
    assertEquals(5, DomeTemplate.ROOF_FIXTURE_Y);
    // Fixtures stand above the shelter's roof, not above Hab B or Ops.
    for (int[] f :
        new int[][] {
          {DomeTemplate.DISH_X, DomeTemplate.DISH_Z}, {DomeTemplate.VENT_X, DomeTemplate.VENT_Z}
        }) {
      assertTrue(f[0] >= 1 && f[0] <= 6 && f[1] >= 1 && f[1] <= 7, "above the shelter");
    }
  }

  @Test
  void roofFixturesAboveASealedRoofDoNotChangeTheSeal() {
    // A fixture at y 5 is outside the template box, so the probe calls it outside, as air is.
    var p = probe(DomeTemplate.cells(5, false));
    var r = fill(p, seed(DomeTemplate.SPAWN_X, DomeTemplate.SPAWN_Z));
    assertEquals(SealCheck.Status.SEALED, r.status());
    Probe withFixtures =
        (x, y, z) -> y == OY + DomeTemplate.ROOF_FIXTURE_Y ? Probe.Cell.OPEN : p.at(x, y, z);
    assertEquals(
        r.volume(), fill(withFixtures, seed(DomeTemplate.SPAWN_X, DomeTemplate.SPAWN_Z)).volume());
  }

  @Test
  void lampsAreOnlyInteriorRoofCellsOverFloorAir() {
    var cells = DomeTemplate.cells(5, false);
    Map<Long, DomeTemplate.Kind> at = new HashMap<>();
    cells.forEach(c -> at.put(Pos.pack(c.x(), c.y(), c.z()), c.kind()));
    int lamps = 0;
    for (var c : cells) {
      if (c.kind() != DomeTemplate.Kind.HULL_LIGHT) {
        continue;
      }
      lamps++;
      assertEquals(DomeTemplate.SIZE_Y - 1, c.y(), "lamps are roof cells");
      assertTrue(
          c.x() > 0
              && c.x() < DomeTemplate.SIZE_X - 1
              && c.z() > 0
              && c.z() < DomeTemplate.SIZE_Z - 1,
          "never on the rim ring: " + c);
      // The cell beneath (y 3) must be room air, and the floor cell y 1 not a wall.
      assertEquals(
          DomeTemplate.Kind.AIR,
          at.getOrDefault(Pos.pack(c.x(), 3, c.z()), DomeTemplate.Kind.AIR),
          "room air under the lamp " + c);
      char ch = DomeTemplate.plan(c.x(), c.z());
      assertTrue(".cgd".indexOf(ch) >= 0 || ch == 'F', "interior floor under lamp: " + ch);
    }
    assertEquals(18, lamps);
    assertEquals(DomeTemplate.LAMPS.length, lamps);
  }

  @Test
  void everyRoomStaysSealedWithLamps() {
    var p = probe(DomeTemplate.cells(5, false));
    int[][] starts = {{3, 4}, {11, 4}, {19, 4}, {3, 11}, {19, 11}, {11, 9}, {11, 13}};
    for (int[] s : starts) {
      assertEquals(
          SealCheck.Status.SEALED,
          fill(p, Pos.pack(OX + s[0], OY + 1, OZ + s[1])).status(),
          "room at " + s[0] + "," + s[1] + " with lamps");
    }
    // Each lamp is airtight: removing one makes its room leak (the probe treats lamps as AIRTIGHT).
    var without =
        DomeTemplate.cells(5, false).stream()
            .filter(c -> !(c.kind() == DomeTemplate.Kind.HULL_LIGHT && c.x() == 17 && c.z() == 2))
            .toList();
    assertEquals(
        SealCheck.Status.OPEN_TO_OUTSIDE,
        fill(probe(without), Pos.pack(OX + 18, OY + 1, OZ + 4)).status());
  }

  @Test
  void makersAreCappedAtFiveBays() {
    long m =
        DomeTemplate.cells(9, true).stream()
            .filter(c -> c.kind() == DomeTemplate.Kind.MAKER)
            .count();
    assertEquals(5, m);
    assertEquals(
        0,
        DomeTemplate.cells(0, true).stream()
            .filter(c -> c.kind() == DomeTemplate.Kind.MAKER)
            .count());
  }

  @Test
  void breachedDomeLeaksOnlyInHabBAndPatchedIsSealed() {
    var breached = probe(DomeTemplate.cells(3, true));
    var patched = probe(DomeTemplate.cells(3, false));
    // Required rooms: Ops (Monitor), shelter (tank), machine bay (maker), all closed by doors.
    long[] required = {seed(11, 4), seed(3, 4), seed(18, 4)};
    for (long s : required) {
      assertEquals(SealCheck.Status.SEALED, fill(breached, s).status(), "required room, breached");
    }
    // Hab B itself leaks, and nothing else does.
    var hab = fill(breached, Pos.pack(OX + 3, OY + 1, OZ + 11));
    assertEquals(SealCheck.Status.OPEN_TO_OUTSIDE, hab.status());
    assertEquals(
        Pos.pack(OX + DomeTemplate.BREACH_X - 1, OY + 2, OZ + DomeTemplate.BREACH_Z), hab.leakAt());
    var habPatched = fill(patched, Pos.pack(OX + 3, OY + 1, OZ + 11));
    assertEquals(SealCheck.Status.SEALED, habPatched.status());
    System.out.println(
        "Hab B volume "
            + habPatched.volume()
            + " blocks; Ops "
            + fill(patched, seed(11, 4)).volume()
            + "; shelter "
            + fill(patched, seed(3, 4)).volume());
  }

  @Test
  void everyRoomIsSealedWhenPatchedAndTheWholeDomeIsUnderTheCap() {
    var p = probe(DomeTemplate.cells(5, false));
    int[][] starts = {{3, 4}, {11, 4}, {19, 4}, {3, 11}, {19, 11}, {11, 9}, {11, 13}};
    int total = 0;
    for (int[] s : starts) {
      var r = fill(p, Pos.pack(OX + s[0], OY + 1, OZ + s[1]));
      assertEquals(SealCheck.Status.SEALED, r.status(), "room at " + s[0] + "," + s[1]);
      total += r.volume();
    }
    System.out.println("Starting dome air volume (rooms, doors closed): " + total + " blocks");
    assertTrue(total < DomeSeals.DEFAULT_CAP);
  }

  @Test
  void anOpenDoorJoinsTwoRooms() {
    // Replace the Ops/shelter door by air: shelter and Ops become one volume.
    var cells =
        DomeTemplate.cells(3, false).stream()
            .filter(c -> !(c.x() == 7 && c.z() == 4 && c.y() <= 2))
            .toList();
    var p = probe(cells);
    var ops = fill(p, seed(11, 4));
    var closed = fill(probe(DomeTemplate.cells(3, false)), seed(11, 4));
    assertTrue(ops.volume() > closed.volume(), "open door joins the shelter to Ops");
  }

  private static SealCheck.Result fill(Probe p, long start) {
    return SealCheck.fill(p, Pos.x(start), Pos.y(start), Pos.z(start), DomeSeals.DEFAULT_CAP);
  }
}
