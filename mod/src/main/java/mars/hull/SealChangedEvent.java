package mars.hull;

import mars.dome.DomeSeals;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.Event;

/**
 * Posted on the NeoForge event bus (server side, once per change, not per tick) when a dome's seal
 * state changes: first check, breach, or repair. The learning log subscribes to this for its "seal
 * breach" action; nothing here knows about players or names.
 */
public class SealChangedEvent extends Event {
  private final ServerLevel level;
  private final DomeSeals.Change change;

  public SealChangedEvent(ServerLevel level, DomeSeals.Change change) {
    this.level = level;
    this.change = change;
  }

  public ServerLevel level() {
    return level;
  }

  public DomeSeals.Change change() {
    return change;
  }
}
