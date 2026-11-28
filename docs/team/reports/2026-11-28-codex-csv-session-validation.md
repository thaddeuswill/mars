# Learning CSV session validation

## Handoffs received

The operator export promises completed sessions. The owner asked Codex to independently find and fix a repository issue.

## Done

The CSV exporter now checks each row’s session against the session named by its source file. A mismatch fails the export and leaves a prior CSV intact. The normal command still exports completed sessions in session order.

The new regression inserted a session 8 record into the session 7 file while session 8 was active. It failed against the old code because the export accepted the row. With the fix, both the operator projection and direct export reject the file. No CSV is published for the invalid operator export.

Formatting and build passed with 311 unit tests. The clean-world headless runner printed `RESULT PASS passed=143 failed=0`.

## Decisions needed

None.

## Handoffs

This is an export validation change. There is no player-visible layout change.

## Risks

Valid session logs must still export in their original order. The existing completed-session and writer-to-CSV tests passed.

## Shared model updates

The per-file session and each row’s recorded session must agree before any CSV is published.
