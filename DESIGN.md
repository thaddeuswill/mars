# Mars Terraforming Modpack — Design

## Core Concept & Game Loop

A total-conversion Minecraft modpack built around terraforming a planet in stages. There are no linear NPC questlines. The world itself is the main progression gate and tells the story. Players start on a barren, hostile Mars. They move from desperate survival inside a closed bio-dome, to industrial geo-engineering, to a planet that sustains itself.

**Core loop:** survive → extract → process → emit into the atmosphere → the world changes → new threats and new resources → expand.

Each change to the planet should be visible and felt: sky color, weather, which blocks survive on the surface, and which mobs spawn.

**Time unit.** A **sol** is one in-game day: 20 real minutes of simulation time. Simulation time stops when the world is paused (the Technical Lead defines it in ARCHITECTURE.md). All hold times and delays are in sols.

**Sleep and the sky (confirmed, answers the Technical Lead).** On Mars, **sleeping never skips time**, and `/time set` has no effect there. The **sky follows simulation time**: sunrise and sunset line up with the sol, and a paused world freezes the sun. A bed sets the respawn point, and lying down is a **natural pause** (TEAM.md 8.6), so queued messages are shown then. Why: skipping a night would let one player fast-forward holds and storms for a whole group, and it would turn waiting into a button instead of play (pillar: pressure, not grind). Revisit if playtests show nights feel idle; the fix would be night work (repairs, frost, stars), not a skip.

**Numbers.** Rates, curves, and tuning values live in the balance sheet: [docs/design/balance-sheet.md](docs/design/balance-sheet.md). Story: [docs/design/story-bible.md](docs/design/story-bible.md).

---

## Progression Phases

### Phase 1: Survival Desperation (Early Game)
- **Setting:** Trapped inside a starting bio-dome on a sterile, toxic surface.
- **Mechanics:** Strict oxygen, water, and power management. Scarce resources push players to build tall and compact rather than wide. Once life support is steady (Dome stable), the player builds the first **Regolith Outgassers**: heaters that bake gas out of the soil and start thickening the air toward the Phase 2 gate.
- **Threats:** Dust storms, solar radiation spikes, mechanical wear on life-support systems, and perchlorate dust tracked in through the airlock.

### Phase 2: Industrial Geo-Engineering (Mid Game)
- **Setting:** Expanding out from the bio-dome with airlocks, pressurized transit tubes, and automated outposts.
- **Mechanics:** Atmospheric processors, super-greenhouse gas emitters, and deep-crust water extractors that thicken the atmosphere and raise surface temperatures.
- **Threats:** Ecosystem blowback. Warmth and water wake dormant underground fauna (fictional, clearly labeled), which later reach a larger **later life stage**. They are drawn to machines, not to the player. The real side effects of warming come too: ground collapse where buried ice melts under buildings, and perchlorate salts dissolving into meltwater.

### Phase 3: Biome Seeding & Equilibrium (Late Game)
- **Setting:** A world in transition. Liquid water pools, storms die down, and engineered flora takes root.
- **Mechanics:** Seeding complex biomes that sustain themselves, and balancing O2 against CO2 while holding pressure and temperature. Drift events keep the balance active.
- **End Goal:** Full planetary equilibrium. The player can take off the spacesuit helmet for good, survive on the surface unaided, and build the beacon that signals the first colonization ships.

---

## Implementation Plan

Settled rules are marked **(settled)**. Everything else here is the current design and is **fluid** or **flexible** (TEAM.md 8.1).

### 1. World State: the Planetary Index

One server-wide state object drives all progression (settled: one Index per world, or per planet when a server hosts several group planets). Machines write to it and world systems read from it.

| Parameter | Start | Equilibrium | Raised by | Effects as it rises |
|---|---|---|---|---|
| Pressure (kPa) | 0.6 | ≥ 75 | Regolith Outgassers (Phase 1), atmospheric processors (Phase 2+) | Fewer suit leaks, thicker sky fog, liquid water possible (with warmth) |
| Temperature (°C, global mean) | −60 | ≥ 5 | Super-greenhouse gas emitters, orbital mirrors, ice–albedo feedback | Ice can melt (with pressure), fewer cold-damage zones |
| Hydrosphere (% of surface covered by liquid water) | 0 | ≥ 30 | Deep-crust extractors, ice-comet impacts, melting ground ice | Surface water, rain, wakes fauna |
| O2 fraction (%) | 0.1 | 19–23 (fire-safety band) | Algae vats, seeded biomes | Fire possible; above 23 %, fire hazard |
| CO2 (partial pressure, kPa) | 0.57 | ≤ 0.5 | Raised by outgassers and decay; lowered by biomes and carbon scrubbers | Must fall while temperature holds (balancing act) |
| Biomass (biomass units) | 0 | ≥ 1,500 (draft) | Seeded flora spreading naturally | Unlocks self-sustaining biomes |

- **Derived values on the Monitor:** O2 partial pressure (pressure × O2 fraction), CO2 partial pressure, and the boiling point of water at the current pressure.
- **Greenhouse emitters emit engineered super-greenhouse gases, not CO2.** That is how temperature can hold while CO2 falls, and it matches real proposals (SME accuracy review, B4).
- **Filler gas.** The air at equilibrium is mostly a filler gas such as nitrogen. The game does not track it; a journal note says so (SME decision: note only).
- Persist the Index in world saved data so it survives restarts and is shared in multiplayer.
- Rates scale with machine count and tier, and values can **fall**: unmaintained machines, storms, polar frost, decay, and fauna damage all push them back. That keeps the mid game active rather than idle.
- **Gates lock in (settled).** Once a gate is reached it stays unlocked even if the values drop below its threshold. Falling values cost progress toward the *next* gate and bring back hazards (thicker storms, colder surface), but never take away unlocked recipes or phases. The one exception is the **helmet-off state**: it is a live condition, checked every step, so the air can become unbreathable again.
- **Equilibrium is a band to hold, not a number to hit.** Too much O2 means fire hazards and runaway flora; too much CO2 is toxic and keeps the air unbreathable.
- **Physical thresholds never scale.** No multiplier (classroom pacing, group size) ever changes a threshold such as the triple point, the 5 and 30 kPa gates, 0 °C, the breathable condition, or the equilibrium band. Multipliers change **rates** and **hold times** only. See Pacing.

### 2. Phase Gates (the world as quest log)

Gates are world-state conditions, not quest checkboxes. Recipes, structures, and spawns unlock when the planet reaches a condition.

| Gate | Condition | Unlocks |
|---|---|---|
| Dome stable | Life support runs 3 sols with no failures. *A machine-state condition, not an Index threshold.* | Airlocks, EVA suit tier 2, first outpost blueprint, **Regolith Outgasser** |
| Phase 2 begins | Pressure ≥ 5 kPa | Atmospheric processor, super-greenhouse emitter, transit tubes |
| Thaw | Temperature ≥ −20 °C | Ice extraction, **dormant fauna begin to stir**, ground-collapse risk near warm machines |
| Hydrosphere | Hydrosphere ≥ 10 % | Surface water, fauna **later life stage**, algae vats, meltwater treatment (perchlorate) |
| Phase 3 begins | Pressure ≥ 30 kPa and temperature ≥ 0 °C | Biome seed kits, soil treatment, storms weaken |
| Breathable | O2 partial pressure ≥ 16 kPa **and** pressure ≥ 75 kPa **and** CO2 partial pressure ≤ 0.5 kPa, held 7 sols | Helmet optional on the surface (live condition), beacon component recipes |
| Equilibrium | All parameters in band, held 14 sols | Colonization beacon assembly |

- **Monitor milestones (not gates):** the **Armstrong line** at 6.3 kPa ("water in your body would no longer boil; you still need a pressure suit") and **Everest pressure** at about 33.7 kPa. They unlock nothing; they give the climb visible landmarks (SME optional item, adopted).
- **Holds overlap.** Breathable sits inside the equilibrium band, so both holds run at once. A hold counter pauses when a value leaves the band and resets only if it stays out for more than 1 sol. Details and drift events: balance sheet §4.

**Ice-to-water rule (draft from SME review B1; SME to confirm the game thresholds).** Each ice block checks the **local** temperature (global mean plus day/night and nearby machine heat) and the global pressure.

| Pressure | Local temperature | What warmed ice does |
|---|---|---|
| < 0.61 kPa (below the triple point) | Any | Shrinks and turns straight to vapor. No water. |
| 0.61 – 5 kPa | > 0 °C | Melts into a water block that boils or evaporates away within seconds |
| ≥ 5 kPa | > 0 °C | Melts into water that stays while the local temperature stays above 0 °C |
| Any | ≤ 0 °C | Stays ice |

So Phase 1 players see ice vanish into vapor near a heater, early Phase 2 players see short-lived puddles, and water only pools for good once pressure *and* temperature are high enough (A2). The gate keeps the name **Thaw** because it is when ground ice near machines starts to melt and the fauna stir; its journal entry says that melting needs both warmth and pressure.

Progress is shown in the world itself: sky tint, fog density, the storm table, and block ticks (ice → vapor or water, regolith → soil near seeded flora). A diegetic **Planetary Monitor** block shows the numbers, trends, causes, and milestones.

### 3. Hazard Systems

