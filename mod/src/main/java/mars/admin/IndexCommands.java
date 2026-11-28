package mars.admin;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import mars.core.ParameterId;
import mars.monitor.MonitorSender;
import mars.monitor.view.Fmt;
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
 * Debug and admin commands for the Index (ARCHITECTURE.md 10), operators only (permission level 2).
 * Every use is written to the server log with who ran it; once the learning layer exists these
 * become {@code Cause.ADMIN} events.
 */
public final class IndexCommands {
  private static final Logger LOG = LoggerFactory.getLogger("mars");
  private static final SimpleCommandExceptionType NOT_A_PLANET =
      new SimpleCommandExceptionType(Component.literal("This dimension is not a Mars planet."));

  private IndexCommands() {}

  public static void register(RegisterCommandsEvent event) {
    event
        .getDispatcher()
        .register(
            Commands.literal("mars")
                .then(
                    Commands.literal("index")
                        .requires(s -> s.hasPermission(2))
                        .then(Commands.literal("get").executes(IndexCommands::get))
                        .then(
                            Commands.literal("advance")
                                .then(
                                    Commands.argument("sols", DoubleArgumentType.doubleArg(0, 200))
                                        .executes(IndexCommands::advance)))
                        .then(
                            Commands.literal("set")
                                .then(
                                    Commands.argument("parameter", StringArgumentType.word())
                                        .then(
                                            Commands.argument(
                                                    "value", DoubleArgumentType.doubleArg())
                                                .executes(IndexCommands::set)))))
                .then(
                    Commands.literal("gate")
                        .requires(s -> s.hasPermission(2))
                        .then(Commands.literal("list").executes(IndexCommands::gateList))
                        .then(
                            Commands.literal("latch")
                                .then(
                                    Commands.argument("gate", StringArgumentType.word())
                                        .executes(IndexCommands::gateLatch))))
                .then(
                    Commands.literal("pace")
                        .requires(s -> s.hasPermission(2))
                        .executes(ctx -> paceShow(ctx))
                        .then(paceMode("standard"))
                        .then(paceMode("classroom").requires(s -> classroomOffered())))
                .then(
                    Commands.literal("monitor")
                        .requires(s -> s.hasPermission(2))
                        .executes(
                            ctx -> {
                              MonitorSender.open(ctx.getSource().getPlayerOrException());
                              return 1;
                            })));
  }

  /** False while pace.json has classroom_enabled off: the word is not offered in tab completion. */
  private static boolean classroomOffered() {
    try {
      return mars.index.IndexDefinitions.require().pace().classroomOffered();
    } catch (RuntimeException notLoaded) {
      return false;
    }
  }

  private static com.mojang.brigadier.builder.LiteralArgumentBuilder<CommandSourceStack> paceMode(
      String name) {
    return Commands.literal(name)
        .executes(ctx -> paceSet(ctx, name, null))
        .then(
            Commands.argument(
                    "group_size", com.mojang.brigadier.arguments.IntegerArgumentType.integer(1, 5))
                .executes(
                    ctx ->
                        paceSet(
                            ctx,
                            name,
                            com.mojang.brigadier.arguments.IntegerArgumentType.getInteger(
                                ctx, "group_size"))));
  }

  /** {@code /mars pace}: show the world's pace. */
  private static int paceShow(CommandContext<CommandSourceStack> ctx) {
    mars.index.sim.Pace p = PlanetManager.pace(ctx.getSource().getLevel());
    ctx.getSource().sendSuccess(() -> Component.literal(paceText(p)), false);
    return 1;
  }

  static String paceText(mars.index.sim.Pace p) {
    return "Pace: "
        + (p.classroom() ? "classroom" : "standard")
        + ", rates x"
        + Fmt.number(p.rateMultiplier(), 1)
        + ", group size "
        + p.groupSize()
        + ", creative gate bypass "
        + (p.creativeGateBypass() ? "on" : "off")
        + ". Thresholds are never scaled.";
  }

