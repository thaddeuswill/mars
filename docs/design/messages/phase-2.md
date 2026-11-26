# Messages: Phase 2 (Industrial Geo-Engineering)

**Owner:** Lead Game Designer · **Status of every entry:** draft: SME check and ID review pending · Conventions: [README.md](README.md)

Order of first play: Shift Briefing 2 → first emitter (F5) → Thaw gate (F4b, Thaw Earth link) → first fauna → the secret (Basu) → Hydrosphere and more machines (F6) → second solar event (F7, any time pressure has doubled) → Phase 3 begins (Keeper's Report 2, Mensah's reply).

---

## 1. Shift Briefing 2 (advance organizer)

| Field | Value |
|---|---|
| `id` | `briefing.shift2` |
| `type` | Shift Briefing |
| `trigger` | Keeper's Report 1 sent or deferred (Phase 2 begins) |
| `delivery` | BELL briefing card, right after the report screen (one sequence, counts as one pop-up); copy in the journal |
| `text.ms` | **Shift Briefing.** This shift: change the planet faster. Processors thicken the air. Emitters warm it. Watch pressure and temperature together: ice needs both to stay water. Warm machines change the ground near them. Notice what else changes when the planet warms. |
| `text.hs` | Same as MS |
| `goal_ids` | A2, A3, B5 |
| `instructional_purpose` | Advance organizer: two new levers, the two values that matter together (A2), and a heads-up that machines have side effects (B5) without naming the fauna |
| `motivational_purpose` | Expansion: new tools, a bigger job |
| `hint_level` | — |
| `science_claims` | Liquid water needs pressure and temperature together (SME B1) |
| `fiction_note` | None |
| `status` | draft |

---

## 2. F5: First greenhouse emitter

| Field | Value |
|---|---|
| `id` | `forecast.f5.emitter_temperature` |
| `type` | forecast |
| `trigger` | Player presses Start on the first super-greenhouse emitter (machine screen = natural pause) |
| `lock` | 1 sol after Start; Monitor open shows the card first (`forecast.look_first`, phase-1.md §11) |
| `delivery` | BELL forecast card on the machine screen; result at the next natural pause after 2 sols |
| `prompt.ms` | You start the greenhouse emitter. What will the surface temperature do over the next few sols? |
| `prompt.hs` | Same as MS |
| `choices` | Rise slowly · Drop · Stay the same |
| `why.hs` | Why? — Its gases trap heat that would escape to space · It blocks sunlight · It burns fuel that heats the air directly |
| `result.ms` | Temperature is rising slowly. The emitter makes factory-made greenhouse gases. Sunlight gets in, but they trap heat that would escape to space. They are not CO2. |
| `result.hs` | Temperature is rising slowly. Greenhouse gases let sunlight in but trap heat the ground gives off. Mars's air is mostly CO2 but too thin to keep it warm, so real plans use factory-made gases that trap heat far better. |
| `goal_ids` | A3, B3 |
| `instructional_purpose` | The greenhouse mechanism; counters "CO2 is the only greenhouse gas" and "it heats the air directly" |
| `motivational_purpose` | The second big lever; visible frost retreat follows |
| `hint_level` | — |
| `science_claims` | Greenhouse gases trap outgoing heat; Mars's thin CO2 air gives little warming; proposals use super-greenhouse gases (SME B4, refs 19, 20). No potency number (SME: unverified) |
| `fiction_note` | No real machine can do this yet. Factory-made greenhouse gases are a real proposal, not a tested plan. |
| `status` | draft |

**Earth link at the first emitter** (journal "On Earth" line):
| Field | Value |
|---|---|
| `id` | `earth.f5.greenhouse` |
| `type` | Earth link (journal line) |
| `trigger` | F5 result shown |
| `delivery` | Last line of the emitter journal page |
| `text.ms` | On Earth: CO2, methane, and water vapor trap heat too. That's the greenhouse effect. It is not the ozone hole. |
| `text.hs` | Same as MS. Read more: greenhouse gases absorb infrared energy given off by the ground and send some of it back down. Water vapor mostly responds to warming rather than starting it. The ozone hole is a different problem (UV light). |
| `goal_ids` | A3, D2 |
| `instructional_purpose` | Transfer to Earth's greenhouse gases; corrects the greenhouse–ozone confusion (SME misconception row, Boyes & Stanisstreet) |
| `motivational_purpose` | "This is the thing from the news" |
| `hint_level` | — |
| `science_claims` | CO2, methane, water vapor are greenhouse gases; ozone depletion is a separate UV issue **[SME: verify wording]** |
| `fiction_note` | None |
| `status` | draft |

