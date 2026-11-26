# Planetary Keyframes — How the World Looks at Every Index Stage

**Owner:** Lead Artist · **Status:** Draft 2 (2026-11-26, round 2: SME fixes, data keys, weather mast, first storm storyboard) · **Stability:** all values here are **fluid** (TEAM.md 8.1); expect changes after every playtest.
**Answers:** consistency review **M-17** (a visible cue for each of the six Planetary Index values, not just pressure and O2) and the Lead Game Designer's draft-2 handoff (outgasser plume, ice states, ground collapse, shrouds, quiet zones).
**Depends on:** [DESIGN.md](../../DESIGN.md) §1–3 (Index, gates, ice-to-water rule, hazards), [balance sheet](../design/balance-sheet.md), [SME accuracy review](../science/accuracy-review.md), [style guide](style-guide.md).
**Reference swatches:** [palettes/palette-sheet-v1-placeholder.png](palettes/palette-sheet-v1-placeholder.png) (placeholder; the hex values in this file win if they ever differ).

> **SME review (round 2, 2026-11-26, [docs/science/round2-review.md](../science/round2-review.md) §2, §4, §5) is applied in this draft:** sky path accepted (rose Phase 2 sky stays, **with a required label**, §2.3); air column "about 2×"; flame bands corrected (§3.4); aurora covers the whole sky and scales with the solar spike only (§3.7); ice states and heavy wisps now follow the **boiling point at current pressure** (§3.3); weather mast fixes (§3.9). Rows still marked **[SME]** are not yet checked.

---

## 1. How this works

### 1.1 One parameter, one main channel

Players learn cause and effect (B1) only if they can tell **which** change caused **which** look. So each Index value owns a main visual channel that nothing else drives, plus backup channels so color is never the only signal (style guide §4.1).

| Index value | Main world channel (owned) | Backup channels | Where it shows |
|---|---|---|---|
| **Pressure** | **Sky color** (zenith, horizon), **fog distance**, **sunset color** | Windsock angle, lift balloon height, barometer, exterior sound level, cloud height | Global |
| **Temperature** | **Frost** (dawn frost cover, dome-glass frost, burn-off time) | Ice block behavior (with pressure), ice-crack sounds, spirit thermometer on the weather mast | Global + local (near machines) |
| **Hydrosphere** | **Water**: ice state, puddles, pools, water color | Clouds, rain, morning ground fog | Global + local |
| **O2** | **Flame**: whether fire can burn, flame size, fire spread | Bubbles from algae and water plants, weather-mast gas analyzer (from Phase 3), HUD | Local (wherever fire or flora is) |
| **CO2** | **No color of its own** (see 1.2). Its effects: carbonate deposits at scrubbers, plant growth, and frost returning if CO2 drops before greenhouse gases hold the heat | Weather-mast gas analyzer (from Phase 3), Monitor | Local + via temperature |
| **Biomass** | **Ground**: regolith → biocrust → soil → moss → grass, and flora blocks | Spore/pollen particles, leaf rustle sound, thawlings settling | Local, spreading |
| *(Dust storms, derived)* | Storm overlay on sky and fog (§3.7) | Dust particles, visor dust film, solar panels dimming | Global event |

### 1.2 Rule: we don't paint invisible gases

O2 and CO2 are colorless. If the sky turned "oxygen blue" or "CO2 brown", the game would teach a false idea (a negative-learning risk under DESIGN.md fantasy rule 3). So:

- **The sky is driven by pressure and dust,** which are what really color a sky (a thicker atmosphere scatters more blue light; suspended dust makes the sky tan).
- **O2 and CO2 show through what they do:** fire, plant growth, bubbles, carbonate deposits, temperature. "You can't see the gas, but you can see its effects" is itself a good science lesson (A5).
- **An in-world instrument shows the gas mix:** the **weather mast** (§3.9), adopted by the user (2026-11-26), standing outside the dome where it can be read from a dome window.

ARCHITECTURE.md §2.5 now lets any Index parameter or derived value drive sky and fog, with the Lead Artist choosing. **Choice:** sky and fog are driven by **pressure, storm strength, and hydrosphere**. O2 and CO2 drive **no** sky or fog value.

### 1.2a Inputs: which value drives which keyframe

Data keys are the Technical Lead's mapping (ARCHITECTURE.md §2.5.2). A **signal** is a read-only world-state value published by one module (ARCHITECTURE.md §2.5.1).

