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

---

## Addendum: follow-up on the SME's and Technical Lead's round 3 (filed by coordinator)

All items accepted and applied; no pushback. On paper every goal is still taught, practiced, given feedback, and measured. Document walkthrough only.

- **FI-1:** new stem ("Your emitters run on solar power. What happens to the warming they add…"); the old one was ambiguous because real dust storms heat the air while cooling the ground.
- **FI-2:** the premise was physically impossible at equilibrium (nitrogen and O2 can't freeze out at the poles). It is rebuilt on a passing low-pressure weather system, warned at least 1 sol ahead. The key is unchanged ("drops a little", about 0.5 °C, SME calculation). B2 was dropped from this item because a weather system isn't a feedback loop. **FI-2 is unusable until the Game Designer replaces the Phase 3 "polar winter" drift event with a weather low** (fallback: fire FI-2 early in Phase 3, while CO2 is still high).
- **FI-3:** the explanation may say "thick air now blocks most of it", never "as safe as Earth". **FI-4:** "Less light, less CO2 taken in. The algae still breathe out CO2."
- **F4a:** passes under the SME's conditions: puddle drying range 0.02–0.25 sol (invented), frozen water counts as "no liquid", thin puddles below 5 kPa, no game text states a drying time. The current 0.1 sol passes. Added to content rule CR-F1, which now also checks that every event a forecast rests on can physically happen in that phase.
- **CTA scenarios** pasted into `cta.md` §4 (4 terraforming proposals, 4 Earth cases) with a use table. **Delayed post-test keys** in LEARNING.md §3.6 (items 6 and 9 use the two examples HS-ESS2-2's clarification statement names: ice–albedo and wetland loss).
- **NGSS map (version 4):** `verification` = "verbatim-checked-2026-11-26"; new `ms_related` field on every goal (B5 gets MS-ESS3-3 as related, not aligned); B3 records HS-ESS3-6's boundary (published model results only). LEARNING.md §6 "verify each code" caveat dropped; the two assessment boundaries that matter are MS-ESS1-3 (excludes recall) and HS-ESS3-6.
- **Educator guide:** debrief 2 teacher note (GISTEMP uses a 1951–1980 baseline while "about 1.1 °C" uses 1850–1900; the CO2 record starts in 1958); ID slips carry IDs only; objectives table marked "checked word for word". **Still open:** dated copies of both graphs must be saved and an ice-core CO2 source named.
- **Technical Lead items:** `forecasts_replay`, `guidance_changes`, `third_party_mods` accepted; "Needs a look" numbers set for `flags.json` (accuracy below 0.33 after 3 forecasts; 2 repeats after own fix; report unsent for 2 sessions or past `debriefMaxDefers`; 1 session present with no own events); CR-H5 partly automatable (lines declare what they name in a `names` field).
- **Handoffs:** Game Designer (replace the Phase 3 "polar winter" event with a weather low, warned at least 1 sol ahead; Shift Briefing 2 says "stay water"; adopt the SME's worked-example reasons); Technical Lead (accept `ms_related` in the `ngss_map.json` schema); Producer (the debrief 2 graphs still block MS-ESS3-5; owner action, now done: gml.noaa.gov and data.giss.nasa.gov were allowed this session, so the SME can save dated copies).
- **Risks:** FI-2 unusable until the weather-low event exists; MS-ESS3-5 rests on graphs not yet opened; FI-4 is conditional on the Phase 3 storm cap and on the SME's proposed CO2 rule A (which could also change F8 and the A5 alignment).
- **Shared model updates:** `ms_related` / `hs_related` mean a real but partial fit, shown to teachers and never counted as alignment; NGSS codes are verified, so documents no longer need the "verify before publishing" caveat; run threshold-vs-curve on events, not only thresholds.
