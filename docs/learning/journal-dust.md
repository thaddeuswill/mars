# Journal Content: Solar Panel Dust (learning spec, shared format)

**Owner:** Lead Instructional Designer · **Status:** Draft 1, 2026-11-26 (BACKLOG #26); storm wording pass **proposed** 2026-11-28 (§3, §4 wind, §6, §7 diagram, new §8 and §9; SME source: [storm-dust-review.md](../science/storm-dust-review.md)) · **Science:** SME, [docs/science/dust-review.md](../science/dust-review.md) §8 and addendum §11–12 · **Mechanic:** DESIGN.md "Solar panel dust", balance sheet §2a · **Format:** DOCUMENTATION.md §5.1

**What this file is.** The journal notes, honesty notes, Earth link, and "What caused it?" item that go with the panel dust mechanic, in the shared format. Wording is the SME's, trimmed to the length budgets (journal ≤ 80 words plus "read more"). The final in-game text lives in `docs/design/messages/` (Lead Game Designer); these entries are handed to them to copy in, in BELL's or the Keeper's voice, **without changing any fact or tag**. I did not edit their files.

**Claim tags** (the SME's): **sourced** (source opened) · **searched** (search summary, not opened) · **calc** · **invented** (game balance) · **unverified**. **Rule (SME):** a *searched* number must not appear in game text until its source is opened. So every entry whose text carries a searched number has a **fallback text with no number**, to ship if the source still isn't opened at Stage 4. The honesty point survives either way.

---

## 1. `journal.dust.panels`: what dust does (the main note)

| Field | Value |
|---|---|
| `id` | `journal.dust.panels` |
| `type` | journal entry ("what happened" page, with diagram 9 or a new dust diagram; see §7) |
| `trigger` | First time any panel on the planet reaches dust stage 1 (the same moment BELL says "New on the Monitor: panel dust") |
| `delivery` | Journal page, at the next natural pause; colony log line points to it |
| `text.ms` | **Dust on the panels.** Fine Martian dust lands on solar panels. On real Mars rovers and landers, power fell by about 0.2 percent each sol. Some days wind blew dust off and power jumped back. One lander, InSight, was never cleaned. Its power fell from about 5,000 to about 500 watt-hours a sol in under four years. **In the game, dust builds about 15 times faster so you can see it.** |
| `text.ms` (fallback if Lorenz 2021 isn't opened) | **Dust on the panels.** Fine Martian dust lands on solar panels. On real Mars, power fell a little each day. Some days wind blew dust off and power jumped back. One lander, InSight, was never cleaned. Its power fell from about 5,000 to about 500 watt-hours a sol in under four years. **In the game, dust builds much faster than on real Mars, so you can see it.** |
| `text.hs` (read more) | Rates on real missions ranged from 0.05 to 2 percent per sol. Spirit's panels once gave about 50 percent more power in a single sol after a cleaning. Cleaning events are probably caused by dust devils, but this isn't proven, and some sites never get them. |
| `goal_ids` | C1, B1, A1 |
| `instructional_purpose` | Explains the power drift the player just saw; separates panel dust from storm dimming; carries the **required honesty note** that the game rate is compressed |
| `motivational_purpose` | A real-mission story (InSight) that makes upkeep feel like the real job |
| `hint_level` | — |
| `science_claims` | ~0.2 %/sol typical (**searched**, Lorenz 2021); InSight 5,000 → 500 Wh/sol (**sourced**, PIA25284); "under four years" (~1,230 sols, **calc**); ~15× (**calc** from 3.3 % vs 0.2 %, so it inherits the *searched* tag); 0.05–2 %/sol (**searched**); Spirit +~50 % in one sol (**searched**/**sourced** per dust-review §2); dust devils as cause "probably" (**searched**) |
| `fiction_note` | Not fiction; the speed is a stated simplification |
| `status` | draft: SME wording; ID-reviewed (this file); Lead Game Designer to place. Ships the fallback MS text unless Lorenz 2021 is opened |

Word counts: MS 71 words; MS fallback 68; HS read more 46 (budget: journal ≤ 80 + read more).

## 2. `journal.dust.floor`: the 60 % floor is optimistic

| Field | Value |
|---|---|
| `id` | `journal.dust.floor` |
| `type` | journal honesty note (simplification) |
| `trigger` | First time any panel on the planet reaches stage 4 (the floor) |
| `delivery` | One line added to the `journal.dust.panels` page; colony log points to it |
| `text.ms` | The game never lets a dusty panel fall below 60 percent. A real panel that's never cleaned keeps losing power. InSight's fell to about a tenth. The game stops at 60 percent so you have time to react. |
| `text.hs` | Same as MS |
| `goal_ids` | C1, A1 |
| `instructional_purpose` | Prevents the misconception "dust can never kill power" (dust-review §11) |
| `motivational_purpose` | Fairness: tells the player why the game is kind here |
| `science_claims` | InSight about a tenth of its early output (**sourced** PIA25284 + **calc**; season effects mixed in, SME); 60 % floor (**invented**) |
| `fiction_note` | Simplification note; both bands (never band-gated) |
| `status` | draft; no searched numbers, can ship |

## 3. `journal.dust.storm_timing`: storm dust keeps falling for weeks

| Field | Value |
|---|---|
| `id` | `journal.dust.storm_timing` |
| `type` | journal honesty note (simplification) |
| `trigger` | End of the first storm that adds panel dust (**after** F1 has resolved for that player, so it can't give F1 away; `reveals: forecast.f1`) |
| `delivery` | Line on the `journal.dust.panels` page |
| `text.ms` (**proposed 2026-11-28**) | In real storms, dust keeps falling out of the air for weeks after the sky starts to clear, and some of it lands on panels. The game adds all of it when the storm ends, to keep it simple. |
| `text.ms` (before) | In real storms, dust keeps settling on panels for days after the sky clears. The game adds it all when the storm ends, to keep it simple. |
| `text.hs` | Same as MS |
| `goal_ids` | B1, C1 |
| `instructional_purpose` | Corrects "a storm coats panels instantly" and "the sky clears and the dust is gone" (storm-dust-review §5.2) |
| `science_claims` | Dust keeps falling out of the air for **weeks** (**verified**, NASA N1 and N3, storm-dust-review §5.2 and §9 item 2). Dust can also add to the panel layer (**verified**, N2). The game adding it at storm end is a simplification (stated) |
| `fiction_note` | Simplification note; both bands |
| `status` | **proposed**; SME change applied ("days" to "weeks"); no number, can ship. 39 words, rough FK 5.0 |

## 4. Short honesty notes (misconceptions, dust-review §11)

Each is one line, both bands, on the dust page or its trigger moment. All are **simplification or fiction notes**, never band-gated.

| `id` | Trigger | Text | Misconception it prevents | Claim tags |
|---|---|---|---|---|
| `journal.dust.wind` | First dust-devil cleaning on the planet | "Wind cleaned some real panels, like Spirit's. It never cleaned InSight's." **Proposed:** "Now and then, wind cleaned some real panels, like Spirit's. It never cleaned InSight's." (adds the rarity; the plain line can feed the distractor "storm wind cleans the panels, so power rises" before F1, 2026-11-28 review; SME: cleaning is about one event per 700 sols at the best sites, storm-dust-review §2) | "Wind always cleans" / "nothing cleans it" | Spirit events, InSight none: **sourced**/**searched** (dust-review §2) |
| `journal.dust.not_only_power` | Dust page, after the first panel cleaning | "Dust also gets into joints and air filters. Power is just the easiest thing to measure." | "Dust only hurts power" | General (**unverified** in detail). I changed the SME's "lungs" to "air filters": filters are already in the game (filter clog), and lung effects aren't sourced |
| `journal.dust.rain` | First Phase 3 rain that cleans panels | "On Earth, rain helps, but the fine dust stays. Your panels won't come fully clean either." | "Rain washes everything clean" | Rain leaves fine dust: **searched** (NREL), qualitative |
| `journal.dust.base` (HS read more) | Dust page, read more | "In the game, panels near your digging and machines get dustier. On real Mars, rocket landings throw dust, but no one has measured dust from a base's daily work." | Treating an invented ×2 rule as fact | Landing plumes **searched**; daily-work effect: real value missing; ×2 **invented** |
| `journal.dust.brush` | First panel brush crafted | "No Mars robot ever carried a panel brush. A person with a brush is something only a crewed base would have." | "This is how rovers were cleaned" | No Mars array brush found (**searched**, dust-review §9.8). Fiction note |
| `journal.dust.wiper` | Panel wiper unlocks (Phase 2) | "This wiper is a game machine. Real engineers are testing dust shields with no moving parts; one worked on the Moon in 2025." | "Mars panels have wipers" | Electrodynamic dust shield on Blue Ghost 1, 2025 (**searched**). **Fallback** (if not opened): "This wiper is a game machine, inspired by real dust-shield research." |

**Not used:** the "flour or talc" particle-size note (size **unverified**; add only after the SME cites it).

## 5. `earth.dust.solar_farms`: D2 Earth link

| Field | Value |
|---|---|
| `id` | `earth.dust.solar_farms` |
| `type` | Earth link ("On Earth" line, last line of the dust page) |
| `trigger` | Shown with `journal.dust.panels` |
| `delivery` | Last line of the dust journal page (story bible §6.3 journal pattern) |
| `text.ms` | **On Earth:** desert solar farms lose about half a percent of their power each day to dust. They're cleaned with water, brushes, or robots. Rain helps, but it leaves the fine dust. |
| `text.ms` (fallback if the soiling study isn't opened) | **On Earth:** desert solar farms lose power to dust too, and they're cleaned with water, brushes, or robots. Rain helps, but it leaves the fine dust. |
| `text.hs` (read more) | Same idea as on Mars: losses build slowly, and someone has to plan the cleaning. Real farms weigh cleaning costs (water, labor) against lost power. |
| `goal_ids` | D2, C1, B4 |
| `instructional_purpose` | D2: the **same mechanism** (soiling, gradual loss, planned cleaning) on Earth, so it's transfer, not trivia. HS adds the B4 cost trade-off |
| `science_claims` | ~0.5 %/day (QEERI soiling test 0.49 %/day: **searched**); cleaning methods (**searched**, general); rain and fine dust (**searched**, NREL). HS cost trade-off: general (**unverified** as a specific claim; no number) |
| `fiction_note` | None |
| `status` | draft. Ships the fallback MS text unless the soiling source is opened |

## 6. `journal.f1.real_vs_game`: F1 real-versus-game line

| Field | Value |
|---|---|
| `id` | `journal.f1.real_vs_game` |
| `type` | journal honesty note; **the same string is also shown at the end of the F1 result's "Read more"** (one key, two places) |
| `trigger` | F1 resolved for that player (never before the lock: CR-F9; `reveals: forecast.f1`) |
| `delivery` | Read-more line on the F1 journal page, both bands |
| `text.ms` (**proposed 2026-11-28, SME §10 plus one added sentence**) | In 2018, a storm that covered all of Mars cut the rover Opportunity's power by about 97 percent. Your storms cut {max_cut} percent at most. Real storms can be bigger, and they last days to months. Yours last a sol or less, so you can keep playing. |
| `text.hs` | MS text, then: NASA believes the rover's batteries ran out, and that ended its mission. |
| `text.ms` (before) | In 2018, a storm that covered all of Mars cut the rover Opportunity's power by 97 percent, and it went silent. Your storms cut 60 percent at most. Real storms can be much bigger. |
| `text.ms` (fallback, no numbers; may stay as a switch) | Real dust storms on Mars can cover the whole planet and cut solar power far more than in the game. One in 2018 left the rover Opportunity without enough power. |
| `goal_ids` | C1, A1 |
| `instructional_purpose` | Honest scale: the game's storm is milder and shorter than real; links C1 planning to a real mission |
| `science_claims` | "About 97 percent": **calc** from 652 to 22 Wh, with 21 Wh and tau about 10.5 to 10.8 **verified (opened)** on NASA pages N7, N8; the 652 Wh baseline is Wikipedia citing NASA (secondary), so keep "about" (storm-dust-review §3.1, §10). {max_cut} is **invented** (60 / 48 / 30 / 15 by phase, balance §2a). "Days to months" **verified** (storm-dust-review §3.3). The compression ("a sol or less") is a game fact. Fate of Opportunity: "NASA believes" **verified** (N2); never say the storm destroyed it, and "went silent" is removed (it implied the storm was the cause) |
| `fiction_note` | Simplification note |
| `status` | **proposed**; SME-approved wording plus one ID sentence for the time compression (SME §7: "name the compression") and `{max_cut}` so the line stays true in later phases (the old "60 at most" was false from Phase 2). 47 words, rough FK 3.8. Hand check: "bigger" can mean wider or stronger; SME's word kept, listed as an open wording question |

## 7. "What caused it?" item: power drop (B1)

| Field | Value |
|---|---|
| `id` | `explain.power_drop` |
| `type` | "What caused it?" check item (question bank: mine) |
| `trigger` | Available on the "What caused it?" page after the dome's solar output has fallen ≥ 10 % over 1 sol from **one dominant cause** (panel dust, storm, or night), at most once per cause per phase |
| `prompt.ms` | Your solar power dropped this sol. What caused it most? |
| `choices` | (keyed by the Monitor's largest cause) Dust on the panels · A dust storm dimming the sky · Night · The panels got cold |
| `result.ms` | Shows the right cause and the Monitor line that shows it: "Panel dust −{n} %" or "Storm: less sunlight −{n} %". |
| `goal_ids` | B1, C1 |
| `instructional_purpose` | The two dust causes (on the panel vs. in the sky) must not blur; this checks that they don't |
| `science_claims` | "Cold panels" is the distractor: cells work slightly *better* cold (SME round 2, F1), so it's not the cause of a drop |
| `status` | draft; first attempt only counts (LEARNING.md 3.10) |

**Diagram (changed 2026-11-28):** two diagrams, not one. The **panel dust page** (`journal.dust.panels`, visible from the first minutes after the Landing) uses a **one-path** variant of diagram 9: dust **on the panel** only, no air path, no storm, no "dims the sky". The earlier text ("two labeled paths on the dust page") would have shown the F1 answer (a storm dimming the sky) about 13 sols before F1 locks, in a page the player can reopen (CR-F9, journal copy rule). The **two-path** variant (dust in the air, dust on the panel) belongs to `journal.storm.first` (§8), which does not exist before F1 resolves. Asset U-25 needs two files. Technical Lead and Artist: see the report handoffs.

## 8. Storm entries (proposed 2026-11-28; all appear only after F1 resolves for that player)

Source texts: SME [storm-dust-review.md](../science/storm-dust-review.md) §10, spec [f1-storm-spec.md](../design/f1-storm-spec.md) §1.5 to §1.7. Every entry below declares `reveals: forecast.f1` and does not exist in the journal until the player's F1 is resolved, expired or late (CR-F9, journal copy rule). Rough FK is a throwaway script (vowel-group syllables), **not the CR-T6 check**.

### 8.1 `journal.storm.first`

| Field | Value |
|---|---|
| `id` | `journal.storm.first` |
| `trigger` | That player's F1 `prediction.resolved` (any result), or onset plus late window if F1 was never issued to them |
| `text.ms` (**proposed**) | **The first dust storm.** Dust rose into the air and blocked some sunlight, so your panels made less power. When the sky cleared, the sunlight came back. But dust had settled on your panels, so they still made less than before. These are two causes. Dust in the air clears on its own. Dust on the panels stays until you clean it. |
| `text.ms` (before, spec §1.6) | Dust rose into the air and blocked some sunlight, so your panels made less power. When the sky cleared, dust settled on the panels too, and they made even less. These are two causes: dust in the air goes away by itself; dust on the panels stays until you clean it. |
| `text.hs` (read more, **proposed**) | The Monitor keeps the two causes on separate lines: "Storm" and "Panel dust". Checking which one moved tells you what to do about it. The two losses multiply, so a dusty panel loses power in a storm too. |
| `goal_ids` | B1, C1, B3 |
| `science_claims` | Dust in the air cuts surface sunlight (**verified**, NASA N2); dust settles and coats panels (**verified**, N2); the two-cause split is game structure. Multiplying losses: game formula (balance §2a), **invented** |
| `status` | **proposed**. 62 words (budget 80), rough FK 2.1 |

**Why the "before" text was changed (hand review, the script cannot see it).** "They made even less" reads as "less than during the storm", which is false: at storm end the sky clears (the storm cut goes away) and the dust stages are added, so a stage-1 panel goes from 36.8 percent of clean output in the storm to 70 percent after it (spec §2.5, §2.6 calc). The real picture is "back up from the storm, but below where you started". The change also sets up `explain.f1.after_storm`, which asks exactly this. The HS sentence on multiplying is optional; it answers the misreading that the two Monitor percentages add up.

### 8.2 `journal.storm.wind` (new; SME §10 wording, ID changes marked)

| Field | Value |
|---|---|
| `id` | `journal.storm.wind` (two variants chosen by live pressure) |
| `trigger` | That player's F1 resolved, as a journal page at the next natural pause (not a card). **Not "the first storm after F1 resolves"**: that reads as the second storm, which is 8 to 14 sols away in standard play (spec §2.1) and would leave the wind misconception uncorrected for most of Phase 1. Variant A when the planet's pressure is below 5 kPa (gate "Phase 2 begins" not reached); variant B after |
| `text.ms` variant A (**proposed**) | The wind in a Mars storm can reach 60 miles an hour. But right now the air is so thin that it pushes about as hard as a breeze on Earth. Storms on Mars dim the sky. They do not knock things over. |
| `text.ms` (SME draft) | The wind in a Mars storm can reach 60 miles an hour. But the air is so thin that it pushes about as hard as a gentle breeze on Earth. Storms on Mars dim the sky. They do not knock things over. |
| `text.hs` variant A (read more) | Wind pushes harder when the air is denser and when it moves faster. Today Mars's surface air is about 1/80 as dense as Earth's, so the same wind pushes about 1/80 as hard. As your air gets thicker, wind will matter more. |
| `text.ms` variant B (pressure at or above 5 kPa) | Your air is thicker now, so the same wind pushes harder than it did at the start. In the game, storms get weaker as the world settles. That is a game rule, not Mars physics. |
| `goal_ids` | B1, C1, A1 |
| `science_claims` | Up to about 60 mph: **verified** (NASA N1). "A fraction of the strength of storms on Earth": **verified** (N1). Push about 1/80 and "like an 11 km/h breeze": **calc** (storm-dust-review §5.1, 210 K assumed). At 5 kPa the same 97 km/h wind feels like about 31 km/h on Earth (calc §5.2 table); storm weakening in later phases: **invented** |
| `reveals` | `forecast.f1` ("Storms on Mars dim the sky" is the F1 answer) |
| `status` | **proposed**. A: 43 words, rough FK 1.5; HS 44 words, FK 5.1; B 35 words, FK 3.1 |

ID changes to the SME draft: (1) "right now" and "a breeze" for "gentle breeze": by Beaufort, a 11 km/h wind is a light breeze and a 31 km/h wind is a fresh breeze, so "gentle" is wrong at both ends; and the first storm comes at sol 14, when pressure has already risen from 0.6 kPa, so a fixed "gentle" would be false for some worlds. (2) The 5 kPa switch to variant B: the SME warned against shipping variant A in Phase 3, but the table shows the push is already about three times today's at 5 kPa, so "so thin" stops being a safe reason at the Phase 2 gate. SME to confirm the switch point. (3) "Storms on Mars dim the sky" is true of real storms (NASA N5, N6); it stays.

### 8.3 `explain.f1.after_storm` (B1, bank item; changes proposed)

| Part | Before (spec §1.7) | Proposed |
|---|---|---|
| `trigger` | Storm ended, F1 resolved, "Panel dust" at least 5 percentage points larger than before | Same, **plus: the Monitor's largest remaining cause line is Panel dust**. Without it, a storm that ends at night makes Night the largest cause, the key is not on the list, and the item has no right answer |
| Prompt | The storm is over, but your solar power is still lower than before it. What is the main cause? | Same (19 words, rough FK 3.0) |
| Distractor 2 | The storm is still going | **The battery is low.** "Storm still going" is not a misconception anyone holds once the log says the sky is clearing; it is a free elimination. "Low battery" stands for mixing up power made with power stored, a real confusion at this age |
| Distractor 3 | The panels got too cold | Same (SME round 2: cold cells work slightly better) |
| Distractor 4 | The Sun is giving off less light | Same (M4, source and path confused) |
| Result line for distractor 2 | The Monitor's storm line is gone, so the sky is clear. | Low battery: "A low battery means less stored power. Your panels still make what the Sun gives them." |
| Result on the key | Clean the panels with a brush. The Monitor's Panel dust line shows how much you gain. | Same wording; the line name must match the final Monitor key (see the 2026-11-28 storm-wording report, Monitor forms) |
| Sub-line | "Pick the most likely cause. No penalty for a wrong pick." (messages/README.md) | "Pick the most likely cause. No penalty for any pick." The word "wrong" appears nowhere else in the UI (spec AC-14 scans for it) |

## 9. Pre-lock check: every `journal.dust.*` text against CR-F9 (2026-11-28)

Question: before F1 locks (sol 14 standard, 4.5 classroom), can any journal-readable text let a player infer that a storm cuts solar power? Rule of this check: a text leaks if it says or shows (a) a storm, the sky, or airborne dust together with less light or less power, or (b) the "dust in the air" path.

| Entry | Readable before the lock? | Result |
|---|---|---|
| `journal.dust.panels` (fallback MS text; HS held) | Yes, from the first pause after the Landing | **Text passes**: it says dust **on panels** cuts power (that is the panel line's own cause), never "storm", "sky", "air" or "sunlight". **Residual inference, accepted**: a student who has read "dust lowers power" can reach "dust storm lowers power" by analogy and answer "It drops" for the wrong reason (right direction, wrong path). That is not the rule's breach (the rule bans saying it), and the F1 result and the HS "Why?" separate the two paths. Log F1 accuracy before the first classroom run; if it is near 100 percent from sol 1, the F1 key is too easy and FI-1 and FI-4 carry B3 |
| The diagram on `journal.dust.panels` | Yes | **Leaked as specced** (two paths, "storm, dims the sky"): fixed in §7 to a one-path diagram |
| `earth.dust.solar_farms` (shown with the panels page) | Yes | **Passes**: desert farms and dust on panels; no storm, no sky |
| `journal.dust.floor` | From the first stage-4 panel (before F1 possible) | **Passes** |
| `journal.dust.wind` | After the first dust devil | **Passes**; rarity word added so it does not push toward the distractor "storm wind cleans the panels" (M2) |
| `journal.dust.not_only_power`, `.base`, `.brush` | Yes | **Pass** (no storm, no sky, no light) |
| `journal.dust.rain`, `.wiper` | Phase 2 and 3, after F1 | Not applicable |
| `journal.dust.storm_timing`, `journal.f1.real_vs_game`, `journal.storm.first`, `.wind`, `explain.f1.after_storm` | **No**: each is held until that player's F1 resolves | Pass if the Technical Lead's hold covers journal retrieval and the pending queue (AC-10) |
| `briefing.shift1` ("Mars weather can change fast, so keep some power in reserve"; `pack/data/mars/journal/briefing_shift1.json`) | Yes, always | **Weak hint, accepted** (Landing spec ground rule, owner-settled). It links "weather" to "power" without a cause or a direction. Watch in playtest |
| `journal.power` (`pack/data/mars/journal/power.json`) | Yes | **Passes** (no weather) |
| **`failure.power_shortfall` hint 2** (`docs/design/messages/failure-reports.md` row 3): "Storms and nights cut solar. What carries the dome till morning?" | **Yes, if power runs short before the first storm** (nights make that likely) | **LEAK, not in this file's scope**: a hint for a real failure that says storms cut solar, with no `reveals` field. Handoff to the Lead Game Designer in the report: hold the storm sentence until F1 resolves, or use "Nights cut solar." before |
