package mars.dome;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The sealed-volume state of one planet (ARCHITECTURE.md 2.3): the domes the players have
 * registered, whether each is sealed, and the cache that lets an unchanged dome cost nothing.
 *
 * <p>A dome is registered by its seed block (the Planetary Monitor): the air volumes touching the
 * seed's six faces are the dome. It is sealed when every one of them is sealed (see {@link
 * SealCheck}). A face that looks straight at open sky is not a room (a Monitor mounted in a wall);
 * a seed with no air next to it counts as sealed (nothing can leak into it); a seed with only open
 * sky around it is leaking.
 *
 * <p>Cache rule. A dome keeps the set of positions its last check probed. {@link #blockChanged}
 * marks a dome dirty only when the changed position is in that set, which is exact: a block outside
 * the set cannot change the answer. {@link #tick} rechecks dirty domes (at most once per tick per
 * dome, so many edits in one tick cost one fill) and, every {@link #RECHECK_TICKS} ticks, marks all
 * domes dirty as a safety net for changes nobody told us about (a vanilla block broken by an
 * explosion, a piston).
 *
 * <p>Cost: with no dome registered, {@link #tick} is one increment and one compare. With domes: one
 * fill of at most {@code cap + 1} open cells per dome per dirty tick, and one per dome per {@link
 * #RECHECK_TICKS}. Memory: the probed set, at most about six times the cap longs per dome.
 */
public final class DomeSeals {
  /**
   * The largest room the game calls a dome (ARCHITECTURE.md 2.3; invented, about 2.8 x the
   * 700-block starting dome).
   */
  public static final int DEFAULT_CAP = 2000;

  /** Ticks between safety-net rechecks (5 s): an unsignalled change shows within this. */
  public static final int RECHECK_TICKS = 100;

  public enum Status {
    UNKNOWN,
    SEALED,
    LEAKING,
    TOO_BIG
  }

  /** One dome as saved: its seed position, last known status and volume. */
  public record Dome(long seed, Status status, int volume) {}

  /** The saved form (PlanetState schema 6). */
  public record State(List<Dome> domes) {
    public static final State EMPTY = new State(List.of());
  }

  /** A dome whose status changed in a refresh. */
  public record Change(long seed, Status before, Status after, int volume) {
    /** True when a dome that was sealed is not any more. */
    public boolean breach() {
      return before == Status.SEALED && after != Status.SEALED;
    }

    /** True when a dome that was not sealed is now (first check or after a repair). */
    public boolean sealedNow() {
      return after == Status.SEALED && before != Status.SEALED;
    }
  }

  private static final class Entry {
    Status status = Status.UNKNOWN;
    int volume;
    boolean dirty = true;
    Set<Long> probed = Set.of();
  }

  private final int cap;
  private final Map<Long, Entry> domes = new LinkedHashMap<>();
  private int ticksSinceCheck;
  private int fills;

  public DomeSeals() {
    this(DEFAULT_CAP);
  }

  public DomeSeals(int cap) {
    this.cap = cap;
  }

  public int cap() {
    return cap;
  }

  /** Register a dome by its seed block. False if already registered. */
  public boolean register(long seed) {
    if (domes.containsKey(seed)) {
      return false;
    }
    domes.put(seed, new Entry());
    return true;
  }

  public boolean unregister(long seed) {
    return domes.remove(seed) != null;
  }

  public int count() {
    return domes.size();
  }

  /** Number of flood fills run so far (tests check the cache with this). */
  public int fillsRun() {
    return fills;
  }

  public Status status(long seed) {
    Entry e = domes.get(seed);
    return e == null ? null : e.status;
  }

  public int volume(long seed) {
    Entry e = domes.get(seed);
    return e == null ? 0 : e.volume;
  }

  /**
   * The state of the planet's domes as one word: LEAKING if any leaks, then TOO_BIG, then UNKNOWN
   * (not checked yet), else SEALED. Null when no dome is registered.
   */
  public Status overall() {
    if (domes.isEmpty()) {
      return null;
    }
    boolean tooBig = false;
    boolean unknown = false;
    for (Entry e : domes.values()) {
      switch (e.status) {
        case LEAKING -> {
          return Status.LEAKING;
        }
        case TOO_BIG -> tooBig = true;
        case UNKNOWN -> unknown = true;
        case SEALED -> {}
      }
    }
    return tooBig ? Status.TOO_BIG : unknown ? Status.UNKNOWN : Status.SEALED;
  }

  /** A block changed at {@code pos}: mark every dome whose last check looked at it. */
  public void blockChanged(long pos) {
    for (Entry e : domes.values()) {
      if (!e.dirty && e.probed.contains(pos)) {
        e.dirty = true;
      }
    }
  }

  /** Call once per planet tick. Returns the status changes found (usually none, usually empty). */
  public List<Change> tick(Probe probe) {
    if (domes.isEmpty()) {
      return List.of();
    }
    if (++ticksSinceCheck >= RECHECK_TICKS) {
      ticksSinceCheck = 0;
      markAllDirty();
    }
    return refreshDirty(probe);
  }

  public void markAllDirty() {
    domes.values().forEach(e -> e.dirty = true);
  }

  /** Recheck every dirty dome now. */
  public List<Change> refreshDirty(Probe probe) {
    List<Change> changes = null;
    for (Map.Entry<Long, Entry> me : domes.entrySet()) {
      Entry e = me.getValue();
      if (!e.dirty) {
        continue;
      }
      Status before = e.status;
      check(me.getKey(), e, probe);
      if (e.status != before) {
        if (changes == null) {
          changes = new ArrayList<>();
        }
        changes.add(new Change(me.getKey(), before, e.status, e.volume));
      }
    }
    return changes == null ? List.of() : changes;
  }

  private static final int[][] FACES = {
    {1, 0, 0}, {-1, 0, 0}, {0, 1, 0}, {0, -1, 0}, {0, 0, 1}, {0, 0, -1}
  };

  private void check(long seed, Entry e, Probe probe) {
    int sx = Pos.x(seed);
    int sy = Pos.y(seed);
    int sz = Pos.z(seed);
    Set<Long> probed = new HashSet<>();
    probed.add(seed);
    Status result = Status.SEALED;
    int volume = 0;
    boolean backedByOutside = false;
    boolean anyFill = false;
    for (int[] f : FACES) {
      int nx = sx + f[0];
      int ny = sy + f[1];
      int nz = sz + f[2];
      if (probed.contains(Pos.pack(nx, ny, nz))) {
        continue; // already part of an earlier fill of this dome, or a wall seen from it
      }
      fills++;
      SealCheck.Result r = SealCheck.fill(probe, nx, ny, nz, cap, probed);
      if (r.status() == SealCheck.Status.OPEN_TO_OUTSIDE && r.volume() == 0) {
        // The face itself looks at open sky: the seed is mounted in a wall (or stands outside).
        // That face is not a room; the faces that are rooms decide.
        backedByOutside = true;
        continue;
      }
      anyFill |= r.status() != SealCheck.Status.NO_SPACE;
      volume += r.volume();
      if (r.status() == SealCheck.Status.OPEN_TO_OUTSIDE) {
        result = Status.LEAKING;
        break;
      }
      if (r.status() == SealCheck.Status.TOO_BIG) {
        result = Status.TOO_BIG;
        break;
      }
    }
    if (result == Status.SEALED && backedByOutside && !anyFill) {
      result = Status.LEAKING; // nothing but open air around the seed
    }
    e.status = result;
    e.volume = volume;
    e.probed = probed;
    e.dirty = false;
  }

  /** The saved form. The cache is not saved: a loaded dome is rechecked on the first tick. */
  public State toState() {
    List<Dome> out = new ArrayList<>();
    domes.forEach((seed, e) -> out.add(new Dome(seed, e.status, e.volume)));
    return new State(List.copyOf(out));
  }

  /** Replace the registry with a saved one. Every dome is rechecked on the next tick. */
  public void restore(State state) {
    domes.clear();
    ticksSinceCheck = 0;
    if (state == null) {
      return;
    }
    for (Dome d : state.domes()) {
      Entry e = new Entry();
      e.status = d.status();
      e.volume = d.volume();
      domes.put(d.seed(), e);
    }
  }
}
