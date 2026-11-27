# Lead Artist report: sky review in the real game (2026-11-27)

Task: answer the five points of the Technical Lead's slice 6 handoff (`2026-11-27-technical-lead-slice6-sky.md` section 9). Owner accepted slice 6 and said continue.

## 0. Handoffs received

| Point | Answer |
|---|---|
| (a) five pressure pairs | Accepted; section 2. No colour changed. |
| (b) dome gradient exponent 0.6 | Accepted, confirmed; section 3. |
| (c) night colour over the whole dome | Accepted, with one thing to check; section 4. |
| (d) white vanilla clouds | Leave until E-10; section 5. |
| (e) sunset band and aureole | Definitions written; band stays out, aureole optional; section 6. |

## 1. What I looked at (verified vs not)

Viewed: `tools/screenshots/out/20261127-130736/slice6_a..f` level shots (0.6, 5, 15, 30, 75 and the falling shot), `slice6_a_p0.6_sol0_up.png`, `slice6_e_p75_up.png`. I also sampled pixels with a script. Read `MarsSkyEffects.java` (dome and day/night code) read-only.

Real references: `docs/art/references/` holds hardware and surface photos, **not sky photographs**. The only real sky in them is a thin strip at the top of `PIA25286.jpg` (InSight, after about four years of dust). I sampled it: horizon `#DAAA7C` to `#E0AD90`, falling to `#D1885B` where it meets ground. The README says the 2022 InSight image is strongly orange-processed, so this is a family check (warm salmon-tan haze), not a calibration. `PIA22876`, `PIA18079`, `PIA19164` show no usable sky. I did not view any sunset, night, or high-sky photo, so nothing below is checked against one.

Not verified: night and dusk in game (all shots at time 4500), fog distance by eye, any renderer but vanilla software GL, motion (the 30 s shift), classroom projector or small-screen legibility.

## 2. (a) The five pairs

Seen / intended pixel numbers are in the TL report section 3 (zenith exact, horizon and fog within 2 per channel); I do not repeat them. My judgement by eye:

| kPa | Verdict | Notes |
|---|---|---|
| 0.6 | **Good.** Butterscotch-tan haze, brighter at the horizon, matches the family of the InSight strip, a little paler and less saturated than that processed image. | Overhead the sun has a warm glow; the sun square has a saturated yellow rim (`#FFFFAA` sampled), not the `#F4EBDD` disc of the keyframes. That is the vanilla sun texture, not `sky.json` (handoff below). |
| 5 | **Good.** Clear rose-grey shift from 0.6 in a side-by-side, so the Phase 2 beat reads. | Salmon-rose is stylised (planetary-keyframes 2.3), needs the required label. |
| 15 | **Good.** Mauve; reads as the middle of the path. | The 5 to 15 step is the weakest to the eye (zenith dE 0.047 on paper); sound and fog carry it too. |
| 30 | **Good.** Slate lavender-blue; the first clearly "blue" frame, as intended. | |
| 75 | **Good.** Pale, whitish blue, clearly not vanilla; reads as a reward after 30. The dome shows a faint darker disc at the zenith in the up shot, which is the gradient working. | |
| falling (75 to 5, 1 s) | **Good.** In-between lavender-blue: regression is visible and smooth. | |

Side finding, not sky: a thin dark dashed line runs along the horizon in the 0.6 to 30 level shots (terrain edge of a flat test world, not the dome). It is hidden at 75 by the thicker fog. I report it to the Technical Lead; I did not look into the cause.

**No value in `sky.json` changed** (only its `_invented` comment, below).

## 3. (b) Dome gradient exponent: confirmed, 0.6

