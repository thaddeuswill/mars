package mars.selftest;

import mars.core.Ids;
import mars.gates.GateTracker;
import mars.machines.MachineState;
import mars.machines.MarsMachines;
import mars.machines.PoweredMachineBlock;
import mars.planets.MachineStateSync;
import mars.planets.PlanetData;
import mars.planets.PlanetManager;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * Slice B, air we make (BACKLOG #49; docs/design/phase3-hydrosphere-and-drift.md section 3): the
 * real algae vat and carbon scrubber blocks on the planet, the Phase 3 begins lock, CO2 to O2 one
 * for one, the carbonate tally, and the Breathable gate latching after its 7-sol hold. Own batch:
 * it resets the shared planet.
 */
@GameTestHolder("mars")
@PrefixGameTestTemplate(false)
public final class OxygenGameTests {
  private OxygenGameTests() {}

  @GameTest(template = "empty", batch = "mars_oxygen", timeoutTicks = 100)
  public static void vatAndScrubberAreLockedUntilPhase3AndConvertGas(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetData data = PlanetManager.resetForSelfTest(level);
    var sim = data.sim();
    boolean lockedVat = !PlanetManager.placementAllowed(level, null, "mars:algae_vat");
    boolean lockedScr = !PlanetManager.placementAllowed(level, null, "mars:carbon_scrubber");
    boolean ok =
        SelfTest.check(
            "oxygen.locked_before_phase3",
            "a survival player cannot place the vat or the scrubber before Phase 3 begins",
            "vat locked=" + lockedVat + " scrubber locked=" + lockedScr,
            lockedVat && lockedScr);
    sim.gates().latch("mars:phase_3_begins", GateTracker.Cause.ADMIN, 0);
    boolean open =
        PlanetManager.placementAllowed(level, null, "mars:algae_vat")
            && PlanetManager.placementAllowed(level, null, "mars:carbon_scrubber");
    ok &=
        SelfTest.check(
            "oxygen.unlocked_by_phase3",
            "Phase 3 begins unlocks both machines",
            "open=" + open,
            open);
    BlockPos vat = new BlockPos(0, 1, 0);
    BlockPos scrubber = new BlockPos(3, 1, 0);
    BlockPos panelA = new BlockPos(6, 1, 6);
    BlockPos panelB = new BlockPos(9, 1, 6);
    h.setBlock(vat, MarsMachines.ALGAE_VAT.get());
    h.setBlock(scrubber, MarsMachines.CARBON_SCRUBBER.get());
    h.setBlock(panelA, MarsMachines.SOLAR_PANEL.get());
    h.setBlock(panelB, MarsMachines.SOLAR_PANEL.get());
    ok &=
        SelfTest.check(
            "oxygen.registered_with_planet",
            "the placed blocks are one vat and one scrubber on the planet's grid",
            "vats="
                + sim.machineCount("mars:algae_vat")
                + " scrubbers="
                + sim.machineCount("mars:carbon_scrubber"),
            sim.machineCount("mars:algae_vat") == 1
                && sim.machineCount("mars:carbon_scrubber") == 1);
    sim.model().set(Ids.PRESSURE, 40.0);
    sim.model().set(Ids.CO2_PARTIAL, 10.0);
    sim.model().set(Ids.O2_FRACTION, 5.0);
    sim.model().set(Ids.TEMPERATURE, 3.0);
    sim.model().set(Ids.HYDROSPHERE, 15.0);
    double o2Before = sim.model().current(Ids.O2_PARTIAL);
    sim.model().advanceSols(2);
    MachineStateSync.sync(level, sim);
    double co2 = sim.model().current(Ids.CO2_PARTIAL);
    double o2 = sim.model().current(Ids.O2_PARTIAL);
    var state = h.getBlockState(vat).getValue(PoweredMachineBlock.MACHINE_STATE);
    // Two sols: the vat moves 0.1 x 2 = 0.2 kPa to O2; the scrubber locks 0.2 x 2 = 0.4 kPa. The
    // warm planet also gives back its buried polar CO2 once (+0.5 kPa, the frost rule), so CO2
    // ends at 10 - 0.6 + 0.5.
    ok &=
        SelfTest.check(
            "oxygen.conversion_one_for_one",
            "2 sols: 0.2 kPa of CO2 became O2, 0.4 kPa went into rock (plus 0.5 buried CO2 released by the frost rule), the vat shows running",
            "co2="
                + co2
                + " o2 gain="
                + (o2 - o2Before)
                + " rock="
                + sim.carbonateLockedKpa()
                + " state="
                + state,
            Math.abs(co2 - 9.9) < 0.01
                && Math.abs((o2 - o2Before) - 0.2) < 0.01
                && Math.abs(sim.carbonateLockedKpa() - 0.4) < 0.01
                && state == MachineState.RUNNING);
    sim.model().set(Ids.HYDROSPHERE, 4.0);
    MachineStateSync.sync(level, sim);
    var dry = h.getBlockState(vat).getValue(PoweredMachineBlock.MACHINE_STATE);
    double co2Dry = sim.model().current(Ids.CO2_PARTIAL);
    sim.model().advanceSols(1);
    ok &=
        SelfTest.check(
            "oxygen.vat_needs_water",
            "below 10 percent hydrosphere the vat shows idle and moves no gas",
            "state=" + dry + " co2 change=" + (sim.model().current(Ids.CO2_PARTIAL) - co2Dry),
            dry == MachineState.IDLE
                && sim.model().current(Ids.CO2_PARTIAL) < co2Dry - 0.15 // the scrubber still runs
                && Math.abs(sim.carbonateLockedKpa() - 0.6) < 0.01);
    h.destroyBlock(vat);
    h.destroyBlock(scrubber);
    h.destroyBlock(panelA);
    h.destroyBlock(panelB);
    ok &=
        SelfTest.check(
            "oxygen.removed",
            "removing the blocks takes both machines off the planet",
            "vats=" + sim.machineCount("mars:algae_vat"),
            sim.machineCount("mars:algae_vat") == 0
                && sim.machineCount("mars:carbon_scrubber") == 0);
    if (ok) {
      h.succeed();
    } else {
      h.fail("vatAndScrubberAreLockedUntilPhase3AndConvertGas failed; see MARS-SELFTEST");
    }
  }

  @GameTest(template = "empty", batch = "mars_oxygen", timeoutTicks = 100)
  public static void breathableLatchesAfterItsHoldWithVatsRunning(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetData data = PlanetManager.resetForSelfTest(level);
    var sim = data.sim();
    sim.gates().latch("mars:phase_3_begins", GateTracker.Cause.ADMIN, 0);
    for (int i = 0; i < 3; i++) {
      h.setBlock(new BlockPos(i * 3, 1, 0), MarsMachines.SOLAR_PANEL.get());
    }
    BlockPos vat = new BlockPos(0, 1, 6);
    h.setBlock(vat, MarsMachines.ALGAE_VAT.get());
    sim.model().set(Ids.PRESSURE, 76.0);
    sim.model().set(Ids.TEMPERATURE, 6.0);
    sim.model().set(Ids.HYDROSPHERE, 31.0);
    sim.model().advanceSols(1.5); // the warm planet gives back its buried CO2 first (frost rule)
    sim.model().set(Ids.CO2_PARTIAL, 0.2);
    sim.model().set(Ids.O2_FRACTION, 21.4); // 16.26 kPa
    sim.model().advanceSols(3);
    boolean early = !sim.gates().isLatched("mars:breathable");
    boolean ok =
        SelfTest.check(
            "oxygen.breathable_holds_first",
            "with every Breathable condition met the gate has not latched after 3 of its 7 sols",
            "latched="
                + !early
                + " o2="
                + sim.model().current(Ids.O2_PARTIAL)
                + " co2="
                + sim.model().current(Ids.CO2_PARTIAL),
            early);
    sim.model().advanceSols(4.5);
    ok &=
        SelfTest.check(
            "oxygen.breathable_latches",
            "after the 7-sol hold with a vat running, Breathable latches and stays latched",
            "latched=" + sim.gates().isLatched("mars:breathable"),
            sim.gates().isLatched("mars:breathable"));
    h.destroyBlock(vat);
    if (ok) {
      h.succeed();
    } else {
      h.fail("breathableLatchesAfterItsHoldWithVatsRunning failed; see MARS-SELFTEST");
    }
  }
}
