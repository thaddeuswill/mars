# SME report: lander deck height (round solar array)

**Date:** 2026-11-27 · **Role:** Subject Matter Expert · **Task:** real deck height for the round solar array.

## Answer
| Lander | Deck height above ground | Source |
|---|---|---|
| **InSight** | **0.83 to 1.08 m** (33 to 43 in), depending on how the three landing legs settled. Deck diameter 1.56 m; each array about 2.2 m across. | NASA/JPL InSight press kit, "Spacecraft" page: https://www.jpl.nasa.gov/news/press_kits/insight/landing/mission/spacecraft |
| Phoenix | **about 1 m (estimate only).** Official figures: 2.2 m to the top of the met mast, mast about 1.14 m above the deck, deck diameter 1.5 m, deployed array span 5.52 m. | NASA Phoenix launch press kit: https://www.nasa.gov/pdf/181835main_phoenix-launch-presskit.pdf ; mast-above-deck figure from a forum post only |

## Recommended in-game value
**16 px (1.0 m, exactly one block).** It sits inside InSight's 0.83 to 1.08 m range (13 to 17 px) and matches the Phoenix estimate, and it lands on a block edge, so it is easy to build around.

## Verified vs not
- **Verified (search summary quoting NASA pages):** InSight 83 to 108 cm range, 1.56 m deck, 2.2 m arrays. The same search noted NASA's tech-specs table repeats the range.
- **Not verified:** I could not open the NASA pages directly (`www.jpl.nasa.gov` is not in this session's allowed network domains), so the numbers come from search-result summaries of those pages, not from reading the pages. Please re-check once the host is allowed.
- **Not verified:** Phoenix deck height. No NASA figure found; about 1 m is derived (2.2 m minus 1.14 m) using a forum number.
- **Conflicting:** a Rice/WUSTL PDS instrument-host page gives about 0.5 m to the top of the InSight deck. It does not say what it measures from. I treat NASA's figure as authoritative. Unresolved.
- **Game simplification:** one fixed height; real decks vary by leg settling (about 25 cm for InSight).

## Report format
0. **Handoffs received:** none.
1. **Done:** this report.
2. **Decisions needed:** none. (Well-structured: use 16 px, or any value from 13 to 17 px.)
3. **Handoffs:**
   > **To:** Lead Artist · **Problem type:** well-structured
   > **Problem:** raise the round array to real deck height.
   > **Context:** this report; `docs/art/reference-audit.md` finding 3.
   > **Needed from them:** set the array height to 16 px above its base.
   > **Done when:** the array sits 16 px above ground in the model, and the art notes cite InSight 0.83 to 1.08 m.
4. **Risks:** the NASA numbers rest on search summaries; low risk because three sources agree on roughly 1 m.
5. **Shared model updates:** None. Lesson: this session cannot open `jpl.nasa.gov`; allow it in network settings for future source checks.
