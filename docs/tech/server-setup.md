# Server and Teacher Setup Guide (draft)

**Owner:** Technical Lead · **Status:** Draft 1, 2026-11-26 (scheduled deliverable: Stage 4; drafted now so the Stage 3 slice can be set up from it) · **Readers:** a teacher or school IT person who has never run a Minecraft server · **Related:** [educator guide](../learning/educator-guide.md) (how to teach with it; Lead Instructional Designer), [school-deployment.md](school-deployment.md) (can a school run it at all), [ARCHITECTURE.md](../../ARCHITECTURE.md) §6–8, §10

> **Design exercise.** No real school will run this. It's written as if one would. **Nothing here has been tried**: no build exists yet. Commands and menu names are the planned ones (ARCHITECTURE.md §10); steps marked **[to test]** need a real run, and **[owner]** marks steps that need the owner's machines. Target: **under 30 minutes** from download to a class ready to join (Success Scorecard, Feasibility row).

---

## 1. Before you start (10 minutes of checking)

| Check | Why | If it fails |
|---|---|---|
| Every student has a **Minecraft: Java Edition** account. Child Microsoft accounts have **multiplayer enabled** by the account's family organizer | Required to launch and join (school-deployment.md, finding 6) | Ask families or IT before the first lesson; it can take time to apply |
| This is **not** Minecraft Education | Minecraft Education can't run community mods (verified, school-deployment.md) | No workaround |
| Student PCs may install and run a **launcher that reads a Modrinth pack** | Students can't install the mod loader by hand in a lesson | IT installs it, or copies a prepared game folder to each PC |
| One **server PC** for the class: 64-bit, Java 21 installed, at least **[to test]** GB of RAM free, wired network | All group planets run on it (one server, one Mars per group) | Use a second PC and split groups (section 7, fallback) |
| Students' PCs can reach the server PC on the school network, TCP port **25565** (Minecraft's usual default, **[to test]**) | Joining | IT opens the port on the school network only. **The server never needs to be reachable from the internet** |

## 2. Install the server (10 minutes)

