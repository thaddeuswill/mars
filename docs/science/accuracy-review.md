# Science Accuracy Review

Owner: Lead Subject Matter Expert. Reviewed: 2026-11-26.
Scope: Planetary Index values and gates in `DESIGN.md`. The "Science accuracy notes" and misconception table (section 0.2) in `LEARNING.md`.

**How this was checked.** Values were checked against NASA, NIST, OSHA/NIOSH, FAA and peer-reviewed sources through web search. This session could not open the NASA fact sheet (nssdc.gsfc.nasa.gov) or nextgenscience.org directly, because the network blocked them. Numbers from those pages come from search-indexed copies of the same pages. Anything I could not confirm against a source is marked **unverified**. Boiling points and altitude equivalents are my own calculations. The method is given with each one.

Verdict key: **Accurate**, **Acceptable simplification** (fine for the game, but the journal should say it is simplified), **Wrong** (would teach something false, so fix it).

---

## Summary of findings

| # | Finding | Severity |
|---|---|---|
| 1 | **The Breathable gate checks O2 percentage only.** Lungs care about O2 *partial pressure*. At 30 kPa (the Phase 3 start), 21% O2 gives about 6.3 kPa of O2. That is like standing above the summit of Everest, and it is deadly without a mask. | **Wrong**: must fix |
| 2 | **Even at the ≥ 60 kPa target, 19–23% O2 is thin air.** It gives 11.4–13.8 kPa of O2, the same as about 4,200 m altitude. You can live there, but it is not "Earth air". | Acceptable if labeled. Better to fix |
| 3 | **Classroom (×0.3) and multiplayer multipliers scale "gate thresholds".** If they scale physical thresholds, breathability would trigger at about 18 kPa. That is lethal, and it teaches a false idea. | **Wrong** if applied to physics thresholds |
| 4 | **The CO2 limit is also a percentage.** Toxicity depends on CO2 partial pressure. A limit of < 1% at 60 kPa gives 0.6 kPa, just above the US 8-hour workplace limit (about 0.5 kPa). | Acceptable. Small tweak suggested |
| 5 | **Nothing in the index explains the other ~78% of the air.** At ≥ 60 kPa with < 1% CO2 and about 21% O2, roughly 46 kPa must be a buffer gas such as nitrogen. Mars has very little nitrogen. | Acceptable simplification. Needs a journal note |
| 6 | **The "Ice melts as soon as it's above 0 °C" misconception row is worded badly.** That statement is *true* on Earth. The real misconception is that 0 °C and 100 °C are fixed whatever the pressure. | Fix wording |
| 7 | **The dormant fauna is fiction.** No life has been found on Mars. The journal must say so, or the game teaches "Mars has animals underground". | **Must label** |
| 8 | **The starting values (0.6 kPa, −60 °C, 95% CO2, 0.1% O2) match real Mars closely.** | Accurate |
| 9 | **All three "Science accuracy notes" are correct.** Two can be made more precise, with sources. | Accurate. Refine |

---

## Part A: Planetary Index starting values and targets

