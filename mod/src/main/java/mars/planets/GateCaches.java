package mars.planets;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import mars.gates.GateDef;
import mars.session.Resupply;
import mars.session.StarterChest;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Delivers each latched gate's supply cache (docs/design/gate-input-items.md): the items listed in
 * the gate's {@code cache} field go to the player's inventory once per player and planet, with a
 * "Supply drop received" line and the gate's supply note. Runs when a gate latches (every player on
 * the planet) and when a player arrives or logs in (so a player who was offline gets theirs). A
 * full inventory drops the stack at the player's feet, like the starting Ovens. Cost: one set
 * lookup per latched gate that has a cache, per arrival or latch.
 */
public final class GateCaches {
  private static final Logger LOG = LoggerFactory.getLogger("mars");

  private GateCaches() {}

  /** Give this player every cache they have not had yet. Returns how many gates delivered. */
  public static int deliver(ServerPlayer player) {
    ServerLevel level = player.serverLevel();
    if (!PlanetManager.isPlanet(level) || !mars.index.IndexDefinitions.isLoaded()) {
      return 0;
    }
    var gates = PlanetManager.data(level).sim().gates();
    int delivered = 0;
    for (GateDef def : gates.defs()) {
      if (!def.cache().isEmpty() && gates.isLatched(def.id()) && give(level, player, def)) {
        delivered++;
      }
    }
    return delivered;
  }

  /** A gate latched: every player on the planet who has not had its cache gets it now. */
  public static void onGateLatched(ServerLevel level, GateDef def) {
    if (def.cache().isEmpty()) {
      return;
    }
    for (ServerPlayer p : level.players()) {
      give(level, p, def);
    }
  }

  private static boolean give(ServerLevel level, ServerPlayer player, GateDef def) {
    CacheData data = CacheData.get(level);
    if (!data.ledger().claim(def.id(), player.getUUID())) {
      return false;
    }
    data.setDirty();
    List<StarterChest.Entry> drop = entries(def);
    hand(player, drop, "grant.cache");
    LOG.info("Gate cache {}: to {} (once per player per planet)", def.id(), name(player));
    player.sendSystemMessage(Component.translatable(def.supplyNoteKey()));
    return true;
  }

  /** A gate's drop as plain id and count lines. */
  public static List<StarterChest.Entry> entries(GateDef def) {
    List<StarterChest.Entry> out = new ArrayList<>();
    for (GateDef.CacheItem c : def.cache()) {
      out.add(new StarterChest.Entry(c.item(), c.count()));
    }
    return out;
  }

  /**
   * The latest gate this planet has latched that has a drop (highest order), or null. This is the
   * gate a resupply request re-sends half of.
   */
  public static GateDef latestDrop(mars.gates.GateTracker gates) {
    GateDef best = null;
    for (GateDef def : gates.defs()) {
      if (!def.cache().isEmpty()
          && gates.isLatched(def.id())
          && (best == null || def.order() > best.order())) {
        best = def;
      }
    }
    return best;
  }

  /** What using a resupply request did. */
  public enum RequestResult {
    SENT,
    WAIT,
    NOTHING_YET
  }

  /**
   * A resupply request (docs/design/recipe-inputs-ruling.md section 6): half of the latest gate's
   * drop, rounded up, once per pass timer. Touches no Index parameter. On WAIT or NOTHING_YET the
   * caller keeps the item. No cap, by design.
   */
  public static RequestResult request(ServerPlayer player) {
    ServerLevel level = player.serverLevel();
    if (!PlanetManager.isPlanet(level) || !mars.index.IndexDefinitions.isLoaded()) {
      return RequestResult.NOTHING_YET;
    }
    var sim = PlanetManager.data(level).sim();
    GateDef latest = latestDrop(sim.gates());
    if (latest == null) {
      player.sendSystemMessage(Component.translatable("grant.refill.none"));
      return RequestResult.NOTHING_YET;
    }
    Resupply rule = rule(level);
    CacheData data = CacheData.get(level);
    double wait = data.ledger().tryRequest(player.getUUID(), sim.model().sols(), rule.passSols());
    if (wait > 0.0) {
      player.sendSystemMessage(
          Component.translatable("grant.refill.wait", String.format(Locale.ROOT, "%.1f", wait)));
      return RequestResult.WAIT;
    }
    data.setDirty();
    hand(player, Resupply.refill(entries(latest)), "grant.refill");
    player.sendSystemMessage(Component.translatable("grant.refill.note"));
    LOG.info("Resupply request from {}: refill of {}", name(player), latest.id());
    return RequestResult.SENT;
  }

  static Resupply rule(ServerLevel level) {
    ResourceLocation file =
        ResourceLocation.fromNamespaceAndPath("mars", "terrain/resupply_request.json");
    var res = level.getServer().getResourceManager().getResource(file);
    if (res.isPresent()) {
      try (Reader r = new InputStreamReader(res.get().open(), StandardCharsets.UTF_8)) {
        return Resupply.parse(r, file.toString());
      } catch (Exception e) {
        LOG.warn("Resupply rule unreadable, using 3 sols: {}", e.toString());
      }
    }
    return new Resupply(3.0);
  }

  private static void hand(ServerPlayer player, List<StarterChest.Entry> items, String lineKey) {
    for (StarterChest.Entry c : items) {
      var item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(c.id()));
      ItemStack stack = new ItemStack(item, c.count());
      Component name = stack.getHoverName();
      if (!player.getInventory().add(stack)) {
        player.drop(stack, false);
      }
      player.sendSystemMessage(Component.translatable(lineKey, c.count(), name));
    }
  }

  private static String name(ServerPlayer p) {
    return p.getName().getString();
  }
}
