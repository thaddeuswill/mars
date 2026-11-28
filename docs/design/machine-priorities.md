# Machine priorities: which machines gate, which teach, which are extras

**Owner:** Lead Game Designer · **Written:** 2026-11-27 · **Status:** confirmed by the designer; owner decisions at the end · **Reads:** `docs/art/invented-machines-look.md`, `docs/design/world-and-blocks.md` (3.2, 3.6, 0.2), `DESIGN.md` gate table, `docs/design/balance-sheet.md`, `pack/data/mars/gates/`, `docs/tech/stage3-plan.md`.
Marks: *invented* = our number; **unverified** = not checked. Legend: **G** gate, **L** supports learning, **N** nice to have.

## 1. What exists today

Built machines: solar panel, battery, Regolith Oven, Regolith Outgasser (`pack/data/mars/machines/`). Gates in data: Dome stable (stand-in condition), Phase 2 begins (5 kPa; its `unlocks` list is empty because the unlocked machines do not exist). **Every machine below is unbuilt.** Thaw, Hydrosphere, Phase 3 begins, Breathable and Equilibrium have no gate file yet.

## 2. Confirmed priority list (Artist's 11 machines)

| # | Machine | Class | Gate it serves (DESIGN.md table) | Index row it moves | Unlocked at | Role clear? |
|---|---|---|---|---|---|---|
| 1 | Water reclaimer | **G**, not wired | Dome stable (real life support; today a power stand-in) | none (dome water) | Start | **Unclear.** The Dome stable condition is "life support, 3 sols, no failures"; nothing says the reclaimer is part of it. Decide when life support becomes real |
| 2 | Greenhouse planter | **L** | none (food; C1 dead-tomato lesson) | none | Phase 1 | Clear. Artist had it as G; world-and-blocks 3.3 says L |
| 3 | Atmospheric processor | **G** | Phase 2 begins unlocks it; carries pressure 5 to 30 kPa (Phase 3 begins) | pressure, CO2 (mostly filler gas) | Phase 2 begins | Clear. **No rate in the balance sheet** |
| 4 | Super-greenhouse emitter | **G** | Thaw, Phase 3 begins (0 C) | temperature | Phase 2 begins | Clear. No rate. Route A for warming |
| 5 | Mirror uplink | **N** (route B) | Same gates as the emitter, as an alternative | temperature | Phase 2 begins | Clear if optional. If the owner makes it required it becomes G |
| 6 | Ice extractor | **G** | Hydrosphere (10 %) | hydrosphere | **Thaw** (not Phase 2 begins) | **Unclear.** Hydrosphere also needs warmth and pressure (ice-to-water rule); the share from extractors versus natural melt is not set |
| 7 | Survey scanner | **L** | supports the extractor (find ice) | none | Phase 1 | Clear; cut candidate (Monitor map page) |
| 8 | Carbon scrubber | **G** | Breathable (CO2 at or below 0.5 kPa) | CO2 down, carbonate | Phase 3 begins (**not in the gate table**) | Clear. Balance sheet: scrubbing totals about 1.1 kPa over Phase 3 |
| 9 | Soil treatment unit | **G (indirect)** + L | Kits need treated soil | none (feeds biomass) | Phase 3 begins | Clear |
| 10 | Seed kits (4 items) | **G** | Breathable (biomass swaps CO2 for O2) | biomass, O2, CO2 | Phase 3 begins | Clear. The dispenser block is **N**: recipes craft kits by hand |
| 11 | Beacon | **G** | Equilibrium (assembly); recipes at Breathable | none | Breathable | Clear |

### Machines the Artist list leaves out

| Machine | Class | Why it matters | Art needed? |
|---|---|---|---|
| Transit tubes and junction | **G** (expansion after Phase 2 begins) | "Wide building is earned" (pillar 4) | Yes, blocks; add to the Artist list |
| Algae vat | **L / G-helper** (unlocks at Hydrosphere) | Turns CO2 to O2 like flora; flora alone can do the job, so not strictly required | Yes; propose after the scrubber |
| Meltwater treatment | **L** (unlocks at Hydrosphere; perchlorate, B5) | Teaches salty water | Yes, small |
| Oxygen maker | exists as art; **G** for real Dome stable | Spine step (MOXIE idea) | Done |
| Compressor intake, electrolyser, Sabatier | **L** (REAL idea spine) | CO2 canister, hydrogen, methane; no gate | Only if the spine ships |
| Wash station (vanilla cauldron style) | L | Phase 1 planter soil | Placeholder, no new art |

