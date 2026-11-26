# Science Review, Round 2: Answers to Open Handoffs

Owner: Lead Subject Matter Expert. Reviewed: 2026-11-26.
Builds on: [accuracy-review.md](accuracy-review.md) (round 1, cited here as **AR**), [ngss-verification.md](ngss-verification.md).
Answers handoffs from: Lead Game Designer (DESIGN.md §1–3, balance sheet §2, §7, §8, story bible §2, §3, §5, §6.5, §8.3), Lead Artist (planetary-keyframes.md, style guide §9.3, §12, asset list), Lead Instructional Designer (prompts-and-reflections.md, rubrics.md, ngss_map.example.json), plus pressure-driven sound and the weather mast.

**Perspective round (my lens).** This succeeds if nothing the player sees, hears, or answers teaches a false idea, and every simplification is either invisible to learning or labeled in the journal. I check against the Accuracy row of the Success Scorecard (TEAM.md 8.5): zero open issues rated "wrong".

## How this was checked

| Method | Used for |
|---|---|
| Web search (search-engine summaries of the papers and agency pages listed in Sources) | Most facts. Each source is listed with its URL |
| Direct page or file access | **Not possible.** The session proxy refused every host I tried (403 "no rule allows this host" or DNS failure): jpl.nasa.gov, science.nasa.gov, photojournal.jpl.nasa.gov, nssdc.gsfc.nasa.gov, pds-imaging.jpl.nasa.gov, pds-rings.seti.org, pancam.sese.asu.edu, nextgenscience.org, pmc.ncbi.nlm.nih.gov, commons/upload.wikimedia.org, elib.dlr.de, Caltech authors library. I did not retry after the refusals |
| Pixel sampling of calibrated rover images | **Not done.** No image host was reachable. Published values are given instead (section 2.11) |
| My own calculations | Boiling points, CO2 frost points, air densities, air columns, light times, ice–albedo size. Method in section 8. Marked "(calc)" |
| My own knowledge, source not re-opened this session | Marked "**not opened**". Treat as well established but not re-checked |

Verdict key (same as AR): **Accurate** · **Acceptable simplification** (fine for play; label it where the journal touches it) · **Wrong** (teaches something false; fix before it ships).

---

## Summary

| # | Item | Verdict | Fix owner |
|---|---|---|---|
| 1 | Ice-to-water rule: < 0.61 kPa sublimates | Accurate | — |
| 2 | Ice-to-water rule: 0.61–5 kPa "boils or evaporates within seconds" at any temperature above 0 °C | **Wrong in part**: water only boils if it is warmer than the boiling point at that pressure (0 °C at 0.61 kPa, 33 °C at 5 kPa) | Lead Game Designer |
| 3 | Phase 1 "ice vanishes into vapor" and forecast **F4a** trigger ("pressure below the triple point") | **Design-physics conflict**: pressure passes 0.611 kPa on about sol 1 (balance sheet §2), so the sublimation regime is almost never seen and F4a almost never fires | Lead Game Designer, Lead Instructional Designer, Lead Artist |
| 4 | Ice-to-water rule: ≥ 5 kPa and > 0 °C, water stays; ≤ 0 °C stays ice | Acceptable simplification | — |
| 5 | Polar frost loss (direction, stop above −20 °C) | Acceptable simplification; wording fix | Lead Game Designer |
| 6 | Ice–albedo feedback | Real loop, but **small on Mars** (my estimate under 1 °C); cap it at about 2 °C | Lead Game Designer (ill-structured: appeal vs. accuracy) |
| 7 | Ground collapse framing | Accurate (Earth and Mars) | — |
| 8 | Perchlorate in soil and meltwater | Accurate | — |
| 9 | "Mature ecosystems are nearly carbon-neutral" | Acceptable simplification, but **contested**; reword | Lead Game Designer |
| 10 | Story bible [verify] tags (17) | 14 accurate; 3 need a wording change | Lead Game Designer |
| 11 | Sky path tan → rose → lavender → blue; stylized rose | Acceptable simplification (hue direction plausible; saturation exaggerated). Does not teach a false idea if the journal says nobody has seen a thickened Mars sky | Lead Artist (note only) |
| 12 | 75 kPa air column "1.9× Earth's" | Accurate; 2.0× is closer (calc 1.96×) | Lead Artist |
| 13 | Equilibrium sky "deeper blue at the zenith" | Acceptable simplification; physics points to a **paler, whiter** blue with twice Earth's air | Lead Artist (optional) |
| 14 | Flame bands by O2 fraction | **Wrong** below 16 %: common fuels mostly can't keep burning below about 14–17 % O2 even at sea-level pressure | Lead Artist |
| 15 | Outgasser vapor puffs | Accurate | — |
| 16 | Vapor wisps over water vs. pressure | Acceptable simplification; tie heavy wisps to the boiling point | Lead Artist |
| 17 | Windsock physics | Accurate | — |
| 18 | Sounding balloon | Acceptable simplification (label it a lift balloon) | Lead Artist |
| 19 | Aurora glow | Color and trigger accurate (Perseverance, 2024 event, published 2025). **Fading it with pressure is wrong**; and it should cover the whole sky, not only the horizon | Lead Artist |
| 20 | Carbonate deposits at scrubbers | Accurate | — |
| 21 | Salt crust | Acceptable simplification; wording given | Lead Artist |
| 22 | Phoenix "Dodo-Goldilocks" ice cited as the basis for below-triple-point sublimation | **Wrong use of a right fact**: Phoenix's pressure was 724–860 Pa (above the triple point) and the ice was below 0 °C. It shows ice sublimating into cold, dry air, not the effect of low pressure | Lead Artist |
| 23 | Art citations (13 checked) | 12 confirmed; the aurora one now has its paper | Lead Artist |
| 24 | Forecasts F1–F10 | All correct answers accurate. **Three distractors are partly true** (F1, F4a, F9); F6 and F8 explanations need one nuance; F4a and F7 triggers need changes | Lead Instructional Designer |
| 25 | Earth links (9 rows) | Accurate; approved lines given | Lead Instructional Designer, Lead Game Designer |
| 26 | Transfer task answer key | **Didn't exist**; drafted here (section 3.3) | Lead Instructional Designer |
| 27 | C1/C2 ETS split | Acceptable, with two changes | Lead Instructional Designer |
| 28 | Permafrost and sublimation examples | Accurate; better everyday examples given | Lead Instructional Designer |
| 29 | Pressure-driven ambient sound | Accurate | — |
| 30 | Weather mast instruments | Plausible; three fixes (spirit thermometer, gauge not "colored gas", add a barometer) | Lead Artist, Lead Game Designer |

**Open "wrong" items: 6** (rows 2, 3, 14, 19, 22, and three distractors in row 24 counted as one). All have a concrete fix below. None needs a settled decision reopened.

---

## 1. Lead Game Designer items

### 1.1 Ice-to-water rule (DESIGN.md §2)

**Facts**
- Triple point of water: 0.611657 kPa, 0.01 °C (AR [14]). Below it, ice can't melt; it sublimates.
- Above the triple point, liquid water can exist between 0 °C and the **boiling point at that pressure**. Boiling points (calc, Antoine): 0.7 kPa → 2 °C; 1 kPa → 7 °C; 2 kPa → 18 °C; 3 kPa → 24 °C; 4 kPa → 29 °C; 5 kPa → 33 °C.
- Water that is liquid but below its boiling point doesn't boil. In thin, dry air it **evaporates fast** and cools itself, so it may also refreeze (Haberle et al. 2001, AR [15]).
- Exposed ice also sublimates **below 0 °C** into dry air. Phoenix saw ice chunks vanish over 4 days (sols 20–24, June 2008) at pressures of 724–860 Pa, above the triple point [S1][S2].
- Real Mars's mean pressure (0.636 kPa) sits **just above** the triple point; high ground is below it, low ground above it (AR [1]). The game's start value (0.6 kPa) is just below it.

**Verdicts on the draft rule**

