# Life-support science review (G2, Stage 3 slice 2)

**Owner:** Subject Matter Expert · **Date:** 2026-11-27 · **Reviews:** `docs/design/balance-sheet.md` section 2d (and `docs/design/dome-layout.md`) · **Docs only.** Nothing built or run in the game.

## 0. How sure am I, and what I could not open

Tags: **verified** (a NASA page or paper I opened, in this or an earlier review), **search-summary** (a NASA source named in a search result summary, which I could not open myself), **calc** (my arithmetic), **invented** (game number), **unverified** (from memory or no source).

**Honest limit of this review.** In this session `science.nasa.gov`, `www.nasa.gov` and `en.wikipedia.org` did **not resolve** when I tried to fetch them (the network blocks them). So I could not open any page today. What I know comes from (a) earlier SME sessions that opened the NASA MOXIE pages (recorded in `dimensions.md`, tag "opened") and (b) web-search result summaries today. I tag the second kind **search-summary**; a person should open the links before a number goes into the journal as "verified". Not opened at all: NASA's Baseline Values and Assumptions Document (BVAD), the ISS water papers, the EMU documents, the Wikipedia "Hypobaric decompression" page, any primary aviation-medicine source. Allowed hosts I did not try because the first two failed the same way: `nssdc.gsfc.nasa.gov`, `images-assets.nasa.gov`.

## 1. Summary table

| # | Game number (balance-sheet 2d) | Verdict | Change? |
|---|---|---|---|
| 1 | 7 O2 units (0.7 kg) per player per sol | **Low by about 20 %.** NASA planning value is 0.84 kg per person per Earth day = about 0.86 kg per sol (calc). Game is 19 % under | **Recommend 8 units** (0.8 kg), maker 10, start fill 14 (see 2) |
| 2a | MOXIE about 12 g/h best, 10 g/h design, 300 W | Verified (earlier opened NASA page; 12 g/h best confirmed by search-summary of NASA pages). "5 to 6 g/h" is the **original goal** (NASA says 12 g/h was twice the goal), not the best | Label only |
| 2b | Oxygen maker 9 units a sol, "3 times MOXIE" | Plausible **calc**. Keep as a game number | Becomes 10 if use becomes 8 |
| 2c | Maker power 1.0 kWh per sol (about 22 times cheaper than real) | **Acceptable if labelled**, with a bigger warning than the sheet planned: it is also **below the physical minimum** (about 5 kWh per kg of O2, calc), so it is a cheat on energy, not only on scale | Keep; journal text in section 3 |
| 3a | Water 3 L per player per sol | In range: 2.4 to 3.5 kg per day drinking and food (search-summary of NASA documents). **Verified as a range** | None |
| 3b | Water recovery 90 % | ISS: 93 to 94 % before the Brine Processor, about 98 % after (search-summary of a NASA page). Game is a bit **below** ISS, which is fair for a new, small dome | None; label it "lower than ISS" |
| 4 | Oven yields 0.5 water unit a sol | **Plausible, invented size.** Mars soil holds about 2 % water by weight (Curiosity, NASA, search-summary). 0.5 L needs about 25 kg of soil a sol (calc) | None; label |
| 5 | Suit 6 units (about 17 min of play), tier 2: 10 units | Real suits: 7 h certified, 8 h nominal (search-summary). The game suit holds **more air relative to a day's need than a real suit does** (section 2.5). Fine as time compression | Label; none |
| 6 | "Faint, wake in your bunk, nothing lost" | OK as a **game rule** if it is called one and the journal says real hypoxia is dangerous and gives little warning | New wording, section 4 |
| 7 | Breach leaks 10 O2 units a sol | A **small crack** (3 to 5 times worse than the worst ISS leak, calc). Real big holes empty a room in **minutes** (calc, unverified coefficient). The game also ignores the air already in the room | Label; none |

Numbers to change if you accept my recommendation are listed in the report (`docs/team/reports/2026-11-27-sme-life-support.md`).

## 2. Detail

### 2.1 Oxygen per person (item 1)