| Visual (section) | Driving input | Data key | Axis | Kind | Smoothing |
|---|---|---|---|---|---|
| Sky zenith, horizon, night sky, sunset halo and band, fog color, fog start/end, sun aureole, star density, cloud height (§3.1) | **Pressure** (kPa) | `mars:pressure` | log10 | Index parameter | 30 s |
| Storm overlay on sky, fog, sun; dust particles (§3.7) | **Storm strength** (0–1; capped by gameplay phase) | `mars:storm_strength` | linear | Signal (`hazards`) | 0 (server ramps) |
| Aurora glow (§3.7) | **Solar spike strength** at the top of the air (not pressure; SME round 2 §2.8) | `mars:solar_spike` (signal, added by the TL in round 3). Not `mars:surface_radiation`, which already includes pressure | linear | Signal | 0 |
| Dosimeter click rate (§3.7, audio direction §6.2) | Radiation reaching the surface × local shielding | `mars:surface_radiation` × local shielding | linear | Signal + local | 0 |
| Windsock angle, lift balloon height, barometer needle (§3.1, §3.9) | **Pressure** | `mars:pressure` | log10 | Index parameter | 30 s |
| Exterior ambient beds (thin, mid, full) and exterior gain (§3.1; [audio direction](../audio/audio-direction.md) §3, §9) | **Pressure** | `mars:pressure` (`keyframes/ambient.json`) | log10 | Index parameter | 30 s |
| Dawn frost cover, burn-off time, dome-glass frost, mast thermometer (§3.2, §3.9) | **Temperature**, global mean (°C) | `mars:temperature` | linear | Index parameter | 30 s |
| Frost-free ring and heat shimmer particles near machines (§3.2) | **Local temperature** | `local_temperature` (client-computed from `mars:temperature` + day/night + `mars:chunk_heat`) | threshold | Derived (client) | — |
| Ice state: boil away / day puddle / pool / rare sublimation (§3.3) | Local temperature vs. **boiling point** | **Server block changes**, not keyframes (rule uses `mars:boiling_point`) | rule table | Server | — |
| Heavy and light vapor wisps (§3.3) | Local temperature vs. **boiling point at current pressure** | `local_temperature`, `mars:boiling_point` (synced) | threshold | Derived | — |
| Water color, underwater fog, cloud coverage and color, rain, ground fog (§3.3) | **Hydrosphere** (% of surface) | `mars:hydrosphere` | linear | Index parameter | 30 s |
| Flame size, flicker, fire spread (§3.4) | **O2 fraction** (%) and the pressure guard | `mars:o2_fraction` (0–100), `mars:pressure` | bands | Index parameter | — |
| Helmet on/off, visor return, Air window dot (§3.4, style guide §9, §11) | **Breathable condition** | `mars:breathable_air` (parts: `mars:pressure`, `mars:o2_partial`, `mars:co2_partial`) | live check | Live condition | — |
| Bubbles from algae vats and water plants (§3.4) | Machine / flora block output | Block entity field | linear | Local | — |
| Weather-mast gas analyzer gauges (§3.5, §3.9) | **O2 and CO2 partial pressure** (kPa) | `mars:o2_partial`, `mars:co2_partial` | linear (O2), square root (CO2) | Derived | 30 s |
| Carbonate deposits at scrubbers (§3.5) | Scrubber cumulative output | Block entity field | step | Local | — |
| Ground conversion, flora color (§3.6) | Seeded flora nearby (block ticks), probability scaled by **biomass** | Blocks + `mars:biomass` | ticks | Local + Index | — |
| Spore particles, rustle ambient (§3.6) | **Biomass** (BU) | `mars:biomass` | linear | Index parameter | 30 s |
| Outgasser plume puff size (§3.8) | **Temperature** (global mean) | `mars:temperature` | linear | Index parameter | 30 s |
| Thawling frills, settling (§3.8, style guide §6) | **Fauna pressure** and quiet-zone state | `mars:fauna_pressure` + block entities | state | Signal + local | — |

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
| Performance | Values are looked up when the Index changes (at most once per second), not per frame (ARCHITECTURE.md §2.5). **No post-process shaders.** Heat shimmer ships as **particles only** (TL decision, ARCHITECTURE.md §2.5.4): it carries meaning, so it must look the same for every player |

**Data shape** (schema owned by the Technical Lead, ARCHITECTURE.md §2.5.2; files live in `pack/assets/mars/keyframes/`: sky, fog, clouds, ambient, mast). `smoothing_s` is per file (30 s for Index parameters; **0 for signals** such as `mars:storm_strength`, which already ramp on the server). `overlay` is the storm layer (§3.7):

```json
{
  "input": "mars:pressure", "axis": "log10", "space": "oklab", "smoothing_s": 30,
  "overlay": { "input": "mars:storm_strength", "smoothing_s": 0, "mix": "toward",
               "targets": { "sky_zenith": "#9A6A48", "sky_horizon": "#9A6A48", "fog": "#A8744E" }, "max_mix": 0.85 },
  "keys": [
    { "at": 0.6,  "sky_zenith": "#A87B5A", "sky_horizon": "#D2AE86", "fog": "#C49A73",
      "sunset_halo": "#7E9CC4", "sunset_band": "#8A6A55", "night": "#0D0B0E",
      "fog_start": 0.70, "fog_end": 1.00 },
    { "at": 75.0, "sky_zenith": "#6A93CF", "...": "..." }
  ]
}
```

---

## 2. Grounding: the real starting Mars

### 2.1 What real imagery and data say

**No source here was opened this session** (network to NASA and reference sites was blocked). Citations are from the artist's knowledge; the SME should confirm them. **Hex values are my estimates from published descriptions and calibrated images I know, not pixel samples.** Before Stage 4, sample calibrated "approximate true color" images (Mastcam, Pancam) to replace them (handoff to SME).

