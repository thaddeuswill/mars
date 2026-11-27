# Cairn Dome layout (input G1 for Stage 3 slice 1)

**Owner:** Lead Game Designer · **Status:** **proposed**, a sketch (straw-man blocks allowed) · **Written:** 2026-11-27, docs only · **For:** Technical Lead (slice 1), Lead Artist (look), SME (science rows) · **Related:** [stage3-plan.md](../tech/stage3-plan.md) §3 slice 1 and §5 input G1, [balance-sheet.md](balance-sheet.md) §2d (life support, same session), DESIGN.md (Phase 1, Landing, group play), [messages/phase-1.md](messages/phase-1.md) §1 (Landing steps)

Note on the file name: the Stage 3 plan asked for `dome-spec.md`; this file is `dome-layout.md` (the request that produced it named it so). Whoever updates the plan can fix the link.

**Tags used here:** *invented* (a design number, with reasoning), *calc*, *sourced*, *unverified*, *design choice*. Nothing here has been built or playtested. Goals served: C1 (life-critical needs first, supply and demand), C2 (failure and repair), A1 (why the air matters), B1 (every change visible). Pillars: compact before wide; every change is visible; pressure, not grind.

## 1. Scale rules this layout follows

| Rule | Value | Tag |
|---|---|---|
| 1 block = 1 m = 16 px | Art rule (AGENTS.md section 2) | sourced (project rule) |
| Interior height | 3 blocks (floor at y0, air y1 to y3, roof at y4). Door 2 blocks high with a hull lintel at y3 | **design choice.** A 3 m ceiling is generous; real habitat ceilings are lower (**unverified**, not checked). Needed so the player (about 2 blocks tall in vanilla) has room to jump and see the Monitor |
| Bunk | 1 x 2 blocks | design choice (about 1 x 2 m; real crew berths are smaller, **unverified**) |
| Machine inside a bay | 1 block (16 px) each | **sourced** from `docs/science/dimensions.md` §5.1: a crew-scale MOXIE-style unit is 0.3 to 0.7 m, so it fits one block |
| Round solar array | 3 x 3 footprint, deck about 1 m (ARCHITECTURE §2.18) | sourced (project decision) |
| Shell | One structure for every group size (DESIGN.md: "the dome fits five; compact before wide survives because the dome doesn't grow with the group") | settled |

## 2. Plan view (group of 5, recommended for every group size)

Outer footprint **23 x 16 blocks** (x east, z south; north at the top). Everything inside is under one hull roof. 1 character = 1 block. Walls are one block thick.

```
x:  0         1         2
    01234567890123456789012
z0  #########=====#########   north wall; windows (=) over Ops
z1  #b....b#SJ.M.CC#XXXXXW#
z2  #b....b#.......#......#
z3  #b.....#.......#.....B#
z4  #b.....D.......D.....B#   D = interior door (z4 x7, x15)
z5  #b....b#.......#.....B#
z6  #b....b#.......#......#
z7  #TT.TTT#L......#OO....#
z8  ###D#######D######D####   doors at x3, x11, x18
z9  #......#RR...RR#......#
z10 #......#.....FF#cc....#
z11 #......####D####......E   door at x11; E = hatch stub (Phase 2 tube)
z12 *......###...###cccc..#   * = cracked hull plate (breach for Landing)
z13 #gggggg###d..###......#   chamber x10 to x12
z14 #gggggg###...###......#
z15 ###########A###########   A = outer airlock door (x11)
```

| Key | Meaning | Key | Meaning |
|---|---|---|---|
| `#` | hull plate (airtight, full block) | `=` | hull glass (airtight) |
| `b` | bunk block (5 bunks: 3 at x1, 2 at x6) | `T` | O2 wall tank (5, one per bunk) |
| `S` | Shift Board | `J` | journal lectern |
| `M` | Planetary Monitor | `C` | crafting bench (2) |
| `L` | locker: seal kits | `X` | oxygen maker bay (5) |
| `W` | water reclaimer | `B` | battery slot (3) |
| `O` | Regolith Oven bay (2) | `g` | greenhouse bed (12; empty until crops, asset F-08) |
| `R` | suit rack (4) | `F` | suit-refill wall tank point (2) |
| `d` | dust-off station | `c` | storage crate (6) |
| `D` / `A` | door (closed = airtight) | `*` / `E` | breach block / sealed hatch plate |

## 3. Rooms and what each is for

