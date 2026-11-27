# Monitor Reference Audit: real Mars weather and power readouts

**Date:** 2026-11-27 · **Author:** Subject Matter Expert · **Status:** findings and design cues; nothing in the game changed.
**Method:** found NASA pages with the search tool and downloaded originals from `assets.science.nasa.gov`. I **opened and looked at every image kept** (files in `docs/art/references/monitor/`). Captions and credits come from the Photojournal (`photojournal.jpl.nasa.gov/catalog/<id>`) or `science.nasa.gov` pages I fetched. "Verified" below = I saw it in the image or read it on the NASA page. "Not verified" = from search summaries only or not found.
**Licence note (all files):** NASA media is generally not copyrighted, but several credits name partner institutions (listed per image). Kept as internal reference only, never shipped in the pack. Check NASA's media guidelines before any wider use. Licence text per image was **not** read beyond the credit line.

## Honest limits first
- **Not found / not collected:** the InSight daily "weather report" graphic (a Wikimedia Commons file named "InSight weather report (sols 1003-1005)" exists per a search listing; Commons rate-limited me, so **not opened, not verified**). The live Curiosity weather page (`mars.nasa.gov/msl/weather/`) was **not retrieved**. No MEDA daily-report page, no Mars Climate Orbiter dashboard, and **no photo of JPL mission-control telemetry screens** (the one team photo I found, `science.nasa.gov/resource/insight-team-reacts-to-first-picture/`, shows monitor backs and identifiable staff, so I dropped it). So **Ls display, wind-rose displays and engineering power screens are not covered by images**; the cues for them below are marked not verified.
- Six of seven images are **science graphs**, not live consoles. They show how NASA draws weather; they are not pictures of operator screens.

## The references

### R1. PIA16912: REMS pressure, daily max and min, sols ~15 to 200 (verified)
- **Source:** `https://photojournal.jpl.nasa.gov/catalog/PIA16912`; file from `assets.science.nasa.gov/.../5205_PIA16912-full2.jpg`. Credit: NASA/JPL-Caltech/CAB(CSIC-INTA)/FMI/Ashima Research (same credit family as PIA16913; the PIA16912 page itself I did not open, **credit line not verified for this id**).
- **Shows:** one panel, white background, title "REMS Pressure Sensor. Daily max. and min. variations". X = Sol (0 to 200), Y = "Pressure Pa" (650 to 1000). Each sol is one **vertical blue bar from daily minimum to daily maximum**, with dot end-caps; dotted grid.
- **Typography/colour:** plain sans-serif, black text, single blue series. Scientific-plot look.
- **Values/trend/units:** unit is **Pa** on the axis label. Trend is the climb of the whole band (about 700-780 up to about 860-960 Pa). The bar length is the daily swing. No error bars.
- **Learns:** Mars air pressure is hundreds of pascals (under 1 kPa), it changes **every day and across the season**, and "one number" hides a daily range.

### R2. PIA16913: REMS air and ground temperature, daily max and min (verified)
- **Source:** `https://photojournal.jpl.nasa.gov/catalog/PIA16913`, added 04/08/2013. Credit: NASA/JPL-Caltech/CAB(CSIC-INTA)/FMI/Ashima Research.
- **Shows:** two stacked panels (air, ground), same layout as R1. Y = Temperature (deg C), -100 to 0+; X = Sol 0 to 200. Daily bars from min to max.
- **Learns:** huge daily swing (air roughly 0 down to about -70 C in the image), and **ground and air differ**; ground swings more.

### R3. PIA16914: vortex (dust devil) detected by REMS, four panels (verified)
- **Source:** `https://photojournal.jpl.nasa.gov/catalog/PIA16914`. Credit as R2.
- **Shows:** title in bold caps "VORTEX DETECTED BY REMS". Four small graphs: pressure in **Pa** vs time in seconds (a sharp dip of about 1 Pa near 798 Pa), a histogram of pressure-drop size, UV intensity (W/m2), and raw wind-sensor data. Each has a **legend box** and light grid.
- **Learns:** a dip in a pressure trace is an **event**, and several instruments are read together to explain one cause.

