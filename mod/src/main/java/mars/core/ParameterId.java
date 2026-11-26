package mars.core;

import java.util.Objects;

/** ID of a Planetary Index parameter, derived value, or signal, e.g. {@code mars:pressure}. */
public record ParameterId(String id) {
  public ParameterId {
    Objects.requireNonNull(id, "id");
    if (!id.matches("[a-z0-9_.-]+:[a-z0-9_./-]+")) {
      throw new IllegalArgumentException("Not a namespaced ID: " + id);
    }
  }

  public static ParameterId of(String id) {
    return new ParameterId(id);
  }

  @Override
  public String toString() {
    return id;
  }
}
