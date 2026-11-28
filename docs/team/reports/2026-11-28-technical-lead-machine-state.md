# Technical Lead: machine state (off / idle / running) and battery charge (2026-11-28, BACKLOG #49)

## Handoffs received
From the Lead Artist (`docs/team/reports/2026-11-28-artist-running-states.md`): wire `machine_state` and battery `charge`; update only on change; blockstates to the prepared models; a test that each state maps to an existing model. Accepted. Decision asked of me: battery thresholds. Chosen: quarters, rounded down, so charge 3 = 75 percent or more (a full bar is not reserved for 100 percent).

## Done
- `mars.power.MachineLook` (pure): `of(PowerGrid.State)` and `batteryBars(charge, capacity)`.
- Exact mapping (read from `MonitorView`: UNPOWERED "needs power", SHORT "low power", otherwise "running"):

| Grid state | Monitor word | `machine_state` |
|---|---|---|
| UNPOWERED (demand, no supply, empty battery) | needs power | off |
| SHORT (some power, less than demand) | low power | idle |
| POWERED, or no demand | running | running |
| machine placed off a planet (not registered) | n/a | off (set in `onPlace`) |

  Running means the grid meets demand; it does not check that the machine "contributes" (the Monitor never did: the reclaimer is "running" with no Index effect). "Idle because nothing to do" is not modelled (no machine has such a state yet).
- Single source of truth: `MonitorView.powerReason` (3 call sites: cause lines, starved alert, Power row) now calls `MachineLook`; `PowerReasonTest` checks every grid state.
- Blocks: `PoweredMachineBlock` (property `machine_state`, default idle) is the parent of Oven, Outgasser, reclaimer, `GatedLoadBlock` (processor, emitter) and the oxygen maker; `BatteryBlock` gets `charge` 0..3 (default 0). Oxygen maker has no machine file or draw, so it is never updated and stays idle (its old look); it will join when it gets a draw.
- Update path: `mars.planets.MachineStateSync.sync` on each planet step (every 100 ticks), after place/remove, and after `/mars index advance`. Block written only if its value differs (flag UPDATE_CLIENTS). Cost: one pass over placed machines (tens), a compare each; no per-tick work beyond the existing planet tick. Unloaded chunks are skipped and fixed by the next step after they load.
- Blockstates for the seven machines: `facing,machine_state` and `charge,facing`, same y rotations, Artist's models. Item models, registry, loot, recipes untouched.
- Old saves: a block without the properties loads with the default (idle, charge 0) and the next sync corrects it (boot check reads real NBT without the properties).
- `tools/screenshots/machine_state.shots`.

## Real output
`cd mod && ./gradlew spotlessApply build runGameTestServer --offline`: BUILD SUCCESSFUL, 355 unit tests, 0 failures (baseline 348: +4 `MachineLookTest`, +1 `PowerReasonTest`, +2 in `MachineAssetsTest`); `MARS-SELFTEST RESULT PASS passed=152 failed=0` (baseline 142: +10 `machine_state.*` in `MachineStateGameTests`: unpowered off, powered running, short idle, no rewrite when unchanged, back to off, property values, facing kept, old save loads, old save synced, battery bars 0/1/2/3 for 0, 2.5, 5, 10 of 10). `LangKeysTest` and `LangWordingTest` green (no new text).

## What I saw (real client, software GL, `tools/screenshots/out/20261128-113214/`, source PNG times 11:32:58 to 11:33:32, `grep -c "Unknown block" client.log` = 0)
- `state_off_*`: reclaimer and processor with the lamp dark; `state_running_*`: the same blocks with a lit blue lamp (and water in the reclaimer window).
- Battery: `state_battery_charge0` no bar lit; `charge2` two bars lit; `charge3` three bars lit (a pixel diff and an 4x crop confirmed 2 versus 3; at normal size they look alike).
- Not visible in stills: the animation (oven coil, puff, rising water, pulse). I cannot say how it looks moving, whether the Outgasser puff overhang renders, or whether the reclaimer reset reads as a glitch.

## Not verified
- The animation in motion; the Outgasser puff cutout and its 8 px overhang; Oven and Outgasser looks (no shot taken).
- The "off a planet" `onPlace` branch: the boot server treats every level as a planet.
- Unloaded-chunk correction: reasoned, not run.
- Dedicated server, a real old-world save file (only NBT without the properties was loaded).

## Decisions needed
1. Should the oxygen maker get a power draw so it can show running? Ill-structured (needs Game Designer on what it does). Recommend yes, with the Stage 3 oxygen row; until then it stays idle.
2. Battery: all batteries show the same level because the store is shared. Recommend keep (it is true to the model).

## Handoffs
> **To:** Lead Artist · **Problem type:** well-structured
> **Needed:** look at the stills and, if you want the animation judged, run the next shot with waits between two shots of the Oven (8+ ticks apart). Charge 2 and 3 differ by one bar only: check readability at distance.
> **Done when:** timings retuned or accepted.

> **To:** Lead Game Designer · **Problem type:** ill-structured (what "idle: nothing to do" should mean, e.g. Outgasser at a pressure cap, oxygen maker with no CO2)
> **Needed:** a rule; the mapping is one function (`MachineLook`) so it is a small change.

## Risks
- A block write per machine per state change; a grid that flickers between SHORT and POWERED each step would rewrite blocks every 5 s (bounded, but visible as lamp flicker). The planet step is deterministic so it should be steady.
- State is up to one step (5 s) stale after a chunk loads or a panel dust change.

## Shared model updates
- Term: **machine look** (`MachineLook`): off, idle, running from the grid state; the Monitor word and the block property come from it.
- Lesson: when art needs a state, put the state function in pure code first and make the Monitor and the block read it.
