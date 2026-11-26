# Learning Design

This file starts with an analysis of the learners and context (section 0), which drives the strategy choices. It then answers four questions:

1. **Learning goals:** what learners need to know or be able to do.
2. **Methods and media:** how they get there.
3. **Assessment:** how we know they got there.
4. **Learner feedback:** how learners know they got there, and how they track their own progress.

> **Draft.** The audience is not set yet (age, classroom or self-directed, subject area). The goals below assume roughly **ages 12–18, informal or classroom use, earth and space science plus systems thinking**. Change the audience and the goals and measures should be revisited.

The game already models real Mars conditions: the starting values in the Planetary Index (0.6 kPa pressure, −60 °C, 95% CO2) are close to the real planet. The learning design builds on that: **the mechanics are the lesson**, and the game doesn't stop to lecture.

---

## 0. Learner and Context Analysis

This analysis determines which strategies and assessments fit. Everything marked *(assumed)* needs confirming; if it changes, sections 1–4 change with it.

### 0.1 Targeted learners

| | Primary | Secondary |
|---|---|---|
| **Who** | Students aged ~12–18 (middle and high school) *(assumed)* | Adult and teen hobbyist players who pick up the pack for fun |
| **Setting** | Science class, STEM club, or after-school program | Home, singleplayer or a small friends' server |
| **Motivation** | Mixed: some love Minecraft, some are there because the teacher said so | High and self-chosen |

**Characteristics that shape the design**
- **Minecraft familiarity is high but uneven.** Most know the basics; few have played tech modpacks. → Phase 1 has to teach modded machines from scratch, and survival must not punish newcomers before they learn the controls.
- **Reading levels vary widely.** → Journal text kept short and plain (aim for roughly a grade 6–7 reading level), with a "read more" layer for depth. Key information is also shown visually.
- **Attention in short sessions.** Classroom periods are 45–60 minutes. → Each session should end at a natural save point with something achieved.
- **Mixed ability groups on servers.** → Global progress lets stronger players carry some load, so the Colony Record tracks each player's own contributions and predictions.
- **Accessibility.** → Color is never the only signal (sky tint plus monitor numbers); subtitles for sound cues; no time-pressured reading.

### 0.2 Prior knowledge and experience

**Likely to know already**
- States of matter (solid, liquid, gas), basic temperature
- Plants need CO2 and light and produce O2 (photosynthesis, basic level)
- Mars is red, cold, and far away

**Likely gaps**
- Atmospheric pressure as a concept, and how it affects boiling and freezing
- Why the greenhouse effect works (what gases do with heat)
- Radiation and magnetic fields
- Feedback loops and equilibrium as ideas

**Common misconceptions to design against**
| Misconception | How the game confronts it |
|---|---|
| "Mars has no atmosphere at all" | Monitor starts at 0.6 kPa, not zero |
| "Ice melts as soon as it's above 0 °C" | At low pressure, ice turns straight to vapor; water only pools once pressure is high enough |
| "The greenhouse effect is the ozone hole" | Journal entry contrasts them when emitters unlock |
| "More oxygen is always better" | Oxygen overshoot causes fires in Phase 3 |
| "Fixing one problem fixes the system" | Blowback: warming wakes fauna |

**Pre-test** (section 3.3) confirms prior knowledge and catches these misconceptions before play.

### 0.3 Nature of the content

Different kinds of content need different teaching and testing approaches.

| Content type | Examples in this game | Teaching strategy | Assessment approach |
|---|---|---|---|
| **Facts** | Mars surface pressure is ~0.6 kPa; atmosphere is ~95% CO2 | Show in the Monitor and journal; repeat through play | Recall questions (pre/post test) |
| **Concepts** | Pressure, greenhouse gas, equilibrium, feedback loop | Examples and non-examples in play; journal definitions at the moment they matter | Classify or identify examples |
| **Cause and effect / principles** | More pressure → liquid water possible; more warming → fauna wakes | Simulation: act, observe, explain. Prediction prompts | Predict outcomes of new scenarios |
| **Procedures** | Seal a dome, set up an O2 loop, maintain a machine | Guided first attempt, then practice with less support | Observed in play: completed without failure |
| **Problem-solving** | Stabilize O2/CO2 while keeping temperature up | Open-ended challenges, productive failure, hints not answers | Performance: reach and hold equilibrium |
| **Attitudes** | Weighing terraforming ethics; linking to Earth's climate | Reflection prompts, discussion | Rubric-scored reflection |

