# Report: Lead Instructional Designer, solar panel dust (BACKLOG #26)

- **Role:** Lead Instructional Designer
- **Date:** 2026-11-26
- **Outputs:** `LEARNING.md` (alignment table, §3.1, §3.8, §5.5), `docs/learning/journal-dust.md` (new), `docs/learning/prompts-and-reflections.md` (§1.4, §4), `docs/learning/content-rules.md` (new CR-F10)
- **Method:** document walkthrough of DESIGN.md "Solar panel dust", balance sheet §2a, and `dust-review.md`; nothing built or playtested; no other role's files edited.

## Result
The dust mechanic keeps C1, B1, B5, C2, and D2 taught, practiced, given feedback, and measured, and adds teaching to each. The forecast re-check found one real problem: **FI-4 gives the wrong answer for a player who cleans their panels during a Phase 3 storm.**

## Alignment
| Goal | What dust adds |
|---|---|
| C1 | Power drift between cleanings and cleaning before a warned storm; a new formative indicator |
| B1 | Two separate power cause lines on the Monitor ("Panel dust −n %" and "Storm: less sunlight −n %") plus a new "What caused it?" item for power drops (`explain.power_drop`) |
| B5 | Minor: the player's own digging and exhaust dust nearby panels faster (an invented ×2 rule, labeled unmeasured on real Mars). "Cleaning before a storm" is **not** B5 evidence; B5 stays measured by Report 2 |
| C2 | Dust as upkeep with a route choice from Phase 2 (brush costs time; wiper costs power and parts). **Panel dust is not a failure type**, so it never counts as a repeat failure |
| D2 | A new Earth link: desert solar farms and dust |

Two stale "credit goes to the claimer" lines in LEARNING.md were fixed (round 3 decided the repairer gets the credit).

## Journal notes (`docs/learning/journal-dust.md`)
In the shared §5.1 format with the SME's claim tags: the main dust note including "about 15 times faster" (71 words, within the 80-word limit); the "60% floor is optimistic" note; storm dust keeps settling for days; six one-line misconception notes (wind, "not only power", rain, dust near a base, the brush, the wiper); the Earth link (desert solar farms lose about half a percent a day); the F1 real-versus-game line about the 2018 storm; the power-drop "What caused it?" item. "Lungs" became "air filters". Every entry using a number the SME only found by search has a **fallback text with no number**; "in about three weeks" and any claim about what ended Opportunity's mission were removed.

## C1 evidence decision
"Cleaned before a forecast storm" counts as C1 evidence, **formative only, not graded**, when all three hold: the player cleaned the panel themselves (`panel.cleaned`, own player ID); the panel was at dust stage 2 or worse (output 82% or less); a storm warning was active. It can fill the C1 Keeper's Record page as an alternative to "placed or refueled life support before the first storm", always **together with** a correct F1. It adds the export column `panels_cleaned_before_storm`. Cleaning clean panels, or cleaning with no storm coming, doesn't count (content rule CR-R2 forbids rewarding activity for its own sake). Full decision record in LEARNING.md §5.5.

## Forecast re-check (worst case: panels at the 60% floor before the storm, cleaned during it)
| Forecast | Result |
|---|---|
| F1 | Pass. Power goes from 60% to 40%, still a drop |
| FI-1 | Pass, narrow. Power goes from 60% to 52%; batteries on the shared grid could also mask the drop at the emitters |
| FI-4 | **Fails on net outcome.** Power goes from 60% to 70% (the Phase 3 storm cap halves the storm's cut), so the vats speed up and CO2 may fall instead of rise |

**Fix (new content rule CR-F10, `resolve_source` field):** resolve all three forecasts on the storm's own cause line (the Monitor already attributes power to each source), not on the net change. Also passing: storm dust is added after F1 locks; the dust journal notes fire at safe moments; dust devils and Phase 3 rain don't flip any answer; and the C1 cleaning chance arises before the first storm.

## Decisions needed
None. The C1 evidence rule is the Instructional Designer's to decide and is recorded with rationale.

## Handoffs
- **Game Designer:** copy the entries in `journal-dust.md` §1–6 into `docs/design/messages/` (phase-1; phase-2 for the wiper line), keeping facts and tags and shipping the fallback texts unless the SME opens the sources first; F1's `reveals` list names "Dust on panels" and should name "Storm: less sunlight" (holding the panel-dust line until F1 locks would hide it from its own first appearance); change F1's `science_claims` from "ended Opportunity's mission" to "left Opportunity without enough power"; add `resolve_source` to F1, FI-1, and FI-4.
- **Technical Lead:** a `panel.cleaned` event with `stage_before`, `storm_warned`, `sols_to_onset`; the export column `panels_cleaned_before_storm`; exclude panel dust from failure and repeat-failure counts; CR-F10 with a test that fails FI-4 on net CO2 when a player cleans mid-storm; the trigger for `explain.power_drop`.
- **Subject Matter Expert:** can "keeps settling for days" ship as qualitative wording with no number? is "air filters" an acceptable swap for "lungs"? Opening Lorenz 2021, the soiling study, and the 2018 storm pages would let the numbered texts replace the fallbacks.

## Risks
- FI-4 stays wrong for some players until CR-F10 is built; the same class of bug can hit any future forecast where the player can move the value another way.
- The C1 dust indicator could turn into a ritual if players learn it fills a page; the revisit rule is to drop the page trigger and keep the export count.

## Shared model updates
- **Rule CR-F10:** a forecast about one cause resolves on that cause's own contribution, not on the net change, whenever the player can move the value another way.
- **Decision:** cleaning a dusty panel (stage 2 or worse) during a storm warning is formative C1 evidence; cleaning in general is not; panel dust is never a failure type.
- **Expectation:** an entry built on a number the SME only found by search ships with a no-number fallback text until the source is opened.
- **Lesson:** worst-case checks need the player's possible actions, not just the physics. The physics alone passed FI-4; a player with a brush broke it.
