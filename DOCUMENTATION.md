# Documentation Plan

Documents are how the team communicates decisions and remembers them. This matters even more for us: the team is AI agents with no memory between tasks, so **if it isn't documented, the team doesn't know it.**

The guiding question: *what does each document need so the team has the information it needs, when it needs it, to make design decisions?*

Owner of this file: Producer.

---

## 1. Document Map

Standard game and instructional design documents fall into four categories (Fullerton, 2008; Schell, 2008). LGD adds instructional analysis documents and learning objectives, methods, assessment, and feedback.

Status key: ✅ draft exists · 🟡 partial · ⬜ missing · 📅 planned for a later stage. Map last reviewed by the Producer: 2026-11-26 (Round 3).

### Design documents
| Standard document | Purpose | Our file | Owner | Status |
|---|---|---|---|---|
| Game Design Overview | High-level design and audience, for management | `README.md` (summary) + `DESIGN.md` intro | Producer / Lead Game Designer | 🟡 Thin: needs a one-page overview |
| Detailed Design Document | All mechanics and interfaces | `DESIGN.md`, `docs/design/` | Lead Game Designer | 🟡 Mechanics yes; UI specs not yet |
| Balance sheet | Every Index value, rate, hold, pacing model, and fauna number | `docs/design/balance-sheet.md` | Lead Game Designer | ✅ Draft 1 (untested starting values) |
| Playtest plan | Solo user playtest sessions, tasks, observation sheet, lenses | `docs/design/playtest-plan.md` | Lead Game Designer | ✅ Draft 1 |
| Story Overview Document | Dialog, narration, settings, characters | `docs/design/story-bible.md` | Lead Game Designer | ✅ Draft 2 (full-team story review pending) |
| UI specs | Monitor, journal, Keeper's Record, forecasts, reports, Shift Board, HUD | `docs/design/ui-specs.md` | Lead Game Designer, Lead Artist | ✅ Draft 1 |
| Message content | Forecasts, Earth links, failure reports, story messages per phase, in the shared format (5.1) | `docs/design/messages/` | Lead Game Designer (with ID and SME review) | 🟡 Draft; ten essential beats still unwritten (R-58) |
| Audience analysis | Who the learners are | `LEARNING.md` §0.1–0.2 | Lead Instructional Designer | ✅ Draft |
| Content analysis | What must be learned, by type | `LEARNING.md` §0.3; `docs/science/` | Lead Instructional Designer, SME | ✅ Draft |
| Task analysis | The steps and decisions players must master (cognitive task analysis) | `docs/learning/cta.md` | Lead Instructional Designer | ✅ Draft |
| Media analysis | Why a Minecraft modpack, and which in-game media carry which content | `docs/learning/media-analysis.md` | Lead Instructional Designer | ✅ Draft |
| Learner survey | Audience analysis questions; used in the owner's playtests as self and as Lens A and B | `docs/learning/learner-survey.md` | Lead Instructional Designer | ✅ Draft |
| Content rules | Testable rules for forecasts, hints, Record pages, text (CR-*); source of truth for content checks | `docs/learning/content-rules.md` | Lead Instructional Designer | ✅ Draft |
| Learning objectives, methods, assessment, feedback | The instructional design | `LEARNING.md` §1–5 | Lead Instructional Designer | ✅ Draft (goal tiers and interruption rules in consensus) |
| Prompts and reflections | Forecasts F1–F10, reflection questions, Keeper's Record pages | `docs/learning/prompts-and-reflections.md` | Lead Instructional Designer | ✅ Draft |
| Rubrics | Scoring for B5, D1, D2 reflections and the transfer task | `docs/learning/rubrics.md` | Lead Instructional Designer | ✅ Draft |
| Goal → NGSS data map | Example of the `ngss_map.json` format used by the export | `docs/learning/ngss_map.example.json` | Lead Instructional Designer | ✅ Example |
| Science reference | Source of truth for the science; value audit; external-review list | `docs/science/accuracy-review.md`, `docs/science/ngss-verification.md`, `docs/science/round2-review.md` | SME | ✅ Draft (verbatim NGSS check and external reviews pending) |

