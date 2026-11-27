# Report: Technical Lead, Stage 2 slice 4 (gates and the Outgasser unlock)

- **Role:** Technical Lead · **Date:** 2026-11-27 · Everything below ran in this Linux sandbox (Gradle `--offline`, nothing downloaded, EULA untouched).

## 0. Handoffs received
- **Coordinator (owner: "accept and continue with recommendations"):** accepted. I did not edit BACKLOG rows and did not duplicate Codex's acceptance-check docs.

## 1. Done
**What changes for the player:** a Regolith Outgasser can no longer be placed by a survival player until the goal "Dome stable" is reached (life support steady for 3 sols). At 5 kPa "Phase 2 begins" is reached. The Monitor shows "Next goal" with progress, then "Goals reached". Reached goals never revert.

- Pure (`mars.gates`, ArchUnit-checked): `Condition` (`all`, `any`, `not`, parameter comparison, `machine_state`; unknown keys rejected, so no scaling field can exist), `GateDef`, `GateTracker` (latching, hold with pause and reset grace, admin latch, unlock query).
- `PlanetSim`: gates, stand-in machine state `life_support_ok` (grid has supply and covers demand), `drainNewlyLatched`; `PlanetaryIndexModel.setAfterStep` so commands and real ticks share one path; `Pace.classroom` and `holdResetSols()` (1 / 0.5 sol); `PlanetState` schema 3 with gate states.
- Data: `pack/data/mars/gates/dome_stable.json` (hold 3 / 1 sols, unlocks `mars:regolith_outgasser`) and `phase_2_begins.json` (pressure >= 5.0).
- Minecraft: `IndexDefinitions` loads gates; `PlanetData` NBT for gates; `RegolithOutgasserBlock.getStateForPlacement` refuses survival players before the gate; `PlanetManager.announceGates` (chat plus log); `/mars gate list|latch`; Monitor rows; lang keys (straw-man, listed in `docs/design/ui-specs.md` section 9); `PlanetManager.resetForSelfTest` (works only with the test seam).
- **Unlock route chosen:** survival players are blocked at placement; **creative players pass (logged)**; `/setblock` and other non-player routes are not gated; the creative tab still lists the Outgasser; operators can `/mars gate latch dome_stable`. No recipes exist yet, so placement is the whole lock. There is no un-latch command (gates lock in).
- Docs: ARCHITECTURE.md 2.19 (four new rows), stage2-plan slice 4 row, balance sheet build note, ui-specs section 9 wording, launch guide 1b, `HANDOFF.md`, `AGENTS.md`.
- Tests: `GateTest` (14): data vs balance sheet, 3-sol latch (2.9 no, 3.1 yes), no power means no gate, short failure pauses and long one resets, classroom 1 sol, **I-1** (4.999 no and 5.0 yes in standard, group 5, classroom 3, 4, 5; a `scale` key is rejected), **I-2** (pressure back to 0.6 and no power for 60 sols: both still latched), save and restore, **I-3** for gate counters, admin latch, condition language, unknown machine state fails at load, Monitor rows. GameTest `domeStableUnlocksTheOutgasser` (6 checks).

## 2. What actually ran
| Check | Command | Result |
|---|---|---|
| Format, build, unit tests | `./gradlew spotlessCheck build test --rerun --offline` | BUILD SUCCESSFUL; 93 tests, 0 failures (79 before) |
| Boot and GameTests | `./gradlew runGameTestServer --offline` (three repeat runs) | `RESULT PASS passed=52 failed=0` (46 before) |
| Prove it can fail (unit) | removed the "latched gates are skipped" guard in `GateTracker.step` | `domeStable...` and `i2_latchedGatesNeverUnlatch...` FAILED (93 tests, 2 failed); restored |
| Prove it can fail (GameTest) | set the lock lookup in `PlanetManager.placementAllowed` to null | `FAIL gate.survival_refused_before ... canPlace=true`, `RESULT FAIL passed=51 failed=1`; restored |
| Screenshots | `tools/screenshots/run-shots.sh /home/user/mars/tools/screenshots/slice4_gates.shots` (no client running; run twice after a text fix) | `tools/screenshots/out/20261127-113824/`. Looked at: Monitor at sol 1.0 shows "Next goal: Dome stable, Life support, no failures, 1.0 / 3 sols"; after 3.5 sols "Goals reached 1 / 2, Dome stable" and "Next goal: Phase 2 begins, Air pressure 0.60 / 5 kPa"; at 5.2 kPa "Goals reached 2 / 2". The first run showed the long progress label colliding with its number; I shortened the text and re-shot |

