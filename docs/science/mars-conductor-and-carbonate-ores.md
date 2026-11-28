# Conductor ores, carbonate and bone meal on Mars: what is real, what we invent

**Owner:** Subject Matter Expert · **Written:** 2026-11-28 · **For:** Game Designer (recipes, rarity), Technical Lead (worldgen), Artist (textures)
**Answers:** `docs/design/machine-priorities.md` conflicts R1, R3, R4 and decisions D1, D4. **Reads with:** `mars-surface-materials.md`, `mars-materials-and-recipes.md`. Rule used: `world-and-blocks.md` 0.2 (an invented block is fine if labelled; vanilla crafting stays).

**Marks.** **VERIFIED** = I opened the page on 2026-11-28 (curl, JSON or HTML stripped with `python3 -I`, never run) and the quoted line is on it. **NOT VERIFIED** = not on a page I opened. **REAL** / **SIMPLIFIED** / **INVENTED** as in the earlier science docs. All numbers marked *invented* are game balance.
**Source limits.** I only used NTRS (search API abstracts), science.nasa.gov and PubMed. No full papers were opened, only abstracts and NASA pages. Several searches (copper, zinc, nickel on Mars) returned no usable page.

## 1. Short answers

1. **Copper:** I found **no opened source** that Mars has minable copper. Trace copper in rocks is plausible but **NOT VERIFIED**. Honest ruling: **copper ore is INVENTED.**
2. **Redstone:** it is a Minecraft material with **no real analogue**. Closest real idea: Mars has a strongly magnetised crust (VERIFIED) and iron sulfides are a theorised magnetic mineral. Ruling: **INVENTED**, with a real hook.
3. **What is real and usable as conductors, structure and electronics:** iron (everywhere), iron-nickel meteorites (found by Opportunity), aluminium, silicon and titanium (in rock; need molten-rock electrolysis, a high-power process).
4. **Carbonate:** real and found in several places. Global dust holds a few percent (orbital estimate); the richest places are Nili Fossae and Jezero. A small mined vein is a fair game-scale placement.
5. **Bone meal from compost:** a fair simplification. Vanilla already makes bone meal from a composter.

## 2. Conductor and electronics materials: real evidence

| Material | What I opened | Source (quoted line) | Use in game |
|---|---|---|---|
| Iron | Mars Facts: crust "made of iron, magnesium, aluminum, calcium, and potassium"; red from rusting iron | https://science.nasa.gov/mars/facts/ **VERIFIED** | Base metal of every recipe (hematite, spherules) |
| Iron-nickel core | "It's made of iron, nickel, and sulfur." (the core) | same page **VERIFIED** | Nickel is real on Mars, but this line is about the core, not minable crust |
| Iron meteorites | "The Mars Exploration Rover Opportunity has encountered four iron meteorites at its landing site in Meridiani Planum." First one "Heat Shield Rock", "approx. 30 by 15 cm" | NTRS 20100003417 https://ntrs.nasa.gov/citations/20100003417 **VERIFIED** (abstract). That they are iron-nickel alloy: **NOT VERIFIED** on the abstract I opened | Rare surface find; REAL |
| Aluminium, silicon, titanium, iron from regolith | "MRE [molten regolith electrolysis] produces iron, silicon, aluminum, titanium, and glass" | NTRS 20170001458 https://ntrs.nasa.gov/citations/20170001458 **VERIFIED** | Real route, high power; keeps aluminium **cut** as in `world-and-blocks.md` |
| Ilmenite (FeTiO3) | Named as an oxygen source for the Moon, "lunar ilmenite" | same NTRS record **VERIFIED**. Ilmenite on Mars: **NOT VERIFIED** | Do not add ilmenite |
| Manganese oxides | "highly concentrated manganese oxides in Gale Crater and on the rim of Endeavour Crater" | NTRS 20150001945 https://ntrs.nasa.gov/citations/20150001945 **VERIFIED**. Also Mn in "fracture fills, coatings, and nodules" (NTRS 20250007199) **VERIFIED** | REAL material; not an electronics metal. Optional flavour only |
| Iron sulfides, magnetic | "incompletely weathered pyrrhotite may represent the magnetic material observed in Martian regolith" | NTRS 19890023539 https://ntrs.nasa.gov/citations/19890023539 **VERIFIED**. A 1989 hypothesis paper, **not settled science** | Hook for a magnetic ore |
| Magnetised crust | "areas of the Martian crust in the southern hemisphere are highly magnetized, indicating traces of a magnetic field from 4 billion years ago." | https://science.nasa.gov/mars/facts/ **VERIFIED** | Real magnetic hook; places it south, old terrain |
| Copper | Searched NTRS; only Earth engineering and one Mars experiment hit. No page saying Mars has copper | **NOT VERIFIED** | INVENTED |
| Zinc, nickel in rocks | Searched NTRS; no usable page | **NOT VERIFIED** | Do not claim |

## 3. Recommendation: two rare ore blocks (answers D1)

Keeps vanilla copper and redstone recipes unchanged. Alternative (swap to iron-only recipes) is in the report.

