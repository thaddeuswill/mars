# Ruling: recipe inputs on a planet with no wood and no cobblestone (v2: orbital resupply)

**Owner:** Lead Game Designer · **Written:** 2026-11-28 (v2; v1 superseded, kept in the appendix) · **Status:** ruling v2, awaiting owner nod (section 9) · **Scope:** standard pace, solo; make-it-work rule (`world-and-blocks.md` 0.2, item 5b); never hard-block. **Marks:** **INVENTED** = our number or item. **NOT VERIFIED** = not checked. **Game design** = a story device, not real.
Serves: C2, A1 (machine parts), B4 (the Index comes from your machines); pillars "pressure, not grind", "your progress provokes the planet". Goal IDs unchanged.

## 1. The rule (one sentence)
**Orbit sends the tech; Mars gives the science. Every gate drops the hard parts, and the Index only moves when the player's own machines run.**

Owner words (5b): original Minecraft items are not required; if a part cannot be made on Mars simply, a satellite or the ISS drops a package at set points in the gate line. Supersedes v1 (lander salvage plus basalt fibre and basalt board in the `planks` tag), which is dropped: no new tag files, no board, no fibre chain.

## 2. Who sends it (labels, all game design)
| Sender | Used at | Label (grade 6-7, US spelling) |
|---|---|---|
| **Lander stock** | Start | "Lander stock. Your first tools came with you." |
| **Relay Orbiter** (an orbiter like the real ones that relay for Mars rovers; the supply role is invented) | every gate | "Sent from orbit. Game design: real spacecraft do not resupply Mars like this. Parts only. Your machines change the planet." |
The ISS is **not** named as a sender: it orbits Earth and cannot reach Mars, so naming it would teach something false. It can appear once in the journal as the model for a "supply drop idea" (real: ISS cargo ships resupply the crew; **verified common knowledge**, SME may confirm).

## 3. What a drop is allowed to carry (the tech-not-progress line)
| Drop gives (tech) | Drop never gives (progress) |
|---|---|
| Machine parts, cores, vanilla bits we cannot make (hopper, furnace, bucket, table, glass, copper, redstone), fuel, gate parts | Any Index change, regolith, ice, ore, water, soil, seeds, kits, or finished Index machines |
| Items used by a machine recipe | A placed machine, or a machine already unlocked |
Mars-made on purpose (teaches science): regolith, ice, hematite and iron, dark-sand glass, washed regolith and the soil ladder (A5, B5), gate parts already built (coil, cartridge, bearing stay craftable), and every machine that changes the Index. Rule check: nothing in a drop moves pressure, temperature, water, O2, CO2 or biomass. A player who only opens drops sees all six rows stay flat.

## 4. New drop item: the machine core
One new item, `mars:machine_core` ("Machine core", **INVENTED**, game design). It replaces the hopper + furnace + bucket + redstone + copper groups in the Phase 2 and 3 machine recipes, so a recipe reads "iron, one core, the gate part". **Drop-only; not craftable.** Reason: the long Mars chain (v1 section 4) taught nothing about Mars. Tooltip: "Machine core. The pump, valves and controller in one box. Sent from orbit. Game design, not real." Needs one icon (section 8).
Everything else in a drop is a vanilla id or an existing `mars:` gate part, so only **one** new item is needed now. The planned gate parts (resin pack, sorbent bed, signal board, activation cell) become **drop-only** too (their craft recipes in `gate-input-items.md` are removed).

