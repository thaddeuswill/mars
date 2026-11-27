package mars.client;

/**
 * Every colour and size of the Planetary Monitor in one place, so the Stage 3 restyle (pixel fonts,
 * final palette) is a change to this file only. Sizes are GUI pixels. Straw-man values (ui-specs.md
 * section 9). Contrast of every colour here on the backing (over white and over black world):
 * docs/team/reports/2026-11-27-technical-lead-monitor-option-a.md; text at least 4.5 to 1, bars and
 * lines at least 3 to 1.
 */
final class MonitorStyle {
  private MonitorStyle() {}

  // Colours (ARGB)
  static final int BACKING = 0xF0181210;
  static final int EDGE = 0xFF9A6A4C;
  static final int LABEL = 0xFF9AA5B1;
  static final int VALUE = 0xFFFFFFFF;
  static final int UP = 0xFF7CD37C;
  static final int DOWN = 0xFFFF8A80;
  static final int STEADY = 0xFFC8D0D8;
  static final int CAUSE = 0xFFE6D7A3;

  /** Power states: Enough green, Short amber, None red (a word always goes with the colour). */
  static final int STATE_GOOD = UP;

  static final int STATE_SHORT = CAUSE;
  static final int STATE_NONE = DOWN;
  static final int BAR_EMPTY = 0xFF78685E;

  /** Pressure plot: the series, the axes, and the dashed event line (the event label is CAUSE). */
  static final int PLOT_LINE = 0xFF8AD4F0;

  static final int PLOT_AXIS = 0xFF9A6A4C;

  // Layout
  static final int PANEL_W = 250;

  /**
   * Panel height we aim for at GUI scale 3 (240 GUI px screen): the plot grows or shrinks to it.
   */
  static final int TARGET_H = 130;

  /**
   * The plot is dropped (rows only) when keeping its minimum would push the panel past {@link
   * #TARGET_H} plus this: a stalled, dusty, levelling-off planet has many problem lines and the
   * history is the lowest priority.
   */
  static final int PLOT_OVERFLOW = 20;

  static final int MARGIN = 6;
  static final int PAD = 6;
  static final int HEADER_H = 15;
  static final int LABEL_W = 86;
  static final int LINE_H = 10;
  static final int ROW_H = 11;
  static final int ROW_H_EMPHASIS = 12;
  static final int ROW_GAP = 1;
  static final int CAUSE_INDENT = 12;

  /** A row's wrapped note ("Levelling off ...") is always drawn in this many lines. */
  static final int NOTE_LINES = 2;

  static final int BAR_SEGMENTS = 10;
  static final int BAR_SEG_W = 3;
  static final int BAR_SEG_GAP = 1;
  static final int BAR_W = BAR_SEGMENTS * BAR_SEG_W + (BAR_SEGMENTS - 1) * BAR_SEG_GAP;
  static final int BAR_H = 5;

  /** Next-gate bar: a continuous bar GOAL_BAR_H tall on its own line of GOAL_BAR_LINE_H. */
  static final int GOAL_BAR_H = 4;

  static final int GOAL_BAR_LINE_H = 7;
  static final int GOAL_BAR_FILL = STATE_GOOD;
  static final int GOAL_BAR_EMPTY = BAR_EMPTY;
  static final int PLOT_MIN_H = 16;
  static final int PLOT_MAX_H = 36;
  static final int PLOT_LABEL_H = 9;

  // Type
  static final float VALUE_SCALE_EMPHASIS = 1.25f;
  static final float VALUE_SCALE = 1.0f;
}
