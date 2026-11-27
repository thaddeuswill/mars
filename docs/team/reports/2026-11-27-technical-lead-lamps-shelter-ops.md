# Technical Lead: lamps in the shelter and Ops, light level 13

## 0. Handoffs received
Owner direction (recommended options): level 13, lamp shelter and Ops about 6 each, worst floor cell at least 8, screenshots incl. night. **Accepted**, all done.

## 1. Done
- `HullBlocks.LIGHT_LEVEL` 12 -> 13. Jar `mod/build/libs/mars-0.3.4-stage3-e.jar`, sha256 `cf77314dcfe554e1647797bcd936a4fc8f58908dcc78bc46523a974b9473fb3c` (`gradle.properties` bumped).
- `DomeTemplate.LAMPS`: 18 -> 30. Shelter 6: (2,2) (5,2) (2,4) (5,4) (2,6) (5,6). Ops 6: (8,2) (14,2) (11,2) (9,6) (13,6) (11,5). All are interior roof cells at y 4, none on the perimeter rim; none sits over a bunk cell (shelter lamps are at x2 and x5, bunks at x1 and x6; and a roof lamp does not clash with a bed anyway).
- Ops placement note: my first two regular layouts (2x3 grids) left a cell at light 7 (GameTest caught both). I searched all symmetric 6-lamp sets by script for "every air cell within 2 of a lamp" (Shift Board cell at x8,z1 counts as air); the set above is the first that fits.
- Tests: `DomeTemplateTest` counts (30 lamps, 6 shelter, 6 Ops); GameTest `dome.lights_machine_bay_lit` now asserts worst floor cell >= 8 in the bay, shelter, Ops, Hab B, storage, suit room, airlock and the cell under a lamp >= 12. Seal tests kept and passing.
- Docs: ARCHITECTURE 2.20, asset-list M-01c, BACKLOG, HANDOFF 0e, AGENTS.md. New screenshot script `tools/screenshots/habitat_lamps.shots`.
- Not touched: maker/reclaimer textures, `mars/learning/**`.

## 2. Measured light (real light engine, GameTest, floor cells y 1)
| Room | No lamps | With 30 lamps, level 13 (worst cell) |
|---|---|---|
| Shelter | 0 | 8 (at 3,1) |
| Ops | 0 | 8 (at 8,4) |
| Machine bay | 0 | 8 (16,3); at head height 9 |
| Hab B annex | n/m | 8 (1,9) |
| Storage | n/m | 8 (16,9) |
| Suit room | n/m | 8 (8,9) |
| Airlock chamber | n/m | 8 (10,12) |
Cell under a lamp (y 3): 12. (n/m = not measured without lamps.)

## 3. Screenshots (new run, folder `tools/screenshots/out/20261127-233344/`, looked at lamp_1, 2, 3, 5, 6, 7 and the CHAPEA reference)
- **Bright and even like CHAPEA/ISS? Partly.** Evenness: yes, no black corners, floors and walls read the same everywhere (shelter lamp_1, Ops lamp_2). Brightness: it reads "well lit room", not "bright white": surfaces are mid-light beige with the dark speckles and seams, nothing glows. CHAPEA's shell is near-white and shadowless; ours still has darker wall shading and a clear brown cast. Lamp panels are visible in the ceiling (cream rectangle in a bluish-grey bezel), top of lamp_1 and lamp_2.
- **Colour of the light: warm beige, not bluish white.** The tint is mostly the surface textures themselves (hull plate and floor are tan/cream pixels) multiplied by the lightmap, which in vanilla's block-light colour is a warm orange-ish white. Tunable without code: (a) the hull plate, floor and rim textures (cooler, whiter pixels; Artist), (b) a resource-pack/shader-free option is not available for lightmap colour in vanilla 1.21.1; the block-light tint is a client constant, so a bluish light needs code (a client-side lightmap tweak, not recommended) or cooler textures. Light level itself is the single constant `HullBlocks.LIGHT_LEVEL` (max 15, which would raise the worst cell to 10).
- **Night (/time set 18000, lamp_5, lamp_6):** the shelter and Ops look the same as by day (hull roofs, no sky light reaches them), the Ops portholes go dark. So the lamps alone carry the room, which is the intended result. At night the room reads a little darker only at the window wall.
- **Not informative:** lamp_3 and lamp_7 (machine bay) show a close wall corner with a maker; the camera position in my script faces a wall, so they say nothing about the bay beyond "evenly dim-lit at that corner". Hab B (lamp_4, lamp_8) not looked at. Bay brightness is covered by the measured number only.
- Vanilla bunks (red), barrel and bench remain the biggest visual clash with the white habitat (see the earlier report section 5).

## 4. Run record
- Gradle invocations: 1 build+test (first), then 3 rounds of (spotlessApply spotlessCheck build test --rerun --offline + runGameTestServer --offline) while fixing the Ops layout, 1 screenshot run (own Gradle call). About 9 Gradle runs in all. Final: `spotlessCheck build test --rerun --offline` BUILD SUCCESSFUL in 6 s (wall 6.6 s), 254 unit tests 0 failures; `runGameTestServer --offline` `MARS-SELFTEST RESULT PASS passed=113 failed=0` (wall 13.7 s). Screenshot run 47 s.
- Not run: dedicated server, real client with the jar in `mods`, Blackout.

## 5. Decisions needed
None new. Ill-structured and still open (Lead Game Designer): do lights go off in a Blackout? Optional (Artist): cooler hull textures if bluish-white is wanted.

## 6. Handoffs
> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** the light reads warm beige because the hull textures are tan; CHAPEA/ISS are bluish white.
> **Needed from them:** decide whether hull plate, floor and rim get a cooler, lighter palette.
> **Done when:** a decision is in asset-list.md.

## 7. Risks
- 30 lamps are 30 breakable roof blocks; breaking one opens its room (intended).
- Level 13 is not "white"; the remaining warmth is texture and client lightmap.

## 8. Shared model updates
- Lesson: choose lamp positions by a coverage search plus the GameTest measurement; hand layouts missed a cell twice (cells count as air even when only a "marker" such as the Shift Board spot).
