# Made-up band: spec and reusable overlays

**Owner:** Lead Artist · **Date:** 2026-11-28 · **Status:** built, seen only in flat previews; not yet on a registered machine · **Source rule:** `docs/art/invented-machines-look.md` section 1 · **Generator:** `tools/art-preview/gen_invented_machines.py` (rerunnable, exits non-zero on a failed check)

The made-up band tells the player "this machine is a game invention, not real hardware". Real hardware (solar array, MOXIE-style Oxygen maker) never gets it. The tooltip and journal carry the words "Game invention, not real" (world-and-blocks 3.2); the band is the in-world cue. Purple is the team's Fiction color (style guide contrast table, badge U-10) and appears on no terrain, flora, fauna or real hardware.

## Exact values

| Part | Spec |
|---|---|
| Band | Bottom 2 rows of every side face (rows y 14 and 15 of a 16 x 16 texture). Row 14 `#CC79A7`, row 15 dark edge `#8E3F6C` |
| Dash | 2 px on, 2 px off, starting "on" at x 0 (dashes at x 0-1, 4-5, 8-9, 12-13). The pattern wraps cleanly around the four sides of a 1 block machine |
| Plate | 4 x 4 px square `#CC79A7` with a 1 px `#2A2420` slash from bottom-left to top-right. Standard place on a front texture: x 10..13, y 9..12 (below the status lamp) |
| Never gets dust | Band and plate stay `#CC79A7` through all dust stages (dust rule in the look brief) |
| Colorblind | Meaning is carried by shape (dashes, slash), not hue. Not yet run through the colour-vision simulation in `gen_phase1_batch1.py`: **unverified** |

## Files (all 16 x 16 RGBA, `pack/assets/mars/textures/block/`)

| File | Content | Use |
|---|---|---|
| `made_up_band.png` | Band only, transparent elsewhere | Overlay: bake it, or draw it as a second thin cuboid (0.01 px proud) with this texture |
| `made_up_plate.png` | Plate only at x 10..13, y 9..12 | Same, on the front face |

Two ways to use them (Technical Lead's choice per machine; the water reclaimer **bakes** the band and plate into its textures, so it needs neither):

1. **Bake** (recommended, no extra faces): the generator functions `band(img)` and `plate(img, x, y)` paint the pixels into a texture. A plinth element 2 px tall uses uv `[0,14,16,16]` of a texture that has the band on rows 14 and 15.
2. **Overlay element**: add a cuboid slightly larger than the plinth with the band texture on its four side faces. Needs a translucent or cutout render type for the clear pixels (a registration choice).

## Rules for the next machines

- Band on every side face of the machine's lowest element, plate on the front face. If the front face is crowded, the plate may move but stays 4 x 4 and keeps the slash.
- The band is the only purple on the machine. The greenhouse LED strip is `#D9A0C8` and is kept pale and strip-shaped to stay apart from this.
- Checked by `mod/src/test/java/mars/art/TextureSpecTest.java` (files exist, 16 x 16, dash pattern).
