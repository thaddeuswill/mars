package mars.client;

import static mars.client.MonitorStyle.*;

import mars.monitor.view.Fmt;
import mars.monitor.view.MonitorView;
import mars.monitor.view.PlotScale;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Straw-man Planetary Monitor screen (Stage 2, option A of BACKLOG #38): a dark console panel
 * centred on the screen (it covers the crosshair; the player opened it on purpose and closes it
 * with Esc). Pressure row, a pressure history plot (Sol on X, the unit on Y, dashed event lines),
 * then one row per line: small label, value, trend; only the goal row and a problem row are large.
 * Default font; the pixel fonts and final look are Stage 3 (ui-specs.md section 1). Draws only what
 * the server sent and keeps no planet state. Colours and sizes live in {@link MonitorStyle}.
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
    int panelW = Math.min(PANEL_W, width - 2 * MARGIN);
    int plotH = MonitorLayout.plotHeight(view, height);
    int panelH = MonitorLayout.panelHeight(view, height);
    int x0 = (width - panelW) / 2;
    int y0 = Math.max(2, (height - panelH) / 2);
    int right = x0 + panelW - PAD;
    // Nothing may be drawn outside the screen, even when a translation or a tiny window overflows.
    g.enableScissor(0, 0, width, height);
    g.fill(x0 - 1, y0 - 1, x0 + panelW + 1, y0 + panelH + 1, EDGE);
    g.fill(x0, y0, x0 + panelW, y0 + panelH, BACKING);
    g.drawString(font, title, x0 + PAD, y0 + 5, VALUE, false);
    String sol = Component.translatable("monitor.sol", Fmt.number(view.sols(), 1)).getString();
    g.drawString(font, sol, right - font.width(sol), y0 + 5, LABEL, false);
    Component hint = Component.translatable("monitor.close_hint");
    int hintX = right - font.width(sol) - 12 - font.width(hint);
    if (hintX > x0 + PAD + font.width(title) + 6) {
      g.drawString(font, hint, hintX, y0 + 5, LABEL, false);
    }

    int y = y0 + HEADER_H;
    boolean first = true;
    for (MonitorView.Row row : view.rows()) {
      y = drawRow(g, row, x0, right, y);
      y += ROW_GAP;
      if (first && plotH > 0) {
        y = drawPlot(g, x0 + PAD, right, y, plotH) + 2;
      }
      first = false;
    }
    g.disableScissor();
  }

  /** The pressure plot. Returns the y below it (after the Sol labels). */
  private int drawPlot(GuiGraphics g, int left, int right, int top, int h) {
    MonitorView.History hist = view.history();
    String topLabel = "";
    double topValue = 1;
    if (!hist.points().isEmpty()) {
      double max = 0;
      for (MonitorView.Point p : hist.points()) {
        max = Math.max(max, p.value());
      }
      topValue = PlotScale.niceMax(max);
      topLabel = Fmt.number(topValue, 0) + " " + hist.unit();
    }
    int gutter = Math.max(font.width("0"), font.width(topLabel)) + 4;
    int px = left + gutter;
    int pw = right - px;
    if (pw < 20) {
      return top + h + PLOT_LABEL_H;
    }
    // Axes: left and bottom.
    g.fill(px - 1, top, px, top + h + 1, PLOT_AXIS);
    g.fill(px - 1, top + h, px + pw, top + h + 1, PLOT_AXIS);
    g.drawString(font, "0", px - 3 - font.width("0"), top + h - 8, LABEL, false);
    if (!hist.drawable()) {
      Component msg = Component.translatable("monitor.plot.empty");
      int mw = font.width(msg);
      g.drawString(font, msg, px + Math.max(2, (pw - mw) / 2), top + (h - 8) / 2, LABEL, false);
      return top + h + PLOT_LABEL_H;
    }
    g.drawString(font, topLabel, left, top, LABEL, false);
    java.util.List<MonitorView.Point> pts = hist.points();
    double first = pts.get(0).sol();
    double last = pts.get(pts.size() - 1).sol();
    // Event lines first so the series draws over them.
    int labelEnd = Integer.MIN_VALUE;
    for (MonitorView.Marker m : hist.markers()) {
      if (m.sol() < first - 1e-6 || m.sol() > last + 1e-6) {
        continue;
      }
      int mx = px + PlotScale.x(m.sol(), first, last, pw);
      for (int yy = top; yy < top + h; yy += 3) {
        g.fill(mx, yy, mx + 1, Math.min(yy + 2, top + h), CAUSE);
      }
      Component label =
          m.argKey() == null
              ? Component.translatable(m.labelKey())
              : Component.translatable(m.labelKey(), Component.translatable(m.argKey()));
      int lw = font.width(label);
      int lx = mx + 2;
      if (lx + lw > px + pw) {
        lx = mx - 2 - lw; // no room on the right: put the label on the left of the line
      }
      if (lx >= px + 1 && lx > labelEnd && h >= 14) {
        g.fill(lx - 1, top, lx + lw + 1, top + 9, BACKING);
        g.drawString(font, label, lx, top + 1, CAUSE, false);
        labelEnd = lx + lw + 2;
      }
    }
    int prevX = 0;
    int prevY = 0;
    boolean dots = pts.size() <= 24;
    for (int i = 0; i < pts.size(); i++) {
      MonitorView.Point p = pts.get(i);
      int sx = px + PlotScale.x(p.sol(), first, last, pw);
      int sy = top + PlotScale.y(p.value(), topValue, h);
      if (i > 0) {
        drawLine(g, prevX, prevY, sx, sy, PLOT_LINE);
      }
      if (dots) {
        g.fill(sx - 1, sy - 1, sx + 2, sy + 2, PLOT_LINE);
      }
      prevX = sx;
      prevY = sy;
    }
    // Sol labels under the X axis: first, middle (when there is room), last.
    int ly = top + h + 2;
    drawSolLabel(g, first, px, ly, 0);
    if (last - first >= 4) {
      drawSolLabel(g, (first + last) / 2, px + pw / 2, ly, 1);
    }
    drawSolLabel(g, last, px + pw, ly, 2);
    return top + h + PLOT_LABEL_H;
  }

  /** Align 0 = text starts at x, 1 = centred on x, 2 = text ends at x. */
  private void drawSolLabel(GuiGraphics g, double sol, int x, int y, int align) {
    Component t = Component.translatable("monitor.sol", Fmt.number(Math.round(sol), 0));
    int w = font.width(t);
    int tx = align == 0 ? x : align == 1 ? x - w / 2 : x - w;
    g.drawString(font, t, tx, y, LABEL, false);
  }

  private static void drawLine(GuiGraphics g, int x1, int y1, int x2, int y2, int color) {
    int steps = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
    if (steps == 0) {
      g.fill(x1, y1, x1 + 1, y1 + 1, color);
      return;
    }
    for (int i = 0; i <= steps; i++) {
      int x = x1 + Math.round((x2 - x1) * i / (float) steps);
      int y = y1 + Math.round((y2 - y1) * i / (float) steps);
      g.fill(x, y, x + 1, y + 1, color);
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
      // Battery state of charge: ten segments plus a percent; the numbers are on the detail line.
      String pct = Fmt.number(Math.round(row.bar() * 100), 0) + "%";
      int pw = font.width(pct);
      int bx = right - pw - 4 - BAR_W;
      int by = y + (mainH - BAR_H) / 2;
      int filled =
          row.bar() >= 0.995
              ? BAR_SEGMENTS
              : row.bar() <= 0.005
                  ? 0
                  : Math.max(
                      1, Math.min(BAR_SEGMENTS - 1, (int) Math.round(row.bar() * BAR_SEGMENTS)));
      for (int i = 0; i < BAR_SEGMENTS; i++) {
        int sx = bx + i * (BAR_SEG_W + BAR_SEG_GAP);
        g.fill(sx, by, sx + BAR_SEG_W, by + BAR_H, i < filled ? STATE_GOOD : BAR_EMPTY);
      }
      g.drawString(font, pct, right - pw, y + (mainH - 8) / 2, LABEL, false);
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
    for (MonitorView.CauseLine c : MonitorLayout.shownCauses(row)) {
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
