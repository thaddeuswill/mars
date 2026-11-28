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

  public static void onSky(mars.sky.SkyPayload payload, IPayloadContext context) {
    mars.client.sky.SkyClient.setTarget(payload.pressureKpa());
  }

  public static void onLandingPanel(
      mars.story.landing.LandingPayloads.Panel payload, IPayloadContext context) {
    LandingClient.setPanel(payload);
  }

  public static void onLandingIntro(
      mars.story.landing.LandingPayloads.Intro payload, IPayloadContext context) {
    Minecraft.getInstance().setScreen(new LandingIntroScreen());
  }
}
