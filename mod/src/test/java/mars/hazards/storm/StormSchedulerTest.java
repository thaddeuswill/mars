package mars.hazards.storm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import mars.TestData;
import mars.hazards.storm.StormScheduler.Event;
import mars.hazards.storm.StormScheduler.Inputs;
import mars.hazards.storm.StormScheduler.Kind;
import mars.hazards.storm.StormScheduler.Mode;
import org.junit.jupiter.api.Test;

/**
 * The pure storm scheduler against the real data file (slice 5 step D; AC-3, AC-4, AC-5, AC-7,
 * AC-8).
 */
class StormSchedulerTest {
  private static final int SOL = 24_000;
  private static final int STEP = 100;
  private static final Inputs OK = new Inputs(0, true, true);

  static StormTuning tuning() throws Exception {
    try (Reader r =
        Files.newBufferedReader(TestData.data("hazards/dust_storm.json"), StandardCharsets.UTF_8)) {
      return StormTuning.parse(r, "dust_storm.json");
    }
  }

  private static StormScheduler sched(boolean classroom, long seed) throws Exception {
    return new StormScheduler(tuning(), classroom, SOL, STEP, seed);
  }

  /**
   * Step from tick {@code from} (exclusive) to {@code to} (inclusive); collect events and
   * strengths.
   */
  private static List<Event> run(
      StormScheduler s, long from, long to, Inputs in, List<double[]> strengths) {
    List<Event> out = new ArrayList<>();
    for (long t = from + STEP; t <= to; t += STEP) {
      var step = s.step(t, in);
      out.addAll(step.events());
      if (strengths != null) {
        strengths.add(new double[] {t, step.strength()});
      }
    }
    return out;
  }

  private static Event first(List<Event> events, Kind k) {
    return events.stream().filter(e -> e.kind() == k).findFirst().orElseThrow();
  }

  @Test
  void theFirstStormIsOnSol14StandardWithAOneSolWarning() throws Exception {
    var ev = run(sched(false, 1), 0, 16L * SOL, OK, null);
    System.out.println("standard first storm events: " + ev);
    long warned = first(ev, Kind.WARNED).tick();
    long reminder = first(ev, Kind.REMINDER).tick();
    long onset = first(ev, Kind.ONSET).tick();
    long clearing = first(ev, Kind.CLEARING).tick();
    long ended = first(ev, Kind.ENDED).tick();
    assertEquals(14.0 * SOL, onset, STEP);
    assertEquals(1.0, (double) (onset - warned) / SOL, 0.2, "warning lead 0.8 to 1.2 sols");
    assertEquals(0.5, (double) (onset - reminder) / SOL, 0.05, "reminder at T-0.5 sol");
    assertEquals(0.75, (double) (ended - onset) / SOL, 0.01, "length 0.75 sol");
    assertEquals(0.075, (double) (ended - clearing) / SOL, 0.005, "ramp-out 0.075 sol");
  }

  @Test
  void theFirstClassroomStormIsOnSol4_5AndHalfASolLongWithTheSameWarning() throws Exception {
    var ev = run(sched(true, 1), 0, 6L * SOL, OK, null);
    long warned = first(ev, Kind.WARNED).tick();
    long onset = first(ev, Kind.ONSET).tick();
    long ended = first(ev, Kind.ENDED).tick();
    assertEquals(4.5 * SOL, onset, STEP);
    assertEquals(1.0, (double) (onset - warned) / SOL, 0.2, "warning lead is never scaled");
    assertEquals(0.5, (double) (ended - onset) / SOL, 0.01);
    // AC-20: Dome stable at sol 2.5 (classroom) and the first warning are 1 sol apart or more.
    assertTrue((double) warned / SOL - 2.5 >= 0.99, "warning at " + (double) warned / SOL);
  }

  @Test
  void theFirstStormIsTheSameForTwentySeedsAndNothingTheCallerDoesMovesIt() throws Exception {
    for (boolean classroom : new boolean[] {false, true}) {
      long want = -1;
      for (long seed = 1; seed <= 20; seed++) {
        var s = sched(classroom, seed);
        s.step(STEP, OK);
        assertEquals(Mode.SCHEDULED, s.mode());
        if (want < 0) {
          want = s.onsetTick();
        }
        assertEquals(want, s.onsetTick(), "seed " + seed);
      }
    }
  }

