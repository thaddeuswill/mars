package mars.learning;

import java.time.Instant;
import java.util.Objects;

/** A structured learning event. The initial event set accepts no user-supplied free text. */
public record ActionEvent(
    Instant timestamp,
    String world,
    int planet,
    int session,
    int timeline,
    long simulationTick,
    double sol,
    PlayerId player,
    Band band,
    Kind kind,
    Cause cause,
    String subject) {

  public enum Band {
    MS,
    HS
  }

  public enum Cause {
    PLAYER,
    SIMULATION,
    ADMIN,
    DEBUG
  }

  public enum Kind {
    MACHINE_BUILT("machine.built", "machine", true),
    MACHINE_REMOVED("machine.removed", "machine", true),
    MONITOR_VIEWED("monitor.viewed", "page", true),
    GATE_REACHED("gate.reached", "gate", false),
    SEAL_BREACHED("failure.reported", "failure", false);

    final String type;
    final String subjectKey;
    final boolean playerScoped;

    Kind(String type, String subjectKey, boolean playerScoped) {
      this.type = type;
      this.subjectKey = subjectKey;
      this.playerScoped = playerScoped;
    }
  }

  public ActionEvent {
    Objects.requireNonNull(timestamp);
    Objects.requireNonNull(kind);
    Objects.requireNonNull(band);
    Objects.requireNonNull(cause);
    requireWorld(world);
    if (planet < 1
        || session < 0
        || timeline < 0
        || simulationTick < 0
        || !Double.isFinite(sol)
        || sol < 0) {
      throw new IllegalArgumentException("Invalid learning event context");
    }
    if ((player != null) != kind.playerScoped) {
      throw new IllegalArgumentException("Actor does not match event scope");
    }
    if (subject == null || !subject.matches("mars:[a-z0-9_]+")) {
      throw new IllegalArgumentException("Expected a Mars content identifier");
    }
  }

  static String requireWorld(String world) {
    if (world == null || !world.matches("w-[0-9a-f]{6}")) {
      throw new IllegalArgumentException("Invalid learning world identifier");
    }
    return world;
  }
}
