package mars.index.sim;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mars.core.Ids;
import mars.core.ParameterId;
import mars.gates.Condition;
import mars.gates.GateDef;
import mars.gates.GateTracker;
import mars.hazards.dust.PanelDustTuning;
import mars.hazards.storm.StormSchedule;
import mars.hazards.storm.StormTuning;
import mars.power.PowerGrid;
import mars.session.SessionClock;

/**
 * One planet's running simulation: the Index model, the fixed-step counter, and the machines that
 * contribute to it (ARCHITECTURE.md 2.1, 2.16). Pure Java. Cost: {@link #onServerTick} is one
 * increment and one compare per tick; a step is one add per contribution.
 */
public final class PlanetSim {
  private final PlanetaryIndexModel model;
  private final Pace pace;
  private final Map<String, MachineDef> defs;

  /** Machine type to its positions (block positions packed as longs), in placement order. */
  private final Map<String, Set<Long>> machines = new LinkedHashMap<>();

  private final Map<String, Map<Long, List<PlanetaryIndexModel.ContributionHandle>>> handles =
      new LinkedHashMap<>();
  private int pendingTicks;
  private final PowerGrid grid = new PowerGrid();

  /** Output fraction (0 to 1) of a machine by position, when not 1 (solar dust). */
  private final Map<Long, Double> efficiency = new LinkedHashMap<>();

  private final GateTracker gates;
  private final PressureHistory history = new PressureHistory();
  private final List<String> newlyLatched = new ArrayList<>();

  /** The polar-frost sink, or null when a planet has none (older tests; every real planet has). */
  private final FrostSink frost;

  /**
   * The dust-storm schedule, or null when this planet has none (older tests; every real planet
   * has).
   */
  private StormSchedule storm;

  private PanelDustTuning dust;
  private StormSchedule.State restoredStorm;
  private final List<StormNotice> stormNotices = new ArrayList<>();

  /**
   * What the game layer must announce or apply after a storm change. {@code stagesToAdd} is the
   * dust every panel gains (set on ENDED only).
   */
  public record StormNotice(StormSchedule.Event event, int stagesToAdd) {}

  /** Machine-state names a gate condition may use. {@code life_support_ok}: see lifeSupportOk. */
  public static final Set<String> MACHINE_STATES = Set.of("life_support_ok");

  public PlanetSim(IndexData.Loaded index, Map<String, MachineDef> machineDefs, Pace pace) {
    this(index, machineDefs, List.of(), pace);
  }

  public PlanetSim(
      IndexData.Loaded index,
      Map<String, MachineDef> machineDefs,
      List<GateDef> gateDefs,
      Pace pace) {
    this(index, machineDefs, gateDefs, null, pace);
  }

  /** With the planet's polar-frost numbers ({@code frostParams} null: no sink). */
  public PlanetSim(
      IndexData.Loaded index,
      Map<String, MachineDef> machineDefs,
      List<GateDef> gateDefs,
      FrostSink.Params frostParams,
      Pace pace) {
    this.frost = frostParams == null ? null : new FrostSink(frostParams);
    this.gates = new GateTracker(gateDefs, pace.classroom());
    for (String name : gates.machineStatesUsed()) {
      if (!MACHINE_STATES.contains(name)) {
        throw new IllegalArgumentException(
            "A gate uses the unknown machine state '" + name + "'; known: " + MACHINE_STATES);
      }
    }
    this.model = new PlanetaryIndexModel(index.parameters(), index.boilingPoint(), pace);
    this.pace = pace;
    this.defs = Map.copyOf(machineDefs);
    if (frost != null) {
      // The sink is a rate like any machine: it gets the pace multiplier (already in dt) and the
      // group scale, so the balance point does not move with the mode (SME review Q2).
      model.addCoupling(
          (values, naturalDt) ->
              values.add(
                  Ids.PRESSURE,
                  frost.step(
                      values.get(Ids.PRESSURE),
                      values.get(Ids.TEMPERATURE),
                      naturalDt * pace.groupRateScale())));
    }
    model.setBeforeStep(
        dt -> {
          stepStorm(dt);
          double sat = grid.step(dt);
          applyPower(sat);
        });
    model.setAfterStep(
        dt -> {
          evaluateGates(dt);
          history.record(model.sols(), model.current(Ids.PRESSURE));
        });
  }

