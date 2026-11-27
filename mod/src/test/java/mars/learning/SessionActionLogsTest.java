package mars.learning;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class SessionActionLogsTest {
  @TempDir Path temp;

  private ActionEvent event(int session, int tick) {
    return new ActionEvent(
        Instant.EPOCH,
        "w-3f9a2c",
        1,
        session,
        0,
        tick,
        0,
        null,
        ActionEvent.Band.MS,
        ActionEvent.Kind.GATE_REACHED,
        ActionEvent.Cause.SIMULATION,
        "mars:dome_stable");
  }

  @Test
  void rotationFlushesOldEventsAndRejectsAnEventForTheWrongSession() throws Exception {
    try (var logs = new SessionActionLogs(temp, "w-3f9a2c")) {
      assertThrows(IllegalStateException.class, () -> logs.append(event(7, 0)));
      logs.begin(7);
      logs.append(event(7, 1));
      logs.begin(7);
      logs.append(event(7, 2));
      logs.begin(8);
      assertEquals(2, Files.readAllLines(file(7)).size());
      assertThrows(IllegalArgumentException.class, () -> logs.append(event(7, 3)));
      assertThrows(IllegalArgumentException.class, () -> logs.begin(7));
      logs.append(event(8, 4));
    }
    assertEquals(1, Files.readAllLines(file(8)).size());
  }

  @Test
  void aFailedCloseCannotBeSilentlyIgnoredOnAnotherRotationAttempt() throws Exception {
    Path blocked = Files.createFile(temp.resolve("regular-file"));
    var logs = new SessionActionLogs(blocked, "w-3f9a2c");
    logs.begin(7);
    logs.append(event(7, 1));
    assertThrows(IOException.class, () -> logs.begin(8));
    assertThrows(IOException.class, () -> logs.begin(8));
    assertThrows(IOException.class, logs::close);
  }

  private Path file(int session) {
    return temp.resolve("w-3f9a2c/actionlog/" + ActionLog.sessionName(session) + ".jsonl");
  }
}
