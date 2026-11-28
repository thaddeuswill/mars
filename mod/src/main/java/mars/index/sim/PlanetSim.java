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

  /** Panel dust, the storm and the planet seed; null on a planet without hazards (bare tests). */
  private mars.hazards.HazardParams hazards;

  private mars.hazards.dust.PanelDustModel dust;
  private mars.hazards.storm.StormScheduler storm;
  private long planetSeed;

  /** Hazard state read from a save, waiting for {@link #configureHazards}. */
  private mars.hazards.HazardState pendingHazards = mars.hazards.HazardState.EMPTY;

  /** Panel states whose block should be rewritten (stage changed); drained by the game layer. */
  private final Map<Long, mars.hazards.dust.PanelDustModel.View> blockSync = new LinkedHashMap<>();

  /**
   * Solar supply as it would be at full output, and with dust (units per sol), from the last count.
   */
  private double panelSupplyClean;

  private double panelSupplyDusty;

  private final GateTracker gates;
  private final PressureHistory history = new PressureHistory();
  private final mars.dome.DomeSeals domes = new mars.dome.DomeSeals();
  private final List<String> newlyLatched = new ArrayList<>();

  /** The polar-frost sink, or null when a planet has none (older tests; every real planet has). */
  private final FrostSink frost;

  /**
   * Machine-state names a gate condition may use. The four life-support states are real (balance
   * sheet 2d.5). {@code life_support_ok} is the Stage 2 stand-in, kept only for planets without a
   * life-support model (saves from schema 6 and older, unit tests that build a bare planet); see
   * {@link #lifeSupportOk}.
   */
  public static final Set<String> MACHINE_STATES =
      Set.of("life_support_ok", "sealed", "air_ok", "water_ok", "power_ok");

  /** Life support, or null on a planet that runs on the legacy stand-in. */
  private mars.life.LifeSupport life;

  private final mars.life.LifeParams lifeParams;

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
    this(index, machineDefs, gateDefs, frostParams, null, pace);
  }

  /** With life support ({@code lifeParams} null: the legacy stand-in, no model). */
  public PlanetSim(
      IndexData.Loaded index,
      Map<String, MachineDef> machineDefs,
      List<GateDef> gateDefs,
      FrostSink.Params frostParams,
      mars.life.LifeParams lifeParams,
      Pace pace) {
    this.lifeParams = lifeParams;
    this.life = lifeParams == null ? null : new mars.life.LifeSupport(lifeParams);
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
          hazardsStep(dt);
          grid.step(dt);
          applyPower(tierSat(false, dt));
          stepLife(dt);
        });
    model.setAfterStep(
        dt -> {
          evaluateGates(dt);
          history.record(model.sols(), model.current(Ids.PRESSURE));
        });
  }

  /** The planet's registered domes and whether they are sealed (ARCHITECTURE.md 2.3). */
  public mars.dome.DomeSeals domes() {
    return domes;
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
   * The legacy stand-in for "life support is running": the planet has power supply and the grid
   * covers its demand. Since Stage 3 slice 2 the gate reads the four real states; this stays only
   * for a planet with no life-support model ({@code life == null}: a world saved before schema 7,
   * or a bare test planet), so a world mid-way through Dome stable keeps its old behaviour and a
   * latched gate stays latched.
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
            if (name.equals("life_support_ok") || life == null) {
              return lifeSupportOk(); // legacy planet: all four states read the stand-in
            }
            mars.life.LifeSupport.States st = life.states();
            return switch (name) {
              case "sealed" -> st.sealed();
              case "air_ok" -> st.airOk();
              case "water_ok" -> st.waterOk();
              case "power_ok" -> st.powerOk();
              default -> false;
            };
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

  /** The power tier of a machine type: its {@code priority}, or the lowest tier if none is set. */
  private static int tierOf(MachineDef def) {
    int p = def.life().priority();
    return p >= 1 && p <= PowerGrid.TIERS ? p : PowerGrid.TIERS;
  }

  /** Recount supply, demand and storage, then show the effect on the machines right away. */
  private void refreshPower() {
    double supply = 0;
    double[] demand = new double[PowerGrid.TIERS];
    double storage = 0;
    double clean = 0;
    double dusty = 0;
    for (Map.Entry<String, Set<Long>> e : machines.entrySet()) {
      MachineDef def = defs.get(e.getKey());
      if (def == null) {
        continue;
      }
      MachineDef.Power pw = def.power();
      for (long pos : e.getValue()) {
        double eff = efficiency.getOrDefault(pos, 1.0);
        supply += pw.supplyPerSol() * eff;
        if (isPanelType(e.getKey())) {
          clean += pw.supplyPerSol();
          dusty += pw.supplyPerSol() * eff;
        }
        demand[tierOf(def) - 1] += pw.drawPerSol();
        storage += pw.storage();
      }
    }
    panelSupplyClean = clean;
    panelSupplyDusty = dusty;
    grid.setTierTotals(supply, demand, storage);
    applyPower(tierSat(true, stepSols()));
    refreshLife();
  }

  private boolean isPanelType(String type) {
    return hazards != null && hazards.storm().affectedMachineTypes().contains(type);
  }

  /** Share of each tier's demand met: the last step's, or a preview of the next one. */
  private double[] tierSat(boolean preview, double dt) {
    double[] sat = new double[PowerGrid.TIERS + 1];
    for (int t = 1; t <= PowerGrid.TIERS; t++) {
      sat[t] = preview ? grid.tierPreview(t, dt) : grid.tierSatisfaction(t);
    }
    return sat;
  }

  /** Scale every powered machine's contribution by the share its tier gets. */
  private void applyPower(double[] tierSat) {
    for (Map.Entry<String, Map<Long, List<PlanetaryIndexModel.ContributionHandle>>> e :
        handles.entrySet()) {
      MachineDef def = defs.get(e.getKey());
      if (def == null || def.power().drawPerSol() <= 0) {
        continue;
      }
      double satisfaction = tierSat[tierOf(def)];
      for (List<PlanetaryIndexModel.ContributionHandle> hs : e.getValue().values()) {
        for (int i = 0; i < hs.size(); i++) {
          hs.get(i).setRate(def.contributions().get(i).ratePerSol() * satisfaction);
        }
      }
    }
  }

  /**
   * Switch this planet to the legacy stand-in ({@link #lifeSupportOk}), as a world saved before
   * schema 7 does. For tests of the gate mechanics that do not want life support in the way; never
   * called by the game.
   */
  public void useLegacyLifeSupport() {
    life = null;
  }

  /** The life-support model, or null on a legacy planet. */
  public mars.life.LifeSupport life() {
    return life;
  }

  /**
   * Fill the tanks and the battery for the start of a game (balance sheet 2d.2: O2 14 per player,
   * water 60, battery 5). The starting dome's placer calls this once, after its machines exist.
   */
  public void startLifeSupport() {
    if (life == null) {
      return;
    }
    life.startFill(pace.groupSize(), o2Capacity());
    grid.setCharge(lifeParams.batteryStartCharge());
    refreshLife();
  }

  /** O2 tank capacity: the tanks placed, at most the dome's design capacity (200). */
  private double o2Capacity() {
    double sum = 0;
    boolean any = false;
    for (Map.Entry<String, Set<Long>> e : machines.entrySet()) {
      MachineDef def = defs.get(e.getKey());
      if (def != null && def.life().capacity() > 0) {
        any = true;
        sum += def.life().capacity() * e.getValue().size();
      }
    }
    // A planet whose data has no tank at all (bare test planets) uses the design capacity.
    return any ? Math.min(sum, lifeParams.o2TankCapacity()) : lifeParams.o2TankCapacity();
  }

  private mars.life.LifeSupport.Inputs lifeInputs() {
    double o2Made = 0;
    int makers = 0;
    double ovenWater = 0;
    int reclaimers = 0;
    double recovery = 0;
    int serves = 0;
    for (Map.Entry<String, Set<Long>> e : machines.entrySet()) {
      MachineDef def = defs.get(e.getKey());
      if (def == null) {
        continue;
      }
      int n = e.getValue().size();
      MachineDef.Life l = def.life();
      if (l.o2PerSol() > 0) {
        o2Made += l.o2PerSol() * n;
        makers += n;
      }
      ovenWater += l.waterPerSol() * n;
      if (l.recovery() > 0) {
        reclaimers += n;
        recovery = Math.max(recovery, l.recovery());
        serves = Math.max(serves, l.servesPlayers());
      }
    }
    mars.dome.DomeSeals.Status dome = domes.overall();
    return new mars.life.LifeSupport.Inputs(
        model.sols(),
        pace.groupSize(),
        pace.classroom(),
        dome == mars.dome.DomeSeals.Status.SEALED,
        o2Made,
        makers,
        ovenWater,
        reclaimers,
        recovery,
        serves,
        o2Capacity(),
        grid.tierSatisfaction(1),
        grid.tierSatisfaction(2),
        grid.tierDemandPerSol(1),
        grid.charge(),
        grid.tierDemandPerSol(1) > 0);
  }

  private void stepLife(double dtSols) {
    if (life != null) {
      life.step(dtSols, lifeInputs());
    }
  }

  /** Recompute the life-support report without moving the stores (after a machine change). */
  public void refreshLife() {
    if (life != null) {
      life.step(0, lifeInputs());
    }
  }

  // ---- hazards: panel dust and storms (ARCHITECTURE.md 2.17, slice 5 plan) ----

  /**
   * Switch the planet's hazards on with their data. Call once, right after construction or {@link
   * #fromState}: it takes the saved hazard state (schema 8) or, for an older save, infers each
   * panel's stage from its saved output and starts the storm schedule from the loaded time. {@code
   * newSeed} is used only when the save has no planet seed.
   */
  public void configureHazards(mars.hazards.HazardParams params, long newSeed) {
    this.hazards = params;
    mars.hazards.HazardState saved = pendingHazards;
    this.planetSeed = saved.seed() != 0 ? saved.seed() : (newSeed == 0 ? 1 : newSeed);
    this.dust = new mars.hazards.dust.PanelDustModel(params.dust().maxStage());
    this.storm =
        new mars.hazards.storm.StormScheduler(
            params.storm(), pace.classroom(), pace.solTicks(), pace.stepTicks(), planetSeed);
    if (saved.storm() != null) {
      storm.restore(saved.storm());
    }
    for (Map.Entry<String, Set<Long>> e : machines.entrySet()) {
      if (!isPanelType(e.getKey())) {
        continue;
      }
      for (long pos : e.getValue()) {
        var sv = saved.dust().get(pos);
        if (sv != null) {
          dust.restore(pos, sv);
        } else {
          var inf = params.dust().infer(efficiency.getOrDefault(pos, 1.0));
          dust.put(pos, inf == null ? 0 : inf.stage(), inf != null && inf.partlyCleaned());
        }
        syncEfficiency(pos);
      }
    }
    refreshPower();
  }

  public boolean hasHazards() {
    return dust != null;
  }

  /** The planet's random seed (saved); 0 before {@link #configureHazards}. */
  public long planetSeed() {
    return planetSeed;
  }

  private int capIndex() {
    return hazards.storm().capIndex(gates::isLatched);
  }

  /** True while a storm is warned and has not started: the forecast is open. */
  public boolean stormWarned() {
    return storm != null && (storm.mode() == mars.hazards.storm.StormScheduler.Mode.WARNED);
  }

  /** The storm strength cap of the phase the planet is in now (1.0, 0.8, 0.5, 0.25). */
  public double stormCap() {
    return hazards == null ? 1.0 : hazards.storm().caps().get(capIndex()).cap();
  }

  /** Phase number (1 to 4) of the current cap row. */
  public int phase() {
    return hazards == null ? 1 : hazards.storm().caps().get(capIndex()).phase();
  }

  /** Stage interval in sols now: 3 / cap / rate multiplier (balance sheet 2a). */
  public double dustIntervalSols() {
    return hazards.dust().intervalSols(stormCap(), pace.rateMultiplier());
  }

  private void syncEfficiency(long pos) {
    var v = dust.view(pos);
    double f = hazards.dust().outputFraction(v.stage(), v.partlyCleaned());
    if (f >= 1.0) {
      efficiency.remove(pos);
    } else {
      efficiency.put(pos, f);
    }
  }

  private void applyDustChanges(java.util.Collection<Long> changed) {
    for (long pos : changed) {
      syncEfficiency(pos);
      blockSync.put(pos, dust.view(pos));
    }
    refreshPower();
  }

  /** One sim step of dust (and, later in this method, the storm). Called before the grid steps. */
  private void hazardsStep(double dtSols) {
    if (dust == null) {
      return;
    }
    List<Long> changed = new ArrayList<>(dust.advance(dtSols, dustIntervalSols()));
    long before = model.simTicks();
    long now = before + pace.stepTicks();
    long solNow = now / pace.solTicks();
    if (solNow != before / pace.solTicks() && phase() <= hazards.dust().dustDevilMaxPhase()) {
      changed.addAll(
          dust.devil(
              planetSeed,
              solNow,
              hazards.dust().dustDevilChancePerSol(),
              hazards.dust().dustDevilFromStage()));
    }
    if (!changed.isEmpty()) {
      applyDustChanges(changed);
    }
  }

  /**
   * A block was placed or changed to this dust state in the world (placement, {@code /setblock}, a
   * test). When the model disagrees the block wins (the model takes its stage with no progress);
   * when they agree (the game's own sync write) nothing happens. {@code fraction} is used only on a
   * planet without hazards.
   */
  public void setPanelDust(long pos, int stage, boolean partlyCleaned, double fraction) {
    if (dust == null) {
      setEfficiency(pos, fraction);
      return;
    }
    var cur = dust.view(pos);
    if (cur == null || (cur.stage() == stage && cur.partlyCleaned() == partlyCleaned)) {
      return;
    }
    dust.put(pos, stage, partlyCleaned);
    syncEfficiency(pos);
    refreshPower();
  }

  /** The brush: panel back to stage 0. Returns the stage before, or -1 if there is no panel. */
  public int cleanPanel(long pos) {
    if (dust == null) {
      return -1;
    }
    int before = dust.clean(pos);
    if (before >= 0) {
      applyDustChanges(List.of(pos));
    }
    return before;
  }

  /** Admin: put every panel at one stage. Returns how many panels changed. */
  public int setAllPanels(int stage, boolean partlyCleaned) {
    if (dust == null) {
      return 0;
    }
    List<Long> changed = new ArrayList<>();
    for (Map.Entry<String, Set<Long>> e : machines.entrySet()) {
      if (isPanelType(e.getKey())) {
        for (long pos : e.getValue()) {
          dust.put(pos, stage, partlyCleaned);
          changed.add(pos);
        }
      }
    }
    applyDustChanges(changed);
    return changed.size();
  }

  /** Panels at each stage 0 to max (for the admin command and tests). */
  public int[] panelStageCounts() {
    int[] counts = new int[hazards == null ? 5 : hazards.dust().maxStage() + 1];
    if (dust != null) {
      for (Map.Entry<String, Set<Long>> e : machines.entrySet()) {
        if (isPanelType(e.getKey())) {
          for (long pos : e.getValue()) {
            counts[dust.stage(pos)]++;
          }
        }
      }
    }
    return counts;
  }

  public int panelStage(long pos) {
    return dust == null ? 0 : dust.stage(pos);
  }

  public boolean panelPartlyCleaned(long pos) {
    return dust != null && dust.partlyCleaned(pos);
  }

  /** Panel states whose blocks must be rewritten, then clears the list. */
  public List<mars.hazards.dust.PanelDustModel.View> drainBlockSync() {
    List<mars.hazards.dust.PanelDustModel.View> out = List.copyOf(blockSync.values());
    blockSync.clear();
    return out;
  }

  /** True when some panel block waits to be rewritten. */
  public boolean hasBlockSync() {
    return !blockSync.isEmpty();
  }

  /** A chunk loaded: queue its panels so their blocks are brought to the planet's stage. */
  public void queueChunkSync(long chunkKey) {
    for (var v : panelsInChunk(chunkKey)) {
      blockSync.put(v.pos(), v);
    }
  }

  /** The panels in one chunk, for the sync when that chunk loads. */
  public List<mars.hazards.dust.PanelDustModel.View> panelsInChunk(long chunkKey) {
    return dust == null ? List.of() : dust.inChunk(chunkKey);
  }

  /**
   * Average share of clean solar output lost to panel dust, 0 to 1 (spec 3.1: the average over
   * panels; every panel has the same clean supply). 0 with no panel.
   */
  public double panelDustLossFraction() {
    return panelSupplyClean <= 0 ? 0 : Math.max(0, 1.0 - panelSupplyDusty / panelSupplyClean);
  }

  /** The same as a whole percent for the Monitor line. */
  public int panelDustLossPercent() {
    return (int) Math.round(panelDustLossFraction() * 100);
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
    if (dust != null && isPanelType(type)) {
      dust.put(pos, 0, false);
    }
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
    if (dust != null) {
      dust.remove(pos);
      blockSync.remove(pos);
    }
    refreshPower();
    return true;
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
    // A type whose last machine was removed is not saved: a save and a load of a planet must give
    // the same state (the removed type would otherwise come back as a missing key).
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
        domes.toState(),
        life == null ? null : life.toState(),
        hazardState());
  }

  private mars.hazards.HazardState hazardState() {
    if (dust == null) {
      return pendingHazards;
    }
    return new mars.hazards.HazardState(
        planetSeed, dust.toSaved(), storm == null ? null : storm.toState());
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
    return fromState(state, index, defs, gateDefs, frostParams, null, pace);
  }

  public static Loaded fromState(
      PlanetState state,
      IndexData.Loaded index,
      Map<String, MachineDef> defs,
      List<GateDef> gateDefs,
      FrostSink.Params frostParams,
      mars.life.LifeParams lifeParams,
      Pace pace) {
    if (state.schema() > PlanetState.CURRENT_SCHEMA) {
      throw new IllegalStateException(
          "Planet save has schema "
              + state.schema()
              + " but this build reads up to "
              + PlanetState.CURRENT_SCHEMA
              + "; refusing to load it (it would be overwritten).");
    }
    PlanetSim sim = new PlanetSim(index, defs, gateDefs, frostParams, lifeParams, pace);
    if (state.life() == null) {
      sim.life = null; // schema 6 and older: no life support in that world (legacy stand-in)
    }
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
    sim.domes.restore(state.domes()); // schema 5 and older: no domes
    if (sim.life != null && state.life() != null) {
      // After the machines are added: adding one at a time would clamp the stores to a half-built
      // tank capacity.
      sim.life.restore(state.life());
    }
    sim.refreshLife();
    sim.pendingHazards = state.hazards() == null ? mars.hazards.HazardState.EMPTY : state.hazards();
    return new Loaded(sim, report, unknownTypes);
  }

  /** A restored planet plus what the restore had to skip or default. */
  public record Loaded(
      PlanetSim sim, PlanetaryIndexModel.RestoreReport report, List<String> unknownMachineTypes) {}
}
