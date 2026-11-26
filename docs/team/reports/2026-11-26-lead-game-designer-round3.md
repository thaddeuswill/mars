# Report: Lead Game Designer Round 3 (BACKLOG #16)

- **Role:** Lead Game Designer
- **Date:** 2026-11-26
- **Outputs:** `DESIGN.md`, `docs/design/balance-sheet.md` (new §6a hazard thresholds), `docs/design/ui-specs.md`, `docs/design/messages/` (new `story-beats.md`), `docs/design/prototypes/first-dust-storm.md` (new), `docs/design/playtest-plan.md`
- **Note:** Document design plus walkthroughs (Lens A: a 12-year-old new to tech modpacks; Lens B: an experienced 17-year-old). Nothing was playtested. One task is still open: the Phase 3 CO2 budget (#7), waiting on the SME's options.

## Handoffs received (all accepted)
- **Instructional Designer (round 2 and addendum):** guidance conditions 1–4 in DESIGN.md; puddle-drying time set under the 0.25 sol limit; the look-first card when the Monitor opens with an unanswered forecast; a Phase 1 source for the gas mix; "and how will we know it worked?" in question 2; Notice lines in all three Shift Briefings; Hana's "hide" means "not shown again automatically", with reasons in steps 2 and 5; a "Your shift" block in the Keeper's Report; FI-1 emitters draw from the dome's solar-plus-battery grid (storms slow warming); every storm warned at least 1 sol ahead in every phase; drift events start at the first hold or first established flora patch; cause breakdown has a per-phase default and opens by itself when a value leaves its band; locked recipes hidden with one quiet "Unlocks at…" line; Shift log adopted.
- **Technical Lead:** C2 credit goes to the repairer (`actor`).
- **Lead Artist:** ui-specs §5 now says the mast carries gauges fitted at Phase 3; the ice table already used the boiling-point rule; journal notes written (stylized sky, sound, lift balloon, Outgasser puff); hazard thresholds sent (balance §6a, all invented); pre-storm haze allowed with a guard (fog only, starts at the warning, the sun's brightness and solar output can't drop before F1 locks); machine name settled as **ice extractor**; music defaults respected (vanilla music off on Mars, one original ending piece).

## Done (highlights)
- **Balance §6a:** puddle drying 0.1 sol (invented; hard limit under 0.25 sol); suit O2 warning at 25% and critical at 10%; dose bands at 30, 60, 90; atmosphere factor (0.6/p)^0.5 (0.35 at 5 kPa, so F7 shows a clear difference); shielding factors. All invented.
- **Messages:** "Check:" pointers on F1–F4a; F3, F5, F8 now lock 1 sol after the trigger instead of when the Monitor opens; Landing step 3 and a Dome stable journal line give the air mix (about 95% CO2); `forecast.look_first`; final text for the ten essential beats in `story-beats.md` (each ≤ 60 words with `asserts` and fiction notes; the beat 4 warning avoids "solar" so it doesn't answer F1). Essential story text totals 936 words (9.4 minutes at 100 wpm) against a budget of 1,500.
- **Paper prototype** (`prototypes/first-dust-storm.md`): six sheets (materials, timeline with lock-point checks, observation, debrief, pass criteria, root cause); on paper 1 sol = 4 minutes so a run takes about 15.
- **Playtest plan:** checks for repairer credit, banned tokens, guidance levels, look-first, and lock points.
- **Threshold-vs-curve rows** for puddle drying, the radiation atmosphere factor, drift start, and storm lead time. All four pass.

## Decisions needed
None new.

## Handoffs
- **SME:** a plausible real range for puddle drying below 5 kPa (or "real value missing"); check the §6a atmosphere-factor direction; the 3–22 minute light-delay source (beat 19); the Phase 3 CO2 options (#7).
- **Instructional Designer:** review the new text and run the alignment check (story-beats, Notice lines, "Check:" pointers, look-first, Shift log, drift start, cause-breakdown defaults); update the alignment table for repairer credit and the lock-rule changes.
- **Technical Lead:** implement `forecast.look_first`; `asserts` on every beat; emitters on the shared grid; the drift-event start condition; guidance exported; "Unlocks at…" from gate data; the §6a meters.
- **Lead Artist:** join the first-dust-storm joint pass with the Instructional Designer.
- **Producer:** add `docs/design/prototypes/` and `messages/story-beats.md` to the DOCUMENTATION.md map; schedule the joint pass.

## Risks
- The Phase 3 CO2 budget is still open, so the Phase 3 curve is incomplete.
- F4a depends on one invented number (puddle-drying time).
- The radiation numbers are invented; their direction needs the SME.
- Hana's beat 6 message is 57 words, close to the 60-word limit.

## Shared model updates
- **Terms:** look-first card (`forecast.look_first`); "Check:" pointer; Notice line; Shift log; "Unlocks at…" line; re-scaffold (the cause breakdown opens by itself when a value leaves its band); ice extractor (the only name for that machine).
- **Decisions:** pre-storm haze allowed with the guard; music off on Mars with one ending piece; emitters run on the shared grid; drift events start at the first hold or first established flora; hazard thresholds in balance §6a.
- **Lesson:** a warning line can leak a forecast answer just as a screen can. Check every warning text against the forecast it precedes.
