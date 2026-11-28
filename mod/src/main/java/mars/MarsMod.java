package mars;

import mars.planets.TravelCommands;
import mars.selftest.SelfTest;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entry point of the Mars companion mod. Stage 2 slice 1: Planetary Index saved per planet,
 * Regolith Outgasser, Planetary Monitor.
 */
@Mod(MarsMod.MOD_ID)
public final class MarsMod {
  public static final String MOD_ID = "mars";
  private static final Logger LOG = LoggerFactory.getLogger("mars");

  public MarsMod(IEventBus modBus, ModContainer container) {
    container.registerConfig(ModConfig.Type.SERVER, MarsServerConfig.SPEC);
    SelfTest.installIfEnabled(modBus);
    SelfTest.step("mod_constructed");
    mars.machines.MarsMachines.register(modBus);
    mars.hull.HullBlocks.register(modBus);
    mars.habitat.HabitatBlocks.register(modBus);
    NeoForge.EVENT_BUS.addListener(TravelCommands::register);
    NeoForge.EVENT_BUS.addListener(mars.admin.IndexCommands::register);
    NeoForge.EVENT_BUS.addListener(mars.planets.PlanetEvents::addReloadListeners);
    NeoForge.EVENT_BUS.addListener(mars.planets.PlanetEvents::onLevelTick);
    NeoForge.EVENT_BUS.addListener(mars.planets.PlanetEvents::onServerStarted);
    NeoForge.EVENT_BUS.addListener(mars.admin.SessionCommands::register);
    NeoForge.EVENT_BUS.addListener(mars.admin.AdminCommands::register);
    NeoForge.EVENT_BUS.addListener(mars.planets.GrantEvents::onChangedDimension);
    NeoForge.EVENT_BUS.addListener(mars.planets.GrantEvents::onLoggedIn);
    modBus.addListener(mars.monitor.MonitorNetwork::register);
    NeoForge.EVENT_BUS.addListener(mars.sky.SkySync::onChangedDimension);
    NeoForge.EVENT_BUS.addListener(mars.sky.SkySync::onLoggedIn);
    NeoForge.EVENT_BUS.addListener(mars.sky.SkySync::onRespawn);
    // Client-only sky and fog (slice 6). The class is only touched on a client.
    if (FMLEnvironment.dist.isClient()) {
      mars.client.sky.SkyClient.init(modBus);
    }
    // Dev-only screenshot helper: client only, and only with -Dmars.devtools=true.
    if (FMLEnvironment.dist.isClient() && Boolean.getBoolean("mars.devtools")) {
      mars.client.dev.DevTools.init();
    }
    LOG.info("Mars companion mod constructed (stage 2 slice 1)");
  }
}
