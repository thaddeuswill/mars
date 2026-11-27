# Technical Lead report: Stage 2 slice 5, polar-frost sink and Regolith Oven (2026-11-27)

BACKLOG #43. Owner scope: all three temperature branches of the polar-frost sink as pure Java and data, nothing tuned in play; slice 5 calls from `docs/design/balance-sheet.md` 2c (owner-accepted).

## 0. Handoffs received

| From | Handoff | Answer |
|---|---|---|
| Game Designer (2c) | Oven draw 1.0, rate 0.01, two starting Ovens once per world, equal share, stall below 5 kPa intended with "Levelling off", frost line hidden until about 0.85 kPa, Oven water out, one hold rule | **Accepted and built.** One deviation in placement, see section 4 (Levelling off sits on the Pressure row, not the Next goal row). |
| Game Designer (ui-specs 9 slice 5 wording) | Keys `monitor.cause.oven`, `polar_frost`, `polar_frost_release`, `.read_more`, `levelling_off`, `levelling_off.hs`, `monitor.oven.tooltip` | **Accepted.** All keys are in `en_us.json`. Four have no screen use yet: `monitor.cause.oven` (machine lines show reasons only, as for the Outgasser, owner-accepted option A), `.read_more` and `levelling_off.hs` (no read-more UI exists), see Risks. The tooltip is live. |
| SME (frost-sink-review.md) | Semantics (acts on total pressure above baseline in Stage 2; scale both sink and sources; buried amount not scaled), "sols 1 to 16 unverified" | **Accepted.** Built as written; see the curve comparison (section 6), which reproduces the early cells under stated assumptions. |
| Codex CX-3 checklist | Cold 2 % at 5 kPa and -50 C = -0.088/sol; zero at baseline; Monitor negative cause and pause; Oven full and empty-grid cases | **Accepted.** Each row has a named test (BACKLOG, CX-3 section updated). |

## 1. Done

Pure Java (no Minecraft imports, ArchUnit still green):
- `mod/src/main/java/mars/index/sim/FrostSink.java`: three branches (cold below -40 C at 2 %, mild -40 to -20 C both ends included at 1 %, release above -20 C at 0.5 kPa per sol then up to 0.5 kPa buried once), reservoir, `ratePerSol` for the Monitor, `equilibriumKpa`, `Def.parse` for the data file with a per-planet baseline.
- `PlanetSim`: sink added as a coupling with the machines' rate scale (pace multiplier x group scale); `frostRatePerSol`, `frostStoredKpa`, `frostBuriedRemainingKpa`, `frostBranch`, `frostEquilibriumKpa`.
- `PlanetState` schema 5 (`frost` state); schema 4 and older saves load with an empty reservoir; a newer schema is still refused.
- `mars.session.StartingGrant`: the once-per-world claim. `MachineDef.startingGrant`.
- Hold rule: `GateDef.Hold.resetAfter{Standard,Classroom}Sols` read from `reset_after_sols` in the gate file; `GateTracker` uses it; `Pace.holdResetSols()` removed.

Data (`pack/`):
- `data/mars/sinks/polar_frost.json` (baseline 0.6 as `baselineKpa.default` plus a `planets` map, thresholds, fractions, release rate, buried 0.5, all with source text and INVENTED labels).
- `data/mars/machines/regolith_oven.json` (rate 0.01, draw 1.0, `startingGrant` 2).
- `data/mars/gates/dome_stable.json`: `reset_after_sols` {1.0, 0.5}.
- `assets/mars/lang/en_us.json`: 9 keys (listed in section 0 plus `monitor.event.placed.regolith_oven` and `grant.starting`).

Minecraft layer:
- `RegolithOvenBlock` (registers by position like the other machines; not gated), Oven item tooltip, `MarsMachines` updated.
- `GrantData` (overworld save data, one flag), `GrantEvents` (dimension change and login), wired in `MarsMod`. `GrantData.resetForSelfTest` refuses to run without the test seam.
- `IndexDefinitions.Defs.frost`; `PlanetData.factory(planetId)` and `load(planetId, ...)` so the baseline is per planet; NBT `frost` tag.
- Monitor: `MonitorView` (frost cause line, steady threshold 0.005 for the pressure row, `Row.noteKey`, `CauseLine.direction`), `MonitorPayload` (both fields), `MonitorLayout`/`MonitorStyle`/`MonitorScreen` (shape plus signed sentence, two-line wrapped note, plot dropped when problem lines crowd it).
- `tools/screenshots/slice5.shots`.

