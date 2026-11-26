# Report: Subject Matter Expert, real dimensions of the Phase 1 reference hardware (BACKLOG #25)

- **Role:** Subject Matter Expert
- **Date:** 2026-11-26
- **Output:** `docs/science/dimensions.md` (tag legend; one table per reference; pixel conversions at 1 px = 6.25 cm; a feasibility table; a rating of how good each number is). No other file changed.
- **Tags:** opened, opened-secondary (Wikipedia), searched (search summary only), calculated, photo, invented, missing.

## Well sourced (NASA pages opened)
- **InSight:** each solar array is **2.2 m across (35 px)**; the deck is 1.56 m (25 px); deck height above ground 0.83–1.08 m (13–17 px); span across both arrays about 6 m (96 px; check: 2.2 + 1.56 + 2.2 = 5.96 m); the arm is 1.8 m. Wikipedia gives 2.15 m for the array diameter (5 cm difference).
- **MOXIE:** the box is **23.9 × 23.9 × 30.9 cm** (3.8 × 3.8 × 4.9 px), so the 24 × 24 × 31 cm figure is right. NASA says 17.1 kg, 300 W, up to 10 g/h (Wikipedia's infobox says 15 kg, which its own body text contradicts). Per photo PIA24203: the largest white tube on top is the CO2 intake; the smallest tube snaking across the top vents the oxygen after a purity sensor; another top tube vents CO. Per PIA24201 the electronics sit in the thicker sidewall.
- **Weather mast:** the Perseverance mast camera head is at about 2 m (32 px); MEDA wind sensors are 5 × 17 cm and 5 × 40 cm; air-temperature sensors 5.75 × 2.75 × 6.75 cm; the pressure and control unit 14 × 14 × 13 cm.
- **Opportunity and Spirit:** the rover body is 1.6 × 2.3 × 1.5 m (Wikipedia); the panels are level with the deck and look like swept-back wings (NSSDC); the Pancam mast is about 1.4 m above the wheel base.

## Calculated (a crude upper bound)
- Crew oxygen use about **0.68 kg per person per day** (1 t per 4 crew per year; Wikipedia citing NASA). A crew-scale unit is **2.4 to 11.4 times MOXIE's** 10–12 g/h, which **replaces the earlier "3 to 10 times, from memory"** in `dust-review.md`.
- Scaling MOXIE's box volume gives about 0.32–0.34 m per side for 1 crew and about 0.5–0.54 m per side (0.65–0.70 m tall) for 4 crew: 5–9 px wide and 7–11 px tall, inside one block. A 1 m block-sized machine is about 4 times real MOXIE and 2–4 times a crew-scale unit.
- It is an upper bound: a search summary (not opened) said NASA's 200x full-scale concept is "a bit larger than a household stove", far smaller than volume scaling predicts.

## Approximate or by eye
InSight has about 12 lobes per array (photo PIA22876, foreshortened; no opened text gives a count); Opportunity about five flat rectangular panels with a rough cell grid read from a photo; the REMS boom about 15 × 5.6 × 9 cm (search summary only); carbothermal figures (9.3 kg of regolith per 100 minutes, 0.65 kg O2/h) from search summaries only.

## Real value missing
- **InSight:** total height, array thickness, array height above the deck, array tilt, cell size.
- **Opportunity:** panel count, panel size, cell size from text.
- **MOXIE:** tube diameter.
- **Mast and boom:** mast diameter, mast height above the deck, boom length and diameter (Perseverance).
- **Human-scale:** regolith reactor size, space-habitat battery size, console size. The only sized battery stand-in found is a home battery (Tesla Powerwall 2, 1.15 × 0.75 × 0.14 m, 18 × 12 × 2 px; an Earth product, not space hardware).

## Under 1 px at 6.25 cm per px
MOXIE tubes and surface detail, solar cell size, array thickness, and the width of the MEDA wind sensors (5 cm, 0.8 px). Wherever these appear they must be exaggerated on purpose.

## Could not open
www.jpl.nasa.gov (403), ntrs.nasa.gov, spaceflight101.com, planetary.org, an.rsl.wustl.edu, satnews.com, solarquotes.com.au, seis-insight.eu. Wikipedia's long MOXIE title is a 404; the article is at "MOXIE".

## Decisions needed (classified by coordinator)
| Decision | Recommendation | Type |
|---|---|---|
| Oxygen maker size: a 1 m block-sized machine is about 4 times real MOXIE and 2–4 times a crew-scale unit. Options: (a) keep 1 block and note the scale-up in the journal; (b) a half-size model inside the block; (c) keep full size and add a small real-size MOXIE prop beside it | (a) or (c); the SME's assumption is that block legibility matters more than exact size | Ill-structured (legibility and appeal against size accuracy) |

## Handoffs
- **Lead Artist:** list each exaggerated feature (tubes, cells, seams, array thickness, sensor widths) in the style guide as a deliberate simplification; keep the InSight-style array at about 2 blocks wide.
- **Game Designer:** pick the oxygen-maker size option; record it in DESIGN.md and the asset list.

## Risks
Missing values will tempt invention (keep them tagged "invented" in asset notes); the crew-scale size is a rough upper bound; the "stove" comparison and the REMS boom size are search-summary only; Wikipedia is secondary to NASA for several rover figures; two of the SME's own wordings are marked invented (the journal comparison "small oven" and the stove-size reading). If exact InSight and MER array thickness, tilt and cell sizes are wanted, someone with access to the blocked JPL and NTRS pages would have to read them.

## Shared model updates
Dimension tags (opened / opened-secondary / searched / calculated / photo / invented / missing); pixel scale 1 px = 6.25 cm; crew oxygen about 0.68 kg per person per day and a crew-scale unit 2.4–11.4 times MOXIE; quote MOXIE mass as 17.1 kg (NASA), not 15 kg.
