package mars.learning;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.IdentityHashMap;
import java.util.Map;
import mars.MarsMod;
import mars.gates.GateTracker;
import mars.hull.SealChangedEvent;
import mars.learning.identity.PlayerIdentities;
import mars.planets.GateLatchedEvent;
import mars.planets.PlanetManager;
import mars.planets.SessionData;
import mars.story.landing.LandingEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Server-thread adapter for player and planet evidence. */
@EventBusSubscriber(modid = MarsMod.MOD_ID)
public final class LearningEvents {
  private static final Logger LOG = LoggerFactory.getLogger("mars.learning");
  private static final Map<MinecraftServer, SessionActionLogs> LOGS = new IdentityHashMap<>();

  private static final Map<MinecraftServer, PlayerIdentities> IDENTITIES = new IdentityHashMap<>();

  private LearningEvents() {}

  @SubscribeEvent
  public static void sealChanged(SealChangedEvent event) {
    if (event.change().breach()) {
      append(
          event.level(),
          ActionEvent.Kind.SEAL_BREACHED,
          ActionEvent.Cause.SIMULATION,
          "mars:seal_breach",
          null);
    }
  }

  @SubscribeEvent
  public static void landing(LandingEvent event) {
    ActionEvent.Kind kind =
        switch (event.type()) {
          case "landing.step" -> ActionEvent.Kind.LANDING_STEP;
          case "landing.skipped" -> ActionEvent.Kind.LANDING_SKIPPED;
          case "landing.done" -> ActionEvent.Kind.LANDING_DONE;
          case "briefing.opened" -> ActionEvent.Kind.BRIEFING_OPENED;
          case "journal.opened" -> ActionEvent.Kind.JOURNAL_OPENED;
          default -> null;
        };
    if (kind != null && event.subject() != null && !event.subject().isEmpty()) {
      append(
          event.player().serverLevel(),
          kind,
          ActionEvent.Cause.PLAYER,
          "mars:" + event.subject(),
          event.player());
    }
  }

  @SubscribeEvent
  public static void gateLatched(GateLatchedEvent event) {
    ServerLevel level = event.level();
    if (!PlanetManager.isPlanet(level)) {
      return;
    }
    GateTracker.State state = PlanetManager.data(level).sim().gates().state(event.gateId());
    if (state != null && state.latched()) {
      ActionEvent.Cause cause =
          state.cause() == GateTracker.Cause.ADMIN
              ? ActionEvent.Cause.ADMIN
              : ActionEvent.Cause.SIMULATION;
      append(level, ActionEvent.Kind.GATE_REACHED, cause, event.gateId(), null);
    }
  }

  private static void append(
      ServerLevel level,
      ActionEvent.Kind kind,
      ActionEvent.Cause cause,
      String subject,
      ServerPlayer actor) {
    if (!PlanetManager.isPlanet(level)) {
      return;
    }
    MinecraftServer server = level.getServer();
    int session = SessionData.get(server).clock().sessionNumber();
    try {
      LearningWorldData world = LearningWorldData.get(server);
      PlayerId player = null;
      if (actor != null) {
        PlayerIdentities identities = IDENTITIES.get(server);
        if (identities == null) {
          identities =
              PlayerIdentities.open(
                  dataDirectory(server)
                      .resolve(world.id())
                      .resolve("identity")
                      .resolve("players.json"));
          IDENTITIES.put(server, identities);
        }
        player = identities.identify(actor.getUUID());
      }
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
              player,
              ActionEvent.Band.MS,
              kind,
              cause,
              subject));
    } catch (IOException e) {
      LOG.error("Could not write the learning event", e);
    } catch (IllegalArgumentException e) {
      LOG.warn("Skipped invalid learning event of type {}", kind.type);
    }
  }

  @SubscribeEvent
  public static void serverStopping(ServerStoppingEvent event) {
    IDENTITIES.remove(event.getServer());
    try {
      closeFor(event.getServer());
    } catch (IOException e) {
      LOG.error("Could not finish the world learning log", e);
    }
  }

  static void finishEarlierSession(MinecraftServer server, int currentSession) throws IOException {
    SessionActionLogs logs = LOGS.get(server);
    if (logs != null) {
      logs.begin(currentSession);
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
