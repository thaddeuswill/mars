# Planetary Keyframes — How the World Looks at Every Index Stage

**Owner:** Lead Artist · **Status:** Draft 1 (2026-11-26) · **Stability:** all values here are **fluid** (TEAM.md 8.1); expect changes after every playtest.
**Answers:** consistency review **M-17** (a visible cue for each of the six Planetary Index values, not just pressure and O2) and the Lead Game Designer's draft-2 handoff (outgasser plume, ice states, ground collapse, shrouds, quiet zones).
**Depends on:** [DESIGN.md](../../DESIGN.md) §1–3 (Index, gates, ice-to-water rule, hazards), [balance sheet](../design/balance-sheet.md), [SME accuracy review](../science/accuracy-review.md), [style guide](style-guide.md).
**Reference swatches:** [palettes/palette-sheet-v1-placeholder.png](palettes/palette-sheet-v1-placeholder.png) (placeholder; the hex values in this file win if they ever differ).

> **SME review flags in this file:** every row marked **[SME]** needs a science check. The biggest one is the **sky path from tan through rose to blue** (§2.3, §3.1). The SME is checking the "tan toward pink" claim now. My values are a best grounded guess and are marked where they are stylized.

---

## 1. How this works

### 1.1 One parameter, one main channel

Players learn cause and effect (B1) only if they can tell **which** change caused **which** look. So each Index value owns a main visual channel that nothing else drives, plus backup channels so color is never the only signal (style guide §4.1).

| Index value | Main world channel (owned) | Backup channels | Where it shows |
|---|---|---|---|
| **Pressure** | **Sky color** (zenith, horizon), **fog distance**, **sunset color** | Windsock angle, sounding balloon height, exterior sound level, cloud height | Global |
| **Temperature** | **Frost** (dawn frost cover, dome-glass frost, burn-off time) | Ice block behavior (with pressure), ice-crack sounds, thermometer on the weather mast | Global + local (near machines) |
| **Hydrosphere** | **Water**: ice state, puddles, pools, water color | Clouds, rain, morning ground fog | Global + local |
| **O2** | **Flame**: whether fire can burn, flame size, fire spread | Bubbles from algae and water plants, weather-mast gas gauge, HUD | Local (wherever fire or flora is) |
| **CO2** | **No color of its own** (see 1.2). Its effects: carbonate deposits at scrubbers, plant growth, and frost returning if CO2 drops before greenhouse gases hold the heat | Weather-mast gas gauge, Monitor | Local + via temperature |
| **Biomass** | **Ground**: regolith → biocrust → soil → moss → grass, and flora blocks | Spore/pollen particles, leaf rustle sound, thawlings settling | Local, spreading |
| *(Dust storms, derived)* | Storm overlay on sky and fog (§3.7) | Dust particles, visor dust film, solar panels dimming | Global event |

### 1.2 Rule: we don't paint invisible gases

O2 and CO2 are colorless. If the sky turned "oxygen blue" or "CO2 brown", the game would teach a false idea (a negative-learning risk under DESIGN.md fantasy rule 3). So:

- **The sky is driven by pressure and dust,** which are what really color a sky (a thicker atmosphere scatters more blue light; suspended dust makes the sky tan).
- **O2 and CO2 show through what they do:** fire, plant growth, bubbles, carbonate deposits, temperature. "You can't see the gas, but you can see its effects" is itself a good science lesson (A5).
- **An in-world instrument shows the gas mix:** the **weather mast** (§3.8), a proposed prop outside the dome.

ARCHITECTURE.md §2.5 now lets any Index parameter or derived value drive sky and fog, with the Lead Artist choosing. **Choice:** sky and fog are driven by **pressure, storm strength, and hydrosphere**. O2 and CO2 drive **no** sky or fog value.

### 1.2a Inputs: which value drives which keyframe

Input names below are descriptive; the Technical Lead maps them to the real data keys.