**Most of the content is cause and effect and problem-solving.** That's why simulation and discovery are the main strategies, and why assessment leans on prediction and performance rather than recall.

### 0.4 Learning environments

| Environment | How it's used | Constraints to design for |
|---|---|---|
| **Classroom / computer lab** | Teacher runs a small server; students play in sessions, with discussion between | 45–60 min periods; school hardware may be weak; school networks may block game servers; teacher needs to pause, reset, or monitor |
| **STEM club / after-school** | Longer sessions, small groups on a shared server | Fewer constraints; less teacher time for debriefs |
| **Home, singleplayer** | Self-directed, at the learner's own pace | No teacher, so feedback and the Colony Record must stand alone |
| **Online / remote class** | Students join a hosted server from home | Hosting cost; supervision |

**Implications**
- 40 hours is long for a class. A classroom unit would cover about 15–25 sessions. Options: a **classroom mode** with faster pacing (using the gate-threshold config), or teachers running selected phases. *(decision needed)*
- Teacher tools: a way to see each student's Colony Record, pause the world, and roll back a save.
- Performance budget must target modest school hardware (Stage 4 of the roadmap).

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

Each objective states the **behavior** (what the learner does), the **condition** (in what situation, with what resources), and the **criterion** (how well). These are the measurable versions of the goals in section 1.

| Goal | Behavior | Condition | Criterion |
|---|---|---|---|
| A1 | Compare Mars and Earth on pressure, temperature, atmosphere, and radiation | Written post-test, no notes | At least 3 of 4 correct |
| A2 | Explain why water can't stay liquid on Mars's surface and what must change | Written or verbal, given a scenario | Names both pressure and temperature |
| A3 | Explain how greenhouse gases warm a planet | Written post-test | Correct mechanism (gases trap outgoing heat) |
| A5 | Describe the CO2/O2 exchange and why balance matters | During Phase 3 play, and on post-test | Holds O2 in band for 7 in-game days; correct written explanation |
| B1 | Draw links between planetary variables | Given a blank diagram of 5 variables | At least 4 correct links |
| B2 | Identify a reinforcing and a balancing feedback loop | In the game or a new Earth example | One correct example of each |
| B3 | Predict the effect of a change before making it | In-game prediction prompts | Phase 3 accuracy at least 20 points above Phase 1 |
| B5 | Identify an unintended consequence of an intervention | Reflection prompt after Phase 2 | Names one and its cause |
| C1 | Keep life support running through the first crisis | Phase 1 play, no outside help | Survives the first dust storm without the dome failing, within 2 attempts |
| C2 | Diagnose and fix a failing system | After any failure report | Same failure doesn't recur from the same cause |
| C3 | Use data to make a decision | Before a major build, with the Monitor | Checks the Monitor and can say why they chose the build |
| D1/D2 | Argue a position on terraforming and link it to Earth | Reflection or class discussion | Rubric score of 3 out of 4 or higher |

Criteria are first drafts. They should be checked against the first playtest, then adjusted.

---

## 1. Learning Goals

Each goal is written as something a learner can **do**, so it can be measured.

### A. Planetary science (knowledge)
| # | By the end, learners can… | Where the game teaches it |
|---|---|---|
| A1 | Describe how Mars differs from Earth in pressure, temperature, atmosphere makeup, and radiation | Phase 1 hazards, Planetary Monitor |
| A2 | Explain why liquid water can't stay on the surface at low pressure and temperature, and what has to change for it to | Thaw and Hydrosphere gates |
| A3 | Explain the greenhouse effect and how adding gases raises surface temperature | Greenhouse emitters, temperature curve |
| A4 | Explain why a thin atmosphere and no magnetic field expose the surface to radiation | Radiation spikes, shielding |
| A5 | Describe how plants and algae exchange CO2 for O2, and why the balance matters | Phase 3 O2/CO2 balancing |

