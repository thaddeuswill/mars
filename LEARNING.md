# Learning Design

**Owner:** Lead Instructional Designer · **Last updated:** 2026-11-26, round 2 (fading plan and process/outcome map; worked example; interleaving; Shift Briefings; Air window check; reflections decision; delayed post-test; priority tiers and interruption rules decided; Chapter 6 motivation lens; Chapter 9 evaluation loop, requirements, behavioral indicators, four purposes, performance tracking, validation; SME round 2 and Technical Lead round 2 applied; educator guide draft)

This file follows an Instructional Systems Design (ISD) model, ADDIE. It starts with an analysis of the learners and context (section 0), which drives the strategy choices. It then answers four questions, followed by classroom mode (section 5) and NGSS alignment (section 6):

1. **Learning goals:** what learners need to know or be able to do.
2. **Methods and media:** how they get there.
3. **Assessment:** how we know they got there, for each student and for each group planet.
4. **Player feedback:** how players know they got there while they play, and how they track their own progress.

Detailed content specs live in `docs/learning/`:
- [docs/learning/prompts-and-reflections.md](docs/learning/prompts-and-reflections.md): forecasts (prediction prompts), Keeper's Report reflections, Earth links (D2), Keeper's Record entries, reading level and grade-band rules.
- [docs/learning/rubrics.md](docs/learning/rubrics.md): scoring rubrics for D1, D2, B5, and the transfer task.
- [docs/learning/journal-dust.md](docs/learning/journal-dust.md): panel dust journal notes, honesty notes, Earth link, and the power-drop "What caused it?" item (shared format, claim tags).
- [docs/learning/educator-guide.md](docs/learning/educator-guide.md): teacher-facing draft: setup, 23-period unit plan, debrief questions with NGSS codes, paper reflection fallback, reading the CSV exports, delayed post-test.

### Decisions

| Decision | Answer | Source |
|---|---|---|
| Learners | **Ages 12–18** (middle and high school), two grade bands | User decision |
| Classroom use | **Classroom mode**: a pacing preset for class periods (section 5) | User decision |
| Classroom group model | **Groups of 3–5 students share one planet; a class runs several planets** | User decision, 2026-11-26 |
| Classroom unit | **~17 play periods** (~11–12 h of play) inside a ~23-period unit (section 5.2) | User decision, 2026-11-26 |
| Curriculum standard | **NGSS**, middle and high school bands (section 6) | User decision |
| Data | **Local action log: pseudonymous on the server, anonymous in every export.** Teacher CSV export uses **pseudonymous IDs only**; there is no username option (section 5.4) | User decision, 2026-11-26 |
| Learning layer in the vertical slice | A **minimal Phase 1 learning layer** is built in roadmap Stage 3 (section 3.5) | User decision, 2026-11-26 |
| LMS integration | **After release, opt-in only** (section 5.6) | User decision, 2026-11-26 |
| Round 3 learning decisions | **Shift Board C2 credit goes to the player who did the repair** (3.0); **`guidance` setting adopted with four conditions** (4.10); **optional delayed post-test in Stage 6** (3.6); **three external reviewers sought before Stage 3**, including science teachers for the transfer key and forecast distractors (3.11; owner action); **the owner's Stage 5 run is a full classroom-preset run plus spot checks** (evaluation loop) | User decisions, 2026-11-26 (round 3, BACKLOG #16), on role recommendations |
| Reflections export | **Separate `reflections.csv`: on by default in classroom mode, off in standard mode; names scrubbed to IDs; "don't write your name" note; paper fallback** (section 5.5) | User decision, 2026-11-26, on Technical Lead and Instructional Designer recommendation |

**Rationale for the learning-side decisions in this file** (DOCUMENTATION.md section 4; goal priorities have their own block in section 1):

| Decision | Rationale | Alternatives considered | Trade-offs (learning / appeal) | Revisit if |
|---|---|---|---|---|
| Individual vs. group evidence (3.0); only individual summative measures are graded | On a shared planet one student can reach every gate for the group (consistency review M-14) | Grade group outcomes (unfair, invalid); split groups into solo worlds (breaks the group model) | Learning: valid per-student measures. Appeal: group play kept | Playtests show per-student in-game evidence is too sparse to be useful |
| 23-period unit, 17 play periods, 4/7/6 split (5.2) | User decision on ~17 play periods at ~11–12 h; split follows the Lead Game Designer's classroom time model (balance sheet §5.3: ~3.6 / 7.7 / 5.6 periods; Phase 1 check ~2.7 h) | 20 periods with ~9–10 h play (would change a settled number) | Learning: three full debriefs and a transfer task kept. Appeal: enough play to reach the beacon | A pilot class can't fit 23 periods (risk R-19) |
| Pacing by rates and hold times, never thresholds (5.1) | Scaled thresholds contradict the journal and can make lethal air "breathable" (SME finding 3). Now also in DESIGN.md (rate multiplier 4.0; holds 1/2/3 sols) | Threshold multiplier ~0.3 (old text); scale distance from start value | Learning: numbers match the science. Appeal: none lost | Rate scaling can't reach ~11–12 h without breaking balance |
| Forecasts hidden from other group members until resolved (3.2) | Keeps B3 evidence individual | Shared group forecast (more discussion, no individual evidence) | Learning: valid B3. Appeal: slightly less social; teachers can run a group "discuss, then re-forecast" activity in debriefs | Playtests show students want group forecasts |

The game models real Mars conditions: the starting values in the Planetary Index (about 0.6 kPa pressure, about −60 °C, about 95% CO2, about 0.1% O2) match the real planet (SME, `docs/science/accuracy-review.md` Part A). The learning design builds on that: **the mechanics are the lesson**, and the game doesn't stop to lecture.

### Player language: design terms vs. in-game names

This document is for the team and for teachers, so it says "learner". **In the game, the person is always a player** (TEAM.md 8.4). Game text never says "learner", "lesson", "objective", "quiz", or "test".

| Design term (this document) | In-game name | Notes |
|---|---|---|
| Learner / student | Player, Keeper | "Keeper" is the approved story name (TEAM.md section 3) |
| Prediction prompt | **Forecast** ("Log your forecast") | BELL asks; see `docs/learning/prompts-and-reflections.md` |
| Colony Record | **Keeper's Record**, header "What the colonists will need to know" | "Colony Record" stays the team and system term |
| Phase debrief | **Keeper's Report** (sent to Earth) | Contains one reflection question |
| Reflection prompt | The question in a Keeper's Report | Never called a "reflection prompt" in game |
| "Explain it" check | Proposed: **"What caused it?"** page in the journal | Final name is the Lead Game Designer's call |
| Learning goal | A page in the Keeper's Record | Goal IDs appear only in teacher views |

---

## Design Framework: ISD (ADDIE)

This project follows an **Instructional Systems Design (ISD)** model, using ADDIE as its structure. ISD guides the design rather than prescribing it: it keeps learning goals, learner characteristics, methods, and assessment connected, while leaving room for game-design judgment and creativity.

ADDIE runs alongside the game development stages in [ROADMAP.md](ROADMAP.md), so learning design isn't bolted on at the end.

