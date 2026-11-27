# Learning session ownership and in-memory identities

## Handoffs received

The typed event writer and the Stage 3 learning file claim.

## Done

Learning sessions now own and rotate the writer, using `mars-data/<world>/actionlog/<session>.jsonl`. The existing writer drains before the new file opens and rejects an event for the wrong world or session. A final write failure is returned on every close attempt so rotation cannot silently discard it.

The world-local identity component assigns random pseudonyms, reuses the same ID for a returning account in that component, and reserves previously issued IDs when the mapping is forgotten. Account UUIDs stay within the in-memory identity component.

Java 21, formatting, and the full Gradle build passed with 183 unit tests. GameTests reported `RESULT PASS passed=82 failed=0`. A scratch change that removed the session-number guard caused the rotation test to fail. I restored the intended source and reran the focused learning tests successfully.

## Decisions needed

Resolve cross-restart identity storage before enabling live gameplay logs. The Stage 3 plan says UUIDs are never written. ARCHITECTURE.md §8.2 describes a private mapping file with raw UUID keys. This patch writes neither.

## Handoffs

Gameplay adapters, cross-restart identity, CSV export, and guidance remain open. Session rotation uses a stable session filename so reopening a session does not need a date to find its file. Each event carries its own timestamp. This patch has no player-visible screen change.

## Risks

| Change | Could break | Check | Result |
|---|---|---|---|
| Rotation | Events enter the wrong session file | Rotate, inspect both files, reject late events, and run a guard-removal mutation | Passed |
| Write failure | A second rotate skips the error | Force a bad path and retry rotation | Passed |
| Identity deletion | An old ID gets assigned to another record | Force a random collision after forgetting | Passed |

## Shared model updates

Cross-restart identity is required before the log can support longitudinal playtest comparisons.
