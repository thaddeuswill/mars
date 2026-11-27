# Lead Artist: hull light panel (ceiling light), assets only

## 0. Handoffs received
TL habitat wiring report, "dark machine bay": accepted. Recommendation of a ceiling light block per room is built as assets here. Whether the light should go off in a Blackout is a Game Designer question (section 3).

## 1. Done
- `tools/art-preview/gen_hull_light.py` writes: `pack/assets/mars/textures/block/hull_light.png` (lit), `hull_light_off.png` (SPARE, unlit, no state uses it), `models/block/hull_light.json` (full cube: panel on the down face, `hull_plate` on the other five), `models/block/hull_light_off.json` (spare), `blockstates/hull_light.json`, `models/item/hull_light.json`; lang `block.mars.hull_light` = "Hull Light Panel" (en_us); loot table `pack/data/mars/loot_table/blocks/hull_light.json` (drops itself; data file, copied from hull_plate_base, say if you want it removed).
- Texture: 16x16, 6 colours, no pure black/white, no marker. 1 px seam `#7C786C` (same as hull plate), aluminium bezel `#A8B0B7` then `#7E8790`, cream diffuser `#E6D2A8` / `#F5E3C3` (the dome lamp colour, style guide section 8) / `#FBF1DA` core. Corner screws. Lit vs unlit differ by value (light cream vs grey), not hue.
- `tools/art-preview/render_model.py`: new `bottom` view (camera under the model, for ceilings). `make_habitat_boards.py`: new `board09()`.
- Board `docs/art/renders/habitat-restyle/09_hull_light.png` (looked at; boards 01-08 untouched). `docs/art/asset-list.md`: row M-01c.
- Test run: `cd mod && ./gradlew test --offline --tests '*Assets*' --tests '*Models*' --tests '*ContentFiles*'` BUILD SUCCESSFUL (all passed). Note these tests do not list hull_light by name; I did not edit tests. Not run: game, screenshots.

## 2. What the board reproduces and does not (I looked at it)
- Reproduced: a flat cream-white diffuser in an aluminium bezel, same seam and palette as the hull plate; reads as a lit rectangle in a plate ceiling (flat mock-up, right panel).
- **Not reproduced, and the references do not contain it:** I opened the four references. CHAPEA shows an evenly white translucent shell with small round spot lamps on the wall; the ISS photos show only a blurred lit light-box edge (rack-open photo, upper right). **There is no flat LED ceiling panel in any of them.** The panel is a stylised stand-in that gives the same effect (even, flat, warm-white light). The owner's premise "flat LED ceiling panels" is not what these photos show; the stand-in is deliberate and should be accepted knowingly.
- Not reproduced: true emission (the texture is only bright; the block light level does the work), LED strips, lamp depth. The Blender "from below" panel shows the lamp dull because the renderer applies Minecraft's down-face shade (0.5); in game the face takes the light of the room cell beneath it, so it should look bright: **unverified in game**.

## 3. Handoff to the Technical Lead (exact)
> **To:** Technical Lead · **Problem type:** well-structured
> **Block:** `mars:hull_light`, class `HullPlateBlock` (airtight full cube) via the existing `plate()` helper plus `.lightLevel(s -> 12)`; tag it in `airtight.json`; add item (`registerSimpleBlockItem`), creative tab via `HullBlocks.ALL`. Assets and loot exist. New `DomeTemplate.Kind.HULL_LIGHT` mapped in `StarterDome.stateFor`.
> **Light level 12 of 15.** Why: the lit shelter's real level is not known to me (the screenshot walls read about 95 to 113 grey, i.e. a middling light). 15 would flatten the white hull and glow like a sea lantern; 12 gives 11 at the ceiling cell below and 9 straight under it, and with the spacing below the worst floor cell is 7. If the bay still looks dark in the next screenshot, raise to 13 (one constant); do not go to 15. Please read the shelter's actual level (F3) and match or beat it by 1.
> **Where (option B, recommended): replace the roof cell at y4 above an interior floor cell**, not an object in the room. The block is a full airtight cube like the hull plate it replaces, so the flood fill, the tag lists and the air volume are literally unchanged (airtight stays airtight; a thin non-airtight panel inside the room at y3 would also leave the fill unchanged only as long as nobody tags it airtight, needs a partial shape, and could be bumped; rejected). It lights the y3 cell beneath it. Keep it off the rim ring (perimeter y4 stays `HULL_RIM`). Coordinates are template (x, z) with y = 4; they light the y3 air cell under them:
>
> | Room | Lights | Cells (x,z) |
> |---|---|---|
> | Machine bay (x16-21, z1-7) | 6 | (17,2) (20,2) (17,4) (20,4) (17,6) (20,6) |
> | Hab B annex (x1-6, z9-14) | 4 | (2,10) (5,10) (2,13) (5,13); these replace ribbed glass cells: glass 36 becomes 32 |
> | Storage (x16-21, z9-14) | 4 | (17,10) (20,10) (17,13) (20,13) |
> | Suit room (x8-14, z9-10) | 3 | (9,10) (11,10) (13,10) |
> | Airlock chamber (x10-12, z12-14) | 1 | (11,13) |
> | Shelter, Ops | 0 for now | already lit in the screenshots; add the same pattern (x2,x5 by z2,z4,z6 and the Ops equivalent) only if they look dark at night |
>
> Total 18. Rule: lights every 3 cells, so no floor cell is more than 2 cells (Manhattan) from a lamp; floor level at least 12 - 3 - 2 = 7 (calc, not tested in game).
> **Tests to update:** `DomeTemplateTest` counts (ribbed glass 36, interior roof hull cells) change; add a test that lamps sit only at interior roof cells and that a sealed volume stays sealed with them.
> **Cost:** not craftable in this slice. Creative tab only, plus the starter dome placement. Later, a KubeJS recipe (proposal, **invented**, Game Designer to confirm): 2 hull plate + 1 copper ingot + 1 glass pane gives 2. No vanilla glowstone (a Nether item).
> **Does not touch sealing:** no change to `mars.dome` or flood fill code, only a new cell kind and the airtight tag entry.
> **Done when:** the next machine-bay screenshot is lit evenly, `RESULT PASS`, tests updated.

## 4. Decisions needed
- Should the lights go off in a Blackout (a `lit` property tied to the power grid)? Ill-structured (feel versus playability and cost). My view: yes later, it makes the Blackout visible (pillar "every change is visible"); `hull_light_off` is ready for it. Not needed for this slice. Route: Lead Game Designer.
- Accept the stylised panel given the references show no flat panel? Well-structured: yes, stylisation is recorded here and on board 09.

## 5. Handoffs
- Technical Lead: section 3.
- Lead Game Designer: the Blackout question above.
- Not answered: the earlier TL handoff about red vanilla beds and crates clashing with the white habitat (separate task).

## 6. Risks
- Light level 12 is a guess against an unmeasured shelter level. Hab B glass roof loses 4 cells. Optional: NeoForge per-face emission data would make the lamp face fullbright but I could not verify the schema for 21.1, so it is not used.

## 7. Shared model updates
None.