  /**
   * Give this planet its dust-storm schedule (ARCHITECTURE.md 2.22). Uses the state a save carried,
   * or starts a new schedule from {@code seed}. The cut and the dust come from {@code dust} (never
   * scaled by pace); only the timing comes from the pace's storm mode.
   */
  public void attachStorms(StormTuning tuning, PanelDustTuning dust, long seed) {
    this.dust = dust;
    StormTuning.Mode mode = tuning.mode(pace.classroom());
    this.storm =
        restoredStorm != null
            ? new StormSchedule(mode, tuning.capPhase1(), restoredStorm)
            : StormSchedule.initial(mode, tuning.capPhase1(), seed, model.sols());
    restoredStorm = null;
    refreshPower();
  }

  /** The storm schedule, or null when this planet has none. */
  public StormSchedule storm() {
    return storm;
  }

  /** Fraction of sunlight left by the storm now (1 when none): 1 - cut x storm cap. */
  public double stormSolarFactor() {
    if (storm == null || dust == null || !storm.active()) {
      return 1.0;
    }
    return 1.0 - dust.stormSunlightCutPerCap() * storm.activeCap();
  }

  /** Operator: start a storm now. Returns false if one is already on or there is no schedule. */
  public boolean stormStart(StormSchedule.Kind kind) {
    if (storm == null || !storm.forceStart(model.sols(), kind)) {
      return false;
    }
    stormNotices.add(new StormNotice(StormSchedule.Event.STARTED, 0));
    refreshPower();
    return true;
  }

  /** Operator: end the storm now (the panels still get their dust). */
  public boolean stormEnd() {
    if (storm == null) {
      return false;
    }
    double cap = storm.activeCap();
    if (!storm.forceEnd(model.sols())) {
      return false;
    }
    stormNotices.add(new StormNotice(StormSchedule.Event.ENDED, dust.stagesAtStormEnd(cap)));
    refreshPower();
    return true;
  }

  /**
   * Storm changes since the last call (the game announces them and dusts the panels), then clears.
   */
  public List<StormNotice> drainStormNotices() {
    List<StormNotice> out = List.copyOf(stormNotices);
    stormNotices.clear();
    return out;
  }

  /** Positions of the machines of one type (a copy), for the game layer to update their blocks. */
  public List<Long> machinePositions(String type) {
    Set<Long> set = machines.get(type);
    return set == null ? List.of() : List.copyOf(set);
  }

  /** Advance the schedule to the end of this step. Never called while the session is paused. */
  private void stepStorm(double dtSols) {
    if (storm == null) {
      return;
    }
    double capBefore = storm.activeCap();
    List<StormSchedule.Event> events = storm.step(model.sols() + dtSols);
    for (StormSchedule.Event e : events) {
      stormNotices.add(
          new StormNotice(
              e, e == StormSchedule.Event.ENDED ? dust.stagesAtStormEnd(capBefore) : 0));
    }
    if (events.contains(StormSchedule.Event.STARTED)
        || events.contains(StormSchedule.Event.ENDED)) {
      refreshPower();
    }
  }

  /** True when this planet has a polar-frost sink. */
  public boolean hasFrost() {
    return frost != null;
  }

  /** The frost's rate right now in kPa per sol, signed (minus: takes air). 0 with no sink. */
  public double frostRatePerSol() {
    if (frost == null) {
      return 0;
    }
    return frost.ratePerSol(
        model.current(Ids.PRESSURE),
        model.current(Ids.TEMPERATURE),
        pace.rateMultiplier() * pace.groupRateScale());
  }

  /** kPa the poles hold now (taken so far, not yet given back). */
  public double frostStoredKpa() {
    return frost == null ? 0 : frost.storedKpa();
  }

  public double frostBuriedRemainingKpa() {
    return frost == null ? 0 : frost.buriedRemainingKpa();
  }

  public FrostSink.Branch frostBranch() {
    return frost == null ? null : frost.branch(model.current(Ids.TEMPERATURE));
  }

  /**
   * Pressure at which the frost balances the sources now (baseline plus net source over the sink
   * fraction); NaN with no sink, in the release branch, or with no source. Same in every pace mode.
   */
  public double frostEquilibriumKpa() {
    if (frost == null) {
      return Double.NaN;
    }
    double s = model.netRatePerSol(Ids.PRESSURE);
    return s <= 0
        ? Double.NaN
        : frost.equilibriumKpa(
            s, model.current(Ids.TEMPERATURE), pace.rateMultiplier() * pace.groupRateScale());
  }

