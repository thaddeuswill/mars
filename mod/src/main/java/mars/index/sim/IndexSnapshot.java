package mars.index.sim;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import mars.core.ParameterId;

/** Immutable copy of a planet's values (stored and derived) for readers. */
public final class IndexSnapshot {
  private final Map<ParameterId, Double> values;
  private final long simTicks;

  IndexSnapshot(Map<ParameterId, Double> values, long simTicks) {
    this.values = Collections.unmodifiableMap(new LinkedHashMap<>(values));
    this.simTicks = simTicks;
  }

  public double get(ParameterId id) {
    Double v = values.get(id);
    if (v == null) {
      throw new IllegalArgumentException("Unknown parameter " + id + "; known: " + values.keySet());
    }
    return v;
  }

  public long simTicks() {
    return simTicks;
  }

  public Map<ParameterId, Double> asMap() {
    return values;
  }

  @Override
  public String toString() {
    return "IndexSnapshot{tick=" + simTicks + ", " + values + "}";
  }
}
