package mars.machines;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Solar panel with per-panel dust state (DESIGN.md "Solar panel dust"; ARCHITECTURE.md 2.17).
 * {@code dust_stage} 0-4 is the dust the panel carries; {@code partly_cleaned} marks what a dust
 * devil left behind (the panel then recovers half of the output its stage had lost). The blockstate
 * JSON maps these to the six models. Stage 1: the state is set by commands only; nothing changes it
 * over time yet.
 */
public class SolarPanelBlock extends MachineBlock {
  public static final IntegerProperty DUST_STAGE = IntegerProperty.create("dust_stage", 0, 4);
  public static final BooleanProperty PARTLY_CLEANED = BooleanProperty.create("partly_cleaned");

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
}
