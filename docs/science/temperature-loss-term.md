# Temperature loss term: why a warmed Mars cools again, and a game rule

**Author:** Subject Matter Expert · **Date:** 2026-11-28 · **Status:** recommendation; numbers are **fluid** and **INVENTED** · **Closes:** `docs/design/phase2-machine-rates.md` C-2 (emitter wear has no loss term) · **Goals:** B2, C2, A3, D2
Tags: **VERIFIED** (quoted line from a page I opened this session), **NOT VERIFIED** (from memory or a secondary source; not for game text), **calc** (my arithmetic, scratch Python, not run in the game), **INVENTED** (game balance).

## 1. The problem in one line
`pack/data/mars/parameters/temperature.json` is a stored value with only sources (emitters, albedo) and no loss. Nothing can lower temperature, so "Emitter wear: temperature -1 C/sol" cannot work, and "turn the emitters off" does nothing. Pressure already has sinks (polar frost); temperature needs one.

## 2. Why a warmed Mars would cool again (real science)
| Reason | Plain version (grade 6-7) | Status |
|---|---|---|
| Heat always leaks to space | A warm planet glows in infrared and loses heat. Greenhouse gases only slow that leak. Take them away and it speeds up. | Basic physics; matches accuracy-review B4 (Mars is about 210 K now). |
| The gases do not last | Factory gases break down (sunlight) or must be made again. | **VERIFIED (abstract only):** Gerstell et al. 2001, PNAS 98:2154: "Martian mining requirements for replenishing the fluorine could be comparable to current terrestrial extraction." (PubMed 11226208, opened via eutils.) Lifetime numbers are in the full text, which the proxy blocked (pmc.ncbi.nlm.nih.gov: 403). |
| Thin air holds little heat | Mars has little air and dry ground, so it swings between warm and cold fast. | Qualitative; **NOT VERIFIED** with a number here. |
| Ice-albedo works in reverse | If it re-freezes, bright ice returns and reflects sunlight. | Already in DESIGN.md as small (<= 2 C cap, round2-review 1.3). Game: **not** modelled in reverse (simplification, section 6). |

**Real lifetimes of super-greenhouse gases (what the literature says).**
- Proposals use fluorine gases such as C3F8 (Marinova, McKay and Hashimoto 2005; cited in accuracy-review [20], 33.5 K of warming from 1 Pa) and a mix of five to seven fluorine compounds (Gerstell 2001, VERIFIED in the abstract above).
- Fully fluorinated gases are very long-lived on Earth: I recall thousands to tens of thousands of years for CF4 and C2F6 (IPCC tables). **NOT VERIFIED**: I could not open IPCC or the full texts. Do not put these numbers in game text until checked.
- On Mars the ultraviolet (UV) light that destroys such gases is stronger at the surface because the air is thin, and the Gerstell paper's window gases were chosen with that in mind. **NOT VERIFIED** (full text blocked). round2-review F10 already says "break down slowly in Mars's strong sunlight, so they would need to be made all the time"; that line rests on [S24] and should be re-checked against the full text before release.
- So: the real answer is "slow loss over centuries, with a need to top up", not "gone in sols". The game compresses time (next section).

## 3. Game rule (recommended): a restoring term toward the cold baseline
**Form.** Each sol, after the emitter, albedo and other sources are added:

`temperature -= k x (temperature - T_base)`

with `T_base = -60 C` (the starting value; the "no warming" state) and **k = 0.004 per sol (INVENTED)**. This is a leak that grows with how far Mars is warmed: a warmer Mars leaks more. It cannot push below -60 C. It is one number, learned once (B1).

**Emitter rate retune (needed).** With a loss, the old 0.25 C/sol per emitter reaches 0 C at sol 56 (calc). Raise the emitter to **0.275 C/sol (INVENTED)**: 0 C at about **sol 52**, Thaw (-20 C) at about sol 38 (calc, standard build schedule of phase2-machine-rates 1.2, with the 2 C albedo spread over 18 sols from the Thaw). Without the loss term the same schedule gives Thaw at sol 39 and 0 C at sol 53 at 0.25.

| Emitter rate | k | Thaw sol | 0 C sol (calc) |
|---|---|---|---|
| 0.25 | 0 (today) | 39 | 53 |
| 0.25 | 0.004 | 41 | 56 |
| **0.275** | **0.004** | **38** | **52** |
| 0.29 | 0.005 | 37 | 51 |

**Why this k.** At 0 C the leak is 60 x 0.004 = **0.24 C/sol, about one emitter**; at 5 C it is 0.26. So holding the Phase 3 band needs about one emitter running, which matches the balance sheet ("1 to 2 emitters run and the rest are switched off", phase2-machine-rates section 1.4). A second emitter is the margin.

**What the player sees when emitters stop (calc).**
| Start | After 1 sol | 5 sols | 7 sols (a Breathable hold) | 14 sols |
|---|---|---|---|---|
| 5 C | 4.74 | 3.71 | 3.20 | 1.45 |
| 0 C | -0.24 | -1.19 | -1.66 | -3.27 |

