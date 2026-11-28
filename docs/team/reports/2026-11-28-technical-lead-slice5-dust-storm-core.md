# Technical Lead report: slice 5 steps A to D (dust over time, brush, Monitor line, storm core)

Date 2026-11-28 · Role: Technical Lead · Task: BACKLOG #65 · Jar `0.4.1-stage3-j`, sha256 `e55e9d66b9c67dda920ecb66fc0a5f5825bf41019b16979855a221ddb2583fd2` · Inputs read: HANDOFF, BACKLOG, `docs/tech/slice5-plan.md`, `docs/design/f1-storm-spec.md`, balance sheet 2a and 2b, `docs/science/dust-review.md`.

**`docs/science/storm-dust-review.md` does not exist** (checked with `ls`). I used the older `docs/science/dust-review.md` (SME). It does not change any value I built: it calls the dust speed (about 15 times real) and the devil chance (10 to 30 times real) "invented, off by a lot" and the storm cut, floor and deposit "reasonable, invented". I kept the spec numbers, all tagged invented in the data files. When the SME's storm review lands, check `dust_storm.json` tags only.

## 0. Handoffs received

| From | Handoff | Answer |
|---|---|---|
| Producer (task text) | Steps A, B, C, D in order, commit and push after each green step | Accepted and done (4 commits, below). E, F, G not started. |
| Game Designer (spec) | AC-1 to AC-21 | Built and tested: AC-1, 2, 3, 4, 5, 7, 8, 9, 11 (solver run in a unit test), 12 (void on no panel), 15, 16, 17 (heights), 19 (GameTest), 20. Not in this slice: AC-6 (no F1 yet; the guard is built), AC-10, 13, 14 (forecast and journal, steps F and G), AC-18 (storm sky, step E), AC-21 (wiper). |
| Game Designer (spec) | Storm lines should read "Dust storm in about {eta}. It lasts half a sol to a full sol." | **Pushed back (shortened):** that sentence is wider than the 232 px Monitor panel (seen in the first screenshot, it ran out of the panel). The Monitor line now reads "Dust storm in about half a sol." The length sentence stays in the chat warning (`log.storm.warn`, exact spec text). Instructional Designer to approve the text. |
| Slice 5 plan | Per-panel registry in `PlanetSim`, schema 8, migration, chunk-load sync, `/mars dust` | Accepted and built as planned. |
| Task text | Brush recipe Fe 2 W 1 with the seal kit's stand-ins | Done: Fe = iron ingot, W = copper ingot (`recipe_costs.json` `materials`; the seal kit recipe uses Fe = iron ingot and B = brick). `recipe_costs.json` has a `mars:panel_brush` entry and `playableRecipes` lists it, so `RecipeCostsTest` checks the recipe file against the cost table. |

## 1. Done

All commits are on `claude/awesome-sagan-tb7mon` and pushed. Elapsed time is from file timestamps, so approximate: about 45 minutes of work. Gradle invocations were about 60 (my tally, not a log): about 20 `spotlessApply`/`compile`, about 20 `build test --rerun`, about 20 `runGameTestServer`, plus the mutation runs and 3 screenshot runs (about 55 s each).

| Step | Commit | What |
|---|---|---|
| A | `9f74470` | `mars.hazards.dust.PanelDustModel` (per-panel progress, stage, partly cleaned, deposit, clean, devil, chunk index); registry in `PlanetSim` (efficiency now derived from it); **`PlanetState` schema 8** with `HazardState` (planet seed, dust per panel, storm state) and migration from schema 7 (stage inferred from the saved efficiency, exact match, else stage 0); chunk-load sync (`ChunkEvent.Load` queues, blocks written on the next planet tick); `/mars dust get\|set`; `dust_storm.json` and pure `StormTuning` |
| B | `9c49b4c` | `mars:panel_brush` (64 uses, 2 s hold, any cell of the 3 x 3 array, no use on a clean panel, none in creative), recipe, texture, model, lang, creative tab; `PanelCleanedEvent`; seeded dust devil (pure hash of seed, chunk, sol; 1 percent, Phases 1 and 2, from stage 2 -> partly cleaned) |
| C | `7d27a97` | Monitor "Panel dust -n %" (average share of clean solar output lost) replacing the old "dusty, makes less" line (same height); `MonitorSender.forPlayer` holds it until that player's Landing is over, then one "New on the Monitor: panel dust." line and a `LineRevealedEvent`; `MonitorView.withoutCause`, `hasCause`; DevTools `hold <ticks>` step |
| D | `dc5a8dd` + this commit | `StormScheduler` (pure, seeded, tick-based), phase caps from data, solar multiplier, storm-end deposit in two steps, `mars.core.AttributedSources` implemented by `PlanetSim`, `StormEvent` on the bus, chat lines for players whose Landing is over, Monitor cloud line, active line and cut line, layout fit order, `/mars storm get\|schedule\|end` |

