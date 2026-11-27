# Artist report: oxygen maker v3 and water reclaimer v3 (2026-11-27)

Assets, blockstate JSON and preview tools only; no Java, no client run. Not yet seen in game.

## 0. Handoffs received
Technical Lead (`2026-11-27-technical-lead-maker-reclaimer-check.md`): maker reads as dark lattice, reclaimer as labelled box; make the maker brighter with thin seams, reclaimer one-sided with facing variants. Accepted, all done. Light-level choice (13/14/15) stays with the owner.

## 1. Done
- `tools/art-preview/gen_maker_reclaimer_v3.py` (imports v2 helpers): writes `o2_generator_{face,back,side,end,top}`, `water_reclaimer`, `water_reclaimer_side` and both models.
- Maker: pockets 4x3 px in a 2x3 grid, 1 px grooves `#70511A` (not `#2A1D0A`; I first tried `#5A3F12` and raised it for brightness), pockets `#FFF2B4`, highlights `#FFFBD8`, lips `#F8DC7C`, frame `#F2CE5E`. Every element has `"shade": false`. Added a front tube loop standing 2 px off the face (bottom right, as in PIA24201); tubes stay 2x2 px because the 1..15 hitbox leaves only 2 px at the sides. Body 10x12x10 and hitbox unchanged (script check passes).
- Reclaimer: label card 8x8 with 6x6 red cross, latch, open strip and hose ring on the north face only; south, east, west plain panel (seam, 4 bolts); side hose runs and south ring removed. `water_reclaimer.json` blockstate now has `facing=north|east|south|west` (y 0/90/180/270), front is the model's north face, same as `oxygen_maker.json`.
- `tools/art-preview/render_model.py` now honours `"shade": false`; boards script `make_maker_reclaimer_boards_v3.py`. Boards `docs/art/renders/habitat-restyle/12_oxygen_maker_v3.png`, `13_reclaimer_v3.png` (I looked at both).
- Docs: `asset-list.md` (M-05, M-06a), `style-guide.md` (tube row, new 5.5b dim-light rule).
- Tests: `./gradlew test --offline --tests '*Assets*' --tests '*Models*' --tests '*ContentFiles*' --tests '*MachineAssets*'` BUILD SUCCESSFUL.

## 2. Numbers (maker face, on-screen luminance 0..255, 2 m)
| | value | how |
|---|---|---|
| v2 | 30 | measured from `mk_2` (texture mean 93, so factor 0.33; per channel R .35 G .32 B .27) |
| v3 | 79 | ESTIMATE: texture mean 195 x measured per-channel factor, shade false (x1.25 over the north face shade 0.8) |
| hull wall | 98 | measured in `mk_2` (you quoted 102) |
v3 / wall = 0.80. Target was at least equal to the wall (83 to 102): **not met**; close to the bottom of the range. The ceiling is physical: a pure white face would give about 83. The shade:false gain is an assumption (Minecraft behaviour, not verified in game).

## 3. Honest verdict from the boards
- Reproduced: in the dim renders the maker now reads as pale-gold panels with thin dark seams, a highlight band, copper base, amber top, white tube loops. Right-hand panels are partly covered by the front loop. Tubes look grey-white in dim light.
- Not reproduced: mirror reflection (painted highlight only), the real 4x4 panel count, hex bolts, curved tubes. Blender has no smooth lighting or AO, so in-game results may differ.
- Reclaimer: one-sided and the red cross is clearly larger; back is plain. Still a clean unit, nothing like the ISS clutter; stays a "labelled equipment box" by design.
- Risk that shade:false makes the maker look flat or glowing next to shaded neighbours; judge in game.

## 4. Decisions needed
1. Lamp level 13/14/15 (ill-structured, owner): with v3 I recommend keeping 13 first and checking in game; 14 only if the maker is still under the wall.

## 5. Handoffs
> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** Check v3 in game. **Context:** boards 12 and 13; same script `habitat_makers.shots`. **Needed from them:** new shots at 2 m and 4 m; confirm the reclaimer label faces the room (dome places it facing south); measure maker face luminance vs wall. **Done when:** maker >= about 83 or a new factor is reported.

## 6. Risks
- Estimate, not measurement; shade:false effect unverified in game.
- `gen_maker_reclaimer_v2.py` and older batch scripts still write v2/v1 files if re-run; run v3 last.
- Shimmer in motion not checked.

## 7. Shared model updates
None (style guide 5.5b is the new rule).