| Parameter | Game value | Real value (units, source) | Verdict | Suggested fix |
|---|---|---|---|---|
| Pressure, start | 0.6 kPa | Mean surface pressure 6.36 mbar = **0.636 kPa**. It varies from 0.40 to 0.87 kPa with the seasons [1]. ESA gives 6.35 mbar [2]. | **Accurate** | Optional: show "~0.6 kPa (changes with seasons)". The seasonal swing happens because CO2 freezes onto the poles in winter, which is a nice journal fact. |
| Pressure, equilibrium | ≥ 60 kPa | Earth sea level is 101.325 kPa (standard atmosphere). In the standard atmosphere, 60 kPa is the pressure at about **4,200 m** altitude (my calculation, ISA barometric formula). People live permanently at about 5,100 m (La Rinconada, Peru), where pressure is about half of sea level [3]. NASA's planned exploration habitat runs at 56.5 kPa (8.2 psia), but only with 34% O2 [4]. | **Acceptable simplification**, *if* the O2 band is fixed (see Part B) | Keep 60 kPa as a "mountain-town" target, and fix breathability as in Part B. Or raise the target to about 75 kPa, which matches the FAA airliner cabin limit of 8,000 ft (about 2,400 m) [5]. |
| Temperature, start | −60 °C | Global average about **214 K (−59 °C)** in the current NASA fact sheet. Older editions said about 210 K (−63 °C) [1]. The daily range at the Viking 1 site was **−89 to −31 °C** [1]. ESA gives −55 °C [2]. | **Accurate** | None. The journal could add that the temperature swings about 60 °C between day and night. |
| Temperature, equilibrium | ≥ 5 °C | Earth's global mean is about 15 °C (**unverified** in this review, a standard textbook value). A mean above 0 °C is what lets liquid water survive year-round over large areas. | **Acceptable simplification** | None needed. A journal line could say "Earth averages about 15 °C". |
| Hydrosphere, start | 0 % | There is **no stable liquid water on the surface today**. Mars does have large amounts of ice. The south polar layered deposits alone hold about 1.6 million km³, enough to cover the planet in a global layer about **11 m** deep [6]. | **Accurate**, if "hydrosphere" means % of surface covered by liquid water | **The unit is undefined in DESIGN.md.** Define it as "% of surface covered by liquid water". Otherwise 0% wrongly suggests Mars has no water. Journal: "Mars has lots of water, but it is frozen." |
| Hydrosphere, equilibrium | ≥ 30 % | Ocean covers about **71%** of Earth's surface [7]. It is **unverified** whether Mars's total water inventory could cover 30% of its surface. Published estimates of Mars's total water vary widely, and this review did not settle them. | **Acceptable simplification** (game target) | Keep it. Journal note: "Earth is about 71% ocean. How much water Mars has, and whether it could fill seas, is still being studied." |
| O2, start | 0.1 % | Curiosity measured **0.145%** O2 (1.45 × 10⁻³ by volume) [8]. ESA gives 0.13% [2]. | **Accurate** (rounded) | Optional: use 0.15% or 0.13%. Either way, it is too small to breathe or to burn anything. |
| O2, equilibrium | 19–23 % | Earth air is about 21% O2, which is 21.2 kPa at sea level. OSHA calls air **below 19.5%** "oxygen-deficient" and air **above 23.5%** "oxygen-enriched" (a fire hazard) [9]. Those limits assume sea-level pressure. | **The band itself is accurate. Using it as the breathability test is wrong** (see Part B) | Keep 19–23% as the *fire-risk* band. Use O2 partial pressure for breathability. |
| CO2, start | 95 % | NASA fact sheet: **95.1%** [1]. ESA: 95.32% [2]. Curiosity: 96.0% [8]. | **Accurate** | None. |
| CO2, equilibrium | < 1 % | Earth is about **0.042%** (422.8 ppm global mean, 2024) [10]. NIOSH/OSHA workplace limit: 5,000 ppm (0.5%) as an 8-hour average. NIOSH short-term limit: 30,000 ppm (3%). Immediately dangerous: 40,000 ppm (4%) [11]. These figures assume sea-level pressure, so 0.5% ≈ **0.51 kPa** of CO2. | **Acceptable simplification** | At 60 kPa, 1% CO2 = 0.6 kPa, a little above the 8-hour limit. Either set the target to < 0.5%, or define it as CO2 partial pressure ≤ 0.5 kPa (better). |
| Biomass | 0 → threshold | Mars has no known life. Biomass 0 is correct. | **Accurate** | None. |
| *(missing)* Buffer gas | — | The target air (≥ 60 kPa, about 21% O2, < 1% CO2) must be about 78% something else. On Earth that is nitrogen, which makes up most of the surface pressure [12]. Mars's air is only about 2–3% N2 of 0.6 kPa [1][8]. Curiosity found nitrates in Martian rock, but how much fixed nitrogen Mars holds overall is poorly known [13]. | **Acceptable simplification**, but the gap should be stated | A journal note is enough: "Real air also needs a filler gas like nitrogen. Mars has very little, so real terraformers might have to bring it from elsewhere." Optional design idea: a nitrogen-import mechanic. Game Designer's call. |

