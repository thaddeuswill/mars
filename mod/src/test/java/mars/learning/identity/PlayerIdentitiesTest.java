package mars.learning.identity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.random.RandomGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PlayerIdentitiesTest {
  @TempDir Path temp;

  @Test
  void forgettingDoesNotReassignAnIdEvenWhenTheRandomDrawCollides() throws Exception {
    RandomGenerator draws =
        new RandomGenerator() {
          private int count;

          @Override
          public long nextLong() {
            throw new UnsupportedOperationException();
          }

          @Override
          public int nextInt(int bound) {
            int draw = count++ / 5;
            return draw == 2 ? 0 : draw;
          }
        };
    var world = new PlayerIdentities(draws);
    UUID first = new UUID(0, 1);
    UUID second = new UUID(0, 2);
    var prior = world.identify(first);
    var teammate = world.identify(second);
    assertEquals(prior, world.identify(first));
    assertNotEquals(prior, teammate);
    assertTrue(world.forget(first));
    assertFalse(world.forget(first));
    assertEquals("P-55555", world.identify(first).value());
    assertEquals(teammate, world.identify(second));
    world.forgetAll();
    assertNotEquals(teammate, world.identify(second));
  }

  @Test
  void persistedPseudonymsSurviveReopenWithoutStoringRawAccounts() throws Exception {
    Path file = temp.resolve("identity/players.json");
    UUID first = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    UUID second = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
    var world = PlayerIdentities.open(file);
    var prior = world.identify(first);
    var teammate = world.identify(second);
    assertNotEquals(prior, teammate);
    String stored = Files.readString(file);
    assertFalse(stored.contains(first.toString()));
    assertFalse(stored.contains(second.toString()));
    var reopened = PlayerIdentities.open(file);
    assertEquals(prior, reopened.identify(first));
    assertEquals(teammate, reopened.identify(second));
    assertTrue(reopened.forget(first));
    var afterForget = PlayerIdentities.open(file);
    assertNotEquals(prior, afterForget.identify(first));
    assertEquals(teammate, afterForget.identify(second));
    afterForget.forgetAll();
    assertNotEquals(teammate, PlayerIdentities.open(file).identify(second));
  }

  @Test
  void malformedIdentityFileIsNeverReplacedWithFreshIds() throws Exception {
    Path file = Files.writeString(temp.resolve("players.json"), "{}");
    assertThrows(IOException.class, () -> PlayerIdentities.open(file));
    assertEquals("{}", Files.readString(file));
  }
}