1. Download the **classroom server bundle** (one zip). It contains NeoForge 21.1.x for Minecraft 1.21.1, the Mars companion mod, KubeJS and its two helper mods, and the classroom configs. **It does not contain Create** (licence) and leaves Mekanism out by default (optional, late-game automation only).
2. Unzip it into a folder on the server PC, e.g. `C:\MarsServer\` or `/opt/mars-server/`.
3. Run `start-server` (`.bat` on Windows, `.sh` on Linux/macOS). The first start:
   - asks you to accept the Minecraft EULA (edit `eula.txt` to `true`; read it first);
   - creates the world with the **classroom preset** and starts **paused**;
   - prints the server address to give students.
4. Type `/op <your Minecraft name>` in the server window once, so you get teacher rights in the game. (Teacher rights = the `mars.teacher` permission, default operator level 2.)

## 3. Set up the class (5 minutes, in the game)

Join from your own PC, then open the **Class overview** (key shown on screen; or `/mars overview`). Everything below is a button there; the commands are listed only as a backup.

| Step | Button | Command | Notes |
|---|---|---|---|
| Open one planet per group | Planets → Open | `/mars planet open <n>` | Up to 8; each is its own Mars with the same terrain |
| Set group size (3–5) | Planet → Group size | `/mars planet size <n> <3-5>` | Set the **planned** size; absent students don't change the planet |
| Set grade band | Planet → Band | `/mars band set <n> ms\|hs` | Science depth |
| Leave **guidance** on gentle | Planet → Guidance | `/mars guidance <n> gentle\|standard` | Gentle is the classroom default; expert isn't offered in class |
| Create join codes, or assign students | Planet → Codes / Assign | `/mars join` codes, or `/mars planet assign <player> <n>` | Unassigned students wait safely in the **Training Hall** |
| Print **player ID slips** | Class → ID slips | — | Writes `id-slips.html` with IDs only; print and cut. Students write the ID, never their name, on tests |

## 4. Every lesson

| When | Do | What happens |
|---|---|---|
| Start | **Resume** | A backup is taken automatically, then a new session starts and each group gets a "Since last shift" recap |
| ~5 minutes before the end | **Wrap up** (`/mars wrapup`) | No new forecasts or report pop-ups, no new critical failures; safety warnings still work |
| End | **Pause** | Everything on Mars freezes until next time. You can also just stop the server; time doesn't pass while it's off |
| Any time | **Needs a look** markers in the overview | A rule spotted something (e.g. low forecast accuracy, a report unsent for 2 sessions). For you only; never a grade |

## 5. When something goes wrong

| Problem | Fix |
|---|---|
| Griefing, a disaster, a crash | **Rollback**: Class → Backups → pick the backup → this planet only, or all. The server restarts. **Students' learning evidence is kept** (it's stored outside the world) |
| One group far behind | Planet → Pacing: raise that planet's rate. It never changes the science thresholds |
| A shortened unit | Planet → Set phase. The overview lists which goals the skipped phase taught |
| Student can't join: "Failed to login" or greyed-out Multiplayer | Child account's multiplayer setting (section 1) |
| Student can't connect | Wrong address, or the school network blocks port 25565 between PCs |
| Server runs out of memory or lags | Fewer open planets, lower view distance (classroom preset already lowers it), or move some groups to a second server PC (section 7) |
| Server won't start: "save is from a newer version" | You opened the world with an older build. Use the build that saved it. The server refuses rather than damaging the save |

## 6. Data: what's stored and how to export

- Everything stays on the server PC in `mars-data/<world>/`, **outside** the world folder. Nothing is sent over the internet.
- Students are random IDs (`P-7K3QX`). The link from ID to Minecraft account (`identity/players.json`) **never leaves the server PC** and is never exported.
- **Export:** Class → Export (or `/mars export all`). Writes CSV files to `mars-data/<world>/exports/<time>/`: `players.csv`, `planets.csv`, `events.csv`, `goals.csv`, and `reflections.csv` (on by default in classroom mode). What each column means: educator guide.
- **Reflections are free text.** Students may type real names; the game replaces Minecraft usernames with IDs but **can't catch real names**. Read before sharing.
- **End of the unit:** Class → Forget everyone (`/mars forget-all`). After that, nothing on the server links an ID to a student. Keep the paper ID slips only if you plan the delayed post-test.

## 7. Fallback: one server per group

If one PC can't carry the whole class, or one group's problems must not affect the others: run the same bundle **once per group** (each in its own folder, each on its own port, e.g. 25565, 25566, …) with one planet each. You get separate overviews and separate exports, one per server. **Students hosting their own worlds ("Open to LAN") is not a classroom setup:** the learning data would sit on a student's PC.

## 8. Support after release (estimate for the Producer's budget)

**All numbers unverified: no release has happened, there's no velocity data, and support demand for a niche educational pack is unknown.**

| Work | Effort estimate | Frequency (assumed) | Per year |
|---|---|---|---|
| Bug triage (issue tracker) | ~2 h/week | Ongoing in the first 6 months, then ~1 h/week | ~75 h |
| NeoForge 21.1.x bump + re-run CI and the playthrough | ~1 day per bump | ~4 per year (only when a fix we need ships) | ~4 days |
| Third-party mod updates (KubeJS, Mekanism) + re-test | ~0.5 day each | ~4 per year | ~2 days |
| Setup questions from teachers | ~1 h each | Unknown; this guide's troubleshooting table is the main way to keep it small | Unknown |
| Docs updates (this guide, the educator guide) | ~1 day per release | ~2 per year | ~2 days |
| **A port to a newer Minecraft version** | **A project, not support**: re-check every API in ARCHITECTURE.md, every mod, the art pipeline; likely weeks | When 1.21.1 builds stop getting fixes (risk R-06) | — |

Rough total without a port: **~3–4 developer-weeks per year** after the first release, plus unknown teacher support.

---

## Appendix: owner platform checklist (each stage exit) **[owner]**

What can't be tested in the agents' Linux environment (test-strategy §10). The owner runs it on their own machines and records results in the stage debrief.

| # | Check | Pass when | Stage |
|---|---|---|---|
| P1 | Install the pack from the `.mrpack` with a common launcher on **Windows** | Launches to the main menu with no errors | 1 onward |
| P2 | Same on **macOS** | Same | 3 onward |
| P3 | Start the classroom server bundle on **Windows** from `start-server.bat` | Starts paused, prints the address | 3 onward |
| P4 | Join with **two clients** (two accounts, or one real and one offline dev client) | Both reach the Training Hall; codes assign them to different planets | 3 onward |
| P5 | Run one class period's flow: resume, play 10 min, wrap up, pause, export | Export opens in a spreadsheet; no names in any file | 3 onward |
| P6 | **Weak-hardware FPS:** a PC with integrated graphics and 8 GB RAM, classroom preset, at 1280×720 | Playable (target ≥ 30 FPS, **[to set]**) in the dome, in a full dust storm, and at equilibrium; note the GPU model | 3, 5 |
| P7 | **Load test:** 4 planets × 4 players (real or fake players) on a modest server PC | No tick lag over a 30-minute run; record RAM and CPU | 3 |
| P8 | A **child Microsoft account** joins the server | Joins after the multiplayer setting is on; note how long the setting took | 4 |
| P9 | Screenshots at 1280×720 reviewed (if the headless run doesn't work, test-strategy §8) | Every screenshot that matters for the change looked at | Every build with UI changes |
| P10 | Teacher walkthrough: someone new follows this guide from section 1 | Done in under 30 minutes, with each stumble noted | 4 |
