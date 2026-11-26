# Messages: Phase 1 (Survival Desperation)

**Owner:** Lead Game Designer · **Status of every entry:** draft: SME check and ID review pending · Conventions: [README.md](README.md)

Order of first play: opening card → Landing checklist → Shift Briefing 1 → first storm (F1) → first solar event (warnings, F2) → ice near a heater (F4a, any time) → Dome stable → Hana's worked example → first Outgasser (F3) → Phase 2 begins (Keeper's Report 1, Mensah's reply).

---

## 1. Landing: BELL's first-shift checklist (story beat 0)

Controls first, content second (DESIGN.md, Landing). No forecast fires until the checklist is done or skipped.

| Field | Value |
|---|---|
| `id` | `landing.checklist` |
| `type` | onboarding checklist (8 steps) |
| `trigger` | First join on a planet (after the opening card) |
| `delivery` | BELL colony log panel, pinned at the side of the screen; each step ticks when the action is detected; the target object gets a soft outline (not color only: outline + small arrow icon) |
| `goal_ids` | C1 (life-critical needs first); no science goal: this teaches the controls schema |
| `instructional_purpose` | Lower extraneous load before any science: movement, suit O2, the Monitor, sealing, the airlock, the journal |
| `motivational_purpose` | Quick wins in the first minutes; "I can do this job" |
| `hint_level` | 1 (step text); 2 after 3 min stuck on a step: the outline pulses (≤ 1 per second) |
| `science_claims` | None |
| `fiction_note` | None |
| `status` | draft |

| Step | `text` (both bands) | Detected when |
|---|---|---|
| intro | Storm's over, Keeper. Damage logged. First-shift checklist is ready. **[Start checklist]** **[I know the drill]** | — |
| 1 | Walk to the shelter door. | Player reaches the door |
| 2 | Find your suit O2 (bubble icon). Top it up at the wall tank. | O2 refilled |
| 3 | Open the Planetary Monitor. Read today's pressure. | Monitor opened, pressure line hovered or clicked |
| 4 | Take a seal kit from the locker. | Seal kit in inventory |
| 5 | Hab B is leaking. Patch the cracked panel. | Breach sealed |
| 6 | Power is low. Switch on the backup battery. | Battery on |
| 7 | Step out through the airlock. Use the dust-off station coming back. | Airlock cycled out and in, dust-off used |
| 8 | Open your journal. Your shift notes live here. | Journal opened |
| done | Checklist done. Logging. | — |

**"I know the drill"** (skip) shows three lines, then Shift Briefing 1:
| Key | Text |
|---|---|
| `landing.skip.1` | Suit O2: bubble icon. Refill at wall tanks. |
| `landing.skip.2` | Monitor: planet data. Journal: your notes and messages. |
| `landing.skip.3` | Dust off after every trip outside. |

Length: 8 steps, ≤ 12 words each. Expected time 15–25 minutes with exploring (design estimate, to measure in playtest S1).

---

## 2. Shift Briefing 1 (advance organizer)

| Field | Value |
|---|---|
| `id` | `briefing.shift1` |
| `type` | Shift Briefing |
| `trigger` | Landing checklist done or skipped |
| `delivery` | BELL briefing card (one screen, dismiss with one click); copy kept in the journal under "Briefings" |
| `text.ms` | **Shift Briefing.** This shift: keep the dome alive. Watch O2, water, and power. Dust storms cut solar power. Run life support {hold_sols} sols with no failures, and the relay link comes back. |
| `text.hs` | Same as MS |
| `goal_ids` | C1, A1 |
| `instructional_purpose` | Advance organizer: names the job, the values to watch, the main threat, and the next goal before play starts |
| `motivational_purpose` | Clear goal; a reason to want the relay (people) |
| `hint_level` | — |
| `science_claims` | "Dust storms cut solar power" (see F1) |
| `fiction_note` | None |
| `status` | draft |

