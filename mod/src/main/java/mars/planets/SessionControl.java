package mars.planets;

import mars.session.SessionClock;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pause and resume (ARCHITECTURE.md 7.2). Pausing sets the session flag, which stops our planet
 * steps, and freezes the server with the vanilla tick-rate manager ({@code /tick freeze}), which
 * stops mobs, block entities and random ticks. Resume unfreezes and starts a new session. Backups
 * and the recap in the 7.2 resume order are later slices.
 */
public final class SessionControl {
  private static final Logger LOG = LoggerFactory.getLogger("mars");

  private SessionControl() {}

  /** True if the state changed. */
  public static boolean pause(MinecraftServer server, String actor) {
    SessionData d = SessionData.get(server);
    boolean changed = d.clock().pause();
    server.tickRateManager().setFrozen(true);
    if (changed) {
      d.setDirty();
      LOG.info("session.paused by {} (session {})", actor, d.clock().sessionNumber());
    }
    return changed;
  }

  /** True if the state changed. */
  public static boolean resume(MinecraftServer server, String actor) {
    SessionData d = SessionData.get(server);
    boolean changed = d.clock().resume();
    server.tickRateManager().setFrozen(false);
    if (changed) {
      d.setDirty();
      LOG.info("session.resumed by {} (session {})", actor, d.clock().sessionNumber());
    }
    return changed;
  }

  /** On server start: a saved pause freezes the server again (the freeze itself is not saved). */
  public static void reapply(MinecraftServer server) {
    SessionClock c = SessionData.get(server).clock();
    if (c.paused()) {
      server.tickRateManager().setFrozen(true);
      LOG.info("Session {} starts paused; the server is frozen.", c.sessionNumber());
    }
  }
}
