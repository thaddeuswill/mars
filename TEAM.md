# Team Shared Mental Model

A shared mental model (SMM) is what every team member knows the same way: the task, the team, the vocabulary, and how we work. It lets members explain and predict each other's work, coordinate without spelling everything out, and adapt as the task changes (Cannon-Bowers et al., 1993).

**Why this file exists.** The LGD team is made of AI agents that start every task with no memory of earlier ones. A human team builds its shared model by working together over time. Ours has to be **written down**, read at the start of every task, and updated after every task. This file is that model.

### The loop

```
  team interactions ──► communication and coordination ──► shared mental model
         ▲                                                          │
         └──────────── better communication, less spelled out ◄────┘
                                  │
                                  ▼
                          team performance
```

1. Every agent **reads this file first**, before its role file and the task.
2. Agents work and communicate through reports and handoffs.
3. Every report ends with **Shared model updates**: new terms, decisions, lessons, or expectations the team should share.
4. The coordinating session merges those updates here. The Producer reviews this file at each roadmap stage.
5. The next task starts from a richer model, so less needs to be explained.

---

## 1. Task Model: What We're Making

- **The game:** an educational Minecraft modpack. Players terraform a hostile Mars in three phases, Survival Desperation → Industrial Geo-Engineering → Biome Seeding & Equilibrium, ending when they can remove the helmet and build the colonization beacon.
- **Learners:** ages 12–18, in two grade bands, aligned to NGSS. Played in classrooms (classroom mode, ~11–12 hours) and at home (~40 hours).
- **Two goals:** instructional value **and** market appeal. Neither wins by default. See "Two Goals" in `DESIGN.md`.
- **Nature of the problem:** ill-structured. No single right design; most big decisions need several disciplines.
- **Design pillars:** the planet is the quest log; every change is visible; pressure, not grind; compact before wide; your progress provokes the planet.
- **Where we are:** see "Where We Are Now" in `ROADMAP.md`.

## 2. Team Model: Who Knows What

Knowing who to ask is part of the shared model.

| Role | Expert in | Ask them about | Needs from others | Tends to argue for | Expects design to change |
|---|---|---|---|---|---|
| Producer | Scope, schedule, risk, release | "Can we afford this?" "Who owns this?" | Effort estimates, risks, status | Feasibility, shipping | Moderately; wants changes planned |
| Technical Lead | Code, tools, data, LMS | "Can we build this, and what does it cost?" | Clear specs from design and learning | Simplicity, performance, privacy | Moderately; prefers stable interfaces |
| Lead Game Designer | Mechanics, balance, story, UI, playtests | "Is it fun? Does it fit the loop?" | Objectives, science limits, art direction, tech constraints | **Appeal** | Often |
| Lead Instructional Designer | Learners, objectives, assessment, feedback | "Will they learn it? How will we know?" | Mechanics, science content, data | **Instructional value** | Less often (watch this gap) |
| Lead Artist | Visual style, visible progress, readability | "How will players see it?" | Design specs, science imagery | **Appeal**, clarity | Often |
| Subject Matter Expert | Mars and Earth science, learners as a population | "Is it true? What do students get wrong?" | What the game simplifies | **Accuracy**, instructional value | Less often |

Healthy disagreement is expected. These leanings are why ill-structured problems go to review by several roles.

## 3. Shared Vocabulary

Use these words the same way everywhere.

| Term | Meaning |
|---|---|
| **Planetary Index** | The single, global set of planet values (pressure, temperature, hydrosphere, O2, CO2, biomass). The one source of truth. |
| **Contribution** | A rate a machine, flora, or event adds to or removes from an Index value. |
| **Gate** | A world-state condition that unlocks the next content. |
| **Lock in** | Once a gate is reached it stays unlocked, even if values later fall. (Breathable air is the exception: a live check.) |
| **Gameplay phase** | Phase 1, 2, or 3 of play. Not the same as a **roadmap stage**. |
| **Roadmap stage** | Development stage 0–7 in `ROADMAP.md`. |
| **Goal ID** | Learning goal code A1–D2 in `LEARNING.md`. |
| **Grade band** | Middle school (6–8) or high school (9–12) depth setting. |
| **Classroom mode** | Config preset: faster pacing, teacher tools, world pause. |
| **Colony Record** | In-game page where learners see which goals they've shown. |
| **Planetary Monitor** | In-game block showing Index values, trends, and causes. |
| **Action log** | Local, anonymous record of player actions, for assessment. |
| **PE / SEP / CCC** | NGSS performance expectation / science and engineering practice / crosscutting concept. |
| **Well-structured / ill-structured** | Problem types; decide how to handle and route a problem. See the agent role files. |
| **Handoff** | A message from one role to another in the standard format (To, Problem type, Problem, Context, Your view, Needed from them, Done when). |

