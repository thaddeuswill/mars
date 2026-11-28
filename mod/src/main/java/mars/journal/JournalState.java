package mars.journal;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * One player's journal: which entries are unlocked, in unlock order, and which of them the player
 * has opened since (landing-spec 5.1 and 5.4). Pure. Unlocking is silent; the caller decides about
 * the single chat pointer.
 */
public final class JournalState {
  private final Set<String> unlocked = new LinkedHashSet<>();
  private final Set<String> seen = new LinkedHashSet<>();

  public JournalState() {}

  public JournalState(List<String> unlocked, List<String> seen) {
    this.unlocked.addAll(unlocked);
    this.seen.addAll(seen);
  }

  /** Unlock an entry. True when it is new to this player. */
  public boolean unlock(String id) {
    return unlocked.add(id);
  }

  public boolean isUnlocked(String id) {
    return unlocked.contains(id);
  }

  public List<String> unlocked() {
    return List.copyOf(unlocked);
  }

  public List<String> seen() {
    return List.copyOf(seen);
  }

  /** Entries unlocked and not yet opened, in unlock order. */
  public List<String> unread() {
    List<String> out = new ArrayList<>();
    for (String id : unlocked) {
      if (!seen.contains(id)) {
        out.add(id);
      }
    }
    return out;
  }

  /** The player opened the book: everything unlocked so far counts as seen. */
  public void markAllSeen() {
    seen.addAll(unlocked);
  }

  public void clear() {
    unlocked.clear();
    seen.clear();
  }
}
