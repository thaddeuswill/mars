package mars.machines;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import mars.TestData;
import mars.TestSims;
import mars.index.sim.Pace;
import mars.index.sim.PlanetSim;
import org.junit.jupiter.api.Test;

/** Atmospheric processor and super-greenhouse emitter: powered loads unlocked by Phase 2 begins. */
class PhaseTwoMachinesTest {
  private static final String[] NAMES = {"atmospheric_processor", "super_greenhouse_emitter"};

  private static JsonObject json(Path p) throws Exception {
    return JsonParser.parseString(Files.readString(p, StandardCharsets.UTF_8)).getAsJsonObject();
  }

  @Test
  void drawAndRateComeFromDataAndAreMarkedInvented() throws Exception {
    for (String n : NAMES) {
      var def = TestSims.defs().get("mars:" + n);
      assertEquals(3.0, def.power().drawPerSol(), 1e-12, n);
      // The processor also adds CO2 until Phase 3 begins (Slice B, rule A).
      assertEquals(n.equals("atmospheric_processor") ? 2 : 1, def.contributions().size(), n);
      String source = json(TestData.data("machines/" + n + ".json")).get("source").getAsString();
      assertTrue(source.contains("INVENTED"), n);
      PlanetSim s = TestSims.sim(Pace.STANDARD);
      s.addMachine("mars:" + n, 1L);
      assertEquals(3.0, s.power().demandPerSol(), 1e-12, n);
    }
  }

  @Test
  void tooltipsAreTheRequiredLabels() throws Exception {
    JsonObject l = json(TestData.pack().resolve("assets/mars/lang/en_us.json"));
    assertEquals(
        "Game-sized idea based on MOXIE and Sabatier",
        l.get("block.mars.atmospheric_processor.tooltip").getAsString());
    assertEquals(
        "Game invention, not real",
        l.get("block.mars.super_greenhouse_emitter.tooltip").getAsString());
    for (String n : NAMES) {
      assertTrue(l.has("block.mars." + n), n);
      assertTrue(l.has("monitor.event.placed." + n), n);
    }
  }

  @Test
  void lootRecipeAndBlockstateExist() throws Exception {
    for (String n : NAMES) {
      assertTrue(
          Files.readString(TestData.data("loot_table/blocks/" + n + ".json"))
              .contains("mars:" + n));
      JsonObject r = json(TestData.data("recipe/" + n + ".json"));
      assertEquals("minecraft:crafting_shaped", r.get("type").getAsString());
      assertEquals("mars:" + n, r.getAsJsonObject("result").get("id").getAsString());
      for (var e : r.getAsJsonObject("key").entrySet()) {
        String item = e.getValue().getAsJsonObject().get("item").getAsString();
        // Vanilla ingredients, plus the gate input parts, which are themselves vanilla-only
        // crafts (heater coil, filter cartridge, pump bearing; see PartsAndCachesTest).
        assertTrue(
            item.startsWith("minecraft:")
                || item.equals("mars:heater_coil")
                || item.equals("mars:filter_cartridge")
                || item.equals("mars:machine_core")
                || item.equals("mars:pump_bearing")
                || item.equals("mars:sorbent_bed"),
            n + ": vanilla-first recipe");
      }
    }
  }

  @Test
  void phaseTwoBeginsUnlocksBoth() throws Exception {
    var defs = TestSims.defs();
    var gates = TestSims.sim(Pace.STANDARD).gates();
    for (String n : NAMES) {
      assertTrue(!gates.isUnlocked("mars:" + n), n + " is locked at the start");
    }
    gates.latch("mars:phase_2_begins", mars.gates.GateTracker.Cause.ADMIN, 0);
    for (String n : NAMES) {
      assertTrue(gates.isUnlocked("mars:" + n), n + " unlocks when Phase 2 begins latches");
    }
    assertTrue(defs != null);
  }
}