### R4. PIA16915: relative humidity and temperature, dual axis (verified)
- **Source:** `https://photojournal.jpl.nasa.gov/catalog/PIA16915`. Credit as R2.
- **Shows:** bold title "ATMOSPHERE RELATIVE HUMIDITY". Blue line = max relative humidity (left axis, %), red line = temperature at that moment (right axis, deg C), X = Sol. **Vertical coloured lines mark events** ("Leave blast zone", "Arrive at Rocknest", ...) with a legend.
- **Learns:** two values with separate axes; **event markers on a time axis explain changes** (the pattern our UI spec already wants: cause lines labelled on the graph, `docs/design/ui-specs.md` §1).

### R5. PIA20600: "What's Mars really like?" infographic (verified top and middle; bottom seen in the crop)
- **Source:** `https://photojournal.jpl.nasa.gov/catalog/PIA20600`, added 05/11/2016. Credit: NASA/JPL-Caltech/CAB(CSIC-INTA). Downsized to 1000 px wide.
- **Shows:** three charts on a pale photo background. (1) Temperature **ranges as floating vertical bars per month, coloured by a warm-to-cool gradient** (yellow top, blue bottom), compared with Los Angeles bars (orange) on a **second axis in deg F** on the right. (2) Water content (red, ppm) and relative humidity (blue, %) as filled area ranges. (3) Pressure as a **blue ribbon in mbar** (about 6.5 to 10 mbar axis), labelled "driven mainly by global trends, with some local amplification". A **season strip (Spring, Summer, Autumn, Winter)** sits under the X axis, with sols (0 to 1200) as tick labels.
- **Typography:** large bold sans titles, grey subtitle, bold blue and red axis numbers colour-matched to their series.
- **Learns:** a comparison to something familiar (Los Angeles) makes Mars numbers meaningful; seasons repeat; pressure here is about 1 percent of Earth's.
- **Note:** this graphic uses **mbar**, the REMS graphs use **Pa**. Real sources mix units.

### R6. PIA25284: "InSight Power Comparison" (verified)
- **Source:** `https://science.nasa.gov/resource/insights-power-generation-after-landing-and-spring-2022/` (the image is PIA25284). Credit line **not read** on the page (NASA/JPL-Caltech presumed, **not verified**).
- **Shows:** black background, white title. Two **vertical bars**: cyan "LANDING" up to 5000 WHr, small orange "NOW" at about 500 WHr, axis "Power generated per day (WHr)" with ticks at 0, 500 and 5000. A photo of the clean array sits left, the dusty array right.
- **Learns:** dust cuts solar energy about tenfold; the **units are energy per sol (Wh)**, not instant power. Strong direct match to our solar panel dust state and the Monitor's "units/sol" power line.
- Numbers: 5,000 to 500 Wh per sol as read from the image; matches the figures in the search summary (not independently sourced further).

### R7. PIA25657: Perseverance dust devil, stacked time readout (verified)
- **Source:** `https://photojournal.jpl.nasa.gov/catalog/PIA25657`, added 12/13/2022. Credit: NASA/JPL-Caltech/LANL/CNES/CNRS/INTA-CSIC/Space Science Institute/ISAE-Supaero. (Video thumbnail, downsized.)
- **Shows:** three stacked strips on one shared time axis: raw camera image, processed image with a colour overlay and a "50-60 m away" scale bar, a **dark-background line "Atmospheric pressure"** with dot per sample, and an audio waveform labelled "No sound yet...". This is the closest thing here to a dark **display** look. The graph has **no numbers on the axes** in this frame.
- **Learns:** a pressure dip lines up with a visible event; instruments are combined.

