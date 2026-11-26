# Report: Producer Round 2 (roadmap, scope, budget, QA, risks, consensus, process)

- **Role:** Producer
- **Date:** 2026-11-26
- **Outputs:** `ROADMAP.md` (rewritten), `docs/production/budget.md` (new), `docs/production/qa-plan.md` (new), `docs/production/risk-register.md`, `docs/production/consensus-round2.md`, `docs/production/consistency-review.md`, `DOCUMENTATION.md`, `README.md`, `PROCESS.md`, `BACKLOG.md`
- **Note:** Document review only. Effort figures are estimates (±50%).

## Done
- **Roadmap:** budget at a glance; "How We Iterate (ACEDT)" mapping the product, task, customer/SME, and local iteration levels and the four test versions to stages; Stage 2 scoped as a timeboxed first iteration that can fail cheaply (paper and spreadsheet prototypes first, straw-man assets, face-validity checks); Stage 3 data we want from the slice; every handoff placed in a stage with an owner; stage exits use the PROCESS.md definition of done; the external expert review in Stage 5; the licensing plan in Stage 7.
- **Effort budget:** about 5,820 hours. Stage 4 is 47% of it and the Technical Lead carries 42%. About 1,220 hours were added this round (+21%). Owner time: about 75 hours of play across Stages 2–7.
- **Scope check:** achievable through Stage 3; Stage 4 is at the edge. The cut list has 11 items in T-C order (goal tier for learning content, fun–learning quadrant for features, Core features last). Taking every cut saves only about 505 hours.
- **QA plan:** severity levels S1–S4; a row for each Chapter 9 testing task; 12 invariants; passes per stage; a release checklist.
- **Risk register:** R-09 verified; R-07, R-14, R-24, R-25, R-50 closed; R-23 and R-13 updated; new R-30 to R-47 and R-51 (no external SME, score lowered to 4 after the Stage 5 review decision).
- **Consistency review:** every round 1 finding closed on paper; new minor findings N-01 to N-05.
- **PROCESS.md reviewed:** iterations sit inside roadmap stages; risk gate table for document changes; mid-task rule is "backlog row first".

## Decisions needed (classified by coordinator)
| Decision | Recommendation | Type |
|---|---|---|
| How deep Mekanism and Create go | Optional and late; decide after the in-house machine list | Ill-structured |
| A full 40-hour standard run by the owner in Stage 5 | No: a full classroom-preset run plus spot checks saves about 20 owner hours | Well-structured |
| Optional delayed post-test in Stage 6 | Yes, as a design artifact (about 14 hours) | Well-structured |
| Stage 2 as a timeboxed, throwaway iteration starting on paper | Yes | Well-structured |

## Handoffs
- **Instructional Designer and Game Designer:** PROCESS.md says "no modal pop-ups for progress" but decided rule 3 (I-C) allows a blocking, deferrable Keeper's Report. Proposal: offer the report as one log line plus a side-panel entry; open it full screen only when the player chooses or at the next natural pause.
- **Instructional Designer:** mark LEARNING.md §1 and §4.6 decided; write the task and media analyses; teacher quick-start for the slice.
- **Game Designer:** weather mast in DESIGN.md; Phase 2–3 pacing curves; per-phase content inventory; "pseudonymous" in Group Play; position on T-C and I-C; in-house machine list; Stage 2a paper prototypes.
- **Technical Lead:** config keys for the I-C caps and a wrap-up signal; invariant tests and headless solver plan; integration pass per stage; LMS plumbing as a local-only prototype in Stage 4; confirm the effort estimates.
- **Lead Artist:** fix the "no audio owner" note; create `docs/audio/`; concept sketches for the Stage 2 face-validity check.
- **Subject Matter Expert:** a review pack (claims list with sources) for the external reviewer before Stage 5.
- **Coordinator:** update agent files; mark the architecture rule reviewed; log each mid-task request in BACKLOG.md before relaying it.

## Risks
- R-01 and R-02 (score 9): Stage 4 is 47% of effort and the companion mod is 42%; even every cut saves only about 18% of Stage 4. The real check is the Stage 3 re-estimate.
- R-17 (score 9): about 75 owner hours of play are needed; owner-only testing can't show learning gain or age fit.
- R-31, R-44 (score 6): the narrow O2 window is hard to read; the Earth-link content isn't written.
- Four mid-task inputs widened this task's scope; the backlog now logs them.

## Shared model updates
- **Terms:** an **iteration** is one round of agent tasks or one build-and-test loop (inside roadmap stages); **face-validity check**; **risk gate table** (change · could break · check · result).
- **Decisions:** T-C cut order (budget.md §4.3); stage exits use the PROCESS.md definition of done; ACEDT levels mapped to stages.
- **Lesson:** a new process rule can silently conflict with a decided rule the same day. When a process document lands, check it against TEAM.md 8.6 and the other decided rules.
- **Expectation:** the coordinator logs each mid-task request in BACKLOG.md before relaying it, with its row number.
