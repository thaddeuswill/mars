# Private learning identity persistence

## Handoffs received

Stage 3 slice 3, the owner’s acceptance of session rotation, and the in-memory world identity component.

## Done

The identity store keeps a random pseudonym stable across opens. It persists a world-private HMAC-SHA-256 lookup token per account and holds the world key and issued-ID list in the same atomically replaced file. The token is a lookup key. Assigned pseudonyms are random and remain reserved after forgetting. The file contains no raw UUIDs or usernames. On POSIX filesystems the file is created with owner-only permissions.

The Gradle build and 216 unit tests passed. The first headless run reused a prior test world and failed the Monitor cause-count assertion at 1.55 kPa, where frost added a second cause. A fresh GameTest world passed all 95 boot checks. A scratch mutation that stored raw UUID keys failed the privacy assertion as intended.

## Decisions needed

None for this component. To preserve the Stage 3 no-raw-UUID requirement, the identity file uses keyed lookup tokens. This replaces the raw-key example in ARCHITECTURE.md §8.2.

## Handoffs

Create one identity component per world outside the game world’s backup directory. Gameplay adapters and controlled candidate-profile lookup are still pending. There is no runtime gameplay or screen change.

## Risks

| Change | Could break | Check | Result |
|---|---|---|---|
| Cross-restart identity | A returning account gets a new pseudonym | Reopen the same file and identify two accounts | Passed |
| Forget | Historical evidence gets a different owner | Forget, reopen, and verify old identifiers remain reserved | Passed |
| Storage privacy | A raw account ID reaches disk | Inspect the saved file and repeat with a raw-key mutation | Passed; mutant rejected |
| Invalid store | A broken file is overwritten | Open malformed JSON and verify it remains intact | Passed |

## Shared model updates

The store is pseudonymous, and its private key remains sensitive. Logs and CSV never read the identity file.