- **Oxygen and pressure:** suit O2 drains outside sealed volumes. Dome breaches vent the atmosphere. Sealing works by checking enclosed volumes.
- **Dust storms:** a weather event that reduces visibility, cuts solar output, and wears exposed machines. Storms come less often as pressure and biomass rise. No damaging wind force (Mars's thin air can't do that).
- **Radiation spikes:** periodic solar events. Exposure on the surface builds a radiation effect, and shielding blocks and depth protect against it. Spikes weaken as the atmosphere thickens.
- **Mechanical wear:** life-support machines lose durability and need maintenance parts. This keeps the early game tense and gives scrap a use.
- **Polar frost (sink):** some of the added air freezes out at the poles while the planet is cold, so pressure leaks back a little each sol. It stops above −20 °C. A balancing loop the Monitor names as a cause (B2). **[SME to verify wording]**
- **Perchlorate (real).** Mars soil holds toxic perchlorate salts. *Phase 1:* dust tracked in through an airlock without a dust-off station builds a mild "perchlorate exposure" effect and clogs filters. *Phase 2 (Hydrosphere):* meltwater picks up dissolved perchlorate and must be treated before the dome can use it. *Phase 3:* soil must be treated before seed kits take. Hazard, not grind: one machine or block answers each stage.
- **Ground collapse (real effect).** After the Thaw, buried ground ice under or near warm machines can melt or sublimate, and the ground above sinks. A survey scan shows ice lenses; building on rock or on piles avoids it. Earth link: thawing permafrost damages buildings and roads today (D2).
- **Ecosystem blowback (fauna):** see "The fauna: decision" under Fantasy and Authenticity. Fauna pressure scales with active terraforming machines, so how hard you push terraforming sets the difficulty.

### 4. Candidate Mod Stack

Target **NeoForge, Minecraft 1.21.1** unless a key mod forces otherwise. Confirm versions before locking anything in (ROADMAP Stage 1).

| Role | Candidate(s) | Notes |
|---|---|---|
| Mars dimension | **Custom** (companion mod) | Full control of sky, fog, weather, and terrain response to the Planetary Index |
| O2, sealing, suits | Custom, or a standalone oxygen/suit mod if one fits | Must hook into the Planetary Index so ambient O2 and pressure come from world state |
| Learning-critical machines | **Custom, simple, in-house** (companion mod) | Every machine a player needs to learn the science or pass a gate is ours, with one interface pattern. See "In-house machines" below |
| Industry / logistics (optional) | Mekanism, Create | **Optional and late:** unlocked at Hydrosphere, never required for a gate, never used to teach. Hidden in recipe viewers until then; classroom preset hides them by default |
| KubeJS (+ ProbeJS for dev) | Recipes, recipe gating, third-party integration | Not the Index or gates. Gates and tuning values live in datapack JSON. |
| Worldgen | Datapack + TerraBlender, or custom | Regolith, ice caps, ground-ice lenses, buried fauna lairs |
| Journal | **Custom** (companion mod, learning module) | Gate entries, prediction prompts, message archive, Colony Record. FTB Quests rejected (user decision): a quest checklist works against "the planet is the quest log". |
| Custom content | Companion mod (core of the pack) | Dimension, Planetary Index storage, sky/fog rendering, storm weather, fauna, journal |

With a custom Mars world, the companion mod is not small: it owns the dimension, the Planetary Index, rendering, weather, fauna, and the journal. **Datapack JSON** holds content and tuning (gates, contributions, balance numbers), so balance changes don't need a Java rebuild. **KubeJS** holds recipes and recipe gating.

### 5. Milestones

The build order lives in [ROADMAP.md](ROADMAP.md) (Stages 2–7). This file does not keep a second plan. The design content each stage must contain:

- **Vertical slice (Phase 1):** spawn in the dome; O2/power/water loop; one dust storm; Dome stable; the **Regolith Outgasser** moving pressure toward 5 kPa (Phase 1 has no atmospheric processor); the first perchlorate dust-off; the minimal learning layer.
- **Phase 2 content:** atmospheric processors, emitters, transit tubes, outposts, Thaw, ice-to-water rule, ground collapse, first fauna, deterrents and quiet zones.
- **Phase 3 content:** biome seeding, O2/CO2 balancing, drift events, Breathable live check, ending cards.

## Two Goals: Instructional Value and Market Appeal

This is an ill-structured design problem: there's no single right design, and learning value and appeal often pull against each other. Neither side wins by default.

| | Instructional value | Market appeal |
|---|---|---|
| **Who judges it** | Teachers, learning outcomes data | Students, hobbyist players, teachers choosing what to adopt |
| **Success looks like** | Pre/post learning gain; goals met in the Colony Record (see LEARNING.md) | Students want to keep playing; teachers adopt it and come back; hobbyists download, rate, and finish it |
| **Draft measures** | Success criteria in LEARNING.md section 3 | Playtest fun rating; share of students who ask to keep playing; teacher setup under 30 minutes; completion rate; ratings and downloads on mod sites |

**Where they pull against each other**
| Tension | Risk if learning wins | Risk if appeal wins |
|---|---|---|
| Journal text and prompts | Feels like homework; players skip it | Science gets lost |
| Realistic physics | Slow, abstract, less exciting | Teaches misconceptions |
| Difficulty and failure | Frustration from "productive failure" | No real problem-solving |
| Classroom pacing | Rushed, less play | Doesn't fit a school unit |
| Fauna and combat | Toned down too far | Combat crowds out the science |

**How the team resolves them**
- The best solutions serve both: the mechanic *is* the lesson (design pillar 1, and section 4.4 of LEARNING.md: the win condition is the learning outcome).
- Every major decision is reviewed by several roles, with the trade-off stated for both goals.
- Disagreements are settled by playtest evidence where possible, not by whichever role argues hardest.
- Each roadmap stage checks both goals before moving on, using the Success Scorecard in [TEAM.md](TEAM.md) (section 8.5).

## Fantasy and Authenticity

Fantasy can pull players in, keep them playing, and help learning (better retention, transfer, confidence), but only when it's designed on purpose (Malone & Lepper; Asgari & Kaufman, 2004; Gunter et al., 2008). Fantasy details that are interesting but irrelevant can distract, set up wrong ideas (negative learning), and add extraneous cognitive load on top of content that's already demanding (Clark & Mayer, 2003; Clark et al., 2006). Instructional disciplines also prefer more realism, and want fantasy to act as a useful metaphor for the objectives (Coleman et al., 2010).

### Rules for every fantasy element

1. **Purpose:** it must be a metaphor for a learning goal, **or** have a stated motivational purpose, and never conflict with a goal.
2. **Complementary activity:** what the player *does* with it must exercise the thinking skill being taught (e.g. predicting, balancing, diagnosing), not something unrelated.
3. **Negative-learning check:** if a player could leave believing something false about real Mars or Earth, the game must correct it (journal "real vs. game" note) or the element must change.
4. **Cognitive load:** don't introduce new fantasy at the same moment as new science. Fantasy detail lives in optional lore; core science is never hidden behind it.
5. **Seductive-detail test:** if it's interesting but teaches nothing and serves no clear motivational purpose, cut it.
6. **Realistic by default:** real physics and real numbers wherever realism teaches. Stylize only where realism would teach nothing (e.g. centuries-long timescales).

### Fantasy and authenticity audit

| Element | Real or fantasy | Metaphor / purpose | Player activity matches skill? | Negative-learning risk | Status |
|---|---|---|---|---|---|
| Starting values (pressure, temperature, CO2) | Real | — | Reading the Monitor (C3) | Low | Keep (SME: accurate) |
| Dust storms: visibility, solar loss, dust wear | Real | — | Planning around power loss (C1) | Low. **Avoid** storms with damaging wind force: Mars's thin air can't do that, and it's a common misconception from films. | Keep, with that limit |
| Radiation spikes | Real | — | Shielding decisions (A4) | Low | Keep |
| Regolith Outgasser | Based on real physics (heating soil releases bound gas and water), size exaggerated | First lever on the planet | Seeing cause and effect on the Monitor (B1, C3) | Medium: "Mars has enough gas in its soil to thicken the air". Real Mars doesn't (Jakosky & Edwards 2018, in SME review). | Keep; journal note: "Real soil and ice don't hold enough gas for this. The game exaggerates it." |
| Atmospheric processors, super-greenhouse emitters, orbital mirrors, ice-comet impacts | Based on real proposals, simplified | Engineering interventions | Choosing and balancing interventions (B4) | Medium: implies these are proven technologies | Keep; journal labels them as proposed ideas; emitters say they make factory-made greenhouse gases, not CO2 |
| Polar frost loss | Real (seasonal CO2 frost), simplified | Balancing loop | Reading a sink on the Monitor (B2) | Low | Keep, pending SME wording |
| Perchlorate dust and meltwater | Real | — | Airlock procedure, water treatment (C1, C2) | Low | Keep |
| Ground collapse from melting ground ice | Real on Earth (permafrost); plausible on a warming Mars **[SME to verify the Mars framing]** | Unintended consequence | Surveying and building on rock (C2, B5) | Low | Keep |
| Terraforming in ~40 hours | Fantasy (timescale) | Compresses centuries into play | Cause and effect visible in play (B1) | Medium: "terraforming is quick" | Keep; journal states real timescales (warming ~100 years; oxygen up to ~100,000 years) |
| Breathable air at equilibrium | Simplified (no filler gas tracked; no atmospheric loss) | Win condition = learning outcome | Holding the balance (B4, A5) | Low now that breathability uses O2 partial pressure. Remaining gaps get journal notes (filler gas; slow loss to solar wind over millions of years or more) | Keep |
| **Dormant subterranean fauna (thawlings)** | **Fantasy**, grounded in real speculation (option B, decided) | Ecosystem response and unintended consequences (B5, D1, B2) | **Yes:** their numbers follow the player's machine signature; non-lethal answers (shrouds, lures, quiet zones) are systems choices; fighting treats the symptom only | Medium, reduced by labeling: "there are animals on Mars". No life has been found on Mars. | **Keep, decided.** Journal labels them every time; Hydrosphere stage is a **later life stage**, never a "mutation" |
| Engineered flora and biome seeding | Plausible future technology | Photosynthesis and the carbon cycle (A5) | Balancing O2 and CO2 (B4) | Low-medium | Keep; based on real plant and algae biology |
| Colonization beacon and ships | Fiction (story frame) | Motivation: a clear end goal | — (motivational) | Low | Keep |
| Weather mast (windsock, balloon, thermometer, gas tubes) | Real instruments, simplified (windsock rises with air density **[SME]**; gas tubes stand in for a gas analyzer) | Honest in-world cue for invisible gases (A1, A5) | Reading an instrument (C3) | Low: shows O2/CO2 as readings, never as sky color | Keep (user adopted) |
| Training Hall lobby (class servers) | Fiction, set on Earth before the story | Waiting area; no learning job | — | None | Keep (story bible §3.6) |
| Player on a planned solo watch (backstory) | Fiction | Motivation and urgency | — (motivational) | Low | Keep (backstory approved); story bible passes the rules above |
| Fauna ending cards | Fiction (outcome of the fauna fiction) | Consequences of the player's choices (D1) | Reflecting on what they did (D1) | Low: no ranking, no moral grade | Keep |

