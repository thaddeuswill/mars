# World and Blocks: how Mars stays Minecraft

**Owner:** Lead Game Designer · **Written:** 2026-11-27 · **Status:** PROPOSAL for the owner (nothing here is built or accepted). **Revised 2026-11-27** to apply the SME verdicts and the owner's "keep real Minecraft" direction (see section 0.1)
**Question from the owner:** "what are we going to do for terrain generation and other blocks to create these things because I still want it to be Minecraft with mining, gardening, building, and everything"
**Reads with:** DESIGN.md (phases, Content Lists), `docs/design/balance-sheet.md`, `docs/design/story-bible.md`, `pack/data/mars/dimension/planet_1.json`, `pack/data/mars/terrain/ice_rule.json`.
**Marks used:** *invented* = our game number or design; **SME check** = real Mars claim not yet verified by us (the Subject Matter Expert must source it before it reaches a journal or sign); **game design** = a step we made up for play, with no source; **unverified estimate** = effort guess, +/-50% (R-68, nothing measured yet).

## 0. The short answer

Today Mars is a flat slab (bedrock, 56 stone, 4 red sandstone, 3 red sand; no ores, caves, structures or plants). It works as a test bench, not as a Minecraft world. The plan:

1. **Keep every Minecraft verb**: dig, build, farm, explore. Each one gets a job in the Planetary Index, but **the Index numbers do not change**. Resources decide what you can *build and where*, not how fast pressure rises. That keeps the balance sheet valid and the physics honest (thresholds never scale).
2. **Replace the flat generator in three steps**, cheapest first. Step 1 is mostly data (JSON), so the owner sees real hills, ore and ice in the first build.
3. **Add about 14 new blocks and 6 new items by the end of Phase 3**, most of them plain blocks with a texture, a loot table and a tag. Only a few need real code (ground ice behaviour, spreading biocrust, ground collapse).
4. **Pillar check:** the planet is the quest log (ground ice is where Hydrosphere comes from, so you go and find it); every change is visible (meltwater, green patches); compact before wide (rich ground is near the dome at first; far ground is earned with tubes); your progress provokes the planet (warmth makes ice under your base collapse).

### 0.1 Changes applied 2026-11-27 (SME recommendations accepted)

| # | Change | Where |
|---|---|---|
| 1 | The "glass ore" is **sand** (dark red-brown), never silica or quartz | 1, 2.2, 3.1, 3.2, 4 |
| 2 | Perchlorate "seams" are **invented**; the survey scanner reads a **soil percentage**, not veins | 1, 3.1, 3.4, 4 |
| 3 | **Water ice** is the main polar block (north cap); frozen CO2 is a thin secondary frost layer | 2.2, 3.1 |
| 4 | Canyon and caves carry a "stylised" note; the survey scan carries a depth note | 2.2, 3.4 |
| 5 | The perchlorate washing step is labelled **game design** | 1, 3.3 |
| 6 | Invented items are labelled (section 3.5) | 3.5 |
| 7 | Dome sits at a stated high-northern "latitude", Phoenix-like | 2.2 |
| 8 | The SME's sourced loop is the crafting spine; aluminium is cut | 3.6 |
| 9 | Every invented machine carries "game invention, not real" | 3.2, 3.5 |
| 10 | Vanilla-first recipe table | 3.6 |

**Vanilla stays.** Crafting table, furnace, tools, farmland, glass, bricks, buckets, bone meal, redstone, diamonds, enchanting all work on Mars. Mars adds; it removes nothing (owner decision, section 3.4).

## 1. What the player does, phase by phase

### Phase 1: Survival (about 8 h standard)

