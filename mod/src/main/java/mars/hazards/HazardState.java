package mars.hazards;

import java.util.Map;
import mars.hazards.dust.PanelDustModel;

/**
 * The saved hazard state of one planet (PlanetState schema 8): the planet's random seed, the dust
 * on every panel, and the storm scheduler. {@code seed} 0 means "not set yet" (a world saved before
 * schema 8).
 */
public record HazardState(
    long seed, Map<Long, PanelDustModel.Saved> dust, mars.hazards.storm.StormScheduler.State storm) {
  public static final HazardState EMPTY = new HazardState(0, Map.of(), null);
}
