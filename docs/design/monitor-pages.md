# Planetary Monitor: pages (paged Monitor)

**Owner:** Lead Game Designer · **Written:** 2026-11-28 · **Status:** PROPOSAL, not built. Wording is straw man (grade 6-7 check by the Instructional Designer not run). Nothing here was tested in the game; sizes are estimates from the code constants and three screenshots (`tools/screenshots/out/20261128-180743/monitor_air_rows.png`, `20261128-112205/alert_1_two_outgassers_no_power.png`; the third, `20261128-141443/monitor_surface_water.png`, was not opened separately because the first two already show the problem).

**Goals served:** C3 (read data to decide), B1 (cause and effect), B3 (predict from a trend), A1 to A5 (per-gate reports), D2 (the planet as a whole). **Pillars:** the planet is the quest log; every change is visible; compact before wide; pressure, not grind.
**Two goals:** instructional value (the important number never scrolls away; one topic per page) and market appeal (a mission-control feel: header, tabs, lamps). Cost: one more click to see a number that is off the current page.

## 1. Problem

The one-screen panel (`MonitorLayout`, `TARGET_H` 155) already drops the plot when problem lines pile up (`PLOT_OVERFLOW`) and drops the goal detail line first (`fit`). The first screenshot shows 10 rows and no plot room; the second shows an alert, plot and 5 rows. Each new system (oxygen, CO2, surface water, power per machine, storms, reports) adds rows. Ill-structured in part (how much to hide is a fun-versus-learning call), so section 11 lists options and open decisions.

## 2. Page set (six lines)

1. **Header (always visible):** title, Sol clock, Pressure (value, unit, trend), alert slot (starved or storm).
2. **Overview** (default): header, pressure plot, Next goal bar.
3. **Air and water:** Temperature, Water boils at, Surface water, Oxygen, CO2, cause lines.
4. **Power and machines:** Power row, Making and using, per-machine lamps, starved list, hold-to-explain card.
5. **Goals and reports:** all gates with progress, latched list, Report to orbit, planet report page.
6. Tabs: number keys 1 to 4, Tab and Shift+Tab, mouse click, dots for attention.

## 3. Fixed header

Always drawn, on every page, never scrolled, never dropped by `fit`:

| Line | Content | Height (GUI px) |
|---|---|---|
| Title row | "Planetary Monitor", "Esc to close", "Sol 2.5" (suffix "(paused)" when paused) | 12 |
| Pressure | Air pressure, big value with unit, trend shape, signed rate (`ROW_H_EMPHASIS`) | 14 |
| Alert slot | One line, only when a starved machine or a storm is active. Starved alert has priority over storm; both can be on, then two lines | 0, 12 or 24 |

Why pressure: it is the main number of the whole game and every Phase 1 and 2 gate is a pressure threshold. The alert sits in the header so a starved Outgasser is seen even on the Goals page (pillar 2). The alert uses the one rare alarm colour (`ALARM`) plus the warning triangle shape; storm uses the same shape with the storm word, never colour only (CR-T7).

The Pressure row is **not** repeated on Air and water (it is in the header). "Pressure detail" from the request: the cause lines for pressure (Outgassers, Ovens, polar frost) live on the Air and water page as the first block, headed by the label "Pressure causes" (15 chars).

## 4. Tab bar

Under the header, 14 px tall, four tabs across the 232 px content width (about 58 px each). Each tab is a 9 px icon and a label of at most 24 characters (target 10 or fewer so it fits in 58 px; the full name shows in the tooltip and the narrow-screen fallback).

| # | Icon (Lead Artist to draw, 9 x 9) | Label (key) | Tooltip |
|---|---|---|---|
| 1 | Gauge | `monitor.tab.overview` "Overview" | "Pressure history and your next goal" |
| 2 | Cloud and drop | `monitor.tab.air` "Air and water" (13) | "Temperature, water, oxygen, CO2" |
| 3 | Plug | `monitor.tab.power` "Power" | "Power and your machines" |
| 4 | Flag | `monitor.tab.goals` "Goals" | "All goals and reports to orbit" |

The tab label is shorter than the page title; the page title key is `monitor.page.power` "Power and machines" (18). Selected tab: filled backing plus underline (shape, not only colour). Unselected tabs use `LABEL` colour with the icon so the label is readable (contrast: Lead Artist to check).

**Input:**

| Input | Action |
|---|---|
| Click tab | Select |
| 1 to 4 | Select page (ignored while a text field or the report screen has focus) |
| Tab / Shift+Tab | Next / previous page, wraps |
| Mouse wheel over body | Scroll only if the page overflows (section 7) |
| Esc / the Monitor key | Close (unchanged) |

Number keys are shown on the tab as a small digit in the corner ("1") so the key is learnable without a manual (a hint line is not needed).

