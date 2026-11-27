# Technical Lead: hull lights (ceiling lamps in the starter dome)

## 0. Handoffs received
- Artist `2026-11-27-artist-hull-light.md` section 3: **accepted** as written (block, tag, item, tab, `Kind.HULL_LIGHT`, 18 cells, Hab B glass 36 to 32). One part **questioned and answered by measurement**: "read the lit shelter's level and match it by 1". There is no lit shelter level to match (section 2).
- Owner direction (lit rooms like real habitats; darkness kept for a later Blackout state): accepted. Lights do not go off in a Blackout yet.

## 1. Done
- `mars:hull_light`: `HullBlocks.HULL_LIGHT` via `plate()` with `lightLevel(12)` (`HullBlocks.LIGHT_LEVEL`), item, creative tab (after rim plate), `airtight.json` entry. Assets, lang and loot already existed.
- `DomeTemplate.Kind.HULL_LIGHT`, `DomeTemplate.LAMPS` (18 cells, y 4), `isLamp`; `StarterDome.stateFor` maps it. Hab B lamps replace ribbed glass (32 left). No change to `mars.dome` sealing code.
- Tests: `DomeTemplateTest` counts (ribbed 32, lights 18), new `lampsAreOnlyInteriorRoofCellsOverFloorAir`, `everyRoomStaysSealedWithLamps` (also: removing a lamp opens the room); `HullAssetsTest` lists hull_light (files, tags, lang). New GameTests `dome.lights_measured_lit_rooms`, `dome.lights_machine_bay_lit`, `dome.lights_keep_the_seal`.
- Jar `mod/build/libs/mars-0.3.3-stage3-d.jar`, sha256 `87ed5dfcee7d039234ab938ec491314f3cb445ded476f626fa42800253ea8359`.
- Docs: ARCHITECTURE 2.20 addendum, asset-list M-01c, BACKLOG row, HANDOFF 0d, AGENTS.md. Screenshot script `tools/screenshots/habitat_restyle.shots` extended (hab_11 to hab_14).

## 2. Measured light (real light engine, GameTest, group 5 dome)
| Place | Block light at floor cells (y 1), no lamps | With 18 lamps |
|---|---|---|
| Machine bay | 0 | worst 7 (at x16,z3), best under lamp 11 at y 3; head height (y 2) worst 8 |
| Shelter | 0 | not lamped |
| Ops | 0 | not lamped |
| Hab B annex, storage, suit room, airlock | not measured without lamps | worst 7 each |

So the "lit shelter" in earlier screenshots is **not lit by the engine**: block light 0, `ambient_light` 0.0, hull roof. What the screenshot shows is the client's minimum lightmap (dim grey, hab_14). Level 12 was kept: worst cell 7 equals the calculation 12 - 3 - 2 and the threshold I justify (7 is clearly readable; vanilla spawn-safe light is 8, so 7 is just under mob-spawn threshold, not relevant here because the dimension spawns no monsters). 13 would give worst 8; not needed. Asserted threshold: worst floor cell at least 7, cell under a lamp at least 11, and above the no-lamp baseline.
Hab B is brighter in the screenshot because its roof is glass (sky light as well). At night Hab B will rely on the lamps only; not shot.

## 3. Screenshots (looked at; folder `tools/screenshots/out/20261127-232753/`, all new)
- hab_7, hab_7b, hab_11 (machine bay): no longer black. Even warm beige, no hot spots, lamps not in frame (camera low). Readable.
- hab_12 (Hab B), hab_13 (storage): clearly lit; the ceiling panel is visible at the top edge (cream rectangle in a bezel). Hab B reads near white, storage warm brown.
- hab_14 (shelter, no lamps): dim grey. Still a "dark-ish room" next to the others.
- Compared with `iss_wrs_rack_open_fincke.jpg`, `iss_wpa_rack_peake.jpg`, `PIA24201.jpg`:
  - Reclaimer now: a white-grey box with teal-blue edge strips, two dark vent slots, orange tag. Matches the references in colour family (white-grey rack with blue struts) and in having slots. **Still differs:** references show racks full of hoses, cables, labels, bags, a dark open panel and bright light-box lamps; ours is one clean, closed cube, no hoses, no depth, no labels.
  - Maker now: a gold-brown crate-like block with a grey-white frame and two bracket handles. It does **not** look like a white rack; it reads as a brown crate (the MOXIE/PIA reference is a gold metal box, so the colour is deliberate, but at game scale it looks like a wooden crate). Differs: no gold foil texture, no pipes.
  - Light itself: references have bright, bluish-white, cluttered light; ours is a warm, even, dim beige. Warm is the lightmap colour, and the lamp texture is flat.
  - Not checked: night, Blackout, other GUI scales.

## 4. Run record
- Gradle invocations this task: spotlessApply+build+test (1), runGameTestServer (1, diagnostic), run-shots (1 client run, its own Gradle call, 60 s), final `spotlessCheck build test --rerun --offline` (1, 6.4 s wall), final `runGameTestServer --offline` (1, 13.1 s wall). Total 5 Gradle runs. Results: BUILD SUCCESSFUL, 254 unit tests 0 failures, `MARS-SELFTEST RESULT PASS passed=113 failed=0`.
- Not run: dedicated server, a real client with the jar in `mods`.

## 5. Vanilla bed, barrel, crafting table clash (list only, not built)
| Now (vanilla) | Problem | Needs |
|---|---|---|
| Red bed (bunks) | red wool, clashes with white habitat | custom bunk: a `BedBlock` subclass plus own `BlockEntityType` (needs its valid-blocks list), 2 textures; or a global resource-pack override of the red bed (zero code, changes all red beds) |
| Barrel (crates) | brown wood, banded | custom storage crate: block + container block entity + menu (or reuse a `BarrelBlock` subclass with a different texture; simplest) |
| Crafting table (bench) | wood | `CraftingTableBlock` subclass with new textures (cheap; recipes unaffected) |
| Lectern | wood, small | optional; could stay |
Order of cost: bench (cheap), crate (cheap if subclassed), bunk (most work). Art needed for each; routed to the Artist.

## 6. Decisions needed
- Lamp the shelter and Ops too (about 6 lamps each, same rule)? Well-structured; recommendation: yes, the shelter is where players start and is block light 0. Needs a count change in the test (18 to about 30) and the Artist's accept; not built because the handoff said 0 for now.
- Blackout turns lamps off? Ill-structured, Lead Game Designer (the Artist already routed it).

## 7. Handoffs
> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** the maker reads as a brown crate and both machines are clean cubes compared with the cluttered ISS/MOXIE references.
> **Context:** section 3, hab_7 and hab_7b.
> **Needed from them:** decide whether the maker needs a white or foil restyle and whether the bed/crate/bench custom textures (section 5) are wanted.
> **Done when:** a decision is in asset-list.md.

## 8. Risks
- Light level 12 fits the measured grid; mixed with the client lightmap the bay still looks beige-dim, not "bright white". Raising to 13 is one constant.
- Lamps are fixed blocks players can break; breaking one leaves a hole in the roof (airtight rule applies, so the room leaks). Intended.
- The shelter and Ops stay dark at night.

## 9. Shared model updates
- Lesson: "what the screenshot shows" and "what the light engine says" differ; measure light in a GameTest before tuning from screenshots.
