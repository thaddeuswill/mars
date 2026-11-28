package mars.index.sim;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mars.TestData;
import mars.TestSims;
import mars.gates.Condition;
import mars.gates.GateDef;
import org.junit.jupiter.api.Test;

/** Slice 9: the pace presets (data/mars/pace.json) against the balance sheet and the rules. */
class PaceConfigTest {
  private static final String SHEET = "../docs/design/balance-sheet.md";

  private static PaceConfig config() throws Exception {
    String file = Files.readString(TestData.data("pace.json"));
    return PaceConfig.parse(JsonParser.parseString(file).getAsJsonObject(), "pace.json");
  }

  private static String sheet() throws Exception {
    return Files.readString(TestData.pack().resolve(SHEET).normalize());
  }

  @Test
  void fileMatchesBalanceSheetSection5() throws Exception {
    Matcher rate =
        Pattern.compile("\\| `pace.rateMultiplier` \\|[^|]*\\| ([\\d.]+) \\| ([\\d.]+) \\|")
            .matcher(sheet());
    assertTrue(rate.find(), "balance sheet 5.1 rate row not found");
    PaceConfig c = config();
    assertEquals(Double.parseDouble(rate.group(1)), c.standard().rateMultiplier());
    assertEquals(Double.parseDouble(rate.group(2)), c.classroom().rateMultiplier());
    // A sol is 20 simulation minutes (TEAM.md decision log): 20 min x 60 s x 20 ticks.
    assertEquals(20 * 60 * 20, c.solTicks());
    assertEquals(100, c.stepTicks());
    assertEquals(1, c.standard().groupSize(), "standard default is solo");
    assertEquals(1, c.standard().groupMin());
    assertEquals(5, c.standard().groupMax());
    assertEquals(3, c.classroom().groupMin());
    assertEquals(5, c.classroom().groupMax());
    assertEquals(4, c.classroom().groupSize(), "classroom default is 4");
  }

  @Test
  void holdsInTheGateFilesMatchBalanceSheetSection4() throws Exception {
    String sheet = sheet();
    int checked = 0;
    for (GateDef g : TestSims.gates()) {
      if (g.hold() == null) {
        continue;
      }
      String name = g.id().equals("mars:dome_stable") ? "Dome stable" : null;
      if (name == null) {
        continue; // later gates have no file yet
      }
      Matcher m =
          Pattern.compile("\\| " + name + " \\| (\\d+) sols? [^|]*\\| (\\d+) sols?").matcher(sheet);
      assertTrue(m.find(), name + " row not found");
      assertEquals(Double.parseDouble(m.group(1)), g.hold().standardSols());
      assertEquals(Double.parseDouble(m.group(2)), g.hold().classroomSols());
      checked++;
    }
    assertTrue(checked >= 1);
  }

  @Test
  void presetsGivePaceWithTheRightFlags() throws Exception {
    PaceConfig c = config();
    Pace std = c.pace(PaceConfig.Mode.STANDARD, null);
    Pace room = c.pace(PaceConfig.Mode.CLASSROOM, null);
    assertFalse(std.classroom());
    assertTrue(std.creativeGateBypass(), "creative bypass is on in standard");
    assertTrue(room.classroom());
    assertFalse(room.creativeGateBypass(), "creative bypass is off in classroom");
    assertEquals(4, room.groupSize());
    assertEquals(3, c.pace(PaceConfig.Mode.CLASSROOM, 3).groupSize());
    assertEquals(5, c.pace(PaceConfig.Mode.STANDARD, 5).groupSize());
    assertThrows(IllegalArgumentException.class, () -> c.pace(PaceConfig.Mode.CLASSROOM, 6));
    for (int n : new int[] {0, 1, 2}) {
      var e =
          assertThrows(IllegalArgumentException.class, () -> c.pace(PaceConfig.Mode.CLASSROOM, n));
      assertTrue(e.getMessage().contains("3 to 5"), e.getMessage());
    }
    assertEquals(1, c.pace(PaceConfig.Mode.STANDARD, 1).groupSize());
    assertEquals(2, c.pace(PaceConfig.Mode.STANDARD, 2).groupSize());
    assertThrows(IllegalArgumentException.class, () -> c.pace(PaceConfig.Mode.STANDARD, 0));
    // The hand-built presets used by older tests agree with the file.
    // (classroomShown is the new visibility flag, off with the shipped file; compare the numbers)
    assertEquals(
        Pace.classroom(4),
        new Pace(
            room.rateMultiplier(),
            room.groupSize(),
            room.solTicks(),
            room.stepTicks(),
            room.classroom(),
            room.creativeGateBypass()));
    assertEquals(Pace.STANDARD, std);
  }

