package mars.index.sim;

import java.util.ArrayList;
import java.util.List;

/**
 * The air-pressure history the Planetary Monitor plots: one sample per sol, plus a short list of
 * events (first machine of a pressure-making type placed, a goal reached). Pure Java.
 *
 * <p>Bounded: at most {@link #MAX_POINTS} samples. When full, every second sample is dropped and
 * the spacing doubles, so the whole history of a long game stays on the plot at coarser detail (120
 * sols is about 40 hours of standard play, so a normal game never thins). At most {@link
 * #MAX_EVENTS} events; the oldest drops first. Cost: {@link #record} is one compare per simulation
 * step (every 100 ticks); a sample is one append per sol.
 */
public final class PressureHistory {
  public static final int MAX_POINTS = 120;
  public static final int MAX_EVENTS = 16;

  /** Pressure value at a simulation time (sols). */
  public record Sample(double sol, double value) {}

  /** What happened: a machine type first placed, or a gate latched. */
  public enum Kind {
    MACHINE_PLACED,
    GATE_LATCHED
  }

  public record Event(double sol, Kind kind, String id) {}

  /** Saved form. {@code stride} is sols between samples. */
  public record State(int stride, double nextSol, List<Sample> samples, List<Event> events) {
    public static final State EMPTY = new State(1, 0, List.of(), List.of());
  }

  private int stride = 1;
  private double nextSol = 0;
  private final List<Sample> samples = new ArrayList<>();
  private final List<Event> events = new ArrayList<>();

  /** Call after every simulation step with the planet's total sols and the pressure. */
  public void record(double sols, double value) {
    if (sols + 1e-9 < nextSol) {
      return;
    }
    samples.add(new Sample(sols, value));
    nextSol = (Math.floor(sols / stride) + 1) * stride;
    if (samples.size() > MAX_POINTS) {
      List<Sample> kept = new ArrayList<>(MAX_POINTS / 2 + 1);
      for (int i = 0; i < samples.size(); i += 2) {
        kept.add(samples.get(i));
      }
      samples.clear();
      samples.addAll(kept);
      stride *= 2;
      nextSol = (Math.floor(sols / stride) + 1) * stride;
    }
  }

  /** Add an event unless the same kind and id is already listed. */
  public void addEventOnce(double sol, Kind kind, String id) {
    for (Event e : events) {
      if (e.kind() == kind && e.id().equals(id)) {
        return;
      }
    }
    events.add(new Event(sol, kind, id));
    if (events.size() > MAX_EVENTS) {
      events.remove(0);
    }
  }

  public List<Sample> samples() {
    return List.copyOf(samples);
  }

  public List<Event> events() {
    return List.copyOf(events);
  }

  public int stride() {
    return stride;
  }

  public double nextSol() {
    return nextSol;
  }

  public State toState() {
    return new State(stride, nextSol, List.copyOf(samples), List.copyOf(events));
  }

  /** Replace the contents from a saved state; bad values are repaired, not trusted. */
  public void restore(State s) {
    samples.clear();
    events.clear();
    stride = Math.max(1, s.stride());
    nextSol = Double.isFinite(s.nextSol()) ? s.nextSol() : 0;
    double last = Double.NEGATIVE_INFINITY;
    for (Sample x : s.samples()) {
      if (Double.isFinite(x.sol()) && Double.isFinite(x.value()) && x.sol() >= last) {
        samples.add(x);
        last = x.sol();
      }
    }
    while (samples.size() > MAX_POINTS) {
      samples.remove(0);
    }
    for (Event e : s.events()) {
      if (Double.isFinite(e.sol())) {
        events.add(e);
      }
    }
    while (events.size() > MAX_EVENTS) {
      events.remove(0);
    }
  }
}
