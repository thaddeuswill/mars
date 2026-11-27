package mars.planets;

import java.util.List;
import java.util.UUID;
import mars.core.Guidance;
import mars.learning.Journal;
import mars.learning.JournalEntryDef;
import mars.learning.OpenForecasts;
import mars.learning.PlayerSettings;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Server glue for guidance and the journal (ARCHITECTURE.md 2.20). The rules live in the pure
 * {@code mars.learning} classes; this class reads the player's planet, the saved settings and sends
 * the chat pointer. Cost: a few set lookups per gate latch, login or Monitor open; nothing per
 * tick.
 */
public final class LearningService {
  private static final Logger LOG = LoggerFactory.getLogger("mars");
  private static final OpenForecasts OPEN = new OpenForecasts();

  /** Counts pointer lines sent (a test aid: it counts sends, not what the client received). */
  private static final java.util.concurrent.atomic.AtomicLong POINTERS =
      new java.util.concurrent.atomic.AtomicLong();

  private LearningService() {}

  public static long pointerLinesSent() {
    return POINTERS.get();
  }

  /** Open-forecast state, session only (see {@link OpenForecasts}). */
  public static OpenForecasts openForecasts() {
    return OPEN;
  }

  /** Classroom mode of this level's planet (always false for a level that is not a planet). */
  public static boolean classroom(ServerLevel level) {
    return PlanetManager.isPlanet(level) && PlanetManager.data(level).sim().pace().classroom();
  }

  public static PlayerSettings settings(MinecraftServer server) {
    return PlayerSettingsData.get(server).settings();
  }

  public static Guidance effective(ServerPlayer player) {
    return Guidance.effective(
        classroom(player.serverLevel()), settings(player.server).stored(player.getUUID()));
  }

  /** The outcome of a request to change a level. */
  public enum SetResult {
    OK,
    /** Classroom mode and the caller is not an operator: the teacher sets it. */
    TEACHER_ONLY,
    EXPERT_REFUSED
  }

  /**
   * Set a player's level. In classroom mode only an operator may, and never to expert. In standard
   * play anyone may set their own level (it only removes extra help, never changes a threshold).
   * Writes the {@code guidance.changed} log line.
   */
  public static SetResult setGuidance(
      MinecraftServer server,
      UUID target,
      Guidance level,
      boolean classroom,
      boolean byOperator,
      String actor) {
    if (classroom && !byOperator) {
      return SetResult.TEACHER_ONLY;
    }
    if (!Guidance.allowed(classroom, level)) {
      return SetResult.EXPERT_REFUSED;
    }
    PlayerSettingsData data = PlayerSettingsData.get(server);
    data.settings().setGuidance(target, level);
    data.setDirty();
    LOG.info("guidance.changed player={} level={} by={}", target, level.id(), actor);
    return SetResult.OK;
  }

  /** Journal entries unlocked on this level's planet (none when it is not a planet). */
  public static List<JournalEntryDef> unlocked(ServerLevel level) {
    if (!PlanetManager.isPlanet(level)) {
      return List.of();
    }
    var gates = PlanetManager.data(level).sim().gates();
    return Journal.unlocked(LearningDefinitions.get().journal(), gates::isLatched);
  }

  /** After a gate latched: one pointer line per gate to every player on the planet. */
  public static void onGateLatched(ServerLevel level, String gateId) {
    List<JournalEntryDef> fresh = Journal.unlockedBy(LearningDefinitions.get().journal(), gateId);
    if (fresh.isEmpty()) {
      return;
    }
    PlayerSettingsData data = PlayerSettingsData.get(level.getServer());
    List<String> ids = fresh.stream().map(JournalEntryDef::id).toList();
    for (ServerPlayer p : level.players()) {
      p.sendSystemMessage(
          Component.translatable(
              "journal.pointer", fresh.size(), Component.keybind("key.mars.journal")));
      POINTERS.incrementAndGet();
      data.settings().markNotified(p.getUUID(), ids);
    }
    data.setDirty();
  }

  /** Login or arrival on a planet: one pointer line if there are entries not told about yet. */
  public static void pointerIfNew(ServerPlayer player) {
    ServerLevel level = player.serverLevel();
    List<JournalEntryDef> unlocked = unlocked(level);
    if (unlocked.isEmpty()) {
      return;
    }
    PlayerSettingsData data = PlayerSettingsData.get(player.server);
    List<String> fresh = data.settings().unannounced(player.getUUID(), unlocked);
    if (fresh.isEmpty()) {
      return;
    }
    player.sendSystemMessage(
        Component.translatable(
            "journal.pointer", fresh.size(), Component.keybind("key.mars.journal")));
    POINTERS.incrementAndGet();
    data.settings().markNotified(player.getUUID(), fresh);
    data.setDirty();
  }

  public static void onLoggedIn(PlayerEvent.PlayerLoggedInEvent e) {
    if (e.getEntity() instanceof ServerPlayer p) {
      pointerIfNew(p);
    }
  }

  public static void onChangedDimension(PlayerEvent.PlayerChangedDimensionEvent e) {
    if (e.getEntity() instanceof ServerPlayer p) {
      pointerIfNew(p);
    }
  }

  /** The Monitor line filter for this player now (guidance plus open forecasts). */
  public static java.util.function.Predicate<String> lineFilter(ServerPlayer player) {
    Guidance g = effective(player);
    var asked = OPEN.askedBy(player.getUUID());
    var rules = LearningDefinitions.get().lines();
    return key -> rules.visible(key, g, asked);
  }
}
