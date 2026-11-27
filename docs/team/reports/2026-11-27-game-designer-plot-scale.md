# Game Designer: Monitor pressure plot scale, 2026-11-27

Type of work: design walkthrough from the code, the Technical Lead report and the real screenshots (`tools/screenshots/out/20261127-123605/monitor_hist_1_new_planet.png`, `_4_long_game.png` opened). No real players. Docs only. Spec: `docs/design/ui-specs.md` section 9.1 (proposed).

## 0. Handoffs received
Owner request via the coordinator: propose how the plot should scale. Accepted. The Technical Lead's risk ("a game heading to 75 kPa looks flat for a long time") is confirmed, with a sharper finding below.

## 1. Done
Added `docs/design/ui-specs.md` section 9.1 (tag proposed): rules, a height table, wording, build list, threshold and content-rule checks. This report.

### Frame (ill-structured: learning vs. readability vs. cost)
What makes it ill-structured: the plot is about 17 px tall, so every scale trades early visibility against long-run shape; a 12-year-old and a 17-year-old read axes differently; and the goal threshold is physical (cannot move) while time is not. Disciplines: Instructional Designer (graph reading, C3, B3), SME (what is honest to show on real-data-style axes), Technical Lead (cost), Artist (line styles).

### Finding about the current plot
`PlotScale.niceMax` sets the top to 1, 2, 5 or 10 times a power of ten above 1.1 x the largest value. At 4.55 kPa the top jumps from 5 to 10, so the whole line halves in height just before the Phase 2 gate (14 px at 4.5 kPa, 8 px at 4.7 kPa, computed). A climbing game briefly looks like a fall. Flatness in the long game is real too: in shot 4 (4.7 of 10 kPa, 53 sols) the line is a 7 px ramp. And nothing on the plot says where the goal is.

### Options (plot 17 px tall; heights computed, not measured)
| Option | What the player sees | Learning (ages 12-18) | Cost | Verdict |
|---|---|---|---|---|
| A. 0 to max so far (current) | Top steps 1, 2, 5, 10...; line halves at each step; no goal | Keeps the zero baseline (good). Step drops read as failure (bad for B1, B3). Cannot answer "how far to go?" (C3) | Built | Fix needed |
| B. Auto-scaled window of recent sols (like REMS) | Last N sols fill the full height; 0.08 kPa per sol looks like a cliff | REMS uses it for a sensor band (verified, monitor-reference-audit R1), but there the point is the daily swing. Here the point is distance to a goal. A truncated axis exaggerates and hides 0; a known graph-reading trap for grade 6-8 | Medium (window rule, labels change every frame) | Reject as default |
| C. Log scale | 0.6 to 75 kPa is 2.1 decades: 44 % of the height for 0.6 to 5, 37 % for 5 to 30, 19 % for 30 to 75 | Even phases and no flat stretch. But logs are beyond most of the band (grade 6-8 math), and distance to goal stops looking like work left: +45 kPa at about 1.8 kPa/sol looks small | Medium | Reject (HS read-more at most) |
| D. Phase-stepped fixed ranges at the gates, labelled goal line | Range 0 to 6, then 36, then 90 (1.2 x goal); a solid line at 5, 30, 75 kPa; each phase ends with the line at about 83 % of the height | One idea per phase: "get the line to the goal line". Linear, zero baseline, two labelled anchors: readable at 12. Axis change is a small lesson in reading scales for 17-year-olds. Supports B3 (extend the slope to the goal) and C3 | Small to medium | **Recommend** |
| E. Two-view toggle (phase range / whole game) | A button; default D | Gives teachers the "whole journey" view; but a control to find, a state to explain, and no room (plot is already 16 px minimum) | Medium plus a layout change | Later, only if playtests ask |

The honest cost of D: early Phase 1 is flatter than today (4 px instead of 13 px at 1.6 kPa) and the line starts on the baseline (2 px at 0.6 kPa). At the start of Phase 2 and 3 the line also drops (2 px, 5 px) when the range steps. The note "The graph now goes up to ..." and the "Goal reached" marker on the same sol answer that. The signed rate in the Pressure row carries small movements.

