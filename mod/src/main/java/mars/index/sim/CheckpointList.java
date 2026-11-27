package mars.index.sim;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Named checkpoints of one planet, newest last, at most {@code max} (the oldest is dropped). Saving
 * under an existing name replaces it. Pure and generic so the payload type (NBT in the game, a
 * string in tests) is not this class's business.
 */
public final class CheckpointList<T> {
  /** One saved checkpoint: its name, the sol it was taken at, and the payload. */
  public record Entry<T>(String name, double sol, T payload) {}

  private final int max;
  private final List<Entry<T>> entries = new ArrayList<>();

  public CheckpointList(int max) {
    if (max < 1) {
      throw new IllegalArgumentException("max must be at least 1");
    }
    this.max = max;
  }

  /** Returns the name of the entry dropped to make room, or null. */
  public String save(String name, double sol, T payload) {
    entries.removeIf(e -> e.name().equals(name));
    entries.add(new Entry<>(name, sol, payload));
    if (entries.size() > max) {
      return entries.remove(0).name();
    }
    return null;
  }

  public Optional<Entry<T>> get(String name) {
    return entries.stream().filter(e -> e.name().equals(name)).findFirst();
  }

  public List<Entry<T>> list() {
    return List.copyOf(entries);
  }
}