Docs: ARCHITECTURE.md 2.19 (new row), `docs/tech/stage2-plan.md` row 5, BACKLOG (row 43, CX-3 note), HANDOFF.md, AGENTS.md, `docs/tech/launch-guide.md`.

## 2. Real output

Commands run from `mod/` (all `--offline`):

| Command | Result |
|---|---|
| `./gradlew spotlessApply` then `./gradlew spotlessCheck build test --rerun --offline` | BUILD SUCCESSFUL (8 s with warm daemon) |
| Unit tests (JUnit XML summed) | **142 tests, 0 failures** (was 114 after option A; +28) |
| `./gradlew runGameTestServer --offline` | `MARS-SELFTEST RESULT PASS passed=66 failed=0` (was 54; +12: 8 frost and Oven checks on real blocks, 4 grant checks) |
| `tools/screenshots/run-shots.sh /home/user/mars/tools/screenshots/slice5.shots` | rc=0, 47 s, `pgrep -f runShotsClient` printed nothing first. A first run had a bad script line (`/mars index advance 400` is refused, maximum 200); I fixed the script and reran; the first run's folder was deleted. Final PNGs: `tools/screenshots/out/20261127-125134/slice5_*.png` |

**Prove-it-can-fail** (each guard removed, `./gradlew test --rerun`, then restored; final run green):

| Guard removed | Tests that failed |
|---|---|
| `if (above <= 0) return 0` (zero at or below baseline) | `FrostSinkTest.zeroAtOrBelowBaselineAndItNeverAddsAirWhileTaking`, `theBaselineIsPerPlanetData` |
| group scale on the sink (sink scaled by pace only) | `FrostSinkTest.paceAndGroupScalingKeepTheEquilibriumEqual` |
| `buriedReleased += fromBuried` (buried CO2 gives back more than once) | `warmReturnsTheStoredAirThenTheBuriedHalfKpaOnce`, `rateScaleSpeedsUpTheSinkAndTheReleaseButNotTheBuriedAmount` |
| `claim()` guard in `StartingGrant` | `StartingGrantTest` (2 tests) |
| `<` changed to `<=` at -40 C (mild treated as cold) | `branchesSwitchExactlyAtMinus40AndMinus20`, `coldTakesTwoPercentAndMildOnePercentOf...` |

Not proved by removal: the GameTest checks (boot server) and the Monitor note rule. I did not remove a guard from those.

**Screenshots (I looked at slice5_2 to 6, and slice5_1 and 3 from the first run, same script lines):**
- `slice5_1`: after `/mars go` the chat shows "You have 2 x Regolith Oven in your pack. Place them, then check the Power row on the Monitor." and hotbar slot 1 holds 2 Ovens. This is the real event path (not the GameTest fake player).
- `slice5_2` (2 Ovens, sol 0): no frost line; "Making +5.0, using -2.0 units/sol"; +0.02 kPa/sol.
- `slice5_3` (sol 40, 1.15 kPa): frost line "Polar frost: −0.01 kPa per sol" with a down triangle; trend +0.01.
- `slice5_4` (sol 440, 1.60 kPa): frost line −0.02, "Levelling off: the frost takes back about as much as your machines add." on two lines, trend Steady +0.00; panel about 137 GUI px tall (measured from the image: 412 px / 3).
- `slice5_5` (Outgasser added, sol 500, 5.09 kPa): frost line −0.09; Goals reached 2 / 2; battery bar.
- `slice5_6` (`/mars index set temperature -10`): "Polar frost gives back: +0.50 kPa per sol" with an up triangle; trend +0.62 (machines 0.12 plus 0.50).

## 3. Numbers that moved, and the tests changed

The frost now acts in every sim built from the real data, so pressure with machines is below the old straight line. Examples (all from the real data files):

