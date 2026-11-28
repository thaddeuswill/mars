# Landing and gate learning adapters

## Handoffs received

Slice 4 publishes Landing and journal events. The planet manager publishes gate latches. Codex owns subscribers and checks inside the learning lane.

## Done

Landing steps, skip and completion, briefing opens, and journal opens now write player-scoped evidence with world-local pseudonyms. An assisted Landing step keeps its suffix and stays a Landing event. Gate latches write planet-scoped evidence, and operator latches retain an administrative cause. The event CSV projects the Landing, briefing, and journal content IDs into dedicated columns.

Formatting and build passed. 295 unit tests and 135 boot checks passed on a fresh GameTest world. The learning check posted five player events across two fake players and one administrative gate event. It inspected all six records for order, attribution and content, and checked that the two usernames and raw UUIDs were absent. The CSV test verified a writer-generated Landing detail reaches the exported row.

## Decisions needed

None.

## Handoffs

Blackout and life-support transition publishers are still pending. Monitor and placement hooks and saved grade-band selection remain open. Band is currently MS in the runtime adapter. This patch changes no screen.

## Risks

| Change | Could break | Check | Result |
|---|---|---|---|
| Player attribution | Mix two learners or expose a raw account identifier | Post events for two fake players and inspect their rows | Passed |
| Gate attribution | Give individual credit for an operator latch | Check null actor, planet scope, and admin cause | Passed |
| CSV | Lose the assisted-step marker | Export a writer-generated Landing step | Passed |

## Shared model updates

Gameplay publishers stay independent of the learning writer. The adapter receives account identity in memory and writes the pseudonym only.
