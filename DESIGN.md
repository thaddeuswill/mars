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
- Each roadmap stage checks both goals before moving on.

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