## Cross-cutting patterns (verified from the images above)
| Pattern | Seen in |
|---|---|
| X axis is **Sol**, labelled "Sol" with plain numbers | R1, R2, R4, R5 |
| **Min-to-max bar per sol** instead of one value | R1, R2, R5 |
| Unit on the axis label, **Pa** for science plots, mbar in the public infographic, deg C primary and deg F secondary | R1, R2, R5 |
| Separate panels for separate quantities, one scale each | R1 to R3 |
| Event markers or lines on the time axis | R4, R7 |
| Season strip under the sol axis | R5 |
| Comparison to Earth | R5 (Los Angeles), R6 (landing vs now) |
| Colour-matched axis numbers on dual-axis charts | R4, R5 |
| Light backgrounds for science figures; black background only in the outreach bar chart and the video frame | all |
| **No status lights, battery gauges, Ls values or uncertainty bars** in anything I collected | gap |

## Design cues for our Monitor (recommendations; the Lead Game Designer and Lead Artist decide)
Cues marked [V] come from the verified images; [NV] are common practice I could not source from an image this round.
1. **Show the daily swing, not only a mean.** A row could show a min-to-max span for the sol (R1, R2) [V]. Honest-simplification note: our simulation may only have one value per sol, so a band would be a game invention, **label it if used**.
2. **Sol as the time axis, with "Sol N" in the header** (R1 to R5) [V]. Ls (solar longitude, the season angle): **not collected**; real REMS reports give it, but I did not see one this round [NV]. Our season strip (R5) could stand in; Ls is a candidate "read more" item only.
3. **Units next to every value, matching the source style:** Pa for pressure in the Phase 1 small-pressure range (our values are kPa; 0.6 kPa = 600 Pa is the scale of R1, a useful bridge) [V]. Check against the formatting helper rule in `ui-specs.md`.
4. **Sparkline or 10-sol history per row**, drawn as a line with dots like R7 or a band like R5 [V]. Our UI spec already has the 10-sol graph on row click.
5. **Event markers on the graph:** mark "Outgasser placed", "dust storm" like R4's coloured vertical lines [V]. This is B1 (cause shown next to effect).
6. **Dual-axis only with colour-matched numbers** (R4, R5) [V]; for 12-year-olds prefer two small panels (R2) over one dual-axis chart.
7. **Season strip** under the history axis (Spring to Winter), if the sim has seasons (R5) [V].
8. **Earth comparison line**, e.g. "Earth sea level: 101 kPa" beside pressure (R5 Los Angeles idea) [V as idea; the number is standard physics, not sourced here].
9. **Power as energy per sol with a before/after bar** (R6): our dusty panel could show "Making X, up to Y" as a bar with a 0 baseline and an axis maximum [V]. Bar plus number, never colour only.
10. **Battery state-of-charge bar, status lights, solar-array output readout:** no real image collected [NV]. Use ordinary instrument-panel practice (bar plus percent plus word) but do not claim it copies a real screen. A real source for these is an open task (see handoffs).
11. **Typography:** bold sans titles, plain axis labels, numbers colour-matched to their series (R5). Real displays are not pixel-fonted; our pixel font is a game choice.
12. **Uncertainty:** real graphs here show it only by the **range** of daily values, not error bars. Our Monitor can do the same and avoid false precision [V]. The TWINS sensor accuracy figure (about 6 C, per a NASA data-archive summary) was **not opened, not verified**.

## What a 12 to 18 year old would take from these
Mars air is thin and measured in hundreds of pascals; it varies daily and by season; the temperature swing is huge; dust cuts solar power a lot; and scientists explain a change by **marking what happened** on the graph. All of this supports A1, B1 and C3 (goal IDs as used in `ui-specs.md`; I did not re-check them in `LEARNING.md`).

## Science accuracy notes for the journal (from what I verified)
- Pressure at Curiosity's site in R1/R5 is about 700 to 1000 Pa (7 to 10 mbar), and **varies with season and with elevation** (Gale Crater is low, so it reads higher than the global average). Our 0.6 kPa starting value is the same scale; the global mean value should be cited from a source before any journal text uses it (**not verified here**).
- The real pressure cycle (R5) swings with seasons because CO2 freezes onto and leaves the polar caps. That is stated in standard Mars references but **not on the pages I opened**; mark **not verified** until cited.
