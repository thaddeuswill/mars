package mars.index.sim;

import java.util.List;
import java.util.Map;

/**
 * The saved form of one planet (ARCHITECTURE.md 7, saves): a schema version, the stored values by
 * ID, simulation ticks, ticks since the last step, and machine positions by machine type. Pure
 * data; the Minecraft layer turns it into NBT. A state with a newer schema than this build knows is
 * refused rather than read and later overwritten.
 */
public record PlanetState(
    int schema,
    Map<String, Double> values,
    long simTicks,
    int pendingTicks,
    Map<String, List<Long>> machines) {

  public static final int CURRENT_SCHEMA = 1;
}
