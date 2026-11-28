# Artist report: habitat bunk and crate v2 (2026-11-28)

## 0. Handoffs received
- SME audit section 9 (bunk colour, label colour, skid, stowage mix): **accepted**, all applied except where noted below.
- Technical Lead (bag 0.53, crate 0.6 of the wall in game): **accepted**; bag lifted well beyond a third, `shade:false` added.

## 1. Done
Generator `tools/art-preview/gen_habitat_furniture.py`; boards script `tools/art-preview/make_furniture_boards.py`. Same file names, same blockstates and model conventions; no Java.
- **Bunk**: bag is light green `#7F9C69 #98B37F #B0C896 #CBDDB4` (CQ photo) with white zip tape and one white Velcro patch; strap is navy `#566796` with white stitches and a silver buckle (Harmony bag bands); frame is plain grey alloy. **Drawers dropped**: no photo shows drawers, and CHAPEA's low bed has a plain dark base. Head board carries tan Velcro squares and a yellow pouch (CQ wall cue). White tether cords left out (microgravity cue, no use on Mars). Bag element `shade:false`.
- **Crate**: yellow label card `#F2CF3A` with text lines (no real text), three dark cut-out slots, two grey latches, seam; skid **shrunk** to a 1 px grey kick plate (same file `stowage_crate_skid`); door `shade:false`; body 1..16 px, door 2..14 px. Blue band removed.
- **Optional, not registered**: `models/block/stowage_bag.json` + `textures/block/stowage_bag_cloth.png` (two stacked soft bags, tan straps, grey buckles, yellow card); `textures/block/habitat_wall_velcro.png` (quilt grid, beige Velcro squares, a stuck-on tablet).
- Boards: `docs/art/renders/habitat-restyle/14_habitat_bunk_v2.png`, `15_stowage_crate_v2.png` (looked at both). `docs/art/asset-list.md` row M-34.

## 2. Real output
- Generator: `OK`, every texture at most 6 colours.
- `./gradlew test --offline --tests '*Assets*' --tests '*Models*' --tests '*ContentFiles*' --tests '*HabitatAssets*'`: BUILD SUCCESSFUL. No client run.
- Luminance (Rec. 709 on texture mean, times lamp factor 0.4375/0.396/0.3375 from board 12, against `hull_plate` mean 206 -> 82.6 dim): **bag top 75.2 = 0.91 of wall** (aim 0.75), crate door 77.0 = 0.93, soft bag 1.01. Old blue bag measured 0.53 in game.

## 3. Reproduced / not reproduced
Reproduced: muted green quilted bag, white zip, Velcro patch, navy bands, low bed with plain frame, white pillow, yellow label cards, grey latches, cut-outs, no skid, soft strapped bag variant. Not reproduced: enclosed compartment and cloth door, olive variant, grey metal colour of lockers (we keep warm-white hull), 2 x 2 door grid, barcodes, strap criss-cross, real bag shape (flat slab).

## 4. Still guessed
All hex values (by eye from photos). The ratio is computed from texture means, **not** measured in game: it ignores face shading, ambient occlusion, and the bag top vs wall light angle, so the real ratio will be lower than 0.91; the old one computed the same way would have been about 0.7 yet measured 0.53, so expect roughly 0.65 to 0.8 (rough estimate, unverified). Item-icon display transforms unchanged and unchecked.

## 5. Decisions needed
None.

## 6. Handoffs
> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** optional assets exist but are unregistered; v2 look needs an in-game check.
> **Context:** `stowage_bag` model, `stowage_bag_cloth`, `habitat_wall_velcro` textures; `shade:false` on the bag and crate door.
> **Needed from them:** next screenshot pass: measure bag and crate vs wall; optionally register the soft bag (as a second crate look or decoration) and the Velcro wall variant.
> **Done when:** ratio at or above 0.75 or Artist sends a new value.

## 7. Risks
Bag may still read low in game; the grey frame is lighter than CHAPEA's black one (deliberate for dim light).

## 8. Shared model updates
Lesson: computed texture luminance over-predicts in-game luminance; keep measuring in game.
