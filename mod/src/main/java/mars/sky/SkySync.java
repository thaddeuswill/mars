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

  private SkySync() {}

  /** Called every tick of a planet level that is running; sends at most once per second. */
  public static void onPlanetTick(ServerLevel level, PlanetData data) {
    if (level.getGameTime() % PERIOD_TICKS != 0) {
      return;
    }
    double p = data.sim().model().snapshot().get(Ids.PRESSURE);
    String key = level.dimension().location().toString();
    if (mars.visual.SkyState.shouldSend(LAST_SENT.get(key), p)) {
      LAST_SENT.put(key, p);
      PacketDistributor.sendToPlayersInDimension(level, new SkyPayload(p));
    }
  }

  public static void sendTo(ServerPlayer player) {
    ServerLevel level = player.serverLevel();
    if (PlanetManager.isPlanet(level)) {
      double p = PlanetManager.data(level).sim().model().snapshot().get(Ids.PRESSURE);
      PacketDistributor.sendToPlayer(player, new SkyPayload(p));
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
