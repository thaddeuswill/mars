package mars.monitor;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;

/**
 * Posted on the NeoForge bus the first time a player is shown a new Monitor line (the learning
 * log's {@code monitor.line_revealed}); {@code lineId} is the line's language key.
 */
public class LineRevealedEvent extends Event {
  private final ServerPlayer player;
  private final String lineId;

  public LineRevealedEvent(ServerPlayer player, String lineId) {
    this.player = player;
    this.lineId = lineId;
  }

  public ServerPlayer player() {
    return player;
  }

  public String lineId() {
    return lineId;
  }
}
