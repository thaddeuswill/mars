# Report: inspiration survey (Lead Game Designer, 2026-11-28)

## Handoffs received
Owner request (BACKLOG wording: "what are some mods we are taking inspiration from; we need to be taking a lot of inspiration for every aspect of this"). Accepted. Docs only.

## Done
- `docs/design/inspiration-survey-mechanics.md`: 8 aspects, 5 to 8 references each, with copy / avoid / goals, then "What we do now", "Gap", "Recommended next steal" (sized) per aspect, and a ranked top-8 table.
- Sources actually read: Modrinth API listings (JSON, scratchpad only) for Create, Mekanism, Immersive Engineering, Farmer's Delight, Mystical Agriculture, Tough As Nails, Ad Astra, Galacticraft-Legacy, Botania, plus search hits for Thermal, Advanced Rocketry forks, Hardcore Survival, Cabricality, Create Ponder. Listings prove existence and author description only. Features are from memory and marked so. Not found: Create: Above and Beyond, Vault Hunters, FTB Quests, Nomifactory (memory only). No licences checked. No game was run or played.

## Decisions needed
1. Which top-8 steals to queue? Recommendation: 1 (hold-to-explain card), 4 (new input item per gate), 7 (three logs) first: all small. Ill-structured (taste, fun versus cost).
2. Bundle a mod (for example Create Ponder) or copy the idea? Recommendation: copy the idea, per the earlier mod verdicts. Well-structured once licences are read.

## Handoffs
> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** Check licence and NeoForge 1.21.1 builds for Create (Ponder), Immersive Engineering, Tough As Nails if the owner wants any bundled.
> **Needed from them:** one table, like `optional-mods-and-shaders.md` section 3. **Done when:** verdict per mod.

> **To:** Instructional Designer · **Problem type:** well-structured
> **Problem:** Confirm the goal-ID mapping (A1-D2) I used for steals; I did not re-read `LEARNING.md`.
> **Done when:** mappings confirmed or corrected.

> **To:** Subject Matter Expert · **Problem type:** well-structured
> **Problem:** Steal 3 (processor as multiblock) and steal 1 need a real reference per 5a (`docs/science/machine-references.md`).

## Risks
- Memory-based claims about games may be wrong in detail; treat as inspiration, not fact.
- Steals 2, 3 and 6 are medium and need code; R1 (no copper or redstone) still blocks machine recipes.
- Mod licences unchecked.

## Shared model updates
None.
