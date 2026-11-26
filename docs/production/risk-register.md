# Risk Register

**Owner:** Producer · **First issue:** 2026-11-26 · **Last review:** 2026-11-26 (Round 2) · **Stage:** 1 (Pre-production) · **Review:** at the start of every roadmap stage, and whenever a blocker is found.

**Scale.** Likelihood: Low / Medium / High. Impact: Low (a few days of slip) / Medium (a stage slips or a feature is cut) / High (release or the learning outcome is at risk). **Score** = likelihood × impact, with L=1, M=2, H=3. Act now on anything scoring 6 or more.

**Status:** Open · Mitigated on paper (designed, not yet tested) · Accepted (a known constraint) · Closed.

**Context.** The project is a design exercise: no real students will use it (TEAM.md Decision Log). We still treat classroom, privacy, and minors' risks as real requirements. Where that changes a score, the row says so.

Facts marked **unverified** need the named owner to check a primary source.

## Top risks

| Rank | ID | Risk | Score | Change this round |
|---|---|---|---|---|
| 1 | R-01 | Scope: content volume plus the companion mod | 9 | Up in volume (~21% added, see [budget.md](budget.md)); score already max |
| 2 | R-02 | Companion mod is larger than the team can build | 9 | Unchanged; now 42% of all effort |
| 3 | R-17 | Only one human playtester, and no players of the target age | 9 | Reframed: the user plays builds (decision); ~75 h of user play needed |
| 4 | R-31 | Narrow breathable O2 window plus frequent drift events | 6 | New |
| 5 | R-44 | D2 Earth-link content not written; four NGSS claims depend on it | 6 | New |
| 6 | R-51 | No external SME (internal SME only) | 6 | New (ACEDT input) |
| 7 | R-09 | Schools that only have Minecraft Education can't run the pack | 6 | **Verified**; now an accepted constraint |

## Register

### Scope and schedule

| ID | Risk | L | I | Score | Owner | Mitigation | Status |
|---|---|---|---|---|---|---|---|
| R-01 | **Scope.** ~40 h of content across three phases, now including perchlorate (3 stages), ground collapse, shrouds, lures, quiet zones, drift events, Shift Board, ending cards, weather mast, 12 audio sets, Landing onboarding, fading, worked examples, Shift Briefings. Budget: ~5,820 h, Stage 4 is 47%. | H | H | 9 | Producer | Commit only through Stage 3; measure effort per content item in the slice; re-estimate Stage 4 before committing. Cut list ordered by goal tier and fun–learning quadrant: [budget.md §4.3](budget.md). Classroom and standard modes share content; tune pacing, don't add content. | Open |
| R-03 | **Hold-time padding.** Long holds make standard mode idle. | M | M | 4 | Lead Game Designer | Holds overlap; counter pauses instead of resetting; drift events every 1–1.5 sols; beacon parts buildable during holds (balance sheet §4). Prototype rule: if testers report waiting, shorten Equilibrium to 10 sols first. | Mitigated on paper |
| R-04 | **Third-party tech mod grind** for 12-year-olds new to tech packs. | M | M | 4 | Lead Game Designer | New design rule: every learning-critical machine is ours and simple; Mekanism and Create are optional and late (DESIGN.md, Learning Mechanisms). Walkthrough as Lens A in Stage 3. | Mitigated on paper |
| R-05 | **Design gaps found late.** | M | M | 4 | Producer | Both Stage 1 blockers closed on paper. New gaps found by the coordinator's Chapter 3 review are now roadmap items with owners. Repeat the consistency review at each stage gate. | Open |
| R-30 | **75 kPa target lengthens Phase 3** and may feel like a grind. | M | M | 4 | Lead Game Designer | Phase 3 curve in the balance sheet; drift events keep holds active; watch Phase 3 pacing in Stage 4 checks; classroom pressure target is the lever if needed (never the 16 kPa rule). | Open |
| R-32 | **Classroom pacing rests on untested assumptions** (rate ×4.0, group scale, ~11.3 h; Phase 2 may run long in class). | M | M | 4 | Lead Game Designer, Lead Instructional Designer | Session S2 (classroom config) in Stage 3; per-planet pacing tool; unit plan says Phase 2 may spill into Phase 3 periods. | Open |
| R-39 | **Phase 2 is the thinnest story stretch** (7 beats in ~18 h), so engagement (behavioral, emotional) may sag. | M | M | 4 | Lead Game Designer | Fauna, outposts, Basu's sites, ground collapse fill it; Phase 2 Shift Briefing; watch in Stage 4 checks. | Open |
| R-47 | **Art and audio volume.** 144 asset rows plus 12 audio sets on one role; audio added to the Lead Artist by user decision. | M | M | 4 | Lead Artist | Stage 2 minimum is 5 assets; placeholders allowed through Stage 4; cut list items 2, 6, 7 reduce art; consider licensed CC0 sound sources (license check with Technical Lead). | Open |
| R-50 | **Two consensus items pending** (goal priority tiers, interruption rules). The cut list and Stage 3 feedback specs depend on them. | M | L | 2 | Producer | Consensus brief: [consensus-round2.md](consensus-round2.md). Decide before Stage 3 specs. | Open |