## 5. Drop table per gate (all counts INVENTED)
Existing caches stay (coil 4, cartridge 3, bearing 2); new lines are added to the same `cache` list.
| # | Gate | Sender | Drop contains | Unlocks (use) | Fallback (never hard-blocked) |
|---|---|---|---|---|---|
| 0 | Start | Lander stock | add to starter chest: 1 crafting table, 2 furnace, 2 bucket, 16 coal (fuel; owner item C, section 9) (existing 4 copper, 10 redstone, seeds stay) | Smelt hematite and dark sand, carry water | Furnaces and bucket: request pack (below). Bucket also craftable from 3 iron |
| 1 | Dome stable | Relay Orbiter | 4 heater coil (built) + 2 furnace + 16 coal | Outgasser (1 coil, no core); extra furnaces for Ovens | Coil: 2 iron. Furnace and coal: request pack |
| 2 | Phase 2 begins | Relay Orbiter | 3 filter cartridge (built) + 6 machine core + 2 bucket + 16 coal | Processor (1 core + 1 cartridge); emitters (1 core + 1 coil each) | Cartridge: craftable. Core: request pack |
| 3 | Thaw | Relay Orbiter | 2 pump bearing (built) + 4 machine core | Ice extractor (1 core + 1 bearing); 2 extractors, 2 emitters | Bearing: craftable. Core: request pack |
| 4 | Hydrosphere | Relay Orbiter | 4 resin pack + 2 machine core | Meltwater treatment (1 core + 1 pack), planter water | Core: request pack. Packs: request pack only (drop-only) |
| 5 | Phase 3 begins | Relay Orbiter | 4 sorbent bed + 4 machine core + 8 glass | Carbon scrubber (1 core + 2 beds), soil unit (1 core) | Beds and cores: request pack. Soil unit and kits never need a bed, so biomass can always progress |
| 6 | Breathable | Relay Orbiter | 5 signal board + 2 machine core | Beacon bench (1 board + 1 core); transmitter core (3 boards) | Request pack |
| 7 | Equilibrium | Relay Orbiter | 1 activation cell | Colonization beacon final assembly | Request pack (the cell is always in the refill) |
Sizing rule (INVENTED): enough for the first of each unlocked machine plus one or two spares. Core counts are a first guess until the Index rate rows for processor, emitter, extractor and scrubber exist (`balance-sheet.md`, R5): tune from those. The 12-year-old finishes the gate with spare parts; the 17-year-old who builds many emitters runs short and uses the request.

## 6. Fallback: the resupply request (minimal)
- **Item:** `mars:resupply_request` (**INVENTED**). Craft: shapeless **4 iron ingot + 1 heater coil** (Mars-made, Phase 1). Open from the start.
- **Use:** right-click. Consumes the item. The Relay Orbiter delivers a **refill pack** for the **latest gate the player has reached**: half of that gate's drop (rounded up), but always at least 1 of the gate's key part (core, bed, board or cell). Same cache code, same chat line.
- **Pass timer:** one request per player per 3 sols (**INVENTED**); text: "The orbiter passes every 3 sols." Honest label: the pass interval is game design. A used request in cool-down keeps the item and shows the time left.
- **Why no cap:** the cost (4 iron + coil) and the 3-sol wait make it a delay, not a grind source, and it never grants Index progress. A cap could hard-block a careless player.
- **Not chosen:** a timer re-drop (extra code and surprise items). Add only if playtest shows players do not find the request item.

