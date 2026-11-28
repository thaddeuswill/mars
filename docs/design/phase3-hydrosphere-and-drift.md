# Emitter-wear rewrite, Hydrosphere definition, what each late gate unlocks

**Owner:** Lead Game Designer · **Written:** 2026-11-28 · **Status:** proposal; numbers are **fluid** and unplayed · **Feeds:** `docs/design/balance-sheet.md` (drift and hydrosphere rows, edited), `pack/data/mars/` (Technical Lead builds)
**Scope (owner):** not a classroom pack; standard pace, solo. Vanilla crafting and progression stay. Make-it-work rule (`world-and-blocks.md` 0.2): short chemistry or an invented machine is allowed if labeled.
Tags: **INVENTED** (game balance), **calc** (my scratch Python, not run in the game), **SME** (needs a source from the Subject Matter Expert). Goals: A1, A3, B1, B2, B4, C1, C2, D2. Pillars: the planet is the quest log; every change is visible; pressure, not grind.

---

## 1. Emitter wear, rewritten

### 1.1 The problem (calc)
With the loss term (k = 0.004, emitter 0.275 C/sol), `leak = 0.004 x (T + 60)`.

| Fact | Number |
|---|---|
| Leak at 5 C | 0.26 C/sol |
| One emitter alone settles at | 8.75 C (0.275 / 0.004 - 60) |
| Two emitters settle at | 17.5 C |
| One emitter holds 5 C only if it gets at least | 94.5 % of its power (a storm night can break this) |
| No emitters, from 6 C, time to fall under 5 C | 3.8 sols |
| No emitters, from 5.5 C / 7 C | 1.9 sols / 7.6 sols |

So losing one of two emitters changes nothing (net +0.01 C/sol). The old "-1 C/sol" is gone. Temperature is the slow variable (heat has inertia, B2); the event cannot push it out of band "in about 1 sol" like the other events. I keep that honest and make the **margin** the thing the event tests.

### 1.2 Event: "Heater coil failure" (id stays `emitter_wear`)
| Part | Spec | Tag |
|---|---|---|
| Fiction | The coil in an emitter burns out. Real emitters would need a part too (DESIGN.md maintenance parts: heater coil serves Ovens, Outgassers, emitters) | Game design |
| When | Only while at least 1 emitter runs. **Guaranteed once per Equilibrium hold**, at a random time in sols 2 to 10 of the 14; it can also fire in Phase 3 once the first flora patch stands (drift events start then; TEAM.md log 2026-11-26). Not in Breathable alone: Breathable has no temperature condition | INVENTED |
| What breaks | **Half of the running emitters, rounded up** (1 of 1, 1 of 2, 2 of 3). Each broken emitter outputs 0 and its lamp shows the broken shape, until a **heater coil** is fitted (1 coil each, consumed) | INVENTED |
| Why this size | A player with 2 emitters sees the line go flat and learns "the spare saved me". A player who switched down to 1 emitter to save power sees cooling of -0.26 C/sol. The event rewards redundancy (B4: more emitters cost power and fauna signature) | Design |
| Effect if ignored | No emitters left: -0.26 C/sol. Out of the 5 C line in about **2 sols from 5.5 C, 4 sols from 6 C, 8 sols from 7 C** (calc). The hold counter pauses at once; it resets after more than 1 sol out (hold rule). A hold is lost about 3 to 5 sols after the event if nobody answers | calc |
| Answer | Fit a heater coil in each broken emitter: 1 to 2 minutes (walk, open, swap). Recovery after repair: about +0.3 C/sol with 2 emitters | INVENTED |
| Exception to the drift rule | The sheet says each event leaves the band in about 1 sol. This one takes 2 to 8 sols. Reason: real heat inertia. State it in the sheet | Flag |
| Heater coil item | New item, vanilla-first recipe, **INVENTED**: shapeless, 1 iron ingot + 1 coal (or charcoal). No copper (R1). Player gets 2 coils in the starter chest when Phase 2 begins; keeps a spare because the failure report asks for one | INVENTED |
| Scheduled wear | **Not built first.** The "every N sols" part interval stays open (sheet section 8). The event is the only wear mechanism in the first build | Scope |

