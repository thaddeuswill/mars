# Monitor mockups (BACKLOG #37): three options

**Date:** 2026-11-27 · **Author:** Lead Artist · **Status:** mockups for the owner to compare. Nothing in the game, Java or `pack/` changed. Not playtested.

Files (all 1280 x 720, GUI scale 3 = 426 x 240 GUI px):

| Option | PNG | Size in GUI px | Share of screen |
|---|---|---|---|
| A. Dark console + sparkline | `A-dark-console-sparkline.png` | 196 x 128, top-left | 46% x 53% |
| B. Light science plot | `B-light-science-plot.png` | 194 x 124, top-left | 45% x 52% |
| C. HUD strip | `C-hud-strip.png` | 414 x 88, top edge | 97% x 37% |

Compare with the current screen: `tools/screenshots/out/20261127-114819/slice3_powered_one_panel.png` (about 250 x 130 GUI px, six rows, no history).

## What the mockups are (and are not)
- **Made with:** HTML + CSS rendered by Playwright Chromium, GUI scale 3 (page scaled 3x). Script: `src/gen.py`, `src/shot.js`.
- **Backdrop:** a real game shot (`tools/screenshots/out/20261127-114819/outline_side_view.png`), so crosshair, chat and the tutorial toast are real. The bottom label strip on each PNG is part of the mockup notes, not of the design.
- **Font is not Minecraft's.** Words use Pixelify Sans (OFL, from `/mnt/skills/.../canvas-fonts`, not in the repo); **digits use JetBrains Mono Bold** because Pixelify's 5 reads as S and 0 as 8 at this size, which would make the numbers wrong to read. The real font is the game's own (Stage 3). Glyph widths differ, so check text fit in-game.
- It is a flat picture: Chromium text is smooth-scaled, not a true 1x pixel grid. Layout and colours are the claim, not pixel exactness.
- **Same data in all three:** Sol 16.0, air pressure 1.60 kPa rising +0.10 kPa/sol, temperature -60 C steady, water boils at **14 C** (computed with `pack/data/mars/parameters/boiling_point.json` Antoine coefficients: 14.09 C at 1.60 kPa; the shipped screens show 8 C at 1.10 kPa), power "Enough", battery 5.0 / 10.0, making +5.0 / using -4.0 units/sol, next goal "Phase 2 begins 1.60 / 5 kPa", goals 1 / 2. Sol 16 and the 1.60 value are a consistent example (not a real run).

## Invented elements (marked in each PNG's note)
| Element | Option | Status |
|---|---|---|
| Pressure history series, Sol 6 to 16 (0.60 to 1.60 kPa, "Outgasser placed" at Sol 6) | A, B | **Invented example data.** The game does not store it today |
| Status lights (Power / Outgasser / Dome), shape + word | C | **Invented**; audit cue 10 [NV], no real image. Not claimed to copy a real screen |
| Battery as 10-segment bar + percent | C (A, B use a plain bar) | **Invented** (cue 10 [NV]) |
| "needs 0.61+ kPa" under Water boils at | C | Wording invented; 0.61 kPa is the water triple point (0.611657 in `boiling_point.json`) |
| Dashed event line "Outgasser placed" | A, B | Idea is real (R4 coloured event lines [V]); this event and its Sol are example |
| 1600 Pa next to 1.60 kPa | B | Unit choice (cue 3); needs a formatting-helper rule, see below |

No min/max band anywhere (the sim has one value per sol). No Ls, no season strip, no Earth comparison line (dropped for space; see cue 8 / 7).

## Option A. Dark console + sparkline history
**Borrows:** sol on the X axis with plain numbers (R1, R2, R4 [V]); dot per sample on a line on a dark ground (R7 [V]); event marker line with a label (R4 [V]); units next to values; colour-matched series (R5 [V], light touch). Keeps the current dark colours and word+shape trend, so it reads as the same Monitor, better organised.
**Pros:** closest to today's look (smallest change for players and code); history answers "is it rising?" at a glance and shows cause (marker) next to effect (B1); left-anchored and 128 GUI px tall, so centre and crosshair stay clear (verified in PNG).
**Cons:** needs stored history; the sparkline is small (150 x 26 GUI px) so only shape, not exact values; dark panel is the least like the real NASA plots.
**Technical Lead must build:** (1) a per-sol series for pressure in `PlanetData`, saved with the planet, appended once per sol (e.g. last 11 values, one double each; about 100 bytes per value); I found no history in `mars/planets` or `mars/monitor` (grep), so this is **new state plus a save-format bump and a unit test**; (2) event records (sol + machine type) for the marker, a second small list; (3) sync to the client in the existing Monitor packet (11 doubles); (4) draw with `GuiGraphics.fill` lines (no new assets). Honest cost: about one slice, mostly persistence and tests. A "no history yet" empty state is needed (new world, sol < 2).

