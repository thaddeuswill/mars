# Technical Lead report: slice 9, pace config and classroom mode (BACKLOG #49)

## Handoffs received
Producer, `2026-11-27-producer-stage3-first-slice.md` and `docs/tech/stage3-plan.md` row 1: accepted, with one change the owner approved: data file plus operator command instead of a NeoForge `*.toml` server config.

## Done (all run from `mod/`)
- `pack/data/mars/pace.json`: sol and step ticks, group range 1 to 5, `standard` (x1.0, group 1, bypass on), `classroom` (x4.0, group 4, bypass off). No threshold key.
- `mars.index.sim.PaceConfig` (pure) and `Pace.creativeGateBypass`; `IndexDefinitions.Defs.pace`; `mars.planets.PaceData` (world save data, default standard); `PlanetManager.pace/setPace`; `PlanetData`, `Checkpoints`, `AdminCommands` no longer use `Pace.STANDARD`.
- `/mars pace` (show) and `/mars pace standard|classroom [group_size]` in `IndexCommands` (permission 2, logged). Switching rebuilds each planet from its own saved state: numbers, goals, machines, history kept.
- Creative bypass in `PlanetManager.placementAllowed` follows the pace. `/mars gate latch` unchanged.
- `LearningService.classroom` is now real (reads the world pace).
- Docs: `ARCHITECTURE.md` 2.21, `docs/tech/launch-guide.md` 1c and 1b, BACKLOG #49, stage2-plan row 9.
- Tests: new `PaceConfigTest` (7): file vs balance sheet 5.1 rates; Dome stable hold vs section 4; preset flags (bypass on in standard, off in classroom); thresholds identical across both modes and group sizes 1 to 5 (gate conditions equal, 5 kPa); classroom rate gain = standard x 4 x group scale within 1 % (short 0.05 sol window, because the frost sink makes the curve slightly nonlinear); holds 3 vs 1 sols; bad files refused; no threshold key in the file. New GameTest `paceCommandSwitchesClassroomMode` (9 boot checks): file loaded, default standard, command sets classroom 3, planet numbers kept, creative refused in classroom, guidance sees classroom, NBT round trip of the choice, group 9 refused, back to standard.

## Real results
- `./gradlew test --rerun --offline`: 206 tests, 0 failed (199 before + 7).
- `./gradlew build --offline`: BUILD SUCCESSFUL (spotless clean).
- `./gradlew runGameTestServer --offline`: `MARS-SELFTEST RESULT PASS passed=96 failed=0` (87 before + 9).

## Not run / unverified
- No dedicated-server or real-client run; `/mars pace` was run through the server command dispatcher in the GameTest, not typed in a client. Its chat text was not seen.
- Guidance refusal of expert level in a classroom world was checked only up to `LearningService.classroom(level) == true`; the refusal rule itself is covered by earlier guidance tests, not re-run end to end through the command.
- Prove-it-can-fail run (stage3-plan asks for it) was not done for the new tests.
- Switching pace mid-session on a multi-planet server: rebuild is run for every planet level, tested with one level only.
- Group default 4 and range 1 to 5 are choices, not sourced.
- Only Dome stable has a hold in a gate file; Breathable and Equilibrium holds (2 and 3 sols classroom) are unbuilt.
- `pace.recipePreset` not built (no recipes yet).

## Decisions needed
1. Well-structured. Should `/mars pace` be allowed once play has started? Built: yes, any time, planet numbers kept (rates and holds change from then on). Recommendation: keep, since teachers need to fix a wrong choice; add a pre-change checkpoint if you want a safety net (small, not built).
2. Ill-structured (design). Classroom default group 4 and range 1 to 5 for standard: balance sheet says teacher sets 3 to 5 in classroom. Recommendation: keep; Game Designer and Instructional Designer to confirm.

## Handoffs
> **To:** Lead Instructional Designer / Game Designer · **Problem type:** well-structured
> **Problem:** Educator guide must say classroom mode is chosen with `/mars pace classroom [3-5]`, that it turns off the creative bypass, and that guidance is teacher-only.
> **Context:** `docs/tech/launch-guide.md` 1c; `ARCHITECTURE.md` 2.21.
> **Needed from them:** wording for the educator guide.
> **Done when:** guide text exists and matches the command.

