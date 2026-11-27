# Codex review of the Monitor reference images

## Handoffs received

CX-5, [SME audit](../../art/monitor-reference-audit.md), and the seven [reference images](../../art/references/monitor/) on the shared branch. I opened and inspected all seven JPEGs. This review checks visible evidence. It leaves NASA captions, attribution and licensing to the original source review.

## Done

| Reference | Supported by the image | Qualification or unsupported claim |
|---|---|---|
| [R1](../../art/references/monitor/PIA16912.jpg) | REMS pressure in Pa against Sol, with blue daily minimum-to-maximum bars and a rising band. | The first band is closer to about 690–780 Pa. The bars show observed spread, which provides no sensor uncertainty estimate. |
| [R2](../../art/references/monitor/PIA16913.jpg) | Stacked air and ground temperature plots in °C, daily extremes and a wider ground swing. | The stated air range is approximate. Some early lows are near −80 °C. |
| [R3](../../art/references/monitor/PIA16914.jpg) | Four panels with legends and grid lines, including a roughly 1 Pa dip around time zero, an event-size histogram, UV and raw wind traces. | The plots illustrate aligning sensor signals around an event. A general causal inference method is outside the image’s scope. |
| [R4](../../art/references/monitor/PIA16915.jpg) | Blue humidity and red temperature traces, two axes, and colored vertical lines keyed to rover events. | Axis numerals are neutral, so this example does not support the claim about color-matched axis numerals. The plot labels events, with no claim that each marked event caused the plotted change. |
| [R5](../../art/references/monitor/PIA20600.jpg) | Gale and Los Angeles temperature bands, °C/°F scales, red water and blue humidity ranges with matched axis colors, a pressure ribbon in mbar, and season strips on the temperature and pressure panels. | Temperature range bars are spaced across a seasonal timeline. The month labels follow the Los Angeles comparison, and this is not a bar for every sol. The Earth pressure percentage and elevation effect are not established by the graphic. |
| [R6](../../art/references/monitor/PIA25284.jpg) | Dark before/after chart, dusty and cleaner panels, and approximately 5,000 versus 500 Wh on an axis labeled per day. | This is a Mars mission before/after comparison, not an Earth comparison, live telemetry or battery gauge. Use “about one tenth as much daily energy” for the pictured observation. The graphic does not supply a general dust-to-output rule. |
| [R7](../../art/references/monitor/PIA25657.jpg) | Raw and processed views, pressure with sample dots, and an audio waveform in aligned bands. The pressure trace dips and has no numbered axes. | There are four visible bands and no explicit event marker of the kind shown in R4. “50–60 m away” annotates distance to the feature. It is not a calibrated scale bar. |

### Design cue check

| Cue | Evidence and limit |
|---|---|
| Show daily swing | Supported by R1 and R2 when minimum and maximum samples exist. R5 supports seasonal ranges. |
| Sol axis and header | R1, R2, R4, and R5 support a sol axis. The live “Sol N” header is a game design choice. R3 uses seconds. No image shows Ls or a wind rose. |
| Units | Pa, mbar and °C are visible as axis units. Repeating units next to every game readout is a design choice. The owner selected kPa for the in-game Monitor. |
| History graph | R7 supports a dotted pressure trace and R5 a pressure band. The 10-sol window and per-row interaction are game design choices. |
| Event markers | R4 supports colored vertical markers and a key. R7 supports aligned channels. |
| Dual axis and small panels | R4 supports dual axes, R5 water/humidity supports matching axis colors, and R2 supports stacked small panels. |
| Season strip | Supported by R5. |
| Earth comparison | R5 supports a familiar Earth example for temperature. These images supply no Earth pressure reference line. |
| Energy bars | R6 supports daily energy bars with a zero baseline. |
| Battery, status lights and live power output | Unsupported by all seven images. R6 shows a retrospective energy chart. |
| Type and dark panel | Bold sans-serif and clear labels are visible. R6 and R7 support dark graphical treatments. These images provide no evidence about the fonts or colors of live operator consoles. |
| Uncertainty | The daily min–max and seasonal bands show variation. None of the seven images identifies those bands as measurement uncertainty or provides the cited sensor accuracy. |

## Decisions needed

None for CX-5.

## Handoffs

Use R4 for the Outgasser event-marker pattern and R7 for the dotted pressure trace. Describe the battery bar as a project design choice.

## Risks

Keep measured variation distinct from uncertainty and daily energy distinct from instantaneous power. Keep the public science charts and video frame identified by their actual formats.

## Shared model updates

The references support a Monitor inspired by scientific plots. Mission-control styling remains a design choice. No game build or test ran for this image review.
