package mars.hazards.storm;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

/**
 * The dust-storm scheduler (docs/design/f1-storm-spec.md section 2; ARCHITECTURE.md 2.5). Pure Java
 * and tick based: every time is a simulation tick, nothing reads a wall clock or an unseeded
 * random, so the schedule is a function of (planet seed, ticks stepped) and it freezes with the
 * pause (I-3) and survives a save and load.
 *
 * <p>Life cycle: {@code NONE} (nothing decided) then {@code SCHEDULED} (onset known) then {@code
 * WARNED} (one sol before onset) then {@code ACTIVE} (ramp in, hold) then {@code ENDING} (ramp out)
 * then back to {@code NONE}. Events come out in this order and never skip one. The onset event is
 * returned in the same step as the first strength above 0, and always before any caller applies
 * that strength (the lock-point rule, CR-F9): a step's events are meant to be handled before its
 * strength is used.
 *
 * <p>Cost: one comparison per step when nothing is scheduled or waiting; a few multiplications
 * while a storm is active.
 */
public final class StormScheduler {
  public enum Mode {
    NONE,
    SCHEDULED,
    WARNED,
    ACTIVE,
    ENDING
  }

  public enum Kind {
    /** The next storm was decided (planet scope; only for logs and tests). */
    SCHEDULED,
    /** One sol before onset. {@code voided}: the guards failed three times (forecast void). */
    WARNED,
    /** Half a sol before onset. */
    REMINDER,
    /** The first step with strength above 0. The forecast locks here. */
    ONSET,
    /** The ramp-out begins: the first storm-end dust stage is due. */
    CLEARING,
    /** The ramp-out ended: the second storm-end dust stage is due. */
    ENDED
  }

  public record Event(Kind kind, long tick, long onsetTick, boolean voided) {}

  /** Saved form (schema 8). Times are simulation ticks; -1 means "none". */
  public record State(
      Mode mode,
      int index,
      long onsetTick,
      long lengthTicks,
      long lastEndTick,
      int retries,
      boolean reminded,
      boolean voided,
      List<Integer> stormsByCap) {
    public static final State EMPTY =
        new State(Mode.NONE, 0, -1, 0, -1, 0, false, false, List.of());
  }

  /** What the sim tells the scheduler each step. */
  public record Inputs(int capIndex, boolean landingDone, boolean solarSupplyPositive) {}

  /** The result of one step. */
  public record Step(double strength, List<Event> events) {}

  private final StormTuning tuning;
  private final boolean classroom;
  private final int solTicks;
  private final int stepTicks;
  private final long seed;

  private Mode mode = Mode.NONE;
  private int index;
  private long onsetTick = -1;
  private long lengthTicks;
  private long lastEndTick = -1;
  private int retries;
  private boolean reminded;
  private boolean voided;
  private final List<Integer> stormsByCap = new ArrayList<>();

  public StormScheduler(
      StormTuning tuning, boolean classroom, int solTicks, int stepTicks, long seed) {
    this.tuning = tuning;
    this.classroom = classroom;
    this.solTicks = solTicks;
    this.stepTicks = stepTicks;
    this.seed = seed;
    for (int i = 0; i < tuning.caps().size(); i++) {
      stormsByCap.add(0);
    }
  }

  public Mode mode() {
    return mode;
  }

  public long onsetTick() {
    return onsetTick;
  }

  public long endTick() {
    return onsetTick + lengthTicks;
  }

  public boolean voided() {
    return voided;
  }

  private long sols(double s) {
    return Math.round(s * solTicks);
  }

  private long lead() {
    return sols(tuning.warningLeadSols());
  }

  /** Strength 0 to 1 of the envelope at a tick (before the phase cap). Pure. */
  public double envelope(long tick) {
    if (mode != Mode.ACTIVE && mode != Mode.ENDING) {
      return 0;
    }
    long s = tick - onsetTick;
    if (s <= 0) {
      return 0;
    }
    long rampIn = sols(tuning.rampInSols());
    long rampOut = sols(tuning.rampOutSols());
    if (s < rampIn) {
      return (double) s / rampIn;
    }
    if (s < lengthTicks - rampOut) {
      return 1;
    }
    if (s < lengthTicks) {
      return (double) (lengthTicks - s) / rampOut;
    }
    return 0;
  }

