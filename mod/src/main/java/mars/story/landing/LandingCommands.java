package mars.story.landing;

import com.mojang.brigadier.context.CommandContext;
import mars.planets.PlanetManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/**
 * {@code /mars landing status|start|skip|skip_step} for the player, {@code reset} and {@code /mars
 * journal give} for operators (landing-spec T7). Each acts on the player who runs it.
 */
public final class LandingCommands {
  private LandingCommands() {}

  public static void register(RegisterCommandsEvent event) {
    event
        .getDispatcher()
        .register(
            Commands.literal("mars")
                .then(
                    Commands.literal("landing")
                        .then(Commands.literal("status").executes(c -> status(c)))
                        .then(Commands.literal("start").executes(c -> act(c, 0)))
                        .then(Commands.literal("skip").executes(c -> act(c, 1)))
                        .then(Commands.literal("skip_step").executes(c -> act(c, 2)))
                        .then(
                            Commands.literal("step")
                                .requires(s -> s.hasPermission(2))
                                .then(
                                    Commands.argument(
                                            "n",
                                            com.mojang.brigadier.arguments.IntegerArgumentType
                                                .integer(1, 8))
                                        .executes(
                                            c -> {
                                              ServerPlayer p = c.getSource().getPlayer();
                                              if (p == null) {
                                                return 0;
                                              }
                                              LandingService.markFor(
                                                  p,
                                                  mars.landing.Step.of(
                                                      com.mojang.brigadier.arguments
                                                          .IntegerArgumentType.getInteger(c, "n")));
                                              return 1;
                                            })))
                        .then(
                            Commands.literal("reset")
                                .requires(s -> s.hasPermission(2))
                                .executes(c -> act(c, 3))))
                .then(
                    Commands.literal("journal")
                        .then(
                            Commands.literal("give")
                                .requires(s -> s.hasPermission(2))
                                .executes(c -> act(c, 4)))));
  }

  private static int act(CommandContext<CommandSourceStack> c, int what) {
    ServerPlayer p = c.getSource().getPlayer();
    if (p == null || !PlanetManager.isPlanet(p.serverLevel())) {
      c.getSource().sendFailure(Component.translatable("landing.command.no_planet"));
      return 0;
    }
    switch (what) {
      case 0 -> {
        LandingService.startLanding(p);
      }
      case 1 -> LandingService.skipAll(p);
      case 2 -> LandingService.skipStep(p);
      case 3 -> LandingService.resetFor(p);
      default -> LandingService.grantBook(p, LandingService.rec(p));
    }
    return 1;
  }

  private static int status(CommandContext<CommandSourceStack> c) {
    ServerPlayer p = c.getSource().getPlayer();
    if (p == null || !PlanetManager.isPlanet(p.serverLevel())) {
      c.getSource().sendFailure(Component.translatable("landing.command.no_planet"));
      return 0;
    }
    var l = LandingService.rec(p).landing;
    StringBuilder sb = new StringBuilder("Landing: " + l.phase() + " [");
    for (var s : mars.landing.Step.values()) {
      sb.append(l.status(s).name().charAt(0));
    }
    c.getSource().sendSuccess(() -> Component.literal(sb + "]"), false);
    return 1;
  }
}