---

## 3. F4b: Thaw gate, ice in thicker air

| Field | Value |
|---|---|
| `id` | `forecast.f4b.ice_thick_air` |
| `type` | forecast |
| `trigger` | Thaw gate reached; issued when the first ground-ice block near a warm machine passes 0 °C locally (pressure then about 22 kPa, boiling point about 62 °C: SME calc) |
| `lock` | That ice block changes state (held up to 0.25 sol for the answer, as F4a) |
| `delivery` | BELL forecast card at the next natural pause; result first in queue |
| `prompt.ms` | Last time, warmed ice vanished. Now the air is much thicker. What will warmed ice do? |
| `prompt.hs` | Same as MS |
| `choices` | Melt into water that stays while it's warm · Vanish into vapor again · Stay frozen |
| `why.hs` | What changed? — Thicker air raised water's boiling point · The ice is a different kind · The machine is stronger |
| `result.ms` | The water stays now. Thicker air raises water's boiling point, so it doesn't boil away. Water needs warmth and thick air together. At night it freezes again. |
| `result.hs` | The water stays. At {pressure} kPa water boils near {boil_c} °C, well above freezing, so liquid water can last. Below 0 °C it still freezes. Warmth and pressure both had to change. |
| `goal_ids` | A2, B3 |
| `instructional_purpose` | Non-example to F4a's example: same warmth, different pressure, different result |
| `motivational_purpose` | First real water: wonder |
| `hint_level` | — |
| `science_claims` | Liquid water possible from 5 kPa between 0 and about 33 °C (SME B1, Antoine table) |
| `fiction_note` | None |
| `status` | draft |

**Earth link at the Thaw gate** (journal gate entry; matches story bible §8.3 sample):
| Field | Value |
|---|---|
| `id` | `earth.gate.thaw` |
| `type` | Earth link (gate journal entry, last line) |
| `trigger` | Thaw gate |
| `delivery` | Journal gate entry, "On Earth" line |
| `text.ms` | On Earth, thick air keeps water from boiling away. That's why puddles last. |
| `text.hs` | Same as MS. Read more: in the Arctic, frozen ground is thawing. Roads crack and houses tilt as the ice underneath melts, like the ground near your warm machines. |
| `goal_ids` | A2, B5, D2 |
| `instructional_purpose` | A2 transfer; HS adds a real unintended consequence of thawing ground |
| `motivational_purpose` | Connects the ground-collapse hazard to something real |
| `hint_level` | — |
| `science_claims` | Thawing permafrost damages Arctic infrastructure (SME round 2 [S6], accurate) |
| `fiction_note` | None |
| `status` | draft |

---

## 4. F6: More machines, more thawlings

| Field | Value |
|---|---|
| `id` | `forecast.f6.fauna_loop` |
| `type` | forecast |
| `trigger` | After the Hydrosphere gate: the player places an emitter or extractor that raises fauna pressure F above its value at the first fauna sighting by 3 or more |
| `lock` | The new machine has run 0.25 sol |
| `delivery` | BELL forecast card at the next natural pause. Correctness follows the signature rule (more signature = more visits), not luck; the result shows F before and after |
| `prompt.ms` | You're adding more machines. What will happen to thawling visits near your machines? |
| `prompt.hs` | Same as MS |
| `choices` | They go up · They go down · No change |
| `why.hs` | What kind of loop is this? — Reinforcing: damage leads to more building, more thawlings · Balancing: thawlings keep machine numbers steady · Not a loop |
| `result.ms` | More visits. Thawlings come to warm, wet, buzzing machines. Each machine adds to fauna pressure on the Monitor. If you answer damage by building more, the visits keep growing. What would lower the pull? |
| `result.hs` | Fauna pressure went from {f_before} to {f_after}. This is a reinforcing loop when you answer damage by building more machines. Without your rebuilding, broken machines go quiet and the loop balances. Fighting doesn't lower the pull. |
| `goal_ids` | B2, B5, B3 |
| `instructional_purpose` | A reinforcing loop the player is inside; points at signature (the cause), not the symptom |
| `motivational_purpose` | Tension; the player's own choices set the difficulty |
| `hint_level` | 1 (question-form) |
| `science_claims` | Reinforcing loops are real (CCC: systems); the thawling behavior is invented |
| `fiction_note` | The thawlings are made up for this game. No life has ever been found on Mars. Loops like this one are real. |
| `status` | draft |

