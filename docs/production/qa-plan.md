# QA Plan

**Owner:** Producer · **Status:** Draft 1 (2026-11-26) · **Stability:** flexible (reviewed at every stage gate)
**Works with:** [ROADMAP.md](../../ROADMAP.md) (stage exit criteria), [TEAM.md §8.5](../../TEAM.md) (Success Scorecard), [docs/design/playtest-plan.md](../design/playtest-plan.md) (user playtest sessions), [ARCHITECTURE.md §3](../../ARCHITECTURE.md) (coding practices and tests), [risk-register.md](risk-register.md), [budget.md](budget.md).

QA here covers more than bugs. A learning game can work perfectly and still fail: it can teach something false, lose a student's evidence, leak a name, or bore a 12-year-old. So every test pass checks **both goals**: instructional value and market appeal (DESIGN.md, Two Goals).

**Honesty rule.** No role can run Minecraft except the user. Every QA result says how it was produced: **automated** (tests in the build), **user playtest** (the user played), **walkthrough** (an agent stepped through the design or build as a player), or **review** (document check). Walkthroughs and solo adult playtests are design judgment, not evidence of learning or age fit (risk R-17).

---

## 1. Bug severity levels

One scale for every kind of issue, so a science error and a crash are ranked the same way.

| Level | Name | Means | Examples | Fix by |
|---|---|---|---|---|
| **S1** | Blocker | Stops play, loses data, leaks identity, or teaches something false | Crash or soft-lock; gate unreachable; save or action log lost; a name or account ID in any export; a threshold changes with a preset; content the SME rates "wrong"; a Primary goal loses its only teaching, feedback, or measure | Before the stage exits. No release with an open S1 |
| **S2** | Major | Breaks a goal, a pillar, or a must-pass check, but play can continue | Value moves with no visible cause; idle or stuck over 5 minutes; color as the only signal; text far over its budget; hint gives the full answer; interruption during danger or building; forecast answerable after its outcome; a Secondary goal loses its only measure; frame rate below target on the reference machine | Before the next stage's main work starts, or cut the feature |
| **S3** | Minor | Noticeable, doesn't break a goal | Typo; wrong icon; awkward wording; small balance drift | At the owner's next edit |
| **S4** | Polish | Nice to have | Animation timing; extra sound variety | If time allows (cut list applies) |

**Category tags** (one or more per issue): `functional`, `balance`, `learning` (alignment, measure, feedback), `accuracy` (science), `privacy`, `accessibility`, `usability`, `performance`, `story`, `art-audio`, `content-text`.

**Who assigns:** the person who finds it proposes a level; the owning role confirms. Disagreements about S1 for `learning` or `accuracy` go to the Lead Instructional Designer or the SME respectively; their call stands for the stage.

## 2. Where issues are tracked

| Period | Where | Format |
|---|---|---|
| Stages 1–4 | Playtest reports in `docs/design/` (Lead Game Designer); review findings in `docs/production/` | Table rows: ID, level, tags, found by (method), owner, goal IDs, status |
| From Stage 5 | GitHub Issues in the project repository, one label per level and tag | Same fields as issue template fields |
| After release | Public GitHub Issues (ROADMAP Stage 7) | Same template, plus pack version |

Issue IDs: `QA-<stage>-<number>` (e.g. `QA-3-012`) until GitHub Issues take over.

## 3. Test types

