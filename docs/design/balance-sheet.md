# Balance Sheet — Mars Terraforming

**Owner:** Lead Game Designer · **Status:** Draft 1 (2026-11-26), numbers are **fluid** (TEAM.md 8.1) and are starting values for the Stage 2 prototype · **Depends on:** [DESIGN.md](../../DESIGN.md), [docs/science/accuracy-review.md](../science/accuracy-review.md)

Numbers and curves for the Planetary Index, pacing, holds, fauna, and group play. Nothing here has been playtested. Each table says what it assumes so the prototype can prove it wrong.

**Rules this sheet follows**
- **Every number is tagged** (SME round 2, value audit): *sourced* (from `docs/science/`), *calc* (arithmetic on sourced values), or *invented* (a game balance number, with its reasoning). Untagged numbers in this sheet are invented balance numbers.
- **Physical thresholds never scale.** Triple point, 5 kPa, 30 kPa, 0 °C, −20 °C, the breathability condition, and the equilibrium band are the same in every mode and group size. Classroom mode and group size change **rates** and **hold times** only (SME accuracy review, finding 3; consistency review M-02).
- **Time unit: the sol.** One sol = one in-game day = 20 real minutes of *simulation time*. Simulation time stops when the world is paused (definition owned by the Technical Lead, M-05). Standard: 3 sols per real hour.

---

## 1. Planetary Index: values and units

| Parameter | Unit | Start | Equilibrium target | Notes |
|---|---|---|---|---|
| Pressure | kPa | 0.6 | ≥ 75 (user confirmed 2026-11-26) | SME option B: lets ~22 % O2 give ~16 kPa of O2 |
| Temperature | °C (global mean) | −60 | ≥ 5 | Local temperature adds day/night and machine heat |
| Hydrosphere | % of surface covered by liquid water | 0 | ≥ 30 | Mars has lots of ice; 0 % means no *liquid* |
| O2 fraction | % | 0.1 | 19–23 (fire-safety band) | Above 23 %: fire hazard |
| CO2 | partial pressure, kPa | 0.57 (95 % of 0.6) | ≤ 0.5 | Shown as both % and kPa |
| Biomass | biomass units (BU) | 0 | ≥ 1,500 (draft) | 1 BU = one established flora block; one algae vat = 20 BU |

