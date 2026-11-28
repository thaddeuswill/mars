# Technical Lead report: slice B, air we make (CO2 to O2, algae vat, carbon scrubber)

Date 2026-11-28 · BACKLOG #49 · Branch `claude/elegant-brown-anu1ci` · Code commit `055c418`

## 0. Handoffs received
- `docs/design/phase3-hydrosphere-and-drift.md` slice B, DESIGN.md rule A, balance sheet 3a, `gate-input-items.md` 3.5, owner rules (standard pace only, INVENTED labels, vanilla crafting stays, never hard-block, make-it-work). **Accepted**, with the deviations in section 4.
- Lead Artist's `algae_vat` and `carbon_scrubber` models, textures and item models (commit `ffd4fe2`): present and used unchanged. I wrote only the two blockstates (copied from the processor's pattern, same `y` turns).
- The Game Designer's pending ruling on hopper, furnace and starter-chest inputs: **not acted on**; affected recipes are listed in section 5.

## 1. Done (all run, real output)
| Item | Result |
|---|---|
| Gas mechanism | `conversions` in machine data (`MachineDef.Conversion`: from CO2, to `mars:o2_partial` or carbonate, `kpaPerSol`, `biomassPerKpa`). `PlanetSim.applyGas` (one coupling): O2 is kept in kPa (pressure changes dilute or concentrate the fraction, never make O2); a converter never takes more CO2 than the air has; to O2 = one for one with total pressure unchanged; to carbonate = CO2 and total pressure both fall, `carbonateLockedKpa()` records it (saved in the `values` map under a reserved key, not a parameter, no schema bump). |
| CO2 sources (needed, see 4.1) | Outgasser +0.10 kPa/sol CO2; processor +0.08 CO2 until Phase 3 begins (new `offAfterGate` on a contribution), then filler only; the polar frost is CO2 ice so it takes and returns CO2 (clamped at 0). |
| Machines | `algae_vat`: 0.1 kPa/sol, 2 units/sol, 100 BU per kPa, `requires` hydrosphere >= 10 and temperature >= 0 (both in the machine file, gate condition language, never scaled). `carbon_scrubber`: 0.2 kPa/sol, 2.5 units/sol, no condition. All INVENTED, rows added to balance-sheet 2d. |
| Blocks | Both `GatedLoadBlock`, one cell, 24 px outline and collision, loot, lang, `machine_state` blockstates on the Artist's models (no placeholder was needed). Tooltips: both start "Game-sized version of a real idea". Vat wording cites the Veggie-style sealed plant chamber (the verified reference); the photobioreactor idea is general knowledge and is not claimed. Scrubber: the CDRA pulls CO2 from air, "the real one lets the CO2 go. Here it is locked into rock" (matches `machine-references.md` row and its "must not claim"). |
| Sorbent bed | Did not exist: built (`item.mars.sorbent_bed`, tooltip, recipe 2 red sandstone + glass pane + iron ingot, placeholder icon from `tools/art-preview/gen_gate_item_textures.py`, which never overwrites). Cache of 2 on `phase_3_begins` (delivery code is the slice A cache path), supply note `gate.supply.phase_3_begins`. |
| Recipes (vanilla-first, INVENTED) | Vat: 5 glass, water bucket, 2 iron ingots, heater coil. Scrubber: 4 iron ingots, hopper, 2 sorbent beds. |
| Gates | `phase_3_begins` unlocks `carbon_scrubber` and `algae_vat`. `breathable.json` and `equilibrium.json`: `unlocks` still empty (`beacon_bench`, `colonization_beacon` are not blocks); their stale "cannot latch" source text is replaced. |
| Monitor | Rows Oxygen and Carbon dioxide (kPa, trend, net rate; causes show problems only: needs power, needs water, too cold, nothing to use). Next goal row: a hold gate whose counter has not started now shows its first unmet condition in any direction ("5.15 / 16 kPa", "2.90 / 0.5 kPa or less"); once it counts, the counter. `PlanetaryIndexModel.unit` knows O2 partial is kPa. |
| Tests | 23 new unit tests (402 total, baseline 379), 8 new boot checks (173, baseline 165). |

Command output (from `mod/`, final): `./gradlew spotlessCheck build runGameTestServer`: **BUILD SUCCESSFUL, 402 unit tests, 0 failures; `MARS-SELFTEST RESULT PASS passed=173 failed=0`.**

## 2. Does the standard build reach Breathable and Equilibrium? Yes in the model (calc, run: `OxygenSliceTest`)
Script (INVENTED counts): 2 outgassers to Phase 2 begins, then retired (design: outgassers are Phase 1 machines); Phase 2 emitters 2/3/4/5/6 at sols 0/6/14/24/34, processors 2/3/4 at 0/14/34, extractors 2/3/4 at Thaw +0/6/14, panels added to cover demand; Phase 3: processors to 10, emitters back to 2, **9 vats, 1 scrubber**, processors removed at 76 kPa.

