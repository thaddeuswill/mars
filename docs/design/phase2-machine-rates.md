# Phase 2 machine rates, remaining gates, recipe unlock

**Owner:** Lead Game Designer · **Written:** 2026-11-28 · **Status:** proposal; numbers are **fluid** (TEAM.md 8.1) and unplayed · **Feeds:** `docs/design/balance-sheet.md` §2d, `pack/data/mars/machines/*.json`, `pack/data/mars/gates/*.json` (Technical Lead builds)
**Scope (owner, 2026-11-28):** not a classroom pack; the owner is the only player; standard pace and solo only. Classroom columns are ignored here (the existing gate files keep their `classroom_sols` field because the parser expects it).
Tags: **sourced**, **calc** (arithmetic; my scratch Python model, not run in the game), **INVENTED** (game balance). Goals: A1, B1, B2, B4, C1, C2. Pillars: the planet is the quest log; every change is visible; pressure, not grind; compact before wide.

---

## 1. Per-machine rate rows

| Machine | Index effect at full power, standard, solo | Draw | Efficiency | Tag |
|---|---|---|---|---|
| Atmospheric processor | pressure **+0.15 kPa/sol** each | **3 units/sol** (existing) | 0.050 kPa per unit | **INVENTED** rate. Draw already INVENTED in `atmospheric_processor.json` |
| Super-greenhouse emitter | temperature **+0.25 °C/sol** each | **3 units/sol** (existing) | 0.083 °C per unit | **INVENTED** |
| (reference) Regolith Outgasser | +0.10 kPa/sol | 4 | 0.025 kPa per unit | existing |
| (reference) Regolith Oven | +0.01 kPa/sol | 1 | 0.010 | existing |
| (reference) Clean solar panel | 5 units/sol; average about 4.5 with dust (§2a, 91 %); about 4.4 with Phase 2 storms | n/a | | existing |

Why these values.
- **Processor is twice as efficient per power unit as the Outgasser** (0.05 against 0.025). That is the progression: the Phase 2 machine is better, so Phase 3's +1.8 kPa/sol is reachable in about 10 machines, not 20. 10 processors is the "wide building" of pillar 4, paid for in panels.
- **Constant rate.** A real compressor works harder at high pressure; we do not model it (one number per machine, learned once, B1). Revisit if players say late pressure feels too easy.
- **Emitter 0.25 °C/sol** makes 60 °C cost 240 emitter-sols. The sheet's +1.1 °C/sol average then means 4 to 5 emitters running on average.
- Pace: `pace.rateMultiplier` is 1.0 in the owner's play, so these are the live numbers. Group scale also 1.0.

### 1.1 Pressure, 5 to 30 kPa (calc)

Model: 4 Outgassers and 2 Ovens always on (0.42 kPa/sol), N processors, polar-frost sink as in `sinks/polar_frost.json` (2 % per sol of P - 0.6 below -40 °C, 1 % from -40 to -20 °C, release at 0.5 kPa/sol above -20 °C, then 0.5 kPa buried CO2). Emitters held at 6 so the Thaw is not the variable.

| Processors (constant) | 0 | 1 | 2 | 3 | 4 | 6 | 8 |
|---|---|---|---|---|---|---|---|
| Sols from 5 to 30 kPa | 58 | 43 | 35 | 32 | 29 | 23 | 18 |

Reading it. The frost sink eats 0.09 kPa/sol at 5 kPa and about 0.2 at 15 kPa, so the cold-phase net of 3 processors is 0.87 - 0.2 = about 0.67 kPa/sol. After the Thaw the stored frost comes back (+0.5 kPa/sol, about 5.6 kPa in total in this model, plus 0.5 buried): that is why even **zero processors reach 30 kPa at sol 58**. The release is a real boost the player earns by warming, and it is the reinforcing loop of B2.

