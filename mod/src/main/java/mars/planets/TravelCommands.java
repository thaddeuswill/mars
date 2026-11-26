package mars.planets;

import com.mojang.brigadier.CommandDispatcher;
import mars.dimension.MarsDimensions;
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
    ServerLevel mars = src.getServer().getLevel(MarsDimensions.PLANET_1);
    if (mars == null) {
      src.sendFailure(Component.translatable("commands.mars.go.missing"));
      return 0;
    }
    int y = mars.getHeight(Heightmap.Types.MOTION_BLOCKING, 0, 0);
    player.teleportTo(mars, 0.5, y, 0.5, player.getYRot(), player.getXRot());
    src.sendSuccess(() -> Component.translatable("commands.mars.go.success"), false);
    return 1;
  }

  private static int back(CommandSourceStack src)
      throws com.mojang.brigadier.exceptions.CommandSyntaxException {
    ServerPlayer player = src.getPlayerOrException();
    ServerLevel overworld = src.getServer().overworld();
    BlockPos spawn = overworld.getSharedSpawnPos();
    int y = overworld.getHeight(Heightmap.Types.MOTION_BLOCKING, spawn.getX(), spawn.getZ());
    player.teleportTo(
        overworld, spawn.getX() + 0.5, y, spawn.getZ() + 0.5, player.getYRot(), player.getXRot());
    src.sendSuccess(() -> Component.translatable("commands.mars.back.success"), false);
    return 1;
  }
}