- NASA life-support planning uses **0.84 kg O2 per crew member per day** (**search-summary**: ICES 2004 and ICES 2017 papers and NASA/TP-2015-218570 Rev 2, the BVAD; not opened). About 10 % of the oxygen is also lost to leaks and airlock use in station planning (same summary).
- The earlier figure, 0.68 kg, was **1,000 kg for 4 people for a year** (Wikipedia MOXIE, citing NASA, **opened-secondary** in `dimensions.md` 5.1). The words are "only about 1 metric ton", which is a rounded round-number estimate, so it is a **low** figure, not a competing measurement.
- Sol length: a Mars sol is 24 h 39.6 min (about 24.66 h; the project uses 24.6 h). 0.84 kg per 24 h becomes 0.84 x 24.6 / 24 = **0.86 kg per sol** (calc).
- Gap: 0.7 / 0.86 = 0.81, so the game is **19 % under**. People also breathe more when they work hard (**unverified**, not sourced here); the 0.84 is for an average crew day.
- A sol in the game lasts 20 minutes of play, but **the per-sol numbers are real-sol numbers**. That is the right choice (the designer's rule: survival numbers are per sol and do not scale with pace).

**Recommendation:** use **8 units (0.8 kg)**. It is 7 % under 0.86, rounds sensibly, and keeps the surplus the design relies on if the maker goes to 10 (net +2). Fallback: keep 7 and add the label "0.7 kg is a little below NASA's planning value of 0.84 kg a day".

### 2.2 MOXIE (item 2a)

- Design output **up to 10 g O2 per hour** and **300 W**: **verified** (science.nasa.gov Perseverance instruments page, opened in the earlier session; `dimensions.md` section 3).
- **Best run 12 g/h at 98 % purity or better**, twice NASA's original goal; **122 g total over 16 runs**; last run on 7 Aug 2023 made 9.8 g (**search-summary** of NASA pages: nasa.gov mission-complete article, science.nasa.gov MOXIE blog; not opened today).
- So "5 to 6 g/h" is the **goal** (12 is "twice the goal", so about 6). It is not wrong to say MOXIE made 5 to 6 g/h on early runs (the first run made about 5 g, **unverified**, from memory), but 12 g/h is the best number. The designer's 12 g/h is right.
- MOXIE is a **technology test**. It ran in short runs, never around the clock, and made 122 g in all, about what one person needs in 3 hours (calc: 0.86 kg / 24.6 h = 35 g/h; 122 / 35 = 3.5 h).
- Energy per kg: 300 W / 12 g/h = 25 kWh per kg; at 10 g/h, 30 kWh per kg (calc). The designer's 25 kWh/kg is right for best-run MOXIE.
- NASA's own full-scale concept (2 kg/h with 25 to 30 kW, **opened-secondary** in `dimensions.md`) is 12.5 to 15 kWh per kg: about twice as efficient. Real scale-up helps, but not by a factor of 20.

### 2.3 The "9 air units and about 22 times cheaper power" simplification (item 2b, 2c)

- Output: 3 x 12 g/h x 24.6 h = 886 g = 8.9 units, so 9 is **calc-consistent** with "3 times MOXIE". A person needs about 2.9 MOXIEs at best rate (0.86 kg / 0.295 kg), so "one maker per player, each about 3 MOXIEs" is a fair idea of reality.
- Power: 3 MOXIEs = 0.9 kW, 22 kWh per sol. The game charges 1.0 kWh. Ratio **about 22 times cheaper** (calc; the sheet's number is right). At the NASA full-scale ratio (12.5 to 15 kWh/kg) it would still be about 11 to 13 times cheaper.
- **New point the sheet did not have: the game price is below the physical floor.** Splitting CO2 into CO and O2 (what MOXIE does) or water into H2 and O2 needs at least about **4.9 to 5.0 kWh per kg of O2** (calc: about 283 kJ per mole of CO for 16 g of O2 gives 17.7 MJ/kg = 4.9 kWh/kg; water about 286 kJ per mole of H2 for 16 g of O2, 5.0 kWh/kg; textbook enthalpies from memory, **unverified** here). The game uses **1.1 kWh per kg**. No machine, however good, can do that. So the journal must not say only "cheaper to run"; that sounds like better engineering. It must say the game **gives you a power discount** because oxygen really needs a lot of power.
- Is it acceptable? **Yes, if labelled.** Reason: with real numbers one person needs about 21 kWh a sol (0.86 kg x 25) while a clean solar array in the game makes 5 units; a real dome would need many panels per person and the first hour of play would be about solar fields. That teaches a true thing (oxygen is the hungry load) but swamps the Phase 1 pacing. **Do not let the compression teach the opposite lesson** ("oxygen is cheap"). The fix is the journal wording in section 3 and a Monitor row that shows the maker as the **largest** tier-1 load. Instructional value: the compression keeps C1 and C2 playable; the risk is a transfer error ("making oxygen on Mars is easy"). Appeal: keeps the early game about building and repairing, not about 20 panels.
- Solar check (**unverified**): the game's panel makes 5 units a sol. I did not source InSight's real daily energy from the array in this review; if the journal compares the game panel to a real one, source it first.

### 2.4 Water (item 3)

- **Use.** NASA's STS-126 press kit says an ISS crew member uses about 3.5 L a day; planning tables split drinking about 1.6 and food water about 0.8 kg a day on orbit, and 3.5 kg on Earth-based tables (**search-summary**, not opened; the sources disagree on scope). The game's 3 L sits inside 2.4 to 3.5. **Verified as a range.** Hygiene water (about 25 kg a day in one planning baseline, **search-summary**) is left out on purpose; the journal must say so.
- **Recovery.** NASA says the Brine Processor Assembly helped the ISS reach its **98 % water recovery goal**, from 93 to 94 % before it (**search-summary** of a nasa.gov article and ICES 2023/2024/2026 papers; not opened). Urine processor designed for 85 % (same summary). The game's 90 % is **below ISS**, so it is safe and honest as "a new, small system". The ISS figure covers water from urine, sweat and breath moisture; it does not mean a pipe that loses nothing.
- Loss: 10 % of 3 L = 0.3 L per player per sol (calc, matches the sheet).

### 2.5 Regolith Oven water (item 4)

- Real basis: Curiosity at Rocknest found about **2 % water by weight** in the fine soil, held in amorphous material and released when the sample was heated to about 835 C (**search-summary** of the NASA SAM article; Wikipedia gives 1.5 to 3 wt %, 2.0 +/- 1.3 wt % in another analysis, not opened). Phoenix (far north) found **ice** under the soil (**search-summary**); the amount depends on place.
- Arithmetic (calc): 0.5 L is 0.5 kg, so 0.5 kg / 0.02 = **25 kg of soil per sol per Oven**, or about 17 L of loose soil (assuming about 1.5 kg per L, **unverified**). Heating 25 kg by 500 C at about 0.8 kJ/(kg K) (**unverified**) is about 10 MJ = **2.8 kWh**. The game's Oven draws 1.0 kWh: **within a factor of 3**, so this one is a milder compression than the oxygen maker.
- Verdict: **plausible, size invented.** Use "a few percent" in the journal, not "Mars soil is wet". Teaching risk: students think Mars has ice everywhere. Say: **at the equator the water is locked in minerals; ice sits farther north and under the surface.**
- Journal note: the water a dome player gets is for life support. It is **not** counted in the hydrosphere index (check with the Designer that the two are not double-counted).

### 2.6 Suit duration (item 5)

- Real: the Shuttle/ISS spacesuit (EMU) is certified for **7 hours** of life support (8 hours nominal, longest use 8.93 h) plus 30 minutes backup; the next-generation suit targets **8 hours** (**search-summary** of NASA OIG and NASA technical papers; not opened; the 8 h is a **design goal**, not a flight capability).
- Game: tier 1 = 6 units = about 17 minutes of play; tier 2 = 10 units = about 29 minutes. That is a **time compression** of about 25 times (420 min / 17 min).
- **Check that the compression is consistent (calc):** 6 units at 7 units a sol is 0.86 sol, about **21 hours** of breathing on the game's own clock. A real suit holds 7 h of a 24.6 h day = 0.28 of a day's air. So the game suit carries about **3 times more air relative to daily need** than the real one. This is harmless (a player does not feel it) but the journal must not say "the suit holds 17 minutes of air" as if it were real: say "17 minutes of **your play time**", and that real suits last 7 to 8 hours.
- If the design wants an honest ratio, tier 1 would be 2 units; that is under 6 minutes of play and would be punishing. **Keep 6 and 10.**

### 2.7 Sealing and leak rate (item 7)

- **Real leaks (search-summary, NASA OIG IG-22-005 and a NASA blog dated 2026-06-05; not opened):** a tight ISS leaks **0.1 to 0.3 lb of air a day** (0.05 to 0.14 kg); the PrK crack leaked about 0.6 lb a day at first, then about 2 to 3 lb a day (0.9 to 1.4 kg), and a 2026 NASA note reports about 2 lb a day. Wikipedia gives 3.7 lb (1.7 kg) a day in April 2024 (not opened). These are **air**, mostly nitrogen.
- **Game:** a breach adds 10 O2 units a sol = **1 kg of oxygen**. Oxygen is about 23 % of air by mass (textbook, **unverified** here), so that equals about **4.4 kg of air a day**: **3 to 5 times the worst ISS crack**. So it is a **bigger crack**, and the tight dome with no breach leaks about **0** (real ISS: tiny but not zero). Plausible and clear to the player.
- **What the game ignores (calc):** the dome holds about 700 m3 of air (dome-layout 3). At sea-level density (about 1.2 kg per m3, textbook) that is about 840 kg of air and **about 190 kg of oxygen**, about **220 person-sols**, ten times the 20 kg the wall tanks hold. In real life the room is the biggest store of air, and a **hand-sized hole (10 cm) would empty a room that size in minutes** (calc: choked flow about 1.9 kg per second, coefficient from a textbook formula, **unverified**). So real breaches are either slow (tiny cracks) or fast (large holes); the game shows only the slow kind. This is a fine **game simplification**: the journal must say it.
- Also not modelled: removing the **CO2 that people breathe out**. On a real station the CO2 scrubber is as important as the oxygen maker, and in a sealed room CO2 builds up before oxygen runs short. The game leaves this out; say so in the journal (a short "not in the game" line). Do not add it unless the owner wants a slice for it.
- **Landing-grace** (no leak in Landing) is a pure game rule; no science issue.

## 3. Journal wording (two depths; grade 6 to 7 at middle school)

**Oxygen maker (read more).**

- *Middle school:* "The oxygen maker is based on MOXIE, a real box on the Perseverance rover. MOXIE took in Mars air, which is mostly carbon dioxide, and made oxygen. It made up to 12 grams an hour. A person needs about 0.8 kilograms of oxygen a day, so one person needs about 3 MOXIEs. **In the game, your oxygen maker is a lot cheaper to run than a real one.** Making oxygen takes a lot of power: MOXIE used 300 watts. We gave you a discount so you can build your base. Do not copy the discount to real life."
- *High school:* "MOXIE used about 300 W to make up to 12 g of O2 an hour, about 25 kWh per kg. Even a perfect machine needs about 5 kWh per kg, because the oxygen has to be pulled out of CO2 or water. A person needs about 0.84 kg a day (NASA planning value). The game maker makes 1 kg for 1 kWh, about 22 times cheaper than MOXIE and below the physical minimum. We did this on purpose, so the first hours are about building and fixing, not about covering a field with panels. Real crews would need a power plant for oxygen alone."

**Water (read more).** "A real crew member uses about 2.4 to 3.5 litres of drinking and food water a day. Washing takes much more, and **the game leaves it out**. The space station recycles about 93 to 98 percent of its water; your reclaimer recycles 90 percent." **Oven:** "Mars soil is not wet. In the fine soil Curiosity measured, water is about 2 percent by weight, locked in minerals, and it comes out when the soil is heated. Ice is found farther north, under the surface."

**Suits.** "A real space suit has air for about 7 to 8 hours. In the game your suit lasts about 17 minutes of play, so a trip outside is a short choice. That is a game rule, not a measurement."

**Leaks.** "A real station leaks a very small amount of air every day, and a small crack can be found and sealed. A big hole empties a room in minutes. In the game, a breach is a slow leak so you have time to find it. Real crews also have to remove the carbon dioxide they breathe out; the game leaves that out."

## 4. The faint state (item 6)

**Problem with "faint, wake in your bunk, nothing lost".** It is fine as a game rule (the owner and Designer chose it so a 12-year-old is not punished). The risks are three possible misconceptions: (a) that losing consciousness from lack of oxygen is harmless; (b) that you feel it coming; (c) that someone carries you to bed.

What I can support: gradual hypoxia causes fatigue, dizziness, confusion and unconsciousness; people sense it **poorly**, so aviators train to know their own signs and crews rely on sensors (**search-summary** of the Wikipedia pages on hypoxia, hypobaric decompression and hypobaric chamber; no primary medical source opened, so **unverified** for any quote). At very low pressure the time of useful consciousness is about 9 to 12 seconds, and loss of consciousness can be fatal unless pressure comes back within about a minute (same summary, **unverified**). I would not use those numbers in the game.

**Recommended wording (game text and journal):**

- Name the state **"Blackout"** (not "faint"; fainting is a different medical event). Failure report line: "You blacked out. In the game you wake up in your bunk and lose nothing."
- Journal entry, middle school: "**Game rule:** if your air runs out, you black out and wake up in your bunk. Real life is not like that. Without oxygen a person can lose consciousness and cannot save themselves. The body is poor at noticing low oxygen, so real crews use alarms and sensors. Check your tank."
- High school adds: "On a real station the danger is often carbon dioxide building up, which the body does notice, and low oxygen, which it does not. The game shows only the oxygen warning."
- Keep the on-screen effects mild (the design already says no red flashes). **Do not describe symptoms as a medical guide** ("tunnel vision at X percent") and do not add real numbers for hypoxia. No "hold your breath" or similar encouragement.
- "Short of breath" and "Dehydrated: slower, no sprint" are acceptable: the first is a real, vague symptom; the second is **game rule**. Do not state how many days a person can live without water; I have no verified figure here.
- A teacher note: the state is a **game rule**; classroom text should not suggest students try breath-holding or fasting.

## 5. Sources

Opened in earlier SME sessions (recorded in `dimensions.md`, "opened"): science.nasa.gov Perseverance instruments page (MOXIE 23.9 x 23.9 x 30.9 cm, 300 W, 10 g/h); photojournal.jpl.nasa.gov PIA24201, PIA24203.

**Search-summary only (not opened today; open before using as "verified"):**

1. NASA, "NASA Achieves Water Recovery Milestone on International Space Station", https://www.nasa.gov/missions/station/iss-research/nasa-achieves-water-recovery-milestone-on-international-space-station/
2. NASA, "NASA's Oxygen-Generating Experiment MOXIE Completes Mars Mission", https://www.nasa.gov/missions/mars-2020-perseverance/perseverance-rover/nasas-oxygen-generating-experiment-moxie-completes-mars-mission/
3. NASA Science, "MOXIE Celebrates 2 Years on Mars", https://science.nasa.gov/blog/moxie-celebrates-2-years-on-mars-discoveries-and-work-left-to-do/
4. NASA, "Curiosity's SAM Instrument Finds Water and More in Surface Sample", https://www.nasa.gov/solar-system/curiositys-sam-instrument-finds-water-and-more-in-surface-sample/
5. NASA STS-126 press kit (3.5 L a day), https://www.nasa.gov/wp-content/uploads/2023/05/287211main-sts126-press-kit2.pdf
6. NASA/TP-2015-218570 Rev 2, Life Support Baseline Values and Assumptions Document, https://ntrs.nasa.gov/api/citations/20210024855/downloads/BVAD_2.15.22-final.pdf (NTRS is not on the allowed host list for this task; named from a search result only)
7. NASA OIG IG-22-005 (ISS leak 0.1 to 3 lb a day), https://oig.nasa.gov/wp-content/uploads/2024/02/IG-22-005.pdf (same note)
8. NASA, "NASA Provides Update on Space Station Leak" (2026-06-05), https://www.nasa.gov/blogs/spacestation/2026/06/05/nasa-provides-update-on-space-station-leak/
9. NASA OIG IG-17-018, "NASA's Management and Development of Spacesuits" (EMU 7 h; next suit 8 h), https://oig.nasa.gov/wp-content/uploads/2024/02/IG-17-018.pdf
10. Wikipedia: "Mars Oxygen ISRU Experiment", "Hypoxia (medicine)", "Hypobaric decompression", "Hypobaric chamber", "Rocknest (Mars)", "Zvezda (ISS module)".

**Calc only, constants from memory (unverified):** enthalpies for CO2 and water splitting, air density 1.2 kg/m3 and 23 % O2 by mass, choked-flow coefficient 0.0404, regolith density and heat capacity.
