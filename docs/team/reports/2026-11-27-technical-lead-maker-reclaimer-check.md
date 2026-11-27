# Technical Lead: maker and reclaimer v2 check (2026-11-27)

## Handoffs received
Artist (`2026-11-27-artist-maker-reclaimer-v2.md`): check v2 in game; try more light over the bay; estimate a facing property for the reclaimer. Accepted all three.

## Done (all run)
- `./gradlew spotlessCheck build test --rerun --offline`: BUILD SUCCESSFUL. `runGameTestServer`: `MARS-SELFTEST RESULT PASS passed=113 failed=0`. The v2 models and textures load; hitbox and shape tests are green.
- Jar `mod/build/libs/mars-0.3.5-stage3-f.jar`, sha256 `9f72d2808e8ca88a83515fb4d0f8394402d05a70b521a05c24dae728cfcc59ed` (`mod_version` bumped in `mod/gradle.properties`).
- Script `tools/screenshots/habitat_makers.shots` (camera computed from the dome template: world = template - (3,4); makers at template x16..20 z1 = world x13..17 z-3; reclaimer world x18 z-3; camera at z 1.5 or -0.5 looking north, yaw 180). Output `tools/screenshots/out/20261127-233726/` (`mk_1` wide, `mk_1b` closer, `mk_2` maker 2 m, `mk_3` maker 4 m, `mk_4` reclaimer 2 m, `mk_5` reclaimer 4 m, `mk_6` to `mk_8` night). I looked at the PNGs. Only 1 maker is placed (solo group, prebuilt = 1), at the west end of the row.
- Day and night shots are identical: the dome is enclosed, so only the lamps light it.

## What the images show (compared with PIA24201.jpg and iss_wrs_rack_open_fincke.jpg)
- Maker: now reads as a gold-framed instrument box on a copper plate with an amber top and a pale tube loop on the side. It is no longer a crate. It is not a MOXIE look: the dark grooves dominate, so at 2 m and 4 m it looks like a dark lattice with amber windows (a grate or oven) rather than polished gold plates. The real unit is bright gold with thin dark seams. The painted highlight does not catch the light. White tubes are small and hard to see. Fine detail (hex bolts, X ribs) is lost at 4 m.
- Reclaimer: reads as a white and grey rack unit with a teal top edge, a red cross label card, a handle, a dark vent strip and a green hose on the side. Better than a cube; it reads as "equipment with a label", not as an ISS rack. The ISS rack differs by clutter (cables, many labels, panels at different depths, dark open panel). The red cross is visible at 2 m, small at 4 m.
- Pocket shimmer: none visible in stills at 2 m and 4 m. Stills cannot show motion shimmer; not checked in motion (not run).
- Bay brightness: still dim at light 13 (maker face mean luminance 32.5 of 255, wall 102).

## Light measurement (item 3)
| Option | Light on cells in front of makers (estimate, level - distance) | Measured in the 2 m shot |
|---|---|---|
| Now, level 13, 6 lamps | 9 to 10 | maker face 32.5, wall 102 |
| Level 15 (one constant) | 11 to 12 | maker face 40.1 (+23 %), wall 128.4 (+26 %) |
| One extra lamp at (16,2) | one cell 10, rest unchanged | not shot (estimate only: +1 on one cell) |
Verdict: raising the level helps; an extra lamp barely does (the bay is already evenly covered). I tried 15: 254 unit tests and 113 boot checks still pass (the asserted worst-cell number is derived, not fixed). Cost: zero per-tick work; light is static and recomputed only on block change; the radius grows 2 blocks per lamp, a few more light updates when placing a lamp (negligible). Trade-off: 15 is the maximum, so it removes headroom for "dim" states (blackout art, night) and makes every room brighter, not just the bay, against the Artist's "evenly lit, not floodlit" intent. **I reverted to 13**: changing it is the owner's and the Artist's call (it also changes the earlier measured lamp results). Screenshots of 15: `out/20261127-233906/` (`L15_mk_*`). Recommendation: 14 as a compromise, or keep 13 and brighten the gold ramp mid-tone in texture (the Artist can do it without a code change).

## Facing property for the reclaimer (item 3)
Already exists in Java: the block extends `MachineBlock`, which has `FACING` (the dome places it facing south). Only the blockstate ignores it (single `""` variant). Adding four variants with `y` 0/90/180/270 is about 12 lines of JSON, no Java. It would show nothing yet because the model has the label on both north and south ends (N = S), so rotating only swaps the label axis. To make the label face the room the Artist must make the model one-sided (label only on north, plain or service end on south). Then the variants make it work. Not built: it would touch the Artist's blockstate and give no visible change now. Estimate: Artist 1 model/texture edit (about 0.25 day) plus 12 lines JSON and a shape test pass (small).

## Decisions needed
1. Light level 13 / 14 / 15 for all hull lamps. Ill-structured (art, level design, mood). Recommend 14 or a brighter gold mid-tone.
2. None else.

## Handoffs
> **To:** Artist · **Problem type:** well-structured
> **Problem:** Maker still reads as dark lattice at 2 to 4 m; reclaimer as a labelled box.
> **Context:** `tools/screenshots/out/20261127-233726/mk_2_maker_2m_day.png`, `mk_4_reclaimer_2m_day.png`; PIA24201.jpg.
> **Needed from them:** thinner grooves (1 px, lighter than #2A1D0A) or lighter groove colour, brighter mid-tone, larger tube loops; make the reclaimer one-sided (label on north only) and then add the four facing variants in `water_reclaimer.json` (`facing=north|east|south|west`, y 0/90/180/270, like `oxygen_maker.json`).
> **Done when:** a new shot at 2 m reads gold with seams, and the label faces the room.

> **To:** Owner · **Problem type:** ill-structured
> **Problem:** light level 13 vs 14 vs 15.
> **Needed from them:** a choice after looking at `out/20261127-233726/mk_1_bay_wide_day.png` and `out/20261127-233906/L15_mk_1_bay_wide_day.png`.

## Risks
- Motion shimmer of the 2 px pockets is not checked (stills only).
- Higher light level would change the earlier lamp reports' numbers.

## Shared model updates
None.