### Technical

| ID | Risk | L | I | Score | Owner | Mitigation | Status |
|---|---|---|---|---|---|---|---|
| R-02 | **Companion mod size.** Dimension, Index, simulation, sky, weather, fauna, sealing, journal, story, learning, teacher tools, session control. ~2,430 h of Technical Lead effort (budget.md). | H | H | 9 | Technical Lead | Stage 1 skeleton; pure-Java simulation core with unit tests first; modules only depend on `core` (ArchUnit); track effort per module; measure in Stage 3. Still open: oxygen/sealing/suits (custom or standalone mod). | Open |
| R-06 | **Version lock.** 1.21.1 is ageing (NeoForge has moved to 26.1 on Java 25, verified by search only). Mekanism 10.7.19, Create 6.0.11, KubeJS 2101.7.2 have 1.21.1 builds (primary source). | M | H | 6 | Technical Lead | Stay on 1.21.1 (user decision); pin versions in packwiz; port cost in budget.md §5 as post-release scope. | Open |
| R-08 | **Performance on school hardware.** Hardware numbers are placeholders; current Java requirements went up (8–12 GB RAM, Vulkan GPU), **unverified** whether they apply to 1.21.1. Clouds and heat shimmer may be costly. | H | M | 6 | Technical Lead | Stage 3 load test (4 planets × 4 players); server preset for weak hardware (ARCHITECTURE.md §6.3); cached per-chunk heat map instead of per-block checks; cut list item 6 (clouds, shimmer). | Open |
| R-10 | **Save and data integrity.** | M | M | 4 | Technical Lead | Evidence stored in `mars-data/<worldId>/`, outside the world; rollback marks events superseded, never deletes; automatic backup at every resume (ARCHITECTURE.md §7). Stage 5 test: saves survive restarts and pack updates. | Mitigated on paper |
| R-11 | **Third-party machines keep running while paused.** `/tick freeze` should stop them, but that is an inference. | M | M | 4 | Technical Lead | Stage 2 GameTests for Mekanism and Create; fallback "pause = stop the server" (simulation-time sols already survive it). | Open |
| R-40 | **Verification debt from blocked hosts.** Modrinth and CurseForge distribution flags, verbatim NGSS text, NASA fact sheet values, Mars color samples, Minecraft Education pages, and several citations were checked only through search copies or not at all. | M | M | 4 | Technical Lead (mods, schools), SME (NGSS, science, colors), Lead Instructional Designer (citations) | Recheck when the hosts are allowed (coordinator to raise with the user); nothing unverified appears in-game as fact. Release gate in qa-plan.md. | Open |

### Learning outcomes

