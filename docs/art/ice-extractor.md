# Ice extractor: model and texture spec (first pass)

**Owner:** Lead Artist · **Date:** 2026-11-28 · **Status:** model, textures and running animation written and script/unit-checked; NOT rendered in 3D and not seen in a real client (flat texture sheets and a flat front/side silhouette plot were viewed) · **Reference:** Rodriguez Well (NTRS 20205011353; `docs/science/machine-references.md` row "Ice extractor"). Copied from the idea: a tall heated drill sleeve in a tripod base, a pump on top, an insulated hose to a collection tank. **Size, band, plate and every number are invented.** Wears the made-up band (`docs/art/made-up-band-spec.md`): a game-sized version of a real idea. No green anywhere (test-checked).

Silhouette cue: spindly tripod, one thin 32 px mast, an **east-offset pump box** on top (lopsided L from the front; the emitter's flared stack is centred and symmetric), a low tank at the back-east. Distinct from the emitter (wide tanks, flare), processor (squat gold box) and reclaimer (two-tier rack with frame posts). Flat silhouette plot compared all four: viewed, OK.

| Part | Elements (px, from-to) | Texture (`pack/assets/mars/textures/block/`) |
|---|---|---|
| Plinth with made-up band | x1-15, y0-2, z1-15 | `ice_extractor_base` (band rows 14-15, uv v 14..16) |
| Pump skid (lamp + plate on the north face) | x2-14, y2-8, z1-4 | `ice_extractor_front` (north, uv 2,2,14,8), `ice_extractor_side` |
| Tripod legs, 2 steps each (west, east, south) | 2 x 2 px, foot y2-7, brace y7-12 | `ice_extractor_leg` |
| Heater collar, orange fins | x5-11, y10-16, z5-11 | `ice_extractor_heater` |
| Drill sleeve | x6-10, y16-32, z6-10 | `ice_extractor_sleeve` (running: `ice_extractor_sleeve_running`, animated) |
| Pump box (offset east) | x6-14, y26-32, z5-11 | `ice_extractor_pump`, `ice_extractor_top` |
| Collection tank (back east) | x10-15, y2-8, z9-15 | `ice_extractor_tank`, `ice_extractor_tank_top` |
| Hose (out of the collar, down into the tank), salt-crust | x11-14, y10-12, z9-11 and x12-14, y8-12 | `ice_extractor_hose` |
| Vent stub | x13-15, y8-10, z13-15 | `ice_extractor_leg` |
| Steam puff (running only, cutout) | x12-16, y10-16, z12-16, 4 sides | `ice_extractor_steam` (animated) |

All elements stay inside x0-16 and z0-16 (checked by `TextureSpecTest`). Height **32 px (2 blocks) model overhang**, like the emitter. Colours: alloy ramp; heater fins orange `#E69F00` (2 px, shape = horizontal fins); hose `#E7E2D6`; tank `#9FB0BC` with alloy hoops; melt glow `#FFD27A` on `#B37400` (same warm tone as the oven coil, so heat reads as heat); steam `#E4E8EA` / `#C2CDD5`; band `#CC79A7` / `#8E3F6C`; plate `#CC79A7` with `#2A2420` slash at x9-12, y3-6 of `ice_extractor_front` (lamp frame x3-7, y2-6). Band, plate and lamp never get dust. Dust overlay stages: not drawn yet (top of the pump box and tank take dust, per the look brief).

## Running state (extends `docs/art/running-state-table.md`)

Off: lamp dark (`ice_extractor_front_off`). Idle: hollow ring (`ice_extractor_front`). Running: solid lamp (`ice_extractor_front_running`) + two moving parts: a 2 px **melt-glow ring climbs the drill sleeve** (4 frames x 10 ticks, bottom to top) and a **steam puff** from the tank vent (4 frames x 10 ticks). Frame counts and ticks are INVENTED. Motion is the colour-blind-safe signal.

## Models (`pack/assets/mars/models/block/`)

| File | Use |
|---|---|
| `ice_extractor` | base model = idle look; the item uses it |
| `ice_extractor_off` | lamp dark |
| `ice_extractor_idle` | = base |
| `ice_extractor_running` | solid lamp, animated sleeve, steam puff element; `"render_type": "minecraft:cutout"` already set |

Item model `pack/assets/mars/models/item/ice_extractor.json` (parent `mars:block/ice_extractor`) is written too. Generator: `tools/art-preview/gen_ice_extractor.py` (rerunnable; writes only the files above).

## Registration list (Technical Lead; not done here)

1. Block `ice_extractor` with `facing` and `machine_state` (off, idle, running); blockstate variants `facing=<d>,machine_state=<s>` -> `mars:block/ice_extractor_<s>`, same `y` rotations as `regolith_oven.json` (north 0, east 90, south 180, west 270). Front = north face (the skid with lamp and plate).
2. Item model already exists (see above).
3. Shape: **one cell**, collision **24 px high** (1.5 blocks); the sleeve and pump box overhang to 32 px, like the emitter. Outline may be 24 px too.
4. The puff element needs the cutout render type: set in the running model; the block itself needs no `noOcclusion` for the idle/off models, but check the cutout element renders (unverified).
5. Light level 0. Lang key and tooltip "Game-sized version of a real idea" (not real size or output). Loot table.
6. Wiring (gate Thaw, ground-ice placement rule, 2 units/sol, +0.30 %/sol) is the Technical Lead's and Game Designer's; nothing here depends on it.
7. Checked by `TextureSpecTest` (band, plate, lamp shapes, no green, one cell, 32 px high, animated strips).

## Not verified
- 3D look: uv crops and element placement were only range-checked; leg steps may look like stairs; first screenshot after registration is the real test.
- Whether the lopsided pump box reads as a pump from 10 blocks away; owner look.
- Colour-vision simulation not run; the 2 px steam and glow animation not seen moving.