**Standard build schedule (recommended, INVENTED):** 1 processor at sol 0 of Phase 2, 2 at sol 14, 3 at sol 34. Result: 30 kPa at **sol 43.6** of Phase 2. Pressure leads and warmth gates, which is the intent (below). The sheet's average of +0.46 kPa/sol would put 30 kPa at sol 54; this build is faster because the release arrives early. To make pressure arrive on sol 54 too, drop the third processor.

Storm effect (Phase 2 cap 0.8, solar -48 % for a 1-sol drift-event storm, §2). With no battery, that sol runs at 52 % share and loses about 0.48 x 0.6 = **0.3 kPa**, one half sol of progress. Three or four such storms cost about one to two sols in total. Batteries make it free: a 45-unit grid short by 48 % needs 21.6 units = **about 2 batteries** (10 units each, §2b). So the player is rewarded for one battery per 4 to 5 panels. The 1-sol storm warns 1 sol ahead (§2).

### 1.2 Temperature, -60 to 0 °C (calc)

| Emitters (constant) | 2 | 3 | 4 | 5 | 6 | 8 |
|---|---|---|---|---|---|---|
| Sols to -20 °C (Thaw) | 80 | 53 | 40 | 32 | 27 | 20 |
| Sols to 0 °C (Phase 3 begins) | 116 | 77 | 58 | 47 | 39 | 29 |

Simple check without albedo: 60 / (0.25 x E): E=4 gives 60 sols, the table's 58 (the albedo gain, below). **Ice-albedo:** capped at 2 °C total (§8, INVENTED), spread over the melting after the Thaw (about 0.11 °C/sol for 18 sols in the model). It is worth about 3 to 4 sols at 4 emitters, as DESIGN.md says: small, visible on the Monitor cause line, honest.

**Standard build schedule (INVENTED):** 2 emitters at sol 0, 3 at sol 6, 4 at sol 14, 5 at sol 24, 6 at sol 34. Result: Thaw at **sol 39.6**, 0 °C at **sol 52.1** (3a curve says Thaw at 36 and 0 °C at 54; I accept a Thaw about 4 sols later: the curve is a sketch). **Emitters, not processors, set the Phase 2 clock**: pressure is ready on sol 44 and waits about 8 sols for warmth. That is the right way round for the story (the Thaw wakes the fauna, F4b, and the polar release) and tells the player where to build.

### 1.3 Phase 3 sizing (so the rates carry on)

Net pressure rate = 0.42 + 0.15 N (no frost sink above -20 °C).

| Processors | 3 | 6 | 8 | 10 | 12 |
|---|---|---|---|---|---|
| kPa/sol | 0.87 | 1.32 | 1.62 | 1.92 | 2.22 |
| Sols for 30 to 75 kPa | 52 | 34 | 28 | 23 | 20 |

The sheet needs +1.8 kPa/sol (25 sols). **About 9 to 10 processors** do it. Phase 3 temperature needs only 0.12 to 0.2 °C/sol (0 to 5 °C), so **1 to 2 emitters** run and the rest are switched off (a lever, vanilla-first). Phase 3 processors add filler gas only (rule A).

### 1.4 Power (calc against §2b)

Use 4.4 units per panel (91 % dust average x 0.97 Phase 2 storm loss).

| Moment | Load | Units/sol | Panels | Batteries (1 per 4 to 5 panels) |
|---|---|---|---|---|
| End of Phase 1 | 4 Outgassers 16 + 2 Ovens 2 | 18 | 4 | 1 |
| Phase 2 sol 0 | + 1 processor 3 + 2 emitters 6 | 27 | 6 | 2 |
| Phase 2 sol 34 | 4 Outgassers 16 + Ovens 2 + 3 processors 9 + 6 emitters 18 | 45 | 10 | 3 |
| Phase 3 (peak) | 4 Outgassers 16 + Ovens 2 + 10 processors 30 + 2 emitters 6 | 54 | 12 | 3 |

