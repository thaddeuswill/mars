# Polar frost sink: SME review (Stage 2 slice 5)

**Owner:** Subject Matter Expert · **Date:** 2026-11-27 · **Answers:** `docs/team/reports/2026-11-27-producer-stage2-checkpoint.md` section 2, "What the SME must decide or confirm" 1 to 4 · **Related:** `docs/design/balance-sheet.md` section 2, `docs/science/round2-review.md` section 1.2 [S3] [S4]

Marks: **verified** = opened or returned by a search on an allowed host this session; **not verified** = from memory or earlier rounds; **calc** = my arithmetic; **invented** = game number.

Network note: `science.nasa.gov` and `www.usgs.gov` would not open in this session (fetch failed, host not allowed). I used search summaries from allowed hosts. Fetch them directly when allowed.

## Sources used

| Ref | Source | What it supports | Mark |
|---|---|---|---|
| N1 | NASA Science, "Seasonal Cycles in Curiosity's First Two Martian Years" https://science.nasa.gov/resource/seasonal-cycles-in-curiositys-first-two-martian-years/ | CO2 freezes out onto the polar caps in winter; Curiosity pressure dips in long southern winters; atmosphere grows and shrinks "about 30 percent" a year | Search summary only (page not opened). **Verified for mechanism; size not verified** |
| N2 | NASA Science, Seasonal Frost in Terra Sirenum / CRISM caption https://science.nasa.gov/photojournal/seasonal-frost-in-terra-sirenum/ | Seasonal caps are mostly CO2 ice with some water frost and dust; frost sublimes in spring and raises pressure; caps reach about 55 degrees latitude by late winter | Search summary only. **Verified (summary)** |
| N3 | NASA NTRS, "The seasonal variation of atmospheric pressure on Mars as affected by the south polar cap" https://ntrs.nasa.gov/citations/19790056626 | Pressure cycle is caused by CO2 exchange between air and winter caps | Search summary only. **Verified (summary)** |
| N4 | A review chapter (arXiv 1804.08340, via search) | "Up to 25 percent of the atmospheric mass removed in southern winter, up to 10 percent in northern winter" | **Not opened; size unverified** |
| S3, S4 | round2-review.md: USGS CO2 cycle; Planetary Society; Phillips et al. 2011 (*Science* 332:838) | 25 to 30 percent seasonal; buried CO2 up to 80 percent of the air's mass | Carried from round 2. S3 and S4 pages not re-opened today |

## Q1. What the sink acts on, and how real frost cycles relate

**Real science.** Only CO2 freezes onto the poles (and a little water). Nitrogen, argon and oxygen do not. CO2 ice forms when the air at the pole falls below the CO2 frost point, which rises as CO2 pressure rises [N2, N3]. The real swing is seasonal and reversible: the air loses a large share to the caps in winter and gets it back in spring [N1, N2, N3]. Sizes found: "about 30 percent" a year (NASA Curiosity page, N1) and "up to 25 percent in southern winter, 10 percent in northern" (N4). So **"about a quarter" is the safe phrase, and "up to 30 percent" is the safe upper bound**. The exact split depends on how it is measured. Do not state a single figure as settled.

**What the game does.** The Index tracks **total pressure** in Stage 2. The sink takes a fraction of (pressure minus 0.6 kPa). It does not know which part is CO2.

