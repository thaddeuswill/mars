# Report: SME Round 3 (BACKLOG #16)

- **Role:** Subject Matter Expert
- **Date:** 2026-11-26
- **Outputs:** `docs/science/round3-review.md` (new), `docs/science/external-review-pack.md` (new), `docs/science/ngss-verification.md` (round 3 word-for-word section), pointers in `accuracy-review.md` and `round2-review.md`
- **Problem types:** the Phase 3 CO2 budget is ill-structured; everything else well-structured.
- **How checked:** opened directly: nextgenscience.org (all 24 PE pages) and the NASA Mars and Earth fact sheets (nssdc). Still blocked after three tries: jpl.nasa.gov, science.nasa.gov, photojournal, gml.noaa.gov, data.giss.nasa.gov, ipcc.ch, ntrs. Facts from these came through web search and are tagged "searched, not opened". pds-imaging.jpl.nasa.gov is now reachable, so rover-image pixel sampling can happen (not done this round). Every number is tagged sourced, calc, invented, or unverified.

## Handoffs received
- **Game Designer (all accepted):** crops accurate; biome chain accurate in order but needs labels for the timescale and the 5 °C tree line; mirror framing accurate; Training Hall wall chart accurate; greenhouse read-more accurate; fire edge at 23% O2 (75 kPa) acceptable and slightly conservative; "1.1 °C" now sourced.
- **Instructional Designer (all accepted):** FI-1 key right but the stem is ambiguous (dust storms heat the air while cooling the ground), reword written; FI-2's 0.5 °C is correct (0.54 °C, calc), but at equilibrium nitrogen and O2 can't freeze out at the poles, so the 1.5 kPa "polar winter" dip can't happen then (fix: a passing weather low, or fire FI-2 early in Phase 3); FI-3 and FI-4 accurate, FI-4 needs the respiration nuance; Shift Briefing 2 should say ice needs both to **stay** water, not "become"; worked-example reasons drafted; delayed post-test keys checked; CTA scenarios written (3 proposals, 4 Earth cases). **F4a puddle drying time:** plausible if frozen water counts as "no liquid" and puddles are thin; accepted range 0.02–0.25 sol (invented), ideally growing with pressure.
- **Lead Artist (audio):** background dosimeter ticks **corrected**: background radiation never reaches zero, even on Earth (about 2.4–3.0 mSv per year), so fade to a small floor, not to silence. Contact sound through suit and ground **accurate** (suits are pressurized to about 29.6 kPa).

## Sourced this round
- Light delay 3.0–22.3 minutes (calculated from the opened fact sheet).
- Earth warming 1.09 °C for 2011–2020 vs. 1850–1900 (IPCC AR6, searched, not opened).
- Permafrost carbon about 1,460–1,600 billion tonnes, about twice the carbon in the air.
- Earth's mean temperature 15 °C (fact sheet).
- Debrief 2 graphs named and cited: NOAA Mauna Loa CO2 and NASA GISTEMP (neither page opened). Teacher note: the two use different baselines and the CO2 record starts in 1958.
- **NGSS:** all 21 mapped codes match nextgenscience.org word for word (one comma fixed: HS-ETS1-3 has none after "aesthetics"); the ETS codes that were only partly verified are now verified, and so is MS-ESS3-3. Two assessment boundaries matter: MS-ESS1-3 rules out recall; HS-ESS3-6 is limited to published model results.

## Phase 3 CO2 budget: recommended option A, "CO2 is the oxygen source"
- Plants turn CO2 into O2 one molecule for one; Phase 3 must start with about 18.5 kPa of CO2 (calc); the rest of the pressure is the already-labeled filler gas.
- Decay and fire reverse the swap, one for one. Scrubbers lock CO2 into rock and make no O2, which makes them a real trade-off (B4). An O2 generator that splits water is the fallback source.
- Option B (plants making O2 while CO2 stays low) would create matter from nothing: wrong. Option C has a grind risk.

## Decisions needed (classified by coordinator)
| Decision | Recommendation | Type |
|---|---|---|
| Phase 3 composition rule | Option A (route through the Producer; the Game Designer leads) | Ill-structured |
| Allow gml.noaa.gov, data.giss.nasa.gov, ipcc.ch (and photojournal.jpl.nasa.gov, already allowed) | Yes: needed to open and save the debrief graphs before the educator guide ships | Well-structured |

## Handoffs
- **Game Designer (ill-structured):** choose a CO2 rule (SME recommends A; CO2 at the Phase 3 gate 17–19 kPa, invented, built around the 18.5 kPa calc); size the Phase 2 CO2 rise to match; add the matching O2 change (−0.1 kPa/sol O2) to the decay-pulse drift event; replace the equilibrium "polar winter" drift event with a weather low; add the labels (succession timescale, 5 °C tree line as invented, the mirror line, "stay water" in Briefing 2). Done when balance sheet §3a has a CO2 row and the alignment check is requested.
- **Instructional Designer:** FI-1 stem reword; FI-2 trigger or premise; FI-4 explanation; paste the CTA scenarios into `cta.md` §4; the delayed post-test keys; F4a condition (frozen counts as "no liquid"); set `ngss_map.example.json` `verification` to "verbatim-checked-2026-11-26"; optionally add MS-ESS3-3 as related for B5; educator guide teacher note on baselines.
- **Lead Artist:** background ticks fade to a small invented floor, never zero, inside and outside; mark the contact path SME ✓ plausible; optional pixel sampling with the SME now that pds-imaging is reachable.
- **Producer:** find the three reviewers and track the pack's status table.

## Risks
- Option A adds a coupling: over-scrubbing CO2 early could stall O2 unless the electrolysis fallback exists.
- The debrief graphs are cited but not opened; MS-ESS3-5 depends on them.
- The 5 °C tree line, the succession timescale, CO2 tolerance in plants, and the CO2 frost-point formula remain unverified.
- Accepting the F4a puddle range depends on "frozen counts as no liquid".
- Risk gate: CO2 rule A (could break the Phase 3 curve, F8, FI-4, A5 alignment) pending; FI-2 premise fix (A2 interleaving count) pending; NGSS text fix done in the SME file, ID files not checked.

## Shared model updates
- **Term:** "CO2 is the O2 feedstock" (rule A, if adopted): photosynthesis swaps CO2 for O2 one for one; decay and fire swap it back.
- **Lessons:** check mass balance whenever an Index value is made from another ("plants make O2" silently created matter until the CO2 budget was written down); a drift event that is real in one phase can be impossible in another, so run the threshold-vs-curve check on events too.
- **Expectation:** the NGSS word-for-word check is complete; LEARNING.md §6 can drop "verify each code before publishing" (Instructional Designer's file).
