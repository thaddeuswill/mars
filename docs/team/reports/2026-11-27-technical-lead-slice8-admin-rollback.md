# Slice 8 admin and rollback verification

## Handoffs received

Claude’s admin and checkpoint implementation, the queued Mars sun texture, and Codex’s asset test. Verification used a clean checkout at `436a63b` on Linux, Gradle 9.2.1, and Java 21.

## Done

- `spotlessCheck build test --rerun runGameTestServer` completed with `BUILD SUCCESSFUL`.
- JUnit XML reports **172 tests, 0 failures, 0 errors, 0 skipped**. All four `PlanetCheckpointTest` cases and `SunTextureTest` passed.
- Headless output ended with `MARS-SELFTEST RESULT PASS passed=81 failed=0`. Eleven admin checks covered checkpoints, value and gate rollback, physical machine retention, start reset, unknown and reserved names, permissions, export, NBT persistence and the resume checkpoint.
- The built mod jar contains the admin commands, sky renderer and Mars sun texture. Its SHA-256 is `324c5a4c68efa2cea920bdec6a1230865cab6a014c87ff73c5c1f41c2aba95d3`.

## Decisions needed

None for this verification.

## Handoffs

Operator commands are `/mars machines`, `/mars index export`, and `/mars checkpoint save|list|restore`. Restoring a checkpoint rolls back simulation values, progress and history. Placed machines, blocks and inventories stay in the world.

## Risks

| Change | Could break | Check | Result |
|---|---|---|---|
| Checkpoint restore | Simulation and placed machine state diverge | Restore pressure and gates while retaining two Outgassers | Passed |
| Operator commands | Non-operators can run commands | GameTest permission guard | Passed |
| Checkpoint storage | Checkpoints disappear during serialization | NBT round trip and resume checkpoint | Passed |
| Packaged jar | Official Launcher or platform setup fails | Install jar in a normal NeoForge profile | Pending |

## Shared model updates

The Gradle build and development GameTest runner are verified. A normal Launcher installation, dedicated-server run and real save/restart test remain open.
