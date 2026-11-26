# Learning Design

This file answers three questions:

1. **Learning goals:** what learners need to know or be able to do.
2. **Methods and media:** how they get there.
3. **Assessment:** how we know they got there.

> **Draft.** The audience is not set yet (age, classroom or self-directed, subject area). The goals below assume roughly **ages 12–18, informal or classroom use, earth and space science plus systems thinking**. Change the audience and the goals and measures should be revisited.

The game already models real Mars conditions: the starting values in the Planetary Index (0.6 kPa pressure, −60 °C, 95% CO2) are close to the real planet. The learning design builds on that: **the mechanics are the lesson**, and the game doesn't stop to lecture.

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

## Open Questions

- **Who is the learner?** Age range, and is this for classrooms, clubs, or self-directed players?
- **Curriculum alignment:** should goals map to a standard (e.g. NGSS earth and space science, or a national curriculum)?
- **How visible is the learning?** Fully hidden, light journal prompts, or an explicit "education mode"?
- **Data:** is a local action log acceptable for the intended users? For minors, it must stay local and anonymous.