## Risks
- A teacher who forgets to run `/mars pace classroom` plays standard pace silently; the Monitor does not yet show the mode (a one-line row would help; not built).
- Group size is static per world, not per planet: a world with several planets shares one group size (planets are "groups of 3 to 5 per planet"). Per-planet group size is not supported; revisit when two planets exist.

## Shared model updates
- Decision (owner, slice 9): pace is data in `pack/data/mars/pace.json` plus a per-world operator command; no NeoForge server config. Owning doc: `ARCHITECTURE.md` 2.21.
- Decision: creative gate bypass is on in standard, off in classroom.

## Follow-up (owner away; recommendations accepted)
- Automatic checkpoint: `PlanetManager.setPace` calls `Checkpoints.takeAll(server, "before_pace_<HHmmss>", "pace-change")` after validating the group size and before changing anything. Bounded by the existing `CheckpointList` (oldest dropped). Boot check `pace.auto_checkpoint` (list seen: `..., before_pace_192407`).
- Monitor tag: `MonitorView.of` adds row `monitor.row.pace` ("Classroom pace", label only, US spelling, grade 6-7) only when the world is classroom. Lang key added; `LangWordingTest` green. New unit test `MonitorLayoutTest.theClassroomPaceTagAddsOneShortRowOnlyInClassroomAndStaysInBound`: standard has no such row, classroom has exactly one more row, plot 36 (min 16), panel 147 GUI px, within the bound.
- `docs/tech/stage3-plan.md` risk list and task 2 now read storm length as settled per the balance sheet storm schedule table (2 sols / 0.5 sol Phase 1; 1 sol / 0.25 sol hold events; invented).
- Docs: ARCHITECTURE 2.21, launch guide 1c, BACKLOG #49.
- Real results: `./gradlew build --offline` BUILD SUCCESSFUL; 207 unit tests, 0 failed; `runGameTestServer` `RESULT PASS passed=97 failed=0`.
- Screenshot: `tools/screenshots/slice9_classroom.shots` run via absolute path, no other client running; `tools/screenshots/out/20261127-192411/slice9_1_classroom_monitor.png` viewed: "Classroom pace" sits at the bottom line, plot and rows intact, nothing clipped. In that run Dome stable latched in under 1 sol (classroom hold 1 sol at x4 rate with 1.5 sols advanced), as expected.
- Unverified: standard-world screenshot not retaken (no row is added, covered by the unit test); the real chat output of the pre-change checkpoint; multi-planet worlds; whether the tag label should be styled differently (kept plain).

## Follow-up 2: classroom group range 3 to 5 (owner away; recommendation accepted)
- `pack/data/mars/pace.json`: the top-level `group_size_range` is gone; each preset carries its own (standard 1..5, classroom 3..5, default 4). `source` text says the 3 to 5 range is a design choice aligned with the Decision Log group of 3 to 5.
- `PaceConfig.Preset` gained `groupMin`/`groupMax`/`groupAllowed`; `parse` refuses a preset with no range, an inverted or sub-1 range, a missing max, or a default outside its range. `pace()` message: "Group size for classroom must be 3 to 5 (you gave 1). Nothing was changed." The check runs before the checkpoint, so a refusal changes nothing and takes no checkpoint. Standard path unchanged.
- Tests: `PaceConfigTest` asserts both ranges, refusal of classroom 0/1/2 (message names "3 to 5"), standard 1 and 2 still fine, five bad-file cases; group loops now 3..5 for classroom. New boot check `pace.classroom_group_range` (command `classroom 1` and setPace 2: refused, pace unchanged, checkpoint count unchanged).
- Docs: ARCHITECTURE 2.21, launch guide, educator guide 2.4 (group size line only), BACKLOG #49.
- Real results: `./gradlew spotlessApply build runGameTestServer`: BUILD SUCCESSFUL; 207 unit tests, 0 failed (no new test methods; assertions added); `RESULT PASS passed=98 failed=0`.
- Unverified: real chat text of the refusal in a client; educator-guide lines 96-100 and 120 still say "group size" without the range (left as instructed).
