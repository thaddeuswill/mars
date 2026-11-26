# Audio Direction: Mars Terraforming

**Owner:** Lead Artist (audio owner by user decision, 2026-11-26) · **Status:** Draft 1 (2026-11-26) · **Stability:** sound design, levels, and mix values are **fluid** (TEAM.md 8.1). The principles in §1 and the accessibility rules in §1.4 are meant to be **stable** once the team agrees to them.
**Closes:** DESIGN.md "Learning Mechanisms in Play", row "Sound builds context; use it sparingly" (⬜ gap), and the audio side of the "Feedback must be salient" row.
**Depends on:** [DESIGN.md](../../DESIGN.md), [story bible](../design/story-bible.md), [LEARNING.md](../../LEARNING.md) §4.6 (interruption rules), [TEAM.md](../../TEAM.md) 8.6, [planetary keyframes](../art/planetary-keyframes.md), [style guide](../art/style-guide.md).
**Companion:** [sound-list.md](sound-list.md) (every sound, with status, subtitle, and visual twin).

> **SME round 2 (2026-11-26, [round2-review.md](../science/round2-review.md) §4): the pressure-driven sound claim is accurate** (Maurice et al., 2022, *Nature* 605, SME [S22], [S26]). Values marked **[SME ✓]** were checked and kept; rows still marked **[SME]** are new and not yet checked.

> **Technical Lead round 2** designed the hooks (ARCHITECTURE.md §2.12). §9 now records how this direction fits them, and where it differs.

---

## 1. Principles

### 1.1 Direction in one paragraph
**The planet is quiet, and you make it loud.** At the start, the outside is nearly silent: a faint, low wind through the helmet, your own breath and suit fan, your boots on rock. Inside the dome there is a warm hum of machines keeping you alive. Every kilopascal the player adds makes the outside a little louder and brighter in sound, until at equilibrium the player takes the helmet off and hears wind, water, leaves, and animals at full range. Sound is a **second channel for the Planetary Index** (pressure above all), a **warning system** kept for real danger, and a quiet **feedback layer** for the colony log. It is never the only way to learn anything.

### 1.2 The five rules

| # | Rule | What it means in practice | Why |
|---|---|---|---|
| 1 | **Sparing and salient** | Silence is the default. A sound plays only if it tells the player something (a state, a change, a danger) or builds the setting. Feedback cues are short (≤ 0.8 s), distinct, and rate-limited (§7.3). At most **6 learnable feedback meanings** plus **3 alarm patterns** in the whole game (§7.1, §5.1) | Sound builds context only when it is used sparingly (DESIGN.md, Multimedia). Too many cues become noise and add extraneous load. The "about six" limit is a rule of thumb from alarm design practice, **unverified** as a number |
| 2 | **Alarms only for life-threatening events** | The alarm list in §5.1 is closed. Machine failures, fauna raids, storms, drift events, and wrong forecasts are **not** alarms | Alarm fatigue: if everything alarms, nothing does. Also TEAM.md 8.6 rule 3 (only life-threatening events may interrupt) |
| 3 | **Never feedback sound over voice** | Any voice line (none planned at launch; BELL voice may come after the Stage 3 playtest) holds all feedback cues until it ends. Alarms are the one exception: an alarm **pauses** the voice line, which resumes from the start of its sentence when the alarm stops (§7.4) | Two verbal-channel signals at once compete (DESIGN.md Story Approach: "never while an alarm is sounding") |
| 4 | **Subtitles for every cue** | Every sound event has a subtitle string (vanilla subtitle system), in plain words, ≤ 4 words (sound-list). Pressure-dependent ambience has pressure-dependent subtitles (§3.4), so a player who can't hear still gets the pressure cue | Accessibility; style guide §4.7 |
| 5 | **No reliance on sound alone** | Every sound that carries meaning has a **visual twin** listed in the sound list. A player with sound off loses mood, never information | Many classrooms will play muted or with one shared speaker (assumption, **unverified**; see §1.4); deaf and hard-of-hearing players |

### 1.3 Where we stand on fidelity (TEAM.md §3)
| Type | Target for audio | Meaning |
|---|---|---|
| **Physical fidelity** | **Moderate** | Sounds are plausible and grounded in what real Mars recordings and physics suggest, then compressed so the game stays playable (a real 0.6 kPa outside would be too quiet to use as a cue) |
| **Functional fidelity** | **High where it teaches** | Sound changes in the **right direction** with pressure, shielding, O2 (fire), and machine state. Honest silences: sublimation and aurora make no sound (§10) |
| **Temporal fidelity** | **Low** | Follows the compressed Index, with the same 30 s smoothing as the sky (§3.3) |

### 1.4 Accessibility and classroom rules (must pass)
- **Subtitles:** every event has a subtitle key `subtitles.mars.<event>`. Proposed default: **subtitles on** in the classroom pack's default options, off in standard with a first-launch prompt (handoff to the Technical Lead and Lead Instructional Designer).
- **Visual twins:** every meaningful cue has one (sound list column "Visual twin"). Audit rule: if the twin column is empty, the sound may only build mood.
- **Laptop speakers:** cues that carry meaning have their main energy between **500 Hz and 4 kHz**, where small laptop speakers work (assumption; laptop speakers roll off in the low bass, **unverified** cutoff). Low rumbles are mood only.
- **Hearing differences:** alarm patterns differ in **rhythm**, not only pitch, so players with high-frequency hearing loss can tell them apart.
- **Volume categories:** every sound is in a vanilla category so players can turn groups down (proposed mapping in §9.4). Alarms are never only in a category that is commonly muted (music).
- **Classroom noise:** 20–30 laptops in one room. Nothing loops loudly while idle; the dome hum and suit fan are quiet beds (§8). A teacher can turn the pack's ambience off without losing any information (rule 5).
- **No time-pressured listening:** nothing important is said only once in sound. Messages are text-first and archived (story bible §6.1).

