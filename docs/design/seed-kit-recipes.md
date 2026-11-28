# Seed kit recipes, starter chest check, band wording

**Owner:** Lead Game Designer · **Written:** 2026-11-28 · **Status:** PROPOSAL (nothing built; recipes are not in the pack yet) · **Reads:** `world-and-blocks.md` 0.2, 3.6; `machine-priorities.md` R1 to R8, D1 to D4; `docs/science/mars-conductor-and-carbonate-ores.md`; `docs/team/reports/2026-11-28-technical-lead-conductor-ores.md`; `docs/art/made-up-band-spec.md`.
**Marks:** *INVENTED* = our balance number. **unverified** = not checked. Every number below is INVENTED unless marked sourced. Item ids are proposals.

## 1. The four kit items

Rules: vanilla ingredients first (3.6 rule 1); every ingredient can exist on Mars by the time the kit unlocks (greenhouse crop, compost, vein or scrubber carbonate, ice water, washed regolith); **one carbonate and one bone meal per kit** (confirms the Technical Lead's question); each kit yields 1 kit (*INVENTED*). Recipes unlock at **Phase 3 begins** (DESIGN.md gate table). Shapeless crafting, any crafting table. The Technical Lead writes the JSON; carbonate is always the tag `#mars:seed_kit_carbonate` (today only `mars:carbonate`; gypsum fallback stays in data, `terrain/conductor_ores.json`).

| # | Item (proposed id) | Ingredients (INVENTED amounts) | Mars source of each | Serves (Index / gate) | Placement rule (chain) | Goals |
|---|---|---|---|---|---|---|
| 1 | Biocrust kit (`mars:biocrust_kit`) | 1 `#mars:seed_kit_carbonate`, 1 `minecraft:bone_meal`, 2 washed regolith (`mars:washed_regolith`, planned), 1 `minecraft:water_bucket` (bucket returned) | vein or scrubber; compost; wash station; ice | **Biomass** row, pioneer patch; toward **Breathable** (CO2 down, O2 up) | Only on soil made by the soil treatment unit | A5, B2, B4 |
| 2 | Tundra meadow kit (`mars:meadow_kit`) | 1 carbonate tag, 1 bone meal, 3 `minecraft:wheat_seeds` (stand-in for grasses and sedges) | greenhouse wheat | Biomass, second step | Only next to biocrust soil | A5, B2 |
| 3 | Wetland margin kit (`mars:wetland_kit`) | 1 carbonate tag, 1 bone meal, 2 `minecraft:sugar_cane` (stand-in for reeds), 1 water bucket (returned) | greenhouse cane; ice water | Biomass, water-edge patch | Only beside surface water (needs Hydrosphere, already passed) and on biocrust soil | A5, B2 |
| 4 | Cold forest kit (`mars:forest_kit`) | 1 carbonate tag, 1 bone meal, 2 `minecraft:sweet_berries` + 2 wheat seeds (stand-in for shrubs and small trees) | greenhouse | Biomass, last and costliest plant step | Only on meadow soil and where temperature is at least 5 C (*invented* game line, DESIGN.md) | A5, B2, B4 |

Each kit seeds a 3 x 3 patch (9 BU, balance sheet unit "1 BU = one established flora block"); the patch spreads on treated soil (W3 behaviour). *INVENTED*; tune with the balance sheet (biomass target 1,500 BU, self-sustaining patch 150 BU).

**Honesty.** The stand-in plants are Minecraft crops, not real Mars biocrust organisms. Tooltip says "Game invention, not real" (wording A). Journal: "On Earth this takes hundreds of years. The game speeds it up." (DESIGN.md). Real note: all four crop types have been grown for space farming (DESIGN.md Content Lists, SME round 3).

**Forecast guard (CR-F9).** F8 (`forecast.f8.flora_gases`) fires when the first kit is placed and asks what happens to O2 and CO2. Kit tooltips and recipe-book text are visible before that, so they **must not name O2, CO2, or any gas change**. The proposed tooltips in section 5 follow this. `reveals: none` by wording; the Technical Lead's text scan should still list `forecast.f8` as guarded for any kit text.

### Why plain 1 + 1 + plants, not a dispenser
R7 cut the dispenser. Kits are hand-crafted; the plant ingredient grows in cost along the chain (3 seeds, 2 cane, 4 plant items), so the later kits feel earned without more rare ore.

## 2. Loop check: "the soil unit needs bone meal" against the compost note

Chain (SME `mars-conductor-and-carbonate-ores.md` section 5): Phase 1 planter -> crops -> composter -> bone meal -> soil unit and kits. The SME's warning: the soil unit also needs bone meal, so the **first** planter must work without it.

| Check | Result |
|---|---|
| Bootstrap deadlock? | **No, if** the Phase 1 planter bed works on washed regolith with no bone meal. Today `world-and-blocks.md` 3.6 (row "Planter bed") says "then bone meal or compost". **Fix proposed (applied to that row):** bone meal is optional and only raises yield. Washed regolith + water is enough |
| Crop seeds on a planet with no grass | **Gap.** Wheat seeds come only from harvesting wheat; nothing on Mars drops them. Proposal: the Lander Supply Chest also holds a "seed packet" (see section 3). Tech Lead to add (handoff) |
| Bone meal supply vs. demand | Demand for a plan of 24 kits (6 + 8 + 6 + 4, *INVENTED*) plus 3 soil units = **27 bone meal**. Vanilla composter: about 7 successful adds fill it; crop item chances (wheat 65%, seeds 30%, cane 50%, berries 30%; from memory, **unverified**) average about 50%, so about 14 plant items per bone meal. 27 bone meal = about **380 plant items**. A wheat plant gives about 2.5 items (1 wheat + about 1.5 seeds, **unverified**) = about **150 plant harvests**, or about 15 harvests of a 10-tile bed. At one harvest per half sol that is about 7 sols (about 2.5 h at 20 min per sol, minus growth time). Fits inside the 14 h Phase 3 and the 8 h Phase 1 greenhouse head start |
| Bone meal also speeds crops | Players who spend bone meal on growth starve the kits. Journal line and the Monitor goal list say "kits need bone meal"; no rule change |
| Carbonate supply vs. demand | 24 kits = **24 carbonate**. SME placement: about 3 patches of 4 to 8 per 256 x 256 = 12 to 24 per area, but Tech Lead notes density is **unmeasured** (probably lower). Scrubber output (also `mars:carbonate`) covers the rest, which ties kits to the scrubber trade-off (rule A). Mining stays small |
| Classroom mode (about 11 to 12 h total) | Kit count scales with rates, not thresholds. Suggest 12 kits (3 + 4 + 3 + 2): 12 carbonate, 12 bone meal. **INVENTED**; check against the classroom balance run |

**Verdict:** the loop works if (1) the first planter needs no bone meal, (2) a seed packet exists, (3) carbonate density is measured by a walk. Journal label for the compost step: `journal.compost.bone_meal` (phase-1.md section 12).

## 3. Starter chest: confirm or adjust (4 copper, 8 redstone)

Costs below are **INVENTED** (3.6 names the ingredients but not the counts). Copper and redstone come from rare ore (`sulfide_vein`, `magnetic_ore`). Vanilla recipes add hidden redstone: a compass is 4 iron + 1 redstone (so the scanner costs 2); a redstone lamp needs glowstone, which Mars lacks, so the grow lamp uses 1 redstone + glass + iron (*INVENTED*, replaces "glowstone dust or redstone lamp" in 3.6; handoff).

| Machine (phase) | Copper | Redstone | On the gate path? | Notes |
|---|---|---|---|---|
| Compressor intake (1) | 0 | 1 | Yes (CO2 canister feeds the oxygen maker) | |
| Oxygen maker x2 (1) | 2 (1 each, in the circuit) | 4 (circuit 1 + 1 each) | Yes (Dome stable, real life support) | One unit does not keep a crew alive (3.6); 2 is *INVENTED* |
| Battery x2 (1) | 0 | 2 | Yes (grid) | Existing machine; recipe "iron + redstone" |
| **Gate-critical total, Phase 1** | **2** | **7** | | |
| Grow lamp x3 (1) | 0 | 3 | No, but food is C1 | |
| Survey scanner (1) | 0 | 2 | No (finds ice; cut candidate) | |
| Electrolyser (1 late / 2) | 2 | 1 | No | |
| Sabatier reactor (2) | 1 | 0 | No | |
| Water reclaimer (1) | 0 | 0 | Yes | No recipe in 3.6; keep iron + glass + hopper, no conductor |
| Phase 2 and 3 machines (processor, emitter, extractor, scrubber, soil unit) | not defined | not defined | Yes | Recipes do not exist yet; if they use circuits they need ore |

| Sum | Copper | Redstone |
|---|---|---|
| Critical path | 2 | 7 |
| + greenhouse lamps | 2 | 10 |
| + scanner | 2 | 12 |
| + electrolyser and Sabatier | 5 | 13 |
| **Chest today** | **4** | **8** |

**Verdict.**
- **Copper 4: right.** It covers both oxygen makers and the electrolyser; the Sabatier and everything later needs a mined vein. That is the exploring pressure we want (pillar 4), without a hard stop in Phase 1.
- **Redstone 8: slightly small.** It covers the gate path (7) with 1 spare, but a 12-year-old who makes one compass or two lamps by mistake is stuck before they have any reason to find a magnetic ore (it only spawns in `highland_mesa`, density unmeasured). **Proposal: 4 copper, 10 redstone.** 10 covers the gate path and the greenhouse lamps (food, C1); the scanner and later machines need ore on purpose.
- Re-check after the Tech Lead's walk counts veins. If veins are too rare, raise the chest, not the vein rate (chest buys time; rate fixes grind).
- **Seed packet (new, INVENTED):** 6 wheat seeds, 2 sugar cane, 2 sweet berries, 2 potatoes, in the same chest. Without it the greenhouse and the kits cannot start. Label: "from the lander".

## 4. R8: one band, two wordings (resolved, with one flag)

| Wording | Tooltip line | Machines |
|---|---|---|
| A | **"Game invention, not real."** | Outgasser, super-greenhouse emitter, atmospheric processor, soil treatment unit, seed kits, mirror uplink, insulation shroud, meltwater treatment, quiet-zone marker, lure station, survey scanner, beacon (3.5) |
| B | **"Game-sized version of a real idea."** | Oven, ice extractor, water reclaimer, oxygen maker, electrolyser, Sabatier reactor, carbon scrubber (3.5) |

- **One band** (purple dashes) on every machine that carries A or B; the tooltip says which. Shape (dashes) carries the meaning, not hue. A short real reference may follow in the tooltip when `docs/science/machine-references.md` has one.
- Machines that are what they are (solar panel, battery, dust-off station, weather mast) carry **no wording and no band**.
- **Flag for the Artist:** `made-up-band-spec.md` says "real hardware (MOXIE-style Oxygen maker) never gets" the band, but 3.5 lists the oxygen maker (and electrolyser, Sabatier) under wording B, and D2 said "band on all". Proposal: B machines wear the band; the oxygen maker is the one finished asset that needs a small retrofit. If the Artist prefers the exemption, then oxygen maker, electrolyser and Sabatier take a **third** wording ("Based on real hardware") and no band. Either is fine; the owner picks if they disagree. Both keep the honesty rule.

## 5. Kit tooltips (proposed text)

Grade 6 to 7, US spelling, positive, no gas names (F8 guard). Keys `item.mars.<id>.tooltip`.

| Key | Text |
|---|---|
| `item.mars.biocrust_kit.tooltip` | Biocrust kit. Seeds a small patch of living crust. Needs treated soil. Game invention, not real. |
| `item.mars.meadow_kit.tooltip` | Tundra meadow kit. Seeds mosses and grasses. Needs soil that biocrust made. Game invention, not real. |
| `item.mars.wetland_kit.tooltip` | Wetland margin kit. Seeds reeds beside water. Needs soil that biocrust made. Game invention, not real. |
| `item.mars.forest_kit.tooltip` | Cold forest kit. Seeds small trees and shrubs. Needs meadow soil and warm enough air. Game invention, not real. |

## 6. Walkthroughs

- **12-year-old, no modpack experience:** reaches Phase 3 begins, sees four kit recipes appear. Biocrust kit asks for stuff they already have (carbonate from the vein they mined, bone meal from the greenhouse compost, washed regolith, water). The tooltip says it needs treated soil; the Monitor goal row points to the soil unit. Risk: they spent bone meal on growing crops and have none; the journal line about compost is the signal. Risk: they never learned to compost. Mitigation: the compost label fires on the first composter bone meal; Hana's tip (ID) if no bone meal by Phase 3 begins.
- **Experienced 17-year-old:** automates the compost loop with hoppers within minutes, sees that 24 kits cost little, and goes straight to the chain. Pressure comes from the placement rules and soil, not from ingredients. If kits feel free, raise the plant counts first (one number each), not carbonate.

## 7. Open items

| Item | Owner |
|---|---|
| Seed packet and chest redstone 10 (section 3) | Technical Lead (JSON), owner nod |
| `mars:washed_regolith` item exists? | Technical Lead |
| Real glow replacement for the grow lamp (no glowstone on Mars) | Game Designer proposes (done above); Technical Lead wires |
| Vanilla composter chances and wheat yield (section 2) | **unverified**; Technical Lead can read the vanilla data |
| Carbonate density per 256 x 256 | Technical Lead walk |
