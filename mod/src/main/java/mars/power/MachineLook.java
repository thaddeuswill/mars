package mars.power;

/**
 * How a powered machine should look, derived from the planet's grid state. This is the single
 * source of truth for "needs power / low power / running": the Monitor's Power row and the
 * blockstate property {@code machine_state} both read it. Pure Java.
 *
 * <ul>
 *   <li>{@link #OFF}: the grid has demand and no power at all (Monitor: "needs power").
 *   <li>{@link #IDLE}: the grid gives the machine only a share (Monitor: "low power").
 *   <li>{@link #RUNNING}: the grid meets demand (Monitor: "running").
 * </ul>
 */
public enum MachineLook {
  OFF,
  IDLE,
  RUNNING;

  /** The look of every machine that draws power, for this grid state. */
  public static MachineLook of(PowerGrid.State grid) {
    return switch (grid) {
      case UNPOWERED -> OFF;
      case SHORT -> IDLE;
      case POWERED, IDLE -> RUNNING;
    };
  }

  /**
   * The look of a machine that also has its own condition (the ice extractor needs warmth): when
   * the condition fails it is doing nothing, so it is never shown running. A look that is already
   * off or idle stays as it is.
   */
  public static MachineLook withRequirement(MachineLook look, boolean requirementMet) {
    return !requirementMet && look == RUNNING ? IDLE : look;
  }

  /**
   * Battery bars 0 to 3: quarters of the stored energy, rounded down, so 3 means 75 percent or
   * more. No capacity (or a bad number) is 0.
   */
  public static int batteryBars(double charge, double capacity) {
    if (!(capacity > 0) || !(charge > 0)) {
      return 0;
    }
    return (int) Math.min(3, Math.floor(4.0 * Math.min(1.0, charge / capacity)));
  }
}
