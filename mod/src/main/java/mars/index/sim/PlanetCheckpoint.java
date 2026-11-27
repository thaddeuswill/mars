package mars.index.sim;

/**
 * Rolling a planet back to a saved {@link PlanetState} (ARCHITECTURE.md 7.4a). A rollback restores
 * what the simulation owns: values, simulation ticks, battery charge, gate states, pressure
 * history, the frost reservoir and the storm schedule. It keeps the machine registry and the
 * per-machine output as they are now, because the blocks in the world are not rolled back: the
 * registry must keep matching the blocks that really stand there. Gates come back as they were at
 * the checkpoint (a rollback replaces the whole saved state; it is not the un-latching of one gate,
 * which no command does).
 */
public final class PlanetCheckpoint {
  private PlanetCheckpoint() {}

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
        snapshot.storm());
  }
}
