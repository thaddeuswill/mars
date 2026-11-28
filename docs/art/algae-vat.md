# Algae vat: model and texture spec (first pass)

**Owner:** Lead Artist · **Date:** 2026-11-28 · **Status:** model, textures and bubble animation written and script/unit-checked; NOT rendered in 3D and not seen in a real client (flat orthographic views and frame strips were viewed: `docs/art/renders/vat-scrubber-sheet.png`) · **Real ideas:** Veggie and Advanced Plant Habitat (lit, sealed growth chamber; `docs/science/machine-references.md`, NTRS 20160005059 / 20160005065, verified there) and the algae photobioreactor idea (glass tank, light, bubbled gas; general knowledge, **not verified** against a source here). **Size, band, plate, colours and every number are invented.** Wording class: **Game-sized version of a real idea.** Wears the made-up band (`docs/art/made-up-band-spec.md`).

**Look rules kept:** the culture is a **dark amber-brown** (`#3E2412` to `#9A6A30`), redder than the processor's gold; **no green anywhere** (test-checked, also in animation frames) and no Earth-plant shapes (no leaves, no pot, no soil); the small lamp is the usual pale-blue status ring; the **grow lamp** is a warm-white strip (`#F3EBC8` lit) kept away from purple (band) and from the greenhouse LED pink.

Silhouette cue: a **hooded glass tank**: low plinth, tall window, wide overhanging lamp hood on top, and one thin air line with an elbow on the hood (the only overhang, 28 px). Distinct from the reclaimer (two-tier rack with posts), processor (squat gold box), emitter (wide tanks and flare), extractor (spindly tripod mast, lopsided pump) and oxygen maker (low box). Flat silhouette views were compared: viewed, OK.

| Part | Elements (px, from-to) | Texture (`pack/assets/mars/textures/block/`) |
|---|---|---|
| Plinth with band, lamp ring and plate on the north face | x1-15, y0-8, z1-15 | `algae_vat_base` (sides, uv v 8..16); `algae_vat_front` (north: lamp frame x3-7 y9-13, plate x10-13 y9-12, band rows 14-15) |
| Tank (glass front, culture inside) | x2-14, y8-20, z3-13 | `algae_vat_glass` (north, uv 2,2,14,14), `algae_vat_tank_side` (other three sides) |
| Lamp hood (grow-lamp strip on its north face) | x1-15, y20-24, z2-14 | `algae_vat_hood` (north), `algae_vat_hood_side`, `algae_vat_top` |
| Air line: riser and elbow (overhang) | x11-13, y24-26, z10-12 and x5-13, y26-28, z10-12 | `algae_vat_pipe` |

All elements stay inside x0-16, z0-16 (test-checked). Model height **28 px** (the air line); the **hood top is 24 px**, which is the collision and outline height. Dust stages: not drawn yet (hood top takes dust; band, plate, lamps never).

## Running state (extends `docs/art/running-state-table.md`)

| State | Status ring (plinth) | Grow-lamp strip (hood) | Culture window |
|---|---|---|---|
| off | dark | dark (`#1E2328`) | static |
| idle | hollow ring | dashed: 2 px lit dim (`#8D8870`), 2 px dark | static |
| running | solid ring | solid lit (`#F3EBC8`) | **bubbles rise**: pale dots (`#D8E4EA`) in three columns, 4 frames x 8 ticks (INVENTED), never interpolated |

Shape carries the state (nothing / dashes / solid) and motion carries "working". Frame counts, ticks and the bubble columns are INVENTED.

## Models (`pack/assets/mars/models/block/`)

| File | Use |
|---|---|
| `algae_vat` | base = idle look |
| `algae_vat_off` | ring and strip dark |
| `algae_vat_idle` | = base |
| `algae_vat_running` | solid ring, solid strip, `algae_vat_glass_running` (animated) |

Item model `pack/assets/mars/models/item/algae_vat.json` (parent `mars:block/algae_vat`) written. No texture has transparency, so **no render type is needed**. Generator: `tools/art-preview/gen_vat_scrubber.py` (rerunnable, exits non-zero on a failed check); flat views: `tools/art-preview/flat_views.py`.

## Registration list (Technical Lead; not done here)

1. Block `algae_vat` with `facing` and `machine_state` (off, idle, running); blockstate variants `facing=<d>,machine_state=<s>` -> `mars:block/algae_vat_<s>`, same `y` rotations as `regolith_oven.json` (north 0, east 90, south 180, west 270). Front = north face.
2. Item model exists. Lang: block name, tooltip "Game-sized version of a real idea" plus "Game invention, not real" journal line (made-up-band rule); loot table.
3. Shape: **one cell, collision and outline 24 px high** (the air line overhangs to 28 px, like the extractor).
4. Light level 0. `MachineLook` mapping as the other machines (off = needs power, idle = low power or no job, running = running).
5. Wiring (Hydrosphere gate, CO2 down / O2 up, +20 BU biomass) is the Technical Lead's and Game Designer's.
6. Checked by `TextureSpecTest` (opaque, 16 x 16, no green, band, plate, lamp shapes, strip, one cell, height).

## Not verified
- 3D look and the 2 px glint; the 1 px hood overhang may z-fight nothing (no coplanar faces), first screenshot decides.
- Colour-vision simulation not run; bubbles and strip animation not seen moving.
- Photobioreactor reference is general knowledge, not an opened source.
