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

## Update 2026-11-28 (Lead Artist): conductor ores and carbonate item are REAL; duricrust cooled

All from `tools/art-preview/gen_terrain_textures.py` (same guards: opaque, no green, no blue cast, no pure black or white, at most 16 colours; the item has clear corners and a 40 to 80 px chunk). `gen_conductor_placeholder_textures.py` is also obsolete now.

| Texture | Status | What it is now | Palette (hex) |
|---|---|---|---|
| `sulfide_vein` (INVENTED) | REAL | Grey-brown rock, 4 rust-orange stains, 5 dull gold flecks. Not green, no bright copper (vanilla copper ore is green-teal: seen side by side) | rock `#403A36 #4E4743 #5E5650`; rust `#7C4A30 #94593A`; gold `#9C8340 #B79B52` |
| `magnetic_ore` (INVENTED) | REAL | Dark metallic stone, red-brown flecks (not bright red, so not redstone), cool-grey glints | rock `#2A2A2C #363638 #444446`; fleck `#6E3328 #8A4332`; glint `#7A7D82 #9A9DA2` |
| `carbonate_rock` (real idea, game-sized) | REAL | Pale tan-grey rock, two faint wavering veins | `#AFA898 #BFB8A8 #CFC9BA`; vein `#E2DDD0`; shade `#948D7E` |
| `item/carbonate` | REAL | Small pale chunk, 49 px, lit top-left, dark outline | `#DDD8CA #C4BDAD #A59E8E`; edge `#6E675A` |
| `duricrust` | REAL, retoned | Cooler tan plates, contrast lowered (was `#8E6A44..#B98F5F`, crack `#5E4228`) | `#8A7656 #9A8664 #A89473`; crack `#6A5A42`; lip `#B5A383` |

Seen in a real client: `tools/screenshots/out/20261128-011135/ores_01_wall.png` (script `tools/screenshots/art_ores.shots`).

## Update 2026-11-28 (Lead Artist): oxygen maker band, seed kit icons, processor ports

| Texture | Status | What changed | Palette (hex) |
|---|---|---|---|
| `o2_generator_face`, `_back`, `_end`, `_side` | REAL (band baked) | Made-up band on rows 14-15 of the used region (global 2 on / 2 off dash), plate at x10..13 y9..12 on the face; status lamp moved from the lower-right to the upper-right panel to make room. Tube and top textures untouched. Wording on the block: "Game-sized version of a real idea" (Game Designer) | band `#CC79A7 #8E3F6C`, slash `#2A2420` |
| `item/biocrust_kit`, `meadow_kit`, `wetland_kit`, `forest_kit` | REAL (hand-scripted pixel art) | Seed packet, ink outline, crimped top, 3-row colour band plus an ink symbol (crumbs, grass tufts, reed with head, two berries). No green, no purple. Replaces the reused carbonate texture | band `#D55E00`, `#F0E442`, `#56B4E9`, `#0072B2`; paper `#E7E2D6 #C9C2B2`; ink `#2A2420` |
| `atmospheric_processor_port` | REAL (retoned) | Ports lightened (alloy `#7E8790` body, `#A8B0B7` top row) after a real-client close-up showed dark ports sinking into the shaded gold east side | `#7E8790 #A8B0B7 #59616A #3B4148` |

Generators: `tools/art-preview/gen_phase1_batch1.py` (oxygen maker; band colours exempt from its 6-colour budget), `gen_seed_kit_icons.py` (new; also rewrites the four item models), `gen_invented_machines.py` (port). Seen in a real client: `tools/screenshots/out/20261128-oxygen-band/` (script `tools/screenshots/oxygen_band_closeups.shots`).

## Update 2026-11-28 (Lead Artist): running-state art PREPARED (new files only)

New textures, animated strips and model variants for off / idle / running, from `tools/art-preview/gen_running_states.py`; **no existing texture changed**, nothing referenced by a blockstate until the Technical Lead adds the property. Table and instructions: `docs/art/running-state-table.md`. Status: PLACEHOLDER-QUALITY first pass, frames viewed flat only, not seen in a real client. Animated: `regolith_oven_front_running`, `regolith_outgasser_puff`, `o2_generator_face_running`, `water_reclaimer_front_running`, `atmospheric_processor_canister_running`, `super_greenhouse_emitter_flare_running` (each with `.png.mcmeta`). Static: `*_front_off`, `*_front_idle`, `*_front_running`, `battery_front_charge0..2`.

## Update 2026-11-28 (Lead Artist): ice extractor art written

13 new textures `ice_extractor_*` (base with band, side, front with plate, front_off, front_running, leg, heater, sleeve, top, pump, tank, tank_top, hose), two animated strips (`ice_extractor_sleeve_running`, `ice_extractor_steam`, 4 frames x 10 ticks, INVENTED), models `ice_extractor`, `_off`, `_idle`, `_running` and item model. Status: REAL first pass (hand-scripted pixel art, `tools/art-preview/gen_ice_extractor.py`), flat sheets viewed, not seen in a real client. Spec and registration list: `docs/art/ice-extractor.md`.

## Ground ladder placeholders (Technical Lead, 2026-11-28)

Three new PLACEHOLDER block textures from `tools/art-preview/gen_ground_ladder_textures.py` (never overwrites an existing file): `washed_regolith.png` (paler, greyer red dust), `rich_soil.png` (dark red-brown with crumbs), `biocrust.png` (dark grey-brown crust, paler flakes, no green). The Lead Artist replaces them (same names).

## Gate input item icons (Technical Lead, 2026-11-28)

Three new PLACEHOLDER 16 x 16 item icons from `tools/art-preview/gen_gate_item_textures.py` (never overwrites an existing PNG or item model): `item/heater_coil.png` (orange wire wound on an alloy spindle), `item/filter_cartridge.png` (ribbed paper drum between alloy caps), `item/pump_bearing.png` (alloy ring with a hole). Transparent corners, ink outline, no green, no purple. The Lead Artist replaces them (same names). Real-client look: hotbar icons in `tools/screenshots/out/20261128-141443/cache_all_three.png`. The other four gate items of `docs/design/gate-input-items.md` (resin pack, sorbent bed, signal board, activation cell) have no icon yet.
