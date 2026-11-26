# Development Roadmap

This file breaks the project into development stages. Each stage lists what must be in place before moving on, so we don't build content on top of a foundation that isn't settled.

The **gameplay** phases (Survival → Geo-Engineering → Equilibrium) are described in [DESIGN.md](DESIGN.md). This file covers **how we build them**. Stages are checkpoints, not one-way gates: any role can move back to analysis or design when evidence calls for it (fuzzy ADDIE, TEAM.md §4).

Status key: ✅ done · 🟡 in progress · ⬜ not started. Owner in brackets where it isn't obvious: [P] Producer, [TL] Technical Lead, [GD] Lead Game Designer, [ID] Lead Instructional Designer, [A] Lead Artist (incl. audio), [SME] Subject Matter Expert.

**Production documents:** effort budget, scope check, and cut list in [docs/production/budget.md](docs/production/budget.md); risks in [risk-register.md](docs/production/risk-register.md); testing in [qa-plan.md](docs/production/qa-plan.md). Every stage exit also uses the Success Scorecard (TEAM.md 8.5) and a team debrief (TEAM.md 8.7). Requests that arrive mid-task go to [BACKLOG.md](BACKLOG.md) first.

**Every stage exit meets the definition of done** in [PROCESS.md](PROCESS.md) §2: the **document version** for Stages 0–1 and for every design change; the **build version** from Stage 2 on. In every stage that means:
- **Risk gate:** for each change in the stage, a table of what could break and the check that catches it, each with a result.
- **The owner looks at it:** a walkthrough, mockup, or screenshot (at 1280×720 once builds exist) for everything the player will see.
- **Accept is conditional while a check runs:** a stage is "accepted, pending <check>" until every SME review, alignment check, consensus, or automated run it depends on has passed. Only then is it marked ✅.
- **Owner platform checklist:** at every stage exit the owner confirms which platforms the stage was checked on (Windows, macOS, weak hardware) and what wasn't covered. Checklist and who tests what: [docs/production/owner-actions.md](docs/production/owner-actions.md).

Testing detail: [qa-plan.md](docs/production/qa-plan.md), which follows the Technical Lead's test layers in [docs/tech/test-strategy.md](docs/tech/test-strategy.md). Things only the owner can do: [owner-actions.md](docs/production/owner-actions.md).

---

## Design Pillars

Every feature is checked against these. If a feature doesn't serve at least one, cut it.

1. **The planet is the quest log.** Progress is unlocked by world state, not by checklists or NPCs.
2. **Every change is visible.** If a number moves, the player should see or feel it in the world.
3. **Pressure, not grind.** Difficulty comes from upkeep and trade-offs, not from repetitive crafting.
4. **Compact before wide.** Scarcity shapes how players build. Expansion is earned.
5. **Your progress provokes the planet.** Terraforming faster means facing stronger threats.

---

## Budget at a Glance

Producer estimates, ±50%, re-estimated after Stage 3 from measured rates. Details: [budget.md](docs/production/budget.md).

| Stage | Team hours | Owner hours |
|---|---|---|
| 1 Pre-production (remaining) | 376 | ~5 |
| 2 Prototype | 311 | ~4.5 |
| 3 Vertical slice | 1,304 | ~17.5 |
| 4 Production | 2,870 | ~16 |
| 5 Alpha | 464 | ~21 |
| 6 Beta | 542 | ~21 |
| 7 Release | 312 | ~4 |
| **Total** | **6,179** | **~89** |

Revision 2 (Round 3). Owner hours include playtests, reviews, platform checklists, and finding external reviewers. Scope verdict: achievable through Stage 3; Stage 4 has about a 15% buffer in the cut list, so a bigger overrun needs an owner scope call (budget.md §4).

---

## How We Iterate (ACEDT, Chapter 9)

Source: the user's Chapter 9 pages on the ACEDT multidisciplinary learning game development method, as relayed by the coordinator. ACEDT describes iteration at several levels; this section maps them onto our stages and onto fuzzy ADDIE (TEAM.md §4). Moving back a stage is normal and goes through the change process (TEAM.md 8.2).

### Levels of iteration and their cadence

