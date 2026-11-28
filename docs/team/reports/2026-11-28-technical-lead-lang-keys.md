# Technical Lead report: lang keys, layout fit, made-up band journal entry (2026-11-28)

## 0. Handoffs received
- Owner: lang-only edit of `pack/assets/mars/lang/en_us.json` approved. Accepted. Two small code changes were also needed and are inside the stated tasks: the oxygen maker item had no tooltip hook (`MarsMachines`), and the goal-row detail line could run under the percent (`MonitorScreen`, `MonitorLayout`).
- Artist: oxygen maker tooltip and the journal line for the purple dashes. Accepted (tooltip done; journal entry done at Dome stable, see gaps).

## 1. Done (run from `mod/`)
- `pack/assets/mars/lang/en_us.json`: added `gate.mars.thaw|phase_3_begins|breathable|equilibrium`; `monitor.gate.thaw.progress`, `phase_3_begins.progress`, `breathable.progress|waiting`, `equilibrium.progress|waiting`; `monitor.cause.temperature_leak` "Warming fades without emitters."; `block.mars.oxygen_maker.tooltip` ("Game-sized version of a real idea." plus one MOXIE line; MOXIE is VERIFIED in `docs/science/machine-references.md`); `journal.band.made_up.title|.ms`. The new detail texts are shorter than the report's drafts so they fit beside "Goals 3 / 6" and the percent (for example "Air and temperature", not "Air pressure, then temperature"). Changed `monitor.gate.dome_stable.waiting` from 44 characters to "Not steady. Count waits." (same overflow problem, same pattern as the new lines).
- `MarsMachines.java`: oxygen maker item now uses the existing `tipped(...)` tooltip hook.
- `LangKeysTest` (new, 7 tests): scans string literals in `src/main/java` (not `selftest/`), every string in `pack/data/mars/**.json` that looks like a key, journal `text_key` bodies (`.ms`), gate names and progress/waiting lines (waiting only if the gate holds), machine names and placed markers, every blockstate and item model name, and the four machine tooltips. Checked red then green: with the old lang file it listed the 10 missing gate keys, the leak key, the tooltip and the band keys. No other key was missing.
- `LangWordingTest` (+3): CR-T1 banned words, CR-T5 blame phrases, labels (`gate.mars.*`, `monitor.row.*`) at most 24 characters, gate detail lines at most 28 characters. The existing CR-T7 tilde/less-than/US-spelling scan stays green.
- Layout fix: `MonitorLayout.fitDetail` (pure). The goal row's left text is "detail . Goals n / m" if it fits left of the percent; else the goals count goes; else the detail is cut with an ellipsis. `MonitorScreen` uses it. Before: the raw key plus the count ran under "87%".
- `MonitorLayoutTest` (+5): every `monitor.gate.*` text at 7, 87 and 100 percent never reaches the percent; the six new texts keep the goals count at 100 percent; an overlong text drops the count then ellipsizes; the temperature leak line and the longest machine cause lines (processor, emitter with a reason) fit 250 px. Text width in tests is an estimate of the default font (narrow glyph table, 6 px otherwise), not the real font.
- Journal: `pack/data/mars/story/journal/band_made_up.json` (unlock Dome stable, goals D2 and A1, no science claim). Text from `docs/design/messages/phase-1.md` section 12. `LearningTest` and two boot checks (`journal.latch_pointer_and_list`, `journal.late_joiner_once`) now expect 4 entries.
- Real client screenshots (script `tools/screenshots/phase2_thaw_goal.shots`, ends with `quit`; no earlier client running; PNG timestamps 02:16 checked): `tools/screenshots/out/20261128-021609/thaw_goal_row.png` and `phase3_goal_row_warmth.png`. Seen: "Thaw" and "Phase 3 begins" as the goal name, "Air temperature . Goals 2 / 6 ... 59%" and "Air and temperature . Goals 3 / 6 ... 87%" on the detail line, no raw keys, no overlap, side text "-36.30 / -20 C" and "-8.00 / 0 C".
- Real output: `./gradlew spotlessApply build runGameTestServer`: BUILD SUCCESSFUL, 333 unit tests, 0 failures (baseline 318 + 15 new); `MARS-SELFTEST RESULT PASS passed=139 failed=0`.
- Docs: BACKLOG #49 row, HANDOFF section 00. Launch guide: no change needed.

## 2. Decisions needed
1. Journal trigger (well-structured). The design row says "first banded block in the inventory"; as built the entry unlocks at Dome stable, the only trigger the journal code has. Players hold the banded machines before that. Recommend: accept for now; an item-pickup trigger is a later slice. Needs Game Designer and owner.
2. `dome_stable.waiting` wording changed for fit (well-structured). Recommend accept; the Instructional Designer may reword within 28 characters.

## 3. Handoffs
> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** New and changed lang text needs a wording review.
> **Context:** `pack/assets/mars/lang/en_us.json` keys listed in section 1. CR-T1, T5 and T7 pass by scan. CR-T6 grade: not run as a tool (a grade formula is meaningless on 3 to 5 word fragments; "temperature" alone pushes it up). 
> **Needed from them:** confirm or reword within the character limits. **Done when:** reviewed.

> **To:** Subject Matter Expert · **Problem type:** well-structured
> **Problem:** Confirm the MOXIE sentence on the oxygen maker tooltip and the water reclaimer line (already open).
> **Done when:** both marked verified.

## 4. Risks
- Not seen in a real client: the temperature leak cause line and the machine cause lines with real text (they only show while cooling or short on power); covered by the width test only, which uses an estimated font.
- Wording of the waiting lines ("Count waits") is terse; may read oddly to a 12-year-old.
- Journal trigger gap (decision 1).

## 5. Shared model updates
- Lesson: add the lang keys in the same change as any gate or machine file; `LangKeysTest` now fails the build otherwise.
- Expectation: gate detail lines are at most 28 characters (they share a line with the goals count and percent).