Against the 3 units/sol placeholder (draw, INVENTED): kept. A processor is 3/4 of an Outgasser's draw and an emitter is the same; both are "Medium". Total build: about 12 panels at the Phase 3 peak, three times the Phase 1 grid. That is the **hidden pacing gate** the sheet warned of (§2): count it in the first playtest (see 1.6). Panel footprint is 3 x 3, so 12 panels is a field: this is the "wide building is earned" beat, with the dust brush and wiper (§2a) as its upkeep.

### 1.5 Cost, vanilla-first (recipes exist: `pack/data/mars/recipe/`)

| Machine | Existing INVENTED recipe | Raw cost | For the schedule |
|---|---|---|---|
| Processor | 4 iron, glass, 2 copper, hopper (5 iron), redstone | 9 iron, 2 copper, 1 redstone, 1 glass | 3 by end of Phase 2: 27 iron, 6 copper. 10 in Phase 3: 90 iron, 20 copper |
| Emitter | 4 iron, hopper, bucket (3 iron), redstone | 12 iron, 1 redstone | 6: 72 iron, 6 redstone |

Iron is minable (hematite, meteorite iron, `world-and-blocks.md`). **Copper and redstone do not exist on Mars (machine-priorities R1)**, and the starter chest holds 4 copper and 10 redstone. Ten processors need 20 copper, so **R1 blocks Phase 3**, not Phase 2 (3 processors need 6). Recommend the owner decide R1 (D1 in machine-priorities) before Phase 3 work: one rare conductor ore, or iron-only processors. My proposal for the processor if iron-only: replace the 2 copper and the redstone with 3 more iron ingots (about 12 iron each, no copper). Cost curve is **INVENTED** and untested.

### 1.6 How to tune in playtest

| Symptom | First lever | Second lever |
|---|---|---|
| Phase 2 takes more than 18 h | processor rate 0.15 to 0.20 and emitter rate 0.25 to 0.30 | raise panel output (5 to 6 units) |
| Panels feel like the grind (more than 12 panel placements) | draws 3 to 2.5 | panel 5 to 6 |
| Thaw arrives too early, no build-up | emitter 0.25 to 0.20 | start with 1 emitter |
| Pressure reaches 30 long before warmth | drop a processor from the schedule | none; it is intended up to about 10 sols |
| Phase 3 pressure needs more than 12 processors | processor 0.15 to 0.18 | |

Measure from the action log: machine counts by sol, Monitor power row, sols between gates. Do not tune by thresholds, only rates and draws.

---

## 2. Gate specs

Format as `gates/dome_stable.json` and `phase_2_begins.json` (`condition`, `hold`, `unlocks`; `all` and `any` composites exist in `Condition.java`). `reset_after_sols` as §4 rule (1.0 sol, standard). Gates **lock in**. Parameter ids are `mars:*`.

| Gate id (order) | Condition | Hold (standard) | Unlocks (machine and item ids; **machines not yet built are listed as proposed ids**) |
|---|---|---|---|
| `mars:thaw` (3) | temperature >= -20 | none | `mars:ice_extractor` (survey scanner needs nothing) |
| `mars:hydrosphere` (4) | hydrosphere >= 10 | none | `mars:algae_vat`, `mars:meltwater_treatment` (Mekanism and Create: optional, hidden until here; not a data unlock) |
| `mars:phase_3_begins` (5) | all of: pressure >= 30, temperature >= 0 | none | `mars:soil_treatment_unit` (also the kit crafting station, section 3), `mars:carbon_scrubber` (R6 proposal) |
| `mars:breathable` (6) | all of: o2_partial >= 16, pressure >= 75, co2_partial <= 0.5 | **7 sols**, reset after 1.0 | `mars:beacon_bench` (beacon component crafting station, section 3) |
| `mars:equilibrium` (7) | all of: pressure >= 75, temperature >= 5, hydrosphere >= 30, o2_fraction >= 19, o2_fraction <= 23, co2_partial <= 0.5, biomass >= 1500 | **14 sols**, reset after 1.0 | `mars:colonization_beacon` (final assembly) |

