package mars.session;

import java.util.LinkedHashMap;
import java.util.Map;
import mars.index.sim.MachineDef;

/**
 * The once-per-world starting machines (balance sheet 2c decision 2): the first player to arrive on
 * a planet gets what the machine files list under {@code startingGrant}. The claim is a single flag
 * in world save data, so dying, logging out and in, or a second player arriving never hands it out
 * again. Pure Java.
 */
public final class StartingGrant {
  private boolean granted;

  public StartingGrant(boolean granted) {
    this.granted = granted;
  }

  public boolean granted() {
    return granted;
  }

  /** True exactly once: the first call. The caller then gives the items. */
  public boolean claim() {
    if (granted) {
      return false;
    }
    granted = true;
    return true;
  }

  /** Machine id to count, for every machine file with a starting grant, in id order. */
  public static Map<String, Integer> plan(Map<String, MachineDef> defs) {
    Map<String, Integer> out = new LinkedHashMap<>();
    defs.keySet().stream()
        .sorted()
        .forEach(
            id -> {
              int n = defs.get(id).startingGrant();
              if (n > 0) {
                out.put(id, n);
              }
            });
    return out;
  }
}
