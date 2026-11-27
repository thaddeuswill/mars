package mars.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import mars.TestSims;
import mars.core.Ids;
import mars.index.sim.Pace;
import mars.index.sim.PlanetSim;
import mars.index.sim.PlanetState;
import org.junit.jupiter.api.Test;

/** Invariant I-3 (qa-plan.md section 4): no simulation time passes while paused or stopped. */
class SessionClockTest {
  private static double pressure(PlanetSim s) {
    return s.model().snapshot().get(Ids.PRESSURE);
  }

  private static PlanetSim running() throws Exception {
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    s.addMachine(TestSims.OUTGASSER, 1L);
    return s;
  }

  @Test
  void i3_pausedSessionAdvancesNothing() throws Exception {
    PlanetSim s = running();
    SessionClock clock = new SessionClock(false);
    for (int i = 0; i < 250; i++) {
      s.onServerTick(clock);
    }
    PlanetState before = s.toState();
    double p0 = pressure(s);
    clock.pause();
    int steps = 0;
    for (int i = 0; i < 100_000; i++) { // about 83 minutes of paused play
      if (s.onServerTick(clock)) {
        steps++;
      }
    }
    PlanetState after = s.toState();
    System.out.println(
        "I-3 paused: simTicks "
            + before.simTicks()
            + " -> "
            + after.simTicks()
            + ", pending "
            + before.pendingTicks()
            + " -> "
            + after.pendingTicks()
            + ", pressure "
            + p0
            + " -> "
            + pressure(s)
            + ", steps while paused "
            + steps);
    assertEquals(0, steps);
    assertEquals(before, after);
    assertEquals(p0, pressure(s), 0.0);
    assertEquals(0.0, s.model().sols() - before.simTicks() / 24000.0, 1e-12);
  }

  @Test
  void i3_resumeContinuesFromWhereItStopped() throws Exception {
    PlanetSim paused = running();
    PlanetSim control = running();
    SessionClock clock = new SessionClock(false);
    SessionClock always = new SessionClock(false);
    for (int i = 0; i < 130; i++) {
      paused.onServerTick(clock);
      control.onServerTick(always);
    }
    clock.pause();
    for (int i = 0; i < 5000; i++) {
      paused.onServerTick(clock);
    }
    clock.resume();
    for (int i = 0; i < 270; i++) {
      paused.onServerTick(clock);
      control.onServerTick(always);
    }
    System.out.println(
        "I-3 resume: paused-run simTicks "
            + paused.toState().simTicks()
            + ", control simTicks "
            + control.toState().simTicks());
    assertEquals(control.toState(), paused.toState());
    assertEquals(2, clock.sessionNumber());
  }

  @Test
  void i3_stoppedServerAddsNothingOnRestore() throws Exception {
    PlanetSim s = running();
    SessionClock clock = new SessionClock(false);
    for (int i = 0; i < 1234; i++) {
      s.onServerTick(clock);
    }
    PlanetState saved = s.toState();
    // The restore reads no clock of any kind: a restored planet equals the saved one, however long
    // the server stayed off.
    PlanetSim back =
        PlanetSim.fromState(saved, TestSims.index(), TestSims.defs(), Pace.STANDARD).sim();
    assertEquals(saved, back.toState());
  }

  @Test
  void i3_nothingInTheTickApiCanReadSleepOrDaylight() throws Exception {
    // The only input to a step is the tick call itself. A night skipped by sleeping changes the
    // daylight time, which the sim never sees, so it can't change sols. Checked by shape: the sim
    // tick method takes no time argument.
    var methods =
        java.util.Arrays.stream(PlanetSim.class.getMethods())
            .filter(m -> m.getName().equals("onServerTick"))
            .toList();
    assertFalse(methods.isEmpty());
    for (var m : methods) {
      for (Class<?> p : m.getParameterTypes()) {
        assertTrue(p == SessionClock.class, "unexpected input to the tick: " + p);
      }
    }
  }

  @Test
  void pauseAndResumeReportChange() {
    SessionClock c = new SessionClock(true);
    assertTrue(c.paused());
    assertFalse(c.pause());
    assertTrue(c.resume());
    assertFalse(c.resume());
    assertEquals(2, c.sessionNumber());
  }

  @Test
  void restoreRefusesNewerSchemaAndKeepsFlag() {
    assertThrows(
        IllegalStateException.class,
        () -> SessionClock.restore(SessionClock.CURRENT_SCHEMA + 1, false, 1));
    SessionClock c = SessionClock.restore(SessionClock.CURRENT_SCHEMA, true, 7);
    assertTrue(c.paused());
    assertEquals(7, c.sessionNumber());
  }
}