### The fauna: decision

**Decided (user, 2026-11-26):** option **B** plus real environmental effects, with non-lethal options, "later life stage" wording, and ending cards that describe outcomes without ranking. Options A (pure labeled fiction) and C (no creatures) were considered and not chosen. The fauna is **fluid** design: behavior and numbers will change after playtests; the framing and labeling are settled.

**What they are.** Thawlings (working name, "Group S") are fictional animals descended from *hypothetical* underground microbial life. Scientists really do study whether microbes could survive in salty brines deep under Mars's ice, and Earth has extremophiles and animals that survive freezing in a dormant state. **No life has ever been found on Mars.** The journal says this plainly every time the fauna appear, and explains what's real (the search for life, extremophiles, brines) and what's invented (the thawlings).

**Life cycle (no "mutation").**

| Stage | Trigger | Behavior |
|---|---|---|
| Dormant | — | In frozen brine under the ice. Basu's samples in Fridge B. |
| Stirring | Thaw gate | Small, few, drawn to warm machines |
| **Later life stage** | Hydrosphere gate | Larger, more active, damage emitters and extractors |
| Settled | Phase 3, near a quiet zone or stable biome | Graze and nest; stop raiding |

Story and journal text never say "mutate" or "mutation". Individuals don't change their genes in response to warmth; animals do have life stages triggered by temperature and water.

**What drives them.** Every active terraforming machine has a signature (heat, water, vibration). Fauna pressure is the sum of signatures, shown on the Monitor's fauna page. More machines → more raids: a reinforcing loop the player can see (B2).

**What the player can do.** All four are real choices with costs; none is "the right answer".

| Option | How it works | Cost | Goals |
|---|---|---|---|
| Defend (lethal) | Fight raids at machines | Doesn't lower fauna pressure; raids continue while signatures stay high; lowers the Group S population | C2 |
| Insulation shrouds (deterrent) | Halve a machine's signature | Machine output −15 % | B4, B2 |
| Lure stations (deterrent) | Draw fauna away from machines | Water upkeep each sol | B4, C1 |
| Quiet zones | Areas with no active machine nearby; fauna there calm down and later settle | Land you don't industrialize | D1, B2, B4 |

Thawlings go after machines, not players, and only fight back if attacked. Numbers: balance sheet §6.

