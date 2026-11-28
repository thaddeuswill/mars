# Science Review: Dust Storms, Storm Power Loss, Warning Time, and the Dust Sources Reopened

Owner: Lead Subject Matter Expert. Reviewed: 2026-11-28. Task: owner request for the Stage 3 slice 5 SME inputs (storm penalty, warning, F1 key, Monitor cue).
Builds on: [dust-review.md](dust-review.md) (panel dust, 2026-11-26), [round2-review.md](round2-review.md) (F1), [round3-review.md](round3-review.md) (tag conventions).
Checks: `docs/design/balance-sheet.md` §2a (storm rows) and §Phase 3 drift events, `DESIGN.md` (dust storms, panel dust), `docs/learning/journal-dust.md`.

**Perspective round (my lens).** This succeeds if a student leaves knowing that a storm hurts solar power by dimming the sky (and later by coating the panels), that Mars wind is weak even when it is fast, and which numbers in the game are real and which are squeezed to fit a play session.

## Tags used

| Tag | Meaning |
|---|---|
| **verified (opened)** | I read the page myself this session and the figure is on it |
| **opened, secondary** | Read on Wikipedia, which cites NASA/JPL; the number is on the page but I did not open the cited primary |
| **search-summary** | Seen only in a web-search summary; do not put in game text until opened |
| **calc** | My arithmetic on tagged inputs (method shown) |
| **invented** | A game balance choice |
| **unverified / missing** | No source found |

## 0. What opened (hosts)

| Host | Result this session |
|---|---|
| science.nasa.gov (articles, Photojournal, blogs) | **Opened.** 13 pages read in full (Sources N1 to N13) |
| www.nasa.gov (feature pages) | **Opened** (homepage and two "feature" URLs; they returned the same JPL articles as science.nasa.gov) |
| en.wikipedia.org | **Opened.** Climate of Mars, Atmosphere of Mars, 2018 Mars global dust storm, Timeline of Opportunity |
| commons.wikimedia.org | Opened (200); upload.wikimedia.org returned a redirect (301); no images needed |
| photojournal.jpl.nasa.gov | Redirects (301) to science.nasa.gov/photojournal, which opened |
| www.jpl.nasa.gov | **Blocked (HTTP 403)** |
| ntrs.nasa.gov, hal.sorbonne-universite.fr, researchportal.ip-paris.fr, digital.inta.es, doi.org, arxiv.org, ui.adsabs.harvard.edu, eos.org, cab.inta-csic.es, doaj.org, planetary.org, space.com, phys.org, robotics.nasa.gov, mars.nasa.gov, atmos.nmsu.edu, svs.gsfc.nasa.gov | **Not reachable** (proxy refused or no connection) |

**Lorenz et al. (2021) is still not opened.** I tried hal.sorbonne-universite.fr, ntrs.nasa.gov, researchportal.ip-paris.fr, digital.inta.es, and doi.org. A second web search this session returned the same abstract text as before ("typically 0.2%/Sol but ranging 0.05 to 2%/Sol"; cleaning events from dust devils and gusts at Spirit and Opportunity; none at InSight where dust devils are not detected in imaging). Two independent searches agree, so I trust it, but it stays **search-summary**. The dust speed (about 15 times real) therefore also stays search-summary-derived. **Action for a human with access:** open it (open-access PDF at HAL, hal-03369920) and confirm 0.2 %/sol and 0.05 to 2 %/sol.

### Sources opened (all read in full this session)

| ID | Page | URL |
|---|---|---|
| N1 | NASA, "NASA's InSight Waits Out Dust Storm" (JPL, 7 Oct 2022) | https://www.nasa.gov/missions/insight/nasas-insight-waits-out-dust-storm/ |
| N2 | NASA, "NASA's InSight Sees Power Levels Stabilize After Dust Storm" (JPL, 11 Jan 2022, updated 15 Feb 2022; includes "InSight Enters Safe Mode During Regional Mars Dust Storm") | https://www.nasa.gov/solar-system/nasas-insight-sees-power-levels-stabilize-after-dust-storm/ |
| N3 | NASA Science, "NASA InSight's Power Level as of Oct. 8, 2022" | https://science.nasa.gov/blogs/insight/2022/10/10/nasa-insights-power-level-as-of-oct-8-2022 |
| N4 | NASA Photojournal PIA25284, "InSight's Power Generation: After Landing and Spring 2022" | https://science.nasa.gov/photojournal/insights-power-generation-after-landing-and-spring-2022 |
| N5 | NASA, "10 Things: Massive Dust Storm on Mars" (13 Jun 2018, updated) | https://science.nasa.gov/solar-system/planets/mars/10-things-massive-dust-storm-on-mars/ |
| N6 | NASA, "Storm Chasers on Mars Searching for Dusty Secrets" (19 Jul 2018) | https://www.nasa.gov/missions/storm-chasers-on-mars-searching-for-dusty-secrets |
| N7 | NASA Photojournal, "Last Images Opportunity Took" PIA22930 (tau about 10.8) | https://science.nasa.gov/photojournal/last-images-opportunity-took/ |
| N8 | NASA, "On Mars, Light Is Energy" (13 Jun 2018; tau 10.5, 21 Wh) | https://science.nasa.gov/resource/on-mars-light-is-energy/ |
| N9 | NASA Photojournal PIA22737, "Mars Climate Sounder Studies 2018 Dust Storm" | https://science.nasa.gov/photojournal/mars-climate-sounder-studies-2018-dust-storm |
| N10 | NASA Photojournal PIA22329, "Dust Storm Covers Opportunity" | https://science.nasa.gov/photojournal/dust-storm-covers-opportunity/ |
| N11 | NASA Science, Opportunity mission page (2007 storm, 128 Wh) | https://science.nasa.gov/mission/mer-opportunity/ |
| N12 | NASA Science blog, "Persevering Through the Storm" (5 Sep 2024; tau, season wording) | https://science.nasa.gov/blog/persevering-through-the-storm/ |
| N13 | NASA, "2018 Giant Dust Storm on Mars" (MRO MARCI image set, 31 May to 11 Jun 2018) | https://science.nasa.gov/resource/2018-giant-dust-storm-on-mars |
| W1 | Wikipedia, "Timeline of Opportunity" (secondary; cites NASA) | https://en.wikipedia.org/wiki/Timeline_of_Opportunity_(rover) |
| W2 | Wikipedia, "Climate of Mars" (secondary) | https://en.wikipedia.org/wiki/Climate_of_Mars |
| W3 | Wikipedia, "2018 Mars global dust storm" (secondary) | https://en.wikipedia.org/wiki/2018_Mars_global_dust_storm |