---

## Part B: Gate thresholds and the physics behind them

### B1. Liquid water vs. pressure and temperature

Facts:
- **Triple point of water: 611.657 Pa (0.611657 kPa) at 273.16 K (0.01 °C)** [14]. Below this pressure, ice cannot melt into liquid. It turns straight into vapor (sublimation).
- Liquid water needs pressure above the triple point, and a temperature between freezing and the boiling point for that pressure. Haberle et al. (2001) used exactly this test. They found low regions of Mars (Hellas, Argyre, and parts of Amazonis, Arabia and Elysium) where it is met for part of the year [15].
- Even when water is "stable" against freezing and boiling, it can still evaporate quickly into very dry air [15].

Boiling point of pure water at each game pressure (my calculation, Antoine equation for water):

| Pressure | Where in the game | Boiling point | What it means |
|---|---|---|---|
| 0.6 kPa | Start | about 0 °C (≈ triple point) | Ice turns to vapor and cannot melt into liquid |
| 0.636 kPa | Real mean Mars | about 0.7 °C | Liquid is possible only between 0 and about 0.7 °C, and only briefly |
| 1.16 kPa | Hellas floor, about 2× mean [15] | about 9 °C | Short-lived liquid is possible on warm days |
| 5 kPa | Phase 2 gate | about 33 °C | Liquid is possible from 0 to 33 °C |
| 30 kPa | Phase 3 gate | about 69 °C | Liquid is comfortably stable |
| 60 kPa | Equilibrium | about 86 °C | Liquid is comfortably stable |

Gate verdicts:

| Gate | Condition | Verdict | Note |
|---|---|---|---|
| Phase 2 begins | Pressure ≥ 5 kPa | **Accurate** as a "water can now be liquid" milestone | At 5 kPa, water stays liquid between 0 and 33 °C. Teaching hook: 5 kPa is still **below the Armstrong limit (6.3 kPa)**. At that pressure water boils at body temperature, so a human still needs a full pressure suit [16]. **Suggestion:** make 6.3 kPa a visible Monitor milestone ("your blood would no longer boil"). It is a memorable real threshold. |
| Thaw | Temperature ≥ −20 °C | **Acceptable simplification** | A global mean of −20 °C means warm areas and daytimes are above 0 °C, so local melting is plausible. |
| Hydrosphere | ≥ 10 % | **Acceptable** | Comes after Thaw and Phase 2 pressure, so the order is physically sensible. |
| Phase 3 begins | ≥ 30 kPa and ≥ 0 °C | **Accurate** | Both conditions together make liquid water stable. This is the clearest A2 lesson in the game. Teaching hook: 30 kPa is about the pressure on **Everest's summit (253 mmHg ≈ 33.7 kPa)** [17]. |
| "Liquids become stable" (pressure effect text) | Listed under rising pressure | **Accurate** | Make sure the game requires *both* pressure and temperature. Pressure alone is not enough. |

### B2. Breathability (O2 partial pressure vs. percentage)

Facts:
- Your lungs respond to the **partial pressure** of O2: total pressure × O2 fraction. At sea level that is about 21.2 kPa.
- 60 kPa × 19–23% = **11.4–13.8 kPa O2**. Sea-level air at about 4,200 m altitude gives the same (my calculation). Healthy, acclimatized people can live like this. About one in four residents of La Rinconada (≈ 5,100 m) has chronic altitude sickness [3]. So this is "breathable, but hard", not "Earth-normal".
- NASA's exploration habitat atmosphere is **56.5 kPa (8.2 psia) with 34% O2**. That gives about 19.2 kPa O2. NASA chose it as a compromise between flammability, hypoxia and decompression sickness [4].
- Airliner cabins must stay at or below the pressure of **8,000 ft** (≈ 75 kPa, ≈ 15.8 kPa O2 at 21%) [5].
- Fire risk rises mainly with O2 **percentage**, not partial pressure. NASA tests show flammability depends more on O2 concentration than on O2 partial pressure. Below about 41 kPa, total pressure starts to matter more [18].

