package mars.client;

import mars.monitor.view.MonitorView;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Straw-man Planetary Monitor screen (Stage 2): a dark panel, large values, small labels, and the
 * cause lines under each row. Default font; the pixel fonts and final layout are Stage 3
 * (ui-specs.md §1). Draws only what the server sent and keeps no planet state.
 */
public final class MonitorScreen extends Screen {
  private static final int PANEL_W = 232;
  private static final int BACKING = 0xF0181210;
  private static final int EDGE = 0xFF6B4A36;
  private static final int LABEL = 0xFF9AA5B1;
  private static final int VALUE = 0xFFFFFFFF;
  private static final int UP = 0xFF7CD37C;
  private static final int DOWN = 0xFFFF8A80;
  private static final int STEADY = 0xFFC8D0D8;
  private static final int CAUSE = 0xFFE6D7A3;

  private final MonitorView view;

  public MonitorScreen(MonitorView view) {
    super(Component.translatable("monitor.title"));
    this.view = view;
  }

  @Override
  public boolean isPauseScreen() {
    return false;
  }

  @Override
  public void renderBackground(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
    // No dimming: the Monitor is a small readout, the world stays visible behind it.
  }

  @Override
  public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
    super.render(g, mouseX, mouseY, partialTick);
    int causeLines = view.rows().stream().mapToInt(r -> r.causes().size()).sum();
    int panelH = 22 + view.rows().size() * 26 + causeLines * 10 + 6;
    int x0 = (width - PANEL_W) / 2;
    int y0 = Math.max(4, (height - panelH) / 2);
    g.fill(x0 - 1, y0 - 1, x0 + PANEL_W + 1, y0 + panelH + 1, EDGE);
    g.fill(x0, y0, x0 + PANEL_W, y0 + panelH, BACKING);
    g.drawString(font, title, x0 + 6, y0 + 6, VALUE, false);
    String sol =
        Component.translatable(
                "monitor.sol", String.format(java.util.Locale.ROOT, "%.1f", view.sols()))
            .getString();
    g.drawString(font, sol, x0 + PANEL_W - 6 - font.width(sol), y0 + 6, LABEL, false);

    int y = y0 + 20;
    for (MonitorView.Row row : view.rows()) {
      g.drawString(font, Component.translatable(row.labelKey()), x0 + 6, y, LABEL, false);
      Component value =
          row.valueKey() != null
              ? Component.translatable(row.valueKey())
              : Component.literal(row.value());
      // Big values, but a long phrase shrinks to fit the panel instead of running off it.
      float scale = Math.min(1.5f, (PANEL_W - 12f) / Math.max(1, font.width(value)));
      g.pose().pushPose();
      g.pose().translate(x0 + 6, y + 9, 0);
      g.pose().scale(scale, scale, 1f);
      g.drawString(font, value, 0, 0, VALUE, false);
      g.pose().popPose();
      if (row.trend() != null) {
        int color =
            row.trend() == MonitorView.Trend.RISING
                ? UP
                : row.trend() == MonitorView.Trend.FALLING ? DOWN : STEADY;
        Component t =
            Component.translatable(row.trend().key())
                .append(Component.literal("  " + row.trendRate()));
        g.drawString(font, t, x0 + PANEL_W - 6 - font.width(t), y + 13, color, false);
      }
      y += 24;
      for (MonitorView.CauseLine c : row.causes()) {
        Component left =
            Component.translatable(c.sourceKey())
                .append(Component.literal(c.count() > 0 ? " ×" + c.count() : ""));
        g.drawString(font, left, x0 + 12, y, CAUSE, false);
        // The rate is right-aligned so a long machine name can never push it off the panel.
        g.drawString(font, c.rate(), x0 + PANEL_W - 6 - font.width(c.rate()), y, CAUSE, false);
        y += 10;
      }
      y += 2;
    }
  }
}
