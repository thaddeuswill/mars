package mars.planets;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import mars.index.IndexDefinitions;
import mars.session.StarterChest;
import mars.session.StartingGrant;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
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
    giveStarterChest(player);
    return true;
  }

  /** The lander's supply chest (copper and redstone), read from data/mars/terrain. */
  static void giveStarterChest(ServerPlayer player) {
    ResourceLocation file =
        ResourceLocation.fromNamespaceAndPath("mars", "terrain/starter_chest.json");
    var res = player.server.getResourceManager().getResource(file);
    if (res.isEmpty()) {
      LOG.warn("Starter chest data missing: {}", file);
      return;
    }
    try (Reader r = new InputStreamReader(res.get().open(), StandardCharsets.UTF_8)) {
      StarterChest chest = StarterChest.parse(r, file.toString());
      List<ItemStack> contents = new ArrayList<>();
      for (StarterChest.Entry e : chest.items()) {
        contents.add(
            new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse(e.id())), e.count()));
      }
      ItemStack stack = new ItemStack(Items.CHEST);
      stack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(contents));
      stack.set(DataComponents.CUSTOM_NAME, Component.translatable(chest.nameKey()));
      if (!player.getInventory().add(stack)) {
        player.drop(stack, false);
      }
      player.sendSystemMessage(Component.translatable(chest.messageKey()));
      LOG.info("Starter chest given to {} (once per world)", player.getName().getString());
    } catch (Exception e) {
      LOG.warn("Starter chest not given: {}", e.toString());
    }
  }
}
