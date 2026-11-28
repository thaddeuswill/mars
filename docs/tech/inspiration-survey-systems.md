# Inspiration survey: which existing mods to learn from, system by system

**Owner:** Technical Lead · **Data date:** Modrinth API (`api.modrinth.com/v2`), read **2026-11-28** · **Scope:** NeoForge or Fabric, Minecraft 1.20 to 1.21.1 · **Status:** research (docs only; nothing downloaded or run; no mod was seen on a screen)

**Why this exists.** Owner, 2026-11-28: "what are some mods we are taking inspiration from; we need to be taking a lot of inspiration for every aspect of this". This is the list, with a verdict per mod and the place in our code where the idea would land.

## How to read it

| Mark | Meaning |
|---|---|
| **V** | Verified: licence, loaders, 1.21.1 build and date read from the Modrinth API on 2026-11-28 (or a raw GitHub file named in the row) |
| **M** | From memory or inference about what the mod does or how its UI behaves. **Not verified.** I did not open the mod or its source |
| **USE** | Adopt as a dependency or an owner-installed add-on |
| **STUDY** | Copy the idea, write our own (code and assets stay ours; no copying of ARR or NC files) |
| **SKIP** | Does not fit, and the reason |
| Effort | small ≤ 1 day, medium 2 to 5 days, large > 5 days. **All estimates unverified**, no velocity data |

**Licence note (owner is the only player).** A licence only bites on what we would ship in a pack the owner builds. Rule used here: MIT, LGPL, MPL, Unlicense, CC0, CC-BY are safe to reference in a `.mrpack`. CC-BY-NC-SA (Jade, WTHIT, Patchouli), GPL and "All Rights Reserved" mods are fine for the owner to install by hand but I do not put them in the shipped pack list; ARR mods are never bundled (`docs/tech/mod-licenses.md`). Download counts are Modrinth's and ignore CurseForge. CurseForge distribution flags: **still not checked** (host blocked).

**Already decided, not reopened:** custom `mars:planet_1` dimension; our own machines for anything learning-critical; Mekanism and Create optional and late (ARCHITECTURE 2.15); KubeJS for recipes only; vanilla crafting; Iris + Sodium as the default look; EMI as the recipe viewer (`docs/tech/optional-mods-and-shaders.md`).

---

## 1. Space, planets and terraforming

**What we do now.** Planet data per dimension in `pack/data/mars/planets/`, the Planetary Index simulation in `mars.index.sim` (pure Java), gates in `pack/data/mars/gates/`, our chunk generator in `mars.terrain` (ARCHITECTURE 2.23), sky in `mars.sky`. Terraforming is the whole game, not a side system.