| Visual (section) | Driving input | Axis | Kind |
|---|---|---|---|
| Sky zenith, horizon, night sky, sunset halo and band, fog color, fog start/end, sun aureole, star density, cloud height (§3.1) | **Pressure** (kPa) | log10 | Index parameter |
| Storm overlay on sky, fog, sun; dust particles (§3.7) | **Storm strength** (0–1, current weather event) | linear | Derived (weather state); max capped by gameplay phase |
| Radiation glow and click rate (§3.7) | **Radiation spike active** × **pressure** scale | linear | Event + Index parameter |
| Windsock angle, sounding balloon height (§3.1) | **Pressure** | log10 | Index parameter |
| Exterior sound gain and low-pass (§3.1, audio) | **Pressure** | log10 | Index parameter |
| Dawn frost cover, burn-off time, dome-glass frost, thermometer (§3.2) | **Temperature**, global mean (°C) | linear | Index parameter |
| Frost-free ring and heat shimmer near machines (§3.2) | **Local temperature** (per-chunk heat map, ARCHITECTURE.md §2.5) | threshold | Derived |
| Ice state: sublimate / flash puddle / day puddle / pool (§3.3) | **Pressure** + **local temperature** (DESIGN.md ice-to-water rule) | rule table | Index parameter + derived |
| Vapor wisp rate over water (§3.3) | **Pressure** (against the boiling point at that pressure) | step | Index parameter (boiling point is a Monitor derived value) |
| Water color, underwater fog, clouds coverage and color, rain, ground fog (§3.3) | **Hydrosphere** (% of surface) | linear | Index parameter |
| Flame size, flicker, fire spread (§3.4) | **O2 fraction** (%) | bands | Index parameter |
| Helmet on/off, visor return, Air window dot (§3.4, style guide §9, §11) | **Breathable condition**: O2 partial pressure, pressure, CO2 partial pressure | live check | Derived |
| Bubbles from algae vats and water plants (§3.4) | **Machine / flora block output** (local) | linear | Local, not Index |
| Weather-mast gas tubes (§3.5) | **O2 partial pressure** and **CO2 partial pressure** (kPa) | linear | Derived |
| Carbonate deposits at scrubbers (§3.5) | **Scrubber cumulative output** (local) | step | Local |
| Ground conversion, flora color (§3.6) | **Seeded flora nearby** (block ticks), probability scaled by **biomass** (BU) | ticks | Local + Index parameter |
| Spore particles, rustle ambient (§3.6) | **Biomass** (BU) | linear | Index parameter |
| Outgasser plume puff size (§3.8) | **Temperature** (global mean) | linear | Index parameter |
| Thawling frills, settling (§3.8, style guide §6) | **Fauna pressure** and quiet-zone state (balance sheet §6) | state | Derived |

### 1.3 Interpolation rules for the client

| Rule | Value |
|---|---|
| Visuals follow **live** Index values, not gates | Gates lock in; the sky does not. If pressure falls, the sky goes back. Regression must be visible (DESIGN.md §1) |
| Pressure axis | Interpolate on **log10(pressure)** between keyframes. Pressure spans 0.6 → 75 kPa (125×); a linear axis would put all visible change in Phase 3 |
| Other axes | Linear between keyframes |
| Color space | Mix colors in **OKLab**, not sRGB (avoids muddy grey midpoints between tan and blue). Convert hex → linear RGB → OKLab, lerp, convert back |
| Outside the range | Clamp to the end keyframe |
| Smoothing | The displayed value moves toward the live value with a time constant of **30 s real time**, so a gate or a debug command produces a visible 1–2 minute shift, never a snap |
| Fog distances | Given as fractions of the player's render distance, so they work on low-end classroom settings; with a floor in blocks |
| Performance | Values are looked up when the Index changes (at most once per second), not per frame (ARCHITECTURE.md §2.5). **No post-process shaders are required**; the optional heat shimmer has a particle fallback |

**Proposed data shape** (the Technical Lead owns the schema; this is only to show what the client needs):

```json
{
  "input": "pressure_kpa", "axis": "log10", "space": "oklab",
  "keys": [
    { "at": 0.6,  "sky_zenith": "#A87B5A", "sky_horizon": "#D2AE86", "fog": "#C49A73",
      "sunset_halo": "#7E9CC4", "sunset_band": "#8A6A55", "night": "#0D0B0E",
      "fog_start": 0.70, "fog_end": 1.00 },
    { "at": 75.0, "sky_zenith": "#4A7AC2", "...": "..." }
  ]
}
```

---

## 2. Grounding: the real starting Mars

### 2.1 What real imagery and data say

**No source here was opened this session** (network to NASA and reference sites was blocked). Citations are from the artist's knowledge; the SME should confirm them. **Hex values are my estimates from published descriptions and calibrated images I know, not pixel samples.** Before Stage 4, sample calibrated "approximate true color" images (Mastcam, Pancam) to replace them (handoff to SME).

