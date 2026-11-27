package mars.learning;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

/**
 * The value ids each player has an open forecast about, for CR-T8 line hiding. Session only, not
 * saved: until real forecasts exist only the debug command sets it (ARCHITECTURE.md 2.6.1 will
 * rebuild it from forecast state). Pure Java.
 */
public final class OpenForecasts {
  private final Map<UUID, Set<String>> open = new HashMap<>();

  public void open(UUID player, String valueId) {
    open.computeIfAbsent(player, k -> new TreeSet<>()).add(valueId);
  }

  public void close(UUID player, String valueId) {
    Set<String> s = open.get(player);
    if (s != null) {
      s.remove(valueId);
      if (s.isEmpty()) {
        open.remove(player);
      }
    }
  }

  /** A copy of what this player has open (empty when nothing). */
  public Set<String> askedBy(UUID player) {
    Set<String> s = open.get(player);
    return s == null ? Set.of() : Set.copyOf(s);
  }

  public void clear() {
    open.clear();
  }
}
