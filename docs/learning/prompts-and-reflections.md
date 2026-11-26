# Forecasts, Reflections, Earth Links, and Keeper's Record

**Owner:** Lead Instructional Designer · **Status:** Draft 2, 2026-11-26 (interleaved forecasts, fading, Record triggers that reward skill, flow-feedback wording rules) · **Depends on:** [LEARNING.md](../../LEARNING.md), [story bible](../design/story-bible.md), [science review](../science/accuracy-review.md)

This file specifies the learning content that lives in the game: what each forecast, reflection question, Earth link, and Keeper's Record page must do. The Lead Game Designer writes the final in-game wording in the story bible's voices; the SME checks every science claim; I review the result against the goal and criterion.

Every entry here becomes an entry in the shared feedback format (DOCUMENTATION.md 5.1) when it's built. Fields not listed here (`id`, `delivery`, `motivational_purpose`, `status`) are filled in by the Lead Game Designer and Technical Lead.

**Status of science content:** all sample answers and explanations are **draft, SME check needed** unless marked otherwise. Nothing here may ship as fact without that check.

**SME round-2 review applied** (`docs/science/round2-review.md` §3.1–3.5, 2026-11-26): all keyed answers accurate; F1, F2, F4a, F9 distractors replaced; F4a and F7 triggers changed; F6, F8, F10 explanation notes added; approved Earth-link lines in §3.1; transfer-task answer key in `rubrics.md`. The forecast check (section 1.2) was re-run with the SME's new "threshold vs. curve" step. Distractors and explanations still need review by real middle and high school science teachers (SME §11, item 6).

---

## 1. Forecasts (prediction prompts)

**How forecasts work** (LEARNING.md 3.2, 4.6):
- BELL asks: "Log your forecast." One question, 2–4 choices.
- Each player answers on their own screen. Other players' answers stay hidden until the forecast resolves.
- Issued at least ~1 sol (active preset) before the outcome, so it can wait for a natural pause.
- Expires, unscored, when the outcome becomes visible.
- The result is shown at the next natural pause, first in the queue: right or wrong, plus a 1–2 sentence explanation either way.
- At least 3 forecasts per phase, so the B3 criterion (own accuracy per phase) has enough data.
- HS variant adds a "why" follow-up choice. Only the main question counts toward B3; the "why" counts toward the goal's Keeper's Record page.
- **One answer per player per forecast**, locked on submit; late, expired, and replayed forecasts don't score (LEARNING.md 3.10).
- **Scaffold fading** (LEARNING.md 4.8): Phase 1 questions add a "Check: …" pointer to the Monitor value; Phase 2 only on new topics (F5); Phase 3 none. Pointers return for a player whose accuracy in the phase is below 1 in 3 after 3 forecasts.
- **Result wording** (LEARNING.md 4.5, feedback for flow): a miss reads "The planet did X. Here's why." plus the Monitor line that would have shown it. Never "Wrong!". A hit gets plain acknowledgment.