Notes.
- **Order and independence.** Hydrosphere is order 4 but is a plain parameter threshold, so it can latch before or after Phase 3 begins in a different build; gates do not depend on order. The sheet's curve has Hydrosphere about sol 46 of Phase 2, before Phase 3.
- **Thaw and Phase 3 begins have no hold**, as DESIGN.md; they are thresholds you cross. Only Breathable and Equilibrium hold (§4).
- **Breathable and Equilibrium run at once.** Each tracks its own counter; Equilibrium includes Breathable's conditions, so Breathable always latches first (sol 7 of the 14).
- **Equilibrium oxygen band.** 19 to 23 % is the §1 fire-safety band; Breathable's O2 partial pressure of 16 kPa at 75 kPa already forces 21.3 %, so the effective window is 21.5 to 23 % (§1 table).
- **Hydrosphere is defined** only as the stored `mars:hydrosphere` percentage; **the world definition and any machine rate are open** (`world-and-blocks.md` section 4, Technical Lead). The ice extractor has **no rate row yet**: out of scope here (task is the pressure and temperature levers). Needed before Thaw is playable: the sheet's curve implies 0 to 16 % over sols 40 to 54 of Phase 2 and 30 % by Phase 3 end, about 1 %/sol late; I will write the extractor row when the owner confirms the Hydrosphere definition.
- **Gate JSON `source` field** per file cites DESIGN.md's gate table and the balance sheet section, as the existing files do. Thresholds are physical: never scaled.

### 2.1 Where the emitter unlocks (answer to the art-spec question)

**Keep the emitter at Phase 2 begins; do not move it to Thaw.** The art spec's "Gate: Thaw and Phase 3 begins" means the emitter *serves* those gates (it is the only lever for them), not that they unlock it. Thaw needs -20 °C and only the emitter and uplink raise temperature: a Thaw-locked emitter would be a deadlock, and the Phase 2 clock (1.2) needs emitters from sol 0. Trade-off: moving it to Phase 3 begins is impossible; the "simpler" option is the only workable one. No change to `phase_2_begins.json`. (Machine-priorities row 4 reads correctly once "serves" replaces "unlocked at".) The ice extractor, which really is Thaw-unlocked, is the only machine whose gate and use differ.

---

## 3. Recipe-unlock rule (kit and beacon recipes)

**Problem.** `unlocks` lists block or machine ids checked at *placement* (`GateTracker`). Vanilla recipes have no unlock condition; the Technical Lead found kit recipes craftable from sol 0.

**Rule (design).** Gate recipes by **crafting station**, not by recipe.
1. Kit recipes (biocrust, meadow, forest, wetland) use a custom recipe type crafted only in the **soil treatment unit**, a block the player can place only after Phase 3 begins (existing placement check). Beacon component recipes use the **beacon bench**, placeable after Breathable. Final assembly is at the bench after Equilibrium (the `colonization_beacon` id).
2. Placement is the lock; no new gate mechanism. Creative players and `/mars gate latch` bypass, as for machines.
3. The recipe book entry appears when the station is built; recipe viewers (EMI) hide the station recipes until then (the queued EMI hide-until-gate slice).

**Why this design.** The unit is already in the plan (DESIGN.md: "soil treatment... soil must be treated before seed kits take"), so the lock is also the fiction: untreated soil, no kit. It reuses the gate machinery that works. Learning: C1 (a requirement you can see and meet); no hidden rule (every change is visible). Appeal: a new block to build is a reward; a recipe that silently refuses is not.

**Alternatives.** (a) An event that cancels or refunds crafting output: fragile, hidden failure, and NeoForge's `ItemCraftedEvent` cannot cancel. (b) Reload the datapack when a gate latches with a recipe condition: heavy, and per-world state is awkward. (c) Advancement-granted recipes only: unlocks the recipe *book*, not crafting. Station gating wins on cost and honesty.

