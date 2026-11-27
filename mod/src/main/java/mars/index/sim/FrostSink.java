package mars.index.sim;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The polar-frost sink (balance sheet 2c, Stage 2 slice 5): CO2 that freezes onto the poles when
 * the planet is cold, and comes back when it is warm. Pure Java, all numbers from data. Cost per
 * step: a few comparisons and multiplications.
 *
 * <p>Three branches by global mean temperature T:
 *
 * <ul>
 *   <li><b>cold</b> (T below {@code coldBelowC}): takes {@code coldFraction} of (P minus baseline)
 *       per sol into the reservoir;
 *   <li><b>mild</b> ({@code coldBelowC} to {@code releaseAboveC}, both ends included): takes {@code
 *       mildFraction} per sol;
 *   <li><b>release</b> (T above {@code releaseAboveC}): returns the reservoir at {@code
 *       releaseRateKpaPerSol}, then up to {@code buriedKpa} more from buried polar CO2, once.
 * </ul>
 *
 * The sink never adds air while taking (zero at or below the baseline). The fractions and the
 * release rate are <i>rates</i>: the caller scales them with pace and group exactly like machine
 * rates, so the equilibrium pressure is the same in every mode. The buried amount is a quantity,
 * not a rate, and is never scaled. All numbers are invented game balance; the direction is sourced
 * (docs/science/frost-sink-review.md).
 */
public final class FrostSink {

  /** Data-driven numbers for one planet. {@code baselineKpa} is the planet's start pressure. */
  public record Params(
      double baselineKpa,
      double coldBelowC,
      double releaseAboveC,
      double coldFractionPerSol,
      double mildFractionPerSol,
      double releaseRateKpaPerSol,
      double buriedKpa) {
    public Params {
      if (baselineKpa < 0
          || coldBelowC > releaseAboveC
          || coldFractionPerSol < 0
          || coldFractionPerSol > 1
          || mildFractionPerSol < 0
          || mildFractionPerSol > 1
          || releaseRateKpaPerSol < 0
          || buriedKpa < 0) {
        throw new IllegalArgumentException("Invalid frost sink numbers: " + this);
      }
    }
  }

  /** Which branch applies at a temperature. */
  public enum Branch {
    COLD,
    MILD,
    RELEASE
  }

  /** Saved form: kPa in the reservoir and kPa of buried CO2 already released. */
  public record State(double storedKpa, double buriedReleasedKpa) {
    public static final State EMPTY = new State(0, 0);
  }

  private final Params params;
  private double stored;
  private double buriedReleased;

  public FrostSink(Params params) {
    this.params = params;
  }

  public Params params() {
    return params;
  }

  public Branch branch(double temperatureC) {
    if (temperatureC < params.coldBelowC()) {
      return Branch.COLD;
    }
    if (temperatureC > params.releaseAboveC()) {
      return Branch.RELEASE;
    }
    return Branch.MILD;
  }

  public double storedKpa() {
    return stored;
  }

  public double buriedRemainingKpa() {
    return Math.max(0, params.buriedKpa() - buriedReleased);
  }

  /**
   * One step. {@code scaledDtSols} is the step length in sols times the pace multiplier and the
   * group scale (the same scale the machines get). Returns the change to pressure in kPa: negative
   * when the frost takes air, positive when it gives back, and updates the reservoir.
   */
  public double step(double pressureKpa, double temperatureC, double scaledDtSols) {
    switch (branch(temperatureC)) {
      case COLD:
        return -take(pressureKpa, params.coldFractionPerSol() * scaledDtSols);
      case MILD:
        return -take(pressureKpa, params.mildFractionPerSol() * scaledDtSols);
      default:
        return release(params.releaseRateKpaPerSol() * scaledDtSols);
    }
  }

  private double take(double pressureKpa, double fraction) {
    double above = pressureKpa - params.baselineKpa();
    if (above <= 0) {
      return 0;
    }
    double amount = Math.min(1.0, fraction) * above;
    stored += amount;
    return amount;
  }

