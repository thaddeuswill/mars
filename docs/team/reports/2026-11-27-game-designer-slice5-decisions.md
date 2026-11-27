# Report: Lead Game Designer, slice 5 decisions (2026-11-27)

Docs only. Nothing was run or playtested; this is a design walk-through (as a 12-year-old new to modpacks, and as an experienced 17-year-old) plus arithmetic. All numbers are labelled invented or calc in the files. Status of every decision: **proposed** until the owner accepts.

## Owner-facing summary

Slice 5 can be built now. Short version of my seven calls:
1. **Oven draw 1 power unit per sol** (invented; a quarter of the Outgasser). Under-powered Ovens scale down by the share of power met, the same rule as the Outgasser.
2. **Starting Ovens go in the player's inventory** on first arrival (once per world); the player places them. No dome structure exists to pre-place them in, and placing teaches "machines need power".
3. **Power shortage: equal share for now** (nothing else draws power yet). Later, life support first, Ovens second, Outgassers last.
4. **Stalling below 5 kPa with one weak machine is intended.** It is the point of the frost sink (the planet pushes back). The Monitor says "Levelling off: the frost takes back about as much as your machines add." The gate is never lowered.
5. **Frost line** "Polar frost: -0.09 kPa per sol", hidden at 0.60 kPa where it is zero, so it appears (about 0.85 kPa) just after your first Outgassers run.
6. **Oven water is out.** The tooltip says "Water comes later."
7. **One hold rule for all holds, including Dome stable:** the count pauses, and resets only after more than 1 sol out (classroom 0.5 sol). The Technical Lead already built this.

Decision for you: accept, or change any of 1 to 7. The biggest design consequence is call 4: a player with one panel and one Outgasser reaches Phase 2 slowly (stalls near 5.6 kPa, 0.6 above the gate), and one with a dusty panel does not reach it at all until they add a machine.

## 0. Handoffs received

| From | Handoff | Answer |
|---|---|---|
| Producer checkpoint, section 4 | Decisions 1 to 6 and one hold-failure rule (R-64) | **Accepted and answered**, balance sheet 2c and ui-specs 9 |
| SME frost report | Frost cause-line rules and the stall decision | **Accepted and answered** (calls 4 and 5). I used the SME's read-more wording and kept "about a quarter" |
| Owner decisions (BACKLOG bottom) | All three temperature branches as data and unit tests, nothing tuned in play; creative bypass stays | **Accepted.** The model table in 2c lists the three branches and says only the cold branch is reachable in play; no tuning proposed except as a named first lever if playtests show a stall |

## 1. Done

- `docs/design/balance-sheet.md`: new **section 2c** (status proposed): Oven draw and under-power, starting Ovens, shortfall priority, stall table, Oven water, frost sink model (three branches, baseline as data, scaling), one hold rule. Pointer added to the §4 hold rule.
- `docs/design/ui-specs.md`: §3 Dome stable row now uses the same hold rule; §9 slice 4 "pick one" note marked resolved; new **Slice 5 wording** table (cause lines for Oven and frost, release line, read-more, "levelling off" line and its high-school variant, Oven tooltip, paused behaviour).

Key arithmetic (calc, from the balance-sheet rates; not run in the game): equilibrium P_eq = 0.6 + 50 x S with the 2 % cold sink. Ovens alone settle at 1.6 kPa. One clean panel with one Outgasser and two Ovens gives a total of +0.10 kPa/sol (share 83 %) and settles at 5.6. A 60 % panel with one Outgasser settles at 4.35; an 82 % panel with the Ovens at 4.7. Producer's figures confirmed. I corrected one of my own numbers during the work (a 1.5 % sink stalls at about 7.3, not 7.1).

Design walk-through notes:
- **12-year-old, new to modpacks:** gets two Ovens, places them, sees "Regolith Ovens: +0.01 per sol" and a tiny rise (the cause-and-effect the flat Phase 1 start was meant to show). Dome stable then gives the Outgasser; the frost line appears as pressure climbs, with the Monitor naming it. Risk: never placing the Ovens (mitigated by the "Nothing is changing this right now." line and the Landing step 6 change below).
- **Experienced 17-year-old:** sees that one machine plateaus, reads the levelling-off line, adds a panel and a second Outgasser. The plateau is a readable lesson in balancing feedback (B2), not a wall.

## 2. Decisions needed

