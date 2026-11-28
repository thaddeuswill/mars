package mars.habitat;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * The Stowage Crate: a barrel (27 slots, opens, drops its contents) with its own model. It joins
 * the barrel block entity type through {@link HabitatBlocks#addBlocksToEntityTypes}. Its collision
 * is the full cube, so the sealing rule treats it exactly like the barrel it replaces.
 */
public class StowageCrateBlock extends BarrelBlock {
  public static final MapCodec<StowageCrateBlock> CODEC = simpleCodec(StowageCrateBlock::new);

  public StowageCrateBlock(BlockBehaviour.Properties properties) {
    super(properties);
  }

  @Override
  public MapCodec<BarrelBlock> codec() {
    return (MapCodec<BarrelBlock>) (MapCodec<?>) CODEC;
  }
}
