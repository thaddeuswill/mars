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
  }
}
