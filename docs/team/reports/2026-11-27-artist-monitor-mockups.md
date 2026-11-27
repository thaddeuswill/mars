# Lead Artist report: Monitor mockups (BACKLOG #37), 2026-11-27

Long version is `docs/art/monitor-mockups/README.md`. Short summary and the standard sections below.

## 0. Handoffs received
None as handoffs. Inputs read: `AGENTS.md`, the monitor reference audit, the SME references report, `ui-specs.md` §1 and §9, the UI skills review. Accepted the audit's rule: only [V] cues are presented as real; the battery bar and status lights are [NV] and are labelled invented.

## 1. Done
- Three PNGs (1280 x 720, GUI scale 3), real game backdrop, same data as the real screen: `docs/art/monitor-mockups/A-dark-console-sparkline.png`, `B-light-science-plot.png`, `C-hud-strip.png`.
- `docs/art/monitor-mockups/README.md`: borrowed cues per reference, pros and cons, Technical Lead build lists with honest history cost, checks, recommendation.
- Source: `docs/art/monitor-mockups/src/` (`gen.py`, `shot.js`, `contrast.txt`). Fonts (Pixelify Sans, JetBrains Mono) are **not** committed; they were loaded from `/mnt/skills` and `/usr/share/fonts`.
- **Verified:** I looked at all three final PNGs (read as images). Fixed visible overlaps and clipping before final (first B overlapped the crosshair; digit "5" showed as "S" in the pixel font, so digits use a monospace face). Contrast measured by script: all text at least 4.5:1 (lowest 5.28 orange on paper). Sizes: A 196x128, B 194x124, C 414x88 GUI px.
- **Not verified:** in-game rendering and real font; warn/fault light states and falling trend (not drawn); colour-blind simulation; any real player. Mockups are flat HTML, not Minecraft output.
- Backdrop boiling point 14 C computed from the shipped Antoine data, not read from a real screen at 1.60 kPa. The history series (Sol 6 to 16) is **invented** example data.

## 2. Decisions needed
| Decision | Type | Recommendation |
|---|---|---|
| Which option (A, B, C) or a mix? | Ill-structured (look vs build cost vs learning) | A as base, battery bar from C, no status lights yet, Pa only in the journal |
| Pay for a stored per-sol pressure history? | Ill-structured (scope) | Yes, one slice; fallback is C without lights first |

## 3. Handoffs
> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** If A or B is chosen, the Monitor needs a stored per-sol pressure series and event list.
> **Context:** No history found in `mod/src/main/java/mars/planets` or `mars/monitor` (grep). Costs and build list in `docs/art/monitor-mockups/README.md`.
> **Needed from them:** an estimate and a persistence plan (ring of last 11 doubles, event list, packet change, tests).
> **Done when:** the Monitor can draw 11 stored points and events after a world reload.

> **To:** Lead Game Designer · **Problem type:** ill-structured
> **Problem:** Option C's status lights (Power, Outgasser, Dome) have no defined states.
> **Your view:** lights are invented (no real source); useful only if the rules are simple and each state has a word and a shape.
> **Needed from them:** state rules, or a decision to drop the lights.
> **Done when:** each light has a rule table and text keys.

> **To:** Subject Matter Expert · **Problem type:** well-structured
> **Problem:** Confirm Pa vs kPa display for a 12-year-old and whether a real source exists for battery or status displays (audit cue 10 is still not verified).
> **Done when:** a verdict on the Pa line and a source or "no real source" note.

## 4. Risks
- Pixel-font digit legibility (5 vs S, 0 vs 8) in the final game font: check before Stage 3 font choice.
- Option B's Pa/kPa mix and a light panel in a dark HUD.
- Mockups use backing alpha 0.97, code uses 0.94.
- History series invented; the real series will be a straight line only while one outgasser runs.

## 5. Shared model updates
None to decisions. Lesson: when mocking a Minecraft UI with web fonts, test digits first; some pixel fonts make 5 look like S.
