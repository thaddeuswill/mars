# Technical Lead: slice 5 plan (dust, storms, first forecast), docs only

**Date:** 2026-11-28 · **Deliverable:** `docs/tech/slice5-plan.md` · No code, no pack change, nothing built or run (docs task).

## 0. Handoffs received
Task from the coordinator (T1 re-cut plus plan). Accepted in full. The Game Designer's `docs/design/f1-storm-spec.md` and the SME's `docs/science/storm-dust-review.md` did not exist when I finished (checked twice); their inputs are listed, not assumed.

## 1. Done
- `docs/tech/slice5-plan.md`: T1 re-cut (what exists vs. missing), sub-steps A to G, storm scheduler and forecast designs, risk table with checks, Monitor height budget, migration, determinism, sync cost, re-estimate, owners and exact inputs.
- Findings that change earlier docs: (a) per-panel dust progress should live in `PlanetSim` (a map by position), not a block entity as ARCHITECTURE 2.17 says (no block entity exists; chunk-unload rule); (b) signals (`mars:storm_strength`) are not in code yet; (c) `PlanetState` has no planet seed, needed for determinism (schema 8); (d) `ActionEvent` has no data map, so choices, results and stages cannot be logged.

## 2. Decisions needed
1. (well-structured, Producer) Wiper is a Phase 2 block; I treat it as out of slice 5. Recommend confirming.
2. (ill-structured, Game Designer, then owner) First storm timing: balance sheet curve (sols 14 to 15, 2 sols), story order (before Dome stable, about sol 9), beat 4 (0.5 to 1 sol). Needs the Game Designer's spec.
3. (ill-structured) Starter panel at stage 1 makes the dust line visible from the start, so the "first time a panel reaches stage 1" reveal never fires. Recommend revealing on the first stage rise.
4. (well-structured, owner) Whether Codex takes step F (pure forecast engine) and the event kinds in `mars/learning/**`. Recommend yes.

## 3. Handoffs
> **To:** Codex · **Problem type:** well-structured
> **Problem:** `ActionEvent` cannot carry what B3, C1 and the dust lines need.
> **Context:** `mod/src/main/java/mars/learning/ActionEvent.java`: only a content-ID `subject`. `docs/tech/slice5-plan.md` section 6 lists kinds (prediction.issued/answered/outcome_visible/resolved, panel.cleaned, monitor.line_revealed, failure.reported for power_shortfall, storm.warned/onset/ended).
> **Needed from them:** a typed whitelisted `data` set per kind (IDs and small numbers, no free text); those kinds; then CX-A `docs/tech/forecast-test-spec.md` using the fixtures in plan section 4; optionally step F under `mars/learning/forecast/**`.
> **Done when:** a fixture forecast produces one `prediction.resolved` per player in an exported CSV.

> **To:** Game Designer · **Problem type:** ill-structured · **Needed:** inputs G1 to G9 in plan section 9.1 (first-storm sol, length, gap, count cap per phase, classroom scaling, brush source, deposit timing, Monitor wording keys, Hana's tip).
> **To:** SME · **Needed:** S1 to S5 in section 9.2 (F1 texts, storm claims, cut/cap tags, F1 distractors, haze wording).
> **To:** Producer · **Needed:** confirm wiper out of slice 5; correct `stage3-plan.md` section 6 ("not for Codex: mars/learning" contradicts AGENTS.md/HANDOFF); stage3-plan row 5 estimate 6 to 10 now re-cut to about 6.5 (range 5 to 9).
> **To:** Artist / Instructional Designer · section 9.3.

## 4. Risks
Eleven rows with checks in plan 5.1 (none run yet). Top three: storm landing inside the Dome stable hold or stage-4 panels before a brush exists (balance, needs G1 and G4); `ActionEvent` gap leaving B3/C1 unmeasured; forecast card UI has no precedent (my least confident estimate). Monitor: dust line costs 0 px (replaces the existing line); a storm adds up to 22 px, so the plot drops first.

## 5. Shared model updates
- Proposed (for ARCHITECTURE when step A ships): dust progress is per-position state in `PlanetSim`, block state is a view.
- Lesson: "session" estimates now have measured anchors (15 to 60 min, 12 to 45 Gradle runs per slice); slice 5 re-estimate about 6.5 sessions (5 to 9), unverified.
- Honest limit to keep in every report: the forecast answer is kept out of the network and UI, not out of the pack files.

Elapsed: about 25 minutes; no Gradle runs (docs only).
