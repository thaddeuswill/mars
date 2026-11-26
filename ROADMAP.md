# Development Roadmap

This file breaks the project into development stages. Each stage lists what must be in place before moving on, so we don't build content on top of a foundation that isn't settled.

The **gameplay** phases (Survival → Geo-Engineering → Equilibrium) are described in [DESIGN.md](DESIGN.md). This file covers **how we build them**.

Status key: ✅ done · 🟡 in progress · ⬜ not started

---

## Design Pillars

Every feature is checked against these. If a feature doesn't serve at least one, cut it.

1. **The planet is the quest log.** Progress is unlocked by world state, not by checklists or NPCs.
2. **Every change is visible.** If a number moves, the player should see or feel it in the world.
3. **Pressure, not grind.** Difficulty comes from upkeep and trade-offs, not from repetitive crafting.
4. **Compact before wide.** Scarcity shapes how players build. Expansion is earned.
5. **Your progress provokes the planet.** Terraforming faster means facing stronger threats.

---

## Stage 0 — Concept ✅

*Goal: know what we're making and why it's fun.*

- ✅ Core concept and loop written down
- ✅ Three gameplay phases defined
- ✅ Design pillars agreed
- ✅ Progress scope: global
- ✅ Target playtime: ~40 hours (pacing split in DESIGN.md)
- ✅ Target audience: singleplayer and small servers
- ✅ Mars world: custom dimension
- ✅ Regression: gates lock in
- ✅ Multiplayer scaling: fauna by infrastructure, plus a gate-threshold config

**Exit criteria:** all open questions answered and recorded in DESIGN.md.

---

## Stage 1 — Pre-production 🟡

*Goal: lock the technical foundation and the numbers before writing content.*

