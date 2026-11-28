package mars.selftest;

import mars.machines.MarsMachines;
import mars.machines.SolarPanelBlock;
import mars.planets.PlanetData;
import mars.planets.PlanetManager;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * Slice 5 with real blocks: panel dust over time (step A), the brush (B), and the dust storm (D).
 * Each test is in its own batch (tests in one batch run at the same time and would reset each
 * other's planet) and starts from a fresh planet ({@code resetForSelfTest}) and uses simulation
 * time directly ({@code advanceSols}), so the checks do not wait for real minutes; the planet
 * tick's own sync ({@code syncPanels}) is called by hand where a test needs the block to follow at
 * once.
 */
@GameTestHolder("mars")
@PrefixGameTestTemplate(false)
public final class HazardGameTests {
  private static final double TOL = 1e-9;

  private HazardGameTests() {}

  private static void finish(GameTestHelper h, boolean ok, String what) {
    if (ok) {
      h.succeed();
    } else {
      h.fail(what + " failed; see MARS-SELFTEST lines");
    }
  }

  static int countFillers(ServerLevel level, BlockPos abs) {
    int fillers = 0;
    for (int dx = -1; dx <= 1; dx++) {
      for (int dz = -1; dz <= 1; dz++) {
        if ((dx != 0 || dz != 0)
            && level.getBlockState(abs.offset(dx, 0, dz)).getBlock()
                instanceof mars.machines.SolarPanelCanopyBlock) {
          fillers++;
        }
      }
    }
    return fillers;
  }

  static int stage(ServerLevel level, BlockPos abs) {
    return level.getBlockState(abs).getValue(SolarPanelBlock.DUST_STAGE);
  }

  /** A real panel dusts by itself, the block follows, the grid sees it, the fillers stay. */
  @GameTest(template = "empty", batch = "mars_hazard_a1")
  public static void panelDustsByItself(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetData data = PlanetManager.resetForSelfTest(level);
    var sim = data.sim();
    BlockPos rel = new BlockPos(6, 1, 6);
    h.setBlock(rel, MarsMachines.SOLAR_PANEL.get());
    BlockPos abs = h.absolutePos(rel);
    boolean ok =
        SelfTest.check(
            "dust.starts_clean",
            "a new real panel is stage 0 and makes the full 5 units",
            "stage=" + stage(level, abs) + " supply=" + sim.power().supplyPerSol(),
            stage(level, abs) == 0 && Math.abs(sim.power().supplyPerSol() - 5.0) < TOL);
    sim.model().advanceSols(6.1);
    PlanetManager.syncPanels(level, data);
    int fillers = countFillers(level, abs);
    ok &=
        SelfTest.check(
            "dust.block_follows_time",
            "after 6.1 sols the block shows stage 2, the grid makes 4.1 units, 8 fillers remain",
            "stage="
                + stage(level, abs)
                + " model="
                + sim.panelStage(abs.asLong())
                + " supply="
                + sim.power().supplyPerSol()
                + " fillers="
                + fillers,
            stage(level, abs) == 2
                && sim.panelStage(abs.asLong()) == 2
                && Math.abs(sim.power().supplyPerSol() - 4.1) < 1e-6
                && fillers == 8);
    // The planet's NBT round trip keeps the dust, and the planet seed.
    CompoundTag tag = data.save(new CompoundTag(), level.registryAccess());
    PlanetData back =
        PlanetData.load(level.dimension().location().toString(), tag, level.registryAccess());
    ok &=
        SelfTest.check(
            "dust.nbt_round_trip",
            "after save and load the panel is still stage 2 and the seed is the same",
            "stage="
                + back.sim().panelStage(abs.asLong())
                + " seed="
                + back.sim().planetSeed()
                + " (was "
                + sim.planetSeed()
                + ")",
            back.sim().panelStage(abs.asLong()) == 2
                && back.sim().planetSeed() == sim.planetSeed()
                && back.sim().planetSeed() != 0
                && back.sim().toState().equals(sim.toState()));
    // /setblock of a stage tells the planet.
    level.setBlock(abs, level.getBlockState(abs).setValue(SolarPanelBlock.DUST_STAGE, 4), 3);
    ok &=
        SelfTest.check(
            "dust.setblock_sets_the_planet",
            "a block set to stage 4 makes the planet's record stage 4 (3.0 units)",
            "model=" + sim.panelStage(abs.asLong()) + " supply=" + sim.power().supplyPerSol(),
            sim.panelStage(abs.asLong()) == 4 && Math.abs(sim.power().supplyPerSol() - 3.0) < TOL);
    h.destroyBlock(rel);
    finish(h, ok, "panelDustsByItself");
  }

  /** A panel in an unloaded chunk keeps dusting; its block is brought up to date on load. */
  @GameTest(template = "empty", batch = "mars_hazard_a2", timeoutTicks = 800)
  public static void panelInUnloadedChunkDustsAndSyncsOnLoad(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetData data = PlanetManager.resetForSelfTest(level);
    var sim = data.sim();
    BlockPos far = new BlockPos(52_000, 100, 52_000);
    int cx = far.getX() >> 4;
    int cz = far.getZ() >> 4;
    level.setChunkForced(cx, cz, true);
    level.getChunk(cx, cz);
    level.setBlockAndUpdate(far, MarsMachines.SOLAR_PANEL.get().defaultBlockState());
    level.setChunkForced(cx, cz, false);
    h.runAfterDelay(
        150,
        () -> {
          boolean unloaded = !level.hasChunkAt(far);
          sim.model().advanceSols(9.5);
          int modelStage = sim.panelStage(far.asLong());
          // The unloaded block cannot have changed; load the chunk again and tick once.
          level.setChunkForced(cx, cz, true);
          level.getChunk(cx, cz);
          h.runAfterDelay(
              40,
              () -> {
                boolean ok =
                    SelfTest.check(
                        "dust.unloaded_chunk_then_load",
                        "chunk really unloaded, the model reached stage 3, and on load the block"
                            + " shows stage 3",
                        "unloaded="
                            + unloaded
                            + " model="
                            + modelStage
                            + " block="
                            + stage(level, far),
                        unloaded && modelStage == 3 && stage(level, far) == 3);
                level.setBlockAndUpdate(
                    far, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState());
                level.setChunkForced(cx, cz, false);
                finish(h, ok, "panelInUnloadedChunkDustsAndSyncsOnLoad");
              });
        });
  }

  /** The brush: any cell of the array cleans the panel, uses one of 64, posts the event. */
  @GameTest(template = "empty", batch = "mars_hazard_b")
  public static void brushCleansAPanel(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetData data = PlanetManager.resetForSelfTest(level);
    var sim = data.sim();
    BlockPos rel = new BlockPos(6, 1, 6);
    h.setBlock(rel, MarsMachines.SOLAR_PANEL.get());
    BlockPos abs = h.absolutePos(rel);
    level.setBlock(abs, level.getBlockState(abs).setValue(SolarPanelBlock.DUST_STAGE, 4), 3);
    var player =
        net.neoforged.neoforge.common.util.FakePlayerFactory.get(
            level, new com.mojang.authlib.GameProfile(java.util.UUID.randomUUID(), "brusher"));
    net.minecraft.world.item.ItemStack brush =
        new net.minecraft.world.item.ItemStack(MarsMachines.PANEL_BRUSH.get());
    int[] events = {0, -1};
    java.util.function.Consumer<mars.machines.PanelCleanedEvent> listener =
        e -> {
          events[0]++;
          events[1] = e.stageBefore();
        };
    net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(listener);
    boolean ok =
        SelfTest.check(
            "brush.item_has_64_uses",
            "the panel brush wears out after 64 uses",
            "maxDamage=" + brush.getMaxDamage(),
            brush.getMaxDamage() == 64);
    int before =
        mars.machines.PanelBrushItem.brush(player, level, abs.offset(1, 0, -1), brush); // a filler
    PlanetManager.syncPanels(level, data);
    ok &=
        SelfTest.check(
            "brush.cleans_through_a_filler",
            "brushing a canopy cell cleans the centre: stage 4 -> 0, block follows, supply 5.0, one"
                + " use, one event with stage 4",
            "before="
                + before
                + " block="
                + stage(level, abs)
                + " supply="
                + sim.power().supplyPerSol()
                + " damage="
                + brush.getDamageValue()
                + " events="
                + events[0]
                + "/"
                + events[1],
            before == 4
                && stage(level, abs) == 0
                && Math.abs(sim.power().supplyPerSol() - 5.0) < TOL
                && brush.getDamageValue() == 1
                && events[0] == 1
                && events[1] == 4);
    int again = mars.machines.PanelBrushItem.brush(player, level, abs, brush);
    ok &=
        SelfTest.check(
            "brush.clean_panel_costs_nothing",
            "brushing a clean panel does nothing and uses no durability",
            "result=" + again + " damage=" + brush.getDamageValue() + " events=" + events[0],
            again == -1 && brush.getDamageValue() == 1 && events[0] == 1);
    net.neoforged.neoforge.common.NeoForge.EVENT_BUS.unregister(listener);
    h.destroyBlock(rel);
    finish(h, ok, "brushCleansAPanel");
  }

  /**
   * The Monitor's "Panel dust" line is held for a player whose Landing is not over, shown (with one
   * "New on the Monitor" line and one reveal event) once it is, and not announced twice.
   */
  @GameTest(template = "empty", batch = "mars_hazard_c")
  public static void dustLineWaitsForTheLanding(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetData data = PlanetManager.resetForSelfTest(level);
    BlockPos rel = new BlockPos(6, 1, 6);
    h.setBlock(rel, MarsMachines.SOLAR_PANEL.get());
    BlockPos abs = h.absolutePos(rel);
    level.setBlock(abs, level.getBlockState(abs).setValue(SolarPanelBlock.DUST_STAGE, 2), 3);
    var player =
        net.neoforged.neoforge.common.util.FakePlayerFactory.get(
            level, new com.mojang.authlib.GameProfile(java.util.UUID.randomUUID(), "keeper"));
    mars.story.landing.LandingData.get(level).forgetForTest(player.getUUID());
    int[] events = {0};
    java.util.function.Consumer<mars.monitor.LineRevealedEvent> listener = e -> events[0]++;
    net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(listener);
    var raw = mars.monitor.MonitorSender.viewFor(level);
    boolean rawHas = raw.hasCause(mars.monitor.view.MonitorView.DUST_LINE);
    boolean heldDuring =
        !mars.monitor.MonitorSender.forPlayer(player, raw)
            .hasCause(mars.monitor.view.MonitorView.DUST_LINE);
    mars.story.landing.LandingService.skipAll(player);
    boolean shown =
        mars.monitor.MonitorSender.forPlayer(player, raw)
            .hasCause(mars.monitor.view.MonitorView.DUST_LINE);
    mars.monitor.MonitorSender.forPlayer(player, raw);
    boolean ok =
        SelfTest.check(
            "monitor.dust_line_waits_for_landing",
            "line is in the server view; held while the Landing is not over; shown after skip; one"
                + " reveal event only",
            "raw=" + rawHas + " held=" + heldDuring + " shown=" + shown + " events=" + events[0],
            rawHas && heldDuring && shown && events[0] == 1);
    net.neoforged.neoforge.common.NeoForge.EVENT_BUS.unregister(listener);
    mars.story.landing.LandingData.get(level).forgetForTest(player.getUUID());
    h.destroyBlock(rel);
    finish(h, ok, "dustLineWaitsForTheLanding");
  }

  /**
   * The storm with a real panel: warning, onset and a cut in supply, the brush mid-storm, the
   * storm-end stages in the real block, the events on the bus in order, and a save and load in the
   * middle of the storm.
   */
  @GameTest(template = "empty", batch = "mars_hazard_d")
  public static void stormCutsSupplyThenDustsThePanel(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetData data = PlanetManager.resetForSelfTest(level);
    var sim = data.sim();
    sim.setLandingDone(true);
    BlockPos rel = new BlockPos(6, 1, 6);
    h.setBlock(rel, MarsMachines.SOLAR_PANEL.get());
    BlockPos abs = h.absolutePos(rel);
    level.setBlock(abs, level.getBlockState(abs).setValue(SolarPanelBlock.DUST_STAGE, 1), 3);
    var player =
        net.neoforged.neoforge.common.util.FakePlayerFactory.get(
            level, new com.mojang.authlib.GameProfile(java.util.UUID.randomUUID(), "storm"));
    java.util.List<String> seen = new java.util.ArrayList<>();
    java.util.function.Consumer<mars.planets.StormEvent> listener =
        e -> seen.add(e.kind().name() + (e.fastForward() ? "(ff)" : ""));
    net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(listener);
    double clean = sim.power().supplyPerSol();
    boolean ok = sim.scheduleStorm(0.2, 0.75);
    sim.model().advanceSols(0.01);
    PlanetManager.announceStorm(level, data);
    boolean warned = sim.stormWarned();
    double beforeOnset = sim.power().supplyPerSol();
    sim.model().advanceSols(0.3); // onset at 0.2 sol, ramp in 0.05
    PlanetManager.announceStorm(level, data);
    double inStorm = sim.power().supplyPerSol();
    ok &=
        SelfTest.check(
            "storm.warned_then_cuts_supply",
            "warned with supply unchanged (4.6), then at full strength the panel makes 1.84",
            "warned="
                + warned
                + " before="
                + beforeOnset
                + " clean="
                + clean
                + " inStorm="
                + inStorm
                + " strength="
                + sim.stormStrength(),
            warned
                && Math.abs(beforeOnset - 4.6) < 1e-6
                && Math.abs(inStorm - 5.0 * 0.92 * 0.4) < 1e-6
                && sim.stormStrength() == 1.0);
    // Brush mid-storm: the panel goes to 0, and still gets the storm-end stages afterwards.
    net.minecraft.world.item.ItemStack brush =
        new net.minecraft.world.item.ItemStack(MarsMachines.PANEL_BRUSH.get());
    mars.machines.PanelBrushItem.brush(player, level, abs, brush);
    boolean stageDuring = sim.panelStage(abs.asLong()) == 0;
    // A save and load in the middle of the storm.
    CompoundTag tag = data.save(new CompoundTag(), level.registryAccess());
    PlanetData back =
        PlanetData.load(level.dimension().location().toString(), tag, level.registryAccess());
    boolean mid =
        back.sim().stormMode() == mars.hazards.storm.StormScheduler.Mode.ACTIVE
            && back.sim().toState().equals(sim.toState());
    sim.model().advanceSols(0.7); // past the end (0.2 + 0.75 = 0.95 sol)
    PlanetManager.announceStorm(level, data);
    PlanetManager.syncPanels(level, data);
    ok &=
        SelfTest.check(
            "storm.brush_midstorm_save_and_end_dust",
            "brushed to 0 mid-storm, save/load keeps the ACTIVE storm, after the end the block"
                + " shows stage 2 and strength is 0",
            "brushedTo0="
                + stageDuring
                + " savedActive="
                + mid
                + " block="
                + stage(level, abs)
                + " strength="
                + sim.stormStrength()
                + " events="
                + seen,
            stageDuring
                && mid
                && stage(level, abs) == 2
                && sim.stormStrength() == 0.0
                && seen.equals(
                    java.util.List.of(
                        "WARNED(ff)",
                        "REMINDER(ff)",
                        "ONSET(ff)",
                        "CLEARING(ff)",
                        "ENDED(ff)",
                        "SCHEDULED(ff)")));
    net.neoforged.neoforge.common.NeoForge.EVENT_BUS.unregister(listener);
    h.destroyBlock(rel);
    finish(h, ok, "stormCutsSupplyThenDustsThePanel");
  }
}