| Mod | Facts (V) | Idea worth having | Verdict |
|---|---|---|---|
| **Galacticraft 5** | MIT ([LICENSE](https://raw.githubusercontent.com/TeamGalacticraft/Galacticraft/main/LICENSE), read); GitHub README says pre-alpha builds only, no release, Fabric `minecraft.version=1.21.1`, Forge "after" Fabric alpha. Not on Modrinth under that name | Space station, rocket tiers gated by planet, oxygen sealer rooms, planet "gravity" per dimension | **STUDY** (ideas only). No NeoForge build, not released; we have no rockets |
| **Galacticraft Legacy** | MIT; Forge **1.12.2 only**; last update 2023-12-09 | Same, older | **SKIP** (wrong version) |
| **Ad Astra** | Terrarium Licence (code MIT; the [LICENSE](https://raw.githubusercontent.com/terrarium-earth/Ad-Astra/1.19/LICENSE) file I read only for its first section; asset terms **not read**); NeoForge and Fabric 1.21.1 build `1.16.26`, 2026-09-13; 6.1 M downloads; required on both sides | Planets as data-driven dimensions, per-planet gravity and oxygen flags, rocket tiers, oxygen distributor | **SKIP** as a dependency (decided: "Ad Astra is out"). **STUDY** the per-planet data flags (M) against our `planets/*.json` |
| **Advanced Rocketry Reworked** | ARR; Forge 1.12.2 only; 2025-08-20 | Planet generation, atmosphere pressure per planet, terraforming a planet's air and temperature by machine | **SKIP** (ARR, wrong version). **STUDY** the *idea* that atmosphere is a per-planet number a machine changes (M): that is our Index, already built |
| **Creating Space** | MIT; NeoForge 1.21.1 `1.7.23` 2026-10-21; needs Create (M) | Create-style rockets to other planets | **SKIP**: rockets are out of scope |
| **Create Cosmonautics** | GPL-3.0-or-later; NeoForge 1.21.1 beta `26.08.307` 2026-08-09; needs Create (M) | Same | **SKIP** |
| **Mars-themed dimension mods** | Search "mars" returns: Mars plugin (Bukkit, ARR, 1.3 k downloads), Mars Renaissance (ARR, 173 downloads), Marston's Fallen Stars (ARR, meteor-impact sites, 392 downloads) | No maintained, open Mars dimension mod exists on Modrinth for 1.21.1 | **SKIP**. Marston's meteor sites: **STUDY** the idea of rare impact structures if W1 terrain wants a landmark (M) |

**Gap.** None of these model *change over time by a planetary number*; they treat a planet as a place you travel to. That is our unique piece. The gap is not in code but in "going elsewhere": we have one planet and no orbit view.
**Recommended next steal.** *Small:* a data flag block per planet (gravity scale, breathable, `suit_required`) copied from the Ad Astra / Galacticraft shape, so a later `mars:planet_2` needs no code (**effort ≤ 1 day, unverified**). *Large, not now:* an orbit map in the Monitor.

---

## 2. Oxygen, pressure, suits and life support

**What we do now.** Pressure and O2 are Index parameters; `mars.hazards` and `pack/data/mars/hazards/`; sealing is ARCHITECTURE 2.3; the Breathable hold gate; oxygen maker and water reclaimer as our machines (ARCHITECTURE 2.16, `mars.machines.WaterReclaimerBlock`). The ROADMAP "oxygen/suit mod" item is already closed as "build our own" (`optional-mods-and-shaders.md` section 3).

| Mod | Facts (V) | Idea worth having | Verdict |
|---|---|---|---|
| **Ad Astra** | see section 1 | Oxygen distributor fills a sealed room by flood fill; spacesuit armour with an oxygen tank item; breathing bar on the HUD (M) | **STUDY** (flood fill: ARCHITECTURE 2.3; tank-on-HUD for the suit) |
| **Galacticraft** (4 and 5) | see section 1 | Oxygen sealer, tanks, "leaking" if a block is open, tiered suits (M) | **STUDY** |
| **Simple Dive Gear** | CC0-1.0; 522 k downloads; "air works like Subnautica's oxygen" (Modrinth description); NeoForge build **not checked** | Air-timer bar above the hunger row, refill item | **STUDY** the HUD bar. CC0 means we could even copy code, but the timer is trivial |
| **Cosmonautics: Deep Air** | MIT; NeoForge 1.21.1, `1.0.2` 2026-06-24 | A bridge mod between Create: Deep Seas and Cosmonautics so entities breathe | **SKIP** (not a suit) |
| **Bottled Air** | ARR | Bottle of air as a pocket oxygen refill | **SKIP** (ARR). The *idea* of a craftable canister is already in our spine (REAL idea: CO2 canister) |
| **Tough As Nails** | ARR; NeoForge 1.21.1 build is a 2024 beta | Temperature and thirst as visible meters, clothing as insulation | **STUDY** meter HUD only (M) |
| **Thirst Was Taken** | MIT; NeoForge 1.21.1 `2.1.5` 2025-09-21 | Thirst bar, purified water via a purifier, mod-friendly API | **STUDY** water purification chain for the Meltwater treatment machine (perchlorate, B5) (M) |
| **Dehydration** | GPL-3.0; Fabric-only for 1.21.1 | Hydration mechanic | **SKIP** (Fabric, GPL) |
| "Oxygen" search | Returns only a pre-1.13 oxygen refill tweak, Bottled Air, Simple Dive Gear, Deep Air, a mob altitude-sickness mod. No pressure-driven suit mod | | confirms "build our own" |

**Gap.** Nobody models oxygen as a *pressure number the whole planet rises through*. Their suits are on/off; ours should show the number that matters (pressure vs a Mars-safe range).
**Recommended next steal.** *Small:* the Ad Astra / Simple Dive Gear pattern of a **HUD bar above the hunger bar** for suit O2 and pressure, driven by our `breathable_air` condition (**≤ 1 day, unverified**). *Medium:* flood-fill sealing (2.3) shown as a coloured outline the way sealer mods do (M).

---

## 3. Power, fluids and gases, machine frameworks, side-config GUIs

**What we do now.** One grid per planet (solar, battery, Oven, Outgasser, reclaimer; ARCHITECTURE 2.19), one `MachineBlock` family and data files in `pack/data/mars/machines/` (2.16), a Monitor Power row. No pipes, no fluids or gases as items yet, no side configuration, no wrench.

| Mod | Facts (V) | What players already expect from it (M unless stated) | Verdict |
|---|---|---|---|
| **Mekanism** | MIT; NeoForge `10.7.19.85` 2026-04-10; 4.0 M downloads | Machine GUI: input left, output right, energy bar on the left edge, progress arrow in the middle; **tabs on the side for Side Config, Upgrades, Security**; colour-coded face configuration (each face Input/Output/Off); gas tanks with tooltip amounts; **configurator item** to rotate and set sides; Jade shows contents | **USE** late and optional (decided). **STUDY** the GUI layout and the six-face colour config for our machines |
| **Mekanism Generators** | MIT; same build | Solar, wind, heat generator; "your output depends on sky and height" tooltips | **STUDY** wind/solar tooltips for the weather mast and dust-stage solar output |
| **Create** | Create licence (code MIT, assets ARR); NeoForge 1.21.1 `6.0.10` 2026-04-21; 28.6 M downloads | **Goggles** show stress and speed overlays on blocks; wrench rotates blocks; machines visible in motion; Ponder (see 7) | **USE** late and optional (decided). **STUDY**: "look at it with an item to see numbers" (our Monitor does this remotely) |
| **Immersive Engineering** | `LicenseRef-Custom` (text **not read**); NeoForge 1.21.1 `12.4.2-194` 2025-07-05 | Multiblock machines built from a hammer-placed blueprint; the in-game manual (an own book with Patchouli-like pages); wires carry RF | **STUDY** multiblock-with-ghost-preview for the Atmospheric processor and beacon (our ARCHITECTURE 2.18 footprint code is the start). Licence unread, so no copy |
| **Powah!** | LGPL-3.0-only; NeoForge 1.21.1 `6.2.10` 2026-04-29 | Tiered generators and energy cells, numbers shown in tooltips, energy cell fill bar | **STUDY** battery tooltip and tier names. Not needed as a dependency |
| **Industrial Foregoing** | MIT; NeoForge 1.21.1 beta `3.6.39` 2026-07-04 | Machine-with-augments; its own "Plastic" chain | **SKIP** (heavy; beta) |
| **Pipez** | ARR; NeoForge beta 2026-07-01 | Cheap pipes with a mode per face | **SKIP** (ARR). The *per-face mode* idea is the same as Mekanism |
| **Thermal (Expansion)** | ARR; Forge 1.20.1 only on Modrinth, 2024-05-26; **no 1.21.1 build seen** (the 1.21 line may exist on CurseForge; **not checked**) | Machine GUIs and **redstone-control tab** (always, never, with signal) | **STUDY** the redstone-control tab: a "run only when powered by a switch" is the cheapest player control we can add |
| **Flux Networks** | Not found on Modrinth by name (CurseForge-only; **not checked**). Only a Fabric fork (21.9 k downloads) appears | Wireless energy by network id, no cables; the idea of "one network, many points" | **STUDY** only. Our "one grid per planet" is already the same shape, without any wiring |
| **Applied Energistics 2** | `LicenseRef-Multiple`; NeoForge `19.2.20` 2026-11-18 | Wrench-free network, terminal | **SKIP** (storage, not our theme) |

**Gap.** Our machines have no player-facing control beyond "place it". Players of Mekanism and Create expect: (1) an energy bar that can be read at a glance (we have the Monitor but not a per-machine bar), (2) a wrench or configurator, (3) faces that accept and emit, (4) a redstone-control choice. Phase 2 and 3 machines (processor, emitter, extractor) will move gas and water; right now those flows are invented as numbers on the grid, not as items.
**Recommended next steal.** *Small:* a per-machine energy bar and a status lamp on the GUI (matches the "one framework" in 2.16; effort ≤ 1 day each). *Medium:* a **machine control tab** with run/stop/redstone, copying Thermal and Mekanism (3 to 5 days, unverified). *Large:* six-face side config and a wrench: **do not build** until a machine truly needs input from a side; a "Mars wrench" is `STUDY`-only.

---

## 4. Worldgen: non-Earth terrain, craters, dunes, ore veins, caves

**What we do now.** `mars.terrain` chunk generator with noise, craters, dunes under a fixed wind, buttes, canyon, ripples, conductor ores and carbonate (ARCHITECTURE 2.23, 2.23a). This was judged "better Earth terrain, not Mars terrain" for every mod below in `optional-mods-and-shaders.md` section 3.

| Mod | Facts (V) | Idea worth having | Verdict |
|---|---|---|---|
| **Terralith** | Stardust Labs Licence; NeoForge `2.6.2` 2026-06-09; 25 M downloads; server required | Hand-tuned biomes with clear colour identity and subtle terrain slope per biome; "cave" biomes underground | **STUDY** (licence is custom, copy nothing). Look at its colour-per-biome discipline and its use of density-function overrides (M) |
| **Tectonic** | MIT; NeoForge `3.0.28` 2026-09-11; reshapes the **overworld** height | Large-scale continental noise, high mountains, deep rifts, using density functions and the 1.18+ noise settings datapack | **STUDY** the datapack noise settings (MIT, so we may read and borrow code with notice); could inform canyon depth. Not a dependency: it only touches `minecraft:overworld` |
| **Incendium** (Legacy) | Stardust Labs; latest NeoForge 1.21.1 build is a 2024 alpha | A non-Earth dimension with its own biomes, structures and mood | **STUDY** how it keeps a coherent palette and per-biome particles (M) |
| **Nullscape** | Stardust Labs; `1.2.14` 2025-10-07 | The End rebuilt: floating islands, haze colours | **STUDY** for **mood** (fog and ambient particles per biome); same licence rule |
| **Oh The Biomes You'll Go (BYG)** | Multiple licences; last build 2024-12-18; **no NeoForge 1.21.1 build** | Hundreds of biomes; a "dunes" desert set | **SKIP** |
| **Regions Unexplored** | MIT (V, from previous survey); 10 M downloads | Biome blends and large biome shapes | **SKIP** (Earth) |
| **Alex's Caves** | not checked in depth; 11.6 M downloads | Themed cave biomes (acid, magnet, toxic) with strong ambience | **STUDY** the *idea* of themed cave biomes (ice cave for the ice extractor, hematite cave). Licence **not read** (M) |
| **Better Deserts, Sandy Dunes** | Better Deserts (42 downloads); Sandy Dunes is ARR, a resource pack (V) | Dune shape and sand colour variety | **SKIP** |
| "Craters" | CraterLib is a different thing (Create-ish library); "no creeper craters" is a tweak (V from search) | No crater mod found for terrain | **SKIP** |

**Gap.** Our terrain has one regime. Real Mars has terrain that changes by region (polar cap, canyon, dust basin, volcanic highland). W1 already has landforms. Missing: **cave and underground variety** for the ice extractor path and strong *per-region mood* (colour and particle) so the player knows where they are.
**Recommended next steal.** *Medium:* a second biome (or sub-region tint) with its own colour and fog from our sky keyframes, learning from Nullscape and Incendium for per-biome mood (**2 to 5 days, unverified**). *Medium:* one underground biome for ground ice (Alex's Caves idea). *Small:* adopt Tectonic's MIT datapack density-function patterns where useful for canyon slopes.

---

## 5. Weather, dust storms, particles and ambience

**What we do now.** Seeded storm schedule that cuts solar output (ARCHITECTURE 2.22, `pack/data/mars/storms/`); sky/fog follow pressure (2.5.5); heat shimmer and dust motes are data-driven (2.5.4); sound hooks (2.12); weather mast (2.11).

| Mod | Facts (V) | Idea worth having | Verdict |
|---|---|---|---|
| **Weather2** | Lineage of storm mods; search returns only **addons** (Weather2 Additions, 56 k downloads); the core is not listed on Modrinth for our versions | Dynamic storm cells that move across the map and have a visible edge | **STUDY** the moving-front idea for the dust storm (M). Our storm is a global value; a visible wall of dust moving in would make "storm incoming" legible |
| **Improved Weather** | CC-BY-4.0 **datapack**; NeoForge `v1.9` 2024-09-05; 60 k downloads | A sandstorm in desert biomes, a datapack-only approach | **SKIP** (Earth biomes). Datapack-only approach is the same philosophy as ours |
| **Particle Rain** | MIT; client only; NeoForge 1.21.1 `v4.0.4` 2026-11-19; 23.4 M downloads | Particle-based rain, ground splash, leaf and dust particles with smooth density | **STUDY** the particle lifecycle for dust on a storm day (M). Do not install: it replaces rain, which Mars has none of |
| **Ambient Environment** | MIT; client only; NeoForge `18.0.0.2` 2024-10-10 | Biome-aware wind and ambient sounds with fade by height | **STUDY** its sound-fade-by-position design for wind that grows with pressure (M) |
| **Sound Physics Remastered** | GPL-3.0-only; client; NeoForge 1.21.1 `1.5.2` (beta) 2026-10-21; 58 M downloads | Reverb in enclosed spaces, muffling by walls | **USE** (optional, owner-installed): inside a sealed dome the room sounds different, which teaches "sealed vs open" through the ears (M). Not bundled (GPL, beta) |
| **Presence Footsteps** | PolyForm Shield; **Fabric only** for 1.21 (V); client | Different footsteps on different blocks | **SKIP** for NeoForge. **STUDY** the block-to-sound map as data: dust vs hematite vs ice footsteps (M) |
| **Dynamic Surroundings** (Rebirth / Fabric forks) | MIT remaster present; the Fabric one is 7.3 M downloads; NeoForge build **not checked** | Footsteps, sound by biome, rain splash, dripping, aurora | **STUDY** |
| **Immersive Weathering** | LGPL-3.0; **no 1.21.1** build (1.20.1 last) | Blocks weather over time (moss, rust) | **SKIP**. The *gradual weathering over time* idea matches our solar-panel dust stages, already built |
| **Particular** | LGPL-3.0; Fabric only, 1.21.4 (V) | Waterfall and splash particles | **SKIP** |

**Gap.** The storm is a number and a sky colour. There is no *place* you can see it coming from, and no audio that tells you pressure.
**Recommended next steal.** *Medium:* a visible dust wall as a client-side particle volume that sweeps across over 30 to 60 seconds before the schedule's peak (Weather2 idea). *Small:* a **block-to-footstep map as JSON** (Presence Footsteps idea) for dust, hematite, ice and metal. *Small:* owner installs Sound Physics Remastered and listens.

---

## 6. Farming, greenhouses, hydroponics, plant growth

**What we do now.** Phase 1 greenhouse planter is **unbuilt**; the food lesson C1 and the seed kits are planned (`docs/design/machine-priorities.md` rows 2, 9, 10). The biomass parameter is in the Index; the planned biggest item is the seed-kit spreading engine (ARCHITECTURE 2.16, 8 to 12 days).

| Mod | Facts (V) | Idea worth having | Verdict |
|---|---|---|---|
| **Farmer's Delight** | MIT; NeoForge `1.21.1-1.3.4` 2026-08-29; 26 M downloads | **Rich Soil**: a block that makes crops grow faster and prevents trampling; **tilled-soil-as-tool** (shovel, knife); a clear "worked soil" chain. Cooking pot with a visible input-to-output display | **STUDY**: our **Soil treatment unit** is the same concept as Rich Soil; reuse its tooltip style of "grows crops faster, needs organic matter" (M). MIT so a dependency is possible, but we do not want Earth crops |
| **Botany Pots** | LGPL-2.1-only; NeoForge `21.1.44` 2026-06-13; 2.2 M downloads | Crops grow in a **pot** with a defined soil and seed; the growth time, drops and compatible soils are all **datapack JSON** | **STUDY**: this is exactly "greenhouse planter as data". Closest existing model to what we need |
| **Hydrofarm** | MIT; Fabric+NeoForge; 4 k downloads; no 1.21.1 listing found | Hydroponic trays with water and nutrient inputs (M) | **STUDY** the "water + nutrient + light" inputs |
| **Botania** | Botania licence; **no NeoForge 1.21.1** on Modrinth; Forge/Fabric builds | Flowers as generators and "mana" linking; strong visual feedback for growth | **SKIP**. **STUDY** nothing: magic framing |
| **Mystical Agriculture** | MIT; NeoForge `8.0.29` 2026-10-22; 1.35 M downloads | Crops that grow as resources; tiers of essence | **ANTI-PATTERN.** Plants that produce abstract "essence" make growth a grind and teach nothing about soil, water, light or CO2. We must keep plant output tied to Index values |
| **Pam's HarvestCraft 2** | multi-part; ~280 k downloads | Dozens of Earth crops | **SKIP** (content-for-content's-sake; contradicts "pressure, not grind") |
| **Dynamic Trees** | MIT; NeoForge `1.7.2` 2026-06-10 | Trees grow shape by sunlight and soil; species and soil compatibility in data | **STUDY** the soil/species compatibility lookup for our flora succession chain (M) |
| Vanilla ideas | | Light level, hydration (farmland), bone meal, moss, azalea. Our design already needs "composted" bone meal substitute (machine-priorities R4) | Use vanilla |

**Gap.** There is no planter yet. The risk is copying an Earth-farm loop (till, plant, wait, harvest) which does not teach why Mars soil fails (perchlorate, no organics, low pressure).
**Recommended next steal.** *Medium:* a **greenhouse planter block with data-driven crops and soils** (Botany Pots shape: `soil` and `crop` JSON, growth conditions listed in the tooltip), with the Index's pressure and temperature as growth conditions; 3 to 5 days (**unverified**). *Medium:* the Soil treatment unit as a **Rich-Soil-like block** whose tooltip says what has been removed (perchlorate) and what is missing (organics).

---

## 7. HUD, scanners, journals and in-game docs

**What we do now.** Planetary Monitor screen (`mars.client.MonitorScreen`, 232 px, goal row with bar and "~N sols"), J-key journal (`mars.client.JournalScreen`, ARCHITECTURE 2.20), story messages and archive (section 9), no look-at overlay, no recipe viewer, no minimap by design ("the planet is the quest log").

| Mod | Facts (V) | Idea worth having | Verdict |
|---|---|---|---|
| **Jade** | CC-BY-NC-SA-4.0; NeoForge `15.10.6` 2026-08-06; 75 M downloads; client optional, server optional | A tooltip when you look at a block: name, mod, **extra lines from plugins** (e.g. "Power: running", "Effect on planet: +0.2 kPa/sol"). Players of every large pack expect this | **USE** (owner-installed, not bundled because of NC-SA) **plus a small Jade plugin from us** (M: plugin API uses `IWailaPlugin`; not verified). Gives the "Effect on planet" and "Costs" lines for free at a glance. Fallback if Jade is absent: our own look-at line |
| **WTHIT** | CC-BY-NC-SA-4.0; NeoForge `12.10.3` beta (2026-11-08) | Same as Jade | **SKIP** (pick one; Jade is more used) |
| **Patchouli** | CC-BY-NC-SA-3.0; NeoForge `1.21.1-93` 2026-03-08; 37.7 M downloads; required both sides | JSON-defined book pages with text, images, crafting recipes, spotlights, templates and **multiblock previews** (the 3D ghost of a structure) | **STUDY** for the book model. **Licence:** NC-SA means pack content cannot be a commercial product, and a book written in Patchouli could carry SA terms; our journal is our own code and CC BY 4.0 (`mod-licenses.md`). Keep our journal; do not adopt |
| **Modonomicon** | MIT AND CC-BY-4.0; NeoForge `1.21.1-1.123.1` 2026-11-16; 1.55 M downloads | A newer data-driven book with conditions (**unlock on advancement**), categories and a search | **STUDY** the "page unlocked by condition" pattern (M); matches our gate-keyed journal entries. MIT, so adopting it later is licence-safe |
| **GuideME** | Multiple OSS licences; NeoForge `21.1.22` 2026-10-29; 3.4 M downloads | Markdown-style guide pages with in-world highlights | **STUDY** (licence mix unread) |
| **Ponder** (inside Create) | MIT code, assets ARR | Animated 3D scenes that teach how a machine is built and used | **STUDY** the format. **Ponderer** (MIT, `1.12.1` 2026-11-22, NeoForge 1.21.1; needs Create? **not checked**) and **Ponder for KubeJS** (MIT, 2026-01-09) let a pack add its own scenes. **USE later** only if the owner wants animated machine tutorials |
| **Xaero's Minimap and World Map** | ARR; NeoForge 1.21.1 `26.6.0` and `1.47.0`, 2026-10-05; 122 M and 108 M downloads | Always-on map and waypoints | **SKIP.** A map answers "where do I go" and weakens "the planet is the quest log" and "compact before wide". The owner may install by hand for their own debugging |
| **JourneyMap** | ARR; NeoForge `1.21.1-6.0.10` 2026-11-22 | Same | **SKIP** |
| **FTB Quests** | Not on Modrinth as the base mod; FTB Library is ARR (`mod-licenses.md`) | Quest tree with tasks and rewards | **REJECTED** (owner: custom journal). Reasons: a quest list tells the player what to do and replaces "the planet is the quest log"; ARR library; a checklist teaches nothing about causes |
| **EMI** | MIT; NeoForge `1.1.24+1.21.1` 2026-05-13; 31 M downloads | Recipe lookup, favourites, tag browsing | **USE** (decided) plus a hide-until-gate plugin |
| **JEI** | MIT; NeoForge 1.21.1 beta build `19.76.1.483` 2026-11-28; 87 M downloads | Same | Acceptable swap; **SKIP** now |
| **AppleSkin** | Unlicense; NeoForge 1.21.1 `3.0.9` 2026-04-01; 101 M downloads | Food and saturation values on the hunger bar | **USE** (owner-installed; Unlicense is safe to bundle). Pairs with the greenhouse lesson C1 |

**Gap.** There is no "look at a block and see what it does" layer; the Monitor requires walking to it. No animated tutorial. No gate-keyed docs system beyond our journal.
**Recommended next steal.** *Small:* Jade plugin line (**1 to 2 days, unverified**, needs a soft dependency like Iris in `optional-mods-and-shaders.md` S-4). *Medium:* a **"page unlocked by condition"** entry type in the journal, copied from Modonomicon, keyed on gates. *Large, later:* a few Ponder scenes via Ponderer or Ponder for KubeJS if Create is installed; otherwise our own 3 to 4 screenshot-based "how it works" cards.

---

## 8. Modpack tooling

**What we do now.** `pack/` has `mods/`, `kubejs/`, `config/`, `defaultconfigs/` and `licenses/`; packwiz is the plan; the launch guide is `docs/tech/launch-guide.md`; Modrinth `.mrpack` as main channel (`mod-licenses.md`).

| Tool | Facts | What it gives us | Verdict |
|---|---|---|---|
| **packwiz** | MIT (V, `mod-licenses.md`); `[option] optional` fields read in source | Text-file pack in git, optional mods with defaults | **USE** (decided) |
| **Modrinth `.mrpack`** | Modrinth rule: bundled mods must allow redistribution (V from search, `mod-licenses.md`) | One-file import into the Modrinth App and Prism | **USE** |
| **Prism Launcher** | Not checked in this survey (M: open source, imports `.mrpack`, per-instance Java) | The owner already has the launch guide; instance isolation | **USE** if the owner prefers; no work for us |
| **Modrinth App** | M | One-click pack install | **USE** |
| **CurseForge** | Host blocked; distribution flags **not checked** | | Still blocked. Only needed if we publish there |
| **Mod Menu** | MIT; **Fabric/Quilt only** (V) | In-game mod list | **SKIP** (not NeoForge) |
| **Cloth Config** | LGPL-3.0; NeoForge `15.0.140` 2024-09-16 | Config screens for other mods | **SKIP** (we use JSON data and `/mars` commands) |
| **KubeJS and ProbeJS** | decided | | decided |

**Gap.** `packwiz` pack files exist as a plan; the owner has no one-click launch yet beyond the guide. Nothing here is a code task.
**Recommended next steal.** *Small:* a **shader options file + EMI plugin + Jade** listed as `optional = true` entries so the owner can toggle (see `optional-mods-and-shaders.md` 1.4).

---

## 9. Quality of life

| Mod | Facts (V) | Verdict |
|---|---|---|
| **Iris, Sodium** | decided (`optional-mods-and-shaders.md`) | **USE** |
| **Inventory Profiles Next** | AGPL-3.0-or-later; NeoForge `2.2.5` 2026-03-25; client | **USE** (owner-installed; not bundled). Sorting inventory is the owner's taste |
| **Mouse Tweaks** | BSD-3-Clause; NeoForge `1.21-2.26.1` 2024-08-17 (V); client | **USE** (safe to bundle) |
| **Controlling** | MIT; NeoForge `19.0.5` 2025-03-30 | **STUDY**: a key-binding search. Low value for one player |
| **Zoomify** | LGPL-3.0; **Fabric 1.21.1 only** (V; no NeoForge 1.21.1 file found) | **SKIP**; vanilla spyglass and F-keys are enough |
| **Distant Horizons** | LGPL-3.0; NeoForge `3.3.3-1.21.1` 2026-09-29; 38.6 M downloads | **USE** (owner tries after shaders; already in `optional-mods-and-shaders.md`) |
| **Entity Culling** | tr7zw Protective Licence | **SKIP** (custom licence; our scenes have few entities) |
| **Sophisticated Backpacks** | 21 M downloads; licence not read | **SKIP** |
| **Lootr** | ARR | **SKIP** (our starter chest is a normal chest) |
| **Carry On** | 28 M downloads; licence not read | **SKIP** |
| **Create Jetpack** | Custom licence; NeoForge `5.2.1` | **SKIP** (needs Create; mobility shortcut cuts "wide building is earned") |

**Gap.** None serious. QoL is the owner's taste.
**Recommended next steal.** *Small:* ship a documented "owner kit": Iris + Sodium + EMI + Jade + Mouse Tweaks + AppleSkin as `optional = true` in `pack/mods/`, verified together by a real client run (not done).

---

## 10. Top picks, ranked

| # | Pick | Verdict | Size | Lands in |
|---|---|---|---|---|
| 1 | **EMI** with hide-until-gate | USE | medium | new client plugin; reads gate `unlocks` lists |
| 2 | **Jade** + our plugin for "Effect on planet / Costs" | USE (owner-installed) | small | `mars.machines`; reads `machines/*.json` |
| 3 | **Botany Pots** data model for the greenhouse planter | STUDY | medium | `mars.machines` plus `pack/data/mars/plants/` (new) |
| 4 | **Mekanism / Thermal** machine tabs (energy bar, redstone control) | STUDY | small to medium | `MachineBlock` GUI |
| 5 | **Ad Astra / Galacticraft** suit O2 HUD and oxygen flood fill | STUDY | small and medium | `mars.hazards`, ARCHITECTURE 2.3 |
| 6 | **Modonomicon** unlock-by-condition pages | STUDY | medium | `mars.journal` |
| 7 | **Weather2** visible storm front | STUDY | medium | `mars.client.atmosphere`, `storms/` |
| 8 | **Sound Physics Remastered**, **AppleSkin**, **Mouse Tweaks**, **Distant Horizons** as the owner's optional stack | USE | small | `pack/mods/` optional entries |

## Revisit if

Galacticraft 5 ships a NeoForge release; Modrinth shows a maintained Mars or planet-data mod for 1.21.1; Jade's or Patchouli's licence changes; the owner decides to publish the pack (then re-check every NC-SA, GPL and AGPL row and the CurseForge flags).
