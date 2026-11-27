# Mars materials and recipes: what is real, what we invent

**Owner:** Subject Matter Expert · **Date:** 2026-11-27 · **Answers:** owner request "get the blocks and items that can be used on Mars; if we have to make up blocks or chemistry, we can" · **Related:** `docs/design/world-and-blocks.md` section 3 (block list), `docs/design/balance-sheet.md`, `docs/science/` reviews

**Rule kept (owner decision 2026-11-27):** vanilla Minecraft crafting, mining, building and gardening stay. Mars adds blocks and machines on top. Recipes below use vanilla items first (iron ingot, glass, brick, furnace, water bucket, farmland, bone meal) so the hands know what to do. **Recipes are ideas for the Game Designer and Technical Lead, not settled.** Recipe shapes and amounts are always *invented* (game balance).

**Status key:** **REAL** = the process or material is real and I opened a source (see section 3) · **SIMPLIFIED** = real idea, shrunk to game size · **INVENTED** = made-up block or chemistry the game needs; the row says what real thing it stands in for and what it must NOT claim. Where I did not open a source, the row says **NOT VERIFIED** (general knowledge, check before shipping).

## 1. Materials and processes mapped to blocks and items

### 1.1 Ground materials

| Real material or process | In-game block / item | Crafting or machine idea (vanilla first) | Serves (Index / build task) | Status and notes |
|---|---|---|---|---|
| Regolith: loose, rusty-red soil. Red colour is iron minerals that "oxidize, or rust" [N-Facts] | Regolith (`red_sand` or new) | Shovel it. Shielding stack, planter base, Oven input | Shielding, Oven bake, soil | **REAL** [N-Facts] for the red/iron-oxide link. Block is game-sized |
| Iron oxide (hematite) as iron source. Crust holds "iron, magnesium, aluminum, calcium, and potassium" [N-Facts] | Hematite ore, then iron ingot | Smelt in vanilla furnace or blast furnace (hematite to ingot). Reuse `iron_ingot` and all iron tools | Bearings, coils, tools, machine cost | **SIMPLIFIED.** Iron in the crust is **REAL** [N-Facts]. Smelting iron from oxide is standard Earth chemistry (carbon removes the oxygen), **NOT VERIFIED** for Mars; the game smelts in one furnace step. Must NOT claim Mars has rich hematite ore bodies you can walk up to: **NOT VERIFIED** (abundance) |
| Silica (silicate minerals) to glass | Silica sand, dome glass, vanilla `glass` | Smelt silica sand in furnace to `glass`; dome glass = glass + iron ingot frame (idea) | Sealed hab, greenhouse | **SIMPLIFIED.** Silicate crust is real; "silica sand ore" is a game placement (round 2: acceptable simplification). Glass-making is Earth chemistry, **NOT VERIFIED** for Mars. Must NOT claim glass is easy to make with solar heat alone |
| Basalt | Vanilla `basalt`, polished basalt, basalt brick | Vanilla stonecutter and crafting | Shielding, early building, crushed for soil (Phase 3) | **SIMPLIFIED.** Mars volcanic rock is basaltic: **NOT VERIFIED** in this session (earlier rounds only) |
| Basalt fibre (spun from melted basalt) | "Basalt fibre" item, rope, reinforced panels | Smelt basalt (furnace) to "basalt fibre", combine with string-like crafting for panels (idea) | Hull panels, insulation | **INVENTED (small).** Real basalt fibre exists on Earth, **NOT VERIFIED** as a Mars plan. Must NOT claim NASA is making it |
| Aluminium and other metals in the crust | Aluminium ingot (optional) | Smelt from "bauxite-like" rock in blast furnace (idea) | Frames, panel backing | **INVENTED (optional).** Crust holds aluminium [N-Facts] (**REAL**), but extracting it needs huge electric power. Game shortcut; cut first. Must NOT imply aluminium is cheap on Mars |
| Meteorite iron (nickel-iron) | Meteorite iron block (rare) | Mine; smelt for tough iron | Exploring prize | Real thing (SME round 2: accurate). Not re-opened today |
| Sulfates / gypsum | Calcite or sulfate rock, sulfur powder | Mine; smelt rock in furnace to sulfur (idea) | Hull, insulation, sulfur concrete | Sulfates on Mars: earlier rounds, not re-opened. Sulfur use: see 1.2 |

### 1.2 Building materials

