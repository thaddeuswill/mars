package mars.planets;

import mars.index.sim.PlanetSim;
import mars.machines.BatteryBlock;
import mars.machines.MachineState;
import mars.machines.PoweredMachineBlock;
import mars.power.MachineLook;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Shows the planet's power state on the placed machines (BACKLOG #49, running-state table). The
 * look comes from {@link MachineLook}, the same function the Monitor's Power row uses. Only a block
 * whose state actually differs is rewritten (flag UPDATE_CLIENTS: no neighbour updates, no
 * re-registration), so a steady planet causes no block updates at all. Machines in chunks that are
 * not loaded are skipped and fixed by the next call after the chunk loads.
 *
 * <p>Cost: one pass over the placed machines of the planet (tens), a hash lookup each, run once per
 * planet step (every 100 ticks) and when a machine is placed or removed.
 */
public final class MachineStateSync {
  private MachineStateSync() {}

  /** Bring every loaded machine block in line with the grid. Returns how many blocks changed. */
  public static int sync(ServerLevel level, PlanetSim sim) {
    var grid = sim.power();
    MachineState state = MachineState.of(MachineLook.of(grid.state(sim.stepSols())));
    int bars = MachineLook.batteryBars(grid.charge(), grid.capacity());
    int changed = 0;
    for (String type : sim.placedTypes()) {
      boolean battery = type.equals(BatteryBlock.TYPE);
      if (!battery && !sim.machineDrawsPower(type)) {
        continue; // solar panel: its look is the dust stage, not a power state
      }
      for (long packed : sim.machinePositions(type)) {
        BlockPos pos = BlockPos.of(packed);
        if (!level.isLoaded(pos)) {
          continue;
        }
        BlockState now = level.getBlockState(pos);
        BlockState want = now;
        if (battery && now.hasProperty(BatteryBlock.CHARGE)) {
          want = now.setValue(BatteryBlock.CHARGE, bars);
        } else if (!battery && now.hasProperty(PoweredMachineBlock.MACHINE_STATE)) {
          want = now.setValue(PoweredMachineBlock.MACHINE_STATE, state);
        }
        if (want != now) {
          level.setBlock(pos, want, Block.UPDATE_CLIENTS);
          changed++;
        }
      }
    }
    return changed;
  }
}