| Question | Answer |
|---|---|
| Is "all pressure above 0.6" physically right? | **No, it is an approximation.** Physically the sink acts on the CO2 part only. At the start, CO2 is 95 % of 0.6 kPa (0.57 kPa, balance sheet section 1), so subtracting 0.6 removes the whole starting atmosphere from the sink's base, which is a fair stand-in for "the part the caps already hold or cannot give". |
| Where does it over-apply? | Phase 2 curve (balance sheet 3a): at 30 kPa, 18 kPa is CO2 and 12 kPa is filler gas. Applying the rate to total pressure over-applies it by up to about 40 % there (calc: 30 minus 0.6 = 29.4 against about 17.4 of CO2). Filler gas does not freeze. |
| Is that acceptable? | **Yes for Stage 2 and the slice 5 prototype**, because CO2 and filler are not tracked separately there and the numbers are invented anyway. **Revisit when the CO2 partial pressure is a live Index value:** then apply the rate to CO2 partial pressure above its own start value (recommend), and drop the "total minus 0.6" form. |
| Baseline 0.6 | Make it the planet's start pressure (data field), not a constant. Physically it stands for "air already there at the start". Say so in the code comment. **Well-structured, Technical Lead.** |
| Rates -2 %, -1 % and thresholds -40, -20 degC | **Invented. Keep the label.** Direction is sourced (more CO2 raises the frost point, so more freezes out). The size is not. |
| "Below -40 degC" | Honest note needed: the real trigger is the pole's winter air temperature against the CO2 frost point (about -125 degC at 0.6 kPa, -109 at 5, -92 at 30; calc from a formula I have not verified, round2 section 9). The game's global-mean temperature is far above the frost point in every phase. The thresholds are a **game stand-in** for "cold enough that the poles trap CO2". |
| Net loss versus real cycle | **Honest note:** the real cycle is a reversible swing with little net loss over a year. The game's net loss below -40 degC is a deliberate simplification (a one-way sink) and the reservoir that returns it above -20 degC is the more accurate half. The game compresses decades of climate into about 40 hours. |
| Scale of the rate | A real quarter-of-the-air swing over about half a Mars year (about 340 sols) is roughly 0.07 percent of the air per sol (calc, linear). The game's 2 % per sol is **about 25 to 30 times faster** (calc). That is the same order of compression as the dust rate (about 15 times, dust-review.md). Say this in the journal note. |
| Buried CO2, 0.5 kPa | Source S4 (Phillips 2011) says buried CO2 ice could add up to 80 % to the atmosphere. 0.5 kPa is a **calc** from [S4] and is small against that, which makes it a cautious game value. Also contested: how much would release, and when. Mark **calc, not settled**. |

## Q2. Is scaling the sink with game pace honest?

**Answer: yes, if it is described as time compression, and it should be scaled.** One framing, three facts.

1. Pace is time compression. In classroom mode one sol of play stands for more planet time. Every rate (machines and frost) speeds up together. That is the same simplification as "one sol is 20 minutes", not a different planet.
2. If only the sources were scaled, the stall pressure (0.6 plus source divided by sink rate) would rise with the multiplier. That moves a **physical** result by pace, which breaks the settled rule "physical thresholds are never scaled" (TEAM.md Decision Log, I-1). Scale **both**: the equilibrium stays the same in every mode (calc: S/k unchanged when both are multiplied).
3. The honest cost: the frost responds faster than the real planet in every mode (already about 25 to 30 times in standard, calc above). Classroom mode makes it more so.

**Safe wording**

| Where | Text |
|---|---|
| Journal note (MS) | "This game runs much faster than real Mars. The frost loss runs faster too, so the game takes less time to show you how the poles pull air out of the sky." |
| Educator guide | "Classroom mode speeds up time, not the rules of the planet. Machines and polar frost both speed up together, so the pressure a machine can reach is the same in every mode. Real Mars frost comes and goes over a Mars year (687 Earth days), much slower than in the game." |
| Do not write | "The frost reacts faster in classroom mode" (reads like different physics). "Mars loses X percent of its air each day." |

Mars year length 687 Earth days: **not opened this session** (NASA Mars fact sheet, round 3; treat as verified from round 3 only if the team already cites it).

## Q3. Which curve is "the SME curve table", and does the solo curve match a -2 % sink?

**Identification.** There is no table in `docs/science/` named "SME curve table". The only curves the plan can mean are the balance sheet's **"Standard solo curve" (section 2)** and the Phase 2 and 3 curves (3a). Those were written by the Game Designer around SME numbers. **Recommend the plan row be reworded: "coupling vs the balance-sheet solo curve (section 2)".** The SME has not reviewed any other curve. **Well-structured, Producer.**

**Recomputation (calc).** Model: each sol, change = sources minus 0.02 x (P minus 0.6). Sources: two Ovens 0.02, plus 0.10 per Outgasser; four Outgassers gives 0.42 (balance sheet section 2). For each table segment I take the average pressure, compute the sink, and recover the implied source:

