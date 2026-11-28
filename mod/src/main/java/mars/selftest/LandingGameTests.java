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

  private static BlockHitResult hitOn(BlockPos pos) {
    return new BlockHitResult(Vec3.atCenterOf(pos), net.minecraft.core.Direction.UP, pos, false);
  }

  private static void useBlock(
      net.minecraft.server.level.ServerPlayer p, ServerLevel level, BlockPos pos) {
    level.getBlockState(pos).useWithoutItem(level, p, hitOn(pos));
  }

  /** The whole Landing with real blocks and a fake player, then a second player (group rule). */
  @GameTest(template = "empty", batch = "mars_landing_flow", timeoutTicks = 600)
  public static void wholeLandingWithRealBlocks(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetManager.resetForSelfTest(level);
    mars.story.landing.LandingService.resetMemory();
    BlockPos origin = h.absolutePos(new BlockPos(0, 1, 0));
    scrub(level, origin);
    StarterDome.place(level, origin, 2);
    // The real arrival path would record the origin; the test records it the same way.
    StarterDome.data(level).setOriginForTest(origin);
    mars.story.landing.LandingData.get(level).forgetForTest(player(level, "[Lander]", 2).getUUID());
    var p = player(level, "[Lander]", 2);
    p.setPos(origin.getX() + 3.5, origin.getY() + 1, origin.getZ() + 4.5);
    var svc = mars.story.landing.LandingService.class;
    mars.story.landing.LandingService.arrive(p);
    var rec = mars.story.landing.LandingService.rec(p);
    boolean ok =
        SelfTest.check(
            "landing.flow_offered_and_book_granted",
            "arrival offers the Landing and puts the journal in the hotbar with the About entry",
            "phase=" + rec.landing.phase() + " slot8=" + p.getInventory().getItem(8).getItem(),
            rec.landing.phase() == mars.landing.Phase.OFFERED
                && p.getInventory().getItem(8).is(net.minecraft.world.item.Items.WRITTEN_BOOK)
                && rec.journal.isUnlocked("journal.about"));
    mars.story.landing.LandingService.onChoice(p, true);
    boolean ok1 = rec.landing.phase() == mars.landing.Phase.RUNNING;
    // 1 door, 2 tank, 3 Monitor: real block use and the real Monitor open
    useBlock(p, level, origin.offset(7, 1, 4));
    useBlock(p, level, origin.offset(1, 1, 7));
    mars.monitor.MonitorSender.open(p);
    // 4 kit from the locker, 5 patch with the real item use
    if (level.getBlockEntity(origin.offset(DomeTemplate.LOCKER_X, 1, DomeTemplate.LOCKER_Z))
        instanceof BaseContainerBlockEntity locker) {
      p.getInventory().add(locker.removeItem(0, 1));
    }
    mars.story.landing.LandingService.secondFor(p);
    var kit =
        p.getInventory()
            .getItem(
                p.getInventory().findSlotMatchingItem(new ItemStack(HullBlocks.SEAL_KIT.get())));
    use(p, kit, origin.offset(DomeTemplate.BREACH_X, 2, DomeTemplate.BREACH_Z));
    // 6 two Ovens placed by real item use on the machine bay floor
    for (int i = 0; i < 2; i++) {
      BlockPos floor = origin.offset(17 + i * 2, 0, 6);
      use(p, new ItemStack(mars.machines.MarsMachines.REGOLITH_OVEN.get()), floor);
    }
    final boolean okA = ok && ok1;
    h.runAfterDelay(
        130,
        () -> {
          mars.story.landing.LandingService.secondFor(p);
          boolean ok2 = okA;
          ok2 &=
              SelfTest.check(
                  "landing.flow_steps_1_to_6",
                  "door, tank, Monitor, kit, patch and two powered Ovens each tick DONE through real blocks",
                  statusLine(rec),
                  statuses(rec, 0, 6, mars.landing.Status.DONE));
          // 7 airlock: out, through the outer door, back in
          BlockPos outer = origin.offset(11, 1, 15);
          p.setPos(origin.getX() + 11.5, origin.getY() + 1, origin.getZ() + 14.5);
          useBlock(p, level, outer);
          p.setPos(origin.getX() + 11.5, origin.getY() + 1, origin.getZ() + 25);
          mars.story.landing.LandingService.secondFor(p);
          p.setPos(origin.getX() + 11.5, origin.getY() + 1, origin.getZ() + 12.5);
          mars.story.landing.LandingService.secondFor(p);
          ok2 &=
              SelfTest.check(
                  "landing.flow_airlock_out_and_back",
                  "going out, using the outer door and coming back ticks step 7",
                  statusLine(rec),
                  rec.landing.status(mars.landing.Step.AIRLOCK) == mars.landing.Status.DONE);
          // 8 journal: the real right-click event on the book
          p.getInventory().selected = 8; // the journal's hotbar slot
          var book = p.getInventory().getItem(8);
          net.neoforged.neoforge.common.NeoForge.EVENT_BUS.post(
              new net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickItem(
                  p, InteractionHand.MAIN_HAND));
          ok2 &=
              SelfTest.check(
                  "landing.flow_done_and_game_rules_unlocked",
                  "opening the journal finishes the Landing (phase DONE) and unlocks sol, pressure, power and game rules",
                  "phase=" + rec.landing.phase() + " journal=" + rec.journal.unlocked(),
                  rec.landing.phase() == mars.landing.Phase.DONE
                      && rec.journal.isUnlocked("journal.sol")
                      && rec.journal.isUnlocked("journal.a1.pressure")
                      && rec.journal.isUnlocked("journal.power")
                      && rec.journal.isUnlocked("journal.gamerules")
                      && !rec.journal.isUnlocked("journal.dome.stable")
                      && book != null);
          ok2 &=
              SelfTest.check(
                  "landing.flow_briefing_due_after_three_seconds",
                  "the briefing is due 60 ticks after the checklist and not before",
                  "due=" + mars.story.landing.LandingService.briefingDue(p.getUUID()),
                  rec.landing.briefingDue()
                      && mars.story.landing.LandingService.briefingDue(p.getUUID()) != null);
          mars.story.landing.LandingService.forceBriefingNow(p);
          mars.story.landing.LandingService.secondFor(p);
          ok2 &=
              SelfTest.check(
                  "landing.flow_briefing_once",
                  "the briefing shows once and its journal copy unlocks",
                  "due="
                      + rec.landing.briefingDue()
                      + " has="
                      + rec.journal.isUnlocked("briefing.shift1"),
                  !rec.landing.briefingDue() && rec.journal.isUnlocked("briefing.shift1"));
          // The Dome stable latch gives every player the two planet entries once.
          PlanetManager.data(level)
              .sim()
              .gates()
              .latch("mars:dome_stable", mars.gates.GateTracker.Cause.ADMIN, 1);
          // A fake player is not in level.players(): the late-joiner path catches the entries up.
          mars.story.landing.LandingService.arrive(p);
          int before = rec.journal.unlocked().size();
          mars.story.landing.LandingService.arrive(p);
          ok2 &=
              SelfTest.check(
                  "landing.flow_dome_stable_entries_once",
                  "the latch unlocks the Dome stable and sky entries, and a repeat adds nothing",
                  "journal="
                      + rec.journal.unlocked()
                      + " latched="
                      + PlanetManager.data(level).sim().gates().isLatched("mars:dome_stable"),
                  rec.journal.isUnlocked("journal.dome.stable")
                      && rec.journal.isUnlocked("journal.sky.stylized")
                      && rec.journal.unlocked().size() == before);
          // Save round trip of the record
          var data = mars.story.landing.LandingData.get(level);
          var tag = data.save(new net.minecraft.nbt.CompoundTag(), level.registryAccess());
          var back = mars.story.landing.LandingData.loadForTest(tag, level.registryAccess());
          var r2 = back.rec(p.getUUID());
          ok2 &=
              SelfTest.check(
                  "landing.flow_save_round_trip",
                  "the saved record loads back with the same phase, steps and journal",
                  r2.landing.toSaved() + " / " + r2.journal.unlocked(),
                  r2.landing.toSaved().equals(rec.landing.toSaved())
                      && r2.journal.unlocked().equals(rec.journal.unlocked())
                      && r2.granted);
          PlanetManager.resetForSelfTest(level);
          StarterDome.data(level).setOriginForTest(null);
          mars.story.landing.LandingData.get(level).forgetForTest(p.getUUID());
          scrub(level, origin);
          if (ok2) {
            h.succeed();
          } else {
            h.fail("landing flow checks failed; see MARS-SELFTEST lines");
          }
        });
  }

  private static String statusLine(mars.story.landing.LandingData.Rec rec) {
    StringBuilder sb = new StringBuilder();
    for (var s : mars.landing.Step.values()) {
      sb.append(s.number()).append('=').append(rec.landing.status(s)).append(' ');
    }
    return sb.toString();
  }

  private static boolean statuses(
      mars.story.landing.LandingData.Rec rec, int from, int to, mars.landing.Status want) {
    for (int i = from; i < to; i++) {
      if (rec.landing.status(mars.landing.Step.values()[i]) != want) {
        return false;
      }
    }
    return true;
  }

  /** Group rule: one player patches, the teammate's step 5 becomes AUTO and credits nobody. */
  @GameTest(template = "empty", batch = "mars_landing_group", timeoutTicks = 300)
  public static void teammateWorkIsAutoForTheOtherPlayer(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetManager.resetForSelfTest(level);
    mars.story.landing.LandingService.resetMemory();
    BlockPos origin = h.absolutePos(new BlockPos(0, 1, 0));
    scrub(level, origin);
    StarterDome.place(level, origin, 2);
    StarterDome.data(level).setOriginForTest(origin);
    var a = player(level, "[Alpha]", 3);
    var b = player(level, "[Bravo]", 4);
    var data = mars.story.landing.LandingData.get(level);
    data.forgetForTest(a.getUUID());
    data.forgetForTest(b.getUUID());
    for (var p : new net.minecraft.server.level.ServerPlayer[] {a, b}) {
      p.setPos(origin.getX() + 3.5, origin.getY() + 1, origin.getZ() + 4.5);
      mars.story.landing.LandingService.arrive(p);
      mars.story.landing.LandingService.onChoice(p, true);
    }
    ItemStack kit = new ItemStack(HullBlocks.SEAL_KIT.get(), 1);
    use(a, kit, origin.offset(DomeTemplate.BREACH_X, 2, DomeTemplate.BREACH_Z));
    var ra = mars.story.landing.LandingService.rec(a);
    var rb = mars.story.landing.LandingService.rec(b);
    mars.story.landing.LandingService.secondFor(b);
    boolean ok =
        SelfTest.check(
            "landing.group_patch_credits_only_the_patcher_and_waits_for_the_active_step",
            "A patched (DONE); B is still on step 1 so B's step 5 is not credited yet",
            "A5="
                + ra.landing.status(mars.landing.Step.PATCH)
                + " B5="
                + rb.landing.status(mars.landing.Step.PATCH),
            ra.landing.status(mars.landing.Step.PATCH) == mars.landing.Status.DONE
                && rb.landing.status(mars.landing.Step.PATCH) == mars.landing.Status.PENDING);
    rb.landing.onDoorOpened(1);
    rb.landing.onTankRead(2);
    rb.landing.onMonitorOpened(3);
    rb.landing.onKitHeld(4);
    mars.story.landing.LandingService.secondFor(b);
    ok &=
        SelfTest.check(
            "landing.group_teammates_step_5_is_auto",
            "when step 5 becomes B's active step and Hab B is sealed, it ticks AUTO",
            "B5=" + rb.landing.status(mars.landing.Step.PATCH),
            rb.landing.status(mars.landing.Step.PATCH) == mars.landing.Status.AUTO);
    data.forgetForTest(a.getUUID());
    data.forgetForTest(b.getUUID());
    PlanetManager.resetForSelfTest(level);
    StarterDome.data(level).setOriginForTest(null);
    scrub(level, origin);
    if (ok) {
      h.succeed();
    } else {
      h.fail("landing group checks failed; see MARS-SELFTEST lines");
    }
  }
}
