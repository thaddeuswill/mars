# Report: Producer, Stage 3 plan (2026-11-27)

Role: Producer · Docs only. Nothing was built, run or playtested. All estimates are **unverified**.

## Owner-facing summary

Stage 2 is done, so I planned Stage 3 (the Phase 1 vertical slice) in `docs/tech/stage3-plan.md`, in the same format as the Stage 2 plan. It has 11 slices (0 to 10). Each ends in a jar you can play for 15 to 30 minutes. Order: close-out, dome and sealing, real life support (this replaces the one-hour Dome stable stand-in and puts pressure back on the designed curve), learning plumbing (action log, pseudonymous IDs, CSV, `guidance`), then Landing and journal, dust and storm with the first forecast, wear and airlock, radiation and the ice rule, the Keeper's Record and group planets, class server and load test, and finally art, audio and your S1/S2 playtests.

I recommend starting slices 1, 2 and 3. Two things are missing before they can go smoothly: a dome layout (slice 1) and life-support numbers (slice 2). Both are the Game Designer's, and the plan asks for them now. Honest size warning: from the three Stage 2 slices where time was recorded (about 8 to 35 minutes each), I cannot convert to hours. By slice count Stage 3 is about 5 to 8 times Stage 2, while the budget row is 3.3 times, so the Technical Lead hours may be short. I did not change any hours; I will re-check after slices 2 and 5.

## 0. Handoffs received

None this task (the request came from the coordinator). I used the Technical Lead's request for elapsed time per report (budget 6.4); only slices 5 to 7 reports have it.

## 1. Done

- `docs/tech/stage3-plan.md` (new): starting point and Stage 2 carry-over table with the slice each item lands in; open owner items with fallbacks; 11 slices with owner role, deliverables, proof, Technical Lead estimate in sessions, what each role must deliver, and inputs needed; first three slices and their input table (G1, G2, S0, T1, I1, G3/I2/S1, I3); Codex lanes (CX-A to CX-H) with file scopes; plan risks; owner questions; how the plan could be wrong.
- `docs/production/budget.md`: new section 6.5 (Stage 3 plan check, measured-rate samples, size ratio, panel dust now in slice 5, owner time); change-log row. No hours changed.
- `docs/production/risk-register.md`: R-69 to R-73 added after R-68; change-log row.
- Not edited: `ROADMAP.md`, `BACKLOG.md`, `HANDOFF.md`, `TEAM.md` (other lanes).

## 2. Decisions needed (owner)

| # | Question | Type | Recommendation |
|---|---|---|---|
| 1 | Approve slices 1 to 3 (dome and sealing, life support and real Dome stable, learning plumbing) as next? | Well-structured | Yes. |
| 2 | May Codex take slice 3 (learning plumbing) under a strict file claim (`mars/learning/**`), so it runs beside the Technical Lead? | Ill-structured (speed vs two workers in `mod/`) | Yes with the claim; otherwise Stage 3 is serial and longer. |
| 3 | If slices 5 and 6 run long, what goes first? | Ill-structured (appeal vs learning) | Trim wear to the filter anchor and move the rest to Stage 4; keep storms (F1) and dust-off. |

Still open from earlier, not blockers: EULA (class-server slice cannot be called verified without it), three external reviewers (needed by slices 5 and 7), platform checklist at slice 10, testing the jar from a real `mods` folder.

## 3. Handoffs

> **To:** Lead Game Designer · **Problem type:** well-structured
> **Problem:** two inputs are missing for the first slices.
> **Context:** `docs/tech/stage3-plan.md` section 5, G1 and G2; balance sheet has no life-support table.
> **Needed from them:** G1 a one-page dome spec (footprint, layout for five, positions of Ovens, oxygen maker, mast, airlock); G2 life-support numbers (O2 and water per player per sol, tank sizes, oxygen maker and reclaimer rates, recipe costs), each tagged invented or sourced, in balance sheet §2b. Also Landing step 6 text, rose-sky trigger and "Useful now" content before slice 4.
> **Done when:** the page and rows exist.

> **To:** Subject Matter Expert · **Problem type:** well-structured
> **Problem:** check G2 life-support figures and the Dome stable "On Earth" replacement sentence.
> **Done when:** each number is marked verified, invented or unverified.

> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** re-estimate each slice before starting; re-cut the panel-dust remainder (T1); say how much `mars.hazards.dust` and `mars.dimension.ice` already hold; keep the elapsed-time line in every report.
> **Done when:** the numbers are in the slice 1 report.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** confirm the event schema and CSV columns against what Stage 2 emits (I1); F1 distractors for review (I3); wording for slice 4 strings.
> **Done when:** a short list of mismatches fixed or none.

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** straw-man dome frame and glass for slice 1; the stage 3 rows per slice (plan section 3); dashed-horizon decision from BACKLOG #49.
> **Done when:** models exist or the slice ships on SM per the fidelity plan.

> **To:** Codex · **Problem type:** well-structured
> **Problem:** parallel tasks CX-A to CX-H with file scopes (plan section 6); none touch Technical Lead code areas.
> **Done when:** each row's condition is met.

## 4. Risks

- R-69 (Stage 3 size vs budget), R-70 (inputs not ready), R-71 (serial bottleneck), R-72 (learning plumbing late), R-73 (Dome stable slows from about 1 h to about 3 h). Details in the risk register.
- Plan weaknesses: "sessions" rest on three samples; I did not read the code in `mars.hazards.dust` or `mars.dimension.ice`; test counts are quoted from reports, not re-run.
- Gap noted: slice 8 report had no elapsed time, and slices 1 to 4 had none (budget 6.4 only applied from slice 5).

## 5. Shared model updates

- **Term:** "session" as an effort unit: one Technical Lead report, about one Stage 2 slice. Used only as a ratio, never converted to hours.
- **Lesson:** plan inputs (dome layout, life-support numbers) are checked before the slice starts, as with slice 5; the plan lists them as a table with "ready?" and "done when".
- **Expectation:** every slice report from Stage 3 carries elapsed time, Gradle runs, tests added; the Producer's Stage 4 re-estimate uses that table.
- **Note:** Stage 3 slice 2 will make Dome stable take about 3 h (was about 1 h); "prototype timing" label stays until measured.
