package mars.selftest;

import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import java.util.UUID;
import mars.core.Ids;
import mars.index.IndexDefinitions;
import mars.machines.MarsMachines;
import mars.machines.RegolithOutgasserBlock;
import mars.monitor.MonitorPayload;
import mars.monitor.MonitorSender;
import mars.monitor.view.MonitorView;
import mars.planets.PlanetData;
import mars.planets.PlanetManager;
import mars.planets.SessionControl;
import mars.planets.SessionData;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * Stage 2 slice 1 checks in the GameTest server: planet data from the real reload path, a real
 * Outgasser block raising pressure through real ticks, saving, the Monitor payload, and the freeze
 * guard. Each test is in its own batch because tests in one batch run in parallel and these share
 * the one planet of the test world. In this server any level counts as a planet (test seam: {@code
 * mars.selftest.anyLevelIsPlanet}, set only by the gameTestServer run configuration).
 */
@GameTestHolder("mars")
@PrefixGameTestTemplate(false)
public final class PlanetGameTests {
  private PlanetGameTests() {}

  private static final double TOL = 1e-9;

  private static final String PANEL = "mars:solar_panel";
  private static final long FAKE_PANEL = 9_000_000_000_000L;

  /** Give the shared test planet clean power (n panels without blocks); undo with unpower. */
  private static void power(PlanetData data, int n) {
    for (int i = 0; i < n; i++) {
      data.sim().addMachine(PANEL, FAKE_PANEL + i);
    }
  }

  private static void unpower(PlanetData data, int n) {
    for (int i = 0; i < n; i++) {
      data.sim().removeMachine(PANEL, FAKE_PANEL + i);
    }
  }

  private static void finish(GameTestHelper h, boolean ok, String what) {
    if (ok) {
      h.succeed();
    } else {
      h.fail(what + " failed; see MARS-SELFTEST lines");
    }
  }

  @GameTest(template = "empty", batch = "mars_data")
  public static void planetDataLoadedFromFiles(GameTestHelper h) {
    boolean loaded = IndexDefinitions.isLoaded();
    boolean ok =
        SelfTest.check(
            "planet.data_loaded",
            "definitions loaded by the reload listener",
            "isLoaded=" + loaded,
            loaded);
    if (loaded) {
      var d = IndexDefinitions.require();
      double start =
          d.index().parameters().stream()
              .filter(p -> p.id().equals(Ids.PRESSURE))
              .findFirst()
              .orElseThrow()
              .start();
      var out = d.machines().get(RegolithOutgasserBlock.TYPE);
      double rate = out == null ? Double.NaN : out.contributions().get(0).ratePerSol();
      ok &=
          SelfTest.check(
              "planet.pressure_start",
              "0.6 kPa from parameters/pressure.json",
              "start=" + start,
              Math.abs(start - 0.6) < TOL);
      ok &=
          SelfTest.check(
              "planet.outgasser_rate",
              "0.10 kPa/sol from machines/regolith_outgasser.json",
              "rate=" + rate,
              Math.abs(rate - 0.10) < TOL);
    }
    finish(h, ok, "planetDataLoadedFromFiles");
  }