**Ending cards.** At the beacon, cards describe the planet as the player left it, read from world state: where the thawlings live (neighbors in the biomes; driven to the deep ice; or only Basu's samples left), plus margins, biomes, and sols held as plain numbers. **No ranking, no score, no "good ending" or "bad ending".** Wording: [story bible §6.5](docs/design/story-bible.md).

**Real effects alongside the fiction.** Perchlorate and ground collapse (Hazard Systems) are real side effects of warming wet soil and melting ground ice, so "unintended consequences" (B5) never depends on fiction alone.

**Review needed.** The Lead Instructional Designer checks the alignment (B2, B4, B5, D1). The SME checks the journal labeling and the real-science hooks.

### Fidelity levels (Chapter 5)
The right authenticity is the right **cognitive fidelity** for the player's expertise, not the most realistic look or simulation. Excess physical and functional detail adds cognitive load. (Definitions: TEAM.md section 3.)

| Kind of fidelity | Target | Why |
|---|---|---|
| Cognitive | High | Players must make the decisions real planetary engineers would: which lever to pull, predicting effects, balancing trade-offs. |
| Functional | High where it teaches, simplified elsewhere | Direction and rough size of every Index relationship are right. No simulation of wind fields, chemistry kinetics, or orbital mechanics. |
| Physical | Moderate | Minecraft style with real Mars colors. Detail beyond what a cue needs is cut. |
| Temporal | Low | Compressed and labeled. |

Fidelity adapts to expertise: middle school depth shows fewer variables and simpler readouts; high school "read more" adds partial pressures and carbon flows.

**Testing rules for authenticity**
- Playtest throughout development, not just at the end.
- Write **test scenarios** that check each learning objective can be completed (one per goal ID, in the playtest plan).
- Run **separate test passes** for functionality (does it work?) and instructional effectiveness (does it teach?).
- Balance SME input: SMEs judge accuracy and cues, but aren't experts in instructional or game design; weigh their reactions against the learning requirements and game design.

## Fun

Both disciplines agree fun is important and desirable, but **not necessary for instructional effectiveness**. They differ on priority: instructional designers put learning first, game designers put fun first, and game designers believe more strongly that fun helps learning (Coleman et al., 2010). The team's positions are in TEAM.md 8.4. This section turns them into design practice.

### Why fun matters for this game specifically
Fun isn't required for learning in general. Here it is required for other reasons:
- **Market appeal** is one of our two goals. Hobbyists play voluntarily for ~40 hours, and nobody does that without fun.
- **Classroom buy-in:** teachers adopt what students engage with.
- **Time on task:** the learning happens through play, so players who stop playing stop learning.

So fun is a **success criterion** (Scorecard: Appeal), not a claim that fun causes learning. Whether fun improves learning here is a hypothesis our playtests test (fun rating vs. learning gain).

### What fun means in this game
Fun should come **from the learning activity itself**, not be decoration added around it.

| Kind of fun | Where it comes from | Learning it carries |
|---|---|---|
| **Mastery and challenge** | Keeping the dome alive; holding equilibrium through drift events | Resource planning (C1), balancing (B4) |
| **Discovery** | Trying a machine and seeing the planet respond; ice vanishing, then puddling, then pooling | Cause and effect (B1), prediction (B3), water (A2) |
| **Building and creating** | Domes, outposts, transit networks, quiet zones | Engineering design (C2) |
| **Visible progress** | Sky turning from tan toward blue, water pooling, plants spreading, Monitor milestones | Every Index parameter (A1–A5) |
| **Tension and threat** | Storms, radiation spikes, fauna raids, ground collapse | Unintended consequences (B5), diagnosis (C2) |
| **Story and fantasy** | Backstory, colony log, the beacon, ending cards | Motivation; ethics (D1) |
| **Cooperation** | Shared group planet, colony posts | Trade-offs (B4), communication |

### Fun–learning matrix
Every feature is placed on this grid in design reviews.

| | **High learning** | **Low learning** |
|---|---|---|
| **High fun** | **Core.** Protect and invest here. | **Allowed in moderation** if it passes the fantasy rules (no negative learning, low extra load) and supports motivation. Keep it light. |
| **Low fun** | **Redesign.** Find the fun inside the learning activity, don't just add a reward on top. | **Cut.** |

### Feature placement (2026-11-26)
Design judgment only; nothing here is playtested yet. Re-place after each playtest.

| Feature | Fun | Learning | Quadrant | Decision |
|---|---|---|---|---|
| Regolith Outgasser (first planet lever) | High: first visible change to the planet | High: B1, C3 | Core | Adopt |
| Armstrong line and Everest pressure milestones (SME optional) | High: memorable landmarks | High: A1 | Core | Adopt (Monitor milestones, not gates) |
| Ice-to-water rule (vapor → brief puddles → pools) | High: discovery | High: A2 | Core | Adopt, SME to confirm thresholds |
| Breathable by O2 partial pressure | Medium: a harder, richer target | High: A1, A5, B4 | Core if the Monitor shows O2 partial pressure clearly; Redesign if players can't read it | Adopt; watch in playtest |
| Drift events during holds | High: active tension | High: B4, C2, C3 | Core | Adopt |
| Beacon components buildable during holds | Medium-high | Low | Moderation | Adopt (cuts idle time) |
| Polar frost loss (sink) | Medium | High: B2 | Core-adjacent | Adopt, pending SME |
| Ice–albedo feedback (SME optional) | Medium: a surprising speed-up | High: B2 | Core | Adopt as a Monitor cause line, size pending SME |
| CO2 from decay (SME optional) | Medium: makes biomes living, not static | High: A5 | Core | Adopt |
| Carbon reservoirs page on the Monitor (SME optional) | Low unless it answers "where did my CO2 go?" | High: A5, B2 | Redesign | Adopt as a simple flow view on the Monitor's Phase 3 page, opened from the CO2 line, not a separate screen |
| Perchlorate dust, meltwater, soil | Medium (risk of chore) | Medium-high: A1, C1, C2, D2 | Redesign | Adopt with one block per stage; cut stages that test as chores |
| Ground collapse | High: dramatic, visible | High: B5, C2, D2 | Core | Adopt |
| Thawlings (option B) | High | High if driven by machine signature: B2, B5, D1 | Core | Adopt |
| Lethal defense | High | Low alone | Moderation | Keep, but it never lowers fauna pressure |
| Shrouds, lures, quiet zones | Medium-high | High: B2, B4, D1 | Core | Adopt |
| Fauna ending cards | High (emotional) | High: D1 | Core | Adopt |
| "Later life stage" wording | Neutral | Removes a misconception | — | Adopt (accuracy) |
| Custom journal (not FTB Quests) | Medium-high: diegetic | High: all goals | Core | Adopt (user decision) |
| Colony posts in group play | Medium-high: ownership | High: per-player evidence | Core | Adopt as suggestions, not locks |
| Rate-based classroom pacing | Neutral to player | Keeps real thresholds: A1, A2 | — (infrastructure) | Adopt |

### Player, not learner
In game text and UI the person is a **player** and a colonist: no "lesson", "objective", "quiz", or "learner". Learning is framed as colony work: predictions are "forecasts", the Colony Record is the **Keeper's Record** of "what the colonists will need to know", self-checks are "What caused it?" pages, and reflection questions are part of the Keeper's Reports. Teacher-facing materials use instructional language.

## Learning Mechanisms in Play

How the game applies research on serious-game elements (*Design and Development of Training Games*, Chapter 3). Each row: the principle, how we apply it, and what's still missing (✅ covered, 🟡 partly, ⬜ gap). Owners close the gaps.

### Cognitive load and schemas
Working memory is limited. **Intrinsic** load comes from the content itself (partial pressure is hard). **Extraneous** load is anything that doesn't help learning (confusing controls, clutter). **Germane** load builds understanding (Chandler & Sweller, 1991; Sweller et al., 1998). If playing is too demanding, players can't learn: in deHaan et al. (2010), students who watched a game learned more than those who played it.

| Principle | How we apply it | Status | Owner |
|---|---|---|---|
| Use a schema players already have | Minecraft's controls, inventory, crafting, and building stay vanilla. New machines use one consistent interface pattern (see In-house machines). | ✅ Spec'd | Lead Game Designer |
| Controls first, content second, for players without the schema | **Landing** onboarding, in story as BELL's first-shift checklist (8 steps, ~15–25 min). No forecast before it's done or skipped. Experienced players skip with "I know the drill". See Closing the gaps. | ✅ Spec'd | Lead Game Designer, Lead Instructional Designer |
| Cut extraneous load from third-party mods | Every machine needed to learn the science is ours and simple. Mekanism and Create are optional, unlock at Hydrosphere, and are never required for a gate. | ✅ Spec'd (Technical Lead: hiding) | Lead Game Designer, Technical Lead |
| Manage intrinsic load by introducing one idea at a time | **Progressive disclosure:** the Monitor shows only the values in play: 2 planet rows in Phase 1 (pressure, temperature; dome O2, water, power on the Dome tab), hydrosphere and fauna at the Thaw, O2, CO2, biomass, and the Air window in Phase 3. Partial pressure is in "read more" at middle school depth. Table: [ui-specs.md](docs/design/ui-specs.md) §2. | ✅ Spec'd | Lead Game Designer, Lead Artist |
| Gameplay and content tightly linked | The mechanic is the lesson: raising pressure *is* the play. | ✅ | — |
| Difficulty matched to skill | Grade bands; fauna scale with infrastructure; hints escalate on repeated failure. | ✅ | — |

### Multimedia and sensory elements
Verbal and visual information use separate channels (Baddeley & Hitch, 1974; Mayer, 2002). Text works best next to the visual it describes (Moreno & Mayer, 1999). Spoken narration beats on-screen text for explanations; adding text to narration neither helps nor hurts (Moreno & Mayer, 1999, 2002).

| Principle | How we apply it | Status | Owner |
|---|---|---|---|
| Pair words with visuals | Journal "what happened" pages pair a small diagram or Monitor graph with ≤ 60 words. No text-only science pages. | 🟡 | Lead Instructional Designer, Lead Artist |
| Put text next to what it explains | Monitor labels and cause lines sit on the graph itself, not in a separate journal page. | 🟡 | Lead Artist |
| Narration for explanations | Recorded messages are text-first (decided). Research favors narration, so **revisit after the Stage 3 playtest**: voice BELL's key explanations, with transcripts kept for accessibility. | 🟡 Revisit | Producer (scope), Lead Game Designer |
| Sound builds context; use it sparingly | **Pressure-driven ambient sound:** at 0.6 kPa outside sounds are faint and thin; as the atmosphere thickens they become fuller. That's real physics (sound carries better in denser air) and an audio cue for pressure. Alarms only for life-threatening events. | ⬜ Gap | Lead Artist (audio), SME (check) |
| A cohesive style beats high-end graphics | Minecraft's low-resolution style is an asset (the book cites it). Our blocks, machines, and UI share one palette and style. Create's art is All Rights Reserved, so any visual matching uses our own original textures. | 🟡 | Lead Artist |
| Self-referential, first-person point of view | First-person play; every message addresses the player directly as "you, Keeper" (Moreno & Mayer, 2004). | ✅ | — |

### Level design: scaffolding, flow, desirable difficulties
| Principle | How we apply it | Status | Owner |
|---|---|---|---|
| Scaffolding in the zone of proximal development (Vygotsky, 1978) | Phases build on each other; hints escalate; grade bands set depth. | ✅ | — |
| **Fading** support as skill grows | Forecast hints shrink from Phase 1 to Phase 3. Failure reports give less detail on repeats. The Monitor's cause breakdown stays available but closed by default in Phase 3. | ⬜ Gap | Lead Instructional Designer |
| **Worked examples** for novices, practice for experts (expertise reversal) | Hana's notes on the first Outgasser are a full worked example; the first processor gets a completion version; later machines get none. Experienced players can hide the notes. | ✅ Spec'd | Lead Game Designer, Lead Instructional Designer |
| Flow: challenge rises with skill (Csikszentmihalyi, 1975) | Pressure builds phase by phase; drift events and fauna scale with progress. | ✅ | — |
| Flow: clear goals | The Monitor's top bar shows the **next gate and progress toward it** ("Next: Phase 2 · Pressure 5 kPa · 62 % · ~4 sols"), log-scaled for pressure, one bar per condition. [ui-specs.md](docs/design/ui-specs.md) §3. | ✅ Spec'd (Artist: visual) | Lead Game Designer, Lead Artist |
| Flow: unambiguous feedback | World responses, Monitor trends, failure reports. | ✅ | — |
| Desirable difficulty: **spacing** (Bjork & Bjork, 2011) | Key ideas return across phases (radiation forecast F2, repeated as F7). | ✅ | — |
| Desirable difficulty: **interleaving** | Drift events during holds mix parameters at random. Later forecasts mix topics from earlier phases. | 🟡 | Lead Instructional Designer |
| Desirable difficulty: **application at unpredictable times** | Storms, radiation spikes, and drift events require recall without warning. | ✅ | — |

### Context and narrative
| Principle | How we apply it | Status | Owner |
|---|---|---|---|
| Endogenous, situated learning (Reiber, 1996; Van Eck, 2006) | Content is the game: colonists terraforming Mars, where every task applies the science. | ✅ | — |
| Narrative **hooks**: player choices tied to the story (Rollings & Adams, 2003) | Choice of interventions, the four fauna options, colony posts, unranked ending cards. | ✅ | — |
| Dickey's (2005) engaged-learning elements: focused roles, goals and challenge, rules, social play, a novel story, choices | Keeper role and colony posts; gates; Index rules; group planets; the story bible; fauna and intervention choices. | ✅ | — |
| **Don't overwhelm with backstory**; avoid seductive details | Backstory is optional (archive, notes). Only BELL and one crew voice are essential per phase (Hana, then Basu, then Moreno); Mensah speaks only in Keeper's Report replies. Check done: story bible §4.4. | ✅ | Lead Game Designer |
| **Advance organizers** (Ausubel, 1960) | At each phase start, a **Shift Briefing** from BELL (≤ 45 words): this shift's job, what to watch, what's new. Text: [messages/](docs/design/messages/README.md). | ✅ Spec'd | Lead Game Designer, Lead Instructional Designer |
| Constructivism: purposeful, authentic, player-directed, challenging, collaborative, reflective | Open-ended play; real Mars values; no single path; group planets; Keeper's Reports. | ✅ | — |

### Feedback types and timing (Chapter 3)
| Principle | How we apply it | Status | Owner |
|---|---|---|---|
| **Process vs. outcome feedback** (Earley et al., 1990): process feedback explains what to do differently; outcome feedback is like a score | New content gets process feedback (failure reports with cause and a hint; forecast explanations). Familiar content gets outcome feedback (Monitor trend arrows, gate progress). | 🟡 | Lead Instructional Designer |
| Too much process feedback overwhelms novices (Wood, 2000) | One cause and one hint per failure report; "read more" for the rest. | ✅ | — |
| **Fade** from process to outcome feedback as skill grows | Process detail shrinks for repeated situations. See fading above. | ⬜ Gap | Lead Instructional Designer |
| Immediate vs. delayed feedback: research is split; immediate is generally best | Immediate world response and brief failure note; deeper explanation delayed to a natural pause (TEAM.md 8.6). | ✅ | — |
| Achievements: role is unclear in research | No achievement system at launch. The Keeper's Record is the progress record. Revisit after playtests. | ✅ Decided (draft) | Lead Game Designer |
| Feedback must be **salient** (sound cue, highlight) | New log entries get a soft chime and a flashing Monitor tab (≤ 3 flashes per second). | 🟡 | Lead Artist |
| Debriefing and after-action review are essential (Peters & Vissers, 2004) | Keeper's Reports at each gate. | ✅ | — |

### Open risk
Narrative and play can both overload players: in Adams et al. (2012), a slideshow beat both a narrative game and the same game without narrative. That makes usability and low extraneous load **the** priority for the Stage 2 prototype, not polish.

### Closing the gaps (Round 2, 2026-11-26)

Specs for the gaps above that the Lead Game Designer owns. Final text is in [docs/design/messages/](docs/design/messages/README.md); screens in [docs/design/ui-specs.md](docs/design/ui-specs.md). All are **fluid**; none has been playtested.

**Landing (controls first, in story).** BELL's first-shift checklist: 8 steps inside and just outside the dome (move, suit O2, Monitor, seal kit, patch a breach, battery, airlock and dust-off, journal). ~15–25 minutes. Each step ticks when the action is detected, and the target gets an outline and an arrow (not color only). "I know the drill" skips it with a 3-line summary. No forecast fires until it is done or skipped; F1 follows only when the first storm is scheduled. Goals: C1 (plus the controls schema). *Rationale:* players without the Minecraft or tech-mod schema can't learn science while fighting controls (deHaan et al., 2010). *Alternatives:* separate tutorial world (breaks the story rule "onboarding stays in story"); no tutorial (loses Lens A players). *Trade-offs:* learning: lower extraneous load; appeal: 15–25 minutes before the "real" game, cut by the skip. *Revisit if* playtest S1 shows Lens A stuck for more than 2 minutes on a step, or the checklist takes over 30 minutes.

**In-house machines for all learning-critical work.** Every machine needed to pass a gate or to see a science relationship is ours: Regolith Oven, Regolith Outgasser, solar panel, battery, O2 generator, water reclaimer, dust-off station, atmospheric processor, super-greenhouse emitter, orbital mirror uplink, ice extractor, meltwater treatment, algae vat, carbon scrubber, soil treatment, biome seed kits, insulation shroud, lure station, quiet-zone marker, weather mast, beacon parts. **One interface pattern** for all of them: input slot, output slot, power bar, status lamp (shape-coded), and two fixed lines: **"Effect on planet"** (its live Index contribution, e.g. "+0.10 kPa/sol") and **"Costs"** (build, power per sol, upkeep part, fauna signature). Mekanism and Create are **optional and late**: unlocked at Hydrosphere for automation and logistics, never required for a gate, never used in journal science, hidden from recipe viewers until unlocked, and hidden by default in the classroom preset. Goals: all (lower load on every goal). *Rationale:* third-party interfaces add extraneous load and recipe depth (risk R-04). *Alternatives:* curated Mekanism subset (still complex; their GUIs aren't ours to simplify); no third-party mods (loses Lens B appeal). *Trade-offs:* learning: every lesson machine is simple and labeled with its effect; appeal: tech-pack fans still get automation late; cost: more machines for us to build (Technical Lead, Producer). *Revisit if* the Stage 2 prototype shows the in-house set is too costly to build, or Lens B playtests show boredom before Hydrosphere.

**Cost rule (one written rule, shown where it matters).** Every machine pays four things, always shown on its "Costs" line: build materials (once), power per sol, one maintenance part every N sols (balance sheet), and its fauna signature (from Thaw on). On a group planet the dome's stores, power grid, and parts are shared; whoever builds a machine is credited in the action log, but the whole team pays its upkeep. Nothing has hidden costs.

**Route choices (depth for little code).** Each big job has at least two routes that fix the same thing at different costs, so choosing is a systems decision (B4):
| Job | Route A | Route B |
|---|---|---|
| Raise pressure (Phase 1–2) | Many cheap Outgassers: low build cost, more upkeep parts | Fewer atmospheric processors: expensive, more power, less upkeep |
| Warm the planet (Phase 2) | Super-greenhouse emitters: cheap, high fauna signature | Orbital mirror uplink: expensive and slow to build, **no ground signature** |
| Water for the dome (Phase 2) | Meltwater treatment: cheap, needs perchlorate filters | Deep-crust extractor: costly power, clean water, high signature |
| Fauna (Phase 2–3) | Shrouds: −15 % output | Quiet zones: land left unbuilt |
| O2 (Phase 3) | Algae vats: fast, controllable, need power | Seeded biomes: slow, free once established, can overshoot |
The orbital mirror route is new: its numbers go to the balance sheet as a draft after the Stage 2 prototype; science framing to the SME (mirrors are a real proposal, SME review B4).

**Worked example: the first Outgasser.** Hana's notes taped to the first Outgasser walk through six steps, each with a short reason: placement, feed, power for the night, switch on (and guess before looking: F3), then *where to look* (the Outgasser line in the Monitor's pressure causes). The notes never state the result, so F3 stays a real prediction. Fading: the first atmospheric processor gets a **completion** version (two steps left for the player), later machines get "You know the drill by now. —H." "Hide Hana's notes" turns them off for experienced players. Goals: B1, C3, C1. *Rationale:* worked examples help novices and hurt experts (expertise reversal), so they fade and can be skipped. *Revisit if* Lens A still can't find the cause line after the example.

**Shift Briefings (advance organizers).** Three BELL briefings (≤ 45 words): Phase 1 after Landing ("keep the dome alive"), Phase 2 after Keeper's Report 1 ("change the planet faster; watch pressure and temperature together"), Phase 3 after Keeper's Report 2 ("hold the balance"). Each names the job, the values to watch, and what's new, without spoiling the fauna. Shown in the side panel, not as a modal pop-up (PROCESS.md §4), and kept in the journal. Goals: C1, A1, A2, A3, B5, A5, B4.

**Character load.** Only BELL and one crew voice are essential per phase (Hana, Basu, Moreno, in that order); Mensah speaks only in Keeper's Report replies. Check and changes: story bible §4.4.

**Feedback wording: positive and fixable (added from user research, Chapter 6).** Failure reports name the **system**, never the player ("Hab B lost air", not "You let Hab B leak"); give the likely cause; and give a hint as a question or as "what holds" ("Seals hold where every outside edge is covered"), never blame and never the full fix. When the player's fix holds, BELL closes the loop with one positive line ("Hab B holding. Seal good."). Story bible §8.3 sample checked: it passes (cause + question hint); the closing line is added.

## Flow and Task Design (added from user research)

From *Design and Development of Training Games*, Chapter 6 (Csikszentmihalyi; Rouse 2004; Schell 2008; Koster 2005; Chen). Design judgment, not playtested.

### Flow conditions
| Flow condition | How we meet it | Unclear spots and fixes | Status / owner |
|---|---|---|---|
| **Clear tasks** | Gates are world conditions; the next-gate display states the target and progress; Shift Briefings name the job | "Raise pressure" alone has no end. Fixed by the next-gate bar ("Pressure 5 kPa, 62 %, ~4 sols"). "Hold the balance" is unclear until the hold counter and Air window are on screen: both shown from Phase 3 | ✅ Spec'd (ui-specs §3, §5) |
| **Clear, immediate feedback** | World responds at once (sky, ice, frost, fire); Monitor trend arrows and cause lines; forecast results first in queue | Pressure changes are slow: a new machine moves the number only a little in a sol. The machine's "Effect on planet" line and the cause line confirm it immediately | ✅ |
| **Concentration (few distractions)** | Interruption rules (TEAM.md 8.6); one message at a time; no modal pop-ups for progress; HUD hidden over full-screen sheets | Message bursts at gates (report, briefing, journal entry, crew message). Fixed: one sequence in the side panel, one item visible at a time | 🟡 Consensus pending (rules) |
| **Attainable, balanced goal** | Gates sized by the balance sheet; holds pause before they reset | The narrow O2 window (21.5–23 % at 75 kPa) may feel unattainable at MS age. Air window shows it widening with pressure; LEARNING.md §4.7 playtest check | 🟡 Playtest |

| Flow characteristic | Our design |
|---|---|
| **Control** (actions visibly affect the outcome) | Every Index change has a named cause; fauna follow the player's own machine signature; no hidden difficulty (see DDA below) |
| **Less self-awareness** | Diegetic UI (Monitor, journal, log); player language only; no quizzes |
| **Altered sense of time** | A risk in class: players lose track of the period. The teacher's end-of-session signal and rule 5 (no new critical failure in the last ~5 minutes) protect them |

### The three task types
| Task type | Ours | Interface support |
|---|---|---|
| **Explicit** (dictated by the game) | Gates, the next-gate display, forecasts, Shift Briefings, Landing checklist, beacon components | Next-gate bar, "Useful now" list, journal |
| **Implicit** (expected, not dictated) | Stay alive, keep the dome sealed, maintain machines, hold values in band, keep fauna pressure manageable | Dome tab (O2, water, power, wear), Shift Board, Air window, hold counters, fauna page. Each implicit task has a gauge that shows how well it's going |
| **Player-driven** (self-set) | Base and outpost design, layout, which routes and interventions to use, where to leave quiet zones, biome placement | Emergent play from simple interacting rules (Index contributions, signatures, ice-to-water rule). We don't script these; we make sure every rule they combine is visible on the Monitor |

### The flow channel against our pacing
Difficulty should rise with skill, between anxiety and boredom. Checked against the balance sheet:

| Stretch (standard) | New skill demanded | Difficulty source | Risk | Check |
|---|---|---|---|---|
| Landing (0–0.4 h) | Controls | None (safe) | Boredom for Lens B | Skip option |
| Phase 1 to Dome stable (~3 h) | C1 loop, sealing | Wear, first storm, first solar event | Anxiety for Lens A (several systems at once) | Landing first; one new system per event |
| Dome stable → 5 kPa (~5 h) | First lever (B1) | Storm dips; frost sink grows near 5 kPa | Boredom in the flat waiting stretch | Outgasser builds every 1–2 sols; ice and F4a discovery; perchlorate; next-gate estimate shows the end |
| Phase 2 (~18 h) | Two levers, A2, fauna loop | Fauna scale with signature; ground collapse | Longest phase; thinnest story per hour (story bible §6.2) | Fauna and route choices add new patterns every few hours; watch idle time |
| Phase 3 build (~9 h) | Balancing A5, B4 | Drift events, overshoot | Anxiety from the narrow O2 window | Air window, F9 early, time-to-edge estimate |
| Holds (~4.7 h, overlapping) | Holding under drift | One drift event per 1–1.5 sols (classroom 0.5–0.75) | Boredom if events repeat; anxiety if they stack | Six event types mixing parameters (interleaving); prototype rule: if "waiting" is reported, shorten Equilibrium to 10 sols before adding events (balance sheet §4) |

### Challenge balance (Chapter 6, 2.6)
| Book tip | Our design |
|---|---|
| Start simplified; open features as skill grows | Three phases; progressive disclosure of Monitor rows (2 → 4 → 6); Mekanism and Create only at Hydrosphere |
| Know the audience | Two lenses (12-year-old new to tech packs; experienced 17-year-old); grade bands |
| Don't introduce many skills at once | Landing teaches controls before science; one lever per gate (Outgasser at Dome stable; processor and emitter at Phase 2; fauna options at Hydrosphere; seeding at Phase 3) |
| Just-in-time guidance tied to current actions | Hana's notes on the machine being built; failure reports on the failure that happened; forecasts triggered by the player's own actions (F3, F5, F6, F8, F10) |
| Don't over-specify | Hints, not answers; worked example fades; no step-by-step quest lines |
| Chapter-based design: tutorial, then harder missions that build on earlier ones | Landing → Phase 1 → 2 → 3, each building on the last (pressure → temperature and water → balance) |
| Chapters of reasonable length | 8 / 18 / 14 h standard; 4 / 7 / 6 periods classroom. Phase 2 is long; its mid-phase gates (Thaw, Hydrosphere) act as sub-chapters with their own world change |
| Add interface as skill grows | Monitor rows, fauna page, Air window, carbon flow view appear when needed |

### Dynamic difficulty adjustment (DDA)
Chen argues most DDA weakens the player's sense of control. **Ours keeps control because every difficulty change comes from a visible choice the player made:**
- Fauna pressure = the sum of the player's machine signatures, shown on the fauna page, lowered by shrouds and quiet zones.
- Hints get more specific after repeated failures of the same kind (adaptive feedback); this changes *information*, not *difficulty*.
- Classroom pace and group size are **configured** by a teacher, visible in settings, and never change thresholds.
- **Confirmed: no hidden difficulty changes exist** in the design: no rubber-banding of storms, wear, or drift events based on how well the player is doing. Storm frequency follows pressure and biomass (visible values). Drift events follow a fixed schedule in sols. If anyone proposes a hidden adjustment, it goes through the consensus process.

**Difficulty as information (PROCESS.md §5).** A `guidance` setting (proposed, fluid): **gentle** (default in classroom) points at the problem, explains blocks, shows "Useful now", and one quiet line on what unlocks more ("More machines unlock at Phase 2: 5 kPa"); **standard** gives hints only after repeat failures; **expert** gives no hints, no "Useful now", no worked example. Thresholds and rates are the same at every level. Needs an ID alignment check (hint levels are feedback).

### Fun as pattern mastery (Koster 2005)
Fun is the brain's reward for learning patterns; its opposites are **noise** (patterns we can't read) and **boredom** (patterns with nothing left to teach). The Index relationships are our patterns.

