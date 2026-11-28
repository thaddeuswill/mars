package mars;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * No raw key on screen: every translation key the code or the pack data refers to is in en_us.json.
 * Three sources: string literals in the main Java code (except the boot checks, whose strings are
 * check names), string values in the pack's data files, and the keys that are built from an id
 * (gate names, gate detail lines, block and item names, placed-machine markers).
 */
class LangKeysTest {
  private static final String PREFIXES =
      "monitor|gate|journal|block|item|log|grant|guidance|earth|commands|key|itemGroup";
  private static final Pattern KEY = Pattern.compile("^(" + PREFIXES + ")\\.[a-z0-9_.]*[a-z0-9_]$");
  private static final Pattern LITERAL =
      Pattern.compile("\"((?:" + PREFIXES + ")\\.[a-z0-9_.]+)\"");

  private static JsonObject json(Path p) throws Exception {
    return JsonParser.parseString(Files.readString(p, StandardCharsets.UTF_8)).getAsJsonObject();
  }

  private static Set<String> lang() throws Exception {
    return json(TestData.pack().resolve("assets/mars/lang/en_us.json")).keySet();
  }

  private static List<Path> files(Path dir, String suffix) throws Exception {
    try (Stream<Path> s = Files.walk(dir)) {
      return s.filter(p -> p.toString().endsWith(suffix)).sorted().toList();
    }
  }

  private static void collectStrings(JsonElement e, List<String> out) {
    if (e.isJsonPrimitive() && e.getAsJsonPrimitive().isString()) {
      out.add(e.getAsString());
    } else if (e.isJsonObject()) {
      e.getAsJsonObject().entrySet().forEach(x -> collectStrings(x.getValue(), out));
    } else if (e.isJsonArray()) {
      e.getAsJsonArray().forEach(x -> collectStrings(x, out));
    }
  }

  private static String stem(Path p) {
    String n = p.getFileName().toString();
    return n.substring(0, n.length() - ".json".length());
  }

  private static void report(Set<String> missing) {
    assertTrue(missing.isEmpty(), "keys missing from en_us.json: " + missing);
  }

  @Test
  void everyKeyLiteralInTheMainCodeExists() throws Exception {
    Set<String> lang = lang();
    Set<String> missing = new TreeSet<>();
    for (Path p : files(Path.of("src/main/java"), ".java")) {
      if (p.toString().contains("/selftest/")) {
        continue; // check names, not translation keys
      }
      Matcher m = LITERAL.matcher(Files.readString(p, StandardCharsets.UTF_8));
      while (m.find()) {
        String k = m.group(1);
        if (!k.endsWith(".") && !lang.contains(k)) {
          missing.add(k + "  (" + p.getFileName() + ")");
        }
      }
    }
    report(missing);
  }

  @Test
  void everyKeyInThePackDataExists() throws Exception {
    Set<String> lang = lang();
    Set<String> missing = new TreeSet<>();
    for (Path p : files(TestData.data(""), ".json")) {
      List<String> strings = new ArrayList<>();
      collectStrings(json(p), strings);
      for (String s : strings) {
        // A journal text_key names a body stored as <key>.ms (grade band).
        if (KEY.matcher(s).matches() && !lang.contains(s) && !lang.contains(s + ".ms")) {
          missing.add(s + "  (" + p.getFileName() + ")");
        }
      }
    }
    report(missing);
  }

  @Test
  void journalTextKeysHaveABody() throws Exception {
    Set<String> lang = lang();
    Set<String> missing = new TreeSet<>();
    for (Path p : files(TestData.data("story/journal"), ".json")) {
      JsonObject o = json(p);
      String text = o.get("text_key").getAsString();
      if (!lang.contains(text + ".ms") && !lang.contains(text)) {
        missing.add(text + ".ms  (" + p.getFileName() + ")");
      }
    }
    report(missing);
  }

  @Test
  void everyGateHasAName_AProgressLine_AndAWaitingLineIfItHolds() throws Exception {
    Set<String> lang = lang();
    Set<String> missing = new TreeSet<>();
    for (Path p : files(TestData.data("gates"), ".json")) {
      JsonObject o = json(p);
      String id = stem(p);
      String name = o.has("name_key") ? o.get("name_key").getAsString() : "gate.mars." + id;
      need(lang, missing, name);
      need(lang, missing, "monitor.gate." + id + ".progress");
      if (o.has("hold")) {
        need(lang, missing, "monitor.gate." + id + ".waiting");
      }
    }
    report(missing);
  }

  @Test
  void everyMachineHasAName_AndAMarkerIfItRaisesPressure() throws Exception {
    Set<String> lang = lang();
    Set<String> missing = new TreeSet<>();
    for (Path p : files(TestData.data("machines"), ".json")) {
      String id = stem(p);
      need(lang, missing, "block.mars." + id);
      boolean raisesPressure =
          Files.readString(p, StandardCharsets.UTF_8).contains("\"mars:pressure\"");
      if (raisesPressure) {
        need(lang, missing, "monitor.event.placed." + id);
      }
    }
    report(missing);
  }

  @Test
  void everyBlockAndItemHasAName() throws Exception {
    Set<String> lang = lang();
    Set<String> missing = new TreeSet<>();
    for (Path p : files(TestData.pack().resolve("assets/mars/blockstates"), ".json")) {
      need(lang, missing, "block.mars." + stem(p));
    }
    for (Path p : files(TestData.pack().resolve("assets/mars/models/item"), ".json")) {
      String id = stem(p);
      if (!lang.contains("block.mars." + id) && !lang.contains("item.mars." + id)) {
        missing.add("block.mars." + id + " or item.mars." + id);
      }
    }
    report(missing);
  }

  @Test
  void machinesThatAreMadeUpOrGameSizedHaveATooltip() throws Exception {
    Set<String> lang = lang();
    Set<String> missing = new TreeSet<>();
    for (String id :
        List.of(
            "oxygen_maker",
            "atmospheric_processor",
            "super_greenhouse_emitter",
            "water_reclaimer",
            "ice_extractor")) {
      need(lang, missing, "block.mars." + id + ".tooltip");
    }
    report(missing);
  }

  private static void need(Set<String> lang, Set<String> missing, String key) {
    if (!lang.contains(key)) {
      missing.add(key);
    }
  }
}
