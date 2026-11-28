# Inspiration survey: look, sound and presentation

**Owner:** Lead Artist · **Date:** 2026-11-28 · **Status:** research, docs only; nothing downloaded, installed or run; no reference was seen on a screen this session · **Stability:** fluid
**Why:** owner: "what are some mods we are taking inspiration from; we need to be taking a lot of inspiration for every aspect of this."
**Honest answer up front:** until now our look was grounded in **real NASA hardware and imagery**, not in other games. This survey names the games, films and mods we should deliberately study, and says what we take from each. We copy **ideas and design rules, never art, text, sound or code**.

## 0. How to read this

| Mark | Meaning |
|---|---|
| **verified (Modrinth, 2026-11-28)** | I opened `api.modrinth.com` JSON for it: licence id, type, latest build, dates. This proves the project exists and what it is licensed as. **It does not mean I saw its look.** |
| **from memory, not verified** | Games, films, shader looks, art direction. My own recollection; details may be wrong. Treat as a pointer for a person to look at, not a fact. |
| **not found** | Searched Modrinth, no matching base project. |

Rules for every row: **reference only**. No texture, model, sound or shader code from any of these is copied or bundled. Licences are listed so nobody is tempted. Mod and pack names help a human find the look; our pack has its own art (`docs/art/style-guide.md` section 1). Effort figures are my estimates, **unverified**, in agent-hours.

Settled and respected here: clouds vanilla until E-10; sunset band and aureole deferred (owner reversal needed); the made-up band (purple, dashed, plate) for invented machines; real hardware is the reference; 1 block = 1 m = 16 px; owner is the only player, not a classroom; shaders are the owner's optional default (`docs/tech/optional-mods-and-shaders.md`, already reviewed, not repeated); no post-process shader of our own; colour is never the only signal.

### Verified mod and pack facts used below

| Project | Type | Licence id | Latest build seen | Source |
|---|---|---|---|---|
| Mekanism | mod | MIT | 10.7.19.85 release, 2026-04-10, NeoForge 1.21.1 (21 builds) | verified |
| Create | mod | LicenseRef-Create-Mod-License | 6.0.10+mc1.21.1 release, 2026-04-21 (11 builds) | verified |
| Immersive Engineering | mod | LicenseRef-Custom | project updated 2025-07-05; 1.21.1 build **not checked** | verified (licence only) |
| Thermal Foundation | mod | All Rights Reserved | project updated 2024-06-19; 1.21.1 **not checked** | verified (licence only) |
| Faithful 32x | resource pack | LicenseRef-Faithful-License | updated 2026-09-15 | verified |
| Stay True, John Smith Legacy, Sphax (base packs) | resource pack | n/a | search returns only **add-ons** (e.g. John Smith Legacy Models Addon, CC-BY-NC-SA-4.0; Stay True Amendments, MIT); base packs **not found** | not found |
| Terralith | mod | Stardust Labs License | updated 2026-09-27 | verified |
| Tectonic | mod | MIT | updated 2026-11-07 | verified |
| Complementary Reimagined / BSL / Photon | shader | Custom / All Rights Reserved / Custom | see `optional-mods-and-shaders.md` | verified earlier |
| AmbientSounds | mod | LGPL-3.0-only | 6.3.11 release, 2026-10-19, NeoForge 1.21.1 | verified |
| Sound Physics Remastered | mod | GPL-3.0-only | NeoForge 1.21.1 build is **beta** (1.5.2, 2026-10-21) | verified |
| Dynamic Surroundings Rebirth (uawiegrfv) | mod | MIT | 5,974 downloads; not checked further | verified (listing) |
| Weather Storms & Tornadoes | mod | All Rights Reserved | listing only | verified (listing) |
| Gentler Weather Sounds | resource pack | Apache-2.0 | listing only | verified (listing) |
| Create Ponder [Frozen] | mod | MIT | last update 2024-05-19; Ponder now ships inside Create (**not verified**) | verified (listing) |
| Jade | mod | CC-BY-NC-SA-4.0 | updated 2026-10-13 | verified (listing) |
| Immersive Weathering | mod | LGPL-3.0-only | updated 2025-01-23 | verified (listing) |
| Mars resource packs | resource pack | n/a | search "mars" returned only unrelated packs (enchanted-book textures, a "Cold Hands" animation pack, an Ad Astra recolour with ARR). **No Mars terrain pack found** | not found |

