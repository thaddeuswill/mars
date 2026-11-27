package mars.client.atmosphere;

import mars.client.sky.SkyClient;
import mars.visual.DustMath;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.joml.Vector3f;

/**
 * Ambient dust (AT-3, docs/art/atmosphere-plan.md 5.1): slow dust motes around the player and short
 * wind streaks along the planet's fixed wind, thicker in a storm. Client only and cosmetic: vanilla
 * {@code minecraft:dust} particles, no new particle type or texture. Registered from {@code
 * MarsMod} with a lambda (dedicated-server safe). Follows the player's Particles setting (all,
 * decreased = half, minimal = none).
 *
 * <p>Cost per client tick: a few random numbers (and a gust clock tick), and per spawned particle
 * one block-state and one sky check; about 0.2 motes per tick on a clear day, at most about 1.4 in
 * a full storm.
 */
public final class DustClient {
  private static final DustParticleOptions LIGHT =
      new DustParticleOptions(new Vector3f(0xC0 / 255f, 0x8A / 255f, 0x5E / 255f), 1.0F);
  private static final DustParticleOptions DARK =
      new DustParticleOptions(new Vector3f(0x8E / 255f, 0x5E / 255f, 0x3E / 255f), 1.0F);
  private static final DustParticleOptions STORM_LIGHT =
      new DustParticleOptions(new Vector3f(0xC0 / 255f, 0x8A / 255f, 0x5E / 255f), 1.6F);
  private static final DustParticleOptions STORM_DARK =
      new DustParticleOptions(new Vector3f(0x8E / 255f, 0x5E / 255f, 0x3E / 255f), 1.6F);
  private static final double MOTE_RADIUS = 12;
  private static final double STREAK_RADIUS = 24;
  // Vanilla dust keeps one tenth of the velocity it is given; 0.04 blocks per tick (0.8 blocks per
  // second at the start, slowing by friction) is about the plan's slow drift. INVENTED.
  private static final double MOTE_SPEED = 0.15;
  private static final double STREAK_SPEED = 0.45;
  private static final double STORM_SPEED = 0.35;

  private static double pressure = 0.6;
  private static final DustMath.Gusts GUSTS = new DustMath.Gusts(Math.random());

  private DustClient() {}

  public static void init() {
    NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post e) -> onTick());
  }

  /** The pressure the sky is showing, for the rate rule. */
  public static void setPressure(double kpa) {
    pressure = kpa;
  }

  private static void onTick() {
    Minecraft mc = Minecraft.getInstance();
    ClientLevel level = mc.level;
    if (level == null || mc.player == null || mc.isPaused() || !SkyClient.onMars(level)) {
      return;
    }
    ParticleStatus status = mc.options.particles().get();
    double factor =
        status == ParticleStatus.ALL ? 1 : (status == ParticleStatus.DECREASED ? 0.5 : 0);
    if (factor == 0) {
      return;
    }
    double storm = SkyClient.stormShown();
    RandomSource rnd = level.random;
    double[] wind = SkyClient.windDeg() >= 0 ? DustMath.windVector(SkyClient.windDeg()) : null;
    double px = mc.player.getX();
    double py = mc.player.getY();
    double pz = mc.player.getZ();

    int motes = count(DustMath.motesPerTick(pressure, storm, factor), rnd);
    for (int i = 0; i < motes; i++) {
      double a = rnd.nextDouble() * Math.PI * 2;
      double r = Math.sqrt(rnd.nextDouble()) * MOTE_RADIUS;
      double x = px + Math.cos(a) * r;
      double z = pz + Math.sin(a) * r;
      double y = py - 1 + rnd.nextDouble() * 5;
      BlockPos pos = BlockPos.containing(x, y, z);
      if (!level.getBlockState(pos).isAir() || !level.canSeeSky(pos)) {
        continue;
      }
      double speed = MOTE_SPEED + (STORM_SPEED - MOTE_SPEED) * storm;
      double wx = wind == null ? 0 : wind[0] * speed;
      double wz = wind == null ? 0 : wind[1] * speed;
      boolean light = rnd.nextFloat() < 0.6F;
      DustParticleOptions o =
          storm > 0.3 ? (light ? STORM_LIGHT : STORM_DARK) : (light ? LIGHT : DARK);
      level.addParticle(
          o,
          x,
          y,
          z,
          wx + (rnd.nextDouble() - 0.5) * 0.05,
          (rnd.nextDouble() - 0.5) * 0.02,
          wz + (rnd.nextDouble() - 0.5) * 0.05);
    }

    boolean gust = GUSTS.tick(rnd.nextDouble());
    if (gust
        && wind != null
        && rnd.nextDouble() < 2 * DustMath.streaksPerTick(pressure, storm, factor)) {
      streak(level, rnd, wind, px, pz);
    }
  }

  /** A row of 3 to 5 motes in a line along the wind, just above the ground. */
  private static void streak(
      ClientLevel level, RandomSource rnd, double[] wind, double px, double pz) {
    double a = rnd.nextDouble() * Math.PI * 2;
    double r = Math.sqrt(rnd.nextDouble()) * STREAK_RADIUS;
    double x = px + Math.cos(a) * r;
    double z = pz + Math.sin(a) * r;
    BlockPos column = BlockPos.containing(x, 0, z);
    if (!level.hasChunkAt(column)) {
      return;
    }
    int top = level.getHeight(Heightmap.Types.MOTION_BLOCKING, column.getX(), column.getZ());
    BlockPos ground = new BlockPos(column.getX(), top - 1, column.getZ());
    boolean sand = level.getBlockState(ground).is(mars.world.MarsTerrainBlocks.BASALTIC_SAND.get());
    if (!sand && rnd.nextBoolean()) { // dune sand is twice as likely to blow
      return;
    }
    if (!level.canSeeSky(ground.above())) {
      return;
    }
    int n = 3 + rnd.nextInt(3);
    for (int i = 0; i < n; i++) {
      double sx = x + wind[0] * i * 0.7;
      double sz = z + wind[1] * i * 0.7;
      double sy = top + 0.5 + rnd.nextDouble() * 0.5;
      if (!level.getBlockState(BlockPos.containing(sx, sy, sz)).isAir()) {
        continue;
      }
      level.addParticle(
          rnd.nextFloat() < 0.6F ? LIGHT : DARK,
          sx,
          sy,
          sz,
          wind[0] * STREAK_SPEED,
          0,
          wind[1] * STREAK_SPEED);
    }
  }

  /** A whole number of spawns from a per-tick rate: the integer part plus a chance for one more. */
  static int count(double perTick, RandomSource rnd) {
    int n = (int) perTick;
    return n + (rnd.nextDouble() < perTick - n ? 1 : 0);
  }
}
