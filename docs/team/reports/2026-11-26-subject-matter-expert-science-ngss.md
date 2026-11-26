# Report: Science Accuracy and NGSS Verification

- **Role:** Subject Matter Expert
- **Date:** 2026-11-26
- **Outputs:** `docs/science/accuracy-review.md`, `docs/science/ngss-verification.md`
- **Note:** nextgenscience.org and the NASA fact sheet site were blocked by the session network. Codes were verified through search copies, state mirrors, and papers. A verbatim check on nextgenscience.org is still needed.

## Done
- **Starting values match real Mars:** pressure 0.636 kPa, temperature −59 °C, CO2 95.1%, O2 0.145%.
- **Breathable gate is physically wrong:** it uses O2 percentage, but breathing depends on O2 partial pressure. 21% of 30 kPa is about 6.3 kPa, which is deadly. Even 60 kPa × 21% is about 12.6 kPa, the same as roughly 4,200 m altitude.
- **CO2 < 1% is about right:** at 60 kPa that is 0.6 kPa, close to the workplace limit of about 0.5 kPa.
- **Missing filler gas:** about 46 kPa of buffer gas is needed (on Earth, nitrogen); Mars has little.
- **Water physics is correct:** triple point 0.611657 kPa; liquid water possible at 5 kPa; stable at 30 kPa and ≥ 0 °C.
- **Greenhouse emitters** must emit super-greenhouse gases, not CO2, or "CO2 falls while temperature holds" makes no sense.
- **Accuracy notes:** confirmed with sources. The oxygen timescale is understated: up to about 100,000 years, not centuries.
- **Misconceptions table:** "ice melts above 0 °C" is mis-worded, since it is true on Earth. The real misconception is that phase-change points are fixed regardless of pressure.
- **NGSS:** three codes map weakly and need replacing.
  - HS-ESS2-5: no good high-school code; treat A2 as an extension of MS-PS1-4.
  - MS-LS2-5 → MS-ETS1-2.
  - HS-LS2-7 → HS-ETS1-3.
  - MS-ESS1-3 rules out recall assessment, which conflicts with the A1 post-test.
  - A replacement table is in `ngss-verification.md`.

## Decisions needed (classified by coordinator)
| Decision | Recommendation | Type |
|---|---|---|
| Breathability: (A) raise O2 to 25–30% at 60 kPa, (B) raise target pressure to about 75 kPa, (C) keep the numbers and add a high-altitude note | B or C | Ill-structured |
| Multipliers never apply to physical thresholds (triple point, breathability) | Yes | Well-structured (accuracy) |
| Filler gas: model it or note it only | Journal note only | Ill-structured |

## Handoffs
- **Lead Game Designer:**
  - Breathable gate condition in partial-pressure terms.
  - CO2 target ≤ 0.5 kPa.
  - Define the hydrosphere unit.
  - Multipliers never apply to physical thresholds.
  - Emitters use super-greenhouse gases.
  - Optional: Armstrong-limit milestone, ice–albedo feedback, CO2 from decay, carbon reservoirs shown on the Monitor.
- **Lead Instructional Designer:**
  - Replace the NGSS 6.1 table.
  - Change the A1 measure to a data-interpretation task.
  - Reword the misconception row; add "CO2 is the only greenhouse gas".
  - Replace the accuracy notes with the sourced versions.
  - Add an SEP ("Asking questions and defining problems") and a CCC ("Patterns").
- **Coordinator:** network access to nextgenscience.org and nssdc.gsfc.nasa.gov for the verbatim check. Raised with the user; not changed by the coordinator.

## Risks
- The fauna and the O2-percentage gate could each teach a false idea.
- Classroom multipliers could break physics if they scale thresholds.
- Several Earth-science NGSS claims depend on D2 Earth-link content that hasn't been written yet.
- Some figures are unverified and must not appear as fact in the game.