### Technical documents
| Standard document | Purpose | Our file | Owner | Status |
|---|---|---|---|---|
| Technical Design Document | Software engineering, networking | `ARCHITECTURE.md`, `docs/tech/` | Technical Lead | ✅ Draft (rewritten by the Technical Lead) |
| Mod licenses and versions | Licenses, 1.21.1 builds, redistribution rules; the release permissions table | `docs/tech/mod-licenses.md` | Technical Lead | ✅ Draft (platform flags not yet checked) |
| Pipeline Overview | How to get art, audio, data, and text into the game | Partly `docs/tech/build-plan.md` (layout, content tasks) and ARCHITECTURE.md §3 | Technical Lead with Lead Artist | 🟡 Partial |
| Build plan | Gradle skeleton, versions pinned from the MDK, repository layout | `docs/tech/build-plan.md` | Technical Lead | ✅ Plan (nothing built) |
| Test strategy | Automated test layers L0–L6, solver, headless playthrough, screenshots, platforms | `docs/tech/test-strategy.md` | Technical Lead | ✅ Draft (QA plan links to it) |
| System Limitations | What the target computers and schools can handle | `docs/tech/school-deployment.md`; performance budget in `ARCHITECTURE.md` §12 | Technical Lead | 🟡 Deployment facts done; hardware numbers wait for the Stage 3 load test |
| Art Bible | Common look and feel for all artists | `docs/art/style-guide.md` | Lead Artist | ✅ Draft 1 |
| Concept Art Overview | What the game will look like before it's built | `docs/art/planetary-keyframes.md`, `docs/art/palettes/`, `docs/art/concepts/` | Lead Artist | ✅ Keyframes, palette sheet, five concept images (face-validity review in Stage 2a) |
| Asset list | Every texture, model, UI element, and sound, with stage and status | `docs/art/asset-list.md` | Lead Artist | ✅ Draft 1 (144 rows) |
| Audio direction and sound list | Ambience, alarms, chimes, fauna, helmet-off; subtitles | `docs/audio/audio-direction.md`, `docs/audio/sound-list.md` (99 rows) | Lead Artist | ✅ Draft |

### Management documents
| Standard document | Purpose | Our file | Owner | Status |
|---|---|---|---|---|
| Schedule / roadmap | Allocation of time and work; iteration model (ACEDT); stage exits use PROCESS.md definition of done | `ROADMAP.md` | Producer | ✅ |
| Budget | Allocation of resources: effort hours per stage and role, the owner's time, scope check, cut list | `docs/production/budget.md` | Producer | ✅ Revision 2 (±50%; re-estimate after Stage 3) |
| Owner actions | One page of things only the owner can do: external reviewers, learner survey, platform checklist | `docs/production/owner-actions.md` | Producer | ✅ Current |
| Risk register | Threats and mitigations | `docs/production/risk-register.md` | Producer | ✅ Round 2 |
| QA plan | Severity levels, test types, invariants, test passes per stage, exit criteria, release checklist | `docs/production/qa-plan.md` | Producer | ✅ Draft 1 |
| Consistency review | Cross-document contradictions with owners | `docs/production/consistency-review.md` | Producer | ✅ Round 1 findings closed on paper |
| Consensus briefs | Positions, common ground, options for ill-structured team decisions | `docs/production/consensus-round2.md` | Producer | ✅ Round 2 decided (T-C, I-C) |
| Process | Iteration gates, definition of done, regression gate, UI and workflow rules, retro log | `PROCESS.md` | Producer (coordinator adds retro rows) | ✅ Draft, reviewed Round 2 |
| Backlog | Owner's requests in their words, acceptance criteria, current iteration, open questions for the owner | `BACKLOG.md` | Producer (coordinator adds requests as they arrive) | ✅ Current |
| Licensing plan | Our licenses (code MIT; art, audio, story, journal, educator materials CC BY 4.0) and third-party obligations | TEAM.md Decision Log; `docs/tech/mod-licenses.md` (third-party) | Producer, Technical Lead | ✅ Decided; LICENSE files at release |
| Team model and decision log | Shared understanding and decisions | `TEAM.md` | Producer (coordinator merges) | ✅ |
| Task reports | What each task did, its handoffs and risks | `docs/team/reports/` | Each role (coordinator files) | ✅ |

### User / player documents
| Standard document | Purpose | Our file | Owner | Status |
|---|---|---|---|---|
| Player guide / tutorial | How to play | — (Landing onboarding in game; guide in Stage 4) | Lead Game Designer | 📅 Stage 4 |
| Educator guide and worksheets | How to teach with it; "Requires Java Edition"; IT sheet; paper reflection fallback; guidance setting | `docs/learning/educator-guide.md` | Lead Instructional Designer | 🟡 Draft (worksheets in Stage 4) |
| Teacher quick-start (slice) | One page for running the Stage 3 slice | `docs/learning/teacher-quickstart-slice.md` | Lead Instructional Designer | ✅ Draft |
| Server and setup guide | How a teacher installs and runs a class | `docs/tech/server-setup.md` (input in `docs/tech/school-deployment.md`) | Technical Lead | 📅 Stage 4 (scheduled) |
| Maintenance plan and user-generated content guide | Support window, version updates, sharing challenge datapacks | — | Producer, Technical Lead | 📅 Stage 7 |

---

## 2. What Each Discipline Needs

