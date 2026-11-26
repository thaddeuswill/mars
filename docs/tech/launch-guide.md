# Launch Guide: Running the Stage 1 Build

**Owner:** Technical Lead · **Status:** Stage 1, 2026-11-26 · **Build:** `mars-0.1.0-stage1.jar` (companion mod only; no modpack yet)

> **What this build is.** An empty Mars dimension you can walk on, plus two commands. No machines, no Planetary Index in the world yet, no sky or fog changes beyond a tan sky and fog colour. The pure simulation core (Index step, boiling point, ice rule) exists and is unit-tested, but isn't connected to the world yet. See the Stage 1 report for exactly what was tested.
>
> **What was NOT tested by us:** loading the built jar from a `mods` folder in a normal NeoForge install (our dev runs load the compiled classes directly, not the jar).
>
> **Also not tested:** the steps below on Windows or macOS, or with the official Minecraft launcher. We built and tested on Linux only (test-strategy §10, checklist P1–P2). Please note anything that differs.

## 1. Play it on your own computer

You need **Minecraft: Java Edition** (not Bedrock, not Minecraft Education) and the official Minecraft Launcher.

1. **Run Minecraft 1.21.1 once, unmodded.** In the Launcher: Installations → New installation → Version **release 1.21.1** → Create → Play. Quit at the title screen. This downloads the game files NeoForge needs.
2. **Install NeoForge 21.1.267 or newer (21.1.x).**
   - Download the installer: <https://maven.neoforged.net/releases/net/neoforged/neoforge/21.1.267/neoforge-21.1.267-installer.jar> (or the newest 21.1.x from <https://neoforged.net>). You need Java to run it; the Minecraft Launcher's own Java isn't on your PATH, so install a Java 21 runtime if double-clicking does nothing.
   - Open it, choose **Install client**, keep the suggested folder, click OK.
   - The Launcher now has a **NeoForge** installation.
3. **Add the mod.** Copy `mars-0.1.0-stage1.jar` into your `mods` folder (create it if it doesn't exist):
   | System | Folder |
   |---|---|
   | Windows | `%APPDATA%\.minecraft\mods` (paste that into the File Explorer address bar) |
   | macOS | `~/Library/Application Support/minecraft/mods` |
   | Linux | `~/.minecraft/mods` |
4. **Start the NeoForge installation** from the Launcher. On the title screen, click **Mods**: "Mars Terraforming" (version 0.1.0-stage1) should be listed.
5. **Create a new world** (Singleplayer → Create New World; any settings). Use a **new** world: whether an old world picks up the Mars dimension is **unverified**.
6. **Go to Mars:** press `T` and type `/mars go`. You land on flat red sand at the centre of planet 1. Type `/mars back` to return to the world spawn. (These two commands work without cheats in this build; later builds replace them with planet assignment.)
   - Alternative with cheats on: `/execute in mars:planet_1 run tp @s 0 64 0`.
7. **What you should see:** flat red sand to the horizon, a tan sky and tan-brown fog (the start-of-game colours from the planetary keyframes, here set as plain biome colours), no mobs, no weather. If you see something else, a screenshot helps.

## 2. Developer commands (from `mod/`)

All of these use the Gradle wrapper; it downloads Gradle 9.2.1, NeoForge 21.1.267, and Minecraft 1.21.1 on first use. Java 21 must be installed.

| Command | What it does | Ran here? |
|---|---|---|
| `./gradlew build` | Compiles, checks formatting (Spotless), runs the unit tests, builds `build/libs/mars-0.1.0-stage1.jar` | Yes, passes |
| `./gradlew test` | Unit tests only (pure core, content files, architecture rules) | Yes, 37 tests pass |
| `./gradlew spotlessApply` | Reformats Java to Google Java Style | Yes |
| `./gradlew runGameTestServer` | Headless GameTest server with the mod: boot checks, then exits (non-zero on any failure) | Yes, 7/7 checks pass |
| `./gradlew runSelftestServer` | Real dedicated server: loads the Mars level, runs the boot checks (including travelling a fake player to Mars), stops cleanly | **No: needs the Minecraft EULA accepted** (see below) |
| `./gradlew runClient` | The game with the mod, at 1280×720 | Only through the screenshot run (section 3) |
| `./gradlew runServer` | A normal dev server | No (EULA) |

**EULA.** A dedicated server (`runServer`, `runSelftestServer`) won't start until `eula.txt` in its run folder says `eula=true`. That is you accepting Mojang's EULA (<https://aka.ms/MinecraftEULA>), so we didn't do it for you. To run the self-test: read the EULA, then create `mod/run-selftest/eula.txt` containing `eula=true` and run `./gradlew runSelftestServer`. Every check prints `MARS-SELFTEST PASS|FAIL check=… expected=… read=…`, and the last line is `RESULT PASS` or `RESULT FAIL … first_failing_check=…`.

## 3. Screenshots

`tools/screenshots/run-shots.sh` starts the real client under a virtual display (Linux, Xvfb, software OpenGL), creates a fresh test world, goes to Mars, places a few blocks, and saves PNGs. On your own computer you don't need Xvfb: run `./gradlew runShotsClient` from `mod/` and the same script runs in a normal window. Results and limits are in the Stage 1 report.