| Level | What it means | How we do it | Cadence |
|---|---|---|---|
| **1. Product iteration** | Repeated passes through analysis → core design → experience design → development → testing, each producing a better version of the game, from paper or rapid prototypes to a feature-complete game. When a problem shows up (too hard because of usability, wrong assumptions about prior knowledge, only some objectives taught), revisit requirements, prioritize with the customer and SMEs, and fix the highest-priority element first | Each roadmap stage from 2 to 7 is one or more product iterations. Each ends with a build, a test pass (qa-plan.md), a Scorecard review, and a team debrief (TEAM.md 8.7). Findings go into a ranked list; the top items are fixed first | **Stages 2–3: short iterations**, about one build per 1–2 weeks of team time, each with a user playtest session. **Stage 4: one iteration per gameplay phase** (Phase 2, then Phase 3). **Stages 5–6: one per test version** (alpha, beta). **Stage 7: candidate release** |
| **2. Task iteration** | Inside a stage, core tasks run in no fixed order; progress on one reveals new considerations for others. Each core discipline stays aware of key decisions and weighs in, which keeps instructional and gaming elements balanced | The **change impact check** (TEAM.md 8.2) on every flexible or stable change, and on fluid changes touching more than one area; handoffs in reports; the debate schedule (TEAM.md §4) with a perspective statement from every role | **Continuous**: every task report. Producer checks at every stage gate that no handoff is left unanswered |
| **3. Customer and SME iteration** | At every key point, iterate with the customer and SMEs so decisions are appropriate, accurate, authentic, and complete. Internal SMEs give frequent feedback on small details; external SMEs and the customer give critical review | **Customer = the user.** **Internal SME = our Subject Matter Expert role.** There is **no standing external SME** (no planetary scientist or science teacher on the team): risk R-51. **Decided (owner): one external expert review at Stage 5**; the owner finds the reviewer | **SME:** every content item before it is "final" (shared format status field); every gate or physics change; every stage gate. **User:** one decision batch per stage, plus each playtest session's report; critical review at the end of Stages 2, 3, 5 and before release |
| **Local iteration** | Back-and-forth between phases while building and applying earlier decisions, plus a tight development-and-testing loop. Integration problems (with other mods, the learning environment, supporting materials) surface here | Automated tests and invariants on every build; an **integration test pass** each stage from 3 on (qa-plan.md §5): Mekanism, Create, KubeJS; the class server; the educator guide against the build | **Every build** (automated); integration pass **per stage** |

### Test versions (local iteration)

| ACEDT test version | What it is | Our stage | Exit criteria |
|---|---|---|---|
| Preliminary prototypes | Limited functionality, basic testing | **Stage 2** (and the paper prototype at its start) | Stage 2 exit criteria below; qa-plan.md §6 |
| Alpha | Functional; find major issues | **Stages 3–4** (slice, then each phase), checked as a whole in Stage 5 | Stage 3 and 4 exit criteria; Stage 5 "no known blockers" |
| Beta | Stable and complete; user experience and bugs | **Stages 5–6** | Stage 6 exit criteria; Scorecard passes on every lens the project can measure |
| Candidate release | Quality assurance and hardening | **Stage 7** | Release checklist (qa-plan.md §7); no open S1 or S2 |

### Staged deployment

Deploy a small amount of content early, collect data on what works, then enlarge. **The Stage 3 vertical slice is our staged deployment:** one gameplay phase with basic interactions and the minimal learning layer. Data we want from it is listed under Stage 3 ("What we want to learn from the slice"). Stage 4 grows the content only after that data is in and the budget is re-estimated.

### Environment integration (staged, early)

Chapter 9 (ACEDT) recommends integrating with the learning environment early and in stages, not at the end. Ours:

| Piece of the learning environment | First integrated | Then |
|---|---|---|
| Class server (several planets, start paused, backups, export) | Stage 3: the slice runs on the class server, not only singleplayer | Stage 5 end-to-end server pass |
| Instructor training (how a teacher runs a session: pause, pacing, overview, export) | Stage 3: a one-page teacher quick-start for the slice, tried by the user playing the teacher | Stage 4 educator guide chapter; Stage 6 unit walkthrough |
| Documentation (educator guide, IT sheet, worksheets) | Stage 3: IT sheet and data notice drafts checked against the slice | Stage 4 full drafts; integration pass each stage (qa-plan.md) |
| LMS plumbing | Stage 5: **offline converter only** (owner decision): turns an exported `events.csv` into xAPI statements for a local test store; never ships; no student data leaves the machine | After release, opt-in (ARCHITECTURE.md §11) |

**Early adopters and champions (for any future pilot).** This is a design exercise, so there is no pilot. If one ever happens, start with one or two teachers who already run Java Edition and want this (early adopters), and give one of them a champion role: they try each build first, help other teachers, and feed back problems. Don't start with a reluctant school.

### Concept formation

The core concept is chosen (Stage 0). Several **sub-concepts** still have live alternatives, and the Stage 2 prototype tests them cheaply before we converge (Stage 2, "Alternatives this prototype tests").

### Learning objectives: broad first, then drill down

All goals A1–D2 were analyzed across the whole domain in Stage 1 (LEARNING.md §0–1). Each iteration then drills down on a subset: Stage 2 on A1, B1, C3 (pressure, cause and effect, reading the Monitor); Stage 3 on the Phase 1 goals (A1, A2, A4, C1, C2, B3); Stage 4 on Phase 2 goals, then Phase 3 goals. **If scope forces the game to teach only a subset well, that is a decision made with the user and the full team**, using the goal priority tiers (LEARNING.md §1; consensus brief [consensus-round2.md](docs/production/consensus-round2.md) §1.7), never a quiet cut by one role.

---

## Stage 0 — Concept ✅

*Goal: know what we're making and why it's fun.*

