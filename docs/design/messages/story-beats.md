# Messages: Essential Story Beats (final text)

**Owner:** Lead Game Designer · **Status of every entry:** draft: ID review pending; science lines use SME round 2 wording · Beat numbers: [story bible](../story-bible.md) §6.2 · Conventions: [README.md](README.md)

Ten essential beats not covered in the phase files. Each is ≤ 60 words of message text (log lines ≤ 20 words; journal ≤ 80). Beats that claim something about the world carry an `asserts` condition (story bible §6.2). Fiction notes are shown in both bands. Unless a row says otherwise, HS text is the same as MS.

---

## Beat 1: Storm clears (P1, spawn)

| Field | Value |
|---|---|
| `id` | `beat.01.storm_clears` |
| `type` | colony log (BELL), opens the game after the opening card |
| `trigger` | First join, after the opening card; before the Landing checklist intro |
| `asserts` | dome has ≥ 1 open breach |
| `delivery` | Top line, then pinned to the side panel above the checklist |
| `text.ms` | Sol 1. Storm passed. Damage logged: Hab B seal cracked, main dish down, power at {power_pct} %. Keeper on watch. |
| `goal_ids` | A1, C1 |
| `instructional_purpose` | Names the three life-critical systems before Landing teaches them |
| `motivational_purpose` | Instant stakes; "you're on watch", not lost |
| `science_claims` | None (fiction setup) |
| `fiction_note` | None (opening card covers it) |

## Beat 3: First failure, with Hana's tip (P1, first failure of each type)

| Field | Value |
|---|---|
| `id` | `beat.03.first_failure` → uses `failure.seal_breach.1` (failure-reports.md) for the usual first case |
| `type` | failure report (BELL) + taped note (Hana) |
| `trigger` | First failure of each type; Hana's tip only the first time |
| `asserts` | the failure is active |
| `delivery` | Top line + Shift Board slip; the tip appears on the machine or wall |
| `text.ms` | Air loss in Hab B. Likely cause: a seal cracked in the cold night. Which blocks touch the outside? *Hana's note: "Cold finds the corners first. —H."* |
| `goal_ids` | C2, A1 |
| `instructional_purpose` | Cause + question hint, never the fix (CR-H2 banned tokens) |
| `motivational_purpose` | Hana as a mentor who expected this; failure is normal |
| `science_claims` | Cold contraction can open seals (general engineering; no number) |

## Beat 4: First dust storm warning (P1, first storm)

| Field | Value |
|---|---|
| `id` | `beat.04.storm_warning` (log line `log.storm.warn`) |
| `type` | colony log + HUD storm icon; F1 queued (phase-1.md §5) |
| `trigger` | First dust storm scheduled, T−1 sol |
| `asserts` | storm scheduled within 0.8–1.2 sol |
| `delivery` | Top line; HUD icon with countdown; Monitor "Storm forecast" row |
| `text.ms` | Dust storm forming. Arrival in about 1 sol. Expected length: half a sol to a full sol. |
| `goal_ids` | C1, B3 |
| `instructional_purpose` | Warning time for planning (C1). **Does not mention solar power**, so F1 isn't answered by the warning (lock-point rule) |
| `motivational_purpose` | Tension with time to act |
| `science_claims` | Mars dust storms are real and can last days to months (2018 storm, round 2 [S12]); the game shortens them |
| `fiction_note` | None (storm length compressed; covered by the timescale note) |

## Beat 6: Relay restored (P1, Dome stable)

| Field | Value |
|---|---|
| `id` | `beat.06.relay` (BELL line) and `msg.hana.relay` (Hana, essential) |
| `type` | colony log + recorded message |
| `trigger` | Dome stable gate |
| `asserts` | Dome stable reached |
| `delivery` | BELL top line; Hana's message in the side panel at the next natural pause; Moreno's and Basu's backlog filed to the archive (optional) |
| `text.ms` (BELL) | Relay restored. Seven sols of messages received. Note: Lab Fridge B lock set to "Hold until thaw (T. Basu)." |
| `text.ms` (Hana) | Keeper! Relay's up, so you made it. Told you the filters whine. Life support's steady. Time to start on the sky. Plans for a Regolith Outgasser are in your journal. I taped notes to the first one. —Hana |
| `goal_ids` | B1 (sets up the first lever); seeds D1 (Fridge B) |
| `instructional_purpose` | Hands over the first planet lever with the worked example (beat 6a) |
| `motivational_purpose` | Not alone any more; a mystery |
| `science_claims` | None |
| `fiction_note` | None |