## Summary

| # | Question | Short answer |
|---|---|---|
| 1 | Does the game's 60 % worst-case storm cut hold up? | **Yes, as a strong regional-to-early-global storm.** Real: InSight regional storm −35 % (verified); Opportunity 2018 on its way into the global storm −28 %, −47 %, −80 %, −97 % at tau 1.0, 2.1, 3.0, 10.8 (calc from opened table). The game's 60 % sits at about tau 2.4 (calc). Keep the number; fix the wording of what it stands for |
| 2 | Is "about 1 sol warning" honest? | **Yes, and conservative.** Real warning ranged from about 1 sol (a lander reading its own sky) to about 12 sols (an orbiter spotted the Oct 2022 storm 3,500 km away) |
| 3 | Storm length | **Game 0.5 to 1 sol is much shorter than real**: local about 3 days, regional weeks, global months. Keep, but name the compression in the journal |
| 4 | F1 key (dust storms cut solar power) | **Accurate.** Two effects: dimmer sky (while it lasts) and dust on the panels (after). Only the first matters at onset |
| 5 | Dust fallout after a storm | **"Weeks" (verified, NASA).** The journal draft says "days"; change to "weeks" |
| 6 | Wind | **Do not claim "thin air" for the whole game.** True now (about 600 Pa). At the game's 30 to 75 kPa, the same wind pushes harder; see §5 |
| 7 | Monitor cue | **Use haze (tau) and day-night temperature spread. Do not use a pressure cue** (§6) |
| 8 | Dust cleaning frequency | **About 0.15 % per sol (one per 700 sols)** at the best sites, two sites agree (calc). Game 1 % per sol is about 7 times real. Fine if named |
| 9 | Corrections to my earlier review | Two: what ended Opportunity is now "believed" drained batteries (verified); and InSight's whirlwinds were detected by its weather sensors, but none cleaned the panels (verified). See §8 |

---

## 1. Dust on the arrays (reopened)

| Item | Figure | Tag | Source |
|---|---|---|---|
| Typical decline | **0.2 % of output per sol**, range **0.05 to 2 % per sol** | **search-summary** (two searches agree; paper not opened) | Lorenz et al. 2021 abstract |
| InSight, first selfie to spring 2022 | About 5,000 Wh/sol after landing; about 500 Wh/sol by spring 2022 | **verified (opened)** | N4 |
| InSight, number of sols | Last selfie "April 24, 2022, the 1,211th Martian day, or sol" | **verified (opened)** | N1 |
| InSight, as a rate | (500 / 5,000)^(1/1,211) is a 0.19 % loss per sol, compounding. Includes season and haze, not dust alone | **calc** | N4, N1. Sits next to the 0.2 % "typical" |
| InSight, why it matters | The team said the mission would end "between late October of this year and January 2023" from the dust trend alone | **verified (opened)** | N1 |
| Dust builds between storms, steadily | "The mission carefully monitors the lander's power level, which has been steadily declining as dust accumulates on its solar arrays" | **verified (opened)** | N1 |
| Opportunity | Per-sol rate: no clean figure. The Wikipedia timeline shows the "dust factor" going from 0.787 (early 2008) to 0.469 (Feb 2012) with cleaning events in between, so no rate can be read from it | opened, secondary; **rate missing** | W1 |

**Game honesty line (unchanged from dust-review §1).** The game's 3 sols per stage is about 3.3 % per sol (calc), about 15 times the typical real 0.2 %. Keep and label.

## 2. Cleaning events (reopened)

