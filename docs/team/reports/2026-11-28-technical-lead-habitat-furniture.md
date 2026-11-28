# Technical Lead report: habitat bunk and stowage crate (2026-11-28)

## 0. Handoffs received
Artist handoff (`2026-11-27-artist-habitat-furniture.md` sections 3, 4, 6): accepted. Two deviations, both stated below: the bunk drops from the **head** part (vanilla pattern), and the Work Bench is lang only (no block).

## 1. Done
- `mod/src/main/java/mars/habitat/`: `HabitatBunkBlock` (BedBlock, MODEL render, null block entity), `StowageCrateBlock` (BarrelBlock), `HabitatBlocks` (registry, `BedItem`, `BlockEntityTypeAddBlocksEvent` adds the crate to `BlockEntityType.BARREL`). Wired in `MarsMod`, creative tab (`MarsMachines`), `StarterDome.stateFor` (bunk with FACING and PART; crate with FACING from the template face).
- Pack: loot tables `habitat_bunk.json` (head part only), `stowage_crate.json` (copies contents and name); lang Habitat Bunk, Stowage Crate, Work Bench (`habitat_bench` has art but no block registered: kept the crafting table).
- Tests: `HabitatAssetsTest` (files, 16 bunk and 12 crate states, 16x16 textures, lang, loot), GameTest `HabitatGameTests`.
- Jar `0.3.7-stage3-h`, sha256 `4a72c3d7ad554827eaabb7369a83f1f4780fb9b2ec6d05df410fe65cd4a8d6bc`. ARCHITECTURE.md 2.21, HANDOFF 0h, BACKLOG row.

## 2. Real output
- `./gradlew spotlessCheck build test --rerun --offline`: BUILD SUCCESSFUL, 6 s (warm daemon), 260 unit tests, 0 failures.
- `./gradlew runGameTestServer --offline`: `RESULT PASS passed=120 failed=0`, 13 s. New checks: `habitat.bunk_is_a_model_bed_without_block_entity` (render MODEL, isBed, no block entity on foot or head, collision top 0.5625 = 9 px), `habitat.mars_dimension_beds_work` (true), `habitat.bunk_sleeps_and_sets_spawn` (startSleepInBed OK, sleeping, respawn = bunk), `habitat.sleep_does_not_move_mars_time`, `habitat.bunk_drops_one`, `habitat.crate_is_a_working_barrel`. Sealing tests unchanged and green.
- Gradle invocations this task: about 12 (two compile errors from my own code, one first-run test failure, see below), plus 1 real-client screenshot run (49 s) and the final full rerun above.
- First GameTest run failed `bunk_sleeps_and_sets_spawn` with NOT_POSSIBLE_NOW: the sky brightness needs a few ticks after `setDayTime`. Fixed by waiting 5 ticks, a test fix, not a game bug.

## 3. Answers to your questions
- **Null block entity:** nothing in the sleep path read it (sleep, respawn, drops, break, placement all tested). Route A stands; no event needed for the bunk. The crate does need `BlockEntityTypeAddBlocksEvent` (barrel entity validates its blocks).
- **Sealing:** bunks are not full cubes, so the flood fill passes through them exactly as it did through the red bed; the crate is a full cube like the barrel. No test moved.
- **Sleep and time:** the planet runs on game ticks, not the level day time a night skip changes. Tested by moving day time +24,000: sim ticks and all Index values unchanged. **Not run:** a real all-players-asleep skip (fake players are not in `level.players()`); the claim rests on the code path plus that test. Vanilla still skips the overworld-shared day time and weather in the mars dimension: sky is pressure-keyed, so only the day and night cycle moves. Flag for the Game Designer if a sleep skip of the sun is unwanted.
- **Blackout:** wakes at `StarterDome.spawnPos`, which does not use the bed; unchanged.
- **Orientation:** foot north, head south (FACING SOUTH, y 180 in the blockstate). In the shots the head board plus pillow sit at each bunk's south end; the next bunk's foot board is next to it, so the pairs read as three bunks with dividers. Acceptable, no template flip.
- **Collision:** 9 px (vanilla bed shape), step-up-able.
- **Loot:** head part, not foot (deviation): vanilla bed pattern. Breaking the foot makes the head destroy itself and drop; breaking the head drops directly. GameTest: breaking the foot gives exactly one bunk and clears the head.

## 4. Screenshots (looked at the PNGs)
`tools/screenshots/out/20261128-002620/fur_1` to `fur_8` (script `tools/screenshots/habitat_furniture.shots`; no other client running; PNG timestamps 00:27:07). Shelter bunks at 2 m and 4 m, Ops crate at 2 m and 4 m, day and night. No red bed and no brown barrel. The crate door (label, latch) faces the room (north, toward the player). Bunks read as slate-blue bags with cream boards, amber straps, beige pillows.
Measured luminance (Rec. 709 on 8-bit RGB, crop of the bag top or crate face against a wall crop):

| Shot | Furniture | Wall | Ratio |
|---|---|---|---|
| bunk 4 m day | 51.7 | 97.3 | 0.53 |
| bunk 4 m night | 42.7 | 81.3 | 0.53 |
| crate 2 m day | 76.6 | 123.8 | 0.62 |
| crate 4 m night | 61.0 | 105.6 | 0.58 |

Verdict for the Artist: both are **darker than the wall but not grey-brown**; the bunk bag reads dark slate (RGB about 44,54,56 in day), a clear shape, a bit low in value; the crate face reads like off-white metal with the label and orange latch visible. The maker v3 (87 vs wall 87 to 117) was brighter. If wanted: lift the bag texture value by about a third. Item icon and display transforms in a real inventory were not checked (no GUI shot).

## 5. Decisions needed
None. (Well-structured: keep the crafting table; swap to `habitat_bench` when recipes land.)

## 6. Handoffs
> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** bunk bag is about half the wall luminance in game; crate face about 0.6.
> **Context:** table in section 4, `tools/screenshots/out/20261128-002620/fur_2_bunk_4m_day.png`.
> **Needed from them:** keep or lift `habitat_bunk_bag` value; check the item icon transforms in the creative tab.
> **Done when:** Artist accepts the look or sends a new texture.

> **To:** Lead Game Designer · **Problem type:** well-structured
> **Problem:** a real vanilla sleep skip would move the day and night phase in the mars dimension (not Mars time).
> **Needed from them:** confirm that is acceptable.

## 7. Risks
- Real night skip with real players not run. Bed item icon not seen in a GUI. Per-bunk bag colours not built.

## 8. Shared model updates
None.