### 1.5 Originality and sources
- **Original sounds only.** Every shipped sound is recorded or synthesized by the team for this project. No sample-library packs, no sounds copied or resampled from vanilla Minecraft, Create, Mekanism, or any other game or mod, and no close imitations of a known game's signature sounds.
- Vanilla actions keep vanilla sounds (footsteps on vanilla blocks, inventory clicks): that is the schema players already have (DESIGN.md, Cognitive load). We don't ship copies of them.
- Third-party machines (Create, Mekanism) keep their own sounds; they are optional support machines (DESIGN.md).
- **Placeholders** may be synthesized by script; file names end in `_placeholder` and the sound list marks them **Placeholder**.
- Real Mars recordings (Perseverance microphones) are used as **reference for direction only**. Putting a real recording into the journal is a separate decision for the user (report, Decisions needed).

---

## 2. Listening model: what the player hears, and through what

Every sound belongs to one **path**. The path decides which filters apply. This is the core of the pressure-driven design.

| Path | Examples | Pressure filter (§3) | Helmet filter (§3.5) | Notes |
|---|---|---|---|---|
| **Exterior airborne** | Wind, rain, water, thawlings, exterior machines, fire outside, windsock | **Yes** | Yes, while helmet on | The path that teaches pressure |
| **Contact (through the suit and ground)** | Own footsteps, tools on blocks the player touches, dust grains hitting the visor, ground creaks under the player | No | No | Sound you feel through your body and suit, so it stays audible in thin air. **SME ✓ plausible** (round 3: suits are pressurized to about 29.6 kPa, so sound carries inside them) |
| **Suit** | Suit fan, breathing, helmet seal, dosimeter clicks, suit alarms | No | n/a (inside the helmet) | Always audible while worn |
| **Interior (sealed volume)** | Dome hum, interior machines, airlock, Monitor, Shift Board | No (dome air is at habitable pressure) | Only if the player wears the helmet indoors | Uses the sealing system to know the player is inside |
| **Exterior heard from inside** | Storm, outside machines, thawlings, heard through dome walls | Yes, then × 0.30 gain (the thin bed is used whatever the pressure, so it stays muffled) | No | Thin outside air plus walls: you barely hear the outside from in the dome |
| **Interface** | Colony log chime, message cues, gate chime, failure and warning tones | No | No | Non-positional (heard the same anywhere). Never filtered, so feedback stays readable |

**Listener state** (which path rules apply) comes from: in a sealed volume or not (sealing system, DESIGN.md §3), helmet on or off, and the live breathable condition.

---

## 3. Pressure-driven ambient sound (SME ✓)

### 3.1 The claim and its grounding (SME ✓)
**Claim (SME ✓):** at 0.6 kPa, outdoor sound is thin and faint; as the atmosphere thickens, outdoor sound gets louder, carries farther, and keeps more high frequencies.

**Verdict: accurate** (SME round 2 §4). The grounding:
1. **Thin air radiates less sound.** For the same vibrating source, the sound power carried by air grows with the air's density (acoustic impedance, density × speed of sound). Mars air at the surface is roughly 1 % of Earth's density, so sources sound much quieter. Maurice et al. (2022) predict, from the low pressure and CO2's properties, that sounds are about **20 dB weaker** on Mars than on Earth (a model prediction from acoustic impedance, not a side-by-side measurement; SME [S22], [S26]). Most sounds fade over about **one-eighth** of the distance they would on Earth [S26].
2. **High pitches fade faster with distance on Mars.** Low-pressure CO2 absorbs high frequencies strongly, so far sounds lose their treble. Attenuation with distance was measured above 2 kHz, and there are **two speeds of sound** (about 240 m/s below 240 Hz and about 250 m/s above; SME [S22]). A fun high school fact; not needed in play.
3. **At 75 kPa** (mostly filler gas plus O2, DESIGN.md §1) sound would be close to Earth's.

**Deliberate simplifications** (stylization log, §10): we drive the change from **pressure only** and ignore the change in gas mix (CO2 to filler gas plus O2), which also matters. And we **compress** the range: start gain is 0.25 (about −12 dB), not about −20 dB, so the player can still hear wind and machines and *notice the change* (SME: acceptable simplification for play). A journal "real vs. game" note can say so, using the SME's wording: "Thin air carries sound poorly: sounds are quieter and high pitches fade fast. As the air thickens, sound gets louder and fuller." (handoff to the Lead Game Designer).

### 3.2 Pressure keyframes for sound
**Input:** Index pressure (kPa), **log10 axis**, same keyframes and the same 30 s smoothing as the sky (planetary keyframes §1.3, §3.1). Values apply to the **exterior airborne** path only.

| Property | 0.6 kPa | 5 kPa | 15 kPa | 30 kPa | 75 kPa | Status |
|---|---|---|---|---|---|---|
| Exterior gain (linear amplitude) | 0.25 | 0.40 | 0.60 | 0.75 | 1.00 | **[SME ✓]** direction accurate; milder than real (acceptable). Applied to every exterior sound, including vanilla and third-party ones (gain only, ARCHITECTURE.md §2.12) |
| Exterior low-pass cutoff | 1.5 kHz | 3 kHz | 5 kHz | 8 kHz | Off | **[SME ✓]** direction accurate. Baked into **pre-filtered files** (thin, mid, full) for our ambient beds and key teaching sounds (§9); no real-time filter in Stages 3–4 |
| Exterior audible range (× each sound's normal attenuation distance) | 0.50 | 0.60 | 0.75 | 0.90 | 1.00 | Direction matches SME [S26] (real: about one-eighth of Earth's distance; ours is milder). **Stage 5 optional**: not in the TL's Stage 3–4 hooks |
| Wind bed level (before exterior gain) | 0.15 | 0.30 | 0.50 | 0.70 | 0.80 | Style choice |
| Wind bed character | Low rumble only, no whistle | Rumble + faint hiss | Hiss + gusts | Gusts + soft whistle on edges | Full wind, gusts, rustle (if biomass) | Style choice; follows the low-pass |
| Windsock flutter level (weather mast, keyframes §3.9) | 0 (silent) | 0.20 | 0.50 | 0.80 | 1.00 | Style choice; matches the windsock angle |
| Ambient bed in use (§3.4) | `wind_thin` | `wind_mid` | `wind_mid` | `wind_full` | `wind_full` | Crossfade over 10 s at 5 and 30 kPa |
| Ambience subtitle (§3.4) | "Faint, thin wind" | "Muffled wind" | "Muffled wind" | "Wind" | "Wind" | Band edges: < 5, 5–30, ≥ 30 kPa |

