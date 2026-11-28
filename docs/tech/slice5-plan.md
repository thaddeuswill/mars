# Slice 5 plan: dust over time, storms, and the first forecast (F1)

**Owner:** Technical Lead · **Written:** 2026-11-28, docs only (no code or pack change) · **Stage:** 3, slice 5 · **Answers:** `stage3-plan.md` input T1 (panel-dust re-cut), the slice 5 row, and the "Technical Lead should re-estimate" rule (section 9) · **Related:** ARCHITECTURE.md 2.5 (storm strength), 2.6.1 and 2.6.2 (forecast lifecycle, lock, `reveals`), 2.17 (panel dust), 2.19 (sim, clock, power), balance sheet 2a and 2b, content rules CR-F9, CR-F10, CR-T8, [first-dust-storm prototype](../design/prototypes/first-dust-storm.md), keyframes 3.7a.

**Status:** plan, nothing built. Every estimate is **unverified** (section 7 says what is not known). At the time of writing neither `docs/design/f1-storm-spec.md` (Game Designer) nor `docs/science/storm-dust-review.md` (SME) existed, so every number below that comes from them is listed as an input in section 9, not assumed.

## 1. T1: what exists for panel dust, and what is missing

I read the code, not only the docs. Result: **slice 5 is bigger than the stage-3 plan guessed on the storm and forecast side, and smaller on the dust-state side**, because the part that feeds the grid already works.

### 1.1 Exists (verified by reading, and by the existing tests named)

| Piece | Where | State |
|---|---|---|
| `dust_stage` (0 to 4) and `partly_cleaned` block-state properties; six models mapped in the blockstate | `mars.machines.SolarPanelBlock`, `pack/assets/mars/blockstates/solar_panel.json` | Built. Nothing changes them except placement and `/setblock` |
| Tuning as data with a balance-sheet test | `pack/data/mars/hazards/panel_dust.json`, `mars.hazards.dust.PanelDustTuning`, `PanelDustTuningTest` | Built. Holds **every** number slice 5 needs for dust (interval 3 sols, output 100/92/82/70/60, partly cleaned, storm end +1/+2, devil 1 %/sol, rain, local x2, storm cut 0.6). Methods already exist: `outputFraction`, `outputInStorm(stage, pc, stormCap)`, `intervalSols(stormCap, rateMultiplier)` |
| Efficiency table feeding the grid | `SolarPanelBlock.onPlace` calls `PlanetManager.machineOutput(level, pos, outputFraction(state))`, which calls `PlanetSim.setEfficiency(pos, fraction)`; `PlanetSim.refreshPower` sums `supplyPerSol x efficiency` over all panels into `PowerGrid.setTierTotals`. Efficiency is saved in `PlanetState.efficiency` (schema 7) and rolled back by checkpoints | Built and tested (`PowerSimTest`, GameTest `power.dust_stage_cuts_output`). A dust-stage change **already** reaches the grid, because a stage change re-runs `onPlace` with the same block |
| Monitor "dusty" line | `MonitorView` line `monitor.cause.dusty`, count from `PlanetSim.reducedCount("mars:solar_panel")` | Built. It counts panels, not a percent; it has no storm line |
| Dev hooks | `/setblock` with a `dust_stage`, `/mars index advance <sols>`, boot checks `BootChecks` (stage 3 and 4 states), starter dome panel at stage 1 (`StarterDome`, balance sheet 2d.2) | There is **no** `/mars dust` command and no dust-aware dev step in `tools/screenshots` |
| Seeds and clock | `PlanetSim.onServerTick` counts server ticks, pause-safe (I-3), step = 100 ticks = 1/240 sol | Built. **No random seed is stored per planet** (`PlanetState` has none) |
| Signals | `pack/data/mars/signals/` is empty; `mars:storm_strength` is only described in ARCHITECTURE 2.5 | **Not built.** I found no signal code outside `ParameterId` mentions |
| Sky overlay | ARCHITECTURE 2.5.5 gives the `overlay` field for `mars:storm_strength`; slice 6 code accepts only pressure inputs | Not built |
| Forecasts | `pack/data/mars/learning/forecasts/` is empty; F1 text is final-draft in `docs/design/messages/phase-1.md` section 5; `JournalEntry` already has a `reveals` list | Content exists, no engine |
| Learning log | `mars.learning.ActionEvent` (Codex) | See section 6 |

### 1.2 Design point that changes the plan: the per-panel progress lives in the planet sim, not in a block entity

ARCHITECTURE 2.17 says each panel's block entity holds its fractional progress. That is now the wrong place:

