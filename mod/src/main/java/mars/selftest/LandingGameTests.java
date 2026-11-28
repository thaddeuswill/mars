package mars.selftest;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import mars.dome.DomeTemplate;
import mars.dome.SealCheck;
import mars.hull.HullBlocks;
import mars.hull.StarterDome;
import mars.hull.WorldProbe;
import mars.machines.O2TankBlock;
import mars.planets.PlanetManager;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * Stage 3 slice 4 (Landing, docs/design/landing-spec.md): real blocks and fake players. Each test
 * has its own batch: batches run in parallel and the tests share the one planet of the test world.
 */
@GameTestHolder("mars")
@PrefixGameTestTemplate(false)
public final class LandingGameTests {
  private LandingGameTests() {}

  /** Clear the dome box and a margin so blocks left by another test cannot change a seal. */
  static void scrub(ServerLevel level, BlockPos origin) {
    for (int x = -3; x < DomeTemplate.SIZE_X + 12; x++) {
      for (int y = -1; y < DomeTemplate.SIZE_Y + 2; y++) {
        for (int z = -3; z < DomeTemplate.SIZE_Z + 3; z++) {
          BlockPos p = origin.offset(x, y, z);
          if (y >= 0 && !level.getBlockState(p).isAir()) {
            level.setBlock(p, Blocks.AIR.defaultBlockState(), 2);
          }
        }
      }
    }
  }

  static net.minecraft.server.level.ServerPlayer player(ServerLevel level, String name, int n) {
    return FakePlayerFactory.get(
        level,
        new GameProfile(UUID.fromString("6d617273-0000-4000-8000-00000000c0" + (10 + n)), name));
  }

  private static InteractionResult use(
      net.minecraft.server.level.ServerPlayer p, ItemStack stack, BlockPos pos) {
    var hit = new BlockHitResult(Vec3.atCenterOf(pos), net.minecraft.core.Direction.UP, pos, false);
    return stack.useOn(new UseOnContext(p.serverLevel(), p, InteractionHand.MAIN_HAND, stack, hit));
  }

  @GameTest(template = "empty", batch = "mars_landing_kit", timeoutTicks = 300)
  public static void sealKitLockerAndTankReadout(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetManager.resetForSelfTest(level);
    BlockPos origin = h.absolutePos(new BlockPos(0, 1, 0));
    scrub(level, origin);
    StarterDome.place(level, origin, 3);
    BlockPos crack = origin.offset(DomeTemplate.BREACH_X, 2, DomeTemplate.BREACH_Z);
    BlockPos habB = origin.offset(3, 1, 11);
    BlockPos lockerPos = origin.offset(DomeTemplate.LOCKER_X, 1, DomeTemplate.LOCKER_Z);
    h.runAfterDelay(
        5,
        () -> {
          var probe = new WorldProbe(level);
          int kitsInLocker = 0;
          String name = "(none)";
          if (level.getBlockEntity(lockerPos) instanceof BaseContainerBlockEntity be) {
            name = be.getName().getString();
            for (int i = 0; i < be.getContainerSize(); i++) {
              if (be.getItem(i).is(HullBlocks.SEAL_KIT.get())) {
                kitsInLocker += be.getItem(i).getCount();
              }
            }
          }
          boolean ok =
              SelfTest.check(
                  "landing.locker_holds_group_plus_one_kits",
                  "the Ops locker is a named crate holding group size + 1 seal kits (3 + 1 = 4 here)",
                  "kits=" + kitsInLocker + " name=" + name,
                  kitsInLocker == StarterDome.lockerKits(3) && kitsInLocker == 4);
          var player = player(level, "[Kit]", 1);
          player.setPos(origin.getX() + 3, origin.getY() + 1, origin.getZ() + 11);
          ItemStack kit = new ItemStack(HullBlocks.SEAL_KIT.get(), 2);
          // A wrong target: nothing changes, nothing is used.
          BlockPos wall = origin.offset(0, 1, 5);
          var before = level.getBlockState(wall);
          use(player, kit, wall);
          ok &=
              SelfTest.check(
                  "landing.seal_kit_wrong_target_uses_nothing",
                  "a kit used on an ordinary hull plate changes nothing and is not used up",
                  "block="
                      + level.getBlockState(wall).getBlock().getDescriptionId()
                      + " kits="
                      + kit.getCount(),
                  level.getBlockState(wall) == before && kit.getCount() == 2);
          var hab = SealCheck.fill(probe, habB.getX(), habB.getY(), habB.getZ(), 2000);
          boolean leaking = hab.status() == SealCheck.Status.OPEN_TO_OUTSIDE;
          var result = use(player, kit, crack);
          var patched = SealCheck.fill(probe, habB.getX(), habB.getY(), habB.getZ(), 2000);
          ok &=
              SelfTest.check(
                  "landing.seal_kit_patches_the_crack",
                  "a kit on the cracked plate makes a hull plate, uses one kit, and Hab B reads SEALED",
                  "result="
                      + result
                      + " block="
                      + level.getBlockState(crack).getBlock().getDescriptionId()
                      + " kits="
                      + kit.getCount()
                      + " before="
                      + hab.status()
                      + " after="
                      + patched.status(),
                  leaking
                      && result.consumesAction()
                      && level.getBlockState(crack).is(HullBlocks.HULL_PLATE.get())
                      && kit.getCount() == 1
                      && patched.status() == SealCheck.Status.SEALED);
          int[] reading = O2TankBlock.reading(level);
          ok &=
              SelfTest.check(
                  "landing.tank_readout",
                  "a wall tank reads its own share: 14 O2 over 5 tanks is 3 of 40 (rounded)",
                  reading == null ? "no life model" : reading[0] + " of " + reading[1],
                  reading != null && reading[0] == 3 && reading[1] == 40);
          PlanetManager.resetForSelfTest(level);
          scrub(level, origin);
          if (ok) {
            h.succeed();
          } else {
            h.fail("landing kit checks failed; see MARS-SELFTEST lines");
          }
        });
  }
}
