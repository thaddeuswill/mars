# Report: Game Designer, storm science edits (2026-11-28)

Role: Lead Game Designer. Docs only. No Java, no pack, no numeric value changed. Checked: the only test that parses balance sheet §2a (`PanelDustTuningTest`) matches rows and phrases I did not touch (stage interval, output by stage, "Recovers half the lost output", devil %, "+N stage at storm end", "1 − 0.6 × storm cap", "Phase 2 3.75 sols, Phase 3 6, Equilibrium 12"). Not run (docs-only; no Gradle).

**Perspective round (my lens).** This succeeds if a 12-year-old can say why a storm hurts power, and every storm number in the sheet says honestly whether it is real, calculated or invented, without the Technical Lead's data or tests moving.

## 0. Handoffs received (SME, `docs/science/storm-dust-review.md` §11)

| Item | Answer |
|---|---|
| Apply §9 items 1 to 8 and 10 | **Accepted, done** (table in §1) |
| Item 9 (storm 1 to 2 sols, F1 warning 2 sols) | **Questioned, proposal only** (§3) |
| Monitor cues (haze row, day-night spread, "feels like" wind) | **Accepted in part** (§4) |
| Orbiter audit row | **Accepted**, added as *proposed* (§5) |
| Thin-air wording for Phase 3 and equilibrium (SME decision 2) | **Accepted** (§6) |
| Journal wording, F1 text (ID), technical function (Technical Lead), storm keyframe shadows (Artist) | Not mine; see Handoffs |

## 1. Done

| SME item | File and place | Edit |
|---|---|---|
| 1 | `docs/design/balance-sheet.md` §2a "With a storm" | Tag now: about −82 % (2007), −97 % (2018), InSight −35 %; verified/calc; cap 1.0 ≈ tau 2.4; real storms last days to months |
| 2 | §2a "Storm adds" | Fallout "for weeks" and "can add to the panel layer", verified (NASA 2022) |
| 3 | §2a "Dust devil chance" | Added Opportunity at least 7 events in 5,111 sols ≈ 0.14 %/sol (calc, lower bound); 1 % is about 7 times real |
| 4 | §2a "Real accumulation" | Added InSight 1,211 sols (verified) and "Lorenz 2021 still not opened" |
| 5 | Drift events, "Dust storm" row | Tagged invented; real 3 days to months, game compresses |
| 6 | Drift events paragraph | Warning "at least 1 sol" now carries the real range (1 sol own sky, about 12 sols orbiter), verified; 1 sol is an honest minimum |
| 7 | §2 storm-cap paragraph | Real anchors: cap 1.0 = tau 2.4; cap 0.5 = InSight regional; 0.8 and 0.25 have no analogue |
| 8 | §2 line "Dust storms cut solar output..." | "left Opportunity without enough power; NASA believes drained batteries ended the mission" |
| 10 | `DESIGN.md` Dust storms bullet and the audit row | "Thin air" now scoped to today's pressure; later phases weak by design; the "less often as pressure and biomass rise" rule is tagged invented with no science reason |
| Closing the flag | `balance-sheet.md` §2 curve note, §2d storm check | First storm is **0.75 sol** (f1-storm-spec, owner-accepted); "2-sol storm on sols 14-15" retired; curve numbers unchanged (calc: about 0.14 kPa loss, under tolerance); battery check now says "up to 2 sols" is survivable |
| Spec upkeep | `docs/design/f1-storm-spec.md` | §2.3 real durations (verified); §2.5 flag marked closed; §5.2 fact statuses upgraded (97 % and real cuts now verified/calc, "days" must become "weeks"); F1 depth-2 line now "On Mars today, the air is so thin..." (Phase 1 text, still true); haze-precursor stays **unverified** with the SME's finding noted |

`ui-specs.md` needed no edit (no new Monitor row is approved).

## 2. Decisions needed (owner), at most 2

