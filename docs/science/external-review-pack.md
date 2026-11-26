# External Review Pack

Owner: Lead Subject Matter Expert. Prepared: 2026-11-26 (round 3). Decision: seek three external reviewers before roadmap Stage 3 (TEAM.md Decision Log, round 3 row; owner action).

**Why.** The team has one internal SME (an AI agent). Critical review should also come from external SMEs and the customer. This pack gives each reviewer a short, self-contained list of the claims we most need checked, the sources we used, and the exact questions. Reviewers don't need to read the whole design.

**How to use it.**
- Send each reviewer only their packet (section 2, 3, or 4) plus section 1.
- Ask them to mark each claim **agree / agree with change / disagree**, with a one-line reason.
- Results go back to the SME, who updates `docs/science/` and raises handoffs.
- Accept is conditional until the review comes back (PROCESS.md §2).
- **Privacy:** reviewers see design text only. No student data exists (design exercise). Don't send player logs.

**Number tags** used below: **sourced** (source listed), **calc** (our arithmetic on sourced inputs), **invented** (game balance choice), **unverified** (not confirmed).

---

## 1. What the reviewer needs to know (send to all three)

- **The game.** An educational Minecraft modpack for ages 12–18. Players start in a damaged dome on today's Mars and terraform it in three phases until they can take off their helmet.
- **Time is compressed** from centuries or more to about 40 hours of play (about 11–12 hours in classroom mode). The journal says so.
- **Two kinds of content.**
  - *Physical thresholds* never change: the triple point, the 5 and 30 kPa gates, 0 °C, the breathable condition.
  - *Rates* are invented and tuned for play.
- **Labeled fiction:** the fauna; the strength of the gas-making machines; the filler gas (Mars has little nitrogen).
- **What we want from you:** tell us where the game would teach a student something false, or where a simplification is too strong to be honest. Don't check our game balance.

---

## 2. Packet A: Planetary scientist (Mars surface water and volatiles)

**Focus:** the ice-to-water rule, which carries learning goal A2 ("why water won't stay liquid on Mars, and what would change that") and two in-game forecasts.

### A.1 The rule as designed

| Pressure | Local temperature | What warmed ice does in the game |
|---|---|---|
| < 0.611 kPa | > 0 °C | Sublimates; no liquid |
| ≥ 0.611 kPa | ≥ boiling point at that pressure | Melts and boils away at once |
| 0.611–5 kPa | 0 °C to boiling point | Thin puddle that dries or refreezes within 0.02–0.25 sol (invented) |
| ≥ 5 kPa | 0 °C to boiling point | Water stays |
| Any | ≤ 0 °C | Stays ice (simplification: real exposed ice also sublimates slowly) |

### A.2 Claims to review

| # | Claim | Tag and source | Question for you |
|---|---|---|---|
| A1 | Triple point of water: 611.657 Pa, 273.16 K | sourced: NIST (AR [14]) | — (for context) |
| A2 | Boiling point of pure water: about 2 °C at 0.7 kPa, 7 °C at 1 kPa, 18 °C at 2 kPa, 33 °C at 5 kPa | calc (Antoine equation, extrapolated below 1 °C) | Is the Antoine extrapolation good enough near the triple point for teaching? |
| A3 | Near a heater at 0.7–5 kPa, melted ice boils away almost at once | calc from A2; Haberle et al. 2001 (AR [15]) | Is "boils away at once" a fair picture, or would a student see something else (e.g. vigorous evaporation with refreezing)? |
| A4 | A puddle kept just above 0 °C at about 0.7 kPa loses about 1 mm per hour | sourced: Sears, Chittenden et al. lab tests, 0.73 mm/h after gravity correction (R3 [R5]) | Is 0.02–0.25 sol an acceptable game drying time for a thin film? Does drying really slow with rising pressure in this range? |
| A5 | Unheated meltwater refreezes quickly in −60 °C surroundings | unverified (direction only) | How quickly, roughly? |
| A6 | Phoenix's ice chunks vanished in about 4 days at 724–860 Pa, below 0 °C: sublimation into dry air, not a low-pressure effect | sourced: NASA/JPL 2008; Taylor et al. 2010 (R2 [S1][S2]) | Do you agree this is the right reading? |
| A7 | 5 kPa is a game line ("water no longer boils away at everyday temperatures"), not a physical constant | invented threshold; boiling point 33 °C at 5 kPa (calc) | Is that wording honest? |
| A8 | Mars's mean pressure (0.636 kPa) sits just above the triple point; high ground is below it, low ground above | sourced: NASA Mars fact sheet (R3 [F1]) | — |
| A9 | Ground can sink where buried ice is lost (scalloped terrain; retreating ice scarps) | sourced: Dundas et al. 2018; HiRISE (R2 [S7][S8]) | Is "melts or sublimates and the ground sinks" a fair one-line summary for 12-year-olds? |
| A10 | Perchlorate brines stay liquid far below 0 °C (Ca(ClO4)2 eutectic about 198 K) | sourced: Toner et al. (R2 [S10]) | Should the game mention brines at all at the middle-school level? |

**Materials to attach:** round2-review.md §1.1; round3-review.md §4.5; DESIGN.md §2 (ice rule).

---

## 3. Packet B: Climate modeler (Mars or planetary climate)

