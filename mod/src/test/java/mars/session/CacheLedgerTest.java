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
}
