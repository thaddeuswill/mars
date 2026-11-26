# Report: User Decisions and Handoffs Applied (Design)

- **Role:** Lead Game Designer
- **Date:** 2026-11-26
- **Outputs:** `DESIGN.md`, `docs/design/balance-sheet.md` (new), `docs/design/story-bible.md` (draft 2), `docs/design/playtest-plan.md` (new)
- **Note:** Design work only. All balance numbers are untested starting values for the Stage 2 prototype.

## Handoffs received
| Item | Response | Result |
|---|---|---|
| C-01 (blocker) | Accepted, fixed | **Regolith Outgasser** unlocked at Dome stable (+0.10 kPa/sol each), plus a starting oven trickle and a "polar frost" sink. Reaches 5 kPa at about sol 25 (~8.3 h solo). |
| M-02 | Accepted | Thresholds never scale. Classroom mode uses a rate multiplier (4.0), shorter holds (1/2/3 sols), and cheaper recipes. Group of 4: ~11.3 h over ~17 play periods. |
| M-13 | Accepted | KubeJS row fixed. |
| M-16 | Accepted | Breathable and Equilibrium holds run at the same time (14 sols). Short dips pause the counter. Drift events every 1–1.5 sols; beacon parts can be built during holds. |
| m-13 to m-17 | Accepted | Ice-to-water rule drafted, pending SME. |
| SME items | Accepted, all applied | Breathable needs O2 partial pressure ≥ 16 kPa, total ≥ 75 kPa, CO2 partial pressure ≤ 0.5 kPa, held 7 sols. Hydrosphere is % of surface. Emitters use super-greenhouse gases. All optional items were adopted after scoring them on the fun–learning matrix. |
| User decisions | All applied | Fauna section rewritten as a decision: life stages, machine signature, four player options (defend, shrouds, lures, quiet zones), unranked ending cards. New Group Play section. |

## Fixes and finds
- Story bible physics error fixed: beat 9 showed ice turning to vapor at the Thaw, but the pressure there is already ≥ 5 kPa.
- At 75 kPa, the breathable O2 window is narrow (21.5–23%). That's a real trade-off (B4), but it may be hard to read.

## Decisions needed
| Decision | Recommendation | Type |
|---|---|---|
| Multiplayer scaling: a static group-size rate scale replaces the "threshold config" | Confirm (accuracy: thresholds never scale) | Well-structured |
| Group size comes from a static setting, not from who's online | Confirm | Well-structured |

## Handoffs
- **Lead Instructional Designer:** alignment check for changes to goals A1, A2, A5, B1, B2, B4, B5, C1, C2, C3, D1, D2; update the LEARNING.md §5.1 classroom table.
- **Technical Lead:**
  - Pace configs and a static group size.
  - Sol definition.
  - Derived values on the Monitor.
  - Hold counters.
  - A per-block ice check (performance).
  - Fauna signature and cap.
  - Ending-card state.
  - Shift Board.
  - One Index per group planet.
- **Subject Matter Expert:**
  - Ice-to-water rule.
  - Polar frost.
  - Ice–albedo size.
  - Ground collapse.
  - Perchlorate.
  - Carbon-neutral ecosystems.
  - Story bible [verify] tags.
- **Lead Artist:** visible cues for:
  - The Outgasser.
  - Ice states.
  - Ground collapse.
  - Shrouds and quiet zones.
  - The thawling life stage.
  - The Shift Board.
  - Monitor milestones.
  - The partial-pressure display.
  - Ending cards.
- **Producer:** update the roadmap for the new content and run a scope check; link the new design docs in the README.

## Risks
- Scope: perchlorate, ground collapse, non-lethal options, drift events, Shift Board. Cut first: the perchlorate soil stage, then lures.
- 75 kPa makes Phase 3 longer and could feel like a grind.
- The narrow O2 window may be hard to read.
- The classroom model depends on untested speed-up assumptions.
- Solo adult playtests can't measure learning gain or age fit.
