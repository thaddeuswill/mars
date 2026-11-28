package mars.selftest;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import mars.core.Ids;
import mars.gates.GateTracker;
import mars.machines.MachineState;
import mars.machines.MarsMachines;
import mars.machines.PoweredMachineBlock;
import mars.planets.CacheData;
import mars.planets.GateCaches;
import mars.planets.MachineStateSync;
import mars.planets.PlanetData;
import mars.planets.PlanetManager;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * Slice A, water (BACKLOG #49; docs/design/phase3-hydrosphere-and-drift.md): the real ice extractor
 * block on the planet, the Thaw lock, the Hydrosphere gate, and the gate supply caches
 * (docs/design/ gate-input-items.md). Own batch: it resets the shared planet.
 */
@GameTestHolder("mars")
@PrefixGameTestTemplate(false)
public final class WaterGameTests {
  private WaterGameTests() {}

  private static final String THAW = "mars:thaw";

  private static int count(FakePlayer p, net.minecraft.world.item.Item item) {
    return p.getInventory().countItem(item);
  }

  private static FakePlayer player(ServerLevel level, String last, String name) {
    return FakePlayerFactory.get(
        level, new GameProfile(UUID.fromString("6d617273-0000-4000-8000-0000000000" + last), name));
  }

  @GameTest(template = "empty", batch = "mars_water", timeoutTicks = 100)
  public static void iceExtractorRunsWarmAndPoweredAndIsLockedUntilTheThaw(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetData data = PlanetManager.resetForSelfTest(level);
    var sim = data.sim();
    BlockPos extractor = new BlockPos(0, 1, 0);
    BlockPos panel = new BlockPos(6, 1, 6);
    boolean locked = !PlanetManager.placementAllowed(level, null, "mars:ice_extractor");
    boolean ok =
        SelfTest.check(
            "water.locked_before_thaw",
            "a survival player cannot place the ice extractor before the Thaw",
            "locked=" + locked,
            locked);
    sim.gates().latch(THAW, GateTracker.Cause.ADMIN, 0);
    ok &=
        SelfTest.check(
            "water.unlocked_by_thaw",
            "the Thaw latch unlocks the ice extractor",
            "allowed=" + PlanetManager.placementAllowed(level, null, "mars:ice_extractor"),
            PlanetManager.placementAllowed(level, null, "mars:ice_extractor"));
    h.setBlock(extractor, MarsMachines.ICE_EXTRACTOR.get());
    h.setBlock(panel, MarsMachines.SOLAR_PANEL.get());
    ok &=
        SelfTest.check(
            "water.registered_with_planet",
            "the placed block is one ice extractor on the planet's grid",
            "count=" + sim.machineCount("mars:ice_extractor"),
            sim.machineCount("mars:ice_extractor") == 1);
    sim.model().set(Ids.TEMPERATURE, -10.0);
    sim.model().advanceSols(2);
    double warm = sim.model().current(Ids.HYDROSPHERE);
    MachineStateSync.sync(level, sim);
    var running = h.getBlockState(extractor).getValue(PoweredMachineBlock.MACHINE_STATE);
    ok &=
        SelfTest.check(
            "water.rises_when_warm_and_powered",
            "two sols at -10 C with a clean panel: about +0.6 percent, and the block shows running",
            "hydrosphere=" + warm + " state=" + running,
            Math.abs(warm - 0.6) < 0.02 && running == MachineState.RUNNING);
    sim.model().set(Ids.TEMPERATURE, -30.0);
    MachineStateSync.sync(level, sim);
    var idle = h.getBlockState(extractor).getValue(PoweredMachineBlock.MACHINE_STATE);
    double before = sim.model().current(Ids.HYDROSPHERE);
    sim.model().set(Ids.TEMPERATURE, -30.0);
    sim.model().advanceSols(1);
    double after = sim.model().current(Ids.HYDROSPHERE);
    ok &=
        SelfTest.check(
            "water.too_cold_does_nothing",
            "at -30 C the extractor shows idle and the value only falls (loss term)",
            "state=" + idle + " before=" + before + " after=" + after,
            idle == MachineState.IDLE && after < before);
    sim.model().set(Ids.HYDROSPHERE, 9.95);
    sim.model().set(Ids.TEMPERATURE, -10.0);
    sim.model().advanceSols(0.5);
    ok &=
        SelfTest.check(
            "water.hydrosphere_gate_latches",
            "the Hydrosphere gate latches at 10 percent with no hold",
            "hydrosphere=" + sim.model().current(Ids.HYDROSPHERE),
            sim.gates().isLatched("mars:hydrosphere"));
    h.destroyBlock(panel);
    h.destroyBlock(extractor);
    ok &=
        SelfTest.check(
            "water.removed",
            "removing the block takes the extractor off the planet",
            "count=" + sim.machineCount("mars:ice_extractor"),
            sim.machineCount("mars:ice_extractor") == 0);
    if (ok) {
      h.succeed();
    } else {
      h.fail("iceExtractorRunsWarmAndPoweredAndIsLockedUntilTheThaw failed; see MARS-SELFTEST");
    }
  }

  @GameTest(template = "empty", batch = "mars_water", timeoutTicks = 100)
  public static void gateCachesArriveOncePerPlayerPerPlanet(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetData data = PlanetManager.resetForSelfTest(level);
    CacheData.resetForSelfTest(level);
    var sim = data.sim();
    var coil = MarsMachines.PARTS.get(0).get();
    var cartridge = MarsMachines.PARTS.get(1).get();
    var bearing = MarsMachines.PARTS.get(2).get();
    FakePlayer first = player(level, "11", "[CacheOne]");
    FakePlayer second = player(level, "12", "[CacheTwo]");
    first.getInventory().clearContent();
    second.getInventory().clearContent();

    boolean ok =
        SelfTest.check(
            "cache.nothing_before_a_gate",
            "no gate has latched, so nothing is delivered",
            "delivered=" + GateCaches.deliver(first) + " coils=" + count(first, coil),
            count(first, coil) == 0);
    sim.gates().latch("mars:dome_stable", GateTracker.Cause.ADMIN, 0);
    int d1 = GateCaches.deliver(first);
    int again = GateCaches.deliver(first);
    ok &=
        SelfTest.check(
            "cache.dome_stable_four_coils_once",
            "Dome stable latched: 4 heater coils arrive once (a second arrival gives none)",
            "d1=" + d1 + " again=" + again + " coils=" + count(first, coil),
            d1 == 1 && again == 0 && count(first, coil) == 4);
    first.getInventory().clearContent();
    ok &=
        SelfTest.check(
            "cache.death_does_not_repeat",
            "after losing the coils (death) no second cache is given",
            "delivered=" + GateCaches.deliver(first) + " coils=" + count(first, coil),
            count(first, coil) == 0);
    sim.gates().latch("mars:phase_2_begins", GateTracker.Cause.ADMIN, 0);
    sim.gates().latch(THAW, GateTracker.Cause.ADMIN, 0);
    int d2 = GateCaches.deliver(first);
    ok &=
        SelfTest.check(
            "cache.later_gates_deliver_theirs",
            "Phase 2 begins gives 3 filter cartridges and the Thaw 2 pump bearings, no more coils",
            "d="
                + d2
                + " cartridges="
                + count(first, cartridge)
                + " bearings="
                + count(first, bearing)
                + " coils="
                + count(first, coil),
            d2 == 2
                && count(first, cartridge) == 3
                && count(first, bearing) == 2
                && count(first, coil) == 0);
    int d3 = GateCaches.deliver(second);
    ok &=
        SelfTest.check(
            "cache.late_player_gets_their_own",
            "a player who was not there when the gates latched gets all three caches on arrival",
            "d="
                + d3
                + " coils="
                + count(second, coil)
                + " cartridges="
                + count(second, cartridge)
                + " bearings="
                + count(second, bearing),
            d3 == 3
                && count(second, coil) == 4
                && count(second, cartridge) == 3
                && count(second, bearing) == 2);
    CompoundTag tag = CacheData.get(level).save(new CompoundTag(), level.registryAccess());
    ok &=
        SelfTest.check(
            "cache.claims_saved",
            "the saved ledger lists both players under Dome stable",
            "nbt=" + tag,
            tag.getCompound("claims").getList("mars:dome_stable", 11).size() == 2);
    CacheData.resetForSelfTest(level);
    if (ok) {
      h.succeed();
    } else {
      h.fail("gateCachesArriveOncePerPlayerPerPlanet failed; see MARS-SELFTEST lines");
    }
  }
}
