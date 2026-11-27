package mars.admin;

import mars.planets.SessionControl;
import mars.planets.SessionData;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/** {@code /mars pause}, {@code /mars resume}, {@code /mars session}: operators only. */
public final class SessionCommands {
  private SessionCommands() {}

  public static void register(RegisterCommandsEvent event) {
    event
        .getDispatcher()
        .register(
            Commands.literal("mars")
                .then(
                    Commands.literal("pause")
                        .requires(s -> s.hasPermission(2))
                        .executes(
                            ctx -> {
                              boolean changed =
                                  SessionControl.pause(
                                      ctx.getSource().getServer(), ctx.getSource().getTextName());
                              ctx.getSource()
                                  .sendSuccess(
                                      () ->
                                          Component.literal(
                                              changed ? "Colony paused." : "Already paused."),
                                      true);
                              return 1;
                            }))
                .then(
                    Commands.literal("resume")
                        .requires(s -> s.hasPermission(2))
                        .executes(
                            ctx -> {
                              boolean changed =
                                  SessionControl.resume(
                                      ctx.getSource().getServer(), ctx.getSource().getTextName());
                              ctx.getSource()
                                  .sendSuccess(
                                      () ->
                                          Component.literal(
                                              changed
                                                  ? "Colony resumed. New session started."
                                                  : "Already running."),
                                      true);
                              return 1;
                            }))
                .then(
                    Commands.literal("session")
                        .requires(s -> s.hasPermission(2))
                        .executes(
                            ctx -> {
                              var c = SessionData.get(ctx.getSource().getServer()).clock();
                              ctx.getSource()
                                  .sendSuccess(
                                      () ->
                                          Component.literal(
                                              "Session "
                                                  + c.sessionNumber()
                                                  + (c.paused() ? ": paused" : ": running")),
                                      false);
                              return 1;
                            })));
  }
}
