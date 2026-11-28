# Inspiration survey: what we copy, what we avoid, for every aspect

**Owner:** Lead Game Designer · **Written:** 2026-11-28 · **Status:** research and proposal; nothing here is built or accepted · **Asked by the owner:** "what are some mods we are taking inspiration from; we need to be taking a lot of inspiration for every aspect of this"
**Reads:** `AGENTS.md`, `DESIGN.md` (gates, Planetary Index, pillars), `docs/design/world-and-blocks.md` (0.2 make-it-work, 5a real reference), `docs/design/machine-priorities.md`, `docs/design/phase3-hydrosphere-and-drift.md`, `docs/tech/optional-mods-and-shaders.md` (mods already reviewed), `docs/design/story-bible.md`, `docs/design/journal-and-guidance-minimum.md`.

## How to read this (marks, honest limits)

| Mark | Meaning |
|---|---|
| **verified (listing)** | I read the Modrinth API record on 2026-11-28 (title, description, loaders, 1.21.1 tag, last update). **This only proves the mod exists and what its author says it is.** I did **not** play it or read its code. Any feature I list for it is from memory |
| **memory** | From my own knowledge of the game or mod; **not verified**. I list only features I am fairly sure of; where I am unsure I say "unsure" |
| No mark possible | Boardgames and non-Minecraft games have no source I could read here, so all are **memory** |

Modrinth listings read (small JSON, untrusted, in my scratchpad only; nothing downloaded or run): Create (NeoForge 1.21.1, updated 2026-04-21), Mekanism (1.21.1, 2026-04-10), Immersive Engineering (1.21.1, 2025-07-05), Thermal Expansion (listed; 1.21.1 build **not checked**), Farmer's Delight (1.21.1, 2026-08-29), Mystical Agriculture (1.21.1, 2026-10-22), Tough As Nails (1.21.1, 2026-11-25), Ad Astra (1.21.1, 2026-09-13; already **out** by decision), Galacticraft-Legacy (Forge only, last update 2023-12; **no NeoForge or 1.21.1 build**), Advanced Rocketry (only forks: "Reworked", "3", "TFRU"; no 1.21.1 tag checked), Hardcore Survival (listed; version **not checked**), Botania (Modrinth record lists fabric/forge/quilt and **no 1.21.1 tag**; so not a NeoForge 1.21.1 option as listed), Create Ponder [Frozen] (a mod that splits Ponder out), Cabricality (a pack "inspired by Create: Above and Beyond"). **Not found on Modrinth by my queries:** Create: Above and Beyond itself, Vault Hunters 3 (pack lives elsewhere), FTB Quests, Nomifactory (only add-ons found). Those are **memory only**.

**Rules respected.** Custom Mars dimension; gates lock in; physical thresholds never scale; vanilla crafting stays; make-it-work rule (0.2); invented machines carry a real reference and the label (5a); owner is the only player (no classroom focus). Our five pillars: the planet is the quest log; every change is visible; pressure, not grind; compact before wide; your progress provokes the planet.

**Learning goals** are cited by ID (A1-D2, `LEARNING.md`): A = Mars and planetary science (A1 atmosphere and pressure, A2 water and boiling, A3 temperature and energy, A5 carbon and oxygen), B = systems thinking (B1 cause and effect, B2 feedback and delay, B3 forecasting, B4 trade-offs), C = engineering and design (C1 failure and diagnosis, C2 maintenance and redundancy), D = reflection and ethics (D2 consequences). Meanings are as used in the existing design docs; I did not re-read `LEARNING.md` for exact wording, so the Instructional Designer should confirm any mapping I mark "proposed".

---

## 1. Progression and gating

