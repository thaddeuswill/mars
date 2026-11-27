# Technical Lead report: Stage 2 slice 6, sky and fog follow pressure (2026-11-27)

BACKLOG #46. Owner: "accept and continue" (slice 5 accepted with the 144 px Monitor and no Oven recipe yet). Scope: ONE visible world response, pressure first, keyframe data plus client interpolation.

## 0. Handoffs received

| From | Handoff | Answer |
|---|---|---|
| Lead Artist (`docs/art/planetary-keyframes.md` 3.1, 1.3) | Five pressure keys (0.6, 5, 15, 30, 75 kPa), log10 axis, OKLab mixing, clamp outside, 30 s smoothing, fog as fractions of render distance with a 48-block floor, sun at 0.66 | **Accepted and built** for zenith, horizon, fog colour, fog start and end, night colour, sunset halo, sun size. Not built: see section 5. |
| Owner | Keyframe JSON "in `pack/data/mars`" | **Deviation, flagged:** the file is `pack/assets/mars/keyframes/sky.json`. The art document and ARCHITECTURE 2.5 already put keyframes under `assets/`, and only the client needs them (a dedicated server has no use for them and a datapack file would have to be synced to clients). Same jar, same JSON. Easy to move if the owner wants `data/`. |

## 1. Done

Pure Java (ArchUnit-checked, `mars.visual` added to the no-Minecraft rule):
- `mars/visual/SkyKeyframes.java`: parse, validate (field named in every error), `sample(pressure)` (log10, OKLab, clamp, NaN safe), `fogDistances`, `daylight`.
- `Oklab.java` (sRGB to OKLab and back, exact at the ends), `Smoothing.java` (exponential step on log10 pressure), `SkyState.java` (first value snaps, later values shift; bad values ignored; `shouldSend` rule).

Data: `pack/assets/mars/keyframes/sky.json` (every value from the art document; invented items labelled in the file), `pack/data/mars/dimension_type/mars.json` now has `"effects": "mars:mars"`.

Server: `mars/sky/SkyPayload.java` (one double: the pressure), `SkySync.java` (login, respawn, arrival, then at most once per second and only for a change over 0.1 %), hooks in `MarsMod`, `PlanetEvents`, `MonitorNetwork` (lambda handler).

Client (`mars/client/sky/`): `SkyClient` (reload listener for the JSON, tick smoothing, `ViewportEvent.ComputeFogColor` and `RenderFog`), `MarsSkyEffects` (dimension special effects: gradient dome, sunrise glow in the halo colour, sun at 0.66, moon, the vanilla 1500-star field).

Other: boot checks `sky.keyframes_load`, `sky.effects_id`, `sky.payload_roundtrip`; `tools/screenshots/slice6.shots`; `build.gradle` sets a 2 s smoothing for the screenshot client only; ARCHITECTURE 2.5.5 (schema) and 2.19 row; stage2-plan row 6; BACKLOG #46; HANDOFF; AGENTS.

## 2. Real output

| Command | Result |
|---|---|
| `./gradlew spotlessApply` then `./gradlew spotlessCheck build test --rerun --offline` | BUILD SUCCESSFUL (6 s warm). One earlier run had 3 failing new tests, all my test expectations (daylight 0.2 clamps to 1; blue-share start value; 300 s is not quite converged); fixed in the tests, not the code. |
| Unit tests (JUnit XML summed) | **156 tests, 0 failures** (was 142; +14: `SkyKeyframesTest` 9, `SkyStateTest` 5) |
| `./gradlew runGameTestServer --offline` | `MARS-SELFTEST RESULT PASS passed=69 failed=0` (was 66; +3). 17 s. The dedicated-server-style boot loads the mod without touching client classes. |
| `tools/screenshots/run-shots.sh /home/user/mars/tools/screenshots/slice6.shots` | rc=0, 96 s wall; `pgrep -f runShotsClient` printed nothing first. Log: "Sky keyframes loaded: 5 keys, smoothing 2.0 s". PNGs: `tools/screenshots/out/20261127-130736/slice6_*.png` (timestamps 13:09, this run) |

