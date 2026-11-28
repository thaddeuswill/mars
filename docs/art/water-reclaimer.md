# Water reclaimer: model and texture spec (first pass)

**Owner:** Lead Artist · **Date:** 2026-11-28 · **Status:** model and textures written, NOT registered, not seen in-game or rendered (Blender is not installed here; only the flat textures were viewed) · **Reference:** ISS Water Recovery System, NTRS 20170008948 (`docs/science/machine-references.md`). Size, look and every number are **invented** for the game. Carries the made-up band (`docs/art/made-up-band-spec.md`).

Silhouette cue: tall two-tier rack (tank low, canister high) with a clear tube on the east side and an orange bucket spout at the front.

| Part | Elements (px, from-to) | Texture (`pack/assets/mars/textures/block/`) |
|---|---|---|
| Plinth with made-up band | x1-15, y0-2, z1-15 | `water_reclaimer_base.png` (band rows 14-15, uv v 14..16) |
| Lower tank (water window, lamp, plate on north face) | x2-14, y2-14, z2-14 | `water_reclaimer_front.png` (north), `water_reclaimer_side.png` |
| Four frame posts | 2 x 2 px each, y14-30 | `water_reclaimer_frame.png` |
| Upper filter canister | x5-11, y15-28, z5-11 | `water_reclaimer_canister.png` |
| Top plate with hatch | x2-14, y28-30, z2-14 | `water_reclaimer_top.png` |
| Clear tube (pale, ribbed, opaque on purpose) | x14-16, y4-28, z7-9 | `water_reclaimer_tube.png` |
| Bucket spout, safety orange `#E69F00` | x6-10, y4-6, z0-2 | `water_reclaimer_spout.png` |

Model: `pack/assets/mars/models/block/water_reclaimer.json` (parent `block/block`, front = north face, `facing` variants via `y` rotation like the Oven). It is **30 px high (1.9 blocks)**: the Technical Lead decides one block with a 2-high shape or a two-block pair. Water level is shown by shape: a stepped surface (3 px steps) in the front window, water color `#9FB0BC`. Lamp is a hollow square (standby, style guide 3.1) in a dark ring; other lamp states are not drawn yet. Dust stages: not drawn (4-frame overlay still to do, look brief section 1).

**Technical Lead must register (not done here):** block `water_reclaimer` (with `facing`), blockstate (copy `regolith_oven.json`), item model (parent `mars:block/water_reclaimer`), lang key, loot table, shape/collision for 1.9 blocks high, and the tooltip "Game-sized version of a real idea" (not real size). Not translucent: if the tube should look like glass, give it a translucent render type later.
