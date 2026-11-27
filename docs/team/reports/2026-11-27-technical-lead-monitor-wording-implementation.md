# Approved Monitor and starting inventory wording

## Handoffs received

The Instructional Designer’s approved wording in the UI spec §9.2.

## Done

Updated six English language keys. The starting Oven grant says “inventory” and directs players to Power. The frost note uses “Leveling off,” and the goal estimate uses words for approximate and under-one-sol readings. All existing formatting arguments were preserved.

Formatting, the Gradle build, 176 unit tests and 82 boot checks passed. I inspected fresh 1280×720 captures of the starting grant, the two-line Leveling off note, and the goal panel at Sol 5.5 with “about 44 sols.”

## Decisions needed

None for this copy update.

## Handoffs

The high-school frost wording is present in the language file. Its read-more screen and forecast-aware estimate visibility are separate tasks.

## Risks

| Change | Could break | Check | Result |
|---|---|---|---|
| English copy | Missing formatting arguments | Parse and compare JSON keys and placeholder counts | Passed |
| Longer estimates | Clip or crowd the goal percentage | Inspect the fresh early-estimate image | Passed for the captured English state |

## Shared model updates

The Oven recipe remains part of the planned recipe integration. The starting grant still supplies two Ovens.