| Room | Interior (blocks) | Area | What is in it | Why it is there (goal) |
|---|---|---|---|---|
| **Shelter (bunk room)** | x1 to 6, z1 to 7 | 42 | 5 bunks, 5 O2 wall tanks along the south wall. **Player spawns here** (story: the shielded shelter) | The tanks sit next to the sleepers, so "my air" is a visible block (C1) |
| **Ops** | x8 to 14, z1 to 7 | 49 | Monitor (centre, north), Shift Board, journal lectern, 2 crafting benches, seal-kit locker. Windows look north at the weather mast | The group's common picture (DESIGN.md). Landing steps 3, 4, 8 happen here |
| **Machine bay** | x16 to 21, z1 to 7 | 42 | 5 oxygen maker bays, 1 water reclaimer, 3 battery slots, 2 Oven bays | All power draws in one room: the Monitor's Power row and the room tell the same story (B1) |
| **Hab B (annex)** | x1 to 6, z9 to 14 | 36 | Greenhouse beds (Phase 1 food, later); the cracked plate is in its west wall | Landing step 5 (patch the leak), then room to grow; glass roof, 36 blocks |
| **Storage** | x16 to 21, z9 to 14 | 36 | Crates; E hatch stub on the east wall for the Phase 2 transit tube | Compact before wide: expansion hangs off the existing shell |
| **Suit room** | x8 to 14, z9 to 10 | 14 | 4 suit racks, 2 suit-refill points | Landing step 2 (refill suit O2); a place to stop before the airlock |
| **Airlock chamber** | x10 to 12, z12 to 14 | 9 | Dust-off station on the west wall. Two doors in series: inner (x11, z11) and outer (x11, z15) | Landing step 7; perchlorate dust-off (DESIGN.md hazards) |
| Wings beside the chamber | x8 to 9 and x13 to 14, z12 to 14 | solid | Hull fill (pump spaces, not modelled) | Keeps the chamber 3 x 3 |

Interior floor about 228 blocks, about 700 blocks of air volume with doors (calc from the plan: 235 non-wall cells x 3 high). Landing walk: shelter door (step 1), tank (2), Ops Monitor (3), locker (4), annex patch (5), machine bay battery and Ovens (6), airlock and dust-off (7), lectern (8). Longest leg about 25 blocks. The route crosses back through the shelter for step 5; an alternative is to move the annex door to Ops (decision for the Technical Lead if the walk feels long).

## 4. Where things go

| Thing | Position | Reason |
|---|---|---|
| Spawn | Shelter, near the door at x7, z4 | Story and Landing step 1 |
| **Airlock** | South side, centre (outer door x11, z15). Interlock: the outer and inner door cannot be open together | Cheapest to extend (tubes go east, the hatch stub is at x22, z11) |
| **Oven** (2) | Machine bay, `O` at x16 and x17, z7 | Near the batteries and makers. Inventory grant stays (balance-sheet §2c decision 2): the player places them in these bays. A bay is a hint, not a requirement; they work anywhere on the grid |
| **Oxygen makers** (5 bays, 3 filled at start for a group of 5) | Machine bay north wall, x16 to 20 | One per player is the rule the Monitor teaches (balance-sheet §2d) |
| **Reclaimer** | Machine bay, x21, z1 | Next to the makers |
| **Battery** (3 slots, 1 filled) | Machine bay east wall, x21, z3 to 5 | Landing step 6 |
| **Solar panels** (outside) | East apron, see §6 | Panels stay far from digging |
| **Monitor** | Ops, x11, z1 (north wall, between the windows) | Everyone passes it; one shared picture |
| **Bunks** (5) | Shelter | DESIGN.md: five bunks |
| **Journal lectern** | Ops, x9, z1 | Landing step 8 |
| **Weather mast** | Outside, 5 blocks north of Ops (x11, z-5) | Seen through the Ops windows |
| **Outgasser yard** | Outside, at least 12 blocks south-west of the hull | Dust rule (balance-sheet §2a: x2 dust within 8 blocks of machinery) |

## 5. The sealing rule (what counts as sealed)

For the Technical Lead (ARCHITECTURE §2.3 gives the algorithm: flood fill, cap, cache). This is the game rule the algorithm must produce.

1. **Airtight blocks.** A full-cube block with the tag `mars:airtight`: hull plate, hull glass, closed doors and hatches, machine casings, the natural terrain. **Slabs, stairs, fences, panes, trapdoors and plants are not airtight** (a partial block leaves a gap). One rule, easy to say: "a full block or nothing" (**design choice**).
2. **Volume.** All non-airtight blocks joined face to face (6 neighbours), open doors included.
3. **Sealed.** A volume is sealed when the fill finishes inside the cap without touching outside air. If it reaches **open sky** or the **cap**, it is **not sealed**. Proposed cap **2,000 blocks** (about 2.8 times the 700-block dome so Phase 2 rooms and a tube stub fit; *invented*, the Technical Lead sets the real cap).
4. **What the game requires.** Every volume that contains a bunk, tank, oxygen maker, reclaimer, battery or the Monitor must be sealed. Hab B and Storage are not required (they hold none of these), so the cracked plate does not end the game; it does make Hab B unusable.
5. **Doors.** Closed is airtight. The airlock interlock prevents a long open path. An open interior door joins two volumes (they seal or fail together).
6. **Breach.** Breaking or replacing any wall block re-runs the fill for the affected volume only (cached otherwise). A breach event is logged once per volume, not per tick. The player sees "Unsealed" on the HUD (chip: icon + word, not colour alone) and on the Monitor; the fix is placing a hull plate or a seal kit patch, and the flag flips back.
7. **Consequences of a breach** are in balance-sheet §2d (leak of O2 and suits required). Inside an unsealed volume the player is treated as outside.
8. **Landing grace.** Nothing is lost during the checklist: the leak, the use clock and failures start when Landing is done or skipped.