## 3. Problems found on the way
- The GameTest world keeps its saved planet between runs. My gate test passed once, then failed on the second run because Dome stable was already latched (it cannot be undone). Fixed with `resetForSelfTest` (refuses to run without the test seam) and checked with repeat runs. The existing persistence test advanced 3 sols with a panel and would have latched the gate on the shared planet, so it now advances 2.
- Chat text in the screenshots is still faint behind the Monitor's left edge (known cosmetic).

## 4. Risk gate
| Change | Could break | Check | Result |
|---|---|---|---|
| Placement refused in `getStateForPlacement` | Client shows a ghost block, server refuses | Server returns null before anything registers; client prediction not exercised | Server side tested (GameTest); the client ghost flicker is **not tested** |
| Gates evaluated after every step | Cost, or double latching | One evaluation per unlatched gate; `drainNewlyLatched` | Unit tested; not profiled |
| Save schema 3 | Old saves | Schema 1 and 2 load with gates unlatched (5- and 7-argument constructors) | Round trip in unit and GameTest; no real disk restart (EULA) |
| Creative bypass | A classroom with creative on skips the gate | Documented; creative is a teacher decision | Open (see decisions) |

## 5. Decisions needed
| Decision | Recommendation | Type |
|---|---|---|
| Creative players bypass the lock. Keep? | Keep for the prototype; a classroom switch is a Stage 3 config item | Ill-structured (teacher control against owner testing) |
| Dome stable is a stand-in (power covers use for 3 sols, about 1 h, not about 3 h) | Keep until the oxygen maker and sealing exist; do not tune timing now | Well-structured |
| Hold rule: a short failure pauses the count, a long one (over 1 sol) resets it (balance sheet 4) versus "a failure empties the boxes" (ui-specs 3, style guide) | Follow the balance sheet; Game Designer picks one wording and rule | Ill-structured |
| `/mars index set pressure 5.2` latches Phase 2 with cause REACHED, not ADMIN | Accept for a debug tool; mark gates as ADMIN when `set` was used later (learning layer) | Well-structured |

## 6. Handoffs
> **To:** Lead Game Designer · **Problem type:** ill-structured
> **Problem:** pick the Dome stable failure rule (pause then reset after 1 sol, or reset at once) and review the straw-man wording in `docs/design/ui-specs.md` section 9 ("Next goal", "Goals reached", "Life support, no failures").
> **Context:** `docs/design/balance-sheet.md` section 4, `docs/design/ui-specs.md` section 3, GateTracker hold rule.
> **Your view:** the balance sheet rule is kinder and matches the Phase 3 holds; the reset rule is clearer to a 12-year-old.
> **Needed from them:** one rule for all holds. **Done when:** the sheet and ui-specs agree and the test `aShortFailurePausesTheCountAndALongOneResetsIt` matches.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** the Dome stable journal entry and its "On Earth" line (A1, D2) have no trigger yet; the gate now announces itself with a chat line only. **Done when:** the entry text is final and a later slice wires it to the gate id `mars:dome_stable`.

## 7. Risks
- Pressure is still not on the balance-sheet curve: Dome stable arrives after about 1 h, the Oven does not exist, and no frost sink yet (slice 5).
- No recipes, so "unlocked" is a placement check only; a player can still take the block from the creative tab if creative is on.
- Not tested: real-client placement refusal (the screenshots use commands), right-click on the Monitor, a dedicated-server restart.
- Gate text is straw-man English only.

## 8. Shared model updates
- **Decision (ARCHITECTURE.md 2.19, data in `pack/data/mars/gates/`):** gates are data; a gate latches once and is never evaluated again; holds count simulation sols; unlocks are enforced at placement for survival players, with a creative and admin route.
- **Lesson:** a GameTest world persists its saved data between runs, so any test about a one-way state (latched gates) needs its own fresh state and must be run twice to prove it repeats.
- **Term:** "stand-in machine state": a named world check that a later system replaces without changing the gate files.
