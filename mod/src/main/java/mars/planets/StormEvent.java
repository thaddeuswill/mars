package mars.planets;

import mars.hazards.storm.StormScheduler;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.Event;

/**
 * Posted on the NeoForge bus for each step of a planet's dust storm, in order: SCHEDULED, WARNED,
 * REMINDER, ONSET (the forecast locks here), CLEARING, ENDED. {@code voided}: the guards failed (no
 * finished Landing or no panel) so a forecast must resolve void. {@code fastForward}: the event
 * came from an operator's {@code /mars index advance}, not from play, so a forecast must not be
 * scored and the log marks it as an admin cause. {@code tick} is the simulation tick of the step.
 */
public class StormEvent extends Event {
  private final ServerLevel level;
  private final StormScheduler.Kind kind;
  private final long tick;
  private final long onsetTick;
  private final boolean voided;
  private final boolean fastForward;

  public StormEvent(
      ServerLevel level,
      StormScheduler.Kind kind,
      long tick,
      long onsetTick,
      boolean voided,
      boolean fastForward) {
    this.level = level;
    this.kind = kind;
    this.tick = tick;
    this.onsetTick = onsetTick;
    this.voided = voided;
    this.fastForward = fastForward;
  }

  public ServerLevel level() {
    return level;
  }

  public StormScheduler.Kind kind() {
    return kind;
  }

  public long tick() {
    return tick;
  }

  public long onsetTick() {
    return onsetTick;
  }

  public boolean voided() {
    return voided;
  }

  public boolean fastForward() {
    return fastForward;
  }
}
