# Artist report: habitat bunk, stowage crate, bench (2026-11-27)
Assets and scripts only; no Java, no client run. Not yet seen in game.

## 0. Handoffs received
Owner direction (accepted): own bunk and crate instead of the red vanilla bed and brown barrel; crafting table stays until machine recipes exist. TL clash list (hull-lights report section 5): accepted. Bench textures delivered as optional pre-work.

## 1. Done
- `tools/art-preview/gen_habitat_furniture.py` (textures, models, blockstates, item models; checks: 16x16, no pure black/white, max 6 colours, texture files exist) and `make_furniture_boards.py`.
- Textures (`pack/assets/mars/textures/block/`): `habitat_bunk_{frame,end,top,bag,strap,pillow}`, `stowage_crate_{front,side,top,skid,inside}`, `habitat_bench_{top,side,front}`. Final-style, no magenta marker (same as other habitat art).
- Models (`models/block/`): `habitat_bunk_foot`, `habitat_bunk_head`, `stowage_crate`, `stowage_crate_open`, `habitat_bench`; items `habitat_bunk` (both parts, display transforms unverified), `stowage_crate`, `habitat_bench`.
- Blockstates: `habitat_bunk.json` (keys `facing,occupied,part`, all 16), `stowage_crate.json` (`facing` north/east/south/west/up/down x `open`), `habitat_bench.json`.
- Boards `docs/art/renders/habitat-restyle/14_habitat_bunk.png`, `15_stowage_crate.png`. `asset-list.md` M-33 and S-06 updated. I did not touch lang files (names below).
- Test: `./gradlew test --offline --tests '*Assets*' --tests '*Models*' --tests '*ContentFiles*'` BUILD SUCCESSFUL.

## 2. References and verdict (I looked at the boards)
Viewed: red bed shot `lamp_1_shelter_day.png`, `chapea_sandbox_360.jpg` (no bunk or locker visible), `quest_airlock_hatch_hurley.jpg` (white panels, mesh grille, webbing strap), `iss_wpa_rack_peake.jpg` (off-white rack doors with latches and cut-outs, white padded bag with strap, labels).
- **Crate: reproduced** in part: off-white door with latch and label card, skid, stacking corners; reads as a locker in the render. Not reproduced: rack clutter, real label text.
- **Bunk: partly reproduced.** It reads as a low bunk with bag, strap, pillow and drawers, no longer red/wood. **No photo of crew quarters or sleeping bags was viewed**, so the shape is a design choice, not a copy. The bag colour (muted blue) is stylised.
- Blender is not Minecraft; light and shimmer in game are unchecked.
- **To collect for a real bunk reference** (none viewed): ISS Crew Quarters (CQ) station interior photo; ISS sleeping bag tethered to a wall; CHAPEA crew quarters interior (NASA JSC); Skylab or HI-SEAS/Mars-500 bunk; ISS cargo transfer bag (CTB) stacks and stowage lockers. Sources: NASA media, Commons.

