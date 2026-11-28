# W1 placeholder textures

**Update 2026-11-28 (Lead Artist): all nine textures below are REPLACED by real pixel art** from `tools/art-preview/gen_terrain_textures.py` (rerunnable; palette ramps, checked for no green, no blue cast, opaque, 16 x 16). The placeholder generator `gen_w2_placeholder_textures.py` is obsolete: do not rerun it, it would overwrite the real art. New: `ripple_sand.png` (dune ripple block). Status table at the end of this file. The look descriptions below stay as the brief the art was drawn to.

**Owner:** Technical Lead (placeholders only) · **Real art:** Lead Artist · **Written:** 2026-11-27

The four W1 terrain blocks ship with **placeholder** 16 x 16 textures, drawn from scratch by a script (no vanilla pixels), so the blocks exist and can be told apart. They are not art. The Artist replaces the PNGs (same file names, no code change).

| Block | File | Placeholder look | What the final art should show (SME `docs/science/mars-terrain-reference.md` section 3) |
|---|---|---|---|
| Basaltic Sand | `pack/assets/mars/textures/block/basaltic_sand.png` | dark grey-brown speckle | Dark chocolate to grey-brown, almost black in dunes (olivine and pyroxene, Curiosity report, verified) |
| Hematite Ore | `hematite_ore.png` | grey stone with rust-red blobs | Rust-red and grey ore in basalt; hematite at Meridiani (SME R5) |
| Dark Sand Ore | `dark_sand_ore.png` | dark red-brown speckle | Dark red-brown sand, not white quartz or silica (SME decision, `world-and-blocks.md` 0.1 item 1) |
| Ground Ice | `ground_ice.png` | pale blue-white with cracks | White to pale ice, dusty (water ice under regolith) |

Block shapes are plain cubes (`cube_all`). Names are in `pack/assets/mars/lang/en_us.json`.

## W2 Tier A placeholders (added with the W2 surface blocks)

Five more blocks, same rules: 16 x 16, drawn by `tools/art-preview/gen_w2_placeholder_textures.py`, plain cubes, replace the PNG with the same file name. SME source: `docs/science/mars-surface-materials.md`.

| Block | File | Placeholder look | Final art should show | Where it generates |
|---|---|---|---|---|
| Duricrust | `pack/assets/mars/textures/block/duricrust.png` | pale tan-brown crust | Pale tan-brown cemented crust that breaks into plates (NTRS 20050203800, VERIFIED) | Layer 2 blocks under the dust (surface rule `planet.json`); cut-away butte ground |
| Vesicular Basalt | `vesicular_basalt.png` | dark grey with holes | Dark grey to brown, rounded gas holes, not red, not glowing (Spirit, Gusev, VERIFIED) | Rare patches on dust plains, patches on rocky flats, boulders |
| Spherule Sand | `spherule_sand.png` | dark sand with grey dots | Dark sand with grey spheres; "blueberries" are grey (Opportunity, VERIFIED). Drops 1 to 3 iron nuggets (INVENTED yield) | Disk patches on dust plains and rocky flats |
| Impact Breccia | `impact_breccia.png` | grey-brown angular chips | Angular fragments in a dark matrix (NTRS 20170006943, VERIFIED) | Crater rims and ejecta (data `rim_block`, `ejecta_block`) |
| Gypsum Vein | `gypsum_vein.png` | white streaks in grey | Bright white veins in darker rock (Curiosity, VERIFIED) | Ore veins in terracotta layers of `highland_mesa` |

Not a block: `co2_frost` is deferred (see `docs/team/reports/2026-11-27-technical-lead-w2-blocks-atmosphere.md`). Sand ripples use `minecraft:red_sand` stripes on `basaltic_sand`, pebble patches use `minecraft:gravel` (no new art).

## Conductor ore placeholders (BACKLOG #49, added 2026-11-28)

Drawn by `tools/art-preview/gen_conductor_placeholder_textures.py` (add-only: it never overwrites an existing file). Same rule: replace the PNG with the same name. SME: `docs/science/mars-conductor-and-carbonate-ores.md`.

| Block or item | File | Placeholder look | Final art should show |
|---|---|---|---|
| Sulfide Vein (INVENTED) | `textures/block/sulfide_vein.png` | grey-brown rock, rust and dull gold flecks | Not green, no bright copper (SME section 3). Reads as rock first |
| Magnetic Ore (INVENTED) | `textures/block/magnetic_ore.png` | dark grey rock, metallic speckle | Dark grey, metallic speckle (SME section 3) |
| Carbonate Rock (REAL, game-sized) | `textures/block/carbonate_rock.png` | pale warm-grey rock, faint bands | Pale rock, small veins (Jezero, Nili Fossae; SME section 4) |
| Carbonate (item) | `textures/item/carbonate.png` | pale chunk | A pale rock chunk |
| Ripple Sand | `textures/block/ripple_sand.png` | made by the Artist's script | Muted tan ripple stripe on dune faces |

## Status after the texture pass (2026-11-28)

| Texture | Status | What it is now | Palette (hex) |
|---|---|---|---|
| `basaltic_sand` | REAL | Dark grain, 4-step ramp, no hole or vein | `#2E2824 #3E3530 #4F443C #665A4F` |
| `ripple_sand` | REAL (new) | Same sand with soft wavy crests, period 8 px, one ramp step of contrast (not stripes) | same four |
| `hematite_ore` | REAL | Basalt grey with rust-red lumps and grey metallic flecks | rock `#3F3B3A #4F4A48 #605A57`; red `#7A2E22 #9A3F2B #B5543A`; grey `#5E6367 #7C8185 #9AA0A4` |
| `dark_sand_ore` | REAL | Dark red-brown sand, lighter ore grains | `#2F1D1A #3F2822 #53332A #693E2F`; ore `#7C4B33 #A8683F #C08450` |
| `ground_ice` | REAL | Dusty neutral white-grey ice, brown dust specks, hairline crack; no blue, no wet shine | `#B9BFC2 #CDD2D4 #E4E8EA`; dust `#8A6F5C #A58468` |
| `duricrust` | REAL | Cemented ochre plates, lifted light edges, dark cracks | `#8E6A44 #A67C52 #B98F5F #CBA672`; crack `#5E4228` |
| `vesicular_basalt` | REAL | Dark grey, small dark holes with a lit lower lip, 11 vesicles | `#303133 #3E3F42 #4D4E52 #5E5F63`; hole `#1C1D1F` |
| `spherule_sand` | REAL | Dark sand with neutral grey balls (r, g, b within 6), 8 spheres | balls `#B3B6B8 #8C8F91 #5D6062` |
| `impact_breccia` | REAL | Angular grey-brown fragments in a dark matrix | `#2F2A28 #3A3431`; frags `#6F6459 #857862 #58545A #9A8872 #4B4E52` |
| `gypsum_vein` | REAL | Dark grey rock with thin pale veins that wrap | rock `#4A4745 #585451 #67625D`; vein `#E8E4D8 #BDB8AC` |

Seen in a real client: `tools/screenshots/out/20261128-004710/` (script `tools/screenshots/art_textures.shots`). Known weak points: duricrust is the warmest and highest-contrast block and can read as wood at close range; vesicular holes are hard to see beyond about 8 blocks. Both are noted in the report as owner looks.
