package mars.planets;

import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.Event;

/** Posted on the NeoForge bus once when a gate latches on a planet (also for Codex's gate log). */
public class GateLatchedEvent extends Event {
  private final ServerLevel level;
  private final String gateId;

  public GateLatchedEvent(ServerLevel level, String gateId) {
    this.level = level;
    this.gateId = gateId;
  }

  public ServerLevel level() {
    return level;
  }

  public String gateId() {
    return gateId;
  }
}
