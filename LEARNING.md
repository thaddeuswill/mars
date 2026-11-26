# Learning Design

**Owner:** Lead Instructional Designer · **Last updated:** 2026-11-26 (classroom group model, 17-play-period unit, per-student measures, SME science and NGSS review applied)

This file follows an Instructional Systems Design (ISD) model, ADDIE. It starts with an analysis of the learners and context (section 0), which drives the strategy choices. It then answers four questions, followed by classroom mode (section 5) and NGSS alignment (section 6):

1. **Learning goals:** what learners need to know or be able to do.
2. **Methods and media:** how they get there.
3. **Assessment:** how we know they got there, for each student and for each group planet.
4. **Player feedback:** how players know they got there while they play, and how they track their own progress.

Detailed content specs live in `docs/learning/`:
- [docs/learning/prompts-and-reflections.md](docs/learning/prompts-and-reflections.md): forecasts (prediction prompts), Keeper's Report reflections, Earth links (D2), Keeper's Record entries, reading level and grade-band rules.
- [docs/learning/rubrics.md](docs/learning/rubrics.md): scoring rubrics for D1, D2, B5, and the transfer task.

### Decisions

| Decision | Answer | Source |
|---|---|---|
| Learners | **Ages 12–18** (middle and high school), two grade bands | User decision |
| Classroom use | **Classroom mode**: a pacing preset for class periods (section 5) | User decision |
| Classroom group model | **Groups of 3–5 students share one planet; a class runs several planets** | User decision, 2026-11-26 |
| Classroom unit | **~17 play periods** (~11–12 h of play) inside a ~23-period unit (section 5.2) | User decision, 2026-11-26 |
| Curriculum standard | **NGSS**, middle and high school bands (section 6) | User decision |
| Data | **Local, anonymous action log.** Teacher CSV export uses **anonymous IDs only**; there is no username option (section 5.4) | User decision, 2026-11-26 |
| Learning layer in the vertical slice | A **minimal Phase 1 learning layer** is built in roadmap Stage 3 (section 3.5) | User decision, 2026-11-26 |
| LMS integration | **After release, opt-in only** (section 5.6) | User decision, 2026-11-26 |

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

**Scope note.** The user has confirmed this project is a design exercise and no real students will use it. We still design every learning, assessment, and privacy element as if real students would.

### How this design meets ISD's strengths

