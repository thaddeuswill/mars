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
5. **Create a new world** (Singleplayer → Create New World; any settings). A world made with an older build keeps its old flat Mars (see "New terrain (W1)" below); to see the new Mars, make a new world.
6. **Go to Mars:** press `T` and type `/mars go`. You land on a flat starting pad at the centre of planet 1 (a new world; an older world lands on the old flat sand). Type `/mars back` to return to the world spawn. (These two commands work without cheats in this build; later builds replace them with planet assignment.)
   - Alternative with cheats on (new world, pad top block at y 80): `/execute in mars:planet_1 run tp @s 0 82 0`.
7. **What you should see (new world):** a flat red-ochre pad about 64 blocks wide, then gently uneven ground (3 to 6 blocks of relief) with darker patches, small craters with raised rims, and dark dunes and layered flat-topped buttes farther out; a tan sky and tan-brown fog (the starting colors from the pressure keyframes), no mobs, no weather. If you see something else, a screenshot helps.

### 1-W1. New terrain (W1)

- **Only new worlds get it.** Each world remembers which Mars it has. A world that already had Mars data (made with an older build) keeps the old flat red sand forever, also in chunks you have not visited yet, so you never see a seam. The choice is saved in the world's `data/mars_terrain.dat` and the game log says `Mars terrain mode for mars:planet_1: noise_v1` (new) or `legacy_flat` (old). To see the new Mars in an old save, make a new world.
- **Places to look (any new world, creative, `/mars go` first).** Spawn pad: the 32 blocks around you are exactly flat. Small craters are a few dozen blocks away (the first two are hand-placed at about x 70, z -52 and x -58, z 74). Large craters with a central peak and flat floor are 120 to 300 blocks wide and come about one per 1000 blocks. Dune fields have gentle slopes on the upwind side and steep slip faces downwind (the wind blows toward compass 110 degrees, set in `pack/data/mars/planets/planet_1.json`). Layered flat-topped buttes (10 to 27 blocks high) are about 100 blocks and more from the pad. Fly with creative flight to see them from above.
- **New blocks (creative tab "Mars Terrain").** Basaltic Sand (dark surface patches and dune sand), Hematite Ore (drops raw iron; found in veins in rock and richly in the butte country), Dark Sand Ore (smelts to glass), Ground Ice (lenses 3 to 12 blocks under the surface; plain blocks for now, melting is later). Their textures are **placeholders**. All ore amounts and places are invented numbers in `pack/data/mars/worldgen/`.
- **Stylised.** Large craters are shrunk about 25 times and the buttes are much smaller than real ones; the journal will say so.
- **Cost.** About 8 ms per chunk to generate on the build machine, no per-tick cost. If chunk loading feels slow on a weak computer, lower the render distance first.
- **W2 additions (new worlds only).** Five more blocks in the "Mars Terrain" tab: Duricrust (thin crust under the dust), Vesicular Basalt, Spherule Sand (drops iron nuggets), Impact Breccia (crater rims), Gypsum Vein (in the layered buttes). Buttes no longer stand within about 130 blocks of the pad. Dunes vary in spacing and show ripples (tan stripes every 3 blocks). Grey gravel patches are pebbles. **The stylised canyon** runs east-west about 780 blocks south of the pad (`/tp @s 0 120 560` then look south; it is 60 blocks deep and about 1000 long, shrunk to fit the game).
- **Not in yet.** CO2 frost (needs the polar biome), the polar cap, river-channel boulder fields, and anything that melts the ice.

### 1a. Try a powered pressure setup

Seven machine blocks are in the creative inventory under the **Mars Terraforming** tab: Regolith Outgasser, Regolith Oven, Water Reclaimer, Solar Panel, Battery, Oxygen Maker (CO2), Planetary Monitor. Place a Solar Panel, the starting Ovens, and a Planetary Monitor in Creative. Right-click the Monitor to inspect pressure, grid use, and your next goal. The Oxygen Maker is a placeable prototype. The art is placeholder: the small magenta squares are the deliberate "not final" marker, not missing textures.

The solar panel is the round InSight-style array: it takes a **3 × 3 area** (the array overhangs about 1 m on each side); if any of the 8 surrounding spots is taken, nothing is placed and a message says so. Breaking any part breaks the whole array.

To see the solar panel's dust stages (needs cheats on): `/setblock ~ ~ ~3 mars:solar_panel[dust_stage=3]` (stages 0–4), or `/setblock ~ ~ ~3 mars:solar_panel[dust_stage=4,partly_cleaned=true]`.

### 1b. Goals (slice 4)

On Mars the **Regolith Outgasser is locked** for survival players until the goal **Dome stable** is reached: place a Solar Panel (power covers use) and wait 3 sols (1 hour at standard pace; `/mars index advance 3.2` skips ahead with cheats). The Planetary Monitor shows "Next goal" with the count, then "Goals reached". **Creative players can place the Outgasser at any time;** operators can use `/mars gate list` and `/mars gate latch dome_stable`. A reached goal never reverts. "Phase 2 begins" fires at 5 kPa and unlocks the Atmospheric Processor and the Super-greenhouse Emitter (powered loads for now: 3 units per sol each, INVENTED; they change no Index row because the balance sheet has no rate for them yet). Placing with `/setblock` is never blocked.

