package mars.gates;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Evaluates one planet's gates (ARCHITECTURE.md 2.2). A gate is <b>latched</b> once reached and is
 * never evaluated or reverted again (gates lock in, invariant I-2). A hold gate counts simulation
 * sols while its condition is true, pauses while it is false, and resets only if it stays false for
 * longer than the reset grace (balance sheet 4). Pure Java. Cost per step: one condition evaluation
 * per gate that has not latched yet; zero for latched gates.
 */
public final class GateTracker {
  /** Why a gate latched. Learning credits only {@link #REACHED}. */
  public enum Cause {
    REACHED,
    ADMIN
  }

  /** The saved state of one gate. */
  public record State(
      boolean latched, Cause cause, long latchedSimTicks, double heldSols, double falseSols) {
    static final State FRESH = new State(false, Cause.REACHED, 0, 0, 0);
  }

  private final List<GateDef> defs;
  private final Map<String, State> states = new LinkedHashMap<>();
  private final boolean classroom;

  /** The reset grace of each hold comes from its gate file ({@code reset_after_sols}). */
  public GateTracker(List<GateDef> defs, boolean classroom) {
    this.defs = defs.stream().sorted(Comparator.comparingInt(GateDef::order)).toList();
    this.classroom = classroom;
    for (GateDef d : this.defs) {
      states.put(d.id(), State.FRESH);
    }
  }

  /** Every machine-state name the gates use, so the owner of the world can check them at load. */
  public Set<String> machineStatesUsed() {
    Set<String> out = new HashSet<>();
    defs.forEach(d -> d.condition().machineStates(out));
    return out;
  }

  public List<GateDef> defs() {
    return defs;
  }

  /**
   * One simulation step of {@code dtSols}. Returns the ids that latched during this call, in gate
   * order.
   */
  public List<String> step(Condition.Context ctx, double dtSols, long simTicks) {
    List<String> fresh = new ArrayList<>();
    for (GateDef d : defs) {
      State s = states.get(d.id());
      if (s.latched()) {
        continue;
      }
      boolean ok = d.condition().eval(ctx);
      if (d.hold() == null) {
        if (ok) {
          states.put(d.id(), new State(true, Cause.REACHED, simTicks, 0, 0));
          fresh.add(d.id());
        }
        continue;
      }
      double need = classroom ? d.hold().classroomSols() : d.hold().standardSols();
      double held = s.heldSols();
      double falseFor = s.falseSols();
      if (ok) {
        held += dtSols;
        falseFor = 0;
      } else {
        falseFor += dtSols;
        double resetSols =
            classroom ? d.hold().resetAfterClassroomSols() : d.hold().resetAfterStandardSols();
        if (falseFor > resetSols) {
          held = 0;
        }
      }
      if (held >= need - 1e-9) {
        states.put(d.id(), new State(true, Cause.REACHED, simTicks, need, 0));
        fresh.add(d.id());
      } else {
        states.put(d.id(), new State(false, Cause.REACHED, 0, held, falseFor));
      }
    }
    return fresh;
  }

  public boolean isLatched(String id) {
    State s = states.get(id);
    return s != null && s.latched();
  }

  public State state(String id) {
    return states.getOrDefault(id, State.FRESH);
  }

  /** Hold length in sols for this planet's mode, or 0 for a gate without a hold. */
  public double holdSols(GateDef d) {
    return d.hold() == null ? 0 : (classroom ? d.hold().classroomSols() : d.hold().standardSols());
  }

  /** Admin: latch a gate now. Returns false if the id is unknown or it had already latched. */
  public boolean latch(String id, Cause cause, long simTicks) {
    State s = states.get(id);
    if (s == null || s.latched() || defs.stream().noneMatch(d -> d.id().equals(id))) {
      return false;
    }
    states.put(id, new State(true, cause, simTicks, s.heldSols(), 0));
    return true;
  }

  /**
   * May survival players place this machine? True when no gate lists it, or every gate that lists
   * it has latched.
   */
  public boolean isUnlocked(String machineId) {
    for (GateDef d : defs) {
      if (d.unlocks().contains(machineId) && !isLatched(d.id())) {
        return false;
      }
    }
    return true;
  }

  /** The first gate (in order) that still locks this machine, or null. */
  public GateDef lockedBy(String machineId) {
    for (GateDef d : defs) {
      if (d.unlocks().contains(machineId) && !isLatched(d.id())) {
        return d;
      }
    }
    return null;
  }

  /** Copy of every gate state, for saving. */
  public Map<String, State> toStates() {
    return new LinkedHashMap<>(states);
  }

  /** Put saved states back. Ids this build has no gate for are kept (a later build may). */
  public void restore(Map<String, State> saved) {
    states.putAll(saved);
  }
}
