# Report: Lead Instructional Designer Round 2

- **Role:** Lead Instructional Designer
- **Date:** 2026-11-26
- **Outputs:** `LEARNING.md` (rewritten), `docs/learning/` new: `educator-guide.md`, `cta.md`, `learner-survey.md`, `content-rules.md`, `media-analysis.md`, `teacher-quickstart-slice.md`; updated: `prompts-and-reflections.md`, `rubrics.md`, `ngss_map.example.json` (version 3)
- **Perspective round:** Succeeds if every goal is taught, practiced, given feedback, and measured for each student on a shared planet; help shrinks as skill shows; players aren't lectured or nagged; a teacher can run the unit and read the data without us in the room.
- **Note:** Design walkthrough only; the Air window was walked through five planet states as a 12-year-old at middle school depth. The agent was later resumed with the Game Designer's round 2 handoffs, so a further report may follow.

## Main findings
- No goal lost its teaching, feedback, or measure in the round 2 alignment re-check.
- **A2 is conditional.** Forecast F4a's trigger and answer only hold if DESIGN.md adopts the SME's boiling-point ice rule, and only if the trigger also requires the ice's local temperature to be at or above the boiling point. Barely warmed ice makes a puddle, so "leaves no water" would be the wrong answer.
- One Record trigger rewarded clicks, not skill (the D2 page filled when a player opened three "On Earth" pages). It now fills from writing only.

## Handoffs received (all accepted unless noted)
- **Artist, Air window:** accepted with concerns (arrow with the actual number when O2 is below the bottom of the bar; too many readouts at middle school depth; SME to confirm the 23% fire edge at 75 kPa; A1 support is weak, an optional Earth marker would help).
- **SME:** all applied (F4a, F1, F2, F9 distractors, F6, F7, F8, F10, Earth links with the 1.1–1.2 °C warming line marked "approved, pending source", transfer-task answer key, HS C2 uses HS-ETS1-2 and HS-ETS1-4, new misconception).
- **Technical Lead:**
  - NGSS map fields added (`tier`, `scope` always `player`, `record_title_key`).
  - New export columns accepted (`forecasts_late`, `void`, `names_replaced`).
  - **Shift Board C2 credit goes to the player who did the repair (`actor`)**; a claimer who didn't repair gets no C2 credit, and diagnosing the cause still earns C2 evidence through "What caused it?". This changes DESIGN.md Group Play.
  - Monitor-check window 0.25 sol, with a 1-second minimum view.
  - Fading numbers: Hana's tip retires after 1 own fix; hints escalate at the 2nd and 4th repeat; outcome-only form after 1 own fix that held; full help on any repeat; forecast pointers return below 1 in 3 accuracy; `debriefMaxDefers` = 2 (after two deferrals the report waits in the side panel and never blocks).
  - Model-based assessment confirmed out of scope; name-scrubbing limits in the educator guide.
- **Producer:** the Keeper's Report accepted as one log line plus a side-panel entry; task analysis, media analysis, and the Stage 3 teacher quick-start done.
- **User decisions:** LEARNING.md §1 (T-C) and §4.6 (I-C) now say decided, with rationale.

## Consensus positions
- **T-C: Agree.** Would block only a cut that removes a Primary goal's only teaching, feedback, or measure, or drops forecasts below 3 per phase. Can live with T-B.
- **I-C: Agree.** Would block only if reports could vanish with no re-offer and no teacher visibility, or forecast results could be dropped.

## Decisions needed (classified by coordinator)
| Decision | Recommendation | Type |
|---|---|---|
| Shift Board C2 credit: the player who did the repair, or the claimer | The player who did the repair (needs the Game Designer to agree) | Well-structured |
| Optional "Veteran" setting for hobbyists (help starts faded, standard mode only) | Defer until after Stage 3 | Ill-structured |
| Weather mast CO2 tube from the start vs. fitted at Phase 3 | Fit at the Phase 3 gate; can live with the other | Ill-structured |

## Handoffs
- **Game Designer:** adopt the boiling-point ice rule and add "local temperature ≥ boiling point" to F4a; a ≤ 10-word "Notice" line in each Shift Briefing; Hana's notes "hide" means "not shown again automatically", add reasons to steps 2 and 5; change Group Play credit to the repairer; report delivery as a log line plus side panel with a "Your shift" block; FI-1 says what powers the emitters, FI-4 gives Phase 3 storms a warning at least 1 sol ahead; start drift events after the first flora has taken root; positive wording, banned-token lists (CR-H2), "Check:" pointers on Phase 1 forecasts, Monitor cause-breakdown default per phase; a quiet "Unlocks at…" line on locked items; optional end-of-session "Shift log".
- **Artist:** an arrow with the actual number when O2 is below 15%; words and shapes only on the check row at middle school depth.
- **SME:** FI-1 to FI-4 stems, keys, and distractors (FI-2's "about 0.5 °C" is a calculation); Shift Briefing science lines; reasons in the worked-example steps; delayed post-test items; name, open, and cite the Debrief 2 Earth graphs; fire edge at 75 kPa; a source for the warming figure and permafrost carbon; real terraforming and Earth-side-effect scenarios for the CTA.
- **Technical Lead:** export columns (`forecasts_interleaved_answered/correct`, `forecasts_scaffolded`, `worked_example`, `briefings_opened`); a Monitor minimum-view key (1 s); "Needs a look" flags in the class overview; printable player ID slips; credit rule = `actor`; tag forecasts `interleaved` and `scaffold`, and mark forecasts re-issued after a rollback `replay` (not scored); `debriefMaxDefers` as above; content rules CR-* and the threshold-vs-curve check, or a feasibility answer for each.
- **Producer:** DOCUMENTATION.md map (educator guide = draft; task analysis in `cta.md`; media analysis in `media-analysis.md`); add `feedback_kind` and `fade_rule` to the shared format; schedule the delayed post-test in Stage 6; external teacher reviewers for the answer key and distractors; the assessment-expert gap in the risk register; fix R-19 (still says "20 periods"); the learner survey in the owner's playtests.

## Risks
- A2 measurement is broken until the ice rule and F4a trigger land (F4a has now broken twice).
- No assessment expert; interleaved items are unreviewed.
- Per-player fading means teammates see different hint levels.
- `reflections.csv` is not anonymous (real names can't be scrubbed); no legal review (R-20).
- Chapter 6 and 9 items are on paper only; cross-references may need a consistency pass.
- Unverified citations: Nestojko et al. (2014), the RETAIN definitions, the reading-rate assumption.

## Shared model updates
- **Terms:** interleaved forecast (FI); outcome form; re-scaffold; "Your shift" block; "Needs a look" flag (teacher-only, never a grade); player ID slip.
- **Decisions (LEARNING.md):** reflections export rules recorded; model-based assessment out of scope at launch; Record pages fill from skill, never activity (≥ 8-word answers); one scored answer per player per forecast; grade bands set science depth, not amount of help; proposed: C2 credit to the player who did the repair.
- **Lessons:** review the owner's draft against learning requirements instead of writing a competing spec, and send requirements before the text is drafted; run threshold-vs-curve on every forecast (it found a second F4a flaw).
