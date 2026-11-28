# Messages: Phase 1 (Survival Desperation)

**Owner:** Lead Game Designer · **Status of every entry:** draft: SME check and ID review pending · Conventions: [README.md](README.md)

**2026-11-27 update (proposed):** Landing (§1), Shift Briefing 1 (§2), `journal.sky.stylized` (§12) and `earth.gate.dome_stable` (§9) are superseded in detail by [../landing-spec.md](../landing-spec.md), which is the build-ready spec (per-step triggers, detection, keys, events). Where the two differ, the spec wins; the changed lines are marked below.

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
| 2 | Find your suit O2. Top it up at the wall tank. **Until suits exist (proposed): "Right-click the air tank by your bunk. Read the level."** | O2 refilled (until suits: tank right-clicked) |
| 3 | ~~Open the Planetary Monitor. Read today's pressure and air mix.~~ **Proposed (landing-spec §12 W1):** Open the Planetary Monitor. Find the Outside air line. | Monitor opened, pressure line and "Air mix" line shown (Air mix: about 95 % CO2, almost no oxygen; A1) |
| 4 | Take a seal kit from the locker. | Seal kit in inventory |
| 5 | ~~Hab B is leaking. Patch the cracked panel.~~ **Proposed: "Patch the crack in Hab B with your seal kit."** (Hab B holds no tank air, so "leaking" is not true of the simulation, CR-H1) | Cracked plate replaced, Hab B reads sealed |
| 6 | ~~Power is low. Switch on the backup battery.~~ **Proposed: "Place your two Ovens. Check Power on the Monitor."** The battery has no switch; it stays in the hint and the outline | Two Ovens placed and both powered |
| 7 | Step out through the airlock. Use the dust-off station coming back. **Until the station exists (proposed): "Go out through the airlock and come back in."** | Out and back in through the airlock (dust-off used, when built) |
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
| `delivery` | BELL briefing card in the side panel (non-modal, ui-specs §0); copy kept in the journal under "Briefings" |
| `text.ms` | **Shift Briefing.** This shift: keep the dome alive. Watch air, water, and power. ~~Dust storms cut solar power, so store some.~~ **Proposed 2026-11-28 (ID pass, landing-spec §12 W2):** Mars weather can change fast, so keep some power in reserve. Keep life support steady for {hold_sols} sols and the relay link comes back. If something nearly fails, ask why. (**proposed, 38 words**; the storm sentence is removed because it is F1's answer and the journal copy stays readable while F1 is open) |
| `text.hs` | Same as MS |
| `goal_ids` | C1, A1 |
| `instructional_purpose` | Advance organizer: names the job, the values to watch, the main threat, and the next goal before play starts |
| `motivational_purpose` | Clear goal; a reason to want the relay (people) |
| `hint_level` | — |
| `science_claims` | None (the storm claim moved to the F1 result) |
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
| `trigger` | Full: first Regolith Outgasser placed. Completion: first atmospheric processor placed. Faded: first emitter, ice extractor, and later machine types. "Hide Hana's notes" means they are **not shown again automatically**; every note stays in the journal |
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
| 2 | Feed it regolith. Plain surface dirt is fine. Heat bakes out the gas trapped in it. |
| 3 | It needs medium power. Can your battery carry it overnight? |
| 4 | Switch it on. BELL will ask for a forecast. Guess first. |
| 5 | After a sol, open the Monitor. Find "Outgasser" in the pressure causes. That line proves it's your machine moving the air. |
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
| `check.ms` (Phase 1 scaffold, both bands; hidden once the player's own accuracy is ≥ 1 in 3 after 3 forecasts, LEARNING.md §4.8) | Check: solar power on the Monitor's Dome tab. |
| `choices` | It drops · It rises · It stays the same |
| `why.hs` (**result card only**, logged unscored) | Why? — Dust in the air blocks sunlight · Strong wind damages the panels · The Sun itself gets dimmer during storms (last two **proposed**, pending SME) |
| `result.ms` / `result.hs` (same text; right and other variants in f1-storm-spec §1.5) | **You called it.** / **Here is what happened.** Solar power dropped. Dust in the air blocks sunlight before it reaches your panels. Look for the Storm line on the Monitor. What could you store before the next storm? |
| `result.detail` (Read more) | Choice line (`m1` to `m4`), then the SME explanation by band, then `journal.f1.real_vs_game`: full text in f1-storm-spec §1.5 |
| `goal_ids` | B3, C1 |
| `instructional_purpose` | First prediction; links the storm to power and to planning ahead. HS counters the film misconception of damaging Mars wind |
| `motivational_purpose` | A chance to be right about the planet before the first big threat; a nudge to prepare |
| `hint_level` | 1 (question-form hint about storage) |
| `science_claims` | Dust storms reduce sunlight at the surface and cut solar power (**verified**); the 2018 storm left Opportunity without enough power and NASA believes drained batteries ended its mission (**verified**; say "believes", never that the storm destroyed it); Mars winds push very little because the air is thin, true **today only** (SME; `journal.storm.wind` holds the time-qualified version) |
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
| `check.ms` (Phase 1 scaffold, both bands; hidden once the player's own accuracy is ≥ 1 in 3 after 3 forecasts, LEARNING.md §4.8) | Check: the Space weather line on the Monitor. |
| `choices` | Inside, under thick shielding · Out on open ground · On top of a hill · Next to a running machine |
| `why.hs` | Why does Mars's surface get more radiation than Earth's? — Thin air and no global magnetic field · Mars is closer to the Sun · The red color soaks up radiation |
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
| `lock` | 1 sol after Start. If the player opens the Monitor while the forecast is unanswered, the forecast card shows **first** (`forecast.look_first`); answering or skipping it then opens the Monitor. Looking never silently closes a forecast |
| `delivery` | BELL forecast card on the machine screen; result at the next natural pause after 1 sol |
| `prompt.ms` | Your Outgasser is on. It bakes gas out of the soil. What will the Monitor show for pressure over the next sol? |
| `prompt.hs` | Same as MS |
| `check.ms` (Phase 1 scaffold, both bands; hidden once the player's own accuracy is ≥ 1 in 3 after 3 forecasts, LEARNING.md §4.8) | Check: how far pressure moved since you landed. |
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

**Design note (SME round 2, §1.1 and §3.1).** Pressure passes the triple point (0.611 kPa) at about sol 1, so in Phase 1 warmed ice near a heater almost always **melts and boils away at once** (the heater is far above the boiling point, which is only 2–30 °C below 5 kPa). The pure "straight to vapor" case shows only in the first sol. The trigger, question, and choices follow the SME's fix: true for sublimation and for instant boiling. The result text picks the case.

| Field | Value |
|---|---|
| `id` | `forecast.f4a.ice_thin_air` |
| `type` | forecast |
| `trigger` | First ice block warmed above 0 °C locally while global pressure < 5 kPa, after Landing. Fallback: the oven sample tray (DESIGN.md, Core Design Check) |
| `lock` | The ice block changes state |
| `delivery` | Side-panel card at once if the player is in the dome or at a machine screen, else at the next natural pause; the ice change is held up to 0.25 sol for the answer (heater warming curve) |
| `prompt.ms` | The heater has warmed this ice above 0 °C. The air is very thin. What will it do? |
| `prompt.hs` | Same as MS |
| `check.ms` (Phase 1 scaffold, both bands; hidden once the player's own accuracy is ≥ 1 in 3 after 3 forecasts, LEARNING.md §4.8) | Check: the Monitor's water line. Where does water boil here? |
| `choices` | Disappear into vapor, leaving no water · Melt into a puddle that stays · Stay frozen |
| `why.hs` | Why? — In thin air, water boils at a low temperature · The heater destroys the water · It's still too cold |
| `result.ms` (local temp ≥ boiling point; the usual Phase 1 case) | The ice melted and boiled away at once. The air here is so thin that water boils just above freezing. Melted ice boils off before it can make a puddle. |
| `result.ms` (pressure < 0.611 kPa) | The ice turned straight into vapor. No puddle. The air is so thin that water can't be liquid at all. This is called sublimation. |
| `result.ms` (0 °C to boiling point, below 5 kPa) | The ice made a small puddle, then it dried up fast. In thin air, water boils near freezing and evaporates quickly. Warm is not enough. |
| `result.hs` (≥ boiling point) | At {pressure} kPa water boils at about {boil_c} °C. The heater is hotter than that, so melted ice boils off at once. Liquid water needs warmth **and** thicker air. Check the Monitor's water line. |
| `result.hs` (< 0.611 kPa) | Below 0.611 kPa (water's triple point), ice can't melt at all; it sublimates straight to vapor. Liquid water needs more pressure. |
| `result.hs` (0 °C to boiling point) | Below its boiling point ({boil_c} °C here) the water doesn't boil, but in thin, dry air it evaporates fast and cools until it refreezes. Pools need thicker air. |
| `goal_ids` | A2, B3 |
| `instructional_purpose` | The A2 misconception moment: phase changes depend on pressure, not only on 0 °C. Example for the F4b contrast |
| `motivational_purpose` | Surprise and discovery ("where did it go?") |
| `hint_level` | — |
| `science_claims` | Triple point 0.611657 kPa (AR [14]); boiling points by pressure (SME calc, Antoine: 0.7 kPa → 2 °C … 5 kPa → 33 °C); liquid below its boiling point evaporates fast in thin dry air (AR [15]) |
| `fiction_note` | None |
| `status` | draft (SME round 2 fix applied; ID to update the spec) |

**Earth link after F4a** (journal "On Earth" line, Keeper's notes voice):
| Field | Value |
|---|---|
| `id` | `earth.f4a.sublimation` |
| `type` | Earth link (journal line) |
| `trigger` | F4a result shown |
| `delivery` | Last line of the F4a journal page |
| `text.ms` | On Earth, ice can turn straight to vapor too. Old ice cubes shrink in the freezer, and freeze-dried food is made this way. |
| `text.hs` | Same as MS. Read more: this jump from ice to vapor is called sublimation. Freeze-drying lowers the pressure to speed it up. Real Mars ice does it too, slowly, even below 0 °C. |
| `goal_ids` | A2, D2 |
| `instructional_purpose` | One real Earth example of the same physics (transfer) |
| `motivational_purpose` | "That's in my kitchen too" |
| `hint_level` | — |
| `science_claims` | Freezer ice shrinkage and freeze-drying are sublimation (SME round 2 §3.2, approved; textbook, not opened); Phoenix saw ice sublimate (round 2 [S1][S2]) |
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
| `text.ms` | ~~Mars's air pushes with less than 1 % of the force of Earth's air. Earth's thick air protects us for free.~~ **Proposed (SME to confirm the second sentence):** On Earth, air pushes on you with more than 100 times the force of the air outside this dome. You never notice, because your body pushes back. |
| `journal.dome_stable.airmix` (gate entry, before the Earth line; both bands) | Outside air: about 95 % carbon dioxide, almost no oxygen, and very thin. You can't breathe it. |
| `text.hs` | Same as MS. Read more: Earth at sea level is about 101 kPa. Outside Cairn Dome it's about 0.6 kPa. **Proposed:** ~~Your hull holds back that difference.~~ **Further proposed 2026-11-28 (landing-spec §12 W4, SME to confirm):** Inside the dome, air pushes out on the hull harder than the thin air outside pushes in. The hull holds that difference. "More than 100 times the force" becomes "as hard as"; "95 %" becomes "95 percent" (§12 W5). |
| `goal_ids` | A1, D2 |
| `instructional_purpose` | Mars vs. Earth pressure as a ratio, anchored to the dome the player just stabilized |
| `motivational_purpose` | Pride: "I'm holding back a planet's worth of vacuum" |
| `hint_level` | — |
| `science_claims` | Mars surface pressure about 0.6 kPa, under 1 % of Earth's 101.3 kPa; CO2 95.1 %, O2 0.145 % (SME Part A, sourced) |
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
| `text.ms` | Keeper, this is Sefa Mensah at mission control. Your pressure curve made my week. On high mountains, water boils below 100 °C. On top of Everest it boils at about 70 °C. You're climbing the other way. |
| `text.hs` | Same as MS. Read more: Everest's summit air is about 34 kPa. At your 5 kPa, water boils at about 33 °C. Every kPa you add raises it. |
| `goal_ids` | A1, A2, D2 |
| `instructional_purpose` | Earth example of pressure setting the boiling point; sets up the F4b contrast |
| `motivational_purpose` | Someone on Earth is reading your data; a new warm voice after the crew |
| `hint_level` | — |
| `science_claims` | Everest summit about 33.7 kPa → boils at about 72 °C (SME calc, AR [17]; approved line says "about 70"); at 5 kPa about 33 °C (SME calc) |
| `fiction_note` | None |
| `status` | draft |

---

## 11. Forecast card on Monitor open (`forecast.look_first`)

| Field | Value |
|---|---|
| `id` | `forecast.look_first` |
| `type` | forecast card variant |
| `trigger` | Player opens the Monitor while they have an unanswered F3, F5, or F8 (the forecasts the Monitor would reveal) |
| `delivery` | The forecast card shows before the Monitor sheet; **Answer** or **Skip this one** then opens the Monitor. Skipped = logged as unanswered, never wrong |
| `text.ms` | Log your forecast before you look. |
| `text.hs` | Same as MS |
| `goal_ids` | B3, C3 |
| `instructional_purpose` | Checking data (C3) never costs the player their prediction evidence (B3) silently |
| `motivational_purpose` | A fair rule the player can see |
| `status` | draft |

## 12. Journal notes (world-cue explanations; both bands, MS text)

| `id` | Trigger | Text | Science (SME round 2) |
|---|---|---|---|
| `journal.outgasser.puff` | First Outgasser running | The white puff is water vapor freezing into tiny ice crystals, like your breath on a cold day. The CO2 coming out is invisible. | §2.5, accurate |
| `journal.sound.thin_air` | First trip outside after Landing | Thin air carries sound poorly. Sounds are quiet, and high pitches fade fast. As the air thickens, sound gets louder and fuller. | §4, accurate |
| `journal.mast.balloon` | First look at the weather mast (or Landing step 7) | The lift balloon floats higher as the air gets thicker, because thicker air pushes up on it harder. | §2.7, acceptable simplification |
| `journal.sky.stylized` | **Proposed:** latch of `mars:dome_stable` or first Outgasser placed (before any sky change); repeated at Phase 2 begins | More air scatters more blue light, so the sky slowly loses its dusty tan. Nobody has seen a thickened Mars sky. These in-between colors are our best guess, made stronger so you can see them. (SME-required wording, verbatim: `docs/science/round2-review.md` §2.1) | §6 note 8 |

## 13. Shift log (optional end-of-session mini-review, classroom)

| Field | Value |
|---|---|
| `id` | `log.shift_end` |
| `type` | end-of-session review (BELL) |
| `trigger` | Teacher ends the session (world pauses); at the pause, never in the last minutes of play (I-C rule 5) |
| `delivery` | Side panel, three lines from the player's **own** data; copy in the journal |
| `text.ms` | **Shift log.** You did: {own_highlight}. Forecast: {last_forecast_result}. Open job: {open_job}. |
| `text.hs` | Same as MS |
| `goal_ids` | B3, C2, C3 (self-assessment) |
| `instructional_purpose` | A small after-action review: one action, one prediction result, one next step |
| `motivational_purpose` | Ends the class on something done, not a failure; sets up the next session |
| `hint_level` | — |
| `science_claims` | None |
| `status` | draft (fits the story: BELL already writes shift logs) |

---

## 14. Solar panel dust: journal notes (from the ID's `docs/learning/journal-dust.md` §1–6)

Facts and claim tags are unchanged from the ID's spec (SME wording, `docs/science/dust-review.md`). Voice: the Keeper's journal. **Shipping rule:** a *searched* number never appears in game text until its source is opened, so the **fallback (no-number) texts ship** for `journal.dust.panels` and `earth.dust.solar_farms`. **Exception (2026-11-28):** `journal.f1.real_vs_game` ships the numbered text (SME opened the NASA pages; 97 percent is calc, "about" kept), see `docs/learning/journal-dust.md` §6. The numbered versions stay in the ID's file, ready to swap in when the SME opens the sources. The wiper note is in phase-2.md §7.

### `journal.dust.panels` (main note)
| Field | Value |
|---|---|
| `id` | `journal.dust.panels` |
| `type` | journal entry ("what happened" page, diagram 9 **one-path** variant: dust on the panel only; no sky, storm or air path, CR-F11. The two-path variant (dust in the air vs. dust on the panel) is on `journal.storm.first`, after F1 resolves) |
| `trigger` | First panel on the planet reaches dust stage 1 (same moment as BELL's "New on the Monitor: panel dust") |
| `delivery` | Journal page at the next natural pause; colony log points to it |
| `text.ms` (ships) | **Dust on the panels.** Fine Martian dust lands on solar panels. On real Mars, power fell a little each day. Some days wind blew dust off and power jumped back. One lander, InSight, was never cleaned. Its power fell from about 5,000 to about 500 watt-hours a sol in under four years. **In the game, dust builds much faster than on real Mars, so you can see it.** |
| `text.hs` (read more) | Held until the SME opens Lorenz et al. 2021 (it carries searched numbers). Until then HS sees the MS text |
| `goal_ids` | C1, B1, A1 |
| `instructional_purpose` | Explains the power drift; separates panel dust from storm dimming; the required honesty note that the game rate is compressed |
| `motivational_purpose` | A real-mission story that makes upkeep feel like the real job |
| `science_claims` | InSight 5,000 → 500 Wh/sol (**sourced**, PIA25284); "under four years" (**calc**); "a little each day" and wind cleaning (**searched**, qualitative, no number) |
| `fiction_note` | Not fiction; the speed is a stated simplification |
| `status` | draft; ID-reviewed; fallback text |

### Short honesty notes (both bands; never band-gated)
| `id` | Trigger | Text | Claim tags |
|---|---|---|---|
| `journal.dust.floor` | First panel reaches stage 4 | The game never lets a dusty panel fall below 60 percent. A real panel that's never cleaned keeps losing power. InSight's fell to about a tenth. The game stops at 60 percent so you have time to react. | InSight ~a tenth (**sourced** + **calc**); 60 % (**invented**). Can ship |
| `journal.dust.storm_timing` | End of the first storm that adds panel dust (after F1 resolves) | In real storms, dust keeps falling out of the air for weeks after the sky starts to clear, and some of it lands on panels. The game adds it all when the storm ends, to keep it simple. | Fallout continues (**searched**, qualitative). Pending SME: can "days" ship without a number? |
| `journal.dust.wind` | First dust-devil cleaning on the planet | Wind cleaned some real panels, like Spirit's. It never cleaned InSight's. | **sourced**/**searched**, no number |
| `journal.dust.not_only_power` | Dust page, after the first panel cleaning | Dust also gets into joints and air filters. Power is just the easiest thing to measure. | General (**unverified** in detail); "air filters" pending SME |
| `journal.dust.rain` | First Phase 3 rain that cleans panels | On Earth, rain helps, but the fine dust stays. Your panels won't come fully clean either. | **searched** (NREL), qualitative |
| `journal.dust.base` (HS read more) | Dust page, read more | In the game, panels near your digging and machines get dustier. On real Mars, rocket landings throw dust, but no one has measured dust from a base's daily work. | Plumes **searched**; daily-work: real value missing; ×2 **invented** |
| `journal.dust.brush` | First panel brush crafted | No Mars robot ever carried a panel brush. A person with a brush is something only a crewed base would have. | **searched**; fiction note |

Goals: C1, B1, A1, B5 (base note). Misconceptions prevented: "dust can never kill power", "storms coat panels instantly", "wind always cleans", "rain washes everything clean", "dust only hurts power", "rovers were brushed".

### `earth.dust.solar_farms` (D2 Earth link, last line of the dust page)
| Field | Value |
|---|---|
| `id` | `earth.dust.solar_farms` |
| `type` | Earth link |
| `trigger` | Shown with `journal.dust.panels` |
| `text.ms` (ships) | **On Earth:** desert solar farms lose power to dust too, and they're cleaned with water, brushes, or robots. Rain helps, but it leaves the fine dust. |
| `text.hs` (read more) | Same idea as on Mars: losses build slowly, and someone has to plan the cleaning. Real farms weigh cleaning costs (water, labor) against lost power. |
| `goal_ids` | D2, C1, B4 |
| `science_claims` | Cleaning methods, rain and fine dust (**searched**, qualitative); cost trade-off (general, **unverified**, no number) |
| `status` | draft; fallback text |

### `journal.f1.real_vs_game` (read-more line on the F1 journal page, after F1 resolves)
| Field | Value |
|---|---|
| `id` | `journal.f1.real_vs_game` |
| `trigger` | F1 resolved (never before the lock, CR-F9) |
| `text.ms` (**ships**, ID wording) | In 2018, a storm that covered all of Mars cut the rover Opportunity's power by about 97 percent. Your storms cut {max_cut} percent at most. Real storms can be bigger, and they last days to months. Yours last a sol or less, so you can keep playing. (The old no-number line stays as a switch.) |
| `text.hs` | MS text, then: NASA believes the rover's batteries ran out, and that ended its mission. |
| `goal_ids` | C1, A1 |
| `science_claims` | 97 percent: calc from 652 to 22 Wh (verified, baseline secondary); "NASA believes" (verified); {max_cut} invented |
| `status` | draft; fallback text |
