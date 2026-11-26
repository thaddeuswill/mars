# Balance Sheet — Mars Terraforming

**Owner:** Lead Game Designer · **Status:** Draft 1 (2026-11-26), numbers are **fluid** (TEAM.md 8.1) and are starting values for the Stage 2 prototype · **Depends on:** [DESIGN.md](../../DESIGN.md), [docs/science/accuracy-review.md](../science/accuracy-review.md)

Numbers and curves for the Planetary Index, pacing, holds, fauna, and group play. Nothing here has been playtested. Each table says what it assumes so the prototype can prove it wrong.

**Rules this sheet follows**
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
| Polar frost loss (sink) | Always | −2 % of (pressure − 0.6) per sol while temperature < −40 °C; −1 % from −40 to −20 °C; 0 above −20 °C | — | A balancing loop (B2): the thicker the air, the more freezes out at the poles **[SME to verify direction and wording]** |

Dust storms cut solar output by 60 % (Outgassers slow). Unmaintained Outgassers lose output (mechanical wear).

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
| 3 | 42 | 30 → 75 kPa | 0 → 5 °C | +1.1 kPa/sol (first 25 sols), then hold |

Phase 3's pressure jump got larger when the target moved from 60 to 75 kPa (+15 kPa, user decision). Phase 3 machines must be sized for it; watch this in the prototype as a grind risk.

---

## 4. Holds: what players do (M-16)

| Hold | Standard | Classroom | Runs in parallel with |
|---|---|---|---|
| Dome stable | 3 sols (1.0 h) | 1 sol | — |
| Breathable | 7 sols (2.3 h) | 2 sols | Equilibrium |
| Equilibrium | 14 sols (4.7 h) | 3 sols | Breathable |

**Holds overlap.** Breathable is a subset of the equilibrium band, so a group that holds everything in band reaches Breathable on sol 7 of the equilibrium hold. Total hold time in Phase 3 is 14 sols (4.7 h), not 21. That is a third of Phase 3: acceptable only if it is active play.

**Hold rule.** The hold counter **pauses** when a value leaves the band. It **resets to zero** only if the value stays out for more than 1 continuous sol (classroom: 0.5 sol). A quick fix keeps your progress; neglect loses it.

**Drift events (during any hold and all of Phase 3).** One event every 1–1.5 sols (classroom: every 0.5–0.75 sol, so a 3-sol hold has 4–6). Each one pushes a value out of band in about 1 sol if nobody answers, and takes about 1–5 minutes of play to answer once diagnosed.

| Event | What happens | Effect if ignored | Answer | Goals |
|---|---|---|---|---|
| Dust storm | Solar −60 % for 0.5–1 sol | Algae vats and scrubbers slow: CO2 +0.15 kPa/sol | Battery reserve, temporary vat shutdown plan | C1, B1 |
| Flora bloom | A biome grows fast | O2 +0.6 %/sol → fire band | Prune, add CO2 source, slow vats | A5, B4 |
| Fauna grazing | Settled thawlings graze a patch | Biomass −5 %; O2 output dips | Move or widen the quiet zone, reseed | B2, D1 |
| Emitter wear | One emitter fails | Temperature −1 °C/sol | Repair (failure report names it) | C2 |
| Polar winter | Seasonal frost | Pressure −1.5 kPa over 2 sols | Run processors harder; accept a small dip | B2, A1 |
| Decay pulse | Old biomass dies back | CO2 +0.1 kPa/sol | Reseed; keep the biome young-to-mature mix | A5 |

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

## 7. Phase 3 carbon and biomass (draft)

| Item | Value |
|---|---|
| Established flora block | 1 BU; takes up CO2 and releases O2 while growing |
| Young patch (first 3 sols) | Full uptake |
| Mature patch | Uptake falls to 30 %: growth and decay nearly balance (real ecosystems near maturity are close to carbon-neutral **[SME to verify wording]**) |
| Decay | Dead flora returns CO2 (SME optional item, adopted) |
| Self-sustaining biome | A seeded patch of ≥ 150 BU that survives 3 sols without a seed kit |
| Equilibrium biomass | ≥ 1,500 BU |

Lesson the numbers build: planting more raises O2 quickly at first, then only a growing, mixed biome keeps pulling CO2 down (A5, B2).

---

## 8. Open numbers

| Item | Waiting on |
|---|---|
| Polar frost loss direction and rate | SME |
| Ice-to-water rule thresholds (DESIGN.md §2) | SME confirmation of the draft rule |
| Ice–albedo feedback size | SME |
| The 35 % group throughput assumption; the build/wait split | Prototype action log |
| Fauna cap | Technical Lead (performance) |
| Orbital mirror uplink (new route: warming with no ground signature): cost, build time, °C/sol | Stage 2 prototype; SME for framing (DESIGN.md, Route choices) |
| Maintenance part intervals per machine (filter, seal kit, bearing, heater coil) | Stage 2 prototype (Hana's "Filters every 2 sols" is the Phase 1 anchor) |
| `guidance` setting (gentle / standard / expert): which hints each level shows | ID alignment check (DESIGN.md, Difficulty as information) |
