# Report: Consistency Review and Risk Register

- **Role:** Producer
- **Date:** 2026-11-26
- **Outputs:** `docs/production/consistency-review.md`, `docs/production/risk-register.md`, fixes to `ROADMAP.md`
- **Note:** Ran before the team protocols existed and before the Fun and Fantasy sections were added to DESIGN.md. The coordinator applied the protocols when filing.

## Done
- **Consistency review:** 2 blockers, 19 major, 20 minor findings, each with owner and fix.
  - **C-01 (blocker):** Phase 1 can't reach the Phase 2 gate. The atmospheric processor is unlocked by the very gate it must reach, and no other pressure source is designed.
  - **C-02 (blocker):** no model for how a whole class plays when a world is built for 1–6 players.
- **Risk register:** 29 risks plus a scope cut list. The highest are the 40 hours of content, the size of the companion mod, schools that can't run modded Java, and having no human playtesters.
- **ROADMAP.md:** Stage 3 gate wording now matches DESIGN.md. Added missing items for work already described elsewhere. Added "resolve the two blockers" to the next steps.

## Decisions needed (classified by coordinator)
| Decision | Recommendation | Type |
|---|---|---|
| How a class plays (C-02) | Groups of 3–5 students per planet | Ill-structured |
| Classroom hours vs. unit plan mismatch (M-01) | Stretch to ~17 play periods, keep ~11–12 h | Ill-structured |
| Usernames in teacher CSV (M-07) | IDs only, remove username option | Well-structured (privacy) |
| Journal: FTB Quests or custom (M-12) | Custom | Well-structured |
| Learning layer in the vertical slice (M-19) | Pull a minimal set into Stage 3 | Ill-structured |
| LMS integration timing | Post-release, opt-in | Well-structured |
| README owner | Producer | Well-structured |
| Who runs real playtests, how often | — (needs user) | Ill-structured |
| Licenses for our own code, assets, materials | — (needs user) | Well-structured |

## Handoffs
- **Lead Game Designer:**
  - Phase 1 pressure source.
  - Classroom pacing that scales rates, not raw thresholds.
  - KubeJS row in DESIGN.md.
  - What players do during long holds.
  - Minor items.
- **Lead Instructional Designer:**
  - Unit plan hours.
  - Objectives for A4 and B4.
  - Measures that work per student on a shared world.
  - Stale values and NGSS table gaps.
- **Technical Lead:**
  - Live conditions next to locked gates.
  - Pause and the meaning of an "in-game day".
  - Backups kept separate from the action log.
  - Random per-world IDs.
  - CSV export and the mapping file.
  - Grade band storage.
  - Admin API.
  - Colony Record storage.
  - Can schools run modded Java?
  - Mod licenses.
- **Subject Matter Expert:**
  - Breathable values and oxygen partial pressure.
  - Ice-to-water rule.
  - Sky color.
- **Lead Artist:** a visible cue for all six planet values.

## Risks
- Scope (9).
- Classroom deployment (9): school Minecraft Education licenses may not run NeoForge (unverified).
- No human playtesters (9).
- Minors' data: Minecraft IDs in the log, username export, no consent plan yet.