**Technical decisions**
- ⬜ Minecraft version and loader confirmed (proposed: NeoForge 1.21.1)
- ⬜ Every candidate mod checked for that version, plus actively maintained
- ⬜ **Redistribution permissions checked** for each mod (Modrinth/CurseForge allow-lists or the author's license). This blocks release if skipped.
- ⬜ Decide the companion-mod scope: what KubeJS can do vs. what needs Java code
- ⬜ Oxygen, sealing, and suits: find a standalone mod that can read the Planetary Index, or plan to build them
- ⬜ Companion mod skeleton: builds, loads, and registers an empty Mars dimension

**Project setup**
- ⬜ Pack managed as text files (e.g. packwiz) so mod lists and configs live in git
- ⬜ Folder layout: `pack/` (manifest, configs, KubeJS scripts), `mod/` (companion mod), `docs/`
- ⬜ Local dev instance and dedicated test server can be launched from the repo
- ⬜ Basic CI: the pack builds and the server boots without errors

**Learning design** (see [LEARNING.md](LEARNING.md))
- ⬜ Learner audience and curriculum alignment decided
- ⬜ Learning goals finalized and mapped to game content
- ⬜ Pre/post test drafted
- ⬜ Action-log format defined (local only, no personal data)

**Design data**
- ⬜ Balance sheet: every Planetary Index parameter, its rates, machine outputs, and gate thresholds in one table
- ⬜ Rough pacing curve: when each gate should be reached against the target playtime
- ⬜ Content inventory for each gameplay phase: blocks, items, machines, mobs, structures, events

**Exit criteria:** a fresh clone produces a launchable pack and server; the balance sheet exists.

---

## Stage 2 — Prototype ⬜

*Goal: prove the core loop is fun with placeholder art.*

- ⬜ Planetary Index stored in world save data and readable by command
- ⬜ One machine that raises one parameter (pressure)
- ⬜ One visible world response to that parameter (sky tint or fog)
- ⬜ One regression source (machine needs upkeep, or a storm drains pressure)
- ⬜ Debug commands to set and inspect every index value

**Exit criteria:** a playtester can raise pressure, see the sky change, and feel pressure to maintain it. If it's not fun here, rework the loop before going further.

---

## Stage 3 — Vertical Slice (Gameplay Phase 1 complete) ⬜

*Goal: the first gameplay phase at near-final quality, as the template for everything else.*

- ⬜ Mars spawn and starting dome structure
- ⬜ Oxygen, water, and power loop with real recipes
- ⬜ Dome sealing and breach venting
- ⬜ Dust storm event (visibility, solar penalty, machine wear)
- ⬜ Radiation spike event and shielding
- ⬜ Life-support wear and maintenance parts
- ⬜ Planetary Monitor block
- ⬜ "Dome stable" gate, which unlocks the move to Phase 2
- ⬜ Early-game onboarding: does a new player know what to do without a wiki?

**Exit criteria:** an outside playtester gets from spawn to the Phase 2 gate without help, and the time matches the pacing curve.

---

## Stage 4 — Production ⬜

*Goal: build out the remaining gameplay phases using the slice as the template.*

**Gameplay Phase 2 — Geo-Engineering**
- ⬜ Airlocks, transit tubes, outposts
- ⬜ Atmospheric processors, greenhouse emitters, deep-crust extractors
- ⬜ Thaw: ice → water block behavior
- ⬜ Dormant fauna: spawn rules tied to temperature and water
- ⬜ Mutated fauna variants that target infrastructure
- ⬜ Infrastructure defense options

**Gameplay Phase 3 — Equilibrium**
- ⬜ Biome seed kits and the spread of flora
- ⬜ O2/CO2 balancing machines and failure states
- ⬜ Storms weaken over time, and the sky shifts toward a living world
- ⬜ Helmet-off check
- ⬜ Colonization beacon and ending sequence

**Cross-cutting**
- ⬜ Art: textures, models, sounds, sky/fog shaders
- ⬜ Lore journal entries triggered by world-state gates, including science explanations, prediction prompts, and reflection questions
- ⬜ Local action log for assessment
- ⬜ Learner feedback tools: Monitor cause breakdown, failure reports, Colony Record, phase debriefs
- ⬜ Educator guide and worksheets
- ⬜ Performance budget: how many ticking machines a typical late-game base runs, measured on the test server

**Exit criteria:** every gate in DESIGN.md is reachable in a normal playthrough.

---

## Stage 5 — Alpha ⬜

*Goal: feature complete; find what's broken.*

- ⬜ Full playthrough from spawn to beacon by the team
- ⬜ No progression blockers (soft-locks, unreachable gates, uncraftable items)
- ⬜ Multiplayer tested on a dedicated server
- ⬜ Saves survive restarts and pack updates

**Exit criteria:** no known blocker bugs.

---

## Stage 6 — Beta ⬜

*Goal: balance and polish with outside players.*

- ⬜ External playtest group with a feedback form
- ⬜ Pacing tuned against the target playtime
- ⬜ Difficulty of fauna and hazards tuned
- ⬜ Performance on low-end hardware checked
- ⬜ Config options for difficulty or pacing, if needed
- ⬜ Learning study: pre/post test with a playtest group; check against success criteria in LEARNING.md

**Exit criteria:** playtesters finish the pack and the pacing feels right.

---

## Stage 7 — Release ⬜

- ⬜ All redistribution permissions confirmed (from Stage 1)
- ⬜ Pack exported for Modrinth and/or CurseForge
- ⬜ Server pack published
- ⬜ Pack page: description, screenshots, trailer
- ⬜ Changelog and versioning scheme for updates
- ⬜ A place to report bugs (GitHub Issues)

---

## Where We Are Now

Stage 0 is done. We're starting **Stage 1**. The next actions are:

1. Confirm the Minecraft version and check each candidate mod against it.
2. Decide how oxygen, sealing, and suits will work now that Ad Astra is out.
3. Set up the repo layout and a companion mod skeleton that loads an empty Mars dimension.
4. Start the balance sheet, using the 8 / 18 / 14 hour pacing split.
