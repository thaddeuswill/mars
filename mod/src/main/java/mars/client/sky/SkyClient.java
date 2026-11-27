package mars.client.sky;

import com.google.gson.JsonParser;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import mars.visual.SkyKeyframes;
import mars.visual.SkyState;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client side of the pressure-driven sky and fog (Stage 2 slice 6). Client only: the registration
 * call is made from {@code MarsMod} only when running on a client.
 *
 * <p>Cost: once per client tick one exponential step and, when the shown value moved, one keyframe
 * lookup (three OKLab mixes). Per frame: reads of cached values only.
 */
public final class SkyClient {
  private static final Logger LOG = LoggerFactory.getLogger("mars");
  static final ResourceLocation EFFECTS_ID = ResourceLocation.fromNamespaceAndPath("mars", "mars");
  private static final ResourceLocation KEYFRAMES =
      ResourceLocation.fromNamespaceAndPath("mars", "keyframes/sky.json");

  private static SkyKeyframes keyframes;
  private static SkyState state;
  private static double pendingTarget = Double.NaN;
  private static long lastNanos;

  private SkyClient() {}

  public static void init(IEventBus modBus) {
    modBus.addListener(
        (RegisterDimensionSpecialEffectsEvent e) -> e.register(EFFECTS_ID, new MarsSkyEffects()));
    modBus.addListener(
        (RegisterClientReloadListenersEvent e) ->
            e.registerReloadListener((ResourceManagerReloadListener) SkyClient::loadKeyframes));
    NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post e) -> onTick());
    NeoForge.EVENT_BUS.addListener((ViewportEvent.ComputeFogColor e) -> onFogColor(e));
    NeoForge.EVENT_BUS.addListener((ViewportEvent.RenderFog e) -> onRenderFog(e));
    NeoForge.EVENT_BUS.addListener(
        (ClientPlayerNetworkEvent.LoggingOut e) -> {
          pendingTarget = Double.NaN;
          if (state != null) {
            state.reset();
          }
        });
  }

  /** Reads {@code assets/mars/keyframes/sky.json}; on a bad file the vanilla sky stays. */
  private static void loadKeyframes(ResourceManager rm) {
    try (Reader r =
        new java.io.InputStreamReader(
            rm.getResourceOrThrow(KEYFRAMES).open(), StandardCharsets.UTF_8)) {
      SkyKeyframes kf = SkyKeyframes.parse(JsonParser.parseReader(r).getAsJsonObject());
      double tau = kf.smoothingSeconds();
      String dev = System.getProperty("mars.sky.smoothing_s"); // dev override (screenshot runs)
      if (dev != null) {
        tau = Double.parseDouble(dev);
      }
      keyframes = kf;
      state = new SkyState(kf, tau);
      if (!Double.isNaN(pendingTarget)) {
        state.setTarget(pendingTarget);
      }
      LOG.info("Sky keyframes loaded: {} keys, smoothing {} s", kf.keys().size(), tau);
    } catch (Exception ex) {
      keyframes = null;
      state = null;
      LOG.error("Sky keyframes NOT loaded, the vanilla sky is used: {}", ex.toString());
    }
  }

  public static void setTarget(double kpa) {
    pendingTarget = kpa;
    if (state != null) {
      state.setTarget(kpa);
    }
  }

  static SkyKeyframes keyframes() {
    return keyframes;
  }

  static SkyKeyframes.Look look() {
    return state != null ? state.look() : null;
  }

  private static void onTick() {
    if (state == null) {
      return;
    }
    long now = System.nanoTime();
    double dt = lastNanos == 0 ? 0 : Math.min(1.0, (now - lastNanos) / 1e9);
    lastNanos = now;
    state.advance(dt);
  }

  /** True when the camera is in the air of a Mars level and the keyframes are loaded. */
  private static boolean active(Camera camera) {
    Minecraft mc = Minecraft.getInstance();
    if (state == null
        || mc.level == null
        || !(mc.level.effects() instanceof MarsSkyEffects)
        || camera.getFluidInCamera() != FogType.NONE) {
      return false;
    }
    return !(camera.getEntity() instanceof LivingEntity le
        && (le.hasEffect(MobEffects.BLINDNESS) || le.hasEffect(MobEffects.DARKNESS)));
  }

  private static void onFogColor(ViewportEvent.ComputeFogColor e) {
    if (!active(e.getCamera())) {
      return;
    }
    SkyKeyframes.Look look = state.look();
    float day =
        (float)
            SkyKeyframes.daylight(
                Minecraft.getInstance().level.getTimeOfDay((float) e.getPartialTick()));
    float[] c = MarsSkyEffects.dayNight(look.fog(), look.night(), day);
    e.setRed(c[0]);
    e.setGreen(c[1]);
    e.setBlue(c[2]);
  }

  private static void onRenderFog(ViewportEvent.RenderFog e) {
    if (e.getMode() != FogRenderer.FogMode.FOG_TERRAIN || !active(e.getCamera())) {
      return;
    }
    double renderDistance = Minecraft.getInstance().gameRenderer.getRenderDistance();
    double[] d = keyframes.fogDistances(state.look(), renderDistance);
    e.setNearPlaneDistance((float) d[0]);
    e.setFarPlaneDistance((float) d[1]);
    e.setCanceled(true); // NeoForge applies the changed distances only when the event is cancelled
  }
}
