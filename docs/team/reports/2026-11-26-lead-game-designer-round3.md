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

---

## Addendum: follow-up on the SME's and Technical Lead's round 3 (filed by coordinator)

Design work on documents only; nothing was playtested. No other role's files were changed.

- **Phase 3 CO2 budget: option A adopted** ("CO2 is the oxygen source"), under the owner's standing delegation; no block on appeal grounds. The CO2 and O2 lines now move as a mirrored pair. Outgassers add CO2; Phase 2 processors add mostly filler gas plus some CO2; Phase 3 processors add filler gas only. Plants and algae vats swap CO2 for O2 one for one; decay and fire swap it back; scrubbers turn CO2 into rock and make no O2 (a real choice: scrub CO2 and you lose your O2 source); an O2 generator that splits water is the fallback. "Low CO2" is the last check to go green, which gives Phase 3 a clear final push.
- **Numbers:** the Phase 2 CO2 rise reaches **18.0 kPa** at the Phase 3 gate (invented, inside the SME's 17–19 kPa range). In Phase 3 the O2 push passes 16 kPa at about sol 25 and CO2 reaches ≤ 0.5 kPa at sol 28. A table error was corrected: "Breathable conditions met" at sol 25 was wrong, because CO2 is still 1.6 kPa then.
- **Drift events:** the decay pulse now also lowers O2 (−0.1 kPa/sol). The "polar winter" event is replaced by a **weather low** (nitrogen and O2 can't freeze out at equilibrium). Puddle drying time is now 0.05–0.2 sol, rising with pressure, inside the SME's accepted 0.02–0.25 sol.
- **Labels and wording:** succession timescale, the 5 °C tree line (invented), the mirror journal line, and crop verification; Shift Briefing 2 says ice needs both to "stay" water; Shift Briefing 3 says "Plants turn CO2 into O2."
- **Technical Lead items:** `lock`, `reveals`, `rests_on` and `names` written as one index table in `messages/README.md`; transit tubes confirmed as simple paired stations with no vehicle; Training Hall structure specified (about 15 × 11 × 6 blocks, in the overworld).
- **Threshold-vs-curve check: all rows pass** (CO2 ≤ 0.5 kPa at Phase 3 sol 28; O2 push at sol 25; the weather low is valid at equilibrium; puddle drying below 0.25 sol at every pressure; F8's answer still true under rule A; FI-4 holds because slower uptake against ongoing decay means net CO2 rises). The playtest plan gains an over-scrubbing check: seed biomes, over-scrub on purpose, and still reach the window using the O2 generator.
- **Handoffs:** Instructional Designer (alignment check for A5, B2, B4; retarget FI-2 from "polar winter" to the weather low; check F8 and FI-4 wording); Technical Lead (implement rule A's coupling: O2 gain equals CO2 loss for flora, vats, decay and fire, scrubbers reduce CO2 plus O2, a "carbon in rock" reservoir; the message fields); Lead Artist (build the Training Hall; add a "carbonate rock" reservoir to the Monitor's carbon flow view); SME (confirm the Phase 2 and Phase 3 CO2 curves stay mass-balanced).
- **Risks:** over-scrubbing early can stall O2 unless the O2 generator is reachable and affordable; plants growing at low O2 and tolerating about 18 kPa of CO2 are unverified; only about 3 sols separate O2 ready from CO2 ready.
- **Shared model updates:** terms "CO2 is the oxygen source" (rule A) and "weather low"; decisions rule A with the 18 kPa gate value, and transit tubes as simple paired stations; lesson: breathability is three conditions, and "ready" must be checked against every one, not the headline one.
