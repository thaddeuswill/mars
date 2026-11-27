# Report: Technical Lead, Stage 2 slice 3 (power)

- **Role:** Technical Lead · **Date:** 2026-11-27 · Everything below ran in this Linux sandbox (Gradle `--offline`, nothing downloaded, EULA untouched).

## 0. Handoffs received
- **Coordinator (BACKLOG #33):** accepted. The stale arrival line was already removed by Codex in `07fba27`, so I skipped that task and did not duplicate it. I merged `origin` first (a half-finished merge was in the tree; conflicts only in `AGENTS.md` and `BACKLOG.md`, resolved by keeping both sides' facts). BACKLOG now has two rows numbered 33 (mine and Codex's coordinator row): the Producer should renumber one.

## 1. Done
**What changes for the player:** a Regolith Outgasser now needs power. A solar panel makes power (less when dusty), a battery stores spare power, and the Monitor has a **Power** row ("Enough power", "Not enough power", "No power") with supply, demand and battery lines, so a slow Outgasser says why.

**Slice 1 behaviour, documented as changed:** an Outgasser used to raise pressure with no power; **it now adds 0 kPa/sol unless powered**. Recorded in ARCHITECTURE.md 2.19 ("Changed in slice 3"), the machine file's `source`, balance sheet 2b, HANDOFF known gaps.

- Pure: `mod/src/main/java/mars/power/PowerGrid.java` (supply first, then battery, surplus charges it, shortfall shared equally); `MachineDef.Power` (`drawPerSol`, `supplyPerSol`, `storage`, optional); `PlanetaryIndexModel.setBeforeStep`; `PlanetSim` (grid, per-position output, `applyPower`, save/restore); `PlanetState` schema 2 (schema 1 saves still load).
- Minecraft: `BatteryBlock`, `SolarPanelBlock` registers with the planet and updates output on every dust-stage change, `PlanetManager.machineOutput`, `IndexDefinitions` now loads `data/mars/hazards/panel_dust.json` (`Defs.dust`), `PlanetData` saves charge and output.
- Data: `pack/data/mars/machines/{regolith_outgasser,solar_panel,battery}.json`; lang keys `monitor.row.power`, `monitor.power.*`.
- Numbers, balance sheet new **2b** (`docs/design/balance-sheet.md`): clean panel **5 units/sol** (sourced anchor: InSight about 5,000 Wh/sol, PIA25284); Outgasser draw **4 units/sol (invented)**; battery **10 units (invented)**; 1 unit = 1 kWh; panel output by dust stage from 2a. Power is never scaled by pace (tested).
- Docs: ARCHITECTURE.md 2.19, `docs/tech/stage2-plan.md` slice 3 row, AGENTS.md, HANDOFF.md, BACKLOG #33.
- Tests: `PowerGridTest` (4), `PowerSimTest` (8, incl. data-vs-sheet), ArchUnit now covers `mars.power..`; GameTest `outgasserNeedsPowerFromRealBlocks`. Existing tests and GameTests that relied on unpowered Outgassers now add panels first.

## 2. What actually ran
| Check | Command | Result |
|---|---|---|
| Format, build, unit tests | `./gradlew spotlessCheck build test --rerun --offline` | passed; 79 tests, 0 failures (67 before) |
| Boot + GameTests | `./gradlew runGameTestServer --offline` | `RESULT PASS passed=46 failed=0` (41 before) |
| Printed evidence | unit and GameTest output | 1 panel + 1 Outgasser, 10 sols: 0.6 -> 1.6; 2 Outgassers on 1 panel: 1.85 (62.5 %); stage 4 panel: +0.75 (60 % of 5 = 3 of 4); battery full 10.0 after 10 sols, then 2.5 sols of running after the panel is removed; unpowered: 0.6 -> 0.6; save/restore charge 10.0 = 10.0; classroom and standard charge identical (3.0); real blocks: alone `added 0.0 row=monitor.power.unpowered`, real panel `added 0.1 supply=5.0`, stage 4 `added 0.075 supply=3.0`, battery `capacity=10.0` |
| Prove the checks can fail | made `applyPower` skip scaling (`if (true \|\| ...)`) | 4 unit tests failed (unpowered, shared shortfall, dusty panel, battery); GameTest `FAIL power.outgasser_alone_unpowered ... added 0.1` and `power.dust_stage_cuts_output ... added 0.1`, RESULT FAIL passed=44 failed=2. Code restored; all green again |
| Screenshots | `tools/screenshots/run-shots.sh /home/user/mars/tools/screenshots/slice3_power.shots` (no client was running; rc=0) | `tools/screenshots/out/20261127-112714/` (new `slice3_power.shots`). I looked at them: **unpowered** shows "No power" and the Outgasser cause line at 0.00 kPa/sol, pressure steady at 0.60; **short** (two Outgassers, stage 4 panel, empty battery) shows "Not enough power", +3.0 supply, -8.0 demand, battery 0.0 / 10.0, pressure rising at +0.08 kPa/sol; the powered shot was also taken. Panel fits at 1280x720 |

## 3. Risk gate
| Change | Could break | Check | Result |
|---|---|---|---|
| Outgasser needs power | Old saves or tests expecting a free Outgasser; owner surprised that slice 1 pressure stopped | Tests updated; documented as changed | Pass; owner should place a panel first |
| Save schema 2 | Old saves unreadable | `PlanetState` 5-arg path still loads schema 1; newer-than-2 refused (GameTest) | Pass for NBT round trip; no real disk restart (EULA) |
| Dust stage to output | Panel dust change not reaching the grid | `power.dust_stage_cuts_output` with a real block-state change | Pass |
| Per-step cost | Extra work per step | One pass over powered machines per step (every 100 ticks); not profiled | Stated |
| Dust never changes by itself | Players see no dust effect | Out of slice scope; dust stage changes only by placement or commands until the hazard slice | Open |

## 4. Decisions needed
| Decision | Recommendation | Type |
|---|---|---|
| Are 5 / 4 / 10 units (panel / Outgasser / battery) the right feel? | Keep as invented starting values; check in the first playtest whether one panel per Outgasser feels like too little or too much to build | Ill-structured (balance) |
| Should the Oven (Low power) draw power now? | Not until slice 4/5 with the Dome stable gate | Well-structured |

## 5. Handoffs
> **To:** Lead Game Designer · **Problem type:** well-structured
> **Problem:** confirm balance sheet 2b numbers and give Monitor wording for "Enough power / Not enough power / No power" and the "units/sol" label at grade 6-7. **Done when:** 2b tagged approved and lang keys final.

> **To:** Producer · **Problem type:** well-structured
> **Problem:** BACKLOG has two rows numbered 33; renumber one.

## 6. Risks
- Power numbers are invented; night and storms are not on the grid, so the grid is easier than the design intends until later slices.
- Pressure is still not on the balance-sheet curve until the Dome stable gate and Oven exist.
- Not tested: real server restart, a real right-click placement of a panel by a player in the client (screenshots use `/setblock`), third-party machines.

## 7. Shared model updates
- **Decision (owner doc ARCHITECTURE.md 2.19, balance sheet 2b):** one shared power grid per planet in power units (1 unit = 1 kWh) per sol; supply first, then battery; a shortfall is shared; an Outgasser's effect scales with the share of power it gets; power is not scaled by pace.
- **Lesson:** when a behaviour change breaks old tests, update the tests to add the new precondition (panels) and keep a test that asserts the unpowered case, rather than loosening the old assertions.
