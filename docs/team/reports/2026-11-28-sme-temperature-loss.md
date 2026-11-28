# SME report: temperature loss term (2026-11-28)

Full science and numbers: `docs/science/temperature-loss-term.md`. Docs only; no code or pack edits.

## 0. Handoffs received
Task from the coordinator, answering `docs/design/phase2-machine-rates.md` C-2. Accepted.

## 1. Done
- `docs/science/temperature-loss-term.md`: why Mars would cool again; real-timescale status (one claim verified, lifetimes NOT VERIFIED); game rule; Monitor and journal text; hold-gate and drift-event effects.
- **Rule (recommended, INVENTED numbers):** each sol `temperature -= 0.004 x (temperature + 60)` (restoring term toward -60 C). Emitter rate raised 0.25 to **0.275 C/sol** so 0 C stays at about sol 52 (Thaw sol 38; calc, scratch Python, not run in game). No-emitter cooling from 5 C: -0.26 C/sol, 3.7 C after 5 sols.
- Holding Phase 3 needs about 1 emitter (leak 0.26 vs 0.275); 2 give margin. Matches the existing "1 to 2 emitters" plan.
- Monitor label: "Warming fades without emitters."
- Sources: only the Gerstell 2001 abstract line was opened and quoted. Lifetime figures (CF4, C2F6) and the full Marinova text were not reachable: NOT VERIFIED, kept out of game text.

## 2. Decisions needed
1. Adopt k = 0.004/sol and emitter 0.275? (ill-structured: realism vs. pace). Recommend yes; retune after first playtest.
2. Emitter-wear event: keep slow (honest, recommended), make it bite with a grid fault, or drop it. Ill-structured; the -1 C/sol size must change either way.
3. Ask the owner to open the Gerstell full text and an IPCC lifetime table to verify lifetimes. Well-structured.

## 3. Handoffs
> **To:** Lead Game Designer · **Problem type:** ill-structured
> **Problem:** Emitter wear (-1 C/sol) needs a rewrite; temperature is a slow variable.
> **Context:** `docs/science/temperature-loss-term.md` section 5; balance-sheet section 4 drift table; phase2-machine-rates C-2.
> **Your view:** options 1 to 3 in section 5; I recommend 1.
> **Needed from them:** pick an option, update the drift table, section 2d rate (0.275), the 3a curve check, and the rest-margin advice for holds.
> **Done when:** balance sheet and rates doc agree with the loss term.

> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** Add a loss term to temperature.
> **Context:** `pack/data/mars/parameters/temperature.json` has none. Proposed data: `loss: { form: "restore", baseline: -60, perSol: 0.004 }`, applied after sources, clamped at baseline. Emitter `ratePerSol` 0.25 to 0.275 once the Game Designer confirms; tests compare rate data to the balance sheet.
> **Needed from them:** a pure-Java term in `mars.index.sim`, unit tests (cooling table in section 3), Monitor cause line.
> **Done when:** with the standard schedule 0 C is at sol 52 +/- 1 in the sim and emitters off cools 0.24 to 0.26 C/sol at 0 to 5 C.

## 4. Risks
- Constants are invented and unplayed; the emitter retune changes a doc the Game Designer owns.
- Slow cooling may make the wear event dull. Hold margin: players resting at 5.0 C can slip out; add a margin cue.
- Real lifetimes unverified; do not add them to journal text yet.

## 5. Shared model updates
- New term: **temperature loss term** (restoring leak toward -60 C, "warming fades without emitters"), INVENTED k = 0.004/sol.
- Lesson: every stored Index value needs both a source and a sink before drift events reference it.