## Option B. Light science plot (REMS/MEDA style)
**Borrows:** white plot panel, black text, one blue series, dotted grid, "Sol" X axis, "Pa" on the Y axis (R1 PIA16912, R2 [V]); blue readout colour-matched to the series (R5 [V]); event line (R4 [V]). Pressure in Pa is the only place the monitor changes units.
**Pros:** strongest link to the real sources, teaches that Mars pressure is hundreds of Pa and that scientists mark causes on the graph (A1, B1 per the audit; goal IDs not re-checked); highest contrast (all text at least 5.28:1 on the paper); the axis is honest about its range.
**Cons:** a bright panel in a dark game clashes with the rest of the HUD and is the biggest visual change; mixing Pa and kPa (the rest of the game says kPa) risks confusion unless one helper formats both, and the 5 kPa goal is off the 1800 Pa axis (the axis range would have to rescale as pressure climbs: new logic); smaller type needed to fit 194 px.
**Technical Lead must build:** everything in A (history series, events, sync), plus: auto-scaling Y axis (nice ticks, range grows as pressure climbs to 75 kPa, log or linear is a design decision, **not made here**), a light colour set in `MonitorStyle` (4 new colours), and a units rule in the one formatting helper so the same value can show as "1600 Pa (1.60 kPa)". Honest cost: A plus about half a slice for axis logic.

## Option C. HUD strip
**Borrows:** mostly **ordinary instrument-panel practice, not a real screen** (audit cue 10 [NV]): battery state-of-charge bar + percent + number, status lights, columns per quantity (R1 to R3 separate panels with one scale each [V] as the idea). Power as energy per sol and "making / using" (R6 [V]).
**Pros:** no history needed: **zero new stored state**, cheapest build; lights give "is anything wrong?" in one glance, each with shape + word, never colour alone; 88 GUI px tall; thin, so most of the view stays clear (the strip is on the top edge, above the centre; verified).
**Cons:** the widest object (97% of screen width), so it sits over the top toast and sky; no trend over time, which loses the strongest Mars-science lesson (graph + cause line); the lights are invented and the Outgasser and Dome lights need defined rules (what turns amber or red); five columns leave little room for long translations.
**Technical Lead must build:** layout only in `MonitorScreen`/`MonitorStyle`: columns, segmented battery bar, three status lights with defined states (a small enum per light and its text key). Needs new lang keys (`monitor.light.*`) and a rule per light; the Game Designer must define the Outgasser and Dome light states (**not defined anywhere I read**). No persistence change.

## Checks
| Check | Result | How |
|---|---|---|
| Text contrast at least 4.5:1 | **Pass**: dark text 6.94 to 17.85 (backing #181210 at alpha 0.97 over sky #B98A66 and over white); paper text 5.28 to 15.17; header 16.04 | `src/gen.py` (WCAG formula), output `src/contrast.txt` |
| Non-text parts at least 3:1 | Bar fill vs empty 7.0; B grid 3.2; panel edge #9A6A4C vs backing 3.86 (the current edge #6B4A36 is 2.2, per the UI review) | same script |
| Colour never the only signal | Trend = word + triangle or bar shape; power = word + bar + number; lights = word (the light shape is a circle in the mock; **warn and fault shapes are not drawn**, see Not verified) | looked at the PNGs |
| Height at most about 130 GUI px | A 128, B 124, C 88 | from the CSS sizes, matched by eye in the PNGs |
| Centre clear | A, B: right edge at 98 and 97 of 213 GUI px centre; crosshair and Outgasser array visible. C: bottom at 94 of 120 | looked at the PNGs |

**Backing alpha:** the mocks use 0.97; the code uses 0xF0 (0.94). At 0.94 the old review measured 6.4 to 17.4, still passing. I used 0.97 so the tutorial toast does not ghost through option C; if the owner prefers translucency, that is fine for contrast.

## Not verified
- In-game look: real font, real GUI scale rounding, translations, long names.
- Warn and fault states of the status lights and a falling trend (only the "good" state is drawn).
- Colour-blind simulation: not run. Shapes and words carry meaning; hues were not tested.
- Whether 12 to 18 year olds read the sparkline or Pa correctly (no players).
- Real NASA console screens: none were collected (audit "Honest limits"); C's lights and battery have no real source.

## Recommendation
**Option A as the base, taking two things from B and one from C.** Reasons: A is closest to the current Monitor, fits the pillar "every change is visible" and "your progress provokes the planet" by showing the pressure line climb after the player acts, and is clear of the centre. From B: the labelled dashed event line (already in A) and a **"Pa" read-more line** only in the journal, not a Pa readout on the panel, so units stay kPa on screen (avoids the Pa/kPa risk). From C: the battery segmented bar (cheap) but **not** the status lights until the Game Designer defines them.
Fallback if the Technical Lead cannot spare the history slice yet: build **C without lights** now (no new state) and add A's sparkline later.
Choose B only if the owner wants the strongest "this is real science" look and accepts the light panel and axis logic cost.
This is an ill-structured call (look vs cost vs learning); the owner decides. Instructional value: A and B add history + cause; C adds status. Market appeal: A and C stay inside the game's dark HUD look; B is the most distinctive.