**Derived values the Monitor shows:** O2 partial pressure (pressure × O2 fraction), CO2 partial pressure, and the boiling point of water at the current pressure (from the SME's Antoine table).

**Breathable condition (live check):** O2 partial pressure ≥ 16 kPa **and** pressure ≥ 75 kPa **and** CO2 partial pressure ≤ 0.5 kPa.

| At 75 kPa, O2 fraction | O2 partial pressure | Breathable? | Fire band? |
|---|---|---|---|
| 19 % | 14.3 kPa | No | In band |
| 21 % | 15.8 kPa | Just under (rounding: tune to 21.5 %) | In band |
| 22 % | 16.5 kPa | Yes | In band |
| 23 % | 17.3 kPa | Yes | Edge |
| 24 % | 18.0 kPa | Yes | **Out**: fires |

So the real target is **O2 21.5–23 % at ≥ 75 kPa**. The window is narrow on purpose: more pressure widens it (at 85 kPa, 19 % already gives 16.2 kPa). That is a real B4 trade-off the player can choose: push pressure higher, or hold O2 tighter.

---

## 2. Phase 1 pressure (fixes C-01)

**Sources in Phase 1**

| Source | Unlocked by | Contribution (standard, per sol) | Power | Also gives |
|---|---|---|---|---|
| Regolith Oven (2 in the starting dome) | Start | +0.01 kPa each | Low | Water for the dome (bound water baked out of soil) |
| **Regolith Outgasser** | **Dome stable** | +0.10 kPa each | Medium (solar or battery) | Nothing else: a pure terraforming machine, the player's first |
| Polar frost (reversible reservoir) | Always | Takes −2 % of (pressure − 0.6) per sol below −40 °C; −1 % from −40 to −20 °C. Above −20 °C: returns what it took at +0.5 kPa/sol until empty, then up to **0.5 kPa** more from buried polar CO2 | — | *Invented* rates and thresholds; direction *sourced* (more CO2 raises the frost point; seasonal caps hold ~25–30 % of the air, SME round 2 [S3]); 0.5 kPa buried CO2 *calc* from [S4]. Balancing when cold, reinforcing when warm (B2) |

Dust storms cut solar output by 60 % × storm cap (*invented* balance number; real storms vary, and the 2018 storm ended Opportunity's mission). Outgassers slow.

**Storm caps scale gameplay and look (decided, answers the Technical Lead).** The storm cap (Phase 1: 1.0, Phase 2: 0.8, Phase 3: 0.5, Equilibrium: 0.25; Artist's values, ARCHITECTURE.md §2.5) multiplies the solar cut and the dust wear as well as the visuals, so "storms weaken as the air thickens" is true in the mechanics (B2). At cap 0.25 a storm cuts solar by 15 %. *Invented.* Revisit if late storms stop mattering as drift events (then raise the Phase 3 floor). Unmaintained Outgassers lose output (mechanical wear).

**Standard solo curve** (assumed build order: first Outgasser at Dome stable on sol 9, then 2 by sol 11, 3 by sol 13, 4 by sol 16; one 2-sol storm on sols 14–15):

| Sol | 1 | 5 | 9 | 10 | 12 | 14 | 16 | 18 | 20 | 22 | 24 | 25 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| Pressure (kPa) | 0.61 | 0.67 | 0.74 | 0.86 | 1.18 | 1.70 | 1.93 | 2.71 | 3.46 | 4.18 | 4.87 | **5.20** |
| Real hours | 0.3 | 1.7 | 3.0 | 3.3 | 4.0 | 4.7 | 5.3 | 6.0 | 6.7 | 7.3 | 8.0 | 8.3 |

Phase 2 begins at about 8.3 h, on target (~8 h). The curve is flat before Dome stable (the Ovens show the player that cause and effect exist), bends up as Outgassers come online, dips in the storm, and slows slightly near 5 kPa because frost loss grows. Every bend has a cause the Monitor can name.

**Stage 3 update (proposed):** §2d replaces the stand-in below with a real condition (sealed, air, water, power) and puts Dome stable at about sol 9.2 (3.07 h) solo, so this curve's timing holds again.

**Stage 2 build note (slice 4):** Dome stable is built as a 3-sol hold on a stand-in machine state, "the power grid has supply and covers demand" (no oxygen maker or sealing exists yet). With one clean panel a player reaches it after 3 sols, about 1 h of play, not the about 3 h of the curve below, so the curve timing is not reproduced until life support is a real loop. Phase 2 begins fires at 5.0 kPa in every mode.

**Why Dome stable unlocks the Outgasser:** survive first, then terraform. It keeps Phase 1 about life support (C1) until the dome is steady, then hands the player the first lever on the planet (B1). The relay beat (story beat 6) also fires at Dome stable, so the player gets people and a planet lever at the same moment.

**Power order before the Outgasser (design question, 2026-11-27; open numbers marked).** The Outgasser needs Medium power, so the build order is solar panel, then battery, then Outgasser. This does not contradict DESIGN.md: solar panel and battery are already part of life support and the Landing checklist (step 6, battery), so a player has both before Dome stable; the Outgasser then needs **extra** supply on top of life support, and the battery covers its night (Hana's note "power for the night"). Two corrections to the wording "first session": (1) the Outgasser is **not** in the first session; Landing is 0.4 h and Dome stable is about 3 h (sol 9 in the standard curve above), so it is the end of the first sitting at the earliest; (2) the plan's slice 3 must make "demand above supply" visible (Monitor power line, B1 cause "Outgassers slowed: low power") or a player will think the machine is broken. **Open numbers (all unset, invented when set):** panels needed per Outgasser, battery size for one night, and how an unpowered Outgasser behaves (recommend: output scales with power satisfied, as slice 3 plans, never a hard stop). Check: Phase 1 curve assumes about 91 % panel output (§ assumptions); with 4 Outgassers by sol 16 the power the player must build is a second, hidden pacing gate, so measure it in the slice 3 prototype before trusting the 8 h Phase 1 target. Goals: C1, B1.

---

## 2a. Solar panel dust (approved draft; SME refinements from docs/science/dust-review.md)

Tags: **sourced** (opened), **searched** (search summary, not opened), **calc** (arithmetic), **invented** (game balance). No dust number appears in player text except in the journal notes the SME wrote (DESIGN.md, Solar panel dust).

| Item | Value | Tag and reasoning |
|---|---|---|
| Real accumulation (reference only) | About 0.2 % of output per sol; range 0.05–2 % | **Searched** (Lorenz et al. 2021 abstract). InSight 5,000 → 500 Wh/sol over ~1,230 sols ≈ 0.19 %/sol compounding: **sourced** (PIA25284) + **calc** |
| Stage interval, Phase 1 | 3 sols per stage | **Invented.** Game rate ≈ 3.3 % per sol (**calc**), **about 15 times faster than real**; the journal says so. At the real rate an uncleaned panel would lose only ~24 % over a 120-sol game (**calc**, SME), invisible in play |
| Later phases (slower) | Interval ÷ storm cap: Phase 2 3.75 sols, Phase 3 6, Equilibrium 12 | **Invented**; ≈ 1.7 %/sol in Phase 3, 0.8 %/sol at Equilibrium (**calc**), easing toward real as storms weaken |
| Classroom (×4) | Interval ÷ 4 (0.75 sol in Phase 1) | **Invented**; same proportions, about 65 times real (**calc**); at most one cleaning trip per class period |
| Storm adds | +1 stage at storm end; +2 if storm cap ≥ 0.8 | **Invented.** Timing is a design choice for the F1 lock point; real fallout continues as storms fade (SME) |
| Heavy machinery and exhaust | ×2 dust rate within 8 blocks of the player's digging, heavy machinery, or machine exhaust | **Invented**; plausible (landing plumes are the only real support, **searched**) |
| Output by stage | 0: 100 % · 1: 92 % · 2: 82 % · 3: 70 % · 4: 60 % (floor) | **Invented.** Spirit stood near 60 % before its sol 420 event (**searched**); the floor is **optimistic** against InSight's fall to about 10 % (journal note) |
| Partly cleaned (dust devil) | Recovers **half the lost output**: from stage 4 (60 %) to 80 %; from stage 3 (70 %) to 85 %; from stage 2 (82 %) to 91 % | **Invented** formula; inside real event sizes (+5–10 points Opportunity, ~+33 points Spirit sol 420: **sourced**/**searched**) |
| Dust devil chance | **1 % per sol per panel group** (Phases 1–2); only panels at stage ≥ 2 change | **Invented**; real Spirit ≈ 0.15 %/sol (**calc** from 3 events in ~2,000 sols); about one event per standard game (**calc**) |
| Phase 3 rain | Panels under open sky go to stage 1 or partly cleaned, whichever is cleaner (never stage 0) | **Invented**; direction from Earth (rain leaves fine dust, **searched**); no Mars analogue |
| With a storm | Output = stage factor × (1 − 0.6 × storm cap) | **Invented.** Phase 1 worst case 24 %. Real storms: Opportunity −80 % (2007), −97 % (2018); InSight regional storm about −35 % (**searched**) |
| Panel brush | Back to stage 0; ~2 s per panel; 64 uses | **Invented**; fiction (no Mars mission flew a brush) |
| Panel wiper (from Phase 2) | Keeps a row at ≤ stage 1; 1 power unit per sol per panel; one bearing every 10 sols | **Invented**; fiction |
| Phase 1 curve assumption | Panels cleaned about every 6 sols (average output ≈ 91 %) | **Calc** from the stage table; §2 curve holds only if power stays above Outgasser demand; measure in the prototype |

## 2b. Power: solar panel, battery, Outgasser draw (Stage 2 slice 3)

One shared grid per planet. Unit: **1 power unit = 1 kWh**; rates are units per sol. Power is physical, so pace never scales it (only the machines' planet effect is scaled). Not modelled yet: night, per-block wiring, storms on the grid (slice 5 and later).

| Item | Value | Tag and reasoning |
|---|---|---|
| Clean solar panel supply | 5 power units per sol | **Sourced** anchor: InSight's array made about 5,000 Wh per sol at landing (PIA25284, see §2a); the game panel is sized like it |
| Panel output by dust stage | stage factor from §2a (100, 92, 82, 70, 60 %) | Same table as §2a; a stage 4 panel makes 3.0 units per sol |
| Regolith Outgasser draw | 4 power units per sol | **Invented.** "Medium" in §2. One clean panel runs one Outgasser with 1 unit per sol spare |
| Battery capacity | 10 power units per battery | **Invented.** 2.5 sols of one Outgasser, so a dust-storm sol is survivable |
| Sharing rule | Supply first, then battery; a shortfall cuts every powered machine by the same share | **Design choice.** Surplus charges batteries |
| Effect when short | Outgasser pressure rate x share of power met | Simple and visible: the Monitor's Power row says "No power" or "Not enough power" |

Worked example (check against `PowerGridTest`): one Outgasser, one clean panel: 5 of 4 units, full rate, +0.10 kPa/sol. Two Outgassers, one clean panel: 5 of 8 units, 62.5 % of the rate, +0.125 kPa/sol in total. One Outgasser on a stage 4 panel: 3.0 of 4 units, 75 %.

## 2c. Slice 5: polar-frost sink, Regolith Oven, shortfall, holds (status: **owner-accepted 2026-11-27**, proposed by the Game Designer; owner scope: all three temperature branches as data and unit tests, nothing tuned in play)

Tags as in §2a: **sourced**, **calc**, **invented** (game balance), **design choice**. Science wording for the sink is the SME's (handoff in the report). Goals: B1 (causes visible), B2 (balancing feedback), C1 (survive first). Pillars: the planet is the quest log; your progress provokes the planet; pressure, not grind.

### Decision 1. Regolith Oven draw and under-power behaviour

| Item | Value | Tag and reasoning |
|---|---|---|
| Oven draw | **1 power unit per sol** each | **Invented.** "Low" in §2; one quarter of the Outgasser's 4 ("Medium"), one fifth of a clean panel. Two Ovens use 2 of 5 units, so the starting grid works with one panel and leaves 3 spare |
| Oven pressure rate | +0.01 kPa per sol each at full power, standard pace | From §2 (existing). Scaled by pace and group like every source |
| Under-power | Rate x share of power met, **the same rule as the Outgasser** | **Design choice.** One rule for all machines: a player learns it once (B1). Never a hard stop. The Monitor shows the existing "low power" or "needs power" suffix on the Oven line |
| Machine JSON | `regolith_oven.json`: `ratePerSol` 0.01, `power.drawPerSol` 1.0 | Same shape as `regolith_outgasser.json`; Technical Lead builds |

Effect on §2b worked examples: one Outgasser plus two Ovens needs 6 units; one clean panel makes 5, so the share is 83 % and the total is +0.10 kPa/sol (not 0.12). The slice 3 examples (Outgasser only) are unchanged; add this one to `PowerGridTest`. The player's lesson: a second panel is worth building before the second Outgasser.

### Decision 2. Where the two starting Ovens come from

**Both Ovens are in the player's inventory on first arrival (one grant per world, not per player), and the player places them.** Reasons: there is no dome structure to pre-place them in (dome build is later work); placing teaches "a machine needs the grid" (C1) at a time when nothing can go wrong; a pre-placed machine would be an invisible cause (breaks "every change is visible"). They are also craftable from the start (cheap recipe, KubeJS, so a lost Oven is not a dead end). The Landing checklist step 6 ("battery") becomes "battery and Ovens: place them and see the Power row" (handoff to the Instructional Designer; Landing is **stable-ish**, so it is a proposal). Dome stable does not require Ovens in slice 5; its stand-in stays "the grid covers demand".

*Risk:* a player who never places them sees a flat 0.60 kPa. Mitigation: the Monitor's cause line says "Nothing is changing this right now." (existing key), and the Next goal line points at pressure.

### Decision 3. Shortfall priority

| Stage | Rule |
|---|---|
| **Slice 5 (now)** | **Equal share for every load**, as in §2b. No change to `PowerGrid`. Reason: no life-support machine draws from the grid yet, so a priority rule has nothing to rank. In survival the Outgasser cannot be placed before Dome stable, so the Dome stable stand-in cannot be starved by the Outgasser |
| **When life support is a real load (later slice)** | Three tiers: **1 life support** (never cut while any lower tier draws), **2 Ovens**, **3 Outgassers and later terraforming machines**. Equal share inside a tier. Add a `priority` integer to machine JSON then, not now |

Why: C1 and the C1/B1 order "survive first, then terraform". A shortfall that slows the Outgasser first teaches the supply and demand lesson (B1) without punishing the 12-year-old's survival. Tier 2 sits above the Outgasser because the Oven also makes dome water (Decision 6) and draws little. **Design choice**; revisit if a playtest shows players cannot tell which machine was cut (then the Monitor names it).

### Decision 4. Stalling below 5 kPa is intended; what the player is told

Equilibrium with only the cold branch: sink = 0.02 x (P - 0.6) per sol, so P_eq = 0.6 + 50 x S, where S is the net source in kPa/sol (**calc**, Producer's arithmetic, rechecked here; not run in the game).

| Build | S (kPa/sol) | P_eq (kPa) | Reaches 5 kPa? |
|---|---|---|---|
| 2 Ovens only, full power | 0.02 | 1.6 | No (the flat start; expected before Dome stable) |
| 1 Outgasser, 1 clean panel, no Ovens | 0.10 | 5.6 | Yes, slowly (margin 0.6) |
| 1 Outgasser, 2 Ovens, 1 clean panel (share 83 %) | 0.10 | 5.6 | Yes, slowly |
| 1 Outgasser on a stage 4 panel (60 %), no Ovens | 0.075 | 4.35 | **No** |
| 1 Outgasser, 2 Ovens, stage 2 panel (82 %) | 0.082 | 4.7 | **No** |
| 2 Outgassers, 2 Ovens, 2 clean panels (full) | 0.22 | 11.6 | Yes, much faster (the net source is 2.2 x) |

**Decision: stalling with one machine is intended.** The sink exists to teach B2 (a balancing feedback: the more air, the more the frost takes back), and the Phase 1 curve (§2) already assumes four Outgassers by sol 16. A single weak machine cannot finish Phase 1; the fix is a second machine or a cleaner panel, both of which the player can see and do. This is "pressure needs upkeep", not grind. Guard: **the gate is never lowered** (5 kPa is physical; thresholds never scale). If a playtest shows 12-year-olds stuck, the first lever is the sink rate (2 % to 1.5 %: then one clean-panel Outgasser stalls at about 7.3 kPa; **calc**), not the gate. Not tuned in play (owner scope).

**What the player is told** (Monitor, wording in ui-specs §9): when the 1-sol net trend is below 0.005 kPa/sol (*invented*) and the goal is not reached, the Next goal estimate reads "Levelling off: the frost takes back about as much as your machines add." plus the existing cause lines. No equilibrium number in the default text (grade 6-7); the high-school read-more adds "The two balance near {P_eq} kPa. More machines move the balance up." This replaces "Not rising: check causes" for this case. Goals: B1, B2.

(Decision 5, the cause-line wording, is in `docs/design/ui-specs.md` §9.)

### Decision 6. Oven water is out of slice 5

Hydrosphere and dome water are not built. Name it, do not hide it: the Oven tooltip and journal say "Bakes soil and releases a little gas. Water comes later." No fake water number. The Oven keeps its "also gives water" row in §2 as a Stage 3 item (dome systems, goals C1, A2).

### Frost sink model (data and unit tests; Technical Lead builds, SME confirms semantics)

| Item | Value | Tag |
|---|---|---|
| Baseline pressure | `baselineKpa` = 0.6, a **per-planet data field**, not a constant. The sink acts on total pressure above baseline in Stage 2 (SME: an acceptable approximation, over-applies up to about 40 % in Phase 2); switch to CO2 partial pressure when it is tracked | **Design choice**; planets are per dimension |
| Cold branch | T < -40 C: takes 2 % of (P - baseline) per sol. Zero when P <= baseline; never adds air | From §2; rate **invented** |
| Mild branch | -40 C <= T <= -20 C: takes 1 % per sol | From §2; **invented**; boundary inclusion is a design choice, unit-test both sides |
| Release branch | T > -20 C: returns the stored amount at +0.5 kPa/sol until empty, then up to 0.5 kPa more from buried polar CO2, once | From §2; 0.5 kPa buried CO2 **calc** from [S4]; rate **invented** |
| Reservoir | Stored kPa taken so far, saved, never latches; paused session leaves it unchanged | Needs schema bump (Technical Lead) |
| Pace and group scaling | Sink fraction and release rate are rates, so **both scale (SME confirmed: honest and required, review Q2) with pace and group like the sources**; the buried 0.5 kPa amount does not scale. Equilibrium is then identical in all modes (P_eq above does not depend on the scale) | **Design choice**; SME wording for journal and educator guide is in the review Q2; never say "the frost reacts faster in classroom mode" |
| Reachable in play | Cold branch only (temperature stays at -60 C until a temperature source exists). Mild and release: `/mars index set temperature` and unit tests | Owner scope |
| Curve check | Ovens alone give P(t) = 1.6 - e^(-0.02 t): 0.77 at sol 9 (**calc**) against 0.74 in the §2 curve, within 0.03 kPa. SME recomputed sols 16 to 25 as consistent with a -2 % sink but could not reproduce sols 1 to 16 (`docs/team/reports/2026-11-27-sme-frost-sink.md`), so treat the early cells as **unverified**; the Technical Lead compares the prototype curve cell by cell. Dome stable timing already differs (slice 4 note) | **calc**, not run; early cells unverified |

### Decision 7. One hold-failure rule for every hold

**Pause, then reset.** Applies to Dome stable, Breathable and Equilibrium. The counter **pauses** when the condition fails. It **resets to zero** only if the failure lasts more than **1 continuous sol** (classroom: **0.5 sol**, which is 10 minutes of play). This is the §4 rule, extended to Dome stable. Reasons: ages 12-18 and "attainable, balanced goal" (DESIGN.md); a night flicker in power must not erase 2 sols of work; Dome stable is a machine-state hold where one brief shortfall is likely; it is what the Technical Lead built in slice 4. The ui-specs "failure resets at once" wording is retired. The grace timer is data (`resetAfterSols`, classroom value from the pace preset) and is a rate-like scale, not a threshold. Revisit if playtesters never lose a count (then the pause is too kind and the lesson in C1 and B2 is lost) or if classroom pace makes 0.5 sol too short to read a message.

---

## 2d. Life support and the real Dome stable (Stage 3 slice 2; input G2; status: **owner-accepted** 2026-11-27 (all recommendations, BACKLOG); SME science check done, tags below; numbers changed 2026-11-27: O2 use 8, maker 10, start fill 14, reserve 24)

Replaces the Dome stable stand-in (§2 build note). Tags as in §2a: **sourced**, **calc**, **invented** (game balance), **unverified**, **design choice**. All arithmetic below is **calc**: done by hand and checked with a throwaway script outside the game; **not run in the game or in the prototype**. Goals: C1 (supply and demand, survive first), C2 (failure and repair), B1 (every change visible), A1 (why the air matters). Pillars: pressure, not grind; compact before wide; every change is visible. Layout the numbers assume: [dome-layout.md](dome-layout.md).

**Rules this section keeps.** Physical thresholds never scale: the 75 kPa, 5 kPa, 30 kPa, 0 C and breathability numbers are untouched here. Classroom mode and group size change **rates of terraforming, hold times and recipe cost**. The dome's survival numbers (use per player per sol, maker output, tank sizes, leak) are **per-sol physical numbers and do not scale with pace** (a sol is 20 simulation minutes in every mode; scaling them would make a classroom player breathe four times faster). They do scale with **`group.size`** (the configured group, balance-sheet §5.1), **not** with who is online. (DESIGN.md now says "configured group size", matching §5.1.)

**Units.** 1 O2 unit = 100 g of oxygen. 1 water unit = 1 L. 1 power unit = 1 kWh (§2b). 1 ration = one player-day of food.

### 2d.1 Use, per player per sol

| Item | Value | Tag and reasoning |
|---|---|---|
| Oxygen use | **8 O2 units** (0.8 kg) | **Verified-range, rounded**: NASA planning value 0.84 kg per person per Earth day = 0.86 kg per sol (calc, x 24.6 / 24); the game is 7 % under. The NASA figure is **search-summary** (SME could not open the page; open before the journal says "verified"). The old 0.7 came from a rounded "about 1 tonne for 4 for a year" and was 19 % low (`docs/science/life-support-review.md` 2.1) |
| Water use (drinking and food) | **3 water units** | **Verified as a range**: 2.4 to 3.5 L a day for drinking and food (NASA documents, search-summary); 3 is a game number inside it. Hygiene and laundry water is left out on purpose (**game rule**; the journal says so) |
| Food | 1 ration per player per sol; start stock 40 per player | **Stub, invented.** Never fails inside Phase 1 slice 2 and does not take part in Dome stable. The greenhouse (asset F-08) makes food real later |

### 2d.2 Machines and tanks

| Item | Value | Tag and reasoning |
|---|---|---|
| **Oxygen maker** output | **10 O2 units per sol** each (1.0 kg) | **Invented**, sized from a **calc**: MOXIE best 12 g/h (NASA, verified) x 3 x 24.6 h = 886 g = about 9 units; raised to 10 so one maker per player keeps a net of +2 against use 8. A person needs about 2.9 MOXIEs at best rate |
| Oxygen maker power | **1.0 power unit per sol** each | **Invented, deliberately compressed (game rule: power discount).** MOXIE draws about 300 W and makes up to 12 g/h (**verified**, NASA), about 25 kWh per kg; the game uses 1 kWh for 1.0 kg, about 22 times cheaper. It is also **below the physical minimum of about 5 kWh per kg of O2** (calc, textbook enthalpies, unverified): no machine could do it, so this is a discount on energy, not better engineering. Why: one clean panel (5 units, §2b) could not run even one real-ratio maker (about 4 panels per player would swamp Phase 1). Owner-accepted as a labelled simplification; the journal wording is in `life-support-review.md` section 3 and must say "a discount for the game; real oxygen needs a lot of power" (transfer risk: "oxygen on Mars is easy") |
| Rule of thumb the player learns | **One oxygen maker per player.** 10 made against 8 used: each maker leaves 2 units per sol over | **design choice**; the Monitor's O2 line shows made, used, and net |
| Water reclaimer | One unit serves up to 5 players. Takes back **90 %** of the water used; power **1.0** per sol | Recovery 90 % is **invented, below ISS** (ISS 93 to 94 % before the Brine Processor, about 98 % after; NASA, search-summary); fair for a new small system and the journal says "lower than ISS". Power invented. Loss = 10 % x 3 = **0.3 per player per sol** |
| Regolith Oven water (§2 "also gives water", §2c decision 6) | **+0.5 water unit per sol** each, at full power; scaled by power share like every machine | **Invented, plausible** (SME): Mars soil holds about 2 % water by weight (Curiosity, NASA, search-summary), so 0.5 L needs about 25 kg of soil a sol (calc); heating it is about 2.8 kWh (calc, unverified constants) against the game's 1.0. Journal: "a few percent, locked in minerals; ice is farther north", not "wet soil". Two Ovens give 1.0, which covers the loss of 3 players (0.9). **Dome water only: it is a separate dome stock and is never added to the hydrosphere index** (checked 2026-11-27: `regolith_oven.json` contributes pressure only, `hydrosphere.json` has no Oven source, and 2c decision 6 says no water is built yet; this rule stops a double count when it is) |
| O2 wall tank | **40 O2 units** each, 5 tanks, capacity **200** (one tank beside each bunk) | **Invented.** 200 units is 5.0 sols of full use for 5 players (200 / 40), 25 sols for one (200 / 8) |
| Water tank | **60 units** each, 2 tanks, capacity **120** | **Invented** |
| **Start fill** | O2: **14 units per player** (1.75 sols of use). Water: **60**. Battery: **5** | **Invented** ("the storm drained the tanks"). Scales with `group.size`, so every group starts with the same time to act |
| Suit O2 | Tier 1: **6 units** (about 17 minutes of play at 20 min per sol); tier 2 (unlocked by Dome stable): **10 units** (about 29 minutes). Refill at the suit points is **free** but needs a non-empty wall tank | **Invented, time compression** (SME): real suits are certified 7 h (8 h nominal), so the game suit is about 25 times shorter in play time and about 3 times more generous against a day's air need; the journal says "17 minutes of your play time". Free refill is a **game rule** and a design choice (does not punish exploring); revisit if trips never feel tense |
| Leak while a required volume is unsealed | Wall tanks lose an **extra 10 O2 units per sol** | **Invented, game rule** (SME): 1 kg of O2 a day is about 4.4 kg of air, 3 to 5 times the worst ISS crack, and the game ignores the air already in the room (about 190 kg of O2, calc) and the CO2 scrubber; the journal says so. Equal to the surplus of a full group of 5 (5 x 2 = 10 per sol), so a breach erases the margin |
| Machines in the dome at start (group n) | Makers pre-built: **ceil(n/2)**; reclaimer 1; battery 1 (charged 5); panel 1 at dust stage 1 (92 %); Ovens: 2 in inventory (§2c) | **Design choice.** The shelter "was built for the whole crew but only half the makers survived the storm"; players build the rest, so a bigger group builds more |

### 2d.3 Costs of recipes (KubeJS; item names are stand-ins, the Technical Lead maps them)

Material stand-ins: Fe (iron plate), W (wire), G (glass), B (regolith brick). **All counts invented.** A Phase 1 raw-resource list does not exist yet (open item G4 for the Technical Lead and Artist). Classroom preset is about 23 % cheaper (divide by 1.3, §5.3, rounded up).

| Machine | Standard | Classroom | Power per sol | Tier |
|---|---|---|---|---|
| Oxygen maker | 6 Fe, 3 W, 2 G | 5 Fe, 3 W, 2 G | 1.0 | 1 |
| Water reclaimer | 6 Fe, 2 W, 4 G | 5 Fe, 2 W, 4 G | 1.0 | 1 |
| Solar panel (round array) | 6 Fe, 4 W, 8 G | 5 Fe, 4 W, 7 G | makes 5 | none |
| Battery | 4 Fe, 6 W | 4 Fe, 5 W | stores 10 | none |
| Regolith Oven | 3 Fe, 2 W, 5 B | 3 Fe, 2 W, 4 B | 1.0 | 2 |
| Regolith Outgasser | 10 Fe, 6 W, 6 B, 2 G | 8 Fe, 5 W, 5 B, 2 G | 4.0 | 3 |
| Seal kit | 1 Fe, 1 B | 1 Fe, 1 B | none | n/a |

Time check (calc, **invented** gather rate of 4 items per minute for an experienced player, 2 for a new one): one extra oxygen maker is 11 items: about 3 minutes (0.14 sol) or 6 minutes (0.28 sol). A second panel plus two makers is 18 Fe, 10 W, 12 G = 40 items: 10 minutes (0.5 sol) or 20 minutes (1 sol) for one player. The schedule in §2d.7 assumes 0.6 sol for the first extra maker and 0.4 sol more for each one after.

### 2d.4 Power: shortfall priority tiers (extends §2c decision 3)

`priority` is an integer in machine JSON: **1** life support (oxygen maker, reclaimer), **2** Regolith Oven, **3** Outgasser and every later terraforming machine. Tier 1 is never cut while any lower tier draws. Equal share inside a tier; a tier gets what is left after the tiers above it (supply first, then battery; same rule as §2b).

| Group n | Tier 1 draw (n makers + reclaimer) | Tier 2 (2 Ovens) | Total | Panels at about 91 % (4.55 each) | With 1 Outgasser (+4) | Panels |
|---|---|---|---|---|---|---|
| 1 | 2 | 2 | 4 | 1 | 8 | 2 |
| 2 | 3 | 2 | 5 | 2 | 9 | 2 |
| 3 | 4 | 2 | 6 | 2 | 10 | 3 |
| 4 | 5 | 2 | 7 | 2 | 11 | 3 |
| 5 | 6 | 2 | 8 | 2 | 12 | 3 |

Reading it: a second panel comes before the Outgasser for everyone (§2 "power order"), and a group of 4 or 5 needs it before the fourth maker (tier 1 of 5 or 6 beats one panel's 4.6). While tier 1 is short, the makers run at the share they get, so O2 net falls: the Monitor names it ("Oxygen makers slowed: low power"). Ovens drop first, so the pressure line flattens before the air does, and the cause line says why.

Storm check (calc; a storm cuts a panel by 60 %, §2): 2 panels at stage 1 give 2 x 5 x 0.92 x 0.4 = 3.7 units; a group of 5 needs 6 for tier 1: shortfall 2.3 per sol; a full battery (10) lasts 4.3 sols, so the §2 two-sol storm is survivable with a charged battery. Ovens and Outgassers get nothing meanwhile, which is the lesson (B1, C1).

### 2d.5 Dome stable as a real condition (replaces `life_support_ok` stand-in)

Four machine states, all must be true, then the existing hold (3 sols; classroom 1) with the existing hold rule (§2c decision 7: pause on failure, reset after more than 1 continuous sol; classroom 0.5). Splitting into four named states lets the Monitor say which one is failing.

| State | True when | Plain words for the player |
|---|---|---|
| `sealed` | Every volume holding a bunk, tank, maker, reclaimer, battery or the Monitor is sealed (dome-layout §5) | The dome holds air |
| `air_ok` | O2 made at the present power is at least what the group uses (net 0 or more) **and** stored O2 is at least **reserveSols x use** (reserveSols: **3**, classroom **1**) | The air is made as fast as it is used, with a reserve |
| `water_ok` | The reclaimer is running and the water lasts at least 10 sols at the present net (or is not falling) | The water is looked after |
| `power_ok` | Every tier 1 load is fully powered **and** the battery holds at least one sol of tier 1 draw | Life support has power, and a night's backup |

The reserve is the part that makes the timing: the start fill is 14 units per player and the reserve needed is 24 per player (3 sols x use 8), so the tank must refill by 10 per player. With every maker built the net is +2 per player per sol (10 made, 8 used), so that is **5 sols whatever the group size** (calc; the same as before the change, because use, maker output and start fill all moved together). **Classroom:** reserveSols drops to 1 (8 units per player), which the start fill (14) already exceeds, so the classroom reaches the condition as soon as the makers and panels are built. reserveSols is a **pacing requirement like a hold, not a physical threshold** (design choice and owner question 3): the 75 kPa, breathability and every Index threshold stay as they are.

Gate JSON (proposal for the Technical Lead; the data keeps the §2c rule "balance in JSON, tests compare"): `dome_stable.json` gets `condition: { "all_of": [ {"machine_state":"sealed"}, {"machine_state":"air_ok"}, {"machine_state":"water_ok"}, {"machine_state":"power_ok"} ] }`, hold unchanged (3 / 1, reset 1.0 / 0.5). `PlanetSim.lifeSupportOk()` is retired. New data: `machines/oxygen_maker.json`, `machines/water_reclaimer.json` (`priority`, `power.drawPerSol`, `o2PerSol` 10, `recovery` 0.9), `machines/regolith_oven.json` gets `priority` 2 and `waterPerSol` 0.5, `machines/regolith_outgasser.json` gets `priority` 3, plus `life_support.json` (`o2UsePerPlayerSol` 8, `waterUsePerPlayerSol` 3, `leakO2PerSol` 10, tank sizes, start fills, `reserveSols` 3 / 1, `waterLastSols` 10, `precomposedMakers` "ceil(n/2)"). A unit test fails if any value differs from this table (CX-C). The exact files and keys are in [life-support-spec.md](../tech/life-support-spec.md).

### 2d.6 What the player sees at each failure state

**Game rules** here (journal label, see 2d.9): Blackout, "Dehydrated", free suit refill. Real low oxygen is dangerous and poorly sensed; no medical detail or numbers are used (SME, `life-support-review.md` section 4). Wording is a sketch in grade 6 to 7 language and needs the Instructional Designer's reading-level pass. Full entries (DOCUMENTATION §5.1: id, trigger, feedback kind, fade rule) are written with slice 4. No state ever uses colour alone: each has an icon and a word. `L` = sols of air left = stored O2 / (use - made), when net is negative.

| State | Trigger | HUD | Monitor Dome tab | Log and report | Effect on play | Goals |
|---|---|---|---|---|---|---|
| **Normal** | Net 0 or more | none | O2 line: stored, made, used, net ("+2 a sol") | none | none | C1 |
| **Draining** | Net below 0, L at least 2 sols | none (quiet) | Amber icon, "Air falling: about {L} sols left. Make more or use less." names the cause (power short, breach, too few makers) | One log line (BELL) | none | C1, B1 |
| **Low** | L under 2 sols | Air chip with down arrow and "~{L} sols" appears | Same line, bolder | Log line; **failure report** (first time with Hana's tip: "One maker for each Keeper. Check the power.") | none | C1, C2 |
| **Critical** | L under 0.5 sol (10 min standard) | Chip gets a bold border; countdown in minutes; one soft chime | Same | Tip level 2 (names the fix) | none | C1, C2 |
| **Empty tank** | Stored O2 = 0 | "No tank air" icon | "Tanks empty. Suits cannot refill." | Failure report: what failed, why, what to try | "Short of breath": slower, no sprint, tunnel vignette (not red flashes) | C1, C2 |
| **Blackout** (**game rule**) | In the "short of breath" state for 120 s, or suit empty outside or in an unsealed volume for 30 s (*invented*) | Screen fades | none | Report: "You blacked out. In the game you wake up in your bunk and lose nothing." | Wake in your bunk after 10 s, suit at 3 units, **no item or health loss, no death** (**owner-accepted** 2026-11-27: out of air = Blackout, wake in bunk, nothing lost) | C1, C2 |
| **Unsealed** | A required volume is not sealed | Chip "Unsealed" (icon plus word) and the suit warning | "Dome open. Air leaks 10 a sol." | Failure report with the breach place; first time: Hana's tip about seal kits | Tank leak +10 per sol; suits required in the volume | C2, A1 |
| **Water low** | Water lasts under 10 sols | none | Amber line "Water: {n} sols left" | One log line | none | C1 |
| **Water critical** | Under 3 sols | Chip | Same | Failure report | none | C1, C2 |
| **No water** | Water = 0 | Chip | "No water" | Report | "Dehydrated": slower, no sprint. No blackout, no damage | C1, C2 |
| **Power short** | Tier 1 not fully powered | none | Power row: "Not enough power. Cut first: Outgassers, then Ovens" | Log line; report on repeats only | Makers run at their share, so O2 net falls | C1, B1 |
| **Dome stable waiting** | A hold is paused | Gate chip pauses (never flashes) | "Waiting: {state}. Resets in {t} sol if not fixed" (existing ui-specs wording) | On reset: failure-report line says why | Hold counter paused, then reset after more than 1 sol out | C1 |

After Dome stable latches, the same failures still happen and still show; the gate does not unlatch.

### 2d.7 Arithmetic: Dome stable timing, consistent with §2 (calc, not run in the game)

**t0 = the end of Landing = sol 1.2 (0.4 h).** The life support clock starts there (Landing is safe; dome-layout §5.8).

Solo, standard (1 maker pre-built, nothing to build): net = 10 - 8 = +2 per sol. Stored 14 against a reserve of 24: (24 - 14) / 2 = **5.0 sols** (unchanged by the 2026-11-27 number change). `air_ok` first holds at sol 1.2 + 5.0 = 6.2. Add the 3-sol hold: **Dome stable at sol 9.2 = 3.07 h**. The §2 curve has the first Outgasser at sol 9 (3.0 h): a difference of 0.2 sol (4 minutes).

Groups (script, outside the game). Assumptions: pre-built makers ceil(n/2); the first extra maker is built 0.6 sol after t0, each later one 0.4 sol after the previous (standard); in the classroom all extras are built 0.3 sol after t0 by parallel hands (cheaper recipes); panels and batteries in time; stored O2 never below 0 in the build window (true for every row, see the last column).

| Mode | n | Makers pre-built / to build | Condition first true (sols after t0) | Dome stable (sol) | Dome stable (h) | Tank empties in (sols) if the player builds nothing |
|---|---|---|---|---|---|---|
| Standard | 1 | 1 / 0 | 5.0 | 9.2 | 3.07 | never (net +2) |
| Standard | 2 | 1 / 1 | 6.5 | 10.7 | 3.57 | 4.7 |
| Standard | 3 | 2 / 1 | 6.0 | 10.2 | 3.40 | 10.5 |
| Standard | 4 | 2 / 2 | 7.0 | 11.2 | 3.73 | 4.7 |
| Standard | 5 | 3 / 2 | 6.6 | 10.8 | 3.60 | 7.0 |
| Classroom | 3 to 5 | 2 or 3 / 1 or 2 | 0.3 | 2.5 | 0.83 | 4.7 to 10.5 |

Recalculated 2026-11-27 with use 8, maker 10, start fill 14 per player, reserve 24 per player (**calc**, throwaway script outside the game; **not run in the game**). Worked example, n = 4: start 56, reserve 96. Two makers (20) against use 32 = -12 a sol until sol 0.6 (stored 56 - 7.2 = 48.8); three makers (30) = -2 a sol to sol 1.0 (48.0); four makers (40) = +8 a sol: (96 - 48.0) / 8 = 6.0 sols, so 1.0 + 6.0 = 7.0 after t0. "Tank empties in" = start fill / (use - pre-built output): n = 2: 28 / (16 - 10) = 4.7; n = 3: 42 / (24 - 20) = 10.5; n = 4: 56 / (32 - 20) = 4.7; n = 5: 70 / (40 - 30) = 7.0. The 5-player wall tank (200) with no makers lasts 200 / 40 = 5.0 sols.

Reading it. **Standard solo lands on the §2 curve (3.07 h against 3.0 h).** A standard group of 2 to 5 arrives at 3.4 to 3.7 h (3.40, 3.57, 3.60, 3.73): within 25 % of 3 h (the Stage 3 exit tolerance, 2.25 to 3.75 h), **0.02 h (about 1 minute) from the top edge for n = 4**; if n = 4 measures over, the first lever is the build delay of the extra makers (cheaper recipe), not the physical numbers. A fast solo player who builds a second maker at once refills in about 1 sol and gets Dome stable near sol 6 (2 h): that is the player's choice and shows B1 (a second maker is the visible fix); the typical player does not. **Classroom (group of 4):** Dome stable at sol 2.5 (0.83 h). The §5.3 check assumed sol 3 (1.0 h); this is 17 % earlier, about 10 minutes, which is inside the classroom Phase 1 budget (2.4 h): update the §5.3 check line to "sol 2.5" when this section is accepted.

**Pressure curve against §2.** Before Dome stable only the two Ovens act (§2c frost model): P(t) = 1.6 - e^(-0.02 t). The Ovens are placed at Landing (t = 1.2), so the effective time at sol 9.2 is 8.0 sols: P = 1.6 - e^(-0.16) = **0.748 kPa**, against 0.74 in §2: inside 0.01 kPa. After the first Outgasser the net source is 0.12 kPa per sol (0.10 + 0.02), so dP = 0.12 - 0.02 x (0.75 - 0.6) = 0.117 per sol, against 0.12 in the §2 cell sol 9 to 10 (0.74 to 0.86). The 0.2-sol delay leaves sol 10 at about 0.84 against 0.86 (-0.02 kPa), and later cells shift by at most that amount: **§2 curve holds within 0.03 kPa; the "5 kPa at about 8.3 h" target shifts by minutes, not hours.** What does change: (a) the Outgasser power order. Life support uses tier 1 and 2 power first, so the Outgasser needs a **second panel** for a solo player (8 units against 4.6) and a **third** for a group of 3 to 5 (§2d.4); the §2 curve's four Outgassers by sol 16 need 2 + 2 + 16 = 20 units for a solo dome, 5 panels at 91 %, which is the "second hidden gate" §2 already warns about, and now a measured prototype item (open number: panel build time per sol); (b) a group of 5 before the second panel gives the Ovens 0.6 of 2 units (30 %), so its pre-Dome-stable pressure is flatter by under 0.01 kPa (calc).

**Plain-language contract with R-73:** Dome stable comes at about 3.1 h solo (3.4 to 3.7 h for a group in standard mode, about 0.8 h in the classroom), not the stand-in's 1 h. Label it "prototype timing" until the owner's play measures it.

### 2d.8 Open items on this section (status 2026-11-27: items 1 and 2 done by the SME; item 4 answered by [life-support-spec.md](../tech/life-support-spec.md), Technical Lead confirms; item 3 open)

| # | To | Question |
|---|---|---|
| 1 | SME | Verify or replace: 0.7 kg O2 per person per day (secondary source), water 3 L and 90 % recovery (unverified), MOXIE power for the 22x compression note (unverified), Oven water 0.5 per sol, suit duration compression. Mark each verified, invented or unverified in this table |
| 2 | SME | Journal wording for the power compression ("the game's oxygen maker is far cheaper to run than a real one") and for the fainting and short-of-breath states so no medical claim is made |
| 3 | Instructional Designer | Reading level of the failure wording; whether "reserve" (stored air in sols) is one idea too many for grade 6 at Landing, or belongs in the high-school read-more; the fade rule for the first Low and Unsealed reports |
| 4 | Technical Lead | Gate `all_of`, the `priority` field, the numbers in the JSON list in §2d.5, and the Phase 1 raw-resource list for recipe costs (G4) |

### 2d.9 "Game rule" labels for the journal (SME term; every item here is something the game does that real life does not)

The journal entry for each of these carries the words **"Game rule"** and says what real life is like. Do not hide them in small print.

| Game rule | What real life is like (one line, from the SME review) |
|---|---|
| Blackout (out of air: wake in your bunk, nothing lost) | Real low oxygen is dangerous, poorly sensed by the body, and a person cannot save themselves; crews use alarms and sensors |
| Free suit refill at the suit points | Real suits last about 7 to 8 hours and their consumables are limited; the game suit is "17 minutes of your play time" |
| Oxygen power discount (1 kWh for 1 kg) | MOXIE used about 300 W for up to 12 g an hour; real oxygen needs at least about 5 kWh per kg; the game charges about 22 times less than MOXIE |
| Hygiene and laundry water left out | Washing uses far more water than drinking and food |
| Leak larger than the worst ISS crack (and the room's own air ignored) | A tight station leaks a very small amount a day; a big hole empties a room in minutes; real crews must also remove the CO2 they breathe out (not in the game) |
| Also game rules, same treatment: "Dehydrated" (slower, no sprint), Landing grace (no leak during Landing), reclaimer at 90 % (below ISS) | n/a |

Learning goals: A1, C1, C2 (the label itself teaches the difference between a model and the real thing, a transfer guard for C1). The Instructional Designer owns the reading-level pass and the misconception item "oxygen is easy to make on Mars".

---

## 3. Required net rates per phase (standard, solo)

These set the machine sizes. Each phase's machines must reach these averages with a realistic build-up.

| Phase | Sols | Pressure | Temperature | Avg. net rate needed |
|---|---|---|---|---|
| 1 | 24 | 0.6 → 5 kPa | −60 °C (flat) | +0.18 kPa/sol |
| 2 | 54 | 5 → 30 kPa | −60 → 0 °C | +0.46 kPa/sol; +1.1 °C/sol |
| 3 | 42 | 30 → 75 kPa | 0 → 5 °C | +1.8 kPa/sol (first 25 sols), then hold *(was +1.1: arithmetic error, 45 kPa ÷ 25 sols = 1.8; found by the threshold-vs-curve check)* |

Phase 3's pressure jump got larger when the target moved from 60 to 75 kPa (+15 kPa, user decision). Phase 3 machines must be sized for it; watch this in the prototype as a grind risk.

---

## 3a. Phase 2 and 3 curves (standard, solo; draft)

Assumed build-up; *invented*. Real hours: Phase 2 starts at 8.3 h; 3 sols per hour.

**Phase 2** (54 sols)
| Sol of Phase 2 | 0 | 6 | 12 | 18 | 24 | 30 | 36 | 42 | 48 | 54 |
|---|---|---|---|---|---|---|---|---|---|---|
| Pressure (kPa) | 5.0 | 6.4 | 8.5 | 11.2 | 14.3 | 17.8 | 21.6 | 24.9 | 27.3 | **30.0** |
| CO2 partial pressure (kPa) | 4.9 | 5.6 | 6.7 | 8.1 | 9.7 | 11.6 | 13.5 | 15.2 | 16.5 | **18.0** |
| Filler gas (kPa, untracked in play; shown here for the budget) | 0.1 | 0.8 | 1.8 | 3.1 | 4.6 | 6.2 | 8.1 | 9.7 | 10.8 | 12.0 |
| Temperature (°C) | −60 | −56 | −50 | −42 | −35 | −27 | **−20** | −13 | −6 | **0** |
| Hydrosphere (%) | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 6 | 12 | 16 |
| Events | Phase 2; F5 | Armstrong line (6.3) | | Frost slows (−40 °C) | | | **Thaw**; F4b; polar release starts (+0.5 kPa/sol bump) | **Hydrosphere** (~sol 46); F6 | Ground collapse risk peaks | **Phase 3** |
| Real hours | 8.3 | 10.3 | 12.3 | 14.3 | 16.3 | 18.3 | 20.3 | 22.3 | 24.3 | 26.3 |

**Phase 3** (42 sols)
| Sol of Phase 3 | 0 | 5 | 10 | 15 | 20 | 25 | 28 | 35 | 42 |
|---|---|---|---|---|---|---|---|---|---|
| Pressure (kPa) | 30 | 39 | 48 | 57 | 66 | **75** | 75 | 76 | 76 |
| Temperature (°C) | 0 | 1 | 2 | 3 | 4 | 5 | 5 | 5 | 5 |
| CO2 partial pressure (kPa) | 18.0 | 16.0 | 12.8 | 9.0 | 5.0 | 1.6 | **0.5** | 0.4 | 0.4 |
| O2 partial pressure (kPa) | 0.2 | 2.2 | 5.4 | 9.2 | 13.1 | **16.1** | 16.6 | 16.7 | 16.8 |
| O2 fraction (%) | 0.7 | 5.6 | 11 | 16 | 20 | 21.5 | 22.1 | 22.3 (drift ±) | 22.1 |
| CO2 + O2 (kPa; falls only by scrubbing) | 18.2 | 18.2 | 18.2 | 18.2 | 18.1 | 17.7 | 17.1 | 17.1 | 17.2 |
| Biomass (BU) | 0 | 100 | 300 | 600 | 900 | 1,200 | 1,500 | 1,700 | 1,800 |
| Events | Phase 3; F8 | Everest (33.7) passed | | | O2 window opens (69.6 kPa) | O2 push ≥ 16 kPa; CO2 still 1.6 (not yet breathable) | CO2 ≤ 0.5: holds start (all in band) | **Breathable** (7 sols) | **Equilibrium** (14 sols) → beacon |
| Real hours | 26.3 | 28.0 | 29.6 | 31.3 | 33.0 | 34.6 | 35.6 | 38.0 | 40.3 |

**Named sinks and sources in Phase 3 (rule A):** flora and algae vats swap CO2 for O2 one for one (CO2 falls, O2 rises by the same amount); decay and fire swap it back; carbon scrubbers lock CO2 into carbonate rock and make **no** O2 (so CO2 + O2 falls only when scrubbing: 1.1 kPa over the climb above); Phase 3 processors add filler gas only. Total pressure is held constant during conversion (simplification; the real drop is about 0.33 kPa per kPa converted, HS read more). All values *invented* around the SME's calc (18.5 kPa of CO2 needed). Prototype check: O2 reaches the window by sol 25 with no grind (SME round 3 §1.4). The flow channel check is in DESIGN.md (Flow and Task Design).

## 4. Holds: what players do (M-16)

| Hold | Standard | Classroom | Runs in parallel with |
|---|---|---|---|
| Dome stable | 3 sols (1.0 h) | 1 sol | — |
| Breathable | 7 sols (2.3 h) | 2 sols | Equilibrium |
| Equilibrium | 14 sols (4.7 h) | 3 sols | Breathable |

**Holds overlap.** Breathable is a subset of the equilibrium band, so a group that holds everything in band reaches Breathable on sol 7 of the equilibrium hold. Total hold time in Phase 3 is 14 sols (4.7 h), not 21. That is a third of Phase 3: acceptable only if it is active play.

**Hold rule (one rule for every hold, including Dome stable; see §2c decision 7).** The hold counter **pauses** when a value leaves the band. It **resets to zero** only if the value stays out for more than 1 continuous sol (classroom: 0.5 sol). A quick fix keeps your progress; neglect loses it.

**Drift events (during any hold, and in Phase 3 from the first established flora patch).** One event every 1–1.5 sols (classroom: every 0.5–0.75 sol, so a 3-sol hold has 4–6). Storm events are announced at least 1 sol ahead. Each one pushes a value out of band in about 1 sol if nobody answers, and takes about 1–5 minutes of play to answer once diagnosed.

| Event | What happens | Effect if ignored | Answer | Goals |
|---|---|---|---|---|
| Dust storm | Solar −60 % for 0.5–1 sol | Algae vats and scrubbers slow: CO2 +0.15 kPa/sol | Battery reserve, temporary vat shutdown plan | C1, B1 |
| Flora bloom | A biome grows fast | O2 +0.6 %/sol → fire band | Prune, add CO2 source, slow vats | A5, B4 |
| Fauna grazing | Settled thawlings graze a patch | Biomass −5 %; O2 output dips | Move or widen the quiet zone, reseed | B2, D1 |
| Emitter wear | One emitter fails | Temperature −1 °C/sol | Repair (failure report names it) | C2 |
| Weather low (replaces "polar winter" in holds; SME round 3: at equilibrium, nitrogen and O2 can't freeze out at the poles) | A passing low-pressure system | Pressure −1.5 kPa over 2 sols, then recovers (*invented*; about 2 % of 75 kPa) | Run processors harder; accept a small dip | B2, A1, A2 (boiling point dips a little: FI-2) |
| Decay pulse | Old biomass dies back | CO2 +0.1 kPa/sol **and O2 −0.1 kPa/sol** (one for one, rule A) | Reseed; keep the biome young-to-mature mix | A5, B2 |

All drift-event sizes above are *invented* balance numbers.

**Also available during holds:** building beacon components (recipes unlock at Breathable; final assembly at Equilibrium), settling quiet zones, visiting Basu's sites, and expanding biomes for margin. Nobody should wait with nothing to do.

**Prototype test:** if playtesters report "waiting" during holds, shorten Equilibrium to 10 sols before adding more events.

---

## 5. Classroom pacing and group scaling (M-02, C-02)

### 5.1 Config keys (separate on purpose)

| Key | What it scales | Standard | Classroom | Never scales |
|---|---|---|---|---|
| `pace.rateMultiplier` | Every Index contribution (machines, flora, sinks) | 1.0 | 4.0 | Any threshold |
| `pace.holdSols.*` | Hold durations per gate | 3 / 7 / 14 | 1 / 2 / 3 | — |
| `pace.recipePreset` | Recipe cost (KubeJS) | standard | cheaper | — |
| `group.size` | Static rate scale for the number of players on a planet | 1 (solo) | Teacher sets 3–5 per planet | Thresholds; fauna rules |

Group size is set in config, **not** read from who is online. An absent student must not change the planet's rates mid-class.

### 5.2 Group rate scale

`groupRateScale(n) = 1 / (1 + 0.35 × (n − 1))`

| Players | 1 | 2 | 3 | 4 | 5 | 6 |
|---|---|---|---|---|---|---|
| Rate scale | 1.00 | 0.74 | 0.59 | 0.49 | 0.42 | 0.36 |

**Assumption:** each extra player adds about 35 % more machine output (shared resources and coordination limit it below 100 %). If 4 players run about 2× the solo machine count, 2 × 0.49 ≈ 1.0, so Index waits stay close to solo. Building still goes faster with more hands; we accept that.

### 5.3 Classroom time model (group of 4)

**Assumed split of the 40-hour solo game** (to check in the prototype from the action log):

| Phase | Building and learning (h) | Index-limited waiting (h) | Of which holds |
|---|---|---|---|
| 1 | 5 | 3 | 1.0 (Dome stable) |
| 2 | 10 | 8 | — |
| 3 | 7 | 7 | 4.7 (overlapping holds) |
| **Total** | **22** | **18** | 5.7 |

**Classroom, group of 4:** building ÷ 2.5 (four players, assumed throughput) ÷ 1.3 (cheaper recipes); non-hold waits ÷ 4 (rate multiplier, with group scale cancelling the extra machines); holds at classroom length.

| Phase | Building | Waiting | Holds | Total | Play periods (40 min) |
|---|---|---|---|---|---|
| 1 | 1.5 h | 0.5 h | 0.3 h | **2.4 h** | ~3.6 |
| 2 | 3.1 h | 2.0 h | — | **5.1 h** | ~7.7 |
| 3 | 2.2 h | 0.6 h | 1.0 h | **3.8 h** | ~5.6 |
| **Total** | | | | **11.3 h** | **~17** |

This matches the settled ~11–12 h and the ~17 play periods. Same proportions as standard (8 / 18 / 14).

**Classroom Phase 1 check** (group of 4, 2 Outgassers per build step, Dome stable on sol 3): pressure 0.83 kPa at sol 3, 2.14 at sol 5, 4.96 at sol 7, past 5 kPa during sol 8 (about 2.7 h). Slightly slow against 2.4 h; tune Outgasser output or the classroom multiplier in the prototype.

**Group of 3 or 5:** the rate scale handles machine count. Building time changes with hands (3 players: about +0.7 h; 5 players: about −0.4 h). Teachers can use groups of 3–5 without retuning.

---

## 6. Fauna numbers (draft)

| Item | Value | Notes |
|---|---|---|
| Machine signature | Tier 1 = 1, Tier 2 = 2, Tier 3 = 3 (emitters, extractors, processors, outgassers; not dome life support) | Heat, water, vibration |
| Insulation shroud | Signature −50 %, machine output −15 % | Non-lethal deterrent; a real trade-off (B4) |
| Lure station | Draws fauna within 64 blocks; costs 1 water unit per sol | Redirects, does not remove |
| Quiet zone | No active machine within 48 blocks | Fauna there are calm and settle |
| Fauna pressure F | Σ signatures − shroud reductions | Visible on the Monitor's fauna page |
| Raids per sol | 0.15 × F after Thaw; × 1.5 after Hydrosphere | A raid = a few thawlings heading for the highest-signature machine |
| Planet cap (active thawlings) | 12 + 4 per configured player, hard cap 30 | Performance; Technical Lead to confirm |
| Lethal defense | Allowed; does **not** lower F | Treats the symptom; raids keep coming while F is high |
| Thawling defensive shove (only when hit) | Juvenile (stirring): knockback only, 0 damage. Later life stage: 1 damage (half a heart) + knockback, once, then curl and roll away. Never chase | Invented game numbers (draft, 2026-11-26); keeps "they only fight back if attacked" true (style guide §6.4) |
| Group S population | 0–100 index: +2 per raid that reaches a quiet zone, −1 per thawling killed, +0.5/sol per settled colony in a biome | Drives the ending cards |

**Behavior.** Thawlings go after machines, not players. They defend themselves if attacked. Settled thawlings (Phase 3, in a quiet zone or stable biome) graze and nest and stop raiding.

**Ending card thresholds (Group S population at the beacon)**

| Population and where | Card theme |
|---|---|
| ≥ 40 and at least one settled colony | Neighbors: they live in the biomes |
| ≥ 15, no settled colony | Driven off but surviving: they keep to the deep ice |
| < 15 | Few or none left: Basu's samples are what remains |

No ranking, no score, no "good" or "bad" label (story bible rule 7).

---

## 6a. Hazard thresholds for UI and art (draft, requested by the Lead Artist)

All *invented* game numbers unless tagged. Direction checked against the SME's sources where noted; sizes are play values.

| Item | Value | Notes |
|---|---|---|
| Suit O2 capacity | 100 units = 1 sol outside at rest (*invented*) | Refill at wall tanks |
| Suit O2 warning | ≤ 25 % (HUD icon + tone, log line) | Not blocking |
| Suit O2 critical | ≤ 10 % (HUD flashes ≤ 3 per second; first time is a blocking warning, I-C rule 3) | Damage starts at 0 % |
| Radiation dose meter | 0–100 game units, decays 10 units per sol when sheltered | A game meter, not sieverts. Journal never converts it to real units |
| Dose bands | 0–29 normal · **30 elevated** (HUD icon) · **60 harmful** (weakness effect, slower mining) · **90 severe** (damage over time) | *Invented* |
| Solar event surface dose rate | 30 units per 0.1 sol in open ground at 0.6 kPa (*invented*) | So 0.2 sol in the open reaches "harmful" in the first event |
| Atmosphere factor | dose × (0.6 ÷ pressure)^0.5, capped at 1 (*invented curve*; direction sourced: more air overhead absorbs more, SME round 2 §3.1) | 1.0 at 0.6 kPa; 0.35 at 5 kPa (F7: a clear difference); 0.14 at 30 kPa; 0.09 at 75 kPa |
| Shielding per block between player and sky | Regolith or rock ×0.5 per block; water ×0.5 per block; dome glass ×0.9; metal plate ×0.7 (*invented*) | Three blocks of regolith ≈ ×0.13 |
| Shelter (core module) | ×0.05 (*invented*) | The "inside, under thick shielding" answer to F2 |
| Perchlorate exposure | +1 level per unbrushed airlock entry; 3 levels = mild effect (slower O2 refill); clears 1 level per sol | *Invented*; real toxicity sourced (SME round 2 §1.5) |

## 7. Phase 3 carbon and biomass (draft)

| Item | Value |
|---|---|
| Established flora block | 1 BU; takes up CO2 and releases O2 while growing |
| Young patch (first 3 sols) | Full uptake |
| Mature patch | Uptake falls to 30 % (*invented*). Wording (SME round 2 §1.6): "As a biome matures, the CO2 its plants take in and the CO2 released by decay get closer to balanced. A young, growing biome pulls CO2 down much faster. (Some old forests and peat bogs still store carbon slowly.)" Never say mature ecosystems stop storing carbon |
| Decay | Dead flora returns CO2 (SME optional item, adopted) |
| Self-sustaining biome | A seeded patch of ≥ 150 BU that survives 3 sols without a seed kit |
| Equilibrium biomass | ≥ 1,500 BU |

Lesson the numbers build: planting more raises O2 quickly at first, then only a growing, mixed biome keeps pulling CO2 down (A5, B2).

---

## 8. Open numbers

| Item | Waiting on |
|---|---|
| ~~Polar frost direction~~ | Done (SME round 2): reversible reservoir above |
| Ice–albedo: total capped at **2 °C** (*invented*, generous upper bound on the SME's ~0.4–1 °C estimate) | Decided; spread over melting from the Thaw on |
| Puddle drying time below 5 kPa (0 °C up to the boiling point) | **0.05 sol at 0.6 kPa rising to 0.2 sol at 5 kPa** (grows with pressure, SME round 3 direction), *invented*, inside the SME's accepted 0.02–0.25 sol. Puddles here are thin films; frozen counts as "no liquid". **Hard limit: must stay under 0.25 sol**, because forecast F4a is scored by "no liquid water at that spot 0.25 sol after the ice changes state". SME round 3: plausible; real thin films dry in about 1–3 h at 0.7–1 mm/h (R5, calc); a 1 cm puddle would not. The threshold-vs-curve check treats this as a forecast-critical number |
| ~~Phase 3 CO2 budget~~ | **Decided: rule A** ("CO2 is the oxygen source"; DESIGN.md, Phase 3 composition rule). CO2 at the Phase 3 gate 18.0 kPa (*invented*, inside the SME's 17–19 kPa range) |
| Ice-to-water rule thresholds (DESIGN.md §2) | SME confirmation of the draft rule |
| The 35 % group throughput assumption; the build/wait split | Prototype action log |
| Fauna cap | Technical Lead (performance) |
| Orbital mirror uplink (new route: warming with no ground signature): cost, build time, °C/sol | Stage 2 prototype; SME for framing (DESIGN.md, Route choices) |
| Maintenance part intervals per machine (filter, seal kit, bearing, heater coil) | Stage 2 prototype (Hana's "Filters every 2 sols" is the Phase 1 anchor) |
| `guidance` setting (gentle / standard / expert): which hints each level shows | ID alignment check (DESIGN.md, Difficulty as information) |
