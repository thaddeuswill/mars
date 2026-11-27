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
    h.setBlock(at, MarsMachines.REGOLITH_OUTGASSER.get());
    data.sim().model().advanceSols(3);
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
            same && tag.getInt("schema") == 1);
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
    data.sim().model().set(Ids.PRESSURE, 0.6);
    finish(h, ok, "planetSavesAndReloads");
  }

  @GameTest(template = "empty", batch = "mars_monitor")
  public static void monitorShowsRealValuesAndPayloadSurvivesTheWire(GameTestHelper h) {
    ServerLevel level = h.getLevel();
    PlanetData data = PlanetManager.data(level);
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
}
