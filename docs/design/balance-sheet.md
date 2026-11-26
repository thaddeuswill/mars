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

**Why Dome stable unlocks the Outgasser:** survive first, then terraform. It keeps Phase 1 about life support (C1) until the dome is steady, then hands the player the first lever on the planet (B1). The relay beat (story beat 6) also fires at Dome stable, so the player gets people and a planet lever at the same moment.

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
| Temperature (°C) | −60 | −56 | −50 | −42 | −35 | −27 | **−20** | −13 | −6 | **0** |
| Hydrosphere (%) | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 6 | 12 | 16 |
| Events | Phase 2; F5 | Armstrong line (6.3) | | Frost slows (−40 °C) | | | **Thaw**; F4b; polar release starts (+0.5 kPa/sol bump) | **Hydrosphere** (~sol 46); F6 | Ground collapse risk peaks | **Phase 3** |
| Real hours | 8.3 | 10.3 | 12.3 | 14.3 | 16.3 | 18.3 | 20.3 | 22.3 | 24.3 | 26.3 |

**Phase 3** (42 sols)
| Sol of Phase 3 | 0 | 5 | 10 | 15 | 20 | 25 | 28 | 35 | 42 |
|---|---|---|---|---|---|---|---|---|---|
| Pressure (kPa) | 30 | 39 | 48 | 57 | 66 | **75** | 75 | 76 | 76 |
| Temperature (°C) | 0 | 1 | 2 | 3 | 4 | 5 | 5 | 5 | 5 |
| O2 fraction (%) | 0.5 | 3 | 8 | 13 | 18 | 21.5 | 22 | 22 (drift ±) | 22 |
| Biomass (BU) | 0 | 100 | 300 | 600 | 900 | 1,200 | 1,500 | 1,700 | 1,800 |
| Events | Phase 3; F8 | Everest (33.7) passed | | | O2 window opens (69.6 kPa) | Breathable conditions met | Holds start (all in band) | **Breathable** (7 sols) | **Equilibrium** (14 sols) → beacon |
| Real hours | 26.3 | 28.0 | 29.6 | 31.3 | 33.0 | 34.6 | 35.6 | 38.0 | 40.3 |

CO2 is left out of the Phase 3 table until the CO2 budget (§8) is settled. The flow channel check against these curves is in DESIGN.md (Flow and Task Design).

## 4. Holds: what players do (M-16)

| Hold | Standard | Classroom | Runs in parallel with |
|---|---|---|---|
| Dome stable | 3 sols (1.0 h) | 1 sol | — |
| Breathable | 7 sols (2.3 h) | 2 sols | Equilibrium |
| Equilibrium | 14 sols (4.7 h) | 3 sols | Breathable |

**Holds overlap.** Breathable is a subset of the equilibrium band, so a group that holds everything in band reaches Breathable on sol 7 of the equilibrium hold. Total hold time in Phase 3 is 14 sols (4.7 h), not 21. That is a third of Phase 3: acceptable only if it is active play.

**Hold rule.** The hold counter **pauses** when a value leaves the band. It **resets to zero** only if the value stays out for more than 1 continuous sol (classroom: 0.5 sol). A quick fix keeps your progress; neglect loses it.

**Drift events (during any hold, and in Phase 3 from the first established flora patch).** One event every 1–1.5 sols (classroom: every 0.5–0.75 sol, so a 3-sol hold has 4–6). Storm events are announced at least 1 sol ahead. Each one pushes a value out of band in about 1 sol if nobody answers, and takes about 1–5 minutes of play to answer once diagnosed.

| Event | What happens | Effect if ignored | Answer | Goals |
|---|---|---|---|---|
| Dust storm | Solar −60 % for 0.5–1 sol | Algae vats and scrubbers slow: CO2 +0.15 kPa/sol | Battery reserve, temporary vat shutdown plan | C1, B1 |
| Flora bloom | A biome grows fast | O2 +0.6 %/sol → fire band | Prune, add CO2 source, slow vats | A5, B4 |
| Fauna grazing | Settled thawlings graze a patch | Biomass −5 %; O2 output dips | Move or widen the quiet zone, reseed | B2, D1 |
| Emitter wear | One emitter fails | Temperature −1 °C/sol | Repair (failure report names it) | C2 |
| Polar winter | Seasonal frost (game-sized: the real seasonal swing is ~25–30 % of the air, which the game deliberately doesn't model) | Pressure −1.5 kPa over 2 sols | Run processors harder; accept a small dip | B2, A1 |
| Decay pulse | Old biomass dies back | CO2 +0.1 kPa/sol | Reseed; keep the biome young-to-mature mix | A5 |

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
| Puddle drying time below 5 kPa (0 °C up to the boiling point) | **0.1 sol (2 minutes)**, *invented*. **Hard limit: must stay under 0.25 sol**, because forecast F4a is scored by "no liquid water at that spot 0.25 sol after the ice changes state". Asked the SME for a plausible real range (depends on wind, humidity, temperature; real value missing). The threshold-vs-curve check treats this as a forecast-critical number |
| **Phase 3 CO2 budget:** what processors and Outgassers add (CO2 vs. untracked filler gas), so CO2 can fall from its Phase 2 level to ≤ 0.5 kPa | **Open, important.** If most added gas is CO2, Phase 3 must remove tens of kPa. Needs a composition rule (Lead Game Designer) checked by the SME before the Phase 3 curve is final |
| Ice-to-water rule thresholds (DESIGN.md §2) | SME confirmation of the draft rule |
| The 35 % group throughput assumption; the build/wait split | Prototype action log |
| Fauna cap | Technical Lead (performance) |
| Orbital mirror uplink (new route: warming with no ground signature): cost, build time, °C/sol | Stage 2 prototype; SME for framing (DESIGN.md, Route choices) |
| Maintenance part intervals per machine (filter, seal kit, bearing, heater coil) | Stage 2 prototype (Hana's "Filters every 2 sols" is the Phase 1 anchor) |
| `guidance` setting (gentle / standard / expert): which hints each level shows | ID alignment check (DESIGN.md, Difficulty as information) |
