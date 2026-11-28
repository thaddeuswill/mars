package mars.monitor;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/** Registers the Monitor payload. The client handler class loads only when a payload arrives. */
public final class MonitorNetwork {
  private MonitorNetwork() {}

  public static void register(RegisterPayloadHandlersEvent event) {
    PayloadRegistrar registrar = event.registrar("2");
    // A lambda, not a method reference: a method reference loads the client class while the
    // registrar runs, which crashes a dedicated server ("invalid dist"). The lambda loads it only
    // when a payload arrives, and payloads only arrive on a client.
    registrar.playToClient(
        MonitorPayload.TYPE,
        MonitorPayload.CODEC,
        (payload, context) -> mars.client.ClientPayloadHandlers.onMonitor(payload, context));
    // Sky: pressure only, for the client-side sky and fog (slice 6). Same lambda rule.
    registrar.playToClient(
        mars.sky.SkyPayload.TYPE,
        mars.sky.SkyPayload.CODEC,
        (payload, context) -> mars.client.ClientPayloadHandlers.onSky(payload, context));
    // Landing panel, intro screen and the player's choice (slice 4). Same lambda rule.
    registrar.playToClient(
        mars.story.landing.LandingPayloads.Panel.TYPE,
        mars.story.landing.LandingPayloads.Panel.CODEC,
        (payload, context) -> mars.client.ClientPayloadHandlers.onLandingPanel(payload, context));
    registrar.playToClient(
        mars.story.landing.LandingPayloads.Intro.TYPE,
        mars.story.landing.LandingPayloads.Intro.CODEC,
        (payload, context) -> mars.client.ClientPayloadHandlers.onLandingIntro(payload, context));
    registrar.playToServer(
        mars.story.landing.LandingPayloads.Choice.TYPE,
        mars.story.landing.LandingPayloads.Choice.CODEC,
        (payload, context) ->
            context.enqueueWork(
                () ->
                    mars.story.landing.LandingService.onChoice(
                        (net.minecraft.server.level.ServerPlayer) context.player(),
                        payload.start())));
  }
}
