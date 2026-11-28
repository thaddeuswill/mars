package mars.landing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

/** The Landing state machine (landing-spec 3.2, 3.3, 3.5): one test per rule. */
class LandingStateTest {
  private static LandingState running() {
    LandingState l = new LandingState();
    l.offer();
    l.start(0);
    return l;
  }

  private static List<Change.Kind> kinds(List<Change> c) {
    return c.stream().map(Change::kind).toList();
  }

  @Test
  void aNewPlayerIsOfferedOnceAndNothingTicksBeforeTheyStart() {
    LandingState l = new LandingState();
    assertEquals(Phase.NOT_OFFERED, l.phase());
    assertEquals(List.of(Change.Kind.OFFERED), kinds(l.offer()));
    assertEquals(Phase.OFFERED, l.phase());
    assertTrue(l.offer().isEmpty(), "the offer is made once");
    assertTrue(l.onDoorOpened(1).isEmpty(), "an offered Landing does not tick");
    assertNull(l.active());
    assertEquals(List.of(Change.Kind.STARTED), kinds(l.start(5)));
    assertEquals(Step.DOOR, l.active());
  }

  @Test
  void everyStepHasItsOwnCompletionConditionAndTicksDone() {
    LandingState l = running();
    assertEquals(Status.DONE, l.onDoorOpened(1).get(0).status());
    assertEquals(Step.TANK, l.active());
    l.onTankRead(2);
    assertEquals(Status.DONE, l.status(Step.TANK));
    l.onMonitorOpened(3);
    assertEquals(Status.DONE, l.status(Step.MONITOR));
    l.onKitHeld(4);
    assertEquals(Status.DONE, l.status(Step.KIT));
    l.onPatched(true, 5);
    assertEquals(Status.DONE, l.status(Step.PATCH));
    l.onOvenPlaced();
    l.onFacts(new Facts(true, true), 6);
    assertEquals(Status.DONE, l.status(Step.OVENS), "this player's Oven, powered: DONE not AUTO");
    // Airlock: out, outer door, back in.
    assertTrue(l.onPosition(false, 7).isEmpty());
    l.onOuterDoorUsed();
    assertTrue(l.onPosition(false, 8).isEmpty(), "inside only: the player has not been out");
    l.onPosition(true, 9);
    assertEquals(Status.PENDING, l.status(Step.AIRLOCK), "outside is not enough");
    List<Change> back = l.onPosition(false, 10);
    assertEquals(Status.DONE, l.status(Step.AIRLOCK));
    assertEquals(1, back.size());
    List<Change> last = l.onJournalOpened(11);
    assertEquals(List.of(Change.Kind.STEP, Change.Kind.FINISHED), kinds(last));
    assertEquals(Phase.DONE, l.phase());
    assertNull(l.active());
  }

  @Test
  void theAirlockNeedsTheOuterDoorNotJustAWalkAround() {
    LandingState l = running();
    l.onPosition(true, 1); // flying round the outside of the dome
    assertTrue(l.onPosition(false, 2).isEmpty());
    assertEquals(Status.PENDING, l.status(Step.AIRLOCK));
  }

  @Test
  void stepsTickInAnyOrderAndTheActiveStepIsTheLowestPending() {
    LandingState l = running();
    l.onJournalOpened(10);
    l.onMonitorOpened(11);
    assertEquals(Status.DONE, l.status(Step.JOURNAL), "a later step done early ticks at once");
    assertEquals(Step.DOOR, l.active(), "the active step stays the lowest pending one");
    l.onDoorOpened(12);
    assertEquals(Step.TANK, l.active());
  }

  @Test
  void aPatchMadeByThisPlayersKitAlsoTicksTheKitStep() {
    LandingState l = running();
    List<Change> c = l.onPatched(true, 3);
    assertEquals(Step.KIT, c.get(0).step());
    assertEquals(Step.PATCH, c.get(1).step());
    LandingState byHand = running();
    byHand.onPatched(false, 3);
    assertEquals(Status.PENDING, byHand.status(Step.KIT), "a plate placed by hand uses no kit");
    assertEquals(Status.DONE, byHand.status(Step.PATCH));
  }