  @GameTest(template = "empty", batch = "mars_pressure", timeoutTicks = 600)
  public static void outgasserRaisesPressureThroughRealTicks(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetData data = PlanetManager.data(level);
    double p0 = data.sim().model().snapshot().get(Ids.PRESSURE);
    long t0 = data.sim().model().simTicks();
    int before = data.sim().machineCount(RegolithOutgasserBlock.TYPE);
    power(data, 2); // two clean panels = 10 units per sol, enough for two Outgassers (8)
    h.setBlock(new BlockPos(0, 1, 0), MarsMachines.REGOLITH_OUTGASSER.get());
    h.setBlock(new BlockPos(2, 1, 0), MarsMachines.REGOLITH_OUTGASSER.get());
    int after = data.sim().machineCount(RegolithOutgasserBlock.TYPE);
    boolean ok =
        SelfTest.check(
            "outgasser.placed_registers",
            "2 more machines on the planet",
            "before="
                + before
                + " after="
                + after
                + " net rate/sol="
                + data.sim().model().netRatePerSol(Ids.PRESSURE),
            after == before + 2);
    double rateWithTwo = data.sim().model().netRatePerSol(Ids.PRESSURE);
    h.runAfterDelay(
        230,
        () -> {
          long dTicks = data.sim().model().simTicks() - t0;
          double dp = data.sim().model().snapshot().get(Ids.PRESSURE) - p0;
          double expected = rateWithTwo * dTicks / 24_000.0;
          boolean ok2 =
              SelfTest.check(
                  "outgasser.raises_pressure",
                  "pressure rises by net rate x ticks/24000 = "
                      + expected
                      + " kPa over "
                      + dTicks
                      + " sim ticks",
                  "rose by "
                      + dp
                      + " kPa over "
                      + dTicks
                      + " sim ticks (steps="
                      + dTicks / 100
                      + ")",
                  dTicks >= 200 && dTicks % 100 == 0 && Math.abs(dp - expected) < TOL);
          h.destroyBlock(new BlockPos(0, 1, 0));
          double rateOne = data.sim().model().netRatePerSol(Ids.PRESSURE);
          ok2 &=
              SelfTest.check(
                  "outgasser.break_unregisters",
                  "net rate falls to half after breaking one (" + rateWithTwo / 2 + ")",
                  "rate="
                      + rateOne
                      + " machines="
                      + data.sim().machineCount(RegolithOutgasserBlock.TYPE),
                  Math.abs(rateOne - rateWithTwo / 2) < TOL
                      && data.sim().machineCount(RegolithOutgasserBlock.TYPE) == before + 1);
          h.destroyBlock(new BlockPos(2, 1, 0));
          boolean back = data.sim().machineCount(RegolithOutgasserBlock.TYPE) == before;
          ok2 &=
              SelfTest.check(
                  "outgasser.cleanup",
                  "machine count back to " + before,
                  "count=" + data.sim().machineCount(RegolithOutgasserBlock.TYPE),
                  back);
          unpower(data, 2);
          data.sim().model().set(Ids.PRESSURE, p0); // put the shared planet back
          finish(h, ok2, "outgasserRaisesPressureThroughRealTicks");
        });
    if (!ok) {
      h.fail("outgasser placement did not register; see MARS-SELFTEST lines");
    }
  }

  @GameTest(template = "empty", batch = "mars_persist")
  public static void planetSavesAndReloads(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetData data = PlanetManager.data(level);
    BlockPos at = new BlockPos(0, 1, 0);
    power(data, 1);
    h.setBlock(at, MarsMachines.REGOLITH_OUTGASSER.get());
    data.sim().model().advanceSols(2); // under the 3 sol Dome stable hold, so no gate latches here
    CompoundTag tag = data.save(new CompoundTag(), level.registryAccess());
    PlanetData back = PlanetData.load(tag, level.registryAccess());
    boolean same = data.sim().toState().equals(back.sim().toState());
    boolean ok =
        SelfTest.check(
            "save.round_trip",
            "state after NBT save and load equals the state before",
            "same="
                + same
                + " pressure="
                + back.sim().model().snapshot().get(Ids.PRESSURE)
                + " nbtKeys="
                + tag.getAllKeys(),
            same && tag.getInt("schema") == 3);
    double rate = back.sim().model().netRatePerSol(Ids.PRESSURE);
    ok &=
        SelfTest.check(
            "save.machines_return",
            "reloaded planet still has the placed machine's rate",
            "net rate="
                + rate
                + " machines="
                + back.sim().machineCount(RegolithOutgasserBlock.TYPE),
            rate >= 0.10 - TOL);
    CompoundTag future = tag.copy();
    future.putInt("schema", 99);
    String refused;
    try {
      PlanetData.load(future, level.registryAccess());
      refused = "loaded (wrong)";
    } catch (IllegalStateException e) {
      refused = "refused: " + e.getMessage();
    }
    ok &=
        SelfTest.check(
            "save.newer_schema_refused",
            "a schema 99 save is refused",
            refused,
            refused.startsWith("refused"));
    h.destroyBlock(at);
    unpower(data, 1);
    data.sim().model().set(Ids.PRESSURE, 0.6);
    finish(h, ok, "planetSavesAndReloads");
  }