| Phase | Patterns to master | Noise risk | Boredom risk | Mitigation |
|---|---|---|---|---|
| 1 | Supply vs. demand (O2, power, water); storms cut solar; one machine moves the planet a little | Many dome systems at once | Waiting for 5 kPa | Landing; one system per event; next-gate estimate; ice discovery |
| 2 | Pressure × temperature → water; signature → fauna (reinforcing loop); warmth → ground collapse | Two levers plus fauna plus collapse within a few hours | Long build-out once patterns are known | Thaw and Hydrosphere spaced apart; route choices; Basu's sites |
| 3 | O2/CO2 exchange; band holding; maturity slowdown; drift recovery | Six values plus Air window | Holds after the pattern is mastered | Cause breakdown closed by default (less clutter); drift events mix parameters; beacon building during holds |

### Paradox of choice
Too many options at once overloads players and makes every choice feel costly. **"Useful now"** lists ≤ 3 recommended machines per gate on the Monitor and at the top of the recipe view (ui-specs §3); locked recipes are hidden, not greyed out. Recommendations never lock anything.

## Motivation and Player Types (added from user research)

| Principle (Chapter 6) | Our design | Check |
|---|---|---|
| **Intrinsic over extrinsic** | Rewards are the planet changing: sky, water, plants, helmet off. Intrinsic rewards lead more readily to flow | ✅ |
| **Rewards as control** (if-then rewards cut creativity and shift focus to the reward) | No points, XP, loot for forecasts, or rewards for reading. Forecasts give an explanation, not a prize. Gates unlock tools that are needed for the next job, not bonuses | ✅ None of our rewards reads as control. Watch the Keeper's Record: pages fill from evidence, never pay out items |
| **Baseline rewards** (fair acknowledgment of completed work) | **Our baseline:** gate unlock moments (world change + BELL line + side-panel "just finished" note), Keeper's Reports and Mensah's replies, the Keeper's Record pages filling, the ending cards. Missing acknowledgment feels like cheating, so every gate and hold gets one | ✅ Spec'd |
| **No achievement system at launch** | Fits: achievements risk acting as control. If added after playtests, they must be goal-like, unexpected, and verbal or informational (e.g. a BELL line "First storm survived with no power loss"), never a points tally | ✅ Decided (draft) |
| **Engagement tied to performance** | Flow keeps players engaged; cognitive engagement is the one tied to learning (TEAM.md §3) | Playtest |
| **Scarcity** (players value what costs effort) | Gates, resource limits, the compact dome. What players earn is tied to **player skill, not avatar skill**: the Keeper has no XP or levels; progress is planet state | ✅ |
| **Zeigarnik effect** (visible unfinished tasks drive completion) | Next-gate bar with progress; hold rings; blank Keeper's Record pages. **Caution:** avoid "get every quest" compulsion. Every visible task is tied to a learning goal; there are no collectible checklists | ✅ |
| **Experiential design: five dimensions** | Sensory: art and pressure-driven audio. Cognitive: task structure (gates, forecasts, holds). Active: agency (routes, building, fauna choices). Affective: story emotion arc (story bible §2). Relational: group planets, colony posts, Shift Board | ✅ |

