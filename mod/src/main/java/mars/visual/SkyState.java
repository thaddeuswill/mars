package mars.visual;

/**
 * What the client currently believes about the planet's pressure and the pressure it is showing
 * (which follows the live value with a time constant, so a change is a shift and not a snap). Pure
 * Java so the rules are unit tested. The first value after joining or arriving snaps, because a
 * player arriving at 5 kPa must not watch the sky "grow" from the start colours.
 */
public final class SkyState {
  private static final double MIN_RELATIVE_CHANGE = 0.001;
  private double target = Double.NaN;
  private double shown = Double.NaN;
  private SkyKeyframes.Look look;
  private final SkyKeyframes keyframes;
  private final double tauSeconds;

  public SkyState(SkyKeyframes keyframes, double tauSeconds) {
    this.keyframes = keyframes;
    this.tauSeconds = tauSeconds;
  }

  /**
   * Server side rule: a pressure is worth a packet when it is the first, or moved by over 0.1 %.
   */
  public static boolean shouldSend(Double lastSent, double now) {
    return lastSent == null || Math.abs(now - lastSent) > MIN_RELATIVE_CHANGE * Math.abs(lastSent);
  }

  /** The server sent a pressure. Ignores values that are not positive numbers. */
  public void setTarget(double kpa) {
    if (!(kpa > 0) || Double.isInfinite(kpa)) {
      return;
    }
    target = kpa;
    if (Double.isNaN(shown)) {
      shown = kpa;
      look = keyframes.sample(shown);
    }
  }

  /** Forget everything (leaving the world). */
  public void reset() {
    target = Double.NaN;
    shown = Double.NaN;
    look = null;
  }

  /** Called once per client tick. Recomputes the look only when the shown value moved. */
  public void advance(double dtSeconds) {
    if (Double.isNaN(target)) {
      return;
    }
    double next = Smoothing.step(shown, target, dtSeconds, tauSeconds);
    if (next != shown || look == null) {
      shown = next;
      look = keyframes.sample(shown);
    }
  }

  public boolean hasValue() {
    return !Double.isNaN(shown);
  }

  public double shownKpa() {
    return shown;
  }

  public double targetKpa() {
    return target;
  }

  /** The current look, or the first keyframe's look before any value arrived. */
  public SkyKeyframes.Look look() {
    return look != null ? look : keyframes.sample(0);
  }
}
