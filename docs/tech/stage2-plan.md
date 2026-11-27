# Stage 2 Build Plan: first playable (planet values, Planetary Monitor, one pressure machine)

**Owner:** Technical Lead · **Written:** 2026-11-27, before any Stage 2 code · **Stage:** 2 Prototype (ROADMAP.md) · **Follows:** PROCESS.md §1 (plan, risk gate, build, verify, report, retro)
**Related:** ARCHITECTURE.md §2.1 (Index simulation), §2.9 (Monitor), §2.16–2.18 (machines), §7 (pause), [test-strategy.md](test-strategy.md), balance sheet §2 (`docs/design/balance-sheet.md`)

**Status of this file:** the plan. Slice 1 is built the same day; its results are in `docs/team/reports/2026-11-27-technical-lead-stage2-slice1.md`. Later slices are plan only.

**Changes made while building slice 1 (recorded, not hidden):** the freeze test had to call the tick hook by hand (freezing the whole test server stops the GameTest runner); a test-harness hole was found and closed (a failed server start exited 0; now exit 6, see ARCHITECTURE.md 2.19); the Monitor's long water phrase overflowed its panel in the first screenshot and now shrinks to fit. Risk rows R1 to R10 are answered in the report.

## 1. What Stage 2 must prove (from ROADMAP.md)

