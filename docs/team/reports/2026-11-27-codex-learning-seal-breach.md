# Seal-breach learning handoff and life-support data check

## Handoffs received

- Technical Lead, Stage 3 slice 1. Subscribe to `SealChangedEvent` from the learning package.
- Game Designer, CX-C. Verify the 2d life-support data check rejects a changed balance value.

## Done

The first live learning hook turns a sealed-to-unsealed transition into a planet-scoped `failure.reported` entry with `player: null`. Initial sealing and repairs do not emit this breach entry. The server owns the per-world log, creates an opaque ID in world save data, rotates by session, and drains on shutdown. The data file contains a planet ordinal rather than a block position.

Formatting and the build passed, with 248 unit tests and 108 clean-world boot checks. The GameTest posted an initial seal and a breach on the NeoForge bus, drained the writer and read exactly one new JSON line with the expected scope and type.

The Technical Lead already added the CX-C guards in `LifeSupportTest` and `RecipeCostsTest`. Both classes passed against shipped data. In a scratch copy, changing `o2UsePerPlayerSol` from 8 to 9 caused `lifeSupportJsonMatchesTheBalanceSheet` to fail with expected 8.0, actual 9.0. The shared data was untouched.

## Decisions needed

None.

## Handoffs

Placement, Monitor and gate adapters are next. The current planet event uses the default middle-school band and timeline 0 until those runtime settings are wired. There is no screen change.

## Risks

| Change | Could break | Check | Result |
|---|---|---|---|
| Seal listener | Record ordinary initial sealing as a breach | Post a first seal and a breach, inspect count and JSON | Passed |
| Event attribution | Assign a team event to an individual | Check null player and planet scope on disk | Passed |
| Balance data | Live numbers drift from the approved sheet | Mutate a scratch pack and rerun the focused tests | Passed; mutation rejected |

## Shared model updates

The learning subscriber consumes a server event. Dome code remains independent of the learning writer.
