# Lead Artist: habitat restyle (dome, hatch, glass, tank, reclaimer, maker)

Date 2026-11-27. Role: Lead Artist. Task: match what we build to the real references (SME audit `docs/art/habitat-reference-audit.md`, images in `docs/art/references/habitat/`).

## 0. Handoffs received
- SME audit section 4 cues and option A (trim courses, roof rim, vent, dish, rounded-corner look): **accepted**. Stub, steps and berm (B, C): not done (layout).
- "No Java edits": kept. All existing file names unchanged. Two existing models changed parent (see section 1).

## 1. Done
I looked at the reference images (hatch #11, NORS tank #10, MOXIE PIA24201, ISS racks #8/#9, MDRS #12, BEAM #7) and the in-game shots life_1, life_2, life_3 before drawing. Generator: `tools/art-preview/gen_habitat_restyle.py` (all textures and new models; checks 16x16, no pure black/white, opaque unless glass; printed OK). Boards: `tools/art-preview/make_habitat_boards.py`, output `docs/art/renders/habitat-restyle/01..08_*.png`. I looked at boards 01, 03, 04, 05, 06, 07, 08; **I did not open board 02** (cracked plate; I saw the texture on the contact strip only).

Colours are read by eye from the photos and snapped to style-guide ramps; **not measured**. Restyled files carry **no magenta marker** (status "Draft, reference-checked", not Final).

| Asset (file) | Reproduced | Not reproduced, why |
|---|---|---|
| `hull_plate.png` | Warm off-white; **vertical seams only** (MDRS tall panels), bolts near both edges, light wear | Stickers/lettering (no text in textures); curved cylinder; paint weathering (dust is gradual, later) |
| `hull_plate_cracked.png` | Same plate, dark crack, bright lip, rust dust streak (two channels) | No real reference exists (invented, already on the fiction audit) |
| `hull_glass.png` | Round ringed porthole with silver ring and dark gasket in a painted panel; 35% alpha glass | A truly round hole (3D); MDRS recessed housing. **Side effect:** Hab B roof glass now reads as porthole plates until `hull_glass_ribbed` is used |
| `hull_door_top/bottom.png` | Rounded corners (3 px cut), yellow-green seal frame, raw aluminium, ringed window with green/yellow/red arc, crank, two placards, mesh grille | Hinges, latch hardware, brushed finish, real text. Arc is static decoration, not a live sealed state. Top tile has 8 colours (exceeds the 6-colour rule, accents only) |
| `o2_tank.png` + new `o2_tank_top.png` | White wrap with cylinder shading, two straps, diagonal hazard label (safety orange #E69F00 for the yellow), green tag with ring mark, dark base ring | Round silhouette (block must stay a full cube for culling/collision), cables, valve block, label text. 8 colours (accents) |
| `water_reclaimer.png` + new `_top`, spare `_open` | Off-white bay panels, seam, latches, dark slots, blue handrail, orange tag; open state dark with silver studs and green card | Hoses, red-cross label, Condensate bag, rack grid. Open texture is unused: no block state exists |
| `o2_generator_*.png` | Gold ramp, pocketed grid (3 x 2 per big face), X-rib end, off-white tubes, silver fittings, copper strip, silver connector, cable dots | MOXIE's 4 x 4 face grid, gloss, smooth tube bends, base plate, hex bolts. Still one block, about **3x MOXIE** scale (style guide 5.5a) |

Model files changed: `models/block/o2_tank.json` and `water_reclaimer.json` now use parent `minecraft:block/cube_column` (was `cube_all`) so top and sides differ; blockstates and item models still load (tests below). `o2_generator.json` unchanged.

New variant files (blockstate, block model, item model, textures all exist; **no Java registers them yet**): `hull_plate_base`, `hull_plate_rim` (+ `hull_plate_rim_top`), `hull_glass_ribbed`, `roof_vent` (+ `roof_vent_side`, `roof_vent_top`), `roof_dish`.

Also: `docs/art/asset-list.md` (rows M-01a..M-12), `PROCESS.md` (one-line art definition of done: reference check before done).

**Verified:** `cd mod && ./gradlew test --offline --tests '*Assets*' --tests '*Models*' --tests '*ContentFiles*'` BUILD SUCCESSFUL; my own script found 0 missing textures across all block models. **Not run:** full test suite, any client, in-game look. Boards 04 and 08 are flat composites of the real textures (Blender framing clipped the 2-block door), not Blender or in-game renders.

## 2. Decisions needed (ill-structured)
**Window look vs greenhouse roof.** Wall windows are now round portholes. Roof glass needs `hull_glass_ribbed`. Recommendation: register it and use it for the annex roof (cheap). Option: keep porthole everywhere (roof looks like plate with holes). Needs Game Designer + TL.

## 3. Handoffs
> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** Trim courses, roof rim, vent and dish need new blocks and template changes (no layout change).
> **Context:** assets exist in `pack/assets/mars` (list in section 1). `DomeTemplate`: floor y0, walls y1..3, roof y4 (outer faces of y4 are the 4th visible course).
> **Needed from them:** (1) Register `hull_plate_base` and `hull_plate_rim` as `HullPlateBlock` (airtight tag), `hull_glass_ribbed` as `HullGlassBlock` (airtight), `roof_vent` and `roof_dish` as non-full, non-airtight decoration blocks (`not_airtight` tag, no collision needed; shapes: vent 12 x 5 x 12 px inset 2; dish full 16 px tall model, hit box small). Loot tables like `hull_plate`. (2) Template: perimeter wall cells (`#`, `=`, `*`, `E`) at y1 use `hull_plate_base` (not under door/glass cells); perimeter roof cells at y4 use `hull_plate_rim`; Hab B roof (`annexRoof`) uses `hull_glass_ribbed`; interior roof cells stay `hull_plate`. (3) Optional extra layer y5 (placer only, not part of the seal): one `roof_dish` and one `roof_vent` on the roof near the shelter; do not change `SIZE_Y` if the flood fill counts it (your call). (4) Cracked plate keeps `hull_plate_cracked`; it sits in a y2 course so no change.
> **Done when:** new screenshot from outside shows cream bands at base and rim, porthole bank and vent/dish; seal tests still pass.

> **To:** Lead Game Designer · **Problem type:** ill-structured
> **Problem:** Entry steps/ramp stub and regolith berm (SME B and C) are outside the 23 x 16 layout; the real hatch and MDRS door both sit above steps.
> **Needed from them:** a yes/no after looking at board 08. I made nothing for these.
> **Done when:** decision recorded in the dome layout doc.

> **To:** Technical Lead (later) · **Problem type:** well-structured
> **Problem:** `water_reclaimer_open.png` has no use. If a repair state (goal C2) is wanted, add a block-state property and a second model using it.

## 4. Risks
- Porthole roof (see section 2) until the TL step lands.
- White hull is much brighter than the old grey; it was checked only in flat composites, **not in game lighting**. Interior will look lighter.
- Hatch and tank exceed the 6-colour-per-face rule (labelled accents); revisit if the owner finds them busy.
- Rounded corners on the tank are impossible with a full-cube block.
- Colour hexes are by eye; a SME check against measured values is optional.

## 5. Shared model updates
- Lesson: "Reference check before done" (side-by-side board, honest reproduced / not reproduced list) now in `PROCESS.md` section 2.
- New files and generator names for the habitat set: `gen_habitat_restyle.py`, `make_habitat_boards.py`.