  @Test
  void envelopeShapeAndCap() throws Exception {
    var s = sched(false, 1);
    List<double[]> st = new ArrayList<>();
    run(s, 0, 16L * SOL, OK, st);
    long onset = 14L * SOL;
    for (double[] p : st) {
      double sols = (p[0] - onset) / SOL;
      if (sols < 0 || sols >= 0.75) {
        assertEquals(0.0, p[1], 0.0, "no strength outside the storm at " + p[0]);
      } else if (sols > 0.05 && sols < 0.75 - 0.075) {
        assertEquals(1.0, p[1], 1e-12, "full strength in the hold");
      } else if (sols > 0 && sols <= 0.05) {
        assertEquals(sols / 0.05, p[1], 0.03, "ramp in");
      } else if (sols >= 0.75 - 0.075) {
        assertEquals((0.75 - sols) / 0.075, p[1], 0.03, "ramp out");
      }
    }
    // A cap of 0.8 scales the whole envelope.
    var capped = sched(false, 1);
    List<double[]> st2 = new ArrayList<>();
    for (long t = STEP; t <= 16L * SOL; t += STEP) {
      st2.add(new double[] {t, capped.step(t, new Inputs(1, true, true)).strength()});
    }
    double max = st2.stream().mapToDouble(p -> p[1]).max().orElse(0);
    assertEquals(0.8, max, 1e-12);
  }

  @Test
  void theLockPointNothingChangesBeforeOnsetAndOnsetAndFirstStrengthShareAStep() throws Exception {
    for (long seed = 1; seed <= 1000; seed++) {
      var s = sched(seed % 2 == 0, seed);
      boolean onsetSeen = false;
      for (long t = STEP; t <= 16L * SOL; t += STEP) {
        var step = s.step(t, OK);
        boolean onsetNow = step.events().stream().anyMatch(e -> e.kind() == Kind.ONSET);
        if (!onsetSeen && !onsetNow) {
          assertEquals(0.0, step.strength(), 0.0, "seed " + seed + " tick " + t);
        }
        if (onsetNow) {
          onsetSeen = true;
          assertTrue(step.strength() >= 0);
        }
        if (step.strength() > 0) {
          assertTrue(onsetSeen, "strength above 0 only from the onset step on");
        }
      }
    }
  }

  @Test
  void theWarningIsNeverShorterThanTheLeadForAnyLaterStorm() throws Exception {
    for (long seed = 1; seed <= 200; seed++) {
      for (boolean classroom : new boolean[] {false, true}) {
        var s = sched(classroom, seed);
        long scheduledAt = -1;
        long warnedAt = -1;
        long onsetAt = -1;
        int storms = 0;
        for (long t = STEP; t <= 120L * SOL && storms < 6; t += STEP) {
          var step = s.step(t, new Inputs(1, true, true));
          for (var e : step.events()) {
            if (e.kind() == Kind.WARNED) {
              warnedAt = e.tick();
            }
            if (e.kind() == Kind.ONSET) {
              onsetAt = e.tick();
              assertTrue(
                  (double) (onsetAt - warnedAt) / SOL >= 0.99,
                  "seed " + seed + " lead " + (double) (onsetAt - warnedAt) / SOL);
            }
            if (e.kind() == Kind.ENDED) {
              storms++;
            }
          }
        }
        assertTrue(storms >= 3, "several storms in 120 sols: " + storms);
      }
    }
  }

  @Test
  void laterStormsAreSeededWithGapAndLengthInTheirRanges() throws Exception {
    for (boolean classroom : new boolean[] {false, true}) {
      var s = sched(classroom, 77);
      List<Event> ev = new ArrayList<>();
      for (long t = STEP; t <= 200L * SOL; t += STEP) {
        ev.addAll(s.step(t, new Inputs(1, true, true)).events());
      }
      List<Event> onsets = ev.stream().filter(e -> e.kind() == Kind.ONSET).toList();
      List<Event> ends = ev.stream().filter(e -> e.kind() == Kind.ENDED).toList();
      assertTrue(onsets.size() >= 4);
      for (int i = 1; i < onsets.size(); i++) {
        double gap = (double) (onsets.get(i).onsetTick() - ends.get(i - 1).tick()) / SOL;
        double len = (double) (ends.get(i).tick() - onsets.get(i).onsetTick()) / SOL;
        System.out.printf("%s storm %d: gap %.2f length %.2f%n", classroom, i, gap, len);
        double lo = classroom ? 2.0 : 8.0;
        double hi = classroom ? 3.5 : 14.0;
        assertTrue(gap >= lo - 0.01 && gap <= hi + 0.02, "gap " + gap);
        assertTrue(len >= 0.49 && len <= (classroom ? 0.51 : 1.01), "length " + len);
      }
    }
  }

  @Test
  void sameSeedSameStormsDifferentSeedDifferentStorms() throws Exception {
    List<Long> a = onsets(77);
    assertEquals(a, onsets(77));
    assertNotEquals(a, onsets(78));
  }

  private static List<Long> onsets(long seed) throws Exception {
    var s = sched(false, seed);
    List<Long> out = new ArrayList<>();
    for (long t = STEP; t <= 150L * SOL; t += STEP) {
      for (var e : s.step(t, new Inputs(1, true, true)).events()) {
        if (e.kind() == Kind.ONSET) {
          out.add(e.onsetTick());
        }
      }
    }
    return out;
  }