  @Test
  void aTeammatesWorkIsAutoOnlyWhenTheStepBecomesActive() {
    LandingState l = running();
    Facts both = new Facts(true, true);
    assertTrue(l.onFacts(both, 5).isEmpty(), "step 1 is active: nothing is credited yet");
    assertEquals(Status.PENDING, l.status(Step.PATCH));
    l.onDoorOpened(6);
    l.onTankRead(7);
    l.onMonitorOpened(8);
    l.onKitHeld(9);
    List<Change> c = l.onFacts(both, 10);
    assertEquals(Status.AUTO, l.status(Step.PATCH), "step 5 is active and the world is sealed");
    assertEquals(Status.AUTO, l.status(Step.OVENS), "then step 6 is active and powered");
    assertEquals(2, c.size());
    assertEquals(Step.AIRLOCK, l.active());
  }

  @Test
  void aTeammateSealingHabBDoesNotCreditAPlayerWhoNeverPatched() {
    LandingState l = running();
    l.onFacts(new Facts(true, false), 1);
    assertEquals(Status.PENDING, l.status(Step.PATCH));
    assertFalse(l.toSaved().patchedByMe());
  }

  @Test
  void ovensNeedBothPoweredAndThePlayersOwnOvenForDone() {
    LandingState l = running();
    l.onOvenPlaced();
    assertTrue(l.onFacts(new Facts(false, false), 1).isEmpty(), "placed but not powered");
    assertEquals(Status.PENDING, l.status(Step.OVENS));
    l.onFacts(new Facts(false, true), 2);
    assertEquals(Status.DONE, l.status(Step.OVENS));
  }

  @Test
  void skipThisStepMarksOnlyThatStepAndFinishingTheRestFinishesTheLanding() {
    LandingState l = running();
    List<Change> c = l.skipStep(Step.OVENS, 4);
    assertEquals(Status.SKIPPED, c.get(0).status());
    assertEquals(
        Step.DOOR, l.active(), "skipping a later step changes nothing about the active one");
    for (Step s : Step.values()) {
      l.skipStep(s, 5);
    }
    assertEquals(Phase.DONE, l.phase(), "all steps settled: DONE, not SKIPPED_ALL");
    assertTrue(l.skipStep(Step.DOOR, 6).isEmpty(), "settled steps do not move again");
  }

  @Test
  void skipAllSettlesEveryUnfinishedStepKeepsFinishedOnesAndIsFinal() {
    LandingState l = running();
    l.onDoorOpened(1);
    List<Change> c = l.skipAll();
    assertEquals(List.of(Change.Kind.SKIPPED_ALL), kinds(c));
    assertEquals(Phase.SKIPPED_ALL, l.phase());
    assertEquals(Status.DONE, l.status(Step.DOOR), "what was done stays done");
    for (Step s : Step.values()) {
      if (s != Step.DOOR) {
        assertEquals(Status.SKIPPED, l.status(s));
      }
    }
    assertTrue(l.skipAll().isEmpty());
    assertTrue(l.start(9).isEmpty(), "never offered again");
    assertTrue(l.onJournalOpened(10).isEmpty());
  }

  @Test
  void theDrillCanBeSkippedStraightFromTheOffer() {
    LandingState l = new LandingState();
    l.offer();
    l.skipAll();
    assertEquals(Phase.SKIPPED_ALL, l.phase());
    assertTrue(l.phase().over());
    assertEquals(Status.SKIPPED, l.status(Step.DOOR));
  }

  @Test
  void theBriefingIsDueOnceAfterDoneOrSkippedAll() {
    LandingState l = running();
    assertFalse(l.briefingDue());
    l.skipAll();
    assertTrue(l.briefingDue());
    l.briefingShown();
    assertFalse(l.briefingDue(), "once per player");
    LandingState d = running();
    for (Step s : Step.values()) {
      d.skipStep(s, 1);
    }
    assertTrue(d.briefingDue(), "DONE through skipped steps also gets it");
  }

