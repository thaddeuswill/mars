package mars.core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PlanetsTest {
  @Test
  void onlyMarsDimensionsArePlanetsUnlessTheTestSeamIsOn() {
    assertTrue(Planets.isPlanetNamespace("mars", false));
    assertFalse(Planets.isPlanetNamespace("minecraft", false), "the overworld is not a planet");
    assertFalse(Planets.isPlanetNamespace("mekanism", false));
    assertTrue(Planets.isPlanetNamespace("minecraft", true), "seam on (gameTestServer only)");
  }
}
