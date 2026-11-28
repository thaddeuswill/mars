# SME report: conductor ores, carbonate, bone meal (2026-11-27 file, written 2026-11-28)

Full content: `docs/science/mars-conductor-and-carbonate-ores.md`.

## Handoffs received
None beyond the task (R1, R3, R4, D1, D4 in `docs/design/machine-priorities.md`). Accepted.

## Done
- Wrote `docs/science/mars-conductor-and-carbonate-ores.md`. Opened NTRS abstracts (via the search API) and the NASA Mars Facts page. No full papers.
- **Copper:** no source found; **INVENTED**. **Redstone:** **INVENTED** with a real hook (magnetised southern crust, VERIFIED on Mars Facts; pyrrhotite is a 1989 hypothesis).
- **REAL materials:** iron meteorites found by Opportunity (NTRS 20100003417); Al, Si, Ti, Fe from regolith by molten-regolith electrolysis (NTRS 20170001458); Mn oxides in Gale and Endeavour (NTRS 20150001945).
- **Carbonate:** 2 to 5 wt% in global dust (orbital, NTRS 20090011357); Jezero margin unit "one of the most carbonate-rich regions of the planet" (NTRS 20240000510). No Jezero percent found: NOT VERIFIED. Rule: small mined vein, labelled game-scale.
- **Bone meal:** compost via vanilla composter is a fair simplification; label given.

## Decisions needed (owner)
1. **D1 conductor source.** Ill-structured. Recommend: add `sulfide_vein` (copper, INVENTED) and `magnetic_ore` (redstone, INVENTED), both rare in walls and old high ground, plus a starter chest of 4 copper and 8 redstone; optional REAL `meteorite_iron`. Alternative: iron-only recipes (more honest, but breaks "keep real crafting").
2. **D4 carbonate.** Ill-structured. Recommend small vein (4 to 8 blocks, ~3 per 256x256 in crater floors); gypsum is the fallback.
3. **Journal honesty.** Well-structured. Recommend the one-line labels in section 3 and 4 of the science doc are shown on the block tooltips or journal.

## Handoffs
> **To:** Lead Game Designer · **Problem type:** ill-structured
> **Problem:** Machine recipes need copper and redstone; Mars worldgen has none.
> **Context:** `docs/science/mars-conductor-and-carbonate-ores.md` sections 3 to 5; `docs/design/machine-priorities.md` R1, R3, R4.
> **Your view:** SME: both ores are game inventions; keep them rare and wall-hosted so they cost exploration, not grind; starter stock so Phase 1 is not blocked.
> **Needed from them:** pick the option, set rarity numbers (mine are *invented* first guesses), decide first-kit carbonate count, update the recipe table 3.6 and the audit table in `DESIGN.md`.
> **Done when:** recipe table names the ore blocks and each has a journal label.

> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** worldgen entries for up to 3 new ore blocks and a `carbonate_rock` vein.
> **Context:** placement and rarity in the science doc sections 3 and 4 (after the Game Designer confirms).
> **Needed from them:** configured and placed features, loot tables (raw copper, redstone, carbonate), starter chest.
> **Done when:** a survival player can find each ore by exploring and machines can be crafted without creative mode.

> **To:** Lead Artist · **Problem type:** well-structured · textures for `sulfide_vein` (no green or bright copper), `magnetic_ore`, `meteorite_iron`, `carbonate_rock` (pale grey-tan, not white). Done when they pass `mars-surface-materials.md` section 4.

## Risks
- Players may read "copper ore" as real. Labels must show.
- Rarity numbers are invented; wrong values either block machines or make them free. Needs playtest.
- Bone meal loop in the soil unit (see section 5).
- Carbonate abundance remains unquantified for Jezero.

## Shared model updates
- Lesson: NTRS search API (`ntrs.nasa.gov/api/citations/search`) returns abstracts and is reachable; many `science.nasa.gov/resource/...` guesses return 404.
- New terms for the audit table: `sulfide_vein`, `magnetic_ore` (INVENTED), `meteorite_iron` (REAL), `carbonate_rock` (SIMPLIFIED).
