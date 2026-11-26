# Software Architecture

This file covers how the software is built. It addresses five practices:

1. **Architecture:** the components and how they interact
2. **Algorithms:** the logic behind the key systems
3. **Coding practices:** shared style and conventions
4. **Elegant design:** clean responsibilities, no hidden side effects
5. **Future enhancements:** hooks so new features don't need rewrites

> **Draft.** This is written before any code exists. It will be revised during the Stage 2 prototype ([ROADMAP.md](ROADMAP.md)).

---

## 1. Architecture

### 1.1 Components

The pack has three layers. Each has one job.

| Layer | Technology | Responsibility |
|---|---|---|
| **Companion mod** | Java, NeoForge | Systems: the Mars dimension, the Planetary Index, simulation, hazards, fauna, rendering, learning tools |
| **Data layer** | Datapack JSON | Content and tuning: parameters, gates, journal entries, prediction prompts, balance numbers |
| **Script layer** | KubeJS | Recipes, recipe gating, and integration with third-party mods (Mekanism, Create) |

Rule of thumb: **systems in Java, content in data, glue in scripts.** A balance change or a new journal entry should never need a Java rebuild.

### 1.2 Companion mod modules

```
                          ┌───────────────────┐
  Machines, flora,  ───►  │  Planetary Index  │  ◄─── Teacher commands
  storms, fauna           │  (single source   │
  (contributions)         │   of truth)       │
                          └─────────┬─────────┘
                                    │ index updated (event)
          ┌──────────────┬──────────┼────────────┬──────────────┐
          ▼              ▼          ▼            ▼              ▼
     Gate evaluator   Hazards     Fauna     World response   Learning
     (unlocks)        (storms,    (spawn    (sky, fog,       (journal,
                      radiation,  weights)  block changes)   Colony Record,
                      O2)                                    action log)
          │
          └──► gate reached (event) ──► recipes unlock, journal, debrief
```

| Module | Owns | Depends on |
|---|---|---|
| `core` | Public API, events, shared types | Nothing |
| `index` | Planetary Index state, persistence, simulation | `core` |
| `gates` | Gate definitions (from data), evaluation, latched unlocks | `core` |
| `dimension` | Mars dimension, worldgen, terrain response | `core` |
| `hazards` | Oxygen and sealing, dust storms, radiation, machine wear | `core` |
| `machines` | Terraforming machines, Planetary Monitor | `core` |
| `fauna` | Mobs, spawn rules | `core` |
| `client` | Sky, fog, Monitor screen, HUD | `core` (synced data only) |
| `learning` | Journal, prediction prompts, Colony Record, action log | `core` |
| `admin` | Teacher and debug commands | `core` |

Every module depends only on `core`. Modules never call each other directly: they talk through the API and events (section 4). This keeps any module replaceable.

### 1.3 Interfaces (APIs)

The `core` module defines the contracts. Sketch:

```java
// Read and contribute to the planet's state. Server-side only.
interface PlanetaryIndex {
    double get(ParameterId parameter);
    IndexSnapshot snapshot();                       // immutable copy for readers
    ContributionHandle contribute(ParameterId parameter, double ratePerDay, SourceId source);
}

// Machines hold a handle and update or remove it. They never write values directly.
interface ContributionHandle {
    void setRate(double ratePerDay);
    void remove();
}

interface Gates {
    boolean isUnlocked(GateId gate);
}

// Events posted on the NeoForge event bus.
record IndexUpdatedEvent(IndexSnapshot before, IndexSnapshot after) {}
record GateReachedEvent(GateId gate, long gameTime) {}
record HazardEvent(HazardType type, BlockPos origin, Severity severity) {}
record LearningEvent(UUID player, String type, Map<String, Object> data) {}
```

### 1.4 Who can do what