- ✅ Core concept and loop written down
- ✅ Three gameplay phases defined
- ✅ Design pillars agreed
- ✅ Progress scope: global
- ✅ Target playtime: ~40 hours (pacing split in DESIGN.md)
- ✅ Target audience: singleplayer and small servers; classroom group planets of 3–5
- ✅ Mars world: custom dimension
- ✅ Regression: gates lock in
- ✅ Multiplayer scaling: fauna by machine signature, plus a static group-size rate scale (thresholds never scale)

**Exit criteria:** all open questions answered and recorded in DESIGN.md.

---

## Stage 1 — Pre-production 🟡

*Goal: lock the technical foundation and the numbers before writing content.*

**Debates this stage (TEAM.md §4):** definition of success, role of objectives, alignment, measurable outcomes, realism vs. fantasy, contested terms *(held)*; **goal priority tiers and interruption rules** *(consensus brief: [consensus-round2.md](docs/production/consensus-round2.md))*.

**Technical decisions**
- ✅ Minecraft version and loader: NeoForge 1.21.1 (user decision; evidence in [docs/tech/mod-licenses.md](docs/tech/mod-licenses.md))
- 🟡 Every candidate mod checked for that version and maintenance: Mekanism, Create, KubeJS, TerraBlender have 1.21.1 builds (primary source); "last updated" dates not checked (hosts blocked) [TL]
- 🟡 **Redistribution permissions** checked for each mod: licenses done from primary sources (all MIT, LGPL, or MPL; Create assets All Rights Reserved; ProbeJS never shipped). Modrinth and CurseForge distribution flags still to check. Blocks release if skipped. [TL]
- 🟡 Companion-mod scope: modules defined (ARCHITECTURE.md §1.2); KubeJS limited to recipes and gating [TL]
- ⬜ **Which machines are in-house:** list every machine needed to learn the science (ours, simple, one interface pattern) and which third-party machines stay optional and late (DESIGN.md, Learning Mechanisms) [GD, TL]
- ⬜ Oxygen, sealing, and suits: name a standalone mod that can read the Planetary Index, or record "build our own" [TL]
- ⬜ Companion mod skeleton: builds, loads, and registers an empty Mars dimension [TL]
- ✅ Software architecture drafted and reviewed by the Technical Lead ([ARCHITECTURE.md](ARCHITECTURE.md)): modules, APIs, session control, data and privacy, story system, admin API, performance budget
- ✅ Class hosting: one server, one Mars dimension per group; one server per group as fallback (user decision)
- ⬜ Spotless formatting and unit-test setup in the build [TL]

**Project setup**
- ⬜ Pack managed as text files (packwiz) so mod lists and configs live in git [TL]
- ✅ Folder layout created: `pack/` (assets, config, data, defaultconfigs, kubejs, licenses, mods) and `mod/` skeletons; plan in [docs/tech/build-plan.md](docs/tech/build-plan.md). Empty: nothing builds yet [TL]
- ⬜ Local dev instance and dedicated test server can be launched from the repo [TL]
- ⬜ **Basic CI at Stage 1 exit** (owner decision): runs the fast test layers L0–L2 from [docs/tech/test-strategy.md](docs/tech/test-strategy.md) (no Minecraft needed); later layers (server boot, GameTests) join as they exist [TL]
- ✅ Consistency review and risk register ([docs/production/](docs/production/)); both Stage 1 blockers closed on paper
- ✅ QA plan drafted: test passes, severity levels, invariants, exit criteria ([qa-plan.md](docs/production/qa-plan.md))
- ✅ Effort budget and cut list ([budget.md](docs/production/budget.md))
- ⬜ Pipeline overview: how art, audio, data, and text get into the game [TL, A]

**Learning design** (see [LEARNING.md](LEARNING.md))
- ✅ Learner and context analysis drafted (LEARNING.md §0)
- ✅ Learner audience: ages 12–18, two grade bands
- ✅ Curriculum: NGSS
- ✅ Classroom mode: groups of 3–5 per planet; 23-period unit with 17 play periods (~11.3 h)
- 🟡 NGSS codes verified: replacement table in [docs/science/ngss-verification.md](docs/science/ngss-verification.md), checked through mirrors; verbatim check on nextgenscience.org pending (host blocked) [SME]
- 🟡 Learning goals mapped to game content (alignment table); goal priority tiers decided (T-C) [ID]
- 🟡 Pre/post test: blueprint done (LEARNING.md §3.3); items not written [ID]
- ✅ Action log approved: pseudonymous on the server, anonymous in every export
- ✅ Action-log format and CSV export defined (LEARNING.md §5.5, ARCHITECTURE.md §8.6); `goals.csv` handed to [TL]
- ✅ Prompts, reflections, and rubrics drafted ([docs/learning/](docs/learning/))
- ⬜ Task analysis and media analysis (missing from the document map) [ID]
- ✅ Interruption rules decided: option I-C, now in TEAM.md 8.6; numbers are config values tuned in Stage 3 [ID, GD]

