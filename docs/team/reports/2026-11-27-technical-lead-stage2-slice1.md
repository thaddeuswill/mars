# Report: Technical Lead, Stage 2 plan and slice 1 (planet values, Outgasser, Planetary Monitor)

- **Role:** Technical Lead · **Date:** 2026-11-27 · **Task:** owner said "continue" after the round array; Stage 2 is next. Plan first, then one verifiable vertical slice.
- **Everything below was run in this Linux sandbox.** Jar: `mod/build/libs/mars-0.1.0-stage1.jar` (185 KB; version string unchanged; gitignored).

## 0. Handoffs received
None from other roles. The coordinator's instruction set the scope (plan file first, then pressure first, saved world value, Monitor, Outgasser; report only what ran; no EULA, no unvetted downloads). Accepted as given. Nothing third-party was downloaded; all builds used the existing Gradle cache.

## 1. Done

**What changed for the player (in the owner's terms):** a placed Regolith Outgasser now raises the planet's pressure by 0.10 kPa per sol, the planet's values are saved with the world, and the Planetary Monitor block opens a screen that shows pressure, temperature, and the water boiling point, with a Rising/Steady/Falling word, the rate, and a cause line ("Regolith Outgasser ×2 +0.20 kPa/sol"). Operators get `/mars index get`, `/mars index advance <sols>`, `/mars index set ...` and `/mars monitor` to look and to fast-forward.

**Screenshots (real client, Mars level `mars:planet_1`, 1280×720, I looked at all three):** `tools/screenshots/out/20261127-053445/`
- `slice1_monitor_start.png`: 0.60 kPa, −60 °C, "No liquid water at this pressure", Steady 0.00, no cause line.
- `slice1_monitor_two_outgassers_10_sols.png`: 2.60 kPa, Rising +0.20 kPa/sol, "Regolith Outgasser ×2 +0.20 kPa/sol", water boils at 22 °C.
- `slice1_monitor_40_sols.png`: 8.60 kPa, water boils at 43 °C (past 5 kPa nothing happens yet: no gates).
- A screenshot shows layout and readability only; the numbers are proven by the tests. Look issues found and fixed from the first run: the long water phrase ran off the panel (now shrinks to fit), and chat text showed through the panel (backing now 99% opaque). Straw-man styling: default font, no pixel fonts, no icons, trend as a word (no arrow glyph risk). Stale text from Stage 1 in the chat ("The planet is empty for now; this is the Stage 1 build") is not mine to fix; see Handoffs.

**Files**
- Plan: `docs/tech/stage2-plan.md` (committed before any code: slices 1–9, tests, risk gate, screenshot plan, estimates).
- Pure core (`mod/src/main/java/mars/`): `index/sim/PlanetSim.java`, `PlanetState.java`, `MachineDef.java`; additions to `PlanetaryIndexModel.java` (causes, net rate, set, restore) and `IndexData.java` (load from parsed files); `monitor/view/MonitorView.java`, `Fmt.java`; `core/Planets.java`.
- Minecraft layer: `index/IndexDefinitions.java` (reload listener), `planets/PlanetData.java` (SavedData), `PlanetManager.java`, `PlanetEvents.java` (tick hook, freeze guard), `machines/RegolithOutgasserBlock.java`, `PlanetaryMonitorBlock.java`, `monitor/MonitorPayload.java`, `MonitorSender.java`, `MonitorNetwork.java`, `client/MonitorScreen.java`, `ClientPayloadHandlers.java`, `admin/IndexCommands.java`; wiring in `MarsMod.java`, `MarsMachines.java`.
- Data and text: `pack/data/mars/machines/regolith_outgasser.json`, Monitor lang keys in `pack/assets/mars/lang/en_us.json`.
- Tests: `mod/src/test/java/mars/index/sim/PlanetSimTest.java`, `monitor/view/MonitorViewTest.java`, `FmtTest.java`, `core/PlanetsTest.java`, `TestSims.java`; ArchUnit rule extended to `mars.monitor.view..`; GameTests in `selftest/PlanetGameTests.java`; `tools/screenshots/slice1.shots`.
- Docs: `ARCHITECTURE.md` §2.19 (new), `mod/README.md`, plan "changes while building" note.

## 2. What actually ran (results)

| Check | Command | Result |
|---|---|---|
| Build, Spotless, compile | `./gradlew spotlessCheck build test --rerun` | BUILD SUCCESSFUL, 61 unit tests passed, 0 failed (15 of them new: 9 PlanetSim, 4 MonitorView, 1 Fmt, 1 Planets; I did not record the exact count before my changes) |
| Unit evidence printed | same | Outgasser rate "data 0.1, balance sheet 0.1"; 250 ticks = 2 steps, pressure 0.600833 expected and read; 1 Outgasser 10 sols = 1.6000; classroom ×4 group 4 = 0.10×4/2.05 per sol; save/restore equal; schema 2 save "refusing to load it"; unknown id reported; Monitor water row at 0.60 kPa reads the no-liquid key, never "NaN"; 2.60 kPa → 22 °C |
| Headless boot + GameTests | `./gradlew runGameTestServer`, three consecutive runs after the last code change | `RESULT PASS passed=35 failed=0` each time, exit 0 (35 checks = 19 from Stage 1 and 16 new) |
| New GameTest evidence | printed | planet data loaded by the real reload path (pressure start 0.6, outgasser rate 0.1 read from the jar's files); two real Outgasser blocks placed raise pressure "by 0.0016666666666664831 kPa over 200 sim ticks" against 0.0016666666666666668 expected; breaking one halves the rate; cleanup returns count to 0; NBT save and load equal, schema 99 refused; Monitor row and payload round trip (213 bytes, equal, nothing left over); the Monitor block use returns CONSUME without throwing; freeze guard (below) |
| Real client screenshots | `tools/screenshots/run-shots.sh slice1.shots` (Xvfb, software GL) | rc=0, 38 s; log shows `mars:regolith_outgasser placed at 2, 64, 4 in mars:planet_1: 1 on this planet`, so the real Mars level path (not the test seam) worked end to end |
| Prove the checks can fail | broken data copy via `-PmarsPackDir` | Rate 0.50 in the data file: unit tests FAIL (balance-sheet check, 10-sol test, Monitor test, others), GameTest `FAIL check=planet.outgasser_rate expected="0.10…" read="rate=0.5"`, exit 1. Missing `contributions` field: server refuses to start, `RESULT FAIL reason=exited_without_result last_step=common_setup`, exit 1 |

**Failures I hit on the way (real ones, all fixed):**
1. The server crashed on load: `Attempted to load class Screen for invalid dist DEDICATED_SERVER`. A method reference to the client handler loads the client class during payload registration. Fixed with a lambda. This would have broken every dedicated server and every class server.
2. **A hole in the boot gate:** `runGameTestServer` reported BUILD SUCCESSFUL (exit 0) while the server had failed to start, because Minecraft's server `Main` logs the error and exits 0. The Stage 1 gate only passed because a RESULT line was visible to a human. `SelfTest` now has a shutdown hook: no RESULT line means `RESULT FAIL reason=exited_without_result`, exit 6. Proved by the broken-data run above. Stage 1's "boot test passes" claim stands (its RESULT line was seen) but the gate was weaker than reported.
3. My first freeze test froze the whole test server and hung the GameTest runner until the watchdog killed it (125 s, exit 4). Rewritten to call the tick hook by hand while the tick-rate manager is frozen and unfrozen in one tick. The next version failed because `runsNormally()` is refreshed only by the manager's own tick (fixed in the test; documented in ARCHITECTURE §2.19). A third version was flaky (expected 2 steps, real count is 2 or 3 depending on where the counter starts); it is now computed from the counter, and both alignments (200 and 300 ticks) appeared across the three final runs.
4. My Monitor-use expectation was wrong (server returns CONSUME, client SUCCESS); the test was corrected, not loosened elsewhere.

## 3. Risk gate (plan §5, filled in)

| # | Change | Could break | Check | Result |
|---|---|---|---|---|
| R1 | Reload-listener data loading | Data loaded late, never, or silently defaulted | GameTest `planet.data_loaded`, `planet.outgasser_rate` read from the jar; broken-file run refuses to start with the file named | **Pass** |
| R2 | Saved planet values | Newer save overwritten; values or machines lost or doubled on load | Unit save/restore equality and continuation test; GameTest NBT round trip; schema 99 refused in both | **Pass for NBT round trip.** Not proven: a real world save to disk and a server restart (see Risks) |
| R3 | Contributions by position, no block entity | Phantom machine after explosion, piston, `/setblock` | `onRemove` path tested with `destroyBlock`; the real-client run used `/setblock` placement and registered correctly | **Partly passed.** Explosions and pistons not tested; they use the same `onRemove` path in vanilla (unverified here). Chunk-unload behavior is a design call (kept) |
| R4 | Tick hook cost, frozen levels | Cost on every level; sim runs while frozen | Hook returns early for non-planet levels; freeze GameTest | **Pass** (cost is by reading, not profiled) |
| R5 | Monitor payload and screen | Stale/invented value on client; dedicated-server crash; unreadable at 1280×720 | Server builds all numbers; payload round trip; the crash above was caught by the boot test and fixed; screenshots looked at | **Pass** at 1280×720. Not tested on a real dedicated server (EULA) or over a real network connection |
| R6 | Pure packages grow | Minecraft leaks into core | ArchUnit extended to `mars.monitor.view..` | **Pass** |
| R7 | Test seam | Overworld acting as planet in real play | Property appears only in `build.gradle`'s `gameTestServer` block (grep), plus a unit test of the rule; the real-client run did not set it and the overworld was not treated as a planet | **Pass** |
| R8 | Debug commands | A student breaks the planet | Permission level 2, logged (`ADMIN Dev advanced the planet by 10.0 sols` seen in the log) | **Pass by reading and log;** permission denial for a non-op not tested |
| R9 | Outgasser unpowered and ungated | Pressure speed mistaken for the balance sheet | Named here and in the plan | **Stated.** Measured: 8.60 kPa after 40 sols with two machines, which is not the balance-sheet curve (that needs power, gate, frost sink, storms) |
| R10 | Boiling point NaN below triple point | "NaN" on screen | Unit test and screenshot 1 | **Pass** |

## 4. Not proven (will not be claimed)
- The dedicated server with the Mars level (owner's EULA still pending; I did not accept it or write `eula.txt`).
- A real save to disk and restart; the NBT round trip stands in.
- Right-click on the Monitor block in the real client (the screenshots use `/mars monitor`; the block's server path is GameTested, the packet delivery to a real client is shown by the screenshots through the command path, which uses the same `MonitorSender`).
- Explosions or pistons moving an Outgasser; `/tick step` behavior; `/reload` mid-game (running planets keep old definitions until loaded again).
- Windows, macOS, weak hardware (software GL ran at the usual 6–20 FPS in Stage 1; not measured this time).
- Effort: slice 1 took roughly one working session of agent time; I cannot convert that into developer-days, so the plan's 26-day estimate stays **unverified**, and slice 1's 4-day figure has no measured rate behind it.

## 5. Definition of done (build)
Parse/compile gate met; unit tests met; boot test with watchdog met (and now fails on a silent exit); screenshot at 1280×720 met and looked at; automated playthrough not applicable yet (no gates); owner acceptance pending ("Do you accept?" below).

## 6. Decisions needed

| Decision | Recommendation | Type |
|---|---|---|
| Do you accept slice 1 (look at the three screenshots), and may I continue with slice 2 (session clock and pause)? | Accept, then slice 2 before power, because pause (I-3) is the item most costly to rework | Well-structured |
| Should an Outgasser keep contributing while its chunk is unloaded? | Yes: the planet is one simulation and the design says progress is global. Needs the Game Designer to confirm. | Ill-structured (play feel: a player who walks away from their base sees it keep working; both goals affected), routed below |
| Minecraft EULA for the dedicated-server self-test (still open from Stage 1) | Owner's call; it would close the "Mars level on a real server" and restart gaps | Well-structured |

## 7. Handoffs

> **To:** Lead Game Designer · **Problem type:** ill-structured
> **Problem:** Outgassers are registered by position and keep working when their chunk is unloaded.
> **Context:** `ARCHITECTURE.md` §2.19; `RegolithOutgasserBlock`. Alternative: only loaded machines contribute, so players must stay near their base (pressure, not grind, pillar "pressure"; but it punishes exploration and breaks classroom pacing when students wander).
> **Your view:** Technical Lead lens: simplicity and performance favor "always contributes" (no per-chunk bookkeeping); instructional value favors it too (cause and effect stays visible on the Monitor).
> **Needed from them:** confirm "always contributes", or say what upkeep should replace loaded-chunk dependence (maintenance parts, power).
> **Done when:** the answer is in DESIGN.md or the balance sheet.

> **To:** Lead Game Designer · **Problem type:** well-structured
> **Problem:** straw-man Monitor wording needs owner-approved text: "No liquid water at this pressure", "Water boils at", Rising/Steady/Falling, "Sol 10.0" (all in `pack/assets/mars/lang/en_us.json`, keys `monitor.*`). Also: Stage 1 chat line "The planet is empty for now; this is the Stage 1 build" in `TravelCommands` is stale text; tell me the replacement, or I will remove it with the next travel-command change.
> **Needed from them:** final wording at grade 6–7 level, with grade-band keys if wanted. **Done when:** keys updated.

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** the Monitor screen is a straw-man (default font, flat panel, no icons). Pixel fonts, the pressure log gauge, and the trend glyphs are Stage 3 art; the screen reads cleanly at 1280×720 now, so placeholder art can wait.
> **Needed from them:** nothing yet; the layout budget is in `ui-specs.md` §1. **Done when:** Stage 3 art list picks it up.

> **To:** Producer · **Problem type:** well-structured
> **Problem:** ROADMAP Stage 2 rows to update: "Planetary Index stored in world save data and readable by command" and "One machine that raises one parameter (pressure)" are built (slice 1; the placeholder Outgasser art is the Artist's existing model); "Simulation clock" is partly built (clock and freeze guard, no pause flag or sols counter on screen beyond "Sol n"); invariant I-3 is not yet tested. Also R-10/R-11 stay open until slice 2.
> **Done when:** checkboxes reflect this report. Note for the retro log below.

## 8. Risks
- **Silent gate failure** (found and fixed): any future harness that runs Minecraft must be checked for "exit 0 on failure"; the shutdown-hook pattern is the fix.
- **Dedicated server and real saves unproven** until the owner accepts the EULA; the highest remaining technical gap for "launchable product".
- **`/reload` mid-game** leaves running planets on old definitions; acceptable for the prototype, wrong for datapack tuning by teachers later (fix when pacing config lands, slice 9).
- **Static holder for definitions** (`IndexDefinitions.current`): standard for reload data, but it is mutable static state; guarded by being replaced whole.
- **Estimates:** unverified; first measured rate comes with slice 2.
- **Balance numbers still invented:** the Outgasser rate is the balance sheet's untested starting value; slice 1 does not test it against the curve.

## 9. Shared model updates
- **Lessons (add to Learning Log):** (a) A harness that runs another program must treat "no result line" as a failure; Minecraft's server exited 0 after failing to start and Gradle printed BUILD SUCCESSFUL. (b) A method reference to a client-only class in a common-side registration crashes dedicated servers; use a lambda and the boot test catches it. (c) Freezing the whole test server freezes the test runner; drive the guard by hand. (d) A test that expects "2 steps from 250 ticks" is flaky; the count depends on where the counter starts, so compute expectations from the starting state. (e) Tests in one GameTest batch run in parallel; tests that share one planet each get their own batch.
- **Decision (owner doc: ARCHITECTURE.md §2.19):** a planet is any dimension in the `mars` namespace; planet state is saved per dimension as `PlanetData`; machine contributions are registered by position and survive chunk unload (pending Game Designer confirmation); definitions are datapack data loaded by a reload listener; pause uses the vanilla tick-rate manager.
- **Vocabulary:** none new.
- **Retro row (PROCESS.md §7), for the coordinator:** Keep: writing the plan and risk gate first (it named the NaN, test-seam, and phantom-machine risks before code, and two of them were real); looking at every screenshot (overflow found). Change: the Stage 1 "boot passes" claim relied on a human reading the log; every gate now has to fail by itself.
