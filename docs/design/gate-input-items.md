# Gate input items: one new thing per gate that the next machine needs

**Owner:** Lead Game Designer · **Written:** 2026-11-28 · **Status:** PROPOSAL (nothing built; no recipe, item or gate file is changed) · **Steals:** inspiration survey 4 (new unseen input per gate), plus 1 (hold-to-explain cards), 6 (planet report page), 7 (log items).
**Reads:** `docs/design/inspiration-survey-mechanics.md`, `phase3-hydrosphere-and-drift.md`, `machine-priorities.md`, `seed-kit-recipes.md`, `world-and-blocks.md` (0.2, 3.6), `docs/science/mars-conductor-and-carbonate-ores.md`, `docs/science/machine-references.md`, `pack/data/mars/gates/`, `pack/data/mars/recipe/`.
**Marks:** **INVENTED** = our number or item. **unverified** = not checked. Scope (owner): standard pace only; vanilla crafting stays; make-it-work rule (0.2) and the real-reference rule (5a) apply. Item ids are proposals.

## 1. The rule

At every gate the player gets **one item they have never seen** (a "gate cache" of a few, plus a recipe the journal shows). **The next machine's recipe needs that item.** So each gate changes what you can build, and the player can feel it.

How the player is never stuck (every row in section 2 follows this):
1. **Cache:** a one-time delivery when the gate latches (same pattern as the two starting Ovens: once per world, into the inventory, with a Monitor line "Supply drop received"). Enough for the first machine plus one spare.
2. **Craft path:** a plain vanilla crafting-table recipe from Mars-minable inputs. No new machine is needed to make the item (a "placed machine" crafting step is a later upgrade, not required). Recipes are open from the start; the machine they feed is still locked by the gate (placement lock), so nothing is skipped.
3. **No dead inputs:** no recipe here uses coal, wood, gold, quartz or glowstone (not on Mars; machine-priorities R1, SME rulings). Copper and redstone only at the end, and the Breathable cache tops them up.

## 2. Gate-to-item table

| # | Gate (order) | New item (id) | Real idea | Label | Needed by (next build) | Index / goal |
|---|---|---|---|---|---|---|
| 1 | Dome stable (1) | **Heater coil** (`mars:heater_coil`) | Resistive heating wire; Phoenix TEGA ovens heat soil | Game-sized version of a real idea | Regolith Outgasser; later every emitter | Pressure; C2, A1 |
| 2 | Phase 2 begins (2) | **Filter cartridge** (`mars:filter_cartridge`) | MOXIE and the ISS air systems filter dust from intake air | Game-sized version of a real idea | Atmospheric processor (intake) | Pressure; C1, C2, A1 |
| 3 | Thaw (3) | **Pump bearing** (`mars:pump_bearing`) | Rotating parts in pumps wear out; hematite spherules are real Mars finds | Game-sized version of a real idea | Ice extractor (pump) | Hydrosphere; C2, A2 |
| 4 | Hydrosphere (4) | **Resin pack** (`mars:resin_pack`) | Ion-exchange resin catches perchlorate in water on Earth (verified, SME); nobody has cleaned real Mars soil this way | Game-sized version of a real idea (SIMPLIFIED) | Meltwater treatment, then planter water | Safe water; B5, C1 |
| 5 | Phase 3 begins (5) | **Sorbent bed** (`mars:sorbent_bed`) | ISS CO2 scrubber (CDRA) uses zeolite or amine beds that capture CO2 and are heated to let it go again (real, regenerable) | Game-sized version of a real idea (SIMPLIFIED: the game locks CO2 into rock) | Carbon scrubber | CO2 and O2; A5, B4 |
| 6 | Breathable (6) | **Signal board** (`mars:signal_board`) | A radio transmitter needs a circuit board | Game invention, not real | Beacon bench, transmitter core | Beacon; B4 |
| 7 | Equilibrium (7) | **Activation cell** (`mars:activation_cell`) | A transmitter needs a charged power source | Game invention, not real | Colonization beacon, final lighting | Ending; D2 |