| Verb | What the player does | Resources and why | Feeds the Index / gate | Goals |
|---|---|---|---|---|
| **Mine** | Dig regolith and rock around the dome. Dig *down* and pile regolith *up* to shield the shelter and living quarters | Regolith and basalt are the shielding blocks already in the balance sheet (each block x0.5 dose; 3 blocks = about x0.13). **Hematite** (iron) for bearings and heater coils. **Sand** (dark red-brown, smelted to vanilla glass) for dome glass and greenhouse panes. Rare **meteorite iron** as an exploring prize | Parts keep machines alive (C1). Shielding answers forecast F2. Heater coils build Outgassers and Ovens, which move pressure to the 5 kPa gate | A1, C1, C2, B1 |
| **Build** | Compact hab: stack stories upward inside a sealed footprint. Airlock with a brush station. Solar panel arrays (3x3), batteries, Ovens, first Outgassers | Scarcity is real: a limited footprint of sealed, pressurised space; glass and parts cost ore | Power grid and Outgasser rate (existing slices 3 to 5) | B1, C1, C2 |
| **Garden** | **Sealed dome greenhouse.** Wash perchlorate out of regolith (the washing step is *game design*, not a sourced method), fill planters, grow potato, lettuce, dwarf wheat, tomato under lamps and warmth. Water from the dome reclaimer | Food is the pressure that keeps people away from the machines. Perchlorate washing is a real constraint (**SME checked 2026-11-27, accurate**: perchlorate in Mars soil is real, about 0.4 to 0.6% by mass of leachate at Phoenix [R1]; it kills plants at high dose in analog soil [R10]. **Washing method NOT VERIFIED**: no source opened; label the washing step *game design*) | Food and the dead-tomato lesson (C1). Does **not** change a Planetary Index row | C1, A1 |
| **Explore** | Short trips (radius about 64 blocks, *invented*) in a suit: find a hematite outcrop, a crater rim, the first ground-ice patch (the *survey scan*, an *invented* tool, shows it; you do not guess) | Exploring sets up Phase 2: you learn where the ice is before you need it | Nothing yet; it plants B5 and A2 | A2, A4 |

### Phase 2: Industrial Geo-Engineering (about 18 h standard)

| Verb | What the player does | Resources and why | Feeds the Index / gate | Goals |
|---|---|---|---|---|
| **Mine** | Locate and drill **ground ice** (lenses a few blocks under regolith, thicker at higher "latitude" toward the polar cap). Quarry **sulfate/gypsum** and **carbonate rock** for tube hulls and insulation. Mine more iron for processors | **Ground ice must be under the ice extractor** to place it (*invented* rule). Dry ground: it will not run | Hydrosphere gate (ice extractors), Thaw. The extractor's rate is the same number as today; terrain decides *where*, not *how fast* | A2, A3, B1 |
| **Build** | Airlocks, **pressurised transit tubes** to outposts, outposts with their own panels, atmospheric processors, emitters, insulation shrouds | Tubes are the "expansion is earned" step: wide building is allowed only after Phase 2 begins. Build on rock, not on ice lenses | Temperature gate (emitters, mirror uplink). Ground collapse is a cost of putting heat on ice (B5) | B2, B5, C2 |
| **Garden** | Not a Phase 2 theme. Greenhouse continues for food; meltwater needs treatment before it reaches planters (perchlorate is a soluble salt, measured in water extracts of Phoenix soil [R1]: **accurate**; the treatment machine is *invented*) | Keeps the farming verb alive and ties it to the salty-meltwater hazard | None (food, safety) | C1, B5 |
| **Explore** | Survey for ice lenses, outposts, canyon walls, polar cap edge. Quiet zones and lure stations are placed on terrain you scouted | The map is the second quest log: every outpost sits on ground you read | Fauna signature (existing) | A4, B5, D1 |

### Phase 3: Biome Seeding and Equilibrium (about 14 h standard)

| Verb | What the player does | Resources and why | Feeds the Index / gate | Goals |
|---|---|---|---|---|
| **Mine** | Less digging, more **soil making**: crush basalt and carbonate for soil treatment; keep scrubbers fed (carbonate rock is where scrubbed CO2 ends up) | Mining shrinks on purpose: the late game is about balance, not extraction | Carbon scrubber locks CO2 (rule A in the balance sheet) | A5, B2 |
| **Build** | Algae vats, scrubbers, soil treatment plants; the **beacon** (mast segments x7, transmitter core, dish array, 8 landing lamps) | Beacon parts are the long build, a visible monument | Breathable and Equilibrium holds | B4 |
| **Garden** | **Seed biomes in a chain:** biocrust, tundra meadow, wetland margin, cold forest. Each needs the soil the one before made | The vanilla "grow a garden" fantasy, at planet size. Journal label: "On Earth this takes hundreds of years. The game speeds it up." | Biomass and the CO2-to-O2 swap (existing rule A numbers) | A5, B2, B4 |
| **Explore** | Walk the green you made; find the places the water pooled; choose where the next patch goes | The reward screen is the world itself | None (feedback) | B4, D2 |

## 2. Terrain generation

### 2.1 Where we are

`planet_1.json` uses the vanilla **flat** generator, one biome (`mars:regolith_flats`), no features, no carvers, no structures. Everything on the surface is at one height, so there is no reason to dig for anything, no place to hide a base, and no "over the hill" to explore.

### 2.2 Target world (what it should look like, and the real-Mars link)