Reasoning. Ring elevations are 90, 70, 50, 35, 22, 12, 5, 0 degrees; `t = (e/90)^0.6` gives t = 0.18 at 5, 0.30 at 12, 0.43 at 22, 0.56 at 35, 0.86 at 70. So the horizon colour fades over the first 10 to 20 degrees, then slowly: the same shape as a real clear sky, bright near the horizon and flatter above. The level shots (view spans about 0 to 35 degrees) show a smooth band with no visible ring steps; the up shots show none either. At 0.6 kPa the zenith and horizon keys are close, so the exponent barely matters; at 75 kPa it matters most and it looks right. A larger exponent would push the pale band up the sky and make it look foggy; a smaller one would shrink the band to a hairline. This is still an **invented** choice (no source); the 0.5 to 0.7 range would be indistinguishable to a player. One exponent for all pressures is fine; a per-pressure exponent is not needed.

## 4. (c) Night colour across the dome

Confirmed as a design choice: one night colour for the whole dome and for the fog, mixed in by vanilla's day brightness. Reasons: night keys are dark and nearly neutral (`#0D0B0E` to `#0A0F20`), so a horizon-to-zenith gradient would be invisible; stars and the moon carry the night; and the code mixes dome and fog with the same function (`dayNight`, `SkyClient.onFogColor`), so the horizon cannot show a lit band at night. Unverified: I did not see a night frame. Real Mars twilight is long because dust scatters light after sunset (SME to size; **unverified** here); vanilla's short dusk and the sunrise fan are our stand-in, a deliberate simplification. Ask for one night shot (time about 18000) in the next screenshot run to confirm there is no horizon line.

## 5. (d) White vanilla clouds

