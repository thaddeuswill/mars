# Science Review: Solar Panel Dust

Owner: Lead Subject Matter Expert. Reviewed: 2026-11-26. Backlog: #22.
Answers: the Lead Game Designer's handoff in `docs/team/reports/2026-11-26-lead-game-designer-panel-dust.md`; checks `DESIGN.md` ("Solar panel dust") and `docs/design/balance-sheet.md` §2a.
Builds on: [round3-review.md](round3-review.md) (tagging conventions).

**Perspective round (my lens).** This succeeds if a student who reads the dust cause line and the journal note comes away with the right direction (dust builds slowly, wind sometimes helps, storms are worse) and the right size of the effect, or is told honestly that the game runs about 15 times faster than the real thing.

## Tags used

| Tag | Meaning |
|---|---|
| **sourced, opened** | I read the page myself this session |
| **searched, not opened** | Seen in a web-search summary only; page blocked or not fetched. Do not put in game text without a second look |
| **calculated** | My arithmetic on tagged inputs (method shown) |
| **invented** | A game balance choice |
| **real value missing** | I could not find a figure |

## How this was checked

| Host | Result |
|---|---|
| science.nasa.gov, assets.science.nasa.gov (photojournal image pages) | **Opened.** Five image captions read in full (§Sources S1–S5); two before/after images viewed |
| en.wikipedia.org | **Opened** ("Cleaning event"), with a 5 s wait between requests. Secondary source; it cites NASA/JPL pages |
| www.jpl.nasa.gov, www.nasa.gov news pages | **Blocked (HTTP 403)** from the command line |
| ntrs.nasa.gov | **Blocked** (proxy refused), tried once |
| hal.sorbonne-universite.fr, pmc.ncbi.nlm.nih.gov, arxiv.org, planetary.org, space.com, universetoday.com, dlr.de, researchportal.ip-paris.fr | **Not reachable** from this session |
| Web search | Source of everything tagged "searched, not opened" |

**The key paper I could not open:** Lorenz, R. D., Reiss, D., et al. (2021), "Lander and rover histories of dust accumulation on and removal from solar arrays on Mars", *Planetary and Space Science* 207, 105337. Its abstract figures come to me only through search summaries (the same text appeared in four separate searches, so I trust it, but it is still "searched, not opened"). **Action for a human with access:** open it and confirm the figures in §1 and the Spirit event count in §2.

## Summary

| # | Question | Short answer |
|---|---|---|
| 1 | Real accumulation | **About 0.2 % of output per sol is typical; the range is 0.05 to 2 % per sol.** Fairly steady, with extra fallout after storms. Per-mission figures below |
| 2 | Wind cleaning | **Real and episodic.** Typically +5 to +10 % per event on Opportunity, up to about +50 % on Spirit (sol 420). InSight never saw one |
| 3 | Dust devils | A dust devil is the likely cause, **not confirmed** for the panel events. Streaks on the ground are real; streaks on the arrays are **not confirmed**. The Opportunity images I viewed look patchy, not striped |
| 4 | Storm dust at the end | **Partly fair.** Fallout continues as a storm fades, but dust also falls during it. "Most of it at the end" is **real value missing** |
| 5 | Rain in Phase 3 | **Fair as a game consequence of a wetter world**; no Mars analogue exists today. Real on Earth, but rain only removes coarse dust |
| 6 | Digging and intake dust the panels | **Plausible, unmeasured.** No source measures it. Intake is the weak part |
| 7 | Our invented numbers | **Direction right for all. Two are off by a lot: dust speed (about 15 times real) and dust devil chance (about 10 to 30 times real).** Output floor and storm cut are reasonable |

---

## 1. Real accumulation

