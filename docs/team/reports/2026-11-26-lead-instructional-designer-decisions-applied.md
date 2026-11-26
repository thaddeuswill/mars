# Report: User Decisions and Handoffs Applied (Learning)

- **Role:** Lead Instructional Designer
- **Date:** 2026-11-26
- **Outputs:** `LEARNING.md` (rewritten), `docs/learning/prompts-and-reflections.md`, `docs/learning/rubrics.md`, `docs/learning/ngss_map.example.json`
- **Perspective round:** Succeeds if every goal is taught, practiced, given feedback, and measured **for each student** on a shared planet, without breaking play or leaking identity.
- **Note:** A document review only; nothing was playtested.

## Handoffs received (all accepted unless noted)
- **Producer:**
  - M-01: 23-period unit, 17 play periods (~11.3 h), debriefs in periods 6, 14, and 21.
  - M-14: new §3.0 separates individual summative, individual formative, and group evidence. Only individual summative evidence is graded; the Keeper's Record fills only from a player's own evidence.
  - M-15: A4 and B4 objectives added.
  - m-01 to m-12 fixed. m-18 marked unverified.
- **Story bible:**
  - Radiation forecast added as F2, repeated as F7.
  - D2 Earth-link spec written.
  - Reflection prompts edited; HS versions need a counter-reason.
  - Reading level rules set.
  - "Keeper's Record" accepted as the in-game title.
  - **Pushback:** story bible §7.1 still mentions ×0.3 and the old debrief periods.
- **SME:** all applied. New NGSS table (verify caveat kept), A1 is now a data task, misconception rows fixed, sourced accuracy notes, new SEP and CCC.
- **Game Designer alignment check:** all accepted.
  - **Concern:** the narrow O2 window (21.5–23%). Recommendation: one zoomed gauge with three zones; partial pressure only in "read more" for middle school.
  - **Found:** the ice-to-water change broke forecast F4. It's now split into F4a (vapor, Phase 1) and F4b (water stays, Thaw).
- **Technical Lead:**
  - "Pseudonymous on the server, anonymous in every export" used throughout.
  - Username option removed.
  - Java-only note added.
  - Final CSV columns set.
  - Every goal is player-scope.
  - NGSS JSON example added.
  - `reflections.csv` agreed, with conditions.
- **Coordinator:** fuzzy ADDIE noted; RETAIN review added (plus Gagné and ARCS); goal priority tiers set; interruption rules reviewed.

## Done (highlights)
- Player-language mapping: forecast, Keeper's Record, Keeper's Report, "What caused it?".
- Alignment table now has columns for taught / practiced / feedback / individual measure / group evidence.
- **RETAIN review:** embedding strong; relevance, transfer, adaptation (RETAIN), and immersion (RETAIN) partial; **naturalization a gap** (nothing measures retention after the unit).
- **Goal priority tiers (proposed):**
  - Primary: A2, A3, A5, B1, B2, B4, D2.
  - Secondary: A1, B3, B5, C1, C2, C3.
  - Can be deemphasized: A4, D1.
- Stage 3 minimal learning layer (§3.5), interruption rules (§4.6), O2 readability (§4.7).

## Decisions needed
| Decision | Recommendation | Type |
|---|---|---|
| Reflections export | Separate `reflections.csv`: on by default in classroom mode, names scrubbed to IDs, "don't write your name" note, paper fallback | Ill-structured |
| Goal priority tiers | Adopt after consensus with Game Designer and Producer | Ill-structured |
| Interruption rules | Consensus with Game Designer. ID position: debrief deferral limited; rate limit applies to pop-ups only | Ill-structured |

## Handoffs
- **Lead Game Designer:**
  - Fix story bible §7.1–7.2.
  - Write F1–F10 and the Earth links in the characters' voices.
  - Change "by accident" to "without meaning to".
  - Rename "Explain it".
  - Align "colony expertise" wording.
  - Breathable-air gauge UI spec.
  - Radiation spike warning ~1 sol ahead.
  - Consensus input on interruption rules and priority tiers.
- **Technical Lead:**
  - Add `goals.csv` and the final columns to §8.6.
  - Name scrubbing in exports.
  - Reflections on by default in classroom mode.
  - Log expired or late forecasts.
  - Actor ID on Shift Board actions.
- **SME:**
  - Forecast explanations and distractors.
  - Earth links.
  - Transfer-task answer key.
  - ETS split for C1/C2.
  - Permafrost and sublimation examples.
- **Producer:**
  - Stage 3 slice items into the roadmap.
  - Optional delayed post-test in Stage 6.
  - Java-only note and paper fallback in Stage 4.
  - R-13 update.
  - DOCUMENTATION.md entries for `docs/learning/`.

## Risks
- The narrow O2 window plus frequent drift events.
- Sparse individual evidence for quiet students.
- Phase 2 may run long in class.
- Free text in exports, with no legal review.
- D2 content not yet written.
- Unverified citations (Nestojko et al., 2014; RETAIN definitions; reading rate).
