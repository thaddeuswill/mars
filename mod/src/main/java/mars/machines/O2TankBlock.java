package mars.machines;

import mars.planets.PlanetManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.NeoForge;

/**
 * The wall oxygen tank. Right-clicking it reads the level on the action bar ("Tank: 14 of 40",
 * landing-spec 3.3 step 2): the dome's air store shared evenly over the tanks that stand, so a lost
 * tank shows a smaller capacity. Cost: one report read per click.
 */
public class O2TankBlock extends RegisteredMachineBlock {
  public O2TankBlock(Properties properties, VoxelShape shape) {
    super(properties, shape, "mars:o2_tank");
  }

  /** The per-tank reading, or null when the planet has no life model. */
  public static int[] reading(ServerLevel level) {
    var sim = PlanetManager.data(level).sim();
    if (sim.life() == null || sim.life().report() == null) {
      return null;
    }
    var report = sim.life().report();
    int tanks = Math.max(1, sim.machineCount("mars:o2_tank"));
    return new int[] {
      (int) Math.round(report.o2Stored() / tanks), (int) Math.round(report.o2Capacity() / tanks)
    };
  }

  @Override
  protected InteractionResult useWithoutItem(
      BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
    if (player instanceof ServerPlayer sp && level instanceof ServerLevel server) {
      int[] r = PlanetManager.isPlanet(server) ? reading(server) : null;
      if (r != null) {
        sp.displayClientMessage(Component.translatable("tank.level", r[0], r[1]), true);
        NeoForge.EVENT_BUS.post(new TankReadEvent(sp));
      }
    }
    return InteractionResult.sidedSuccess(level.isClientSide);
  }
}
