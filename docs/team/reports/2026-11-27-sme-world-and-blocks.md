# SME report: world and blocks science check (2026-11-27)

## Handoffs received
From the Game Designer (`docs/design/world-and-blocks.md`, report `2026-11-27-game-designer-world-and-blocks.md`): check every "SME check" claim. Accepted. Method: documents-only review; sources fetched with curl (NASA Science, NTRS, PubMed via eutils), HTML stripped with `python3 -I`, treated as data. Several guessed NASA/Planetary Society URLs returned "page not found" and PubMed article pages were cookie-blocked; abstracts came through eutils instead.

## Done
Replaced each "SME check" mark in `docs/design/world-and-blocks.md` with a verdict and source number. Verdicts (16 marks):

| # | Claim | Verdict | Source (quoted line) |
|---|---|---|---|
| 1 | Perchlorate in Mars soil | Accurate | R1 |
| 2 | Perchlorate level | Accurate (0.4-0.6% by mass of leachate) | R1 |
| 3 | Perchlorate hurts plants / dead-tomato lesson | Accurate (analog soil, 1%) | R10 |
| 4 | Perchlorate dissolves in meltwater | Accurate (soluble salt) | R1 |
| 5 | Washing perchlorate out of regolith works | NOT VERIFIED (no source opened); mark as game design | none |
| 6 | Perchlorate "seams" a scan can find | Wrong as geology (it is spread through soil); keep as invented | R1 |
| 7 | Iron-oxide dust, basaltic crust | Accurate | R3, R8 |
| 8 | Craters | Accurate ("everywhere" simplified) | R3 |
| 9 | Dunes | Accurate; sand is dark basaltic | R8 |
| 10 | Valles Marineris / canyon | Accurate; stand-in must say "stylised" | R3 |
| 11 | Hematite, sulfate, meteorite iron | Accurate | R5, R6, R7 |
| 12 | Silica sand ore for glass | Acceptable simplification (Mars sand is basaltic; silica source NOT VERIFIED) | R8 |
| 13 | Ground ice at 3 to 12 blocks | Acceptable simplification | R1, R2 |
| 14 | Polar cap "layered ice and frozen CO2" | Acceptable simplification (north water, south CO2, seasonal frost both) | R4 |
| 15 | Carbonate rock | Real but abundance NOT VERIFIED | R9 |
| 16 | Lava-tube caves | NOT VERIFIED (no Mars source opened) | none |

Counts: accurate 8 (1,2,3,4,7,8,9,10, plus 11 = 9 if counted as one: 9), acceptable simplification 3 (12,13,14), wrong 1 (6, as geology), not verified 3 (5,15 abundance,16). Wrecked probe: fiction by design (real landers and the Opportunity heat shield exist, but no source needed); fine if labelled.

**Sources actually opened (all fetched 2026-11-27):**
- R1 Hecht et al. 2009, PubMed 19574385: "The solutions contained approximately 10 mM of dissolved salts with 0.4 to 0.6% perchlorate (ClO4) by mass"; "the approximately 5-centimeter-deep ice table". Also NASA Phoenix page (science.nasa.gov/mission/phoenix/): "Phoenix discovered traces of the chemical perchlorate, a possible energy source for microbes and a potentially valuable future resource".
- R2 Feldman et al. 2002, PubMed 12040090: ice "constitutes 35 +/- 15% of the layer by weight"; hydrogen-poor upper layer thins toward the pole. Plus PubMed 17476262: "water ice is stable in the shallow subsurface (depths of <1-2 m) of Mars at high latitudes ... a water-ice table that steadily increases in depth with decreasing latitude".
- R3 NASA Mars Facts (science.nasa.gov/mars/facts/): "iron minerals in the Martian dirt oxidize, or rust, causing the surface to look red"; "Its volcanoes, impact craters, crustal movement, and atmospheric conditions such as dust storms have altered the landscape"; Valles Marineris "about 2,400 miles (3,870 kilometers) in length ... about 370 miles (600 kilometers) across ... 5.8 miles (9.3 kilometers) at its deepest"; "water-ice just under the surface in the polar regions as well as in briny (salty) water".
- R4 NTRS 19940023386: "the residual polar cap was composed of water ice in the northern hemisphere but was primarily carbon dioxide ice in the southern hemisphere"; "the seasonal polar cap of CO2 recedes".
- R5 NTRS 20030066630: gray crystalline hematite "in a 350 km by 750 km region near Meridiani Planum".
- R6 NTRS 20170001689: "many Ca-sulfate veins that cut all three stratigraphic units" at Gale.
- R7 NTRS 20100003417: "Opportunity has encountered four iron meteorites at its landing site".
- R8 NTRS 20180007625 and 20180004270: active "Bagnold Dune Field"; "varying abundances of olivine and pyroxene across the dune field".
- R9 NTRS 20220012767: "olivine and carbonate-bearing rocks of the Seitah formation in the floor of Jezero".
- R10 PubMed 41597718: "tomato plants grown over perchlorate-polluted analog (1% m/m) died in less than 2 weeks" (a lab analog, not Mars soil).

**Label as invented (game-only):** hematite/ore amounts and distances; the 64-block and 150-400-block rule; perchlorate seams and the survey scanner; ground collapse; ice extractor on ice only; frozen CO2 block; dwarf-tier crops; dome washing station; "heater tip" tools; biocrust speed.

## Decisions needed
1. Perchlorate washing (well-structured): is the washing step game design or sourced? Recommend: label it game design until a source (for example NASA or a bioremediation paper) is opened; SME to find one.
2. Ground-ice depth near the dome (ill-structured: realism vs. Phase 1 pacing): near-dome ice at 3-12 blocks is not equatorial truth. Recommend: place the dome at a stated mid/high "latitude" (north plains, Phoenix-like) so shallow ice is honest, and say so in the journal.

## Handoffs
> **To:** Game Designer · **Problem type:** well-structured
> **Problem:** four edits to the terrain plan.
> **Needed:** (a) call it "sand", dark red-brown, not "silica/quartz"; (b) perchlorate is soil-wide, not veins: make the scanner read a soil percentage, or label seams invented; (c) polar cap: choose water ice (north) or CO2 (south) as the main block; (d) canyon and caves carry a "stylised" journal note.
> **Done when:** labels appear in `world-and-blocks.md` section 3.1.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Needed:** journal accuracy notes for items 12-14, 16 (see `LEARNING.md` "Science accuracy notes").

## Risks
- Players may learn "Mars has white quartz sand and perchlorate veins" (misconception risk, items 6, 12).
- Shallow ice near the dome could teach that ice is everywhere under the surface.
- Items 5, 15, 16 remain unsourced; do not put them on a sign or in the journal as fact.
- Source limits: abstracts only for PubMed items, no full papers read.

## Shared model updates
None. (Suggest a lesson: NASA page URLs guessed from memory fail; search or use the NTRS API.)
