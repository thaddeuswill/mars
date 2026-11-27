# Technical Lead: habitat restyle wiring (jar 0.3.2-stage3-c)

## 0. Handoffs received
Artist restyle report section 3: **accepted**, all four items done. Layout unchanged. `water_reclaimer_open` (repair state): not built, no goal needs it yet.

## 1. Done
- New `mars.hull.RoofFixtureBlock` (shape = union of the model boxes: vent 14x1x14 foot + 12x5x12 body; dish 14-wide base to 11 px + 4-wide mast to 16 px). Registered in `HullBlocks`: `hull_plate_base`, `hull_plate_rim` (HullPlateBlock), `hull_glass_ribbed` (HullGlassBlock), `roof_vent`, `roof_dish`; items; creative tab (via `HullBlocks.ALL`); lang keys; loot tables (drop themselves).
- Tags: `airtight.json` has base, rim, ribbed; `not_airtight.json` has vent and dish (plus cracked).
- `DomeTemplate`: new kinds `HULL_BASE` (perimeter `#`, `*`, `E` at y1; not `=` glass columns, not doors), `HULL_RIM` (perimeter roof y4), `GLASS_RIBBED` (Hab B roof). Interior roof stays `hull_plate`; cracked plate unchanged. `StarterDome.stateFor` maps them. Dish at (4,5,3) and vent at (3,5,6) above the shelter, placed by `StarterDome.placeRoofFixtures`; `SIZE_Y` stays 5, fixtures are outside the template box and the flood fill.
- Tests: `DomeTemplateTest` (glass 10 + ribbed 36, trim only on perimeter and right y, 74 rim cells, a fixture layer at y5 does not change a sealed volume), `HullAssetsTest` (files, textures, tags, lang for all 8 hull blocks), boot check `dome.habitat_variants_placed_and_airtight` (real blocks: base/rim/ribbed airtight; dish and vent not airtight and not full cubes).
- Docs: ARCHITECTURE 2.20 addendum, asset-list M-01b and M-12, HANDOFF 0c, AGENTS, BACKLOG row. Screenshot script `tools/screenshots/habitat_restyle.shots`.

## 2. Real output
- `./gradlew spotlessApply --offline`, then `spotlessCheck build test --rerun --offline`: BUILD SUCCESSFUL in 7 s (warm daemon), 252 unit tests, 0 failures.
- `./gradlew build runGameTestServer --offline` (jar version bumped): BUILD SUCCESSFUL in 19 s, `MARS-SELFTEST RESULT PASS passed=110 failed=0`.
- Gradle invocations: 4 (spotlessApply; spotlessCheck+build+test; build+runGameTestServer; plus the first spotlessApply). Screenshot runs: 2 client runs (about 43 s each); the first run's output was discarded after I found two script faults.
- Jar `mod/build/libs/mars-0.3.2-stage3-c.jar`, sha256 `1f68ea1c7a2a1dc5029faf37246759542d75b638be81a2fa36ba0e37179a576c`.

## 3. Screenshots (`tools/screenshots/out/20261127-232021/`) vs references
I opened every PNG (timestamps 23:21, new).
| Shot | Should match | Result and what still differs |
|---|---|---|
| hab_1, hab_2 (noon, two sides) | #12 MDRS (cream trim top and bottom, vertical panels, dish on roof) | Cream bands at base and rim read clearly, vertical panels read, dish and vent visible on the roof, ribbed glass patch in the corner. Differs: a flat-roofed box, not a cylinder; no steps or door above steps (layout, Game Designer question). Dish is small at this distance. |
| hab_3 roof close-up | #12 roof dish, #7 ISS panel seams | Rim course with bolts and a dark inner line reads well; dish and vent readable. The ribbed glass looks like plain glass panes at this angle. |
| hab_4 door from outside | #11 Quest hatch | Rounded-corner grey door, ringed window with arc, yellow-green seal frame, crank, placards: close. Differs: hatch is flat in the wall (no frame depth, no hinges, no steps); the door bottom is cut by the ground line in the shot. |
| hab_5, hab_6 porthole bank | #12 MDRS portholes, #13 | Round ringed portholes in white panels read as portholes (two rows of five). Differs: round shape is pixel-art only, the wall is flat. Inside (hab_6) the sky colour shows through. |
| hab_7, hab_7b machine bay | #1 MOXIE (maker), #8 ISS racks (reclaimer) | **Not judged: the bay is almost black.** I can tell a maker (left) and the blue-edged reclaimer (right) are there, no more. |
| hab_8 shelter | #10 NORS tank | Tanks read as white wraps with orange-black hazard label and green tag, plus the door with its window: matches #10 and #11 well. Differs: full cubes, no straps visible at this size, bunks are vanilla beds (red) and clash with the white palette. |
| hab_9, hab_10 low sun | same as hab_1 | Hull goes cool blue-grey with dark shadows; reads fine. |

**Brightness.** In game lighting the white hull is not too bright: measured pixels (RGB) roof top about 203,200,188 to 221,217,206, noon wall faces about 170,166,157, nothing clipped, none above 221. It reads light grey-cream, close to MDRS white. The interior walls in the shelter (about 95 to 113 grey) look fine. Easy to tune: the base colours in `tools/art-preview/gen_habitat_restyle.py` (one ramp for the plate), no code change.

**Problem found (not caused by this change, not fixed):** the machine bay (and Hab B probably) is nearly black while the shelter and Ops are lit. It is the room with no window and, as far as I can tell, no light source. Not verified why the shelter is lit without a window (maybe the dimension ambient light plus the nearby porthole). Cheap fixes to choose from: light-emitting hull lamp blocks, or Mars dimension ambient light. This belongs to the Game Designer (is darkness intended as part of the blackout feel?) and Artist.

The first low-sun attempt (`/time set 1000`) looked identical to noon, so I used 12400.

## 4. Decisions needed
- Dark machine bay: ill-structured (design intent versus playability). Recommendation: a small ceiling light block per room, matching the real hardware (ISS racks have lights). 
- Ribbed glass annex roof (Artist's question): registered and used, **well-structured, done**.

## 5. Handoffs
> **To:** Lead Game Designer / Lead Artist · **Problem type:** ill-structured
> **Problem:** machine bay unlit in game; steps/stub still open from the Artist report.
> **Needed from them:** decide light source rule for rooms without windows; answer the steps/berm question.
> **Done when:** decision in the dome layout doc.

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** vanilla red beds and crates/barrels/crafting table inside the white habitat clash with the restyled palette (hab_6, hab_8).
> **Needed from them:** a decision whether to make habitat bunk/crate blocks.

## 6. Risks
- Fixtures at y5 sit above the sealed roof; if a later feature builds a second storey they would need to move. Existing worlds (placed before 0.3.2) keep the old dome blocks (no migration, no loss).
- `RoofFixtureBlock` shape is box approximation of the dish (not exact model).

## 7. Shared model updates
None.
