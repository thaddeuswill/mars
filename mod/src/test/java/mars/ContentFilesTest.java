package mars;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * Layer L0 content gate: every JSON in pack/ parses, and the dimension files reference each other.
 */
class ContentFilesTest {
  @Test
  void everyJsonFileParses() throws Exception {
    List<Path> files;
    try (Stream<Path> s = Files.walk(TestData.pack())) {
      files = s.filter(p -> p.toString().endsWith(".json")).toList();
    }
    for (Path f : files) {
      try (Reader r = Files.newBufferedReader(f, StandardCharsets.UTF_8)) {
        JsonParser.parseReader(r);
      } catch (Exception e) {
        throw new AssertionError(
            "Bad JSON in " + TestData.pack().relativize(f) + ": " + e.getMessage(), e);
      }
    }
    System.out.println("parsed " + files.size() + " JSON files under pack/");
    assertTrue(files.size() >= 10, "expected at least 10 content files, read " + files.size());
  }

  @Test
  void planetOneUsesTheMarsTypeAndBiome() throws Exception {
    JsonObject dim = read(TestData.data("dimension/planet_1.json"));
    String type = dim.get("type").getAsString();
    String biome =
        dim.getAsJsonObject("generator").getAsJsonObject("settings").get("biome").getAsString();
    System.out.println("planet_1: type " + type + ", biome " + biome);
    assertEquals("mars:mars", type);
    assertTrue(
        Files.exists(TestData.data("dimension_type/mars.json")),
        "dimension_type/mars.json missing");
    assertEquals("mars:regolith_flats", biome);
    assertTrue(
        Files.exists(TestData.data("worldgen/biome/regolith_flats.json")), "biome file missing");
  }

  @Test
  void noNonDimensionFilesInTheDimensionFolder() throws Exception {
    // data/<ns>/dimension/ is a vanilla registry folder: any JSON there is loaded as a dimension.
    try (Stream<Path> s = Files.list(TestData.data("dimension"))) {
      List<String> names =
          s.map(p -> p.getFileName().toString()).filter(n -> n.endsWith(".json")).toList();
      System.out.println("dimension folder holds: " + names);
      assertTrue(
          names.stream().allMatch(n -> n.startsWith("planet_")), "unexpected files: " + names);
    }
  }

  private static JsonObject read(Path p) throws Exception {
    try (Reader r = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
      return JsonParser.parseReader(r).getAsJsonObject();
    }
  }
}