At -0.25 C/sol the Monitor's one-decimal reading changes within one sol, and a clear drop shows in 3 to 5 sols: slow enough that nothing is lost in a short break, fast enough to notice and act. Time to fall from 6 C to below 5 C with no emitters: about 4 sols.

**Honest scale note.** 1 / k = 250 sols, about 83 hours of play. The accuracy review says McKay estimates about 100 years of warming (partly verified), and Phase 2 is 54 sols, so 1 sol is roughly 2 years (calc). On that scale 250 sols is about 500 years: the same order as a long-lived gas. The match is a coincidence of tuning, not a measurement. Say "game rule, sped up".

## 4. Monitor and journal text
- **Cause line (Monitor, temperature row):** "Warming fades without emitters." Detail line: "Heat leaks to space: -0.26 C/sol. Emitters: +0.55 C/sol." (Live numbers, grade 6-7.)
- **Journal, middle school:** "Greenhouse gases slow down how fast a planet loses heat. Stop making them and the heat leaks away, slowly. Real factory gases last a very long time, but someone has to make them first."
- **Journal, high school:** "Surface temperature balances heat in against heat out. Greenhouse gases reduce the heat out. If they are removed or break down, Mars cools toward its old balance. The game shows this as a steady leak that grows as Mars gets warmer. It is simplified: real cooling depends on the gases, dust, ice and ground."
- **Earth link (D2):** "On Earth, cutting emissions slows warming, but CO2 lasts so long that the planet stays warm for centuries." (**NOT VERIFIED** here: check IPCC before use.)
- Failure report for Emitter wear: "Temperature is falling because an emitter stopped. Repair it. The leak keeps going as long as Mars is warm."

## 5. Effect on hold gates and Phase 3
| Item | Effect | Recommendation |
|---|---|---|
| Hold rule (pause; reset after more than 1 sol out) | Cooling is slow, so a player who rests exactly on 5.0 C drops out in under a sol with no emitter, and a reset needs more than a sol out. | Players need a rest margin: run 2 emitters and sit at about 6 C. Telegraph with a Monitor "margin" line. |
| Breathable (7 sols), Equilibrium (14 sols) | Temperature is the **slowest** drift variable, so it gives warning time (good for fairness). 14 sols with 0 emitters from 6 C ends near 2 C (calc). | Keep holds as is. Do not scale k with holds. Physical thresholds still never scale. |
| Recovery | After a lapse, 2 emitters add +0.55 against a 0.26 leak, so about +0.3 C/sol: 1 C back in about 3 sols. | Acceptable; one extra emitter is the answer. |
| Phase 3 power | 1 to 2 emitters (3 to 6 units/sol) become a permanent small draw. | Matches phase2-machine-rates 1.4; no change. |
| Drift event "Emitter wear" | Old size (-1 C/sol) is wrong. A lost emitter removes 0.275 C/sol of forcing; the temperature then falls only by (leak minus remaining forcing). With 2 running, the net stays slightly positive: no drift. | **Rewrite the event** as below. |

**Rewritten event (INVENTED, for the Game Designer).** "Emitter wear: one emitter fails. Effect if ignored: temperature falls about 0.25 C/sol while the leak exceeds the remaining emitters (with one emitter left at 5 C, net -0.0; with none, -0.26). Out of band in 2 to 4 sols if you rest near 5.5 C." Honest consequence: it is a **slow** event, unlike pressure and gas events. Options:
1. Keep it slow, as the real physics (recommended; it teaches that heat has inertia, B2).
2. Make it bite: the event also **stops two emitters** (a grid fault) or hits when only one is running.
3. Drop it and use "Weather low" for the temperature story.
Both new numbers (0.25 C/sol, 2 to 4 sols) are **calc/INVENTED**, not sourced.

## 6. Simplifications to state in the journal
- One global leak toward -60 C; real loss depends on pressure, dust, ice and gas type (**real science**, ignored).
- CO2 falling in Phase 3 does not cool the game planet (greenhouse from factory gases, already decided; round3-review).
- No reverse ice-albedo (re-freezing does not speed cooling). If added later, cap at the same 2 C.
- k and the emitter rate are invented; the real lifetime is much longer, and it is only the time compression that makes the leak visible.

## 7. Assumptions and what would sharpen this
- k is tuned to the Game Designer's draft schedule (unplayed). Re-tune after the first playtest: if cooling is not noticed, raise k to 0.005 with emitter 0.29; if holds feel punishing, lower to 0.003 with emitter 0.26.
- Needed to firm up the science: Gerstell 2001 full text and an IPCC lifetime table (ask the owner to open them, or a climate-science reviewer).
- Ill-structured parts: how bitey the wear event is (fun vs. realism). Routed to the Game Designer.

## Sources
- Gerstell, Francisco, Yung, Boxe, Aaltonee (2001), PNAS 98:2154-2157, PubMed 11226208 (abstract opened; full text blocked): https://pubmed.ncbi.nlm.nih.gov/11226208/
- Marinova, McKay, Hashimoto (2005), JGR (not opened here; cited via accuracy-review [20])
- `docs/science/accuracy-review.md` B4 and round2-review 1.3, F10 (earlier work; partly verified)