**Narrow or short screens:** if the panel plus tab bar does not fit, tabs show icons only (a 12-year-old keeps the tooltip). Below that, the existing compact mode (`plotHeight` returns 0) still applies to Overview.

## 5. Default page and memory

| Rule | Behavior |
|---|---|
| First ever open | Overview |
| Later opens (same client session) | Last page used, kept in a client-side field (not saved to the world) |
| After a world load | Overview. (Local, nothing leaves the computer; saved per player later only if playtest asks.) |
| Open from a "Report ready" prompt or the goal-latched chat line | Goals page, report entry selected |
| Open while an alert is active and the player never opened Power | Overview still; the header alert and the Power tab dot guide them (the planet is the quest log; do not yank the player) |

## 6. Attention dots

A 3 x 3 px dot at the top right of a tab, plus the text "!" is **not** used; the dot shape (filled square, not colour only) plus a tooltip line "New: {what}" make it accessible. A dot clears when the player opens that page and the thing is seen (for a report: when it is filed or after the page was open for 2 s, never auto-filed).

| Tab | Dot when | Clears when |
|---|---|---|
| Power | A machine is starved (same condition as the alert) | Condition ends (live, not a one-time) |
| Air and water | A new water or air line just appeared for the first time (for example Surface water) | Page opened once |
| Goals | A gate just latched and its Report to orbit is ready, or a supply drop was received | Report filed or page opened |
| Overview | Never (the header already carries the alert) | n/a |