| Reference | Source mark | What it does (short) | Copy | Avoid | Supports |
|---|---|---|---|---|---|
| **Create: Above and Beyond** and its descendants (Cabricality, Create: CABIN crafts) | Pack itself: memory. Cabricality and CABIN listings: verified (listing) | A progression pack where recipes are rewritten so each new tool needs the previous machine's output; there are quests in the original but the recipe chain itself is the gate | Gate by **recipe chain, not by checklist**: "you cannot build X until your last machine makes Y". We already gate by the world; add recipe inputs that only appear after a gate (e.g. heater coil needs coal from Phase 2 output) | Rewriting vanilla recipes into a puzzle (our rule: vanilla crafting stays) | Pillar 1; C1 |
| **GregTech / Nomifactory** (voltage tiers) | memory (Nomifactory not on Modrinth by my search) | Hard tiers named by power level; each tier needs a new machine class and bigger power | A small number of **named tiers tied to a physical quantity** (our tiers are pressure and temperature, not voltage). The sense that power is a budget you size up | Tier count (10+), circuit-board recipe pyramids, hidden multi-step intermediates | A3, B4 |
| **Factorio** tech tree | memory | Research unlocks recipes; each science pack colour costs more throughput | The clear rule "a higher tier asks more throughput", shown on one screen. A visible "what is locked and what it needs" list | Research as a separate menu detached from the world (our gate is the world state) | Pillar 1; B1 |
| **Kerbal Space Program** tech tree and contracts | memory | Science points unlock parts; contracts give goals and money | Goals that name a **measurable physical target** ("reach 5 kPa") rather than a task | Currency and contracts (we have no shop; pillar 3) | A1, B3 |
| **Vault Hunters** (and FTB packs generally) | memory (FTB Quests and Vault Hunters not found by my Modrinth search) | Level-and-skill-tree RPG progression and quest books | Nothing for gating. One thing for feel: a **big visible bar** that makes a long goal feel close | XP trees, quest books, power-by-grind (pillar 3, "no linear NPC questlines") | none (avoid) |

**What we do now.** Seven gates as world conditions (`DESIGN.md` section 2, `pack/data/mars/gates/`); gates lock in; the Monitor shows Next goal, a bar and a "~N sols" estimate (slice 7); machine unlocks are `unlocks` lists in gate JSON (`machine-priorities.md` section 3).
**Gap.** Most unlocks are only a list; few **recipe inputs** change at a gate, so players meet gates as a number, not as "now I can finally make this". `machine-priorities.md` R1 (no copper or redstone) leaves recipes unwritten.
**Recommended next steal (small):** from Create: Above and Beyond / Factorio: at each gate give **one new input item the player has never seen** (for example heater coil at Phase 2 begins, iron plus coal) and make the next machine's recipe need it. One recipe edit per gate, no new system. Pillars 1 and 3; supports C2.

---

## 2. Terraforming and planet-state games

| Reference | Source mark | What it does (short) | Copy | Avoid | Supports |
|---|---|---|---|---|---|
| **Per Aspera** | memory | Real-time Mars terraforming with an AI narrator; global parameters (temperature, pressure, oxygen, water) with research and building; you see charts | **Parameter panel with a graph per quantity**, and the narrator voice reacting to parameters. We have the Monitor plot (option A) | A full tech-research UI; its map-scale abstraction (we are first person in blocks) | A1, A3, B2 |
| **Surviving Mars** | memory | Colony building under a dome with later terraforming parameters (atmosphere, temperature, water, vegetation) and a grid of dome resources | Terraforming as **several visible parameters that each need different buildings**, each with a milestone landmark | Colonist-happiness micromanagement, random disasters without cause | B4 |
| **Terraforming Mars** (board game) | memory | Three global parameters (temperature, oxygen, ocean tiles) with fixed end values; every player action raises one; end when all are met | **Equilibrium is a set of numbers all met together**, and each parameter has a single, countable step. Matches our Equilibrium band | Competitive scoring and card luck | A1, B1 |
| **Spore** (cell to space) | memory | Stage games change the world you act in; abilities follow from your choices | The feeling that **one choice unlocks a different stage of play**. Our phases do this; keep phase starts distinct (new machines, new threats) | Free-form creature editor; spaceflight stage | pillar 5 |
| **Civilization** (climate, global warming) | memory | Pollution accumulates globally and raises sea level | Global **side effect from your own industry** the player did not choose (our ground collapse, perchlorate in meltwater) | Slow end-game flooding that only punishes late | D2 |
| **Dwarf Fortress** (cascades) | memory | Systems interact in unplanned ways ("fun" losses) | **Cascades that are traceable to a cause**; losing the colony taught by a causal chain the player can read after | Opaque cascades; permadeath loss of the world | B1, B2, C1 |
| **Minecraft: Advanced Rocketry** (terraforming) | Listing of forks verified; features memory | Planet atmosphere pressure/temperature fields per planet, a terraforming block set that changes biomes outward | The idea that **a terraform effect spreads from the block outward**, visibly | Rocket, space-station and warp-travel tech (out of scope) | pillar 2 |
| **Galacticraft** | Listing verified (Legacy, Forge only, last update 2023-12, no NeoForge) | Planets need oxygen sealing and oxygen tanks; gravity and temperature vary | Reminder that **a breathable pocket you build is the classic loop**; our dome seal is the same idea | Rocket pad progression; being Forge-only, it is not a dependency | A1 |

