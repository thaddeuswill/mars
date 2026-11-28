package mars.index.sim;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonParser;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import mars.TestData;
import mars.TestSims;
import mars.core.Ids;
import mars.monitor.view.MonitorView;
import mars.session.SessionClock;
import org.junit.jupiter.api.Test;

/** The temperature loss term: a restoring leak toward -60 C (SME recommendation, INVENTED k). */
class TemperatureLossTest {
  private static final String EMIT = "mars:super_greenhouse_emitter";

  private static double t(PlanetSim s) {
    return s.model().current(Ids.TEMPERATURE);
  }

  @Test
  void theLeakIsDataAndMarkedInvented() throws Exception {
    var o =
        JsonParser.parseString(
                Files.readString(
                    TestData.data("parameters/temperature.json"), StandardCharsets.UTF_8))
            .getAsJsonObject();
    var loss = o.getAsJsonObject("loss");
    assertEquals("restore", loss.get("form").getAsString());
    assertEquals(-60.0, loss.get("baseline").getAsDouble());
    assertEquals(0.004, loss.get("perSol").getAsDouble());
    assertTrue(o.get("source").getAsString().contains("INVENTED"));
    var def =
        TestSims.index().parameters().stream()
            .filter(d -> d.id().equals(Ids.TEMPERATURE))
            .findFirst()
            .orElseThrow();
    assertEquals(0.004, def.loss().perSol());
  }

  @Test
  void aWarmedPlanetCoolsSlowlyWithEmittersOff() throws Exception {
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    s.model().set(Ids.TEMPERATURE, 5.0);
    assertEquals(-0.26, s.model().lossRatePerSol(Ids.TEMPERATURE), 1e-9);
    s.model().advanceSols(1);
    assertEquals(4.74, t(s), 0.02);
    s.model().advanceSols(4);
    assertEquals(3.71, t(s), 0.02);
    s.model().advanceSols(9);
    assertEquals(1.45, t(s), 0.03, "14 sols from 5 C, SME table");
  }

  @Test
  void noEffectAtTheBaselineAndNeverBelowIt() throws Exception {
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    s.model().advanceSols(10);
    assertEquals(-60.0, t(s), 1e-12);
    assertEquals(0.0, s.model().lossRatePerSol(Ids.TEMPERATURE), 0);
    s.model().set(Ids.TEMPERATURE, -60.0 + 1e-6);
    s.model().advanceSols(50);
    assertTrue(t(s) >= -60.0);
  }

  @Test
  void aPausedSessionDoesNotCool() throws Exception {
    PlanetSim s = TestSims.sim(Pace.STANDARD);
    s.model().set(Ids.TEMPERATURE, 5.0);
    SessionClock clock = new SessionClock(false);
    clock.pause();
    for (int i = 0; i < 50_000; i++) {
      s.onServerTick(clock);
    }
    assertEquals(5.0, t(s), 0.0);
  }

  @Test
  void theLeakScalesWithPaceButTheThresholdsDoNot() throws Exception {
    Pace room = Pace.classroom(4);
    PlanetSim std = TestSims.sim(Pace.STANDARD);
    PlanetSim cls = TestSims.sim(room);
    std.model().set(Ids.TEMPERATURE, 5.0);
    cls.model().set(Ids.TEMPERATURE, 5.0);
    assertEquals(
        room.rateMultiplier() * std.model().lossRatePerSol(Ids.TEMPERATURE),
        cls.model().lossRatePerSol(Ids.TEMPERATURE),
        1e-12);
    for (PlanetSim s : new PlanetSim[] {std, cls}) {
      var thaw = s.gates().defs().stream().filter(d -> d.id().equals("mars:thaw")).findFirst();
      assertEquals(-20.0, ((mars.gates.Condition.Compare) thaw.orElseThrow().condition()).value());
    }
  }

  @Test
  void emittersOutheatTheLeakAndTheMonitorSaysWhenTheLeakWins() throws Exception {
    PlanetSim s = TestSims.withPanels(TestSims.sim(Pace.STANDARD), 2);
    s.addMachine(EMIT, 1L);
    s.addMachine(EMIT, 2L);
    s.model().set(Ids.TEMPERATURE, 5.0);
    s.model().advanceTicks(100);
    assertFalse(leakLine(s), "two emitters give +0.55 against a 0.26 leak");
    assertTrue(s.model().netRatePerSol(Ids.TEMPERATURE) > 0.5);
    s.removeMachine(EMIT, 1L);
    s.removeMachine(EMIT, 2L);
    s.model().advanceTicks(100);
    assertTrue(leakLine(s), "no emitters at 5 C: warming fades");
    var row =
        MonitorView.of(s).rows().stream()
            .filter(r -> r.labelKey().equals("monitor.row.temperature"))
            .findFirst()
            .orElseThrow();
    assertEquals(MonitorView.Trend.FALLING, row.trend());
    PlanetSim cold = TestSims.sim(Pace.STANDARD);
    assertFalse(leakLine(cold), "at the baseline there is no leak line");
  }

  private static boolean leakLine(PlanetSim s) {
    return MonitorView.of(s).rows().stream()
        .filter(r -> r.labelKey().equals("monitor.row.temperature"))
        .flatMap(r -> r.causes().stream())
        .anyMatch(c -> c.sourceKey().equals("monitor.cause.temperature_leak") && c.direction() < 0);
  }
}
