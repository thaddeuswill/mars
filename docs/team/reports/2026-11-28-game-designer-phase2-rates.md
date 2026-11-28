# Game Designer: Phase 2 machine rates, remaining gates, recipe unlock (2026-11-28)

## Handoffs received
Owner: not a classroom pack, standard pace only: accepted (classroom columns ignored). Technical Lead gaps (processor and emitter change no Index row; no Thaw or Phase 3 gate files; no recipe unlock): accepted, answered below.

## Done
- `docs/design/phase2-machine-rates.md`: rate rows, arithmetic tables, power, cost, tuning levers, gate specs, recipe unlock design, conflicts.
- `docs/design/balance-sheet.md` new section 2d (rate rows only; no other section touched).
- Rates (INVENTED): processor +0.15 kPa/sol, emitter +0.25 C/sol, both 3 units/sol (draws unchanged). Scratch Python model (not committed, not run in the game): 1/2/3 processors and 2 to 6 emitters on a schedule give 30 kPa at Phase 2 sol 44, Thaw sol 40, 0 C sol 52. Phase 3 needs about 10 processors. Power peak 45 units (10 panels) in Phase 2, 54 (12 panels) in Phase 3.
- Gates: Thaw (T >= -20), Hydrosphere (>= 10 %), Phase 3 begins (30 kPa and 0 C), Breathable (7 sols), Equilibrium (14 sols, full band), all in the existing format; no hold on the first three.
- Emitter stays at Phase 2 begins: the art spec's "Thaw gate" means it serves Thaw; a Thaw unlock would deadlock.
- Recipe unlock: gate the crafting station (soil treatment unit at Phase 3 begins, beacon bench at Breathable), reusing the placement check.

## Decisions needed
1. Copper and redstone source (machine-priorities D1/R1); blocks 10 processors in Phase 3. Ill-structured. Recommend one rare conductor ore, SME to name the real mineral.
2. Temperature has no loss term, so the "emitter wear -1 C/sol" drift event cannot work. Ill-structured (SME, Tech Lead). Recommend a small cooling term, set with the SME, before the holds.
3. Beacon bench as a new block (vs a gated catalyst item). Well-structured; recommend the bench.

## Handoffs
> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** build rates, gates, station-gated recipes.
> **Context:** `docs/design/phase2-machine-rates.md` sections 2, 3, 5; optional `onlyBeforeGate` for the processor CO2 contribution (C-5).
> **Needed from them:** the two `contributions` in machine JSON, five gate files, soil unit and bench stations, tests comparing JSON to sheet 2d.
> **Done when:** a survival player in a test world can reach Phase 3 begins using only processors and emitters.

> **To:** Subject Matter Expert · **Problem type:** ill-structured
> **Problem:** temperature loss term (C-2); name a real stand-in for the conductor ore (R1); confirm the Phase 2 air is about 52 % CO2 (C-5).
> **Needed from them:** a defensible small cooling rate and wording.
> **Done when:** the Game Designer can write the drift event number.

> **To:** Lead Artist / Instructional Designer · **Problem type:** well-structured
> **Problem:** reword emitter "unlocked at Thaw" to "serves Thaw"; the beacon bench needs art; Landing wording is unaffected.
> **Done when:** `docs/art/invented-machines-look.md` row 3 matches.

## Risks
Panel count (10 to 12 three-by-three arrays) is a hidden pacing gate; unmeasured. All rates untested by play. R1 blocks Phase 3 crafting. Pressure waits about 8 sols for warmth: confirm it is not boring.

## Shared model updates
- Decision proposal: emitter unlock stays at Phase 2 begins ("serves" Thaw, never "unlocked by").
- Decision proposal: recipes are gated by crafting station, not per recipe.
- Lesson: check that a lever is not locked behind the gate it serves.