## 3. Gates with no machine path yet

| Gate | What is missing |
|---|---|
| Dome stable | Real life support: oxygen maker, reclaimer and sealing are not coded; stand-in is "grid covers demand" |
| Phase 2 begins | Path exists (Outgassers and Ovens), but its unlock list is empty |
| Thaw | Emitter (and uplink) unbuilt; no temperature source exists in code; no gate file |
| Hydrosphere | Extractor unbuilt; hydrosphere definition open (`world-and-blocks.md` section 4, Technical Lead); no rate |
| Phase 3 begins | Needs processor and emitter together at 30 kPa and 0 C |
| Breathable | Seed kits, soil unit, scrubber unbuilt; scrubber and kits are not in the gate unlock table (kits and soil are; scrubber is not) |
| Equilibrium | No machine; beacon assembly only |

## 4. Recipe and amount conflicts

| # | Conflict | Where | Proposed fix |
|---|---|---|---|
| R1 | **No redstone or copper on Mars.** Worldgen has hematite, dark sand, gypsum, ground ice, spherules, vesicular rock only. Recipes for the compressor, oxygen maker, electrolyser, Sabatier, scanner, battery and grow lamp need redstone or copper | `world-and-blocks.md` 3.6 vs `pack/data/mars/worldgen/configured_feature/` | Add one rare ore (SME to say what real mineral stands in) or swap those inputs for iron and sand. **Blocks every machine but the extractor and scrubber** |
| R2 | Ice extractor timing: Artist and 3.2 say "Phase 2 opener"; DESIGN says unlocked at **Thaw** | DESIGN.md gate table line 73 | DESIGN wins; Artist order fixed |
| R3 | Seed kit recipe needs **carbonate rock**, which is the scrubber's output; scrubber is Phase 3, and scrubbing costs O2 source | 3.6 farming table, 3.1 | A small mined carbonate vein for the first kits (abundance **unverified**), or kits use gypsum |
| R4 | Kit recipe needs **bone meal**, and the soil unit too; no animals on Mars | 3.6 | Compost from planter crops. Good link (Phase 1 planter feeds Phase 3); say so in the journal |
| R5 | No cost or power rows for any Phase 2 or 3 machine; no contribution rate for processor, emitter, extractor, scrubber, uplink, vat, kit. Sheet gives only phase-average net rates (+0.46 kPa/sol, +1.1 C/sol, +1.8 kPa/sol) | `balance-sheet.md` sections 3, 3a | Designer to add rows once gate files exist; Technical Lead builds from JSON |
| R6 | Scrubber unlock missing from the gate table | DESIGN.md line 75 | Add "carbon scrubber" to Phase 3 begins (proposal; Breathable's last check needs it) |
| R7 | Seed kit dispenser has no recipe or role | Artist row 9 | Cut it; art the 4 kit items only |
| R8 | Band wording: 3.5 splits "Game invention, not real" from "Game-sized version of a real idea" (reclaimer, extractor, scrubber, oxygen maker); the Artist band is one cue for all | brief section 1 vs 3.5 | One band, two tooltip wordings (see decision D2) |

## 5. Confirmed work order and first batch

See `docs/art/invented-machines-look.md` section 3. Order: band, reclaimer, processor, emitter, extractor, scanner, planter, kit items, soil unit, scrubber, beacon, uplink. **First batch: water reclaimer, atmospheric processor, super-greenhouse emitter** (after the band). Reason: the player meets the reclaimer first (Phase 1), then the Phase 2 begins unlocks (processor, emitter), which are the pressure and temperature levers every later gate depends on. The extractor waits for Thaw.

Effect on the two goals: **instructional** (A1, B1, B4: the processor and emitter are the Index levers a player must balance; the band keeps invented machines honest); **appeal** (the first batch gives the Phase 2 look early, and the gold processor is the most photogenic).

## 6. Decisions for the owner

| ID | Decision | Type | Recommendation |
|---|---|---|---|
| D1 | Source for redstone and copper (R1) | Ill-structured (science, fun, cost) | One rare "conductor ore" with SME naming a real analogue; else iron-only recipes |
| D2 | Band on all machines, or only "Game invention" ones? | Well-structured | All; two tooltip wordings |
| D3 | Is the mirror uplink required or optional? | Ill-structured | Optional (route B); keep last in art |
| D4 | First kit carbonate: small mined vein, or gypsum? (R3) | Ill-structured (SME: abundance unverified) | Small vein, labelled game-scale |
