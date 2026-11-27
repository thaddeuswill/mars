package mars.planets;

import mars.index.IndexDefinitions;
import mars.session.StartingGrant;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Gives the starting machines (the two Regolith Ovens) to the first player who is on a planet, once
 * per world (balance sheet 2c decision 2). Runs on arrival and on login, so a world that saved
 * while a player was already on Mars still hands them out. The flag is set when they are given, so
 * dying, relogging or a second player never repeats it. Cost: one flag read per arrival or login.
 */
public final class GrantEvents {
  private static final Logger LOG = LoggerFactory.getLogger("mars");

  private GrantEvents() {}

  public static void onChangedDimension(PlayerEvent.PlayerChangedDimensionEvent e) {
    if (e.getEntity() instanceof ServerPlayer p) {
      maybeGrant(p);
    }
  }

  public static void onLoggedIn(PlayerEvent.PlayerLoggedInEvent e) {
    if (e.getEntity() instanceof ServerPlayer p) {
      maybeGrant(p);
    }
  }

  /** Give the starting machines if this player is on a planet and nobody has had them yet. */
  public static boolean maybeGrant(ServerPlayer player) {
    ServerLevel level = player.serverLevel();
    if (!PlanetManager.isPlanet(level) || !IndexDefinitions.isLoaded()) {
      return false;
    }
    GrantData data = GrantData.get(player.server);
    if (!data.grant().claim()) {
      return false;
    }
    data.setDirty();
    StartingGrant.plan(IndexDefinitions.require().machines())
        .forEach(
            (id, count) -> {
              var item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(id));
              ItemStack stack = new ItemStack(item, count);
              Component name = stack.getHoverName();
              if (!player.getInventory().add(stack)) {
                player.drop(stack, false);
              }
              player.sendSystemMessage(Component.translatable("grant.starting", count, name));
              LOG.info(
                  "Starting grant: {} x {} to {} (once per world)",
                  count,
                  id,
                  player.getName().getString());
            });
    return true;
  }
}
