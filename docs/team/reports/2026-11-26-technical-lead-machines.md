# Report: Technical Lead, batch 2 machines in the mod (BACKLOG #26)

- **Role:** Technical Lead
- **Date:** 2026-11-26
- **Outputs:** `mod/src/main/java/mars/machines/` (`MachineBlock`, `SolarPanelBlock`, `MarsMachines`), `mod/src/main/java/mars/hazards/dust/PanelDustTuning.java`, `mod/src/test/java/mars/` (`PanelDustTuningTest`, `MachineAssetsTest`), `pack/data/mars/hazards/panel_dust.json`, blockstates and item models in `pack/assets/mars/`, `ARCHITECTURE.md` §2.17, `docs/tech/launch-guide.md` §1a, `tools/screenshots/batch2.shots`, screenshots in `tools/screenshots/out/20261126-185121/` (8 PNGs)
- **Everything below was run in this Linux sandbox.**

## Result
- Six machines are in the mod and show up in the real game: placed, fronts toward the camera, 3D icons in the hotbar, no missing texture. The solar panel renders its dust stages (0, 2, 4, and partly cleaned all distinct). The machines do nothing yet (behaviour is Stage 2).
- **Numbers:** `./gradlew clean build` successful; **45 unit tests pass, 0 fail**; headless boot test **10 of 10 checks**; the screenshot run saved 8 PNGs (4 runs, each rc=0 in about 66–69 s; the FPS counter read 34 and 42 in the runs that printed it).
- **Dust data:** `data/mars/hazards/panel_dust.json` (stage interval 3 sols, divided by storm cap and rate multiplier, with the note "about 15 times faster than real"; output 100/92/82/70/60%; partly cleaned recovers half the lost output; storm end +1 stage, +2 at cap ≥ 0.8; dust devil 1% per sol per panel group; rain to stage 1; ×2 within 8 blocks; storm sunlight cut 0.6; every block tagged `invented`). A test reads balance sheet §2a and compares every number. **Proved it can fail:** with a copy set to stage 4 = 55% and dust devil = 5%, 3 of 6 dust tests failed for the right reasons.
- **Jar** (`mod/build/libs/mars-0.1.0-stage1.jar`, sha256 `57e085ca…571b`): no `solar_panel_dusty` or old `models/block/solar_panel.json`; contains the new blockstates and the dust data; no dev screenshot helper.
- **Boot test:** `machines.registered` ("all 6 present"), `machines.panel_state`, and `machines.panel_placed` pass. The first attempt failed `machines.panel_state` because the check compared the wrong string format; the check was fixed, not the block.

## Design decisions
Machine collision shapes are fixed centred boxes in code, independent of models and facing; panel dust is the block state `dust_stage` (0–4) plus `partly_cleaned`; the oxygen maker's id (`oxygen_maker`) is decoupled from its art file name (`o2_generator`). Display names used: "Regolith Outgasser", "Regolith Oven", "Solar Panel", "Battery", "Oxygen Maker (CO2)", "Planetary Monitor", tab "Mars Terraforming".

## What the in-game screenshots show (the Technical Lead's reading)
All six machines render with textures and fronts toward the camera. The magenta patches on every block are the art's deliberate 2×2 placeholder marker (every texture except `solar_panel_post.png` has exactly 4 `#FF00FF` pixels), not missing textures; the marker isn't always top-left as style guide §5.5 says and reads as large magenta corners at play distance. The oxygen maker's gold grid and white tube loops read clearly but look like a waffle at about 4 blocks. Stage 4 shows bold near-black seams on a tan veil (a lattice, not an even veil); partly cleaned is distinguishable up close but almost identical to stage 4 in a wide shot; stages 4 and 2 differ clearly at low angle. Vanilla clouds still show in the Mars sky (custom sky comes later).

## Decisions needed (classified by coordinator)
| Decision | Recommendation | Type |
|---|---|---|
| Seven blocks or six: the task said seven but listed six ids; `solar_panel_dusty` is now a panel state, not a block | Six is correct | Well-structured |
| Which way a panel faces: toward the player who placed it (current), or a fixed direction toward the sun (the SME sets the direction) | Placer-facing for now; sun-facing is a design question | Well-structured |

## Handoffs
- **Lead Artist:** remove orphan assets no registered block uses (blockstates `solar_panel_dust0..4`, `solar_panel_partly_cleaned`, `o2_generator`; item models of the same names; the unwired `solar_panel_round_*` set); lighter 1 px stage 4 seams; make partly cleaned more distinct from stage 4 at distance; the placeholder marker's position and size; decide whether the oxygen maker's grid should look less like a waffle. When the round array is final, tell the Technical Lead its footprint so the shape can change and a multi-block rule can be added if it is wider than one block.
- **Game Designer:** confirm the display names; which way a panel should face.
- **Producer:** add the remaining panel-dust work (about 11.5–12.5 developer-days, unverified; ARCHITECTURE.md §2.17) on top of the machine framework (§2.16).

## Risks
The magenta marker reads as "broken" to a casual viewer; stage 4 and partly cleaned look alike from a distance (a gap in readability and learning feedback); collision boxes don't match some models exactly (the oxygen maker model spans the full block width, so players may clip slightly). **Still not tested:** the jar from a `mods` folder, the dedicated server (EULA), Windows and macOS.

## Shared model updates
- **Decision:** the shape and data decisions above.
- **Lesson:** a content test that reads the design document itself (balance sheet §2a) keeps data and design in step without anyone remembering to compare them.
