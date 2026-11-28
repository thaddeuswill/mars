package mars.core;

/**
 * The planet's attributed power sources: how much supply one named cause adds or removes, kept
 * apart from the net total so a forecast can resolve on its own cause and not on a number the
 * player can also change (content rule CR-F10; docs/design/f1-storm-spec.md 1.4). Values are signed
 * power units per sol: a cause that removes supply is negative. The planet's simulation implements
 * this; the learning code reads it, so the two modules share only this interface.
 */
public interface AttributedSources {
  /** Supply the storm removes from the panels' clean output: sunlight cut, 0 or negative. */
  String STORM_SOLAR_CUT = "mars:storm_solar_cut";

  /** Supply the dust on the panels removes from the clean output: 0 or negative. */
  String PANEL_DUST_LOSS = "mars:panel_dust_loss";

  /** The source's value now. 0 for an unknown id. */
  double current(String sourceId);

  /**
   * The largest effect (the most negative value for a loss) the source has had since {@code tick}
   * (simulation ticks), at most the last two sols; the current value when nothing older is kept.
   */
  double peakSince(String sourceId, long tick);
}
