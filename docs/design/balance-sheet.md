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

**Storm schedule (settled 2026-11-27; all numbers *invented* except the direction; SME to confirm the science rows in the report).** One length rule per kind of storm, so the curve and the drift table agree.

| Item | Standard | Classroom | Tag and reasoning |
|---|---|---|---|
| Phase 1 storm (the scheduled one; F1) | **2 sols** (about 40 min of play) | **0.5 sol** (÷4, same as dust interval) | **Invented.** Long enough to drain a battery and be seen on the Monitor graph (battery 10 units = 2.5 sols of one Outgasser, so 2 sols is survivable but only if prepared: C1). Real storms are far longer: the 2019 InSight storm ran about 20 sols (sols 40 to 60, **verified**, Lorenz et al. 2020); the 2018 global storm's effects at Gale lasted 55+ sols (sols 2075 to 2130, **verified**, Viudez-Moreiras et al. 2019). General lengths: the 2007 storms ran "nearly a month" (**verified**, NASA release 07-162) and the 2018 storm blanketed Mars "for the last six weeks" by late July (**verified**, Planetary Society); a universal "regional days to weeks, global months" rule is **not verified** (the Planetary Society says storms can "last a long time, or not"). Power cuts: Opportunity 2007 about -80 % (700 to 128 Wh/sol = -82 %, **verified**, NASA release 07-162); Opportunity 2018 about -97 % (645 to 22 Wh/sol = -97 %, **verified**, Planetary Society); InSight 2022 about -35 % (430 to 275 Wh/sol = -36 %, **verified**, NASA Science blog; the 425 version is from a JPL page not opened). Details and quotes: addendum in `docs/team/reports/2026-11-27-sme-storm-figures.md`. So even 2 sols is short |
| Hold drift-event storm (Phase 2 on; §4 drift table) | **1 sol** | **0.25 sol** | **Invented.** Drift events arrive every 1–1.5 sols and must be answerable in 1–5 min, so these are shorter than the Phase 1 storm |
| Warning lead | 1 sol before onset (0.25 sol classroom) | | Matches story beat 4 (0.8–1.2 sol) and F1 (T−1 sol) |
| First Phase 1 storm | Not before sol 14; curve uses sols 14–15 | Not before sol 4 | **Invented.** After the first Outgassers exist, so there is a pressure rate to lose |
| Gap to the next storm | 12–20 sols, seeded random | 3–5 sols | **Invented.** Standard Phase 1 ends near sol 25, so normally one storm per Phase 1 |
| Solar cut | 60 % × storm cap (Phase 1 cap 1.0 = 60 %) | same | **Invented**; unchanged. Physical, never scaled by pace |
| Dust stage at storm end | +1 stage; +2 if cap ≥ 0.8 (applies to both storm kinds; Phase 1 and 2 caps are ≥ 0.8) | same | **Invented**; unchanged. Physical, never scaled |

Only the storm's length, gap and warning scale with pace; the cut and the dust never do.

**Standard solo curve** (assumed build order: first Outgasser at Dome stable on sol 9, then 2 by sol 11, 3 by sol 13, 4 by sol 16; one 2-sol storm on sols 14–15):

| Sol | 1 | 5 | 9 | 10 | 12 | 14 | 16 | 18 | 20 | 22 | 24 | 25 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| Pressure (kPa) | 0.61 | 0.67 | 0.74 | 0.86 | 1.18 | 1.70 | 1.93 | 2.71 | 3.46 | 4.18 | 4.87 | **5.20** |
| Real hours | 0.3 | 1.7 | 3.0 | 3.3 | 4.0 | 4.7 | 5.3 | 6.0 | 6.7 | 7.3 | 8.0 | 8.3 |

Phase 2 begins at about 8.3 h, on target (~8 h). The curve is flat before Dome stable (the Ovens show the player that cause and effect exist), bends up as Outgassers come online, dips in the storm, and slows slightly near 5 kPa because frost loss grows. Every bend has a cause the Monitor can name.

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

## 2d. Phase 2 machine rates (proposal 2026-11-28, solo standard; full arithmetic in [phase2-machine-rates.md](phase2-machine-rates.md))

| Machine | Effect at full power | Draw (units/sol) | Tag |
|---|---|---|---|
| Atmospheric processor | pressure +0.15 kPa/sol each | 3 | **INVENTED** rate; draw existing INVENTED. Twice the Outgasser's kPa per power unit |
| Super-greenhouse emitter | temperature +0.25 C/sol each | 3 | **INVENTED** |

Standard build (INVENTED): processors 1 / 2 / 3 at Phase 2 sols 0 / 14 / 34; emitters 2 / 3 / 4 / 5 / 6 at sols 0 / 6 / 14 / 24 / 34. Result (calc, scratch model, not run in game): 30 kPa at sol 44, Thaw at sol 40, 0 C at sol 52. Emitters set the Phase 2 clock. Power peaks at 45 units/sol (10 panels, 3 batteries) in Phase 2 and 54 (12 panels) in Phase 3 with 10 processors. Tune: processor and emitter rate first, then panel supply.

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
| Dust storm | Solar −60 % × storm cap for 1 sol (classroom 0.25 sol). The Phase 1 storm is 2 sols: see the storm schedule in §2 | Algae vats and scrubbers slow: CO2 +0.15 kPa/sol | Battery reserve, temporary vat shutdown plan | C1, B1 |
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