  @GameTest(template = "empty", batch = "mars_monitor")
  public static void monitorShowsRealValuesAndPayloadSurvivesTheWire(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetData data = PlanetManager.data(level);
    power(data, 2);
    h.setBlock(new BlockPos(0, 1, 0), MarsMachines.REGOLITH_OUTGASSER.get());
    h.setBlock(new BlockPos(2, 1, 0), MarsMachines.REGOLITH_OUTGASSER.get());
    double p0 = data.sim().model().snapshot().get(Ids.PRESSURE);
    int others = data.sim().machineCount(RegolithOutgasserBlock.TYPE) - 2;
    MonitorView view = MonitorSender.viewFor(level);
    MonitorView.Row pr = view.rows().get(0);
    boolean ok =
        SelfTest.check(
            "monitor.pressure_row",
            "pressure row shows the planet's pressure and a rising trend with an Outgasser cause line",
            "value="
                + pr.value()
                + " trend="
                + pr.trend()
                + " rate="
                + pr.trendRate()
                + " causes="
                + pr.causes(),
            pr.trend() == MonitorView.Trend.RISING
                && pr.causes().size() == 1
                && pr.causes().get(0).count() == 2 + others
                && pr.causes().get(0).sourceKey().equals("block.mars.regolith_outgasser"));
    MonitorPayload sent = new MonitorPayload(view);
    RegistryFriendlyByteBuf buf =
        new RegistryFriendlyByteBuf(Unpooled.buffer(), level.registryAccess());
    MonitorPayload.CODEC.encode(buf, sent);
    int bytes = buf.readableBytes();
    MonitorPayload got = MonitorPayload.CODEC.decode(buf);
    ok &=
        SelfTest.check(
            "monitor.payload_round_trip",
            "decoded payload equals the sent one",
            "bytes=" + bytes + " equal=" + got.equals(sent) + " leftover=" + buf.readableBytes(),
            got.equals(sent) && buf.readableBytes() == 0);

    FakePlayer player =
        FakePlayerFactory.get(
            level,
            new GameProfile(
                UUID.fromString("6d617273-0000-4000-8000-000000000002"), "[MonitorTest]"));
    String result;
    try {
      InteractionResult r =
          level
              .getBlockState(h.absolutePos(new BlockPos(0, 1, 0)))
              .useWithoutItem(
                  level,
                  player,
                  new BlockHitResult(
                      Vec3.ZERO,
                      net.minecraft.core.Direction.UP,
                      h.absolutePos(new BlockPos(0, 1, 0)),
                      false));
      result = "outgasser use -> " + r;
    } catch (RuntimeException e) {
      result = "threw " + e;
    }
    ok &=
        SelfTest.check(
            "monitor.use_other_block_is_harmless",
            "using an Outgasser does nothing and doesn't throw",
            result,
            !result.startsWith("threw"));
    h.setBlock(new BlockPos(4, 1, 0), MarsMachines.PLANETARY_MONITOR.get());
    BlockPos mon = h.absolutePos(new BlockPos(4, 1, 0));
    try {
      InteractionResult r =
          level
              .getBlockState(mon)
              .useWithoutItem(
                  level,
                  player,
                  new BlockHitResult(Vec3.ZERO, net.minecraft.core.Direction.UP, mon, false));
      result = "monitor use -> " + r;
    } catch (RuntimeException e) {
      result = "threw " + e;
    }
    ok &=
        SelfTest.check(
            "monitor.use_block_sends",
            "using the Monitor block is handled on the server (CONSUME) without throwing",
            result,
            result.startsWith("monitor use -> ")
                && !result.contains("PASS")
                && result.contains("CONSUME"));
    h.destroyBlock(new BlockPos(0, 1, 0));
    h.destroyBlock(new BlockPos(2, 1, 0));
    h.destroyBlock(new BlockPos(4, 1, 0));
    unpower(data, 2);
    data.sim().model().set(Ids.PRESSURE, p0);
    finish(h, ok, "monitor");
  }

