# Ruling: recipe inputs on a planet with no wood and no cobblestone

**Owner:** Lead Game Designer · **Written:** 2026-11-28 · **Status:** ruling, awaiting owner nod on the three open items (section 6) · **Scope:** standard pace, solo; vanilla crafting stays; make-it-work rule (`world-and-blocks.md` 0.2); never hard-block. **Marks:** **INVENTED** = our number or item. **NOT VERIFIED** = not checked.
Serves: C2 and A1 (machine parts), pillar "pressure, not grind". Goal IDs unchanged.

## 1. The problem
Mars has no trees and no cobblestone. Vanilla recipes the machines use need them: crafting table (planks), chest (planks), **hopper** (chest, so planks), **furnace** (`#stone_crafting_materials`, in vanilla only cobblestone, blackstone, cobbled deepslate). The Processor, Emitter, Extractor (and Compressor, Sabatier, soil unit) use a hopper; the Extractor also a furnace. Slice A also found the Outgasser had no recipe (fixed with the heater coil) and that "regolith brick" is not an item.

## 2. The rule (one sentence)
**The lander gives you one of each part you need first. Mars lets you make the rest from rock you can mine. Vanilla recipes are never edited, only given more things they accept.**

Two layers, so nothing is hard-blocked:
1. **Lander salvage** (fixed small counts, section 3): a first copy of each blocked item, so a 12-year-old is never stuck before they find basalt.
2. **Mars path** (section 4): every one of those items stays craftable from basalt, iron and ice, through vanilla recipes plus two small labelled additions.

## 3. Lander salvage (all counts INVENTED)
Granted by the existing systems: starter chest (once per world) and gate `cache` (once per player per gate per planet). Item names are vanilla ids; the Technical Lead confirms `GateCaches` accepts non-`mars:` ids (the starter chest does).

| Where | Adds | Why this much |
|---|---|---|
| Starter chest (now: 4 copper, 10 redstone, seeds) | 1 crafting table, 1 furnace, 1 bucket | Start crafting and smelting at once. Bucket is iron only, so one is a courtesy |
| Dome stable cache (4 heater coils) | nothing | The Outgasser needs no hopper or furnace |
| Phase 2 begins cache (3 filter cartridges) | 2 hoppers, 2 buckets | First Processor (1 hopper) plus first Emitter (1 hopper, 1 bucket). Spare bucket for water |
| Thaw cache (2 pump bearings) | 2 hoppers, 2 furnaces, 2 buckets | Exactly two Extractors (hopper, furnace, bucket, bearing each). Design: one just meets Equilibrium, two give margin |
| **Total** | **1 table, 3 furnaces, 5 buckets, 4 hoppers** | Covers the first of each machine. More is the Mars path |

Chat line reuses "Supply drop received: N x Item." Journal note (grade 6-7, US spelling): "Your lander carried a few spare parts. After these, you make your own."

## 4. Mars path (nothing hard-blocked)
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

## 5. Row name and the regolith brick stand-in
**Row name (ruling): keep "Surface water" on the Monitor row; keep "Hydrosphere" as the gate name; link them in text.** Reason: plain words on the screen, science word where the learner is asked to learn it (A2). The gate's detail line already says "Surface with liquid water". Add the pair once in the journal: "Surface water (the Hydrosphere)". The Monitor goal line reads "Hydrosphere: X / 10 %" and is shown directly under the "Surface water" row, so the two meet on one screen. Revisit if the first playtest shows a 12-year-old cannot say which row the gate watches.

**Regolith brick stand-in (ruling): keep `minecraft:red_sandstone` in the filter cartridge until M4 exists, then swap to `mars:regolith_brick` in the same commit.** Reason: red sandstone is craftable now from 4 red sand (the surface block), needs no new item, and reads as "pressed regolith". The sorbent bed (slice C) uses the brick item, since it must be fired and porous. Both stand-ins carry the SME note: a brick is not a real sorbent or filter.

**Soil ladder placement.** Ladder (plantable ground): raw regolith (red sand) -> washed regolith (4 red sand + water bucket, built) -> rich soil (soil unit, Phase 3) -> biocrust patch. Brick, red sandstone and basalt board sit **off the ladder**: they are building and part materials that branch from rung 0 (raw regolith, fired or pressed instead of washed) and are never plantable. Players learn "wet makes soil, heat makes brick" (A5, B5).

## 6. Walkthroughs
- **12-year-old, no modpack experience:** opens the chest, sees table, furnace, bucket. Mines basalt, smelts fibre, makes boards. Gets stuck only on fuel (item C).
- **Experienced 17-year-old:** goes straight to "furnace from basalt", skips boards for the hopper if the lander parts suffice. Sees M1 as a tag, not a quest.

## 7. Open owner decisions
| # | Question | Type | Recommendation |
|---|---|---|---|
| A | Accept the rule (lander salvage plus Mars path) and the counts in section 3? | Well-structured | Yes |
| B | Basalt board in the `minecraft:planks` tag (side effect: wood-style tools, doors, boats-from-boards where recipes use the tag)? | Ill-structured (vanilla feel vs. world consistency) | Yes; the owner rule is that vanilla crafting works. Alternative: a custom chest and hopper recipe only |
| C | **Furnace fuel.** No coal or wood on Mars; `world-and-blocks.md` 3.6 already says "needs power-free furnace fuel". Options: (1) lander coal, 8 in the starter chest, then a Mars fuel later; (2) the powered Oven smelts instead of burning fuel; (3) a labelled invented fuel from greenhouse crops. I did not check how the Oven smelts or what the mod already burns, so it is **not run** | Ill-structured (fun, science, cost) | (1) now (8 coal, INVENTED), decide (2) or (3) after first playtest |

## 8. Technical Lead: recipe and data changes (exact)
1. Starter chest: add `minecraft:crafting_table` 1, `minecraft:furnace` 1, `minecraft:bucket` 1 (`pack/data/mars/terrain/starter_chest.json`). If owner picks C(1): `minecraft:coal` 8.
2. Gate caches (`pack/data/mars/gates/*.json`): Phase 2 begins add hopper 2, bucket 2; Thaw add hopper 2, furnace 2, bucket 2. Dome stable unchanged. Update `GateInputItemsTest` cache amounts.
3. New tag files: `data/minecraft/tags/item/stone_crafting_materials.json` (basalt) and `data/minecraft/tags/item/planks.json` (`mars:basalt_board`), both `"replace": false`.
4. New items and recipes M2 and M3 (M4 later). Boot test: a survival player with only mined basalt and iron can craft a furnace, a hopper and a crafting table (extend `noRecipeNeedsAnItemThatNothingCanMake`).
5. No existing recipe amount changes. Machine recipes keep hopper, furnace, bucket as they are.
6. The balance-sheet extractor row is now the built numbers. The data-vs-sheet test finds the row by the prefix `| Ice extractor`, so it still matches; run the test once.

## 9. Proposals for `TEAM.md` and `DESIGN.md` (not edited)
- **Term:** *lander salvage* = fixed first copies of blocked vanilla items from the starter chest and gate caches; *Mars path* = the way to make the same item from Mars materials.
- **Decision Log:** "Vanilla recipes are never edited; Mars materials are added to vanilla tags (`stone_crafting_materials`, `planks`). Rationale: vanilla feel, no hard block. Alternatives: custom recipes (more code, breaks other mods), lander-only (hard block after the cache). Trade-off: boards in `planks` leak into wood-style tools. Revisit if playtest shows confusion."
- **DESIGN.md Fantasy audit table:** add basalt board (INVENTED) and basalt fibre (SIMPLIFIED, NOT VERIFIED).