**Technical Lead tasks (done when):** custom recipe type and station block menu for the soil unit; the unit and bench in `phase_3_begins.json` and `breathable.json` `unlocks`; boot check that a survival player cannot place the unit before the gate and that a kit cannot be crafted at a table; old table recipes removed. **Interim:** keep kit recipes as is; the starter chest already supplies one seed packet.

**Cost to the plan:** one new block (soil unit, art is already in the list: row 9) and the bench (new; proposed; art: Artist). If the owner does not want a bench, Breathable's beacon recipes may stay table-craftable with a gated catalyst item; recommend the bench for pillar consistency.

---

## 4. Conflicts and open items against the Decision Log and docs (propose only)

| # | Conflict | Where | Proposal |
|---|---|---|---|
| C-1 | Art spec and machine-priorities say the emitter is a "Thaw" gate machine, task order says Phase 2 begins. Resolved above: serves, not unlocked by. | `docs/art/invented-machines-look.md` row 3; `machine-priorities.md` row 4 | Reword "unlocked at Phase 2 begins" in both |
| C-2 | **Emitter wear event** (DESIGN.md, §4 drift table): "Temperature -1 °C/sol". The Index has **no temperature loss term**, and one emitter is only +0.25 °C/sol, so losing one cannot lower temperature, and -1 °C/sol exceeds the whole Phase 3 emitter output (0.25 to 0.5). | balance-sheet §4; no cooling in `parameters/temperature.json` | Add a small cooling term (INVENTED, to be set with the SME) so that temperature falls when forcing drops, then rescale the event to the lost emitter's 0.25 °C/sol. Or define "equilibrium temperature = forcing" and have temperature follow. Needed before the holds, not before Phase 2. Ill-structured (science, fun, cost) |
| C-3 | Sheet §3a curve assumed Thaw on sol 36; this model gives sol 40 (temperature-limited, pressure waits about 8 sols). | balance-sheet §3a | Accept; update §3a after the prototype curve |
| C-4 | R1 (no copper or redstone) blocks 10 processors in Phase 3 (20 copper). | machine-priorities R1, D1 | Owner decision D1 before Phase 3 |
| C-5 | Processor CO2: sheet §3a has about 52 % CO2 in the Phase 2 air (18 of 30 kPa) and none in Phase 3 (rule A, filler only); machines carry only a pressure contribution, `mars:co2_partial` has none. | machines JSON; §3a | Add to processor JSON `{ "parameter": "mars:co2_partial", "ratePerSol": 0.08, "onlyBeforeGate": "mars:phase_3_begins" }` (INVENTED; 52 % of 0.15; `onlyBeforeGate` is a new optional field). Not blocking: can wait for the Phase 3 slice |
| C-6 | DESIGN.md Thaw wording "dormant fauna begin to stir"; emitters are tier 3 signatures (§6). A 6-emitter build at the Thaw makes the first raid immediate (0.15 x F). | balance-sheet §6 | Playtest check: first raid not before sol 3 after Thaw. Not a decision |

No contradiction with the settled decisions (75 kPa, gates lock in, thresholds never scale, rule A, sol = 20 min).

---

## 5. Machine JSON (for the Technical Lead; values as above)

- `atmospheric_processor.json`: `contributions: [{ "parameter": "mars:pressure", "ratePerSol": 0.15 }]`, power draw stays 3.0. Update `source` to cite this file and the sheet §2d.
- `super_greenhouse_emitter.json`: `contributions: [{ "parameter": "mars:temperature", "ratePerSol": 0.25 }]`, draw stays 3.0.
- New gate files per section 2; new `unlocks` in `phase_3_begins.json` and `breathable.json` as the ids get built.
- Unit tests: share-of-power rule applies to both (same rule as the Outgasser); processor and emitter ratePerSol in the data match the §2d table (tests compare the two).