| Mission | Figure | Tag | Source |
|---|---|---|---|
| Typical across landers and rovers | **0.2 % of output per sol**; observed range **0.05 to 2 % per sol** | searched, not opened | Lorenz et al. 2021 (abstract via search) |
| Mars Pathfinder | about 0.3 % per sol (a search summary says "per day") | searched, not opened | Pathfinder Materials Adherence Experiment, as cited in search results |
| Sojourner (1997) | about 0.25 % per sol | searched, not opened | NASA/JPL text, via search summary |
| Phoenix | about 0.2 % per sol (fits the Lorenz "typical" statement; I found no Phoenix-specific page) | searched, not opened | Lorenz et al. 2021 via search. **Phoenix-only figure: real value missing** |
| Spirit | Gradual decline, "interrupted by occasional sharp increases". Output reached about 60 % of clean around sol 420 before a cleaning event (see §2). A per-sol rate: **real value missing** | sourced, opened (shape); rate missing | S2 (NASA Photojournal, Spirit graph caption) |
| Opportunity | Per-sol rate: **real value missing**. Output rose from under 375 Wh/day (Jan 2014) to over 620 Wh/day (mid-April 2014) | sourced, opened | S1 (NASA Photojournal PIA18079 caption). Part of this rise is longer winter days, not only cleaning |
| InSight | About 5,000 Wh/sol after landing, about 500 Wh/sol by spring 2022 | **sourced, opened** | S3 (NASA Photojournal PIA25284) |
| InSight, as a rate | Landing 26 Nov 2018 to about May 2022 is about 3.5 years, about 1,230 sols. A 10-times drop in 1,230 sols is **0.19 % per sol, compounding** | **calculated** | From S3. Includes season and haze, not dust alone. It still lands next to the 0.2 % "typical" figure, which is reassuring |
| InSight, share blocked | About 80 % of the array obscured by mid-2022 | searched, not opened | Mission update via search |