| Fact | Figure | Tag | Source |
|---|---|---|---|
| Wind has cleaned real panels | "Martian breezes proved a saving grace ... sweeping away accumulated dust" (Spirit, Opportunity) | **verified (opened)** | N5 (#3) |
| Whirlwinds and gusts have helped clear panels at Spirit and Opportunity | Yes. **At InSight: "many passing whirlwinds" were detected by its weather sensors, "none have cleared any dust"** | **verified (opened)** | N2 |
| Opportunity events named in the timeline | Nov 2005 (to about 720 Wh, 80 % of max); a series from Apr 2007 (to above 800 Wh/sol); Apr 2009 (+about 40 %, to 515 Wh); Apr 2012 (small); Dec 2012 (small, +about 40 Wh/sol); 31 Dec 2013 to 1 Jan 2014; two in Mar 2014 | opened, secondary | W1 |
| Count and frequency | **At least 7 named events in 5,111 sols** = at most about 0.14 % per sol, about one per 730 sols. The timeline is a summary, so this is a **lower bound** on events | **calc** | W1 (5,111 = last sol, from N7) |
| Spirit | 3 events in about 2,000 sols = 0.15 % per sol | search-summary + dust-review §2 | Lorenz via search |
| Agreement | The two best-cleaned sites both come out near **0.15 % per sol** | calc | |
| Size of the Opportunity events | +40 % (Apr 2009), about +10 % each (Mar 2014, search-summary), to 80 % of max (Nov 2005) | opened, secondary / search-summary | W1 |

**Game check.** Dust devil 1 % per sol per panel group (balance sheet §2a) is about 7 times the real 0.15 % [calc: 1 ÷ 0.14]. Acceptable and labelled; a standard game (about 120 sols) then sees about one event, where real sites would see about 0.2 per 120 sols. Keep.

## 3. Storm optical depth and solar loss (the core numbers)

**What tau is.** Tau is the "optical depth": a measure of how much light the dust blocks (NASA's own wording: "how opaque the atmosphere was", N7; "how much light is being blocked by atmospheric dust", N9). NASA states the anchor point: **"A tau of three indicates that only about 5 percent of the sunlight entering the atmosphere directly reaches the surface"** (N9, verified). The direct beam falls as e^(−tau) [standard physics, calc]: tau 1 gives 37 %, tau 2 gives 14 %, tau 3 gives 5 % (matches N9), tau 5 gives 0.7 %.

### 3.1 Opportunity 2018: power against tau, day by day

| Date (sol) | Tau | Array energy, Wh per sol | Change from 29 May (652 Wh) [calc] | Direct beam, e^(−tau) [calc] |
|---|---|---|---|---|
| 8 May (5079) | not given | 667 | — | — |
| 29 May (5100) | not given | 652 | 0 % | — |
| 3 Jun (5105) | about 1.0 | 468 | −28 % | 37 % |
| 4 Jun (5106) | 2.1 | 345 | −47 % | 12 % |
| 6 Jun (5107) | 3.0 | 133 | −80 % | 5 % (NASA: "about 5 percent", N9) |
| 10 Jun (5111) | about 10.8 (NASA N7) or 10.5 (NASA N8) | 22 (W1) or 21 (NASA N8) | **−97 %** | about 0.002 % |

Tag: the energy and tau series is **opened, secondary** (W1, which cites NASA); the 10 June figures and tau are **verified (opened)** on NASA pages (N7, N8). **Source conflict, recorded:** NASA's own pages give tau 10.8 (N7) and 10.5 (N8), and 21 Wh (N8) against 22 Wh (W1). Use "more than 10" and "about 97 percent" in text.

### 3.2 Other storms

| Storm | Result | Tag | Source |
|---|---|---|---|
| **2007 global storms (Opportunity)** | Array reported **128 Wh on 18 Jul 2007, the lowest for either rover at that point**; normal about 700 Wh/day; below 150 Wh/day the rover must drain its batteries. 128 ÷ 700 is **about −82 %**. 99 % of the direct beam blocked at tau 4.7. About six weeks of storms. Peak tau about 5.5 (estimate, W1) | NASA 128 Wh **verified (opened, N11)**; 700, 150, 99 %, 5.5: **opened, secondary (W1, W2)**; −82 % **calc** | N11, W1, W2 |
| **InSight, regional storm, Oct 2022** | 425 to 275 Wh/sol = **−35 %** (calc: 150 ÷ 425 = 35 %). Haze thickness up "by nearly 40 %". Low of 275 on about 6 Oct; 300 on 8 Oct as it waned | **verified (opened)** | N1, N3 |
| InSight, regional storm, Jan 2022 | Safe mode on 7 Jan; contact back 10 Jan; "almost as much power as they did before the storm" several weeks after the end. No Wh figure on the page | **verified (opened)** | N2 |
| Local storm, Opportunity sol 631 (Nov 2005) | "Mild dust storm that lasted three days"; rover drove in auto mode but took no post-drive images; no power figure | opened, secondary | W1 |
| Curiosity (2018) | Peak tau about 8.5 at Gale; **nuclear power, so no solar loss** | search-summary (tau); N5, N1 confirm Curiosity is nuclear | |

### 3.3 Percent-loss ranges to use (all at the same site, against a clear day)

| Storm class | Typical size | Power loss at the lander | Duration at one site | Tag |
|---|---|---|---|---|
| **Local** (a few hundred km; "about 2,000 per Mars year" is a search/wiki count) | Hours to about 3 days | **Small to moderate; no measured figure found** (real value missing). Tau rises toward 1 | about 3 days (sol 631 storm) | duration opened, secondary; **loss missing** |
| **Regional** (continent-size, e.g. 3,500 km away) | Weeks | **about −35 % (InSight, 2022); about −28 to −47 % on Opportunity's way into the 2018 storm at tau 1 to 2** | days to weeks (InSight Oct 2022: seen 21 Sep, power low about 6 Oct, "dust ... will continue falling out for weeks") | **verified (opened)** and calc |
| **Global** ("planet-encircling"; every 3 to 4 Mars years, roughly 1 in 3 Mars years) | Months | **about −80 % (2007) to −97 % (2018)** at tau 3 to 11 | 2007: about 6 weeks; 2018: global by 20 Jun, near peak about 3 weeks, tau below 1.5 over Opportunity by early Sep, "multi-month decay" | **verified (opened)** (N6, N9, N11) and W1 |

Frequency of global storms: NASA lists **1971, 1977, 1982, 1994, 2001, 2007 (and 2018)** (N5, verified) and says "only appear every six to eight years (that's 3 to 4 Mars years)" (N6, verified). W2 says a planet-wide storm in a given Mars year is "approximately one in three" (opened, secondary). The two agree loosely. **NASA's pages disagree on season wording** (N5: regional storms in "northern spring and summer"; N1: "more of them, and bigger ... during northern fall and winter"; N12: highest "around ... early spring in the northern hemisphere"). The commonly used range Ls 180 to 360 (southern spring and summer, the perihelion season, Ls about 250) comes only from search summaries. **Do not teach a storm season in the game.** W2 (opened, secondary) says storms are "most common during perihelion". Season is therefore **unverified** here.

### 3.4 Diffuse versus direct light: why power does not hit zero

The key insight for the F1 explanation and for high school depth. The rover still made power when almost no direct sunlight arrived.

| Tau | Direct beam left, e^(−tau) [calc] | Power left (Opportunity 2018) [calc, §3.1] |
|---|---|---|
| 1.0 | 37 % | 72 % |
| 2.1 | 12 % | 53 % |
| 3.0 | 5 % | 20 % |
| 10.8 | 0.002 % | 3 % (22 Wh) |

At every step the array kept more power than the direct beam alone would give. The gap is **scattered (diffuse) light**: dust scatters sunlight in all directions, so some still reaches the panel from the whole sky, as on an overcast day. Evidence in what I opened: NASA's own wording is that the dust "blocked 99 percent of **direct** sunlight" (W1, quoting NASA), the rover still made 21 to 22 Wh at tau about 10.5 (N8), and the Opportunity team described the sky change as "from a clear, sunny day to that of an overcast one" (N6, verified). **The step from these numbers to "scattered light is what is left" is my reasoning (calc)**; I did not open a paper that measures the diffuse fraction. The baseline is also imperfect (the clear-day tau was not 0, and the array was already dusty at 652 Wh). Say "some light still arrives, scattered across the sky".

**Not one curve.** Power against tau also depends on how dusty the array is, the Sun's height, and the season. 2007 gives about 280 Wh at tau 4.1 and about 150 Wh at tau 5 (search-summary of a NASA status report) against 2018's 133 Wh at tau 3.0. These cannot be laid on one line. Do not draw a "tau to percent" chart in the game as if it were a law.

### 3.5 What the game's storm cut stands for

Game: output = stage factor × (1 − 0.6 × storm cap) (balance sheet §2a). The cuts are 60 %, 48 %, 30 %, 15 % at storm caps 1.0, 0.8, 0.5, 0.25 (calc).

| Game | Real analogue (calc against §3.1 and §3.3) |
|---|---|
| Cap 1.0, −60 % (Phase 1) | tau about **2.4** (between −47 % at 2.1 and −80 % at 3.0, by straight-line reading; calc). A strong regional storm or the early days of a global one. **Milder than the real worst (−80 to −97 %)** |
| Cap 0.8, −48 % (Phase 2) | about the 2018 reading at tau 2.1, or a bit more than InSight's −35 % regional storm |
| Cap 0.5, −30 % | about InSight's regional storm (−35 %) |
| Cap 0.25, −15 % | a hazy week; no real measured analogue (loss for local storms: **real value missing**) |

**Verdict: reasonable, keep 60 %.** The F1 journal line must say the game's worst storm is milder than the real worst (it already does). I recommend no number change (§9).

## 4. Warning time: what a lander can see, and from where

| Source of warning | What it shows | Lead time | Tag |
|---|---|---|---|
| **Orbiter maps (MRO's MARCI camera, daily global maps)** | NASA calls these "an early warning system for spacecraft on the Martian surface" (N2). Oct 2022: first seen **21 Sep**; haze at InSight up about 40 % by **3 Oct**. That is 12 Earth days, about **11.7 sols** [calc]. The storm was 3,500 km away (N1) | **About 12 sols** | **verified (opened)** |
| 2018 storm: MRO | First detected 30 May to 1 Jun (NASA pages differ: N6 says 30 May, N10 says 1 Jun, the MARCI image set N13 says first detected 31 May); the storm was about 1,000 km from Opportunity (W3/W1). Power fell on 3 Jun (−28 %) | **about 3 to 5 sols** from first detection to the first power loss | calc; dates conflict by 1 to 2 days |
| **Lander's own sensing of the sky** | Tau rises: 1.0 (3 Jun) to 2.1 (4 Jun) to 3.0 (6 Jun). The team made plans on 4 and 5 Jun for the lower power (W1) | **About 1 sol** between "clearly rising" and "−47 %"; about 2 sols to −80 % | opened, secondary; calc |
| **Pressure or wind as a warning signal** | Not used as a warning in any source I opened. Pressure tides grow during storms (search-summary, §6) | — | missing |

**Verdict.** The game's "at least 1 sol warning, every phase" (DESIGN, 82) matches the **fastest real case**, a lander reading its own rising haze. An orbiter gives days. So the number is an honest minimum and a fair simplification. Two suggestions (both the Lead Game Designer's call):

1. **First storm (F1): warn about 2 sols ahead**, because it carries a planning lesson (C1) and the classroom run is faster. Later storms keep "at least 1 sol". Real orbiters give up to about 12 sols, so a longer lead is also honest.
2. **In-story source.** If the fiction says BELL gets storm maps from a Mars orbiter, that is **real** (MRO did exactly this, N2). It needs one line in the `DESIGN.md` Fantasy and Authenticity audit table.

## 5. Misconceptions: wind, hurricanes, "blown away"

### 5.1 What the numbers say

| Fact | Figure | Tag | Source |
|---|---|---|---|
| Wind speed in storms | Up to about **60 mph (97 km/h)**, but "the Martian air is thin enough that it has just a fraction of the strength of storms on Earth" | **verified (opened)** | N1 |
| Viking, global storm | Wind within an hour rose to 17 m/s (61 km/h), gusts to 26 m/s (94 km/h); "**no actual transport of material was observed** at either site, only a gradual brightening and loss of contrast of the surface material as dust settled" | opened, secondary (quoting Viking results) | W2 |
| NASA on the film | "Mars isn't capable of generating the strong winds that stranded actor Matt Damon's character ... Mars' atmosphere is too thin and winds are more breezy than brutal" | **verified (opened)** | N5 (#5) |
| To lift dust | Winds of 18 to 22 m/s (65 to 79 km/h) are needed | opened, secondary | W2 |
| Mean surface pressure | About 600 Pa, nearly 170 times lower than Earth's 101,000 Pa | opened, secondary | W2 |
| How a wind pushes | The push per area is 1/2 × air density × speed² [standard physics] | calc | |
| Density today | ρ = P ÷ (R × T) with P = 600 Pa, R = 188.9 J/(kg K) for CO2 and T = 210 K (**my assumption** for a typical mean) gives **0.015 kg/m³**, about 1/80 of Earth's 1.2 | calc | |
| Result | A 97 km/h (27 m/s) wind pushes with about 5.5 Pa on Mars and about 440 Pa on Earth [calc]. **On Earth, a wind of only about 11 km/h gives the same push** | calc | |

### 5.2 Corrections at two depths

| Misconception | Grade 6 to 8 correction (about grade 6 to 7 reading level) | High school correction |
|---|---|---|
| **"A Mars storm can blow a lander or a base away"** (from the film *The Martian*; NASA names this itself, N5) | The Mars wind can be fast, but the air is very thin. Thin air has little push. A 97 km/h wind on Mars pushes about as hard as an 11 km/h breeze on Earth. A storm fills the sky with dust, but it does not knock things over. | Wind force depends on air density times speed squared. Mars's surface air is about 1/80 as dense as Earth's, so the same wind gives about 1/80 of the push. Real storms blocked sunlight and killed Opportunity's power, not its structure. Wind does lift very fine dust once it reaches about 18 to 22 m/s. |
| **"A Mars dust storm is like an Earth hurricane"** | Both are big. But a hurricane is full of water and heavy, fast wind. A Mars storm is a huge cloud of fine dust in thin air. It can cover the whole planet and last for weeks, but it is more messy than violent. | A hurricane gets its energy from warm ocean water and brings rain and storm surge. A Mars dust storm is powered by sunlight warming the dust itself (NASA: "solar heating of the dust lofted into the atmosphere changes temperatures, which changes winds, which may amplify the storm by lifting more dust", N6). Its main effects are dimming and heating the sky, not wind force. (Hurricane mechanism: standard meteorology, general knowledge; **not opened** here.) |
| **"A storm is gone in a day"** (a risk the game creates: 0.5 to 1 sol) | Real storms last days to months. The game makes them short so you can play through them. | Local about 3 days; regional weeks; global months (§3.3). The game compresses a storm to 0.5 to 1 sol. |
| **"With no direct Sun, the panels make nothing"** | Dust spreads sunlight across the sky, like an overcast day. Opportunity still made a little power at the darkest point. | Scattered light remains when direct light is gone (§3.4). That is why the arrays made 21 to 22 Wh at tau above 10. |
| **"Storms only matter because they cover the panels"** | A storm does two things to power: it dims the sky while it lasts, and it can leave dust on the panels. | NASA says the same (N2: "Dust reduces sunlight filtering through the atmosphere, and it can also accumulate on the panels"). Our game keeps them as two separate cause lines. |
| **"Storm dust falls and the sky is clear at once"** | The dust takes weeks to fall. | N3: "dust particles will continue falling out of the atmosphere for weeks". N1: dust "slowly drops back down, sometimes taking weeks". |
| **"Dust storms make everything colder because the Sun is blocked"** | Daytime gets cooler, but the dust also holds heat, so nights are warmer. | The day-night swing shrinks. Viking: from about 50 °C to about 10 °C (W2, secondary). Dust absorbs sunlight and heats the air (NASA N5 #9, verified). |

**Warning on the thin-air explanation (new, important).** "Thin air" is true only at today's pressure. In the game, pressure rises from about 0.6 kPa to 75 kPa. Air density rises in proportion at a fixed temperature [calc], so the same wind pushes harder every phase:

| Pressure | Density at 210 K [calc] | A 97 km/h wind feels like, on Earth [calc] |
|---|---|---|
| 0.6 kPa (today) | 0.015 kg/m³ | about **11 km/h** |
| 5 kPa (Phase 2 gate) | 0.126 kg/m³ | about **31 km/h** |
| 30 kPa (Phase 3 gate) | 0.76 kg/m³ | about **77 km/h** |
| 75 kPa (equilibrium) | 1.9 kg/m³ | about **120 km/h** (more than Earth air at the same speed) |

(Warmer air is lighter; I used 210 K throughout as a fixed assumption, so later rows are rough and overstate the density somewhat. At 75 kPa the real air temperature would be higher.) **So `DESIGN.md` lines 139 and 229 ("No damaging wind force: Mars's thin air can't do that") are true for Phases 1 and early 2 only.** For Phase 3 and equilibrium, the reason storms are weak must be the game's: "storms weaken as the world stabilizes" (storm cap, invented). It must not keep citing thin air. See handoffs.

## 6. Pressure and temperature during a storm (for an optional Monitor cue)

| Signal | What happens | Size | Tag | Source |
|---|---|---|---|---|
| **Temperature, day-night swing** | Narrows sharply. Viking global storm: about 50 °C to about 10 °C range. REMS at Gale (2018): range fell about 35 K at the ground and 56 K in the air | Viking opened, secondary; REMS search-summary | W2; Viúdez-Moreiras et al. 2019 (search-summary) |
| **Temperature, average** | Surface cooler by day, atmosphere warmer. 2001 storm: atmosphere about +30 K, surface cooler. 2018 at Gale: daily mean down about 2 K (ground) and 4 K (air) | 2001 opened, secondary; 2018 search-summary | W2; Viúdez-Moreiras |
| NASA's summary | "The same swirling dust that blocks out sunlight also absorbs heat, raising the ambient temperature surrounding Opportunity"; the dust "acts as an atmospheric insulator, keeping nighttime temperatures from dropping" | **verified (opened)** | N5 (#9), N6 |
| **Pressure** | Daily pressure tides grow. 2018 Gale: semidiurnal tide amplitude up to about 40 Pa; normal tides up to about 50 Pa (10 % of 600 Pa) | 40 Pa search-summary; 10 % opened, secondary | Guzewich et al. 2019 (search-summary); W2 |
| Mean pressure change during a storm | **Not found** (real value missing) | — | |
| Light and UV | Peak tau 8.5 at Gale, about 97 % of solar UV at the surface cut | search-summary | |
| Dust devils | Stop for weeks: less temperature contrast. (Fewer cleaning events during a storm) | search-summary; N6 has the reasoning ("less direct sunlight and lower daytime temperatures; this might mean fewer devils") | N6 verified (as a hypothesis) |

**Recommended Monitor cues (one view, my lens; the Game Designer decides).**

| Cue | Honest? | Use |
|---|---|---|
| **Sky haze (tau) row**: "Haze 0.5, clear" to "Haze 2.4, storm" | Yes (the quantity NASA uses; every number in §3 is tau-linked) | Best cue. Matches the existing "Storm: less sunlight −n %" cause line. High school depth: show tau; middle school: show a plain word ("clear / hazy / storm") |
| **Day-night temperature spread** during a storm ("Nights warmer, days cooler") | Yes (verified qualitatively) | Good second cue; a genuine silver lining that teaches "dust is not only bad". Show **after** F1 locks (CR-F9) so it cannot give the answer away |
| **Wind with a "feels like on Earth" line** ("Wind 60 km/h, feels like 7 km/h on Earth") | Yes (calc §5) | Optional. Directly kills the "blown away" misconception and teaches why wind will matter more as pressure rises. The conversion can use the live Planetary Index pressure |
| **Pressure cue (storm raises or lowers pressure)** | **No.** The tide change is about 40 Pa (0.04 kPa), invisible next to a 5 kPa gate, and "mean pressure change" is missing. A pressure cue invites the false idea that storms add or remove atmosphere | **Do not build** |

## 7. Penalty numbers: the verdict table (for the Game Designer)

| Balance-sheet item | Real comparison | Verdict | Suggested change |
|---|---|---|---|
| Storm cut 60 % × cap (§2a) | InSight regional −35 %; Opportunity 2018 −28/−47/−80/−97 %; 2007 −82 % | Reasonable. Cap 1.0 equals tau about 2.4 | **No number change.** Update the Tag cell: "Opportunity −82 % (2007), −97 % (2018), InSight −35 %: verified/calc, see storm-dust-review §3" |
| Storm duration 0.5 to 1 sol (drift events, Phase 3) | Local about 3 days; regional weeks; global about 6 weeks to 4 months | Compressed about 3 to 100 times | **No change needed**; add the journal compression note; consider **1 to 2 sols** for the Phase 1 storm if the battery curve allows (a full battery lasts about 4.3 sols, balance sheet line 244) |
| Warning at least 1 sol | 1 sol (own sensors) to about 12 sols (orbiter) | Honest minimum | Keep. Option: 2 sols for F1 |
| Storm adds +1 stage (+2 at cap ≥ 0.8) at storm end | NASA: dust falls "for weeks" (verified), and storms can leave a dust layer (N2). Opportunity ended 2007 dustier, Spirit cleaner (search-summary) | Fair, invented. Upgrade the basis from "searched" to "verified" for *fallout lasts weeks* | Change the cell tag; keep the numbers |
| Dust devil 1 % per sol | About 0.15 % per sol at two sites (calc) | About 7 times real; labelled | Keep; add the Opportunity count to the tag |
| Storms "less often as pressure and biomass rise" (DESIGN) | No analogue. Denser air lowers the wind needed to lift dust (W2: low density is why 18 to 22 m/s is needed today) | **Invented; arguable the other way for real physics.** | Keep as a game rule; tag it **invented** and do not give a science reason |
| "No damaging wind force" | True at 0.6 kPa; not at 30 to 75 kPa (§5) | Qualify | Say "at today's pressure"; for Phase 3 keep the rule as a game choice |
| Battery 10 units vs a storm (§2b) | Real rovers survive storms by cutting loads and sleeping (N11, W1: Opportunity talked to Earth once every three days in 2007) | Fits | None |

## 8. Corrections to my earlier reviews (honest record)

| Earlier statement | New finding | Source |
|---|---|---|
| dust-review §12 "What ended it: **real value missing** (I will not claim it)" | NASA (JPL) now read in full: "**Drained batteries are believed to have caused the end of NASA's Opportunity rover**" in the 2018 storms. "Believed" stays; it is not proven | N2 verified. Another NASA page (N1) says the storm "brought ... Opportunity rover to an end". Use "believed" |
| dust-review §2 "dust devils were not detected in InSight's images" (search-summary) | NASA: InSight's weather sensors **did** detect "many passing whirlwinds", but **none cleared any dust** | N2 verified |
| dust-review §12 "last message 10 Jun 2018, 21 Wh, down 97 %" (search-summary) | Now: 21 Wh (N8) and 22 Wh (W1); loss −97 % is calc from 652 → 22; tau 10.8 (N7) or 10.5 (N8) | N7, N8, W1 |
| dust-review §12 "three weeks" (my reading, unverified) | Real chain: storm first seen 30 May to 1 Jun; power −28 % on 3 Jun; last contact 10 Jun. That is about **10 to 11 days** from first detection to last contact | N6, N10, W1 (calc) |
| InSight tau 1.45 against 0.6 to 0.7 (search-summary) | Still not opened. NASA's opened wording is "haze thickness up by nearly 40 %". These are different measures and I cannot reconcile them. **Use the opened "nearly 40 %"** | N1 |

## 9. Balance-sheet numbers to change (the Game Designer edits; I did not touch the sheet)

| # | Where | Now | Change to | Why |
|---|---|---|---|---|
| 1 | §2a "With a storm", Tag cell | "Opportunity −80 % (2007), −97 % (2018); InSight regional storm about −35 % (**searched**)" | "Opportunity about −82 % (2007: 128 Wh against about 700), −97 % (2018: 652 → 22 Wh); InSight regional −35 % (425 → 275 Wh). **Verified (opened)** / **calc**; storm-dust-review §3. Cap 1.0 ≈ tau 2.4" | Upgrades "searched" to opened and calc |
| 2 | §2a "Storm adds", Tag cell | "real fallout continues as storms fade (SME)" | "NASA: dust keeps falling out for **weeks** after a storm (**verified**, NASA 2022); dust can also add to the panel layer (**verified**)" | Opened source |
| 3 | §2a "Dust devil chance" Tag | "Spirit ≈ 0.15 %/sol (calc from 3 events in ~2,000 sols)" | Add: "Opportunity: at least 7 named events in 5,111 sols ≈ 0.14 %/sol (**calc**, lower bound, Wikipedia timeline citing NASA). 1 % is about 7 times real" | A second site agrees |
| 4 | §2a "Real accumulation" | "(**Searched** ...)" | Keep "searched" and add: "InSight 1,211 sols to the spring 2022 selfie (**verified**, NASA 2022); 0.19 %/sol compounding (**calc**)". Lorenz 2021 still not opened | |
| 5 | Drift events table (line 393) "Solar −60 % for 0.5–1 sol" | untagged | Tag **invented**; add "real storms last about 3 days (local) to months (global); the game compresses them" | Honest compression |
| 6 | Drift events (line 389) "Storm events announced at least 1 sol ahead" | untagged | Add "real: about 1 sol (a lander's own sky) to about 12 sols (orbiter map, Oct 2022); **verified**" | Opened |
| 7 | §2 storm cap ladder 1.0 / 0.8 / 0.5 / 0.25 | "(invented)" | Add real anchors: cap 1.0 → −60 % ≈ tau 2.4; cap 0.5 → −30 % ≈ InSight regional (−35 %) | calc |
| 8 | Line 51 "the 2018 storm ended Opportunity's mission" | | "the 2018 storm left Opportunity without enough power; NASA believes drained batteries ended the mission (**verified**, JPL 2022)" | Wording fix (§8) |
| 9 | Optional | Storm length 0.5 to 1 sol; first storm warning ≥ 1 sol | Phase 1 storm **1 to 2 sols**, F1 warning **2 sols** | Closer to real; both survivable with the 4.3-sol battery (**calc** in the sheet); Designer's call |
| 10 | Not a sheet number, DESIGN.md lines 139 and 229 | "Mars's thin air can't do that" | "At today's pressure, thin air cannot do that. Our storms stay weak by design" (§5) | Honesty in later phases |

## 10. Text: F1 keyed answer and explanation (reading level marked)

**F1 keyed answer (unchanged):** "Solar power drops. Dust in the air blocks sunlight." Round2-review already found it accurate; this review adds the mechanism and numbers.

| Depth | Text | Words | Level | Claim tags |
|---|---|---|---|---|
| **Middle school (about grade 6 to 7)** | **Why solar power drops.** In a dust storm the air fills with fine dust. The dust blocks the Sun's light, so less reaches your panels. Some light still gets through, scattered across the sky, like on a cloudy day. When the storm ends, dust settles out of the air over weeks and can coat the panels. | 56 | about grade 6 | NASA: dust reduces sunlight and can accumulate (**verified**, N2); weeks (**verified**, N1); overcast (**verified**, N6) |
| **High school (read more)** | **Optical depth.** Scientists measure how much dust blocks the light with a number called tau. At tau 3, only about 5 percent of the Sun's direct light reaches the ground (NASA). Opportunity's solar power fell about 80 percent at tau 3 and about 97 percent near tau 11 in 2018. It did not fall to zero, because scattered light still reaches the panels. Dust in the air and dust on the panels are two separate losses. | 76 | about grade 9 | 5 % at tau 3 (**verified**, N9); 80 and 97 % (**calc** from opened energy table, W1 and N8); scattered light (calc, my reasoning) |
| **Ships without numbers (fallback if W1 table is not accepted as a source)** | In a dust storm the dust blocks the Sun. Panels make less power. In 2018 a storm covering all of Mars left the Opportunity rover with almost no power. | 29 | about grade 6 | N8 verified (21 Wh) |

**Real-versus-game line for the F1 page (replaces the draft in journal-dust §6; wording change only):** "In 2018, a storm that covered all of Mars cut the rover Opportunity's power by about 97 percent. Your storms cut 60 percent at most. Real storms can be bigger, and they last days to months." (Tags: 97 % **calc** from opened sources; 60 % **invented**; durations **verified**.) **Opportunity's fate:** if mentioned, say "NASA believes its batteries ran out", not "the storm destroyed it."

**Wind note (new journal entry `journal.storm.wind`, trigger: first storm after F1 resolves).** MS: "The wind in a Mars storm can reach 60 miles an hour. But the air is so thin that it pushes about as hard as a gentle breeze on Earth. Storms on Mars dim the sky. They do not knock things over." Tags: 97 km/h (**verified**, N1); gentle breeze (calc, §5). HS adds the density formula and the "wind matters more as the air thickens" line. Reading level about grade 6. **Do not ship this entry in Phase 3 or later unchanged** (§5 warning); in later phases the line becomes: "The air is thicker now, so wind pushes harder than it did at the start."

## 11. Handoffs

> **To:** Lead Game Designer · **Problem type:** well-structured (the numbers), ill-structured (item 2)
> **Problem:** Balance-sheet tags and a few storm numbers need the opened sources; two DESIGN lines overstate "thin air".
> **Context:** `docs/science/storm-dust-review.md` §3, §5, §9.
> **Your view:** No penalty number must change. Ten sheet edits are listed in §9. The first storm could last 1 to 2 sols with a 2-sol warning (optional).
> **Needed from them:** (1) apply §9 items 1 to 8 and 10; (2) decide §9 item 9 (storm length and F1 warning); (3) decide the optional Monitor cues (§6): haze row, day-night spread, "feels like" wind. (4) If BELL's warning comes from an orbiter, add one row to the Fantasy and Authenticity audit table (real: MRO).
> **Done when:** the sheet and DESIGN.md cite this file and no longer say "thin air" for later phases.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** Journal wording and one new entry.
> **Context:** `docs/learning/journal-dust.md` §3 (`journal.dust.storm_timing` says "days") and §6 (`journal.f1.real_vs_game`); this file §10.
> **Needed from them:** (a) change "days" to "weeks" in `journal.dust.storm_timing`; (b) upgrade `journal.f1.real_vs_game` to the §10 text (the 97 % figure now has opened sources; fallback is no longer required but may stay); (c) replace the "ended Opportunity" wording with "believed"; (d) add `journal.storm.wind` and the F1 explanation (§10) with goal IDs (B1, C1, A1); (e) check the "Not one curve" caution before any tau chart is drawn.
> **Done when:** journal-dust.md carries the new wording and tags.

> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** Storm values must be data, and the optional wind line needs the live pressure.
> **Context:** §5 (feels-like table), §9.
> **Needed from them:** keep storm cut, cap, duration, warning lead, and dust-devil chance in datapack JSON (already required in dust-review). If the Designer approves the "feels like" wind cue, the conversion is 97 km/h × sqrt(P ÷ (188.9 × 210 × 1.2)) with P in Pa, as a pure function in `mars.core` with a unit test against the §5 table (values 10.9, 31.4, 77.0, 121.8 km/h at 600, 5,000, 30,000, 75,000 Pa).
> **Done when:** values are in JSON and the test exists (only if the cue is approved).

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** Storm sky reference.
> **Context:** `docs/art/references/PIA25286.jpg` (InSight, 2022, which I viewed): even orange-brown veil on the array, cells invisible, **soft-edged shadows** in a hazy sky.
> **Needed from them:** for storm keyframes, soften shadows and lower contrast (diffuse light is the real effect, §3.4) rather than only darkening the sky. Not a hard requirement.
> **Done when:** the storm keyframe note mentions soft shadows.

## 12. Risks

| Risk | Effect | Mitigation |
|---|---|---|
| Lorenz 2021 still not opened | 0.2 %/sol and 0.05 to 2 % stay search-summary; "about 15 times faster" inherits that tag | A human opens the HAL PDF; until then the journal ships the no-number fallback |
| The energy and tau table (W1) is Wikipedia citing NASA | The 97 % and −28/−47/−80 % figures are secondary | NASA's own pages give 21 Wh and tau 10.5/10.8 (verified); the earlier points are in W1 only. Use "about 97 percent" and keep the day-by-day table in this file, not in game text |
| Season (Ls) of dust storms unverified; NASA pages disagree | A wrong season could get taught | Not in the game; do not add one |
| "Thin air" reason in Phase 3 | Teaches a physics claim that stops being true as the player thickens the air | §5 handoff |
| Storm length and warning compress reality | Students think storms last a day and warn a sol ahead | Compression notes in the journal (§10) |
| No source for the effect of the game's storm on *temperature* | If the Monitor shows temperature in storms | Use only the qualitative "narrower swing" cue (verified), with no number, until Viúdez-Moreiras 2019 is opened |

## 13. Assumptions

- Mean surface temperature 210 K for the density and "feels like" conversions (my assumption; typical but not opened).
- Baseline for loss percentages is Opportunity's 29 May 2018 value of 652 Wh/sol (W1), already dusty and at a tau near 0.5 (tau not given for that date).
- A "storm" in the game is a solar-dimming event; wind damage stays out of scope.
- The reader's reading level target is grade 6 to 7 at middle-school depth; word counts above were counted by hand.

## 14. Could not open (for the next human with access)

Lorenz et al. 2021; Guzewich et al. 2019 (GRL) and Viúdez-Moreiras et al. 2019 (JGR Planets), for the REMS pressure and temperature figures; Lemmon et al. 2015 (Icarus, MER optical depth record) for the tau-to-power relation; the JPL pages (HTTP 403); NASA's MER status report with the tau 4.12 and tau 5 figures (2007); a measured diffuse-light fraction for Mars; a loss figure for **local** storms.