  @Test
  void aSaveAndLoadInTheMiddleGivesTheSameFutureAtEveryPhaseOfAStorm() throws Exception {
    // Cut at: scheduled, warned, active (ramp in), active (hold), ending, idle after.
    long[] cuts = {
      SOL,
      13L * SOL + 50 * STEP,
      14L * SOL + 3 * STEP,
      14L * SOL + 40 * STEP,
      14L * SOL + 170 * STEP,
      15L * SOL
    };
    for (long cut : cuts) {
      var s = sched(false, 5);
      run(s, 0, cut / STEP * STEP, new Inputs(1, true, true), null);
      var copy = sched(false, 5);
      copy.restore(s.toState());
      assertEquals(s.toState(), copy.toState());
      var ea = run(s, cut / STEP * STEP, 40L * SOL, new Inputs(1, true, true), null);
      var eb = run(copy, cut / STEP * STEP, 40L * SOL, new Inputs(1, true, true), null);
      assertEquals(ea, eb, "cut at " + cut);
      assertEquals(s.toState(), copy.toState());
    }
  }

  @Test
  void theGuardsMoveTheStormOneSolAtATimeThenGoAheadVoid() throws Exception {
    // Nobody has finished the Landing: three one-sol delays, then the warning goes out void.
    var s = sched(false, 1);
    var ev = run(s, 0, 20L * SOL, new Inputs(0, false, true), null);
    Event warned = first(ev, Kind.WARNED);
    assertTrue(warned.voided());
    assertEquals(
        14.0 + 3.0, (double) warned.onsetTick() / SOL, 0.02, "three retries, one sol each");
    assertEquals(first(ev, Kind.ONSET).onsetTick(), warned.onsetTick());
    // No panel standing: the same.
    var s2 = sched(false, 1);
    assertTrue(
        first(run(s2, 0, 20L * SOL, new Inputs(0, true, false), null), Kind.WARNED).voided());
    // One guard recovers after the first delay: not void, one sol late.
    var s3 = sched(false, 1);
    List<Event> ev3 = new ArrayList<>();
    for (long t = STEP; t <= 20L * SOL; t += STEP) {
      boolean landed = t > 13L * SOL + 50 * STEP;
      ev3.addAll(s3.step(t, new Inputs(0, landed, true)).events());
    }
    Event w3 = first(ev3, Kind.WARNED);
    assertFalse(w3.voided());
    assertEquals(15.0, (double) w3.onsetTick() / SOL, 0.02);
  }

  @Test
  void phase1HasOneStormUntilPhase2BeginsThenTheNextIsScheduled() throws Exception {
    var s = sched(false, 9);
    var inPhase1 = new Inputs(0, true, true);
    var ev = run(s, 0, 100L * SOL, inPhase1, null);
    assertEquals(1, ev.stream().filter(e -> e.kind() == Kind.ONSET).count());
    assertEquals(Mode.NONE, s.mode());
    long phase2At = 100L * SOL;
    var ev2 = new ArrayList<Event>();
    for (long t = phase2At + STEP; t <= 130L * SOL; t += STEP) {
      ev2.addAll(s.step(t, new Inputs(1, true, true)).events());
    }
    Event sched = first(ev2, Kind.SCHEDULED);
    assertTrue(sched.tick() <= phase2At + 2 * STEP, "scheduled as soon as the phase allows");
    assertTrue(ev2.stream().anyMatch(e -> e.kind() == Kind.ONSET));
  }

  @Test
  void theDataFileHasTheSpecValues() throws Exception {
    var t = tuning();
    assertEquals(1.0, t.warningLeadSols());
    assertEquals(0.5, t.reminderLeadSols());
    assertEquals(0.05, t.rampInSols());
    assertEquals(0.075, t.rampOutSols());
    assertEquals(14.0, t.standard().firstOnsetSol());
    assertEquals(4.5, t.classroom().firstOnsetSol());
    assertEquals(0.75, t.standard().firstLengthSols());
    assertEquals(0.5, t.classroom().firstLengthSols());
    assertEquals(8.0, t.standard().gapMinSols());
    assertEquals(14.0, t.standard().gapMaxSols());
    assertEquals(2.0, t.classroom().gapMinSols());
    assertEquals(3.5, t.classroom().gapMaxSols());
    assertEquals(
        List.of(1.0, 0.8, 0.5, 0.25), t.caps().stream().map(StormTuning.Cap::cap).toList());
    // The highest latched gate wins; a cap row whose gate cannot latch yet never applies.
    assertEquals(1, t.capIndex(g -> g.equals("mars:phase_2_begins")));
    assertEquals(0, t.capIndex(g -> false));
  }
}
