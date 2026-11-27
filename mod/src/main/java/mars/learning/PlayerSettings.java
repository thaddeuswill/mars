package mars.learning;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import mars.core.Guidance;

/**
 * Per-player learning settings keyed by player UUID (no name is stored; local to the server's
 * computer): the chosen {@link Guidance} (null means the default for the planet), the journal
 * entries the player has opened ("seen") and the ones they were already told about ("notified", so
 * the pointer line is never repeated for the same entries). Pure Java.
 */
public final class PlayerSettings {
  /** One player's values. */
  public static final class Entry {
    public Guidance guidance;
    public final Set<String> seen = new LinkedHashSet<>();
    public final Set<String> notified = new LinkedHashSet<>();
  }

  private final Map<UUID, Entry> players = new HashMap<>();

  public Entry entry(UUID id) {
    return players.computeIfAbsent(id, k -> new Entry());
  }

  public Map<UUID, Entry> all() {
    return players;
  }

  public Guidance stored(UUID id) {
    Entry e = players.get(id);
    return e == null ? null : e.guidance;
  }

  public void setGuidance(UUID id, Guidance g) {
    entry(id).guidance = g;
  }

  public Set<String> seen(UUID id) {
    Entry e = players.get(id);
    return e == null ? Set.of() : Set.copyOf(e.seen);
  }

  public Set<String> notified(UUID id) {
    Entry e = players.get(id);
    return e == null ? Set.of() : Set.copyOf(e.notified);
  }

  public void markSeen(UUID id, String entryId) {
    entry(id).seen.add(entryId);
  }

  public void markNotified(UUID id, Iterable<String> entryIds) {
    Entry e = entry(id);
    entryIds.forEach(e.notified::add);
  }

  /** Ids of unlocked entries this player has not been told about yet. */
  public java.util.List<String> unannounced(UUID id, java.util.List<JournalEntryDef> unlocked) {
    Set<String> told = notified(id);
    return unlocked.stream().map(JournalEntryDef::id).filter(i -> !told.contains(i)).toList();
  }

  /** Ids of unlocked entries this player has not opened. */
  public java.util.List<String> unread(UUID id, java.util.List<JournalEntryDef> unlocked) {
    Set<String> s = seen(id);
    return unlocked.stream().map(JournalEntryDef::id).filter(i -> !s.contains(i)).toList();
  }
}
