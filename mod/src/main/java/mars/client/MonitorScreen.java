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
  private int scroll;
  private int maxScroll;

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
    if (width < 40 || height < 48) {
      return;
    }
    int causeLines = view.rows().stream().mapToInt(r -> r.causes().size()).sum();
    int contentHeight = view.rows().size() * 26 + causeLines * 10;
    int panelW = Math.min(PANEL_W, width - 12);
    int panelH = Math.min(28 + contentHeight, Math.max(40, height * 55 / 100));
    int x0 = 6;
    int right = x0 + panelW - 6;
    maxScroll = Math.max(0, contentHeight - (panelH - 28));
    scroll = Math.min(scroll, maxScroll);
    int y0 = Math.max(4, (height - panelH) / 2);
    g.fill(x0 - 1, y0 - 1, x0 + panelW + 1, y0 + panelH + 1, EDGE);
    g.fill(x0, y0, x0 + panelW, y0 + panelH, BACKING);
    String sol =
        Component.translatable(
                "monitor.sol", String.format(java.util.Locale.ROOT, "%.1f", view.sols()))
            .getString();
    drawColumns(g, title.getString(), sol, x0 + 6, right, y0 + 6, VALUE, LABEL);

    g.enableScissor(x0 + 1, y0 + 20, x0 + panelW - 1, y0 + panelH - 8);
    int y = y0 + 20 - scroll;
    for (MonitorView.Row row : view.rows()) {
      String label = Component.translatable(row.labelKey()).getString();
      g.drawString(font, fit(label, right - x0 - 6), x0 + 6, y, LABEL, false);
      Component value =
          row.valueKey() != null
              ? Component.translatable(row.valueKey())
              : Component.literal(row.value());
      String trend = "";
      int trendColor = STEADY;
      if (row.trend() != null) {
        trendColor =
            row.trend() == MonitorView.Trend.RISING
                ? UP
                : row.trend() == MonitorView.Trend.FALLING ? DOWN : STEADY;
        trend = Component.translatable(row.trend().key()).getString() + "  " + row.trendRate();
      }
      int lineWidth = right - x0 - 6;
      trend = fit(trend, lineWidth / 2);
      int valueWidth = lineWidth - (trend.isEmpty() ? 0 : font.width(trend) + 6);
      String fittedValue = fit(value.getString(), (int) (valueWidth / 1.25f));
      g.pose().pushPose();
      g.pose().translate(x0 + 6, y + 9, 0);
      g.pose().scale(1.25f, 1.25f, 1f);
      g.drawString(font, fittedValue, 0, 0, VALUE, false);
      g.pose().popPose();
      if (!trend.isEmpty()) {
        g.drawString(font, trend, right - font.width(trend), y + 11, trendColor, false);
      }
      y += 24;
      for (MonitorView.CauseLine c : row.causes()) {
        Component left =
            Component.translatable(c.sourceKey())
                .append(Component.literal(c.count() > 0 ? " ×" + c.count() : ""));
        drawColumns(g, left.getString(), c.rate(), x0 + 12, right, y, CAUSE, CAUSE);
        y += 10;
      }
      y += 2;
    }
    g.disableScissor();
    if (maxScroll > 0) {
      int trackHeight = panelH - 28;
      int thumbHeight = Math.max(4, trackHeight * trackHeight / contentHeight);
      int thumbY = y0 + 20 + (trackHeight - thumbHeight) * scroll / maxScroll;
      g.fill(right + 2, thumbY, right + 4, thumbY + thumbHeight, LABEL);
    }
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double horizontal, double vertical) {
    if (maxScroll == 0 || vertical == 0) {
      return super.mouseScrolled(mouseX, mouseY, horizontal, vertical);
    }
    scroll = Math.clamp(scroll - (int) Math.round(vertical * 24), 0, maxScroll);
    return true;
  }

  private void drawColumns(
      GuiGraphics g, String left, String right, int x, int end, int y, int leftColor, int rightColor) {
    String rightText = fit(right, (end - x) / 2);
    int rightX = end - font.width(rightText);
    int leftWidth = rightX - x - (rightText.isEmpty() ? 0 : 6);
    g.drawString(font, fit(left, leftWidth), x, y, leftColor, false);
    g.drawString(font, rightText, rightX, y, rightColor, false);
  }

  private String fit(String text, int pixels) {
    if (font.width(text) <= pixels) {
      return text;
    }
    String ellipsis = "…";
    int available = pixels - font.width(ellipsis);
    return available < 0 ? "" : font.plainSubstrByWidth(text, available) + ellipsis;
  }
}
