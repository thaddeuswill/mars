# Report: paged Planetary Monitor (Game Designer, 2026-11-28)

## 0. Handoffs received
Owner request: the Monitor is crowded, add pages or tabs. Accepted. Questioned: code `PANEL_W` is 250 (`MonitorStyle.java`), the request and ui-specs say 232.

## 1. Done
- `docs/design/monitor-pages.md`: page set, fixed header, tab bar and input, default and memory, dots, height bound (panel 190, body 138), empty rows, row-to-page table, ASCII mockups, content-rule effects, text keys, slices P1 to P4 with tests.
- `docs/design/ui-specs.md`: section 11 pointer.
- Method: read the code constants, the layout class, docs and two screenshots. Not playtested, not run in the game; effort estimates are **unverified**. Did not run Gradle.

## 2. Decisions needed
1. Four pages (recommended). Ill-structured.
2. Fixed 190 px height for all pages (recommended). Well-structured after a screenshot test.
3. Default Overview, remember last page in the session. Well-structured.
4. Dot rules, no auto-open on alert. Ill-structured.
5. Width 232 versus 250 in code. Well-structured; recommend 232 and re-measure.

## 3. Handoffs
> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** build the paged Monitor.
> **Context:** `docs/design/monitor-pages.md` sections 9 and 14.
> **Needed from them:** slices P1 (pure model and tests), P2 (screen), P3 (dots); confirm number keys do not clash with the hotbar in a screen; resolve width 232 vs 250.
> **Done when:** the seven tests pass and screenshots of all pages at scale 2, 3 and 720p are checked.

> **To:** Instructional Designer · **Problem type:** well-structured
> **Problem:** grade 6-7 and CR-T7, CR-F9 scan of the new keys and the dot tooltips; judge whether hiding causes on another page weakens B1.
> **Context:** section 13 and 12 of the pages doc.
> **Done when:** keys approved or reworded.

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** four 9 x 9 tab icons, selected-tab and dot look, lamp boxes; contrast check.
> **Done when:** icons in `pack/assets`, checked at scale 2 and 3.

## 4. Risks
- A key number hidden on another page may weaken cause-and-effect learning (B1); the header and the dots are the mitigation.
- Fixed 190 px height leaves empty space on short pages.
- Report to orbit depends on slice R2, so page 4 may ship first without it.
- Width mismatch 232 vs 250 may change line wrapping.

## 5. Shared model updates
New terms: "Monitor page", "header" (always-visible block), "attention dot". Proposed decision (pending owner): the Monitor has four pages under a fixed header; owning doc `docs/design/monitor-pages.md`.
