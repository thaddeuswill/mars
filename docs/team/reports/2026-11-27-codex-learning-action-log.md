# Stage 3 learning action log foundation

## Handoffs received

Stage 3 slice 3 file claim and ARCHITECTURE.md §8. The owner accepted the preceding copy and asked to continue.

## Done

Added typed player and planet events and a session-local JSON Lines writer under `mars/learning/`. Writes batch on a background thread after five seconds or 200 events, and close drains remaining events. The writer accepts project content IDs and pseudonyms and has no dependency on a raw account UUID. A planet event carries a null player field.

On Linux, Java 21 and Gradle 9.2.1, formatting and build passed, 180 unit tests passed, and the headless runner printed `RESULT PASS passed=82 failed=0`. The focused tests covered ordering across a batch boundary, append across reopen, scope, invalid IDs, and an unwritable path. A scratch mutation that gave a planet event a player ID failed the scope test. The intended source was restored and all four focused tests passed again.

## Decisions needed

The planning row says UUIDs are never written, while ARCHITECTURE.md §8.2 specifies one UUID-to-pseudonym mapping file. The event format and writer have no dependency on that choice. Reconcile the identity lifecycle before persisting a mapping.

## Handoffs

Next are world-local identity lifecycle, session ownership, and the gameplay event adapters. The writer is tested directly and is not active in the game yet. CSV, guidance, and interruption settings remain open. There is no screen change in this patch.

## Risks

| Change | Could break | Check | Result |
|---|---|---|---|
| Background batching | Reorder or lose accepted events | Write 206 events across batch and reopen | Passed |
| Actor scope | Attribute team evidence to a player | Null actor assertion and seeded mutation | Passed; mutant rejected |
| Storage errors | Hide a failed final write | Attempt to write beneath a regular file | Passed |
| Privacy boundary | Expose account UUID to the writer | ArchUnit dependency check and ID validation | Passed |

## Shared model updates

The event cause is independent of actor scope, so an operator-triggered planet event can be recorded as administrative without assigning it to a learner.
