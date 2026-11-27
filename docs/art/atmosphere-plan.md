# Atmosphere Plan: making the surface feel like Mars beyond terrain

**Owner:** Lead Artist · **Status:** Draft 1 (2026-11-27) · **Stability:** fluid (TEAM.md 8.1)
**Why:** the owner accepted the W1 terrain and said it needs more atmosphere.
**Builds on:** [planetary-keyframes.md](planetary-keyframes.md) (3.1 sky and fog, 3.7 storm), `pack/assets/mars/keyframes/sky.json` (slice 6), [artist sky review](../team/reports/2026-11-27-artist-sky-review.md), [audio direction](../audio/audio-direction.md), [sound list](../audio/sound-list.md), [terrain reference](../science/mars-terrain-reference.md).
**Marks:** INVENTED = my game number, no source. NOT VERIFIED = from memory or my own arithmetic, SME to confirm. Effort estimates are **unverified** (agent-hours, my guess, not measured).

## 0. What I looked at

Real client screenshots `tools/screenshots/out/20261127-205929/w1_01`, `w1_05`, `w1_07`, `w1_09` (all at 0.6 kPa, midday). Seen, in order of importance:

| Seen | Meaning |
|---|---|
| A dusty haze already dissolves far buttes and the far crater rim into the sky (w1_05, w1_09). | Distance haze works; it is a tuning job, not a build job. |
| Near the horizon the far ground reads as a **darker** brown band under a lighter sky (w1_01, w1_07). | The fog colour is darker than the horizon colour, so far hills read as a dark stripe, not as dust-washed shapes. Real Mars photos wash distant hills toward the sky colour (NOT VERIFIED, family check only). |
| The sky is a flat, even tan with white blocky vanilla clouds overhead (w1_01, w1_09). | Settled: clouds stay vanilla until E-10. No light in the sky, no sense of dust moving. |
| Nothing moves. No dust, no wind, no sound files (`pack/assets/mars/sounds/` and `particles/` are empty). | This is the biggest atmosphere gap and the cheapest to fill. |
| No storm change at all (S3-1 shipped the power effect but `SkyPayload` carries only pressure). | The first-storm beat (C1) has a Monitor effect and no world effect. |

Not looked at: night, dusk and dawn frames (no such shots exist yet), motion, any shot above 0.6 kPa for the W1 terrain.

## 1. Constraints (settled; not reopened)

- No post-process shaders (planetary-keyframes 1.3). Everything below is fog, sky, particle, sound, texture or one HUD overlay. Anything that needs a shader or a custom renderer is marked **SHADER** or **RENDERER**; I list none in the recommended path.
- Clouds stay vanilla until E-10 (owner). Sun aureole and sunset band were deferred by the owner; both are in section 7 and need an owner reversal.
- Color is never the only signal. Storm: fog distance, sun fade and sound carry it too.
- No violent wind and no flying debris in thin air (DESIGN.md audit; planetary-keyframes 3.7a).
- O2 and CO2 never colour the sky (planetary-keyframes 1.2).
- Cost classes: **data** = JSON or a texture file only; **small** = a small mod change (under about a day); **large** = more than a day or a new system.

## 2. Priority table

| Rank | ID | What | Cost class | Effort (unverified) | Needs |
|---|---|---|---|---|---|
| 1 | AT-1 | Storm sky tint, dim, fog close, sun fade, pre-storm haze | small | 0.5 to 1 day | TL |
| 2 | AT-2 | Far haze retune: fog colour toward the horizon, earlier fog start | data | 1 to 2 hours | TL (pack edit), my step recheck |
| 3 | AT-3 | Ambient dust motes and wind streaks along 110 degrees, storm drift | small | 0.5 to 1 day | TL, SME (rates) |
| 4 | AT-4 | Mars sun texture and Phobos-in-the-moon-slot texture | data (art) | 2 to 3 hours | me (binary art, next run), TL confirms paths |
| 5 | AT-5 | First sound set: wind beds, storm beds, footsteps by block | small + audio production | code under 0.5 day; audio large (not estimated) | TL, SME |
| 6 | AT-6 | Dust devils (cosmetic) | small | 0.5 day | TL |
| 7 | AT-7 | Star density by pressure; Deimos as a point | small | 2 to 4 hours | TL |
| 8 | AT-8 | Shadow fill (`ambient_light`) | data | 1 hour plus screenshots | TL |
| 9 | AT-9 | Visor: dust film after a storm, breath fog | small to large | 0.5 day (film) | LGD, SME |
| 10 | AT-10 | Thin ice-crystal cirrus | with E-10 | in E-10 | none now |
| 11 | AT-11 | Sunset band and sun aureole | small | 0.5 to 1 day | **owner reversal** |