### Bartle player types (players move between them)
| Type | What they get here | Note |
|---|---|---|
| **Explorers** | Ice and water discovery, Basu's drill sites, the archive, lore notes, the weather mast, the survey scan | Rich |
| **Achievers** | Gates, holds, Keeper's Record pages, the beacon, forecast tally (own, per phase) | No leaderboard by design (unranked ending) |
| **Socializers** | Group planets, colony posts, Shift Board, the shared Monitor, class debriefs | Classroom-strong; solo players get the crew's messages instead |
| **Killers** | Lethal fauna defense, which works but never lowers fauna pressure | **No PvP: a deliberate choice, not a gap.** Reasons: classroom safety for ages 12–18 on shared planets; one student could wreck a group's evidence and progress; PvP teaches nothing in our goals. The competitive urge is served by the planet itself (storms, drift events, fauna raids) |

## Core Design Check (added from user research)

From Chapter 9 (ACEDT core design). Core design = the gaming front end, core rules, general mechanics, and the nature of the unfolding play; level details come later.

### What's stable now, and what the Stage 2 prototype may overturn
| Stable now (core) | May be overturned by the Stage 2 prototype |
|---|---|
| One Planetary Index; gates lock in; thresholds never scale | Whether six values are readable at MS age, or Phase 3 needs fewer on screen |
| The core loop: build → Index responds → read Monitor → adjust | Whether the Monitor is enough feedback, or the world cues must carry more (style of instruction) |
| Three phases; equilibrium as a band; beacon as the end | The phase split (8/18/14) and hold lengths |
| Diegetic feedback (Monitor, journal, log); forecasts as the main per-player evidence | Whether forecasts feel like play or homework; how often they can fire |
| Fauna driven by machine signature; non-lethal options | Whether the signature loop is legible enough to teach B2 |
| Player-driven story inside a designer frame | Whether text-first messages are read at all (narration revisit) |

### Story foundation against the book's criteria
| Criterion | Story bible | Result |
|---|---|---|
| Grounded in the learning objectives | §1 table maps every story job to goal IDs | ✅ |
| Meaningful context | Keeper on watch; colonists depend on what you learn | ✅ |
| Plot embeds instructional sequencing | Arc follows pressure → water and warmth → balance; beats tied to gates | ✅ |
| Premise with room for many learning experiences | Whole planet, three phases, routes, fauna choices | ✅ |
| Player can create their own story | Unranked ending cards read from world state; group choices; colony posts | ✅ |