**Focus:** two feedback loops the game teaches as real (learning goal B2), and the Phase 3 carbon budget.

### B.1 Claims to review

| # | Claim | Tag and source | Question for you |
|---|---|---|---|
| B1 | Ice–albedo feedback on Mars is small: my estimate about 0.4 °C, under 1–2 °C even with generous assumptions; capped at 2 °C in the game | calc (R2 §9); cap invented | Is the order of magnitude right for a warming, wetter Mars? Would seasonal snow or sea ice on new seas make it much larger? |
| B2 | About 25–30 % of today's atmosphere freezes onto the seasonal caps each year | sourced: USGS, Planetary Society, James et al. 1992 (R2 [S3]) | — |
| B3 | Thicker CO2 air freezes out more at the poles (the frost point rises with pressure) | sourced direction; frost points calc from a standard formula, formula unverified | Is the frost-point formula T = 3148 / (23.102 − ln(P/100 Pa)) K acceptable? |
| B4 | The game's polar sink stops above a −20 °C global mean and becomes a source (releasing what it took, plus up to about 0.5 kPa of buried CO2) | invented thresholds; 0.5 kPa calc from Phillips et al. 2011, "up to 80 %" (R2 [S4]) | Is a reversible polar reservoir an honest teaching model? Is any threshold defensible, or should we just say "colder poles take, warmer poles give"? |
| B5 | At equilibrium (75 kPa, CO2 ≤ 0.5 kPa, mostly N2 and O2), a seasonal polar pressure dip of about 1.5 kPa is **not** physical, because N2 and O2 don't condense | my reasoning (R3 §4.1) | Agree? Would a weather-system pressure dip of about 1–2 % be realistic instead? |
| B6 | Making 22 % O2 at 75 kPa from CO2 by photosynthesis needs about 18.5 kPa of CO2 and locks away about 260,000 billion tonnes of carbon, about 300 times the carbon in Earth's air | calc (R3 §10) from NASA fact sheets | Is the mass balance right? Is "carbon must be buried, or decay takes the oxygen back" the right message? |
| B7 | Dust storms heat the air (dust absorbs sunlight) while cooling the ground by day | unverified as a citation | Is that a fair one-liner? (Affects forecast FI-1) |
| B8 | Super-greenhouse gases break down in sunlight on Mars and need replenishing | sourced: Gerstell et al. 2001 (R2 [S24]) | — |

**Materials to attach:** round2-review.md §1.2–1.3; round3-review.md §1; balance-sheet.md §2 and §7.

---

## 4. Packet C: Science teachers (one middle school, one high school)

**Focus:** whether students will read our assessment items the way we intend. This is a classroom judgment, not only a science one.

### C.1 Transfer task answer key ("Draw the feedback loops in Earth's carbon cycle")

Full key: round2-review.md §3.3.

| # | Question for you |
|---|---|
| C1 | Is "count the minus links: even = reinforcing, odd = balancing" teachable at your grade level? |
| C2 | Should an ice–albedo loop (a climate loop, not a carbon loop) get partial or full credit? |
| C3 | We score direction, not strength, because some link strengths (plant growth response, soil decay) are uncertain. Is that fair to students? |
| C4 | Which common student errors are missing from our list? |

### C.2 Forecast distractors

Each forecast has one correct answer and 1–3 distractors. We removed distractors that were partly true (e.g. "Cold makes panels stronger": solar cells do work slightly better in the cold). Full list: `docs/design/messages/` and round2-review.md §3.1.

| # | Item | Question for you |
|---|---|---|
| C5 | F2 "why": Thin air and no global magnetic field · Mars is closer to the Sun · Red color soaks up radiation | Do your students hold "Mars is closer to the Sun"? Is any distractor arguably true? |
| C6 | F8: "Plants make CO2 at night only" as a distractor | Too tricky, or the right misconception to target? |
| C7 | F9: "More O2 smothers flames" | Plausible to students, or obviously wrong? |
| C8 | FI-1 (reworded): "Your emitters run on solar power. What happens to the warming they add while the storm lasts?" | Clear enough? |
| C9 | F4a: "The heater has warmed this ice above 0 °C. The air is very thin. What will it do?" Key: "Disappear into vapor, leaving no water" | Will students who know "ice melts at 0 °C" find this fair once they see the result? |
| C10 | Reading level: target grade 6–7 for all main text | Spot-check five items: too hard, right, too easy? |

### C.3 Earth data for debrief 2

| # | Question for you |
|---|---|
| C11 | We'll use NOAA's Mauna Loa CO2 graph and NASA's GISTEMP temperature graph. Do you already use these, and is the baseline difference (1951–1980 vs. 1850–1900) a problem in class? |

**Materials to attach:** rubrics.md (transfer task); prompts-and-reflections.md §1; round3-review.md §3.1; five sample forecast cards.

---

## 5. Return form (copy per claim)

| Claim # | Agree / agree with change / disagree | Reason (one line) | Suggested wording or source |
|---|---|---|---|
| | | | |

## 6. Status

| Packet | Reviewer found | Sent | Returned | SME follow-up |
|---|---|---|---|---|
| A Planetary scientist | — | — | — | — |
| B Climate modeler | — | — | — | — |
| C Science teachers | — | — | — | — |
