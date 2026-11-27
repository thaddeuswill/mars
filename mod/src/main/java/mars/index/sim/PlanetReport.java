package mars.index.sim;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * A readable plain-text summary of one planet for a teacher (BACKLOG owner request, slice 8). It
 * holds the planet's numbers only: no player names, no positions, nothing personal, so it is safe
 * to hand around (ARCHITECTURE.md 8.1). Pure: built from a {@link PlanetState}.
 */
public final class PlanetReport {
  private PlanetReport() {}

  public static String text(
      String planetId, PlanetState s, Pace pace, int sessionNumber, boolean paused) {
    StringBuilder sb = new StringBuilder();
    sb.append("Mars Terraforming planet report\n");
    sb.append("Planet: ").append(planetId).append('\n');
    sb.append("Simulation time: sol ")
        .append(f(s.simTicks() / (double) pace.solTicks(), 2))
        .append(" (")
        .append(s.simTicks())
        .append(" ticks)\n");
    sb.append("Session: ")
        .append(sessionNumber)
        .append(paused ? " (paused)" : " (running)")
        .append('\n');
    sb.append("\nIndex values:\n");
    new TreeMap<>(s.values())
        .forEach(
            (id, v) ->
                sb.append("  ")
                    .append(id)
                    .append(" = ")
                    .append(Double.isNaN(v) ? "n/a" : f(v, 4))
                    .append('\n'));
    sb.append("\nGates:\n");
    new TreeMap<>(s.gates())
        .forEach(
            (id, g) -> {
              sb.append("  ").append(id).append(": ");
              if (g.latched()) {
                sb.append("reached at sol ")
                    .append(f(g.latchedSimTicks() / (double) pace.solTicks(), 2))
                    .append(" (")
                    .append(g.cause().name().toLowerCase(Locale.ROOT))
                    .append(")\n");
              } else {
                sb.append("not reached (held ").append(f(g.heldSols(), 2)).append(" sols)\n");
              }
            });
    sb.append("\nMachines:\n");
    Map<String, Integer> counts = new TreeMap<>();
    s.machines().forEach((type, pos) -> counts.put(type, pos.size()));
    if (counts.isEmpty()) {
      sb.append("  none\n");
    }
    counts.forEach((type, n) -> sb.append("  ").append(type).append(": ").append(n).append('\n'));
    sb.append("\nBattery charge: ").append(f(s.batteryCharge(), 3)).append(" units\n");
    sb.append("Polar frost stored: ").append(f(s.frost().storedKpa(), 4)).append(" kPa\n");
    sb.append("\nValues are the planet's own numbers. No player data is in this file.\n");
    return sb.toString();
  }

  private static String f(double v, int decimals) {
    return String.format(Locale.ROOT, "%." + decimals + "f", v);
  }
}