Maintenance link (DESIGN.md parts list): rows 1, 2, 3 are three of the four parts (heater coil, filter cartridge, bearing). The fourth, **seal kit**, exists from the start (landing checklist step "seal kit, patch a breach"), so it is not a gate reward. Rows 1 to 3 are also the **spares** the failure reports ask for (emitter wear in `phase3-hydrosphere-and-drift.md` 1.2), so the reward keeps being useful. All wear intervals stay open (balance sheet section 8); this doc only makes each item exist and be needed once.

## 3. Item detail

All amounts **INVENTED**. "Cache" is the gate delivery. Tooltip text is grade 6 to 7, US spelling, no gas names (forecast guard CR-F9). Journal reveal: a one-line entry the first time the item is held.

### 3.1 Heater coil (Dome stable)
| Field | Spec |
|---|---|
| Obtained | Cache **4 coils**. Craft: shapeless **2 iron ingots -> 1 coil** (iron wire). Replaces the earlier "iron + coal" proposal because coal does not exist on Mars (`phase3-hydrosphere-and-drift.md` 1.2 flagged this for the SME) |
| Unlocks next | Regolith Outgasser recipe gains **1 coil** (Technical Lead edits the recipe). Emitters (gate 2) take 1 coil each, so 4 coils plus crafting covers the first emitters |
| Tooltip | "Heater coil. Iron wire that glows when power runs through it. Machines that make heat need one." |
| Journal | "Heat needs a wire that resists power. Your first coils came from the lander." |
| No hard block | Iron comes from hematite, which is in the dome area. 2 iron per coil |

### 3.2 Filter cartridge (Phase 2 begins)
| Field | Spec |
|---|---|
| Obtained | Cache **3**. Craft: **2 glass pane + 1 regolith brick + 1 iron nugget -> 1 cartridge** (all Phase 1 materials) |
| Unlocks next | Atmospheric processor recipe gains **1 cartridge**. Option (later, balance sheet): the processor uses one every 10 sols (**INVENTED**); a clogged cartridge is a failure report (C1) |
| Tooltip | "Filter cartridge. Catches dust before air enters a machine. It clogs, so keep a spare." |
| Journal | "Mars air carries dust. Real air systems filter it first." |
| No hard block | Glass and brick are early items; the cache covers the first processor |

### 3.3 Pump bearing (Thaw)
| Field | Spec |
|---|---|
| Obtained | Cache **2**. Craft: shaped ring of **4 iron nuggets around 1 iron ingot -> 2 bearings**. If hematite spherules drop an item (Technical Lead check), 1 spherule may replace the ingot (a nice real link: "blueberries") |
| Unlocks next | Ice extractor recipe (iron, furnace, hopper, bucket per `phase3-hydrosphere-and-drift.md` slice A) gains **1 bearing**. Wear: a stuck pump is a failure report (later) |
| Tooltip | "Pump bearing. A steel ring that lets a pump spin. Pumps need them." |
| Journal | "Ice is under your feet. A pump that spins needs a bearing." |
| No hard block | Only iron; the extractor itself is never gated by an ore |

### 3.4 Resin pack (Hydrosphere)
| Field | Spec |
|---|---|
| Obtained | Cache **4**. Craft: shapeless **2 sulfur powder + 1 dark sand + 1 water bucket (bucket returned) -> 2 packs**. Sulfur powder is the Phase 2 smelt of sulfate rock (3.6) |
| Unlocks next | Meltwater treatment recipe (stubbed in slice A of the water plan) holds **1 pack**, and uses one every N sols (**INVENTED**, open). Planter beds need treated water from now on |
| Tooltip | "Resin pack. Catches salts in water. Game-sized version of a real idea: on Earth, resin catches perchlorate. Nobody has cleaned real Mars soil this way yet." (SME wording; label changed from "Game invention")  |
| Journal | "Melted ice is salty. On Mars the salt is perchlorate, and plants dislike it." (Real fact: perchlorate in Phoenix soil, NTRS 20190028297, verified. Resin on Earth: verified. The Mars-ready pack is SIMPLIFIED, not invented. Journal must say the salt comes from the soil, not from the ice itself.) |
| No hard block | Sulfur and sand are Phase 2 items. Untreated water still runs the Index; it only harms planters (a consequence, not a lock) |

