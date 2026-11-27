package mars.selftest;

import mars.dome.DomeSeals;
import mars.dome.Pos;
import mars.hull.HullBlocks;
import mars.machines.MarsMachines;
import mars.monitor.view.MonitorView;
import mars.planets.PlanetData;
import mars.planets.PlanetManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * Stage 3 slice 1 checks: real hull blocks build a small room, a real Planetary Monitor registers
 * it as a dome, and the real planet tick reports sealed or leaking. Each test is in its own batch
 * because tests in one batch run in parallel and these share the one planet of the test world.
 */
@GameTestHolder("mars")
@PrefixGameTestTemplate(false)
public final class DomeGameTests {
  private DomeGameTests() {}

  // Outer box 7 x 6 x 7: hull shell, 5 x 4 x 5 of air inside (100 blocks).
  private static final int SX = 7;
  private static final int SY = 6;
  private static final int SZ = 7;
  private static final BlockPos MONITOR = new BlockPos(3, 1, 1);

  /** A wall cell on the east side, where the hole and the door go. */
  private static final BlockPos EAST_WALL = new BlockPos(6, 1, 3);

  private static void buildRoom(GameTestHelper h) {
    for (int x = 0; x < SX; x++) {
      for (int y = 0; y < SY; y++) {
        for (int z = 0; z < SZ; z++) {
          boolean wall = x == 0 || x == SX - 1 || y == 0 || y == SY - 1 || z == 0 || z == SZ - 1;
          h.setBlock(
              new BlockPos(x, y, z),
              wall
                  ? HullBlocks.HULL_PLATE.get().defaultBlockState()
                  : Blocks.AIR.defaultBlockState());
        }
      }
    }
  }

  private static void clearRoom(GameTestHelper h) {
    for (int x = 0; x < SX; x++) {
      for (int y = 0; y < SY; y++) {
        for (int z = 0; z < SZ; z++) {
          h.setBlock(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState());
        }
      }
    }
  }

  private static DomeSeals.Status status(PlanetData d, BlockPos rel, GameTestHelper h) {
    return d.sim().domes().status(h.absolutePos(rel).asLong());
  }

  private static String monitorWord(PlanetData d) {
    return MonitorView.of(d.sim()).rows().stream()
        .filter(r -> r.labelKey().equals("monitor.row.dome"))
        .map(MonitorView.Row::valueKey)
        .findFirst()
        .orElse("(no dome row)");
  }

  private static void put(GameTestHelper h, BlockPos p, net.minecraft.world.level.block.Block b) {
    h.setBlock(p, b.defaultBlockState());
  }

