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
    double machineScale = pace.rateMultiplier() * pace.groupRateScale() * dtSols;
    for (Handle h : contributions) {
      if (!h.removed) {
        values.merge(h.parameter, h.rate * machineScale, Double::sum);
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
