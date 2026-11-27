# Technical Lead report: W1 terrain (real-Mars landforms), BACKLOG #49

**Date:** 2026-11-27 · **Role:** Technical Lead · **Branch:** claude/elegant-brown-anu1ci · **Inputs:** `docs/science/mars-terrain-reference.md` (SME), `docs/design/world-and-blocks.md`, owner decisions listed in the task.

## 0. Handoffs received

| From | Handoff | Answer |
|---|---|---|
| Owner (via coordinator) | Terrain must read as real Mars; relief 3 to 6 on plains; craters, dunes, mesas, one canyon; large craters about 25 times and canyon about 100 times compressed, labelled stylised; one fixed wind per planet in planet data; new worlds only; resources never change Index rates; new ore and ice blocks | Accepted. **Canyon not built** (the task list for this session named craters, dunes, ridges and mesas, and boulders): proposed for W2, see section 3. |
| SME | Landform table, palette, notes 5.1 to 5.5 | Accepted with these deviations: surface layer under the dust is red sandstone, not gravel (gravel falls and the old slab already used sandstone); mesa bands use only red, orange, brown, tan, white and grey terracotta (vanilla bandlands has yellow that reads olive); rim blocks only on craters of 16 blocks or more. Listed in section 2. |

## 1. Done (what changed)

**Generator and data**
- `mod/src/main/java/mars/dimension/MarsChunkGenerator.java` (registered as `mars:planet`): the vanilla noise generator plus one pass between the carver step and the features. `MarsWorldgen`, `PlanetTerrainData` (reload listener for `data/mars/planets/*.json`), `TerrainModeData` (per-world saved mode).
- Datapack (`pack/data/mars/worldgen/`): noise, density functions, `noise_settings/planet.json` (surface rule), biomes `dust_plain`, `rocky_flats`, `highland_mesa`, ore, ice and boulder features. `dimension/planet_1.json` now uses `mars:planet`. The old `regolith_flats` biome file stays (old saves reference it).
- **Which parts are datapack and which are mod code (asked for):** relief, mesas, ridges, caves, surface colours, biomes, ore veins, ice lenses and boulders are **datapack**. Craters, dunes and the pad are **mod code** (a small pure package, `mars.terrain`), because a datapack feature cannot make a bowl-and-rim profile or a one-sided dune. Block registration is mod code.
- Pure `mars.terrain` (no Minecraft imports, ArchUnit rule added): `Landforms`, `CraterField`, `Crater`, `DuneField`, `TerrainParams`, `TerrainMode`, `LegacyFlat`, `LegacyDetect`, `Hash`.
- Planet data `pack/data/mars/planets/planet_1.json`: wind (110 degrees, toward east-south-east), pad, crater levels, dunes, surface blocks, rock chances. Every number is marked INVENTED or cites the SME row.
- Four new blocks (`mars.world.MarsTerrainBlocks`): `basaltic_sand`, `hematite_ore` (drops raw iron, silk touch keeps it), `dark_sand_ore` (smelts to glass), `ground_ice`. Blockstates, models, loot tables, tags, lang, one recipe. **Textures are placeholders** (`docs/art/w1-placeholder-textures.md`).
- Flat dome pad: radius 32 flat, ramp to 60, top block y 80 (data).
- Docs: ARCHITECTURE 2.23, launch guide (W1 section), BACKLOG #49, HANDOFF, this report. Screenshot scripts: `tools/screenshots/w1_terrain.shots`, `w1_legacy_save.shots`, `w1_machines_on_terrain.shots`; `run-shots.sh` and `DevTools` can now open an existing save (`MARS_SHOT_WORLD=<folder>`).

**How "new worlds only" is guaranteed (important; the first idea would not have worked).** I read the 1.21.1 source (`WorldDimensions.bake`): the datapack's dimension file wins over the dimension stored in `level.dat`. So just editing `planet_1.json` would have turned every old world's not-yet-generated chunks into noise terrain, next to old flat chunks. The fix: each world saves a terrain mode (`data/mars_terrain.dat`) the first time its Mars level loads. A world that already holds Mars data (region files under `dimensions/mars`, or any `mars_*.dat`) is saved as `legacy_flat`, and the generator then builds exactly the old slab (no surface rule, no features). Any other world is `noise_v1`. A generator with no mode set throws.

## 2. What I ran (real output)