  /** The air-pressure history and events the Monitor plots. */
  public PressureHistory history() {
    return history;
  }

  public GateTracker gates() {
    return gates;
  }

  /**
   * Stand-in for "life support is running" until the oxygen maker and sealing exist (Stage 3): the
   * planet has power supply and the grid covers its demand. A real life-support check replaces this
   * one function; the gate files do not change.
   */
  boolean lifeSupportOk() {
    return grid.supplyPerSol() > 0
        && grid.state(stepSols()) != PowerGrid.State.UNPOWERED
        && grid.state(stepSols()) != PowerGrid.State.SHORT;
  }

  private void evaluateGates(double dtSols) {
    Condition.Context ctx =
        new Condition.Context() {
          @Override
          public double value(ParameterId id) {
            return model.current(id);
          }

          @Override
          public boolean machineState(String name) {
            return name.equals("life_support_ok") && lifeSupportOk();
          }
        };
    List<String> now = gates.step(ctx, dtSols, model.simTicks());
    for (String id : now) {
      history.addEventOnce(model.sols(), PressureHistory.Kind.GATE_LATCHED, id);
    }
    newlyLatched.addAll(now);
  }

  /** Admin: have the game announce a gate that was latched by hand. */
  public void queueAnnouncement(String gateId) {
    history.addEventOnce(model.sols(), PressureHistory.Kind.GATE_LATCHED, gateId);
    newlyLatched.add(gateId);
  }

  /** Gate ids that latched since the last call (the game announces them), then clears. */
  public List<String> drainNewlyLatched() {
    List<String> out = List.copyOf(newlyLatched);
    newlyLatched.clear();
    return out;
  }

  public PowerGrid power() {
    return grid;
  }

  /** Length of one step in sols. */
  public double stepSols() {
    return (double) pace.stepTicks() / pace.solTicks();
  }

  /** Set a machine's output fraction (a dusty panel makes less). */
  public void setEfficiency(long pos, double fraction) {
    double f = Math.max(0, Math.min(1, fraction));
    if (f >= 1.0) {
      efficiency.remove(pos);
    } else {
      efficiency.put(pos, f);
    }
    refreshPower();
  }

  /** Recount supply, demand and storage, then show the effect on the machines right away. */
  private void refreshPower() {
    double supply = 0;
    double demand = 0;
    double storage = 0;
    // All supply is solar so far; a storm cuts it (balance sheet 2: 60 % x storm cap). When a
    // non-solar source exists, make this per machine type.
    double sunlight = stormSolarFactor();
    for (Map.Entry<String, Set<Long>> e : machines.entrySet()) {
      MachineDef def = defs.get(e.getKey());
      if (def == null) {
        continue;
      }
      MachineDef.Power pw = def.power();
      for (long pos : e.getValue()) {
        supply += pw.supplyPerSol() * efficiency.getOrDefault(pos, 1.0) * sunlight;
        demand += pw.drawPerSol();
        storage += pw.storage();
      }
    }
    grid.setTotals(supply, demand, storage);
    applyPower(grid.preview(stepSols()));
  }

  /** Scale every powered machine's contribution by the share of power it gets. */
  private void applyPower(double satisfaction) {
    for (Map.Entry<String, Map<Long, List<PlanetaryIndexModel.ContributionHandle>>> e :
        handles.entrySet()) {
      MachineDef def = defs.get(e.getKey());
      if (def == null || (def.power().drawPerSol() <= 0 && def.requires() == null)) {
        continue;
      }
      // A machine with a "requires" condition (the ice extractor: warm enough) contributes nothing
      // while it is false, however much power it gets. Powered machines scale with their share.
      double share = def.power().drawPerSol() > 0 ? satisfaction : 1.0;
      double ok = requirementMet(def) ? 1.0 : 0.0;
      for (List<PlanetaryIndexModel.ContributionHandle> hs : e.getValue().values()) {
        for (int i = 0; i < hs.size(); i++) {
          hs.get(i).setRate(def.contributions().get(i).ratePerSol() * share * ok);
        }
      }
    }
  }

