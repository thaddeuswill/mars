# Science Review, Round 3

Owner: Lead Subject Matter Expert. Reviewed: 2026-11-26. Backlog: #16.
Builds on: [accuracy-review.md](accuracy-review.md) (**AR**), [round2-review.md](round2-review.md) (**R2**), [ngss-verification.md](ngss-verification.md) (updated this round), [external-review-pack.md](external-review-pack.md) (new this round).
Answers: Lead Game Designer round 2 (CO2 budget; crops, biome chain, mirror, Training Hall line, greenhouse read-more, fire edge), Lead Instructional Designer round 2 (FI-1 to FI-4, Shift Briefings, worked-example reasons, delayed post-test, CTA scenarios, F4a puddle-drying time, debrief 2 graphs), Lead Artist round 2 (dosimeter background ticks, contact sound), and the coordinator's sourcing list.

**Perspective round (my lens).** This round succeeds if the Phase 3 carbon numbers obey conservation of mass (a game that makes oxygen out of nothing teaches a false idea about A5), and every number a student can read has a tag.

## How this was checked

| Host or method | Result |
|---|---|
| nextgenscience.org | **Opened.** All 24 PE pages downloaded; word-for-word check done (ngss-verification.md, round 3 section) |
| nssdc.gsfc.nasa.gov (Mars and Earth fact sheets) | **Opened.** Mars fact sheet updated 19 May 2025; Earth fact sheet updated 15 November 2024 [F1][F2] |
| pds-imaging.jpl.nasa.gov | Reachable (HTTP 200); not used this round (pixel sampling is still an open R2 item, now possible) |
| jpl.nasa.gov, science.nasa.gov, photojournal, gml.noaa.gov, data.giss.nasa.gov, ipcc.ch, climate.nasa.gov, ntrs.nasa.gov | **Still blocked** after three tries spaced over the session. Facts from them come through web-search summaries and are tagged "searched, not opened" |
| Web search | Everything else; each source listed in §9 |
| My own calculations | Tagged **[calc]**; method in §10 |

**Number tags (PROCESS.md §1).** Every number below carries one tag: **[sourced: Fx/Rx]** (source opened or confirmed through search; see §9), **[calc]** (my arithmetic on sourced inputs), **[invented]** (a game balance choice, with reasoning), **[unverified]** (from memory or a source I could not confirm; never put in game text).

---

## Summary

| # | Item | Verdict |
|---|---|---|
| 1 | Phase 3 CO2 budget | **Ill-structured.** Recommend option A: **CO2 is the oxygen feedstock.** Plants turn CO2 into O2 one molecule for one molecule; Phase 3 starts with about 18 kPa of CO2 [calc]; the rest of the pressure is the labeled filler gas (§1) |
| 2 | Crop list (potato, lettuce, dwarf wheat, tomato) | **Accurate** |
| 3 | Biome succession chain | **Accurate in order**; timescale and one threshold need labels |
| 4 | Orbital mirror framing | **Accurate as a real proposal**, with a size and a caveat |
| 5 | Training Hall "−60 °C vs. about 15 °C" | **Accurate**, now from the NASA fact sheets |
| 6 | Greenhouse read-more | **Accurate** |
| 7 | Fire edge at 23 % O2 at 75 kPa | **Acceptable, slightly conservative** |
| 8 | "About 1.1 °C" Earth warming | **Sourced** (IPCC AR6, searched, not opened): approve "about 1.1 °C" |
| 9 | Permafrost carbon | **Sourced**: about 1,460–1,600 billion tonnes; "about twice the carbon in the air" |
| 10 | Light delay 3–22 min | **Sourced** (calc from the opened fact sheet: 3.0 and 22.3 min) |
| 11 | Debrief 2 Earth graphs | **Named and cited**; pages still blocked, so not opened. One teacher note on baselines |
| 12 | FI-1 | Key accurate; **stem ambiguous** (dust storms warm the air while cooling the ground) |
| 13 | FI-2 | Key accurate (their 0.5 °C checks: 0.54 °C [calc]). **Premise wrong at equilibrium**: N2 and O2 don't freeze out at the poles |
| 14 | FI-3, FI-4 | Accurate (FI-4 needs the respiration nuance) |
| 15 | Shift Briefings | Accurate; one word change in Briefing 2 |
| 16 | Worked-example reasons | Accurate; reasons drafted for steps 2 and 5 |
| 17 | Delayed post-test items | Science accurate; keys drafted |
| 18 | CTA scenarios | 3 terraforming proposals, 4 Earth side-effect cases, with sources |
| 19 | F4a puddle-drying time below 5 kPa (< 0.25 sol, invented) | **Plausible, with two conditions**; accept 0.02–0.25 sol |
| 20 | Dosimeter background ticks fade to 0 at 30 kPa | **Corrected**: the background never reaches zero, even on Earth |
| 21 | Contact sound through suit and ground stays audible | **Accurate** |
| 22 | NGSS word-for-word check | **Done.** All 21 mapped codes match; one comma fixed in HS-ETS1-3; MS-ESS3-3 now verified |
| 23 | External-review pack | **Built**: [external-review-pack.md](external-review-pack.md) |