## Beat 9: The Thaw (P2, Thaw gate)

| Field | Value |
|---|---|
| `id` | `beat.09.thaw` (log), `note.basu.fridge_b` (note), `journal.gate.thaw` (journal) |
| `type` | colony log + world trace (note in Fridge B) + gate journal entry |
| `trigger` | Thaw gate; BELL's motion line within 1 sol, at the first thawling spawn |
| `asserts` | pressure ≥ 5 kPa and a liquid water block near a machine |
| `delivery` | Top line; Fridge B unlocks with the note inside; journal entry |
| `text.ms` (BELL) | Sol {sol}. Temperature −20 °C. Meltwater near {machine}, refroze overnight. Unidentified motion near {machine}. Logging. |
| `text.ms` (Basu's note) | If you're reading this, it's warm enough. They might wake up. Watch them before you decide anything. —Teo |
| `text.ms` (journal) | The ice by the {machine} melted into a puddle today. Tonight it froze again. Back when the air was thinner, ice by the heater boiled away at once. To keep water liquid, we need warmth *and* thick air. One is not enough. *On Earth, thick air keeps water from boiling away. That's why puddles last.* |
| `goal_ids` | A2, B5, D1 |
| `instructional_purpose` | The A2 contrast (F4a → F4b) in the player's own journal |
| `motivational_purpose` | Wonder, then unease: something moved |
| `science_claims` | Water needs pressure and warmth (SME B1, round 2 §1.1) |
| `fiction_note` | Shown with the first thawling sighting: *No life has ever been found on Mars. Scientists are still looking for tiny microbes that might live deep underground. The thawlings are made up for this game.* |

## Beat 10: The secret (P2, ~1 sol after the first fauna sighting)

| Field | Value |
|---|---|
| `id` | `msg.basu.secret` (essential); `msg.moreno.secret`, `msg.mensah.earth_part` (archive) |
| `type` | recorded message |
| `trigger` | 1 sol × `story.delayScale` after the first thawling sighting |
| `asserts` | ≥ 1 thawling seen |
| `delivery` | Side panel at the next natural pause |
| `text.ms` | Keeper, it's Teo. We knew they were there. Earth asked us to wait for test results. Dalia agreed. I argued, then I said nothing too. You should have heard it from us. They were sleeping, not dead. Look at them first. Then decide. |
| `goal_ids` | D1, B5 |
| `instructional_purpose` | Opens the D1 question without telling the player which side is right |
| `motivational_purpose` | Trust shaken and repaired: adults admit mistakes |
| `science_claims` | None. "Wait for results" is Earth's choice, not planetary-protection policy (SME round 2) |
| `fiction_note` | Covered by the beat 9 note |

## Beat 12: Hydrosphere (P2, Hydrosphere gate)

| Field | Value |
|---|---|
| `id` | `beat.12.hydrosphere` (log), `journal.gate.hydrosphere` (journal); fauna and collapse failures use failure-reports.md |
| `type` | colony log + gate journal entry |
| `trigger` | Hydrosphere gate |
| `asserts` | hydrosphere ≥ 10 % |
| `delivery` | Top line; journal entry at the next natural pause |
| `text.ms` (BELL) | Surface water: {hydro_pct} % of the surface. Thawling visits rising at {machine}. |
| `text.ms` (journal) | Water stays on the surface now. The thawlings have grown into a bigger, busier life stage. They come to warm, wet machines. Check the fauna page: your machines set how strong the pull is. Under the dust is ice, too. Warm it, and the ground can drop. |
| `text.hs` (journal) | Same as MS. Read more: some Earth animals wait dormant until rain or warmth returns, then wake and grow, like spadefoot toads or mosquito eggs. That part is real; the thawlings are not. |
| `goal_ids` | B2, B5, C2 |
| `instructional_purpose` | Names the signature loop and the ground-ice side effect at the moment both start |
| `motivational_purpose` | Conflict peaks: what moves the planet draws the animals |
| `science_claims` | Mars ground ice and collapse (round 2 §1.4); dormant animals that wake with water (round 2 §1.7, not opened) |
| `fiction_note` | The thawlings are made up for this game. No life has ever been found on Mars. |

## Beat 15: First O2 overshoot fire (P3)

| Field | Value |
|---|---|
| `id` | `beat.15.o2_fire` (uses `failure.o2_overshoot_fire.1`) + `journal.fire` |
| `type` | failure report + journal note |
| `trigger` | First fire started by O2 above the fire band |
| `asserts` | O2 fraction > 23 % |
| `delivery` | Top line + Shift Board slip; journal at the next natural pause |
| `text.ms` (report) | Fire near {place}. Likely cause: O2 above the Air window. What's adding O2 fastest? |
| `text.ms` (journal) | Below about 15 % oxygen, most things won't keep burning. Above about 23 %, almost everything burns too easily. That's why the Air window has a top as well as a bottom. |
| `goal_ids` | A5, B4 |
| `instructional_purpose` | The top edge of the band, felt and explained |
| `motivational_purpose` | Drama; a mistake the player can recover from |
| `science_claims` | Fire limits about 15 % and 23 % O2 (round 2 §6 note 11, sourced) |

## Beat 17: Helmet off (P3, Breathable)

| Field | Value |
|---|---|
| `id` | `beat.17.helmet_off` (log), `msg.moreno.breathe` (Moreno, essential), `journal.gate.breathable` |
| `type` | colony log + recorded message + gate journal entry |
| `trigger` | Breathable gate (hold complete) |
| `asserts` | breathable condition true (live) |
| `delivery` | Top line; the helmet icon opens; Moreno's message at the next natural pause; journal entry |
| `text.ms` (BELL) | Breathable air confirmed for {hold_sols} sols. Helmet optional outside. |
| `text.ms` (Moreno) | Keeper, it's Dalia. BELL's numbers just came in. Breathe for all of us. Take a deep one. Then go and look at what you built. |
| `text.ms` (journal) | The air outside is thick enough, has enough oxygen push, and has little CO2. All three, at once. It took the whole system, not one machine. |
| `text.hs` (journal) | Same as MS. Read more: your lungs need about 16 kPa of oxygen pressure. At 75 kPa, that means about 21.5 % O2 or more. |
| `goal_ids` | A5, A1, B4 |
| `instructional_purpose` | Breathability is three conditions at once (O2 partial pressure, total pressure, CO2) |
| `motivational_purpose` | The emotional peak |
| `science_claims` | 16 kPa O2 partial pressure; CO2 ≤ 0.5 kPa (user decision on SME recommendation) |
| `fiction_note` | Earth's air is mostly nitrogen. Mars has very little. The game doesn't track it; real terraformers would have to find some. |

## Beat 19: The beacon (ending)

| Field | Value |
|---|---|
| `id` | `beat.19.beacon` (BELL), `ending.reply.*`, `ending.intro`, final question (prompts-and-reflections.md §2) |
| `type` | ending sequence |
| `trigger` | Beacon lit (Equilibrium hold complete; beat 18 merged here) |
| `asserts` | equilibrium hold complete |
| `delivery` | Ending sequence (story bible §6.5); the one original music piece plays here |
| `text.ms` (BELL) | Signal sent. Delay to Earth: {delay_min} minutes. |
| `text.ms` (Moreno) | Signal received, Keeper. The ships are fueling. Your call got us here. |
| `text.ms` (Basu) | Tell the colonists about Group S. Tell them first. |
| `text.ms` (Hana) | Leave them notes on every machine. Trust me. |
| `text.ms` (Mensah) | `earth.ending.mensah` (phase-3.md §6) |
| `text.ms` (ending cards intro) | Here is the planet you're handing over. |
| `goal_ids` | D1, D2, all (Keeper's Record handover) |
| `instructional_purpose` | Sets up the final question; the cards describe, never rank |
| `motivational_purpose` | Pride and a quiet question |
| `science_claims` | Earth–Mars light delay 3–22 minutes (**unverified**: SME to source; `{delay_min}` drawn from that range) |
| `fiction_note` | None (opening card) |
