# Life-support spec for Stage 3 slices 1 and 2 (machine-readable)

**Owner:** Lead Game Designer (numbers) · **Reader:** Technical Lead · **Written:** 2026-11-27 · **Docs only; nothing built or run.**
**Source of numbers:** [balance-sheet.md](../design/balance-sheet.md) section 2d (**owner-accepted** 2026-11-27: O2 use 8, maker 10, start fill 14, reserve 24). **If this file and the balance sheet differ, the balance sheet wins; report the difference.** Layout: [dome-layout.md](../design/dome-layout.md). Science tags: [life-support-review.md](../science/life-support-review.md).
**Status (Technical Lead, 2026-11-27): CONFIRMED and built in slice 2.** S1 cap 2,000 kept. S2 full block or nothing kept (`mars.hull.Airtight`). Differences from this file: the gate key is the existing `all` (not `all_of`); water tank capacity is a fixed 120 (no water tank blocks); O2 tank capacity is tanks x 40 capped at 200; `life_support.json` also holds `landingEndSol` 1.2 and `blackoutWakeSuitO2`; recipes are in `data/mars/recipe_costs.json` and only hull plate, tank and door are crafting-table recipes (grid limit 9). See the slice 2 report.
**Status of file formats:** the key names below are **proposals** that follow the existing files in `pack/data/mars/machines/` (`schema`, `id`, `contributions`, `power`, `source`). The Technical Lead may rename keys; keep the values and the "a unit test compares data to the sheet" rule.

Units: 1 O2 unit = 100 g; 1 water unit = 1 L; 1 power unit = 1 kWh; per sol = per simulated sol (20 simulation minutes). All values **invented** (game balance) unless the balance sheet tags them otherwise.

## 1. Slice 1: sealing and the dome

### 1.1 Decisions proposed (Technical Lead confirms or changes; **ill-structured part flagged**)

| # | Question | Proposal | Why | Cost if wrong |
|---|---|---|---|---|
| S1 | Flood-fill volume cap | **2,000 blocks** (`sealing.volumeCap`). The starting dome is about 700 blocks (dome-layout 3), so the cap is 2.8 times that: room for Phase 2 rooms and a tube stub. Hitting the cap or open sky = not sealed | ARCHITECTURE 2.3 says "cap"; dome-layout 5.3 proposed 2,000 | A cap too low makes a big legal build "unsealed" (feels like a bug). Too high costs ticks: spread the fill over several ticks and cache per volume (ARCHITECTURE 11 budget row "Up to the volume cap, spread over several ticks") |
| S2 | Partial blocks (slabs, stairs, fences, panes, trapdoors, plants) | **Not airtight in survival.** Airtight = a full-cube block with tag `mars:airtight` (hull plate, hull glass, closed door or hatch, machine casings, natural terrain). One rule: "a full block or nothing" | Easy to say to a 12-year-old, easy to test, and the Monitor can name the gap. Vanilla partial shapes have both full and open faces, so face-level rules need per-state shape checks and are hard to explain | Builders who like slab walls hit a surprise. Mitigation: the "Unsealed" report names the block position of the leak (see 1.3), so the cause is visible. Revisit if a playtest shows players fight the rule |
| S3 | Which volumes must be sealed | A volume that contains a bunk, wall tank, oxygen maker, reclaimer, battery or the Monitor. Hab B and Storage are not required (dome-layout 5.4) | A cracked, unneeded room does not end the game | none |
| S4 | When the clock starts | Life support (use, leak, Blackout) starts at the end of Landing (t0 = sol 1.2); Landing is safe (dome-layout 5.8) | Teaching first | none |

Assumption to challenge: that `mars:airtight` as a block tag is cheap to maintain when other mods add blocks. Fallback: default-deny (only listed blocks airtight).

### 1.2 Data files

| File | Keys and values |
|---|---|
| `data/mars/sealing/rules.json` (new) | `schema` 1; `volumeCap` **2000**; `airtightTag` `"mars:airtight"`; `requiredContents` list of machine ids and block ids: `mars:oxygen_maker`, `mars:water_reclaimer`, `mars:o2_tank`, `mars:bunk`, `mars:battery`, `mars:monitor`; `partialBlocksAirtight` **false**; `breachEventOncePerVolume` true; `source` |
| `data/mars/tags/block/airtight.json` (new; vanilla tag format) | `values`: `mars:hull_plate`, `mars:hull_glass`, `mars:airlock_door` (closed), plus machine casings listed in section 2 |

### 1.3 Sealed state sync (client and HUD)

Per required volume, the server sets `sealed` true or false and syncs one boolean per planet plus, when false, one block position of a boundary hole (for the failure report). The HUD chip "Unsealed" is icon plus word. The flood fill re-runs only for the volume touched by a block change; the open-door rule is dome-layout 5.5.