  /**
   * The freeze guard, driven by hand inside one tick: freezing the whole test server stops the
   * GameTest runner's own delays (the first version of this test hung until the watchdog killed
   * it), so the tick hook is called directly while the tick-rate manager is frozen and unfrozen.
   */
  @GameTest(template = "empty", batch = "mars_freeze")
  public static void frozenLevelDoesNotAdvanceThePlanet(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetData data = PlanetManager.data(level);
    var tm = level.getServer().tickRateManager();
    var event = new net.neoforged.neoforge.event.tick.LevelTickEvent.Post(() -> true, level);
    var s0 = data.sim().toState();
    boolean ok;
    try {
      tm.setFrozen(true);
      tm.tick(); // runsNormally() is refreshed by the manager's own tick
      for (int i = 0; i < 250; i++) {
        mars.planets.PlanetEvents.onLevelTick(event);
      }
      var s1 = data.sim().toState();
      ok =
          SelfTest.check(
              "freeze.stops_clock",
              "250 tick-hook calls while frozen change nothing",
              "simTicks "
                  + s0.simTicks()
                  + " -> "
                  + s1.simTicks()
                  + ", pending "
                  + s0.pendingTicks()
                  + " -> "
                  + s1.pendingTicks(),
              s0.simTicks() == s1.simTicks() && s0.pendingTicks() == s1.pendingTicks());
    } finally {
      tm.setFrozen(false);
      tm.tick();
    }
    var s2a = data.sim().toState();
    for (int i = 0; i < 250; i++) {
      mars.planets.PlanetEvents.onLevelTick(event);
    }
    var s2 = data.sim().toState();
    // 250 calls cross a step boundary 2 or 3 times depending on where the counter started.
    long expectedTicks = ((s2a.pendingTicks() + 250) / 100) * 100L;
    ok &=
        SelfTest.check(
            "freeze.resumes",
            "250 hook calls when not frozen advance the clock by "
                + expectedTicks
                + " sim ticks (counter started at "
                + s2a.pendingTicks()
                + ")",
            "simTicks " + s2a.simTicks() + " -> " + s2.simTicks(),
            s2.simTicks() - s2a.simTicks() == expectedTicks);
    finish(h, ok, "frozenLevelDoesNotAdvanceThePlanet");
  }

  /**
   * Pause (Stage 2 slice 2, invariant I-3): with the session paused the tick hook changes nothing,
   * both with the flag alone and with the vanilla freeze that {@link SessionControl} also sets. Run
   * by hand inside one tick for the same reason as the freeze test. Third-party machines stopping
   * under {@code /tick freeze} is vanilla's rule and is NOT tested here (Mekanism and Create are
   * not on the test classpath).
   */
  @GameTest(template = "empty", batch = "mars_session")
  public static void pausedSessionDoesNotAdvanceThePlanet(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    var server = level.getServer();
    PlanetData data = PlanetManager.data(level);
    var clock = SessionData.get(server).clock();
    var tm = server.tickRateManager();
    var event = new net.neoforged.neoforge.event.tick.LevelTickEvent.Post(() -> true, level);
    boolean ok = true;
    try {
      // 1. Flag alone (server not frozen): the planet must still stop.
      clock.pause();
      var a0 = data.sim().toState();
      for (int i = 0; i < 250; i++) {
        mars.planets.PlanetEvents.onLevelTick(event);
      }
      var a1 = data.sim().toState();
      ok &=
          SelfTest.check(
              "session.flag_stops_clock",
              "250 hook calls with the pause flag set change nothing",
              "simTicks "
                  + a0.simTicks()
                  + " -> "
                  + a1.simTicks()
                  + ", pending "
                  + a0.pendingTicks()
                  + " -> "
                  + a1.pendingTicks(),
              a0.equals(a1));
      clock.resume();
      // 2. Through the real control: flag and freeze together, then resume.
      int sessionBefore = clock.sessionNumber();
      boolean paused = SessionControl.pause(server, "gametest");
      tm.tick();
      boolean frozen = !tm.runsNormally();
      var b0 = data.sim().toState();
      for (int i = 0; i < 250; i++) {
        mars.planets.PlanetEvents.onLevelTick(event);
      }
      var b1 = data.sim().toState();
      ok &=
          SelfTest.check(
              "session.pause_control",
              "SessionControl.pause sets the flag and freezes the server; the planet does not move",
              "changed="
                  + paused
                  + " paused="
                  + clock.paused()
                  + " frozen="
                  + frozen
                  + " simTicks "
                  + b0.simTicks()
                  + " -> "
                  + b1.simTicks(),
              paused && clock.paused() && frozen && b0.equals(b1));
      boolean resumed = SessionControl.resume(server, "gametest");
      tm.tick();
      var c0 = data.sim().toState();
      for (int i = 0; i < 250; i++) {
        mars.planets.PlanetEvents.onLevelTick(event);
      }
      var c1 = data.sim().toState();
      long expected = ((c0.pendingTicks() + 250) / 100) * 100L;
      ok &=
          SelfTest.check(
              "session.resume_control",
              "resume unfreezes, starts session "
                  + (sessionBefore + 1)
                  + ", and the clock runs by "
                  + expected,
              "changed="
                  + resumed
                  + " session="
                  + clock.sessionNumber()
                  + " running="
                  + clock.running()
                  + " simTicks "
                  + c0.simTicks()
                  + " -> "
                  + c1.simTicks(),
              resumed
                  && clock.sessionNumber() == sessionBefore + 1
                  && tm.runsNormally()
                  && c1.simTicks() - c0.simTicks() == expected);
      // 3. The saved form keeps the pause across a restart.
      clock.pause();
      var saved = SessionData.get(server).save(new CompoundTag(), level.registryAccess());
      var back =
          mars.session.SessionClock.restore(
              saved.getInt("schema"), saved.getBoolean("paused"), saved.getInt("sessionNumber"));
      ok &=
          SelfTest.check(
              "session.pause_survives_save",
              "a paused session saved and restored is still paused, same session number",
              "paused=" + back.paused() + " session=" + back.sessionNumber() + " nbt=" + saved,
              back.paused() && back.sessionNumber() == clock.sessionNumber());
    } finally {
      SessionControl.resume(server, "gametest-cleanup");
      tm.tick();
    }
    finish(h, ok, "pausedSessionDoesNotAdvanceThePlanet");
  }