**Recommended first batch: AT-1, AT-2, AT-3.** Section 9 gives the reasons.

## 3. (1) Distance haze and fog

**What the player sees today:** at 0.6 kPa fog starts at 70 % of render distance and ends at 100 %, so buttes at 90 blocks are crisp and only the very far ones dissolve. The far ground is darker than the horizon band.

**AT-2 proposal (data-only; values INVENTED, to be checked by eye in one screenshot run):**

1. **Fog colour moves 70 % of the way to the horizon colour** at every key, so far shapes dissolve into the sky band instead of forming a dark stripe. Computed from the current `sky.json` hex values (sRGB lerp, rounded; the real client mixes in OKLab, so expect a tiny difference):

| kPa | Fog now | Horizon | **New fog** |
|---|---|---|---|
| 0.6 | `#C49A73` | `#D2AE86` | `#CEA880` |
| 5 | `#BD9384` | `#CDA696` | `#C8A091` |
| 15 | `#B39A9C` | `#C8ABAA` | `#C2A6A6` |
| 30 | `#A9AAB9` | `#C0BAC3` | `#B9B5C0` |
| 75 | `#B4C6D8` | `#B8CDE2` | `#B7CBDF` |

2. **Fog start** (fraction of render distance), earlier at the start so silhouettes fade over a longer range: 0.6 kPa **0.50** (was 0.70), 5 kPa **0.47** (was 0.62), 15 kPa **0.42** (was 0.50), 30 kPa **0.36** (was 0.38), 75 kPa 0.30 (unchanged). Fog end unchanged (1.00, 1.00, 0.95, 0.90, 0.85).

**Trade-off I am accepting:** the "thicker air closes the fog" cue (pressure channel, B1) gets weaker at the low end: the 0.6 to 75 kPa fog-start travel shrinks from 0.40 to 0.20 of render distance. The colour, sky, windsock and sound still carry the pressure change. If the playtest says the Phase 2 beat feels weaker, restore the 5 kPa and 15 kPa starts first. **I have not re-run the OKLab step check** for the new fog colours (the old fog step minimum was 0.034 at 5 to 15 kPa); that is a condition of accepting this, and I run it when the file is edited.

**Science:** haze over distance on Mars today is dust (aerial perspective, NOT VERIFIED here beyond the family check in the sky review). A thicker air column also adds haze at high pressure; that is why the trend stays.

**Not proposed:** height fog (ground mist in craters and low basins). Vanilla fog is distance-based only; a height term needs a shader or a custom fog renderer (**RENDERER**, large). Skip; the dust motes in AT-3 give the near-ground layer instead.

## 4. (2) Light

