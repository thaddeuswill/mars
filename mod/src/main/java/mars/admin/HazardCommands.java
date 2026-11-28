package mars.admin;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import mars.planets.PlanetData;
import mars.planets.PlanetManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Operator commands for the hazards (slice 5), permission level 2, all logged: {@code /mars dust
 * get}, {@code /mars dust set <stage> [partly]} (every panel), and, from step D, {@code /mars storm
 * ...}. The commands change the planet's record and the blocks follow; they are for testing and for
 * a teacher fixing a class world, never part of the game.
 */
public final class HazardCommands {
  private static final Logger LOG = LoggerFactory.getLogger("mars");

  private HazardCommands() {}

  public static void register(RegisterCommandsEvent event) {
    event
        .getDispatcher()
        .register(
            Commands.literal("mars")
                .then(
                    Commands.literal("dust")
                        .requires(s -> s.hasPermission(2))
                        .then(
                            Commands.literal("get")
                                .executes(ctx -> reply(ctx, dustText(level(ctx)))))
                        .then(
                            Commands.literal("set")
                                .then(
                                    Commands.argument("stage", IntegerArgumentType.integer(0, 4))
                                        .executes(ctx -> set(ctx, false))
                                        .then(
                                            Commands.literal("partly")
                                                .executes(ctx -> set(ctx, true))))))
                .then(
                    Commands.literal("storm")
                        .requires(s -> s.hasPermission(2))
                        .then(
                            Commands.literal("get")
                                .executes(ctx -> reply(ctx, stormText(level(ctx)))))
                        .then(
                            Commands.literal("schedule")
                                .then(
                                    Commands.argument(
                                            "in_sols", DoubleArgumentType.doubleArg(0.01, 100))
                                        .executes(ctx -> schedule(ctx, 0.75))
                                        .then(
                                            Commands.argument(
                                                    "length_sols",
                                                    DoubleArgumentType.doubleArg(0.125, 5))
                                                .executes(
                                                    ctx ->
                                                        schedule(
                                                            ctx,
                                                            DoubleArgumentType.getDouble(
                                                                ctx, "length_sols"))))))
                        .then(Commands.literal("end").executes(HazardCommands::end))));
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

  private static int reply(CommandContext<CommandSourceStack> ctx, String text) {
    ctx.getSource().sendSuccess(() -> Component.literal(text), false);
    return 1;
  }

  /** Plain text: how many panels at each stage, the loss, the interval. */
  public static String dustText(ServerLevel level) {
    var sim = PlanetManager.data(level).sim();
    int[] c = sim.panelStageCounts();
    StringBuilder sb = new StringBuilder("Panels by dust stage:");
    for (int i = 0; i < c.length; i++) {
      sb.append(' ').append(i).append('=').append(c[i]);
    }
    sb.append(
        String.format(
            java.util.Locale.ROOT, "; power lost to dust %d %%", sim.panelDustLossPercent()));
    if (sim.hasHazards()) {
      sb.append(
          String.format(
              java.util.Locale.ROOT,
              "; a stage every %.2f sols (storm cap %.2f)",
              sim.dustIntervalSols(),
              sim.stormCap()));
    }
    return sb.toString();
  }

  private static int set(CommandContext<CommandSourceStack> ctx, boolean partly)
      throws CommandSyntaxException {
    ServerLevel l = level(ctx);
    int stage = IntegerArgumentType.getInteger(ctx, "stage");
    PlanetData d = PlanetManager.data(l);
    int n = d.sim().setAllPanels(stage, partly);
    d.setDirty();
    PlanetManager.syncPanels(l, d);
    LOG.info(
        "{} set {} panels to dust stage {} (partly cleaned {})",
        ctx.getSource().getTextName(),
        n,
        stage,
        partly);
    return reply(
        ctx, n + " panel(s) set to dust stage " + stage + (partly ? ", partly cleaned" : "") + ".");
  }

  /** Plain text: the storm's state, strength, time left and the next onset. */
  public static String stormText(ServerLevel level) {
    var sim = PlanetManager.data(level).sim();
    return String.format(
        java.util.Locale.ROOT,
        "Storm %s, strength %.2f (cap %.2f), %.2f sols to %s; onset tick %d, end tick %d, now tick %d.",
        sim.stormMode(),
        sim.stormStrength(),
        sim.stormCap(),
        sim.stormEtaSols(),
        sim.stormMode().name().startsWith("ACTIVE") || sim.stormMode().name().startsWith("END")
            ? "the end"
            : "onset",
        sim.stormOnsetTick(),
        sim.stormEndTick(),
        sim.model().simTicks());
  }

  private static int schedule(CommandContext<CommandSourceStack> ctx, double length)
      throws CommandSyntaxException {
    ServerLevel l = level(ctx);
    double in = DoubleArgumentType.getDouble(ctx, "in_sols");
    PlanetData d = PlanetManager.data(l);
    if (!d.sim().scheduleStorm(in, length)) {
      throw new SimpleCommandExceptionType(
              Component.literal(
                  "A storm is already scheduled or under way. /mars storm end first."))
          .create();
    }
    d.setDirty();
    LOG.info(
        "{} scheduled a storm in {} sols, {} sols long", ctx.getSource().getTextName(), in, length);
    return reply(ctx, "Storm scheduled in " + in + " sol(s), " + length + " sol(s) long.");
  }

  private static int end(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
    ServerLevel l = level(ctx);
    PlanetData d = PlanetManager.data(l);
    if (!d.sim().endStorm()) {
      throw new SimpleCommandExceptionType(Component.literal("No storm is active.")).create();
    }
    d.setDirty();
    LOG.info("{} ended the storm", ctx.getSource().getTextName());
    return reply(ctx, "The storm is clearing.");
  }
}
