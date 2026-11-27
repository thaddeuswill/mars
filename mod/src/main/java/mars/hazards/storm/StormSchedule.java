package mars.hazards.storm;

import java.util.ArrayList;
import java.util.List;

/**
 * One planet's dust-storm schedule (ARCHITECTURE.md 2.22). Pure Java. A storm goes CALM, then
 * WARNING (a lead time before onset), then ACTIVE for its length, then CALM again with the next
 * start drawn from a seeded generator. The generator's state is part of the saved {@link State}, so
 * the schedule reads the same after a save and load. It is stepped only by the planet's own step,
 * which does not run while the session is paused (invariant I-3), so a paused world changes nothing
 * here. Cost per step: a few comparisons; one generator draw per storm.
 */
public final class StormSchedule {
  public enum Phase {
    CALM,
    WARNING,
    ACTIVE
  }

  /**
   * Which sort of storm: the scheduled Phase 1 storm, or the shorter drift-event ("hold") storm.
   */
  public enum Kind {
    PHASE_1,
    HOLD
  }

  /** What changed in a step. */
  public enum Event {
    WARNED,
    STARTED,
    ENDED
  }

  /**
   * The saved form. {@code startSol} is the scheduled or actual onset, {@code endSol} the end of
   * the active storm, {@code lastEndSol} when the last storm ended (-1: none yet), {@code
   * stormsEnded} how many have ended, {@code cap} the active storm's cap, {@code rng} the generator
   * state.
   */
  public record State(
      long rng,
      Phase phase,
      Kind kind,
      double startSol,
      double endSol,
      double lastEndSol,
      int stormsEnded,
      double cap) {}

  private final StormTuning.Mode mode;
  private final double capPhase1;
  private long rng;
  private Phase phase;
  private Kind kind;
  private double startSol;
  private double endSol;
  private double lastEndSol;
  private int stormsEnded;
  private double cap;

  /** The generator seed for one planet of one world: the same world and planet always agree. */
  public static long seedFor(long worldSeed, String planetId) {
    return worldSeed * 0x9E3779B97F4A7C15L + planetId.hashCode();
  }

  /** A schedule from a saved state. */
  public StormSchedule(StormTuning.Mode mode, double capPhase1, State s) {
    this.mode = mode;
    this.capPhase1 = capPhase1;
    this.rng = s.rng();
    this.phase = s.phase();
    this.kind = s.kind();
    this.startSol = s.startSol();
    this.endSol = s.endSol();
    this.lastEndSol = s.lastEndSol();
    this.stormsEnded = s.stormsEnded();
    this.cap = s.cap();
  }

  /**
   * A new schedule: the first storm at the earliest sol the data allows, but never sooner than its
   * warning lead after {@code nowSol} (an older save loaded late must still warn first).
   */
  public static StormSchedule initial(
      StormTuning.Mode mode, double capPhase1, long seed, double nowSol) {
    double start = Math.max(mode.firstNotBeforeSol(), nowSol + mode.warningSols());
    return new StormSchedule(
        mode, capPhase1, new State(seed, Phase.CALM, Kind.PHASE_1, start, start, -1, 0, capPhase1));
  }

  public State toState() {
    return new State(rng, phase, kind, startSol, endSol, lastEndSol, stormsEnded, cap);
  }

  /**
   * Advance to simulation time {@code nowSol} (the end of the step that just ran). Returns what
   * happened, in order. Normal steps are 1/240 sol, so at most one change happens per call; a
   * larger jump still reports each change once.
   */
  public List<Event> step(double nowSol) {
    List<Event> out = new ArrayList<>(0);
    if (phase == Phase.CALM && nowSol >= startSol - mode.warningSols()) {
      phase = Phase.WARNING;
      out.add(Event.WARNED);
    }
    if (phase == Phase.WARNING && nowSol >= startSol) {
      begin(startSol, kind, capPhase1);
      out.add(Event.STARTED);
    }
    if (phase == Phase.ACTIVE && nowSol >= endSol) {
      finish(endSol);
      out.add(Event.ENDED);
    }
    return out;
  }

  /** Operator: start a storm now. False (nothing changes) if one is already active. */
  public boolean forceStart(double nowSol, Kind newKind) {
    if (phase == Phase.ACTIVE) {
      return false;
    }
    begin(nowSol, newKind, capPhase1);
    return true;
  }

  /** Operator: end the active storm now. False if none is active. */
  public boolean forceEnd(double nowSol) {
    if (phase != Phase.ACTIVE) {
      return false;
    }
    finish(nowSol);
    return true;
  }

  private void begin(double at, Kind k, double stormCap) {
    phase = Phase.ACTIVE;
    kind = k;
    startSol = at;
    endSol = at + mode.lengthSols(k);
    cap = stormCap;
  }

  private void finish(double at) {
    phase = Phase.CALM;
    lastEndSol = at;
    stormsEnded++;
    kind = Kind.PHASE_1;
    double u = nextUnit();
    startSol = at + mode.gapMinSols() + u * (mode.gapMaxSols() - mode.gapMinSols());
    endSol = startSol;
  }

  /** SplitMix64: a small generator with one long of state, the same on every Java version. */
  private double nextUnit() {
    rng += 0x9E3779B97F4A7C15L;
    long z = rng;
    z = (z ^ (z >>> 30)) * 0xBF58476D1CE4E5B9L;
    z = (z ^ (z >>> 27)) * 0x94D049BB133111EBL;
    z = z ^ (z >>> 31);
    return (z >>> 11) * 0x1.0p-53;
  }

  public Phase phase() {
    return phase;
  }

  public boolean active() {
    return phase == Phase.ACTIVE;
  }

  public Kind kind() {
    return kind;
  }

  /** The active storm's cap; 0 when calm or warning. */
  public double activeCap() {
    return phase == Phase.ACTIVE ? cap : 0;
  }

  public double startSol() {
    return startSol;
  }

  public double endSol() {
    return endSol;
  }

  public double lastEndSol() {
    return lastEndSol;
  }

  public int stormsEnded() {
    return stormsEnded;
  }

  public double warningSols() {
    return mode.warningSols();
  }

  public double lengthSols(Kind k) {
    return mode.lengthSols(k);
  }

  /** Sols until the next storm's onset (0 if active); meaningful in CALM and WARNING. */
  public double solsUntilStart(double nowSol) {
    return phase == Phase.ACTIVE ? 0 : Math.max(0, startSol - nowSol);
  }

  /** Sols left in the active storm; 0 when none. */
  public double solsLeft(double nowSol) {
    return phase == Phase.ACTIVE ? Math.max(0, endSol - nowSol) : 0;
  }
}