Verdicts:

| Claim | Verdict | Fix |
|---|---|---|
| "Breathable once O2 in band (19–23%)" | **Wrong as stated.** Percentage alone says nothing about breathability. At 30 kPa, 21% O2 is about 6.3 kPa, which is deadly. | Breathable when **O2 partial pressure ≥ about 16 kPa** (airliner-cabin level), **and** total pressure ≥ the pressure target, **and** CO2 partial pressure ≤ 0.5 kPa. Keep the 19–23% band as the *fire-safety* band, separately. |
| "≥ 60 kPa with 19–23% O2 is breathable" | **Acceptable only with a label.** It is like living at about 4,200 m. | Option A: raise O2 to 25–30% at 60 kPa (more O2, more fire risk). That would turn into a real, teachable trade-off, which NASA faces too [4][18]. Option B: raise the pressure target to about 75 kPa and keep 19–23%. Option C: keep the numbers and add a journal line: "Like living high in the mountains: you can breathe, but you'll get out of breath." **Recommendation: B or C** for simplicity. A is the richest lesson for B4 (trade-offs). |
| "Too much O2 → fire" | **Accurate.** OSHA defines > 23.5% as oxygen-enriched [9]. Flammability rises with O2 fraction [18]. | None. |
| "Fire possible" as O2 rises from 0.1% | **Accurate in direction.** The exact O2 level where common materials stop burning was not checked here (**unverified**). | Tune by playtest. Avoid stating an exact "fire threshold" in the journal. |

### B3. CO2 toxicity

- Workplace limits (sea level): 0.5% for 8 hours; 3% for 15 minutes; 4% immediately dangerous to life [11].
- What matters is CO2 **partial pressure**. At the start, 95% of 0.6 kPa is about 0.57 kPa, which is not the main danger: there is no O2. **In mid-game it becomes deadly.** At 30 kPa with, for example, 80% CO2, the air holds about 24 kPa CO2, far above any safe level.
- **Verdict:** a target of "< 1%" is **acceptable**. Defining it as **CO2 partial pressure ≤ 0.5 kPa** would be accurate at any total pressure.
- **Teaching opportunity:** the failure report for removing a helmet in mid-game could name CO2 poisoning *and* lack of O2. That is a true and memorable lesson.

### B4. Greenhouse warming from CO2

- Mars's air is about 95% CO2, but it is so thin (about 1% of Earth's) that its greenhouse effect is weak. Mars averages about 210 K [19]. Studies find that even much thicker CO2 atmospheres on early Mars struggle to warm it above freezing [19].
- Real proposals warm Mars with **artificial "super-greenhouse" gases** (fluorine compounds such as C3F8). Marinova, McKay and Hashimoto (2005) modeled about 33.5 K of warming from 1 Pa of C3F8 [20]. Mirrors and engineered nanoparticles have also been proposed. Nanoparticles: more than 30 K of warming in a 2024 *Science Advances* modeling study [21].
- **Verdict on the design:** "CO2 must fall while temperature holds" is **consistent with real proposals** *only if* the greenhouse emitters make super-greenhouse gases, not CO2. In the game, temperature should be held by those gases or mirrors, not by the CO2 that is being removed.
- **Fix:** the emitter's journal entry should say what it emits. For example: "factory-made greenhouse gases that trap heat thousands of times better than CO2". The exact potency multiplier is **unverified** here, so leave out a number or verify one first.

---

## Part C: "Science accuracy notes" (LEARNING.md)

