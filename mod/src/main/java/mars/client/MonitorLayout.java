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

  /**
   * Cause lines shown on the panel: reasons (a stalled machine) and planet-side lines (the polar
   * frost); a machine's rate lives in the trend.
   */
  static List<MonitorView.CauseLine> shownCauses(MonitorView.Row row) {
    return row.causes().stream().filter(c -> c.reasonKey() != null || c.isPlanetSide()).toList();
  }

  /**
   * The view as drawn. When the rows alone would leave the plot less than {@link
   * MonitorStyle#PLOT_MIN_H} inside {@link MonitorStyle#TARGET_H}, the lowest-priority line goes
   * first: the goal's detail line (what it needs, the percent, the time estimate and the goals
   * count; the bar stays). Problem lines (a stalled machine, a dusty panel) never drop.
   */
  static MonitorView fit(MonitorView view) {
    if (fixedHeight(view) + PLOT_MIN_H + PLOT_EXTRA_H <= TARGET_H) {
      return view;
    }
    List<MonitorView.Row> rows = new java.util.ArrayList<>();
    for (MonitorView.Row r : view.rows()) {
      boolean goal = r.labelKey().equals("monitor.row.next_goal");
      rows.add(
          !goal
              ? r
              : new MonitorView.Row(
                  r.labelKey(),
                  r.value(),
                  r.valueKey(),
                  r.trend(),
                  r.trendRate(),
                  r.causes(),
                  r.tone(),
                  r.emphasis(),
                  r.side(),
                  null,
                  List.of(),
                  null,
                  null,
                  r.bar(),
                  r.noteKey(),
                  r.goal()));
    }
    return new MonitorView(view.sols(), List.copyOf(rows), view.history());
  }

  /** Gap between the left detail text and the percent (and the estimate), GUI px. */
  static final int DETAIL_GAP = 8;

  /**
   * The left text of a goal row's detail line. It is "detail · count" when that fits left of the
   * percent; otherwise the goals count goes first (the lowest priority), and if the detail alone is
   * still too wide it is cut with an ellipsis. So the text can never run under the percent.
   *
   * @param width text width in GUI px (the font in the game, an estimate in tests)
   * @param available px between the left edge of the text and the right edge of the panel content
   * @param percentWidth px of the percent text on the right
   */
  static String fitDetail(
      String detail,
      String count,
      int available,
      int percentWidth,
      java.util.function.ToIntFunction<String> width) {
    int budget = available - percentWidth - DETAIL_GAP;
    String both = count == null || count.isEmpty() ? detail : detail + " \u00b7 " + count;
    if (width.applyAsInt(both) <= budget) {
      return both;
    }
    if (width.applyAsInt(detail) <= budget) {
      return detail;
    }
    String cut = detail;
    while (cut.length() > 1 && width.applyAsInt(cut + "\u2026") > budget) {
      cut = cut.substring(0, cut.length() - 1).stripTrailing();
    }
    return cut + "\u2026";
  }

  /** Height of everything except the plot block, in GUI px. */
  static int fixedHeight(MonitorView view) {
    int h = HEADER_H + 1;
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
    if (fixed + PLOT_MIN_H + PLOT_EXTRA_H > TARGET_H + PLOT_OVERFLOW) {
      return 0; // many problem lines: rows only, no history
    }
    int wanted = Math.max(PLOT_MIN_H, Math.min(PLOT_MAX_H, TARGET_H - fixed - PLOT_EXTRA_H));
    int room = screenHeight - 2 * MARGIN - fixed - PLOT_EXTRA_H;
    return room < PLOT_MIN_H ? 0 : Math.min(wanted, room);
  }

  /** Total height in GUI px; also used by the layout so the two never disagree. */
  static int panelHeight(MonitorView view, int screenHeight) {
    int ph = plotHeight(view, screenHeight);
    return fixedHeight(view) + (ph > 0 ? ph + PLOT_EXTRA_H : 0);
  }

  /**
   * A goal row's detail line carries the percent, the estimate and the goals count; the other rows
   * have one when they have a detail text or a right-hand text.
   */
  static boolean hasDetailLine(MonitorView.Row row) {
    return row.detailKey() != null || row.detailRight() != null;
  }

  static int rowHeight(MonitorView.Row row) {
    int h = row.emphasis() ? ROW_H_EMPHASIS : ROW_H;
    if (row.goal() != null) {
      h += GOAL_BAR_LINE_H;
    }
    if (hasDetailLine(row)) {
      h += LINE_H - 1;
    }
    return h + shownCauses(row).size() * LINE_H + (row.noteKey() != null ? NOTE_LINES * LINE_H : 0);
  }
}
