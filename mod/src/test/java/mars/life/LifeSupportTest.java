package mars.life;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import mars.TestSims;
import mars.dome.Probe;
import mars.index.sim.Pace;
import mars.index.sim.PlanetSim;
import mars.index.sim.PlanetState;
import org.junit.jupiter.api.Test;

/**
 * Life support (balance sheet 2d): the numbers match the sheet, the model moves the stores, the
 * tiers order the power, and Dome stable arrives when the table in 2d.7 says. Every timing test
 * prints actual against table.
 */
class LifeSupportTest {
  static final String MAKER = "mars:oxygen_maker";
  static final String RECLAIMER = "mars:water_reclaimer";
  static final String TANK = "mars:o2_tank";
  static final long SEED = Pos(2, 2, 2);

  static long Pos(int x, int y, int z) {
    return mars.dome.Pos.pack(x, y, z);
  }

  /** A closed 3 x 3 x 3 room: sealed. With {@code hole}, one wall cell is open sky. */
  static Probe room(boolean hole) {
    return (x, y, z) -> {
      if (hole && x == 0 && y == 2 && z == 2) {
        return Probe.Cell.OUTSIDE;
      }
      boolean wall = x <= 0 || x >= 4 || y <= 0 || y >= 4 || z <= 0 || z >= 4;
      boolean outer = x < 0 || x > 4 || y < 0 || y > 4 || z < 0 || z > 4;
      return outer ? Probe.Cell.OUTSIDE : wall ? Probe.Cell.AIRTIGHT : Probe.Cell.OPEN;
    };
  }

  // ---- data against the balance sheet 2d (CX-C: a changed number fails here) ----

  @Test
  void lifeSupportJsonMatchesTheBalanceSheet() throws Exception {
    LifeParams p = TestSims.life();
    assertEquals(8, p.o2UsePerPlayerSol());
    assertEquals(3, p.waterUsePerPlayerSol());
    assertEquals(14, p.o2StartFillPerPlayer());
    assertEquals(60, p.waterStartFill());
    assertEquals(5, p.batteryStartCharge());
    assertEquals(200, p.o2TankCapacity());
    assertEquals(120, p.waterTankCapacity());
    assertEquals(10, p.leakO2PerSol());
    assertEquals(3, p.reserveSols(false));
    assertEquals(1, p.reserveSols(true));
    assertEquals(10, p.waterLastSols());
    assertEquals(1.2, p.landingEndSol());
    assertEquals(120, p.blackoutAfterSeconds());
    assertEquals(30, p.blackoutEmptySuitSeconds());
    assertEquals(10, p.blackoutWakeDelaySeconds());
    assertEquals(6, p.suitO2Tier1());
    assertEquals(10, p.suitO2Tier2());
    assertEquals(1, LifeParams.prebuiltMakers(1));
    assertEquals(1, LifeParams.prebuiltMakers(2));
    assertEquals(2, LifeParams.prebuiltMakers(3));
    assertEquals(2, LifeParams.prebuiltMakers(4));
    assertEquals(3, LifeParams.prebuiltMakers(5));
  }

  @Test
  void machineJsonMatchesTheBalanceSheet() throws Exception {
    var defs = TestSims.defs();
    var maker = defs.get(MAKER);
    assertEquals(10, maker.life().o2PerSol());
    assertEquals(1.0, maker.power().drawPerSol());
    assertEquals(1, maker.life().priority());
    var rec = defs.get(RECLAIMER);
    assertEquals(0.9, rec.life().recovery());
    assertEquals(5, rec.life().servesPlayers());
    assertEquals(1.0, rec.power().drawPerSol());
    assertEquals(1, rec.life().priority());
    assertEquals(40, defs.get(TANK).life().capacity());
    assertEquals(2, defs.get(TestSims.OVEN).life().priority());
    assertEquals(0.5, defs.get(TestSims.OVEN).life().waterPerSol());
    assertEquals(3, defs.get(TestSims.OUTGASSER).life().priority());
  }

  // ---- stores ----

