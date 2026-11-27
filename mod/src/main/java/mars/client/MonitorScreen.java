package mars.client;

import static mars.client.MonitorStyle.*;

import mars.monitor.view.MonitorView;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Straw-man Planetary Monitor screen (Stage 2): a compact panel anchored top left so the centre of
 * the view and the hotbar stay clear. One row per line: small label, value, trend; only the goal
 * row and a problem row are large. Default font; the pixel fonts and final look are Stage 3
 * (ui-specs.md section 1). Draws only what the server sent and keeps no planet state. Colours and
 * sizes live in {@link MonitorStyle}.
 */
public final class MonitorScreen extends Screen {
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

  /** Total height in GUI px; also used by the layout so the two never disagree. */
  static int panelHeight(MonitorView view) {
    int h = HEADER_H;
    for (MonitorView.Row row : view.rows()) {
      h += rowHeight(row) + ROW_GAP;
    }
    return h;
  }

  private static int rowHeight(MonitorView.Row row) {
    int h = row.emphasis() ? ROW_H_EMPHASIS : ROW_H;
    if (row.detailKey() != null || row.detailRight() != null) {
      h += LINE_H - 1;
    }
    return h + row.causes().size() * LINE_H;
  }

  private static int toneColor(MonitorView.Tone tone) {
    return switch (tone) {
      case GOOD -> STATE_GOOD;
      case SHORT -> STATE_SHORT;
      case NONE -> STATE_NONE;
      case NORMAL -> VALUE;
    };
  }

  @Override
  public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
    super.render(g, mouseX, mouseY, partialTick);
    if (width < 40 || height < 48) {
      return; // too small to lay out (kept from the earlier viewport fix, dc5e5bc)
    }
    int panelH = panelHeight(view);
    int x0 = MARGIN;
    int y0 = MARGIN;
    int right = x0 + PANEL_W - PAD;
    g.fill(x0 - 1, y0 - 1, x0 + PANEL_W + 1, y0 + panelH + 1, EDGE);
    g.fill(x0, y0, x0 + PANEL_W, y0 + panelH, BACKING);
    g.drawString(font, title, x0 + PAD, y0 + 5, VALUE, false);
    String sol =
        Component.translatable(
                "monitor.sol", String.format(java.util.Locale.ROOT, "%.1f", view.sols()))
            .getString();
    g.drawString(font, sol, right - font.width(sol), y0 + 5, LABEL, false);
    Component hint = Component.translatable("monitor.close_hint");
    g.drawString(font, hint, right - font.width(sol) - 12 - font.width(hint), y0 + 5, LABEL, false);