### Four questions for every design decision
Asked in reviews and in the change impact check (proposed to the Producer for TEAM.md 8.2; kept here until then):
1. How is this instruction part of the game?
2. What is this part of the game teaching?
3. How does this instructional technique affect gameplay?
4. Does this aspect support or distract from the instruction?

### How we align gaming and instructional strategy
| Approach | Used? | Why |
|---|---|---|
| 1. Naturally compatible strategies | **Yes, primary.** Guided discovery with implicit hints (world, cause lines) and explicit hints (failure reports, Hana's notes) fits open-world Minecraft play | |
| 2. Instruction first | Partly: goals came first (story "objectives first"), but no instructional sequence is forced on the open world | |
| 3. From key game capabilities | **Yes.** The game lets players safely see consequences that are rare, slow, or dangerous in reality: centuries of warming, runaway loops, an O2 fire, waking life. They fail safely (gates lock in; rollback) and replay | |
| 4. Build both together | **Yes, for feedback.** Guidance ranges from direct consequences (world) and iconic cues (HUD shapes, trend arrows) to instructive text (failure reports, forecast results). Iconic cues are unobtrusive but can be missed, so life-threatening firsts may be modal; everything else waits for natural pauses (TEAM.md 8.6) | |

### Instructional logic: rules that depend on a player action
Our logic is **fixed with rule-based hints**: fixed triggers (gates, first events) plus rules that interpret actions (repeat failures escalate hints). In free play, a player may never take the action a rule needs. Each such rule, and what happens if the player never acts:

| Rule depends on | Goal | If the player never does it | Fallback |
|---|---|---|---|
| Warming ice in Phase 1 (F4a) | A2 | No F4a; the A2 contrast with F4b is lost | The water reclaimer's sample tray: at Dome stable, Hana's note asks the player to drop an ice sample in the oven tray (it happens in front of them). F4b still fires at Thaw |
| Starting the first Outgasser (F3, worked example) | B1, A1 | Can't reach 5 kPa without it | None needed: required for progress |
| Starting an emitter (F5) | A3 | Can't reach 0 °C without warming; the mirror route also warms | F5 also triggers on the first orbital mirror uplink (same question, "mirror" wording) |
| Adding machines after Hydrosphere (F6) | B2 | A player who shrouds early or stops building never sees F6 | B2 still measured by Report 2 (MS) and the post-test; F6 also fires if fauna pressure rises by 3 from any cause |
| A solar event after pressure doubles (F7) | A4 | Scheduler-dependent | The event scheduler guarantees one solar event in Phase 2 after the doubling |
| Turning emitters down (F10) | B4 | Careful players may never do it | The emitter-wear drift event (guaranteed during holds) triggers F10 |
| Building on ground ice (ground collapse) | B5 | No collapse seen | B5 is carried by the fauna loop and Report 2; collapse is optional evidence |
| Claiming repairs on the Shift Board | C2 | Solo or untidy groups never claim | Solo: own repairs credited automatically. Group: unclaimed repairs credit the player who placed the fix block |
| Opening the Monitor before builds | C3 | — | Measured passively; the next-gate bar puts the Monitor in front of players |
If players find any of these hard or confusing in playtests, we treat it as a sign the strategy is misaligned, not that players are wrong.

### The "why" behind the approach (so the "how" can change)
| Goal of the approach (why) | Current method (how), may change |
|---|---|
| The player learns by changing the planet and seeing why it changed | Monitor cause lines, world cues |
| Each player leaves individual evidence on a shared planet | Forecasts, reports, own repairs |
| Science never waits behind story or fantasy | Text-first messages, optional lore, fantasy rules |
| The player feels in control of difficulty | Signature-driven fauna, configured pacing |
No detailed gaming element is locked in that couldn't carry the instruction later: the Monitor, journal, and forecasts are data-driven (datapack JSON), so a playtest can change them without code.

### Prototype early: what the first playable must prove
Prototype the Index simulation before building content: first as a **paper or spreadsheet model** of the balance sheet (curves, holds, drift events), then the Stage 2 rapid prototype. **Straw-man assets** in Stage 2: machines as plain colored boxes with a text label and a status shape; the Monitor as text rows with ASCII-style bars; thawlings as a placeholder mob shape; sky as two or three flat keyframes; no audio except one chime. Conceptual designs go to the user and the SME for face validity before art starts.

| The first playable must prove | Pass when |
|---|---|
| Index values move with machines, and the Monitor names the cause | Tester points to the cause of every change (task 6–7, playtest plan) |
| Phase 1 curve matches the balance sheet within ±20 % | Real time to 5 kPa 6.5–10 h standard; 2–3 h classroom |
| Thresholds are identical in all presets | Gate numbers equal across standard, classroom, group 4 |
| The ice-to-water rule reads as three states | Tester describes vapor, brief puddle, lasting water at the right pressures |
| A forecast can be issued, deferred, locked, and resolved | Action log shows all four states |
| The next-gate bar gives a clear end | Tester can say how close the next gate is without opening the docs |

### First experience to design in detail: the first dust storm
Experience design works on one experience at a time so game, instruction, and art can agree on it. **First pick: the first dust storm in Phase 1** (story beat 4, F1, C1, B3). It touches every layer (world cue, warning, forecast, power loss, recovery, Hana's tip) at small scale, sits in the Stage 3 vertical slice, and is the first real test of "pressure, not grind". Detail spec to write next (handoff to the Lead Artist and Lead Instructional Designer for a joint pass).

## Content Lists (for art and recipes)

Lists the Lead Artist asked for. Names follow the domain (real crop and instrument names). Counts are design drafts; recipes and numbers go to the balance sheet after the Stage 2 prototype.

| List | Items | Goals | Notes |
|---|---|---|---|
| **Dome greenhouse crops** (Phase 1 food; asset F-08, 3 growth stages each) | Potato, lettuce, dwarf wheat, tomato | C1 | Tomato answers the dead-tomato trace ("Next shift: you try"). All four are crops studied for space farming **[SME: verify]** |
| **Biome seed kits** (Phase 3; asset F-07) | 1 **Biocrust** (pioneer: cyanobacteria and lichen crust; grows on treated regolith) · 2 **Tundra meadow** (mosses, sedges, cold grasses; needs biocrust soil) · 3 **Wetland margin** (algae mats, reeds; needs adjacent water) · 4 **Cold forest** (dwarf conifers and shrubs; needs meadow soil and temperature ≥ 5 °C) | A5, B2, B4 | A succession chain: each kit needs the soil the one before makes. Teaches that ecosystems build on each other. Real-biology basis **[SME: verify]** |
| **Maintenance parts** (asset I-01) | Filter cartridge (air and perchlorate dust), seal kit (seals and gaskets merged), bearing (fans, pumps, rotors), heater coil (ovens, Outgassers, emitters) | C1, C2 | Four parts serve all machines. The Artist's "gasket" merges into the seal kit (one fewer item, same job) |
| **Beacon components** (asset I-07; recipes unlock at Breathable, assembly at Equilibrium) | Mast segment (×7), transmitter core, dish array (uses the repaired main dish design), landing-guide lamp (×8, the style guide's lamp ring), **air record** | B4 (holding the band) | The air record is crafted at the Monitor only at Equilibrium: it stores the hold data, the "proof that the air holds" the fleet is waiting for. Five icon types |


## Story Approach (Chapter 4)

Applies *Design and Development of Training Games*, Chapter 4. The story bible (`docs/design/story-bible.md`) carries the detail; this section records the approach.

### Objectives first
The story was built after the learning goals and to serve them, not the other way around. A change to the goals changes the content, and the story follows. (Matches the book's process: objectives → audience → story elements, side by side with content.)

### Designer-driven or player-driven?
**Player-driven inside a designer frame.** Open goals and free building call for a player-driven story (Church, 1999); the designer frame (the Keeper's watch, the crew's messages, the beacon) gives purpose and the D1 ethics thread.
- Because it's player-driven, **the connection between each action and the game's reaction must be absolutely clear**: Monitor cause lines and visible world change.
- **Story stays within the player's scope of control.** Background events (Earth's decisions, the First Shift) are reminded through repeated messages and props, not single subtle mentions, so players don't miss them.

### Storytelling methods
| Method | Type | Our use |
|---|---|---|
| **Player actions** (action verbs match story verbs) | Integrated | **Primary.** The story verbs are seal, raise, warm, thaw, seed, balance. Those are exactly what the player does. |
| Scripted scenes | Integrated | Few: first storm arrival, first thaw, helmet-off. Triggered by gates, never taking control from the player for long. |
| In-game dialog | Integrated | None two-way. Characters speak through one-way recorded messages. |
| Voice-over | Integrated | Text first (decided); possible later for BELL. Never while an alarm is sounding (the two would compete). |
| **In-game text** | Integrated | Colony log, notes, journal. Lowest cost; short and paired with visuals. |
| Cut-scenes, animatics | Encapsulating | Only the ending sequence. Encapsulated moments halt play, so they're kept to the beacon. |
| Slide show | Encapsulating | Possibly the Keeper's Report summaries. |
| External media | Encapsulating | Educator materials and the launcher page set up the story before play. |

Rule: where story and high interactivity meet, **separate them by time or space**. Story beats land at natural pauses (TEAM.md 8.6), not mid-building.

### Voice decisions (compare the book's DCT example)
| Question | Our answer |
|---|---|
| Character narrator or omnipotent narrator? | Station system **BELL** as a character voice, plus crew messages. No omnipotent narrator. |
| Does the player see themselves? | First person; hands and suit only. |
| Avoiding gendered references to the player | Addressed as "Keeper" or "you". No he/she for the player anywhere. |
| How does the player communicate in the story? | Through actions, Keeper's Reports to Earth, and Shift Board entries. |

### Stay in story
The onboarding ("Landing", the controls tutorial) is **in story**: the Keeper's first shift checklist from BELL, not a separate tutorial screen. Every later system follows the same protocol as the tutorial (BELL briefs, the log confirms briefly).

### Relevance and motivation
- **Relevance:** the story connects to what 12–18 year olds already know: Mars rovers and missions in the news, climate change on Earth (D2), survival and building games.
- **Motivation ("so what?"):** the colonists arriving depend on what the Keeper learns ("what the colonists will need to know").
- **Audience attributes** are in LEARNING.md section 0.

### Development challenges and our mitigations
| Challenge | Mitigation |
|---|---|
| Underestimating story writing time (about 2 hours per line of dialog, by one estimate) | Text-first, one-way messages; word limits per medium (story bible); story writing scheduled in Stage 4 with an effort budget *(Producer)*. |
| Dialog without enough player choice | Choice lives in play (interventions, fauna options, colony posts), not in dialog trees. |
| Story integrated too late | Story bible exists before any code; the Stage 3 vertical slice includes the essential Phase 1 beats; story tested in the first playtest. |
| Relying too heavily on story | No "story sandwich" (story, then an info dump, then story); story is spread throughout. Ending cards are descriptive and unranked; the player's own choices make the story. |
| Metaphor causing negative transfer | Fantasy rules and audit (above); fauna labeled fiction. |

### Story review (the book's StoryJam step)
Before Stage 3, a full-team story review: each role reviews the story bible against all six story elements (characters, voice, setting, plot, conflict and resolution, emotion), using the perspective round. The SME is involved in every pass.

## Group Play: One Planet, 3–5 Players

**Decided (user, 2026-11-26):** in classroom mode, groups of 3–5 students share one planet; a class runs several planets (see Decisions). The same model fits small home servers. Multiplayer scaling still applies within a group.

### How a group shares a planet
- **One Index, one dome, one story.** All machines feed the same Index; all players face the same storms and fauna. Story beats fire once per planet and reach everyone; offline players find them in the journal archive.
- **Personal pieces stay personal.** Journal, forecasts, Colony Record, Keeper's Reports (debriefs), and "What caused it?" answers are per player. That gives individual evidence on a shared planet (handoff to the Lead Instructional Designer, M-14).
- **The dome fits five.** Five bunks and life support sized for up to five. O2, water, and food use scale with real player count, so a bigger group feels more Phase 1 pressure (C1). Compact before wide survives because the dome doesn't grow with the group.

### Colony posts (roles)
Posts are **suggested, not enforced**. Any player can do anything. The journal offers them at the start of each phase, and players pick one at the dome's **Shift Board** (a block that lists open failure reports and who has claimed each, using in-game names only; nothing leaves the server).

| Post | Phase 1 focus | Phase 2 focus | Phase 3 focus | Main goals |
|---|---|---|---|---|
| Life Support | O2, water, power, airlock | Dome upkeep, meltwater treatment | Biome water | C1, C2 |
| Atmosphere | Regolith Outgassers | Processors, emitters | Pressure and CO2 margin | A1, A3, B1 |
| Survey | Storm and radiation watch | Outposts, ground ice, fauna | Quiet zones, Group S | A4, B5, D1 |
| Biology | Dome greenhouse | Algae vats | Seeding, O2 band | A5, B4 |
| Data | Monitor, forecasts | Trends and causes | Holds and drift events | B3, C3 |

With 3 players, Data merges into Atmosphere and Biology into Life Support. With 4, Data merges into Survey. **Posts rotate each phase**, so over the unit each player works at least two different posts and sees more than one goal area. Teachers can assign posts; the game only suggests.

### Coordination
- **Shared Planetary Monitor** in the dome: the group's common picture.
- **Shift Board:** claim a failure, mark it fixed. Credit for fixing goes to the claimer's log (anonymous ID) for C2 evidence.
- **Forecasts are individual** even when the group acts together, so each player's B3 evidence is their own.
- **Absent players:** their post is covered by anyone; BELL's "Since last shift" recap catches them up. The planet's rates use the configured group size, not who is online, so an absence doesn't change the planet.
- **No player-versus-player damage** by default.

### Fauna and pacing in a group
- Fauna pressure comes from machine signatures, so a group that builds twice the machines gets about twice the raids: about the same pressure **per player** as solo. A planet cap keeps mob counts playable (balance sheet §6).
- Group rate scale: `1 / (1 + 0.35 × (n − 1))` for a configured group size n (balance sheet §5). With 4 players, each machine adds about half as much, so the planet moves at about solo speed while the building goes faster.
- Classroom pacing is tuned for a group of 4. Groups of 3 or 5 work without retuning (about ±0.5 h over the unit).

## Decisions

| Decision | Answer | What it means for the design |
|---|---|---|
| Progress scope | **Global**: one planet shared by everyone on it | One Planetary Index per world (per planet when a server hosts several). All players' machines feed it and all players face the same threats. |
| Audience | **Singleplayer, small servers (roughly 1–6 players), and classroom group planets of 3–5** | Balance for one player first, then a group of 4 in classroom mode. |
| Classroom group model | **Groups of 3–5 share one planet; a class runs several** (user decision) | See Group Play. Hosting: ARCHITECTURE.md. |
| Playtime | **About 40 hours** from spawn to beacon | Pacing targets below. |
| Mars world | **Custom dimension** | Built in the companion mod. Ad Astra is out; oxygen and suits need a replacement (see mod stack). |
| Regression | **Gates lock in** | Values can fall, unlocked phases and recipes cannot. Helmet-off is a live condition. |
| Learners | **Ages 12–18**, NGSS-aligned | See [LEARNING.md](LEARNING.md). |
| Classroom mode | **Yes**: ~11–12 hours of play over ~17 play periods | Same content and gates, faster rates, shorter holds, cheaper recipes, teacher tools. See Pacing and LEARNING.md section 5. |
| Multiplayer scaling | **Fauna by machine signature, plus a static group-size rate scale** | See below. Thresholds never scale. |
| Journal | **Custom** (user decision) | Companion mod's learning module. FTB Quests rejected. |
| Fauna | **Option B plus real environmental effects; non-lethal options; "later life stage"; unranked ending cards** (user decisions) | See "The fauna: decision". |
| Equilibrium pressure | **≥ 75 kPa** (user decision on SME option B) | Lets ~22 % O2 reach the breathable O2 partial pressure. |
| Story | **Player is the Keeper; planned-solo-watch backstory; text-first recorded messages** (user decisions) | See the story bible. |

### Pacing Targets (40 hours, solo)

| Gameplay phase | Hours | Cumulative | Ends at gate |
|---|---|---|---|
| Phase 1: Survival Desperation | ~8 | 8 | Phase 2 begins (pressure ≥ 5 kPa) |
| Phase 2: Industrial Geo-Engineering | ~18 | 26 | Phase 3 begins (pressure ≥ 30 kPa, temperature ≥ 0 °C) |
| Phase 3: Biome Seeding & Equilibrium | ~14 | 40 | Equilibrium → beacon |

Phase 2 is the longest because it holds the industry build-out and the fauna escalation. The split is a starting point to test in the prototype, not a fixed target.

### Classroom Pacing (group of 4, ~11.3 hours)

Classroom mode compresses **rates and hold times, never thresholds**. Players see the same real numbers (5 kPa, 0 °C, 16 kPa of O2) as in standard mode, so the journal's science stays true.

| Lever | Standard | Classroom |
|---|---|---|
| Rate multiplier (all Index contributions) | 1.0 | 4.0 |
| Dome stable / Breathable / Equilibrium hold | 3 / 7 / 14 sols | 1 / 2 / 3 sols |
| Recipe preset (KubeJS) | Standard | Cheaper |
| Gate thresholds | Real values | **Same real values** |

| Gameplay phase | Hours | Play periods (~40 min) |
|---|---|---|
| Phase 1 | ~2.4 | ~3–4 |
| Phase 2 | ~5.1 | ~7–8 |
| Phase 3 | ~3.8 | ~5–6 |
| **Total** | **~11.3** | **~17** |

Model and assumptions: balance sheet §5. The unit plan that places these periods is in LEARNING.md §5.2.

### Multiplayer Scaling

**Options considered:**
- Scale gate thresholds with player count. **Rejected:** scaling a physical threshold teaches false science (SME review, finding 3), and temperature thresholds can't be multiplied sensibly (−20 °C × 0.3 is harder, not easier).
- Scale fauna pressure with active terraforming machines rather than player count.
- Accept faster group runs and only scale threats.

**Chosen:** fauna pressure scales with the signature of active terraforming machines. A **static** config value for group size sets a rate scale (default group size 1, rate scale 1.0, so singleplayer is unchanged). It is read from config, not from who is online. Classroom pacing uses its own separate config keys. This keeps the intent of the original decision (a server knob that slows group progress) but moves it from thresholds to rates.

## Open Questions

None at the concept level. Science items waiting on the SME are listed in the balance sheet §8. Technical questions are tracked in Stage 1 of [ROADMAP.md](ROADMAP.md).
