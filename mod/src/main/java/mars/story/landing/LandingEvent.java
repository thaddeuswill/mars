package mars.story.landing;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;

/**
 * What the Landing and the journal did, posted on the NeoForge bus so the learning log (Codex,
 * {@code mars/learning}) can write it without this package knowing the log. {@code type} is the
 * event type of landing-spec 3.9 ("landing.step", "landing.skipped", "landing.done",
 * "briefing.opened", "journal.opened"); {@code subject} is the content id after "mars:".
 */
public class LandingEvent extends Event {
  private final ServerPlayer player;
  private final String type;
  private final String subject;

  public LandingEvent(ServerPlayer player, String type, String subject) {
    this.player = player;
    this.type = type;
    this.subject = subject;
  }

  public ServerPlayer player() {
    return player;
  }

  public String type() {
    return type;
  }

  public String subject() {
    return subject;
  }
}
