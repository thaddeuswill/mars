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
