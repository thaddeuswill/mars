# Report: Game Designer, life-support numbers applied (2d final), 2026-11-27

Docs only. Nothing built or run in the game. Arithmetic is **calc** (throwaway script outside the game).

## 0. Handoffs received
- SME (`2026-11-27-sme-life-support.md`): apply tags and the accepted numbers, rename Faint to Blackout, check Oven water vs hydrosphere. **Accepted, done.** Owner accepted all recommendations (BACKLOG, 2026-11-27).

## 1. Done
- `docs/design/balance-sheet.md` 2d: status **owner-accepted**; O2 use 8, maker 10, start fill 14, reserve 24; 5-player wall tank 5.0 sols (solo 25); SME tags (verified range, below ISS, invented plausible, time compression, game rule); MOXIE 300 W verified and "real minimum about 5 kWh per kg" added; Faint renamed Blackout and marked a game rule; new 2d.9 game-rule label list (blackout, free suit refill, power discount, hygiene water omitted, leak larger than ISS worst case, plus Dehydrated, Landing grace, 90 % reclaimer).
- Rechecked 2d.7 (calc, not run in game):

| Mode, n | Condition (sols after t0) | Dome stable sol / h | Tank empties (sols) |
|---|---|---|---|
| Std 1 | 5.0 | 9.2 / 3.07 | never |
| Std 2 | 6.5 | 10.7 / 3.57 | 4.7 |
| Std 3 | 6.0 | 10.2 / 3.40 | 10.5 |
| Std 4 | 7.0 | 11.2 / 3.73 | 4.7 |
| Std 5 | 6.6 | 10.8 / 3.60 | 7.0 |
| Classroom 3 to 5 | 0.3 | 2.5 / 0.83 | 4.7 to 10.5 |

  Solo and classroom unchanged (use, maker, fill moved together). Groups moved 0.05 to 0.07 h later. n = 4 is 0.02 h under the 3.75 h exit tolerance: tight, flagged. Pressure-curve check (2d.7) is unaffected (solo timing same).
- `DESIGN.md` line 716: "real player count" -> "configured group size (`group.size`, not who is online)", matching balance sheet 5.1.
- **Oven water vs hydrosphere:** no double count today. `pack/data/mars/machines/regolith_oven.json` contributes pressure only; `parameters/hydrosphere.json` has no Oven source; 2c decision 6 says Oven water is not built. I added a rule in 2d.2 (and the spec): Oven water is a dome stock and is never added to the hydrosphere index.
- New `docs/tech/life-support-spec.md`: sealing (cap 2,000, full-block-only airtightness, proposed), `life_support.json` keys, machine files (oxygen maker, reclaimer, tanks, hull plate, airlock, Oven and Outgasser edits with `priority`), recipes standard and classroom (hull plate, tank, airlock are new **proposed** invented costs), the `all_of` gate with the four states, and which numbers scale with `group.size` only (use, start fill, pre-built makers, reserve; nothing scales with pace except `reserveSols`).

## 2. Decisions needed
None from the owner. (Technical Lead confirmations below.)

## 3. Handoffs
> **To:** Technical Lead · **Problem type:** ill-structured (partial-block airtightness trades builder freedom against a simple, testable rule) / well-structured (cap)
> **Problem:** confirm the 2,000-block cap and "full block or nothing" airtightness; confirm key names.
> **Context:** `docs/tech/life-support-spec.md` section 1.1.
> **Your view:** full-block-only is explainable to a 12-year-old and cheap to test; cost is surprise for slab builders, softened by naming the leak position. Alternative: per-face shape checks (accurate, hard to explain, costly). Recommend full-block-only.
> **Needed from them:** confirm or change S1 and S2; map recipe stand-ins to real items.
> **Done when:** spec marked confirmed and balance sheet 2d.3 gets the three new recipe rows.

> **To:** Instructional Designer · **Problem type:** well-structured
> **Problem:** reading-level pass on Blackout and game-rule journal lines; add the misconception "oxygen is easy to make on Mars".
> **Context:** balance sheet 2d.6, 2d.9; `docs/science/life-support-review.md` 3 and 4.
> **Done when:** strings accepted.

> **To:** Producer / owner · **Problem type:** well-structured
> **Problem:** NASA hosts blocked for the SME; 0.84 kg, ISS recovery and suit hours remain "search-summary". Someone should open the links in the review before the journal says "verified".

## 4. Risks
- n = 4 standard Dome stable at 3.73 h, 0.02 h under the tolerance.
- Transfer error "oxygen is easy" from the power discount; mitigated by the label list.
- The sheet's section 5.3 check line should read "sol 2.5" (unchanged from before; still to edit when someone owns 5.3).
- Recipe costs for hull plate, tank, airlock are invented and untested.

## 5. Shared model updates
- Term: **game rule** = anything the game does that real life does not; labelled in the journal (list in balance-sheet 2d.9).
- Decision (owner-accepted): O2 use 8, maker 10, start fill 14, reserve 24 per player; out of air = Blackout. Owning document: balance-sheet 2d.
- Lesson: when use, output and start fill move together, solo and classroom timing do not change; only group builds shift.