  /** A dome for n players: pre-built makers, reclaimer, 5 tanks, battery, 2 Ovens, panels. */
  static PlanetSim dome(Pace pace, int makers, int panels, boolean sealedRoom) throws Exception {
    PlanetSim s = TestSims.lifeSim(pace);
    for (int i = 0; i < makers; i++) {
      s.addMachine(MAKER, 100L + i);
    }
    s.addMachine(RECLAIMER, 200L);
    for (int i = 0; i < 5; i++) {
      s.addMachine(TANK, 300L + i);
    }
    s.addMachine(TestSims.BATTERY, 400L);
    s.addMachine(TestSims.OVEN, 500L);
    s.addMachine(TestSims.OVEN, 501L);
    for (int i = 0; i < panels; i++) {
      s.addMachine(TestSims.PANEL, 1000L + i);
      s.setEfficiency(1000L + i, 0.92); // dust stage 1
    }
    s.domes().register(SEED);
    s.domes().refreshDirty(room(!sealedRoom));
    s.startLifeSupport();
    return s;
  }

  @Test
  void startFillIsFourteenPerPlayerAndTheBatteryFive() throws Exception {
    PlanetSim s = dome(new Pace(1.0, 4, 24_000, 100), 2, 3, true);
    assertEquals(56, s.life().o2(), 1e-9);
    assertEquals(60, s.life().water(), 1e-9);
    assertEquals(5, s.power().charge(), 1e-9);
    assertEquals(200, s.life().report().o2Capacity(), 1e-9);
  }

  @Test
  void nothingIsUsedBeforeTheLandingEnds() throws Exception {
    PlanetSim s = dome(Pace.STANDARD, 1, 1, true);
    s.model().advanceSols(1.0);
    assertEquals(14, s.life().o2(), 1e-9, "no use and no making before sol 1.2");
    assertFalse(s.life().states().airOk(), "no state can start a hold during the Landing");
    s.model().advanceSols(0.5); // to sol 1.5: 0.3 sol of net +2
    assertEquals(14 + 0.3 * 2, s.life().o2(), 0.05);
  }

  @Test
  void useScalesWithGroupSizeOnlyAndNotWithTheClassroomRate() throws Exception {
    for (Pace p : new Pace[] {new Pace(1.0, 4, 24_000, 100), Pace.classroom(4)}) {
      PlanetSim s = dome(p, 0, 3, true); // no makers: pure use
      s.model().advanceSols(1.2 + 1.0);
      assertEquals(56 - 4 * 8.0, s.life().o2(), 0.5, "4 players use 8 each per sol in " + p);
    }
  }

  @Test
  void aBreachDrainsTenMorePerSol() throws Exception {
    PlanetSim sealed = dome(Pace.STANDARD, 1, 1, true);
    PlanetSim leaky = dome(Pace.STANDARD, 1, 1, false);
    sealed.model().advanceSols(2.2);
    leaky.model().advanceSols(2.2);
    assertEquals(10.0, sealed.life().o2() - leaky.life().o2(), 0.1, "10 units lost in one sol");
    assertFalse(leaky.life().states().sealed());
    assertTrue(sealed.life().states().sealed());
  }

  @Test
  void anUnpoweredMakerStarvesTheAir() throws Exception {
    PlanetSim s = dome(Pace.STANDARD, 1, 0, true); // no panel: the battery (5) runs out
    s.model().advanceSols(1.2 + 3.0);
    assertEquals(0, s.life().report().o2Made(), 1e-9, "no power, no oxygen");
    assertTrue(s.life().report().o2Net() < 0);
    assertEquals(0, s.life().o2(), 1e-9, "the tank ran dry: 14 units at 8 a sol");
    assertFalse(s.life().states().airOk());
    assertFalse(s.life().states().powerOk());
  }