| Feature | In game | Real Mars link (SME checked 2026-11-27; verdict in brackets, sources R1 to R10 in `docs/team/reports/2026-11-27-sme-world-and-blocks.md`) |
|---|---|---|
| Rolling regolith plains, rocky basalt highs | Noise-based hills 10 to 40 blocks, red sand and dust on top, basalt outcrops | Basaltic crust under iron-oxide dust [**accurate**: red from iron oxide, R3; basaltic sand, R8] |
| **Craters** | Bowls 8 to 60 blocks wide with raised rims; rim shows layers, floor traps dust | Impact craters are everywhere on Mars [**accurate**: R3 lists impact craters among the shapers of the surface; "everywhere" is a simplification, density varies] |
| **Dunes** | Wavy sand ridges in low basins | Dune fields exist (wind-formed) [**accurate**: active Bagnold dunes, R8]. Sand there is dark basaltic (olivine, pyroxene), not white quartz; keep dunes dark or red-brown |
| **Canyon / scarp** | One long cut near the map edge for a Phase 2 outpost and view | Valles Marineris is the famous example; ours is a small stand-in, labelled "stylised" [**accurate**: about 3,870 km long, 600 km wide, up to 9.3 km deep, R3; a 1 m per block canyon cannot be real size, so the "stylised" label is required] |
| **Ore veins** | Hematite (red-grey) in outcrops; **sand** (dark red-brown) in dune basins; sulfate/gypsum in canyon walls; rare meteorite iron on the surface | Hematite and sulfates have been found from orbit and by rovers; meteorite iron-nickel has been found on the surface. Ore amounts and places are *invented* [hematite: **accurate**, 350 by 750 km region at Meridiani, R5; sulfates: **accurate**, calcium sulfate veins at Gale, R6; meteorite iron: **accurate**, four found by Opportunity, R7. **Sand ore: acceptable simplification**, Mars sand is basaltic and a glass-quality silica source is NOT VERIFIED. Applied: the block is "sand", dark red-brown, never "silica" or "quartz"] |
| **Caves and ice** | Small lava-tube-style caves (**stylised, cosmetic and invented**, shelter; journal: "Caves like these are a game feature"); **ground-ice lenses** at 3 to 12 blocks under the surface | Buried ground ice is real at mid and high latitudes; whether it sits this close is *stylised* [ground ice: **acceptable simplification**, models say ice is stable within 1 to 2 m at high latitude and deeper toward the equator, R2; Phoenix hit an ice table about 5 cm down, R1. 3 to 12 blocks near the dome is deeper than the polar truth and shallower than the equatorial truth; the survey scan says so (section 3.4). Lava tubes: **NOT VERIFIED** (no Mars source opened); label caves "cosmetic, invented"] |
| **Polar cap** | At the far north edge: **water ice is the main block** (layered, `packed_ice`-style); frozen CO2 is a thin secondary frost layer on top (seasonal) | Real [**acceptable simplification**: the north residual cap is water ice and the south residual cap has CO2 ice, with seasonal CO2 frost on both, R4; applied: the north cap is water ice, with CO2 as seasonal frost only, so the two are no longer merged] ; the polar frost sink (slice 5) already models CO2 there |
| **Dormant fauna sites** | Not terrain. Left to the fauna design (fictional, labelled) | n/a (fiction) |

**Dome latitude (applied from SME decision 2).** The dome sits on the high-northern plains, a Phoenix-like site, stated in the journal as "high northern latitude". There, ground ice near the surface is honest (ice table about 5 cm down at Phoenix, R1; shallow ice stable at high latitude, R2). The exact Phoenix latitude is **not verified** here (SME to confirm before it goes in text). The 3 to 12 block depth stays *stylised* (deeper than Phoenix, a block is 1 m). It also puts the polar cap at the north edge, consistent with a water-ice cap. Journal line: "Ice is this close only near the poles. Nearer the equator you would dig much deeper."

**Design rule:** resources that matter for a gate sit **near the dome in Phase 1** (a hematite outcrop and one ice patch within about 64 blocks) and the richer deposits sit **far** (150 to 400 blocks), so wide travel is a Phase 2 reward, not a Phase 1 chore. The 64 and 150 to 400 block distances, and all ore amounts, are *invented* game numbers and must be tuned in the first playtest.

### 2.3 Data-only versus needs mod code