| Check | Result |
|---|---|
| `./gradlew build --offline --rerun-tasks` (spotless, compile, unit tests, jar) | BUILD SUCCESSFUL; **260 unit tests, 0 failed** (was 233; +27: 12 in `LandformsTest`, 8 in `TerrainContentTest`, 7 in `LegacyDetectTest`) |
| `./gradlew runGameTestServer --offline` | **RESULT PASS passed=123 failed=0** (was 106; +17: 3 generator/mode checks, 10 terrain pipeline checks, 4 feature/block checks) |
| Real client, new world, 9 screenshots | seen, see section 4 |
| Real client, **existing pre-W1 save** (made today with the old flat Mars, region files present) opened with the new build | log: `Mars terrain mode for mars:planet_1: legacy_flat`; teleported to x 1200, z 900 (never generated), `/execute if block` found red sand at y 63, air at 64, red sandstone at 57, stone at 56, bedrock at 0: five "Test passed". Mode saved to `mars_terrain.dat`. |
| Real client, new world | log: `noise_v1`; Outgasser x2, solar panel, battery and Monitor placed on the generated pad; `/mars index advance 10`: pressure 0.600 to 1.733 kPa; Monitor opens and shows it |

Boot check readings (seed 20261127, 384 x 384 block window, bare chunks): relief of 31 plains windows (64 x 64): median 4, range 2 to 10 (budget 3 to 6); pad: 3209 columns, 0 wrong; palette: 72 % red sand, 23 % dark sand tops; caves 3.3 % of rock 16+ blocks down, 0 fluid blocks; bedrock at y 0 in 4096 of 4096 columns; large crater at (-1500, 884): pure delta -16.6, built drop 17; ice lenses 3 to 12 blocks under the surface; 509 boulders on the ground, 0 floating; **7 to 9 ms per chunk** for noise, surface and landforms (vanilla about 20 to 60; unverified on a weak classroom computer).

**What datapack worldgen could not be verified headless:** the GameTest server has no Mars level (it builds its own world without datapack dimensions), so I ran the real generator on bare chunks (biomes, noise fill, surface rule, landform pass) and the real placed features in a scratch corner of the test world. Not covered headless: a real world-gen region and the chunk system's neighbour rules, lighting, how it looks, a dedicated server (EULA), and a long flight across many chunk borders at speed. The real client run covers the first four for a few hundred chunks. The `ice`, `boulders` and `veins` features ran in a normal level, not in a world-gen region, and the `biome` placement filter was left out in that test.

## 3. Honest comparison with the SME notes: Mars or Earth hills?

Seen in the real-client screenshots (`tools/screenshots/out/20261127-205929/w1_*.png` terrain, `20261127-205257/` existing save, `20261127-205410/` machines on the pad).

**Reads as Mars:** wide flat red-ochre plain with a 3 to 6 block relief (no rolling hills), a hand-placed small crater with a dark rim ring, a large crater (about 230 blocks across) with raised rim, flat floor and central peak and small craters inside it, dark parallel dune rows with gentle and steep sides, banded flat-topped buttes with steep sides, dusty tan sky. Nothing is green or blue, no water, no trees.

**Still reads as Minecraft, not as a NASA photo:**
- Everything is 1 m cubes, so ripples (3 m period) and pebbles are missing; plains look empty at block scale (the SME warned about this). Needs the W2 ripple and pebble pass.
- Dune rows are too straight and evenly spaced (one wavelength for the planet; varying it sheared the crests, found and fixed in the unit tests). No crescent barchans, no horns.
- Dark basaltic patches are blobby and about a quarter of the surface; real Mars dark areas follow dunes and craters.
- Crater counts follow the SME ratios but the pale ejecta speckle (sandstone, 25 % times a falloff) still looks like salt and pepper near big rims.
- The buttes are too close to the pad in the first screenshots (the nearest is about 90 blocks from spawn for seed 20261126). Position is noise-luck; a "keep mesas out of 150 blocks" rule would need code or a different mask.
- Exposed ground ice shows in cliff sides. That is a free "ice in the wall" visual but it does mean ice is found at the surface in butte sides, which is deeper in the SME's sense only inside plains.
- Mesa band colours are my guess (NOT VERIFIED), no yellow.
- Not built: **canyon, polar cap, lava plain, fans and flood channels, polygons**. The canyon needs either a very long density spline or a second landform in `mars.terrain` (a straight trench with stepped walls); I propose it as the first W2 item.