| Type | What it checks | Who | Method label |
|---|---|---|---|
| **Unit tests** | Pure-Java simulation core: Index step, couplings, gates, holds, derived values, group rate scale | Technical Lead | automated |
| **Architecture tests** | Modules depend only on `core` (ArchUnit) | Technical Lead | automated |
| **GameTests** (NeoForge) | In-world behavior: sealing, pause, ice-to-water, gate unlocks, machine contributions | Technical Lead | automated |
| **Invariant tests** | Rules that must never break (section 4) | Technical Lead | automated |
| **Boot and build (CI)** | Pack builds; server boots without errors; client loads | Technical Lead | automated |
| **User playtests** | Sessions P1, P2, S1, S2, A1+ in playtest-plan.md | User; Lead Game Designer writes the report | user playtest |
| **Lens walkthroughs** | Lens A (12-year-old, new to tech packs) and Lens B (experienced 17-year-old) | Lead Game Designer, Lead Instructional Designer | walkthrough |
| **Content review** | Science claims, reading level, goal IDs, the shared message format (DOCUMENTATION.md §5.1) status field | SME (accuracy), Lead Instructional Designer (alignment, reading level) | review |
| **Alignment check** | Every goal taught, practiced, given feedback, measured (LEARNING.md alignment table); forecasts still valid after any gate or physics change (TEAM.md Learning Log) | Lead Instructional Designer | review |
| **Privacy audit** | Exports contain IDs only; mapping file never exported or backed up; reflections scrubbed | Technical Lead, Lead Instructional Designer | automated + review |
| **Accessibility checks** | Color never the only signal; colorblind and grayscale numeric checks; ≤ 3 flashes per second; subtitles for every sound; 420×236 GUI fit | Lead Artist | automated where possible + review |
| **Performance and load tests** | Frame rate on modest hardware; 4 planets × 4 players server load | Technical Lead | automated + user |
| **Save and update tests** | Saves survive restarts, pack updates, rollback; evidence survives rollback | Technical Lead | automated |
| **Scorecard review** | All seven Scorecard lenses (TEAM.md 8.5) | Whole team; Producer runs | review |
| **Integration tests** | The pack with the things around it: Mekanism, Create, and KubeJS (recipes, gating, pause); the class server (several planets, start paused, backups, export); the educator guide and worksheets against the actual build (every step, screenshot, and number matches) | Technical Lead (mods, server), Lead Instructional Designer (educator materials) | automated + review |
| **External expert review** | Our SME role is internal; there is no standing external SME (risk R-51). Science is checked against published primary sources by the internal SME. **Decided (owner): one external expert review at Stage 5**, of the journal science, forecasts, and the educator guide. The owner finds the reviewer; the SME prepares the review pack (claims list with sources); findings use the severity scale above | SME (prepares), owner (arranges), external reviewer | review |

### 3.1 Chapter 9 testing tasks

From the user's Chapter 9 pages (ACEDT), via the coordinator. Each task maps to the test types above.