---

## 3. Solar-event warnings (story beat 5; UI spec §4)

Log lines and HUD only, never a pop-up (except the first-ever exposure warning, a life-threatening first: TEAM.md 8.6 rule 3).

| Field | Value |
|---|---|
| `id` | `log.solar.*` |
| `type` | colony log lines + HUD warning |
| `trigger` | Solar event scheduled; lines fire at T−1 sol, T−0.25 sol, onset, and end |
| `delivery` | Colony log; HUD sun-burst icon with countdown; Monitor "Space weather" line |
| `goal_ids` | A4, C1 |
| `instructional_purpose` | Gives time to predict (F2/F7) before the shelter advice reveals the answer; then a clear safety action |
| `motivational_purpose` | Tension with enough warning to plan; a fair threat |
| `hint_level` | 1 |
| `science_claims` | Solar particle events raise surface dose on Mars; shielding and depth reduce dose; more atmosphere reduces dose **[SME check]** |
| `fiction_note` | Warning time compressed: real warnings range from minutes to a day or more **[SME check]** |
| `status` | draft |

| Key | When | Text (both bands) |
|---|---|---|
| `log.solar.warn` | T−1 sol | Solar event expected in about 1 sol. Surface radiation will rise. |
| `log.solar.shelter` | T−0.25 sol (forecast locks first) | Solar event soon. Get under thick shielding. Shelter is in the core module. |
| `log.solar.onset` | Onset | Solar event in progress. Surface dose rising. |
| `log.solar.onset.first` | First-ever onset while the player is outside (blocking, rule 3) | You're outside in a solar event. Go under shielding now. |
| `log.solar.end` | End | Solar event over. Surface safe again. |

---

## 4. Hana's worked example: first Regolith Outgasser (story beat 6a)

Three levels, faded by machine (DESIGN.md, Worked example). Taped to the machine; opens as a journal page. "Hide Hana's notes" closes them for good (experienced players).

