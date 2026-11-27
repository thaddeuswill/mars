package mars.index.sim;

import java.util.List;
import java.util.Map;

/**
 * The saved form of one planet (ARCHITECTURE.md 7, saves): a schema version, the stored values by
 * ID, simulation ticks, ticks since the last step, and machine positions by machine type, the
 * battery charge, and output fractions by position. Pure data; the Minecraft layer turns it into
 * NBT. A state with a newer schema than this build knows is refused rather than read and later
 * overwritten.
 */
public record PlanetState(
    int schema,
    Map<String, Double> values,
    long simTicks,
    int pendingTicks,
    Map<String, List<Long>> machines,
    double batteryCharge,
    Map<Long, Double> efficiency,
    Map<String, mars.gates.GateTracker.State> gates,
    PressureHistory.State history) {

  /**
   * 2 adds the battery charge and per-machine output (solar dust); 3 adds gate states (latched,
   * hold counters); 4 adds the pressure history for the Monitor plot. Older saves still load; their
   * gates start unlatched and their history starts empty.
   */
  public static final int CURRENT_SCHEMA = 4;

  public PlanetState(
      int schema,
      Map<String, Double> values,
      long simTicks,
      int pendingTicks,
      Map<String, List<Long>> machines,
      double batteryCharge,
      Map<Long, Double> efficiency,
      Map<String, mars.gates.GateTracker.State> gates) {
    this(
        schema,
        values,
        simTicks,
        pendingTicks,
        machines,
        batteryCharge,
        efficiency,
        gates,
        PressureHistory.State.EMPTY);
  }

  public PlanetState(
      int schema,
      Map<String, Double> values,
      long simTicks,
      int pendingTicks,
      Map<String, List<Long>> machines,
      double batteryCharge,
      Map<Long, Double> efficiency) {
    this(schema, values, simTicks, pendingTicks, machines, batteryCharge, efficiency, Map.of());
  }

  public PlanetState(
      int schema,
      Map<String, Double> values,
      long simTicks,
      int pendingTicks,
      Map<String, List<Long>> machines) {
    this(schema, values, simTicks, pendingTicks, machines, 0, Map.of());
  }
}