## 7. Recipe changes for the Technical Lead (exact; gate part recipes unchanged)
| # | Machine / item | Now (built) | Change |
|---|---|---|---|
| 1 | `mars:machine_core` | none | New item, no recipe. Registered, tooltip, lang, icon |
| 2 | `mars:atmospheric_processor` | iron, copper, hopper, redstone, `filter_cartridge` | Replace copper + hopper + redstone with 1 `machine_core`. Keep iron count and the cartridge |
| 3 | `mars:super_greenhouse_emitter` | iron, hopper, bucket, redstone, `heater_coil` | Replace hopper + bucket + redstone with 1 `machine_core`. Keep iron and the coil |
| 4 | `mars:ice_extractor` | iron, hopper, `pump_bearing`, bucket, furnace | Replace hopper + bucket + furnace with 1 `machine_core`. Keep iron and the bearing |
| 5 | `mars:water_reclaimer`, `regolith_oven`, `regolith_outgasser`, battery, solar panel | iron, glass, copper, bucket, furnace, coil as built | **No change** (Phase 1, from starter chest, dark-sand glass and iron) |
| 6 | New machines (as they are built) | none | carbon scrubber: iron + 1 core + 2 sorbent bed; soil unit: iron + 1 core + rich-soil input as designed; meltwater treatment: iron + 1 core + 1 resin pack; beacon bench: iron + 1 core + 1 signal board; transmitter core: 3 signal board + 1 core; beacon: 1 activation cell as in `gate-input-items.md` |
| 7 | `mars:resin_pack`, `sorbent_bed`, `signal_board`, `activation_cell` | planned recipes | Items only; **no craft recipe** (drop-only) |
| 8 | `mars:resupply_request` | none | New item, shapeless 4 iron + 1 `heater_coil`; use handler calls the cache code with the refill list; per-player timestamp in `CacheLedger` (schema bump) |
| 9 | Gate JSON `cache` lists | coil 4; cartridge 3; bearing 2 | Add the section 5 lines. Add `refill` list per gate (half counts) or compute it. Starter chest: add table 1, furnace 2, bucket 2, coal 16 (if C accepted) |
| 10 | Chat | "Supply drop received: N x Item." plus `gate.supply.<id>` | Keep. Add `Sent from orbit` label to the supply note; new note text below. Add `grant.refill` line for requests |
| 11 | Tests | `GateInputItemsTest` cache amounts; "nothing uncraftable" | Update amounts; allow drop-only items (cores, packs, beds, boards, cell) in the "craftable" check; boot test: latch each gate, items arrive once; request item gives refill, cool-down holds, resupply never raises any Index row |

**Dropped from v1:** basalt fibre, basalt board, `planks` and `stone_crafting_materials` tag files, regolith brick item (M1 to M4). The filter cartridge and sorbent stand-ins keep `red_sandstone` and the brick stand-in as built; the brick question is only for the sorbent bed and now closed (the bed is a drop).
**Hull panels** (basalt fibre in `world-and-blocks.md` 3.6) are outside this ruling; flag for the Artist/Technical Lead only if they are built.

## 8. Effects on Artist and journal
**Artist (handoff):** new icons: machine core (a grey-gold box with a pipe stub; same band as invented machines), resupply request (a small beacon card or flare), and, now needed as drop items, resin pack, sorbent bed, signal board, activation cell (already planned). A small "Sent from orbit" corner mark on items from drops (a tiny arc-and-dot symbol, readable in greyscale). A tooltip line colour is not the only cue.
**Journal and tooltips (Instructional Designer checks CR-F9; no gas names):**
- Supply note per gate (12 to 16 words): "Relay Orbiter: parts released over your dome. The air and water are still yours to change."
- Journal entry "Sent from orbit" (grade 6-7): "Mars is far from Earth. Some parts are hard to make here, so the game sends them from a relay satellite. This is a story device, not real: real spacecraft do not resupply Mars like this. The parts are tools. Your machines are what change the planet." Goals: A1, B4.
- Item rows in the journal show a small tag "Sent from orbit" (game design) beside the item; the machine recipe card shows "machine core: sent from orbit".
- Request item tooltip: "Resupply request. Calls the orbiter for more parts. It passes every 3 sols. Game design, not real."

## 9. Open owner decisions
| # | Question | Type | Recommendation |
|---|---|---|---|
| A | Accept orbital resupply as the main answer, with the machine core and request item (section 4 to 6)? | Ill-structured (fun and learning vs. realism of supply from orbit) | Yes; the drops are labelled game design and carry no progress |
| B | Name only the Relay Orbiter (not the ISS)? | Well-structured | Yes; ISS cannot reach Mars. Mention it in the journal as the model only |
| C | **Furnace fuel.** Coal in drops (16 at start, gates 1, 2) or the Oven smelts instead, or a Mars fuel later | Ill-structured | Coal in drops now (INVENTED); revisit after playtest. Not run: how the Oven smelts is unchecked |
| D | Request cost (4 iron + 1 coil) and 3-sol timer | Well-structured (tune) | As written; tune after playtest |
| E | Drop-only gate parts (resin, bed, board, cell)? | Ill-structured (loses a craft step, gains simplicity) | Yes; the craft recipes taught no Mars science |

