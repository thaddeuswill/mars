# Report: Lead Artist, batch 3 (BACKLOG #28)

- **Role:** Lead Artist
- **Date:** 2026-11-26
- **Outputs:** `tools/art-preview/gen_round_panel.py` and `gen_phase1_batch1.py` (both now write block models and textures only), `pack/assets/mars/` (round panel models `solar_panel_round_dust0..4` and `solar_panel_round_partly_cleaned`, updated box machines), renders in `docs/art/renders/phase1-batch3/` (`before_after.png`, `contact_sheet.png`, `panel/contact_sheet.png`), `docs/art/style-guide.md` §2.4, §5.5, new §5.5a, `docs/art/asset-list.md`, the rebuilt viewer
- **Not checked:** how any of it looks inside Minecraft (no game re-run). All pictures are Blender renders and a viewer screenshot.

## What changed
1. **Round panel is the game panel; the square set is retired** (models and textures deleted). 2.2 m (35 px across the corners), 12 segments, softened dark tan-brown seams at stages 3–4. Dust steps (each stage keeps the previous stage's dust): 0.067, 0.138, 0.120, 0.040 OKLab ΔE, all above the 0.035 minimum. **"Partly cleaned"** is rebuilt as a full veil with large connected clean wedges (some segments wiped to the rim, some halfway, some untouched); its mean colour differs from stage 4 by 0.116 ΔE (the check needs 0.10 or more); the largest clean area is 29% of the cells. Model names match the Technical Lead's blockstate.
2. **Placeholder marker is now 1 px**, at the top-left pixel of each texture's largest region; style guide §5.5 updated. An inventory-only overlay would need code (offered below).
3. **Orphan assets removed:** old blockstates and item models; both generators now write block models and textures only, so they can't recreate orphans.
4. **Oxygen maker** stays one block, and the grid now reads as framed panels (2 × 3 light gold panels with thin mid-gold ribs in a light outer frame, no deep shadows; tube loops and silver fittings kept). Its size, 0.75 × 0.625 × 0.75 m, is about **3 times the real MOXIE**, as the owner chose.
5. **Other machines at real scale:** the **battery** is now a wall pack, 1.0 × 0.69 × 0.19 m, on a small invented foot with no terminals, using the Earth home-battery stand-in from `dimensions.md` §5.3 (no space-habitat size exists). For the **outgasser, oven, and monitor** no reference photo or real size was found; designs kept, sizes marked invented. New style guide §5.5a lists every feature under 1 px drawn larger on purpose (seams, cells, cut-outs, panel thickness and rim, tubes, ribs, status lamps, screen lines, stripes, future mast sensors) plus the oxygen maker's 3× scale-up.
6. Style guide §2.4 and asset list rows updated; `render_model.py` and the viewer frame large models and lay the overview out by real width (checked in headless Chrome).

## Checks
`gen_phase1_batch1.py`: palette only (now ignoring transparent pixels), lamps inside dark rings, contrast pairs, colour-blind checks: all pass. `gen_round_panel.py`: palette, stage superset, step sizes, panel structure, "partly cleaned" distinctness: all pass.

## Coordinator's look at the result
In `before_after.png` the round panel at real scale (next to the 1.8 m player box) is a flat disc that looks far smaller and lower than the old square panel, and reads as a dark or tan disc on a post. The clean disc is near-black with a clear 12-way seam pattern; stage 4 is an even tan veil with thin seams (much closer to InSight 2022 than before); partly cleaned shows large dark wedges clearly different from stage 4. The marker is now barely visible. The battery as a wall pack reads as a thin tall slab. In the game the disc's overhang, lighting, and hitbox are untested.

## What the Artist couldn't judge
How it looks in the game (lighting under the 1 m overhang, whether the 1 px marker is visible at play distance, partly cleaned against stage 4 in a wide shot); how the outgasser, oven, and monitor compare to real hardware (no reference exists).

## Decisions needed (classified by coordinator)
| Decision | Recommendation | Type |
|---|---|---|
| Battery as a wall pack with no terminals; and the journal line that the oxygen maker is about 3× MOXIE | Confirm both | Well-structured |

## Handoffs
- **Technical Lead:** the round panel is 3 × 3 blocks, overhanging about 1 m (16–17 px) on every side of its centre block; element extent x/z −9 to 25 px, centred on the block; disc top at y 11 px (0.69 m), 1 px thick, 23 elements per model; batch 1's facing variants still apply. Needed: the hitbox and canopy rule; a lighting check under the overhang; a new screenshot run including "partly cleaned" next to stage 4 in a wide shot; optionally an inventory-only placeholder overlay if 1 px still distracts.
- **Game Designer:** confirm the wall-pack battery and the "about 3× MOXIE" journal line.

## Shared model updates
- **Expectation:** art generators write block models and textures only; blockstates and item models belong to the registered blocks (Technical Lead).
- **Lesson:** two roles editing the same asset names in parallel nearly broke the block-to-model mapping; check the live blockstate before naming models.