Effective level of an exterior sound = file level × its own volume × exterior gain × helmet filter (if worn) × distance falloff (using the scaled audible range).

### 3.3 Change over time
- Sound uses the **same smoothed pressure value** as the sky (30 s time constant), so sky, fog, windsock, and sound shift together. A player who hears the outside get louder should be able to look up and see why (B1).
- **Regression is audible.** Falling pressure muffles the outside again (keyframes §5).
- **Gate moment (Phase 2 begins, 5 kPa):** no stinger. The change is gradual by design; the gate chime (§7.1) marks the gate.

### 3.4 Subtitle parity for pressure
Vanilla subtitles show what plays, not how it sounds. So the exterior wind bed is **three sound events**, one per pressure band, each pre-filtered and with its own subtitle: `ambient.exterior.wind_thin` ("Faint, thin wind", < 5 kPa, low-passed at 1.5 kHz), `ambient.exterior.wind_mid` ("Muffled wind", 5–30 kPa, low-passed at 5 kHz), `ambient.exterior.wind_full` ("Wind", ≥ 30 kPa, unfiltered). They crossfade over 10 s at the band edges. A deaf player gets the same pressure cue a hearing player does.

### 3.5 Helmet filter
While the helmet is on, exterior airborne sound also passes a helmet filter: **gain × 0.70 (about −3 dB)**, plus the suit fan bed masking it lightly. The muffling is baked into one extra file, `wind_full_helmet` (the full bed low-passed at 6 kHz), which plays instead of `wind_full` while the helmet is on; at helmet-off it crossfades to `wind_full`. This is a stylization (helmets do muffle sound). It is what makes the helmet-off moment open up even at 75 kPa, where the pressure filter is already fully open (§4).

### 3.6 Other ambience layers (exterior, all pass the pressure filter)

| Layer | Driving input | Keyframes | Goals |
|---|---|---|---|
| Ice cracks near warm machines | **Local temperature** (per-chunk heat map) and global temperature | None below −20 °C; occasional (1 per 30–60 s per warm chunk) at −20; frequent by day (1 per 10–20 s) near 0 °C; rare at 5 °C (keyframes §3.2) | A2, B1 |
| Drips and water lapping | **Hydrosphere** (%) and nearby water blocks | None at 0 %; drips at 2 %; lapping at pools from 10 %; small waves at 20–30 % | A2 |
| Rain | **Hydrosphere** (rain events, keyframes §3.3) | Light rain from 20 %; regular rain at 30 % | A2, B2 |
| Leaf and grass rustle | **Biomass** (BU) | None < 150; faint at 150; normal at 750; more variety at 1,500 (keyframes §3.6) | A5 |
| Distant thawling calls | **Fauna state** (stirring, later life stage, settled) | Only after Thaw; settled groups near quiet zones call softly at dusk | D1, B5 |

---

## 4. The helmet-off moment (Breathable gate, story beat 17)

The emotional peak (story bible §5.4). It should sound like a breath of open air. Extends style guide §9.2, which keeps the visual timeline.

**Driving inputs:** the **breathable condition** (live: O2 partial pressure, pressure, CO2 partial pressure, DESIGN.md §2) and the **player's helmet action**. Goals: **A5, A1** (the air is breathable because the whole system holds).

| Time | Sound | Level and notes |
|---|---|---|
| Before | Normal outdoor mix at ≥ 75 kPa: pressure filter open, helmet filter on, suit fan bed | — |
| 0.0 s | **Seal release:** soft latch click, then a short hiss (0.4 s) | Suit path. Subtitle "Helmet seal opens" |
| 0.0–1.5 s | Suit fan and any breathing loop fade out | Linear fade, 1.5 s |
| 1.5–4.5 s | **Helmet filter lifts:** exterior gain 0.70 → 1.00, and `wind_full_helmet` crossfades to `wind_full`, over 3 s. Wind, water, leaves, distant thawling chirrs open to full range | No new sounds added; the world was already there. The change is that *you* hear it now |
| 2.0 s | **First breath:** one slow unvoiced inhale and exhale, close to the listener | Breath only, no voice, no gendered sound (DESIGN.md voice decisions). Subtitle "You breathe in" |
| 4.5–10 s | Open world sound only. **No music sting, no chime** | Let the world be heard |
| ≥ 10 s | Moreno's message slip arrives with the normal message cue (§7.2) at −3 dB | Moved from 4.5 s in the first draft so the cue doesn't land on the moment (style guide §9.2 updated) |

- **Not an alarm, not a reward jingle.** The reward is the sound world itself.
- **Every later helmet removal** uses the seal release and the filter lift, without the first breath and without the message.
- **Live condition fails while the helmet is off:** the **Air alarm** (§5.1) plays, the visor returns with a warning (style guide §9.2), and the helmet filter closes back in over 1 s. Not deferred, not merged.
- **Group planets:** each player hears their own moment when they remove their own helmet. Moreno's message arrives once per planet (ARCHITECTURE.md §9.2), so on a group planet it follows the first player's removal.

---

## 5. Alarms (life-threatening only)

### 5.1 The closed list
Only these events may sound an alarm. Adding one needs the Lead Instructional Designer and Lead Game Designer (TEAM.md 8.6 rule 3).

