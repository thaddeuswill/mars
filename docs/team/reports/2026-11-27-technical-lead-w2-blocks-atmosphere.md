# Technical Lead report: W2 surface blocks, terrain polish, stylised canyon (BACKLOG #49)

**Date:** 2026-11-28 · **Role:** Technical Lead · **Branch:** claude/elegant-brown-anu1ci · **Continues:** the stopped W2 run (commit 1d1866d, no report).

## 0. Handoffs received
| From | Handoff | Answer |
|---|---|---|
| Coordinator | Audit 1d1866d; co2_frost if small; W2 polish; canyon only if 1 to 3 verified | Accepted. co2_frost deferred (section 2). Canyon built with one deviation (depth 60, section 3). |

## 1. Audit of 1d1866d (Tier A blocks)
| SME table place | Wired? | Evidence |
|---|---|---|
| Duricrust crust layer under the dust | Yes | `noise_settings/planet.json` surface rule, stone-depth 2 layer |
| Vesicular basalt on rocky flats | Yes | `vesicular_patch` (rocky_flats), `vesicular_patch_rare` (dust_plain), boulder entry in `boulders_rocky` |
| Spherules in sedimentary patches | Yes | `spherule_patch` on dust_plain and rocky_flats (disk on dust, dark sand, duricrust). Drops 1 to 3 iron nuggets (INVENTED yield), silk touch keeps the block |
| Breccia in crater rims | Yes | `planet_1.json` `rim_block` and `ejecta_block` = `mars:impact_breccia` |
| Gypsum veins in butte layers | Yes | `gypsum_vein` feature targets the `terracotta` tag, in `highland_mesa` |
| Loot, lang, tags, blockstates, models, textures | Yes | all five present; pickaxe, shovel and stone-tool tags set |
| Listed in `docs/art/w1-placeholder-textures.md` | **No (gap)** | Fixed: table added with the final-art notes and where each generates |
| Boot checks | **Gap** | At 1d1866d, on this machine, `runGameTestServer` gave `RESULT FAIL passed=122 failed=2` (`terrain.ore_and_ice`, `terrain.w2_surface_patches`), not the PASS in HANDOFF. I ran the stashed tip twice. Cause: the scratch ground in the test left natural terrain above y 77 and the features used the stale `_WG` heightmap, so ice sat too high and spherules did not place. Fixed in the test (clear the area, live heightmap). It is a test fix, not a worldgen change. |

## 2. co2_frost: deferred
It is polar and winter only and must sublimate. That needs a polar biome (not built) and a rule that reacts to season or temperature (a ticking or scheduled block, plus a saved-state decision). Not small. Left for the W2 polar biome. Recorded in ARCHITECTURE 2.23.

## 3. Built
- **Butte keep-out:** `Landforms.cutNatural` cuts ground above pad height + 8 inside 130 blocks, fading out by 220. Cut columns get dust over duricrust (`Kind.CUT`).
- **Dune wavelength variation:** smooth phase offset, about 25 percent spacing variation, crests bend but do not shear (the W1 failure).
- **Sand ripples:** stripe 1 block in every 3 across the wind, on dune columns only (no new block). The stripe block is `mars:duricrust`: red sand read as a zebra barcode, gravel was too bright (both seen).
- **Pebbles:** `minecraft:gravel` disk patches (radius 1 to 2). No new block.
- **Stylised canyon** (`mars.terrain.Canyon`, data in `planet_1.json` `terrain.canyon`): about 1000 long, 200 to 400 wide, flat floor, 6-block benches, terracotta wall bands, centre about 780 blocks south of the pad. **Deviation: 60 deep, not 60 to 100.** The dimension has `min_y` 0 and the ground is at y 80; 100 would hit the world floor. A deeper canyon needs a new dimension type, which breaks existing saves (see decision below). Labelled stylised in data; the journal text is the Instructional Designer's.