### 1.3 Forecast F10 and the content rules
- **CR-F10.** F10 asks what happens when emitters stop. The player can change temperature another way (build a third emitter, or switch one back on), so F10 must resolve on the **leak's attributed contribution** (the loss-term line, about -0.26 C/sol), not on net temperature. Change `docs/design/messages/README.md` row F10 `resolve_source` from "-" to the loss-term source id (Technical Lead names it, for example `loss:mars:temperature`). Instructional Designer confirms.
- **CR-F9 and CR-T8.** The cause line "Warming fades without emitters." and any "about N sols to the line" estimate give F10's answer away. Both carry `reveals: F10` and stay hidden while F10 is open. F10 locks at the **event start (failure.emitter_wear delivered) or 0.5 sol**, whichever is first; the failure report is delivered after the lock, as the README already says.
- Failure report (format `DOCUMENTATION.md` 5.1, grade 6-7): **Happened:** "An emitter stopped. Its heater coil burned out." **Why:** "The coil wore out. Heat still leaks to space, so Mars cools." **Fix:** "Put a heater coil in each stopped emitter." **Next time:** "Keep a spare coil. Keep one more emitter than you need."

### 1.4 Monitor wording (grade 6-7, US spelling, fits 232 px: 34 characters or fewer per line)
| Situation | Cause line | Detail line |
|---|---|---|
| Emitter stopped, others hold | "Emitter stopped. Holding steady." | "Heat leak -0.26. Warming +0.28." |
| Emitters stopped, cooling | "Emitters stopped. Mars is cooling." | "Heat leak -0.26. Warming 0." |
| Normal leak (existing key) | "Warming fades without emitters." | "Heat leaks to space: -0.26 C/sol." |
| Repair done | "Emitter fixed. Warming is back." | none |

**Margin cue** (temperature row, shown from Phase 3 begins to the end; the line refers to the Equilibrium line, 5 C). One number plus a shape, never color alone:

| State | Rule | Text | Icon (shape) |
|---|---|---|---|
| Safe | margin >= 1.0 C and net rate >= 0 | "Margin 1.4 C above the line." | check mark |
| Thin | margin < 1.0 C, or net rate < 0 | "Margin 0.4 C. Thin." | triangle |
| Out | below the line | "0.3 C below the line." | cross |
| Cooling estimate (hidden while F10 is open, CR-T8) | net rate < 0 | "About 3 sols to the line." | clock |

**Display bug to prevent:** a value of 4.96 C rounds to "5.0" on one decimal while the gate needs 5.0. Show margin computed from the stored value and round the temperature **down** (floor) on gate rows, or show two decimals within 0.1 C of a line. This answers the Technical Lead's "resting exactly on 5 C" risk: the real cause is the one-emitter equilibrium (section 1.1) plus rounding, not the comparison itself.

### 1.5 Tuning knobs (all INVENTED, in data, never thresholds)
| Knob | First value | Raise it if | Lower it if |
|---|---|---|---|
| Fraction of running emitters lost | half, rounded up | players with 2 emitters never feel it | holds are lost to it too often |
| Guaranteed events per Equilibrium hold | 1 | F10 is not reached | players wait too much (sheet note: shorten hold to 10 sols) |
| Coils per repair | 1 | repair is trivial | repair feels like a chore |
| Starter coils | 2 | players run dry | players ignore stock |
| Heater coil recipe | 1 iron + 1 coal | n/a | coal is scarce on Mars: use charcoal from vanilla wood; SME check |
| Leak k and emitter rate | 0.004 and 0.275 (SME) | cooling is not noticed: 0.005 and 0.29 | holds feel punishing: 0.003 and 0.26 |
| Margin "thin" limit | 1.0 C | players still slip | cue nags |

---

## 2. Hydrosphere: minimum definition

