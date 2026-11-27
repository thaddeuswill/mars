# Artist report: round solar array raised to deck height

**Date:** 2026-11-27 · **Role:** Lead Artist · **Task:** raise the round array to real lander deck height (SME: 16 px).

## 0. Handoffs received
SME handoff (`2026-11-27-sme-deck-height.md`): set the array 16 px above its base and cite InSight 0.83 to 1.08 m. **Accepted.** I read "16 px above its base" as the array's **underside** at 16 px (the array rides on the deck), so the 1 px disc is at **y 16..17**. This is one constant (`DECK_Y`) in the generator. The alternative, disc top at 16 px (`DECK_Y = 15`), is covered below.

## 1. Done
- `tools/art-preview/gen_round_panel.py`: disc now at y 16..17 on a lander-deck mount: 12 x 12 px base plate (y 0..1), three 2 x 2 px legs at 120 degrees (y 1..14), octagonal deck plate 14 px across and 2 px thick (y 14..16). All mount parts sit inside x/z 0..16 (the centre block). 30 elements per model (was 25). Element bounds: **x/z -9..25 px (unchanged), y 0..17**. All generator checks pass (palette, superset dust rule, steps, partly cleaned distinct, structure). Textures are unchanged (same 48 x 48 top textures).
- Regenerated `pack/assets/mars/models/block/solar_panel_round_dust0..4.json` and `solar_panel_round_partly_cleaned.json`. Worked on a scratch copy, then copied the generator over and re-ran it in the repo.
- Renders: `docs/art/renders/raised-array/before_contact_sheet.png` and `after_contact_sheet.png` (iso with player box, front, top, 32 x 32 silhouette; dust0, dust4, partly cleaned).
- Viewer rebuilt: `docs/art/viewer/index.html` and `viewer.fragment.html`.
- Notes: `docs/art/style-guide.md` (raised-array paragraph with the SME source and stylizations), `docs/art/asset-list.md` (M-09), `tools/art-preview/README.md`.

**Source cited:** InSight deck 0.83 to 1.08 m (13 to 17 px), NASA/JPL InSight press kit, "Spacecraft" page, via the SME report. **Not directly verified** (the SME could not open the page); one conflicting PDS figure (about 0.5 m, unstated reference) is unresolved.

**What I looked at (Blender contact sheets, both before and after):** the disc is clearly higher; the front view reads as a flat disc on a deck with three legs; the 32 x 32 silhouette is distinct from the old "table on one post" (deck step plus three legs). The player box (1.8 m) now reaches well above the disc. **Not verified:** the browser viewer (rebuilt and structure-checked only; I did not open it in a browser, and three.js loads from a CDN); anything in Minecraft (lighting, AO, selection box, collision); the item model and inventory icon (the item parent is `solar_panel_round_dust0`, so the icon will change, unseen).

## What the shape code must change (Technical Lead, `ARCHITECTURE.md` 2.18; I did not edit Java)
At 16 px the disc's underside is the block-top boundary, so the outline slice **moves up one block**.

| Part | Today | Needed (disc at y 16..17) |
|---|---|---|
| Canopy slice (8 cells) | same height as centre, local **y 10..11 px** | cells at **centre.y + 1**, local **y 0..1 px** (16..17 px in model space) |
| Centre cell's own part of the array | in the centre block, top at 11 px (`[0,0,0]->[1,0.6875,1]`) | no longer in the centre block: needs a **9th canopy cell at centre.y + 1** (local y 0..1, the middle of the outline) |
| Centre block shape | base + post + array over own cell, top at 11 px | base + legs + deck: bounding box about `[0.0625,0,0.0625]->[0.9375,1.0,0.9375]` (deck 14 px wide up to y 16); or keep a thin post-only box `[3,0,3]->[13,16,13]` px if collision should stay simple |
| Placement check | 8 cells free (same level) | the **9 cells at y+1** must be free (and the 8 neighbours at y stay free to build on: battery beside it still fits; the mount does not leave the centre block) |
| Break/remove, loot | any cell breaks all | same, with cells now at y+1 |
| Tests | `footprint.*` read y 10..11 | update expected shapes; `footprint.corner_shape` becomes `[0.4375,0,0.4375]->[1,0.0625,1]` (the same footprint, local y 0..1) |

**Cheaper alternative (one-line art change, minimal Java):** set `DECK_Y = 15`. The disc is then y 15..16, still inside the centre block's height; canopy cells stay at the same height and only the slice becomes **y 15..16 px** (top 1.0). The array's top is at 16 px, its underside at 15 px (inside InSight's 13 to 17 px range). Nothing moves up a block; the only cost is the "top of the deck" reading is 1 px lower than the SME's number. I recommend whichever the Technical Lead finds cheaper; both are within the SME's range. Tell me and I regenerate in seconds.

Also: the "player can walk under it" question: with a 1 m deck, neither option lets a 1.8 m player under the array; the ground cells beside the centre remain free in the primary option, and the overhang is only in the cells above.
Known 2.18 look issue "low round tables from the front" is now smaller (deck and legs), but the front view still reads somewhat table-like; the three legs help.

## 2. Decisions needed
- **Which reading of "16 px":** underside at 16 (disc y 16..17, canopy one block up) or top at 16 (disc y 15..16, minimal Java). Well-structured; both inside the SME range. Recommendation: let the Technical Lead pick by cost; I prefer underside at 16 because it matches how a deck mount actually works, but 15 is fine if Stage 2 is tight.

## 3. Handoffs
> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** the round array's shape code must follow the raised model.
> **Context:** table above; `pack/assets/mars/models/block/solar_panel_round_dust*.json` already updated (disc y 16..17).
> **Needed from them:** either move the canopy cells up one block (9 cells at y+1, local y 0..1) or ask me for the `DECK_Y = 15` variant; update the `footprint.*` GameTests and the 2.18 table.
> **Done when:** GameTests read the new shapes and an in-game screenshot shows the disc, selection box, and collision matching.

> **To:** Subject Matter Expert · **Problem type:** well-structured
> **Problem:** re-check the NASA InSight deck figure once `jpl.nasa.gov` is reachable; resolve the PDS 0.5 m conflict.
> **Needed from them:** confirm 0.83 to 1.08 m. **Done when:** the style guide note loses "not directly verified".

## 4. Risks
- The deck-height source is unverified (low risk; three sources agree on about 1 m).
- Collision and selection will not match the model until the Java is updated; an in-game screenshot may show the array floating over an old-height shape.
- Legs are vertical and the deck is 1 m, not the real 1.56 m (stylized, written in the style guide).
- Element count 25 to 30 per model: negligible.

## 5. Shared model updates
None. (If the Technical Lead adopts the underside reading, update `ARCHITECTURE.md` 2.18 with the new canopy height.)
