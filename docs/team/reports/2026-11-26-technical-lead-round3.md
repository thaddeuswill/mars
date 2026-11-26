# Report: Technical Lead Round 3

- **Role:** Technical Lead
- **Date:** 2026-11-26
- **Outputs:** `ARCHITECTURE.md` (new §2.6.2, §2.14, §2.15, §2.16 and many updates), `docs/tech/server-setup.md` (new), `docs/tech/mod-licenses.md`, `docs/tech/school-deployment.md`, `docs/tech/test-strategy.md`, `docs/tech/build-plan.md`
- **Perspective round:** Round 3 succeeds if every owner decision becomes something we can build and test for a stated cost, and the rechecks either confirm or correct what we assumed with the network blocked.
- **Note:** Documents only; nothing was built or run. Rechecks read primary sources: the Modrinth API, raw.githubusercontent.com (NeoForge 1.21.1, Create, Registrate, JUnit, ArchUnit, PIT, ModDevGradle, packwiz, Blockbench plugin list), docs.neoforged.net (1.21.1), and the Minecraft Education help centre. Still blocked: curseforge.com, api.curseforge.com, support.modrinth.com, NeoForge's Maven and GitHub history pages.

## Owner decisions applied
Shift Board credit to the repairer (`actor`); `guidance` setting with its four conditions; orbital mirror route; Mekanism and Create optional and late; mast gas gauges at the Phase 3 gate; basic CI at Stage 1 exit (layers L0–L2); offline LMS converter in Stage 5.

## Handoffs received (all accepted)
- **Instructional Designer:** new export columns and `guidance` in `planets.csv` and `players.csv`; Monitor minimum view 1 s; teacher-only "Needs a look" flags from `flags.json`; printable player ID slips (HTML, IDs only, never names); forecast tags `interleaved` and `scaffold` and a `replay` result after a rollback (not scored); `debriefMaxDefers` behavior (after 2 deferrals the report sits in the side panel, the teacher overview flags it, it never blocks); a feasibility answer for every content rule. All are automatable except the manual list in content-rules.md §5. CR-H5 works only partly (needs a `names` field on briefing lines). The `reveals` field and check CR-F9 work (the interruption gate holds any revealing message until that player's forecast locks). F4a's outcome check works, with a build test that the puddle-drying time stays under 0.25 sol.
- **Game Designer:** sleep never skips Mars time, `/time set` is overwritten every tick, the sky follows the simulation clock (verified on the 1.21.1 branch); Mekanism and Create are left out of the classroom bundle (recipes can't be locked per planet); when installed, their blocks are blocked per planet until Hydrosphere; machine list and orbital mirror costed; Training Hall lobby sits in the vanilla overworld (about 3–4 developer days); the look-first forecast card.
- **Lead Artist:** new signal `mars:solar_spike` for the aurora; log chimes merge within 10 s; storm and solar-event warnings play during danger; extra `wind_full_helmet` bed; alarms and feedback cues in the master sound category so they can't be muted via another category (1.21.1 has no interface category, from knowledge; check in the skeleton); a glyph-existence test and a text-on-real-background contrast check in the screenshot run; nine-slice GUI sprites, mono positional sounds, and vanilla keyframe animation JSON (`assets/<ns>/neoforge/animations/entity/`, via Blockbench's "Animation to JSON Converter") all verified for 1.21.1.
- **Coordinator rechecks:** FakePlayerFactory verified on the 1.21.1 branch; `/reload` in dev likely fine (confirm in the skeleton); earliest NeoForge 21.1.x with chunk attachment sync not found, so chunk heat is sent by our own packet (the documented 1.21.1 path); **Minecraft Education really can't run community mods** (FAQ updated 2026-11-26, verified).

## Corrections to earlier work
- **Create's jar contains its All Rights Reserved assets**, so our offline classroom bundle may not include it. The round 1 note that every "OK" mod could be bundled was wrong for Create. A `.mrpack` pointing at Modrinth's copy is still fine, and Create is now left out of the classroom bundle anyway.
- **KubeJS 2101.7.2 also requires Better Advanced Tooltips (MIT)** as well as Rhino. Round 1 found only Rhino.

## Cost estimates (unverified; developer-days for one experienced NeoForge developer; code and data only, no art or audio)
| Item | Estimate |
|---|---|
| Shared machine framework | 12–18 days |
| About 21 machines | 40–53 days |
| **Machines total** | **about 52–71 days** |
| Biome seed kits (largest single item) | 8–12 days |
| Beacon and ending | 6–8 days |
| Orbital mirror | 4–6 days |
| Transit tubes as simple paired stations | about 4 days (rideable: 8–12) |
| Support after release | about 3–4 developer-weeks per year plus teacher setup questions; a port to a newer Minecraft version is a separate project |

## Decisions needed (classified by coordinator)
| Decision | Recommendation | Type |
|---|---|---|
| CI host | GitHub Actions, since the repo is on GitHub (free-tier limits unverified) | Well-structured |
| Allow `curseforge.com` and `support.modrinth.com` | Allow before Stage 7, not now (only needed for a CurseForge export or Modrinth's wording on what a modpack may include) | Well-structured |

## Handoffs
- **Game Designer:** a `reveals` list on every message that could give a forecast away; a `lock` condition on every forecast; `rests_on` (the physical thresholds a forecast or beat depends on); a `names` field on Shift Briefing and worked-example lines; a Phase 1 gas-mix source (now landed); confirm transit tubes as simple paired stations; the Training Hall structure with the Artist.
- **Instructional Designer:** accept or reject `forecasts_replay`, `guidance_changes`, `third_party_mods` columns; note in the educator guide that ID slips carry IDs only (the teacher fills in names by hand); put the "Needs a look" rule numbers in `flags.json`; accept that CR-H5 is only partly automatable.
- **Lead Artist:** animations through the converter plugin into `pack/assets/mars/neoforge/animations/entity/`; GUI panels as nine-slice sprites; aurora keyed to `mars:solar_spike`; alarms and feedback in the master category; gas tubes drawn only after the Phase 3 gate.
- **Producer:** machine cost (52–71 days before art and audio) and support cost into the budget; update R-23 (Create's jar can't go in an offline bundle); schedule the owner platform checklist; route the CI host decision; add `server-setup.md` to the map as a draft.

## Risks
- **Machine scope:** at about 52–71 days before art and audio, the in-house machine set is the largest build item; biome kits and the beacon are the biggest parts.
- **Rendering compatibility:** Sodium has only beta builds for NeoForge 1.21.1, so it isn't suitable for classrooms now; weak lab PCs rest on our own performance budget and the classroom preset.
- **Still unverified:** 1.21.1 sound categories and the `spawnChunkRadius` game rule name (from knowledge); the Blockbench box-model export template for 1.21.1; CurseForge distribution flags.
- Minecraft Education pages conflict about dedicated servers; irrelevant to us, since we can't run there either way.

## Shared model updates
- **Lock point** is now implemented: the `reveals` field holds messages until the lock (ARCHITECTURE.md §2.6.2).
- **Decisions:** the aurora uses its own signal `mars:solar_spike`; Mekanism and Create are left out of the classroom bundle with per-planet block use gated at Hydrosphere; entity animations are NeoForge JSON content; sky time is synced from the simulation clock every tick; chunk heat is sent by our own packet; Training Hall in the overworld; Create never goes in an offline bundle.
- **Lesson:** two "verified" license findings changed once primary sources were reachable. Recheck licenses and dependencies against the exact build we pin at every stage exit, not only once.
