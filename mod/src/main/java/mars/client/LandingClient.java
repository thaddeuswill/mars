package mars.client;

import java.util.List;
import mars.story.landing.LandingPayloads;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

/**
 * The Landing checklist panel (landing-spec 3.10): a small HUD layer on the left edge, never over
 * the screen centre. It draws what the server last sent and nothing else. Cost per frame: eight
 * short text lines while the Landing runs; nothing otherwise.
 */
public final class LandingClient {
  private static final int PANEL_W = 150;
  private static final int BACKING = 0xA0181210;
  private static final int EDGE = 0xFF9A6A4C;
  private static volatile LandingPayloads.Panel panel = LandingPayloads.Panel.hidden();

  private LandingClient() {}

  public static void init(IEventBus modBus) {
    modBus.addListener(
        (RegisterGuiLayersEvent e) ->
            e.registerAboveAll(
                ResourceLocation.fromNamespaceAndPath("mars", "landing_panel"),
                (g, delta) -> draw(g)));
  }

  static void setPanel(LandingPayloads.Panel p) {
    panel = p;
  }

  static LandingPayloads.Panel panel() {
    return panel;
  }

  private static String box(int status) {
    return switch (status) {
      case 1 -> "[x] ";
      case 2, 3 -> "[-] ";
      default -> "[ ] ";
    };
  }

  /** The lines as drawn, for the layout test and the draw. */
  static void draw(GuiGraphics g) {
    LandingPayloads.Panel p = panel;
    Minecraft mc = Minecraft.getInstance();
    if (!p.show()
        || mc.options.hideGui
        || mc.screen != null
            && !(mc.screen instanceof net.minecraft.client.gui.screens.ChatScreen)) {
      return;
    }
    var font = mc.font;
    int x = 6;
    int y = 24;
    int h = 14;
    List<List<net.minecraft.util.FormattedCharSequence>> blocks = new java.util.ArrayList<>();
    for (int i = 0; i < p.textKeys().size(); i++) {
      Component line =
          Component.literal(box(p.statuses().get(i)))
              .append(Component.translatable(p.textKeys().get(i)));
      if (i == p.active()) {
        line = line.copy().withStyle(net.minecraft.ChatFormatting.BOLD);
        if (p.chipBlocks() >= 0) {
          line =
              line.copy()
                  .append(
                      Component.literal(" ")
                          .append(
                              Component.translatable(
                                  "landing.chip",
                                  p.chipBlocks(),
                                  Component.translatable("landing.dir." + p.chipDir()))));
        }
      }
      var split = font.split(FormattedText.of(line.getString(), line.getStyle()), PANEL_W - 8);
      if (i == p.active() && !p.hintKey().isEmpty()) {
        split = new java.util.ArrayList<>(split);
        split.addAll(font.split(Component.translatable(p.hintKey()), PANEL_W - 8));
      }
      blocks.add(split);
      h += split.size() * 9 + 2;
    }
    h += 12;
    g.fill(x - 3, y - 3, x + PANEL_W, y + h, BACKING);
    g.fill(x - 3, y - 3, x - 2, y + h, EDGE);
    g.drawString(font, Component.translatable("landing.panel.title"), x, y, 0xFFE6D7A3, false);
    int yy = y + 12;
    for (var lines : blocks) {
      for (var l : lines) {
        g.drawString(font, l, x, yy, 0xFFFFFFFF, false);
        yy += 9;
      }
      yy += 2;
    }
    g.drawString(font, Component.translatable("landing.panel.footer"), x, yy, 0xFF9AA5B1, false);
  }
}
