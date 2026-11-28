# Instructional Designer report: F1 and dust-storm wording pass, 2026-11-28

Type of work: docs only, desk review. No Java, no pack, no game run, no learner data. Readability = a **rough** Flesch-Kincaid script (vowel-group syllables; throwaway, session scratchpad), **not the CR-T6 check**; short sentences make FK look easy, so each text also got a hand review for misreadings. All wording changes are **proposed**, with before and after. Edited: `docs/learning/journal-dust.md` (§3, §4 wind row, §6, §7 diagram, new §8 and §9), `docs/learning/items-phase1-misconceptions.md` (six storm items). I did not edit `f1-storm-spec.md`, `messages/*` or the pack; those changes are in section 2 and the handoffs.

## 0. Handoffs received

Task came from the coordinating session (spec section 5.1 pass, SME review sections 3, 5, 10). Accepted all. One pushed back lightly: the owner prompt says "six" storm misconceptions; the SME table has **seven** rows. I wrote six and held the seventh (storms and temperature): the game shows no storm temperature and the figures are search-summary (SME 6 and 14). One SME draft changed, with reasons (wind "gentle breeze", section 1.5).

## 1. Done

### 1.1 Rough readability (words, FK)

| Text | Words | FK | Note |
|---|---|---|---|
| F1 prompt | 17 | 3.0 | pass, unchanged |
| Result depth 1 (right / other), proposed | 33 / 34 | 2.7 / 3.0 | cap 40 (CR-F8): pass |
| Depth 2 MS (SME 10) | 56 | 2.9 | pass |
| Depth 2 HS (SME 10, "tau", "optical depth") | 75 | 6.0 | HS only; the words "tau" and "scattered" are defined in context |
| `journal.f1.real_vs_game` proposed | 47 | 3.8 | HS adds 12 words, FK 8.8 (one long sentence; HS only) |
| `m1` / `m2` / `m3` / `m4` proposed | 29 / 29 / 22 / 15 | 3.1 / 1.2 / 3.2 / 2.3 | pass |
| `journal.storm.first` MS proposed | 62 | 2.1 | budget 80: pass |
| `journal.dust.storm_timing` proposed | 39 | 5.0 | pass |
| `journal.storm.wind` A / HS / B | 43 / 44 / 35 | 1.5 / 5.1 / 3.1 | pass |
| `log.storm.warn` | 17 | 2.0 | CR-T7(c) cap 20: pass (spec says 16; the script counts "1" as a word) |
| `log.storm.onset` (new), `log.storm.end` | 11, 10 | 1.6, 1.7 | pass |
| `explain.f1.after_storm` prompt | 19 | 3.0 | pass |

Words a 12-year-old may stumble on, checked by hand: "scattered" (SME verbatim, used with "like on a cloudy day"), "optical depth" and "tau" (HS, defined in the sentence), "Opportunity" (a rover name, introduced as "the rover Opportunity"), "sol" (taught since the Landing).

### 1.2 SME changes applied (proposed)

| SME item | Where | Result |
|---|---|---|
| Fallout lasts weeks, not days | `journal.dust.storm_timing` | Applied: "keeps falling out of the air for weeks after the sky starts to clear, and some of it lands on panels." (journal-dust §3) |
| "Believed" for what ended Opportunity | `journal.f1.real_vs_game` HS line; F1 `science_claims` | Applied: "NASA believes the rover's batteries ran out, and that ended its mission." The old "and it went silent" is removed (it implied the storm was the cause). MS does not state the fate at all |
| Real-versus-game line in the F1 result | F1 result depth 2 and the journal page (one string, one key) | Applied with `{max_cut}` so it stays true in Phase 2 and later (old line said "60 at most", false once the cap falls), plus one added sentence naming the time compression ("Yours last a sol or less, so you can keep playing"; SME 7 asked for the compression to be named) |
| F1 explanation at two depths (SME 10) | F1 result depth 2 | Applied verbatim (below) |
| `journal.storm.wind` | journal-dust §8.2 | Drafted with three ID changes (1.5) |
| 97 percent now allowed | `journal.f1.real_vs_game` | The numbered text now ships (SME 3.1, 10: 21 Wh and tau verified on NASA pages; 652 Wh baseline is Wikipedia citing NASA, hence "about"). The phase-1.md shipping rule ("a searched number never ships") no longer blocks this one line; `journal.dust.panels` and `earth.dust.solar_farms` still ship their fallbacks (their sources, Lorenz 2021 and the soiling study, are still not opened) |