  @Test
  void waterHoldsUpWithTheReclaimerAndFallsWithout() throws Exception {
    PlanetSim with = dome(Pace.STANDARD, 1, 1, true);
    with.model().advanceSols(1.2 + 5);
    assertTrue(with.life().states().waterOk());
    assertTrue(with.life().water() > 59, "loss 0.3 a sol, ovens give 1.0: water does not fall");
    PlanetSim without = TestSims.lifeSim(new Pace(1.0, 4, 24_000, 100));
    for (int i = 0; i < 2; i++) {
      without.addMachine(MAKER, 100L + i);
    }
    without.addMachine(TANK, 300L);
    without.addMachine(TestSims.PANEL, 1000L);
    without.startLifeSupport();
    without.model().advanceSols(1.2 + 5);
    assertFalse(without.life().states().waterOk(), "no reclaimer: not ok");
    assertEquals(60 - 5 * 12.0, without.life().water(), 1.0, "4 players use 12 a sol");
  }

  // ---- power tiers (balance sheet 2d.4) ----

  @Test
  void lifeSupportIsNeverCutWhileOvensAndOutgassersDraw() throws Exception {
    // 1 panel at 92 % = 4.6; demand: tier 1 = 1 maker + reclaimer = 2, tier 2 = 2 Ovens = 2,
    // tier 3 = 1 Outgasser = 4. Total 8.
    PlanetSim s = dome(Pace.STANDARD, 1, 1, true);
    s.addMachine(TestSims.OUTGASSER, 700L);
    s.model().advanceSols(2.0);
    assertEquals(1.0, s.power().tierSatisfaction(1), 1e-9, "tier 1 is full");
    assertEquals(1.0, s.power().tierSatisfaction(2), 1e-9, "Ovens next, 2.0 of the 2.6 left");
    assertEquals(0.6 / 4.0, s.power().tierSatisfaction(3), 1e-6, "Outgasser gets the 0.6 left");
    // The Outgasser drained the battery (3.4 units short a sol), so there is no night's backup:
    // power_ok is false although tier 1 is fully powered. Intended (balance sheet 2d.5).
    assertFalse(s.life().states().powerOk());
    // A storm: panel at 40 %: 1.84 units; tier 1 (2.0) is short, tiers 2 and 3 get nothing.
    s.setEfficiency(1000L, 0.4);
    s.model().advanceSols(0.2);
    // The battery covers the shortfall at first, then tier 1 runs short while Ovens get nothing.
    s.model().advanceSols(3.0);
    assertTrue(s.power().tierSatisfaction(1) < 1.0 + 1e-9);
    assertEquals(0.0, s.power().tierSatisfaction(3), 1e-9);
    assertEquals(0.0, s.power().tierSatisfaction(2), 1e-9);
  }

  // ---- Dome stable timing (balance sheet 2d.7) ----

  /** Latch time in sols; -1 if it never latched within 40 sols. */
  static double latchSol(int n, boolean classroom, double firstExtraDelay, double nextDelay)
      throws Exception {
    Pace pace = classroom ? Pace.classroom(n) : new Pace(1.0, n, 24_000, 100);
    int pre = LifeParams.prebuiltMakers(n);
    int panels = (int) Math.ceil((n + 1 + 2) / 4.6) + 1; // panels "in time" (the sheet's rule)
    PlanetSim s = dome(pace, pre, panels, true);
    double t0 = 1.2;
    List<Double> builds = new ArrayList<>();
    for (int i = pre; i < n; i++) {
      builds.add(t0 + (classroom ? 0.3 : firstExtraDelay + nextDelay * (i - pre)));
    }
    int built = 0;
    double dt = 1.0 / 48.0;
    for (double t = 0; t < 40; t += dt) {
      while (built < builds.size() && t >= builds.get(built) - 1e-9) {
        s.addMachine(MAKER, 150L + built++);
      }
      s.domes().refreshDirty(room(false));
      s.model().advanceSols(dt);
      if (s.gates().isLatched("mars:dome_stable")) {
        return s.model().sols();
      }
    }
    return -1;
  }

