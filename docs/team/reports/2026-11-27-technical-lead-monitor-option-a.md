# Technical Lead: Monitor option A, centred (BACKLOG #38), 2026-11-27

## 0. Handoffs received
Owner (BACKLOG #38) via the coordinator: centre the panel; build mockup A (dark console, pressure history plot, Sol on X, event marker, kPa) plus C's battery bar; no status lights; Pa only in the journal. Accepted in full. Artist mockups A and C read (`docs/art/monitor-mockups/`). Codex's viewport clamp (dc5e5bc) was already folded into `MonitorScreen` in #36; I kept its tiny-screen guard and added a plot-drop rule.

## 1. Done
- **Stored history (pure, `mars.index.sim.PressureHistory`):** one sample per sol (plus the first step), max 120; when full every second sample drops and the spacing doubles (whole game stays visible, coarser). Events: first pressure-making machine placed, each gate latch (also `/mars gate latch`); max 16. `PlanetState` **schema 4**; older saves load with empty history (unit test and boot check). A save load adds no events.
- **Sync:** `MonitorView.History` (points thinned to 60, current value appended, markers, unit) in `MonitorPayload`.
- **Screen:** `MonitorScreen` centred (x and y), plot with axes ("kPa" top label, 0, Sol first/middle/last), dashed event lines, labels on the band the series does not cross, 10-segment battery bar plus percent, "Esc to close", reasons on stalled machines kept, goal row kept. `MonitorLayout` (pure) holds the heights; `PlotScale` (pure) the axis. Colours: `MonitorStyle` (panel edge raised to #9A6A4C for 3:1; empty battery segment #78685E).
- **Dropped to fit:** the rate-only cause line under Pressure (the trend already shows the rate and the marker shows the cause); it shows only when it carries a reason ("needs power", "low power"). The goal's second line drops first when the plot would fall under 16 px.
- Lang: `monitor.event.placed.regolith_outgasser`, `monitor.plot.empty`; docs: `ARCHITECTURE.md` 2.19 row, `docs/design/ui-specs.md` section 9.
- **Heights (layout test, GUI px at 240 px screen):** new planet 122, powered 130, short plus dusty 135 (target about 130). Measured from the screenshots: 123 / 131 / 136, plus or minus 3.
- **Contrast (measured by `MonitorStyleTest`, backing over white / black / Mars-sky world):** text LABEL 6.42-7.47, VALUE 16.06-18.69, UP 8.77-10.21, DOWN 7.03-8.19, STEADY 10.30-11.99, CAUSE 11.17-13.01 (all at least 4.5). Series 9.75-11.35, plot axis and panel edge 3.47-4.04, empty battery segment 3.01-3.51, full vs empty segment 2.91 (the percent text carries the value too; the mockup's 3:1 for non-text is missed by 0.09 on that one pair).
- **Tests and real output:** `spotlessCheck build test --rerun --offline` BUILD SUCCESSFUL, **114 unit tests, 0 failures** (was 96). `runGameTestServer`: `MARS-SELFTEST RESULT PASS passed=54 failed=0` (was 52): new `save.history_round_trip` and `monitor.history_payload_round_trip` (644 bytes, equal, nothing left over). New unit tests: `PressureHistoryTest` (append once per sol, bound and thinning, event once, bad-save repair, save round trip, schema-3 save, goal event), `MonitorHistoryViewTest` (new / rising / long game, under one sol not drawn), `PlotScaleTest`, `MonitorLayoutTest` (3 states, short screens), `MonitorStyleTest`.
- **Failing-guard proof:** raising `MAX_POINTS * 1000` made `isBoundedAndKeepsTheWholeSpanByThinning` fail; removing `history.restore` in `fromState` made `historySurvivesSaveAndLoad...` fail. Both restored. (Swapping `addMachineQuietly` for `addMachine` on load is not caught, because `restore` then overwrites the events; the guard is redundant, so no test can see it.)
- **Screenshots (real client, 1280x720, GUI scale 3):** `tools/screenshots/out/20261127-123605/` from `tools/screenshots/monitor_history.shots`: `monitor_hist_1_new_planet`, `_2_powered_10_sols` (older run `20261127-123346`, label shows "Outgasser placed"), `_3_short_and_dusty`, `_4_long_game` (53 sols). I opened shots 1, 3, 4 of the final run and shot 2 of the previous run (same code except the empty-message wording and the rule that skips a crossed label on a short plot). PNG timestamps are the copy time, so I confirmed freshness by file sizes changing between runs and by the visible changes.

## 2. Decisions needed
1. **Centred panel covers the crosshair** (expected; opened on purpose, Esc closes). The panel is 250 GUI px wide, 59% of the screen. Narrower (about 205) needs the trend on the cause line. Recommendation: keep. Ill-structured (look vs space); owner.
2. **Rate-only cause line dropped** from the Pressure row to fit 130 px. The pillar "every change is visible" is served by the plot marker and the trend, but "Regolith Outgasser x2 +0.20" is gone unless something is stalled. Recommendation: accept; revisit if testers cannot tell which machine raises pressure. Ill-structured (Game Designer and Instructional Designer input).
3. **Worst case 135 px**, not 130 (short on power plus a dusty panel). A tighter plot would hide the series. Recommendation: accept. Well-structured.

## 3. Handoffs
> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** wording check of "History starts after sol 1.", "Outgasser placed", and the goal marker "Goal reached: Dome stable".
> **Context:** `pack/assets/mars/lang/en_us.json`, `docs/design/ui-specs.md` section 9.
> **Needed from them:** reading level and whether a plot caption should say what the line is ("Air pressure over time").
> **Done when:** keys confirmed or replaced.

> **To:** Lead Game Designer · **Problem type:** ill-structured
> **Problem:** status lights (Power / Outgasser / Dome) were left out because no rules exist.
> **Needed from them:** if wanted later, the states and rules per light.
> **Done when:** rules written in `DESIGN.md`, then the Technical Lead can add them in `MonitorLayout`.

## 4. Risks
- **Not verified:** other GUI scales (2, 4, 5), a window short enough to drop the plot (tested only in unit tests, not drawn), the Falling trend and a falling plot (no machine lowers pressure yet), the paused state, other languages and long names, colour-blind rendering, a real save to disk and restart (the NBT round trip is a boot check, not a world file), the dedicated server (needs the EULA), real players reading the plot.
- Chat text still shows faintly through the left edge of the centred panel (cosmetic, Stage 3).
- With the Y axis starting at 0 the early line (0.6 to 1.6 kPa) is a low, shallow slope; a 75 kPa game will look flat for a long time. A log axis is a design call (not made).
- Event labels can be dropped on a short plot (shot 3 shows the dashed line without its label).
- `PressureHistory` samples only pressure; temperature or other series would need the class generalised.

## 5. Shared model updates
- New term: **pressure history** (per-sol samples plus events, saved in `PlanetState` schema 4). Owning doc: `ARCHITECTURE.md` 2.19.
- Lesson: unit tests cannot load Minecraft classes (no `net.minecraft` on the test classpath); NBT and network round trips belong in boot checks, and layout maths in pure classes (`MonitorLayout`). Spotless reformats code, so scripted text replacements must be re-checked.
