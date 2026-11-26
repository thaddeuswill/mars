# Real dimensions for the Phase 1 machines (BACKLOG #25)

**Date:** 2026-11-26 · **Owner:** Subject Matter Expert · **For:** Lead Artist (models), Lead Game Designer (block footprints)
**Scale used:** 1 block = 1 m = 16 px, so **1 px = 6.25 cm**. Pixel value = metres x 16.

## Tags
| Tag | Meaning |
|---|---|
| **opened** | I opened the page and read the number myself |
| **opened-secondary** | Opened, but the page is Wikipedia (it quotes NASA, not NASA itself) |
| **searched** | Appeared in a web-search summary only; I could not open the page |
| **calculated** | My arithmetic from tagged inputs (shown) |
| **photo** | Read by eye from a NASA photo in `docs/art/references/` (approximate) |
| **invented** | Artist or designer choice, not a real fact |
| **missing** | No source found. Not guessed |

**What I could open:** science.nasa.gov (InSight spacecraft page, PIA22736, Perseverance instruments page), photojournal.jpl.nasa.gov (PIA19164, PIA24175, PIA24201, PIA24203, PIA17759), nssdc.gsfc.nasa.gov (InSight, Opportunity), en.wikipedia.org (InSight, Mars Exploration Rover, Spirit, MOXIE, REMS, MEDA, Curiosity, Perseverance, Tesla Powerwall).
**What I could not open:** www.jpl.nasa.gov (403), NTRS (`ntrs.nasa.gov`, blocked), spaceflight101.com, planetary.org, an.rsl.wustl.edu, satnews.com, solarquotes.com.au, seis-insight.eu. Anything that came only from those is tagged **searched**.
**Not found in anything reachable:** the Wikipedia article is titled "MOXIE", not "Mars Oxygen In-Situ Resource Utilization Experiment" (the long title is a 404).

---

## 1. InSight (landed Nov 2018, Elysium Planitia)

| Quantity | Metres | Pixels | Tag and source |
|---|---|---|---|
| Array diameter (each) | **2.2** | 35.2 | **opened**: NASA PIA22736 caption "7-foot (2.2 meter) wide solar panel"; science.nasa.gov InSight spacecraft page "about 7 feet (2.2 meters) each in diameter"; NSSDC "each 2.2 m in diameter" |
| Array diameter, other value | 2.15 | 34.4 | **opened-secondary**: Wikipedia "2.15 m (7.1 ft) in diameter when unfurled". Sources differ by 5 cm (under 1 px). Use 2.2 (NASA) |
| Lander deck diameter | **1.56** | 25.0 | **opened**: science.nasa.gov ("1.56 meters, lander deck diameter"); NSSDC "1.56 meter diameter platform" |
| Deck height above ground | **0.83 to 1.08** | 13.3 to 17.3 | **opened**: science.nasa.gov "33 to 43 inches (83 to 108 cm)"; NSSDC "approximately 108 cm above the ground by three legs"; Wikipedia says the range depends on how far the legs compress on landing |
| Span across both arrays ("wingspan") | **about 6.0** | 96 | **opened**: science.nasa.gov "19 feet 8 inches (6 meters) with solar panels deployed"; NSSDC "about 6 meters" |
| Check of the span | 5.96 | 95.4 | **calculated**: 2.2 + 1.56 + 2.2 = 5.96. Matches 6.0 (arrays touch the deck edge) |
| Robotic arm length | 1.8 | 28.8 | **opened**: science.nasa.gov |
| Mass | 360 kg | n/a | **opened**: science.nasa.gov |
| Lander **total height** (ground to top of mast or antenna) | | | **missing**. Only deck height was found. A search summary of an `an.rsl.wustl.edu` page said "about 0.5 m to the top of the deck", which contradicts the NASA 0.83 to 1.08 m. I could not open it. **Do not use.** |
| Array **thickness** | | | **missing** |
| **Height of the arrays above the deck** | | | **missing** |
| **Array tilt** | | | **missing**. Lander tilt after landing was about 4 degrees (Wikipedia, **opened-secondary**); that is the whole lander, not the arrays |
| **Number of segments** | **12** (each array) | n/a | **photo**: my look at PIA22876 agrees with the audit: each array is a round fan of about 12 wedge-shaped lobes separated by dark ribs (foreshortened, so I cannot count them exactly). No NASA text I opened gives a number (a search for "gores" found none) |
| **Cell size and arrangement** | | | **missing** (no size). **photo**: each wedge is covered in rows of small rectangular cells in a grid; a few dark rectangular cut-outs sit in the wedges; the ribs are dark with a thin pale edge |
| Cell type | | | **opened-secondary**: SolAero ZTJ triple-junction cells on Orbital ATK "UltraFlex" arrays (Wikipedia). Design descends from Phoenix (searched, ATK press) |