  /**
   * Owner-accepted rule (BACKLOG #32): an Outgasser keeps contributing while its chunk is unloaded.
   * A real block is placed in a far chunk, the chunk is released, and after it has really unloaded
   * the planet must still count the machine and still rise by its rate.
   */
  @GameTest(template = "empty", batch = "mars_unloaded", timeoutTicks = 600)
  public static void outgasserKeepsWorkingWhileItsChunkIsUnloaded(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetData data = PlanetManager.data(level);
    BlockPos far = new BlockPos(48_000, 100, 48_000);
    int cx = far.getX() >> 4;
    int cz = far.getZ() >> 4;
    int before = data.sim().machineCount(RegolithOutgasserBlock.TYPE);
    power(data, 1);
    level.setChunkForced(cx, cz, true);
    level.getChunk(cx, cz); // load it now
    level.setBlockAndUpdate(far, MarsMachines.REGOLITH_OUTGASSER.get().defaultBlockState());
    boolean placed = data.sim().machineCount(RegolithOutgasserBlock.TYPE) == before + 1;
    level.setChunkForced(cx, cz, false);
    h.runAfterDelay(
        150,
        () -> {
          boolean unloaded = !level.hasChunkAt(far);
          int during = data.sim().machineCount(RegolithOutgasserBlock.TYPE);
          double rate = data.sim().model().netRatePerSol(Ids.PRESSURE);
          long t0 = data.sim().model().simTicks();
          double p0 = data.sim().model().snapshot().get(Ids.PRESSURE);
          h.runAfterDelay(
              230,
              () -> {
                long dTicks = data.sim().model().simTicks() - t0;
                double dp = data.sim().model().snapshot().get(Ids.PRESSURE) - p0;
                double expected = rate * dTicks / 24_000.0;
                boolean ok =
                    SelfTest.check(
                        "outgasser.chunk_unloaded",
                        "chunk really unloaded, machine still counted, pressure rises by "
                            + expected
                            + " kPa over "
                            + dTicks
                            + " sim ticks",
                        "placed="
                            + placed
                            + " unloaded="
                            + unloaded
                            + " machines="
                            + during
                            + " (before "
                            + before
                            + ") rate="
                            + rate
                            + " rose by "
                            + dp,
                        placed
                            && unloaded
                            && during == before + 1
                            && rate >= 0.1 - TOL
                            && dTicks >= 200
                            && Math.abs(dp - expected) < TOL);
                // clean up: load the chunk, remove the block, restore pressure
                level.setChunkForced(cx, cz, true);
                level.getChunk(cx, cz);
                level.setBlockAndUpdate(
                    far, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState());
                level.setChunkForced(cx, cz, false);
                ok &=
                    SelfTest.check(
                        "outgasser.chunk_unloaded_cleanup",
                        "machine count back to " + before,
                        "count=" + data.sim().machineCount(RegolithOutgasserBlock.TYPE),
                        data.sim().machineCount(RegolithOutgasserBlock.TYPE) == before);
                unpower(data, 1);
                data.sim().model().set(Ids.PRESSURE, p0);
                finish(h, ok, "outgasserKeepsWorkingWhileItsChunkIsUnloaded");
              });
        });
  }

