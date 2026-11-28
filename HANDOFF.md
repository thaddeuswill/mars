# HANDOFF.md: full status for the next worker (Codex/ChatGPT) — 2026-11-27

Written by Claude because its usage limit is near and the owner asked for a complete handoff. Read `AGENTS.md` first (rules, commands, read order), then this file (state and what to do next). When you finish something, update this file.

## 00. CODEX: START HERE (Claude handoff, 2026-11-27 late; owner said "commit so codex can take over")

**Branch:** `claude/elegant-brown-anu1ci`, tip `1d1866d`, working tree clean, everything pushed. All Claude background agents are stopped. Do `git pull --rebase` first. The environment `mars` (Claude's) had Gradle/NeoForge/Mojang hosts allowed; in yours check `./gradlew` works from `mod/`.

**Verified at the tip (Claude ran this):** `./gradlew spotlessCheck build runGameTestServer` from `mod/`: BUILD SUCCESSFUL, 269 unit tests, 0 failures, `MARS-SELFTEST RESULT PASS passed=124 failed=0`. Jar: `mod/build/libs/mars-0.1.0-stage1.jar` (gitignored; the owner already has the W1-era jar). Not tested: dedicated server (EULA is the owner's), jar from a real mods folder, Windows/macOS.

**Done this session (all pushed, reports in `docs/team/reports/`):** slice 8 verified; wording keys 9.2; plot option D and taller plot; journal (J key) and per-player guidance (CR-T8); slice 9 pace config and `/mars pace` (classroom 3 to 5, default 4, creative bypass off in classroom, auto checkpoint, Monitor "Classroom pace" tag); S3-1 dust storm on the power grid (`/mars storm`, schedule saved in `PlanetState` schema 6); W1 terrain (`mars:planet` chunk generator, craters, dunes with one fixed wind, flat dome pad, new blocks basaltic_sand, hematite_ore, dark_sand_ore, ground_ice; existing saves keep the flat world via a saved terrain mode); atmosphere batch AT-1 to AT-3 (storm sky look, far haze, dust motes and bursty wind streaks); W2 Tier A surface blocks (commit `1d1866d`: duricrust, vesicular basalt, spherule sand, impact breccia, gypsum vein). Design docs: `docs/design/world-and-blocks.md` (owner rules: make-it-work, every invented machine gets a real reference, vanilla crafting stays), `docs/science/` (terrain, surface materials, materials and recipes, machine references, atmosphere visuals), `docs/art/` (atmosphere plan, invented-machines look).

**In flight when stopped (no report written for W2):** the Technical Lead's W2 job was stopped after commit `1d1866d`. Unknown or unfinished: `co2_frost` (needs sublimation code; deferred or not built, check), whether Tier A blocks are wired into worldgen and listed in `docs/art/w1-placeholder-textures.md`, W2 terrain polish (buttes away from the dome pad, varied dune wavelength, sand ripples and pebbles) and the stylised canyon (not built). Read the last two commit diffs and `docs/team/reports/2026-11-27-technical-lead-w1-terrain.md`, write `docs/team/reports/2026-11-27-technical-lead-w2-blocks-atmosphere.md`.

**Update 2026-11-28 (Technical Lead, W2 finished):** Tier A blocks audited and wired; butte keep-out near the pad, varied dune wavelength, sand ripples (duricrust stripes), gravel pebble patches and a stylised canyon (60 deep, about 780 blocks south of the pad) built; co2_frost deferred (needs the polar biome). `./gradlew spotlessCheck build runGameTestServer`: 273 unit tests, RESULT PASS passed=126. Note: at 1d1866d two scratch-ground boot checks failed on this machine (fixed in the test). Report `docs/team/reports/2026-11-27-technical-lead-w2-blocks-atmosphere.md`; screenshots `tools/screenshots/out/20261128-w2/`. Open: owner look at the canyon and ripples.

**Update 2026-11-28 (Technical Lead, conductor ores):** `sulfide_vein`, `magnetic_ore`, `carbonate_rock` (+ item `carbonate`), Lander Supply Chest in the once-per-world grant, `ripple_sand` for the dune ripple stripe (`planet_1.json`); all amounts in worldgen data, labels as item tooltips. `./gradlew build runGameTestServer`: 283 unit tests, RESULT PASS passed=130. Report `docs/team/reports/2026-11-28-technical-lead-conductor-ores.md`, ARCHITECTURE 2.23a. Not built: `meteorite_iron`, co2_frost, a map-half filter for magnetic ore; seed kit and other recipes that use `#mars:seed_kit_carbonate` do not exist yet.

**Update 2026-11-28 (Lead Artist, ores and processor):** real art for `sulfide_vein`, `magnetic_ore`, `carbonate_rock` and item `carbonate`; duricrust cooled and lowered in contrast (no longer ochre); atmospheric processor and super-greenhouse emitter models and textures written, NOT registered (lists in `docs/art/atmospheric-processor.md` and `docs/art/super-greenhouse-emitter.md`). 285 unit tests, RESULT PASS passed=130. Seen in a real client: `tools/screenshots/out/20261128-011135/ores_01_wall.png`. Report `docs/team/reports/2026-11-28-artist-ores-processor.md`.

**Queue, in order:** (1) ~~finish and report W2~~ done (see update above); (2) Tier B blocks only with the W2 biomes; (3) Lead Artist: replace placeholder textures, "made-up band" for invented machines, first machine batch (confirm which machines gate progress: Game Designer `docs/design/machine-priorities.md` was NOT written, the job was stopped); (4) forecasts F1 (S3-2) and brush for panel dust; (5) pause test with Mekanism/Create moved to Stage 3 (owner accepted); (6) sky tint for the storm and sunset band stay deferred unless the owner reverses.

**Owner answers recorded (do not re-ask):** W1 look accepted ("needs more atmosphere and block types"); canyon in W2 stylised (about 1000 long, 60 to 100 deep); new terrain for new worlds only; no "roar" in Phase 1 storm sound, rumble plus faint hiss; wind streaks rare and bursty; breath fog deferred; clouds vanilla; sunset band deferred; fictional emitter, mirror uplink and soil treatment unit kept with "Game invention, not real" label; vanilla crafting and progression stay (diamonds etc. kept). **Still the owner's:** EULA; P1 playtest (about 1 hour, `docs/tech/launch-guide.md`); three external reviewers; two storm kinds and the story-order fix (applied, proposed); journal text must quote only the three verified power figures.

**Unverified items to carry:** perchlorate washing, lava-tube caves, carbonate abundance, Phobos and Deimos angular sizes, body-pressure wording ("fluids push back", partly verified), general storm-length rule; SME marks are in `docs/science/*`. Sky storm tint values are INVENTED.

## 0. READ FIRST: state at Claude's final handoff (2026-11-27, owner switching to Codex)

The owner is switching to Codex because Claude's usage is nearly out. Claude's background agents may stop with Claude. Verify with `git log`, `git status`, and the newest files in `docs/team/reports/`.

**Update 2026-11-27 (Claude coordinator):** items 2 (sun texture), 3 (plot option D) and 3b (wording keys) below are DONE (commits dbf07a3..6b29797, 1de7712); journal and guidance minimum built (ba3154a, 709deef, dadfc0f); 199 unit tests, 87 boot checks. Still open: CR-T8 for forecasts that do not exist yet, rescale line, later gate files. Stage 3 first slice proposal: `docs/tech/stage3-plan.md`.

**Update 2026-11-27 (later, Claude coordinator):** slice 9 done (pace.json, `/mars pace`, classroom 3 to 5 default 4, bypass switch, auto checkpoint, Monitor 'Classroom pace' tag); 207 unit tests, 98 boot checks. Storm length settled (balance-sheet storm table); storm power figures verified by the SME. Next: owner plays P1, then Stage 3 slice S3-1 (`docs/tech/stage3-plan.md`). Owner answers still open: two storm kinds, EULA, external reviewers, real mods-folder and Windows/macOS jar test.

**Update 2026-11-27 (Technical Lead, S3-1):** dust storm built (seeded saved schedule, schema 6, solar cut, dust at storm end, Monitor storm row, `/mars storm`); 233 unit tests, 106 boot checks RESULT PASS; report `docs/team/reports/2026-11-27-technical-lead-s3-1-storm.md`. Not built: sky dim, HUD icon, forecast F1 (S3-2). Owner playtest of S3-1 is next.

**Update 2026-11-27 (Technical Lead, W1 terrain):** the owner required real-Mars terrain. Built: `mars:planet` chunk generator (vanilla noise generator plus a landform pass: craters, dunes with one fixed wind direction from `pack/data/mars/planets/planet_1.json`, flat 64-block dome pad), datapack noise settings and surface rule (red dust over dark basaltic sand patches, terracotta bands on buttes), three biomes, small caves, four new blocks with placeholder textures, ore, ground-ice and boulder features. **New worlds only:** the datapack dimension file overrides `level.dat`, so each world saves its terrain mode (`data/mars_terrain.dat`); a world that already has Mars data keeps the old flat slab (verified with a real client on a pre-W1 save, 1.2 km out in never-generated chunks). See `docs/team/reports/2026-11-27-technical-lead-w1-terrain.md` and ARCHITECTURE 2.23. Tests: see the report. Screenshots `tools/screenshots/out/20261127-205929/w1_*.png`. Not built: canyon, polar cap, ripples, flood channels. Open for the owner: whether this reads as Mars (look at the screenshots), the dust-versus-patch balance, placeholder textures to the Artist.

**Slice 8 verified (Claude, 2026-11-27):** 171 unit tests, 81 boot checks, RESULT PASS; report `docs/team/reports/2026-11-27-coordinator-slice8-verification.md`.

**Codex takeover:** The owner confirmed Claude is shut down. Slice 8 admin and checkpoint code is present on the shared branch at `a6cab96`, and the named slice 8 report is absent. Treat slice 8 as unverified until the build and GameTests run. The [Stage 2 launch guide](docs/tech/launch-guide.md) is current through slice 7. A fresh jar is pending a build-capable checkout.

**Done and pushed:** Stage 2 slices 1 to 7 (planet values, Outgasser, Monitor with history plot and next-goal bar, session clock and pause, power grid, gates, polar-frost sink and Ovens, sky/fog keyed on pressure). 167 unit tests, 70 boot checks at slice 7.

**In flight when Claude stopped (check each, finish or restart):**
1. **Slice 8, admin commands and rollback (Technical Lead agent, started this hour).** Uncommitted WIP in the working tree: `mod/src/main/java/mars/admin/AdminCommands.java` (new), `mars/index/sim/CheckpointList.java` (new), edits to `MarsMod.java`, `planets/PlanetData.java`, `planets/SessionControl.java`. If a report `docs/team/reports/2026-11-27-technical-lead-slice8-admin-rollback.md` exists, slice 8 is done: verify with `./gradlew spotlessCheck build test --rerun --offline` and `runGameTestServer`. If not, finish it: plan row 8 in `docs/tech/stage2-plan.md` (full debug command set; choose rollback approach, start-script world backup by default, or an in-game checkpoint snapshot command; document what is not covered: placed blocks, inventories, third-party machines). No dedicated-server run (EULA is the owner's).
2. **Queued for the Technical Lead after slice 8** (BACKLOG.md, "Queued"): switch `MarsSkyEffects` SUN to `mars:textures/environment/sun.png` (the Artist's placeholder is already in `pack/assets/mars/textures/environment/sun.png`), a check that the texture exists and is 32x32, night and dusk frames and the dashed-horizon-line question in the next screenshot run, and send the 0.6 and 75 kPa shots to the Artist.
3. **Monitor plot scale option D (owner ADOPTED):** phase-stepped range with a goal line from sol 0 (axis top 1.2 x next goal: 6, 36, 90 kPa; zero baseline; goal line from the gate JSON; rescale note). Spec: `docs/design/ui-specs.md` section 9.1 and `docs/team/reports/2026-11-27-game-designer-plot-scale.md`. Build `PlotScale.topFor`, a `goalLine` in `MonitorView.History`, screen drawing, tests (plotted goal equals gate value in both pace presets). Wording is being checked by the Instructional Designer agent (report `docs/team/reports/2026-11-27-instructional-designer-monitor-wording.md`, may or may not exist); use its final wording if present.
3b. **Wording is final (Instructional Designer, commit a6cab96):** `docs/team/reports/2026-11-27-instructional-designer-monitor-wording.md`, `docs/design/ui-specs.md` section 9.2, new content rules CR-T7 and CR-T8. Technical Lead to apply: new `monitor.plot.goal.partial` ('Pressure goal: %s') for Phase 3 and Breathable, `monitor.plot.rescaled` = 'The graph now goes up to %s. The line looks lower, but the pressure did not drop.', 'about %s sols' / 'about 1 sol' / 'under 1 sol' instead of '~' and '<', `grant.starting` and `monitor.estimate.levelling_off` rewrites (US spelling 'Leveling off', key ids unchanged), a `reveals` and `guidance` rule on the estimate line (CR-T8), wire the Dome stable journal entry to the `mars:dome_stable` latch. Game Designer still owes: the rose-sky label trigger, Landing step 6 text in `docs/design/messages/phase-1.md`, and the Next goal row showing an unmet temperature condition. SME to confirm the 'On Earth' replacement sentence.
4. **After Stage 2 is complete:** the owner chose Stage 3 as the next focus (ROADMAP). Panel dust remainder (storms, wear, dust-off) is Stage 3. Gate creative bypass stays; a classroom switch is a Stage 3 item.

**Owner decisions already made (do not re-ask):** panels meant the Monitor screen; run all UI skills (done); EULA not yet; clouds stay vanilla until E-10, aureole and sunset band deferred; pause flag stays in world save data; sky keyframes stay in `pack/assets/mars/keyframes/`; slice 5 calls accepted (Oven draw 1.0 invented, two starting Ovens in inventory, equal power share, stall below 5 kPa intended, hold rule, Landing step 6 "battery and Ovens"); Monitor busy-state trade-off accepted; percent on the detail line; plot option D adopted; creative gate bypass kept; panel dust in Stage 3; slice 8 then Stage 3.

**Still open for the owner (not blockers):** EULA for the dedicated-server self-test; three external reviewers; testing the jar from a real `mods` folder and on Windows/macOS; the rose-sky (5 kPa) journal label and the Dome stable "On Earth" trigger (Instructional Designer / Game Designer).

**Next owner-facing step:** a clean jar plus refreshed `docs/tech/launch-guide.md` so the owner can playtest. Build with `./gradlew build` in `mod/`; jar is `mod/build/libs/` (gitignored). Check the launch guide still describes the current commands.

**Reply format to the owner:** short: what changed, recommended next steps, a screenshot for anything visual. Ask decisions as a few clear questions with a recommended option.


## 1. What this project is, in five lines

Mars terraforming Minecraft modpack (NeoForge 21.1.267, MC 1.21.1, Java 21) built as a learning-game design exercise for ages 12 to 18, NGSS-aligned. Custom Mars dimension, global progress per planet, groups of 3 to 5, classroom mode about 11 to 12 h, standard about 40 h, equilibrium pressure 75 kPa, gates lock in. The owner (not a developer) playtests personally and answers by "accept / change / add / continue". Branch: `claude/awesome-sagan-tb7mon`. Only the owner accepts the Minecraft EULA.

## 2. State right now

Done and pushed:
- **Stage 1:** mod skeleton, empty Mars world, `/mars go`, `/mars back`.
- **Stage 2 slice 1:** planet values saved per dimension; Regolith Outgasser (+0.10 kPa/sol from `pack/data/mars/machines/regolith_outgasser.json`); Planetary Monitor screen; `/mars index get|advance|set`, `/mars monitor`. Last run: 61 unit tests, 35 of 35 boot checks.
- **Round solar array:** 3 x 3 footprint (centre block + 8 invisible canopy cells `SolarPanelCanopyBlock`), loot tables for all machines, six dust-stage models (stage 0 to 4 plus partly cleaned).
- **Raised array models:** disc top at 16 px on a three-leg deck mount (generator `tools/art-preview/gen_round_panel.py`, `DECK_Y = 15`). SME source for ~1 m deck height: `docs/team/reports/2026-11-27-sme-deck-height.md` (NASA JPL InSight press kit, not directly verified; one Rice/WUSTL page conflicts at about 0.5 m).
- **Monitor screen shrink:** `mod/src/main/java/mars/client/MonitorScreen.java`, 232 px wide, no world dimming, warm palette, rates right-aligned. Verified in a real-client screenshot: `tools/screenshots/out/20261127-110408/`.
- `AGENTS.md` and this file.

Also done 2026-11-27 (commit 5d7dbea, report `docs/team/reports/2026-11-27-technical-lead-slice2.md`):
- **Raised-array shape fix:** canopy slice y 15..16 px, centre shape [0,0,0]->[1,1,1], `footprint.*` tests and ARCHITECTURE.md 2.18 updated; seen in real-client screenshots (`tools/screenshots/out/20261127-110427/`, `20261127-110853/`). Collision is proven by GameTest bounds only.
- **Stage 2 slice 2:** `SessionClock`, `SessionData` (pause flag in world save data, not session.json), `SessionControl`, `/mars pause|resume|session`, invariant I-3 tested (100,000 paused ticks change nothing), Outgasser keeps contributing with its chunk unloaded (GameTest `outgasser.chunk_unloaded`). 67 unit tests, 41 of 41 boot checks, all run in the sandbox.
- **Arrival message:** `/mars go` now says “You are on Mars (planet 1).” Codex pushed the English translation in `07fba27` and checked the JSON. Client check pending. Codex and Claude are peer coordinators.

Also done 2026-11-27 (report `docs/team/reports/2026-11-27-technical-lead-slice4-gates.md`):
- **Stage 2 slice 3 (power)** and **slice 4 (gates)**: gate files in `pack/data/mars/gates/`, pure `mars.gates`, latching (I-2), thresholds never scaled (I-1), Outgasser placement locked for survival players until Dome stable (creative and `/mars gate latch dome_stable` bypass), Monitor "Next goal" and "Goals reached" rows. 93 unit tests, 52 of 52 boot checks, screenshots in `tools/screenshots/out/20261127-113824/`.

Also done 2026-11-27 (report `docs/team/reports/2026-11-27-technical-lead-monitor-ui.md`, BACKLOG #36):
- **Monitor UI review fixes:** reason on the stalled machine line ("needs power", "low power", panel "dusty, makes less"), compact top-left panel (about 131 GUI px tall at worst, was 173), one Power summary line plus battery bar, one goal line, state colours, trend shapes, "Esc to close", all colours and sizes in `mars.client.MonitorStyle`. 96 unit tests, 52 of 52 boot checks. The panel still covers the horizontal screen centre (see report).

Also done 2026-11-27 (report `docs/team/reports/2026-11-27-technical-lead-monitor-option-a.md`, BACKLOG #38):
- **Monitor option A, centred:** stored pressure history (`PressureHistory`, `PlanetState` schema 4), plot with Sol and kPa axes and event lines, 10-segment battery bar, panel about 122 to 135 GUI px, `MonitorLayout`/`PlotScale` pure and tested. 114 unit tests, 54 of 54 boot checks, screenshots in `tools/screenshots/out/20261127-123605/`. Rate-only cause line dropped; no status lights. Not verified: GUI scales 2/4, a very short window, Falling, paused.

Also done 2026-11-27 (report `docs/team/reports/2026-11-27-technical-lead-slice5-frost.md`, BACKLOG #43):
- **Stage 2 slice 5:** polar-frost sink with all three temperature branches (pure `FrostSink`, data in `pack/data/mars/sinks/polar_frost.json`, baseline per planet), Regolith Oven on the power grid (`regolith_oven.json`), two starting Ovens once per world, hold rule `reset_after_sols` as data, `PlanetState` schema 5, Monitor frost line and "Levelling off" note. 142 unit tests, 66 boot checks, screenshots in `tools/screenshots/out/20261127-125134/`. Pressure numbers moved (one Outgasser for 10 sols reads 1.51 kPa, not 1.60); the report lists every moved test.

Also done 2026-11-27 (report `docs/team/reports/2026-11-27-technical-lead-slice6-sky.md`, BACKLOG #46):
- **Stage 2 slice 6 (sky response):** the Mars sky and terrain fog follow pressure from `pack/assets/mars/keyframes/sky.json` (values from `docs/art/planetary-keyframes.md`). Pure `mars.visual` (interpolation, OKLab, smoothing, state), server sends only pressure (`mars:sky`), client `mars.client.sky` (dimension effects `mars:mars` draws the sky; fog events). 156 unit tests, 69 boot checks, screenshots `tools/screenshots/out/20261127-130736/` with measured pixel colours matching the keyframes within 2 per channel. Not verified: shaders or other renderers, a real multiplayer client join, GUI scales, the 30 s smoothing in a real client (the shot run uses 2 s).

Also done 2026-11-27 (report `docs/team/reports/2026-11-27-technical-lead-slice7-gate-bar.md`, BACKLOG #47):
- **Stage 2 slice 7 (next-gate bar):** goal row gets a full-width progress bar (pressure on a log scale from 0.6 kPa; hold gates by held sols), a percent, a "~N sols" estimate and the goals count on the detail line. 167 unit tests, 70 boot checks, screenshots `tools/screenshots/out/20261127-132753/`. The bar costs 4 GUI px in every state; in the busiest states the detail line (percent, estimate, count) is dropped and only the bar shows. Not built: "Useful now" list, one bar per condition. Slice 3 and 4 screenshots re-shot after the frost sink (`20261127-132842/`, `20261127-132921/`).

## 3. Known gaps and honest caveats (do not claim these work)

- **Not tested:** the jar loaded from a real `mods` folder; Windows and macOS; the dedicated server and a real save to disk and restart (needs the EULA, the owner's call); right-click on the Monitor block in the real client (screenshots use `/mars monitor`); explosions or pistons moving an Outgasser; frustum culling of an array whose overhang crosses a chunk border while its centre is off-screen.
- **Not seen in Minecraft:** the raised model (only in Blender renders) until the screenshot run after the shape fix.
- Dome stable uses a stand-in life-support state (power covers use), so it arrives after about 1 h and pressure is not on the balance-sheet curve; no recipes exist, so the unlock is a placement check only. Power numbers are invented (balance sheet 2b); no night or storm on the grid yet; dust stage never changes by itself yet (only by placement or `/setblock`).
- `/reload` mid-game leaves running planets on old definitions (fine for the prototype).
- Chat text shows faintly through the left edge of the Monitor panel (cosmetic; Stage 3 art).
- Estimates in the docs (about 26 developer-days for Stage 2 code) are unverified.
- The AI-design-audit skill in Claude's environment is written for web pages; it was applied by hand to the Monitor only.

- **Slice 5 not tested:** the grant through a real dedicated server and a real second player (GameTest uses fake players and calls `GrantEvents.maybeGrant`; the real `/mars go` path was seen once in a screenshot); no recipe for a lost Oven; the Levelling off state makes the Monitor about 144 GUI px (target 130), see the report; temperature never changes by itself, so only the cold branch is reachable in play (the other two via `/mars index set temperature`).

## 4. Owner questions waiting for an answer (do not decide these yourself)

1. Was "the panels are too big" about the **Monitor screen** (what I changed) or the **solar panels** (today about 2.1 blocks across, close to real InSight)? BACKLOG #30.
2. Which "anti UI skills" were meant (BACKLOG #31)?
3. **EULA** for the dedicated-server self-test (`runSelftestServer`). Only the owner can accept.
4. Allow network host `www.jpl.nasa.gov` so the SME can verify the deck height against NASA directly (optional).
5. Battery wall-pack and the "about 3x MOXIE" journal line were treated as confirmed unless the owner objects.
6. Owner-only recruiting: three external reviewers (a planetary scientist, a climate modeler, science teachers).

## 5. What to do next, in order

1. Sync and verify (see section 2). Make the build and tests green and the boot test print `RESULT PASS`.
2. Check `pgrep -f runShotsClient` before any screenshot run; never overlap client runs.
3. Small fixes remaining: pause does not yet block player actions or show a HUD line.
4. Then, per `docs/tech/stage2-plan.md`: slice 7 onward, the Monitor minimum view and the forecast `resolve_source` JSON plus the CR-F10 test.
5. After each step: update `BACKLOG.md` status, add a report in `docs/team/reports/`, update the "state" in `AGENTS.md` and this file, commit, push.
6. Reply to the owner in the standing format: **what changed, recommended next steps, screenshot if visual.** Keep it short; the owner is also credit-limited.

## 6. Pitfalls we already hit (save yourself the time)

- `run-shots.sh` with a **relative** script path fails inside `mod/` and silently copies old screenshots. Use an absolute path and check PNG timestamps.
- A boot test with no `RESULT` line is a failure even when Gradle says BUILD SUCCESSFUL.
- Register client screens with a lambda, never a method reference (it loads `Screen` on a dedicated server and crashes it).
- Freezing the whole test server hangs the GameTest runner; drive the tick hook by hand. GameTest batches run in parallel, so tests sharing one planet each need their own batch.
- Creative players fall before a shot: `view` now enables flying; check the camera position before drawing conclusions from a screenshot.
- Compute test expectations from the starting state, not from fixed numbers.
- When another worker regenerates assets, test against a scratch copy of `pack/` (`-PmarsPackDir=...`, or `MARS_PACK_DIR` for screenshots), then rerun on the real files.
- The sandbox blocks many hosts (NASA, Mojang, NeoForge, etc.). Builds work from the Gradle cache. Network allowlist changes need the owner's approval in Claude's environment.
- Never write `eula=true`; never run unvetted third-party downloads.

## 7. Where the history is

Backlog references #36–#38 here and in the Technical Lead reports refer to Claude’s Monitor tasks. The formerly duplicated Codex rows moved to #39 (roadmap), #40 (Monitor review), and #41 (process alignment).

`TEAM.md` (Decision Log, lessons), `BACKLOG.md` (every owner request in their words), `docs/team/reports/` (one report per task), `git log`. The full Claude conversation transcript is not in the repo.