**Shape facts for the artist (all photo, PIA22876):**
- The array is a **flat round fan** (an umbrella-like disc with the hub at the deck end). It is not a rectangle. The outer rim is a ring of thin frame.
- Two arrays, one each side, mirror images, with the lander deck between. The deck has a dome-shaped (grey) cover, a brown hexagonal cover over the seismometer, and a tall dark pole. Those are not needed for the panel block.
- A round array drawn on a square grid: 2.2 m is **2.2 blocks wide**. A 2 x 2 block panel is 2.0 m (about 9 % smaller than real). 2.2 m would be a 35 px disc, so a round array is feasible only as a multi-block structure, not as one 16 px face (the audit already chose the square 2 x 2 form).

## 2. Opportunity and Spirit (twin rovers, 2004)

| Quantity | Metres | Pixels | Tag and source |
|---|---|---|---|
| Rover length, width, height | 1.6 / 2.3 / 1.5 | 25.6 / 36.8 / 24 | **opened-secondary**: Wikipedia, Spirit: "1.5 m high, 2.3 m wide and 1.6 m long" (the 2.3 m width is the stowed-wheel and panel width as the article gives it; I did not find whether it includes the panels) |
| Total panel area of cells | **1.3 m2** (area, not a length) | n/a | **searched**: Spectrolab article (compoundsemiconductor.net) in a search summary. Not opened |
| Power | 140 W in full sun | n/a | **opened**: NSSDC (Opportunity) |
| Panel mounting | "level with the deck", "a pair of swept-back wings" attached to the two forward sides of the triangular equipment deck | n/a | **opened**: NSSDC |
| Panel count and panel size | | | **missing**. **photo** (PIA18079 and PIA18080): about **five** flat rectangular panels (one large rear panel, two on each side that fold) plus small deck-top strips; the side panels are long rectangles, roughly twice as long as wide (my eye, approximate) |
| Cell size | | | **missing**. **photo**: cells are small near-rectangles in a tight grid with thin pale seams; each panel is about 8 to 10 cells across and 12 to 14 long (my rough count, approximate) |
| Cell type | | | **opened-secondary**: triple-junction (Wikipedia). **searched**: Spectrolab GaInP/GaAs/Ge |
| Pancam mast height | **1.4** (from the base of the wheels) | 22.4 | **opened**: NSSDC (Opportunity). Wikipedia says 1.5 m ("cameras mounted 1.5 meters high") |
| Rover wheel-base-to-deck, deck height | | | **missing** |

**Do not confuse:** Wikipedia's "five sections, 2.65 m diameter, 300 W at Mars" belongs to the **cruise stage** that carried the rover to Mars, not the rover panels.

## 3. MOXIE (Perseverance)

