# Launch Guide: Stage 3 (slice 1) Playtest

**Owner:** Technical Lead · **Build:** Mars Terraforming companion mod for Minecraft Java 1.21.1 and NeoForge 21.1.267.

This build (`0.3.0-stage3-a`) adds the **dome**: hull blocks, a sealing check and a Dome line on the Monitor (section 1d). It also includes the Mars world, solar and battery power, Regolith Ovens and Outgassers, pressure-dependent sky and fog, and a Planetary Monitor with pressure history and next-goal progress. The packaged jar in a normal Launcher installation and the Windows and macOS steps still need verification. See [HANDOFF.md](../../HANDOFF.md) for the latest build evidence.

## 1. Play it on your own computer

You need **Minecraft: Java Edition** (not Bedrock, not Minecraft Education) and the official Minecraft Launcher.

1. **Run Minecraft 1.21.1 once, unmodded.** In the Launcher: Installations → New installation → Version **release 1.21.1** → Create → Play. Quit at the title screen. This downloads the game files NeoForge needs.
2. **Install NeoForge 21.1.267 or newer (21.1.x).**
   - Download the installer: <https://maven.neoforged.net/releases/net/neoforged/neoforge/21.1.267/neoforge-21.1.267-installer.jar> (or the newest 21.1.x from <https://neoforged.net>). You need Java to run it; the Minecraft Launcher's own Java isn't on your PATH, so install a Java 21 runtime if double-clicking does nothing.
   - Open it, choose **Install client**, keep the suggested folder, click OK.
   - The Launcher now has a **NeoForge** installation.
3. **Add the mod.** Copy the Mars Terraforming jar supplied for this playtest into your `mods` folder. If you are building from source, use the jar produced by the current checkout in `mod/build/libs/`. Remove any older Mars jar from that profile first.
   | System | Folder |
   |---|---|
   | Windows | `%APPDATA%\.minecraft\mods` (paste that into the File Explorer address bar) |
   | macOS | `~/Library/Application Support/minecraft/mods` |
   | Linux | `~/.minecraft/mods` |
4. **Start the NeoForge installation** from the Launcher. On the title screen, click **Mods**: “Mars Terraforming” should be listed.
5. **Create a new world** (Singleplayer → Create New World; any settings). Use a **new** world: whether an old world picks up the Mars dimension is **unverified**.
6. **Go to Mars:** press `T` and type `/mars go`. You land on flat red sand at the centre of planet 1. Type `/mars back` to return to the world spawn. (These two commands work without cheats in this build; later builds replace them with planet assignment.)
   - Alternative with cheats on: `/execute in mars:planet_1 run tp @s 0 64 0`.
7. **What you should see:** flat red sand to the horizon, a tan sky and tan-brown fog (the starting colors from the pressure keyframes), no mobs, no weather. If you see something else, a screenshot helps.

### 1a. Try a powered pressure setup

Six machine blocks are in the creative inventory under the **Mars Terraforming** tab: Regolith Outgasser, Regolith Oven, Solar Panel, Battery, Oxygen Maker (CO2), Planetary Monitor. Place a Solar Panel, the starting Ovens, and a Planetary Monitor in Creative. Right-click the Monitor to inspect pressure, grid use, and your next goal. The Oxygen Maker is a placeable prototype. The art is placeholder: the small magenta squares are the deliberate "not final" marker, not missing textures.

The solar panel is the round InSight-style array: it takes a **3 × 3 area** (the array overhangs about 1 m on each side); if any of the 8 surrounding spots is taken, nothing is placed and a message says so. Breaking any part breaks the whole array.

To see the solar panel's dust stages (needs cheats on): `/setblock ~ ~ ~3 mars:solar_panel[dust_stage=3]` (stages 0–4), or `/setblock ~ ~ ~3 mars:solar_panel[dust_stage=4,partly_cleaned=true]`.

### 1b. Goals (slice 4)

On Mars the **Regolith Outgasser is locked** for survival players until the goal **Dome stable** is reached: place a Solar Panel (power covers use) and wait 3 sols (1 hour at standard pace; `/mars index advance 3.2` skips ahead with cheats). The Planetary Monitor shows "Next goal" with the count, then "Goals reached". By default, **Creative players can place the Outgasser at any time**. For a classroom playtest, stop the game or server, set `creativeGateBypass = false` in `config/mars-server.toml` for that instance, and restart. Creative placement then requires Dome stable too. Operators can use `/mars gate list` and `/mars gate latch dome_stable`. A reached goal never reverts. "Phase 2 begins" fires at 5 kPa and unlocks nothing yet. Placing with `/setblock` is never blocked.

### 1e. The starting dome and life support (Stage 3 slice 2, jar 0.3.1-stage3-b)