**What we do now.** Six-parameter Planetary Index (`DESIGN.md` section 1); simulation core (pure Java); sky and fog follow pressure (slice 6, `pack/assets/mars/keyframes/sky.json`); ice-to-water rule; carbon rule A; Phase 3 drift in `phase3-hydrosphere-and-drift.md`.
**Gap.** Only the sky shows the Index in the world; hydrosphere, biomass and temperature are numbers on the Monitor until the biomes exist. No **cascade chain** a player can read backwards after something goes wrong.
**Recommended next steal (medium):** from Per Aspera / Terraforming Mars: a **"planet report" page after each gate** (one screen: the six parameters then and now, plus the one side effect your work caused). It is the phase debrief the design already wants, built from data we already store (the pressure history exists). Supports B1, B2, D2.

---

## 3. Life support and survival loops

| Reference | Source mark | What it does (short) | Copy | Avoid | Supports |
|---|---|---|---|---|---|
| **Oxygen Not Included** | memory | Colony of dupes; oxygen, heat, pressure, power and water all interact; **overlays** show gas, heat and power per tile | **Overlay modes** that colour the world by one quantity; the "pipes of gas have mass" feel; failure that comes from an unmanaged side effect | Colony micromanagement; dupe stress | A1, A3, B1, C1 |
| **Subnautica** | memory | Oxygen is a timer you can see; depth makes it a risk-reward decision; tools extend the timer | **A visible, short, personal timer** that makes each trip a decision; tank upgrades as the progression of range | Fear-driven jump scares; hunger and thirst micromanagement | B4, pillar 3 |
| **The Long Dark** | memory | Cold and calories as constant slow pressure; clothing layers; a fire is a place of safety | **Slow pressure plus a safe place**; wind and cold give weather a body | Permanent scarcity that blocks learning | A3, pillar 3 |
| **Tough As Nails** | Listing verified (1.21.1 NeoForge, updated 2026-11-25; "body temperature and thirst") | Body temperature and thirst bars affected by biome, armour and weather | A **second status gauge for temperature** with clear icons (we use shape, not colour only) | Thirst as a chore; our make-it-work rule does not need it. Also overlaps vanilla hunger | A3 |
| **Hardcore Survival** | Listing verified (survival-oriented; version not checked) | Harsher vanilla survival tweaks | Nothing concrete; I did not read it | Blanket difficulty. Our difficulty is **information, not punishment** (`DESIGN.md` "Difficulty as information") | none |
| **Mekanism / Galacticraft oxygen systems** | Mekanism listing verified; oxygen features memory | Oxygen as a gas produced by electrolysis, stored in tanks, piped; Galacticraft seals rooms | A **sealed room as a unit**: seal, pump, gauge, alarm when it leaks. Real electrolysis is already our fallback O2 generator | Mekanism's configuration depth (tabs of side-configs) | A1, C1, C2 |
| **Ad Astra** | Listing verified; **out** by decision | Planets, space suit, oxygen distributor | Nothing built; it is out. Only note: its oxygen use per tick is a visible bar | Dependency | none |

