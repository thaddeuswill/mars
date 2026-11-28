package mars.learning;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ActionEventCsvTest {
  @TempDir Path temp;

  @Test
  void exportsTheActualLogWithPseudonymsAndASeparatePlanetRow() throws Exception {
    Path log = temp.resolve("s-0007.jsonl");
    var actor = new PlayerId("P-7K3QX");
    try (var writer = new ActionLog(log)) {
      writer.append(
          new ActionEvent(
              Instant.EPOCH,
              "w-3f9a2c",
              2,
              7,
              0,
              100,
              0.25,
              actor,
              ActionEvent.Band.HS,
              ActionEvent.Kind.MACHINE_BUILT,
              ActionEvent.Cause.PLAYER,
              "mars:regolith_oven"));
      writer.append(
          new ActionEvent(
              Instant.EPOCH,
              "w-3f9a2c",
              2,
              7,
              0,
              200,
              0.5,
              null,
              ActionEvent.Band.MS,
              ActionEvent.Kind.SEAL_BREACHED,
              ActionEvent.Cause.SIMULATION,
              "mars:seal_breach"));
      writer.append(
          new ActionEvent(
              Instant.EPOCH,
              "w-3f9a2c",
              2,
              7,
              0,
              300,
              0.75,
              actor,
              ActionEvent.Band.HS,
              ActionEvent.Kind.LANDING_STEP,
              ActionEvent.Cause.PLAYER,
              "mars:landing_step_5_auto"));
    }
    Path csv = temp.resolve("exports/events.csv");
    ActionEventCsv.write("w-3f9a2c", List.of(log), csv);
    var lines = Files.readAllLines(csv);
    assertEquals(4, lines.size());
    assertEquals('\ufeff', lines.getFirst().charAt(0));
    var columns = List.of(lines.getFirst().substring(1).split(","));
    var player = lines.get(1).split(",", -1);
    var planet = lines.get(2).split(",", -1);
    var landing = lines.get(3).split(",", -1);
    assertEquals(columns.size(), player.length);
    assertEquals(columns.size(), planet.length);
    assertEquals("P-7K3QX", player[columns.indexOf("player_id")]);
    assertEquals("player", player[columns.indexOf("scope")]);
    assertEquals("mars:regolith_oven", player[columns.indexOf("machine")]);
    assertEquals("", planet[columns.indexOf("player_id")]);
    assertEquals("planet", planet[columns.indexOf("scope")]);
    assertEquals("mars:seal_breach", planet[columns.indexOf("failure")]);
    assertEquals("mars:landing_step_5_auto", landing[columns.indexOf("landing")]);
    assertEquals("landing.step", landing[columns.indexOf("event_type")]);
    assertFalse(columns.contains("uuid"));
    assertFalse(columns.contains("username"));

    Files.writeString(log, Files.readString(log).replace("P-7K3QX", "ExamplePlayer"));
    assertThrows(IOException.class, () -> ActionEventCsv.write("w-3f9a2c", List.of(log), csv));
    assertEquals(lines, Files.readAllLines(csv), "a failed export preserves the previous version");
  }

  @Test
  void operatorExportIncludesOnlyCompletedSessionsInSessionOrder() throws Exception {
    Path world = temp.resolve("w-3f9a2c");
    assertNull(ActionEventCsv.exportCompleted(world, "w-3f9a2c", 8));
    for (int session : List.of(7, 2, 8)) {
      Path file = world.resolve("actionlog").resolve(ActionLog.sessionName(session) + ".jsonl");
      try (var log = new ActionLog(file)) {
        log.append(
            new ActionEvent(
                Instant.EPOCH,
                "w-3f9a2c",
                1,
                session,
                0,
                100,
                0.25,
                null,
                ActionEvent.Band.MS,
                ActionEvent.Kind.SEAL_BREACHED,
                ActionEvent.Cause.SIMULATION,
                "mars:seal_breach"));
      }
    }
    var csv = ActionEventCsv.exportCompleted(world, "w-3f9a2c", 8);
    var lines = Files.readAllLines(csv);
    assertEquals(3, lines.size());
    assertEquals("s-0002", lines.get(1).split(",")[1]);
    assertEquals("s-0007", lines.get(2).split(",")[1]);
  }

  @Test
  void rejectsAnActiveSessionRowStoredInACompletedSessionFile() throws Exception {
    Path world = temp.resolve("w-3f9a2c");
    Path log = world.resolve("actionlog/s-0007.jsonl");
    try (var writer = new ActionLog(log)) {
      writer.append(
          new ActionEvent(
              Instant.EPOCH,
              "w-3f9a2c",
              1,
              8,
              0,
              100,
              0.25,
              null,
              ActionEvent.Band.MS,
              ActionEvent.Kind.SEAL_BREACHED,
              ActionEvent.Cause.SIMULATION,
              "mars:seal_breach"));
    }
    Path previousExport = Files.writeString(temp.resolve("events.csv"), "previous export");
    assertThrows(IOException.class, () -> ActionEventCsv.exportCompleted(world, "w-3f9a2c", 8));
    assertThrows(
        IOException.class, () -> ActionEventCsv.write("w-3f9a2c", List.of(log), previousExport));
    assertEquals("previous export", Files.readString(previousExport));
    try (var exports = Files.list(world.resolve("exports"))) {
      assertEquals(0, exports.count());
    }
  }

  @Test
  void escapesSpreadsheetFormulasQuotesAndLineBreaks() {
    assertEquals("'=1+2", ActionEventCsv.escape("=1+2"));
    assertEquals("'+3", ActionEventCsv.escape("+3"));
    assertEquals("'-4", ActionEventCsv.escape("-4"));
    assertEquals("'@cell", ActionEventCsv.escape("@cell"));
    assertEquals("\"a,\"\"b\"\"\nc\"", ActionEventCsv.escape("a,\"b\"\nc"));
  }
}
