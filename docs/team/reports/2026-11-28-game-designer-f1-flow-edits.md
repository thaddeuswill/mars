# Game Designer report: F1 flow edits (Why? on the result card, tally, wording, leak fixes), 2026-11-28

Docs only. No Java, no pack, no game run, no learner data. Desk edits against the Instructional Designer's accepted wording and the owner's answers. I did not touch the data values the Technical Lead is building (storm timing, caps, cut, stage factors, lock point): none of them changed.

## 0. Handoffs received

| From | Handoff | Answer |
|---|---|---|
| Instructional Designer (storm wording report, section 4) | Apply wording to `f1-storm-spec.md`, `messages/phase-1.md` section 14, the "What caused it?" sub-line, F1 `science_claims`; add `reveals`; update AC-16 to one-path | **Accepted**, done (section 1) |
| Instructional Designer | `failure.power_shortfall` hint 2 leak; check other hints and `log.solar.*` | **Accepted**, fixed with the hold variant; scan done (section 1.3) |
| Owner (BACKLOG, 2026-11-28 F1 wording pass) | Why? to the result card; tally to teacher view and shared group screen; CR-F11 | **Accepted**, applied |
| Owner | Rewrite the F2 to F10 Why? placement as a pattern | Done in DESIGN.md (section 1.4) |

## 1. Done

### 1.1 `docs/design/f1-storm-spec.md` (Draft 2)

| Section | Change |
|---|---|
| 1.1 | HS "Why?" is on the **result card only**, logged unscored (`prediction.answered`, step `why`). Distractors changed to "Strong wind damages the panels" (M3) and "The Sun itself gets dimmer during storms" (M4), marked **proposed, pending SME**. Added the 50 percent guess-rate note for "It rises" and "accepted, pending external review (I3)" |
| 1.2 | Held list now includes `journal.storm.wind`, `explain.f1.after_storm`, the F1 Why? step and the two-path diagram; `hud.storm.tooltip` text; failure hints and the Briefing line named as weak-hint watch item |
| 1.3 | Tally only in the **teacher view and the shared group screen** after resolve, all three counts on one line, never on the individual card, never "you were in the minority" |
| 1.5 | Result card: ID text with "Look for the Storm line on the Monitor." (33 and 34 words, cap 40). Read more rebuilt as three parts: choice line (`m1` to `m4`), SME explanation by band, real-versus-game line with `{max_cut}`. Old wind sentence removed from the card (moved to `journal.storm.wind`). F1 `science_claims` updated ("NASA believes", never "went silent") |
| 1.6 | `journal.storm.first` ID text (fixes the "even less" misreading); HS multiply sentence; two-path diagram only here; `journal.storm.wind` and `journal.dust.storm_timing` ("weeks") listed as unlocked with it |
| 1.7 | `explain.f1.after_storm`: trigger guard (largest remaining cause is Panel dust); distractor 2 = "The battery is low" with its result line; sub-line "No penalty for any pick." |
| 2.2, 2.6, 2.5 | `log.storm.onset` text; `log.storm.end`, storm_timing wording; note that the shortfall hint has a plain and a held variant |
| 3.1 | Monitor forms: "Panel dust: {n} percent less power", "Storm: {n} percent less sunlight" (CR-T7(b)); fallback "Storm: 60% less sunlight", never the minus sign |
| AC-10 | Adds **journal retrieval and pending-queue** delivery order for the five held entries; Why? not obtainable before the result card; CR-F11 scan of failure hints and journal assets |
| AC-13 | Tally only in teacher view and shared group screen, absent from every individual card |
| AC-14 | Includes the sub-line in the "wrong" scan; Why? step unscored |
| AC-16 | **One-path** dust-page diagram in every state; two-path only in `journal.storm.first`; test that the two asset ids differ and the dust-page asset is referenced by no held page |

### 1.2 Messages files

| File | Change |
|---|---|
| `messages/phase-1.md` | F1 `why.hs`, result text, `science_claims`; section 14 diagram line (one-path), shipping-rule exception for `journal.f1.real_vs_game` (numbered text ships, SME opened the sources), `storm_timing` "weeks"; `why.hs` placement note on the other Phase 1 forecasts |
| `messages/phase-2.md`, `phase-3.md` | `why.hs` rows carry the "result card only" placement note |
| `messages/README.md` | Sub-line "No penalty for any pick."; `why` and `result` field meanings; F1 `reveals` form; list of items held with F1; failure hints in the leak list |
| `messages/failure-reports.md` | `power_shortfall` hint 2 fixed; new forecast-leak rule and scan table |

### 1.3 Hint leak fix and scan (CR-F9, CR-F11)

- **Fix:** plain hint 2 "Nights cut solar. What carries the dome till morning?"; the variant "Storms and nights cut solar..." is held (`reveals: forecast.f1`) and shown after that player's F1 resolves. Hana's first-occurrence tip must use the plain variant until then.
- **Scan result.** `power_shortfall` hint 2: fixed. `fauna_damage` hints 1 and 2 point at "signature draws raids" (F6 answer): hold with `reveals: forecast.f6`. `emitter_wear` hint 3 ("Warming needs upkeep every sol") states the F5 and F10 mechanism: held to F10's resolve. The other `failure.*` hints, `o2_overshoot_fire` (fires after F9's lock) and `log.solar.*` pass. `grep` of the pack finds no storm text in `lang` or the journal data yet except `landing.intro` ("Storm's over, Keeper. Damage logged.").
- **Watch item, not changed:** the backstory storm in `beat.01.storm_clears` and `landing.intro` ("Storm passed. Damage logged ... power at {power_pct} %") links storms to damage and low power before F1, and a storm that breaks a seal reinforces the damaging-wind belief (M3). It is owner-accepted Landing text. If playtest F1 accuracy is near 100 percent, use "The dome took a hit." (names no cause).

