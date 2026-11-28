package mars.learning;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/** Projects structured session log records to the pseudonym-only events.csv. */
public final class ActionEventCsv {
  private static final List<String> HEADER =
      List.of(
          "timestamp",
          "session",
          "planet",
          "timeline",
          "superseded",
          "sol",
          "player_id",
          "scope",
          "band",
          "event_type",
          "cause",
          "prompt_id",
          "result",
          "interleaved",
          "scaffold",
          "guidance",
          "goal_ids",
          "ngss_codes",
          "machine",
          "page",
          "gate",
          "failure",
          "landing",
          "briefing",
          "journal");

  private ActionEventCsv() {}

  /**
   * Exports completed session logs in caller-provided order. Writes a UTF-8 BOM and publishes the
   * result only after all rows validate. Run off the server thread.
   */
  public static void write(String world, List<Path> logs, Path output) throws IOException {
    ActionEvent.requireWorld(world);
    Path target = output.toAbsolutePath();
    Path parent = Files.createDirectories(target.getParent());
    Path temp = Files.createTempFile(parent, ".events-", ".tmp");
    try {
      try (var writer = Files.newBufferedWriter(temp)) {
        writer.write('\ufeff');
        writeRow(writer, HEADER);
        for (Path log : logs) {
          try (var reader = Files.newBufferedReader(log)) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null) {
              row++;
              try {
                ActionEvent event = read(world, JsonParser.parseString(line).getAsJsonObject());
                writeEvent(writer, event);
              } catch (RuntimeException e) {
                throw new IOException(
                    "Invalid learning event at line " + row + " of " + log.getFileName());
              }
            }
          }
        }
      }
      Files.move(temp, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
    } finally {
      Files.deleteIfExists(temp);
    }
  }

  private static ActionEvent read(String world, JsonObject row) {
    if (row.get("v").getAsInt() != 1 || !world.equals(row.get("world").getAsString())) {
      throw new IllegalArgumentException("Wrong event version or world");
    }
    String type = row.get("type").getAsString();
    ActionEvent.Kind kind =
        Arrays.stream(ActionEvent.Kind.values())
            .filter(candidate -> candidate.type.equals(type))
            .findFirst()
            .orElseThrow();
    PlayerId player =
        row.get("player").isJsonNull() ? null : new PlayerId(row.get("player").getAsString());
    String expectedScope = player == null ? "planet" : "player";
    if (!expectedScope.equals(row.get("scope").getAsString())) {
      throw new IllegalArgumentException("Wrong event scope");
    }
    String session = row.get("session").getAsString();
    if (!session.matches("s-[0-9]{4,10}")) {
      throw new IllegalArgumentException("Invalid session identifier");
    }
    return new ActionEvent(
        Instant.parse(row.get("ts").getAsString()),
        world,
        row.get("planet").getAsInt(),
        Integer.parseInt(session.substring(2)),
        row.get("tl").getAsInt(),
        row.get("sim").getAsLong(),
        row.get("sol").getAsDouble(),
        player,
        enumValue(ActionEvent.Band.class, row, "band"),
        kind,
        enumValue(ActionEvent.Cause.class, row, "cause"),
        row.getAsJsonObject("data").get(kind.subjectKey).getAsString());
  }

  private static <T extends Enum<T>> T enumValue(Class<T> type, JsonObject row, String key) {
    return Enum.valueOf(type, row.get(key).getAsString().toUpperCase(Locale.ROOT));
  }

  private static void writeEvent(BufferedWriter writer, ActionEvent event) throws IOException {
    writeRow(
        writer,
        List.of(
            event.timestamp().toString(),
            ActionLog.sessionName(event.session()),
            Integer.toString(event.planet()),
            Integer.toString(event.timeline()),
            "",
            Double.toString(event.sol()),
            event.player() == null ? "" : event.player().value(),
            event.player() == null ? "planet" : "player",
            event.band().name().toLowerCase(Locale.ROOT),
            event.kind().type,
            event.cause().name().toLowerCase(Locale.ROOT),
            "",
            "none",
            "",
            "",
            "",
            "",
            "",
            detail(event, "machine"),
            detail(event, "page"),
            detail(event, "gate"),
            detail(event, "failure"),
            detail(event, "landing"),
            detail(event, "briefing"),
            detail(event, "journal")));
  }

  private static String detail(ActionEvent event, String key) {
    return event.kind().subjectKey.equals(key) ? event.subject() : "";
  }

  private static void writeRow(BufferedWriter writer, List<String> cells) throws IOException {
    for (int i = 0; i < cells.size(); i++) {
      if (i > 0) {
        writer.write(',');
      }
      writer.write(escape(cells.get(i)));
    }
    writer.write("\r\n");
  }

  static String escape(String value) {
    if (!value.isEmpty() && "=+-@".indexOf(value.charAt(0)) >= 0) {
      value = "'" + value;
    }
    if (value.indexOf(',') >= 0
        || value.indexOf('"') >= 0
        || value.indexOf('\r') >= 0
        || value.indexOf('\n') >= 0) {
      return "\"" + value.replace("\"", "\"\"") + "\"";
    }
    return value;
  }

  static Path exportCompleted(Path worldDirectory, String world, int currentSession)
      throws IOException {
    Path actions = worldDirectory.resolve("actionlog");
    if (!Files.isDirectory(actions)) {
      return null;
    }
    List<Path> logs;
    try (var entries = Files.list(actions)) {
      logs =
          entries
              .filter(Files::isRegularFile)
              .filter(path -> sessionNumber(path) >= 0 && sessionNumber(path) < currentSession)
              .sorted(java.util.Comparator.comparingInt(ActionEventCsv::sessionNumber))
              .toList();
    }
    if (logs.isEmpty()) {
      return null;
    }
    Path exports = Files.createDirectories(worldDirectory.resolve("exports"));
    Path directory = Files.createTempDirectory(exports, "events-");
    Path file = directory.resolve("events.csv");
    try {
      write(world, logs, file);
      return file;
    } catch (IOException e) {
      try {
        Files.deleteIfExists(directory);
      } catch (IOException cleanup) {
        e.addSuppressed(cleanup);
      }
      throw e;
    }
  }

  private static int sessionNumber(Path file) {
    String name = file.getFileName().toString();
    if (!name.matches("s-[0-9]{4,10}\\.jsonl")) {
      return -1;
    }
    try {
      return Integer.parseInt(name.substring(2, name.length() - ".jsonl".length()));
    } catch (NumberFormatException e) {
      return -1;
    }
  }
}