---

## 1. Phase 3 CO2 budget (ill-structured; with the Lead Game Designer)

### 1.1 Framing
**Problem.** The game raises pressure from 0.6 to 75 kPa and must end with CO2 ≤ 0.5 kPa and O2 at 21.5–23 %. Nobody has said what the added gas is made of. If it's all CO2, Phase 3 must remove tens of kPa. If it's none, the plants have nothing to make O2 from.

**Why it's ill-structured.** It needs design (pacing, grind risk, route choices), learning (A5, B4, HS-ESS2-6), science (mass balance), and art (the Monitor's carbon view). There are several workable answers, and they trade accuracy against simplicity.

**Disciplines needed.** Lead Game Designer (rule and curves), Lead Instructional Designer (A5, B4 alignment; forecasts F8, FI-4), Lead Artist (Monitor carbon flow, mast gauges), Technical Lead (one more tracked flow), me (mass balance).

### 1.2 Real constraints (my discipline's view)

| Constraint | Value | Tag |
|---|---|---|
| Plants and algae make O2 from CO2 and water. Each CO2 molecule taken in gives out one O2 molecule | 1 : 1 by molecules | Textbook photosynthesis (CO2 + H2O → sugar + O2); **[unverified]** as a citation, but it is standard |
| The O2 is a lasting gain **only if the carbon is locked away** (buried in soil or rock). If the plant matter decays or burns, it takes the O2 back and returns the CO2 | Direction | [sourced: R12] (Earth's O2 rose because organic carbon was buried; the relative role of burial over all of Earth's history is debated [R12]) |
| CO2 needed to make the target O2 at 75 kPa (22 % O2, 0.5 kPa CO2 left over) | About **18.5 kPa** of CO2 before conversion | [calc] (§10) |
| Turning CO2 into O2 lowers total pressure a little, because the carbon leaves the air | About **0.33 kPa** less total pressure per 1 kPa of CO2 converted | [calc] |
| Carbon that must be locked away on Mars to make that O2 | About **1,800 kg of carbon per square meter**, about **260,000 billion tonnes** planet-wide | [calc], using Mars's radius and gravity [F1] |
| For comparison: all the carbon in Earth's air today | About **890 billion tonnes** | [calc] from the Earth fact sheet: air mass 5.1 × 10¹⁸ kg, CO2 420 ppm [F2] |
| Can Mars supply 18 kPa of CO2? | **No.** Accessible CO2 would only about double today's 0.6 kPa (AR [22]); buried polar CO2 adds up to 80 % of today's air (R2 [S4]) | [sourced] |
| Filler gas (about 60 kPa) | Mars's air is 2.59 % N2 of 0.636 kPa, about 0.016 kPa | [sourced: F1], [calc] |
| How long real plants would take | Up to about 100,000 years (AR, McKay; partly verified) | [sourced, partly] |

**What this means.** A real terraformed Mars could only get its O2 the way Earth did: plants splitting CO2, with the carbon buried. The needed carbon is about 300 times the carbon in Earth's whole atmosphere [calc]. The game already labels two things as exaggerated: the size of the gas sources ("Real Mars soil and ice don't hold enough gas") and the filler gas ("real terraformers would have to find some"). Option A below stays inside those two existing labels and adds no new fiction.

### 1.3 Options