| Piece | Data-only (datapack JSON) | Needs mod code |
|---|---|---|
| Hills, plains, basins (noise settings, density functions) | Yes (`noise_settings`, the vanilla noise router; reuse and edit the vanilla pieces) | No |
| Surface blocks by height or slope (red sand, regolith, basalt) | Yes (`surface_rule`) | No |
| Several biomes (plains, dune basin, highlands, canyon, polar) | Yes (`worldgen/biome`, `multi_noise` biome source) | No |
| Ore veins | Yes (`configured_feature`, `placed_feature`, ore feature) **once the ore blocks exist** | Registering the new ore blocks (simple) |
| Caves | Yes (vanilla carvers and noise caves, tuned) | No |
| Ground-ice lenses | Yes as an ore-style feature of `mars:ground_ice` | Registering `mars:ground_ice`. Collapse (*invented*) and "extractor must sit on ice" (*invented*) need code |
| Craters | **Partly.** A set of structure templates (`.nbt`) placed by a structure set gives fixed shapes. A *round bowl with a rim that fits the ground* needs a custom feature | Custom feature (small, pure geometry), later |
| Dunes | Yes with noise (wavy ridges) | No (a custom dune feature is only an upgrade) |
| Canyon | Yes with a noise carve; a long straight cut is simpler as a custom feature | Optional |
| Structures: starting dome, outposts, crashed probe, meteorite | Yes (`structure`, `template_pool`, `.nbt`, jigsaw). The mod already holds `structure/empty.nbt` | Only if the dome must be sealed by code |
| Polar cap | Yes (biome plus surface rule by latitude noise) | No |
| Biome change at runtime when seeding (Phase 3) | No | Yes: an item or machine that rewrites biome and blocks. Plan: Phase 3 only |

**Open for the Technical Lead:** how the starting dome is placed today (spawn-time code or a structure), and whether the dome and boot tests assume a flat world. Both must work on noisy ground (a flat pad under the dome is the usual fix).

### 2.4 Three steps

| Step | Contents | Data vs code | Effort (unverified estimate) | What the owner sees |
|---|---|---|---|---|
| **W1: shaped world** (cheap, first) | Noise terrain with hills, basins and a few biomes; red sand and basalt surface; hematite and sand veins; ground-ice lenses; small caves; flat pad for the dome | About 85% data. Code: register 4 simple blocks (hematite ore, sand ore, basalt, ground ice) | 4 to 6 dev-days incl. textures and tuning | Real Minecraft terrain on Mars, ore to dig, ice under the ground |
| **W2: landmarks** | Craters (custom feature), dune fields, a canyon, polar cap, meteorite finds, a wrecked probe structure, outposts sites | About 50% data, 50% small features | 5 to 8 dev-days | Reasons to explore, places to build outposts, screenshots |
| **W3: living ground** | Ground collapse over melting ice; spreading biocrust and soil; biome rewrite by seed kits; meltwater pools; ground ice lenses react to temperature | Mostly mod code (random-tick blocks, chunk edits) | 8 to 12 dev-days | The planet answers your machines |

Existing saves keep their flat chunks (terrain changes only apply to new chunks). It is a design exercise, so we accept that and say so in the release notes.

## 3. Block and item list

Legend for "Why": **G** = needed for a gate; **L** = learning purpose; **N** = nice to have (cut first). "Code" is small unless noted.

### 3.1 Terrain and resource blocks

| Block / item | New, reused or retextured | Phase | Why | Notes |
|---|---|---|---|---|
| Regolith (loose red-brown soil, shovel) | Reuse `red_sand` or new block | 1 | G, L | Shielding, Oven input, planter base once washed |
| Basalt | Vanilla `basalt`, reused as is (textures are global, so we cannot retexture it for Mars only) | 1 | L | Shielding, crushed for soil in Phase 3 |
| Red sandstone / red sand | Vanilla | 1 | N | Surface, already in use |
| Hematite ore (iron) | **New** | 1 | G | Iron ingots for bearings, coils, tools |
| Sand (dark red-brown; ore in dune basins) | **New** (or vanilla `sand` retinted; textures are global, so new block preferred) | 1 | G | Smelts to vanilla `glass`. Called "sand", not silica or quartz (SME) |
| Meteorite iron | **New**, rare surface block | 1 | N | Exploring prize; real thing (**SME checked: accurate**, R7) |
| Ground ice | **New**, with behaviour | 1 (found), 2 (used) | G, L | Source for ice extractors (*invented*: extractor works on ice only); collapses when heated (*invented*); follows `ice_rule.json` |
| Sulfate / gypsum rock | **New** or reuse vanilla `calcite` | 2 | L | Tube hull and insulation |
| Carbonate rock | **New** | 2 to 3 | L, G | Output block of the carbon scrubber; building block |
| Perchlorate-rich regolith | **New block, but no veins**: perchlorate is soil-wide. The scanner reads a **soil percentage** for the patch you scan (a few percent scale is *invented*; real leachate 0.4 to 0.6% by mass, R1). A patch block that reads "high" is an *invented* seam | 1 | L | Hazard source; the thing you wash out (A1, B5). Journal: "Real perchlorate is mixed through the soil, not in veins. The game marks patches to keep play simple." |
| Frozen CO2 block | **New**, polar only, **secondary** thin frost layer over the ice | 2 | N | Visual for the polar sink; block is *invented* (cut first) |
| Polar water ice (main polar block) | Vanilla `packed_ice`, reused | 2 | L, N | Edge of the world; the real north cap is water ice (R4) |

