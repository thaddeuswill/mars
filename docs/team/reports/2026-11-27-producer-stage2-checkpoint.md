# Report: Producer, Stage 2 checkpoint (2026-11-27)

Role: Producer · Docs only; no Java or `pack/` edits; Codex's CX-1 to CX-5 rows untouched. Nothing was run or playtested; this is a read-through of the repo and the reports. Everything about effort is **unverified**.

## Owner-facing status (one paragraph)

Stage 2 is going fast and the checks are holding: four slices (planet values and Outgasser, pause, power, gates) plus a Monitor clean-up are built, with 96 unit tests and 52 boot checks reported passing, and every slice proved its tests can fail. The honest caveats: we still have no measured effort rate, so the old "26 developer-days" plan is a guess; pressure is not yet on the designed curve (no Oven, no frost sink, "Dome stable" is a one-hour stand-in); the power numbers are invented; nothing has been tested on a real server or after a real restart because that needs your EULA call. Next is slice 5 (polar-frost sink and the Oven), and the Technical Lead is building the Monitor pressure history you chose (#38). I recommend keeping solar-panel dust (about 12 more days of work) out of Stage 2 and in Stage 3. Open for you: the five decisions at the end of section 2.

## 0. Handoffs received
- **Technical Lead (slice 2 report):** ROADMAP Stage 2 rows and R-10/R-11. Accepted: R-10 and R-11 updated (closed for our systems only). The ROADMAP rows are Codex's lane this round (CX status refresh, BACKLOG row 36), so I did not edit `ROADMAP.md`.
- **Technical Lead (machines report, 2026-11-26):** add the panel-dust remainder (about 11.5 to 12.5 developer-days) to the budget. Accepted, in `budget.md` section 6.2, with the overlap with slice 3 flagged.
- **Technical Lead (slice 3):** renumber duplicate BACKLOG rows. Not mine this round: it is CX-4.
- **Artist (monitor mockups):** pay for a stored history? Accepted as scope; see section 2 decision 5.

## 1. Done
- `docs/production/budget.md`: new section 6 "Stage 2 actuals vs estimates" (change log is now section 7). Slices 1 to 4 and the Monitor UI pass: plan estimate vs delivered output (tests, boot checks). **No actual effort was measured** (the reports say "about one session"), so the table says so rather than inventing days. New finding: the stage2-plan total (about 208 h) is 28 h over the Stage 2 Technical Lead row (180 h). Panel-dust remainder (92 to 100 h) and Monitor option A history (placeholder 16 to 24 h, Producer guess) listed as unscheduled items. A forecast (41 to 44 dev-days if everything is pulled in) and a cut recommendation. Section 2 totals are unchanged on purpose (no measured rate yet).
- `docs/production/risk-register.md`: R-10 closed for our systems only (real disk restart moved to new R-62); R-11 closed for our systems, open for Mekanism and Create. New R-60 to R-68: invented power numbers, pressure off the curve, EULA/real restart gap, creative gate bypass, hold-failure rule conflict, Monitor covers centre (accepted: you said you like it), reference images internal-only, Stage 2a paper prototypes still unchecked, no measured effort rate. Top-risks table gets R-61, R-62, R-68 (score 6).
- Slice 5 scope check (below).

## 2. Slice 5 scope check (polar-frost sink, Regolith Oven power draw)

Compared `docs/tech/stage2-plan.md` row 5 and the slice 3 and 4 reports with `docs/design/balance-sheet.md` sections 2, 2b and 5.2 and ARCHITECTURE 2.1. Plan row 5 reads: "Polar-frost sink coupling (-2 % of (pressure - 0.6) per sol below -40 C) ... Monitor shows it as a negative cause line", 2 dev-days. The Oven power draw is not in the plan row at all (slice 3 report: "not until slice 4/5").

**What the balance sheet says that the plan row does not cover**

| # | Gap | Why it matters |
|---|---|---|
| 1 | **Frost is a reservoir, not just a sink.** Balance sheet section 2: -2 %/sol below -40 C, -1 %/sol from -40 to -20 C, above -20 C it returns what it took at +0.5 kPa/sol, then up to 0.5 kPa more from buried CO2. Returning "what it took" needs a stored amount. | New saved state and a schema bump (now schema 3). The plan row names neither. |
| 2 | **Temperature never moves yet.** `temperature.json` starts at -60 C with no contribution or greenhouse coupling in slices 1 to 4. | Only the "-2 % below -40 C" branch is reachable in play. The -1 % band and the thaw return can only be tested by `/mars index set temperature` and unit tests. Is that acceptable for Stage 2? |
| 3 | **Oven draw has no number.** Section 2 says "Low", section 2b (power) has only the Outgasser (4), panel (5), battery (10). There is no Oven machine JSON in `pack/data/mars/machines/`; the block is registered but has no definition. | Needs an invented number, labelled invented. Two Ovens at a small draw can push the one-panel, one-Outgasser grid (5 supply, 4 demand) into shortage, so the slice 3 worked example and tests change. |
| 4 | **Where the two starting Ovens come from.** Balance sheet: "2 in the starting dome", unlocked at Start. In the build, machines register only when placed. There is no dome structure. | Pre-placed at world creation, given as items, or placed by the player? Changes the first-session flow and the Dome stable test. |
| 5 | **Shortfall priority.** Rule (2b): a shortfall cuts every powered machine by the same share. Dome stable is now defined as "the grid covers demand", so adding Oven demand makes the Dome stable hold depend on the Oven too. | Should life support (and Ovens) outrank the Outgasser when short? Design call (C1 "survive first"). |
| 6 | **Does the sink scale with pace and group size?** ARCHITECTURE 2.1 scales every contribution and sink by `pace.rateMultiplier x groupRateScale`. A sink that is a fraction per sol is a rate. | If sources are scaled and the sink is not, the equilibrium pressure moves in classroom mode, which edges toward scaling a physical value (I-1 allows rates only). Both scaled keeps equilibrium identical. The plan does not say which. |
| 7 | **"0.6" baseline** is hard-coded in the formula text. It is the start pressure of this planet. | Should be a data field (per planet), not a constant, since planets are per dimension. |
| 8 | **"SME curve table" in the plan's check ("coupling vs SME curve table") is not identified.** | The only curve in the repo that the sink can be compared with is the "Standard solo curve" in balance sheet section 2 (and 3a). My spot check: it appears consistent with a -2 % sink (sol 22 to 24 climbs about 0.345 per sol against 0.42 source minus about 0.078 sink), but I did not recompute every cell. **Unverified.** |
| 9 | **Existing numbers will move.** Slice 1 to 3 screenshots and tests assert "0.6 to 1.6 in 10 sols", "2.60 after 10 sols", "8.60 at 40 sols", "+0.075". The launch guide and reports quote them. | Tests must compute from the start state (known pitfall, HANDOFF section 6). Owner-visible numbers change, so say so. |
| 10 | **Stalls below 5 kPa.** Equilibrium is 0.6 + net source / 0.02 (my arithmetic, `50 x S`). One full Outgasser with no Oven stalls at 5.6 kPa; with two Ovens at 6.6; one Outgasser on a stage 4 panel (75 %) with no Oven stalls at 4.35 and **never reaches Phase 2**; at 50 % power with Ovens, 4.1. | Is "an under-powered single Outgasser cannot finish Phase 2" intended pressure-needs-upkeep, or too harsh for a 12-year-old? |
| 11 | **Monitor negative cause line:** no wording, no rule for hiding it at 0.6 kPa (sink is zero there), no key. | ui-specs section 9 straw-man needed; wording owner is the Game Designer (grade 6-7 check by the Instructional Designer). |
| 12 | **Oven side effects:** "water for the dome" has no model (hydrosphere or dome water is not built). | In or out of slice 5? Recommend out (name it, do not hide it). |
| 13 | **Estimate.** Plan: 2 dev-days. The list above (Oven machine data, grid demand, state and schema, three sink branches, Monitor, tests, updated tests and screenshots) is more. | Producer guess 3 to 4 dev-days, unverified; the Technical Lead should re-estimate once the decisions are in. |

**What the Game Designer must decide (I have not decided any of these)**
1. Oven power draw number (invented, tagged) and whether an Oven short on power gives its +0.01 scaled by share, like the Outgasser (gap 3).
2. Where the two starting Ovens come from (gap 4).
3. Shortfall priority: equal share, or life support first (gap 5).
4. Whether a single weak Outgasser may stall below 5 kPa on purpose (gap 10), or the sink starts from a gentler value.
5. One wording for the negative cause line and when it shows (gap 11).
6. In or out: Oven "water for the dome" (gap 12).

**What the SME must decide or confirm**
1. Whether the sink and reservoir are applied to the **whole** pressure above 0.6 or to the part that is CO2 (the "0.6 baseline" in physical terms), and the labelled-invented status of the -2 % and -1 % rates (gap 6, 7).
2. Whether scaling the sink with pace (both sides scaled) is physically honest for the journal ("the frost reacts faster in classroom mode" is a pacing device, not physics) and what the educator guide says.
3. Which curve is "the SME curve table" in the plan (gap 8), or confirm the balance-sheet curve.
4. Read-more sentence for the frost line (direction sourced to SME round 2 [S3]; check grade 6-7 wording with the Instructional Designer).

**What the Technical Lead can decide alone (well-structured):** gap 1 (state and schema bump, I-2 style: frost never latches, but must save), gap 7 (data field), gap 9 (update tests and screens).

**Decision for the owner / team (gap 2, ill-structured):** build all three sink branches now (data-driven, unit-tested, only the first reachable in play) or only the reachable one and defer the rest until a temperature source exists. Producer view: build the branches as data and unit tests (small cost, avoids a schema change later), do not tune any of them in play. Trade-off: some code and tests exercise paths the player cannot yet reach.

**Overlap note:** BACKLOG CX-3 (Codex) writes the slice 5 acceptance checklist. This list is the input it needs (gaps 1 to 13); I did not write a checklist and did not touch the CX rows. CX-3's checks cannot be final until decisions 1 to 6 above are made.

## 3. Decisions needed (owner)

| # | Decision | Type | Recommendation |
|---|---|---|---|
| 1 | **Panels.** HANDOFF section 4 still lists "Monitor or solar panels?" and "which anti-UI skills?" as open, but BACKLOG #36 records the answers (Monitor screen; run all UI skills). Is the question closed, and are the solar panels (about 2.1 blocks across, close to real InSight) fine as they are? Separately, BACKLOG #38 says you like the Monitor in the middle, so R-65 is accepted. | Well-structured | Treat as closed; mark HANDOFF section 4 items 1 and 2 answered (Codex lane or coordinator) |
| 2 | **EULA** for a real dedicated server and a real restart (R-62). Only you can accept. | Well-structured (owner's call) | Not required to continue; but until you do, no document claims saves survive a real restart. Cheaper alternative: you quit and reload a singleplayer world once by hand and tell us what you see. |
| 3 | **Creative bypass of gates** (R-63): creative players and `/setblock` skip "Dome stable". Keep? | Ill-structured (teacher control vs your testing) | Keep for the prototype; add a classroom switch as a Stage 3 config item; report which mode each playtest used |
| 4 | **Hold-failure rule** (R-64): a short failure pauses the count and a long one (over 1 sol) resets it (balance sheet), or any failure resets it (ui-specs, style guide). | Ill-structured | Game Designer picks one rule for all holds; Producer leans to the balance-sheet rule (kinder, matches Phase 3 holds), as the Technical Lead does |
| 5 | **Monitor history cost** (#38): the Artist says "one slice", no Technical Lead estimate; Producer placeholder 2 to 3 dev-days with a new saved series and schema bump. Pay it in Stage 2? Fallback is mockup C without lights now and the sparkline in Stage 3. | Ill-structured (look vs cost vs learning) | Pay it if the Technical Lead's estimate is 3 dev-days or less; otherwise the fallback |
| 6 | **Panel dust (about 11.5 to 12.5 dev-days).** In Stage 2 or Stage 3? | Ill-structured (scope) | Stage 3 (budget.md 6.3). It is already in the Stage 3 row, short by 12 to 20 h |
| 7 | **Stage 2a paper prototypes** (ROADMAP, still unchecked) while code is on slice 4 (R-67). | Ill-structured (process) | Accept the order change since you are the playtester; ask the Game Designer for paper findings before Stage 3 |

## 4. Handoffs

> **To:** Lead Game Designer · **Problem type:** ill-structured
> **Problem:** slice 5 has six open design numbers and rules before the Technical Lead starts.
> **Context:** section 2 above; `docs/design/balance-sheet.md` sections 2, 2b, 5.2; `docs/tech/stage2-plan.md` row 5.
> **Your view:** Producer: decide 1 to 4 first (they change code shape); 5 and 6 can follow.
> **Needed from them:** answers to decisions 1 to 6, and one hold-failure rule for all holds (R-64).
> **Done when:** balance sheet 2b has an Oven row tagged invented, and the plan row 5 matches.

> **To:** Subject Matter Expert · **Problem type:** well-structured
> **Problem:** four frost questions (what the sink acts on, scaling honesty, which curve table, read-more wording).
> **Context:** section 2 above; balance sheet section 2 polar frost row [S3], [S4].
> **Needed from them:** a verdict on each, or "unverified".
> **Done when:** each item is answered in a short report.

> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** (a) re-estimate slice 5 after the Game Designer answers, (b) give an estimate and persistence plan for the Monitor history, (c) re-cut the panel-dust remainder against what slice 3 built, (d) add elapsed time and build cycles to each report from slice 5 on.
> **Context:** budget.md section 6; BACKLOG #38.
> **Done when:** the three numbers are in the next report.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** wording check for the frost cause line and the Dome stable "On Earth" trigger (still open from slice 4).
> **Done when:** keys confirmed.

> **To:** Coordinator (Codex lane, not touched by me) · **Problem type:** well-structured
> **Problem:** HANDOFF section 4 items 1 and 2 look answered by BACKLOG #36; stage2-plan slice 5 row should be updated after the Game Designer decides; ROADMAP Stage 2 rows may need the history item.
> **Done when:** the stale lines are fixed.

## 5. Risks
- **Prototype timing misread (R-61):** the owner may take today's pace as the design. Mitigation: label it, tune nothing until slice 5.
- **Effort is unmeasured (R-68):** four slices in a day cannot be converted to a schedule. Do not shrink Stage 3 or 4 from it.
- **Stage 2 over-budget if all is pulled in:** about 41 to 44 dev-days against a 180 h row, if panel dust and history both come into Stage 2.
- **Unverified claims in this report:** all estimates; the equilibrium arithmetic (my calculation from the balance-sheet rates, not run in the game); the curve spot check.
- **Docs only:** two files outside my ownership were already modified in the working tree by the Technical Lead (Monitor history work); I did not touch them.

## 6. Shared model updates
- **Term:** "prototype timing": pace and pressure numbers seen before slice 5 and before life support is a real loop; never used to tune rates or holds.
- **Lesson:** when a role reports effort as "one session", the Producer's actuals column must say "not measured" and show output, not invent days. Add elapsed time and build cycles to reports.
- **Lesson:** a plan row can pass review while missing state, scaling and who places the machine; the Producer should check each plan row against the balance sheet before the build, as done here for slice 5.
- **Expectation:** HANDOFF section 4 (owner questions) must be reconciled with BACKLOG when the owner answers, or the next worker re-asks.