### 1.4 DESIGN.md: "Forecast 'Why?' pattern"

Added under "First experience to design in detail": a rule table (where, why, scoring, feedback, tally, Read-more order, data, how F2 to F10 and FI-1 to FI-4 follow it) and a proposed Decision Log entry with rationale, alternatives, trade-off and "revisit if". F2 to F10 keep their wording; only placement moves.

## 2. Decisions needed

| # | Question | Type | Recommendation |
|---|---|---|---|
| 1 | Accept the Decision Log entry "Why? on the result card" (text in DESIGN.md) | Well-structured | Accept |
| 2 | Hold `fauna_damage` hints and `emitter_wear` hint 3 as above (small extra holds in Phase 2 content) | Well-structured | Accept; low cost |

## 3. Handoffs

> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** Slice 5 step G (`docs/tech/slice5-plan.md`, row G, section 3 forecast JSON table, and `ForecastCard`) must match the flow changes.
> **Context:** `docs/design/f1-storm-spec.md` 1.1, 1.3, 1.5, 1.6, AC-10, AC-13, AC-14, AC-16; `messages/failure-reports.md`.
> **Needed from them:** (1) **Why? not on the card sent before the lock**: `ForecastCard(id, promptKey, choiceKeys, checkKey?)` stays as is; the `why` field (HS) travels only with the result payload, and the reflective test must assert no Why key or text is in any pre-lock packet. Log it as `prediction.answered` with step `why`, unscored, no score field. (2) **Tally**: no group count on the individual result card; produce it only for the teacher view and the shared group screen after resolve (all three counts in one line); AC-13 tests both. (3) Result keys per spec 1.5: `result.right`, `result.other`, and Read more in parts (choice line `m1` to `m4`, explanation by band, `journal.f1.real_vs_game`); the Why? step gets a one-line answer per choice. (4) **Held entries** (`reveals: forecast.f1`, journal retrieval and pending queue, AC-10): `journal.storm.first`, `journal.storm.wind` (variant by live pressure, 5 kPa), `journal.dust.storm_timing`, `journal.f1.real_vs_game`, `explain.f1.after_storm` (with the "largest cause is Panel dust" guard), `log.storm.onset`, `log.storm.end`. (5) **Diagram assets**: one-path file for `journal.dust.panels`, two-path file for `journal.storm.first`; test the ids differ. (6) **Monitor keys**: "Panel dust: {n} percent less power", "Storm: {n} percent less sunlight"; update the height-budget lines in section 5.2 of the plan (they quote the "-n %" forms). (7) **Failure hints**: hold variant of `power_shortfall` hint 2 and `fauna_damage` and `emitter_wear` hold as in failure-reports.md; a solver run with a night shortfall before sol 13 shows no storm word. (8) "What caused it?" sub-line "No penalty for any pick." and the CR-T5 scan for "wrong".
> **Done when:** AC-10, AC-13, AC-14, AC-16 pass as updated, and the plan's step G and section 5.2 quote the new keys and flow.

> **To:** Subject Matter Expert · **Problem type:** well-structured
> **Problem:** Two new HS Why? distractors, and the F6/F10 hint holds.
> **Needed from them:** confirm "Strong wind damages the panels" and "The Sun itself gets dimmer during storms" (ID report 1.7), and that "less sunlight" stays true at the Monitor.
> **Done when:** marked confirmed or changed in `f1-storm-spec.md` 1.1 and `phase-1.md`.

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** Diagram 9 (asset U-25) needs two files: panel-only (dust page) and two-path (`journal.storm.first`).
> **Done when:** the asset list shows both; the panel-only file and alt text show no sky, storm or air path.

> **To:** Instructional Designer · **Problem type:** well-structured
> **Problem:** Check the new `failure.*` hold variants and the proposed neutral backstory wording; confirm the layout decision (MS: choice line and real-versus-game open, explanation behind Read more; HS all open).
> **Done when:** marked accepted or changed in `failure-reports.md` and spec 1.5.

## 4. Risks

| Risk | Effect | Mitigation |
|---|---|---|
| F1 may be easy (briefing, panel-dust page, nights hint, backstory storm all point at power) | B3 measures inference, not prediction | Log F1 accuracy and pick rates at the first playtest; Why? accuracy is a second signal |
| Why? on the result card gets skipped | No self-explanation evidence | Log tap rate; one tap only; revisit per Decision Log entry |
| HS result card is long (190 words in Read more) | Reading load | MS closed by default; CR-T6 real check by Technical Lead |
| Slice 5 step G was planned before these changes | Rework | Handoff above, before G starts |
| New distractors not SME-confirmed | A wrong distractor ships | Marked proposed; ship only with SME mark or "pending" label |

## 5. Shared model updates

- **Decision (owning doc DESIGN.md, "Forecast 'Why?' pattern"):** the HS "Why?" step is on the result card for every forecast, logged unscored. Rationale: protects the prediction surprise. Alternatives rejected: before lock, drop it. Revisit after the F1 playtest.
- **Decision (owning doc `messages/README.md`):** the anonymous tally is teacher-view and shared-group-screen only.
- **Lesson:** `failure.*` hints, journal diagrams and backstory lines are forecast leak paths; run the `reveals` scan on all of them, not just logs and the Monitor (CR-F11).
- **Vocabulary:** none new.
