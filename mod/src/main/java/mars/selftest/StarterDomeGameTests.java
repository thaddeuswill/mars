package mars.selftest;

import mars.dome.DomeSeals;
import mars.dome.DomeTemplate;
import mars.dome.SealCheck;
import mars.hull.HullBlocks;
import mars.hull.StarterDome;
import mars.hull.WorldProbe;
import mars.monitor.view.MonitorView;
import mars.planets.PlanetData;
import mars.planets.PlanetManager;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * Stage 3 slice 2 step A: the real starting dome (23 x 16, dome-layout.md) is placed with real
 * blocks in the test world and the real planet tick says which rooms are sealed. Own batch: the
 * tests of one batch run in parallel and share the one planet of the test world.
 */
@GameTestHolder("mars")
@PrefixGameTestTemplate(false)
public final class StarterDomeGameTests {
  private StarterDomeGameTests() {}

  private static String monitorWord(PlanetData d) {
    return MonitorView.of(d.sim()).rows().stream()
        .filter(r -> r.labelKey().equals("monitor.row.dome"))
        .map(MonitorView.Row::valueKey)
        .findFirst()
        .orElse("(no dome row)");
  }

  @GameTest(template = "empty", batch = "mars_start_dome", timeoutTicks = 300)
  public static void startingDomeSealedExceptTheDesignedLeak(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetData fresh = PlanetManager.resetForSelfTest(level);
    BlockPos origin = h.absolutePos(new BlockPos(0, 1, 0));
    StarterDome.place(level, origin, 5);
    BlockPos crack = origin.offset(DomeTemplate.BREACH_X, 2, DomeTemplate.BREACH_Z);
    BlockPos habB = origin.offset(3, 1, 11);
    h.runAfterDelay(
        5,
        () -> {
          PlanetData d = PlanetManager.data(level);
          var probe = new WorldProbe(level);
          var hab = SealCheck.fill(probe, habB.getX(), habB.getY(), habB.getZ(), 2000);
          boolean ok =
              SelfTest.check(
                  "dome.template_placed",
                  "the template places 3 oxygen makers (group 5), 5 tanks, 1 reclaimer, 1 battery, 1 panel and the cracked plate",
                  "makers="
                      + d.sim().machineCount("mars:oxygen_maker")
                      + " tanks="
                      + d.sim().machineCount("mars:o2_tank")
                      + " reclaimers="
                      + d.sim().machineCount("mars:water_reclaimer")
                      + " batteries="
                      + d.sim().machineCount("mars:battery")
                      + " panels="
                      + d.sim().machineCount("mars:solar_panel")
                      + " crack="
                      + level.getBlockState(crack).getBlock().getDescriptionId(),
                  d.sim().machineCount("mars:oxygen_maker") == 3
                      && d.sim().machineCount("mars:o2_tank") == 5
                      && d.sim().machineCount("mars:water_reclaimer") == 1
                      && d.sim().machineCount("mars:battery") == 1
                      && d.sim().machineCount("mars:solar_panel") == 1
                      && level.getBlockState(crack).is(HullBlocks.HULL_PLATE_CRACKED.get()));
          ok &=
              SelfTest.check(
                  "dome.template_required_rooms_sealed",
                  "every registered room (Monitor, tanks, makers, reclaimer) is SEALED; the leak is only in Hab B",
                  "domes="
                      + d.sim().domes().count()
                      + " overall="
                      + d.sim().domes().overall()
                      + " word="
                      + monitorWord(d),
                  d.sim().domes().count() >= 10
                      && d.sim().domes().overall() == DomeSeals.Status.SEALED
                      && monitorWord(d).equals("monitor.dome.sealed"));
          ok &=
              SelfTest.check(
                  "dome.template_habb_leaks_at_the_crack",
                  "Hab B reads OPEN_TO_OUTSIDE and the leak is next to the cracked plate",
                  "status=" + hab.status() + " leakAt=" + BlockPos.of(hab.leakAt()).toShortString(),
                  hab.status() == SealCheck.Status.OPEN_TO_OUTSIDE
                      && BlockPos.of(hab.leakAt()).distSqr(crack) <= 1);
          // Patch: break the cracked plate and put a hull plate there.
          level.setBlock(crack, HullBlocks.HULL_PLATE.get().defaultBlockState(), 3);
          var patched = SealCheck.fill(probe, habB.getX(), habB.getY(), habB.getZ(), 2000);
          ok &=
              SelfTest.check(
                  "dome.template_patch_seals_habb",
                  "after the plate is replaced Hab B reads SEALED (108 air blocks, 6 x 6 x 3)",
                  "status=" + patched.status() + " volume=" + patched.volume(),
                  patched.status() == SealCheck.Status.SEALED && patched.volume() == 108);
          // A door opened by the player joins two rooms; the planet tick reports the change.
          BlockPos door = origin.offset(7, 1, 4);
          var open = level.getBlockState(door).setValue(DoorBlock.OPEN, true);
          level.setBlock(door, open, 3);
          level.setBlock(
              door.above(), level.getBlockState(door.above()).setValue(DoorBlock.OPEN, true), 3);
          var joined =
              SealCheck.fill(probe, origin.getX() + 11, origin.getY() + 1, origin.getZ() + 4, 2000);
          ok &=
              SelfTest.check(
                  "dome.template_open_door_joins_rooms",
                  "an open interior door joins Ops and the shelter into one sealed volume",
                  "status=" + joined.status() + " volume=" + joined.volume(),
                  joined.status() == SealCheck.Status.SEALED && joined.volume() > 250);
          level.setBlock(door, Blocks.AIR.defaultBlockState(), 3);
          PlanetManager.resetForSelfTest(level); // leave a clean planet for the other tests
          if (ok) {
            h.succeed();
          } else {
            h.fail("starting dome checks failed; see MARS-SELFTEST lines");
          }
        });
  }
}