## 2. Slice 2: machines, tanks, recipes, gate

### 2.1 `data/mars/life_support.json` (new)

All values per balance-sheet 2d.1 and 2d.2. "Scales with n" means the code multiplies by `group.size` (config, never who is online; balance sheet 5.1).

| Key | Value | Scales with `group.size` only? | Scales with pace (`pace.*`)? |
|---|---|---|---|
| `o2UsePerPlayerSol` | **8** | multiplied by n | **No** (per-sol physical number) |
| `waterUsePerPlayerSol` | **3** | multiplied by n | No |
| `foodRationsPerPlayerSol` | 1 | multiplied by n (stub; never fails; not in Dome stable) | No |
| `foodStartPerPlayer` | 40 | multiplied by n | No |
| `o2StartFillPerPlayer` | **14** | multiplied by n (start fill = 14 x n) | No |
| `waterStartFill` | **60** | not scaled (fixed) | No |
| `batteryStartCharge` | **5** | not scaled | No |
| `o2TankCapacity` | **200** (5 tanks of 40) | not scaled (the dome is sized for 5) | No |
| `waterTankCapacity` | **120** (2 tanks of 60) | not scaled | No |
| `leakO2PerSol` | **10** (extra loss per sol while a required volume is unsealed) | not scaled | No |
| `reserveSols` | **3** standard, **1** classroom | reserve needed = `reserveSols` x `o2UsePerPlayerSol` x n | **Yes: this is a pacing requirement** (like a hold), set by the classroom preset, not a physical threshold |
| `waterLastSols` | **10** | no | No |
| `precomposedMakers` | `"ceil(n/2)"` (pre-built oxygen makers) | n only | No |
| `precomposedReclaimers` | 1 | no | No |
| `suitO2Tier1` / `suitO2Tier2` | **6** / **10** (tier 2 unlocked by Dome stable) | no | No |
| `suitRefillFree` | true (needs a non-empty wall tank) | no | No |
| `blackoutAfterSeconds` | **120** s short of breath; **30** s with an empty suit outside or in an unsealed volume | no | No (real seconds; **invented**) |
| `blackoutWakeDelaySeconds` | **10**; wake in bunk; suit refilled to 3 units; no item or health loss | no | No |
| `landingGrace` | true (no use or leak before t0 = sol 1.2) | no | No |

**Only group.size scales:** total use, total start fill, pre-built makers, reserve needed. **Nothing in this file scales with `pace.rateMultiplier` except through `reserveSols`.** Physical thresholds (75 kPa, 5 kPa, 30 kPa, breathability) are not in this file and never scale.

### 2.2 Machine files in `data/mars/machines/`

Existing keys kept: `schema`, `id`, `contributions` (list), `power` (`drawPerSol`, `supplyPerSol`, `storage`), `source`. New keys: `priority` (int), `o2PerSol`, `waterPerSol`, `recovery`, `servesPlayers`, `unlockedBy`, `recipe`.

| File | id | Keys and values | Notes |
|---|---|---|---|
| `oxygen_maker.json` (new) | `mars:oxygen_maker` | `priority` **1**; `power.drawPerSol` **1.0**; `o2PerSol` **10**; `contributions` [] ; `tier` 1 | Output scales with power share like every powered machine. One per player is the rule of thumb |
| `water_reclaimer.json` (new) | `mars:water_reclaimer` | `priority` **1**; `power.drawPerSol` **1.0**; `recovery` **0.9**; `servesPlayers` **5** | Loss = (1 - 0.9) x 3 = 0.3 per player per sol; one unit serves up to 5 |
| `o2_tank.json` (new) | `mars:o2_tank` | `capacity` **40**; `count` in dome 5; no power | Registers capacity with the planet O2 store; casing airtight |
| `water_tank.json` (new) | `mars:water_tank` | `capacity` **60**; count in dome 2 | |
| `hull_plate.json` (new; may be a block-only definition) | `mars:hull_plate` | `airtight` true; no power; `recipe` see 2.3 | Block, not a machine, but kept in this list because it has a recipe |
| `airlock.json` (new) | `mars:airlock_door` | `airtight` true when closed; interlock (no two doors open together); `suitRefillPoint` true; no power in slice 2 | The dust-off station is slice 6 |
| `regolith_oven.json` (edit) | `mars:regolith_oven` | add `priority` **2**; add `waterPerSol` **0.5** (dome water stock only, **never added to the hydrosphere index**, balance-sheet 2d.2); keep `power.drawPerSol` 1.0, `startingGrant` 2 | `waterPerSol` scaled by power share |
| `regolith_outgasser.json` (edit) | `mars:regolith_outgasser` | add `priority` **3** | |
| `solar_panel.json`, `battery.json` | unchanged | `battery.power.storage` 10 | no `priority` (they are supply and storage) |