  /**
   * Advance to {@code nowTick} (the simulation tick this step ends at). Returns the strength after
   * the phase cap and the events of this step in order.
   */
  public Step step(long nowTick, Inputs in) {
    List<Event> events = new ArrayList<>();
    if (mode == Mode.NONE) {
      scheduleNext(nowTick, in.capIndex(), events);
    }
    if (mode == Mode.SCHEDULED && nowTick >= onsetTick - lead()) {
      boolean ok = in.landingDone() && in.solarSupplyPositive();
      if (!ok && retries < tuning.maxRetries()) {
        retries++;
        onsetTick += sols(tuning.retryEverySols());
      } else {
        voided = !ok;
        mode = Mode.WARNED;
        events.add(new Event(Kind.WARNED, nowTick, onsetTick, voided));
      }
    }
    if (mode == Mode.WARNED) {
      if (!reminded && nowTick >= onsetTick - sols(tuning.reminderLeadSols())) {
        reminded = true;
        events.add(new Event(Kind.REMINDER, nowTick, onsetTick, voided));
      }
      if (nowTick >= onsetTick) {
        mode = Mode.ACTIVE;
        events.add(new Event(Kind.ONSET, nowTick, onsetTick, voided));
      }
    }
    if (mode == Mode.ACTIVE && nowTick >= onsetTick + lengthTicks - sols(tuning.rampOutSols())) {
      mode = Mode.ENDING;
      events.add(new Event(Kind.CLEARING, nowTick, onsetTick, voided));
    }
    double env = envelope(nowTick);
    if (mode == Mode.ENDING && nowTick >= onsetTick + lengthTicks) {
      events.add(new Event(Kind.ENDED, nowTick, onsetTick, voided));
      lastEndTick = onsetTick + lengthTicks;
      mode = Mode.NONE;
      onsetTick = -1;
      env = 0;
    }
    double cap = tuning.caps().get(in.capIndex()).cap();
    return new Step(env * cap, List.copyOf(events));
  }

  private void scheduleNext(long nowTick, int capIndex, List<Event> events) {
    int limit = tuning.caps().get(capIndex).maxStorms();
    if (limit >= 0 && stormsByCap.get(capIndex) >= limit) {
      return;
    }
    StormTuning.Preset p = tuning.preset(classroom);
    long earliest = nowTick + lead() + stepTicks;
    long onset;
    long length;
    if (index == 0) {
      onset = Math.max(sols(p.firstOnsetSol()), earliest);
      length = sols(p.firstLengthSols());
    } else {
      SplittableRandom r = new SplittableRandom(seed * 0x9E3779B97F4A7C15L + index);
      double gap = p.gapMinSols() + r.nextDouble() * (p.gapMaxSols() - p.gapMinSols());
      double len = p.lengthMinSols() + r.nextDouble() * (p.lengthMaxSols() - p.lengthMinSols());
      onset = Math.max(Math.max(lastEndTick, 0) + sols(gap), earliest);
      length = sols(len);
    }
    onsetTick = onset;
    lengthTicks = length;
    index++;
    retries = 0;
    reminded = false;
    voided = false;
    stormsByCap.set(capIndex, stormsByCap.get(capIndex) + 1);
    mode = Mode.SCHEDULED;
    events.add(new Event(Kind.SCHEDULED, nowTick, onsetTick, false));
  }

  public State toState() {
    return new State(
        mode,
        index,
        onsetTick,
        lengthTicks,
        lastEndTick,
        retries,
        reminded,
        voided,
        List.copyOf(stormsByCap));
  }

  public void restore(State s) {
    mode = s.mode();
    index = s.index();
    onsetTick = s.onsetTick();
    lengthTicks = s.lengthTicks();
    lastEndTick = s.lastEndTick();
    retries = s.retries();
    reminded = s.reminded();
    voided = s.voided();
    for (int i = 0; i < stormsByCap.size(); i++) {
      stormsByCap.set(i, i < s.stormsByCap().size() ? s.stormsByCap().get(i) : 0);
    }
  }
}
