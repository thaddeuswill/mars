# Risk Register

**Owner:** Producer · **First issue:** 2026-11-26 · **Stage:** 1 (Pre-production) · **Review:** at the start of every roadmap stage, and whenever a blocker is found.

**Scale.** Likelihood: Low / Medium / High. Impact: Low (a few days of slip) / Medium (a stage slips or a feature is cut) / High (release or the learning outcome is at risk). **Score** = likelihood × impact, with L=1, M=2, H=3. Act now on anything scoring 6 or more.

Mod licenses, version support, and school rules below are **unverified** until the named owner checks them against a primary source.

## Top risks

| Rank | ID | Risk | Score |
|---|---|---|---|
| 1 | R-01 | Scope: 40 h of content plus a custom dimension mod | 9 |
| 2 | R-02 | Companion mod is larger than the team can build | 9 |
| 3 | R-09 | Schools can't run Minecraft Java with mods | 9 |
| 4 | R-17 | No human playtesting capacity | 9 |
| 5 | R-07 | Classroom group model undefined | 6 |

## Register

### Scope and schedule

| ID | Risk | L | I | Score | Owner | Mitigation |
|---|---|---|---|---|---|---|
| R-01 | **Scope.** About 40 h of content across three phases (6 parameters, 7 gates, about 15 machines and sources, 5 hazards, fauna families, journal and quizzes for two grade bands, teacher tools, educator package) is too much for one release. | H | H | 9 | Producer | Build the vertical slice first and measure real effort per content item before committing to Stage 4. Keep a cut list ready, in order: (1) orbital mirrors and ice-comet impacts, (2) LMS integration (post-release), (3) mutated fauna variants beyond one per family, (4) trailer and cinematic ending (credits only). Classroom mode and standard mode share content, so tune pacing rather than adding content to reach 40 h. |
| R-03 | **Hold-time padding.** 40 h is reached through long holds and slow rates instead of play, so standard mode feels idle. | M | M | 4 | Lead Game Designer | Balance sheet shows player activity per hour; prototype test of the hold gates (consistency review M-16). |
| R-04 | **Third-party tech mod grind.** Mekanism/Create recipe depth overwhelms 12-year-olds new to tech packs and works against *Pressure, not grind*. | M | M | 4 | Lead Game Designer | Use a curated subset via KubeJS; hide unused content; walkthrough as a 12-year-old in Stage 3. |
| R-05 | **Design gaps found late.** Two blockers already (Phase 1 pressure source, classroom group model). More may surface during content build. | M | M | 4 | Producer | Close the consistency review blockers before Stage 1 exits. Repeat this review at each stage gate. |

### Technical

| ID | Risk | L | I | Score | Owner | Mitigation |
|---|---|---|---|---|---|---|
| R-02 | **Companion mod size.** It owns the dimension, Index, simulation, sky and fog rendering, weather, fauna, sealing, learning tools, and teacher tools. It is the largest technical risk. | H | H | 9 | Technical Lead | Skeleton loads an empty dimension in Stage 1 (already a roadmap item). Track effort per module. Look for standalone mods for oxygen/sealing before building. Build a pure-Java simulation core with unit tests first. |
| R-06 | **Version lock.** A key mod (Mekanism, Create, KubeJS, oxygen mod) is not on NeoForge 1.21.1, or stops being maintained mid-project. | M | H | 6 | Technical Lead | Check every candidate in Stage 1 with sources; pin versions in packwiz; avoid hard dependencies where KubeJS integration can be optional. |
| R-08 | **Performance on school hardware.** Many ticking machines, sealing flood-fills, and sky shaders on weak lab PCs or a teacher's laptop as the server. | H | M | 6 | Technical Lead | Performance budget item in Stage 4; measure early on low-end hardware in Stage 3, not only in Stage 6; fixed-step simulation and cached contributions (already in ARCHITECTURE). |
| R-10 | **Save and data integrity.** Pack updates break worlds; rollback loses the action log (consistency review M-06). | M | M | 4 | Technical Lead | Schema version in saved data (already planned); log stored outside rolled-back data; Stage 5 test "saves survive restarts and updates". |
| R-11 | **Third-party mods bypass the world pause** and keep producing between classes. | M | M | 4 | Technical Lead | Define pause semantics (M-05); simplest fallback is "pause = server stopped". |

### Learning outcomes

| ID | Risk | L | I | Score | Owner | Mitigation |
|---|---|---|---|---|---|---|
| R-12 | **The game teaches a misconception** (e.g. oxygen fraction means breathable, sky color, ice melt rules). | M | H | 6 | SME | SME accuracy review of Index values, couplings, and gates in Stage 1; journal accuracy notes; SME playtest review at each stage. |
| R-13 | **Learning can't be measured per student** on a shared world (M-14), so the pilot shows no valid result. | M | H | 6 | Lead Instructional Designer | Per-player measures based on predictions, checks, contributions; pre/post test is the main measure. |
| R-14 | **Learning layer built last.** Journal, prompts, feedback, and log all sit in Stage 4, so the vertical slice is never tested for learning (M-19). | M | M | 4 | Producer | Move a thin Phase 1 learning layer into Stage 3 (decision pending). |
| R-15 | **Classroom compression breaks learning.** At ~11–12 h, students skip the explanation layer and only race gates. | M | M | 4 | Lead Instructional Designer | Debrief periods in the unit plan (already there); fuller prompts in classroom mode; check in the Stage 6 pilot. |
| R-16 | **NGSS codes are wrong** in the educator guide. | L | M | 2 | Lead Instructional Designer | Verify every code against the official NGSS text (Stage 1 item). |

