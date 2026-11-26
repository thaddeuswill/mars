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

## 4. Real-world scenarios (from the SME, round 3)

Written by the SME in `docs/science/round3-review.md` §4.6 and pasted here unchanged. Source codes (R…, AR […], S…) refer to that file and `accuracy-review.md`. Use: journal "read more", debrief 2, transfer and delayed-test item ideas, and rubric anchors for B4, B5, and D1.

**Terraforming proposals**

| Proposal | MS summary (grade 6–7) | HS extension | Options and consequences | Source |
|---|---|---|---|---|
| Release Mars's own CO2 (mirrors, dark dust on the poles) | "Warm the poles so frozen CO2 turns to gas and thickens the air." | Zubrin & McKay: mirrors of order 100 km radius, about 200,000 t; about 4 K of polar warming might trigger a warmer state | Works only if enough CO2 exists. Jakosky & Edwards found it would only about double today's pressure. Consequence: the plan alone can't make Mars warm | R17; AR [22] |
| Factory-made super-greenhouse gases | "Build factories that make gases thousands of times better at trapping heat than CO2." | 1 Pa of C3F8 gives about 33.5 K warming in a model (Marinova et al. 2005); gases break down in sunlight and need topping up (Gerstell et al. 2001) | Fast warming; needs mining fluorine and running factories for a long time; the gases are also potent greenhouse gases on Earth | AR [20]; R2 [S24] |
| Local warming with silica aerogel sheets | "Cover small areas with a thin, clear foam-like sheet that traps heat, like a greenhouse roof." | A 2–3 cm layer could raise the ground underneath above melting and block UV, in lab tests and models | Works locally, not planet-wide; lab-tested only; cheaper first step | R6 |
| Nanoparticles (optional) | "Release tiny engineered particles that trap heat." | More than 30 K warming in a 2024 model | Modeled only; long-term effects unknown | AR [21] |

**Earth cases with side effects through a loop**

| Case | MS summary | Loop and side effect | Options people had | Source |
|---|---|---|---|---|
| Thawing permafrost | "Warming thaws frozen Arctic ground. It releases stored carbon, which warms the planet more." | Reinforcing loop; infrastructure damage | Cut emissions; build on piles; monitor | R3; R2 [S6] |
| CFCs and the ozone layer | "Chemicals used in fridges and sprays seemed safe. They thinned the ozone layer. A world treaty banned them, and the ozone layer is healing." | Side effect of a useful technology; the ban also avoided about 0.5 °C of warming | Ban (Montreal Protocol), replace, wait | R18: recovery to 1980 levels by about 2066 (Antarctic), 2045 (Arctic), 2040 (rest of world) |
| Cane toads in Australia | "In 1935, 2,400 toads were released to eat beetles that hurt sugarcane. They didn't eat the beetles. They spread and poisoned native animals." | Untested intervention; invasive spread | Test first; release fewer; control later (no broad method exists) | R7 (northern quoll decline of 75 % reported in one source; don't use the "200 million toads" figure, unconfirmed) |
| Arctic sea ice and albedo | "Warming melts bright ice. Dark ocean absorbs more sunlight, so the Arctic warms faster." | Reinforcing loop; Arctic warmed about 3.8× the global rate, 1979–2021 | Cut emissions (the only lever) | R2 [S5] |

Each case connects to D1/B5 rubric anchors: name the side effect, its cause, and one middle step.

**How each is used**

| Scenario | Goals | Where |
|---|---|---|
| Release Mars's own CO2 | A3, B4, D2 | Journal honesty note (already), HS debrief 2 |
| Super-greenhouse gases | A3, B5 | Emitter journal "read more"; debrief 2 question 2 |
| Silica aerogel | B4, C2 | HS "read more" (a local alternative; trade-off of scale) |
| Thawing permafrost | B2, B5, D2 | Thaw Earth link, Report 2 reply, transfer key |
| CFCs and ozone | B5, D2 | Debrief 2 question 4 (side effect of a useful technology; also separates ozone from greenhouse) |
| Cane toads | B5, D1 | Rubric anchor for "untested intervention"; debrief 2 question 4 |
| Arctic sea ice and albedo | B2, D2 | Delayed post-test item 6; transfer key (counts for B2, not carbon links) |
