# How to play Mars Terraforming

This page tells you what to download and where to put it. No programming needed. It takes about 15 minutes the first time.

> **Status:** a work-in-progress prototype. Tested so far only on Linux in the developers' sandbox (software rendering). **Not yet tested on Windows or macOS, and not yet tested from a real `mods` folder.** If something does not work, see [Troubleshooting](#7-if-something-goes-wrong) and tell us what you saw.

## 1. What you need

| Item | Notes |
|---|---|
| A computer that can run Minecraft | Windows, macOS or Linux |
| **Minecraft: Java Edition** | The Java edition, not Bedrock, not Minecraft Education. Owned on your Mojang/Microsoft account |
| The **Minecraft Launcher** | The official one from minecraft.net |
| About 4 GB of free memory for the game | More is better with shaders |

## 2. Run Minecraft 1.21.1 once, with no mods

1. Open the Minecraft Launcher.
2. Go to **Installations → New installation**.
3. Under **Version**, choose **release 1.21.1**. Click **Create**.
4. Click **Play** on that installation. When the title screen appears, **quit**.

This downloads the game files that the next step needs.

## 3. Install NeoForge 21.1.267 (the mod loader)

1. Download the installer: <https://maven.neoforged.net/releases/net/neoforged/neoforge/21.1.267/neoforge-21.1.267-installer.jar>
   (A newer 21.1.x from <https://neoforged.net> also works, as long as it starts with 21.1.)
2. Double-click the installer. If nothing happens, you may need Java 21 installed. The Minecraft Launcher ships its own Java, but the installer uses the one on your computer. Get "Temurin 21" from <https://adoptium.net> if needed.
3. Choose **Install client**, keep the suggested folder, and click **OK**.
4. The Launcher now has a new **NeoForge** installation.

## 4. Download the Mars Terraforming mod

The mod is one file: **`mars-0.1.0-stage1.jar`** (version numbers change as we build).
Get it in one of two ways:

- **Easiest:** download it from this repository's `downloads/` folder (once the owner has uploaded a build there) or from the repository's **Releases** page.
  Click the file, then click the **Download raw file** button.
- **Build it yourself** from the source code: see [section 8](#8-build-the-mod-yourself).

Everything the game needs (the Mars world, textures, text, machines) is **inside that one jar**. There is no separate resource pack or datapack to install.

## 5. Put the mod in your `mods` folder

Copy the jar into the `mods` folder of your Minecraft folder. If there is no `mods` folder, create one.

| System | Where to put it |
|---|---|
| **Windows** | Press `Win + R`, type `%APPDATA%\.minecraft\mods`, press Enter. (Make the `mods` folder if it does not exist.) |
| **macOS** | In Finder choose **Go → Go to Folder…** and type `~/Library/Application Support/minecraft/mods` |
| **Linux** | `~/.minecraft/mods` |

If you used an older Mars jar before, **delete the old one** so only one Mars jar is in the folder.

Then start the **NeoForge** installation from the Launcher. On the title screen click **Mods**: "Mars Terraforming" should be in the list.

## 6. Start playing

1. **Singleplayer → Create New World.** Any settings. Turn **Allow Cheats on** if you want the test commands below.
2. **Always make a new world** after updating the mod. Only new worlds get the current Mars terrain. An older world keeps its old flat Mars.
3. Press **T** and type `/mars go`. You land on Mars (planet 1). `/mars back` returns you.
4. Right-click the **Planetary Monitor** block (creative inventory, "Mars Terraforming" tab) or type `/mars monitor`. It shows pressure, temperature, power and your next goal.
5. Press **J** for the **Keeper's Journal**.

### Useful commands (cheats on)

| Command | What it does |
|---|---|
| `/mars go` · `/mars back` | Travel to Mars and back |
| `/mars monitor` | Open the Monitor from chat |
| `/mars index get` | Show the planet's numbers |
| `/mars index advance 3.2` | Skip ahead 3.2 sols (a sol is 20 minutes of play) |
| `/mars storm start` | Start a dust storm for testing |
| `/mars pause` · `/mars resume` | Pause the planet clock |
| `/mars guidance` | Show or set how much the Monitor hints |

More: [docs/tech/launch-guide.md](docs/tech/launch-guide.md).

## 6b. Optional: make it look better (shaders and extras)

These are **optional**. The game plays without them. We have **not yet checked how the Mars sky and fog look under shaders**, and shaders may change or hide the sky that shows rising air pressure. You are the first tester. All are free from Modrinth.

Install the mods into the same `mods` folder. Download the **NeoForge 1.21.1** version of each:

| What | Link | Notes |
|---|---|---|
| **Sodium** | <https://modrinth.com/mod/sodium> | Faster graphics. Needed by Iris |
| **Iris Shaders** | <https://modrinth.com/mod/iris> | Lets you load shader packs |
| **EMI** | <https://modrinth.com/mod/emi> | A recipe viewer (press the key shown in its menu) |

Use a **matching pair** of Iris and Sodium:
- Newest pair: **Iris 1.8.14-beta.1** with **Sodium 0.8.13**.
- If that misbehaves: **Iris 1.8.12** with **Sodium 0.6.13**.

Shader pack (goes in a different folder, **not** `mods`):
- **Complementary Shaders – Reimagined:** <https://modrinth.com/shader/complementary-reimagined>
- Put the `.zip` in the `shaderpacks` folder inside your Minecraft folder (Windows: `%APPDATA%\.minecraft\shaderpacks`; macOS: `~/Library/Application Support/minecraft/shaderpacks`; Linux: `~/.minecraft/shaderpacks`). In the game: **Options → Video Settings → Shader Packs**, pick it.

Please tell us what the Mars sky, fog and storms look like with and without the shader at low air pressure (about 0.6 kPa) and later at 5 and 30 kPa. Other mods (Jade, sound mods, Distant Horizons) are in [docs/tech/optional-mods-and-shaders.md](docs/tech/optional-mods-and-shaders.md). Some of them cannot be bundled into a published pack because of their licenses; installing them on your own computer is fine.

## 7. If something goes wrong

| Problem | Try this |
|---|---|
| The Launcher has no NeoForge installation | Run the NeoForge installer again (step 3) and choose **Install client** |
| "Mars Terraforming" is not in the Mods list | The jar is in the wrong folder, or it is not the NeoForge installation. Check step 5 |
| The game crashes on start | Remove the optional mods (Sodium, Iris, EMI) and try again. Then add them back one at a time |
| The sky looks wrong or flat with shaders | Turn shaders off (Options → Video Settings → Shader Packs → Off) and compare |
| `/mars go` says unknown command | You are in a world the mod did not load in. Check the Mods list |
| Raw text such as `gate.mars.thaw` appears on screen | A text key is missing from this build. Tell us which one |
| The game is slow | Lower **Render Distance** first. New Mars terrain costs a few milliseconds per chunk |

The game logs are in `logs/latest.log` inside your Minecraft folder. Crash reports are in `crash-reports/`. Send us the newest file.

## 8. Build the mod yourself

You need **Java 21** (a "JDK", for example Temurin 21) and a copy of this repository.

```bash
cd mod
./gradlew build          # on Windows: gradlew.bat build
```

The jar is created in `mod/build/libs/`. The first build downloads Gradle, NeoForge and Minecraft files, so it needs internet access and a few minutes. To run the headless self-test: `./gradlew runGameTestServer` (it must print `RESULT PASS`).

> **Never** put `eula=true` into a server's `eula.txt` for us. Playing on a dedicated server needs you to accept Mojang's EULA yourself. We do not do that for you.

## 9. Reporting back

Tell us what you saw, in your own words. A screenshot helps. The most useful things to report:
- Did the game start? What was your system (Windows, macOS, Linux)?
- Could you reach Mars and open the Monitor?
- Did anything feel confusing, boring or unfair?
- If you tried shaders: how did the sky look?
