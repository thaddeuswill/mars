package mars.hazards.dust;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SplittableRandom;

/**
 * The dust on every solar panel of one planet, by block position (ARCHITECTURE.md 2.17, slice 5 plan
 * 1.2). Each panel keeps a fractional {@code progress} in stages (0 to {@code maxStage}); the whole
 * number is the stage the block shows and the grid uses. Time-based: the planet sim calls {@link
 * #advance} once per step with the step length in sols, so the model freezes with the pause (I-3)
 * and runs with a chunk unloaded. Pure Java: no Minecraft classes.
 *
 * <p>Cost: {@link #advance} is one add and one compare per panel per step (a step is 5 s); 300
 * panels is microseconds. The brush and the dust devil touch only the panels they hit.
 */
public final class PanelDustModel {
  /** Saved form of one panel (schema 8). */
  public record Saved(double progress, boolean partlyCleaned) {}

  /** A panel's state as the world shows it. */
  public record View(long pos, int stage, boolean partlyCleaned) {}

  private static final double EPS = 1e-9;

  private static final class Entry {
    double progress;
    boolean partly;
  }

  private final int maxStage;
  private final Map<Long, Entry> panels = new LinkedHashMap<>();

  public PanelDustModel(int maxStage) {
    this.maxStage = maxStage;
  }

  public int size() {
    return panels.size();
  }

  public boolean contains(long pos) {
    return panels.containsKey(pos);
  }

  private int stageOf(Entry e) {
    return Math.min(maxStage, (int) Math.floor(e.progress + EPS));
  }

  /** Stage 0 to {@code maxStage}; 0 for a position that has no panel. */
  public int stage(long pos) {
    Entry e = panels.get(pos);
    return e == null ? 0 : stageOf(e);
  }

  public boolean partlyCleaned(long pos) {
    Entry e = panels.get(pos);
    return e != null && e.partly;
  }

  /** Register a panel (or reset it) at a whole stage, with no progress towards the next. */
  public void put(long pos, int stage, boolean partlyCleaned) {
    Entry e = panels.computeIfAbsent(pos, k -> new Entry());
    e.progress = Math.max(0, Math.min(maxStage, stage));
    e.partly = partlyCleaned;
  }

  public boolean remove(long pos) {
    return panels.remove(pos) != null;
  }

  /**
   * Add {@code dtSols / intervalSols} stages to every panel. A panel that rises a stage loses its
   * partly-cleaned flag (fresh dust covers the wiped patch; an invented simplification, see the
   * slice 5 report). Returns the positions whose stage or flag changed.
   */
  public List<Long> advance(double dtSols, double intervalSols) {
    List<Long> changed = new ArrayList<>();
    double add = dtSols / intervalSols;
    for (Map.Entry<Long, Entry> en : panels.entrySet()) {
      Entry e = en.getValue();
      if (e.progress >= maxStage) {
        continue;
      }
      int before = stageOf(e);
      e.progress = Math.min(maxStage, e.progress + add);
      if (stageOf(e) != before) {
        e.partly = false;
        changed.add(en.getKey());
      }
    }
    return changed;
  }

  /**
   * Storm-end deposit: {@code stages} more stages on every panel, capped at the maximum, and the
   * partly-cleaned flag cleared (balance sheet 2a). Returns the positions that changed.
   */
  public List<Long> deposit(int stages) {
    List<Long> changed = new ArrayList<>();
    if (stages <= 0) {
      return changed;
    }
    for (Map.Entry<Long, Entry> en : panels.entrySet()) {
      Entry e = en.getValue();
      int before = stageOf(e);
      boolean flag = e.partly;
      e.progress = Math.min(maxStage, e.progress + stages);
      e.partly = false;
      if (stageOf(e) != before || flag) {
        changed.add(en.getKey());
      }
    }
    return changed;
  }

  /** The brush: back to stage 0, flag cleared. Returns the stage before, or -1 if no panel. */
  public int clean(long pos) {
    Entry e = panels.get(pos);
    if (e == null) {
      return -1;
    }
    int before = stageOf(e);
    e.progress = 0;
    e.partly = false;
    return before;
  }

  /**
   * The dust devil roll for one sol (ARCHITECTURE.md 2.17): each chunk that holds a panel at stage
   * {@code fromStage} or more is hit with probability {@code chance}, decided by a hash of (seed,
   * chunk, sol index) so the same planet gives the same answer after a save and load. A hit sets
   * those panels to partly cleaned. Returns the positions that changed.
   */
  public List<Long> devil(long seed, long solIndex, double chance, int fromStage) {
    Map<Long, List<Long>> byChunk = new LinkedHashMap<>();
    for (Map.Entry<Long, Entry> en : panels.entrySet()) {
      Entry e = en.getValue();
      if (stageOf(e) >= fromStage && !e.partly) {
        byChunk.computeIfAbsent(chunkKey(en.getKey()), k -> new ArrayList<>()).add(en.getKey());
      }
    }
    List<Long> changed = new ArrayList<>();
    for (Map.Entry<Long, List<Long>> c : byChunk.entrySet()) {
      if (devilStrikes(seed, c.getKey(), solIndex, chance)) {
        for (long pos : c.getValue()) {
          panels.get(pos).partly = true;
          changed.add(pos);
        }
      }
    }
    return changed;
  }

  /** True when a dust devil crosses this chunk on this sol. Pure function of its arguments. */
  public static boolean devilStrikes(long seed, long chunkKey, long solIndex, double chance) {
    long h = mix(mix(seed ^ 0x5DEECE66DL, chunkKey), solIndex);
    return new SplittableRandom(h).nextDouble() < chance;
  }

  private static long mix(long a, long b) {
    long z = a + 0x9E3779B97F4A7C15L * (b + 1);
    z = (z ^ (z >>> 30)) * 0xBF58476D1CE4E5B9L;
    z = (z ^ (z >>> 27)) * 0x94D049BB133111EBL;
    return z ^ (z >>> 31);
  }

  /** Chunk key of a packed block position (BlockPos.asLong layout: x 26 bits, z 26, y 12). */
  public static long chunkKey(long blockPos) {
    int x = (int) (blockPos >> 38);
    int z = (int) ((blockPos << 26) >> 38);
    return ((long) (x >> 4) & 0xFFFFFFFFL) | (((long) (z >> 4) & 0xFFFFFFFFL) << 32);
  }

  /** The panels in one chunk (for the chunk-load sync). */
  public List<View> inChunk(long chunkKey) {
    List<View> out = new ArrayList<>();
    for (Map.Entry<Long, Entry> en : panels.entrySet()) {
      if (chunkKey(en.getKey()) == chunkKey) {
        out.add(view(en.getKey(), en.getValue()));
      }
    }
    return out;
  }

  public View view(long pos) {
    Entry e = panels.get(pos);
    return e == null ? null : view(pos, e);
  }

  private View view(long pos, Entry e) {
    return new View(pos, stageOf(e), e.partly);
  }

  public Map<Long, Saved> toSaved() {
    Map<Long, Saved> out = new LinkedHashMap<>();
    panels.forEach((p, e) -> out.put(p, new Saved(e.progress, e.partly)));
    return out;
  }

  public void restore(long pos, Saved s) {
    Entry e = panels.computeIfAbsent(pos, k -> new Entry());
    e.progress = Math.max(0, Math.min(maxStage, s.progress()));
    e.partly = s.partlyCleaned();
  }
}