---

## 5. F7: Second solar event, thicker air

| Field | Value |
|---|---|
| `id` | `forecast.f7.solar_thicker` |
| `type` | forecast |
| `trigger` | First solar event scheduled once pressure ≥ 5 kPa (SME round 2: about 8× today's air overhead, a clear difference); issued with `log.solar.warn`. The scheduler guarantees one in Phase 2 |
| `lock` | `log.solar.shelter` at T−0.25 sol (it shows the expected surface dose) |
| `delivery` | As F2 |
| `prompt.ms` | The air is much thicker than at your first solar event. How much radiation will reach the ground this time? |
| `prompt.hs` | Same as MS |
| `choices` | Less than last time · More than last time · The same |
| `why.hs` | Why? — More air means more matter to absorb it · Thicker air reflects the Sun · The thawlings block it |
| `result.ms` | Less reached the ground. Radiation has to pass through the air. More air means more matter in the way to soak it up. Shelter is still safest. |
| `result.hs` | Surface dose was {dose_pct} % of the first event's. Thicker air absorbs more radiation on the way down. Mars still has no global magnetic field, so shelter stays the safe choice. |
| `goal_ids` | A4, B3 |
| `instructional_purpose` | Spaced retrieval of F2 with one changed variable |
| `motivational_purpose` | Visible proof that the player's work made the planet safer |
| `hint_level` | — |
| `science_claims` | Atmospheric mass absorbs radiation; more column, less surface dose **[SME: verify; and the size of the game's reduction]** |
| `fiction_note` | None |
| `status` | draft |

---

## 6. Earth link: Mensah's reply to Keeper's Report 2 (Phase 3 begins)

The key D2 message. Supports the class debrief in period 14 (the classroom graph itself is educator material: handoff to the Lead Instructional Designer).

| Field | Value |
|---|---|
| `id` | `earth.report2.reply` |
| `type` | recorded message (Earth desk) |
| `trigger` | Keeper's Report 2 sent (or gate + 1 sol if deferred), delay 0.5 sol × `story.delayScale` |
| `delivery` | Recorded message, text first |
| `text.ms` | Keeper, I read your report. On Earth we are adding greenhouse gas too. You do it with a plan. We did it without meaning to, mostly by burning coal, oil, and gas. Our records show more CO2 and a warmer Earth since the 1800s. Thawing frozen ground releases more carbon: a reinforcing loop, like yours. |
| `text.hs` | Same as MS. Read more: scientists conclude that human activities are the main cause of recent warming. Records: the Mauna Loa CO2 record and NASA's temperature record. Melting Arctic ice leaves darker sea that absorbs more sunlight: another reinforcing loop. The Arctic has warmed nearly 4 times faster than the globe. |
| `goal_ids` | D2, A3, B2, B5 |
| `instructional_purpose` | Evidence of Earth's warming and its main cause (MS-ESS3-5, HS-ESS2-4 context); a real reinforcing loop matched to the thawling loop; emissions as a side effect (B5) |
| `motivational_purpose` | The Mars story suddenly matters at home; no blame, no lecture |
| `hint_level` | — |
| `science_claims` | Human activity is the main cause of recent warming (IPCC AR6 WG1 SPM, not opened); NOAA Mauna Loa and NASA GISTEMP records (not opened); permafrost carbon (Schuur et al. 2015, not opened); Arctic ~3.8× global rate (round 2 [S5]). **The "about 1.1 °C" figure is left out until sourced** (SME 10.1) |
| `fiction_note` | None (no thawlings or Earth organisms paired in this message) |
| `status` | draft |