| | **A. CO2 is the O2 feedstock (recommended)** | B. Processors make only filler; CO2 stays low | C. All added gas is CO2 |
|---|---|---|---|
| Composition rule | Outgassers: CO2 (plus water). Phase 2 processors: mostly filler gas plus some CO2, sized so CO2 is **about 17–19 kPa at the Phase 3 gate** [calc target; exact value invented]. Phase 3 processors: filler only. Flora and algae vats: turn CO2 into O2 one for one. Decay and fire: turn O2 back into CO2 one for one. Carbon scrubbers: lock CO2 into carbonate with **no O2 made** | Processors add only filler. CO2 never rises far above about 1–2 kPa. O2 must come mostly from **electrolysis of water** (O2 generators scaled up), not plants | Everything added is CO2. Phase 3 must remove about 30 kPa or more, and still add about 45 kPa of filler |
| Accuracy | **High.** Mass balance is right; O2 really comes from CO2; carbon has to go somewhere | **Wrong if plants make the O2** (16 kPa of O2 from 2 kPa of CO2 creates matter). Accurate only if most O2 comes from splitting water, which makes plants decoration | Accurate chemistry, but most of the CO2 must be scrubbed, not breathed into O2 |
| Fun (Lead Game Designer to judge) | A visible, mirrored pair: the CO2 line falls as the O2 line rises. Scrubbers become a real choice ("scrub it and you lose your O2 source") | Simple; no grind; but plants don't matter much to the air | **Grind risk**: a long removal job with a filler climb on top |
| Learning | **A5** (photosynthesis turns CO2 into O2; respiration and decay reverse it), **B4** (scrub vs. keep), **B2** (decay and fire as balancing loops), **HS-ESS2-6** (a quantitative carbon model), HS-LS2-5 | Weak A5; misses the carbon cycle | A5 partly; B4 becomes "remove more" |
| Cost | One more coupling (O2 gain = CO2 loss); a "carbon locked away" reservoir on the Monitor's flow view | Lowest | Bigger machines in Phase 3 |
| Risk | If the player over-scrubs CO2 early, O2 can't reach the window. **Needs a fallback**: the O2 generator (water electrolysis; real) as an expensive route | Teaches that plants make air from nothing, unless labeled | Phase 3 grind (already a listed risk) |

### 1.4 Recommendation and rules for option A

**Recommend A.** It is the only option where the planet's main learning loop (plants turn CO2 into O2) is also physically true, and it turns the scrubber into a B4 trade-off at no extra content cost.

| Rule | Value | Tag |
|---|---|---|
| Flora and algae vats: each 1 kPa of CO2 taken in gives 1 kPa of O2 | 1 : 1 | [sourced as chemistry]; the 1 kPa-for-1 kPa form is a **simplification**: by partial pressure it is close to exact because molecules are swapped one for one |
| Total pressure while converting | Either hold it constant (simplification) or drop it 0.33 kPa per kPa converted | [calc]. Recommend holding it constant at MS depth and naming the drop in HS "read more" |
| Phase 3 CO2 at the gate | 17–19 kPa | [calc] range around 18.5 kPa; exact value **[invented]**, set in the Stage 2 prototype |
| Decay pulse and fire | Return CO2 and remove the same O2 (1 : 1) | Real direction. The balance sheet's decay pulse (CO2 +0.1 kPa/sol) should also take O2 −0.1 kPa/sol. Size **[invented]** |
| Carbon scrubber | Removes CO2 into carbonate; makes no O2 | Real (mineral carbonation, R2 [S19]) |
| Where the carbon goes | Monitor flow view: "in plants", "in soil and buried", "in carbonate rock" | Real reservoirs. A mature biome holds less new carbon (R2 §1.6) |
| Fallback O2 source | O2 generator: splits water into O2 and hydrogen; costly power | Real (electrolysis). MOXIE made O2 from CO2 instead (R2 [S13]). Either is honest |

**Journal (MS):** "Plants take in CO2 and give out O2, one for one. The carbon stays in the plants and the soil. If plants rot or burn, the carbon takes the oxygen back."
**HS read more:** "To make Mars's air breathable this way, about 300 times the carbon in Earth's whole atmosphere would have to be locked in soil and rock. On Earth, oxygen built up over hundreds of millions of years as carbon got buried. The game squeezes that into hours."

