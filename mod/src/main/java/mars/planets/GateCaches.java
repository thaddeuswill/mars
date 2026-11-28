package mars.planets;

import mars.gates.GateDef;
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
    for (GateDef.CacheItem c : def.cache()) {
      var item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(c.item()));
      ItemStack stack = new ItemStack(item, c.count());
      Component name = stack.getHoverName();
      if (!player.getInventory().add(stack)) {
        player.drop(stack, false);
      }
      player.sendSystemMessage(Component.translatable("grant.cache", c.count(), name));
      LOG.info(
          "Gate cache {}: {} x {} to {} (once per player per planet)",
          def.id(),
          c.count(),
          c.item(),
          player.getName().getString());
    }
    player.sendSystemMessage(Component.translatable(def.supplyNoteKey()));
    return true;
  }
}
