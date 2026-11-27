package mars.learning.identity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import java.util.random.RandomGenerator;
import org.junit.jupiter.api.Test;

class PlayerIdentitiesTest {
  @Test
  void forgettingDoesNotReassignAnIdEvenWhenTheRandomDrawCollides() {
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
}