### 2.1 What it measures
**Hydrosphere = how much of the planet's surface has liquid water, in percent (0 to 100).** On Mars today it is 0: there is ice, but no stable liquid. The simulation carries **one stored number** and moves it by machine rates and one loss term, exactly like temperature. **It is a machine tally, not a count of water blocks.** The world shows it through thresholds (first puddles, ponds in basins, lakes), but the Index never reads blocks. Why: counting blocks needs terrain and fluid work that does not exist (`world-and-blocks.md` section 4, open question), cannot be paused or frozen cleanly (invariant I-3), and would let a player skip a gate with a bucket (the doc already says hand water does not move the row). Trade-off: the tally can disagree with what the player sees; fix by tying visuals to the number, never the other way round.

Why 30 %: it is the game target in DESIGN.md. **Label (SME, 2026-11-28): "Game design, based on real ice."** Mars holds about 34 m of near-surface water as ice (verified, second hand); 30 % of the surface at 50 to 100 m depth is 15 to 30 m, so the target would melt much of it (calc). A former ocean's surface fraction is **NOT VERIFIED**; the journal says "may have had" and never calls 30 % the real ocean size. Earth is about 71 % ocean (common knowledge). Source: `docs/science/hydrosphere-and-gate-items.md` 1.2.

### 2.2 Sources, loss, and the dependence on warmth
| Piece | Rule (first version) | Tag |
|---|---|---|
| Source: **ice extractor** | **+0.30 %/sol each** at full power, standard. Unlocked at the Thaw. Draw **2 units/sol** (low: a pump, not a heater) | **INVENTED** |
| "Meltwater" | The extractor's product is meltwater; no second source. Melt-from-warming is already in play as ground collapse (B5) and polar release. A natural temperature-driven melt source is **not** in version 1 (it would carry Phase 3 with no player action; calc) | Design |
| Needs warmth | The extractor only runs when temperature >= -20 C (the Thaw). Below that it does nothing; the gate that unlocks it is the same line. That ties water to warmth (the reinforcing loop B2) with no new code beyond one condition | Design |
| Loss term | Same form as temperature: `loss: { form: "restore", baseline: 0, perSol: 0.01 }`: water is lost at **1 % of what exists per sol**. Note text: "freezing, vapor loss, soaking in; rate invented" | **INVENTED** k; direction real (SME verified, NTRS 20250010130 and Mars Facts) |
| What it does not do | No dependence on pressure in version 1. **Upgrade (version 2):** the loss is larger when it is cold (freezing) and when pressure is low (under 10 kPa, water boils and turns to vapor, A1); rate stays INVENTED | Later |
| Settling | One extractor settles at 30 % (0.30 / 0.01). Two give 60 %. So **one running extractor just meets Equilibrium; two give the margin**, like emitters | calc |

Per-machine summary for the sheet: extractor **0.30 %/sol, 2 units/sol, 0.15 % per unit** (INVENTED).

### 2.3 Curve with the standard build (calc, not run in game)
Temperature from the standard emitter schedule (2/3/4/5/6 at sols 0/6/14/24/34 of Phase 2; emitters cut to 2 once 0 C is reached). Extractors: 2 at the Thaw (sol 38), 3 at sol 44, 4 at sol 52, 4 kept.

| Phase 2 sol | 38 | 44 | 48 | 52 | 54 | 60 | 68 | 72 | 80 |
|---|---|---|---|---|---|---|---|---|---|
| Hydrosphere % | 0 | 3.8 | 7.1 | 10.7 | 12.8 | 19.0 | 26.7 | 30.4 | 37.2 |

So the **Hydrosphere gate (10 %) latches near sol 51**, the Phase 3 begins gate about sol 52, and 30 % arrives near sol 72 (**Phase 3 sol 20**), before the Equilibrium hold starts (about Phase 3 sol 28). The sheet said sol 46 for the 10 % gate; this is 5 sols later (conflict, section 4). Power: 4 extractors add 8 units/sol (Phase 2 peak 45 to 53; about 12 panels, up from 10): the "wide building" beat, now one more field.

### 2.4 Dependence in Phase 3 and Equilibrium
- Equilibrium needs hydrosphere >= 30 for 14 sols. One extractor holds it (the leak is 0.3 %/sol at 30 %); a **water loss** drift event is **not** added now (do not add a second slow event before the first is played).
- Hydrosphere >= 10 already means: fauna "later life stage" and raids x1.5 (balance sheet section 6), Mekanism and Create unlock (Decision Log), meltwater treatment and algae vats become placeable.
- The wetland-margin seed kit needs hydrosphere >= 10 (recipe or placement check; proposal, Technical Lead).
- Visual response (every change is visible), keyed on the stored value as the sky is keyed on pressure: 1 %: puddles that last, 5 %: ice-edge ponds, 10 %: pools in craters, 20 %: shallow lakes in basins, 30 %: rivers or wide lakes. **Art:** Lead Artist.

