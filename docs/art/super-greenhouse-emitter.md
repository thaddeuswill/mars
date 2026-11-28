# Super-greenhouse gas emitter: model and texture spec (first pass)

**Owner:** Lead Artist · **Date:** 2026-11-28 · **Status:** written and script-checked; REGISTERED 2026-11-28 by the Technical Lead (one cell, 24 px collision, seen in a real client, `tools/screenshots/out/20261128-013010/phase2_*.png`); not rendered in 3D · **Reference:** **none.** There is no real machine (SME `docs/science/machine-references.md`, NOT VERIFIED as anything planned or tested). It is plainly fictional: a generic chemical-plant vent stack. Carries the made-up band; the tooltip must say "Game invention, not real". Every size and colour is invented.

Silhouette cue: stack with a flared top rising from two stacked tanks; orange hazard stripe at the stack base.

**Deviation:** one block, 32 px high (2 blocks; the brief's 2 x 2 x 3 does not fit one model, the model limit is 32 px). The Technical Lead may make it a two-block-tall block.

| Part | Elements (px) | Texture |
|---|---|---|
| Plinth with made-up band | x1-15, y0-2, z1-15 | `super_greenhouse_emitter_base.png` |
| Lower tank (north face: valve box, orange valve wheel, lamp, plate) | x2-14, y2-8, z2-14 | `super_greenhouse_emitter_tank.png`, `..._front.png` |
| Upper tank | x3-13, y8-13, z3-13 | `..._tank.png` |
| Pipe gantry posts | x3-4 and x12-13, y13-22, z3-4 | `..._tank.png` |
| Vent stack, hazard stripe on the lower 6 px | x6-10, y13-29, z6-10 | `super_greenhouse_emitter_stack.png` |
| Flared top | x5-11, y29-32, z5-11 | `super_greenhouse_emitter_flare.png` |
| Tank/plinth top | | `super_greenhouse_emitter_top.png` |

Colours: alloy ramp as the processor; hazard `#E69F00` on `#59616A` at 45 degrees (shape carries the danger, not hue; vermillion is not used); lamp `#56B4E9` hollow square; plate x10-13, y6-9 of the front texture. Vent glow `#F0E442` and the taller plume (animation frame 2 in the brief) are **not drawn**. Dust stages not drawn.

Registration list for the Technical Lead: block with `facing`, blockstate, item model, lang with "Game invention, not real", loot, shape 32 px, gate wiring (Thaw, Phase 3 begins; no rate in the balance sheet yet). Generator: `emitter()` in `tools/art-preview/gen_invented_machines.py`. Not rendered: **unverified**.
