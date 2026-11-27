package mars.monitor;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/** Registers the Monitor payload. The client handler class loads only when a payload arrives. */
public final class MonitorNetwork {
  private MonitorNetwork() {}

  public static void register(RegisterPayloadHandlersEvent event) {
    PayloadRegistrar registrar = event.registrar("1");
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
    // Keeper's Journal: the request and the "seen" note go to the server (server classes, so plain
    // method references are fine); the list comes back to the client (lambda rule again).
    registrar.playToServer(
        mars.journal.JournalRequestPayload.TYPE,
        mars.journal.JournalRequestPayload.CODEC,
        mars.journal.JournalServer::onRequest);
    registrar.playToServer(
        mars.journal.JournalSeenPayload.TYPE,
        mars.journal.JournalSeenPayload.CODEC,
        mars.journal.JournalServer::onSeen);
    registrar.playToClient(
        mars.journal.JournalPayload.TYPE,
        mars.journal.JournalPayload.CODEC,
        (payload, context) -> mars.client.ClientPayloadHandlers.onJournal(payload, context));
  }
}
