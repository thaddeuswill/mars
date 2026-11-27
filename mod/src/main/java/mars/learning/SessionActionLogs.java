package mars.learning;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

/** Owns one world's current action writer and drains it before switching sessions. */
public final class SessionActionLogs implements AutoCloseable {
  private final Path directory;
  private final String world;
  private int session = -1;
  private ActionLog writer;
  private boolean closed;

  public SessionActionLogs(Path marsData, String world) {
    this.world = ActionEvent.requireWorld(world);
    directory = Objects.requireNonNull(marsData).resolve(world).resolve("actionlog");
  }

  /** The caller supplies the server's session number. Repeating it keeps the current writer. */
  public synchronized void begin(int nextSession) throws IOException {
    requireOpen();
    if (nextSession < 0 || nextSession < session) {
      throw new IllegalArgumentException("Learning session cannot move backwards");
    }
    if (nextSession == session) {
      return;
    }
    if (writer != null) {
      writer.close();
    }
    writer = new ActionLog(directory.resolve(ActionLog.sessionName(nextSession) + ".jsonl"));
    session = nextSession;
  }

  public synchronized void append(ActionEvent event) {
    requireOpen();
    Objects.requireNonNull(event);
    if (writer == null) {
      throw new IllegalStateException("Start a learning session before logging events");
    }
    if (!world.equals(event.world()) || session != event.session()) {
      throw new IllegalArgumentException("Event belongs to another world or session");
    }
    writer.append(event);
  }

  @Override
  public synchronized void close() throws IOException {
    closed = true;
    if (writer != null) {
      writer.close();
    }
  }

  private void requireOpen() {
    if (closed) {
      throw new IllegalStateException("Learning session logs are closed");
    }
  }
}
