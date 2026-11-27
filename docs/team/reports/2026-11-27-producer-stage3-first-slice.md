# Report: Producer, Stage 2 close-out check and first Stage 3 slice (2026-11-27)

Role: Producer · Docs only (no `mod/` or `pack/` edits). I read the repo and reports and grepped the code; I did **not** run Gradle, the game, or a playtest. Test counts below are quoted from the Technical Lead's report. All effort numbers are **unverified estimates**.

## 0. Handoffs received
None formal. Inputs: the owner's choice "Stage 3 next after Stage 2" (BACKLOG, HANDOFF section 0), and my own open items R-61, R-62, R-68 from the Stage 2 checkpoint report. Note: HANDOFF section 0 still lists plot scale option D as queued; the code already has `PlotScale.topFor` and `MonitorView.History.goalLine` (grep verified, tests not re-run). HANDOFF is stale there (Codex/coordinator lane).

## 1. Done
- `docs/tech/stage3-plan.md`: proposal for the first Stage 3 slice, one recommendation, one alternative, task table with roles, dependencies, acceptance checks, effort.
- `ROADMAP.md`: Stage 2 checklist rows for items I could verify in the repo ticked or marked partial; "Where We Are Now" rewritten (it still said slices 1 and 2, 67 tests).
- Decision Log: not touched; proposals below.

## 2. Is Stage 2 done? Roadmap vs. what exists

| Roadmap item | Status | Evidence |
|---|---|---|
| Index in save data, readable by command | Done | slice 1 report, `PlanetData` |
| Clock, sols, pause | Done (fake-machine test) | slice 2 report |
| Pause stops Mekanism and Create machines | **Open** | no Mekanism or Create in `mod/build.gradle` (grep) |
| One pressure machine | Done (Outgasser, power, Oven, gates) | slices 1 to 5 |
| One visible response | Done | slice 6, screenshots looked at by that agent |
| One regression source (wear or storm) | **Partly**: frost sink yes; storm and wear no | slice 5; no storm code |
| Admin commands; rollback chosen | Done | slice 8 report |
| Invariant tests I-1 to I-3 | Reported done | not re-run by me |
| Pace config and classroom preset (plan slice 9) | **Not built**: `PlanetData` and `AdminCommands` pass `Pace.STANDARD` | grep |
| Prototype art minimum (E-01, E-02, T-01, M-08, U-01) | Unverified | not checked |
| Stage 2a paper prototypes findings | **Open**: one paper prototype exists (`docs/design/prototypes/first-dust-storm.md`); no findings report found | R-67 |
| Face-validity review (owner and SME) | Unverified, no record found | |
| P1 and P2 playtests | **Not done** | no playtest report |

Exit criterion "the user can raise pressure, see the sky change, feel pressure to maintain it, and say why": every piece exists in the build, but **nobody has confirmed it by playing**. Quoted counts: 199 unit tests, 87 boot checks, `RESULT PASS` (journal and guidance report). Not tested: real `mods` folder, Windows/macOS, dedicated server, real restart (EULA).

**Recommendation: yes, the owner should playtest P1 before Stage 3 content starts** (about 1 h, standard pace, using `docs/tech/launch-guide.md`). Reasons: Stage 2's own rule is "if it's not fun here, rework the loop before going further"; pressure is still off the balance-sheet curve (R-61), so the play will feel fast or flat in places, and the owner should know that is prototype timing; and a playtest costs 1 owner hour against about 10 dev-days of storm work built on an unconfirmed loop. Stage 3 tasks that do not depend on feel (slice 9 pace config, storm numbers, SME facts) can start in parallel. P2 (classroom) needs slice 9 first.

Proposed to count Stage 2 as done when: P1 is played and a short note filed; slice 9 is built; the paper-prototype findings are filed or waived by the owner; the Mekanism/Create pause test is moved to Stage 3 by decision. The first two are required; the last two are owner calls.

## 3. First Stage 3 slice

Full table in `docs/tech/stage3-plan.md`.

**Recommended: S3-1, dust storm on the power grid** (pace config first). Storm schedule (pure, saved, paused with the clock), solar cut 60% x storm cap, panel dust stage added at storm end, Monitor cause line, sky dim, `/mars storm` debug command, wording checks, owner "S1-lite" check. About 10 to 11 dev-days plus 1 owner hour (unverified). Why: it is the missing regression source, reuses slices 3 to 7, its numbers are already in the balance sheet, and it is the base for forecast F1. Out of scope: forecast, dust build-up between storms, wear, dust-off, radiation, recipes.