  @Test
  void aLateJoinerStartsOfferedWhateverTheWorldIsLike() {
    LandingState l = new LandingState();
    l.offer();
    l.start(100);
    assertEquals(Step.DOOR, l.active());
    // The world is already patched and powered: the early steps still come first.
    assertTrue(l.onFacts(new Facts(true, true), 101).isEmpty());
    assertEquals(Step.DOOR, l.active());
  }

  @Test
  void theHintIsDueAfterTheDelayOnTheActiveStepAndRestartsWhenTheStepChanges() {
    LandingState l = running(); // started at tick 0
    assertFalse(l.hintDue(3599, 3600));
    assertTrue(l.hintDue(3600, 3600));
    l.onDoorOpened(3600);
    assertFalse(l.hintDue(3601, 3600), "a new active step restarts the timer");
    assertTrue(l.hintDue(3600 + 3600, 3600));
    LandingState over = running();
    over.skipAll();
    assertFalse(over.hintDue(1_000_000, 1), "no hint when nothing is active");
  }

  @Test
  void theSavedFormRoundTripsEveryField() {
    LandingState l = running();
    l.onDoorOpened(10);
    l.onOvenPlaced();
    l.onOuterDoorUsed();
    l.onPosition(true, 11);
    l.onPatched(true, 12);
    l.skipStep(Step.JOURNAL, 13);
    LandingState back = LandingState.fromSaved(l.toSaved());
    assertEquals(l.toSaved(), back.toSaved());
    assertEquals(l.active(), back.active());
    assertEquals(Phase.RUNNING, back.phase());
    // The restored player carries on: the saved flags still complete the airlock and the ovens.
    assertEquals(Status.DONE, back.onPosition(false, 20).get(0).status());
    back.onFacts(new Facts(true, true), 21);
    assertEquals(Status.DONE, back.status(Step.OVENS));
  }

  @Test
  void aSavedFormWithMissingOrUnknownPartsLoadsAsPending() {
    LandingState.Saved odd =
        new LandingState.Saved(
            null, java.util.Arrays.asList(Status.DONE, null), 0, false, false, false, false, false);
    LandingState l = LandingState.fromSaved(odd);
    assertEquals(Phase.NOT_OFFERED, l.phase());
    assertEquals(Status.DONE, l.status(Step.DOOR));
    assertEquals(Status.PENDING, l.status(Step.TANK));
    assertEquals(Status.PENDING, l.status(Step.JOURNAL));
  }

  @Test
  void markRefusesStatusesThatAreNotAWayOfFinishing() {
    assertThrows(
        IllegalArgumentException.class, () -> running().mark(Step.DOOR, Status.PENDING, 0));
    assertThrows(
        IllegalArgumentException.class, () -> running().mark(Step.DOOR, Status.SKIPPED, 0));
  }

  @Test
  void resetGoesBackToTheStartForAnOwnerPlaytest() {
    LandingState l = running();
    l.onDoorOpened(1);
    l.skipAll();
    l.briefingShown();
    l.reset();
    assertEquals(Phase.NOT_OFFERED, l.phase());
    assertEquals(0, l.settled());
    assertFalse(l.briefingDue());
    assertEquals(List.of(Change.Kind.OFFERED), kinds(l.offer()));
  }

  @Test
  void stepNumbersAndPlanetStateFlagsMatchTheSpec() {
    assertEquals(8, Step.values().length);
    assertEquals(1, Step.DOOR.number());
    assertEquals(8, Step.JOURNAL.number());
    assertEquals(Step.AIRLOCK, Step.of(7));
    assertTrue(Step.PATCH.planetState());
    assertTrue(Step.OVENS.planetState());
    assertFalse(Step.MONITOR.planetState());
  }
}