| # | Decision | Type | Recommendation |
|---|---|---|---|
| 1 | Keep the first-storm warning at **1 sol** and the length at 0.75 sol for slice 5 (SME item 9 asks for 2 sols and 1 to 2 sols) | Ill-structured (honesty vs. classroom pacing vs. testable fixed numbers) | **Keep 1 sol and 0.75 sol now.** Reasons in §3. Revisit after the first playtest, then try a 2-sol warning for F1 only |
| 2 | Name the storm-warning source in the fiction as "an orbiter weather map" (real, MRO) or leave it unnamed | Ill-structured (small story/authenticity gain vs. one more fiction claim to maintain) | **Leave unnamed in slice 5**; add the orbiter when the relay or orbit story beat needs one. The audit row is in, marked proposed |

## 3. SME item 9 (storm length, F1 warning): proposal, not applied

| Option | Effect | Cost |
|---|---|---|
| A. Keep: 1 sol warning, 0.75 sol first storm (recommended now) | AC-4, AC-8, AC-20 and the JSON stay true; classroom Dome stable (sol 2.5) and first warning (sol 3.5) stay at least 1 sol apart | Real warning is longer; the sheet says so in the note |
| B. F1 warning 2 sols, storm 0.75 | Closer to real; more time for the C1 plan and for a player to reach a natural pause for the card | Classroom: warning at sol 2.5 equals Dome stable (breaks AC-20) unless onset moves to 5.5; the log reminders and `{eta}` text change; data and tests change |
| C. Storm 1 to 2 sols as well | Closer to real duration; battery lasts 4.3 sols (calc), so it is survivable | Larger power loss, curve dip grows (balance §2 curve to re-check), classroom 0.5 sol stays, so the presets differ more |

My lens only: game design. The SME's lens (honesty) favors B or C; the Technical Lead's lens (build in progress) favors A. Learning goals C1 and B3 are served by A already. **Assumption:** most players meet a natural pause within 1 sol (not measured; the `expired` count in logs will tell). **Better defined by:** the first playtest `expired` rate for F1; if above about 30 % (my guess, unverified), take B for the standard preset only.

## 4. Monitor cues: decision and height cost

Budget (read from `MonitorStyle`, ARCHITECTURE 2.20): `LINE_H` 10, `TARGET_H` 130, `PLOT_OVERFLOW` 20, so **150 GUI px is the cap**; the normal state already sits at about 149 and the busiest at about 156 without the plot. The f1-storm-spec adds two problem lines (+20 px worst case, **not measured**), so the plot already gives way. Any further row is paid for by the plot.

| Cue | Decision | Height cost | Why |
|---|---|---|---|
| Sky haze (tau) | **Yes, but not a new row.** Fold a plain word into the existing storm line while the storm is active ("Dust storm now. Sky: hazy. Clearing in about {eta}"); show the tau number for high school only in the line's detail or the journal, never before the lock | **0 px** (same line); +10 px only if the word needs its own line, which I reject | Matches the cause line; teaches the quantity NASA uses. Must stay held until the F1 lock (`reveals: forecast.f1`), and a plain word must not use the banned words "light" or "sunlight" in pre-lock text |
| Day-night temperature spread ("Nights warmer, days cooler") | **Not in slice 5.** Build for the second storm (Phase 2). One line, text only, no number, shown during the storm after the lock, in the slot the "Outside air" line gives up | **+10 px** while a storm is active (swapped for the 9 px Outside air line in Phase 1, so about +1 px; Phase 2 needs a measure) | Good "dust is not only bad" lesson, but the game has no day-night temperature model; a qualitative line with no number is honest (verified), but it must not contradict the Index temperature, so Technical Lead and SME check before it ships |
| "Feels like on Earth" wind line | **Not on the Monitor.** Put it on the **weather mast** (windsock plus a small dial) from Phase 2, driven by live pressure, with the Technical Lead's pure function | **0 px** on the Monitor | The Monitor is at its cap; the mast is the in-world instrument, and it teaches why wind grows with pressure (B1, B5). Slice 5 does not need it |
| Pressure cue | **No** (SME and I agree) | — | A 40 Pa tide is invisible next to kPa and invites "storms add or remove air" |

## 5. Fiction audit row