The user can raise pressure, see the sky change, feel pressure to maintain it, and say why each change happened. The prototype may be thrown away; only the simulation core, its tests, and the session clock are meant to survive. Timebox in ROADMAP: about 300 team hours for the whole stage (Producer's budget 311), two build-and-play iterations at most.

**What this plan covers:** the Technical Lead's code items. Paper prototypes (Stage 2a, Game Designer) and face-validity sketches come first in the roadmap and are not mine; code here follows the roadmap's rule "code only what the paper version couldn't answer". Slice 1 touches nothing the paper prototype could decide (it is plumbing: stored values, a machine contribution, a display).

## 2. Slices, in order

Each slice ends with a run of the checks in §4 and a report. "Dev-days" are for one experienced developer, **unverified** (no measured rate yet; the first real number comes from slice 1, see report).

| # | Slice | Contents | Verified how (headless unless said) | Estimate (unverified) |
|---|---|---|---|---|
| **1** | **Values, Outgasser, Monitor** (built 2026-11-27) | Pure planet values (pressure first; every parameter file loads); saved with the world; Regolith Outgasser contributes +0.10 kPa/sol per balance sheet §2; Planetary Monitor block opens a screen with value, trend, and cause line; `/mars index` commands to read, advance, and set values | Unit tests, GameTest boot server, one real-client screenshot run (looked at) | 4 dev-days |
| 2 (built 2026-11-27, see report `2026-11-27-technical-lead-slice2.md`; third-party machines half still open) | **Session clock and pause** | Pause flag in `session.json`; steps stop while paused or `/tick freeze`d; I-3 test; resume order (§7.2); **GameTest that pause stops third-party machines** needs Mekanism and Create on the test classpath, so it is its own task | Unit (I-3), GameTest with a fake machine, then with Mekanism/Create | 3 dev-days |
| 3 | **Power for the Outgasser** | Minimal solar panel + battery supply (one number: kW available vs. demand) so "Medium (solar or battery)" is true; Outgasser rate scales with power satisfied | Unit (supply vs. demand), GameTest | 3 dev-days |
| 4 | **Gate and unlock** | "Dome stable" as a latched gate (machine-state condition, 3 sols); Outgasser recipe/placement unlocked by it; "Phase 2 begins" gate at 5 kPa; I-1 and I-2 tests | Unit (condition language, latching, I-1, I-2), GameTest | 4 dev-days |
| 5 | **One regression source** | Polar-frost sink coupling (balance sheet §2: −2 % of (pressure − 0.6) per sol below −40 °C) so pressure needs upkeep; the Monitor shows it as a negative cause line | Unit (coupling vs. SME curve table), threshold-vs-curve test | 2 dev-days |
| 6 | **One visible world response** | Sky/fog tint keyed on pressure through the keyframe data (`docs/art/planetary-keyframes.md`); client interpolation | Screenshot run at sol 0, mid, 5 kPa (looked at); unit test for the interpolation | 4 dev-days |
| 7 | **Next-gate bar and Monitor polish** | Next-gate display (ARCHITECTURE §2.9); trend from the last steps; hold nothing yet | Unit, screenshot at 1280×720 | 2 dev-days |
| 8 | **Admin and rollback decision** | Full debug command set; choose the rollback approach (start script default) | GameTest on commands | 2 dev-days |
| 9 | **Pack config and classroom pacing** | `pace.*` and `group.size` as NeoForge server config; classroom preset | Unit (Pace) plus GameTest reading the config | 2 dev-days |

Total code estimate about 26 dev-days (about 208 h) of the 311 team hours; the rest is art, design, QA, producer. **Unverified**; re-estimate after slices 1–3 from the measured rate.

Order reasoning: slice 1 is the thinnest line from "a number in a save file" to "a player sees why it moved", and every later slice hangs off it. Slice 2 comes before power because I-3 and the pause guarantee are the Stage 2 items most likely to need a rework if left late (R-10, R-11).

## 3. Slice 1 design (what is built)

**Pure layer (no Minecraft; ArchUnit-enforced):**
- `PlanetaryIndexModel` gains `causes(parameter)` (effective rate per sol by source, after pace and group scale), `netRatePerSol`, and `restore` (values and sim ticks from a save).
- `MachineDef` + loader: a machine type is data (`data/mars/machines/regolith_outgasser.json`: parameter, `ratePerSol`, tag `balance-sheet §2`). A test reads balance sheet §2 and fails if the file's number differs (the content-test habit from §2.17).
- `PlanetSim`: the model plus a tick counter and machine registry (position → contribution handle). `onServerTick()` does one increment and compare per tick and one `step()` every 100 ticks (**cost per tick: zero beyond that; per step: one add per contribution**).
- `PlanetState`: the saved form (schema version, values, sim ticks, pending ticks, machine positions by type). A state with a newer schema than the code is refused, never overwritten.
- `MonitorView`: rows for pressure, temperature, and "Water boils here at X °C" (ui-specs §2, Phase 1), each with value text, trend word, and cause lines; one number-formatting helper (thousands separators, true minus, units).

**Minecraft layer:**
- `PlanetData extends SavedData`, one per Mars dimension (`mars:planet_*` only). Saved in that dimension's own data folder.
- Reload listener for `data/mars/parameters/` and `data/mars/machines/` (so definitions are data, reloadable, and overridable by a datapack).
- `LevelTickEvent.Post` calls `onServerTick()` for planet levels, guarded by the vanilla tick-rate manager (frozen = no steps).
- `RegolithOutgasserBlock`: `onPlace` / `onRemove` register and unregister its position (no block entity, no ticking). Position list is saved, so contributions survive restarts and chunk unloads.
- `PlanetaryMonitorBlock`: right click sends a snapshot payload (server authoritative; the client holds no state) and opens `MonitorScreen`. Straw-man look: dark backing, big value, small label, default font (the pixel fonts are Stage 3).
- `/mars index` (op): `get`, `advance <sols>`, `set <parameter> <value>`. These are debug tools; any use is logged to the server log with the actor and counts as `Cause.ADMIN` once the learning layer exists.

**Left out on purpose (named, not hidden):** power (the Outgasser runs unpowered, which the balance sheet says it must not; slice 3), the "Dome stable" unlock (slice 4; in slice 1 the block is in the creative tab), the next-gate bar, regression sources, sky response, pause flag beyond the tick-rate guard, config for pace (slice 1 uses `Pace.STANDARD`), the learning log (no events are written), sealing and oxygen.

## 4. Test and check plan for slice 1

| Layer (test-strategy) | Check | What it reads |
|---|---|---|
| L0 parse | `./gradlew build` (compile, Spotless, content-file test) | exit code |
| L1 unit | Outgasser rate from data equals balance sheet §2 (0.10) | file value vs. sheet |
| L1 | 1 Outgasser × 10 sols = +1.000 kPa; 2 Outgassers = +2.000; removing one halves the rate; classroom ×4 and group 4 scale rates only | pressure printed with expected |
| L1 | Save and restore: values, sim ticks, pending ticks, machine positions identical; a newer schema is refused; an unknown parameter in a save is ignored with a warning, a new one takes its start value | printed |
| L1 | Cause lines: two Outgassers read "×2, +0.20 kPa/sol"; Monitor text for a known snapshot | printed strings |
| L1 | ArchUnit: new pure packages have no Minecraft or NeoForge dependency | rule |
| L3 GameTest boot | Parameters and machine data were loaded by the real reload path; two real Outgasser blocks placed in a level raise pressure by the exact expected amount after real ticks; breaking one lowers the rate; `PlanetData` NBT round trip; the Monitor payload encodes and decodes through a real network buffer; Outgasser outside a planet level does nothing (and says so) | printed expected and read |
| L5 screenshot | One real-client run (Xvfb, software GL, 1280×720): Monitor screen after `/mars index advance 10` with two Outgassers placed. A person looks at it | PNG |
| Prove-it-can-fail | Run the Outgasser-rate test against a broken data copy (`-PmarsPackDir`), as in Stage 1 | failing output |

**Not covered by slice 1 (will not be claimed):** the Mars dimension itself in the GameTest server (it builds its own world, Stage 1 report), the dedicated server (EULA not accepted by the owner), Windows/macOS, persistence across a real server restart (the NBT round trip stands in for it), vanilla `/tick freeze` stopping the sim (unless the GameTest exercises it; the report says which).

**Test seam, stated plainly:** the GameTest server has no Mars level, so a system property `mars.selftest.anyLevelIsPlanet=true`, set only by the `gameTestServer` run configuration, lets the GameTest world act as a planet. It is read in one function (`PlanetManager.isPlanet`) and is off in every normal run.

## 5. Risk gate (PROCESS.md §1 step 2)

| # | Change | Could break | Check that catches it | Result (slice 1) |
|---|---|---|---|---|
| R1 | Reload-listener data loading | Parameters loaded after the first level tick, or never in some launch path; a missing file silently falls back to code defaults | GameTest asserts definitions are present and read from the files; `PlanetData` throws with a clear message if asked before load | see report |
| R2 | Saved planet values | A save from a later build overwritten; values lost on restart; machines double-counted after load | Newer-schema refusal test; round-trip test comparing values and rate; machine set is positions, so reload cannot duplicate | see report |
| R3 | Contributions stored by position, not by block entity | A machine removed by an explosion, piston, or `/setblock` keeps contributing forever (a phantom machine) | `onRemove` covers all of those paths in vanilla (`onRemove` runs on any replacement); GameTest breaks and replaces; **chunk unload deliberately keeps the contribution** (planet-scale simulation does not depend on loaded chunks), noted as a design call for the Game Designer | see report |
| R4 | Tick hook | Per-tick cost on every level; sim runs in a frozen or non-planet level | Hook returns at once for non-planet levels (one namespace compare); frozen guard; cost stated as one increment per tick | see report |
| R5 | Monitor payload and screen | Client shows a stale or invented value; server crash on a dedicated server if client classes load; a screen that cannot be read at 1280×720 | Server builds every number; payload round-trip test; client classes only in `mars.client` (ArchUnit rule exists); screenshot looked at | see report |
| R6 | Pure packages grow | Minecraft types leak into the core | ArchUnit rule extended to the new packages | see report |
| R7 | Test seam property | A test-only switch lets overworld act as a planet in real play | Property only set in `gameTestServer`; a unit-level check that the run configuration is the only place it appears (grep in the report) | see report |
| R8 | Debug commands | A student runs `/mars index set` and breaks the planet | Permission level 2 (ops only; singleplayer cheats off by default); logged to the server log | see report |
| R9 | Outgasser unpowered and un-gated in slice 1 | The owner mistakes slice 1 pressure speed for the balance-sheet curve | Named in the report and the Monitor is straw-man; slices 3 and 4 close it | stated |
| R10 | Antoine boiling point displayed below the triple point | NaN text on screen at 0.6 kPa (below 0.61 kPa the table says the value is undefined) | Unit test: Monitor line for 0.60 kPa reads the "no liquid water" wording, never "NaN" | see report |

## 6. Screenshots (what they will show)

Slice 1 produces `tools/screenshots/out/<timestamp>/` PNGs at 1280×720 (GUI scale 3):
1. **Monitor, start:** pressure 0.60 kPa, temperature −60 °C, "Water boils here: not liquid below 0.61 kPa" (wording is a straw-man; text belongs to the Game Designer), trend "steady", no cause lines.
2. **Monitor, two Outgassers after 10 sols:** pressure about 2.60 kPa, trend "rising +0.20 kPa/sol", cause line "Regolith Outgasser ×2 +0.20 kPa/sol".
3. If the run allows: the two Outgassers beside the Monitor block in the world.
A screenshot is a check on layout and readability only. It does not show that the numbers are right; the unit tests and GameTest do.

## 7. Ways this plan could be wrong

- The Mars level itself is not exercised by any automated check here; slice 1 relies on the namespace rule plus the test seam. The dedicated-server self-test (needs the owner's EULA) would close that.
- `SavedData` API names for 1.21.1 are taken from the decompiled sources in the Gradle cache, not from memory; compile errors would show any slip.
- The estimates are guesses. Slice 1's actual effort is the first data point.