**Priority rule (balance-sheet 2d.4):** tier 1 life support (oxygen maker, reclaimer) is never cut while any lower tier draws; equal share inside a tier; a tier gets what remains after the tiers above it (supply first, then battery). `PowerGrid` must run tiers in order; the current single equal-share rule (slice 3) is replaced.

### 2.3 Recipes (KubeJS; item names are stand-ins, the Technical Lead maps them to the Phase 1 resource list, open item G4)

Material stand-ins: Fe (iron plate), W (wire), G (glass), B (regolith brick). **All counts invented.** Classroom is the standard cost divided by 1.3, rounded up (balance-sheet 5.3). Rows marked **proposed** were not in the balance sheet; the Game Designer adds them there when the Technical Lead confirms.

| Item | Standard | Classroom | Tier gate | Status |
|---|---|---|---|---|
| Oxygen maker | 6 Fe, 3 W, 2 G | 5 Fe, 3 W, 2 G | 1 | balance sheet 2d.3 |
| Water reclaimer | 6 Fe, 2 W, 4 G | 5 Fe, 2 W, 4 G | 1 | 2d.3 |
| Solar panel (round array) | 6 Fe, 4 W, 8 G | 5 Fe, 4 W, 7 G | none | 2d.3 |
| Battery | 4 Fe, 6 W | 4 Fe, 5 W | none | 2d.3 |
| Regolith Oven | 3 Fe, 2 W, 5 B | 3 Fe, 2 W, 4 B | 2 | 2d.3 |
| Regolith Outgasser | 10 Fe, 6 W, 6 B, 2 G | 8 Fe, 5 W, 5 B, 2 G | 3 | 2d.3 |
| Seal kit | 1 Fe, 1 B | 1 Fe, 1 B | n/a | 2d.3 |
| **Hull plate** (airtight full block) | 2 Fe | 2 Fe | none | **proposed**, invented: cheap, so a breach is a quick fix (goal C2) |
| **O2 wall tank** (+40 capacity) | 4 Fe, 2 G | 4 Fe, 2 G | none | **proposed**, invented; the dome starts with 5, extra tanks raise the capacity above 200 only if the Technical Lead lifts the cap (default: capacity fixed at 200, so this recipe is for replacement only) |
| **Airlock door** | 4 Fe, 2 W, 2 G | 3 Fe, 2 W, 2 G | none | **proposed**, invented |

`pace.recipePreset` selects the column (standard or classroom). Recipe time check (invented gather rate 4 items per minute experienced, 2 new): one extra maker is 11 items, about 3 to 6 minutes; balance-sheet 2d.3.

### 2.4 Gate: `data/mars/gates/dome_stable.json` (edit)

Replace `"condition": { "machine_state": "life_support_ok" }` with:

```json
"condition": { "all_of": [
  { "machine_state": "sealed" },
  { "machine_state": "air_ok" },
  { "machine_state": "water_ok" },
  { "machine_state": "power_ok" }
] }
```

Hold, reset and unlocks stay: `hold.standard_sols` 3, `classroom_sols` 1, `reset_after_sols` standard 1.0, classroom 0.5; `unlocks` `mars:regolith_outgasser`. Pause on failure, reset after more than one continuous sol out (balance-sheet 2c decision 7). `PlanetSim.lifeSupportOk()` is retired. Keep latching for good. The Monitor names the failing state in "Waiting: {state}".

| State key | True when (all as data where possible) |
|---|---|
| `sealed` | Every required volume (1.1 S3) is sealed |
| `air_ok` | O2 made at the present power >= O2 used by n players (net >= 0) **and** stored O2 >= `reserveSols` x `o2UsePerPlayerSol` x n |
| `water_ok` | A reclaimer is running **and** (stored water / net daily loss >= `waterLastSols` or water is not falling) |
| `power_ok` | Every priority-1 load is fully powered **and** battery charge >= one sol of priority-1 draw |

### 2.5 Test values (so a unit test can compare data to the sheet)

Reference numbers the tests assert (balance-sheet 2d.7, calc not run in the game): solo standard reaches `air_ok` 5.0 sols after t0 (stored 14, reserve 24, net +2); Dome stable at sol 9.2 (3.07 h). Standard groups: n = 2: sol 10.7; 3: 10.2; 4: 11.2; 5: 10.8. Classroom (extra makers built 0.3 sol after t0): sol 2.5. The pressure-curve check against balance-sheet section 2 stays within 0.03 kPa (2d.7). Tolerance for the "within 25 % of 3 h" exit criterion: 2.25 to 3.75 h; n = 4 sits at 3.73 h.

## 3. What to hand back

Technical Lead: confirm S1 and S2 (or change with reasons), key names, whether `o2_tank` capacity can grow, and any recipe item mapping; the Game Designer then updates the balance sheet. Dependencies: slice 1 (sealing) must give the `sealed` state before slice 2's gate can be true.
