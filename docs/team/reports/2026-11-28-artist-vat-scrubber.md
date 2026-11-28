# Lead Artist report: algae vat and carbon scrubber art (2026-11-28)

## Handoffs received
Technical Lead registers `algae_vat` and `carbon_scrubber` in parallel and will use these files. Accepted. Model names, state suffixes, shape heights and the registration lists are in `docs/art/algae-vat.md` and `docs/art/carbon-scrubber.md`.

## Done (real output)
- `tools/art-preview/gen_vat_scrubber.py` (rerunnable, prints OK, self-checks size, opacity, no green, band, plate, uv range, heights) and `tools/art-preview/flat_views.py` (flat orthographic viewer; not a 3D render).
- Models in `pack/assets/mars/models/block/`: `algae_vat`, `_off`, `_idle`, `_running`; `carbon_scrubber`, `_off`, `_idle`, `_running`; items in `models/item/`.
- 22 static textures and 4 animated strips (`algae_vat_glass_running`, `carbon_scrubber_front_running`, `_bed_a_running`, `_bed_b_running`, each with `.mcmeta`), made-up band and plate baked in.
- Vat: dark amber-brown culture, pale-blue status ring, warm-white grow-lamp strip (off dark, idle dashed, running solid), bubbles 4 x 8 ticks. 28 px model (air line), 24 px collision.
- Scrubber: twin cream-grey beds, valve block, header, chute; beds and indicator windows take turns, 4 x 20 ticks. 24 px.
- Docs: `docs/art/algae-vat.md`, `docs/art/carbon-scrubber.md`, two rows in `docs/art/running-state-table.md`, `docs/art/w1-placeholder-textures.md`, BACKLOG #49, HANDOFF section 00 (my lines only).
- `TextureSpecTest` extended (4 new tests plus ANIMATED list). Cited sheet: `docs/art/renders/vat-scrubber-sheet.png` (flat views of off/idle/running for both, then the four strips frame by frame; committed with `git add -f`).
- Ran `./gradlew spotlessApply build runGameTestServer --offline`: **382 unit tests, 2 failed, neither mine**: `LangKeysTest.everyBlockAndItemHasAName` (missing `block.mars.algae_vat` and `block.mars.carbon_scrubber`: the item models I wrote now make the scan expect names; Technical Lead adds the keys) and `PhaseTwoMachinesTest.drawAndRateComeFromDataAndAreMarkedInvented` (atmospheric_processor expected 1 got 2; caused by the uncommitted `MachineDef.java` edit in the tree, not touched by me). All `TextureSpecTest` tests pass (run on its own). Because the build stops at failing tests, I ran `runGameTestServer` separately: `MARS-SELFTEST RESULT PASS passed=165 failed=0`.

## What I saw
Flat views (viewed): both silhouettes read apart from each other and from the other machines; amber culture reads brown, not green; bubbles visibly move between frames; scrubber windows and bed rings alternate. A first viewer pass was flipped vertically (texture rows); I fixed the viewer and re-viewed. **Not seen:** 3D render, a real client, animation playing, colour-vision simulation.

## Decisions needed
1. Is a 28 px model with a 24 px collision acceptable for the vat (air line overhang, like the extractor and emitter)? Well-structured. Recommendation: yes; otherwise I trim the line to 24 px in minutes.
2. If the Technical Lead prefers the vat/scrubber items to need lang keys later: the item models exist already, so the lang test will fail until keys exist. Well-structured; the Technical Lead adds the keys with the registration.

## Handoffs
> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** Register `algae_vat` and `carbon_scrubber`.
> **Context:** `docs/art/algae-vat.md` and `docs/art/carbon-scrubber.md` (registration lists): `facing` + `machine_state`, models `<name>_<state>`, one cell, 24 px collision, no render type needed, lang name and "Game-sized version of a real idea" tooltip, loot.
> **Needed from them:** blockstates, lang keys (the two keys `LangKeysTest` reports), shapes, loot; check the 2-3 uv crops in a real client.
> **Done when:** screenshot shows both blocks in all three states; I review the look.

## Risks
- Crossed-box cylinders and uv crops are unseen in 3D; a first real screenshot may need small fixes.
- Timings and frame counts are INVENTED. Photobioreactor reference is general knowledge, not an opened source.
- Two scrubber strips plus one front strip must stay in step (same frametime); do not change one without the others.

## Shared model updates
None (new rows follow the existing running-state table conventions).
