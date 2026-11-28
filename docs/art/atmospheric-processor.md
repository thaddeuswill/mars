# Atmospheric processor: model and texture spec (first pass)

**Owner:** Lead Artist · **Date:** 2026-11-28 · **Status:** model and textures written and checked by script (JSON refs, uv ranges, textures exist); REGISTERED 2026-11-28 by the Technical Lead (block, blockstate, item, lang, loot, recipe, 3 units/sol draw, gate Phase 2 begins; seen in a real client, `tools/screenshots/out/20261128-013010/phase2_*.png`; ports on the east face not seen); not rendered in 3D (no Blender here, block not registered) · **Reference:** MOXIE gold-wrapped box for the split step; adsorption CO2 compressor and Sabatier "Atmospheric Processing Module" for intake (`docs/science/machine-references.md` row "Atmospheric processor"; NTRS 20000121346, 20170007818; repo photos `docs/art/references/PIA24201.jpg`, `PIA24203.jpg`). Copied from the references: gold-wrapped body, intake grille, two output ports with tubing, a raised filter canister, lander-style feet. **Size, band, plate and every number are invented.** Wears the made-up band (`docs/art/made-up-band-spec.md`): it is a game-sized, game-wired version of a real idea, and the real MOXIE look is only borrowed.

Silhouette cue: squat gold box on four feet, a canister bump on top, two ports on one side.

**Deviation from the look brief (decision for the Technical Lead, easy to reverse):** the brief said 2 x 2 footprint, 2 high. This pass is **one block, 24 px high (1.5 blocks)**, so it registers like the Oven and needs no multiblock. If you want 2 x 2, tell me and I split the model into four.

| Part | Elements (px, from-to) | Texture (`pack/assets/mars/textures/block/`) |
|---|---|---|
| Skirt with made-up band | x2-14, y0-2, z2-14 | `atmospheric_processor_base.png` (band rows 14-15, uv v 14..16) |
| Four lander feet | 2 x 2 px at the corners, y0-2 | `atmospheric_processor_foot.png` |
| Gold-wrapped body (grille, lamp, plate on north face) | x1-15, y2-14, z1-15 | `atmospheric_processor_front.png` (north), `atmospheric_processor_side.png` |
| Lid | x2-14, y14-15, z2-14 | `atmospheric_processor_top.png` (canister socket drawn on it) |
| Raised filter canister | x9-13, y15-23, z3-7 | `atmospheric_processor_canister.png` |
| Two output ports (east face, alloy) | x15-16, y5-9, z3-6 and z10-13 | `atmospheric_processor_port.png` |
| Tubing between the ports (salt-crust) | x15-16, y6-8, z6-10 | `atmospheric_processor_tube.png` |

Colours: gold ramp `#5C4320 #7E5E2C #A3823F #C9AE6A`; alloy `#3B4148 #59616A #7E8790 #A8B0B7`; intake lip orange `#E69F00` (the touch part); grille slots `#1E2328`; lamp hollow square `#56B4E9` (standby, style guide 3.1); band `#CC79A7` / `#8E3F6C`; plate `#CC79A7` with `#2A2420` slash at x10-13, y9-12. Grille, lamp, band and plate never get dust. Dust stages are not drawn yet (brief section 1: top and canister take dust, gold stays visible at stage 3).

Model: `pack/assets/mars/models/block/atmospheric_processor.json` (parent `block/block`, front = north face, `facing` by `y` rotation like the Oven). Generator: `tools/art-preview/gen_invented_machines.py` (`processor()`), rerunnable.

## Registration list (Technical Lead; not done here)

1. Block `atmospheric_processor` with `facing` (copy `regolith_oven` / the reclaimer registration).
2. Blockstate `pack/assets/mars/blockstates/atmospheric_processor.json` (copy `regolith_oven.json`).
3. Item model (parent `mars:block/atmospheric_processor`).
4. Lang key and a tooltip that says it is a game-sized, game-wired idea based on MOXIE and Sabatier hardware (not real size or output; **not** "game invention" only: the real idea is the point).
5. Loot table; shape and collision 24 px high (1.5 blocks), feet are inside the 16 x 16 footprint.
6. Gate wiring: unlocked by "Phase 2 begins"; it carries pressure 5 to 30 kPa. **No rate exists in the balance sheet** (`docs/design/machine-priorities.md`): Game Designer must supply one before it does anything.
7. Light level 0, no transparency needed (all faces opaque).
8. Check against `mod/src/test/java/mars/art/TextureSpecTest.java` (all eight textures 16 x 16; band on the base; plate on the front).

## Not verified
- 3D look: uv crops and element positions were only range-checked. The first screenshot after registration is the real test.
- Gold creases may read slightly "woody" at a distance; the lit ridge pixels carry the foil idea. Owner look.
- Colour-vision simulation for band and lamp: not run.
