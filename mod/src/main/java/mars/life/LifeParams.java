package mars.life;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The life-support numbers, read from {@code data/mars/life_support.json} (balance sheet 2d, spec
 * docs/tech/life-support-spec.md 2.1). All values are per sol (20 simulation minutes in every pace
 * mode) and none scales with the pace multiplier; only {@code group.size} multiplies use and start
 * fills, and only the classroom preset changes {@code reserveSols}, a pacing requirement like a
 * hold, never a physical threshold.
 */
public record LifeParams(
    double o2UsePerPlayerSol,
    double waterUsePerPlayerSol,
    double o2StartFillPerPlayer,
    double waterStartFill,
    double batteryStartCharge,
    double o2TankCapacity,
    double waterTankCapacity,
    double leakO2PerSol,
    double reserveSolsStandard,
    double reserveSolsClassroom,
    double waterLastSols,
    double landingEndSol,
    double blackoutAfterSeconds,
    double blackoutEmptySuitSeconds,
    double blackoutWakeDelaySeconds,
    double suitO2Tier1,
    double suitO2Tier2) {

  public double reserveSols(boolean classroom) {
    return classroom ? reserveSolsClassroom : reserveSolsStandard;
  }

  /** Pre-built oxygen makers for a group: ceil(n / 2) (balance sheet 2d.2). */
  public static int prebuiltMakers(int players) {
    return (players + 1) / 2;
  }

  public static LifeParams parse(JsonObject o, String file) {
    JsonObject reserve = obj(o, "reserveSols", file);
    return new LifeParams(
        num(o, "o2UsePerPlayerSol", file),
        num(o, "waterUsePerPlayerSol", file),
        num(o, "o2StartFillPerPlayer", file),
        num(o, "waterStartFill", file),
        num(o, "batteryStartCharge", file),
        num(o, "o2TankCapacity", file),
        num(o, "waterTankCapacity", file),
        num(o, "leakO2PerSol", file),
        num(reserve, "standard", file + " reserveSols"),
        num(reserve, "classroom", file + " reserveSols"),
        num(o, "waterLastSols", file),
        num(o, "landingEndSol", file),
        num(o, "blackoutAfterSeconds", file),
        num(o, "blackoutEmptySuitSeconds", file),
        num(o, "blackoutWakeDelaySeconds", file),
        num(o, "suitO2Tier1", file),
        num(o, "suitO2Tier2", file));
  }

  private static JsonObject obj(JsonObject o, String field, String file) {
    JsonElement e = o.get(field);
    if (e == null || !e.isJsonObject()) {
      throw new IllegalArgumentException(file + ": missing object field '" + field + "'");
    }
    return e.getAsJsonObject();
  }

  private static double num(JsonObject o, String field, String file) {
    JsonElement e = o.get(field);
    if (e == null || !e.isJsonPrimitive() || !e.getAsJsonPrimitive().isNumber()) {
      throw new IllegalArgumentException(file + ": missing number field '" + field + "'");
    }
    double v = e.getAsDouble();
    if (v < 0) {
      throw new IllegalArgumentException(file + ": '" + field + "' must be 0 or more");
    }
    return v;
  }
}