### 2.5 What the SME had to source (answered 2026-11-28 in `docs/science/hydrosphere-and-gate-items.md`; items 1 to 4 closed, wording applied in 2.1 and 2.2)
1. Real surface and near-surface water ice inventory (polar caps, mid-latitude ground ice) and a plain-language scale for "how much of the surface it could cover".
2. A defensible plain statement of why liquid water is lost: freezing, sublimation, evaporation, and boiling at low pressure; and whether 1 % of existing water per sol is in a sensible direction (rate is invented anyway).
3. Whether to label the 30 % target as game design or tie it to a source.
4. Whether ground-ice extraction by melting (heat plus power) is accurately named "game-sized version of a real idea" (the label rule already says so).

---

## 3. What each late gate unlocks first

Rule: **one new thing to build per gate**, so the player reaching it has a clear next step (progressive disclosure, one lever per gate). Only ids on the existing block list (`world-and-blocks.md` 3.2) are used. **Built today:** outgasser, oven, solar panel, battery, atmospheric processor, super-greenhouse emitter, water reclaimer, monitor, oxygen maker (registered, no Index effect). **Not built** are marked **BUILD**.

| Gate | First thing unlocked (placement lock) | Also available | Why it is the right first thing | Status |
|---|---|---|---|---|
| **Thaw** (-20 C, order 3) | `mars:ice_extractor` | The Monitor adds the hydrosphere row | It is the only lever for the next gate (Hydrosphere). Terrain: placed on ground ice (rule can be skipped in v1, see slice A) | **BUILD** ice extractor |
| **Hydrosphere** (10 %, order 4; **no gate file exists yet**) | `mars:meltwater_treatment` | `mars:insulation_shroud`, `mars:lure_station`, `mars:quiet_zone_marker` (fauna answers; DESIGN.md "fauna options at Hydrosphere"); Mekanism and Create (not a data unlock) | Water brings the perchlorate hazard and the later fauna stage: one answer per stage | **BUILD** all four; the first slice may stub only the treatment unit |
| **Phase 3 begins** (30 kPa and 0 C, order 5) | `mars:soil_treatment_unit` (also the seed-kit station, `phase2-machine-rates.md` section 3) | `mars:carbon_scrubber`, `mars:algae_vat` (if built), more atmospheric processors keep working | Soil first, then kits, then the first flora patch that starts drift events | **BUILD** soil unit, scrubber, algae vat |
| **Breathable** (7 sol hold, order 6) | `mars:beacon_bench` (beacon component recipes) | Components craftable: mast segments, lamps, transmitter core | Nobody should wait during holds (sheet section 4: "also available during holds") | **BUILD** bench |
| **Equilibrium** (14 sol hold, order 7) | `mars:colonization_beacon` (final assembly) | The ending | The reward is the world; the beacon is the monument | **BUILD** beacon |

**Latching needs more than unlocks.** Breathable and Equilibrium cannot latch until something raises O2 and biomass and lowers CO2 (the Technical Lead found no machine). The minimum is the **CO2-to-O2 swap** (rule A: one for one, mass kept) with two machines: **algae vat** (CO2 down, O2 up, +20 BU biomass each) and **carbon scrubber** (CO2 down into rock, no O2). That is a new mechanism (a conversion contribution that moves two parameters), not a new rate. The seed kits give biomass through flora; first version may skip kit-driven spread and let vats carry biomass to 1,500 (75 vats; too many: **keep the flora patch path or lower the 1,500 target after the first Phase 3 prototype**: open number, sheet section 8).

