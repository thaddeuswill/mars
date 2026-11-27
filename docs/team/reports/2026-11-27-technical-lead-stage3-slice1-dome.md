# Technical Lead report: Stage 3 slice 0 close-out and slice 1 (dome and sealing), 2026-11-27

## 0. Handoffs received
Owner "accept and cont with recc": Stage 3 slices 1 to 3 approved; Codex owns slice 3 in `mars/learning/**`. Accepted. Constraints kept: slice 2 not started, `lifeSupportOk()` not replaced, nothing under `mars/learning/**` touched, no EULA step, no downloads. Game Designer's 2d numbers were not needed in slice 1 (no life-support JSON keys read).

## 1. Done
**Slice 0 (close-out).** Checked git log: sun texture (#50, #51), night and dusk frames (#49) and the Monitor and grant wording keys (#55) were already done by Codex. I did: version `mod_version=0.3.0-stage3-a` (`mod/gradle.properties`), launch guide refreshed (`docs/tech/launch-guide.md`, new section 1d), jar built `mod/build/libs/mars-0.3.0-stage3-a.jar`. Still owner-side: a real `mods` folder test. The dashed-horizon question is still open (the starting dome, a new horizon reference, is not built).

**Slice 1, built:**

| Piece | Where | Notes |
|---|---|---|
| Flood fill | `mars/dome/SealCheck.java`, `Probe.java`, `Pos.java` | Pure Java, ASCII-art testable. BFS over OPEN cells, 6 neighbours. Guards: first OUTSIDE cell, cap 2,000 open cells. Results SEALED, OPEN_TO_OUTSIDE, TOO_BIG, NO_SPACE |
| Dome registry and cache | `mars/dome/DomeSeals.java` | Seed = Planetary Monitor; dome = air volumes on its 6 faces. Exact cache (dirty only if the changed block was probed), one recheck per dome per tick at most, 5 s safety net, `Change` list for breach and repair |
| Saved state | `PlanetState` (schema 6, `domes`), `PlanetData`, `PlanetSim`, `PlanetCheckpoint` | Old saves load with no domes; rollback keeps current domes; cache not saved, rechecked on first tick |
| Blocks | `mars/hull/` Hull Plate, Hull Glass, Hull Door (+ items, loot tables, blockstates, models, straw-man 16 px textures made with a script) | In the machines creative tab; no recipes (slice 2) |
| Rule | `mars/hull/Airtight.java`, tags `pack/data/mars/tags/block/{airtight,not_airtight}.json` | Full block or nothing; closed hull door airtight; open door, slab, stair, fence, pane, trapdoor leak; machine casings airtight |
| World access, tick | `WorldProbe`, `DomeEvents`, hook in `PlanetEvents.onLevelTick` | Open sky = heightmap (not the lagging light engine); unloaded chunk = OUTSIDE (never loads one) |
| Event | `mars/hull/SealChangedEvent.java` | NeoForge bus, once per change. **For Codex's learning log**: subscribe there for the seal-breach action |
| Monitor | `MonitorView.domeRow`, `MonitorLayout.fit`, en_us keys | "Dome: Sealed / Leaking / Too big to check / Checking". Straw-man wording, flagged for the Instructional Designer. Messages `dome.breach`, `dome.repaired` (action bar) |

**Decisions I made (proposed; flag if you disagree):**
1. **Partial blocks never seal** (full block or nothing, per dome-layout section 5). Doors: closed is airtight, open leaks.
2. **Seed = Planetary Monitor.** A wall-mounted Monitor's outdoor face is ignored; a Monitor in open air reads Leaking. The Monitor must stand inside against a wall (as in the layout).
3. **Cap 2,000** kept as proposed (a 13 x 13 x 13 room is Too big; invented number).
4. **Change detection:** our blocks signal at once (reported within 1 tick; GameTest checks at +2 to +3 ticks); anything else within 5 s. Reason: Minecraft has no "any block changed" event and `/setblock` on a same-block state change fires nothing.
5. **Door:** a vanilla-style copper-set double door (`BlockSetType.COPPER`, opens by hand). One block type serves inner and outer doors. The interlock is slice 6.
6. Hull Door in the Monitor budget: the Dome row appears only when a dome is registered (12 GUI px sealed, a large problem row when leaking). In the busiest state (levelling off, short, dusty) a sealed row gives way (like the goal detail line) so the panel stays at 152 px; a leaking row stays and the panel is 165 px (fits scale 3, 228 px usable; the existing busiest state was already over the 130 px target and is unchanged).
7. `PlanetSim.toState` no longer writes empty machine lists (a test-order flake exposed this: a removed machine type came back as a missing key, so "save equals load" failed depending on batch order). Behaviour change is small and test-covered.

## 2. Verification (what I ran)
Commands run from `mod/`, offline, in the final merged tree (with Codex's learning commits merged):
- `./gradlew spotlessCheck build test --rerun --offline`: **BUILD SUCCESSFUL**, 214 tests, 0 failures (was 176 per BACKLOG #55; Codex added 7 learning tests, I added 31).
- `./gradlew runGameTestServer --offline`: **`MARS-SELFTEST RESULT PASS passed=95 failed=0`** (82 before; 13 new dome checks).
- 31 new unit tests: `SealCheckTest` (sealed room, one hole, world-edge hole, diagonal gap, cap exceeded, cap guard with an endless world, start in a wall, door closed and open, door leaking to the sky, Pos equals BlockPos packing), `DomeSealsTest` (no cost without domes, check on next tick, breach and repair, cache and unrelated edits, safety net, wall-mounted Monitor, Monitor in open air, too big, two domes, save round trip and recheck, null state), `DomeStateTest` (schema 6, schema 5 save loads, rollback keeps domes), `MonitorDomeViewTest`, `HullAssetsTest`, one layout test. ArchUnit now covers `mars.dome..` as pure.
- 13 new boot checks (`DomeGameTests`, three batches), with real blocks: sealed 5x4x5 room reads SEALED with 99 air blocks and the Monitor row says Sealed; breaking a plate reads LEAKING 2 ticks later; hull glass reseals; a slab leaks; a closed Hull Door seals; a fake player's right-click opens it and the room leaks within 3 ticks; closing reseals; breaking the Monitor unregisters; a Monitor in open air leaks; Pos matches BlockPos; NBT round trip with schema 6; a schema 5 NBT loads with no domes.
- **Guard removed (proof it can fail):** I temporarily changed `if (volume > cap)` to `if (false)` in `SealCheck`; `./gradlew test --tests 'mars.dome.*'` printed "21 tests completed, 3 failed" (the cap tests); restored and re-run green.
- **Screenshot run** (`pgrep -f runShotsClient` empty first; absolute path): `tools/screenshots/slice_dome.shots`, output `tools/screenshots/out/20261127-192420/`. I looked at the PNGs: `dome_1` (room with glass and door from outside), `dome_2` (a hole in the wall, glass visible), `dome_3` (Monitor "Dome: Leaking" in warm colour), `dome_4` (after patching, "Dome: Sealed" in green). The check showed the client's own server reporting SEALED -> LEAKING -> SEALED in `client.log`.
- **Elapsed and runs:** about 12 minutes of agent wall time from the first build to the final green run (718 s by clock), about 24 Gradle invocations (not counted exactly), 5 screenshot runs (about 44 s each; four were reruns for the script, see below), 1 mutation run.

## 3. Not verified or not done
- **Not done (in the plan's slice 1 text, not in my brief):** the Mars spawn site and terrain, the 23 x 16 starting dome structure from `dome-layout.md` (about 728 hull blocks as one template), breach air vents, a synced "sealed" flag to the client for the O2 HUD (needs the per-player "inside a sealed volume" test from slice 2). The sealing rule is proven on small rooms, not on the full layout. Building the template is mostly data (NBT or a build command) and about one more short session; I recommend doing it at the start of slice 2 so life support has a real room.
- **Not verified:** a Hull Door opened by redstone (code covers `neighborChanged`; not run); multiplayer with two clients; a dedicated server and a restart (needs the EULA); the 5 s safety net in a real client for `/setblock` door changes (I tried a door-open screenshot and `/setblock` answered "Could not set the block" for a same-block state change; I dropped that shot. The right-click path is covered by the GameTest instead); chunk borders (a dome across an unloaded chunk reads OUTSIDE, so Leaking); the cost in a 2,000-block dome in a real world (computed, not measured); render of the door when open (not shot); GUI scales 2 and 4 for the new line.
- Textures are straw-man (script-made, 16 px, Artist to redo). The hull glass uses a translucent render type; I saw it in the screenshot.
- Hull blocks have no pickaxe tag, so they break slowly by hand and drop themselves; fine for a prototype, to be decided with recipes.

## 4. Decisions needed (owner)
1. **Start slice 2 with the starting dome template?** Recommend yes (well-structured). Then life support has the real shape from `dome-layout.md`, and the Dome stable timing test runs on it.
2. **Too big to check** as a third Monitor word (instead of folding into Leaking). Recommend keep: the cause differs (no hole to find). Ill-structured only in wording; Instructional Designer to word it.

## 5. Handoffs
> **To:** Codex (learning lane) · **Problem type:** well-structured
> **Problem:** a seal breach is available as an event.
> **Context:** `mars.hull.SealChangedEvent` on the NeoForge bus carries `level()` and `change()` (`before`, `after`, `breach()`, `sealedNow()`, `volume`); posted once per change on the server tick thread.
> **Needed from them:** subscribe from `mars/learning/**` for the seal-breach action; use `change().breach()`. It carries no player, so the log must attribute with its own rules.
> **Done when:** a learning test fires the event and sees one log line.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** straw-man strings need the content-rules check: `monitor.row.dome`, `monitor.dome.sealed|leaking|too_big|checking`, `dome.breach`, `dome.repaired`.
> **Context:** `pack/assets/mars/lang/en_us.json`.
> **Needed from them:** final wording and reading level (CR-F9 and CR-T7 style); also whether the HUD chip wants a different word than the Monitor.
> **Done when:** keys final; key ids unchanged.

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** hull plate, glass and door textures are script-made straw-man (16 px); `tools/screenshots/out/20261127-192420/dome_*.png`.
> **Needed from them:** replacement textures under the same file names (the scale rule is 16 px = 1 m).
> **Done when:** same names, 16 x 16, glass keeps alpha.

> **To:** Lead Game Designer · **Problem type:** ill-structured (small)
> **Problem:** the Monitor is the dome's seed, so a dome with no Monitor is invisible to the game. The layout puts the Monitor in Ops against the north wall.
> **Your view (Technical Lead):** fine for the slice. Alternatives: a dedicated "dome core" block (more art, another thing to teach), or the oxygen maker as the seed (ties sealing to life support in slice 2). I recommend deciding in slice 2 whether the oxygen maker should also be a seed.
> **Needed from them:** a call by slice 2.
> **Done when:** the seed rule is in dome-layout section 5.

## 6. Risks
- The starting dome (about 728 blocks and the Landing walk) is not built, so slice 1's exit is "rule proven on rooms", not "owner can walk the layout". Slice 2 should start with it.
- 2,000-cell cap is invented; a real dome plus Phase 2 tube may need more. One constant.
- The 5 s safety net means unreported changes (explosions, pistons, `/setblock`) can show up to 5 s late; a long-lived leak shows quickly, a very short breach shorter than 5 s is invisible. Acceptable at slice 1.
- Busiest Monitor state with a leaking dome is 165 GUI px, over the 130 px target. It fits scale 3, not scale 4.
- Hull Glass translucency not seen under shaders or other renderers.

## 7. Shared model updates
- New term: **dome** = the air volumes touching a registered Planetary Monitor; **sealed** = every one enclosed by airtight blocks within the 2,000-block cap (ARCHITECTURE 2.3).
- Rule: **full block or nothing** is implemented in one function (`mars.hull.Airtight`).
- Lesson: GameTest batch order changes when batches are added; a state-equality check that passed by luck of order failed (empty machine lists). Fixed in `PlanetSim.toState`; keep save-and-load equality tests independent of batch order.
- Lesson: `/setblock` of a same-block state change fires no block hook; code that must notice state changes of existing blocks needs the real interaction hook or a safety-net recheck.
