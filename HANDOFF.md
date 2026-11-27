# HANDOFF.md: full status for the next worker (Codex/ChatGPT) — 2026-11-27

Written by Claude because its usage limit is near and the owner asked for a complete handoff. Read `AGENTS.md` first (rules, commands, read order), then this file (state and what to do next). When you finish something, update this file.

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

In flight (a Claude Technical Lead agent was running; it may or may not have finished or been cut off by the limit):
- (1) Java shape fix for the raised array: canopy slice to y 15..16 px (0.9375..1.0), centre-block shape to match, `footprint.*` GameTest expectations (`footprint.corner_shape` becomes `[0.4375,0.9375,0.4375]->[1,1,1]`), ARCHITECTURE.md §2.18, and a screenshot run with `tools/screenshots/round.shots`.
- (2) Stage 2 slice 2: session clock and pause (invariant I-3) and "Outgasser keeps contributing while its chunk is unloaded" with a test.
- Uncommitted or WIP edits may exist in `mod/src/main/java/mars/index/sim/PlanetSim.java`. **Before doing anything, run `git pull`, `git status`, `git log --oneline -15`, and look for `docs/team/reports/2026-11-27-technical-lead-slice2.md`.** If that report exists, (1) and (2) are done; verify by running the commands in AGENTS.md section 3. If it does not exist, check what is in the working tree, run the build and tests, and finish what is incomplete. Do not redo finished work.

## 3. Known gaps and honest caveats (do not claim these work)

- **Not tested:** the jar loaded from a real `mods` folder; Windows and macOS; the dedicated server and a real save to disk and restart (needs the EULA, the owner's call); right-click on the Monitor block in the real client (screenshots use `/mars monitor`); explosions or pistons moving an Outgasser; frustum culling of an array whose overhang crosses a chunk border while its centre is off-screen.
- **Not seen in Minecraft:** the raised model (only in Blender renders) until the screenshot run after the shape fix.
- Outgasser has no power draw and no "Dome stable" unlock yet, so slice 1 pressure is not on the balance-sheet curve (40 sols with two machines gave 8.60 kPa).
- `/reload` mid-game leaves running planets on old definitions (fine for the prototype).
- Chat text shows faintly through the left edge of the Monitor panel (cosmetic; Stage 3 art).
- Estimates in the docs (about 26 developer-days for Stage 2 code) are unverified.
- The AI-design-audit skill in Claude's environment is written for web pages; it was applied by hand to the Monitor only.

## 4. Owner questions waiting for an answer (do not decide these yourself)

1. Was "the panels are too big" about the **Monitor screen** (what I changed) or the **solar panels** (today about 2.1 blocks across, close to real InSight)? BACKLOG #30.
2. Which "anti UI skills" were meant (BACKLOG #31)?
3. **EULA** for the dedicated-server self-test (`runSelftestServer`). Only the owner can accept.
4. Allow network host `www.jpl.nasa.gov` so the SME can verify the deck height against NASA directly (optional).
5. Battery wall-pack and the "about 3x MOXIE" journal line were treated as confirmed unless the owner objects.
6. Owner-only recruiting: three external reviewers (a planetary scientist, a climate modeler, science teachers).

## 5. What to do next, in order

1. Sync and verify (see section 2). Make the build and tests green and the boot test print `RESULT PASS`.
2. If the shape fix is not done: do it, run the footprint GameTests, run `tools/screenshots/run-shots.sh /home/user/mars/tools/screenshots/round.shots` (absolute path!), look at the new PNGs, send the owner the front and from-above shots.
3. Finish **Stage 2 slice 2** (session clock, pause, I-3, chunk-unload behaviour), tests first.
4. Then, per `docs/tech/stage2-plan.md`: power (solar panel and battery), the pressure machine chain, the "Dome stable" unlock, then the Monitor minimum view and the forecast `resolve_source` JSON plus the CR-F10 test.
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

`TEAM.md` (Decision Log, lessons), `BACKLOG.md` (every owner request in their words), `docs/team/reports/` (one report per task), `git log`. The full Claude conversation transcript is not in the repo.