Recommendation: **leave until E-10** (agrees with the owner's lean and the TL).

What I saw, which is better than "white at every pressure": distant clouds are tinted by the fog and read tan, pink, lavender or pale blue to match the sky (cloud versus sky samples: 0.6 `#BF9B7B`/`#BB936E`; 75 `#ACC0D8`/`#8EAED8`). Overhead, at 0.6, clouds are neutral grey-white blocks (`#B1A9A2` on `#B58B68`): visible, boxy, and more than the "thin high wisps, 5 %, 0.35 opacity" of the keyframes, but not alarming, and real Mars does have water-ice clouds. At 75 kPa they are what we want anyway.

Cost of a cheap tint, as far as I can tell (mechanism **not verified**, I did not test it):
- Without code: not possible. A resource-pack override of the vanilla cloud texture would tint clouds the same at every pressure (and in every dimension), which is the wrong answer at 75 kPa.
- With code: a pressure-keyed cloud colour and opacity needs a cloud-render override in `MarsSkyEffects`. Hiding clouds below a pressure is a few lines; a proper tint means copying vanilla cloud drawing (about 100 lines, same version-drift risk as the custom sky). I estimate half a day to a day, and it would be thrown away when E-10 replaces clouds. Not worth it.
- A `clouds_density.png` already exists in `pack/assets/mars/textures/environment/` (not examined by me); E-10 should reuse that path.

## 6. (e) Sunset band and sun aureole

Both were not applied; definitions that can be built from data later:

**Sunset halo (built):** keep as is. It already carries the blue-to-orange flip, which is the teaching beat.

**Sunset band (stays out for now; optional polish).**
- Where: the lowest 22 degrees of the dome, on the half of the azimuth away from the sun.
- When: only during the existing sunrise/sunset window, with weight `w = glow strength (vanilla sunrise alpha) x (1 - e/22)^2`.
- How: add `w x 0.6` of `sunset_band` into the ring colour (OKLab or simple linear mix), zero outside the window.
- No new data: the five `sunset_band` hexes already in `sky.json` are the values.
- Why it can wait: the halo fan already shows the colour change at the sun, and at 0.6 to 15 kPa the band is a barely visible `#8A6A55`-ish brown on a tan sky. It earns its place at 30 and 75 kPa (`#B07466`, `#D9714F`), so schedule it with the first time a player sees a sunset at 30 kPa. **Needs a small code change.**

**Sun aureole (optional; one new key).**
- Shape: a soft radial glow quad behind the sun, centre alpha 0.35 falling to 0 at the edge (smooth, no ring).
- Size: radius = disc radius x 2.5 x `aureole_scale`. The 0.66 sun is about 11 degrees wide, so at 0.6 kPa the glow edge is about 14 degrees from the centre.
- Colour: the sun colour of the keyframe (`#F4EBDD` at 0.6, `#FFF6E0` at 75).
- Data: `aureole_scale` = 1.0 / 1.0 / 0.75 / 0.5 / 0.25 at 0.6 / 5 / 15 / 30 / 75 kPa (planetary-keyframes 3.1). I did **not** add this key to `sky.json`, because I did not check whether the parser accepts unknown keys; the Technical Lead adds it with the code.
- Existing state: the shots already show a soft warm or blue-white glow around the sun, about 2 times the disc width. I did not find where it comes from in `MarsSkyEffects.java` and did not check; if it is a feature of the vanilla texture or a renderer effect, it will not follow pressure. Either way, **the aureole may stay out** until storm work, because that is where the aureole (widens before a storm, vanishes in one) is a real signal.

## 7. Done

- `pack/assets/mars/keyframes/sky.json`: only the `_invented` comment changed (exponent and single night colour marked as artist-reviewed and confirmed, still invented). No value, no key.
- Ran `cd mod && ./gradlew test --offline --tests '*SkyKeyframes*'`: BUILD SUCCESSFUL, all `SkyKeyframesTest` tests PASSED. No client run; no other Java.

## 8. Decisions needed

| # | Question | Recommendation | Type |
|---|---|---|---|
| 1 | Clouds: leave vanilla clouds until E-10? | Yes (section 5). | ill-structured (art vs cost); owner confirmation |
| 2 | Should the sunset band and aureole wait for storm/sunset work? | Yes: aureole with the storm slice, band when a 30 kPa sunset is first shown. | ill-structured (low) |

## 9. Handoffs

> **To:** Technical Lead · **Problem type:** well-structured
> **Problem:** Three small items from the sky review.
> **Context:** Section 2 to 6 of this report.
> **Needed from them:** (1) in the next screenshot run, add one night frame (time about 18000) and one dusk frame at 0.6 and 75 kPa; (2) a custom sun texture is not in the pack (`pack/assets/minecraft/textures/environment/` has no mars sun): the vanilla sun's saturated yellow rim `#FFFFAA` contradicts the keyframe disc colour `#F4EBDD`; ask me for a 32x32 sun texture (I can generate it, label placeholder) or say it is out of scope; (3) the dark dashed line along the horizon in the 0.6 to 30 level shots: is it the test-world edge or a rendering seam? When the aureole is built, add `aureole_scale` to the keys per section 6.
> **Done when:** the three frames exist, the sun texture question is answered, the horizon line is explained.

> **To:** Subject Matter Expert · **Problem type:** well-structured
> **Problem:** Keyframe colours are still estimates; `docs/art/references/` has no sky photographs.
> **Needed from them:** sampled approximate-true-colour Pancam/Mastcam sky images at a few dust opacities (planetary-keyframes section 6, last row), and a typical length of Martian twilight (for the night/dusk simplification, section 4).
> **Done when:** the 0.6 kPa keys are confirmed or replaced.

## 10. Risks

- All colours remain unsampled estimates against real rover images; the only real sky I could look at was a heavily processed InSight strip.
- Rose sky at 5 to 15 kPa is stylised; a playtester will take it for fact until the required label exists (open with the Lead Instructional Designer and Lead Game Designer since slice 6).
- Night and dusk are unseen in the real game; low risk, but an error there would be loud (a lit horizon band).

## 11. Shared model updates

None. (Lesson for the record: the reference folder lacks sky photographs, so "check against real references" for the sky means a family check only; add NASA sky images to `docs/art/references/` when the SME samples them.)
