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

  private double supplyPerSol;
  private double demandPerSol;
  private double capacity;
  private double charge;

  /** Set the totals (units per sol, units). Charge is clamped to the new capacity. */
  public void setTotals(double supplyPerSol, double demandPerSol, double capacity) {
    this.supplyPerSol = Math.max(0, supplyPerSol);
    this.demandPerSol = Math.max(0, demandPerSol);
    this.capacity = Math.max(0, capacity);
    this.charge = Math.min(this.charge, this.capacity);
  }

  /** Fraction of demand met over {@code dtSols} if a step ran now. Does not change the grid. */
  public double preview(double dtSols) {
    return satisfaction(dtSols);
  }

  /** Run one step of {@code dtSols}; returns the fraction of demand met (0 to 1). */
  public double step(double dtSols) {
    double sat = satisfaction(dtSols);
    double made = supplyPerSol * dtSols;
    double need = demandPerSol * dtSols;
    if (made >= need) {
      charge = Math.min(capacity, charge + (made - need));
    } else {
      charge -= Math.min(charge, need - made);
    }
    return sat;
  }

  private double satisfaction(double dtSols) {
    if (demandPerSol <= 0) {
      return 1.0;
    }
    double made = supplyPerSol * dtSols;
    double need = demandPerSol * dtSols;
    if (made >= need) {
      return 1.0;
    }
    return Math.min(1.0, (made + charge) / need);
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
