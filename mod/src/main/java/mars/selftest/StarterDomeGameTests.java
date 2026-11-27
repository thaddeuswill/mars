package mars.selftest;

import mars.dome.DomeSeals;
import mars.dome.DomeTemplate;
import mars.dome.SealCheck;
import mars.hull.Airtight;
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

  /**
   * Clear the dome box and a margin of two blocks (and the solar pad), so blocks left by another
   * test of this world cannot close a hole the test makes or open a wall it relies on.
   */
  private static void scrub(ServerLevel level, BlockPos origin) {
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
    scrub(level, origin);
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
          BlockPos dish =
              origin.offset(DomeTemplate.DISH_X, DomeTemplate.ROOF_FIXTURE_Y, DomeTemplate.DISH_Z);
          BlockPos vent =
              origin.offset(DomeTemplate.VENT_X, DomeTemplate.ROOF_FIXTURE_Y, DomeTemplate.VENT_Z);
          BlockPos rim = origin.offset(0, 4, 5);
          BlockPos base = origin.offset(0, 1, 5);
          BlockPos ribbed = origin.offset(3, 4, 11);
          ok &=
              SelfTest.check(
                  "dome.habitat_variants_placed_and_airtight",
                  "base course, roof rim and ribbed glass are airtight; roof dish and vent stand on the roof and are not",
                  "base="
                      + level.getBlockState(base).getBlock().getDescriptionId()
                      + " rim="
                      + level.getBlockState(rim).getBlock().getDescriptionId()
                      + " ribbed="
                      + level.getBlockState(ribbed).getBlock().getDescriptionId()
                      + " dish="
                      + level.getBlockState(dish).getBlock().getDescriptionId()
                      + " vent="
                      + level.getBlockState(vent).getBlock().getDescriptionId(),
                  level.getBlockState(base).is(HullBlocks.HULL_PLATE_BASE.get())
                      && level.getBlockState(rim).is(HullBlocks.HULL_PLATE_RIM.get())
                      && level.getBlockState(ribbed).is(HullBlocks.HULL_GLASS_RIBBED.get())
                      && level.getBlockState(dish).is(HullBlocks.ROOF_DISH.get())
                      && level.getBlockState(vent).is(HullBlocks.ROOF_VENT.get())
                      && Airtight.test(level.getBlockState(base), level, base)
                      && Airtight.test(level.getBlockState(rim), level, rim)
                      && Airtight.test(level.getBlockState(ribbed), level, ribbed)
                      && !Airtight.test(level.getBlockState(dish), level, dish)
                      && !Airtight.test(level.getBlockState(vent), level, vent)
                      && !level.getBlockState(dish).isCollisionShapeFullBlock(level, dish)
                      && !level.getBlockState(vent).isCollisionShapeFullBlock(level, vent));
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
          var life = d.sim().life();
          ok &=
              SelfTest.check(
                  "life.template_start_fill",
                  "the template fills the tanks: O2 14 per player (group.size 1 in the test world), water 60, battery 5, capacity 200 from 5 tanks",
                  life == null
                      ? "no life support"
                      : "o2="
                          + life.o2()
                          + " water="
                          + life.water()
                          + " battery="
                          + d.sim().power().charge()
                          + " capacity="
                          + life.report().o2Capacity(),
                  life != null
                      && Math.abs(life.o2() - 14.0 * d.sim().pace().groupSize()) < 1e-9
                      && life.water() == 60
                      && Math.abs(d.sim().power().charge() - 5.0) < 1e-9
                      && life.report().o2Capacity() == 200);
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
          scrub(level, origin);
          if (ok) {
            h.succeed();
          } else {
            h.fail("starting dome checks failed; see MARS-SELFTEST lines");
          }
        });
  }

  @GameTest(template = "empty", batch = "mars_life_blocks", timeoutTicks = 400)
  public static void realBlocksMakeUseAndLeakAir(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetData fresh = PlanetManager.resetForSelfTest(level);
    BlockPos origin = h.absolutePos(new BlockPos(0, 1, 0));
    scrub(level, origin);
    StarterDome.place(level, origin, 1); // group 1: one pre-built maker
    BlockPos maker = origin.offset(16, 1, 1);
    BlockPos tank = origin.offset(1, 1, 7);
    BlockPos opsWall = origin.offset(8, 2, 0);
    h.runAfterDelay(
        5,
        () -> {
          PlanetData d = PlanetManager.data(level);
          var sim = d.sim();
          sim.model().advanceSols(1.2 + 1.0); // one sol after the Landing
          double o2After1 = sim.life().o2();
          boolean ok =
              SelfTest.check(
                  "life.blocks_make_air",
                  "one maker, one player: after one sol past the Landing the tanks hold 14 + 2 = 16",
                  "o2=" + o2After1 + " net=" + sim.life().report().o2Net(),
                  Math.abs(o2After1 - 16.0) < 0.1 && sim.life().states().sealed());
          level.destroyBlock(maker, false);
          sim.model().advanceSols(0.5);
          double o2NoMaker = sim.life().o2();
          ok &=
              SelfTest.check(
                  "life.broken_maker_starves",
                  "break the only maker: 0.5 sol of use (4 units) with nothing made",
                  "o2=" + o2NoMaker + " makers=" + sim.machineCount("mars:oxygen_maker"),
                  sim.machineCount("mars:oxygen_maker") == 0
                      && Math.abs(o2NoMaker - (o2After1 - 4.0)) < 0.1);
          level.destroyBlock(tank, false);
          ok &=
              SelfTest.check(
                  "life.broken_tank_lowers_capacity",
                  "break one of five tanks: capacity 200 -> 160",
                  "capacity=" + sim.life().report().o2Capacity(),
                  sim.life().report().o2Capacity() == 160);
          level.destroyBlock(opsWall, false); // a hole in Ops north wall
          h.runAfterDelay(
              4,
              () -> {
                var sim2 = PlanetManager.data(level).sim();
                double before = sim2.life().o2();
                boolean leaking = !sim2.life().states().sealed();
                sim2.model().advanceSols(0.5);
                double lost = before - sim2.life().o2();
                boolean ok2 =
                    SelfTest.check(
                        "life.breach_drains_air",
                        "a hole in a required room: sealed turns false and 0.5 sol costs 4 (use) + 5 (leak) units",
                        "leaking="
                            + leaking
                            + " lost="
                            + lost
                            + " overall="
                            + sim2.domes().overall()
                            + " domes="
                            + sim2.domes().count()
                            + " outside="
                            + level.getBlockState(opsWall.north()).getBlock().getDescriptionId()
                            + " h="
                            + level.getHeight(
                                net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING,
                                opsWall.getX(),
                                opsWall.getZ() - 1)
                            + " y="
                            + opsWall.getY()
                            + " fills="
                            + sim2.domes().fillsRun()
                            + " opsWallBlock="
                            + level.getBlockState(opsWall).getBlock().getDescriptionId(),
                        leaking && Math.abs(lost - 9.0) < 0.2);
                PlanetManager.resetForSelfTest(level);
                scrub(level, origin);
                if (ok2) {
                  h.succeed();
                } else {
                  h.fail("life checks failed; see MARS-SELFTEST lines");
                }
              });
          if (!ok) {
            h.fail("life checks failed; see MARS-SELFTEST lines");
          }
        });
  }

  @GameTest(template = "empty", batch = "mars_blackout", timeoutTicks = 200)
  public static void emptyTanksBlackOutAPlayerAndNothingIsLost(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetManager.resetForSelfTest(level);
    var player =
        net.neoforged.neoforge.common.util.FakePlayerFactory.get(
            level,
            new com.mojang.authlib.GameProfile(
                java.util.UUID.fromString("6d617273-0000-4000-8000-0000000000b1"), "[Blackout]"));
    player.getInventory().add(new net.minecraft.world.item.ItemStack(Blocks.DIRT.asItem(), 5));
    float healthBefore = player.getHealth();
    boolean blacked = false;
    int secondsToBlackout = 0;
    for (int s = 1; s <= 130 && !blacked; s++) {
      blacked = mars.planets.BlackoutEvents.secondFor(player, level, true);
      secondsToBlackout = s;
    }
    boolean ok =
        SelfTest.check(
            "life.blackout_after_120_seconds",
            "a survival player short of breath blacks out on the 120th second, is blinded, and loses no item or health",
            "blacked="
                + blacked
                + " after="
                + secondsToBlackout
                + " blind="
                + player.hasEffect(net.minecraft.world.effect.MobEffects.BLINDNESS)
                + " dirt="
                + player.getInventory().countItem(Blocks.DIRT.asItem())
                + " health="
                + player.getHealth()
                + "/"
                + healthBefore,
            blacked
                && secondsToBlackout == 120
                && player.hasEffect(net.minecraft.world.effect.MobEffects.BLINDNESS)
                && player.getInventory().countItem(Blocks.DIRT.asItem()) == 5
                && player.getHealth() == healthBefore);
    // Breathing again resets: 100 s short, then fine, then 100 s short does not black out.
    boolean again = false;
    for (int s = 0; s < 100; s++) {
      again |= mars.planets.BlackoutEvents.secondFor(player, level, true);
    }
    mars.planets.BlackoutEvents.secondFor(player, level, false);
    for (int s = 0; s < 100; s++) {
      again |= mars.planets.BlackoutEvents.secondFor(player, level, true);
    }
    ok &=
        SelfTest.check(
            "life.blackout_count_resets",
            "100 s short of breath, one second of air, 100 s short again: no blackout",
            "blackedOut=" + again,
            !again);
    if (ok) {
      h.succeed();
    } else {
      h.fail("blackout checks failed; see MARS-SELFTEST lines");
    }
  }

  /** Block light (0..15) of the air cell at template (x, y, z), or -1 if the cell is not air. */
  private static int blockLight(ServerLevel level, BlockPos origin, int x, int y, int z) {
    BlockPos p = origin.offset(x, y, z);
    return level.getBlockState(p).isAir()
        ? level.getBrightness(net.minecraft.world.level.LightLayer.BLOCK, p)
        : -1;
  }

  /** Lowest block light over the air cells at y = {@code y} in a room box, and where it is. */
  private static String worst(
      ServerLevel level, BlockPos origin, int y, int x0, int x1, int z0, int z1) {
    int min = 99;
    String at = "none";
    for (int x = x0; x <= x1; x++) {
      for (int z = z0; z <= z1; z++) {
        int l = blockLight(level, origin, x, y, z);
        if (l >= 0 && l < min) {
          min = l;
          at = x + "," + z;
        }
      }
    }
    return min + "@" + at;
  }

  private static int worstValue(String w) {
    return Integer.parseInt(w.substring(0, w.indexOf('@')));
  }

  /**
   * Hull Light Panels (Artist handoff, ARCHITECTURE 2.20 addendum): reads the real light engine.
   * Without lamps a window-less room is dark; with them every floor cell of the machine bay is lit.
   * The lit rooms (shelter, Ops) are measured too, so the lamp level is set against a real number.
   */
  @GameTest(template = "empty", batch = "mars_dome_light", timeoutTicks = 400)
  public static void ceilingLightsLightTheMachineBay(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetManager.resetForSelfTest(level);
    BlockPos origin = h.absolutePos(new BlockPos(0, 1, 0));
    scrub(level, origin);
    StarterDome.place(level, origin, 5);
    // Baseline: swap every lamp for a plain hull plate and let the light engine settle.
    for (int[] l : DomeTemplate.LAMPS) {
      level.setBlock(
          origin.offset(l[0], 4, l[1]), HullBlocks.HULL_PLATE.get().defaultBlockState(), 3);
    }
    h.runAfterDelay(
        60,
        () -> {
          String bayDark = worst(level, origin, 1, 16, 21, 1, 7);
          String shelter = worst(level, origin, 1, 1, 6, 1, 7);
          String ops = worst(level, origin, 1, 8, 14, 1, 7);
          String opsMax =
              String.valueOf(
                  blockLight(level, origin, 9, 1, 1) + "/" + blockLight(level, origin, 11, 3, 4));
          for (int[] l : DomeTemplate.LAMPS) {
            level.setBlock(
                origin.offset(l[0], 4, l[1]), HullBlocks.HULL_LIGHT.get().defaultBlockState(), 3);
          }
          h.runAfterDelay(
              60,
              () -> {
                String bay = worst(level, origin, 1, 16, 21, 1, 7);
                String shelterLit = worst(level, origin, 1, 1, 6, 1, 7);
                String opsLit = worst(level, origin, 1, 8, 14, 1, 7);
                String bayHead = worst(level, origin, 2, 16, 21, 1, 7);
                String habB = worst(level, origin, 1, 1, 6, 9, 14);
                String storage = worst(level, origin, 1, 16, 21, 9, 14);
                String suit = worst(level, origin, 1, 8, 14, 9, 10);
                String lock = worst(level, origin, 1, 10, 12, 12, 14);
                int under = blockLight(level, origin, 17, 3, 2);
                boolean ok =
                    SelfTest.check(
                        "dome.lights_measured_lit_rooms",
                        "measure block light (0..15) at floor cells: rooms with no lamp (baseline, lamps swapped for plates)",
                        "bayNoLamps="
                            + bayDark
                            + " shelter="
                            + shelter
                            + " ops="
                            + ops
                            + " opsSample="
                            + opsMax,
                        true);
                ok &=
                    SelfTest.check(
                        "dome.lights_machine_bay_lit",
                        "with 30 Hull Light Panels (level 13) the worst floor cell of every room is at least 8, the cell under a lamp is at least 12",
                        "bayWorst="
                            + bay
                            + " shelter="
                            + shelterLit
                            + " ops="
                            + opsLit
                            + " bayHead="
                            + bayHead
                            + " underLamp="
                            + under
                            + " habB="
                            + habB
                            + " storage="
                            + storage
                            + " suit="
                            + suit
                            + " airlock="
                            + lock,
                        worstValue(bay) >= 8
                            && worstValue(shelterLit) >= 8
                            && worstValue(opsLit) >= 8
                            && under >= 12
                            && worstValue(habB) >= 8
                            && worstValue(storage) >= 8
                            && worstValue(suit) >= 8
                            && worstValue(lock) >= 8
                            && worstValue(bay) > worstValue(bayDark));
                var probe = new WorldProbe(level);
                var r =
                    SealCheck.fill(
                        probe, origin.getX() + 18, origin.getY() + 1, origin.getZ() + 4, 2000);
                ok &=
                    SelfTest.check(
                        "dome.lights_keep_the_seal",
                        "the machine bay with its lamps still reads SEALED and the lamp block is airtight",
                        "status=" + r.status() + " volume=" + r.volume(),
                        r.status() == SealCheck.Status.SEALED
                            && Airtight.test(
                                level.getBlockState(origin.offset(17, 4, 2)),
                                level,
                                origin.offset(17, 4, 2)));
                PlanetManager.resetForSelfTest(level);
                scrub(level, origin);
                if (ok) {
                  h.succeed();
                } else {
                  h.fail("light checks failed; see MARS-SELFTEST lines");
                }
              });
        });
  }

  @GameTest(template = "empty", batch = "mars_legacy_dome")
  public static void aWorldSavedBeforeLifeSupportGetsNoDome(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetData d = PlanetManager.resetForSelfTest(level);
    d.sim().useLegacyLifeSupport(); // what a schema 6 save loads as
    BlockPos spot = StarterDome.ensure(level, 1);
    boolean ok =
        SelfTest.check(
            "dome.legacy_world_gets_no_dome",
            "a planet loaded from a schema 6 save: ensure() returns null, places nothing and records nothing",
            "spot=" + spot + " recorded=" + StarterDome.data(level).origin(),
            spot == null && StarterDome.data(level).origin() == null);
    PlanetManager.resetForSelfTest(level);
    if (ok) {
      h.succeed();
    } else {
      h.fail("legacy dome check failed; see MARS-SELFTEST lines");
    }
  }
}
