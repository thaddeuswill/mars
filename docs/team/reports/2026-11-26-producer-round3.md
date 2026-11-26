# Report: Producer Round 3 (BACKLOG #16 and #17)

- **Role:** Producer
- **Date:** 2026-11-26
- **Outputs:** `docs/production/budget.md` (revision 2), `ROADMAP.md`, `docs/production/owner-actions.md` (new, one page), `docs/production/risk-register.md`, `docs/production/qa-plan.md`, `DOCUMENTATION.md`, `README.md`, `BACKLOG.md`, `PROCESS.md`, `docs/production/consensus-round2.md`
- **Perspective round:** Succeeds if the owner's decisions show up as scheduled, costed work, and the owner can see on one page what only they can do.
- **Note:** Document work only. Effort numbers are the Producer's estimates (±50%) unless a source is named.

## Scope verdict
Stages 1–3 are still achievable as planned. Stage 4 is tighter than in round 2: the total grew 6% while the cut list shrank, so cuts now cover only about a 15% overrun of Stage 4. Anything worse needs a release-scope call from the owner after the Stage 3 slice.

## Handoffs received (all accepted)
- **Owner round 3 decisions:** each is now in the roadmap, the budget, and the cut list.
- **Technical Lead:** `server-setup.md` scheduled in Stage 4; the owner platform checklist is part of the definition of done, the QA exit criteria, and owner-actions.md; support cost based on "about 1 day per NeoForge bump" (unverified); folder layout done; basic CI is a Stage 1 exit item.
- **Instructional Designer:** map, `feedback_kind` and `fade_rule`, delayed post-test in Stage 6, teacher reviewers, assessment-expert risk. **Pushback on R-19:** its round 2 rewrite already said "23 periods"; the wording was clarified and an old round 1 "~20-period" line in the consistency review was marked superseded.
- **Game Designer:** `ui-specs.md` and `messages/` in the map; Stage 2a paper prototypes scheduled.
- **Lead Artist:** fonts and sound production budgeted (sound hours assigned to the Lead Artist); face-validity review scheduled; music and Perseverance defaults.

## Done (highlights)
- **Budget revision 2:** team effort **6,179 h** (was 5,820, +6%); Stage 4 is 46%, the Technical Lead carries 41%, the Lead Artist 18%. Round 3 changes with hours: guidance setting +76, mirror route +20, ice–albedo loop +22, CI +16, external reviews +56, LMS converter +28, fonts +32, sound re-estimate +52 (about 230 h of sound production in total), ending music +32, Mekanism and Create made optional −20. Owner time is about **89 h** (was 75). The cut list has 13 items worth about 435 h, in T-C order.
- **owner-actions.md:** find three external reviewers (with what to send each), allow the blocked source sites if possible, take the learner survey as yourself and as the two target players, and say who tests on Windows, macOS, and weak hardware. Licensing is already decided.
- **Risk register:** updated R-01, R-02, R-47, R-51, R-40, R-43; new R-52 (Phase 3 CO2 budget, score 6), R-53 (no assessment expert, 6), R-54 (F4a depends on an invented number), R-55 (teammates see different hint levels), R-56 (Sodium compatibility), R-57 (headless screenshots may fail), R-58 (essential story text unfinished), R-59 (guidance level affects evidence).
- **QA plan:** links to `docs/tech/test-strategy.md` (the source of truth for how automated tests work; the QA plan owns exit criteria); new passes for face validity and guidance (Stage 2), targeted external reviews (Stage 3), the LMS converter (Stage 5).
- **Consensus brief:** agreement levels recorded. T-C: everyone agrees. I-C: everyone agrees, except the Game Designer can live with rule 3. No blocks.

## Decisions needed (classified by coordinator)
| Decision | Recommendation | Type |
|---|---|---|
| Do the three early reviewers (before Stage 3) replace the Stage 5 external review, or add to it? | Add to it: the early three check the riskiest science claims; Stage 5 checks the whole journal and educator guide. Costs 16 h of SME time | Well-structured |
| Release-scope options if Stage 4 comes in more than about 15% over budget | Wait until after Stage 3; the team frames options before the Stage 3 debrief. Candidates: ship two of four biome kits and add the rest in an update, or ship classroom mode complete with standard mode tuned for Phases 1–2 | Ill-structured |

## Handoffs
- **Game Designer and SME (ill-structured):** the Phase 3 CO2 budget (R-52): a composition rule for gas added in Phases 1–2 and a threshold-vs-curve check on the Phase 3 curve. Done when the balance sheet shows Phase 3 CO2 reaching ≤ 0.5 kPa with named sinks, before Stage 4 is committed.
- **Technical Lead:** cost the ~21 in-house machines and the mirror route; confirm or revise the Technical Lead rows in budget.md; write `server-setup.md` in Stage 4.
- **Lead Artist:** confirm or revise the sound hours (about 230 h); mark which sounds are "secondary" for cut item 5; name the ice extractor consistently.
- **SME:** three review packs before Stage 3 (one per reviewer: claim, our sources, 2–3 questions).
- **Instructional Designer:** a short item-and-rubric review pack for the teacher reviewer (R-53).

## Risks
- R-01 and R-02 (score 9): the cut-list buffer is only about 15% of Stage 4.
- R-17 (score 9): about 89 owner hours are needed.
- External reviewers depend on the owner finding them before Stage 3; if not found, Stage 3 content stays "accepted, pending external review".

## Shared model updates
- **Terms:** owner platform checklist (at each stage exit, record which platforms were checked and which weren't); budget revision (re-issued when decisions change scope; current is revision 2); "ordered by" in the cut list (says whether the goal tier or the fun–learning quadrant places each cut).
- **Decisions:** sound-production hours belong to the Lead Artist; the forecast floor of 3 per phase is part of the never-cut line (budget.md §4.3, from the Instructional Designer's T-C condition); the QA plan owns exit criteria and test-strategy.md owns how automated tests work.
- **Lessons:** a regex keyed on a risk ID matched the summary table before the register row and silently rewrote the wrong row (the assert-once rule in PROCESS.md §6 would have caught it; fixed before filing); check the current file, not the last report, before handing off a "still stale" item.
