# Art preview (Blender)

Renders a Minecraft block-model JSON to PNG so the team can look at it and iterate before anything is loaded in the game.

**Status:** proof of concept (2026-11-26). It supports cuboid `elements` with per-face textures (`#name`), nearest-neighbour pixels, and three views (`iso`, `front`, `top`).

**What it is not:** it is not Minecraft. Blender's light and shadow differ from Minecraft's fixed per-face shading, and it doesn't show blocks in a world, animations, entity models, or the GUI. Use it to judge shapes, proportions, and texture readability. Final judgment happens in the game.

## Setup
Blender is installed per sandbox, not kept in the repo:
```bash
apt-get install -y --no-install-recommends blender   # Ubuntu 24.04 package: Blender 4.0.2
```

## Use
```bash
blender -b -P tools/art-preview/render_model.py -- <model.json> <texture_dir> <out.png> [iso|front|top]
```
Textures are looked up as `<texture_dir>/<name>.png`, where `<name>` is the value in the model's `textures` map. Cycles on CPU, no denoiser (this Blender build has none), 640×640.

## Known limits
- Cuboids only (no rotated elements, no `display` transforms, no tint indexes).
- The camera and light are fixed.
- Test renders in `docs/art/renders/test/` use a throwaway placeholder model and generated textures. They are not project assets.
