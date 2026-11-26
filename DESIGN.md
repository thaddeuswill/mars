# Mars Terraforming Modpack — Design

## Core Concept & Game Loop

A total-conversion Minecraft modpack built around terraforming a planet in stages. There are no linear NPC questlines. The world itself is the main progression gate and tells the story. Players start on a barren, hostile Mars. They move from desperate survival inside a closed bio-dome, to industrial geo-engineering, to a planet that sustains itself.

**Core loop:** survive → extract → process → emit into the atmosphere → the world changes → new threats and new resources → expand.

Each change to the planet should be visible and felt: sky color, weather, which blocks survive on the surface, and which mobs spawn.

---

## Progression Phases

### Phase 1: Survival Desperation (Early Game)
- **Setting:** Trapped inside a starting bio-dome on a sterile, toxic surface.
- **Mechanics:** Strict oxygen, water, and power management. Scarce resources push players to build tall and compact rather than wide.
- **Threats:** Dust storms, solar radiation spikes, and mechanical wear on life-support systems.

### Phase 2: Industrial Geo-Engineering (Mid Game)
- **Setting:** Expanding out from the bio-dome with airlocks, pressurized transit tubes, and automated outposts.
- **Mechanics:** Atmospheric processors, greenhouse gas emitters, and deep-crust water extractors that thicken the atmosphere and raise surface temperatures.
- **Threats:** Ecosystem blowback. Water and warmth wake dormant underground fauna and mutate it. The danger shifts from the environment alone to creatures that attack your infrastructure.

### Phase 3: Biome Seeding & Equilibrium (Late Game)
- **Setting:** A world in transition. Liquid water pools, storms die down, and engineered flora takes root.
- **Mechanics:** Seeding complex biomes that sustain themselves, and balancing the oxygen/CO2 ratio in the atmosphere.
- **End Goal:** Full planetary equilibrium. The player can take off the spacesuit helmet for good, survive on the surface unaided, and build the beacon that signals the first colonization ships.

---

## Implementation Plan (proposal)

### 1. World State: the Planetary Index

One server-wide state object drives all progression. Machines write to it and world systems read from it.

| Parameter        | Start  | Equilibrium | Raised by                                   | Effects as it rises                                    |
|------------------|--------|-------------|---------------------------------------------|--------------------------------------------------------|
| Pressure (kPa)   | 0.6    | ≥ 60        | Atmospheric processors, regolith outgassing | Fewer suit leaks, thicker sky fog, liquids become stable |
| Temperature (°C) | −60    | ≥ 5         | Greenhouse emitters, orbital mirrors        | Ice melts into water, fewer cold-damage zones           |
| Hydrosphere (%)  | 0      | ≥ 30        | Deep-crust extractors, ice-comet impacts    | Surface water placement, rain, wakes fauna              |
| O2 fraction (%)  | 0.1    | 19–23       | Algae vats, seeded biomes                   | Fire possible, breathable once in band                  |
| CO2 fraction (%) | 95     | < 1         | Biomes, carbon scrubbers                    | Must fall while temperature holds (balancing act)       |
| Biomass          | 0      | threshold   | Seeded flora spreading naturally            | Unlocks self-sustaining biomes                          |

- Persist it in world saved data so it survives restarts and is shared in multiplayer.
- Rates scale with machine count and tier, and values can **fall**: unmaintained machines, storms, and fauna damage all push them back. That keeps the mid game active rather than idle.
- **Gates lock in.** Once a gate is reached it stays unlocked even if the values drop below its threshold. Falling values cost progress toward the *next* gate and bring back hazards (thicker storms, colder surface), but never take away unlocked recipes or phases. The one exception is the helmet-off state: it is a live check, so the air can become unbreathable again if O2 leaves the band.
- O2 and CO2 pull against each other in the late game. Too much O2 means fire hazards and runaway flora; too much CO2 cancels the warming. Equilibrium is a band to hold, not a number to hit.

### 2. Phase Gates (the world as quest log)

Gates are world-state thresholds, not quest checkboxes. Recipes, structures, and spawns unlock when the planet reaches a condition.

| Gate                     | Condition                         | Unlocks                                                        |
|--------------------------|-----------------------------------|----------------------------------------------------------------|
| Dome stable              | Life support runs 3 in-game days with no failures | Airlocks, EVA suit tier 2, first outpost blueprint |
| Phase 2 begins           | Pressure ≥ 5 kPa                  | Atmospheric processor, transit tubes                          |
| Thaw                     | Temperature ≥ −20 °C              | Ice extraction, **dormant fauna spawns begin**                 |
| Hydrosphere              | Hydrosphere ≥ 10 %                | Surface water, mutated fauna variants, algae vats              |
| Phase 3 begins           | Pressure ≥ 30 kPa and temperature ≥ 0 °C | Biome seed kits, storms weaken                          |
| Breathable               | O2 in band for 7 in-game days     | Helmet optional on the surface                                 |
| Equilibrium              | All parameters in band for 14 in-game days | Colonization beacon recipe                            |