| Alarm pattern | Events (life-threatening) | Driving input | Rhythm (distinct by rhythm, not just pitch) | Visual twin |
|---|---|---|---|---|
| **Air alarm** | Suit O2 nearly out (threshold: Lead Game Designer; placeholder ≤ 60 s left); suit breach; dome breach while the player is inside the leaking volume; air turns unbreathable while the helmet is off | Suit O2, suit integrity, sealed-volume pressure loss, breathable condition | Two tones, low then high (≈ 660 Hz, 880 Hz), **3 pulses of 0.2 s with 0.15 s gaps**, then 2 s of silence; repeats | HUD octagon "AIR" icon pulsing at 2 Hz (≤ 3 flashes/s), O2 number, visor edge in failure color + word "FAILED"/"WARNING" (style guide §4.4) |
| **Radiation alarm** | Radiation dose reaches the harmful level (threshold: Lead Game Designer) | Radiation dose (suit dosimeter) | One tone (≈ 1 kHz), **fast even pulses: 6 of 0.08 s**, then 2 s of silence; repeats. Overlays the dosimeter clicks | Trefoil icon solid with "DOSE" word, dose number, shelter arrow if the Lead Game Designer adopts one |
| **Fire alarm** | Fire inside a sealed volume (O2 overshoot fire, beat 15, reaching the dome) | Fire blocks in the player's sealed volume | **One long falling tone** (1 kHz → 600 Hz over 0.8 s), then 1.5 s silence; repeats | Flame icon with "FIRE" word; visible flames (keyframes §3.4) |

**Not alarms** (they use the Warning or Failure tones in §7.1, or no sound): machine failure, thawling raid, dust storm arriving, solar event forecast, drift event, ground collapse warning, low battery, perchlorate exposure, outdoor fire away from the dome, a wrong forecast.