Files (main): `mod/src/main/java/mars/hazards/**`, `mars/core/AttributedSources.java`, `mars/index/sim/PlanetSim.java`, `PlanetState.java`, `PlanetCheckpoint.java`, `mars/planets/PlanetData.java`, `PlanetManager.java`, `PlanetEvents.java`, `StormEvent.java`, `mars/machines/PanelBrushItem.java`, `PanelCleanedEvent.java`, `SolarPanelBlock.java`, `MarsMachines.java`, `mars/monitor/MonitorSender.java`, `LineRevealedEvent.java`, `monitor/view/MonitorView.java`, `mars/client/MonitorLayout.java`, `MonitorScreen.java` (cloud shape), `mars/admin/HazardCommands.java`, `mars/story/landing/LandingData.java` and `LandingService.java` (one method each), `mars/selftest/HazardGameTests.java`. Data/assets: `pack/data/mars/hazards/dust_storm.json`, `recipe/panel_brush.json`, `recipe_costs.json`, `lang/en_us.json`, item model and texture. Docs: ARCHITECTURE.md 2.17 and new 2.23, `docs/tech/launch-guide.md`, HANDOFF 0j, AGENTS.md, BACKLOG row 65, `docs/tech/stage3-plan.md`.

### Real output (run by me)

- `./gradlew spotlessApply spotlessCheck build test --rerun --offline`: green. **347 unit tests, 0 failed** (was 295 at slice 4; 52 new: `PanelDustSimTest` 15, `StormSchedulerTest` 12, `StormSimTest` 12, `MonitorStormLayoutTest` 7, `MonitorDustLineTest` 5, and the migrated ones).
- `./gradlew runGameTestServer --offline`: `MARS-SELFTEST RESULT PASS passed=146 failed=0` (was 134). New GameTests: `dust.starts_clean`, `dust.block_follows_time`, `dust.nbt_round_trip`, `dust.setblock_sets_the_planet`, `dust.unloaded_chunk_then_load` (chunk really unloaded, model reached stage 3, block shows 3 after load), `brush.*` (3), `monitor.dust_line_waits_for_landing`, `storm.warned_then_cuts_supply`, `storm.brush_midstorm_save_and_end_dust`.
- Timings and values printed by the tests: standard stage times 3, 6, 9, 12 sols and classroom 0.75, 1.5, 2.25, 3 (AC-1); stage-1 panel in a full storm 1.84 (cap 1.0) and 2.39 (cap 0.8 latched), formula 3.22 and 3.91 for caps 0.5 and 0.25 (AC-2); first storm onset sol 14.0 standard, 4.5 classroom, warning 1.0 sol before, length 0.75 / 0.5, ramp-out 0.075 (AC-4, 5, 8); later gaps 9.2 to 13.2 sols standard in the run (range 8 to 14), classroom 2 to 3.5 (asserted); 300 panels: about 65 microseconds per step.
- **Guard-removal proof** (each guard removed by hand, tests run, file restored; `git status` clean afterwards): (1) onset event at `>=` instead of `>` (lock-point order): 1 test failed; (2) storm-end deposit moved to the onset: 2 failed; (3) stage clamp removed: 10 failed; (4) Landing and panel guard removed from the scheduler: 3 failed; (5) Landing hold on the dust line removed (GameTest): `monitor.dust_line_waits_for_landing` FAIL, `RESULT FAIL passed=145 failed=1`.
- **Screenshots, real client, software GL, viewed by me** (`pgrep -f runShotsClient` was empty before each run; absolute script paths; PNG times checked):
  - `tools/screenshots/out/20261128-182343/` (script `slice5_dust.shots`): `dust_1` to `dust_4` the starter panel at stage 1 to 4 (dark blue to brown); `dust_5` Monitor with "Panel dust -40 %" and the down shape; `dust_6`/`dust_7` the **real brush**, hold-right-click for 55 ticks in the client: stage 4 brown to stage 0 blue; `dust_8` Monitor with the line gone and "Making +5.0".
  - `tools/screenshots/out/20261128-183512/` (script `slice5_storm.shots`): `storm_1` chat shows the warning and reminder lines; `storm_2` Monitor warned ("Dust storm in about half a sol.", cloud shape, fits the panel); `storm_3` Monitor active (cloud line "Clearing in about half a sol", "Storm: less sunlight -60 %", Making +1.8, the dust line "-8 %" separate); `storm_5` after the storm "Panel dust -30 %" (stage 1 to 3) and the plot back. `storm_4`/`storm_6` show the panel unchanged in the sky because there is no storm look yet (step E).
  - First storm screenshot run showed the long storm line overflowing the panel: fixed (shortened) and re-shot. The old chat output lines "Storm WARNED, strength..." are the `/mars storm get` command output.

