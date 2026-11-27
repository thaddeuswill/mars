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
    PressureHistory.State history,
    FrostSink.State frost,
    mars.dome.DomeSeals.State domes,
    mars.life.LifeSupport.State life) {

  /**
   * 2 adds the battery charge and per-machine output (solar dust); 3 adds gate states (latched,
   * hold counters); 4 adds the pressure history for the Monitor plot; 5 adds the polar-frost
   * reservoir (kPa stored, buried kPa released); 6 adds the registered domes and their sealed
   * state; 7 adds the life-support stores (oxygen and water). Older saves still load; their gates
   * start unlatched, their history starts empty, their reservoir starts empty, they have no domes,
   * and a save without life support (schema 6 and older) keeps running on the legacy stand-in
   * ({@code life == null}; see PlanetSim.lifeSupportOk).
   */
  public static final int CURRENT_SCHEMA = 7;

  /** Without life support (schema 6 and older). */
  public PlanetState(
      int schema,
      Map<String, Double> values,
      long simTicks,
      int pendingTicks,
      Map<String, List<Long>> machines,
      double batteryCharge,
      Map<Long, Double> efficiency,
      Map<String, mars.gates.GateTracker.State> gates,
      PressureHistory.State history,
      FrostSink.State frost,
      mars.dome.DomeSeals.State domes) {
    this(
        schema,
        values,
        simTicks,
        pendingTicks,
        machines,
        batteryCharge,
        efficiency,
        gates,
        history,
        frost,
        domes,
        null);
  }

  /** Without domes (schema 5 and older). */
  public PlanetState(
      int schema,
      Map<String, Double> values,
      long simTicks,
      int pendingTicks,
      Map<String, List<Long>> machines,
      double batteryCharge,
      Map<Long, Double> efficiency,
      Map<String, mars.gates.GateTracker.State> gates,
      PressureHistory.State history,
      FrostSink.State frost) {
    this(
        schema,
        values,
        simTicks,
        pendingTicks,
        machines,
        batteryCharge,
        efficiency,
        gates,
        history,
        frost,
        mars.dome.DomeSeals.State.EMPTY);
  }

  public PlanetState(
      int schema,
      Map<String, Double> values,
      long simTicks,
      int pendingTicks,
      Map<String, List<Long>> machines,
      double batteryCharge,
      Map<Long, Double> efficiency,
      Map<String, mars.gates.GateTracker.State> gates,
      PressureHistory.State history) {
    this(
        schema,
        values,
        simTicks,
        pendingTicks,
        machines,
        batteryCharge,
        efficiency,
        gates,
        history,
        FrostSink.State.EMPTY);
  }

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