| Real material or process | In-game block / item | Crafting or machine idea | Serves | Status and notes |
|---|---|---|---|---|
| Sintered regolith bricks (heat regolith so grains fuse) | Regolith brick | Smelt regolith in furnace to "regolith brick" (copies `brick` smelting); 4 bricks craft the brick block | Compact building, shelter | **SIMPLIFIED.** NASA-funded study lists "strong ceramic tiles made of sintered regolith" as a construction option [N-Constr]. Real sintering needs very high heat (above 1000 C, from a search summary, **NOT VERIFIED**). Must NOT claim a campfire makes bricks |
| Sulfur concrete (melted sulfur binds regolith, no water) | Sulfur-regolith block | Regolith + sulfur powder in a furnace/cauldron-style recipe (idea) | Tube hulls, pads, roads | **SIMPLIFIED.** The same NASA study did "hot-extruding sulfur and JSC1-A regolith stimulant" [N-Constr] and calls it "a reasonably good candidate" for a landing pad apron [N-Constr]. Tests used simulant, not real Mars soil. Strength numbers **NOT VERIFIED** |
| Molten regolith / extruded (3D-printed) | Printed wall (optional) | None needed; stay with bricks | Wide building | Listed in [N-Constr] as an option. Skip in game (N) |
| Shielding by piling regolith or rock overhead | Regolith roof layers | Place blocks; shielding value per block is in the balance sheet | Radiation (dose) | **SIMPLIFIED.** Dose direction sourced round 2; per-block numbers *invented* |
| Pressurised habitat hull, airlock | Hull panel, airlock door | Iron ingot + glass + basalt fibre (idea) | Sealed hab | **SIMPLIFIED.** Real habs are pressure vessels; the panel is a game part |

### 1.3 Air, water and gases

| Real material or process | In-game block / item | Crafting or machine idea | Serves | Status and notes |
|---|---|---|---|---|
| CO2 is the air: "Carbon Dioxide (CO2) - 95.1%", pressure "6.36 mb at mean radius (variable from 4.0 to 8.7 mb)" [N-Fact] | CO2 canister / gas tank | Intake machine (compressor) fills a bucket-like tank from the air; no ore needed | Feedstock for O2, methane, scrubbers | **REAL** [N-Fact]. Intake and compression are real ISRU steps [N-ISRU] |
| MOXIE: split CO2 into oxygen. "inhales Mars' atmosphere and exhales pure oxygen"; "oxygen ions from the carbon dioxide"; other port gives carbon monoxide and unreacted CO2 [N-Moxie]. Peak 10.44 g/h (sol 534) vs a 6 g/h design goal [N-Moxie] | **Oxygen maker** (exists in design) | Machine: CO2 canister + power in, O2 tank out, carbon monoxide as a spent by-product item. Craft: iron ingots, glass, redstone-like circuit (idea) | Life support, O2 index, Phase 3 rule A | **REAL** process, **SIMPLIFIED** scale. MOXIE made 122 g total (search summary, **NOT VERIFIED** on the opened page); a hab needs far more. Game maker output is *invented*. Must NOT claim one small unit keeps a crew alive |
| Water ice: buried ice "within one meter of the surface" is the feedstock; "the surface ice will be heated to sublimate, then water will be retrieved" [N-MSR] | Ground ice block, ice extractor | Mine ground ice (needs iron pick); extractor = iron + furnace + hopper-like craft, placed on ground ice only | Hydrosphere, water, hydrogen | **SIMPLIFIED.** Real: buried ice and heating to recover water [N-MSR]. Depth and place are stylised (round 2). Must NOT claim ice is under every block |
| Polar caps: CO2 and water ice. Polar layered deposits are "the largest potential sink for water" [N-Atm]; CO2 swaps about 25% of air mass with the surface each year [N-Atm] | Polar ice, frozen CO2 block | Mine only in polar zone; no recipe | Polar sink (slice 5) | **REAL** [N-Atm]. The "about a quarter" phrase is the safe one (frost-sink review) |
| Water electrolysis: split water into hydrogen and oxygen [N-MSR] | Electrolyser | Water bucket + power in; hydrogen and oxygen items out (idea). Craft: iron, glass, copper-like wire | O2, hydrogen for fuel and balloons | **REAL** [N-MSR]. Game uses buckets and items, no real flow rates |
| Sabatier reaction: CO2 + hydrogen to methane and water; the water is recycled [N-MSR, N-Sab] | Methane tank, Sabatier reactor | Reactor: CO2 canister + hydrogen in, methane and a water bucket out (idea) | Fuel, water loop, leaving Mars | **REAL** [N-MSR, N-Sab]. The game may use methane as fuel for a beacon rocket stage. Must NOT claim it warms the planet by itself |
| Perchlorate salts: oxidiser in soil, toxic to people. Phoenix found about 0.4 to 0.6 percent perchlorate by mass in the soil leachate (Hecht et al. 2009) | Perchlorate regolith, wash station, airlock brush | Wash regolith with water bucket, drain brine (idea). Brine can feed a "salt" item | Hazard (A1, B5), soil safety, planter base | **REAL, NOT VERIFIED this session:** only the search summary of the PubMed abstract was returned; the page itself did not open (HTTP 203 with no text). Round 1 note R1 holds the figure. "Seams" and a scanner that finds them are **INVENTED** (it is spread through soil, not veins) |
| Perchlorate as oxygen source (heating releases O2); brine harvesting | Perchlorate "bake" recipe (optional) | Furnace: perchlorate salt to oxygen item (idea) | Early O2 trickle | **SIMPLIFIED** and **NOT VERIFIED**: a paper "Fuel and oxygen harvesting from Martian regolithic brine" appeared in a search (PubMed 33257545), not opened. Keep optional |

