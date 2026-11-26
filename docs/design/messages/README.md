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
