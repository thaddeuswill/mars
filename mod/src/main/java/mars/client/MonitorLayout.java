package mars.client;

import static mars.client.MonitorStyle.*;

import java.util.List;
import mars.monitor.view.MonitorView;

/**
 * Heights of the Planetary Monitor panel in GUI px. Pure (no Minecraft classes) so the layout is
 * unit-tested; {@link MonitorScreen} draws with these numbers, so the two never disagree.
 */
final class MonitorLayout {
  private MonitorLayout() {}

  /** Cause lines shown on the panel: only reasons (a stalled machine); rates live in the trend. */
  static List<MonitorView.CauseLine> shownCauses(MonitorView.Row row) {
    return row.causes().stream().filter(c -> c.reasonKey() != null).toList();
  }

  /** Height of everything except the plot block, in GUI px. */
  static int fixedHeight(MonitorView view) {
    int h = HEADER_H + 2;
    for (MonitorView.Row row : view.rows()) {
      h += rowHeight(row) + ROW_GAP;
    }
    return h;
  }

  /**
   * Plot height (the drawn area, not the axis labels) so the whole panel is about {@link
   * MonitorStyle#TARGET_H} tall: problem lines make the plot shorter, never the panel taller, down
   * to {@link MonitorStyle#PLOT_MIN_H}. Returns 0 when the screen is too short for any plot
   * (compact mode: the rows alone).
   */
  static int plotHeight(MonitorView view, int screenHeight) {
    int fixed = fixedHeight(view);
    int wanted = Math.max(PLOT_MIN_H, Math.min(PLOT_MAX_H, TARGET_H - fixed - PLOT_LABEL_H - 2));
    int room = screenHeight - 2 * MARGIN - fixed - PLOT_LABEL_H - 2;
    return room < PLOT_MIN_H ? 0 : Math.min(wanted, room);
  }

  /** Total height in GUI px; also used by the layout so the two never disagree. */
  static int panelHeight(MonitorView view, int screenHeight) {
    int ph = plotHeight(view, screenHeight);
    return fixedHeight(view) + (ph > 0 ? ph + PLOT_LABEL_H + 2 : 0);
  }

  static int rowHeight(MonitorView.Row row) {
    int h = row.emphasis() ? ROW_H_EMPHASIS : ROW_H;
    if (row.detailKey() != null || row.detailRight() != null) {
      h += LINE_H - 1;
    }
    return h + shownCauses(row).size() * LINE_H;
  }
}