  private double release(double wanted) {
    double fromStored = Math.min(stored, wanted);
    stored -= fromStored;
    double fromBuried = Math.min(buriedRemainingKpa(), wanted - fromStored);
    buriedReleased += fromBuried;
    return fromStored + fromBuried;
  }

  /**
   * The rate the frost has right now, in kPa per sol, signed (negative when it takes), at the given
   * rate scale (pace multiplier times group scale). For the Monitor; changes nothing.
   */
  public double ratePerSol(double pressureKpa, double temperatureC, double scale) {
    return switch (branch(temperatureC)) {
      case COLD ->
          -Math.max(0, pressureKpa - params.baselineKpa()) * params.coldFractionPerSol() * scale;
      case MILD ->
          -Math.max(0, pressureKpa - params.baselineKpa()) * params.mildFractionPerSol() * scale;
      case RELEASE ->
          stored + buriedRemainingKpa() > 1e-12 ? params.releaseRateKpaPerSol() * scale : 0;
    };
  }

  /**
   * Pressure at which the taking branch balances a net source: {@code baseline + S / fraction},
   * where S and the fraction are both per sol at the same scale, so the answer does not depend on
   * pace or group size. NaN when the release branch applies or the fraction is 0.
   */
  public double equilibriumKpa(double netSourcePerSol, double temperatureC, double scale) {
    Branch b = branch(temperatureC);
    double f = b == Branch.COLD ? params.coldFractionPerSol() : params.mildFractionPerSol();
    if (b == Branch.RELEASE || f <= 0 || scale <= 0) {
      return Double.NaN;
    }
    return params.baselineKpa() + netSourcePerSol / (f * scale);
  }

  public State toState() {
    return new State(stored, buriedReleased);
  }

  public void restore(State s) {
    this.stored = Math.max(0, s.storedKpa());
    this.buriedReleased = Math.max(0, Math.min(params.buriedKpa(), s.buriedReleasedKpa()));
  }

  /**
   * The file {@code data/mars/sinks/polar_frost.json}: one set of numbers and a baseline per
   * planet. {@code baselineKpa.default} applies to every planet without its own entry.
   */
  public record Def(Params defaults, Map<String, Double> baselineByPlanet) {
    public Params forPlanet(String planetId) {
      Double b = baselineByPlanet.get(planetId);
      if (b == null) {
        return defaults;
      }
      return new Params(
          b,
          defaults.coldBelowC(),
          defaults.releaseAboveC(),
          defaults.coldFractionPerSol(),
          defaults.mildFractionPerSol(),
          defaults.releaseRateKpaPerSol(),
          defaults.buriedKpa());
    }

    public static Def parse(JsonObject o, String file) {
      JsonElement b = o.get("baselineKpa");
      if (b == null || !b.isJsonObject()) {
        throw new IllegalArgumentException(file + ": missing object field 'baselineKpa'");
      }
      JsonObject bo = b.getAsJsonObject();
      Map<String, Double> byPlanet = new LinkedHashMap<>();
      if (bo.has("planets")) {
        bo.getAsJsonObject("planets")
            .entrySet()
            .forEach(e -> byPlanet.put(e.getKey(), e.getValue().getAsDouble()));
      }
      try {
        return new Def(
            new Params(
                num(bo, "default", file),
                num(o, "coldBelowC", file),
                num(o, "releaseAboveC", file),
                num(o, "coldFractionPerSol", file),
                num(o, "mildFractionPerSol", file),
                num(o, "releaseRateKpaPerSol", file),
                num(o, "buriedKpa", file)),
            Map.copyOf(byPlanet));
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException(file + ": " + e.getMessage(), e);
      }
    }

    private static double num(JsonObject o, String f, String file) {
      JsonElement e = o.get(f);
      if (e == null || !e.isJsonPrimitive() || !e.getAsJsonPrimitive().isNumber()) {
        throw new IllegalArgumentException(file + ": missing number field '" + f + "'");
      }
      return e.getAsDouble();
    }
  }
}
