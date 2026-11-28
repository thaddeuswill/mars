# Game Designer report: emitter wear, Hydrosphere, late-gate unlocks (2026-11-28)

Full design: `docs/design/phase3-hydrosphere-and-drift.md`. Docs only. Numbers are INVENTED or calc (scratch Python, not run in the game). I read the SME loss-term doc and report, the Technical Lead report, the rates doc, the sheet and the gate and parameter JSON. I walked no playtest.

## 0. Handoffs received
- SME: rewrite the emitter-wear event and update the sheet. **Accepted** (option 1 plus a bite via margin; section 1 of the design doc).
- Technical Lead: hydrosphere definition and extractor rate; decide wear rewrite. **Accepted.** Lang keys and the CR-T7 check are not mine.

## 1. Done
- `docs/design/phase3-hydrosphere-and-drift.md` (new): wear event, Monitor wording, margin cue, tuning knobs; Hydrosphere definition and curve; unlocks per gate; next 3 slices; conflicts.
- `docs/design/balance-sheet.md`: drift row (wear), hydrosphere curve row and Phase 3 row, two rows in section 2d. Nothing else touched.
- Key finding (calc): one emitter settles at 8.75 C, so losing one of two emitters changes nothing; the old -1 C/sol event cannot work. The "rest at 5.0 C" slip is really (a) one emitter on under 94.5 % power, and (b) rounding 4.96 to "5.0" on the Monitor.

## 2. Decisions needed
1. **Wear event bite (ill-structured: realism vs. fun).** Recommend: half of running emitters (rounded up) burn out, once per Equilibrium hold; slow, rewards a spare emitter. Alternative: lose all emitters (bites every player, less honest); drop it (loses F10).
2. **Hydrosphere is a machine tally (ill-structured: honesty vs. cost).** Recommend yes; a block count needs terrain and fluid work and breaks the pause rule.
3. **Hydrosphere gate arrives about 5 sols later (sol 51, not 46) and the extractor adds 8 units/sol (about 2 more panels).** Well-structured; recommend accept.
4. **Heater coil recipe: 1 iron + 1 coal (no copper).** Well-structured; recommend accept; also R1 (copper) is still open for processors.

## 3. Handoffs
> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** build slices A (water), B (air we make), C (hold and finish).
> **Context:** design doc section 3; hydrosphere loss reuses the temperature loss code (baseline 0, k 0.01); `gates/hydrosphere.json` does not exist.
> **Needed from them:** slice A first; Monitor margin row with floor rounding on gate rows; loss-term source id for F10.
> **Done when:** a boot check reaches Thaw, places an extractor, sees +0.3 %/sol, Hydrosphere latches near 10 %.

> **To:** Subject Matter Expert · **Problem type:** well-structured
> **Problem:** source the real water inventory and the loss direction; check the heater coil recipe and the 30 % target label.
> **Context:** design doc 2.5. **Done when:** four items marked verified or NOT VERIFIED.

> **To:** Lead Instructional Designer · **Problem type:** well-structured
> **Problem:** F10 `resolve_source` and `reveals` on the leak cause line; CR-T7 on the new wording.
> **Context:** design doc 1.3 and 1.4; `docs/design/messages/README.md` row F10. **Done when:** README row updated and wording accepted.

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** hydrosphere visual stages (puddles, ponds, lakes) and the broken-emitter lamp shape. **Done when:** a keyframe list exists.

## 4. Risks
- Wear event is a slow event in a game of 1-sol events; first playtest may call it dull. Fallback: all emitters at once.
- Slice B (CO2-to-O2 swap) is a new mechanism; Breathable and Equilibrium stay dead until it lands. Biomass 1,500 needs 75 vats, so the flora path or a lower target is needed.
- A drift event engine does not exist (only storms).
- All curves unplayed; extractor rate, k and draw INVENTED.

## 5. Shared model updates
- New term proposals: **Hydrosphere (stored)**: machine tally of liquid-water surface percent, with a loss term; **heater coil failure** = the rewritten emitter wear event.
- Lesson: for any slow stored value, check the one-machine equilibrium (rate / k) before sizing an event; it shows which failures change nothing.
- Decision Log entries to propose (owner confirms): Hydrosphere is a machine tally; temperature events are exempt from the "out of band in 1 sol" rule; F10 resolves on the leak source.