### 3.2 Building blocks and machines

| Item | New or reused | Phase | Why |
|---|---|---|---|
| Dome glass | **New** (smelted sand + iron frame) | 1 | G (sealed hab) |
| Airlock door and brush station | **New** | 1 | L (perchlorate tracking) |
| Habitat hull panels, basalt brick, regolith brick | **New**, craftable | 1 to 2 | N, L (compact building) |
| Solar panel (round array), battery, Oven, Outgasser, O2 maker, water reclaimer, dust-off station, weather mast | **Exist or planned** (DESIGN.md Content Inventory) | 1 | G |
| Transit tube segment, tube junction, outpost airlock | **New** | 2 | G (expansion), L |
| Ice extractor | **New** (on ground ice only) | 2 | G |
| Atmospheric processor, super-greenhouse emitter, mirror uplink, insulation shroud, lure station, quiet-zone marker, meltwater treatment | Planned | 2 | G or L |
| Algae vat, carbon scrubber, soil treatment | Planned | 3 | G |
| Beacon parts (mast x7, transmitter, dish array, lamps x8, air record) | Planned | 3 | G |

**Labelling rule (SME): every invented machine in the table above and in section 3.5 carries the plain journal line "Game invention, not real" (with the longer wording in `docs/science/mars-materials-and-recipes.md` section 2 where it explains what real thing it stands in for). Machines that are game-sized versions of a real idea (Oven, ice extractor, water reclaimer) say "Game-sized version of a real idea".**

### 3.3 Farming

| Item | New or reused | Phase | Why |
|---|---|---|---|
| Planter (washed regolith in a sealed bed; washing is *game design*) | Reuse vanilla `farmland` behaviour, new look | 1 | L (C1) |
| Grow lamp | **New** | 1 | L |
| Potato, wheat | Vanilla crops | 1 | N |
| Lettuce, tomato | **New** crops (3 growth stages each) | 1 | L (the dead-tomato lesson) |
| Biocrust, tundra meadow, wetland margin, cold forest seed kits | **New** items; each places a patch and (W3) spreads on treated soil | 3 | G, L (A5) |
| Dwarf conifer, moss, sedge, reed, algae mat | New plants or retexture; keep to 6 total | 3 | N to L |

### 3.4 Tools and tiers

Keep the vanilla tool set so the hands know what to do. Tier by material, not by new tool types:

| Tier | Material | Who needs it | Why |
|---|---|---|---|
| 0 | Dome-stock (starting tools, limited durability) | Landing | Teach that parts wear (C1) |
| 1 | Basalt-headed (stone-tier) | Regolith, sand | Cheap, early |
| 2 | Iron (from hematite) | Ore, ground ice | Needs the first outcrop trip |
| 3 | Reinforced iron with a heater tip | Ground ice at depth | Extractors do the real work; this is a hand option |
| Item | **Survey scanner** (new, *invented* tool) | Finds ice lenses; reads a perchlorate **soil percentage** (no seams) | L (A4, B5); cheaper than guessing; the scan is the "read the planet" tool. **Depth note on every scan:** "Ice this shallow is true only near the poles; depth is stylised in this game." Journal: "Game tool. Real surveys map ice from orbit." |
| Item | Suit with oxygen tank and shielding rating | Always | Exists in design; leave to the Technical Lead |

**Owner decision 2026-11-27: keep vanilla Minecraft crafting and progression.** Diamonds, netherite, enchanting and normal crafting stay in the Mars dimension; Mars adds blocks and tools, it does not take vanilla ones away. "I just don't want to take the beauty of minecraft and the actual crafting and everything." Where a vanilla item would skip a gate (for example a vanilla tool replacing a Mars machine), gate it by recipe or placement, not by removal. The classroom preset still hides Mekanism and Create (DESIGN.md). (Supersedes the earlier proposal to remove them.)