## 4. Process Model: How We Work

- **Who owns what:** see the table in `README.md`. Edit only your own files; hand off the rest.
- **Task flow:** user or Producer sets a task → role does it → report with handoffs → coordinating session merges, commits, and routes handoffs.
- **Well-structured problems:** solved by one role, reported.
- **Ill-structured problems:** framed, viewed from several disciplines, options with trade-offs for both goals, routed to the Producer for review or to the user for a values or scope call.
- **Where decisions live:** gameplay in `DESIGN.md`, learning in `LEARNING.md`, technical in `ARCHITECTURE.md`, schedule in `ROADMAP.md`. This file keeps a short log pointing to them.
- **Evidence beats argument:** playtests, prototypes, and sources settle disagreements where possible.

## 5. What Teammates Can Count On

Implicit expectations, so we don't have to restate them each time.

- Every claim of fact has a source or is marked **unverified**.
- Every report uses the standard format and names the problem type.
- No one changes another role's files.
- No one reopens a settled decision without new evidence; concerns are flagged, not acted on.
- Text for students is short and plain (about grade 6–7 at middle school depth).
- Nothing personal about players ever leaves the server's computer.
- No one commits or pushes except the coordinating session.

## 6. Decision Log

Short pointers. Details live in the owning document.

| Date | Decision | Where | Consensus |
|---|---|---|---|
| 2026-11-26 | Global progress; singleplayer and small servers; ~40 hours | DESIGN.md: Decisions | User decision |
| 2026-11-26 | Custom Mars dimension; gates lock in; fauna scales with infrastructure plus a threshold config | DESIGN.md: Decisions | User decision |
| 2026-11-26 | Ages 12–18, two grade bands; NGSS; classroom mode; local anonymous logging | LEARNING.md: Decisions | User decision |
| 2026-11-26 | ISD / ADDIE as the learning design framework | LEARNING.md | From user research |
| 2026-11-26 | Systems in Java, content in data, glue in KubeJS; server authoritative | ARCHITECTURE.md | Coordinator proposal; not yet reviewed by Technical Lead |
| 2026-11-26 | Two goals: instructional value and market appeal, neither wins by default | DESIGN.md: Two Goals | User decision |

## 7. Building the Shared Model: Four Skills

The shared model grows through four team skills (the development of an SMM depends on them). For an agent team each one needs a concrete practice.

| Skill | What it means here | How we practice it |
|---|---|---|
| **1. Recall prior team experiences** | Know what the team has already done, decided, and learned before starting | Read the Task History (section 10), the Decision Log, and the Learning Log first. Open past reports in `docs/team/reports/` related to your task. Build on them; don't redo or contradict them unknowingly. |
| **2. Interpret team behaviors** | Understand *why* a teammate took a position, not just what it was | When responding to another role's work, state how you read it using the team model (section 2), e.g. "The SME is flagging this because accuracy is their lens; the risk they see is a misconception." Assume good reasons; ask if unclear. |
| **3. Build consensus** | Reach decisions the team can stand behind on ill-structured problems | Use the consensus process (section 9). Look for options that serve both goals before choosing between them. Record how strong the agreement is. |
| **4. Communicate and interact** | Share information and coordinate clearly | Use the shared vocabulary and handoff format. Answer every handoff you receive (accept, question, or push back with reasons). Ask before assuming. Keep messages short and specific. |

## 8. Expectations About Change

Research on LGD teams found that all disciplines agree design elements (mechanics, UI, look and feel, characters) will change during development, but **instructional disciplines expect far fewer changes than gaming disciplines do**. Left unspoken, that gap causes disputes: designers feel blocked, instructional designers feel the ground keeps moving. We make the expectations explicit instead.

### 8.1 How often each part is expected to change

