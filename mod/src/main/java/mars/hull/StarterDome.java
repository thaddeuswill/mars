package mars.hull;

import java.util.List;
import mars.dome.DomeTemplate;
import mars.machines.MarsMachines;
import mars.machines.SolarPanelBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.Heightmap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Places the starting dome (docs/design/dome-layout.md) on a planet, once per planet per world, the
 * first time a player arrives (ARCHITECTURE.md 2.3). The shape is {@link DomeTemplate}; this class
 * only turns each cell into a block. The cracked plate in Hab B's wall is left in on purpose
 * (Landing step 5). The surface is flat on planet 1, so the floor is the top layer of the ground; a
 * bumpy planet would need a site check first (not built).
 *
 * <p>Cost: once per world, about 1,900 block placements (flag 2: no neighbour updates), a few
 * milliseconds; then nothing. One saved-data read per arrival.
 */
public final class StarterDome {
  private static final Logger LOG = LoggerFactory.getLogger("mars");

  /** World x and z of the spot the player stands on at arrival; the dome is built around it. */
  public static final int ARRIVAL_X = 0;

  public static final int ARRIVAL_Z = 0;

  private StarterDome() {}

  /** Off only for a dev test world: {@code -Dmars.noStarterDome=true} (never set in play). */
  public static boolean enabled() {
    return !Boolean.getBoolean("mars.noStarterDome");
  }

  /** The placement record of a planet level. */
  public static StarterDomeData data(ServerLevel level) {
    return level.getDataStorage().computeIfAbsent(StarterDomeData.factory(), StarterDomeData.NAME);
  }