| Row | Draft | Verdict |
|---|---|---|
| < 0.61 kPa, any warmth | Sublimates, no water | **Accurate** |
| 0.61–5 kPa, > 0 °C | Melts, then "boils or evaporates away within seconds" | **Wrong in part.** At 3 kPa, water at 10 °C does not boil (boiling point 24 °C). It would sit as a puddle that evaporates over minutes and may refreeze. Only water warmer than the boiling point boils |
| ≥ 5 kPa, > 0 °C | Water stays while local temperature > 0 °C | **Acceptable simplification.** Real water still evaporates, but slowly enough to pool. 5 kPa is a **game line**, not a physical constant: say "high enough that water no longer boils away at everyday temperatures" |
| Any, ≤ 0 °C | Stays ice | **Acceptable simplification.** Real exposed ice slowly sublimates in dry air (Phoenix). Fine to ignore; mention in HS "read more" |
| Missing | Warm water above the boiling point at ≥ 5 kPa (e.g. 40 °C next to a heater at 5 kPa) | Should boil. The draft says it stays |

**The bigger problem (row 3 of the Summary).** Balance sheet §2 puts pressure at 0.61 kPa on sol 1 and 0.67 kPa on sol 5. So the "< 0.61 kPa" regime lasts about one sol. Phase 1 players will almost always be in the 0.61–5 kPa band. Under the draft rule they see flash puddles, not ice vanishing into vapor. That breaks:
- DESIGN.md §2 ("Phase 1 players see ice vanish into vapor near a heater");
- forecast F4a, whose trigger requires "pressure below the triple point";
- keyframes §3.3 (the "sublimating ice" state is the Phase 1 look).

**Recommended rule** (well-structured; uses the boiling point the Monitor already shows):

| Pressure | Local temperature | What warmed ice does | Player sees |
|---|---|---|---|
| < 0.611 kPa | > 0 °C | Sublimates; no liquid | Ice shrinks, wisps, dry ground |
| ≥ 0.611 kPa | ≥ boiling point at this pressure | Melts and **boils away** at once | Flash puddle, hard steaming |
| 0.611 – 5 kPa | 0 °C to boiling point | Melts into a puddle that **evaporates quickly** and refreezes when it cools. *How quickly: real value missing (depends on wind, humidity, temperature); the game time is an **invented balance number** for the balance sheet* | Short-lived puddle, light wisps |
| ≥ 5 kPa | 0 °C to boiling point | Water stays | Puddle, then pools |
| Any | ≤ 0 °C | Stays ice (simplified) | Ice |

What Phase 1 players then see near a heater (heater well above the boiling point of 2–30 °C): **ice melts and boils off at once, leaving no water.** That is real, it's dramatic, and it is the exact A2 lesson: "in thin air, water boils at a low temperature." The Monitor's boiling-point line explains it on the spot. Ice warmed only a little (just above 0 °C) gives a brief puddle. The pure-sublimation look still shows in the first sol and whenever pressure is reset in "set phase" or debug worlds.

**Journal note (MS):** "The air here is so thin that water boils at just a few degrees above freezing. Melted ice boils away before it can make a puddle."
**HS read more:** "Real Mars's average air pressure is right at water's triple point (0.61 kPa). Low places are a bit above it, high places below it. Either way, water can't last as a liquid there today. Ice also slowly turns straight to vapor in Mars's dry air, even below 0 °C."

### 1.2 Polar frost loss (DESIGN.md §3; balance sheet §2)

**Facts.** About a quarter of Mars's atmosphere (up to about 30 %) freezes onto the seasonal polar caps as CO2 ice each winter and returns in spring; Viking measured the pressure rising and falling with the caps [S3]. The CO2 frost point rises with pressure (calc): about −125 °C at 0.6 kPa, −109 °C at 5 kPa, −92 °C at 30 kPa. A buried CO2 ice deposit in the south polar layered deposits could add up to 80 % to the atmosphere's mass if released [S4].

