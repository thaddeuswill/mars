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