## 3. Bunk: what the TL must build (verified in the NeoForge 21.1.267 sources jar)
- Vanilla `BedBlock.getRenderShape` returns `ENTITYBLOCK_ANIMATED`; `newBlockEntity` returns `BedBlockEntity` (colour only); vanilla `red_bed` blockstate points at a particle-only model, the look comes from `BedRenderer` (entity). So a custom bed is NOT a plain block model unless overridden.
- **Recommended route A (no renderer):** `HabitatBunkBlock extends BedBlock` (pass `DyeColor.WHITE`), override `getRenderShape` to `RenderShape.MODEL`, override `newBlockEntity` to return `null` (BedBlock itself never reads the BE; unverified that nothing else needs it, test by sleeping). Alternative: keep the BE and add the block with `BlockEntityTypeAddBlocksEvent` (exists in NeoForge 21.1.267). Sleeping, spawn point, `occupied`, head placement stay vanilla. `LivingEntity.checkBedExists` uses `state.isBed(...)`, which is true for any `BedBlock`, so the `minecraft:beds` tag is not needed (I read the source). `mars` dimension type has `bed_works: true`, so no explosion.
- Route B (own two-block block with own sleeping logic): more code, no gain. Not recommended. No BlockEntityRenderer or entity model is needed on route A; the JSON models above are what I can deliver and they are complete for it.
- Block id `mars:habitat_bunk`, item `BlockItem` (or `BedItem`). Collision: use the 9 px mattress box (`Block.box(0,0,0,16,9,16)`) so it is step-up-able like vanilla; boards (10 px) and pillow (11 px) are visual only. Properties: `noOcclusion()` (bunk is not a full cube anyway) and sounds as wool/cloth or metal (TL's choice).
- Model orientation: blockstate matches vanilla FACING (foot block faces the head block), `y` 0/90/180/270 for north/east/south/west. The dome template (foot north cell, `Face.SOUTH`) maps to y=180 with the head board at the south end of the head cell; the head board of one bunk then touches the foot board of the next, which looks like a divider. If the designer wants heads against a wall, flip the faces in `DomeTemplate` (not an art need).
- Design need (`DESIGN.md` line 13, dome-layout): a bed sets the respawn point and lying down is a natural pause; sleeping never skips Mars time (TL/Designer logic, not art). Unused bunks are plain furniture (dome-layout line 126). Route A satisfies both. Blackout wakes the player via `StarterDome.spawnPos`, which does not depend on the bed block.

## 4. Crate and replacement in the dome
- `mars:stowage_crate` as `BarrelBlock` subclass (FACING 6 directions, OPEN): blockstate has exactly those keys, model front = model north, so default facing=north shows the door (what `StarterDome` gets today from `Blocks.BARREL.defaultBlockState()`). Needs `noOcclusion()` (door is recessed, the open door swings outside the cell, and non-boundary faces of an occluding full cube would render dark; from memory, check with a screenshot). If not a BarrelBlock, map `open` to the closed model.
- `StarterDome.stateFor` (lines 176-191): `BED_FOOT`/`BED_HEAD` -> `HabitatBlocks.HABITAT_BUNK` with `BedBlock.FACING = f`, `PART`; `CRATE` -> `HABITAT_CRATE` default state. `DomeTemplate` needs no change. Optional: `BENCH` -> `habitat_bench` (a `CraftingTableBlock` subclass, same recipes) when the owner wants it; textures exist.
- Names for lang (TL): `block.mars.habitat_bunk` "Habitat Bunk", `block.mars.stowage_crate` "Stowage Crate", `block.mars.habitat_bench` "Work Bench". Tests: add ids to the asset tests; `DomeTemplateTest` counts kinds, unaffected.
- Loot: crate drops itself and contents (barrel behaviour); bunk loot table must drop one item only from the foot part (vanilla bed loot pattern).

## 5. Decisions needed
1. Keep the crafting table or use `habitat_bench` now (well-structured; owner said keep until recipes exist). Recommendation: keep; swap when recipes land.
2. Bunk bag colour per bunk ("personal touches", style guide section 8): one blue for now; per-bunk colours need 5 texture variants (ill-structured, low priority).

## 6. Handoffs
> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** Register the bunk and crate and swap them into `StarterDome`.
> **Context:** section 3 and 4 above; assets in `pack/assets/mars/`.
> **Needed from them:** blocks as in section 3-4, lang, asset test ids, screenshot of the shelter at 2 m and 4 m, check that sleeping works and the item icon looks right (display transforms in `models/item/habitat_bunk.json` are my guess).
> **Done when:** shelter screenshot shows no red bed or brown barrel and a player can sleep and set spawn.

## 7. Risks
- Bunk shape not reference-backed; blue bag not sourced. Light at game scale unchecked (crate and bunk may read grey in dim lamp light like the maker did).
- Route A with a null BlockEntity is untested by me.

## 8. Shared model updates
None.
