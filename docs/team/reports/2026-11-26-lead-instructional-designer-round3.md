# Report: Lead Instructional Designer Round 3

- **Role:** Lead Instructional Designer
- **Date:** 2026-11-26
- **Outputs:** `LEARNING.md`, `docs/learning/educator-guide.md`, `docs/learning/teacher-review-pack.md` (new), `docs/learning/prompts-and-reflections.md`
- **Perspective round:** Round 3 succeeds if the decided items are written down with their reasons, outside teachers can review exactly what they are best placed to judge, and the physics changes leave no forecast resting on a regime players never see.
- **Note:** Document walkthrough only; nothing was built or playtested.

## Result
On paper, every goal is still taught, practiced, given feedback, and measured. One forecast is conditional: **FI-4**.

## Handoffs received
- **Owner round 3 decisions:** C2 credit to the repairer; `guidance` adopted with the four conditions; delayed post-test in Stage 6; three external reviewers before Stage 3; owner's Stage 5 run is a full classroom-preset run plus spot checks. All recorded in LEARNING.md with rationale, alternatives, trade-offs, and "revisit if".
- **Game Designer round 3 changes (landed, pass):** A1 now has a Phase 1 source for the gas mix (Landing step 3, a Monitor "Air mix" line, a Dome stable journal line); opening the Monitor shows an unanswered forecast's card first, so checking data no longer costs B3 evidence; puddle-drying time is 0.1 sol, under F4a's 0.25 sol limit; drift events start at the first hold or first established flora; every storm gets at least a 1-sol warning; emitters run on the shared solar grid (answers FI-1). One accepted difference: the Game Designer's Phase 1 "Check:" pointers fade a little sooner than the default, still on the player's own evidence.
- **Lead Artist:** mast gas gauges at Phase 3 agreed and decided (the Phase 1 gas-mix line met the learning condition); one neutral forecast-result sound agreed (LEARNING.md §4.5); subtitles on by default in the classroom pack agreed (§5.1, educator guide); diagram panel count U-25 is **11 diagrams** (7 essential, 4 simple; table in prompts-and-reflections §4a); pre-storm haze is fine as long as the solar output number doesn't drop before the storm starts.

## Done
- **teacher-review-pack.md:** for one middle school and one high school science teacher, about 2–3 hours: (A) transfer-task answer key, (B) forecast distractors and explanations (F1, F2, F4a, F9 first), (C) report questions and D1/D2/B5 rubrics, (D) does the educator guide fit a real classroom, (E) optional test blueprint. It separates classroom judgments (the teachers') from the SME's science pack.
- **Forecast check:** pass for F1, F3, F4a, F4b, F5, F7, F8, F9, FI-1, FI-2, FI-3 and the A1 gas-mix source. **FI-4 is conditional:** the Phase 3 storm cap (0.5) halves the solar cut, so FI-4 is issued only while the cap is 0.5 or higher and the automated check CR-F1 must simulate it; if it fails, drop FI-4 (FI-2 and FI-3 keep Phase 3 at 5 forecasts).

## Decisions needed
None.

## Handoffs
- **Technical Lead:** issue FI-4 only while the storm cap is 0.5 or higher and include it in CR-F1's simulation; subtitles on and `guidance = gentle` in the classroom preset; export `guidance` (planets.csv and players.csv).
- **Subject Matter Expert:** confirm the science pack covers the science side of the transfer key (link signs, strengths) and the FI-1 to FI-4 science; flag items that should move between packs.
- **Producer:** send the teacher review pack with the SME's pack (owner action, before Stage 3); add `teacher-review-pack.md` to the DOCUMENTATION.md map; note in the playtest plan that group dynamics still have no real test (the Stage 5 run is solo).
- **Lead Artist:** plan U-25 as 11 diagrams (7 essential, 4 simple); SME check before drawing.

## Risks
- FI-4 may fail its automated check under the Phase 3 storm cap (a fallback exists).
- No real group test: the Stage 5 run is solo (R-13).
- Until the teachers report back, the transfer key and distractors can't grade real students.

## Shared model updates
- **Term:** teacher review pack (the classroom-judgment half of the external review; the SME's science pack is the other half).
- **Decisions:** one neutral forecast-result sound; subtitles on by default in classroom mode; guidance gentle by default in class; journal diagrams counted per mechanism, not per entry (11 for U-25).
- **Lesson:** re-check forecasts when a tuning number changes, not just a threshold. The new storm cap quietly weakened FI-4. Add tuning multipliers (caps, scales) to the threshold-vs-curve check.