| Claim | Verdict | Note |
|---|---|---|
| Direction: the thicker the air, the more freezes out | **Accurate.** More CO2 raises the frost point, so polar winters trap more | Applies to CO2 (and water), not to nitrogen or O2 |
| A one-way, steady sink | **Acceptable simplification.** Real seasonal frost comes back each spring. A net loss is defensible as the permanent caps growing while the poles stay cold | The "polar winter" drift event already shows the seasonal part |
| Stops above −20 °C global mean | **Acceptable simplification.** Whether a warmer, thicker Mars would still freeze CO2 out at the poles depends on how much heat the air carries poleward. Real models disagree (**uncertain**; don't state a threshold as fact) | Keep as a game line |
| Balancing loop (B2) | **Accurate** | — |

**Wording for the Monitor cause line:** "Polar frost: some CO2 freezes onto the poles as dry ice." **Journal (MS):** "Every Mars winter, about a quarter of the air freezes onto the poles as dry ice. The thicker the air, the more can freeze out. That pulls your pressure back down." **HS read more:** "Today the frost comes back as gas each spring. Whether a warmer Mars would still lose air to its poles is something scientists are still studying."

**Optional improvement (more accurate and teaches more):** make the polar reservoir **reversible**. Below −40 °C it takes air; above −20 °C it gives back what it took, plus up to about 0.5 kPa from buried polar CO2 ice (derived: [S4] says up to 80 % more atmospheric mass, and 80 % of today's 0.636 kPa ≈ 0.5 kPa; real, and small next to 75 kPa). That turns one sink into a real reinforcing loop on the way up (warmer → CO2 released → warmer) and matches McKay-style terraforming feedbacks. Game Designer's call.

### 1.3 Ice–albedo feedback size (DESIGN.md §1; balance sheet §8)

**Facts.** The feedback is real: bright ice reflects sunlight, dark ground or water absorbs it. On Earth it is large: the Arctic warmed about 3.8 times faster than the globe over 1979–2021 [S5]. **On Mars it is small**, because ice covers only a small part of the surface, mostly near the poles where sunlight is weakest.

**My estimate (calc, order of magnitude).** Mars's mean absorbed sunlight is about 147 W/m² (586 W/m² ÷ 4). If ice covering about 3 % of the surface, at poorly lit latitudes (weighting about 0.5), darkened by 0.4 in albedo, absorbed sunlight would rise by about 0.9 W/m². With no other feedbacks, that warms Mars by about 0.4 °C. Even generous assumptions give **1–2 °C**, out of the 65 °C the game needs.

**Verdict:** a real loop, so including it is **accurate**; making it a big speed-up is **wrong**. Recommendation: cap its total contribution at **about 2 °C**, and let the journal say so. *The 2 °C cap is an **invented balance number**, set as a generous upper bound on my order-of-magnitude estimate; no published value for ice–albedo warming on a terraformed Mars was found (real value missing).*
- **Journal (MS):** "When bright ice melts, the darker ground soaks up more sunlight, so the planet warms a little more. On Mars this is small, because there isn't much ice. On Earth's Arctic it's huge."
- **Earth link (D2):** Arctic warming nearly 4× the global rate [S5].

This is an **ill-structured** trade-off: the fun–learning matrix says "surprising speed-up" (appeal), and the honest size is not surprising. Options are in Handoff H1.

### 1.4 Ground collapse framing (DESIGN.md §3; story bible §3.4a)

| Claim | Verdict | Evidence |
|---|---|---|
| Thawing permafrost damages buildings and roads on Earth today | **Accurate** | About 70 % of current infrastructure in the permafrost region (range 48–87 %) sits where near-surface permafrost is likely to thaw by 2050 [S6] |
| Heated buildings thaw the ground under them; piles avoid it | **Accurate** | Standard Arctic practice: buildings on piles with an air gap; the Trans-Alaska Pipeline's raised supports use heat pipes (**not opened**) |
| Mars has buried ground ice that can be lost, making the ground sink | **Accurate** | Thick ice sheets 1–2 m under the surface at 55–58° latitude, exposed in scarps that are retreating as the ice sublimates [S7]. Scalloped pits in Utopia Planitia are read as ground that slumped as buried ice sublimated (or, in another view, melted) [S8] |
| Speed | **Acceptable simplification** | Real collapse takes seasons to years; the game makes it fast |
| "Polygon cracks" warning look | **Accurate** | Polygonal ground is common over Mars ground ice (e.g. Utopia Planitia [S8]) |

**Journal (MS):** "Under the dust is ice. Warm it, and the ice melts or turns to vapor, and the ground drops. On Earth, thawing frozen ground is cracking roads and houses in the Arctic today."

### 1.5 Perchlorate (DESIGN.md §3; story bible §3.4a)

| Claim | Verdict | Evidence |
|---|---|---|
| Mars soil holds perchlorate salts | **Accurate** | Phoenix wet chemistry: a few tenths of a percent by weight; reanalysis gives about 0.5–0.6 wt % [S9] |
| Toxic | **Accurate** | Perchlorate blocks iodide uptake by the thyroid (EPA basis for its drinking-water assessment; **not opened**) |
| Dissolves into meltwater | **Accurate** | Perchlorates are highly soluble and attract water [S9] |
| Treat water and soil | **Accurate** | Real options include ion exchange and perchlorate-reducing bacteria (**not opened**) |
| Salty water stays liquid colder | **Accurate** | Calcium perchlorate brine stays liquid down to about −75 °C (198 K) [S10] |

**Journal (MS):** "Mars dirt has a salt called perchlorate mixed in. It's bad for your thyroid, a gland in your neck. It dissolves easily, so meltwater picks it up. Treat the water before you drink it."

### 1.6 "Mature ecosystems are nearly carbon-neutral" (balance sheet §7)

**Verdict: acceptable simplification, but contested; reword.** The classic view (young ecosystems store carbon fast; mature ones near balance) is still taught. But a large review found old-growth forests usually **keep taking up** carbon, accounting for at least 10 % of global forest net ecosystem productivity on about 15 % of the area [S11]. Peatlands store carbon for thousands of years (**not opened**).

The game mechanic (uptake falls to 30 % in a mature patch) is fine. The words must not claim mature ecosystems stop storing carbon.

**Wording:** "As a biome matures, the CO2 its plants take in and the CO2 released by decay get closer to balanced. A young, growing biome pulls CO2 down much faster than a mature one. (Some old forests and peat bogs still store carbon slowly.)"

### 1.7 Story bible [verify] tags

| Line (story bible §) | Claim | Verdict | Source / fix |
|---|---|---|---|
| §2 Setting | 0.6 kPa, −60 °C, perchlorate soil, storms, radiation | **Accurate** | AR Part A; [S9] |
| §2 Setting | Mid-northern lowlands near buried ground ice | **Accurate** | [S7]; Phoenix found ice centimeters down at 68° N (**not opened**) |
| §3.2 | Pre-placing cargo before crews is a real planning idea | **Accurate** | NASA Mars Design Reference Architecture 5.0 (2009) pre-deploys cargo (**not opened**) |
| §3.2 | Buried ground ice in the mid-latitudes | **Accurate** | [S7] |
| §3.2 | Scientists study whether microbes could survive in deep brines | **Accurate** | Say "study whether". Claims of liquid lakes under the south polar cap are **contested**; don't state them as fact |
| §3.2 | Planetary protection, COSPAR | **Accurate**, with a wording caution | COSPAR's Policy on Planetary Protection is about preventing contamination of other worlds and of Earth (**not opened**). It does **not** require keeping finds secret. The "say nothing until results are confirmed" order must read as Earth's choice, not as COSPAR policy |
| §3.2 | Launch windows about every 26 months | **Accurate** | Earth–Mars synodic period about 780 days, about 25.6 months (AR [1]) |
| §3.2 | Dust storms cover the planet; 2018 storm ended a rover | **Accurate** | Opportunity last contact 10 June 2018, mission declared complete 13 February 2019 [S12] |
| §3.4 | Extremophiles in ice, brine, deep rock | **Accurate** | (**not opened**; textbook-level) |
| §3.4 | Tardigrades survive freezing and drying (cryptobiosis) | **Accurate** | (**not opened**) |
| §3.4 | Salty water stays liquid at lower temperatures | **Accurate** | [S10] |
| §3.4 | "Life stages triggered by temperature and water (e.g. insects, frogs)" | **Acceptable, imprecise.** Frog metamorphosis is hormone-driven | Better: "Some animals wait dormant until rain or warmth returns, then wake and grow, like spadefoot toads that dig out after rain, or mosquito eggs that hatch when flooded." (**not opened**) |
| §3.4 | HS-LS2-6 mapping | **Accurate** | Second half of the PE: "changing conditions may result in a new ecosystem" ([ngss-verification.md](ngss-verification.md)) |
| §3.4a | Perchlorate | **Accurate** | 1.5 |
| §3.4a | Permafrost damage | **Accurate** | [S6] |
| §3.5 | MOXIE | **Accurate** | 16 runs, 122 g O2 in total, up to 12 g/h at ≥ 98 % purity, ended 2023 [S13]. Line is fine as written |
| §4 | Message delay 3–22 minutes one way | **Accurate, source pending** | Light time at closest approach about 3.0 min, at farthest about 22.3 min (calc from distances recalled, **unverified**; section 9) |
| §5.3 | Thawing permafrost releasing stored carbon | **Accurate** | Permafrost holds about twice as much carbon as the atmosphere (Schuur et al. 2015, *Nature*; **not opened**) |
| §5.3 | Ancient life revived from frozen ground | **Accurate** | Nematode worms revived after about 46,000 years in Siberian permafrost (Shatilovich et al. 2023) [S14]. Don't add disease-scare framing |
| §6.5 | "Delay to Earth: 14 minutes" | **Accurate** if computed in range | Any value from 3 to 22 min. A random value in range is fine; label nothing as "today's" delay |
| §7.3 | Reading rate | **Accurate as a conservative planning assumption** | Grade 6 oral reading: spring median 146 words correct per minute, 10th percentile 91 (Hasbrouck & Tindal 2017) [S15]. The ID's 100 wpm sits near the 10th–25th percentile: right for a "cover weak readers" budget |
| §8.3 sample | "On Earth: our thick, warm air is why puddles last." | **Acceptable** | Sharper: "On Earth, thick air keeps water from boiling away. That's why puddles last." |
| §8.3 sample | Mensah: "We did it by accident." | **Accept the ID's edit**, tightened | "We did it without meaning to, mostly by burning coal, oil, and gas." Human activity is the main cause of recent warming (IPCC AR6 WG1 SPM, **not opened**) |

---

## 2. Lead Artist items

### 2.1 Sky path tan → rose → lavender → blue, and the stylized rose Phase 2 sky

**What's known.**
- Today's sky color comes from suspended dust that absorbs blue and scatters red (artist's citations exist, 2.12; the "about 1.5 µm" particle size is **unverified** this session). Calibrated Pancam work found sky color tracks dust opacity: "dark yellowish brown" at high opacity, and modeled to go "bluish-black" or "black" if dust-free **at today's pressure** [S16]. Today's air is too thin to make a bright blue sky.
- The blue part of a sky comes from scattering by gas (Rayleigh), which grows with the mass of air overhead.

**How much blue at each gate (calc from pressure and gravity, which are sourced; the per-molecule scattering strength of CO2 vs. Earth air is a value I recall, about 2.4×, **unverified**, so treat the gas-blue comparison below as qualitative).** Air overhead: today about 17 g/cm²; 5 kPa about 135; 30 kPa about 810; 75 kPa about 2,020 (Earth: about 1,030). At 5 kPa the gas adds blue light, but a normal dust load still dominates. Gas scattering reaches Earth-like strength around 30 kPa.

**Verdicts**

| Step | Verdict |
|---|---|
| Tan start | **Accurate** (mid-dust day) |
| Mixing warm dust light with growing violet-blue gas scattering gives a cooler, greyer, slightly purple tint, not green | **Accurate in direction**. Rayleigh scattering is strongest in violet and blue, so the hue swings through mauve and lavender rather than through green |
| A clearly **rose / mauve** sky at 5–15 kPa | **Acceptable simplification.** Real change at 5 kPa would be subtle and dust-dependent. The game exaggerates saturation and timing |
| Blue at 30–75 kPa | **Accurate**, if background dust is also low (the game's storm caps already lower dust by phase) |

**Does the rose sky teach anything false?** I checked the three likely false lessons:
1. "Pink sky means oxygen or another gas is being added." Low risk: the team rule is that O2 and CO2 never drive sky color (keyframes §1.2), and the journal can name pressure as the cause.
2. "Mars's sky today is pink." Low risk: the start sky is tan.
3. "Scientists know a thickening Mars sky turns pink." **This is the real risk**, and a one-line label removes it.

So the user's decision stands: **keep the rose Phase 2 sky, labeled stylized.** Required label (journal, Phase 2 gate, both bands): "More air scatters more blue light, so the sky slowly loses its dusty tan. Nobody has seen a thickened Mars sky. These in-between colors are our best guess, made stronger so you can see them."

### 2.2 The 75 kPa air column

The artist's reasoning is right. Air overhead = pressure ÷ gravity. At 75 kPa on Mars: (75 / 101.325) × (9.81 / 3.71) = **1.96× Earth's** (calc). Write "about 2×", not 1.9×. The same pressure gives 2.64× more air overhead on Mars (calc); "about 2.6×" is right.

### 2.3 Equilibrium sky color

**Acceptable simplification, with a physics note.** On Earth the sky is a **deeper** blue from high mountains (less air) and paler near the horizon. With **twice** Earth's air overhead, more light scatters more than once, so the sky would most likely be a **brighter, paler, whiter blue**, with a whiter horizon and a yellower sun, not a deeper zenith (physics reasoning from standard atmospheric optics, **not opened**; no published simulation of a terraformed Mars sky found). Optional: lighten and slightly desaturate the 75 kPa zenith (`#4A7AC2`). Low stakes: the sky color is not taught as a fact.

The orange sunset at equilibrium is **accurate in direction**: with thick air and little dust, blue scatters out of the long sunset path, as on Earth.

### 2.4 Flame bands by O2 fraction (keyframes §3.4)

**Facts.**
- Hypoxic fire-prevention rooms are run at about 15 % O2 at sea-level pressure, because many materials struggle to ignite there. But fire risk is not zero at 15 %: measured ignition limits were about 14.1 % for rolled newspaper and 17.0 % for untreated wood; common plastics (PE, PP) about 16 % [S17].
- At **lower total pressure**, the same O2 percentage burns less well. Flammability depends mainly on O2 percentage, but total pressure matters more below about 41 kPa (AR [18]).
- OSHA: above 23.5 % is "oxygen-enriched", a fire hazard (AR [9]).

**Verdict: wrong below 16 %.** The draft shows a struggling flame at 5–12 % and a small steady flame at 12–16 %. That would teach "fire burns in air with a third of Earth's oxygen."

**Recommended bands** (O2 fraction, at ≥ 30 kPa total, which is where O2 rises in play):

| O2 fraction | Flame |
|---|---|
| < 14 % | **No flame**: smoke puff, fizz |
| 14–17 % | Struggling: flickers, goes out |
| 17–19 % | Small, steady |
| 19–23 % | Normal (fire-safety band) |
| > 23 % | Large, sparks, fast spread |

Add one guard: **no open flame below a minimum total pressure, whatever the O2 fraction.** Direction supported by AR [18]. **Real value missing:** I found no sourced pressure below which common materials stop burning in 21 % O2. Until one is sourced, use about 20 kPa as an **invented balance number** (reasoning: below the Phase 3 gate, so it never conflicts with the O2 rise in play) and keep it out of journal text.

*The bands above are **game bands derived from sourced limits** (14.1 % newspaper, 16 % common plastics, 17.0 % wood, 23.5 % OSHA enriched). The band edges themselves are rounded game choices.* Journal: "Fire needs enough oxygen. Below about 15 % oxygen, most things won't keep burning. Above about 23 %, almost everything burns too easily."

### 2.5 Regolith Outgasser vapor (keyframes §3.8)

**Accurate.** Heating Mars soil does release water and CO2 (and some O2 from perchlorate); the Phoenix and Curiosity ovens measured these gases (**not opened**; consistent with [S9]). Hot, moist exhaust meeting very cold air makes a visible cloud of ice crystals, like breath on a cold day. CO2 is invisible. A smaller puff in warmer air is correct. One detail: as hydrosphere rises, more humid air makes puffs last longer; ignoring that is fine.

**Journal (MS):** "The white puff is water vapor freezing into tiny ice crystals, like your breath on a cold day. The CO2 coming out is invisible."

### 2.6 Vapor wisps over water (keyframes §3.3)

**Acceptable simplification.** In thin air, water evaporates faster, and near its boiling point it boils, so more wisps at low pressure is the right direction. But visible "steam" over water also happens on Earth at full pressure, when water is warmer than the air above it (sea smoke, **not opened**). Recommendation: drive **heavy** wisps by "local temperature near or above the boiling point" (same derived value as the ice rule, 1.1), and keep a few light wisps on cold mornings at any pressure. Avoid a journal claim that "no wisps means no evaporation."

### 2.7 Windsock and sounding balloon (keyframes §3.1)

**Windsock: accurate.** The push of wind grows with air density × speed². Today's Mars air density is about 0.015 kg/m³, 1.2 % of Earth's (calc), so a normal windsock hangs limp. Phoenix's "telltale" wind sensor was made extra-light for that reason (**not opened**). At 75 kPa (N2/O2 at 5 °C) density is about 0.94 kg/m³, 77 % of Earth's (calc), so a windsock in a breeze lifts nearly level.

**Balloon: acceptable simplification.** Lift = (air density − gas density) × volume. Thin air gives almost no lift, so "slack on the ground → taut aloft" is the right direction. A tethered balloon's height is really set by its line, so call it a **"lift balloon"** in text and say: "Balloons float only when the air is thick enough to hold them up."

### 2.8 Aurora (keyframes §3.7; asset E-12)

**Facts.** MAVEN found diffuse ultraviolet aurora on Mars, driven by solar energetic particles (Schneider et al. 2015, *Science*). Perseverance recorded the first **visible** aurora from Mars's surface: **green** light from oxygen atoms at 557.7 nm, during a solar storm in March 2024, published by Knutsen et al., *Science Advances*, May 2025 [S18]. The emission comes from solar energetic particles; NASA says future astronauts might see it [S18].

| Spec | Verdict |
|---|---|
| Faint green glow at night during radiation spikes | **Accurate**: right color, right trigger |
| "Along the horizon" | **Acceptable**, but diffuse aurora covers the **whole sky** fairly evenly (**not opened**: detail of the paper). Recommend a faint whole-sky tint, slightly brighter toward the horizon |
| Glow fades with pressure (100 % at 0.6 kPa → 20 % at 75 kPa) | **Wrong.** Aurora glows high in the atmosphere, tens of kilometers up. A thicker atmosphere near the ground blocks particles from reaching **you**, but it does not switch off the aurora. Scale the **dosimeter clicks** with pressure (correct), and scale the aurora with **spike strength only** |

### 2.9 Carbonate deposits at scrubbers (keyframes §3.5)

**Accurate.** CO2 reacts with calcium-, magnesium-, and iron-rich rock to form solid carbonate minerals. In Iceland's CarbFix project, over 95 % of CO2 injected into basalt turned into carbonate in under 2 years [S19]. Mars is mostly basalt, and Mars already has carbonate rocks (**not opened**). Pale color is right (calcite and magnesite are white to pale).

### 2.10 Salt crust (keyframes §2.3)

**Acceptable simplification.** Wording for the journal: "On real Mars, perchlorate is mixed into the dirt in amounts too small to see. Where a lot of salty water dries up, salts can leave a pale crust, like Earth's salt flats."

### 2.11 Start colors: pixel sampling and published values

**Pixel sampling was not possible**: every image host was refused (see "How this was checked"). Published values I could confirm:
- Calibrated sky color tracks dust opacity: "dark yellowish brown" at high opacity [S16]. The artist's tan `#A87B5A` zenith / `#D2AE86` horizon is a reasonable mid-dust value.
- NASA describes a white-balanced Mastcam view's sky as looking "overly blue" and says the sky "would look more of a butterscotch color to the human eye" [S20]. So **avoid white-balanced images** as color references; use "approximate true color" or "natural color" products.
- Pancam "true color" products are calibrated to radiance and converted to sRGB, but the Pancam team calls them **estimates** [S20].
- Curiosity's Mastcam is slightly less sensitive to blue than human eyes, per NASA's sunset caption [S21].

**Hand-off kept open:** once photojournal.jpl.nasa.gov or pds-imaging.jpl.nasa.gov is allowed, sample sky and ground patches from approximate-true-color Pancam images and the Mastcam "natural color" products, at several dust opacities (Bell et al. 2006 relate color to opacity [S16]).

### 2.12 Art citations

| Citation (keyframes §2.1, style guide §12) | Status |
|---|---|
| Smith & Lemmon (1999), Pathfinder opacity, *JGR* | Exists (**not opened**) |
| Lemmon et al. (2004), MER atmospheric imaging, *Science* 306 | Exists (**not opened**) |
| Bell et al. (2003), Pancam investigation, *JGR* | Exists (**not opened**). Add Bell, Savransky & Wolff (2006), "Chromaticity of the Martian sky", *JGR* 111, doi:10.1029/2006JE002687 [S16]: the best source for sky color |
| PIA19400, Curiosity sunset, sol 956, 15 April 2015 | **Confirmed** [S21] |
| Sun 0.66× wide, 43 % of Earth's light | **Accurate** (calc: Mars at 1.52 AU) |
| Curiosity "John Klein" grey drill powder; dark Bagnold dunes | Accurate (**not opened**) |
| Phoenix "Dodo-Goldilocks" ice vanished in days | **Confirmed** (sols 20–24) [S1]. **But fix what it is used for** (Summary row 22): it shows sublimation into cold, dry air at a pressure **above** the triple point [S2], not "Phase 1 below-triple-point" physics. Use it for the HS read-more line in 1.1 |
| Viking Lander 2 frost, Utopia Planitia | Accurate (**not opened**) |
| Curiosity water-ice clouds | Accurate (**not opened**) |
| MAVEN diffuse aurora, Schneider et al. 2015 | Accurate (**not opened**) |
| Perseverance visible aurora (2024) | **Now verified**: Knutsen et al. (2025), *Science Advances* 11, eads1563 [S18] |
| Phobos rises in the west | **Accurate** (orbital period about 7.7 h, shorter than a sol) |
| Maurice et al. (2022), Mars soundscape, *Nature* 605 | **Confirmed** [S22]; see section 4 |
| "About 1 in 12 boys of Northern European descent" are red–green colorblind | **Accurate**: about 8 % of men and 0.4 % of women of European ancestry; 4–6.5 % of Chinese and Japanese men (Birch 2012) [S23]. For a mixed classroom, plan for about 1 in 12 to 1 in 20 boys |

---

## 3. Lead Instructional Designer items

### 3.1 Forecasts F1–F10

| ID | Correct answer | Verdict | Problem and fix |
|---|---|---|---|
| F1 | Solar power drops; dust blocks sunlight | **Accurate** (Opportunity, 2018 [S12]) | **Distractor "Cold makes panels stronger" is partly true**: solar cells do work slightly better when cold (**not opened**; standard PV physics). Replace with "The Sun gives off less light during storms" (a plausible wrong idea) |
| F2 | Inside under thick shielding; thin air and no global magnetic field | **Accurate** (AR Part C) | "Red soil gives off radiation" is a weak distractor because the ground does give off some secondary radiation when hit by cosmic rays (**not opened**: Curiosity RAD). Reword: "Red color soaks up radiation". "Closer to the Sun" is a good misconception distractor (Mars is about 1.5× farther) |
| F3 | Pressure rises slowly; the air is huge next to one machine | **Accurate** in the game's terms | Fine. The journal note on Outgasser exaggeration (DESIGN.md audit) covers the real-world size |
| F4a | Ice turns to vapor with no puddle | **Accurate at < 0.611 kPa**, but see Summary row 3: almost never fires | (1) Trigger: "first ice warmed above 0 °C while pressure < 5 kPa." (2) Question: "The heater has warmed this ice above 0 °C. The air is very thin. What will it do?" (3) Correct: "Disappear into vapor, leaving no water" (true for sublimation and for instant boiling). (4) HS "why": "In thin air, water boils at a low temperature." (5) **Replace "Ice on Mars is not water"**: partly true, since Mars also has CO2 (dry) ice. Use "The heater destroys the water" |
| F4b | Water stays; higher pressure raised the boiling point | **Accurate** | At the Thaw, pressure is about 22 kPa (boiling point about 62 °C, calc) |
| F5 | Temperature rises slowly; gases trap heat that would escape to space | **Accurate** | HS read more: "absorb infrared energy given off by the ground, and send some of it back down." "It blocks sunlight" is a good distractor: it targets a real confusion (AR Part D) |
| F6 | Fauna activity goes up; reinforcing loop | **Accurate under the game's rules**, with one nuance | The loop is reinforcing **only through the player's rebuilding**: fauna damage machines → the player builds more → more fauna. If damaged machines stop running and drop out of the signature, the loop **without** the player is balancing. Explanation: "This is a reinforcing loop when you answer damage by building more machines." Also state the fiction: the behavior is invented; the loop type is real |
| F7 | Less radiation reaches the ground; more air absorbs it | **Accurate** | Trigger "pressure at least doubled since F2" is too small to make a clear difference: air overhead goes from about 17 to 34 g/cm² (calc). Recommend firing once pressure ≥ 5 kPa (about 135 g/cm², 8× today's) so the click-rate change is obvious |
| F8 | O2 rises, CO2 falls; plants take in CO2 and give off O2 | **Accurate** | "Plants make CO2 at night only" is a good distractor because it is a known misconception: plants respire all the time (Haslam & Treagust 1987, **not opened**). Explanation should add: "Plants give off some CO2 all day and night. While they grow in light, they take in more than they give off." |
| F9 | Fires start more easily; more O2 makes things burn more easily | **Accurate** (AR B2) | **Distractor "O2 makes the air heavier" is partly true** (O2 is slightly heavier than N2). Replace with "More O2 smothers flames" |
| F10 | Temperature falls slowly; trade-off of power vs. warmth | **Accurate as a game rule** | Real super-greenhouse gases last a long time but break down in sunlight on Mars and would need topping up [S24]. "Falls slowly" is the right answer. HS read more: "Real factory gases would break down slowly in Mars's strong sunlight, so they would need to be made all the time." |

**Process lesson:** the F4 item broke a second time because of a physics rule and a balance curve meeting. See Shared model updates.

### 3.2 Earth links (D2)

All rows: **accurate.** Approved MS lines (≤ 25 words) and what to avoid:

| Row | Approved line (MS) | Facts and source | Avoid |
|---|---|---|---|
| Dome stable | "Mars's air pushes with less than 1 % of the force of Earth's air. Earth's thick air protects us for free." | 0.636 / 101.3 kPa = 0.63 % (AR [1], calc) | "Mars has no air" |
| Report 1 reply | "On high mountains, water boils below 100 °C. On top of Everest it boils at about 70 °C." | Everest summit about 33.7 kPa → about 72 °C (calc; AR [17]) | Exact single numbers without "about" |
| F4a, sublimation | "Ice can turn straight to vapor on Earth too. Old ice cubes shrink in the freezer, and freeze-dried food is made this way." | Freezer shrinkage and freeze-drying are sublimation (**not opened**; textbook) | "Frost that vanishes" alone: morning frost usually melts. If used, say "snow can shrink on cold, dry, sunny days without melting." **Dry ice** is CO2, not water: if used, label it |
| Thaw (F4b), permafrost | "In the Arctic, frozen ground is thawing. Roads crack and houses tilt as the ice underneath melts." | [S6] | Implying the damage is all in the future |
| First emitter (F5) | "Earth's air has greenhouse gases too: carbon dioxide, methane, and water vapor. They trap heat. That's different from the ozone hole." | AR Part D; IPCC AR6 (**not opened**) | HS nuance: water vapor responds to warming (a feedback) more than it starts it |
| Report 2 reply | "Earth has warmed by about 1.1–1.2 °C since the 1800s, mostly from burning coal, oil, and gas. Thawing permafrost adds more carbon: a reinforcing loop." | IPCC AR6 WG1 SPM: about 1.1 °C for 2011–2020 above 1850–1900 (**not opened**). Data for the debrief: NOAA Mauna Loa CO2 record, NASA GISTEMP temperature (**not opened**; both public). Permafrost carbon: Schuur et al. 2015 (**not opened**) | Using a single recent hot year as "the" number; blame language |
| First flora | "About half of the oxygen made on Earth comes from tiny ocean plants and algae." | NOAA: "roughly half" [S25] | "50–80 %" (NOAA's own page says "roughly half"). "The rainforest is Earth's lungs." HS nuance: ocean life also uses about as much O2 as it makes [S25] |
| Report 3 reply | "Carbon moves between air, plants, soil, ocean, and rock. Balancing loops kept it steady for thousands of years. We're now adding carbon faster than they can remove it." | IPCC AR6 WG1 ch. 5 (**not opened**) | Reservoir numbers in MS text; if HS uses them, cite IPCC AR6 Fig. 5.12 and mark as approximate |
| Ending (Mensah) | Tone line; no fact | — | — |

### 3.3 Transfer task answer key: "Draw the feedback loops in Earth's carbon cycle"

No answer key existed (rubrics.md says the SME must check one). Here is one. Directions use "+" (more A → more B) and "−" (more A → less B). **Loop type rule:** count the "−" links around the loop. Even (including zero) = **reinforcing**; odd = **balancing**.

**Accept these links** (score toward "at least 4 correct links"):

| Link | Sign | Notes |
|---|---|---|
| Burning fossil fuels → CO2 in air | + | An input, not a loop |
| Cutting or burning forests → CO2 in air | + | Input |
| CO2 in air → temperature (greenhouse effect) | + | Core link |
| CO2 in air → plant growth (photosynthesis) | + | Size of this effect is uncertain; direction accepted |
| Plant growth → CO2 in air | − | |
| CO2 in air → CO2 taken up by the ocean | + | |
| Ocean uptake → CO2 in air | − | |
| Temperature → how much CO2 the ocean can hold | − | Warm water holds less gas |
| Temperature → decay of soil and dead plants (respiration) | + | |
| Decay → CO2 in air | + | |
| Temperature → permafrost thaw | + | |
| Permafrost thaw → CO2 and methane in air | + | |
| Temperature → rock weathering | + | Very slow (100,000+ years) |
| Rock weathering → CO2 in air | − | |
| Temperature → wildfires | + | Also depends on drought |
| Wildfires → CO2 in air | + | |

**Accept these loops**

| Loop | Type |
|---|---|
| CO2 → temperature → permafrost thaw → CO2 | Reinforcing |
| CO2 → temperature → soil decay → CO2 | Reinforcing |
| CO2 → temperature → ocean holds less CO2 → more CO2 stays in air | Reinforcing |
| CO2 → temperature → wildfires → CO2 | Reinforcing |
| CO2 → plant growth → CO2 down | Balancing |
| CO2 → ocean uptake → CO2 down | Balancing |
| CO2 → temperature → rock weathering → CO2 down | Balancing (slow) |

**Partial credit and common errors**

| Student answer | Score as |
|---|---|
| Ice–albedo loop (temperature → ice melts → darker surface → temperature) | A correct **climate** loop, but not a carbon-cycle loop. Counts for B2 (loop type), not for the carbon-cycle links |
| "Fossil fuels → CO2 → warming" labeled as a loop | Not a loop (no path back). Correct links, wrong loop |
| CO2 → plants → less CO2, labeled "reinforcing" | Loop types swapped (rubric score 2) |
| Ozone hole in the diagram as a cause of warming | Misconception (D2 rubric score 1 for that link) |
| "Plants make oxygen, so less CO2" with no direction | Partial link |
| "Balancing = good, reinforcing = bad" | A misreading worth correcting in feedback: loop type describes behavior, not value |

**Uncertainty note for teachers:** the **direction** of every link above is well established; how **strong** some are (plant growth response, soil decay, permafrost) is still being measured. Score direction, not size. Source basis: IPCC AR6 WG1 chapter 5 (**not opened**).

### 3.4 C1/C2 ETS split (ngss_map.example.json)

**Verdict: acceptable, with two changes.** PE wordings are in [ngss-verification.md](ngss-verification.md).

| Goal | Band | Draft | My view |
|---|---|---|---|
| C1 (plan for limited resources, life-critical first) | MS | ETS1-1, ETS1-2 | **Keep.** ETS1-1 (criteria and constraints) is the strong fit; ETS1-2 (evaluate competing solutions) fits the "rank and justify" post-test item |
| C1 | HS | ETS1-1, ETS1-2 | **Keep**, but HS-ETS1-1 needs a "major global challenge": count it only if the transfer task or debrief pairs C1 with an Earth challenge (as noted in round 1) |
| C2 (diagnose, fix so it stays fixed) | MS | ETS1-3, ETS1-4 | **Keep both.** ETS1-4 (iterative testing and modification) is the strong fit. ETS1-3 (compare test data across several solutions) fits only if failure reports let the player compare fixes; otherwise mark it related |
| C2 | HS | ETS1-3, ETS1-4 | **Change:** drop HS-ETS1-3 from C2 (it is B4/B5's code: prioritized criteria and trade-offs). Use **HS-ETS1-2** (break a complex problem into smaller ones) and HS-ETS1-4 (simulation of impacts). HS-ETS1-4 fits the whole game more than C2 alone; keep it, noting "moderate" |

Nothing here is verified word-for-word on nextgenscience.org yet (still blocked).

### 3.5 Permafrost and sublimation examples

See 3.2. **Permafrost** lines are accurate [S6]. **Sublimation**: freeze-drying and shrinking freezer ice are accurate and familiar; "frost that vanishes without melting" is only true on cold, dry days, so qualify it; dry ice must be labeled as frozen CO2.

---

## 4. Pressure-driven ambient sound (DESIGN.md, Learning Mechanisms)

**Claim:** "at 0.6 kPa outside sounds are faint and thin; as the atmosphere thickens they become fuller ... sound carries better in denser air."

**Verdict: accurate.**
- Perseverance's microphones: Maurice et al. (2022) predict, from the low pressure and CO2's properties, that sounds are about **20 dB weaker** on Mars than on Earth (a model prediction from acoustic impedance, not a direct side-by-side measurement) [S22][S26].
- Most sounds fade over about **one-eighth** of the distance they would on Earth [S26].
- High pitches fade more: attenuation with distance was measured above 2 kHz [S22]. So the low-pass filter in keyframes §3.1 is the right direction.
- Two speeds of sound, about 240 m/s below 240 Hz and about 250 m/s above [S22]. Fun HS fact; not needed in play.
- Why: thin air couples sound poorly from a vibrating source (low acoustic impedance), and CO2 at low pressure absorbs high frequencies strongly (**not opened**: standard acoustics; consistent with [S22]).

Better wording for DESIGN.md: "Thin air carries sound poorly: sounds are quieter and high pitches fade fast. As the air thickens, sound gets louder and fuller." The keyframe numbers (gain 0.25 ≈ −12 dB at 0.6 kPa) are milder than the real ≈ −20 dB: **acceptable simplification** for play.

## 5. Weather mast (adopted)

**Verdict: plausible.** Real Mars landers carried weather masts: Viking, Pathfinder, Phoenix (with its telltale wind indicator), Curiosity's REMS and Perseverance's MEDA (wind, pressure, air and ground temperature, humidity, and radiation and dust sensors) (**not opened**).

| Instrument | Real analog | Verdict | Fix |
|---|---|---|---|
| Windsock | Phoenix telltale | **Accurate** (2.7) | — |
| Lift balloon | None on landers | **Acceptable simplification** | Call it a "lift balloon" (2.7) |
| Bulb thermometer | Electronic temperature sensors (REMS, MEDA) | **Acceptable**, with one fix | **Mercury freezes at −38.8 °C** (**not opened**; standard value), so at −60 °C a mercury thermometer won't read. Draw a **red spirit (alcohol)** column, or a dial |
| O2 and CO2 gas "tubes" (bar height ∝ partial pressure) | Gas sensors (e.g. Curiosity's laser spectrometer; MOXIE's sensors) | **Acceptable** as a gauge | Draw them as **gauges or indicator bars**, never as tubes of tinted gas, which would break "we don't paint invisible gases" (keyframes §1.2) |
| *(missing)* Barometer | REMS and MEDA pressure sensors | — | Optional: a dial barometer reading kPa, the most direct pressure instrument and a strong A1 cue |
| *(optional)* Radiation and UV sensor | MEDA's radiation and dust sensor; Curiosity's RAD | — | Optional A4 cue that pairs with dosimeter clicks |

---

## 6. New journal honesty notes (add to AR's list)

7. **Thin-air boiling:** "Here, water boils just above freezing. Warmed ice boils away before it can pool."
8. **Sky colors:** "Nobody has seen a thickened Mars sky. These in-between colors are our best guess, made stronger so you can see them."
9. **Ice–albedo:** "On Mars this effect is small, because there isn't much ice. On Earth's Arctic it's huge."
10. **Mature biomes:** "Young biomes pull CO2 down fast. Mature ones are closer to balanced, though some old forests still store carbon slowly."
11. **Fire:** "Below about 15 % oxygen, most things won't keep burning. Above about 23 %, almost everything burns too easily."

## 7. Learner population notes (for LEARNING.md §0)

| Item | Evidence | Use |
|---|---|---|
| "Plants only respire at night" / "plants don't respire" | Documented in diagnostic-test research on photosynthesis and respiration (Haslam & Treagust 1987, *J. Biol. Educ.*, **not opened**) | F8 distractor and explanation; candidate misconception row |
| "The greenhouse effect blocks sunlight" | Part of the greenhouse/ozone confusion literature (AR [28]) | F5 distractor |
| Reading fluency, grade 6 | Median 146 wcpm (spring), 10th percentile 91 (Hasbrouck & Tindal 2017) [S15] | Confirms the 100 wpm planning rate as conservative |
| Red–green color vision deficiency | About 8 % of boys of European ancestry, fewer in some other groups [S23] | Confirms the style guide's colorblind priority |

---

## 8. Sources

Accessed through web search on 2026-11-26 (summaries of the pages listed). Pages themselves were not opened (network policy).

- [S1] NASA/JPL, "Bright chunks at Phoenix lander's Mars site must have been ice" (2008), and PIA10906 "Disappearing ice": https://www.jpl.nasa.gov/news/bright-chunks-at-phoenix-landers-mars-site-must-have-been-ice/ ; https://science.nasa.gov/photojournal/disappearing-ice/
- [S2] Taylor et al. (2010), "On pressure measurement and seasonal pressure variations during the Phoenix mission", *JGR* (abstract: 860 Pa falling to a 724 Pa daily-mean minimum): https://www.aminer.cn/pub/53e9af94b7602d97039db3eb/on-pressure-measurement-and-seasonal-pressure-variations-during-the-phoenix-mission
- [S3] USGS, "Mars atmospheric-surface interactions and the CO2 cycle"; Planetary Society, "Dry ice snowfall at the poles of Mars"; James et al. (1992), *Mars* (book chapter): https://www.usgs.gov/publications/mars-atmospheric-surface-interactions-and-co2-cycle ; https://www.planetary.org/articles/20140116-dry-ice-snowfall-at-the-poles-of-mars ; https://ui.adsabs.harvard.edu/abs/1992mars.book..934J/abstract
- [S4] Phillips et al. (2011), "Massive CO2 ice deposits sequestered in the south polar layered deposits of Mars", *Science* 332:838–841: https://pubs.usgs.gov/publication/70004038
- [S5] Rantanen et al. (2022), "The Arctic has warmed nearly four times faster than the globe since 1979", *Communications Earth & Environment* 3:168: https://ui.adsabs.harvard.edu/abs/2022ComEE...3..168R
- [S6] Hjort et al. (2018), "Degrading permafrost puts Arctic infrastructure at risk by mid-century", *Nature Communications* 9:5147: https://pmc.ncbi.nlm.nih.gov/articles/PMC6289964
- [S7] Dundas et al. (2018), "Exposed subsurface ice sheets in the Martian mid-latitudes", *Science* 359:199–201: https://pubs.usgs.gov/publication/70195019 ; https://www.jpl.nasa.gov/news/steep-slopes-on-mars-reveal-structure-of-buried-ice
- [S8] HiRISE, "Scallop and polygonal features in Utopia Planitia"; Dundas et al. (2015), *Icarus*: https://hirise.lpl.arizona.edu/PSP_002070_2250 ; https://www.lpl.arizona.edu/~shane/publications/dundas_etal_icarus_2015.pdf
- [S9] Hecht et al. (2009), "Detection of perchlorate and the soluble chemistry of martian soil at the Phoenix lander site", *Science* 325; reanalysis in Catling et al. (2010), *JGR*: https://experts.arizona.edu/en/publications/detection-of-perchlorate-and-the-soluble-chemistry-of-martian-soi/ ; https://research-repository.st-andrews.ac.uk/bitstream/10023/4051/1/Catling_jgre2709.pdf
- [S10] Toner et al. (2014, 2015) on perchlorate brines; eutectic of Ca(ClO4)2 about 198 K: https://faculty.washington.edu/dcatling/Toner2014_SupercoolSalts.pdf ; https://pmc.ncbi.nlm.nih.gov/articles/PMC7745847
- [S11] Luyssaert et al. (2008), "Old-growth forests as global carbon sinks", *Nature* 455:213–215: https://www.nature.com/articles/nature07276.pdf
- [S12] NASA/JPL, "NASA's Opportunity rover mission on Mars comes to end" (2019): https://www.jpl.nasa.gov/news/nasas-opportunity-rover-mission-on-mars-comes-to-end/ ; https://www.planetary.org/articles/02-mer-update-opportunity-complete
- [S13] MIT AeroAstro, "Oxygen-generating experiment MOXIE completes Mars mission" (2023): https://aeroastro.mit.edu/news-impact/oxygen-generating-experiment-moxie-completes-mars-mission
- [S14] Shatilovich et al. (2023), *PLOS Genetics*, nematode revived after about 46,000 years; coverage: https://www.npr.org/2023/07/30/1190950660/nematode-worm-permafrost-discovery-frozen ; https://www.the-scientist.com/worms-frozen-for-46-000-years-are-oldest-known-living-animals-70048
- [S15] Hasbrouck & Tindal (2017), oral reading fluency norms: https://www.readingrockets.org/article/fluency-norms-chart-2017-update ; https://education.ufl.edu/patterson/files/2018/07/2017-ORF-NORMS-PDF.pdf
- [S16] Bell, Savransky & Wolff (2006), "Chromaticity of the Martian sky as observed by the Mars Exploration Rover Pancam instruments", *JGR* 111, doi:10.1029/2006JE002687: https://asu.elsevierpure.com/en/publications/chromaticity-of-the-martian-sky-as-observed-by-the-mars-explorati ; https://earthref.org/ERR/96350
- [S17] Hypoxic fire prevention: Lund University report; Chiti (2011), FireSEAT; HSE RR1137: https://lup.lub.lu.se/search/files/3181087/3972932.pdf ; https://www.fireseat.eng.ed.ac.uk/sites/fireseat.eng.ed.ac.uk/files/images/FS11-Proc-Chiti.pdf ; https://hse.gov.uk/researcH/rrhtm/rr1137.htm ; https://en.wikipedia.org/wiki/Limiting_oxygen_concentration (secondary)
- [S18] Knutsen et al. (2025), "Detection of visible-wavelength aurora on Mars", *Science Advances* 11, eads1563; NASA release: https://elib.dlr.de/215286/ ; https://www.jpl.nasa.gov/news/nasa-observes-first-visible-light-auroras-at-mars/
- [S19] Matter et al. (2016), "Rapid carbon mineralization for permanent disposal of anthropogenic carbon dioxide emissions", *Science* 352:1312–1314: https://ui.adsabs.harvard.edu/abs/2016Sci...352.1312M
- [S20] NASA/JPL PIA16768, "Mount Sharp panorama in white-balanced colors"; ASU Pancam true-color method: https://www.jpl.nasa.gov/images/pia16768-mount-sharp-panorama-in-white-balanced-colors ; https://pancam.sese.asu.edu/projects_1.html
- [S21] NASA/JPL PIA19400, "Sunset in Mars' Gale Crater" (sol 956, 15 April 2015): https://www.jpl.nasa.gov/images/pia19400-sunset-in-mars-gale-crater/
- [S22] Maurice et al. (2022), "In situ recording of Mars soundscape", *Nature* 605:653–658, doi:10.1038/s41586-022-04679-0: https://authors.library.caltech.edu/records/yymhf-wyt75 ; https://www.dlr.de/en/latest/news/2022/02/20220401_the-sound-of-mars
- [S23] Birch (2012), "Worldwide prevalence of red-green color deficiency", *JOSA A* 29(3):313–320: https://pubmed.ncbi.nlm.nih.gov/22472762/
- [S24] Gerstell et al. (2001), "Keeping Mars warm with new super greenhouse gases", *PNAS* 98:2154–2157: https://pmc.ncbi.nlm.nih.gov/articles/PMC30108
- [S25] NOAA Ocean Service, "How much oxygen comes from the ocean?": https://oceanservice.noaa.gov/facts/ocean-oxygen.html
- [S26] Physics Today, "Listening to the Martian soundscape"; CBS News summary of Maurice et al.: https://physicstoday.aip.org/news/listening-to-the-martian-soundscape ; https://www.cbsnews.com/news/mars-sound-slower-nasa-perseverance-rover-recordings

## 9. Calculations (reproducible)

- **Boiling point:** Antoine equation as in AR. 0.611 → 0.1 °C; 0.7 → 2.0; 1 → 7.1; 2 → 17.6; 3 → 24.1; 4 → 29.0; 5 → 32.9; 6.3 → 37.1; 22 → 62.2; 30 → 69.2; 33.7 → 71.9; 75 → 91.8 °C.
- **CO2 frost point:** T = 3148 / (23.102 − ln(P / 100 Pa)) K (common Mars-model form, **not opened**). 600 Pa → 148 K (−125 °C); 5 kPa → 164 K (−109 °C); 30 kPa → 181 K (−92 °C).
- **Air overhead:** P / g. Mars g = 3.71 m/s². 0.636 kPa → 17 g/cm²; 5 kPa → 135; 30 kPa → 808; 75 kPa → 2,021. Earth: 1,033. Ratio at 75 kPa: 1.96×. Same pressure: 9.81 / 3.71 = 2.64×.
- **Air density:** ρ = P·M / (R·T). Mars today (0.6 kPa, CO2, 214 K): 0.0148 kg/m³. 75 kPa N2/O2 at 278 K: 0.94 kg/m³. Earth sea level: 1.225 kg/m³.
- **Light time:** closest Earth–Mars distance about 54.6 million km → 182 s ≈ 3.0 min; farthest about 401 million km → 1,337 s ≈ 22.3 min. **The two distances are from memory, not from a source opened this session (unverified).** The 3–22 min range is widely quoted but must be sourced (NASA Mars fact sheet, still blocked) before it appears in game text.
- **Ice–albedo:** ΔT ≈ ΔF / (4σT³). Mean absorbed-sunlight base 586 / 4 ≈ 147 W/m². ΔF ≈ 147 × 0.03 (ice area) × 0.5 (polar sunlight weighting) × 0.4 (albedo change) ≈ 0.9 W/m². 4σT³ at 210 K ≈ 2.1 W/m²/K, so ΔT ≈ 0.4 K with no other feedbacks. Order of magnitude only; area and weighting are my assumptions.

---

## 10. Value audit (PROCESS.md §1: never invent a number meant to be real)

Rule applied: real-world values live only in sourced documents; a missing real value is listed as missing, not estimated; invented balance numbers are labeled with their reasoning.

### 10.1 Status of every kind of value in `docs/science/`

| Status | Meaning | Where it appears |
|---|---|---|
| **Sourced** | Taken from a source in the Sources list (AR or this file) | Most facts: starting values, triple point, OSHA/NIOSH limits, 724–860 Pa Phoenix, 25–30 % seasonal CO2, 80 % buried CO2, 3.8× Arctic, 70 % infrastructure, 0.5–0.6 wt % perchlorate, 198 K brine, MOXIE totals, aurora, 20 dB, fire limits 14.1/16/17.0 %, 95 % in 2 years, reading norms, 8 % colorblind, "roughly half" ocean O2 |
| **Calculated from sourced inputs** | My arithmetic on sourced values; method in section 9 or AR | Boiling points, air columns (1.96×, 2.64×), air densities, CO2 frost points (formula itself **unverified**), 0.63 % pressure ratio, ≈ 0.5 kPa buried CO2 |
| **Order-of-magnitude estimate** | A calc with my own assumptions; not a real-world value and not for game text | Ice–albedo ≈ 0.4 °C (assumed ice area, weighting, albedo change) |
| **Invented balance number** | A game choice; goes to the balance sheet | 2 °C ice–albedo cap; ~20 kPa no-flame guard; puddle evaporation time; flame band edges (rounded from sourced limits) |
| **Unverified (from memory, not opened)** | Real values I could not source this session. **Must not appear in game text until sourced** | Earth–Mars distances (light-time 3–22 min); CO2 Rayleigh factor 2.4×; mercury freezing −38.8 °C; Earth warming ~1.1 °C (IPCC AR6); permafrost carbon ≈ 2× atmosphere (Schuur 2015); Everest boiling point uses AR's sourced 33.7 kPa (calc, fine); Earth mean 15 °C (AR, already marked) |

AR (round 1) already marks its own unverified values; no changes needed there beyond a pointer to this file.

### 10.2 Values elsewhere that read as real but aren't sourced (flag only; other roles' files)

| File | Value | Problem | Ask |
|---|---|---|---|
| `docs/design/balance-sheet.md` §2, §4 | "Dust storms cut solar output by 60 %" | Reads as a real figure. It is a game number. Real storms vary: the 2018 storm cut Opportunity's power enough to end the mission [S12] | Label as invented balance number |
| `docs/design/balance-sheet.md` §2 | Polar frost "−2 % / −1 % per sol", stops at −20 °C | Invented rates presented next to a real process | Label invented; use the wording in 1.2 |
| `docs/design/balance-sheet.md` §4 | Drift event sizes (O2 +0.6 %/sol, pressure −1.5 kPa over 2 sols, etc.) | Game numbers; fine, but the "Polar winter" event reads like a real seasonal size | Label invented. Real seasonal swing is ~25–30 % of the air [S3], which the game deliberately doesn't model |
| `docs/art/planetary-keyframes.md` §2.1 | Dust "particles around 1.5 µm" | Plausible, but my searches did not confirm the number | Mark unverified, or source to Lemmon et al. 2004 once opened |
| `docs/art/planetary-keyframes.md` §2.2 | "1.9×" column | Calc gives 1.96× | Write "about 2×" |
| `docs/art/planetary-keyframes.md` §3.1, style guide §9.3 | Sound gain and low-pass values | Fine as stylized, but sit next to a real-physics claim | Label as stylized; real reference is ~20 dB weaker [S22] |
| `docs/art/planetary-keyframes.md` §3.7 | Aurora fades "100 % → 20 %" with pressure | Invented and physically wrong (2.8) | Remove the pressure scaling |
| `docs/design/story-bible.md` §4, §6.5 | "3 to 22 minutes"; "14 minutes" | Real range not yet sourced in our docs (10.1) | Keep as is; SME sources the range when the fact sheet is reachable |
| `docs/learning/prompts-and-reflections.md` §5 | 100 wpm | Already labeled an assumption; now supported by [S15] | None |
| `LEARNING.md` / AR journal notes | "about 100 years" / "100,000 years" | Sourced only to popular summaries of McKay (AR: partly verified) | External review (section 11) |

---

## 11. Verdicts that most need an external expert

We have one internal SME. These are the verdicts where a wrong call would most likely teach a misconception, or where I relied on reasoning or memory rather than an opened source. Listed in priority order.

| # | Verdict | Why it needs outside review | Best reviewer |
|---|---|---|---|
| 1 | Recommended ice-to-water rule (1.1), including "water boils away at once in Phase 1 near a heater" | Core A2 lesson and two forecasts depend on it. Real behavior of warmed ice in thin, dry, cold air (boiling vs. fast evaporation vs. refreezing) is subtle | Planetary scientist (Mars surface water / volatiles) |
| 2 | Ice–albedo is small on Mars (1.3), and the "stops above −20 °C" polar frost line (1.2) | Order-of-magnitude reasoning only; atmospheric-collapse behavior of a thicker Mars is studied and uncertain | Planetary climate modeler |
| 3 | Sky color path and the "paler, not deeper, blue" equilibrium sky (2.1, 2.3) | Physics reasoning; no published simulation found; no pixel sampling | Planetary atmospheric optics specialist (e.g. a rover camera team member) |
| 4 | Flame bands and the low-pressure flame guard (2.4) | Real pressure threshold is missing; combustion limits vary by material | Combustion / spacecraft fire-safety engineer |
| 5 | Transfer-task answer key (3.3) | Will be used to grade students; link strengths are uncertain | High-school Earth science or environmental science teacher, plus a carbon-cycle scientist |
| 6 | Forecast distractors and explanations F1–F10 (3.1) | Whether distractors read as wrong to real 12–18 year olds is a classroom judgment, not a science one | Middle and high school science teachers |
| 7 | Terraforming timescales (AR: ~100 years warming, up to ~100,000 years O2) | Sourced only to popular summaries | Terraforming / astrobiology researcher |
| 8 | C1/C2 NGSS split (3.4) and all NGSS wording | nextgenscience.org still blocked; mapping is a judgment | Science teacher or state NGSS coordinator |
