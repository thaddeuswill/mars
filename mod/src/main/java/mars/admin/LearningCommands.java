package mars.admin;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.List;
import java.util.Locale;
import mars.core.Guidance;
import mars.learning.JournalEntryDef;
import mars.planets.LearningService;
import mars.planets.PlanetManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Learning-layer commands (ARCHITECTURE.md 2.20): {@code /mars guidance [level] [player]}, {@code
 * /mars journal [n]} and the operator-only {@code /mars debug forecast open|close [value]}.
 */
public final class LearningCommands {
  private static final Logger LOG = LoggerFactory.getLogger("mars");

  private LearningCommands() {}

  public static void register(RegisterCommandsEvent event) {
    event
        .getDispatcher()
        .register(
            Commands.literal("mars")
                .then(
                    Commands.literal("guidance")
                        .executes(LearningCommands::guidanceShow)
                        .then(
                            Commands.argument("level", StringArgumentType.word())
                                .suggests(
                                    (c, b) ->
                                        net.minecraft.commands.SharedSuggestionProvider.suggest(
                                            List.of("gentle", "standard", "expert"), b))
                                .executes(LearningCommands::guidanceSelf)
                                .then(
                                    Commands.argument("player", EntityArgument.player())
                                        .requires(s -> s.hasPermission(2))
                                        .executes(LearningCommands::guidanceOther))))
                .then(
                    Commands.literal("journal")
                        .executes(LearningCommands::journalList)
                        .then(
                            Commands.argument("n", IntegerArgumentType.integer(1))
                                .executes(LearningCommands::journalEntry)))
                .then(
                    Commands.literal("debug")
                        .requires(s -> s.hasPermission(2))
                        .then(
                            Commands.literal("forecast")
                                .then(forecast("open", true))
                                .then(forecast("close", false)))));
  }

  private static com.mojang.brigadier.builder.LiteralArgumentBuilder<CommandSourceStack> forecast(
      String name, boolean open) {
    return Commands.literal(name)
        .executes(ctx -> debugForecast(ctx, "mars:pressure", open))
        .then(
            Commands.argument("value", StringArgumentType.word())
                .executes(
                    ctx -> {
                      String v = StringArgumentType.getString(ctx, "value");
                      return debugForecast(ctx, v.contains(":") ? v : "mars:" + v, open);
                    }));
  }

  private static CommandSyntaxException fail(String m) {
    return new SimpleCommandExceptionType(Component.literal(m)).create();
  }

  private static int guidanceShow(CommandContext<CommandSourceStack> ctx)
      throws CommandSyntaxException {
    ServerPlayer p = ctx.getSource().getPlayerOrException();
    Guidance g = LearningService.effective(p);
    ctx.getSource()
        .sendSuccess(
            () ->
                Component.translatable(
                    "guidance.current",
                    g.id(),
                    Component.translatable("guidance.effect." + g.id())),
            false);
    return 1;
  }

  private static int guidanceSelf(CommandContext<CommandSourceStack> ctx)
      throws CommandSyntaxException {
    ServerPlayer p = ctx.getSource().getPlayerOrException();
    return applyGuidance(ctx, p, ctx.getSource().hasPermission(2), true);
  }

  private static int guidanceOther(CommandContext<CommandSourceStack> ctx)
      throws CommandSyntaxException {
    return applyGuidance(
        ctx, EntityArgument.getPlayer(ctx, "player"), ctx.getSource().hasPermission(2), false);
  }

  private static int applyGuidance(
      CommandContext<CommandSourceStack> ctx, ServerPlayer target, boolean op, boolean self)
      throws CommandSyntaxException {
    String text = StringArgumentType.getString(ctx, "level").toLowerCase(Locale.ROOT);
    Guidance level =
        Guidance.parse(text)
            .orElseThrow(
                () ->
                    new SimpleCommandExceptionType(Component.translatable("guidance.unknown"))
                        .create());
    boolean classroom = LearningService.classroom(target.serverLevel());
    var result =
        LearningService.setGuidance(
            ctx.getSource().getServer(),
            target.getUUID(),
            level,
            classroom,
            op,
            ctx.getSource().getTextName());
    switch (result) {
      case TEACHER_ONLY ->
          throw new SimpleCommandExceptionType(Component.translatable("guidance.teacher_sets"))
              .create();
      case EXPERT_REFUSED ->
          throw new SimpleCommandExceptionType(Component.translatable("guidance.expert_refused"))
              .create();
      case OK -> {
        ctx.getSource()
            .sendSuccess(
                () ->
                    self
                        ? Component.translatable("guidance.set", level.id())
                        : Component.translatable(
                            "guidance.set.other", target.getDisplayName(), level.id()),
                false);
        return 1;
      }
    }
    return 0;
  }