| Item | Reality (status) | In game | Cost |
|---|---|---|---|
| Sun size | About 0.66 of Earth's apparent width (geometry from 1.52 AU, planetary-keyframes 2.1) | Built (`sun_scale` 0.66). | done |
| Sun colour | Whitish disc with a bluish glow in the dust (NOT VERIFIED); our key `#F4EBDD` at 0.6 kPa, `#FFF6E0` at 75 kPa | The vanilla sun texture has a saturated yellow rim `#FFFFAA` that contradicts the keys. **AT-4**: a 32x32 Mars sun texture (disc `#F4EBDD`, soft edge to transparent over the last 3 px, no yellow, not tinted per pressure) | data (art) |
| Sun angle | At a high-northern landing site the sun stays lower and the sol path is flatter (NOT VERIFIED) | **Do nothing.** Vanilla skylight falls straight down, independent of the sun's angle, so a lower sun changes nothing on the ground; rotating the sky disc would only look odd. Not worth the code | skip |
| Shadow softness | Dust scatters light into shadows, so Mars shadows are filled with soft skylight (NOT VERIFIED) | Vanilla has no cast shadows, only block shading and light levels, so "shadow softness" cannot be shaped. The one data knob is `ambient_light` in `pack/data/mars/dimension_type/` (now `0.0`). **AT-8**: try `0.04` (INVENTED); it also lifts the night floor, so check that the dome interior and night stay readable. Keep only if one screenshot shows a visible, not flat, result | data |
| Dawn and dusk, blue sunset | Mars sunsets are **blue near the sun** because dust scatters blue forward. Source cited in planetary-keyframes 2.1: Curiosity Mastcam sunset, NASA/JPL-Caltech/MSSS PIA19400 (sol 956). **I did not open it this session; NOT VERIFIED here; SME owns accuracy.** | Built as the sunset halo (`sunset_halo` `#7E9CC4` at 0.6, flipping to `#F2A35E` at 75). Nothing new. The blue-to-orange flip is the lesson. **Needs: one dusk frame at 0.6 and 75 kPa to check it** (asked in the sky review, not yet taken) | none |
| Day and night length | A sol is about 24.66 hours (NOT VERIFIED here); the game sol is 20 simulation minutes, the same as a vanilla Minecraft day | **No change.** The 2.7 % real difference is not felt. Real twilight is long because dust keeps lighting the sky after sunset (SME to size, NOT VERIFIED); vanilla dusk is short. Lengthening dusk means a custom time curve (small to large, touches the session clock). Not recommended now | skip |
| Night colour | `night` keys `#0D0B0E` to `#0A0F20` | Built | done |

## 5. (3) Ambient particles and the storm

All particles are client-side and cosmetic, spawned around the player each client tick, with a hard cap. They never carry a game fact except the storm drift (which follows `mars:storm_strength`). **Wind direction is fixed per planet in planet data: 110 degrees, "blows toward" (`pack/data/mars/planets/planet_1.json`, `blows_toward_deg`).** I assume a compass bearing (0 = north = -Z, 90 = east = +X, so 110 degrees is toward east-south-east). **The Technical Lead confirms the convention** in the report handoff.

### 5.1 AT-3 Blowing dust motes and wind streaks

**First version needs no new texture:** vanilla `minecraft:dust` takes a colour and a scale as options, so it can show dust in `#C08A5E` and `#8E5E3E` (the colours the storm spec already names).

