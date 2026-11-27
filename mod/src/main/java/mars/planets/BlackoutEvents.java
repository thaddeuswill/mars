package mars.planets;

import java.util.UUID;
import mars.hull.StarterDome;
import mars.life.BlackoutTimer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

/**
 * The player side of empty tanks (balance sheet 2d.6): while the planet's stored oxygen is 0 every
 * survival player on it is short of breath (slowed); after {@code blackoutAfterSeconds} (120) they
 * black out: the screen fades for the wake delay, they wake in the shelter beside the bunks, and
 * nothing is lost (no items, no health, no death). A <b>game rule</b>, labelled so in the journal.
 * Suits do not exist yet, so the 30-second "empty suit outside" case is not built.
 *
 * <p>Cost: once a second per planet, one loop over the planet's players (a map update each while
 * short of breath, nothing otherwise). The counters live in memory only and are never saved.
 */
public final class BlackoutEvents {
  private static final BlackoutTimer<UUID> TIMER = new BlackoutTimer<>(120);

  private BlackoutEvents() {}

  /** Called every planet tick; acts once a second. */
  public static void onPlanetTick(ServerLevel level, PlanetData data) {
    if (level.getGameTime() % 20 != 0) {
      return;
    }
    var life = data.sim().life();
    if (life == null || life.report() == null) {
      return;
    }
    boolean empty = life.report().started() && life.o2() <= 1e-9;
    for (ServerPlayer p : level.players()) {
      if (!p.isCreative() && !p.isSpectator()) {
        secondFor(p, level, empty);
      }
    }
  }

  /** One second for one player; public so a test can drive it without waiting 120 real seconds. */
  public static boolean secondFor(ServerPlayer p, ServerLevel level, boolean shortOfBreath) {
    if (shortOfBreath) {
      p.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0, false, false, true));
    }
    if (!TIMER.tick(p.getUUID(), shortOfBreath)) {
      return false;
    }
    blackout(p, level);
    return true;
  }

  private static void blackout(ServerPlayer p, ServerLevel level) {
    var starter = StarterDome.data(level).origin();
    if (starter != null) {
      BlockPos bunk = StarterDome.spawnPos(starter);
      p.teleportTo(level, bunk.getX() + 0.5, bunk.getY(), bunk.getZ() + 0.5, p.getYRot(), 0);
    }
    p.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 0, false, false, false));
    p.displayClientMessage(Component.translatable("life.blackout"), false);
  }
}