### Recommended build order (next 3 slices, Technical Lead)
| Slice | Content | Done when | Why this order |
|---|---|---|---|
| **A. Water** | Hydrosphere loss term (reuse temperature loss code, baseline 0, k 0.01); ice extractor machine + block + recipe (iron, furnace, hopper, bucket; no copper) with the rate and draw above; `gates/hydrosphere.json` (order 4, no hold); `thaw.json` unlocks the extractor; Monitor hydrosphere row from the Thaw with cause lines; sheet-vs-data test; ground-ice placement rule **deferred** (v1 places anywhere on regolith, flagged) | A boot check reaches Thaw, places an extractor, sees the row rise at 0.3 %/sol, Hydrosphere latches near 10 % | The Thaw is the first gate with nothing to build; this slice fixes the Phase 2 end |
| **B. Air we make** | Conversion contribution (CO2 down, O2 up, one for one); algae vat and carbon scrubber; soil unit and scrubber unlocked at `phase_3_begins`; biomass and o2 rows live; Breathable can latch in a test with the Phase 3 table values | Unit tests match balance sheet Phase 3 table; boot check latches Breathable | Without this, Breathable and Equilibrium are dead (largest risk) |
| **C. Hold and finish** | Drift event engine (data events: emitter wear first, then the rest); heater coil item and recipe; margin cue; beacon bench and beacon; Equilibrium latch and ending trigger | The wear event fires in a hold, F10 resolves on the loss source, a repaired emitter restores the margin | Events need the air slice to have holds worth holding; the ending needs the beacon |

Not in these slices: meltwater treatment, lure stations, kits, mirror uplink, mid-phase fauna work. They are a later slice; a Hydrosphere gate that unlocks nothing buildable would repeat today's problem, so slice A must at least stub the treatment unit.

---

## 4. Conflicts with the Decision Log and docs (proposed, nothing edited in TEAM.md or DESIGN.md)

| # | Conflict | Proposal |
|---|---|---|
| D-1 | DESIGN.md and the sheet (section 4): every drift event leaves the band in about 1 sol. The wear event takes 2 to 8 sols | Add "temperature events are slow (heat inertia)" to DESIGN.md drift text. Owner or coordinator |
| D-2 | Sheet section 4 and DESIGN.md: "Emitter wear: temperature -1 C/sol" | Replaced in the sheet drift row (done). DESIGN.md line to follow |
| D-3 | `docs/design/messages/README.md` F10 `resolve_source` is "-" (net value, "player cannot move it another way"). With one more emitter possible, CR-F10 requires a source | Set to the loss-term source. Instructional Designer |
| D-4 | Sheet gate curve: Hydrosphere at about sol 46 and 16 % at sol 54; this model gives sol 51 and 12.8 % | Sheet curve row updated (done); Phase 2 curve is a sketch, retune after play |
| D-5 | DESIGN.md Hydrosphere sources list "ice-comet impacts" | Not in version 1; keep in DESIGN.md as a later source |
| D-6 | `world-and-blocks.md` section 4: Hydrosphere "defined by what the world shows (water blocks counted, or machine tally)" | This doc picks the machine tally. The Technical Lead and the owner confirm |
| D-7 | SME doc says Breathable (7 sols) is affected by temperature drift; `breathable.json` has no temperature condition | Only Equilibrium needs 5 C. No change to the gate; fix the SME wording |
| D-8 | Decision Log: Mekanism and Create unlock at Hydrosphere, but no `hydrosphere.json` exists and `DESIGN.md` gate table lists it | Slice A adds the gate file |
| D-9 | `phase2-machine-rates.md` sections 1.4 and 2: panel counts (10 and 12) and the gate table omit the extractor draw | Update power table: +8 units/sol, 12 panels in Phase 2 peak |

No contradiction with the settled decisions (75 kPa, gates lock in, thresholds never scale, rule A, sol = 20 min).

## 5. Assumptions and what would sharpen this
- The standard emitter schedule is unplayed. If emitters finish late, the Thaw slides and the extractor starts later.
- Extractor draw 2 and rate 0.30 are untested; first playtest measures sols between Thaw and Hydrosphere (target 10 to 14).
- A playtest should also record whether anyone loses an Equilibrium hold to the wear event, and whether the margin cue is read (log the hover time on the row).
- Ill-structured parts: how bitey the wear event is; whether a machine tally is honest enough. Routed in the report.