**What we do now.** Phase 1 is oxygen, water, power; the dome; Dome stable as a hold; dust and radiation hazards; failure reports with the 5.1 format; `machine-priorities.md` rows 1 (water reclaimer) and oxygen maker (spine). Real life support is still a stand-in (`machine-priorities.md` section 3).
**Gap.** No **sealed-room check** a player can see; no personal timer or suit gauge; no overlay.
**Recommended next steal (small to medium):** from Subnautica and Tough As Nails: a **suit HUD with two gauges (oxygen, warmth) using shapes** that slow down in the dome and drain outside. Small if only HUD; supports A1, A3, pillar 3 ("pressure"). A later medium step is an ONI-style **Monitor overlay** (hold a key to colour nearby blocks by heat or pressure).

---

## 4. Machines, power and factory feel

| Reference | Source mark | What it does (short) | Copy | Avoid | Supports |
|---|---|---|---|---|---|
| **Create** | Listing verified (NeoForge 1.21.1, 28.5M downloads, updated 2026-04-21); features memory | Mechanical, visible, moving contraptions; rotation speed and stress shown with goggles; Ponder explains each block | **Machines whose operation is visible**; a **readout item** (goggles) that shows numbers on a block; Ponder (see 6) | Its stress-unit economy as a second power system; very large contraption building as a requirement (pillar 4) | A3, C1 |
| **Mekanism** | Listing verified (1.21.1, updated 2026-04-10) | Multi-tier machines, gases, chemical processing chains, upgrade modules | **Chemistry as visible gas flow** (we only need a few); an install-upgrade idea for machine wear parts | Its sheer machine count; config tabs; upgrade pyramid | C2 |
| **Immersive Engineering** | Listing verified (1.21.1, updated 2025-07-05) | Multiblock structures built in the world (e.g. a refinery you assemble block by block) | **Multiblock assembly as building**: the atmospheric processor as a 3x3 you build, not a single block. Matches the 3x3 solar array | Heavy recipe-per-multiblock lists | pillar 4, C1 |
| **Thermal series** | Listing verified (Thermal Expansion listed; 1.21.1 build not checked) | Machines with a clear augment system and a flux energy | The **furnace-like machine with a status lamp and a progress arrow** (our Oven, Outgasser) | Flux conduits as another network | A3 |
| **Factorio** | memory | Belts, inserters, production statistics, alerts on missing power or input | **Alerts** when a machine starves; per-machine production graph; ratio problems the player solves by looking | A belt-logistics requirement; train networks | B1, B4, C1 |
| **Satisfactory** | memory | 3D factory, clean power and demand graphs, hand-placed foundations | **Power graph with "capacity vs. demand" shown at the grid**; it is what our Monitor power row already does | Production-line scale | A3, B4 |

**What we do now.** Solar panel with dust stage, battery, Regolith Oven, Regolith Outgasser, Monitor with a Power row and battery bar (slices 3, 5, monitor option A); the processor, emitter, extractor, scrubber, soil unit and kits are **unbuilt** (`machine-priorities.md`).
**Gap.** Machines are single blocks with no **multiblock assembly**, no per-machine readout, and no starved-machine alert.
**Recommended next steal (medium):** from Immersive Engineering: build the **atmospheric processor as a multiblock** (for example 3x3 base, 2 tall, with intake, pump and stack parts the player places). It teaches the parts of a real process (see 5a reference), supports "compact before wide" (it fits in a small footprint), and the Artist already has a gold processor concept. A **small** companion steal from Create: a held "readout" item that prints one block's numbers (power in, output rate) in chat or on the HUD.