**Assumptions to challenge.** (1) The Phase 2 temperature is held by super-greenhouse gases, not by CO2 (already decided), so CO2 falling in Phase 3 doesn't cool the planet in the game. (2) Higher plants can grow while O2 is still low. That is **[unverified]**; the biome chain's pioneer stage (cyanobacteria, which made Earth's first O2) is the honest place for growth to start. (3) 17–19 kPa of CO2 doesn't harm plants. Very high CO2 can harm some plants **[unverified]**; label it as a simplification if it comes up.

**What would settle it.** The Stage 2 paper prototype: run the Phase 3 curve with rule A and check that O2 reaches the window by sol 20 of Phase 3 without a grind.

---

## 2. Game Designer verification items

### 2.1 Crop list (DESIGN.md Content Lists): **accurate**

| Crop | Real space-farming basis | Tag |
|---|---|---|
| Potato | NASA's Biomass Production Chamber at Kennedy ran four potato crops (plus wheat, lettuce, soybean) over about 1,200 days from 1988 | [sourced: R13] |
| Lettuce | 'Outredgeous' red romaine grown in the Veggie chamber on the ISS (VEG-01, VEG-03) | [sourced: R14] |
| Dwarf wheat | USU-Apogee full-dwarf wheat (released 1996 with NASA) grew on the ISS in 2003; Super-Dwarf wheat grew on Mir | [sourced: R15] |
| Tomato | 'Red Robin' dwarf cherry tomatoes in VEG-05 on the ISS, December 2022 to March 2023 | [sourced: R14] |

Journal can say: "All four have been grown for space farming, on the space station or in NASA test chambers."

### 2.2 Biome succession chain: **accurate in order; label time and one threshold**

| Kit | Verdict | Note |
|---|---|---|
| 1 Biocrust (cyanobacteria, lichen) | **Accurate** | Cyanobacteria are the first colonizers of bare soil and bind it; lichens and mosses join as the crust matures; crusts add fertility and water retention [sourced: R16]. They stabilized soils on early Earth before plants [sourced: R16] |
| 2 Tundra meadow (mosses, sedges, cold grasses) on biocrust soil | **Accurate** as a next stage | Standard primary succession order (crust → mosses and grasses → shrubs → trees), textbook level **[unverified]** as a citation |
| 3 Wetland margin (algae mats, reeds) next to water | **Accurate** | — |
| 4 Cold forest needs meadow soil and ≥ 5 °C | **Acceptable simplification** | Trees need a warm-enough growing season; the treeline is tied to growing-season warmth **[unverified]** (no figure checked this round). Keep 5 °C as **[invented]**; don't state it as the real tree limit |
| Timescale | **Label it** | Real primary succession from bare ground to forest takes centuries or more **[unverified]**. Journal: "On Earth this takes hundreds of years. The game speeds it up." |

### 2.3 Orbital mirror framing: **accurate as a real proposal**
- Zubrin & McKay (1993) proposed orbital mirrors of order **100 km radius**, made of solar-sail-like material, about **200,000 tonnes**, to warm the south polar cap and release CO2 [sourced: R17]. They estimated that about **4 K** of polar warming might tip Mars into a warmer state [sourced: R17, via a later review].
- **Caveat:** later work found Mars doesn't hold enough accessible CO2 for that release to do much (AR [22]). In the game the mirror warms the planet directly by adding sunlight, which is real physics.
- **Journal (MS):** "Scientists have proposed giant mirrors in space, about 200 km across, to send extra sunlight to Mars. Nothing that big has ever been built." (200 km = twice the 100 km radius [calc].)
- "No ground signature" is fair: the mirror is in orbit.

### 2.4 Training Hall wall chart: **accurate**
NASA fact sheets: Earth average 288 K (**15 °C**), surface pressure 1014 mb (**101.4 kPa**) [sourced: F2]; Mars average about 214 K (**−59 °C**), 6.36 mb (**0.636 kPa**) [sourced: F1]. Suggested wording: "Average temperature: about 15 °C on Earth, about −60 °C on Mars. Air pressure: 101 kPa vs. 0.6 kPa."

### 2.5 Greenhouse read-more (messages/phase-2.md, `earth.f5.greenhouse`): **accurate**
"Greenhouse gases absorb infrared energy given off by the ground and send some of it back down. Water vapor mostly responds to warming rather than starting it. The ozone hole is a different problem (UV light)." All three sentences are standard (IPCC AR6 WG1, searched, not opened [R2]). Optional HS addition, **sourced**: "Some gases that thinned the ozone layer (CFCs) also trap heat; banning them under the Montreal Protocol helped both" [sourced: R18].

### 2.6 Fire edge at 23 % O2 at 75 kPa: **acceptable, slightly conservative**
- OSHA's 23.5 % "oxygen-enriched" line is defined at sea-level pressure (AR [9]).
- Above about 41 kPa, flammability depends mainly on O2 **percentage**; at lower total pressure, the same percentage burns a little less readily (AR [18]). So at 75 kPa a 23 % edge is a little **stricter** than the sea-level hazard, which is a safe side to err on.
- NASA's planned exploration habitat runs at 56.5 kPa with 34 % O2 and controls materials to manage the risk (AR [4]). That shows the edge is a design choice, not a law of nature.
- **Journal (HS read more):** "At lower pressure, a bit more oxygen percent is needed for the same fire risk. Our 23 % line plays it safe."

---

## 3. Sourced or marked: the coordinator's list

| Claim | Status | Approved wording | Source |
|---|---|---|---|
| "About 1.1 °C" Earth warming | **Sourced** (searched; ipcc.ch blocked, not opened) | "Earth has warmed about 1.1 °C since the late 1800s, mainly because of human activity." | IPCC AR6 WG1 SPM: 2011–2020 was 1.09 °C [likely range 0.95–1.20] above 1850–1900; human activities have "unequivocally caused global warming" [R1] |
| Permafrost carbon | **Sourced** | "Frozen Arctic soils hold about twice as much carbon as the air." | 1,460–1,600 billion tonnes (NOAA Arctic Report Card, citing Hugelius et al. 2014 and Schuur et al. 2015); Schuur et al. 2015 gave 1,330–1,580 [R3]. Air holds about 890 billion tonnes [calc from F2] |
| Light delay 3–22 min one way | **Sourced** | "Messages take 3 to 22 minutes to cross, depending on where the planets are." | Earth–Mars distance 54.6 to 401.4 million km [sourced: F1]; ÷ speed of light = 3.0 and 22.3 min [calc] |

### 3.1 Debrief 2 Earth graphs (MS-ESS3-5 condition)

| Graph | Source to use | What it shows | Status |
|---|---|---|---|
| Atmospheric CO2 | NOAA Global Monitoring Laboratory, "Trends in Atmospheric Carbon Dioxide": Mauna Loa monthly mean, https://gml.noaa.gov/ccgg/trends/ | The longest direct CO2 record, started by C. D. Keeling (Scripps) in 1958; monthly and annual data as text or CSV [R4] | **Named and cited; not opened** (host blocked) |
| Global temperature | NASA GISS Surface Temperature Analysis (GISTEMP v4), https://data.giss.nasa.gov/gistemp/ | Global surface temperature anomaly, relative to 1951–1980 [R4] | **Named and cited; not opened** |

**Teacher note (required in the educator guide):**
- **Baselines differ.** GISTEMP anomalies are measured from the 1951–1980 average [R4]; the IPCC's "about 1.1 °C" is measured from 1850–1900 [R1]. So the graph's latest value won't match "1.1 °C". Students who notice should be praised, not corrected.
- **The CO2 graph starts in 1958.** "Past century" (the PE wording) needs earlier data. Use an ice-core-plus-Mauna-Loa graph for the full century **[unverified]**: I couldn't find or open one this round, so the source still needs to be named.
- The PE's clarification statement names exactly this evidence: "graphs, and maps of global and regional temperatures, atmospheric levels of gases such as carbon dioxide" (verified word for word, ngss-verification.md).

**Still open:** someone must open both pages and save dated copies of the graphs before the educator guide ships (network: gml.noaa.gov, data.giss.nasa.gov).

---

## 4. Instructional Designer items

### 4.1 Interleaved forecasts FI-1 to FI-4

| ID | Key | Verdict | Fix |
|---|---|---|---|
| FI-1 | Warming slows during a storm | **Key accurate** for the warming the emitters add (less power → less gas made) | **Stem is ambiguous.** Real Mars dust storms **heat the air** (dust absorbs sunlight) while cooling the ground by day (AR, Mars climate; **[unverified]** as a citation this round). "Warming speeds up" could then be defended. Reword: "A dust storm arrives in about a sol. Your emitters run on solar power. What happens to the warming they add while the storm lasts?" |
| FI-2 | Boiling point drops a little | **Key accurate.** Their 0.5 °C checks out: 1.5 kPa less at 75 kPa lowers the boiling point by 0.54 °C; at 30 kPa by 1.2 °C [calc] | **Premise needs a fix.** A "polar winter" that drops pressure works by freezing CO2 onto the poles. At equilibrium CO2 is ≤ 0.5 kPa, and nitrogen and O2 don't freeze out at Mars temperatures, so a 1.5 kPa polar dip is **wrong** late in Phase 3. Options: (a) fire FI-2 early in Phase 3, while CO2 is still high (under option A, about 18 kPa); (b) at equilibrium, cause the dip with a **passing low-pressure weather system** (real on Earth: storms lower pressure by a few kPa; **[unverified]** size). Recommend (b), which also fixes the balance sheet's "Polar winter" drift event at equilibrium |
| FI-3 | Much less radiation | **Accurate.** Air overhead at 30 kPa is about 800 g/cm², at 75 kPa about 2,000 g/cm², vs. about 17 g/cm² at the start and about 1,030 on Earth [calc from F1, F2] | Helmet-off question: safe to say "thick air now blocks most of it." **Don't** say "as safe as Earth": Mars still has no global magnetic field, and I found no model of surface dose under a thick Mars atmosphere **[unverified]** |
| FI-4 | CO2 rises | **Accurate** with one nuance | Plants and algae give off CO2 all the time; in low light they take in less than they give off, so CO2 rises (R2, F8 nuance). Under option A, O2 also dips. Explanation: "Less light, less CO2 taken in. The algae still breathe out CO2." |

### 4.2 Shift Briefing science lines

| Briefing | Line | Verdict |
|---|---|---|
| 1 | "Dust storms cut solar power." | **Accurate** (R2 [S12]) |
| 2 | "Watch pressure and temperature together: ice needs both to become water." | **Accurate, sharpen one word**: "ice needs both to **stay** water." Below the triple point it can't melt at all; just above, it melts but boils or dries away (R2 §1.1) |
| 2 | "Warm machines change the ground near them." | **Accurate** (ground collapse, R2 §1.4) |
| 3 | "Plants add O2 and take in CO2. … too little and you can't breathe, too much and fires start." | **Accurate.** Under option A, consider: "Plants turn CO2 into O2." |

### 4.3 Reasons in the worked-example steps (messages/phase-1.md §4)

| Step | Current reason | Verdict / suggested reason |
|---|---|---|
| 1 | "Short cables lose less power." | **Accurate** (resistance grows with length; textbook) |
| 2 | "It bakes gas out." | **Accurate in kind** (heating Mars soil releases CO2 and water, R2 §2.5). Suggested: "Heat drives gas and water out of the dirt." |
| 3 | "Can your battery carry it overnight?" | **Accurate** (no sun at night) |
| 5 | (needs a reason) | Suggested: "The Monitor lists every cause of change, so you can see this machine's share." (not a science claim) |
| Processor 1 | "Keep it off ice." | **Accurate**: a warm machine on ground ice can make the ground sink (R2 §1.4). Suggested reason: "Its heat can melt the ice underneath." |

### 4.4 Delayed post-test items (LEARNING.md §3.6): science check and keys

| # | Science check | Key notes |
|---|---|---|
| 1–4 | Same as the post-test anchors; check those items when written | — |
| 5 | Earth lake or farm link diagram | Accept links with correct direction, e.g. fertilizer → algae growth (+); algae → water O2 at night (−) **[unverified]** as a citation; textbook eutrophication |
| 6 | Loop type in a new Earth example | Good sourced candidates: ice–albedo (reinforcing; the HS-ESS2-2 clarification statement itself uses it, verified); wetland loss reduces humidity, which shrinks wetlands more (reinforcing; same clarification statement, verified) |
| 7 | "21 % O2 is always breathable" | **False** statement; correct answer turns on O2 partial pressure (AR B2). Distractor idea: "true, because Earth's air is 21 %" |
| 8 | "Water always boils at 100 °C" | **False**; it boils lower on mountains (Everest about 72 °C [calc, AR]) |
| 9 | Wetland-drain scenario | **Accurate and well chosen**: HS-ESS2-2's clarification statement names wetland loss as a feedback example (verified). Rubric anchor for a "4": names the humidity–wetland loop or the loss of a carbon store (wetland soils store carbon **[unverified]** this round) |

### 4.5 F4a puddle-drying time below 5 kPa (invented: must be under 0.25 sol)

**Facts.** Lab tests at 7 mbar (0.7 kPa) CO2 and about 0 °C measured pure water evaporating at about 1 mm/h (0.73 mm/h after correcting to Mars gravity) [sourced: R5]. A game sol stands for one Mars day, 24.66 h [sourced: F1], so 0.25 sol ≈ 6.2 h [calc].

| Case (below 5 kPa) | What real water does | Gone within 0.25 sol? |
|---|---|---|
| Warmed above the boiling point (0 °C at 0.61 kPa up to 33 °C at 5 kPa) | Boils away at once (R2 §1.1) | Yes |
| Melted, then left without heat | Refreezes quickly in −60 °C surroundings **[unverified]** speed; certain in direction | Yes, as ice (no liquid) |
| Thin film (≤ 1–2 mm), kept just above 0 °C | About 1–3 h at 0.7–1 mm/h [calc] | Yes |
| 1 cm puddle, kept just above 0 °C | About 8–14 h (0.34–0.56 sol) [calc] | **No** |
| Puddle held warm (below boiling) at 2–5 kPa | Evaporation slows as pressure rises **[unverified]** size | **No** |

**Verdict: plausible, with two conditions.**
1. The F4a outcome check must count **frozen** water as "no liquid water" (it already says "no liquid water at that spot"). Keep it that way.
2. Puddles in the 0.611–5 kPa band are **thin** (a film, not a 1 cm pool), and the game never states a drying time in text.

**Range I accept: 0.02–0.25 sol** [invented], ideally **growing with pressure** (direction: drying slows as pressure rises). The balance sheet's start value of 0.05 sol is inside it. One edge case: a heater holding a puddle between 0 °C and the boiling point at 2–5 kPa would really last longer than 0.25 sol. That's acceptable as a simplification; label it if a journal line ever describes it.

### 4.6 Real scenarios for docs/learning/cta.md §4

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

---

## 5. Lead Artist audio items (docs/audio/audio-direction.md)

| Item | Verdict | Reason and fix |
|---|---|---|
| §6.2 Background dosimeter ticks: 0.1/s below 5 kPa, **fading to 0 at 30 kPa**; none inside | **Corrected** | The direction is right: thicker air blocks cosmic rays. Today's Mars surface dose is about 0.64–0.67 mSv per day [sourced: R8]. But background radiation **never reaches zero**, even on Earth: natural radiation gives about 2.4–3.0 mSv per year, from cosmic rays (about 0.3–0.4 mSv/yr at sea level) and from rocks and soil [sourced: R9]. **Fix:** fade to a small floor (e.g. one tick every 10–30 s outside; a little less inside), never silence. The floor value is **[invented]**. Journal (HS): "Even on Earth a dosimeter ticks now and then. Rocks and space both give off a little radiation." |
| §2 Contact sound (footsteps, tools, grains on the visor) stays audible in thin air | **Accurate** | Sound travels through solids and through the suit's own air regardless of the outside pressure. Suits are pressurized (NASA's EMU runs at about 29.6 kPa, 4.3 psi) [sourced: R10], so sound inside the helmet behaves normally. Footsteps reach the ear through the boots and body. The "through the body" path is standard acoustics; no Mars-specific source found **[unverified]** as a citation. Keep it, labeled plausible |