## 1. Planet and terrain look

| Reference | Mark | What we copy | What we avoid | Where it applies |
|---|---|---|---|---|
| **Planet Crafter** (game) | memory | The core feeling: the sky, ground and horizon shift stage by stage as your terraforming meters rise, and you can point at the change. A visible meter for each stage | Its saturated, stylised colours; its ore and vegetation look | Our keyframes: `docs/art/planetary-keyframes.md`, `pack/assets/mars/keyframes/sky.json` |
| **Per Aspera** (game) | memory | Map-level colour story: rust, then dust-brown with ice and dark basalt patches, then green and blue creeping in. The idea that the planet colour **is** the scoreboard | Strategy-map UI; top-down perspective | Late-stage biome tints, W3 palette (terrain reference section 3) |
| **Surviving Mars** (game) | memory | Warm orange haze and long soft shadows; low, readable horizon silhouettes with domes and a few props | Its cartoon dome look and bright saturation | Horizon framing for screenshots; dome-vs-plain contrast |
| **Outer Wilds / No Man's Sky palettes** (games) | memory | Outer Wilds: strong, simple colour per place so you know where you are. No Man's Sky: palette variety within one planet via a few controlled hue families | NMS neon palettes; anything not grounded in real Mars | Keeping each biome to one hue family (terrain reference: "Mars is brown-orange almost everywhere") |
| **The Martian (film) grade** | memory | Orange-ochre ground, pale desaturated sky, harsh low contrast shadows; film colour is warmer than real rover colour (stylised) | Over-orange sky; we have real white-balanced images | Cross-check only: our sky is butterscotch at 0.6 kPa, blue near the sun at sunset (SME owns accuracy) |
| **NASA Perseverance / Curiosity imagery** | search terms only, not opened | Real colour, real scale, real clutter. Search: `Mars true color Curiosity Mastcam white balanced`, `Perseverance Mastcam-Z Jezero panorama`, `Phoenix lander panorama polygon terrain`, `HiRISE Mars dunes` | Colour-boosted "enhanced" frames; images are not copied into the repo (NASA media guidelines still unchecked) | Terrain table `docs/science/mars-terrain-reference.md` section 1 already carries the search terms |
| **Terralith / Tectonic** (Minecraft; Terralith: Stardust Labs License; Tectonic: MIT; both verified listings) | verified listing; look from memory | Study them as the **anti-reference**: dramatic rolling hills, tall spires, varied tree biomes, rivers | Everything: "pretty" Earth terrain. Mars plains are **flat and empty**; relief comes from craters, dunes, ridges, scarps (terrain reference section 4) | Terrain tuning review: if a screenshot looks like Terralith, it is wrong |
| **Mars resource packs** | not found | none | none | We make our own textures (`pack/assets/mars/textures/`) |

**What we do now:** W1 terrain accepted by the owner (craters, dunes with one fixed wind, buttes, ripples); real-image palette in the style guide section 2; pressure keyframes for sky and fog (`sky.json`, slice 6).
**Gap:** no on-screen Mars reference board; colours are my estimates, no rover pixel samples (planetary-keyframes section 6); mid and late stages (30 and 75 kPa) have never been looked at as a whole landscape.
**Recommended next steal (small, 2 to 3 h):** build a **reference board** in `docs/art/references/` as a text list of 12 search terms with the keyframe stage each one checks (no images downloaded), then do one SME colour-sample pass of a white-balanced Mastcam frame to confirm the three ground colours. Second (medium, 1 day): a "Planet Crafter test": one screenshot trio at 0.6, 5 and 30 kPa from the same spot, to judge whether a player could **name the stage with no UI**.

## 2. Machines and block design language

