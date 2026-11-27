package mars.power;

/**
 * One planet's shared power grid (docs/tech/stage2-plan.md slice 3; balance sheet §2b). Pure Java.
 * Units are "power units per sol"; one unit is 1 kWh (the InSight array made about 5 kWh per sol
 * when clean, sourced in balance sheet §2a). Per step: supply fills demand first, the battery
 * covers a shortfall, and any surplus charges the battery. Cost per step: a few additions.
 *
 * <p>Power is physical, so it is never scaled by pace; only the machines' planet effect is.
 */
public final class PowerGrid {
  /** What the grid can say about itself; the Monitor words these. */
  public enum State {
    /** Nothing on the grid draws power. */
    IDLE,
    /** Demand fully met. */
    POWERED,
    /** Some power, less than demand. */
    SHORT,
    /** Demand but no supply and an empty battery. */
    UNPOWERED
  }

  /** Power tiers: 1 life support, 2 Regolith Ovens, 3 terraforming (balance sheet 2d.4). */
  public static final int TIERS = 3;

  private double supplyPerSol;
  private double demandPerSol;
  private final double[] tierDemand = new double[TIERS + 1]; // index 1..3 (0 unused)
  private final double[] lastSat = {1, 1, 1, 1};
  private double capacity;
  private double charge;

  /** Set the totals (units per sol, units). Charge is clamped to the new capacity. */
  public void setTotals(double supplyPerSol, double demandPerSol, double capacity) {
    setTierTotals(supplyPerSol, new double[] {0, 0, Math.max(0, demandPerSol)}, capacity);
  }

  /**
   * Set the totals with the demand split by tier ({@code demandByTier[0]} is tier 1). A tier gets
   * what is left after the tiers above it (supply first, then the battery); inside a tier every
   * machine gets the same share (balance sheet 2d.4).
   */
  public void setTierTotals(double supplyPerSol, double[] demandByTier, double capacity) {
    this.supplyPerSol = Math.max(0, supplyPerSol);
    double total = 0;
    for (int t = 1; t <= TIERS; t++) {
      tierDemand[t] = t - 1 < demandByTier.length ? Math.max(0, demandByTier[t - 1]) : 0;
      total += tierDemand[t];
    }
    this.demandPerSol = total;
    this.capacity = Math.max(0, capacity);
    this.charge = Math.min(this.charge, this.capacity);
  }

  /** Share (0 to 1) of tier {@code tier}'s demand met over {@code dtSols} if a step ran now. */
  public double tierPreview(int tier, double dtSols) {
    return allocate(dtSols)[tier];
  }

  /** Share of tier {@code tier}'s demand met in the last step (1 before any step). */
  public double tierSatisfaction(int tier) {
    return lastSat[tier];
  }

  public double tierDemandPerSol(int tier) {
    return tierDemand[tier];
  }

  /** Sat per tier (index 1..3) for a step of {@code dt}: supply then battery, tier by tier. */
  private double[] allocate(double dt) {
    double[] sat = new double[TIERS + 1];
    double avail = supplyPerSol * dt + charge;
    for (int t = 1; t <= TIERS; t++) {
      double need = tierDemand[t] * dt;
      if (need <= 0) {
        sat[t] = 1.0;
        continue;
      }
      double got = Math.min(need, avail);
      sat[t] = got / need;
      avail -= got;
    }
    return sat;
  }

  /** Fraction of demand met over {@code dtSols} if a step ran now. Does not change the grid. */
  public double preview(double dtSols) {
    return satisfaction(dtSols);
  }

  /** Run one step of {@code dtSols}; returns the fraction of demand met (0 to 1). */
  public double step(double dtSols) {
    double[] sat = allocate(dtSols);
    double used = 0;
    for (int t = 1; t <= TIERS; t++) {
      used += sat[t] * tierDemand[t] * dtSols;
      lastSat[t] = sat[t];
    }
    double made = supplyPerSol * dtSols;
    charge = Math.max(0, Math.min(capacity, charge + made - used));
    return satisfaction(sat, dtSols);
  }

  private double satisfaction(double dtSols) {
    return satisfaction(allocate(dtSols), dtSols);
  }

  /** Overall share of demand met: used over needed (the same as before tiers, for one tier). */
  private double satisfaction(double[] sat, double dtSols) {
    if (demandPerSol <= 0) {
      return 1.0;
    }
    double used = 0;
    for (int t = 1; t <= TIERS; t++) {
      used += sat[t] * tierDemand[t];
    }
    return Math.min(1.0, used / demandPerSol);
  }

  public State state(double dtSols) {
    if (demandPerSol <= 0) {
      return State.IDLE;
    }
    double sat = satisfaction(dtSols);
    if (sat >= 0.999) {
      return State.POWERED;
    }
    return sat <= 1e-9 ? State.UNPOWERED : State.SHORT;
  }

  public double supplyPerSol() {
    return supplyPerSol;
  }

  public double demandPerSol() {
    return demandPerSol;
  }

  public double capacity() {
    return capacity;
  }

  public double charge() {
    return charge;
  }

  /** Restore a saved charge (clamped). */
  public void setCharge(double charge) {
    this.charge = Math.max(0, Math.min(charge, capacity));
  }
}
