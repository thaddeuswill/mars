# AGENTS.md: start here (for Codex, ChatGPT or any coding agent)

You are joining a small team building **Mars Terraforming**: a Minecraft (Java 1.21.1, NeoForge 21.1.267) mod and datapack/pack for a learning game. It is a **design exercise**: design it as if learners aged 12 to 18 would play it (NGSS-aligned), but there are no real students. The owner playtests personally and decides. The owner is short on credits, so **be efficient: do small, verified pieces of work, commit often, and do not re-read the whole repo for every task.**

Claude and Codex coordinate as peers. Claim tasks and file scope in `BACKLOG.md`, check for overlap, then do the work and report. Do not redesign things that are already decided.

## 1. Read these first (in this order, stop when you have enough)

**`HANDOFF.md` section 0 is Claude's final handoff (in-flight work and exact next steps). Read it first. `HANDOFF.md` has the current state, the in-flight work, open owner questions and the ordered next steps. Read it right after this file.**

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

State as of 2026-11-27 (see HANDOFF.md for detail):
- Stage 1 done. Stage 2 slice 1 (planet values, Outgasser, Monitor, `/mars index`) and slice 2 (session clock, pause, invariant I-3, chunk-unload rule) done and accepted by the owner. 67 unit tests, 41 boot checks.
- Round solar array done: 3 x 3 footprint, raised models, shape code matches (canopy slice y 15..16 px).
- Monitor screen shrunk to 232 px; accepted.
- Slice 3 (power) done: solar panel (with dust-stage output) and battery feed one grid per planet; the Regolith Outgasser only raises pressure while powered; the Monitor has a Power row. 79 unit tests, 46 boot checks. 
- Slice 4 (gates) done: "Dome stable" (3-sol hold on a stand-in life-support state) and "Phase 2 begins" (5 kPa) are data in `pack/data/mars/gates/`, latch for good, and unlock the Regolith Outgasser for survival players (creative and `/mars gate latch` bypass). Monitor shows Next goal and Goals reached. 93 unit tests, 52 boot checks. Report `docs/team/reports/2026-11-27-technical-lead-slice4-gates.md`.
- Slice 7 done (BACKLOG #47): next-gate bar, percent and "~N sols" estimate on the Monitor goal row (`MonitorView.Goal`, `GoalProgress`); 167 unit tests, 70 boot checks; report `docs/team/reports/2026-11-27-technical-lead-slice7-gate-bar.md`. Next: forecast `resolve_source` JSON and the CR-F10 test; "Useful now" list.
- Slice 6 done (BACKLOG #46): the sky and fog follow pressure through `pack/assets/mars/keyframes/sky.json`; 156 unit tests, 69 boot checks; report `docs/team/reports/2026-11-27-technical-lead-slice6-sky.md`.
- Slice 5 done (BACKLOG #43): polar-frost sink (cold, mild, release; data in `pack/data/mars/sinks/`), Regolith Oven on the power grid, two starting Ovens once per world, hold reset as data, schema 5, Monitor frost line and Levelling off. 142 unit tests, 66 boot checks. Report `docs/team/reports/2026-11-27-technical-lead-slice5-frost.md`.
- Monitor option A (BACKLOG #38) done: centred panel, stored pressure history (`PlanetState` schema 4), plot, battery bar. 114 unit tests, 54 boot checks. Report `docs/team/reports/2026-11-27-technical-lead-monitor-option-a.md`.

- **Stage 3 slice 5 steps A to D done (2026-11-28, BACKLOG #65):** panel dust over time per panel (PlanetState schema 8), brush, dust devil, Monitor "Panel dust" line (held until the Landing is over), storm scheduler with solar cut and storm-end dust, storm Monitor lines, `/mars dust`, `/mars storm`; 347 unit tests, 146 boot checks, jar 0.4.1-stage3-j; report `docs/team/reports/2026-11-28-technical-lead-slice5-dust-storm-core.md`. Open: E (storm sky, HUD icon), F (Codex), G (F1 card).
- **Stage 3 slice 4 done (2026-11-28, BACKLOG #61):** seal kit and Ops locker, tank read-out, Outside air line, per-player Landing (8 steps, panel, intro screen, commands), journal as a server-built written book (8 entries), Shift Briefing 1; 295 unit tests, 134 boot checks, jar 0.4.0-stage3-i; report `docs/team/reports/2026-11-28-technical-lead-stage3-slice4-landing.md`. Codex: Landing events are posted as `mars.story.landing.LandingEvent`.
- **Hull lights (2026-11-27):** `mars:hull_light` ceiling lamps (level 13, 30 in the starter dome incl. shelter and Ops); 254 unit tests, 113 boot checks, jar 0.3.4-stage3-e (supersedes 0.3.3-stage3-d); report `docs/team/reports/2026-11-27-technical-lead-hull-lights.md`.
- **Habitat restyle wired (2026-11-27):** variant hull blocks, roof vent and dish registered and used by the starting dome; 252 unit tests, 110 boot checks, jar 0.3.2-stage3-c; report `docs/team/reports/2026-11-27-technical-lead-habitat-wiring.md`.
- **Stage 3 slice 2 done in part (BACKLOG #59):** starting dome placed on first `/mars go`, life support (`mars.life`), power tiers, real Dome stable (four states), Monitor Air/Water/Waiting, Blackout; 248 unit tests, 108 boot checks, jar 0.3.1-stage3-b. Machine recipes, suits and the Landing flow are not built (see the report and HANDOFF 0b).
- **Stage 3 slice 1 done (BACKLOG #57):** dome sealing (`mars.dome`, `mars.hull`), hull plate/glass/door, Monitor Dome line, PlanetState schema 6; 214 unit tests, 95 boot checks, jar 0.3.0-stage3-a. Slice 2 (life support) is next; Codex owns `mars/learning/**`.

Good tasks for you (check BACKLOG.md first, then claim by adding a line there):
1. Stage 2 slice 6: one visible world response (sky tint keyed on pressure); then the Monitor minimum view.
2. (Open) forecast and Monitor items below.
3. Technical Lead leftovers: forecast JSON `resolve_source` and the CR-F10 test; Monitor minimum view; a frustum-culling test for an array crossing a chunk border.
4. Pure-Java unit tests and ArchUnit rules for any new simulation code.
Do **not** pick: art direction, story, learning objectives, EULA, external reviewers. Those are the owner's or other roles'.

## 7. How to report to the owner

The owner wants short replies (PROCESS.md): **what changed, recommended next steps, and a screenshot if anything is visual.** Then they accept, change, add, or say continue. Put the long version in a report file: `docs/team/reports/YYYY-MM-DD-<role>-<topic>.md` with these sections: Handoffs received, Done (real output), Decisions needed, Handoffs, Risks, Shared model updates (lessons and decisions). Add new owner requests to `BACKLOG.md` with the owner's exact words, then route them.

## 8. Working alongside Claude

- Claude works in the same repo and branch. Always `git pull --rebase` (or fetch and merge) before you start, and push soon after you finish, so you do not collide.
- Do not edit the same file as a running agent. If the Technical Lead agent is mid-task on `mod/`, take a different file area or wait.
- If two sources disagree (code, docs, a report), do not guess: the order of authority is owner message, `TEAM.md` Decision Log, `ARCHITECTURE.md`, latest report, older docs. Write the conflict into your report.