### 1.3 F1 text, before and after

| Key (spec 1.5) | Before | After (proposed) |
|---|---|---|
| `result.right` | You called it. Solar power dropped. Dust in the air blocks sunlight before it reaches your panels. The Monitor shows how much: "Storm: less sunlight". What could you store before the next storm? | **Only the Monitor sentence changes:** ... Look for the Storm line on the Monitor. ... (reason in 1.4: the quoted line text must not depend on the Monitor key's final form) |
| `result.other` | Here is what happened. (same body) | Same single change |
| Depth 2 general, all | The panels did not break, and nothing pushed them. The light itself got dimmer. On Mars, the air is so thin that wind pushes very little, so the dust is the problem. In the game, a Phase 1 storm can cut your sunlight by up to {max_cut} percent. Real storms can cut much more. | **Split in three.** (a) the choice line, `m1` to `m4` below, first (it answers what that player just thought); (b) the SME explanation by band; (c) the real-versus-game line. The wind sentence leaves this card for `journal.storm.wind` (it said "the air is so thin" with no time qualifier, untrue after the Phase 2 gate, SME 5) |
| Depth 2 (b), MS | (none) | **Why solar power drops.** In a dust storm the air fills with fine dust. The dust blocks the Sun's light, so less reaches your panels. Some light still gets through, scattered across the sky, like on a cloudy day. When the storm ends, dust settles out of the air over weeks and can coat the panels. (SME verbatim, 56 words) |
| Depth 2 (b), HS (shown after the MS paragraph, open by default) | (none) | **Optical depth.** Scientists measure how much dust blocks the light with a number called tau. At tau 3, only about 5 percent of the Sun's direct light reaches the ground. Opportunity's solar power fell about 80 percent at tau 3 and about 97 percent near tau 11 in 2018. It did not fall to zero, because scattered light still reaches the panels. Dust in the air and dust on the panels are two separate losses. (SME verbatim, 75 words) |
| Depth 2 (c) | see 1.2 | In 2018, a storm that covered all of Mars cut the rover Opportunity's power by about 97 percent. Your storms cut {max_cut} percent at most. Real storms can be bigger, and they last days to months. Yours last a sol or less, so you can keep playing. |
| `m1` (picked "stays the same") | unchanged | The Sun was just as bright above the dust. But the light has to pass through dusty air to reach your panels, and the dust blocks some of it. (kept) |
| `m2` ("rises") | A storm puts more dust into the air, so the light gets dimmer. Wind did not clean your panels. Dust only settles on them as the sky clears. | The storm put more dust in the air, so the light got dimmer. Wind did not clean the panels **this time**. Dust settles on them as the sky clears. ("this time": real wind has cleaned some real panels, `journal.dust.wind`; the old line could teach "wind never cleans". SME asked a wording check on `m2` in spec 5.2: still open) |
| `m3` (HS Why, wind) | (merged with `m4`) | Wind did not turn or push your panels. Right now Mars's air is so thin that even fast wind pushes very little. ("Right now", SME 5) |
| `m4` (HS Why, Sun) | (merged with `m3`) | The Sun shines as usual. The dust between the Sun and you is what changed. |
| Tally line (spec 1.3) | "{n} of {m} Keepers said 'It drops'." | Open question 2 |

Depth 2 for MS is about 120 words closed by default, for HS about 190 open. Recommend the card shows the choice line and the real-versus-game line open and the SME paragraphs behind one "Read more" for MS; Game Designer decides the layout.

### 1.4 Monitor and HUD forms against CR-T7 (and the "−" and "%" question)

| Key (spec 3.1) | Now | CR-T7 | Proposed |
|---|---|---|---|
| `monitor.power.cause.dust` | Panel dust −{n} % | (b) **fails in spirit**: "−" and "%" are symbols where words fit, and no existing Monitor line uses either (existing lines say "per sol" and "about"). Also the cause line **double-negates** in the storm line ("less sunlight −60 %") | **Panel dust: {n} percent less power** (32 chars at one digit, 33 at two) |
| `monitor.power.cause.storm` | Storm: less sunlight −{n} % | same | **Storm: {n} percent less sunlight** (31 chars at 60) |
| `monitor.storm.expected` | Dust storm in about {eta}. It lasts half a sol to a full sol. | (a), (b), (c) pass (14 words). "about" is a word, correct form | keep |
| `monitor.storm.active` | Dust storm now. Clearing in about {eta}. | pass (7 words) | keep; `reveals: forecast.f1` |
| `panel.lookat` | Dust stage {n} of 4. It makes {pct} percent of full power. | pass (13 words; "percent" already a word) | keep |
| HUD tooltip (not drafted) | n/a | must pass the banned-word scan (solar, power, sunlight, light, dim) before lock | **Dust storm. Arrives in about {eta}.** (6 words) |
| `log.storm.onset` (not drafted anywhere) | n/a | `reveals: forecast.f1`; delivered at the lock | **Dust storm is here. Watch the Power row on the Monitor.** (11 words) |

If the Technical Lead measures the lines as too wide at 232 px, the fallback is "Storm: 60% less sunlight" (no space before "%", still no "−"), not the minus sign. A unicode minus (U+2212) may also not render in the default Minecraft font: **unverified**, Technical Lead to check. One more hand-review point: the two lines do **not** add (the game multiplies them, spec 2.5), so "Storm 60 percent" plus "Panel dust 8 percent" does not mean 68 percent off. The HS line in `journal.storm.first` now says so; MS is left as is.

### 1.5 `journal.storm.first` and `journal.storm.wind`: what changed and why (full tables in journal-dust §8)

- **`journal.storm.first` had a factual misreading.** The spec said, after the storm, panels "made even less". Against the storm level that is false: the storm cut ends and the dust steps are added, so a stage-1 panel goes from 36.8 to 70 percent of clean output (spec 2.5, 2.6, calc). The only true comparison is "less than before the storm". Fixed in the text and it now sets up `explain.f1.after_storm`.
- **`journal.storm.wind` trigger.** SME wrote "first storm after F1 resolves". Read literally that is the second storm, 8 to 14 sols later (spec 2.1). I set it to F1 resolved. It declares `reveals: forecast.f1` because "Storms on Mars dim the sky" is the answer.
- **"A gentle breeze" to "a breeze", and "right now".** By Beaufort, 11 km/h is a light breeze and 31 km/h (the same wind at 5 kPa) a fresh breeze; the first storm comes at sol 14 at an unknown pressure, so a fixed "gentle" is false for some worlds. Variant B at 5 kPa instead of "Phase 3" (SME's switch): the push is already about three times today's at the Phase 2 gate (SME 5 table). SME to confirm the switch point.
- **HS wind text** uses "1/80 as dense, so about 1/80 as hard" (calc, SME 5.1) and says "today".

### 1.6 CR-F9 pre-lock check on all `journal.dust.*` (journal-dust §9)

| Finding | Severity |
|---|---|
| `journal.dust.panels` text: **passes** (panel path only; no storm, sky, or air). Residual inference accepted: a student can reach "dust lowers power, so a dust storm will" by analogy and answer right for the wrong path. The rule bans saying it, not inferring it. Log F1 accuracy early | Low, accepted |
| **The diagram on the panels page was specced with two paths including "storm, dims the sky"** (`messages/phase-1.md` §14 and journal-dust §7). That puts the F1 answer in a reopenable page about 13 sols early | **High, fixed in journal-dust §7**: panels page = one-path diagram; two-path diagram moves to `journal.storm.first`. Asset U-25 needs two files |
| **`failure.power_shortfall` hint 2** in `failure-reports.md`: "Storms and nights cut solar. What carries the dome till morning?" fires on a real shortfall, likely before the first storm (nights), and has no `reveals` | **High, not my file**; handoff to the Lead Game Designer |
| `journal.dust.wind` "Wind cleaned some real panels" can push toward distractor M2 before F1 | Low; "Now and then" added |
| Briefing "Mars weather can change fast, so keep some power in reserve" (and `power.json`, `briefing_shift1.json`) | Weak hint, accepted (owner-settled in the Landing spec); watch in playtest |
| `journal.dust.floor`, `.not_only_power`, `.base`, `.brush`, `earth.dust.solar_farms` | Pass |

### 1.7 F1 distractors (I3): reviewed, shipped as "accepted, pending external review"

Per the owner's decision. In-lane review, so the external reviewer has something to check:

| Choice | Plausible for a 12-year-old? | Note |
|---|---|---|
| It stays the same (M1) | **Yes**, strongest distractor: the Sun is the same | fine |
| It rises (M2) | **Weak**: few learners believe it without the film or a wind-cleans idea. Cannot change: the game cannot produce a rise (CR-F2). Expect a low pick rate; log it; a distractor that nobody picks is harmless but gives F1 an effective 2-choice guess rate (50 percent) | accepted |
| Why: "Wind turns the panels away from the Sun" (M3) | **Mismatch** with its stored misconception ("strong, damaging wind"): panels in the game do not turn. Proposed: **"Strong wind damages the panels"**, which the new `m3` line then answers directly | proposed, SME to confirm |
| Why: "The Sun gives off less light during storms" (M4) | **Ambiguous**: "less light" is true at the ground, so a correct-direction thinker can pick it fairly. Proposed: **"The Sun itself gets dimmer during storms"** (puts the source in the distractor) | proposed |
| **Why? timing** (not a wording issue) | The three Why choices show the mechanism (dust blocks sunlight) **before the lock**, to a player who just answered. The first answer is locked, so the score is safe, but the player now knows the answer while the storm is still coming, and the surprise of the result is gone (productive failure) | Open question 1 |

### 1.8 `explain.f1.after_storm` (B1 bank item)

Changes in journal-dust §8.3: (1) **trigger guard**: the largest remaining cause line must be Panel dust, else a storm ending at night leaves Night as the real answer, which is not on the list and the item has no key; (2) distractor "The storm is still going" replaced by **"The battery is low"** (a real confusion between power made and power stored; the old one is a free elimination once the log says the sky is clearing); (3) result lines follow; (4) the sub-line "No penalty for a **wrong** pick" contradicts spec AC-14 ("wrong" never appears): change to "No penalty for any pick."

### 1.9 Six storm misconception items

In `docs/learning/items-phase1-misconceptions.md`, MS and HS stems, one key each, goal-tagged: S1 wind blows a base away (A1, B1), S2 hurricane (B1, A1), S3 gone in a day (C1, A1 weak), S4 no direct Sun means no power (B1), S5 only the panels matter (B1, C1), S6 sky clear means dust gone (B1, C1). **Pre-test rule:** only S1 and S3 may go on the pre-test; S2, S4, S5, S6 state the F1 answer in the stem or key and go on the post and delayed post tests only. S2 and S4 have no in-play practice (single exposure): measure them on the delayed post, do not add content.

## 2. Keys and files the Technical Lead must change

State of the pack today: grep shows **no storm, F1, panel-dust or `journal.storm.*` key in `pack/assets/mars/lang/en_us.json` or `pack/data/mars/journal/`** (only `landing.*` dust-off keys and the existing `monitor.cause.dusty`). So these are **new keys**, not edits; use these texts when creating them. Exact ids as in the spec:

| Key / id | Text (both bands unless noted) | Hold |
|---|---|---|
| `log.storm.warn` | Dust storm forming. Arrival in about {eta}. Expected length: half a sol to a full sol. (spec: "about 1 sol") | none |
| `log.storm.remind` | Dust storm in about half a sol. | none |
| `log.storm.onset` | Dust storm is here. Watch the Power row on the Monitor. | `reveals: forecast.f1` |
| `log.storm.end` | The sky is clearing. Dust is settling on your panels. | after F1 resolves |
| `hud.storm.tooltip` (name proposed; before lock) | Dust storm. Arrives in about {eta}. | banned-word scan |
| `monitor.storm.expected` | Dust storm in about {eta}. It lasts half a sol to a full sol. | none |
| `monitor.storm.active` | Dust storm now. Clearing in about {eta}. | `reveals: forecast.f1` |
| `monitor.power.cause.dust` | Panel dust: {n} percent less power | none (after Landing) |
| `monitor.power.cause.storm` | Storm: {n} percent less sunlight | `reveals: forecast.f1` |
| `panel.lookat` | Dust stage {n} of 4. It makes {pct} percent of full power. | none |
| `forecast.f1.storm_power`: `text.ms/hs`, choices | unchanged; HS Why choices 2 and 3 as proposed in 1.7 (**pending SME**) | label "pending external review (I3)" |
| `forecast.f1.storm_power`: `result.right`, `result.other` | 1.3 (only the Monitor sentence changes) | after lock |
| `forecast.f1...detail.*` (depth 2: `m1` to `m4`, explanation MS, explanation HS, real-versus-game) | 1.3 | after lock |
| `journal.f1.real_vs_game` (same string as the result's last line) | 1.2 / journal-dust §6; uses `{max_cut}` from `dust_storm.json` by phase | `reveals: forecast.f1` |
| `journal.storm.first` | journal-dust §8.1; **diagram: two-path variant** | does not exist before F1 resolves |
| `journal.storm.wind` (A below 5 kPa) and a variant B key (name Technical Lead's choice) | journal-dust §8.2 | `reveals: forecast.f1`; variant by live planet pressure (gate "Phase 2 begins") |
| `journal.dust.storm_timing` | journal-dust §3 | after F1 resolves |
| `journal.dust.wind` | journal-dust §4 proposed line | none |
| `journal.dust.panels` | fallback MS text (unchanged); **diagram: one-path variant** | after Landing (spec 3) |
| `explain.f1.after_storm` | journal-dust §8.3: trigger guard "largest remaining cause is Panel dust"; distractor 2 = "The battery is low" | after storm and F1 |
| "What caused it?" sub-line | No penalty for any pick. | Game Designer's file |

Also for the Technical Lead: AC-10 must cover **journal retrieval** and the pending-queue path for `journal.storm.first`, `journal.storm.wind`, `journal.dust.storm_timing`, `journal.f1.real_vs_game` and `explain.f1.after_storm`. A rendering check for the unicode minus is no longer needed if section 1.4 is accepted.

## 3. Decisions needed (at most 2)

| # | Question | Type | Recommendation |
|---|---|---|---|
| 1 | **When do the HS "Why?" choices on F1 appear?** Now: right after the first answer, before the storm locks, so they hand the mechanism to a player who has locked their pick (and the same pattern is in F2 to F10). Alternative: show the "Why?" on the result card, as a "which reason fits what you saw?" step, logged but unscored | Ill-structured (prediction surprise and the self-explanation habit against one more tap on the result card; needs the Game Designer for flow) | Move "Why?" to the result card for F1 now, and test it as the pattern for the rest. Playtest the F1 accuracy and the "Why?" accuracy together |
| 2 | **Anonymous tally on the result card** ("{n} of {m} Keepers said 'It drops'"): on the player's card, or teacher view only? In a group of 3 to 5, "1 of 4 said it rises" can feel like a score or point at a neighbour even with pseudonyms | Ill-structured (the discuss-then-re-forecast activity needs it; belonging against exposure for minors) | Teacher view and the group's shared screen after resolve, **not** on the individual card for a player who answered differently; show all three counts in one line, never "you were in the minority" |

## 4. Handoffs

> **To:** Lead Game Designer · **Problem type:** well-structured
> **Problem:** `failure.power_shortfall` hint 2 says "Storms and nights cut solar" and can fire before the first storm, with no `reveals`. That tells a player the F1 answer before it locks (CR-F9).
> **Context:** `docs/design/messages/failure-reports.md` row `power_shortfall`; report 1.6.
> **Your view:** Hold the storm sentence until that player's F1 resolves, or use "Nights cut solar. What carries the dome till morning?" before it and the full line after. Add `reveals: forecast.f1` to the held variant.
> **Needed from them:** pick one of the two; add the field; check other hints and `log.solar.*` for "storm".
> **Done when:** a solver run with a night shortfall before sol 13 shows no storm word.

> **To:** Lead Game Designer · **Problem type:** well-structured
> **Problem:** Apply the proposed wording to `f1-storm-spec.md` and `messages/phase-1.md` section 14 (diagram line), the "What caused it?" sub-line in `messages/README.md`, and F1 `science_claims` ("left Opportunity without enough power; NASA believes drained batteries ended the mission").
> **Context:** this report 1.3, 1.4, 1.8; `journal-dust.md` §3, §6, §7, §8.
> **Needed from them:** copy in without changing facts or tags (`DOCUMENTATION.md` §5.1); add the new `reveals`; update AC-16 so the dust page diagram is one-path.
> **Done when:** the spec's section 1.5, 1.6 and the messages file match journal-dust.

> **To:** Subject Matter Expert · **Problem type:** well-structured
> **Problem:** Confirm six wording points.
> **Needed from them:** (1) `journal.storm.wind` switch at 5 kPa instead of Phase 3; "breeze" for "gentle breeze"; (2) HS Why distractors "Strong wind damages the panels" and "The Sun itself gets dimmer during storms"; (3) `m2` "Wind did not clean the panels this time"; (4) the S4 key "scattered light is what is left" (your own calc, not a measured fraction); (5) "bigger" in the real-versus-game line (wider or stronger?); (6) the seventh misconception (temperature) when the numbers are opened.
> **Done when:** each is marked confirmed or changed in journal-dust and the items file.

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** Diagram 9 variant (asset U-25) needs two files: panel-only for the dust page, two-path for `journal.storm.first`.
> **Needed from them:** confirm the two assets; the panel-only one must show no sky, storm or air path.
> **Done when:** the asset list shows both.

> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** Create the keys in section 2; hold per `reveals`; variant B of `journal.storm.wind` by live pressure; trigger guard for `explain.f1.after_storm`.
> **Done when:** AC-10 and AC-16 pass including journal retrieval; the banned-word scan covers the HUD tooltip.

## 5. Risks

| Risk | Effect | Mitigation |
|---|---|---|
| F1 may be too easy: the briefing, the panel-dust page, and the nights hint all point at power and dust before sol 13 | B3 measures a guess or an inference, not a prediction | Log F1 accuracy per player at the first playtest; if near 100 percent, B3 leans on FI-1 and FI-4 |
| "It rises" nearly nobody picks | F1 becomes a 50-50 guess | Cannot change (CR-F2); log pick rates |
| Why? shown before lock (decision 1) | Mechanism leaked to every player | Move to the result card |
| `power_shortfall` hint leak not fixed | CR-F9 breach in real play | Handoff above; add a solver check |
| Pre-test items S2, S4, S5, S6 used by mistake | Pre-test gives F1's answer | Marked "post only" in the items file; teacher guide must carry it |
| 97 percent baseline (652 Wh) is secondary | A hard number in game text | "about" kept; fallback line retained |
| Two Monitor lines read as additive | Players add 60 and 8 | HS sentence; consider MS if playtests show it |
| Reading level scored by a rough script | FK underestimates word difficulty | Hand review done; CR-T6 real check is the Technical Lead's |

## 6. Shared model updates

- **Lesson:** a journal diagram counts as text for CR-F9. A page that is fine in words can leak through its picture; check assets, not only strings.
- **Lesson:** hints on real failures (`failure.*`) are a leak path for forecasts as much as logs and the Monitor; every hint needs the `reveals` scan.
- **Rule proposed for `content-rules.md` (CR-F9 addition):** "A diagram or image in a journal page that is readable before a forecast locks must not show the forecast's mechanism." Owner of the rule: me; I have not edited it (stable file; adds a rule, wait for the owner).
- **Decision proposed (owning doc `docs/learning/journal-dust.md`):** `journal.storm.wind` is delivered when F1 resolves, in two variants split at 5 kPa. Rationale: wind push scales with air density; alternatives: one fixed text (false after Phase 2), Phase 3 switch (SME; false from about 5 kPa); revisit if the Game Designer adds a "feels like" Monitor cue, which would replace the text with a live number.
- **Vocabulary:** none new.