**Design data**
- ✅ Balance sheet ([docs/design/balance-sheet.md](docs/design/balance-sheet.md)): Index values, Phase 1 pressure, rates, holds, classroom pacing, fauna numbers (untested starting values)
- 🟡 Pacing curve: Phase 1 curve and classroom time model done; Phases 2–3 curves still to draw [GD]
- 🟡 Content inventory: the asset list ([docs/art/asset-list.md](docs/art/asset-list.md), 144 rows) covers it in part; a design-side inventory per phase is still missing [GD]
- ✅ Story bible draft 2 ([docs/design/story-bible.md](docs/design/story-bible.md))
- ✅ Playtest plan for solo user playtests ([docs/design/playtest-plan.md](docs/design/playtest-plan.md))
- 🟡 UI specs: Planetary Monitor (with next-gate display and Breathable air gauge), journal, Keeper's Record, forecasts, failure reports, Keeper's Reports, Shift Board, HUD: draft 1 in [docs/design/ui-specs.md](docs/design/ui-specs.md) [GD, A]
- ✅ Science accuracy review ([docs/science/](docs/science/)); [SME] and [verify] tags still open in art and story docs [SME]
- ✅ Art direction: style guide, keyframes, asset list, palette sheet ([docs/art/](docs/art/)); visual mockups still missing [A]
- ⬜ Audio direction and sound list in `docs/audio/` (Lead Artist owns audio; direction now sits in asset list §9) [A]
- ✅ **Weather mast** added to DESIGN.md (user decision; asset M-30); CO2 gauge fitted at the Phase 3 gate (owner decision) [GD]
- ✅ In-house machine list (about 21 machines) in DESIGN.md; Mekanism and Create optional and late, unlocked at Hydrosphere (owner decision) [GD]
- ✅ `guidance` setting adopted (gentle / standard / expert; gentle is the classroom default; thresholds never change) (owner decision) [GD, ID]

**Before leaving Stage 1**
- ⬜ **Owner action:** seek three external reviewers before Stage 3: a planetary scientist (ice rule), a climate modeler (ice–albedo and polar frost), science teachers (transfer-task key and forecast distractors). SME prepares the review packs from [docs/science/round2-review.md](docs/science/round2-review.md) §11 [owner, SME; see owner-actions.md]
- ⬜ **Full-team story review** (DESIGN.md, Story review): every role reviews the story bible against the six story elements, perspective round first; SME in every pass [P runs, all roles]
- ✅ Consensus recorded on goal priority tiers (T-C) and interruption rules (I-C): owner decision on the team's recommendation [P]

**Exit criteria:** a fresh clone produces a launchable pack and server; the balance sheet exists ✅; both consensus items recorded; story review held; QA invariant tests exist for every Stage 2 system; basic CI green for L0–L2; owner platform checklist; definition of done (document version, then build version for the skeleton).

---

## Stage 2 — Prototype ⬜

*Goal: prove the core loop is fun with placeholder art.* Usability and low extraneous load come first here, not polish (DESIGN.md, Open risk).

**A quick first iteration that can fail cheaply.** This is ACEDT's preliminary prototype. Rules:
- **Timebox:** about 300 team-hours, two short build-and-play iterations at most. If the loop isn't fun after the second, stop and rework the design; don't polish.
- **Paper or spreadsheet first (Stage 2a, ~40 h of the 300):** before code, test the Index model and the alternatives below on paper or in a spreadsheet simulation, with the user and agent walkthroughs. Code (Stage 2b) only what the paper version couldn't answer.
- **Throwaway allowed:** prototype code may be thrown away. Only the simulation core, its unit tests, and the session clock are expected to survive.
- **No final art, no audio, no story text** beyond placeholders.
- **Straw-man assets:** quick, deliberately rough assets (script-generated placeholders with the magenta marker, style guide §5.5) stand in for everything. Their job is to test function and readability, not looks.
- **Face validity:** conceptual designs of the key assets (Monitor screen, Breathable air gauge, Regolith Outgasser, weather mast, thawling silhouette) go to the owner and the SME as sketches or mockups before final art: does it look like what it claims to be, and does it claim anything false?

**Alternatives this prototype tests (concept formation)**

| Sub-concept | Alternatives | Cheapest test | Decides |
|---|---|---|---|
| Air window / Breathable air gauge | One zoomed gauge with three worded zones vs. two separate numbers (O2 % and O2 partial pressure) | Paper mockups read by Lens A and Lens B; spreadsheet of the band under classroom drift events | GD, A, ID |
| Next-gate display | "Next: pressure 5 kPa, 62%" vs. no goal display | Paper Monitor mockup; then in the code prototype | GD, ID |
| Fauna options | Lethal only vs. shrouds, lures, quiet zones; how signature drives raids | Spreadsheet simulation of fauna pressure against machine count | GD |
| Colony posts | Suggested posts vs. no posts; merges for 3 and 4 players | Paper role-play with the user plus agents playing posts | GD, ID |
| Classroom pacing | Rate ×4.0 and short holds | Spreadsheet first, then session P2 | GD |

**Debates this stage:** fun vs. learning with real evidence; usability; functional fidelity of the Index model.

