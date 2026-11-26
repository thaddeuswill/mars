# Software Architecture

This file covers how the software is built. It addresses five practices:

1. **Architecture:** the components and how they interact
2. **Algorithms:** the logic behind the key systems
3. **Coding practices:** shared style and conventions
4. **Elegant design:** clean responsibilities, no hidden side effects
5. **Future enhancements:** hooks so new features don't need rewrites

Sections 6–12 cover the classroom and data systems: group planets and hosting, session control, learning data and privacy, the story system, the admin API, LMS integration, and the performance budget.

> **Draft, owned by the Technical Lead.** No code exists yet. This will be revised during the Stage 2 prototype ([ROADMAP.md](ROADMAP.md)). Supporting notes: [docs/tech/](docs/tech/) (mod licenses, school deployment).
>
> **Pending Stage 1 confirmation** (consistency review m-19): the Minecraft version (proposed 1.21.1) and whether oxygen and sealing are custom or come from a standalone mod. Everything below assumes 1.21.1 and a custom sealing system; both are marked where they matter.

---

## 1. Architecture

### 1.1 Components

The pack has three layers. Each has one job.

| Layer | Technology | Responsibility |
|---|---|---|
| **Companion mod** | Java, NeoForge | Systems: the Mars dimension, the Planetary Index, simulation, hazards, fauna, rendering, journal, story delivery, learning tools, teacher tools |
| **Data layer** | Datapack JSON (server) + language files (resource pack, client) | Content and tuning: parameters, gates, live conditions, story messages, epilogue cards, journal entries, prediction prompts, Colony Record triggers, balance numbers, all player-facing text |
| **Script layer** | KubeJS | Recipes, recipe gating, and integration with third-party mods (Mekanism, Create). Not the Index, not gates, not tuning. |

Rule of thumb: **systems in Java, content in data, glue in scripts.** A balance change, a new story message, or a new journal entry should never need a Java rebuild.

### 1.2 Companion mod modules

```
                         ┌─────────────────────────┐
  Machines, flora,  ───► │ Planetary Index         │ ◄─── Admin API (admin module only)
  storms, fauna          │ one per planet          │
  (contributions)        │ (single source of truth)│
                         └────────────┬────────────┘
       SimClock (session) gates every │ step: no step while paused
                                      │ IndexUpdatedEvent(planet, …)
     ┌──────────────┬─────────────┬───┴────────┬──────────────┬──────────────┐
     ▼              ▼             ▼            ▼              ▼              ▼
  Gates +        Hazards       Fauna      World response   Story         Learning
  live           (storms,      (spawn     (sky, fog,       (message      (journal data,
  conditions     radiation,    weights)   block changes)   queue,        Colony Record,
  (latched /     O2)                                       archive,      predictions,
  live)                                                    recap,        action log,
     │                                                     epilogue)     export)
     └──► GateReachedEvent / LiveConditionChangedEvent ──► recipes unlock, story beats, journal, debrief
```