---

## 5. Farming, greenhouses and ecology

| Reference | Source mark | What it does (short) | Copy | Avoid | Supports |
|---|---|---|---|---|---|
| **Farmer's Delight** | Listing verified (1.21.1, 26M downloads, updated 2026-08-29: "cozy expansion to farming and cooking") | Rich soil, rice, tomatoes, cooking pots, picnic baskets | **Rich soil as a planted-bed upgrade** (our soil treatment unit and washed regolith); crops that need a prepared bed; a cooking step that feels good | Dozens of food items (we need a handful) | A5 (biomass), C1 (dead-tomato lesson) |
| **Botania** | Listing: no 1.21.1 tag, fabric/forge/quilt; features memory | Generating flowers that make mana; a world-in-a-pot feel | **Plants as the machine**: the player's garden **is** the power or output. Our flora already turns CO2 into O2 | Magic and mana; the mod is not a 1.21.1 NeoForge option as listed | A5, pillar 2 |
| **Mystical Agriculture** (anti-pattern) | Listing verified (1.21.1, updated 2026-10-22: "resource crops for more than 150+ vanilla and modded materials") | Grow a crop for every resource (copper crop, diamond crop) | **Nothing.** It trades mining for waiting on crops and breaks resource scarcity. Our worldgen and ice are the scarcity. We avoid "grow ore" crops | Resource crops. Recorded as the deliberate avoid | none (avoid) |
| **Terraria** | memory | Biomes spread corruption/purity; biome conditions decide spawns | **Biome as a counted condition** (so many blocks of X change the area). Our flora spread and biocrust do it | Boss-gated progression | pillar 2 |
| **Stardew Valley** | memory | Daily farming rhythm, seasons, a calm loop | A **calm daily loop** between pressure events: plant, water, wait a sol | Social and dating systems | pillar 3 (pressure, not grind) |
| **Eco** | memory | Shared server ecosystem; actions affect other players' resources; laws | **Shared planet whose health reflects player choices**, one world state for everyone (our one Planetary Index) | Heavy economics and politics | D2, B4 |
| **Planet Crafter** | memory | Terraform a planet with machines; atmosphere, heat and pressure values each raise a stage; world visuals change by stage (ice melts, then plants, rain) | The closest cousin: **machines that add to terraform meters and visibly change the world at thresholds**. Copy the **staged world change** (barren, then moss, then trees, then animals) | Its fixed per-machine "rate" displayed with no cause; our Monitor shows causes | A1, A5, pillar 2 |

**What we do now.** Planned only: Phase 1 greenhouse planter (L), Phase 3 seed kits, soil unit, biocrust first (`world-and-blocks.md` 3.3; `DESIGN.md` carbon rule A). Fauna are dormant, fictional and labelled.
**Gap.** No farm or flora exists in code. The **R1** recipe conflict (no copper, redstone) also blocks planter and grow-lamp recipes.
**Recommended next steal (medium):** from Planet Crafter and Farmer's Delight: **a fixed ladder of four ground states** the player can plant into (raw regolith, washed soil, rich soil, biocrust patch) with each step needing a visible action. This makes "biomass" a thing the player did, not a number. Pair with a **small** step: grow a first crop in the dome (Phase 1 tomato lesson, C1).

---

## 6. Exploration, scanning and discovery

