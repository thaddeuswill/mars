# In-Game Messages: Final Text

**Owner:** Lead Game Designer · **Status:** Draft 1 (2026-11-26); SME round 2 fixes applied (F1, F2, F4a, F6, F7, F9, F10 and all Earth links). All text is still **draft: ID review pending**. Nothing here ships as fact before the SME check. · **Spec:** [docs/learning/prompts-and-reflections.md](../../learning/prompts-and-reflections.md) (what each item must do) · **Voices:** [story bible](../story-bible.md) §4 and §8 · **Format:** [DOCUMENTATION.md](../../../DOCUMENTATION.md) §5.1

These files hold the final player-facing wording for forecasts, Earth links, the Landing checklist, Shift Briefings, the first worked example, and the solar-event warnings. They are the content source the Technical Lead turns into datapack JSON and language files (ARCHITECTURE.md §9).

| File | Contents |
|---|---|
| [phase-1.md](phase-1.md) | Landing checklist, Shift Briefing 1, worked example (first Outgasser), solar-event warning lines, F1, F2, F3, F4a, Earth links: Dome stable, sublimation, Keeper's Report 1 reply |
| [phase-2.md](phase-2.md) | Shift Briefing 2, F4b, F5, F6, F7, Earth links: Thaw, greenhouse effect, Keeper's Report 2 reply |
| [phase-3.md](phase-3.md) | Shift Briefing 3, F8, F9, F10, Earth links: first flora, Keeper's Report 3 reply, Mensah's ending line |
| [story-beats.md](story-beats.md) | Final text for the ten remaining essential story beats (1, 3, 4, 6, 9, 10, 12, 15, 17, 19) |
| [failure-reports.md](failure-reports.md) | Failure report hint ladders, closing lines, and banned hint tokens per failure type (CR-H2) |

## Conventions

**Entry format.** Every entry is one table with the §5.1 fields. Text fields become language keys: `<id>.<field>.<band>` (e.g. `forecast.f1.prompt.ms`). If MS and HS text are identical, the entry says "same as MS" and the loader uses one key.

**Forecast fields** (extending §5.1 for forecasts only):
| Field | Meaning |
|---|---|
| `prompt` | The question. ≤ 25 words |
| `choices` | Correct choice **first** here; the game shuffles them. ≤ 10 words each |
| `why` (HS only) | The "why" follow-up and its choices. Counts toward the goal's Keeper's Record page, not toward B3 |
| `result` | The explanation, shown right or wrong. ≤ 40 words |
| `lock` | When the forecast stops taking answers. Always **before** the outcome or any hint that gives it away (UI spec §4). An unanswered forecast at lock is logged as expired, never as wrong |
| `fiction_note` | Shown in **both** bands, never band-gated (prompts-and-reflections.md §5) |

**Shared result headers** (BELL, both bands):
| Key | Text |
|---|---|
| `forecast.header.right` | Forecast checked: you called it. |
| `forecast.header.wrong` | Forecast checked: not this time. Here's what happened. |
| `forecast.header.expired` | Forecast closed. No answer logged. No penalty. |
| `forecast.issue` | Log your forecast. Your answer stays private until it's checked. |
| `forecast.defer` | Later (closes {lock_hint}) |

**"What caused it?"** is the final in-game name for the old "Explain it" check (a journal page). Header: *"What caused it?"* Sub-line (BELL): *"Pick the most likely cause. No penalty for a wrong pick."* The question bank is the Lead Instructional Designer's; wording follows the forecast rules above.

**Reading level.** Main text targets grade 6–7. Readability was screened with a Flesch–Kincaid grade formula (a screen, not proof; prompts-and-reflections.md §5). Results are in the word-count table below.

**Arguments** in `{braces}` are filled by the game (e.g. `{hold_sols}` is 3 in standard play and 1 in classroom mode). Text never hard-codes a number that a preset changes.

## Lock points, reveals, rests_on, and names (data fields for the Technical Lead)

These fields go in every entry's datapack JSON (ARCHITECTURE.md §2.6.2). They are listed here once so they can be checked together (content rules CR-F9, CR-H5; threshold-vs-curve check).

