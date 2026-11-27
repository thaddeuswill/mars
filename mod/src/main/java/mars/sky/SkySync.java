package mars.sky;

import java.util.HashMap;
import java.util.Map;
import mars.core.Ids;
import mars.planets.PlanetData;
import mars.planets.PlanetManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * Sends the planet's pressure to the players on it, for the client-side sky (Stage 2 slice 6).
 *
 * <p>Cost per tick on a planet: one counter check. Once per second per planet: one map read, one
 * comparison and, only when the pressure moved by more than 0.1 %, one 8-byte packet per player on
 * that planet. A player gets the current value on login, respawn and arriving on a planet.
 */
public final class SkySync {
  static final int PERIOD_TICKS = 20;
  private static final Map<String, Double> LAST_SENT = new HashMap<>();
  private static final Map<String, float[]> LAST_STORM = new HashMap<>();

  /** The pre-storm haze starts this long before onset, in sols (plan 5.2; mirrors sky.json). */
  static final double HAZE_WINDOW_SOLS = 0.25;

  private SkySync() {}

  /** Called every tick of a planet level that is running; sends at most once per second. */
  public static void onPlanetTick(ServerLevel level, PlanetData data) {
    if (level.getGameTime() % PERIOD_TICKS != 0) {
      return;
    }
    double p = data.sim().model().snapshot().get(Ids.PRESSURE);
    String key = level.dimension().location().toString();
    float[] st = stormNow(data);
    float[] last = LAST_STORM.get(key);
    boolean stormMoved =
        last == null || mars.visual.SkyState.shouldSendStorm(last[0], last[1], st[0], st[1]);
    if (stormMoved || mars.visual.SkyState.shouldSend(LAST_SENT.get(key), p)) {
      LAST_SENT.put(key, p);
      LAST_STORM.put(key, st);
      PacketDistributor.sendToPlayersInDimension(level, payload(level, p, st));
    }
  }

  /** {strength, haze}: strength is the active storm's cap; haze rises over the warning's end. */
  static float[] stormNow(PlanetData data) {
    var storm = data.sim().storm();
    if (storm == null) {
      return new float[] {0, 0};
    }
    if (storm.active()) {
      return new float[] {(float) storm.activeCap(), 0};
    }
    if (storm.phase() == mars.hazards.storm.StormSchedule.Phase.WARNING) {
      double left = storm.solsUntilStart(data.sim().model().sols());
      return new float[] {0, (float) Math.max(0, Math.min(1, 1 - left / HAZE_WINDOW_SOLS))};
    }
    return new float[] {0, 0};
  }

  private static SkyPayload payload(ServerLevel level, double p, float[] st) {
    var terrain = mars.dimension.PlanetTerrainData.get(level.dimension().location());
    float wind = terrain == null ? -1 : (float) terrain.windTowardDeg();
    return new SkyPayload(p, st[0], st[1], wind);
  }

  public static void sendTo(ServerPlayer player) {
    ServerLevel level = player.serverLevel();
    if (PlanetManager.isPlanet(level)) {
      double p = PlanetManager.data(level).sim().model().snapshot().get(Ids.PRESSURE);
      PacketDistributor.sendToPlayer(
          player, payload(level, p, stormNow(PlanetManager.data(level))));
    }
  }

  public static void onChangedDimension(PlayerEvent.PlayerChangedDimensionEvent e) {
    if (e.getEntity() instanceof ServerPlayer p) {
      sendTo(p);
    }
  }

  public static void onLoggedIn(PlayerEvent.PlayerLoggedInEvent e) {
    if (e.getEntity() instanceof ServerPlayer p) {
      sendTo(p);
    }
  }

  public static void onRespawn(PlayerEvent.PlayerRespawnEvent e) {
    if (e.getEntity() instanceof ServerPlayer p) {
      sendTo(p);
    }
  }
}