  /**
   * Slice 3 with real blocks: an Outgasser alone does nothing; a real solar panel (3 x 3 array)
   * powers it; dust cuts the rate; a battery adds capacity; the Monitor's Power row says which.
   */
  @GameTest(template = "empty", batch = "mars_power")
  public static void outgasserNeedsPowerFromRealBlocks(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetData data = PlanetManager.data(level);
    var sim = data.sim();
    double r0 = sim.model().netRatePerSol(Ids.PRESSURE);
    BlockPos outPos = new BlockPos(0, 1, 0);
    BlockPos panelPos = new BlockPos(6, 1, 6);
    h.setBlock(outPos, MarsMachines.REGOLITH_OUTGASSER.get());
    double alone = sim.model().netRatePerSol(Ids.PRESSURE) - r0;
    String state1 =
        MonitorSender.viewFor(level).rows().stream()
            .filter(r -> r.labelKey().equals("monitor.row.power"))
            .map(r -> String.valueOf(r.valueKey()))
            .findFirst()
            .orElse("none");
    boolean ok =
        SelfTest.check(
            "power.outgasser_alone_unpowered",
            "an Outgasser with no panel adds 0 kPa/sol and the Power row says unpowered",
            "added " + alone + " row=" + state1,
            Math.abs(alone) < TOL && state1.equals("monitor.power.unpowered"));
    h.setBlock(panelPos, MarsMachines.SOLAR_PANEL.get());
    double clean = sim.model().netRatePerSol(Ids.PRESSURE) - r0;
    ok &=
        SelfTest.check(
            "power.real_panel_powers_it",
            "a real clean solar panel gives the Outgasser its full 0.10 kPa/sol",
            "added " + clean + " supply=" + sim.power().supplyPerSol(),
            Math.abs(clean - 0.10) < TOL && Math.abs(sim.power().supplyPerSol() - 5.0) < TOL);
    var dusty =
        MarsMachines.SOLAR_PANEL
            .get()
            .defaultBlockState()
            .setValue(mars.machines.SolarPanelBlock.DUST_STAGE, 4);
    level.setBlock(h.absolutePos(panelPos), dusty, 3);
    double dust4 = sim.model().netRatePerSol(Ids.PRESSURE) - r0;
    ok &=
        SelfTest.check(
            "power.dust_stage_cuts_output",
            "a stage 4 panel (60 %) gives 3.0 of 4 units, so 0.075 kPa/sol",
            "added " + dust4 + " supply=" + sim.power().supplyPerSol(),
            Math.abs(dust4 - 0.075) < TOL && Math.abs(sim.power().supplyPerSol() - 3.0) < TOL);
    h.setBlock(new BlockPos(2, 1, 0), MarsMachines.BATTERY_BLOCK.get());
    ok &=
        SelfTest.check(
            "power.battery_adds_capacity",
            "a placed battery adds 10 units of capacity",
            "capacity=" + sim.power().capacity(),
            Math.abs(sim.power().capacity() - 10.0) < TOL);
    h.destroyBlock(new BlockPos(2, 1, 0));
    h.destroyBlock(panelPos);
    h.destroyBlock(outPos);
    ok &=
        SelfTest.check(
            "power.cleanup",
            "rate and supply are back to the start",
            "rate "
                + sim.model().netRatePerSol(Ids.PRESSURE)
                + " (was "
                + r0
                + ") supply="
                + sim.power().supplyPerSol()
                + " capacity="
                + sim.power().capacity(),
            Math.abs(sim.model().netRatePerSol(Ids.PRESSURE) - r0) < TOL
                && sim.power().capacity() == 0.0);
    finish(h, ok, "outgasserNeedsPowerFromRealBlocks");
  }