## 2. Decisions needed

| # | Question | Type | Recommendation |
|---|---|---|---|
| 1 | The spec's `monitor.storm.expected` text does not fit the 232 px panel. I shortened it to "Dust storm in about {eta}." and kept the length sentence in the chat warning. Approve, or allow a two-line wrap (+10 px) | Well-structured (text; Instructional Designer) | Keep the short line. |
| 2 | **Phase 1 allows one storm until Phase 2 begins** (`max_storms: 1` in `dust_storm.json`, from the spec sentence "Phase 1 has one storm in the slice"). The spec gives no number; a longer Phase 1 would have no second storm. Is that intended? | Ill-structured (pacing; Game Designer and owner) | Keep for the slice; revisit after the first full playtest. Data-only change. |
| 3 | A stage rise clears the partly-cleaned flag (dust covers the wiped patch). The spec clears it only at the storm-end deposit and by the brush. Alternative: keep the flag until the stage is cleaned or deposited (more generous) | Well-structured (balance; Game Designer) | Keep mine (simpler, slightly harsher). One-line change if the Game Designer disagrees. |
| 4 | A bug I fixed that was not in my task: `PlanetCheckpoint.restoreOver` dropped the life-support stores on a rollback, so a restored planet fell back to the legacy stand-in. It now rolls the life stores back with the snapshot. Confirm this is wanted | Well-structured | Keep the fix; ask the owner to accept. |

## 3. Handoffs