| Case | Before | Now |
|---|---|---|
| 1 Outgasser, 1 panel, 10 sols | 1.600 kPa | 1.5063 |
| 2 Outgassers (3 panels), 10 sols | 2.60 | 2.41 (Monitor trend +0.16, was +0.20; 2 cause lines) |
| 2 Outgassers on 1 panel, 10 sols | +1.25 | 1.7329 |
| Stage 4 panel, 1 Outgasser, 10 sols | 1.35 | 1.2797 |
| Boiling point shown at the 2.41 kPa case | 22 C (at 2.60) | 21 C |

Tests changed: `PlanetSimTest` (4 tests: analytic expected value `TestSims.coldPressure`, tolerance 1e-3; a 2-step tick test 1e-12 to 1e-6; save round trip loads with frost), `PowerSimTest` (4 expected values, battery test now two phases, save round trip), `MonitorViewTest` (value, trend, cause count, boiling point), `GateTest` (I-1 test uses frost-free sims, because a planet set to exactly 5.0 with no machines sinks below 5.0 before the gate reads it, which is correct play behaviour; classroom grace now read from data), `SessionClockTest` and `PressureHistoryTest` (frost-aware load), `MonitorLayoutTest` (two new states), GameTests `outgasser.raises_pressure` and `outgasser.chunk_unloaded` (tolerance now allows the frost's removal, `frostAllowance`). New: `FrostSinkTest` (13 tests), `MonitorFrostViewTest` (5), `StartingGrantTest` (3), `SoloCurveComparisonTest` (1), a `PowerGridTest` case, a `PowerSimTest` case and an Oven data-vs-balance-sheet test, a hold-rule test in `GateTest`.

**Not redone:** the older screenshot folders (`slice3_*`, `slice4_*`, `monitor_hist_*`) show the pre-slice-5 numbers (for example 2.60 kPa); their scripts would now read lower. Rerun them if an owner wants current pictures.

## 4. Design points and deviations (flagged, not hidden)

1. **"Levelling off" is on the Pressure row, not the Next goal row.** ui-specs 9 puts it in "the Next goal estimate", but that row has one detail line about 238 px wide and the sentence needs two lines; it also would not show while the next goal is Dome stable (a hold), when two Ovens stall at 1.6 kPa. Rule built: frost is taking, sources are above 0, net rate below 0.005 kPa/sol, and a pressure goal has not latched.
2. **Panel height.** Frost line +10 px and note +20 px. Measured with `MonitorLayoutTest`: normal 122, powered 130, short and dusty 135 (unchanged); **levelling off 144**; worst case (levelling, short on power, dusty, battery) 148 with the plot dropped. The target was about 130, so levelling off is about 14 px over. A one-line wording of about 45 characters would save 10 px; that is the Game Designer's wording (handoff below). New rule: the plot is dropped when problem lines would push the panel past the target plus 20 (`MonitorStyle.PLOT_OVERFLOW`).
3. **Pressure at a threshold.** The sink runs before the gate is read. A player sitting exactly on 5.0 kPa with weak machines can fall back under it and never latch. That is the intended stall (decision 4), not a bug, but a playtester may meet it at 4.99.
4. **Pressure trend rounding.** Pressure reads Steady when the net rate is under 0.005 kPa/sol (what the screen would round to 0.00), for the pressure row only.
5. **Starting grant goes to the first player on any planet.** In a group the first arrival holds both Ovens and must share them; there is no recipe yet. One flag per world (overworld), not per planet.
6. **Grant trigger.** Dimension change into a planet, or login while on a planet. Not exercised: a real dedicated server, a second real player, respawn after death in a real client (GameTest simulates these with a fake player and the pure flag).
7. **Hold rule.** Behaviour was already "pause, reset after more than the grace" from slice 4; slice 5 makes the grace data. `Pace.holdResetSols()` is removed.
8. **Oven in the placed-event list.** A pressure-making machine's first placement adds an event marker, so "Oven placed" now appears on the plot at sol 0.

## 5. Equilibrium and pace check

`FrostSinkTest.equilibriumIsBaselinePlusFiftyTimesTheNetSourceAndMatchesTheSheetTable` runs the six builds of 2c decision 4 for 1500 sols through the real sim: simulated equals the sheet within 0.01 kPa for every row (1.600, 5.600, 5.600, 4.350, 4.700, 11.599) and the "reaches 5 kPa" column matches (no, yes, yes, no, no, yes). `paceAndGroupScalingKeepTheEquilibriumEqual`: 2 Outgassers, 2 Ovens, 2 panels settle at 11.599 (standard), 11.598 (classroom, group 4), 11.5978 (group 3), 11.5985 (group 5), 11.5996 (standard rate, group 5). The Monitor's `frostEquilibriumKpa` is 5.6 in standard, classroom x4 and group 5.

## 6. Prototype curve against the balance-sheet solo curve (cell by cell)

`SoloCurveComparisonTest` (prints; asserts the late curve and the storm run within 0.06 kPa). Assumptions are mine; the sheet states none of them: "Sol N" read as the start of sol N (t = N - 1); two Ovens from the start; first Outgasser at the start of sol 9, 2 by 11, 3 by 13, 4 by 16; clean panels exactly enough for the demand (5 units each); storm on sols 14 and 15 modelled as panel output 40 % (a 60 % cut).

| Sol | Sheet | Sim, no storm | Diff | Sim, storm sols 14-15 | Diff |
|---|---|---|---|---|---|
| 1 | 0.61 | 0.600 | -0.010 | 0.600 | -0.010 |
| 5 | 0.67 | 0.677 | +0.007 | 0.677 | +0.007 |
| 9 | 0.74 | 0.748 | +0.008 | 0.748 | +0.008 |
| 10 | 0.86 | 0.864 | +0.004 | 0.864 | +0.004 |
| 12 | 1.18 | 1.184 | +0.004 | 1.184 | +0.004 |
| 14 | 1.70 | 1.687 | -0.013 | 1.687 | -0.013 |
| 16 | 1.93 | 2.272 | +0.342 | 1.920 | -0.010 |
| 18 | 2.71 | 3.025 | +0.315 | 2.681 | -0.029 |
| 20 | 3.46 | 3.754 | +0.294 | 3.423 | -0.037 |
| 22 | 4.18 | 4.453 | +0.273 | 4.135 | -0.045 |
| 24 | 4.87 | 5.126 | +0.256 | 4.820 | -0.050 |
| 25 | 5.20 | 5.452 | +0.252 | 5.152 | -0.048 |

Reading: **under these assumptions every cell of the sheet is reproduced within 0.05 kPa**, including sols 1 to 16 the SME could not reproduce; the storm is what explains the gap the SME saw (a storm of 40 % output with panels sized to demand slows four Outgassers a lot). Without the storm the sim is 0.25 to 0.34 kPa above the sheet from sol 16. Caveats: this shows consistency, not truth (other assumption sets could also fit); sol 1 differs by 0.01 under any reading (0.60 at t = 0, 0.62 at t = 1); the real game's Dome stable arrives about sol 3 on a stand-in, not sol 9, so real play will not follow this curve (slice 4 note); the sheet's Phase 2 and 3 tables were not touched.

## 7. Elapsed time and build cycles

About 15 minutes of wall-clock by the sandbox clock from the first read to the last verification, plus the doc edits and this report. Roughly 30 Gradle invocations (compile, spotless, unit test, five prove-it-fails runs, four `runGameTestServer`, two client screenshot runs). Counts are from my own tally, not a log.

## 8. Decisions needed

| # | Question | Recommendation | Type |
|---|---|---|---|
| 1 | The Levelling off state makes the Monitor 144 GUI px (target about 130). Accept, or shorten the sentence to about 45 characters (one line, about 134 px)? | Accept for the prototype; Game Designer proposes a one-line wording when the Monitor text is next reviewed. | well-structured (wording owner: Game Designer) |
| 2 | Lost Ovens: no recipe yet. Is a creative-tab or `/give` replacement acceptable until KubeJS recipes (a later slice)? | Yes for the prototype; add a cheap Oven recipe in the recipe slice (2c decision 2 already says craftable from the start). | well-structured |

## 9. Handoffs

> **To:** Lead Game Designer · **Problem type:** well-structured
> **Problem:** Balance sheet §2c decision 4 and the §2 curve can now be marked with measured results.
> **Context:** Section 5 and 6 above; the six-row table matches the simulation within 0.01 kPa; the solo curve is reproduced within 0.05 kPa under the stated assumptions (storm on sols 14 and 15 at 40 % panel output, panels sized to demand). One-Outgasser-for-10-sols now reads 1.51, not 1.60.
> **Needed from them:** (a) replace "calc, not run" in 2c with "run in `FrostSinkTest`"; (b) state the solo curve's build and storm assumptions in §2 so the test's reading is the sheet's; (c) a one-line wording (about 45 characters) for `monitor.estimate.levelling_off`, or accept 144 px.
> **Done when:** the sheet and the tests name the same assumptions.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** New player-facing straw-man text needs the grade 6-7 and CR-F9/CR-F10 check.
> **Context:** `pack/assets/mars/lang/en_us.json`: `grant.starting`, `monitor.cause.polar_frost`, `monitor.cause.polar_frost_release`, `monitor.estimate.levelling_off`, `monitor.oven.tooltip`, `monitor.event.placed.regolith_oven`. Landing step 6 ("battery and Ovens") is already owner-approved.
> **Needed from them:** reading-level check and any rewording; confirm the journal line that says the frost is faster than real Mars (SME wording, frost-sink-review Q2).
> **Done when:** the keys carry reviewed text.

> **To:** SME · **Problem type:** well-structured
> **Problem:** The curve comparison (section 6) reproduces cells you could not, under assumptions you did not have.
> **Needed from them:** a look at the two assumptions (panels sized to demand; a 60 % storm cut on sols 14 and 15) and whether the early-cell flag can be lifted.
> **Done when:** the flag in `docs/science/frost-sink-review.md` Q3 is updated.

> **To:** Producer · **Problem type:** well-structured
> **Problem:** Re-shoot decision for the older screenshot folders (slice 3 and 4, `monitor_hist_*`) whose numbers are now stale. Slice 6 is next in the plan.

## 10. Risks

- **Stall teaches or frustrates:** one Outgasser on a dusty panel now cannot finish Phase 1 (4.35 to 4.7 kPa). Intended by the owner; the Monitor says "Levelling off" and the dust cause line is on the power row. First lever if playtests show stuck players is the sink rate (2 % to 1.5 %), not the gate.
- **Gate at the edge:** see section 4, point 3.
- **Monitor height:** 144 to 148 GUI px in the stall states; fits 240 px (GUI scale 3), not tested at scales 2 or 4 or a very short window.
- **Four keys with no screen use** (`monitor.cause.oven`, `.read_more`, `levelling_off.hs`; and the read-more UI itself) are straw man, not tested.
- **Group grant:** the first arrival holds both Ovens.
- **Single baseline source:** the sink acts on total pressure above baseline, over-applying up to about 40 % in Phase 2 (SME); revisit when CO2 partial pressure is tracked.
- **`/reload` mid-game** keeps running planets on old frost numbers (same as other data).
- **Temperature never changes by itself**, so warm and mild branches are reachable only by `/mars index set temperature`.
- Estimates and the 144 px figure are measured by `MonitorLayoutTest` arithmetic and one screenshot, not by a user.

## 11. Shared model updates

- **Decision (owning doc: balance sheet 2c and ARCHITECTURE 2.19):** the polar-frost sink is data (`data/mars/sinks/polar_frost.json`), baseline per planet, rate-scaled like machines; the buried amount never scales. Equilibrium P = baseline + S / fraction is the same in every mode and is tested.
- **Decision:** the hold rule's grace (`reset_after_sols`) is gate data, not a constant.
- **Term:** "starting grant" = items a machine file gives the first player on a planet, once per world.
- **Lesson:** when a new sink changes every expected number, derive test expectations from the formula (`TestSims.coldPressure`) rather than editing constants; and a boundary test at exactly the threshold (5.0 kPa) must account for the order the step applies the sink and the gate.
- **Lesson:** a shots script must respect command limits (`/mars index advance` maximum 200); read the first screenshot of a run before trusting the rest.