  /** True when the machine type has no {@code requires} condition, or it holds right now. */
  private boolean requirementMet(MachineDef def) {
    if (def.requires() == null) {
      return true;
    }
    return def.requires()
        .eval(
            new Condition.Context() {
              @Override
              public double value(ParameterId id) {
                return model.current(id);
              }

              @Override
              public boolean machineState(String name) {
                return false;
              }
            });
  }

  /**
   * Does this machine type's own condition hold now (the ice extractor needs temperature at or
   * above -20 C)? True for types with no condition or no definition. The Monitor and the block look
   * read this, so "running" is never shown for a machine that is doing nothing.
   */
  public boolean machineRequirementMet(String type) {
    MachineDef def = defs.get(type);
    return def == null || requirementMet(def);
  }

  public PlanetaryIndexModel model() {
    return model;
  }

  public Pace pace() {
    return pace;
  }

  /**
   * Call once per server tick. Nothing advances while the session is paused (invariant I-3):
   * counters, values and machines all stay as they were. Returns true if a step ran.
   */
  public boolean onServerTick(SessionClock clock) {
    return clock.running() && onServerTick();
  }

  /** Call once per server tick while the planet's clock runs. Returns true if a step ran. */
  public boolean onServerTick() {
    pendingTicks++;
    if (pendingTicks >= pace.stepTicks()) {
      pendingTicks = 0;
      model.step();
      return true;
    }
    return false;
  }

  /** Register a machine. Returns false if one is already registered at that position. */
  public boolean addMachine(String type, long pos) {
    boolean added = addMachineQuietly(type, pos);
    MachineDef placed = defs.get(type);
    if (added
        && placed != null
        && placed.contributions().stream().anyMatch(c -> c.parameter().equals(Ids.PRESSURE))) {
      history.addEventOnce(model.sols(), PressureHistory.Kind.MACHINE_PLACED, type);
    }
    return added;
  }

  /** Same as {@link #addMachine} but records no history event (used when loading a save). */
  private boolean addMachineQuietly(String type, long pos) {
    MachineDef def = defs.get(type);
    if (def == null) {
      throw new IllegalArgumentException(
          "Unknown machine type " + type + "; known: " + defs.keySet());
    }
    if (!machines.computeIfAbsent(type, k -> new LinkedHashSet<>()).add(pos)) {
      return false;
    }
    attach(def, pos);
    refreshPower();
    return true;
  }

  /** Unregister a machine. Returns false if none was registered there. */
  public boolean removeMachine(String type, long pos) {
    Set<Long> set = machines.get(type);
    if (set == null || !set.remove(pos)) {
      return false;
    }
    Map<Long, List<PlanetaryIndexModel.ContributionHandle>> byPos = handles.get(type);
    if (byPos != null) {
      List<PlanetaryIndexModel.ContributionHandle> hs = byPos.remove(pos);
      if (hs != null) {
        hs.forEach(PlanetaryIndexModel.ContributionHandle::remove);
      }
    }
    efficiency.remove(pos);
    refreshPower();
    return true;
  }

  /** Machine types that have at least one placed machine, in the order first placed. */
  public java.util.List<String> placedTypes() {
    java.util.List<String> out = new java.util.ArrayList<>();
    for (Map.Entry<String, Set<Long>> e : machines.entrySet()) {
      if (!e.getValue().isEmpty()) {
        out.add(e.getKey());
      }
    }
    return out;
  }

  public int machineCount(String type) {
    Set<Long> set = machines.get(type);
    return set == null ? 0 : set.size();
  }

  /** How many machines of this type make less than full output (dusty solar panels). */
  public int reducedCount(String type) {
    Set<Long> set = machines.get(type);
    if (set == null) {
      return 0;
    }
    int n = 0;
    for (long pos : set) {
      if (efficiency.getOrDefault(pos, 1.0) < 1.0) {
        n++;
      }
    }
    return n;
  }

  /** True when this machine type draws power (so "needs power" can be its reason). */
  public boolean machineDrawsPower(String type) {
    MachineDef def = defs.get(type);
    return def != null && def.power().drawPerSol() > 0;
  }

  /** How many Index contributions one machine of this type makes (0 for a load only). */
  public int contributionCount(String type) {
    MachineDef def = defs.get(type);
    return def == null ? 0 : def.contributions().size();
  }

