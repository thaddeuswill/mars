package mars.learning;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonParser;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ActionLogTest {
  @TempDir Path temp;

  private static ActionEvent event(int tick) {
    return new ActionEvent(
        Instant.EPOCH,
        "w-3f9a2c",
        1,
        7,
        0,
        tick,
        tick / 24_000.0,
        new PlayerId("P-7K3QX"),
        ActionEvent.Band.MS,
        ActionEvent.Kind.MACHINE_BUILT,
        ActionEvent.Cause.PLAYER,
        "mars:regolith_oven");
  }

  @Test
  void writesInOrderAcrossTheBatchBoundaryAndPreservesExistingLines() throws Exception {
    Path output = temp.resolve("actions/s-0007.jsonl");
    try (var log = new ActionLog(output)) {
      for (int i = 0; i < 205; i++) {
        log.append(event(i));
      }
    }
    var reopened = new ActionLog(output);
    try (reopened) {
      reopened.append(event(205));
    }
    assertThrows(IllegalStateException.class, () -> reopened.append(event(206)));
    var lines = Files.readAllLines(output);
    assertEquals(206, lines.size());
    for (int i = 0; i < lines.size(); i++) {
      var fields = JsonParser.parseString(lines.get(i)).getAsJsonObject();
      assertEquals(i, fields.get("sim").getAsInt());
      assertEquals("P-7K3QX", fields.get("player").getAsString());
      assertEquals("player", fields.get("scope").getAsString());
      assertEquals("s-0007", fields.get("session").getAsString());
    }
  }

  @Test
  void planetEventsCarryNoPlayerAndInvalidIdentitiesAreRejected() throws Exception {
    Path output = temp.resolve("planet.jsonl");
    try (var log = new ActionLog(output)) {
      log.append(
          new ActionEvent(
              Instant.EPOCH,
              "w-3f9a2c",
              1,
              7,
              0,
              3,
              0,
              null,
              ActionEvent.Band.HS,
              ActionEvent.Kind.GATE_REACHED,
              ActionEvent.Cause.ADMIN,
              "mars:dome_stable"));
    }
    String line = Files.readString(output);
    var fields = JsonParser.parseString(line).getAsJsonObject();
    assertEquals("planet", fields.get("scope").getAsString());
    assertEquals("admin", fields.get("cause").getAsString());
    assertTrue(fields.get("player").isJsonNull());
    assertFalse(line.contains("uuid"));
    assertThrows(IllegalArgumentException.class, () -> new PlayerId("ExamplePlayer"));
    assertThrows(
        IllegalArgumentException.class, () -> new PlayerId("123e4567-e89b-12d3-a456-426614174000"));
  }

  @Test
  void reportsAnUnwritableLogAtShutdown() throws Exception {
    Path parent = Files.createFile(temp.resolve("file-instead-of-directory"));
    var log = new ActionLog(parent.resolve("session.jsonl"));
    log.append(event(1));
    assertThrows(java.io.IOException.class, log::close);
  }

  @Test
  void logBoundaryHasNoDependencyOnAccountUuids() {
    noClasses()
        .should()
        .dependOnClassesThat()
        .haveFullyQualifiedName("java.util.UUID")
        .check(
            new ClassFileImporter()
                .importClasses(ActionEvent.class, ActionLog.class, PlayerId.class));
  }
}
