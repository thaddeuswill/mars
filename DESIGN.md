# Mars Terraforming Modpack — Design

## Core Concept & Game Loop

A total-conversion Minecraft modpack built around terraforming a planet in stages. There are no linear NPC questlines. The world itself is the main progression gate and tells the story. Players start on a barren, hostile Mars. They move from desperate survival inside a closed bio-dome, to industrial geo-engineering, to a planet that sustains itself.

**Core loop:** survive → extract → process → emit into the atmosphere → the world changes → new threats and new resources → expand.

Each change to the planet should be visible and felt: sky color, weather, which blocks survive on the surface, and which mobs spawn.

**Time unit.** A **sol** is one in-game day: 20 real minutes of simulation time. Simulation time stops when the world is paused (the Technical Lead defines it in ARCHITECTURE.md). All hold times and delays are in sols.

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
| Industry / gas processing | Mekanism, Create | Gas chemistry maps well onto atmospheric processing |
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
In game text and UI the person is a **player** and a colonist: no "lesson", "objective", "quiz", or "learner". Learning is framed as colony work: predictions are "forecasts", the Colony Record tracks "colony expertise", reflection questions come from the colony log. Teacher-facing materials use instructional language.

## Group Play: One Planet, 3–5 Players

**Decided (user, 2026-11-26):** in classroom mode, groups of 3–5 students share one planet; a class runs several planets (see Decisions). The same model fits small home servers. Multiplayer scaling still applies within a group.

### How a group shares a planet
- **One Index, one dome, one story.** All machines feed the same Index; all players face the same storms and fauna. Story beats fire once per planet and reach everyone; offline players find them in the journal archive.
- **Personal pieces stay personal.** Journal, forecasts, Colony Record, Keeper's Reports (debriefs), and Explain-it answers are per player. That gives individual evidence on a shared planet (handoff to the Lead Instructional Designer, M-14).
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
