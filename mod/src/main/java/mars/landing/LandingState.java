package mars.landing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * One player's Landing (landing-spec 3.2 and 3.3), pure. Eight steps, any order, nothing can fail:
 * a step is PENDING, then DONE (the player did it), AUTO (the world was already there) or SKIPPED.
 * The active step is the lowest PENDING one; a later step done early still ticks (decision L1).
 * Every method returns the {@link Change}s it made so the caller can log and show them; nothing
 * here knows about players, blocks or time except the tick numbers it is handed.
 *
 * <p>Cost: eight enum slots and a few flags; every call is a loop of at most eight.
 */
public final class LandingState {
  private Phase phase = Phase.NOT_OFFERED;
  private final Status[] steps = new Status[Step.values().length];
  private long activeSinceTick;
  private boolean briefingShown;
  // Progress flags for the steps that need more than one observation.
  private boolean wentOut;
  private boolean outerDoorUsed;
  private boolean ovenPlaced;
  private boolean patchedByMe;

  public LandingState() {
    Arrays.fill(steps, Status.PENDING);
  }

  /** The saved form: names only, so an unknown name from another build reads as the default. */
  public record Saved(
      Phase phase,
      List<Status> steps,
      long activeSinceTick,
      boolean briefingShown,
      boolean wentOut,
      boolean outerDoorUsed,
      boolean ovenPlaced,
      boolean patchedByMe) {}

  public Saved toSaved() {
    return new Saved(
        phase,
        List.of(steps),
        activeSinceTick,
        briefingShown,
        wentOut,
        outerDoorUsed,
        ovenPlaced,
        patchedByMe);
  }

  public static LandingState fromSaved(Saved s) {
    LandingState l = new LandingState();
    l.phase = s.phase() == null ? Phase.NOT_OFFERED : s.phase();
    for (int i = 0; i < l.steps.length && i < s.steps().size(); i++) {
      Status st = s.steps().get(i);
      l.steps[i] = st == null ? Status.PENDING : st;
    }
    l.activeSinceTick = s.activeSinceTick();
    l.briefingShown = s.briefingShown();
    l.wentOut = s.wentOut();
    l.outerDoorUsed = s.outerDoorUsed();
    l.ovenPlaced = s.ovenPlaced();
    l.patchedByMe = s.patchedByMe();
    return l;
  }

  public Phase phase() {
    return phase;
  }

  public Status status(Step step) {
    return steps[step.ordinal()];
  }

  /** The lowest PENDING step while the checklist runs, else null. */
  public Step active() {
    if (phase != Phase.RUNNING) {
      return null;
    }
    for (Step s : Step.values()) {
      if (status(s) == Status.PENDING) {
        return s;
      }
    }
    return null;
  }

  /** True when the active step has been showing for {@code delayTicks} (the hint, landing 3.3). */
  public boolean hintDue(long nowTick, long delayTicks) {
    return active() != null && nowTick - activeSinceTick >= delayTicks;
  }

  // ---- phase moves ----

  /** First join on a planet: show the offer. Only from NOT_OFFERED. */
  public List<Change> offer() {
    if (phase != Phase.NOT_OFFERED) {
      return List.of();
    }
    phase = Phase.OFFERED;
    return List.of(Change.offered());
  }

  /** "Start checklist" (or the command). From NOT_OFFERED or OFFERED. */
  public List<Change> start(long nowTick) {
    if (phase != Phase.NOT_OFFERED && phase != Phase.OFFERED) {
      return List.of();
    }
    phase = Phase.RUNNING;
    activeSinceTick = nowTick;
    return List.of(Change.started());
  }

  /** "I know the drill" or Skip all: every unfinished step becomes SKIPPED; final. */
  public List<Change> skipAll() {
    if (phase.over()) {
      return List.of();
    }
    for (int i = 0; i < steps.length; i++) {
      if (steps[i] == Status.PENDING) {
        steps[i] = Status.SKIPPED;
      }
    }
    phase = Phase.SKIPPED_ALL;
    return List.of(Change.skippedAll());
  }

  /** Skip one step. Only while RUNNING and only a PENDING step. */
  public List<Change> skipStep(Step step, long nowTick) {
    return move(step, Status.SKIPPED, nowTick);
  }

  /** The player did the step ({@code DONE}) or the world already was ({@code AUTO}). */
  public List<Change> mark(Step step, Status how, long nowTick) {
    if (how != Status.DONE && how != Status.AUTO) {
      throw new IllegalArgumentException("mark takes DONE or AUTO, not " + how);
    }
    return move(step, how, nowTick);
  }

