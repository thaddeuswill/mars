# Software Architecture

This file covers how the software is built. It addresses five practices:

1. **Architecture:** the components and how they interact
2. **Algorithms:** the logic behind the key systems
3. **Coding practices:** shared style and conventions
4. **Elegant design:** clean responsibilities, no hidden side effects
5. **Future enhancements:** hooks so new features don't need rewrites

Sections 6–12 cover the classroom and data systems: group planets and hosting, session control, learning data and privacy, the story system, the admin API, LMS integration, embedded assessment (11a), and the performance budget.

> **Draft, owned by the Technical Lead.** No code exists yet. This will be revised during the Stage 2 prototype ([ROADMAP.md](ROADMAP.md)). Supporting notes: [docs/tech/](docs/tech/) (mod licenses, school deployment).
>
> **Version settled (user decision, 2026-11-26): Minecraft 1.21.1, NeoForge 21.1.x, Java 21.** See section 3.1 for the decision record.
>
> **Still pending Stage 1** (consistency review m-19): whether oxygen and sealing are custom or come from a standalone mod. Everything below assumes a custom sealing system, marked where it matters.
>
> **Round 2 additions (2026-11-26):** signals (2.5.1), the keyframe input map (2.5.2), clouds and heat shimmer (2.5.3–2.5.4), the forecast lifecycle (2.6.1), Shift Board actors (2.8), Monitor disclosure and next-gate display (2.9), feedback fading (2.10), weather mast (2.11), sound system (2.12), entity animation (2.13), repository and asset layout (3.2), name scrubbing (8.7), final export columns (8.6), embedded assessment feasibility (11a), interruption control (9.6).
>
> **Stage 1 build (2026-11-26):** code now exists in `mod/` (see [docs/tech/launch-guide.md](docs/tech/launch-guide.md)). The pure core in `mars.index.sim` and `mars.dimension.ice` matches 2.1 and 2.5; nothing else is wired to the world yet. Layout differences from the plan: `mod/` is a standalone Gradle build with its own wrapper; no Parchment mappings (its Maven host is blocked here); the ice rule lives in `data/mars/terrain/`.
>
> **Batch 2 machines (BACKLOG #26):** six machine blocks registered (`mars:regolith_outgasser`, `regolith_oven`, `solar_panel`, `battery`, `oxygen_maker`, `planetary_monitor`) with facing, simple model-independent shapes, items, and a creative tab; panel dust state and tuning data (2.17).
>
> **Round 3 additions (2026-11-26):** owner decisions applied (repairer gets Shift Board credit, `guidance` setting, orbital mirror, Mekanism and Create optional and late, CI at Stage 1 exit, offline LMS converter in Stage 5); `mars:solar_spike` (2.5.1); forecast tags, lock points, and `reveals` (2.6.2); time on Mars (2.14); third-party mods (2.15); machine framework and cost (2.16); Training Hall (6.2); export and teacher-overview additions (8.5, 8.6); rechecks with the network open (mod-licenses.md, school-deployment.md). Server and teacher setup: [docs/tech/server-setup.md](docs/tech/server-setup.md). Test strategy: [docs/tech/test-strategy.md](docs/tech/test-strategy.md). Build plan: [docs/tech/build-plan.md](docs/tech/build-plan.md).

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
    double get(ParameterId parameter);              // stored, derived, or signal (e.g. mars:o2_partial)
    IndexSnapshot snapshot();                       // immutable copy for readers; includes signals
    ContributionHandle contribute(ParameterId parameter, double ratePerSol, SourceId source);
    SignalHandle publish(SignalId signal, SourceId source);   // section 2.5.1
}

// Machines hold a handle and update or remove it. They never write values directly.
interface ContributionHandle {
    void setRate(double ratePerSol);
    void remove();
}

// A system module (hazards, fauna) publishes a read-only world-state value into the snapshot.
// Signals are not accumulated or coupled; they are set, not summed. One owner per signal.
interface SignalHandle {
    void set(double value);
}

// Replaces current player names in free text with pseudonyms (section 8.7). Implemented
// inside learning's identity component; callers never see the mapping itself.
interface NameScrubber {
    ScrubResult scrub(PlanetId planet, String text);   // text with names replaced, count replaced
}