| Note | Verdict | Evidence | Suggested wording |
|---|---|---|---|
| "Real Mars likely doesn't hold enough frozen CO2 to warm itself by outgassing alone." | **Accurate** | Jakosky & Edwards (2018, *Nature Astronomy*) found that most remaining CO2 is not accessible. Melting the polar CO2 ice would only about double today's pressure, to about 1.2% of Earth's. The authors concluded that terraforming is "not possible using present-day technology" [22][23]. Press coverage gives a total of about 15 mbar even using all reservoirs. That figure is **unverified** against the paper's own table [23]. | "Scientists measured how much CO2 Mars has left. Even releasing all the CO2 we can reach would only raise the pressure to a few percent of Earth's. So the game's machines make *other* gases too." |
| "Without a magnetic field, a thickened atmosphere would slowly be stripped away by solar wind. The game ignores that timescale." | **Accurate. The timescale can be made precise.** | Mars has no global magnetic field, only patches in its crust [24]. MAVEN measured solar-wind loss today at about **100 g/s**, faster during solar storms [25]. Mars's whole atmosphere is about 2.5 × 10¹⁶ kg [1]. At 100 g/s it would take about **8 billion years** to lose that much (my calculation). A thicker atmosphere would lose gas faster, but still over far longer than human history. That scaling is **unverified** here. Venus has no global magnetic field and still keeps a very thick atmosphere [26]. | "Without a magnetic shield, the Sun's wind slowly strips Mars's air. At today's rate this takes millions to billions of years, so it wouldn't undo terraforming in human times. It is a big reason ancient Mars dried out." Avoid saying "a magnetic field is required to keep an atmosphere". |
| "Terraforming would take centuries or millennia, not 40 hours." | **Accurate, but understated for oxygen** | McKay estimates warming could take about **100 years** with super-greenhouse gases. Building Earth-like O2 by photosynthesis at Earth's average efficiency would take about **100,000 years**, or a few hundred years only at an ideal, best-case efficiency [27]. These are estimates from popular-science summaries of McKay's work. The original papers were not opened (**partly verified**). | "Warming Mars might take about a century. Filling its air with oxygen made by plants could take up to about 100,000 years. The game squeezes this into hours." |

---

## Part D: Misconceptions table (LEARNING.md 0.2)

| Misconception | Is the misconception real? | Is the game's counter accurate? | Fix |
|---|---|---|---|
| "Mars has no atmosphere at all" | Plausible. How common it is among 12–18 year olds is **unverified** (no study found in this review). | **Accurate.** 0.6 kPa is real [1]. | None. Consider adding "it's about 1% of Earth's". |
| "Ice melts as soon as it's above 0 °C" | The real misconception is that melting and boiling points are fixed and don't depend on pressure. Research evidence for its prevalence was not checked (**unverified**). | **Mostly accurate, but worded loosely.** At pressures below 0.611 kPa, ice sublimates [14]. Just above that, liquid can form only briefly before it boils or evaporates [15]. | Misconception: "Water always melts at 0 °C and boils at 100 °C." Counter: "At Mars's low pressure, warmed ice turns straight to vapor, or melts and quickly boils away. Water only pools once the pressure is high enough to keep its boiling point well above its freezing point." |
| "The greenhouse effect is the ozone hole" | **Real and well documented.** Students confuse global warming with ozone depletion (Boyes & Stanisstreet 1993, 1997; Greek students study 1999) [28]. The same research found students often don't count CO2 or water vapor as greenhouse gases [28]. | **Accurate** | Consider a second item: "CO2 is the only greenhouse gas". This ties in with the super-greenhouse gas emitters. |
| "More oxygen is always better" | Plausible. Prevalence **unverified**. | **Accurate.** Fire risk rises with O2 fraction [9][18]. | None. A "read more" could mention that NASA must balance oxygen against fire risk in real habitats [4]. |
| "Fixing one problem fixes the system" | A systems-thinking misconception, not a science fact. Fine as a learning target. | **Fiction warning:** the game's example (warming wakes fauna) is invented. **There is no evidence of life on Mars.** | The Phase 2 journal must say: "The creatures in this game are fiction. Scientists have not found life on Mars. But real interventions do have side effects, for example …" Pair it with a real Earth example in the D2 "on Earth" note, written by the SME. Without this note, the game risks *creating* a misconception. |

