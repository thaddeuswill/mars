package mars.monitor.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import mars.TestSims;
import mars.gates.Condition;
import mars.gates.GateDef;
import mars.index.sim.Pace;
import mars.index.sim.PlanetSim;
import org.junit.jupiter.api.Test;

/** The plot's goal line comes from the gate data and never scales with pace (I-1). */
class GoalLineTest {
  private static double gateValue(String id) throws Exception {
    for (GateDef d : TestSims.gates()) {
      if (d.id().equals(id)) {
        return ((Condition.Compare) d.condition()).value();
      }
    }
    throw new AssertionError(id);
  }

  @Test
  void goalLineEqualsTheGateValueInBothPaces() throws Exception {
    for (Pace pace : new Pace[] {Pace.STANDARD, Pace.classroom(4)}) {
      PlanetSim sim = TestSims.sim(pace);
      GoalLine g = MonitorView.historyOf(sim).goalLine();
      assertEquals(gateValue("mars:phase_2_begins"), g.value(), 0, "pace " + pace);
      assertEquals(5.0, g.value(), 0);
      assertFalse(g.partial(), "pressure is the only condition of Phase 2 begins");
    }
  }

  @Test
  void afterTheLastPressureGateTheLineStaysOnIt() throws Exception {
    GoalLine g = GoalLine.next(TestSims.gates(), id -> true);
    assertEquals(75.0, g.value(), 0, "Equilibrium is the last gate with a pressure condition");
  }

  @Test
  void aGateWithAnotherConditionIsPartial() throws Exception {
    GateDef p2 =
        TestSims.gates().stream()
            .filter(d -> d.id().equals("mars:phase_2_begins"))
            .findFirst()
            .get();
    GateDef multi =
        new GateDef(
            "mars:x",
            9,
            new Condition.All(
                List.of(
                    p2.condition(),
                    new Condition.Compare(mars.core.Ids.TEMPERATURE, Condition.Op.GE, 0))),
            null,
            List.of(),
            "gate.mars.x");
    GoalLine g = GoalLine.next(List.of(multi), id -> false);
    assertTrue(g.partial());
    assertEquals(5.0, g.value(), 0);
  }
}
