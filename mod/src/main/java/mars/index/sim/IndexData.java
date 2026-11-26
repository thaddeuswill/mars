package mars.index.sim;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import mars.core.ParameterId;

/**
 * Reads parameter definitions and the Antoine coefficients from JSON (Gson only, no Minecraft
 * classes). A bad file fails with the file and field named (ARCHITECTURE.md 3, Data).
 */
public final class IndexData {
  private IndexData() {}

  public record Loaded(List<ParameterDef> parameters, BoilingPoint boilingPoint) {}

  /** Load every {@code *.json} in {@code data/mars/parameters/}. */
  public static Loaded loadDirectory(Path parametersDir) throws IOException {
    List<ParameterDef> defs = new ArrayList<>();
    BoilingPoint bp = null;
    try (Stream<Path> files = Files.list(parametersDir)) {
      for (Path f : files.filter(p -> p.toString().endsWith(".json")).sorted().toList()) {
        try (Reader r = Files.newBufferedReader(f, StandardCharsets.UTF_8)) {
          JsonObject o = JsonParser.parseReader(r).getAsJsonObject();
          String kind = str(o, "kind", f);
          if (kind.equals("stored")) {
            defs.add(parameter(o, f.getFileName().toString()));
          } else if (kind.equals("derived") && "antoine".equals(str(o, "formula", f))) {
            bp = boilingPoint(o, f.getFileName().toString());
          }
        }
      }
    }
    if (bp == null) {
      throw new IllegalStateException("No Antoine boiling-point definition in " + parametersDir);
    }
    return new Loaded(defs, bp);
  }

  public static ParameterDef parameter(JsonObject o, String file) {
    return new ParameterDef(
        ParameterId.of(str(o, "id", file)),
        str(o, "unit", file),
        num(o, "start", file),
        num(o, "min", file),
        num(o, "max", file));
  }

  public static BoilingPoint boilingPoint(JsonObject o, String file) {
    JsonElement a = o.get("antoine");
    if (a == null || !a.isJsonObject()) {
      throw new IllegalArgumentException(file + ": missing object field 'antoine'");
    }
    JsonObject k = a.getAsJsonObject();
    return new BoilingPoint(
        num(k, "a", file),
        num(k, "b", file),
        num(k, "c", file),
        num(k, "kpa_to_mmhg", file),
        num(k, "valid_from_kpa", file));
  }

  private static String str(JsonObject o, String field, Object file) {
    JsonElement e = o.get(field);
    if (e == null || !e.isJsonPrimitive()) {
      throw new IllegalArgumentException(file + ": missing string field '" + field + "'");
    }
    return e.getAsString();
  }

  private static double num(JsonObject o, String field, Object file) {
    JsonElement e = o.get(field);
    if (e == null || !e.isJsonPrimitive() || !e.getAsJsonPrimitive().isNumber()) {
      throw new IllegalArgumentException(file + ": missing number field '" + field + "'");
    }
    return e.getAsDouble();
  }
}