- ⬜ **Stage 2a (scheduled first, owner decision):** paper and spreadsheet prototypes for the alternatives above, plus the `guidance` levels and the orbital mirror route as a B4 choice; findings filed by the Lead Game Designer before any Stage 2b code [GD, ID, A]
- ⬜ **Face-validity review** of concept sketches (Monitor wireframe, sky strip, weather mast, thawling silhouettes, Outgasser; `docs/art/concepts/`): owner and SME, one sitting, during Stage 2a [A runs; owner, SME]
- ⬜ Planetary Index stored in world save data and readable by command [TL]
- ⬜ Simulation clock and sols (simulation time; sleeping never advances it) [TL]
- ⬜ Pause and resume; **GameTests confirming that pause stops third-party machines** (Mekanism, Create); fallback "pause = stop the server" [TL]
- ⬜ One machine that raises one parameter (pressure): placeholder Regolith Outgasser [TL, A]
- ⬜ One visible world response to that parameter (sky tint or fog) [TL, A]
- ⬜ One regression source (machine wear or a storm) [TL]
- ⬜ Admin API and debug commands to set and inspect every Index value [TL]
- ⬜ Rollback restore approach chosen (start script is the safe default) [TL]
- ⬜ Invariant tests I-1 to I-3 (qa-plan.md §4) [TL]
- ⬜ Prototype art minimum: E-01, E-02, T-01, M-08, U-01 (placeholders allowed) [A]
- ⬜ User playtests P1 (standard) and P2 (classroom config) and reports [user, GD]

**Exit criteria:** the user can raise pressure, see the sky change, and feel pressure to maintain it, and can say why each change happened; face-validity notes from the owner and SME recorded; definition of done (build version). If it's not fun here, rework the loop before going further: that outcome is a success of this stage, not a failure.

---

## Stage 3 — Vertical Slice (Gameplay Phase 1 complete) ⬜

*Goal: the first gameplay phase at near-final quality, as the template for everything else, including a minimal learning layer.*

**Debates this stage:** feedback (motivational and instructional); player language; cognitive load of text and fantasy. **Also:** revisit voice narration for BELL's key explanations after the playtest (DESIGN.md, Multimedia) [P, GD].

**World and systems**
- ⬜ Mars spawn and starting dome structure (sized for up to five) [TL, A]
- ⬜ Oxygen, water, and power loop with real recipes, using simple in-house machines [TL, GD]
- ⬜ Dome sealing and breach venting [TL]
- ⬜ Dust storm event (visibility, solar penalty, machine wear) [TL]
- ⬜ Radiation spike event and shielding, with a warning ~1 sol ahead [TL, GD]
- ⬜ Life-support wear and maintenance parts [TL]
- ⬜ Perchlorate, Phase 1 stage: airlock dust-off station [TL, GD]
- ⬜ "Dome stable" gate (3 sols; unlocks airlocks, EVA suit tier 2, first outpost blueprint, Regolith Outgasser) [TL]
- ⬜ Airlocks and EVA suit tier 2 [TL, A]
- ⬜ **Regolith Outgasser** (+0.10 kPa/sol each), starting Regolith Ovens, and the polar frost sink, per balance sheet §2 [TL, GD]
- ⬜ "Phase 2 begins" gate (pressure ≥ 5 kPa) [TL]
- ⬜ Ice-to-water rule, Phase 1 part (ice turns to vapor below the triple point) [TL]
- ⬜ Planetary Monitor block with cause lines, milestones, and **next-gate display** ("Next: pressure 5 kPa, 62%") [TL, A, GD]
- ⬜ **Weather mast** prop (windsock, lift balloon, spirit thermometer; pointer gas gauges fitted later at the Phase 3 gate) [A, TL]
- ⬜ Group planets: planet registry and player assignment; group rate scale [TL]