| Block (NEW) | Where | How rare (*invented*) | Drops | Status | Journal label (one line, grade 6 to 7) |
|---|---|---|---|---|---|
| `sulfide_vein` (grey-brown rock with rust and dull gold flecks; not green, no bright copper) | `highland_mesa` and crater-wall rock, in the walls of cuts and canyons, not on open plains | 1 to 2 clusters of 3 to 6 blocks per 256 x 256 area; none within 64 blocks of spawn | Raw copper (vanilla item; smelts to vanilla copper ingot) | **INVENTED** (real basis: Mars has iron sulfides; copper amount **NOT VERIFIED**) | "Game invention. Mars rocks may hold a little copper, but we do not know of copper veins. We add rare ones so you can build machines." |
| `magnetic_ore` (dark grey, metallic speckle) | Old high ground and crater rims in the southern half of the map; deeper than 8 blocks | 1 cluster of 3 to 5 blocks per 256 x 256 area; fewer than copper | Redstone dust (vanilla) | **INVENTED** (real hook: magnetised crust; pyrrhotite is a hypothesis) | "Game invention. Mars crust is strongly magnetic in places. No one has found ore to make electronics from. Redstone here stands for 'wiring and magnet material'." |
| `meteorite_iron` (optional, REAL) | Surface, loose on plains, single blocks | about 1 per 512 x 512 area | Iron ingots, tough tool bonus (design call) | **REAL**, SIMPLIFIED (amount) | "Real. Rover Opportunity found iron meteorites on Mars. We make them easier to find." |

Why two and not one: the machine recipes use copper (wiring) and redstone (control and storage) separately; one block dropping both would teach "one rock gives both", which is wrong.
Why rare and from walls: copper and redstone should cost exploration, not mining time (pillar "compact before wide"). Spawn gets a small starter stock so machines are not blocked (Game Designer: give a starter chest of 4 copper ingots and 8 redstone dust, labelled "from the lander", *invented*).
Do not show a green tint on copper (`mars-surface-materials.md` section 4). Do not tell players aluminium or silicon cells are cheap. Solar panels stay a vanilla-style craft; silicon is real in rock, the extraction process is the hard part.

## 4. Carbonate (answers R3, D4)

| Fact | Source (quoted) | Mark |
|---|---|---|
| Carbonate is a few percent of global dust | Mg carbonates "suggested to be a component (2-5 wt %) of the martian global dust based upon orbital thermal emission spectroscopy" | NTRS 20090011357 https://ntrs.nasa.gov/citations/20090011357 **VERIFIED** (abstract; orbital estimate) |
| Few lander detections | "there have been only a few indications of carbonates on the surface by a host of orbiting and landed missions" | same **VERIFIED** |
| Nili Fossae | "Mg-rich carbonates have been identified in the Nili Fossae region by the CRISM instrument" | same **VERIFIED** |
| Jezero margin unit | "identified from orbit as one of the most carbonate-rich regions of the planet" | NTRS 20240000510 https://ntrs.nasa.gov/citations/20240000510 **VERIFIED** |
| Jezero delta | "Fe/Mg-smectite and Mg-carbonate sediment ... (the only exposure of lacustrine shoreline carbonates seen so far on Mars)" (2017 statement) | NTRS 20170002464 https://ntrs.nasa.gov/citations/20170002464 **VERIFIED** |
| Gusev (Spirit) | Comanche carbonate "Mg-Fe-rich" and "present at high concentrations" in the outcrop | NTRS 20110003451 https://ntrs.nasa.gov/citations/20110003451 **VERIFIED** |
| Phoenix | "Evidence for Calcium Carbonate at the Phoenix Landing Site" | NTRS 20090011357 (title) **VERIFIED** |
| Percent of Jezero or Nili Fossae rock that is carbonate | not in any abstract I opened | **NOT VERIFIED** |

**Ruling:** REAL, SIMPLIFIED. Real carbonate is Mg- and Fe-rich, not calcium. Real Mars has little of it at the surface in most places; the richest spots are specific outcrops.
**Placement (answers D4):** `carbonate_rock` as a **small mined vein**, 4 to 8 block patches in `crater_floor` and crater-wall rock, about 3 patches per 256 x 256 area (*invented*). Enough for the first kits (one block per kit, Game Designer to confirm). Later carbonate comes from the scrubber, so mining stays small.
**Journal label:** "Real, but game-sized. Real Mars has carbonate rock in a few places, mostly Mg-rich, in craters like Jezero. We make small veins easy to find. Scrubbers making carbonate is a game idea." The **gypsum fallback** is also acceptable (VERIFIED gypsum on Mars) but carbonate teaches the CO2 link better.
Honesty note for the journal: scrubbing CO2 into carbonate at this scale is **invented**; real carbonate formed over long times with water.

## 5. Bone meal from compost (answers R4)

- Vanilla Minecraft already turns composted crop waste into bone meal (composter). So the game needs **no new rule**; the path is Phase 1 planter -> compost -> bone meal -> kits. Good link: Phase 1 farming feeds Phase 3.
- **Real idea:** closed-loop growing, where crop waste is returned to soil as organic matter. I did **not** open a source on Mars regolith nutrients or composting, so the claim "regolith needs organic matter and nutrients added" is **NOT VERIFIED** here (earlier docs also mark washing and fertility NOT VERIFIED).
- **Honest simplification:** real bone meal is ground animal bone (phosphorus and calcium). There are no animals. The game lets compost stand in. Real Mars crops would also need nitrogen and phosphorus brought or recycled; amounts **NOT VERIFIED**.
- **Journal label:** "Game-sized. Real growers on Mars would recycle plant waste to feed the soil. We use the Minecraft compost recipe. A real garden would need more than compost."
- Watch for a loop trap: the soil unit also needs bone meal (`world-and-blocks.md` 3.6). Make the first planter work with washed regolith and no bone meal.

## 6. Open items (NOT VERIFIED)

Copper, zinc and nickel in Mars rock; nickel content of the Meridiani iron meteorites; carbonate percent in Jezero and Nili Fossae rock; ilmenite on Mars; nutrient content of regolith and what compost would add. A Technical Lead or SME follow-up can open full papers (outside what this session could reach).