**Alternative: S3-1b, pace config plus action log and CSV export.** About 6 to 8 dev-days (unverified). Lower risk and gives measurement for every later slice, but the owner sees nothing new.

| | Storm (recommended) | Log and CSV |
|---|---|---|
| Instructional value | Medium | Medium (measurement) |
| Market appeal | High | Low |
| Tech risk | Medium (schema bump) | Low |

Finding while planning: the balance sheet curve assumes a 2-sol storm (sols 14 to 15) but the drift-event table says 0.5 to 1 sol. Task 2 (Game Designer) must pick one.

## 4. Decisions needed (owner)

1. **Playtest P1 before Stage 3 starts?** Well-structured (owner's call). Recommend yes, 1 hour; start slice 9 and the number tasks meanwhile.
2. **Which first slice: storm (S3-1) or log and CSV (S3-1b)?** Ill-structured (appeal vs measurement). Recommend S3-1.
3. **Creative gate bypass as a config switch** (default on in standard, off in classroom), built with slice 9. Ill-structured (R-63). Recommend yes.
4. **Move the Mekanism/Create pause test to Stage 3 (or drop it) and waive the Stage 2a paper findings?** Ill-structured (process; R-67). Recommend move the pause test to Stage 3 next to the load test; ask the Game Designer for short paper findings only for the storm.
5. Still open, not blockers: EULA, three external reviewers (needed before Stage 3 content is final), real `mods` folder and Windows/macOS jar test.

## 5. Handoffs

> **To:** Lead Game Designer · **Problem type:** well-structured
> **Problem:** pick one storm duration and a first-storm sol, and tag the numbers invented.
> **Context:** balance sheet §4 curve (2 sols, sols 14 to 15) vs drift table (0.5 to 1 sol); `pack/data/mars/hazards/panel_dust.json`.
> **Needed from them:** one set of numbers in §2a/§4.
> **Done when:** the curve and the drift table agree and the plan task 2 is closed.

> **To:** Subject Matter Expert · **Problem type:** well-structured
> **Problem:** verify the real storm figures used as ceilings (Opportunity -80% 2007, -97% 2018; InSight about -35%).
> **Needed from them:** source and verified / not verified per figure.
> **Done when:** each figure is marked in the balance sheet and the journal line.

> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** build slice 9, then the storm model; re-estimate from the table, and report elapsed time and build cycles.
> **Context:** `docs/tech/stage3-plan.md` tasks 1, 4, 5, 6.
> **Done when:** acceptance checks in the plan pass in a real run and are quoted.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** grade 6 to 7 check of the storm Monitor and journal wording.
> **Done when:** keys and CR rules confirmed.

> **To:** Coordinator (Codex lane) · **Problem type:** well-structured
> **Problem:** HANDOFF section 0 item 3 (plot option D queued) and the "owner decisions" line are stale; add the stage3 plan to BACKLOG when the owner picks a slice.
> **Done when:** HANDOFF matches the code.

## 6. Risks
- Stage 2 is declared done without a playtest: the loop is unconfirmed (R-17, R-61).
- Slice 9 and the creative bypass change playtest conditions; each playtest must say which mode was used (R-63).
- The storm adds saved state: schema bump must keep old saves loading.
- Stage 3 row budget is already short (panel dust remainder 92 to 100 h, budget.md 6.3); storm S3-1 draws on it. Do not shrink Stage 4 from these guesses (R-68).
- External reviewers are still not found; Stage 3 content is "final" only after their replies.
- Unverified in this report: all estimates; the invariant tests and counts (quoted, not run); prototype art and face-validity status (no record found, may exist elsewhere).

## 7. Shared model updates
- **Lesson:** a roadmap checklist can lag the build by many slices (it said 67 tests with 199 existing); the Producer should tick rows only from a repo check and quote the source.
- **Proposed decision (for the Decision Log, owner to confirm):** Stage 3 starts with the dust storm on the power grid, after pace config; forecast F1 follows as S3-2. Owning document: `docs/tech/stage3-plan.md`.
- **Expectation:** every Technical Lead report from now on states elapsed time and build-and-test cycles (budget.md 6.4).