| Segment (sol) | P start to end | Change per sol | Mean P | Sink at -2 % | Implied source | Matches 0.42? |
|---|---|---|---|---|---|---|
| 16 to 18 | 1.93 to 2.71 | 0.390 | 2.32 | 0.034 | 0.424 | Yes |
| 18 to 20 | 2.71 to 3.46 | 0.375 | 3.09 | 0.050 | 0.425 | Yes |
| 20 to 22 | 3.46 to 4.18 | 0.360 | 3.82 | 0.064 | 0.424 | Yes |
| 22 to 24 | 4.18 to 4.87 | 0.345 | 4.53 | 0.079 | 0.424 | Yes |
| 24 to 25 | 4.87 to 5.20 | 0.330 | 5.04 | 0.089 | 0.419 | Yes |

**Result: the late part of the curve (sols 16 to 25, all four Outgassers running) is consistent with a -2 % sink and a 0.42 source to within 0.005 kPa per sol. It already includes the sink.** Without a sink the same segments would climb at a flat 0.42 per sol, so the table's "slows near 5 kPa because frost loss grows" is true by the arithmetic. At 5 kPa the sink is about 0.088 kPa per sol, about a fifth of the source.

**Cells I could not reproduce (unverified).** A plain run of the model with the stated build order (first Outgasser sol 9, 2 by 11, 3 by 13, 4 by 16, no power limits, no storm) gives sol 25 = 5.83 kPa with the sink (6.60 without), not 5.20, and sol 18 = 3.43, not 2.71. The table is lower. The gap comes from the sol 14 to 16 storm and an assumed power share and exact build timing, none of which the sheet states per sol. So the **early and middle cells (sols 1 to 16) are not reproducible from the text**. I did not find an error; I cannot confirm them either. The Technical Lead's prototype run is the real check. Also unverified: Phase 2 and 3 tables (3a), which I did not recompute.

**Stall check (calc, supports Producer gap 10).** Equilibrium without a temperature source = 0.6 + S / 0.02. One full Outgasser, no Oven: 5.6 kPa. One Outgasser plus two Ovens: 6.6. A single Outgasser on 75 % power, no Ovens: 4.35, **below the 5 kPa gate**. This is a real design consequence, and it is the physical meaning of "a thin atmosphere keeps leaking to the poles". It is also a pacing risk for a 12-year-old; the Game Designer decides (balance-sheet question 4).

## Q4. Read-more sentence for the frost cause line

Cause line (already approved, round2 1.2): "Polar frost: some CO2 freezes onto the poles as dry ice."

**Read-more, middle school (grade 6 to 7; about 30 words).**
"On real Mars, about a quarter of the air freezes onto the poles each winter. In spring the dry ice turns back into gas, so the air pressure goes back up."

**High school depth (add after the line above).**
"The thicker the air, the warmer the poles can be and still trap CO2. In the game the frost takes air away for good while Mars is cold. Real Mars gives it all back each year."

**Reading level.** Short sentences, words a grade 6 student knows except "dry ice" (explained as frozen CO2 in the cause line; keep "dry ice" and avoid "sublimate"). Not checked against a readability tool and not checked with the Instructional Designer. Content-rule checks (`docs/learning/content-rules.md`, CR-F9, CR-F10) **not run by me**: Instructional Designer to confirm.

**Accuracy flags.** "About a quarter" is supported by N4 and round 2 [S3] and is consistent with N1's "about 30 percent"; keep "about". Do not use "25 percent" as a bare number. "Gives it all back" is true for the seasonal part only. Do not extend it to the buried CO2 [S4].

## What I could not verify

- The exact seasonal fraction (25 versus 30 percent, and which quantity): sources differ; kept as "about a quarter, up to 30 percent".
- Direct pages at science.nasa.gov and usgs.gov (blocked); results are search summaries.
- CO2 frost-point formula (still unverified since round 2).
- Mars year of 687 Earth days (not opened today).
- Early cells of the solo curve and all of the Phase 2 and 3 tables.
- Whether a warmer, thicker Mars would lose air to its poles (open research question; keep "scientists still study this").
