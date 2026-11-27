package mars.admin;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;
import mars.index.sim.Pace;
import mars.index.sim.PlanetReport;
import mars.planets.CheckpointData;
import mars.planets.Checkpoints;
import mars.planets.PlanetData;
import mars.planets.PlanetManager;
import mars.planets.SessionData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Slice 8 operator commands (ARCHITECTURE.md 10, 7.4a), all permission level 2 and all logged:
 * {@code /mars machines}, {@code /mars checkpoint save|list|restore}, {@code /mars index export}.
 * Rollback is a checkpoint restore; there is no command that un-latches a single gate.
 */
public final class AdminCommands {
  private static final Logger LOG = LoggerFactory.getLogger("mars");
  private static final int MAX_POSITIONS = 8;

  private AdminCommands() {}

  public static void register(RegisterCommandsEvent event) {
    event
        .getDispatcher()
        .register(
            Commands.literal("mars")
                .then(
                    Commands.literal("machines")
                        .requires(s -> s.hasPermission(2))
                        .executes(ctx -> reply(ctx, machinesText(level(ctx)))))
                .then(
                    Commands.literal("index")
                        .requires(s -> s.hasPermission(2))
                        .then(
                            Commands.literal("export")
                                .executes(
                                    ctx -> {
                                      ServerLevel l = level(ctx);
                                      Path dir =
                                          l.getServer()
                                              .getServerDirectory()
                                              .resolve("mars-data")
                                              .resolve("exports");
                                      try {
                                        Path file = export(l, ctx.getSource().getTextName(), dir);
                                        return reply(ctx, "Planet report written to " + file);
                                      } catch (IOException e) {
                                        throw fail("Could not write the report: " + e.getMessage());
                                      }
                                    })))
                .then(
                    Commands.literal("checkpoint")
                        .requires(s -> s.hasPermission(2))
                        .then(
                            Commands.literal("save")
                                .executes(ctx -> save(ctx, "manual"))
                                .then(
                                    Commands.argument("name", StringArgumentType.word())
                                        .executes(
                                            ctx ->
                                                save(
                                                    ctx,
                                                    StringArgumentType.getString(ctx, "name")))))
                        .then(
                            Commands.literal("list")
                                .executes(ctx -> reply(ctx, listText(level(ctx)))))
                        .then(
                            Commands.literal("restore")
                                .then(
                                    Commands.argument("name", StringArgumentType.word())
                                        .executes(AdminCommands::restore)))));
  }

  private static SimpleCommandExceptionType err(String m) {
    return new SimpleCommandExceptionType(Component.literal(m));
  }

  private static CommandSyntaxException fail(String m) {
    return err(m).create();
  }

  private static ServerLevel level(CommandContext<CommandSourceStack> ctx)
      throws CommandSyntaxException {
    ServerLevel l = ctx.getSource().getLevel();
    if (!PlanetManager.isPlanet(l)) {
      throw fail("This dimension is not a Mars planet. Use /mars go first.");
    }
    return l;
  }

  private static int reply(CommandContext<CommandSourceStack> ctx, String text) {
    ctx.getSource().sendSuccess(() -> Component.literal(text), false);
    return 1;
  }

  private static int save(CommandContext<CommandSourceStack> ctx, String name)
      throws CommandSyntaxException {
    ServerLevel l = level(ctx);
    if (Checkpoints.START.equals(name)) {
      throw fail("\"start\" is reserved for the start state. Pick another name.");
    }
    String dropped = Checkpoints.take(l, name, ctx.getSource().getTextName());
    String sol = Double.toString(round(PlanetManager.data(l).sim().model().sols()));
    ctx.getSource()
        .sendSuccess(
            () ->
                Component.literal(
                    "Checkpoint \""
                        + name
                        + "\" saved at sol "
                        + sol
                        + (dropped == null
                            ? "."
                            : ". The oldest one (" + dropped + ") was dropped.")),
            true);
    return 1;
  }

  private static int restore(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
    ServerLevel l = level(ctx);
    String name = StringArgumentType.getString(ctx, "name");
    if (!Checkpoints.restore(l, name, ctx.getSource().getTextName())) {
      throw fail("No checkpoint called \"" + name + "\". Try /mars checkpoint list.");
    }
    ctx.getSource()
        .sendSuccess(
            () ->
                Component.literal(
                    "Planet restored to \""
                        + name
                        + "\". Machines and blocks stay as they are now; only the planet's"
                        + " numbers, goals and history went back."),
            true);
    return 1;
  }

  /** Plain text list of the planet's machines (counts, and the first few positions). */
  public static String machinesText(ServerLevel level) {
    PlanetData d = PlanetManager.data(level);
    Map<String, java.util.List<Long>> machines = new TreeMap<>(d.sim().toState().machines());
    StringBuilder sb = new StringBuilder("Machines on this planet:");
    if (machines.values().stream().allMatch(java.util.List::isEmpty)) {
      sb.append(" none");
    }
    machines.forEach(
        (type, list) -> {
          if (list.isEmpty()) {
            return;
          }
          sb.append("\n  ").append(type).append(" x").append(list.size());
          int reduced = d.sim().reducedCount(type);
          if (reduced > 0) {
            sb.append(" (").append(reduced).append(" below full output)");
          }
          sb.append(": ");
          for (int i = 0; i < Math.min(MAX_POSITIONS, list.size()); i++) {
            sb.append(i == 0 ? "" : ", ")
                .append(net.minecraft.core.BlockPos.of(list.get(i)).toShortString());
          }
          if (list.size() > MAX_POSITIONS) {
            sb.append(", ...");
          }
        });
    return sb.toString();
  }

  /** Plain text list of this planet's checkpoints. */
  public static String listText(ServerLevel level) {
    var list =
        CheckpointData.get(level.getServer()).of(level.dimension().location().toString()).list();
    StringBuilder sb = new StringBuilder("Checkpoints (restore start = start values):");
    if (list.isEmpty()) {
      sb.append(" none yet");
    }
    for (var e : list) {
      sb.append("\n  ").append(e.name()).append("  sol ").append(round(e.sol()));
    }
    return sb.toString();
  }

  /** Write the readable planet report into {@code dir}; returns the file. Planet numbers only. */
  public static Path export(ServerLevel level, String actor, Path dir) throws IOException {
    PlanetData d = PlanetManager.data(level);
    var clock = SessionData.get(level.getServer()).clock();
    String id = level.dimension().location().toString();
    String text =
        PlanetReport.text(
            id, d.sim().toState(), Pace.STANDARD, clock.sessionNumber(), clock.paused());
    Files.createDirectories(dir);
    String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
    Path file = dir.resolve(stamp + "_" + id.replace(':', '_') + ".txt");
    Files.writeString(file, text, StandardCharsets.UTF_8);
    LOG.info("ADMIN {} exported the planet report for {} to {}", actor, id, file);
    return file;
  }

  private static double round(double v) {
    return Math.round(v * 100.0) / 100.0;
  }
}
