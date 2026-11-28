package mars.machines;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;

/**
 * Posted on the NeoForge bus when a player brushes a solar panel (the learning log's {@code
 * panel.cleaned}): who, the dust stage the panel had, and whether a storm had been forecast (a
 * warned storm that is still to come: evidence for goal C1, cleaning before a forecast storm).
 */
public class PanelCleanedEvent extends Event {
  private final ServerPlayer player;
  private final ServerLevel level;
  private final int stageBefore;
  private final boolean stormWarned;

  public PanelCleanedEvent(
      ServerPlayer player, ServerLevel level, int stageBefore, boolean stormWarned) {
    this.player = player;
    this.level = level;
    this.stageBefore = stageBefore;
    this.stormWarned = stormWarned;
  }

  public ServerPlayer player() {
    return player;
  }

  public ServerLevel level() {
    return level;
  }

  public int stageBefore() {
    return stageBefore;
  }

  public boolean stormWarned() {
    return stormWarned;
  }
}