### 3.5 Invented items (honest labels)

All amounts, speeds and costs are *invented*. Plain journal label for each is in `docs/science/mars-materials-and-recipes.md` section 2. Short form for the journal: **"Game invention, not real"** for Outgasser, super-greenhouse emitter, atmospheric processor, soil treatment unit, biome seed kits, mirror uplink, insulation shroud, meltwater treatment, quiet-zone marker, lure station, survey scanner, beacon; **"Game-sized version of a real idea"** for Oven, ice extractor, water reclaimer, oxygen maker, electrolyser, Sabatier reactor, carbon scrubber. Also *invented*: ore amounts and distances, the 64 and 150 to 400 block rules, ground collapse, "extractor on ice only", the scanner, the perchlorate washing step (*game design*), basalt fibre. Aluminium is **cut** (SME: needs huge power; not worth a tier).

### 3.6 Recipe table: vanilla first

**Rules.** (1) Every recipe starts from vanilla ingredients; the Mars part is the input (regolith, ice, CO2 canister) or the machine. (2) A recipe never changes pressure or temperature by itself; machines do, at the rates in the balance sheet. (3) **All amounts are *invented* balance numbers, to tune in playtest.** (4) Status marks follow the SME file: REAL / SIMPLIFIED / INVENTED. "Shape" is described in words; the exact grid is a Technical Lead task.

**Vanilla kept, visibly (nothing below replaces these):**

| Vanilla thing | Still works on Mars | Where the player meets it |
|---|---|---|
| Crafting table, chest, torch-style lamps | Yes | Every phase |
| Furnace and blast furnace | Yes; they are the smelter for hematite, sand and regolith | Phase 1 |
| Wood/stone/iron/diamond tools, netherite, enchanting | Yes; iron tools come from hematite. Diamonds are not on Mars (journal joke line, tone check by Artist and ID) | Phase 1+ |
| Glass, bricks, stone bricks, stonecutter | Yes; glass and brick recipes are copied, not replaced | Phase 1 |
| Hoe, farmland, bone meal, water bucket, potato, wheat | Yes; farmland needs washed regolith (*game design*) | Phase 1 |
| Redstone, bucket, hopper, iron ingot, copper | Yes; used as machine parts | Phase 1+ |

**Resource and building recipes**

| Output | Recipe (vanilla ingredients bold) | Serves (Index / build task) | Status | Gate check |
|---|---|---|---|---|
| Iron ingot | Smelt hematite ore in a **furnace** (1 ore to 1 ingot; *invented* ratio) | Machine cost, tools (C1) | SIMPLIFIED | Needs outcrop trip: a gate on exploring, not skipped |
| Glass | Smelt dark red-brown sand in a **furnace** (vanilla glass recipe, new input) | Sealed hab, greenhouse | SIMPLIFIED | none |
| Dome glass | 4 **glass** + 4 **iron ingot** frame (ring), 1 to 8 yield *invented* | Sealed hab (B1) | SIMPLIFIED | none |
| Regolith brick | Smelt regolith in a **furnace** (as clay to **brick**); 4 bricks craft the regolith brick block | Compact building, shelter | SIMPLIFIED | Needs power-free furnace fuel; cannot make a campfire brick (SME) |
| Basalt brick | **Stonecutter** from vanilla **basalt** | Shielding, building | SIMPLIFIED | none |
| Basalt fibre | Smelt **basalt** in a **furnace** (kept only for hull panels) | Hull panels | INVENTED (small) | none |
| Hull panel | **Iron ingot** + **glass** + basalt fibre (3 panels per craft, *invented*) | Sealed hab, tubes | SIMPLIFIED | none |
| Sulfur powder | Smelt sulfate rock (or **calcite**) in a **furnace** | Sulfur-regolith block | SIMPLIFIED | Phase 2 |
| Sulfur-regolith block | Regolith + sulfur powder (shapeless, 4 + 1 *invented*) | Tube hulls, pads | SIMPLIFIED | Phase 2; tubes gated by "Phase 2 begins" |
| Tool tiers 1 to 3 | Vanilla tool recipes with basalt, iron, and iron + heater coil | Dig regolith, ore, ground ice | Game design | Iron pick needed for ground ice (placement gate) |

**Machines and the spine chain** (SME loop: CO2 canister + ice, electrolysis, Sabatier, giving O2, water, methane)

