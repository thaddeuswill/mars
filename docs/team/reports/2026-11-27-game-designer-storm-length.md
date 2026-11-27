# Report: Lead Game Designer, storm length (2026-11-27)

Docs only. Desk check of documents; nothing run. Settles Stage 3 plan task 2 (R-61 related).

## 0. Handoffs received
`docs/tech/stage3-plan.md` task 2: pick one storm duration; curve (2 sols) vs drift table (0.5 to 1 sol). Accepted.

## 1. Done
- Decision: the two numbers described two different storms. **Phase 1 storm = 2 sols standard, 0.5 sol classroom** (the curve was right for it). **Hold drift-event storm = 1 sol standard, 0.25 sol classroom** (the drift table needed shorter, but 0.5 to 1 was the old range; now one value).
- Frequency: first storm not before sol 14 (classroom sol 4); gap 12 to 20 sols (classroom 3 to 5), seeded. Warning 1 sol ahead. Solar cut 60 % x storm cap kept; dust stage +1 (+2 if cap >= 0.8) at storm end kept. All invented, tagged so.
- Edited `docs/design/balance-sheet.md`: new "Storm schedule" table in section 2 (after the storm cap paragraph); drift-table storm row (section 4) now says "x storm cap, 1 sol" and points to it. The curve (2-sol storm on sols 14 to 15) is unchanged.
- Edited `docs/design/prototypes/first-dust-storm.md`: warning text "about 2 sols"; paper storm runs 6:00 to 14:00 (the run is about 18 min, not 15).
- Why 2 and not 0.5 to 1: a 1-sol storm is 20 min of play; the battery lesson (C1) and the Monitor dip need time to read. Why not 2 in holds: drift events come every 1 to 1.5 sols.

## 2. Decisions needed
- Ill-structured, low stakes: is "two storm kinds" acceptable, or should every storm be 2 sols? Recommend two kinds (keeps holds playable). Revisit if playtests show hold storms feel trivial.

## 3. Handoffs
> **To:** Subject Matter Expert, well-structured. **Needed:** mark verified / not verified: real regional storms last days to weeks, global months; Opportunity -80 % (2007), -97 % (2018); InSight about -35 %; confirm the 60 % cut is a fair game value and that "game storms are much shorter than real" is honest. Currently **searched, not verified**. **Done when:** marks added in `docs/science/dust-review.md`.

> **To:** Technical Lead, well-structured. **Needed:** `pack/data/mars/hazards/dust_storm.json` uses the table values (duration, gap, first-not-before, lead; standard and classroom); content test against the balance sheet; fix `docs/tech/stage3-plan.md` line 53 (open question now resolved) and task 2 wording. **Done when:** test passes.

> **To:** Lead Instructional Designer, well-structured. **Needed:** F1 and journal wording must not state a length in sols; check `prompts-and-reflections.md` for "half a sol". **Done when:** grep clean.

## 4. Risks
- Story order lists the first storm before Dome stable (`phase-1.md` line 5), but the curve puts it on sol 14, after Dome stable (sol 9). The storm table follows the curve; the order line needs a fix or the curve needs a different storm sol. Owner or Producer to confirm.
- A 2-sol storm may drain a one-battery grid in classroom if demand is high: physical, not scaled; check in the slice test.

## 5. Shared model updates
Decision (owner: `balance-sheet.md` section 2 storm schedule): Phase 1 storm 2 sols (classroom 0.5); hold storms 1 sol (classroom 0.25); cut and dust never scale with pace.
