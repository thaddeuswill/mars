package mars.index.sim;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import mars.core.Ids;
import mars.core.ParameterId;

/**
 * One planet's Planetary Index as pure Java (ARCHITECTURE.md 2.1, 4.4). Fixed-step update: each
 * step sums the cached contribution rates, applies couplings, clamps, then recomputes derived
 * values. Cost per step: one add per contribution plus the couplings.
 */
public final class PlanetaryIndexModel {
  private final Map<ParameterId, ParameterDef> defs = new LinkedHashMap<>();
  private final Map<ParameterId, Double> values = new LinkedHashMap<>();
  private final Map<ParameterId, Double> derived = new LinkedHashMap<>();
  private final List<Handle> contributions = new ArrayList<>();
  private final List<Coupling> couplings = new ArrayList<>();
  private final BoilingPoint boilingPoint;
  private final Pace pace;
  private long simTicks;
  private java.util.function.DoubleConsumer beforeStep = dt -> {};
  private java.util.function.DoubleConsumer afterStep = dt -> {};

  public PlanetaryIndexModel(List<ParameterDef> parameters, BoilingPoint boilingPoint, Pace pace) {
    for (ParameterDef d : parameters) {
      defs.put(d.id(), d);
      values.put(d.id(), d.start());
    }
    this.boilingPoint = boilingPoint;
    this.pace = pace;
    recomputeDerived();
  }

  /** Register a machine's rate. The machine keeps the handle and updates or removes it. */
  public ContributionHandle contribute(ParameterId parameter, double ratePerSol, String source) {
    if (!defs.containsKey(parameter)) {
      throw new IllegalArgumentException("Can't contribute to unknown parameter " + parameter);
    }
    Handle h = new Handle(parameter, ratePerSol, source);
    contributions.add(h);
    return h;
  }

  /** Called at the start of every step with the step length in sols (the power grid hooks in). */
  public void setBeforeStep(java.util.function.DoubleConsumer hook) {
    this.beforeStep = hook;
  }

  /** Called at the end of every step, after values and derived values are final (gates hook in). */
  public void setAfterStep(java.util.function.DoubleConsumer hook) {
    this.afterStep = hook;
  }

  /** Current value of a stored or derived parameter; NaN where a derived value has no meaning. */
  public double current(ParameterId id) {
    Double d = derived.get(id);
    if (d != null) {
      return d;
    }
    return value(id);
  }

  /** Start value of a stored parameter (the left end of a goal bar), or 0 if it has none. */
  public double start(ParameterId id) {
    ParameterDef d = defs.get(id);
    return d == null ? 0 : d.start();
  }

  /** Unit text of a stored parameter ("kPa"), or "" if it has none or is derived. */
  public String unit(ParameterId id) {
    ParameterDef d = defs.get(id);
    return d == null ? "" : d.unit();
  }

  public void addCoupling(Coupling coupling) {
    couplings.add(coupling);
  }

  /** Advance by whole steps covering {@code ticks} simulation ticks. */
  public void advanceTicks(long ticks) {
    long steps = ticks / pace.stepTicks();
    for (long i = 0; i < steps; i++) {
      step();
    }
  }

  public void advanceSols(double sols) {
    advanceTicks(Math.round(sols * pace.solTicks()));
  }

  /** One fixed step of {@code pace.stepTicks} simulation ticks. */
  public void step() {
    double dtSols = (double) pace.stepTicks() / pace.solTicks();
    beforeStep.accept(dtSols);
    double machineScale = pace.rateMultiplier() * pace.groupRateScale() * dtSols;
    for (Handle h : contributions) {
      if (!h.removed) {
        values.merge(h.parameter, h.rate * machineScale, Double::sum);
      }
    }
    double naturalLoss = pace.rateMultiplier() * dtSols;
    for (ParameterDef d : defs.values()) {
      if (d.loss() != null) {
        double v = values.get(d.id());
        double above = v - d.loss().baseline();
        if (above > 0) {
          values.put(d.id(), v - Math.min(above, d.loss().perSol() * naturalLoss * above));
        }
      }
    }
    Coupling.Values view =
        new Coupling.Values() {
          @Override
          public double get(ParameterId id) {
            return value(id);
          }

          @Override
          public void add(ParameterId id, double delta) {
            values.merge(id, delta, Double::sum);
          }
        };
    double naturalDt = pace.rateMultiplier() * dtSols;
    for (Coupling c : couplings) {
      c.apply(view, naturalDt);
    }
    for (ParameterDef d : defs.values()) {
      values.put(d.id(), d.clamp(values.get(d.id())));
    }
    recomputeDerived();
    simTicks += pace.stepTicks();
    afterStep.accept(dtSols);
  }

  private double value(ParameterId id) {
    Double v = values.get(id);
    if (v == null) {
      throw new IllegalArgumentException("Unknown parameter " + id);
    }
    return v;
  }

