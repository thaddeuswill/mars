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
    assertTrue(c.groupAllowed(3) && c.groupAllowed(5), "teacher range 3 to 5 is allowed");
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
    assertThrows(IllegalArgumentException.class, () -> c.pace(PaceConfig.Mode.STANDARD, 0));
    // The hand-built presets used by older tests agree with the file.
    assertEquals(Pace.classroom(4), room);
    assertEquals(Pace.STANDARD, std);
  }

  @Test
  void thresholdsAreIdenticalInBothPresetsAndAllGroupSizes() throws Exception {
    PaceConfig c = config();
    var reference = TestSims.gates();
    for (PaceConfig.Mode mode : PaceConfig.Mode.values()) {
      for (int n = 1; n <= 5; n++) {
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
    for (int n = 1; n <= 5; n++) {
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
}
