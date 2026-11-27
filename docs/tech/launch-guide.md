# Launch Guide: Stage 2 Playtest

**Owner:** Technical Lead · **Build:** Mars Terraforming companion mod for Minecraft Java 1.21.1 and NeoForge 21.1.267.

This build includes the Mars world, solar and battery power, Regolith Ovens and Outgassers, pressure-dependent sky and fog, and a Planetary Monitor with pressure history and next-goal progress. The packaged jar in a normal Launcher installation and the Windows and macOS steps still need verification. See [HANDOFF.md](../../HANDOFF.md) for the latest build evidence.

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

On Mars the **Regolith Outgasser is locked** for survival players until the goal **Dome stable** is reached: place a Solar Panel (power covers use) and wait 3 sols (1 hour at standard pace; `/mars index advance 3.2` skips ahead with cheats). The Planetary Monitor shows "Next goal" with the count, then "Goals reached". **Creative players can place the Outgasser at any time**; operators can use `/mars gate list` and `/mars gate latch dome_stable`. A reached goal never reverts. "Phase 2 begins" fires at 5 kPa and unlocks nothing yet. Placing with `/setblock` is never blocked.

### 1c. Operator controls

Enable cheats for the following commands in a singleplayer test world.

| Command | Use |
|---|---|
| `/mars monitor` | Open the Monitor snapshot from chat. |
| `/mars index get` | Inspect the current planet's simulation values. |
| `/mars index advance 3.2` | Advance the planet by 3.2 sols for a test. |
| `/mars pause`, `/mars resume`, `/mars session` | Pause, resume, or inspect the session. |

Slice 8 operator commands (permission level 2, all logged; they work only inside a Mars dimension):

| Command | Use |
|---|---|
| `/mars machines` | List the planet's machines by type, with the first positions and how many run below full output. |
| `/mars checkpoint save [name]` | Save the planet's numbers, goals and history (up to 8 per planet; the oldest is dropped; `start` is reserved). A checkpoint is also taken automatically at each session start. |
| `/mars checkpoint list` | List the checkpoints with the sol each was taken at. |
| `/mars checkpoint restore <name>` | Roll the planet back to a checkpoint, or to `start` for the start values. Blocks, machines, items and inventories are **not** rolled back. |
| `/mars index export` | Write a readable planet report (numbers only, no player data) to `mars-data/exports/` in the game folder. |

For a full rollback of the world (blocks and inventories too), copy the world folder before a class; the checkpoint command only rewinds the planet's simulation.

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