| ID | Risk | L | I | Score | Owner | Mitigation | Status |
|---|---|---|---|---|---|---|---|
| R-12 | **The game teaches a misconception.** | L | H | 3 | SME | Breathable gate now uses partial pressures; thresholds never scale; "later life stage"; ice-to-water rule follows the triple point; accuracy notes sourced. Remaining: [SME] and [verify] tags, sky colors. SME review at every stage. | Mitigated on paper |
| R-13 | **Learning can't be measured per student** on a shared planet. | L | H | 3 | Lead Instructional Designer | LEARNING.md §3.0 separates individual summative, individual formative, and group evidence; every log event carries the actor's ID; Keeper's Record fills only from own evidence. To be tested end to end in the Stage 3 slice (action log + CSV). | **Mitigated on paper** |
| R-14 | **Learning layer built last.** | — | — | — | — | Closed: a minimal learning layer moved into the Stage 3 slice (user decision; LEARNING.md §3.5; ROADMAP Stage 3). | **Closed** |
| R-15 | **Classroom compression breaks learning.** | M | M | 4 | Lead Instructional Designer | Three debriefs and a transfer task in the 23-period unit; forecasts issued ≥ 1 sol ahead; check in the Stage 6 unit walkthrough. | Open |
| R-16 | **NGSS codes are wrong** in the educator guide. | L | M | 2 | SME, Lead Instructional Designer | Replacement table in `docs/science/ngss-verification.md`; verbatim check pending (R-40). | Mitigated on paper |
| R-31 | **Narrow breathable O2 window** (about 21.5–23% at 75 kPa) is hard to read, and classroom drift events (every 0.5–0.75 sol) can push a group out of band repeatedly without understanding why. | M | H | 6 | Lead Game Designer (gauge spec), Lead Artist (visual), Lead Instructional Designer (wording) | One zoomed "Breathable air" gauge with three worded zones; time-to-edge estimate; partial pressure in "read more" at MS depth (LEARNING.md §4.7). Playtest check in Stages 2–3; lever is the pressure target, never the 16 kPa rule. | Open |
| R-35 | **Fictional fauna reinforce "Mars has life".** | M | M | 4 | SME, Lead Game Designer | Labeled fiction every time; journal line "No life has been found on Mars"; post-test misconception item; fantasy audit in DESIGN.md. | Mitigated on paper |
| R-37 | **Text load for weaker readers.** | M | M | 4 | Lead Instructional Designer | Reading-level rules; ~60 words per message; pair words with visuals; "read more"; narration revisit after Stage 3. Reading-rate assumption **unverified**. | Open |
| R-41 | **Sparse individual evidence for quiet students** in a group. | M | M | 4 | Lead Instructional Designer | Individual forecasts on every player's screen; colony posts rotate; teacher overview flags players with little evidence; summative grades come from the pre/post test and reflections only. | Open |
| R-44 | **D2 Earth-link content isn't written yet.** D2 is Primary, and four NGSS mappings (MS-ESS3-5, HS-ESS2-4, HS-ESS3-6, A3's MS code) only count if it exists. | M | H | 6 | Lead Instructional Designer (spec), Lead Game Designer (voice), SME (accuracy) | First Earth link ("On Earth" line in the Dome stable entry, Mensah's first message) is in the Stage 3 slice; the full set is a Stage 4 item with a done-when. | Open |
| R-45 | **Red-green colorblind players miss biomass greening.** | L | M | 2 | Lead Artist | Lightness and texture changes, numerically checked (style guide §4.2–4.3). | Mitigated on paper |

### Classroom deployment

| ID | Risk | L | I | Score | Owner | Mitigation | Status |
|---|---|---|---|---|---|---|---|
| R-09 | **Schools that only have Minecraft Education can't run the pack.** **Verified:** Minecraft Education is Bedrock-based and doesn't support community mods; its licenses don't work for Java Edition ([docs/tech/school-deployment.md](../tech/school-deployment.md), findings 1–3; official pages quoted via search, not fetched). No bulk Java licensing route found (**unverified**). | H | M | 6 | Technical Lead (facts), Lead Instructional Designer (educator guide) | **Accepted as a constraint.** Club, home, and Java-equipped labs are first-class audiences. Educator guide says up front "Requires Java Edition; does not run in Minecraft Education". IT requirements sheet drafted. No Bedrock port. Impact lowered from H to M: the audiences are redefined and this is a design exercise. | Accepted |
| R-07 | **Classroom group model undefined.** | — | — | — | — | Closed: groups of 3–5 per planet; one server with one Mars dimension per group (user decisions). | **Closed** |
| R-18 | **Teachers can't run the server.** | M | M | 4 | Technical Lead | One server, one machine; start paused; teacher tools in game; setup under 30 minutes (Scorecard). Stage 6 walkthrough with the user as teacher. | Open |
| R-19 | **The unit doesn't fit school calendars** (23 periods). | M | M | 4 | Lead Instructional Designer | "Set phase" shorter unit; educator guide lists goals each shortcut drops. | Mitigated on paper |
| R-42 | **School networks or managed PCs block the launcher, Java, or the server port** (**unverified** how common). | M | M | 4 | Technical Lead | IT requirements sheet; pre-built instance folder option; LAN only, no internet traffic during play. | Open |

### Data and privacy (minors)

| ID | Risk | L | I | Score | Owner | Mitigation | Status |
|---|---|---|---|---|---|---|---|
| R-20 | **Pseudonymous data has had no legal review** (COPPA, FERPA, GDPR may apply depending on country, **unverified**). | L | H | 3 | Technical Lead, Lead Instructional Designer | Random per-world IDs; mapping file never exported or backed up; no username export (user decision); teacher can delete the mapping. Legal review before any real use. Likelihood lowered: no real students (design exercise). | Mitigated on paper |
| R-43 | **Free-text reflections in `reflections.csv`** may contain names or personal details. | M | M | 4 | Technical Lead (scrubbing), Lead Instructional Designer (prompts) | Separate file, teacher can switch it off; on by default only in classroom mode; "don't write your name" note; names on the planet scrubbed to IDs at export; paper fallback (user decision). | Mitigated on paper |
| R-21 | **Playtests with minors without proper consent.** | L | H | 3 | Producer | No minors are recruited (design exercise; the user plays builds). The consent and safety plan stays a design artifact in Stage 5. | Mitigated |
| R-22 | **Future LMS integration sends student data off the server.** | L | H | 3 | Technical Lead | Post-release, opt-in, off by default; xAPI file export with pseudonymous IDs only; separate privacy review first. | Open |

### Mod redistribution and licensing

| ID | Risk | L | I | Score | Owner | Mitigation | Status |
|---|---|---|---|---|---|---|---|
| R-23 | **A required mod forbids redistribution.** Licenses checked from primary sources: every mod in the current stack is MIT, LGPL, or MPL ([docs/tech/mod-licenses.md](../tech/mod-licenses.md)). Limits: Create's assets are All Rights Reserved (never copy or edit); ProbeJS is GPL-3.0 (dev only, never ship). Still open: Modrinth and CurseForge distribution flags, Registrate and Rhino on 1.21.1 branches, JUnit and ArchUnit, the oxygen/suit mod if any. | L | H | 3 | Producer (tracking), Technical Lead (checks) | Mod-licenses file is the permissions table; recheck flags when hosts are allowed (R-40); release checklist item in qa-plan.md. | Mitigated on paper |
| R-24 | **FTB Quests terms.** | — | — | — | — | Closed: custom journal (user decision); FTB Quests rejected. FTB Library is All Rights Reserved, so it stays out. | **Closed** |
| R-25 | **Our own license is unclear** (code, assets, educator materials). | M | M | 4 | Producer | User decision, still open (TEAM.md §4). Recommended before Stage 4, when educator materials start. | Open |
| R-26 | **Minecraft EULA or brand rules** limit school distribution or the pack page (**unverified**). | L | M | 2 | Producer | Read the current usage guidelines before Stage 7. | Open |
| R-36 | **Names not cleared against existing works** (Thawlings, Cairn Dome, BELL, the Keeper, crew names). | M | M | 4 | Producer | Name search before Stage 4 content writing (ROADMAP Stage 3 item); "Thawlings" is a working name. | Open |

### Team capacity and process

| ID | Risk | L | I | Score | Owner | Mitigation | Status |
|---|---|---|---|---|---|---|---|
| R-17 | **One human playtester, no target-age players.** The user plays builds (decision); agents add design walkthroughs. Solo adult playtests give design judgment, not learning evidence or age fit. About 75 h of user play across Stages 2–7 (budget.md §3). | H | H | 9 | Producer | Short, focused sessions (playtest-plan.md); Lens A and Lens B walkthroughs; debug shortcuts instead of a full 40-h run; every report labels measured vs. judged findings. Accepted limit: learning gain can't be shown in this project. | Open |
| R-27 | **The user is a single point of approval.** | H | M | 6 | Producer | One decision batch per stage with a recommendation each (7–8 batches total). | Open |
| R-28 | **Parallel edits cause drift.** | M | M | 4 | Producer | File ownership; consistency review at every stage gate; handoffs instead of cross-edits. | Open |
| R-29 | **No teacher with classroom experience** reviews the design. | M | M | 4 | Lead Instructional Designer | Design exercise: the user reviews as teacher in the Stage 6 unit walkthrough; teacher-facing text reviewed against LEARNING.md §5. | Open |
| R-51 | **No external SME.** Our Subject Matter Expert is an internal SME (a team role). There is no external planetary scientist or science teacher to give critical review, which ACEDT expects at key points (Chapter 9, via coordinator). Errors the internal SME shares with its sources, or classroom-practice problems, may go unnoticed. | M | H | 6 | SME (sources), Producer (review plan), user (optional external review) | Every science claim cites a published primary source; unverified items never appear as fact in game (R-40); SME review at every stage gate and every physics or gate change; the user may arrange an optional external review of journal science and the educator guide before release (qa-plan.md §3, §7). | Open |
| R-38 | **Isolation themes** (solo watch) could land badly with some teens. | L | M | 2 | Lead Game Designer, Lead Instructional Designer | Tone rules in the story bible; crew contact through messages; story review before Stage 3. | Open |
| R-46 | **Create and Mekanism keep their own look**, breaking the cohesive style. | M | L | 2 | Lead Artist | In-house machines carry the learning and the style; third-party machines are late and optional. | Mitigated on paper |

## Change log

| Date | Change |
|---|---|
| 2026-11-26 | First issue, from the Stage 1 consistency review. |
| 2026-11-26 | Round 2. R-09 verified and accepted (impact H→M). R-07, R-14, R-24 closed (R-14: learning layer moved into the Stage 3 slice). R-13 mitigated on paper (L). R-23 links mod-licenses.md (L). R-12, R-20, R-21 lowered. R-01, R-02, R-06, R-08, R-11, R-17 updated. New: R-30–R-32, R-35–R-47, R-50 from the Game Designer, Technical Lead, Instructional Designer, and Artist reports. Status column added. R-51 (no external SME) added from the coordinator's Chapter 9 (ACEDT) input. IDs R-33, R-34, R-48, R-49 not used: those report risks merged into R-17 (solo adult playtests), R-01 (new content scope), R-47 (audio workload), and R-17 (user hours). |
