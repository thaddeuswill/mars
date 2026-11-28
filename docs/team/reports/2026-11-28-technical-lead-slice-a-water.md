# Technical Lead report: slice A, water (Hydrosphere, ice extractor, gate input items)

Date 2026-11-28 · BACKLOG #49 · Branch `claude/elegant-brown-anu1ci` · Code commit `ac22386`

## 0. Handoffs received
- Design docs `phase3-hydrosphere-and-drift.md` (slice A), `gate-input-items.md` (slices A and the bearing of B), `invented-machines-look.md`, owner rules (standard pace only, INVENTED labels, vanilla crafting stays, never hard-block). **Accepted**, with the deviations listed in section 4.
- Lead Artist's ice extractor models and textures: used as they are (not edited). Their files were uncommitted in the working tree when I started; they landed in `5fe7fbe` while I worked. I wrote only the blockstate.

## 1. Done (all run, real output)
| Item | Result |
|---|---|
| Hydrosphere parameter | `pack/data/mars/parameters/hydrosphere.json`: restore loss to 0, k 0.01 per sol (source field says INVENTED, direction real, NOT VERIFIED), range 0 to 100. Reuses `ParameterDef.Loss`. |
| Machine condition in data | New optional `requires` in a machine file: a gate-style condition (parameter comparison only; machine-state names rejected). `MachineDef.requires()`, `PlanetSim.applyPower` multiplies by 1/0, `PlanetSim.machineRequirementMet`. No pace field can scale it. |
| `ice_extractor` | `GatedLoadBlock` (facing, machine_state, loot, lang with 'Game-sized version of a real idea', Rodwell/NTRS 20205011353 in the machine file source). +0.30 %/sol, 2 units/sol (INVENTED), `requires temperature >= -20`. Blockstate uses the Artist's `ice_extractor_off/_idle/_running`. Not placed on ground ice only (see 4). |
| Look | `MachineLook.withRequirement`: too cold shows IDLE, never RUNNING. Monitor line says "too cold". |
| Gates | `gates/hydrosphere.json` (order 4, >= 10, no hold, `unlocks` empty: `meltwater_treatment` is not a block). `thaw.json` unlocks `mars:ice_extractor`. Counts now "N / 7"; tests updated. |
| Monitor row | "Surface water" (`monitor.row.hydrosphere`). **Decision: shown once the Thaw has latched OR whenever the value is above 0.** Reason: design says the row comes with the Thaw, but an admin value or old save must never be hidden. Trend, net rate, loss line "Water freezes or sinks back." Cause line for the extractor shows a reason only ("too cold", "needs power"), like every machine line on screen. No time estimate for water. |
| Items | `heater_coil`, `filter_cartridge`, `pump_bearing` (the bearing is needed by the extractor recipe, so it came with slice A): registered with tooltips, vanilla-only recipes (2 iron -> coil; 2 glass pane + red sandstone + iron nugget -> cartridge; cross of 4 nuggets + ingot -> 2 bearings), placeholder icons from `tools/art-preview/gen_gate_item_textures.py` (none existed; never overwrites), listed in `docs/art/w1-placeholder-textures.md`. |
| Caches | `cache` field in gate JSON: Dome stable 4 coils, Phase 2 begins 3 cartridges, Thaw 2 bearings. `GateCaches` gives them to the inventory (drops if full) on latch to every player on the planet and on login or dimension change, once per player per gate per planet (`CacheLedger` pure, `CacheData` saved in the planet's data folder). Two chat lines: "Supply drop received: N x Item." and a supply note (`gate.supply.<id>`, 12 to 16 words, no pressure/temperature/gas words). |
| Recipes | **The Outgasser had no recipe at all** (survival players could not get one). Added: 8 iron ingots around a heater coil (INVENTED). Processor: cartridge replaces the glass. Emitter: a coil joins the top row. Extractor: iron, hopper, bearing, bucket, furnace. All ingredients are vanilla or one of the three parts, each with its own open recipe. Test `noRecipeNeedsAnItemThatNothingCanMake`. |

Command output (from `mod/`, after the final code change):
- `./gradlew spotlessApply build runGameTestServer`: **BUILD SUCCESSFUL, 379 unit tests, 0 failures** (baseline 355); **`MARS-SELFTEST RESULT PASS passed=165 failed=0`** (baseline 152). The 13 new boot checks are `water.*` (7) and `cache.*` (6) in `WaterGameTests`; the existing `gate.files_loaded` check now expects 7 gates.
- Unit tests added (24): `IceExtractorTest` (13: data vs balance sheet incl. rate, draw, -20 C and k; only powered and at or above -20 C; condition exactly -20 and not scaled by classroom; loss restores toward 0 at e^-0.01 a sol; one extractor settles at 30 (+-0.4), two at 60; Hydrosphere gate latches at 10, no hold, locks in, unscaled; Thaw unlocks it and every unlocked id is a built machine; look; pause freezes the extractor and the loss; Monitor row visibility, trend, causes and "too cold"; next-goal row), `GateInputItemsTest` (7: cache amounts, recipes vanilla-only, parts used by machine recipes, nothing uncraftable, block registered everywhere, wording scan: 20 words, no `~`, no give-away words), `CacheLedgerTest` (2). Existing tests changed: goal counts to 7, Thaw to Phase 3 test now passes the Hydrosphere gate, Equilibrium hold test pins hydrosphere at 31 (the loss term takes 0.03 per 0.1 sol), one tolerance for the new loss, vanilla-recipe test also allows the two parts, tooltip test lists the extractor.

## 2. What I saw (real client, software GL; script `tools/screenshots/slice_a_water.shots`, 1280x720)
Run dir `tools/screenshots/out/20261128-141443/`; no other client was running (`pgrep` empty); PNG timestamps 14:15:17 to 14:15:40 in `mod/run-shots/screenshots`, identical to the copies; `client.log` has 0 'Unknown block'.
- `cache_dome_stable.png`: chat "Goal reached: Dome stable", "You can now build: Regolith Outgasser", "Supply drop received: 4 x Heater Coil.", the supply note; hotbar shows the coils (placeholder icon, orange coil) with count 4.
- `cache_all_three.png`: all three supply drops in chat; hotbar shows 4 coils, 3 cartridges (pale drum), 2 bearings (ring).
- `extractors_running.png` and `extractors_too_cold_idle.png`: two extractors beside a battery and a panel; the Artist's model renders (spindly tower, east pump box, made-up band). The two frames differ in the sleeve (running has the orange glow band, idle is plain). Stills only: I did not see the animation.
- `monitor_surface_water.png`: Temperature -13 C, **Surface water 6.2 % Rising +0.54 %/sol**, "Water freezes or sinks back.", Next goal **Hydrosphere 6.23 / 10 %** with the bar. Panel fits.
- `monitor_too_cold.png`: at -32 C the row reads Falling -0.06 %/sol with "Ice Extractor x2 ... too cold".
(First attempts were dark because I built the pad in a pit under the terrain; fixed by using the real pad, y 81.)

## 3. Decisions needed
1. **Row naming (ill-structured, small).** The row says "Surface water", the gate says "Hydrosphere". A 12-year-old may not link them. Options: rename the row "Hydrosphere" (matches the gate and the Index vocabulary), keep both, or rename the gate. Recommendation: keep "Surface water" on the row (plain words) and add the word to the gate detail line ("Surface with liquid water" is already there). Owner or Instructional Designer.
2. **Regolith brick (well-structured).** Design says the filter cartridge needs a regolith brick; no such item exists. I used `minecraft:red_sandstone` (4 red sand, pressed). Alternative: build a smelted regolith brick item. Recommendation: keep red sandstone until the brick exists.
3. **Hopper and furnace need wood or cobble (flag, not mine to fix).** Processor, emitter and extractor recipes use a hopper (needs a chest, so planks) and the extractor a furnace (cobblestone); Mars has no trees. Pre-existing for the hopper. Needs a Game Designer ruling on the vanilla-crafting rule versus Mars-minable inputs (R1 in machine-priorities.md).

## 4. Deviations and gaps (honest list)
- **Ground-ice placement rule deferred**, as slice A says: the extractor places on any block.
- Outgasser recipe is an addition (it had none). The design only said "gains 1 coil".
- Design-doc "regolith brick": red sandstone (above). Pump bearing wear, filter clogging and coil wear are not built (intervals open).
- The Monitor does not show the extractor rate while it runs (screen shows reasons only, by existing design); the rate is in the row's net rate.
- The Hydrosphere gate unlocks nothing: `meltwater_treatment` does not exist. Listed as open.
- Balance-sheet row still says "Ice extractor (proposal)"; my test matches that text. If the Game Designer renames the row, update `IceExtractorTest.dataMatchesTheBalanceSheet`.
- Old saves: `PlanetState` schema unchanged (hydrosphere was already a stored value; the missing gate is added on load). Old worlds that already latched gates get their caches on next login.
- Not run: dedicated server, a real key or mouse path (the script uses commands), animation, colour-vision checks.

## 5. Handoffs
> **To:** Lead Game Designer · **Problem type:** well-structured
> **Problem:** hopper/furnace and "regolith brick" inputs are not Mars-minable; the balance-sheet extractor row is still "(proposal)".
> **Context:** `pack/data/mars/recipe/*.json`, `docs/design/balance-sheet.md` line 202.
> **Needed from them:** a ruling (keep vanilla crafting items, or define a Mars-minable stand-in) and update the sheet row.
> **Done when:** recipes and sheet agree.

> **To:** Instructional Designer · **Problem type:** well-structured
> **Problem:** alignment and wording check of the new learner-visible text: `monitor.row.hydrosphere`, `monitor.cause.hydrosphere_loss`, `monitor.cause.too_cold`, `gate.supply.*`, `grant.cache`, three item tooltips (all in `pack/assets/mars/lang/en_us.json`). No goal IDs were changed.
> **Done when:** wording is confirmed or replaced.

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** replace the three placeholder item icons (`heater_coil`, `filter_cartridge`, `pump_bearing`); four more gate items still need icons.
> **Done when:** real PNGs exist under the same names.

## 6. Risks
- Balance is unplayed: first playtest should measure sols between the Thaw and the Hydrosphere gate (design target 10 to 14 sols with 2 to 4 extractors).
- The Dome-stable cache gives 4 coils; the Outgasser takes 1, each emitter 1. A player who builds 4 emitters runs short and must craft coils (2 iron each): by design, not a hard block.
- Equilibrium needs hydrosphere >= 30 and the loss makes one extractor settle at exactly 30 (0.30/0.01): it can only reach 30 in the limit, so two extractors are needed in practice. This matches the design ("one just meets, two give the margin") but is worth a playtest note.

## 7. Shared model updates
- **New term:** *machine condition* (`requires`): a machine's own data-driven gate-style condition; the machine contributes only while it holds (the ice extractor needs temperature >= -20 C). Owning document: `ARCHITECTURE.md` 2.23h.
- **New term:** *supply cache*: the parts a gate hands each player once per planet when it latches. Owning document: `docs/design/gate-input-items.md`, `ARCHITECTURE.md` 2.23h.
- **Lesson:** a screenshot pad built below the terrain surface is dark (no skylight); use the real pad (top block y 80) for world shots.
- **Decision recorded (Technical Lead, within scope):** the Surface water row shows from the Thaw or when above 0.