| # | Decision | Type | Recommendation |
|---|---|---|---|
| 1 | Accept calls 1 to 7 as the slice 5 design (they are only proposed) | Ill-structured (fun vs pressure-needs-upkeep vs a 12-year-old's patience) | Accept; playtest decides the sink rate later, not the gate |
| 2 | Start Ovens as inventory items (call 2) vs pre-placed | Ill-structured | Items; pre-placing needs a dome structure that does not exist |
| 3 | Landing step 6 wording change ("battery and Ovens") | Well-structured once the owner agrees | Accept; handoff to the Instructional Designer |

## 3. Handoffs

> **To:** Subject Matter Expert · **Problem type:** well-structured
> **Problem:** confirm the frost sink semantics the design now relies on.
> **Context:** balance sheet 2c "Frost sink model"; `docs/science/frost-sink-review.md`.
> **Needed from them:** (a) the sink and release run on total pressure above baseline in Stage 2, switching to CO2 partial pressure later: confirm the Stage 2 simplification is acceptable for the journal; (b) boundary choice at -40 C and -20 C (inclusive/exclusive) is a design choice, confirm no physical objection; (c) the "buried 0.5 kPa released once" wording and that the reservoir model (stored then returned) matches "reversible"; (d) the high-school line "The two balance near X kPa" is true as stated for the simplified model; (e) pace-scaling sentence for the educator guide (Q2 text).
> **Done when:** each item is marked confirmed or changed in the SME review.

> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** build slice 5 to 2c.
> **Context:** balance sheet 2c; ui-specs 9 slice 5 wording.
> **Needed from them:** `regolith_oven.json` (ratePerSol 0.01, drawPerSol 1.0); a `PowerGridTest` case for Outgasser plus two Ovens on one clean panel (share 83 %, total +0.10); `baselineKpa` per planet; reservoir state and schema bump; three sink branches with unit tests at both sides of -40 C and -20 C and at P <= baseline (never adds air); sink and release scaled by pace and group, buried 0.5 kPa not scaled; a 2 Oven first-arrival grant (once per world); Monitor lines and the levelling-off rule; `resetAfterSols` as data for the hold rule (confirm slice 4 matches; classroom 0.5 sol); re-estimate (Producer guessed 3 to 4 dev-days, unverified); compare the prototype curve to the §2 table cell by cell.
> **Done when:** tests above pass and the checklist rows in BACKLOG CX-3 are updated by whoever owns them.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** grade 6 to 7 and CR-F9, CR-F10 check of the slice 5 wording; Landing step 6 "battery and Ovens".
> **Context:** ui-specs 9 slice 5 table; DESIGN.md Landing (8 steps).
> **Needed from them:** pass or edits; confirm the Landing change does not break the 15 to 25 minute target.
> **Done when:** keys confirmed.

> **To:** Producer / coordinator (not touched by me: `docs/tech/stage2-plan.md` row 5, BACKLOG CX rows) · **Problem type:** well-structured
> **Problem:** plan row 5 omits the Oven, the reservoir state and the hold rule; CX-3 checks reference "Oven low power".
> **Needed from them:** update row 5 to point at balance sheet 2c; set the Oven draw to 1.0 in the CX-3 checklist; reword the plan's "SME curve table" to "balance-sheet solo curve" (SME item).
> **Done when:** row 5 and CX-3 match 2c.

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** none needed now; the Oven tooltip and the frost line need no new art.

## 4. Risks

- **Stall risk (R-61 area):** call 4 makes a single-machine player stall. If playtesters feel stuck, the first lever is the sink rate (1.5 % stalls at about 7.3 kPa with one clean-panel Outgasser; calc), never the gate. Not tuned in play, per the owner.
- **Power as a hidden gate:** one Outgasser plus two Ovens already needs 6 units against 5 from one panel. Check in the prototype that the second panel does not feel like grind before Phase 2 (balance sheet §2 note).
- **Early curve cells unverified** (SME): the 8 h Phase 1 target rests on them. My "Ovens alone give 0.77 at sol 9" (calc) is close to the table's 0.74 but not proof.
- **Net-loss simplification:** the sink can teach "Mars loses air to the poles for good". The read-more states it is reversible; keep it with the line.
- **Landing is a stable-ish element:** the step 6 change needs the owner's nod.
- Unverified: every number above is invented or calc, not run in game; the "0.005 kPa/sol" levelling-off threshold and "about 0.85 kPa" first appearance are invented/calc.

## 5. Shared model updates

- **Decision (owner: `docs/design/balance-sheet.md` 2c, proposed, for the Decision Log):** one hold rule for all holds (pause, reset after more than 1 sol out; classroom 0.5 sol); retires "reset at once".
- **Decision (proposed):** machines share power equally in slice 5; tiered priority (life support, Ovens, Outgassers) when life support becomes a load.
- **Decision (proposed):** a stall below a gate with too few machines is intended pressure-needs-upkeep; gates and thresholds are never lowered to fix it.
- **Decision (proposed, SME-confirmed):** pace scales sources and sinks together; equilibrium values never scale.
- **Term:** "levelling off" = the net trend is near zero because the sink balances the sources (B2).
- **Lesson:** when two documents disagree on a rule (hold failure), the Designer must pick one and edit both the same day, or the Technical Lead builds a third.
