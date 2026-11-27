package mars.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import mars.TestSims;
import org.junit.jupiter.api.Test;

/** The once-per-world starting Ovens (balance sheet 2c decision 2). */
class StartingGrantTest {
  @Test
  void theMachineFilesAskForTwoOvensAndNothingElse() throws Exception {
    Map<String, Integer> plan = StartingGrant.plan(TestSims.defs());
    System.out.println("starting grant plan: " + plan);
    assertEquals(Map.of("mars:regolith_oven", 2), plan);
  }

  @Test
  void theClaimIsHandedOutOnceNoMatterHowManyPlayersOrArrivals() {
    StartingGrant g = new StartingGrant(false);
    assertTrue(g.claim(), "first player to arrive gets the Ovens");
    assertFalse(g.claim(), "a second player, or the same player again, gets nothing");
    assertFalse(g.claim());
    assertTrue(g.granted());
  }

  @Test
  void deathAndRelogDoNotHandThemOutAgain() {
    StartingGrant g = new StartingGrant(false);
    assertTrue(g.claim());
    // Save and load the world (relog or restart): the flag is all that is stored.
    boolean saved = g.granted();
    StartingGrant afterRestart = new StartingGrant(saved);
    assertFalse(afterRestart.claim(), "a restart does not repeat it");
    // Death changes nothing here: the flag does not depend on the player or their inventory.
    assertFalse(afterRestart.claim(), "dying and arriving again does not repeat it");
    // A world that has not granted yet still does, after a save and load.
    assertTrue(new StartingGrant(new StartingGrant(false).granted()).claim());
  }
}