### B. Systems thinking (skills)
| # | By the end, learners can… | Where the game teaches it |
|---|---|---|
| B1 | Identify cause-and-effect links between planetary variables (e.g. temperature → water → fauna) | The Planetary Index and its world responses |
| B2 | Recognize feedback loops, both reinforcing and balancing | Storms weaken as pressure rises; fauna rises with infrastructure |
| B3 | Predict the effect of a change before making it, and check the prediction | Planetary Monitor trends |
| B4 | Manage trade-offs between competing goals (warming vs. CO2 removal, expansion vs. upkeep) | Late game equilibrium band |
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
| D1 | Discuss whether terraforming another planet is a good idea, and what's at stake for life that might already be there (the dormant fauna) |
| D2 | Connect what they learned to Earth's own climate and ecosystems |

### Science accuracy notes
Where the game simplifies, it should say so in the journal. This keeps the learning honest.
- Real Mars likely doesn't hold enough frozen CO2 to warm itself by outgassing alone.
- Without a magnetic field, a thickened atmosphere would slowly be stripped away by solar wind. The game ignores that timescale.
- Terraforming would take centuries or millennia, not 40 hours.

---

## 2. Methods and Media

| Method | What it looks like in the game | Goals it serves |
|---|---|---|
| **Simulation / learning by doing** | The Planetary Index responds to what players build. Variables follow simplified real physics. | A1–A5, B1–B4 |
| **Discovery and experimentation** | Players test machines and see results; no tutorial tells them the answer. | B1, B3, C2 |
| **Productive failure** | Breaches, storms, and wear break systems. Players diagnose and rebuild. | C1, C2, B5 |
| **Data literacy** | Planetary Monitor block shows current values and trend graphs. | B3, C3 |
| **Embedded explanation** | Lore journal entries unlock at each gate and explain the real science behind what just happened. Short and optional. | A1–A5 |
| **Prediction prompts** | At key moments the journal asks "What do you expect to happen if…?" The player picks an answer, then sees the result. | B3, assessment |
| **Reflection prompts** | At each phase gate, a short open question (e.g. "Was waking the fauna worth it?"). | D1, D2 |
| **Collaboration** | On small servers, shared global progress forces players to coordinate. | B4, C1 |

**Outside the game (for classrooms):**
- Educator guide: goals, how each phase maps to them, discussion questions, pacing for 40 hours of play across class sessions.
- Worksheets: pre/post questions and a variable-tracking sheet.

---

## 3. Assessment: How We'll Know

Three layers, from least to most effort for the learner.

### 3.1 In-game evidence (stealth assessment)
The game logs actions to a **local file** (no network, no personal data) that an educator can read.

| Evidence | What it shows | Goals |
|---|---|---|
| Time and order of gates reached | Progress and pacing | Overall |
| How players respond when values fall (repair, rebalance, or ignore) | Diagnosis and system management | B2, C2 |
| Whether O2/CO2 is stabilized or swings wildly | Understanding of balancing loops | A5, B4 |
| Number of preventable failures (e.g. repeated breaches from the same cause) | Learning from failure | C2 |
| How often the Planetary Monitor is checked before big builds | Data-driven decisions | B3, C3 |

### 3.2 Embedded checks
- **Prediction prompts:** right/wrong is logged. Improvement over the playthrough shows a better mental model (B3).
- **Gate journal questions:** one short multiple-choice question per gate on the science just shown (A1–A5).

### 3.3 Outside the game
- **Pre/post test:** the same 10–15 questions before and after play, covering A and B goals. Learning gain is the main success measure.
- **Reflection writing or discussion:** scored with a simple rubric (D1, D2, B5).
- **Transfer task:** apply the same reasoning to Earth, e.g. "Draw the feedback loops in Earth's carbon cycle" (B1, B2, D2).

### Success criteria (draft)
- Average post-test score improves meaningfully over pre-test (target to set after the first playtest).
- At least 70% of learners reach the Phase 3 gate.
- Prediction-prompt accuracy rises from Phase 1 to Phase 3.
- Learners can name at least two feedback loops and one unintended consequence in reflection.

