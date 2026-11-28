package mars.hull;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;

/** Posted on the NeoForge bus after a player used a hull door; {@code open} is its new state. */
public class HullDoorUsedEvent extends Event {
  private final ServerPlayer player;
  private final BlockPos pos;
  private final boolean open;

  public HullDoorUsedEvent(ServerPlayer player, BlockPos pos, boolean open) {
    this.player = player;
    this.pos = pos;
    this.open = open;
  }

  public ServerPlayer player() {
    return player;
  }

  public BlockPos pos() {
    return pos;
  }

  public boolean open() {
    return open;
  }
}