| Reference | Mark | What we copy | What we avoid | Where it applies |
|---|---|---|---|---|
| **Mekanism** (MIT, NeoForge 1.21.1 verified) | listing verified; look from memory | **Colour-coded configurable sides** and clear input/output faces; tiered machines that look more advanced at each tier; a consistent frame-plus-window pattern | Its steel-grey-with-neon style; sci-fi chrome. Our machines copy real NASA hardware first | Machine face rules in `docs/art/invented-machines-look.md`: put input and output on **different, shape-coded faces** |
| **Create** (Create licence, 6.0.10 verified) | listing verified; look from memory | **Moving parts that show state**: a spinning cog tells you the machine runs. Tile-able, warm, hand-made look with strong silhouettes | Brass-and-wood steampunk language; the shaft-and-belt system | Animation frames: the emitter plume (2 frames), oven glow; one moving part per machine as its "running" cue |
| **Immersive Engineering** (custom licence; build not checked) | memory | Industrial scale: multiblocks that read as buildings, hazard stripes, caution colours, visible pipes | Its copper-and-steel-tower look | 2 x 2 and 3 x 3 machines (processor, emitter, beacon); the hazard stripe already planned on the emitter |
| **Thermal** (ARR; 1.21.1 not checked) | memory | Each machine type has one accent colour on a neutral body so you can read function at a glance | Its bright candy accent colours | Our rule is already similar: alloy body, orange only on "touch this" parts |
| **Satisfactory** (game) | memory | Clean industrial: bold primary-colour accent on grey, rounded corners, big readable status lamps, chunky pipes | Its orange-on-black corporate look and heavy glow | Status lamps (style guide 3.1); lamp-sized as the only glow |
| **Factorio** (game) | memory | **Legibility at a glance**: each machine silhouette is distinct from far away; animation only when running; wear and grime that never hides function | Dense pipe spaghetti look | Silhouette test (32 x 32 black), dust stages that never hide the lamp or grille |
| **Faithful 32x / Stay True / John Smith Legacy / Sphax** (Faithful: verified listing; other base packs not found) | memory | Faithful and Stay True: stay vanilla-faithful so Mars blocks sit well beside vanilla blocks; John Smith Legacy: cleaner, more readable shapes at 16 px; Sphax: strong outlines and a lit edge | Their palettes (all are Earth-bright); their licences forbid copying art | 16 x 16 ramp discipline: dark edge, mid, light edge per face, consistent top-left light |