**Steady, or season and storm?** Both. Between storms the decline is roughly steady (Lorenz: about 0.2 % per sol over InSight's first 800 sols, searched, not opened). Storms change it: after the 2007 global storm, mission scientists expected "enhanced dust accumulation on the arrays as the storm subsides" (Squyres, quoted in a news report, searched, not opened), and Opportunity's panels ended dustier than before the storm while Spirit's ended cleaner (Lemmon, same report). Whether deposition is higher in the dusty season: **unverified** (I recall that it is; I could not confirm it).

**Honest note for the journal.** "Real arrays lose about 0.2 % of their power each sol, so after 100 sols an uncleaned panel has lost roughly a fifth. This game runs about 15 times faster so you can see it." The 100-sol figure is **calculated** (0.2 % × 100 sols, simple sum) and rough.

## 2. Real wind cleaning events

| Event | What happened | Tag | Source |
|---|---|---|---|
| **Spirit, sol 420 (9 Mar 2005)** | Panel output jumped by about 50 % in one sol. The same graph shows gradual declines "interrupted by occasional sharp increases". Spirit photographed dust devils the next sol, about 1,100 m away; the caption asks "coincidence, or did a helpful dust devil pass over Spirit?" | **sourced, opened** | S2, S4 (NASA Photojournal captions) |
| Spirit, sol 420, size | "40 % reduced to only 7 % reduced" (a JPL scientist, Golombek). The same summary says output was about twice as high after | searched, not opened | News summaries via search |
| **Spirit, April 2009** | Cleaning events on 18 and 28 April; output rose from 223 Wh/day (31 Mar) to 372 Wh/day (29 Apr) = **+67 %**. Season may add to this | **sourced, opened** (secondary) | Wikipedia "Cleaning event", citing JPL (2009) |
| Spirit, count | Three dust-clearing events in about 2,000 sols (Lorenz and Reiss, from Vaughan et al. 2010). Sol 420 plus the two in April 2009 gives three, which agrees | searched, not opened | Lorenz via search; agrees with Wikipedia |
| **Opportunity, March 2014** | Two events in about a week; each added about **10 %** (about 574 Wh after the first). A mid-April total of over 620 Wh/day against under 375 Wh/day in January. The "70 %" figure in news is the whole early-2014 rise, which includes lengthening winter days | NASA caption **opened** (620 and 375); "10 % each" searched, not opened | S1; JPL release (27 Mar 2014) and Planetary Society via search |
| Opportunity, 2004 | Several overnight rises of about 5 % that surprised the team | sourced, opened (secondary) | Wikipedia, citing New Scientist 2004 |
| Opportunity, overall count | **Real value missing.** Many events over 14 years; I have no count | missing | |

**How often?** Spirit: about 3 events in 2,000 sols = about 0.15 % per sol [calculated]. Opportunity had more. Treat real frequency as roughly one event per hundred to several hundred sols at the two best-cleaned sites.

**Why InSight never saw them.** Dust devils were not detected in InSight's images at its landing site, and the authors say MER's cleaning history "may be contingent upon the local meteorological conditions and/or the specifics of the solar panel", so the rovers' long lives should not be assumed for every site (Lorenz et al. 2021, searched, not opened). The Wikipedia article adds that rover tilt and crater winds may help (speculation, opened). InSight's only noted bump was a dust devil on sol 65 that raised the two panels by about 0.7 % and 2.7 %, "not a significant difference" (search summary of a JPL page, not opened). A deliberate attempt to trickle sand across the panels gained only about 25 to 30 Wh/sol (JPL, via search, not opened).

**Teaching point (A-level science, "natural variation"):** Spirit and Opportunity's long lives were partly luck of place. That is a good B5 line: "cleaning events are not guaranteed".

## 3. How dust devils clean panels, and streaks

- **Mechanism.** A dust devil is a whirlwind that lifts a thin layer of bright dust and leaves darker ground behind (HiRISE and JPL image pages, searched, not opened). Spirit's team estimated the ones it saw moved at about 3 m/s (S4, opened). That the sol 420 jump was caused by one is **likely, not proven**: a team member suggested the clearing could have happened at night (search summary).
- **Fade.** Dust-devil tracks on the ground fade over about months as new dust covers them (search summary, not opened).
- **Streaks on arrays?** The ground tracks are streaks. For the arrays themselves, **I could not confirm streaks from any caption.** I viewed the Opportunity self-portraits (before: January 2014, PIA17759; after: March 2014, PIA18079; both from assets.science.nasa.gov). Before: a fairly even reddish veil. After: individual solar cells are clearly visible on most panels, but the cleaning is **uneven between panels and patchy within them**, and I see no obvious parallel stripes at this resolution. That is a human reading of a low-resolution image, not a research finding.
- **Why not fully clean.** Even after cleaning events, the Spirit arrays did not return to 100 % (sol 420: about 93 %, see §2). Much of the dust is held by static charge (a space-power review via search, not opened).

**Implication for the Lead Artist's option C.** An even veil for stage 4 matches the January 2014 image. For "partly cleaned" the honest look is **uneven patches that differ from panel to panel**, with streaks acceptable as a readable shorthand. See Handoffs.

## 4. Do storms deposit their dust as the air clears?

- **What is sourced.** Opportunity's output fell by as much as 80 % during the 2007 global storm (searched, not opened). During the peak, optical depth exceeded 5 (searched, not opened). Fallout kept building after the peak: a news report said panels were collecting a thin layer "comparable to the worst periods of the storm", and the team expected extra dust "as the storm subsides" (searched, not opened). Some wind cleaning removed part of the new dust "almost as soon as it accumulated" (a later report, searched, not opened).
- **What is missing.** How the total deposit divides between "during" and "after": **real value missing**. I found no measured split.
- **For InSight in Oct 2022**: a regional storm cut output from about 425 to 275 Wh/sol, an effect of haze, not deposited dust (searched, not opened).
- **Verdict.** "Storm dust added at the storm's end" is a **fair simplification** with a real basis (fallout continues as the storm fades, and the team named it), but it is **not** "most of the dust falls at the end". The F1 lock-point reason (nothing lowers output before onset) is a game-design reason; it should not be stated as science. Suggested journal wording in §8.

## 5. Rain washing panels in Phase 3

- **Real analogue on Mars.** None for liquid rain today. Mars panels have only wind, rare dust devils, and test methods such as sand trickling (§2) and electrostatic removal (concept studies, searched, not opened). Water-ice snow and frost on Mars exist (Phoenix saw snow falling; Viking saw frost; **unverified**, from memory). Whether they clean arrays: **real value missing**.
- **Earth (real).** Rain removes coarse particles but not fine ones, which tend to stick (NREL, searched, not opened). A desert test site measured about **0.49 % per day** soiling on fixed-tilt panels, about 13 % in a month without rain or cleaning (QEERI, searched, not opened). This is a good D2 link: Earth desert panels lose about 2.5 times more per day than the Mars typical 0.2 % per sol [calculated: 0.49 ÷ 0.2], and people still clean them with water, brushes, or robots.
- **Verdict.** Fair as a Phase 3 mechanic, **because rain is a consequence of the world the player has built**, not a claim about real Mars. Two honesty fixes: (a) "resets to stage 0" is too strong; real rain removes coarse dust, not all of it. "Back to stage 1" or "back to partly cleaned" is closer; (b) label it as "on Earth".

## 6. Do the player's own digging and a running Outgasser intake dust nearby panels?

| Evidence | What it shows | Tag |
|---|---|---|
| Rocket plumes on Mars | Thin air focuses landing plumes, causing heavy surface erosion; a Phoenix-era concern was that lifted dust would settle on the lander and could reduce power | searched, not opened (Planetary Society article; a 2021 plume white paper on arXiv) |
| Apollo descent videos | Plume-lofted dust sheets moved radially outward from the landing zone | searched, not opened |
| Rover wheels and digging | I found **no** report that driving, trenching, or sampling dusted a rover's or lander's solar array | real value missing |
| Intake | An intake draws air in, which does not blow dust outward. A vent, exhaust, or heavy machinery would be the physical cousin | calculated reasoning, no source |

**Verdict.** The idea (activity near arrays raises dust that settles on them) is **physically reasonable**, with real support for landing plumes only. No source supports a number, so **"×2 within 8 blocks" stays invented**, and the "Outgasser intake" wording should change. Mars's thin air does keep very fine dust suspended a long time (**unverified**, from memory), which also makes the idea reasonable. Suggested honest note: "We do not have measurements of this on Mars. It is based on how landing engines kick up dust."

## 7. Direction and size of each invented number (balance sheet §2a)

| Invented number | Real comparison | Direction | Size | Verdict |
|---|---|---|---|---|
| **3 sols per stage** (Phase 1). Stage 4 after 12 sols; loss 8 / 10 / 12 / 10 % per stage; 40 % in 12 sols = **3.3 % per sol** [calculated from the table] | Typical **0.2 % per sol**, range 0.05 to 2 % (searched) | Right (slow, steady) | **About 15 times real typical** [calculated: 3.3 ÷ 0.2 = 16; first stage 2.7 ÷ 0.2 = 13]; about 1.5 times the real worst case (2 % per sol). Phase 3 (6 sols per stage): about 1.7 % per sol; Equilibrium (12 sols per stage): about 0.8 % per sol. **Classroom ×4**: 13 % per sol, about 65 times real | **Off by a lot, deliberately.** A standard run has about 120 sols (3 sols/hour × 40 h, balance sheet §0 time unit); at the real rate an uncleaned panel would lose only about 24 % by the end of the game [calculated], so no one would notice. Compression is defensible but must be named in the journal. See Decision 1 |
| **Output 100 / 92 / 82 / 70 / 60 %** | Spirit stood at about 60 % just before the sol 420 event (opened/searched). InSight fell to about 10 % of landing output, and about 80 % obscuration (searched) | Right | Floor of 60 % is **reasonable for the rovers, optimistic for InSight**. Real arrays with no cleaning kept falling; no floor seen | **Acceptable.** Note in journal: "A real array with no cleaning can fall below this" |
| **85 % for partly cleaned** | Opportunity events: +5 to +10 % each (opened/searched). Spirit sol 420: about 60 % to about 93 %. Spirit 2009: +67 % output, season-mixed | Right (partial recovery, not a full reset) | From stage 4 (60 %) this is +25 points: inside the real range, between Opportunity-size and Spirit-size. From stage 2 (82 %) it is +3: smaller than any real event I found | **Reasonable.** Consider "recovers about half of what was lost" so it scales (60 → 80, 82 → 91). Designer's call |
| **Storm: factor × (1 − 0.6 × cap)**; worst case **24 %** | Opportunity −80 % in the 2007 global storm; InSight −35 % in a regional storm (425 → 275 Wh/sol); both searched, not opened | Right | A −60 % cut at cap 1.0 sits between the two; 24 % total is the same size as a real global storm alone | **Reasonable.** The "dust plus storm" combination is a game stacking of two real effects; fine |
| **Storm adds +1 stage (+2 if cap ≥ 0.8) at the end** | Fallout continues as storms fade; Opportunity ended 2007 dustier, Spirit cleaner (searched) | Right in direction | Size: +1 to +2 stages = 8 to 22 points, a plausible fraction of the real range. No measured number | **Acceptable, invented.** Real storms sometimes also clean (Spirit 2007) |
| **Local digging or intake ×2** | No measurement (§6) | Plausible | Unknown | **Invented, plausible**; change "intake" |
| **Dust devil: 5 % per sol per panel group** | Spirit about 0.15 % per sol [calculated: 3 events ÷ 2,000 sols]. Opportunity more, count missing. InSight 0 | Right (rare, lucky) | **About 10 to 30 times too frequent** against Spirit [calculated: 5 ÷ 0.15 = 33]; in a 120-sol game 5 % per sol gives about 6 events per panel group [calculated: 120 × 0.05], where real Spirit would have had about 0.2 [120 × 0.0015] | **Off by a lot.** At 5 % the "lucky surprise" becomes routine, and also hides the "InSight never got one" lesson. See Decision 2 |
| **Stage interval ÷ storm cap in later phases** (slower accumulation as the world wets) | Plausible: wetter, more stable ground binds dust. No source | Plausible | Unknown | **Invented; unverified** |
| **Phase 1 assumption: cleaning every 6 sols, about 90 % average** | 6 sols at the game rate: stages 0 to 2 are 100 to 82 %, average about 91 % [calculated]; at the real rate this would be about 99 % | n/a | Self-consistent with the game rate | Fine as a balance assumption |

## 8. Proposed journal text (for the Technical Lead / Lead Instructional Designer to place)

Middle school (about grade 6 to 7), tags in the notes:

> **Dust on the panels.** Fine Martian dust lands on solar panels. On the real Mars rovers and landers, power fell by about 0.2 percent each sol (a Mars day), but it was not the same every sol. Some days wind blew the dust off and the power jumped back. Spirit's panels once got about 50 percent more power in one sol. One lander, InSight, was never cleaned. Its power fell from about 5,000 to about 500 watt-hours a sol in under four years. **In the game, dust builds about 15 times faster so you can see it.**

High school adds: "Rates on real missions ranged from 0.05 to 2 percent per sol. Cleaning events are probably caused by dust devils, but this is not proven. Whether a site gets them depends on local weather."

Storm line: "A dust storm blocks light while it lasts, and the dust keeps settling on panels as the air clears. In the game, the dust is added when the storm ends."

Earth link (D2): "On Earth, desert solar farms lose about half a percent of their output each day to dust, and they are cleaned with water, brushes, or robots. Rain helps, but it washes off the coarse dust and leaves the fine dust."

## Decisions needed (ill-structured, for the Game Designer and Producer, with the user if scope)

**Decision 1: How honest should the dust speed be? (ill-structured)**
- **Framing.** The real rate (0.2 % per sol) would never show in a 120-sol game; the draft rate (about 3 % per sol) is about 15 times real. Disciplines: Game Design (visible chore, not too fast), Learning (does speed teach "dust kills panels in days"?), Art (the stage sequence needs to show within hours), Science (a stated scale factor).
- **Options.** (A) Keep the draft rate; add the journal note "about 15 times faster than real" (cheapest; honest if labelled). (B) Slow to about 6 sols per stage (about 1.5 % per sol, about 7 times real): fewer chores, slower Phase 1 impact. (C) Keep it fast in Phase 1 only and ease toward real by Equilibrium (the draft already does this partly: 0.8 % per sol, about 4 times real).
- **My recommendation.** A, with C's existing slowdown, because the learning goal is to plan power around the drift and a long, true-to-life timescale would remove it. Not a science problem if labelled.
- **Assumptions.** Standard run about 120 sols; one cleaning trip costs under a minute.
- **Better defined by.** The paper prototype / a playtest: do testers clean before the forecast storm (C1)?

**Decision 2: Dust devil chance (well-structured enough for the designer to settle).**
- Real: about one event per hundred to several hundred sols at the best sites; never at InSight's. Game: 5 % per sol (one per 20 sols).
- **Recommendation.** Lower to about 1 % per sol per group (about one per 100 sols; about once or twice a standard game), and tag it "real events are rarer still". Alternatively, tie it to season or to the site, and teach "some sites never get one" (InSight).

## Handoffs

> **To:** Lead Game Designer · **Problem type:** well-structured
> **Problem:** Two invented numbers are far from the real figures; a few labels need changing.
> **Context:** `docs/science/dust-review.md` §7. Dust speed is about 15 times typical real (3.3 % per sol against 0.2 %); dust devil chance is 10 to 30 times real (5 % per sol against about 0.15 % at Spirit).
> **Your view:** Keep the speed (labelled as compressed); lower the dust devil chance to about 1 % per sol. Change "Outgasser intake" to exhaust or heavy machinery. Make rain go to "stage 1 or partly cleaned", not stage 0. Optionally make "partly cleaned" recover about half the lost output.
> **Needed from them:** Decision on each; update `balance-sheet.md` §2a "Reasoning" cells to tags from this file.
> **Done when:** §2a cites `dust-review.md` and the changed numbers; DESIGN.md "Solar panel dust" matches.

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** "Partly cleaned" is drawn as streaks; the real before and after images look patchy.
> **Context:** `docs/science/dust-review.md` §3 (I viewed PIA17759 and PIA18079). No caption confirms streaks on arrays.
> **Your view:** Even veil for stage 4 is correct. For "partly cleaned" use uneven patches that differ between panels (streaks as readable shorthand are fine); show cell edges becoming visible.
> **Needed from them:** Check the art row against the two images in `docs/art/reference-audit.md` (add them as references: NASA/JPL-Caltech PIA17759, PIA18079).
> **Done when:** The reference audit lists the two images and the partly cleaned art row says "patchy".

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** Journal text and an evidence-event check.
> **Context:** §8 gives draft text at two depths; §5 gives a D2 link.
> **Needed from them:** Place the text; confirm "cleaned before a forecast storm" as a C1/B5 evidence event; note that the scale-factor line ("about 15 times faster") is a required honest note.
> **Done when:** Journal and CTA pages carry the tagged wording.

> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** If the dust devil chance and rain reset change, the config keys change.
> **Needed from them:** Keep dust devil chance, stage interval, and "rain resets to stage N" as data-driven values.
> **Done when:** Values are in datapack JSON, not code.

## Sources

Opened:
- **S1.** NASA Photojournal PIA18079, "Self-Portrait by Freshly Cleaned Opportunity Mars Rover in March 2014" (NASA/JPL-Caltech), https://science.nasa.gov/photojournal/self-portrait-by-freshly-cleaned-opportunity-mars-rover-in-march-2014. Image viewed from assets.science.nasa.gov. Companion PIA17759 (January 2014) viewed from the same host.
- **S2.** NASA Photojournal, "Solar-Panel Dust Accumulation and Cleanings" (Spirit graph, 2005), https://science.nasa.gov/photojournal/solar-panel-dust-accumulation-and-cleanings/.
- **S3.** NASA Photojournal PIA25284, "InSight's Power Generation: After Landing and Spring 2022", https://science.nasa.gov/photojournal/insights-power-generation-after-landing-and-spring-2022.
- **S4.** NASA Photojournal, "Dust Devils Seen by Spirit" (sol 421), https://science.nasa.gov/photojournal/dust-devils-seen-by-spirit/.
- **S5.** Wikipedia, "Cleaning event" (secondary; cites NASA/JPL and New Scientist), https://en.wikipedia.org/wiki/Cleaning_event.

Searched, not opened (do not quote in game text before opening):
- Lorenz, R. D., Reiss, D., et al. (2021), *Planetary and Space Science* 207, 105337, via search summaries (hal.sorbonne-universite.fr copy; NTRS 20230007029).
- JPL, "Cleaner NASA Rover Sees Its Shadow in Martian Spring" (27 Mar 2014) and "NASA Rover Opportunity's Selfie Shows Clean Machine" (17 Apr 2014).
- Planetary Society and Universe Today reports on the 2014 events; Space.com on Spirit sol 420 and on Opportunity.
- 2007 storm coverage (NBC News, Space.com, Spaceflight Now, Phys.org).
- JPL, "NASA's InSight Waits Out Dust Storm" and the InSight power-level blog (Oct 2022); JPL, "For InSight, Dust Cleanings Will Yield New Science".
- Planetary Society, "Will Phoenix' thrust raise too much dust?"; a 2021 plume white paper (arXiv 2102.12312).
- NREL (rain and fine particles); QEERI soiling test (0.49 % per day).
- HiRISE and JPL dust-devil-track pages.