  @Test
  void thresholdsAreIdenticalInBothPresetsAndAllGroupSizes() throws Exception {
    PaceConfig c = config();
    var reference = TestSims.gates();
    for (PaceConfig.Mode mode : PaceConfig.Mode.values()) {
      for (int n = mode == PaceConfig.Mode.CLASSROOM ? 3 : 1; n <= 5; n++) {
        Pace pace = c.pace(mode, n);
        PlanetSim s = TestSims.sim(pace);
        for (GateDef g : reference) {
          GateDef other =
              s.gates().defs().stream()
                  .filter(x -> x.id().equals(g.id()))
                  .findFirst()
                  .orElseThrow();
          assertEquals(g.condition(), other.condition(), mode + " " + n + " " + g.id());
        }
        // The pressure gate trips at 5 kPa exactly, in every mode and group size.
        PlanetSim t = TestSims.sim(pace);
        t.model().set(mars.core.Ids.PRESSURE, 4.99);
        t.model().advanceSols(0.01);
        assertFalse(t.gates().isLatched("mars:phase_2_begins"), mode + " " + n + " at 4.99");
        Condition.Compare p2 =
            (Condition.Compare)
                t.gates().defs().stream()
                    .filter(x -> x.id().equals("mars:phase_2_begins"))
                    .findFirst()
                    .orElseThrow()
                    .condition();
        assertEquals(5.0, p2.value());
      }
    }
  }

  @Test
  void ratesScaleByMultiplierAndGroupAndHoldsByMode() throws Exception {
    PaceConfig c = config();
    double std = gain(c.pace(PaceConfig.Mode.STANDARD, 1));
    assertTrue(std > 0);
    for (int n = 3; n <= 5; n++) {
      Pace room = c.pace(PaceConfig.Mode.CLASSROOM, n);
      double expected = std * 4.0 * room.groupRateScale();
      assertEquals(expected, gain(room), expected * 0.01, "classroom group " + n);
    }
    var dome =
        TestSims.gates().stream().filter(g -> g.id().equals("mars:dome_stable")).findFirst().get();
    assertEquals(3.0, TestSims.sim(Pace.STANDARD).gates().holdSols(dome));
    assertEquals(1.0, TestSims.sim(c.pace(PaceConfig.Mode.CLASSROOM, 4)).gates().holdSols(dome));
  }

