package mars.learning;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import mars.MarsMod;
import mars.planets.SessionData;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.LoggerFactory;

/** Operator-only export of completed learning sessions from the current world. */
@EventBusSubscriber(modid = MarsMod.MOD_ID)
public final class LearningExportCommand {
  private LearningExportCommand() {}

  @SubscribeEvent
  public static void register(RegisterCommandsEvent event) {
    event
        .getDispatcher()
        .register(
            Commands.literal("mars")
                .then(
                    Commands.literal("learning")
                        .requires(source -> source.hasPermission(2))
                        .then(
                            Commands.literal("export")
                                .executes(context -> start(context.getSource())))));
  }

  private static int start(CommandSourceStack source) {
    var server = source.getServer();
    String world = LearningWorldData.get(server).id();
    int currentSession = SessionData.get(server).clock().sessionNumber();
    Path directory = LearningEvents.dataDirectory(server).resolve(world);
    try {
      LearningEvents.finishEarlierSession(server, currentSession);
    } catch (IOException e) {
      LoggerFactory.getLogger("mars.learning")
          .error("Could not drain completed learning session", e);
      source.sendFailure(
          Component.literal("Could not finish writing the previous learning session."));
      return 0;
    }
    CompletableFuture.runAsync(
        () -> {
          try {
            var file = ActionEventCsv.exportCompleted(directory, world, currentSession);
            server.execute(
                () -> {
                  if (file == null) {
                    source.sendFailure(
                        Component.literal("No completed learning sessions to export yet."));
                  } else {
                    source.sendSuccess(
                        () -> Component.literal("Learning events exported to " + file), false);
                  }
                });
          } catch (IOException e) {
            LoggerFactory.getLogger("mars.learning").error("Learning export failed", e);
            server.execute(
                () -> source.sendFailure(Component.literal("Could not export learning events.")));
          }
        },
        Util.ioPool());
    return 1;
  }
}
