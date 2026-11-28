package mars.hull;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;

/** Posted on the NeoForge bus after a player's Seal Kit replaced a patchable block. */
public class SealKitPatchedEvent extends Event {
  private final ServerPlayer player;
  private final ServerLevel level;
  private final BlockPos pos;

  public SealKitPatchedEvent(ServerPlayer player, ServerLevel level, BlockPos pos) {
    this.player = player;
    this.level = level;
    this.pos = pos;
  }

  public ServerPlayer player() {
    return player;
  }

  public ServerLevel level() {
    return level;
  }

  public BlockPos pos() {
    return pos;
  }
}
