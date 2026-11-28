# Carbon scrubber: model and texture spec (first pass)

**Owner:** Lead Artist · **Date:** 2026-11-28 · **Status:** model, textures and cycling animation written and script/unit-checked; NOT rendered in 3D and not seen in a real client (flat views and frame strips viewed: `docs/art/renders/vat-scrubber-sheet.png`) · **Real idea:** ISS Carbon Dioxide Removal Assembly (CDRA): two desiccant/adsorbent beds and air selector valves (**VERIFIED**, NTRS 20130000838, quoted in `docs/science/machine-references.md`). Copied from the idea: twin cylindrical beds, a valve block, a CO2 line on top, beds that take turns. The real CDRA vents or reprocesses the CO2; "carbonate rock out of a chute" is the game's step (look brief row 4). **Size, band, plate and every number are invented.** Wording class: **Game-sized version of a real idea.** Wears the made-up band.

Silhouette cue: **twin cream-grey cylinders** side by side (cylinders as crossed 6 x 4 and 4 x 6 px boxes, octagon look), a low dark **valve block across their front**, a flat inlet header on the caps, a small pale chute on the east side. Twin vertical cans: no other machine has two equal columns (emitter tanks are wide and low, processor is one squat box, extractor one thin mast). Flat views compared: viewed, OK.

| Part | Elements (px, from-to) | Texture (`pack/assets/mars/textures/block/`) |
|---|---|---|
| Plinth with band | x1-15, y0-4, z1-15 | `carbon_scrubber_base` (uv v 12..16), `_top` |
| Valve block (lamp ring, plate, two bed windows, valve bar on the north face) | x2-14, y4-14, z2-7 | `carbon_scrubber_front` (north, uv 2,0,14,10), `_valve_side` |
| Bed A (west) and bed B (east), each two crossed boxes | A: x1-7 / x2-6, B: x9-15 / x10-14; y4-20, z8-12 / z7-13 | `carbon_scrubber_bed` (running: `_bed_a_running`, `_bed_b_running`, animated) |
| Bed caps | x2-6 and x10-14, y20-22, z8-12 | `carbon_scrubber_cap` |
| CO2 inlet header across the caps | x3-13, y22-24, z9-11 | `carbon_scrubber_pipe` |
| Manifold between the beds (behind the valve block) | x7-9, y6-10, z9-11 | `carbon_scrubber_pipe` |
| Carbonate chute (east face of bed B) | x15-16, y4-8, z8-12 | `carbon_scrubber_chute` (running: `_chute_running`, filled `#D3CDBF`) |

Colours: canister ramp `#8A877E` `#AFAB9F` `#CFCABD` `#E4E0D2` (cream-grey); alloy ramp as the other machines; indicator pale blue `#BFE3F7` lit, `#6F7F8B` dim; status ring `#56B4E9`; band `#CC79A7` / `#8E3F6C`; plate `#CC79A7` with `#2A2420` slash at x9-12, y4-7 of the front (lamp frame x3-7, y4-8). No green (test-checked). Model height **24 px (the inlet header), one cell**, so collision and outline are the full model.

## Running state (extends `docs/art/running-state-table.md`)

| State | Status ring | Bed windows on the valve block | Beds | Chute |
|---|---|---|---|---|
| off | dark | both dark | ring rows dark | empty mouth |
| idle | hollow ring | both dim pale | ring rows dark | empty mouth |
| running | solid ring | **one window lit, then the other** (A, A + valve bar, B, B + valve bar) | the active bed's top ring lights pale blue, the other is dark: **the beds take turns** (the cycle) | filled with carbonate |

Cycle: 4 frames x 20 ticks = 80 ticks (4 s), all three strips share the same timing so they stay in step. Frame counts and ticks are INVENTED. The chute fill is static (shape), the cycle is the moving part.

## Models (`pack/assets/mars/models/block/`)

`carbon_scrubber` (= idle), `carbon_scrubber_off`, `carbon_scrubber_idle`, `carbon_scrubber_running` (animated front, both beds, filled chute). Item model `pack/assets/mars/models/item/carbon_scrubber.json`. No transparency, no render type needed.

## Registration list (Technical Lead; not done here)

1. Block `carbon_scrubber` with `facing` and `machine_state`; blockstate `facing=<d>,machine_state=<s>` -> `mars:block/carbon_scrubber_<s>`, `y` rotations north 0, east 90, south 180, west 270. Front = north face.
2. Item model exists; lang name and tooltip "Game-sized version of a real idea"; "Game invention, not real" journal line; loot table.
3. Shape: **one cell, collision and outline 24 px**.
4. Light level 0; `MachineLook` mapping as the other machines.
5. Wiring (Phase 3 unlock, CO2 into rock, no O2) is the Technical Lead's and Game Designer's.
6. Checked by `TextureSpecTest`.

## Not verified
- 3D look: the crossed-box cylinders and uv crops were only range-checked and seen flat; the valve block hides the lower third of both beds from the front by design.
- Colour-vision simulation not run; cycling animation not seen moving.
