package mars.index.sim;

/**
 * Rolling a planet back to a saved {@link PlanetState} (ARCHITECTURE.md 7.4a). A rollback restores
 * what the simulation owns: values, simulation ticks, battery charge, gate states, pressure history
 * and the frost reservoir. It keeps the machine registry and the per-machine output as they are
 * now, because the blocks in the world are not rolled back: the registry must keep matching the
 * blocks that really stand there. Gates come back as they were at the checkpoint (a rollback
 * replaces the whole saved state; it is not the un-latching of one gate, which no command does).
 */
public final class PlanetCheckpoint {
  private PlanetCheckpoint() {}

  /**
   * Panel dust stays as the blocks show it now (like the output), the storm schedule rolls back
   * with the clock, and the planet seed never changes once set.
   */
  private static mars.hazards.HazardState rolledBackHazards(
      PlanetState snapshot, PlanetState current) {
    var cur = current.hazards();
    var snap = snapshot.hazards();
    if (cur == null) {
      return snap;
    }
    return new mars.hazards.HazardState(
        cur.seed() != 0 ? cur.seed() : snap == null ? 0 : snap.seed(),
        cur.dust(),
        snap == null ? null : snap.storm());
  }

  /** The snapshot's simulation state with the machines and outputs of {@code current}. */
  public static PlanetState restoreOver(PlanetState snapshot, PlanetState current) {
    return new PlanetState(
        PlanetState.CURRENT_SCHEMA,
        snapshot.values(),
        snapshot.simTicks(),
        snapshot.pendingTicks(),
        current.machines(),
        snapshot.batteryCharge(),
        current.efficiency(),
        snapshot.gates(),
        snapshot.history(),
        snapshot.frost() == null ? FrostSink.State.EMPTY : snapshot.frost(),
        // Domes are blocks in the world, which a rollback does not roll back: keep the current
        // ones.
        current.domes() == null ? mars.dome.DomeSeals.State.EMPTY : current.domes(),
        // Life-support stores are simulation state: they roll back (a snapshot without them, from
        // before schema 7, keeps what the planet has now).
        snapshot.life() != null ? snapshot.life() : current.life(),
        rolledBackHazards(snapshot, current));
  }
}