Added to `DESIGN.md` Fantasy and Authenticity table: "BELL's storm warning comes from an orbiter map", real (MRO, NASA's "early warning system"), status **proposed, keep if used**. Extended the Dust storms row (thin air limit, real vs. game cut and duration).

## 6. SME open decision 2: thin air and later phases

**Answer: yes, keep storms weak by design, and stop citing thin air beyond today's pressure.** Wording rules now in DESIGN.md and f1-storm-spec:

| Where | Allowed wording | Not allowed |
|---|---|---|
| Phase 1 (0.6 to 5 kPa), F1 depth 2 | "On Mars today, the air is so thin that wind pushes very little." | "Mars's air is thin" with no time frame |
| Phase 2 | "The air is thicker than at the start, so wind pushes more, but our storms stay mild." | Thin air as the reason storms are weak |
| Phase 3 and equilibrium | "The air is thicker now, so wind pushes harder than at the start. Storms are weaker because the planet is steadier." Mark the second clause as the game's rule | Any physics reason for weaker storms |

The honest reading for students: storms weaken because the game says so; as air thickens, the same wind would push harder. That is a feature (B2), not a flaw: it shows that thickening the air has side effects.

## 7. Handoffs

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** Journal and F1 text need the SME wording and the Phase-scoped thin-air rule.
> **Context:** `docs/science/storm-dust-review.md` §10; `docs/design/f1-storm-spec.md` §1.5, §5.2; this report §6.
> **Needed from them:** "days" to "weeks" in `journal.dust.storm_timing`; upgrade `journal.f1.real_vs_game`; "believed" for Opportunity; add `journal.storm.wind` with the Phase 1 / later wording split; check haze word has no banned pre-lock term.
> **Done when:** `journal-dust.md` carries them with goal IDs (B1, C1, A1).

> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** No change to slice 5 data. Later: weather mast "feels like" function; haze word on the storm line.
> **Context:** §4. Formula in storm-dust-review §11 (10.9, 31.4, 77.0, 121.8 km/h).
> **Needed from them:** nothing now; when building the Monitor storm line, measure height with the new line and report it (AC-17). Confirm no hard-coded F1 depth-2 text still says "On Mars, the air is so thin" (spec text only was changed).
> **Done when:** height printed in the slice 5 report.

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** Storm keyframes: soft shadows, lower contrast (SME, optional).
> **Context:** storm-dust-review §11, `docs/art/references/PIA25286.jpg`.
> **Needed from them:** optional note in keyframes §3.7a.
> **Done when:** note exists.

> **To:** Lead Subject Matter Expert · **Problem type:** well-structured
> **Problem:** Confirm the day-night line may be text-only with no number; confirm the "Sky: hazy" word.
> **Needed from them:** a yes/no before the Phase 2 storm build.
> **Done when:** reply recorded.

## 8. Risks

| Risk | Mitigation |
|---|---|
| Lorenz 2021 still unopened, so "about 15 times faster" and 0.2 %/sol keep their *searched* tag | Human opens HAL PDF hal-03369920 |
| Real storms are far stronger than ours (−97 % vs −60 %); students may think Mars storms are mild | Journal real-vs-game line; sheet note |
| 1-sol warning plus a short window may leave some players without the F1 card | Watch `expired` counts in playtest (§3) |
| Monitor already at its height cap | Haze folded into an existing line; mast hosts the wind cue |

## 9. Shared model updates

- **Lesson (from SME, confirmed):** a "because Mars is X" reason in a design rule must be checked against the Index range the player will reach.
- **Decision (this report, owner to confirm):** storm weakness in Phase 3 and equilibrium is a game rule (storm cap, invented), never "thin air"; owning docs: `DESIGN.md` Dust storms and Fantasy table, `docs/design/f1-storm-spec.md`.
- **Fact:** the first storm is 0.75 sol (standard) and 0.5 sol (classroom); the balance sheet curve now says so.
- **Fact:** Monitor height cap is 150 GUI px (`TARGET_H` 130 + `PLOT_OVERFLOW` 20); new storm cues go on the weather mast or into existing lines.
