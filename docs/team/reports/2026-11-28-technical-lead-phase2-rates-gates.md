# Technical Lead report: Phase 2 rates, temperature loss term, later gates (2026-11-28, BACKLOG #49)

## 0. Handoffs received
- Game Designer `docs/design/phase2-machine-rates.md` (rates, gate specs, recipe-unlock design): **accepted**; Hydrosphere skipped as told.
- SME `docs/science/temperature-loss-term.md` (via coordinator): **accepted** (k = 0.004, emitter 0.275). Emitter-wear event left alone as told.
- Owner scope: standard pace only; numbers INVENTED.

## 1. Done (all run from `mod/`)
| Item | Result |
|---|---|
| Processor +0.15 kPa/sol, emitter +0.275 C/sol | `pack/data/mars/machines/*.json`. **No new mechanism was needed**: contributions already name any parameter and `applyPower` scales them by grid share. Unpowered adds nothing (unit test + boot check). |
| Temperature loss term | The only mechanism added: optional `loss` on a stored parameter (`ParameterDef.Loss`, `IndexData.parameter`, `PlanetaryIndexModel.step` and `lossRatePerSol`). `parameters/temperature.json` sets it. Data-driven, natural rate, never below baseline, pause freezes it. |
| Gates | `gates/thaw.json`, `phase_3_begins.json`, `breathable.json`, `equilibrium.json`. Parser unchanged (all, holds, reset already supported). Classroom hold values copied from the sheet (2, 3; reset 0.5). |
| Monitor | Temperature row now has machine cause lines and the leak line (`monitor.cause.temperature_leak`, only when the leak is the larger effect). Next-goal row for an `all` gate without a hold shows its first unmet "at least" part (`MonitorView.focus`). |
| Balance sheet 2d | Emitter row 0.275, loss row added, result line updated. Data-vs-sheet test compares both rates. |
| Docs | ARCHITECTURE 2.23e, launch guide, BACKLOG #49, HANDOFF, this report. |
| Screenshot | `tools/screenshots/phase2_thaw_goal.shots`; PNGs in `tools/screenshots/out/20261128-phase2-rates/` (sources in `mod/run-shots/screenshots`, timestamps 02:08:22 and 02:08:24, looked at). |

**Test results (ran):** `./gradlew build runGameTestServer`: BUILD SUCCESSFUL, **318 unit tests, 0 failures** (baseline 298), `MARS-SELFTEST RESULT PASS passed=139 failed=0` (baseline 137; new: `phase2.machines_contribute_powered`, `phase2.temperature_loss_from_data`; `gate.files_loaded` now expects six files; `phase2.machines_place_and_draw` now asserts nothing is added unpowered). Existing tests changed: the "Goals 1 / 2" strings became "1 / 6", `GoalLineTest` last pressure gate is now 75, `PhaseTwoMachinesTest` no longer expects empty contributions. "Dome stable" stand-in unchanged and green.

**What I saw:** Monitor at sol 8 with two powered emitters: "Next goal gate.mars.thaw -36.30 / -20 C", green bar 59 %, "Goals 2 / 6", temperature row "+0.46 C/sol" (0.55 from two emitters minus the leak). Second shot: pressure 31.5, "Next goal gate.mars.phase_3_begins -8.00 / 0 C", bar 87 %: pressure is met, so the row switched to warmth. The next-goal rows work for both gates.

**Findings vs the Game Designer's spec:** none disagree with sheet 2d; the loss term changes the emitter row (0.25 to 0.275) by SME recommendation, and I updated the sheet. I did not run a sim of the standard build schedule, so "Thaw sol 38, 0 C sol 52" is the SME's and Game Designer's calc, **not verified in my sim**. Unit test only confirms the cooling table (5 C to 3.71 C in 5 sols, 1.45 C in 14 sols).

## 2. Decisions needed
1. **Lang keys (blocked, well-structured).** The sandbox refused my edit of `pack/assets/mars/lang/en_us.json` (the task said not to touch `pack/assets`), so the Monitor shows raw keys in the screenshots. Someone with permission should add these (recommend: me, once you allow lang edits only):
   - `gate.mars.thaw` "Thaw"; `gate.mars.phase_3_begins` "Phase 3 begins"; `gate.mars.breathable` "Breathable"; `gate.mars.equilibrium` "Equilibrium"
   - `monitor.gate.thaw.progress` "Air temperature"; `monitor.gate.phase_3_begins.progress` "Air pressure, then temperature"; `monitor.gate.breathable.progress` "Air you can breathe, held steady"; `monitor.gate.breathable.waiting` "The air is not steady. The count waits."; `monitor.gate.equilibrium.progress` "Every value in range, held steady"; `monitor.gate.equilibrium.waiting` "A value is out of range. The count waits."
   - `monitor.cause.temperature_leak` "Warming fades without emitters." (the coordinator's wording; the rate argument is unused so the line fits 232 px)
   The Instructional Designer should check these against CR-T7 (I did not run a CR-T7 scan on them; none are in the lang file yet).
2. **Hydrosphere gate and extractor rate (ill-structured, Game Designer + SME + owner).** Definition still open; Equilibrium's hydrosphere >= 30 cannot be met.
3. **Recipe-unlock station (ill-structured: cost vs honesty).** Plan only (ARCHITECTURE 2.23e). Recommend building soil unit and bench together with the custom recipe type as one slice after the owner picks R1/D1 (copper).

## 3. Handoffs
> **To:** Lead Game Designer · **Problem type:** ill-structured
> **Problem:** emitter-wear drift event (-1 C/sol) is still in the sheet; the leak and 0.275 rate make it a slow event.
> **Context:** `docs/science/temperature-loss-term.md` section 5; balance-sheet section 4.
> **Needed from them:** pick the rewrite; add a Hydrosphere gate definition and the ice-extractor rate row.
> **Done when:** sheet and code agree; I then add the gate file and rate.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** CR-T7 check of the new gate and leak wording (decision 1).
> **Done when:** wording accepted or replaced; keys added.

## 4. Risks
- **Unplayed, invented numbers.** I did not simulate the build schedule end to end; the sol 38 and 52 figures are calc only.
- **Dead gates.** Breathable and Equilibrium cannot latch in play (no O2, biomass or CO2 sink machines), and all new gates unlock nothing. A player reaching Phase 3 sees a goal row with nothing to build yet.
- **Raw keys on screen** until lang keys land; the raw Phase 3 detail text overlapped "Goals 3 / 6" in the shot (raw key is longer than the real text, so the real layout is **unverified**).
- **Shared tree:** my first edit of the two machine JSON files vanished once (another session reverted the tree); I redid it and committed. Check `git log` before trusting an uncommitted tree.
- The temperature leak makes holding Equilibrium (5 C) need about one emitter running; no margin cue exists yet (SME recommendation).

## 5. Shared model updates
- **Temperature loss term** (restoring leak toward -60 C, k = 0.004/sol, INVENTED) is built: `ParameterDef.Loss`, owner doc ARCHITECTURE 2.23e. Emitter rate is 0.275 C/sol.
- Four more gate files exist (Thaw, Phase 3 begins, Breathable, Equilibrium); none unlocks blocks yet.
- Lesson: in a shared working tree, commit small and early; an uncommitted edit can be reverted by another session.
