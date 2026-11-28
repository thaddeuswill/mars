package mars.hazards;

import mars.hazards.dust.PanelDustTuning;
import mars.hazards.storm.StormTuning;

/** The data a planet's hazards run on: panel dust and the storm schedule. */
public record HazardParams(PanelDustTuning dust, StormTuning storm) {}