  public boolean hasMachineDef(String type) {
    return defs.containsKey(type);
  }

  private void attach(MachineDef def, long pos) {
    List<PlanetaryIndexModel.ContributionHandle> hs = new ArrayList<>();
    for (MachineDef.Contribution c : def.contributions()) {
      hs.add(model.contribute(c.parameter(), c.ratePerSol(), def.id()));
    }
    handles.computeIfAbsent(def.id(), k -> new LinkedHashMap<>()).put(pos, hs);
  }

  public PlanetState toState() {
    Map<String, Double> values = new LinkedHashMap<>();
    model.storedValues().forEach((id, v) -> values.put(id.id(), v));
    Map<String, List<Long>> ms = new LinkedHashMap<>();
    // Types with no machines are left out, so a planet that had an Oven placed and removed has the
    // same state as one saved and loaded (the loader never sees an empty type).
    machines.forEach(
        (type, set) -> {
          if (!set.isEmpty()) {
            ms.put(type, List.copyOf(set));
          }
        });
    return new PlanetState(
        PlanetState.CURRENT_SCHEMA,
        values,
        model.simTicks(),
        pendingTicks,
        ms,
        grid.charge(),
        Map.copyOf(efficiency),
        gates.toStates(),
        history.toState(),
        frost == null ? FrostSink.State.EMPTY : frost.toState(),
        storm != null ? storm.toState() : restoredStorm);
  }

  /**
   * Rebuild a planet from a saved state. A newer schema is refused. Machine types this build has no
   * definition for keep their positions (so a later build that has them loses nothing) but
   * contribute nothing; they are listed in the returned report.
   */
  public static Loaded fromState(
      PlanetState state, IndexData.Loaded index, Map<String, MachineDef> defs, Pace pace) {
    return fromState(state, index, defs, List.of(), pace);
  }

  public static Loaded fromState(
      PlanetState state,
      IndexData.Loaded index,
      Map<String, MachineDef> defs,
      List<GateDef> gateDefs,
      Pace pace) {
    return fromState(state, index, defs, gateDefs, null, pace);
  }

  public static Loaded fromState(
      PlanetState state,
      IndexData.Loaded index,
      Map<String, MachineDef> defs,
      List<GateDef> gateDefs,
      FrostSink.Params frostParams,
      Pace pace) {
    if (state.schema() > PlanetState.CURRENT_SCHEMA) {
      throw new IllegalStateException(
          "Planet save has schema "
              + state.schema()
              + " but this build reads up to "
              + PlanetState.CURRENT_SCHEMA
              + "; refusing to load it (it would be overwritten).");
    }
    PlanetSim sim = new PlanetSim(index, defs, gateDefs, frostParams, pace);
    if (sim.frost != null && state.frost() != null) {
      sim.frost.restore(state.frost()); // schema 4 and older saves: empty reservoir
    }
    sim.gates.restore(state.gates());
    Map<ParameterId, Double> saved = new LinkedHashMap<>();
    state.values().forEach((k, v) -> saved.put(ParameterId.of(k), v));
    PlanetaryIndexModel.RestoreReport report = sim.model.restore(saved, state.simTicks());
    sim.pendingTicks = Math.max(0, Math.min(state.pendingTicks(), pace.stepTicks() - 1));
    sim.efficiency.putAll(state.efficiency());
    List<String> unknownTypes = new ArrayList<>();
    state
        .machines()
        .forEach(
            (type, positions) -> {
              if (sim.defs.containsKey(type)) {
                positions.forEach(p -> sim.addMachineQuietly(type, p));
              } else {
                sim.machines.computeIfAbsent(type, k -> new LinkedHashSet<>()).addAll(positions);
                unknownTypes.add(type);
              }
            });
    sim.refreshPower();
    sim.grid.setCharge(state.batteryCharge());
    sim.refreshPower();
    sim.history.restore(state.history() == null ? PressureHistory.State.EMPTY : state.history());
    sim.restoredStorm = state.storm(); // null for schema 5 and older: attachStorms starts afresh
    return new Loaded(sim, report, unknownTypes);
  }

  /** A restored planet plus what the restore had to skip or default. */
  public record Loaded(
      PlanetSim sim, PlanetaryIndexModel.RestoreReport report, List<String> unknownMachineTypes) {}
}
