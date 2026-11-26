# Art preview (Blender)

Renders Minecraft block-model JSON to PNG so the team can look at art and iterate before anything is loaded in the game. Owner: Lead Artist.

**What it is not:** it is not Minecraft. It shows no world, no ambient occlusion, no smooth lighting, no day/night, no animation, no entity models, and no GUI. Use it to judge **shape, proportion, face orientation, and texture readability**. Final judgment happens in the game.

## Setup
Blender is installed per sandbox, not kept in the repo:
```bash
apt-get install -y --no-install-recommends blender   # Ubuntu 24.04 package: Blender 4.0.2
```

## Files
| File | What it does |
|---|---|
| `render_model.py` | Renders one block model in one view (Blender, headless) |
| `render_batch.py` | Renders a list of models (iso with ground and a player-size box, front, top, 32×32 front silhouette) and composes a labeled contact sheet |
| `gen_round_panel.py` | **Prototype** (BACKLOG #25): the round, real-scale InSight-style array as a pixel-circle disc of flat cuboids (no rotated elements), one 48×48 texture per dust stage covering the 3×3 block area so texel density stays 16 px per metre; same dust checks. Large models: `render_model.py` now frames the whole model and puts the player box beside it; the viewer does the same |
| `gen_phase1_batch1.py` | Generates the Phase 1 straw-man assets (batch 1, plus batch 2: the MOXIE-style Oxygen maker and the five solar panel dust stages with the partly cleaned state). Dust checks: each stage is a superset of the last, each step changes the cell area by OKLab ΔE ≥ 0.035 (normal and 2 of 3 colour-vision simulations), and the panel structure stays readable. Also checks gold against the hazard accents in all simulations. Generates the textures, block models, item models, and blockstates into `pack/assets/mars/`, from palette colors only, and runs the numeric checks (palette-only, ≤ 6 colors per face, no pure black or white, contrast of meaning-bearing pairs ≥ 3:1, every lamp pixel inside a dark ring). Exits non-zero on any failure |

## Use
```bash
python3 -I tools/art-preview/gen_phase1_batch1.py                      # textures, models, checks
python3 -I tools/art-preview/render_batch.py docs/art/renders/phase1-batch1 regolith_outgasser battery
blender -b -P tools/art-preview/render_model.py -- <model.json> <texture_dir> <out.png> [iso|front|top|side|back] \
        [--shading mc|lit] [--ground <tex or .png>] [--sky <hex>] [--ref] [--transparent] [--size <px>]
```

## How it draws
- **Shading `mc` (default):** unlit faces with Minecraft's fixed per-face brightness (up 1.0, north/south 0.8, east/west 0.6, down 0.5). Colors on screen are the texture colors times that factor, so contrast judgments match the game's flat look more closely than lit renders. `lit` uses a Blender sun (soft shadows that the game doesn't have).
- **Supported model features:** cuboid elements; per-face `uv` (explicit, or Minecraft's default); face `rotation`; element `rotation` (origin, axis, angle). **Not supported:** `parent` resolution (render the model that has the elements), `display` transforms, tint indexes, `shade: false`, rescale.
- **Orientation:** front = the model's north face (Minecraft's convention for `facing=north`). Top view has north at the top of the image. Checked with an asymmetric test texture on all six faces (2026-11-26).
- **Background:** the 0.6 kPa horizon color `#D2AE86` (keyframes §3.1) by default. The ground plane is a render-only copy of the regolith straw-man with its placeholder marker painted out.
- Cycles on CPU, no denoiser (this Blender build has none). About 3 s per view.

## Interactive viewer (browser)
`python3 tools/art-preview/build_viewer.py` builds `docs/art/viewer/index.html` from `viewer_template.html`: a single page (three.js from jsDelivr, pinned 0.158.0; models and textures embedded) with orbit, zoom, view presets, an all-machines lineup, a Mars ground, a player-for-scale box, and a turntable. `--fragment` writes the form the Artifact tool wants. It draws models the same way as the Blender script (same corner order, UVs, per-face brightness). Tested headless in Chromium with software GL; not Minecraft.

## Browser viewer
`build_viewer.py` builds `docs/art/viewer/index.html` (and `--fragment`) from `viewer_template.html`. Textures are flagged sRGB in three.js (without that, colours showed washed out in a headless Chrome check). Picking the solar panel shows a **Dust level** slider (0–4) and a **Partly cleaned** button that swap the model live; the overview uses dust 0.

## Known limits
- Fixed orthographic cameras; no player-eye perspective.
- Silhouette test uses the front view at a fixed scale (1 block = 32 px). For box-shaped machines the silhouette is just a box; their identity is carried by the front face, not the outline.
- Test renders in `docs/art/renders/test/` use a throwaway model and are not project assets.
