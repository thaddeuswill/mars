# HANDOFF.md: full status for the next worker (Codex/ChatGPT) — 2026-11-27

Written by Claude because its usage limit is near and the owner asked for a complete handoff. Read `AGENTS.md` first (rules, commands, read order), then this file (state and what to do next). When you finish something, update this file.

## 0a. UPDATE 2026-11-27 (Technical Lead): Stage 3 slice 0 closed, slice 1 (dome and sealing) built

Jar `0.3.0-stage3-a`. 214 unit tests, 95 boot checks (`RESULT PASS`). New: `mars.dome` (pure: `SealCheck` flood fill with cap 2,000, `DomeSeals` registry and cache, `Pos`), `mars.hull` (Hull Plate, Hull Glass, Hull Door, `Airtight` rule, `WorldProbe`, `DomeEvents`, `SealChangedEvent`), `PlanetState` schema 6 (`domes`), Monitor Dome row, `tools/screenshots/slice_dome.shots`. A Planetary Monitor placed on Mars registers a dome; `lifeSupportOk()` is **unchanged** (slice 2 replaces it). Codex's `SealChangedEvent` hook: subscribe on `NeoForge.EVENT_BUS` for the seal-breach action. Next: slice 2 (life support, recipes, priority tiers) and the starting dome template. Report `docs/team/reports/2026-11-27-technical-lead-stage3-slice1-dome.md`; design in ARCHITECTURE.md 2.3.

## 0. READ FIRST: state at Claude's final handoff (2026-11-27, owner switching to Codex)

The owner is switching to Codex because Claude's usage is nearly out. Claude's background agents may stop with Claude. Verify with `git log`, `git status`, and the newest files in `docs/team/reports/`.

**Current verification:** Codex and Claude are working in parallel. On clean commit `436a63b`, formatting, Gradle build, 172 unit tests, and 81 boot checks passed. A Stage 2 playtest jar was built. See the [slice 8 report](docs/team/reports/2026-11-27-technical-lead-slice8-admin-rollback.md) and [launch guide](docs/tech/launch-guide.md).

**Done and pushed:** Stage 2 slices 1 to 7 (planet values, Outgasser, Monitor with history plot and next-goal bar, session clock and pause, power grid, gates, polar-frost sink and Ovens, sky/fog keyed on pressure). 167 unit tests, 70 boot checks at slice 7.

**UPDATE (Claude, after the limit reset): slice 8 turned out to be finished on the remote by another worker (report `docs/team/reports/2026-11-27-technical-lead-slice8-admin-rollback.md`). I merged it with my WIP commit; spotlessCheck, build and test pass after a spotlessApply on one test file, and runGameTestServer printed RESULT PASS passed=82 failed=0. Check git log for what else landed.

**In flight when Claude stopped (check each, finish or restart):**
1. **Slice 8, admin commands and rollback:** shipped and verified. Checkpoints restore the planet simulation and retain placed machines and blocks. The normal Launcher install and dedicated-server restart remain pending.
2. **Queued for the Technical Lead after slice 8** (BACKLOG.md, "Queued"): switch `MarsSkyEffects` SUN to `mars:textures/environment/sun.png` (the Artist's placeholder is already in `pack/assets/mars/textures/environment/sun.png`), a check that the texture exists and is 32x32, night and dusk frames and the dashed-horizon-line question in the next screenshot run, and send the 0.6 and 75 kPa shots to the Artist.
3. **Monitor plot scale option D (owner ADOPTED):** phase-stepped range with a goal line from sol 0 (axis top 1.2 x next goal: 6, 36, 90 kPa; zero baseline; goal line from the gate JSON; rescale note). Spec: `docs/design/ui-specs.md` section 9.1 and `docs/team/reports/2026-11-27-game-designer-plot-scale.md`. Build `PlotScale.topFor`, a `goalLine` in `MonitorView.History`, screen drawing, tests (plotted goal equals gate value in both pace presets). Wording is being checked by the Instructional Designer agent (report `docs/team/reports/2026-11-27-instructional-designer-monitor-wording.md`, may or may not exist); use its final wording if present.
3b. **Wording is final (Instructional Designer, commit a6cab96):** `docs/team/reports/2026-11-27-instructional-designer-monitor-wording.md`, `docs/design/ui-specs.md` section 9.2, new content rules CR-T7 and CR-T8. Technical Lead to apply: new `monitor.plot.goal.partial` ('Pressure goal: %s') for Phase 3 and Breathable, `monitor.plot.rescaled` = 'The graph now goes up to %s. The line looks lower, but the pressure did not drop.', 'about %s sols' / 'about 1 sol' / 'under 1 sol' instead of '~' and '<', `grant.starting` and `monitor.estimate.levelling_off` rewrites (US spelling 'Leveling off', key ids unchanged), a `reveals` and `guidance` rule on the estimate line (CR-T8), wire the Dome stable journal entry to the `mars:dome_stable` latch. Game Designer still owes: the rose-sky label trigger, Landing step 6 text in `docs/design/messages/phase-1.md`, and the Next goal row showing an unmet temperature condition. SME to confirm the 'On Earth' replacement sentence.
3c. **English wording applied:** Monitor estimates use words, the frost note uses “Leveling off,” and the starting grant says “inventory.” CR-T8 forecast-aware visibility and the high-school read-more view remain separate.
4. **After Stage 2 is complete:** the owner chose Stage 3 as the next focus (ROADMAP). Panel dust remainder (storms, wear, dust-off) is Stage 3. Gate creative bypass stays; a classroom switch is a Stage 3 item.

**Owner decisions already made (do not re-ask):** panels meant the Monitor screen; run all UI skills (done); EULA not yet; clouds stay vanilla until E-10, aureole and sunset band deferred; pause flag stays in world save data; sky keyframes stay in `pack/assets/mars/keyframes/`; slice 5 calls accepted (Oven draw 1.0 invented, two starting Ovens in inventory, equal power share, stall below 5 kPa intended, hold rule, Landing step 6 "battery and Ovens"); Monitor busy-state trade-off accepted; percent on the detail line; plot option D adopted; creative gate bypass kept; panel dust in Stage 3; slice 8 then Stage 3.

**Still open for the owner (not blockers):** EULA for the dedicated-server self-test; three external reviewers; testing the jar from a real `mods` folder and on Windows/macOS; the rose-sky (5 kPa) journal label and the Dome stable "On Earth" trigger (Instructional Designer / Game Designer).

**Next owner-facing step:** a clean jar plus refreshed `docs/tech/launch-guide.md` so the owner can playtest. Build with `./gradlew build` in `mod/`; jar is `mod/build/libs/` (gitignored). Check the launch guide still describes the current commands.

**Reply format to the owner:** short: what changed, recommended next steps, a screenshot for anything visual. Ask decisions as a few clear questions with a recommended option.


**Stage 3 slice 3 (Codex):** typed action events and local JSON Lines writer are built under `mars/learning/`. Session rotation and in-memory identity allocation are built and tested. Gameplay hooks, cross-restart identity, CSV, guidance, and interruption settings remain open. See the [first learning report](docs/team/reports/2026-11-27-codex-learning-action-log.md).

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