### 1c. Operator controls

Enable cheats for the following commands in a singleplayer test world.

| Command | Use |
|---|---|
| `/mars monitor` | Open the Monitor snapshot from chat. |
| `/mars index get` | Inspect the current planet's simulation values. |
| `/mars index advance 3.2` | Advance the planet by 3.2 sols for a test. |
| `/mars pause`, `/mars resume`, `/mars session` | Pause, resume, or inspect the session. |
| `/mars pace` | Show the world's pace (standard: rate multiplier, group size, creative bypass). |
| `/mars pace standard [1-5]` | Choose the standard pace and a group size 1 to 5 (saved with the world; the game first saves a `before_pace_<time>` checkpoint; undo with `/mars checkpoint restore <name>`). Numbers live in `pack/data/mars/pace.json`. Classroom pace is hidden: `classroom_enabled` is false, so `/mars pace classroom` is not offered and answers "Classroom pace is turned off." (owner decision 2026-11-28; set the flag true to restore it, see `docs/learning/educator-guide.md` 2.4). |

Slice 8 operator commands (permission level 2, all logged; they work only inside a Mars dimension):

| Command | Use |
|---|---|
| `/mars machines` | List the planet's machines by type, with the first positions and how many run below full output. |
| `/mars checkpoint save [name]` | Save the planet's numbers, goals and history (up to 8 per planet; the oldest is dropped; `start` is reserved). A checkpoint is also taken automatically at each session start. |
| `/mars checkpoint list` | List the checkpoints with the sol each was taken at. |
| `/mars checkpoint restore <name>` | Roll the planet back to a checkpoint, or to `start` for the start values. Blocks, machines, items and inventories are **not** rolled back. |
| `/mars index export` | Write a readable planet report (numbers only, no player data) to `mars-data/exports/` in the game folder. |

Learning commands (journal and guidance; `/mars guidance` and `/mars journal` need no cheats):

| Command | Use |
|---|---|
| `/mars guidance` | Show your guidance level (gentle, standard or expert) and what it hides. |
| `/mars guidance <level>` | Set your own level in standard play. Expert hides the time-to-goal estimate on the Monitor. |
| `/mars guidance <level> <player>` | Operator: set another player's level. |
| **J key** (rebindable in Controls, category Mars) | Open the Keeper's Journal screen: titles on the left (unread ones show a dot and "new"), the text on the right. Esc or J closes. |
| `/mars journal` | List the journal entries unlocked on this planet ("(new)" marks unread). Entries unlock when their goal is reached (Dome stable gives three). A chat line "New journal entries: N" tells you once. |
| `/mars journal <n>` | Read entry n in chat. |
| `/mars debug forecast open\|close [value]` | Operator, debug only: pretend a forecast about a value (default `pressure`) is open for you, which hides the Monitor's time estimate and "Leveling off" for you only. Not a learning event; it is only logged. |

Dust storm (Stage 3 slice S3-1; permission level 2, logged, Mars dimension only):

| Command | Use |
|---|---|
| `/mars storm status` | Say whether the planet is calm (and when the next storm is), warned (arrives in), or storming (time left, sunlight factor). |
| `/mars storm start [hold]` | Start a storm now: solar supply falls by 60 % x the storm cap (Phase 1 cap 1.0 = 60 %), the Monitor shows a storm row and the cause line "Storm: less sunlight". `hold` starts the shorter 1-sol drift-event storm. |
| `/mars storm end` | End the storm now. The panels still gain their dust (+2 stages at cap 1.0) and the next scheduled storm is drawn again. |

Without commands: the first storm is warned at sol 13, starts at sol 14, lasts 2 sols, and the next comes 12 to 20 sols later. All of these are invented balance numbers in `pack/data/mars/storms/dust_storm.json`. A chat line warns, one marks onset, one marks the end. The sky does not change during a storm yet. Panels are cleaned only by replacing them (the brush and dust-off station are not built), so each storm leaves them dustier.

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

**Water Reclaimer (BACKLOG #49).** Craft it (iron ingots in a ring, glass top centre, copper ingots either side of a bucket) or take it from the Mars Terraforming tab. It is a game-sized version of the ISS water recovery system. It draws 1.5 power units per sol (invented) and so far does nothing else: no water number exists yet. The Monitor Power row lists it as running, low power or needs power. The model is taller than one block; the part above the block is look only.

**Conductor ores, carbonate and the supply chest (BACKLOG #49).** The first player to arrive on Mars also gets a **Lander Supply Chest** (4 copper ingots and 8 redstone dust), once per world, with the two Ovens (existing worlds that already handed out the Ovens do not get it; use `/give` or Creative). Three new rare blocks sit underground in the canyon and crater walls, so you find them where the ground is cut: **Sulfide Vein** (drops raw copper), **Magnetic Ore** (drops redstone; old high ground only), **Carbonate Rock** (small veins; drops Carbonate). Hold the item to read its honest one-line label. Sulfide and magnetic ore are labelled "Game invention". Dune ripples now use a new Ripple Sand block (placeholder texture from the Artist). To look for them in Creative, open the **Mars Terrain** tab. New worlds only for the new terrain; old saves keep the flat Mars.
