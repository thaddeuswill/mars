package mars.selftest;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import mars.habitat.HabitatBlocks;
import mars.planets.PlanetManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** The Habitat Bunk and Stowage Crate (Artist handoff, ARCHITECTURE.md 2.21). */
@GameTestHolder("mars")
@PrefixGameTestTemplate(false)
public final class HabitatGameTests {
  private HabitatGameTests() {}

  @GameTest(template = "empty", batch = "mars_habitat_furniture", timeoutTicks = 200)
  public static void bunkSleepsAndSetsSpawnWithoutMovingMarsTime(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    var planet = PlanetManager.resetForSelfTest(level);
    BlockPos foot = h.absolutePos(new BlockPos(1, 1, 1));
    BlockPos head = foot.south();
    for (int dx = -2; dx <= 2; dx++) {
      for (int dz = -2; dz <= 3; dz++) {
        for (int dy = 0; dy <= 3; dy++) {
          level.setBlock(foot.offset(dx, dy, dz), Blocks.AIR.defaultBlockState(), 2);
        }
      }
    }
    var bunk = HabitatBlocks.HABITAT_BUNK.get();
    level.setBlock(foot, bunk.defaultBlockState().setValue(BedBlock.FACING, Direction.SOUTH), 3);
    level.setBlock(
        head,
        bunk.defaultBlockState()
            .setValue(BedBlock.FACING, Direction.SOUTH)
            .setValue(BedBlock.PART, BedPart.HEAD),
        3);
    var footState = level.getBlockState(foot);
    boolean okA =
        SelfTest.check(
            "habitat.bunk_is_a_model_bed_without_block_entity",
            "the bunk renders as a model, is a bed, has no block entity, and a 9 px collision box",
            "render="
                + footState.getRenderShape()
                + " isBed="
                + footState.isBed(level, foot, null)
                + " be="
                + level.getBlockEntity(foot)
                + " collisionTop="
                + footState.getCollisionShape(level, foot).max(Direction.Axis.Y),
            footState.getRenderShape() == RenderShape.MODEL
                && footState.isBed(level, foot, null)
                && level.getBlockEntity(foot) == null
                && level.getBlockEntity(head) == null
                && Math.abs(
                        footState.getCollisionShape(level, foot).max(Direction.Axis.Y) - 9 / 16.0)
                    < 1e-9);
    var bedWorks =
        level
            .getServer()
            .registryAccess()
            .registryOrThrow(Registries.DIMENSION_TYPE)
            .get(ResourceLocation.fromNamespaceAndPath("mars", "mars"));
    okA &=
        SelfTest.check(
            "habitat.mars_dimension_beds_work",
            "dimension type mars:mars has bed_works true (a bed does not explode)",
            "bedWorks=" + (bedWorks == null ? "missing" : bedWorks.bedWorks()),
            bedWorks != null && bedWorks.bedWorks());
    final boolean okAFinal = okA;
    final long oldDay = level.getDayTime();
    level.setDayTime(18000); // night: vanilla only lets a player sleep at night
    h.runAfterDelay(
        5,
        () -> {
          boolean ok0 = okAFinal;
          var player =
              FakePlayerFactory.get(
                  level,
                  new GameProfile(
                      UUID.fromString("6d617273-0000-4000-8000-0000000000b2"), "[Sleeper]"));
          player.setPos(foot.getX() + 0.5, foot.getY(), foot.getZ() - 0.5);
          // (night set above)
          level.setDayTime(18000); // night: vanilla only lets a player sleep at night
          var result = player.startSleepInBed(head);
          boolean sleeping = player.isSleeping();
          var spawn = player.getRespawnPosition();
          ok0 &=
              SelfTest.check(
                  "habitat.bunk_sleeps_and_sets_spawn",
                  "a player can lie down in the bunk and the respawn point is the bunk",
                  "result="
                      + result.map(Enum::name, u -> "OK")
                      + " sleeping="
                      + sleeping
                      + " spawn="
                      + spawn,
                  result.right().isPresent()
                      && sleeping
                      && foot.getY() == spawn.getY()
                      && spawn.distManhattan(head) <= 1);
          // What a vanilla night skip does to the level: it moves the day time. Mars time must not
          // move.
          long simBefore = planet.sim().model().simTicks();
          var valuesBefore = planet.sim().model().storedValues();
          level.setDayTime(18000 + 24000);
          player.stopSleepInBed(true, true);
          ok0 &=
              SelfTest.check(
                  "habitat.sleep_does_not_move_mars_time",
                  "moving the level day time by a night skip leaves the sim ticks and every Index value unchanged",
                  "simTicks=" + simBefore + "->" + planet.sim().model().simTicks(),
                  planet.sim().model().simTicks() == simBefore
                      && planet.sim().model().storedValues().equals(valuesBefore));
          level.setDayTime(oldDay);
          // Drops: breaking either half gives exactly one bunk.
          final boolean finalOk = ok0;
          level.destroyBlock(foot, true);
          h.runAfterDelay(
              3,
              () -> {
                int n = countDrops(level, foot, HabitatBlocks.HABITAT_BUNK_ITEM.get());
                boolean ok2 =
                    SelfTest.check(
                        "habitat.bunk_drops_one",
                        "breaking the foot removes both halves and drops exactly one Habitat Bunk",
                        "drops=" + n + " head=" + level.getBlockState(head).getBlock(),
                        n == 1 && level.getBlockState(head).isAir());
                // Crate: opens, holds items, keeps them when broken.
                BlockPos crate = h.absolutePos(new BlockPos(1, 1, 5));
                level.setBlock(
                    crate,
                    HabitatBlocks.STOWAGE_CRATE
                        .get()
                        .defaultBlockState()
                        .setValue(BarrelBlock.FACING, Direction.NORTH),
                    3);
                var be = level.getBlockEntity(crate);
                boolean hasBe = be instanceof BarrelBlockEntity;
                if (hasBe) {
                  ((BarrelBlockEntity) be)
                      .setItem(0, new net.minecraft.world.item.ItemStack(Blocks.DIRT, 3));
                }
                level.destroyBlock(crate, true);
                h.runAfterDelay(
                    3,
                    () -> {
                      int crates = countDrops(level, crate, HabitatBlocks.STOWAGE_CRATE_ITEM.get());
                      var dirt =
                          level
                              .getEntitiesOfClass(ItemEntity.class, new AABB(crate).inflate(3))
                              .stream()
                              .filter(e -> e.getItem().is(Blocks.DIRT.asItem()))
                              .count();
                      boolean ok3 =
                          SelfTest.check(
                              "habitat.crate_is_a_working_barrel",
                              "the crate has a barrel block entity, holds items, and drops itself and its contents",
                              "be=" + hasBe + " crates=" + crates + " dirtStacks=" + dirt,
                              hasBe && crates == 1 && dirt == 1);
                      PlanetManager.resetForSelfTest(level);
                      if (ok2 && ok3 && finalOk) {
                        h.succeed();
                      } else {
                        h.fail("habitat checks failed; see MARS-SELFTEST lines");
                      }
                    });
              });
        });
  }

  private static int countDrops(
      ServerLevel level, BlockPos at, net.minecraft.world.item.Item item) {
    int n = 0;
    for (ItemEntity e : level.getEntitiesOfClass(ItemEntity.class, new AABB(at).inflate(3))) {
      if (e.getItem().is(item)) {
        n += e.getItem().getCount();
      }
    }
    return n;
  }
}