  /**
   * Slice 4 with real blocks and the real reload path: the gate files are loaded; a survival player
   * cannot place the Regolith Outgasser before "Dome stable"; a creative player can (admin route);
   * after the gate latches (real power for 3 sols) a survival player can; the gate stays latched
   * when the power is removed; the latched state survives an NBT save and load.
   */
  @GameTest(template = "empty", batch = "mars_gate")
  public static void domeStableUnlocksTheOutgasser(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetData data = PlanetManager.resetForSelfTest(level); // gates cannot be un-latched
    var sim = data.sim();
    var defs = IndexDefinitions.require().gates();
    boolean ok =
        SelfTest.check(
            "gate.files_loaded",
            "two gate files read by the reload listener: dome_stable and phase_2_begins",
            "gates=" + defs.stream().map(mars.gates.GateDef::id).toList(),
            defs.size() == 2 && !sim.gates().isLatched("mars:dome_stable"));
    FakePlayer player =
        FakePlayerFactory.get(
            level,
            new GameProfile(UUID.fromString("6d617273-0000-4000-8000-000000000003"), "[GateTest]"));
    java.util.function.Supplier<Boolean> canPlace =
        () -> {
          var ctx =
              new net.minecraft.world.item.context.BlockPlaceContext(
                  player,
                  net.minecraft.world.InteractionHand.MAIN_HAND,
                  new net.minecraft.world.item.ItemStack(MarsMachines.REGOLITH_OUTGASSER.get()),
                  new BlockHitResult(
                      Vec3.atCenterOf(h.absolutePos(new BlockPos(0, 0, 0))),
                      net.minecraft.core.Direction.UP,
                      h.absolutePos(new BlockPos(0, 0, 0)),
                      false));
          return MarsMachines.REGOLITH_OUTGASSER.get().getStateForPlacement(ctx) != null;
        };
    boolean before = canPlace.get();
    ok &=
        SelfTest.check(
            "gate.survival_refused_before",
            "a survival player cannot place the Outgasser before Dome stable",
            "canPlace=" + before + " creative=" + player.isCreative(),
            !before && !player.isCreative());
    player.setGameMode(net.minecraft.world.level.GameType.CREATIVE);
    boolean creative = canPlace.get();
    player.setGameMode(net.minecraft.world.level.GameType.SURVIVAL);
    ok &=
        SelfTest.check(
            "gate.creative_route",
            "a creative player can place it (admin route)",
            "canPlace=" + creative,
            creative);
    power(data, 1);
    sim.model().advanceSols(3.2);
    boolean after = canPlace.get();
    ok &=
        SelfTest.check(
            "gate.unlocked_after_three_sols",
            "after 3 sols of life support the gate has latched and a survival player can place it",
            "latched=" + sim.gates().isLatched("mars:dome_stable") + " canPlace=" + after,
            after && sim.gates().isLatched("mars:dome_stable"));
    unpower(data, 1);
    sim.model().advanceSols(5);
    ok &=
        SelfTest.check(
            "gate.stays_latched",
            "the gate stays latched after power is removed (gates lock in)",
            "latched=" + sim.gates().isLatched("mars:dome_stable"),
            sim.gates().isLatched("mars:dome_stable"));
    PlanetData back =
        PlanetData.load(
            data.save(new CompoundTag(), level.registryAccess()), level.registryAccess());
    ok &=
        SelfTest.check(
            "gate.nbt_round_trip",
            "latched gate and cause survive an NBT save and load",
            "latched=" + back.sim().gates().isLatched("mars:dome_stable"),
            back.sim().gates().isLatched("mars:dome_stable")
                && back.sim().gates().state("mars:dome_stable").cause()
                    == mars.gates.GateTracker.Cause.REACHED);
    data.sim().model().set(Ids.PRESSURE, 0.6);
    finish(h, ok, "domeStableUnlocksTheOutgasser");
  }
}
