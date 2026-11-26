# Report: Architecture Gaps Closed, Decisions Applied

- **Role:** Technical Lead
- **Date:** 2026-11-26
- **Outputs:** `ARCHITECTURE.md` (rewritten; sections 1–5 keep their numbers), `docs/tech/mod-licenses.md`, `docs/tech/school-deployment.md`
- **Perspective round:** Succeeds if one teacher can start, pause, back up, and export a whole class from one machine, and the game can't leak a student's identity or lose their evidence.
- **Note:** Documents only; no code yet. Blocked hosts: modrinth.com, neoforged.net, docs.neoforged.net, edusupport.minecraft.net, and the GitHub API. Licenses were read from primary sources through raw.githubusercontent.com.

## Handoffs received
- **Producer (M-03, M-05–M-11, m-19, R-09, licenses): all closed.**
  - Live conditions next to latched gates.
  - Pause, and a sol based on simulation time.
  - Backups, with evidence stored in `mars-data/<worldId>/` outside the world folder, so rollback never deletes it.
  - Random per-world IDs (`P-XXXXX`) and a mapping file that stays on the server.
  - CSV export.
  - Grade band storage.
  - Admin API.
  - Colony Record storage and UI.
- **Story bible: accepted.**
  - Gate-triggered message queue with a per-planet archive.
  - Delays counted in sols, scaled for classroom mode.
  - "Since last shift" recap.
  - Epilogue cards.
  - "Unread from earlier" bundle.
  - All of it as datapack JSON.
- **Game Designer (mid-task): accepted, nothing too costly.**
  - Pace config keys and a static `group.size`; thresholds have no scaling field.
  - Derived partial pressures on the Monitor.
  - Live helmet-off check.
  - Hold counters.
  - Fauna signature sum and caps.
  - Ending-card counters.
  - Shift Board.
  - One Index per planet.
  - The per-block ice check is replaced by a cached per-chunk heat map, which is much cheaper.
- **DOCUMENTATION.md shared format:** adopted for the message JSON.
- **New modules:** `planets`, `session`, `story`. New sections: group hosting, pause and backups, IDs and data, story system, admin API, LMS (xAPI file export, post-release, opt-in), performance budget.

## Findings
- **Mod licenses:**
  - Mekanism: MIT.
  - Create: code MIT, **assets All Rights Reserved**.
  - KubeJS: LGPL-3.0.
  - **ProbeJS: GPL-3.0, dev only, never ship.**
  - TerraBlender: LGPL-3.0. NeoForge: LGPL-2.1.
  - **FTB Library: All Rights Reserved** (FTB Quests is already rejected).
  - 1.21.1 builds exist for Mekanism, Create, and KubeJS. Java 21 is confirmed.
- **Schools:** Minecraft Education is Bedrock-based and **can't run community mods**. Education licenses don't cover Java Edition. No bulk Java licensing route was found (unverified).

## Decisions needed
| Decision | Recommendation | Type |
|---|---|---|
| Class hosting: (A) one server with a dimension per group, (B) one server process per group, (C) LAN hosted by a student | A, with B as a no-code fallback; C not supported (logs would end up on student PCs) | Ill-structured |
| Stay on Minecraft 1.21.1 (NeoForge has moved on to 26.1 / Java 25) | Yes | Well-structured |
| Reflections in a separate `reflections.csv` the teacher can switch off | Yes | Ill-structured (privacy vs. D1 assessment) |

## Handoffs
- **Lead Instructional Designer:**
  - Final CSV columns and the scope of each goal (player or planet).
  - Reword "anonymous" to "pseudonymous on the server, anonymous in every export".
  - Remove the username option.
  - Educator guide: "Requires Java Edition, not Minecraft Education".
  - Goal → NGSS JSON map.
- **Lead Game Designer:** fiction for the lobby where unassigned players wait; confirm that sleeping doesn't skip Mars time and that the sky follows simulation time.
- **Producer:**
  - Route the hosting decision.
  - Risk register: R-09 verified, R-24 can close, R-23 links to licenses.
  - ROADMAP: Stage 3 load test (4 planets × 4 players); Stage 2 GameTests confirming pause stops third-party machines.
  - DOCUMENTATION.md System Limitations → `docs/tech/school-deployment.md`.
- **Lead Artist:** name the driving Index parameter for each keyframe.

## Risks
- Pause may not stop Mekanism and Create machines (an inference). The fallback is to stop the server.
- Hardware numbers are placeholders until the load test.
- Java hardware requirements went up (unverified whether that applies to 1.21.1).
- 1.21.1 is ageing.
- Rechecks needed once the hosts are allowed.
- No legal review of pseudonymous data (COPPA, FERPA, GDPR).
