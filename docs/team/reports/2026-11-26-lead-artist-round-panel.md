# Report: Lead Artist, round real-scale solar array prototype (BACKLOG #25)

- **Role:** Lead Artist
- **Date:** 2026-11-26
- **Outputs:** `tools/art-preview/gen_round_panel.py`; in `pack/assets/mars/`: models, item models, blockstates, and 48×48 textures for `solar_panel_round_dust0`–`dust4` and `solar_panel_round_partly_cleaned`; renders in `docs/art/renders/round-panel-prototype/` (`dust_progression_round.png`, `compare_with_photos.png`, `contact_sheet.png`); the viewer has a "Solar panel, round (prototype)" button with the dust slider and a 1.8 m player box. The square panel assets are untouched until the owner picks. Not checked: anything inside Minecraft.

## Result
A round, real-scale, InSight-style array works with plain vanilla block models: a flat "pixel disc" of 21 thin boxes plus a post and base (23 elements). From above it reads as a 12-segment fan, and all five dust stages plus "partly cleaned" carry over.

## What Minecraft allows (official NeoForge 1.21.1 docs, read by the Artist)
| Question | Answer |
|---|---|
| Element `from`/`to` range | **[−16, 32]** (3 blocks wide); going outside [0, 16] is "highly discouraged" because of lighting and culling issues |
| Element rotation | **−45 to 45 in steps of 22.5 degrees**, about one axis per element |
| Whole-model rotation | NeoForge adds a root `transform` (translation, any rotation, scale) applied to all of a model's geometry |
| Custom geometry | **Yes, built in:** `neoforge:obj` loads Wavefront .obj models with arbitrary shapes; also `neoforge:composite`, `neoforge:elements`, custom loaders in Java |
| minecraft.wiki | Not reachable; not read |

**Untested:** whether each part of a `neoforge:composite` model can carry its own root `transform` (that would allow 12 segments each rotated 30°); the OBJ loader's behaviour in our build.

## Options compared
| Option | Elements | How round | Problems |
|---|---|---|---|
| Octagon (crossed boxes) | 2–3 | Reads as a stop sign | Overlapping faces flicker; 8 sides don't match 12 segments |
| 16-gon from 8 bars at 22.5° steps | 8 | Fairly round | Bars overlap, each shows its own texture so seams can't line up; 22.5° steps don't fit 30° segments |
| **Pixel disc of flat strips (chosen)** | 21 + post + base = 23 | Stair-step edge at 1 px = 6.25 cm; reads as a round fan from above | No rotation or overlap; one continuous 48×48 texture so seams, rows, cut-outs, and the dust map line up |
| Pixel disc with 2 px rows | about 11 | Coarser edge | Cheaper |
| `neoforge:obj` 12-gon fan | 1 mesh | Truly polygonal, can tilt | Needs .obj and .mtl files (no Java); smooth edges sit oddly next to pixel art; Technical Lead to cost and test |

## What was built and fixed (four looks at the renders)
Size: 34 px across (about **2.1 m**, provisional), 1 px thick, top at 0.69 m, on a post; one 48×48 texture covers the 3×3-block area (16 px per metre like every other asset). Fixes: a true circle with ring seams looked like a target, so the outline is now a **12-sided polygon** with cell rows **parallel to each segment's outer edge**, as in PIA22876; thick grey spokes looked like a wheel; spokes drawn by angle came out dotted, so there are now 12 continuous 1 px radial seams (light when clean, dark under dust); the full veil's highlights were reduced so it reads as even, with one dark cut-out per segment. Dust gathers first along the rim; every stage keeps the previous stage's dust. At stages 3–4 the seams are **softened to a 1 px dark tan-brown `#7A4530`** (2.2:1 on the veil); cut-outs stay dark (5.0:1); this applies to the round prototype only. Checks all pass: dust steps 0.067, 0.138, 0.127, 0.040 (OKLab ΔE on the cell area); structure contrast 3.2:1 or more; at most 6 colours per stage; palette colours only.

## Still off compared with the photos
Real seams and cell rows are much finer (every seam here is about 6 cm wide); the real array sits on a lander deck at about 1 m (unsourced), ours on a post; the real rim is thinner; the clean array's cut-outs look lighter in PIA22876 (ours are dark); in the viewer's perspective view the player box looks large (the front view is the fair size check).

## Limits and costs
- **Space:** the disc overhangs its block by about 1 m on every side. Neighbouring blocks would cut through it, and the hitbox and selection outline stay 1×1 unless the Technical Lead adds a custom shape or a 3×3 footprint rule.
- **Lighting:** the docs warn that geometry outside the block can light and cull wrongly; this could not be seen in a Blender preview.
- **Cost:** about 23 elements, roughly 80–130 faces per panel; a field of 30 panels is a few thousand faces (the Artist's estimate, not measured).

## Decisions needed (classified by coordinator)
| Decision | Recommendation | Type |
|---|---|---|
| Round (prototype) or square for the game panel | Round: it matches the real array and reads well from above | Ill-structured (accuracy and appeal against footprint cost) |
| Which way to build the round array: vanilla pixel disc, `neoforge:obj`, or composite with transforms; is a custom renderer worth it? | Technical Lead costs and tests all three with in-game screenshots; the Artist sees a custom renderer as worth it only for folding animation | Ill-structured |

## Handoffs
- **Technical Lead:** cost the three approaches; footprint and hitbox handling for a 3×3 overhang; an in-game lighting check under the overhang. Done when one approach is recommended with in-game screenshots.
- **Subject Matter Expert:** replace the provisional numbers (2.1–2.2 m, 12 segments, 0.69 m height) from `docs/science/dimensions.md` (now available: 2.2 m, about 12 lobes by eye); check whether clean cut-outs are dark or light.
- **Owner/Producer:** choose round or square; then the Artist retires the other and applies the softened seams to the chosen set.

## Shared model updates
- **Fact (cited):** NeoForge 1.21.1 element range is [−16, 32]; element rotation is limited to ±45° in 22.5° steps; `neoforge:obj` gives arbitrary geometry with no code.
- **Lesson:** drawing at real scale showed the InSight array is a 12-sided polygon with cell rows parallel to each edge, not a circle with rings. Compare renders to the photos, not to memory.
