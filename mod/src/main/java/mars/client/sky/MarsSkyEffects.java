package mars.client.sky;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import mars.visual.SkyKeyframes;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Sky of the Mars dimension type ({@code effects: mars:mars}). Draws the dome as a gradient from
 * the horizon colour to the zenith colour of the current pressure keyframe, the sunrise glow in the
 * keyframe's sunset-halo colour, the sun at 0.66 of vanilla size, the moon and the stars. The
 * drawing follows vanilla's {@code LevelRenderer.renderSky} (1.21.1) so the sun, moon and star
 * geometry match; only colours and the sun size differ. Not drawn: sun aureole, star density by
 * pressure, sunset band (planetary-keyframes 3.1 rows not applied in slice 6).
 */
final class MarsSkyEffects extends DimensionSpecialEffects {
  private static final ResourceLocation SUN =
      ResourceLocation.withDefaultNamespace("textures/environment/sun.png");
  private static final ResourceLocation MOON =
      ResourceLocation.withDefaultNamespace("textures/environment/moon_phases.png");
  // Elevation rings of the dome in degrees, from the zenith down to the horizon (invented layout).
  private static final float[] RINGS = {90, 70, 50, 35, 22, 12, 5, 0};
  private static final int SEGMENTS = 16;
  private static VertexBuffer starBuffer;

  MarsSkyEffects() {
    super(192.0F, true, SkyType.NORMAL, false, false);
  }

  @Override
  public Vec3 getBrightnessDependentFogColor(Vec3 color, float brightness) {
    return color.multiply(
        brightness * 0.94F + 0.06F, brightness * 0.94F + 0.06F, brightness * 0.91F + 0.09F);
  }

  @Override
  public boolean isFoggyAt(int x, int z) {
    return false;
  }

  /** Vanilla's sunrise strength and shape, in the keyframe's sunset-halo colour. */
  @Override
  public float[] getSunriseColor(float timeOfDay, float partialTick) {
    float[] c = super.getSunriseColor(timeOfDay, partialTick);
    SkyKeyframes.Look look = SkyClient.look();
    if (c != null && look != null) {
      c[0] = ((look.sunsetHalo() >> 16) & 0xFF) / 255f;
      c[1] = ((look.sunsetHalo() >> 8) & 0xFF) / 255f;
      c[2] = (look.sunsetHalo() & 0xFF) / 255f;
    }
    return c;
  }

  /** Mixes a day colour toward the night colour by vanilla's day brightness. */
  static float[] dayNight(int dayRgb, int nightRgb, float day) {
    float[] out = new float[3];
    for (int i = 0; i < 3; i++) {
      int shift = 16 - 8 * i;
      float d = ((dayRgb >> shift) & 0xFF) / 255f;
      float n = ((nightRgb >> shift) & 0xFF) / 255f;
      out[i] = n + (d - n) * day;
    }
    return out;
  }

