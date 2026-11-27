package mars.learning.identity;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import mars.learning.PlayerId;

/** The private identity store. This file is never part of a log or export. */
final class IdentityFile {
  private static final Gson JSON = new Gson();
  private static final HexFormat HEX = HexFormat.of();
  static final int KEY_BYTES = 32;
  private static final String HMAC = "HmacSHA256";

  record Snapshot(int version, String key, Map<String, PlayerId> accounts, Set<PlayerId> issued) {}

  private final Path path;
  private final byte[] key;

  IdentityFile(Path path, byte[] key) {
    this.path = path.toAbsolutePath();
    this.key = key.clone();
  }

  static Snapshot read(Path path) throws IOException {
    try (var input = Files.newBufferedReader(path)) {
      Snapshot s = JSON.fromJson(input, Snapshot.class);
      if (s == null
          || s.version() != 1
          || s.key() == null
          || !s.key().matches("[0-9a-f]{64}")
          || s.accounts() == null
          || s.issued() == null
          || s.issued().contains(null)
          || !s.issued().containsAll(s.accounts().values())
          || new HashSet<>(s.accounts().values()).size() != s.accounts().size()
          || s.accounts().keySet().stream().anyMatch(k -> !k.matches("[0-9a-f]{64}"))) {
        throw new IOException("Invalid learning identity file");
      }
      return s;
    } catch (JsonParseException | IllegalArgumentException e) {
      throw new IOException("Invalid learning identity file", e);
    }
  }

  static byte[] key(Snapshot snapshot) {
    return HEX.parseHex(snapshot.key());
  }

  String accountKey(UUID account) {
    try {
      Mac mac = Mac.getInstance(HMAC);
      mac.init(new SecretKeySpec(key, HMAC));
      byte[] bytes =
          ByteBuffer.allocate(16)
              .putLong(account.getMostSignificantBits())
              .putLong(account.getLeastSignificantBits())
              .array();
      return HEX.formatHex(mac.doFinal(bytes));
    } catch (GeneralSecurityException e) {
      throw new IllegalStateException("HmacSHA256 is unavailable", e);
    }
  }

  void write(Map<String, PlayerId> accounts, Set<PlayerId> issued) throws IOException {
    Path directory = Files.createDirectories(path.getParent());
    Path temp =
        Files.getFileStore(directory).supportsFileAttributeView("posix")
            ? Files.createTempFile(
                directory,
                ".players-",
                ".tmp",
                PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rw-------")))
            : Files.createTempFile(directory, ".players-", ".tmp");
    try {
      Files.writeString(temp, JSON.toJson(new Snapshot(1, HEX.formatHex(key), accounts, issued)));
      Files.move(temp, path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
    } finally {
      Files.deleteIfExists(temp);
    }
  }
}
