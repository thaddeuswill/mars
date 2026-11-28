package mars.index.sim;

import mars.core.ParameterId;

/** A stored Index parameter, defined in {@code data/mars/parameters/<name>.json}. */
public record ParameterDef(
    ParameterId id, String unit, double start, double min, double max, Loss loss) {
  /**
   * A restoring loss (optional, in the parameter file): each sol the value falls by {@code perSol}
   * of its distance above {@code baseline}, so a warmed planet cools slowly. It never goes below
   * the baseline. Natural rate: it scales with pace like the polar frost, and thresholds do not.
   */
  public record Loss(double baseline, double perSol) {
    public Loss {
      if (perSol < 0 || perSol >= 1) {
        throw new IllegalArgumentException("loss perSol must be in [0, 1): " + perSol);
      }
    }
  }

  public ParameterDef(ParameterId id, String unit, double start, double min, double max) {
    this(id, unit, start, min, max, null);
  }

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