| Module | Owns | Depends on |
|---|---|---|
| `core` | Public API, events, shared types (`PlanetId`, `PlayerRef`, `Cause`), the `SimClock` and `AdminControl` interfaces | Nothing |
| `planets` | Planet registry, dimension slots, player-to-planet assignment, lobby | `core` |
| `session` | Pause and resume, simulation clock and sols, session IDs, backups, rollback requests, timelines | `core` |
| `index` | Planetary Index state per planet, persistence, simulation, derived values | `core` |
| `gates` | Gate and live-condition definitions (from data), evaluation, holds, latched unlocks, milestones | `core` |
| `dimension` | Mars dimension type, worldgen, terrain response (ice → vapor or water, regolith → soil) | `core` |
| `hazards` | Oxygen and sealing (*custom, pending Stage 1*), dust storms, radiation, machine wear, perchlorate, ground collapse | `core` |
| `machines` | Terraforming machines, Planetary Monitor, Shift Board | `core` |
| `fauna` | Mobs, signatures, spawn rules, planet caps, Group S population | `core` |
| `story` | Message queue, message archive, "Since last shift" recap, epilogue cards | `core` |
| `learning` | Journal content selection, prediction prompts, Colony Record, Keeper's Reports, action log, CSV export, pseudonymous IDs, grade band | `core` |
| `admin` | Teacher and debug commands, teacher overview data, the only caller of `AdminControl` | `core` |
| `client` | Sky, fog, HUD, Planetary Monitor screen, **journal screen** (entries, message archive, Keeper's Record, forecasts, reports), **teacher overview screen**, Shift Board screen | `core` (synced data only) |

Every module depends only on `core`. Modules never call each other directly: they talk through the API and events (section 4). This keeps any module replaceable. An ArchUnit test in the build enforces it (section 3).

### 1.3 Interfaces (APIs)

The `core` module defines the contracts. Everything planet-related is keyed by `PlanetId` (section 6).

```java
// One planet = one Mars dimension with its own Index. Standard play has exactly one.
record PlanetId(ResourceLocation dimension) {}

interface Planets {
    Collection<PlanetId> all();
    Optional<PlanetId> planetOf(ServerLevel level);
    Optional<PlanetId> assignedPlanet(PlayerRef player);
    PlanetaryIndex index(PlanetId planet);
}

// Read and contribute to one planet's state. Server-side only.
interface PlanetaryIndex {
    double get(ParameterId parameter);              // stored or derived (e.g. o2_partial_kpa)
    IndexSnapshot snapshot();                       // immutable copy for readers
    ContributionHandle contribute(ParameterId parameter, double ratePerSol, SourceId source);
}

// Machines hold a handle and update or remove it. They never write values directly.
interface ContributionHandle {
    void setRate(double ratePerSol);
    void remove();
}

interface Gates {
    boolean isUnlocked(PlanetId planet, GateId gate);           // latched: once true, stays true
    boolean isActive(PlanetId planet, LiveConditionId cond);    // live: re-checked every step
    HoldProgress hold(PlanetId planet, GateId gate);            // sols held, paused or not
}

// Simulation time. Stops while paused. All "in-game days" are sols of this clock.
interface SimClock {
    long simTicks(PlanetId planet);
    double sols(PlanetId planet);
    boolean isPaused();
    SessionId currentSession();
    int timeline(PlanetId planet);                  // +1 after each rollback of that planet
}

// Why something happened. Learning ignores ADMIN causes when crediting players.
enum Cause { SIMULATION, PLAYER, ADMIN, DEBUG }

// Events posted on the NeoForge event bus. All carry the planet.
record IndexUpdatedEvent(PlanetId planet, IndexSnapshot before, IndexSnapshot after) {}
record GateReachedEvent(PlanetId planet, GateId gate, long simTick, Cause cause) {}
record LiveConditionChangedEvent(PlanetId planet, LiveConditionId cond, boolean active, long simTick) {}
record MilestoneReachedEvent(PlanetId planet, MilestoneId milestone, long simTick) {}
record HazardEvent(PlanetId planet, HazardType type, BlockPos origin, Severity severity) {}
record SessionEvent(SessionEventType type, SessionId session) {}      // PAUSED, RESUMED, ROLLBACK, …
record LearningEvent(PlanetId planet, PlayerRef player, String type, Cause cause, Map<String, Object> data) {}
```

`PlayerRef` wraps the Minecraft UUID **in memory only**. Nothing written by `learning` may contain it: the action log writer accepts only `Pseudonym` (section 8). The `AdminControl` interface is in section 10.

### 1.4 Who can do what

| Actor | Can read | Can change |
|---|---|---|
| Server systems | Everything | Index (through contributions), gates, world |
| Clients (players' game) | Synced snapshot of **their own planet**: index values, unlocked gates, live conditions, delivered messages; their own journal, Colony Record, forecasts, grade band | Nothing directly; send actions (e.g. answering a prediction, claiming a Shift Board item) to the server |
| Players | Monitor, Shift Board, own journal and Colony Record | The world, through normal play |
| Teachers (permission `mars.teacher`, default operator level 2) | Teacher overview for all planets; pseudonym ↔ name lookup **on the server only** | Pause and resume, roll back, set phase, set grade band, assign players to planets, export CSV, change pacing config, delete a player's ID mapping |
| Debug (permission `mars.debug`, operator level 4, and `debug.enabled = true` in config) | Everything | Set any Index value, latch or unlatch any gate, step the simulation. Off in release builds' default config. |
| KubeJS scripts | Index, gates | Recipes; add contributions through the same API as machines |

The **server is authoritative**. Clients only render what they're sent, which avoids desync in multiplayer and cheating in classrooms. Permission nodes use the NeoForge permission API, so a school can map them to its own permission tool.

### 1.5 Data formats and storage

Two storage areas, on purpose (consistency review M-06):

- **World data** (inside the world folder): the game state. Restored by rollback.
- **Mars data folder** (outside the world folder): `<server or game directory>/mars-data/<worldId>/`. The learning evidence and identity. **Never restored by rollback, never inside a backup, never sent over the network.**

| Data | Format | Location | Rolled back? |
|---|---|---|---|
| Index state per planet, latched gates, hold counters, live-condition state, simulation clock, story queue and delivered-message list | NBT saved data with a schema version, one set per planet | World: the planet's dimension data folder | Yes (it is the planet) |
| Per-player game state: read/unread messages, last-seen sim tick, Shift Board claims | NeoForge data attachments on the player (serialized, `copyOnDeath`) | World: `playerdata/` | Yes, with that player's planet |
| `worldId` (random, created with the world) | NBT saved data | World: overworld data | n/a (never changes) |
| Parameters, derived values, gates, live conditions, milestones, holds | JSON | Datapack: `data/mars/parameters/`, `data/mars/gates/`, `data/mars/conditions/` | n/a (content) |
| Story messages, epilogue cards, recap templates | JSON (IDs, triggers, delays, band variants) | Datapack: `data/mars/story/` | n/a |
| Journal entries, prediction prompts, Colony Record triggers, goal → NGSS map | JSON | Datapack: `data/mars/learning/` | n/a |
| All player-facing text | Language files, with `.ms` / `.hs` key variants | Resource pack: `assets/mars/lang/` | n/a |
| Pacing and group config | NeoForge server config (TOML) | World: `serverconfig/mars-server.toml` | No (teacher setting) |
| Session state: paused flag, session ID, timeline per planet, planet assignments, planet grade-band overrides | JSON | Mars data: `session.json`, `planets.json` | **No** |
| Pseudonym mapping (Minecraft UUID → random ID) | JSON | Mars data: `identity/players.json` | **No** |
| Action log | JSON Lines, one event per line, pseudonymous IDs only | Mars data: `actionlog/<date>_<session>.jsonl` | **No** |
| Learning records per player: Colony Record, forecasts and results, Keeper's Reports, Explain-it answers, grade band choice | JSON, one file per pseudonym, written atomically | Mars data: `learning/players/<pseudonym>.json` | **No** |
| Backups | Zip of the world folder | Mars data: `backups/` | n/a |
| Teacher exports | CSV (UTF-8) | Mars data: `exports/` | n/a |
| Client sync | NeoForge custom payload packets | Network (server ↔ players' game only) | n/a |

Details: section 7 (backups, rollback), section 8 (IDs, log, CSV, grade band, Colony Record), section 9 (story).

---

## 2. Algorithms

### 2.1 Planetary Index simulation
- **Fixed-step update**, not every game tick. The simulation runs once every 100 ticks (5 seconds) **per planet**, and only while the session is not paused (section 7). The planet changes over hours of play, so finer steps waste CPU.
- **Contributions are cached, not polled.** Each machine registers a rate once and updates it only when its state changes (on, off, broken). Each step sums the cached rates: cost scales with the number of machines, not with ticks × machines.
- **Rates are per sol.** One step adds `rate × pace.rateMultiplier × groupRateScale(group.size) × (100 / solTicks)`. Multipliers touch rates only, **never thresholds** (DESIGN.md: physical thresholds never scale; consistency review M-02; balance sheet §5).
- **Coupled update** for each step:
  1. Sum contributions for each parameter.
  2. Apply coupling: temperature gets a greenhouse term (from pressure, CO2, and super-greenhouse gas); ice melts into hydrosphere only when temperature and pressure allow liquid water; biomass converts CO2 to O2; decay returns CO2.
  3. Apply sinks and decay (e.g. polar frost loss, pressure leak without upkeep).
  4. Clamp to physical limits.
  5. Recompute **derived values** (O2 partial pressure = pressure × O2 fraction; CO2 partial pressure; boiling point of water at the current pressure). Derived values are defined in data and are read-only. They are part of the synced snapshot, so the Planetary Monitor shows them like any other value (DESIGN.md §1).
  6. Post `IndexUpdatedEvent` if anything changed by more than a small amount.
- Simple step-by-step (Euler) integration is enough at this step size. Coefficients live in data so balance can be tuned without code.

**Pacing and group config keys** (balance sheet §5.1; one key per job, per consistency review M-02):

| Key | Scope | Standard | Classroom | Used in |
|---|---|---|---|---|
| `pace.rateMultiplier` | Server | 1.0 | 4.0 | Every contribution and sink (2.1) |
| `pace.holdSols.<gate>` | Server | 3 / 7 / 14 | 1 / 2 / 3 | Hold gates (2.2) |
| `pace.holdResetSols` | Server | 1.0 | 0.5 | Hold reset grace (2.2) |
| `pace.solTicks` | Server | 24,000 (20 min) | 24,000 | Simulation clock (7.1) |
| `pace.recipePreset` | Server | `standard` | `cheaper` | Read by KubeJS scripts at load to pick the recipe set |
| `group.size` | **Per planet**, static | 1 | Teacher sets 3–5 | `groupRateScale(n) = 1 / (1 + 0.35 × (n − 1))`; fauna planet cap (2.4). Never read from who is online. |
| `story.delayScale` | Server | 1.0 | 0.5 | Story delays (9.2) |

None of these keys is applied to a threshold. Thresholds have no scaling field at all (2.2).

### 2.2 Gates, live conditions, holds, and milestones
Four kinds of condition, all defined in data with the same condition language (consistency review M-03):

| Kind | Evaluated | Stays true? | Example |
|---|---|---|---|
| **Gate** | Each `IndexUpdatedEvent` until reached | **Latched.** Stored as unlocked and never re-evaluated (gates lock in, DESIGN.md) | Phase 2 begins: pressure ≥ 5 kPa |
| **Hold gate** | Each step; a hold counter in sols | Latched once the hold completes | Breathable: condition held 7 sols |
| **Live condition** | Every step, forever | **No.** Can turn off again | Helmet-off: O2 partial pressure ≥ 16 kPa, pressure ≥ 75 kPa, CO2 partial pressure ≤ 0.5 kPa |
| **Milestone** | Each step until reached | Fires once, unlocks nothing | Armstrong line (6.3 kPa) on the Monitor |

- **Condition language** (JSON): `all`, `any`, `not`, and comparisons on stored or derived parameters, plus a few named world checks (e.g. `machine_state` for Dome stable). Thresholds are plain numbers. There is **no field for scaling a threshold**: the code has no way to multiply one.
- **Hold rule** (balance sheet §4): the counter adds simulation time while the condition is true and **pauses** while false. It resets to zero only if the condition stays false for longer than `pace.holdResetSols` (1 sol standard, 0.5 classroom). Hold length comes from `pace.holdSols.<gate>`. Counters use simulation time, so a paused world never moves them.
- **Live condition flicker guard:** a live condition changes state only after its new value has held for `liveConditionDwellTicks` (default 200 ticks, 10 s). This stops the helmet state flipping every step when a value sits on the edge.
- **Helmet-off is two things.** The *Breathable* hold gate (latched) unlocks the feature: helmets become optional and the beacon component recipes unlock. The *breathable_air* live condition decides, every step, whether a player without a helmet takes harm. That matches DESIGN.md §1.
- **Cause:** a gate latched by the admin API carries `Cause.ADMIN`. Learning doesn't credit anyone for it (section 10).

### 2.3 Sealing (dome and airlock detection)
*Custom system, pending the Stage 1 search for a standalone oxygen mod.*
- Flood fill from the oxygen source through air blocks, with a maximum volume cap. If the fill hits the cap or reaches open sky, the room isn't sealed.
- **Cached:** result stored per sealed volume and only recomputed when a block changes inside or on the boundary of that volume.
- Runs over a few ticks if large, so it never causes a lag spike.

### 2.4 Fauna pressure
- Machines are stored in a **per-chunk index** per planet (map from chunk position to machines and their signatures).
- **Fauna pressure F** per planet = Σ machine signatures (tier 1/2/3) − shroud reductions (balance sheet §6). Kept as a running sum: updated only when a machine is built, removed, switched on or off, or shrouded. Reading it costs nothing; it is synced to the Monitor's fauna page.
- Raids per sol = 0.15 × F (× 1.5 after Hydrosphere), scheduled in simulation time. A raid targets the highest-signature machine, found from the per-chunk index.
- Spawn weight near a position = base weight × f(nearby machine signatures) × g(temperature, hydrosphere). Looking up nearby chunks is fast because of the index.
- **Group S population** (0–100) and per-planet fauna stats are kept in the planet's saved data for the ending cards (9.5).
- **Caps:** per-planet cap from the balance sheet (§6: 12 + 4 per configured player, hard cap 30), plus a **server-wide cap** across all planets (`fauna.serverCap`, default 80, *to measure in Stage 3*). When the server cap is hit, planets share it in proportion to their own caps.
- This implements "fauna scales with infrastructure" from the multiplayer scaling decision, and keeps it inside each group's planet.

### 2.5 World response
- **Sky and fog:** the client interpolates between keyframes defined in data. **Any Index parameter or derived value can be an input** (consistency review M-17); the Lead Artist chooses which parameter drives which visual. Smooth transitions, no per-frame lookups.
- **Terrain changes** (ice → vapor or water, regolith → soil): driven by random block ticks with a probability scaled by the Index, so the whole world isn't rewritten at once.
- **Local temperature for the ice rule** (DESIGN.md §2): global mean + day/night offset + heat from nearby machines.

  | Approach | Cost per ice random tick | Notes |
  |---|---|---|
  | **Per-block, exact** (sum heat from every machine within range, with distance falloff) | Look up ~9 chunks in the per-chunk index and loop over their machines: tens to hundreds of operations | Estimate: with simulation distance 4 (81 chunks per player) and vanilla's default 3 random ticks per chunk section per tick, a player's area gets a few thousand random ticks per tick. If ~10 % land on ice, that is several hundred lookups per tick per player. Likely well under a millisecond, but it grows with players × machines. *Estimate, not measured.* |
  | **Cached per-chunk heat map (recommended)** | One map read: O(1) | Each chunk stores the heat it receives from machines. Recomputed for the ~9 chunks around a machine only when that machine is built, removed, or switched. Heat is uniform within a chunk (16 × 16 blocks), which is fine for "ice near a heater melts". |
  | Global only | O(1) | Loses "ice near machines melts first", which the Thaw beat needs. Not recommended. |

  **Recommendation:** the cached per-chunk heat map. Same player-visible behavior at constant cost. If playtests show chunk edges look wrong, add a cheap distance falloff from the nearest machine only.

### 2.6 Learning
- Prediction prompts: answer stored on the server, then resolved when the predicted condition is reached or a timeout passes (timeouts in simulation time).
- Colony Record: each learning goal is a set of trigger rules (in data) matched against events on the bus. Details in section 8.5.
- Action log: events buffered in memory and written to disk in batches on a background thread (section 8.3).

### 2.7 Story delivery
- A per-planet queue of scheduled messages, checked each simulation step. Details in section 9.

### 2.8 Shift Board (group coordination block)
- A block in the `machines` module. It lists the planet's **open failure reports** (from `failure.reported` events) and who has claimed each.
- **State:** a per-planet list of `(failureId, claimedBy, status)` in the planet's saved data. Any Shift Board on the planet shows the same list.
- **Names:** the screen shows **in-game names** of claimers (already visible in game). Nothing is stored by name: the claim holds a `PlayerRef` in memory and saved data, and the action log records `shiftboard.claimed` and `failure.fixed` with the pseudonym only. Credit for a fix goes to the claimer's log (C2 evidence, DESIGN.md Group Play).
- **Cost:** updated on claim, fix, or new failure only. No ticking.

---

## 3. Coding Practices

| Area | Convention |
|---|---|
| Language | Java 21. NeoForge for Minecraft 1.21.1 requires a Java 21 JDK (NeoForge docs, 1.21.1 getting started; Mekanism's 1.21.x build also sets `java_version=21`: see [docs/tech/mod-licenses.md](docs/tech/mod-licenses.md)). The Minecraft version itself is *pending Stage 1*. |
| Build | Gradle with NeoForge's ModDevGradle plugin (the 1.21.1 MDK template uses it). Gradle wrapper committed. |
| Formatting | Google Java Style, enforced by Spotless (Apache-2.0) in the build. Formatting is never discussed in review: the tool decides. |
| Package layout | `mars.<module>` matching the modules above; `mars.<module>.internal` for anything not part of the API |
| Module boundaries | An ArchUnit test fails the build if any module other than `core` is imported by another module, or if anything except `mars.admin` uses `AdminControl`. |
| Naming | Classes are nouns (`DustStorm`), methods are verbs (`applyDecay`), IDs use `snake_case` (`mars:atmospheric_processor`) |
| Side separation | Client-only code only in `mars.client`. The server must run without any client classes. |
| State | No static mutable state outside registries. State lives in the Index, world saved data, player attachments, block entities, or the Mars data folder (learning only). |
| Planet scope | Every piece of game state is keyed by `PlanetId` or lives in the planet's dimension. Nothing assumes "the" Mars dimension. |
| Time | Game logic uses simulation time from `SimClock`, never the daylight cycle or wall-clock time. Wall-clock time appears only as a timestamp in the action log. |
| Nulls | Use `Optional` for "may be missing" return values. |
| Logging | One logger per module; no `System.out`. Debug logs never print a Minecraft UUID next to a pseudonym. |
| Testing | **Unit tests** (JUnit 5) for simulation, gates, holds, live conditions, story scheduling, ID mapping, CSV export, and scoring as pure Java, with no Minecraft needed. **GameTests** for in-world behavior (sealing, machines, pause freezing third-party machines). **Privacy test:** a test writes a full session log and fails if any UUID pattern appears in `actionlog/` or `exports/`. |
| Scripts | KubeJS: one file per mod being integrated; recipe IDs always explicit. |
| Data | JSON validated at load; a bad file logs a clear error naming the file and field. Every story and learning JSON has a schema in `mod/src/main/resources/schemas/`. |
| Git | Small commits with clear messages; feature branches; review before merge. |
| Docs | Every public API type has a short Javadoc. Design decisions go in DESIGN.md, not code comments. |

---

## 4. Elegant Design

The rules that keep components clean and free of hidden side effects:

1. **Single source of truth.** All planetary state lives in the Planetary Index of that planet. No system keeps its own copy of "current pressure".
2. **One-way data flow.** Contributions go in, the simulation steps, events go out. Systems that react to events never write back to the Index in the same step. The admin API is the only other way in, and it posts the same events.
3. **Talk through events, not direct calls.** Hazards don't know the learning module exists. Learning listens to `HazardEvent`. Adding or removing a module doesn't break the others.
4. **Pure simulation core.** The Index math, gate and hold logic, and story scheduling have no Minecraft dependencies, so they can be unit-tested and reasoned about in isolation.
5. **Content is data.** Code defines *how* gates, conditions, and messages work; data defines *which* exist.
6. **Immutable snapshots for readers.** Readers get a copy (`IndexSnapshot`), so nothing can change state by accident while reading it.
7. **Game state and evidence live apart.** Rolling back the game never erases what students showed (section 7).
8. **Identity stops at one boundary.** Minecraft UUIDs exist in memory and in one mapping file. Everything learning writes uses pseudonyms (section 8).

---

## 5. Hooks for Future Enhancements

| Hook | What it allows later without rewrites |
|---|---|
| **Parameter registry** | Add a new planetary parameter (e.g. filler gas, soil nitrogen) with a JSON file and a coupling rule |
| **Derived values in data** | New Monitor readouts (e.g. boiling point) from existing parameters, no code |
| **Data-driven gates and conditions** | New gates, live conditions, milestones, phases, or a whole new planet's progression from data only |
| **Contribution API** | Other mods or KubeJS scripts add terraforming sources through the same `contribute()` call as built-in machines |
| **Event bus** | Add-ons react to index changes, gates, and hazards without touching core code |
| **Hazard strategy interface** | New hazard types (meteor showers, quakes) plug in as one class each |
| **Learning triggers in data** | New learning goals, prompts, and Colony Record entries without code |
| **Story in data** | New messages, beats, and epilogue cards without code |
| **Config presets** | Standard and classroom modes are bundles of config values (`pace.*`, `group.size`, hazard severity, recipe preset). New presets, e.g. a "hard mode", need no code. |
| **Schema version in saved data** | Saves migrate forward when the data format changes, so worlds survive updates |
| **Planet as a parameter** | The Index, gates, story, and fauna are keyed by `PlanetId` (the dimension). The same code runs one planet at home and several group planets on a class server (section 6). A moon could be another planet type later. |
| **Export interface** | CSV today; an opt-in xAPI exporter later plugs into the same event stream (section 11) |

### Design patterns in use
| Pattern | Where |
|---|---|
| Observer (events) | Index updates, gates, live conditions, hazards, story, learning |
| Registry | Parameters, gates, conditions, hazards, fauna, messages |
| Strategy | Hazard types, coupling rules, exporters |
| Handle | Contribution handles that machines own and release |
| Snapshot (immutable value object) | Index reads |
| Data-driven configuration | Gates, parameters, journal, story, balance |

---

## 6. Group Planets and Hosting (classroom model)

**Decision being implemented (user, 2026-11-26):** groups of 3–5 students share one planet; a class runs several planets.

**Term.** A **planet** is one Mars dimension with its own Planetary Index, gates, story, and fauna. Standard play (singleplayer or a small server) has one planet per world. A classroom server can host several planets in one world save. The settled rule "one shared Planetary Index per world" holds per planet: everyone on a planet shares one Index. (Vocabulary clarification proposed for TEAM.md; DESIGN.md already says "one Index per world, or per planet".)

### 6.1 Problem framing

How several planets run is **ill-structured**: it trades teacher setup effort, school hardware, isolation between groups, and build cost, and the right answer depends on the school. The options below give my discipline's view. The Lead Instructional Designer (teacher workload, unit plan), the Producer (scope), and the Lead Game Designer (lobby and group fiction) should weigh in.

| | **A. One server, one dimension per group** (recommended) | **B. One server program per group** | **C. One student hosts each group ("Open to LAN")** |
|---|---|---|---|
| What it is | One server program. Planets are dimensions `mars:planet_1` … `mars:planet_8` in one world. Players are assigned to a planet. | Several copies of a normal one-planet server on one or more machines, each on its own port | Each group plays a singleplayer world opened to the school network by one student |
| Teacher setup | One install, one start command, one place for pause, backups, export | One install per group (scripted); N consoles; N exports to merge | Nothing on a server, but N student hosts to manage; the host must be present every class |
| Hardware | One server machine. Shared baseline: one Java process, mods loaded once | The baseline (Java + all mods) repeated N times. Likely needs several machines or a large one (*to measure*) | Host student PCs run a server and a game at once: the hardest load lands on lab PCs |
| Isolation | One group's lag affects all; a crash stops all planets | Full: one group's crash affects no one else | Full |
| Pause, backups, rollback | One command for the class; per-planet rollback supported (section 7) | Per server; scriptable | Per host PC; no teacher control unless the teacher is at that PC |
| Data and privacy | All logs in one Mars data folder on the teacher's server machine | N folders to collect | Logs on student PCs: **conflicts with keeping evidence on the teacher's machine** |
| Build cost (ours) | Planet-keyed state (already planned), planet assignment, a lobby, per-planet rollback | Almost none beyond option A's single-planet code; a launcher script | None extra, but teacher tools barely work |
| Fit with design pillars | Same terrain on every planet (shared world seed) lets the class compare outcomes in debriefs | Same, if seeds match | Same, if seeds match |

**Recommendation: A, with B as the documented fallback.** A gives the teacher one thing to start, pause, back up, and export, which is the Feasibility target (setup under 30 minutes). B needs no extra code, because a one-planet server *is* the standard mode, so it is a free fallback for schools with spare machines or when one group's problems must not affect others. C is not supported as a classroom mode (logs end up on student PCs), though LAN play still works for clubs and homes.

**Assumptions to challenge:** a class has ≤ 8 groups (≤ 40 students); the school has one machine that can be the server; groups don't need to visit each other's planets; identical terrain across planets is a plus, not a minus.

**What would settle it:** a Stage 3 load test (the slice, 4 planets × 4 simulated players, on modest hardware), and one teacher walking through setup.

### 6.2 How option A works

- **Dimension slots.** The mod's built-in datapack declares 8 planet dimensions. Minecraft creates dimensions from data when the world loads, so the count is fixed per world. `planets.count` in config (1–8, default 1) says how many are open. Unopened planets get no players and load no chunks; their cost is small but *to measure* (an empty level still exists in memory).
- **Same seed, same terrain.** All planets use the world seed, so every group starts on the same map. Debriefs can compare "why did group 2's pressure climb faster?"
- **Assignment.** The teacher assigns players to planets in the teacher overview or with `/mars planet assign <player> <n>`. Optional join codes (`/mars join <code>`) let students pick their group themselves. Assignments live in `planets.json` (Mars data), so a rollback never moves a student to another group.
- **Lobby.** Unassigned players wait in a small, safe lobby in the overworld. The fiction for it (e.g. an orbital transfer room) is a Lead Game Designer call.
- **Travel between planets:** none for players. Teachers can visit any planet in spectator mode.
- **Group size:** `group.size` is set **per planet** (3–5) by the teacher, never read from who is online (balance sheet §5).
- **Grade band:** a per-planet override is allowed, so one server can mix a middle school and a high school group (section 8.4).
- **Standard play:** `planets.count = 1`, no lobby, the player spawns straight into `mars:planet_1`.

### 6.3 Server settings for weak hardware (classroom preset)

| Setting | Classroom preset | Why |
|---|---|---|
| View distance | 6 chunks (*to tune*) | Loaded chunks are the biggest server cost per player |
| Simulation distance | 4 chunks (*to tune*) | Limits ticking machines and mobs around each player |
| `fauna.serverCap` | 80 (*to tune*) | Pathfinding cost across all planets |
| Autosave | Default, plus a backup at each session start (section 7) | Bounded data loss |

Numbers marked *to tune* are placeholders until the Stage 3 load test. Hardware facts and school IT findings: [docs/tech/school-deployment.md](docs/tech/school-deployment.md).

---

## 7. Session Control: Pause, Time, Backups, Rollback

Closes consistency review M-05 and M-06, and risks R-10 and R-11.

### 7.1 Simulation time and the sol

- **Simulation time** is a per-planet tick counter in the `session` module. It advances by 1 for each server tick **only while the session is running** (not paused). Server stopped = no ticks = no time.
- **One sol = `pace.solTicks` simulation ticks** (default 24,000 = 20 real minutes at a full 20 ticks per second), matching DESIGN.md and the balance sheet. If the server lags, sols get longer in real time, and so does everything else on the planet, so the balance holds.
- **Every "in-game day" in the design is a sol of simulation time**: hold gates, story delays, prediction timeouts, drift-event spacing, machine wear, fauna raid rates.
- **The daylight cycle is cosmetic.** Sleeping, `/time set`, or a frozen sky never move simulation time. (Recommendation to the Lead Game Designer: the Mars sky follows simulation time so day and sol line up; sleeping should not skip time on Mars.)

### 7.2 Pause and resume

One paused flag for the whole server, stored in `session.json` so it survives restarts. In classroom mode the server **starts paused** (`session.startPaused = true`) and the teacher resumes it at the start of class.

| System | While paused |
|---|---|
| Index simulation, holds, live conditions | No steps. Counters frozen. |
| Story queue, prediction timeouts, drift events | Frozen (they use simulation time) |
| Our machines, hazards, wear, storms, radiation | No effect (they act through the simulation step or simulation time) |
| Vanilla and third-party ticking: mobs, block entities (Mekanism, Create machines), random ticks, weather, daylight | Frozen with the game's built-in tick freeze (`/tick freeze`, Java Edition since 1.20.3). **To verify in a Stage 2 GameTest** that Mekanism and Create machines stop. Fallback if they don't: "pause = stop the server", which the time rules above already handle. |
| Players | Can join, move, look around, read the journal and archive. Can't break, place, or use blocks (events cancelled). Can't take damage. A HUD line says "Colony paused". |
| Learning | Logs `session.paused` and `session.resumed`. No other events are produced. |

**Resume** runs in this order: take the session backup (7.3), start a new session ID, deliver the "Since last shift" recap (9.4), then unfreeze.

**Cost:** pausing is a flag check at the top of each step; zero cost while running.

### 7.3 Backups

- **When:** automatically at every resume (session start), plus on demand (`/mars backup`). The world is paused during the backup, so the copy is consistent.
- **How:** force a full save, turn autosave off, zip the world folder on a background thread, turn autosave back on. Players see "Preparing the colony…". Time depends on world size and disk; *to measure* (likely seconds to a minute for a class world).
- **Where:** `mars-data/<worldId>/backups/<timestamp>_<session>.zip`. The Mars data folder is outside the world, so the action log is never inside a backup.
- **Retention:** keep the last `backup.keep` (default 10) plus the first backup of each day. A one-line notice in the teacher overview shows disk use.

### 7.4 Rollback

- **Command:** `/mars rollback list`, then `/mars rollback <backup> [planet n | all]`.
- **Per-planet rollback** restores that planet's dimension folder (terrain, Index, gates, story queue) and the `playerdata` of the players assigned to it. Other planets are untouched. This works because every piece of planet state lives in the planet's dimension (section 3, Planet scope).
- **How:** a running server can't swap its own files, so the command writes `restore-pending.json`, logs `admin.rollback_requested`, tells players, and stops the server. On the next start, the restore runs **before any level loads**. (Whether this is a NeoForge startup hook or our start script is *to verify in Stage 2*; the start script is the safe default.)
- **Evidence is kept.** The action log and learning records are not touched. The planet's `timeline` number goes up by one, and a `session.rollback` event records the backup's simulation tick. Every log event carries its timeline, so the export can mark events from the lost stretch as `superseded` instead of deleting them. A teacher looking into griefing still has the evidence.
- **Learning records after a rollback:** Colony Record entries already earned **stay earned** (a student showed it; the world being restored doesn't undo that). Pending predictions from the superseded stretch are voided and logged.

---

## 8. Learning Data and Privacy

Closes consistency review M-07, M-08, M-09, M-11, and implements the decision "teacher CSV export uses anonymous IDs only". Risk R-20.

### 8.1 What "anonymous" means here (honest wording)

- On the server's computer, the data is **pseudonymous**: a mapping file links each random ID to a Minecraft account.
- Everything that is exported (CSV) has **only the random IDs**. Outside the server's computer it can't be linked to a student unless someone also takes the mapping file.
- The teacher can delete the mapping (one player, or everyone at the end of the unit). After that, the logs are anonymous even on the server.
- Suggested wording for LEARNING.md and the educator guide: *"pseudonymous on the server, anonymous in every export."* Legal classification (COPPA, FERPA, GDPR) is **unverified**: no legal review yet.

### 8.2 Player IDs

- **Format:** `P-` plus 5 characters from a 31-character alphabet without look-alikes (no 0/O, 1/I/L), e.g. `P-7K3QX`. Easy to read aloud in class, about 28 million possibilities. Unique per world; regenerated on collision.
- **Random, per world.** Created the first time a player joins the world. **Never derived from the Minecraft UUID** (no hashing), so the ID alone reveals nothing.
- **Mapping file:** `mars-data/<worldId>/identity/players.json`: `{ "<minecraft uuid>": { "id": "P-7K3QX", "created": "<date>" } }`. No usernames are stored; names are looked up live from the server when the teacher asks.
- **Rules:** the export code never reads this file; it's excluded from backups; it's never synced to any client. The teacher sees ID ↔ name only in the teacher overview or with `/mars who <id>`, on the server.
- **Deletion:** `/mars forget <player>` removes one entry (that player gets a new ID if they join again); `/mars forget-all` ends the unit.
- **Considered and rejected:** a keyed hash of the UUID (no mapping file). The secret key is just as sensitive as the file, and the teacher can't look up a student who is offline.

### 8.3 Action log

- **Format:** JSON Lines, schema version `v`, one event per line. Example:
  ```json
  {"v":1,"ts":"2026-11-26T14:03:11Z","world":"w-3f9a2c","planet":2,"session":"s-0007","tl":0,"sim":1234500,"sol":51.44,"player":"P-7K3QX","band":"ms","type":"prediction.answered","cause":"player","goals":["B3"],"data":{"prompt":"mars:storm_power","choice":2}}
  ```
- **Event types** (first list; the Lead Instructional Designer decides which ones measure which goal): `session.*`, `gate.reached`, `condition.changed`, `milestone.reached`, `hazard.*`, `failure.reported`, `failure.fixed`, `shiftboard.claimed`, `monitor.viewed`, `prediction.answered`, `prediction.resolved`, `explain.answered`, `journal.opened`, `message.read`, `record.goal_shown`, `report.submitted`, `machine.built`, `machine.removed`, `admin.*`.
- **Not logged:** chat, usernames, UUIDs, IP addresses, coordinates beyond the chunk, anything typed except answers to in-game prompts.
- **Writing:** events are buffered in memory and appended by a background thread every 5 seconds or 200 events. One file per session. Cost: a small allocation per event; disk writes are batched.
- **Group planet note:** gate and planet events have `player: null` and belong to the group; player-caused events carry the player's ID. That gives both group and individual evidence (consistency review M-14).

### 8.4 Grade band

- **Values:** `ms` (middle school, grades 6–8) and `hs` (high school, grades 9–12).
- **Effective band** for a player = server override (classroom config) → else planet override (`planets.json`) → else the player's own choice (learning record) → else the default (`ms`).
- **Content selection:** every journal entry, prompt, message, and quiz item JSON has `"band": "all" | "ms" | "hs"`. Text keys have optional variants: the client looks up `key.hs` or `key.ms` first, then `key`. So a high school "read more" is one extra language key, not a second file.
- The effective band is synced to the player's client and written into every log event (`band`), so the export can split results by band.

### 8.5 Colony Record (Keeper's Record), forecasts, reports

- **Storage:** per player, in `learning/players/<pseudonym>.json`, outside the world: goals shown (with the event that showed each), forecasts and their results, prediction accuracy per gameplay phase, Keeper's Report reflections, Explain-it answers, and the player's own grade band choice.
- **Triggers in data** (`data/mars/learning/record/*.json`): goal ID, player-language text key, and rules (event type, conditions, count). Each rule says `"scope": "player"` (the player's own action) or `"scope": "planet"` (a group achievement credited to everyone on the planet). The Lead Instructional Designer decides the scope of each goal.
- Events with `cause: admin` or `debug` never fill a goal.
- **Client:** the journal screen shows the Keeper's Record; the server sends only the player's own record.
- **Teacher overview:** per planet, each student's goals shown, forecast accuracy by phase, current gameplay phase. Students appear by in-game name for online players (names are already visible in game) and by ID otherwise. Nothing from it is written to disk on the teacher's client.

### 8.6 CSV export

- **Command:** `/mars export [planet n | all]`, or a button in the teacher overview. Writes to `mars-data/<worldId>/exports/<timestamp>/`.
- **Files** (draft columns; the Lead Instructional Designer finalizes the list):

| File | One row per | Draft columns |
|---|---|---|
| `events.csv` | Log event | timestamp, session, planet, timeline, superseded (yes/no), sol, player_id, band, event_type, cause, goal_ids, ngss_codes, detail columns flattened from `data` |
| `players.csv` | Player | player_id, planet, band, sols played, for each goal ID: shown (yes/no) and sol shown, matching NGSS codes, forecast accuracy for gameplay phases 1/2/3, failures reported, failures fixed, Monitor checks before builds |
| `planets.csv` | Planet | planet, group size, gates reached with sol, current phase, hold progress, rollbacks |
| `reflections.csv` | Written answer | player_id, planet, band, report or prompt ID, text. **Separate file**, because free text can contain things students typed about themselves. Teacher can turn it off (`export.includeReflections`). |

- **NGSS codes** come from `data/mars/learning/ngss_map.json` (goal ID + band → codes). The Lead Instructional Designer and SME own its content; codes change there, not in code.
- **IDs only.** No username column exists, and there is no option to add one (user decision).
- **Safety:** UTF-8 with a byte-order mark so spreadsheets open it correctly; any cell starting with `=`, `+`, `-`, or `@` is prefixed with `'` to block spreadsheet formula injection.
- **Cost:** reads the session files once on a background thread; the game keeps running.

---

## 9. Story System

Implements the Lead Game Designer's story-bible handoff: gate-triggered message queue, replayable archive, sol-based delays scaled by classroom mode, "Since last shift" recap, epilogue cards, all as datapack JSON plus language files.

### 9.1 Message data

One JSON file per message in `data/mars/story/messages/` (story beats) or `data/mars/learning/feedback/` (failure reports, prediction results, journal entries, debriefs). Both use the **shared feedback format** in DOCUMENTATION.md §5.1, so the content document maps field for field onto the file:

```json
{
  "id": "mars:beat_10_secret",
  "type": "recorded_message",
  "speaker": "moreno",
  "trigger": { "after": "mars:beat_9_first_thawlings", "delay_sols": 1.0 },
  "classroom": "essential",
  "band": "all",
  "text": { "ms": "story.mars.beat_10_secret.ms", "hs": "story.mars.beat_10_secret.hs" },
  "read_more": "story.mars.beat_10_secret.more",
  "goal_ids": ["D1"],
  "hint_level": null,
  "status": "draft"
}
```

| Shared-format field | Used by the game at runtime? |
|---|---|
| `id`, `type`, `trigger`, `text.ms` / `text.hs`, `hint_level` | Yes |
| `goal_ids` | Yes: copied into the action log event (`goals`) and the export |
| `delivery` | Yes, as `type` + `speaker` |
| `status` | Load check only: a `release` build refuses entries that aren't `final` |
| `instructional_purpose`, `motivational_purpose`, `science_claims`, `fiction_note` | No. Kept in the content documents; allowed in the JSON and ignored by the loader, so one file can carry everything |

- **Triggers:** `gate` (gate reached), `condition` (live condition turns on or off), `milestone`, `first_event` (first failure of a type, first storm), `repeat_event` (same failure again, for escalating `hint_level`), `after` (another message plus a delay), `session_start`, `ending`.
- **`classroom`:** `essential` (delivered with a notification), `optional` (filed in the archive silently), or `merge_into: <id>` (not delivered; its short line is shown inside the named message).
- **Text** is only language keys; the server sends keys and arguments, the client renders them. That keeps translation and grade-band variants in language files. A single key string instead of `{ms, hs}` means the same text for both bands.

### 9.2 Queue and delivery

- **Per planet.** Each planet has a queue of `(messageId, dueSimTick)` and a delivered list, in the planet's saved data. A message is delivered **once per planet**.
- **Delays** are in sols of simulation time, multiplied by `story.delayScale` (standard 1.0; classroom default 0.5, so a 1-sol delay lands in about 10 minutes of play and stays inside one class period). Paused time doesn't count.
- **Delivery:** due messages are appended to the planet's archive and pushed to every **online player on that planet**. Players who are offline see them as unread the next time they join (per-player read state on the player).
- **Set phase:** the essential messages of the skipped phases are filed in the archive as one "Unread from earlier" bundle (story bible §7.2).
- **Cost:** a sorted queue per planet; each step checks only the head. Effectively zero.

### 9.3 Archive

- Lives in the journal. Every delivered message can be replayed. Optional messages and world traces are listed too.
- The client gets the planet's delivered-message list when the player joins and each new message as it arrives; text comes from the client's language files.

### 9.4 "Since last shift" recap

- **When:** at every resume, to everyone on each planet; and when a player joins after missing simulation time (their last-seen sim tick is older than the planet's last resume).
- **What:** 2–3 lines in BELL's voice built from templates in `data/mars/story/recap/`: gates or milestones reached since then, the largest Index change, open failures on the Shift Board. Chosen by a fixed priority order, so the recap is predictable and testable.

### 9.5 Epilogue cards

- Data in `data/mars/story/epilogue/`: each card has a condition (same condition language as gates, plus named stats like `group_s_population`, `biome_count`, `sols_held`, O2 margin) and a priority. At the beacon, the highest-priority card in each category is shown, with numbers filled in from the planet's snapshot.
- **State the cards read** (per planet, in the planet's saved data, so it rolls back with the planet): Group S population (2.4), count of settled thawling colonies, biome count (self-sustaining biomes), sols held in equilibrium, O2 and CO2 margins from the snapshot, thawlings killed, raids that reached a quiet zone. Each is a running counter updated by events, not a world scan at the end, so the ending costs nothing extra.
- **No ranking or score** anywhere in the data format (story bible §6.5, DESIGN.md).
- The final reflection answer goes to the learning record and the `reflections.csv` export.

---

## 10. Admin API, Teacher Commands, Debug Commands

Closes consistency review M-10.

```java
// Only the admin module may use this (enforced by an ArchUnit test). Every call is logged
// as an admin.* event and posts the normal events with Cause.ADMIN or Cause.DEBUG.
interface AdminControl {
    void setPhase(PlanetId planet, PhaseId phase, PlayerRef teacher);     // teacher
    void pause(PlayerRef teacher);
    void resume(PlayerRef teacher);
    void setValue(PlanetId planet, ParameterId p, double value, PlayerRef who);   // debug
    void latchGate(PlanetId planet, GateId gate, PlayerRef who);                  // debug
    void unlatchGate(PlanetId planet, GateId gate, PlayerRef who);                // debug only
    void stepSimulation(PlanetId planet, int steps, PlayerRef who);               // debug
}
```

- **Set phase** latches every gate up to that gameplay phase, sets the Index to the phase's start values (from `data/mars/phases/<phase>.json`), and files the skipped essential messages (9.2). Unlocks happen through the normal `GateReachedEvent`, so recipes and journal entries follow without special cases.
- **No credit for admin changes.** Events carry `Cause.ADMIN` or `Cause.DEBUG`; the Colony Record ignores them (8.5).
- **Teacher commands** (`mars.teacher`): `pause`, `resume`, `backup`, `rollback`, `planet assign`, `phase set`, `band set`, `export`, `who`, `forget`. All are also buttons in the teacher overview, so no teacher needs to type commands or edit config files (risk R-18).
- **Debug commands** (`mars.debug`, and only when `debug.enabled = true`): `set`, `latch`, `unlatch`, `step`. Used for the Stage 2 debug requirement (set every Index value).

---

## 11. LMS Integration (after release, opt-in only)

**Decision being implemented (user, 2026-11-26):** LMS integration comes after release and is opt-in. Nothing is built for it before release. This section records the design so the pre-release code doesn't block it.

**Rules that never change:** off by default; no student data leaves the server unless the teacher turns it on for that world; only pseudonymous IDs ever leave; a separate privacy review happens before it is built (risk R-22).

| Standard | What it is | Fit for us | Source |
|---|---|---|---|
| **xAPI 2.0** | Learning event statements ("actor, verb, object") sent to a Learning Record Store (LRS). Published as **IEEE 9274.1.1-2023**. | **Good.** Our action log is already event statements. An exporter maps each event to an xAPI statement with the pseudonym as an account name, e.g. `{"account": {"homePage": "urn:mars:<worldId>", "name": "P-7K3QX"}}`. | ADL news, 2023-11-22 ([link](https://adlnet.gov/news/2023/11/22/xAPI-Officially-Becomes-a-Published-IEEE-Standard)); IEEE record ([link](https://standards.ieee.org/ieee/9274.1.1/7321)) |
| **LTI 1.3 (1EdTech)** | Lets an LMS launch a web tool and receive grades | **Poor.** It assumes a web app launched from the LMS. A Minecraft client isn't one. Would need a companion web service, which we don't want. | 1EdTech LTI Core v1.3.1, named in an xAPI resource list; **details unverified** |

**Planned path (post-release):**
1. **File export first:** an xAPI JSON file the teacher downloads and imports into the school's LRS by hand. The teacher decides what leaves.
2. **Optional live sending:** only to an LRS address the school configures, with credentials the school provides, behind a world-level opt-in that the teacher turns on. Only pseudonymous statements. No free text by default.
3. **No LTI** unless a school need appears that xAPI can't meet.

The **export interface** (section 5) is the only hook needed now.

---

## 12. Performance Budget

Classrooms run on weak hardware (risk R-08). Costs per system, on the server:

| System | When it runs | Cost |
|---|---|---|
| Index step | Every 100 ticks per open planet, unpaused | Sum of cached rates (one add per contribution) + a few coupling formulas. Microseconds per planet. |
| Gates, holds, live conditions, milestones | Each Index step | A few dozen comparisons per planet |
| Story queue | Each Index step | One comparison (queue head) |
| Pause check | Top of each step | One flag read |
| Sealing flood fill | On block change near a sealed volume | Up to the volume cap, spread over several ticks |
| Fauna spawn weight | Vanilla spawn cycle | A few chunk lookups in the per-chunk index |
| Terrain random ticks (ice, soil) | Vanilla random ticks | One read of the cached per-chunk heat map per tick on our blocks (2.5) |
| Chunk heat map, fauna pressure F, ending-card counters | When a machine changes state, or on the counted event | Recompute ~9 chunks or one running sum; no ticking |
| Action log | Per event (buffer) + every 5 s (disk) | Small allocation; batched writes on a background thread |
| Backups, rollback, export | Session start / on command | Background thread or server stopped; never during play |
| Client sky and fog | Per frame (client) | Interpolation between cached keyframes; no lookups |

The large costs are vanilla and third-party: loaded chunks, mobs, and Mekanism/Create machines. Those are controlled by view and simulation distance, fauna caps, and recipe gating (section 6.3). A **load test** in Stage 3 (four planets, four simulated players each, on modest hardware) replaces the *to tune* numbers with measured ones.

---

## Open Questions

| Question | Owner | Status |
|---|---|---|
| Fixed simulation step size: is 5 seconds right, or should it be configurable? | Technical Lead | Keep 5 s; make it a config value hidden from teachers. Revisit after the Stage 2 prototype. |
| Do we need a public API for other mods at launch? | Technical Lead | No. Internal API only at launch; the contribution API is designed to be opened later. |
| Build tooling: NeoForge ModDevGradle with Gradle; a mono-repo with the pack and the mod | Technical Lead | Assumed; confirm when the Stage 1 skeleton builds |
| Does `/tick freeze` stop Mekanism and Create machines? | Technical Lead | Stage 2 GameTest; fallback "pause = stop the server" |
| Rollback restore: NeoForge startup hook or start script? | Technical Lead | Stage 2; start script is the safe default |
| Hosting option A vs. B for classrooms | Producer to route (ill-structured) | Recommendation A, fallback B (section 6.1) |
| Memory and CPU per open planet; view-distance and fauna caps | Technical Lead | Stage 3 load test |
| Event list and CSV columns for each goal ID | Lead Instructional Designer | Draft in section 8 |
| Minecraft version: stay on 1.21.1? | Technical Lead, Stage 1 | Recommended; evidence in [docs/tech/mod-licenses.md](docs/tech/mod-licenses.md) |