| Output | Recipe (vanilla bold) | Serves | Status | Gate check |
|---|---|---|---|---|
| CO2 canister | Compressor intake (machine) fills an empty **bucket**-style tank from the air; no ore | Feedstock for O2, methane, scrubber | REAL idea [N-Fact, N-ISRU] | none; free feedstock |
| Compressor intake | **Iron ingots**, **glass**, **hopper**, **redstone** | Spine step 1 | INVENTED machine | Phase 1 |
| Ground ice (hand) | Mine with iron pick; melt in **furnace** to a **water bucket** (small, hand-scale) | Dome water; not Hydrosphere | SIMPLIFIED | Hand water does **not** move the Hydrosphere row (machines do), so it skips no gate |
| Oxygen maker | **Iron ingots**, **glass**, **redstone**, circuit (copper + redstone); in: CO2 canister + power, out: O2 tank + carbon monoxide by-product | Life support, O2 row (Phase 3 rule A) | REAL process (MOXIE), SIMPLIFIED scale | Output *invented*; one unit does not keep a crew alive |
| Electrolyser | **Iron ingots**, **glass**, **copper ingots**, **redstone**; in: **water bucket** + power, out: hydrogen + oxygen | O2, hydrogen | REAL [N-MSR] | Phase 1 late or Phase 2 |
| Sabatier reactor | **Iron ingots**, **furnace**, **hopper**, copper; in: CO2 canister + hydrogen, out: methane + **water bucket** | Water loop, fuel (beacon stage) | REAL [N-MSR, N-Sab] | Phase 2; does not warm the planet |
| Regolith Oven (exists) | **Furnace**, **iron ingots**, **brick** | Dome water, small pressure | SIMPLIFIED | Existing slice 5 |
| Solar panel, battery | Existing; battery = **iron ingot** + **redstone** | Power | REAL / SIMPLIFIED | Existing slice 3 |
| Ice extractor | **Iron ingots**, **furnace**, **hopper**, **bucket**; place on ground ice only | Hydrosphere, Thaw | SIMPLIFIED, placement rule *invented* | Phase 2 |
| Carbon scrubber | **Iron ingots**, **hopper**, basalt brick; in: CO2 canister, out: carbonate rock | CO2 sink (rule A) | INVENTED scale; carbonate abundance NOT VERIFIED | Phase 3 |
| Survey scanner | **Compass**, **glass**, **redstone**, iron ingot | Read ice, soil percentage (A4, B5) | INVENTED tool | Phase 1 |
| Soil treatment unit | **Iron ingots**, **water bucket**, **bone meal**, **hopper** | Washed, fed planter soil | INVENTED machine | Phase 3 |

**Farming recipes (vanilla `farmland` rules kept)**

| Output | Recipe | Serves | Status |
|---|---|---|---|
| Washed regolith | Regolith + **water bucket** in a wash station (cauldron-style), brine drains to a salt item | Planter base (C1, A1) | *Game design* (washing NOT VERIFIED) |
| Planter bed | Hoe on washed regolith, then **bone meal** or compost as organic matter | Food, biomass | SIMPLIFIED (needs treatment, NOT VERIFIED) |
| Grow lamp | **Glowstone dust or redstone lamp** + **iron ingot** | Crops under dome | *Game design* |
| Lettuce, tomato | **Seeds** from vanilla `wheat_seeds` plus planter; 3 growth stages | Dead-tomato lesson (C1) | INVENTED crops |
| Biome seed kits | **Bone meal** + **wheat seeds** + carbonate rock + soil (per kit, *invented*) | Biomass, A5 | INVENTED; order real, speed fiction |

**Open for the Technical Lead:** exact shapes and KubeJS wiring. **Open for the SME:** source for the washing step, carbonate abundance, Phoenix latitude.

## 4. Science and Index ties

