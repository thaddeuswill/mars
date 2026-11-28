# Completed-session learning event CSV

## Handoffs received

The seal-breach hook and Stage 3 slice 3 CSV scope. Blackout and life-support event publishers are still pending.

## Done

The operator command `/mars learning export` writes a UTF-8 events.csv for completed sessions in the current world. The export reads only versioned JSONL events under actionlog, in session order. It validates pseudonyms, preserves player and planet scope, leaves the player ID blank on planet rows, and ignores unrelated data keys. The private identity store is outside the export path. The current session stays out of the export to avoid reading a live file.

Formatting and build passed, with 257 unit tests and 114 boot checks. Tests wrote actual JSONL through ActionLog, exported two event scopes, rejected an invalid player field without replacing an existing CSV, checked spreadsheet escaping, and checked session selection and order. GameTest confirmed command registration and operator permission. The shell and Minecraft client were not used to exercise the asynchronous command response.

## Decisions needed

None.

## Handoffs

The export currently covers the event kinds in ActionEvent. Forecast and summary columns remain incomplete until those systems emit the source data. The command is documented in the launch guide.

## Risks

| Change | Could break | Check | Result |
|---|---|---|---|
| CSV projection | Expose an account name or lose actor scope | Read writer-generated player and planet rows and reject a changed player field | Passed |
| Session selection | Read current session or reorder older sessions | Export sessions 2 and 7 while 8 is active | Passed |
| Operator registration | Expose the command to players | Check Brigadier permission with levels 0 and 2 | Passed |

## Shared model updates

The exporter can use completed sessions while the active writer continues recording the current one.
