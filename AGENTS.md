# AGENTS.md: start here (for Codex, ChatGPT or any coding agent)

You are joining a small team building **Mars Terraforming**: a Minecraft (Java 1.21.1, NeoForge 21.1.267) mod and datapack/pack for a learning game. It is a **design exercise**: design it as if learners aged 12 to 18 would play it (NGSS-aligned), but there are no real students. The owner playtests personally and decides. The owner is short on credits, so **be efficient: do small, verified pieces of work, commit often, and do not re-read the whole repo for every task.**

Claude and Codex coordinate as peers. Claim tasks and file scope in `BACKLOG.md`, check for overlap, then do the work and report. Do not redesign things that are already decided.

## 1. Read these first (in this order, stop when you have enough)

**`HANDOFF.md` has the current state, the in-flight work, open owner questions and the ordered next steps. Read it right after this file.**

| File | Why |
|---|---|
| `README.md` | Index and ownership table |
| `PROCESS.md` | Definition of done, iteration gates, **how to reply to the owner** |
| `BACKLOG.md` | Owner requests, newest first. Your task list. |
| `TEAM.md` | Shared mental model, **Decision Log** (what is settled), glossary, lessons |
| `ROADMAP.md` | Stages. Stage 1 done, Stage 2 in progress (first playable) |
| `ARCHITECTURE.md` | Code structure; §2.18 round solar array footprint, §2.19 planets/Monitor/simulation |
| `docs/tech/stage2-plan.md` | The Stage 2 task list and slices |
| `docs/team/reports/` | Latest report per task (newest filenames last). Read only the latest 2 or 3. |
| `DESIGN.md`, `LEARNING.md` | Only if your task touches gameplay or learning content |

Role prompts for the six Claude agents are in `.claude/agents/*.md` (Producer, Technical Lead, Lead Game Designer, Lead Instructional Designer, Lead Artist, Subject Matter Expert). If your task is in one role's area, read that file and work in that role's voice and limits.

## 2. Settled decisions (do not reopen without the owner)

- Custom Mars dimension (`mars:planet_1`; any dimension in the `mars` namespace is a planet). Global progress per planet. Groups of 3 to 5 per planet.
- Classroom mode about 11 to 12 hours; standard about 40 hours. Rates, group size and holds are scaled; **physical thresholds are never scaled**.
- Gates **lock in**. Equilibrium pressure is **75 kPa**. A sol is 20 simulation minutes.
- Planetary Index: pressure, temperature, hydrosphere, O2, CO2, biomass. Pure-Java simulation core with **ArchUnit** rules; keep Minecraft out of `mars.core` and `mars.index.sim`.
- Phase 3 rule A: CO2 is the oxygen source.
- Art: 1 block = 1 m = 16 px (1 px = 6.25 cm). Real NASA hardware is the reference (InSight-style round 12-segment array, 3 x 3 footprint, deck about 1 m). Gradual dust weathering.
- Numbers that are invented must be labelled invented. Science claims need a source and a "verified / not verified" mark.

## 3. How to build and test (run from `mod/`)

Java 21 and the Gradle wrapper (downloads Gradle 9.2.1, NeoForge, Minecraft on first use).

```bash
./gradlew spotlessApply          # format Java (Google style); the build fails if unformatted
./gradlew build                  # compile, format check, unit tests, jar
./gradlew test --rerun           # unit tests only
./gradlew runGameTestServer      # headless boot/GameTest checks; must print RESULT PASS
```

- A boot test that prints no `RESULT` line is a **failure** even if Gradle says success.
- Real-client screenshots (Linux, Xvfb, software GL): `tools/screenshots/run-shots.sh /ABSOLUTE/path/to/script.shots`. **Use an absolute script path**, or it fails and silently copies old screenshots. Check the PNG timestamps and look at the images. Scripts live in `tools/screenshots/*.shots`.
- Do not start two client runs at once.
- `runServer` and `runSelftestServer` need the Minecraft EULA. **Only the owner accepts the EULA.** Never write `eula=true`.
- Art previews: `tools/art-preview/` (Blender 4.0 renderer, browser viewer). See its README.

## 4. Rules of work

