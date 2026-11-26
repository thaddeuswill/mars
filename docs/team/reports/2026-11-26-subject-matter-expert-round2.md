# Report: SME Round 2 (handoffs cleared, value audit, external-review list)

- **Role:** Subject Matter Expert
- **Date:** 2026-11-26
- **Outputs:** `docs/science/round2-review.md` (verdicts with sources S1–S26, value audit in section 10, external-review list in section 11); `docs/science/accuracy-review.md` now points to it
- **Note:** The network refused every source host (NASA, JPL, nextgenscience.org, and others), so checks used search summaries and the SME's own calculations. Pixel sampling of rover images was not possible; published sky-color values were used. Every claim is tagged sourced, calculated, invented, or not opened.

## Verdicts
- **Ice rule: partly wrong.** Pressure passes the triple point (0.611 kPa) on about sol 1, so the "ice turns to vapor" stage almost never happens in Phase 1 and forecast F4a's trigger almost never fires. The draft also said water boils within seconds anywhere from 0.61 to 5 kPa; water boils only above the boiling point for that pressure (0 °C at 0.61 kPa, 33 °C at 5 kPa). **Fix:** compare each ice block's local temperature with the boiling point the Monitor already shows. Near a heater in Phase 1, warmed ice melts and boils away at once. That is real, and teaches the same A2 lesson.
- **Wrong, with fixes:** flame bands below 16% O2; the aurora fading with pressure; the Phoenix ice citation (Phoenix was above the triple point); three partly-true forecast distractors (F1, F4a, F9).
- **Acceptable with rewording:** polar frost, "mature ecosystems are nearly carbon-neutral", the rose Phase 2 sky (stays, labeled stylized), balloon and salt crust, the C1/C2 NGSS split (one change).
- **Accurate:** ground collapse, perchlorate, outgasser, windsock, carbonate deposits, sound, Earth links, all forecast answers. Story bible: 14 [verify] tags accurate, 3 need rewording.
- **Ice–albedo** is real but small on Mars (about 1 °C or less).
- **Not yet sourced in our docs:** the 3–22 minute light delay, 1.1 °C of Earth warming, permafrost carbon. None may appear as fact in game text until sourced.

## Decisions needed (classified by coordinator)
| Decision | Recommendation | Type |
|---|---|---|
| Allow NASA, JPL, and NGSS hosts in the network settings | Yes: needed for pixel sampling, the NGSS word-for-word check, and sourcing | Well-structured (owner action) |
| Seek external reviewers for the list in round2-review §11 (top three: ice rule → planetary scientist; ice–albedo and polar frost → climate modeler; transfer key and forecast distractors → science teachers) | Yes, before Stage 3 | Well-structured (owner action) |
| Ice–albedo size: (a) cap at about 2 °C, "small on Mars, huge in Earth's Arctic" (best for D2); (b) keep it small and add the real loop "warm poles release buried CO2"; (c) make it big (teaches a wrong size) | a or b | Ill-structured |

## Handoffs
- **Lead Game Designer:** DESIGN.md ice-to-water table (boiling-point row), polar frost wording, carbon-neutral wording, label as invented (60% storm solar cut, frost rates, drift-event sizes), story bible lines (COSPAR does not require secrecy, life-stage examples, puddle line, Mensah's coal/oil/gas line), sound wording, weather mast and instruments. Choose the ice–albedo size.
- **Lead Artist:** flame bands (none below 14%; struggling 14–17%; small 17–19%; a minimum-pressure guard); aurora covers the sky and scales with the solar spike only; Phoenix citation; air column "about 2×"; wisps tied to the boiling point; mast fixes (spirit thermometer, gauges not tinted tubes); label the stylized sky; mark the 1.5 µm dust size unverified.
- **Lead Instructional Designer:** F4a trigger (pressure below 5 kPa) and wording; replace distractors in F1, F4a, F9; F6 "reinforcing through the player's rebuilding"; F7 fires at ≥ 5 kPa; F8 respiration note; approved Earth links; adopt the drafted transfer-task answer key; HS C2 uses HS-ETS1-2, not HS-ETS1-3; add the candidate misconception "plants only respire at night".
- **Technical Lead:** the ice check compares local temperature with the boiling point at current pressure (already a Monitor value); update the cost estimate.

## Risks
- F4a anchors the A2 assessment and is broken until the rule and trigger change.
- The flame bands as drafted would teach "fire burns at 10% O2".
- A single internal SME: the verdicts in §11 stay "accepted, pending external review".

## Shared model updates
- **Lesson:** check each physics threshold against the balance curve. A threshold the curve passes in the first sol hides that regime and any forecast built on it.
- **Term:** "boiling point at current pressure" is the one derived value behind the ice states and heavy vapor wisps.
- **Expectation:** every number in a doc is tagged sourced, calculated, invented (balance), or unverified.
