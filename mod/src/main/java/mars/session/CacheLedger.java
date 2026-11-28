package mars.session;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Who has already received which gate's supply cache on one planet (docs/design/gate-input-items.md
 * section 1). A claim is per player and per gate: dying, relogging or a second arrival never hands
 * the same cache out twice, and a player who was offline when the gate latched claims it on the
 * next arrival. Pure Java. Cost: one set lookup per claim.
 */
public final class CacheLedger {
  private final Map<String, Set<UUID>> claimed = new LinkedHashMap<>();
  private final Map<UUID, Double> lastRequest = new LinkedHashMap<>();

  public CacheLedger() {}

  public CacheLedger(Map<String, Set<UUID>> saved) {
    saved.forEach((gate, players) -> claimed.put(gate, new HashSet<>(players)));
  }

  public CacheLedger(Map<String, Set<UUID>> saved, Map<UUID, Double> requests) {
    this(saved);
    lastRequest.putAll(requests);
  }

  /**
   * A resupply request (docs/design/recipe-inputs-ruling.md section 6): 0 and the time is recorded
   * when this player may call the orbiter now, otherwise the sols still to wait (nothing recorded,
   * so a request made in the cool-down costs nothing). {@code nowSols} is the planet's simulation
   * clock, which a pause freezes. A recorded time in the future (a clock that went back) never
   * blocks.
   */
  public double tryRequest(UUID player, double nowSols, double passSols) {
    Double last = lastRequest.get(player);
    if (last != null && last <= nowSols && nowSols - last < passSols) {
      return last + passSols - nowSols;
    }
    lastRequest.put(player, nowSols);
    return 0.0;
  }

  /** A copy of the last request times for saving. */
  public Map<UUID, Double> requestsToMap() {
    return new LinkedHashMap<>(lastRequest);
  }

  /**
   * True exactly once for each player and gate: the first call. The caller then gives the items.
   */
  public boolean claim(String gateId, UUID player) {
    return claimed.computeIfAbsent(gateId, k -> new HashSet<>()).add(player);
  }

  public boolean hasClaimed(String gateId, UUID player) {
    return claimed.getOrDefault(gateId, Set.of()).contains(player);
  }

  /** A copy for saving. */
  public Map<String, Set<UUID>> toMap() {
    Map<String, Set<UUID>> out = new LinkedHashMap<>();
    claimed.forEach((g, p) -> out.put(g, Collections.unmodifiableSet(new HashSet<>(p))));
    return out;
  }
}
