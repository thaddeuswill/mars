# Cognitive Task Analysis: The Planetary Engineer

**Owner:** Lead Instructional Designer · **Status:** Draft 1, 2026-11-26 · **Depends on:** [LEARNING.md](../../LEARNING.md) §0 and §0.6, [DESIGN.md](../../DESIGN.md), [science review](../science/accuracy-review.md)

**What this is.** A short cognitive task analysis (CTA) of the job the player does: run a planet's atmosphere, water, and life support toward a stable, breathable state. It names the core knowledge and skills, separates **procedural** steps from **cognitive** work, and lists the mistakes typical at each expertise level. Chapter 9 (ACEDT, coordinator's summary) recommends doing CTA with the SME and adding real scenarios, because a traditional CTA misses story and interaction elements.

**Method and limits.** Built from the design documents and the SME's accuracy review. Not built from interviews with real planetary scientists or engineers, and not from observing learners. Expertise-level mistakes are *(assumed)* until playtests or a teacher review confirm them. Section 4 requests real scenarios from the SME; none are invented here.

---

## 1. Task hierarchy

Top-level task: **Keep the colony alive while moving the planet to a stable, breathable band, and hold it there.**

| # | Subtask | Procedural (steps, recall) | Cognitive (judgment, models, decisions) | Goals |
|---|---|---|---|---|
| 1 | Keep life support running | Seal a volume; place and fuel O2, water, power machines; replace filters; use the airlock dust-off | Decide what's life-critical first when resources are short; anticipate storms | C1, C2 |
| 2 | Read the planet | Open the Monitor; find a value, trend, cause line, next gate | Interpret a trend; compare Mars with Earth; spot which cause dominates; notice a value nearing an edge | C3, A1, B1 |
| 3 | Predict before acting | Log a forecast | Run a mental model of the Index forward ("if I add X, Y will…"); pick which idea applies (interleaving) | B3, A2–A5 |
| 4 | Change the planet | Build and power Outgassers, processors, emitters, extractors, flora | Choose which lever moves which value, at what cost; sequence interventions toward a gate | B1, A1–A3, A5 |
| 5 | Diagnose and fix failures | Claim on the Shift Board; repair; mark fixed | Find the root cause rather than the symptom; stop it recurring | C2, B1 |
| 6 | Manage side effects | Use shrouds, lures, quiet zones, defense; survey ground ice; treat meltwater | Recognize reinforcing loops (fauna, ice–albedo); weigh output against side effects | B2, B5, B4 |
| 7 | Hold the balance | Answer drift events | Keep several values in band at once; avoid overcorrection; trade pressure against O2 margin | B4, A5, B2 |
| 8 | Reflect and decide what's right | Write a Keeper's Report | Argue a position on waking the thawlings with evidence; link the mechanism to Earth | D1, D2, B5 |

**Core cognitive skills** (what makes an expert, not the steps): a working **causal model** of the Index (which lever moves what, by roughly how much, with what delay); **loop recognition** (reinforcing vs. balancing); **anticipation** (acting before an edge, not after); **root-cause diagnosis**; **multi-variable balancing** with costs.

## 2. Typical mistakes by expertise level *(assumed)*

| Level | Typical mistakes | Design response |
|---|---|---|
| **New to Minecraft survival** | Lost in controls and inventory; can't seal a room; dies to suffocation before learning anything | Landing onboarding (DESIGN.md gap); F1 only after it; failure reports with Hana's tips |
| **Experienced Minecraft, new to tech mods** | Doesn't know machines need power and inputs; doesn't expect machines to wear | Worked example on the first Outgasser (LEARNING.md 2.2); simple in-house machines |
| **Experienced tech-mod player** | Optimizes throughput and ignores side effects; builds wide; treats fauna as a combat problem; skips text | Skippable worked example; fading (4.8); fauna pressure follows machine signature, so defense alone doesn't work |
| **MS band (science)** | Water always boils at 100 °C and melts at 0 °C; 21 % O2 is always breathable; greenhouse effect = ozone hole; more O2 is always better; one fix fixes the system | Forecasts F4a/F4b, F9; Air window; journal contrasts; real side effects (LEARNING.md 0.2) |
| **HS band (science)** | Knows partial pressure in principle but doesn't use it; mixes up reinforcing and balancing; treats equilibrium as a fixed point, not a band that needs work | HS "why" follow-ups; HS Air window chart; Report 3 claim evaluation (HS-LS2-6) |
| **All levels (systems)** | Reacts after an edge is crossed; overcorrects; attributes a change to the last thing they did rather than the largest cause | Cause breakdown; time-to-edge estimate (4.7.1); drift events; "What caused it?" |

## 3. Procedural vs. cognitive: what this means for teaching and measuring

| Element type | Teach with | Measure with |
|---|---|---|
| Procedural (sealing, wiring power, repairs, airlock) | Worked example once, then practice; consistent machine UI | Observed in play (completed without failure); not on the post-test |
| Cognitive (models, loops, anticipation, trade-offs, diagnosis) | Simulation, forecasts, failure reports, interleaving, debriefs | Forecasts, "What caused it?", rubrics, post-test scenarios (LEARNING.md 3.8–3.9) |

The post-test measures the cognitive elements only. Procedural skill with Minecraft is a means, not a goal.

## 4. Real-world scenarios requested from the SME

A CTA alone misses the settings and cases where these skills are used. **Requested from the Subject Matter Expert** (handoff); nothing below is a claim yet:

| Scenario type | What we need | Used in |
|---|---|---|
| Real terraforming proposals | Two or three published proposals and their main trade-offs (e.g. the analysis already cited as Jakosky & Edwards, 2018, and McKay's timescales in accuracy-review.md), with what each would and wouldn't achieve | Journal "read more"; HS debrief 2; delayed post-test item ideas |
| Earth climate cases with side effects | Real cases where an intervention or human activity had an unintended consequence through a loop (the permafrost case already in the Earth links, and others the SME can source) | Debrief 2; transfer task; B5 rubric anchors |
| Real engineered life-support systems | Real closed-habitat or spacecraft life-support cases and what failed | Debrief 1 (C1, C2); Report 1 anchors |
| Range of actions and consequences | For each scenario: the options people had, and the positive and negative consequences of each | Rubric anchors for B4, B5, D1; Report 3 |

Each scenario needs a source, a plain-language summary at grade 6–7, and an HS extension.
