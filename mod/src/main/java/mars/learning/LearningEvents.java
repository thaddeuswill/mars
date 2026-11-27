package mars.learning;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.IdentityHashMap;
import java.util.Map;
import mars.MarsMod;
import mars.hull.SealChangedEvent;
import mars.planets.PlanetManager;
import mars.planets.SessionData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Server-thread adapter for planet-scoped evidence. */
@EventBusSubscriber(modid = MarsMod.MOD_ID)
public final class LearningEvents {
  private static final Logger LOG = LoggerFactory.getLogger("mars.learning");
  private static final Map<MinecraftServer, SessionActionLogs> LOGS = new IdentityHashMap<>();

  private LearningEvents() {}

  @SubscribeEvent
  public static void sealChanged(SealChangedEvent event) {
    if (!event.change().breach() || !PlanetManager.isPlanet(event.level())) {
      return;
    }
    ServerLevel level = event.level();
    MinecraftServer server = level.getServer();
    int session = SessionData.get(server).clock().sessionNumber();
    try {
      LearningWorldData world = LearningWorldData.get(server);
      SessionActionLogs logs =
          LOGS.computeIfAbsent(
              server, key -> new SessionActionLogs(dataDirectory(key), world.id()));
      logs.begin(session);
      var model = PlanetManager.data(level).sim().model();
      logs.append(
          new ActionEvent(
              Instant.now(),
              world.id(),
              world.planet(level.dimension().location().toString()),
              session,
              0,
              model.simTicks(),
              model.sols(),
              null,
              ActionEvent.Band.MS,
              ActionEvent.Kind.SEAL_BREACHED,
              ActionEvent.Cause.SIMULATION,
              "mars:seal_breach"));
    } catch (IOException e) {
      LOG.error("Could not write the planet learning event", e);
    }
  }

  @SubscribeEvent
  public static void serverStopping(ServerStoppingEvent event) {
    try {
      closeFor(event.getServer());
    } catch (IOException e) {
      LOG.error("Could not finish the world learning log", e);
    }
  }

  static void closeFor(MinecraftServer server) throws IOException {
    SessionActionLogs logs = LOGS.remove(server);
    if (logs != null) {
      logs.close();
    }
  }

  static Path dataDirectory(MinecraftServer server) {
    return server.getServerDirectory().resolve("mars-data");
  }
}