// Per-player feedback fading (section 2.10). Implemented by learning; read by story when
// it renders a message for one recipient.
interface FeedbackState {
    int hintLevel(PlayerRef player, FeedbackKey key);   // 1..3
    boolean tipRetired(PlayerRef player, FeedbackKey key);
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

**Service interfaces** (`NameScrubber`, `FeedbackState`, `Planets`, `Gates`, `SimClock`) are defined in `core` and registered once at startup in a `core` service registry by the module that implements them. A module that needs one asks the registry for the interface, never for the implementing class, so the "talk only through `core`" rule (1.2) still holds and the ArchUnit test still passes.

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
| Per-chunk machine heat (2.5) | NeoForge data attachment on the chunk, synced to players tracking the chunk | World: region files | Yes |
| Machine IDs (`m-` + 6 characters, random) and last actor per machine | Block entity data | World: region files | Yes |
| Per-player feedback fading state (2.10) | Part of the learning record | Mars data: `learning/players/<pseudonym>.json` | **No** (what a player has learned isn't undone by a rollback) |
| Monitor pages, next-gate display order, signals, storm caps | JSON | Datapack: `data/mars/monitor/`, `data/mars/signals/`, `data/mars/hazards/` | n/a (content) |
| Sky, fog, cloud, and instrument keyframes (2.5.2) | JSON | Resource pack: `assets/mars/keyframes/` | n/a (client content) |
| Sound events and subtitles (2.12) | `sounds.json` + Ogg Vorbis files + language keys | Resource pack: `assets/mars/sounds.json`, `assets/mars/sounds/` | n/a |
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
  5. Recompute **derived values** (O2 partial pressure = pressure × O2 fraction; CO2 fraction = CO2 partial pressure ÷ pressure; boiling point of water at the current pressure, from the Antoine equation with coefficients in data, valid from the triple point up). CO2 is **stored** as a partial pressure (kPa) and O2 as a fraction (%), matching DESIGN.md §1; the other form of each is derived. Derived values are defined in data and are read-only. They are part of the synced snapshot, so the Planetary Monitor shows them like any other value (DESIGN.md §1).
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
- **Ice rule (updated 2026-11-26 from the SME's round 2 review, `docs/science/round2-review.md` §1.1).** Each warmed ice block compares its **local temperature** with `mars:boiling_point` (the boiling point of water at the planet's current pressure, already a derived value, 2.1) instead of using fixed pressure bands only:

  | Pressure | Local temperature | Server result |
  |---|---|---|
  | < 0.611 kPa | > 0 °C | Sublimate: shrink the ice, no water |
  | ≥ 0.611 kPa | ≥ `mars:boiling_point` | Melt and boil away at once (flash puddle, then gone) |
  | 0.611–5 kPa | 0 °C to boiling point | Short-lived puddle that evaporates after a balance-sheet time and refreezes when cold |
  | ≥ 5 kPa | 0 °C to boiling point | Water stays |
  | any | ≤ 0 °C | Stays ice |

  The rule table is data (`data/mars/terrain/ice_rule.json`; **moved in Stage 1** from `data/mars/dimension/`, because `data/<namespace>/dimension/` is Minecraft's own dimension registry folder and any JSON there is loaded as a dimension, thresholds as plain numbers and the name of the derived value), so the SME's or Lead Game Designer's later changes need no code. The forecast content check (CR-F1) runs against this same table.

- **Local temperature** = global mean + day/night offset + machine heat at that block.
  - **Why this matters more now:** with fixed bands, local temperature only had to answer "above 0 °C?". Against the boiling point it has to answer "how far above 0 °C?" (2 °C at 0.7 kPa, 33 °C at 5 kPa), so ice right next to a heater should boil while ice a few blocks away only puddles. A heat value that is uniform across a 16×16 chunk would give every ice block in the chunk the same result and lose that gradient.

  | Approach | Cost per ice random tick | Notes |
  |---|---|---|
  | Per-block, exact (sum heat from every machine within range, with falloff) | ~9 chunk lookups plus a loop over their machines: tens to hundreds of operations | A player's area gets a few thousand random ticks per tick (simulation distance 4, vanilla's 3 random ticks per section); if ~10 % land on ice, several hundred lookups per tick per player. *Estimate, not measured.* Grows with players × machines |
  | Cached per-chunk heat, uniform in the chunk | One map read | Cheapest, but loses the near/far gradient the new rule needs. **No longer recommended alone** |
  | **Cached per-chunk heat sources with falloff (recommended)** | One map read + one distance calculation for each of at most 2 sources: about 10 arithmetic operations, O(1) | Each chunk caches the **two strongest heat sources** that reach it (position, heat in °C at the source, range), recomputed for the ~9 chunks around a machine only when it is built, removed, switched, or shrouded. Local heat = sum over the cached sources of `heat × max(0, 1 − distance / range)`. Then one comparison with `mars:boiling_point` (read once per simulation step, not per block) |

  **Cost of the new comparison itself:** one subtraction and two comparisons per ice random tick. The boiling point is computed once per planet per simulation step (Antoine equation, 2.1), not per block. **Total added cost over the old plan:** a few floating-point operations per ice random tick; still far under a millisecond per player per tick (*estimate, to confirm in the Stage 3 load test*).

- **Chunk heat reaches the client** (the two cached sources per chunk, so the client can show the same near/far gradient for the frost-free ring, wisps, and shimmer) as a chunk data attachment with sync (the 1.21.1 NeoForge branch has `AttachmentType.Builder#sync`: [source](https://raw.githubusercontent.com/neoforged/NeoForge/1.21.1/src/main/java/net/neoforged/neoforge/attachment/AttachmentType.java); but the official 1.21.1 docs still say chunk attachments must be synced by sending your own packet, using `ChunkWatchEvent.Sent` ([docs](https://docs.neoforged.net/docs/1.21.1/datastorage/attachments)), and the earliest 21.1.x build with built-in sync couldn't be found: NeoForge's Maven and GitHub history were blocked in round 3). **Decision: use the documented path**, a custom payload sent on `ChunkWatchEvent.Sent` and on change; switch to built-in sync only if the pinned build compiles against it. Cost: two small records (position, heat, range) per chunk, sent only when a machine changes state.

#### 2.5.1 Signals: world-state values that aren't Index parameters

Some visuals and conditions need values that the Index doesn't accumulate: storm strength, the radiation reaching the surface, fauna pressure. Each is published by the module that owns it as a **signal** (`PlanetaryIndex.publish`, section 1.3). Signals:
- sit in the same `IndexSnapshot` as parameters, so the client, the Monitor, keyframes, and the condition language read them the same way;
- have exactly one owner and are **set**, not summed. Coupling rules may read them (e.g. solar output reads storm strength); nothing else writes them;
- are defined in `data/mars/signals/*.json` (ID, unit, range, owner module), so a typo in a keyframe file fails at load with the file and field named.

This keeps one source of truth: the number that hurts the player is the number the sky shows. When a visual must show something *different* from the harm (the aurora shows the solar event itself, which doesn't weaken with pressure), the owner module publishes a second signal from the same event (`mars:solar_spike`) rather than the client re-deriving it. Example: the hazards module computes `mars:surface_radiation` once, applies the dose from it, and publishes it; the client draws the radiation glow from the same value.

#### 2.5.2 Keyframe inputs: art names → data keys

Answers the Lead Artist's handoff (planetary keyframes §1.2a). Units live in each parameter's definition, not in its ID.

| Art input (keyframes §1.2a) | Data key | Kind | Unit, range | Who computes it | Notes |
|---|---|---|---|---|---|
| Pressure | `mars:pressure` | Stored parameter | kPa | `index` | Use `"axis": "log10"` |
| Temperature (global mean) | `mars:temperature` | Stored parameter | °C | `index` | |
| Hydrosphere | `mars:hydrosphere` | Stored parameter | % of surface | `index` | |
| O2 fraction | `mars:o2_fraction` | Stored parameter | % (0–100, not 0–1) | `index` | |
| CO2 partial pressure | `mars:co2_partial` | Stored parameter | kPa | `index` | |
| Biomass | `mars:biomass` | Stored parameter | BU | `index` | |
| O2 partial pressure | `mars:o2_partial` | Derived | kPa | `index` | Weather-mast O2 tube; Air window |
| CO2 fraction | `mars:co2_fraction` | Derived | % | `index` | Not used by art; Monitor "read more" |
| Boiling point of water | `mars:boiling_point` | Derived, **synced to the client in the snapshot** | °C | `index` | Drives the server ice rule (2.5) **and** the client vapor wisps: heavy wisps where local temperature ≥ boiling point, light wisps between 0 °C and it (SME round 2, item 16). Monitor boiling-point line |
| **Storm strength** | `mars:storm_strength` | **Signal** | 0–1 | `hazards` | See below. Client smoothing **off** (the server envelope already ramps) |
| Radiation spike × pressure scale | `mars:surface_radiation` | Signal | 0–1 (relative to a full spike at 0.6 kPa) | `hazards` | Already includes the pressure attenuation; art doesn't multiply again |
| Solar event strength at the top of the atmosphere (aurora glow) | `mars:solar_spike` | Signal | 0–1 (envelope of the current solar event, **no** pressure attenuation) | `hazards` | Added round 3 for the Lead Artist: the aurora must **not** fade as the air thickens, so it can't use `mars:surface_radiation`. The hazards module publishes both from the same event, so they can't disagree about timing |
| Fauna pressure | `mars:fauna_pressure` | Signal | F (balance sheet §6) | `fauna` | Thawling frills, settling |
| Breathable condition | `mars:breathable_air` | Live condition (boolean) | on/off | `gates` | Helmet, visor, Air window dot. The three parts are `mars:pressure`, `mars:o2_partial`, `mars:co2_partial` |
| Local temperature | `local_temperature` (client-computed, not a key) | Derived on the client | °C | `client` | `mars:temperature` + day/night offset (from data, by time of sol) + falloff heat from the chunk's cached sources (`mars:chunk_heat`). Same formula as the server, shared code in `core` |
| Machine heat in a chunk | `mars:chunk_heat` | Chunk attachment: up to 2 heat sources (position, heat, range) | °C | `machines` | 2.5 above |
| Ice state (sublimate, boil away, short-lived puddle, pool) | none: these are **server block changes** | Server rule table comparing local temperature with `mars:boiling_point` (2.5) | — | `dimension` | Not client keyframes. The client just draws the block the server placed |
| Machine and flora output (bubbles), scrubber cumulative output (deposits) | Block entity fields | Local | — | `machines` | Synced with the block entity's update packet |
| Seeded flora nearby, quiet-zone state | Blocks and block entities | Local | — | `dimension`, `fauna` | |

**Storm strength.** `storm_strength = envelope(t) × intensity × cap`. The `hazards` module computes it each simulation step from the active storm. `envelope` ramps in over 0.05 sol (60 s) and out over 0.075 sol (90 s) of **simulation time**, matching the art's ramp, so the storm freezes with the pause. `cap` comes from `data/mars/hazards/dust_storm.json`, keyed on latched gates (Phase 1: 1.0, Phase 2: 0.8, Phase 3: 0.5, Equilibrium: 0.25, the art's values). Whether the cap also scales the storm's **gameplay** effects (solar cut, wear), or only its look, is a balance question for the Lead Game Designer (handoff). My recommendation: scale both, so "storms weaken" is true in the mechanics and not only in the sky.

**Keyframe file schema** (accepts the Lead Artist's proposed shape, keyframes §1.3, with three additions):

```json
{
  "input": "mars:pressure",
  "axis": "log10",
  "space": "oklab",
  "smoothing_s": 30,
  "keys": [ { "at": 0.6, "sky_zenith": "#A87B5A", "fog_start": 0.70 }, { "at": 75.0, "sky_zenith": "#4A7AC2" } ],
  "overlay": { "input": "mars:storm_strength", "smoothing_s": 0, "mix": "toward", "targets": { "sky_zenith": "#9A6A48" }, "max_mix": 0.85 }
}
```

- `input` must be a registered parameter, derived value, or signal; checked at resource load.
- `smoothing_s` per file (default 30 s, the art's time constant; 0 for signals that already ramp).
- `overlay` covers the storm layer mixing on top of the clear-weather sky (keyframes §3.7).
- **Cost (client):** per frame, a dozen float lerps and one OKLab conversion per colour channel in use (about 10 colours). Microseconds. Keyframe lookups happen only when the smoothed value changes by more than a small step.

#### 2.5.5 Sky keyframe data (built in Stage 2 slice 6)

File `pack/assets/mars/keyframes/sky.json` (an **asset**, not a datapack file: the client needs it and a dedicated server does not; it ships in the same jar). Values come from `docs/art/planetary-keyframes.md` 3.1 (Lead Artist, fluid). Pure classes in `mars.visual` (`SkyKeyframes`, `Oklab`, `Smoothing`, `SkyState`); client drawing in `mars.client.sky`.

| Field | Meaning |
|---|---|
| `input`, `axis`, `space` | Must be `"mars:pressure"`, `"log10"`, `"oklab"` in slice 6 (other inputs, such as storm strength, are later) |
| `smoothing_s` | Time constant of the shown pressure (30 s, artist). Dev override: client system property `mars.sky.smoothing_s` (used only by the screenshot run) |
| `fog_end_floor_blocks` | The fog end never drops below this many blocks (48) |
| `sun_scale` | Sun quad size relative to vanilla (0.66) |
| `dome_gradient_exponent` | Horizon-to-zenith blend, `t = (elevation / 90 deg) ^ exponent`; **invented** (0.6) |
| `keys[]` | Strictly increasing `at` (kPa, above 0), each with `sky_zenith`, `sky_horizon`, `fog`, `sunset_halo`, `sunset_band`, `night` (`#RRGGBB`), `fog_start`, `fog_end` (fractions of render distance, `0 <= start < end <= 1`) |

Rules: interpolate on log10(pressure); mix colours in OKLab; fractions linearly; clamp outside the range; a bad file is refused with the field named (`SkyKeyframes.parse`), the boot check `sky.keyframes_load` fails, and on a client the vanilla sky stays and an error is logged.

Flow: the server sends `mars:sky` (`SkyPayload`: **one double, the planet's pressure in kPa**, nothing else) to players on a planet on login, respawn, arrival and at most once per second when it moved by more than 0.1 % (`SkySync`). The client `SkyState` snaps to the first value, then follows later values with the time constant. Per client tick: one exponential step and, when the value moved, one keyframe lookup. Hooks: `RegisterDimensionSpecialEffectsEvent` (effects id `mars:mars`, named by `dimension_type/mars.json`; `MarsSkyEffects.renderSky` draws the dome, sunrise glow, sun, moon, stars), `ViewportEvent.ComputeFogColor` and `ViewportEvent.RenderFog` (terrain fog only, cancelled so NeoForge applies the new distances; skipped under water, lava, powder snow, blindness, darkness). Dedicated-server safety: the client registration is called only when `FMLEnvironment.dist.isClient()`, the payload handler is a lambda, ArchUnit keeps `net.minecraft.client` inside `mars.client`.

#### 2.5.3 Clouds (asset E-10): cost of a custom cloud renderer

NeoForge 1.21.1 lets a dimension replace cloud rendering: `IDimensionSpecialEffectsExtension.renderClouds(...)` returns true to skip vanilla clouds ([source, 1.21.1 branch](https://raw.githubusercontent.com/neoforged/NeoForge/1.21.1/src/main/java/net/neoforged/neoforge/client/extensions/IDimensionSpecialEffectsExtension.java)). The same interface has `renderSky` and `renderSnowAndRain`, which the Mars sky and rain need anyway.

| Option | How | Client cost | Build cost (estimate) | Notes |
|---|---|---|---|---|
| **A. Vanilla-style clouds, our texture, mesh rebuilt on coverage change (recommended)** | Copy the vanilla cloud approach (a mesh built from a 256×256 map), but the map stores **density** in grey levels. When `mars:hydrosphere` crosses a coverage step (5 steps: 5/8/20/35/45 %), rebuild the mesh keeping only cells above the step's threshold. Colour, opacity, and height come from keyframes each frame | Same as vanilla clouds per frame. A mesh rebuild of a few ms, only when coverage changes a step (a handful of times per playthrough) | ~3–5 developer days | Respects the player's cloud setting (off / fast / fancy), so weak PCs can turn clouds off like in vanilla. No shader. |
| B. Swap between 5 pre-drawn cloud maps | Same renderer, a different texture per step | Same as A | ~2–4 days | More art (5 maps), harder transitions; fine if A's density map looks wrong |
| C. Volumetric or shader clouds | Custom shader | Much higher; risky on integrated GPUs | Weeks | Not recommended |

**Decision (Technical Lead, fluid):** option A. E-10 is **unblocked**: the artist delivers one 256×256 greyscale density map (`environment/clouds_density.png`) where brighter = appears at lower coverage. Compatibility with Sodium for NeoForge (which replaces parts of the renderer) is **to test in Stage 2**; Sodium is under the PolyForm Shield 1.0.0 licence ([LICENSE, 1.21.1/stable](https://raw.githubusercontent.com/CaffeineMC/sodium/1.21.1/stable/LICENSE.md)) and isn't yet in our stack (see 12).

#### 2.5.4 Heat shimmer (asset E-15): shader vs. particle fallback

| Option | How | Cost on school hardware | Risk |
|---|---|---|---|
| Post-process distortion shader | Render shimmer volumes into a mask, then a full-screen distortion pass | At least one extra full-screen pass every frame (about 1 M pixels at 1366×768) even when only one machine is on screen. On integrated GPUs this is plausibly 0.5–2 ms per frame (**estimate, not measured**), on machines that may already be near 30 FPS | Conflicts with shader packs (Iris and similar) and "Fabulous" graphics; one more thing to break per Minecraft update |
| **Particle fallback (recommended, default and only shipped path for Stages 3–4)** | The art's 8×8 wavy particle (12 % alpha, rising), emitted by running heat sources | A few particles per visible running machine; particle cost is bounded by vanilla's particle settings | None beyond vanilla particles |

**Decision (Technical Lead, fluid):** ship the particle version. The shimmer **carries meaning** (machine is hot; thawlings come to heat; a shrouded machine shows none: B5, keyframes §3.8), so it must look the same for every player. A shader that some players turn off would hide a learning cue from them. A shader may be added in Stage 5 polish as an **optional extra on top of** the particles, never instead of them. E-15 is **unblocked**: build the particle.

### 2.6 Learning
- Prediction prompts (forecasts): lifecycle in 2.6.1.
- Colony Record: each learning goal is a set of trigger rules (in data) matched against events on the bus. Details in section 8.5.
- Action log: events buffered in memory and written to disk in batches on a background thread (section 8.3).

#### 2.6.1 Forecast lifecycle and results

Answers the Lead Instructional Designer's handoff (LEARNING.md 3.2, 5.5: results `expired` and `late`). Each forecast JSON (`data/mars/learning/forecasts/`) has an `issue` trigger, an `outcome_visible` condition (when the player could see the answer in the world, e.g. the storm arrives), a `resolve` rule (which choice is correct), and a `timeout_sols`.

Every **(player, forecast) pair ends with exactly one** `prediction.resolved` event, so counts never double up.

| Step | When | Logged |
|---|---|---|
| Issued | The trigger fires for the planet. Online players on the planet get it now; a player who joins later gets it on join **only if** the outcome isn't visible yet | `prediction.issued` (player). A player who never received it has no events for it: absence isn't "expired" |
| Answered | Player submits before `outcome_visible` | `prediction.answered` (player, choice; HS "why" choice separately) |
| Outcome visible | `outcome_visible` becomes true on the planet. Open prompts close on every client ("The outcome is in: result at your next pause") | `prediction.outcome_visible` (planet) |
| Resolved | At outcome visible + `forecast.lateWindowSols` (default 0.05 sol, 1 minute) | One `prediction.resolved` per issued player with `result`: |

| `result` | Meaning | Scored for B3? |
|---|---|---|
| `correct` / `incorrect` | Answered before the outcome was visible | Yes |
| `late` | Answer arrived after the outcome was visible but inside the late window (a prompt that was open when the storm hit; network race) | No |
| `expired` | Issued, never answered by the end of the late window | No |
| `void` | The outcome never happened before `timeout_sols`, the forecast was skipped by "set phase", or it belongs to a rollback's superseded stretch | No, and not counted as expired |

The result text itself is delivered at the next natural pause (TEAM.md 8.6), first in the queue, independent of when it was resolved.

#### 2.6.2 Forecast tags, lock points, and `reveals` (round 3)

Answers the Lead Instructional Designer's round 2 handoffs (LEARNING.md 2.3, 3.10, 4.8; content rule CR-F9).

| Field or rule | Where | Effect |
|---|---|---|
| `interleaved: true`, `source_goal` | Forecast JSON (FI-1 … FI-4) | Copied into `prediction.*` events; feeds `forecasts_interleaved_*`; a correct FI also fills the source goal's Record page |
| `scaffold` | Event field, set by the server | `none` or `pointer` when the "Check: …" pointer was shown (pointer returns below 1 in 3 accuracy after 3 forecasts in the phase). Feeds `forecasts_scaffolded`; B3 analysis can separate scaffolded answers |
| `replay` | `prediction.resolved` result | A forecast issued again **after a rollback** to a player who had it issued before in the superseded stretch. Shown and explained; **not scored**, not counted as expired. The check is one lookup in the player's learning record (which rollback never touches) |
| `answer_time_ms` | `prediction.answered` | Time from the card opening to the answer; answers under ~2 s are flagged as possible guesses (formative only) |
| **Lock point** | Forecast JSON `lock` (a condition, e.g. `sim_before_outcome: 0.25` sol, or `monitor_opened`) | The forecast closes for that player when the lock fires; afterwards it resolves as in 2.6.1 (`expired` if unanswered) |
| **`reveals`** | Message JSON: list of forecast IDs the message would give away | The interruption gate (9.6) **holds** a message for a player while any forecast it `reveals` is still open for that player; the message is delivered right after the player answers or the forecast locks. Nothing is dropped. CR-F9 checks it in the solver |
| **Forecast card first on Monitor open** | Client + server | If a player with an open forecast whose lock is `monitor_opened` (F3, F5, F8) opens the Monitor, the forecast card shows first ("Log your forecast before you look"). Answering or pressing "Skip" opens the Monitor. Skipping closes the forecast as `expired`. So checking data (C3) never silently costs the prediction (B3) |

**F4a outcome check** (Lead Instructional Designer and Lead Game Designer, round 2): F4a is keyed "the ice leaves no water". Its `outcome_check` is: **no liquid water block at the ice block's position 0.25 sol after the ice block changes state.** That is true for sublimation, boiling away, and fast evaporation, so it holds under the ice rule (2.5). It depends on one invented balance number: the puddle-drying time below 5 kPa must be under 0.25 sol (now 0.1 sol, DESIGN.md threshold-vs-curve table). A content test fails the build if that number ever reaches 0.25.
- **Implementation:** when the ice rule changes an ice block that a player's open F4a is attached to, the server schedules one check at +0.25 sol of simulation time (a queue entry, like story delays). Cost: one block read.

### 2.7 Story delivery
- A per-planet queue of scheduled messages, checked each simulation step. Details in section 9.

### 2.8 Shift Board (group coordination block)
- A block in the `machines` module. It lists the planet's **open failure reports** (from `failure.reported` events) and who has claimed each.
- **State:** a per-planet list of `(failureId, claimedBy, status)` in the planet's saved data. Any Shift Board on the planet shows the same list.
- **Names:** the screen shows **in-game names** of claimers (already visible in game). Nothing is stored by name: the claim holds a `PlayerRef` in memory and saved data, and the action log records pseudonyms only.
- **Failures are tied to a system.** Every machine gets a random machine ID (`m-` + 6 characters) when placed, never its coordinates (the log keeps nothing finer than a chunk). A failure record is `(failureId, machineId, failureType, cause, phase)`.
- **Who is the actor** (answers the Lead Instructional Designer's handoff, LEARNING.md alignment row C2). The repair is completed by a player interaction on the server (e.g. using a maintenance part), so the server knows exactly who did it.

| Event | `player` (actor) | `data` |
|---|---|---|
| `failure.reported` | none (planet event, `scope: planet`) | failure_id, machine_id, failure_type, cause, phase |
| `shiftboard.claimed` | the claimer | failure_id, failure_type |
| `shiftboard.released` | the claimer who let go | failure_id |
| `failure.fixed` | **the player who did the repair** | failure_id, machine_id, cause, `claimed_by` (pseudonym or empty), `fixed_by_claimer` (yes/no), `credit` (pseudonym) |
| `failure.repeat` | none (planet event) | failure_id, machine_id, cause, phase, `prior_credit` (pseudonym credited for the earlier fix of the same cause on the same machine in this phase) |

- **Credit rule (decided, owner, round 3): the player who did the repair (`actor`).** `shiftboard.creditRule = actor`. A claimer who didn't do the repair gets no C2 credit; diagnosing the cause still shows C2 through "What caused it?". Claimer and actor are both still logged, so the evidence stays readable whatever the rule.
- `repeat_failures_after_own_fix` in the export counts `failure.repeat` events whose `prior_credit` is that player (with the actor rule: the player who did the earlier repair).
- Repairs with no player (automation, admin) log `failure.fixed` with no actor and `cause: simulation` or `admin`; they never give credit.
- **Cost:** updated on claim, fix, or new failure only. No ticking. Repeat detection is one map lookup `(machineId, cause, phase) → last credit` per failure.

### 2.9 Planetary Monitor: progressive disclosure and the next-gate display

Feasibility check of DESIGN.md "Learning Mechanisms in Play". Both are **cheap and feasible**.

**Progressive disclosure.** Which pages and readouts the Monitor shows is data (`data/mars/monitor/pages.json`): each readout has a `visible_when` in the same condition language as gates (usually a latched gate, e.g. hydrosphere `visible_when: gate mars:thaw`). The server already syncs latched gates, so the client filters locally.
- **Display only.** The Index still simulates every value from the start; hiding a readout never changes the planet. The world cues for hidden values (frost, the weather mast) still show.
- **Never hides a live danger.** A readout whose value is driving a current failure or warning is shown regardless, with a "new reading" tag. Rule in code, not data, so content can't break it.
- **Teachers and debug** see every readout in the teacher overview.
- **Monitor views count only if they last** `learning.monitorMinViewSec` (default **1 s**, Lead Instructional Designer). The client sends "opened" and "closed"; the server logs `monitor.viewed` with the duration and page, and only views at or above the minimum count toward `monitor_checks_before_builds` (gaming guard, LEARNING.md 3.10).
- **Cost:** one condition check per readout when gates change. Zero per tick.

**Next-gate display** ("Next: pressure 5 kPa, 62 %"). The server picks the next gate from a data order list per planet and, each simulation step, adds a small block to the snapshot: for each condition of that gate, `{parameter, current, start, target, axis}`, plus hold progress for hold gates.
- **One bar per condition, not one combined percentage.** Phase 3 needs pressure **and** temperature; a single "71 %" would hide which one is behind (the same reasoning as the Air window, style guide §11.2).
- **Progress uses the gauge's axis.** Pressure uses log10, like the Monitor gauge, so 62 % means the same on the bar and the gauge. On a linear axis, 0.6 → 5 kPa would show 62 % at 3.3 kPa; on log10 it shows 62 % at 2.2 kPa. The Lead Game Designer and Lead Artist pick which one players see; the code supports both (`axis` per condition).
- **Time-to-gate estimate** ("~4 sols at this rate", ui-specs.md): the server keeps the last few steps' rate per parameter (it already has the summed contributions), divides the remaining distance by it, and sends "no estimate" when the rate is zero or negative. Cost: one division per condition per step.
- **Progress can go down.** If pressure falls, the bar falls. That is intended (regression must be visible, DESIGN.md §1).
- **Machine-state gates** (Dome stable): progress = sols without a life-support failure ÷ 3 (or the classroom hold).
- **Hold gates:** a second bar for "held X of Y sols", paused when out of band.
- **Cost:** a few subtractions per step for one gate. Zero on the client beyond drawing.
- **As built (slice 7, 2026-11-27):** `MonitorView.Goal(fraction, percent, etaKey, etaArg)` on the goal row, computed by pure `mars.monitor.view.GoalProgress` when the view is built (the Monitor is built on request, not per tick). Pressure progress is log10 from the parameter's start value (0.6 kPa) to the target; hold gates show held sols over the hold. The estimate is remaining distance over the **current net rate** (sources plus frost), not an average of past steps; "Not rising" below 0.005 kPa/sol. One bar, one condition only (no multi-condition gate exists yet). The bar is drawn full width, 4 GUI px; the percent, estimate and goals count are on the detail line, which gives way first when the panel is tight. "Useful now" is not built (needs gate data lists and the recipe view).

### 2.10 Fading feedback state per player

Feasibility of DESIGN.md "Fading" and LEARNING.md 4.2 (Hana's tips stop once the player has fixed that cause; hints get more specific on repeats). **Feasible**, with one design consequence to note: on a shared planet, two players can see **different** versions of the same failure report.

- **State per player** (in the learning record, Mars data, not rolled back): for each feedback key (e.g. `failure.seal_breach`), `times_seen`, `times_fixed_by_player`, `current_hint_level` (1–3), `tip_retired` (yes/no), `worked_example_skipped` (yes/no).
- **Rules in data** (`fade` block in the feedback JSON, shared format DOCUMENTATION.md 5.1): e.g. `{"retire_tip_after_own_fixes": 1, "escalate_hint_after_repeats": [2, 4]}`. The Lead Instructional Designer tunes the numbers without code. **Starting numbers (Lead Instructional Designer, LEARNING.md 5.5):** tip retires after 1 own fix; hint level 2 at the 2nd and level 3 at the 4th occurrence; outcome form after 1 own fix that held; back to level 1 on any repeat after the player's own fix; forecast pointers return below 1 in 3 after 3 forecasts in a phase.
- **`guidance` (decided, owner, round 3)** sits on top of fading: gentle (classroom default), standard, expert (not offered in classroom mode). It only removes *extra* help; at every level failure reports still name the cause, forecast results still explain, and safety warnings fire. Fading runs inside every level. Stored per planet (teacher) and per player (standard play), logged on change (`guidance.changed`) and exported (8.6). Implementation: one more input to the same per-recipient text selection; no new system.
- **Rendering:** the story module delivers a message to each recipient. Before it sends, it asks `FeedbackState` (1.3) for that player's hint level and tip state and picks the text keys. Per recipient, not per planet.
- **Phase-level fading** (forecast hints shrink by gameplay phase; Monitor cause breakdown closed by default in Phase 3) needs no per-player state: it is keyed on latched gates.
- **Evidence:** each delivery logs `feedback.shown` with `hint_level` and `tip` (yes/no), so the action log shows the adaptive feedback (TEAM.md section 3) firing.
- **Cost:** one map lookup per message per recipient; a few dozen small counters per player.

### 2.11 Weather mast instruments

The weather mast (decision log 2026-11-26) shows windsock angle and sounding-balloon height (pressure), a thermometer (temperature), and O2 and CO2 tubes (partial pressures).

| Option | How | Cost |
|---|---|---|
| Block states (discrete steps) | The server changes the block's state as values change | Too many state combinations (5 windsock × 9 balloon × 12 thermometer × 12 O2 × 12 CO2); every change re-meshes the chunk and sends block updates |
| **Block entity renderer reading the client snapshot (recommended)** | The mast is one block with a block entity that stores nothing. Its client renderer reads `mars:pressure`, `mars:temperature`, `mars:o2_partial`, `mars:co2_partial` from the synced snapshot and draws the moving parts with the same 30 s smoothing as the sky | **Zero server cost** and no network traffic beyond the snapshot. Client: a few dozen quads per visible mast per frame. The balloon floats up to 8 blocks above the base, so the renderer widens its bounding box (`getRenderBoundingBox`, [1.21.1 source](https://raw.githubusercontent.com/neoforged/NeoForge/1.21.1/src/main/java/net/neoforged/neoforge/client/extensions/IBlockEntityRendererExtension.java)) |

- **Honest by construction:** the mast and the Monitor read the same snapshot values, so they can't disagree.
- **Global values:** the thermometer shows the global mean (keyframes §3.2), not local heat. If the artist wants local temperature, the renderer can add `mars:chunk_heat` at no extra cost.
- **Gas gauges fitted at the Phase 3 gate (decided, owner, round 3).** The mast's O2 and CO2 tubes appear when Phase 3 begins (a model part the renderer draws only when that gate is latched; no cost). Before that the mast shows windsock, balloon, and thermometer. A1's "atmosphere makeup" needs another Phase 1 source (Lead Game Designer).

### 2.12 Sound system hooks

Audio is owned by the Lead Artist; these are the engine hooks the audio design plugs into. Three requirements: **pressure-driven ambient sound**, a **cue queue that never plays feedback over voice**, and **subtitles for every cue**. All of it is client-side; the server only sends events.

**1. Pressure-driven ambient sound**
- **Ambient layers** are looping sounds (`TickableSoundInstance`) whose volume is set every client tick from the smoothed `mars:pressure` (same 30 s smoothing as the sky). Layers and their volume curves live in `assets/mars/keyframes/ambient.json`, same schema as 2.5.2 (`input: mars:pressure`, `axis: log10`).
- **Muffling without engine changes (recommended for Stages 3–4):** each exterior bed comes in **pre-filtered versions** (e.g. `wind_thin` already low-passed at ~1.5 kHz, `wind_mid`, `wind_full`), cross-faded by pressure. This matches the art's gain and low-pass table (keyframes §3.1) without touching the sound engine.
- **Vanilla and third-party world sounds** (footsteps, block breaking, mobs) outside the dome: a `PlaySoundEvent` handler ([1.21.1 source](https://raw.githubusercontent.com/neoforged/NeoForge/1.21.1/src/main/java/net/neoforged/neoforge/client/event/sound/PlaySoundEvent.java)) wraps the sound with the exterior gain from the keyframe table when the listener is outside a sealed volume. Gain only, no filter.
- **Real low-pass filter (optional, Stage 5):** OpenAL's EFX low-pass filter can muffle every exterior sound. Minecraft's sound channels don't expose their OpenAL source, so this needs an access transformer or mixin and is fragile across updates. Only if playtests show gain alone breaks the effect. (Sound Physics Remastered does this kind of thing but is GPL-3.0 and does much more than we need: [LICENSE, 1.21.1](https://raw.githubusercontent.com/henkelmax/sound-physics-remastered/1.21.1/LICENSE); not adopted.)
- **Inside vs. outside:** the client knows from the synced "in sealed volume" flag (already needed for the O2 HUD). Inside the dome, interior sounds play normally and exterior beds drop further.
- **Helmet-off** (style guide §9.2): the exterior beds open to full when `mars:breathable_air` is on **and** the helmet is off; the suit fan and breathing layers fade. Same mechanism, one more input. **Accepted (round 3):** one extra bed, `wind_full_helmet` (6 kHz low-pass), used at high pressure while the helmet is still on, so the 75 kPa helmet-off moment is audible as the filter opening. Input: the synced helmet state.
- **Sound categories (audio direction §9.4), accepted with one change.** Minecraft 1.21.1's categories (`SoundSource`) are master, music, records, weather, blocks, hostile, neutral, players, ambient, voice (**from knowledge; check against the skeleton's sources in Stage 1**); there is no separate interface category in 1.21.1. So: ambience and wind beds → ambient; rain and storm → weather; machines, airlock, terrain → blocks; thawlings → **neutral**; suit, footsteps, dosimeter → players; **alarms and feedback cues → master**, so a player who turns down "players" or "ambient" can't mute a life-threatening alarm or lose feedback cues (subtitles still apply); music → music; voice (if ever added) → voice.
- **Audible range scaling** (audio direction §9.3, "Stage 5 optional"): agreed as optional Stage 5 polish; gain already carries the cue.
- **Cost:** 3–5 looping sources; one volume update per loop per tick. Negligible.

**2. Cue queue: feedback never plays over voice**

A client `CueQueue` sorts every non-ambient sound we trigger into channels:

| Channel | Examples | Rule |
|---|---|---|
| **Alarm** | First O2 critical, breach, visor returns | Plays at once (TEAM.md 8.6 rule 3, life-threatening). Ducks voice and ambient by about 10 dB while it plays |
| **Voice** | Voiced BELL lines, if the Stage 3 narration revisit adds them (DESIGN.md: narration "revisit") | One at a time, queued in order. Ambient ducks about 6 dB under it |
| **Feedback** | Message chime, log entry, gate reached, forecast result | **Never starts while voice is playing or within 0.5 s after it ends.** Never during danger (TEAM.md 8.6 rule 1), **except storm and solar-event warnings**, which are safety information and play at once (accepted from audio direction §9.3). Otherwise waits for the natural-pause signal the message queue uses. **Log chimes merge within 10 s** (`audio.chimeMergeSec`, accepted from audio direction §9.3; was 3 s); info-only bursts play at most once per 60 s. |
| **World** | Machines, fauna, weather events | Normal vanilla behavior, with exterior gain |

- "Is voice playing" uses the vanilla sound manager's active check on the voice instance we hold. No engine changes.
- There is **no voice today** (text-first, decided), so the voice channel is a hook. If narration is added later, the rule already holds.
- **Cost:** a short priority queue checked once per client tick.

**3. Subtitles for every cue** (style guide §4.7)
- Every `mars:` sound event in `sounds.json` must have a `subtitle` key, and that key must exist in `en_us.json`. A **unit test fails the build** otherwise.
- **Ambient layers** show a subtitle when a layer becomes audible or changes band ("Wind, faint" → "Wind"), so a player with sound off still learns that the air is thickening.
- **Voice lines** (if added) don't need subtitles: the message card is the transcript (text-first).
- **Format notes for the audio producer:** Ogg Vorbis; **mono** for any positional sound (vanilla only places mono sounds in 3D; **stated from knowledge, unverified** in this session); stereo is fine for ambient beds and UI.

### 2.13 Entity animation (thawlings)

Answers style guide §5.4 and asset A-03 (11 animations, 2–3 life stages).

| Option | What | Dependency | For the artist | For us |
|---|---|---|---|---|
| **A. Vanilla Java models + vanilla keyframe animations (recommended)** | Box-model `EntityModel` plus Minecraft's built-in keyframe animation system (the one the warden, camel, and sniffer use). Blockbench's "Modded Entity" export produces the model and animation code | None | Blockbench, export, done. No blending between animations; transitions need a short dedicated animation or a cut | No extra mod on every client and server; nothing to update but Minecraft itself |
| B. GeckoLib | Animation library with Blockbench plugin, blending, and more | Extra mod required on client **and** server. MIT ([LICENSE](https://raw.githubusercontent.com/bernie-g/geckolib/main/LICENSE), main branch); NeoForge 1.21.1 builds listed on Modrinth (search, not fetched) | Easiest pipeline; smooth blends | One more dependency to track per version (risk R-06) |

**Decision (Technical Lead, fluid):** **A**. The thawlings are box-model animals with simple, slow motions (style guide §6.4); vanilla's system handles that, and it keeps every player's install one mod lighter. **Revisit if** the artist finds that missing blends make the "rear up, shove, curl, roll away" sequence look broken in the Stage 4 prototype; GeckoLib is then the drop-in alternative, and the Blockbench sources carry over. The Blockbench "Modded Entity" animation export is described in the MCreator wiki ([link](https://mcreator.net/wiki/entity-model-animations), via search, not opened): **unverified** for 1.21.1 output; check in Stage 2 with one test animation.

**Verified round 3 (network open):** NeoForge 1.21.1 ships a **JSON loader for vanilla keyframe entity animations**: `AnimationLoader`, reading `assets/<namespace>/neoforge/animations/entity/*.json` as a resource reload listener ([source, 1.21.1 branch](https://raw.githubusercontent.com/neoforged/NeoForge/1.21.1/src/main/java/net/neoforged/neoforge/client/entity/animation/json/AnimationLoader.java)). The official Blockbench plugin list has **"Animation to JSON Converter"** ("Converts Blockbench animations to the JSON format for the NeoForge mod loader", v1.0.1) and marks the older Java-code converter **deprecated** ([plugins.json](https://raw.githubusercontent.com/JannisX11/blockbench-plugins/master/plugins.json)). So the pipeline is: Blockbench → JSON in `pack/assets/mars/neoforge/animations/entity/` → reloadable with F3+T, no Java per animation. Animations become content, like keyframes. The box model itself comes from Blockbench's built-in Modded Entity export (Java); whether its template matches 1.21.1 names is checked with one test model in Stage 2 (**still unverified**).

### 2.14 Time on Mars: sleep, `/time set`, and the sky

Implements DESIGN.md "Sleep and the sky" (Lead Game Designer, round 2).
- **The sky follows simulation time.** Each server tick, every planet's level has its day time set from its `SimClock` (`dayTime = simTicks mod pace.solTicks`). The client's sky renderer reads that, so sunrise and sunset line up with the sol and a paused world freezes the sun. Cost: one assignment per planet per tick.
- **Sleeping never skips time on Mars.** A `SleepFinishedTimeEvent` handler ([1.21.1 source](https://raw.githubusercontent.com/neoforged/NeoForge/1.21.1/src/main/java/net/neoforged/neoforge/event/level/SleepFinishedTimeEvent.java)) sets the new time to the current time for Mars levels. A bed still sets the respawn point, and lying down sends the "natural pause" signal (9.6).
- **`/time set` has no effect in Mars dimensions**: whatever it sets is overwritten on the next tick by the sync above. The overworld (Training Hall) keeps vanilla time.
- **Mob spawning and light** follow the level's day time, so they follow the sol too, which is what the design wants.

### 2.15 Mekanism and Create: optional and late

Decided (owner, round 3): unlocked at Hydrosphere, never required for a gate, never used to teach; hidden until then; off by default in the classroom preset (DESIGN.md "In-house machines").

| Layer | How |
|---|---|
| **Install** | In `pack/mods/` both are marked **optional** (`[option] optional = true` in their packwiz `.pw.toml`; field confirmed in [packwiz source](https://raw.githubusercontent.com/packwiz/packwiz/main/core/mod.go)). The **classroom server bundle leaves them out**: no RAM, no ticking machines, nothing to hide. That is also the cheapest answer for weak hardware. |
| **Our mod** | No hard dependency: Mekanism and Create are `compileOnly`; integration code runs only if the mod is loaded. KubeJS scripts check the same before touching their recipes |
| **Gate (when installed)** | Per planet, until `mars:hydrosphere` is latched: placing or using any block from the `mekanism` or `create` namespace is cancelled with one quiet line ("Unlocks at Hydrosphere", only on `guidance = gentle`). Their items can be crafted but do nothing yet. Recipes can't be locked per planet because recipes are server-wide; blocking placement and use is per planet and costs one gate lookup per placement |
| **Hidden** | No recipe viewer is in our stack yet. If one is added (JEI or EMI), its runtime API hides those namespaces per client until the gate syncs. Until then, "hidden" means the journal and our screens never mention them before Hydrosphere |
| **Pause** | The Stage 2 GameTest on `/tick freeze` (7.2) is only needed for standard play now; the classroom bundle has neither mod |

**Licence consequence (round 3 recheck):** Create's jar contains its All Rights Reserved assets, so **our offline classroom bundle may not include Create** without permission (mod-licenses.md). Leaving it out of the classroom bundle solves that too.

### 2.16 In-house machines: one framework, ~21 machines, and the orbital mirror

Costing for the Lead Game Designer's list (DESIGN.md "In-house machines"). **All numbers are estimates in developer-days for one experienced NeoForge developer, code and data only, excluding art, audio, and text; unverified, no velocity data yet.** Re-estimate after the Stage 2 prototype.

**The framework (built once, ~12–18 days).** One `MarsMachine` block entity type and one screen template give every machine the DESIGN.md interface pattern: input slot, output slot, power bar, shape-coded status lamp, and the two fixed lines **"Effect on planet"** (read from its live `ContributionHandle`) and **"Costs"** (from data). It also covers: power draw from the dome grid, wear and maintenance parts, fauna signature registration (2.4), heat source registration (2.5), machine ID and last actor (2.8), failure reporting, the shroud modifier, and the `major` flag. Each machine is then mostly a **data file** (`data/mars/machines/<id>.json`: contribution per sol, power, upkeep, signature, heat, unlock gate) plus a small class only where it does something special.

| Machine | Phase | Beyond the framework | Estimate (days) |
|---|---|---|---|
| Regolith Oven, solar panel, battery, O2 generator, water reclaimer | 1 | Data only; solar reads `mars:storm_strength` | 0.5 each → 2.5 |
| Dust-off station | 1 | Clears the perchlorate-dust effect from a player in the airlock | 1 |
| Regolith Outgasser | 1 | Data + puff rate state for art | 1 |
| Weather mast | 1 | Renderer (2.11) | 2–3 |
| Atmospheric processor, super-greenhouse emitter | 2 | Data | 0.5 each → 1 |
| **Orbital mirror uplink** | 2 | See below | 4–6 |
| Ice extractor | 2 | Reads ground-ice lenses; collapse risk | 2–3 |
| Meltwater treatment | 2 | Perchlorate filter state | 1 |
| Transit tubes | 2 | **Player transport is not a simple machine.** Recommend the simplest form: paired tube stations that move a player between two points after a short ride screen (no moving entity), ~4 days. A rideable tube system would be 8–12 days | 4 (simple) |
| Insulation shroud | 2 | Modifier on another machine: −15 % output, no signature, no heat | 1–2 |
| Lure station | 2 | Attracts thawlings (fauna AI goal) | 2 |
| Quiet-zone marker | 2 | Area registration + ring renderer + fauna rule | 2 |
| Algae vat, carbon scrubber | 3 | Data; scrubber deposits are block placement over time | 0.5 + 2 → 2.5 |
| Soil treatment | 3 | Converts treated blocks | 1–2 |
| Biome seed kits (×4, a succession chain) | 3 | **The biggest item:** spreading flora by random ticks, ground conversion chain, biomass contribution from living blocks, self-sustaining check | 8–12 |
| Beacon components (5 types) and beacon sequence | 3 | Assembly check, ending sequence, ending cards (9.5) | 6–8 |

**Total:** about **12–18 (framework) + 40–53 (machines) ≈ 52–71 developer-days** (sum of the low and high ends of the rows), before art and audio. The framework is what makes each later machine cheap; it should be built in Stage 2–3 with the Phase 1 machines, so its cost is proven early.

**Cheaper options if scope is cut (Producer):** simple transit stations (already assumed); biome kits share one spreading engine with four data files (assumed); defer lure stations (DESIGN.md cut order); beacon sequence reuses the helmet-off camera-free style (no cutscene).

**Orbital mirror uplink (decided route, owner, round 3).** A ground uplink block, expensive and slow to build: after placing it, the player delivers parts over several sols (a build-progress state in the block entity, shown on its "Costs" line). When complete it adds a temperature contribution with **no fauna signature and no ground heat source** (so no frost-free ring, no shimmer, no thawlings: the point of the route). It draws dome power like every machine. In the sky, a small bright point moves across the night sky per completed mirror (drawn by our sky renderer from a synced count; a few quads). F5 also fires on the first completed uplink (DESIGN.md). Science framing and numbers: SME and balance sheet. Cost: **4–6 days** (multi-sol build state 2, contribution and Monitor cause line 0.5, sky point 1–2, F5 trigger 0.5, tests 1).

### 2.17 Solar panel dust (BACKLOG #22, #26)

Implements DESIGN.md "Solar panel dust" with the numbers from balance sheet §2a. **Built so far (Stage 1, BACKLOG #26):** the block-state property and the tuning data with a test; nothing changes dust over time yet. **All cost estimates are developer-days for one experienced developer, unverified.**

| Piece | Design | Cost per tick | Estimate |
|---|---|---|---|
| **Per-panel dust state** (built) | Block state on `mars:solar_panel`: `dust_stage` 0–4 and `partly_cleaned` (true/false), mapped in `blockstates/solar_panel.json` to the six models. The stage is what the client draws. Fractional progress toward the next stage lives in the panel's block entity (Stage 2, with the machine framework, 2.16), so the block state changes only once per stage | A block-state change re-meshes one chunk section, about once per 3 sols per panel | Built; progress field ~1 day with the framework |
| **Tuning as data** (built) | `data/mars/hazards/panel_dust.json`: stage interval 3 sols (÷ storm cap ÷ `pace.rateMultiplier`), output 100/92/82/70/60 %, partly cleaned recovers half the lost output (from stage 2), storm end +1 stage (+2 at cap ≥ 0.8), dust devil 1 %/sol per panel group (Phases 1–2), rain to stage 1 or partly cleaned (Phase 3), ×2 within 8 blocks of digging, heavy machinery, or exhaust. Every block tagged `invented` as the sheet says. A unit test reads the balance sheet and fails if any number differs | — | Built |
| **Accumulation** | Each Index step, every panel's block entity adds `dtSols / interval` to its progress, doubled if its chunk is flagged as a local dust source. Local sources: a per-chunk "dusty until tick" stamp set by player block breaking and by exhaust machines (from the per-chunk machine index, 2.4) | One add and compare per panel per 5 s step: microseconds for hundreds of panels | ~2 days |
| **Storm end and rain** | On the storm's end event: add 1 or 2 stages to every panel on the planet. Phase 3 rain event: panels with open sky above (heightmap check) go to stage 1 or partly cleaned, whichever is cleaner | Once per event, one pass over the planet's panel list | ~1.5 days |
| **Dust-devil roll in simulation time** | Once per sol of **simulation time** (so a paused world never rolls), per **panel group** = the panels in one chunk. Seeded from `hash(worldId, planet, chunkPos, solIndex)` so it reads the same after save and load (test-strategy §6). On success, panels at stage ≥ 2 in that chunk become `partly_cleaned` | One RNG draw per chunk with panels per sol | ~1 day |
| **Dust cause line** | The planet keeps a running sum of panel output factors (updated only when a panel changes stage), so "Panel dust −n %" = 1 − average factor costs nothing to read. It goes in the Monitor's power-row causes, separate from "Storm: less sunlight". Progressive disclosure: the line appears the first time any panel reaches stage 1, with one BELL line, logged as `monitor.line_revealed` | Zero per tick | ~1.5 days |
| **Panel brush** (item) | Use on a panel: back to stage 0, clears `partly_cleaned`; 64 uses (item durability); logs `panel.cleaned` with the actor, the stage before, and whether a storm was forecast (C1 evidence the Instructional Designer asked about) | On use only | ~1 day |
| **Panel wiper** (block, from Phase 2) | A machine (2.16 framework) attached to a panel row: keeps each panel in its row at stage ≤ 1 while powered; 1 power unit per sol per panel; one bearing (maintenance part) every 10 sols | Runs inside the panel accumulation step | ~2–3 days |
| **Tests** | Unit tests for accumulation, storm, devil (seeded), rain, brush; a GameTest that dusts and cleans a panel | — | ~1.5 days |

**Total remaining (unverified): about 11.5–12.5 developer-days**, on top of the machine framework. The shape stays a simple box (`0,0,0 → 16,10,16`) that doesn't follow the model, so a wider circular array model later needs only a shape change (and, if wider than one block, a multi-block placement rule).

### 2.18 Round solar array: footprint, hitbox, and model approach (BACKLOG #28)

**Decision (Technical Lead, fluid): a 3 × 3 footprint of one centre block plus eight invisible canopy blocks; the model stays vanilla JSON boxes (the Artist's 23-element pixel disc).**

| Part | Rule | Tested |
|---|---|---|
| **Centre block** `mars:solar_panel` | Draws the whole array (model elements −9..25 px, inside the docs' allowed [−16, 32]). Shape = base plate (y 0..1), three legs (y 1..13), deck (y 13..15) and the part of the array over its own cell (disc y 15..16), matching the Artist's model (`DECK_Y = 15`, Producer's reading of the SME's 16 px deck height) | GameTest `footprint.centre_shape`: bounds `[0,0,0]→[1,1,1]` |
| **Canopy cells** `mars:solar_panel_canopy` (no item, not in the tab) | Placed automatically in the 8 surrounding cells at the same height, for player placement and `/setblock` alike (`onPlace`). Each cell's shape is only the slice of the array's square outline over that cell, at the array's height (disc y 15..16 px, the top pixel of the cell), so **no shape ever leaves its own block** and selection and collision follow the overhang. Invisible (`RenderShape.INVISIBLE`); its particle texture is the frame | `footprint.fillers` read `CCC/CPC/CCC`; `footprint.corner_shape` read `[0.4375,0.9375,0.4375]→[1,1,1]` |
| **Placement** | Needs all 8 surrounding cells free (replaceable: air, grass). Otherwise nothing is placed and the action bar says "The solar array needs a clear 3 × 3 space around it" (no silent failure). Neighbours outside the 3 × 3 are unaffected: a battery fits right next to a footprint | `footprint.blocked` / `footprint.clear`; screenshots (battery beside an array) |
| **Breaking** | Breaking any cell breaks the whole array and drops one panel (loot table on the centre; canopy has none). Removing the centre clears its cells. A dust-stage change doesn't touch the cells | `footprint.break_overhang` read `destroyed=true … cells=0, drops=[1 mars:solar_panel]`; `footprint.remove_centre`; `footprint.dust_change_keeps_fillers` |
| **Lighting** | Canopy blocks don't occlude or block light. In game the array's underside renders as an even dark disc with no light leaks, black patches, or holes, at noon and at dusk. The ground under the overhang is **not shadowed** (Minecraft lighting is per block; a non-occluding canopy casts no shadow) | Screenshots `round_under_overhang_noon`, `_dusk` |
| **Culling** | An array centred on a chunk corner (16, 64, 16), with its overhang in three other chunks, rendered whole in two views. A view with the centre's chunk section fully outside the camera wasn't achieved, so frustum culling of the overhang is **not fully tested** | Screenshots `round_chunk_corner_*` |

**Model approach compared** (cost and in-game look):

| Approach | Cost | In-game look | Verdict |
|---|---|---|---|
| **Vanilla JSON boxes** (pixel disc, 23 elements) | **Zero code**; the Artist's generator already writes it; hot-reloadable; ~80–130 faces per array (Artist's estimate) | **Seen in game**: reads as a 12-segment fan from above, matches the pixel art, dust stages and partly cleaned distinct; stair-stepped edge at 1 px is invisible at play distance | **Chosen** |
| `neoforge:obj` (Wavefront mesh) | No Java, but a second art pipeline (.obj/.mtl export), and UVs must match the 48 × 48 dust textures | **Not tried in game.** Smooth polygon edges would sit next to pixel-art blocks | Not adopted; revisit only if a tilt or fold animation is wanted |
| `neoforge:composite` with per-part transforms | Unknown whether each part can carry its own transform (Artist: untested); JSON only | Not tried | Not adopted |

Same footprint rule applies whatever the model approach, so switching later touches only the blockstate.

**Raised array (2026-11-27):** the disc now sits 1 m up on a lander-deck mount (SME: InSight deck 0.83–1.08 m, source not opened by the SME). Only the shape constants moved; nothing moves up a block and there is no ninth cell. The slab over each canopy cell is now at the very top of the block, so a player cannot walk under it (0.94 m clearance, as the Artist noted). Legs are three 2 px boxes at the model's leg positions, so the selection outline follows the legs.

**Known look issues (for the Artist and Game Designer):** The selection outline shows one box per cell (up to 9 for one array), and each corner box includes a small empty corner outside the 12-sided edge. A single whole-array outline would need a client render hook (`RenderHighlightEvent`), about 1 developer-day (unverified).

**Cost of what was built:** about 1 developer-day including tests. **Per-tick cost: zero** (no ticking; work happens only on place and break).

The **battery** (batch 3 wall pack, 16 px tall and 6 px deep) now uses one plain box that turns with facing (`{2,0,5,14,16,11}` north); GameTest `machines.battery_shape_turns`.

### 2.19 Planet values, Outgasser, Monitor, session clock, power, gates, and polar frost: Stage 2 slices 1 to 5 (built)

Plan and checks: [docs/tech/stage2-plan.md](docs/tech/stage2-plan.md). Fluid; the first slice is allowed to be thrown away except the pure core and its tests.

| Piece | Design | Cost |
|---|---|---|
| **`PlanetSim`** (pure, `mars.index.sim`) | The Index model plus the step counter (one step per `pace.stepTicks` = 100) and a machine registry (type, packed block position) to contribution handles. `PlanetState` is its saved form: schema version, stored values, sim ticks, pending ticks, machine positions by type. A newer schema is refused; unknown ids and machine types are reported and kept out of the values (machine positions are kept so a later build loses nothing) | Per tick: one increment and compare. Per step: one add per contribution |
| **Data** | `data/mars/parameters/*.json` and `data/mars/machines/*.json`, loaded by one reload listener (`IndexDefinitions`), so definitions are datapack data and reloadable. A machine file lists contributions (`parameter`, `ratePerSol`); a test checks the Outgasser rate against balance sheet §2 | Once per reload |
| **Saving** | `PlanetData extends SavedData`, one per planet dimension (name `mars_planet`, in that dimension's data folder). A planet is any dimension in the `mars` namespace (`mars.core.Planets`) | A dirty flag set every step; written by the normal level save |
| **Clock** | `LevelTickEvent.Post` calls `onServerTick()` for planet levels only, and not while the level's tick-rate manager is frozen (`/tick freeze` is the pause mechanism, §7.2). Note: `runsNormally()` is refreshed by the manager's own tick, so a freeze takes effect on the next tick; `/tick step` also advances the planet | One string compare per level per tick |
| **Machines** | `RegolithOutgasserBlock`: `onPlace` and `onRemove` register and unregister the position. No block entity, no ticking. Chunk unload does **not** remove the contribution (planet-scale simulation does not depend on loaded chunks); this is a design call to confirm with the Game Designer | Zero per tick |
| **Monitor** | `MonitorView` (pure) is built on the server from the real Index: rows for pressure, temperature, and the water line, each with a trend (steady below 1e-9 per sol) and cause lines. `MonitorPayload` carries it to the client (already formatted by `Fmt`); `MonitorScreen` only draws it. The client handler is registered through a lambda, never a method reference (a method reference loads the client class on a dedicated server and crashes the load) | One snapshot per open |
| **Debug** | `/mars index get / advance <sols> / set <parameter> <value>` and `/mars monitor`, operators only, logged | — |
| **Session clock and pause (slice 2)** | `SessionClock` (pure, `mars.session`): one paused flag and a session number that rises on every resume. `PlanetSim.onServerTick(SessionClock)` does nothing while paused, so counters, values and machines stay exactly as they were (**invariant I-3**). Nothing in the tick takes a time argument, so wall-clock time, the daylight cycle and sleeping cannot reach the sim; a stopped server adds nothing on restore (the saved state is read back unchanged). `SessionData` saves the flag in the overworld's data folder (deviation from 7.2's `session.json`; recorded there). `SessionControl.pause/resume` set the flag **and** the vanilla tick freeze; `ServerStartedEvent` re-freezes a saved pause (the freeze itself isn't saved). `/mars pause`, `/mars resume`, `/mars session` (ops). Not yet: start-paused config (slice 9), backup and recap in the resume order, blocking player actions while paused | Per planet tick: one saved-data lookup and one boolean read |
| **Unloaded chunks** | Owner-accepted rule (BACKLOG #32): machines contribute whether or not their chunk is loaded. The registry is by position and vanilla does not call `onRemove` when a chunk unloads, so nothing special is needed; GameTest `outgasser.chunk_unloaded` proves it with a real unload | Zero |
| **Power (slice 3)** | `PowerGrid` (pure, `mars.power`): one grid per planet with totals of supply, demand and battery capacity (power units per sol, 1 unit = 1 kWh) and a charge. Per step: supply meets demand first, the battery covers a shortfall, a surplus charges it; a shortfall cuts every powered machine by the same share. `MachineDef` gained an optional `power` object (`drawPerSol`, `supplyPerSol`, `storage`); `PlanetaryIndexModel.setBeforeStep` lets `PlanetSim` run the grid at the start of every step and set each powered machine's contribution to its base rate x the share of power met. Solar output is the base supply x a per-position output fraction (`PlanetSim.setEfficiency`), which `SolarPanelBlock` sets from `PanelDustTuning.outputFraction(stage, partlyCleaned)` on every place and dust-stage change (`PanelDustTuning` is now loaded by `IndexDefinitions` from `data/mars/hazards/panel_dust.json`). `BatteryBlock` and the panel register their positions like the Outgasser. `PlanetState` schema 2 adds the battery charge and output fractions; schema 1 saves still load (full output, empty battery). Monitor: a **Power** row appears when the planet has any power machine, with the words "Enough power", "Not enough power" or "No power" and cause lines for supply, demand and battery. Numbers: balance sheet 2b; the tests compare the data files to the sheet. | Per step: one pass over the powered machines (a few additions each); totals are recounted only when a machine is placed, removed or its output changes. Not modelled: night, wiring range, storms on the grid |
| **Changed in slice 3 (was slice 1)** | **A Regolith Outgasser now raises pressure only while powered.** In slice 1 it ran with no power; now an Outgasser with no panel adds 0 kPa/sol (the Monitor says "No power"). Tests and GameTests that placed Outgassers now add panels first. | - |
| **Gates and unlocks (slice 4)** | Pure package `mars.gates` (ArchUnit-checked). `Condition` is the JSON language of 2.2: `all`, `any`, `not`, `{parameter, op, value}` and `{machine_state}`; the parser rejects any other key, so a file cannot carry a scaling field (I-1). `GateDef` reads `data/mars/gates/*.json` (id, order, condition, optional `hold` {standard_sols, classroom_sols}, `unlocks` machine ids). `GateTracker` holds the state: **latched gates are skipped and never reverted (I-2)**; a hold counts simulation sols while true, pauses while false, resets after `Pace.holdResetSols()` (1 sol, classroom 0.5) of continuous failure. `PlanetaryIndexModel.setAfterStep` runs the evaluation after every step, so `/mars index advance` and the real tick take the same path. Saved in `PlanetState` schema 3 (latched, cause, latched tick, held, false sols); older saves load with gates unlatched. Cost: one condition evaluation per unlatched gate per step (every 100 ticks); zero after latching. | One list check per step |
| **Machine state `life_support_ok` (stand-in)** | The only machine state so far: the grid has supply and covers demand (not Short, not Unpowered). It stands in for life support until the oxygen maker and sealing exist (Stage 3); replace `PlanetSim.lifeSupportOk()` and the gate files stay as they are. An unknown state name in a gate file fails at load. | - |
| **What "unlocked" means** | A machine type listed in a gate's `unlocks` cannot be placed by a **survival player** until that gate has latched: `RegolithOutgasserBlock.getStateForPlacement` returns null and the player sees "Not yet. Reach "Dome stable" first." on the action bar. **Creative players pass** (logged); `/setblock`, loot and other non-player routes are not gated: that is the creative/admin route. The creative tab still lists the Outgasser. There are no recipes yet (KubeJS is a later slice), so the placement check is the whole lock; when recipes arrive the same gate list drives them. | - |
| **Gate announcements and admin** | `PlanetManager.announceGates` sends one chat line per latched gate and one for what it unlocks, and logs it with the cause. `/mars gate list` and `/mars gate latch <id>` (operators, logged, `Cause.ADMIN`); there is no un-latch command because gates lock in. The Monitor shows a "Next goal" row (hold progress, or the value against its threshold) and a "Goals reached" row. | - |
| **Pressure history and the centred Monitor (BACKLOG #38)** | `PressureHistory` (pure, `mars.index.sim`) is kept by `PlanetSim` and recorded after every step: one sample per sol (the first step is sampled at once), at most 120 samples; when full every second sample drops and the spacing doubles, so a long game keeps its whole span at coarser detail (120 sols is about 40 h). Events: the first machine of a pressure-making type placed (`MACHINE_PLACED`, not recorded when a save loads) and each gate latch (`GATE_LATCHED`, including `/mars gate latch`); at most 16, oldest drops. `PlanetState` **schema 4** adds it (older saves load with an empty history); NBT lists `sols` and `values` plus an `events` list. `MonitorView.History` thins the samples to at most 60 points, appends the current value, and carries markers (language key plus optional argument key) and the unit; the payload sends it (about 650 bytes in the boot check). `PlotScale` (pure) gives the axis: starts at 0, tops out at 1, 2, 5, 10, ... above 1.1 x the maximum. `MonitorLayout` (pure, unit-tested) computes heights; `MonitorScreen` is centred, the plot height flexes so the panel stays about 130 GUI px (122 / 130 / 135 in the three tested states), the goal's second line drops first if the plot would fall under 16 px, and a screen too short for a plot gets the rows only. Battery is ten segments plus a percent. No status lights (no rules defined). | Per step: one compare; per sol: one append. Per frame while open: about 200 fills for the line |
| **Polar-frost sink and the Regolith Oven (slice 5, BACKLOG #43)** | `FrostSink` (pure, `mars.index.sim`) holds the three temperature branches as data from `data/mars/sinks/polar_frost.json`: **cold** (T below -40 C) takes 2 % of (P minus baseline) per sol into a reservoir; **mild** (-40 to -20 C, both ends included) takes 1 %; **release** (T above -20 C) returns the reservoir at 0.5 kPa per sol, then up to 0.5 kPa more from buried polar CO2, once. Zero at or below the baseline; it never adds air while taking. `baselineKpa` is a per-planet entry in the file (`default` plus `planets` overrides; `PlanetData.factory(planetId)` picks it). `PlanetSim` adds the sink as a coupling after the contributions and gives it the same rate scale as the machines (pace multiplier x group scale); the buried amount and the baseline never scale, so the balance point P = baseline + S / fraction is the same in every mode (`PlanetSim.frostEquilibriumKpa`, tested at classroom x4 and groups 3 to 5). Reservoir and buried-released amounts are saved: `PlanetState` **schema 5** (`frost`); older saves load with an empty reservoir. Paused sessions run no step, so pressure and reservoir stay put. **Oven:** `regolith_oven.json` (rate 0.01, `power.drawPerSol` 1.0, `startingGrant` 2) on the same equal-share grid rule as the Outgasser; `RegolithOvenBlock` registers by position like the other machines, not gated. **Starting grant:** `StartingGrant` (pure, `mars.session`) is one flag; `GrantData` (overworld save data) stores it; `GrantEvents` gives the machine-file grant to the first player on a planet (dimension change or login), once per world, so death, relog or a second player never repeats it. There is no recipe yet, so a lost Oven needs the creative tab or `/give`. **Hold rule as data:** each gate hold has `reset_after_sols` (standard 1.0, classroom 0.5); `GateTracker` pauses a failing hold and resets it after more than that; `Pace.holdResetSols` is gone. **Monitor:** the pressure trend now includes the frost (a net under 0.005 kPa/sol reads Steady); `CauseLine.direction` marks planet-side lines, drawn with a shape and a signed number (hidden while the rate rounds to 0.00, about 0.85 kPa; "gives back" when warm); `Row.noteKey` is a two-line wrapped note, used for "Levelling off" when the frost balances the sources and a pressure goal is still open. `MonitorLayout` drops the plot when problem lines would push the panel past the target plus 20 GUI px. | Per step: a few comparisons and multiplications for the sink; no new per-tick work |
| **Sky and fog response (slice 6, BACKLOG #46)** | Pressure drives the Mars sky and terrain fog through `assets/mars/keyframes/sky.json` (2.5.5). Server sends only the pressure; client interpolates (log10, OKLab) with a 30 s time constant. Measured in a real client at all five keyframes (report `2026-11-27-technical-lead-slice6-sky.md`). Not applied yet: storm overlay, hydrosphere, clouds, aureole, star density, sunset band |
| **Test seam** | System property `mars.selftest.anyLevelIsPlanet` (set only in the `gameTestServer` run configuration) makes any level a planet, because the GameTest server has no Mars level. It also allows `PlanetManager.resetForSelfTest` (a fresh planet for the gate GameTest: the GameTest world keeps its saved planet between runs and gates cannot be undone) | — |

Boot-test gap closed in the same slice: Minecraft's server `Main` exits 0 after logging a failed start, so `runGameTestServer` once reported BUILD SUCCESSFUL on a server that never started. `SelfTest` now installs a shutdown hook that prints `RESULT FAIL reason=exited_without_result` and exits 6 if the JVM ends without a RESULT line (proved with a deliberately broken data file).

---

## 3. Coding Practices

| Area | Convention |
|---|---|
| Language | Java 21. NeoForge for Minecraft 1.21.1 requires a Java 21 JDK (NeoForge docs, 1.21.1 getting started; NeoForge's own 1.21.1 branch sets `java_version=21`: [gradle.properties](https://raw.githubusercontent.com/neoforged/NeoForge/1.21.1/gradle.properties)). Minecraft 1.21.1 is settled (3.1). |
| Build | Gradle with NeoForge's ModDevGradle plugin. Starting versions from the official 1.21.1 MDK template: NeoForge `21.1.267`, ModDevGradle `2.0.148`, Gradle `9.2.1`, Parchment `2024.11.17` ([MDK gradle.properties](https://raw.githubusercontent.com/NeoForgeMDKs/MDK-1.21.1-ModDevGradle/main/gradle.properties), [build.gradle](https://raw.githubusercontent.com/NeoForgeMDKs/MDK-1.21.1-ModDevGradle/main/build.gradle), read 2026-11-26). Gradle wrapper committed. Plan: [docs/tech/build-plan.md](docs/tech/build-plan.md). |
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
| Sounds | Every `mars:` sound event has a subtitle key present in `en_us.json`; a unit test enforces it (2.12) |

### 3.1 Decision: stay on Minecraft 1.21.1

- **Decision (user, 2026-11-26, on Technical Lead recommendation):** Minecraft 1.21.1, NeoForge 21.1.x, Java 21. Pin one NeoForge build per roadmap stage; update within 21.1.x only at stage boundaries.
- **Rationale:** every mod in our stack has a maintained 1.21.1 NeoForge build (Mekanism 10.7.19, Create 6.0.11, KubeJS 2101.7.2: [docs/tech/mod-licenses.md](docs/tech/mod-licenses.md)); the official MDK still ships for it; the NeoForge APIs we need (dimension effects, attachment sync, sound events) are present on the 1.21.1 branch (sections 2.5, 2.12).
- **Alternatives:** NeoForge 26.1 on Java 25 (newer, but our stack's builds there are unchecked and the art pipeline would change: e.g. armour textures moved to a new equipment layout after 1.21.1, **stated from knowledge, unverified**). Ongoing ports (cost every stage).
- **Trade-offs:** instructional value: none directly; stability protects the schedule for learning features. Market appeal: players on newer versions may see the pack as dated; most modded players pick packs by content, not version (**unverified**).
- **Revisit if:** Mekanism, Create, or KubeJS stop fixing 1.21.1 bugs that affect us; a security issue in 1.21.1 isn't patched; or Stage 7 planning picks a maintenance port (risk R-06).

### 3.2 Repository and asset layout

Answers the Lead Artist's handoff (style guide §5.3: set up `pack/assets/`, confirm paths).

```
pack/                      the modpack as packwiz text files (what launchers download)
  pack.toml, index.toml    packwiz manifest (Stage 1)
  mods/                    *.pw.toml references to Mekanism, Create, KubeJS, ...
  config/, defaultconfigs/ third-party configs; our mars-server.toml presets
  kubejs/server_scripts/   recipes, one file per integrated mod (section 3)
  licenses/                licence and notice of every bundled mod
  assets/mars/             OUR art, sound, lang, keyframes  -> built into the mod jar
  data/mars/               OUR datapack content (gates, story, learning) -> built into the mod jar
  .packwizignore           excludes assets/ and data/ (they ship inside the jar, not as loose files)
mod/                       companion mod, Gradle project (docs/tech/build-plan.md)
docs/
```

- **Confirmed:** the style guide's paths stand. Art goes in `pack/assets/mars/textures/<block|item|entity|models/armor|particle|environment|gui|misc>/`, exactly as in style guide §5.3. The folders now exist.
- **Why the jar:** the mod's own blocks need their textures and language files wherever the mod runs, including singleplayer with no resource pack. The Gradle build copies `pack/assets/` and `pack/data/` into the jar (build plan §4). Artists and writers never touch `mod/`.
- **Other paths the art and audio need** (vanilla 1.21.1 conventions; **from knowledge, check in the Stage 1 skeleton**): particle definitions `assets/mars/particles/<name>.json` next to `textures/particle/`; sounds `assets/mars/sounds.json` plus `assets/mars/sounds/<category>/<name>.ogg`; language `assets/mars/lang/en_us.json`; block and item models `assets/mars/models/block|item/`; blockstates `assets/mars/blockstates/`; animated textures use `<name>.png.mcmeta`; armour layers `textures/models/armor/<name>_layer_1.png` and `_layer_2.png` (1.21.1 layout).
- **GUI sprites (verified round 3):** 1.21.1 supports GUI sprites under `textures/gui/sprites/`, drawn with `blitSprite`, with `"scaling": {"type": "nine_slice", "width", "height", "border"}` (also `stretch`, `tile`) declared in the sprite's `.png.mcmeta` ([NeoForge docs 1.21.1: textures](https://docs.neoforged.net/docs/1.21.1/resources/client/textures), [screens](https://docs.neoforged.net/docs/1.21.1/gui/screens)). Recommended over hand-cut sheets. **Mono for positional sounds verified** ([NeoForge docs 1.21.1: sounds](https://docs.neoforged.net/docs/1.21.1/resources/client/sounds): stereo files are not attenuated and always play at the listener).
- **Keyframes:** `pack/assets/mars/keyframes/` (sky, fog, clouds, ambient, mast). Owned by the Lead Artist as content.

### 3.3 Screen-size assumption (style guide §4.5)

**Confirmed as a design floor, with one correction to the reasoning.** Vanilla picks the largest GUI scale at which the scaled screen stays at least 320×240 (rule described by a mod developer, [MineColonies issue 9821](https://github.com/ldtteam/minecolonies/issues/9821), via search; not from Mojang code, so **partly verified**).

| Window | Max GUI scale | GUI canvas | Fits 420×236? |
|---|---|---|---|
| 1280×720 full screen | 3 | 427×240 | Yes |
| 1366×768 full screen | 3 | 455×256 | Yes |
| 1366×768 **windowed** (taskbar and title bar take ~60 px; estimate) | 2 | ~683×352 | Yes, with room |
| **854×480, Minecraft's default window size** (from knowledge) | 2 | 427×240 | Yes: the same tight case as 1280×720 |

So the 420×236 limit is right, and it matters even on big screens, because a fresh install opens in an 854×480 window. Whether school PCs actually run 1366×768 or 1280×720 is **unverified**: no current survey of school device resolutions was found. 1366×768 was the most common low-cost laptop panel (NPD figures reported by [LaptopMag](https://www.laptopmag.com/articles/laptop-screen-resolution-ripoff), 2017, consumer laptops). Chromebooks are a large share of US classroom devices ([GSMArena summary](https://www.gsmarena.com/nearly_50_of_all_of_us_classroom_devices_are_now_chromebooks-blog-15333.php), via search, not opened); whether a NeoForge pack can run on a school Chromebook at all is **unverified** and likely no, so schools that can run us probably use Windows or macOS PCs, often with larger screens than budget Chromebooks.

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
9. **Build just what the learning experience needs** (Chapter 9). Each back-end piece is first built as the smallest prototype that tells us whether it is feasible, then as end-to-end plumbing; the staging is in [docs/tech/test-strategy.md](docs/tech/test-strategy.md) §9.
10. **Identify things by ID, never by display name.** Registry IDs, machine IDs, and data IDs only; an ArchUnit rule forbids reading translated text in game logic (test-strategy §5).

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
| **Signals** | New world-state values (e.g. a meteor-shower intensity) for visuals and conditions with one JSON file and one `publish` call (2.5.1) |
| **Keyframes in the resource pack** | Re-tune or add sky, fog, cloud, ambient-sound, and instrument looks without code (2.5.2) |
| **Cue channels** | Voiced narration can be added later; the queue already keeps feedback off voice (2.12) |
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

**Decision (user, 2026-11-26, on Technical Lead recommendation): option A; option B as the documented fallback; option C (student-hosted LAN) is not supported for classes.**
- **Rationale:** below ("A gives the teacher one thing…").
- **Alternatives:** B and C in the table above.
- **Trade-offs:** instructional value: all evidence lands in one Mars data folder on the teacher's machine, which the per-student export and the privacy rules depend on; one pause button means no group loses class time. Market appeal: none for home players (standard play is one planet); for clubs, LAN play still works informally, just without teacher tools.
- **Revisit if:** the Stage 3 load test shows one modest machine can't carry 4 planets × 4 players; or a teacher walkthrough finds one group's crash taking down the class is common.
- **What "not supported" means for C:** nothing blocks "Open to LAN" in code. The educator guide says it is not a classroom setup, because the logs would sit on a student PC.

A gives the teacher one thing to start, pause, back up, and export, which is the Feasibility target (setup under 30 minutes). B needs no extra code, because a one-planet server *is* the standard mode, so it is a free fallback for schools with spare machines or when one group's problems must not affect others. C is not supported as a classroom mode (logs end up on student PCs), though LAN play still works for clubs and homes.

**Assumptions to challenge:** a class has ≤ 8 groups (≤ 40 students); the school has one machine that can be the server; groups don't need to visit each other's planets; identical terrain across planets is a plus, not a minus.

**What would settle it:** a Stage 3 load test (the slice, 4 planets × 4 simulated players, on modest hardware), and one teacher walking through setup.

### 6.2 How option A works

- **Dimension slots.** The mod's built-in datapack declares 8 planet dimensions. Minecraft creates dimensions from data when the world loads, so the count is fixed per world. `planets.count` in config (1–8, default 1) says how many are open. Unopened planets get no players and load no chunks; their cost is small but *to measure* (an empty level still exists in memory).
- **Same seed, same terrain.** All planets use the world seed, so every group starts on the same map. Debriefs can compare "why did group 2's pressure climb faster?"
- **Assignment.** The teacher assigns players to planets in the teacher overview or with `/mars planet assign <player> <n>`. Optional join codes (`/mars join <code>`) let students pick their group themselves. Assignments live in `planets.json` (Mars data), so a rollback never moves a student to another group.
- **Lobby: the Training Hall** (story bible §3.6, Lead Game Designer). Fits the plan at low cost:
  - **Where:** the vanilla overworld, which must exist anyway and already has an Earth sky. A small hall structure is placed at the world spawn when a class world is created (a structure file from data). Standard play (`planets.count = 1`) never shows it.
  - **Rules:** while a player is in the overworld and unassigned, damage, hunger, block breaking and placing, and item pickup are cancelled by event handlers; the inventory is cleared on assignment. **No learning events** fire there (story bible: "no message, forecast, or log event"); the only log line is the administrative `planet.assigned` (pseudonym, planet), needed so the teacher's export shows group membership.
  - **Terminal block:** opens a code field and runs the same join logic as `/mars join <code>`.
  - **On assignment:** the client shows the two opening cards (text from language files), then the player is moved to the planet's shelter spawn and Landing begins.
  - **Server cost:** the overworld keeps only the hall loaded; set the `spawnChunkRadius` game rule to a small value for class worlds (**rule name from knowledge; check in Stage 1**). No ticking machines, no mobs (mob spawning off in the hall area).
  - **Build cost (estimate, unverified):** ~3–4 developer days plus the hall structure (Lead Game Designer and Lead Artist).
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
- **The daylight cycle is cosmetic.** Sleeping, `/time set`, or a frozen sky never move simulation time. Decided (Lead Game Designer, round 2): the Mars sky follows simulation time and sleeping never skips Mars time; implementation in 2.14.

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

**Built so far (slice 2):** the flag, the freeze, the new session number on resume, the saved pause. `session.paused` and `session.resumed` are written to the server log until the learning layer exists. Third-party machines stopping under `/tick freeze` is still **unverified** (needs Mekanism and Create on the test classpath).

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
- **Event types** (the Lead Instructional Designer decides which ones measure which goal): `session.*` (including `session.player_present`, once per player per session, for `sessions_present`), `gate.reached`, `condition.changed`, `milestone.reached`, `hazard.*`, `failure.reported`, `failure.fixed`, `failure.repeat`, `shiftboard.claimed`, `shiftboard.released`, `post.chosen` (colony post per phase), `monitor.viewed` (with the page), `prediction.issued`, `prediction.answered`, `prediction.outcome_visible`, `prediction.resolved`, `explain.answered`, `journal.opened`, `message.read`, `feedback.shown` (with hint level), `record.goal_shown`, `report.submitted`, `machine.built`, `machine.removed`, `machine.toggled`, `drift.started`, `drift.answered`, `admin.*`, and from round 3: `briefing.opened`, `worked_example.opened|collapsed|skipped_by_doing|hidden`, `guidance.changed`, `planet.assigned`, `interrupt.*` (9.6).
- **Every event has a `scope`**: `player` (the actor's own action; `player` is set) or `planet` (a group event; `player` is null). Section 2.8 and 2.6.1 give the actor rules for Shift Board and forecasts.
- **Drift answered:** `drift.answered` is logged for each player whose own action (build, toggle, repair, rate change) on a machine contributing to the drifted parameter, in the correcting direction, happened while the drift was active, if the value then returned to band. Machines remember their last actor (1.5), so this is a lookup, not a search.
- **Major builds and Monitor checks:** machines flagged `"major": true` in data count as major builds. `monitor_checks_before_builds` counts major builds where the same player viewed the Monitor within `learning.monitorCheckWindowSols` (default 0.25 sol, about 5 minutes) before. The window is the Lead Instructional Designer's number to tune.
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
- **Triggers in data** (`data/mars/learning/record/*.json`): goal ID, player-language text key, and rules (event type, conditions, count).
- **Scope rule (Lead Instructional Designer decision, LEARNING.md 5.5): every goal page A1–D2 fills only from `scope: player` events** (the player's own action). The loader **rejects** a goal-page rule that matches `scope: planet` events, so content can't break the rule by mistake. Planet events (gates, first storm survived, breathable held, equilibrium, beacon) fill the record's separate **"Cairn team"** section, which never sets `<goal>_shown`.
- Events with `cause: admin` or `debug` never fill a goal.
- **Client:** the journal screen shows the Keeper's Record; the server sends only the player's own record.
- **Teacher overview:** per planet, each student's goals shown, forecast accuracy by phase, current gameplay phase. Students appear by in-game name for online players (names are already visible in game) and by ID otherwise. Nothing from it is written to disk on the teacher's client.
- **"Needs a look" flags** (LEARNING.md 3.7; teacher-only, never shown to students, never a grade, never exported as a grade). Rule-based, computed **per session and per phase** from data already in the learning records: own forecast accuracy below 1 in 3 after 3 forecasts in a phase; the same failure cause recurring twice after the student's own fix; a Keeper's Report unsent for more than 2 sessions or past `interrupt.debriefMaxDefers`; no own events for a whole session while present. Rules and numbers live in `data/mars/learning/flags.json`. **Cost:** a few counters per player, re-evaluated when one of the counted events arrives; no scan. Shown as a small marker with the reason in words.
- **Printable player ID slips** (educator guide §4): a teacher-overview button writes `exports/<timestamp>/id-slips.html`, one cut-out slip per student with the pseudonymous ID in large type and a line "Write this ID, never your name, on tests". **The file contains IDs only, no names**; the teacher hands slips out in the room. (Names would need the mapping, which nothing written to disk may contain.) Plain HTML so any browser prints it; no extra library.

### 8.6 CSV export

- **Command:** `/mars export [planet n | all]`, or a button in the teacher overview. Writes to `mars-data/<worldId>/exports/<timestamp>/`.
- **Files and columns: final** (Lead Instructional Designer's list, LEARNING.md 5.5, adopted 2026-11-26). Phase columns use `_p1`, `_p2`, `_p3`; goal columns repeat for A1–D2. Columns marked *(TL addition)* are proposed additions for the Lead Instructional Designer to accept.

| File | One row per | Columns | Where each value comes from |
|---|---|---|---|
| `players.csv` | Student (pseudonymous ID) | player_id, planet, band, group_size, sessions_present, sols_played, post_p1, post_p2, post_p3; forecasts_answered_p1..p3, forecasts_correct_p1..p3, forecast_accuracy_p1..p3, forecasts_expired, forecasts_late *(TL addition)*; b3_criterion_met (yes/no/not enough data); explain_correct, explain_attempted; failures_claimed, failures_fixed, repeat_failures_after_own_fix; major_builds, monitor_checks_before_builds; drift_events_answered; forecasts_interleaved_answered, forecasts_interleaved_correct, forecasts_scaffolded, forecasts_replay *(TL addition)*, worked_example (used / collapsed / skipped_by_doing / hidden), briefings_opened, guidance (the player's level; for a planet-set level, the planet's); report_1, report_2, report_3, report_final (submitted yes/no); per goal: `<goal>_shown`, `<goal>_sol`, `<goal>_evidence` | Action log (2.6.1, 2.6.2, 2.8, 8.3) and the learning record. `worked_example` from `worked_example.*` events; `briefings_opened` from `briefing.opened`; `guidance` from the planet config and the player's choice. `post_pN` from `post.chosen`. `<goal>_shown` only from `scope: player` events (8.5). B3 threshold and "enough data" minimum come from data (`learning.b3`), not code |
| `events.csv` | Log event | timestamp, session, planet, timeline, superseded (yes/no), sol, player_id (empty for planet events), scope (player/planet), band, event_type, cause, prompt_id, result (correct/incorrect/expired/late/void/replay/none), interleaved, scaffold, guidance, goal_ids, ngss_codes, detail columns flattened from `data` | Action log. `void` is a TL addition (2.6.1) |
| `planets.csv` | Group planet | planet, preset, group_size, band, guidance, guidance_changes (count; the level can change mid-unit), third_party_mods (on/off), current_phase; per gate: `<gate>_reached`, `<gate>_sol`, `<gate>_session`; storms_survived, core_failures_per_storm, repeat_failures_p1..p3, drift_events, drift_events_answered, hold_progress, shrouds, lures, quiet_zones, defenses, thawling_population_stage, rollbacks | A snapshot of planet state taken on the server thread at export time (gates, counters, fauna options) plus the action log |
| `goals.csv` *(new)* | Goal ID | goal_id, record_page_title (colony language), ngss_ms, ngss_hs, practice_only (yes/no), priority_tier, scope | `ngss_map.json` (codes, practice_only) plus three fields it doesn't have yet: `tier`, `scope`, `record_title_key` (handoff to the Lead Instructional Designer). The title is read from the English language file inside the mod jar, so it matches what players see |
| `reflections.csv` | Written answer | player_id, planet, band, report_id, goal_ids, sol, text, d1_score, d2_score, b5_score, names_replaced *(TL addition)* | Learning record. Rubric columns are written **empty** for the teacher to fill (rubrics: `docs/learning/rubrics.md`). `names_replaced` = how many names were swapped for IDs (8.7), so a teacher knows to read that answer with care |

- **Reflections default (user decision, 2026-11-26):** `export.includeReflections` is **on in the classroom preset and off in the standard preset**. "Off" means the file isn't written; the answers still sit in the player's own learning record so the player can reread their reports in the journal. Names are scrubbed to IDs (8.7). Next to every reflection question the game shows "This goes in your report. Don't write your name or personal details." (text: Lead Game Designer and Lead Instructional Designer). If a teacher turns the file off, the educator guide's paper worksheet covers D1, D2, and B5.
- **NGSS codes** come from `data/mars/learning/ngss_map.json` (goal ID + band → codes). The Lead Instructional Designer and SME own its content; codes change there, not in code.
- **IDs only.** No username column exists, and there is no option to add one (user decision).
- **Safety:** UTF-8 with a byte-order mark so spreadsheets open it correctly; any cell starting with `=`, `+`, `-`, or `@` is prefixed with `'` to block spreadsheet formula injection. Free text is quoted per RFC 4180 (line breaks kept inside quotes).
- **Cost:** a planet-state snapshot on the server thread (microseconds), then the session files are read once on a background thread; the game keeps running.

### 8.7 Name scrubbing in free text

**Feasibility answer** (Lead Instructional Designer handoff, LEARNING.md 5.5; user decision "names scrubbed to IDs"): **feasible for Minecraft usernames, not for real names.**

**How it works**
- **Twice:** when an answer is submitted (so stored text never holds a username) **and again at export** (to catch players who joined the world after the answer was written).
- **Name list:** the current usernames of every player in this world's identity mapping, looked up from the server's own profile cache. Usernames are 3–16 characters of letters, digits, and `_`.
- **Matching:** split the text at anything that isn't a letter, digit, or `_`; compare each piece case-insensitively against the name list; replace a match with that player's ID (`P-7K3QX`). Any name on the server is replaced, not only the writer's own group.
- **Boundary:** the scrubber lives inside learning's identity component and is reached only through the `NameScrubber` interface (1.3). The export code gets text back, never the mapping, so "the export never reads the mapping file" still holds. An ArchUnit rule enforces it.
- **Cost:** one hash lookup per word; well under a millisecond per answer, at a natural pause.

**What it can't do** (the educator guide must say so)

| Not caught | Example | Mitigation |
|---|---|---|
| Real names, nicknames | "Me and Sarah fixed the dome" | The on-screen "don't write your name" note; teacher reads before sharing; paper fallback |
| Misspelled or split usernames | "Stevee", "Ste ve" | Same |
| Other personal details | School, town, age | Same |
| **False positives** | A username that is also a word, e.g. a player called `Water`: every "water" in every answer becomes an ID and the science answer gets harder to score | Replace anyway (privacy first); `names_replaced` tells the teacher; the teacher overview shows the original only on the server |

- **Considered and rejected:** a teacher-entered list of real first names to scrub. It would store students' real names on the server, which the pack otherwise never holds.
- **Decision rationale:** privacy is a must-pass for minors; scrubbing usernames is cheap and catches the most likely leak (students naming teammates by their in-game names). It doesn't make free text anonymous, so the separate file, the teacher switch, and the on-screen note all stay. **Revisit if** playtest answers show frequent real-name use (then strengthen the on-screen note) or frequent false positives (then let the teacher see the unscrubbed answer in the overview).

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

### 9.6 Interruption control (decided: option I-C)

Implements TEAM.md 8.6 and LEARNING.md §4.6 (user decision, option I-C, as the **Stage 3 test configuration**). One per-player `InterruptionGate` on the server decides, for each queued item, whether it may pop up now, go only to the colony log, or wait. The client's cue queue (2.12) applies the same decision to sounds.

**Config keys** (server TOML; numbers are starting values, final numbers set from Stage 3 action-log counts):

| Key | Standard | Classroom | Rule |
|---|---|---|---|
| `interrupt.popupMinIntervalMin` | 10 | 10 | At most one non-urgent pop-up per ~10 min of **play** per player (rule 4). Time counts only while the player is online and the session runs |
| `interrupt.logLinesPerMin` | 3 | 3 | Colony-log lines per player per minute; extra lines wait |
| `interrupt.logMergeWindowSec` | 60 | 60 | Similar lines (same message type and cause) inside the window merge into one ("Seal breach ×3") |
| `interrupt.reportReofferPerSession` | 1 | 1 | An unsent Keeper's Report is re-offered at most once per session, plus the "Since last shift" recap line |
| `interrupt.debriefMaxDefers` | 2 | 2 | After 2 deferrals the Keeper's Report **stops being offered as a pop-up**: it stays in the side panel, the "Since last shift" line still names it, the teacher overview flags it ("Needs a look"), and it **never blocks**. Logged as `interrupt.deferred` with `reason: max_defers` |
| `session.wrapUpMinutes` | — | 5 | After the teacher's **wrap-up signal** (`/mars wrapup` or the overview button, or automatically this many minutes before a scheduled end if the teacher sets one), no new forecast and no report pop-up, no new critical failure from drift or fauna scheduling (rule 5). Life-threatening warnings still fire |
| `interrupt.dangerWindowSec` | 10 | 10 | "In danger" = took damage, an active hazard at the player's position, or O2 low within this many seconds. Non-urgent items wait (rule 1) |
| `interrupt.buildingWindowSec` | 5 | 5 | "Actively building" = placed or broke a block within this many seconds |

**Order in the queue:** forecast results first, then failure reports, then story messages, then journal entries. Within a type, oldest first.

**Natural pauses** (rule 2) are server-visible events: entering a sealed volume, sleeping, opening the Monitor or journal, a gate reached. The client reports "screen opened" as an action packet; the server decides.

**Never pauses the group.** Nothing a single player is shown blocks anyone else: no world pause, no shared modal, no "waiting for all players". Only the teacher's pause stops a planet (7.2).

**Logging** (LEARNING.md §4.6 rule "every interruption, deferral, and expiry logged"): `interrupt.shown` (item, kind: popup / log / blocking), `interrupt.deferred` (item, reason: danger / building / cap / wrapup / player_deferred), `interrupt.expired` (item; forecasts expire as `expired`, never wrong: 2.6.1), `session.wrapup` (teacher signal). All with the player's pseudonym.

**Cost:** a few timestamps per player and a small sorted queue; checked when an item is queued and at each natural pause. No per-tick work beyond the danger flag, which the hazard code already sets.

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
- **Teacher commands** (`mars.teacher`): `overview`, `pause`, `resume`, `wrapup`, `backup`, `rollback`, `planet open`, `planet size`, `planet assign`, `phase set`, `band set`, `guidance`, `export`, `who`, `forget`, `forget-all`. ID slips are a button only (no names, IDs only). All are also buttons in the teacher overview, so no teacher needs to type commands or edit config files (risk R-18).
- **Debug commands** (`mars.debug`, and only when `debug.enabled = true`): `set`, `latch`, `unlatch`, `step`. Used for the Stage 2 debug requirement (set every Index value).

---

## 11. LMS Integration (after release, opt-in only)

**Decision being implemented (user, 2026-11-26):** LMS integration comes after release and is opt-in. Nothing is built for it before release. This section records the design so the pre-release code doesn't block it.

**Rules that never change:** off by default; no student data leaves the server unless the teacher turns it on for that world; only pseudonymous IDs ever leave; a separate privacy review happens before it is built (risk R-22).

| Standard | What it is | Fit for us | Source |
|---|---|---|---|
| **xAPI 2.0** | Learning event statements ("actor, verb, object") sent to a Learning Record Store (LRS). Published as **IEEE 9274.1.1-2023**. | **Good.** Our action log is already event statements. An exporter maps each event to an xAPI statement with the pseudonym as an account name, e.g. `{"account": {"homePage": "urn:mars:<worldId>", "name": "P-7K3QX"}}`. | ADL news, 2023-11-22 ([link](https://adlnet.gov/news/2023/11/22/xAPI-Officially-Becomes-a-Published-IEEE-Standard)); IEEE record ([link](https://standards.ieee.org/ieee/9274.1.1/7321)) |
| **LTI 1.3 (1EdTech)** | Lets an LMS launch a web tool and receive grades | **Poor.** It assumes a web app launched from the LMS. A Minecraft client isn't one. Would need a companion web service, which we don't want. | 1EdTech LTI Core v1.3.1, named in an xAPI resource list; **details unverified** |

**Decided (owner, round 3): an offline LMS converter as a Stage 5 prototype.** A small script **outside the pack** turns an exported `events.csv` into xAPI statements and loads them into a **local test LRS** on the developer's machine, so we learn the mapping problems before release. It never ships, has no network sending, and uses only the pseudonymous IDs already in the export; no student data leaves the machine (in a design exercise there is none, but it's built as if there were). Done when: every event type in 8.3 maps to a statement or is listed as "not sent", and the statements validate in the test store (tool to choose in Stage 5).

**Planned path (post-release):**
1. **File export first:** an xAPI JSON file the teacher downloads and imports into the school's LRS by hand. The teacher decides what leaves.
2. **Optional live sending:** only to an LRS address the school configures, with credentials the school provides, behind a world-level opt-in that the teacher turns on. Only pseudonymous statements. No free text by default.
3. **No LTI** unless a school need appears that xAPI can't meet.

The **export interface** (section 5) is the only hook needed now.

---

## 11a. Embedded Assessment: Feasibility and Cost

The game is software, so it can be instrumented. Some tracking is **simple** (one player picks one of a few choices); some is **hard** (a team's coordinated decisions in a changing world over time). Group planets are the hard case. This section says what we can afford to measure and what we log so harder readings stay possible later.

### 11a.1 Simple vs. hard evidence

| Evidence | Difficulty | Feasible now? | Cost | Why |
|---|---|---|---|---|
| Forecast answers and results (2.6.1) | Simple | Yes | One event per answer | One player, fixed choices, known correct answer |
| "What caused it?" answers | Simple | Yes | One event per answer | Same |
| Monitor checks before own major builds (8.3) | Simple | Yes | One event per view and build; a window comparison at export | Same player, two timestamps |
| Shift Board claims and fixes, repeats after own fix (2.8) | Simple to moderate | Yes | One event each; one map lookup for repeats | The server knows the actor of every repair |
| Drift events answered (8.3) | Moderate | Yes | One lookup of the machine's last actor | Attribution is a rule ("own action, right direction, value returned"), not a judgment |
| Keeper's Report text | Simple to collect, hard to score | Collect: yes. Score: by the teacher with rubrics | Text storage | Machine scoring of free text is out of scope |
| **How a group divides work, plans, and recovers together** (who noticed, who decided, who acted, in what order) | **Hard** | **Not scored by the game.** Logged so a teacher or a later study can read it | Nothing extra beyond the fields below | Needs judgment of context; the teacher overview and debriefs cover it |
| Whether a player's mental model improved (B3 trend) | Moderate | Yes, as a simple per-phase accuracy rule (LEARNING.md 0.6) | Export arithmetic | A rule, not a model |

**What every event carries so the hard readings stay possible later:** wall-clock timestamp **and** simulation tick (ordering within and across sessions), session and timeline (rollbacks), planet, actor pseudonym or `scope: planet`, cause (player / simulation / admin / debug), the machine ID or failure ID it concerns (so actions can be chained: report → claim → fix → repeat), and the player's band. Events are written in the order they happen on the server thread, so the order in a file is the true order.

### 11a.2 The four purposes, and where each consumer finds the data

| Purpose | Consumer | Data used | Where it sits | Status |
|---|---|---|---|---|
| Inform the instructor | Teacher | Per-planet and per-student summaries | Teacher overview (live, on the server) and CSV export (`players.csv`, `planets.csv`, `events.csv`) | Designed (8.5, 8.6) |
| Tailor in-game instruction | The game | Per-player fading state (2.10), phase-level fading, grade band | Learning record on the server; read by `FeedbackState` | Designed. Same data as fading; **no learner model** (TEAM.md "adaptive": no automatic personalization at launch) |
| Feedback to the student | Player | Own Keeper's Record pages, forecast results, failure reports | Synced to the player's own client only | Designed (8.5, 9) |
| Evaluate against standards | Teacher, learning study | Goal evidence + NGSS codes + rubric scores | `players.csv` goal columns, `goals.csv`, `reflections.csv` rubric columns, pre/post test outside the game | Designed (8.6) |

### 11a.3 Model-based assessment (e.g. dynamic Bayesian networks)

**Not in scope** (Technical Lead recommendation, for the Lead Instructional Designer to confirm). Reasons: it needs calibration data from real players we won't have in a design exercise; a model's output is harder for a teacher to trust or explain than "own forecast accuracy went from 40 % to 70 %"; and it adds a component to validate. The event log above is rich enough (actor, order, timing, context IDs) to fit such a model **offline, later**, from exported `events.csv` files, with no game change. **Revisit if** a study partner wants it after release.

### 11a.4 Integration points and their tests

| Integration point | What can break | Test | When |
|---|---|---|---|
| Mekanism and Create machines | Pause doesn't freeze them; their machines bypass our contribution API | GameTest: pause, check their machines stop; GameTest: a KubeJS-wired contribution changes the Index | Stage 2 |
| KubeJS recipe gating | A gated recipe is craftable before its gate; a preset (`pace.recipePreset`) loads the wrong set | Server boot test per preset that lists recipes before and after latching each gate | Stage 2–3 |
| packwiz pack and launchers | Missing mod, wrong version, a non-redistributable file in the bundle | CI: export `.mrpack`, check every mod's licence row (docs/tech/mod-licenses.md), boot a server from the export | Stage 1 onward |
| Class server (option A, several planets) | Planet assignment, per-planet rollback, load | Stage 3 load test (4 planets × 4 simulated players); rollback test on one planet | Stage 3 |
| Export files ↔ spreadsheets | Encoding, formula injection, broken quoting of free text | Unit test: write a session with tricky text (accents, quotes, line breaks, `=SUM(...)`), open in LibreOffice headless, compare | Stage 3 |
| Export ↔ privacy | A UUID or username appears in a file | Privacy test (section 3): fails on any UUID pattern or known test username in `exports/` and `actionlog/` | Every build |
| Educator guide ↔ game | Guide's column names, commands, or screenshots go stale | Doc check at each stage exit: every command and CSV column in the guide exists in the build | Stage 4 onward |
| LMS file export (after release, opt-in) | Statements a school's LRS rejects | Validate generated xAPI statements against an LRS conformance tool (**tool unverified**) | Post-release |

### 11a.5 Teaching the teacher, and support after release

- **Server and setup guide** (DOCUMENTATION.md lists it as missing; owner: Technical Lead; planned at `docs/tech/server-setup.md`, Stage 4): install, first start, assigning planets, pause/resume, backups and rollback, export and what each file means, deleting the ID mapping at the end of a unit, and troubleshooting (child account can't join, firewall port, out of memory). Target: a teacher who has never run a server is done in under 30 minutes (Scorecard Feasibility row).
- **Educator guide** (Lead Instructional Designer) links to it and covers reading the export, the rubric columns, and the limits of name scrubbing (8.7).
- **Support after release:** estimate in [docs/tech/server-setup.md](docs/tech/server-setup.md) §8 (all numbers **unverified**, for the Producer's budget).
- **Server and setup guide:** drafted as [docs/tech/server-setup.md](docs/tech/server-setup.md) (Stage 4 deliverable; draft now so the Stage 3 slice can be set up from it).

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
| Terrain random ticks (ice, soil) | Vanilla random ticks | One read of the chunk's cached heat sources, a falloff for at most 2 sources, and one comparison with the boiling point: about 10 arithmetic operations per random tick on our blocks (2.5) |
| Chunk heat map, fauna pressure F, ending-card counters | When a machine changes state, or on the counted event | Recompute ~9 chunks or one running sum; no ticking |
| Action log | Per event (buffer) + every 5 s (disk) | Small allocation; batched writes on a background thread |
| Backups, rollback, export | Session start / on command | Background thread or server stopped; never during play |
| Client sky and fog | Per frame (client) | Interpolation between cached keyframes; no lookups |
| Signals (storm, surface radiation, fauna pressure) | Each Index step | One value set per signal; travels in the existing snapshot |
| Next-gate display, Monitor disclosure | Each Index step / on gate change | A few subtractions for one gate; one condition check per readout on gate change |
| Feedback fading lookup | Per message per recipient | One map lookup |
| Name scrubbing | Per reflection answer, and at export | One hash lookup per word |
| Repeat-failure and drift attribution | Per failure or drift event | One map lookup |
| **Client:** clouds (2.5.3) | Per frame | Same as vanilla clouds; mesh rebuilt only when coverage changes a step |
| **Client:** heat shimmer particles (2.5.4) | Per visible running heat source | A few particles, inside vanilla particle limits. No post-process shader |
| **Client:** weather mast renderer (2.11) | Per visible mast per frame | A few dozen quads; zero server cost |
| **Client:** ambient sound (2.12) | Per client tick | 3–5 looping sources, one volume update each; cue queue check |
| **Client:** thawling animation (2.13) | Per visible thawling per frame | Vanilla keyframe animation, same as vanilla animated mobs |

The large costs are vanilla and third-party: loaded chunks, mobs, and Mekanism/Create machines. Those are controlled by view and simulation distance, fauna caps, and recipe gating (section 6.3). A **load test** in Stage 3 (four planets, four simulated players each, on modest hardware) replaces the *to tune* numbers with measured ones.

---

## Open Questions

| Question | Owner | Status |
|---|---|---|
| Fixed simulation step size: is 5 seconds right, or should it be configurable? | Technical Lead | Keep 5 s; make it a config value hidden from teachers. Revisit after the Stage 2 prototype. |
| Do we need a public API for other mods at launch? | Technical Lead | No. Internal API only at launch; the contribution API is designed to be opened later. |
| Build tooling: NeoForge ModDevGradle with Gradle; a mono-repo with the pack and the mod | Technical Lead | Assumed; confirm when the Stage 1 skeleton builds. **CI decided (owner, round 3):** basic CI at Stage 1 exit for layers L0–L2 |
| Does `/tick freeze` stop Mekanism and Create machines? | Technical Lead | Stage 2 GameTest; fallback "pause = stop the server" |
| Rollback restore: NeoForge startup hook or start script? | Technical Lead | Stage 2; start script is the safe default |
| Hosting option A vs. B for classrooms | User | **Decided:** A, fallback B, no LAN for classes (6.1) |
| Memory and CPU per open planet; view-distance and fauna caps | Technical Lead | Stage 3 load test |
| Event list and CSV columns for each goal ID | Lead Instructional Designer | **Final** columns adopted (8.6); three TL additions (`forecasts_late`, `void`, `names_replaced`) await acceptance |
| Minecraft version: stay on 1.21.1? | User | **Decided:** yes (3.1) |
| Does the cloud renderer and shimmer particle work with Sodium for NeoForge? Do we ship a performance mod at all? | Technical Lead | Stage 2 test; licence check of Sodium (PolyForm Shield) in mod-licenses.md |
| Do storm caps scale gameplay effects or only visuals? | Lead Game Designer | **Decided:** both (2.5.2) |
| Shift Board credit: claimer or actor? | Owner | **Decided:** actor, the player who did the repair (2.8) |
| CO2 on the weather mast before the Monitor shows it | Owner | **Decided:** gas gauges fitted at the Phase 3 gate (2.11) |
| Blockbench animation export for 1.21.1 | Technical Lead | **Resolved:** NeoForge JSON animations + Blockbench "Animation to JSON Converter" (2.13). Model export template still to test in Stage 2 |
| Does copying `pack/assets` and `pack/data` into the jar keep `/reload` usable in dev? | Technical Lead | **Likely yes:** ModDevGradle runs load the mod's source set output, which includes `processResources` output; rerun `processResources`, then `/reload` (build plan §4). Confirm in the skeleton |