At most one dot per tab; text in the tooltip names the cause. Dots are for the player, not a hint of a forecast answer (CR-F9: a dot must not appear on a tab that would reveal an open forecast's answer; see section 9).

## 7. Height bound and overflow

Constants proposed (new in `MonitorStyle`; current `PANEL_W` in code is **250**, the docs and this request say **232**: see open decision 5; every number below is written for width W and does not depend on it).

| Item | Value (GUI px) |
|---|---|
| Hard panel height `PAGE_H_MAX` | **190**, same for every page (the panel does not jump size when switching tabs; the body area has a fixed height) |
| Header | 38 without alert, 50 with one alert, 62 with two |
| Tab bar | 14 |
| Body (page content) | 190 - 38 - 14 = **138** (126 with one alert, 114 with two) |
| Screen fit | 240 px at GUI scale 3, minus 2 x `MARGIN` 6 = 228 >= 190; 360 px at 720p scale 2 (and 540 px at 1080p scale 2) is more than enough. Scale 4 at 720p (180 px): compact mode, see below |
| Compact mode | Screen height below 190 + 12: drop `PAGE_H_MAX` to the screen height minus margins, body scrolls |

Fixed panel height is a deliberate choice: a panel that changes size with each tab feels jittery, and the test is simpler (one bound). The cost is empty space on short pages (Goals with two gates); the empty space can show the "nothing yet" line (section 8).

**Overflow:** content is laid out in rows; if a page's rows exceed the body, the wheel scrolls it and a small "more below" arrow shows at the bottom edge (shape). The test goal is that **normal** play never scrolls; scrolling is the safety net for many starved machines or long gate lists. The old `fit` priority (drop goal detail first) is replaced by this on the paged screen. Problem lines (starved, dusty) never drop (kept from today).

## 8. Empty rows and empty pages

Rows appear only when relevant, as today (the Surface water row appears when there is water to track; Oxygen and CO2 when the gas row exists; Power when a machine uses the grid). Pages never hide the tab itself: a page with nothing yet shows one line.

| Page | Empty line (key) | Text (straw man) |
|---|---|---|
| Air and water | `monitor.page.empty.air` | "Only temperature so far. More shows up as the air changes." |
| Power and machines | `monitor.page.empty.power` | "No machines on the grid yet." |
| Goals | never empty (the next gate always exists until the last) | n/a |

## 9. Pages and rows (every row on exactly one page)

Row keys are those in `MonitorView` today. `Tone` and `emphasis` are unchanged. Cause lines stay attached to their parent row.

| Row key (labelKey) | Page | Notes |
|---|---|---|
| `monitor.row.pressure` | Header | Big value, trend, rate. Its cause lines go to Air and water ("Pressure causes") |
| `monitor.row.alert` (starved) | Header | One line |
| Storm line | Header (alert slot) | Wording in ui-specs section 10; the Power-row dust change stays on Power |
| Sol clock, paused flag | Header | |
| Pressure plot, goal line, event markers | Overview | Plot height is the leftover of the body after the Next goal row, min `PLOT_MIN_H` |
| `monitor.row.next_goal` (bar, percent, estimate) | Overview | Estimate follows CR-T8 |
| `monitor.row.temperature` + causes | Air and water | |
| `monitor.row.water_boils` | Air and water | Read-more line stays |
| `monitor.row.hydrosphere` (Surface water) + causes | Air and water | |
| `monitor.row.oxygen`, `monitor.row.co2` + causes | Air and water | |
| Pressure cause lines (Outgassers, Ovens, polar frost, Levelling off) | Air and water, first block | Read-more lines (frost) here |
| `monitor.row.pace` | Overview, under the plot | Only in the existing case |
| `monitor.row.power` + Making/using | Power and machines | |
| Per-machine rows, state lamps, starved list (`needs_power`, `low_power`, `dusty`) | Power and machines | |
| Hold-to-explain card (key `H`) | Power and machines | A machine row under the pointer or key focus shows the card (section 10) |
| `monitor.row.goals_reached` | Goals and reports | |
| All gates with progress, latched list | Goals and reports | |
| Report to orbit entry, planet report page | Goals and reports | |

## 10. Page mockups (width 232 px; one char is about 2 px per 3 px of text, so lines are schematic)

**Header (all pages), alert on:**
```
Planetary Monitor          Esc to close   Sol 5.0
! Regolith Outgasser x2 starved of power
Pressure       0.60 kPa          - 0.00 kPa/sol
[1 Overview*][2 Air and water][3 Power !][4 Goals]
```

**1 Overview** (body 126 with alert; plot 40 px at most, 25 px minimum today at `PLOT_MIN_H`)
```
Phase 2 begins: 5 kPa
5 +-------------------------------------+
  | Outgasser placed        ---------- |   <- goal line
0 +.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-+
  Sol 0               Sol 3          Sol 5
Next goal   Dome stable      0.0 / 3 sols
[==========------------------------------]
```

**2 Air and water**
```
Pressure causes
  Regolith Outgassers: +0.60 per sol
  Polar frost: -0.02 per sol
Temperature   -60 C      - 0.00 C/sol
Water boils at  Too thin for liquid water
Surface water   19.5 %   v Falling -0.20
  Water freezes or sinks back.
Oxygen   5.15 kPa            ^ +0.30 kPa/sol
Carbon dioxide 7.25 kPa      v -0.50 kPa/sol
```

**3 Power and machines**
```
Power          Enough power   [|||||||||] 100%
Making +15.0, using -8.5 units/sol   10.0 / 10.0
Machines
  [ON ] Regolith Outgasser x2      -4.0
  [LOW] Regolith Oven                -2.5
  [OFF] Oxygen maker  needs power    -2.0
  Solar panel x3  dusty, makes less
Hold H on a machine: what it does
```
State lamps are a box with a word (ON, LOW, OFF) plus colour; the box shape and word carry the meaning.

**Hold-to-explain card** (aim at a machine row with the pointer, or hold H; 25 words or fewer, from `gate-input-items.md` section 6): drawn over the body bottom 3 lines; it never covers the header or tab bar.

**4 Goals and reports**
```
Goals reached  1 / 7
[x] Dome stable           (sol 4.0)   Report: filed
[>] Phase 2 begins   0.60 / 5 kPa     [=-----]
[ ] Breathable ...
[ Report to orbit: Dome stable ]   <- button, dot while unfiled
[ Planet report ]
```
The "Report to orbit" button opens the 5-step screen from `report-to-orbit.md` (a sub-screen with a back control to this page); the planet report page (before/after table) opens from the latched row. Locked future gates appear as a line with the gate name only after the progressive-disclosure rules in ui-specs section 2 allow it (no spoilers).

## 11. Options and recommendation (ill-structured part)

**Frame.** How much to split the Monitor is a trade-off between glanceability (everything at once) and clarity (one topic per page). Disciplines: Instructional Designer (does hiding rows weaken B1 cause-and-effect or C3 reading data?), Technical Lead (cost, test), Artist (icons, tab look), SME (none for layout).

| Option | Fun | Learning | Cost |
|---|---|---|---|
| A. Paged with fixed header (this doc) | Mission-control feel, room to grow | One topic per page; the key numbers never vanish | Medium |
| B. One screen, collapsible sections | Familiar, fewer clicks | Collapsed sections hide causes (B1 risk) | Low to medium |
| C. Keep one screen, shrink fonts | None | Hurts reading at 12 years old | Low, but fails the scale-3 bound |

**Recommendation:** A. **Assumptions:** (1) four pages are enough through Phase 3; a fifth (Life and soil) may be needed when biomass rows exist (it would hold biomass rows, a Phase 3 tab shown only then; not designed here). (2) Players check the Monitor in short visits. (3) Number keys do not clash with the hotbar while the screen is open (the Monitor is a screen, so the hotbar keys do nothing there; Technical Lead to confirm). **Made better defined by:** a playtest watching whether a 12-year-old finds the Power cause for a starved alert via the dot.

## 12. Content-rule effects

| Rule | Effect on pages |
|---|---|
| CR-F9 / CR-T8 | Hiding a line that would reveal an open forecast's answer is still **per player** (the forecast state of the viewer), done by `MonitorView.hidingLines(Predicate)`, applied before the page split, so the same predicate hides the same key on every page. A dot's tooltip text and the "report ready" dot obey the same predicate: a report with an open listed forecast is not offered, so no dot. Under `guidance` = expert the time-to-goal estimate stays hidden (Overview) |
| CR-T7 | US spelling ("Leveling"), words over symbols ("about", not "~"), labels at most 24 chars (all tab and page labels above are at most 18), shape plus colour, no colour-only dots or lamps |
| Reading level | Grade 6-7 for all new keys; the Instructional Designer scans before ship |
| Report to orbit | Entry moves from a lone tab to the Goals page; the rest of `report-to-orbit.md` is unchanged |

## 13. Text keys (new; straw man, US spelling, at most 24 chars)

| Key | Text |
|---|---|
| `monitor.tab.overview` | Overview |
| `monitor.tab.air` | Air and water |
| `monitor.tab.power` | Power |
| `monitor.tab.goals` | Goals |
| `monitor.page.power` | Power and machines |
| `monitor.page.goals` | Goals and reports |
| `monitor.section.pressure_causes` | Pressure causes |
| `monitor.section.machines` | Machines |
| `monitor.page.empty.air` | (section 8, over 24 chars: a sentence, not a label; allowed) |
| `monitor.page.empty.power` | (section 8) |
| `monitor.tab.dot.starved` | New: a machine needs power |
| `monitor.tab.dot.report` | New: a report is ready |
| `monitor.tab.dot.drop` | New: supply drop received |
| `monitor.tab.dot.air` | New: a new air line |
| `monitor.scroll.more` | More below |
| `monitor.lamp.on`, `.low`, `.off` | ON, LOW, OFF |

## 14. What the Technical Lead builds (slices; effort **unverified**)

| Slice | Content | Effort | Done when |
|---|---|---|---|
| P1 | Pure page model in `mars.monitor.view`: `MonitorPage` enum, `MonitorPages.assign(MonitorView) -> Map<Page, List<Row>>` plus header rows and plot. No Minecraft imports (ArchUnit rule holds) | 1 d | Tests below pass |
| P2 | `MonitorScreen` and `MonitorLayout` per page: header, tab bar, fixed `PAGE_H_MAX`, input, last-page memory, scroll fallback; hide per-player lines first | 2 d | Screenshots of all four pages at scale 2 and 3 and 720p look right (run and look) |
| P3 | Tab dots: pure `TabAttention` computed from `MonitorView` plus gate and report state; drawing; clear rules | 1 d | Dots appear and clear in tests and a screenshot |
| P4 | Goals page content (all gates, latched list, Report to orbit button, planet report page); depends on `report-to-orbit.md` R2 | 2 d (with R2) | Opens the report from the page |

**Tests that would prove it:**

1. Every `labelKey` in a full-state `MonitorView` maps to exactly one page (header counts as a page); an unmapped key fails the test, so new rows must be assigned.
2. The header contains Sol, Pressure and the alert whenever the view has them, on every page, including with 3 starved machines (one alert line, not three).
3. Layout bound: for every page in a worst-case view (all rows, 6 cause lines, two alerts), `bodyHeight` is at most 138 minus the alert lines, or the page declares overflow (scroll); and for a normal mid-game view no page overflows. Panel height equals `PAGE_H_MAX` for every page, so at most 228 px (scale 3) and the screen-fit assertion holds for 240 and 360 px screens.
4. `hidingLines` predicate: a row hidden for a player is on no page for that player, and a dot is not set by a hidden row (CR-F9, CR-T8).
5. Key handling: 1 to 4 and Tab cycle correctly; Tab on page 4 goes to page 1; last page is restored on reopen; a report prompt opens page 4.
6. Text scan: all new keys at most 24 chars (labels), no "~", no "Levelling", US spelling list.
7. Dot rules: starved dot equals alert condition; report dot only for a ready, unfiled report; none on Overview.

## 15. Open owner decisions

1. **Four pages or fewer?** Recommend four (Overview, Air and water, Power, Goals). Ill-structured.
2. **Fixed panel height (190) or height per page?** Recommend fixed. Well-structured after a screenshot test.
3. **Default page** Overview, remember last in the session. Recommend as written.
4. **Dots:** accept the dot rules (never on Overview; no auto-open on alert). Ill-structured (guidance versus nagging).
5. **Width:** code says `PANEL_W` 250, docs say 232. Keep 232 as requested? Recommend setting 232 in code and re-checking the Overview line widths ("Making +15.0, using -8.5 units/sol" is the widest line today).
6. **Pace row and a fifth "Life" page** for Phase 3 biomass: decide later, when biomass rows exist.