## 6. Fit with the 3 x 3 solar array and the scale

| Item | Value |
|---|---|
| Pad | 3 x 3 blocks (array deck about 1 block high, models per ARCHITECTURE §2.18) |
| Pitch | 4 blocks (3 for the array plus 1 for the aisle and the brush) |
| East apron | 5 pads at x26 to 28, in a row along z from z0 to z18 (5 x 3 + 4 gaps = 19 blocks). 3 blocks of clear ground between the hull and the pads |
| Pre-placed | Pad 1 (z0 to 2), at dust stage 1 (storm left a film; balance-sheet §2a). Pads 2 to 5 are free ground for the player |
| Distance to machinery | Pads are 36 blocks from the Outgasser yard, outside the 8-block dust penalty |
| Panels needed | 1 panel for a solo dome, 2 for five players (balance-sheet §2d power table) |

Check: the apron is 19 blocks long against a 16-block dome, so it is a little longer than the dome. If the Technical Lead wants a square site, put pads 4 and 5 on a second column at x30 to 32.

## 7. Groups of 3 and 4

The shell is the same for all sizes (recommended). The group size changes what is **active**, never the build:

| Group | Bunks used | O2 tanks that start filled | Oxygen makers needed (one per player) | Makers pre-built | Panels for life support and Ovens |
|---|---|---|---|---|---|
| 3 | 3 of 5 | all 5 tanks exist; fill = 11 units per player | 3 | 2 | 2 |
| 4 | 4 of 5 | same | 4 | 2 | 2 |
| 5 | 5 of 5 | same | 5 | 3 | 2 |

Unused bunks and bays stay as empty blocks (a bunk nobody sleeps in is just furniture). Reason: one structure means one set of tests and screenshots, and a teacher can change `group.size` without a new world.

**Fallback (only if the Technical Lead wants a smaller site for 3):** shrink the three top rooms from 7 to 5 deep (bunk room x1 to 6, z1 to 5: 30 blocks, 3 bunks) and move the lower band up two rows: footprint **23 x 14**, about 130 fewer hull blocks. The shell for 4 and 5 stays **23 x 16**. A parametrised builder would need two shells and double the screenshot work, which is why it is not the recommendation.

## 8. Worked blocks list (straw-man, group of 5 shell)

Counts come from the plan above (calc, not run in game). Wall columns: 133 (126 `#`, 5 windows, 1 cracked plate, 1 hatch) x 3 high.

| Item | Count | Note |
|---|---|---|
| Hull plate, walls | 389 | 133 columns x 3, minus 10 window blocks |
| Hull plate, roof | 332 | 368 roof cells minus the 36 annex glass cells |
| Hull plate, door lintels | 7 | one above each door |
| **Hull plate total** | **about 728** | One block type, one texture (straw-man) |
| Hull glass | 46 | 10 windows (5 wide x 2 high) + 36 annex roof |
| Doors | 7 | 6 interior, 1 outer airlock door; each 2 high |
| Cracked hull plate | 1 | Landing step 5; replaced by a plate |
| Floor | about 235 | Floor plates inside |
| Placed content | 5 bunks, 5 tanks, 5 maker bays, 1 reclaimer bay, 3 battery slots, 2 Oven bays, Monitor, Shift Board, lectern, 2 benches, locker, 4 racks, 2 refill points, dust-off station, 6 crates, 12 greenhouse beds | Machines are the Technical Lead's blocks; bays are floor markers |

The structure should be one NBT or `structure` template placed at spawn so the Technical Lead can test sealing on a known shape; the Artist reskins the hull and glass later.

## 9. Open questions

| # | To | Question |
|---|---|---|
| 1 | Technical Lead | Flood-fill cap (proposed 2,000) and whether partial blocks should be airtight in survival; both affect the "full block or nothing" rule |
| 2 | Lead Artist | Hull plate and glass straw-man; frost rim and window glow are not needed in slice 1 |
| 3 | SME | The 3 m ceiling and 1 x 2 m bunk are readability choices, not data; if a real figure matters for the journal, say so |
| 4 | Lead Instructional Designer | Landing step order walks back through the shelter (step 5); confirm the order or move the annex door |