| Measure | Result |
|---|---|
| Phase 3 begins | global sol 82 (sheet: Phase 2 is 54 sols; this build is 8 sols slower, temperature-limited, same as C-3 in `phase2-machine-rates.md`) |
| CO2 at Phase 3 begins | 19.87 kPa (sheet 18.0, "invented around the SME's 18.5") |
| Conversion | 9 vats x 0.1 = 0.9 kPa/sol to O2, scrubber 0.2 kPa/sol to rock: 1.1 kPa/sol, CO2 gone in about 18 sols |
| Pressure | 10 processors x 0.15 = 1.5 kPa/sol from 31.5; the scrubber also removes 3.6 kPa: 75 kPa at about Phase 3 sol 32 (sheet says 25; the sheet needs 1.8 kPa/sol, i.e. 12 processors) |
| Breathable | latches Phase 3 sol 39 (75 kPa at 32 + 7-sol hold) |
| Equilibrium | latches Phase 3 sol 46 (14-sol hold from 32); O2 21.0 %, biomass 1,626 BU, carbonate tally 3.6 kPa |
| Power | peak demand 64.5 units/sol (4 extractors 8, 2 emitters 6, 10 processors 30, 9 vats 18, scrubber 2.5) = 13 panels; the sheet's Phase 3 peak is 54 (12 panels), so the air machines add about one panel per 2.5 vats |
| No scrubber | Breathable still latches (Phase 3 sol 36) but O2 ends at 26 % (all 19.9 kPa of CO2 became O2): Equilibrium never latches in the script. Escape, not a hard block: more pressure (O2 under 23 % needs about 87 kPa) or scrub. This is the B4 trade-off in the design. |

So the design numbers do reach both gates. Two things do **not** reach as written (section 4.2 and 4.1).

## 3. What I saw (real client, software GL; script `tools/screenshots/slice_b_oxygen.shots`, 1280x720)
Run dir `tools/screenshots/out/20261128-180743/` (it also holds copies of older PNGs; only three are cited). `pgrep -f "[r]unShotsClient"` was empty first; source PNGs in `mod/run-shots/screenshots` are timestamped 18:08 and match the copies; `client.log` has 0 'Unknown block'.
- `vats_and_scrubber_running.png`: three vats (hood, amber culture window, pipe) and a scrubber (twin canisters) beside a battery; stills only, I did not see the bubble animation.
- `monitor_air_rows.png`: Pressure 55.0, Temperature 3, Surface water 19.5, **Oxygen 5.15 kPa rising +0.30 kPa/sol**, **Carbon dioxide 7.25 kPa falling -0.50 kPa/sol** (3 vats = 0.3, scrubber 0.2), Power enough, **Next goal Breathable 5.15 / 16 kPa** with a bar. Panel fits.
- `monitor_next_goal_breathable_co2.png`: Oxygen 16.8, CO2 2.90, Next goal "Breathable 2.90 / 0.5 kPa or less". The goal name draws smaller (existing fit rule) but is readable.
Note: the panel shows no plot in this state (rows only), which the layout test expects for a crowded Phase 3 panel.

## 4. Deviations and findings (honest list)
1. **CO2 had no source.** Before this slice nothing raised `co2_partial` (balance sheet gap C-5): it sat at 0.57, so vats had nothing to convert and Breathable was unreachable even with vats. I added the CO2 terms above (a scope extension; all rates INVENTED, in data). Side effects: the polar frost's one-time 0.5 kPa buried CO2 now lands in CO2, and kept outgassers in Phase 2 push CO2 above the sheet (more scrubbing needed): that is the player's B4 choice.
2. **Biomass.** The design's "+20 BU per vat" needs 75 vats for the 1,500 BU target, which the design itself calls too many. I made biomass follow the gas moved (100 BU per kPa converted, about 1,500 BU per 15 kPa), so vats alone reach 1,500. Flora patches (not built) are the intended second source. Smallest alternative: lower the target. **Decision needed.**
3. **Vat gate.** `DESIGN.md` line 74 and `machine-priorities.md` unlock vats at Hydrosphere; the phase3 doc section 3 table and the task say Phase 3 begins. I used Phase 3 begins plus the vat's own `requires hydrosphere >= 10` (so the effect is the same). DESIGN.md is not edited (not mine).
4. **Scrubbing lowers total pressure** (the gas leaves the air); the sheet says total is held constant "during conversion" and is silent on scrubbing. Cost: the scrubbed 3.6 kPa has to be made up by processors. Easy to switch off in `applyGas` if the Game Designer disagrees.
5. **O2 kept in kPa.** `o2_fraction` is stored, but pressure rises by 45 kPa in Phase 3; without the dilution rule early conversion would have made free O2 later. This changes the O2 fraction during Phases 1 and 2 (it dilutes toward 0.00 %), which nothing shows.
6. **Gas rows hidden before Phase 2 begins** (design said "once the value changes from baseline"): with Outgassers the CO2 row would appear in sol 1 and breaks the existing Phase 1 layout bounds (the layout tests failed at +12 px). Phase 1 CO2 is nearly all of the pressure row anyway.
7. Balance-sheet 2d had no rows for vat, scrubber or the CO2 terms: I added three (INVENTED tagged), so tests compare data with the sheet. The Game Designer should review them.
8. Gate files were re-written with a JSON tool (indent changed, content the same).
9. Vat and scrubber recipes use glass, water bucket, hopper, red sandstone: all vanilla; nothing is craftable only from Mars-unminable items except the hopper (needs wood).