---

## 6. NGSS word-for-word check

Done. See [ngss-verification.md](ngss-verification.md), "Round 3". Results:
- All 21 codes in LEARNING.md §6.1 and `ngss_map.example.json` match the official text. One punctuation fix: HS-ETS1-3 has no comma after "aesthetics".
- MS-ETS1-3, MS-ETS1-4 and HS-ETS1-1 to -4 moved from "partly verified" to verified.
- MS-ESS3-3 is now verified; it could be **related** for B5 with an Earth example (ID's call).
- Two assessment boundaries matter: MS-ESS1-3 rules out recall (supports the A1 data task); HS-ESS3-6 is limited to using published model results, so the debrief's published Earth data, not the game, carries it.
- HS-ESS2-2's clarification statement uses ice–albedo **and** wetland loss as its examples: a near-exact fit for B2 and the delayed post-test item 9.

---

## 7. Updates to earlier SME files

- R2 §10.1's "unverified" list: the light delay, 1.1 °C, and permafrost carbon are now sourced (§3). Earth mean 15 °C (AR) is now sourced [F2].
- AR used Mars gravity 3.71 m/s²; the fact sheet gives 3.73 (mean) [F1]. Air-column numbers change by under 1 % (e.g. 75 kPa → 2.0× Earth's column, unchanged when rounded) [calc].
- AR gave Mars O2 as 0.145 % (Curiosity, AR [8]). The current fact sheet gives 0.16 % [F1]. Both round to the game's 0.1 %. If the journal quotes a number, use "about 0.15 %".

---

## 8. External review

Built as a separate file: [external-review-pack.md](external-review-pack.md). Three reviewer packets (planetary scientist, climate modeler, science teachers), each with the claims list, sources, and the questions to ask.

---

## 9. Sources

**Opened this round**
- [F1] NASA NSSDCA, Mars Fact Sheet (D. R. Williams, updated 19 May 2025): https://nssdc.gsfc.nasa.gov/planetary/factsheet/marsfact.html
- [F2] NASA NSSDCA, Earth Fact Sheet (updated 15 November 2024): https://nssdc.gsfc.nasa.gov/planetary/factsheet/earthfact.html
- NGSS PE pages: https://www.nextgenscience.org/pe/ (24 pages; list in ngss-verification.md)

**Through web search (pages not opened)**
- [R1] IPCC AR6 WG1 Summary for Policymakers (2021), A.1 and headline statements: https://www.ipcc.ch/report/ar6/wg1/chapter/summary-for-policymakers/ ; https://www.ipcc.ch/report/ar6/wg1/resources/spm-headline-statements
- [R2] Same report, for greenhouse mechanism and water-vapor feedback (not specifically checked; standard content)
- [R3] NOAA Arctic Report Card, permafrost carbon (T. Schuur): https://arctic.noaa.gov/?p=629 ; Schuur et al. (2015), "Climate change and the permafrost carbon feedback", *Nature* 520:171–179: https://pubs.usgs.gov/publication/70169233
- [R4] NOAA GML, Trends in Atmospheric Carbon Dioxide: https://gml.noaa.gov/ccgg/trends/ ; NASA GISS, GISTEMP v4: https://data.giss.nasa.gov/gistemp/
- [R5] Sears, Chittenden, Moore et al., evaporation of water and brine under Mars conditions (2004–2006): https://ntrs.nasa.gov/citations/20040055914 ; https://dsears.hosted.uark.edu/publications/pub%20by%20year/2006%20papers/chittenden%20et%20al%202006.pdf
- [R6] Wordsworth, Kerber & Cockell (2019), "Enabling Martian habitability with silica aerogel via the solid-state greenhouse effect", *Nature Astronomy* 3:898–903: https://arxiv.org/abs/1907.09089v1
- [R7] National Museum of Australia, "Introduction of cane toads": https://www.nma.gov.au/defining-moments/resources/introduction-of-cane-toads
- [R8] Hassler et al. (2014), "Mars' surface radiation environment measured with the Mars Science Laboratory's Curiosity rover", *Science* 343; SwRI release: https://swri.org/node/9079 ; https://www.space.com/23875-mars-radiation-life-manned-mission.html
- [R9] Natural background radiation: Canadian Nuclear Safety Commission fact sheet; UN press release on the UNSCEAR public-exposure evaluation (about 3.0 mSv/yr): https://www.cnsc-ccsn.gc.ca/eng/resources/fact-sheets/natural-background-radiation/ ; https://unis.unvienna.org/unis/en/pressrels/2026/unisous453.html
- [R10] NASA EMU operating pressure (4.3 psi): https://ntrs.nasa.gov/api/citations/20240004074/downloads/SHOP%20-%20Variable%20Pressure%20Suit_v2.pdf
- [R12] Organic carbon burial and atmospheric O2: Krissansen-Totton et al. (2015): https://faculty.washington.edu/dcatling/Krissansen-Totton2015_Carbon_Isotopes_Geologic_History.pdf ; https://www.ncbi.nlm.nih.gov/pmc/articles/PMC8359855/
- [R13] Wheeler et al., "NASA's Biomass Production Chamber: a testbed for bioregenerative life support studies": https://ntrs.nasa.gov/citations/19900016140 ; Wheeler (2017), "Agriculture for space": https://sciences.ucf.edu/class/wp-content/uploads/sites/23/2017/02/Wheeler_AgricultureForSpace_PeoplePlaces_OpenAgriculture_2017.pdf
- [R14] NASA VEG-05 and Veggie: https://ntrs.nasa.gov/citations/20240001505 ; https://ntrs.nasa.gov/citations/20190028955
- [R15] Utah State University, dwarf crops for space flight (USU-Apogee): https://qanr.usu.edu/labs/cpl/research/dwarf-crops ; https://digitalcommons.usu.edu/cpl_dwarfcrops/10
- [R16] USGS, Biological Soil Crust (biocrust) science: https://usgs.gov/centers/southwest-biological-science-center/science/biological-soil-crust-biocrust-science ; https://www.usgs.gov/publications/biological-soil-crusts-soil-stabilizers
- [R17] Zubrin & McKay (1993), "Technological requirements for terraforming Mars", AIAA 93-2005: https://marspapers.org/paper/Zubrin_1993_3.pdf
- [R18] WMO/UNEP Scientific Assessment of Ozone Depletion 2022, executive summary: https://csl.noaa.gov/assessments/ozone/2022/executivesummary/ ; https://wmo.int/news/media-centre/ozone-layer-recovery-track-helping-avoid-global-warming-05degc

## 10. Calculations

- **CO2 needed for O2 at 75 kPa.** Final mix: 22 % O2, 0.5 kPa CO2, rest N2 (molar masses 32, 44, 28). Mean molar mass ≈ 29.0. Undo the conversion molecule for molecule (O2 back to CO2): mean molar mass ≈ 31.6, total ≈ 81.8 kPa, CO2 ≈ 18.5 kPa, filler ≈ 63.3 kPa. Each 1 kPa of CO2 converted lowers total pressure by about 0.33 kPa (12 g of carbon leaves per mole).
- **Carbon to lock away.** Column moles at 75 kPa = 75,000 / (3.73 × 0.0290) ≈ 6.9 × 10⁵ mol/m²; 22 % of that, × 12 g ≈ 1,830 kg C/m²; × Mars area 4π(3,389.5 km)² ≈ 1.44 × 10¹⁴ m² ≈ 2.6 × 10¹⁷ kg ≈ 260,000 billion tonnes. Inputs: radius and gravity [F1].
- **Carbon in Earth's air.** 5.1 × 10¹⁸ kg × 420 × 10⁻⁶ × 12 / 28.97 ≈ 8.9 × 10¹⁴ kg ≈ 890 billion tonnes [F2]. Ratio ≈ 300.
- **Light time.** 54.6 × 10⁶ km ÷ 299,792 km/s = 182 s = 3.0 min; 401.4 × 10⁶ km → 1,339 s = 22.3 min [F1].
- **Air column.** P / g: 30 kPa → 804 g/cm²; 75 kPa → 2,011 g/cm² (Mars g 3.73 [F1]); Earth 101.4 kPa / 9.82 → 1,033 g/cm² [F2].
- **Boiling point dips (FI-2).** Antoine, as in AR: 75 → 73.5 kPa: 91.81 → 91.27 °C (0.54 °C); 30 → 28.5 kPa: 69.18 → 68.01 °C (1.17 °C).
- **Puddle drying.** 10 mm ÷ (0.73 to 1.2 mm/h) = 8.3 to 13.7 h = 0.34 to 0.56 sol (sol = 24.66 h [F1]); 1–2 mm films: 1–3 h.
