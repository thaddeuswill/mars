package mars.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class CacheLedgerTest {
  private static final UUID A = UUID.fromString("6d617273-0000-4000-8000-00000000000a");
  private static final UUID B = UUID.fromString("6d617273-0000-4000-8000-00000000000b");

  @Test
  void eachPlayerClaimsEachGateOnce() {
    CacheLedger l = new CacheLedger();
    assertTrue(l.claim("mars:dome_stable", A));
    assertFalse(l.claim("mars:dome_stable", A), "relog, death or a second arrival: nothing");
    assertTrue(l.claim("mars:phase_2_begins", A), "another gate is another cache");
    assertTrue(l.claim("mars:dome_stable", B), "a player who was offline claims theirs later");
    assertFalse(l.claim("mars:dome_stable", B));
  }

  @Test
  void claimsSurviveSavingAndLoading() {
    CacheLedger l = new CacheLedger();
    l.claim("mars:thaw", A);
    CacheLedger back = new CacheLedger(l.toMap());
    assertTrue(back.hasClaimed("mars:thaw", A));
    assertFalse(back.claim("mars:thaw", A));
    assertTrue(back.claim("mars:thaw", B));
    assertEquals(
        1, l.toMap().get("mars:thaw").size(), "the saved copy is not changed by later claims");
  }

  @Test
  void aRequestPassesOnceEveryThreeSolsAndAnEarlyOneCostsNothing() {
    CacheLedger l = new CacheLedger();
    assertEquals(0.0, l.tryRequest(A, 10.0, 3.0), "first request: sent");
    assertEquals(2.0, l.tryRequest(A, 11.0, 3.0), 1e-9, "1 sol later: 2 sols to wait");
    assertEquals(0.5, l.tryRequest(A, 12.5, 3.0), 1e-9, "the early try did not restart the timer");
    assertEquals(0.0, l.tryRequest(A, 13.0, 3.0), "3 sols after the first: sent again");
    assertEquals(0.0, l.tryRequest(B, 13.0, 3.0), "the timer is per player");
    assertEquals(0.0, l.tryRequest(A, 5.0, 3.0), "a clock that went back never blocks");
  }

  @Test
  void requestTimesSurviveSavingAndLoading() {
    CacheLedger l = new CacheLedger();
    l.tryRequest(A, 4.0, 3.0);
    CacheLedger back = new CacheLedger(l.toMap(), l.requestsToMap());
    assertEquals(1.0, back.tryRequest(A, 6.0, 3.0), 1e-9);
    assertEquals(0.0, new CacheLedger(l.toMap()).tryRequest(A, 4.5, 3.0), "schema 1 save: none");
  }
}
