package mars.hazards.storm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import mars.TestData;
import mars.TestSims;
import mars.hazards.storm.StormSchedule.Event;
import mars.hazards.storm.StormSchedule.Phase;
import org.junit.jupiter.api.Test;

/**
 * The storm schedule: numbers equal the balance sheet, seeded and saved, pace scales timing only.
 */
class StormScheduleTest {
  private static List<String> run(StormSchedule s, double fromSol, double toSol) {
    List<String> log = new ArrayList<>();
    for (long n = Math.round(fromSol * 240) + 1; n <= Math.round(toSol * 240); n++) {
      double t = n / 240.0;
      for (Event e : s.step(t)) {
        log.add(e + "@" + Math.round(t * 1000) / 1000.0);
      }
    }
    return log;
  }

  private static StormSchedule fresh(boolean classroom, long seed) throws Exception {
    StormTuning t = TestSims.stormTuning();
    return StormSchedule.initial(t.mode(classroom), t.capPhase1(), seed, 0);
  }

  @Test
  void dataEqualsTheBalanceSheetStormTable() throws Exception {
    String sheet =
        Files.readString(
            TestData.pack().resolve("../docs/design/balance-sheet.md").normalize(),
            StandardCharsets.UTF_8);
    int a = sheet.indexOf("**Storm schedule (settled");
    int b = sheet.indexOf("Only the storm's length, gap and warning scale", a);
    assertTrue(a >= 0 && b > a, "storm schedule table not found");
    String table = sheet.substring(a, b);
    StormTuning t = TestSims.stormTuning();
    assertTrue(table.contains("**2 sols** (about 40 min of play) | **0.5 sol**"));
    assertEquals(2, t.standard().phase1LengthSols());
    assertEquals(0.5, t.classroom().phase1LengthSols());
    assertTrue(table.contains("| **1 sol** | **0.25 sol**"));
    assertEquals(1, t.standard().holdLengthSols());
    assertEquals(0.25, t.classroom().holdLengthSols());
    assertTrue(table.contains("Not before sol 14"));
    assertTrue(table.contains("Not before sol 4"));
    assertEquals(14, t.standard().firstNotBeforeSol());
    assertEquals(4, t.classroom().firstNotBeforeSol());
    assertTrue(table.contains("12–20 sols"));
    assertTrue(table.contains("3–5 sols"));
    assertEquals(12, t.standard().gapMinSols());
    assertEquals(20, t.standard().gapMaxSols());
    assertEquals(3, t.classroom().gapMinSols());
    assertEquals(5, t.classroom().gapMaxSols());
    assertTrue(table.contains("1 sol before onset (0.25 sol classroom)"));
    assertEquals(1, t.standard().warningSols());
    assertEquals(0.25, t.classroom().warningSols());
    assertTrue(table.contains("Phase 1 cap 1.0 = 60 %"));
    assertEquals(1.0, t.capPhase1());
    assertEquals(0.6, TestSims.dustTuning().stormSunlightCutPerCap());
    assertTrue(table.contains("+1 stage; +2 if cap ≥ 0.8"));
    assertEquals(1, TestSims.dustTuning().stagesAtStormEnd(0.5));
    assertEquals(2, TestSims.dustTuning().stagesAtStormEnd(0.8));
    assertEquals(2, TestSims.dustTuning().stagesAtStormEnd(1.0));
  }

  @Test
  void firstStormWarnsOneSolAheadStartsAtSol14AndLastsTwoSols() throws Exception {
    StormSchedule s = fresh(false, 7);
    List<String> log = run(s, 0, 17);
    System.out.println("standard first storm: " + log);
    assertEquals(List.of("WARNED@13.0", "STARTED@14.0", "ENDED@16.0"), log.subList(0, 3));
    assertEquals(Phase.CALM, s.phase());
  }

  @Test
  void classroomStormIsShorterAndEarlierButTheSameKind() throws Exception {
    StormSchedule s = fresh(true, 7);
    List<String> log = run(s, 0, 6);
    System.out.println("classroom first storm: " + log);
    assertEquals(List.of("WARNED@3.75", "STARTED@4.0", "ENDED@4.5"), log.subList(0, 3));
  }

  @Test
  void gapsStayInRangeAndAreSeeded() throws Exception {
    for (boolean classroom : new boolean[] {false, true}) {
      StormTuning.Mode m = TestSims.stormTuning().mode(classroom);
      StormSchedule s = fresh(classroom, 12345);
      double prevEnd = Double.NaN;
      for (int i = 0; i < 40; i++) {
        double now = s.startSol();
        // Walk to the end of this storm, then read the next start.
        s.forceStart(now, StormSchedule.Kind.PHASE_1);
        double end = s.endSol();
        s.step(end);
        assertEquals(Phase.CALM, s.phase());
        double gap = s.startSol() - end;
        assertTrue(gap >= m.gapMinSols() && gap <= m.gapMaxSols(), "gap " + gap);
        prevEnd = end;
      }
      assertFalse(Double.isNaN(prevEnd));
    }
  }

  @Test
  void sameSeedSameScheduleAndAnotherSeedDiffers() throws Exception {
    List<String> a = run(fresh(true, 99), 0, 120);
    List<String> b = run(fresh(true, 99), 0, 120);
    List<String> c = run(fresh(true, 100), 0, 120);
    assertTrue(a.size() >= 30, "classroom has many storms in 120 sols: " + a.size());
    assertEquals(a, b);
    assertNotEquals(a, c);
  }

  @Test
  void aSavedAndRestoredScheduleContinuesIdentically() throws Exception {
    StormTuning t = TestSims.stormTuning();
    StormSchedule straight = fresh(false, 5);
    run(straight, 0, 200);
    StormSchedule first = fresh(false, 5);
    run(first, 0, 90); // mid-run, possibly mid-storm
    StormSchedule restored = new StormSchedule(t.mode(false), t.capPhase1(), first.toState());
    assertEquals(first.toState(), restored.toState());
    run(restored, 90, 200); // resume exactly where the first run stopped
    assertEquals(straight.toState().rng(), restored.toState().rng());
    assertEquals(straight.toState().stormsEnded(), restored.toState().stormsEnded());
    assertEquals(straight.startSol(), restored.startSol(), 1e-6);
  }

  @Test
  void aLateLoadStillWarnsBeforeTheFirstStorm() throws Exception {
    StormTuning t = TestSims.stormTuning();
    StormSchedule s = StormSchedule.initial(t.mode(false), t.capPhase1(), 1, 40);
    assertTrue(s.startSol() >= 41 - 1e-9, "start " + s.startSol());
  }

  @Test
  void operatorStartAndEnd() throws Exception {
    StormSchedule s = fresh(false, 3);
    assertFalse(s.forceEnd(1));
    assertTrue(s.forceStart(1, StormSchedule.Kind.HOLD));
    assertEquals(2.0, s.endSol(), 1e-9); // hold storm: 1 sol
    assertFalse(s.forceStart(1.5, StormSchedule.Kind.PHASE_1));
    assertTrue(s.forceEnd(1.5));
    assertEquals(Phase.CALM, s.phase());
    assertEquals(1, s.stormsEnded());
    assertEquals(1.5, s.lastEndSol());
  }
}
