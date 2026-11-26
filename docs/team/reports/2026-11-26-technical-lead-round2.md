# Report: Technical Lead Round 2

- **Role:** Technical Lead
- **Date:** 2026-11-26
- **Outputs:** `ARCHITECTURE.md` (extended), `docs/tech/test-strategy.md` (new), `docs/tech/build-plan.md` (new), `docs/tech/mod-licenses.md` (licensing plan, Sodium and GeckoLib entries), folder skeletons `pack/` and `mod/`
- **Perspective round:** Succeeds if every learning and art feature can be built cheaply on weak school hardware and the evidence it produces stays attributable, pseudonymous, and never lost.
- **Note:** Documents and folder skeletons only; nothing was built or run. Facts came from primary sources via raw.githubusercontent.com (NeoForge 1.21.1 branch, MDK, licenses). Unchecked items are marked unverified.

## Handoffs received
- **User decisions:** hosting option A with B as fallback and no LAN for classes (§6.1); stay on 1.21.1, with versions pinned from the MDK (NeoForge 21.1.267, ModDevGradle 2.0.148, Gradle 9.2.1) (§3.1); `export.includeReflections` on in classroom mode, off in standard ("off" writes no file; answers stay in the player's own record) (§8.6).
- **Instructional Designer:**
  - Final export columns, including `goals.csv` and empty score columns in `reflections.csv`.
  - Name scrubbing is feasible for Minecraft usernames, not real names (§8.7); it runs at submit and at export behind a `NameScrubber` interface, so export never reads the mapping file.
  - Every goal page is player-scope; the loader rejects planet-event rules (§8.5).
  - Forecast results `expired` and `late` (§2.6.1): exactly one result per player and forecast pair; absent players are not counted as expired.
  - Actor IDs on Shift Board claims and fixes (§2.8).
- **Artist:**
  - Art inputs mapped to real keys (§2.5.2); `mars:storm_strength` exposed as a signal.
  - Clouds: vanilla-style with a density map (3–5 developer days).
  - Heat shimmer: particles only, so it looks the same for every player.
  - `pack/assets/` set up; vanilla entity animation (GeckoLib is the fallback); 420×236 GUI budget confirmed.
- **Audio:** pre-filtered ambient layers cross-faded by pressure; a cue queue with alarm, voice, feedback, and world channels; a build test fails if any sound lacks a subtitle (§2.12).
- **Design feasibility:** progressive disclosure on the Monitor, the next-gate display with a time-to-gate estimate, per-player fading feedback, and the weather mast are all feasible and cheap.
- **Chapter 9 and process:** embedded assessment (§11a; model-based assessment out of scope for now); test strategy; I-C interruption rules with config keys and a teacher wrap-up command (§9.6); licensing plan.

## Decisions needed (classified by coordinator)
| Decision | Recommendation | Type |
|---|---|---|
| A throwaway LMS plumbing prototype before release: an offline converter in Stage 5 turning an exported `events.csv` into xAPI statements for a local test store (never ships; no student data leaves the machine). Earlier decision was "nothing for LMS before release". | Allow it | Ill-structured |
| Basic CI at Stage 1 exit running the fast layers L0–L2 (no Minecraft needed) | Yes | Well-structured |

## Handoffs
- **Instructional Designer:** add `tier`, `scope`, `record_title_key` to the NGSS map; accept or reject the `forecasts_late` column, a `void` result, and a `names_replaced` column; Shift Board credit default ("claimer if claimed, else whoever did the repair"; should a claimer who didn't repair get C2 credit?); time window for "checked the Monitor before a major build" (default 0.25 sol); the fading numbers and `interrupt.debriefMaxDefers` (default 2); confirm no model-based assessment; educator guide states what name scrubbing can't catch.
- **Game Designer:** whether storm caps scale gameplay effects or only the look (recommend both); log or linear axis for the next-gate pressure bar (recommend log); an `asserts` condition on story beats that make a claim about the world; banned-token list per failure type (CR-H2); "Don't write your name" text; lobby fiction (open since round 1).
- **Lead Artist:** one 256×256 greyscale cloud density map; shipped shimmer is particles; animate thawlings for vanilla keyframe export; consider nine-slice GUI sprites (unverified); ambient beds in thin, mid, and full versions, positional sounds mono, every sound subtitled; keyframe files gain `smoothing_s` and `overlay`.
- **Game Designer, Artist, Instructional Designer (small, ill-structured):** the Monitor hides CO2 until Phase 3 but the weather mast's CO2 tube shows it from the start. Fit the tube at a gate (recommended, for lower cognitive load) or accept that the mast shows more.
- **Producer:** route the decisions above; schedule `docs/tech/server-setup.md` and an owner platform checklist at each stage exit; add post-release support to the budget (unverified: about 1 day per NeoForge 21.1.x version bump plus setup questions); update the Stage 1 checkboxes; link the QA plan to test-strategy.md.

## Risks
- Per-player fading means two players on one planet can see different hint levels for the same failure.
- Name scrubbing garbles answers when a username is also an ordinary word (for example "Water"); the `names_replaced` column flags these.
- Clouds and sky may not work with Sodium (to test in Stage 2).
- Unverified API details: the earliest NeoForge 21.1.x with attachment sync; Blockbench animation export for 1.21.1; the `FakePlayerFactory` name; whether `/reload` works in dev.
- Headless screenshots may fail if the Mojang asset download is blocked; fallback is the owner taking them.

## Shared model updates
- **Term: Signal.** A read-only world-state value (storm strength, surface radiation, fauna pressure) published by one system module into the planet snapshot. Visuals and conditions read it like an Index parameter, but it isn't one (ARCHITECTURE.md §2.5.1).
- **Decisions (Technical Lead):** clouds use a density map; heat shimmer is particles only; thawlings use vanilla animation; art and content live in `pack/assets/` and `pack/data/` built into the mod jar; goal pages fill from player-scope events only; one result per player and forecast pair; test strategy adopted.
- **Lesson:** when a new requirement seems to break a privacy rule, look for a narrow interface at the boundary before relaxing the rule.

---

## Addendum: SME ice rule (filed by coordinator, after the main report)

- **Done:** `ARCHITECTURE.md` §2.5 now uses the SME's boiling-point rule. Below 0.611 kPa ice sublimates; at or above the boiling point it boils away at once; between 0 °C and the boiling point it makes a short-lived puddle up to 5 kPa and water that stays from 5 kPa; at 0 °C or below it stays ice. The rule lives in data (`data/mars/dimension/ice_rule.json`) and content check CR-F1 runs against the same table.
- **Heat map replaced:** a single heat value per chunk would lose the difference between ice next to a heater (boils) and ice a few blocks away (puddles). Each chunk now caches its two strongest heat sources (position, heat, range); local heat is their sum, fading linearly with distance. Recomputed for about 9 nearby chunks only when a machine is built, removed, switched, or shrouded. Cost: about 10 arithmetic operations per ice random tick, constant regardless of machine count. An estimate to confirm in the Stage 3 load test.
- **Boiling point:** computed once per planet per simulation step (Antoine equation, coefficients in data). `mars:boiling_point` is in the client snapshot, driving vapor wisps and the Monitor's boiling-point line. The client computes local temperature with the same shared code as the server.
- **Handoff:** forecast F4a's trigger and keyed answer, the "sublimating ice" art state (keyframes §3.3), and the balance-sheet time for the short-lived puddle (an invented number). Done when F4a passes CR-F1 against `ice_rule.json`.
- **Risk:** two cached heat sources per chunk may look wrong where three or more heaters overlap; raising the cap to three costs a few more operations.
