# Technical Lead: slice 8, admin commands and rollback (2026-11-27)

## Handoffs received
HANDOFF section 0 and `docs/tech/stage2-plan.md` row 8: full debug command set, choose a rollback approach, document what is not covered. Accepted.

## Done
Code was already on the branch (`a6cab96`, verified by the coordinator: `2026-11-27-coordinator-slice8-verification.md`). This task read it, documented it and closed two test gaps.

**Command set** (`mars/admin/AdminCommands.java`; all permission level 2; refused outside a `mars` dimension; every change is logged with the actor):
| Command | Effect |
|---|---|
| `/mars machines` | Machines by type, first 8 positions, count below full output |
| `/mars checkpoint save [name]` | Snapshot of one planet; default name `manual`; `start` reserved |
| `/mars checkpoint list` | Names and sols |
| `/mars checkpoint restore <name>` | Roll back; `start` restores start values |
| `/mars index export` | Text report to `<server dir>/mars-data/exports/<time>_<planet>.txt`, planet numbers only |
Older commands (`/mars index get|advance|set`, `/mars gate list|latch`, `/mars pause|resume|session`) are unchanged.

**Rollback approach.** Two layers. (1) World backup by the start script or by copying the world folder is the default for a full rollback and is the only thing that restores blocks and inventories. (2) An in-game checkpoint: `Checkpoints.take` writes the planet's saved NBT (a few KB) into `CheckpointData` (world save data), at most 8 per planet, oldest dropped (`CheckpointList`, pure and generic). A session start takes an automatic `session-N` checkpoint. `Checkpoints.restore` calls the pure `PlanetCheckpoint.restoreOver`, which takes values, sim ticks, battery charge, gate states, pressure history and frost reservoir from the snapshot, and keeps the current machine registry and per-machine output, then resyncs the sky. Gates return to the checkpoint state; no command un-latches a single gate (I-2 keeps its meaning for normal play).

**Not covered by rollback:** placed or broken blocks; the machine registry (kept as is so it matches the blocks standing); inventories, starting-Oven grant flag and other player data; third-party machines or mods that change the planet; session clock and pause flag; other planets (checkpoints are per planet). After a restore, the registry can disagree with a snapshot taken when different machines stood, by design.

**Tests added** (`PlanetCheckpointTest`): unknown name returns empty and a list taken earlier is a copy; restore keeps the current machines and output. Launch guide updated (`docs/tech/launch-guide.md` section 1c).

## Real output
`./gradlew spotlessApply test --rerun` from `mod/`: BUILD SUCCESSFUL, 173 unit tests, 0 failures (was 171). Boot checks not re-run for this commit (no main-code change; coordinator ran 81 PASS at the prior commit).

## Decisions needed
None.

## Handoffs
None.

## Risks
Not exercised by hand: commands through a real dedicated server or client (GameTests run the commands through the command dispatcher with an operator source). Checkpoints live in world save data and grow with the number of planets (8 x a few KB each).

## Shared model updates
Lesson: rollback of the planet's numbers and rollback of the world are different things; teachers should back up the world folder for the second.
