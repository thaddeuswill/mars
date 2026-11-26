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
- **Where decisions live:** gameplay in `DESIGN.md`, learning in `LEARNING.md`, technical in `ARCHITECTURE.md`, schedule in `ROADMAP.md`. This file keeps a short log pointing to them.
- **Evidence beats argument:** playtests, prototypes, and sources settle disagreements where possible.

### Still open (needs the user)
- Licenses for our own code, assets, and educator materials.

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
| 2026-11-26 | Systems in Java, content in data, glue in KubeJS; server authoritative | ARCHITECTURE.md | Coordinator proposal; not yet reviewed by Technical Lead |
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

### 8.6 Rules for interrupting play with feedback (draft, needs consensus)

Feedback must balance keeping players motivated with what's best for learning. Interrupting at the wrong moment (e.g. a critical failure message near the end of a level or session) is frustrating. Draft criteria:

1. **Never interrupt** during danger, combat, or active building. Messages wait in the colony log.
2. **Natural pauses only** for non-urgent feedback: entering the dome, sleeping, opening the Monitor or journal, reaching a gate.
3. **The only blocking interruptions** are (a) life-threatening first-time warnings (first O2 critical, first breach) and (b) phase debriefs at a gate, and the player can defer a debrief.
4. **At most one** non-urgent pop-up message per ~10 minutes of play; the rest are queued.
5. **No new critical failure in the last ~5 minutes** of a classroom session (teacher end-of-session signal).
6. **Every prompt is deferrable** except the life-threatening ones in rule 3.

Owner: Lead Instructional Designer with the Lead Game Designer. To be confirmed through the consensus process and checked in the Stage 3 playtest.

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
| Game goals | Agree on criteria for prioritizing learning objectives | 🟡 Requested from the Lead Instructional Designer | LEARNING.md |
| General design | Discuss authenticity choices and their effect on thinking skills; document decision criteria | ✅ | DESIGN.md Fantasy and Authenticity |
| General design | Decide the role of every instructional and gaming element in the goals | ✅ | Traceability (DOCUMENTATION.md 3); fun–learning matrix |
| General design | Decide the strategy for making it fun for the target players | ✅ | DESIGN.md Fun |
| Feedback | Discuss how feedback supports objectives and motivates; evaluate against those criteria | ✅ Shared format has both purposes | DOCUMENTATION.md 5.1 |
| Feedback | Agree on criteria for interrupting the game for feedback | 🟡 Drafted | 8.6 |
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
| 2026-11-26 | Lead Artist | First art direction, keyframes, asset list | `docs/art/` | `docs/team/reports/2026-11-26-lead-artist-art-direction.md` |
| 2026-11-26 | Lead Instructional Designer | Apply decisions, per-student measures, RETAIN, priorities, prompts, rubrics | `LEARNING.md`, `docs/learning/` | `docs/team/reports/2026-11-26-lead-instructional-designer-decisions-applied.md` |
| 2026-11-26 | Technical Lead | Architecture gaps, licenses, school deployment | `ARCHITECTURE.md`, `docs/tech/` | `docs/team/reports/2026-11-26-technical-lead-architecture-gaps.md` |
| 2026-11-26 | Lead Game Designer | Apply decisions, C-01 fix, balance sheet, playtest plan | `DESIGN.md`, `docs/design/` | `docs/team/reports/2026-11-26-lead-game-designer-decisions-applied.md` |
| 2026-11-26 | Subject Matter Expert | Science accuracy and NGSS verification | `docs/science/` | `docs/team/reports/2026-11-26-subject-matter-expert-science-ngss.md` |
| 2026-11-26 | Producer | Consistency review, risk register | `docs/production/` | `docs/team/reports/2026-11-26-producer-consistency-review.md` |
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
| 2026-11-26 | Running numeric checks on palettes (perceptual difference and colorblind simulation) caught two invisible "visible changes". Check visuals numerically, not by eye. | Lead Artist |
| 2026-11-26 | Chapter 5: authenticity means the right **cognitive** fidelity for the learner's expertise, without extra load from excessive physical or functional fidelity. Added cognitive fidelity to the glossary and testing rules to DESIGN.md. | User research |
| 2026-11-26 | Applied Chapter 3 feedback research and Chapter 4 story research (DESIGN.md: Story Approach). | User research |
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