### 1.4 Power, farming, life

| Real material or process | In-game block / item | Crafting or machine idea | Serves | Status and notes |
|---|---|---|---|---|
| Solar arrays (dust cuts output; storms matter) | Solar panel (round array), dust-off station | Existing design | Power for all machines | **REAL** (storm numbers in balance sheet section 2b/2c, sourced in earlier rounds) |
| Batteries | Battery | Iron + redstone-like dust (vanilla `redstone`) | Night and storm power | **SIMPLIFIED.** Capacity *invented* (10 units) |
| Heat from regolith baking (bound water, gases) | Regolith Oven | Existing machine | Dome water, small pressure | **SIMPLIFIED** (game machine). See section 2 |
| Greenhouse growing in treated regolith: wash out salts, add nutrients, sealed bed | Planter, grow lamp, farmland look | Vanilla `farmland` rules; wash regolith (1.3), add `bone_meal` or compost as "organic matter"; hoe to till | Biomass, C1 | **SIMPLIFIED.** Real Mars soil needs treatment (perchlorate, no organic matter): **NOT VERIFIED** (no growth paper opened). Must NOT claim crops grow in raw regolith |
| Vanilla potato, wheat | Vanilla crops | Unchanged | Food | Game choice. Real crop trials on simulant: **NOT VERIFIED** |
| Algae and microbes for O2 and CO2 | Algae vat | Water + light + CO2 (idea) | Phase 3 O2 swap | **SIMPLIFIED**, source not opened this session; Phase 3 numbers are fiction and labelled (round 3) |

## 2. INVENTED items the game needs

Each has a plain journal label. All amounts, speeds and costs are *invented*. All items obey the world-and-blocks rule: **a block never changes pressure or temperature by itself; a machine does.**

