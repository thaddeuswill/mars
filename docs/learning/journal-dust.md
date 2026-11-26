# Journal Content: Solar Panel Dust (learning spec, shared format)

**Owner:** Lead Instructional Designer · **Status:** Draft 1, 2026-11-26 (BACKLOG #26) · **Science:** SME, [docs/science/dust-review.md](../science/dust-review.md) §8 and addendum §11–12 · **Mechanic:** DESIGN.md "Solar panel dust", balance sheet §2a · **Format:** DOCUMENTATION.md §5.1

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

## 3. `journal.dust.storm_timing`: storm dust settles for days

| Field | Value |
|---|---|
| `id` | `journal.dust.storm_timing` |
| `type` | journal honesty note (simplification) |
| `trigger` | End of the first storm that adds panel dust (**after** F1 has resolved, so it can't give F1 away) |
| `delivery` | Line on the `journal.dust.panels` page |
| `text.ms` | In real storms, dust keeps settling on panels for days after the sky clears. The game adds it all when the storm ends, to keep it simple. |
| `text.hs` | Same as MS |
| `goal_ids` | B1, C1 |
| `instructional_purpose` | Corrects "a storm coats panels instantly" (dust-review §4, §11) |
| `science_claims` | Fallout continues as storms fade (**searched**, dust-review §4: "days to weeks"); the "days" wording is the SME's honest note |
| `fiction_note` | Simplification note; both bands |
| `status` | draft. "Days" is a qualitative claim from a searched source: SME to confirm it can ship as wording without a number (my view: yes, no number is stated) |

## 4. Short honesty notes (misconceptions, dust-review §11)

Each is one line, both bands, on the dust page or its trigger moment. All are **simplification or fiction notes**, never band-gated.

| `id` | Trigger | Text | Misconception it prevents | Claim tags |
|---|---|---|---|---|
| `journal.dust.wind` | First dust-devil cleaning on the planet | "Wind cleaned some real panels, like Spirit's. It never cleaned InSight's." | "Wind always cleans" / "nothing cleans it" | Spirit events, InSight none: **sourced**/**searched** (dust-review §2) |
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
| `type` | journal honesty note (on the F1 result's journal page, after F1 resolves) |
| `trigger` | F1 resolved (never before the lock: CR-F9) |
| `delivery` | Read-more line on the F1 journal page, both bands |
| `text.ms` | In 2018, a storm that covered all of Mars cut the rover Opportunity's power by 97 percent, and it went silent. Your storms cut 60 percent at most. Real storms can be much bigger. |
| `text.ms` (fallback if the 2018 sources aren't opened) | Real dust storms on Mars can cover the whole planet and cut solar power far more than in the game. One in 2018 left the rover Opportunity without enough power. |
| `text.hs` | Same as MS |
| `goal_ids` | C1, A1 |
| `instructional_purpose` | Honest scale: the game's storm is milder than real; links C1 planning to a real mission |
| `science_claims` | −97 % to 21 Wh by 10 June 2018 (**searched**); 60 % max game cut (**invented**, balance §2a). **Removed** from the SME's draft: "in about three weeks" (SME's own reading, **unverified**). **Never claim** what finally ended Opportunity's mission (SME: real value missing) |
| `fiction_note` | Simplification note |
| `status` | draft. Ships the fallback unless the 2018 sources are opened. **Note for the Lead Game Designer:** F1's own `science_claims` says "2018 storm ended Opportunity's mission"; the SME says what ended it is a missing value. Reword it to "left Opportunity without enough power" |

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

**Diagram:** the dust page uses a variant of diagram 9 (prompts-and-reflections §4a) with two labeled paths: dust **in the air** (storm, dims the sky) and dust **on the panel** (builds slowly). No new diagram row needed; the variant is noted for asset U-25.