---

## 4. Learner Feedback and Self-Assessment

Section 3 is how *we* (designers, educators) know. This section is how *learners* know, while they play.

**Answer: a combination.** Natural consequences come first, and specific feedback explains them. Every piece of feedback lives inside the game world (monitor, journal, colony log), so it never breaks immersion or turns into a pop-up quiz.

### 4.1 Natural consequences: "did it work?"
The world itself responds to what the learner does. This is the fastest and most frequent feedback.

| Learner action | Consequence they see | Tells them |
|---|---|---|
| Builds an atmospheric processor | Sky slowly shifts from tan toward pink, fog thickens | Pressure is rising (A1, B1) |
| Lets maintenance slip | Machine sputters, values on the monitor drop | Systems need upkeep (C2) |
| Pushes warming hard | Ice melts, then fauna appear near the emitters | Interventions have side effects (B5) |
| Over-seeds flora | O2 overshoots, fires break out | Balancing loops matter (A5, B4) |
| Gets the balance right | Storms fade, water stays liquid, plants spread | The system is stable (B2) |

Natural consequences show **what** happened. They don't always show **why**, which is where the next layer comes in.

### 4.2 Specific feedback: "why did it happen?"
| Tool | When | What it says |
|---|---|---|
| **Planetary Monitor** | Any time | Current values, trend arrows, and which machines or events moved each value most in the last day. Turns "something changed" into "this caused it". |
| **Failure report** | After a breach, machine failure, or fauna attack | A short colony log entry: what failed, the likely cause, one hint. Never the full answer. |
| **Prediction result** | Right after a prediction prompt resolves | Whether the prediction was right, and a one- or two-sentence science explanation either way. |
| **Gate journal entry** | When a gate is reached | What the learner just achieved and the real science behind it. |

### 4.3 Self-assessment: "how am I doing?"
| Tool | How it works | Goals |
|---|---|---|
| **Colony Record** (in the journal) | Lists each learning goal written in player language (e.g. "Understand why water won't stay liquid"). Each one fills in as the player shows it in play: gate reached, prediction right, failure recovered from. The player can see what's filled in and what's still blank. | All |
| **Prediction accuracy** | Shown in the journal as a running tally per phase, so learners see their own mental model improving. | B3 |
| **Phase debrief** | At each phase gate, a short summary: what you built, what went wrong and how you fixed it, your prediction accuracy, plus one reflection question. | B, C, D |
| **"Explain it" check** | Optional journal page: pick the cause of a recent event from a short list. Instant feedback. Lets learners test themselves without consequences. | A1–A5, B1 |

### 4.4 How learners know they've achieved the outcomes
A learner has met the outcomes when, in their own Colony Record:
- every goal is filled in,
- prediction accuracy in Phase 3 is clearly higher than in Phase 1, and
- they've reached equilibrium and held it, which is only possible if they understand the balancing loops.

**Reaching the beacon is the final proof.** The ending needs every variable in band at the same time for 14 in-game days. A learner can't get there by luck or grinding, only by understanding the system. The game's win condition and the learning outcome are the same thing.

### 4.5 Feedback principles
- **Consequences first, explanation second.** Let the world respond, then offer the why.
- **Hints, not answers.** Failure reports point in a direction and leave the fix to the learner.
- **Optional depth.** Journal entries are short, with "read more" for learners who want it.
- **Fail forward.** Because gates lock in, mistakes cost time, not progress, which keeps failure safe enough to learn from.

---

## Open Questions

- **Classroom pacing:** add a classroom mode with faster pacing, or have teachers run selected phases?
- **Teacher tools:** how much does the teacher need to see and control (Colony Records, pause, rollback)?
- **Who is the learner?** Age range, and is this for classrooms, clubs, or self-directed players?
- **Curriculum alignment:** should goals map to a standard (e.g. NGSS earth and space science, or a national curriculum)?
- **How visible is the learning?** Fully hidden, light journal prompts, or an explicit "education mode"?
- **Data:** is a local action log acceptable for the intended users? For minors, it must stay local and anonymous.
