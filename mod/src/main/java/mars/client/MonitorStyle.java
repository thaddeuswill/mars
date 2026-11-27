package mars.client;

/**
 * Every colour and size of the Planetary Monitor in one place, so the Stage 3 restyle (pixel fonts,
 * final palette) is a change to this file only. Sizes are GUI pixels. Straw-man values (ui-specs.md
 * section 9); contrast ratios of the text colours were measured in the 2026-11-27 UI review (all at
 * least 4.5 to 1 on the backing).
 */
final class MonitorStyle {
  private MonitorStyle() {}

  // Colours (ARGB)
  static final int BACKING = 0xF0181210;
  static final int EDGE = 0xFF6B4A36;
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
  static final int BAR_EMPTY = 0xFF3A302A;

  // Layout
  static final int PANEL_W = 250;
  static final int MARGIN = 6;
  static final int PAD = 6;
  static final int HEADER_H = 16;
  static final int LABEL_W = 86;
  static final int LINE_H = 10;
  static final int ROW_H = 11;
  static final int ROW_H_EMPHASIS = 13;
  static final int ROW_GAP = 3;
  static final int CAUSE_INDENT = 12;
  static final int BAR_W = 36;
  static final int BAR_H = 5;

  // Type
  static final float VALUE_SCALE_EMPHASIS = 1.25f;
  static final float VALUE_SCALE = 1.0f;
}
