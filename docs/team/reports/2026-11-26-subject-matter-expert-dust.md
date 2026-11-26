# Report: Subject Matter Expert, solar panel dust review (BACKLOG #22 and #23)

- **Role:** Subject Matter Expert
- **Date:** 2026-11-26
- **Output:** `docs/science/dust-review.md` (276 lines; questions 1–7 in the main sections, 8–12 in "§9 Addendum"). Checked by the coordinator: intact.
- **Method:** opened NASA Photojournal image pages (science.nasa.gov, assets.science.nasa.gov) and Wikipedia; everything else is "searched, not opened". Blocked or unreachable: jpl.nasa.gov and nasa.gov news (403), ntrs.nasa.gov, hal.sorbonne-universite.fr, pmc.ncbi.nlm.nih.gov, arxiv.org, planetary.org, space.com, universetoday.com, dlr.de, researchportal.ip-paris.fr. **Never opened:** the Lorenz et al. 2021 paper, the JPL 2014 and 2018 releases, MOXIE pages, all 2018 storm pages, HiRISE dust-devil pages, NREL and QEERI soiling pages.

## Key findings (tagged in the file)
- **Accumulation:** about **0.2% of output per sol** is typical, range 0.05–2% (Lorenz et al. 2021; abstract seen four times, paper not opened). InSight figures **opened** (NASA PIA25284: about 5,000 to about 500 Wh/sol by spring 2022): calculated about 0.19% per sol, compounding, over about 1,230 sols. Phoenix-only and Spirit/Opportunity per-sol rates: **real value missing**. Roughly steady between storms, with extra fallout after storms.
- **Cleaning events:** Spirit sol 420 (about +50% in one sol; NASA captions opened); Spirit April 2009 (223 to 372 Wh/day, Wikipedia opened); Opportunity March 2014 (under 375 Wh/day in January to over 620 in mid-April, partly from longer winter days; opened). Spirit had about 3 events in about 2,000 sols (about 0.15% per sol, calculated). InSight never got a significant one (no dust devils detected at its site; the paper authors' view, searched).
- **Streaks:** viewing Opportunity's January and March 2014 self-portraits, the SME saw an even veil before, and after: individual cells visible but **patchy and uneven between panels**, with **no obvious parallel streaks** (the SME's own reading; no caption confirms streaks).
- **Storm dust at the end:** a fair simplification, not "most dust falls at the end"; fallout continues as storms fade. The F1 lock-point reason is a design reason and must not be presented as science.
- **Rain:** no Mars analogue; on Earth rain removes coarse dust but not fine dust. Desert soiling about 0.49% per day (searched): a good Earth link (D2), about 2.5 times the Mars typical rate.
- **Digging and intake dusting panels:** plausible; the only real support is landing plumes; the "intake" wording is physically weak.
- **Hardware:** NASA's electrodynamic dust shield flew on the **Moon** (Blue Ghost 1, March 2025), not on Mars. InSight's sand trickle gained only about 25–30 Wh/sol. **No Mars mission carried an array brush or wiper** that the SME could find; a player brush and wiper are plausible as game fiction, not flown hardware.
- **MOXIE:** about 122 g of O2 over 16 runs, best about 12 g/h, last run 9.8 g (searched). Power draw: **real value missing**. No statement found that dust or the filter limited output. Call the game unit "a scaled-up MOXIE".
- **Misconceptions** (risk and journal note for each, in §9): instant storm coating, "dust only hurts power", "wind always cleans", "Mars dust is like Earth sand", "rain washes it all", "the 60% floor means dust can't kill power".
- **2018 storm:** peak tau 10.8; last message 10 June 2018 with 21 Wh, down 97%; mission declared over 13 February 2019 (searched, not opened). The final cause of Opportunity's loss is **real value missing**; do not claim it was the dust coat. InSight's October 2022 regional storm cut output about 35% (425 to 275 Wh/sol).

## Invented numbers to change first (SME recommendation; none changed yet)
| Order | Number | Change to | Basis |
|---|---|---|---|
| 1 | Dust devil 5% per sol | About 1% per sol (or 0.5%) | Spirit about 0.15% per sol; 5% is about 10–30 times real, about 6 events in a 120-sol game |
| 2 | 3 sols per stage | Keep, but label it "about 15 times faster than real (0.2% per sol)" | Game rate 3.3% per sol; at the real rate an uncleaned panel would lose only about 24% over a whole game |
| 3 | Rain resets to stage 0 | Stage 1 or "partly cleaned" | Rain leaves fine dust |
| 4 | Outgasser intake | Exhaust or heavy machinery; keep ×2 as invented | No source |
| 5 | 85% partly cleaned | Optional: recover about half the loss | Real events range from +5–10% to about +33 points |

The storm cut (−60% at cap 1.0; worst case 24%) and the 60% floor are reasonable (a global storm took Opportunity down 80% in 2007 and 97% in 2018; InSight's regional storm cost about 35%; the floor is optimistic against InSight's fall to about 10%, season-mixed). Add a journal note.

## Decisions needed (classified by coordinator; owner approved the draft, so these are refinements)
| Decision | Recommendation | Type |
|---|---|---|
| Dust speed honesty | Keep the fast rate and label it "about 15 times faster than real"; use the slower rate in later phases | Ill-structured |
| Dust devil chance | About 1% per sol | Well-structured |

## Handoffs
- **Game Designer:** apply the change list above; tag balance §2a from `dust-review.md`; rename "intake". Done when §2a and DESIGN.md match.
- **Lead Artist:** "partly cleaned" should look patchy and differ between panels (streaks as shorthand are fine); add NASA/JPL-Caltech PIA17759 and PIA18079 to the reference audit. Done when the art row says patchy.
- **Instructional Designer:** place the journal text drafted in §8 and the §9 notes; confirm "cleaned before a forecast storm" as a C1/B5 evidence event.
- **Technical Lead:** keep dust devil chance, stage interval, and rain reset as datapack values.

## Risks
- Most key figures are search-summary only; the Lorenz 2021 paper must be opened by someone with access before any number goes into player-facing text.
- Speed compression (about 15 times) could teach "dust kills a panel in days"; the journal note is needed.
- Unverified precision to avoid: "about three weeks" for the 2018 storm, MOXIE power, dust grain size, the breathing rate.

## Shared model updates
- **Term:** "compressed": describe the game's dust rate by its factor (about 15 times real).
- **Fact:** the electrodynamic dust shield flew on the Moon in 2025; no Mars mission has flown an array brush or wiper.
- **Lesson:** NASA news pages and NTRS stay blocked; science.nasa.gov and assets.science.nasa.gov Photojournal image pages work and give opened captions and viewable images.
