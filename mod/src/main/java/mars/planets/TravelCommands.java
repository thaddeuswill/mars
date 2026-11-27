package mars.planets;

import com.mojang.brigadier.CommandDispatcher;
import mars.MarsServerConfig;
import mars.dimension.MarsDimensions;
import mars.hull.StarterDome;
import mars.index.IndexDefinitions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/**
 * Stage 1 travel: {@code /mars go} and {@code /mars back}. Open to every player for now (permission
 * level 0) so the owner can test without cheats; Stage 3 replaces this with planet assignment
 * (ARCHITECTURE.md 6.2).
 */
public final class TravelCommands {
  private TravelCommands() {}

  public static void register(RegisterCommandsEvent event) {
    CommandDispatcher<CommandSourceStack> d = event.getDispatcher();
    d.register(
        Commands.literal("mars")
            .then(Commands.literal("go").executes(ctx -> go(ctx.getSource())))
            .then(Commands.literal("back").executes(ctx -> back(ctx.getSource()))));
  }

  private static int go(CommandSourceStack src)
      throws com.mojang.brigadier.exceptions.CommandSyntaxException {
    ServerPlayer player = src.getPlayerOrException();
    if (!sendToMars(player)) {
      src.sendFailure(Component.translatable("commands.mars.go.missing"));
      return 0;
    }
    src.sendSuccess(() -> Component.translatable("commands.mars.go.success"), false);
    return 1;
  }

  /** Moves a player to the surface of planet 1 at x=0, z=0. False if the dimension isn't loaded. */
  public static boolean sendToMars(ServerPlayer player) {
    ServerLevel mars = player.server.getLevel(MarsDimensions.PLANET_1);
    if (mars == null) {
      return false;
    }
    // Load (and generate) the target chunk first: Level.getHeight returns the bottom of the world
    // for an unloaded chunk, which dropped players into the void (found by the Stage 1
    // screenshots).
    mars.getChunk(0, 0);
    // The first arrival on a planet places its starting dome (once per world) and lands in the
    // shelter; the dome needs the pack data (the solar panel reads the dust tuning).
    if (StarterDome.enabled() && IndexDefinitions.isLoaded()) {
      BlockPos spot = StarterDome.ensure(mars, MarsServerConfig.pace().groupSize());
      player.teleportTo(
          mars,
          spot.getX() + 0.5,
          spot.getY(),
          spot.getZ() + 0.5,
          player.getYRot(),
          player.getXRot());
      return true;
    }
    int y = mars.getHeight(Heightmap.Types.MOTION_BLOCKING, 0, 0);
    player.teleportTo(mars, 0.5, y, 0.5, player.getYRot(), player.getXRot());
    return true;
  }

  private static int back(CommandSourceStack src)
      throws com.mojang.brigadier.exceptions.CommandSyntaxException {
    ServerPlayer player = src.getPlayerOrException();
    ServerLevel overworld = src.getServer().overworld();
    BlockPos spawn = overworld.getSharedSpawnPos();
    overworld.getChunk(spawn.getX() >> 4, spawn.getZ() >> 4);
    int y = overworld.getHeight(Heightmap.Types.MOTION_BLOCKING, spawn.getX(), spawn.getZ());
    player.teleportTo(
        overworld, spawn.getX() + 0.5, y, spawn.getZ() + 0.5, player.getYRot(), player.getXRot());
    src.sendSuccess(() -> Component.translatable("commands.mars.back.success"), false);
    return 1;
  }
}