| Field | Value |
|---|---|
| `id` | `example.outgasser` (full), `example.processor` (completion), `example.later` (faded) |
| `type` | worked example (Hana's notes) |
| `trigger` | Full: first Regolith Outgasser placed. Completion: first atmospheric processor placed. Faded: first emitter, extractor, and later machine types |
| `delivery` | Note on the machine + journal page; one step per line, each with a checkbox that ticks itself |
| `goal_ids` | B1, C3, C1 |
| `instructional_purpose` | Worked example for novices: shows the steps **and** where to look for the effect (cause line). Faded to a completion problem, then to nothing (expertise reversal) |
| `motivational_purpose` | Hana as mentor; confidence on the first planet lever |
| `hint_level` | 1 (full) → 2 (completion) → 3 (faded) |
| `science_claims` | "Heating soil releases gas" (real, exaggerated: fiction note) |
| `fiction_note` | Real Mars soil doesn't hold enough gas to thicken the air this much. The game exaggerates it. |
| `status` | draft |

**Full (`example.outgasser`), both bands:**
| Step | Text |
|---|---|
| title | Hana's notes: your first Outgasser |
| 1 | Set it on solid ground near the dome. Short cables lose less power. |
| 2 | Feed it regolith. Plain surface dirt is fine. It bakes gas out. |
| 3 | It needs medium power. Can your battery carry it overnight? |
| 4 | Switch it on. BELL will ask for a forecast. Guess first. |
| 5 | After a sol, open the Monitor. Find "Outgasser" in the pressure causes. |
| 6 | One won't do much alone. That's normal. Watch the line, then decide. —H. |

The note never states the result (it would give away F3). It shows where to look.

**Completion (`example.processor`), both bands:**
| Step | Text |
|---|---|
| title | Hana's notes: atmospheric processor |
| 1 | Solid ground again. Keep it off ice. |
| 2 | Your turn: what does it need to run? |
| 3 | Power: more than the Outgasser. Plan for night. |
| 4 | Your turn: where will you see its effect? —H. |

**Faded (`example.later`):** *You know the drill by now. —H.*

---

## 5. F1: Dust storm and solar power (story beat 4)

| Field | Value |
|---|---|
| `id` | `forecast.f1.storm_power` |
| `type` | forecast |
| `trigger` | First dust storm scheduled; issued with `log.storm.warn` at T−1 sol |
| `lock` | Storm onset (solar output starts falling) |
| `delivery` | BELL forecast card at the next natural pause; result first in the queue at the next natural pause after onset + 0.25 sol |
| `prompt.ms` | A dust storm reaches the dome in about 1 sol. What will happen to your solar power? |
| `prompt.hs` | Same as MS |
| `choices` | It drops · It rises · It stays the same |
| `why.hs` | Why? — Dust blocks sunlight · Wind turns the panels away · Cold makes panels stronger |
| `result.ms` | Solar power dropped. Dust in the air blocks sunlight before it reaches your panels. Less light, less power. What could you store before the next storm? |
| `result.hs` | Solar power dropped. Dust blocks sunlight before it reaches your panels. Mars's thin air can't push hard enough to move panels; the dust is the problem. What could you store before the next one? |
| `goal_ids` | B3, C1 |
| `instructional_purpose` | First prediction; links the storm to power and to planning ahead. HS counters the film misconception of damaging Mars wind |
| `motivational_purpose` | A chance to be right about the planet before the first big threat; a nudge to prepare |
| `hint_level` | 1 (question-form hint about storage) |
| `science_claims` | Dust storms reduce sunlight at the surface and cut solar power (2018 storm ended Opportunity's mission) **[SME: verify]**; Mars winds exert little force because the air is thin **[SME: verify wording]** |
| `fiction_note` | None |
| `status` | draft |

---

## 6. F2: First solar event (story beat 5)

| Field | Value |
|---|---|
| `id` | `forecast.f2.solar_dose` |
| `type` | forecast |
| `trigger` | First solar event scheduled; issued with `log.solar.warn` at T−1 sol |
| `lock` | `log.solar.shelter` at T−0.25 sol (the shelter advice gives the answer away) |
| `delivery` | BELL forecast card at the next natural pause; result first in the queue at the next natural pause after the event ends |
| `prompt.ms` | A burst of radiation from the Sun arrives in about 1 sol. Where will you get the least radiation? |
| `prompt.hs` | A solar event arrives in about 1 sol. Where will a Keeper get the smallest radiation dose? |
| `choices` | Inside, under thick shielding · Out on open ground · On top of a hill · Next to a running machine |
| `why.hs` | Why does Mars's surface get more radiation than Earth's? — Thin air and no global magnetic field · Mars is closer to the Sun · Red soil gives off radiation |
| `result.ms` | Thick shielding stops most of it. Mars has thin air and no planet-wide magnetic field. So more of the Sun's radiation reaches the ground than on Earth. |
| `result.hs` | Shielding and depth block most of the dose. Thin air and no global magnetic field let more radiation reach Mars's surface. Mars is actually farther from the Sun than Earth; the difference is the shield, not the distance. |
| `goal_ids` | A4, B3 |
| `instructional_purpose` | Introduces why the surface is exposed; HS explanation corrects the "closer to the Sun" distractor |
| `motivational_purpose` | Survival choice with a clear safe action; tension |
| `hint_level` | — |
| `science_claims` | Mars has no global magnetic field, only crustal patches (SME review ref 24); thin atmosphere lets more radiation reach the surface; shielding reduces dose; Mars is farther from the Sun than Earth **[SME: verify dose claims]** |
| `fiction_note` | None |
| `status` | draft |

---

## 7. F3: First Regolith Outgasser

| Field | Value |
|---|---|
| `id` | `forecast.f3.outgasser_pressure` |
| `type` | forecast |
| `trigger` | Player presses Start on the first Regolith Outgasser (the machine screen is a natural pause) |
| `lock` | The player opens the Monitor's pressure page, or 1 sol passes, whichever is first |
| `delivery` | BELL forecast card on the machine screen; result at the next natural pause after 1 sol |
| `prompt.ms` | Your Outgasser is on. It bakes gas out of the soil. What will the Monitor show for pressure over the next sol? |
| `prompt.hs` | Same as MS |
| `choices` | Pressure rises slowly · Pressure jumps to Earth's level · Nothing changes |
| `why.hs` | Why so slowly? — A planet's air is huge next to one machine · The machine is broken · Gas leaks to space at once |
| `result.ms` | Pressure is rising, a little each sol. One machine adds a small amount of gas to a whole planet's air. What would two do? |
| `result.hs` | Pressure is rising slowly: about {rate} kPa per sol from one Outgasser. Earth's air is about 101 kPa. One machine adds a tiny amount to a whole planet's air. What would two do? |
| `goal_ids` | A1, B1, B3 |
| `instructional_purpose` | First cause and effect on the Index; the scale of a planet's atmosphere; points to the Monitor cause line |
| `motivational_purpose` | First visible lever on the planet; prompts the player to build more |
| `hint_level` | 1 |
| `science_claims` | Earth sea-level pressure about 101 kPa (SME-verified value); heating soil releases gas (exaggerated: fiction note) |
| `fiction_note` | Real Mars soil doesn't hold enough gas to thicken the air this much. The game exaggerates it. |
| `status` | draft |

---

## 8. F4a: Ice near a heater, thin air

**Design note (physics check, TEAM.md lesson "physics change triggers a forecast check").** The balance sheet's Phase 1 curve passes the triple point (0.61 kPa) at about sol 1. So in most of Phase 1, warmed ice **melts for a moment and the water boils away** (0.61–5 kPa row of the ice-to-water rule), not straight to vapor. The original correct choice ("turn straight to vapor, with no puddle") would be wrong for most players. The choices below are true in both pressure bands, and the result text picks the band. No numbers changed. **SME: please confirm.**

| Field | Value |
|---|---|
| `id` | `forecast.f4a.ice_thin_air` |
| `type` | forecast |
| `trigger` | First ice block whose local temperature passes 0 °C while global pressure < 5 kPa (Phase 1), and the Landing checklist is done |
| `lock` | The ice block changes state |
| `delivery` | BELL forecast card at once if the player is in the dome or at a machine screen, else at the next natural pause; the ice change is held up to 0.25 sol for the answer (heater warming curve) |
| `prompt.ms` | This ice is now above 0 °C, but the air is very thin. What will it do? |
| `prompt.hs` | Same as MS |
| `choices` | Vanish, leaving no water that lasts · Melt into a puddle that stays · Stay frozen |
| `why.hs` | Why? — The air is too thin for water to stay liquid · Ice on Mars isn't water · It's still too cold |
| `result.ms` (pressure < 0.61 kPa) | The ice turned straight into vapor. No puddle. The air is so thin that water can't be liquid at all. This jump is called sublimation. |
| `result.ms` (0.61–5 kPa) | The ice melted for a moment, then the water boiled away. The air is so thin that water boils near its freezing point. Warm is not enough. |
| `result.hs` (pressure < 0.61 kPa) | Below about 0.61 kPa, ice can't melt at all; it sublimates straight to vapor. Liquid water needs more pressure. Check the Monitor's water line. |
| `result.hs` (0.61–5 kPa) | Above 0.61 kPa ice can melt, but at this pressure water boils at about {boil_c} °C, so it boils away at once. Liquid water needs warmth and thicker air. |
| `goal_ids` | A2, B3 |
| `instructional_purpose` | The A2 misconception moment: phase changes depend on pressure, not only on 0 °C. Example for the F4b contrast |
| `motivational_purpose` | Surprise and discovery ("where did it go?") |
| `hint_level` | — |
| `science_claims` | Triple point 0.611657 kPa (SME ref 14); below it ice sublimates; just above it liquid forms briefly then boils or evaporates (SME refs 14, 15); boiling point at current pressure from the SME's Antoine table |
| `fiction_note` | None |
| `status` | draft (SME to confirm the two-band wording) |

**Earth link after F4a** (journal "On Earth" line, Keeper's notes voice):
| Field | Value |
|---|---|
| `id` | `earth.f4a.sublimation` |
| `type` | Earth link (journal line) |
| `trigger` | F4a result shown |
| `delivery` | Last line of the F4a journal page |
| `text.ms` | On Earth: frost can vanish on a cold, sunny morning without melting. Freeze-dried food is made the same way, at low pressure. |
| `text.hs` | Same as MS. Read more: on Earth this happens below freezing, when ice sublimates into very dry air; freeze-drying lowers the pressure to speed it up. |
| `goal_ids` | A2, D2 |
| `instructional_purpose` | One real Earth example of the same physics (transfer) |
| `motivational_purpose` | "That's in my kitchen too" |
| `hint_level` | — |
| `science_claims` | Frost can sublimate without melting; freeze-drying uses low pressure **[SME: verify examples]** |
| `fiction_note` | None |
| `status` | draft |

---

## 9. Earth link: Dome stable gate

| Field | Value |
|---|---|
| `id` | `earth.gate.dome_stable` |
| `type` | Earth link (gate journal entry, last line) |
| `trigger` | Dome stable gate |
| `delivery` | Journal gate entry, "On Earth" line |
| `text.ms` | On Earth, thick air presses on us all the time, for free. Mars's air pressure is less than 1 % of Earth's. |
| `text.hs` | Same as MS. Read more: Earth at sea level is about 101 kPa. Outside Cairn Dome it's about 0.6 kPa. Your dome has to make up the difference. |
| `goal_ids` | A1, D2 |
| `instructional_purpose` | Mars vs. Earth pressure as a ratio, anchored to the dome the player just stabilized |
| `motivational_purpose` | Pride: "I'm holding back a planet's worth of vacuum" |
| `hint_level` | — |
| `science_claims` | Mars surface pressure about 0.6 kPa, under 1 % of Earth's 101.3 kPa (SME Part A) |
| `fiction_note` | None |
| `status` | draft |

---

## 10. Earth link: Mensah's reply to Keeper's Report 1 (Phase 2 begins)

Mensah's first message. Voice: kind teacher, curious about the data, never quizzes.

| Field | Value |
|---|---|
| `id` | `earth.report1.reply` |
| `type` | recorded message (Earth desk) |
| `trigger` | Keeper's Report 1 sent (or the gate + 1 sol if deferred), delay 0.5 sol × `story.delayScale` |
| `delivery` | Recorded message, text first |
| `text.ms` | Keeper, this is Sefa Mensah at mission control. Your pressure curve made my week. On Earth, thick air keeps puddles and oceans liquid. On a high mountain the air is thinner, so water boils at a lower temperature. You're climbing the other way. |
| `text.hs` | Same as MS. Read more: on top of Everest (about 34 kPa), water boils near 70 °C. At your 5 kPa it boils near 33 °C. Every kPa you add raises it. |
| `goal_ids` | A1, A2, D2 |
| `instructional_purpose` | Earth example of pressure setting the boiling point; sets up the F4b contrast |
| `motivational_purpose` | Someone on Earth is reading your data; a new warm voice after the crew |
| `hint_level` | — |
| `science_claims` | Water boils at a lower temperature at altitude; Everest summit ~33.7 kPa (SME milestone value), boiling near 70 °C **[SME: verify]**; at 5 kPa boils near 33 °C (SME Antoine table) |
| `fiction_note` | None |
| `status` | draft |
