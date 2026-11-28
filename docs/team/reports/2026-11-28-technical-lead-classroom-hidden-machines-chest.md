# Technical Lead: classroom hidden, processor and emitter registered, chest and seed kits (BACKLOG #49)

Date 2026-11-28. Branch `claude/elegant-brown-anu1ci`. Three commits.

## Handoffs received
Owner ("its not actually for a classroom", "hide the classroom stuff"): accepted, job 1. Lead Artist registration lists for the processor and emitter: accepted, job 2. Game Designer `docs/design/seed-kit-recipes.md` changes: accepted, job 3, with gaps below.

## Done
1. **Classroom hidden.** `classroom_enabled` (default false, also false if missing) in `pack/data/mars/pace.json`; `PaceConfig.refusal()` returns "Classroom pace is turned off."; `/mars pace classroom` refuses before any checkpoint or change and is not in tab completion (`requires`); `Pace.classroomShown` hides the Monitor tag; launch guide classroom lines removed; Not-in-use banner on `docs/learning/educator-guide.md` 2.4. Code kept. Other classroom-only doc sections (educator guide section 11, balance-sheet classroom columns) not bannered: not found to be classroom-only by a quick scan, **not checked in full**.
2. **Processor and emitter.** `GatedLoadBlock` (one class), blockstates, item models, loot, lang with the two required tooltips, INVENTED recipes, `machines/*.json` draw 3 units/sol INVENTED, `phase_2_begins.json` `unlocks` lists both (the Outgasser mechanism took block lists cleanly), Monitor Power row lists them via the existing load path.
3. **Chest and kits.** Chest: 4 copper, 10 redstone, 6 wheat seeds, 2 sugar cane, 2 sweet berries, 2 potatoes. Four kit items and shapeless recipes on `#mars:seed_kit_carbonate`; kit tooltips from the Game Designer; item models reuse the carbonate icon as a placeholder; `mars:washed_regolith` tag (empty).

## Real output
`./gradlew spotlessApply build runGameTestServer` from `mod/`, final: BUILD SUCCESSFUL, 298 unit tests, 0 failures (baseline 288); `MARS-SELFTEST RESULT PASS passed=137 failed=0` (baseline 133). New tests: `PaceConfigTest` (+2), `PhaseTwoMachinesTest` (4), `SeedKitContentTest` (4, includes the F8 gas-name guard and a check that the guard catches gas words); boot checks `pace.classroom_off`, `phase2.machines_gated`, `phase2.machines_place_and_draw`, `kits.recipes_and_items` (all four recipes loaded by the real recipe manager); `grant.starter_chest` and `machines.registered` updated. Not run: dedicated server, real crafting at a table, a real placement by a survival player's hand (the check calls `placementAllowed`), `/mars pace classroom` typed in chat (the boot check runs the command through the command dispatcher and sees it refused).

## What I saw (real client, `tools/screenshots/out/20261128-013010/phase2_*.png`, script `tools/screenshots/phase2_machines.shots`, real pad y 81, run made 01:30 to 01:31, no client left running, 0 "Unknown block" lines)
- Processor: gold-wrapped box with a dark grille, a blue lamp, a pink plate, and a grey canister on top. Reads as a gold machine. Pink dashed band visible on the plinth only on the emitter's base; not clearly seen on the processor.
- Emitter: tall dark stack, orange hazard marks low on the stack, gantry posts, tank with a blue lamp, pink band on the plinth. Reads as a vent stack. The flared top is a dark cap.
- Not seen: the processor's two east-face ports (my side shot looked at the emitter's side), the real "hollow square" lamp, dust stages (not drawn). Close-ups are not very close; no model looked broken or magenta. All the shot folders are copies of every earlier shot too (the harness copies the whole set), so this commit is large (79 files).

## Gaps (written, not invented)
- **No balance-sheet rate rows** for the processor or emitter (machine-priorities R5): neither changes any Index row. Pressure 5 to 30 kPa and warming have no lever yet. Needs Game Designer rows (kPa/sol and C/sol per machine, cost, draw) before Phase 2 can be played.
- **Emitter gate:** the art spec says Thaw/Phase 3 begins, the task said Phase 2 begins; both unlocked at Phase 2 begins. No Thaw or Phase 3 gate file exists.
- **No recipe unlock mechanism** (gates unlock machine ids only). Kit recipes are available from the start. A gate-driven recipe unlock (advancement-based or a filter) is a separate slice.
- **Washed regolith item does not exist**: the biocrust recipe loads but cannot be crafted.
- Kits do nothing when placed (W3). Kit icons are a placeholder.
- **Oxygen maker band not done**: the Artist's oxygen maker has no band texture (no oxygen_maker texture files in `pack/assets/mars/textures/block`); the retrofit is the Artist's.
- Shapes: processor and emitter use a 16 px outline and 24 px collision in one cell (the emitter's model is 32 px, so the top 8 px overhang and are visual only). Overhang culling at a chunk edge not tested.
- Test lesson: a GameTest that calls `resetForSelfTest` replaced the planet while the slow Outgasser test (230 ticks) held the old object, so its checks failed. I run my checks inside the reclaimer test. Any new test that resets the planet must not overlap another.

## Decisions needed
1. Emitter at Phase 2 begins or Thaw? Recommend Thaw once a Thaw gate exists; until then Phase 2 begins. Ill-structured (Game Designer, SME).
2. Do you want a recipe unlock mechanism (gate-driven recipes)? Recommend yes, later, with the EMI hide-until-gate slice already queued. Well-structured.
3. Commit the screenshot output folders? Recommend stop committing `tools/screenshots/out/*` copies (large). Well-structured, owner call.

## Handoffs
> **To:** Lead Game Designer · **Problem type:** well-structured
> **Problem:** Processor and emitter do nothing to the Index.
> **Needed from them:** rate rows (kPa/sol, C/sol), draw and cost per machine in `docs/design/balance-sheet.md`.
> **Done when:** the numbers exist; Technical Lead adds them to `machines/*.json`.

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** Oxygen maker needs the made-up band (R8); the four kit items need icons; check the processor's east ports and the emitter's lamp in a close shot.
> **Done when:** textures exist and a real-client shot shows them.

> **To:** Technical Lead (later) · washed regolith item and wash station; placement behaviour of kits (W3).

## Risks
Draws of 3 units each are INVENTED and enter "Dome stable" demand when built. Kits that cannot be placed may confuse a tester. Kit recipes craftable from sol 0 skip the Phase 3 pacing.

## Shared model updates
- Lesson: GameTests that reset the shared planet must not overlap another planet test.
- Decision proposed: classroom pace is hidden by `classroom_enabled` (default false); code stays.
