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
  private SkyKeyframes.Look calmLook;
  private SkyKeyframes.Look look;
  private double stormTarget;
  private double hazeTarget;
  private double stormShown;
  private double hazeShown;
  private boolean stormKnown;
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

  /**
   * Server side rule for the storm part: worth a packet when the storm level changed at all, or the
   * haze moved by 0.02 or more, or either reached 0 or 1 exactly (so the ends are exact).
   */
  public static boolean shouldSendStorm(
      double lastStorm, double lastHaze, double storm, double haze) {
    return storm != lastStorm
        || Math.abs(haze - lastHaze) >= 0.02
        || (haze != lastHaze && (haze == 0 || haze == 1));
  }

  /**
   * The server sent the storm level (0..1) and the pre-storm haze (0..1). The first one snaps, so
   * joining in the middle of a storm shows the storm; later ones ramp (60 s in, 90 s out).
   */
  public void setStorm(double strength, double haze) {
    stormTarget = clamp01(strength);
    hazeTarget = clamp01(haze);
    if (!stormKnown) {
      stormKnown = true;
      stormShown = stormTarget;
      hazeShown = hazeTarget;
      refreshLook();
    }
  }

  private static double clamp01(double v) {
    return v > 1 ? 1 : (v > 0 ? v : 0);
  }

  /** Linear ramp toward {@code target}: up in {@code upSeconds}, down in {@code downSeconds}. */
  static double ramp(double shown, double target, double dt, double upSeconds, double downSeconds) {
    double next =
        target > shown
            ? Math.min(target, shown + dt / upSeconds)
            : Math.max(target, shown - dt / downSeconds);
    return Math.abs(next - target) < 1e-9 ? target : next; // no float residue at the ends
  }

  private void refreshLook() {
    calmLook = keyframes.sample(shown);
    look = keyframes.withStorm(calmLook, stormShown, hazeShown);
  }

  /** The server sent a pressure. Ignores values that are not positive numbers. */
  public void setTarget(double kpa) {
    if (!(kpa > 0) || Double.isInfinite(kpa)) {
      return;
    }
    target = kpa;
    if (Double.isNaN(shown)) {
      shown = kpa;
      refreshLook();
    }
  }

  /** Forget everything (leaving the world). */
  public void reset() {
    target = Double.NaN;
    shown = Double.NaN;
    look = null;
    calmLook = null;
    stormTarget = 0;
    hazeTarget = 0;
    stormShown = 0;
    hazeShown = 0;
    stormKnown = false;
  }

  /** Called once per client tick. Recomputes the look only when the shown value moved. */
  public void advance(double dtSeconds) {
    if (Double.isNaN(target)) {
      return;
    }
    double next = Smoothing.step(shown, target, dtSeconds, tauSeconds);
    var st = keyframes.storm();
    double s2 = ramp(stormShown, stormTarget, dtSeconds, st.rampInSeconds(), st.rampOutSeconds());
    double h2 = ramp(hazeShown, hazeTarget, dtSeconds, st.rampInSeconds(), st.rampOutSeconds());
    if (next != shown || s2 != stormShown || h2 != hazeShown || look == null) {
      shown = next;
      stormShown = s2;
      hazeShown = h2;
      refreshLook();
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

  /** The storm strength being shown now (0 calm to 1 full). */
  public double stormShown() {
    return stormShown;
  }

  public double hazeShown() {
    return hazeShown;
  }

  /** The current look, or the first keyframe's look before any value arrived. */
  public SkyKeyframes.Look look() {
    return look != null ? look : keyframes.sample(0);
  }
}