## 10. Walkthroughs (design walk, not real players)
- **12-year-old, no modpack experience:** reaches Dome stable, sees "Sent from orbit" in chat, opens the package, finds coils, furnaces, coal. At Phase 2 the recipe book shows "iron + machine core + filter cartridge" and the core is already in the pocket. Stuck risk: loses the cores; the journal says "Craft a resupply request" with the recipe (iron + coil), and the Monitor goal line can say so when the player holds no core.
- **Experienced 17-year-old:** builds many emitters, runs out of cores, crafts a request, waits up to 3 sols (while the Index keeps moving), learns that machine count matters more than parts. Cannot skip a gate: placement lock stays.

## 11. Proposals for `TEAM.md` and `DESIGN.md` (not edited)
- **Term:** *orbital resupply* = a labelled drop at a gate that delivers tech (parts, cores, fuel), never Index progress; *machine core* = the drop-only part inside Phase 2 and 3 machines; *resupply request* = the craftable fallback.
- **Decision Log:** "Orbital resupply is the main answer for parts Mars cannot make simply; no new tag files; vanilla recipes not edited. Rationale: owner 5b, fewer invented chains, tech not progress. Alternatives: v1 lander salvage plus basalt board (more code, more invented items), vanilla-only (hard block). Trade-off: less Mars-made tech, one more invented item. Revisit if playtest shows players see drops as free progress."
- **DESIGN.md Fantasy audit table:** add machine core, resupply request (INVENTED), Relay Orbiter drop (game design). Remove basalt board.

---
# Appendix A. SUPERSEDED v1 (2026-11-28 earlier): lander salvage and Mars path
Kept for reference only. Do not build. Superseded by sections 1 to 11 above (owner 5b).

### Ruling: recipe inputs on a planet with no wood and no cobblestone

**Owner:** Lead Game Designer · **Written:** 2026-11-28 · **Status:** ruling, awaiting owner nod on the three open items (section 6) · **Scope:** standard pace, solo; vanilla crafting stays; make-it-work rule (`world-and-blocks.md` 0.2); never hard-block. **Marks:** **INVENTED** = our number or item. **NOT VERIFIED** = not checked.
Serves: C2 and A1 (machine parts), pillar "pressure, not grind". Goal IDs unchanged.

#### 1. The problem
Mars has no trees and no cobblestone. Vanilla recipes the machines use need them: crafting table (planks), chest (planks), **hopper** (chest, so planks), **furnace** (`#stone_crafting_materials`, in vanilla only cobblestone, blackstone, cobbled deepslate). The Processor, Emitter, Extractor (and Compressor, Sabatier, soil unit) use a hopper; the Extractor also a furnace. Slice A also found the Outgasser had no recipe (fixed with the heater coil) and that "regolith brick" is not an item.

#### 2. The rule (one sentence)
**The lander gives you one of each part you need first. Mars lets you make the rest from rock you can mine. Vanilla recipes are never edited, only given more things they accept.**

Two layers, so nothing is hard-blocked:
1. **Lander salvage** (fixed small counts, section 3): a first copy of each blocked item, so a 12-year-old is never stuck before they find basalt.
2. **Mars path** (section 4): every one of those items stays craftable from basalt, iron and ice, through vanilla recipes plus two small labelled additions.

#### 3. Lander salvage (all counts INVENTED)
Granted by the existing systems: starter chest (once per world) and gate `cache` (once per player per gate per planet). Item names are vanilla ids; the Technical Lead confirms `GateCaches` accepts non-`mars:` ids (the starter chest does).