## 5. Handoffs
> **To:** Lead Game Designer · **Problem type:** ill-structured (biomass) and well-structured (rest)
> **Problem:** (a) biomass: 20 BU per vat (75 vats) versus 100 BU per kPa converted, or a lower 1,500 target, or flora only. (b) Confirm vats unlock at Phase 3 begins (and fix DESIGN.md line 74). (c) Confirm scrubbing lowers total pressure. (d) Review balance-sheet 2d rows for vat, scrubber and CO2 terms. (e) Phase 3 pressure: 10 processors reach 75 kPa at about Phase 3 sol 32, not 25.
> **Context:** `pack/data/mars/machines/algae_vat.json`, `carbon_scrubber.json`, section 2 of this report.
> **Needed from them:** a ruling per item; (a) is the one that needs a choice. My view: keep gas-driven biomass until flora exists, then re-split.
> **Done when:** the sheet and data agree.

> **To:** Lead Game Designer (the pending vanilla-crafting ruling) · **Problem type:** well-structured
> **Problem:** recipes that use items Mars cannot make: `algae_vat` (glass, water bucket), `carbon_scrubber` (hopper), `sorbent_bed` (red sandstone, glass pane), and from earlier slices `atmospheric_processor`, `super_greenhouse_emitter`, `ice_extractor` (hopper, furnace, bucket). I did not change the starter chest or the hopper and furnace inputs.
> **Done when:** the ruling is applied to these recipes (data only).

> **To:** Instructional Designer · **Problem type:** well-structured
> **Problem:** wording check: `monitor.row.oxygen`, `monitor.row.co2`, `monitor.cause.needs_water`, `monitor.cause.no_input`, `gate.supply.phase_3_begins`, tooltips of `algae_vat`, `carbon_scrubber`, `sorbent_bed` (gas names are allowed there per design 3.5). No goal IDs changed.
> **Done when:** confirmed or replaced.

> **To:** Subject Matter Expert · **Problem type:** well-structured
> **Problem:** the algae vat has no row in `machine-references.md`; its tooltip leans on Veggie (verified) and not on a photobioreactor source. Porous brick as sorbent (here red sandstone) and carbonate abundance stay NOT VERIFIED.
> **Done when:** a vat row and a source line exist.

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** replace the `sorbent_bed` placeholder icon (`pack/assets/mars/textures/item/sorbent_bed.png`); three more gate items (resin pack, signal board, activation cell) still need icons.
> **Done when:** real PNG under the same name.

## 6. Risks
- Balance is unplayed. The playtest should measure: sols from Phase 3 begins to 16 kPa O2 (model: about 18 with 9 vats), whether players find the O2-over-23 % trap, and power (13 panels).
- Breathable and Equilibrium still unlock nothing: after Equilibrium there is no beacon to build.
- Decay and fire (O2 back to CO2), flora conversion and biomass decay are not built, so the drift events in the design have no gas hook yet; the `applyGas` bookkeeping is where they go.
- Not run: dedicated server, a real key path, animation, colour-vision check, old save with vats (a new type loads from the planet data; the carbonate tally is read from the saved values map and tested in `OxygenSliceTest`).

## 7. Shared model updates
- **New term:** *conversion* (a machine data field that moves gas between Index values, rule A). Owning document: `ARCHITECTURE.md` 2.23i.
- **New term:** *carbonate tally* (kPa of CO2 scrubbers have locked into rock; not a parameter). Owning document: `ARCHITECTURE.md` 2.23i.
- **Lesson:** a slice that adds a consumer (the vat) must check that the producer exists; CO2 had none for three slices because no test ran a whole play-through. `OxygenSliceTest.standardBuild` is now that test.
- **Decisions recorded (Technical Lead, within scope):** O2 is bookkept in kPa; scrubbing lowers total pressure; vats unlock at Phase 3 begins with their own water and warmth condition; gas rows appear from Phase 2 begins.
