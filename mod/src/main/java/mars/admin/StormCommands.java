package mars.admin;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import mars.hazards.storm.StormSchedule;
import mars.monitor.view.Fmt;
import mars.planets.PlanetData;
import mars.planets.PlanetManager;
import mars.planets.StormEvents;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code /mars storm start [hold]}, {@code end}, {@code status}: operators only (permission 2),
 * every change logged with the actor. A started storm behaves like a scheduled one (cut on the
 * grid, dust at its end); an ended one still dusts the panels, and the next scheduled storm is
 * drawn again from the seeded generator.
 */
public final class StormCommands {
  private static final Logger LOG = LoggerFactory.getLogger("mars");

  private StormCommands() {}

  public static void register(RegisterCommandsEvent event) {
    event
        .getDispatcher()
        .register(
            Commands.literal("mars")
                .then(
                    Commands.literal("storm")
                        .requires(s -> s.hasPermission(2))
                        .then(
                            Commands.literal("start")
                                .executes(ctx -> start(ctx, StormSchedule.Kind.PHASE_1))
                                .then(
                                    Commands.literal("hold")
                                        .executes(ctx -> start(ctx, StormSchedule.Kind.HOLD))))
                        .then(Commands.literal("end").executes(StormCommands::end))
                        .then(Commands.literal("status").executes(StormCommands::status))));
  }

  private static ServerLevel level(CommandContext<CommandSourceStack> ctx)
      throws CommandSyntaxException {
    ServerLevel l = ctx.getSource().getLevel();
    if (!PlanetManager.isPlanet(l)) {
      throw new SimpleCommandExceptionType(
              Component.literal("This dimension is not a Mars planet. Use /mars go first."))
          .create();
    }
    return l;
  }

  private static int reply(CommandContext<CommandSourceStack> ctx, String text, boolean log) {
    ctx.getSource().sendSuccess(() -> Component.literal(text), log);
    return 1;
  }

  private static int start(CommandContext<CommandSourceStack> ctx, StormSchedule.Kind kind)
      throws CommandSyntaxException {
    ServerLevel l = level(ctx);
    PlanetData d = PlanetManager.data(l);
    if (d.sim().storm() == null) {
      return reply(ctx, "This planet has no storm schedule.", false);
    }
    if (!d.sim().stormStart(kind)) {
      return reply(ctx, "A storm is already on.", false);
    }
    LOG.info("storm.start ({}) by {}", kind, ctx.getSource().getTextName());
    StormEvents.announce(l, d);
    return reply(
        ctx, "Storm started (" + kind.name().toLowerCase(java.util.Locale.ROOT) + ").", true);
  }

  private static int end(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
    ServerLevel l = level(ctx);
    PlanetData d = PlanetManager.data(l);
    if (!d.sim().stormEnd()) {
      return reply(ctx, "No storm is on.", false);
    }
    LOG.info("storm.end by {}", ctx.getSource().getTextName());
    StormEvents.announce(l, d);
    return reply(ctx, "Storm ended. The panels gained their dust.", true);
  }

  private static int status(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
    var sim = PlanetManager.data(level(ctx)).sim();
    var st = sim.storm();
    if (st == null) {
      return reply(ctx, "This planet has no storm schedule.", false);
    }
    double now = sim.model().sols();
    double min = sim.pace().solTicks() / 1200.0;
    String text =
        switch (st.phase()) {
          case CALM ->
              "Calm. Next storm at sol "
                  + Fmt.number(st.startSol(), 2)
                  + " (warning at sol "
                  + Fmt.number(st.startSol() - st.warningSols(), 2)
                  + ").";
          case WARNING ->
              "Warning: storm arrives in "
                  + Fmt.duration(st.solsUntilStart(now), min)
                  + " (sol "
                  + Fmt.number(st.startSol(), 2)
                  + ").";
          case ACTIVE ->
              "Storm on ("
                  + st.kind().name().toLowerCase(java.util.Locale.ROOT)
                  + "): "
                  + Fmt.duration(st.solsLeft(now), min)
                  + " left, sunlight x"
                  + Fmt.number(sim.stormSolarFactor(), 2)
                  + ".";
        };
    return reply(
        ctx,
        text + " Sol " + Fmt.number(now, 2) + ", storms so far " + st.stormsEnded() + ".",
        false);
  }
}