  private static double gain(Pace pace) throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(pace), 3);
    s.addMachine(TestSims.OUTGASSER, 1L);
    double p0 = s.model().current(mars.core.Ids.PRESSURE);
    s.model().advanceSols(0.05);
    return s.model().current(mars.core.Ids.PRESSURE) - p0;
  }

  @Test
  void badFilesAreRefused() {
    JsonObject o = JsonParser.parseString("{\"schema\":2}").getAsJsonObject();
    assertThrows(IllegalArgumentException.class, () -> PaceConfig.parse(o, "x.json"));
    JsonObject noMode =
        JsonParser.parseString(
                "{\"schema\":1,\"sol_ticks\":1,\"step_ticks\":1,"
                    + "\"group_size_range\":{\"min\":1,\"max\":5}}")
            .getAsJsonObject();
    assertThrows(IllegalArgumentException.class, () -> PaceConfig.parse(noMode, "x.json"));
    // A preset with no range, an inverted range, or a default outside its own range is refused.
    String good = "{\"rate_multiplier\":1,\"group_size\":%d,%s\"creative_gate_bypass\":true}";
    String ok = "\"group_size_range\":{\"min\":3,\"max\":5},";
    String[][] bad = {
      {String.format(good, 4, ""), "no range"},
      {String.format(good, 4, "\"group_size_range\":{\"min\":5,\"max\":3},"), "inverted"},
      {String.format(good, 2, ok), "default outside range"},
      {String.format(good, 4, "\"group_size_range\":{\"min\":0,\"max\":5},"), "min below 1"},
      {String.format(good, 4, "\"group_size_range\":{\"min\":3},"), "no max"},
    };
    for (String[] b : bad) {
      String text =
          "{\"schema\":1,\"sol_ticks\":1,\"step_ticks\":1,\"standard\":"
              + String.format(good, 4, ok)
              + ",\"classroom\":"
              + b[0]
              + "}";
      JsonObject o2 = JsonParser.parseString(text).getAsJsonObject();
      assertThrows(IllegalArgumentException.class, () -> PaceConfig.parse(o2, "x.json"), b[1]);
    }
  }

  @Test
  void packFileHasNoThresholdKeys() throws Exception {
    String text = Files.readString(TestData.data("pace.json"));
    JsonObject o = JsonParser.parseString(text).getAsJsonObject();
    for (String key : o.keySet()) {
      assertFalse(key.contains("threshold") || key.contains("kpa"), key);
    }
    for (String mode : new String[] {"standard", "classroom"}) {
      for (String key : o.getAsJsonObject(mode).keySet()) {
        assertFalse(key.contains("threshold") || key.contains("kpa"), mode + "." + key);
      }
    }
  }

  /** Owner decision 2026-11-28: classroom pace is off by default and the flag restores it. */
  @Test
  void classroomPaceIsOffByDefaultAndTheFlagRestoresIt() throws Exception {
    PaceConfig shipped = config();
    assertFalse(shipped.classroomOffered(), "pace.json ships with classroom_enabled false");
    assertEquals("Classroom pace is turned off.", shipped.refusal(PaceConfig.Mode.CLASSROOM));
    assertEquals(null, shipped.refusal(PaceConfig.Mode.STANDARD), "standard is never refused");
    // A missing flag also means off.
    JsonObject o =
        JsonParser.parseString(Files.readString(TestData.data("pace.json"))).getAsJsonObject();
    o.remove("classroom_enabled");
    assertFalse(PaceConfig.parse(o, "x.json").classroomOffered());
    // Flag on: the old behaviour is back.
    o.addProperty("classroom_enabled", true);
    PaceConfig on = PaceConfig.parse(o, "x.json");
    assertTrue(on.classroomOffered());
    assertEquals(null, on.refusal(PaceConfig.Mode.CLASSROOM));
    Pace room = on.pace(PaceConfig.Mode.CLASSROOM, 3);
    assertTrue(room.classroom());
    assertTrue(room.classroomShown());
    assertEquals(4.0, room.rateMultiplier());
  }

  /** Standard pace and its numbers are the same with the flag off or on; the tag is hidden off. */
  @Test
  void standardPaceIsUnchangedByTheFlagAndTheTagHidesWhileOff() throws Exception {
    JsonObject o =
        JsonParser.parseString(Files.readString(TestData.data("pace.json"))).getAsJsonObject();
    Pace off = config().pace(PaceConfig.Mode.STANDARD, null);
    o.addProperty("classroom_enabled", true);
    Pace on = PaceConfig.parse(o, "x.json").pace(PaceConfig.Mode.STANDARD, null);
    assertEquals(1.0, off.rateMultiplier());
    assertEquals(on.rateMultiplier(), off.rateMultiplier());
    assertEquals(on.groupSize(), off.groupSize());
    assertEquals(on.creativeGateBypass(), off.creativeGateBypass());
    assertFalse(off.classroom());
    // A world saved in classroom mode shows no "Classroom pace" tag while the flag is off.
    Pace savedRoomOff = config().pace(PaceConfig.Mode.CLASSROOM, 4);
    assertFalse(savedRoomOff.classroomShown());
    assertTrue(
        mars.monitor.view.MonitorView.of(TestSims.sim(savedRoomOff)).rows().stream()
            .noneMatch(r -> r.labelKey().equals("monitor.row.pace")));
  }
}
