package mars.hazards.dust;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mars.TestData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * The dust tuning file parses and matches balance sheet §2a number for number. If the sheet
 * changes, this test fails until the data file is updated (or the other way round).
 */
class PanelDustTuningTest {
  private static PanelDustTuning tuning;
  private static JsonObject raw;
  private static String sheet;

  @BeforeAll
  static void load() throws Exception {
    Path f = TestData.data("hazards/panel_dust.json");
    try (Reader r = Files.newBufferedReader(f, StandardCharsets.UTF_8)) {
      tuning = PanelDustTuning.parse(r, "panel_dust.json");
    }
    try (Reader r = Files.newBufferedReader(f, StandardCharsets.UTF_8)) {
      raw = JsonParser.parseReader(r).getAsJsonObject();
    }
    Path sheetFile = TestData.pack().resolve("../docs/design/balance-sheet.md").normalize();
    String all = Files.readString(sheetFile, StandardCharsets.UTF_8);
    int a = all.indexOf("## 2a.");
    int b = all.indexOf("\n## 3.", a);
    assertTrue(a >= 0 && b > a, "balance sheet §2a not found in " + sheetFile);
    sheet = all.substring(a, b);
  }

  private static String find(String regex) {
    Matcher m = Pattern.compile(regex).matcher(sheet);
    assertTrue(m.find(), "balance sheet §2a has no match for: " + regex);
    return m.group(1);
  }

  @Test
  void stageIntervalMatchesSheet() {
    double sheetValue =
        Double.parseDouble(find("Stage interval, Phase 1 \\| (\\d+(?:\\.\\d+)?) sols per stage"));
    System.out.println(
        "stage interval: sheet " + sheetValue + " sols, data " + tuning.stageIntervalSols());
    assertEquals(sheetValue, tuning.stageIntervalSols());
    assertTrue(sheet.contains("about 15 times faster than real"), "sheet label changed");
    String note = raw.getAsJsonObject("stage_interval_sols").get("note").getAsString();
    assertTrue(
        note.contains("15 times faster than real"),
        "data note must carry the label, read: " + note);
  }

  @Test
  void outputByStageMatchesSheet() {
    String row = find("(\\| Output by stage \\|[^\\n]*)");
    Matcher m = Pattern.compile("(\\d): (\\d+) %").matcher(row);
    StringBuilder read = new StringBuilder();
    int n = 0;
    while (m.find()) {
      int stage = Integer.parseInt(m.group(1));
      double pct = Double.parseDouble(m.group(2));
      read.append(stage).append('=').append(pct).append(' ');
      assertEquals(pct, tuning.outputByStagePercent()[stage], "stage " + stage);
      n++;
    }
    System.out.println(
        "output by stage, sheet: "
            + read
            + "| data: "
            + java.util.Arrays.toString(tuning.outputByStagePercent()));
    assertEquals(5, n);
  }

  @Test
  void partlyCleanedRecoversHalfAsTheSheetSays() {
    assertTrue(sheet.contains("Recovers **half the lost output**"), "sheet wording changed");
    Map<Integer, Integer> sheetExamples = Map.of(4, 80, 3, 85, 2, 91);
    for (Map.Entry<Integer, Integer> e : sheetExamples.entrySet()) {
      double got = tuning.outputFraction(e.getKey(), true) * 100;
      System.out.printf(
          "partly cleaned from stage %d: sheet %d %%, data gives %.1f %%%n",
          e.getKey(), e.getValue(), got);
      assertEquals(e.getValue(), got, 0.5);
      assertTrue(
          sheet.contains("to " + e.getValue() + " %"),
          "sheet example " + e.getValue() + " % missing");
    }
  }

  @Test
  void dustDevilRainStormAndLocalMatchSheet() {
    double devil =
        Double.parseDouble(find("Dust devil chance \\| \\*\\*(\\d+(?:\\.\\d+)?) % per sol"));
    int radius = Integer.parseInt(find("×2 dust rate within (\\d+) blocks"));
    int rainStage = Integer.parseInt(find("go to stage (\\d+) or partly cleaned"));
    int stormPlus = Integer.parseInt(find("\\+(\\d+) stage at storm end"));
    int stormHeavy = Integer.parseInt(find("\\+(\\d+) if storm cap ≥"));
    double stormCap = Double.parseDouble(find("if storm cap ≥ (\\d+(?:\\.\\d+)?)"));
    double cut =
        Double.parseDouble(find("stage factor × \\(1 − (\\d+(?:\\.\\d+)?) × storm cap\\)"));
    System.out.printf(
        "sheet: devil %.0f %%/sol, radius %d, rain stage %d, storm +%d (+%d if cap >= %.1f), cut %.1f%n"
            + "data:  devil %.0f %%/sol, radius %d (x%.0f), rain stage %d, storm +%d (+%d if cap >= %.1f), cut %.1f%n",
        devil,
        radius,
        rainStage,
        stormPlus,
        stormHeavy,
        stormCap,
        cut,
        tuning.dustDevilChancePerSol() * 100,
        tuning.localRadiusBlocks(),
        tuning.localRateMultiplier(),
        tuning.rainTargetStage(),
        tuning.stormEndStages(),
        tuning.stormEndStagesHeavy(),
        tuning.stormCapForExtra(),
        tuning.stormSunlightCutPerCap());
    assertEquals(devil / 100.0, tuning.dustDevilChancePerSol(), 1e-12);
    assertEquals(radius, tuning.localRadiusBlocks());
    assertEquals(2.0, tuning.localRateMultiplier());
    assertEquals(rainStage, tuning.rainTargetStage());
    assertEquals(stormPlus, tuning.stormEndStages());
    assertEquals(stormHeavy, tuning.stormEndStagesHeavy());
    assertEquals(stormCap, tuning.stormCapForExtra());
    assertEquals(cut, tuning.stormSunlightCutPerCap());
    // Worst case the sheet quotes: stage 4 in a Phase 1 storm (cap 1.0) = 24 %.
    assertEquals(0.24, tuning.outputInStorm(4, false, 1.0), 1e-9);
  }

  @Test
  void laterPhasesAreSlowerAsTheSheetSays() {
    // Sheet: Phase 2 3.75 sols (cap 0.8), Phase 3 6 (cap 0.5), Equilibrium 12 (cap 0.25); classroom
    // 0.75.
    assertEquals(3.75, tuning.intervalSols(0.8, 1.0), 1e-9);
    assertEquals(6.0, tuning.intervalSols(0.5, 1.0), 1e-9);
    assertEquals(12.0, tuning.intervalSols(0.25, 1.0), 1e-9);
    assertEquals(0.75, tuning.intervalSols(1.0, 4.0), 1e-9);
    assertTrue(sheet.contains("Phase 2 3.75 sols, Phase 3 6, Equilibrium 12"));
  }

  @Test
  void everyTuningBlockCarriesATag() {
    for (Map.Entry<String, JsonElement> e : raw.entrySet()) {
      if (e.getValue().isJsonObject()) {
        JsonObject o = e.getValue().getAsJsonObject();
        assertTrue(o.has("tag"), e.getKey() + " has no tag");
        System.out.println(e.getKey() + ": tag " + o.get("tag").getAsString());
      }
    }
  }
}