  /**
   * Make sure this planet has its starting dome and return where a new arrival should stand (the
   * shelter, just inside its door). Places the dome the first time. Returns null for a planet saved
   * before life support existed (schema 6 and older, no life model): that world keeps its own base
   * and gets no dome, so nothing the players built at the arrival point is overwritten.
   */
  public static BlockPos ensure(ServerLevel level, int groupSize) {
    StarterDomeData d = data(level);
    if (d.origin() == null && mars.planets.PlanetManager.data(level).sim().life() == null) {
      return null;
    }
    if (d.origin() == null) {
      level.getChunk(ARRIVAL_X >> 4, ARRIVAL_Z >> 4);
      int floorY =
          level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ARRIVAL_X, ARRIVAL_Z) - 1;
      BlockPos origin =
          new BlockPos(ARRIVAL_X - DomeTemplate.SPAWN_X, floorY, ARRIVAL_Z - DomeTemplate.SPAWN_Z);
      place(level, origin, groupSize);
      d.setOrigin(origin);
      LOG.info(
          "Starting dome placed at {} in {} (group {})",
          origin.toShortString(),
          level.dimension().location(),
          groupSize);
    }
    return spawnPos(d.origin());
  }

  public static BlockPos spawnPos(BlockPos origin) {
    return origin.offset(DomeTemplate.SPAWN_X, 1, DomeTemplate.SPAWN_Z);
  }

  /** Pre-built oxygen makers for a group (balance sheet 2d.2: ceil(n / 2)). */
  public static int prebuiltMakers(int groupSize) {
    return (groupSize + 1) / 2;
  }

  /** Place the template with its floor corner at {@code origin}. Public for tests. */
  public static void place(ServerLevel level, BlockPos origin, int groupSize) {
    // Clear the dome box first so nothing the world had there leaks into the sealing.
    BlockState air = Blocks.AIR.defaultBlockState();
    for (int x = 0; x < DomeTemplate.SIZE_X; x++) {
      for (int z = 0; z < DomeTemplate.SIZE_Z; z++) {
        for (int y = 1; y < DomeTemplate.SIZE_Y; y++) {
          level.setBlock(origin.offset(x, y, z), air, Block.UPDATE_CLIENTS);
        }
      }
    }
    List<DomeTemplate.Cell> cells = DomeTemplate.cells(prebuiltMakers(groupSize), true);
    for (DomeTemplate.Cell c : cells) {
      BlockState s = stateFor(c);
      if (s != null) {
        level.setBlock(origin.offset(c.x(), c.y(), c.z()), s, Block.UPDATE_CLIENTS);
      }
    }
    placeRoofFixtures(level, origin);
    fillLocker(level, origin, groupSize);
    placeSolarPad(level, origin);
    // The storm drained the tanks: start fills (balance sheet 2d.2), once, after every tank exists.
    var planet = mars.planets.PlanetManager.data(level);
    planet.sim().startLifeSupport();
    planet.setDirty();
  }

  /** Seal Kits in the locker: group size + 1 (one Landing kit each, one spare), at most 6. */
  public static int lockerKits(int groupSize) {
    return Math.max(1, Math.min(6, groupSize + 1));
  }

  /** The Ops locker: a named crate holding the Landing's seal kits (landing-spec section 2). */
  private static void fillLocker(ServerLevel level, BlockPos origin, int groupSize) {
    BlockPos pos = origin.offset(DomeTemplate.LOCKER_X, 1, DomeTemplate.LOCKER_Z);
    if (level.getBlockEntity(pos)
        instanceof net.minecraft.world.level.block.entity.BaseContainerBlockEntity be) {
      be.applyComponents(
          net.minecraft.core.component.DataComponentMap.EMPTY,
          net.minecraft.core.component.DataComponentPatch.builder()
              .set(
                  net.minecraft.core.component.DataComponents.CUSTOM_NAME,
                  net.minecraft.network.chat.Component.translatable("locker.name"))
              .build());
      be.setItem(
          0,
          new net.minecraft.world.item.ItemStack(HullBlocks.SEAL_KIT.get(), lockerKits(groupSize)));
      be.setChanged();
    }
  }

  /** One dish and one vent on the shelter roof (y 5, above the sealed roof; no sealing role). */
  private static void placeRoofFixtures(ServerLevel level, BlockPos origin) {
    level.setBlock(
        origin.offset(DomeTemplate.DISH_X, DomeTemplate.ROOF_FIXTURE_Y, DomeTemplate.DISH_Z),
        HullBlocks.ROOF_DISH.get().defaultBlockState(),
        Block.UPDATE_CLIENTS);
    level.setBlock(
        origin.offset(DomeTemplate.VENT_X, DomeTemplate.ROOF_FIXTURE_Y, DomeTemplate.VENT_Z),
        HullBlocks.ROOF_VENT.get().defaultBlockState(),
        Block.UPDATE_CLIENTS);
  }

  /** Pad 1 of the east apron: one array at dust stage 1 (balance sheet 2d.2). */
  private static void placeSolarPad(ServerLevel level, BlockPos origin) {
    BlockPos centre = origin.offset(27, 1, 1);
    level.setBlock(
        centre,
        MarsMachines.SOLAR_PANEL
            .get()
            .defaultBlockState()
            .setValue(SolarPanelBlock.DUST_STAGE, 1)
            .setValue(
                net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING, Direction.WEST),
        Block.UPDATE_ALL);
  }

  private static Direction dir(DomeTemplate.Face f) {
    return switch (f) {
      case NORTH -> Direction.NORTH;
      case SOUTH -> Direction.SOUTH;
      case EAST -> Direction.EAST;
      case WEST -> Direction.WEST;
    };
  }

  static BlockState stateFor(DomeTemplate.Cell c) {
    Direction f = dir(c.face());
    return switch (c.kind()) {
      case AIR -> null;
      case HULL -> HullBlocks.HULL_PLATE.get().defaultBlockState();
      case HULL_BASE -> HullBlocks.HULL_PLATE_BASE.get().defaultBlockState();
      case HULL_RIM -> HullBlocks.HULL_PLATE_RIM.get().defaultBlockState();
      case HULL_LIGHT -> HullBlocks.HULL_LIGHT.get().defaultBlockState();
      case GLASS_RIBBED -> HullBlocks.HULL_GLASS_RIBBED.get().defaultBlockState();
      case GLASS -> HullBlocks.HULL_GLASS.get().defaultBlockState();
      case CRACKED -> HullBlocks.HULL_PLATE_CRACKED.get().defaultBlockState();
      case DOOR ->
          HullBlocks.HULL_DOOR
              .get()
              .defaultBlockState()
              .setValue(DoorBlock.FACING, f)
              .setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER);
      case DOOR_UPPER ->
          HullBlocks.HULL_DOOR
              .get()
              .defaultBlockState()
              .setValue(DoorBlock.FACING, f)
              .setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER);
      case MONITOR -> facing(MarsMachines.PLANETARY_MONITOR.get(), f);
      case TANK -> facing(MarsMachines.O2_TANK.get(), f);
      case MAKER -> facing(MarsMachines.OXYGEN_MAKER_BLOCK.get(), f);
      case RECLAIMER -> facing(MarsMachines.WATER_RECLAIMER.get(), f);
      case BATTERY -> facing(MarsMachines.BATTERY_BLOCK.get(), f);
      case BED_FOOT ->
          mars.habitat.HabitatBlocks.HABITAT_BUNK
              .get()
              .defaultBlockState()
              .setValue(net.minecraft.world.level.block.BedBlock.FACING, f)
              .setValue(net.minecraft.world.level.block.BedBlock.PART, BedPart.FOOT);
      case BED_HEAD ->
          mars.habitat.HabitatBlocks.HABITAT_BUNK
              .get()
              .defaultBlockState()
              .setValue(net.minecraft.world.level.block.BedBlock.FACING, f)
              .setValue(net.minecraft.world.level.block.BedBlock.PART, BedPart.HEAD);
      case LECTERN ->
          Blocks.LECTERN
              .defaultBlockState()
              .setValue(net.minecraft.world.level.block.LecternBlock.FACING, f);
      case BENCH -> Blocks.CRAFTING_TABLE.defaultBlockState();
      case CRATE, LOCKER ->
          mars.habitat.HabitatBlocks.STOWAGE_CRATE
              .get()
              .defaultBlockState()
              .setValue(net.minecraft.world.level.block.BarrelBlock.FACING, f);
    };
  }

  private static BlockState facing(Block b, Direction f) {
    return b.defaultBlockState()
        .setValue(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING, f);
  }
}
