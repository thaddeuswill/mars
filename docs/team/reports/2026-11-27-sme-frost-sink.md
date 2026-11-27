# SME report: polar frost sink (Stage 2 slice 5)

Full review: `docs/science/frost-sink-review.md`. Docs only. This is a desk review: no game run, no playtest.

## 0. Handoffs received
Four SME questions from `2026-11-27-producer-stage2-checkpoint.md` section 2. All accepted and answered.

## 1. Done
- `docs/science/frost-sink-review.md`: answers 1 to 4 with sources and marks.
- Answers in short:
  1. **Sink base.** Physically it acts on CO2 only. "Pressure minus 0.6" is an acceptable approximation in Stage 2 (CO2 not tracked separately), over-applying up to about 40 % in Phase 2 where 12 of 30 kPa is filler (calc). When CO2 partial pressure is live, apply the rate to it. Real cycle is seasonal and reversible, about a quarter of the air, up to about 30 % (NASA Curiosity page says 30 %; a review says up to 25 % southern winter; sources differ, so write "about a quarter"). Rates and temperature thresholds stay labelled **invented**; the game's 2 % per sol is about 25 to 30 times faster than the real swing (calc).
  2. **Scaling with pace is honest and required.** Scale both sources and sink so the stall pressure (S/k) is the same in every mode; otherwise a physical value moves with pace (breaks I-1). Safe journal and educator wording is in section Q2 of the review; do not say "frost reacts faster in classroom mode".
  3. **Curve table.** No "SME curve table" exists; it means the balance-sheet Standard solo curve (section 2). Recomputed sols 16 to 25: the implied source is 0.419 to 0.425 per sol against 0.42 expected, so **those cells already include a -2 % sink**. Sols 1 to 16 and Phases 2 and 3 are not reproducible from the text (my plain run gives 5.83 at sol 25, not 5.20): **unverified**.
  4. **Read-more (MS):** "On real Mars, about a quarter of the air freezes onto the poles each winter. In spring the dry ice turns back into gas, so the air pressure goes back up." HS extra line is in the review.
- Network limit: science.nasa.gov and usgs.gov did not open; NASA facts are from search summaries on allowed hosts.

## 2. Decisions needed
| # | Decision | Type | Recommendation |
|---|---|---|---|
| 1 | Reword the plan's check to "balance-sheet solo curve" | Well-structured | Yes |
| 2 | A single Outgasser on 75 % power stalls at 4.35 kPa and never opens Phase 2 (calc). Intended? | Ill-structured (fun vs pressure-needs-upkeep, Game Designer and owner) | Keep upkeep idea but make the stall visible with a Monitor cause line; or soften the sink for the first prototype. Playtest decides |

## 3. Handoffs
> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** Baseline 0.6 hard-coded; sink base is total pressure.
> **Context:** `docs/science/frost-sink-review.md` Q1.
> **Needed from them:** per-planet baseline data field; later switch to CO2 partial pressure when it is tracked; run the sols 1 to 16 curve cells in the prototype and report them against the table.
> **Done when:** the prototype curve is compared cell by cell and differences are explained.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** Check grade 6 to 8 wording and CR-F9, CR-F10 for the frost read-more and the pace notes.
> **Context:** review sections Q2 and Q4.
> **Needed from them:** pass or edits.
> **Done when:** text accepted for the journal and educator guide.

> **To:** Lead Game Designer · **Problem type:** ill-structured (see decision 2)
> **Needed from them:** frost cause-line rules (hide at 0.6 kPa where the sink is 0) and the stall decision.

## 4. Risks
- Seasonal fraction figures differ between sources; wording must stay "about a quarter".
- Early solo-curve cells unverified; the 8 h Phase 1 target rests on them.
- Net-loss sink is a simplification that could teach "Mars loses its air to the poles for good"; the journal notes must say the real cycle is reversible.

## 5. Shared model updates
- Lesson: "SME curve table" does not exist; name the file and section when a plan refers to a curve.
- Decision proposed (owner: balance sheet, for the Decision Log): pace scales sources and sinks together; equilibrium values never scale.