### Classroom deployment

| ID | Risk | L | I | Score | Owner | Mitigation |
|---|---|---|---|---|---|---|
| R-09 | **Schools can't run it.** Each student needs Minecraft Java Edition; many schools license Minecraft Education instead, which does not run NeoForge mods (**unverified**: TL to confirm). School IT must approve installs, a launcher, and Java. School networks often block game servers and ports. | H | H | 9 | Technical Lead (facts), Lead Instructional Designer (educator guide) | Find out in Stage 1 what schools actually have: ask the pilot school early. Write a one-page IT requirements sheet (accounts, launcher, Java, ports, hardware). Support a LAN server on a teacher machine. Keep club and home play as the fallback audience. |
| R-07 | **Classroom group model undefined** (C-02). A full class on one world breaks balance, the dome, and server performance. | H | M | 6 | Lead Instructional Designer + Lead Game Designer | User decision in Stage 1; recommended groups of 3–5 per planet. |
| R-18 | **Teachers can't run the server** without technical help. | M | M | 4 | Technical Lead | One-command server start; teacher tools in-game, not config files; educator guide setup chapter; test with a non-technical teacher in Stage 6. |
| R-19 | **The unit doesn't fit school calendars** (20 periods is about four weeks of science time). | M | M | 4 | Lead Instructional Designer | Offer a shortened plan using "set phase" (e.g. 10 periods, Phase 2 only). |

### Data and privacy (minors)

| ID | Risk | L | I | Score | Owner | Mitigation |
|---|---|---|---|---|---|---|
| R-20 | **The log isn't actually anonymous.** It uses Minecraft UUIDs plus a username mapping; a CSV with usernames can leave the computer (M-07). Students are minors, so laws such as COPPA, FERPA, or GDPR may apply depending on the country (**unverified**: no legal review yet). | M | H | 6 | Technical Lead + Lead Instructional Designer | Random per-world IDs; mapping file never exported; no username option in the CSV; plain-language data notice in the educator guide; legal or school data-protection review before the pilot. |
| R-21 | **Playtests with minors without proper consent.** | M | H | 6 | SME (recruitment plan), Producer | Consent and safety plan (school and parent consent, no personal data, adult supervision) approved before any outside playtest. Added to ROADMAP Stage 5. |
| R-22 | **Future LMS integration sends student data off the server.** | L | H | 3 | Technical Lead | Opt-in only, off by default, post-release; separate review before building. |

### Mod redistribution and licensing

| ID | Risk | L | I | Score | Owner | Mitigation |
|---|---|---|---|---|---|---|
| R-23 | **A required mod forbids redistribution** in a modpack or server pack, which blocks release. | M | H | 6 | Producer (tracking), Technical Lead (checks) | Check each mod's license and Modrinth/CurseForge permission in Stage 1, before building on it; keep a permissions table in `docs/production/`; have a replacement for each mod. |
| R-24 | **FTB Quests or another mod has terms we can't meet**, or forces a checklist UI (M-12). | M | M | 4 | Technical Lead | Decide the journal build in Stage 1; recommended custom. |
| R-25 | **Our own license is unclear** (companion mod, assets, educator materials). Schools need to know if they can copy worksheets. | M | M | 4 | Producer | User picks licenses for code, assets, and educator materials before Stage 7. |
| R-26 | **Minecraft EULA or brand rules** limit how the pack can be marketed or distributed to schools (**unverified**). | L | M | 2 | Producer | Read the current Minecraft usage guidelines before the pack page is written. |

### Team capacity

| ID | Risk | L | I | Score | Owner | Mitigation |
|---|---|---|---|---|---|---|
| R-17 | **No human playtesting capacity.** The team agents can't run Minecraft or watch players. Every exit criterion from Stage 2 on needs a real playtest. | H | H | 9 | Producer | Ask the user who can run playtests and how often. Plan short, focused playtest scripts per stage. Recruit outside testers early (Stage 5 plan). |
| R-27 | **The user is a single point of approval** for decisions, builds, and testing. | H | M | 6 | Producer | Batch decisions into one list per stage with a recommendation each; keep a "Decisions needed" log. |
| R-28 | **Parallel edits cause drift.** Several roles edit related documents at once, which recreates contradictions. | M | M | 4 | Producer | Clear file ownership (already in README); consistency review at every stage gate; handoffs instead of cross-edits. |
| R-29 | **No teacher or SME with classroom experience** checks the design until Stage 6. | M | M | 4 | Lead Instructional Designer | Ask one real teacher to review LEARNING §5 and the teacher tools list during Stage 1–3. |

## Change log

| Date | Change |
|---|---|
| 2026-11-26 | First issue, from the Stage 1 consistency review. |