| Reference | Source mark | What it does (short) | Copy | Avoid | Supports |
|---|---|---|---|---|---|
| **Subnautica** (scanner and PDA) | memory | Scan fragments to unlock blueprints; PDA entries; scan targets glow | **Scan to learn a thing exists**, then craft: the survey scanner (`machine-priorities.md` row 7). Our rule keeps thresholds unchanged; scanning only reveals where | Scan-fragment grind (collecting 3 of 5 pieces of a wreck) | A2, pillar 1 |
| **No Man's Sky** (scanner/discovery log) | memory | A visor scan marks resources and animals; the first to name a thing | **Hold-to-scan with a clear ring and an on-screen result.** The first scan of a type creates a log entry | Procedural everything; naming mechanics | A1 |
| **Create Ponder** | Create listing verified; a separate "Create Ponder [Frozen]" mod listed (19k downloads); how it works: memory | Hold a key over an item, a short animated 3D scene shows how it works | **In-world animated explanation of a machine**, short, wordless-first. Perfect for the 12-year-old who never used a modpack | Building a general animation engine. licence of Ponder not checked | C1, A3 |
| **Outer Wilds** (curiosity, ship log) | memory | No stats; knowledge is the only progress; rumor map shows linked clues and "unexplored" arrows | **Knowledge as progress**; entries show **question marks** until seen. Maps well to our journal gate entries | Time-loop death; sparse hints for a 12-year-old | pillar 1, B3 |
| **Hades-style journals** (the Codex) | memory | A codex that fills as you meet things and unlocks lore as you repeat | Journal pages that **unlock by meeting** the thing, with a short line per page | Repetitive boon-grind | pillar 1 |

**What we do now.** Journal entries are computed from latched gates (`journal-and-guidance-minimum.md` section 2: no per-player storage); the Monitor shows causes; the survey scanner is **unbuilt** and a cut candidate.
**Gap.** Nothing in the world is **discovered**; all knowledge arrives via gates. No "what is this machine?" help in-world.
**Recommended next steal (small to medium):** from Create Ponder: a **hold-to-explain** key on any Mars machine that opens a 10-second, mostly wordless, captioned panel (what it takes, what it makes, what it moves on the Index). Small first version: a static card using existing UI (not animated). Supports C1, A3 and the "12-year-old who never used a modpack" walkthrough.

---

## 7. Storytelling without quests; journal and logs

| Reference | Source mark | What it does (short) | Copy | Avoid | Supports |
|---|---|---|---|---|---|
| **Outer Wilds** | memory | Environmental text on walls; the ship log connects clues | **Traces left by earlier crew** (our story bible 4.3) with a **link map** in the journal | Hidden critical info | pillar 1 |
| **Return of the Obra Dinn** | memory | Investigate a scene; confirm identities and fates in sets of three | **Confirm to lock**: the player proposes, the game confirms only when right (our prediction prompts) | Pure deduction without hints | B3 |
| **Subnautica radio and PDA** | memory | Distress calls, audio logs from the dead crew; PDA voice reports hazards | **A narrator voice that reports world state** ("Pressure is rising"); logs found at places | Voiced lines (cost, and a text game for 12-year-olds should read fast) | pillar 2 |
| **Planet Crafter** | memory | Terraform meter unlocks short text and sound from your character; derelict wrecks hold logs | **Short log lines on stage change**; wrecks that hold logs | Repetitive voice quips | pillar 2 |
| **Oxygen Not Included** | memory | Dry, funny one-line descriptions on every item and message | **Item and message flavour in a consistent dry voice**; the Monitor lines are already short (34 characters) | Quantity of text | engagement |

**What we do now.** Story bible with the crew, traces and beats (`story-bible.md`); journal derived from gates; Keeper's Record, failure reports, debriefs; Monitor lines per `docs/design/messages/`.
**Gap.** Traces and logs exist as text plan but few appear in the world; no **location-triggered log** yet.
**Recommended next steal (small):** from Subnautica and Outer Wilds: **three physical log items** (a book on a lectern in the dome, a data tablet in a wreck, a sign at the crater) each unlocked in the journal when first read, with the Monitor showing "?" for unread ones. Data-only plus 3 blocks. Supports pillar 1; story rules (no NPC questlines).

---

## 8. Learning games and science in games