### 3.5 Sorbent bed (Phase 3 begins)
| Field | Spec |
|---|---|
| Obtained | Cache **2**. Craft: **2 regolith brick + 1 glass pane + 1 iron ingot -> 1 bed** (porous brick is an INVENTED stand-in for zeolite or amine pellets, SME; the journal must not say brick captures CO2) |
| Unlocks next | Carbon scrubber recipe gains **2 beds** (twin beds, as in the CDRA). Beds fill and need swapping (later; the scrubber's carbonate output is the balance-sheet piece) |
| Tooltip | "Sorbent bed. Porous pellets that hold CO2 until they fill. Real ones use zeolite or amine, and are heated to let the CO2 go." (Gas name is allowed here: the scrubber's whole job is CO2; forecast F8 concerns kits only) |
| Journal | "Your air has a lot of CO2. A bed can catch it, the way real space stations do. On the station it is let out again. In the game, the scrubber locks it into rock." |
| No hard block | The soil unit and kits, also unlocked here, do not need a bed, so the player can always progress biomass |

### 3.6 Signal board (Breathable)
| Field | Spec |
|---|---|
| Obtained | Cache **2 boards plus 4 copper ingots and 6 redstone** (tops up the rare ores, so the end is not decided by a vein search). Craft: **1 copper ingot + 2 redstone + 1 glass pane + 1 iron nugget -> 1 board** |
| Unlocks next | Beacon bench recipe (needs 1 board); the **transmitter core** needs **3 boards** (so the bench teaches the craft, then the core uses it). Mast segments, lamps and dish still use iron and glass only |
| Tooltip | "Signal board. A circuit for the beacon radio. Game invention, not real." |
| Journal | "A beacon must speak. This board is the part that does the talking." |
| No hard block | Cache plus copper and redstone ore; the Breathable hold gives time to craft during the wait (sheet section 4) |

### 3.7 Activation cell (Equilibrium)
| Field | Spec |
|---|---|
| Obtained | Cache **1** (the gate's reward, the "last part from the lander"). Craft path if lost: **2 batteries + 1 signal board + 1 iron ingot -> 1 cell** |
| Unlocks next | `mars:colonization_beacon` final assembly consumes it. This is the last placement unlock |
| Tooltip | "Activation cell. Charges the beacon for its first call. Game invention, not real." |
| Journal | "The planet holds steady. Now the beacon can call home." |
| No hard block | Always craftable from the batteries the player already owns |

## 4. New blocks and items to build

| Kind | Items |
|---|---|
| Items (7) | heater coil, filter cartridge, pump bearing, resin pack, sorbent bed, signal board, activation cell |
| Recipes to add (7 plus 3 edits) | The seven craft recipes above (JSON). **Edits:** Regolith Outgasser (+coil), atmospheric processor (+cartridge), super-greenhouse emitter (+coil) in `pack/data/mars/recipe/`; later ice extractor, meltwater treatment, scrubber, beacon bench, transmitter core, beacon |
| Gate data | Cache table per gate (item id, count) in `pack/data/mars/gates/*.json` as a new `cache` list; add `thaw`, `hydrosphere`, `phase_3_begins` unlocks as the machines exist (existing files: `thaw.json` unlocks is empty today) |
| Delivery | One-time cache grant on latch: reuse the starting-Oven grant code path |
| Logs (steal 7) | 7 "supply notes", one inside each cache, 2 lines each; journal entry shows "?" until read. Text is Instructional Designer and Artist work |
| Machines needed by these gates | ice extractor, meltwater treatment, scrubber, soil unit, beacon bench, beacon (already planned in `machine-priorities.md`) |

## 5. Build order for the Technical Lead (3 slices)

| Slice | Content | Done when | Why |
|---|---|---|---|
| **A. First two gates** | Item registry for coil and cartridge; recipes; Outgasser, processor and emitter recipe edits; `cache` field in gate JSON and the grant on latch; Monitor "Supply drop received" line; hold-to-explain static card for the 9 existing machines (section 6) | Boot check: latch Dome stable, 4 coils arrive once; Outgasser cannot be crafted without a coil; latch Phase 2 begins, 3 cartridges arrive once | Uses only existing machines, so it ships first |
| **B. Water gates** | Bearing and resin pack; ice extractor and stubbed meltwater treatment (water-plan slice A); `thaw.json` and new `hydrosphere.json` unlock lists and caches; start of the report-page snapshot (stored parameter values at each latch, `PlanetState` schema bump) | Boot check reaches Thaw, gets 2 bearings, builds an extractor; report page shows before and after for gates 1 to 4 | Fixes the Thaw gate that unlocks nothing today |
| **C. Air and finish** | Sorbent bed, signal board, activation cell; scrubber, soil unit, bench, beacon; supply notes and "?" journal entries; full report page including side-effect lines | Boot check latches Breathable and Equilibrium in a test and consumes the cell | Depends on the conversion mechanism of water-plan slice B |

## 6. Hold-to-explain card (minimal, static first)

Rules: hold a key (proposal: `H`) while aiming at a machine; a small panel shows the card. Text **25 words or fewer** (counted), grade 6 to 7, US spelling, no color-only signal. Keys `card.mars.<id>`. The label sentence is part of the count. Cards for machines that touch forecast answers must not reveal them (CR-F9): the Instructional Designer scans before ship. Real references are from `docs/science/machine-references.md`; the label wording A or B follows `seed-kit-recipes.md` section 4.

| Machine | Card text | Words | Label |
|---|---|---|---|
| Planetary Monitor | "Shows the planet's six numbers and your next goal. Read it before you build. Game tool, not real hardware." | 19 | tool |
| Solar panel | "Turns sunlight into power. Dust blocks light, so brush it clean. Output drops at night and in storms." | 18 | none (real) |
| Battery | "Stores power for night and storms. Fills when panels make extra. An empty battery means machines stop." | 17 | none (real) |
| Regolith Oven | "Bakes regolith to free water and some gas for the dome. Uses power. Game-sized version of a real idea." | 19 | B |
| Regolith Outgasser | "Heats soil to release trapped gas. This raises pressure a little. Needs power and a coil. Game invention, not real. Idea: Phoenix soil ovens." | 24 | A |
| Atmospheric processor | "Makes and adds gas to the air. Raises pressure toward 30 kPa. Needs power and a filter. Game invention, not real. Idea: MOXIE." | 23 | A |
| Super-greenhouse emitter | "Traps heat to warm Mars. Raises temperature toward 0 C. Needs power and a coil. Game invention, not real. Idea: greenhouse gas." | 23 | A |
| Water reclaimer | "Cleans used dome water so you can drink it again. Uses power. Game-sized version of a real idea: ISS water recovery." | 21 | B |
| Oxygen maker | "Splits CO2 into oxygen. Needs power and a CO2 canister. One unit cannot keep a crew alive. Game-sized version of MOXIE." | 21 | B |

Notes: the Outgasser card is 24 words with the coil clause; if the Technical Lead drops the coil from the recipe, drop the clause. The processor card names "30 kPa" (the Phase 3 gate value, a physical threshold, public in the Monitor). Later: animated version (Create Ponder idea) is a separate, medium task; licence not checked.

## 7. Planet report page spec

A one-screen page shown once at each gate (and reopenable from the journal): **the six parameters before and after, plus one side effect your work caused** (survey steal 6; goals B1, B2, D2). Built from a stored snapshot of the six values at the previous latch and at this latch. Fits 232 px: one line per parameter, 34 characters or fewer. Shapes with numbers, never color alone (arrow up, arrow down, equals sign).

**Template** (placeholders in braces; grade 6 to 7):
```
Report: {gate_name}
Pressure   {p0} to {p1} kPa   {arrow}
Temp       {t0} to {t1} C     {arrow}
Water      {h0} to {h1} %     {arrow}
Oxygen     {o0} to {o1} kPa   {arrow}
CO2        {c0} to {c1} kPa   {arrow}
Life       {b0} to {b1} BU    {arrow}
Side effect: {side_line}
Why: {cause_line}
```
(Row labels reuse the Monitor labels; parameter names and units come from `pack/data/mars/parameters/`.)

| Gate | Side effect line (template) | Cause line | Data it needs | Goals |
|---|---|---|---|---|
| Dome stable | "Your machines use {d} units a sol." | "Every machine you add draws power." | grid demand (exists) | A3, B4 |
| Phase 2 begins | "Frost took back {f} kPa on cold nights." | "Cold ground freezes some CO2 out of the air." | frost sink total (exists, `sinks/`) | A1, B2 |
| Thaw | "{n} blocks of ground sank near hot machines." | "Heat melted ice under the soil." | ground-collapse count (new counter, B5) | B1, D2 |
| Hydrosphere | "Meltwater test: {s} percent salt level." Alt: "Wildlife signature rose to {w}." | "Melted ice carries salts. Warm, wet places draw life." | fauna signature (exists in design); perchlorate level (new) | B5, D2 |
| Phase 3 begins | "Heat leaks {l} C each sol." | "Mars loses heat to space unless emitters keep up." | temperature loss term (exists) | A3, B2 |
| Breathable | "Plants used {c} kPa of CO2." | "Living things swap one gas for another." | flora conversion total (needs water-plan slice B) | A5, B1 |
| Equilibrium | "Repairs this hold: {r}. Wear events: {e}." | "Holding a planet steady takes upkeep." | repair and event counts (needs drift engine) | C2, D2 |

Every number is a real stored value shown to the player; none is invented in the template. Decision on the Hydrosphere row (salt level versus fauna) is open. Instructional Designer owns final wording and the alignment check; the Artist owns the page's look.

## 8. Walkthroughs (design walk, not real players)

- **12-year-old, never used a modpack:** latches Dome stable, sees "Supply drop received", reads "heater coil" in the journal, and the Outgasser recipe in the recipe book shows a coil. The gap between "I got a thing" and "the thing is a recipe slot" is one step. Risk: the player crafts the Outgasser before the coil (cannot) and doesn't know the recipe changed; mitigation: the unlock line on the Monitor names the part.
- **Experienced 17-year-old:** sees the cache as a hint of what to stockpile. Crafts 20 coils early. No harm: coils are cheap, and the gate remains the lock (they cannot place the machine early). Pressure comes from the machine counts, not from the item.
- Risk shared by both: the new item is a "fetch quest in disguise" if the cache is empty by the time they look. Hence cache sizes cover the first machine plus a spare.

## 9. Assumptions and open items

| Item | Owner |
|---|---|
| Hematite spherules as an item (3.3), regolith brick availability, sulfur powder in Phase 2 | Technical Lead |
| ~~"Porous brick" and "resin removes perchlorate" unverified~~ Answered by SME 2026-11-28: resin real on Earth (SIMPLIFIED); brick is an INVENTED stand-in; wording applied above | closed |
| Wear intervals for coil, cartridge, bearing, resin, bed (all open) | Designer with balance sheet |
| Where the cache arrives (inventory versus lander crate) | Owner pick; recommendation inventory, like the Ovens |
| Per-recipe amounts are all **INVENTED**; tune after a playtest | Owner playtest |