**Prove-it-can-fail:**

| Guard removed | What failed |
|---|---|
| Strict-increasing-keys check in `SkyKeyframes` (set to `if (false)`) | `SkyKeyframesTest.aBrokenFileIsRefusedWithTheFieldNamed` (1 of 156 failed); restored |
| Data copy with `keys[2].at = 3.0` run through `runGameTestServer -PmarsPackDir=<scratch copy>` | `FAIL check=sky.keyframes_load ... keys[2].at = 3.0 after 5.0`, `RESULT FAIL passed=68 failed=1 first_failing_check=sky.keyframes_load` |

Not proved by removal: `SkySync` rate limit, the fog event cancel, the client drawing.

## 3. Pixels against the keyframes (real client, 1280x720, noon-ish, time 4500)

Method: a script (`PIL`) read the PNGs. Zenith = centre pixel looking straight up. Horizon = pixel 6 px above the horizon, 300 px right of centre (sky at about 0.6 degrees elevation). Fog = pixel 10 px below the horizon line, which is the clear colour beyond the loaded terrain.

| Pressure | Zenith seen / intended | Horizon seen / intended | Fog seen / intended |
|---|---|---|---|
| 0.6 (sol 0) | A87B5A / A87B5A | D1AD85 / D2AE86 | C49971 / C49A73 |
| 5 | A0736C / A0736C | CCA595 / CDA696 | BD9283 / BD9384 |
| 15 | 927489 / 927489 | C7AAA9 / C8ABAA | B39A9C / B39A9C |
| 30 | 727EAC / 727EAC | BEB9C2 / C0BAC3 | A9AAB9 / A9AAB9 |
| 75 | 6A93CF / 6A93CF | B6CCE2 / B8CDE2 | B4C6D8 / B4C6D8 |

Zenith is exact; horizon is within 2 per channel (the pixel sits slightly above the horizon so it already blends 4 to 5 % toward the zenith colour, as designed); fog within 2. Looking at the PNGs (a_level, e_level, f_falling): tan haze at 0.6, lavender-grey at 5 to 15, slate then pale blue at 30 to 75; the falling shot taken 1 s after 75 to 5 shows an in-between lavender-blue, so the shift is smoothed and regression is visible. **Fog distance** (start 0.70 to 0.30 of render distance) was not measured by pixels; the screenshots do not show a clear difference because the flat terrain edge is hard to read. It is unit tested (`fogDistancesFollowTheRenderDistanceWithAFloor`) and applied by the event, but I did not see it in a picture.

## 4. Cost

- Server: one counter check per planet tick; once per second one map read and compare; a packet of 8 bytes plus header per player only when pressure moved over 0.1 %.
- Client per tick: one exp/pow step; one keyframe lookup (three OKLab mixes) only when the shown value moved. Per frame: the dome is 8 rings x 16 segments = 128 quads rebuilt into a buffer each frame (the vanilla sky is a prebuilt buffer, so this is a small per-frame cost; not measured). Stars use one static buffer built once.

## 5. Not applied from the art document (named, not hidden)

Storm overlay (`mars:storm_strength` does not exist yet), hydrosphere-driven water and clouds (vanilla clouds are still drawn and look white at every pressure; the document wants thin high wisps at hydrosphere 0), sun aureole size, star density by pressure, sunset band colour (only the halo is used), sunrise/sunset timing beyond vanilla's, cloud base height, pre-storm haze.

## 6. Not verified

- Shaders (Iris/Optifine) or any renderer but vanilla's with software GL (Mesa llvmpipe).
- A real multiplayer client joining a dedicated server (payload registered and round-trip tested; delivery to a remote client not run). Singleplayer path was exercised in the screenshot client.
- The real 30 s smoothing in a client (the shot run used 2 s through a dev-only property; the maths is unit tested at tau 30).
- Fog distance by eye; other GUI scales and render distances (floor 48 blocks tested only in unit test).
- Sunrise/sunset glow and the night sky (all shots at time 4500).
- Rain (vanilla darkening is not applied) and the underwater/blind cases (the code skips them; not seen).
- Keyframe estimates are the artist's, not pixel samples of rover images.