| ISD strength | How we meet it |
|---|---|
| Calls attention to key factors and how they relate | Section 0 analyzes learners, content, and environments; the alignment table below connects them |
| Grounded in the science of learning | Simulation and discovery for cause-and-effect content; productive failure; immediate feedback; prediction (retrieval and self-explanation); spaced retrieval (A4 is forecast twice); fading support (Hana's tips stop once a failure is fixed); spaced reflection at each gate |
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
| A1 Mars vs. Earth | Phase 1 hazards (incl. real perchlorate dust); Monitor values and milestones (Armstrong line 6.3 kPa, Everest pressure ~33.7 kPa); gate journal "On Earth" lines | Reading the Monitor; forecast F3 | Monitor values; gate journal | Post-test **data task**: compare a Mars/Earth data table | — | Covered |
| A2 Liquid water | Ice-to-water rule (DESIGN.md §2): ice near heaters turns to vapor in early Phase 1, brief puddles that boil away later in Phase 1, water that stays from 5 kPa and above 0 °C; Monitor shows water's boiling point | Forecasts F4a (Phase 1) and F4b (Thaw); "What caused it?" | Ice behavior changes as pressure rises; journal | Post-test scenario; F4a, F4b results | Hydrosphere gate reached | Covered |
| A3 Greenhouse effect | Greenhouse emitters (factory-made greenhouse gases); temperature curve | Forecast F5 | Temperature trend; journal | Post-test explanation | — | Covered. Depends on the emitter journal naming what it emits (SME; Lead Game Designer) |
| A4 Radiation | Radiation spikes and shielding; solar-event story beat 5; journal | Forecast F2 (Phase 1), F7 (Phase 2, spaced) | Exposure effect; failure report; forecast result | Post-test scenario; F2 and F7 results | — | **Gap closed on paper**, if story beat 5 and a spike warning ~1 sol ahead are built (Lead Game Designer, Technical Lead) |
| A5 CO2/O2 exchange | Phase 3 balancing; flora; CO2 from decay; carbon scrubbers; carbon flow view on the Monitor | Forecasts F8, F9; flora-bloom and decay drift events | Fires or stability; Monitor (O2 fraction **and** O2 partial pressure) | Post-test explanation; F8 result | Breathable condition (O2 partial pressure ≥ 16 kPa, pressure ≥ 75 kPa, CO2 ≤ 0.5 kPa) held for the active preset's hold time | Covered. **Concern:** narrow O2 window at 75 kPa may be hard to read at MS depth (4.7) |
| B1 Cause and effect | Planetary Index responses; Monitor cause breakdown | "What caused it?"; every forecast | Monitor cause breakdown | Post-test variable-link diagram | — | Covered |
| B2 Feedback loops | Fauna vs. machine signature (reinforcing); ice–albedo (reinforcing, adopted); polar frost sink and storms vs. pressure (balancing); Monitor names each as a cause | Forecast F6; Keeper's Report 2; polar-winter and fauna-grazing drift events | Keeper's Report; Earth reply | Report 2 reflection and transfer task: one loop of each type | — | Covered. Ice–albedo adopted (size pending SME), which makes HS-ESS2-2 a near-exact fit |
| B3 Prediction | Forecasts and Monitor trends | All forecasts (F1–F10) | Forecast result; per-phase tally | Own forecast accuracy, Phase 1 → Phase 3 | — | Covered |
| B4 Trade-offs | Equilibrium band; narrow breathable O2 window vs. fire risk; shrouds (−15 % output) and lures (water upkeep) vs. fauna; expansion vs. upkeep | Forecast F10; drift events during holds; Keeper's Report 3 | Monitor; fire failure report; Keeper's Report | Post-test trade-off scenario; Report 3 reflection | Equilibrium hold reached | **Gap closed** (objective added in 0.6) |
| B5 Unintended consequences | Fauna blowback in Phase 2 (fiction) **and real side effects**: ground collapse from melting ground ice, perchlorate in meltwater; Earth example in D2 content | Forecast F6; Keeper's Report 2 | Fauna appear near emitters; failure reports name fauna | Report 2 reflection, B5 rubric | — | Covered |
| C1 Resource planning | Phase 1 O2, water, and power | Forecast F1; preparing for the first storm | Dome survives or fails | Post-test planning scenario; F1 result | Dome survives the first dust storm (by the second storm) | Covered |
| C2 Design, test, improve | Breaches, wear, storms, ground collapse, emitter-wear drift events | Own repairs, claimed on the **Shift Board** | Failure reports; Hana's tips fade | Own repairs (claim → fixed credit goes to the claimer's anonymous ID): same cause doesn't recur; "What caused it?" on own failures | Repeat failures fall across phases | Covered. Needs Shift Board claims and repair events logged with the actor ID (Technical Lead) |
| C3 Data use | Planetary Monitor (derived values: O2 and CO2 partial pressure, boiling point) | Checking the Monitor before builds; Data post (DESIGN.md colony posts) | Monitor trends | Own Monitor checks before own major builds, plus a data reason in a Keeper's Report or teacher check-in | — | Covered |
| D1 Ethics | Thawlings; the crew's secret; the Moreno–Basu argument; four fauna options (defend, shrouds, lures, quiet zones), none "right"; unranked ending cards | Keeper's Report 2; final reflection; class discussion | Earth reply; epilogue cards (no ranking) | Written reflection, D1 rubric | — | Covered. Depends on non-lethal fauna options (decided 2026-11-26) |
| D2 Link to Earth | Journal "On Earth" lines; Mensah's replies; Phase 2 debrief with Earth CO2 and temperature graphs | Keeper's Report reflections; transfer task | Mensah's replies; class debriefs | Transfer task and reflections, D2 rubric | — | **Content specified** in `docs/learning/prompts-and-reflections.md`; text not yet written; Earth facts need SME check |

