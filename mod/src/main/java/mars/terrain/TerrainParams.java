package mars.terrain;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Terrain numbers of one planet, read from {@code data/mars/planets/<planet>.json}. Nothing here
 * has a default: a missing key is an error that names the key (no silent fallback).
 */
public record TerrainParams(
    double windTowardDeg,
    Pad pad,
    CraterShape craterShape,
    List<CraterLevel> smallLevels,
    LargeLevel large,
    List<FixedCrater> fixed,
    Dunes dunes,
    Surface surface,
    Rocks rocks) {

  /** Flat starting pad. */
  public record Pad(int flatRadius, int rampRadius, int heightY) {}

  /** Shared crater profile numbers. */
  public record CraterShape(
      double depthMin,
      double depthMax,
      double rimRatio,
      int minRimBlocks,
      double rimWidthFraction,
      double reachRadii) {}

  /** One diameter octave of small craters: at most one per {@code cell} x {@code cell}. */
  public record CraterLevel(int cell, double chance, double diameterMin, double diameterMax) {}

  /** The large complex craters (flat floor, central peak). */
  public record LargeLevel(
      int cell,
      double chance,
      double diameterMin,
      double diameterMax,
      double depthMin,
      double depthMax,
      double floorMin,
      double floorMax,
      double peakMin,
      double peakMax) {}

  /** A hand-placed crater. */
  public record FixedCrater(int x, int z, double diameter) {}

  /** Dune field numbers. */
  public record Dunes(
      double fieldScale,
      double thresholdLow,
      double thresholdHigh,
      double wavelength,
      double heightMin,
      double heightMax,
      double windwardFraction,
      double crestWarp,
      double crestLength) {}

  /** Top-block overrides (block ids as text). */
  public record Surface(
      String rimBlock,
      double rimBlockMinDiameter,
      String ejectaBlock,
      double ejectaBlockChance,
      String duneSand) {}

  /** One-block rocks on crater rims and ejecta. */
  public record Rocks(double rimChance, double ejectaChance, List<String> blocks) {}

  /** Parses the whole planet file; {@code source} names the file in error messages. */
  public static TerrainParams parse(JsonObject root, String source) {
    JsonObject t = obj(root, "terrain", source);
    JsonObject wind = obj(t, "wind", source + " terrain");
    JsonObject pad = obj(t, "pad", source + " terrain");
    JsonObject craters = obj(t, "craters", source + " terrain");
    JsonObject shape = obj(craters, "shape", source + " craters");
    JsonObject large = obj(craters, "large", source + " craters");
    JsonObject dunes = obj(t, "dunes", source + " terrain");
    JsonObject surface = obj(t, "surface", source + " terrain");
    JsonObject rocks = obj(t, "rocks", source + " terrain");
    String cs = source + " craters.shape";
    String cl = source + " craters.large";
    String sd = source + " dunes";

    List<CraterLevel> levels = new ArrayList<>();
    for (JsonElement e : arr(craters, "small_levels", source + " craters")) {
      JsonObject o = e.getAsJsonObject();
      levels.add(
          new CraterLevel(
              (int) num(o, "cell", cs),
              num(o, "chance", cs),
              num(o, "diameter_min", cs),
              num(o, "diameter_max", cs)));
    }
    List<FixedCrater> fixed = new ArrayList<>();
    for (JsonElement e : arr(craters, "fixed", source + " craters")) {
      JsonObject o = e.getAsJsonObject();
      fixed.add(
          new FixedCrater((int) num(o, "x", cs), (int) num(o, "z", cs), num(o, "diameter", cs)));
    }
    List<String> rockBlocks = new ArrayList<>();
    for (JsonElement e : arr(rocks, "blocks", source + " rocks")) {
      rockBlocks.add(e.getAsString());
    }
    TerrainParams p =
        new TerrainParams(
            num(wind, "blows_toward_deg", source + " wind"),
            new Pad(
                (int) num(pad, "flat_radius", source + " pad"),
                (int) num(pad, "ramp_radius", source + " pad"),
                (int) num(pad, "height_y", source + " pad")),
            new CraterShape(
                num(shape, "depth_ratio_min", cs),
                num(shape, "depth_ratio_max", cs),
                num(shape, "rim_ratio", cs),
                (int) num(shape, "min_rim_blocks", cs),
                num(shape, "rim_width_fraction", cs),
                num(shape, "reach_radii", cs)),
            List.copyOf(levels),
            new LargeLevel(
                (int) num(large, "cell", cl),
                num(large, "chance", cl),
                num(large, "diameter_min", cl),
                num(large, "diameter_max", cl),
                num(large, "depth_ratio_min", cl),
                num(large, "depth_ratio_max", cl),
                num(large, "floor_fraction_min", cl),
                num(large, "floor_fraction_max", cl),
                num(large, "peak_ratio_min", cl),
                num(large, "peak_ratio_max", cl)),
            List.copyOf(fixed),
            new Dunes(
                num(dunes, "field_scale", sd),
                num(dunes, "field_threshold_low", sd),
                num(dunes, "field_threshold_high", sd),
                num(dunes, "wavelength", sd),
                num(dunes, "height_min", sd),
                num(dunes, "height_max", sd),
                num(dunes, "windward_fraction", sd),
                num(dunes, "crest_warp", sd),
                num(dunes, "crest_length", sd)),
            new Surface(
                str(surface, "rim_block", source + " surface"),
                num(surface, "rim_block_min_diameter", source + " surface"),
                str(surface, "ejecta_block", source + " surface"),
                num(surface, "ejecta_block_chance", source + " surface"),
                str(surface, "dune_sand", source + " surface")),
            new Rocks(
                num(rocks, "rim_chance", source + " rocks"),
                num(rocks, "ejecta_chance", source + " rocks"),
                List.copyOf(rockBlocks)));
    p.validate(source);
    return p;
  }

  private void validate(String source) {
    require(pad.flatRadius > 0 && pad.rampRadius > pad.flatRadius, source, "pad radii");
    require(!smallLevels.isEmpty(), source, "small_levels");
    for (CraterLevel l : smallLevels) {
      // Largest reach (radius x reach_radii) must stay within one neighbour cell.
      double reach = l.diameterMax / 2 * craterShape.reachRadii;
      require(
          reach <= l.cell, source, "crater level cell " + l.cell + " is smaller than its reach");
      require(l.diameterMin > 0 && l.diameterMax > l.diameterMin, source, "level diameters");
      require(l.chance >= 0 && l.chance <= 1, source, "level chance");
    }
    require(
        large.diameterMax / 2 * craterShape.reachRadii <= large.cell,
        source,
        "large crater cell smaller than its reach");
    require(
        dunes.windwardFraction > 0.5 && dunes.windwardFraction < 1, source, "windward_fraction");
    require(dunes.thresholdHigh > dunes.thresholdLow, source, "dune thresholds");
    require(!rocks.blocks.isEmpty(), source, "rock blocks");
  }

  private static void require(boolean ok, String source, String what) {
    if (!ok) {
      throw new IllegalArgumentException(source + ": invalid " + what);
    }
  }

  private static JsonObject obj(JsonObject o, String key, String where) {
    JsonElement e = o.get(key);
    if (e == null || !e.isJsonObject()) {
      throw new IllegalArgumentException(where + ": missing object '" + key + "'");
    }
    return e.getAsJsonObject();
  }

  private static JsonArray arr(JsonObject o, String key, String where) {
    JsonElement e = o.get(key);
    if (e == null || !e.isJsonArray()) {
      throw new IllegalArgumentException(where + ": missing array '" + key + "'");
    }
    return e.getAsJsonArray();
  }

  private static double num(JsonObject o, String key, String where) {
    JsonElement e = o.get(key);
    if (e == null || !e.isJsonPrimitive() || !e.getAsJsonPrimitive().isNumber()) {
      throw new IllegalArgumentException(where + ": missing number '" + key + "'");
    }
    return e.getAsDouble();
  }

  private static String str(JsonObject o, String key, String where) {
    JsonElement e = o.get(key);
    if (e == null || !e.isJsonPrimitive() || !e.getAsJsonPrimitive().isString()) {
      throw new IllegalArgumentException(where + ": missing string '" + key + "'");
    }
    return e.getAsString();
  }
}