- `SolarPanelBlock` has **no block entity** (like the Outgasser), and efficiency is already a per-position map in `PlanetSim`.
- The owner-accepted rule (BACKLOG #32) is that machines contribute with their chunk unloaded. A block entity does not tick in an unloaded chunk; the sim does.

**Decision (Technical Lead, fluid):** `PlanetSim` owns a map `packed pos -> PanelDust(progress, partlyCleaned)`. The block state is a **view** the server writes when the stage changes and the chunk is loaded; a panel in an unloaded chunk gets its state written when the chunk loads (`ChunkEvent.Load`, one map lookup by chunk). The grid reads the sim, never the block. This will replace the sentence in 2.17 (I edit ARCHITECTURE when A ships, not now). Revisit if: scanning panels per chunk on load shows up in a profile (it is a few lookups).

### 1.3 What slice 5 needs (the full list, then split into A to G)

1. Time-based accumulation per panel: `progress += dtSols / intervalSols(phaseCap, pace.rateMultiplier)`; stage = floor, clamped at 4. Interval is 3 sols in Phase 1 standard, 0.75 sol in classroom. Per-panel state saved.
2. Panel brush (item, use on a panel or a canopy cell), 64 uses, back to stage 0; `panel.cleaned` log event. Dust devil roll.
3. Dust cause line on the Monitor ("Panel dust -n %"), the reveal moment, the height budget (section 5.2).
4. Storm system (pure scheduler in `mars.hazards.storm`): warning at least 1 sol ahead, active (envelope in 0.05 sol, out 0.075 sol), ending, a deposit of panel dust at the end, strength cap per phase, a solar multiplier into the grid, a published `mars:storm_strength` signal.
5. Storm look: sky and fog through the keyframe `overlay`, a pre-storm haze, a HUD storm icon and the warning line.
6. Forecast system: data-driven definitions, per-player answers with the answer held server-side, a lock point, `resolve_source`, the CR-F9 and CR-F10 tests, the F1 card and result, and the learning events.

## 2. Ordered sub-steps (each independently testable and shippable)

Each step ends with the Stage 2 standard: unit tests, `runGameTestServer` `RESULT PASS`, a prove-it-fails run, a report line with elapsed time and Gradle runs. "Shippable" means the jar is playable and nothing is half on: a later step's data is simply absent.

| Step | What it delivers | Depends on | Tests that prove it | Owner-visible after it |
|---|---|---|---|---|
| **A. Dust over time** | `PanelDustModel` (pure, `mars.hazards.dust`): accumulate, stage, partly-cleaned flag, deposit(stages), clean(). `PlanetSim` panel registry (pos to progress) replacing the single `setEfficiency` call path for panels; the stage-4 clamp; the world cap per phase from a small data file (`dust_storm.json` cap table, shared with D); `PlanetState` **schema 8** (`dust`, plus `planetSeed`); `ChunkEvent.Load` sync of block states; `/setblock` of a stage sets the sim progress so GameTests still work; `/mars dust get\|set <stage>` for operators | none | Unit: stage timing equals the balance sheet (3 sols standard, 0.75 classroom, 3.75 in Phase 2), clamp at 4, paused 100,000 ticks changes nothing (I-3), save then load gives the same stage, migration from schema 7 (1.4), per-step cost with 300 panels. GameTest: panel with its chunk unloaded still dusts; stage change keeps the 8 canopy cells (`footprint.dust_change_keeps_fillers` stays green) | Panels darken by themselves; Monitor power drifts down |
| **B. Brush and dust devil** | Item `mars:panel_brush` (durability 64, tooltip, recipe, loot), use on the centre block or any canopy cell (`SolarPanelCanopyBlock.centreOf`); `PanelDustModel.devilRoll(seed, chunk, solIndex)`; `panel.cleaned` event post (`PanelCleanedEvent(player, stageBefore, stormWarned)`) | A | Unit: brush returns stage 0 and clears the flag, devil only at stage 2 or more and only to partly cleaned, devil rolls equal across save and load (seeded by `hash(planetSeed, chunkPos, solIndex)`), 1 % over many seeds within bounds. GameTest: use on a canopy cell cleans the centre | A chore with a tool |
| **C. Dust cause line** | `Panel dust -n %` from a running sum kept in `refreshPower` (zero extra cost); replaces the current "dusty" count line (same line, so no new height); first-reveal event `monitor.line_revealed`; the `reveals` field stays empty (the line does not name any forecast answer) | A | Unit: percent equals 1 minus average factor at stages 0 to 4 and partly cleaned; `MonitorLayoutTest` fixtures for the budget in 5.2. Screenshot: Monitor with the line at stage 3 | B1 cause line |
| **D. Storm system (server side)** | `StormScheduler` and `StormTuning` (pure, `mars.hazards.storm`), states `IDLE, WARNED, ACTIVE, ENDING`; `data/mars/hazards/dust_storm.json`; signal `mars:storm_strength` (new minimal signal holder in `PlanetSim`); solar multiplier `1 - 0.6 x strength` applied in `refreshPower` for panels only; deposit at the end through A's `deposit`; `StormEvent` posted on the bus (`SCHEDULED, WARNED, ONSET, CLEARING, ENDED`); attributed sources `mars:storm_solar_cut` and `mars:panel_dust_loss` readable from the sim (needed by F); `PlanetState` storm block; warning chat line `log.storm.warn` and a Monitor "Storm forecast" row; `/mars storm schedule\|end\|get` for operators; `advance` marks events as fast-forward (section 5.5) | A | Unit: warning lead at least 1.0 sol in every phase, envelope shape and end value, cap per phase (1.0, 0.8, 0.5, 0.25), storm cut 24 % at cap 1.0 and 15 % at 0.25 after the formula, deposit +1 or +2 only after the end, scheduling identical with and without a save in the middle, pause-safe (100,000 paused ticks), storm never starts inside the warning lead. GameTest: warning then solar drop; panels keep their stage until the end | Storm cuts power; no sky change yet |
| **E. Storm look and HUD** | Payload `mars:storm` (strength float, state byte, ticks to onset int) sent on change; client `overlay` in the keyframe loader (extends slice 6, `mix: toward`, `max_mix 0.85`, targets from keyframes 3.7); pre-storm haze (fog end -10 % over 0.25 sol, proposed by the Artist); HUD storm icon with countdown (shape, not colour only); warning tone and subtitle hooks (audio files are the Artist's) | D | Unit: overlay mix maths, payload round trip, no client class loads on a dedicated server (existing boot guard). Real-client screenshots at strength 0, 0.5, 1 with measured pixels, as in slice 6 | The storm is seen |
| **F. Forecast engine** | Pure package `mars.learning.forecast` (Codex scope, see section 8): `ForecastDef` (JSON), `ForecastState` per planet, `PlayerForecast` per player, lock, `resolve_source`, results `correct, incorrect, late, expired, void, replay` (2.6.1); card view with **no answer field**; CR-F9 and CR-F10 tests; a test fixture forecast, not F1 content | D for the source ids; can start in parallel against fixtures | Section 4 | Nothing visible |
| **G. F1 content and delivery** | `forecasts/f1_storm_power.json` from phase-1.md section 5; the forecast card screen (client) and its delivery at a natural pause; "Look first" rule on Monitor open; result card first in the queue; "What caused it?" prompt after a power fall; learning events (section 6); journal line `journal.f1.real_vs_game` after resolution; Hana's tip on the first `power_shortfall` | D, E, F, Codex events | GameTest: warning, card, answer, lock, onset, result for two fake players with different answers; the Lens A walk-through by the owner | The first forecast plays end to end |

**Optional H (cut-list candidate, not counted in the totals):** the local-dust rate x2 (digging, machine exhaust) from balance sheet 2a. It needs a per-chunk "dusty until tick" stamp set by block-break events and machine exhaust (ARCHITECTURE 2.17 gave 2 days). It carries goal B5 ("your own digging dusts your panels"). If it is cut, B5 loses its only in-game evidence in Phase 1; the Game Designer decides. **Wiper:** the plan row lists "brush and wiper", but the wiper is a Phase 2 block (ARCHITECTURE 2.17, balance sheet 2a: "from Phase 2"), and Stage 3 builds Phase 1 only. I treat the wiper as **out of slice 5** and ask the Producer to confirm (handoff 1).

### 1.4 Migration

`PlanetState` goes from schema 7 to 8 (`CURRENT_SCHEMA`). It already has about ten constructor overloads, one per past schema; I will add one `withDust` style step instead of another overload chain. Loading rules:

| Saved schema | Dust | Storm | Seed |
|---|---|---|---|
| 8 | as saved | as saved | as saved |
| 7 or older | For each registered `mars:solar_panel` position, find the stage and flag whose output equals the saved `efficiency` (exact match over the 5 stages x 2 flags, tolerance 1e-9); no match, stage 0. Progress is the stage with zero fraction | Scheduler starts `IDLE` and schedules the first storm from the loaded sim time. **No storm is ever created in the past** | Derived once from the dimension's level seed and saved |

A newer schema than the build knows is still refused (existing rule). Test: a saved schema 7 fixture from the Stage 3 slice 2 jar loads, keeps its efficiencies, and a rollback checkpoint taken before the bump restores. Checkpoints (`PlanetCheckpoint`) store `PlanetState`, so dust and storm state roll back with it; forecast answers live in the learning record, which rollback never touches (2.6.2 `replay`), so a rolled-back storm re-issues F1 as `replay` and is not scored twice.

## 3. Storm scheduler design (pure Java)

- **Clock:** every time value is a `long` sim tick from `PlanetSim.model.simTicks()`. No `System.currentTimeMillis`, no `Instant`, no `level.getGameTime()`, no unseeded `Random`. The scheduler is stepped from the same place as the Index, so it stops when the session is paused (I-3) and a stopped server adds nothing.
- **Randomness:** `SplittableRandom(hash(planetSeed, stormIndex))` draws the gap to the next onset and the length; nothing else in the planet draws from it. `planetSeed` is saved in `PlanetState` (schema 8), so a save and load gives the same storms.
- **Schedule ahead:** the next storm (onset tick, length) is decided when the previous one ends (or at first load), so the warning at `onset - 1.0 sol` is a fact about the future the scheduler already holds. Gap minimum is at least the warning lead plus the length of the previous storm's ramp-out, so a warning is never skipped.
- **States:** `IDLE` (next storm decided) -> `WARNED` (at `onset - lead`; haze channel rises, strength 0) -> `ACTIVE` (envelope in over 0.05 sol, hold, out over 0.075 sol, from ARCHITECTURE 2.5) -> `ENDING` (the tick the ramp-out reaches 0) -> deposit once -> `IDLE`.
- **Strength:** `strength = envelope(t) x intensity x cap(phase)`; `cap` comes from latched gates in `dust_storm.json` (Phase 1 1.0, Phase 2 0.8, Phase 3 0.5, Equilibrium 0.25). Only `phase_2_begins` and `dome_stable` exist as gates today; later phases need their gate IDs, so the table falls back to the highest latched entry and a test asserts that.
- **Power:** `solarFactor = 1 - sunlight_cut_per_storm_cap x strength` (0.6 from `panel_dust.json`, so 24 % cut at cap 1.0, 15 % at 0.25). Applied to the panel part of supply in `refreshPower`, called once per step while a storm is not `IDLE` (about 240 steps per sol, each one loop over the machine registry: tens of microseconds). A storm never touches the dust stage until `ENDING`.
- **Lock-point guarantee:** the order inside a step is fixed: (1) emit `ONSET` and let listeners lock, (2) only then apply `envelope(tick - onset)`. `envelope(0) = 0`, so no supply change happens in the step that emits `ONSET`. A unit test asserts `lockTick < firstTickWithSupplyBelowClear` over 1,000 seeds. The warning never changes supply, and the pre-storm haze is fog only (prototype step 1).
- **Per-phase count cap:** a storm count per phase from data (Game Designer supplies the number, input 9.1); `strength` cap and count cap are different things and both are in the file.
- **Cost:** per tick zero extra work (the Index step already runs every 100 ticks); per step one comparison while `IDLE`, a few multiplications while active.

## 4. Forecast system design

**Place.** `mars.learning.forecast` (pure, ArchUnit-checked: no Minecraft imports). It reads the world only through a small interface in `mars.core`:

```
interface AttributedSources { double current(String sourceId); double peakSince(String sourceId, long tick); }
```

`PlanetSim` implements it (D adds `mars:storm_solar_cut` = supply the storm removed, `mars:panel_dust_loss`). Modules talk only through `core` and the NeoForge bus, as in ARCHITECTURE 3.

**Data** (`pack/data/mars/learning/forecasts/*.json`, one file per forecast):

| Field | Meaning | F1 value (from phase-1.md section 5) |
|---|---|---|
| `id`, `goal_ids`, `band_text` keys | Identity and wording keys (text stays in `lang`) | `mars:f1_storm_power`, B3 and C1 |
| `issue` | Event that issues it | `StormEvent.WARNED` |
| `lock` | Event or condition that closes it | `StormEvent.ONSET` |
| `outcome_visible` | When the world shows the answer | first tick `mars:storm_solar_cut` > 0 |
| `choices` | Ordered keys with **no correctness flag** | drops, rises, stays the same |
| `resolve` | `{ "resolve_source": "mars:storm_solar_cut", "rule": ">", "value": 0, "correct": "drops" }` | the storm's own cut, not net power |
| `late_window_sols`, `timeout_sols` | 2.6.1 | 0.05 (config), from Game Designer |
| `interleaved`, `source_goal`, `check_pointer_key`, `why` (HS) | 2.6.2 | `check.ms` pointer, HS why |

**Where the answer lives.** The `correct` choice is read by the server **when it resolves**, from the source value, and by nothing else. The card sent to the client is a record `ForecastCard(id, promptKey, choiceKeys, checkKey?)` with no answer, no result and no group counts; a test asserts by reflection that no payload record has a field named `correct`, `key`, `answer` or `result` before resolution. **Honest limit:** the pack ships inside the mod jar and the world folder, so a determined player can read the JSON. This design keeps the answer out of the **network and the UI**; it is not protection against opening the files, and no report may claim it is.

**Per player and per group.** The outcome is one fact per planet; answers are per player. No screen shows a teammate's choice or "3 of 4 answered" before the lock (that would be a social leak); the Record shows own evidence only (8.5). Every (player, forecast) pair ends in exactly one `prediction.resolved` (2.6.1). A player who joins after the lock gets no card and no events (absence is not "expired").

**Lock point.** `lock` fires once per planet; at that tick every unanswered player's forecast closes as `expired` after the late window. A message that `reveals` the forecast is held by the interruption gate until the player answered or the lock fired (2.6.2). The Monitor's "forecast card first" rule applies only to forecasts whose lock is `monitor_opened`; F1's lock is the storm onset, so for F1 the card is delivered at the next natural pause and Monitor opening is not blocked.

**`resolve_source` and the CR-F10 test.** F1 resolves on `mars:storm_solar_cut`, not on net power. Tests (spec drafted by Codex in `docs/tech/forecast-test-spec.md`, CX-A, not yet written):

| Test | Fixture | Must fail when |
|---|---|---|
| CR-F10 positive | Sim with a storm; a scripted player brushes four stage-4 panels mid-storm so **net** power rises above its pre-storm value | The key is read from net power (answer would flip to "rises") |
| CR-F10 lint | Every forecast JSON has a `resolve_source` that exists in `AttributedSources` ids; none names a net or total value | A forecast resolved on `mars:power_net` |
| CR-F9 lock | 1,000 seeds: no tick between `issue` and `lock` at which supply, the Monitor power row, or any `reveals`-less message differs from clear-sky | Warning or deposit changes supply before the lock; a message whose text contains the F1 answer is delivered while F1 is open |
| Exactly-one | Two players, one answers, one does not, one joins late, one rolls back | Any pair with zero or two `prediction.resolved` |
| Fast-forward | `/mars index advance 20` across a storm | F1 scored instead of `void` |
| Leak | Reflection test on payload records | Any answer-like field |

## 5. Risks, with the check that catches each (PROCESS.md risk gate)

### 5.1 Table

| # | Change | Could break | Check | Result |
|---|---|---|---|---|
| 1 | Dust moves from block state to sim progress | Existing GameTests that `/setblock` a stage; the efficiency numbers in `PowerSimTest` | `/setblock` path sets sim progress; the existing tests run unchanged before any new code | not run |
| 2 | `PlanetState` schema 8 | Old worlds, checkpoints, `PlanetGameTests` fixtures | Schema 7 fixture load test, newer-schema refusal test, checkpoint round trip | not run |
| 3 | Storm in `refreshPower` | The 4 slice 3 worked examples, Dome stable timing (balance sheet 2d.7) | Unit tests with strength 0 equal today's numbers to 1e-9; 2d.7 timing test unchanged | not run |
| 4 | Storm scheduling | Determinism and pause safety (I-3) | Same storms across save and load and across `advance` step sizes; 100,000 paused ticks change nothing; ArchUnit rule: no wall-clock or unseeded random in `mars.hazards` and `mars.learning.forecast` | not run |
| 5 | A storm in the first sols of a new world | Landing (22 to 30 min), Dome stable (about 9 sols) and the 3-sol hold; a storm during the hold could reset it | Unit test with a storm placed in the Dome stable hold; balance sheet 2d.7 timing with and without; ask the Game Designer for the first-storm sol (9.1) | not run |
| 6 | Brush availability | Panels reach stage 4 (60 %) in 9 sols from the starting stage 1 and the player has no way to clean them before Dome stable | Check the grid numbers against 2d.7 at stage 4; the Game Designer decides a brush in the Ops locker or recipe cost | open |
| 7 | Block-state sync on chunk load | Chunk-load cost; a panel never updated | GameTest with a forced unload and reload; timing with 300 panels | not run |
| 8 | F1 answer leak (CR-F9) | A cue (haze, HUD icon text, Monitor row, journal copy) names the answer | Section 4 lock test; the HUD icon and "Storm forecast" row text reviewed against the F1 answer by the Instructional Designer; journal copies count (rule added 2026-11-28) | not run |
| 9 | Admin `advance` or rollback across a storm | Forecasts scored for time that never passed; log events from fast-forward | Fast-forward test (section 4); events carry `Cause.ADMIN` | not run |
| 10 | Client sync | Packet spam, dedicated-server crash from a client class | Payload only on change; boot guard with a lambda registration; cost line 5.4 | not run |
| 11 | Event kinds Codex has not built | Slice 5 ships without evidence for B3, C1, C2 | Section 6; step G is not "done" until the events appear in a real exported CSV | open |

### 5.2 Monitor height budget

Facts from the code: `TARGET_H` 130 GUI px, `PLOT_OVERFLOW` 20, so the plot is dropped when the fixed rows exceed about **150 - 27 = 123** px (`PLOT_MIN_H` 16 + label 9 + 2). ARCHITECTURE 2.20 measured the normal state at about 149 px with the plot and the busiest state at about 156 without it (the Monitor panel fits a 270 px screen; GUI scale 4 is not verified). Lines are 10 px (`LINE_H`), rows 11 to 12, a goal detail line 9, the goal bar 4.

| Situation | Added height | Dust-line cost |
|---|---|---|
| Dust only | **0**: "Panel dust -n %" replaces the existing `monitor.cause.dusty` line | none |
| Storm warned | One "Storm forecast" row, one line (about 12 px, no detail line) | none |
| Storm active | The row stays; one cause line "Storm: less sunlight -n %" (10 px) | none |
| Storm active, dusty, stalled machine, waiting line | up to +22 px over today's busiest state | the plot is already gone in that state |

**What drops, in order (extends `MonitorLayout.fit`):** the plot (it already goes first when the rows exceed the cap), then the Outside air line, then the goal's detail line, then "Dome sealed", then (new) the **warning-state** "Storm forecast" row, which is information, not a problem. Never dropped: an **active** storm cause line, the dust line, a stalled-machine line, "Waiting", a leaking dome. Check: new `MonitorLayoutTest` fixtures for storm warned, storm active, and the worst combination, each asserting `panelHeight <= 240 - 12` (GUI scale 3) and that no never-drop line is missing; then a real-client screenshot. If the worst case exceeds the screen, the fallback is to move the storm row into the header strip (no new height), a Game Designer and Artist call.

### 5.3 Schema and migration

Section 1.4. The risk that matters is the constructor-overload chain in `PlanetState`; slice 5 should not add an eleventh overload.

### 5.4 Per-tick and sync costs (stated, not measured)

| Item | Cost |
|---|---|
| Dust accumulation | One add and compare per panel per 5 s step; 300 panels is microseconds. No block entity, no ticking |
| Block-state writes | One per panel per stage (every 3 sols per panel; at storm end all panels at once: N block updates and N chunk-section remeshes in one tick; 50 panels over a few chunks is fine, hundreds in one chunk is unmeasured) |
| Storm | Zero while idle; a recount of supply per step while active (about 480 steps per storm at 1 sol warning and 1 sol length; haze needs no recount) |
| Packets | `mars:storm` about 12 bytes per player per step while warned or active (about 480 packets per storm, each under 20 bytes with headers: unmeasured); plus once on join. Dust: vanilla block updates only. Forecast card: one payload of about 100 bytes per issue per player. Monitor snapshot: no new packet, the new lines are in the existing one |
| Chunk load | One lookup of panels by chunk (a map from chunk to positions kept next to the registry) |

### 5.5 Other risks

- **`/mars index advance` across storms** posts events that never happened in play. D flags a fast-forward span so forecasts resolve `void` and log events carry `Cause.ADMIN` (risk 9).
- **Resolving on a number the player can also change** (CR-F10): the storm cut is attributed in the sim, so brushing, batteries and added panels cannot change it; a test proves it (section 4).
- **A forecast card UI is a new client screen.** Registered by lambda (AGENTS.md rule 9); a dedicated-server boot check stays in the gate.
- **Rollback and replay:** a rolled-back planet can issue F1 again; `replay` is not scored (2.6.2).

## 6. Learning events: what Codex's `ActionEvent` has and what is missing

`mars.learning.ActionEvent` today (read-only for me): kinds `MACHINE_BUILT` (`machine.built`), `MACHINE_REMOVED`, `MONITOR_VIEWED` (`monitor.viewed`, page), `GATE_REACHED` (`gate.reached`), `SEAL_BREACHED` (`failure.reported`, planet scope), and, added by Codex on 2026-11-28 (after I first read the file), the five Landing kinds (`landing.step`, `landing.skipped`, `landing.done`, `briefing.opened`, `journal.opened`). Fields: timestamp, world, planet, session, timeline, simulation tick, sol, optional player pseudonym, band (MS or HS), kind, cause (PLAYER, SIMULATION, ADMIN, DEBUG), and one `subject` string that must match `mars:[a-z0-9_]+`. **There is no data map.** The subject pattern is now `mars:[a-z0-9_./-]+`. The type rejects an actor on planet-scope kinds and requires one on player-scope kinds.

| Needed by slice 5 | ARCHITECTURE source | Scope | Exists? |
|---|---|---|---|
| `prediction.issued` | 2.6.1 | player | No |
| `prediction.answered` (choice, HS why, `answer_time_ms`, `scaffold`) | 2.6.1, 2.6.2 | player | No |
| `prediction.outcome_visible` | 2.6.1 | planet | No |
| `prediction.resolved` (result: correct, incorrect, late, expired, void, replay) | 2.6.1, 2.6.2 | player | No |
| `panel.cleaned` (stage before, storm forecast yes or no) | 2.17 | player | No |
| `monitor.line_revealed` (line id) | 2.17 | player | No |
| `failure.reported` with cause `power_shortfall` (and `failure.fixed`) | 2.8 | planet and player | Only the seal-breach kind; the kind is named for one cause |
| `storm.warned`, `storm.onset`, `storm.ended` (planet scope, not in ARCHITECTURE; proposed so a CSV row can say "storm forecast was open") | new | planet | No |
| Landing kinds (`landing.step` and so on) | 2.22 | player | Yes (Codex, 2026-11-28; outside this slice) |

**The gap that matters:** the current record cannot carry a choice, a result, a stage or a flag, because it only has a content-ID subject. Needed from Codex: a typed, whitelisted `data` set per kind (IDs and small numbers only, never free text, so the "no free text" rule holds), and the kinds above. This is a Codex change in `mars/learning/**`. Until it lands, step G can post bus events (`PredictionEvent`, `PanelCleanedEvent`, `StormEvent`) and ship without log evidence, which would leave B3 and C1 unmeasured in the owner's playtest. I will not call G done in that case.

## 7. Re-estimate, in sessions, and what is unknown

**Unit.** One session = one Technical Lead report, as in `stage3-plan.md` section 3. Measured values from the reports I could read:

| Report | Wall time | Gradle runs | Note |
|---|---|---|---|
| Stage 2 slice 5 (frost, Ovens, schema 5) | about 15 min | about 30 | tally, not a log |
| Stage 2 slice 6 (sky keyframes, payload, client, screenshots) | about 35 min | 12 | plus one 96 s client run |
| Stage 2 slice 7 (gate bar) | about 8 min | about 13 | plus four 42 s screenshot runs |
| Stage 3 slice 1 (dome sealing) | about 12 min | about 24 | plus 5 screenshot runs |
| Hull lights | not stated | 5 | small |
| Stage 3 slice 4 (Landing, journal, seal kit, 8 steps, UI) | roughly 1 hour after a usage reset (not measured) | about 45 | plus 6 screenshot runs of about 50 s |

So a thin slice cost 10 to 35 minutes and 12 to 30 runs; the biggest one so far cost about an hour and 45 runs. Slice 4 is my only sample with a client screen and many hooks.

| Step | Analogy | My estimate (sessions) | Wall time at the measured rate | Gradle runs |
|---|---|---|---|---|
| A | Stage 2 slice 5 (sim state, schema, Monitor) | 1.0 | 20 to 40 min | 25 to 40 |
| B | Slice 4 seal kit (item, use, event) | 0.5 | 10 to 25 min | 15 to 25 |
| C | Stage 2 slice 7 | 0.4 | 8 to 20 min | 12 to 20 |
| D | Slice 5 plus 6 mix: pure state machine, signal, schema, GameTest | 1.3 | 30 to 50 min | 30 to 50 |
| E | Stage 2 slice 6 (keyframes, payload, screenshots) | 1.0 | 35 to 50 min | 15 to 25 |
| F | Slice 4 pure packages (Codex may take it) | 1.0 | 30 to 45 min | 20 to 35 |
| G | Slice 4 UI and hooks | 1.5 | 45 to 75 min | 35 to 55 |
| **Total** | | **about 6.5 sessions (range 5 to 9)** | **about 3 to 5.5 hours of agent time** | **about 150 to 250** |

This sits inside the stage-3-plan range (6 to 10). It is **not** a dev-day number and does not replace the 11.5 to 12.5 dev-days human-equivalent in ARCHITECTURE 2.17 (that figure covered only panel dust; my A to C come to about 1.9 sessions). With step F taken by Codex, the Technical Lead's own load is about 5.5 sessions; the owner calendar is the other driver (every client-visible step wants a real-client screenshot and an owner look).

**What I do not know (so the range is wide):**

1. **Inputs.** Section 9 lists them; none of the Game Designer's or SME's numbers existed when I wrote this. D, E and G cannot be finished without them.
2. **Step E and G are my lowest-confidence estimates.** I have one sample of a client screen (slice 4) and one of a sky change (slice 6). A forecast card screen plus delivery at a natural pause has no precedent in the code; I did not find a "natural pause" mechanism in `mars.story` and did not read it in depth.
3. **Block-state sync on chunk load** may need more than a map lookup (chunk loading order, canopy cells). Not tried.
4. **Codex turnaround** for event kinds and the typed `data` set is not controlled by me.
5. **Playtest findings.** The paper prototype is design judgment, not evidence. A storm that cuts life support during the Dome stable hold, or panels at stage 4 before a brush exists (risk 6), may force a balance round that is outside the sessions above.
6. **The Gradle-run counts are my own tallies** in the earlier reports, not logs.

## 8. Who takes what

| Part | Owner | File scope |
|---|---|---|
| A, B, C, D, E | Technical Lead | `mars/hazards/**`, `mars/index/sim/PlanetSim*`, `PlanetState*`, `mars/machines/SolarPanel*`, new item registration, `MonitorView`, `mars/client/Monitor*`, sky client files, `pack/data/mars/hazards/**`, `pack/assets/mars/keyframes/**`, `mod/src/test/**` for those packages |
| **F** forecast engine (pure) | **Codex, if the owner wants a second worker** (it is well specified in section 4 and has no Minecraft code) | `mod/src/main/java/mars/learning/forecast/**`, matching tests, `pack/data/mars/learning/forecasts/` schema docs; Technical Lead supplies `AttributedSources` in `mars.core` first |
| New `ActionEvent` kinds and the typed `data` set (section 6), subscribers for `StormEvent`, `PanelCleanedEvent`, `PredictionEvent`, the CSV columns | Codex | `mars/learning/**` |
| CX-A forecast and CR-F10 test spec | Codex | `docs/tech/forecast-test-spec.md` (new); the fixtures in section 4 are the starting list |
| Balance-sheet vs data test for the new `dust_storm.json` | Codex | new test file under `mod/src/test/java/`, once the Game Designer's numbers exist |
| Screenshot scripts for dust stages, a storm at 0, 0.5, 1, and the Monitor in a storm | Codex | `tools/screenshots/*.shots` (absolute paths) |
| Docs upkeep: launch guide, BACKLOG, HANDOFF | Codex | as in AGENTS.md |
| G (card screen, delivery, F1 content wiring) | Technical Lead | after F and the events exist |

**Note on a conflict in the docs:** `stage3-plan.md` section 6 says "not for Codex: `mars/learning`", while AGENTS.md and HANDOFF say Codex owns `mars/learning/**` (slice 3 was built there). I follow AGENTS.md and HANDOFF (more recent). The Producer should correct section 6.

## 9. Inputs I need

### 9.1 From the Game Designer (`docs/design/f1-storm-spec.md`, not yet present)

| # | Input | Why | Today's state |
|---|---|---|---|
| G1 | **First storm: sol of onset** (or a range). Three documents disagree: balance sheet section 2 curve puts a 2-sol storm on sols 14 to 15; the story order (phase-1.md line 7) puts the first storm **before** Dome stable (about sol 9.2 solo); the prototype uses a short storm | Sets the schedule and whether a storm can hit the Dome stable hold | Missing |
| G2 | Storm length (the beat says 0.5 to 1 sol; the curve says 2 sols), gap between storms, and the **number of storms per phase** (a count cap, distinct from the strength cap) | Scheduler data | Missing |
| G3 | Whether warning lead, length and gap scale in classroom mode (a sol is 20 minutes either way; the balance sheet scales dust rate only) | One data field decides it | Missing |
| G4 | Brush: where the first one comes from (Ops locker, recipe cost for the 9-item grid, or both), and whether a starting panel at stage 1 is intended (balance sheet 2d.2 says yes; that **starts the dust line already visible**, so the DESIGN rule "the line appears the first time a panel reaches stage 1" cannot fire. Recommend the reveal keys on the first stage **rise**) | Risk 6 and step C | Open |
| G5 | Dust at storm end: one deposit at the end of the ramp-out (my default; DESIGN says "when the storm ends") or stepped over the ramp-out (keyframes 3.7a step 7). Both are after the lock, so F1 is safe either way | Step D | Conflict between DESIGN and keyframes |
| G6 | Storm row wording keys (`monitor.row.storm`, `monitor.cause.storm`, "Storm forecast" value), HUD icon countdown text, `log.storm.warn` already in beat 4 | Step D, E | Beat 4 text exists; Monitor keys missing |
| G7 | Hana's tip trigger text for the first `power_shortfall` | Step G | Mentioned in phase-1.md, text unverified |
| G8 | Whether the Shift Board slip appears for a near-miss (prototype open question) | Step G | Open |
| G9 | Forecast timing numbers: late window (2.6.1 default 0.05 sol), `timeout_sols`, interval between the card and the lock for a player who is slow | Step F data | Defaults only |

### 9.2 From the SME (`docs/science/storm-dust-review.md`, not yet present)

| # | Input | Why |
|---|---|---|
| S1 | Check of the F1 result texts (MS, HS) and the "thin air cannot push panels" claim, marked verified or not (phase-1.md marks `[SME: verify wording]`) | Step G wording |
| S2 | Storm length and warning lead as claims: real storms last days to months, the game compresses; what can be said in the journal about it (`journal.dust.storm_timing`, "can days ship without a number?") | Journal and F1 read-more |
| S3 | The storm cut (0.6 x cap), the cap per phase, and deposit at the end: sourced or invented? (the balance sheet tags them invented; Opportunity -80 % and -97 % are cited) | Tags in `dust_storm.json` |
| S4 | **F1 distractors:** F1 has "It drops, It rises, It stays the same" and HS "why" options (dust blocks sunlight, wind turns the panels, the Sun gives off less light). Any change to wrong answers, and which "why" option is the intended misconception, needs the SME and the Instructional Designer | Step G; stage-3 plan input I3 (external teacher review still open) |
| S5 | Whether the pre-storm haze (fog -10 %) and "dust drifts sideways" can be shown without implying wind force on panels | Step E look |

### 9.3 From others (smaller)

- **Artist:** storm overlay colours (keyframes 3.7: `sky_zenith` #9A6A48 is the target in ARCHITECTURE 2.5.5, others unspecified), HUD storm icon shape, haze channel confirmation, brush item model and texture.
- **Instructional Designer:** the "What caused it?" prompt wording and trigger, the `guidance` rule for it, the check that the "Storm forecast" Monitor row and the HUD icon reveal nothing (CR-F9, CR-T8).
- **Producer:** the wiper question (handoff 1) and the section 6 correction.

## 10. Ways this plan could be wrong

- I read the code listed above, not all of `mars.story`; a natural-pause delivery or a `reveals` message queue may exist and make G smaller (or reveal that it does not and make it bigger).
- I assumed `PlanetSim` is the right home for per-panel dust. Real multiplayer with thousands of panels is untested; the classroom limit is small.
- The Monitor numbers (149 normal, 156 busiest, cap 150, drop order) are from ARCHITECTURE 2.20 and the layout constants, not from a fresh run.
- Storm numbers are all from the paper prototype and the balance sheet, which are invented, and `dust_storm.json` does not exist yet.