### What the player sees (option D)
A 12-year-old at sol 0: a nearly empty plot, a solid line near the top labelled "Phase 2 begins: 5 kPa", and a "0" and "5" in the margin. Each sol the line climbs toward it. When it crosses, the range steps to 36 kPa, one line says so, and the line is low again with a new goal line near the top. A 17-year-old sees the same and can say why the line "dropped" (scale, not pressure). Wording, key names and line styles are in section 9.1.

### Build (Technical Lead; estimate unverified)
`PlotScale.topFor`, goal line in `MonitorView.History` (payload change, extend the boot round-trip check, no save schema change), screen drawing reusing the label-band rule, optional rescale message, tests. Rough size: under a day; **unverified**. `MonitorLayout` heights do not change.

### Physical thresholds and content rules
- The goal line reads the gate JSON (single source). Classroom mode changes sols, not kPa. Test under both presets. The 1.2 factor is a drawing choice (invented). DESIGN.md "Physical thresholds never scale" and I-1 hold.
- CR-F9: only thresholds already public. Triple point, Armstrong and Everest lines must declare `reveals` before they are ever drawn (F4a, F7). CR-F10: forecasts never resolve on the plot. CR-T1, T4, T5, T6 apply to the new keys.
- NGSS: the data practices at stake are "analyzing and interpreting data" and "using mathematics" (practice names; I did not look up or cite codes: **unverified** pending the Instructional Designer).

## 2. Decisions needed
1. **Adopt option D?** Recommend yes. Ill-structured (readability vs. early visibility); owner, after Instructional Designer input.
2. **Show the 5 kPa goal line from sol 0, before "Phase 2 begins" is the Next goal?** The Next goal row first shows "Dome stable". Recommend yes: pressure is the only lever and the player needs a target; the number is already in the public gate table. Ill-structured (progressive disclosure vs. guidance).
3. **Plot height.** Taller (about 24 px) would help early Phase 1 more than any scale. Needs a layout trade-off with the 130 px panel. Recommend not now. Ill-structured; Technical Lead and owner.

## 3. Handoffs
> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** build option D after the owner accepts it.
> **Context:** `docs/design/ui-specs.md` section 9.1; `mod/src/main/java/mars/monitor/view/PlotScale.java`.
> **Needed from them:** size estimate, then the five items in the build list.
> **Done when:** the plot never halves between 4.5 and 5 kPa; the goal line equals the gate JSON in both presets; payload round-trip check passes; screenshots of sol 0, 4.7 kPa and the step to the 0 to 36 kPa range.

> **To:** Lead Instructional Designer · **Problem type:** ill-structured
> **Problem:** do the range step and the goal line support graph reading for both bands?
> **Context:** section 9.1; LEARNING.md C3, B3.
> **Your view:** a labelled goal line and a zero baseline help grade 6-7; the step is a risk for a reader who misses the note.
> **Needed from them:** grade 6-7 check of `monitor.plot.goal`, `monitor.plot.rescaled`, the caption; confirm the NGSS practice names; opinion on decision 2.
> **Done when:** keys confirmed or replaced.

> **To:** Lead Subject Matter Expert · **Problem type:** well-structured
> **Problem:** confirm that a zero-based linear axis with a goal line is a fair way to show pressure, and the REMS comparison.
> **Needed from them:** yes or changes. **Done when:** noted in section 9.1.

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** a solid goal line must read differently from the dashed event lines at 3:1 contrast on the dark console.
> **Done when:** line style and colour chosen in `MonitorStyle`.

## 4. Risks
- Early Phase 1 looks flat in D; a 12-year-old may think nothing is happening at sol 2. Mitigation: the rate line and the goal line. Test in the first playtest.
- A reader who misses the rescale note may read the step as a failure. Fallback: toggle (option E).
- Pixel heights are computed, not measured. The goal label can collide with the series on short plots (existing label-band rule applies).
- Phase 3 gate has a temperature half; pressure can pass 30 kPa before it latches. The overshoot guard covers the plot, but the goal line then sits below the line while "Phase 3 begins" is still open. Needs a wording check.

## 5. Shared model updates
- Proposed (not decided): Monitor pressure plot uses a phase-stepped range, zero baseline, and a solid goal line read from the gate data. Owning doc: `docs/design/ui-specs.md` section 9.1.
- Lesson: an auto-nice axis that steps (1, 2, 5, 10) makes a rising value look like a fall at each step; check any auto axis by computing the line height just before and after each step.