1. On Mars type `/mars go`. The first time, a 23 x 16 dome is built around the arrival point and you stand in the shelter (five bunks, five oxygen tanks). It is the same dome for every group size.
2. Read the Planetary Monitor in Ops (north wall). It now has an **Air** row (stored out of 200 and the net a sol), a **Water** row only when water will not last, a **Dome** row, and under the Dome stable goal a **Waiting:** line that names the one thing missing.
3. Life support starts at the end of the Landing (sol 1.2). Standard, one player: Dome stable arrives about sol 9.2 (about 3.1 h at 20 min a sol), if you keep the dome sealed and powered. `/mars index advance 3` skips time for a test.
4. Hab B (south-west annex) leaks on purpose through a **Cracked Hull Plate** in its west wall: break it and put a Hull Plate in. Hab B holds nothing required, so the leak costs no air.
5. Break a wall plate in Ops (or any room with a tank, maker, reclaimer, battery or Monitor) and the air drains 10 units a sol faster. With empty tanks a survival player is slowed and after 2 minutes **blacks out** and wakes in the shelter, nothing lost (game rule).
6. Config (server config file): `group.size` (1 to 5) and `pace.classroom`. Recipes: Hull Plate, Oxygen Tank and Hull Door can be crafted with iron, copper and glass stand-ins; the six machines cannot yet (they cost 10 to 24 items, the grid holds 9; see the slice 2 report). Lost machines come from the creative tab or `/give`.
7. Worlds saved before 0.3.1 keep the old stand-in for Dome stable (no air or water, no Air row) and are **not** given a starting dome: `/mars go` takes you to the old arrival point as before, so nothing you built is overwritten. To see the new loop, start a new world.

### 1d. Build a sealed room (Stage 3 slice 1)

Three building blocks are in the **Mars Terraforming** creative tab: **Hull Plate**, **Hull Glass**, **Hull Door**. There are no recipes yet (slice 2). To try the sealing check:

1. On Mars, build a closed room of Hull Plate (floor, walls and roof; Hull Glass for windows; a Hull Door is two blocks high).
2. Stand inside and place a **Planetary Monitor** against a wall. Placing it registers the room as a dome.
3. Right-click the Monitor. A **Dome** line reads **Sealed**, **Leaking** (a hole, an open door, a slab or fence in the wall, or no roof), **Too big to check** (more than 2,000 blocks of air) or **Checking** (first tick). It updates within a tick when you break or place a hull block or open or close a Hull Door by hand, and within 5 seconds for other changes (a vanilla block, `/setblock`, an explosion).
4. The rule is **a full block or nothing**: slabs, stairs, fences, panes, trapdoors and open doors let air through. A message "The dome is leaking." appears when a sealed dome is breached and "The dome is sealed again." when it is repaired.

Since slice 2 the sealed state feeds life support and **Dome stable** (section 1e). The Hull Door has no airlock interlock yet (slice 6). A Monitor placed in a world saved before this build is not registered as a dome until you place it again.

### 1c. Operator controls

Enable cheats for the following commands in a singleplayer test world.

| Command | Use |
|---|---|
| `/mars monitor` | Open the Monitor snapshot from chat. |
| `/mars index get` | Inspect the current planet's simulation values. |
| `/mars index advance 3.2` | Advance the planet by 3.2 sols for a test. |
| `/mars pause`, `/mars resume`, `/mars session` | Pause, resume, or inspect the session. |

| `/mars machines` | List registered machines on this planet. |
| `/mars dust get`, `/mars dust set <0-4> [partly]` | Show panels by dust stage and the power lost to dust; set every panel to one stage (0.4.1). |
| `/mars storm get`, `/mars storm schedule <sols> [length]`, `/mars storm end` | Show the dust storm's state; decide a storm that many sols from now (warning goes out at once); start clearing an active storm (0.4.1). |
| `/mars index export` | Write a planet report to the server’s `mars-data/exports` folder. |
| `/mars checkpoint save <name>`, `/mars checkpoint list` | Create a planet simulation checkpoint and list available checkpoints. |
| `/mars checkpoint restore <name>` | Restore a simulation checkpoint. The reserved name `start` restores starting values. |

Checkpoint restore affects simulation values, progress, and history. Placed blocks, machines, and player inventories stay in the world. Use a disposable world for prototype testing. See the [slice 8 verification report](../team/reports/2026-11-27-technical-lead-slice8-admin-rollback.md).

### 1c. Operator commands (slice 8)

All need operator rights (cheats on in singleplayer) and are written to the log. Run them while standing on Mars.

| Command | Does |
|---|---|
| `/mars index get` | Print every Index value |
| `/mars index advance <sols>` | Skip simulation time (up to 200 sols) |
| `/mars index set <parameter> <value>` | Set one value, e.g. `set pressure 30` to look at the sky at 30 kPa, or `set temperature -10` for the warm branch |
| `/mars index export` | Write a readable text report of the planet (values, goals, machine counts) to `mars-data/exports/` next to the server or game folder. No player data |
| `/mars machines` | Count machines by type, with the first positions |
| `/mars gate list`, `/mars gate latch <id>` | Show goals; latch one (there is no way to un-latch a single goal) |
| `/mars checkpoint save [name]`, `list`, `restore <name>` | Save, list and restore the planet's numbers. A checkpoint `session-N` is saved automatically each time you `/mars resume`. `restore start` returns the planet to its start values |
| `/mars pause`, `/mars resume`, `/mars session` | Freeze, unfreeze, show the session |

