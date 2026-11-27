package mars.learning;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/** One background JSON Lines writer per session file. Call close on session or server shutdown. */
public final class ActionLog implements AutoCloseable {
  private static final int BATCH_SIZE = 200;
  private static final Gson JSON = new GsonBuilder().serializeNulls().create();

  private final Path file;
  private final ScheduledExecutorService worker;
  private final List<ActionEvent> pending = new ArrayList<>();
  private boolean flushQueued;
  private boolean closed;
  private volatile IOException failure;

  public ActionLog(Path file) {
    this.file = file.toAbsolutePath();
    worker =
        Executors.newSingleThreadScheduledExecutor(
            task -> Thread.ofPlatform().daemon().name("mars-action-log").unstarted(task));
    worker.scheduleWithFixedDelay(this::flushInBackground, 5, 5, TimeUnit.SECONDS);
  }

  /** Called in event order from the server thread. Disk IO stays on the worker. */
  public synchronized void append(ActionEvent event) {
    if (closed) {
      throw new IllegalStateException("Action log is closed");
    }
    pending.add(java.util.Objects.requireNonNull(event));
    if (pending.size() >= BATCH_SIZE && !flushQueued && failure == null) {
      flushQueued = true;
      worker.execute(this::flushInBackground);
    }
  }

  /** The last background write failure, cleared by the next successful write. */
  public IOException failure() {
    return failure;
  }

  private void flushInBackground() {
    try {
      flush();
    } catch (IOException e) {
      failure = e;
    }
  }

  private void flush() throws IOException {
    List<ActionEvent> batch;
    synchronized (this) {
      flushQueued = false;
      if (pending.isEmpty()) {
        return;
      }
      batch = List.copyOf(pending);
    }
    StringBuilder lines = new StringBuilder();
    for (ActionEvent event : batch) {
      lines.append(JSON.toJson(jsonFields(event))).append('\n');
    }
    Files.createDirectories(file.getParent());
    try (FileChannel channel =
        FileChannel.open(file, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
      long before = channel.size();
      channel.position(before);
      try {
        ByteBuffer bytes = StandardCharsets.UTF_8.encode(lines.toString());
        while (bytes.hasRemaining()) {
          channel.write(bytes);
        }
      } catch (IOException writeFailure) {
        try {
          channel.truncate(before);
        } catch (IOException rollbackFailure) {
          writeFailure.addSuppressed(rollbackFailure);
        }
        throw writeFailure;
      }
    }
    synchronized (this) {
      pending.subList(0, batch.size()).clear();
      failure = null;
    }
  }

  private static Map<String, Object> jsonFields(ActionEvent event) {
    Map<String, Object> fields = new LinkedHashMap<>();
    fields.put("v", 1);
    fields.put("ts", event.timestamp().toString());
    fields.put("world", event.world());
    fields.put("planet", event.planet());
    fields.put("session", String.format(Locale.ROOT, "s-%04d", event.session()));
    fields.put("tl", event.timeline());
    fields.put("sim", event.simulationTick());
    fields.put("sol", event.sol());
    fields.put("scope", event.player() == null ? "planet" : "player");
    fields.put("player", event.player() == null ? null : event.player().value());
    fields.put("band", event.band().name().toLowerCase(Locale.ROOT));
    fields.put("type", event.kind().type);
    fields.put("cause", event.cause().name().toLowerCase(Locale.ROOT));
    fields.put("goals", List.of());
    fields.put("data", Map.of(event.kind().subjectKey, event.subject()));
    return fields;
  }

  @Override
  public void close() throws IOException {
    java.util.concurrent.Future<?> finalWrite;
    synchronized (this) {
      if (closed) {
        return;
      }
      closed = true;
      finalWrite =
          worker.submit(
              () -> {
                flush();
                return null;
              });
      worker.shutdown();
    }
    try {
      finalWrite.get();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IOException("Interrupted while closing action log", e);
    } catch (ExecutionException e) {
      throw new IOException("Could not finish writing the action log", e.getCause());
    }
  }
}
