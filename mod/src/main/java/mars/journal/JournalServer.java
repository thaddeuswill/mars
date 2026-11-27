package mars.journal;

import java.util.List;
import mars.learning.JournalEntryDef;
import mars.planets.LearningService;
import mars.planets.PlanetManager;
import mars.planets.PlayerSettingsData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Server side of the Keeper's Journal screen. The unlocked list is the same pure function the chat
 * command uses ({@link mars.learning.Journal}); the server decides what is unlocked and what is
 * unread. Cost: one list build per key press or entry click.
 */
public final class JournalServer {
  private JournalServer() {}

  /** Send the journal to a player, or say there is none (not on a planet). */
  public static void open(ServerPlayer player) {
    if (!PlanetManager.isPlanet(player.serverLevel())) {
      player.displayClientMessage(Component.translatable("journal.no_planet"), true);
      return;
    }
    List<JournalEntryDef> unlocked = LearningService.unlocked(player.serverLevel());
    var unread = LearningService.settings(player.server).unread(player.getUUID(), unlocked);
    var gates = PlanetManager.data(player.serverLevel()).sim().gates().defs();
    List<JournalPayload.Entry> out =
        unlocked.stream()
            .map(
                d ->
                    new JournalPayload.Entry(
                        d.id(),
                        d.titleKey(),
                        d.textKey(),
                        gates.stream()
                            .filter(g -> g.id().equals(d.gate()))
                            .map(mars.gates.GateDef::nameKey)
                            .findFirst()
                            .orElse(d.gate()),
                        unread.contains(d.id())))
            .toList();
    PacketDistributor.sendToPlayer(player, new JournalPayload(out));
  }

  public static void onRequest(JournalRequestPayload payload, IPayloadContext context) {
    if (context.player() instanceof ServerPlayer p) {
      open(p);
    }
  }

  /** Marks an entry read, only if it really is unlocked for this player (ignores anything else). */
  public static void onSeen(JournalSeenPayload payload, IPayloadContext context) {
    if (!(context.player() instanceof ServerPlayer p)) {
      return;
    }
    boolean unlocked =
        LearningService.unlocked(p.serverLevel()).stream()
            .anyMatch(d -> d.id().equals(payload.entryId()));
    if (unlocked) {
      PlayerSettingsData data = PlayerSettingsData.get(p.server);
      data.settings().markSeen(p.getUUID(), payload.entryId());
      data.setDirty();
    }
  }
}