  private static int journalList(CommandContext<CommandSourceStack> ctx)
      throws CommandSyntaxException {
    ServerPlayer p = ctx.getSource().getPlayerOrException();
    if (!PlanetManager.isPlanet(p.serverLevel())) {
      throw fail("Your journal belongs to a planet. Go to Mars first.");
    }
    List<JournalEntryDef> list = LearningService.unlocked(p.serverLevel());
    if (list.isEmpty()) {
      ctx.getSource().sendSuccess(() -> Component.translatable("journal.list.empty"), false);
      return 0;
    }
    var unread = LearningService.settings(p.server).unread(p.getUUID(), list);
    ctx.getSource()
        .sendSuccess(() -> Component.translatable("journal.list.header", list.size()), false);
    for (int i = 0; i < list.size(); i++) {
      JournalEntryDef d = list.get(i);
      int n = i + 1;
      boolean isNew = unread.contains(d.id());
      ctx.getSource()
          .sendSuccess(
              () ->
                  Component.translatable(
                      "journal.list.line",
                      n,
                      Component.translatable(d.titleKey()),
                      isNew ? Component.translatable("journal.list.new") : Component.empty()),
              false);
    }
    return list.size();
  }

  private static int journalEntry(CommandContext<CommandSourceStack> ctx)
      throws CommandSyntaxException {
    ServerPlayer p = ctx.getSource().getPlayerOrException();
    List<JournalEntryDef> list = LearningService.unlocked(p.serverLevel());
    int n = IntegerArgumentType.getInteger(ctx, "n");
    if (n > list.size()) {
      throw fail("No journal entry " + n + ".");
    }
    JournalEntryDef d = list.get(n - 1);
    var data = mars.planets.PlayerSettingsData.get(p.server);
    data.settings().markSeen(p.getUUID(), d.id());
    data.setDirty();
    var gateName =
        PlanetManager.data(p.serverLevel()).sim().gates().defs().stream()
            .filter(g -> g.id().equals(d.gate()))
            .map(g -> Component.translatable(g.nameKey()))
            .findFirst()
            .orElse(Component.literal(d.gate()));
    ctx.getSource()
        .sendSuccess(
            () ->
                Component.translatable(d.titleKey())
                    .withStyle(net.minecraft.ChatFormatting.BOLD)
                    .append("\n")
                    .append(Component.translatable(d.textKey() + ".ms"))
                    .append("\n")
                    .append(Component.translatable("journal.entry.unlocked_at", gateName)),
            false);
    return 1;
  }

  /**
   * Debug: mark a value as asked about by an open forecast for the player who runs the command, to
   * test CR-T8 hiding before real forecasts exist. Creates no learning event and fills no Record
   * page (CR-R1, CR-R2); it is only logged.
   */
  private static int debugForecast(
      CommandContext<CommandSourceStack> ctx, String valueId, boolean open)
      throws CommandSyntaxException {
    ServerPlayer p = ctx.getSource().getPlayerOrException();
    if (open) {
      LearningService.openForecasts().open(p.getUUID(), valueId);
    } else {
      LearningService.openForecasts().close(p.getUUID(), valueId);
    }
    LOG.info(
        "DEBUG forecast {} {} for {} (no learning event)",
        open ? "open" : "close",
        valueId,
        p.getUUID());
    ctx.getSource()
        .sendSuccess(
            () ->
                Component.literal(
                    "Debug: forecast about "
                        + valueId
                        + (open ? " opened" : " closed")
                        + " for you. This is not a learning event."),
            false);
    return 1;
  }
}