## 7. Elapsed time and build cycles

About 35 minutes of wall-clock by the sandbox clock from first read to the last verification (the screenshot run 96 s). Gradle invocations (my own tally): 12 (2 spotless, 5 build/test, 4 runGameTestServer incl. one with the broken pack, 1 client run).

## 8. Decisions needed

| # | Question | Recommendation | Type |
|---|---|---|---|
| 1 | Keyframes live in `assets/` not `data/` (section 0). Accept? | Accept; matches the art document and ARCHITECTURE. | well-structured |
| 2 | Vanilla clouds are still drawn white at every pressure. Hide them until the hydrosphere keyframes exist, or leave? | Leave for now; the cloud map (E-10) is a later asset. | ill-structured (art vs cost); owner or Lead Artist |

## 9. Handoffs

> **To:** Lead Artist · **Problem type:** well-structured
> **Problem:** Slice 6 shows your numbers in the real game; a few things need your eye.
> **Context:** `tools/screenshots/out/20261127-130736/slice6_*`. Used as given: zenith, horizon, fog, halo, night, fog fractions, sun 0.66.
> **Needed from them:** (a) look at the five pairs and say if any look wrong; (b) the dome blends horizon to zenith with `t = (elevation/90)^0.6` (my invented choice, in `sky.json`): confirm or give a gradient shape; (c) night colour is applied to the whole dome by vanilla's day brightness: confirm; (d) white vanilla clouds at all pressures: wanted or hidden until E-10; (e) nothing defines a sunset band or aureole in a usable form yet (not applied).
> **Done when:** `sky.json` values or the gradient exponent are confirmed or changed.

> **To:** Subject Matter Expert · **Problem type:** well-structured
> **Problem:** The keyframe colours remain estimates.
> **Needed from them:** the optional pixel sampling of calibrated rover images (art document section 6, last row); the file's values are the only place to change.
> **Done when:** the estimates are replaced or confirmed.

> **To:** Lead Instructional Designer / Lead Game Designer · **Problem type:** well-structured
> **Problem:** The Phase 2 sky label required by SME round 2 ("More air scatters more blue light...", planetary-keyframes 2.3) has no screen yet; the sky now shifts to rose at 5 kPa.
> **Needed from them:** where the label appears (journal, Phase 2 gate) before a playtester sees the rose sky.

## 10. Risks

- **Custom sky code:** `MarsSkyEffects.renderSky` replaces vanilla's drawing for the Mars dimension, so a Minecraft or NeoForge update can change what vanilla does and ours will not follow. It copies 1.21.1 geometry; revisit on a version change.
- **Per-frame dome rebuild** is small but unmeasured; low-end classroom machines are the target. If it shows in a profile, build the dome once and colour it with `setShaderColor`.
- **Shader packs** will probably bypass or fight custom sky code; classroom machines rarely use them.
- **Pressure is the only value sent;** adding storm or hydrosphere later means a wider payload (still only what the sky needs).
- **`/mars index set pressure` can jump pressure** and the frost sink and gates react; the sky just follows.

## 11. Shared model updates

- **Decision (owning doc ARCHITECTURE 2.5.5):** sky and fog keyframes are client assets (`assets/mars/keyframes/sky.json`); the server sends only the planet's pressure, rate limited; the client smooths it (30 s) and interpolates (log10, OKLab).
- **Term:** "shown pressure" = the smoothed value the sky draws, as opposed to the live Index value.
- **Lesson:** NeoForge 1.21.1 has no event for the sky dome colour (only fog colour and distance), so a pressure-driven sky needs custom `DimensionSpecialEffects.renderSky`; fog distance changes only apply when the `RenderFog` event is cancelled.
- **Lesson:** sample pixels from screenshots with a script and put them next to the intended values; it caught nothing wrong here but turns "looks right" into numbers.