Progress is shown in the world itself: sky tint, fog density, the storm table, and block ticks (ice → water, regolith → soil near seeded flora). A diegetic **Planetary Monitor** block shows the numbers for players who want them.

### 3. Hazard Systems

- **Oxygen and pressure:** suit O2 drains outside sealed volumes. Dome breaches vent the atmosphere. Sealing works by checking enclosed volumes.
- **Dust storms:** a weather event that reduces visibility, cuts solar output, and wears exposed machines. Storms come less often as pressure and biomass rise.
- **Radiation spikes:** periodic solar events. Exposure on the surface builds a radiation effect, and shielding blocks and depth protect against it. Spikes weaken as the atmosphere thickens.
- **Mechanical wear:** life-support machines lose durability and need maintenance parts. This keeps the early game tense and gives scrap a use.
- **Ecosystem blowback:** fauna spawning is tied to temperature and hydrosphere. Creatures target emitters and extractors, so how hard you push terraforming sets the difficulty.

### 4. Candidate Mod Stack

Target **NeoForge, Minecraft 1.21.1** unless a key mod forces otherwise. Confirm versions before locking anything in.

| Role                         | Candidate(s)                     | Notes                                                  |
|------------------------------|----------------------------------|--------------------------------------------------------|
| Mars dimension               | **Custom** (companion mod)       | Full control of sky, fog, weather, and terrain response to the Planetary Index |
| O2, sealing, suits           | Custom, or a standalone oxygen/suit mod if one fits | Must hook into the Planetary Index so ambient O2 and pressure come from world state |
| Industry / gas processing    | Mekanism, Create                 | Gas chemistry maps well onto atmospheric processing    |
| Scripting the Planetary Index | KubeJS (+ ProbeJS for dev)      | Global state, recipe gating, event hooks               |
| Worldgen                     | Datapack + TerraBlender, or custom | Regolith, ice caps, buried fauna lairs              |
| Optional guidance            | FTB Quests (as a lore journal)   | Reads world-state gates only, no NPC questlines        |
| Custom content               | Companion mod (core of the pack) | Dimension, Planetary Index storage, sky/fog rendering, storm weather, fauna |

With a custom Mars world, the companion mod is no longer small: it owns the dimension, the Planetary Index, rendering, and weather. KubeJS stays the place for recipes, gate unlocks, and tuning, so balance changes don't need a Java rebuild.

### 5. Milestones

1. **Vertical slice:** spawn in the dome, O2/power/water loop, one dust storm, one atmospheric processor that moves pressure.
2. **Planetary Index:** persistent state, Planetary Monitor block, sky/fog tied to pressure.
3. **Phase 2 content:** transit tubes, outposts, thaw, first fauna wave.
4. **Phase 3 content:** biome seeding, O2/CO2 balancing, helmet-off check.
5. **Ending:** colonization beacon and a cinematic or credits moment.

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
| Starting values (pressure, temperature, CO2) | Real | — | Reading the Monitor (C3) | Low | Keep |
| Dust storms: visibility, solar loss, dust wear | Real | — | Planning around power loss (C1) | Low. **Avoid** storms with damaging wind force: Mars's thin air can't do that, and it's a common misconception from films. | Keep, with that limit |
| Radiation spikes | Real | — | Shielding decisions (A4) | Low | Keep |
| Atmospheric processors, greenhouse emitters, orbital mirrors, ice-comet impacts | Based on real proposals, simplified | Engineering interventions | Choosing and balancing interventions (B4) | Medium: implies these are proven technologies | Keep; journal labels them as proposed ideas |
| Terraforming in ~40 hours | Fantasy (timescale) | Compresses centuries into play | Cause and effect visible in play (B1) | Medium: "terraforming is quick" | Keep; journal states real timescales |
| Breathable air at equilibrium | Simplified | Win condition = learning outcome | Holding the balance (B4, A5) | Medium: real Mars likely can't keep a thick atmosphere without a magnetic field | Keep; journal note on atmospheric loss |
| **Dormant subterranean fauna** | **Fantasy** | Ecosystem response and unintended consequences (B5, D1) | Defending infrastructure — **weak match** unless their behavior is driven by the player's terraforming choices | **High:** "there are animals on Mars". No life has been found on Mars. | **Needs team decision** (below) |
| Engineered flora and biome seeding | Plausible future technology | Photosynthesis and the carbon cycle (A5) | Balancing O2 and CO2 (B4) | Low-medium | Keep; based on real plant and algae biology |
| Colonization beacon and ships | Fiction (story frame) | Motivation: a clear end goal | — (motivational) | Low | Keep |
| Player alone in a dome (backstory) | Fiction | Motivation and urgency | — (motivational) | Low | Keep; story bible must pass the rules above |

### Open decision: the fauna (ill-structured)

The fauna serve appeal strongly and are a good metaphor for unintended consequences, but they carry the biggest negative-learning risk in the game. Options for the team:

| Option | Appeal | Learning | Accuracy risk |
|---|---|---|---|
| **A. Keep as fiction, clearly labeled.** Journal says plainly: no life has been found on Mars; these creatures are invented. | High | Metaphor intact | Medium, reduced by labeling |
| **B. Ground in real speculation.** Fauna are fictional descendants of *hypothetical* underground microbial life, which scientists do study as a possibility. The journal explains what's real (the search for life, extremophiles on Earth) and what's invented. | High | Stronger: links to astrobiology and D1 ethics ("what if there *was* life here?") | Lower, if labeled carefully |
| **C. Replace with an environmental response.** No creatures. The "blowback" is runaway effects instead: sinkholes from melting ground ice, toxic perchlorate dust from wet soil, algae blooms clogging machines. | Lower (no creatures to fight) | Strong, and fully realistic | Very low |

Whichever option is chosen, rule 2 applies: fauna behavior should respond to the player's terraforming choices (more emitters → more aggression), so dealing with them exercises systems thinking, not just combat.

Recommendation: **B**, with some of C's real effects (perchlorates, ground collapse) added alongside the fauna. It needs review by the Lead Game Designer, Lead Instructional Designer, and Subject Matter Expert through the consensus process.

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
| **Mastery and challenge** | Keeping the dome alive; holding equilibrium | Resource planning (C1), balancing (B4) |
| **Discovery** | Trying a machine and seeing the planet respond | Cause and effect (B1), prediction (B3) |
| **Building and creating** | Domes, outposts, transit networks | Engineering design (C2) |
| **Visible progress** | Sky turning from tan toward blue, water pooling, plants spreading | Every Index parameter (A1–A5) |
| **Tension and threat** | Storms, radiation spikes, fauna attacks | Unintended consequences (B5), diagnosis (C2) |
| **Story and fantasy** | Backstory, colony log, the beacon | Motivation; ethics (D1) |
| **Cooperation** | Shared planet on small servers | Trade-offs (B4) |

### Fun–learning matrix
Every feature is placed on this grid in design reviews.

| | **High learning** | **Low learning** |
|---|---|---|
| **High fun** | **Core.** Protect and invest here. | **Allowed in moderation** if it passes the fantasy rules (no negative learning, low extra load) and supports motivation. Keep it light. |
| **Low fun** | **Redesign.** Find the fun inside the learning activity, don't just add a reward on top. | **Cut.** |

### Player, not learner
In game text and UI the person is a **player** and a colonist: no "lesson", "objective", "quiz", or "learner". Learning is framed as colony work: predictions are "forecasts", the Colony Record tracks "colony expertise", reflection questions come from the colony log. Teacher-facing materials use instructional language.

## Decisions

| Decision | Answer | What it means for the design |
|---|---|---|
| Progress scope | **Global**: one planet shared by everyone on the server | One Planetary Index per world. All players' machines feed it and all players face the same threats. |
| Audience | **Singleplayer and small servers** (roughly 1–6 players) | Balance for one player first. On servers, more players means more machines, so rates and threats need a player-count check (see below). |
| Playtime | **About 40 hours** from spawn to beacon | Pacing targets below. |
| Mars world | **Custom dimension** | Built in the companion mod. Ad Astra is out; oxygen and suits need a replacement (see mod stack). |
| Regression | **Gates lock in** | Values can fall, unlocked phases and recipes cannot. See Planetary Index. |
| Learners | **Ages 12–18**, NGSS-aligned | See [LEARNING.md](LEARNING.md). |
| Classroom mode | **Yes**: ~11–12 hour preset | Same content and gates, faster pacing, teacher tools. See LEARNING.md section 5. |
| Multiplayer scaling | **Fauna by infrastructure, plus a gate-threshold config** | See below. |

### Pacing Targets (40 hours, solo)

| Gameplay phase | Hours | Cumulative | Ends at gate |
|---|---|---|---|
| Phase 1: Survival Desperation | ~8 | 8 | Phase 2 begins (pressure ≥ 5 kPa) |
| Phase 2: Industrial Geo-Engineering | ~18 | 26 | Phase 3 begins (pressure ≥ 30 kPa, temperature ≥ 0 °C) |
| Phase 3: Biome Seeding & Equilibrium | ~14 | 40 | Equilibrium → beacon |

Phase 2 is the longest because it holds the industry build-out and the fauna escalation. The split is a starting point to test in the prototype, not a fixed target.

### Multiplayer Scaling

With global progress, a group of four could otherwise finish in a quarter of the time with a quarter of the tension. Options to evaluate in the prototype:

- **Scale gate thresholds or upkeep with online player count** (e.g. +25% per extra player, capped).
- **Scale fauna pressure** with the number of active emitters rather than player count, so it tracks how hard the group pushes.
- **Accept faster group runs** and only scale threats. This is simplest, and may be fine for casual servers.

**Chosen:** fauna pressure scales with the number of active terraforming machines, and a server config multiplier for gate thresholds defaults to 1.0 (singleplayer unchanged).

## Open Questions

None at the concept level. Technical questions are tracked in Stage 1 of [ROADMAP.md](ROADMAP.md).
