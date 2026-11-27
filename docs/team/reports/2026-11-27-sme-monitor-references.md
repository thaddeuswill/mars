# SME report: real Mars monitor and weather readouts (2026-11-27)

## 0. Handoffs received
Task from the owner via the coordinator: collect real reference imagery for the Planetary Monitor and describe it. Accepted. Partly met: see Risks (no mission-control screen photo, no live weather page, no InSight daily report graphic).

## 1. Done
- 7 images downloaded and each **looked at** (`docs/art/references/monitor/`): PIA16912 (REMS pressure, Pa), PIA16913 (air and ground temperature), PIA16914 (vortex, 4 panels), PIA16915 (humidity, dual axis, event lines), PIA20600 (Curiosity seasonal infographic, downsized), PIA25284 (InSight power bar, 5000 vs 500 Wh), PIA25657 (dust devil stacked pressure strip, downsized). Two candidates rejected after viewing (PIA24664 is a mole photo, PIA26574 is a selfie, no readout); a team-at-screens photo dropped (screens not legible, identifiable people).
- `docs/art/monitor-reference-audit.md`: per image what it shows, layout, type, colour, units/trend/uncertainty, what a learner takes away, a cross-pattern table, 12 design cues marked [V] verified or [NV] not verified, and journal accuracy notes.
- `docs/art/references/README.md`: section for the new folder.
- Ran: downloads, image viewing, page-text extraction. Not run: any game code.

## 2. Decisions needed
1. Should the Monitor show a **daily min-max band** per value? Ill-structured (needs Game Designer, Technical Lead). Recommendation: no for now; the sim has one value per sol, a band would be invented. Keep the 10-sol line with event markers.
2. Pressure unit in Phase 1: kPa (current) vs Pa (matches real REMS graphs). Well-structured once the formatting-helper rule is checked. Recommendation: keep kPa, add "600 Pa" in read-more.

## 3. Handoffs
> **To:** Lead Artist · **Problem type:** ill-structured
> **Problem:** Which cues in audit "Design cues" 4 to 9 to adopt in the Monitor look.
> **Context:** `docs/art/monitor-reference-audit.md`. Real displays are light-background science plots; only R6/R7 are dark.
> **Needed from them:** pick the look (science-plot vs console), within the pixel-font choice.
> **Done when:** a mock-up uses the chosen cues.

> **To:** Lead Game Designer · **Problem type:** well-structured
> **Problem:** Confirm event markers (cue 5) and a season strip (cue 7) fit the sim (are there seasons?).
> **Done when:** yes/no per cue.

> **To:** Producer (owner's call) · **Problem type:** well-structured
> **Problem:** Gaps: battery state-of-charge, status lights, solar-array output screens, Ls display, InSight daily report, mission-control screens.
> **Context:** not found on allowed hosts via search; Wikimedia Commons rate-limited me; `mars.nasa.gov/msl/weather/` not fetched.
> **Needed from them:** permission to retry Commons later, or the owner supplies a source.
> **Done when:** one real image per gap, or the gap is accepted.

## 4. Risks
- Credits name partner institutions (CSIC-INTA, FMI, Ashima, CNES...). Internal reference only; do not ship.
- PIA16912 and PIA25284 credit lines were not read on their own pages (marked not verified in the audit).
- Cues 10 and 12 are not backed by an image; do not describe them as "like real NASA screens".
- Sol-based daily band could imply data the sim does not have (false precision).

## 5. Shared model updates
- Lesson: NASA's public weather graphics show **ranges per sol**, not single values, and use Pa on science plots and mbar on outreach graphics. None for decisions.
