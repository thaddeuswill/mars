# Report: Lead Game Designer, panel dust mechanic (BACKLOG #22)

- **Role:** Lead Game Designer
- **Date:** 2026-11-26
- **Outputs:** `DESIGN.md` (new subsection "Solar panel dust" under Hazard Systems), `docs/design/balance-sheet.md` (new §2a), `docs/design/ui-specs.md` §2 (new progressive-disclosure row)
- **Note:** Design draft only; not playtested. **Every number is invented for game balance; none is a real figure yet.** The Opportunity 2014 wind cleaning is cited from an unopened search summary.

## Handoffs answered
- **Lead Artist, "does 'partly cleaned' belong in the mechanic?"** Yes: it is the state a dust devil leaves behind. The Game Designer recommends option C (even veil for stage 4, streaks for "partly cleaned").
- **Lead Artist, "which process does the Phase 1 oxygen generator use?"** The dome unit is MOXIE-style: it makes O2 from Mars's CO2. It only feeds the dome and has no effect on the Planetary Index. It is a different machine from the Phase 3 fallback, which splits water at planet scale. Proposed names: **"oxygen maker (CO2)"** and **"water electrolysis plant"**, so two machines aren't both called "O2 generator".

## Mechanic (draft)
- **What adds dust:** time on the surface; dust storms (added when the storm ends); the player's own digging or a running Outgasser intake nearby.
- **What removes dust:** a panel brush (a trip outside), a panel wiper (automatic, from Phase 2), a rare dust devil, Phase 3 rain.
- **Stages:** 0 to 4, plus "partly cleaned".

| Item | Draft value (all invented) |
|---|---|
| Stage interval, standard | 3 sols per stage, divided by the storm cap and by the rate multiplier |
| Stage interval, classroom (×4) | 0.75 sol |
| Storm | +1 stage at storm end; +2 while the storm cap is ≥ 0.8 |
| Local digging or Outgasser intake | Dust builds twice as fast within 8 blocks |
| Output by stage | 100 / 92 / 82 / 70 / 60 % (stages 0–4); 85 % partly cleaned |
| During a storm | stage factor × (1 − 0.6 × storm cap); worst case 24 % |
| Panel brush | Back to stage 0; 64 uses |
| Panel wiper | 1 power per sol per panel, plus a bearing every 10 sols |
| Dust devil | 5 % chance per sol per panel group |

- **Monitor:** a "Panel dust −n %" cause line appears at the first dust stage, separate from the "Storm: less sunlight" line.
- **Goals:** C1 (plan power), B1 (cause and effect on the cause line), B5 (unintended consequences), C2 (diagnose and fix).

## Forecast and beat checks
- **F1** still correct: storm dust is added at the storm's end, so nothing about the incoming storm lowers output before F1 locks at onset; earlier dust drift shows under its own cause line.
- **FI-1 and FI-4** still correct: a storm still cuts solar on top of any dust.
- **Threshold-vs-curve:** the Phase 1 pressure curve now assumes panels are cleaned about every 6 sols (about 90% average output). The classroom Phase 1 check is already slow (2.7 h against a 2.4 h target) and dust could slow it further; flagged for the prototype.

## Decisions needed
None new. The owner's earlier choice (even veil plus a "partly cleaned" state) matches the design recommendation. The two proposed machine names are logged as a default pending the owner.

## Handoffs
- **Subject Matter Expert:** real dust accumulation rates on Mars arrays (output loss per sol); real wind-cleaning events (Opportunity 2014, Spirit) with sources; how dust devils clean panels; whether storms deposit most of their dust as the air clears; whether rain washing panels is fair; a check on "your own digging dusts your panels". Done when figures are sourced or marked "real value missing" and the direction of each invented number is confirmed.
- **Lead Artist:** in storm step 5 (keyframes §3.7a) panels **don't change stage during the storm**; they gain their stages **at clearing** (step 7), and the airborne-dust overlay carries the look during the storm; a small per-panel stage bar using shape, not colour only; brush and wiper art rows in the asset list; rebuild the dome oxygen maker as the MOXIE gold box.
- **Lead Instructional Designer:** alignment check for C1, B1, B5, C2; is "cleaned before a forecast storm" a usable C1 evidence event?
- **Technical Lead:** cost estimate for per-panel dust state (block state), the dust-devil roll in simulation time, the dust cause line, and two new machines (brush and wiper).

## Risks
- **Chore risk:** cleaning could feel like busywork. Guard: under about 1 minute of cleaning per sol of play. The mechanic sits in the "Moderation" quadrant of the fun–learning matrix until playtests say otherwise.
- **Slower Phase 1:** dust may push classroom Phase 1 further past its 2.4 h target.
- **Harsh worst case:** a fully dusted panel in a Phase 1 storm drops to 24% output, which could cause dome failures for new players.

## Shared model updates
- **Terms:** panel dust stage (0–4, plus partly cleaned); dust devil (a rare event that leaves the partly cleaned state); oxygen maker (CO2) vs. water electrolysis plant (names proposed).
- **Decision:** storm dust is added at the storm's end, never before, which keeps the F1 lock point clean (DESIGN.md, balance sheet §2a).

---

## Addendum: SME refinements applied (filed by coordinator)

All six refinements from `docs/science/dust-review.md` applied to `DESIGN.md` ("Solar panel dust") and balance sheet §2a; every row tagged sourced, searched, calculated, or invented. Document work only; not playtested; no other role's files edited.
- Dust devil chance 5% → **about 1% per sol** (a standard game gets about one lucky cleaning instead of about six, calculated).
- Three sols per stage kept and labeled **"about 15 times faster than real (0.2% per sol)"**; later phases are slower because the interval is divided by the storm cap (about 1.7% per sol in Phase 3, 0.8% at Equilibrium); the Phase 2 interval is explicit at 3.75 sols.
- Phase 3 rain takes a panel to **stage 1 or partly cleaned, whichever is cleaner**, never stage 0 (about −8% on Phase 3 solar).
- "Outgasser intake" renamed **heavy machinery and machine exhaust**; the ×2 dust rate stays, marked invented.
- Partly cleaned panels **recover half the lost output** (60 → 80%, 70 → 85%, 82 → 91%), replacing the flat 85%.
- A journal note that the 60% storm floor is optimistic (InSight fell to about 10%).
- "Partly cleaned" is described as patchy and different from panel to panel; "dust is added at the storm's end" is labeled a design choice, not science; the brush and wiper are marked fiction (no Mars mission has flown either).
- **Re-run checks:** F1, FI-1, and FI-4 unaffected; a new "Panel dust stages" row in the threshold-vs-curve table passes, with the Phase 1 cleaning assumption still to be measured in the prototype.
- **Handoffs:** Instructional Designer (place the SME's journal notes and the D2 Earth link; confirm "cleaned before a forecast storm" as C1 evidence); Lead Artist (patchy partly cleaned; rain shows as stage 1); Technical Lead (keep dust devil chance, stage interval, rain target, half recovery, and the ×2 radius as datapack values).
- **Risks:** the Lorenz 2021 paper must be opened before any number reaches player text beyond the SME's journal notes; the 15× speed-up could teach "dust kills a panel in days" (the journal note is the mitigation); Phase 1 power with dust is unmeasured.