| Real observation | Source (not opened; **unverified** details) | What we take from it |
|---|---|---|
| The daytime sky is a pale **butterscotch / tan**, because fine suspended dust (particles around 1.5 µm, size **unverified**) absorbs blue light and scatters red | Smith & Lemmon (1999), Mars Pathfinder imager opacity results, *JGR Planets*; Lemmon et al. (2004), "Atmospheric imaging results from the Mars Exploration Rovers", *Science* 306; **Bell, Savransky & Wolff (2006), "Chromaticity of the Martian sky", *JGR* 111, doi:10.1029/2006JE002687** (best source for sky color: color tracks dust opacity; SME [S16]) | Start sky: tan, brighter near the horizon, slightly darker and redder at the zenith |
| Published sky colors vary (tan, butterscotch, salmon, "pinkish") with dust load, time of day, and **image processing** (approximate true color vs. white-balanced). Early Viking releases were mis-colored | Bell et al. (2003), Pancam investigation and calibration, *JGR* | Our start sky is a **mid** dust day, not a storm day. "Pink" descriptions of today's Mars exist; see §2.3 |
| **Sunsets are blue** near the sun: dust scatters blue light forward, close to the sun's direction | NASA/JPL-Caltech/MSSS, Curiosity Mastcam sunset in Gale Crater, PIA19400 (sol 956, April 2015) | Start sunset halo: cool blue. This flips to warm orange at equilibrium (§3.1) |
| The Sun looks about **2/3 as wide** and gives about **43 %** of Earth's light | Geometry: Mars at ~1.52 AU (1/1.52 ≈ 0.66; 1/1.52² ≈ 0.43) | Sun quad drawn at **0.66×** vanilla size. Light level is **not** lowered (gameplay readability; stylized) |
| Surface: bright **reddish dust** (iron oxides) over **darker basaltic sand**; fresh rock under the dust is **grey** | Curiosity's first drill hole ("John Klein", 2013) showed grey powder; dark dunes in Gale crater (Bagnold dunes) | Three ground families: rust regolith, bright dust, dark basalt sand; grey for drilled or broken rock |
| Exposed **ground ice vanished** over a few days without melting, by sublimation into cold, dry air | Phoenix lander, "Dodo-Goldilocks" trench (2008, sols 20–24; confirmed, SME [S1]) | **Supports slow sublimation of cold ice only.** Phoenix was **above** the triple point (SME [S2]), so it is **not** evidence for below-triple-point physics. Used for the high school read-more line, not for the Phase 1 heater look (that is a flash puddle, §3.3) |
| **Frost** forms on the ground | Viking Lander 2, Utopia Planitia, frost images (1979) | Dawn frost cover keyed to temperature (§3.2) |
| Thin **water-ice clouds** occur today | Curiosity cloud observations (several campaigns, e.g. 2019, 2021) | Faint high wisps even at hydrosphere 0 (§3.3) |
| Mars has **aurora**: diffuse aurora from solar energetic particles; the first **visible** aurora seen from the surface was **green** (oxygen, 557.7 nm), during a March 2024 solar storm | MAVEN diffuse aurora (Schneider et al., 2015, *Science*); **Knutsen et al. (2025), *Science Advances* 11, eads1563** (Perseverance; verified by SME [S18]) | Optional faint green **whole-sky** night glow during solar spikes, scaled by spike strength only (§3.7) |
| **Phobos** orbits faster than Mars spins, so it rises in the west and sets in the east | Phobos orbital period ~7.7 h < 24.6 h sol | Optional night-sky detail (asset list) |

### 2.2 What happens as the air thickens (physics reasoning; SME round 2: accurate in direction)

This is my own reasoning, for the SME to check:
- A blue sky comes from **Rayleigh scattering** by gas molecules. It grows with the **mass of air above you** (pressure ÷ gravity).
- Mars's gravity is about 0.38 of Earth's, so the same surface pressure means about **2.6×** more air overhead. At **75 kPa** on Mars, the column is **about 2×** Earth's (75/101.3 × 9.81/3.71 = 1.96; SME round 2 §2.2).
- So at equilibrium, **with low dust**, the sky should be **blue**. SME round 2 §2.3: with twice Earth's air overhead, more light scatters more than once, so the sky is most likely a **brighter, paler, whiter blue** with a whiter horizon, not a deeper zenith (physics reasoning, not a published simulation). The 75 kPa key now follows that (§3.1).
- In between, weak blue scattering mixes with the warm dust glow. A mix of warm tan and cool blue light gives a **desaturated, cooler, greyish-rose to lavender** sky, not a clean pink.
- The sunset flips: with thick air and little dust, blue is scattered **out** of the long sunset path, so sunsets turn **orange-red**, like Earth's.

### 2.3 Stylization log (deliberate departures)

| Where | Real / expected | What we do | Why |
|---|---|---|---|
| Phase 2 sky (5–15 kPa) | A subtle, dust-dependent shift toward cooler, greyer, slightly purple (SME round 2 §2.1: direction accurate; saturation and timing exaggerated) | A clearly **rose / mauve** tint (`#A0736C` → `#927489` zenith) | The Phase 2 gate needs a visible sky change (story beat 7: "the sky shifts"). My first, more realistic value was below the visible threshold (style guide §4.2). User decision: keep, labeled stylized. **Required label** (journal, Phase 2 gate, both bands; SME wording): *"More air scatters more blue light, so the sky slowly loses its dusty tan. Nobody has seen a thickened Mars sky. These in-between colors are our best guess, made stronger so you can see them."* It also gets the SIMPLIFIED badge (style guide §7.4). Handoff: Lead Game Designer (journal text) |
| Light level | Mars sunlight is ~43 % of Earth's | Vanilla light levels | Play readability; light level is not a learning goal |
| Equilibrium sky | Brighter, paler, whiter blue than Earth's, if dust is low (SME round 2 §2.3, reasoning) | `#6A93CF` zenith (was `#4A7AC2`), whiter horizon `#B8CDE2` (was `#A9C3DF`) | Follows the SME's optional note; low stakes (sky color is not taught as a fact). Deliberately **not** vanilla Minecraft's sky color |
| Fog getting closer as pressure rises | More air = more haze over distance (aerial perspective) is real; Mars today also has dust haze | Fog start pulls in from 70 % to 30 % of render distance | Makes "thicker air" visible (DESIGN.md: "thicker sky fog") |
| Salt crust (perchlorate) | Perchlorate in soil is not visible | Pale salt crust where brine dried | Gives the real hazard a place to be seen; journal says the salts are invisible in real soil **[SME wording]** |
| Flame thresholds | Real flame limits depend on O2 fraction and total pressure | Bands by O2 fraction from sourced ignition limits, plus a 20 kPa pressure guard (§3.4) | Readable; SME round 2 corrected the numbers; the guard value is invented |

---

## 3. Keyframes by Index value

Snapshot values at gates for the other five values are in §4. All hex values are sRGB.

### 3.1 Pressure (kPa): sky, fog, sunset, sound

**Input:** pressure (kPa), log10 axis.

