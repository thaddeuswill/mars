package mars.learning;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * The journal is a pure function of latched gates (gates lock in, invariant I-2): an entry is
 * unlocked exactly when its gate has latched. No per-player journal is stored, so late joiners see
 * everything, and an admin latch unlocks entries too. Pure Java.
 */
public final class Journal {
  private Journal() {}

  /** Entries whose gate has latched, in (order, id) order. */
  public static List<JournalEntryDef> unlocked(
      List<JournalEntryDef> defs, Predicate<String> gateLatched) {
    return defs.stream()
        .filter(d -> gateLatched.test(d.gate()))
        .sorted(Comparator.comparingInt(JournalEntryDef::order).thenComparing(JournalEntryDef::id))
        .toList();
  }

  /** Entries that this latch just unlocked. */
  public static List<JournalEntryDef> unlockedBy(List<JournalEntryDef> defs, String gateId) {
    return unlocked(defs, gateId::equals);
  }
}
