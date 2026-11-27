package mars.planets;

import mars.core.Planets;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Finds a level's planet and routes machine place/remove events to it (ARCHITECTURE.md 1.3, 6). A
 * level is a planet when its dimension is in the {@code mars} namespace. No state is kept here: the
 * data lives in the level's saved data.
 */
public final class PlanetManager {
  private static final Logger LOG = LoggerFactory.getLogger("mars");

  /** Test seam, set only by the gameTestServer run configuration (docs/tech/stage2-plan.md §4). */
  private static final boolean ANY_LEVEL_IS_PLANET =
      Boolean.getBoolean("mars.selftest.anyLevelIsPlanet");

  private PlanetManager() {}

  public static boolean isPlanet(Level level) {
    return Planets.isPlanetNamespace(
        level.dimension().location().getNamespace(), ANY_LEVEL_IS_PLANET);
  }

  /** The planet's data, created from the start values on first use. Only call for planet levels. */
  public static PlanetData data(ServerLevel level) {
    return level.getDataStorage().computeIfAbsent(PlanetData.factory(), PlanetData.NAME);
  }

  public static void machinePlaced(ServerLevel level, String type, BlockPos pos) {
    PlanetData d = data(level);
    if (d.sim().addMachine(type, pos.asLong())) {
      d.setDirty();
      LOG.info(
          "{} placed at {} in {}: {} on this planet",
          type,
          pos.toShortString(),
          level.dimension().location(),
          d.sim().machineCount(type));
    }
  }

  public static void machineRemoved(ServerLevel level, String type, BlockPos pos) {
    PlanetData d = data(level);
    if (d.sim().removeMachine(type, pos.asLong())) {
      d.setDirty();
      LOG.info(
          "{} removed at {} in {}: {} left",
          type,
          pos.toShortString(),
          level.dimension().location(),
          d.sim().machineCount(type));
    }
  }
}