**What we do now:** the invented-machines look brief with 11 machines specified (`docs/art/invented-machines-look.md`), made-up band and plate built (`docs/art/made-up-band-spec.md`), water reclaimer shipped; real NASA hardware as the base look.
**Gap:** only a few machines are modelled; **no animation spec per machine** (what moves when running); no "same-hardware family" test of all machines in one screenshot.
**Recommended next steal (small, 2 to 3 h):** a **running-state table**: for each of the 11 machines, one moving or changing part and its frame count (Create's idea), written into the look brief. Then (medium, 0.5 day) one **lineup screenshot** of every shipped machine side by side at 0 and 3 dust stages, judged by Factorio's rule: can you tell function from 20 blocks away?

## 3. Atmosphere, light and weather

Shader packs were reviewed in `docs/tech/optional-mods-and-shaders.md`; this table only says what **look** to learn from them, and whether we can get it without a shader.

| Reference | Mark | What we copy | What we avoid | Where it applies |
|---|---|---|---|---|
| **Complementary Reimagined** (verified listing) | look from memory | Soft, hazy distance fog that blends hills into the sky; slightly desaturated sun colour; warm-cool contrast between lit and shaded faces | Earth features that clash (blue sky, rain, aurora) | AT-2 far-haze retune in `atmosphere-plan.md` section 3 gets the distance fade without a shader |
| **BSL / Photon** (ARR / custom; verified listings) | look from memory | Photon: physically-based sky colour and aerial perspective as a **target** to match by eye; BSL: soft volumetric light | Their sky logic assumes Earth; ARR means we cannot bundle | Used by the owner as a comparison when judging our own fog colours |
| **Weather Storms & Tornadoes**, **Dynamic Surroundings Rebirth** (ARR; MIT, listings verified) | listing verified; look from memory | Storms as a **ramp**: a slow build, particles that grow in density, a sound bed that thickens | Tornadoes, violent wind, flying debris (our audit says no violent wind in thin air) | AT-1 storm ramp (60 s in, 90 s out) and AT-3 dust motes |
| **Dust storms in games** (Planet Crafter, Surviving Mars; exact storm looks not recalled in detail) | memory | Planet Crafter / Surviving Mars: a clear screen-space cue plus a number on a meter; the **warning comes before the storm** | Hiding the player in total darkness; sudden flashes | Pre-storm haze (atmosphere-plan 5.2) and the "storm warning" (S3-1) |
| **Dune (2021) and Blade Runner 2049** (films) | memory | Dune: monochrome ochre and wide empty space, small human figures against huge scale. Blade Runner 2049 (Las Vegas orange sequence): one saturated haze colour that turns the whole image, with the sun as a soft smear | Over-grading; their orange is far stronger than real Mars light | The storm tint `#9A6A48` at 85 percent; a dust-storm screenshot should look like that one-colour wash |
| **Immersive Weathering** (LGPL; verified listing) | look from memory | Surfaces that change over time: moss, rust, cracks as progress cues | Its Earth-like weathering | The gradual dust weathering rule (dust stages) |

**What we do now:** keyframed sky, fog and sun size by pressure (slice 6); storm and haze fully specified in `atmosphere-plan.md` but **not yet built**; clouds vanilla until E-10; sound beds not made.
**Gap:** the world still has no motion: no dust, no wind, no storm look (atmosphere-plan section 0); the far-ground band is darker than the sky.
**Recommended next steal (medium, 1 to 2 days):** build **AT-2 + AT-1 + AT-3** as written in `atmosphere-plan.md` (fog retune, storm tint and fog close, dust motes). Borrow from Blade Runner 2049 the rule "**one colour, applied to everything, with the sun softened**" for the storm check: if the storm screenshot has more than one dominant hue, it is wrong.

## 4. User interface and HUD

| Reference | Mark | What we copy | What we avoid | Where it applies |
|---|---|---|---|---|
| **Mekanism GUIs** (MIT; verified listing) | look from memory | Machine screens with a **progress bar, a status line and an energy bar** in fixed places; side-configuration tabs | Dense tab clutter | Machine screens (recipes ready, power state) and Monitor power row |
| **Create Ponder** (Create licence; Ponder frozen listing MIT) | look from memory | A short **animated explanation tied to the real block** in the world, step by step with a caption | Its full 3D scene editor (large work) | Journal "why" pages: maybe a 3 step highlight over the real machine, later |
| **Satisfactory and Oxygen Not Included info panels** (games) | memory | Satisfactory: one panel per object with name, rate in and out, clear icons. ONI: overlays that colour the world by one quantity (temperature, gas) | ONI's very dense screens | Monitor rows (`docs/design/ui-specs.md`); an "overlay mode" idea for later: tint the world by one Index row |
| **Subnautica HUD** (game) | memory | Minimal HUD that stays out of the way; the world and a small gauge tell the state; text shown as brief fade-in toasts | Hidden-until-needed gauges that make learners miss warnings | HUD strip (`monitor-mockups/C-hud-strip.png`) |
| **Outer Wilds ship log** (game) | memory | A **map of discoveries**: cards you fill in by exploring, linked by arrows; unknown cards shown as dim outlines | Fully free-form puzzles | Journal structure (fits "the planet is the quest log") |
| **Real NASA telemetry screens** (search: `Mars 2020 mission control Jet Propulsion Laboratory`, `JPL Mars Science Laboratory operations room`) | search terms only | Dark background, thin lines, **many small values each with a label and unit**, a clock always in view, one big shared screen; no decoration | Fake green-on-black hacker look; invented jargon | The Planetary Monitor: dark slate `#1E2328`, mission clock (sol), every value with a unit and a trend |

**What makes the Monitor feel like mission control (my view, one perspective):** (1) a **clock** (sol and hold timer) always visible; (2) **every number has a unit and a trend arrow**; (3) a shared **shift log** line at the bottom (BELL); (4) calm, flat, square panels, no gloss; (5) **alarm is rare** so it means something.

**What we do now:** Monitor with pressure plot, battery bar, power row, goal bar and "~N sols" estimate; style-guide section 11 gauge with milestones; three mockups in `docs/art/monitor-mockups/`.
**Gap:** no sol clock on the screen, no unit-and-trend rule written as a rule, no "overlay by one value" view, no Ponder-style explanation.
**Recommended next steal (small, 2 to 4 h):** write a **"mission control rules" box** into `docs/design/ui-specs.md` handoff (sol clock, unit on every number, trend arrow, one alarm colour) so the TL builds to it. Then (medium, 1 to 2 days, later): an ONI-style **overlay toggle** on the Monitor that tints the minimap by temperature or frost.

## 5. Sound and music

Factual base: **Perseverance microphone recordings**. NASA released the first sounds recorded on Mars in 2021 (wind, the rover's wheels and laser). From memory: Mars air is mostly CO2 and very thin, so high pitches carry badly and sound is quieter and lower than on Earth, and sound travels at two speeds there. **Not verified by me this session;** the SME has the sourced version (`docs/audio/audio-direction.md` section 3). Search term: `NASA Perseverance microphone Mars sounds`.

| Reference | Mark | What we copy | What we avoid | Where it applies |
|---|---|---|---|---|
| **Perseverance microphone recordings** | search term only | Low rumble of wind, a very quiet world, **contact sounds** (wheel on gravel, laser zaps) louder than air sound | Using actual recordings in the pack before licence is checked (NASA guidelines unchecked) | Wind beds AU-A01 to A03, footsteps as contact sound (atmosphere-plan 7) |
| **Outer Wilds** (game) | memory | Sparse acoustic instruments, **space as silence**, music that arrives at moments of discovery | Banjo and warm folk tone (we are lonelier) | The ending piece only; no music in Phase 1 (audio direction 12) |
| **Subnautica** (game) | memory | Ambient **layers that fade in with depth/danger**, creature calls heard from far, quiet most of the time | Constant tension drone | Thin-to-full air ambience by pressure; thawling calls from far away |
| **The Martian score** (film) | memory | Restraint: sparse electronics and soft strings, a sense of space | Disco and pop tracks used for humour | Mood reference for the ending piece |
| **Hans Zimmer-style restraint** (film scoring, Interstellar-like organ and drones) | memory | One long sustained tone that slowly changes, silence between. Do not copy the swell cliches | Big brass swells, constant music | Helmet-off moment (audio direction 4): near silence, then breath |
| **AmbientSounds** (LGPL-3.0; 6.3.11 verified) | listing verified | A **rule engine** that plays sound by place and condition, with soft layering; useful as a design pattern | Bundling it: it cannot read our Index (tech doc) | Pattern for our own `keyframes/ambient.json` |
| **Sound Physics Remastered** (GPL-3.0; NeoForge build is **beta**, verified) | listing verified | Reverb and muffling by space: a sealed dome sounds different from open plain | GPL cannot be bundled; beta | Dome interior vs outside (audio direction 2 listening model) |

**What silence-heavy, thin-air audio could do (my proposal):** (a) the quiet itself carries the lesson, so sound **fades in** as pressure rises (outdoor beds at 0.6 kPa barely above silence, fuller at 30 and 75 kPa); (b) contact sound is loud, air sound is quiet, so a player learns by hearing that the air is thin; (c) the helmet-off moment is the **loudest air sound** the player has ever heard in the game; (d) no music in Phase 1; (e) every sound has a subtitle, never sound over voice.

**What we do now:** full direction (`docs/audio/audio-direction.md`) and sound list (`docs/audio/sound-list.md`), pressure-band table, helmet-off design; **`pack/assets/mars/sounds/` is empty**; no sound is in the game.
**Gap:** zero audio shipped.
**Recommended next steal (small to large):** (small, 2 to 3 h) wire **footsteps and block sound types** with vanilla sounds (AU-A15, no new files) so the first sound is contact sound; (large, not estimated) make the three wind beds.

## 6. Icons, fonts, journal and diegetic documents

| Reference | Mark | What we copy | What we avoid | Where it applies |
|---|---|---|---|---|
| **Return of the Obra Dinn** (game) | memory | Documents as **objects with a hand-made look**: the logbook with a clear page layout, sketches, a restricted palette. The player **fills in** the book | The 1-bit shader (we keep colour); murder-mystery tone | The journal as a field log with entries the player completes (`docs/design/story-bible.md` media look) |
| **NASA Graphic Standards Manual (1975, "worm" logo)** (document) | memory | Rules-first design: a single sans typeface, strong grid, symbols that work at tiny sizes; **icon family with one stroke weight** | Using NASA logos (restricted) | Icon set U-series: one stroke weight, one grid, pictogram plus label |
| **Outer Wilds ship log** (game) | memory | Entries shown as small **cards with a picture and a short caption**; links show what is unfinished | Over-large map | Journal cards and gate cards |
| **Papers, Please / Disco Elysium-style note UI** (games) | memory | A plain paper-like form with stamps; text that can be re-read | Heavy grunge | BELL log stamps ("Sol 41") already specced in style guide 7.1 |
| **Real mission documents** (search: `NASA technical report cover page`, `JPL engineering notebook`) | search term only | Flat layout: number, title, date, sparse diagrams | Boilerplate density | Journal page template, "Fiction" and "Real" badges |

**What we do now:** style guide 7 gives BELL's log look (slate panel, severity icon + word, sol stamp); font stack chosen in 4.8 (Cairn Mono for numbers); icon IDs (U-10 Fiction badge).
**Gap:** no journal page template drawn; icon set not yet drawn as a family; no paper/field-log "personal" document look for crew props.
**Recommended next steal (small, 3 to 4 h):** one **journal page template mockup** (header, sol stamp, a card, a "Real / Fiction" badge) using the NASA-manual rule: one typeface, one stroke weight for icons.

## 7. Fauna and later-life visuals

| Reference | Mark | What we copy | What we avoid | Where it applies |
|---|---|---|---|---|
| **Subnautica creature design language** (game) | memory | Creatures read as **animals first**: silhouettes distinct by function (grazer, predator, shy), bioluminescent accents only on a few species, behaviour that tells you intent | Horror jump-scares; predatory teeth | Thawlings (style guide 6): domed, calm eyes, frills fan toward heat |
| **Annihilation** (film) | memory | The idea of **alien life that is beautiful and unsettling at the same time**; colour refraction on surfaces; things growing in slow, geometric patterns | Body horror | Later-life biocrust and lichen look; shimmer on frost plants |
| **Real extremophiles and Earth analogues** (search: `Antarctic dry valley cryptoendolith`, `tardigrade scanning electron micrograph`, `Svalbard biocrust`) | search terms only | Real small life forms as the root of our fauna shapes | Making thawlings look like tardigrades one to one | Thawling body plan (pill bug and seal pup in style guide 6.2) |
| **Minecraft mobs** (vanilla; mods with new mobs) | memory | One distinct silhouette per species; two-frame idle | Existing mob shapes (silverfish, endermite avoided in style guide 6.1) | Silhouette test of thawlings against vanilla mobs |

**What we do now:** thawling design goals, body plan and life stages (style guide section 6); no models or textures yet; later-life biome palette in keyframes.
**Gap:** no concept sheet showing thawlings against vanilla mobs and against the Mars ground.
**Recommended next steal (medium, 4 to 6 h):** one **silhouette sheet** of the five thawling life stages next to a zombie, a silverfish and a cow, on dust-plain and ice backgrounds, to prove none reads as an existing mob.

## 8. Character and story presentation

| Reference | Mark | What we copy | What we avoid | Where it applies |
|---|---|---|---|---|
| **Outer Wilds** (game) | memory | Story told through **found objects and places**, not cutscenes; a quiet, curious tone; no one tells you what to do | Time-loop structure | Story bible's in-world media: logs and props (style guide 7 and 8) |
| **Firewatch** (game) | memory | A **voice over a radio** with a strong personality while you are alone in a beautiful place; the relationship carried by short lines; warm sunset palettes | Romance plot; a limited 2 colour sunset | BELL and crew messages (BELL is a careful instrument, never a personality, style guide 7.1; crew voice can carry warmth) |
| **Oxenfree** (game) | memory | **Overlapping short lines** in a text-message style, with player choices that do not stop play; messages appear in the world, not in a pause menu | Teen drama; the glitch aesthetic | HUD toast and BELL text messages: max 3 lines, 6 s, reopenable (style guide 7.1) |
| **Real mission blogs and radio logs** (search: `Apollo 11 air-to-ground transcript`, `Curiosity mission blog sol`) | search term only | Plain, calm, procedural voices, with humour only in small lines | Fake heroic speeches | Crew message tone, BELL phrasing (LGD owns text) |

**What we do now:** BELL log look, crew props and dome, helmet and helmet-off visuals, beacon ending (style guide 7 to 10).
**Gap:** no portrait or silhouette style for crew (no faces by design?) and no mockup of a crew message over the world.
**Recommended next steal (small, 2 to 3 h):** one **message-over-world mockup** in the Oxenfree manner (a 3 line toast with a crew-name chip and sol stamp) added to the monitor-mockups folder.

## 9. Top steals, ranked (my view, one perspective)

| Rank | Steal | From | Size | Effort (unverified) | Why first |
|---|---|---|---|---|---|
| 1 | Dust motes, storm tint and far haze, in `atmosphere-plan.md` | Complementary look, Blade Runner 2049 one-colour wash | medium | 1 to 2 days | The world is static; biggest visible gain, no shader |
| 2 | Footsteps and block sound types by surface | Perseverance contact sound, Subnautica restraint | small | 2 to 3 h | First sound in the game; no new files |
| 3 | "Stage test": same view at 0.6 / 5 / 30 kPa, nameable with no UI | Planet Crafter | medium | 1 day | Checks the "every change is visible" pillar |
| 4 | Mission-control rules for the Monitor (sol clock, units, trend, rare alarm) | NASA telemetry screens, Satisfactory | small | 2 to 4 h | Cheap and makes the Monitor read as mission control |
| 5 | Running-state table for the machines | Create, Factorio | small | 2 to 3 h | Gives each machine a visible "is working" cue |
| 6 | Journal page template, one icon stroke weight | NASA Graphic Standards Manual, Obra Dinn | small | 3 to 4 h | Brings journal, icons and Fiction badge into one family |
| 7 | Silhouette sheet of thawlings vs vanilla mobs | Subnautica creature language | medium | 4 to 6 h | Proves the "animal, not monster" rule |
| 8 | Overlay toggle on the Monitor (tint by one value) | Oxygen Not Included | medium | 1 to 2 days | Teaches the planet as a system; later |

## 10. Assumptions and what would make this better defined

| Assumption | How to check |
|---|---|
| Game, film and shader descriptions are from memory and may be partly wrong | Owner or SME looks at each one and tells me "no" where my memory fails |
| Modrinth "verified" shows only licence and build status, **not** that the look suits us | Owner tries Complementary Reimagined and the one or two mods named above (optional-mods T-1) |
| NASA Perseverance sound facts above are from memory | SME to source them (`audio-direction.md` section 3 already has an SME-checked version) |
| Base packs Stay True, John Smith Legacy and Sphax are **not on Modrinth** by my search; they may be on other sites | Owner or TL |
| Effort estimates | Re-scale after the first item is built |

**Routing:** none of this is ill-structured except "how much to borrow from shader looks vs our own keyframes" (owner's taste, already routed in `optional-mods-and-shaders.md` decision 1). The rest is well-structured art work in my lane.
