# SME report: Mars materials and recipes (2026-11-27)

## Handoffs received
Owner request: list real Mars materials and ISRU processes that map to craftable blocks and items; invent where needed; keep vanilla crafting. Accepted. The file `docs/design/world-and-blocks.md` was not edited (another SME run owns its marks).

## Done
- `docs/science/mars-materials-and-recipes.md`: three tables (materials to blocks with recipe ideas and REAL / SIMPLIFIED / INVENTED status; 14 invented items with honest journal labels; sources opened with quoted lines).
- Opened and quoted: NASA Mars facts, NSSDC fact sheet, two MOXIE pages, NTRS ISRU records (Sabatier, ice within 1 m, electrolysis), NTRS polar/pressure report, NIAC construction report (sintered regolith, sulfur concrete).
- Not run: no game, no tests (docs only).

## Decisions needed
1. **Spine recipe chain (well-structured):** use the sourced loop CO2 + ice, electrolysis, Sabatier, O2 / water / methane as Phase 1 and 2 crafting? Recommend yes.
2. **Optional invented materials (ill-structured, fun vs scope):** basalt fibre, aluminium, perchlorate-bake. Recommend cut aluminium, keep basalt fibre only if hull panels need it.
3. **Journal labels (well-structured):** adopt the section 2 wording ("game invention, not real") for all invented machines. Recommend yes.

## Handoffs
> **To:** Lead Game Designer · **Problem type:** well-structured
> **Problem:** Turn the recipe ideas into real recipes and costs.
> **Context:** `docs/science/mars-materials-and-recipes.md` sections 1 and 2.
> **Needed from them:** pick recipes, amounts; flag any that skip a gate.
> **Done when:** recipes listed in `docs/design/world-and-blocks.md` with status marks kept.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** Place the invented-item labels in journal entries and check goal IDs.
> **Needed from them:** alignment check. **Done when:** labels appear in journal content.

## Risks
- Perchlorate figure and several process details are NOT VERIFIED this session (pages did not open). Re-check before journal text ships.
- Super-greenhouse emitter and seed kits could teach "terraforming is easy and fast" without labels.

## Shared model updates
- Lesson: PubMed returned HTTP 203 with no text; treat abstract summaries from search as NOT VERIFIED.
- Term: "spine chain" = the sourced CO2/ice/electrolysis/Sabatier loop. Otherwise None.
