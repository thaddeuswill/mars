package mars.index.sim;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import mars.TestSims;
import mars.dome.DomeSeals;
import mars.dome.Pos;
import org.junit.jupiter.api.Test;

/** Schema 6: the registered domes are saved with the planet; older saves load without any. */
class DomeStateTest {
  @Test
  void aRegisteredDomeSurvivesToStateAndFromState() throws Exception {
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    long seed = Pos.pack(10, 64, -3);
    s.domes().register(seed);
    PlanetState st = s.toState();
    assertEquals(7, st.schema());
    assertEquals(1, st.domes().domes().size());
    PlanetSim back =
        PlanetSim.fromState(
                st,
                TestSims.index(),
                TestSims.defs(),
                TestSims.gates(),
                TestSims.frost(),
                Pace.STANDARD)
            .sim();
    assertEquals(1, back.domes().count());
    assertEquals(DomeSeals.Status.UNKNOWN, back.domes().status(seed));
    assertEquals(st.domes(), back.toState().domes());
  }

  @Test
  void aSchema5SaveLoadsWithNoDomes() throws Exception {
    // Built with the schema 5 constructor: no dome part at all.
    PlanetState old =
        new PlanetState(
            5,
            Map.of("mars:pressure", 2.0),
            24_000,
            0,
            Map.of(),
            0,
            Map.of(),
            Map.of(),
            PressureHistory.State.EMPTY,
            FrostSink.State.EMPTY);
    PlanetSim s =
        PlanetSim.fromState(
                old,
                TestSims.index(),
                TestSims.defs(),
                TestSims.gates(),
                TestSims.frost(),
                Pace.STANDARD)
            .sim();
    assertEquals(0, s.domes().count());
    assertTrue(s.toState().domes().domes().isEmpty());
    assertEquals(7, s.toState().schema(), "a re-save writes the current schema");
  }

  @Test
  void aRollbackKeepsTheDomesThatStandInTheWorld() throws Exception {
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    PlanetState snapshot = s.toState(); // no domes then
    s.domes().register(Pos.pack(1, 2, 3));
    PlanetState restored = PlanetCheckpoint.restoreOver(snapshot, s.toState());
    assertEquals(1, restored.domes().domes().size());
  }
}