### 5.2 Alarm behavior
| Rule | Value |
|---|---|
| Repeats | While the condition holds. After **3 full repeats**, it drops to a reminder (one pattern every 15 s) until resolved, so the alarm doesn't drown out the player's thinking. The HUD twin stays on |
| Acknowledge | No acknowledge button at launch (the fix is the acknowledgement). Revisit if playtests show it frustrates |
| Priority | Highest. Ducks every other sound by **−10 dB** while a pattern plays. Holds all feedback cues. Pauses voice (rule 3) |
| Several at once | Play the pattern for the most urgent: Air, then Fire, then Radiation. The HUD shows all |
| First time | The first Air alarm and first breach are also the only blocking warnings (TEAM.md 8.6 rule 3). Visual spec: Lead Game Designer's UI spec |
| Group planets | Suit alarms are heard by the affected player only. Dome breach and dome fire alarms are heard by every player inside that sealed volume |
| Classroom end-of-session | Alarms are never suppressed. (Rule 5 of 8.6 stops *new critical failures* from being triggered in the last ~5 minutes; it doesn't silence real danger) |

---

## 6. Storms and radiation spikes

### 6.1 Dust storms
**Driving input:** **storm strength** (0–1, derived weather state; capped by gameplay phase: 1.0 / 0.8 / 0.5 / 0.25 at equilibrium, keyframes §3.7) × the pressure filter for the airborne parts. Goals: **C1** (planning around power loss), **B2** (storms fade as the planet thickens).

| Layer | Path | Level at full strength | Notes |
|---|---|---|---|
| **Grain patter on the visor and suit** | Contact | 0.6 | Fine, dry ticking. Audible even in thin air, because the grains hit *you*. This is the storm's main sound in Phase 1 |
| Airborne dust hiss and wind | Exterior | 0.8 × exterior gain | Mostly a muffled rumble at 0.6 kPa; becomes an audible hiss as pressure rises |
| Patter on dome glass | Exterior heard from inside | 0.35 | So players in the dome know a storm is on |
| Solar power drop | Interior | — | The dome hum drops pitch by 2 semitones and lights flicker once when output falls below demand (visual twin: Monitor power row, lamp flicker) |

- **No howling gale, no flying-debris whooshes, no thunder.** Thin Mars air can't throw objects (DESIGN.md audit: avoid the film misconception). The sound must match the slow sideways dust in the visuals.
- **Ramp:** in over 60 s, out over 90 s (matches visuals).
- **Storm warning:** BELL's log line uses the **Warning** tone (§7.1). No alarm.

### 6.2 Radiation spikes
**Driving inputs:** **radiation reaching the surface** (`mars:surface_radiation`: the spike, already weakened by thicker air, 100 % at 0.6 kPa → 20 % at 75 kPa; SME ✓ that this weakens with pressure), and **local shielding** (shielding blocks and depth, DESIGN.md §3). Goal: **A4** (thin air and no magnetic field let radiation through; shielding helps).

**Dosimeter clicks (suit path, unfiltered):**

| Quantity | Value |
|---|---|
| Dose rate r (0–1) | `mars:surface_radiation` (signal; already includes the pressure attenuation, ARCHITECTURE.md §2.5.2) × local shielding factor (shielding numbers: Lead Game Designer) |
| Click rate | **0.2 + 7.8 × r clicks per second**, random (Poisson) intervals, capped at 8/s |
| Background ticks (no spike) | Background radiation never reaches zero, even on Earth (about 2.4–3.0 mSv per year; SME round 3). So ticks fade to a **floor, never to silence**: outside, **0.1 per second** below 5 kPa, fading on the log axis to the floor at 30 kPa; the **floor is 0.02 per second** (about one tick a minute) outside above 30 kPa **and** inside sealed volumes. Both rates are **invented** game numbers (chosen to be heard rarely, so the sound stays sparing); flagged for the balance pass. SME ✓ on the direction |
| Click sound | Short dry tick, 3 random variants, main energy 2–4 kHz |
| Subtitle | Two events: "Dosimeter clicking" (r < 0.4), "Dosimeter clicking fast" (r ≥ 0.4) |
| Visual twin | HUD trefoil with slow pulse (1 per 2 s, style guide §4.6) and dose-rate number |

- **The lesson is audible:** step under shielding during a spike and the clicks slow at once. That's A4's cause and effect in sound.
- Spike start: BELL's log line with the **Warning** tone. Harmful dose: **Radiation alarm** (§5.1).
- **Aurora is silent** (honest silence, §10). It scales with the solar spike only, not pressure (SME round 2 §2.8), so a strong spike at equilibrium lights the sky while the clicks stay slow.

---

## 7. Feedback cues, BELL's text-message cues

### 7.1 The feedback cue set (6 meanings)
BELL is text-first (story bible §6.1). These cues tell the player "something new is in the log" and **how serious** it is, before they read. Shape and word in the log carry the same information (style guide §7.1).

| Cue | Used for | Sound | Length | Goals / purpose |
|---|---|---|---|---|
| **Log chime** | New colony log line (Info severity), BELL recap lines | One soft glassy note, ≈ 1.3 kHz, gentle tail | 0.4 s | Salience of new feedback (DESIGN.md Feedback table); B1, C3 |
| **Gate chime** | Gate reached, Monitor milestone (Armstrong line, Everest pressure), hold confirmed | Two rising notes (a fifth apart) with a warm resonant tail | 0.8 s | Clear goals and progress (flow); A1; motivational: the most rewarding cue in the game |
| **Warning tone** | Warning severity: storm or solar event coming, drift event pushing a value out of band, hold paused, ground creaking near a machine, low battery | Two level notes, mid pitch, soft attack | 0.5 s | C1, C2, B4 |
| **Failure tone** | Failure report: machine failed, breach in an empty volume, fauna damage | Two falling notes, lower, rounded (not harsh) | 0.6 s | C2 (process feedback) |
| **Message cue** | Recorded message from the crew or Mensah arrives | Relay "lock" blip, then a short slip-printer whirr (the message is a printed slip, style guide §7.2) | 0.8 s | Motivational: people are reaching you; D1, D2 carriers |
| **Forecast cue** | "Log your forecast" prompt, and the forecast result | Soft pencil-tap pair | 0.4 s | B3. **The result uses the same neutral cue whether the forecast was right or wrong**; the text says which. No "wrong" buzzer: a sound of failure would punish guessing, and forecasts must feel safe to try (handoff to the Lead Instructional Designer) |

Everything else in the interface is either silent or a small diegetic action sound (§7.5).

### 7.2 BELL and message cues, beat by beat
| Situation | Cue | Notes |
|---|---|---|
| BELL log line (Info) | Log chime | Rate-limited (§7.3) |
| BELL failure report | Failure tone | Hana's taped tip, when attached, adds a tape-rip sound (0.2 s) on opening, not on arrival |
| Repeated failure (compact, faded report) | Failure tone at −3 dB | Fading: less detail, softer cue (DESIGN.md Fading) |
| **Relay restored, backlog arrives (Dome stable, beat 6)** | Gate chime, then **one** message cue followed by 2 s of printer whirr | Seven days of messages are **one** event, never seven cues |
| Crew message, Mensah reply | Message cue | One per message; queued 3 s apart if several are due |
| Keeper's Report sent (phase debrief) | Outgoing transmission: a rising sweep (0.6 s) | Subtitle "Report sent to Earth" |
| Shift Briefing (advance organizer, DESIGN.md) | Log chime | At phase start, at a natural pause |
| "Since last shift" recap (session start) | None (the dome hum starting is enough) | Recap panel is visual |
| Forecast prompt / result | Forecast cue | Only at natural pauses (LEARNING.md §4.6) |
| Keeper's Record page filled | Stamp (0.3 s) | Motivational; one per page |

### 7.3 Rate limits and queue
| Rule | Value |
|---|---|
| One feedback cue at a time | Minimum gap **1.5 s** between any two feedback cues |
| Log chime | At most **one per 10 s**; entries arriving inside that window merge into one chime. Info-only bursts: at most one chime per 60 s |
| Same cue repeated | A failure tone for the same machine and cause plays at most once per 2 minutes |
| During an alarm | All feedback cues wait. When the alarm stops, the highest-priority waiting cue plays once (Failure > Warning > Gate > Message > Forecast > Log); the rest merge silently into the log badge |
| During voice (if added later) | All feedback cues wait until the line ends + 0.5 s (rule 3) |
| During danger, combat, or active building (TEAM.md 8.6 rule 1) | **Feedback cues wait** for the natural-pause signal, as in the TL's cue queue (ARCHITECTURE.md §2.12; I accept it: simpler and lower load). **One exception, proposed:** a Warning tone about a *new hazard on its way* (storm or solar event coming) plays at once, because it is about safety and the player may need to act |
| Classroom last ~5 minutes | Forecast cues wait for the next session (LEARNING.md §4.6, extended rule 5) |
| Group planets | Planet events (gates, failures, messages) play for every player on the planet. Personal events (own forecast, own Record page) play only for that player |

### 7.4 Priority ladder (mixing)
1. **Alarm**: ducks everything −10 dB; holds cues; pauses voice.
2. **Voice** (future): ducks ambience and machines −6 dB; holds cues.
3. **Failure / Warning tones.**
4. **Gate chime, message, forecast, log chime.**
5. **World**: machines, fauna, weather, ambience.

Feedback cues never duck the world: they sit on top, short and in the 500 Hz–4 kHz band.

### 7.5 Silent or small interface sounds
- Opening the Monitor: a soft switch click. Opening the journal: a page turn. Shift Board claim: a pin into cork; marked fixed: a pencil tick.
- Monitor tab highlight (new log entry): the flashing is visual only; its sound is the log chime above (no second sound).
- Teacher pauses the world: world sounds fade out over 1 s; a soft two-note "paused" cue; subtitle "World paused". Resume fades back in over 1 s.

---

## 8. World sounds

### 8.1 Dome interior (Phase 1 home)
**Driving input:** life support state (machine-state; the Dome stable gate is "3 sols with no failures", DESIGN.md §2). Goals: C1, C2.
- **Before Dome stable:** the hum wavers; lamps buzz and flicker; a filter whine from the O2 generator (Hana: "Told you the filters whine.").
- **At Dome stable:** the hum steadies, the buzz stops. The inside changes, not the sky (keyframes §4.2), and you can hear it.
- Level: a quiet bed (≈ −36 LUFS target, §11). Never louder than a feedback cue.

### 8.2 Machines
Every terraforming and life-support machine has a sound set: **start, running loop, worn, failed (wind-down), repaired**. Driving input: **machine state** (off, running, worn, failed), plus the inputs listed. Exterior machines pass the pressure filter, so **in Phase 1 an Outgasser outside is a dull thump through your boots; by Phase 3 it's a full roar.** That's the pressure lesson again, from the machine the player built (B1).

| Machine | Running sound | State cues | Extra driving input |
|---|---|---|---|
| **Regolith Outgasser** | Low heater drone (≈ 80–120 Hz fundamental with a 400–800 Hz overtone so laptops carry it) + a soft **"chuff" synced to each vapor puff** from the stack (keyframes §3.8) | Start: igniter ticks, drone rises over 2 s. Worn: chuffs irregular, a rattle every few seconds. Storm power cut: drone drops pitch and chuffs slow, matching the slower puffs. Failed: drone winds down, one metal clank | Puff rate and size (visual), so sound and plume always agree. Drone comes as **two pre-filtered files** (`run_thin` low-passed at 1.5 kHz, `run_full`), crossfaded by pressure like the wind bed, because this is the main machine that teaches "thin air, faint sound". Contact thump (unaffected by pressure) within 3 blocks, so it's never silent in Phase 1 |
| O2 generator | Electrolysis bubbling + small fan + **filter whine** | Whine rises in pitch as the filter clogs (perchlorate dust, DESIGN.md §3); replaced filter: whine gone | Filter wear (C2: you can hear maintenance coming) |
| Water recycler | Pump gurgle cycle (4 s) | Worn: gurgle stutters | — |
| Regolith Oven | Oven tick and low hum | — | — |
| Solar panel | Silent | — | Power drop is heard in the dome hum (§6.1) |
| Battery | Faint electrical hum within 2 blocks | Low charge: Warning tone in the log, not a battery sound | — |
| Airlock | **Pump-down hiss fading to silence**; pump-up hiss rising | — | **Pressure difference** between dome and outside: at low Index pressure the hiss is long and strong (≈ 3 s); at ≥ 30 kPa shorter and softer; near equilibrium a small sigh. You hear the pressure gap close (A1) |
| Dust-off station | Short air-jet bursts | — | — |
| Atmospheric processor | Compressor thump-cycle + gas rush | Worn: knock | Pressure filter |
| Super-greenhouse emitter | Steady vent hiss and heat tick; **no smoke or puff sound** (gas is invisible, keyframes §3.5) | — | — |
| Ice extractor | Slow drill thump (≈ 1 per 1.5 s) + pump | Scraped by thawlings: metal rasp overlay | — |
| Algae vat | Bubbling whose rate follows output | — | Vat output (same as bubble particles, A5) |
| Carbon scrubber | Fan + occasional crumble as carbonate drops | — | Scrubber output |
| Meltwater treatment, soil treatment | Filter pump; soil churn | — | — |
| **Insulation shroud** on any machine | The machine's own sound at **−6 dB and low-pass 2 kHz** | — | Story: "machines run cooler and quieter" (B4, B2) |
| Lure station | Warm water lapping, gentle steam | — | — |
| Survey scanner | Rising sonar-like sweep (0.8 s), soft ping per ice lens found | — | Goals C2, B5 |
| Weather mast | Windsock flutter (§3.2 table); faint tether creak when the balloon is up | — | Pressure |

**Concurrency:** at most **8 machine loops** audible at once (the nearest); others are culled. Loops of identical machines within 4 blocks merge into one louder loop (+3 dB) instead of stacking.

### 8.3 Fire (O2)
**Driving input:** **O2 fraction** bands and the minimum-pressure guard, as corrected by the SME (keyframes §3.4). Goals A5, B4.
| O2 band | Sound |
|---|---|
| < 14 %, **or total pressure < 20 kPa** (invented guard) | **Igniter clicks, no catch** (click-click, nothing): honest, because the flame never forms. Subtitle "Igniter fails to light" |
| 14–17 % | Weak sputter that keeps dying |
| 17–23 % | Normal small crackle (quieter at 17–19 %) |
| > 23 % | Louder crackle with a "whump" as fire spreads; sparks pop. Fire inside a sealed volume: **Fire alarm** |

### 8.4 Terrain and water
| Event | Sound | Driving input | Goals |
|---|---|---|---|
| Ice sublimating (rare: only below the triple point, about the first sol) | **None** (honest silence, §10) | — | A2 |
| Flash puddle boiling (Phase 1 near a heater) | Brief fizzing boil, 2–4 s, quiet (pressure gain) | Local temperature **at or above the boiling point** at current pressure (`mars:boiling_point`; SME round 2 §1.1) | A2 |
| Day puddle freezing at night | Thin ice "tick" at dusk | Local temperature | A2 |
| Ground collapse warning | Low creak through the ground (contact path) 3–5 s before, then a thump and dust slide | Ground-ice lens melt state (local) | B5, C2 |

### 8.5 Thawlings: animal, not monster
**Fiction** (option B, labeled in the journal). Direction from style guide §6.4. Driving inputs: **fauna state** (stirring, later life stage, settled) and **behavior** (sniff, bask, scrape, defend, graze, nest). Goals: **D1, B2, B5**.

| Behavior | Juvenile ("stirring") | Later life stage (adult) |
|---|---|---|
| Idle | Soft clicks, 2–3 per call, every 8–20 s (random) | Same family, about 5 semitones lower, slower |
| Sniff | Short snuffles | Deeper snuffles |
| **Bask near heat** | Low purr-hum (≈ 150–250 Hz body, 600 Hz overtone), rising slightly when frills fan open | Fuller hum |
| Scrape at a machine | The scrape is the machine's sound (metal rasp); the animal adds contented clicks | Same, heavier rasp |
| Startled, curl, roll | Plates clack softly | Same |
| **Defend (only if hit)** | Short breathy hiss (0.6 s), then a shove thump | Same, lower |
| Settled: graze, groom, nest | Quiet chirr (trilled clicks), digging scrapes | Same; soft group chirrs at dusk |
| Death (lethal defense) | A soft sigh and salt crumble. **No scream, no gore** | Same |

**Rules:**
- **No roars, growls, screeches, or screams.** Nothing predatory. They never vocalize *at* the player except the defensive hiss.
- **Must not sound like any existing mob or game creature** (checked against vanilla silverfish and endermite, which hiss and squeak, and avoided).
- Main energy in 500 Hz–3 kHz so they survive the pressure low-pass at Thaw (~22 kPa, cutoff ~6–7 kHz) and laptop speakers.
- **A raid is not an alarm.** The player learns of it from the scrape sounds near the machine (if close), BELL's "motion near Extractor 1" log line (Log chime), and the failure report if damage happens (Failure tone).
- **Shrouded machine:** thawlings that sniff it and leave make one disappointed snuffle (a readable "nothing warm here").
- Category: **neutral** (animals), never hostile (§9.4).

---

## 9. Fit with the Technical Lead's sound hooks (ARCHITECTURE.md §2.12)

The TL's design is adopted. This records what the audio content must supply, and where this direction asks for a little more.

### 9.1 Inputs (data keys from ARCHITECTURE.md §2.5.2)
| Input | Data key | Used by |
|---|---|---|
| Pressure, smoothed 30 s (same as the sky) | `mars:pressure` (`keyframes/ambient.json`, log10) | Bed crossfades and subtitles, exterior gain, windsock, airlock hiss, background ticks, Outgasser drone variants |
| Temperature; local temperature | `mars:temperature`; client `local_temperature` | Ice cracks, puddle freezing |
| Hydrosphere | `mars:hydrosphere` | Drips, lapping, rain |
| O2 fraction; pressure guard | `mars:o2_fraction`, `mars:pressure` | Fire bands |
| Biomass | `mars:biomass` | Rustle |
| Boiling point | `mars:boiling_point` | Flash-puddle boil |
| Breathable condition | `mars:breathable_air` | Helmet-off, Air alarm when helmet off |
| Storm strength | `mars:storm_strength` (signal) | Storm layers |
| Surface radiation × local shielding | `mars:surface_radiation` (signal) | Dosimeter |
| Fauna pressure, behavior | `mars:fauna_pressure`, entity state | Thawling sounds |
| In sealed volume; helmet; suit O2 and integrity; dose | Synced player state | Path choice (§2), suit path, alarms |
| Machine state, puff rate, output, shroud, filter wear | Block entity fields | Machine sounds |
| Log events with severity, messages, forecasts, reports, Record pages | Story and learning events (ARCHITECTURE.md §9) | Feedback cues |
| Natural-pause signal; classroom end-of-session signal | Learning layer | Cue queue |

### 9.2 What the content supplies
1. **Pre-filtered ambient beds**: `wind_thin` (low-pass 1.5 kHz), `wind_mid` (5 kHz), `wind_full` (none), and `wind_full_helmet` (6 kHz), crossfaded by `keyframes/ambient.json`. Stereo allowed (non-positional).
2. **Pre-filtered variants for the main teaching sound**: Outgasser drone `run_thin` and `run_full`. Every other exterior sound gets the **gain only** (TL hook, applied to vanilla and third-party sounds too).
3. **Mono** files for every positional sound (TL: vanilla only places mono sounds in 3D; unverified in this session).
4. A **subtitle** for every event (a unit test fails the build otherwise, ARCHITECTURE.md §2.12).
5. The cue channels map: Alarm = §5; Voice = none yet; Feedback = §7.1 cues; World = everything else.

### 9.3 Where this direction differs (for the TL to accept or push back)
| Item | TL hook | This direction | Why |
|---|---|---|---|
| Merge window for identical cues | 3 s | Log chime merges within **10 s**; Info-only bursts once per 60 s | Sparing; 3 s still allows a chime every few seconds in busy periods |
| Danger hold | Feedback waits during danger | Same, **plus one exception**: storm/solar warnings play at once | Safety information |
| Helmet bed | Exterior beds open at helmet-off | One extra file, `wind_full_helmet` | Makes the 75 kPa helmet-off moment audible |
| Audible range scaling | Not included | Stage 5 optional | Real effect, but gain already carries the cue |

### 9.4 Proposed vanilla sound categories
| Our sounds | Category |
|---|---|
| Exterior ambience, wind beds | ambient |
| Rain, storm | weather |
| Machines, airlock, terrain | blocks |
| Thawlings | **neutral** (never hostile) |
| Suit, footsteps, dosimeter | players |
| **Alarms and feedback cues** | **master** (TL round 3: 1.21.1 has no interface category, from knowledge, check in the skeleton). In master they can only be silenced by turning all game sound down, so they can't be muted by accident through another slider |
| Music (if adopted, §12) | music |
| Voice (if adopted later) | voice |

---

## 10. Stylization log (deliberate departures)

| Where | Real / expected | What we do | Why |
|---|---|---|---|
| Outdoor loudness at 0.6 kPa | About 20 dB weaker than Earth (model prediction, SME [S22]) | Gain 0.25 (≈ −12 dB) | Keeps the outside usable as a cue; SME: acceptable simplification |
| Gas mix | Sound depends on gas type as well as pressure | Pressure only | One input per channel (keyframes §1.1) |
| Contact sounds | Some sound reaches you through the suit and ground | Footsteps, tools, grain patter stay audible | Playability; SME ✓ plausible (round 3) |
| Helmet filter | Helmets muffle sound | −3 dB, plus a pre-filtered helmet bed (6 kHz) | Makes helmet-off audible |
| Sublimation | Silent and slow | **No sound** | Honest; a hiss would teach a false idea |
| Aurora | Silent | **No sound** | Honest |
| Dust storms | Thin air: weak force, no howling | Grain patter and muffled hiss, no gale | Avoids the film misconception (DESIGN.md audit) |
| Fire at low O2 | No flame at all | Igniter clicks, no flame sound | Honest: the sound is the tool, not a fire |
| Dosimeter | A real instrument | Click rate follows dose | Real instrument behavior, simplified |

---

## 11. Technical specs for sound files

| Spec | Value |
|---|---|
| Format | **OGG Vorbis** (Minecraft's format), quality ≈ 5 (about 160 kbps) |
| Sample rate | 44.1 kHz |
| Channels | **Mono for every positional sound** (Minecraft does not attenuate stereo files by distance; TL to confirm for 1.21.1). Stereo only for non-positional beds, interface cues, and music |
| Peak | ≤ −1 dBFS true peak |
| Loudness targets (starting points, tune in playtests) | Alarms −14 LUFS momentary max · Failure/Warning tones −18 · Gate chime, message, log chime, forecast −22 · Machine loops at 1 block −24 · Thawlings −24 · Dosimeter click −24 · Ambient beds −30 integrated · Dome hum and suit fan −36 integrated · Music −23 integrated |
| Loops | Seamless, 4–12 s, zero-crossing ends, no audible repeat of a distinct event within the loop |
| Variants | Short one-shots that repeat often (clicks, footsteps, chuffs, ice cracks) have **3–4 variants**; pitch randomization ±5 % in `sounds.json` |
| Naming | `snake_case`, event names `mars:<group>.<name>` (e.g. `mars:machine.outgasser.chuff`); files `<name>_<n>.ogg`; placeholders end in `_placeholder` |
| Layout (proposal; TL owns) | `pack/assets/mars/sounds/<group>/` with groups `ambient`, `weather`, `terrain`, `suit`, `machine`, `fauna`, `ui`, `alarm`, `ending`, `music`; subtitles in `lang/en_us.json` under `subtitles.mars.*` |

---

## 12. Music (proposal; decision needed)

Vanilla Minecraft's music is pastoral and Earth-like; it doesn't fit a dead Mars. Rules if we add any:
- **Vanilla music off in the Mars dimension** (the custom biomes define no vanilla music).
- **No music in Phase 1 by default.** Silence is the setting.
- **Never during alarms, never at the helmet-off moment.**
- Options (decision for the user, report): (a) one original **ending piece** only; (b) ending piece plus three sparse phase pieces (cut-list); (c) none.

---

## 13. The ending (beacon)

Follows story bible §6.5 and style guide §10. Driving input: ending sequence steps (story system). Goals: D1, D2 (reflection), motivational: earned relief.

| Step | Sound |
|---|---|
| **Build** | Normal block and machine sounds |
| **Light it** | 8 lamp clicks, one per lamp, each with a short rising tone; the 8 tones climb a scale over 2 s (synced to the visual) |
| **Signal rings** | A soft low pulse every 2 s, synced to each ring (no faster than the visuals; well under 3 per second) |
| **"Signal sent"** | Gate chime with BELL's log line. Then rings stop |
| **The wait** | **Silence except the world**: full open ambience at equilibrium (wind, water, leaves, settled thawlings at dusk). No music. The player can walk around |
| **Replies** | Message cue once per speaker (4), 4 s apart, at −3 dB |
| **Epilogue cards** | Page turn per card. If music is adopted (§12), the ending piece starts here, quietly. **Same sound for every outcome card; no sad or triumphant variant** (no ranking, story bible rule 7) |
| **Final reflection** | No cues while the player writes |
| **Keeper's Record opens** | One stamp sound (all seals are already stamped) |
| **After** | Normal play; the helmet-off check stays live, so the Air alarm still works |

---

## 14. Traceability summary (sound → goal)

| Goal | Sounds that carry it (always with a visual twin) |
|---|---|
| A1 | Pressure-driven exterior sound, airlock hiss, helmet-off, gate chime at milestones |
| A2 | Boiling flash puddle, puddle freezing tick, drips and lapping, honest silence of sublimation |
| A4 | Dosimeter clicks that slow under shielding; background ticks that fade as pressure rises |
| A5 | Fire bands by O2; helmet-off; algae vat bubbling |
| B1 | Every sound that follows an Index input (above), synced to visuals |
| B2 | Storm sounds fading by phase cap; thawling calls rising with machine count |
| B3 | Forecast cue (neutral for right and wrong) |
| B4 | Shrouded machines quieter; fire overshoot |
| B5, D1 | Thawling sounds (animal, not monster), scrape at machines, ground creak |
| C1 | Storm power drop in the dome hum; warning tones |
| C2 | Failure tone; filter whine rising with wear; repaired sound |
| C3 | Log chime and Monitor highlight (prompt to read the data) |
| D2 | (No sound; carried by text) |

---

## 15. Open items

| Item | Who | Type |
|---|---|---|
| ~~§3.1 claim, background ticks, contact path~~ Done (SME rounds 2–3). Optional: pixel-sample rover images together now that pds-imaging.jpl.nasa.gov is reachable (keyframes §2.1) | SME + Lead Artist | Well-structured |
| ~~Hooks, merge window, safety-warning exception, helmet bed, categories~~ Accepted (TL round 3). Still open: 1.21.1 category list to confirm in the skeleton; Stage 5 range scaling | Technical Lead | Well-structured |
| Thresholds: suit O2 alarm, harmful dose, shielding factors | Lead Game Designer | Well-structured |
| Neutral forecast-result cue; subtitles on by default in classroom | Lead Instructional Designer | Well-structured |
| Music scope; real Mars recording in the journal | User (via Producer) | Ill-structured (scope, appeal) / well-structured (license) |
| Who produces final sounds (recording, synthesis), effort | Producer | Well-structured |
