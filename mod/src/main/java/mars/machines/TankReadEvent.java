package mars.machines;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;

/** Posted on the NeoForge bus when a player reads a wall oxygen tank (Landing step 2). */
public class TankReadEvent extends Event {
  private final ServerPlayer player;

  public TankReadEvent(ServerPlayer player) {
    this.player = player;
  }

  public ServerPlayer player() {
    return player;
  }
}
