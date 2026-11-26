# Learning Content Rules (for automated checks)

**Owner:** Lead Instructional Designer · **Status:** Draft 1, 2026-11-26 · **For:** the Technical Lead, who turns each rule into a check in the regression gate (PROCESS.md §3) · **Depends on:** [LEARNING.md](../../LEARNING.md), [prompts-and-reflections.md](prompts-and-reflections.md), [DESIGN.md](../../DESIGN.md), the balance sheet

**Why.** "Content rules belong in tests" (PROCESS.md §3). We already had a physics change (the ice-to-water rule) quietly break a forecast (F4 → split into F4a and F4b; TEAM.md Learning Log). A human re-reading every forecast after every change won't scale; a check that runs on every build will.

**How each rule is written.** Every rule has an id, what must be true, what the check reads, and how to prove the check can fail (PROCESS.md: "prove a new check can fail"). Checks print what they read, not just pass or fail.

Feasibility of each check is the Technical Lead's call; rules they can't automate stay on the manual review list (section 5).

---

## 1. Forecast rules

| ID | Rule | What the check reads | Prove it can fail |
|---|---|---|---|
| **CR-F1** | **Every forecast's keyed answer matches the current physics.** Run the forecast's situation in the simulation from its trigger state for the forecast's horizon, with no player action (or with the action the question states), and compare the outcome with the keyed choice | Forecast JSON (`trigger`, `horizon_sols`, `keyed_choice`, `outcome_check`) + Index simulation + gate and physics data | Change the ice-to-water threshold in a test datapack so F4a's ice melts instead of sublimating: CR-F1 must fail on F4a and print the simulated outcome |
| CR-F2 | Every forecast has exactly one keyed answer, and the keyed answer is the only choice whose `outcome_check` passes | Forecast JSON | Key two choices on a copy of F1 |
| CR-F3 | Every forecast's trigger can fire before its outcome is visible by at least the minimum lead (≥ ~1 sol of the active preset; LEARNING.md 4.6 rule 7), in **both** standard and classroom presets | Trigger and outcome timing from the simulation under each preset | Shorten F1's warning to 0.2 sol |
| CR-F4 | Every forecast is reachable: some solver run reaches its trigger (PROCESS.md: "every gate and story beat reachable") | Solver playthrough log | Put F5's trigger behind an unreachable gate |
| CR-F5 | Each gameplay phase issues at least 3 forecasts in every solver run (B3 needs 3 per phase) | Solver playthrough log, per phase | Remove F2 and F3 from the pool |
| CR-F6 | Every forecast and FI item has `goal_ids`, NGSS codes resolve through `ngss_map.json`, and both `text.ms` and `text.hs` exist | Forecast JSON, NGSS map, language files | Delete one `text.hs` key |
| CR-F7 | Interleaved forecasts (FI) never name their source idea or phase in the question text (no "remember", "like the ice", "Phase 1") | Language files, a small banned-phrase list | Add "Remember the ice?" to FI-2 |
| CR-F8 | Length budgets: question ≤ 25 words, each choice ≤ 10, result explanation ≤ 40 (prompts-and-reflections §5) | Language files | Pad an explanation to 41 words |

### 1.1 Forecast-check list: runs whenever a gate or physics rule changes

Trigger: any change to gate data (`data/mars/gates/`), Index contribution or physics data (ice-to-water rule, breathable condition, fire band, polar frost, storm effects, radiation weakening), or the balance-sheet values those read.

1. Run CR-F1 to CR-F5 on **every** forecast (F1–F10, FI-1 to FI-4), in standard and classroom presets, group sizes 1 and 5.
2. **Threshold vs. curve** (SME round-2 lesson): for every physical threshold a forecast or story beat rests on (triple point, 5 kPa, boiling point at current pressure, −20 °C, 23 % O2), find the sol when the solver's balance curve first passes it. If a forecast's regime exists for less than ~2 sols of play, or the curve passes the threshold before the forecast can be issued, the check fails and prints the threshold, the sol it was passed, and the forecasts resting on it. (This is what broke F4a twice.)
3. Diff each forecast's simulated outcome against the last passing run, and print every forecast whose outcome **changed**, even if it still matches the key (a drift the designers should see).
4. Re-run the HS "why" follow-ups' `outcome_check` where one exists.
5. Re-check story beats that state a physical fact against the same gate data (TEAM.md Learning Log: a story beat once contradicted the physics).
6. Re-check Keeper's Record triggers that point at a forecast id (a renamed or split forecast must not orphan a page).
7. If any forecast fails: the build is red, and a handoff goes to the Lead Instructional Designer (content) and the SME (science), never a silent key change.

