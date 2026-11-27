package mars.client;

import mars.monitor.MonitorPayload;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Client handlers for server payloads. Only referenced from the registrar, never loaded on a
 * server.
 */
public final class ClientPayloadHandlers {
  private ClientPayloadHandlers() {}

  public static void onMonitor(MonitorPayload payload, IPayloadContext context) {
    Minecraft.getInstance().setScreen(new MonitorScreen(payload.view()));
  }
}