| Quantity | Metres | Pixels | Tag and source |
|---|---|---|---|
| Enclosure | **0.239 x 0.239 x 0.309** (9.4 x 9.4 x 12.2 in) | **3.8 x 3.8 x 4.9** | **opened**: science.nasa.gov Perseverance instruments page "23.9 x 23.9 x 30.9 centimeters". Wikipedia: 24 x 24 x 31 cm. Your "24 x 24 x 31 cm" is right |
| Mass | 17.1 kg on Earth | n/a | **opened**: NASA page. (Wikipedia infobox says 15 kg; the same article's body says 17.1 kg. Use 17.1) |
| Power, rate | 300 W; up to 10 g O2 per hour designed; best run 12 g/h | n/a | **opened**: NASA page; Wikipedia for 12 g/h |
| Material and finish | gold-plated aluminum box | n/a | **opened**: PIA24201 caption |
| **Intake** | The **largest white tube** on the top surface takes in filtered Mars air | n/a | **opened**: PIA24203 caption |
| **Outlet** | The **smallest tube** "snaking across the top of the unit" carries O2 through a purity sensor and vents it to the atmosphere | n/a | **opened**: PIA24203 caption |
| Also on top | A second tube vents the CO and leftover gas (the PIA24201 caption: "tubes to take in the Mars atmosphere and vent oxygen and carbon monoxide ... are connected at the top") | n/a | **opened**: PIA24201 |
| Electronics | In the **larger sidewall** (thicker face) on the right in PIA24201 | n/a | **opened**: PIA24201 caption |
| **Tube diameter** | | | **missing**. Any real tube under 6.25 cm is **under 1 px**; these tubes are far thinner than that. Must be exaggerated or drawn as a 1 px line |
| Inside | HEPA filter, scroll compressor, heaters, solid-oxide electrolysis (10 cells in two stacks of 5), about 800 C | n/a | **opened-secondary**: Wikipedia |

**Block fit:** the real unit is about a quarter of a block on each side. A one-block MOXIE-style machine is **about 4 times real size** (1 m vs 0.24 m). That is a deliberate scale-up (see section 5).

## 4. Weather mast and boom

| Quantity | Metres | Pixels | Tag and source |
|---|---|---|---|
| Perseverance mast: height of the camera head | **about 2** ("eye level of a 6 1/2-foot-tall person (2 meters tall)") | 32 | **opened**: science.nasa.gov Mastcam-Z entry |
| Curiosity overall size (whole rover) | 2.9 x 2.7 x 2.2 | 46 x 43 x 35 | **opened-secondary**: Wikipedia infobox (the same figures also appear in the Perseverance infobox) |
| MEDA wind sensor 1 | 0.05 x 0.17 | 0.8 x 2.7 | **opened**: NASA instruments page ("5 by 17 cm") |
| MEDA wind sensor 2 | 0.05 x 0.40 | 0.8 x 6.4 | **opened**: same ("5 by 40 cm") |
| MEDA air-temperature sensor (5 of them) | 0.0575 x 0.0275 x 0.0675 | 0.9 x 0.4 x 1.1 | **opened**: same |
| MEDA pressure and control unit | 0.14 x 0.14 x 0.13 | 2.2 x 2.2 x 2.1 | **opened**: same |
| MEDA sensor placement | on the mast "neck", and on the deck, front and interior | n/a | **opened**: same |
| REMS boom (Curiosity), each | 0.151 x 0.056 x 0.092 (boom 1) | 2.4 x 0.9 x 1.5 | **searched**: spaceflight101.com via search summary (I could not open it). Also said: booms are "roughly the diameter of the mast" in length, 120 degrees apart, about 1.6 m above ground. **Treat as approximate** |
| REMS boom shape | A short horizontal boom projecting from the mast, carrying a cylindrical head; cream-white painted | n/a | **opened**: PIA19164 caption; **photo** for colour (audit) |
| **Mast diameter** | | | **missing** |
| **Mast height above the deck** | | | **missing** (only height of the camera head above ground, ~2 m for Perseverance; Spirit's Pancam mast 1.4 m above the wheel base) |
| **Boom length and diameter** | | | **missing** from anything opened; the ~15 cm figure above is **searched** |

**Reading for the artist:** the real sensors are tiny. Even the longest (40 cm) is 6.4 px; the others are 1 to 3 px, and the cross-section of the wind sensors (5 cm) is **under 1 px**. A readable weather mast at block scale is a **2 m pole (32 px)** with a boom a few pixels long. The pole itself is thin: a real mast is likely about 5 to 10 cm (**invented**, no source), which is 1 to 1.6 px.

## 5. Human-scale equivalents

### 5.1 Crew-scale oxygen unit (calculated)
**Inputs**
- Crew use: "a mission of four astronauts on the Martian surface for a year, only about 1 metric ton of oxygen would be used for life support" (**opened-secondary**: Wikipedia MOXIE, citing NASA). Per person per day = 1000 kg / (4 x 365) = **0.68 kg** [calculated]. This agrees with the 0.7 kg I got from the same figure in a search and is close to my earlier "about 0.8 kg" (from memory; this replaces it).
- MOXIE output: 10 g/h designed (**opened**, NASA), 12 g/h best (**opened-secondary**). Per day: 0.24 to 0.29 kg [calculated: x 24].
- MOXIE box volume: 0.239 x 0.239 x 0.309 = **0.0177 m3** [calculated].

**Scale factor k = crew need / MOXIE output**
| Crew | Need (kg/day) | k (vs 12 g/h) | k (vs 10 g/h) |
|---|---|---|---|
| 1 | 0.68 | 2.4 | 2.8 |
| 4 | 2.74 | 9.5 | 11.4 |

So "3 to 10 times MOXIE" was about right: **2.4 to 11.4 times**.

**Size, if the whole box scales with output** (volume x k, each side x k^(1/3)) [calculated; assumption, not a design fact]
| Case | k | Side factor | Box (m) | Box (px) |
|---|---|---|---|---|
| 1 crew | 2.4 to 2.8 | 1.34 to 1.41 | about 0.32 to 0.34 x 0.32 to 0.34 x 0.41 to 0.44 | about 5 to 5.5 x 5 to 5.5 x 6.6 to 7 |
| 4 crew | 9.5 to 11.4 | 2.1 to 2.25 | about 0.50 to 0.54 x 0.50 to 0.54 x 0.65 to 0.70 | about 8 to 8.6 x 8 to 8.6 x 10.4 to 11.1 |

**So a 1-to-4-crew MOXIE-style unit is about 0.3 to 0.55 m on a side, 0.4 to 0.7 m tall: it fits inside one block (16 px) with room to spare.** A 1 m, 16 px cube is about 2 to 4 times too large for a real crew unit.
**Honesty note:** straight volume scaling is a crude guess. Compressors, filters and electronics do not scale evenly; the oxide stack is only part of the box. NASA's own full-scale concept (2 kg/h, about 200 times MOXIE, with a 25 to 30 kW power plant, **opened-secondary**, Wikipedia) was described in a search summary as "a bit larger than a household stove, about 1,000 kg" (**searched**, not opened; size not confirmed). If that is right, 200x is roughly stove-sized (about 0.6 x 0.6 x 0.9 m [**invented** reading of "stove"]). That is **smaller** than the linear-scaling rule would give (200 x 0.0177 = 3.5 m3, a 1.5 m cube), so real scaling is much gentler than volume x k. Treat the table above as an **upper bound**, and 0.3 to 0.7 m as a safe range.

### 5.2 Regolith heater or reactor
**Real value missing** from anything I could open. NTRS (where ROxygen, RESOLVE and carbothermal reactor papers live) is blocked.
- **searched** (not opened): a carbothermal plant design processes **9.3 kg of regolith every 100 minutes** and makes about **0.65 kg O2 per hour**; a flight-like carbothermal reactor (Sierra Space COPR) reached TRL 6. Reactor dimensions were not in the summaries.
- **calculated**, assumption flagged: if loose regolith packs at about 1.5 kg/L (**assumed**, not sourced), 9.3 kg is about 6 L, a chamber of a few litres (about a 15 to 20 cm cube) [**invented**-grade guess]. That is 2.4 to 3.2 px. The real reactor body would be larger than the chamber (insulation, heaters, auger, hopper), perhaps 0.3 to 1 m [**invented**, no source].
- **For the artist:** no real photo or size exists to match. Anything from 0.5 to 1 m (8 to 16 px) is plausible and honest; call it "invented, from the text description of a heated cylinder with a feed hopper" (reference-audit finding 6).

### 5.3 Battery pack for a small habitat
**Real value missing** for a space habitat pack. The only sized stand-in I could open:

| Item | Size (H x W x D) | Pixels (H x W x D) | Tag and source |
|---|---|---|---|
| Home wall battery, Tesla Powerwall 2 (13.5 kWh) | 1.15 x 0.75 x 0.14 m | 18.4 x 12 x 2.2 | **opened-secondary**: Wikipedia "Tesla Powerwall" |
| Powerwall 3 (13.5 kWh) | 1.10 x 0.61 x 0.19 m | 17.6 x 9.8 x 3.1 | **opened-secondary**: same |
| Opportunity rover battery (two, 7.15 kg each) | | | **opened-secondary**: mass only; size **missing** |

A home wall pack is the right order of magnitude for "a small habitat's battery": about **1 m tall, 0.6 to 0.75 m wide, 0.14 to 0.19 m deep** (16 x 10 to 12 x 2 to 3 px) [Earth product, not space hardware; say so]. A 16 px cube battery is about 1.5 to 2 times too deep for that.

### 5.4 Habitat control console or weather display
**Real value missing.** No source with a size was opened. Any size is **invented**. For reference only: ISS and spacecraft displays are tablet to monitor sized, which I did not source.

## 6. Pixel table and what is feasible (1 px = 6.25 cm)

| Feature | Real size (m) | Pixels | Flag |
|---|---|---|---|
| Array diameter (InSight) | 2.2 | 35.2 | Over 2 blocks. A 2 x 2 block panel = 32 px = 2.0 m |
| InSight span | 6.0 | 96 | 6 blocks |
| InSight deck | 1.56 | 25 | 1.5 blocks |
| InSight deck height | 0.83 to 1.08 | 13 to 17 | About 1 block |
| MOXIE box | 0.24 x 0.24 x 0.31 | 3.8 x 3.8 x 5 | About a quarter-block cube |
| MOXIE tubes (diameter) | missing | **under 1 px** | Exaggerate or draw as 1 px lines |
| MOXIE surface detail (pocket panels, X ribs) | few cm | **under 1 px** to 1 px | Cannot be drawn at real scale |
| Crew-scale (1 to 4) O2 unit | 0.3 to 0.55 x 0.3 to 0.55 x 0.4 to 0.7 | 5 to 9 x 5 to 9 x 7 to 11 | Fits in one block |
| Mast (Perseverance camera height) | about 2 | 32 | 2 blocks |
| MEDA wind sensor, long | 0.05 x 0.40 | 0.8 x 6.4 | Width **under 1 px** |
| MEDA wind sensor, short | 0.05 x 0.17 | 0.8 x 2.7 | Width **under 1 px** |
| REMS boom | 0.15 x 0.06 x 0.09 | 2.4 x 0.9 x 1.5 | Approximate (searched) |
| Solar cell (a few cm) | missing | **under 1 px** | A cell is smaller than a texel; the seams and cell rows must be drawn larger than real |
| Array thickness | missing | likely **under 1 px** | |
| Battery pack | 1.1 x 0.7 x 0.15 | 18 x 11 x 2.4 | Earth product as stand-in |

## 7. How good are these numbers?
| Rating | Items |
|---|---|
| **Well sourced (NASA pages opened, agree across 2+ pages)** | InSight array diameter 2.2 m, deck diameter 1.56 m, deck height 0.83 to 1.08 m, span 6 m, arm 1.8 m. MOXIE 23.9 x 23.9 x 30.9 cm, 17.1 kg, 300 W, 10 g/h. MOXIE intake and outlet roles (PIA24203). MEDA sensor sizes. Perseverance camera height 2 m |
| **Reasonably sourced (one NASA page, or Wikipedia only)** | Spirit and Opportunity body size; Pancam mast 1.4 m (NSSDC) vs 1.5 m (Wikipedia); MER panels "level with the deck"; 1.0 t of O2 per 4 crew per year; Powerwall sizes; Curiosity 2.9 x 2.7 x 2.2 m |
| **Approximate** | InSight segment count (12, by eye); MER panel count and cell grid (by eye); REMS boom size (search summary only); crew-scale unit size (calculated, upper-bound); carbothermal figures (search only) |
| **Real value missing** | InSight total height; array thickness; array height above deck; array tilt; cell size (InSight and MER); MOXIE tube diameter; mast diameter; mast height above deck; boom length for Perseverance; regolith reactor size; space-habitat battery size; console size |

## 8. Recommendations (from the science side, for the Artist and Game Designer to weigh)
1. **Two things are firmly true and are worth showing:** the InSight array is a **round 12-lobe fan about 2.2 m wide** (so 2 blocks is honest within about 10 %), and the **MOXIE box is a quarter-block**, not a block.
2. **The Oxygen maker block (1 m) is about 4 times real MOXIE size.** For a crew-scale machine that is about 2 to 4 times too large, which is a small, defensible stretch. Say it in the journal: "MOXIE was the size of a toaster; a unit for a crew would be about the size of a small oven or smaller."  (**invented comparison; check before use**).
3. **Pixel realities:** tubes, cells, seams, array thickness and sensor widths are under 1 px; exaggerate them on purpose and note that in the style guide as a deliberate simplification.
4. **Closing the "missing" items:** the owner or Artist could provide InSight deployed-height and cell data from the NASA press kit (jpl.nasa.gov, blocked for me) or the UltraFlex datasheet; MER solar array paper (Stella and Herman, IEEE 2005, not opened; **unverified reference**); a Perseverance MEDA instrument paper (Rodriguez-Manfredi et al. 2021, **unverified**).

---

# Report

**0. Handoffs received:** Coordinator message to continue after the usage-limit reset: accepted; all work was redone from the saved scratchpad and fresh fetches.

**1. Done:** `/home/user/mars/docs/science/dimensions.md` (this file). No other files were changed.

**2. Decisions needed:** None for the user. One well-structured gap: if exact InSight and MER cell sizes or array height matter to the owner, someone with access to the blocked JPL/NTRS pages should read them (well-structured).

**3. Handoffs:**
> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** Several real sizes are under 1 px and the model sizes differ from the real ones.
> **Context:** Section 6 of `docs/science/dimensions.md`.
> **Needed from them:** Decide which features to exaggerate (tubes, cells, seams) and note them in the style guide as deliberate simplifications; keep the InSight array at about 2 blocks wide and the MOXIE-style box near the 0.3 to 0.55 m crew-scale range if the machine is to be size-honest.
> **Done when:** the style guide lists each exaggerated feature.

> **To:** Lead Game Designer · **Problem type:** ill-structured (fun and legibility vs. size accuracy)
> **Problem:** A one-block (1 m) oxygen maker is 4 times real MOXIE and 2 to 4 times a crew-scale unit; a block-sized machine reads well but is not size-accurate.
> **Context:** Sections 3 and 5.1.
> **Your view (SME):** Honest enough if the journal says so; a smaller footprint (a half-block model inside a 1 m cell) is more accurate; the Artist and Designer should decide with readability and appeal in mind. Options: (a) 1 block, note the scale-up; (b) half-height machine inside the block; (c) keep full size and show the real MOXIE as a small prop beside it.
> **Needed from them:** pick one.
> **Done when:** the choice is in `DESIGN.md` and the asset list.

**4. Risks:** Missing values (array thickness, tilt, cell size, tube diameter) will tempt invention; keep them tagged **invented** in any asset notes. The crew-scale size is an upper bound from a crude scaling rule. The "stove" statement is from a search summary only. Wikipedia figures are secondary; NASA's JPL pages were blocked.

**5. Shared model updates:** New tag set for dimension claims (opened / opened-secondary / searched / calculated / photo / invented / missing) is used in `docs/science/dimensions.md` section "Tags". Pixel scale to quote: 1 px = 6.25 cm. Correction: crew O2 is about 0.68 kg per person per day (1 t per 4 crew per year, Wikipedia citing NASA), and a crew-scale unit is 2.4 to 11.4 times MOXIE, replacing the earlier "3 to 10, from memory".
