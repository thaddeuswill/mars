# Stage 3 Plan: first slice (dust storm on the power grid)

**Owner:** Producer (proposal) · **Written:** 2026-11-27 · **Status:** PROPOSAL, not yet accepted by the owner · **Stage:** 3 Vertical Slice (ROADMAP.md)
**Inputs:** `docs/design/balance-sheet.md` §2a, §2b, §4 (storm rows), `pack/data/mars/hazards/panel_dust.json`, `docs/design/prototypes/first-dust-storm.md`, `docs/tech/stage2-plan.md`.
All effort numbers are **unverified estimates**. No measured rate exists (R-68). Reports from the next slice on should state elapsed time and build-and-test cycles.

## 1. Why the storm first

| Reason | Evidence |
|---|---|
| It is the missing "pressure to maintain" and regression source in Stage 2 (frost sink is the only one) | ROADMAP Stage 2 rows; balance sheet §4 curve assumes a 2-sol storm on sols 14 to 15 |
| It reuses what exists: grid, battery, panel dust stages, Monitor cause lines, sky keyframes | Slices 3, 5, 6, 7 |
| Numbers are already written and tagged invented; the paper prototype exists | balance sheet §2a; `first-dust-storm.md` |
| It is the base for forecast F1 (B3, C1), the main Stage 3 learning item | ROADMAP Stage 3 learning layer |
| Visible: the planet answers the player (pillars 2 and 5) | DESIGN.md |

## 2. Recommended slice S3-1 (small, verifiable)

**Scope:** a storm that starts and ends on a pure, saved, pausable schedule; it cuts solar output by 60% x storm cap; at its end each panel gains dust stage (+1, or +2 if cap is 0.8 or more); the Monitor says so with a cause line; the sky dims (reuse keyframes, one extra tint). Pace config and classroom switch come first because storm timing scales with `pace.rateMultiplier` and `Pace.classroom()` is always false in this build (`PlanetData` passes `Pace.STANDARD`).

**Out of scope (named):** forecast F1 and its hidden answer (S3-2), automatic dust build-up between storms, wear, dust-off station and cleaning, dust devils, radiation, recipes, sealing, anything with Ovens' recipe, load test.

| # | Task | Owner (role) | Depends on | Done when (acceptance check) | Effort (unverified) |
|---|---|---|---|---|---|
| 1 | Pace and group config: NeoForge server config for `pace.*`, `group.size`, classroom preset; replace `Pace.STANDARD` in `PlanetData` and `AdminCommands`; creative gate bypass becomes a config switch, default on in standard, off in classroom | Technical Lead | none (stage2-plan slice 9) | Unit: `Pace` presets; GameTest reads config and a classroom planet advances 4x; thresholds unchanged (I-1) in both presets; prove-it-can-fail run | 2 dev-days (plan figure) |
| 2 | Storm numbers confirmed: duration (settled per the balance sheet storm schedule table: Phase 1 storm 2 sols standard / 0.5 classroom; hold-event storms 1 sol / 0.25 sol; invented), gap between storms in Phase 1, first storm not before sol 14, storm cap Phase 1 = 1.0; confirm, no conflict left to resolve | Lead Game Designer | none | One row set in balance sheet §2a/§4 tagged invented; plan and curve agree | 0.5 dev-day |
| 3 | Storm facts and journal line: real storm figures (Opportunity -80% 2007 and -97% 2018, InSight about -35%) marked verified or not; "faster than real" note | Subject Matter Expert | none | Source and verified/not-verified mark per figure | 0.5 dev-day |
| 4 | Storm model (pure, no Minecraft): schedule from planet seed, state saved (schema bump, old saves load), paused with the session clock, scaled by pace; data in `pack/data/mars/hazards/dust_storm.json`; Monitor cause line "Dust storm: solar -X%" | Technical Lead | 1, 2 | Unit: schedule is deterministic and identical after save/restore; 100,000 paused ticks change nothing (I-3); content test equals balance sheet; classroom shortens timing only | 3 dev-days |
| 5 | Grid and panel hook: solar output x (1 - 0.6 x cap); stage +1/+2 at storm end through `panel_dust.json`; battery ride-through | Technical Lead | 4 | Unit: output table incl. stage x storm; GameTest: 2-sol storm drains a full battery as in balance sheet §2b ("2.5 sols of one Outgasser" is survivable), Outgasser slows, panel stage rises at end only | 2 dev-days |
| 6 | Sky dim during storm (one keyframe input), `/mars storm start|end|status` debug command | Technical Lead, Lead Artist (tint values) | 4 | Screenshot at storm start and end looked at; command logged with actor | 1.5 dev-days |
| 7 | Monitor and journal wording: storm line, "storm ended, panels dustier" note, grade 6 to 7 check (CR rules) | Lead Instructional Designer with Lead Game Designer | 2, 3 | Keys in lang file; wording test passes; content rules checked | 0.5 dev-day |
| 8 | Owner playtest S1-lite: place panel, battery, Outgasser; run `/mars storm start`; say why the Monitor changed | Owner (playtest), Producer (sheet) | 4 to 7 | Owner answers "what changed and why" for pressure rate and panel; screenshot or notes filed | 1 h owner |
| 9 | Report with real counts; Producer updates budget table with elapsed time and build cycles | Technical Lead, Producer | all | Unit and boot counts quoted from a real run; cycles recorded | n/a |

**Total (unverified):** about 10 to 11 dev-days plus 1 owner hour. Producer view: could be 30% either way; first measured data point for Stage 3.

**Dependencies in one line:** 1 and 2 first (parallel), then 4, then 5 and 6, then 7, then 8.

## 3. Alternative S3-1b (measurement base first)

Pace and group config (task 1), the pseudonymous action log with actor IDs, and the CSV export (ROADMAP Stage 3, all goals). About 6 to 8 dev-days (unverified). Pro: smaller risk, every later slice is measured, matches the "what we want to learn" table, needed for S1/S2 playtests. Con: nothing new for the owner to see; weakest on market appeal (Two Goals); delays the storm loop. Choose it if the owner wants evidence for Stage 4 sizing before more gameplay, or if the Technical Lead says the storm schedule needs the log first.

| | Recommended (storm) | Alternative (log and CSV) |
|---|---|---|
| Instructional value | Medium (cause and effect, C1 base) | Medium (measurement only) |
| Market appeal | High (visible, dramatic) | Low |
| Technical risk | Medium (new saved state, schema bump) | Low |
| Owner can see it | Yes | No |

## 4. Risks for this slice

- Storm numbers are invented; the real-world figures are only sources for a ceiling. Label them in the journal line.
- Storm length is settled (balance sheet storm schedule table, 2026-11-27; all invented): Phase 1 storm 2 sols standard, 0.5 sol classroom; hold-event storms 1 sol, 0.25 sol classroom. The curve and the drift table agree; task 2 only confirms the numbers (R-61 related).
- New saved state: schema bump must keep old saves loading (tested).
- Creative bypass switch changes playtest conditions: say which mode each playtest used (R-63).
- Estimates are guesses; do not shrink Stage 4 from them (R-68).

## 5. After S3-1

S3-2: forecast F1 (hidden answer per player, `resolve_source` JSON and the CR-F10 test, announced at least 1 sol ahead). Then panel dust remainder (build-up, wear, dust-off station), then Landing and the real recipes. Order of the rest is the Technical Lead's and Game Designer's call after S3-1 is measured.
