# SME report: Mars surface materials and block set

**Date:** 2026-11-27 · **Role:** Subject Matter Expert · **Product:** `docs/science/mars-surface-materials.md`

## Handoffs received
Owner (via coordinator): more atmosphere and more surface block types, accurate to Mars, make-it-work rule applies. Accepted. Technical Lead W1 report asked for checks on mesa band colours, pale ejecta and gravel vs sandstone; answered in part below.

## Done
- 21-row table of surface materials (real colour, where, block set with NEW marked, game use, status, quoted source). 16 rows carry a quote from a page I opened today (curl, HTML stripped with `python3 -I`, never run). Colours of layered rock, mudstone, carbonate, olivine rock and salt stay **NOT VERIFIED**.
- Block proposal: 6 Tier A NEW blocks (`duricrust`, `vesicular_basalt`, `spherule_sand`, `impact_breccia`, `gypsum_vein`, `co2_frost`), 5 Tier B (`conglomerate`, `mudstone`, `carbonate_rock`, `olivine_rock`, `salt_crust`). Rest reuses vanilla or is a pattern, not a block.
- Biome map and a "what the player should NOT see" table.
- Answers to the Technical Lead: W1 ejecta `sandstone` speckle should become `impact_breccia` (dark, angular) near big rims; sandstone as the sub-layer under dust is fine; gravel is real-looking but falls, place only on solid ground; mesa band colours remain NOT VERIFIED (no yellow stays right).
- Not run: no game, no screenshots; I did not look at the textures.

## Decisions needed
| # | Question | Type | Recommendation |
|---|---|---|---|
| 1 | Build Tier A (6 blocks) now and hold Tier B for W2 biomes? | well-structured | Yes |
| 2 | "Atmosphere": this task covered surface only. Do you want a separate pass on air visuals (dust haze, sky colour, dust devils, clouds) as a sourced note? | ill-structured (art, learning, tech) | Yes, next SME task; sky is already keyed to pressure |
| 3 | Is `co2_frost` worth its small code? Skip if time is short | ill-structured (cost vs the frost-sink lesson) | Keep, polar and winter only |

## Handoffs
> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** textures for up to 11 new cubes; palettes in the doc, no green, no bright white except veins and frost.
> **Context:** `docs/science/mars-surface-materials.md` sections 2 and 4; placeholder rule in `docs/art/w1-placeholder-textures.md`.
> **Needed from them:** Tier A textures first; check real photos using the search terms (section 3) and tell me any colour I should source.
> **Done when:** 6 PNGs in `pack/assets/mars/textures/block/` and a screenshot looks right.

> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** register Tier A blocks, loot tables and worldgen entries; ripple stripes and troughs as surface pattern.
> **Context:** same doc, sections 2 and 3; existing W1 pattern (`MarsTerrainBlocks`). No block changes any Index rate.
> **Needed from them:** an estimate and the order; note `co2_frost` needs a layer that melts.
> **Done when:** blocks exist, generate in the right biomes, and a boot check covers them.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** journal lines for the new blocks, and goal IDs (D2 "on Earth" links: gypsum veins on Earth, frost, breccia).
> **Needed from them:** wording check against the plain-text notes in section 2.

## Risks
- Many colours are my reading of NASA text, not measured. The Artist must check photos.
- Too many new blocks can bury the Index lesson; Tier A only keeps scope small.
- `spherule_sand` iron yield can undercut hematite ore balance (Game Designer to set).

## Shared model updates
- Term: **Tier A / Tier B** blocks (build now / with W2 biomes).
- Rule: terrain blocks must avoid green, liquid water, Earth soil, hell colours (table in section 4 of the doc).