| Testing task | What it means for us | When | Who | Caution |
|---|---|---|---|---|
| **Formative feedback** | Every symptom found (stuck player, wrong forecast pattern, idle hold) is analyzed for its **root cause from several perspectives** before a fix is chosen: usability, prior-knowledge assumption, content, balance, technical. A perspective round (TEAM.md 8.5) for any S1 or S2 that touches more than one discipline | Every iteration, Stages 2–6 | Finder proposes; the owning roles analyze together; Producer tracks | Don't fix the symptom in one discipline when the cause is in another (e.g. a "too hard" report caused by an unclear Monitor, not by balance) |
| **Playtesting** | **Alpha** playtests with the SME and internal testers (the owner plus agent walkthroughs); **beta** ideally with the target group (ages 12–18) | Alpha: Stages 3–5. Beta: Stages 5–6 | Owner plays; Lead Game Designer reports; SME reviews | **With only the owner as tester, avoid strong conclusions.** Label findings as design judgment, check them with the owner and the SME, and never claim learning gain or age fit (R-17). No minors are recruited (design exercise) |
| **Software testing** | Unit, GameTests, invariants, the headless playthrough and gate solver (section 4a), save and update tests, integration tests | Every build from Stage 2 | Technical Lead | A passing check that read nothing is worse than none (print what it read) |
| **Usability testing** | Can players use the controls, Monitor, journal, machines, and teacher tools without outside help? Task-based sessions (playtest-plan.md tasks with time limits); screenshots at 1280×720 looked at by a person | Stage 2 first (usability is the prototype's priority), then every stage | Owner; Lead Game Designer; Lead Artist for readability | Must-pass check, not the whole evaluation (TEAM.md 8.4) |
| **Validation testing** | Does the game do what it was designed to do for its learners? Learning goals checked against the alignment table and the pre/post test design; Scorecard; external expert review of science and the educator guide | Stage 5 (external review), Stage 6 (Scorecard, unit walkthrough) | Lead Instructional Designer, SME, external reviewer | In a design exercise, validation of learning gain can't be done; validate the **instrument and alignment**, and say so |

## 4a. Regression gate (from PROCESS.md §3)

Once the mod exists, these run on every build, locally and later in CI:

- **Headless playthrough through the real code:** seal the dome, raise pressure, reach each gate, breathe, build the beacon. Runs N times across standard mode, classroom mode, group size 1 and 5, and an "everything at once" stress case. Exits non-zero on failure and prints the first failing check with its ID.
- **Gate solver:** every gate and every story beat is reachable by an automatic solver; an unreachable gate is an S1 soft-lock.
- **Checks state where things must be**, not only where they mustn't (e.g. "the next-gate chip is at the top center and names pressure 5 kPa", not just "the chip is on screen").
- **Every new check is proven to fail** against the old code, for the right reason.
- **Checks print what they read.**
- **Tests restore any global state they change.**
- **Determinism:** seeded content (forecast wording variants, drift events) reads the same after save and load.
- **Content rules as tests:** a hint appears only for a problem that really exists and never states the answer; story beats match gate conditions; every forecast has a correct answer under current physics.
- **Readability:** text color against its real background at 1280×720.
- **Saves:** schema version and migration per change; never overwrite a newer save; corrupt the main file on purpose to test the backup fallback.

## 4b. Invariants (automated from Stage 2)

These are S1 if they ever fail. Each becomes a test as soon as its system exists.

| # | Invariant | Source |
|---|---|---|
| I-1 | Gate thresholds are identical in standard, classroom, and every `group.size` | DESIGN.md §1; TEAM.md Decision Log |
| I-2 | Latched gates never unlock again after values fall; helmet-off is re-checked every step | DESIGN.md §1 |
| I-3 | No simulation time passes while paused, while the server is stopped, or by sleeping | ARCHITECTURE.md §7.1 |
| I-4 | Third-party machines produce nothing while paused (or pause = stop the server) | ARCHITECTURE.md §7.2; R-11 |
| I-5 | Rollback never deletes action-log or learning records | ARCHITECTURE.md §7.4 |
| I-6 | No export file contains a username, account ID, IP, or chat | LEARNING.md §5.4; ARCHITECTURE.md §8 |
| I-7 | Every log event carries an actor ID or `scope: planet` | LEARNING.md §3.1 |
| I-8 | A group event never fills a player's Keeper's Record goal page | LEARNING.md §5.5 |
| I-9 | A forecast answered after its outcome is visible is logged as late or expired, never scored | LEARNING.md §3.2 |
| I-10 | Every player-facing text has both grade-band keys and a goal ID or motivational purpose | DOCUMENTATION.md §3, §5.1 |
| I-11 | No game text uses "mutate", "mutation", "learner", "lesson", "quiz", or "objective" | DESIGN.md (fauna; Player, not learner) |
| I-12 | Every sound event has a subtitle key | Style guide §4.7 |

## 5. Test passes per stage

| Stage | Pass | Main checks | Method |
|---|---|---|---|
| **1 Pre-production** | Document pass | Consistency review re-run; document map complete; every design element has goal IDs; story review held | review |
| | Setup pass | Fresh clone builds the pack and server; CI green | automated |
| **2 Prototype** | Core-loop pass | Index steps; one machine raises pressure; sky responds; one regression source; debug commands | automated + user (P1) |
| | Session pass | Pause stops our systems and third-party machines (GameTests); sols use simulation time; I-1 to I-3 | automated |
| | Classroom-config pass | Rates ×4 and short holds work; thresholds identical | user (P2) |
| | Usability pass | Can the player tell what changed and why? (usability first, DESIGN.md open risk) | user + walkthrough |
| **3 Vertical slice** | Phase 1 pass | Spawn to 5 kPa at standard pace; times against the balance sheet curve | user (S1) |
| | Group-rates pass | Classroom config with `group.size` = 4 | user (S2) |
| | Learning-layer pass | Minimal layer (LEARNING.md §3.5) works end to end: forecasts hidden in a group, failure report gives a hint not the answer, Keeper's Record shows own vs. team, CSV export by actor ID, mini pre/post test ready | automated + user + review |
| | Onboarding pass | Landing teaches controls before the first forecast; Lens A has a next step within ~2 minutes at all times | user + walkthrough |
| | Interruption pass | Interruption rules (TEAM.md 8.6, after consensus) hold; the log records every interruption, deferral, and expiry | automated + user |
| | Load pass | 4 planets × 4 simulated players on modest hardware | automated |
| | Content pass | Every Phase 1 text: SME-reviewed and ID-reviewed status; reading level; word budgets | review |
| | Accessibility pass (Phase 1) | Color, flashes, subtitles, small screens | review + automated |
| | Integration pass | KubeJS recipes and gating; class server with several planets; any third-party machines in the slice | automated + review |
| **4 Production** | Per-phase pass | Each new gate reachable; fauna options work; holds and drift events active; Breathable gauge readable | user + walkthrough |
| | Fantasy audit pass | Every new fictional element against the six rules in DESIGN.md | review |
| | Content pass | All journal entries, forecasts F1–F10, Earth links, ending cards in both grade bands | review |
| | Alignment pass | No gaps in the alignment table; forecasts re-checked after any physics or gate change | review |
| | Performance pass | Late-game base on modest hardware | automated + user |
| | Integration pass | Mekanism, Create, KubeJS with the full recipe set and pause; educator guide drafts against the build | automated + review |
| **5 Alpha** | Full-run pass | Spawn to beacon (classroom preset full run, standard spot checks); no soft-locks | user + walkthrough |
| | Multiplayer pass | Dedicated server, several planets, group posts, Shift Board | automated + user (multiple local accounts) |
| | Save and update pass | Restarts, rollback, pack update | automated |
| | Privacy pass | All invariants I-5 to I-9; export audit | automated + review |
| | Integration pass | Class server end to end (setup, pause, backup, rollback, export); educator guide steps match | automated + user + review |
| | **External expert review** (owner decision) | Journal science, forecasts, educator guide reviewed by one outside expert the owner finds | review |
| **6 Beta** | Tuning pass | Pacing, fauna, hazards against targets | user |
| | Unit walkthrough | 23-period unit with the user playing the teacher; setup under 30 minutes | user + review |
| | Scorecard pass | All seven lenses | review |
| **7 Release** | Candidate-release hardening | Release checklist (section 7); final integration pass; optional external SME review | review + user |

## 5a. Test versions (ACEDT local iteration)

From the user's Chapter 9 pages (ACEDT), via the coordinator. Testing runs through four versions; each maps to roadmap stages and uses the exit criteria in section 6.

| Version | Means | Our stages | Focus of testing |
|---|---|---|---|
| Preliminary prototypes | Limited functionality, basic testing | 2 (paper and spreadsheet prototypes first, then code) | Does the loop work and is it worth continuing? Fail cheaply |
| Alpha | Functional; find major issues | 3–4 (slice, then each phase); checked whole in 5 | Blockers, balance, learning layer end to end, integration |
| Beta | Stable and complete; user experience and bugs | 5–6 | Experience, pacing, polish, remaining bugs |
| Candidate release | Quality assurance and hardening | 7 | No open S1 or S2; release checklist |

## 6. Stage exit criteria (QA view)

A stage exits when its ROADMAP exit criterion is met **and**:

| Stage | Open S1 | Open S2 | Also required |
|---|---|---|---|
| 1 | 0 | Allowed with an owner and a plan | Invariant tests exist for every system planned in Stage 2 |
| 2 | 0 | ≤ 5, none tagged `learning` or `accuracy` | P1 and P2 reports filed; Scorecard: Usability and Feasibility rows reviewed |
| 3 | 0 | ≤ 5, none tagged `learning`, `accuracy`, `privacy`, `accessibility` | S1 and S2 reports filed; Phase 1 time within ±25% of the curve; load-test numbers replace placeholders; Scorecard all rows reviewed |
| 4 | 0 | ≤ 10, none tagged `accuracy` or `privacy` | Every gate reachable; alignment table has no gaps; fantasy audit complete |
| 5 | 0 | ≤ 10 | No known blockers; saves survive updates; external expert review done and its S1 findings fixed (S2 findings scheduled) |
| 6 | 0 | 0 tagged `learning`, `accuracy`, `privacy`, `accessibility` | Scorecard passes on every lens the project can measure (Learning-gain row marked "not measurable in a design exercise") |
| 7 | 0 | 0 | Release checklist complete |

## 7. Release checklist (Stage 7)

- [ ] All redistribution permissions confirmed, including Modrinth and CurseForge flags ([docs/tech/mod-licenses.md](../tech/mod-licenses.md); R-23, R-40)
- [ ] ProbeJS not in the shipped pack; Create's assets untouched; license notices in `pack/licenses/`
- [ ] LICENSE files added per the licensing plan (owner decision): code MIT; art, audio, story, journal, and educator materials CC BY 4.0. Third-party notices per [docs/tech/mod-licenses.md](../tech/mod-licenses.md)
- [ ] Names cleared (R-36); Minecraft usage guidelines read (R-26)
- [ ] No open S1 or S2
- [ ] Every [verify], [SME], and **unverified** tag either resolved or kept out of game text
- [ ] External expert review (Stage 5) findings closed or accepted by the owner (R-51)
- [ ] Educator package: guide (with "Requires Java Edition; not Minecraft Education", IT sheet, data notice), worksheets, 23-period unit plan, pre/post test, paper reflection fallback
- [ ] Server pack boots from a clean machine; teacher setup under 30 minutes
- [ ] Version number and changelog (section 8)
- [ ] Maintenance plan and support window published (ROADMAP After Release)

## 8. Versioning and changelog (proposal)

- **Pack version:** `MAJOR.MINOR.PATCH`. MAJOR: world-breaking change (new Minecraft version, save format). MINOR: new content or a balance change that affects a gate's timing. PATCH: fixes only.
- **Companion mod** uses its own version with the same rules and a save-schema number (ARCHITECTURE.md).
- **Changelog** lists, for each release: fixes by severity, balance changes with the gate they affect, and any change to a goal's teaching, feedback, or measure (so teachers know when the unit plan needs updating).

## 9. Change log

| Date | Change |
|---|---|
| 2026-11-26 | Draft 1. Added ACEDT test versions, integration tests, and the external SME line (coordinator input from Chapter 9). Added Chapter 9 testing tasks (3.1), the regression gate (4a), the Stage 5 external expert review and the licensing plan (owner decisions). |