Each discipline needs different information to make decisions. When writing a document, check it serves the readers listed here.

| Discipline | Needs to know | Mainly from |
|---|---|---|
| Producer | Scope, effort, status, risks, decisions, owners | ROADMAP, BACKLOG, budget, risk register, TEAM |
| Technical Lead | Exact behaviors, numbers, data formats, triggers, limits | DESIGN, LEARNING (feedback and logging), story bible (message triggers), art specs |
| Lead Game Designer | Learning goals and constraints, science limits, technical cost, art direction | LEARNING, docs/science, ARCHITECTURE, docs/art |
| Lead Instructional Designer | Which mechanic teaches what; feedback text and triggers; what data is logged | DESIGN, story bible, ARCHITECTURE (action log) |
| Lead Artist | What must be visible, when, and why; characters and settings | DESIGN, story bible, docs/science |
| Subject Matter Expert | What the game claims or simplifies | DESIGN, LEARNING, story bible, journal content |

---

## 3. Traceability: Design Decision → Goal → Performance Requirement

The key difference from entertainment game documentation: we must show **how each design decision links to what learners must be able to do.** This is how the instructional design stays accountable, and it keeps the design consistent as it changes.

The chain:

```
Design element (mechanic, feedback, story beat, art cue)
    → Goal ID (A1–D2, LEARNING.md section 1)
        → Performance requirement (behavior, condition, criterion, LEARNING.md section 0.6)
            → Measure (LEARNING.md section 3)
```

**Rules**
- Every design element that teaches, gives feedback, or measures lists its **goal IDs** where it's defined (DESIGN.md, story bible, art keyframes, UI specs).
- Elements with no goal ID must state their **motivational purpose** (see the fun–learning matrix in DESIGN.md).
- The alignment table in LEARNING.md is the reverse view: goal → elements. The Lead Instructional Designer keeps it in step.
- When a design element changes or is cut, check the chain. A goal that loses its only teaching element, feedback, or measure is a gap to fix before the change lands.

---

## 4. Decision Rationale

When designs are revised after playtests, the team needs to know *why* a decision was made, to keep the right balance between elements.

Every recorded decision (in its owning document, and pointed to from the TEAM.md Decision Log) includes:
- **Decision:** what was chosen.
- **Rationale:** why, including evidence (sources, playtests).
- **Alternatives considered** and why they lost.
- **Trade-offs:** effect on instructional value and market appeal.
- **Revisit if:** the evidence that would reopen it.

Decisions made before this rule are being given rationale as their owning documents are next edited.

---

## 5. Shared Formats

Different disciplines want the same information formatted differently. For content several roles touch, we use **one shared format** that serves all of them.

### 5.1 Feedback and message entries

Used for journal entries, failure reports, prediction results, colony log lines, recorded messages, and debriefs. Programmers need triggers and keys; the game designer needs tone and timing; the instructional designer needs the goal and instructional purpose; the SME needs the claims.

| Field | Who it's for | Example |
|---|---|---|
| `id` | Technical | `failure.dome_breach.first` |
| `type` | All | failure report |
| `trigger` | Technical, Game Design | first dome breach in a world |
| `delivery` | Game Design, Art | colony log, BELL voice text |
| `text.ms` / `text.hs` | All | Middle and high school versions (language-file keys) |
| `goal_ids` | Instructional Design | C2, A1 |
| `instructional_purpose` | Instructional Design | Connects low pressure to air loss; hints at sealing |
| `motivational_purpose` | Game Design | Urgency; first sense of fragility |
| `hint_level` | Game Design, Instructional Design | 1 of 3 (escalates on repeat) |
| `feedback_kind` | Instructional Design, Game Design | `process` (explains what to do differently), `outcome` (result only, like a score), or `feed-forward` (what to try next time) |
| `fade_rule` | Instructional Design, Technical | When this entry shrinks or stops, per player: e.g. "retire after 1 own fix that held; full help again on any repeat" (LEARNING.md fading rules) |
| `science_claims` | SME | "Pressure equalizes through gaps" (source ref) |
| `fiction_note` | SME | none |
| `status` | Producer | draft / SME-reviewed / ID-reviewed / final |

This format maps directly to the datapack JSON the Technical Lead will use, so the document **is** the content source.

### 5.2 Other shared formats
- **Handoffs:** the format in the agent role files.
- **Decisions:** section 4 above.
- **Reports:** saved in `docs/team/reports/`, using the role file report format.

---

## 6. Keeping Documentation Alive

- Documents are updated **in the same task** as the decision, not later.
- Requests that arrive mid-task are logged in `BACKLOG.md` first, then routed (PROCESS.md §1).
- The Producer reviews this map at each roadmap stage: missing documents, stale documents, broken traceability.
- If a document isn't read by anyone, merge or cut it.
