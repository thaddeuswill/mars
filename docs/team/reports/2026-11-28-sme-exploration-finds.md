# Report: SME, exploration finds (2026-11-28)

Role: Lead Subject Matter Expert. Task: owner idea (BACKLOG 2026-11-28) on explorable finds. Full review: [docs/science/exploration-finds-review.md](../../science/exploration-finds-review.md). Docs only; no Java, no pack.

**Perspective round (my lens).** Success is a student who leaves knowing that rovers are real, that "possible sign of life" is a scientific claim with evidence and alternatives, that Mars is not proven lifeless or alive, and that terraforming is not possible with present technology. Nothing here may teach "Mars has rare-metal mines" or "NASA found life".

## 0. Handoffs received

None beyond the task brief. All four areas (a to d) answered.

## 1. Done

- `docs/science/exploration-finds-review.md`. Contents:
  - **A.** 16 real missions with status (dead, lost, crashed, retired, active), sizes, what each found, tags and sources; per-find MS and HS sentences; cautions for placement as "replica monuments".
  - **B.** Minerals and resources, MS and HS sentences, safe or label tags. "Rare metals" verdict: **iron, nickel, aluminium, magnesium are honest; platinum-group and other rare metals are not known on Mars (only ppb traces in meteorites, no ore). An invented rare metal must be labelled invented.**
  - **C.** What rovers look for, Cheyava Falls with NASA's exact wording, a claims, evidence and alternatives method for the game, the honesty line, misconceptions.
  - **D.** Terraforming ideas graded. NASA's release on the 2018 Jakosky and Edwards study verified: polar CO2 would only double pressure to 1.2% of Earth's; soil CO2 up to 4%, minerals under 5% of the needed pressure.
  - **E.** One summary table (safe, label, do not show). **F.** The ill-structured question framed with three options. **G.** Sources.
- What opened: science.nasa.gov (Perseverance, Curiosity, MER, InSight, Pathfinder, Phoenix, Viking 1, Mars facts), the NASA 2018 terraforming release, and Wikipedia text for about 19 articles. **Not opened:** mars.nasa.gov (no connection), jpl.nasa.gov, the Nature paper and LASP copy, phys.org, svs.gsfc.nasa.gov. Wikipedia throttled (HTTP 429) for Sojourner, Mars 3, Zhurong, Curiosity, Viking 1, Perchlorate, MOXIE; those facts use NASA pages or search summaries and are tagged.
- Verified this session (opened-NASA): Cheyava Falls "potential biosignatures ... require more data" (Nature, Sept 2025); Perseverance 45.22 km on 11 Oct 2026; Curiosity 899 kg; Viking 1 lander 978 kg, ended 11 Nov 1982; Spirit 7.7 km; Opportunity 28.06 mi, last contact 10 Jun 2018; Phoenix found ice and perchlorate; the terraforming numbers above.
- Committed and pushed (only these two files).

## 2. Decisions needed

| # | Question | Type | My recommendation |
|---|---|---|---|
| 1 | Should "claims, evidence and alternatives" become a learning objective (goal change)? | ill-structured | Frame it under B3 first; decide after a paper prototype. Goal changes go to the Instructional Designer |
| 2 | Real rare metals, invented metal, or none? | ill-structured (values and scope) | None in the first pass. If scarcity is wanted, use ice, power or nitrogen. If a metal is wanted, make it clearly invented, not a real element name, with a fantasy-audit row |
| 3 | Is a replica of real hardware an acceptable fantasy element? | well-structured (apply the six rules) | Yes, as "replica" with true text; Game Designer adds the audit row |
| 4 | May a "possible sign" investigation exist at all? | ill-structured | Yes, with the honesty line and no life outcome. The top rung of the confidence ladder stays empty |

## 3. Handoffs

> **To:** Lead Game Designer · **Problem type:** ill-structured
> **Problem:** Fit exploration finds into phases, goals and scope without breaking "pressure, not grind" or blurring real science with the fictional fauna.
> **Context:** `docs/science/exploration-finds-review.md` sections A.3, C.3, E, F.
> **Your view:** SME recommends Option 2 (monuments plus one "possible sign" investigation, no invented rare metal first). Opportunity, InSight and Spirit link directly to storm, dust and power already in the game.
> **Needed from them:** `docs/design/exploration-finds.md` with placement, rewards, and a fantasy-audit row for replicas.
> **Done when:** the design names which finds ship, what each teaches (goal IDs), and the labels from section E.

> **To:** Lead Instructional Designer · **Problem type:** ill-structured
> **Problem:** Does the claim, evidence and alternatives structure need its own goal or assessment, or does it ride on B3 and C3?
> **Context:** review section C.3.
> **Needed from them:** alignment check; no goal ID is changed by this review.
> **Done when:** a ruling on goal mapping and any formative item.

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** Replica hardware models need real sizes.
> **Context:** review A.2. Sizes opened: Curiosity 899 kg; Viking lander 978 kg; Spirit and Opportunity driving distances only. Sojourner 65 x 48 cm is search-only. Perseverance, Spirit and Opportunity mass: unverified.
> **Needed from them:** wait for SME to verify sizes (or use NASA pages) before modelling; use `docs/art/references/`.
> **Done when:** each model's dimensions have an opened-NASA source.

> **To:** Producer · **Problem type:** ill-structured
> **Problem:** Cost and sequencing of the three options.
> **Context:** review F.
> **Needed from them:** cross-role review once the Game Designer's proposal exists.
> **Done when:** the owner picks a scope.

## 4. Risks

- **High:** "NASA found life on Mars" misreading from Cheyava Falls content. Mitigation: exact NASA wording, honesty line, no life outcome.
- **High:** Invented rare metals teaching that Mars has ore deposits.
- **Medium:** Real hardware and the fictional fauna in one game blur "what is real". Mitigation: separate labels ("Real mission" and "Fiction").
- **Medium:** Many `memory`-tagged facts (silica, clays, Viking heating explanation, masses) are unverified. They are marked and must be checked before student text.
- **Medium:** Mars Sample Return status and Zhurong's state come from news search, not primary sources, and may change.
- **Low:** Section D reinforces that the game's CO2 pool is a simplification; the existing journal note must stay visible.
- The Nature paper itself was not opened; terraforming numbers rely on NASA's release.

## 5. Shared model updates

- **Term:** "replica monument": real hardware shown in the game world at an invented location, labelled as a replica. Needs a fantasy-audit row.
- **Rule (proposed):** "Possible sign" content never has a life outcome; the honesty line is "No life has been found on Mars. No sign of life has been confirmed."
- **Lesson:** Wikipedia's API throttles (HTTP 429) when many pages are fetched quickly; fetch with delays and prefer NASA pages. mars.nasa.gov does not resolve from this environment.
- **Decision candidate (for owner):** no real-element "rare metals" in the game; scarcity comes from real shortages (ice, power, nitrogen).