  @Test
  void domeStableArrivesWhenTheSheetSaysSoloStandard() throws Exception {
    double got = latchSol(1, false, 0.6, 0.4);
    System.out.printf("Dome stable, solo standard: actual sol %.2f, table 9.2%n", got);
    assertEquals(9.2, got, 0.1);
  }

  @Test
  void domeStableForGroupsMatchesTheTable() throws Exception {
    double[] table = {10.7, 10.2, 11.2, 10.8}; // n = 2..5
    for (int n = 2; n <= 5; n++) {
      double got = latchSol(n, false, 0.6, 0.4);
      System.out.printf(
          "Dome stable, group %d standard: actual sol %.2f, table %.1f (%.2f h)%n",
          n, got, table[n - 2], got / 3.0);
      assertEquals(table[n - 2], got, 0.15, "group " + n);
      assertTrue(got / 3.0 >= 2.25 && got / 3.0 <= 3.75 + 0.05, "within 25 percent of 3 h");
    }
  }

  @Test
  void domeStableInTheClassroomMatchesTheTable() throws Exception {
    double got = latchSol(4, true, 0, 0);
    System.out.printf("Dome stable, classroom group 4: actual sol %.2f, table 2.5%n", got);
    assertEquals(2.5, got, 0.15);
  }

  @Test
  void aBreachDuringTheHoldPausesIt() throws Exception {
    PlanetSim s = dome(Pace.STANDARD, 1, 1, true);
    double dt = 1.0 / 48.0;
    double t = 0;
    while (s.model().sols() < 6.2 + 1.5) { // 1.5 sols into the hold
      s.domes().refreshDirty(room(false));
      s.model().advanceSols(dt);
      t += dt;
    }
    assertFalse(s.gates().isLatched("mars:dome_stable"));
    // Breach: the hold pauses (the air still lasts), and a repair resumes it.
    s.domes().markAllDirty();
    s.domes().refreshDirty(room(true));
    s.model().advanceSols(0.5);
    assertFalse(s.life().states().sealed());
    assertFalse(s.gates().isLatched("mars:dome_stable"));
    s.domes().markAllDirty();
    s.domes().refreshDirty(room(false));
    for (int i = 0; i < 48 * 4 && !s.gates().isLatched("mars:dome_stable"); i++) {
      s.domes().refreshDirty(room(false));
      s.model().advanceSols(dt);
    }
    assertTrue(s.gates().isLatched("mars:dome_stable"), "latches after the repair");
  }

  // ---- saves ----

  @Test
  void storesSurviveASaveAndAnOldSaveStaysOnTheStandIn() throws Exception {
    PlanetSim s = dome(Pace.STANDARD, 1, 1, true);
    s.model().advanceSols(3.0);
    PlanetState st = s.toState();
    assertEquals(PlanetState.CURRENT_SCHEMA, st.schema());
    assertNotNull(st.life());
    PlanetSim back =
        PlanetSim.fromState(
                st,
                TestSims.index(),
                TestSims.defs(),
                TestSims.gates(),
                TestSims.frost(),
                TestSims.life(),
                Pace.STANDARD)
            .sim();
    assertEquals(s.life().o2(), back.life().o2(), 1e-12);
    assertEquals(s.life().water(), back.life().water(), 1e-12);
    assertEquals(st, back.toState());
    // A schema 6 save has no life part: it loads without life support (the legacy stand-in).
    PlanetState old =
        new PlanetState(
            6,
            st.values(),
            st.simTicks(),
            st.pendingTicks(),
            st.machines(),
            st.batteryCharge(),
            st.efficiency(),
            st.gates(),
            st.history(),
            st.frost(),
            st.domes());
    PlanetSim oldSim =
        PlanetSim.fromState(
                old,
                TestSims.index(),
                TestSims.defs(),
                TestSims.gates(),
                TestSims.frost(),
                TestSims.life(),
                Pace.STANDARD)
            .sim();
    assertNull(oldSim.life(), "a world saved before schema 7 keeps the legacy stand-in");
    assertEquals(PlanetState.CURRENT_SCHEMA, oldSim.toState().schema());
    assertNull(oldSim.toState().life());
  }
}