| Resource or terrain | Feeds | How (no change to Index numbers) | Source status |
|---|---|---|---|
| Regolith, basalt | Radiation shielding; Outgasser/Oven bake | Shielding numbers already in the balance sheet (*invented*) | Dose direction sourced (SME round 2); block value invented |
| Ground ice | Hydrosphere, Thaw, ground collapse | Extractor must sit on it; rate stays as in the balance sheet | Real buried ice (**SME checked: acceptable simplification** on depth and place, R1 R2); the game stylises depth |
| Hematite, sand, meteorite iron | Machine and glass cost | Build cost only | Real minerals (**SME checked: accurate** for hematite, sulfate, meteorite iron; sand ore **acceptable simplification**, see 2.2); amounts *invented* |
| Carbonate rock | CO2 sink (scrubber) | Matches Phase 3 rule A "scrubbers lock CO2 into carbonate rock" | Real chemistry; Mars carbonates exist (Jezero, R9) but **abundance NOT VERIFIED**; keep carbonate rock rare and label the scrubber output as game-scale |
| Perchlorate (soil percentage; "seams" invented) | Hazard; meltwater salts | Existing perchlorate exposure numbers (*invented*) | Perchlorate in Mars soil is real; levels 0.4 to 0.6% by mass in Phoenix leachate (R1), **accurate**; seams are **invented**; the scan reads a soil percentage (perchlorate is spread through the soil, not in veins) |
| Polar cap | Polar-frost sink | Existing slice 5 | CO2 cap real (balance sheet cites SME round 2) |
| Craters and dunes | Mood, exploring, shelter | Nothing | Real features; sizes stylised |
| Biocrust and soil | Biomass, O2/CO2 swap | Existing Phase 3 numbers | Succession order accurate (SME round 3); speed is fiction and labelled |

**Rule:** a block never changes a pressure or temperature number by itself. Machines do. Blocks decide **what you can build, where, and at what cost**. If the owner later wants mined resources to affect rates (for example richer ice means faster water), that is a balance change and goes to the Lead Instructional Designer and Subject Matter Expert first.

**Open question (Technical Lead):** the Hydrosphere percentage must be defined by what the world shows (water blocks counted, or machine tally). Today the Index is pure simulation. Terrain only needs to *support* it (ice to melt, basins that hold water). Do not build water-in-the-world until that definition exists.

## 5. Scope and cost (all unverified estimates)

| Plan | Contents | Estimate | Notes |
|---|---|---|---|
| **Recommended first slice: W1 shaped world** | Noise terrain, 3 biomes, hematite, sand, ground ice, small caves, dome pad; scanner as a stub; Phase 1 loop: dig, smelt, glass, bearings | 4 to 6 dev-days world plus 2 dev-days for blocks and textures (about 6 to 8 total) | Fits next to Stage 3 work. Gives the owner the Minecraft feel at once. Does not need ground collapse or craters |
| **Alternative: W1-lite plus Phase 1 greenhouse** | Same terrain but only 2 ores and no caves; build the sealed greenhouse with lettuce and tomato | About 7 to 9 dev-days | Pick this if the owner wants gardening to appear first. Delays caves and exploring |
| Later: W2 | Craters, dunes, canyon, cap, meteorite, probe wreck | 5 to 8 dev-days | After Phase 1 slice is playtested |
| Later: W3 | Collapse, spreading soil, biome seeds, meltwater pools | 8 to 12 dev-days | Needs the Hydrosphere definition (section 4) |

**Cut if credits are short (in order):** (1) frozen CO2 and sulfate blocks (reuse vanilla); (2) meteorite iron and wreck; (3) dunes and canyon (keep craters); (4) caves (shelters become dug by hand); (5) a separate tomato and lettuce (reuse vanilla beetroot and carrot, retexture later); (6) the survey scanner becomes a Monitor map page. **Never cut:** ground ice (it is the Hydrosphere quest), hematite (it gates parts), flat dome pad.

## 6. Walkthroughs

- **12-year-old, never used a modpack:** wakes in the dome, the Monitor says "pressure 0.6 kPa". Digs the nearest wall (red dirt) and finds nothing special; the journal says a red outcrop lies north. At the outcrop, iron. Craft a bearing. That is a quest with no quest log (pillar 1). Risk: nothing tells them dirt is shielding; the shelter message should say "thick walls of soil stop radiation".
- **Experienced 17-year-old:** wants caves and veins within the first hour; W1 gives both. Will try to "branch mine" for diamonds and find none: the journal should say why (invented joke line, tone check by the Artist and ID).

## 7. Decisions for the owner

1. **Go with W1 (shaped world) as the next world slice, or W1-lite plus greenhouse?** Recommend W1. *Ill-structured* (fun and learning compete for first place).
2. **Resources do not change Index rates, only build cost and placement.** Recommend yes (keeps the balance sheet valid). *Well-structured* once stated.
3. **Do we accept that new terrain applies only to new worlds?** Recommend yes. *Well-structured.*
4. **Are new blocks allowed for ores and ground ice (not retextured vanilla)?** Recommend yes, because textures are global in a modpack. *Well-structured.*
5. ~~Remove vanilla progression from the Mars dimension?~~ **Decided by the owner 2026-11-27: no, keep it.**
