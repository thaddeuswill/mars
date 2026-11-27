package mars.learning.identity;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.random.RandomGenerator;
import mars.learning.PlayerId;

/** In-memory identity boundary for one world. An issued pseudonym is never reassigned. */
public final class PlayerIdentities {
  private final RandomGenerator random;
  private final Map<UUID, PlayerId> accounts = new HashMap<>();
  private final Set<PlayerId> issued = new HashSet<>();

  public PlayerIdentities() {
    this(new SecureRandom());
  }

  PlayerIdentities(RandomGenerator random) {
    this.random = java.util.Objects.requireNonNull(random);
  }

  public synchronized PlayerId identify(UUID account) {
    java.util.Objects.requireNonNull(account);
    return accounts.computeIfAbsent(account, ignored -> nextId());
  }

  public synchronized boolean forget(UUID account) {
    return accounts.remove(java.util.Objects.requireNonNull(account)) != null;
  }

  /** Remove all links while reserving the old IDs for evidence already written. */
  public synchronized void forgetAll() {
    accounts.clear();
  }

  private PlayerId nextId() {
    String letters = PlayerId.ALPHABET;
    while (true) {
      StringBuilder id = new StringBuilder("P-");
      for (int i = 0; i < 5; i++) {
        id.append(letters.charAt(random.nextInt(letters.length())));
      }
      PlayerId candidate = new PlayerId(id.toString());
      if (issued.add(candidate)) {
        return candidate;
      }
    }
  }
}