    int y = y0 + HEADER_H;
    for (MonitorView.Row row : view.rows()) {
      y = drawRow(g, row, x0, right, y);
      y += ROW_GAP;
    }
  }

  private int drawRow(GuiGraphics g, MonitorView.Row row, int x0, int right, int y) {
    int mainH = row.emphasis() ? ROW_H_EMPHASIS : ROW_H;
    float scale = row.emphasis() ? VALUE_SCALE_EMPHASIS : VALUE_SCALE;
    g.drawString(
        font, Component.translatable(row.labelKey()), x0 + PAD, y + (mainH - 8) / 2, LABEL, false);

    // Right side of the main line: trend (shape + rate) or the goal's progress.
    int rightEdge = right;
    if (row.trend() != null) {
      int color =
          row.trend() == MonitorView.Trend.RISING
              ? UP
              : row.trend() == MonitorView.Trend.FALLING ? DOWN : STEADY;
      Component full =
          Component.translatable(row.trend().key())
              .append(Component.literal("  " + row.trendRate()));
      // The word goes when the value needs the room; the shape and the signed rate stay.
      Component shown = row.valueKey() == null ? Component.literal(row.value()) : Component.empty();
      int valueEnd = x0 + PAD + LABEL_W + Math.round(font.width(shown) * scale);
      Component t =
          valueEnd + 8 + font.width(full) + 12 <= right ? full : Component.literal(row.trendRate());
      int tw = font.width(t);
      g.drawString(font, t, right - tw, y + (mainH - 8) / 2, color, false);
      drawTrendShape(g, row.trend(), right - tw - 10, y + mainH / 2, color);
      rightEdge = right - tw - 12;
    } else if (row.side() != null) {
      int sw = font.width(row.side());
      g.drawString(font, row.side(), right - sw, y + (mainH - 8) / 2, LABEL, false);
      rightEdge = right - sw - 6;
    } else if (row.bar() >= 0) {
      // Battery: a bar on the main line; the numbers are on the detail line.
      int bx = right - BAR_W;
      int by = y + (mainH - BAR_H) / 2;
      g.fill(bx - 1, by - 1, bx + BAR_W + 1, by + BAR_H + 1, EDGE);
      g.fill(bx, by, bx + BAR_W, by + BAR_H, BAR_EMPTY);
      g.fill(bx, by, bx + Math.round((float) (BAR_W * row.bar())), by + BAR_H, STATE_GOOD);
      rightEdge = bx - 6;
    }

    Component value =
        row.valueKey() != null
            ? Component.translatable(row.valueKey())
            : Component.literal(row.value());
    int valueX = x0 + PAD + LABEL_W;
    // A long phrase shrinks to fit the space left instead of running into the trend.
    float fit = Math.min(scale, (rightEdge - valueX) / (float) Math.max(1, font.width(value)));
    g.pose().pushPose();
    g.pose().translate(valueX, y + (mainH - 8 * fit) / 2f, 0);
    g.pose().scale(fit, fit, 1f);
    g.drawString(font, value, 0, 0, toneColor(row.tone()), false);
    g.pose().popPose();
    y += mainH;

    if (row.detailKey() != null || row.detailRight() != null) {
      y += 1;
      if (row.detailKey() != null) {
        Component d = Component.translatable(row.detailKey(), row.detailArgs().toArray());
        g.drawString(font, d, x0 + PAD, y, LABEL, false);
      }
      if (row.detailRight() != null) {
        Component r =
            row.detailRightKey() != null
                ? Component.translatable(row.detailRightKey(), row.detailRight())
                : Component.literal(row.detailRight());
        g.drawString(font, r, right - font.width(r), y, LABEL, false);
      }
      y += LINE_H - 1;
    }
    for (MonitorView.CauseLine c : row.causes()) {
      Component left =
          Component.translatable(c.sourceKey())
              .append(Component.literal(c.count() > 0 ? " ×" + c.count() : ""));
      g.drawString(font, left, x0 + CAUSE_INDENT, y, CAUSE, false);
      // The reason (or rate) is right-aligned so a long name can never push it off the panel.
      if (c.reasonKey() != null) {
        Component why = Component.translatable(c.reasonKey());
        int color = "monitor.cause.needs_power".equals(c.reasonKey()) ? STATE_NONE : STATE_SHORT;
        g.drawString(font, why, right - font.width(why), y, color, false);
      } else if (!c.rate().isEmpty()) {
        g.drawString(font, c.rate(), right - font.width(c.rate()), y, CAUSE, false);
      }
      y += LINE_H;
    }
    return y;
  }

  /** Up triangle, flat bar or down triangle, so the trend is never colour alone. */
  private static void drawTrendShape(
      GuiGraphics g, MonitorView.Trend trend, int x, int cy, int color) {
    switch (trend) {
      case RISING -> {
        for (int i = 0; i < 4; i++) {
          g.fill(x + 3 - i, cy - 2 + i, x + 4 + i, cy - 1 + i, color);
        }
      }
      case FALLING -> {
        for (int i = 0; i < 4; i++) {
          g.fill(x + i, cy - 2 + i, x + 7 - i, cy - 1 + i, color);
        }
      }
      case STEADY -> g.fill(x, cy - 1, x + 7, cy + 1, color);
    }
  }
}