  private void recomputeDerived() {
    double p = values.getOrDefault(Ids.PRESSURE, Double.NaN);
    derived.put(Ids.BOILING_POINT, boilingPoint.atKpa(p));
    if (values.containsKey(Ids.O2_FRACTION)) {
      derived.put(Ids.O2_PARTIAL, p * values.get(Ids.O2_FRACTION) / 100.0);
    }
  }

  /** Total simulation ticks this model has been stepped through. */
  public long simTicks() {
    return simTicks;
  }

  /**
   * Effective rate per sol of every live contribution to {@code parameter}, grouped by source
   * (after {@code pace.rateMultiplier} and the group scale), for the Monitor's cause lines.
   */
  public List<Cause> causes(ParameterId parameter) {
    double scale = pace.rateMultiplier() * pace.groupRateScale();
    Map<String, double[]> bySource = new LinkedHashMap<>();
    for (Handle h : contributions) {
      if (!h.removed && h.parameter.equals(parameter)) {
        double[] acc = bySource.computeIfAbsent(h.source, k -> new double[2]);
        acc[0] += 1;
        acc[1] += h.rate * scale;
      }
    }
    List<Cause> out = new ArrayList<>();
    bySource.forEach((src, acc) -> out.add(new Cause(src, (int) acc[0], acc[1])));
    return out;
  }

  /**
   * The restoring loss of {@code parameter} right now, per sol, signed (0 or below), or 0 when it
   * has none or sits at its baseline. Natural rate: scaled by {@code pace.rateMultiplier} only.
   */
  public double lossRatePerSol(ParameterId parameter) {
    ParameterDef d = defs.get(parameter);
    if (d == null || d.loss() == null) {
      return 0;
    }
    double above = Math.max(0, values.get(parameter) - d.loss().baseline());
    return -d.loss().perSol() * above * pace.rateMultiplier();
  }

  /** Sum of the cause rates for {@code parameter}, per sol. */
  public double netRatePerSol(ParameterId parameter) {
    double sum = 0;
    for (Cause c : causes(parameter)) {
      sum += c.ratePerSol();
    }
    return sum;
  }

  /** Debug and admin: set a stored value (clamped to its limits). */
  public void set(ParameterId parameter, double value) {
    ParameterDef d = defs.get(parameter);
    if (d == null) {
      throw new IllegalArgumentException("Unknown or derived parameter " + parameter);
    }
    values.put(parameter, d.clamp(value));
    recomputeDerived();
  }

  /** Copy of the stored (not derived) values, for saving. */
  public Map<ParameterId, Double> storedValues() {
    return new LinkedHashMap<>(values);
  }

  /**
   * Put saved values back. Stored ids this build doesn't know are ignored and listed in the report
   * (never silently dropped from the log); parameters missing from the save take their start value.
   */
  public RestoreReport restore(Map<ParameterId, Double> saved, long savedSimTicks) {
    List<ParameterId> ignored = new ArrayList<>();
    List<ParameterId> defaulted = new ArrayList<>();
    for (ParameterId id : saved.keySet()) {
      if (!defs.containsKey(id)) {
        ignored.add(id);
      }
    }
    for (ParameterDef d : defs.values()) {
      Double v = saved.get(d.id());
      if (v == null || v.isNaN()) {
        values.put(d.id(), d.start());
        defaulted.add(d.id());
      } else {
        values.put(d.id(), d.clamp(v));
      }
    }
    this.simTicks = savedSimTicks;
    recomputeDerived();
    return new RestoreReport(ignored, defaulted);
  }

  /** What {@link #restore} did with ids that didn't match this build's parameters. */
  public record RestoreReport(List<ParameterId> ignored, List<ParameterId> defaulted) {}

  /**
   * One line of a cause breakdown: {@code count} machines of one source, {@code ratePerSol} in
   * total.
   */
  public record Cause(String source, int count, double ratePerSol) {}

  public IndexSnapshot snapshot() {
    Map<ParameterId, Double> all = new LinkedHashMap<>(values);
    all.putAll(derived);
    return new IndexSnapshot(all, simTicks);
  }

  public double sols() {
    return (double) simTicks / pace.solTicks();
  }

  /** Handle a machine owns for its contribution. */
  public interface ContributionHandle {
    void setRate(double ratePerSol);

    void remove();
  }

  private static final class Handle implements ContributionHandle {
    private final ParameterId parameter;
    private final String source;
    private double rate;
    private boolean removed;

    private Handle(ParameterId parameter, double rate, String source) {
      this.parameter = parameter;
      this.rate = rate;
      this.source = source;
    }

    @Override
    public void setRate(double ratePerSol) {
      this.rate = ratePerSol;
    }

    @Override
    public void remove() {
      this.removed = true;
    }

    @Override
    public String toString() {
      return source + "→" + parameter + "@" + rate + "/sol";
    }
  }
}