> **To:** Codex · **Problem type:** well-structured
> **Problem:** Step F and the learning events can now bind to real hooks.
> **Context:** `mars.planets.StormEvent` (kinds SCHEDULED, WARNED, REMINDER, ONSET, CLEARING, ENDED; fields `tick`, `onsetTick`, `voided`, `fastForward`); `mars.machines.PanelCleanedEvent` (player, stageBefore, stormWarned); `mars.monitor.LineRevealedEvent` (player, lineId `monitor.power.cause.dust`); `mars.core.AttributedSources` with `STORM_SOLAR_CUT = "mars:storm_solar_cut"` and `PANEL_DUST_LOSS`, **signed, negative when supply is removed** (the spec's check is `<= -0.01` for "drops"); `PlanetSim.stormWarned()` is true while a forecast is open; `PlanetSim` implements `AttributedSources` (`PlanetManager.data(level).sim()`). Posting happens on the server thread right after the step, before anything reads the new strength.
> **Needed from them:** event kinds `storm.warned`, `storm.onset`, `storm.ended`, `panel.cleaned`, `monitor.line_revealed` in `mars/learning/**`; the forecast engine reads the source through the interface only; events with `fastForward` true must log as admin cause and resolve forecasts void. The CR-F10 solver run exists as a unit test (`StormSimTest.theAttributedStormCutIgnoresWhatThePlayerDoesMidStorm`, includes the negative control: net supply rose while the attributed cut stayed negative); lift it into the forecast fixtures.
> **Done when:** the events appear in a real exported CSV.

> **To:** Instructional Designer / Game Designer · **Problem type:** well-structured
> **Problem:** Text drafts I wrote or shortened need an ID pass.
> **Context:** `pack/assets/mars/lang/en_us.json`: `monitor.storm.expected`, `monitor.storm.active`, `monitor.power.cause.storm`, `monitor.power.cause.dust`, `monitor.dust.revealed` ("New on the Monitor: panel dust."), `log.storm.remind`, `log.storm.onset` ("The dust storm has arrived.", my draft: the spec gives no onset text), `log.storm.end` (spec text), `log.storm.warn` (spec text, exact). A unit test scans the warn, remind and expected texts for solar, power, sunlight, light, dim (CR-F9). The eta words are English strings built in `MonitorView.etaWords` (not translatable keys). `journal.dust.panels` is **not built** (no journal entry file exists for it; it is held with the line per the spec once it exists).
> **Needed from them:** approve or rewrite the texts; supply the `journal.dust.panels` entry.
> **Done when:** texts final.

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** Brush item texture is a placeholder I drew (16 x 16, brown handle, grey ferrule, tan bristles, `pack/assets/mars/textures/item/panel_brush.png`), and the Monitor cloud shape is 8 x 3 px of fills.
> **Needed from them:** a reference check and a real texture, and the HUD storm icon shape for step E.
> **Done when:** reference image check recorded.

> **To:** Producer · **Problem type:** well-structured
> **Problem:** Batch order in GameTests changed when I added batches and exposed an order dependency in an old test (`outgasserRaisesPressureThroughRealTicks` assumed no machines; `AdminGameTests` leaves one). It now resets the planet first. New rule for GameTests: **tests in one batch run at the same time and share one planet, so a test that resets the planet needs its own batch name** (mine are `mars_hazard_a1`, `a2`, `b`, `c`, `d`).
> **Done when:** noted in the test strategy or lessons.

## 4. What remains for slice 5 and the integration points

| Step | What | Where it plugs in |
|---|---|---|
| **E** storm look and HUD (Technical Lead) | Payload `mars:storm` (strength, mode, ticks to onset), keyframe `overlay` input keyed on `mars:storm_strength`, pre-storm haze (fog end -10 % over 0.25 sol, `mars:storm_haze`), HUD cloud icon with countdown | Read `PlanetSim.stormStrength()`, `stormMode()`, `stormEtaSols()`, `stormOnsetTick()`; send on change from `PlanetEvents.onLevelTick` next to `SkySync.onPlanetTick`; client loader `mars.client.sky` (slice 6); icon uses the same cloud shape as `MonitorScreen.drawCloud`. The haze is a function of the scheduler's WARNED mode and ticks since the warning (add `haze(now)` to `StormScheduler`). If the overlay is cut: fog only (spec 2.4) |
| **F** forecast engine (Codex) | `mars.learning.forecast`, `resolve_source`, CR-F9 and CR-F10 tests on shipped data | `AttributedSources`, `StormEvent`, `PlanetSim.stormWarned()` |
| **G** F1 content and card (Technical Lead) | Card screen, delivery at a natural pause, result card, "What caused it?" prompt, journal pages, Hana's tip, learning events | Hold points exist: `LandingService.landingOver(player)`, `MonitorSender.forPlayer`. The F1 result needs "storm onset + 0.25 sol", available from `StormEvent.onsetTick` |
| Open items in A to D | Local dust (2x near digging and exhaust, spec 3), rain (Phase 3), wiper, the "Dust stage n of 4" action-bar line when looking at a panel, `journal.dust.panels` | Optional H in the plan; the action-bar line is client code on `SolarPanelBlock` |

## 5. Risks

- **Not run:** a storm landing inside the Dome stable hold (plan risk 5). The first storm is at sol 14 (Dome stable about sol 9.2 solo) and at 4.5 in classroom (Dome stable 2.5), so the fixed schedule avoids it, but a slow group's hold could still reset; I wrote no test for the interaction with the real life-support loop. **Not run:** real multiplayer, a dedicated server, GUI scale 4 for the new lines (unit-tested heights: storm active + dust + leak 155 px, fits 228 at scale 3), the storm with a real `/mars pause` in a client.
- **Balance consequence to know:** an uncleaned starting panel reaches stage 4 at sol 9 and is at 60 percent when the first storm arrives at sol 14 (unit test saw 1.2 units, not the table's 1.84, until I brushed it back to stage 1). The player must brush before the storm; that is the design, but it means a standard player who never brushes enters the storm at 1.2 units. The brush recipe is crafting-table cheap (3 ingots); no starting brush in the Ops locker (spec open item G4).
- Storm events from `/mars index advance` are marked `fastForward` and make no chat noise; real ticks do not. Operator-scheduled storms do not count against the phase storm budget.
- The planet seed is derived from the world seed and the dimension at first creation and saved. A schema 7 world gets a seed on first load; its first storm is `max(sol 14, loaded time + 1 sol + one step)`, never in the past.
- Dust on a block is rewritten with flag 2 (clients only). A large array field at storm end writes one block per panel in one tick (about 2 stage writes per panel per storm); unmeasured beyond 300 simulated panels (the sim side is measured, the block remeshing is not).
- The `peakSince` history is not saved; after a reload it only knows the current value.
- Plan estimate check: the plan said A 1.0, B 0.5, C 0.4, D 1.3 sessions (3.2 total). This took one session of about 45 minutes with about 60 Gradle runs, below the plan's 25 to 40 runs for A alone. The plan overestimated; the specs and the plan removed most of the unknowns.

## 6. Shared model updates (for TEAM.md)

- **Lesson:** GameTests in the same batch run concurrently on one shared planet; a test that calls `resetForSelfTest` needs its own batch, and batch order is not alphabetical (adding a batch exposed an order dependency in an old test).
- **Lesson:** "first step where strength > 0" and "onset tick" differ by one step when the envelope is 0 at the onset tick; the onset event must be defined on the strength, not the tick (lock-point rule, CR-F9). The scheduler fires ONSET at `now > onsetTick`.
- **Decision proposed for the log (Technical Lead, flexible):** per-panel dust progress lives in the planet sim, not in a block entity; the block state is a view written on stage change and chunk load (supersedes the 2.17 first plan). Revisit if panels need per-panel interaction state beyond stage.
- **Term:** "attributed source" = a named cause of supply (`mars:storm_solar_cut`, `mars:panel_dust_loss`), signed, kept apart from the net total so a forecast can resolve on it (CR-F10).
