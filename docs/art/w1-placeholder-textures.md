# W1 placeholder textures

**Owner:** Technical Lead (placeholders only) · **Real art:** Lead Artist · **Written:** 2026-11-27

The four W1 terrain blocks ship with **placeholder** 16 x 16 textures, drawn from scratch by a script (no vanilla pixels), so the blocks exist and can be told apart. They are not art. The Artist replaces the PNGs (same file names, no code change).

| Block | File | Placeholder look | What the final art should show (SME `docs/science/mars-terrain-reference.md` section 3) |
|---|---|---|---|
| Basaltic Sand | `pack/assets/mars/textures/block/basaltic_sand.png` | dark grey-brown speckle | Dark chocolate to grey-brown, almost black in dunes (olivine and pyroxene, Curiosity report, verified) |
| Hematite Ore | `hematite_ore.png` | grey stone with rust-red blobs | Rust-red and grey ore in basalt; hematite at Meridiani (SME R5) |
| Dark Sand Ore | `dark_sand_ore.png` | dark red-brown speckle | Dark red-brown sand, not white quartz or silica (SME decision, `world-and-blocks.md` 0.1 item 1) |
| Ground Ice | `ground_ice.png` | pale blue-white with cracks | White to pale ice, dusty (water ice under regolith) |

Block shapes are plain cubes (`cube_all`). Names are in `pack/assets/mars/lang/en_us.json`.