  private List<Change> move(Step step, Status to, long nowTick) {
    if (phase != Phase.RUNNING || status(step) != Status.PENDING) {
      return List.of();
    }
    List<Change> out = new ArrayList<>(2);
    Step before = active();
    steps[step.ordinal()] = to;
    out.add(Change.step(step, to));
    if (active() == null) {
      phase = Phase.DONE;
      out.add(Change.finished());
    } else if (active() != before) {
      activeSinceTick = nowTick;
    }
    return out;
  }

  // ---- observations ----

  /** The player right-clicked a hull door and it opened (step 1). */
  public List<Change> onDoorOpened(long nowTick) {
    return mark(Step.DOOR, Status.DONE, nowTick);
  }

  /** The player read a wall oxygen tank (step 2). */
  public List<Change> onTankRead(long nowTick) {
    return mark(Step.TANK, Status.DONE, nowTick);
  }

  /** The server opened the Monitor for the player (step 3). */
  public List<Change> onMonitorOpened(long nowTick) {
    return mark(Step.MONITOR, Status.DONE, nowTick);
  }

  /** The player's inventory holds a seal kit (step 4). */
  public List<Change> onKitHeld(long nowTick) {
    return mark(Step.KIT, Status.DONE, nowTick);
  }

  /**
   * The player's own kit or a hand-placed plate closed the crack (step 5). With a kit, step 4 is
   * evidently done too (the player held one), so it ticks first.
   */
  public List<Change> onPatched(boolean withKit, long nowTick) {
    patchedByMe = true;
    List<Change> out = new ArrayList<>(3);
    if (withKit) {
      out.addAll(mark(Step.KIT, Status.DONE, nowTick));
    }
    out.addAll(mark(Step.PATCH, Status.DONE, nowTick));
    return out;
  }

  /** The player placed a Regolith Oven (step 6 needs it to be this player's, for DONE). */
  public List<Change> onOvenPlaced() {
    ovenPlaced = true;
    return List.of();
  }

  /** The player used the dome's outer (airlock) door. */
  public void onOuterDoorUsed() {
    outerDoorUsed = true;
  }

  /** The player opened the journal (step 8). */
  public List<Change> onJournalOpened(long nowTick) {
    return mark(Step.JOURNAL, Status.DONE, nowTick);
  }

  /**
   * One look at where the player is (step 7): outside means more than 2 blocks outside the dome's
   * box. Going out sets a flag; being inside again after that, having used the outer door, is DONE.
   * The "dust-off station" form of the step needs the station and is not built (flag in data).
   */
  public List<Change> onPosition(boolean outside, long nowTick) {
    if (phase != Phase.RUNNING || status(Step.AIRLOCK) != Status.PENDING) {
      return List.of();
    }
    if (outside) {
      wentOut = true;
      return List.of();
    }
    if (wentOut && outerDoorUsed) {
      return mark(Step.AIRLOCK, Status.DONE, nowTick);
    }
    return List.of();
  }

  /**
   * The once-a-second look at the planet (steps 5 and 6). A step that is this player's work ticks
   * DONE at once, even out of order; a step the world already reached becomes AUTO only when it is
   * the active step (landing-spec 3.2 group rule).
   */
  public List<Change> onFacts(Facts facts, long nowTick) {
    if (phase != Phase.RUNNING) {
      return List.of();
    }
    List<Change> out = new ArrayList<>(2);
    if (facts.habBSealed() && status(Step.PATCH) == Status.PENDING) {
      if (patchedByMe) {
        out.addAll(mark(Step.PATCH, Status.DONE, nowTick));
      } else if (active() == Step.PATCH) {
        out.addAll(mark(Step.PATCH, Status.AUTO, nowTick));
      }
    }
    if (facts.ovensPowered() && status(Step.OVENS) == Status.PENDING && phase == Phase.RUNNING) {
      if (ovenPlaced) {
        out.addAll(mark(Step.OVENS, Status.DONE, nowTick));
      } else if (active() == Step.OVENS) {
        out.addAll(mark(Step.OVENS, Status.AUTO, nowTick));
      }
    }
    return out;
  }

  // ---- briefing ----

  /** True once the checklist is over and Shift Briefing 1 has not been shown to this player. */
  public boolean briefingDue() {
    return phase.over() && !briefingShown;
  }

  public void briefingShown() {
    briefingShown = true;
  }

  /** Owner/test reset: back to the beginning (the {@code /mars landing reset} command). */
  public void reset() {
    phase = Phase.NOT_OFFERED;
    Arrays.fill(steps, Status.PENDING);
    activeSinceTick = 0;
    briefingShown = false;
    wentOut = false;
    outerDoorUsed = false;
    ovenPlaced = false;
    patchedByMe = false;
  }

  /** How many steps are not PENDING. */
  public int settled() {
    int n = 0;
    for (Status s : steps) {
      if (s != Status.PENDING) {
        n++;
      }
    }
    return n;
  }
}
