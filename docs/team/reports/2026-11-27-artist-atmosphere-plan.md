# Report: Lead Artist, atmosphere plan (2026-11-27)

Docs only. Plan: [docs/art/atmosphere-plan.md](../art/atmosphere-plan.md). No edits under `mod/` or `pack/`, no binary art. I viewed four W1 screenshots (`tools/screenshots/out/20261127-205929/w1_01, w1_05, w1_07, w1_09`, all 0.6 kPa midday). I ran no build or client.

## 0. Handoffs received

| From | Handoff | Answer |
|---|---|---|
| Technical Lead (S3-1 report) | Storm tint and haze values: hazy at the warning, brown during, fog only before onset so F1 is not answered | **Accepted, done in plan section 5.2** (tint, dim, fog, sun fade, night rule, pre-storm haze values, signal `mars:storm_haze`). Pass-through: needs a wider `SkyPayload` |
| Technical Lead (W1 report) | Final terrain textures and colour view | Not in this run (docs only); my atmosphere view of the terrain is in the plan section 0 |
| Owner | W1 terrain needs more atmosphere | Plan written, first batch recommended |

## 1. Done

- `docs/art/atmosphere-plan.md`: six areas (haze, light, particles and storm, sky, sound hooks, HUD and visor), each with what the player sees, data and code needed, cost class, unverified effort, and a ranked list of 11 items. First batch: **AT-1 storm look, AT-2 far haze retune (data), AT-3 dust motes and wind streaks (vanilla `dust` particles, no new art)**. Sunset band and aureole are last and marked as needing an owner reversal.
- This report.

Findings from the screenshots: haze already fades far buttes; far ground reads darker than the horizon (fog colour darker than horizon colour); nothing moves; `pack/assets/mars/sounds/` and `particles/` are empty; the storm has no world cue.

## 2. Decisions needed

| # | Question | Type | Recommendation |
|---|---|---|---|
| 1 | Approve the first batch AT-1, AT-2, AT-3? | ill-structured (art vs cost, low) | Yes, in that order |
| 2 | Accept earlier fog start at the low end (0.6 kPa: 0.70 to 0.50)? It weakens the fog-closes-with-pressure cue slightly | ill-structured (art vs B1 cue) | Yes; restore the 5 and 15 kPa starts first if the Phase 2 beat feels weaker |
| 3 | The owner's word "storm roar" conflicts with the no-howling-wind rule. Keep rumble plus hiss? | ill-structured (feel vs science honesty) | Yes; SME decides if a roar is honest |
| 4 | Reverse the deferral of the sunset band and sun aureole? | values call | No, not yet; revisit with the first 30 kPa sunset |

## 3. Handoffs

> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** build AT-1, then AT-3, and apply AT-2 values.
> **Context:** `docs/art/atmosphere-plan.md` sections 3, 5.1, 5.2. AT-1: add storm level and storm haze to `SkyPayload`/`SkyState`, new `sky.json` block (`dim` 0.18, `overlay_alpha` 0.12, targets from planetary-keyframes 3.7). AT-2: new fog hex and fog_start values in plan section 3 (I can edit `sky.json` in the next run if you prefer). AT-3: client tick spawner using vanilla `dust` particles, cap 40.
> **Needed from them:** confirm (a) 110 degrees is a compass bearing (0 = north = -Z), (b) a full-screen quad can be drawn under the HUD in 1.21.1, else drop the overlay, (c) a `mars:storm_haze` signal over the last 5 simulation minutes before arrival, with solar output and the Monitor unchanged, (d) texture paths for a Mars sun and moon (AT-4), (e) one screenshot run with a dusk and a night frame at 0.6 and 75 kPa.
> **Done when:** the storm tints the sky and closes the fog in a screenshot; haze and motes seen in a run.

> **To:** Subject Matter Expert · **Problem type:** well-structured
> **Problem:** check the claims the atmosphere plan makes from memory.
> **Context:** plan sections 4, 5.1, 6, 7, 8.
> **Needed from them:** blue sunset (PIA19400, not opened by me); long Martian twilight and its length; wind-driven sand and dust motion at low density (rate direction); dust devils as real (citation); Phobos apparent size (about 0.21 degrees, about 0.6 of the Sun) and Deimos as a point; whether a roar or only rumble and hiss is honest in thin air; whether breath fog on a visor teaches a wrong idea.
> **Done when:** each line is marked verified, corrected, or dropped.

> **To:** Lead Game Designer · **Problem type:** well-structured
> **Problem:** visor dust after a storm needs a decay or wipe rule (AT-9a).
> **Needed from them:** a rule, or "no persistence".
> **Done when:** a decision I can specify.

## 4. Risks

- Fog start change weakens one of the pressure cues; the OKLab step check for the new fog colours is not run yet.
- The storm dim is a screen overlay stand-in; it may look like a dirty window. Fallback is sky and fog only.
- Particle rates and every storm value marked INVENTED are guesses until the owner looks at them.
- No night or dusk screenshot exists; those frames are unseen.
- Sound production is the large cost; the plan has no estimate for it.

## 5. Shared model updates

None. (Lesson for the record: vanilla skylight is independent of sun angle and there are no cast shadows, so sun angle and shadow softness are not shaping levers; the data knob is `ambient_light` only.)