1. **Report only what you actually ran.** If you did not run it, say "not run". Never write "should work" as a result. Distinguish verified from unverified, always.
2. **Look at screenshots yourself** before describing them. Check the file is new.
3. Keep changes small. One task, one commit. Commit messages say what and why. Push to the branch the owner names (currently `claude/awesome-sagan-tb7mon`). No force-pushes, no history rewrites. Do not open a pull request unless asked.
4. Do not fetch third-party code from GitHub and run it without telling the owner. Do not add dependencies without a note in `ARCHITECTURE.md`.
5. Do not delete or overwrite files you have not looked at. Do not change the Decision Log yourself: propose, and write it in your report.
6. Content rules for learning text and forecasts are in `docs/learning/content-rules.md` (e.g. CR-F9, CR-F10). Check them before writing journal or Monitor text.
7. Keep data (balance numbers, rates) in `pack/data/mars/...` JSON and the balance sheet `docs/design/balance-sheet.md`, not hard-coded. Tests compare the two.
8. Update docs when behaviour changes: `ARCHITECTURE.md`, `docs/tech/launch-guide.md`, `BACKLOG.md` status, the report.
9. Prefer pure-Java and data-driven changes over new Minecraft-specific code. Dedicated-server safety: register client handlers with a lambda, never a method reference that loads `Screen` on a server.

## 5. Where the code is

```
mod/                        NeoForge mod (Gradle, Java 21)
  src/main/java/mars/
    core/ index/sim/        pure simulation (no Minecraft imports)
    planets/ monitor/       planet data per dimension, Planetary Monitor
    machines/               blocks (outgasser, oven, solar panel + canopy, battery, oxygen maker, monitor)
    client/                 client-only screens and dev tools
    admin/                  /mars commands
    selftest/               boot checks and GameTests
  src/test/java/            unit and content tests
pack/                       datapack + resource pack (data/mars, assets/mars)
tools/art-preview/          Blender renderer, viewer, model generators
tools/screenshots/          in-game screenshot harness and out/<timestamp>/ results
docs/                       team reports, design, learning, science, tech, art, production
```

## 6. Current state and next tasks (full detail in `HANDOFF.md`; update both when it changes)

State as of 2026-11-27:
- Stage 1 (mod skeleton, empty Mars world) done.
- Stage 2 slice 1 done and accepted: planet values saved per dimension, Regolith Outgasser raises pressure by 0.10 kPa per sol (data file), Planetary Monitor screen, `/mars index get|advance|set`, `/mars monitor`. 61 unit tests and 35 boot checks passed when last run.
- Round solar array: 3 x 3 footprint (centre block plus 8 invisible canopy cells), loot tables, 6 dust-stage models. Models were raised to put the disc top at 16 px; the canopy shape now matches the 15–16 px disc. See `BACKLOG.md` #28 and #32.
- Monitor screen narrowed to 232 px and Claude checked the client screenshots. Stage 2 slice 2 and the raised-array shape fix are complete (see `HANDOFF.md`). The stale arrival message was removed in `07fba27`.

Good tasks for you (check BACKLOG.md first, then claim by adding a line there):
1. Remaining Stage 2 slices: power (solar panel and battery), the pressure machine chain, "Dome stable" unlock.
2. Technical Lead leftovers: forecast JSON `resolve_source` and the CR-F10 test; Monitor minimum view; a frustum-culling test for an array crossing a chunk border.
3. Pure-Java unit tests and ArchUnit rules for any new simulation code.
Do **not** pick: art direction, story, learning objectives, EULA, external reviewers. Those are the owner's or other roles'.

## 7. How to report to the owner

The owner wants short replies (PROCESS.md): **what changed, recommended next steps, and a screenshot if anything is visual.** Then they accept, change, add, or say continue. Put the long version in a report file: `docs/team/reports/YYYY-MM-DD-<role>-<topic>.md` with these sections: Handoffs received, Done (real output), Decisions needed, Handoffs, Risks, Shared model updates (lessons and decisions). Add new owner requests to `BACKLOG.md` with the owner's exact words, then route them.

## 8. Working alongside Claude

- Claude works in the same repo and branch. Always `git pull --rebase` (or fetch and merge) before you start, and push soon after you finish, so you do not collide.
- Do not edit the same file as a running agent. If the Technical Lead agent is mid-task on `mod/`, take a different file area or wait.
- If two sources disagree (code, docs, a report), do not guess: the order of authority is owner message, `TEAM.md` Decision Log, `ARCHITECTURE.md`, latest report, older docs. Write the conflict into your report.