| Where | Adds | Why this much |
|---|---|---|
| Starter chest (now: 4 copper, 10 redstone, seeds) | 1 crafting table, 1 furnace, 1 bucket | Start crafting and smelting at once. Bucket is iron only, so one is a courtesy |
| Dome stable cache (4 heater coils) | nothing | The Outgasser needs no hopper or furnace |
| Phase 2 begins cache (3 filter cartridges) | 2 hoppers, 2 buckets | First Processor (1 hopper) plus first Emitter (1 hopper, 1 bucket). Spare bucket for water |
| Thaw cache (2 pump bearings) | 2 hoppers, 2 furnaces, 2 buckets | Exactly two Extractors (hopper, furnace, bucket, bearing each). Design: one just meets Equilibrium, two give margin |
| **Total** | **1 table, 3 furnaces, 5 buckets, 4 hoppers** | Covers the first of each machine. More is the Mars path |

Chat line reuses "Supply drop received: N x Item." Journal note (grade 6-7, US spelling): "Your lander carried a few spare parts. After these, you make your own."

#### 4. Mars path (nothing hard-blocked)
| # | Addition | What the Technical Lead builds | Label |
|---|---|---|---|
| M1 | **Furnace from basalt.** Add `minecraft:basalt` (and `mars:regolith_bricks` when it exists) to the item tag `minecraft:stone_crafting_materials` (`pack/data/minecraft/tags/item/stone_crafting_materials.json`, `"replace": false`). The vanilla furnace recipe is unchanged | One tag file. Also lets stone tools and other "stone" recipes use basalt | SIMPLIFIED: Mars rock stands in for cobblestone |
| M2 | **Basalt fibre** (`mars:basalt_fibre`): smelt 1 basalt in a furnace (1 to 1, vanilla smelting JSON) | New item, one recipe. Already planned in `world-and-blocks.md` 3.6 (hull panels) | Basalt fibre is a real material; use in this form **NOT VERIFIED** (SME to confirm) |
| M3 | **Basalt board** (`mars:basalt_board`): shapeless 1 basalt fibre gives 2 boards | New item, one recipe. Add it to the item tag `minecraft:planks` (`replace: false`). Then vanilla crafting table (4), chest (8), hopper (chest plus 5 iron) and sticks all work unchanged | **INVENTED**: "Game invention, not real. Stands in for wood." |
| M4 | **Regolith brick** (`mars:regolith_brick`): smelt 1 red sand in a furnace (as clay to brick); 4 bricks craft `mars:regolith_bricks` (block) | Replaces the red sandstone stand-in once built (section 5). Not needed for the first playtest | SIMPLIFIED |

**Cost check (all INVENTED):** a hopper made on Mars = 5 iron + 4 basalt fibre (4 basalt, 2 smelts). A furnace = 8 basalt. A crafting table = 2 fibre. Iron stays the bottleneck, as in the balance sheet; basalt is plentiful on the surface. Fuel for the furnace is open (section 6, item C).

**Tooltips (grade 6-7, US spelling):**
- Basalt board: "Basalt board. A flat board made from basalt fibre. Mars has no trees, so the game uses it where wood would go. Game invention, not real."
- Basalt fibre: "Basalt fibre. Rock melted and drawn into threads. Real engineers make it. The game makes it in a furnace."
- Lander parts (hopper, furnace and so on): no new tooltip; the supply note carries the label.

#### 5. Row name and the regolith brick stand-in
**Row name (ruling): keep "Surface water" on the Monitor row; keep "Hydrosphere" as the gate name; link them in text.** Reason: plain words on the screen, science word where the learner is asked to learn it (A2). The gate's detail line already says "Surface with liquid water". Add the pair once in the journal: "Surface water (the Hydrosphere)". The Monitor goal line reads "Hydrosphere: X / 10 %" and is shown directly under the "Surface water" row, so the two meet on one screen. Revisit if the first playtest shows a 12-year-old cannot say which row the gate watches.

**Regolith brick stand-in (ruling): keep `minecraft:red_sandstone` in the filter cartridge until M4 exists, then swap to `mars:regolith_brick` in the same commit.** Reason: red sandstone is craftable now from 4 red sand (the surface block), needs no new item, and reads as "pressed regolith". The sorbent bed (slice C) uses the brick item, since it must be fired and porous. Both stand-ins carry the SME note: a brick is not a real sorbent or filter.

