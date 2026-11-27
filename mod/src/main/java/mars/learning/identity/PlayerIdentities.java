package mars.learning.identity;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.random.RandomGenerator;
import mars.learning.PlayerId;

/** World-local identity boundary. An issued pseudonym is never reassigned. */
public final class PlayerIdentities {
  private final RandomGenerator random;
  private final IdentityFile store;
  private final Map<String, PlayerId> accounts = new HashMap<>();
  private final Set<PlayerId> issued = new HashSet<>();

  /** An in-memory world for temporary use. */
  public PlayerIdentities() {
    this(new SecureRandom());
  }

  PlayerIdentities(RandomGenerator random) {
    this(random, null);
  }

  private PlayerIdentities(RandomGenerator random, IdentityFile store) {
    this.random = Objects.requireNonNull(random);
    this.store = store;
  }

  /** Open one identity owner for the world's private identity file. */
  public static PlayerIdentities open(Path file) throws IOException {
    SecureRandom random = new SecureRandom();
    IdentityFile.Snapshot snapshot;
    try {
      snapshot = IdentityFile.read(file);
    } catch (NoSuchFileException missing) {
      byte[] key = new byte[IdentityFile.KEY_BYTES];
      random.nextBytes(key);
      return new PlayerIdentities(random, new IdentityFile(file, key));
    }
    var identities =
        new PlayerIdentities(random, new IdentityFile(file, IdentityFile.key(snapshot)));
    identities.accounts.putAll(snapshot.accounts());
    identities.issued.addAll(snapshot.issued());
    return identities;
  }

  public synchronized PlayerId identify(UUID account) throws IOException {
    String key = accountKey(account);
    PlayerId existing = accounts.get(key);
    if (existing != null) {
      return existing;
    }
    PlayerId id = nextId();
    accounts.put(key, id);
    issued.add(id);
    try {
      save();
    } catch (IOException e) {
      accounts.remove(key);
      issued.remove(id);
      throw e;
    }
    return id;
  }

  public synchronized boolean forget(UUID account) throws IOException {
    String key = accountKey(account);
    PlayerId removed = accounts.remove(key);
    if (removed == null) {
      return false;
    }
    try {
      save();
    } catch (IOException e) {
      accounts.put(key, removed);
      throw e;
    }
    return true;
  }

  /** Remove all links while reserving the old IDs for evidence already written. */
  public synchronized void forgetAll() throws IOException {
    if (accounts.isEmpty()) {
      return;
    }
    Map<String, PlayerId> previous = Map.copyOf(accounts);
    accounts.clear();
    try {
      save();
    } catch (IOException e) {
      accounts.putAll(previous);
      throw e;
    }
  }

  private String accountKey(UUID account) {
    Objects.requireNonNull(account);
    return store == null ? account.toString() : store.accountKey(account);
  }

  private void save() throws IOException {
    if (store != null) {
      store.write(accounts, issued);
    }
  }

  private PlayerId nextId() {
    String letters = PlayerId.ALPHABET;
    while (true) {
      StringBuilder id = new StringBuilder("P-");
      for (int i = 0; i < 5; i++) {
        id.append(letters.charAt(random.nextInt(letters.length())));
      }
      PlayerId candidate = new PlayerId(id.toString());
      if (!issued.contains(candidate)) {
        return candidate;
      }
    }
  }
}