**Last manual run:** round 2, 2026-11-26, results in `prompts-and-reflections.md` §1.2.

## 2. Hint and clue rules

| ID | Rule | What the check reads | Prove it can fail |
|---|---|---|---|
| **CR-H1** | **A failure report, hint, or Hana's tip appears only for a failure that really exists** on that planet at that moment (the cause it names is active in the simulation) | Failure events + simulation state at message time | Fire a "seal breach" report in a test world with no breach |
| **CR-H2** | **A hint never states the answer**: it doesn't name the exact block, recipe, or setting that fixes it (banned-token list per failure type, written by the Lead Game Designer) | Language files + per-type banned tokens | Write "Place a seal block at x" into a hint |
| CR-H3 | The cause a failure report names is the simulation's actual largest cause (the same one the Monitor cause breakdown lists first) | Failure event cause vs. Monitor cause list | Make the report name a minor cause |
| CR-H4 | Hint levels rise only after a repeat of the same cause (adaptive feedback); the outcome form appears only after the viewing player's own fix held (LEARNING.md 4.8) | Per-player fade state + event history | Show the outcome form to a player with no own fix |
| CR-H5 | Shift Briefings and worked-example lines name only machines and values that exist and are unlocked at that moment | Briefing and card text keys vs. unlock state | Name the atmospheric processor in the P1 briefing |
| CR-H6 | Drift event alerts name a real active drift event | Drift events vs. alert messages | Send an alert with no event |

## 3. Record and evidence rules

| ID | Rule | Check |
|---|---|---|
| CR-R1 | No Keeper's Record page fills from a planet-scope event, an admin or debug cause, or another player's action (LEARNING.md 3.0; ARCHITECTURE.md §10) | Fill events vs. their causing event's scope, cause, and actor |
| CR-R2 | No Record page fills from activity alone (opening pages, clicking, time spent). Every trigger is a correct answer, a held fix, or a written answer of ≥ 8 words (LEARNING.md 4.9) | Trigger definitions against an allowed-trigger list |
| CR-R3 | Each forecast is scored once per player: the first submitted answer; late, expired, and replayed forecasts never count toward B3 (LEARNING.md 3.10) | Export vs. event log |
| CR-R4 | Exports contain no username, account ID, or IP, and reflections only appear in `reflections.csv` | Export files scanned for every name and UUID in the test server's mapping file |

## 4. Text rules

| ID | Rule | Check |
|---|---|---|
| CR-T1 | Player-facing text never uses "learner", "lesson", "objective", "quiz", "test" (TEAM.md 8.4) | Banned-word scan of language files |
| CR-T2 | Never "mutate" or "mutation" for the thawlings | Banned-word scan |
| CR-T3 | Every fauna journal entry carries the fiction note | Entry keys vs. fiction-note key |
| CR-T4 | Length budgets per medium (story bible §6.1; prompts-and-reflections §5) | Word counts |
| CR-T5 | Feedback wording is in positive terms: no "you failed", "wrong!", "you forgot" (LEARNING.md 4.5) | Banned-phrase scan |
| CR-T6 | Readability screen: Flesch–Kincaid grade ≤ 7 for MS main text (a screen, not proof; prompts-and-reflections §5) | Formula over language files; report, don't block, at first |

## 5. Stays manual (can't be automated well)

- Science correctness of explanations and Earth links (SME review, LEARNING.md evaluation loop).
- Whether a hint is *useful* (playtest).
- Tone, fairness of the D1 voices, and "no right answer" framing (Lead Game Designer, SME, me).
- Rubric calibration (two raters, playtest answers).