---

## Proposed journal "honesty notes" (draft, middle school depth)

1. **Air you can breathe:** "Your lungs care about how much oxygen is pushing into them, not just the percent. Thin air with 21% oxygen is like a high mountain."
2. **Filler gas:** "Earth's air is mostly nitrogen. Mars has very little, so real terraformers would need to find or bring a filler gas."
3. **Not enough CO2:** "Mars doesn't have enough CO2 to warm itself. Real plans use factory-made greenhouse gases."
4. **Losing air:** "Without a magnetic shield, the Sun's wind slowly strips Mars's air. It takes millions of years or more."
5. **Time:** "Warming might take about 100 years. Making oxygen with plants could take up to 100,000 years."
6. **Fauna:** "The creatures are made up. No life has been found on Mars."

---

## Sources

1. NASA NSSDC, Mars Fact Sheet (updated May 2025): https://nssdc.gsfc.nasa.gov/planetary/factsheet/marsfact.html (read through a search-indexed copy, because direct access was blocked in this session)
2. ESA, Facts about Mars: https://www.esa.int/Science_Exploration/Space_Science/Mars_Express/Facts_about_Mars
3. NASA Earth Observatory, The Highest Settlement in the World: https://earthobservatory.nasa.gov/images/145864/the-highest-settlement-in-the-world ; Live Science: https://www.livescience.com/health/whats-the-highest-place-on-earth-that-humans-live
4. NASA NTRS, Exploration Atmospheres (8.2 psia / 34% O2): https://ntrs.nasa.gov/citations/20150021491 ; https://ntrs.nasa.gov/citations/20130013505
5. 14 CFR 25.841, Pressurized cabins: https://www.law.cornell.edu/cfr/text/14/25.841
6. Plaut et al. (2007), Subsurface radar sounding of the south polar layered deposits of Mars, *Science* 316:92–95, doi:10.1126/science.1139672
7. USGS, How much water is there on Earth?: https://www.usgs.gov/water-science-school/science/how-much-water-there-earth
8. Mahaffy et al. (2013), Abundance and isotopic composition of gases in the martian atmosphere from the Curiosity rover, *Science* 341:263–266: https://public.websites.umich.edu/~atreya/Articles/Science_2013_Mahaffy_263_6.pdf
9. OSHA 29 CFR 1910.146 (definitions): https://www.osha.gov/laws-regs/regulations/standardnumber/1910/1910.146
10. NOAA / Climate.gov, Climate change: atmospheric carbon dioxide: https://www.climate.gov/news-features/understanding-climate/climate-change-atmospheric-carbon-dioxide
11. NIOSH Pocket Guide, Carbon dioxide: https://www.cdc.gov/niosh/npg/npgd0103.html ; NIOSH IDLH: https://www.cdc.gov/niosh/idlh/124389.html
12. Olson et al. (2018), review chapter on atmospheres and habitability (states that N2 provides most of Earth's surface pressure). Exact title not confirmed in this review: https://sseh.uchicago.edu/doc/Olson_et_al_2018.pdf
13. Stern et al. (2015), PNAS, Evidence for indigenous nitrogen in sedimentary and aeolian deposits from Curiosity: https://pmc.ncbi.nlm.nih.gov/articles/PMC4394254 ; NASA JPL news: https://www.jpl.nasa.gov/news/curiosity-rover-finds-biologically-useful-nitrogen-on-mars
14. NIST, triple point of water 611.657 Pa: https://www.nist.gov/si-redefinition/kelvin-present-realization ; Guildner et al. (1976) J. Res. NBS 80A: https://nvlpubs.nist.gov/nistpubs/jres/80A/jresv80An3p505_A1b.pdf
15. Haberle et al. (2001), On the possibility of liquid water on present-day Mars, *JGR*: https://ntrs.nasa.gov/citations/20010084314 ; Science@NASA summary: https://webarchive.library.unt.edu/eot2008/20090513093945/http://science.nasa.gov/headlines/y2000/ast29jun_1m.htm
16. Armstrong limit (6.3 kPa; vapor pressure of water at 37 °C): https://en.wikipedia.org/wiki/Armstrong_limit (secondary source. The physics matches standard steam tables.)
17. West et al. (1983), Barometric pressures at extreme altitudes on Mt. Everest, *J. Appl. Physiol.*: https://pubmed.ncbi.nlm.nih.gov/10066724 ; https://pmc.ncbi.nlm.nih.gov/articles/PMC3707097
18. NASA NTRS, maximum oxygen concentration and flammability vs. pressure: https://ntrs.nasa.gov/api/citations/20160004937/downloads/20160004937.pdf ; https://ntrs.nasa.gov/api/citations/20220006612/downloads/ICES-2022-45%20Low%20P%20ECLSS_Second%20Submission_2.pdf
19. Space.com, Climate explained: why Mars is cold: https://www.space.com/climate-explained-why-mars-is-cold-despite-an-atmosphere-of-mostly-carbon-dioxide.html ; arXiv 2411.11111: https://arxiv.org/abs/2411.11111
20. Marinova, McKay & Hashimoto (2005), Radiative-convective model of warming Mars with artificial greenhouse gases, *JGR*: https://sseh.uchicago.edu/doc/marinova_et_al_journal_of_geophysical_research_2005.pdf
21. Ansari et al. (2024), Feasibility of keeping Mars warm with nanoparticles, *Science Advances*: https://news.uchicago.edu/story/scientists-lay-out-revolutionary-method-warm-mars
22. Jakosky & Edwards (2018), Inventory of CO2 available for terraforming Mars, *Nature Astronomy*, doi:10.1038/s41550-018-0529-6: https://lasp.colorado.edu/maven/files/2018/08/Inventory-of-CO2-available-for-terraforming-Mars.pdf
23. Press coverage of [22]: https://in.nau.edu/news/terraforming-mars/ ; https://discovermagazine.com/the-sciences/sorry-elon-theres-not-enough-co2-to-terraform-mars
24. NASA SVS, Solar wind strips the Martian atmosphere: https://svs.gsfc.nasa.gov/4370/ ; Berkeley SSL MAVEN: https://www.ssl.berkeley.edu/?p=6181
25. LASP/MAVEN (2015), Speed of solar wind stripping: https://lasp.colorado.edu/maven/2015/11/05/maven-reveals-speed-of-solar-wind-stripping-martian-atmosphere/
26. Venus induced magnetosphere (no intrinsic global field): https://eos.org/research-spotlights/solar-wind-disconnects-venuss-magnetotail ; https://aasnova.org/2019/07/08/how-venus-reacts-when-the-sun-strikes
27. McKay, Terraforming: Ecosynthesis and ecological restoration on Mars (Obayashi): https://www.obayashi.co.jp/en/kikan_obayashi/detail/kikan_65_mckay.html ; Nautilus: https://nautil.us/make-mars-great-again-236288/ ; McKay, Toon & Kasting (1991), Making Mars habitable, *Nature* 352:489, doi:10.1038/352489a0
28. Boyes & Stanisstreet, student ideas on the greenhouse effect and ozone: https://cjee.lakeheadu.ca/article/view/289 ; https://link.springer.com/article/10.1007/BF00695323

### Calculations (reproducible)
- Boiling point: Antoine equation for water, log₁₀P(mmHg) = 8.07131 − 1730.63/(233.426 + T). Values below 1 °C are extrapolated and approximate.
- Altitude equivalent: ISA, h = 44,330.8 × (1 − (P/101.325)^(1/5.25588)) m.
- Atmospheric loss time: 2.5 × 10¹⁶ kg ÷ (0.1 kg/s × 3.156 × 10⁷ s/yr) ≈ 7.9 × 10⁹ yr. This is an order-of-magnitude figure at today's loss rate.