| Particle | Look | Where and how many (all INVENTED) | Motion | Tied to |
|---|---|---|---|---|
| **Dust motes** | 2 px specks, `#C08A5E` (60 %) and `#8E5E3E` (40 %), alpha 0.5, life 4 to 6 s | Within 12 blocks of the player, about **4 per second** on a clear day | Drift **slowly** along the wind (0.3 to 0.5 blocks per second), small random wobble, no lift | `mars:pressure`: in thin air they hang and drift; above 30 kPa halve them |
| **Wind streaks** (sand and dust skimming the ground) | A row of 3 to 5 motes in a line along the wind, 0.5 to 1 block above ground, life 2 s | Spawn within 24 blocks, **1 streak per 5 s** at 0.6 kPa; more on `basaltic_sand` (dunes) tops; none on blocks with water above | Along 110 degrees, 1 to 1.5 blocks per second (a walking pace is 4 to 5) | Rate x (1 - hydrosphere % / 20), floored at 0 (wet ground does not blow); also faded by biomass later (ground cover) |
| **Storm dust** (3.7) | Same specks, 2x2 px | Up to **40 alive near the player** at full strength (cap; the particle limit is the player's setting) | Slowly sideways along the wind, still no gusts | `mars:storm_strength` x cap |

Science: wind-driven sand and dust movement on Mars is real and was observed at the Bagnold dunes (Curiosity; the sourced line in the terrain reference is about dunes and ripples, **not about sand being in motion; NOT VERIFIED**). Thin air needs strong wind to lift sand, so streaks are rare and slow at the start; that is the honest direction. **SME to confirm the rate direction.**

**Why the wind direction matters:** the dunes in W1 already have gentle and steep sides facing the wind; streaks along 110 degrees make the dune shapes legible as wind-made (terrain reference section 4, "one global wind direction").

**Needs:** `ParticleEngine` calls from a client tick handler (small mod code; no new particle type for version 1). Later (nice to have): a custom 1x6 px streak texture, 4 frames, `particles/wind_streak.json`, so streaks are elongated instead of dots (art + registering a particle type, small).

### 5.2 AT-1 The storm: sky tint, dim, fog and the pre-storm haze (design for the open handoff)

Starts from planetary-keyframes 3.7 and 3.7a. New: a **dim**, per-pressure behaviour, the night rule, and the pre-storm haze values. `s` = `mars:storm_strength` (0 to 1, already capped by phase: 1.0, 0.8, 0.5, 0.25; the server ramps it, 60 s in and 90 s out).

| Property | At strength `s` | At full strength (`s` = 1) | Status |
|---|---|---|---|
| Sky zenith and horizon | mix toward `#9A6A48` by `0.85 x s` (OKLab) | 85 % `#9A6A48` | from 3.7 |
| Fog colour | mix toward `#A8744E` by `0.85 x s` | 85 % `#A8744E` | from 3.7 |
| **Dim** (new) | multiply the final sky and fog colour (linear RGB) by `1 - 0.18 x s` | x 0.82 | INVENTED |
| Fog start | lerp(base start, 0.0, `s`) | 0.0 | from 3.7 |
| Fog end | lerp(base end, 0.18, `s`) with a floor of 16 blocks | 0.18 x render distance | from 3.7 |
| Sun disc | alpha lerp(1.0, 0.30, `s`) | 0.30 | from 3.7 |
| Stars at night | alpha x (1 - 0.9 x `s`) | 0.1 | INVENTED |
| Whole-screen tint (new) | one translucent quad `#2A1608`, alpha `0.12 x s`, drawn under the HUD | 0.12 | INVENTED; see below |

Examples of what the base values give (fog end, as a fraction of render distance; render distance 12 chunks = 192 blocks):

| Stage | Base fog end | Cap `s` | Fog end in the storm | At 192 blocks |
|---|---|---|---|---|
| 0.6 kPa, Phase 1 | 1.00 | 1.0 | 0.18 | 35 blocks |
| 5 kPa, Phase 2 | 1.00 | 0.8 | 0.34 | 65 blocks |
| 30 kPa, Phase 3 | 0.90 | 0.5 | 0.54 | 104 blocks |
| 75 kPa, equilibrium | 0.85 | 0.25 | 0.68 | 131 blocks |

So storms shrink visibly as the planet changes (the "storms die down" promise, DESIGN.md section 3), and the late ones read as a dusty day, not a catastrophe.

**Night rule:** apply the storm mix first, then the existing day/night mix, so a night storm is dark, not bright brown. Night storm stars fade with `s` as above.

**The "dim" and its honest limit.** The sky and fog colours and the sun fade do most of the work. A real dim of *ground light* would need the lightmap (a **RENDERER** change, large). The cheap stand-in is the whole-screen tint above: **a translucent quad drawn before the HUD** (a GUI layer event; small mod code; **not verified that 1.21.1 draws it where I expect; the Technical Lead checks**). It is a flat multiply-like overlay, not a shader. If it looks like a dirty window instead of dim light, drop it and keep the sky and fog darkening only; the storm still reads through fog distance, sun and particles. Design rule: never flash, 60 s in and 90 s out as already agreed.

**Pre-storm haze (3.7a step 1), values:**

| Property | Value | Notes |
|---|---|---|
| Signal | `mars:storm_haze` 0 to 1, rises linearly over the **last 0.25 sol (5 simulation minutes) before arrival**, then hands over to `mars:storm_strength` | New server signal. With the S3-1 warning at "about 10 min", the haze starts halfway through the warning |
| Fog end | x (1 - 0.10 x haze) | From 3.7a (proposed there) |
| Fog colour | mix toward `#A8744E` by 0.06 x haze | INVENTED |
| Aureole, sun, stars, particles | **no change** | Keeps the aureole deferral and keeps the haze quiet |
| Solar output, Monitor | **no change** | The Lead Instructional Designer's one condition (F1 must not be answered by the world); the haze says "something is coming", not how big |

**Needs (small code):** one more field in `SkyPayload` (storm level; haze level), one more input in `SkyState`, one extra block in `sky.json` for the targets and the two constants (`dim`, `overlay_alpha`). The overlay targets already exist in planetary-keyframes section 1.3's data shape.

## 6. (4) Sky: clouds, stars, moons

| Item | What the player sees | Cost | Notes |
|---|---|---|---|
| **Clouds** | Vanilla blocky white clouds until E-10 (settled; my sky review agrees). E-10 delivers the density map `clouds_density.png` (exists as a concept) and the hydrosphere-driven coverage | in E-10 | Nothing now |
| **AT-10 Thin ice-crystal cirrus** (optional, after E-10) | Very thin, high, streaky wisps with a faint white-to-pink tint at dusk; present at hydrosphere 0 because real Mars has thin water-ice clouds today (Curiosity cloud observations, planetary-keyframes 2.1, NOT VERIFIED here). Same E-10 map, a second layer with 3x horizontal stretch along the wind (110 degrees) at opacity 0.2 | large only if it needs a second cloud layer; in E-10 it is a map variant | Decide when E-10 starts |
| **Stars** | Dense at 0.6 kPa, "slightly fewer" at 30 and "fewer" at 75 (planetary-keyframes 3.1) | small (a star alpha scale in the custom sky; the code already draws its own dome) | AT-7. Also gives the storm star fade (5.2). Low impact; the night is rarely looked at |
| **Phobos** (nice to have) | A small, dark, lumpy grey disc in the moon slot (not round) | data (art) | AT-4. Apparent width: my calculation from mean diameter about 22 km and an altitude of about 6,000 km gives about 0.21 degrees, about 0.6 of the Martian Sun's width overhead (**NOT VERIFIED**, SME check). Moon texture: a 19 px potato shape in each 32x32 phase cell, all 8 cells the same shape with the lit part shaded `#9A938C`, shadow `#4A4642`. Real Phobos crosses the sky about twice a sol, rising in the west (planetary-keyframes 2.1); vanilla's moon does not. **Deliberate simplification: do not claim the motion** |
| **Deimos** (nice to have) | A bright star-like point, about 0.03 degrees by my calculation (**NOT VERIFIED**) | small | AT-7 only; skip if it needs its own orbit code |

## 7. (5) Sound hooks (I own audio)

**State of play:** `pack/assets/mars/sounds/` is empty, so none of the beds exist yet. The specs are in the sound list (AU-A01 to A03 wind beds, AU-A14 helmet bed, AU-W01 to W03 storm). What follows ties atmosphere to them; no new bed is invented.

| Hook | What the player hears | Driven by | Existing ID | Cost |
|---|---|---|---|---|
| **Wind, thin** | Faint low rumble, no whistle, low-passed at 1.5 kHz at the start; a hiss and gusts as pressure rises (audio direction section 3, the existing band table) | `mars:pressure` (`keyframes/ambient.json`) | AU-A01, A02, A03 | audio production (large, not estimated) plus small code |
| **Thin-air muffling, and its journal line** | The same wind, filtered. The sentence the journal needs: the A1 line about sound ("Sound is quiet and muffled because there is so little air to carry it") is **Lead Instructional Designer text**; the SME checks the claim (audio direction section 3 says SME-checked already). I only tie the subtitle band edges (below 5, 5 to 30, 30 and up kPa) to it | `mars:pressure` | the existing ambience subtitles | none new |
| **Storm sound** | The owner's word was "roar". **The audio rule says no howling wind in thin air.** I propose a **low rumble felt through the suit, plus a soft hiss**, never a whistle: at 0.6 kPa mostly the rumble, at 30 kPa and up an audible hiss that fades with storm strength. This is AU-W01 (patter on the visor), AU-W02 (hiss) and AU-W03 (glass). If the owner wants a real roar, the SME decides whether that stays honest | `mars:storm_strength` x pressure filter | AU-W01, W02, W03 | audio production |
| **Footsteps on sand versus rock** | The player's own steps are **contact sound** (through the suit and ground), audible even in thin air (audio direction section 3.1, SME-checked). Sand steps are a soft dry crunch; rock a short hard tick. Vanilla keeps its footsteps for vanilla actions (audio direction). Set the **sound type** of each W1 block: `basaltic_sand` and red sand as sand, `hematite_ore` and boulders as stone, `ground_ice` as vanilla ice | block's `SoundType` (code, when the blocks are registered) | none (new row proposed: AU-A15 footstep set) | small code, no new files if vanilla sounds are used |
| **Dust motes on the visor** | A faint grain tick when the player walks into a streak or a storm; contact channel, unfiltered | Storm strength; streak contact | AU-W01 | small |
| **Dust devil** | A faint rising hiss, exterior channel (so thin and muffled at 0.6 kPa) | The devil event | none (new row proposed: AU-A16) | with AT-6 |

**Rules kept:** sound is sparing; no sound over voice; every cue has a subtitle; the storm hiss and patter share one subtitle line so it does not spam. I do not add the new rows (AU-A15, A16) to `docs/audio/sound-list.md` in this run beyond the proposal here; they go in when AT-5 is built.

## 8. (6) HUD and visor cues

| Item | What the player sees | Cost | Notes |
|---|---|---|---|
| **AT-9a Dust on the visor after a storm** | Fine specks at the frame edges while the storm runs (already in 3.7a step 6), peaking at the storm end, then fading over about 2 sols or a wipe action. **The centre stays clear** (settled rule, style guide section 9) | small (a HUD overlay with 3 densities) plus a decay rule | The decay and any wipe action are **gameplay, so Lead Game Designer**; panel dust already has the "check your panels" chore, and a visor wipe could share it. I do not design the chore |
| **AT-9b Breath fog inside the helmet** | A soft white bloom at the lower visor edge on each exhale (every 4 s, 25 % alpha, 1 s fade) when the outside is below 0 degrees C | small to large (needs the helmet HUD overlay to exist first) | **Science caution (SME):** a pressurised suit recirculates air, and fog on a visor depends on humidity and surface temperature, so the claim is plausible but NOT VERIFIED. Nice to have; drop if the SME says it teaches a wrong idea |

## 9. Recommended first batch

| Rank | Item | Why first |
|---|---|---|
| 1 | **AT-1 storm look** | The first storm is a core learning beat (C1, B3) with no world cue; the Technical Lead already asked for these values. Small code, one payload field. The design is complete in 5.2 |
| 2 | **AT-2 far haze retune** | Data only, an hour of work, and it makes the W1 buttes and crater rims fade into the sky the way the owner's "more atmosphere" asks. Needs one screenshot run and my step recheck |
| 3 | **AT-3 dust motes and wind streaks** | The planet currently never moves. It uses vanilla `dust` particles, no new art, and shows the fixed wind that W1's dunes already follow. Also becomes the storm drift in AT-1 |

Fast follow: **AT-4** (sun and Phobos textures; the yellow sun rim is a known mismatch). Not first because it is binary art and I was asked for docs only this run.

## 10. Items that need an owner reversal (last)

| Item | Reason it is last | What the owner would reverse |
|---|---|---|
| **AT-11a Sunset band** (definition in the sky review section 6: lowest 22 degrees away from the sun, weight `glow x (1 - e/22)^2`, 0.6 of `sunset_band`) | The owner deferred it earlier; the halo already shows the blue-to-orange flip. It earns its place only at 30 and 75 kPa | "Deferred" to "build with the first 30 kPa sunset" |
| **AT-11b Sun aureole** (radius 2.5 x the disc x `aureole_scale` 1.0, 1.0, 0.75, 0.5, 0.25) | Owner deferred it; it only becomes a real signal with storms, and AT-1 deliberately does not use it | "Deferred" to "build for the pre-storm haze and the dust days" |

## 11. Assumptions and what would make this better defined

| Assumption | Check |
|---|---|
| The 1.21.1 GUI layer events can draw a full-screen translucent quad under the HUD | TL spike, 30 minutes |
| Fog colour toward the horizon does not weaken the pressure steps below the 0.035 target | My OKLab step recheck when the file is edited |
| Dust-mote and streak rates (4 per second, 1 per 5 s) read as "alive but quiet" | One screenshot pair and one short video-free playtest by the owner |
| Wind direction 110 degrees is a compass bearing | TL |
| All colours are my estimates, no rover pixel samples (open item in planetary-keyframes section 6) | SME sample, optional |
| Effort numbers | Measured on the first item (AT-1) and then rescaled |
