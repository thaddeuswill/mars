package mars.planets;

import mars.index.IndexDefinitions;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

/** Game event hooks for planets: data reload and the per-tick clock (ARCHITECTURE.md 2.1). */
public final class PlanetEvents {
  private PlanetEvents() {}

  public static void addReloadListeners(AddReloadListenerEvent event) {
    event.addListener(new IndexDefinitions.Listener());
  }

  /**
   * Cost per tick: for a level that is not a planet, one string compare; for a planet, a map lookup
   * (plus one saved-data lookup for the pause flag), one increment and one compare. A frozen level
   * (the vanilla {@code /tick freeze}, our pause mechanism, ARCHITECTURE.md 7.2) does not advance
   * the planet.
   */
  public static void onServerStarted(net.neoforged.neoforge.event.server.ServerStartedEvent e) {
    SessionControl.reapply(e.getServer());
  }

  public static void onLevelTick(LevelTickEvent.Post event) {
    if (!(event.getLevel() instanceof ServerLevel level) || !PlanetManager.isPlanet(level)) {
      return;
    }
    if (!level.tickRateManager().runsNormally()) {
      return;
    }
    PlanetData data = PlanetManager.data(level);
    if (data.sim().onServerTick(SessionData.get(level.getServer()).clock())) {
      data.setDirty();
      PlanetManager.announceGates(level, data);
    }
    mars.hull.DomeEvents.onPlanetTick(level, data);
    mars.sky.SkySync.onPlanetTick(level, data);
  }
}
