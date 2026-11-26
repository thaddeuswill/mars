package mars.index.sim;

import mars.core.ParameterId;

/** A stored Index parameter, defined in {@code data/mars/parameters/<name>.json}. */
public record ParameterDef(ParameterId id, String unit, double start, double min, double max) {
  public ParameterDef {
    if (min > max || start < min || start > max) {
      throw new IllegalArgumentException(
          id + ": start " + start + " must lie in [" + min + ", " + max + "]");
    }
  }

  double clamp(double v) {
    return Math.max(min, Math.min(max, v));
  }
}
