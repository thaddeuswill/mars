package mars.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Reader;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;
import mars.TestData;
import mars.TestSims;
import mars.core.Ids;
import mars.gates.GateDef;
import mars.gates.GateTracker;
import mars.index.sim.Pace;
import org.junit.jupiter.api.Test;

/** The resupply request rule and the "tech, not progress" line of the ruling (pure). */
class ResupplyTest {
  @Test
  void theRuleIsReadFromDataThreeSols() throws Exception {
    try (Reader r = Files.newBufferedReader(TestData.data("terrain/resupply_request.json"))) {
      assertEquals(3.0, Resupply.parse(r, "x").passSols());
    }
    assertThrows(IllegalArgumentException.class, () -> new Resupply(0.0));
  }

  @Test
  void aRefillIsHalfRoundedUpAndNeverBelowOneOfAnyLine() {
    var drop =
        List.of(
            new StarterChest.Entry("mars:machine_core", 6),
            new StarterChest.Entry("minecraft:coal", 16),
            new StarterChest.Entry("mars:filter_cartridge", 3),
            new StarterChest.Entry("mars:activation_cell", 1));
    assertEquals(
        List.of(
            new StarterChest.Entry("mars:machine_core", 3),
            new StarterChest.Entry("minecraft:coal", 8),
            new StarterChest.Entry("mars:filter_cartridge", 2),
            new StarterChest.Entry("mars:activation_cell", 1)),
        Resupply.refill(drop));
  }

  @Test
  void everyRealGateDropHasAtLeastOneKeyPartInItsRefill() throws Exception {
    for (GateDef g : TestSims.gates()) {
      if (g.cache().isEmpty()) {
        continue;
      }
      var refill =
          Resupply.refill(
              g.cache().stream().map(c -> new StarterChest.Entry(c.item(), c.count())).toList());
      assertEquals(g.cache().size(), refill.size(), g.id());
      assertTrue(refill.stream().allMatch(e -> e.count() >= 1), g.id());
    }
  }

  /** Tech, not progress: no drop line is something that is itself Index progress. */
  @Test
  void dropsCarryPartsAndFuelNeverMaterialsOrMachinesThatMoveTheIndex() throws Exception {
    Set<String> tech =
        Set.of(
            "mars:heater_coil",
            "mars:filter_cartridge",
            "mars:pump_bearing",
            "mars:sorbent_bed",
            "mars:machine_core",
            "mars:resin_pack",
            "mars:signal_board",
            "mars:activation_cell",
            "minecraft:furnace",
            "minecraft:bucket",
            "minecraft:coal",
            "minecraft:glass",
            "minecraft:crafting_table");
    for (GateDef g : TestSims.gates()) {
      for (GateDef.CacheItem c : g.cache()) {
        assertTrue(tech.contains(c.item()), g.id() + " drops " + c.item() + ": not a tech item");
        assertFalse(c.item().contains("regolith") || c.item().contains("ice"), c.item());
      }
    }
    assertFalse(tech.stream().anyMatch(i -> i.contains("water_bucket")));
  }

  /**
   * Latching every gate (which is what hands out the drops) and running the whole request ledger
   * changes no Index value: a drop has no path into the model.
   */
  @Test
  void aReleaseTouchesNoIndexParameter() throws Exception {
    var sim = TestSims.sim(Pace.STANDARD);
    var params =
        List.of(
            Ids.PRESSURE,
            Ids.TEMPERATURE,
            Ids.HYDROSPHERE,
            Ids.O2_FRACTION,
            Ids.CO2_PARTIAL,
            Ids.BIOMASS);
    double[] before = params.stream().mapToDouble(sim.model()::current).toArray();
    for (GateDef g : sim.gates().defs()) {
      sim.gates().latch(g.id(), GateTracker.Cause.ADMIN, 0);
    }
    CacheLedger ledger = new CacheLedger();
    for (GateDef g : sim.gates().defs()) {
      ledger.claim(g.id(), java.util.UUID.randomUUID());
      Resupply.refill(
          g.cache().stream().map(c -> new StarterChest.Entry(c.item(), c.count())).toList());
    }
    ledger.tryRequest(java.util.UUID.randomUUID(), sim.model().sols(), 3.0);
    double[] after = params.stream().mapToDouble(sim.model()::current).toArray();
    assertEquals(java.util.Arrays.toString(before), java.util.Arrays.toString(after));
  }

  @Test
  void theResupplyClassesKnowNothingOfTheIndex() throws Exception {
    for (String f : List.of("Resupply", "CacheLedger", "StarterChest")) {
      String src =
          Files.readString(java.nio.file.Path.of("src/main/java/mars/session/" + f + ".java"));
      assertFalse(src.contains("mars.index"), f + " must not import the Index");
      assertFalse(src.contains("net.minecraft"), f + " is pure Java");
    }
  }
}