A restore puts back the planet's numbers, goals, history and battery only. **Blocks, items, inventories and other mods' machines stay as they are.** For those, use the world backup script (`tools/world-backup.sh`, see `school-deployment.md`).

### Learning event export

Run `/mars learning export` with operator permission after moving on to a new session. It writes an `events.csv` file under the current world’s `mars-data/<world>/exports/` directory and prints the path. This prototype exports completed sessions only. Player events use pseudonyms, and planet events have an empty player column. Player and planet summary CSVs are still planned.

## 2. Developer commands (from `mod/`)

All of these use the Gradle wrapper; it downloads Gradle 9.2.1, NeoForge 21.1.267, and Minecraft 1.21.1 on first use. Java 21 must be installed.

| Command | What it does | Ran here? |
|---|---|---|
| `./gradlew build` | Compiles, checks formatting (Spotless), runs the unit tests, writes the jar to `build/libs/` | Yes, passes |
| `./gradlew test` | Unit tests only (pure core, content files, dust tuning vs. the balance sheet, machine assets, architecture rules) | Yes (see the latest report for the count) |
| `./gradlew spotlessApply` | Reformats Java to Google Java Style | Yes |
| `./gradlew runGameTestServer` | Headless GameTest server with the mod: boot checks (including machine registration and panel dust state), then exits (non-zero on any failure) | Yes (see the latest report) |
| `./gradlew runSelftestServer` | Real dedicated server: loads the Mars level, runs the boot checks (including travelling a fake player to Mars), stops cleanly | **No: needs the Minecraft EULA accepted** (see below) |
| `./gradlew runClient` | The game with the mod, at 1280×720 | Only through the screenshot run (section 3) |
| `./gradlew runServer` | A normal dev server | No (EULA) |

**EULA.** A dedicated server (`runServer`, `runSelftestServer`) won't start until `eula.txt` in its run folder says `eula=true`. That is you accepting Mojang's EULA (<https://aka.ms/MinecraftEULA>), so we didn't do it for you. To run the self-test: read the EULA, then create `mod/run-selftest/eula.txt` containing `eula=true` and run `./gradlew runSelftestServer`. Every check prints `MARS-SELFTEST PASS|FAIL check=… expected=… read=…`, and the last line is `RESULT PASS` or `RESULT FAIL … first_failing_check=…`.

## 3. Screenshots

`tools/screenshots/run-shots.sh [script]` starts the real client under a virtual display (Linux, Xvfb, software OpenGL), creates a fresh test world, goes to Mars, places a few blocks, and saves PNGs. `tools/screenshots/batch2.shots` places every machine and the panel at dust stages 0, 2, 4 and partly cleaned; `round.shots` checks the round array's footprint, outline, and lighting under the overhang. On your own computer you don't need Xvfb: run `./gradlew runShotsClient` from `mod/` and the same script runs in a normal window. Pass an absolute script path to the Linux harness and check the PNG timestamps. The latest results are in the [handoff](../../HANDOFF.md).

**Starting Ovens and the polar frost (slice 5).** The first player to arrive on Mars (`/mars go`) gets **two Regolith Ovens** in the inventory, once per world (not per player; dying or logging in again does not repeat it). Place them and a Solar Panel: each Oven draws 1 power unit per sol and adds +0.01 kPa per sol. While Mars is cold the polar frost takes back 2 % of the pressure above 0.60 kPa each sol, so pressure levels off: two Ovens settle at 1.6 kPa; one Outgasser on a clean panel settles near 5.6 kPa. The Monitor shows "Polar frost" as a cause line once it passes about 0.85 kPa, and "Levelling off" when the frost balances your machines. Temperature does not change by itself yet; to see the other two branches use `/mars index set temperature -30` (mild, 1 %) or `-10` (warm: the frost gives the air back). Dome stable and Phase 2 begins are unchanged.

## Panels, dust and the first storm (0.4.1-stage3-j)

Panels now dust by themselves: one stage about every 3 sols (0.75 sol in classroom mode), four stages, down to 60 percent output. The Monitor's Power row shows "Panel dust -n %" once your Landing is over. Craft a **Panel Brush** (2 iron ingots and 1 copper ingot, shapeless) and **hold right-click on a panel for 2 seconds** to clean it (64 uses). The first dust storm warns one sol ahead, arrives on sol 14 (sol 4.5 in classroom mode), and lasts 0.75 sol (0.5 in classroom mode). There is no storm sky or HUD icon yet (step E). For a test: `/mars storm schedule 0.5`, then `/mars index advance 0.6`. Panel dust accrues in simulation time, so it stops with `/mars pause`.
