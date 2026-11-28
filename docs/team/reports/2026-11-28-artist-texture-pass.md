# Lead Artist report: texture pass (BACKLOG #49)

**Date:** 2026-11-28 · **Branch:** `claude/elegant-brown-anu1ci`

## Handoffs received
HANDOFF queue item 3 (replace placeholders, made-up band, first machine). Accepted. The machine order (made-up band, then water reclaimer) follows `docs/design/machine-priorities.md` and the look brief section 3. The "which machines gate progress" question in the queue is answered by that doc (it exists now); I did not reopen it.

## Done
- **Ten real terrain textures**, 16 x 16 opaque PNGs: the nine W1/W2 blocks plus new `ripple_sand`. Script `tools/art-preview/gen_terrain_textures.py` (Pillow, deterministic; fails if a pixel is greenish, blue-cast, pure black or white, or non-opaque). Spherule balls are neutral grey; vesicular basalt dark grey with small holes; duricrust ochre plates; gypsum pale veins. Ice is neutral white-grey, not blue. Details and hex values: `docs/art/w1-placeholder-textures.md` (status table).
- `ripple_sand`: same dark sand, crests with a wobble and one ramp step of contrast, period 8 px; no dark trough band, so it should not read as zebra. The Technical Lead had already registered the block (blockstate, model, loot, lang exist in the working tree); the PNG was the missing piece.
- **Made-up band** overlays `made_up_band.png` and `made_up_plate.png`, plus bake functions, spec `docs/art/made-up-band-spec.md`.
- **Water reclaimer** first pass: model `pack/assets/mars/models/block/water_reclaimer.json` and eight textures; spec and registration list in `docs/art/water-reclaimer.md`. Generator `tools/art-preview/gen_invented_machines.py`.
- Test: `mod/src/test/java/mars/art/TextureSpecTest.java` (3 tests: terrain 16 x 16 and opaque; machine textures 16 x 16; band dash pattern and clear above the band).
- Screenshot script `tools/screenshots/art_textures.shots`; images `tools/screenshots/out/20261128-004710/` (`tex_01_wall.png`, `tex_02_top.png`).

## Verified (ran)
- `cd mod && ./gradlew spotlessApply build runGameTestServer`: BUILD SUCCESSFUL, `MARS-SELFTEST RESULT PASS passed=126 failed=0`. The new test class ran 3 tests (the unit total includes the Technical Lead's uncommitted tests in the same working tree, so I do not quote a total).
- Both generators exit 0 with all checks OK.
- Real client (Xvfb, software GL): I looked at both PNGs (timestamps 00:47:58, new). Nine blocks float above the terrain at y 166. Order in the wall: top row duricrust, ground ice, dark sand ore, hematite, basaltic sand; bottom row gypsum, breccia, spherule, vesicular (the camera looks south, so x is mirrored). All nine read as different blocks; none green or liquid; spherules read grey. First two runs failed (a Technical Lead compile error in `PlanetGameTests`, then a dim run inside the dome); both discarded.

## Not verified
- The water reclaimer model has not been rendered or loaded (no Blender here; block not registered). UV crops and element positions are unchecked in 3D.
- `ripple_sand` was not placed in a screenshot (block registered by the Technical Lead mid-task; not in my script). Whether the ripples stop reading as stripes **in the dunes** is for the owner.
- Colorblind simulation for the band and the new textures: not run.
- Weak points I saw: duricrust is high-contrast and warm (can look like wood up close); vesicular holes are small at distance.

## Decisions needed
1. Duricrust: keep as is, or lower contrast and cool it a step? (well-structured; I recommend lowering contrast once the owner sees it on the dunes.)
2. Band: bake into textures (recommended) or overlay element. Technical Lead / owner, well-structured, decided per machine.

## Handoffs
> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** register the water reclaimer and use the new textures.
> **Context:** `docs/art/water-reclaimer.md` lists block, blockstate, item model, lang, loot, 1.9-block shape, tooltip. `ripple_sand.png` is done for the block you registered.
> **Needed from them:** register; tell me if the model needs a vertical split.
> **Done when:** block shows in a screenshot with the band visible.

> **To:** Lead Instructional Designer / Game Designer · **Problem type:** well-structured
> **Problem:** the reclaimer tooltip must say "Game-sized version of a real idea" and the band needs a journal line explaining the purple dashes.
> **Done when:** text exists in the journal data.

## Risks
Uncommitted Technical Lead edits share this working tree, so I committed only my files and, for HANDOFF and BACKLOG, only my lines. Reclaimer may need rework once rendered.

## Shared model updates
- `gen_w2_placeholder_textures.py` is obsolete; rerunning it overwrites real art. Use `gen_terrain_textures.py`.
- Lesson: run-shots.sh with a broken build still copies old PNGs; check the timestamps and the log for BUILD FAILED. Pad at y 163 (above the dome roof) is lit; at y 63 inside the dome it is nearly black.