| Reference | Source mark | What it does (short) | Copy | Avoid | Supports |
|---|---|---|---|---|---|
| **Kerbal Space Program** | memory | Orbital mechanics learned by failing and trying again; the delta-v map | **Learn physics by building, failing and reading the cause.** Our failure report (Happened / Why / Fix / Next time) | Math-first onboarding | C1, B3 |
| **Eco** | memory | Systems teaching via shared consequences | One world, shared consequences; ecology as the thing you read | Law-writing UI | B4, D2 |
| **Foldit** | memory | Puzzle model of protein folding with live score | **A live score that moves while you tweak** (our Monitor values react at once) | Abstract puzzles detached from a world | B1 |
| **SimCity** | memory | City sim with overlays and advisors | **Advisor lines** that name a cause (we have cause lines) | Advisor pop-ups that nag | B1 |
| **Minecraft Education** | memory (the Education Edition; the owner's mod stack is not it) | Chemistry resource pack (element constructor, lab table), Code Builder, worlds with lessons | **A lab bench idea in Minecraft style** (the soil treatment unit as a bench); a short "ask the agent" help is **out** | Classroom features (no classroom focus) | A5 |
| **Universe Sandbox** | memory | Planetary physics sandbox; change a planet's parameters and watch | **Sandbox view of the same model** (our `/mars index` and a test world); a "what if" slider | Full N-body | A1, A3, B3 |

**What we do now.** Prediction prompts (forecasts F1-F10), failure reports, debriefs, the "real reference" rule for invented machines (`world-and-blocks.md` 5a), `docs/science/`.
**Gap.** Science explanation depends on text; no **what-if** place a player can try a number without risk.
**Recommended next steal (small):** from Universe Sandbox and Kerbal: a **creative-mode "what if" dial** (admin command already exists: `/mars index`) documented as a player option, with the rule that gates it latches are **not** earned that way (survival players never get it). Supports A1, B3. Documentation only.

---

## Top steals across aspects (ranked, with size)

| # | Steal | From | Size | Aspect | Goals |
|---|---|---|---|---|---|
| 1 | Hold-to-explain panel on every machine (static card first, animation later) | Create Ponder | small, then medium | 6 | C1, A3 |
| 2 | Staged world change per gate (visible ladder of ground states) | Planet Crafter, Farmer's Delight | medium | 5 | A5, pillar 2 |
| 3 | Atmospheric processor as an assembled multiblock | Immersive Engineering | medium | 4 | pillar 4, C1 |
| 4 | New unseen input item at each gate; next recipe needs it | Create: Above and Beyond, Factorio | small | 1 | pillar 1, C2 |
| 5 | Suit HUD: oxygen and warmth gauges using shapes | Subnautica, Tough As Nails | small | 3 | A1, A3 |
| 6 | Planet report page after each gate (before and after, one side effect) | Per Aspera, Terraforming Mars | medium | 2 | B1, B2, D2 |
| 7 | Three physical logs; "?" entries in the journal until read | Outer Wilds, Subnautica | small | 7 | pillar 1 |
| 8 | Starved-machine alerts on the Monitor | Factorio | small | 4 | B1, C1 |

## Assumptions and what would make this better

- I assume "verified (listing)" is acceptable as a source for existence only; features are memory. A deeper pass would need the Technical Lead to read each mod's source or the owner to play it for 15 minutes. That is the best next step for steals 1, 3 and 5 (Create, Immersive Engineering, Tough As Nails).
- I did **not** check licences. Any steal that means **bundling** a mod (Create Ponder, Immersive Engineering, Tough As Nails) needs the Technical Lead's licence check in `docs/tech/mod-licenses.md` style first. The default here is: copy the idea and build ours, as the existing mods verdicts do (`optional-mods-and-shaders.md` section 3).
- Ill-structured part: how much to copy against the "invented machines need a real reference" rule (5a). I read it as: a game reference is inspiration for **feel**; the real NASA/ESA hardware is the reference for **what the machine does**. The two are different jobs.
- A playtest by the owner (steals 1, 2, 5) would settle which feels best.