| Real observation | Source (not opened; **unverified** details) | What we take from it |
|---|---|---|
| The daytime sky is a pale **butterscotch / tan**, because fine suspended dust (particles around 1.5 µm) absorbs blue light and scatters red | Smith & Lemmon (1999), Mars Pathfinder imager opacity results, *JGR Planets*; Lemmon et al. (2004), "Atmospheric imaging results from the Mars Exploration Rovers", *Science* 306 | Start sky: tan, brighter near the horizon, slightly darker and redder at the zenith |
| Published sky colors vary (tan, butterscotch, salmon, "pinkish") with dust load, time of day, and **image processing** (approximate true color vs. white-balanced). Early Viking releases were mis-colored | Bell et al. (2003), Pancam investigation and calibration, *JGR* | Our start sky is a **mid** dust day, not a storm day. "Pink" descriptions of today's Mars exist; see §2.3 |
| **Sunsets are blue** near the sun: dust scatters blue light forward, close to the sun's direction | NASA/JPL-Caltech/MSSS, Curiosity Mastcam sunset in Gale Crater, PIA19400 (sol 956, April 2015) | Start sunset halo: cool blue. This flips to warm orange at equilibrium (§3.1) |
| The Sun looks about **2/3 as wide** and gives about **43 %** of Earth's light | Geometry: Mars at ~1.52 AU (1/1.52 ≈ 0.66; 1/1.52² ≈ 0.43) | Sun quad drawn at **0.66×** vanilla size. Light level is **not** lowered (gameplay readability; stylized) |
| Surface: bright **reddish dust** (iron oxides) over **darker basaltic sand**; fresh rock under the dust is **grey** | Curiosity's first drill hole ("John Klein", 2013) showed grey powder; dark dunes in Gale crater (Bagnold dunes) | Three ground families: rust regolith, bright dust, dark basalt sand; grey for drilled or broken rock |
| Exposed **ground ice vanished** over a few days without melting | Phoenix lander, "Dodo-Goldilocks" trench (2008) | Phase 1 ice sublimates: shrinks with vapor wisps, no water (DESIGN.md ice-to-water rule) |
| **Frost** forms on the ground | Viking Lander 2, Utopia Planitia, frost images (1979) | Dawn frost cover keyed to temperature (§3.2) |
| Thin **water-ice clouds** occur today | Curiosity cloud observations (several campaigns, e.g. 2019, 2021) | Faint high wisps even at hydrosphere 0 (§3.3) |
| Mars has **aurora**; visible-light aurora was reported from the surface in 2024 | MAVEN diffuse aurora (Schneider et al., 2015, *Science*); Perseverance visible aurora (2024; **unverified**) | Optional faint night glow during radiation spikes (§3.7) **[SME]** |
| **Phobos** orbits faster than Mars spins, so it rises in the west and sets in the east | Phobos orbital period ~7.7 h < 24.6 h sol | Optional night-sky detail (asset list) |

### 2.2 What happens as the air thickens (physics reasoning) **[SME]**

This is my own reasoning, for the SME to check:
- A blue sky comes from **Rayleigh scattering** by gas molecules. It grows with the **mass of air above you** (pressure ÷ gravity).
- Mars's gravity is about 0.38 of Earth's, so the same surface pressure means about **2.6×** more air overhead. At **75 kPa** on Mars, the column is about **1.9×** Earth's (75/101.3 × 9.81/3.71).
- So at equilibrium, **with low dust**, the sky should be **blue, probably deeper at the zenith and paler/whiter near the horizon than Earth's.**
- In between, weak blue scattering mixes with the warm dust glow. A mix of warm tan and cool blue light gives a **desaturated, cooler, greyish-rose to lavender** sky, not a clean pink.
- The sunset flips: with thick air and little dust, blue is scattered **out** of the long sunset path, so sunsets turn **orange-red**, like Earth's.

### 2.3 Stylization log (deliberate departures)

| Where | Real / expected | What we do | Why |
|---|---|---|---|
| Phase 2 sky (5–15 kPa) | Probably a subtle, desaturated shift **[SME]** | A clearly **rose / mauve** tint (`#A0736C` → `#927489` zenith) | The Phase 2 gate needs a visible sky change (story beat 7: "the sky shifts"). Matches the story bible's "pink sky" in Phase 2. My first, more realistic value was below the visible threshold (style guide §4.2) |
| Light level | Mars sunlight is ~43 % of Earth's | Vanilla light levels | Play readability; light level is not a learning goal |
| Equilibrium sky | Deeper blue than Earth's, if dust is low **[SME]** | `#4A7AC2` zenith, a touch warmer at the horizon than Earth-like | Keeps a residual dust warmth so it still feels like Mars; deliberately **not** vanilla Minecraft's sky color |
| Fog getting closer as pressure rises | More air = more haze over distance (aerial perspective) is real; Mars today also has dust haze | Fog start pulls in from 70 % to 30 % of render distance | Makes "thicker air" visible (DESIGN.md: "thicker sky fog") |
| Salt crust (perchlorate) | Perchlorate in soil is not visible | Pale salt crust where brine dried | Gives the real hazard a place to be seen; journal says the salts are invisible in real soil **[SME wording]** |
| Flame thresholds | Real flame limits depend on O2 fraction and total pressure | Simple bands by O2 fraction (§3.4) | Readable; numbers **[SME]** |

---

## 3. Keyframes by Index value

Snapshot values at gates for the other five values are in §4. All hex values are sRGB.

### 3.1 Pressure (kPa): sky, fog, sunset, sound