**Remaining gaps:**
- A4 and D2 are closed in design only. Both need content written and built (Stage 4; A4's first forecast also belongs in the Stage 3 slice if the radiation spike is in it).
- Several high school NGSS mappings are "Moderate" until specific content exists (section 6.1 notes): the D2 Earth content, carbon reservoirs on the Monitor (HS-ESS2-6), and a claim-evaluation question in the Phase 3 debrief (HS-LS2-6, now in Keeper's Report 3).

### RETAIN review

RETAIN (Gunter, Kenny & Vick, 2008, *Educational Technology Research and Development*) rates how well a game embeds academic content in its fantasy and supports transfer. It draws on Gagné's nine events of instruction, Keller's ARCS model, and Bloom's taxonomy. The six element definitions below are my working summaries from memory; the paper's exact wording and its level-scoring scheme were **not re-checked** in this session (**unverified**). Ratings are of the **current design on paper**, not of played behavior (nothing has been built or playtested).

**Vocabulary warning.** RETAIN's "adaptation" means the *learner* changing behavior, and its "immersion" means the learner being intellectually invested. Our team terms differ: "adaptive" is about the *game* changing (TEAM.md section 3), and immersion has diegetic, narrative, and flow types. Below, "adaptation (RETAIN)" and "immersion (RETAIN)" mean the RETAIN senses.

| Element | Working meaning | Rating | Where it's met | Gaps and owner |
|---|---|---|---|---|
| **Relevance** | Content connects to the learner's world and needs, in manageable steps | **Partial** | Phase 1 survival makes A1, C1 immediately useful; story gives a reason to care (Keeper, colonists waiting); gates pace the content in steps; D2 "On Earth" lines and Mensah's replies point to the learner's own planet | D2 Earth content is specified but not written (Lead Game Designer writes, SME checks, ID reviews). Relevance for unmotivated classroom students is **assumed**, untested (playtest; Lead Game Designer) |
| **Embedding** | Academic content is tied into the game's fantasy and core mechanics (endogenous), not bolted on | **Strong** | The Planetary Index *is* the science (A1–A5); the win condition is equilibrium (B4); fauna stand for unintended consequences (B5, D1); forecasts and reports are colony work in BELL's and the Keeper's voices | Thawlings are fiction and could teach "Mars has life": fiction note in every fauna entry (ID, done in 1; Lead Game Designer to apply in the journal). Super-greenhouse emitters must name their gas or A3 embeds a contradiction (Lead Game Designer) |
| **Transfer** | Learners apply what they learned to new situations | **Partial** | Transfer task on Earth's carbon cycle (B1, B2, D2); post-test uses *new* scenarios (A2, A4, B4, C1); A4 is forecast in two different situations (F2, F7) | Transfer task and pre/post items not yet written (ID, Stage 4). No near-transfer practice inside the game beyond forecasts; the Phase 2 debrief's Earth data activity is the main bridge (ID, educator guide) |
| **Adaptation (RETAIN)** | Learners change their behavior because of what they learned | **Partial** | C2 criterion: a fixed failure doesn't recur; B3: forecast accuracy rises; C3: Monitor checked before builds; Hana's tips fade once behavior changes | Behavior change is logged per student only if actor IDs are on every relevant event (Technical Lead). No check that changed behavior survives a new hazard type (consider a late "new failure" in Phase 3; Lead Game Designer) |
| **Immersion (RETAIN)** | Learners are intellectually invested and actively taking part | **Partial** (strong on paper, unproven) | No pop-up quizzes; all feedback diegetic (4.2); player language; real choices about the fauna (D1); forecasts ask players to commit before seeing the result | Long holds risk idle play and cognitive disengagement (M-16; Lead Game Designer). Text load for weak readers (ID rules in `docs/learning/prompts-and-reflections.md` section 5; playtest check). Group play can leave one student watching (colony posts rotate each phase, section 2; check in playtest) |
| **Naturalization** | Using the knowledge becomes habitual and spontaneous | **Gap** | Repeated Monitor use across all three phases (C3); forecasts every phase (B3) | ~11–12 h in class is short for habits. **Nothing measures retention after the unit** (ID: add an optional delayed post-test 3–4 weeks later to the study design, Stage 6). Home play (~40 h) gives more practice but has no measure (ID) |

#### Gagné's nine events (insight check)

| Event | Where our design does it | Note |
|---|---|---|
| 1. Gain attention | Opening: storm clears, dome damaged, alone (story beat 1) | — |
| 2. Inform learners of objectives | Keeper's Record pages ("what the colonists will need to know") | Done diegetically, never as a list of objectives (player language, TEAM.md 8.4) |
| 3. Stimulate recall of prior learning | Pre-test; forecasts ask players to use what they already know | Debrief openers could recall earlier phases explicitly (ID, educator guide) |
| 4. Present the content | World response first, then gate journal entry | — |
| 5. Provide learning guidance | Failure-report hints; Hana's tips; Monitor cause breakdown | — |
| 6. Elicit performance | Forecasts; repairs; holding the balance | — |
| 7. Provide feedback | Natural consequences, forecast results, failure reports (section 4) | — |
| 8. Assess performance | Pre/post, transfer task, rubrics (section 3) | Outside the game by design |
| 9. Enhance retention and transfer | Transfer task; D2 Earth links; A4 spaced forecast | Weakest event: see Naturalization |

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
| "Water always melts at 0 °C and boils at 100 °C" | Prevalence **unverified** | At Mars's low pressure, warmed ice turns straight to vapor, or melts and quickly boils away. Water only pools once the pressure is high enough to keep its boiling point well above its freezing point (ice-to-water rule: vapor, then brief puddles in Phase 1, pooling water from 5 kPa; forecasts F4a and F4b) |
| "The greenhouse effect is the ozone hole" | **Documented** (Boyes & Stanisstreet, cited in accuracy-review.md [28]) | Journal entry contrasts them when emitters unlock |
| "CO2 is the only greenhouse gas" | **Documented**: students often don't count CO2 or water vapor as greenhouse gases, and confuse which gases count (same research, [28]) | The emitters make factory-made greenhouse gases that trap heat far better than CO2; the emitter journal entry names them (no potency number until verified) |
| "Air with 21% oxygen is always breathable" | Risk the old design could have *created* (SME finding 1) | Breathable depends on O2 partial pressure. The Monitor shows both the O2 percentage and the O2 partial pressure; the journal says "your lungs care how much oxygen pushes into them, not just the percent" |
| "More oxygen is always better" | Plausible; prevalence **unverified** | Oxygen overshoot causes fires in Phase 3 |
| "Fixing one problem fixes the system" | A systems-thinking target, not a science fact | Real side effects carry the lesson: ground collapse from melting ground ice and perchlorate in meltwater. Warming also wakes the fauna; **the fauna are fiction**, so the journal says no life has been found on Mars and pairs it with a real Earth side effect (D2 content) |
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

**Most of the content is cause and effect and problem-solving.** That's why simulation and discovery are the main strategies, and why assessment leans on prediction and performance rather than recall.

### 0.4 Learning environments

| Environment | How it's used | Constraints to design for |
|---|---|---|
| **Classroom / computer lab** | Teacher runs several **group planets** (one per group of 3–5); students play in sessions, with discussion between | 45–60 min periods *(assumed)*; school hardware may be weak *(assumed)*; school networks may block game servers; teacher must pause, reset, and monitor several planets at once. **Each student needs a Minecraft Java Edition account**; many schools license Minecraft Education instead, which may not run NeoForge mods (**unverified**; Technical Lead to confirm, risk R-09). School IT must approve the launcher and Java |
| **STEM club / after-school** | Longer sessions, small groups on a shared planet | Fewer constraints; less teacher time for debriefs |
| **Home, singleplayer** | Self-directed, at the player's own pace | No teacher, so feedback and the Keeper's Record must stand alone |
| **Online / remote class** | Students join hosted group planets from home | Hosting cost; supervision |

**Implications**
- 40 hours is long for a class, so the pack has a **classroom mode** (section 5).
- Teacher tools: one class overview across all group planets, pause, and rollback (section 5.3).
- Performance budget must target modest school hardware running several planets (Technical Lead; roadmap Stage 4).
- The educator guide needs a one-page IT requirements sheet (accounts, launcher, Java, ports, hardware), with facts from the Technical Lead.

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
- **Own:** done by this player's anonymous ID in the action log (a repair placed, a forecast answered, a build placed).

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

---

## 1. Learning Goals

Each goal is written as something a learner can **do**, so it can be measured. Goal IDs A1–D2 are **stable** (TEAM.md 8.1).

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
| C2 Design, test, improve | Yes (failures in all phases) | Partial | Yes (MS/HS-ETS1-3, -4) | Partial | 2+ | Secondary |
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
- **Status:** proposed by the Lead Instructional Designer. Flexible (TEAM.md 8.1). Needs a consensus check with the Lead Game Designer and Producer, because it affects scope cuts.

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

**Outside the game (for classrooms):**
- Educator guide: goals, how each phase maps to them, discussion questions for the three debriefs, the classroom unit plan (section 5.2), the group model and colony posts (with a tip to give every student at least one turn on the Data post or at the Monitor before a build), and an IT requirements sheet.
- Worksheets: pre/post test, transfer task, and a variable-tracking sheet.

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

### 3.1 In-game evidence (stealth assessment)

The game logs actions to a **local file** on the server's computer. Each player is a random per-world anonymous ID; no names are logged. Every event records the actor's ID, so evidence can be attributed per student.

| Evidence | What it shows | Level | Goals |
|---|---|---|---|
| Time and order of gates reached | Progress and pacing | Group | Overall |
| How players respond when values fall (repair, rebalance, or ignore), and who did it | Diagnosis and system management | Individual (actor) | B2, C2 |
| Whether O2/CO2 is stabilized or swings wildly | Understanding of balancing loops | Group | A5, B4 |
| Shift Board claims and fixes; repeat failures from the same cause after a player's own repair | Learning from failure | Individual (claimer's ID) | C2 |
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
| Forecast F1 (dust storm) and, if the radiation spike is in the slice, F2 | B3, C1, A4 | Tests the forecast flow and hidden answers in a group |
| One failure report (seal breach) with Hana's tip | C2 | Tests "hints, not answers" |
| Keeper's Record skeleton with 2 pages (C1, C2) and the "Cairn team" section | C1, C2 | Tests own-evidence vs. group-milestone display |
| Action log with actor IDs, and a CSV export | All | Tests per-student evidence end to end |
| A 6-item Phase 1 mini pre/post test (A1 data task, A4, C1, two misconceptions) | A1, A4, C1 | First evidence of learning gain |

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
| **Failure report** (BELL) | After a breach, machine failure, or fauna attack | What failed, the likely cause, one hint. Never the full answer. Hana's tip is attached the first time; it stops once the player has fixed that cause (fading). Hints get more specific after repeat failures of the same kind (adaptive feedback, TEAM.md section 3). |
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

### 4.6 When feedback may interrupt play (instructional view, for consensus)

TEAM.md 8.6 holds the draft rules, owned jointly with the Lead Game Designer. This is my review and proposed version. It goes to the consensus process; nothing here is final until the Lead Game Designer has weighed in and the Stage 3 playtest has checked it.

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

**Where we may disagree with the Lead Game Designer:** rule 3 (limited deferral of reports) and rule 4 (scope: pop-ups only). Both serve measurement; both cost a little flow. I'd accept "can live with it" on a stricter pop-up limit if log lines stay unlimited in content but capped in rate.

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

Every gate, hazard, and learning moment is still there. Classroom mode makes them come sooner, not disappear.

### 5.2 Suggested unit plan (23 class periods, 17 play periods)

Phase play split 4 / 7 / 6 periods, from the Lead Game Designer's classroom time model (~3.6 / 7.7 / 5.6 periods for a group of 4; balance sheet §5.3). Phase labels are expected, not fixed: Phase 2 will often spill into the start of Phase 3's periods, and the debriefs work either way.

| Periods | Activity | Gameplay phase | Main goals |
|---|---|---|---|
| 1 | Pre-test; intro to Mars; form groups of 3–5; roles; controls | — | — |
| 2–5 | Play: survive in the dome, first dust storm, first solar event, Regolith Outgassers, ice that vanishes | Phase 1 | A1, A2, A4, C1, C2, C3 |
| 6 | Debrief 1: Mars vs. Earth data, pressure and water | — | A1, A2, C1 |
| 7–13 | Play: processors, emitters, thaw, fauna and fauna options, ground collapse, expansion | Phase 2 | A2, A3, B1, B2, B5 |
| 14 | Debrief 2: greenhouse effect with Earth's CO2 and temperature graphs; feedback loops; unintended consequences; "Was waking them worth it?" | — | A3, B2, B5, D1, D2 |
| 15–20 | Play: biome seeding, O2/CO2 balance, equilibrium, beacon | Phase 3 | A5, B4, B3 |
| 21 | Debrief 3: carbon cycle, stability and change, evaluating a claim about stable ecosystems | — | A5, B4, D2 |
| 22 | Transfer task: Earth's climate system | — | B1, B2, D2 |
| 23 | Post-test and reflection discussion | — | All |

**Groups move at different speeds.** Each group is on its own planet, so not every group reaches a gate before its debrief period. Debriefs are built to work anyway: they discuss what groups have seen so far, using the teacher overview to pick examples. A group that's far behind can get a higher rate on its own planet (teacher tool); a group that's ahead keeps playing ("read more" pages and optional story beats are the extension). Keeper's Reports fire at gates in the game, so for some groups the report arrives before the class debrief and for others after. Both are fine.

**Shorter units.** "Set phase" lets a teacher start at Phase 2 for a shorter unit, but the goals taught only in Phase 1 (A4, C1, much of A1) then need class time instead. The educator guide lists which goals each shortcut drops.

### 5.3 Teacher tools

| Tool | What it does |
|---|---|
| Class overview | Every group planet in one view: each planet's phase and gates; each student's (anonymous ID) Keeper's Record pages, forecast tally, and reports sent. Individual and group evidence shown separately (3.0) |
| Pause / resume world | Freezes the simulation, hazards, and fauna between classes, per planet or all at once |
| Pacing per planet | Raise or lower one planet's rate multiplier to keep groups near the unit plan. Never changes thresholds |
| Rollback | Restore a save from the start of a session (e.g. after griefing or a bad crash). The action log must survive the rollback (Technical Lead, M-06) |
| Set phase | Start a planet at a later phase if the unit is shortened. The overview marks the goals skipped |
| Export log | Writes the local action log to CSV files with anonymous IDs only (5.4, 5.5) |
| Grade band | Set middle or high school depth for each planet |

### 5.4 Data and privacy

- The action log stays **on the server's computer**. Nothing is sent over the internet.
- Each player gets a **random per-world anonymous ID** before anything is logged. The game never logs Minecraft account IDs or usernames in the action log.
- A local mapping from IDs to players stays on the server's computer, in a separate file that is **never exported**. The teacher uses it in the in-game class overview to give feedback. Because of this mapping, the data is pseudonymous on the server's computer and anonymous outside it.
- **The teacher CSV export uses anonymous IDs only. There is no option to export usernames.** To match an ID to a student, the teacher looks it up on the server's computer.
- No names, emails, ages, or other personal data are collected by the pack.
- **Free-text answers** (Keeper's Report reflections) could contain a name a student typed. See Decisions needed in the current report: recommended handling is a separate reflections file, with known player names replaced by IDs and a plain-language warning.
- The educator guide carries a plain-language data notice for students and families.

### 5.5 What teachers need from the data (input to the Technical Lead)

The teacher needs to answer three questions: *Where is each group? What has each student shown? Who needs help?* The CSV export serves those. Columns are the "done when" for consistency review item M-08.

| File | One row per | Columns |
|---|---|---|
| `students.csv` | Student (anonymous ID) | planet_id, anon_id, grade_band, sessions_present, posts_held (by phase), shift_board_claims, shift_board_fixed, forecasts_answered_p1/p2/p3, forecasts_correct_p1/p2/p3, explain_checks_correct/attempted, own_repairs, own_repeat_failures, own_major_builds, monitor_checks_before_own_builds, reports_sent (0–4), record_pages_filled (list of goal IDs) |
| `evidence.csv` | Evidence event | planet_id, anon_id, goal_id, ngss_ms, ngss_hs, evidence_type (forecast, check, repair, report, monitor), result, sim_day, session_number |
| `planets.csv` | Group planet | planet_id, preset, grade_band, current_phase, each gate with the session it was reached, storms survived, dome failures per storm, current Index values |
| `reflections.csv` | Reflection answer | planet_id, anon_id, report_number, goal_ids, text (handling pending decision) |

NGSS codes come from a goal-ID → NGSS lookup in data (section 6.1), so the export updates when the mapping does.

### 5.6 LMS integration (after release, opt-in)

Decided 2026-11-26: no LMS integration before release. After release it is **opt-in, off by default**. Learning needs, for the Technical Lead to design against later:
- Teachers want the same three answers as 5.5, inside their school's LMS, ideally as per-goal status and the rubric-scored reflections.
- Only goal-level summaries need to travel; raw action logs never do.
- **Open conflict to solve before building it:** passing results into an LMS gradebook needs to know which student is which. That conflicts with "nothing personal leaves the server's computer" unless the ID-to-student match happens on the school's side (e.g. the teacher imports the anonymous-ID CSV and matches it locally). This is an ill-structured problem for after release; it is not decided here.

---

## 6. NGSS Alignment

Mapped to NGSS performance expectations (PEs), with the science practices (SEPs) and crosscutting concepts (CCCs) the game exercises. The SME reviewed every code on 2026-11-26 ([docs/science/ngss-verification.md](docs/science/ngss-verification.md)). The session's network blocked nextgenscience.org, so codes were checked through state mirrors and search copies. **Verify each code word for word against the official NGSS text before the educator guide is published** (about 30 minutes with normal web access).

### 6.1 Performance expectations

| Goal | Middle school (6–8) | High school (9–12) |
|---|---|---|
| A1 Mars vs. Earth | MS-ESS1-3 (assessed with data, not recall) | — (background knowledge) |
| A2 Liquid water | MS-PS1-4 (DCI PS1.A: state changes with temperature or pressure) | Extends MS-PS1-4. Related: HS-ESS2-5 (only if water–regolith effects are shown in game) |
| A3 Greenhouse effect | MS-ESS3-5 (via the Earth debrief) | HS-ESS2-4 |
| A4 Radiation | — (introduce as a concept) | HS-PS4-4 (EM radiation only; magnetic shielding is beyond this PE) |
| A5 CO2/O2 exchange | MS-LS1-6 | HS-LS2-5; HS-ESS2-6 (needs carbon reservoirs on the Monitor) |
| B1 / B2 Systems and feedback | MS-LS2-4 (B1) | HS-ESS2-2 |
| B3 Prediction from models | — (practice, see SEPs) | HS-ESS3-6 |
| B4 / B5 Trade-offs and consequences | MS-ETS1-2 | HS-ETS1-3; HS-LS2-6 (stability and change) |
| C1 / C2 Engineering design | MS-ETS1-1 to MS-ETS1-4 | HS-ETS1-1 to HS-ETS1-4 |
| C3 Data use | — (practice only: Analyzing and interpreting data) | — (practice only) |
| D1 Ethics | — (no PE; NGSS PEs don't assess values. Practice: Engaging in argument from evidence) | — (same) |
| D2 Link to Earth | MS-ESS3-5 | HS-ESS2-4, HS-ESS3-6 |

**Conditions the SME attached to these mappings** (each one is tracked in the alignment table):
- MS-ESS3-5 and the D2 codes count only if the Phase 2 debrief uses Earth's CO2 and temperature data (unit plan period 14; `docs/learning/prompts-and-reflections.md` section 3).
- HS-ESS2-6 needs the Monitor to show carbon per reservoir at high school depth (Lead Game Designer).
- HS-LS2-6 needs a claim-evaluation question in the Phase 3 debrief (now in Keeper's Report 3, HS variant).
- HS-ESS2-2 would be a near-exact fit with an ice–albedo feedback in the game (optional; Lead Game Designer).
- MS-ESS3-3 for B5 was suggested but its wording is **unverified**; not added.

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
- **Reflection text in the export:** see the current report's Decisions needed.
