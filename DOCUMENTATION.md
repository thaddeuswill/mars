# Documentation Plan

Documents are how the team communicates decisions and remembers them. This matters even more for us: the team is AI agents with no memory between tasks, so **if it isn't documented, the team doesn't know it.**

The guiding question: *what does each document need so the team has the information it needs, when it needs it, to make design decisions?*

Owner of this file: Producer.

---

## 1. Document Map

Standard game and instructional design documents fall into four categories (Fullerton, 2008; Schell, 2008). LGD adds instructional analysis documents and learning objectives, methods, assessment, and feedback.

### Design documents
| Standard document | Purpose | Our file | Owner | Status |
|---|---|---|---|---|
| Game Design Overview | High-level design and audience, for management | `README.md` (summary) + `DESIGN.md` intro | Producer / Lead Game Designer | 🟡 Thin: needs a one-page overview |
| Detailed Design Document | All mechanics and interfaces | `DESIGN.md`, `docs/design/` | Lead Game Designer | 🟡 Mechanics yes; UI specs not yet |
| Story Overview Document | Dialog, narration, settings, characters | `docs/design/story-bible.md` | Lead Game Designer | ✅ Draft |
| Audience analysis | Who the learners are | `LEARNING.md` section 0.1–0.2 | Lead Instructional Designer | ✅ Draft |
| Content analysis | What must be learned, by type | `LEARNING.md` section 0.3; `docs/science/` | Lead Instructional Designer, SME | ✅ Draft |
| Task analysis | The steps and decisions players must master | — | Lead Instructional Designer | ⬜ Missing |
| Media analysis | Why a Minecraft modpack, and which in-game media carry which content | Partly `LEARNING.md` section 2 | Lead Instructional Designer | ⬜ Missing as an analysis |
| Learning objectives, methods, assessment, feedback | The instructional design | `LEARNING.md` sections 1–4 | Lead Instructional Designer | ✅ Draft |
| Science reference | Source of truth for the science | `docs/science/` | SME | ✅ Draft |

### Technical documents
| Standard document | Purpose | Our file | Owner | Status |
|---|---|---|---|---|
| Technical Design Document | Software engineering, networking | `ARCHITECTURE.md`, `docs/tech/` | Technical Lead | 🟡 In progress |
| Pipeline Overview | How to get art, data, and text into the game | — | Technical Lead with Lead Artist | ⬜ Missing |
| System Limitations | What the target computers can handle | — (research under way in `docs/tech/`) | Technical Lead | ⬜ Missing |
| Art Bible | Common look and feel for all artists | `docs/art/style-guide.md` | Lead Artist | 🟡 In progress |
| Concept Art Overview | What the game will look like before it's built | `docs/art/planetary-keyframes.md` (partly) | Lead Artist | ⬜ Needs visual mockups |

### Management documents
| Standard document | Purpose | Our file | Owner | Status |
|---|---|---|---|---|
| Schedule / roadmap | Allocation of time and work | `ROADMAP.md` | Producer | ✅ |
| Budget | Allocation of resources | — | Producer | ⬜ Missing. For a design exercise: an **effort budget** in hours per stage and role. |
| Risk register | Threats and mitigations | `docs/production/risk-register.md` | Producer | ✅ |
| Team model and decision log | Shared understanding and decisions | `TEAM.md` | Producer (coordinator merges) | ✅ |
| QA plan | Testing and bug process | — | Producer | ⬜ Missing |

### User / player documents
| Standard document | Purpose | Our file | Owner | Status |
|---|---|---|---|---|
| Player guide / tutorial | How to play | — (planned, Stage 4) | Lead Game Designer | ⬜ |
| Educator guide and worksheets | How to teach with it | — (planned, Stage 4) | Lead Instructional Designer | ⬜ |
| Server and setup guide | How a teacher installs and runs a class | — | Technical Lead | ⬜ |

---

## 2. What Each Discipline Needs

Each discipline needs different information to make decisions. When writing a document, check it serves the readers listed here.

| Discipline | Needs to know | Mainly from |
|---|---|---|
| Producer | Scope, effort, status, risks, decisions, owners | ROADMAP, risk register, TEAM |
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
- The Producer reviews this map at each roadmap stage: missing documents, stale documents, broken traceability.
- If a document isn't read by anyone, merge or cut it.
