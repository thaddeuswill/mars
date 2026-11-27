package mars.dome;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import mars.dome.DomeSeals.Status;
import org.junit.jupiter.api.Test;

/** The per-planet registry: registration, breach and repair, the cache, the safety net, saving. */
class DomeSealsTest {
  /** A 7 cube room with the Monitor standing inside against the south wall at (3,2,1). */
  private static Grid roomWithMonitor() {
    Grid g = Grid.room(7);
    g.set(3, 2, 1, 'M');
    return g;
  }

  private static final long SEED = Pos.pack(3, 2, 1);

  @Test
  void noDomeNoCostAndNoStatus() {
    DomeSeals d = new DomeSeals();
    Grid g = roomWithMonitor();
    assertNull(d.overall());
    assertTrue(d.tick(g).isEmpty());
    assertEquals(0, g.probes, "an empty registry must not touch the world");
  }

  @Test
  void aNewDomeIsCheckedOnTheNextTickAndReportsSealed() {
    Grid g = roomWithMonitor();
    DomeSeals d = new DomeSeals();
    d.register(SEED);
    assertEquals(Status.UNKNOWN, d.overall());
    var changes = d.tick(g);
    assertEquals(1, changes.size());
    assertTrue(changes.get(0).sealedNow());
    assertEquals(Status.SEALED, d.overall());
    assertEquals(5 * 5 * 5 - 1, d.volume(SEED)); // the Monitor takes one cell
  }

  @Test
  void breakingAWallFlipsToLeakingOnTheNextTickAndRepairFlipsBack() {
    Grid g = roomWithMonitor();
    DomeSeals d = new DomeSeals();
    d.register(SEED);
    d.tick(g);
    g.set(6, 3, 3, '.'); // punch a hole in the east wall (grid edge is open sky)
    d.blockChanged(Pos.pack(6, 3, 3));
    var changes = d.tick(g);
    assertEquals(1, changes.size());
    assertTrue(changes.get(0).breach());
    assertEquals(Status.LEAKING, d.overall());
    g.set(6, 3, 3, '#');
    d.blockChanged(Pos.pack(6, 3, 3));
    var fixed = d.tick(g);
    assertEquals(1, fixed.size());
    assertTrue(fixed.get(0).sealedNow());
    assertEquals(Status.SEALED, d.overall());
  }

  @Test
  void anUnchangedDomeCostsNoFillAndAnUnrelatedEditIsIgnored() {
    Grid g = roomWithMonitor();
    DomeSeals d = new DomeSeals();
    d.register(SEED);
    d.tick(g);
    int fills = d.fillsRun();
    for (int i = 0; i < DomeSeals.RECHECK_TICKS / 2; i++) {
      d.tick(g);
    }
    assertEquals(fills, d.fillsRun(), "no fill without a change");
    d.blockChanged(Pos.pack(500, 10, 500)); // far away: not in what the fill looked at
    d.tick(g);
    assertEquals(fills, d.fillsRun(), "an edit the fill never looked at cannot matter");
    d.blockChanged(Pos.pack(3, 3, 3)); // inside the room
    d.tick(g);
    assertTrue(d.fillsRun() > fills, "an edit inside the room rechecks it");
  }

  @Test
  void theSafetyNetCatchesAChangeNobodyReported() {
    Grid g = roomWithMonitor();
    DomeSeals d = new DomeSeals();
    d.register(SEED);
    d.tick(g);
    g.set(6, 3, 3, '.'); // no blockChanged call: an explosion, a piston
    var found = java.util.List.<DomeSeals.Change>of();
    int ticks = 0;
    while (found.isEmpty() && ticks <= DomeSeals.RECHECK_TICKS + 1) {
      found = d.tick(g);
      ticks++;
    }
    assertEquals(1, found.size());
    assertTrue(ticks <= DomeSeals.RECHECK_TICKS, "found within " + ticks + " ticks");
  }

  @Test
  void aMonitorMountedInTheWallIgnoresItsOutdoorFace() {
    Grid g = Grid.room(7);
    g.set(3, 2, 0, 'M'); // in the south wall: back face is the world edge (open sky)
    DomeSeals d = new DomeSeals();
    d.register(Pos.pack(3, 2, 0));
    d.tick(g);
    assertEquals(Status.SEALED, d.overall());
    g.set(6, 3, 3, '.');
    d.blockChanged(Pos.pack(6, 3, 3));
    d.tick(g);
    assertEquals(Status.LEAKING, d.overall());
  }

  @Test
  void aMonitorStandingInOpenAirIsLeaking() {
    Grid g = new Grid(new String[] {"...", "...", "..."}, new String[] {".M.", "...", "..."});
    DomeSeals d = new DomeSeals(50);
    d.register(Pos.pack(1, 1, 0));
    d.tick(g);
    assertEquals(Status.LEAKING, d.overall());
  }

  @Test
  void aRoomOverTheCapReadsTooBig() {
    Grid g = Grid.room(15);
    g.set(7, 1, 1, 'M');
    DomeSeals d = new DomeSeals(2000);
    d.register(Pos.pack(7, 1, 1));
    d.tick(g);
    assertEquals(Status.TOO_BIG, d.overall());
  }

  @Test
  void twoDomesLeakIfEitherLeaks() {
    Grid g = roomWithMonitor();
    g.set(3, 4, 1, 'M'); // a second Monitor, same room
    DomeSeals d = new DomeSeals();
    d.register(SEED);
    d.register(Pos.pack(3, 4, 1));
    d.tick(g);
    assertEquals(Status.SEALED, d.overall());
    g.set(0, 3, 3, '.');
    d.markAllDirty();
    d.tick(g);
    assertEquals(Status.LEAKING, d.overall());
    d.unregister(SEED);
    assertEquals(1, d.count());
  }

  @Test
  void savedStateRoundTripsAndIsRecheckedAfterLoading() {
    Grid g = roomWithMonitor();
    DomeSeals d = new DomeSeals();
    d.register(SEED);
    d.tick(g);
    DomeSeals back = new DomeSeals();
    back.restore(d.toState());
    assertEquals(d.toState(), back.toState());
    assertEquals(Status.SEALED, back.overall(), "the saved answer shows until the first tick");
    assertTrue(back.tick(g).isEmpty(), "a recheck that agrees says nothing");
    assertTrue(back.fillsRun() >= 1, "it did recheck");
    // A world edited while the server was off: the recheck reports the breach.
    g.set(6, 3, 3, '.');
    DomeSeals again = new DomeSeals();
    again.restore(d.toState());
    var changes = again.tick(g);
    assertEquals(1, changes.size());
    assertTrue(changes.get(0).breach());
  }

  @Test
  void aNullOrEmptySavedStateMeansNoDomes() {
    DomeSeals d = new DomeSeals();
    d.register(SEED);
    d.restore(null);
    assertEquals(0, d.count());
    d.restore(DomeSeals.State.EMPTY);
    assertEquals(0, d.count());
  }
}
