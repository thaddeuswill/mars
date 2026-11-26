package mars.client.dev;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dev-only client helper for repeatable in-game screenshots.
 *
 * <ul>
 *   <li>{@code /mars_shot <name>}: saves {@code screenshots/<name>.png} with Minecraft's own
 *       screenshot function.
 *   <li>{@code /mars_view <x> <y> <z> <yaw> <pitch>}: places the camera (teleports the player,
 *       hides the HUD).
 *   <li>With {@code -Dmars.shots.script=<file>}: creates a fresh creative test world from the title
 *       screen, then runs the script's lines one by one ({@code cmd <command>}, {@code wait
 *       <ticks>}, {@code view x y z yaw pitch}, {@code shot <name>}, {@code hud on|off}, {@code
 *       quit}).
 * </ul>
 */
public final class DevTools {
  private static final Logger LOG = LoggerFactory.getLogger("mars-devtools");
  private static final Deque<String> SCRIPT = new ArrayDeque<>();
  private static boolean worldRequested;
  private static int waitTicks;

  private DevTools() {}

  public static void init() {
    NeoForge.EVENT_BUS.addListener(DevTools::registerCommands);
    String script = System.getProperty("mars.shots.script");
    if (script != null) {
      try {
        List<String> lines = Files.readAllLines(Path.of(script));
        for (String l : lines) {
          if (!l.isBlank() && !l.startsWith("#")) {
            SCRIPT.add(l.trim());
          }
        }
      } catch (IOException e) {
        throw new IllegalStateException("Can't read shot script " + script, e);
      }
      NeoForge.EVENT_BUS.addListener(DevTools::onScreenInit);
      NeoForge.EVENT_BUS.addListener(DevTools::onTick);
      log("script loaded: " + SCRIPT.size() + " steps from " + script);
    }
  }

  private static void registerCommands(RegisterClientCommandsEvent event) {
    event
        .getDispatcher()
        .register(
            Commands.literal("mars_shot")
                .then(
                    Commands.argument("name", StringArgumentType.word())
                        .executes(
                            ctx -> {
                              shot(StringArgumentType.getString(ctx, "name"));
                              return 1;
                            })));
    event
        .getDispatcher()
        .register(
            Commands.literal("mars_view")
                .then(
                    Commands.argument("x", FloatArgumentType.floatArg())
                        .then(
                            Commands.argument("y", FloatArgumentType.floatArg())
                                .then(
                                    Commands.argument("z", FloatArgumentType.floatArg())
                                        .then(
                                            Commands.argument("yaw", FloatArgumentType.floatArg())
                                                .then(
                                                    Commands.argument(
                                                            "pitch", FloatArgumentType.floatArg())
                                                        .executes(
                                                            ctx -> {
                                                              view(
                                                                  FloatArgumentType.getFloat(
                                                                      ctx, "x"),
                                                                  FloatArgumentType.getFloat(
                                                                      ctx, "y"),
                                                                  FloatArgumentType.getFloat(
                                                                      ctx, "z"),
                                                                  FloatArgumentType.getFloat(
                                                                      ctx, "yaw"),
                                                                  FloatArgumentType.getFloat(
                                                                      ctx, "pitch"));
                                                              return 1;
                                                            })))))));
  }

  private static void onScreenInit(ScreenEvent.Init.Post event) {
    // A fresh game directory shows the accessibility welcome screen first; treat it like the title.
    boolean start =
        event.getScreen() instanceof TitleScreen
            || event.getScreen()
                instanceof net.minecraft.client.gui.screens.AccessibilityOnboardingScreen;
    if (worldRequested || !start) {
      return;
    }
    worldRequested = true;
    Minecraft mc = Minecraft.getInstance();
    String name = "mars-shots-" + System.currentTimeMillis();
    log("title screen reached; creating test world " + name);
    LevelSettings settings =
        new LevelSettings(
            name,
            GameType.CREATIVE,
            false,
            Difficulty.PEACEFUL,
            true,
            new GameRules(),
            WorldDataConfiguration.DEFAULT);
    WorldOptions options = new WorldOptions(20261126L, false, false);
    mc.createWorldOpenFlows()
        .createFreshLevel(
            name,
            settings,
            options,
            registries ->
                registries
                    .registryOrThrow(Registries.WORLD_PRESET)
                    .getHolderOrThrow(WorldPresets.FLAT)
                    .value()
                    .createWorldDimensions(),
            event.getScreen());
  }

  private static void onTick(ClientTickEvent.Post event) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.player == null || mc.level == null || SCRIPT.isEmpty()) {
      return;
    }
    if (waitTicks > 0) {
      waitTicks--;
      return;
    }
    String line = SCRIPT.poll();
    log("step: " + line);
    String[] p = line.split(" ", 2);
    switch (p[0]) {
      case "cmd" ->
          mc.player.connection.sendCommand(p[1].startsWith("/") ? p[1].substring(1) : p[1]);
      case "wait" -> waitTicks = Integer.parseInt(p[1]);
      case "view" -> {
        String[] v = p[1].split(" ");
        view(
            Float.parseFloat(v[0]),
            Float.parseFloat(v[1]),
            Float.parseFloat(v[2]),
            Float.parseFloat(v[3]),
            Float.parseFloat(v[4]));
      }
      case "hud" -> mc.options.hideGui = !"on".equals(p[1]);
      case "fps" ->
          log("fps=" + mc.getFps() + " (frames in the last second, as Minecraft counts them)");
      case "shot" -> shot(p[1]);
      case "quit" -> {
        log("quitting");
        mc.stop();
      }
      default -> log("unknown step ignored: " + line);
    }
  }

  private static void view(float x, float y, float z, float yaw, float pitch) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.player == null) {
      return;
    }
    mc.player.connection.sendCommand(
        String.format(
            java.util.Locale.ROOT, "tp @s %.2f %.2f %.2f %.1f %.1f", x, y, z, yaw, pitch));
    mc.options.hideGui = true;
  }

  private static void shot(String name) {
    Minecraft mc = Minecraft.getInstance();
    Screenshot.grab(
        mc.gameDirectory,
        name + ".png",
        mc.getMainRenderTarget(),
        msg -> log("screenshot " + name + ": " + msg.getString()));
  }

  private static void log(String s) {
    LOG.info(s);
    System.out.println("MARS-SHOTS " + s);
  }
}