| Invented item | Stands in for | What it must NOT claim | Journal label (plain) |
|---|---|---|---|
| **Regolith Outgasser** (exists) | Heating soil and polar-edge materials so trapped gas is released. Real Mars holds buried CO2 (up to about 80 percent of the air's mass, Phillips et al. 2011, carried from round 2, not re-opened) | That any machine like this exists or that heating regolith could thicken the air quickly | "Game invention, not real. Real Mars has trapped gas, but no machine like this has been built." |
| **Regolith Oven** (exists) | Baking bound water out of soil. Heating ice-bearing regolith to get water is real [N-MSR] | That an oven bakes real pressure into the air | "Game-sized version of a real idea: heating soil to get water." |
| **Super-greenhouse gas emitter** | Strong warming gases made on site. Warming gases are real physics; no plan to make them on Mars at scale is verified | That it is a tested method or safe | "Game invention. Scientists have only studied this idea on paper." (**Method NOT VERIFIED**) |
| **Ice extractor** | Heated drill that recovers ice water [N-MSR] | That one block mines a lake | "Game-sized. Real plans heat ice-rich soil to get water." |
| **Atmospheric processor** | A CO2 intake and conversion plant [N-ISRU] | Real size or speed | "Game invention based on real CO2 processing; real ones are tiny so far." |
| **Carbon scrubber and carbonate rock** | Locking CO2 into carbonate minerals. Chemistry real; Mars carbonate abundance **NOT VERIFIED** | That Mars has lots of carbonate | "Game-scale chemistry. The real process is far slower." |
| **Soil treatment unit** | Washing out perchlorate and adding organics so plants can grow | A proven Mars farming method | "Game invention. Real Mars soil would need cleaning, but this machine is made up." |
| **Biome seed kits** (biocrust, tundra meadow, wetland margin, cold forest) | Steps of ecological succession | That life spreads this fast or can survive outside a sealed area today | "Game invention. The order (crust, then moss, then plants) is realistic; the speed is made up." |
| **Survey scanner** | Orbital and rover mapping of ice and minerals | That it finds perchlorate "veins" | "Game tool. Real surveys map ice from orbit; perchlorate is spread through the soil." |
| **Mirror uplink, insulation shroud** | Orbital mirrors, insulated structures | Anything buildable today | "Game invention (mirrors are an old paper idea, **NOT VERIFIED**)." |
| **Lettuce, tomato (new crops)** | Real crops that need warm, wet, pressurised, salt-free conditions | That they grow on Mars unprotected | "Game crops. Real plants need protection on Mars." |
| **Water reclaimer, quiet-zone marker, lure station, meltwater treatment** | Water recycling is real (life support); the rest are game aids | Real hardware | "Game part. Recycling water is real; this unit is simplified." |
| **Colonization beacon** | A transmitter and landing marker | Any real mission | "Game goal, not a real device." |
| **Basalt fibre, aluminium, perchlorate bake** (optional, section 1) | Real Earth materials | That Mars plans use them | "Game shortcut." |

## 3. Sources I opened

Fetched by `curl` this session into the scratchpad, HTML stripped with `python3 -I`, never run. Search summaries are **not** sources; anything only seen in a summary is **NOT VERIFIED**.

| Ref | URL | Quoted line | Used for |
|---|---|---|---|
| N-Facts | https://science.nasa.gov/mars/facts/ | "iron minerals in the Martian dirt oxidize, or rust, causing the surface to look red"; "a crust made of iron, magnesium, aluminum, calcium, and potassium" | Regolith colour; crust metals |
| N-Fact | https://nssdc.gsfc.nasa.gov/planetary/factsheet/marsfact.html | "Surface pressure: 6.36 mb at mean radius (variable from 4.0 to 8.7 mb depending on season)"; "Carbon Dioxide (CO2) - 95.1%" | CO2 air |
| N-Moxie | https://science.nasa.gov/blog/moxie-sets-consecutive-personal-bests-and-mars-records-for-oxygen-production/ | "a unique device near its heart that inhales Mars' atmosphere and exhales pure oxygen"; "first demonstration on another planet of In Situ Resource Utilization"; "peak of 10.44 grams per hour"; "design goal of 6 grams per hour" | MOXIE |
| N-Moxie2 | https://science.nasa.gov/resource/mars-oxygen-isru-experiment-instrument-for-mars-2020-rover-is-moxie/ | "will produce oxygen from Martian atmospheric carbon dioxide" | MOXIE |
| N-ISRU | https://ntrs.nasa.gov/citations/20160004359 | "produce methane (CH4) and oxygen as a fuel/oxidizer combo via a Sabatier reaction using resources from the Martian environment" | Sabatier, CO2 plant |
| N-MSR | https://ntrs.nasa.gov/api/citations/20110014952/downloads/20110014952.pdf | "CO2 from the atmosphere and extracted ice from the Martian regolith (within one meter of the surface) are the only feedstocks"; "through the use of a Sabatier reactor and water electrolysis cells, hydrogen, methane, and oxygen will be produced" | Ice, Sabatier, electrolysis |
| N-Sab | same page as N-ISRU, plus NTRS bibliography https://ntrs.nasa.gov/api/citations/20040045217/downloads/20040045217.pdf | "combined electrolysis of water vapor and carbon dioxide ... solid oxide electrolysis cell" (bibliography entry) | Electrolysis of CO2 and water |
| N-Atm | https://ntrs.nasa.gov/api/citations/19980147990/downloads/19980147990.pdf (NASA/TM-98-206538) | "This cyclic exchange of about 25% of the atmospheric mass, between the atmosphere and the surface"; "the polar layered deposits constitute the largest potential sink for water" | Polar sink |
| N-Constr | https://ntrs.nasa.gov/api/citations/20170004640/downloads/20170004640.pdf (NIAC Phase II, Khoshnevis et al.) | "strong ceramic tiles made of sintered regolith and possibly grouted by sulfur concrete or molten silica"; "mixing and hot-extruding sulfur and JSC1-A regolith stimulant" | Bricks, sulfur concrete |

**Not opened (NOT VERIFIED):** Hecht et al. 2009 perchlorate paper (PubMed 19574385; the page returned no text, only a search summary was seen); MOXIE total of 122 g; the PubMed brine-oxygen paper 33257545; sintering above 1000 C and the 50 MPa sulfur concrete result (summaries of other pages); basalt fibre; aluminium extraction; Earth smelting and glass chemistry; crop growth in simulant; carbonate abundance; Phillips et al. 2011. Host limits: `www.planetary.org` article URLs I tried returned 404, and `science.nasa.gov` MOXIE mission page returned 404.

## 4. Notes for the design team

- Keep **vanilla items as the base of every recipe** and put the Mars part in the machine or the input (regolith, ice, CO2 canister). Players keep furnaces, hoes and buckets.
- The most science-defensible chain is a **real loop**: CO2 canister + ice, then electrolysis and Sabatier, then O2, water, methane. Every step has a NASA source above. Use it as the spine of Phase 1 and 2 crafting.
- Biggest honesty risk: the **super-greenhouse emitter and the biome seed kits**. Labels in section 2 must show in the journal.
- Do not let any recipe change pressure or temperature directly (rule in world-and-blocks section 4).
