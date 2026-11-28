package mars.selftest;

import mars.machines.BatteryBlock;
import mars.machines.MachineState;
import mars.machines.MarsMachines;
import mars.machines.PoweredMachineBlock;
import mars.planets.MachineStateSync;
import mars.planets.PlanetData;
import mars.planets.PlanetManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * The block-state side of the running-state table (BACKLOG #49): {@code machine_state} follows the
 * planet's grid, {@code charge} follows the stored energy, only changed blocks are rewritten, and a
 * block saved without the properties still loads. Own batch: it resets the shared planet.
 */
@GameTestHolder("mars")
@PrefixGameTestTemplate(false)
public final class MachineStateGameTests {
  private MachineStateGameTests() {}

  private static MachineState look(GameTestHelper h, BlockPos p) {
    return h.getBlockState(p).getValue(PoweredMachineBlock.MACHINE_STATE);
  }

  @GameTest(template = "empty", batch = "mars_machine_state", timeoutTicks = 100)
  public static void machineStateFollowsThePowerGrid(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetData data = PlanetManager.resetForSelfTest(level);
    var sim = data.sim();
    BlockPos out1 = new BlockPos(0, 1, 0);
    BlockPos out2 = new BlockPos(2, 1, 0);
    BlockPos panel = new BlockPos(6, 1, 6);
    h.setBlock(out1, MarsMachines.REGOLITH_OUTGASSER.get());
    boolean ok =
        SelfTest.check(
            "machine_state.unpowered_is_off",
            "a placed Outgasser with no panel shows off",
            "state=" + look(h, out1),
            look(h, out1) == MachineState.OFF);
    h.setBlock(panel, MarsMachines.SOLAR_PANEL.get());
    ok &=
        SelfTest.check(
            "machine_state.powered_is_running",
            "with a clean panel (5 units for a 4 unit draw) the Outgasser shows running",
            "state=" + look(h, out1),
            look(h, out1) == MachineState.RUNNING);
    h.setBlock(out2, MarsMachines.REGOLITH_OUTGASSER.get());
    ok &=
        SelfTest.check(
            "machine_state.short_is_idle",
            "a second Outgasser (8 unit draw, 5 supply) leaves both idle (low power)",
            "a=" + look(h, out1) + " b=" + look(h, out2),
            look(h, out1) == MachineState.IDLE && look(h, out2) == MachineState.IDLE);
    int again = MachineStateSync.sync(level, sim);
    ok &=
        SelfTest.check(
            "machine_state.no_rewrite_when_unchanged",
            "a second sync with nothing changed rewrites no block",
            "changed=" + again,
            again == 0);
    h.destroyBlock(panel);
    ok &=
        SelfTest.check(
            "machine_state.back_to_off",
            "removing the panel puts both machines back to off",
            "a=" + look(h, out1) + " b=" + look(h, out2),
            look(h, out1) == MachineState.OFF && look(h, out2) == MachineState.OFF);
    h.destroyBlock(out2);
    h.destroyBlock(out1);

    ok &=
        SelfTest.check(
            "machine_state.property_values",
            "the property values are exactly off, idle, running (the blockstate keys)",
            PoweredMachineBlock.MACHINE_STATE.getPossibleValues().toString(),
            PoweredMachineBlock.MACHINE_STATE.getName().equals("machine_state")
                && PoweredMachineBlock.MACHINE_STATE.getPossibleValues().stream()
                    .map(MachineState::getSerializedName)
                    .toList()
                    .equals(java.util.List.of("off", "idle", "running"))
                && BatteryBlock.CHARGE.getName().equals("charge"));

    // Facing is kept while the state changes (the blockstate rotates with it).
    h.setBlock(out1, MarsMachines.WATER_RECLAIMER.get());
    BlockState east =
        h.getBlockState(out1)
            .setValue(
                net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING,
                net.minecraft.core.Direction.EAST);
    level.setBlock(h.absolutePos(out1), east, 2);
    h.setBlock(panel, MarsMachines.SOLAR_PANEL.get());
    ok &=
        SelfTest.check(
            "machine_state.facing_kept",
            "a reclaimer facing east keeps its facing when it turns running",
            "state=" + h.getBlockState(out1),
            look(h, out1) == MachineState.RUNNING
                && h.getBlockState(out1)
                        .getValue(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING)
                    == net.minecraft.core.Direction.EAST);
    h.destroyBlock(panel);
    h.destroyBlock(out1);

    // A block saved before the properties existed: no machine_state / charge in its NBT.
    var blocks = level.holderLookup(Registries.BLOCK);
    CompoundTag oldOven = new CompoundTag();
    oldOven.putString("Name", "mars:regolith_oven");
    CompoundTag props = new CompoundTag();
    props.putString("facing", "east");
    oldOven.put("Properties", props);
    BlockState oven = NbtUtils.readBlockState(blocks, oldOven);
    CompoundTag oldBattery = new CompoundTag();
    oldBattery.putString("Name", "mars:battery");
    CompoundTag props2 = new CompoundTag();
    props2.putString("facing", "south");
    oldBattery.put("Properties", props2);
    BlockState battery = NbtUtils.readBlockState(blocks, oldBattery);
    ok &=
        SelfTest.check(
            "machine_state.old_save_loads",
            "an Oven and a battery saved without the new properties load with idle and charge 0",
            "oven=" + oven + " battery=" + battery,
            oven.is(MarsMachines.REGOLITH_OVEN.get())
                && oven.getValue(PoweredMachineBlock.MACHINE_STATE) == MachineState.IDLE
                && battery.is(MarsMachines.BATTERY_BLOCK.get())
                && battery.getValue(BatteryBlock.CHARGE) == 0);
    BlockPos ovenPos = new BlockPos(0, 1, 4);
    BlockPos batPos = new BlockPos(2, 1, 4);
    level.setBlock(h.absolutePos(ovenPos), oven, 2);
    level.setBlock(h.absolutePos(batPos), battery, 2);
    // Placing registers the machine (and already shows off); put the loaded idle look back, as a
    // chunk that loaded after the last planet step would have it, then run the next sync.
    level.setBlock(h.absolutePos(ovenPos), oven, 2);
    int fixed = MachineStateSync.sync(level, sim);
    ok &=
        SelfTest.check(
            "machine_state.old_save_synced",
            "the next planet sync turns the loaded idle Oven off (no power); the empty battery needs no change",
            "fixed=" + fixed + " oven=" + look(h, ovenPos),
            fixed == 1 && look(h, ovenPos) == MachineState.OFF);

    // Battery bars from stored energy (10 unit capacity).
    int[] bars = new int[4];
    double[] stored = {0.0, 2.5, 5.0, 10.0};
    for (int i = 0; i < stored.length; i++) {
      sim.power().setCharge(stored[i]);
      MachineStateSync.sync(level, sim);
      bars[i] = h.getBlockState(batPos).getValue(BatteryBlock.CHARGE);
    }
    ok &=
        SelfTest.check(
            "machine_state.battery_bars",
            "stored 0, 2.5, 5, 10 of 10 units show charge 0, 1, 2, 3",
            "bars=" + java.util.Arrays.toString(bars),
            bars[0] == 0 && bars[1] == 1 && bars[2] == 2 && bars[3] == 3);
    h.destroyBlock(batPos);
    h.destroyBlock(ovenPos);
    if (ok) {
      h.succeed();
    } else {
      h.fail("machineStateFollowsThePowerGrid failed; see MARS-SELFTEST lines");
    }
  }
}