Keyframes at 0.6 (start), 5 (Phase 2 gate), 15, 30 (Phase 3 gate), **75 (equilibrium target)**. Interpolate on log10(pressure). Values are for **clear weather at midday**; the storm overlay (§3.7) mixes on top.

| Property | 0.6 kPa | 5 kPa | 15 kPa | 30 kPa | 75 kPa |
|---|---|---|---|---|---|
| Sky zenith | `#A87B5A` | `#A0736C` | `#927489` | `#727EAC` | `#6A93CF` |
| Sky horizon | `#D2AE86` | `#CDA696` | `#C8ABAA` | `#C0BAC3` | `#B8CDE2` |
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
| Lift balloon (weather mast) | On the ground, slack | 2 blocks up | 4 | 6 | 8 blocks up, taut |
| Exterior sound gain (audio direction §3.2; SME ✓) | 0.25 | 0.40 | 0.60 | 0.75 | 1.00 |
| Exterior low-pass cutoff (audio direction §3.2; SME ✓; baked into pre-filtered beds) | 1.5 kHz | 3 kHz | 5 kHz | 8 kHz | Off |

**Measured step sizes** (OKLab ΔE; ~0.02 is just noticeable; target ≥ 0.035, style guide §4.2):

| Step | Zenith | Horizon | Fog | Sunset halo | Weakest simulated (zenith) |
|---|---|---|---|---|---|
| 0.6 → 5 | 0.041 | 0.036 | 0.036 | 0.018 | 0.023 (tritanopia) |
| 5 → 15 | 0.047 | 0.026 | 0.034 | 0.044 | 0.037 |
| 15 → 30 | 0.067 | 0.043 | 0.049 | 0.087 | 0.054 |
| 30 → 75 | 0.068 | 0.055 | 0.079 | 0.074 | 0.061 (deuteranopia) |

(30 → 75 row rechecked 2026-11-26 after the paler equilibrium sky; both new values pass in all three simulations: zenith ≥ 0.061, horizon ≥ 0.048.)

Where a step is weak (0.6 → 5 sunset halo; tritanopia at 0.6 → 5), the **windsock, balloon, fog distance, and sound** change at the same time, so the Phase 2 change never relies on color alone.