**Onboarding and scaffolding**
- ⬜ **Landing**: controls-first onboarding in story (BELL's first-shift checklist), ~20–30 min; skippable for experienced players; the first forecast comes only after it [GD, ID, TL]
- ⬜ **Worked example:** the first Outgasser set up with Kowalczyk's notes; skippable [GD, ID]
- ⬜ **Shift Briefing** for Phase 1 (advance organizer from BELL) [GD, ID]

**Minimal learning layer** (LEARNING.md §3.5)
- ⬜ Dome stable journal entry, with its "On Earth" line (A1, D2) [ID, GD, SME]
- ⬜ Forecasts F1 (dust storm), F4a (ice to vapor), and F2 (radiation) with hidden answers in a group (B3, C1, A2, A4) [ID, TL]
- ⬜ One failure report (seal breach) with Hana's tip (C2) [ID, GD]
- ⬜ Keeper's Record skeleton: pages C1 and C2 plus the "Cairn team" section [ID, TL, A]
- ⬜ Action log with actor IDs, and a CSV export (all goals) [TL]
- ⬜ 6-item Phase 1 mini pre/post test (A1 data task, A4, C1, two misconceptions) [ID]
- ⬜ Interruption rules implemented as config values, per the consensus outcome [TL]

**Story, art, audio**
- ⬜ Essential Phase 1 story beats (1, 3, 4, 5, 6, 7) [GD]
- ⬜ Phase 1 art from the asset list (Stage 3 rows) [A]
- ⬜ Three original pixel fonts (Cairn Display, Text, Mono) with a glyph-existence test [A, TL]
- ⬜ `guidance` setting in the slice: all three levels, gentle default in classroom, expert not offered in classroom; level logged and exported [TL, GD, ID]
- ⬜ External reviewers' replies (ice rule, ice–albedo, transfer key, distractors) folded in before Stage 3 content is final [SME]
- ⬜ Phase 1 audio: pressure-driven exterior ambience (AU-01), suit sounds, storm, dosimeter, message and log chimes (AU-01 to AU-04, AU-11), with subtitles [A; SME checks the sound physics]
- ⬜ Accessibility pass for Phase 1 (qa-plan.md) [A]
- ⬜ Name check: Thawlings, Cairn Dome, BELL, crew names against existing works, before Stage 4 content writing [P]

**Testing and measurement**
- ⬜ **Load test: 4 planets × 4 simulated players** on modest hardware; replace placeholder numbers [TL]
- ⬜ User playtests S1 (standard Phase 1) and S2 (classroom config, `group.size` = 4), and reports [user, GD]
- ⬜ Teacher quick-start (one page) and IT sheet draft tried against the slice by the user playing the teacher [ID, TL]
- ⬜ Effort measured per content item (machine, journal entry, texture, sound); Stage 4 re-estimated and cut list applied (budget.md §4) [P]

**What we want to learn from the slice (staged deployment)**

| Question | Data | Source |
|---|---|---|
| Does Phase 1 take the planned time? | Real time to each gate; pressure at sols 5, 10, 15, 20 vs. balance sheet §2 | S1, S2 observation sheets |
| Is anything idle or stuck? | Longest stretch with nothing to do; minutes stuck per task | S1 observation sheet |
| Does Landing work for players without the Minecraft schema? | Time to first useful action; Lens A notes | S1, walkthrough |
| Do forecasts and per-player evidence work in a group? | Forecasts answered, expired, late; Keeper's Record pages filled from own evidence; CSV rows per actor ID | Action log, CSV export |
| Do the interruption rules hold? | Interruptions, deferrals, expiries, unsent reports | Action log |
| Is the text the right length and level? | Text boxes over budget; reading-level checks | S1, content review |
| Can the player name the cause of every pressure change? | Post-session question 3 | S1 report |
| What does content actually cost? | Hours per machine, journal entry, texture, sound | Team effort log (Producer) |
| Will a classroom server hold up? | Memory, CPU, tick time for 4 planets × 4 players | Load test |
| Mini pre/post test works as an instrument? | Item clarity (walkthrough; no student data in a design exercise) | ID review |

**Exit criteria:** the user gets from spawn to the Phase 2 gate without help; the time is within ±25% of the balance sheet curve; the learning layer works end to end (own vs. team evidence, CSV by actor ID); the class server runs the slice for several planets; QA exit criteria for Stage 3 met; definition of done (build version).

---

## Stage 4 — Production ⬜

*Goal: build out the remaining gameplay phases using the slice as the template.*

**Debates this stage:** fantasy audit for each new element; how often design is changing; scope cuts (budget.md cut list).

**Gameplay Phase 2 — Geo-Engineering**
- ⬜ Transit tubes, outposts [TL, A]
- ⬜ Atmospheric processors, super-greenhouse gas emitters, deep-crust extractors (in-house) [TL, GD]
- ⬜ **Orbital mirror route** (owner decision): orbital mirror uplink warms with no ground fauna signature, expensive and slow; a B4 route choice against emitters [TL, GD, A, SME]
- ⬜ Ice-comet impacts: **cut list item 1**; build only if the Stage 3 re-estimate allows [GD]
- ⬜ Ice–albedo feedback capped at about 2 °C, plus the real polar CO2 release loop (owner decision, a+b) [TL, GD, SME]
- ⬜ Mekanism and Create: optional and late, unlocked at Hydrosphere, hidden from recipe viewers until then and hidden in the classroom preset; never needed for a gate (owner decision) [TL]
- ⬜ Thaw gate; ice-to-water rule, full (brief puddles, then pooling) with the chunk heat map [TL]
- ⬜ Hydrosphere gate content: surface water, algae vats [TL]
- ⬜ **Ground collapse** after the Thaw, with a survey scan for ice lenses [TL, GD, A]
- ⬜ **Perchlorate, Phase 2 stage:** meltwater treatment (cut list item 8) [TL, GD]
- ⬜ Dormant fauna (thawlings): spawn rules tied to temperature, water, and machine signature; planet caps [TL, GD, A]
- ⬜ Fauna **later life stage** at the Hydrosphere gate (never "mutation"); one per family if cut list item 12 applies [TL, GD, A]
- ⬜ Fauna options: lethal defense (never lowers fauna pressure), **shrouds**, **quiet zones**, **lures** (cut list item 11) [TL, GD, A]
- ⬜ Phase 2 Shift Briefing [GD, ID]

**Gameplay Phase 3 — Equilibrium**
- ⬜ Biome seed kits and the spread of flora [TL, A]
- ⬜ **Perchlorate, Phase 3 stage:** soil treatment (cut list item 6) [TL, GD]
- ⬜ O2/CO2 balancing machines (including carbon scrubbers), CO2 from decay, and failure states [TL, GD]
- ⬜ **Drift events** during holds and Phase 3 (balance sheet §4) [TL, GD]
- ⬜ Breathable live check, helmet-off, and the **Breathable air gauge** (LEARNING.md §4.7) [TL, GD, A, ID]
- ⬜ Storms weaken over time, and the sky shifts toward a living world [TL, A]
- ⬜ Colonization beacon, ending sequence, and **ending cards** (fauna outcomes, unranked) [TL, GD, A]
- ⬜ Phase 3 Shift Briefing [GD, ID]

**Group play**
- ⬜ **Shift Board**: claim and fix failures; actor IDs in the log; **C2 credit goes to the player who did the repair** (owner decision) [TL, GD, A]
- ⬜ Colony posts suggested at each phase start [GD]

**Learning layer (full)**
- ⬜ Gate journal entries for every gate, both grade bands, paired with visuals [ID, GD, SME]
- ⬜ Forecasts F1–F10 in the characters' voices, with explanations and distractors [ID, GD, SME]
- ⬜ **Earth links (D2)** for every phase: done when each Primary goal's NGSS claim that needs D2 has its content (risk R-44) [ID, GD, SME]
- ⬜ Keeper's Record, all pages; Keeper's Reports 1–3 and final [ID, TL]
- ⬜ "What caused it?" checks [ID, TL]
- ⬜ **Fading:** forecast hints shrink across phases; failure reports give less detail on repeats; cause breakdown closed by default in Phase 3 [ID, TL]
- ⬜ Escalating hints at natural pauses only [ID, TL]
- ⬜ Close alignment gaps: radiation forecast (A4, done as F2/F7), Earth-link content (D2) [ID]

**Story, art, audio**
- ⬜ Story writing for the remaining essential beats, plus optional beats if budget allows (cut list item 7) [GD]
- ⬜ Art: textures, models, sky/fog, clouds and shimmer (cut list item 4) [A, TL]
- ⬜ Audio: the rest of [docs/audio/sound-list.md](docs/audio/sound-list.md) (machines, fire, ice and water, ground collapse, thawlings, flora, beacon), with subtitles; secondary sounds may be simple synthesized versions (cut list item 5) [A]
- ⬜ Music: **one original ending piece**; vanilla music off on Mars; phase pieces on the cut list (proposed default) [A]
- ⬜ Real Perseverance recording in the journal only, labeled REAL, **only if its license is verified** (proposed default) [A, P]
- ⬜ Accessibility pass: color never the only signal, colorblind-safe palettes, subtitles, UI legible on small screens [A]

**Classroom and educator**
- ⬜ Classroom mode preset (rate ×4.0, holds 1/2/3 sols, KubeJS recipe preset, start paused) [TL]
- ⬜ Teacher tools: class overview, pause, per-planet pacing, rollback, set phase, log export, grade band [TL]
- ⬜ Educator guide and worksheets, including the **23-period unit plan**; up front: "Requires Minecraft: Java Edition. Does not run in Minecraft Education." [ID]
- ⬜ **Paper reflection fallback** worksheet for teachers who switch off `reflections.csv` [ID]
- ⬜ IT requirements sheet from [docs/tech/school-deployment.md](docs/tech/school-deployment.md) in the educator guide [ID, TL]
- ⬜ Player guide and in-game help [GD]
- ⬜ Performance budget: ticking machines in a typical late-game base, measured on the test server [TL]
- ⬜ **`docs/tech/server-setup.md`**: how a teacher installs, starts, pauses, backs up, and exports a class server [TL]

**Exit criteria:** every gate in DESIGN.md is reachable in a normal playthrough and by the automatic solver (PROCESS.md §3); the alignment table has no gaps; the fantasy audit covers every new element; definition of done (build version).

---

## Stage 5 — Alpha ⬜

*Goal: feature complete; find what's broken.*

**Debates this stage:** evaluation criteria and results; revisit TEAM.md 8.4 positions with playtest data.

- ⬜ **Owner's run (decided):** a full classroom-preset run from spawn to beacon, plus standard-mode spot checks with debug shortcuts, not the full 40 hours; agent walkthroughs for the rest [owner, GD]
- ⬜ **Offline LMS converter** (owner decision): exported `events.csv` → xAPI statements in a local test store; never ships; no student data leaves the machine [TL, ID]
- ⬜ No progression blockers (soft-locks, unreachable gates, uncraftable items)
- ⬜ Multiplayer tested on a dedicated server (several planets; the user runs several local accounts)
- ⬜ Saves survive restarts, rollback, and pack updates
- ⬜ Privacy pass: export audit and invariants I-5 to I-9 [TL, ID]
- ⬜ Consent and safety plan for minors written as a design artifact (no minors are recruited in this design exercise) [P, ID]
- ⬜ **External expert review** (owner decision): one outside expert reviews the journal science, forecasts, and educator guide. The owner finds the reviewer; the SME prepares the review pack (claims with sources); S1 findings fixed before Stage 5 exits [owner, SME, ID]

**Exit criteria:** no known blocker bugs; definition of done (build version).

---

## Stage 6 — Beta ⬜

*Goal: balance and polish.*

**Debates this stage:** evaluation criteria and results; revisit TEAM.md 8.4 positions with playtest data.

- ⬜ Tuning runs by the user, with the feedback form from the playtest plan
- ⬜ Pacing tuned against the target playtime
- ⬜ Difficulty of fauna and hazards tuned
- ⬜ Performance on low-end hardware checked
- ⬜ Config options for difficulty or pacing, if needed
- ⬜ Learning study design: pre/post test protocol and success criteria (LEARNING.md §3.4), written as if a class would take it; learning gain can't be measured in this design exercise
- ⬜ **Delayed post-test** (decided: a design artifact; a few weeks after the unit) to check retention: closes RETAIN's naturalization gap [ID, SME]
- ⬜ Classroom unit walkthrough: the user plays the teacher through the 23-period plan; setup under 30 minutes [user, ID, TL]

**Exit criteria:** the pack can be finished and the pacing feels right to the user; Scorecard passes on every lens the project can measure; definition of done (build version).

---

## Stage 7 — Release ⬜

**Debates this stage:** operational lifetime; user-generated content; what we learned as a team.

- ⬜ All redistribution permissions confirmed, including platform flags (from Stage 1)
- ✅ Licensing plan decided (owner): **code MIT; art, audio, story, journal, and educator materials CC BY 4.0**
- ⬜ LICENSE files added at release (none before then, per the plan); third-party notices per [docs/tech/mod-licenses.md](docs/tech/mod-licenses.md) [P, TL]
- ⬜ Pack exported for Modrinth (main) and CurseForge (from packwiz)
- ⬜ Server pack published (offline classroom bundle with license notices)
- ⬜ Pack page: description, screenshots, trailer (trailer is cut list item 2)
- ⬜ Changelog and versioning scheme (qa-plan.md §8)
- ⬜ Educator package published: educator guide, worksheets, unit plan, pre/post test, paper reflection fallback
- ⬜ A place to report bugs (GitHub Issues)
- ⬜ **Maintenance plan:** how long the pack is supported, how Minecraft and NeoForge updates are handled, support window [P, TL]
- ⬜ **User-generated content guide:** how teachers and players share challenge datapacks and worlds, using the data-driven gates and tuning; what we learn from them [TL, ID, GD]
- ⬜ Release checklist complete (qa-plan.md §7)

**Exit criteria:** release checklist complete; no open S1 or S2; definition of done (build version), with acceptance recorded only after the final runs pass.

---

## After Release (planned, not budgeted)

| Item | Note |
|---|---|
| Support window | Proposal: 12 months, ~8 h/month (about 1 day per NeoForge 21.1.x bump plus setup questions, Technical Lead estimate, unverified) |
| Version ports | 1.21.1 is ageing (R-06); each port is a separate scope decision |
| LMS integration | Opt-in only, off by default; builds on the Stage 5 offline converter; privacy review before building (ARCHITECTURE.md §11, LEARNING.md §5.6) |
| "Veteran" hobbyist setting | Deferred until after Stage 3 (owner decision); only if Lens B playtests ask for it |
| Voice audio | Only if the Stage 3 narration revisit recommends it |
| Lessons from user-generated content | Feed challenge datapacks and teacher feedback into the next update |

---

## Where We Are Now

**Stage 1 (Pre-production), in progress (Round 3).** Design documents are drafted, both Stage 1 blockers are closed on paper, and the owner has decided every pending recommendation (TEAM.md Decision Log, Round 3). The folder layout exists; nothing builds yet. Budget revision 2: 6,179 team-hours and ~89 owner-hours. The plan through Stage 3 is achievable; Stage 4 is at the edge, with about a 15% buffer in the cut list (budget.md §4).

Next actions:
1. **Apply the decided consensus** (owner: goal tiers T-C, interruption rules I-C; [consensus-round2.md](docs/production/consensus-round2.md)): LEARNING.md §1 and §4.6 marked decided; config keys for the caps. [ID, GD, TL]
2. **Hold the full-team story review** of the story bible before Stage 3. [P, all]
3. **Build the foundation:** companion-mod skeleton with an empty Mars dimension (build-plan.md), packwiz, Spotless, unit tests, and basic CI for L0–L2. [TL]
4. **Cost the in-house machine list and the mirror route**, and decide the oxygen/sealing approach. [TL]
5. **Review the UI specs draft** ([docs/design/ui-specs.md](docs/design/ui-specs.md)) against PROCESS.md §4 UI rules and the decided interruption rules (I-C). [GD, A, ID]
6. **Owner actions** ([owner-actions.md](docs/production/owner-actions.md)): find three external reviewers; plan who tests on Windows, macOS, and weak hardware.
7. **Phase 3 CO2 budget** (open, ill-structured; Game Designer with the SME): where the CO2 from Outgassers and processors goes, so Phase 3 isn't a grind or a false climb (risk R-52).