  /**
   * {@code /mars pace standard|classroom [group_size]}: operator only, logged, saved in the world.
   */
  private static int paceSet(CommandContext<CommandSourceStack> ctx, String mode, Integer group)
      throws CommandSyntaxException {
    var server = ctx.getSource().getServer();
    String refused =
        mars.index.IndexDefinitions.require()
            .pace()
            .refusal(mars.index.sim.PaceConfig.Mode.parse(mode));
    if (refused != null) {
      // Nothing happens: no checkpoint, no change.
      throw new SimpleCommandExceptionType(Component.literal(refused)).create();
    }
    try {
      PlanetManager.setPace(server, mars.index.sim.PaceConfig.Mode.parse(mode), group);
    } catch (IllegalArgumentException e) {
      throw new SimpleCommandExceptionType(Component.literal(e.getMessage())).create();
    }
    mars.index.sim.Pace p = PlanetManager.pace(ctx.getSource().getLevel());
    LOG.info("ADMIN {} set the pace: {}", ctx.getSource().getTextName(), paceText(p));
    ctx.getSource().sendSuccess(() -> Component.literal("Set. " + paceText(p)), true);
    return 1;
  }

  private static PlanetData planet(CommandContext<CommandSourceStack> ctx)
      throws CommandSyntaxException {
    ServerLevel level = ctx.getSource().getLevel();
    if (!PlanetManager.isPlanet(level)) {
      throw NOT_A_PLANET.create();
    }
    return PlanetManager.data(level);
  }

  private static int get(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
    PlanetData d = planet(ctx);
    StringBuilder sb =
        new StringBuilder("Index at sol " + Fmt.number(d.sim().model().sols(), 2) + ":");
    d.sim()
        .model()
        .snapshot()
        .asMap()
        .forEach(
            (id, v) ->
                sb.append("\n  ")
                    .append(id)
                    .append(" = ")
                    .append(Double.isNaN(v) ? "n/a" : Fmt.number(v, 3)));
    ctx.getSource().sendSuccess(() -> Component.literal(sb.toString()), false);
    return 1;
  }

  private static int advance(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
    PlanetData d = planet(ctx);
    double sols = DoubleArgumentType.getDouble(ctx, "sols");
    d.sim().model().advanceSols(sols);
    d.setDirty();
    LOG.info("ADMIN {} advanced the planet by {} sols", ctx.getSource().getTextName(), sols);
    ctx.getSource()
        .sendSuccess(
            () -> Component.literal("Advanced " + Fmt.number(sols, 2) + " sols (admin)."), true);
    return 1;
  }

  private static int set(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
    PlanetData d = planet(ctx);
    String name = StringArgumentType.getString(ctx, "parameter");
    ParameterId id = ParameterId.of(name.contains(":") ? name : "mars:" + name);
    double value = DoubleArgumentType.getDouble(ctx, "value");
    try {
      d.sim().model().set(id, value);
    } catch (IllegalArgumentException e) {
      throw new SimpleCommandExceptionType(Component.literal(e.getMessage())).create();
    }
    d.setDirty();
    LOG.info("ADMIN {} set {} to {}", ctx.getSource().getTextName(), id, value);
    ctx.getSource().sendSuccess(() -> Component.literal("Set " + id + " (admin)."), true);
    return 1;
  }

  private static int gateList(CommandContext<CommandSourceStack> ctx)
      throws CommandSyntaxException {
    PlanetData d = planet(ctx);
    StringBuilder sb = new StringBuilder("Gates:");
    for (mars.gates.GateDef g : d.sim().gates().defs()) {
      var st = d.sim().gates().state(g.id());
      sb.append("\n  ").append(g.id()).append(": ");
      if (st.latched()) {
        sb.append("reached (").append(st.cause()).append(")");
      } else if (g.hold() != null) {
        sb.append("held ")
            .append(Fmt.number(st.heldSols(), 2))
            .append(" of ")
            .append(Fmt.number(d.sim().gates().holdSols(g), 0))
            .append(" sols");
      } else {
        sb.append("not reached");
      }
    }
    ctx.getSource().sendSuccess(() -> Component.literal(sb.toString()), false);
    return 1;
  }

  /** Admin route: latch a gate now. Gates never un-latch, so there is no reverse command. */
  private static int gateLatch(CommandContext<CommandSourceStack> ctx)
      throws CommandSyntaxException {
    PlanetData d = planet(ctx);
    String name = StringArgumentType.getString(ctx, "gate");
    String id = name.contains(":") ? name : "mars:" + name;
    if (!d.sim()
        .gates()
        .latch(id, mars.gates.GateTracker.Cause.ADMIN, d.sim().model().simTicks())) {
      throw new SimpleCommandExceptionType(
              Component.literal("No such gate, or it is already reached: " + id))
          .create();
    }
    d.sim().queueAnnouncement(id);
    d.setDirty();
    LOG.info("ADMIN {} latched gate {}", ctx.getSource().getTextName(), id);
    PlanetManager.announceGates(ctx.getSource().getLevel(), d);
    return 1;
  }
}
