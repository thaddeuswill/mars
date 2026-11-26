# Report: Lead Artist, Phase 1 batch 1 straw-man assets (BACKLOG #19)

- **Role:** Lead Artist
- **Date:** 2026-11-26
- **Outputs:** `tools/art-preview/gen_phase1_batch1.py`, `tools/art-preview/render_model.py` (extended), `tools/art-preview/render_batch.py` (new), `pack/assets/mars/` textures (21), block models (7), item models, blockstates; `docs/art/renders/phase1-batch1/contact_sheet.png` and per-model views and silhouettes; `docs/art/asset-list.md` and `docs/art/style-guide.md` §2.2 updated.
- **Scope:** six dome machines (Regolith Outgasser, Regolith Oven, solar panel, battery, O2 generator, Planetary Monitor) plus a dusty solar panel variant and a regolith texture, all straw-man placeholders (fidelity code SM). The renders come from Blender, not Minecraft.

## Risk gate
| Risk | Check | Result |
|---|---|---|
| Faces upside down or mirrored | Test cube with an arrow and an "L" on all six faces | Side faces right; the top view was rotated 180°, fixed and re-checked |
| Textures stretched | Explicit UVs the size of each element face; every render looked at | None seen |
| Colors off palette, too many, pure black or white | `gen_phase1_batch1.py` checks every pixel and exits non-zero | Pass |
| Low contrast on things that carry meaning | WCAG ≥ 3:1 for meaning pairs; running-lamp pixels inside a dark ring | First run failed 5 checks plus 13 lamp pixels; all fixed |
| Broken JSON or missing files | Every reference resolves; UVs within 0–16; allowed rotations | Pass (7 models, 21 textures) |

## What was fixed by looking (three render passes)
1. Ground showed magenta placeholder corners (painted out on a render-only copy); silhouette test now uses the front view at a fixed scale; one placeholder corner per texture.
2. Solar panel tilt flipped so cells face the front; the Monitor's screen lowered to stay inside one block.
3. Dusty panel, three tries: blotches read as holes, whole-grid silt read as a tan lattice, final short darker-redder streaks plus a drift along the low edge (streak highlight 4.0:1).
4. Contrast: lamps now in a dark ring (3.0:1); heater slots on a dark door (4.6:1); battery segments in a dark well with the empty segment hollow (filled vs. empty is a shape difference).

## What the renders show and don't
- The Outgasser and the Oven are easy to tell apart; battery terminals, the O2 generator's twin tanks, and the Monitor's tilted screen read.
- **Cannot tell us:** anything about the game itself (no smooth lighting, ambient occlusion, day/night, fog, real distance or field of view, vanilla neighbors, mipmapping, or GUI scale). Shading follows Minecraft's per-face factors as the Artist understands them (not verified). Box machines have plain box silhouettes, so the 32×32 silhouette rule only really tests mobs and odd shapes. Only the Artist has judged them; this is not a playtest.

## Handoffs
- **Technical Lead:** the blockstates assume a horizontal `facing` property and the blocks aren't registered in the mod yet; register the 7 blocks with `facing` (or say to switch to one variant), then add them to the screenshot script for an in-game shot at 1280×720.
- **Coordinator:** show the contact sheet to the owner; collect feedback for batch 2 in BACKLOG #19.

## Shared model updates
- **Tool:** `tools/art-preview/` generates and renders straw-man block assets in about 3 seconds per view, with numeric checks that fail the run.
- **Lesson:** the numeric checks and the eye caught different problems. The checks found the contrast failures; only looking found the upside-down top view, the dust that looked like holes, and the panel tilted the wrong way. Do both.