| ID | Phase / trigger | Question (MS) | Choices (correct first here; shuffle in game) | HS "why" follow-up | Goals | Notes |
|---|---|---|---|---|---|---|
| F1 | P1 / first dust storm warning (story beat 4) | "A dust storm reaches the dome in about 1 sol. What will happen to your solar power?" | It drops · It rises · It stays the same | "Why?" Dust blocks sunlight · Wind turns the panels · The Sun gives off less light during storms | B3, C1 | Pair with BELL's storm forecast. Avoid any claim about damaging wind (DESIGN.md fantasy audit). SME r2: "Cold makes panels stronger" replaced (partly true: cells work slightly better cold) |
| F2 | P1 / first solar event, ~1 sol ahead (story beat 5) | "A solar event is coming. Where will a Keeper get the smallest radiation dose?" | Inside, under thick shielding · Out on open ground · On top of a hill · Next to a running machine | "Why does Mars's surface get more radiation than Earth's?" Thin air and no global magnetic field · Mars is closer to the Sun · Red color soaks up radiation | A4, B3 | "Closer to the Sun" is a deliberate misconception distractor (Mars is about 1.5× farther). SME r2: "Red soil gives off radiation" replaced (the ground does give off some secondary radiation) |
| F3 | P1 / first Regolith Outgasser running (unlocks at Dome stable) | "Your Regolith Outgasser is adding gas to the air. What will the Monitor show for pressure over the next sol?" | Pressure rises slowly · Pressure jumps to Earth's level · Nothing changes | "Why so slowly?" A planet's air is huge compared to one machine · The machine is broken · Gas leaks to space at once | A1, B1, B3 | About +0.10 kPa/sol per Outgasser (balance sheet §2). Monitor milestone "Armstrong line" at 6.3 kPa is a good follow-up journal moment |
| F4a | P1 / first ice block warmed above 0 °C locally while pressure < 5 kPa, after Landing (Lead Game Designer's final trigger). **Scored by outcome check:** "no liquid water at that spot 0.25 sol after the ice changes state" (see 1.2) | "The heater has warmed this ice above 0 °C. The air is very thin. What will it do?" | Disappear into vapor, leaving no water · Melt into a puddle that stays · Stay frozen | "Why?" In thin air, water boils at a low temperature · The heater destroys the water · It's still too cold | A2, B3 | SME r2 trigger and wording. Key is true for sublimation and for instant boiling. "Ice on Mars is not water" replaced (partly true: Mars also has CO2 ice). The A2 misconception moment |
| F4b | P2 / Thaw gate: first ground ice near an extractor melts (pressure already ≥ 5 kPa, about 22 kPa at the Thaw; local temperature below the boiling point, about 62 °C there) | "Last time, warmed ice vanished. Now the air is much thicker. What will warmed ice do?" | Melt into water that stays · Turn to vapor again · Stay frozen | "What changed?" Higher pressure raised water's boiling point · The ice is different · The machine is stronger | A2, B3 | Contrast with F4a (example and non-example). The Monitor shows water's boiling point at the current pressure |
| F5 | P2 / first greenhouse emitter switched on | "You start the greenhouse emitter. What will the surface temperature do over the next few sols?" | Rise slowly · Drop · Stay the same | "Why?" Its gases trap heat that would escape to space · It blocks sunlight · It burns fuel that heats the air directly | A3, B3 | Emitter makes factory-made greenhouse gases, not CO2 (SME B4) |
| F6 | P2 / player count of active emitters and extractors passes a threshold after first fauna (Hydrosphere period) | "You add three more extractors. What will happen to thawling activity near your machines?" | It goes up · It goes down · No change | "What kind of loop is this?" Reinforcing: more machines bring more fauna, which damage machines, so you build more · Balancing · Not a loop | B2, B5, B3 | Fiction: thawlings. Explanation must say the behavior is invented, the loop type is real. SME r2: the loop is reinforcing **through the player's rebuilding**: "This is a reinforcing loop when you answer damage by building more machines." (Without rebuilding, damaged machines drop out and the loop balances.) |
| F7 | P2 / first solar event once pressure ≥ 5 kPa (SME r2: about 8× today's air overhead; the scheduler guarantees one in Phase 2) | "The air is much thicker than at your first solar event. What will this one do at the surface?" | Less than last time · More than last time · The same | "Why?" More air means more matter to absorb radiation · Thicker air reflects the Sun · Fauna block it | A4, B3 | Spaced retrieval of F2. Depends on DESIGN.md "spikes weaken as the atmosphere thickens" |
| F8 | P3 / first large flora seeding | "You seed a large field of flora. What will happen to O2 and CO2?" | O2 rises, CO2 falls · Both rise · Both fall | "Why?" Plants take in CO2 and give off O2 · Plants make CO2 at night only · Soil releases O2 | A5, B3 | SME r2: "Plants make CO2 at night only" is a documented misconception, a good distractor. Explanation adds: "Plants give off some CO2 all day and night. While they grow in light, they take in more than they give off." |
| F9 | P3 / O2 rising toward the top of its band | "O2 keeps rising. What risk is coming?" | Fires start more easily · The air gets too thin · Plants stop growing | "Why?" More O2 makes things burn more easily · O2 is poisonous at any level · More O2 smothers flames | A5, B4, B3 | Fire-safety band 19–23%; breathable window at 75 kPa is ~21.5–23% (LEARNING.md 4.7). Consider firing F9 early, when O2 first enters the window. SME r2: "O2 makes the air heavier" replaced (partly true) |
| F10 | P3 / player turns emitters down (or upkeep fails) during the equilibrium push | "Your emitters are turned down to save power. What will temperature do over the next sols?" | Fall slowly · Rise · Stay the same | "What's the trade-off?" Less power used, but the planet cools and water may freeze · No trade-off · Less fauna, nothing else | B4, A3, B3 | SME r2 HS read more: "Real factory gases would break down slowly in Mars's strong sunlight, so they would need to be made all the time." |

Count per phase: P1 4 (F1, F2, F3, F4a), P2 4 (F4b, F5, F6, F7), P3 3 (F8, F9, F10). With interleaved forecasts (1.1): P1 4, P2 5, P3 5.

### 1.2 Forecast check, round 2 (manual run of `content-rules.md` §1.1, with the new threshold-vs-curve step)

Step added (SME lesson): for every threshold a forecast rests on, check **when the balance curve passes it**. A threshold the curve passes in the first sol hides that regime and any forecast built on it.

| ID | Rests on | Curve check (balance sheet §2–4) | Result |
|---|---|---|---|
| F1 | Storms cut solar output | No threshold | **OK** |
| F2 | Thin air, no field; P1 radiation | Pressure 0.6–5 kPa through P1 | **OK** |
| F3 | Outgasser +0.10 kPa/sol (×4 in class) | "Rises slowly" holds in both presets | **OK** |
| F4a | Old: pressure < 0.611 kPa | Curve passes 0.611 kPa on about sol 1 | **Was broken; fixed** by the SME trigger (< 5 kPa). **Catch:** ice warmed only to between 0 °C and the boiling point (2–33 °C across 0.7–5 kPa) makes a short-lived puddle first. Resolved with the Lead Game Designer's three result texts plus a fixed **outcome check: no liquid water 0.25 sol after the state change**, which is true in all three regimes (sublimation, instant boiling, fast evaporation) **only if** the invented puddle-evaporation time below 5 kPa is under 0.25 sol (balance sheet; Lead Game Designer). CR-F1 must test that number |
| F4b | Water stays ≥ 5 kPa, 0 °C to boiling point | Thaw at ~22 kPa, boiling point ~62 °C | **OK**, if extractor heat stays below ~62 °C locally (condition added) |
| F5 | Emitters raise temperature | No early threshold | **OK** |
| F6 | Fauna signature; rebuilding | Reinforcing only with rebuilding | **OK** with the new explanation |
| F7 | Air absorbs radiation | Old trigger ("doubled") fired at ~1.2 kPa: weak difference | **Fixed**: ≥ 5 kPa |
| F8 | Flora: O2 up, CO2 down | Decay adds CO2, but net growth wins while flora grows | **OK** with the respiration note |
| F9 | Fire above 23 % O2 | Window ~21.3–23 % at 75 kPa | **OK**; fire-band edge at reduced pressure still with the SME (LEARNING.md 4.7.1, concern 6) |
| F10 | Emitters down → cooling | No early threshold | **OK** |
| FI-1 | Storm cuts solar power → warming slows | Depends on emitter power source | **Open** (Lead Game Designer) |
| FI-2 | Boiling point falls with pressure | At ~75 kPa, a 1.5 kPa dip lowers the boiling point by well under 1 °C (calc from the SME's Antoine values: roughly 0.5 °C) | **OK** ("drops a little"); SME to confirm |
| FI-3 | Thicker air absorbs more | Phase 3 ≥ 30 kPa | **OK** |
| FI-4 | Storm → vats slow → CO2 up | Balance sheet §4 drift table | **OK**, if storms in Phase 3 get a ≥ 1 sol warning |

### 1.3 Forecast check, round 3 (threshold vs. curve, against DESIGN.md's table and the balance sheet as of round 3)

Run on the current documents. DESIGN.md now has the SME's boiling-point ice rule and its own threshold-vs-curve table (DESIGN.md §2), so the A2 condition below is met on paper. Re-run after the Lead Game Designer's round-3 changes landed (DESIGN.md §2 table, balance sheet, messages, ui-specs): Phase 1 gas-mix source, forecast card first on Monitor open, puddle-drying time, drift events after the first flora, storm warning ≥ 1 sol in every phase, emitters on the shared solar grid.

| ID | Threshold or number | Current value | Result |
|---|---|---|---|
| F4a | Puddle-drying time below 5 kPa must be < 0.25 sol (outcome check) | DESIGN.md: 0.1 sol, invented, with 0.25 sol as a hard limit | **Pass.** SME r3 accepts 0.02–0.25 sol (invented), ideally growing with pressure, on two conditions, both adopted: the outcome check counts **frozen water as "no liquid"**, and puddles below 5 kPa are thin films; no game text states a drying time. CR-F1 keeps testing it |
| F4b | Thaw at ~22 kPa, boiling point ~62 °C | DESIGN.md table | **Pass** |
| F7 | First solar event at ≥ 5 kPa | Phase 2 gate ~sol 25; scheduler guarantees one in Phase 2 | **Pass** |
| F9 | Window opens at 69.6 kPa, 75 kPa ~25 sols into Phase 3 | DESIGN.md table | **Pass**; the "not thick enough yet" Air window state lasts only ~3 sols (fine: it's a transition, not a forecast) |
| F1, FI-1 | Storm cuts solar | Per-phase storm cap 1.0 / 0.8 / 0.5 / 0.25 scales the solar cut; every storm warned ≥ 1 sol ahead; emitters draw from the shared solar grid (DESIGN.md) | **Pass** in Phases 1–2 (cap 1.0, 0.8). FI-1's dependency (what powers emitters) is now answered |
| **FI-4** | Storm during a Phase 3 hold → vats slow → CO2 rises | Phase 3 cap 0.5 (solar cut ~30 %); 0.25 at Equilibrium (~15 %). Storms during holds are warned ≥ 1 sol ahead (landed) | **Conditional pass.** During holds the cap is 0.5, so the drift-table CO2 rise (+0.15 kPa/sol at full cut) shrinks to roughly half but should still be a rise. Rule: issue FI-4 only while the storm cap is ≥ 0.5; CR-F1 simulates it. If it fails, drop FI-4 from the pool (FI-2 and FI-3 keep Phase 3 at 5 forecasts) |
| FI-2 | Pressure dip → boiling point drops a little | **Round 3 SME finding:** the polar-winter dip can't happen at equilibrium | **Was broken (premise); fixed** by a passing weather low (pending the Lead Game Designer). Lesson: run the threshold-vs-curve check on **events** too, not only on thresholds |
| F3, F5, F8 | Lock | Now "1 sol after Start"; opening the Monitor shows the forecast card first (`forecast.look_first`, Answer or Skip) | **Pass.** Checking data no longer costs a forecast. A "Skip" logs as unanswered, never wrong |
| A1 | Phase 1 gas-mix source | Landing step 3 points at a Monitor "Air mix" fact line (about 95 % CO2, almost no O2); Dome stable journal line; sourced (SME Part A) | **Pass.** A1's four variables all have a Phase 1 teaching element |

**Alignment after round 3 (on paper):** every goal is still taught, practiced, given feedback, and measured. Only FI-4 is conditional (storm cap). Also noted: the Phase 1 "Check:" pointers hide once a player's own accuracy is ≥ 1 in 3 after 3 forecasts, a slightly faster fade than my §4.8 default (pointer on every Phase 1 forecast). **Accept**: it fades on the player's own evidence, which is the principle. F4a's pointer ("Where does water boil here?") points at the data without stating the answer (CR-H2), and the `scaffold` flag lets the B3 analysis separate pointed answers.

### 1.4 Forecast check after the panel dust mechanic (BACKLOG #26; threshold vs. curve, including events)

Inputs: DESIGN.md "Solar panel dust", balance sheet §2a (output by stage 100 / 92 / 82 / 70 / 60 %; with a storm, output = stage factor × (1 − 0.6 × storm cap); storm dust added **at storm end**; caps 1.0 / 0.8 / 0.5 / 0.25), dust-review.md. The worst case for a "power drops" key is a player whose panels sit at the 60 % floor before the storm and who **cleans them during the storm** (to 100 %).

| ID | Phase (cap) | Storm factor | No player action | Worst case (stage 4 before, cleaned during) | Result |
|---|---|---|---|---|---|
| F1 | 1 (1.0) | 0.40 | Drops at every stage | 60 % → 40 %: drops | **Pass** |
| FI-1 | 2 (0.8) | 0.52 | Drops | 60 % → 52 %: drops, margin 8 points | **Pass, narrow.** Also: with batteries on the shared grid, emitter output may not fall during a short storm even though solar falls |
| FI-4 | 3 (0.5) | 0.70 | Drops (CO2 rises) | 60 % → 70 %: **power rises**, vats speed up, CO2 may fall | **Fails on net outcome** for a player who cleans mid-storm |

**Fix (all three): resolve these forecasts by the storm's own cause line, not by the net change.** The Monitor already splits power into "Storm: less sunlight −n %" and "Panel dust −n %" (B1), and every contribution has a source ID. So F1, FI-1, and FI-4 resolve on the storm-attributed contribution: solar lost to the storm (F1), emitter output lost to the storm (FI-1), CO2 added because vats slowed in the storm (FI-4). That contribution has the keyed sign whatever the player does with a brush or a battery. It is also what the question asks ("while the storm lasts", "what the storm does"). New content rule **CR-F10** (content-rules.md). Explanations stay as written; FI-4's result may add: "If you cleaned your panels during the storm, your power went up, but the storm itself still cut it."

**Other dust checks**

| Check | Finding | Result |
|---|---|---|
| F1 lock and reveals | Storm dust is added at storm **end**, after F1 locks at onset: nothing reveals the answer early. Pre-storm drift shows on the separate "Panel dust" line. **But** the Lead Game Designer's F1 `reveals` list (messages/README.md) names the Monitor line **"Dust on panels"**. The storm's line is "Storm: less sunlight". Holding the panel-dust line until F1 locks would hide it from its own first appearance (stage 1, about sol 3) until the first storm, which breaks its progressive disclosure | **Fix the reveals entry** to "Storm: less sunlight" (handoff) |
| F1 science claim | F1's `science_claims` says the 2018 storm "ended Opportunity's mission"; the SME says what ended it is a missing value | Reword to "left Opportunity without enough power" (handoff; journal-dust.md §6) |
| Dust journal notes vs. forecasts | `journal.dust.storm_timing` fires at the first storm's **end** (after F1 resolves); `journal.dust.panels` fires at stage 1 and says nothing about storms | **Pass** (CR-F9) |
| Events (dust devil 1 %/sol, Phase 3 rain to stage 1) | Neither changes a keyed answer: a dust devil can only raise output between storms; rain leaves panels at 92 %, and the storm still cuts on top | **Pass** |
| C1 evidence opportunity | First storm about sol 14 (balance §2); one stage per 3 sols, so uncleaned panels are at stage ≥ 2 by then; a warning window of ≥ 1 sol gives time to clean | **Pass**: the C1 dust indicator can actually occur |
| Phase 1 curve | Assumes cleaning about every 6 sols (≈ 91 % average); the classroom Phase 1 check is already slightly slow (2.7 h vs 2.4 h) | Lead Game Designer's to measure in the prototype; no forecast rests on it |

**Alignment after the dust mechanic (on paper):** C1, B1, B5, C2, and D2 are still taught, practiced, given feedback, and measured, and the mechanic adds teaching to each (LEARNING.md alignment table). It adds a new C1 indicator and a new B1 "What caused it?" item. No goal loses anything. Conditional on CR-F10 (FI-4), and on the reveals fix (F1).

Alignment effect: A2's in-game teaching and measures (F4a, F4b, FI-2) stand **only if DESIGN.md adopts the SME's boiling-point ice rule** (handoff to the Lead Game Designer). Until then the A2 row in LEARNING.md's alignment table is "covered, conditional".

**Stage 3 vertical slice:** F1 and F4a (and F2 if the radiation spike is in the slice).

**Final text and lock points** live in `docs/design/messages/phase-1.md` to `phase-3.md` (Lead Game Designer, round 2). This table is the learning spec; where wording differs, the messages files win and this table follows. **Lock points** (a forecast closes before any message or screen that gives the answer away; a lock after an unanswered issue logs `expired`, never wrong):

| ID | Locks at | Alignment check |
|---|---|---|
| F1 | Storm onset | OK: ~1 sol to answer |
| F2, F7 | `log.solar.shelter`, T−0.25 sol | OK: ~0.75 sol to answer; the shelter advice gives the answer away, so it must lock first |
| F3, F5, F8 | The player opens the matching Monitor page, or 1 sol | OK, **with one request**: if a player with an open, unanswered forecast opens the Monitor, show the forecast card first ("Log your forecast before you look"), instead of silently expiring it. Otherwise checking the data (C3) costs the player their B3 evidence |
| F4a, F4b | The ice changes state (held up to 0.25 sol) | OK |
| F6 | The new machine has run 0.25 sol | OK |
| F9 | O2 reaches 23 %, or 0.5 sol | Can live with it. Below rule 7's ~1 sol lead; F9 is conceptual (scored by rule), so the short window only risks more expiries. Watch the expiry rate in Stage 3 |
| F10 | Temperature trend turns down, or 0.5 sol | Same as F9 |

### 1.1 Interleaved forecasts (FI): draft stems, science pending the SME

Design: LEARNING.md 2.3. Each brings back an earlier idea in a new situation, with **no topic label**. Always followed by the full explanation, right or wrong (4.8). Counts toward the B3 tally of the phase it's asked in. A correct FI also fills the source goal's Keeper's Record page. **Every stem, choice, and key below is pending the SME**; don't build from it yet.

| ID | Phase / trigger | Stem (MS, draft) | Intended key (pending SME) | Distractors (draft) | Source idea | Goals | Depends on |
|---|---|---|---|---|---|---|---|
| FI-1 | P2 / first dust storm warning after the first emitter runs | "A dust storm arrives in about a sol. Your emitters run on solar power. What happens to the warming they add while the storm lasts?" (SME r3 reword: real dust storms heat the air while cooling the ground, so the old stem was ambiguous) | Warming slows | Warming speeds up · No change | Storms cut solar power (F1) | C1, A3, B1, B3 | Emitters (or their power) are affected by solar loss. **If emitters don't draw solar power, swap the stem to processors and pressure** (Lead Game Designer) |
| FI-2 | P3 / first **passing low-pressure weather** warning (SME r3 option b) | "A low-pressure weather system is passing. Air pressure will drop a little for a sol. What happens to the temperature water boils at?" | It drops a little (about 0.5 °C at 75 kPa; SME calc) | It rises · It doesn't change | Pressure sets the boiling point (F4a, F4b) | A2, B3 | SME r3: a polar-winter dip is **wrong** at equilibrium (CO2 ≤ 0.5 kPa; nitrogen and O2 don't freeze out). Depends on the Lead Game Designer replacing the equilibrium "polar winter" drift event with a weather low, warned ≥ 1 sol ahead; the size of the dip is invented (real weather lows: a few kPa, **unverified**). B2 dropped from this item (a passing low isn't a loop). Fallback if no weather low is built: fire FI-2 early in Phase 3 on a polar-frost dip while CO2 is still high |
| FI-3 | P3 / first solar event in Phase 3 (SME r3: accurate; the explanation may say "thick air now blocks most of it" but never "as safe as Earth") | "A solar event is coming. The air is much thicker than at your first one. Compared with then, how much radiation reaches the ground?" | Much less | More · The same | Air absorbs radiation (F2, F7) | A4, B3 | Spikes weaken as the atmosphere thickens (DESIGN.md); SME on whether helmet-off surface exposure is safe enough to say anything more |
| FI-4 | P3 / dust storm warning during a hold, while the storm cap is ≥ 0.5 (explanation, SME r3: "Less light, less CO2 taken in. The algae still breathe out CO2.") | "A storm will cut solar power for about a sol. Your algae vats run on solar. What will CO2 do?" | Rise | Fall · Stay the same | Storms cut solar power (F1) | C1, A5, B1, B3 | Balance sheet §4: storm → vats slow → CO2 +0.15 kPa/sol; needs a ≥ 1 sol storm warning in Phase 3 (Lead Game Designer) |

Phase 2: FI-1 always. Phase 3: pool of FI-2, FI-3, FI-4; each player gets the first two whose events fire. Counts per phase become P1 4, P2 5, P3 5.

---

## 2. Keeper's Report reflections

One question per report. The player's answer is part of the report sent to Earth (story bible 6.4). Answers are individual evidence, scored with `rubrics.md`. In classroom mode the teacher expects an answer; the game never blocks progress for it. Deferral rules: LEARNING.md 4.6.

| Report | Gate | MS question | HS question | Goals | Rubric |
|---|---|---|---|---|---|
| 1 | Phase 2 begins | "What almost failed in the dome, and what would you do differently next time?" Sentence starter (shown): "The ___ almost failed because ___. Next time I would ___." | Same, plus: "What did the Monitor show that helped you find the cause?" | C2, C1, C3 | Not rubric-scored; C2 and C3 evidence for the Keeper's Record and teacher check-in |
| 2 | Phase 3 begins | "Was waking the thawlings worth it? Give one reason from what you saw." Starter: "I think ___ because I saw ___." | "Was waking the thawlings worth it? Give your reason, and the strongest reason someone could give against you." | D1, B5, B2 | D1, B5 |
| 3 | Equilibrium | "To hold the balance, what did you have to give up? Was it worth it?" | "A colonist claims: 'Once the planet is balanced, it will stay balanced.' Is that claim right? Use what happened on your planet as evidence." | B4, B2 (HS: HS-LS2-6 claim evaluation) | B4 is scored by post-test; Report 3 is formative |
| Final | Beacon lit (story bible 6.5) | "The ships are coming. What should the colonists know before they land?" | Same, plus: "What does your planet teach about Earth?" | D1, D2 | D1, D2 |

**Changes from the story bible's draft:** "Was waking them worth it?" now asks for a reason from what the player saw, so it can be scored as argument from evidence (D1 rubric). The HS versions ask for a counter-reason or a claim evaluation (NGSS: argument from evidence; HS-LS2-6).

**Sentence starters fade (MS):** shown on Reports 1 and 2; on Report 3 and the Final question they sit behind a "Need a start?" button (logged). HS gets no starters.

**A report answer fills a Keeper's Record page only if it's at least one sentence (≥ 8 words)** (LEARNING.md 4.9). Quality is the teacher's call.

**After Action Review:** each report also shows a "Your shift" block: own builds, claims and fixes, forecasts right and missed, with missed topics linked to their blank Record pages, in positive terms (LEARNING.md 4.3).

**Paper and oral alternatives:** if `reflections.csv` is off, the same questions are on the paper worksheet (educator guide §6). A teacher may take an oral answer and score it with the same rubric.

**Group play:** every player gets their own copy of each report question. A player who was absent when the gate fired finds it waiting ("Since last shift: report not sent").

---

## 3. Earth links (D2)

Every gate journal entry ends with an "On Earth" line (story bible 6.3). Mensah's reply to each Keeper's Report is the longer Earth link. Each link must name **one real Earth mechanism that matches what the player just did on Mars**, so D2 is about transfer, not trivia.

| Where | Mars experience | Earth link must teach | Goals / NGSS (6.1) | SME check needed for |
|---|---|---|---|---|
| Gate: Dome stable | Keeping air in, life support | Earth's thick air protects us for free; Mars's air pressure is less than 1% of Earth's | A1, D2 | Ratio wording |
| Report 1 reply (Phase 2 begins) | Pressure rising; water still can't stay liquid | On Earth, thick air keeps puddles and oceans liquid; water's boiling point drops on high mountains | A1, A2, D2 | Mountain boiling example |
| Phase 1: first ice turns to vapor (F4a) | Ice turns to vapor | Same physics on Earth: freeze-dried food and frost that vanishes without melting (sublimation) | A2, D2 | Examples |
| Gate: Thaw (F4b) | Water now stays; ground collapses near warm machines | Thick air keeps water liquid on Earth; thawing permafrost damages buildings and roads today (real side effect) | A2, B5, D2 | Permafrost claim |
| Gate: first emitter (F5) | Adding greenhouse gas warms the planet | Earth's greenhouse gases (CO2, methane, water vapor) trap heat; the greenhouse effect is **not** the ozone hole | A3, D2; MS-ESS3-5, HS-ESS2-4 | All |
| **Report 2 reply (Phase 3 begins)** and class debrief period 14 | Warming had side effects; reinforcing loop with fauna | **Evidence of Earth's warming over the past century**: CO2 and temperature records, with human activities as the main cause; a real reinforcing loop (thawing permafrost releasing stored carbon, or ice–albedo); Earth's emissions as a side effect, not a goal | A3, B2, B5, D2; MS-ESS3-5, HS-ESS2-4, HS-ESS2-2 | All. The classroom debrief needs a real CO2 and temperature graph from a named source (MS-ESS3-5 condition) |
| Gate: first flora | Plants take in CO2 and give off O2 | Earth's oxygen comes from photosynthesis on land and in the ocean | A5, D2 | Ocean share, if a number is used |
| Report 3 reply (Equilibrium) | Holding a band; balancing loops | Earth's carbon cycle: air, plants, soil, ocean; balancing loops that keep it steady, and how human activity is shifting it | A5, B2, D2; HS-ESS2-6, HS-ESS3-6 | All |
| Ending, Mensah's last line | Building a planet by hand | What Earth can learn: we already change a planet; we can do it with a plan | D2, D1 | Tone check |

### 3.1 Approved MS lines (SME round 2, `round2-review.md` §3.2)

The Lead Game Designer uses these as the content of each link (wording can be tuned for voice; facts can't change without the SME).

| Where | Approved MS line | Status |
|---|---|---|
| Dome stable | "Mars's air pushes with less than 1 % of the force of Earth's air. Earth's thick air protects us for free." | Approved (calc from sourced values) |
| Report 1 reply | "On high mountains, water boils below 100 °C. On top of Everest it boils at about 70 °C." | Approved (calc) |
| F4a | "Ice can turn straight to vapor on Earth too. Old ice cubes shrink in the freezer, and freeze-dried food is made this way." | Approved; textbook, source not opened. Don't use "frost that vanishes" unqualified; label dry ice as CO2 if used |
| Thaw (F4b) | "In the Arctic, frozen ground is thawing. Roads crack and houses tilt as the ice underneath melts." | Approved (sourced) |
| First emitter (F5) | "Earth's air has greenhouse gases too: carbon dioxide, methane, and water vapor. They trap heat. That's different from the ozone hole." | Approved; HS nuance on water vapor as a feedback |
| Report 2 reply | "Earth has warmed by about 1.1–1.2 °C since the 1800s, mostly from burning coal, oil, and gas. Thawing permafrost adds more carbon: a reinforcing loop." | **Approved pending source**: the SME lists the warming figure and permafrost carbon as not yet opened; can't ship as fact until sourced |
| First flora | "About half of the oxygen made on Earth comes from tiny ocean plants and algae." | Approved (sourced, NOAA "roughly half"); avoid "Earth's lungs" |
| Report 3 reply | "Carbon moves between air, plants, soil, ocean, and rock. Balancing loops kept it steady for thousands of years. We're now adding carbon faster than they can remove it." | Approved; source not opened; no reservoir numbers at MS |

**Rules for Earth links**
- Fiction stays on the Mars side. An Earth link never implies the thawlings are real, and never pairs them with real Earth organisms without a clear "this part is real" label (accuracy-review.md Part D).
- Plain causes, no blame: say Earth's warming is mainly caused by human activities (the scientific consensus the SME's sources support), framed as a side effect, which is the B5 lesson. Suggested edit to the story bible sample: "We did it by accident" → "We did it without meaning to, as a side effect of burning fuel." (Lead Game Designer's call; SME to check.)
- One Earth idea per line. "Read more" (HS) can add a second example or a number.

---

## 4. Keeper's Record pages

Header: *"What the colonists will need to know."* One page per goal. A page fills from the player's **own** evidence only (LEARNING.md 3.0). The "Cairn team" section lists group milestones separately.

| Goal | Page title (colony language, draft) | Fills when the player… (any one) |
|---|---|---|
| A1 | How Mars is different from home | Gets F3 right, or a "What caused it?" check on a Phase 1 hazard right |
| A2 | Why water won't stay liquid here, and what would change that | Gets F4a, F4b, or FI-2 right (HS: two of them, with the "why") |
| A3 | How to warm a planet | Gets F5 right (HS: and its "why") |
| A4 | Staying safe from the Sun's storms | Gets F2, F7, or FI-3 right |
| A5 | Plants, air, and keeping the balance | Gets F8 or F9 right |
| B1 | What changes what | Gets 3 "What caused it?" checks right **on the first attempt**. The page says so in plain words ("Fills when you name the cause on your first try, three times"), so the rule is fair and legible; the check's own sub-line, "Pick the most likely cause. No penalty for a wrong pick.", stays true |
| B2 | Loops that grow and loops that settle | Gets F6's "why" right (HS), or answers Report 2 (MS) |
| B3 | Reading the planet ahead | Own forecast accuracy in the current phase beats the previous phase |
| B4 | What it costs to hold the balance | Gets F10 right and answers Report 3 |
| B5 | When a fix causes a new problem | Answers Report 2 |
| C1 | Getting through a storm | Gets F1 right, **and** either placed or refueled life support before the first storm, **or** cleaned a dusty panel (stage ≥ 2) while a storm warning was active (LEARNING.md 5.5: cleaning clean panels, or with no storm coming, doesn't count) |
| C2 | Fixing it so it stays fixed | Own repair (claimed and marked fixed on the Shift Board) of a failure type, with no repeat from the same cause that phase |
| C3 | Checking the data first | Opened the Monitor (≥ ~1 s) before at least half of own major builds (minimum 3 builds) |
| D1 | Sharing a planet | Answers Report 2 or the final question |
| D2 | What Mars teaches about Earth | Answers the final question, or an Earth-link question in a report (≥ 8 words). *Opening "On Earth" pages no longer counts: it rewarded activity, not skill (LEARNING.md 4.9)* |

A filled page shows the player's own words where there are some (report answers), which supports the "writing for the colonists" frame. Pages for answering a report fill on *answering*; quality is scored by the teacher, not the game.

**Framing response (story bible handoff):** accepted. "What the colonists will need to know" turns the record into something written for others, which fits the expecting-to-teach effect (LEARNING.md section 2; citation unverified) and keeps player language. The system and team term stays "Colony Record"; the in-game title is "Keeper's Record".

---

## 4a. Journal diagram panels (asset U-25 count, for the Lead Artist)

Style guide §13.5: each "what happened" page has one 120 × 72 diagram panel above ≤ 60 words. One diagram per **mechanism**, not per entry: entries and forecast results that teach the same mechanism share a diagram (with a state variant where needed). **Count: 11 diagrams** (7 essential, 4 simple).

| # | Diagram (mechanism) | Used by | Goals | Fidelity plan |
|---|---|---|---|---|
| 1 | Water and pressure: boiling point vs. pressure, with "you are here" (two states: thin air, thick air) | Thaw gate entry; F4a, F4b, FI-2 results | A2 | Essential |
| 2 | Greenhouse: sunlight in, heat trapped by factory gases | First-emitter entry; F5, F10 results | A3 | Essential |
| 3 | Gas exchange: plant takes in CO2, gives off O2; small respiration arrow | Phase 3 begins / first flora entry; F8 result | A5 | Essential |
| 4 | Air window: pressure × O2 with the breathable, too-little-oxygen, and fire zones | Breathable gate entry; F9 result | A5, B4 | Essential |
| 5 | Fauna loop: machines → thawlings → damage → rebuilding (reinforcing), with the "without rebuilding" branch dashed | Hydrosphere entry; F6 result | B2, B5 | Essential |
| 6 | Pressure ladder: Mars start, triple point, 5 kPa, Armstrong line, Everest, 75 kPa, Earth | Phase 2 begins entry | A1, B1 | Essential |
| 7 | Holding the band: several values with their bands and one balancing loop | Equilibrium entry | B4, B2 | Essential |
| 8 | Mars vs. Earth air: two columns, pressure and makeup | Dome stable entry | A1 | Simple |
| 9 | Storm and solar power: dust between Sun and panel | F1, FI-1, FI-4 results | C1, B1 | Simple |
| 10 | Radiation column: less air above → more dose; shielding | F2, F7, FI-3 results | A4 | Simple |
| 11 | One machine vs. a whole planet's air (scale) | F3 result | A1, B1 | Simple |

Science content is SME-checked before drawing (labels follow the forecast and journal text).

## 5. Reading level and grade-band rules

| Rule | Detail |
|---|---|
| Main text level | Grade 6–7 for all main text, both bands. Short sentences, one idea per sentence, common words; a science word is defined the first time it appears |
| Check | Every text gets a readability-formula check (e.g. Flesch–Kincaid grade) before ID review, plus a read-aloud check with playtesters. A formula is a screen, not proof |
| Length budgets | As in story bible 6.1 (log line ≤ 20 words, failure report ≤ 35, journal ≤ 80 + read more, message ≤ 60 + read more). Forecast question ≤ 25 words; each choice ≤ 10 words; result explanation ≤ 40 words |
| Reading-time planning rate | Plan at **100 words per minute** for students in this age range. A deliberately conservative design assumption, now **confirmed as conservative** by the SME: grade 6 oral reading fluency median 146 words correct per minute in spring, 10th percentile 91 (Hasbrouck & Tindal, 2017; round2-review §7). At 100 wpm, the story bible's ~780 words of essential classroom messages take about 8 minutes, inside its 15-minute budget |
| MS band | Main text only; one "On Earth" line; sentence starters on reflection questions; forecasts without the "why" follow-up |
| HS band | Same main text; "read more" adds numbers and a second Earth example; "why" follow-ups on forecasts; counter-argument or claim-evaluation reflection questions; Monitor shows carbon per reservoir (if built) |
| Never band-gated | Safety warnings, fiction notes, and the core explanation of each forecast result appear in both bands. Fading (LEARNING.md 4.8) can move a *correct* answer's explanation behind one click in later phases, in both bands alike; a wrong answer always shows it |
| Accessibility | Nothing time-limited; everything replayable from the archive; key information also shown visually |