| ADDIE phase | Question it answers | Where it lives | Roadmap stage |
|---|---|---|---|
| **Analysis** | Who are the learners, what must they learn, in what context? | Section 0 | Stages 0–1 |
| **Design** | What are the objectives, methods, and assessments, and how do they line up? | Sections 1–4, `docs/learning/` | Stage 1 |
| **Development** | Build the content, feedback tools, and assessments | Stage 3: minimal Phase 1 learning layer (section 3.5). Stage 4: the full layer (journal, forecasts, Keeper's Record, debriefs, pre/post test) | Stages 3–4 |
| **Implementation** | Deliver to learners in their environments | Classroom, club, and home play; educator guide. LMS integration after release, opt-in | Stages 5–7 |
| **Evaluation** | Did it work, and what should change? | Formative: playtests at each stage. Summative: pre/post study | Every stage; study in Stage 6 |

Evaluation is **not** only at the end. Each roadmap stage has a playtest, and its findings feed back into analysis and design. That's why the criteria in section 0.6 are labeled drafts.

**We use "fuzzy ADDIE", not strict ADDIE** (TEAM.md section 4, "Design process model"):
- **Nonlinear.** Following FIDGE (Akalli & Cagiltay, 2006), we can go back to an earlier phase whenever evidence calls for it. A playtest finding sends us back to design; a new learner insight sends us back to analysis (section 0). Roadmap stages are checkpoints, not one-way gates. Going back runs through the change process (TEAM.md 8.2), and any change to a goal ID gets an alignment check here.
- **Additions for software-based learning environments** (Fanning, 2008): software production, preparing learners to use the environment, operational lifetime, user-generated content, and environment-specific summative evaluation. How each is covered is in TEAM.md section 4. The learning side here: "preparing learners" is classroom period 1 plus Phase 1 onboarding (5.2); summative evaluation is the pre/post study and the Success Scorecard (3.4).
- **RETAIN as a review lens** (Gunter et al., 2008). See the RETAIN review below.

My summaries of FIDGE and Fanning come from TEAM.md; I have not re-read the original papers in this session (**unverified** beyond TEAM.md).

### Evaluation loop: who reviews each learning artifact, and when

Iteration with subject-matter and customer review at **every** phase, not only at the end (*Design and Development of Training Games*, Chapter 9, ACEDT; summarized by the coordinator, not re-read by me). Here the customer is the user. The SME checks the science; the user checks fit and plays the builds; I review against goals and criteria. No learning artifact moves from `draft` to `final` (DOCUMENTATION.md 5.1 status) without both reviews.

| Artifact | Subject Matter Expert reviews | User (customer) reviews | When |
|---|---|---|---|
| Objectives and criteria (0.6), NGSS map (6.1) | Science in each criterion; NGSS codes word for word | Signs off the goal set (stable, TEAM.md 8.1) | Done once in Stage 1; again only through the consensus process. NGSS word-for-word check: **done** (SME round 3) |
| Forecasts (F1–F10, FI-1–FI-4) and their explanations | Every question, distractor, and explanation before it's built; **external science teachers** review distractors and explanations before Stage 3 (teacher review pack) | Plays them in the Stage 3 slice (F1, F4a, F2) and in the Stage 5 run (**a full classroom-preset run plus spot checks**, user decision round 3); says which feel like a test | Before build; after each playtest that touches them |
| Journal entries, Earth links, Shift Briefings, worked example card | Every science line | Reads them in play (Lens A / Lens B walkthrough) | Before build; after each playtest |
| Keeper's Report questions and rubrics | Answer keys and sample answers; **external science teachers** review the transfer-task key and rubric anchors before Stage 3 | Reads them; rubric check by two raters on playtest answers | Stage 3 (Report 1), Stage 4 (rest), Stage 5 (rubric calibration) |
| Pre/post test, transfer task, delayed post-test | Answer key and every item | Takes the test as a stand-in learner (design exercise) | Stage 4 draft; Stage 5 revision; Stage 6 study |
| Educator guide and worksheets | Debrief science, Earth graph sources, NGSS codes | Reads as a teacher; checks the 23-period plan and setup steps | Each draft (first draft: round 2, now); Stage 4; Stage 6 before any pilot |
| Alignment table and this evaluation | — | Reviews gaps at each stage's team debrief (TEAM.md 8.7) | Every roadmap stage exit |

### Formative evaluation: playtest signals and root-cause analysis

**Signals to collect** in every playtest, alongside the playtest plan's observation sheet (Lead Game Designer owns the sheet; these are the learning-side columns):

| Signal | What it can tell us | Goals |
|---|---|---|
| **Questions the tester asks** (out loud, or would ask: the user notes them in persona) | Where the game fails to explain; misconceptions in the question itself | All; especially A2, A5 |
| **Interaction between testers** (in group tests: who talks, who holds the keyboard, who explains to whom) | Whether posts spread the work; whether one student carries the group (R-13) | C3, B4, all per-student evidence |
| **Whether they accept help** (open the cause breakdown, read Hana's notes, use "Need a start?", or ignore them) | Whether scaffolds are used and when they become redundant (expertise reversal) | B1, C2, C3 |
| Forecast answer times, expiries, and wrong-then-explained patterns | Guessing vs. reasoning; whether explanations land | B3, A2–A5 |
| Words used in reflections and think-aloud ("thin", "push", "loop") | Whether game vocabulary turned into the right concepts | A5, B2 |
| Moments of "noise" (confused, more than ~2 min stuck) and "boredom" (idle, skipping) | Koster's lens on load (4.9) | All |

**Root-cause approach.** A symptom (for example: "the tester missed F4a") is analyzed **from several perspectives before anyone prioritizes a fix**, using the change impact check roles (TEAM.md 8.2): learning (was the idea taught before it was tested?), game design (did the trigger fire at a natural moment?), science (is the physics right, does the trigger ever fire? The SME's round-2 review found F4a's trigger almost never fires), art (was the ice change visible?), technical (did the event log?). Each perspective names a candidate cause; the team picks the root cause with the evidence, then the fix. A symptom is never "fixed" by changing only the measure (e.g. dropping the forecast) without that analysis.

**Scope note.** The user has confirmed this project is a design exercise and no real students will use it. We still design every learning, assessment, and privacy element as if real students would.

### How this design meets ISD's strengths

| ISD strength | How we meet it |
|---|---|
| Calls attention to key factors and how they relate | Section 0 analyzes learners, content, and environments; the alignment table below connects them |
| Grounded in the science of learning | Simulation and discovery for cause-and-effect content; productive failure; immediate feedback; prediction (retrieval and self-explanation); spaced retrieval (A4 is forecast twice) and interleaving (2.3); a worked example that fades into practice (2.2); advance organizers (Shift Briefings, 2.1); process feedback that fades to outcome feedback as skill grows (4.8); spaced reflection at each gate |
| Clear statement of objectives | Section 0.6: behavior, condition, criterion for every goal, A1–D2 |
| Assessment linked to objectives from the start | Every objective has an individual measure in 0.6 and section 3, written before any content is built |
| Guides but doesn't prescribe | Game design pillars and ISD work together; creative choices are still made by the designers |
| Deliberate decisions and alternatives | Decisions are recorded with reasons; open questions are listed, not assumed |
| Common language | Goal IDs (A1–D2) are used across design, content, and assessment |
| Works in any environment | Section 0.4 covers classroom, club, home, and online use |

### Alignment check

Every goal must be **taught, practiced, given feedback, and measured** (TEAM.md 8.1). A gap in any column is a design gap. "Individual" measures separate students on a shared group planet; "group" evidence belongs to the whole planet and never counts as one student's evidence (section 3.0).

| Goal | Taught by (game content) | Practiced by | Feedback | Measured: individual | Group evidence (formative) | Status |
|---|---|---|---|---|---|---|
| A1 Mars vs. Earth | Phase 1 Shift Briefing (2.1); a Phase 1 source for the gas mix (~95 % CO2: Landing step 3, the Monitor's "Air mix" line, Dome stable journal; landed round 3), because the Monitor and the weather mast show gases only from Phase 3 (mast gas gauges at Phase 3: decided round 3, agreed); Phase 1 hazards (incl. real perchlorate dust); Monitor values and milestones (Armstrong line 6.3 kPa, Everest pressure ~33.7 kPa); gate journal "On Earth" lines | Reading the Monitor; forecast F3 | Monitor values; gate journal | Post-test **data task**: compare a Mars/Earth data table | — | Covered |
| A2 Liquid water | Ice-to-water rule (DESIGN.md §2, SME round-2 version): ice warmed near heaters in Phase 1 melts and boils away at once (boiling point a few °C); barely warmed ice gives a brief puddle; water stays from 5 kPa while above 0 °C and below the boiling point; Monitor shows water's boiling point. Adopted in DESIGN.md §2 (round 2) | Forecasts F4a (Phase 1) and F4b (Thaw); interleaved FI-2 (Phase 3, 2.3); "What caused it?" | Ice behavior changes as pressure rises; journal | Post-test scenario; F4a, F4b, FI-2 results | Hydrosphere gate reached | Covered. F4a's key holds while the invented puddle-drying time stays under 0.25 sol (now 0.05; prompts-and-reflections §1.3) |
| A3 Greenhouse effect | Greenhouse emitters (factory-made greenhouse gases); temperature curve | Forecast F5 | Temperature trend; journal | Post-test explanation | — | Covered. Depends on the emitter journal naming what it emits (SME; Lead Game Designer) |
| A4 Radiation | Radiation spikes and shielding; solar-event story beat 5; journal | Forecast F2 (Phase 1), F7 (Phase 2 at ≥ 5 kPa, spaced), interleaved FI-3 (Phase 3) | Exposure effect; failure report; forecast result | Post-test scenario; F2 and F7 results | — | **Gap closed on paper**, if story beat 5 and a spike warning ~1 sol ahead are built (Lead Game Designer, Technical Lead) |
| A5 CO2/O2 exchange | Phase 3 balancing; flora; CO2 from decay; carbon scrubbers; carbon flow view on the Monitor | Forecasts F8, F9; flora-bloom and decay drift events | Fires or stability; Monitor (O2 fraction **and** O2 partial pressure) | Post-test explanation; F8 result | Breathable condition (O2 partial pressure ≥ 16 kPa, pressure ≥ 75 kPa, CO2 ≤ 0.5 kPa) held for the active preset's hold time | Covered. **Concern:** narrow O2 window at 75 kPa may be hard to read at MS depth (4.7) |
| B1 Cause and effect | Planetary Index responses; Monitor cause breakdown; worked example on the first Outgasser (2.2); **panel dust**: two separate power cause lines, "Panel dust −n %" and "Storm: less sunlight −n %" (DESIGN.md "Solar panel dust") | "What caused it?" (incl. the power-drop item `explain.power_drop`, journal-dust.md §7); every forecast | Monitor cause breakdown | Post-test variable-link diagram | — | Covered |
| B2 Feedback loops | Fauna vs. machine signature (reinforcing); ice–albedo (reinforcing, adopted); polar frost sink and storms vs. pressure (balancing); Monitor names each as a cause | Forecast F6; Keeper's Report 2; polar-winter and fauna-grazing drift events | Keeper's Report; Earth reply | Report 2 reflection and transfer task: one loop of each type | — | Covered. Ice–albedo adopted (size pending SME), which makes HS-ESS2-2 a near-exact fit |
| B3 Prediction | Forecasts and Monitor trends | All forecasts (F1–F10) | Forecast result; per-phase tally | Own forecast accuracy, Phase 1 → Phase 3 | — | Covered |
| B4 Trade-offs | Equilibrium band; narrow breathable O2 window vs. fire risk; shrouds (−15 % output) and lures (water upkeep) vs. fauna; expansion vs. upkeep | Forecast F10; drift events during holds; Keeper's Report 3 | Monitor; fire failure report; Keeper's Report | Post-test trade-off scenario; Report 3 reflection | Equilibrium hold reached | **Gap closed** (objective added in 0.6) |
| B5 Unintended consequences | Fauna blowback in Phase 2 (fiction) **and real side effects**: ground collapse from melting ground ice, perchlorate in meltwater; Earth example in D2 content. **Minor:** the player's own digging and machine exhaust dust nearby panels faster (×2, invented; labeled "no one has measured this on Mars", journal-dust.md §4) | Forecast F6; Keeper's Report 2 | Fauna appear near emitters; failure reports name fauna | Report 2 reflection, B5 rubric | — | Covered |
| C1 Resource planning | Phase 1 O2, water, and power; **panel dust** makes power drift down between cleanings, and storms add dust when they end (journal-dust.md §1–3) | Forecast F1; preparing for the first storm, **including cleaning dusty panels in the storm warning window**; interleaved FI-1, FI-4 (Phases 2–3) | Dome survives or fails; the power line and "Panel dust" cause line | Post-test planning scenario; F1 result. Formative: `panel.cleaned` on dusty panels before a warned storm (3.8) | Dome survives the first dust storm (by the second storm) | Covered |
| C2 Design, test, improve | Breaches, wear, storms, ground collapse, emitter-wear drift events; **panel dust as upkeep, with a route choice from Phase 2** (brush = your time; wiper = power and parts) | Own repairs on the **Shift Board**; choosing and maintaining a cleaning route | Failure reports; Hana's tips fade; power line | Own repairs (credit goes to the player who did the repair, decided round 3): same cause doesn't recur; "What caused it?" on own failures. **Panel dust is not a failure type**: it recurs by design, so it never counts as a repeat failure | Repeat failures fall across phases | Covered. Actor IDs on repairs: done (ARCHITECTURE.md §2.8) |
| C3 Data use | Planetary Monitor (derived values: O2 and CO2 partial pressure, boiling point); worked example step "find your machine's cause line" (2.2); Shift Briefings name what to watch (2.1) | Checking the Monitor before builds; Data post (DESIGN.md colony posts) | Monitor trends | Own Monitor checks before own major builds, plus a data reason in a Keeper's Report or teacher check-in | — | Covered |
| D1 Ethics | Thawlings; the crew's secret; the Moreno–Basu argument; four fauna options (defend, shrouds, lures, quiet zones), none "right"; unranked ending cards | Keeper's Report 2; final reflection; class discussion | Earth reply; epilogue cards (no ranking) | Written reflection, D1 rubric | — | Covered. Depends on non-lethal fauna options (decided 2026-11-26) |
| D2 Link to Earth | Journal "On Earth" lines (incl. desert solar farms and dust, `earth.dust.solar_farms`, journal-dust.md §5); Mensah's replies; Phase 2 debrief with Earth CO2 and temperature graphs | Keeper's Report reflections; transfer task | Mensah's replies; class debriefs | Transfer task and reflections, D2 rubric | — | **Content specified** in `docs/learning/prompts-and-reflections.md`; text not yet written; Earth facts need SME check |

**Remaining gaps:**
- **Round 3 re-check** (prompts-and-reflections §1.3): every goal still taught, practiced, given feedback, and measured on paper. The Lead Game Designer's round-3 changes landed and pass: A1's Phase 1 gas-mix line (Landing step 3, Monitor "Air mix", Dome stable journal); forecast card first on Monitor open (B3 evidence kept); puddle-drying time 0.1 sol (F4a); drift events after the first flora; storm warnings ≥ 1 sol in every phase. Only FI-4 is conditional (Phase 3 storm cap).
- **Round 2 alignment re-check (after the SME review):** no goal lost its teaching, feedback, or measure. Changes: A2 is conditional on the boiling-point ice rule (Lead Game Designer); A4's F7 fires earlier (clearer); C2's HS codes changed (HS-ETS1-2, -4); D2's Record page no longer fills from opening pages (now writing only). New elements added to rows: worked example (B1, C3), Shift Briefings (A1, C3), interleaved forecasts (A2, A4, C1).
- A4 and D2 are closed in design only. Both need content written and built (Stage 4; A4's first forecast also belongs in the Stage 3 slice if the radiation spike is in it).
- Several high school NGSS mappings are "Moderate" until specific content exists (section 6.1 notes): the D2 Earth content, carbon reservoirs on the Monitor (HS-ESS2-6), and a claim-evaluation question in the Phase 3 debrief (HS-LS2-6, now in Keeper's Report 3).

### RETAIN review

RETAIN (Gunter, Kenny & Vick, 2008, *Educational Technology Research and Development*) rates how well a game embeds academic content in its fantasy and supports transfer. It draws on Gagné's nine events of instruction, Keller's ARCS model, and Bloom's taxonomy. The six element definitions below are my working summaries from memory; the paper's exact wording and its level-scoring scheme were **not re-checked** in this session (**unverified**). Ratings are of the **current design on paper**, not of played behavior (nothing has been built or playtested).

**Vocabulary warning.** RETAIN's "adaptation" means the *learner* changing behavior, and its "immersion" means the learner being intellectually invested. Our team terms differ: "adaptive" is about the *game* changing (TEAM.md section 3), and immersion has diegetic, narrative, and flow types. Below, "adaptation (RETAIN)" and "immersion (RETAIN)" mean the RETAIN senses.

| Element | Working meaning | Rating | Where it's met | Gaps and owner |
|---|---|---|---|---|
| **Relevance** | Content connects to the learner's world and needs, in manageable steps | **Partial** | Phase 1 survival makes A1, C1 immediately useful; story gives a reason to care (Keeper, colonists waiting); gates pace the content in steps; D2 "On Earth" lines and Mensah's replies point to the learner's own planet | D2 Earth content is specified but not written (Lead Game Designer writes, SME checks, ID reviews). Relevance for unmotivated classroom students is **assumed**, untested (playtest; Lead Game Designer) |
| **Embedding** | Academic content is tied into the game's fantasy and core mechanics (endogenous), not bolted on | **Strong** | The Planetary Index *is* the science (A1–A5); the win condition is equilibrium (B4); fauna stand for unintended consequences (B5, D1); forecasts and reports are colony work in BELL's and the Keeper's voices | Thawlings are fiction and could teach "Mars has life": fiction note in every fauna entry (ID, done in 1; Lead Game Designer to apply in the journal). Super-greenhouse emitters must name their gas or A3 embeds a contradiction (Lead Game Designer) |
| **Transfer** | Learners apply what they learned to new situations | **Partial** | Transfer task on Earth's carbon cycle (B1, B2, D2); post-test uses *new* scenarios (A2, A4, B4, C1); A4 is forecast in three different situations (F2, F7, FI-3); interleaved forecasts apply earlier ideas in new situations (2.3); debrief questions with Earth data (educator guide) | Transfer task and pre/post items not yet written (ID, Stage 4). The Phase 2 debrief's Earth data activity is still the main bridge; its graphs need a named source (SME) |
| **Adaptation (RETAIN)** | Learners change their behavior because of what they learned | **Partial** | C2 criterion: a fixed failure doesn't recur; B3: forecast accuracy rises; C3: Monitor checked before builds; Hana's tips fade once behavior changes | Behavior change is logged per student only if actor IDs are on every relevant event (Technical Lead). No check that changed behavior survives a new hazard type (consider a late "new failure" in Phase 3; Lead Game Designer) |
| **Immersion (RETAIN)** | Learners are intellectually invested and actively taking part | **Partial** (strong on paper, unproven) | No pop-up quizzes; all feedback diegetic (4.2); player language; real choices about the fauna (D1); forecasts ask players to commit before seeing the result | Long holds risk idle play and cognitive disengagement (M-16; Lead Game Designer). Text load for weak readers (ID rules in `docs/learning/prompts-and-reflections.md` section 5; playtest check). Group play can leave one student watching (colony posts rotate each phase, section 2; check in playtest) |
| **Naturalization** | Using the knowledge becomes habitual and spontaneous | **Partial (on paper)** | Repeated Monitor use across all three phases (C3); forecasts every phase (B3); interleaved forecasts in Phases 2–3 that don't say which idea applies (2.3); faded feedback so players act without prompts by Phase 3 (4.8); **optional delayed post-test ~4 weeks after the unit, with a cue-free "spontaneous use" item** (3.6) | ~11–12 h in class is still short for habits. Home play (~40 h) gives more practice but has no outside-the-game measure (accepted: home players aren't tested). The delayed test needs the Stage 6 study schedule (Producer) |

#### Gagné's nine events (insight check)

| Event | Where our design does it | Note |
|---|---|---|
| 1. Gain attention | Opening: storm clears, dome damaged, alone (story beat 1) | — |
| 2. Inform learners of objectives | Keeper's Record pages ("what the colonists will need to know") | Done diegetically, never as a list of objectives (player language, TEAM.md 8.4) |
| 3. Stimulate recall of prior learning | Pre-test; forecasts ask players to use what they already know; each Shift Briefing opens with one line recalling the last shift (2.1); debrief openers recall the previous debrief (educator guide) | — |
| 4. Present the content | World response first, then gate journal entry | — |
| 5. Provide learning guidance | Failure-report hints; Hana's tips; Monitor cause breakdown | — |
| 6. Elicit performance | Forecasts; repairs; holding the balance | — |
| 7. Provide feedback | Natural consequences, forecast results, failure reports (section 4) | — |
| 8. Assess performance | Pre/post, transfer task, rubrics (section 3) | Outside the game by design |
| 9. Enhance retention and transfer | Transfer task; D2 Earth links; A4 spaced forecast; interleaved forecasts (2.3); delayed post-test (3.6) | Still the weakest event: see Naturalization |

#### Keller's ARCS (insight check)

| ARCS | Where our design does it | Gap |
|---|---|---|
| Attention | Visible world change; storms; fauna; story mystery (Fridge B) | Phase 2 has the fewest story beats per hour (story bible 6.2) |
| Relevance | See RETAIN relevance | D2 content not written |
| Confidence | Fail forward (gates lock in); hints not answers; own forecast tally shows improvement | Phase 1 must not punish players new to tech mods (Lead Game Designer) |
| Satisfaction | Helmet-off moment; beacon; epilogue cards; Mensah praising the player's data | — |

---

## 0. Learner and Context Analysis

This analysis determines which strategies and assessments fit. Everything marked *(assumed)* needs confirming with a teacher or a playtest; if it changes, sections 1–4 change with it.

### 0.1 Targeted learners

| | Primary | Secondary |
|---|---|---|
| **Who** | Students aged 12–18 (middle and high school) | Adult and teen hobbyist players who pick up the pack for fun |
| **Setting** | Science class, STEM club, or after-school program; in class, groups of 3–5 per planet | Home, singleplayer or a small friends' server |
| **Motivation** | Mixed: some love Minecraft, some are there because the teacher said so *(assumed)* | High and self-chosen |

**Characteristics that shape the design**
- **Minecraft familiarity is high but uneven** *(assumed)*. Most know the basics; few have played tech modpacks. → Phase 1 has to teach modded machines from scratch, and survival must not punish newcomers before they learn the controls.
- **Reading levels vary widely** *(assumed)*. → Main text at roughly a grade 6–7 reading level, with a "read more" layer for depth. Key information is also shown visually. Rules in `docs/learning/prompts-and-reflections.md` section 5.
- **Attention in short sessions.** Classroom periods are 45–60 minutes *(assumed)*, with about 40 minutes of actual play *(assumed)*. → Each session should end at a natural save point with something achieved.
- **Two grade bands.** A 12-year-old and an 18-year-old need different depth. → A **grade band** setting (middle school / high school) picks the journal depth, forecasts, reflection questions, and post-test items, mapped to the matching NGSS band (section 6).
- **Mixed-ability groups on a shared planet.** In class, 3–5 students share one planet. A strong player can carry the group's gates. → Progress belongs to the group, but **evidence of learning is per student**: own forecasts, own "What caused it?" checks, own repairs, own reflections (section 3.0). Suggested colony posts, rotating each phase, spread the work (DESIGN.md Group Play; section 2).
- **Accessibility.** Color is never the only signal (sky tint plus Monitor numbers); subtitles for sound cues; no time-pressured reading.
- **Game experience in general** *(assumed; to be surveyed)*. Most 12–18-year-olds play some games, but not all, and not all play Minecraft survival (vs. creative or minigames). Genres they like vary: sandbox, survival, strategy, story, competitive shooters. → The pack can't assume a love of tech modpacks or of long survival play; the first ~30 minutes must be readable to someone who mostly plays other genres.
- **Themes that interest them** *(assumed; to be surveyed)*. Space and Mars missions, survival, building, mysteries, animals. Climate topics may interest some and tire others ("climate fatigue"; **unverified**). → The thawling mystery and survival carry early appeal; Earth links stay one line at a time.
- **Demographics** *(assumed; to be surveyed)*. Mixed gender, mixed home access to gaming PCs, some English learners. → No gendered player references (DESIGN.md); short plain text; play must be possible entirely at school.
- **Direct survey, not assumption.** The questions we'd ask a few target learners are in [docs/learning/learner-survey.md](docs/learning/learner-survey.md). In this design exercise they're marked for the user's playtests and for any future teacher-reviewer.
- **Cognitive task analysis.** The core knowledge and skills of the planetary-engineer task, split into procedural and cognitive parts, with typical mistakes per expertise level: [docs/learning/cta.md](docs/learning/cta.md). Real-world scenarios are requested from the SME.

### 0.2 Prior knowledge and experience

**Likely to know already** *(assumed)*
- States of matter (solid, liquid, gas), basic temperature
- Plants need CO2 and light and produce O2 (photosynthesis, basic level)
- Mars is red, cold, and far away

**Likely gaps** *(assumed)*
- Atmospheric pressure as a concept, and how it affects boiling and freezing
- Why the greenhouse effect works (what gases do with heat)
- Radiation and magnetic fields
- Feedback loops and equilibrium as ideas

**Common misconceptions to design against**

Science wording checked by the SME (`docs/science/accuracy-review.md` Part D).

| Misconception | Evidence it's common | How the game confronts it |
|---|---|---|
| "Mars has no atmosphere at all" | Plausible; prevalence **unverified** | Monitor starts at about 0.6 kPa, not zero (less than 1% of Earth's air pressure) |
| "Water always melts at 0 °C and boils at 100 °C" | Prevalence **unverified** | In Phase 1, water's boiling point is only a few degrees above freezing (the Monitor shows it), so ice warmed by a heater melts and boils away at once; barely warmed ice makes a brief puddle. Water pools once pressure is high enough to keep the boiling point well above everyday temperatures (SME round-2 ice rule; game line 5 kPa; forecasts F4a and F4b) |
| "The greenhouse effect is the ozone hole" | **Documented** (Boyes & Stanisstreet, cited in accuracy-review.md [28]) | Journal entry contrasts them when emitters unlock |
| "CO2 is the only greenhouse gas" | **Documented**: students often don't count CO2 or water vapor as greenhouse gases, and confuse which gases count (same research, [28]) | The emitters make factory-made greenhouse gases that trap heat far better than CO2; the emitter journal entry names them (no potency number until verified) |
| "Air with 21% oxygen is always breathable" | Risk the old design could have *created* (SME finding 1) | Breathable depends on O2 partial pressure. The Monitor shows both the O2 percentage and the O2 partial pressure; the journal says "your lungs care how much oxygen pushes into them, not just the percent" |
| "More oxygen is always better" | Plausible; prevalence **unverified** | Oxygen overshoot causes fires in Phase 3 |
| "Fixing one problem fixes the system" | A systems-thinking target, not a science fact | Real side effects carry the lesson: ground collapse from melting ground ice and perchlorate in meltwater. Warming also wakes the fauna; **the fauna are fiction**, so the journal says no life has been found on Mars and pairs it with a real Earth side effect (D2 content) |
| "Plants only respire at night" / "plants don't respire" (candidate) | **Documented** in diagnostic research on photosynthesis and respiration (Haslam & Treagust, 1987, per SME round2-review §7; not opened) | F8 uses it as a distractor; F8's explanation says plants give off some CO2 day and night and take in more than they give off while growing in light; decay pulses (CO2 from biomass) show respiration on the Monitor |
| "Mars has life underground" (risk the game could *create*) | Risk flagged by SME and story bible | Every fauna journal entry carries the fiction note (section 1, Science accuracy notes) |

**Pre-test** (section 3.3) confirms prior knowledge and catches these misconceptions before play.

### 0.3 Nature of the content

Different kinds of content need different teaching and testing approaches.

| Content type | Examples in this game | Teaching strategy | Assessment approach |
|---|---|---|---|
| **Facts** | Mars surface pressure is ~0.6 kPa; atmosphere is ~95% CO2 | Show in the Monitor and journal; repeat through play | Data interpretation, not recall (MS-ESS1-3 excludes recall) |
| **Concepts** | Pressure, greenhouse gas, equilibrium, feedback loop | Examples and non-examples in play; journal definitions at the moment they matter | Classify or identify examples |
| **Cause and effect / principles** | More pressure → liquid water possible; more warming → fauna wakes | Simulation: act, observe, explain. Forecasts | Predict outcomes of new scenarios |
| **Procedures** | Seal a dome, set up an O2 loop, maintain a machine | Guided first attempt, then practice with less support | Observed in play: completed without failure |
| **Problem-solving** | Stabilize O2/CO2 while keeping temperature up | Open-ended challenges, productive failure, hints not answers | Group: reach and hold equilibrium. Individual: trade-off scenario and reflection |
| **Attitudes** | Weighing terraforming ethics; linking to Earth's climate | Reflection questions, discussion | Rubric-scored written reflection |

**Task analysis:** [docs/learning/cta.md](docs/learning/cta.md) (cognitive task analysis). **Media analysis:** [docs/learning/media-analysis.md](docs/learning/media-analysis.md).

**Most of the content is cause and effect and problem-solving.** That's why simulation and discovery are the main strategies, and why assessment leans on prediction and performance rather than recall.

### 0.4 Learning environments

| Environment | How it's used | Constraints to design for |
|---|---|---|
| **Classroom / computer lab** | Teacher runs several **group planets** (one per group of 3–5); students play in sessions, with discussion between | 45–60 min periods *(assumed)*; school hardware may be weak *(assumed)*; school networks may block game servers; teacher must pause, reset, and monitor several planets at once. **Requires Minecraft Java Edition; it can't run in Minecraft Education** (Minecraft Education is Bedrock-based and doesn't support community mods; `docs/tech/school-deployment.md`). Each student needs a Java Edition account; Education licenses don't carry over. School IT must approve the launcher and Java. Realistic classroom audiences: schools with Java Edition, clubs, after-school programs |
| **STEM club / after-school** | Longer sessions, small groups on a shared planet | Fewer constraints; less teacher time for debriefs |
| **Home, singleplayer** | Self-directed, at the player's own pace | No teacher, so feedback and the Keeper's Record must stand alone |
| **Online / remote class** | Students join hosted group planets from home | Hosting cost; supervision |

**Implications**
- 40 hours is long for a class, so the pack has a **classroom mode** (section 5).
- Teacher tools: one class overview across all group planets, pause, and rollback (section 5.3).
- Performance budget must target modest school hardware running several planets (Technical Lead; roadmap Stage 4).
- The educator guide must open with: **"Requires Minecraft Java Edition; it can't run in Minecraft Education."** It also needs a one-page IT requirements sheet (accounts, launcher, Java, ports, hardware), with facts from `docs/tech/school-deployment.md`.

### 0.5 Where learners will use it afterward (transfer)

| Setting | What they'll do with it |
|---|---|
| Later science classes | Explain atmospheric pressure, the greenhouse effect, phase changes, and ecosystems |
| Exams and coursework | Answer questions on Earth and space science and climate |
| Discussions about Earth's climate | Reason about feedback loops and unintended consequences in real systems |
| Everyday decisions and civic life | Think in systems: "what else does this change affect?" |
| Future STEM study or careers | Engineering design habits: plan, test, fail, improve |

Transfer to Earth is the main target. The transfer task in section 3.3 checks it directly.

### 0.6 Behaviors, conditions, and criteria

Each objective states the **behavior** (what the learner does), the **condition** (in what situation, with what resources), and the **criterion** (how well). Every row has an **individual** measure, so it can separate students on a shared group planet (section 3.0). Where a group outcome is also tracked, it is listed separately and is formative only.

Terms used below:
- **Active preset's hold time:** the hold for that gate in the running preset (standard or classroom, section 5.1). Never a fixed number of sols.
- **Attempt (C1):** one dust storm event. "By the second storm" means the dome survives the first or the second storm without a life-support failure in the core module.
- **Own:** done by this player's pseudonymous ID in the action log (a repair placed, a forecast answered, a build placed).

| Goal | Behavior | Condition | Criterion (both bands unless noted) | Group evidence (formative) |
|---|---|---|---|---|
| A1 | Compare Mars and Earth on pressure, temperature, atmosphere makeup, and radiation, using data | Post-test **data task**: a Mars/Earth data table for the four variables, no other notes | States the difference and one consequence for life or water for at least 3 of 4 variables. HS: also expresses at least one difference as a ratio or scale (e.g. "less than 1% of Earth's pressure") | — |
| A2 | Explain why water can't stay liquid on Mars's surface and what must change | Written or verbal, given a new scenario (post-test); in-game forecasts F4a, F4b | Names both pressure and temperature. HS: also explains that low pressure lowers the boiling point, so warmed ice turns to vapor or boils away | Hydrosphere gate reached |
| A3 | Explain how greenhouse gases warm a planet | Written post-test | Correct mechanism: sunlight gets in, gases trap outgoing heat. HS: names infrared (heat) radiation and links it to the planet's energy balance | — |
| A4 | Explain why Mars's surface gets more radiation than Earth's, and choose a protection | Post-test scenario (a solar event is coming; where should a Keeper be?); in-game forecasts F2 and F7 | MS: names at least one cause (thin air or no global magnetic field) and one valid protection (shielding, depth, staying inside). HS: names both causes, and explains why thicker air lowers surface exposure (matter absorbs radiation) | — |
| A5 | Describe the CO2/O2 exchange and why balance matters | Post-test; in-game forecast F8 | Correct written explanation that names one consequence of imbalance (fire from too much O2, or toxic or unbreathable air from too much CO2). HS: also explains that breathing depends on O2 partial pressure, not percentage alone | Breathable condition held for the active preset's hold time |
| B1 | Draw links between planetary variables | Post-test: blank diagram of 5 variables | At least 4 correct links with the right direction | — |
| B2 | Identify a reinforcing and a balancing feedback loop | Keeper's Report 2 (in the game) or the transfer task (a new Earth example) | One correct example of each, with each step of the loop named | — |
| B3 | Predict the effect of a change before making it | In-game forecasts, answered by each player on their own screen | Own Phase 3 forecast accuracy at least 20 percentage points above own Phase 1 accuracy, with at least 3 forecasts answered in each phase | — |
| B4 | Manage a trade-off between competing goals and justify the choice | Post-test trade-off scenario (e.g. cut CO2 while keeping the planet warm); Keeper's Report 3 | Names both competing goals, picks an action, and states its cost. MS: compares two options against at least one criterion (MS-ETS1-2). HS: compares two options against at least two prioritized criteria, including one side effect on the planet (HS-ETS1-3) | Equilibrium: all values in band for the active preset's hold time |
| B5 | Identify an unintended consequence of an intervention | Keeper's Report 2 (after Phase 2) | Names one consequence and its cause (B5 rubric, `docs/learning/rubrics.md`) | — |
| C1 | Plan for limited resources and put life-critical needs first | Post-test planning scenario (a storm is coming, resources are limited); in-game forecast F1 | Ranks life-critical needs first and gives a reason for the order | Dome survives a dust storm without a core life-support failure, by the second storm |
| C2 | Diagnose and fix a failing system | After any failure the player repairs | For each failure type the player repaired, the same cause doesn't recur on that system in the same phase; "What caused it?" answer correct for that failure | Number of repeat failures falls from Phase 1 to Phase 3 |
| C3 | Use data to make a decision | Before a major build, with the Monitor | Opened the Monitor before at least half of own major builds, **and** can give a data reason for one build (Keeper's Report or teacher check-in) | — |
| D1 | Argue a position on terraforming that weighs what's at stake for life already there | Written answer to Keeper's Report 2 or the final reflection; class discussion is formative only | D1 rubric score of 3 out of 4 or higher | — |
| D2 | Connect a mechanism from the game to Earth's climate or ecosystems | Transfer task; Keeper's Report reflections | D2 rubric score of 3 out of 4 or higher | — |

D1 and D2 now have separate rows. They share prompts (the same reflection can show both), but they are different skills: D1 is argument about values, D2 is transfer of a science mechanism. Each has its own rubric.

Criteria are first drafts. They should be checked against the first playtest, then adjusted.

### 0.7 Requirements: where the game sits in the curriculum

| Question (Chapter 9, requirements analysis) | Answer | Where the educator guide covers it |
|---|---|---|
| What comes before the game? | Pre-test, a short Mars intro, groups and posts (period 1). Recommended prior unit: states of matter and basic photosynthesis (0.2 "likely to know") | §1, §4 (period 1) |
| What comes after? | Transfer task on Earth's climate system, post-test, optional delayed post-test; natural follow-on units: Earth's climate and human impacts, ecosystems, engineering design | §1, §4 (periods 22–23), §9 |
| What role does it play in the curriculum? | An **anchoring experience** for a unit on Earth and space systems, climate, and engineering design: the phenomena students explain in class come from their own planet. It replaces some lab and lecture time; it doesn't replace instruction on Earth's climate data, which happens in debriefs | §1 |
| Is LMS integration needed? | **Not at launch** (decided: after release, opt-in, 5.6). Teachers use the CSV exports and paper rubrics | §1, §8 |

---

## 1. Learning Goals

Each goal is written as something a learner can **do**, so it can be measured. Goal IDs A1–D2 are **stable** (TEAM.md 8.1).

**When we revisit objectives.** Stable doesn't mean frozen (Chapter 9: objectives get revised as needs change). We reopen a goal or its criterion, through the consensus process, when:
- **The customer's needs change** (a user decision on scope, audience, or classroom use).
- **An assumption in section 0 fails** (survey or playtest shows different prior knowledge, reading level, or game experience).
- **The game teaches a goal poorly**: after a playtest or the Stage 6 study, a goal misses its criterion and no affordable design change fixes it. Then we either redesign the teaching, move the goal to class time (debriefs), or narrow the objective, and say so in the educator guide.
- **The SME finds a science or NGSS problem** with the objective's wording.
- **A goal is learned for almost no cost** (it may be demoted in priority, not removed).
Criteria (the "how well" in 0.6) are **flexible** and change at stage boundaries with evidence; goal IDs and behaviors are **stable**.

### A. Planetary science (knowledge)
| # | By the end, learners can… | Where the game teaches it |
|---|---|---|
| A1 | Describe how Mars differs from Earth in pressure, temperature, atmosphere makeup, and radiation | Phase 1 hazards, Planetary Monitor |
| A2 | Explain why liquid water can't stay on the surface at low pressure and temperature, and what has to change for it to | Thaw, Hydrosphere, and Phase 3 gates |
| A3 | Explain the greenhouse effect and how adding gases raises surface temperature | Greenhouse emitters, temperature curve |
| A4 | Explain why a thin atmosphere and no global magnetic field expose the surface to radiation | Radiation spikes, shielding, solar-event forecasts |
| A5 | Describe how plants and algae exchange CO2 for O2, and why the balance matters | Phase 3 O2/CO2 balancing |

### B. Systems thinking (skills)
| # | By the end, learners can… | Where the game teaches it |
|---|---|---|
| B1 | Identify cause-and-effect links between planetary variables (e.g. temperature → water → fauna) | The Planetary Index and its world responses |
| B2 | Recognize feedback loops, both reinforcing and balancing | Storms weaken as pressure rises; fauna rises with infrastructure |
| B3 | Predict the effect of a change before making it, and check the prediction | Forecasts and Planetary Monitor trends |
| B4 | Manage trade-offs between competing goals (warming vs. CO2 removal, expansion vs. upkeep, more O2 vs. fire risk) | Late-game equilibrium band |
| B5 | Recognize unintended consequences of interventions | Ecosystem blowback in Phase 2 |

### C. Engineering and resource management (skills)
| # | By the end, learners can… | Where the game teaches it |
|---|---|---|
| C1 | Plan for limited resources and prioritize life-critical needs | Phase 1 O2/water/power loop |
| C2 | Design, test, and improve a system that fails under stress | Dome sealing, maintenance, storm wear |
| C3 | Read data (numbers and graphs) to make decisions | Planetary Monitor |

### D. Reflection and ethics (attitudes)
| # | By the end, learners can… |
|---|---|
| D1 | Discuss whether terraforming another planet is a good idea, and what's at stake for life that might already be there (the dormant fauna, which are fiction) |
| D2 | Connect what they learned to Earth's own climate and ecosystems |

### Goal priorities

When scope, schedule, or a design change forces a choice, goals get resources in this order. Priorities decide **who gets resources first**, not whether a goal exists: every goal stays in the game and on the post-test. Goal IDs stay stable (TEAM.md 8.1).

**Criteria** (one point for each "yes"; ties broken by criterion 4, transfer to Earth, because transfer is the main target, 0.5):

| # | Criterion | "Yes" means |
|---|---|---|
| 1 | **Core loop** | The goal is practiced through the core loop (build → Index responds → read Monitor → adjust) in at least two gameplay phases, not only through story or journal |
| 2 | **Win condition** | Reaching equilibrium (the beacon) depends on understanding it |
| 3 | **NGSS strength** | At least one PE mapped **Strong** by the SME (`docs/science/ngss-verification.md`) |
| 4 | **Transfer to Earth** | The goal directly supports reasoning about Earth's climate or ecosystems |

| Goal | 1 Core loop | 2 Win | 3 NGSS Strong | 4 Earth | Score | Tier |
|---|---|---|---|---|---|---|
| A2 Liquid water | Yes (Phase 2–3 gates) | Yes (hydrosphere band) | Yes (MS-PS1-4) | Partial | 3+ | **Primary** |
| A3 Greenhouse effect | Yes (emitters, Phase 2–3) | Yes (temperature band) | Yes (HS-ESS2-4) | Yes | 4 | **Primary** |
| A5 CO2/O2 exchange | Yes (Phase 3, flora since Phase 2) | Yes (O2, CO2 bands) | Yes (MS-LS1-6, HS-LS2-5) | Yes | 4 | **Primary** |
| B1 Cause and effect | Yes (all phases) | Yes | Yes (HS-ESS2-2) | Yes | 4 | **Primary** |
| B2 Feedback loops | Yes (fauna, storms) | Yes (balancing) | Yes (HS-ESS2-2) | Yes | 4 | **Primary** |
| B4 Trade-offs | Yes (Phase 2–3) | Yes (the band *is* a trade-off) | Yes (HS-ETS1-3) | Yes | 4 | **Primary** |
| D2 Link to Earth | No (journal, story, debriefs) | No | Yes (MS-ESS3-5 for D2; HS-ESS2-4) | Yes | 2 | **Primary** (override, see rationale) |
| C2 Design, test, improve | Yes (failures in all phases) | Partial | Yes (MS-ETS1-4; HS-ETS1-2, -4) | Partial | 2+ | Secondary |
| C3 Data use | Yes (Monitor, all phases) | Partial | No (practice only) | Yes | 2+ | Secondary |
| B3 Prediction | Partial (forecasts are an added layer) | Partial | No (HS-ESS3-6 Moderate) | Partial | 1+ | Secondary |
| B5 Unintended consequences | Yes (fauna, Phase 2–3) | No | No (Moderate) | Yes | 2 | Secondary |
| C1 Resource planning | No (mainly Phase 1) | No | Yes (MS/HS-ETS1-1) | Partial | 1+ | Secondary |
| A1 Mars vs. Earth | Yes (Monitor) | No | No (MS-ESS1-3 Moderate) | Partial | 1+ | Secondary |
| A4 Radiation | No (Phase 1 hazard, fading) | No | No (HS-PS4-4 Moderate; MS none) | No | 0 | **Can be deemphasized** |
| D1 Ethics | No (story, reflection) | No | No (no PE) | Partial | 0+ | **Can be deemphasized** |

**What each tier means**

| Tier | Gets | If scope is cut |
|---|---|---|
| **Primary** | Resources first: content, feedback, playtest attention, post-test items. Must meet its criterion (0.6) for the release decision | Never cut. A design change that removes its teaching, feedback, or measure is blocked until replaced (DOCUMENTATION.md 3) |
| **Secondary** | Full alignment, built after Primary needs are met | Dedicated content can shrink (e.g. fewer forecasts), but it keeps one teaching element, one feedback element, and one individual measure |
| **Can be deemphasized** | Built last | Can drop to "taught by world and journal, measured by post-test only". A4: drop story beat 5 and forecast F7 first. D1: keep the Report 2 question and final reflection; drop optional beats 11 and 16 first |

**Decision rationale** (DOCUMENTATION.md section 4)
- **Decision:** the tiers above.
- **Rationale:** criteria 1 and 2 follow TEAM.md 8.4 ("the win condition is the main learning outcome"): goals the core loop and win condition already carry are the cheapest to teach well and the most practiced. Criterion 3 protects the NGSS claims a teacher will rely on. Criterion 4 reflects 0.5 (transfer to Earth is the main target).
- **Override:** D2 scores 2 but is Primary, because the SME found that four Earth-science mappings (MS-ESS3-5, HS-ESS2-4, HS-ESS3-6, and A3's MS code) only count if D2 content exists. Losing D2 would weaken A3 and B2 at the NGSS level too.
- **B3 note:** B3 is Secondary as a *goal*, but forecasts are also the main per-student in-game evidence for A2–A5 and C1. Cutting forecasts hurts measurement of Primary goals; treat the forecast system as Primary infrastructure.
- **Alternatives considered:** (a) all goals equal: rejected, gives no guidance when scope is cut (risk R-01). (b) Rank by NGSS alone: rejected, would push D2 and B2 down at MS and ignore the core loop. (c) Rank by what's cheapest: rejected, makes learning priorities follow production convenience.
- **Trade-offs:** instructional value: protects the systems goals and the Earth link that carry most of the learning gain. Market appeal: A4 (radiation spikes) and D1 (the thawling story) are some of the most dramatic content, so deemphasizing their *learning* support doesn't mean cutting the *mechanics*; the Lead Game Designer may keep them for appeal.
- **Revisit if:** the first playtests show a deemphasized goal is learned well for little cost, or a Primary goal can't reach its criterion; the SME's verbatim NGSS check changes a verdict; or the breathability decision turns O2 into a richer B4 trade-off.
- **Status: decided** (user, 2026-11-26, option **T-C** in the Producer's brief `docs/production/consensus-round2.md`). The tiers above **plus the quadrant rule**: *learning resources* (content, feedback, playtest attention, post-test items) follow the goal tier; *feature cut order* follows the fun–learning quadrant (DESIGN.md, Fun), so Core-quadrant features (fauna options, ending cards, radiation spikes) are cut only after Redesign and Moderation items. Any cut that drops a goal below its tier's minimum goes to the user. Flexible (TEAM.md 8.1). The Lead Game Designer's position was still pending when the user decided.
- **Why T-C over the alternatives** (DOCUMENTATION.md §4): T-A (tiers alone) left the appeal concern unanswered: a scope cut could remove dramatic A4 and D1 features because their *goals* rank low. T-B (D1 moved to Secondary) gives a stronger D1 guarantee with no evidence yet that D1 needs it. T-C keeps the criteria unchanged and protects appeal with a rule the team already uses (the matrix).
- **Trade-offs:** learning: Primary goals keep first claim on learning resources; appeal: Core-quadrant features survive cuts regardless of goal tier. Cost: none beyond the existing cut list (`docs/production/budget.md`).
- **Revisit:** D1's tier after the Stage 3 story test. **Fallback:** if the Lead Game Designer blocks on D1, option T-B (D1 to Secondary); I can live with T-B.

### Science accuracy notes

Where the game simplifies, the journal says so. This keeps the learning honest. Wording and sources from the SME (`docs/science/accuracy-review.md` Part C and proposed honesty notes). Journal text at middle school depth:

| Simplification | What's true (with source in accuracy-review.md) | Journal line (middle school) |
|---|---|---|
| Warming Mars with its own CO2 | Accurate: most of Mars's remaining CO2 can't be reached; releasing it would only about double today's pressure (Jakosky & Edwards, 2018 [22]) | "Scientists measured how much CO2 Mars has left. Even releasing all the CO2 we can reach would only raise the pressure to a few percent of Earth's. So the game's machines make *other* gases too." |
| Keeping the air without a magnetic field | Accurate: solar wind strips Mars's air (MAVEN, about 100 g/s today [25]); at that rate, losing the whole atmosphere takes billions of years (SME calculation). Venus has no global field and keeps a thick atmosphere [26] | "Without a magnetic shield, the Sun's wind slowly strips Mars's air. At today's rate this takes millions to billions of years, so it wouldn't undo terraforming in human times. It is a big reason ancient Mars dried out." Do **not** say "a magnetic field is required to keep an atmosphere". |
| Speed of terraforming | Accurate, and the oxygen part is longer than we first said: warming ~100 years; oxygen by plants up to ~100,000 years (McKay, **partly verified** [27]) | "Warming Mars might take about a century. Filling its air with oxygen made by plants could take up to about 100,000 years. The game squeezes this into hours." |
| The dormant fauna (thawlings) | **Fiction.** No life has ever been found on Mars | "The creatures are made up for this game. No life has been found on Mars. But real changes to a planet do have side effects." (Paired with a real Earth example, D2 content.) |
| Breathable air | Breathing depends on O2 partial pressure. The game's breathable condition (O2 partial pressure ≥ 16 kPa, about an airliner cabin) follows the SME review (B2, [5]) | "Your lungs care how much oxygen pushes into them, not just the percent. Thin air with 21% oxygen is like a high mountain." |
| Filler gas | The game doesn't track the filler gas that would make up most of the air at equilibrium (~77% at 75 kPa); Mars has very little nitrogen ([12], [13]) | "Earth's air is mostly nitrogen. Mars has very little, so real terraformers would need to find or bring a filler gas." |

---

## 2. Methods and Media

| Method | What it looks like in the game | Learning-science principle | Goals it serves |
|---|---|---|---|
| **Simulation / learning by doing** | The Planetary Index responds to what players build. Variables follow simplified real physics. | Simulation-based learning for cause and effect | A1–A5, B1–B4 |
| **Discovery and experimentation** | Players test machines and see results; no tutorial tells them the answer. | Guided discovery (hints, not answers) | B1, B3, C2 |
| **Productive failure** | Breaches, storms, and wear break systems. Players diagnose and rebuild. | Productive failure; fading support (Hana's tips stop once a failure is fixed) | C1, C2, B5 |
| **Data literacy** | Planetary Monitor shows current values, trend graphs, and causes. | Data interpretation in context | B3, C3, A1 |
| **Embedded explanation** | Journal entries unlock at each gate and explain the real science behind what just happened. Short and optional. | Just-in-time explanation | A1–A5 |
| **Forecasts (prediction prompts)** | BELL asks "Log your forecast" at key moments. The player picks an answer, then sees the result. | Prediction and self-explanation; retrieval practice; **spaced retrieval** (A4 is forecast in Phase 1 and again in Phase 2) | B3, A2–A5, C1 |
| **Keeper's Reports (reflection)** | At each phase gate the player sends a report to Earth with one reflection question. | Reflection; argument from evidence | B2, B4, B5, C2, D1, D2 |
| **Writing for the colonists** | The Keeper's Record is framed as "what the colonists will need to know". | Expecting to teach (Nestojko et al., 2014; citation **unverified** in this session) | All |
| **Collaboration** | In class, 3–5 students share one planet. Suggested **colony posts** (Life Support, Atmosphere, Survey, Biology, Data) rotate each phase (DESIGN.md Group Play); the **Shift Board** shows who claimed each failure. | Cooperative learning: positive interdependence **and** individual accountability (Johnson & Johnson). Posts give each student ownership; per-player evidence keeps accountability individual | B4, C1, C2, C3 |
| **Worked example, faded** | Hana's notes on the first Regolith Outgasser show each step and where to see its effect; the first atmospheric processor gets a completion version; later machines get one line (2.2). Skippable. | Worked-example effect; backward fading; expertise reversal | B1, C3, A1 |
| **Advance organizers** | A short **Shift Briefing** from BELL at each phase start: what last shift achieved, the big idea of this shift, what to watch, the next gate, one thing to notice (2.1). | Advance organizers (Ausubel, 1960); stimulating recall (Gagné event 3) | All, by phase |
| **Interleaving** | Phase 2 and 3 forecasts mix in earlier-phase ideas at unpredictable moments, without saying which idea applies (2.3). | Interleaved practice; desirable difficulties (Bjork & Bjork, 2011) | A2, A4, C1, B1, B3 |

**Outside the game (for classrooms):**
- Educator guide: goals, how each phase maps to them, discussion questions for the three debriefs, the classroom unit plan (section 5.2), the group model and colony posts (with a tip to give every student at least one turn on the Data post or at the Monitor before a build), and an IT requirements sheet.
- Worksheets: pre/post test, transfer task, and a variable-tracking sheet.
- Draft guide: [docs/learning/educator-guide.md](docs/learning/educator-guide.md).

**Citations in 2.1–2.3 and 4.8.** Ausubel (1960), Bjork & Bjork (2011), Earley et al. (1990), and Wood (2000) are cited from DESIGN.md "Learning Mechanisms in Play"; I did not re-read them this session. Checked this session by search (abstracts and catalog records only, not full text): Kalyuga, Ayres, Chandler & Sweller (2003), "The expertise reversal effect", *Educational Psychologist* 38(1), 23–31; Renkl & Atkinson (2003), "Structuring the transition from example study to problem solving…", *Educational Psychologist* 38, 15–22 (fading worked-out steps); backward fading (removing the last steps first) comes from a related 2002 *Journal of Experimental Education* study by the same group, and self-explanation prompts on worked steps from Atkinson, Renkl & Merrill (2003). Rohrer & Taylor (2007) on interleaving: **not re-checked** this session.

### 2.1 Shift Briefings (advance organizers)

**Problem.** Each phase brings new ideas at the moment the game is busiest. An advance organizer gives players a frame to hang the new details on *before* they arrive (Ausubel, 1960). Where the content is new, the organizer states the big idea in plain terms; where it builds on what players know, it compares the new idea to the familiar one. (Ausubel's "expository" vs. "comparative" organizers; my summary, **not re-checked**.)

**Format (all briefings).** BELL's voice, on a briefing card at the first natural pause after the phase gate (interruption rules, 4.6), copied to the journal under "Briefings"; never blocks; replayable. **≤ 45 words** (the Lead Game Designer's budget, story bible §6.1), grade 6–7. Final text: `docs/design/messages/phase-1.md` to `phase-3.md` (Lead Game Designer). Instructional parts:

| Part | Purpose | Principle | Required? |
|---|---|---|---|
| **This shift** (1 line) | The big idea, in colony terms | Advance organizer | Yes |
| **Watch** (2 Monitor values) | Points attention to the relationship that matters | Signaling; cuts search load | Yes |
| **Next** (the next gate or goal) | Clear goal | Flow: clear goals | Yes |
| **Recall** | What the last shift achieved | Gagné event 3 | Covered by the sequence: SB2 and SB3 come right after the Keeper's Report screen, which *is* the recall. SB1 has nothing to recall |
| **Notice** (≤ 10 words, one thing to look for) | Sets up the Keeper's Report question that ends the phase, without the answer | Bookends the phase: organizer → reflection | **Requested** (not yet in the draft text) |

**Instructional content per briefing**, checked against the Lead Game Designer's draft text (round 2). Sample lines show intent; final wording is theirs; the SME checks every science line.

| Briefing | When | Big idea (part 2) | Watch (part 3) | Next (part 4) | Notice (part 5) → report | Goals | Comparative link to the familiar |
|---|---|---|---|---|---|---|---|
| **P1: "Keep the dome alive"** | End of Landing onboarding, before F1 | Mars has thin, cold air with almost no oxygen. The dome makes what the planet lacks. | O2 and power | Dome stable (life support steady for the hold) | "Notice what almost fails, and why." → Report 1 | A1, C1, C2 | "Inside the dome is a small piece of Earth's air." |
| **P1b: "Start on the sky"** (not a separate card: carried by Hana's Dome stable message and worked example, story beats 6 and 6a) | Dome stable | More gas in the air means more pressure. One machine moves a whole planet slowly. | Pressure and its cause lines | Phase 2 (pressure 5 kPa) | "Notice what makes pressure go up, and what pulls it down." | A1, B1, C3 | — |
| **P2: "Warm it and thicken it"** | Phase 2 begins (after Report 1 is offered) | Water needs both warmth and enough pressure to stay liquid. Greenhouse gases keep heat from escaping. | Temperature and pressure together (and water's boiling point) | Thaw, then Hydrosphere, then Phase 3 | "Notice what else changes when the planet warms." → Report 2 (doesn't name the fauna: no spoiler) | A2, A3, B1, B5 | Recall of the Phase 1 ice that vanished; "Earth's air holds in heat" **[SME: wording; avoid the 'blanket' analogy unless the SME clears it]** |
| **P3: "Make the air breathable, then keep it"** | Phase 3 begins (after Report 2 is offered) | Plants take in CO2 and give off O2. Breathable air needs enough oxygen *and* enough pressure; too much oxygen means fire. Balance is a band you hold, not a number you hit. | Air window (O2 and pressure) and CO2 | Breathable, then Equilibrium | "Notice what you have to give up to hold the balance." → Report 3 | A5, B4, B2 | Recall of F4a/F4b: "Pressure changed what water could do. Now it changes what you can breathe." |

**Review of the draft text (Shift Briefings 1–3, `docs/design/messages/`): accept, with one request.** All three name the job, the values to watch, and the next goal, within budget. SB2's "Watch pressure and temperature together: ice needs both to become water" is exactly the A2 organizer, and "warm machines change the ground near them" sets up B5 without spoiling the fauna. SB3's "Holding is harder than climbing" is a good B4 frame. **Request:** add the ≤ 10-word "Notice" line to each, pointing at its report question (Lead Game Designer; it may push SB3 just over 45 words; I'd accept 50). 

**Fading.** Landing (the first-shift checklist) teaches *where* things are on the Monitor; the briefings only name the values. In classroom mode the teacher can project the same text as the period opener for the phase (educator guide), so the in-game organizer and the class match.

**Measure.** Not graded. Logged (`briefing.shown`, `briefing.opened`) so playtests can check whether players who open it can say the shift's goal. Playtest question (Stage 3): after the P1 briefing, can a Lens A player say what to watch?

**Group play.** Fires once per planet; each player gets it at their own next natural pause; absent players find it at the top of "Since last shift".

### 2.2 Worked example: the first Regolith Outgasser (with the Lead Game Designer)

**Why here.** The Outgasser is the player's first lever on the planet (balance sheet §2) and the first machine whose effect is on the **Index**, not on the dome. Novices to tech mods need to see one complete, correct example before they solve the next on their own (worked-example effect). Players who already know modded machines learn less from a step-by-step guide than from doing it (expertise reversal, Kalyuga et al., 2003). So: a worked example that's **easy to skip** and **fades** into practice.

**Delivery (diegetic).** *Hana's notes*: taped to the first Outgasser, opened as a journal page, never a tutorial screen (DESIGN.md Story Approach). Final text and triggers: `docs/design/messages/phase-1.md` §4 (Lead Game Designer, round 2).

**Review of the Lead Game Designer's version: accept.** Their design fades **across machines** (full notes on the first Outgasser → a completion version on the first atmospheric processor, with "Your turn" steps → a one-line "You know the drill" on later machines). That is better than the per-Outgasser fading I first drafted: the completion problem lands on a *new* machine, so the player transfers the steps instead of repeating them. What the instructional side requires, and where the draft meets it:

| Requirement | Met by the draft? |
|---|---|
| Each step shows the action **and** where to look for the effect | Yes: steps 5–6 send the player to the Monitor's pressure causes (C3, B1) |
| Never gives away the forecast (F3) | Yes: step 4 says "Guess first"; the note "never states the result" |
| Ends with a problem, not more instructions | Yes: step 6 "Watch the line, then decide"; F3 fires at switch-on |
| Honesty note (A1): real soil holds too little gas | Yes: fiction note on the entry |
| Completion version leaves the *key* steps to the player (backward fading: the last steps go first) | Yes: "what does it need to run?" and "where will you see its effect?" are the player's |
| A reason with each step, to invite self-explanation | **Partly**: steps 1 and 3 have reasons; steps 2 and 5 could add a ≤ 8-word reason (request, not a block) |
| Skippable for experienced players | Yes, but see below |

**Expertise reversal: how skipping should work.**
1. **"Hide Hana's notes"** (the draft's skip) hides the worked-example notes only, never Hana's first-failure tips, and the notes stay **reopenable from the journal**. The draft says it closes them "for good"; I ask that "for good" means "not shown again automatically", not "deleted". (Lead Game Designer.)
2. **Skip by doing.** If the player places, feeds, and powers the Outgasser before opening the notes, the notes tick themselves and file to the archive.
3. **Never automatic from Landing.** "I know the drill" on the Landing checklist is evidence of vanilla controls, not of modded machines, so it doesn't hide the notes.

**Group play.** Notes are per player. A player who didn't place the first Outgasser gets the full notes at their first Outgasser interaction (place, power, or open), and the completion version at their first processor. Watching a teammate counts as nothing; the log can't see it.

**Measure and check.** Logged: `worked_example.opened`, `.collapsed`, `.skipped_by_doing`, `.steps_viewed`. Not graded. The check for the expertise-reversal call: in playtests, compare F3 results and "Monitor opened before the 2nd Outgasser" for players who used the notes vs. hid them. If skippers do as well or better, keep the skip; if Lens A players skip and then fail F3, make the skip a two-step confirm.

**Goals:** B1, C3, C1 (the draft's tags), plus A1 (honesty note) and B3 (ends with F3).

### 2.3 Interleaving in later forecasts

**Problem.** The forecast set in `docs/learning/prompts-and-reflections.md` is *blocked*: each forecast asks about the idea that just unlocked, so the trigger tells the player which idea to use. Blocked practice feels easier but transfers worse than interleaved practice, where the learner must first decide *which* idea applies (Bjork & Bjork, 2011; Rohrer & Taylor, 2007, **not re-checked**).

**Design.**
- **Phases 2 and 3 add interleaved forecasts (FI)** that bring back an earlier-phase idea in a new situation. They're attached to events BELL can warn about at least ~1 sol ahead (dust storms, solar events, polar winter), so they follow interruption rule 7 (4.6) and arrive at unpredictable times. Drift events themselves are too fast for a forecast (about 1 sol to push a value out of band in classroom mode), so they stay unprompted application.
- **No topic label.** The question never says "remember the ice" or names the phase. Choosing the right idea is the skill.
- **Pool, not a list.** Phase 3 has a pool of three; each player gets the first two whose events fire. Different planets get different mixes.
- **Count and load.** Interleaved forecasts count toward the B3 tally of the phase they're asked in. Totals become P1 4, P2 5, P3 5. Phase 3 stays at 5, so forecasts don't start to feel like a test (open question: "How visible is the learning?").
- **Evidence.** Tagged `interleaved: true` with the source goal. A correct FI also fills the source goal's Keeper's Record page (e.g. FI-2 fills A2). New export columns: `forecasts_interleaved_answered`, `forecasts_interleaved_correct` (5.5). The FI items are the in-game retention check that the delayed post-test (3.6) continues outside the game.

**Items.** Specified in `docs/learning/prompts-and-reflections.md` section 1.1 as stems only. **Science pending the SME** (forecast content is under SME review in parallel; nothing in 1.1 rewrites F1–F10).

| ID | Phase / trigger | Brings back | Mixed with | Goals |
|---|---|---|---|---|
| FI-1 | P2 / first dust storm warning after the first emitter runs (stem names the solar-powered emitters, SME round 3) | Storms cut solar power (F1) | Warming rate (A3) | C1, A3, B1, B3 |
| FI-2 | P3 / first passing low-pressure weather warning (SME round 3: a polar-winter dip can't happen at equilibrium) | Pressure sets water's boiling point (F4a, F4b) | A short weather-driven pressure dip | A2, B3 |
| FI-3 | P3 / first solar event in Phase 3 | Air absorbs radiation (F2, F7) | Helmet-off life on the surface | A4, B3 |
| FI-4 | P3 / dust storm warning during a hold | Storms cut solar power (F1) | Algae vats and CO2 (A5) | C1, A5, B1, B3 |

---

## 3. Assessment: How We'll Know

### 3.0 Individual vs. group evidence

On a shared group planet, gates, the dome, and the breathable air belong to the whole group. One strong player can reach them for everyone. So:

| Evidence level | What counts | Used for |
|---|---|---|
| **Individual, summative** | Pre/post test, transfer task, written reflections (rubric-scored) | Grades and the learning study. These are the only measures a teacher should grade with |
| **Individual, formative** | Own forecasts, own "What caused it?" answers, own repairs, own Monitor checks before own builds | Keeper's Record, teacher overview, feedback to the student. **Not for grades**: in a group, chances to act differ (who held the keyboard, who was absent) |
| **Group (per planet)** | Gates reached, dome survival, breathable condition held, equilibrium, beacon, drift events answered | Group progress, pacing decisions, class discussion. Never copied into one student's evidence |

This rule also applies to the Keeper's Record (section 4.3): a page fills only from the player's **own** evidence. Group milestones show in a separate "Cairn team" section.

**C2 credit on the Shift Board: decided (user, round 3): credit the player who did the repair (`shiftboard.creditRule = actor`), not a claimer who didn't repair.** C2 is "diagnose and fix so it stays fixed"; claiming a failure and handing it off shows coordination, not repair. The claim is still logged (useful for the teacher and for B4/C3 discussions), and a claimer who diagnosed the cause still earns C2 evidence through a correct "What caused it?" answer on that failure. `failure.repeat` → `prior_credit` follows the same rule. Alternatives considered: credit the claimer (DESIGN.md's earlier wording; rewards coordination, not repair); shared credit (blurs individual evidence). Trade-offs: learning: C2 evidence goes to the student who showed the skill; appeal: none lost (claims still show on the Shift Board). DESIGN.md Group Play follows (Lead Game Designer). **Revisit if** playtests show groups where one student always diagnoses and another always repairs (then consider shared credit).

### 3.1 In-game evidence (stealth assessment)

The game logs actions to a **local file** on the server's computer. Each player is a random per-world pseudonymous ID (5.4); no names are logged. Every event records the actor's ID, so evidence can be attributed per student.

| Evidence | What it shows | Level | Goals |
|---|---|---|---|
| Time and order of gates reached | Progress and pacing | Group | Overall |
| How players respond when values fall (repair, rebalance, or ignore), and who did it | Diagnosis and system management | Individual (actor) | B2, C2 |
| Whether O2/CO2 is stabilized or swings wildly | Understanding of balancing loops | Group | A5, B4 |
| Shift Board claims and fixes; repeat failures from the same cause after a player's own repair | Learning from failure | Individual (the repairer's ID, decided round 3) | C2 |
| Panels cleaned while dusty (stage ≥ 2) inside a storm warning window | Planning ahead for a known threat | Individual (actor) | C1 |
| Drift events answered during holds, and by whom | Active system management | Individual (actor) and group | B4, C2, C3 |
| Monitor opened before own major builds | Data-driven decisions | Individual | B3, C3 |

### 3.2 Embedded checks

- **Forecasts:** each player answers on their own screen; other players' answers stay hidden until the forecast resolves. Right/wrong is logged per player. Improvement over the playthrough shows a better mental model (B3). A forecast answered after its outcome is visible is logged as late and not scored. The forecast set is in `docs/learning/prompts-and-reflections.md` section 1.
- **"What caused it?" checks:** optional journal page; pick the cause of a recent event from a short list (A1–A5, B1, C2).

There are no quiz questions at gates. Gate journal entries explain; forecasts and "What caused it?" checks do the checking, in the colony's own voice (diegetic immersion, TEAM.md section 3).

### 3.3 Outside the game

- **Pre/post test:** the same 12–15 items before and after play, written per grade band and tagged with goal ID and NGSS code (section 6.4). Learning gain is the main success measure. Blueprint:

| Goal | Item type | Items |
|---|---|---|
| A1 | Data task: Mars/Earth table, compare and explain | 1 (4 parts) |
| A2, A3, A5 | Short explanation of a new scenario | 1 each |
| A4 | Scenario: solar event, where to be and why | 1 |
| B1 | Blank 5-variable link diagram | 1 |
| B2 | Identify the loop type in a short Earth example | 1 |
| B4 | Trade-off scenario | 1 |
| C1 | Storm planning scenario | 1 |
| Misconceptions (0.2) | Multiple choice, one per row | 4–6 |

- **Reflection writing:** Keeper's Report answers, scored with the rubrics in `docs/learning/rubrics.md` (B5, D1, D2). Class discussion is formative.
- **Transfer task:** apply the same reasoning to Earth, e.g. "Draw the feedback loops in Earth's carbon cycle" (B1, B2, D2). Rubric in `docs/learning/rubrics.md`.

### 3.4 Success criteria (draft)

- Average post-test score improves meaningfully over pre-test (target to set after the first playtest). Individual.
- Each student's B3 criterion (0.6): own Phase 3 forecast accuracy at least 20 percentage points above own Phase 1. Target: most students meet it (number set after the first playtest). Individual.
- At least 70% of students score 3/4 or higher on the D2 rubric in the transfer task. Individual.
- At least 70% of **group planets** reach the Phase 3 gate within the unit. Group.
- Students can name at least two feedback loops and one unintended consequence in their reflections. Individual.

### 3.5 Minimal learning layer for the Stage 3 vertical slice

Decided 2026-11-26: the vertical slice includes a thin Phase 1 learning layer so its playtest can check learning, not only fun. Recommended contents (the Producer schedules them in ROADMAP.md):

| Item | Goal | Why it's in the slice |
|---|---|---|
| One gate journal entry (Dome stable), with its "On Earth" line | A1, D2 | Tests reading level and length |
| Forecasts F1 (dust storm) and F4a (warmed ice vanishes, no water), and F2 if the radiation spike is in the slice | B3, C1, A2, A4 | Tests the forecast flow and hidden answers in a group |
| One failure report (seal breach) with Hana's tip | C2 | Tests "hints, not answers" |
| Keeper's Record skeleton with 2 pages (C1, C2) and the "Cairn team" section | C1, C2 | Tests own-evidence vs. group-milestone display |
| Action log with actor IDs, and a CSV export | All | Tests per-student evidence end to end |
| A 6-item Phase 1 mini pre/post test (A1 data task, A4, C1, two misconceptions) | A1, A4, C1 | First evidence of learning gain |

### 3.6 Optional delayed post-test (RETAIN naturalization)

**Problem.** RETAIN's naturalization asks whether using the knowledge becomes habitual and spontaneous. The post-test in period 23 is given the day after play ends, so it can't tell retention from short-term memory, and it always cues the concept ("explain the greenhouse effect"). Nothing measured whether learners use the ideas unprompted later.

**Design.**

| Feature | Choice | Reason |
|---|---|---|
| Status | **Decided (user, round 3): optional, scheduled in Stage 6** as part of the learning study (a design artifact in this exercise); a teacher can also use it alone. Classroom only | Teachers' calendars vary (risk R-19). Home players aren't tested outside the game (no teacher) |
| Timing | **About 4 weeks after the post-test** (acceptable window 3–6 weeks). Record the actual interval in days | Long enough that the unit isn't fresh, short enough to stay in the same term. The exact interval is a design judgment, not a research norm (**unverified** as optimal) |
| Length | **9 items, ~20–25 minutes**, one class period or less | Fits a start-of-period slot |
| Form | Paper or the school's usual quiz tool; outside the game (TEAM.md 8.5) | Same as the pre/post test |
| Bands | MS and HS versions, like the pre/post test | Same depth rules (prompts-and-reflections §5) |
| Identity | Students write their **player ID** (`P-XXXXX`), not their name, on study copies. The teacher hands out ID slips at period 1 and collects them back at period 23, to reuse for the delayed test. Only ID-labeled scores go to the study | Keeps study data anonymous after the teacher runs `forget-all`; the slips stay in the classroom like any class paperwork |
| No teaching before it | Teachers don't review the unit before the delayed test | Otherwise it measures the review, not retention |

**Item blueprint** (items to be written in Stage 4 with the pre/post test; SME checks the science; tagged with goal ID and NGSS code like the pre/post items).

| # | Type | Goal | Source | Measures | NGSS (MS / HS) |
|---|---|---|---|---|---|
| 1 | **Anchor**: same item as the post-test | A2 | Post-test A2 item | Retention, direct comparison | MS-PS1-4 / extends MS-PS1-4 |
| 2 | **Anchor** | A3 | Post-test A3 item | Retention | MS-ESS3-5 / HS-ESS2-4 |
| 3 | **Anchor** | A5 | Post-test A5 item | Retention | MS-LS1-6 / HS-LS2-5 |
| 4 | **Anchor** | B4 | Post-test trade-off scenario | Retention | MS-ETS1-2 / HS-ETS1-3 |
| 5 | **Parallel** (new scenario, same structure) | B1 | 5-variable link diagram with a new variable set (e.g. an Earth lake or farm) | Retention with new surface features | MS-LS2-4 / HS-ESS2-2 |
| 6 | **Parallel** | B2 | Identify the loop type in a new short Earth example | Retention | — / HS-ESS2-2 |
| 7 | **Misconception** (multiple choice) | A5 | Post-test "21% oxygen is always breathable" item | Did the misconception come back? | — (no PE assesses partial pressure; tagged A5 only) |
| 8 | **Misconception** (multiple choice) | A2 | Post-test "water always boils at 100 °C" item | Same | MS-PS1-4 |
| 9 | **Cue-free "spontaneous use"** (open response) | B1, B2, B5, D2 | New: an Earth news-style scenario that names no science idea, e.g. "A town wants to drain a wetland to build a factory. What should they think about before they decide?" | Whether the student *chooses on their own* to use systems reasoning (side effects, loops, trade-offs) | — (SEP: Constructing explanations; CCC: Systems and system models) |

**Keys and science check (SME round 3, `round3-review.md` §4.4):**

| Item | Key notes |
|---|---|
| 1–4 | Same as the post-test anchors; checked when those items are written |
| 5 | Earth lake or farm link diagram: accept links with correct direction, e.g. fertilizer → algae growth (+); algae → water O2 at night (−) (textbook eutrophication; citation **unverified**) |
| 6 | Loop type in a new Earth example. Use **ice–albedo** (reinforcing) or **wetland loss → less humidity → more wetland loss** (reinforcing): both are the examples in HS-ESS2-2's clarification statement (verified) |
| 7 | "21 % O2 is always breathable": **false**; the answer turns on O2 partial pressure. Distractor: "true, because Earth's air is 21 %" |
| 8 | "Water always boils at 100 °C": **false**; lower on mountains (Everest about 72 °C, calc) |
| 9 | Wetland-drain scenario: well chosen (HS-ESS2-2 names wetland loss as a feedback example). Anchor for a 4: names the humidity–wetland loop, or the loss of a carbon store (wetland soils store carbon: **unverified** this round) |

**Item 9 rubric (naturalization, 1–4)**, added to `docs/learning/rubrics.md` when the items are written:
- 4: Unprompted, uses at least two systems ideas (a side effect with its cause chain, a loop, or a trade-off with a cost), correctly applied.
- 3: Unprompted, uses one systems idea correctly.
- 2: Mentions effects, but only first-order ("it will pollute"), no chain, loop, or trade-off.
- 1: No systems reasoning.

**Optional self-report (2 questions, anonymous, not scored):** "Since the unit, have you used an idea from the game outside class? If yes, where?" This is a weak measure of habits (self-report) and is reported only as context.

**Analysis.** Retention = delayed score − post score on items 1–4 (same items), per student by ID; items 5–8 against the matching post items; item 9 as a distribution of rubric scores, compared with the same rubric applied to the post-test transfer task. Expect some drop; the question is how much is kept compared with pre-test. Note: the post-test itself is retrieval practice, so anchor items may overstate retention compared with a group that never took the post-test (accepted; there is no control group in a design exercise).

**Success criterion (draft, set after the first study):** most students keep at least half of their pre→post gain on items 1–4; median item 9 score ≥ 2 at MS, ≥ 3 at HS.

**Cost and effect on both goals.** Learning: closes the naturalization measure gap and gives the only retention evidence. Appeal: none (outside the game, optional). Teacher cost: one ~25-minute slot and scoring 9 items. **Revisit if** teachers in a pilot can't find the slot (then use items 1–4 and 9 only, ~12 minutes).


### 3.7 Four purposes of embedded assessment

Chapter 9 names four purposes. Map of each assessment piece (✅ serves it, 🟡 partly, — no):

| Piece | (a) Inform the teacher | (b) Tailor in-game instruction | (c) Feedback to the student | (d) Evaluate against the objectives' standard |
|---|---|---|---|---|
| Action log | ✅ via overview and CSV | 🟡 drives hint escalation and fading (4.8) only | — (players never see it raw) | 🟡 formative counts only (3.0) |
| Forecasts (incl. FI) | ✅ per-phase accuracy | 🟡 pointers return when accuracy is low (4.8) | ✅ result + explanation | 🟡 B3 criterion is formative; A2–A5 are evaluated by the post-test |
| "What caused it?" checks | ✅ | — | ✅ | 🟡 formative |
| Failure reports + Shift Board | ✅ claims, fixes, recurrences | ✅ hint level and fade follow own fixes | ✅ | 🟡 C2 formative |
| Keeper's Record | ✅ per page, per student | — | ✅ (self-assessment, 4.3) | 🟡 shows *a* piece of evidence, not mastery |
| Keeper's Reports + rubrics | ✅ (`reflections.csv` or paper) | — | 🟡 Mensah's reply responds to the planet, not the answer | ✅ D1, D2, B5 rubrics (criterion: 3 of 4) |
| CSV exports / class overview | ✅ | — | — | 🟡 columns map to criteria (0.6) but don't decide mastery |
| Pre/post, transfer, delayed test | ✅ | — | 🟡 only if the teacher returns them | ✅ the summative measures |

**Gaps:**
- **(b) Tailoring is deliberately thin.** We adapt hints and fading, not content or path, because we don't build an automatic learner model at launch (TEAM.md §3, "adaptive"). Accepted for launch.
- **(a) "Who needs help?" isn't flagged.** The overview lists data but doesn't point at students. Proposed rule-based "needs a look" flag in the class overview, never shown to students and never a grade: own forecast accuracy below 1 in 3 after 3 forecasts in a phase; the same failure cause recurring twice after the student's own fix; a report unsent for more than 2 sessions; no own events for a whole session while present. Numbers live in `data/mars/learning/flags.json` (ARCHITECTURE.md §8.6; teacher-only, never shown to students, never exported as a grade). Starting values I set: `forecast_accuracy_below`: 0.33 after `min_forecasts`: 3 per phase; `repeat_after_own_fix`: 2; `report_unsent_sessions`: 2 (or past `interrupt.debriefMaxDefers`); `no_own_events_sessions`: 1 while present. Tune after Stage 3.
- **(c) Reflections get no feedback on quality in game.** By design (no automatic scoring of free text); the teacher returns rubric scores. The educator guide asks teachers to return them before the next debrief.

### 3.8 Behavioral indicators

For each goal: what a player does that shows understanding (or its lack), the event that captures it, and how we tell an appropriate expression from a poor one. "Formative" indicators never grade (3.0).

| Goal | Shows understanding | Shows a gap | In-game event (logged with actor ID) | How we tell good from poor |
|---|---|---|---|---|
| A1 | Predicts slow pressure rise; reads Mars values against Earth milestones | Expects Earth-like behavior (pressure jumps, water pools at once) | `forecast.F3`; `explain.answer` on Phase 1 hazards | First-attempt correct choice; post-test data task (summative) |
| A2 | Predicts vapor below the triple point, short puddles, then lasting water | Picks "melts into a puddle" in Phase 1; puzzled by vanishing ice | `forecast.F4a`, `F4b`, `FI-2` (+ HS "why") | Correct main answer (B3) and correct "why" (HS); interleaved FI-2 correct shows it transferred |
| A3 | Predicts warming from emitters; restores a failed emitter when temperature falls | "Blocks sunlight"; "ozone" distractors | `forecast.F5`; `repair` on emitter after `drift.emitter_wear` | Correct choice and "why"; repair before the hold resets |
| A4 | Gets to shielding or depth after a solar warning | Stays exposed after a warning | `forecast.F2`, `F7`, `FI-3`; `radiation.exposure` per player during a warned event | Exposure after a warning compared with the first, unwarned spike; correct forecasts. Needs per-player exposure logged (Technical Lead) |
| A5 | Acts on rising O2 before fire (prunes, slows vats) | Acts only after a fire, or adds more flora | `forecast.F8`, `F9`; `drift.flora_bloom` + actor's response, with O2 at the time | Response while O2 is still in band = good; after crossing 23 % = late |
| B1 | Names the right cause; opens the cause breakdown when a value moves | Guesses; rebuilds an unrelated machine | `explain.answer` (first attempt only); `monitor.cause_opened` | First-attempt accuracy; opening the breakdown within a sol of the change |
| B2 | Names both loop types and their steps | Calls the fauna loop balancing, or "not a loop" | `forecast.F6` "why" (HS); Report 2 text | F6 "why" correct; rubric on Report 2 and transfer task. Fauna option choice is **not** an indicator (it's a values choice, D1) |
| B3 | Own accuracy rises across phases | Flat or falling accuracy; many expired forecasts; answers in under ~2 s | All `forecast.*` with `result`, `answer_time`, `scaffold` | Per-phase accuracy (0.6 criterion); very fast answers flagged as possible guesses (formative only) |
| B4 | Answers drift events without pushing another value out of band | Overcorrects (O2 swings from "not enough oxygen" to fire risk); ignores costs | `drift.*` + actor's actions + Index values over the next sol | Value back in band within ~1 sol **and** no other value leaves its band: good. Complex: see 3.9 |
| B5 | Names a side effect and its cause chain | Names an intended result as a side effect | Report 2 text | B5 rubric (3 of 4) |
| C1 | Fuels or places life support and batteries before the first storm; **cleans dusty panels in the storm warning window** | Core failure during the storm; cleans only after the storm, or cleans panels that were already clean | `forecast.F1`, `FI-1`, `FI-4`; `build`/`refuel` before `storm.start`; `panel.cleaned` with `stage_before`, `storm_warned` (yes/no), `sols_to_onset` | Life-critical actions in the warning window, in the right order (0.6). Cleaning counts **only** if the panel was at stage ≥ 2 and a storm warning was active: cleaning clean panels, or cleaning with no storm coming, is activity, not planning |
| C2 | Own fix holds; picks the right cause | Same cause recurs after own fix | `shiftboard.claim`, `.fixed`; `failure` with cause | No recurrence from that cause in the phase; "What caused it?" correct for own failures |
| C3 | Opens the Monitor and reads it before major builds | Builds without checking, or opens it for under ~1 s | `monitor.opened` (with dwell time) before `build.major` | Half of own major builds preceded by a check of ≥ ~1 s, **plus** a data reason in a report or teacher check-in |
| D1 | A position with a game-based reason and a stake for existing life | Yes/no only; generic reason | Report 2, final reflection text | D1 rubric only; no in-game behavior counts |
| D2 | A game mechanism matched to the same Earth mechanism | Surface link, or a misconception (greenhouse = ozone) | Final reflection, transfer task, report Earth-link answers | D2 rubric only |

### 3.9 Complex behaviors: what we measure, and what we don't attempt

Simple behaviors (a fact, a step) are easy to detect from single events. **Complex behaviors** depend on context: situation awareness, decisions under trade-offs, patterns over time. Chapter 9 points to dynamic Bayesian networks for these. We don't build one at launch: it needs a validated learner model and calibration data we won't have, and it conflicts with our "no automatic personalization" position (TEAM.md §3).

| Goal | Why it's complex | What we do instead | What we don't attempt automatically |
|---|---|---|---|
| **B2** Feedback loops | Recognizing a loop is a judgment about a chain of causes over time | F6 "why" (HS); Report 2 and transfer task with the rubric; teacher discussion | Inferring loop understanding from play patterns (e.g. use of shrouds) |
| **B4** Trade-offs | Good choices depend on the whole planet state and the player's priorities | Simple rule on drift responses (3.8: back in band, nothing else out) as a formative signal; post-test trade-off scenario and Report 3 | Scoring the quality of a decision from Index traces |
| **C2** Design, test, improve | Diagnosis and redesign across time and teammates | Rule-based: own fix, no recurrence; "What caused it?" on own failures | Judging *design quality* of a fix |
| **D1** Ethics | Values argument; no right answer | D1 rubric on written or oral answers, scored by the teacher | Any automatic judgment of fauna choices or text |
| B3, C3 (partly complex) | Patterns across many events | Per-phase ratios (forecast accuracy; checks before builds) | Inferring "situation awareness" from movement or camera direction |

**Principle:** the game logs clean, attributable events and computes simple, explainable counts; judgment of complex skills stays with rubrics and the teacher. **Revisit if** Stage 6 data shows the simple counts disagree badly with rubric scores, or a validated model for one of these skills becomes available.


### 3.10 Performance tracking: time ranges, memory, replay, and gaming the system

**Time range of each measure** (Chapter 9: decide the span each assessment covers):

| Measure | Time range | Notes |
|---|---|---|
| Forecast (F, FI) | **One event**: issue → outcome | Scored once (below) |
| "What caused it?" | One event | First attempt only |
| Failure report level, fade state | Per failure type, **per phase** | Resets to process feedback for new types (4.8) |
| C2 criterion (own fix holds) | **Per phase** | 0.6 |
| B3 criterion | **Whole planet run** (Phase 1 vs. Phase 3) | Needs ≥ 3 forecasts in each phase |
| C3 criterion | Per phase, summarized over the run | — |
| Keeper's Record | **Whole planet run** | Pages stay earned after a rollback (ARCHITECTURE.md §7.4) |
| Keeper's Reports | Per phase (AAR, below) | — |
| Class overview "needs a look" flags (3.7) | **Per session** and per phase | — |
| Pre/post, transfer, rubrics | **The unit** | Summative |
| Delayed post-test | ~4 weeks after the unit (3.6) | Summative, optional |

**What the back end must remember across sessions** (per player, in the learning records outside the world folder, so rollback never deletes it; ARCHITECTURE.md §7): every forecast issued, answered, expired, or voided, with result, answer time, and scaffold flag; the FI pool picks; per-failure-type hint level and fade state; worked-example and briefing state; Record pages and the evidence that filled each; report status and text; Monitor checks before major builds. Per planet: drift-event first occurrences (4.8), session list. (Technical Lead is checking cost.)

**Scoring rules for repeats, replays, and gaming.**

| Situation | Rule | Why |
|---|---|---|
| Repeated attempts at a forecast | **One answer per player per forecast.** The first submitted answer is locked; no retries | A forecast can't be brute-forced by guessing again |
| Answer after the outcome is visible | Logged `late`, not scored (3.2) | It isn't a prediction |
| Deferred until the outcome | Logged `expired`, never `wrong` (4.6 rule 6) | Fair to busy players |
| Re-reading a forecast in the archive | Allowed; it can't be re-answered | Review is good; re-scoring isn't |
| **Rollback** (teacher restores a backup) | Pending forecasts from the lost stretch are voided (ARCHITECTURE.md §7.4). If the same forecast is issued again after the rollback, it's marked `replay`: shown and explained, **not scored** for B3 | The player may already have seen the outcome |
| **A second planet or world** (home replay, a student moved to another group) | New world = new pseudonymous ID = a fresh record. A student moved to another planet in the same class keeps their ID (ARCHITECTURE.md §6.2) | Per-world IDs are settled; we accept that a home replayer's second run starts fresh |
| Guessing | Chance on 3 choices is 33 %, on 4 is 25 %. B3 asks for a **20-point rise** over ≥ 3 forecasts per phase, which guessing rarely produces. Answers under ~2 s are flagged as possible guesses (formative only) | Detects, doesn't punish |
| "What caused it?" farming | First attempt per event only; the B1 page needs 3 correct first attempts | No retry-until-right |
| Record pages from activity | Not allowed (4.9; content rule CR-R2) | Rewards skill, not clicks |
| Monitor-open spam for C3 | Checks under ~1 s don't count; C3 also needs a data reason (0.6) | — |
| Copying a teammate's forecast | Answers stay hidden until the forecast resolves (3.2); talking in the room can't be prevented, so B3 stays formative | Accepted limit of group play |

### 3.11 Validation: how we'll know students learned

**The gap, stated plainly: there is no assessment expert on the team.** Item writing, reliability, and validity are specialist work. Mitigation: (1) use published instruments where one fits; (2) build our own items to a written blueprint (3.3), tagged by goal and NGSS code, and pilot them; (3) the user reviews every instrument as the customer (evaluation loop above); (4) the SME checks answer keys; (5) two-rater scoring of rubrics on playtest answers; (6) if this ever leaves design-exercise status, bring in an assessment specialist before any real pilot (Producer: risk register).

**Published instruments: what exists and what they test.** Searched this session (catalog pages and reviews, not the instruments themselves):

| Instrument | Tests | Fits our goals? | Use? |
|---|---|---|---|
| Greenhouse Effect Concept Inventory (GECI; Keller) | Physics of energy flow through the atmosphere; ~15-minute multiple choice, pre/post; built for undergraduates; validation evidence mainly in one dissertation (PhysPort) | A3 only, at HS depth; too narrow and too advanced for MS | **Candidate** for a few HS A3 items, if its terms of use allow; check reading level |
| Climate Change Concept Inventory (Jarrett, Ferry & Takacs, 2012) | Climate change concepts beyond the greenhouse effect (listed in a 2023 review) | A3, D2 (Earth) | **Candidate** for D2/A3 misconception items; target age and validation **unverified** |
| Other climate inventories in the same review (e.g. the Anthropogenic Climate Change Dissenter Inventory; a German CCCI-422) | Climate science concepts | Partial | Not yet reviewed |
| Water phase change under pressure; partial pressure and breathability; feedback-loop identification; engineering trade-offs | — | A2, A5 (HS), B1, B2, B4, C1 | **No instrument found** this session; **we build these** from the blueprint (3.3) |

**What each instrument tests vs. what the game trains:** the GECI tests energy-flow physics that the game doesn't simulate (we show warming from gases, not radiation transfer), so a gain on it would mostly come from debrief 2, not from play. Our own items test what play trains: predicting outcomes in new scenarios, reading data tables, and naming loops and trade-offs. Report the two separately.

**External review: decided (user, round 3): three external reviewers are sought before Stage 3** (owner action): a planetary scientist, a climate modeler, and science teachers. The teacher requests are in [docs/learning/teacher-review-pack.md](docs/learning/teacher-review-pack.md); the SME builds the science pack. Details (SME round2-review §11; the teacher-facing items fold in here): the **transfer-task answer key** (rubrics.md) needs a high school Earth or environmental science teacher plus a carbon-cycle scientist, because it will be used to grade and some link strengths are uncertain; the **forecast distractors and explanations** need middle and high school science teachers, because whether a distractor *reads* as wrong to a 12–18-year-old is a classroom judgment; the **NGSS mapping** needs a science teacher or state NGSS coordinator. Until then these stay "accepted, pending external review". The planetary-scientist and climate-modeler items (ice rule, ice–albedo, polar frost) affect forecasts F4a, F4b, FI-2, and B2 content; they're tracked in the SME's list.

**Validation steps for our own items (Stage 4–6):** blueprint review by the SME and the user; a read-aloud check for reading level; a pilot with stand-in learners (the user, in persona); item analysis after the first real use (difficulty, discrimination); check that rubric scores and in-game indicators (3.8) point the same way.

---

## 4. Player Feedback and Self-Assessment

Section 3 is how *we* (designers, educators) know. This section is how *players* know, while they play.

**Answer: a combination.** Natural consequences come first, and specific feedback explains them. Every piece of feedback lives inside the game world (Monitor, journal, colony log), so it keeps diegetic and flow immersion and never turns into a pop-up quiz (TEAM.md section 3).

### 4.1 Natural consequences: "did it work?"

The world itself responds to what the player does. This is the fastest and most frequent feedback.

| Player action | Consequence they see | Tells them |
|---|---|---|
| Builds a pressure source | Sky and fog change as pressure rises (keyframes set by the Lead Artist; sky colors **unverified**, SME to check) | Pressure is rising (A1, B1) |
| Lets maintenance slip | Machine sputters, values on the Monitor drop | Systems need upkeep (C2) |
| Pushes warming hard | Ice shrinks, then fauna appear near the emitters | Interventions have side effects (B5) |
| Over-seeds flora | O2 overshoots, fires break out | Balancing loops matter (A5, B4) |
| Builds warm machines on ground ice | Ground sinks and collapses under the machine | Real side effect of melting ground ice (B5, C2; Earth link: thawing permafrost, D2) |
| Stays out during a solar event | Radiation effect builds; shielding and depth protect | Thin air and no magnetic field let radiation through (A4) |
| Gets the balance right | Storms fade, water stays liquid, plants spread | The system is stable (B2) |

Natural consequences show **what** happened. They don't always show **why**, which is where the next layer comes in.

### 4.2 Specific feedback: "why did it happen?"

| Tool | When | What it says |
|---|---|---|
| **Planetary Monitor** | Any time | Current values, trend arrows, and which machines or events moved each value most in the last day. Turns "something changed" into "this caused it". |
| **Failure report** (BELL) | After a breach, machine failure, or fauna attack | What failed, the likely cause, one hint. Never the full answer. Hana's tip is attached the first time; it stops once the player has fixed that cause (fading). Hints get more specific after repeat failures of the same kind (adaptive feedback, TEAM.md section 3). Once the player's own fix holds, later reports of that type shrink to an outcome line (4.8). |
| **Forecast result** | Right after a forecast resolves | Whether the forecast was right, and a one- or two-sentence science explanation either way. |
| **Gate journal entry** | When a gate is reached | What the player just saw, the real science, a fiction note if needed, an "On Earth" line, and "read more". |

### 4.3 Self-assessment: "how am I doing?"

| Tool | How it works | Goals |
|---|---|---|
| **Keeper's Record** (Colony Record) | Header: *"What the colonists will need to know."* One page per goal, written in colony language (e.g. "Why water won't stay liquid here, and what would change that"). A page fills only from the player's **own** evidence: own forecast right, own "What caused it?" answer, own repair that held, own report answer. A separate **"Cairn team"** section shows group milestones (gates, storms survived, equilibrium). The player can see what's filled in and what's still blank. Page texts and triggers: `docs/learning/prompts-and-reflections.md` section 4. | All |
| **Forecast tally** | Shown in the journal as the player's own running tally per phase, so they see their own mental model improving. | B3 |
| **Keeper's Report** (phase debrief) | At each phase gate: what the team built, what went wrong and how it was fixed, the player's own forecast tally, and one reflection question. The player "sends" it to Earth; Mensah replies. | B, C, D |
| **"What caused it?" check** | Optional journal page: pick the cause of a recent event from a short list. Instant feedback. Lets players test themselves without consequences. | A1–A5, B1, C2 |

The Keeper's Record never gates progress (story rule 1; pillar "the planet is the quest log").

**Immediate statistics vs. After Action Review (Chapter 9).** During play, players get immediate statistics: Monitor values and trends, the next-gate bar, the forecast tally. At each phase gate the **Keeper's Report is the After Action Review**. Check against the AAR requirements:

| AAR requirement | Current Keeper's Report | Change |
|---|---|---|
| Shows the player's **own choices** | Shows what *the team* built and what went wrong | **Add a "Your shift" block** (own events only): machines you built, failures you claimed and fixed, forecasts you logged, times you checked the Monitor before a build |
| Shows **areas of strength** | Own forecast tally | **Add:** topics you forecast right; fixes that held ("Your seal repair held for 6 sols") |
| Shows **areas to work on** | Not shown | **Add, in positive terms:** topics you missed, each linked to the blank Keeper's Record page and the Monitor line that would have shown it ("Worth another look: what storms do to solar power"). Never "weak", "failed", or a grade |
| Group part separate | "What the team built" | Keep, labeled "Cairn team" (3.0) |
| Reflection | One question | Keep (prompts-and-reflections §2) |

The "Your shift" block reads from the same own-evidence data as the Record, so it adds no new tracking. **End-of-session mini-review (optional, classroom):** when the teacher ends a session, each player gets a three-line "Shift log" (one thing you did, one forecast result, one open job), at the pause, not in the last minutes of play (4.6 rule 5). Lead Game Designer to decide if it fits the story; it would replace nothing.

### 4.4 How players know they've achieved the outcomes

A player has met the outcomes when, in their own Keeper's Record:
- every page is filled in from their own evidence,
- their own Phase 3 forecast accuracy is at least 20 percentage points above their Phase 1 accuracy (the B3 criterion, 0.6), and
- their Keeper's Reports have been sent with an answer to each question.

**Reaching the beacon is the group's final proof.** The ending needs every variable in band at the same time for the active preset's Equilibrium hold time. A group can't get there by luck or grinding, only by understanding the system, so the game's win condition and the main learning outcome are the same thing. In a group, the beacon shows that the **group** understands the system; each player's own Keeper's Record shows what **they** have shown.

### 4.5 Feedback principles

- **Consequences first, explanation second.** Let the world respond, then offer the why.
- **Hints, not answers.** Failure reports point in a direction and leave the fix to the player.
- **Optional depth.** Journal entries are short, with "read more" for players who want it.
- **Fail forward.** Because gates lock in, mistakes cost time, not progress, which keeps failure safe enough to learn from.
- **Motivational and instructional, both.** Every feedback element is reviewed for both values (TEAM.md 8.4).
- **Fade process to outcome as skill grows; bring it back after errors.** Full map in 4.8.
- **Feedback for flow** (Chapter 6, coordinator's summary). Every failure report and forecast result must pass five checks:

| Check | Failure report rule | Forecast result rule |
|---|---|---|
| Linked to a goal the player cares about | Names the system at risk in colony terms ("the dome's air", "Outgasser 2"), not a game mechanic | Ties the result to the event the player is living through ("The storm hit; your panels…") |
| Meaningful | One likely cause, one hint (4.2) | One- or two-sentence explanation of *why* (4.8) |
| Timed appropriately | Immediate world response; report at the next natural pause (4.6) | First in the queue at the next natural pause (4.6) |
| **In positive terms** | The hint says what to try ("Seal the corners first"), never what the player did wrong ("You forgot…"). No "you failed" | Neutral wording for a miss: "The planet did X. Here's why." Never "Wrong!" or a buzzer. A hit gets plain acknowledgment, not hype. **Sound: one neutral result cue, the same for right and wrong** (Lead Artist audio direction; agreed): no buzzer, and in a classroom nobody hears that a teammate missed |
| Helps correct performance | The hint points to the next action or the Monitor line to check | A miss names the Monitor line that would have shown it |

These rules go into the wording rules for the Lead Game Designer (story bible §6.1 budgets) and the forecast spec (`docs/learning/prompts-and-reflections.md` §1).

### 4.6 When feedback may interrupt play (decided: option I-C)

**Status: decided** (user, 2026-11-26, option **I-C** in `docs/production/consensus-round2.md`; TEAM.md 8.6). Rules 1–11 below are the **Stage 3 test configuration**, with two flow guards that override my original wording of rule 3 and set rule 4's numbers as config:
1. **Report re-offers are bounded:** an unsent Keeper's Report is re-offered **at most once per session**, plus the "Since last shift" recap line, not at every natural pause. The teacher overview still shows unsent reports. After 2 deferrals (`interrupt.debriefMaxDefers`) it stops popping up and waits in the side panel. Delivery is a log line plus a side-panel entry, opened full screen by choice or at the next natural pause (5.5; PROCESS.md §4).
2. **Caps are config values:** pop-ups about 1 per 10 minutes; log lines about 3 per minute, merged when similar. Final numbers come from Stage 3 action-log counts (rule 10 logs every interruption, deferral, and expiry).

**Lead Game Designer's position (round 2):** agrees with T-C and with I-C, except rule 3, where they **can live with it** because the report is never modal in our UI. Their two additions, **both accepted**: (a) the gate sequence report → Mensah's reply → Shift Briefing counts as **one** pop-up; (b) **a forecast locks before any message that gives its answer away** (content rule CR-F9). Report delivery confirmed: one log line plus a side-panel entry, opened full screen only by the player's choice or at the next natural pause.

- **Rationale:** keeps every measurement safeguard (reports can't vanish; forecasts expire instead of being scored late; results arrive first) while bounding the reminders that most threaten flow immersion. Disputed numbers become config, settled by playtest evidence (TEAM.md §4).
- **Alternatives considered:** I-A (the six baseline rules only: open-ended report deferral would lose D1, D2, B5 evidence); I-B (all eleven rules with re-offers at every pause: more reminders than flow can bear).
- **Trade-offs:** learning: individual measures for D1, D2, B5, and B3 validity kept; appeal: at most one reminder per session per report; the paper fallback (5.5) lowers the stakes of an unsent report further.
- **Revisit if:** Stage 3 counts show unsent reports above about one in five per phase (then add a second re-offer at the gate's next natural pause), or players call reminders nagging. The Lead Game Designer's position was pending at decision time; a block on a specific rule goes to the user or is settled by the Stage 3 playtest.

My original review of each rule follows, kept as the record of the instructional view.

**Terms.** A **pop-up** takes over the screen or needs a click to dismiss. A **log line** is one line in the colony log or HUD that doesn't stop play. A **natural pause** is entering the dome, sleeping, opening the Monitor or journal, or reaching a gate.

| Draft rule (TEAM.md 8.6) | My position | Reason (learning view) |
|---|---|---|
| 1. Never interrupt during danger, combat, or active building | **Agree** | Interrupting during a task splits attention and adds extraneous cognitive load; the natural consequence is the first feedback anyway |
| 2. Non-urgent feedback only at natural pauses | **Agree**, with one addition: **forecast results get first place in the queue** at the next natural pause | Feedback works best close to the event it explains. The world's response is immediate; the explanation should come at the first pause, before other messages bury it |
| 3. Only blocking interruptions: first life-threatening warnings, and deferrable phase debriefs | **Agree on the warnings. Partly disagree on the debrief:** deferral should be limited, not open-ended | A Keeper's Report holds the reflection that D1, D2, B2, B5 are measured by. If it can be deferred forever, those individual measures go missing. Proposal: deferrable; re-offered at each natural pause and at the start of the next session ("Since last shift: report not sent"); teacher overview shows unsent reports. Still never blocks progress |
| 4. At most one non-urgent pop-up per ~10 min | **Agree for pop-ups; disagree if it covers log lines** | Log lines don't break flow immersion and carry the cause-and-effect information (B1, C2). Limit pop-ups, not log lines. Log lines get their own cap (proposed: no more than about 3 per minute, merged when similar) to avoid overload |
| 5. No new critical failure in the last ~5 min of a classroom session | **Agree, and extend it**: no new forecast and no Keeper's Report pop-up in that window either; they wait for the next session's "Since last shift" recap | A forecast that resolves after a pause is far from its outcome in real time; students forget their reasoning. A report written while packing up gets a weak answer |
| 6. Every prompt is deferrable except life-threatening ones | **Agree**, with a rule for forecasts: a deferred forecast **expires** when its outcome becomes visible. It's logged as unanswered, never as wrong | Answering after seeing the outcome isn't a prediction (B3 validity). Expiry with no penalty keeps it fair to players who were busy |

**Additions I propose**

| # | Rule | Reason |
|---|---|---|
| 7 | Forecasts are issued at least ~1 sol (of the active preset) before their outcome, so they can wait for a natural pause and still be answered in time | Makes rules 1, 2, and 6 compatible with B3 |
| 8 | Escalating hints (adaptive feedback) appear only at natural pauses, never mid-task | Consequences first; the player tries their own fix before the next hint |
| 9 | Nothing important exists only in a pop-up. Every message stays in the journal archive | No time-pressured reading (0.1); absent students can catch up |
| 10 | The action log records each interruption, deferral, and expiry | Lets us measure breaks in immersion (TEAM.md section 3) and check rules 3–6 in playtests |
| 11 | In a group, a forecast or report for one player never pauses the others | Shared planet: one student's prompt shouldn't stop the group |

**Where we differed, and how I-C settled it:** rule 3 (limited deferral) is now bounded to one re-offer per session; rule 4 caps pop-ups and log lines separately as config. I agree with I-C.

### 4.7 Is the breathable O2 window readable at middle school depth?

The Lead Game Designer flagged that at 75 kPa the breathable O2 window is narrow: about 21.5–23% (16 kPa of O2 needs ~21.3% at 75 kPa; 23% is the top of the fire-safety band).

**Assessment (design walkthrough, not a playtest):**

| Question | Finding |
|---|---|
| Is the science right and worth teaching? | Yes. It replaces a misconception risk ("21% = breathable") with the real rule, and it's a genuine B4 trade-off (too little: can't breathe; too much: fire). Accept |
| Can a 12-year-old *compute* it? | Partly. Percent of a quantity is middle school math, but "partial pressure" as a concept is high school chemistry. Asking MS players to multiply two Monitor numbers to judge the air adds cognitive load at exactly the moment the game is busiest (holds, drift events) |
| Can they *see* it? | Not on a 0–100% bar: a 1.5-point window is a sliver. Two separate numbers (O2 % and O2 partial pressure) that must both be right is hard to read under time pressure |
| Will it frustrate? | Risk. In classroom mode drift events come every 0.5–0.75 sol and a flora bloom adds ~0.6 %/sol (balance sheet §4, ×4 rate). The window could be crossed in well under a sol of real time; a group could lose its hold repeatedly without understanding why |

**Recommendation (concern, not a block):**
1. **One "Breathable air" gauge** on the Monitor for both bands: a zoomed bar that shows only the region around the window, with three zones in words and shapes, not only color: "not enough oxygen to breathe", "breathable", "fire risk". The two numbers sit underneath.
2. **MS band:** gauge plus the words; the partial-pressure number is in "read more". MS criterion for A5 stays "names one consequence of imbalance" and doesn't require partial pressure.
3. **HS band:** gauge plus both numbers and the derived value; the HS A5 criterion includes partial pressure (0.6).
4. **Trend arrow and a time-to-edge estimate** ("fire risk in ~0.4 sol at this rate") so the hold is managed by reading data (C3), not by reflex.
5. **Playtest check (Stage 2–3):** can MS-age testers say, unprompted, why the air stopped being breathable? If fewer than most can, widen the window in classroom mode by changing the *pressure* target, never by relaxing the 16 kPa rule (thresholds never scale), or add a forecast (F9) earlier.

Owner of the gauge: Lead Game Designer (UI spec) and Lead Artist (visual). I review the wording.

#### 4.7.1 Air window check (style guide §11.2): **accept, with concerns**

The Lead Artist's design (`docs/art/style-guide.md` §11.2) answers my four recommendations above: one place to read breathable air, words and shapes as well as color, partial pressure kept out of the MS main view, and a zoomed bar. The sliding lower edge, which widens the window as pressure rises, is the best part: it makes the B4 trade-off visible without arithmetic.

**Method: design walkthrough, not a playtest.** I walked the display as a 12-year-old at MS depth (playtest plan Lens A: reads short lines, doesn't connect a number to a cause unless shown), through five planet states.

| # | State | What Lens A sees | Reads it right? |
|---|---|---|---|
| 1 | Early Phase 3: 32 kPa, O2 2 % | Three crosses on the check row. O2 dot pinned at the bottom of a 15–27 % bar | Check row: yes ("not yet"). Gauge: **no**, the dot shows nothing below 15 % (concern 3) |
| 2 | 72 kPa, O2 22 % | "Thick enough" ✗, "Oxygen push" ✗. A thin "Breathe here" band (22.2–23 %) is drawn, and the dot sits just under it | **Mixed.** A breathe zone is drawn though the gate's pressure check fails (concern 2) |
| 3 | 80 kPa, O2 19 % | "Thick enough" ✓, "Oxygen push" ✗. Dot in the zone labeled **"Too thin to breathe"** | **No.** One readout says the air is thick enough; the next says it's too thin. A 12-year-old will raise pressure, which isn't the main fix here (concern 1) |
| 4 | 78 kPa, O2 22.5 %, CO2 low | Three checks ✓, helmet-off icon lit, dot in "Breathe here" | Yes, clearly |
| 5 | Flora bloom pushes O2 to 23.4 % | Dot moves into hatched flame zone | Yes, after the fact. No warning beforehand (concern 4) |

**Alignment.**

| Goal | Supported? | Note |
|---|---|---|
| A5 | **Yes, strong** | The three zones *are* the A5 criterion: name a consequence of imbalance (too little O2, fire) |
| B4 | **Yes, strong** | The sliding edge shows the trade-off: more pressure or tighter O2 |
| C3 | **Partly** | Shows state, not rate. Holding a band under drift events needs rate: add the trend arrow and time-to-edge estimate (my recommendation 4 above, not yet in §11.2) |
| A1 | **Weak** | Nothing compares with Earth. Optional fix: on the HS chart, an arrow at the right edge, "Earth, sea level: 101 kPa, 21 %" |

**Update after the Lead Game Designer's UI spec** (`docs/design/ui-specs.md` §5, written in parallel): concern 1 is **resolved** ("Too little oxygen"; I accept their wording over my "Not enough oxygen"), concern 2 is **resolved** (a states table drawn from the same condition, including "No safe oxygen level at this pressure. Raise pressure first."), concern 4 is **resolved** (time-to-edge estimate, "soon" / "in about a sol" at MS). Concern 3 is **half resolved**: the high end has an off-scale arrow, the low end doesn't yet; the Air window only appears in Phase 3 (progressive disclosure), but O2 is still far below 15 % then. Concerns 5 and 6 stay open.

**Concerns as raised** (none blocks; 1–3 should be fixed before the display is built in Stage 4):

| # | Concern | Recommended fix | Owner |
|---|---|---|---|
| 1 | **"Too thin to breathe" mixes up the two causes.** "Thin" means pressure everywhere else in the game ("Thick enough"; story bible "thin air"). Below the lower edge the problem is too little oxygen, often at a pressure that's already fine. This could teach the very confusion the display exists to prevent | Rename the zone **"Not enough oxygen"** (closed-helmet pictogram unchanged). Keep "thin" and "thick" for pressure only | Lead Artist, Lead Game Designer (wording); I review |
| 2 | **The zone and the gate can disagree.** Below 75 kPa a "Breathe here" band can be drawn while the gate's live check fails (state 2). Below about 69.6 kPa (16 ÷ 0.23) the lower edge passes 23 % and there is no breathable mix at all | Draw "Breathe here" from the **same live check the server uses**. Below 75 kPa: draw the band dashed, labeled "Needs more pressure". When the lower edge passes 23 %: "No breathable mix yet: needs more pressure". Never hard-code 21.5 %; compute the edge from 16 kPa ÷ pressure (21.3 % at 75 kPa; the balance sheet's 21.5 % is a tuning round-up) | Lead Artist (visual), Technical Lead (same check), Lead Game Designer |
| 3 | **Off-scale O2.** For most of the game O2 is far below 15 %, so the dot sits on the floor and says nothing | Below 15 %: an arrow at the bottom with the number ("O2 2 % ↓"). Show the zoomed Air window from Phase 3 (progressive disclosure, DESIGN.md); before that, a plain O2 % line | Lead Artist, Lead Game Designer |
| 4 | **No rate, so no warning.** In classroom mode a flora bloom adds ~0.6 %/sol; the band is ~1.7 points wide at 75 kPa | Trend arrow plus "fire risk in ~0.4 sol at this rate" (and "not enough oxygen in…") next to the gauge. This turns the hold into a data task (C3) instead of a reflex | Lead Game Designer (spec), Lead Artist |
| 5 | **MS reading load.** Check row + gauge = four readouts at once | Fine **if** the check row stays words and shapes only at MS (numbers on hover or in "read more") and the gauge has no numbers on its axis at MS except the current value | Lead Artist |
| 6 | **Fire band in percent at reduced pressure** | Not my call: the SME confirms whether 23 % is the right fire edge at 75 kPa (the OSHA band assumes sea level, accuracy-review.md) | SME |

**Playtest check (unchanged from 4.7, item 5):** can MS-age testers say, unprompted, why the air stopped being breathable? With the user as the only tester, this becomes a Lens A walkthrough until a real group test exists.

### 4.8 Process vs. outcome feedback, and the fading plan

**Principle.** Process feedback says *why* and *what to do differently*; outcome feedback says *how it went* (Earley et al., 1990). Novices need process feedback, but not too much at once (Wood, 2000). As skill grows, process feedback should fade toward outcome feedback, or it becomes redundant and adds load (expertise reversal again, Kalyuga et al., 2003). Our rules:
1. **New content gets process feedback.** Every new failure type, forecast topic, and gate starts with the cause and one hint or explanation.
2. **Familiar, mastered content gets outcome feedback.** Mastery is shown by the **player's own** evidence (own fix that held, own correct forecasts), never by the group's progress (3.0).
3. **Errors bring process feedback back.** A wrong forecast or a failure that recurs after the player's own fix re-opens the full explanation. Fading follows success; re-scaffolding follows failure.
4. **The phase sets the starting level; the player's evidence moves it.** So a new player joining a Phase 3 planet still gets process feedback for things new to them.
5. **Process detail never disappears, it moves.** Faded detail is one click away and kept in the journal archive (interruption rule 9).

**Map of every feedback type.** "Fade unit" says what the fading counts: per player unless stated.

| Feedback type | Kind | Goals | Phase 1 | Phase 2 | Phase 3 | Fades when | Comes back when |
|---|---|---|---|---|---|---|---|
| Natural consequences (world response) | Outcome | All | Always | Always | Always | Never | — |
| Colony log event line | Outcome | B1, C2 | Event only | Event only | Event only | Already minimal | — |
| Monitor values, trend arrows, next-gate progress | Outcome | C3, B3 | Always | Always | Always | Never | — |
| **Monitor cause breakdown** | **Process** | B1, C3 | Open by default; top 3 causes, plain labels, +/− per sol | Open; top 3 causes with numbers | **Closed by default** (one click); all causes; HS carbon flow | Phase default (UI hook confirmed: ui-specs §2) | Auto-opens for a value that leaves its band or pauses a hold. Every open is logged (C3 data-seeking evidence) |
| **Failure report** (per failure type) | **Process** | C2, B1 | Level 1: what failed, likely cause, one hint (+ Hana's tip the first time) | Same rules | Same rules | After **the player's own** fix of that cause held for the rest of the phase, the next one shows the **outcome form**: "Seal breach, Module 2. Cause on the Monitor." (≤ 12 words) | Recurs while unfixed: hint gets more specific (hint levels 2–3, adaptive feedback). Recurs after own fix: back to level 1 |
| Hana's tip | Process | C2, C1 | First time each failure type | Same | Last tip at Phase 3 start (story bible) | Stops once that cause is fixed (existing rule) | Doesn't return (her absence is part of the story) |
| **Forecast "look here" pointer** (part of the question: "Check: solar power on the Monitor") | Process (feed-forward) | B3, C3 | On every forecast | Only on new-topic forecasts (F5) | None | Phase default | Player's accuracy in the current phase is below 1 in 3 after 3 forecasts: pointers return for that phase. Logged as `scaffold: pointer`, so the B3 analysis can separate scaffolded answers |
| **Forecast result** | Outcome (right or wrong) **+** process (explanation) | B3 + item goal | Right or wrong: full explanation (≤ 40 words) | Right: one-line "why" (≤ 15 words), full text one click. Wrong: full explanation + the Monitor line to check | Right: tally update, "why" one click. Wrong: full explanation | Correct answers in later phases | Any wrong answer gets the full explanation. **Interleaved forecasts (FI) always get the full explanation**, right or wrong, because the lesson is *which* idea applied |
| HS "why" follow-up | Process prompt (self-explanation) | Item goal | All | All | All | No fade (it is the HS depth) | — |
| "What caused it?" result | Process → outcome | B1, C2, A1–A5 | Correct cause + the Monitor line that shows it | Right: "Right." + line one click. Wrong: cause + line | Same as Phase 2 | Phase default | Wrong answer: full |
| Gate journal entry | Process (explanation) | A1–A5, D2 | Full | Full | Full | Never: each gate is new content | — |
| Shift Briefing | Feed-forward (organizer) | By phase | Says where to look | Names values only | Names values only | Phase (2.1) | — |
| Worked example (Hana's notes) | Process (worked example) | B1, C3 | Full notes on the first Outgasser | Completion version on the first processor; one line on later machines | — | Per player, by machine type (2.2) | Notes stay in the archive |
| Drift event alert (**per planet**) | Outcome, with a cause name the first time | B4, C2, C3 | — | — | First of each type on the planet: names the event and value ("Flora bloom: O2 climbing"), no fix. Later: value only ("O2 climbing") | Second occurrence of the type | A drift event resets a hold: the next one of that type names its cause again |
| Air window and air check row | Outcome with a built-in process cue (the failing check names what to fix) | A5, B4, C3 | — | — | Always, with trend and time-to-edge (4.7.1) | Never | — |
| Keeper's Report summary (built, failed, fixed, own tally) | Outcome | B3, C2 | Report 1 | Report 2 | Report 3, Final | Never | — |
| MS sentence starters on reflection questions | Process scaffold | C2, D1, B5, B4 | Report 1: shown | Report 2: shown | Report 3 and Final: behind a "Need a start?" button (logged) | By report | The button is always there |
| Mensah's reply | Process (Earth explanation) | D2 | Each report | Each report | Each report | Never: new content | — |
| Forecast tally; Keeper's Record pages | Outcome | B3; all | Always | Always | Always | Never | — |
| Teacher debriefs (classroom) | Process, outside the game | By debrief | Period 6 | Period 14 | Period 21 | — | — |

**What this changes in other documents (handoffs):** the Monitor's default view per phase (Lead Game Designer, Lead Artist); per-player rendering of failure report level and forecast pointer state (Technical Lead: failure reports are planet events, so each player's view must read that player's own fix history. If that's costly, fallback: the planet's history, i.e. "fixed by anyone and held"; I can live with the fallback because the fixer's own evidence still lands in C2); two new fields in the shared feedback format, `feedback_kind` (process / outcome / feed-forward) and `fade_rule` (Producer owns DOCUMENTATION.md 5.1).

**Effect on both goals.** Learning: process feedback where it helps, less load where it doesn't, and wrong answers always explained. Appeal: messages get shorter and rarer as players get better, which supports flow immersion and the interruption rules; experienced players aren't lectured.

**How we'll know (Stage 3 and Stage 5 playtests):** a Lens B player shouldn't call any message "obvious" more than once a session; a Lens A player shouldn't hit an outcome-only message for something they haven't fixed themselves. Action log: count of `scaffold` returns per player per phase (should fall).


### 4.9 Motivation, rewards, and player types (Chapter 6 lens)

Research summaries below come from the coordinator's Chapter 6 brief (Deci, 1995; Pink, 2009; Bartle; Koster, *A Theory of Fun*); I have not read the sources this session (**unverified** beyond that brief). The Lead Game Designer maps the gameplay side in DESIGN.md; this is the instructional side.

**Rewards: baseline acknowledgment and information, not control.** If-then rewards used to control behavior can reduce creativity and performance (Deci, 1995). Baseline acknowledgment is expected as fair; extra tangible rewards on top can harm intrinsic motivation (Pink, 2009).

| Element | Works as | Check | Verdict |
|---|---|---|---|
| Keeper's Record | Informational: shows what the player has shown, in colony language | No points, no levels, no "unlocked!" fanfare; never gates progress (4.3) | **Pass**, with two page fixes below |
| Keeper's Reports | Baseline acknowledgment: Mensah replies to the planet's data | Reply never depends on answer length or "good" answers; never blocks progress. In class, the *teacher's* expectation of an answer is outside the game | **Pass** |
| Ending cards | Informational: describe the planet as left | Unranked, no score, no "good ending" (DESIGN.md) | **Pass** |
| Forecast tally | Informational: own accuracy over time | No reward for a high tally beyond the Record page (B3) | **Pass** |
| Achievement system | — | None at launch (current decision) | **Agree**; if one is added later, it must reward skill shown (as below), never counts of activity |

**Reward player skill, not activity.** I checked every Keeper's Record trigger (`docs/learning/prompts-and-reflections.md` §4) for "click yes to every quest" compulsion:

| Page | Trigger | Rewards skill? | Change |
|---|---|---|---|
| A1–A5, C1 | Correct forecasts or checks | Yes | — |
| B1 | 3 correct "What caused it?" checks | Mostly; guessing on 3–4 choices can fill it | **Only the first attempt** per event counts |
| B3, C2 | Own accuracy rises; own fix holds | Yes | — |
| **D2** | "...or opens 3 'On Earth' read-more pages" | **No.** Opening pages is activity | **Removed.** D2 fills from a written answer only (final question, or an Earth-link answer in a report) |
| B2 (MS), B5, D1, B4 | Answers a report question | Partly: writing takes effort, but a one-word answer would count | **Needs at least one sentence (≥ 8 words)**; quality is still the teacher's call (rubrics) |
| C3 | Monitor opened before half of own major builds | Partly: reflexive opening could game it | Keep (it is a habit we want), but the C3 criterion also needs a data reason (0.6); playtest watches for reflexive opening (open time under ~1 s) |

**Zeigarnik effect: the next-gate display.** Unfinished goals stay on the mind. The Monitor's "Next: pressure 5 kPa, 62 %" display (DESIGN.md, gap owned by the Lead Game Designer) supports **C3** (reading data toward a target), **B1** (which causes move the bar), **A1** and **A2** (the gate values are real Mars and water thresholds), and **B4** in Phase 3 (hold progress). Condition: it must show the **variable and the real threshold**, not only a percent, or it becomes a progress bar that teaches nothing.

**Experiential design and player types: who falls through the cracks?**

| Experience channel | Where we have it | Thin for |
|---|---|---|
| Sensory | Sky, fog, ice, pressure-driven sound | Players with low vision or hearing (covered by numbers, patterns, subtitles) |
| Cognitive | Forecasts, Monitor, "What caused it?" | — |
| Active | Building, repairs, holds | — |
| Affective | Thawlings, helmet-off, crew story | Players who skip story (most essential science doesn't depend on story) |
| Relational | Group planets, colony posts, Shift Board, crew messages | **Solo home players**: crew messages are the only relationship |

| Bartle type | What draws them | Goals they reach naturally | Risk |
|---|---|---|---|
| Achievers | Gates, Record, beacon | A1–A5, B3, B4 | Rush gates and skip journal; the faded, short feedback suits them |
| Explorers | "Read more", Basu's sites, the ice experiment | A2, D2, B1 | Fine |
| Socializers | Group planets, posts | B4, C2 (group) | **Weak in solo play** |
| Killers | Fauna defense | C2 | **May stay in lethal defense**, which never lowers fauna pressure. That's the B2 lesson by consequence, but their D1 thinking may stay shallow. Report 2 asks every player the D1 question |

**Is any goal reachable through only one channel?** **D1, B5, and D2 are measured only by writing** (reports, transfer task). Every player type must write, which is fair, but it disadvantages weak writers. Fix: the educator guide allows an **oral answer** to the same question, scored by the teacher with the same rubric (accessibility). No goal depends on one Bartle type.

**Koster's lens on cognitive load.** Fun is the brain enjoying learning patterns; noise is patterns we can't yet read, boredom is patterns with nothing left to teach.

| Risk | Where | Our control |
|---|---|---|
| **Noise** (too much at once) | **Phase 3 start**: O2, CO2, biomass, the Air window, and drift events arrive together | Shift Briefing names two values only (2.1); Air window shown from Phase 3; **recommend drift events start only after the first flora has taken root**, not at the Phase 3 gate (Lead Game Designer) |
| Noise | Third-party machine GUIs | Learning-critical machines are ours and simple (DESIGN.md gap, Lead Game Designer) |
| **Boredom** (nothing new) | Long holds; Index-limited waits in Phase 2 (~2 h in class, balance sheet §5.3) | Drift events and beacon parts during holds; interleaved forecasts (2.3) give old patterns a new twist; Phase 2 waiting stays a watch item |
| Boredom | Experts reading guidance they don't need | Skippable worked example (2.2); fading (4.8) |


### 4.10 Difficulty as information (PROCESS.md §5)

The idea: gentle settings point at the problem and explain why something is blocked; hard settings tell you nothing and let bad work come back. Checked against our settings:

| Setting | What it changes today | As information difficulty | Verdict |
|---|---|---|---|
| **Grade band** (MS / HS) | Depth of science: "why" follow-ups, partial pressure, read-more numbers, sentence starters | Not a difficulty setting. Both bands get the **same** amount of pointing at problems; HS gets *more* science, not *less* help | **Keep it that way.** Don't let the band quietly become "HS = fewer hints" |
| **Classroom mode** | Rates, holds, recipe cost, reduced hazard severity | **Gentle on information**, which is right for class: blocks are explained, the next gate shows what's missing | Keep. Add: every locked recipe or machine shows one quiet line, "Unlocks at Phase 2: pressure 5 kPa" (PROCESS.md §4, "only offer actions the player can do now"), which is also A1/C3 information |
| **Fading** (4.8) | Per player, by own evidence | This *is* information difficulty, earned: help shrinks as skill shows, and returns after errors | Keep; it adapts per player instead of per setting |
| **`guidance` setting: decided (user, round 3), with the four conditions in this row** (DESIGN.md: gentle / standard / expert; thresholds and rates never change) | Gentle: points at problems, explains blocks, "Useful now", one quiet unlock line. Standard: hints only after repeat failures. Expert: no hints, no "Useful now", no worked example | Exactly the "difficulty as information" idea, and it replaces my "Veteran" suggestion | **Agree, with four conditions:** (1) **at every level** a failure report still names the cause, forecast results still explain, and safety warnings fire (information difficulty may shorten, never remove these); (2) **gentle is the classroom default and expert is not offered in classroom mode** (equity, and C2 evidence would no longer be comparable); a teacher may pick standard for a planet; (3) fading (4.8) still runs inside gentle; gentle isn't "never fade"; (4) the level is logged and exported (`guidance` in `planets.csv` and `players.csv`), since it changes the conditions of C2 and B3 evidence. Grade bands stay separate: band = science depth, guidance = amount of help |

**Guidance decision rationale.** Rationale: difficulty as information serves hobbyist experts (appeal) without changing the science. Alternatives: no setting (experts get guidance they don't need: expertise reversal, boredom); a "Veteran" setting in standard mode only (now deferred until after Stage 3, covered by expert). Trade-offs: learning: novices and classes stay on gentle; measures still comparable because the level is logged. Appeal: experts can turn help off. Revisit if Stage 3 shows gentle players feeling hand-held, or expert players missing causes.

Rule we keep: **information difficulty never removes a forecast result, a failure report's cause, or a safety warning.** It can only shorten them.

---

## 5. Classroom Mode

A config preset a teacher turns on for each group planet. It keeps the same game and the same learning goals, compressed to fit a school unit.

### 5.1 What changes

**Rule: classroom and multiplayer settings never change physical thresholds.** Gate thresholds, the triple point of water, and breathability stay at their real values in every mode. Pacing is compressed only through **rates** (machine output and natural rates) and **hold times**. Scaling a threshold would show students numbers that contradict the science in the journal, and could make lethal air count as breathable (SME finding 3; consistency review M-02). Values below come from DESIGN.md "Classroom Pacing" and the balance sheet §4–5; they are untested starting values.

| Setting | Standard | Classroom mode |
|---|---|---|
| Target playtime | ~40 hours | **~11–12 hours**: 17 play periods of ~40 min ≈ 11.3 h |
| Players per planet | 1–6 | **3–5 students per group planet**; a class runs several planets |
| Group size setting | 1 (solo) | **Static**, set by the teacher per planet (3–5). Not read from who is online, so an absent student doesn't change the planet's rates |
| Physical thresholds (gates, water, breathability) | Real values | **Same real values** (never scaled) |
| Rate multiplier (all Index contributions) | 1.0 | **4.0** (balance sheet §5; tune in prototype) |
| Dome stable / Breathable / Equilibrium hold | 3 / 7 / 14 sols | **1 / 2 / 3 sols**. Breathable and Equilibrium run at the same time. A hold pauses when a value leaves the band and resets only after 1 sol out (classroom 0.5 sol) |
| Drift events during holds and Phase 3 | Every 1–1.5 sols | Every 0.5–0.75 sol (a 3-sol hold has 4–6) |
| Hazard severity | Full | Reduced: failures still happen, but recover faster |
| Recipe cost | Standard recipes | Cheaper recipes (KubeJS preset) |
| Grade band | Chosen by each player | Set by the teacher for each group planet |
| Forecasts and Keeper's Reports | On; reflection can be skipped | On, same set; teacher expects a written reflection (it still never blocks progress) |
| Teacher-led debriefs | — | Three class periods (5.2) |
| Session save points | — | Teacher ends the session; the world pauses (simulation and hazards stop) until the next class. Sols for holds must count simulation time so they survive a pause (Technical Lead, M-05) |
| Teacher tools | Operator commands only | Enabled (5.3) |
| Subtitles | Player's choice | **On by default** in the classroom pack (Lead Artist proposal, agreed): noisy rooms, shared or missing headphones, deaf and hard-of-hearing students. Subtitles are short and don't count toward the log-line cap |
| Guidance (4.10) | Player's choice (gentle / standard / expert) | **Gentle** by default; teacher may set standard; expert not offered |

Every gate, hazard, and learning moment is still there. Classroom mode makes them come sooner, not disappear.

### 5.2 Suggested unit plan (23 class periods, 17 play periods)

Phase play split 4 / 7 / 6 periods, from the Lead Game Designer's classroom time model (~3.6 / 7.7 / 5.6 periods for a group of 4; balance sheet §5.3). Phase labels are expected, not fixed: Phase 2 will often spill into the start of Phase 3's periods, and the debriefs work either way.

| Periods | Activity | Gameplay phase | Main goals |
|---|---|---|---|
| 1 | Pre-test; intro to Mars; form groups of 3–5; colony posts and the Shift Board; controls | — | — |
| 2–5 | Play: survive in the dome, first dust storm, first solar event, Regolith Outgassers, ice that vanishes | Phase 1 | A1, A2, A4, C1, C2, C3 |
| 6 | Debrief 1: Mars vs. Earth data, pressure and water | — | A1, A2, C1 |
| 7–13 | Play: processors, emitters, thaw, fauna and fauna options, ground collapse, expansion | Phase 2 | A2, A3, B1, B2, B5 |
| 14 | Debrief 2: greenhouse effect with Earth's CO2 and temperature graphs; feedback loops; unintended consequences; "Was waking them worth it?" | — | A3, B2, B5, D1, D2 |
| 15–20 | Play: biome seeding, O2/CO2 balance, equilibrium, beacon | Phase 3 | A5, B4, B3 |
| 21 | Debrief 3: carbon cycle, stability and change, evaluating a claim about stable ecosystems | — | A5, B4, D2 |
| 22 | Transfer task: Earth's climate system | — | B1, B2, D2 |
| 23 | Post-test and reflection discussion | — | All |
| (+~4 weeks) | Optional delayed post-test (3.6) | — | A2, A3, A5, B1, B2, B4, B5, D2 |

Period-by-period teacher detail, debrief questions, and shortened-unit options: [docs/learning/educator-guide.md](docs/learning/educator-guide.md).

**Groups move at different speeds.** Each group is on its own planet, so not every group reaches a gate before its debrief period. Debriefs are built to work anyway: they discuss what groups have seen so far, using the teacher overview to pick examples. A group that's far behind can get a higher rate on its own planet (teacher tool); a group that's ahead keeps playing ("read more" pages and optional story beats are the extension). Keeper's Reports fire at gates in the game, so for some groups the report arrives before the class debrief and for others after. Both are fine.

**Shorter units.** "Set phase" lets a teacher start at Phase 2 for a shorter unit, but the goals taught only in Phase 1 (A4, C1, much of A1) then need class time instead. The educator guide lists which goals each shortcut drops.

### 5.3 Teacher tools

| Tool | What it does |
|---|---|
| Class overview | Every group planet in one view: each planet's phase and gates; each student's (by name on the server, by ID in exports) Keeper's Record pages, forecast tally, and reports sent. Individual and group evidence shown separately (3.0) |
| Pause / resume world | Freezes the simulation, hazards, and fauna between classes, per planet or all at once |
| Pacing per planet | Raise or lower one planet's rate multiplier to keep groups near the unit plan. Never changes thresholds |
| Rollback | Restore a save from the start of a session (e.g. after griefing or a bad crash). The action log must survive the rollback (Technical Lead, M-06) |
| Set phase | Start a planet at a later phase if the unit is shortened. The overview marks the goals skipped |
| Export log | Writes the local action log to CSV files with pseudonymous IDs only (5.4, 5.5) |
| Grade band | Set middle or high school depth for each planet |

### 5.4 Data and privacy

**In one line: pseudonymous on the server, anonymous in every export.** (Wording agreed with ARCHITECTURE.md §8.1; "pseudonymous" as defined in TEAM.md section 3.)

- The action log stays **on the server's computer**. Nothing is sent over the internet.
- Each player gets a **random per-world pseudonymous ID** (`P-XXXXX`, e.g. `P-7K3QX`) the first time they join, before anything is logged. It is never derived from the Minecraft account. The action log never contains Minecraft account IDs, usernames, chat, or IP addresses.
- The **mapping file** (ID ↔ Minecraft account) stays on the server's computer. It is **never exported**, never backed up with the world, and never sent to a client. The teacher sees ID ↔ name only in the in-game class overview or with a teacher command, on the server.
- **Every export uses pseudonymous IDs only. There is no option to export usernames** (user decision, 2026-11-26). Outside the server's computer, the data can't be linked to a student without the mapping file, so exports are anonymous.
- At the end of a unit the teacher can delete the mapping (one student or all). After that the logs are anonymous on the server too. The educator guide recommends doing this.
- No names, emails, ages, or other personal data are collected by the pack.
- **Free-text answers** (Keeper's Report reflections) can contain anything a student typed, including their own name. They go in a separate `reflections.csv`: on by default in classroom mode, off in standard mode, and the teacher can switch it off (user decision, 2026-11-26; details in 5.5).
- The educator guide carries a plain-language data notice for students and families. Legal classification (COPPA, FERPA, GDPR) is **unverified**: no legal review yet (risk R-20).

### 5.5 What teachers need from the data (final CSV columns)

The teacher needs to answer three questions: *Where is each group? What has each student shown? Who needs help?* The export serves those. This is the final column list for consistency review M-08, using the file names in ARCHITECTURE.md §8.6. Phase columns use `_p1`, `_p2`, `_p3`. Goal columns repeat for each goal ID A1–D2.

| File | One row per | Columns |
|---|---|---|
| `players.csv` | Student (pseudonymous ID) | player_id, planet, band, group_size, sessions_present, sols_played, post_p1, post_p2, post_p3; forecasts_answered_p1..p3, forecasts_correct_p1..p3, forecast_accuracy_p1..p3, forecasts_expired; b3_criterion_met (yes/no/not enough data); explain_correct, explain_attempted; failures_claimed, failures_fixed, repeat_failures_after_own_fix; major_builds, monitor_checks_before_builds; drift_events_answered; report_1, report_2, report_3, report_final (submitted yes/no); forecasts_interleaved_answered, forecasts_interleaved_correct (2.3); forecasts_scaffolded (pointer returned, 4.8); worked_example (used / collapsed / skipped_by_doing, 2.2); briefings_opened; for each goal: `<goal>_shown` (yes/no), `<goal>_sol`, `<goal>_evidence` (event type that filled it) |
| `events.csv` | Log event | timestamp, session, planet, timeline, superseded (yes/no), sol, player_id (empty for planet events), scope (player/planet), band, event_type, cause, prompt_id, result (correct/incorrect/expired/late/none), goal_ids, ngss_codes, detail columns flattened from `data` |
| `planets.csv` | Group planet | planet, preset, group_size, band, current_phase; for each gate: reached (yes/no), sol, session; storms_survived, core_failures_per_storm, repeat_failures_p1..p3, drift_events (count, answered), hold_progress, fauna options in use (shrouds, lures, quiet zones, defenses), thawling population stage, rollbacks |
| `goals.csv` | Goal ID | goal_id, record_page_title (colony language), ngss_ms, ngss_hs, practice_only (yes/no), priority_tier, scope. Generated from `ngss_map.json`, so teachers see codes without repeating them in every row |
| `reflections.csv` | Written answer | player_id, planet, band, report_id, goal_ids, sol, text, d1_score, d2_score, b5_score (rubric columns left blank for the teacher; rubrics in `docs/learning/rubrics.md`) |

**Technical Lead additions (ARCHITECTURE.md §8.6, round 2): all accepted.** `forecasts_late` in `players.csv` (late answers are a signal of prompts arriving mid-task; not scored); `void` as a forecast result (never happened, skipped by "set phase", or rolled back; not scored and not counted as expired); `names_replaced` in `reflections.csv` (tells the teacher to read that answer with care). My round-2 additions for the Technical Lead to add: `forecasts_interleaved_answered`, `forecasts_interleaved_correct`, `forecasts_scaffolded`, `worked_example`, `briefings_opened` (players.csv), and the "needs a look" flags (3.7) in the class overview, not the export.

**Technical Lead round-3 columns: all accepted.** `forecasts_replay` (players.csv: forecasts re-issued after a rollback; shown, never scored); `guidance_changes` (planets.csv: how often the level changed mid-unit, so evidence can be read under the right conditions); `third_party_mods` (planets.csv: whether Mekanism and Create were on; context only). Printable ID slips carry **IDs only**; the teacher writes names by hand if needed, and the slips stay in the classroom.

**Panel dust and C1 evidence (decided by me, BACKLOG #26; DOCUMENTATION.md §4).**
- **Decision:** "cleaned before a forecast storm" **counts as formative C1 evidence**, under a strict rule: the player's **own** `panel.cleaned` event, on a panel at **stage ≥ 2** (output ≤ 82 %), **while a storm warning is active** (between `log.storm.warn` and storm onset). It can fill the C1 Keeper's Record page in place of "placed or refueled life support before the first storm", always **together with** a correct F1 (prompts-and-reflections §4). New export column `panels_cleaned_before_storm` (players.csv; count of qualifying cleanings). Not graded (3.0).
- **Rationale:** C1 is planning for limited resources before a threat. Cleaning a dusty panel when a storm is announced protects the power budget the dome will need, which is exactly that behavior. The stage and warning conditions make it a *decision*, not a habit.
- **Why not count every cleaning:** cleaning clean panels, or cleaning with no storm coming, would reward activity for its own sake (4.9; CR-R2). A player who brushes every panel every sol would fill the page without planning anything.
- **Why not B5:** cleaning shows no understanding of side effects. B5 stays measured by Report 2.
- **Why not C2:** panel dust isn't a failure, it recurs by design. Counting it as C2 would inflate repeat-failure counts.
- **Alternatives considered:** (a) no dust evidence (wastes the clearest planning behavior in Phase 1); (b) any cleaning (rewards activity); (c) automatic wiper use counts (it's a one-time build, not planning before a storm).
- **Trade-offs:** learning: one more individual, attributable C1 signal in Phase 1, where C1 evidence is thin. Appeal: none; players are never told cleaning "scores".
- **Revisit if** playtests show players cleaning only because the page tells them to (then remove the page trigger and keep the export count), or the wiper makes manual cleaning rare (then C1 stays on F1 and life support).

**Tuning numbers I own** (data, not code; starting values, to be checked against Stage 3 action-log counts):

| Key | Value | Meaning |
|---|---|---|
| `learning.monitorCheckWindowSols` | **0.25 sol** (accept the default) | Monitor viewed by the same player within this window before a major build counts for C3 |
| Monitor minimum view (new key, Technical Lead) | **1 s** | Shorter views don't count (gaming guard, 3.10) |
| `fade.retire_tip_after_own_fixes` | **1** | Hana's tip for a cause stops after the player's first own fix of it |
| `fade.escalate_hint_after_repeats` | **[2, 4]** | Hint level 2 at the 2nd occurrence of an unfixed cause; level 3 at the 4th. Not faster: the player should try their own fix first (productive failure) |
| Outcome form | after **1** own fix with no repeat of that cause on that machine in the phase | 4.8 |
| Re-scaffold | back to **level 1** on any repeat after the player's own fix | 4.8 |
| Forecast pointer return | accuracy **below 1 in 3 after 3** forecasts in the phase | 4.8 |
| `interrupt.debriefMaxDefers` | **2** | After 2 deferrals the report stops being offered as a pop-up; it stays in the side panel, the "Since last shift" line still names it, and the teacher overview flags it. It never blocks |
| `forecast.lateWindowSols` | 0.05 (accept) | — |

**Model-based assessment** (ARCHITECTURE.md §11a.3): **confirmed out of scope** for launch (3.9). The event log keeps enough for an offline model later.

**Keeper's Report delivery vs. "no modal pop-ups for progress"** (PROCESS.md §4; Producer's proposal): **accept.** A report is offered as one colony-log line plus an entry in the side panel; it opens full screen only when the player chooses it or at the next natural pause (gate reached counts). This keeps every measure (the report still can't vanish: re-offer once per session, the recap line, the side panel, the teacher flag) and removes the only blocking progress screen. Shift Briefings 2 and 3, which follow the report screen, then follow it whenever it is opened. Lead Game Designer to confirm in the UI spec.

`goals.csv` is new compared with ARCHITECTURE.md §8.6 (handoff to the Technical Lead; now adopted there). NGSS codes come from `data/mars/learning/ngss_map.json`; an example in that format is in [docs/learning/ngss_map.example.json](docs/learning/ngss_map.example.json), now with `tier`, `scope` (always `player`), and `record_title_key` for each goal.

**Reflections export: decision (user, 2026-11-26).**

| Part | Rule |
|---|---|
| File | Reflections go in their own `reflections.csv`, never mixed into the other files |
| Default | **On** in classroom mode; **off** in standard mode (home play has no teacher). The teacher can switch it off (`export.includeReflections`, ARCHITECTURE.md §8.6) |
| Names scrubbed | At export, any current player name on that planet found in the text is replaced with that player's ID (Technical Lead implements; exact matching rules are theirs) |
| Note to students | Next to every reflection question, in the game: "This goes in your report. Don't write your name or personal details." |
| Paper fallback | If the file is switched off, or the school doesn't allow free text in exports, the educator guide gives the same questions as a paper worksheet with sentence starters (educator guide §6), so D1, D2, and B5 can still be scored |

- **Rationale:** D1, D2, and B5 are measured **only** by written answers (0.6). Without the text a teacher can't score them, and two of the three goals have no other individual measure. Free text is the one place personal details can slip in, so it is separated, switchable, scrubbed, and warned about.
- **Alternatives considered:** (a) no free text in any export: safest, but loses the only D1/D2/B5 evidence unless every class uses paper; (b) free text inside `players.csv`: simpler, but can't be switched off without losing the other columns; (c) off by default everywhere: protects privacy, but most teachers would never turn it on and the rubric-scored goals would go unmeasured.
- **Trade-offs:** Learning: keeps the only individual measures of D1, D2, B5. Appeal: none for players; one more file and one setting for teachers. Privacy: residual risk that a student writes another identifying detail (a nickname, a school name) that name-scrubbing can't catch; the in-game note and teacher review reduce it.
- **Revisit if:** a legal or school data-protection review (risk R-20) says free text can't be exported at all (then paper becomes the default), or playtests show students write names anyway.

**Keeper's Record scope per goal** (the `"scope"` field in ARCHITECTURE.md §8.5): **every goal page A1–D2 is `"player"` scope.** No group achievement ever fills a player's goal page (section 3.0). Planet-scope events (gates, first storm survived, breathable held, equilibrium, beacon) are shown in the record's separate "Cairn team" section and logged with `scope: planet`, but they never set `<goal>_shown`.

### 5.6 LMS integration (after release, opt-in)

Decided 2026-11-26: no LMS integration before release. After release it is **opt-in, off by default**. Learning needs, for the Technical Lead to design against later:
- Teachers want the same three answers as 5.5, inside their school's LMS, ideally as per-goal status and the rubric-scored reflections.
- Only goal-level summaries need to travel; raw action logs never do.
- **Open conflict to solve before building it:** passing results into an LMS gradebook needs to know which student is which. That conflicts with "nothing personal leaves the server's computer" unless the ID-to-student match happens on the school's side (e.g. the teacher imports the pseudonymous-ID file and matches it locally). This is an ill-structured problem for after release; it is not decided here. The Technical Lead's planned path (ARCHITECTURE.md §11: xAPI file export first, pseudonymous IDs only, no free text by default) fits these needs; reflections should stay out of any LMS export unless a school opts in separately.

---

## 6. NGSS Alignment

Mapped to NGSS performance expectations (PEs), with the science practices (SEPs) and crosscutting concepts (CCCs) the game exercises. The SME reviewed every code on 2026-11-26 and, in round 3, **checked all of them word for word on nextgenscience.org** ([docs/science/ngss-verification.md](docs/science/ngss-verification.md), "Round 3"). Two assessment boundaries matter for how we use them: MS-ESS1-3 excludes recall (so A1 is a data task), and HS-ESS3-6 is limited to using published model results (so the debrief's published Earth data carries it, not the game).

### 6.1 Performance expectations

| Goal | Middle school (6–8) | High school (9–12) |
|---|---|---|
| A1 Mars vs. Earth | MS-ESS1-3 (assessed with data, not recall) | — (background knowledge) |
| A2 Liquid water | MS-PS1-4 (DCI PS1.A: state changes with temperature or pressure) | Extends MS-PS1-4. Related: HS-ESS2-5 (only if water–regolith effects are shown in game) |
| A3 Greenhouse effect | MS-ESS3-5 (via the Earth debrief) | HS-ESS2-4 |
| A4 Radiation | — (introduce as a concept) | HS-PS4-4 (EM radiation only; magnetic shielding is beyond this PE) |
| A5 CO2/O2 exchange | MS-LS1-6 | HS-LS2-5; HS-ESS2-6 (needs carbon reservoirs on the Monitor) |
| B1 / B2 Systems and feedback | MS-LS2-4 (B1) | HS-ESS2-2 |
| B3 Prediction from models | — (practice, see SEPs) | HS-ESS3-6 (via published Earth data in debriefs; the PE's boundary limits it to published model results) |
| B4 / B5 Trade-offs and consequences | MS-ETS1-2; related for B5: MS-ESS3-3 (only with an Earth side-effect example) | HS-ETS1-3; HS-LS2-6 (stability and change) |
| C1 Resource planning | MS-ETS1-1, MS-ETS1-2 | HS-ETS1-1 (only when paired with an Earth challenge in the transfer task or a debrief), HS-ETS1-2 |
| C2 Design, test, improve | MS-ETS1-4; MS-ETS1-3 (only if failure reports let players compare fixes, else related) | HS-ETS1-2, HS-ETS1-4 (moderate). Not HS-ETS1-3, which belongs to B4/B5 (SME round 2) |
| C3 Data use | — (practice only: Analyzing and interpreting data) | — (practice only) |
| D1 Ethics | — (no PE; NGSS PEs don't assess values. Practice: Engaging in argument from evidence) | — (same) |
| D2 Link to Earth | MS-ESS3-5 | HS-ESS2-4, HS-ESS3-6 |

**Conditions the SME attached to these mappings** (each one is tracked in the alignment table):
- MS-ESS3-5 and the D2 codes count only if the Phase 2 debrief uses Earth's CO2 and temperature data (unit plan period 14; `docs/learning/prompts-and-reflections.md` section 3).
- HS-ESS2-6 needs the Monitor to show carbon per reservoir at high school depth (Lead Game Designer).
- HS-LS2-6 needs a claim-evaluation question in the Phase 3 debrief (now in Keeper's Report 3, HS variant).
- HS-ESS2-2 would be a near-exact fit with an ice–albedo feedback in the game (optional; Lead Game Designer).
- MS-ESS3-3 for B5: wording now verified (SME round 3); listed as **related**, not aligned, because it fits only with an Earth side-effect example (cta.md §4).

### 6.2 Science and engineering practices (SEPs)

| Practice | Where in the game |
|---|---|
| Asking questions and defining problems | Phase 2 debrief questions about Earth's warming data; defining the constraints of the dome (C1) |
| Developing and using models | The Planetary Index is a model; forecasts ask players to use it |
| Planning and carrying out investigations | Testing machines and observing results |
| Analyzing and interpreting data | Planetary Monitor values and trend graphs; A1 data task |
| Using mathematics and computational thinking | Rates, thresholds, balancing O2/CO2 (high school depth) |
| Constructing explanations and designing solutions | Failure reports, dome and machine design |
| Engaging in argument from evidence | Keeper's Report reflections, terraforming ethics discussion |

### 6.3 Crosscutting concepts (CCCs)

| Concept | Where in the game |
|---|---|
| Patterns | Monitor trends; forecast results over a phase |
| Cause and effect | Every Planetary Index response |
| Scale, proportion, and quantity | Mars vs. Earth values; rates per day |
| Systems and system models | The whole planet as one system |
| Energy and matter (flows, cycles, and conservation) | Greenhouse warming, carbon and oxygen cycling |
| Stability and change | Equilibrium as the win condition |

### 6.4 Assessment alignment

- Pre/post test items are written per grade band and tagged with goal ID and the PE they assess.
- Forecasts, reflections, and rubrics carry goal IDs; NGSS codes come from the 6.1 lookup.
- The Keeper's Record shows players their goals in colony language; the teacher export shows the matching NGSS codes.

---

## Open Questions

- **How visible is the learning?** (ill-structured; TEAM.md lists it.) Current answer: the same forecasts and Keeper's Reports in both modes; classroom mode adds teacher debriefs and an expectation of written reflections (5.1). Test in playtests: do forecasts feel like play or like a test?
- **Teacher review:** ask one real teacher (or a teacher-reviewer stand-in, since this is a design exercise) to check section 5 and the teacher tools during Stages 1–3 (risk R-29).
- **Colony posts:** do posts rotating each phase give every student enough Monitor practice (C3)? With 4–5 players and 3 phases, not everyone holds the Data post. Check in the first group playtest.
- **Reflection text in the export:** decided (5.5). Open part: legal review of free text (risk R-20).
- **Fading by player or by planet?** Settled: per player (ARCHITECTURE.md §2.10, feasible and cheap).
- **Stage 5 coverage.** The owner's Stage 5 run is one full classroom-preset run plus spot checks, not 40 hours of standard play. Standard-mode-only learning moments (longer holds, home pacing) get spot checks; group dynamics still have no real test (playtest plan gap).