**Input:** pressure (kPa), log10 axis.

Keyframes at 0.6 (start), 5 (Phase 2 gate), 15, 30 (Phase 3 gate), **75 (equilibrium target)**. Interpolate on log10(pressure). Values are for **clear weather at midday**; the storm overlay (§3.7) mixes on top.

| Property | 0.6 kPa | 5 kPa | 15 kPa | 30 kPa | 75 kPa |
|---|---|---|---|---|---|
| Sky zenith | `#A87B5A` | `#A0736C` | `#927489` | `#727EAC` | `#4A7AC2` |
| Sky horizon | `#D2AE86` | `#CDA696` | `#C8ABAA` | `#C0BAC3` | `#A9C3DF` |
| Fog color (day) | `#C49A73` | `#BD9384` | `#B39A9C` | `#A9AAB9` | `#B4C6D8` |
| Sunset halo (around the sun) | `#7E9CC4` (blue) | `#8A98BE` | `#A29CAE` | `#CCA388` | `#F2A35E` (orange) |
| Sunset band (horizon away from sun) | `#8A6A55` | `#8C6660` | `#8E6670` | `#B07466` | `#D9714F` |
| Night sky | `#0D0B0E` | `#0D0B10` | `#0C0C14` | `#0B0D18` | `#0A0F20` |
| Fog start (× render distance) | 0.70 | 0.62 | 0.50 | 0.38 | 0.30 |
| Fog end (× render distance) | 1.00 | 1.00 | 0.95 | 0.90 | 0.85 |
| Fog end floor | 48 blocks | 48 | 48 | 48 | 48 |
| Sun disc | `#F4EBDD`, wide warm aureole (dust) | same | aureole 75 % | aureole 50 % | `#FFF6E0`, aureole 25 % |
| Stars at night | Dense | Dense | Dense | Slightly fewer | Fewer (thicker air) |
| Cloud base height | High (Y 220) | High | Y 200 | Y 190 | Y 180 |
| Windsock angle (weather mast, in wind) | Hangs at 5° | 15° | 35° | 55° | 80° (near level) |
| Sounding balloon (weather mast) | On the ground, slack | 2 blocks up | 4 | 6 | 8 blocks up, taut |
| Exterior sound gain (direction for audio) | 0.25 | 0.40 | 0.60 | 0.75 | 1.00 |
| Exterior low-pass cutoff (direction for audio) | 1.5 kHz | 3 kHz | 5 kHz | 8 kHz | Off |

**Measured step sizes** (OKLab ΔE; ~0.02 is just noticeable; target ≥ 0.035, style guide §4.2):

| Step | Zenith | Horizon | Fog | Sunset halo | Weakest simulated (zenith) |
|---|---|---|---|---|---|
| 0.6 → 5 | 0.041 | 0.036 | 0.036 | 0.018 | 0.023 (tritanopia) |
| 5 → 15 | 0.047 | 0.026 | 0.034 | 0.044 | 0.037 |
| 15 → 30 | 0.067 | 0.043 | 0.049 | 0.087 | 0.054 |
| 30 → 75 | 0.061 | 0.046 | 0.079 | 0.074 | 0.044 |

Where a step is weak (0.6 → 5 sunset halo; tritanopia at 0.6 → 5), the **windsock, balloon, fog distance, and sound** change at the same time, so the Phase 2 change never relies on color alone.

**Physics note on the weather mast:** wind force on a windsock grows with air density, so in thin air a normal windsock barely lifts. The Phoenix lander's "telltale" was built extra-light so Mars wind could move it. Our windsock is a normal one; its rising with pressure is real physics, simplified **[SME]**.

**Monitor milestones** (DESIGN.md §2): the **Armstrong line (6.3 kPa)** and **Everest pressure (~33.7 kPa)** are not sky keyframes; they get a Monitor marker and a BELL log line. The sky passes them smoothly. Visual spec: style guide §11.

### 3.2 Temperature (°C, global mean): frost

**Input:** global mean temperature (°C), linear; local cues use local temperature from the per-chunk heat map.

Keyframes at −60 (start), −40, −20 (Thaw gate), 0 (part of Phase 3 gate), 5 (equilibrium). Temperature is **flat at −60 through Phase 1** (balance sheet §2), so frost stays the same until Phase 2; that is fine, because Phase 1's visible change is pressure.

| Property | −60 °C | −40 °C | −20 °C | 0 °C | 5 °C |
|---|---|---|---|---|---|
| Dawn frost: share of exposed top faces | 70 % | 55 % | 35 % | 10 % (shade only) | 0 % (above Y 160 only) |
| Frost burn-off (ticks after sunrise) | 3000 | 2500 | 1500 | 600 | — |
| Night frost on dome glass (px in from pane edge, 16 px pane) | 5 | 4 | 3 | 1 | 0 |
| Ice-crack sounds near warm machines | None | None | Occasional | Frequent by day | Rare |
| Weather-mast thermometer (bulb column, of 12 px) | 1 px | 3 px | 5 px | 8 px | 9 px |