| Stability | What | Expect | Changed by |
|---|---|---|---|
| **Fluid** | Mechanics details, balance numbers, UI layout, art, sky and fog values, fauna design, characters, journal wording | Frequent change, after almost every playtest. This is normal, not a failure. | Owning role, with notice (8.2) |
| **Flexible** | Gate thresholds, pacing split, story arc, feedback tools, assessment items, classroom unit plan | Changes at stage boundaries or when evidence shows a problem | Owning role, with an alignment check |
| **Stable** | Learning goals (A1–D2), NGSS mapping, design pillars, two goals, core loop, settled decisions in the Decision Log | Rare. Only with new evidence. | Consensus process (section 9) or the user |

**For instructional roles:** expect fluid items to change often. Judge a design change by whether the **learning goal is still taught, practiced, given feedback, and measured**, not by whether the mechanic stayed the same.

**For gaming roles:** change fluid items freely, but every change that touches how a goal is taught or measured must be visible to the instructional roles. Stable items are the fixed points you design around.

### 8.2 Change process

1. **Fluid change:** make it, then list it in your report under Done, with the goal IDs it touches (if any).
2. **Touches a goal ID:** also send a handoff to the Lead Instructional Designer, who runs the alignment check (LEARNING.md) and replies accept or concern.
3. **Flexible change:** propose it with evidence (playtest, prototype, source), and get the alignment check before it lands.
4. **Stable change:** ill-structured by default. Use the consensus process.

Every design document change that affects a goal ID updates the alignment table in LEARNING.md, so learning value is never lost silently.

### 8.3 Level of agreement, not just agreement

Two roles can both "agree" and still mean very different things. When recording a consensus, note the **level** (agree / can live with it), and note when roles agree on *what* but differ on *how much* or *how often*. Those gaps are where disputes start later.

### 8.4 Known agreement gaps between disciplines

From Coleman et al. (2010), as summarized in *Design and Development of Training Games*, Table 2.2. Both disciplines agreed with every statement, but one agreed **more strongly**. These are where our disputes will most likely come from, so the team takes a position on each up front.

| Issue | Agreed more strongly | Team position |
|---|---|---|
| Design element changes are expected to be infrequent | Instructional | Design elements are **fluid** and change often (8.1). Learning goals are what stay stable. |
| Targeting learning objectives is important | Instructional | Yes. Every mechanic serves a goal ID and a pillar. |
| Learning objectives are the primary game drivers | Instructional | Objectives decide **what** the game must teach; game design decides **how**. The best answer is a mechanic that *is* the lesson. |
| Usability is the most important evaluation criterion | Game | Usability is a **must-pass check**, not the whole evaluation. We evaluate learning and appeal too (DESIGN.md: Two Goals). |
| The game must produce measurable learning outcomes | Instructional | Yes. See LEARNING.md section 3. |
| Game goals and learning goals must align | Instructional | Yes. The win condition (equilibrium) *is* the main learning outcome. |
| Realistic games are preferred | Instructional | Realistic where realism teaches (pressure, water, greenhouse effect). Stylized where it doesn't (timescales, fauna). Every simplification gets an honest journal note. |
| Fantasy should provide a useful metaphor for learning | Instructional | Yes. Fictional elements must stand for something real: e.g. the fauna stand for ecosystem response and unintended consequences (B5, D1). If a fictional element teaches nothing, it needs a story or appeal reason. |
| Feedback is considered for its motivational value | Game | Both. Every feedback element is designed and reviewed for **motivational and instructional** value. |
| Feedback is considered for its instructional value | Instructional | (See above.) |
| Learning has priority over fun | Instructional | Neither has priority by default. Conflicts go to the consensus process with playtest evidence. |
| Fun has priority over learning | Game | (See above.) |
| A learner having fun is more likely to learn | Game | We treat this as a **hypothesis to test**: playtests record a fun rating alongside learning gain. |
| Better for someone to see themselves as a "player" than a "learner" | Game | **In the game, they're a player.** Game text never says "learner", "lesson", or "objective"; the Colony Record uses colony language. "Learner" belongs in teacher-facing materials only. |

### 8.5 Harnessing differences about game goals

Everyone wants the best product, but each discipline sees "best" differently. We don't average those views away; we **combine** them, so every aspect of the design is covered. A view only one discipline holds is often the thing the others would have missed.

