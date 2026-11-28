package mars.index.sim;

/**
 * A coupling, sink, or decay rule applied each step after contributions (ARCHITECTURE.md 2.1 step
 * 2-3). {@code dtSols} already includes {@code pace.rateMultiplier}: natural rates are compressed
 * in classroom mode exactly like machine rates.
 */
@FunctionalInterface
public interface Coupling {
  void apply(Values values, double dtSols);

  /** Mutable view of the stored values during one step. */
  interface Values {
    double get(mars.core.ParameterId id);

    void add(mars.core.ParameterId id, double delta);

    /** True when this planet has the parameter. */
    boolean has(mars.core.ParameterId id);
  }
}
