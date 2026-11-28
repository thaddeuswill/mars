# Lead Artist report: ice extractor art (2026-11-28)

## Handoffs received
Task came from the coordinator (BACKLOG #49 line): build the model and textures with the existing generator conventions; the Technical Lead registers the block in parallel. Accepted. Deviation: the look brief says 2 high; this is one cell with a 32 px overhang (as the task said, like the emitter).

## Done (real output)
- `tools/art-preview/gen_ice_extractor.py` (new, rerunnable, prints OK; imports the helpers of `gen_invented_machines.py`, edits no existing texture).
- Models in `pack/assets/mars/models/block/`: `ice_extractor`, `ice_extractor_off`, `ice_extractor_idle`, `ice_extractor_running` (animated sleeve, steam puff element, cutout) and `models/item/ice_extractor.json`.
- 13 static textures plus two animated strips with `.mcmeta` (`ice_extractor_sleeve_running`, `ice_extractor_steam`; 4 frames x 10 ticks, INVENTED). Band baked into the base; plate and lamp baked into the front (lamp ring x4-6, y3-5; plate x9-12, y3-6). No green.
- Docs: `docs/art/ice-extractor.md` (spec and registration list), row added in `docs/art/running-state-table.md`, `docs/art/asset-list.md` M-18, `docs/art/w1-placeholder-textures.md`, BACKLOG #49 and HANDOFF section 00 (my lines only).
- `TextureSpecTest`: ice textures in the 16 x 16 list and animated list; two new tests (band, plate, lamp shapes, no green; one cell and 32 px height, running model uses the animated sleeve, item model exists). All 12 `TextureSpecTest` tests PASS (`./gradlew test --tests 'mars.art.*'`).
- What I saw: flat contact sheet of every texture and animation frame (looked right: band dashes, slashed plate, hollow/dark/solid lamp, climbing glow ring, steam shapes) and a flat front/side silhouette plot against the emitter, processor and reclaimer. After the first plot the pump box was moved east so the front silhouette is lopsided and distinct from the centred emitter flare. Sheets not committed (not cited as files).

## Full build result (not green, not from my files)
`./gradlew spotlessApply build runGameTestServer --offline`: 357 unit tests, 4 failed, so runGameTestServer did not run. Failures: `LangKeysTest` x2 (no `block.mars.ice_extractor` / `item.mars.ice_extractor` lang yet, and a code literal), `PlanetaryIndexModelTest.removedHandleStopsAndValuesClamp`, `PhaseTwoRatesGatesTest.breathableHoldsSevenSolsAndEquilibriumFourteen`. The Technical Lead's uncommitted edits to `GateDef`, `MachineDef`, `PlanetSim`, `hydrosphere.json` etc. were in the tree at the same time; I believe these are theirs in flight (the lang failure is certainly the pending lang key). I did not confirm against a clean baseline. Rerun after the Technical Lead lands registration.

## Decisions needed
None for the owner. Well-structured.

## Handoffs
> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** Register `ice_extractor` using the finished art.
> **Context:** `docs/art/ice-extractor.md` registration list: facing + machine_state variants with the Oven's y rotations, item model already written, one cell, 24 px collision, lang + "Game-sized version of a real idea" tooltip.
> **Needed from them:** blockstate, block, lang, loot, shape; check the cutout steam element renders.
> **Done when:** a real-client screenshot shows the three states and TextureSpecTest plus LangKeysTest are green.

## Risks
- Not seen in 3D or a client: leg steps, uv crops of the skid front and the east-offset pump box are only range-checked.
- Steam puff overhangs the tank within the cell; cutout sorting unverified.
- The orange heater fins are the largest orange area on any machine; check it does not read as a touch part.

## Shared model updates
None. (Lesson: one flat silhouette plot caught a near-duplicate silhouette before any render; worth repeating for new machines.)