## 4. What I ran (real output)
| Check | Result |
|---|---|
| `./gradlew spotlessCheck build` (offline) | BUILD SUCCESSFUL, **273 unit tests, 0 failures** (baseline 269; +4: wavelength and shear, ripple share, butte keep-out, canyon size and distance) |
| `./gradlew runGameTestServer` | **MARS-SELFTEST RESULT PASS passed=126 failed=0** (baseline 124; new `terrain.butte_keepout` read "0 tall columns built (902 before the cut)", `terrain.canyon` read "lowest top y 12, highest 87, 2152 of 9728 columns more than 30 below the pad, 1814 with bands"). The `terrain.legacy_slab`, `legacy_base_height` and mode checks still pass (existing-save path). 29 "Unprimed heightmap" log errors appear before and after my change (pre-existing, bare-chunk tests). |
| Real client, new world, script `tools/screenshots/w2_terrain.shots` | seen; the committed folder is `tools/screenshots/out/20261128-w2/` (six PNGs, timestamps checked 00:37). `run-shots.sh` also copies old PNGs into each out folder (same timestamp); I cited only my own. |
| Existing-save real client run | **not re-run**. Only the boot checks (above) cover legacy_flat this time; W1 did the client run. |

## 5. What I saw (honest verdict: more Mars or still Minecraft?)
- **Dunes with ripples (w2_04, w2_05):** tan stripes on dark sand every 3 blocks read as wind ripples and make dune faces look combed. This is more Mars than before. Still block-sized: a 1 m stripe is a rib, not a ripple. The first two colour tries failed.
- **Canyon (w2_10 from the floor):** terraced, colour-banded walls look like a layered canyon wall: the best single image of the batch. Banding is regular (a ruler look). **Not seen:** a whole-canyon overview; my two overview shots hit a large crater and the 192-high cloud layer. Canyon size comes from the unit and boot checks, not from a picture.
- **Pebble patches:** pale grey flat patches on the plain (w2_02). They read as Minecraft gravel blobs, not as pebbles; the plain is still too clean at block scale.
- **Buttes near the pad:** the boot check proves none stand within 125 blocks (902 columns cut). In w2_01 a butte shows about 150 to 200 blocks out, as designed. The aerial shot does not frame the pad, so it is not proof.
- **Overall:** still reads as Minecraft up close (cube grid, 1 m stripes), Mars from a height and in the colour and layer language. The sky and fog carry much of the Mars feel.
- **Placeholder textures** (duricrust, vesicular basalt, spherules, breccia, gypsum) are not art; the verdict says nothing about the final look.

## 6. Decisions needed
| # | Question | Type | Recommendation |
|---|---|---|---|
| 1 | Canyon depth 60 is the maximum for `min_y` 0. Raise `min_y` for a deeper canyon (breaks existing saves' dimension, new worlds only)? | Ill-structured (scope vs. risk) | No. Keep 60; the owner's range is "60 to 100" and 60 is in it. |
| 2 | Accept duricrust as the ripple stripe, or want a dedicated darker or lighter "ripple sand" block (Artist)? | Well-structured | Dedicated block in the Artist's texture pass later. |

## 7. Handoffs
> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** Replace the five Tier A placeholders; check the ripple stripe (duricrust) against dark sand and the grey gravel pebbles.
> **Context:** `docs/art/w1-placeholder-textures.md`, `tools/screenshots/out/20261128-w2/`
> **Needed from them:** textures with the same file names; a view on a ripple block.
> **Done when:** real PNGs are in `pack/assets/mars/textures/block/`.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** Journal wording for the stylised canyon (about 1000 long, 60 deep, "shrunk to fit") and the ripple and duricrust lines.
> **Needed from them:** text following content rules; SME to confirm real Valles Marineris dimensions (not re-verified here).
> **Done when:** entries are in the journal data.

## 8. Risks
- Chunk gen cost with the canyon and ripples: not re-measured on a weak machine; the boot `terrain.cost` check passed on this one.
- The first run of `runGameTestServer` at the previous tip failed on this machine; other machines may differ, so the 124-PASS claim in HANDOFF may have come from a different setup.
- Canyon walls are a ruler-regular terrace; the wander is 60 blocks only.

## 9. Shared model updates
- Lesson: terrain boot checks that place features in a live test level must clear the scratch ground and use the live heightmap, not `_WG`. A "124 PASS" must be re-run on the new machine before it is trusted.
- Canyon depth is limited by the dimension floor (`min_y` 0, ground y 80): at most about 70.