My view: it passes "not generic hills" (the owner's test) because the relief comes from craters, dunes and buttes on a flat plain, but the SME and the owner should look at the pictures before calling it "Mars-like" in the journal. Everything called "large crater" or "butte" must carry the "stylised, shrunk to fit the game" label (SME note 5.4).

## 4. Decisions needed

| # | Question | Type | Recommendation |
|---|---|---|---|
| 1 | Is the look acceptable as the W1 baseline, or should I tune first (fewer dark patches, rounder dunes, farther buttes)? | ill-structured (taste, with SME and Artist input) | Accept as W1; open a W2 task list: ripples and pebbles, canyon, crescent dunes, mesa distance, polar cap. |
| 2 | Canyon: build in W2 as a landform in `mars.terrain` (straight trench, stepped walls, scree, labelled stylised)? | well-structured once agreed | Yes, one canyon about 1000 blocks long, 60 to 100 deep, 200 to 400 wide, placed by seed near a planet edge. |
| 3 | A world that already holds Mars data keeps the flat slab. A world that was never on Mars before this build but already ran the mod also counts as "has Mars data" (the mod writes `mars_*.dat` as soon as it runs). Accept? | well-structured | Yes: safe. If the owner wants such worlds to get the new terrain, we would have to check for generated Mars chunks instead; I do not recommend it. |
| 4 | Dedicated server and the real `mods` folder remain unverified (EULA). | owner's call | Owner runs `runSelftestServer` once after accepting the EULA. |

## 5. Handoffs

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** four terrain blocks have placeholder 16 x 16 textures.
> **Context:** `docs/art/w1-placeholder-textures.md`; files in `pack/assets/mars/textures/block/` (`basaltic_sand`, `hematite_ore`, `dark_sand_ore`, `ground_ice`).
> **Needed from them:** final textures (same file names), plus a view on the dust, patch, rim and ejecta colours (set in `pack/data/mars/planets/planet_1.json` `surface` and the surface rule in `noise_settings/planet.json`).
> **Done when:** PNGs replaced and one screenshot run looks right to the Artist.

> **To:** Subject Matter Expert · **Problem type:** well-structured
> **Problem:** check three numbers and two choices against the screenshots: mesa band colours, the pale ejecta tint, the gravel-versus-sandstone sub-layer; and confirm the "stylised" wording for large craters and buttes.
> **Context:** this report section 3; `docs/science/mars-terrain-reference.md` rows 3, 4, 10.
> **Needed from them:** corrections or "fine"; any sourced number replaces an INVENTED one in `planet_1.json`.
> **Done when:** the NOT VERIFIED marks in the planet file are either sourced or kept.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** the journal needs "shrunk to fit the game" lines for large craters, buttes and (later) the canyon, and a line that caves and ground-ice depth are game features (SME notes 5.4, world-and-blocks 2.2).
> **Needed from them:** wording and goal IDs; nothing teaches from the terrain yet.

## 6. Risks

- **Generation cost on weak classroom machines** is measured only on this build machine (7 to 9 ms per chunk for our part). Unverified elsewhere; render distance is the first fix.
- **No dedicated-server or real `mods` folder run** (EULA, owner). The generator is registered through a normal registry and the mode hook uses `LevelEvent.Load`, which fires on a dedicated server, but that is read from the source, not run.
- **Crash between world creation and first save** re-decides the mode on the next start with the same evidence; a brand-new world that never saved stays new. A world that saved Mars data but not the marker is treated as legacy (safe direction).
- **Data reload** (`/reload`) changes numbers for chunks not yet generated only; already generated chunks keep their shape. Same limit as other data in this build.
- **Interpolation trap** (fixed, noted for whoever edits the density functions): any non-linear term near the surface moves the ground by several blocks because cells are 8 blocks tall. Keep extra terms 16 blocks below the surface.
- **Seed luck:** pad flatness is guaranteed by code, but a butte can sit next to the pad (the pad cuts it). A rule to keep buttes out is not built.
- **Scope:** canyon, polar cap and finer surface detail (ripples, pebbles) are not in; the owner's "one canyon" is a W2 item.

## 7. Shared model updates

- **Decision (Technical Lead, ARCHITECTURE 2.23):** the datapack dimension file overrides `level.dat`, so "new terrain for new worlds only" is a **per-world saved terrain mode** (`mars_terrain.dat`), decided when the world's Mars level first loads. Worlds that already hold Mars data keep the old flat slab. Rationale: the alternative (edit the dimension file) changes every old world's new chunks. Alternatives: a second dimension id (breaks the "any `mars:` dimension is a planet" rule and old planet data); a world-creation flag (not available to a datapack). Revisit if: the owner wants old worlds migrated.
- **Term:** "landform pass" = the step inside `MarsChunkGenerator` that applies craters, dunes and the pad to each chunk's own columns. "Pad" = the flat starting area (data).
- **Lesson:** a hidden off-by-one (`ChunkAccess.getHeight` already returns the top block, not the first free y) and a hidden non-linearity (cave term near the surface under 8-block cells) both made flat-looking numbers wrong by several blocks; the headless pipeline check (`TerrainRig`) found both before any screenshot. Run the generator on bare chunks first, screenshots second.
- **Lesson:** a GameTest that asks a real level for chunks from a world-gen thread can hang (the structure manager asks the server thread). Use a manager that finds no structures.
