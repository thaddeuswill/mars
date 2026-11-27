package mars.hull;

import mars.planets.PlanetData;
import mars.planets.PlanetManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The bridge between blocks and the planet's {@link mars.dome.DomeSeals}: blocks call {@link
 * #changed}, the planet tick calls {@link #onPlanetTick}. Server side only.
 */
public final class DomeEvents {
  private static final Logger LOG = LoggerFactory.getLogger("mars");

  private DomeEvents() {}

  /** A block that matters for sealing changed. Cost: a map lookup, then a set lookup per dome. */
  public static void changed(Level level, BlockPos pos) {
    if (level instanceof ServerLevel server && PlanetManager.isPlanet(server)) {
      PlanetManager.data(server).sim().domes().blockChanged(pos.asLong());
    }
  }

  /** Register a dome by its seed block (the Planetary Monitor). */
  public static void register(ServerLevel level, BlockPos pos) {
    PlanetData d = PlanetManager.data(level);
    if (d.sim().domes().register(pos.asLong())) {
      d.setDirty();
      LOG.info("Dome registered at {} in {}", pos.toShortString(), level.dimension().location());
    }
  }

  public static void unregister(ServerLevel level, BlockPos pos) {
    PlanetData d = PlanetManager.data(level);
    if (d.sim().domes().unregister(pos.asLong())) {
      d.setDirty();
      LOG.info("Dome removed at {} in {}", pos.toShortString(), level.dimension().location());
    }
  }

  /**
   * Once per planet tick. Cost: with no dome registered, one size check; with domes, see {@link
   * mars.dome.DomeSeals}. A status change is logged, posted as a {@link SealChangedEvent} and told
   * to the players on the planet (straw-man wording keys).
   */
  public static void onPlanetTick(ServerLevel level, PlanetData data) {
    var domes = data.sim().domes();
    if (domes.count() == 0) {
      return;
    }
    var changes = domes.tick(new WorldProbe(level));
    if (!changes.isEmpty()) {
      data.sim().refreshLife(); // the sealed state feeds life support at once, not at the next step
    }
    for (var c : changes) {
      data.setDirty();
      LOG.info(
          "Dome at {} in {}: {} -> {} ({} blocks of air)",
          net.minecraft.core.BlockPos.of(c.seed()).toShortString(),
          level.dimension().location(),
          c.before(),
          c.after(),
          c.volume());
      NeoForge.EVENT_BUS.post(new SealChangedEvent(level, c));
      if (c.breach()) {
        tell(level, "dome.breach");
      } else if (c.sealedNow() && c.before() != mars.dome.DomeSeals.Status.UNKNOWN) {
        tell(level, "dome.repaired");
      }
    }
  }

  private static void tell(ServerLevel level, String key) {
    for (var p : level.players()) {
      p.displayClientMessage(Component.translatable(key), true);
    }
  }
}
