package mars.hazards.dust;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.Reader;

/**
 * Panel dust tuning from {@code data/mars/hazards/panel_dust.json} (balance sheet §2a). Pure Java;
 * a missing or malformed field fails with the file and field named.
 */
public record PanelDustTuning(
    double stageIntervalSols,
    int maxStage,
    double[] outputByStagePercent,
    double partlyCleanedRecoveredFraction,
    int partlyCleanedFromStage,
    int stormEndStages,
    double stormCapForExtra,
    int stormEndStagesHeavy,
    double dustDevilChancePerSol,
    int rainTargetStage,
    double localRateMultiplier,
    int localRadiusBlocks,
    double stormSunlightCutPerCap) {

  /** Output fraction of a panel (0-1) at {@code stage}, optionally partly cleaned. */
  public double outputFraction(int stage, boolean partlyCleaned) {
    double out = outputByStagePercent[stage] / 100.0;
    if (partlyCleaned && stage >= partlyCleanedFromStage) {
      out += partlyCleanedRecoveredFraction * (1.0 - out);
    }
    return out;
  }

  /** Output with a storm on top: stage factor x (1 - cut x storm cap). */
  public double outputInStorm(int stage, boolean partlyCleaned, double stormCap) {
    return outputFraction(stage, partlyCleaned) * (1.0 - stormSunlightCutPerCap * stormCap);
  }

  /** Stage interval in sols for a storm cap and rate multiplier (interval / cap / multiplier). */
  public double intervalSols(double stormCap, double rateMultiplier) {
    return stageIntervalSols / stormCap / rateMultiplier;
  }

  public static PanelDustTuning parse(Reader json, String file) {
    JsonObject o = JsonParser.parseReader(json).getAsJsonObject();
    JsonArray outs = obj(o, "output_by_stage_percent", file).getAsJsonArray("value");
    int maxStage = num(o, "max_stage", file).getAsInt();
    if (outs == null || outs.size() != maxStage + 1) {
      throw new IllegalArgumentException(
          file + ": output_by_stage_percent.value needs " + (maxStage + 1) + " entries");
    }
    double[] out = new double[outs.size()];
    for (int i = 0; i < out.length; i++) {
      out[i] = outs.get(i).getAsDouble();
    }
    JsonObject pc = obj(o, "partly_cleaned", file);
    JsonObject storm = obj(o, "storm_end", file);
    JsonObject heavy = obj(storm, "stages_added_if_storm_cap_at_least", file);
    JsonObject devil = obj(o, "dust_devil", file);
    JsonObject rain = obj(o, "rain", file);
    JsonObject local = obj(o, "local_dust", file);
    return new PanelDustTuning(
        num(obj(o, "stage_interval_sols", file), "value", file).getAsDouble(),
        maxStage,
        out,
        num(pc, "recovers_fraction_of_lost_output", file).getAsDouble(),
        num(pc, "applies_from_stage", file).getAsInt(),
        num(storm, "stages_added", file).getAsInt(),
        num(heavy, "storm_cap", file).getAsDouble(),
        num(heavy, "stages_added", file).getAsInt(),
        num(devil, "chance_per_sol", file).getAsDouble(),
        num(rain, "target_stage", file).getAsInt(),
        num(local, "rate_multiplier", file).getAsDouble(),
        num(local, "radius_blocks", file).getAsInt(),
        num(obj(o, "storm_output", file), "sunlight_cut_per_storm_cap", file).getAsDouble());
  }

  private static JsonObject obj(JsonObject o, String field, String file) {
    JsonElement e = o.get(field);
    if (e == null || !e.isJsonObject()) {
      throw new IllegalArgumentException(file + ": missing object field '" + field + "'");
    }
    return e.getAsJsonObject();
  }

  private static JsonElement num(JsonObject o, String field, String file) {
    JsonElement e = o.get(field);
    if (e == null || !e.isJsonPrimitive() || !e.getAsJsonPrimitive().isNumber()) {
      throw new IllegalArgumentException(file + ": missing number field '" + field + "'");
    }
    return e;
  }
}