  @GameTest(template = "empty", batch = "mars_dome_seal", timeoutTicks = 200)
  public static void sealedRoomHoleDoorAndGlass(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetData data = PlanetManager.data(level);
    buildRoom(h);
    h.setBlock(MONITOR, MarsMachines.PLANETARY_MONITOR.get().defaultBlockState());
    long seed = h.absolutePos(MONITOR).asLong();
    boolean ok =
        SelfTest.check(
            "dome.monitor_registers",
            "placing the Monitor registers one dome, not checked yet",
            "domes=" + data.sim().domes().count() + " status=" + status(data, MONITOR, h),
            data.sim().domes().status(seed) == DomeSeals.Status.UNKNOWN);
    final boolean[] okBox = {ok};
    // Stage by stage, each a few ticks after an edit (the planet tick does the check).
    h.runAfterDelay(
        3,
        () -> {
          var d = PlanetManager.data(level);
          okBox[0] &=
              SelfTest.check(
                  "dome.sealed_room",
                  "a closed 5x4x5 hull room around the Monitor reads SEALED, 99 air blocks, Monitor says Sealed",
                  "status="
                      + status(d, MONITOR, h)
                      + " volume="
                      + d.sim().domes().volume(seed)
                      + " word="
                      + monitorWord(d),
                  status(d, MONITOR, h) == DomeSeals.Status.SEALED
                      && d.sim().domes().volume(seed) == 99
                      && monitorWord(d).equals("monitor.dome.sealed"));
          // Punch a hole: break one wall plate.
          h.destroyBlock(EAST_WALL);
        });
    h.runAfterDelay(
        5,
        () -> {
          var d = PlanetManager.data(level);
          okBox[0] &=
              SelfTest.check(
                  "dome.hole_leaks",
                  "two ticks after a wall plate is broken the room reads LEAKING and the Monitor says Leaking",
                  "status=" + status(d, MONITOR, h) + " word=" + monitorWord(d),
                  status(d, MONITOR, h) == DomeSeals.Status.LEAKING
                      && monitorWord(d).equals("monitor.dome.leaking"));
          put(h, EAST_WALL, HullBlocks.HULL_GLASS.get());
        });
    h.runAfterDelay(
        8,
        () -> {
          var d = PlanetManager.data(level);
          okBox[0] &=
              SelfTest.check(
                  "dome.glass_seals",
                  "hull glass in the hole seals it again",
                  "status=" + status(d, MONITOR, h),
                  status(d, MONITOR, h) == DomeSeals.Status.SEALED);
          h.setBlock(EAST_WALL, Blocks.STONE_SLAB.defaultBlockState());
        });
    h.runAfterDelay(
        11,
        () -> {
          var d = PlanetManager.data(level);
          okBox[0] &=
              SelfTest.check(
                  "dome.partial_block_leaks",
                  "a slab in a wall cell leaks (full block or nothing)",
                  "status=" + status(d, MONITOR, h),
                  status(d, MONITOR, h) == DomeSeals.Status.LEAKING);
          // A hull door, closed, in the east wall (two blocks high).
          BlockState lower =
              HullBlocks.HULL_DOOR
                  .get()
                  .defaultBlockState()
                  .setValue(DoorBlock.FACING, Direction.EAST)
                  .setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER);
          h.setBlock(EAST_WALL, Blocks.AIR.defaultBlockState());
          h.setBlock(EAST_WALL, lower);
          h.setBlock(EAST_WALL.above(), lower.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER));
        });
    h.runAfterDelay(
        14,
        () -> {
          var d = PlanetManager.data(level);
          okBox[0] &=
              SelfTest.check(
                  "dome.door_closed_seals",
                  "a closed hull door seals the wall",
                  "status="
                      + status(d, MONITOR, h)
                      + " door="
                      + h.getBlockState(EAST_WALL).getBlock()
                      + " open="
                      + h.getBlockState(EAST_WALL).getValue(DoorBlock.OPEN),
                  status(d, MONITOR, h) == DomeSeals.Status.SEALED);
          // Open it the way a player does: right-click with a fake player.
          FakePlayer player = FakePlayerFactory.getMinecraft(level);
          h.useBlock(EAST_WALL, player);
        });
    h.runAfterDelay(
        17,
        () -> {
          var d = PlanetManager.data(level);
          okBox[0] &=
              SelfTest.check(
                  "dome.door_open_leaks",
                  "after a right-click opens the hull door the room reads LEAKING within 3 ticks",
                  "open="
                      + h.getBlockState(EAST_WALL).getValue(DoorBlock.OPEN)
                      + " status="
                      + status(d, MONITOR, h),
                  h.getBlockState(EAST_WALL).getValue(DoorBlock.OPEN)
                      && status(d, MONITOR, h) == DomeSeals.Status.LEAKING);
          FakePlayer player = FakePlayerFactory.getMinecraft(level);
          h.useBlock(EAST_WALL, player);
        });
    h.runAfterDelay(
        20,
        () -> {
          var d = PlanetManager.data(level);
          okBox[0] &=
              SelfTest.check(
                  "dome.door_closed_again",
                  "closing the door seals the room again",
                  "status=" + status(d, MONITOR, h),
                  status(d, MONITOR, h) == DomeSeals.Status.SEALED);
          // Take the Monitor away: the dome goes with it.
          h.destroyBlock(MONITOR);
          okBox[0] &=
              SelfTest.check(
                  "dome.monitor_removed_unregisters",
                  "breaking the Monitor unregisters the dome",
                  "domes=" + d.sim().domes().count(),
                  d.sim().domes().status(seed) == null);
          clearRoom(h);
          if (okBox[0]) {
            h.succeed();
          } else {
            h.fail("dome room checks failed; see MARS-SELFTEST lines");
          }
        });
  }

  @GameTest(template = "empty", batch = "mars_dome_open")
  public static void aMonitorInOpenAirIsLeakingAndPosMatchesBlockPos(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetData data = PlanetManager.data(level);
    h.setBlock(new BlockPos(2, 1, 2), MarsMachines.PLANETARY_MONITOR.get().defaultBlockState());
    BlockPos at = h.absolutePos(new BlockPos(2, 1, 2));
    h.runAfterDelay(
        3,
        () -> {
          var d = PlanetManager.data(level);
          boolean ok =
              SelfTest.check(
                  "dome.open_air_leaks",
                  "a Monitor standing in the open (sky above, no walls) reads LEAKING",
                  "status=" + d.sim().domes().status(at.asLong()),
                  d.sim().domes().status(at.asLong()) == DomeSeals.Status.LEAKING);
          boolean same = true;
          for (BlockPos p :
              new BlockPos[] {
                new BlockPos(0, 0, 0),
                new BlockPos(5, 64, -3),
                new BlockPos(-30_000_000, 255, 29_999_999),
                new BlockPos(-1, 0, -1)
              }) {
            same &=
                Pos.pack(p.getX(), p.getY(), p.getZ()) == p.asLong()
                    && Pos.x(p.asLong()) == p.getX()
                    && Pos.y(p.asLong()) == p.getY()
                    && Pos.z(p.asLong()) == p.getZ();
          }
          ok &=
              SelfTest.check(
                  "dome.pos_matches_blockpos",
                  "mars.dome.Pos packs exactly like BlockPos.asLong",
                  "same=" + same,
                  same);
          h.destroyBlock(new BlockPos(2, 1, 2));
          data.sim().domes().unregister(at.asLong());
          if (ok) {
            h.succeed();
          } else {
            h.fail("open-air dome checks failed; see MARS-SELFTEST lines");
          }
        });
  }

  @GameTest(template = "empty", batch = "mars_dome_save")
  public static void domesSaveAndOldSavesLoad(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetData data = PlanetManager.data(level);
    long seed = h.absolutePos(new BlockPos(1, 1, 1)).asLong();
    data.sim().domes().register(seed);
    CompoundTag tag = data.save(new CompoundTag(), level.registryAccess());
    PlanetData back =
        PlanetData.load(level.dimension().location().toString(), tag, level.registryAccess());
    boolean ok =
        SelfTest.check(
            "dome.save_round_trip",
            "a registered dome comes back from NBT with the same state; schema 6",
            "domes=" + back.sim().domes().count() + " schema=" + tag.getInt("schema"),
            back.sim().domes().count() == 1
                && back.sim().domes().status(seed) == DomeSeals.Status.UNKNOWN
                && data.sim().toState().equals(back.sim().toState())
                && tag.getInt("schema") == 6);
    CompoundTag old = tag.copy();
    old.remove("domes");
    old.putInt("schema", 5);
    PlanetData oldBack =
        PlanetData.load(level.dimension().location().toString(), old, level.registryAccess());
    ok &=
        SelfTest.check(
            "dome.schema5_save_loads",
            "a schema 5 save (no dome list) loads with no domes",
            "domes=" + oldBack.sim().domes().count(),
            oldBack.sim().domes().count() == 0);
    data.sim().domes().unregister(seed);
    if (ok) {
      h.succeed();
    } else {
      h.fail("dome save checks failed; see MARS-SELFTEST lines");
    }
  }
}