**Soil ladder placement.** Ladder (plantable ground): raw regolith (red sand) -> washed regolith (4 red sand + water bucket, built) -> rich soil (soil unit, Phase 3) -> biocrust patch. Brick, red sandstone and basalt board sit **off the ladder**: they are building and part materials that branch from rung 0 (raw regolith, fired or pressed instead of washed) and are never plantable. Players learn "wet makes soil, heat makes brick" (A5, B5).

#### 6. Walkthroughs
- **12-year-old, no modpack experience:** opens the chest, sees table, furnace, bucket. Mines basalt, smelts fibre, makes boards. Gets stuck only on fuel (item C).
- **Experienced 17-year-old:** goes straight to "furnace from basalt", skips boards for the hopper if the lander parts suffice. Sees M1 as a tag, not a quest.

#### 7. Open owner decisions
| # | Question | Type | Recommendation |
|---|---|---|---|
| A | Accept the rule (lander salvage plus Mars path) and the counts in section 3? | Well-structured | Yes |
| B | Basalt board in the `minecraft:planks` tag (side effect: wood-style tools, doors, boats-from-boards where recipes use the tag)? | Ill-structured (vanilla feel vs. world consistency) | Yes; the owner rule is that vanilla crafting works. Alternative: a custom chest and hopper recipe only |
| C | **Furnace fuel.** No coal or wood on Mars; `world-and-blocks.md` 3.6 already says "needs power-free furnace fuel". Options: (1) lander coal, 8 in the starter chest, then a Mars fuel later; (2) the powered Oven smelts instead of burning fuel; (3) a labelled invented fuel from greenhouse crops. I did not check how the Oven smelts or what the mod already burns, so it is **not run** | Ill-structured (fun, science, cost) | (1) now (8 coal, INVENTED), decide (2) or (3) after first playtest |

#### 8. Technical Lead: recipe and data changes (exact)
1. Starter chest: add `minecraft:crafting_table` 1, `minecraft:furnace` 1, `minecraft:bucket` 1 (`pack/data/mars/terrain/starter_chest.json`). If owner picks C(1): `minecraft:coal` 8.
2. Gate caches (`pack/data/mars/gates/*.json`): Phase 2 begins add hopper 2, bucket 2; Thaw add hopper 2, furnace 2, bucket 2. Dome stable unchanged. Update `GateInputItemsTest` cache amounts.
3. New tag files: `data/minecraft/tags/item/stone_crafting_materials.json` (basalt) and `data/minecraft/tags/item/planks.json` (`mars:basalt_board`), both `"replace": false`.
4. New items and recipes M2 and M3 (M4 later). Boot test: a survival player with only mined basalt and iron can craft a furnace, a hopper and a crafting table (extend `noRecipeNeedsAnItemThatNothingCanMake`).
5. No existing recipe amount changes. Machine recipes keep hopper, furnace, bucket as they are.
6. The balance-sheet extractor row is now the built numbers. The data-vs-sheet test finds the row by the prefix `| Ice extractor`, so it still matches; run the test once.

#### 9. Proposals for `TEAM.md` and `DESIGN.md` (not edited)
- **Term:** *lander salvage* = fixed first copies of blocked vanilla items from the starter chest and gate caches; *Mars path* = the way to make the same item from Mars materials.
- **Decision Log:** "Vanilla recipes are never edited; Mars materials are added to vanilla tags (`stone_crafting_materials`, `planks`). Rationale: vanilla feel, no hard block. Alternatives: custom recipes (more code, breaks other mods), lander-only (hard block after the cache). Trade-off: boards in `planks` leak into wood-style tools. Revisit if playtest shows confusion."
- **DESIGN.md Fantasy audit table:** add basalt board (INVENTED) and basalt fibre (SIMPLIFIED, NOT VERIFIED).
