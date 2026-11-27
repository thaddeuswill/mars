package mars.monitor;

import mars.monitor.view.MonitorView;
import mars.planets.LearningService;
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
   * The view for this player: the planet's view without the estimate lines their guidance level or
   * open forecasts hide (CR-T8). The view is built per recipient on every open, so hiding per
   * player costs one pass over the rows and no change to the payload.
   */
  public static MonitorView viewFor(ServerPlayer player) {
    MonitorView view = viewFor(player.serverLevel());
    return view == null ? null : view.hidingLines(LearningService.lineFilter(player));
  }

  /** Open the Monitor for a player. In a level that is not a planet it says so instead. */
  public static boolean open(ServerPlayer player) {
    MonitorView view = viewFor(player);
    if (view == null) {
      player.displayClientMessage(Component.translatable("monitor.no_planet"), true);
      return false;
    }
    PacketDistributor.sendToPlayer(player, new MonitorPayload(view));
    return true;
  }
}