  @Override
  public boolean renderSky(
      ClientLevel level,
      int ticks,
      float partialTick,
      Matrix4f modelView,
      Camera camera,
      Matrix4f projection,
      boolean isFoggy,
      Runnable setupFog) {
    SkyKeyframes kf = SkyClient.keyframes();
    SkyKeyframes.Look look = SkyClient.look();
    if (kf == null || look == null) {
      return false; // keyframes not loaded: vanilla draws its own sky
    }
    setupFog.run();
    FogType fluid = camera.getFluidInCamera();
    boolean blind =
        camera.getEntity() instanceof LivingEntity le
            && (le.hasEffect(MobEffects.BLINDNESS) || le.hasEffect(MobEffects.DARKNESS));
    if (isFoggy || fluid == FogType.POWDER_SNOW || fluid == FogType.LAVA || blind) {
      return true;
    }
    PoseStack pose = new PoseStack();
    pose.mulPose(modelView);
    float day = (float) SkyKeyframes.daylight(level.getTimeOfDay(partialTick));
    Tesselator tess = Tesselator.getInstance();

    RenderSystem.depthMask(false);
    RenderSystem.disableCull();
    FogRenderer.setupNoFog();
    RenderSystem.setShader(GameRenderer::getPositionColorShader);
    RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    drawDome(tess, pose.last().pose(), projection, look, kf.domeGradientExponent(), day);

    RenderSystem.enableBlend();
    float[] glow = getSunriseColor(level.getTimeOfDay(partialTick), partialTick);
    if (glow != null) {
      RenderSystem.setShader(GameRenderer::getPositionColorShader);
      pose.pushPose();
      pose.mulPose(Axis.XP.rotationDegrees(90.0F));
      float flip = Mth.sin(level.getSunAngle(partialTick)) < 0.0F ? 180.0F : 0.0F;
      pose.mulPose(Axis.ZP.rotationDegrees(flip));
      pose.mulPose(Axis.ZP.rotationDegrees(90.0F));
      Matrix4f m = pose.last().pose();
      BufferBuilder fan =
          tess.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
      fan.addVertex(m, 0.0F, 100.0F, 0.0F).setColor(glow[0], glow[1], glow[2], glow[3]);
      for (int j = 0; j <= 16; j++) {
        float a = j * (float) (Math.PI * 2) / 16.0F;
        fan.addVertex(m, Mth.sin(a) * 120.0F, Mth.cos(a) * 120.0F, -Mth.cos(a) * 40.0F * glow[3])
            .setColor(glow[0], glow[1], glow[2], 0.0F);
      }
      BufferUploader.drawWithShader(fan.buildOrThrow());
      pose.popPose();
    }

    RenderSystem.blendFuncSeparate(
        GlStateManager.SourceFactor.SRC_ALPHA,
        GlStateManager.DestFactor.ONE,
        GlStateManager.SourceFactor.ONE,
        GlStateManager.DestFactor.ZERO);
    pose.pushPose();
    float clear = 1.0F - level.getRainLevel(partialTick);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, clear);
    pose.mulPose(Axis.YP.rotationDegrees(-90.0F));
    pose.mulPose(Axis.XP.rotationDegrees(level.getTimeOfDay(partialTick) * 360.0F));
    Matrix4f m = pose.last().pose();
    float s = (float) (30.0 * kf.sunScale());
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderTexture(0, SUN);
    BufferBuilder bb = tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
    bb.addVertex(m, -s, 100.0F, -s).setUv(0.0F, 0.0F);
    bb.addVertex(m, s, 100.0F, -s).setUv(1.0F, 0.0F);
    bb.addVertex(m, s, 100.0F, s).setUv(1.0F, 1.0F);
    bb.addVertex(m, -s, 100.0F, s).setUv(0.0F, 1.0F);
    BufferUploader.drawWithShader(bb.buildOrThrow());
    s = 20.0F;
    RenderSystem.setShaderTexture(0, MOON);
    int phase = level.getMoonPhase();
    int col = phase % 4;
    int row = phase / 4 % 2;
    float u0 = col / 4.0F;
    float v0 = row / 2.0F;
    float u1 = (col + 1) / 4.0F;
    float v1 = (row + 1) / 2.0F;
    bb = tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
    bb.addVertex(m, -s, -100.0F, s).setUv(u1, v1);
    bb.addVertex(m, s, -100.0F, s).setUv(u0, v1);
    bb.addVertex(m, s, -100.0F, -s).setUv(u0, v0);
    bb.addVertex(m, -s, -100.0F, -s).setUv(u1, v0);
    BufferUploader.drawWithShader(bb.buildOrThrow());
    float stars = level.getStarBrightness(partialTick) * clear;
    if (stars > 0.0F) {
      RenderSystem.setShaderColor(stars, stars, stars, stars);
      FogRenderer.setupNoFog();
      VertexBuffer buf = stars();
      buf.bind();
      buf.drawWithShader(m, projection, GameRenderer.getPositionShader());
      VertexBuffer.unbind();
      setupFog.run();
    }
    pose.popPose();

    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.disableBlend();
    RenderSystem.defaultBlendFunc();
    RenderSystem.enableCull();
    RenderSystem.depthMask(true);
    return true;
  }

  /** The dome: rings from the zenith to the horizon, then straight down in the horizon colour. */
  private static void drawDome(
      Tesselator tess,
      Matrix4f modelView,
      Matrix4f projection,
      SkyKeyframes.Look look,
      double exponent,
      float day) {
    float[] zenith = dayNight(look.zenith(), look.night(), day);
    float[] horizon = dayNight(look.horizon(), look.night(), day);
    BufferBuilder bb = tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
    for (int i = 0; i < RINGS.length; i++) {
      float e0 = RINGS[i];
      float e1 = i + 1 < RINGS.length ? RINGS[i + 1] : -90.0F; // last band: horizon to nadir
      float[] c0 = ringColor(zenith, horizon, e0, exponent);
      float[] c1 = e1 < 0 ? horizon : ringColor(zenith, horizon, e1, exponent);
      for (int j = 0; j < SEGMENTS; j++) {
        float a0 = j * (float) (Math.PI * 2) / SEGMENTS;
        float a1 = (j + 1) * (float) (Math.PI * 2) / SEGMENTS;
        vertex(bb, modelView, e0, a0, c0);
        vertex(bb, modelView, e0, a1, c0);
        vertex(bb, modelView, e1, a1, c1);
        vertex(bb, modelView, e1, a0, c1);
      }
    }
    BufferUploader.drawWithShader(bb.buildOrThrow());
  }

  private static float[] ringColor(
      float[] zenith, float[] horizon, float elevationDeg, double exponent) {
    float t = (float) Math.pow(Math.max(0, elevationDeg) / 90.0, exponent);
    return new float[] {
      horizon[0] + (zenith[0] - horizon[0]) * t,
      horizon[1] + (zenith[1] - horizon[1]) * t,
      horizon[2] + (zenith[2] - horizon[2]) * t
    };
  }

  private static void vertex(
      BufferBuilder bb, Matrix4f m, float elevationDeg, float azimuth, float[] c) {
    double e = Math.toRadians(elevationDeg);
    float r = (float) (100.0 * Math.cos(e));
    bb.addVertex(m, r * Mth.sin(azimuth), (float) (100.0 * Math.sin(e)), r * Mth.cos(azimuth))
        .setColor(c[0], c[1], c[2], 1.0F);
  }

  /** The same 1500 stars as vanilla (seed 10842), built once. */
  private static VertexBuffer stars() {
    if (starBuffer == null) {
      RandomSource random = RandomSource.create(10842L);
      BufferBuilder bb =
          Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
      for (int i = 0; i < 1500; i++) {
        float x = random.nextFloat() * 2.0F - 1.0F;
        float y = random.nextFloat() * 2.0F - 1.0F;
        float z = random.nextFloat() * 2.0F - 1.0F;
        float size = 0.15F + random.nextFloat() * 0.1F;
        float len = Mth.lengthSquared(x, y, z);
        if (!(len <= 0.010000001F) && !(len >= 1.0F)) {
          Vector3f p = new Vector3f(x, y, z).normalize(100.0F);
          float roll = (float) (random.nextDouble() * (float) Math.PI * 2.0);
          Quaternionf q =
              new Quaternionf().rotateTo(new Vector3f(0.0F, 0.0F, -1.0F), p).rotateZ(roll);
          bb.addVertex(p.add(new Vector3f(size, -size, 0.0F).rotate(q)));
          bb.addVertex(p.add(new Vector3f(size, size, 0.0F).rotate(q)));
          bb.addVertex(p.add(new Vector3f(-size, size, 0.0F).rotate(q)));
          bb.addVertex(p.add(new Vector3f(-size, -size, 0.0F).rotate(q)));
        }
      }
      starBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
      starBuffer.bind();
      starBuffer.upload(bb.buildOrThrow());
      VertexBuffer.unbind();
    }
    return starBuffer;
  }
}
