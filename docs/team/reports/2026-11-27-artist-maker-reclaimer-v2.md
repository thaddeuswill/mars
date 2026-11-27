# Artist report: oxygen maker v2 and water reclaimer v2 (2026-11-27)

Assets only, no Java, no client run. Real-reference compare done in Blender (not Minecraft). Not yet seen in game.

## Handoffs received
Technical Lead (hull-lights report): maker reads as a brown crate, reclaimer as a clean cube. Accepted; both reworked.

## Done
- `tools/art-preview/gen_maker_reclaimer_v2.py` (new; `gen_habitat_restyle.py` no longer writes these). Writes `o2_generator.json`, `water_reclaimer.json`, textures `o2_generator_{face,back,end,side,top,tube}` (same names), new `o2_generator_base`, `o2_generator_kapton`, `water_reclaimer_side`, `water_reclaimer_hose`, rewritten `water_reclaimer`, `water_reclaimer_top`.
- Maker: gold ramp brightened (#2A1D0A groove, #9C7226, #D8AC40, #F0CE66, #FFEBA6 specular), diagonal specular band, 3 x 4 pocket grid on three faces, X ribs east, copper base plate, amber Kapton top plate with silver connector, two white 2 x 2 px tube loops (was 1 px) with 2 x 3 px silver fittings. Body 10 x 12 x 10 px (slightly smaller than v1's 12 x 12 x 10; about 3x MOXIE). Style guide 5.5a allows this; hitbox (Java 1..15, 0..15, 1..15) unchanged and all elements stay inside it (script checks). Style guide tube row updated.
- Reclaimer: element model with hose loop standing off N and S faces, grey hose run and green braided hose run with clamps on E and W, overhanging top shroud with blue handrail edge; N/S texture has label card with red cross (#C8322B, shape carries the meaning), two latch handles, dark open-panel strip with silver studs and a green card; E/W has latches, cut-outs, tag. The block has no facing, so N=S and E=W.
- Boards: `docs/art/renders/habitat-restyle/10_oxygen_maker_v2.png`, `11_reclaimer_v2.png` (script `make_maker_reclaimer_boards.py`; dim light = Minecraft face shade x (0.53, 0.48, 0.40), measured from hab_11 screenshot: hull (218,214,201)x0.8 shows as (92,82,64)).
- `asset-list.md` updated. Test run: `./gradlew test --offline --tests '*Assets*' --tests '*Models*' --tests '*ContentFiles*'` BUILD SUCCESSFUL.

## Numbers (gold pixels only; luminance 0..255; spread = mean of top 10 % minus bottom 10 %)
| | mean | spread |
|---|---|---|
| Real MOXIE face (studio light) | 99 | 78 |
| Old v1, normal | 105 | 104 |
| New v2, normal | 140 | 103 |
| Old v1, dim | 65 | 37 |
| New v2, dim | 72 | 54 |

Reclaimer clutter (share of pixels with a luminance step; rough metric): real racks 0.32 and 0.30; old cube 0.13; new 0.16.

## Honest verdict
- Reproduced: gold with dark grooves in a 3 x 4 grid, bright tubes and fittings, copper base, amber top. In the board it now reads as a gold instrument box on a plate, clearly not wood.
- Not reproduced: real specular reflection. In dim light the spread rises from 37 to 54 (+46 %) but is still below the real photo's 78 (studio lit), and mean gold luminance (72) is still below the dim wall (about 83). Brightness in the room (lamps) is the other half of the fix; art cannot reach it alone. The highlight band is painted, so it does not move with the light.
- Reclaimer: clutter metric only rose 0.13 to 0.16 against 0.31 for the real racks. It now has labels, a hose, an open strip and handrails, but is still much cleaner than the ISS, which cannot be matched in one 16 px block. Looks better to my eye, not close to the photos.
- The red cross is deeper red than the Okabe-Ito vermillion on purpose; colour is not the only cue (cross shape). Gold #D8AC40 is close to safety orange #E69F00 in hue, but the maker carries no hazard colour.

## Decisions needed
Bed, crate, bench custom textures (Technical Lead clash list; well-structured, my recommendation): yes for the red bed and the barrel/crate stand-ins, low priority for the crafting table. They are the vanilla look in a dome that is otherwise styled, and the owner wants things to match references. Cost: 1 texture set each (bed 2 textures plus model parent, storage crate 3 faces, bench 3 faces), about 0.5 day total in textures only, but each needs a Technical Lead decision whether to register own blocks or use a resource-pack override of the vanilla texture (override is cheapest and assets-only, but changes vanilla blocks everywhere, also outside the dome). Recommendation: override the vanilla bed and barrel textures in the pack; leave the crafting table alone until the machine recipes exist.

## Handoffs
> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** Check v2 in game. **Context:** boards 10, 11; generator in `tools/art-preview/`. **Needed:** one screenshot of the machine bay at the same view as hab_11 and hab_7b. **Done when:** maker reads as gold box and reclaimer shows label and hose.

> **To:** Technical Lead · **Problem type:** ill-structured (art, level design)
> **Problem:** Machines stay dim in the bay. **Your view:** a lamp near the machine bay would do more for the maker than more texture value. **Needed:** try one extra lamp or higher light level over the bay. 

> **To:** Technical Lead / Game Designer · **Problem type:** well-structured
> **Problem:** Reclaimer has no `facing`; clutter is repeated on both ends. Adding facing would let the label face the room. **Done when:** property added and blockstate variants made by me.

## Risks
- Not seen in game; Blender has no smooth lighting. `gen_phase1_batch1.py` still writes the v1 maker model if re-run; do not run it for these assets.
- Texel-heavy front (2 x 2 px pockets) may shimmer at distance without mipmaps; check in game.

## Shared model updates
None.