| Actor | Can read | Can change |
|---|---|---|
| Server systems | Everything | Index (through contributions), gates, world |
| Clients (players' game) | Synced snapshot: index values, unlocked gates, own Colony Record | Nothing directly; send actions (e.g. answering a prediction) to the server |
| Players | Monitor, own journal and Colony Record | The world, through normal play |
| Teachers (operator permission) | All players' Colony Records, action log | Pause world, roll back, set phase, set grade band, export log, change pacing config |
| KubeJS scripts | Index, gates | Recipes; add contributions through the same API as machines |

The **server is authoritative**. Clients only render what they're sent, which avoids desync in multiplayer and cheating in classrooms.

### 1.5 Data formats

| Data | Format | Location |
|---|---|---|
| Index state, unlocked gates | NBT (world saved data), with a schema version | World save |
| Parameters, gates, balance numbers | JSON | Datapack: `data/mars/parameters/`, `data/mars/gates/` |
| Journal entries, prompts | JSON plus language files for text | Datapack and resource pack |
| Learning action log | JSON Lines, one event per line, anonymous player IDs | Local file in the world folder; never sent over the network |
| Client sync | NeoForge custom payload packets | Network |

---

## 2. Algorithms

### 2.1 Planetary Index simulation
- **Fixed-step update**, not every game tick. The simulation runs once every 100 ticks (5 seconds). The planet changes over hours of play, so finer steps waste CPU.
- **Contributions are cached, not polled.** Each machine registers a rate once and updates it only when its state changes (on, off, broken). Each step sums the cached rates: cost scales with the number of machines, not with ticks × machines.
- **Coupled update** for each step:
  1. Sum contributions for each parameter.
  2. Apply coupling: temperature gets a greenhouse term based on pressure and CO2; ice melts into hydrosphere only when temperature and pressure are above the threshold for liquid water; biomass converts CO2 to O2.
  3. Apply natural decay (e.g. pressure leaks slowly without upkeep).
  4. Clamp to physical limits.
  5. Post `IndexUpdatedEvent` if anything changed by more than a small amount.
- Simple step-by-step (Euler) integration is enough at this step size. Coefficients live in data so balance can be tuned without code.

### 2.2 Gate evaluation
- Runs on each `IndexUpdatedEvent`.
- Each gate is a condition (thresholds, and optionally a duration, e.g. "O2 in band for 7 days").
- Duration gates keep a counter that resets when the condition fails.
- **Latched:** once reached, a gate is stored as unlocked and never re-evaluated (gates lock in, per DESIGN.md).
- Threshold multiplier from server config applied here, for multiplayer scaling.

### 2.3 Sealing (dome and airlock detection)
- Flood fill from the oxygen source through air blocks, with a maximum volume cap. If the fill hits the cap or reaches open sky, the room isn't sealed.
- **Cached:** result stored per sealed volume and only recomputed when a block changes inside or on the boundary of that volume.
- Runs over a few ticks if large, so it never causes a lag spike.

### 2.4 Fauna pressure
- Machines are stored in a **per-chunk index** (map from chunk position to machines).
- Spawn weight near a position = base weight × f(nearby active emitters) × g(temperature, hydrosphere). Looking up nearby chunks is fast because of the index.
- This implements "fauna scales with infrastructure" from the multiplayer scaling decision.

### 2.5 World response
- **Sky and fog:** client interpolates between colors and densities defined in data, keyed to pressure and O2. Smooth transitions, no per-frame lookups.
- **Terrain changes** (ice → water, regolith → soil): driven by random block ticks with a probability scaled by the index, so the whole world isn't rewritten at once.

### 2.6 Learning
- Prediction prompts: answer stored on the server, then resolved when the predicted condition is reached or a timeout passes.
- Colony Record: each learning goal is a set of trigger conditions listened for on the event bus.
- Action log: events buffered in memory and written to disk in batches on a background thread.

---

## 3. Coding Practices

| Area | Convention |
|---|---|
| Language | Java 21 (required by NeoForge 1.21.1) |
| Formatting | Google Java Style, enforced by Spotless in the build. Formatting is never discussed in review: the tool decides. |
| Package layout | `mars.<module>` matching the modules above; `mars.<module>.internal` for anything not part of the API |
| Naming | Classes are nouns (`DustStorm`), methods are verbs (`applyDecay`), IDs use `snake_case` (`mars:atmospheric_processor`) |
| Side separation | Client-only code only in `mars.client`. The server must run without any client classes. |
| State | No static mutable state outside registries. State lives in the Index, world saved data, or block entities. |
| Nulls | Use `Optional` for "may be missing" return values. |
| Logging | One logger per module; no `System.out`. |
| Testing | **Unit tests** (JUnit) for simulation, gates, and scoring as pure Java, with no Minecraft needed. **GameTests** for in-world behavior (sealing, machines). |
| Scripts | KubeJS: one file per mod being integrated; recipe IDs always explicit. |
| Data | JSON validated at load; a bad file logs a clear error naming the file and field. |
| Git | Small commits with clear messages; feature branches; review before merge. |
| Docs | Every public API type has a short Javadoc. Design decisions go in DESIGN.md, not code comments. |

---

## 4. Elegant Design

The rules that keep components clean and free of hidden side effects:

1. **Single source of truth.** All planetary state lives in the Planetary Index. No system keeps its own copy of "current pressure".
2. **One-way data flow.** Contributions go in, the simulation steps, events go out. Systems that react to events never write back to the Index in the same step.
3. **Talk through events, not direct calls.** Hazards don't know the learning module exists. Learning listens to `HazardEvent`. Adding or removing a module doesn't break the others.
4. **Pure simulation core.** The Index math has no Minecraft dependencies, so it can be unit-tested and reasoned about in isolation.
5. **Content is data.** Code defines *how* gates and parameters work; data defines *which* gates and parameters exist.
6. **Immutable snapshots for readers.** Readers get a copy (`IndexSnapshot`), so nothing can change state by accident while reading it.

---

## 5. Hooks for Future Enhancements

| Hook | What it allows later without rewrites |
|---|---|
| **Parameter registry** | Add a new planetary parameter (e.g. magnetic field strength, soil nitrogen) with a JSON file and a coupling rule |
| **Data-driven gates** | New gates, phases, or a whole new planet's progression from data only |
| **Contribution API** | Other mods or KubeJS scripts add terraforming sources through the same `contribute()` call as built-in machines |
| **Event bus** | Add-ons react to index changes, gates, and hazards without touching core code |
| **Hazard strategy interface** | New hazard types (meteor showers, quakes) plug in as one class each |
| **Learning triggers in data** | New learning goals, prompts, and Colony Record entries without code |
| **Config presets** | Standard and classroom modes are bundles of config values (thresholds, hold times, hazard severity, recipe preset). New presets, e.g. a "hard mode", need no code. |
| **Schema version in saved data** | Saves migrate forward when the data format changes, so worlds survive updates |
| **Planet as a parameter** | The Index is keyed by dimension, so a second world (e.g. a moon) could have its own Index later |

### Design patterns in use
| Pattern | Where |
|---|---|
| Observer (events) | Index updates, gates, hazards, learning |
| Registry | Parameters, gates, hazards, fauna |
| Strategy | Hazard types, coupling rules |
| Handle | Contribution handles that machines own and release |
| Snapshot (immutable value object) | Index reads |
| Data-driven configuration | Gates, parameters, journal, balance |

---

## Open Questions

- Fixed simulation step size: is 5 seconds right, or should it be configurable?
- Do we need a public API for other mods at launch, or only internally?
- Build tooling: NeoForge MDK with Gradle (assumed); a mono-repo with the pack and the mod together (assumed).
