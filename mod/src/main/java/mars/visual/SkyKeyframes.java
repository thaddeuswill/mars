package mars.visual;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Sky and fog keyframes keyed on pressure (ARCHITECTURE.md 2.5.5, data in {@code
 * assets/mars/keyframes/sky.json}). Pure Java: parsing, validation and interpolation only. Pressure
 * is interpolated on log10, colours in OKLab, fractions linearly; outside the range the end
 * keyframe holds. Cost of {@link #sample}: a binary-free scan of at most a handful of keys and
 * three OKLab mixes, called about once per client tick, never per frame.
 */
public final class SkyKeyframes {
  /** One keyframe. Colours are 0xRRGGBB. Fog start and end are fractions of the render distance. */
  public record Key(
      double at,
      int zenith,
      int horizon,
      int fog,
      int sunsetHalo,
      int sunsetBand,
      int night,
      double fogStart,
      double fogEnd) {}

  /** The interpolated look at one pressure. */
  public record Look(
      int zenith,
      int horizon,
      int fog,
      int sunsetHalo,
      int sunsetBand,
      int night,
      double fogStart,
      double fogEnd) {}

  private final List<Key> keys;
  private final double smoothingSeconds;
  private final double fogEndFloorBlocks;
  private final double sunScale;
  private final double domeGradientExponent;

  public SkyKeyframes(
      List<Key> keys,
      double smoothingSeconds,
      double fogEndFloorBlocks,
      double sunScale,
      double domeGradientExponent) {
    if (keys.size() < 2) {
      throw new IllegalArgumentException("sky keyframes: need at least 2 keys, got " + keys.size());
    }
    for (int i = 0; i < keys.size(); i++) {
      Key k = keys.get(i);
      if (!(k.at() > 0)) {
        throw new IllegalArgumentException(
            "sky keyframes: keys[" + i + "].at must be > 0 (log axis), got " + k.at());
      }
      if (i > 0 && !(k.at() > keys.get(i - 1).at())) {
        throw new IllegalArgumentException(
            "sky keyframes: keys must be in strictly increasing order of 'at', keys["
                + i
                + "].at = "
                + k.at()
                + " after "
                + keys.get(i - 1).at());
      }
      if (!(k.fogStart() >= 0 && k.fogStart() < k.fogEnd() && k.fogEnd() <= 1)) {
        throw new IllegalArgumentException(
            "sky keyframes: keys[" + i + "] needs 0 <= fog_start < fog_end <= 1");
      }
    }
    if (smoothingSeconds < 0
        || fogEndFloorBlocks < 1
        || sunScale <= 0
        || domeGradientExponent <= 0) {
      throw new IllegalArgumentException(
          "sky keyframes: smoothing_s >= 0, fog_end_floor_blocks >= 1, sun_scale > 0 and"
              + " dome_gradient_exponent > 0 are required");
    }
    this.keys = List.copyOf(keys);
    this.smoothingSeconds = smoothingSeconds;
    this.fogEndFloorBlocks = fogEndFloorBlocks;
    this.sunScale = sunScale;
    this.domeGradientExponent = domeGradientExponent;
  }

  /** Reads and validates the data file; the message names the field that is wrong. */
  public static SkyKeyframes parse(JsonObject json) {
    expect(json, "input", "mars:pressure");
    expect(json, "axis", "log10");
    expect(json, "space", "oklab");
    List<Key> keys = new ArrayList<>();
    JsonArray arr = json.getAsJsonArray("keys");
    if (arr == null) {
      throw new IllegalArgumentException("sky keyframes: 'keys' is missing");
    }
    for (int i = 0; i < arr.size(); i++) {
      JsonObject k = arr.get(i).getAsJsonObject();
      String where = "keys[" + i + "]";
      keys.add(
          new Key(
              number(k, "at", where),
              color(k, "sky_zenith", where),
              color(k, "sky_horizon", where),
              color(k, "fog", where),
              color(k, "sunset_halo", where),
              color(k, "sunset_band", where),
              color(k, "night", where),
              number(k, "fog_start", where),
              number(k, "fog_end", where)));
    }
    return new SkyKeyframes(
        keys,
        number(json, "smoothing_s", "root"),
        number(json, "fog_end_floor_blocks", "root"),
        number(json, "sun_scale", "root"),
        number(json, "dome_gradient_exponent", "root"));
  }

  public List<Key> keys() {
    return keys;
  }

  public double smoothingSeconds() {
    return smoothingSeconds;
  }

  public double fogEndFloorBlocks() {
    return fogEndFloorBlocks;
  }

  public double sunScale() {
    return sunScale;
  }

  public double domeGradientExponent() {
    return domeGradientExponent;
  }

  /** The look at {@code pressureKpa}; clamped to the first and last key. */
  public Look sample(double pressureKpa) {
    Key first = keys.get(0);
    Key last = keys.get(keys.size() - 1);
    if (!(pressureKpa > first.at())) { // also catches NaN
      return look(first, first, 0);
    }
    if (pressureKpa >= last.at()) {
      return look(last, last, 0);
    }
    for (int i = 1; i < keys.size(); i++) {
      Key b = keys.get(i);
      if (pressureKpa <= b.at()) {
        Key a = keys.get(i - 1);
        double t =
            (Math.log10(pressureKpa) - Math.log10(a.at()))
                / (Math.log10(b.at()) - Math.log10(a.at()));
        return look(a, b, t);
      }
    }
    return look(last, last, 0);
  }

  private static Look look(Key a, Key b, double t) {
    return new Look(
        Oklab.mix(a.zenith(), b.zenith(), t),
        Oklab.mix(a.horizon(), b.horizon(), t),
        Oklab.mix(a.fog(), b.fog(), t),
        Oklab.mix(a.sunsetHalo(), b.sunsetHalo(), t),
        Oklab.mix(a.sunsetBand(), b.sunsetBand(), t),
        Oklab.mix(a.night(), b.night(), t),
        a.fogStart() + (b.fogStart() - a.fogStart()) * t,
        a.fogEnd() + (b.fogEnd() - a.fogEnd()) * t);
  }

  /**
   * Fog distances in blocks for a render distance in blocks: {start, end}. The end never drops
   * below the floor (low render distances stay playable) and the start stays under the end.
   */
  public double[] fogDistances(Look look, double renderDistanceBlocks) {
    double end = Math.max(fogEndFloorBlocks, look.fogEnd() * renderDistanceBlocks);
    double start = Math.min(look.fogStart() * renderDistanceBlocks, end * 0.95);
    return new double[] {Math.max(0, start), end};
  }

  /** Vanilla's day brightness of the sky (1 by day, 0 at night) for a time of day in 0..1. */
  public static double daylight(double timeOfDay) {
    double f = Math.cos(timeOfDay * 2 * Math.PI) * 2 + 0.5;
    return Math.max(0, Math.min(1, f));
  }

  private static void expect(JsonObject o, String field, String value) {
    JsonElement e = o.get(field);
    if (e == null || !e.isJsonPrimitive() || !value.equals(e.getAsString())) {
      throw new IllegalArgumentException(
          "sky keyframes: '" + field + "' must be \"" + value + "\", got " + e);
    }
  }

  private static double number(JsonObject o, String field, String where) {
    JsonElement e = o.get(field);
    if (e == null || !e.isJsonPrimitive() || !e.getAsJsonPrimitive().isNumber()) {
      throw new IllegalArgumentException(
          "sky keyframes: " + where + "." + field + " must be a number");
    }
    return e.getAsDouble();
  }

  private static int color(JsonObject o, String field, String where) {
    JsonElement e = o.get(field);
    String s = e != null && e.isJsonPrimitive() ? e.getAsString() : "";
    if (!s.matches("#[0-9A-Fa-f]{6}")) {
      throw new IllegalArgumentException(
          "sky keyframes: " + where + "." + field + " must be a #RRGGBB colour, got " + e);
    }
    return Integer.parseInt(s.substring(1), 16);
  }
}
