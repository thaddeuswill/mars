package mars.life;

/**
 * One planet's life support (balance sheet 2d). Pure Java. Two stores, oxygen and water, and four
 * named states (sealed, air_ok, water_ok, power_ok) that the Dome stable gate reads.
 *
 * <p>Per step the planet gives the machines' aggregate numbers ({@link Inputs}); this class moves
 * the stores and answers the states. Nothing here scales with the pace multiplier: a sol is 20
 * simulation minutes in every mode and a player breathes the same in a classroom. Only the group
 * size multiplies use, and the classroom preset shortens the air reserve (a pacing requirement).
 *
 * <p>Landing grace (dome-layout section 5.8): before {@link LifeParams#landingEndSol()} nothing is
 * made, used or leaked, so the Landing checklist is safe and the timing in balance sheet 2d.7
 * starts from the start fill.
 *
 * <p>Cost per step: a few multiplications. Memory: two doubles.
 */
public final class LifeSupport {

  /** What the planet tells life support each step. */
  public record Inputs(
      double sols,
      int players,
      boolean classroom,
      boolean sealed,
      /** O2 units per sol all oxygen makers make at full power. */
      double o2MadeFull,
      /** Number of oxygen makers (for the Monitor). */
      int makers,
      /** Water units per sol the Ovens make at full power (dome stock only). */
      double ovenWaterFull,
      int reclaimers,
      double reclaimerRecovery,
      int reclaimerServes,
      double o2Capacity,
      /** Share (0 to 1) of tier 1 power demand met this step. */
      double tier1Satisfaction,
      /** Share of tier 2 power demand met this step. */
      double tier2Satisfaction,
      /** Power units per sol the tier 1 machines draw. */
      double tier1DrawPerSol,
      double batteryCharge,
      boolean anyTier1) {}

  /** The four gate states. */
  public record States(boolean sealed, boolean airOk, boolean waterOk, boolean powerOk) {
    public boolean all() {
      return sealed && airOk && waterOk && powerOk;
    }
  }

  /** Everything the Monitor shows about the last step. */
  public record Report(
      boolean started,
      double o2Stored,
      double o2Capacity,
      double o2Made,
      double o2Used,
      double o2Leak,
      double o2Net,
      double reserveNeeded,
      double solsOfAirLeft,
      double waterStored,
      double waterNetPerSol,
      double waterSolsLeft,
      int makers,
      int playersServedByMakers,
      States states) {}

  /** The saved form (PlanetState schema 7). */
  public record State(double o2, double water) {}

  private final LifeParams params;
  private double o2;
  private double water;
  private Inputs last;
  private Report report;

  public LifeSupport(LifeParams params) {
    this.params = params;
  }

  public LifeParams params() {
    return params;
  }

  /**
   * Fill the tanks to the start fill for a group: O2 14 per player, water 60 (balance sheet 2d.2).
   */
  public void startFill(int players, double o2Capacity) {
    o2 = Math.min(o2Capacity, params.o2StartFillPerPlayer() * players);
    water = Math.min(params.waterTankCapacity(), params.waterStartFill());
  }

  public double o2() {
    return o2;
  }

  public double water() {
    return water;
  }

  /** Last computed report; null until the first step or refresh. */
  public Report report() {
    return report;
  }

  public State toState() {
    return new State(o2, water);
  }

  public void restore(State s) {
    o2 = Math.max(0, s.o2());
    water = Math.max(0, s.water());
  }

  /** Advance one step of {@code dtSols} (not scaled by pace). */
  public void step(double dtSols, Inputs in) {
    last = in;
    boolean started = in.sols() >= params.landingEndSol();
    double use = in.players() * params.o2UsePerPlayerSol();
    double made = in.o2MadeFull() * in.tier1Satisfaction();
    double leak = in.sealed() ? 0 : params.leakO2PerSol();
    double waterUse = in.players() * params.waterUsePerPlayerSol();
    double servedShare =
        in.players() <= 0
            ? 1.0
            : Math.min(1.0, (double) in.reclaimers() * in.reclaimerServes() / in.players());
    double recovered =
        in.reclaimers() > 0
            ? waterUse * in.reclaimerRecovery() * servedShare * in.tier1Satisfaction()
            : 0;
    double ovenWater = in.ovenWaterFull() * in.tier2Satisfaction();
    double waterNet = ovenWater - (waterUse - recovered);
    if (started) {
      o2 = clamp(o2 + (made - use - leak) * dtSols, 0, in.o2Capacity());
      water = clamp(water + waterNet * dtSols, 0, params.waterTankCapacity());
    } else {
      o2 = clamp(o2, 0, in.o2Capacity());
    }
    double net = made - use - leak;
    double reserve = params.reserveSols(in.classroom()) * use;
    // The Landing is safe: until it ends no state but sealed can be true, so no hold can start.
    boolean airOk = started && net >= -1e-9 && o2 >= reserve - 1e-9;
    boolean running = started && in.reclaimers() > 0 && in.tier1Satisfaction() > 0;
    double waterSolsLeft = waterNet >= 0 ? Double.POSITIVE_INFINITY : water / -waterNet;
    boolean waterOk = running && (waterNet >= 0 || waterSolsLeft >= params.waterLastSols());
    boolean powerOk =
        started
            && (!in.anyTier1()
                || (in.tier1Satisfaction() >= 0.999 && in.batteryCharge() >= in.tier1DrawPerSol()));
    double airLeft = net >= 0 ? Double.POSITIVE_INFINITY : o2 / -net;
    report =
        new Report(
            started,
            o2,
            in.o2Capacity(),
            made,
            use,
            leak,
            net,
            reserve,
            airLeft,
            water,
            waterNet,
            waterSolsLeft,
            in.makers(),
            (int)
                Math.floor(params.o2UsePerPlayerSol() > 0 ? made / params.o2UsePerPlayerSol() : 0),
            new States(in.sealed(), airOk, waterOk, powerOk));
  }

  /** The states as of the last step; all false before the first step. */
  public States states() {
    return report == null ? new States(false, false, false, false) : report.states();
  }

  private static double clamp(double v, double lo, double hi) {
    return Math.max(lo, Math.min(hi, v));
  }
}