**Forecasts**
| Forecast | `lock` (closes at the first of) | `reveals` (held until the lock) | `rests_on` (thresholds the correct answer depends on) | `resolve_source` (CR-F10) |
|---|---|---|---|---|
| F1 | Storm onset | `log.storm.onset`; solar output drop; Monitor cause line "Storm: less sunlight" (the panel-dust line is **not** held: it has its own cause and must show at its first appearance) | Storm solar cut > 0 (balance §2) | `storm.solar_cut` (the storm's own cause line, not net power) |
| F2 | T−0.25 sol (`log.solar.shelter`) | `log.solar.shelter`; HUD dose meter; Monitor dose read-more | Shelter factor < open-ground factor (balance §6a) | — (net value: the player can't move it another way within the horizon) |
| F3 | 1 sol after Start (Monitor open shows the card first) | Monitor pressure cause line "Outgasser"; `example.outgasser` step 5 result | Outgasser rate > 0 and ≪ Earth pressure | — (net value: the player can't move it another way within the horizon) |
| F4a | Ice block changes state | Ice change itself; F4a journal page; `earth.f4a.sublimation` | Pressure < 5 kPa; boiling point at pressure; puddle drying < 0.25 sol | — (net value: the player can't move it another way within the horizon) |
| F4b | Ice block changes state | Ice change; `beat.09.thaw` journal entry; `journal.gate.thaw` | Pressure ≥ 5 kPa at the Thaw (~22 kPa); boiling point > local temperature | — (net value: the player can't move it another way within the horizon) |
| F5 | 1 sol after Start (card first on Monitor open) | Monitor temperature cause line "Emitter"; `earth.f5.greenhouse` | Emitter contribution > 0 | — (net value: the player can't move it another way within the horizon) |
| F6 | New machine has run 0.25 sol | Fauna page F before/after; raid log lines | Raids ∝ signature (balance §6) | — (net value: the player can't move it another way within the horizon) |
| F7 | T−0.25 sol (`log.solar.shelter`) | `log.solar.shelter`; expected dose read-more | Pressure ≥ 5 kPa; atmosphere factor < 1 (balance §6a) | — (net value: the player can't move it another way within the horizon) |
| F8 | 1 sol after the kit (card first on Monitor open) | Monitor O2 and CO2 cause lines; carbon flow view | Rule A swap (DESIGN.md Phase 3 composition rule) | — (net value: the player can't move it another way within the horizon) |
| F9 | O2 reaches 23 % or 0.5 sol | `beat.15.o2_fire`; Air window "Fire risk in…" line | Fire band 23 % | — (net value: the player can't move it another way within the horizon) |
| F10 | Temperature trend turns down or 0.5 sol | Monitor temperature trend; `failure.emitter_wear` | Emitter contribution needed to hold temperature | — (net value: the player can't move it another way within the horizon) |

| FI-1 (spec: prompts-and-reflections.md) | Storm onset | as F1 | Emitters on the shared solar grid (DESIGN.md) | `storm.solar_cut` → warming contribution of grid-powered emitters during the storm |
| FI-4 (spec: prompts-and-reflections.md) | Storm onset | as F1 | Vats on solar; decay continues (rule A) | `storm.solar_cut` → vat CO2 uptake lost to the storm; **not net CO2** (cleaning panels mid-storm can move net CO2) |

**Beats and messages that could give a forecast away:** `beat.04.storm_warning` (written so it doesn't: no mention of solar), `log.solar.*` (only `shelter` reveals F2/F7, and it is the lock), `example.outgasser` (never states the result), Shift Briefings (no forecast answers: checked).

**`names` (machines and values a line names; CR-H5: must exist and be unlocked when shown)**
| Entry | `names` |
|---|---|
| `landing.checklist` | suit O2, Planetary Monitor, pressure, air mix, seal kit, battery, airlock, dust-off station, journal |
| `briefing.shift1` | O2, water, power, life support |
| `briefing.shift2` | atmospheric processor, super-greenhouse emitter, pressure, temperature (unlocked at Phase 2 begins: OK) |
| `briefing.shift3` | O2, CO2, Air window (Phase 3 begins: OK) |
| `example.outgasser` | Regolith Outgasser, regolith, battery, Monitor pressure causes |
| `example.processor` | atmospheric processor, Outgasser |

## Word counts and reading time (essential classroom text)

Planning rate: 100 words per minute (Lead Instructional Designer's conservative assumption; story bible §7.3). Budget: 1,500 words (15 minutes) for essential story text across the unit.

| Block (MS main text, essential, both readings of each item once) | Words | Minutes at 100 wpm | Flesch–Kincaid grade (screen) |
|---|---|---|---|
| Landing checklist | 87 | 0.9 | 2.6 |
| Shift Briefings (3) | 101 | 1.0 | 3.0 |
| Worked example (full + completion) | 112 | 1.1 | 2.7 |
| Mensah replies (Reports 1–3, ending line) | 152 | 1.5 | 3.1 |
| **Story subtotal, these files** | **452** | **4.5** | |
| Shift Briefing "Notice" lines (round 3) | 24 | 0.2 | — |
| Essential story beats 1, 3, 4, 6, 9, 10, 12, 15, 17, 19 ([story-beats.md](story-beats.md); messages 300, journal parts 160) | 460 | 4.6 | — |
| **Essential story total (budget 1,500)** | **936** | **9.4** | Within budget |
| Forecasts F1–F10 (question, choices, one result each) | 609 | 6.1 | 3.7 |
| Earth links (journal lines) | 93 | 0.9 | 2.4 |
| Solar warnings | 47 | 0.5 | 5.2 |
| **Learning text in these files** | **749** | **7.5** | |

Counted 2026-11-26 by script (words = tokens of letters/digits; F4a counts its usual-case result only). The grade formula scores short sentences low; it is a screen, not proof. A read-aloud check with playtesters is still needed (prompts-and-reflections.md §5). Spread over 17 play periods, all of it is under 1.5 minutes of reading per period.
