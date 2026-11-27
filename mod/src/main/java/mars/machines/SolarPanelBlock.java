package mars.machines;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * The round solar array (BACKLOG #28): one centre block that draws the whole array, plus eight
 * invisible {@link SolarPanelCanopyBlock}s that reserve the 3x3 footprint under the overhang
 * (ARCHITECTURE.md 2.18). Dust state: {@code dust_stage} 0-4 and {@code partly_cleaned} (2.17).
 */
public class SolarPanelBlock extends MachineBlock {
  public static final IntegerProperty DUST_STAGE = IntegerProperty.create("dust_stage", 0, 4);
  public static final BooleanProperty PARTLY_CLEANED = BooleanProperty.create("partly_cleaned");

  public static final String TYPE = "mars:solar_panel";

  /** Share of full output (0 to 1) for a panel in this state, from the dust tuning data. */
  public static double outputFraction(BlockState state) {
    return mars.index.IndexDefinitions.require()
        .dust()
        .outputFraction(state.getValue(DUST_STAGE), state.getValue(PARTLY_CLEANED));
  }

  public SolarPanelBlock(Properties properties, VoxelShape shape) {
    super(properties, shape);
    registerDefaultState(
        defaultBlockState().setValue(DUST_STAGE, 0).setValue(PARTLY_CLEANED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(DUST_STAGE, PARTLY_CLEANED);
  }

  /** True if all eight cells around {@code centre} (same height) can take a canopy block. */
  public static boolean footprintClear(LevelReader level, BlockPos centre) {
    for (int dx = -1; dx <= 1; dx++) {
      for (int dz = -1; dz <= 1; dz++) {
        if (dx == 0 && dz == 0) {
          continue;
        }
        if (!level.getBlockState(centre.offset(dx, 0, dz)).canBeReplaced()) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    if (!footprintClear(context.getLevel(), context.getClickedPos())) {
      // Tell the player why nothing happened instead of failing silently.
      if (context.getLevel().isClientSide() && context.getPlayer() != null) {
        context
            .getPlayer()
            .displayClientMessage(
                Component.translatable("block.mars.solar_panel.needs_space"), true);
      }
      return null;
    }
    return super.getStateForPlacement(context);
  }

  @Override
  protected void onPlace(
      BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
    super.onPlace(state, level, pos, oldState, movedByPiston);
    if (level instanceof net.minecraft.server.level.ServerLevel server
        && mars.planets.PlanetManager.isPlanet(server)) {
      if (!oldState.is(this)) {
        mars.planets.PlanetManager.machinePlaced(server, TYPE, pos);
      }
      // Dust lowers the panel's share of the grid's supply (balance sheet 2a, 2b).
      mars.planets.PlanetManager.machineOutput(server, pos, outputFraction(state));
    }
    if (level.isClientSide() || oldState.is(this)) {
      return; // a dust-stage change, not a new panel
    }
    // Runs for player placement and for /setblock alike. Cells that aren't free are skipped.
    BlockState canopy = MarsMachines.SOLAR_PANEL_CANOPY.get().defaultBlockState();
    for (int dx = -1; dx <= 1; dx++) {
      for (int dz = -1; dz <= 1; dz++) {
        BlockPos p = pos.offset(dx, 0, dz);
        if ((dx != 0 || dz != 0) && level.getBlockState(p).canBeReplaced()) {
          level.setBlock(
              p,
              canopy
                  .setValue(SolarPanelCanopyBlock.OFFSET_X, dx + 1)
                  .setValue(SolarPanelCanopyBlock.OFFSET_Z, dz + 1),
              Block.UPDATE_ALL);
        }
      }
    }
  }

  @Override
  protected void onRemove(
      BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
    if (level instanceof net.minecraft.server.level.ServerLevel server
        && !newState.is(this)
        && mars.planets.PlanetManager.isPlanet(server)) {
      mars.planets.PlanetManager.machineRemoved(server, TYPE, pos);
    }
    if (!level.isClientSide() && !newState.is(this)) {
      for (int dx = -1; dx <= 1; dx++) {
        for (int dz = -1; dz <= 1; dz++) {
          BlockPos p = pos.offset(dx, 0, dz);
          BlockState s = level.getBlockState(p);
          if (s.getBlock() instanceof SolarPanelCanopyBlock
              && SolarPanelCanopyBlock.centreOf(p, s).equals(pos)) {
            level.setBlock(
                p,
                net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(),
                Block.UPDATE_ALL);
          }
        }
      }
    }
    super.onRemove(state, level, pos, newState, movedByPiston);
  }
}