| Area of difference | Game design view | Instructional design view | Team definition (combines both) |
|---|---|---|---|
| **What a successful game is** | Players engage, finish, and recommend it; it gets downloaded and rated well | Learners meet the objectives; measurable learning gain | Success means passing **every** part of the Success Scorecard below. Failing either side is failing. |
| **Importance of objectives in design decisions** | One input among many; fun and flow come first | The primary driver | Objectives set **what** must be learned; design chooses **how**. A decision that serves no goal ID and no pillar is cut. |
| **Evaluation criteria** | Usability, engagement, retention | Learning gain, objective mastery | The Scorecard uses both sets, plus accuracy and feasibility. |
| **Measurable learning outcomes** | Useful, but must not break play | Essential | Measure **inside the play** (action log, prediction prompts, Colony Record), never by stopping the game for a test. Pre/post tests happen outside the game. |
| **Aligning game goals and learning goals** | Nice when it happens | Required | Required, and done through mechanics: the win condition (equilibrium) *is* the main learning outcome. The alignment table in LEARNING.md checks it. |

#### Success Scorecard

Used at the end of each roadmap stage from the prototype on, and for the final release decision. Each discipline owns its criteria, but the whole team signs off.

| Lens | Criterion | Owner | Draft target |
|---|---|---|---|
| Usability | Players can use controls, Monitor, journal, and machines without outside help | Lead Game Designer | Must pass: no blocking usability issue in playtests |
| Appeal | Players enjoy it and want to keep going | Lead Game Designer, Lead Artist | Average fun rating ≥ 4/5; most students ask to keep playing |
| Learning | Measurable gain on the pre/post test; Colony Record goals met | Lead Instructional Designer | Targets set after first playtest (LEARNING.md section 3) |
| Alignment | Every goal ID is taught, practiced, given feedback, and measured | Lead Instructional Designer | No gaps in the alignment table |
| Accuracy | No content teaches a misconception; simplifications are labeled | Subject Matter Expert | Zero open accuracy issues rated "wrong" |
| Feasibility | Runs on target classroom hardware; teachers set it up quickly | Technical Lead | Playable frame rate on modest hardware; setup under 30 minutes |
| Delivery | On schedule and in scope for the stage | Producer | Stage exit criteria met |

#### Perspective round

At the start of any design review, before anyone critiques, each role answers in one or two sentences: **"From my lens, what would make this succeed?"** That puts every discipline's definition of success on the table first, so the critique covers all of them and differences are seen as coverage, not opposition.

## 9. Consensus Process (ill-structured decisions)

1. **Frame:** the Producer (or the raising role) states the problem in shared vocabulary and lists which roles must weigh in.
2. **Positions:** each role gives its view, its lens (from section 2), and its effect on both goals.
3. **Common ground:** identify what everyone already agrees on, and any option that serves both instructional value and appeal.
4. **Options:** narrow to two or three, with trade-offs.
5. **Check agreement:** each role marks the leading option as:
   - **Agree**
   - **Can live with it** (note the concern)
   - **Block** (only with a reason tied to a learning goal, pillar, science accuracy, safety, or feasibility)
6. **Decide:** consensus if no blocks. If a block remains, the user decides, or a playtest or prototype settles it.
7. **Record:** add to the Decision Log with the consensus level and any concerns to revisit.

## 10. Task History

Every completed task, so teammates can recall prior work. Full reports are saved in `docs/team/reports/`.

| Date | Role | Task | Output | Report |
|---|---|---|---|---|

## 11. Team Learning Log

What we've learned about working together. Add an entry whenever a task teaches the team something about coordination, communication, or the problem.

| Date | Lesson | Source |
|---|---|---|
| 2026-11-26 | Agents start with no memory, so the shared model must be written down and read first. | Team setup | User decision |
| 2026-11-26 | Gameplay phases and roadmap stages were easy to confuse; the vocabulary now separates them. | Early docs review |
| 2026-11-26 | Each discipline defines success differently. The team now uses a combined Success Scorecard and a perspective round at reviews (TEAM.md 8.5). | User research |
| 2026-11-26 | Disciplines agree on the same LGD issues at different strengths (Coleman et al., 2010). The team now holds an explicit position on each (TEAM.md 8.4). | User research |
| 2026-11-26 | Instructional and gaming roles expect different amounts of design change. Stability levels and a change process now make that explicit. | User research on LGD teams | User decision |
