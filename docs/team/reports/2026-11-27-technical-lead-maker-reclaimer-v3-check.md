# Technical Lead: maker and reclaimer v3 check (2026-11-27)

## Handoffs received
Artist (`2026-11-27-artist-maker-reclaimer-v3.md`): check v3 in game, 2 m and 4 m shots, confirm reclaimer label faces the room, measure maker vs wall. Accepted, all done.

## Done (all run)
- `mod_version` 0.3.6-stage3-g. `./gradlew spotlessCheck build test --rerun --offline`: BUILD SUCCESSFUL. `runGameTestServer`: `MARS-SELFTEST RESULT PASS passed=113 failed=0`. v3 blockstate (`water_reclaimer` facing variants), maker models with `shade:false` and hitbox tests load and pass. Lamp level in code is 14 (`HullBlocks.LIGHT_LEVEL`), so this run is at 14.
- Jar `mod/build/libs/mars-0.3.6-stage3-g.jar`, sha256 `533a4140867be64730725ded210f382a12fe2536d6737c0bb032f40f415114e3`.
- Screenshots (script `tools/screenshots/habitat_makers.shots`, absolute path, no client running before): `tools/screenshots/out/20261127-234533/` `mk_1` wide, `mk_1b`, `mk_2` maker 2 m, `mk_3` maker 4 m, `mk_4` reclaimer 2 m, `mk_5` reclaimer 4 m, `mk_6` to `mk_8` night. New (PNG differs from the 233726 run). The folder also holds stale copies of older shots (the harness copies the whole screenshots folder); only `mk_*` are new. I looked at mk_1, mk_2, mk_3, mk_4.

## Reclaimer facing
No Java change needed. `DomeTemplate` places it `Face.SOUTH` at template z 1 against the north wall; the room is to the south; the model front is the north face and the south variant is y 180 (same convention as the maker). In `mk_1` and `mk_4` the red cross label faces the camera in the room. The placer and template are correct.

## Luminance (real PNG, Rec. 709 mean, 0 to 255, `mk_2` 2 m)
| | maker face | wall |
|---|---|---|
| v2, light 13 (old shot 233726) | 30.5 | 103.1 (left wall 78.8) |
| v3, light 14 (new shot) | 87.4 | 117.0 (left wall 87.5) |
Maker is about 3x brighter than v2 and meets the Artist target (at least about 83 absolute). It equals the darker wall and is 75 % of the brightest lit wall. Two effects are mixed (light 13 to 14 and v3 texture), so the texture share is not separated. Crop boxes: face (580,445)-(700,550), wall (760,100)-(1000,300), left wall (100,300)-(300,500). The crop of the face includes some grooves.

## What the images show (vs PIA24201.jpg and iss_wrs_rack_open_fincke.jpg)
- Maker: now reads as a gold box on a copper plate with an amber top and a pale tube loop. Clearly more MOXIE-like than v2 and no longer dark. Differences: grooves are one thick dark cross-and-bars pattern (a ladder or window look) rather than a 4x4 grid of separate plates; no specular shine (flat, `shade:false` looks flat but not glowing next to neighbours); tubes are grey-white stubs, not long looping hoses; no hex bolts; side X-rib face not shown. At 4 m it is a small gold block with a cross.
- Reclaimer: white and grey unit, teal top band, red cross card facing the room, dark vent strip, small hose ring on the front. Reads as a labelled medical or equipment cabinet. Still not an ISS rack: no clutter, no cable bundles, no panels at different depths, no open dark panel, no multiple labels. Plain sides and back are as designed.
- Shimmer: none in stills; motion not checked (not run).

## Decisions needed
None. Lamp level 14 is now in code (owner-recommended middle).

## Handoffs
> **To:** Artist · **Problem type:** well-structured
> **Problem:** remaining gaps to the references.
> **Context:** `tools/screenshots/out/20261127-234533/mk_2_maker_2m_day.png`, `mk_4_reclaimer_2m_day.png`.
> **Needed from them:** (optional) maker: 4x4 plate grid with thin 1 px grooves instead of a cross, a 1 px bright edge strip for a specular hint, longer tube loops; reclaimer: a second label, a cable run and an inset panel for rack clutter.
> **Done when:** a 2 m shot reads as separate gold plates and the reclaimer as a multi-label rack.

## Risks
- Motion shimmer unchecked. Luminance not separated by cause (light vs texture).

## Shared model updates
None.
