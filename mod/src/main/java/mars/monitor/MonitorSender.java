package mars.monitor;

import mars.monitor.view.MonitorView;
import mars.planets.PlanetManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

/** Server side of the Monitor: builds the view from the real planet and sends it. */
public final class MonitorSender {
  private MonitorSender() {}

  /** The view for a level's planet, or null if the level isn't a planet. */
  public static MonitorView viewFor(ServerLevel level) {
    return PlanetManager.isPlanet(level) ? MonitorView.of(PlanetManager.data(level).sim()) : null;
  }

  /**
   * The view as this player may see it. The panel-dust line is held until the player's Landing is
   * over (f1-storm-spec.md 3: the starting panel is dusty from the first tick, and the line would
   * otherwise appear in the middle of Landing step 3). The first time the line is shown the player
   * gets one "New on the Monitor" line and a {@link LineRevealedEvent}.
   */
  public static MonitorView forPlayer(ServerPlayer player, MonitorView view) {
    if (!mars.story.landing.LandingService.landingOver(player)) {
      // No storm or dust line for a player still in the Landing (spec: no forecast before it).
      return view.withoutCause(MonitorView.DUST_LINE)
          .withoutCause(MonitorView.STORM_EXPECTED)
          .withoutCause(MonitorView.STORM_ACTIVE)
          .withoutCause(MonitorView.STORM_CUT);
    }
    if (view.hasCause(MonitorView.DUST_LINE)) {
      var rec = mars.story.landing.LandingService.rec(player);
      if (rec.revealedLines.add(MonitorView.DUST_LINE)) {
        player.sendSystemMessage(
            mars.story.landing.LandingService.bell(
                Component.translatable("monitor.dust.revealed")));
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.post(
            new LineRevealedEvent(player, MonitorView.DUST_LINE));
      }
    }
    return view;
  }

  /** Open the Monitor for a player. In a level that is not a planet it says so instead. */
  public static boolean open(ServerPlayer player) {
    MonitorView view = viewFor(player.serverLevel());
    if (view == null) {
      player.displayClientMessage(Component.translatable("monitor.no_planet"), true);
      return false;
    }
    if (mars.story.landing.LandingService.wantsOutsideAir(player)) {
      view = view.withPinnedOutsideAir();
    }
    view = forPlayer(player, view);
    PacketDistributor.sendToPlayer(player, new MonitorPayload(view));
    mars.story.landing.LandingService.onMonitorOpened(player);
    return true;
  }
}