**Physics note on the weather mast:** wind force on a windsock grows with air density, so in thin air a normal windsock barely lifts. The Phoenix lander's "telltale" was built extra-light so Mars wind could move it. Our windsock is a normal one; its rising with pressure is real physics (SME round 2 §2.7: accurate; Mars air today is about 1.2 % of Earth's density, about 77 % at 75 kPa, calc). The **lift balloon** is an acceptable simplification: lift grows with air density; its height is really set by its line. Text: "Balloons float only when the air is thick enough to hold them up."

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
- **Heat around machines (local):** emitters, outgassers, and extractors melt frost in the chunks their heat reaches (the TL's per-chunk heat map; a 3-block soft edge is optional polish) and show **heat shimmer**. Shimmer is a **particle** (faint rising 8×8 wavy particle, 3 frames, `#FFFFFF` at 12 % alpha, 2–4 per second per running heat source). It is the only shipped version (TL decision); a shader may come later only as an extra on top. This also shows why thawlings come to machines.
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

**Ice and water states (follows the ice-to-water rule as corrected by the SME, round 2 §1.1).** Each warmed ice or water block compares its **local temperature** with the **boiling point of water at the current pressure** (the Monitor's derived value: 0 °C at 0.61 kPa, about 33 °C at 5 kPa, SME). Pressure passes the triple point (0.611 kPa) in about the first sol, so the below-triple-point state is rare; the **Phase 1 look near a heater is the flash puddle**. DESIGN.md's table still shows the old rule; the Lead Game Designer is updating it (SME handoff), and these visuals follow the corrected rule. These are the core A2 visuals, so each has its own look that does not rely on color.

| State | When (corrected rule) | What the player sees | Assets |
|---|---|---|---|
| **Flash puddle** (Phase 1 near a heater) | Local temp **at or above the boiling point** at current pressure | A thin puddle (1 px water layer) appears, **bubbles and steams hard** for 2–4 s, then vanishes. Short-lived, clearly "boiling away" | `puddle_boiling` (4 frames), `vapor_wisp` dense |
| **Day puddle, night ice** (Thaw) | Local temp between 0 °C and the boiling point by day, ≤ 0 °C at night | By day, a puddle (2 px) with light wisps. At night it turns into a **thin ice sheet** (white rime pattern on a flat layer). Next day, puddle again. The cycle is the visual: warmth comes and goes, so does water | `puddle`, `puddle_frozen` |
| **Pooling water** (Hydrosphere) | Local temp stays between 0 °C and the boiling point; hydrosphere rising | Full water blocks, **no heavy wisps**, gentle surface animation; edges darken the ground (wet regolith `#6E4430`). Pools grow outward by block ticks | `mars_water` (still/flowing), `wet_regolith` |
| **Stable seas** | ≥ 30 kPa and ≥ 0 °C | Same as pooling, plus water color moving toward blue (table above) | — |
| *Sublimating ice (rare)* | Pressure below 0.611 kPa (about the first sol only), warmed ice | The ice block shrinks in 4 steps (16 → 12 → 8 → 4 px), puffing vapor wisps; no water. Kept because a player who warms ice on sol 1 must see the right thing; not a planned teaching moment | `water_ice_shrink_1..3`, `vapor_wisp` |

- **Vapor wisp particle:** 8×8, 4 frames, `#F4F1EA` at 60 % → 0 % alpha, rises 1 block over 1.5 s.
- **Heavy wisps** (6 per second) appear when the local temperature is **near or above the boiling point** at current pressure ("near" = within 5 °C: an **invented** game margin, flagged for the balance pass). They are the visible sign of boiling (A2). As pressure rises, the boiling point rises, so heavy wisps over ordinary warm water die out on their own.
- **Light wisps** (1 per second) over water on cold mornings, at **any** pressure, because steam over water warmer than the air also happens on Earth (sea smoke; SME round 2 §2.6). The journal never says "no wisps means no evaporation".

### 3.4 O2 fraction (%): fire, bubbles

**Input:** O2 fraction (%), bands. Breathability visuals use the derived breathable condition (O2 partial pressure, pressure, CO2 partial pressure).

O2 has no sky color (§1.2). **Corrected per SME round 2 §2.4:** the first draft showed flames at 5–16 % O2, which would teach "fire burns in air with a third of Earth's oxygen". The bands are **game bands derived from sourced ignition limits** (about 14.1 % for rolled newspaper, 16 % for common plastics, 17.0 % for untreated wood, 23.5 % "oxygen-enriched" per OSHA; SME [S17], AR [9]); the band edges are rounded game choices.

**Minimum-pressure guard:** no open flame below **20 kPa total pressure**, whatever the O2 fraction (same burning-is-harder-in-thin-air direction as AR [18]). **20 kPa is an invented balance number** (SME: no sourced value found; chosen below the Phase 3 gate so it never conflicts with the O2 rise in play). It stays out of journal text. Input: `mars:pressure`.

| O2 fraction (and pressure ≥ 20 kPa) | Flame look | Fire spread | Other cues |
|---|---|---|---|
| < 14 % (start 0.1 %), **or pressure < 20 kPa** | **No flame.** Using a fire item gives igniter clicks and a 3-particle grey smoke puff (`#8C8480`) | None | Dome uses LED lamps |
| 14–17 % | **Struggling:** half-height flame, flickers at 2 Hz (under the 3 Hz flash limit), often goes out | None | — |
| 17–19 % | Small steady flame (0.75 height) | Very slow | — |
| 19–23 % (fire-safety band) | Normal flame | Normal | — |
| > 23 % (overshoot) | **Large:** flame 1.25× height, sparks (2 px `#FFD27A`) from hot machines and dry flora | Fast | HUD warning (triangle + word) and BELL failure reports (story beat 15) |

- **Bubbles (local, real biology):** seeded water plants and algae release small bubble streams (2 px `#DDEFF5`, rising) while photosynthesizing. Algae vats bubble at a rate that follows their output. These show **where** O2 comes from.
- **Breathability is not an O2-only cue.** It depends on O2 partial pressure, total pressure, and CO2 (DESIGN.md §2). It is shown on the Monitor's **Air window** (style guide §11) and by the helmet (style guide §9).

### 3.5 CO2 (partial pressure, kPa): effects, not color

**Input:** CO2 partial pressure (kPa), linear; scrubber deposits use local machine output.

Start 0.57 kPa; it can rise in Phases 1–2 (outgassers, decay) and must end ≤ 0.5 kPa (DESIGN.md §1). No sky, fog, or water color is tied to CO2 (§1.2). Keyframes for the instrument and local cues:

| CO2 partial pressure | Weather-mast CO2 gauge pointer (of 12 px scale; §3.9) | Carbonate deposits at a running scrubber | Plant growth look |
|---|---|---|---|
| 0.57 (start) | 1 px | — | — |
| 5 | 4 px | — | — |
| 20 | 9 px | Thick crust | Fast, lush new growth (CO2 is plant food) |
| 2 | 3 px | Crust building | Normal |
| ≤ 0.5 (equilibrium) | 1 px, with a "target" notch | Stable pile | Normal |

- **Scrubber deposits:** pale carbonate blocks (`#E2DDD0` / `#C9C2B2`, layered texture) build up beside each carbon scrubber, a visible stockpile of captured carbon. Real basis: mineral carbonation (SME round 2 §2.9: accurate; CarbFix turned over 95 % of injected CO2 into carbonate in under 2 years [S19]).
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

**Inputs:** `mars:storm_strength` (signal, 0–1); solar spike strength (`mars:solar_spike`, TL round 3) for the aurora; `mars:surface_radiation` (already pressure-attenuated) for the dosimeter.

Dust storms are not an Index value, but they drive the sky in Phase 1 and get weaker as pressure and biomass rise (DESIGN.md §3).

| Property | At full storm strength |
|---|---|
| Fog color | `#A8744E` |
| Sky zenith and horizon | Mixed 85 % toward `#9A6A48` |
| Fog start / end | 0.0 / 0.18 × render distance (floor 16 blocks) |
| Sun | Disc at 30 % opacity, no aureole |
| Particles | Dust specks 2×2 px (`#C08A5E`, `#8E5E3E`), drifting **slowly sideways**: no violent gusts, no flying debris (thin air can't do that; DESIGN.md audit) |
| Visor | Dust film builds at edges |
| Solar panels | **No texture change during the storm**: the airborne-dust overlay above (fog, sky, particles, visor film) carries the look. Panels gain dust stages **when the storm ends** (DESIGN.md "Solar panel dust"; style guide §2.4) |
| Ramp in / out | 60 s / 90 s; no flashes |

**Maximum storm strength by stage:** Phase 1: 1.0 · Phase 2: 0.8 · Phase 3: 0.5 · equilibrium: 0.25 (light dust days). The Game Designer owns frequency; these caps keep the "storms die down" promise visible.

**Radiation spikes:** HUD dosimeter clicks (sound + subtitle) and a trefoil icon with a slow pulse (1 per 2 s); at night, an optional faint **green aurora over the whole sky**: `#7FD8A0` overlay at up to 12 % across the sky, rising to 15 % toward the horizon, fading in and out over ≥ 2 s (no flashing). Real basis: Perseverance's visible green (557.7 nm oxygen) aurora during a March 2024 solar storm (Knutsen et al., 2025, *Science Advances* 11, eads1563; SME [S18]); diffuse aurora covers the sky fairly evenly (SME, detail not opened). **The aurora scales with solar spike strength only, not pressure** (SME round 2 §2.8: it glows tens of kilometers up; thicker air near the ground shields *you*, it doesn't switch off the aurora). What weakens with pressure is the **dose at the surface**: the dosimeter clicks follow `mars:surface_radiation`, which already includes the pressure attenuation (100 % at 0.6 kPa → 20 % at 75 kPa). So at equilibrium a strong spike still lights the sky, while the clicks stay slow: a good A4 contrast.

### 3.7a The first dust storm: art and audio storyboard (for the joint pass)

DESIGN.md picks the first dust storm (story beat 4, F1; goals **C1, B3**) as the first experience to design in detail, as a joint pass of the Lead Game Designer, Lead Instructional Designer, and Lead Artist. This is the art and audio contribution, laid on the timeline we already have (ui-specs §0 delivery, ARCHITECTURE.md storm envelope). Times are simulation time; 1 sol = 20 minutes. **Who owns what:** timing of warning and forecast = Lead Game Designer and Lead Instructional Designer; the look and sound = me.

| Step | Time | What the player sees | What they hear | Goals |
|---|---|---|---|---|
| 1. Hint in the world | Before the warning | Horizon haze slowly thickens; the windsock twitches; sun aureole widens a little (storm strength still 0; a **pre-storm haze**: fog end −10 % over 0.25 sol, proposed) | Nothing new | C1 (cue to notice) |
| 2. Warning | When BELL warns (time: LGD) | Log line + HUD storm icon (shape) with countdown; Monitor "Storm forecast" in the right strip | Warning tone (AU-U03), subtitle "Warning logged" | C1 |
| 3. Forecast (F1) | Next natural pause, before lock (LGD/ID) | Forecast card; the solar panel row on the Monitor shows today's output so the player has data to reason from | Forecast cue (AU-U08) | **B3** |
| 4. Arrival | Storm envelope ramps in over 60 s | Sky and fog mix toward the storm colors (§3.7), fog closes to 18 % of render distance, dust drifts **slowly sideways**, sun disc fades to 30 % | Grain patter on the visor (AU-W01), muffled hiss (AU-W02); inside the dome, patter on glass (AU-W03) | C1 |
| 5. Power drops | When solar output falls below demand | **Panels keep their current dust stage** (the storm's dust is in the air, not yet on the panels; the airborne overlay carries the look); dome lamps flicker once, then hold; Monitor power row falls with its cause line "Storm: less sunlight −{n} %" (separate from any "Panel dust" line); Outgasser puffs slow | Dome hum drops 2 semitones (AU-W04); Outgasser chuffs slow | **C1**, B1 |
| 6. Riding it out | Storm length (0.5–1 sol) | Visor dust film at the edges; battery bar falling; failure report only if the player let reserves run out (with Hana's tip the first time) | Failure tone only if something fails | C1, C2 |
| 7. Clearing | Ramp out over 90 s | Fog pulls back, sun returns. **As the air clears, panels gain their storm dust stages** (+1, or +2 while the storm cap is ≥ 0.8: DESIGN.md), stepping through the textures over the ramp-out, and the "Panel dust −{n} %" line rises. They stay dusty until cleaned with the brush or wiper (a clear chore with a reason) | Patter fades; hum returns to pitch | C1 |
| 8. Result | Next natural pause | Forecast result first in the queue (LEARNING.md §4.6); Monitor graph shows the dip with the cause callout on it (style guide §13.3) | Forecast cue (neutral, right or wrong) | **B3**, B1 |

**Art and audio rules for this experience:** no flashes, no camera shake, no howling wind (thin air); the visor keeps the center clear; every step has a visual and a sound twin; the storm's look and gameplay effects both follow `mars:storm_strength` (Game Designer: caps scale both). **Pre-storm haze (step 1):** the Lead Instructional Designer accepts it (round 3) **on one condition: the solar output number must not drop before the storm starts**, so the haze is look only and doesn't reveal F1's answer. So the haze changes fog and aureole only; `mars:storm_strength` stays 0 and solar output is untouched until arrival. Lead Game Designer to confirm in the joint pass.

### 3.8 Machine and hazard cues (local, from the LGD handoff)

| Cue | Look | Index link |
|---|---|---|
| **Regolith Outgasser plume** | A squat oven-like machine with a stack. Running: **white condensing vapor puffs** rise from the stack (water baked from soil freezes into a visible wisp in cold air), plus heat shimmer. The CO2 it releases is invisible, as in reality; the puff is the water. Puff size shrinks as temperature rises (warmer air, less condensation; SME round 2 §2.5: accurate). Journal (MS, SME wording): "The white puff is water vapor freezing into tiny ice crystals, like your breath on a cold day. The CO2 coming out is invisible." Worn: puffs irregular, status lamp triangle. Storm: puffs slower (power cut) | Pressure, temperature |
| **Ground-ice lenses (survey scan)** | When the player uses the survey scan, ice lenses under the ground show as **outlined translucent white blocks** with a diagonal-hatch pattern, through the terrain, for 10 s. Rock that is safe to build on shows a solid grey outline | Temperature, hydrosphere |
| **Ground collapse warning** | Before collapse, surface blocks over a melting lens switch to `regolith_cracked` (dark crack lines in a polygon pattern, a common look for ground over ice) plus thin vapor or drip particles and a creaking sound. Then the ground **sinks one block** with dust puffs (optional camera shake, off by default) | Temperature |
| **Insulation shroud** (shrouded machine) | A quilted, padded wrap over the machine (puffy squares, off-white `#D9D4C7` with grey seams), the stack fitted with a baffle. **No heat shimmer** and no frost-free ring around it. The status lamp stays visible through a cut-out. Thawlings that come near sniff, keep frills flat, and wander off | Fauna signature |
| **Lure station** | Low warm pad with a shallow water trough; gentle shimmer and steam. Thawlings cluster with frills fanned | Fauna signature |
| **Quiet zone** | Not a block the player has to read as "magic". A **marker post** (a survey stake with a round sign showing a sleeping-thawling pictogram), placed by the player to name the area, plus the natural look of land with **no machines**: frost undisturbed, thawling nests, grazing. On the Monitor map, quiet zones are hatched areas | Fauna, biomass |
| **Thawling later life stage** | See style guide §6.2: larger, longer dome, tall frills, broad scraper plate. Still an animal: no teeth, no spikes, no glowing eyes | Hydrosphere gate |

### 3.9 Weather mast (adopted prop: honest world cue for the instruments)

**Status:** adopted by the user (2026-11-26); SME round 2 §5: **plausible**, with the fixes applied below. **Real or fantasy:** real kinds of instrument, simplified. Real landers carried weather masts (Viking, Pathfinder, Phoenix's telltale, Curiosity's REMS, Perseverance's MEDA; SME, not opened). Goals: **A1, A5, B1, C3** (reading an instrument in the world, then checking the Monitor). It is the world-side partner of the Monitor: rough and glanceable, not precise. The Monitor's Air window (style guide §11.2) is the precise instrument.

**Model:** 1 × 1 block footprint, mast 4 blocks tall, alloy ramp with safety-orange bands (style guide §2.2). Silhouette from 30 blocks: a thin pole, an arm with a cone at the top, a round balloon on a line. Readable by shape at a glance; numbers come from the look-at readout (below).

| Part (real name on the label) | Where on the mast | Driving input (data key) | Axis | Keyframes / mapping | Science check |
|---|---|---|---|---|---|
| **Windsock** (cone on an arm) | Top, Y+4 | `mars:pressure` | log10 | Angle 5° / 15° / 35° / 55° / 80° at 0.6 / 5 / 15 / 30 / 75 kPa (§3.1). Sway ±5° at 0.3 Hz; storms +10° (cap 85°) | Accurate (SME §2.7) |
| **Lift balloon** on a tether | Reel at the base; balloon above | `mars:pressure` | log10 | On the ground / 2 / 4 / 6 / 8 blocks up (§3.1); line slack below 5 kPa, taut at 75 | Acceptable simplification (SME §2.7) |
| **Barometer** (dial, 8 × 8 px) | Mast face, Y+2 | `mars:pressure` | log10 | Needle sweeps 270° from 0.5 to 110 kPa, matching the Monitor gauge's log scale (style guide §11.1); notch at 75 kPa | Real instrument (REMS, MEDA pressure sensors). **Added** per SME's optional item: the most direct pressure instrument, strong A1 cue |
| **Spirit thermometer** (12 px column) | Mast face, Y+1.5 | `mars:temperature` | linear | **Red spirit column `#B8322A`** in a clear tube `#D9DEE2` on a dark plate: 1 / 3 / 5 / 8 / 9 px at −60 / −40 / −20 / 0 / 5 °C (§3.2). Notch at 0 °C | **Not mercury:** mercury freezes at −38.8 °C (SME §5), so it couldn't read Mars. Alcohol thermometers are red-dyed; the red is the real dye, not a "hot" color code |
| **Gas analyzer** (two gauges: O2 and CO2) | Box on the mast face, Y+1 | `mars:o2_partial`, `mars:co2_partial` | O2 linear; CO2 square root | Two **indicator gauges**: a vertical tick scale (12 px) with a moving **pointer** (`#E8E3D8` triangle on a `#2A3036` plate), target notch on each. O2: pointer px = clamp(round(12 × pp ÷ 24), 0, 12), notch at 16 kPa. CO2: px = clamp(round(2 × √pp), 1, 12) (matches §3.5), notch at 0.5 kPa | Acceptable as a gauge (SME §5). **Never tubes of tinted gas**: both gases are invisible (§1.2) |
| *(optional, Stage 5)* Radiation sensor | Small dome on the arm | `mars:surface_radiation` | linear | Status lamp blinks at most 1 Hz during spikes | Real analogs (RAD, MEDA); pairs with the dosimeter (A4) |
| Flutter sound, tether creak | — | `mars:pressure` | log10 | Audio direction §3.2 (AU-A11, AU-A12) | — |

**Gas analyzer rules (we don't paint invisible gases, §1.2):**
- Identity by **shape, position, and label**: the O2 ring icon above the left gauge, the CO2 molecule icon above the right (U-01, HUD variant). Pointers and plates are the same neutral colors for both gases.
- The look-at readout says: "Gas analyzer. Both gases are invisible; this shows how much of each there is."
- **Numeric check (2026-11-26):** spirit column `#B8322A` on tube `#D9DEE2` 4.4 : 1, OKLab ΔE ≥ 0.375 in all three colorblind simulations; gauge pointer `#E8E3D8` on plate `#2A3036` 10.4 : 1, ΔE ≥ 0.60. Both pass the icon targets (style guide §4.2, §4.4).
- The breathable O2 window (16–17.25 kPa at 75 kPa) is under one pixel here. **That's fine:** the mast shows direction and rough size; the Monitor's Air window shows the window.

**When is the gas analyzer fitted? Decided: at the Phase 3 gate** (Technical Lead raised it; the Lead Game Designer decided in ui-specs §7; I agree; the Lead Instructional Designer agreed in round 3, because the Phase 1 "Air mix" line already gives A1 its gas-mix source). **Decided.** The gauges are drawn **only after the Phase 3 gate**. The Monitor hides planet O2 and CO2 until Phase 3, so the mast does too: the world and the Monitor reveal the same values at the same time, and the player gets a visible "new instrument" moment (BELL: "Gas gauges fitted to the weather mast") when O2 and CO2 start to matter. Cost: Phase 1–2 lose a world cue for CO2 rising from Outgassers (the Monitor's "read more" still has it).
- **Before Phase 3:** the gas analyzer box is **empty brackets** (two bare mounting rails on the mast face, alloy, no gauge face, no lock symbol, no grey-out). I accept the Game Designer's brackets over my "plain plate": they are a physical object with a visible purpose, not a disabled control, so they fit PROCESS.md §4.
- **At Phase 3:** the gauges appear with a 1 s fade-in and a one-time `#F0E442` outline on the box until the player first looks at the mast.

**Look-at readout** (asset U-29): when the crosshair is on the mast within 8 blocks, a small HUD card on a ≥ 90 % backing strip (style guide §4.4a) lists real instrument names and values, numbers in Cairn Mono (style guide §4.8): "Barometer 3.1 kPa", "Thermometer −58 °C", "Windsock: light wind", and from Phase 3 "Gas analyzer O2 16.2 kPa · CO2 0.48 kPa". Units always; true minus sign (U+2212).

**Placement:** one mast is part of the Cairn Dome structure, visible from a dome window, so a Phase 1 player can read it without going outside. Players can craft more for outposts (recipe: Lead Game Designer).

**Regression:** every part reads the live value, so the windsock droops, the balloon sinks, and the barometer falls if pressure falls (§5).

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
The sky has slid from tan to **rose-tan** (`#A0736C`), haze a little closer, windsock lifts to 15°, balloon off the ground, exterior sounds slightly louder. Frost unchanged (temperature still −60). Ice right next to heaters still gives **flash puddles** (local temperature above the boiling point, now about 33 °C at 5 kPa); a little farther from the heat, warmed ice can now leave a puddle that lasts while it stays warm, but most ground is still far below 0 °C. The barometer needle passes the Phase 2 notch. The 30 s smoothing makes the shift happen over about two minutes after the gate; story beat 7 plays as it does.

### 4.4 Thaw (−20 °C, ~22 kPa) — "Wonder, then unease"
Sky **mauve-lavender** (between 15 and 30 kPa keys), fog noticeably closer. Frost only on 35 % of the ground and gone by mid-morning. **Day puddles that freeze at night** around warm machines; ice-crack sounds. Heat shimmer at emitters. Thawling **tracks** in the dust near machines, then the first juveniles with frills fanned toward the heat. Fridge B's padlock hangs open.

### 4.5 Hydrosphere (10 %) — "Conflict peaks"
Clouds thicker and lower (20 %), morning ground fog by water. **Pools** with no wisps where it stays warm; water still silty grey-teal `#4F6A70`. Adults with tall frills scrape at machines (scrape overlays). **Cracked ground** around warm machines, first collapses. Salt crust where meltwater dried. Shrouded machines look quilted and cold; lure stations steam.

### 4.6 Phase 3 begins (30 kPa, 0 °C) — "Storms fade"
Sky **slate-lavender blue** `#727EAC`; sunset halo has turned neutral-warm (`#CCA388`). Storms capped at half strength. Frost only in shade. Water stable, no wisps, bluer. First treated soil and **biocrust** patches near seed kits.

### 4.7 Breathable (O2 pp ≥ 16 kPa, ≥ 75 kPa, CO2 ≤ 0.5 kPa) — "Breathe"
Sky blue, clouds and rain, moss and grass spreading, bubbles in ponds, normal flames. The change is **the helmet coming off** (style guide §9.2): the world was already this color; the sound world opens.

### 4.8 Equilibrium — "Earned relief"
Bright, pale blue zenith `#6A93CF`, whitish horizon `#B8CDE2`, **orange sunsets** (`#F2A35E` halo, `#D9714F` band): the reverse of the blue Mars sunset at the start. Regular rain, green ground, settled thawlings nesting in quiet zones, spore drift, full sound. The beacon sequence plays in this world (style guide §10).

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
| ~~Sky path; air column~~ Done (SME round 2): path accepted, label required (§2.3), "about 2×", paler equilibrium blue adopted | SME | Closed |
| Replace estimated start colors with pixel samples from calibrated rover images | SME + Lead Artist | Well-structured |
| ~~Flame bands~~ Done (SME round 2): corrected (§3.4); 20 kPa guard is invented, needs a sourced value if one exists | SME | Open for a source only |
| ~~Outgasser puff, wisps, carbonate, windsock, aurora~~ Done (SME round 2): accurate or corrected here | SME | Closed |
| ~~Data keys; solar-spike signal~~ Done (ARCHITECTURE.md §2.5.2; `mars:solar_spike` added in TL round 3) | Technical Lead | Closed |
| Weather mast: **adopted** (user, 2026-11-26). Add it to DESIGN.md (Index section or hazards) and the fantasy audit as "real instrument, simplified"; place one in the Cairn Dome structure | Lead Game Designer | Well-structured |
| Pressure-driven sound claim and values (§3.1 sound rows, audio direction §3) | SME | Well-structured (check) |
| ~~Clouds~~ Decided (TL, option A): one 256×256 greyscale density map, delivered as a generated concept (asset E-10) | — | Closed |
| ~~Mast gas analyzer timing~~ Decided: Phase 3 gate (§3.9) | — | Closed |
| Pixel-sample calibrated rover images (approximate-true-color Pancam, Mastcam natural color; avoid white-balanced) at several dust opacities now that pds-imaging.jpl.nasa.gov is reachable; replace the estimated start colors if they differ by more than ΔE 0.02, then re-run the step checks | SME + Lead Artist (optional) | Well-structured |
