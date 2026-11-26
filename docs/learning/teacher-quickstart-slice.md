# Teacher Quick-Start: Phase 1 Slice (Stage 3)

**Owner:** Lead Instructional Designer · **Status:** Draft 1, 2026-11-26 · **For:** the Stage 3 vertical slice playtest (LEARNING.md 3.5). In this design exercise the "teacher" is the user, or a teacher-reviewer stand-in. Full guide: [educator-guide.md](educator-guide.md).

> **Requires Minecraft Java Edition. It can't run in Minecraft Education.**

**What the slice is.** Phase 1 only: survive in the dome, the first dust storm, the first solar event, Dome stable, the first Regolith Outgasser, up to 5 kPa (Phase 2 begins). About 2.5 hours of play in classroom mode for a group of 4 (balance sheet §5.3): **3–4 class periods**, plus one for the mini pre-test and one for the mini post-test and debrief.

## Before class (about 30 minutes, one time)

1. Server machine ready, with the pack installed and classroom mode on (server setup guide: Technical Lead, `docs/tech/`).
2. Open one planet per group of 3–5 (`planets.count`), set each planet's group size and grade band.
3. Print the **player ID slips** from the class overview (one per student). Students write the ID, never their name, on test papers.
4. Print the **6-item mini pre/post test** (A1 data task, A4, C1, two misconceptions; LEARNING.md 3.5) and the Report 1 paper worksheet (educator guide §6) as a backup.
5. Check that each student's Java Edition account can join a server (child accounts need multiplayer enabled; `docs/tech/school-deployment.md`).

## Each period

| Time | Do |
|---|---|
| Start | Resume the planets (the server starts paused). Students read BELL's "Since last shift" lines |
| Play (~40 min) | Walk around. Don't give answers; point at the Monitor ("What does it say changed?") |
| 5 min before the end | Press **Wrap up** (no new forecasts or crises after that) |
| End | Pause the planets. One question to the class: "What surprised you on your planet today?" |

## Plan

| Period | Activity |
|---|---|
| 1 | Mini pre-test (15 min). Form groups; suggest colony posts. Start play: the first-shift checklist |
| 2–4 | Play Phase 1. Expect: first failure and Hana's tip, the dust storm and forecast F1, the solar event (F2), ice vanishing near a heater (F4a), Dome stable, Hana's notes on the first Outgasser (F3) |
| 5 | Mini post-test (15 min). Short debrief 1 (educator guide §5.1, first three questions). Export the CSV files |

## What to look at afterwards

- `players.csv`: `forecasts_answered_p1`, `forecasts_correct_p1`, `failures_fixed`, `monitor_checks_before_builds`, `report_1`.
- `planets.csv`: did each planet reach Dome stable and survive its first storm?
- Mini pre/post: score by ID; compare.
- Your own notes: questions students asked, who held the keyboard, who used Hana's notes or the cause breakdown (LEARNING.md, formative evaluation signals).

## Report back to the team

What confused players, what they enjoyed, any moment with nothing to do for more than ~2 minutes, and anything a student said that was scientifically wrong after playing.