- **Frost texture:** overlay with 1–2 px rime clusters, `#EEF2F4` highlight, `#C3CDD4` shade. Three densities (`frost_light`, `frost_medium`, `frost_heavy`).
- **Heat around machines (local):** emitters, outgassers, and extractors melt frost in the chunks their heat reaches (the TL's per-chunk heat map; a 3-block soft edge is optional polish) and show **heat shimmer**. Shimmer is a post-process distortion if the TL can afford it; otherwise a particle fallback (faint rising 8×8 wavy particle, `#FFFFFF` at 12 % alpha). This also shows why thawlings come to machines.
- **Falling temperature** (e.g. CO2 dropped before greenhouse gases could hold the heat) brings frost back: the same table, read downward.

### 3.3 Hydrosphere (% of surface covered by liquid water): water

**Input:** hydrosphere (%), linear. Ice states use pressure + local temperature.

Keyframes at 0, 2, 10 (Hydrosphere gate), 20, 30 (equilibrium).

| Property | 0 % | 2 % | 10 % | 20 % | 30 % |
|---|---|---|---|---|---|
| Water surface color | `#6E6250` (silty brine) | `#6E6250` | `#4F6A70` | `#3A6E93` | `#2C6CA6` |
| Underwater fog | `#4A4034` | `#4A4034` | `#34484D` | `#24486A` | `#1B4670` |
| Clouds: coverage / opacity | 5 % / 0.35 thin high wisps | 8 % / 0.40 | 20 % / 0.60 | 35 % / 0.75 | 45 % / 0.85 |
| Cloud color (top / underside) | `#ECE6DE` | `#ECE6DE` | `#EEEAE4` | `#F1F0EC` / `#A8AFB8` | `#F4F4F2` / `#9AA3AE` |
| Rain | None | None | None (morning ground fog near water) | Occasional light rain | Regular rain events |
| Morning ground fog near water (radius) | — | — | 4 blocks | 6 | 8 |

Water color steps: ΔE 0.06, 0.06, 0.03 (normal); deuteranopia 0.05, 0.06, 0.04. The last step is subtle on purpose: by then the change is carried by rain and clouds.

**Ice and water states (follows the DESIGN.md ice-to-water rule).** These are the core A2 visuals, so each has its own look that does not rely on color.

| State | When (DESIGN.md rule) | What the player sees | Assets |
|---|---|---|---|
| **Sublimating ice** | Pressure < 0.61 kPa, warmed ice (Phase 1, near a heater) | The ice block shrinks in 4 steps (layers 16 → 12 → 8 → 4 px tall), each step puffing **vapor wisps** (white, rising, fading). **No water ever appears.** Ground under it stays dry | `water_ice_shrink_1..3`, particle `vapor_wisp` |
| **Flash puddle** | 0.61–5 kPa, local temp > 0 °C | A thin puddle (1 px water layer) appears, **bubbles and steams hard** for 2–4 s, then vanishes. Short-lived, clearly "boiling away" | `puddle_boiling` (4 frames), `vapor_wisp` dense |
| **Day puddle, night ice** (Thaw) | ≥ 5 kPa, local temp > 0 °C by day, ≤ 0 °C at night | By day, a puddle (2 px) with light wisps. At night it turns into a **thin ice sheet** (white rime pattern on a flat layer). Next day, puddle again. The cycle is the visual: warmth comes and goes, so does water | `puddle`, `puddle_frozen` |
| **Pooling water** (Hydrosphere) | ≥ 5 kPa and stays above 0 °C; hydrosphere rising | Full water blocks, **no wisps**, gentle surface animation; edges darken the ground (wet regolith `#6E4430`). Pools grow outward by block ticks | `mars_water` (still/flowing), `wet_regolith` |
| **Stable seas** | ≥ 30 kPa and ≥ 0 °C | Same as pooling, plus water color moving toward blue (table above) | — |

- **Vapor wisp particle:** 8×8, 4 frames, `#F4F1EA` at 60 % → 0 % alpha, rises 1 block over 1.5 s.
- The amount of wisp over a water surface goes **down** as pressure goes up (wisps per second: 6 at < 5 kPa, 2 at 5–30 kPa, 0 at ≥ 30 kPa). "Wisps = water escaping" is a readable cue for the boiling-point idea **[SME]**.

### 3.4 O2 fraction (%): fire, bubbles

**Input:** O2 fraction (%), bands. Breathability visuals use the derived breathable condition (O2 partial pressure, pressure, CO2 partial pressure).

O2 has no sky color (§1.2). Keyframes by O2 fraction; the flame bands are **simplified** and need SME numbers. The SME review notes flammability depends mostly on O2 percentage, with total pressure mattering more below ~41 kPa. **[SME]**

| O2 fraction | Flame look | Fire spread | Other cues |
|---|---|---|---|
| < 5 % (start 0.1 %) | **No flame.** Using a fire item gives a 3-particle grey smoke puff (`#8C8480`) and a "fizz" sound | None | Dome uses LED lamps |
| 5–12 % | **Struggling:** half-height flame, flickers at 2×, often goes out | None | — |
| 12–16 % | Small steady flame (0.75 height) | Very slow | — |
| 16–19 % | Normal flame | Slow | — |
| 19–23 % (fire-safety band) | Normal flame | Normal | — |
| > 23 % (overshoot) | **Large:** flame 1.25× height, sparks (2 px `#FFD27A`) from hot machines and dry flora | Fast | HUD warning (triangle + word) and BELL failure reports (story beat 15) |

- **Bubbles (local, real biology):** seeded water plants and algae release small bubble streams (2 px `#DDEFF5`, rising) while photosynthesizing. Algae vats bubble at a rate that follows their output. These show **where** O2 comes from.
- **Breathability is not an O2-only cue.** It depends on O2 partial pressure, total pressure, and CO2 (DESIGN.md §2). It is shown on the Monitor's **Air window** (style guide §11) and by the helmet (style guide §9).

### 3.5 CO2 (partial pressure, kPa): effects, not color

**Input:** CO2 partial pressure (kPa), linear; scrubber deposits use local machine output.

Start 0.57 kPa; it can rise in Phases 1–2 (outgassers, decay) and must end ≤ 0.5 kPa (DESIGN.md §1). No sky, fog, or water color is tied to CO2 (§1.2). Keyframes for the instrument and local cues:

| CO2 partial pressure | Weather-mast CO2 tube (of 12 px) | Carbonate deposits at a running scrubber | Plant growth look |
|---|---|---|---|
| 0.57 (start) | 1 px | — | — |
| 5 | 4 px | — | — |
| 20 | 9 px | Thick crust | Fast, lush new growth (CO2 is plant food) |
| 2 | 3 px | Crust building | Normal |
| ≤ 0.5 (equilibrium) | 1 px, with a "target" notch | Stable pile | Normal |

- **Scrubber deposits:** pale carbonate blocks (`#E2DDD0` / `#C9C2B2`, layered texture) build up beside each carbon scrubber, a visible stockpile of captured carbon. Real basis: mineral carbonation **[SME]**.
- **CO2 and temperature:** if CO2 falls before super-greenhouse gases hold the warmth, **frost returns** (§3.2). That connection is taught in the journal; the visual is temperature's.
- **Emitters are not CO2 plumes.** Super-greenhouse gases are colorless, so emitters show heat shimmer and a vent glow, never a colored smoke plume (DESIGN.md audit).

### 3.6 Biomass (biomass units, BU): ground and flora

**Input:** biomass (BU), linear; ground conversion is local block ticks near seeded flora.

Keyframes at 0, 50 (first patch), 150 (first self-sustaining biome, balance sheet §7), 750, 1,500 (equilibrium). Ground conversion is **local** (near seeded flora, by block ticks); the global value sets the particle density and ambient sound.

| Property | 0 | 50 | 150 | 750 | 1,500 |
|---|---|---|---|---|---|
| Ground near flora | Regolith | **Biocrust** speckle on treated soil | Soil + moss on wet ground | Grass, shrubs | Mature grass `#5C8C3D`, trees |
| Main flora color | — | `#43432F` (biocrust) | `#7E9150` (moss) | `#79A14B` (grass) | `#5C8C3D` + tree greens |
| Spore/pollen particles (per chunk near flora) | 0 | 1 | 3 | 6 | 8 (`#E8E0B0`, 2 px, slow drift) |
| Leaf rustle ambient | None | None | Faint | Normal | Normal + more variety |
| Thawling behavior near flora | — | — | Begin grazing near quiet zones | Settle, nest | Settled colonies |

- Ground conversion chain alternates dark and light (style guide §4.3), and each step changes the **texture** (crust polygons → tufts → blades), not only the color.
- Treated soil (perchlorate removed, DESIGN.md hazards) is darker brown `#5A4232`, with a fine cross-hatch (tilled) pattern, so "soil ready for seed kits" is visible.

### 3.7 Dust storms and radiation spikes (events)

**Input:** storm strength (0–1, derived weather state); radiation spike active × pressure.

Dust storms are not an Index value, but they drive the sky in Phase 1 and get weaker as pressure and biomass rise (DESIGN.md §3).

| Property | At full storm strength |
|---|---|
| Fog color | `#A8744E` |
| Sky zenith and horizon | Mixed 85 % toward `#9A6A48` |
| Fog start / end | 0.0 / 0.18 × render distance (floor 16 blocks) |
| Sun | Disc at 30 % opacity, no aureole |
| Particles | Dust specks 2×2 px (`#C08A5E`, `#8E5E3E`), drifting **slowly sideways**: no violent gusts, no flying debris (thin air can't do that; DESIGN.md audit) |
| Visor | Dust film builds at edges |
| Solar panels | Dusty texture variant (`solar_panel_dusty`), shows why power drops |
| Ramp in / out | 60 s / 90 s; no flashes |

**Maximum storm strength by stage:** Phase 1: 1.0 · Phase 2: 0.8 · Phase 3: 0.5 · equilibrium: 0.25 (light dust days). The Game Designer owns frequency; these caps keep the "storms die down" promise visible.

**Radiation spikes:** HUD dosimeter clicks (sound + subtitle) and a trefoil icon with a slow pulse (1 per 2 s); at night, an optional faint aurora glow along the horizon, `#7FD8A0` at 15 % overlay **[SME: is this a fair image of Mars aurora?]**. Spikes weaken with pressure (DESIGN.md), so the glow and click rate scale down with the pressure keyframes (100 % at 0.6 kPa → 20 % at 75 kPa).

### 3.8 Machine and hazard cues (local, from the LGD handoff)

| Cue | Look | Index link |
|---|---|---|
| **Regolith Outgasser plume** | A squat oven-like machine with a stack. Running: **white condensing vapor puffs** rise from the stack (water baked from soil freezes into a visible wisp in cold air), plus heat shimmer. The CO2 it releases is invisible, as in reality; the puff is the water. Puff size shrinks as temperature rises (warmer air, less condensation) **[SME]**. Worn: puffs irregular, status lamp triangle. Storm: puffs slower (power cut) | Pressure, temperature |
| **Ground-ice lenses (survey scan)** | When the player uses the survey scan, ice lenses under the ground show as **outlined translucent white blocks** with a diagonal-hatch pattern, through the terrain, for 10 s. Rock that is safe to build on shows a solid grey outline | Temperature, hydrosphere |
| **Ground collapse warning** | Before collapse, surface blocks over a melting lens switch to `regolith_cracked` (dark crack lines in a polygon pattern, a common look for ground over ice) plus thin vapor or drip particles and a creaking sound. Then the ground **sinks one block** with dust puffs (optional camera shake, off by default) | Temperature |
| **Insulation shroud** (shrouded machine) | A quilted, padded wrap over the machine (puffy squares, off-white `#D9D4C7` with grey seams), the stack fitted with a baffle. **No heat shimmer** and no frost-free ring around it. The status lamp stays visible through a cut-out. Thawlings that come near sniff, keep frills flat, and wander off | Fauna signature |
| **Lure station** | Low warm pad with a shallow water trough; gentle shimmer and steam. Thawlings cluster with frills fanned | Fauna signature |
| **Quiet zone** | Not a block the player has to read as "magic". A **marker post** (a survey stake with a round sign showing a sleeping-thawling pictogram), placed by the player to name the area, plus the natural look of land with **no machines**: frost undisturbed, thawling nests, grazing. On the Monitor map, quiet zones are hatched areas | Fauna, biomass |
| **Thawling later life stage** | See style guide §6.2: larger, longer dome, tall frills, broad scraper plate. Still an animal: no teeth, no spikes, no glowing eyes | Hydrosphere gate |

---

## 4. Gate snapshots: the whole world at each gate

The other values at each gate are **my assumptions** from the balance sheet's average rates (balance sheet §2–3: Phase 2 runs 54 sols, temperature +1.1 °C/sol, pressure +0.46 kPa/sol). They are only to show what the client would draw; the balance sheet is the source of truth.

| Gate | Pressure | Temp | Hydro | O2 | CO2 (kPa) | Biomass |
|---|---|---|---|---|---|---|
| Start | 0.6 | −60 | 0 | 0.1 % | 0.57 | 0 |
| Dome stable | ~0.74 | −60 | 0 | 0.1 % | ~0.6 | 0 |
| Phase 2 begins | 5 | −60 | 0 | ~0.2 % | ~4 | 0 |
| Thaw | ~22 | −20 | ~1 | ~1 % | ~15 | 0 |
| Hydrosphere | ~27 | ~−8 | 10 | ~3 % | ~18 | 0 |
| Phase 3 begins | 30 | 0 | ~12 | ~5 % | ~18 | 0 |
| Breathable | ≥ 75 | ~4 | ~25 | 22 % | ≤ 0.5 | ~1,200 |
| Equilibrium | ≥ 75 | ≥ 5 | ≥ 30 | 21.5–23 % | ≤ 0.5 | ≥ 1,500 |

### 4.1 Start — "Alone, tense"
Tan sky `#A87B5A` → `#D2AE86` horizon; far, warm haze; small sun with a wide dust glow; **blue sunset**. Heavy dawn frost on 70 % of the ground; frost rims on the dome glass at night. Silent outside, suit hiss inside. Rust regolith, dark dune sand, white ice in shaded craters. Inside the dome: warm LED light, Hana's notes, a dead tomato plant, the broken dish outside.

### 4.2 Dome stable — the inside changes, not the sky
Index barely moves (a machine-state gate). The visible change is **inside**: lamps stop flickering and go steady, all status lamps show solid squares, the dish is repaired (state swap), the relay icon appears on the Monitor. First Regolith Outgasser puffs appear outside.

### 4.3 Phase 2 begins (5 kPa) — "The sky shifts"
The sky has slid from tan to **rose-tan** (`#A0736C`), haze a little closer, windsock lifts to 15°, balloon off the ground, exterior sounds slightly louder. Frost unchanged (temperature still −60). Ice near heaters still gives **flash puddles** only at the low end; from 5 kPa, the ice-to-water rule allows water to stay if it is warm enough, but it isn't warm yet. The 30 s smoothing makes the shift happen over about two minutes after the gate; story beat 7 plays as it does.

### 4.4 Thaw (−20 °C, ~22 kPa) — "Wonder, then unease"
Sky **mauve-lavender** (between 15 and 30 kPa keys), fog noticeably closer. Frost only on 35 % of the ground and gone by mid-morning. **Day puddles that freeze at night** around warm machines; ice-crack sounds. Heat shimmer at emitters. Thawling **tracks** in the dust near machines, then the first juveniles with frills fanned toward the heat. Fridge B's padlock hangs open.

### 4.5 Hydrosphere (10 %) — "Conflict peaks"
Clouds thicker and lower (20 %), morning ground fog by water. **Pools** with no wisps where it stays warm; water still silty grey-teal `#4F6A70`. Adults with tall frills scrape at machines (scrape overlays). **Cracked ground** around warm machines, first collapses. Salt crust where meltwater dried. Shrouded machines look quilted and cold; lure stations steam.

### 4.6 Phase 3 begins (30 kPa, 0 °C) — "Storms fade"
Sky **slate-lavender blue** `#727EAC`; sunset halo has turned neutral-warm (`#CCA388`). Storms capped at half strength. Frost only in shade. Water stable, no wisps, bluer. First treated soil and **biocrust** patches near seed kits.

### 4.7 Breathable (O2 pp ≥ 16 kPa, ≥ 75 kPa, CO2 ≤ 0.5 kPa) — "Breathe"
Sky blue, clouds and rain, moss and grass spreading, bubbles in ponds, normal flames. The change is **the helmet coming off** (style guide §9.2): the world was already this color; the sound world opens.

### 4.8 Equilibrium — "Earned relief"
Deep blue zenith `#4A7AC2`, pale horizon, **orange sunsets** (`#F2A35E` halo, `#D9714F` band): the reverse of the blue Mars sunset at the start. Regular rain, green ground, settled thawlings nesting in quiet zones, spore drift, full sound. The beacon sequence plays in this world (style guide §10).

---

## 5. Going backwards (regression)

Values can fall (DESIGN.md §1); the world shows it, because hiding it would break "every change is visible" and B1.

| Falling value | What the player sees |
|---|---|
| Pressure | Sky slides back toward tan; fog recedes; windsock droops; sound muffles |
| Temperature | Frost returns; puddles freeze; pools get ice sheets |
| Hydrosphere | Pools shrink by block ticks; clouds thin |
| O2 | Flames shrink; at < 5 % fire can't burn; if breathability fails, the **visor returns** with a warning |
| CO2 above target | Gas tube rises on the weather mast; Monitor warning |
| Biomass | Grass goes back to soil, then biocrust (no instant snap) |

Gates stay unlocked (lock-in), but the look always tells the truth about the planet now.

---

## 6. Open items for review

| Item | Who | Type |
|---|---|---|
| Tan → rose → lavender → blue sky path; equilibrium blue at 1.9× Earth's air column (§2.2, §2.3) | SME | Well-structured (check) |
| Replace estimated start colors with pixel samples from calibrated rover images | SME + Lead Artist | Well-structured |
| Flame bands by O2 fraction (§3.4) | SME | Well-structured |
| Outgasser vapor-puff explanation; wisp rate vs. pressure; carbonate deposits; windsock physics; aurora glow | SME | Well-structured |
| Map the input names in §1.2a to real data keys; confirm storm strength is exposed as a derived value | Technical Lead | Well-structured |
| Weather mast as a new prop outside the dome | Lead Game Designer | Well-structured (design addition) |
| Cloud coverage changes need a custom cloud renderer | Technical Lead (cost) | Well-structured |
