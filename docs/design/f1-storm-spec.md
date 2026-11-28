# F1 and the First Dust Storm: Build Spec (Stage 3 slice 5)

**Owner:** Lead Game Designer · **Status:** Draft 2 (2026-11-28): ID wording and owner flow answers applied ("Why?" on the result card, tally off the player's card, CR-F11); SME confirmation of the marked wording points and external review (I3) still pending. Draft 1 went to the Technical Lead, the Instructional Designer (ID) pass and the SME check · **Goals:** B3, C1, B1 (C2 on failure) · **Pillars:** the planet is the quest log; every change is visible; pressure, not grind.

**Reads with:** DESIGN.md (Solar panel dust, forecasts, lock-point rule), balance sheet §2, §2a, §2b, §2d.4, [messages/phase-1.md](messages/phase-1.md) §5, [messages/story-beats.md](messages/story-beats.md) beat 4, [journal-dust.md](../learning/journal-dust.md), [first-dust-storm prototype](prototypes/first-dust-storm.md), keyframes §3.7a, content rules CR-F1 to CR-F10, CR-T7, CR-T8.

**Tags:** *invented* = game balance number; *calc* = arithmetic on tagged numbers; *sourced* / *searched* / *unverified* as in the SME's notes. No number here is new science. All text is **draft: ID pass and SME check pending**; nothing ships as fact before them.

**Ground rule from the Landing spec (do not break):** the Shift Briefing says only "Mars weather can change fast, so keep some power in reserve." Nothing before F1 locks may say that storms cut solar power, in any message, journal page, Monitor line or tooltip (CR-F9, including copies readable in the journal).

---

## 1. The F1 forecast

### 1.1 Card

| Field | Value |
|---|---|
| `id` | `forecast.f1.storm_power` |
| Issued | When the first storm is scheduled (warning, T−1 sol), to every player on the planet who has finished or skipped the Landing. A player still in the Landing gets no F1 and no event (absence is not "expired") |
| Question (MS and HS) | **A dust storm reaches the dome in about {eta}. What will happen to your solar power?** `{eta}` = "1 sol", then "half a sol" when the card is opened later (never a stale number) |
| Check line (scaffold, both bands) | Check: solar power on the Monitor's Dome tab. |
| Order of choices | **Shuffled per player and per forecast** (seeded by pseudonym), so a neighbour's screen shows a different order. The key is not always first |

| Choice (`text`) | Role | Misconception it stands for (stored in `distractor_misconception`; never shown) |
|---|---|---|
| It drops | **Keyed** | none |
| It stays the same | Distractor | **M1** "Weather happens on the ground; the Sun above is just as bright, so the panels are unaffected" (source and path confused) |
| It rises | Distractor | **M2** "A storm brings energy; strong wind blows the panels clean" (film wind; real wind-cleaning is rare dust-devil luck, not a storm effect) |

Three choices, not four: the card stays short for a 12-year-old, and every extra choice would need its own misconception (CR-F2 asks for one keyed answer). A fourth, "It stops completely", was **rejected**: real storms cut power by 80 to 97 % (searched) and the game cut is never 100 %, so "stops" is neither clearly wrong nor clearly right and would punish a student who has the direction right.

**HS "Why?" (on the result card, after the storm; logged, not scored, not part of B3).** Owner decision 2026-11-28: the "Why?" choices are **not** shown before the lock. Shown before, they hand the mechanism ("dust blocks sunlight") to a player who is still waiting for the storm and remove the surprise of the result (productive failure). They appear only on the result card, as the step "Which reason fits what you saw?", and the pattern is in DESIGN.md ("Forecast 'Why?' pattern").

| Choice | Role | Misconception |
|---|---|---|
| Dust in the air blocks sunlight | Key | none |
| Strong wind damages the panels | Distractor (**proposed**, was "Wind turns the panels away from the Sun") | M3 films: strong, damaging Mars wind (thin air cannot push that hard). The old wording did not match M3: panels in the game do not turn |
| The Sun itself gets dimmer during storms | Distractor (**proposed**, was "The Sun gives off less light during storms") | M4 source and path confused (the storm is between the Sun and the panel, not in the Sun). The old "less light" was ambiguous: less light at the ground is true |

Both new distractor wordings are ID proposals, **pending SME confirmation** (ID report 1.7, SME handoff point 2). The choice order is shuffled like the main choices. The pick is logged as `prediction.answered` with `step: why` (typed, whitelisted data; Codex's event kinds), unscored. Tapping a Why choice shows its one-line `m3` or `m4` answer at once and the key line; nothing is "wrong".

Distractors are meant to be wrong for a real reason a 12-year-old can say, not tricks. Main choices are from the SME's round-2 set already in `phase-1.md`; **all F1 distractors are "accepted, pending external review (input I3)"** (owner decision 2026-11-28). Expected weak spot: "It rises" (M2) will rarely be picked, so F1 behaves like a 2-choice item (50 percent guess rate); the game cannot produce a rise (CR-F2), so this cannot change. Log pick rates and F1 accuracy in the first playtest.

### 1.2 Lock point

| Item | Rule |
|---|---|
| **Locks at** | Storm onset: the first simulation step where `mars:storm_strength` > 0. Same instant for the whole planet. This is also `outcome_visible` |
| Answer after the lock | Logged `late` inside the 0.05-sol late window, not scored; after that `expired`, never "wrong". Nothing in the UI ever says "wrong" |
| Unanswered at lock | `expired`; the result card still arrives |
| Never locks later than | The first step where solar output falls because of the storm (they are the same step by construction; AC-3) |

**What the player has seen by the lock (all allowed, none gives the answer):**

| Cue | Content | Why it is safe |
|---|---|---|
| Colony log line + HUD storm icon (cloud shape, countdown, the word "Storm") | `log.storm.warn`: "Dust storm forming. Arrival in about 1 sol. Expected length: half a sol to a full sol." | No mention of power or light (story beat 4) |
| Monitor line "Dust storm in about 1 sol. It lasts half a sol to a full sol." | `monitor.storm.expected`, `reveals: none` | Same |
| One reminder at T−0.5 sol: "Dust storm in about half a sol." | `log.storm.remind` | Same |
| Pre-storm haze: fog only, fog end −10 % over 0.25 sol from the warning; `storm_strength` stays 0; **sun disc and solar output unchanged** | Sky cue | DESIGN decision "pre-storm haze allowed"; the guard is fog only |
| Solar output number as it was; any "Panel dust −n %" line | Existing power row | The panel line has its own cause and is not held (messages/README.md) |
| Briefing, journal and failure hints |  "Mars weather can change fast, so keep some power in reserve" (no cause named; weak hint, accepted by the owner, watch in playtest); `journal.dust.panels` (panel dust, **no storm sentence**; ID check done 2026-11-28: passes; its diagram is the one-path variant, dust on the panel only, CR-F11) | CR-F9 journal-copy rule |

**What is held until the lock** (each declares `reveals: forecast.f1` and is checked by CR-F9): `log.storm.onset`, the Monitor power-row cause line "Storm: less sunlight −n %", any fall in solar output, the sun dimming, the storm sky, the F1 result and its "Why?" step, the journal pages `journal.storm.first`, `journal.storm.wind`, `journal.f1.real_vs_game`, `journal.dust.storm_timing`, the item `explain.f1.after_storm`, and the two-path dust diagram (CR-F11). The HUD icon tooltip (`hud.storm.tooltip`: "Dust storm. Arrives in about {eta}.") and the Monitor storm line must not contain "solar", "power", "sunlight", "light" or "dim" (banned-word scan for `reveals`).

**Monitor open before the lock:** F1 does **not** need the "look first" card (unlike F3, F5, F8): nothing on the Monitor reveals the answer before onset. The "Check:" line is the pointer.

### 1.3 Group rule (answers hidden)

- Each player answers on their own screen. The server stores the answer under the player's pseudonym; **no packet sends one player's choice to another client** before resolution.
- The card shows no tally, no "2 of 4 have answered", no avatars. Choice order is shuffled per player.
- **After resolution only**, an anonymous tally ("{n} of {m} Keepers said 'It drops', 'It stays the same', 'It rises'": all three counts in one line, no names, never "you were in the minority") shows in **two places only**: the **teacher view** and the group's **shared group screen** (the Monitor-side summary the whole group sees). It is **not** on the individual result card (owner decision 2026-11-28; ID decision 2). Reason: in a group of 3 to 5, "1 of 4 said it rises" can feel like a score or point at a neighbour even with pseudonyms. The tally feeds the "discuss, then re-forecast" activity LEARNING.md offers. Solo play: no tally.
- Chat cannot be controlled; the text of the card does not invite discussion. The teacher guide suggests "no talking until the result".
- Resolution is per player (one `prediction.resolved` each, ARCHITECTURE 2.6.1); the storm and its lock are planet-wide, so no one in the group can answer after seeing the drop.

### 1.4 What resolves it (`resolve_source`, CR-F10)

| Field | Value |
|---|---|
| `resolve_source` | `storm.solar_cut`: the storm's own attributed contribution (power units per sol lost to storm sunlight cut), **not net power** |
| `outcome_visible` | `storm_strength` > 0 |
| Sampled at | `outcome_visible` + late window (0.05 sol, the ramp-in is 0.05 sol, so the storm is at full cut) |
| `outcome_check` | `storm.solar_cut` ≤ −0.01 units/sol → "It drops"; ≥ +0.01 → "It rises"; between → "It stays the same". The game cannot produce "rises" (CR-F2: only the key passes) |
| Void | If solar supply is 0 at onset (the player has no panel standing), the forecast resolves `void` (not counted, not expired) |
| Why not net power | A player can brush stage-4 panels or draw the battery during the storm; net power could rise. The attributed line cannot (CR-F10; messages/README.md) |

`rests_on`: storm cut > 0 (balance §2, §2a). Same `resolve_source` serves FI-1 and FI-4 later.

### 1.5 Feedback after resolution (shared format, DOCUMENTATION §5.1)

Delivered at the next natural pause after onset + 0.25 sol, **first in the queue**. Positive terms only (CR-T5). One result per outcome; the second depth is "Read more".

**Depth 1: the result card (MS and HS, one screen, 40 words or fewer; ID wording 2026-11-28):**

| Case | `text` |
|---|---|
| Answered "It drops" (`result.right`) | **You called it.** Solar power dropped. Dust in the air blocks sunlight before it reaches your panels. Look for the Storm line on the Monitor. What could you store before the next storm? (33 words) |
| Any other answer, expired, or late (`result.other`) | **Here is what happened.** Solar power dropped. Dust in the air blocks sunlight before it reaches your panels. Look for the Storm line on the Monitor. What could you store before the next storm? (34 words) |

(Only the Monitor sentence changed: the quoted line text no longer depends on the final form of the Monitor key.) The result card also carries, for HS, the **"Why?" step** (§1.1) under the result text, and **no tally** (§1.3).

**Depth 2: "Read more"** (HS open by default, MS closed; the real-versus-game line open for both). Three parts in this order, so the first thing a player reads answers what they just thought:

| Part | Case | `text` |
|---|---|---|
| (a) Choice line | Picked "It stays the same" (`m1`) | The Sun was just as bright above the dust. But the light has to pass through dusty air to reach your panels, and the dust blocks some of it. |
| | Picked "It rises" (`m2`) | The storm put more dust in the air, so the light got dimmer. Wind did not clean the panels this time. Dust settles on them as the sky clears. (SME wording check on "this time" open) |
| | HS Why: strong wind (`m3`) | Wind did not turn or push your panels. Right now Mars's air is so thin that even fast wind pushes very little. |
| | HS Why: the Sun gets dimmer (`m4`) | The Sun shines as usual. The dust between the Sun and you is what changed. |
| (b) Explanation, MS | all | **Why solar power drops.** In a dust storm the air fills with fine dust. The dust blocks the Sun's light, so less reaches your panels. Some light still gets through, scattered across the sky, like on a cloudy day. When the storm ends, dust settles out of the air over weeks and can coat the panels. (SME verbatim, 56 words) |
| (b) Explanation, HS (after the MS paragraph) | all | **Optical depth.** Scientists measure how much dust blocks the light with a number called tau. At tau 3, only about 5 percent of the Sun's direct light reaches the ground. Opportunity's solar power fell about 80 percent at tau 3 and about 97 percent near tau 11 in 2018. It did not fall to zero, because scattered light still reaches the panels. Dust in the air and dust on the panels are two separate losses. (SME verbatim, 75 words) |
| (c) Real versus game (same string as `journal.f1.real_vs_game`) | MS | In 2018, a storm that covered all of Mars cut the rover Opportunity's power by about 97 percent. Your storms cut {max_cut} percent at most. Real storms can be bigger, and they last days to months. Yours last a sol or less, so you can keep playing. |
| | HS adds | NASA believes the rover's batteries ran out, and that ended its mission. |

The old general line ("On Mars today, the air is so thin that wind pushes very little...") left this card: it had no time qualifier that stays true after the Phase 2 gate. The wind idea now lives in `journal.storm.wind` (§1.6) and `m3`. `{max_cut}` is read from `dust_storm.json` for the player's phase (60 / 48 / 30 / 15). Layout (Game Designer decision): MS sees the choice line and part (c) open and part (b) behind "Read more"; HS sees (a), (b), (c) open. About 120 words for MS, about 190 for HS.

`hint_level`: 1 (the closing question is a question-form hint about storage). `goal_ids`: B3, C1. Fiction note: none; the storm length and cut are game numbers (storm cut **invented**; real storms called out in `journal.f1.real_vs_game`).

**F1 `science_claims` (updated):** dust storms reduce sunlight at the surface and cut solar power (**verified, opened**: NASA pages; 2018 about 97 percent from 652 to 22 Wh is **calc**, baseline secondary so "about"); the 2018 storm left Opportunity without enough power and **NASA believes** drained batteries ended the mission (never say the storm destroyed it, never "went silent"); at tau 3 about 5 percent of direct light reaches the ground, and Opportunity fell about 80 percent (tau 3) and about 97 percent (tau near 11) (**verified**, SME 2026-11-28); real storms last days to months (**verified**); "thin air pushes very little" is true **today only** (SME, `journal.storm.wind`); the game cut and `{max_cut}` are **invented**.

### 1.6 Journal entry (appears only after resolve, CR-F9)

| Field | Value |
|---|---|
| `id` | `journal.storm.first` |
| `trigger` | That player's `prediction.resolved` for F1 (any result, including `expired`, `late`). A player who was never issued F1 gets it at onset + late window. **The page does not exist in the journal before then**, so a journal copy cannot leak the answer |
| `text.ms` | **The first dust storm.** Dust rose into the air and blocked some sunlight, so your panels made less power. When the sky cleared, the sunlight came back. But dust had settled on your panels, so they still made less than before. These are two causes. Dust in the air clears on its own. Dust on the panels stays until you clean it. (62 words; the old "even less" misread as less than during the storm, which is false) |
| `text.hs` (read more) | The Monitor keeps the two causes on separate lines: "Storm" and "Panel dust". Checking which one moved tells you what to do about it. The two losses multiply, so a dusty panel loses power in a storm too. |
| Also unlocked with it | `journal.f1.real_vs_game` (numbered text now ships, SME opened the sources), `journal.storm.wind` (variant A below 5 kPa, B at or above), and, at storm end, `journal.dust.storm_timing` ("weeks", not "days"). Journal retrieval: all four are `reveals: forecast.f1` |
| `goal_ids` | B1, C1, B3 |
| Diagram | **Two-path** variant of diagram 9 (dust in the air, dust on the panel), journal-dust §7, asset U-25 file 2. This is the **only** page that shows the air path; the dust page `journal.dust.panels` uses the one-path file (CR-F11) |
| `science_claims` | Dust in the air cuts surface sunlight (searched, not opened); the two-cause split is game structure; storm dust added at the end is a stated simplification (`journal.dust.storm_timing`) |

### 1.7 "What caused it?" prompt (B1; the bank is the ID's)

Journal page "What caused it?"; header and sub-line from messages/README.md ("Pick the most likely cause. No penalty for any pick."; "wrong" appears nowhere in the UI, AC-14). First attempt only counts.

| Field | Value |
|---|---|
| `id` | `explain.f1.after_storm` |
| `trigger` | The storm has ended, F1 is resolved for that player, "Panel dust" is at least 5 percentage points larger than before the storm (storm-end dust deposit), **and the Monitor's largest remaining cause line is Panel dust** (else a storm ending at night leaves Night as the true answer, which is not on the list). Once per planet, per player, first storm only |
| `prompt` | **The storm is over, but your solar power is still lower than before it. What is the main cause?** |
| Choices | **Dust settled on the panels** (key; Monitor line "Panel dust: {n} percent less power") · The battery is low · The panels got too cold · The Sun is giving off less light |
| Key rule | The Monitor's largest remaining cause line (so it is true for any player) |
| Result | Shows the key and the line that shows it, and what to do: "Clean the panels with a brush. The Monitor's Panel dust line shows how much you gain." Distractor lines: low battery: "A low battery means less stored power. Your panels still make what the Sun gives them." ("The storm is still going" was replaced: it is a free elimination once the log says the sky is clearing; a low battery stands for mixing up power made with power stored); cold: "Panels work a little better when cold."; Sun: "The Sun shines as usual; dust on the panels is what changed." |

Why this item and not "what caused the drop during the storm": the F1 result already names the in-storm cause, so asking it right after would test recall. The after-storm question tests whether the two causes stay separate (journal-dust §7 purpose). The generic `explain.power_drop` (journal-dust §7) stays for other events.

---

## 2. The first dust storm event

### 2.1 When

| Item | Standard | Classroom | Tag / reason |
|---|---|---|---|
| First storm onset | **Sol 14** (warning at sol 13) | **Sol 4.5** (warning at sol 3.5) | Standard: balance-sheet §2 curve (storm after the Dome stable build-up, about 4.8 sols after Dome stable at sol 9.2). Classroom is **chosen, not ÷4** (÷4 gives 3.5, one sol after Dome stable at sol 2.5 and on top of the gate sequence). *Invented* |
| Warning lead | **1.0 sol** (0.8 to 1.2 assert) | **1.0 sol** | Learning rule (CR-F3: at least about 1 sol in both presets); not scaled |
| Earliest guards | At least one player has finished or skipped the Landing; at warning time solar supply > 0 (if not, retry each sol, up to 3 tries, then `void`); not while paused (sim time) | same | No forecast before the Landing (landing spec) |
| Schedule | **Fixed**, same for every world and every player. No rubber-banding by how well the player is doing | same | DESIGN "no hidden difficulty changes" |
| Later storms | Gap after the previous storm ends: 8 to 14 sols, seeded from the world | 2 to 3.5 sols (÷4) | *Invented*; Phase 1 has one storm in the slice; frequency falls with pressure and biomass in later phases (DESIGN) |

### 2.2 Warning (T−1 sol): what the player sees

| Surface | Content |
|---|---|
| Colony log (top line, BELL) | `log.storm.warn`: "Dust storm forming. Arrival in about 1 sol. Expected length: half a sol to a full sol." (about 16 words; CR-T7(c) cap 20) |
| HUD | Storm icon (cloud with dots; shape, not colour) with the countdown in sols and the word "Storm" |
| Monitor | One line under the power row area: `monitor.storm.expected` "Dust storm in about {eta}. It lasts half a sol to a full sol." |
| Sky | Pre-storm haze, fog only (§2.4) |
| Reminder at T−0.5 | `log.storm.remind` "Dust storm in about half a sol." Log only, no card |
| F1 card | Side panel at the next natural pause (§1) |

### 2.3 Duration

| Item | Value | Tag |
|---|---|---|
| First storm length | **0.75 sol** (fixed, so the first experience is the same for everyone and testable) | *Invented*; inside the warning's "half a sol to a full sol" |
| Later storms | 0.5 to 1.0 sol, seeded | *Invented* |
| Classroom | Always **0.5 sol** (10 minutes), the low end of the same range; the warning text stays true | Chosen: a class period is short. *Invented* |
| Ramp in / out | 0.05 sol (60 s) in; 0.075 sol (90 s) out | Art storyboard, ARCHITECTURE 2.5 |
| Real storms | Local about 3 days, regional weeks, global about 6 weeks to months (**verified**, NASA; storm-dust-review §3.3); the game compresses them about 3 to 100 times (timescale note) | |

`storm_strength = envelope(t) × intensity × cap` with intensity 1.0 for every slice 5 storm.

### 2.4 Visibility: what renders (existing keyframe system where possible)

| Layer | Effect | How |
|---|---|---|
| Pre-storm haze (T−1 to T) | Fog end −10 % over 0.25 sol, held; **fog only**, sun and solar output untouched | New signal `mars:storm_haze` (0 to 1) with an `overlay` block in the keyframe file (below) |
| Storm | Sky mixes toward `#9A6A48`, fog closes to 18 % of render distance, sun disc fades to 30 %, dust particles drift **slowly sideways** | `overlay` block keyed on `mars:storm_strength` (ARCHITECTURE 2.5.5 already specs the shape; slice 6 accepts only `mars:pressure`, so the loader needs the overlay input) |
| Phase cap | The look follows `storm_strength`, so a cap of 0.5 gives half the mix; look and gameplay both use the cap (decision 2026-11-27) | Same signal |
| Not allowed | No flashes, no camera shake, no howling wind, no damaging-wind particles (thin air) | Art rules §3.7a |
| Audio | AU-W01 to AU-W04 stubs per the storyboard; not a blocker | Artist |

If the overlay loader is cut: ship **fog only** (fog end to 18 %) driven by the existing fog event, with the sky unchanged. That keeps the visibility effect (the learning cue) and drops the colour. Say so in the report.

### 2.5 Solar penalty (all *invented*, balance §2a)

`output = stage factor × (1 − 0.6 × storm_strength)`; the attributed `storm.solar_cut` = clean-stage output lost to the storm = `supply × 0.6 × storm_strength`. Panel supply 5 units/sol clean; stage factors 100 / 92 / 82 / 70 / 60 %.

| Phase | Storm cap | Max sunlight cut | Storm-end dust | One stage-1 panel in storm (units/sol) | Left of clean output | Dust interval (standard / classroom) |
|---|---|---|---|---|---|---|
| 1 | 1.0 | 60 % | +2 stages | 5 × 0.92 × 0.40 = **1.84** | 36.8 % | 3 / 0.75 sols |
| 2 | 0.8 | 48 % | +2 | 5 × 0.92 × 0.52 = **2.39** | 47.8 % | 3.75 / 0.94 |
| 3 | 0.5 | 30 % | +1 | 5 × 0.92 × 0.70 = **3.22** | 64.4 % | 6 / 1.5 |
| Equilibrium | 0.25 | 15 % | +1 | 5 × 0.92 × 0.85 = **3.91** | 78.2 % | 12 / 3 |

(All **calc** from the balance sheet; stage-1 is the starting panel, `StarterDome`.) **Cap per phase** comes from `data/mars/hazards/dust_storm.json` keyed on latched gates (ARCHITECTURE 2.5). The cap scales cut, dust and look together.

**Power check (calc, not run).** Group of 5, start kit (1 panel at stage 1, battery charged 5, tier 1 = 4 units/sol before extra makers per balance §2d): in a 0.75-sol Phase 1 storm supply is 1.84, so the shortfall is 2.16 per sol and the battery spends about 1.6 of its 5. With the §2d.4 case (2 panels, tier 1 = 6, battery 10): shortfall 2.3 per sol, **battery lasts 4.3 sols**. So a player who does nothing keeps life support through the first storm but **Ovens and Outgassers stop** (tiers 2 and 3 get nothing): the lesson is lost progress, not death. A `power_shortfall` failure report fires only if tier 1 goes short (a bigger group with the start kit, or a drained battery), with Hana's tip the first time. Technical Lead: the acceptance scenario computes this from the data files, not from this paragraph. The hint text for this failure uses the plain variant ("Nights cut solar. ...") until that player's F1 resolves, then the held variant that names storms (`failure-reports.md`, CR-F11).

**Reconciliation (closed 2026-11-28):** balance sheet §2's note assumed a 2-sol storm; every other source says 0.5 to 1 sol. The owner accepted this spec's 0.75 sol and the balance sheet now says so. Effect on the curve (calc): 3 Outgassers at 40 % power for 0.75 sol lose about 0.14 kPa, under the curve's tolerance, so no curve number changed.

### 2.6 Storm-end dust (stage jump)

| Item | Rule |
|---|---|
| When | Two steps, so the sky and panels clear together: the first stage at the start of the ramp-out, the second (if any) at its end (0.075 sol later). Never before the storm ends (lock point) |
| How many | +2 stages while cap ≥ 0.8 (Phases 1 and 2), +1 below (balance §2a); capped at stage 4; clears `partly_cleaned` |
| Look | Panels step through the dust textures during the ramp-out (art step 7); "Panel dust −n %" rises on the Monitor |
| Log | `log.storm.end` "The sky is clearing. Dust is settling on your panels." (10 words), after F1 resolves; `log.storm.onset` "Dust storm is here. Watch the Power row on the Monitor." (11 words) at the lock (`reveals: forecast.f1`) |
| Honesty note | `journal.dust.storm_timing` after F1 resolves: real dust keeps falling out of the air for weeks after the sky starts to clear, and some of it lands on panels; the game adds it when the storm ends |
| Effect on a stage-1 panel | +2 stages: 92 % to 70 % output (−24 % relative); a stage-3 panel goes to the floor 60 % |

### 2.7 Brush and wiper

| Tool | Rule | Cost | Tag |
|---|---|---|---|
| **Panel brush** (item, tier 0) | Use on a panel: stage 0, clears partly cleaned; about 2 s per panel; 64 uses; needs a trip outside (suit O2, tier 1 suit is about 17 minutes of play) | Recipe: Fe 2, W 1 (standard and classroom; cheap, a first-hour item). Technical Lead adds to `recipe_costs.json` | *Invented*; fiction (no Mars mission flew a brush; `journal.dust.brush`) |
| **Panel wiper** (block, from Phase 2 begins) | Keeps the panels in its row at stage ≤ 1 while powered | 1 power unit/sol per panel; **bearing every 10 sols is a slice 6 item** (maintenance parts); slice 5 ships the wiper powered only | *Invented*; fiction (`journal.dust.wiper`) |
| Action log | `panel.cleaned`: actor, stage before, whether a storm was forecast (C1 evidence) | | |

Time cost check (calc): cleaning about every 6 sols with 2 to 4 panels is at most about 8 s of brushing per trip plus the walk, well under one minute per sol of play. Classroom (interval 0.75 sol): about every 1.5 sols, one short trip each half-sol-plus; at most about one trip per class period of play. If playtests show more than one minute per sol, lengthen the interval, not the brush time.

**Cleaning before a storm** raises the starting stage but the storm multiplies on top (a stage-0 panel in a Phase 1 storm makes 2.0 units, stage-1 makes 1.84); **cleaning after** removes the storm-end stages. Both are allowed and logged; there is no "best" order, which is what C1 planning is about.

### 2.8 Classroom scaling: scaled and not scaled

| Scaled by `pace.rateMultiplier` (×4) | Chosen differently for classroom | **Never scaled** (physical or learning rule) |
|---|---|---|
| Dust stage interval (3 to 0.75 sols) | First storm onset (4.5, not 3.5) | Warning lead (1 sol) |
| Gap between storms (8 to 14 to 2 to 3.5 sols) | Storm length (0.5 sol) | Sunlight cut % and caps (60 / 48 / 30 / 15) |
| | | Stage output factors and the 60 % floor |
| | | Ramp times, panel supply, battery capacity, power draw |
| | | The lock point rule and the forecast lead |
| | | Brush time (2 s) |

---

## 3. Panel dust accumulation, by pace

Rate: one stage per `interval = 3 ÷ storm_cap ÷ pace.rateMultiplier` sols (data already in `pack/data/mars/hazards/panel_dust.json`). Local dust (within 8 blocks of the player's digging, heavy machinery or exhaust) halves the interval (×2 rate, *invented*).

| Uncleaned panel from stage 0 | Stage 1 | Stage 2 | Stage 3 | Stage 4 (floor) |
|---|---|---|---|---|
| Standard, Phase 1 (3 sols) | sol 3 | sol 6 | sol 9 | sol 12 |
| Standard, near local dust | sol 1.5 | 3 | 4.5 | 6 |
| Classroom, Phase 1 (0.75 sol) | 0.75 | 1.5 | 2.25 | 3 (one hour of play) |
| Standard, Phase 2 / 3 / Equilibrium | 3.75 / 6 / 12 sols per stage | | | |

The starting panel is already at stage 1 (`StarterDome`, balance §2d), so it reaches stage 4 at sol 9 (standard) or 2.25 (classroom) if never cleaned.

**What the player does:** reads the Monitor line, looks at a panel (stage bar), crafts a brush, walks out, cleans (Phase 1 chore with a reason: about every 6 sols keeps average output near 91 %, which the Phase 1 curve assumes); from Phase 2 builds a wiper and trades their time for power and a part. A dust devil (1 % per sol per chunk group, Phases 1 and 2) is a lucky surprise (`journal.dust.wind`).

**Reveal of the dust line (decision, within my lane).** The starting panel is at stage 1 from the first tick, so "the first time any panel reaches stage 1" would fire during the Landing and add a new Monitor line to Landing step 3. **Rule: the line, its BELL line ("New on the Monitor: panel dust") and `journal.dust.panels` are held until the Landing is done or skipped, then appear at the next natural pause.** The journal page needs the SME/ID check that it does not say storms cut power (§5).

### 3.1 Monitor lines and the height budget

| Key | Text | When | `reveals` |
|---|---|---|---|
| `monitor.power.cause.dust` | Panel dust: {n} percent less power (average over panels; ID form, CR-T7(b): words, not "−" and "%") | After the Landing, once any panel is at stage ≥ 1 | none |
| `monitor.storm.expected` | Dust storm in about {eta}. It lasts half a sol to a full sol. | Warning until onset | none |
| `monitor.storm.active` | Dust storm now. Clearing in about {eta}. | Onset until clear | `forecast.f1` (not shown before lock) |
| `monitor.power.cause.storm` | Storm: {n} percent less sunlight | Onset until clear | `forecast.f1` |
| `panel.lookat` (action bar, not the Monitor) | Dust stage {n} of 4. It makes {pct} percent of full power. | Looking at a panel | none |

Each line has a shape in front (down-triangle for causes, cloud for the storm line), never colour only (ui-specs rules). The ID changed the forms to words ("percent less power"): no existing Monitor line uses "−" or "%", and "less sunlight −60 %" double-negated. Fallback if 232 px is too narrow: "Storm: 60% less sunlight" (never the minus sign). The two lines multiply, they do not add (§2.5); the HS journal line says so.

**Height.** Source: `MonitorStyle` (`LINE_H` 10, target 130 GUI px, tolerance +20), `MonitorLayout.fit`, and ARCHITECTURE 2.20 (about 149 GUI px normal, 156 busiest, "fits a 270 px screen"). Cost: the dust line is **+10 px**; the storm line is **+10 px** and only **one storm line shows at a time** (expected, then active/cause, then none). Worst case with both: **about +20 px** (estimate from `LINE_H`; **not measured**). Order of giving way, using the existing rules: (1) the "Outside air" line (9 px, until Phase 2), (2) the pressure plot shrinks to its 16 px minimum, then goes, (3) the goal's detail line, (4) the "Dome Sealed" row. **The dust line and the storm line never drop** (problem lines, like a stalled machine). So **what drops is the plot first**, which also fits the story: during a storm the player needs the power row, not the pressure history. At 1280×720 (GUI scale 3, 236 px high) the panel may exceed the target; the plot is gone in that state and the Technical Lead measures the height by screenshot (AC-17).

Optional (cheap): a "Storm" marker on the pressure plot's event list (the list holds up to 16), so the dip has a label. The storyboard's "power graph with a cause callout" has no data behind it (no power history exists); not required.

---

## 4. Acceptance criteria (Technical Lead)

Unit (pure) unless marked GameTest or screenshot.

| # | Test | Passes when |
|---|---|---|
| AC-1 | Stage timing | Uncleaned panel from stage 0 reaches stages 1 to 4 at 3, 6, 9, 12 sols (standard, Phase 1) and 0.75, 1.5, 2.25, 3 (classroom); Phases 2, 3, Equilibrium intervals 3.75, 6, 12; local ×2 halves them. Test reads balance sheet §2a and `panel_dust.json` and fails if they differ |
| AC-2 | Storm formula | `output = stage × (1 − 0.6 × strength)`; table in §2.5 reproduced for caps 1.0, 0.8, 0.5, 0.25 (1.84, 2.39, 3.22, 3.91 units/sol for a stage-1 panel) |
| AC-3 | Lock point | In a step-by-step run, `storm_strength` is 0 and solar supply equals its pre-warning value for every step from warning to the step before onset; the lock event and the first drop are the same step |
| AC-4 | Warning | Warning fires 1.0 sol (0.8 to 1.2) before onset in both presets; text key has no word from the banned set (solar, power, sunlight, light, dim); reminder at T−0.5 |
| AC-5 | Schedule | First storm onset sol 14 (standard) and 4.5 (classroom); fixed across 20 seeds; a player's actions (batteries, cleaning) never move it; later gaps 8 to 14 and 2 to 3.5 sols, seeded and reproducible after save and load |
| AC-6 | Landing guard | No F1 issued to a player mid-Landing; that player has no `prediction.issued` event; a later joiner after onset gets none |
| AC-7 | Pause | 100,000 paused ticks mid-warning, mid-storm and mid-ramp change nothing (invariant I-3) |
| AC-8 | Duration | First storm 0.75 sol (standard) and 0.5 sol (classroom); ramp-in 0.05, ramp-out 0.075 sol |
| AC-9 | Storm-end dust | No stage change before the ramp-out; +2 in Phases 1 and 2, +1 in Phases 3 and Equilibrium; capped at 4; `partly_cleaned` cleared |
| AC-10 | CR-F9 | Every message with `reveals: forecast.f1` is undeliverable (log, Monitor, HUD tooltip, journal, queue) while that player's F1 is open; `journal.storm.first` does not exist in the journal before resolve; the Shift Briefing, `monitor.storm.expected` and `journal.dust.panels` pass the banned-word scan. **Journal retrieval and the pending queue:** a player who reopens the journal or has queued items before F1 resolves cannot read `journal.storm.first`, `journal.storm.wind`, `journal.dust.storm_timing`, `journal.f1.real_vs_game` or `explain.f1.after_storm`; after resolve each is delivered at the next natural pause from the pending queue, in that order, once. The F1 "Why?" choices are not obtainable (UI, log, packet) before the result card. **CR-F11 scan:** every `failure.*` hint and journal asset that can be read before the lock passes the banned-word scan or declares `reveals: forecast.f1` and is held (see `failure.power_shortfall` below) |
| AC-11 | CR-F10 | F1 JSON names `resolve_source: storm.solar_cut`. A solver run where the player brushes stage-4 panels mid-storm: the key still passes; resolving on net power makes the same run fail (negative control) |
| AC-12 | CR-F2 and CR-F1 | Only "It drops" passes `outcome_check`; in a run with no panel the forecast resolves `void` |
| AC-13 | Group hiding | Two fake players answer differently; no packet or screen state for A contains B's choice before resolve; choice order differs per pseudonym and is stable across reopening; the anonymous tally appears only after resolve, only in the teacher view and the shared group screen, and is absent from every player's individual result card |
| AC-14 | Result timing | Result is first in the queue at the next natural pause after onset + 0.25 sol; `expired`, `late` and `answered` all get a result card; "wrong" never appears (CR-T5 scan; includes the "What caused it?" sub-line, "No penalty for any pick."). The HS "Why?" step is on the result card, logged unscored, no score field |
| AC-15 | Brush | Stage 0 after use, 64 uses, `panel.cleaned` logged with actor, stage before and storm-forecast flag; panel cleaned mid-storm still gets its storm-end stages |
| AC-16 | Dust line reveal | Line and BELL message absent during the Landing (both presets), present after it; `journal.dust.panels` the same. **Diagram:** `journal.dust.panels` shows the **one-path** diagram (dust on the panel only; no sky, storm or air path in the asset or its alt text) in every state; the **two-path** diagram appears only in `journal.storm.first`, which does not exist before F1 resolves. Test: the dust-page asset id differs from the storm-page asset id, and the dust-page asset is referenced by no held page (CR-F11) |
| AC-17 | Monitor height (screenshot at 1280×720 and one other GUI scale) | With dust line + storm line + a leaking dome, the plot is dropped before any problem line; no overlap; heights printed in the report |
| AC-18 | Sky (screenshot) | Haze frame (fog only, sun unchanged, solar unchanged) and storm frame (fog end about 18 %, sun disc about 30 %); no flash; both frames and a dusty-panel frame in the report |
| AC-19 | GameTest | Warning, then onset, then drop in supply, then storm-end stage change, in that order, from a scripted clock; save and reload mid-storm keeps strength, panel stages and the F1 state |
| AC-20 | Classroom | Scaled and unscaled values exactly as §2.8; Dome stable (sol 2.5) and the first warning (sol 3.5) are at least 1 sol apart |
| AC-21 | Wiper (if built) | Row at stage ≤ 1 while powered; draws 1 unit/sol/panel; no bearing in slice 5 |

---

## 5. Who must check what

### 5.1 Texts for the Instructional Designer pass

| Item | Check |
|---|---|
| F1 prompt, choices, "Why?" choices (**done 2026-11-28**; Why? moved to the result card) | Distractor plausibility, grade 6 to 8 reading level, no hint of the key; external teacher review (I3) or mark "pending external review" |
| F1 result depth 1 and depth 2 (incl. `m1` to `m4`) | Positive terms (CR-T5); "You called it" tone; reading level; whether the anonymous tally helps or feels like a score |
| `journal.storm.first` (MS and HS) and its diagram variant | Length budget (journal 80 words); two-cause idea clear |
| `explain.f1.after_storm` | Item and distractor wording, first-attempt rule, result lines; it is the ID's bank, so the ID may rewrite it |
| `log.storm.warn` (exists), `log.storm.remind`, `log.storm.onset`, `log.storm.end` | CR-T7(c) 20-word limit; no leak (CR-F9 `reveals`) |
| Monitor keys `monitor.storm.expected`, `.active`, `monitor.power.cause.dust`, `.storm`, `panel.lookat` | CR-T7 (a, b, d), CR-T8; the `−` and `%` forms |
| `journal.dust.panels` | Confirm no sentence lets a player infer "storms cut power" before F1 locks (CR-F9, journal copy rule) |
| Brush and wiper journal lines | Already in journal-dust §4; confirm reveal timing |

### 5.2 Facts for the SME

| Fact | Where | Status |
|---|---|---|
| Dust storms cut surface sunlight and solar power (F1 key); 2018 Opportunity, about −97 % (652 to 22 Wh) | F1 result, `journal.f1.real_vs_game` | **verified (opened)** and calc, storm-dust-review §3 (SME 2026-11-28) |
| "Dust in the air blocks sunlight": is "blocks" or "blocks and scatters" the better word for grades 6 to 12? | F1 result | open |
| Mars's thin air: wind pushes very little | F1 depth 2, `m3` | **true at today's pressure only** (storm-dust-review §5). The F1 storm is in Phase 1, so the line may stay, but say "On Mars today, the air is so thin..."; never reuse it in Phase 3 or equilibrium |
| A hazy sky as a real precursor of a storm (the pre-storm haze cue) | §2.4, DESIGN decision | **unverified** (SME did find that a lander's own rising haze is a real warning, tau 1.0 to 2.1 in about 1 sol, but not a haze before the storm reaches it); if not real, the cue is a game convenience and the journal must not call it real |
| Wind-cleaning of panels is rare and not a storm effect (`m2`) | F1 depth 2 | needs the dust-review §2 wording check |
| Cold panels work slightly better (distractor in `explain.f1.after_storm`) | After-storm item | SME round 2 (exists) |
| Storm dust keeps settling for **weeks** after the sky clears (NASA); "days" in `journal.dust.storm_timing` must become "weeks" | `journal.dust.storm_timing` | **verified**; ID to change the word |
| Real storm cuts (about −82 % 2007, −97 % 2018, −35 % InSight regional) versus the game's 60 % maximum (cap 1.0 ≈ tau 2.4) | `journal.f1.real_vs_game` | **verified (opened)** / calc, storm-dust-review §3.5 |
| Sun disc at about 30 % and sky `#9A6A48` under a storm | §2.4 | Artist's values; ask whether plausible, not authoritative |
| One-sol warning lead on Mars | Warning text | game rule; the text claims nothing real |

---

## 6. Questions for the owner (at most three)

| # | Question | Type | Recommendation |
|---|---|---|---|
| 1 | **Classroom storm numbers:** first storm at sol 4.5 and always 0.5 sol long (chosen), or plain ÷4 scaling (onset 3.5, length 0.75)? | Ill-structured (pacing against class time; no data yet) | Chosen values (4.5, 0.5): keeps one gate sequence at a time and a 10-minute storm. Revisit after the first classroom run |
| 2 | **Ship F1 without the external teacher reviewer** (input I3), marked "accepted, pending external review"? | Well-structured (plan's own fallback) | Yes. Distractors come from SME round 2; change budget about 10 % of the touched items (Producer guess, unverified) |
| 3 | **Add a "how much?" follow-up to F1** (a second prediction on the size of the drop)? | Ill-structured (more B3 depth against more cards; F1 will probably be answered right by most players) | Not now. Log F1 accuracy in playtest; add a size item to a later storm (Phase 2) only if accuracy is near 100 % |

## 7. Assumptions

- The Technical Lead can add the overlay input to the keyframe loader; if not, fog-only fallback (§2.4).
- The tier rules of balance §2d.4 are built as written (tier 2 and 3 get nothing while tier 1 uses the battery).
- Natural pauses occur before onset often enough that most players see the card; the playtest will say (expired counts are logged).
- A first storm at sol 14 means a standard player is past Dome stable; a slow player still gets the storm at sol 14 (fixed schedule).
