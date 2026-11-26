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

- **This is a design exercise.** No real students will use it. We design everything as if they would, as practice in student-centered learning game design. Treat learner needs, NGSS, classroom use, and minors' privacy as real requirements.

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
| Lead Artist | Visual style, visible progress, readability, audio | "How will players see it?" | Design specs, science imagery | **Appeal**, clarity | Often |
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
| **Colony Record** | System name for the page where players see which goals they've shown (in-game title: Keeper's Record). |
| **Planetary Monitor** | In-game block showing Index values, trends, and causes. |
| **Keeper** | What the story's characters call the player (approved). |
| **Thawlings** | Fictional animals descended from hypothetical underground microbial life (option B). Their Hydrosphere stage is a "later life stage", never a mutation. Working name; not yet cleared against existing works. |
| **Planet** | One Mars dimension with its own Planetary Index, gates, story, and fauna. Standard play has one; a class server hosts several. |
| **Group planet** | One planet (not one world) shared by 3–5 students. |
| **Pseudonymous** | Students have random per-world IDs (`P-XXXXX`); the mapping file never leaves the server; every export uses IDs only. Use this word, not "anonymous", for the action log. |
| **Sol** | One in-game day: 20 minutes of simulation time. Stops while paused or when the server is off; sleeping and the daylight cycle never advance it. All holds and delays are counted in sols. |
| **Machine signature** | How much a machine adds to fauna pressure. |
| **Colony post** | An optional, rotating role for a player on a group planet. |
| **Drift event** | An event that pushes a value out of band during a hold. |
| **Regolith Outgasser** | The Phase 1 pressure source, unlocked at Dome stable. |
| **Air window** | Monitor display of the breathable, fire-safe O2 range. |
| **Keyframe** | A visual data point the client interpolates between, keyed on a named Index input. |
| **Explicit / implicit / player-driven tasks** | Task types (Chapter 6): explicit tasks are dictated by the game (gates, forecasts); implicit tasks are expected but not dictated (stay alive, keep the dome sealed); player-driven tasks are self-set (what to build, which interventions). |
| **Baseline reward** | The fair acknowledgment players expect for completed work (for us: gate moments, Keeper's Reports, ending cards). Missing it feels like cheating; adding more tangible rewards on top can harm intrinsic motivation. |
| **Player skill vs. avatar skill** | Player skill is what the real person learns; avatar skill is a number on the character (XP, levels). We reward player skill: the Keeper has no levels, and progress is planet state. |
| **Product / task / customer-SME iteration** | The three levels of iteration (Chapter 9, TEAM.md section 4): repeated improved versions of the game; non-linear progress on tasks within a phase; and reviews with the customer and SMEs at key points. |
| **Internal / external SME** | An internal SME is on the team and gives frequent feedback on small details. An external SME comes from outside (e.g., a planetary scientist or science teacher) and gives critical review. We have only an internal SME. |
| **Behavioral indicator** | An observable behavior in the game that shows understanding (or lack of it) of a learning goal. Each goal needs indicators the game can measure, and a way to tell good from poor ones. |
| **Embedded assessment** | Assessment built into play (the action log, forecasts, Keeper's Record). Four purposes: inform an instructor, tailor in-game instruction, give feedback, evaluate against standards. |
| **Straw-man asset** | A simple placeholder (box, circle, text box) used in early prototypes so core logic can be tested without asset effort. Also a concept design shown to the owner and SME for face validity. |
| **Plumbing prototype** | A thin end-to-end prototype of a back-end capability (e.g. the LMS export) that shows what is feasible before the full build. |
| **Boiling point at current pressure** | A derived Monitor value: the temperature at which water boils at the planet's current pressure (0 °C at 0.61 kPa, about 33 °C at 5 kPa). It alone drives the ice states and the heavy vapor wisps. |
| **Iteration** | One round of agent tasks, or one build-and-test loop. Iterations sit inside roadmap stages. |
| **Face-validity check** | The owner or SME judging whether a concept looks like what it claims to be. |
| **Risk gate table** | For a change: what could break · the check that catches it · the result. |
| **Signal** | A read-only world-state value (storm strength, surface radiation, fauna pressure) published by one system module into the planet snapshot. Visuals and conditions read it like an Index parameter, but it isn't one. |
| **What caused it?** | The in-game name of the cause-checking prompt (the old "Explain it" check). |
| **Shift Briefing** | A short phase-start message from BELL that works as an advance organizer. |
| **Landing** | The first-shift checklist that teaches controls inside the story. |
| **Training Hall** | The class lobby on Earth, before the story timeline, where unassigned players wait. |
| **"Useful now" list** | Up to three recommended builds shown beside the next-gate bar. |
| **Asserts** | The condition a story beat that makes a claim about the world must satisfy. |
| **Lock point** | The moment a forecast closes, before any hint, warning, or example that would give the answer away. |
| **Interleaved forecast (FI)** | A forecast that brings back an earlier idea in a new situation, with no topic label. |
| **Outcome form / re-scaffold** | Outcome form: feedback shrunk to its result after the player's own fix held. Re-scaffold: full process feedback returns after an error. |
| **"Your shift" block** | The per-player after-action review inside a Keeper's Report. |
| **"Needs a look" flag** | A teacher-only pointer in the class overview. Never a grade. |
| **Player ID slip** | A paper slip carrying a student's pseudonymous ID for tests. |
| **`guidance`** | A setting for how much help the game gives (gentle / standard / expert). Separate from the grade band, which sets science depth. Thresholds never change with it. |
| **Fidelity plan codes** | Asset list codes: SM straw-man, C concept, F-E final essential, F-S final simple. |
| **`_hud` / `_paper` variants** | Two versions of each icon: one for dark HUD backings, one for light paper sheets. |
| **Visual twin / honest silence** | A visual twin shows a sound's meaning without sound. An honest silence is a sound deliberately left out because the real thing is silent. |
| **Teacher review pack** | The classroom-judgment half of the external review (the SME's science pack is the other half). |
| **Look-first card** | The forecast card shown before the Monitor opens, when a player has an unanswered forecast (`forecast.look_first`). |
| **"Check:" pointer / Notice line** | A short pointer inside a forecast saying where to look; a ≤ 10-word line in each Shift Briefing saying what to notice. |
| **Shift log / "Unlocks at…" line** | An optional end-of-session log; a quiet line on a locked item saying what unlocks it. |
| **Ice extractor** | The only name for the machine that pulls water from deep ice ("deep-crust water extractor" is retired). |
| **Owner platform checklist** | At each stage exit, a record of which platforms the owner checked (Windows, macOS, weak hardware) and which weren't. |
| **Budget revision** | The budget is re-issued whenever decisions change scope. The current one is revision 2 (6,179 team hours; about 89 owner hours). |
| **Signal `mars:solar_spike`** | The solar-event signal that drives the aurora. Unlike surface radiation, it is not attenuated by pressure. |
| **CO2 is the O2 feedstock** | Phase 3 rule (default, from the SME): photosynthesis swaps CO2 for O2 one for one; decay and fire swap back; scrubbers lock CO2 into rock and make no O2. |
| **`ms_related` / `hs_related`** | NGSS map fields for a real but partial fit. Shown to teachers, never counted as alignment. |
| **Weather low** | The Phase 3 hold-time drift event that replaces the "polar winter" dip: a passing low-pressure system, warned at least 1 sol ahead. |
| **Primary / secondary sounds** | Primary sounds are never simplified (alarms, feedback cues, pressure beds, Outgasser, dosimeter, fire, helmet-off, core thawling sounds). Secondary sounds may be simple synthesized versions under cut item 5. Both keep their subtitle and visual twin. |
| **Action log** | Local record of player actions for assessment: pseudonymous on the server, anonymous in every export. |
| **Forecast** | In-game name for a prediction prompt. |
| **Keeper's Record** | In-game title of the Colony Record. |
| **Keeper's Report** | In-game name for a phase debrief. |
| **Own evidence** | Evidence carrying the player's pseudonymous ID. Only individual summative evidence is graded; group evidence is formative. |
| **Adaptation (RETAIN) / Immersion (RETAIN)** | RETAIN's meanings (the learner changes behavior; intellectual investment). Different from our "adaptive" and "immersive"; always qualify. |
| **PE / SEP / CCC** | NGSS performance expectation / science and engineering practice / crosscutting concept. |
| **Well-structured / ill-structured** | Problem types; decide how to handle and route a problem. See the agent role files. |
| **Handoff** | A message from one role to another in the standard format (To, Problem type, Problem, Context, Your view, Needed from them, Done when). |

### Contested terms: defined for this team

Game and instructional designers use **high fidelity, immersive, adaptive, interactivity,** and **engagement** without consistent definitions, within or across disciplines (Coleman et al., 2010). So we define them here, for this project, at the start. **Don't use these words without the qualifier**: say "functional fidelity", not just "high fidelity".

| Term | Our definition | Types we distinguish | Where we stand | How we check it |
|---|---|---|---|---|
| **Fidelity** | How closely the game matches the real thing | **Physical:** looks, sounds, and feels like it (visual and audio). **Functional:** behaves like it (cause and effect, rough size of effects). **Cognitive:** demands the same thinking the real task does (deciding, predicting, diagnosing). **Temporal:** real timescales. | **High cognitive** and **high functional** (the Planetary Index relationships must be right, and players must reason like planetary engineers). **Moderate physical** (Minecraft blocks, real Mars colors). **Low temporal** (centuries compressed to hours, stated in the journal). Physical and functional detail beyond what the thinking needs is extraneous load. | SME accuracy review; fantasy audit in DESIGN.md |
| **Immersive** | The player feels inside the colony and its story, not outside a lesson | **Diegetic:** feedback and UI exist in the world (Monitor, journal, colony log). **Narrative:** the fiction stays consistent. **Flow:** play isn't interrupted. | All three. No pop-up quizzes; tests happen outside the game; "player, not learner" language. | Playtest presence questions; count of breaks out of the game world |
| **Adaptive** | The game changes in response to the player | **World:** the planet responds to actions (always on). **Difficulty:** fauna scale with infrastructure. **Feedback:** hints get more specific after repeated failures of the same kind. **Configured:** grade band and classroom mode, set by a teacher, not automatic. | All four as listed. We do **not** do automatic personalization from a learner model at launch. | Action log shows the adaptations firing; playtests check that they help |
| **Interactivity** | How the player's actions change what happens | **Agency:** the player decides and acts (vs. watching). **Responsiveness:** how quickly and visibly the game reacts. **Choice load:** how many options are open at once. | High agency (players run the planet; no cutscenes doing the work for them); responsiveness through the Monitor and world changes; choice load kept low early and raised by phase, to manage cognitive load. | Walkthroughs count choices per screen; playtests note confusion |
| **Engagement** | How much players invest in the game | **Behavioral:** time on task, returning to play, finishing. **Emotional:** enjoyment, interest, caring about the colony. **Cognitive:** effort spent thinking: checking the Monitor, trying predictions, fixing root causes. | All three matter; **cognitive engagement is the one tied to learning.** High behavioral engagement alone (e.g. grinding) doesn't count as success. | Action log (behavioral, cognitive); playtest fun rating and survey (emotional) |

If a document or report uses one of these terms differently, flag it in the review. New terms that turn out to mean different things to different roles get added here.

## 4. Process Model: How We Work

- **Who owns what:** see the table in `README.md`. Edit only your own files; hand off the rest.
- **Task flow:** user or Producer sets a task → role does it → report with handoffs → coordinating session merges, commits, and routes handoffs.
- **Well-structured problems:** solved by one role, reported.
- **Ill-structured problems:** framed, viewed from several disciplines, options with trade-offs for both goals, routed to the Producer for review or to the user for a values or scope call.
- **Process and requests:** `PROCESS.md` (how an iteration runs, the definition of done, UI and workflow rules, the retro log) and `BACKLOG.md` (every owner request in their words, with acceptance criteria). Read both at the start of a task.
- **Where decisions live:** gameplay in `DESIGN.md`, learning in `LEARNING.md`, technical in `ARCHITECTURE.md`, schedule in `ROADMAP.md`. This file keeps a short log pointing to them.
- **Evidence beats argument:** playtests, prototypes, and sources settle disagreements where possible.

### Still open (needs the user)
- None at the moment. Open decisions are tracked in BACKLOG.md.

### Design process model: fuzzy ADDIE

No single learning game model dominates, and most teams use some form of ADDIE (Fanning, 2008). We use ADDIE (see LEARNING.md) with three adjustments from the literature:

**1. Nonlinear ("fuzzified") movement between phases** (FIDGE, Akalli & Cagiltay, 2006). Strict, linear ADDIE doesn't fit learning game design. Any role can move back to analysis or design when evidence calls for it: a playtest finding sends us back to design; a new learner insight sends us back to analysis. The roadmap stages are checkpoints, not one-way gates. Moving back is normal, and goes through the change process (section 8.2).

**2. Additions for software-based learning environments** (Fanning, 2008):
| Addition | How we cover it |
|---|---|
| Software production processes | ROADMAP.md stages; ARCHITECTURE.md coding practices and testing |
| Preparing learners to use the environment | Phase 1 onboarding; classroom period 1 (controls and Mars intro); player guide |
| How long it will be operational | Maintenance plan: Minecraft and NeoForge version updates, support window after release *(to add to ROADMAP, Producer)* |
| User-generated content and lessons learned | Data-driven gates and parameters let teachers and players share challenge datapacks and worlds *(to add to ROADMAP, Producer)* |
| Environment-specific summative evaluation | Success Scorecard (8.5) and the pre/post study |

**3. RETAIN as a design and evaluation lens** (Gunter et al., 2008; built on Gagné's nine events, Keller's ARCS, and Bloom's taxonomy). Six elements: **relevance, embedding, transfer, adaptation, immersion, naturalization**. The Lead Instructional Designer reviews the design against it (LEARNING.md).

### Three levels of iteration (ACEDT, Chapter 9)

The multidisciplinary development method in Chapter 9 runs on three kinds of iteration. We use all three, on top of the fuzzy ADDIE above.

| Level | What it is | How we do it |
|---|---|---|
| **Product iteration** | Repeated passes through analysis, core design, experience design, development, and testing, each producing an improved version of the game. Start with paper or rapid prototypes, end with a feature-complete game. | Stage 2 is a fast, cheap prototype; Stage 3 is the vertical slice; later stages add content. When a playtest finds a problem (usability, wrong assumption about what players know, only some objectives taught), we revisit the requirements, prioritize, and work on the highest-priority element first. Shorter iterations are better. |
| **Task iteration** | Within each phase the core tasks have no fixed order, and progress on one reveals issues for the others. | Roles work in parallel in their own files. The change impact check (8.2) and the handoff format keep every discipline aware of key decisions and able to weigh in, which keeps instructional and gaming elements balanced. |
| **Customer/SME iteration** | At every key point, check decisions with the customer and subject matter experts for appropriateness, accuracy, authenticity, and completeness. | The user is the customer. The Subject Matter Expert is an **internal** SME (frequent, small-detail feedback). We have **no external SME** (a planetary scientist or a science teacher) for critical review. That is a known gap: published sources and an optional review by the user stand in until then. |

**Testing iteration (local iteration, Chapter 9).** Alongside the three levels, the build/test loop moves through four versions: **preliminary prototypes** (limited functionality, basic testing), **alpha** (functional; find major issues), **beta** (stable and complete; check user experience and bugs), **candidate release** (quality assurance; harden for release). Integration issues (with other mods, the class server, the educator materials) show up here, so each version gets an integration test pass. Local iteration also means going back to a previous phase to apply or revisit an earlier decision.

**Staged deployment** reduces risk: ship a small amount of content early (our Stage 3 vertical slice), learn what works, then enlarge.

Rule: no phase is "closed". A customer or SME finding at any point can send the team back to any earlier phase.

### When the team debates key issues

The models above stop short of saying **when** an LGD team should debate key design issues or **how** to get everyone actively involved. This schedule fills that gap. Each debate uses the perspective round (8.5) and the consensus process (section 9).

| Stage | Debates to hold | Why then |
|---|---|---|
| 0–1 Concept, pre-production | Definition of success; role of objectives; alignment; measurable outcomes; realism vs. fantasy; contested terms | Cheapest time to settle them. *(Held: see 8.4, 8.5, section 3.)* |
| 2 Prototype | Fun vs. learning with real evidence; usability; functional fidelity of the Index model | First playable evidence |
| 3 Vertical slice | Feedback: motivational and instructional; player language; cognitive load of text and fantasy | First complete experience of the learning layer |
| 4 Production | Fantasy audit for each new element; how often design is changing; scope cuts | Content volume and change are highest |
| 5–6 Alpha, beta | Evaluation criteria and results; revisit 8.4 positions with playtest data | Evidence to confirm or overturn positions |
| 7 Release | Operational lifetime; user-generated content; what we learned as a team | Planning beyond launch |

**Active involvement:** every role gives a perspective-round statement in every scheduled debate. A role that has nothing to add says so explicitly, so silence is never mistaken for agreement.

## 5. What Teammates Can Count On

Implicit expectations, so we don't have to restate them each time.

- Every claim of fact has a source or is marked **unverified**.
- Every report uses the standard format and names the problem type.
- No one changes another role's files.
- No one reopens a settled decision without new evidence; concerns are flagged, not acted on.
- Text for students is short and plain (about grade 6–7 at middle school depth).
- Nothing personal about players ever leaves the server's computer. The action log is pseudonymous on the server and anonymous in every export.
- No one commits or pushes except the coordinating session.

## 6. Decision Log

Short pointers. Details live in the owning document.

| Date | Decision | Where | Consensus |
|---|---|---|---|
| 2026-11-26 | Global progress; singleplayer and small servers; ~40 hours | DESIGN.md: Decisions | User decision |
| 2026-11-26 | Custom Mars dimension; gates lock in; fauna scales with infrastructure plus a threshold config | DESIGN.md: Decisions | User decision |
| 2026-11-26 | Ages 12–18, two grade bands; NGSS; classroom mode; local logging (now pseudonymous on the server, anonymous in every export) | LEARNING.md: Decisions | User decision |
| 2026-11-26 | ISD / ADDIE as the learning design framework | LEARNING.md | From user research |
| 2026-11-26 | Systems in Java, content in data, glue in KubeJS; server authoritative | ARCHITECTURE.md | Coordinator proposal; the Technical Lead adopted it in its rewrite of ARCHITECTURE.md (round 1) and raised no objection |
| 2026-11-26 | Two goals: instructional value and market appeal, neither wins by default | DESIGN.md: Two Goals | User decision |
| 2026-11-26 | **Design exercise:** no real students will use this. Everything (classroom mode, NGSS, teacher tools, minors' privacy) is designed as if they would, as practice in student-centered design. Real playtests mean the user playing, plus agent walkthroughs. | TEAM.md | User decision |
| 2026-11-26 | Equilibrium pressure target ~75 kPa so 21% O2 is breathable; Breathable gate uses O2 partial pressure (≥ ~16 kPa) and CO2 partial pressure (≤ 0.5 kPa) | DESIGN.md | User decision on SME recommendation |
| 2026-11-26 | Multipliers (classroom, multiplayer) scale rates and hold times only, never physical thresholds | DESIGN.md, LEARNING.md | Coordinator, on SME accuracy finding |
| 2026-11-26 | Multiplayer scaling uses a static group-size rate scale (set in config, not from who's online); thresholds never scale | DESIGN.md: Multiplayer Scaling | Lead Game Designer; follows the user-approved accuracy rule. User may override. |
| 2026-11-26 | Game state and learning evidence are stored separately; rollback never deletes evidence | ARCHITECTURE.md §7 | Technical Lead |
| 2026-11-26 | The pack requires Minecraft Java Edition; it can't run in Minecraft Education | docs/tech/school-deployment.md | Fact (Technical Lead research) |
| 2026-11-26 | We don't paint invisible gases: O2 and CO2 never drive sky or fog color; they show through effects and instruments | docs/art/planetary-keyframes.md | Lead Artist |
| 2026-11-26 | Visuals follow live Index values, not gates | docs/art/planetary-keyframes.md | Lead Artist |
| 2026-11-26 | Story is player-driven inside a designer frame; story verbs match player actions; onboarding stays in story | DESIGN.md: Story Approach | Coordinator, from user research |
| 2026-11-26 | Class hosting: one server with one Mars dimension per group (option A); one server per group as fallback; student-hosted LAN not supported | ARCHITECTURE.md §6 | User decision on Technical Lead recommendation |
| 2026-11-26 | Stay on Minecraft 1.21.1 | ARCHITECTURE.md | User decision on Technical Lead recommendation |
| 2026-11-26 | Reflections exported in a separate reflections.csv: on by default in classroom mode, off in standard; names scrubbed to IDs; "don't write your name" note; paper fallback | LEARNING.md §5, ARCHITECTURE.md §8.6 | User decision on Technical Lead and Instructional Designer recommendation |
| 2026-11-26 | Rose/mauve Phase 2 sky kept, labeled stylized, pending SME check | docs/art | User decision on Lead Artist recommendation |
| 2026-11-26 | Weather mast prop adopted (honest O2/CO2 world cue) | docs/art, DESIGN.md (to add) | User decision on Lead Artist recommendation |
| 2026-11-26 | Lead Artist owns audio | README.md, agent files | User decision |
| 2026-11-26 | Goal priority tiers: option T-C. Adopt LEARNING.md §1 tiers (Primary A2, A3, A5, B1, B2, B4, D2; Secondary A1, B3, B5, C1, C2, C3; can be deemphasized A4, D1) **plus the quadrant rule**: learning resources follow the goal tier; feature cut order follows the fun–learning quadrant, so Core-quadrant features (fauna options, ending cards, radiation spikes) are cut only after Redesign and Moderation items. Revisit D1's tier after the Stage 3 story test. Any cut that drops a goal below its tier's minimum goes to the user. Fallback if the Game Designer blocks on D1: T-B (D1 to Secondary). | LEARNING.md §1, docs/production/budget.md | User decision on Producer recommendation; Game Designer: agrees with T-C (no block); I-C agree except rule 3 (can live with it) and rule 4 (agree), with two additions |
| 2026-11-26 | Interruption rules: option I-C (see TEAM.md 8.6) | TEAM.md 8.6, LEARNING.md §4.6 | User decision on Producer recommendation; Game Designer: agrees with T-C (no block); I-C agree except rule 3 (can live with it) and rule 4 (agree), with two additions |
| 2026-11-26 | Licensing plan (not legal advice; unverified): our code MIT; our art, audio, story and journal text CC BY 4.0; educator materials CC BY 4.0. Never retexture or redistribute All Rights Reserved assets (Create, FTB Library) and never ship ProbeJS. No LICENSE file is added until release, because this is a design exercise. | docs/tech/mod-licenses.md | User decision on coordinator recommendation |
| 2026-11-26 | Plan one external review (a planetary scientist or a science teacher) of the science and learning design at Stage 5 (alpha). Optional; the owner identifies a reviewer. Until then, published sources stand in. | ROADMAP.md (to add) | User decision on coordinator recommendation |
| 2026-11-26 | The handoff lessons in PROCESS.md and BACKLOG.md are confirmed as wanted | PROCESS.md | User decision on coordinator recommendation |
| 2026-11-26 | Record pages fill from skill, never from activity (answers of 8 or more words; the D2 page no longer fills from opening pages). One scored answer per player per forecast. Grade bands set science depth, not the amount of help. Model-based assessment out of scope at launch. | LEARNING.md | Lead Instructional Designer |
| 2026-11-26 | Proposed: `guidance` setting (gentle / standard / expert). Conditions: at every level failure reports name the cause, forecast results explain, and safety warnings fire; gentle is the classroom default and expert isn't offered in classroom mode; fading still runs inside gentle; the level is logged and exported. Game Designer and Instructional Designer agree; the owner's decision is pending (default: adopt). | DESIGN.md, LEARNING.md §4.10 | Lead Game Designer and Lead Instructional Designer |
| 2026-11-26 | Report → Mensah's reply → Shift Briefing counts as one pop-up; a forecast locks before any message that gives its answer away (content rule CR-F9) | TEAM.md 8.6, LEARNING.md §4.6 | Lead Game Designer and Lead Instructional Designer agree (I-C additions) |
| 2026-11-26 | Proposed: Shift Board C2 credit goes to the player who did the repair (not the claimer). Changes DESIGN.md Group Play; Game Designer to agree. | LEARNING.md §3.0 | Lead Instructional Designer; pending |
| 2026-11-26 | Sleep never skips Mars time; the sky follows simulation time. Storm caps scale gameplay as well as the look. Ice–albedo capped at about 2 °C (invented) plus the real polar CO2 release. Mast gas gauges are fitted at Phase 3 (pending Artist and Instructional Designer). Training Hall lobby. One lead crew voice per phase (Hana, Basu, Moreno); Mensah only in report replies. | DESIGN.md, docs/design/ | Lead Game Designer |
| 2026-11-26 | Keeper's Report is never modal: one log line plus a side-panel entry, opened full screen only by the player's choice or at the next natural pause | docs/design/ui-specs.md §0 | Lead Game Designer, agreed with the Producer; Instructional Designer to confirm |
| 2026-11-26 | Ice rule implemented as data (`ice_rule.json`) using per-chunk cached heat sources with distance falloff, shared by server and client; boiling point from the Antoine equation computed once per planet per step | ARCHITECTURE.md §2.5 | Technical Lead, on the SME's finding |
| 2026-11-26 | Ice rule changed: compare each ice block's local temperature with the boiling point at current pressure (not a pressure band); the flash puddle near a heater is the Phase 1 look. Flame bands corrected (no flame below 14% O2). Verdicts in round2-review §11 stay "accepted, pending external review". | docs/science/round2-review.md | Subject Matter Expert; design edits sent to roles |
| 2026-11-26 | Clouds use a density map (mesh rebuilt on coverage change); heat shimmer is particles only (it carries meaning, so it must look the same for every player); thawlings use vanilla entity animation; art and content live in `pack/assets/` and `pack/data/` built into the mod jar; every goal page fills from player-scope events only; every player and forecast pair gets exactly one result; test strategy adopted | ARCHITECTURE.md §2.5, §2.6.1, §2.13, §3.2, §8.5; docs/tech/test-strategy.md | Technical Lead |
| 2026-11-26 | **Phase 3 CO2 rule A adopted** (CO2 is the oxygen source; plants and vats swap CO2 for O2 one for one; decay and fire swap back; scrubbers turn CO2 into rock and make no O2; an O2 generator splitting water is the fallback; 18.0 kPa CO2 at the Phase 3 gate, invented). Weather low replaces polar winter. Puddle drying 0.05–0.2 sol rising with pressure. Transit tubes are simple paired stations with no vehicle. Training Hall about 15 × 11 × 6 blocks in the overworld, mostly vanilla blocks. Sound production about 260 h; primary and secondary sounds; dosimeter ticks never reach zero (floor invented). | DESIGN.md Phase 3 composition rule; balance-sheet.md §3a; docs/audio/sound-list.md | Lead Game Designer and Lead Artist |
| 2026-11-26 | Forecast fixes applied in LEARNING.md: FI-1 stem reworded; FI-2 rebuilt on a passing weather low (unusable until the Game Designer replaces the Phase 3 "polar winter" event); NGSS map version 4 with `verification` and `ms_related`; "Needs a look" flag numbers set (accuracy below 0.33 after 3 forecasts; 2 repeats after own fix; report unsent for 2 sessions; 1 session present with no own events). Open: dated copies of the debrief 2 graphs and an ice-core CO2 source. | LEARNING.md, docs/learning/ | Lead Instructional Designer |
| 2026-11-26 | NGSS word-for-word check complete: all 21 mapped codes match nextgenscience.org (the "verify each code" caveat can be dropped). Sourced: light delay 3.0–22.3 min (calc), Earth warming 1.09 °C (IPCC AR6, searched not opened), permafrost carbon about 1,460–1,600 Gt, Earth mean 15 °C. Dosimeter background ticks fade to a small floor, never zero. Proposed default: **Phase 3 CO2 rule A** (CO2 is the O2 feedstock; CO2 at the Phase 3 gate 17–19 kPa, invented, around the 18.5 kPa calc; scrubbers are a B4 trade-off; electrolysis as fallback); the Game Designer sizes the Phase 2 rise. | docs/science/round3-review.md; docs/science/ngss-verification.md | Subject Matter Expert; the rule is a default pending Game Designer confirmation |
| 2026-11-26 | The aurora uses its own signal `mars:solar_spike` (not pressure-attenuated). Mekanism and Create are left out of the classroom bundle; per-planet block use is gated at Hydrosphere. Create's jar never goes in an offline bundle (its assets are All Rights Reserved). Entity animations are NeoForge JSON content. Sky time is synced from the simulation clock every tick; sleep never skips Mars time. Chunk heat is sent by our own packet. Training Hall lives in the vanilla overworld. Alarms and feedback cues use the master sound category. Machine cost estimate 52–71 developer-days before art and audio (unverified). Proposed default: CI on GitHub Actions. | ARCHITECTURE.md §2.5, §2.14, §2.15, §2.16, §6.2; docs/tech/mod-licenses.md | Technical Lead; CI host is a default pending the owner |
| 2026-11-26 | Sound-production hours belong to the Lead Artist. The forecast floor of 3 per phase is part of the never-cut line. The QA plan owns exit criteria; `docs/tech/test-strategy.md` owns how automated tests work. Budget revision 2: 6,179 team hours, about 89 owner hours; cut list of 13 items (about 435 h). Proposed default: the three early reviewers add to the Stage 5 external review (not replace it). | docs/production/budget.md, qa-plan.md | Producer |
| 2026-11-26 | Pre-storm haze allowed with a guard (fog only; starts at the warning; the sun's brightness and solar output can't drop before F1 locks). Emitters run on the shared solar grid. Drift events start at the first hold or first established flora. Every storm warned at least 1 sol ahead. Hazard thresholds in balance §6a (invented). F3, F5, F8 lock 1 sol after the trigger. Machine name: ice extractor. | DESIGN.md, balance-sheet.md, docs/design/messages/ | Lead Game Designer |
| 2026-11-26 | One neutral forecast-result sound; subtitles on by default in classroom mode; `guidance` gentle by default in class; journal diagrams counted per mechanism (11 for U-25: 7 essential, 4 simple); mast gas gauges at Phase 3 confirmed decided (Instructional Designer agrees) | LEARNING.md §4.5, §5.1; prompts-and-reflections §4a | Lead Instructional Designer |
| 2026-11-26 | Audio direction and sound list adopted (owner: Lead Artist, `docs/audio/`). Alarms are a closed list of three, for life-threatening events only. Equilibrium sky is paler. Text over the world sits on a strip at 90% opacity or more. Original pixel fonts. | docs/audio/, docs/art/style-guide.md | Lead Artist |
| 2026-11-26 | Proposed defaults (owner may override): music scope (a): one original ending piece, vanilla music off on Mars, none in Phase 1, phase pieces on the cut list; a real Perseverance recording only in the journal, labeled REAL, if the license is verified | docs/audio/audio-direction.md | Lead Artist recommendation; default adopted |
| 2026-11-26 | Round 3 decisions (owner: "change what it covers and do the reccomendation", read as: treat every pending recommendation as decided): **Mekanism and Create optional and late** (unlocked at Hydrosphere; decided after the in-house machine list); **the owner's Stage 5 run is a full classroom-preset run plus spot checks**, not the full 40 hours; **optional delayed post-test in Stage 6** (design artifact); **Stage 2 is a timeboxed throwaway iteration starting on paper**; **offline LMS converter in Stage 5** (turns exported `events.csv` into xAPI statements for a local test store; never ships; no student data leaves the machine); **basic CI at Stage 1 exit** for the fast layers L0–L2; **seek three external reviewers before Stage 3** (a planetary scientist for the ice rule, a climate modeler for ice–albedo and polar frost, science teachers for the transfer key and forecast distractors; owner action); **Shift Board C2 credit goes to the player who did the repair**; **`guidance` setting adopted** (conditions in the row above); **orbital mirror route adopted**; **mast CO2 gauge fitted at the Phase 3 gate**; **"Veteran" hobbyist setting deferred until after Stage 3**; **ice–albedo capped at about 2 °C plus the real polar CO2 release** | BACKLOG.md #16; ROADMAP.md, DESIGN.md, LEARNING.md, ARCHITECTURE.md (to update) | User decision on role recommendations (interpretation) |
| 2026-11-26 | Stage 1 build decisions: the ice rule lives at `data/mars/terrain/ice_rule.json`; `mod/` is a standalone Gradle build without Parchment (its host is unreachable); the dev screenshot helper is client-only, opt-in, and excluded from the shipped jar; inactive self-test classes ship in the jar for Stage 1–3 (default; exclude at release). Pending the owner: accept the Minecraft EULA to run the dedicated-server self-test. | ARCHITECTURE.md, docs/tech/build-plan.md §1a | Technical Lead |
| 2026-11-26 | Blockbench headless MCP is **not run in the sandbox** (default, owner may override): two read-only source reviews found a careful tool surface but an unpinned native-code npm install with install scripts on first render, plus a need for WebGPU and a newer Node than the sandbox has. If wanted, run it on the owner's own machine (or use the Blockbench desktop app directly) with a pinned commit and a minimal environment. | docs/tech/art-preview-options.md (appendix) | Coordinator; owner may override |
| 2026-11-26 | Owner approved the recommended art-preview plan ("do whats reccomended"): (b) read-only source review of the Blockbench headless MCP before anyone runs it, and (c) real in-game screenshots (dev-only client helper plus a headless client run under Xvfb) as a Technical Lead follow-on after the Stage 1 build. Nothing third-party is run without telling the owner first. | docs/tech/art-preview-options.md; BACKLOG.md #19 | User decision |
| 2026-11-26 | Art preview research (docs/tech/art-preview-options.md): the best fit for "everything it can show" is real in-game screenshots (dev-only client screenshot command, headless client under Xvfb with software GL; Technical Lead, after Stage 1). The only MCP found that fits the art workflow is the Blockbench headless MCP (GPL-3.0; unrun; needs newer Node and a GPU; run blocked by the safety classifier pending the owner's choice). Mineflayer-based MCPs and prismarine-viewer are doubtful for modded content. | docs/tech/art-preview-options.md; BACKLOG.md #19 | Coordinator; owner decision pending |
| 2026-11-26 | Art iteration loop: Blender 4.0.2 (apt, per sandbox) renders Minecraft block-model JSON to PNG (`tools/art-preview/`); the owner reviews the pictures and the Artist iterates. It approximates the look; it is not Minecraft. A real in-game screenshot (headless client under a virtual display, using the Technical Lead's build) is the next fidelity step and is unverified. | BACKLOG.md #19; tools/art-preview/README.md | Coordinator, in response to the owner |
| 2026-11-26 | The owner skips the paper playtest and waits for the launchable product (BACKLOG #18). Network allowlist extended (owner approved): maven.neoforged.net and Mojang download hosts, so the Stage 1 mod skeleton can be built here. The paper prototype stays available. | BACKLOG.md #18; ROADMAP.md Stage 1 | User decision |
| 2026-11-26 | Playtests: the user plays the builds; agents add design walkthroughs | ROADMAP.md | User decision |
| 2026-11-26 | Classroom model: groups of 3–5 students share one planet; a class runs several planets | DESIGN.md, LEARNING.md, ARCHITECTURE.md (to update) | User decision |
| 2026-11-26 | Classroom unit stretched to ~17 play periods; keep ~11–12 h of play | LEARNING.md (to update) | User decision on Producer recommendation |
| 2026-11-26 | Teacher CSV export uses anonymous IDs only; no username option | LEARNING.md, ARCHITECTURE.md (to update) | User decision on Producer recommendation |
| 2026-11-26 | Custom journal, not FTB Quests | DESIGN.md (to update) | User decision on Producer recommendation |
| 2026-11-26 | Minimal learning layer moves into the Stage 3 vertical slice | ROADMAP.md (to update) | User decision on Producer recommendation |
| 2026-11-26 | LMS integration after release, opt-in only | ROADMAP.md, ARCHITECTURE.md (to update) | User decision on Producer recommendation |
| 2026-11-26 | Producer owns README.md | README.md | User decision on Producer recommendation |
| 2026-11-26 | Fauna: option B (fictional descendants of hypothetical underground microbial life, clearly labeled) plus real environmental effects (perchlorate dust, ground collapse from melting ice) | DESIGN.md (to update) | User decision on coordinator recommendation |
| 2026-11-26 | Fauna: non-lethal options (deterrents, quiet zones); "later life stage" replaces "mutation"; ending cards describe fauna outcomes without ranking | DESIGN.md, story bible | User decision on Lead Game Designer recommendation |
| 2026-11-26 | Story: player is the "Keeper"; planned-solo-watch backstory approved; recorded messages text-first | story bible | User decision on Lead Game Designer recommendation |

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

**Change impact check.** Changes ripple in ways one discipline may not see. An artist may not realize that narrowing the player's field of view hides a learning cue; a designer may not see that adding choices raises cognitive load. For any flexible or stable change, and any fluid change that touches more than one area, answer:

| Does the change affect… | If yes, tell |
|---|---|
| Mechanics or balance | Lead Game Designer |
| A goal ID, a measure, or feedback | Lead Instructional Designer |
| What the player can see or hear (field of view, color, cues) | Lead Artist and Lead Instructional Designer |
| Story beats or characters | Lead Game Designer |
| A science claim or simplification | Subject Matter Expert |
| Performance, data, or code | Technical Lead |
| The fun strategy (DESIGN.md: Fun) or a fantasy element | Lead Game Designer |
| Number of choices or text on screen (cognitive load) | Lead Instructional Designer and Lead Game Designer |
| Scope or schedule | Producer |

Each affected role replies with its view before a flexible or stable change lands.

**Four questions for every design decision** (from the Chapter 9 core design pages; gaming and instructional strategy are one joint task):
1. How is this instruction part of the game?
2. What is this part of the game teaching, **and how will we know it worked?** (name the goal IDs and the measure)
3. How does this instructional technique affect the gameplay?
4. Does this aspect support or distract from the instruction?

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
| Realistic games are preferred | Instructional | Realistic where realism teaches (pressure, water, greenhouse effect). Stylized where it doesn't (timescales, fauna). Every simplification gets an honest journal note. Rules and audit: DESIGN.md, Fantasy and Authenticity. |
| Fantasy should provide a useful metaphor for learning | Instructional | Yes. Fictional elements must stand for something real: e.g. the fauna stand for ecosystem response and unintended consequences (B5, D1). If a fictional element teaches nothing, it needs a story or appeal reason. |
| Feedback is considered for its motivational value | Game | Both. Every feedback element is designed and reviewed for **motivational and instructional** value. |
| Feedback is considered for its instructional value | Instructional | (See above.) |
| Learning has priority over fun | Instructional | Neither has priority by default. Fun comes *from* the learning activity; features are placed on the fun–learning matrix (DESIGN.md, Fun). Conflicts go to the consensus process with playtest evidence. |
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

### 8.6 Rules for interrupting play with feedback (decided: option I-C)

Feedback must balance keeping players motivated with what's best for learning. Interrupting at the wrong moment (e.g. a critical failure message near the end of a level or session) is frustrating. Draft criteria:

1. **Never interrupt** during danger, combat, or active building. Messages wait in the colony log.
2. **Natural pauses only** for non-urgent feedback: entering the dome, sleeping, opening the Monitor or journal, reaching a gate.
3. **The only blocking interruptions** are (a) life-threatening first-time warnings (first O2 critical, first breach) and (b) phase debriefs at a gate, and the player can defer a debrief.
4. **At most one** non-urgent pop-up message per ~10 minutes of play; the rest are queued.
5. **No new critical failure in the last ~5 minutes** of a classroom session (teacher end-of-session signal).
6. **Every prompt is deferrable** except the life-threatening ones in rule 3.

**Decided (user, on the Producer's brief `docs/production/consensus-round2.md`): option I-C.** Adopt the fuller rule set in LEARNING.md §4.6 (rules 1–11) as the **Stage 3 test configuration**, with two flow guards:
1. An unsent Keeper's Report is re-offered **at most once per session**, plus the "Since last shift" recap line, not at every pause.
2. The caps are config values: pop-ups about 1 per 10 minutes, log lines about 3 per minute (merged when similar). Final numbers are set from Stage 3 action-log counts.

The six rules above stay as the baseline. LEARNING.md §4.6 adds: forecast results first in the queue, limited debrief deferral, no new forecast or report pop-up in the last ~5 minutes of a class, deferred forecasts expire and log as unanswered (never wrong), forecasts issued about 1 sol before the outcome, escalating hints only at natural pauses, nothing important only in a pop-up, every interruption/deferral/expiry logged, one player's prompt never pauses the group.

Owner: Lead Instructional Designer with the Lead Game Designer. The Lead Game Designer's own position was still pending when this was decided; if it blocks on a specific rule, that goes to the user, or the Stage 3 playtest settles it.

### 8.7 Team debriefs

After each stage's evaluation, especially the prototype, the whole team debriefs together:
- Each role critically examines what was built against its Scorecard rows (perspective round first).
- The team looks at the design **as a system**: how the parts fit together to meet the goals.
- Outputs: improvement list with owners, Scorecard results, lessons for the Team Learning Log.
- The Producer runs it; the coordinator saves it in `docs/team/reports/`.

### 8.8 Recommended team communications: our status

Checklist from *Design and Development of Training Games*, Chapter 2, section 6.

**At the outset**
| Area | Recommendation | Status | Where |
|---|---|---|---|
| Documentation | Get every discipline's input on what documents contain and how | 🟡 Document map drafted by the coordinator; each role still to review its needs (DOCUMENTATION.md section 2) | DOCUMENTATION.md |
| Documentation | Version control, used consistently | ✅ Git, every change committed | — |
| Documentation | Agree on definitions of key terms | ✅ | TEAM.md section 3 |
| Game goals | Agree on the customer's requirements and success criteria, including learner performance outcomes | ✅ Customer is the user (design exercise) | DESIGN.md Two Goals; Scorecard 8.5 |
| Game goals | Agree on criteria for prioritizing learning objectives | ✅ Decided (option T-C) | LEARNING.md §1 |
| General design | Discuss authenticity choices and their effect on thinking skills; document decision criteria | ✅ | DESIGN.md Fantasy and Authenticity |
| General design | Decide the role of every instructional and gaming element in the goals | ✅ | Traceability (DOCUMENTATION.md 3); fun–learning matrix |
| General design | Decide the strategy for making it fun for the target players | ✅ | DESIGN.md Fun |
| Feedback | Discuss how feedback supports objectives and motivates; evaluate against those criteria | ✅ Shared format has both purposes | DOCUMENTATION.md 5.1 |
| Feedback | Agree on criteria for interrupting the game for feedback | ✅ Decided (option I-C) | 8.6 |
| Evaluation | Get every discipline's input on prototype evaluation criteria | 🟡 Scorecard drafted; each role still to review its rows | 8.5 |

**Throughout the project**
| Area | Recommendation | Status | Where |
|---|---|---|---|
| Documentation | Document research behind team decisions | ✅ Sources required; science in docs/science | 5, DOCUMENTATION.md 4 |
| Documentation | Document reasons for changes to key elements | ✅ | DOCUMENTATION.md 4 |
| Game goals | Continually check adherence to goals and objective priorities | ✅ Stage exits use the Scorecard; priorities added once set | 8.5 |
| General design | Discuss changes by their effect on multiple elements, with input from each discipline | ✅ | Change impact check, 8.2 |
| General design | Discuss instructional effects of mechanic changes and the reverse | ✅ | 8.2 |
| Evaluation | Debrief together after evaluation | ✅ | 8.7 |
| Fun | Reconcile design changes with the fun strategy | ✅ | Change impact check, 8.2 |

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
| 2026-11-26 | Technical Lead | Round 2: architecture extensions, test strategy, build plan, licensing plan | `ARCHITECTURE.md`, `docs/tech/` | `docs/team/reports/2026-11-26-technical-lead-round2.md` |
| 2026-11-26 | Producer | Round 2: roadmap, budget, QA plan, risks, consensus brief, process review | `ROADMAP.md`, `docs/production/`, `PROCESS.md` | `docs/team/reports/2026-11-26-producer-round2.md` |
| 2026-11-26 | Subject Matter Expert | Round 2: science verdicts, value audit, external-review list | `docs/science/round2-review.md` | `docs/team/reports/2026-11-26-subject-matter-expert-round2.md` |
| 2026-11-26 | Lead Artist | First art direction, keyframes, asset list | `docs/art/` | `docs/team/reports/2026-11-26-lead-artist-art-direction.md` |
| 2026-11-26 | Lead Instructional Designer | Apply decisions, per-student measures, RETAIN, priorities, prompts, rubrics | `LEARNING.md`, `docs/learning/` | `docs/team/reports/2026-11-26-lead-instructional-designer-decisions-applied.md` |
| 2026-11-26 | Technical Lead | Architecture gaps, licenses, school deployment | `ARCHITECTURE.md`, `docs/tech/` | `docs/team/reports/2026-11-26-technical-lead-architecture-gaps.md` |
| 2026-11-26 | Lead Game Designer | Apply decisions, C-01 fix, balance sheet, playtest plan | `DESIGN.md`, `docs/design/` | `docs/team/reports/2026-11-26-lead-game-designer-decisions-applied.md` |
| 2026-11-26 | Subject Matter Expert | Science accuracy and NGSS verification | `docs/science/` | `docs/team/reports/2026-11-26-subject-matter-expert-science-ngss.md` |
| 2026-11-26 | Producer | Consistency review, risk register | `docs/production/` | `docs/team/reports/2026-11-26-producer-consistency-review.md` |
| 2026-11-26 | Technical Lead | Stage 1 build (mod skeleton, empty Mars dimension, tested simulation core) and in-game screenshots | `mod/`, `pack/data/`, `tools/screenshots/`, `docs/tech/launch-guide.md` | `docs/team/reports/2026-11-26-technical-lead-stage1.md` |
| 2026-11-26 | Lead Artist | Phase 1 batch 1 straw-man assets and contact sheet (BACKLOG #19) | `pack/assets/mars/`, `tools/art-preview/`, `docs/art/renders/phase1-batch1/` | `docs/team/reports/2026-11-26-lead-artist-batch1.md` |
| 2026-11-26 | Subject Matter Expert | Round 3: CO2 budget, NGSS verbatim check, sourcing, external review pack | `docs/science/` | `docs/team/reports/2026-11-26-subject-matter-expert-round3.md` |
| 2026-11-26 | Technical Lead | Round 3: owner decisions built into the architecture, rechecks, server setup guide, cost estimates | `ARCHITECTURE.md`, `docs/tech/` | `docs/team/reports/2026-11-26-technical-lead-round3.md` |
| 2026-11-26 | Producer | Round 3: budget revision 2, roadmap, owner actions, risk register, QA plan | `ROADMAP.md`, `docs/production/` | `docs/team/reports/2026-11-26-producer-round3.md` |
| 2026-11-26 | Lead Game Designer | Round 3: guidance, story beats, hazard thresholds, paper prototype | `DESIGN.md`, `docs/design/` | `docs/team/reports/2026-11-26-lead-game-designer-round3.md` |
| 2026-11-26 | Lead Instructional Designer | Round 3: decisions recorded, teacher review pack, forecast re-check | `LEARNING.md`, `docs/learning/` | `docs/team/reports/2026-11-26-lead-instructional-designer-round3.md` |
| 2026-11-26 | Lead Artist | Round 2: audio direction, sound list, weather mast, learning-mechanism visuals | `docs/audio/`, `docs/art/` | `docs/team/reports/2026-11-26-lead-artist-round2.md` |
| 2026-11-26 | Lead Instructional Designer | Round 2: decisions applied, per-student measures, fading plan, educator guide, content rules | `LEARNING.md`, `docs/learning/` | `docs/team/reports/2026-11-26-lead-instructional-designer-round2.md` |
| 2026-11-26 | Lead Game Designer | Round 2: design updates, messages, UI specs, balance, playtest plan | `DESIGN.md`, `docs/design/` | `docs/team/reports/2026-11-26-lead-game-designer-round2.md` |
| 2026-11-26 | Lead Game Designer | Story bible | `docs/design/story-bible.md` | `docs/team/reports/2026-11-26-lead-game-designer-story-bible.md` |

## 11. Team Learning Log

What we've learned about working together. Add an entry whenever a task teaches the team something about coordination, communication, or the problem.

| Date | Lesson | Source |
|---|---|---|
| 2026-11-26 | Parallel agents finished before new team protocols were added, so the coordinator applies them when filing. Protocol changes take effect from the next task. | Story bible and review filing |
| 2026-11-26 | Agents start with no memory, so the shared model must be written down and read first. | Team setup | User decision |
| 2026-11-26 | Gameplay phases and roadmap stages were easy to confuse; the vocabulary now separates them. | Early docs review |
| 2026-11-26 | Fun isn't necessary for learning but is required for appeal and time on task. Fun must come from the learning activity; all features go on the fun–learning matrix. | User research |
| 2026-11-26 | Fantasy must be purposeful: metaphor or motivation, matching activity, no negative learning, low extra load. All fantasy elements are audited in DESIGN.md. | User research |
| 2026-11-26 | Run threshold-vs-curve on events as well as thresholds; content rule CR-F1 now checks that every event a forecast rests on can physically happen in that phase (FI-2's "polar winter" dip was impossible at equilibrium). | Lead Instructional Designer and SME |
| 2026-11-26 | Order checks so each tests the real starting state (a travel check passed only because a later check had loaded the chunk), and keep "a person looks at every screenshot" in the definition of done: the screenshots caught a fall-into-the-void bug the boot checks missed. | Technical Lead |
| 2026-11-26 | Minecraft's datapack folder names are a registry namespace: never put our own JSON in `data/<ns>/dimension/`, `worldgen/`, `dimension_type/`, or other vanilla registry folders. | Technical Lead |
| 2026-11-26 | For visuals, run the numeric checks and look at the renders: the checks found contrast failures, only looking found an upside-down top view, dust that read as holes, and a panel tilted the wrong way. | Lead Artist |
| 2026-11-26 | Breathability is three conditions (O2 partial pressure, total pressure, CO2). Check "ready" against every condition, not the headline one: the Phase 3 curve showed O2 ready 3 sols before CO2. | Lead Game Designer |
| 2026-11-26 | Check mass balance whenever an Index value is made from another ("plants make O2" silently created matter until the CO2 budget was written down). A drift event real in one phase can be impossible in another (polar CO2 freeze-out at equilibrium), so run threshold-vs-curve on events too. | Subject Matter Expert |
| 2026-11-26 | Recheck licenses and dependencies against the exact build we pin at every stage exit, not only once. Two "verified" findings changed when primary sources became reachable: Create's jar contains its All Rights Reserved assets, and KubeJS added a dependency. | Technical Lead |
| 2026-11-26 | When editing with a script, key a replacement on text that occurs exactly once (a regex on a risk ID rewrote the wrong table row). Check the current file, not the last report, before handing off a "still stale" item. | Producer |
| 2026-11-26 | A warning line can leak a forecast answer just as a screen can. Check every warning text against the forecast it precedes (the beat 4 warning avoids saying "solar"). | Lead Game Designer |
| 2026-11-26 | Re-check forecasts when a tuning number changes (caps, scales), not just a threshold. The new storm cap quietly weakened forecast FI-4. Tuning multipliers belong in the threshold-vs-curve check. | Lead Instructional Designer |
| 2026-11-26 | Draw a straw-man wireframe at 1280×720 before speccing a screen (it caught a layout overflow the numbers hid), and check every color against each background it actually sits on. | Lead Artist |
| 2026-11-26 | A lock rule that fires on an action we want (like opening the Monitor) can turn that behavior into lost evidence. Check each lock against the goal of the action that triggers it. | Lead Instructional Designer |
| 2026-11-26 | Review another role's draft against your requirements instead of writing a competing spec, and send requirements before the text is drafted. | Lead Instructional Designer |
| 2026-11-26 | Forecasts need an explicit lock point before any hint, warning, or worked example that reveals the answer. | Lead Game Designer |
| 2026-11-26 | When a new requirement seems to break a privacy rule, look for a narrow interface at the boundary (e.g. the `NameScrubber`) before relaxing the rule. | Technical Lead |
| 2026-11-26 | A new process rule can silently conflict with a decided rule the same day ("no modal progress pop-ups" vs. a blocking, deferrable Keeper's Report). When a process document lands, check it against TEAM.md 8.6 and the other decided rules. | Producer |
| 2026-11-26 | Check each physics threshold against the balance curve. A threshold the curve passes in the first sol hides that regime and any forecast built on it (the ice rule broke forecast F4a). After every physics or balance change, run the forecast check with "threshold vs. curve". | Subject Matter Expert |
| 2026-11-26 | Running numeric checks on palettes (perceptual difference and colorblind simulation) caught two invisible "visible changes". Check visuals numerically, not by eye. | Lead Artist |
| 2026-11-26 | Owner attached a handoff from an earlier game project (docs/team/reference/GAME_DEV_HANDOFF.md). Adopted as process: PROCESS.md (iteration gates, definition of done, regression gate, UI and workflow rules, retro log) and BACKLOG.md (owner's words, acceptance, current iteration). Assumption: the owner wants the lessons applied; to confirm. | Coordinator, from the user's attachment |
| 2026-11-26 | Chapter 9 (development and testing): straw-man assets in early prototypes; one experience designed at a time; back end built in stages with a plumbing prototype; environment integration early; playtesting (alpha with SME and internal testers, beta with target group; wary when the tester isn't representative); formative feedback needs root-cause analysis from several perspectives; performance tracking, gaming-the-system, and validation need an assessment expert. Routed to the Technical Lead, Game Designer, Instructional Designer, Producer, Artist, and SME. | User research |
| 2026-11-26 | Chapter 9 (ACEDT, more): testing iteration (prototype, alpha, beta, candidate release) and staged deployment (TEAM.md section 4); analysis tasks (cognitive task analysis, audience survey, requirements questions); core design as one joint instructional-and-gaming task with four alignment approaches; embedded assessment with four purposes and behavioral indicators. Routed to the Producer, Instructional Designer, Game Designer, and Technical Lead. | User research |
| 2026-11-26 | Chapter 9 (ACEDT): three levels of iteration (product, task, customer/SME). Added to TEAM.md section 4; sent to the Producer for the roadmap cadence. No external SME exists (known gap). Chapter 6 also adds Koster's fun-as-pattern-mastery, the paradox of choice (recommended builds), and Schell's art-and-science check; sent to the Lead Game Designer. | User research |
| 2026-11-26 | Chapter 6 (motivation): intrinsic over extrinsic; rewards used as control backfire; baseline rewards are expected as fair acknowledgment, extra tangible rewards harm motivation; Zeigarnik effect backs the next-gate display; reward player skill, not avatar skill; difficulty adaptation must preserve the player's sense of control; Bartle types and the five experience dimensions guide content coverage. Sent to Lead Game Designer and Lead Instructional Designer. | User research |
| 2026-11-26 | Chapter 6: flow needs clear tasks, immediate feedback, focus, and balanced goals. Games use three task types (explicit, implicit, player-driven); Minecraft's strength is player-driven, emergent tasks. Sent to the Lead Game Designer for DESIGN.md. | User research |
| 2026-11-26 | Chapter 5: authenticity means the right **cognitive** fidelity for the learner's expertise, without extra load from excessive physical or functional fidelity. Added cognitive fidelity to the glossary and testing rules to DESIGN.md. | User research |
| 2026-11-26 | Applied Chapter 3 feedback research and Chapter 4 story research (DESIGN.md: Story Approach). | User research |
| 2026-11-26 | Gaming and instructional strategy are one joint design task. Every design decision is asked from both views: how is this instruction part of the game; what does this part teach; how does the technique affect gameplay; does it support or distract from the instruction. | User research (Chapter 9) |
| 2026-11-26 | The coordinator sent a message to the wrong agent because it mixed up agent order after parallel launches. Check the role of the recipient against the agent list before sending, and keep the role-to-agent mapping in the task summary. | Coordinator |
| 2026-11-26 | A physics change (ice-to-water rule) silently broke an assessment item (forecast F4). Any gate or physics change triggers a forecast check, not just an alignment-table check. | Lead Instructional Designer |
| 2026-11-26 | New root documents can appear mid-task. Check README for new documents at the start of every task. | Technical Lead |
| 2026-11-26 | A story beat quietly contradicted the physics after gate conditions changed. When gate conditions change, check story beats against them. | Lead Game Designer |
| 2026-11-26 | Solo adult playtests produce design judgment, not learning evidence. | Lead Game Designer |
| 2026-11-26 | Applied Chapter 3 serious-game research (cognitive load, multimedia, level design, narrative). Gaps: controls-first onboarding, simple in-house machines for learning-critical work, pressure-driven sound, fading and worked examples, next-gate goal display, Shift Briefings (advance organizers). | User research |
| 2026-11-26 | Checked against the chapter's recommended team communications (8.8). Added: change impact check across all disciplines, interruption rules (draft), team debriefs, "interactivity" definition; objective priorities requested. | User research |
| 2026-11-26 | Process: fuzzy (nonlinear) ADDIE with Fanning's software additions and RETAIN as a lens, plus a schedule for when key debates happen, which the models leave out. | User research |
| 2026-11-26 | Each discipline needs different documented information, and LGD must show the link from design decisions to learners' performance requirements. DOCUMENTATION.md adds a document map, traceability chain, decision rationale rule, and a shared feedback format. | User research |
| 2026-11-26 | Fidelity, immersive, adaptive, and engagement have no consistent definitions across disciplines (Coleman et al., 2010). Team definitions with required qualifiers are now in TEAM.md section 3. | User research |
| 2026-11-26 | Each discipline defines success differently. The team now uses a combined Success Scorecard and a perspective round at reviews (TEAM.md 8.5). | User research |
| 2026-11-26 | Disciplines agree on the same LGD issues at different strengths (Coleman et al., 2010). The team now holds an explicit position on each (TEAM.md 8.4). | User research |
| 2026-11-26 | Instructional and gaming roles expect different amounts of design change. Stability levels and a change process now make that explicit. | User research on LGD teams | User decision |
